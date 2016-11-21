package repositories;

import models.UserProvider;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import repositories.base.JpaRepository;
import repositories.interfaces.UserProviderRepository;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by eduardo on 9/11/16.
 */
@Singleton
public class JpaUserProviderRepository extends JpaRepository<UserProvider> implements
    UserProviderRepository
{
  @Inject
  public JpaUserProviderRepository(final JPAApi jpaApi)
  {
    super(jpaApi, UserProvider.class);
  }

  @Transactional
  @Override
  public Optional<UserProvider> getByProviderId(final Long providerId)
  {
    return getJpaApi().withTransaction((entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<UserProvider> criteriaQuery = criteriaBuilder.createQuery(
          UserProvider.class);
      final Root<UserProvider> userProviderRoot = criteriaQuery.from(UserProvider.class);

      criteriaQuery.select(userProviderRoot)
          .where(criteriaBuilder.equal(userProviderRoot.get("providerId"), providerId));
      final TypedQuery<UserProvider> query = entityManager.createQuery(criteriaQuery);

      return query.getResultList();

    })).stream().findFirst();
  }
}
