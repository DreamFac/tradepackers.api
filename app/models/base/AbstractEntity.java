package models.base;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by eduardo on 18/11/14.
 */
@MappedSuperclass
public class AbstractEntity
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long id;

  @JsonIgnore
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  public Date creationDate;
  @JsonIgnore
  @Version
  public Date lastModifiedDate;

  /**
   * Returns the identifier of the entity.
   *
   * @return the id
   */
  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Date getCreationDate()
  {
    return this.creationDate;
  }

  public void setCreationDate(Date creationDate)
  {
    this.creationDate = creationDate;
  }

  public Date getLastModifiedDate()
  {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate)
  {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass())))
    {
      return false;
    }

    AbstractEntity that = (AbstractEntity) obj;

    return this.id.equals(that.getId());
  }

  @Override
  public int hashCode()
  {
    return id == null ? 0 : id.hashCode();
  }

  /*
   * @Override public String toString() { return ToStringBuilder.reflectionToString(this); }
   */

}
