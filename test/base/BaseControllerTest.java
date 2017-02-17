package base;

import static play.test.Helpers.*;

import controllers.routes;
import dtos.TokenDTO;
import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import services.interfaces.UserAuthService;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by eduardo on 6/08/16.
 */
public class BaseControllerTest extends BaseTest
{

  protected static final String USER_EMAIL = "user1@test.com";
  protected static final String USER_PASSWORD = "PASSWORD1";
  protected final String LOGIN = routes.AuthController.login().url();
  protected final String SIGNUP = routes.AuthController.signUp().url();
  protected TokenDTO tokenDTO;
  @Inject
  protected UserAuthService userAuthService;


  @Before
  public void setup()
  {
    super.setup();
    loadUserData();
    loggInUser();
  }


  public void loadUserData()
  {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    user.setPassword(USER_PASSWORD);
    this.userAuthService.create(user);
  }

  public void loggInUser(){
    final ObjectNode node = Json.newObject();
    node.put("email", USER_EMAIL);
    node.put("password", USER_PASSWORD);

    final Http.RequestBuilder requestBuilder = new Http.RequestBuilder();
    requestBuilder.bodyJson(node);
    requestBuilder.method(POST);
    requestBuilder.uri(this.LOGIN);

    final Result result = route(requestBuilder);
    tokenDTO = Json.fromJson(Json.parse(contentAsString(result)), TokenDTO.class);

  }

  @After
  public void tearDown()
  {
    super.teardown();
  }

}
