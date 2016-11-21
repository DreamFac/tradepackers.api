package services;

import models.UserProvider;
import repositories.interfaces.UserProviderRepository;
import services.base.AbstractService;
import services.interfaces.UserProviderService;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by eduardo on 9/11/16.
 */
@Singleton
public class UserProviderServiceImpl extends AbstractService<UserProvider> implements
    UserProviderService
{

  UserProviderRepository userProviderRepository;

  @Inject
  public UserProviderServiceImpl(final UserProviderRepository userProviderRepository)
  {
    super(userProviderRepository);
    this.userProviderRepository = userProviderRepository;
  }

  @Override
  public Optional<UserProvider> findByProviderId(final Long providerId)
  {
    return this.userProviderRepository.getByProviderId(providerId);
  }
}
