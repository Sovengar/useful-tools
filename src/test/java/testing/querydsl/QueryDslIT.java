package testing.querydsl;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import testing.studentModel.Gender;
import testing.studentModel.Student;
import testing.studentModel.StudentQueryRepository;
import testing.studentModel.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * QUERYDSL INTEGRATION TEST
 * ═══════════════════════════════════════════════════════════════════════════════
 * Tests for StudentQueryRepository using QueryDSL with real database.
 *
 * Dependencies (pom.xml):
 * - com.querydsl:querydsl-jpa
 * - org.testcontainers:postgresql
 *
 * Run: mvn test -Dtest=QueryDslIntegrationTest
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@SpringBootTest
@Testcontainers
@Transactional
@DisplayName("QueryDSL Repository Integration Tests")
@ActiveProfiles("test")
class QueryDslIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl().replace("jdbc:", "jdbc:p6spy:"));
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private StudentQueryRepository queryRepository;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        queryRepository = new StudentQueryRepository(entityManager);
        studentRepository.deleteAll();
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // BASIC QUERIES
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Basic Queries")
    class BasicQueries {

        @Test
        @DisplayName("Should find all students ordered by name")
        void shouldFindAllOrderedByName() {
            // Given
            studentRepository.save(new Student("Zara", "zara@test.com", Gender.FEMALE));
            studentRepository.save(new Student("Alice", "alice@test.com", Gender.FEMALE));
            studentRepository.save(new Student("Mike", "mike@test.com", Gender.MALE));

            // When
            List<Student> result = queryRepository.findAllOrderedByName();

            // Then
            assertThat(result)
                    .hasSize(3)
                    .extracting(Student::getName)
                    .containsExactly("Alice", "Mike", "Zara");
        }

        @Test
        @DisplayName("Should find student by email")
        void shouldFindByEmail() {
            // Given
            Student saved = studentRepository.save(
                    new Student("John", "john@test.com", Gender.MALE));

            // When
            var result = queryRepository.findByEmail("john@test.com");

            // Then
            assertThat(result)
                    .isPresent()
                    .hasValueSatisfying(s -> {
                        assertThat(s.getName()).isEqualTo("John");
                        assertThat(s.getId()).isEqualTo(saved.getId());
                    });
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // DYNAMIC QUERIES
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Dynamic Queries")
    class DynamicQueries {

        @Test
        @DisplayName("Should search by name contains")
        void shouldSearchByNameContains() {
            // Given
            studentRepository.save(new Student("John Doe", "john@test.com", Gender.MALE));
            studentRepository.save(new Student("Jane Doe", "jane@test.com", Gender.FEMALE));
            studentRepository.save(new Student("Bob Smith", "bob@test.com", Gender.MALE));

            // When
            List<Student> result = queryRepository.searchStudents("Doe", null, null);

            // Then
            assertThat(result)
                    .hasSize(2)
                    .allMatch(s -> s.getName().contains("Doe"));
        }

        @Test
        @DisplayName("Should search by gender")
        void shouldSearchByGender() {
            // Given
            studentRepository.save(new Student("Alice", "alice@test.com", Gender.FEMALE));
            studentRepository.save(new Student("Bob", "bob@test.com", Gender.MALE));
            studentRepository.save(new Student("Carol", "carol@test.com", Gender.FEMALE));

            // When
            List<Student> result = queryRepository.searchStudents(null, Gender.FEMALE, null);

            // Then
            assertThat(result)
                    .hasSize(2)
                    .allMatch(s -> s.getGender() == Gender.FEMALE);
        }
    }
}
