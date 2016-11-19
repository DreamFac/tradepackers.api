package services.interfaces;

import models.UserProvider;

import java.util.Optional;

/**
 * Created by eduardo on 9/11/16.
 */
public interface UserProviderService extends GenericService<UserProvider>
{
  Optional<UserProvider> findByProviderId(Long providerId);
}
