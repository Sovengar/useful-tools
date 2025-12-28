package testing.testcontainers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import testing.config.initializers.UseInfraInitializer1;
import testing.config.initializers.UseInfraInitializer3;
import testing.config.initializers.jvm_level.InfraInitializer2;
import testing.config.initializers.test_class_level.Junit5ITInitializer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * TESTCONTAINERS EXAMPLES
 * ═══════════════════════════════════════════════════════════════════════════════
 * Examples of different ways to configure Testcontainers.
 *
 * Dependencies (pom.xml):
 * - org.testcontainers:postgresql
 * - org.springframework.boot:spring-boot-testcontainers
 * ═══════════════════════════════════════════════════════════════════════════════
 */
class TestContainersIT {
    @Nested
    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer3
    class ExtendsWithExample {
        @Test
        void test() {

        }
    }

    @Nested
    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer1
    class ImportExample {
        @Test
        void test() {

        }
    }

    @Nested
    @ActiveProfiles("test")
    @SpringBootTest
    class ExtendsExample extends InfraInitializer2 {
        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @Nested
    @ActiveProfiles("test")
    @SpringBootTest
    class InClassExample {
        public static GenericContainer postgres = new PostgreSQLContainer().withReuse(true);

        // public static final PostgreSQLContainer<?> postgres = new
        // PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("16-alpine"))
        // .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in
        // ~/.testcontainers.properties

        @org.springframework.test.context.DynamicPropertySource
        static void properties(org.springframework.test.context.DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url",
                    () -> ((PostgreSQLContainer<?>) postgres).getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
            registry.add("spring.datasource.username",
                    () -> ((PostgreSQLContainer<?>) postgres).getUsername());
            registry.add("spring.datasource.password",
                    () -> ((PostgreSQLContainer<?>) postgres).getPassword());
        }

        @BeforeAll
        public static void beforeAll() {
            postgres.start();
        }

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + ((PostgreSQLContainer<?>) postgres).getJdbcUrl());
        }
    }

    @Nested
    @ActiveProfiles("test")
    @SpringBootTest
    class jUnit5Example extends Junit5ITInitializer {

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }
}
