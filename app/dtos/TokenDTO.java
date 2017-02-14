package dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * Created by eduardo on 12/02/17.
 */
@Builder
@AllArgsConstructor
@Getter
public class TokenDTO
{
  private final String token;
  private final Long userId;
  private final Date expirationDate;
}
