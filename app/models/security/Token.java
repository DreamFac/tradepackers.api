package models.security;

import constants.UserLoginStatus;
import models.User;
import models.base.AbstractEntity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

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

}
