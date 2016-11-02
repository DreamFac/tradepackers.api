import com.google.inject.AbstractModule;
import repositories.JpaTokenRepository;
import repositories.JpaUserRepository;
import repositories.interfaces.TokenRepository;
import repositories.interfaces.UserRepository;
import services.SocialOauthService;
import services.UserAuthService;
import services.interfaces.OauthService;
import services.interfaces.UserService;

/**
 * Created by eduardo on 4/08/16.
 */
public class Module extends AbstractModule
{
  @Override
  protected void configure()
  {
    // Repositories
    bind(UserRepository.class).to(JpaUserRepository.class).asEagerSingleton();
    bind(TokenRepository.class).to(JpaTokenRepository.class).asEagerSingleton();
    bind(OauthService.class).to(SocialOauthService.class)
                            .asEagerSingleton();

    // Services
    bind(UserService.class).to(UserAuthService.class);
  }
}
