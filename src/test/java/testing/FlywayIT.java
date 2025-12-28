package testing;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import testing.testcontainers.config.initializers.UseInfraInitializer1;
import testing.postModel.PostRepository;
import testing.studentModel.StudentRepository;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@UseInfraInitializer1
@Tag("integration")
@DisplayName("Flyway Migration Behavioral Tests (Documentation)")
class FlywayIT {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Should NOT load dev-only repeatable migrations (R__Load_data.sql)")
    void flywayShouldNotMigratePopulateScripts() {
        // R__Load_data.sql is in db/dev, but test profile only loads from db/migrations
        assertThat(postRepository.count()).isZero();
        assertThat(studentRepository.count()).isZero();
    }

    @Test
    @DisplayName("Should have exactly 2 versioned migrations applied in 'test' profile")
    void shouldHaveCorrectNumberOfAppliedMigrations() {
        var appliedMigrations = flyway.info().applied();

        assertThat(appliedMigrations).hasSize(2);
        assertThat(Arrays.stream(appliedMigrations).map(m -> m.getVersion().toString()))
                .containsExactly("1", "2");

        assertThat(appliedMigrations[0].getDescription()).isEqualTo("create tables");
        assertThat(appliedMigrations[1].getDescription()).isEqualTo("add version to post");
    }

    @Test
    @DisplayName("Should use 'public' (or default) as schema for migration tracking")
    void shouldVerifySchemaConfiguration() {
        String[] schemas = flyway.getConfiguration().getSchemas();
        if (schemas.length > 0) {
            assertThat(schemas).containsExactly("public");
        }
    }

    @Test
    @DisplayName("Should verify that flyway_schema_history table exists and is populated")
    void shouldVerifySchemaHistoryTable() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE success = true",
                Integer.class);
        assertThat(count).isEqualTo(2);
    }
}
