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
import utils.Cripto;

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
      final Optional<User> userResult = this.userService.finByEmail(loginDTO.email);
      if (userResult.isPresent())
      {
        Logger.error("[{}] User id [{}] alrady exist: ", getClass(), userResult.get().getId());
        return status(StatusCode.USER_EXISTS, "User already exists");
      }
      final User user = new User();
      user.setEmail(loginDTO.getEmail());
      user.setPassword(Cripto.getMD5(loginDTO.getPassword()));
      final Optional<User> savedUser = this.userService.save(user);
      if (!savedUser.isPresent())
      {
        Logger.error("[{}] Something went wrong when trying to save the user: [{}] after signup",
            getClass(), user.getEmail());
        return internalServerError();
      }
      else
      {
        return verifyUser(savedUser.get());
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

    final Optional<User> userResult =
        this.userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());

    if (!userResult.isPresent())
    {
      Logger.error("[{}] User does not exist: ", getClass());
      return notFound("User does not exist");
    }
    else
    {
      return verifyUser(userResult.get());
    }
  }

  @Transactional
  @Authentication
  public Result logout()
  {

    final Optional<User> userResulr = this.userService.getLoggedInUser();
    if (userResulr.isPresent())
    {
      long userId = userResulr.get().getId();
      this.userService.logout(userResulr.get().getId());
      Logger.error("[{}] user id: [{}] logged out", getClass(), userId);
      return ok("logged out");
    }
    Logger.error("[{}] there is not logged in user", getClass());
    return internalServerError("there is not logged in user");

  }

  @Transactional
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
    authTokenJson.put(AuthenticationAction.AUTH_TOKEN, token.getAuthToken());
    return ok(authTokenJson);
  }

}
