package services;

import dtos.BadgeDTO;
import models.Badge;
import repositories.interfaces.BadgeRepository;
import services.base.AbstractService;
import services.interfaces.BadgeService;

import javax.inject.Inject;

/**
 * Created by eduardo on 12/02/17.
 */
public class BadgeServiceImpl extends AbstractService<Badge, BadgeDTO> implements BadgeService
{
  BadgeRepository badgeRepository;

  @Inject
  public BadgeServiceImpl(final BadgeRepository badgeRepository)
  {
    super(badgeRepository);
    this.badgeRepository = badgeRepository;
  }

  @Override
  public BadgeDTO entityToDto(final Badge entity)
  {
    return null;
  }

  @Override
  public Badge dtoToEntity(final BadgeDTO dto)
  {
    return null;
  }
}
