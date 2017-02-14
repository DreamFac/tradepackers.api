package services.interfaces;

import models.User;
import models.security.Token;

import java.util.Optional;

/**
 * Created by eduardo on 4/08/16.
 */
public interface UserAuthService extends GenericService<User>
{
  Optional<User> findByEmailAndPassword(String email, String password);

  Optional<User> finByEmail(String email);

  Optional<Token> getUserToken(User user);

  Optional<Token> login(String email, final String password);

  Optional<Token> login(String userId);

  Optional<User> create(User user);

  boolean isTokenExpired(Token token);

  void logout(String userId);

  Optional<User> getLoggedInUser();
}
