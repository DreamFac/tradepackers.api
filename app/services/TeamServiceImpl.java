package services;

import dtos.BadgeDTO;
import dtos.TeamDTO;
import models.Badge;
import models.Team;
import repositories.interfaces.TeamRepository;
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

  @Inject
  public TeamServiceImpl(final TeamRepository teamRepository)
  {
    super(teamRepository);
    this.teamRepository = teamRepository;
  }

  @Override
  public Optional<TeamDTO> findTeamByUserId(final Long userId)
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
        .abbreviation(team.getAbreviation())
        .badge(badge == null ? BadgeDTO.builder().build() :
            BadgeDTO
                .builder()
                .id(badge.getId())
                .imgUrl(badge.getImgUrl())
                .region(region)
                .build())
        .name(team.getName())
        .build();
  }

  @Override
  public Team dtoToEntity(final TeamDTO teamDTO)
  {
    final Team team = new Team();

    return team;
  }

}
