package testing.config.initializers;

import org.junit.jupiter.api.extension.ExtendWith;
import testing.config.initializers.jvm_level.example3.InfraInitializer3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(InfraInitializer3.class)
public @interface UseInfraInitializer3 {
}
