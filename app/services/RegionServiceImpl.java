package services;

import models.Region;
import repositories.interfaces.RegionRepository;
import services.base.AbstractService;
import services.interfaces.RegionService;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class RegionServiceImpl extends AbstractService<Region> implements RegionService
{
  RegionRepository regionRepository;

  @Inject
  public RegionServiceImpl(final RegionRepository regionRepository)
  {
    super(regionRepository);
    this.regionRepository = regionRepository;
  }
}
