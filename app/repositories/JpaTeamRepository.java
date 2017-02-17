package repositories;

import models.Team;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import repositories.base.JpaRepository;
import repositories.interfaces.TeamRepository;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by eduardo on 12/02/17.
 */
@Singleton
public class JpaTeamRepository extends JpaRepository<Team> implements TeamRepository
{
  @Inject
  public JpaTeamRepository(final JPAApi jpaApi)
  {
    super(jpaApi, Team.class);
  }

  @Transactional
  @Override
  public Optional<Team> findTeamByUserId(final String userId)
  {
    return getJpaApi().withTransaction(entityManager ->
    {
      final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Team> criteriaQuery = criteriaBuilder.createQuery(Team.class);
      final Root<Team> fromTeam = criteriaQuery.from(Team.class);

      criteriaQuery
          .select(fromTeam)
          .where(
              criteriaBuilder
                  .equal(
                      fromTeam
                          .get("user")
                          .get("id"),
                      userId));

      final TypedQuery<Team> query = entityManager.createQuery(criteriaQuery);
      return query.getResultList();
    }).stream().findFirst();
  }
}
