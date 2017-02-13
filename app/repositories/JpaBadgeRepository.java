package repositories;

import models.Badge;
import play.db.jpa.JPAApi;
import repositories.base.JpaRepository;
import repositories.interfaces.BadgeRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by eduardo on 12/02/17.
 */
@Singleton
public class JpaBadgeRepository extends JpaRepository<Badge> implements BadgeRepository
{
  @Inject
  public JpaBadgeRepository(final JPAApi jpaApi)
  {
    super(jpaApi, Badge.class);
  }

}
