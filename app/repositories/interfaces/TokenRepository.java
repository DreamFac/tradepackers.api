package repositories.interfaces;

import java.util.Optional;

import models.security.Token;

/**
 * Created by eduardo on 4/08/16.
 */
public interface TokenRepository extends Repository<Token>
{
  Optional<Token> findUserByAuthToken(String authToken);

  Optional<Token> findTokenByUserId(Long userId);
}
