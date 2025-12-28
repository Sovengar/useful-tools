package testing._config.runners;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import testing._config.BasicTestTags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@BasicTestTags
@Tag("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public @interface DatabaseRunner {
    //Add @DataJpaTest in your target class to import your repositories.
    //Add @Import in your target class to import your slice dependencies.
    //Add @InfraInitializer in your target class to enable infra dependencies.
}
