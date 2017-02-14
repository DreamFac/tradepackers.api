package models;

import models.base.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 12/02/17.
 */
@Entity
@Getter
@Setter
public class Badge extends AbstractEntity
{
  @ManyToOne
  private Region region;

  private String imgUrl;
}
