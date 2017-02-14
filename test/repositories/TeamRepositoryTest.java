package repositories;

import base.BaseAuthenticationTest;
import models.Badge;
import models.Region;
import models.Team;
import models.User;
import play.Logger;
import repositories.interfaces.BadgeRepository;
import repositories.interfaces.RegionRepository;
import repositories.interfaces.TeamRepository;
import repositories.interfaces.UserRepository;

import javax.inject.Inject;

import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by eduardo on 6/08/16.
 */

public class TeamRepositoryTest extends BaseAuthenticationTest
{

  @Inject
  TeamRepository teamRepository;
  @Inject
  UserRepository userRepository;
  @Inject
  BadgeRepository badgeRepository;
  @Inject
  RegionRepository regionRepository;

  @Test
  public void testCreateTeam()
  {
    Logger.debug("[{}] Inside Create Team test", getClass());
    final User user = userRepository.get().stream().findFirst().get();

    final Team team = new Team();
    team.setAbreviation("GT");

    final Badge badge = new Badge();
    badge.setImgUrl("http://sdn.gt/image");

    final Region region = new Region();
    region.setName("Occidente");
    final Optional<Region> regionOptional = regionRepository.persist(region);
    badge.setRegion(regionOptional.get());

    final Optional<Badge> badgeOptional = badgeRepository.persist(badge);

    team.setBadge(badgeOptional.get());

    team.setUser(user);

    final Optional<Team> teamOptional = teamRepository.persist(team);

    assertTrue(teamOptional.isPresent());

  }

}
