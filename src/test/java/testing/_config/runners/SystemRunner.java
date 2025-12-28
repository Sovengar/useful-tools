package testing._config.runners;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@Tag("system")

// Web ///
@AutoConfigureMockMvc
//Better than @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) to avoid 2 threads which means 2 transactions
// ///////

// DB ////
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
// ///////

public @interface SystemRunner {
    //Add @SpringBoot or @SpringModulith in your target class with the configuration you need.
    //Add @InfraInitializer in your target class to enable infra dependencies.
}

