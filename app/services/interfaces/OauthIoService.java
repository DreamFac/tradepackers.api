package services.interfaces;

import steel.dev.oauthio.wrapper.RequestObject;
import steel.dev.oauthio.wrapper.config.OauthOptions;
import steel.dev.oauthio.wrapper.exceptions.NotAuthenticatedException;
import steel.dev.oauthio.wrapper.exceptions.NotInitializedException;

/**
 * Created by eduardo on 1/11/16.
 */

public interface OauthIoService
{
  String getLoginUrl(String provider);

  RequestObject auth(String provider, OauthOptions options) throws NotAuthenticatedException, NotInitializedException;

  RequestObject auth(String provider) throws NotAuthenticatedException, NotInitializedException;

}
