package testing.config.initializers.jvm_level.example3;

import org.testcontainers.containers.PostgreSQLContainer;

public class AppPostgresContainer extends PostgreSQLContainer<AppPostgresContainer> {
    private static final String IMAGE_NAME = "postgres";
    private static final String IMAGE_TAG = "16-alpine";
    static final String DB_NAME = "test";
    static final String USER = "test";
    static final String PASSWORD = "test";

    public AppPostgresContainer() {
        super(IMAGE_NAME + ":" + IMAGE_TAG);
    }
}
