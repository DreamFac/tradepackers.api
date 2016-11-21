package dtos;

import lombok.Getter;
import lombok.Setter;

import play.data.validation.Constraints;

/**
 * Created by eduardo on 14/12/15.
 */
@Getter
@Setter
public class LoginDTO
{
  @Constraints.Required
  @Constraints.Email
  private String email;

  @Constraints.Required
  private String password;
}
