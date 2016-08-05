package models;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import models.base.AbstractEntity;
import play.data.validation.Constraints;

@Entity
@Getter
@Setter
public class User extends AbstractEntity
{

  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  public String firstName;

  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  public String lastName;

  @Constraints.MaxLength(256)
  @Constraints.Required
  @Constraints.Email
  private String email;

  @JsonIgnore
  private String password;
}
