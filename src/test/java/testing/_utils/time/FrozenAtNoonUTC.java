package testing._utils.time;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Documented
@TimeTravel(instant = "2030-01-01T12:00:00Z")
public @interface FrozenAtNoonUTC {
}
