package testing.config.initializers.jvm_level;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import testing.config.runners.SystemRunner;

@SpringBootTest
@SystemRunner
// This initializer will be shared for all test classes.
// Even though this class is extended multiple times and therefore .start() is
// called multiple times, it will only be started one time if you have
// .withReuse and setted in .testcontainers.properties
// You could even have the beforeAll in the concrete test class, and it would
// still be started only once.
public abstract class InfraInitializer2 {
    @Autowired
    protected MockMvc mockMvc;

    protected final Faker faker = new Faker();

    // Static initialization (After JVM, before Spring Test)
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres").withTag("16-alpine"))
            .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in
                              // ~/.testcontainers.properties

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:")); // P6SPY
                                                                                                            // WORKAROUND
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        // DB Start
        postgres.start();

        // Flyway migration
        Flyway flyway = Flyway.configure().dataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
                .locations("db/migrations")
                // .schemas(new String[]{"yourSchema", ""})
                .load();
        flyway.migrate();
    }
}