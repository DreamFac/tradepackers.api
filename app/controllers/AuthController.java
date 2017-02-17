package controllers;

import actions.Authentication;
import constants.StatusCode;
import dtos.LoginDTO;
import dtos.TokenDTO;
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
import services.interfaces.UserAuthService;
import utils.ResponseBuilder;

import java.util.Arrays;
import java.util.Optional;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by eduardo on 4/08/16.
 */
public class AuthController extends Controller
{
  private final UserAuthService userAuthService;
  FormFactory formFactory;

  @Inject
  public AuthController(final UserAuthService userAuthService, final FormFactory formFactory)
  {
    this.userAuthService = userAuthService;
    this.formFactory = formFactory;
  }

  @Transactional
  public Result signUp()
  {
    final Form<LoginDTO> form = this.formFactory.form(LoginDTO.class).bindFromRequest();
    if (form.hasErrors())
    {
      final ErrorDTO errorDTO = ResponseBuilder.buildErrorResponse(form.errorsAsJson(),
          BAD_REQUEST);
      final JsonNode jsonError = Json.toJson(errorDTO);
      Logger.error("[{}] Form errors: {}", getClass(), jsonError);
      return badRequest(jsonError);
    }
    else
    {
      final LoginDTO loginDTO = form.get();
      final Optional<User> userResult = this.userAuthService.finByEmail(loginDTO.getEmail());

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
      final Optional<User> savedUser = this.userAuthService.create(user);

      if (!savedUser.isPresent())
      {
        final String errorMessage =
            String.format("Something went wrong when trying to save the user: [%s] after signup",
                userResult.get().getEmail());
        Logger.error("[{}] {}", getClass(), errorMessage);
        final ErrorDTO errorDTO = ResponseBuilder.buildErrorResponse(form.errorsAsJson(),
            INTERNAL_SERVER_ERROR);
        final JsonNode jsonError = Json.toJson(errorDTO);
        return status(INTERNAL_SERVER_ERROR, jsonError);
      }
      else
      {
        return processLogin(loginDTO.getEmail(), loginDTO.getPassword());
      }
    }
  }

  private Result processLogin(final String email, final String password)
  {

    final Optional<Token> tokenResult = this.userAuthService.login(email, password);

    if (!tokenResult.isPresent())
    {
      Logger.error("[{}] Internal server error, couldn't login: ", getClass());

      final ErrorDTO errorDTO = ResponseBuilder
          .buildErrorResponse(Arrays.asList("Couldn't login"), StatusCode.BAD_CREDENTIALS);

      return status(INTERNAL_SERVER_ERROR, Json.toJson(errorDTO));
    }

    final Token token = tokenResult.get();
    final TokenDTO tokenDTO = TokenDTO.builder()
        .token(token.getAuthToken())
        .expirationDate(token.getExpirationDate())
        .userId(token.getUser().getId())
        .build();
    
    return ok(Json.toJson(tokenDTO));
  }

  @Transactional
  public Result login()
  {

    final Form<LoginDTO> loginForm = this.formFactory.form(LoginDTO.class).bindFromRequest();

    if (loginForm.hasErrors())
    {
      final ErrorDTO errorDTO = ResponseBuilder.buildErrorResponse(loginForm.errorsAsJson(),
          BAD_REQUEST);
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

    final Optional<User> userResulr = this.userAuthService.getLoggedInUser();
    if (userResulr.isPresent())
    {
      final String userId = userResulr.get().getId();
      this.userAuthService.logout(userResulr.get().getId().toString());
      Logger.debug("[{}] user id: [{}] logged out", getClass(), userId);
      return ok("logged out");
    }
    Logger.error("[{}] there is not logged in user", getClass());
    return internalServerError("there is not logged in user");

  }

}
