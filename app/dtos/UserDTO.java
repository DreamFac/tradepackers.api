package dtos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;
import lombok.Setter;
import play.data.validation.Constraints;

/**
 * Created by eduardo on 20/11/14.
 */
@Getter
@Setter
public class UserDTO
{

  public Long id;

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

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
