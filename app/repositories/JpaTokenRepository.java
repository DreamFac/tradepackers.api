package repositories;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.security.Token;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import repositories.base.JpaRepository;
import repositories.interfaces.TokenRepository;

/**
 * Created by eduardo on 4/08/16.
 */
@Singleton
public class JpaTokenRepository extends JpaRepository<Token> implements TokenRepository
{
  @Inject
  public JpaTokenRepository(final JPAApi jpaApi)
  {
    super(jpaApi);
  }

  @Transactional
  @Override
  public Optional<Token> findUserByAuthToken(final String authToken)
  {
    return getJpaApi().withTransaction((entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Token> criteriaQuery = criteriaBuilder.createQuery(Token.class);
      final Root<Token> tokenRoot = criteriaQuery.from(Token.class);
      criteriaQuery.select(tokenRoot).where(
          criteriaBuilder.equal(tokenRoot.get("authToken"), authToken));
      final TypedQuery<Token> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();
    })).stream().findFirst();

  }

  @Transactional
  @Override
  public Optional<Token> findTokenByUserId(final Long userId)
  {
    return getJpaApi().withTransaction((entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Token> criteriaQuery = criteriaBuilder.createQuery(Token.class);
      final Root<Token> tokenRoot = criteriaQuery.from(Token.class);
      criteriaQuery.select(tokenRoot).where(
          criteriaBuilder.equal(tokenRoot.get("user").get("id"), userId));
      final TypedQuery<Token> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();
    })).stream().findFirst();
  }
}
