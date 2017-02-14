package models;

import models.base.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 12/02/17.
 */

@Entity
@Getter
@Setter
public class Team extends AbstractEntity
{
  private String name;

  private String abreviation;

  @OneToOne
  private User user;

  @ManyToOne
  private Badge badge;

}
