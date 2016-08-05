package repositories.interfaces;

import java.util.Optional;

import models.User;

/**
 * Created by eduardo on 4/08/16.
 */
public interface UserRepository extends Repository<User>
{
  Optional<User> findByEmailAndPassword(String email, String password);

  Optional<User> findByEmail(String email);
}
