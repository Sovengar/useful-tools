package testing.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {
    public static final String[] SCHEMAS = new String[]{"public"};
    private static final String ENCODING = "UTF-8";
    private final DataSource dataSource;
    private final Environment env;

    @Value("${flyway.h2-behavior:false}")
    private boolean h2Behavior;

    @Bean
    public Flyway flyway() {
        boolean isInDevMode = env.acceptsProfiles(Profiles.of("dev"));

        final String[] migrationLocations = isInDevMode ? new String[]{"classpath:db/migrations", "classpath:db/dev"} : new String[]{"classpath:db/migrations"};

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .cleanDisabled(!h2Behavior)
                .locations(migrationLocations)
                .schemas(SCHEMAS)
                .encoding(ENCODING)
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .validateMigrationNaming(true)
                .load();

        if (h2Behavior) {
            flyway.clean();
        }

        //Comment this if you want to delegate migrations in cloud to flyway
        if (isInDevMode) {
            flyway.migrate();
        }

        return flyway;
    }
}