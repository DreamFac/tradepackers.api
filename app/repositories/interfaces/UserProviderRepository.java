package repositories.interfaces;

import models.UserProvider;

import java.util.Optional;

/**
 * Created by eduardo on 9/11/16.
 */
public interface UserProviderRepository extends Repository<UserProvider>
{
  Optional<UserProvider> getByProviderId(Long providerId);
}
