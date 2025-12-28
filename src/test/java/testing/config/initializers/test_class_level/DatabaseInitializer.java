package testing.config.initializers.test_class_level;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import testing.config.runners.DatabaseRunner;

@DatabaseRunner
@DataJpaTest
@Testcontainers // jUnit5 annotation, lets jUnit5 controls lifecycle of the container
// This initializer will be shared for all tests in a concrete test class (The
// class that extends from this one).
// But since it is managed with Junit5 Testcontainers extension, the container
// lifecycle is managed by Junit5.
// Which means that after the tests of the concrete test class are finished, the
// container will be stopped and thus not shareable.
public abstract class DatabaseInitializer {

    @Container // jUnit5 annotation, lets jUnit5 controls lifecycle of the container
    // @ServiceConnection // Spring annotation coming from
    // springboot-testcontainers. Injects properties
    // dynamically, no need to use @DynamicPropertySource or System.setProperty
    // Not used because we are using p6spy raw.
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres").withTag("16-alpine"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        // No postgres start because jUnit5 does it for us

        // Flyway migration
        Flyway flyway = Flyway.configure().dataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
                .locations("db/migrations")
                // .schemas(new String[]{"yourSchema", ""})
                .repeatableSqlMigrationPrefix("R_DISABLED")
                .load();
        flyway.migrate();
    }
}
