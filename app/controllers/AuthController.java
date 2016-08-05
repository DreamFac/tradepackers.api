package controllers;

import java.util.Optional;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.Authentication;
import actions.AuthenticationAction;
import constants.StatusCode;
import dtos.LoginDTO;
import models.User;
import models.security.Token;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.interfaces.UserService;

/**
 * Created by eduardo on 4/08/16.
 */
public class AuthController extends Controller
{
  private final UserService userService;
  FormFactory formFactory;

  @Inject
  public AuthController(final UserService userService)
  {
    this.userService = userService;
  }

  public Result signUp()
  {
    final Form<LoginDTO> form = this.formFactory.form(LoginDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      return badRequest(form.errorsAsJson());
    }
    else
    {
      final LoginDTO loginDTO = form.get();
      final Optional<User> userResult = this.userService.finByEmail(loginDTO.email);
      if (userResult.isPresent())
      {
        return status(StatusCode.USER_EXISTS, "User already exists");
      }
      final User user = new User();
      user.setEmail(loginDTO.getEmail());
      user.setPassword(loginDTO.getPassword());
      final Optional<User> savedUser = this.userService.save(user);
      if (!savedUser.isPresent())
      {
        return status(StatusCode.BAD_CREDENTIALS);
      }
      else
      {
        return verifyUser(savedUser.get());
      }
    }
  }

  public Result login()
  {
    final Form<LoginDTO> loginForm = this.formFactory.form(LoginDTO.class).bindFromRequest();

    if (loginForm.hasErrors())
    {
      return badRequest(loginForm.errorsAsJson());
    }

    final LoginDTO loginDTO = loginForm.get();

    final Optional<User> userResult =
        this.userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());

    if (!userResult.isPresent())
    {
      return status(StatusCode.BAD_CREDENTIALS);
    }
    else
    {
      return verifyUser(userResult.get());
    }
  }

  @Authentication
  public Result logout()
  {

    final Optional<User> userResulr = this.userService.getLoggedInUser();
    if (userResulr.isPresent())
    {
      this.userService.logout(userResulr.get().getId());
      return ok("logged out");
    }
    return internalServerError("Please try again something went wrong");

  }

  private Result verifyUser(final User user)
  {
    final Optional<User> userResult = this.userService.login(user);
    if (!userResult.isPresent())
    {
      return status(StatusCode.TOKEN_EXPIRED);
    }
    final Optional<Token> tokenResult = this.userService.getUserToken(userResult.get());

    if (!tokenResult.isPresent())
    {
      return status(StatusCode.TOKEN_EXPIRED);
    }

    final Token token = tokenResult.get();
    final ObjectNode authTokenJson = Json.newObject();
    authTokenJson.put("id", user.getId());
    authTokenJson.put("name", user.firstName + " " + user.lastName);
    authTokenJson.put(AuthenticationAction.AUTH_TOKEN, token.getAuthToken());
    return ok(authTokenJson);
  }

}
