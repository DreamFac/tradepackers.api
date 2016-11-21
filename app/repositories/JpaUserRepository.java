package repositories;

import models.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import repositories.base.JpaRepository;
import repositories.interfaces.UserRepository;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by eduardo on 4/08/16.
 */

@Singleton
public class JpaUserRepository extends JpaRepository<User> implements UserRepository
{

  @Inject
  public JpaUserRepository(final JPAApi jpaApi)
  {
    super(jpaApi, User.class);
  }

  @Transactional
  @Override
  public Optional<User> findByEmailAndPassword(final String email, final String password)
  {
    return getJpaApi().withTransaction((entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
      final Root<User> userRoot = criteriaQuery.from(User.class);
      criteriaQuery.select(userRoot)
          .where(
              criteriaBuilder.and(
                  criteriaBuilder.equal(userRoot.get("email"), email),
                  criteriaBuilder.equal(userRoot.get("password"), password)));
      final TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();

    })).stream().findFirst();
  }

  @Transactional
  @Override
  public Optional<User> findByEmail(final String email)
  {

    return getJpaApi().withTransaction((entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
      final Root<User> userRoot = criteriaQuery.from(User.class);
      criteriaQuery.select(userRoot).where(
          criteriaBuilder.equal(userRoot.get("email"), email));
      final TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();

    })).stream().findFirst();

  }
}
