package testing.config.initializers.jvm_level.example3;

import org.flywaydb.core.Flyway;
import testing.config.FlywayConfig;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class InfraInitializer3 implements BeforeAllCallback {
    public static AppPostgresContainer postgres = new AppPostgresContainer()
            .withDatabaseName(AppPostgresContainer.DB_NAME)
            .withUsername(AppPostgresContainer.USER)
            .withPassword(AppPostgresContainer.PASSWORD)
            .withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) {
        postgres.start();
        updateDataSourceProps(postgres);
        migrateDatabase();
    }

    private static void migrateDatabase() {
        Flyway flyway = Flyway.configure().dataSource(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())
                .locations("db/migrations")
                .schemas(FlywayConfig.SCHEMAS)
                .load();
        flyway.migrate();
    }

    // Hijack because we can't use @DynamicPropertySources
    private void updateDataSourceProps(PostgreSQLContainer<?> container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:")); // P6SPY
                                                                                                             // WORKAROUND
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }
}
