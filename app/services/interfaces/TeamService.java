package services.interfaces;

import dtos.TeamDTO;
import models.Team;

import java.util.Optional;

/**
 * Created by eduardo on 12/02/17.
 */
public interface TeamService extends GenericService<Team>
{
  Optional<TeamDTO> findTeamByUserId(Long userId);
}
