import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

import constants.UserLoginStatus;
import models.User;
import models.security.Token;
import play.Logger;
import services.interfaces.UserService;

/**
 * Created by eduardo on 6/08/16.
 */
public class UserAUthenticationIT extends BaseTest
{

  private static final String USER_EMAIL = "user1@test.com";
  private static final String USER_PASSWORD = "PASSWORD1";

  private static final String USER_EMAIL_SIGNUP = "user2@test.com";
  private static final String USER_PASSWORD_SIGNUP = "PASSWORD2";

  @Inject
  UserService userService;

  @Before
  public void loadData()
  {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    user.setPassword(USER_PASSWORD);
    this.userService.create(user);
  }

  @Test
  public void testSignUp()
  {
    Logger.debug("[{}] Inside signup test", getClass());
    final User user = new User();
    user.setEmail(USER_EMAIL_SIGNUP);
    user.setPassword(USER_PASSWORD_SIGNUP);
    final Optional<User> userResult = this.userService.create(user);

    assertTrue(userResult.isPresent());
    Logger.debug("[{}] User with email: [{}] created", getClass(), userResult.get().getEmail());

    final Optional<Token> tokenResult =
        this.userService.login(USER_EMAIL_SIGNUP, USER_PASSWORD_SIGNUP);
    Logger.debug("[{}] Logging in User with email: [{}]", getClass(), userResult.get().getEmail());
    assertTrue(tokenResult.isPresent());
    assertEquals(tokenResult.get().getStatus(), UserLoginStatus.NEW);
    Logger.debug("[{}] User with email: [{}] successfully Logged in", getClass(),
        userResult.get().getEmail());
  }

  @Test
  public void testLogin()
  {
    Logger.debug("[{}] Inside login test", getClass());
    final Optional<Token> tokenResult =
        this.userService.login(USER_EMAIL, USER_PASSWORD);

    assertTrue(tokenResult.isPresent());
    assertEquals(tokenResult.get().getStatus(), UserLoginStatus.NEW);
    Logger.debug("[{}] User with email: [{}] successfully Logged in", getClass(), USER_EMAIL);
  }

  @Test
  public void testLogout()
  {
    Logger.debug("[{}] Inside logout test", getClass());

    final Optional<Token> tokenResultLogin = this.userService.login(USER_EMAIL, USER_PASSWORD);

    assertTrue(tokenResultLogin.isPresent());
    Logger.debug("[{}] User with email: [{}] successfully Logged in", getClass(), USER_EMAIL);
    final Token token = tokenResultLogin.get();
    final User userLoggedIn = token.getUser();
    this.userService.logout(userLoggedIn.getId());
    final Optional<Token> tokenOut = this.userService.getUserToken(userLoggedIn);
    assertTrue(tokenOut.isPresent());
    assertEquals(tokenOut.get().getStatus(), UserLoginStatus.OUT);
    Logger.debug("[{}] User with email: [{}] successfully Logged out", getClass(), USER_EMAIL);

  }

}
