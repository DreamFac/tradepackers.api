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
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
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
  public AuthController(final UserService userService, final FormFactory formFactory)
  {
    this.userService = userService;
    this.formFactory = formFactory;
  }

  @Transactional
  public Result signUp()
  {
    final Form<LoginDTO> form = this.formFactory.form(LoginDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      Logger.error("[{}] Form errors: {}", getClass(), form.errorsAsJson());
      return badRequest(form.errorsAsJson());
    }
    else
    {
      final LoginDTO loginDTO = form.get();
      final Optional<User> userResult = this.userService.finByEmail(loginDTO.getEmail());

      if (userResult.isPresent())
      {
        Logger.error("[{}] User id [{}] alrady exist: ", getClass(), userResult.get().getId());
        return status(StatusCode.USER_EXISTS, "User already exists");
      }

      final User user = new User();
      user.setEmail((loginDTO.getEmail()));
      user.setPassword(loginDTO.getPassword());
      final Optional<User> savedUser = this.userService.create(user);

      if (!savedUser.isPresent())
      {
        Logger.error("[{}] Something went wrong when trying to save the user: [{}] after signup",
            getClass(), loginDTO.getEmail());
        return internalServerError();
      }
      else
      {
        return processLogin(loginDTO.getEmail(), loginDTO.getPassword());
      }
    }
  }

  @Transactional
  public Result login()
  {

    final Form<LoginDTO> loginForm = this.formFactory.form(LoginDTO.class).bindFromRequest();

    if (loginForm.hasErrors())
    {
      Logger.error("[{}] Form errors: {}", getClass(), loginForm.errorsAsJson());
      return badRequest(loginForm.errorsAsJson());
    }

    final LoginDTO loginDTO = loginForm.get();

    return processLogin(loginDTO.getEmail(), loginDTO.getPassword());

  }

  @Transactional
  @Authentication
  public Result logout()
  {

    final Optional<User> userResulr = this.userService.getLoggedInUser();
    if (userResulr.isPresent())
    {
      final long userId = userResulr.get().getId();
      this.userService.logout(userResulr.get().getId());
      Logger.error("[{}] user id: [{}] logged out", getClass(), userId);
      return ok("logged out");
    }
    Logger.error("[{}] there is not logged in user", getClass());
    return internalServerError("there is not logged in user");

  }

  private Result processLogin(final String email, final String password)
  {

    final ObjectNode jsonResponse = Json.newObject();
    final Optional<Token> tokenResult = this.userService.login(email, password);

    if (!tokenResult.isPresent())
    {
      Logger.error("[{}] Internal server error, couldn't login: ", getClass());
      return internalServerError("Internal server error, couldn't login:");
    }

    final Token token = tokenResult.get();

    jsonResponse.put(AuthenticationAction.AUTH_TOKEN, token.getAuthToken());

    return ok(jsonResponse);
  }

}
