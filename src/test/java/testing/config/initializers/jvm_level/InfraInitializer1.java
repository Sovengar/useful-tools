package testing.config.initializers.jvm_level;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

//This initializer will be shared for all test classes.
public class InfraInitializer1 implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    // Static initialization (After JVM, before Spring Test)
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres").withTag("16-alpine"))
            .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in
                              // ~/.testcontainers.properties
    // static KafkaContainer kafka = new
    // KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    // Since there is no beforeAll, we do it here
    static {
        // Startables.deepStart(postgres, kafka).join();
        postgres.start();
        migrateDatabase();
    }

    private static void migrateDatabase() {
        Flyway flyway = Flyway.configure().dataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
                .locations("db/migrations")
                // .schemas(new String[]{"yourSchema", ""})
                .ignoreMigrationPatterns("*:repeatable") // Ignore R files
                .load();
        flyway.migrate();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ctx.getBeanFactory().registerSingleton("postgres", postgres); // If you want to @Autowired this container
        TestPropertyValues.of(
                // "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
                "spring.datasource.url=" + postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"), // P6SPY WORKAROUND
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()).applyTo(ctx.getEnvironment());
    }
}