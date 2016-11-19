package models;

import models.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 9/11/16.
 */
@Entity
@Setter
@Getter
@SuppressWarnings("serial")

public class UserProvider extends AbstractEntity
{
  private String name;
  private Long providerId;
  @ManyToOne
  private User user;
  @Column(columnDefinition = "TEXT")
  private String credentials;
}
