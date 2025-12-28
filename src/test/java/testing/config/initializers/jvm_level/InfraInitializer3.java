package testing.config.initializers.jvm_level;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class InfraInitializer3 implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        AppPostgresContainer.container.start();
        updateDataSourceProps(AppPostgresContainer.container);
        // migration logic here (if needed)
    }

    // Hijack because we can't use @DynamicPropertySources
    private void updateDataSourceProps(AppPostgresContainer container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:")); //P6SPY WORKAROUND
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }
}

class AppPostgresContainer extends PostgreSQLContainer<AppPostgresContainer> {
    private static final String IMAGE_NAME = "postgres";
    private static final String IMAGE_TAG = "16-alpine";
    private static final String DB_NAME = "testdb";
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    public static AppPostgresContainer container = new AppPostgresContainer()
            .withDatabaseName(DB_NAME)
            .withUsername(USER)
            .withPassword(PASSWORD)
            .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in
                              // ~/.testcontainers.properties

    public AppPostgresContainer() {
        super(IMAGE_NAME + ":" + IMAGE_TAG);
    }
}
