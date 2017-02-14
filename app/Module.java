import repositories.JpaBadgeRepository;
import repositories.JpaRegionRepository;
import repositories.JpaTeamRepository;
import repositories.JpaTokenRepository;
import repositories.JpaUserProviderRepository;
import repositories.JpaUserRepository;
import repositories.interfaces.BadgeRepository;
import repositories.interfaces.RegionRepository;
import repositories.interfaces.TeamRepository;
import repositories.interfaces.TokenRepository;
import repositories.interfaces.UserProviderRepository;
import repositories.interfaces.UserRepository;
import services.BadgeServiceImpl;
import services.OauthIoServiceImpl;
import services.RegionServiceImpl;
import services.TeamServiceImpl;
import services.UserAuthServiceImpl;
import services.UserProviderServiceImpl;
import services.interfaces.BadgeService;
import services.interfaces.OauthIoService;
import services.interfaces.RegionService;
import services.interfaces.TeamService;
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
    bind(TeamRepository.class).to(JpaTeamRepository.class);
    bind(BadgeRepository.class).to(JpaBadgeRepository.class);
    bind(RegionRepository.class).to(JpaRegionRepository.class);


    // Services
    bind(UserAuthService.class).to(UserAuthServiceImpl.class);
    bind(OauthIoService.class).to(OauthIoServiceImpl.class);
    bind(UserProviderService.class).to(UserProviderServiceImpl.class);
    bind(TeamService.class).to(TeamServiceImpl.class);
    bind(BadgeService.class).to(BadgeServiceImpl.class);
    bind(RegionService.class).to(RegionServiceImpl.class);

  }
}
