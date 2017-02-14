package repositories;

import models.Region;
import play.db.jpa.JPAApi;
import repositories.base.JpaRepository;
import repositories.interfaces.RegionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by eduardo on 12/02/17.
 */
@Singleton
public class JpaRegionRepository extends JpaRepository<Region> implements RegionRepository
{
  @Inject
  public JpaRegionRepository(final JPAApi jpaApi)
  {
    super(jpaApi, Region.class);
  }

}
