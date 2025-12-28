package testing.testcontainers.config.initializers;

import org.springframework.test.context.ContextConfiguration;
import testing.testcontainers.config.initializers.jvm_level.InfraInitializer1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = InfraInitializer1.class)
public @interface UseInfraInitializer1 {
}
