package actions;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.google.inject.Inject;

import models.User;
import models.security.Token;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import repositories.interfaces.TokenRepository;
import services.interfaces.UserService;

/**
 * Created by eduardo on 4/08/16.
 */
public class AuthenticationAction extends Action<AuthenticationAction>
{
  public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
  public static final String AUTH_TOKEN = "authToken";

  private final TokenRepository tokenRepository;
  private final UserService userService;

  @Inject
  public AuthenticationAction(final TokenRepository tokenRepository, final UserService userService)
  {
    this.tokenRepository = tokenRepository;
    this.userService = userService;
  }

  @Override
  public CompletionStage<Result> call(final Http.Context ctx)
  {

    final String[] authTokenHeaderValues = ctx.request().headers().get(AUTH_TOKEN_HEADER);

    if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1)
        && (authTokenHeaderValues[0] != null))
    {

      final Optional<Token> tokenResult =
          this.tokenRepository.findUserByAuthToken(authTokenHeaderValues[0]);
      if (tokenResult.isPresent())
      {
        final Token token = tokenResult.get();

        if (this.userService.isTokenExpired(token))
        {
          return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
        }
        else
        {
          final User user = token.user;
          if (user != null)
          {
            ctx.args.put("user", user);
            return this.delegate.call(ctx);
          }
        }
      }
      else
      {
        return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
      }

    }
    return CompletableFuture.completedFuture(unauthorized("Unauthorized"));
  }
}
