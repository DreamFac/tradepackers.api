package dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by eduardo on 12/02/17.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamDTO
{
  private String id;

  private String name;

  private String abbreviation;

  private BadgeDTO badge;

}
