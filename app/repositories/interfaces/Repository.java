package repositories.interfaces;

import models.base.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by eduardo on 4/08/16.
 */
public interface Repository<T extends AbstractEntity>
{
  Optional<T> get(Object id);

  List<T> get();

  Optional<T> persist(T entity);

  Collection<T> persist(Collection<T> entities);

  boolean remove(T entity);

  void remove(Collection<T> entities);

  /**
   * remove the entity with the given id.
   * @param id
   */
  boolean remove(Object id);

}
