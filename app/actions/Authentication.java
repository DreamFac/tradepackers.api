package actions;

import java.lang.annotation.*;

import play.mvc.With;

/**
 * Created by eduardo on 4/08/16.
 */
@With(AuthenticationAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Inherited
@Documented
public @interface Authentication
{
}
