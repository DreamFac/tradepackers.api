package repositories.base;

import models.base.AbstractEntity;
import play.db.jpa.JPAApi;
import repositories.interfaces.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.Getter;

/**
 * Created by eduardo on 4/08/16.
 */
public class JpaRepository<T extends AbstractEntity> implements Repository<T>
{

  @Getter
  private final JPAApi jpaApi;
  private final Class<T> type;

  @Inject
  public JpaRepository(final JPAApi jpaApi, final Class<T> type)
  {
    this.jpaApi = jpaApi;
    this.type = type;
  }



  @Override
  public List<T> get()
  {
    return this.jpaApi.withTransaction(entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<T> criteria = criteriaBuilder.createQuery(this.type);
      final Root<T> root = criteria.from(this.type);
      criteria.select(root);

      final TypedQuery<T> query = entityManager.createQuery(criteria);
      return query.getResultList();
    });
  }

  @Override
  public Optional<T> persist(final T entity)
  {
    return Optional.ofNullable(this.jpaApi.withTransaction(entityManager ->
    {
      return entityManager.merge(entity);
    }));
  }

  @Override
  public Collection<T> persist(final Collection<T> entities)
  {
    return this.jpaApi.withTransaction(entityManager ->
    {
      entities.forEach(entityManager::merge);
      return entities;
    });
  }

  @Override
  public boolean remove(final T entity)
  {
    return remove(entity.getId());
  }

  @Override
  public boolean remove(final Long id)
  {
    return this.jpaApi.withTransaction(entityManager ->
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
  public Optional<T> get(final Long id)
  {
    return Optional.ofNullable(this.jpaApi.withTransaction(entityManager ->
    {
      return entityManager.find(this.type, id);
    }));
  }

  @Override
  public void remove(final Collection<T> entities)
  {
    this.jpaApi.withTransaction(() ->
    {
      entities
          .stream()
          .map(AbstractEntity::getId)
          .map(id -> this.jpaApi.em().find(this.type, id))
          .filter(Objects::nonNull)
          .forEach(this.jpaApi.em()::remove);
    });

  }

}
