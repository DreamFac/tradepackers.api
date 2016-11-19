package controllers;

import actions.AuthenticationAction;
import models.User;
import models.security.Token;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.interfaces.OauthIoService;
import services.interfaces.UserAuthService;
import services.interfaces.UserProviderService;
import steel.dev.oauthio.wrapper.RequestObject;
import steel.dev.oauthio.wrapper.config.OauthOptions;
import steel.dev.oauthio.wrapper.exceptions.NotAuthenticatedException;
import steel.dev.oauthio.wrapper.exceptions.NotInitializedException;
import utils.ResponseBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by eduardo on 1/11/16.
 */

public class SocialAuthController extends Controller
{
  public OauthIoService oauthService;
  public UserProviderService userProviderService;
  public UserAuthService userAuthService;

  @Inject
  public SocialAuthController(final OauthIoService oauthService,
      final UserProviderService userProviderService,

      final UserAuthService userAuthService)
  {
    this.oauthService = oauthService;
    this.userProviderService = userProviderService;
    this.userAuthService = userAuthService;
  }

  public Result getLoginUrl(final String provider)
  {
    final String url = this.oauthService.getLoginUrl(provider);
    final ObjectNode json = Json.newObject();
    json.put("url", url);
    return ok(json);
  }

  public Result socialLogin()
  {
    final OauthOptions oauthOptions = new OauthOptions();
    //Getting request
    final Map<String, String[]> query = request().queryString();
    //parsing request
    final JsonNode json = Json.parse(query.get("oauthio")[0]);
    //getting provider
    final String provider = json.get("provider").toString();
    //get request data
    final JsonNode data = json.get("data");

    //Setting option code to be verified
    oauthOptions.setCode(data.get("code").textValue());

    //authenticating user with provider
    RequestObject oauth = null;
    try
    {
      oauth = this.oauthService.auth(provider, oauthOptions);
    }
    catch (final NotAuthenticatedException e)
    {
      return unauthorized(e.getMessage());
    }
    catch (final NotInitializedException e)
    {
      return badRequest(e.getMessage());
    }

    final JsonNode result = Json
        .parse(
            oauth
                .me("id", "name", "email")
                .getBody()
                .toString()
              );

    final JsonNode userInfo = result.get("data");
/*
    final Long userProviderId = userInfo.get("id").asLong();

    final JsonNode credentials = Json.parse(oauth.getCredentials().toString());

    final Optional<UserProvider> userProviderOptional = this.userProviderService
        .findByProviderId(userProviderId);

    if (userProviderOptional.isPresent())
    {
      final UserProvider userProvider = userProviderOptional.get();
      userProvider.setCredentials(credentials.textValue());

      final Optional<User> userOptional = this.userAuthService
          .findById(userProvider.getUser().getId());

      this.userProviderService.save(userProvider);

      if (userOptional.isPresent())
      {
        final User user = userOptional.get();

        return processLogin(user);
      }

    }
    else
    {
      if (userInfo.has("email"))
      {
        final User user = new User();
        user.setEmail(userInfo.get("email").textValue());
        final Optional<User> createdUser = this.userAuthService.save(user);

        if (createdUser.isPresent())
        {
          final UserProvider userProvider = new UserProvider();
          userProvider.setUser(createdUser.get());
          userProvider.setProviderId(userProviderId);
          userProvider.setName(provider);
          userProvider.setCredentials(credentials.toString());
          if (this.userProviderService.save(userProvider).isPresent())
          {
            return processLogin(user);
          }

        }
      }
    }
*/
    return ok(Json.asciiStringify(userInfo));

  }

  private Result processLogin(final User user)
  {
    final Optional<Token> tokenOptional = this.userAuthService.login(user.getId());

    if (tokenOptional.isPresent())
    {
      return ok(Json
          .newObject()
          .put(AuthenticationAction.AUTH_TOKEN,
              tokenOptional.get().getAuthToken()));
    }
    else
    {
      return status(UNAUTHORIZED,
          Json.toJson(
              ResponseBuilder
                  .buildErrorResponse(
                      Arrays.asList("Cannot login"),
                      UNAUTHORIZED)));
    }
  }

}
