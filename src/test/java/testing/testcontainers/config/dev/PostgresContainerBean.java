package testing.testcontainers.config.dev;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerBean {

    // Without p6spy
    // @RestartScope
    // @Bean
    // @ServiceConnection
    // public PostgreSQLContainer<?> postgresContainer() {
    // return new
    // PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    // }

    // With p6spy
    @RestartScope
    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    }

    @Bean
    public JdbcConnectionDetails postgresConnectionDetails(
            PostgreSQLContainer<?> container) {
        return new JdbcConnectionDetails() {
            @Override
            public String getUsername() {
                return container.getUsername();
            }

            @Override
            public String getPassword() {
                return container.getPassword();
            }

            @Override
            public String getJdbcUrl() {
                return container.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:");
            }

            @Override
            public String getDriverClassName() {
                return "com.p6spy.engine.spy.P6SpyDriver";
            }
        };
    }

}
