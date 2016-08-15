package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.Authentication;
import actions.AuthenticationAction;
import constants.StatusCode;
import dtos.LoginDTO;
import dtos.errors.ErrorDTO;
import dtos.errors.ErrorMessage;
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

  private ErrorDTO buildError(final JsonNode errors, final int code)
  {

    final List<ErrorMessage> errorList = new ArrayList<>();

    errors.fields()
        .forEachRemaining((fields) ->
        {
          final String cause = fields.getKey();
          for (final JsonNode jsonNode : fields.getValue())
          {
            errorList.add(ErrorMessage.builder()
                .value(cause.toUpperCase() + ", " + jsonNode.asText().toLowerCase())
                .build());
          }
        });
    final ErrorDTO errorDTO = ErrorDTO.builder()
        .status(code)
        .message(errorList)
        .build();
    return errorDTO;
  }

  @Transactional
  public Result signUp()
  {
    final Form<LoginDTO> form = this.formFactory.form(LoginDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      final ErrorDTO errorDTO = buildError(form.errorsAsJson(), BAD_REQUEST);
      final JsonNode jsonError = Json.toJson(errorDTO);
      Logger.error("[{}] Form errors: {}", getClass(), jsonError);
      return badRequest(jsonError);
    }
    else
    {
      final LoginDTO loginDTO = form.get();
      final Optional<User> userResult = this.userService.finByEmail(loginDTO.getEmail());

      if (userResult.isPresent())
      {

        final String errorMessage =
            String.format("User with email %s already exists", userResult.get().getEmail());
        Logger.error("[{}] {} ", getClass(), errorMessage);

        return status(StatusCode.USER_EXISTS,
            Json.toJson(ErrorDTO.builder().status(StatusCode.USER_EXISTS)
                .message(Arrays.asList(ErrorMessage.builder()
                    .value(errorMessage)
                    .build()))
                .build()));
      }

      final User user = new User();
      user.setEmail((loginDTO.getEmail()));
      user.setPassword(loginDTO.getPassword());
      final Optional<User> savedUser = this.userService.create(user);

      if (!savedUser.isPresent())
      {
        final String errorMessage =
            String.format("Something went wrong when trying to save the user: [%s] after signup",
                userResult.get().getEmail());
        Logger.error("[{}] {}", getClass(), errorMessage);
        final ErrorDTO errorDTO = buildError(form.errorsAsJson(), INTERNAL_SERVER_ERROR);
        final JsonNode jsonError = Json.toJson(errorDTO);
        return internalServerError(jsonError);
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
      final ErrorDTO errorDTO = buildError(loginForm.errorsAsJson(), BAD_REQUEST);
      final JsonNode jsonError = Json.toJson(errorDTO);
      Logger.error("[{}] Form errors: {}", getClass(), jsonError);
      return badRequest(jsonError);
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
      Logger.debug("[{}] user id: [{}] logged out", getClass(), userId);
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
