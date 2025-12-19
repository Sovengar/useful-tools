package testing.config.initializers;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import testing.config.runners.AcceptanceRunner;

@SpringBootTest
@AcceptanceRunner
@Testcontainers
//This initializer will be shared for all tests in a concrete test class (The class that extends from this one).
public abstract class AbstractITInitializer {

    @Autowired
    protected PostgreSQLContainer<?> postgres;

    protected final Faker faker = new Faker();

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres2 = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
            .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in ~/.testcontainers.properties
    //TODO DOUBT WITH REUSE

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                        postgres2.getJdbcUrl(),
                        postgres2.getUsername(),
                        postgres2.getPassword()
                )
                .locations("db/migrations")
                //.schemas(new String[]{"yourSchema", ""})
                .load();
        flyway.migrate();
    }
}