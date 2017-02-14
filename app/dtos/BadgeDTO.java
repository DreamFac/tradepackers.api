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
public class BadgeDTO
{
  private final Long id;

  private final RegionDTO region;

  private final String imgUrl;
}
