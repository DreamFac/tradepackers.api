package services;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.DateTime;

import constants.UserLoginStatus;
import models.User;
import models.security.Token;
import play.Logger;
import play.mvc.Http;
import repositories.interfaces.TokenRepository;
import repositories.interfaces.UserRepository;
import services.base.AbstractService;
import services.interfaces.UserService;
import utils.Cripto;

/**
 * Created by eduardo on 4/08/16.
 */
@Singleton
public class UserAuthService extends AbstractService<User> implements UserService
{

  UserRepository userRepository;
  TokenRepository tokenRepository;
  private Optional<User> userResult;

  @Inject
  public UserAuthService(final UserRepository userRepository, final TokenRepository tokenRepository)
  {
    super(userRepository);
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public Optional<User> finByEmail(final String email)
  {
    return this.userRepository.findByEmail(email);
  }

  @Override
  public Optional<Token> getUserToken(final User user)
  {
    return this.tokenRepository.findTokenByUserId(user.getId());
  }

  @Override
  public Optional<Token> login(final String email, final String password)
  {
    final Optional<User> userResult = this.findByEmailAndPassword(email, password);

    if (!userResult.isPresent())
    {
      Logger.error("[{}] User with email: {} doesn't exist.", getClass(), email);
      return Optional.empty();
    }

    final User user = userResult.get();
    final String authToken = UUID.randomUUID().toString();
    final Optional<Token> tokenResult = this.tokenRepository.findTokenByUserId(user.getId());

    Token token = null;

    if (!tokenResult.isPresent())
    {
      token = new Token();
      token.setStatus(UserLoginStatus.NEW);
    }
    else
    {
      token = tokenResult.get();
      token.setStatus(UserLoginStatus.ACTIVE);
    }

    token.setAuthToken(authToken);
    token.setExpirationDate(DateTime.now().plusDays(1).toDate());
    token.setUser(user);

    final Optional<Token> savedTokenResult = this.tokenRepository.persist(token);

    if (!savedTokenResult.isPresent())
    {
      Logger.error("[{}] Something went wrong couldn't login", getClass());
      return Optional.empty();
    }
    Logger.debug("[{}] User with email: {} successfully logedIn.", getClass(), email);
    return Optional.ofNullable(token);
  }

  @Override
  public Optional<User> findByEmailAndPassword(final String email, final String password)
  {
    return this.userRepository.findByEmailAndPassword(email, Cripto.getMD5(password));
  }

  @Override
  public boolean isTokenExpired(final Token token)
  {
    final Long expired = token.expirationDate.getTime();
    final Long now = DateTime.now().getMillis();
    if (now > expired)
    {
      return true;
    }
    return false;
  }

  @Override
  public void logout(final Long userId)
  {
    final Optional<Token> tokenResult = this.tokenRepository.findTokenByUserId(userId);
    if (tokenResult.isPresent())
    {
      final Token token = tokenResult.get();
      token.setAuthToken(null);
      token.setStatus(UserLoginStatus.OUT);
      this.tokenRepository.persist(token);
      Logger.debug("[{}] User with userId: {} logged out", getClass(), userId);
    }
    else
    {
      Logger.error("[{}] No token found for userId: {}", getClass(), userId);
    }
  }

  @Override
  public Optional<User> getLoggedInUser()
  {
    return Optional.ofNullable((User) Http.Context.current().args.get("user"));
  }

  @Override
  public Optional<User> create(final User user)
  {
    final String password = Cripto.getMD5(user.getPassword());
    user.setPassword(password);
    Logger.debug("[{}] Creating user: {}", getClass(), user.getEmail());
    return this.save(user);
  }
}
