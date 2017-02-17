package repositories;

import static org.junit.Assert.*;

import base.BaseAuthenticationTest;
import models.Region;
import repositories.interfaces.RegionRepository;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Test;

/**
 * Created by eduardo on 6/08/16.
 */

public class RegionRepositoryTest extends BaseAuthenticationTest
{

  @Inject
  RegionRepository regionRepository;

  @Test
  public void testCreateRegion()
  {

    final Region region = new Region();
    region.setName("Occidente");
    final Optional<Region> regionOptional = this.regionRepository.persist(region);

    assertTrue(regionOptional.isPresent());

  }

}
