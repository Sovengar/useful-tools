package testing.testcontainers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import testing.config.initializers.UseInfraInitializer1;
import testing.config.initializers.UseInfraInitializer3;
import testing.config.initializers.jvm_level.InfraInitializer2;
import testing.config.initializers.test_class_level.Junit5ITInitializer;

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
class TestContainersExamples {
    @Nested
    @SpringBootTest
    @UseInfraInitializer3
    class ExtendsWithExample {
        @Test
        void test() {

        }
    }

    @Nested
    @SpringBootTest
    @UseInfraInitializer1
    class ImportExample {
        @Test
        void test() {

        }
    }

    @Nested
    @SpringBootTest
    class ExtendsExample extends InfraInitializer2 {
        @Test
        void test() {

        }
    }

    @Nested
    @SpringBootTest
    class InClassExample {
        public static GenericContainer postgreSQLContainer = new PostgreSQLContainer().withReuse(true);

        // public static final PostgreSQLContainer<?> postgres = new
        // PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("16-alpine"))
        // .withReuse(true); // Ensure that testcontainers.reuse.enable=true is set in
        // ~/.testcontainers.properties

        @BeforeAll
        public static void beforeAll() {
            postgreSQLContainer.start(); // When is started once, when another class executes his start, it will use
                                         // this one
        }

        @Test
        void test() {

        }
    }

    @Nested
    @SpringBootTest
    class jUnit5Example extends Junit5ITInitializer {

        @Test
        void test() {

        }
    }
}
