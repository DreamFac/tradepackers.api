package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.interfaces.OauthService;
import steel.dev.oauthio.wrapper.RequestObject;
import steel.dev.oauthio.wrapper.config.OauthOptions;
import steel.dev.oauthio.wrapper.exceptions.NotAuthenticatedException;
import steel.dev.oauthio.wrapper.exceptions.NotInitializedException;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by eduardo on 1/11/16.
 */

public class SocialAuthController extends Controller
{
  public OauthService oauthService;

  @Inject
  public SocialAuthController(final OauthService oauthService)
  {
    this.oauthService = oauthService;
  }

  public Result getLoginUrl(final String provider)
  {
    final String     url  = this.oauthService.getLoginUrl(provider);
    final ObjectNode json = Json.newObject();
    json.put("url", url);
    return ok(json);
  }

  public Result socialLogin()
  {
    final OauthOptions oauthOptions = new OauthOptions();
    //Getting request
    final Map<String, String[]> query = request().queryString();
    Logger.debug("Query from request: {}", query);
    //parsing request
    final JsonNode json = Json.parse(query.get("oauthio")[0]);
    //getting provider
    final String provider = json.get("provider")
                                .toString();
    //get request data
    final JsonNode data = json.get("data");

    //Setting option code to be verified
    oauthOptions.setCode(data.get("code")
                             .textValue());

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

    
    final JsonNode meData = Json.parse(oauth.me("id", "name", "firstname", "lastname", "alias", "email")
                                            .getBody()
                                            .toString());


    return ok(Json.asciiStringify(meData));

  }

}
