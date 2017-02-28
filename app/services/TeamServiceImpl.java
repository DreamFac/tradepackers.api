package services;

import static play.mvc.Http.Status.*;
import static play.mvc.Results.*;

import dtos.BadgeDTO;
import dtos.RegionDTO;
import dtos.TeamDTO;
import models.Badge;
import models.Region;
import models.Team;
import models.User;
import play.libs.Json;
import repositories.interfaces.BadgeRepository;
import repositories.interfaces.RegionRepository;
import repositories.interfaces.TeamRepository;
import repositories.interfaces.UserRepository;
import services.base.AbstractService;
import services.interfaces.TeamService;
import utils.ResponseBuilder;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class TeamServiceImpl extends AbstractService<Team> implements TeamService
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
      final String userId) throws NoSuchElementException
  {

    final Team team = new Team();
    team.setAbreviation(teamDTO.getAbbreviation());
    Badge badge;
    Region region;
    final Optional<User> userOptional = this.userRepository.get(userId);

    if (!userOptional.isPresent())
    {
      status(NOT_FOUND, Json.toJson(ResponseBuilder
          .buildErrorResponse(Collections.singletonList("User not found"), NOT_FOUND)));
    }

    if (teamDTO.getBadge() != null && teamDTO.getBadge().getId() != null)
    {
      final Optional<Badge> badgeOptional = this.badgeRepository.get(
          teamDTO.getBadge().getId().toString());
      if (badgeOptional.isPresent())
      {
        badge = badgeOptional.get();
      }
      else
      {
        throw new NoSuchElementException("Cannot find a badge for team");
      }
    }
    else if (teamDTO.getBadge() != null)
    {
      badge = new Badge();
      badge.setImgUrl(teamDTO.getBadge().getImgUrl());

      if (teamDTO.getBadge().getRegion() != null && teamDTO.getBadge().getRegion().getId() != null)
      {
        final Optional<Region> regionOptional = this.regionRepository.get(teamDTO.getBadge()
            .getRegion()
            .getId());

        if (regionOptional.isPresent())
        {
          region = regionOptional.get();
        }
        else
        {
          throw new NoSuchElementException("Cannot find a region for badge");
        }
      }
      else if (teamDTO.getBadge().getRegion() != null)
      {
        region = new Region();
        region.setName(teamDTO.getBadge().getRegion().getName());
        region = this.regionRepository.persist(region).get();
      }
      else
      {
        throw new NoSuchElementException("Cannot find a region for badge");
      }
      badge.setRegion(region);
      badge = this.badgeRepository.persist(badge).get();
    }

    else
    {
      throw new NoSuchElementException("Cannot find a badge for team");
    }

    team.setBadge(badge);
    team.setName(teamDTO.getName());
    team.setUser(userOptional.get());

    return team;
  }

}
