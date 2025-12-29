package testing.testcontainers;

import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import testing.testcontainers.config.dev.PostgresContainerBean;
import testing.testcontainers.config.initializers.UseInfraInitializer1;
import testing.testcontainers.config.initializers.UseInfraInitializer3;
import testing.testcontainers.config.initializers.jvm_level.InfraInitializer1;
import testing.testcontainers.config.initializers.jvm_level.example3.AppPostgresContainer;
import testing.testcontainers.config.initializers.jvm_level.example3.InfraInitializer3;
import testing.testcontainers.config.initializers.jvm_level.InfraInitializer2;
import testing.testcontainers.config.initializers.test_class_level.Junit5ITInitializer;

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
@Tag("integration")
class TestContainersIT {
    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer3
    @Nested
    @Tag("integration")
    class ExtendsWithExample {
        @Test
        void connectionEstablished() {
            var postgres = InfraInitializer3.postgres;
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @UseInfraInitializer1
    @Nested
    @Tag("integration")
    class ImportExample {
        @Test
        void connectionEstablished() {
            PostgreSQLContainer<?> postgres = InfraInitializer1.postgres;
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    @Tag("integration")
    class ExtendsExample extends InfraInitializer2 {
        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    @Tag("integration")
    class InClassExample {
        public static GenericContainer postgres = new PostgreSQLContainer(); //Static means it will be reused for all tests in this class

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
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + ((PostgreSQLContainer<?>) postgres).getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Testcontainers
    @Nested
    @Tag("integration")
    class InClassExample2 {
        @Container
        private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine"); //Static means it will be reused for all tests in this class

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
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Testcontainers
    @Nested
    @Tag("integration")
    class InClassExample3 {
        @Container
        private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine"); //No static, one container per test

        @DynamicPropertySource
        void properties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        }

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    @Tag("integration")
    class jUnit5Example extends Junit5ITInitializer {

        @Test
        void connectionEstablished() {
            assertThat(postgres.isCreated()).isTrue();
            assertThat(postgres.isRunning()).isTrue();
            System.out.println("Port: " + postgres.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgres.getJdbcUrl());
        }
    }

    @ActiveProfiles("test")
    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @Import(PostgresContainerBean.class)
    @Tag("integration")
    @Nested
    class WeirdExample {
        @Autowired
        private PostgreSQLContainer<?> postgresContainer;

        @Test
        void connectionEstablished() {
            assertThat(postgresContainer.isCreated()).isTrue();
            assertThat(postgresContainer.isRunning()).isTrue();
            System.out.println("Port: " + postgresContainer.getMappedPort(5432));
            System.out.println("JDBC URL -> " + postgresContainer.getJdbcUrl());
        }
    }
}
