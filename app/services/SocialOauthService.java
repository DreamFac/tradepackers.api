package services;

import services.interfaces.OauthService;
import steel.dev.oauthio.wrapper.OAuth;
import steel.dev.oauthio.wrapper.RequestObject;
import steel.dev.oauthio.wrapper.config.OauthOptions;
import steel.dev.oauthio.wrapper.exceptions.NotAuthenticatedException;
import steel.dev.oauthio.wrapper.exceptions.NotInitializedException;

import javax.inject.Singleton;

/**
 * Created by eduardo on 1/11/16.
 */
@Singleton
public class SocialOauthService implements OauthService
{
  OAuth oAuth;

  public SocialOauthService()
  {
    this.oAuth = new OAuth();
    this.oAuth.properties.setPort("9000");
    this.oAuth.setOAuthUrl("http://oauth.asuramedia.com:6284", "auth");
    this.oAuth.initialize("Cije2ddbivnUsHXIOJadBQpn6OA", "fvgzrkqoSV12GKEQmO-L8VUywIg");

  }

  @Override
  public String getLoginUrl(final String provider)
  {
    return this.oAuth.redirect(provider, "/social/login");
  }

  @Override
  public RequestObject auth(final String provider, final OauthOptions options) throws NotAuthenticatedException, NotInitializedException
  {
    return this.oAuth.auth(provider, options);

  }

  @Override
  public RequestObject auth(final String provider) throws NotAuthenticatedException, NotInitializedException
  {
    return this.oAuth.auth(provider, null);
  }
}
