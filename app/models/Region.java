package models;

import models.base.AbstractEntity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eduardo on 12/02/17.
 */

@Entity
@Getter
@Setter
public class Region extends AbstractEntity
{
  private String name;
}
