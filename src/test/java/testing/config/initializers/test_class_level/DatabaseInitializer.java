package testing.config.initializers.test_class_level;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
    @ServiceConnection // Spring annotation coming from springboot-testcontainers. Injects properties
                       // dynamically, no need to use @DynamicPropertySource or System.setProperty
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres").withTag("16-alpine"));

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
                .load();
        flyway.migrate();
    }
}
