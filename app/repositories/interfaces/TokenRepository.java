package repositories.interfaces;

import models.security.Token;

import java.util.Optional;

/**
 * Created by eduardo on 4/08/16.
 */
public interface TokenRepository extends Repository<Token>
{
  Optional<Token> findUserByAuthToken(String authToken);

  Optional<Token> findTokenByUserId(String userId);
}
