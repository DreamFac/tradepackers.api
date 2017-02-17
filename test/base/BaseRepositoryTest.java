package base;

import models.User;
import services.interfaces.UserAuthService;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;

/**
 * Created by eduardo on 6/08/16.
 */
public class BaseRepositoryTest extends BaseTest
{

  protected static final String USER_EMAIL = "user1@test.com";
  protected static final String USER_PASSWORD = "PASSWORD1";

  protected static final String USER_EMAIL_SIGNUP = "user2@test.com";
  protected static final String USER_PASSWORD_SIGNUP = "PASSWORD2";
  @Inject
  protected UserAuthService userAuthService;

  @Before
  public void setup()
  {
    super.setup();
    loadUserData();
  }

  public void loadUserData()
  {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    user.setPassword(USER_PASSWORD);
    this.userAuthService.create(user);
  }

  @After
  public void tearDown(){
    super.teardown();
  }

}
