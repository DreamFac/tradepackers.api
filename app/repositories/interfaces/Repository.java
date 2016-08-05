package repositories.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaQuery;

import models.base.AbstractEntity;

/**
 * Created by eduardo on 4/08/16.
 */
public interface Repository<T extends AbstractEntity>
{
  Optional<T> get(Long id);

  /**
   * return all entities that match the given predicate.
   */
  List<T> get(CriteriaQuery<T> query);

  List<T> get();

  Optional<T> persist(T entity);

  Collection<T> persist(Collection<T> entities);

  boolean remove(T entity);

  void remove(Collection<T> entities);

  /**
   * remove the entity with the given id.
   */
  boolean remove(Long id);

  /**
   * remove all entities that match the given predicate.
   */
  void remove(CriteriaQuery<T> query);
}
