package repositories;

import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.security.Token;
import play.db.jpa.JPA;
import repositories.base.JpaRepository;
import repositories.interfaces.TokenRepository;

/**
 * Created by eduardo on 4/08/16.
 */
@Singleton
public class JpaTokenRepository extends JpaRepository<Token> implements TokenRepository
{
  @Override
  public Optional<Token> findUserByAuthToken(final String authToken)
  {
    final CriteriaBuilder criteriaBuilder = JPA.em().getCriteriaBuilder();
    final CriteriaQuery<Token> criteriaQuery = criteriaBuilder.createQuery(Token.class);
    final Root<Token> tokenRoot = criteriaQuery.from(Token.class);
    criteriaQuery.select(tokenRoot).where(
        criteriaBuilder.equal(tokenRoot.get("authToken"), authToken));
    return get(criteriaQuery).stream().findFirst();
  }

  @Override
  public Optional<Token> findTokenByUserId(final Long userId)
  {
    final CriteriaBuilder criteriaBuilder = JPA.em().getCriteriaBuilder();
    final CriteriaQuery<Token> criteriaQuery = criteriaBuilder.createQuery(Token.class);
    final Root<Token> tokenRoot = criteriaQuery.from(Token.class);
    criteriaQuery.select(tokenRoot).where(
        criteriaBuilder.equal(tokenRoot.get("user").get("id"), userId));
    return get(criteriaQuery).stream().findFirst();
  }
}
