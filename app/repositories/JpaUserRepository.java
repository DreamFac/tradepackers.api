package repositories;

import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.User;
import play.db.jpa.JPA;
import repositories.base.JpaRepository;
import repositories.interfaces.UserRepository;

/**
 * Created by eduardo on 4/08/16.
 */
@Singleton
public class JpaUserRepository extends JpaRepository<User> implements UserRepository
{

  @Override
  public Optional<User> findByEmailAndPassword(final String email, final String password)
  {
    final CriteriaBuilder criteriaBuilder = JPA.em().getCriteriaBuilder();
    final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
    final Root<User> userRoot = criteriaQuery.from(User.class);
    criteriaQuery.select(userRoot)
        .where(
            criteriaBuilder.and(
                criteriaBuilder.equal(userRoot.get("email"), email),
                criteriaBuilder.equal(userRoot.get("password"), password)));
    return get(criteriaQuery).stream().findFirst();
  }

  @Override
  public Optional<User> findByEmail(final String email)
  {
    final CriteriaBuilder criteriaBuilder = JPA.em().getCriteriaBuilder();
    final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
    final Root<User> userRoot = criteriaQuery.from(User.class);
    criteriaQuery.select(userRoot).where(
        criteriaBuilder.equal(userRoot.get("email"), email));
    return get(criteriaQuery).stream().findFirst();
  }
}
