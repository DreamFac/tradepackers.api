package dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by eduardo on 12/02/17.
 */
@Builder
@AllArgsConstructor
@Getter
public class TokenDTO
{
  private final String token;
  private final Date expirationDate;
}
