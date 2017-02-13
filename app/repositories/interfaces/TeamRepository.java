package repositories.interfaces;

import models.Team;

import java.util.Optional;

/**
 * Created by eduardo on 12/02/17.
 */
public interface TeamRepository extends Repository<Team>
{
  Optional<Team> findTeamByUserId(Long userId);
}
