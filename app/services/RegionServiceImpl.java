package services;

import dtos.RegionDTO;
import models.Region;
import repositories.interfaces.RegionRepository;
import services.base.AbstractService;
import services.interfaces.RegionService;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class RegionServiceImpl extends AbstractService<Region, RegionDTO> implements RegionService
{
  RegionRepository regionRepository;

  @Inject
  public RegionServiceImpl(final RegionRepository regionRepository)
  {
    super(regionRepository);
    this.regionRepository = regionRepository;
  }

  @Override
  public RegionDTO entityToDto(final Region entity)
  {
    return null;
  }

  @Override
  public Region dtoToEntity(final RegionDTO dto)
  {
    return null;
  }
}
