package services.interfaces;

import java.util.Optional;

import models.User;
import models.security.Token;

/**
 * Created by eduardo on 4/08/16.
 */
public interface UserService extends GenericService<User>
{
  Optional<User> findByEmailAndPassword(String email, String password);

  Optional<User> finByEmail(String email);

  Optional<Token> getUserToken(User user);

  Optional<Token> login(String email, final String password);

  boolean isTokenExpired(Token token);

  void logout(Long userId);

  Optional<User> getLoggedInUser();
}
