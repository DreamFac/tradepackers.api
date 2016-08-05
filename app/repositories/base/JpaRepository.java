package repositories.base;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.base.AbstractEntity;
import play.db.jpa.JPA;
import repositories.interfaces.Repository;

/**
 * Created by eduardo on 4/08/16.
 */
public class JpaRepository<T extends AbstractEntity> implements Repository<T>
{

  private Class<T> type;

  @PostConstruct
  public void init()
  {
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
    this.type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
  }

  @Override
  public Optional<T> get(Long id)
  {
    return Optional.ofNullable(run(entityManager ->
    {
      return entityManager.find(type, id);
    }));
  }

  @Override
  public List<T> get(final CriteriaQuery<T> criteriaQuery)
  {
    List<T> resultList = run(entityManager ->
    {
      final TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();
    });

    return resultList;
  }

  @Override
  public List<T> get()
  {
    List<T> resultList = run(entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<T> criteria = criteriaBuilder.createQuery(type);

      final Root<T> root = criteria.from(type);
      criteria.select(root);

      final TypedQuery<T> query = entityManager.createQuery(criteria);
      return query.getResultList();
    });

    return resultList;
  }

  @Override
  public Optional<T> persist(T entity)
  {
    return Optional.ofNullable(runInTransaction(entityManager ->
    {
      return entityManager.merge(entity);
    }));
  }

  @Override
  public Collection<T> persist(Collection<T> entities)
  {
    return runInTransaction(entityManager ->
    {
      entities.forEach(entityManager::merge);
      return entities;
    });
  }

  @Override
  public boolean remove(T entity)
  {
    return remove(entity.getId());
  }

  @Override
  public void remove(Collection<T> entities)
  {
    runInTransaction(entityManager ->
    {
      entities
          .stream()
          .map(AbstractEntity::getId)
          .map(id -> entityManager.find(type, id))
          .filter(Objects::nonNull)
          .forEach(entityManager::remove);
    });
  }

  @Override
  public boolean remove(Long id)
  {
    return runInTransaction(entityManager ->
    {
      final Optional<T> managedEntity = get(id);
      if (managedEntity.isPresent())
      {
        entityManager.remove(managedEntity);
        return true;
      }
      return false;
    });
  }

  @Override
  public void remove(CriteriaQuery<T> query)
  {
    remove(get(query));
  }

  private <R> R run(Function<EntityManager, R> function)
  {
    final EntityManager entityManager = JPA.em();
    try
    {
      return function.apply(entityManager);
    }
    finally
    {
      entityManager.close();
    }
  }

  private void run(Consumer<EntityManager> function)
  {
    run(entityManager ->
    {
      function.accept(entityManager);
      return null;
    });
  }

  private <R> R runInTransaction(Function<EntityManager, R> function)
  {
    return run(entityManager ->
    {
      entityManager.getTransaction().begin();

      final R result = function.apply(entityManager);

      entityManager.getTransaction().commit();

      return result;
    });
  }

  private void runInTransaction(Consumer<EntityManager> function)
  {
    runInTransaction(entityManager ->
    {
      function.accept(entityManager);
      return null;
    });
  }
}
