package models.base;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by eduardo on 18/11/14.
 */
@MappedSuperclass
public class AbstractEntity
{
  @Id
  public String id;
  @JsonIgnore
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  public Date creationDate;
  @JsonIgnore
  @Version
  public Date lastModifiedDate;
  public AbstractEntity() {
    this.id = UUID.randomUUID().toString();
  }

  public Date getCreationDate()
  {
    return this.creationDate;
  }

  public void setCreationDate(final Date creationDate)
  {
    this.creationDate = creationDate;
  }

  public Date getLastModifiedDate()
  {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(final Date lastModifiedDate)
  {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass())))
    {
      return false;
    }

    final AbstractEntity that = (AbstractEntity) obj;

    return this.id.equals(that.getId());
  }

  /**
   * Returns the identifier of the entity.
   *
   * @return the id
   */
  public String getId()
  {
    return this.id;
  }

  public void setId(final String id)
  {
    this.id = id;
  }

  @Override
  public int hashCode()
  {
    return this.id == null ? 0 : this.id.hashCode();
  }

  /*
   * @Override public String toString() { return ToStringBuilder.reflectionToString(this); }
   */

}
