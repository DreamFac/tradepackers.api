package dtos;

import play.data.validation.Constraints;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 20/11/14.
 */
@Getter
@Setter
public class UserDTO
{

  @Constraints.MaxLength(256)
  @Constraints.Email
  public String email;
  @Constraints.MinLength(6)
  @Constraints.MaxLength(256)
  public String password;
  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  public String firstName;
  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  public String lastName;
  private String id;

}
