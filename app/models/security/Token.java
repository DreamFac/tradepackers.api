package models.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;

import constants.UserLoginStatus;
import lombok.Getter;
import lombok.Setter;
import models.User;
import models.base.AbstractEntity;

/**
 * Created by eduardo on 1/11/14.
 */

@Entity
@Getter
@Setter
public class Token extends AbstractEntity
{

  public String authToken;

  @OneToOne
  public User user;

  public UserLoginStatus status;

  public Date expirationDate;

  public String ip;

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}
