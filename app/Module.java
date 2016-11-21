import repositories.JpaTokenRepository;
import repositories.JpaUserProviderRepository;
import repositories.JpaUserRepository;
import repositories.interfaces.TokenRepository;
import repositories.interfaces.UserProviderRepository;
import repositories.interfaces.UserRepository;
import services.OauthIoServiceImpl;
import services.UserAuthServiceImpl;
import services.UserProviderServiceImpl;
import services.interfaces.OauthIoService;
import services.interfaces.UserAuthService;
import services.interfaces.UserProviderService;

import com.google.inject.AbstractModule;

/**
 * Created by eduardo on 4/08/16.
 */
public class Module extends AbstractModule
{
  @Override
  protected void configure()
  {
    // Repositories
    bind(UserRepository.class).to(JpaUserRepository.class);
    bind(TokenRepository.class).to(JpaTokenRepository.class);
    bind(UserProviderRepository.class).to(JpaUserProviderRepository.class);


    // Services
    bind(UserAuthService.class).to(UserAuthServiceImpl.class);
    bind(OauthIoService.class).to(OauthIoServiceImpl.class);
    bind(UserProviderService.class).to(UserProviderServiceImpl.class);
  }
}
