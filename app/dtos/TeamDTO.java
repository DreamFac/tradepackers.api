package dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by eduardo on 12/02/17.
 */
@Builder
@AllArgsConstructor
@Getter
public class TeamDTO
{
  private final String name;

  private final String abbreviation;

  private final BadgeDTO badge;

}
