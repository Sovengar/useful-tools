package testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import testing._utils.ConcurrentTest;
import testing.testcontainers.config.initializers.UseInfraInitializer1;
import testing.domain.Gender;
import testing.domain.models.Student;
import testing.domain.repositories.StudentQueryRepository;
import testing.domain.repositories.StudentRepository;

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
@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest
@Transactional
@UseInfraInitializer1
@DisplayName("QueryDSL Repository Integration Tests")
class QueryDslIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentQueryRepository queryRepository;

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
