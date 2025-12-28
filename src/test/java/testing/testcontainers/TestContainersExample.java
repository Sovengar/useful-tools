package testing.testcontainers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import testing.config.initializers.UseInfraInitializer1;
import testing.config.initializers.UseInfraInitializer3;
import testing.config.initializers.jvm_level.InfraInitializer1;
import testing.config.initializers.jvm_level.example3.InfraInitializer3;
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
class TestContainersExample {
    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer3
    @Nested
    class ExtendsWithExample {
        @Test
        void connectionEstablished() {
            assertThat(InfraInitializer3.postgres.isCreated()).isTrue();
            assertThat(InfraInitializer3.postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + InfraInitializer3.postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer1
    @Nested
    class ImportExample {
        @Test
        void connectionEstablished() {
            assertThat(InfraInitializer1.postgres.isCreated()).isTrue();
            assertThat(InfraInitializer1.postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + InfraInitializer1.postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    class ExtendsExample extends InfraInitializer2 {
        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    class InClassExample {
        public static GenericContainer postgres = new PostgreSQLContainer();

        @DynamicPropertySource
        static void properties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url",
                    () -> ((PostgreSQLContainer<?>) postgres).getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
            registry.add("spring.datasource.username", () -> ((PostgreSQLContainer<?>) postgres).getUsername());
            registry.add("spring.datasource.password", () -> ((PostgreSQLContainer<?>) postgres).getPassword());
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

    @ActiveProfiles("test")
    @SpringBootTest
    @Testcontainers
    @Nested
    class InClassExample2 {
        @Container
        protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        }

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Testcontainers
    @Nested
    class InClassExample3 {
        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

        @DynamicPropertySource
        static void properties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        }

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    class jUnit5Example extends Junit5ITInitializer {

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }
}
