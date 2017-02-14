package services;

import dtos.BadgeDTO;
import dtos.RegionDTO;
import dtos.TeamDTO;
import models.Badge;
import models.Region;
import models.Team;
import models.User;
import repositories.interfaces.BadgeRepository;
import repositories.interfaces.RegionRepository;
import repositories.interfaces.TeamRepository;
import repositories.interfaces.UserRepository;
import services.base.AbstractService;
import services.interfaces.TeamService;

import java.util.Optional;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class TeamServiceImpl extends AbstractService<Team, TeamDTO> implements TeamService
{
  TeamRepository teamRepository;
  BadgeRepository badgeRepository;
  RegionRepository regionRepository;
  UserRepository userRepository;

  @Inject
  public TeamServiceImpl(
      final TeamRepository teamRepository,
      final UserRepository userRepository,
      final BadgeRepository badgeRepository,
      final RegionRepository regionRepository)
  {
    super(teamRepository);
    this.teamRepository = teamRepository;
    this.userRepository = userRepository;
    this.badgeRepository = badgeRepository;
    this.regionRepository = regionRepository;
  }

  @Override
  public Optional<TeamDTO> findTeamByUserId(final String userId)
  {
    final Optional<Team> team = this.teamRepository.findTeamByUserId(userId);
    if (!team.isPresent())
    {
      return Optional.empty();
    }

    return Optional.ofNullable(entityToDto(team.get()));
  }

  @Override
  public TeamDTO entityToDto(final Team team)
  {
    final Badge badge = team.getBadge();
    final String region =
        badge != null ? badge.getRegion() != null ? badge.getRegion().getName() : "" : "";

    return TeamDTO
        .builder()
        .id(team.getId().toString())
        .abbreviation(team.getAbreviation())
        .badge(badge == null ? BadgeDTO.builder().build() :
            BadgeDTO
                .builder()
                .id(badge.getId().toString())
                .imgUrl(badge.getImgUrl())
                .region(RegionDTO.builder()
                    .id(team.getBadge().getRegion().getId().toString())
                    .name(team.getBadge().getRegion().getName())
                    .build())
                .build())
        .name(team.getName())
        .build();
  }

  public Team dtoToEntity(
      final TeamDTO teamDTO,
      final String userId)
  {
    final Team team = new Team();
    team.setAbreviation(teamDTO.getAbbreviation());
    Badge badge;
    Region region;
    final Optional<User> userOptional = this.userRepository.get(userId);
    if (teamDTO.getBadge().getId() != null)
    {
      final Optional<Badge> badgeOptional = this.badgeRepository.get(
          teamDTO.getBadge().getId().toString());
      badge = badgeOptional.get();
    }
    else
    {
      badge = new Badge();
      badge.setImgUrl(teamDTO.getBadge().getImgUrl());

      if (teamDTO.getBadge().getRegion().getId() != null)
      {
        final Optional<Region> regionOptional = this.regionRepository.get(teamDTO.getBadge()
            .getRegion()
            .getId());
        region = regionOptional.get();
      }
      else
      {
        region = new Region();
        region.setName(teamDTO.getBadge().getRegion().getName());
        region = this.regionRepository.persist(region).get();
      }
      badge.setRegion(region);
      badge = this.badgeRepository.persist(badge).get();
    }

    team.setBadge(badge);
    team.setName(teamDTO.getName());
    team.setUser(userOptional.get());

    return team;
  }

}
