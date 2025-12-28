package testing._config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@DisplayNameGeneration(PrettyTestNames.class)
public @interface BasicTestTags {
}
