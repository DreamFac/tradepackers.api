package dtos;

import play.data.validation.Constraints;

import lombok.Getter;
import lombok.Setter;

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
