package models;

import models.base.AbstractEntity;
import play.data.validation.Constraints;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity

@Setter
@Getter
public class User extends AbstractEntity
{

  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  private String firstName;

  @Constraints.MinLength(2)
  @Constraints.MaxLength(256)
  private String lastName;

  @Constraints.MaxLength(256)
  @Constraints.Required
  @Constraints.Email
  private String email;

  @JsonIgnore
  private String password;

}
