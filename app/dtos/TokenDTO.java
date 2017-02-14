package dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by eduardo on 12/02/17.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDTO
{
  private String token;
  private String userId;
  private Date expirationDate;
}
