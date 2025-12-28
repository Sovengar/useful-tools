package testing.approvaltests;

import com.github.javafaker.Faker;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.approvaltests.reporters.QuietReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testing.studentModel.Gender;
import testing.studentModel.Student;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPROVALTESTS EXAMPLES
 * ═══════════════════════════════════════════════════════════════════════════════
 * ApprovalTests is a library for "Golden Master" or "Snapshot" testing.
 *
 * Dependency (pom.xml):
 * com.approvaltests:approvaltests
 *
 * How it works:
 * 1. First run creates a .received file with current output
 * 2. You approve it by renaming to .approved file
 * 3. Future runs compare against .approved file
 *
 * Files created:
 * - ApprovalTestsExamples.testStudentToString.approved.txt
 * - ApprovalTestsExamples.testStudentToString.received.txt (when test fails)
 *
 * Run: mvn test -Dtest=ApprovalTestsExamples
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@DisplayName("Approval Tests Examples")
@UseReporter(QuietReporter.class) // Use QuietReporter in CI, remove for local development
class ApprovalTestsExamples {

    private final Faker faker = new Faker();

    // ─────────────────────────────────────────────────────────────────────────────
    // BASIC APPROVAL TEST
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Verify Student toString format")
    void testStudentToString() {
        // Given
        Student student = new Student("John Doe", "john.doe@example.com", Gender.MALE);
        student.setId(1L);

        // When
        String result = student.toString();

        // Then - Approvals will compare with .approved file
        Approvals.verify(result);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // APPROVAL TEST WITH LIST
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Verify list of students format")
    void testStudentListFormat() {
        // Given
        List<Student> students = List.of(
                createStudent(1L, "Alice", "alice@test.com", Gender.FEMALE),
                createStudent(2L, "Bob", "bob@test.com", Gender.MALE),
                createStudent(3L, "Carol", "carol@test.com", Gender.FEMALE));

        // When
        String result = students.stream()
                .map(s -> String.format("ID: %d, Name: %s, Email: %s, Gender: %s",
                        s.getId(), s.getName(), s.getEmail(), s.getGender()))
                .collect(Collectors.joining("\n"));

        // Then
        Approvals.verify(result);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // APPROVAL TEST WITH JSON (using custom formatter)
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Verify Student as JSON format")
    void testStudentAsJson() {
        // Given
        Student student = new Student("Jane Smith", "jane.smith@example.com", Gender.FEMALE);
        student.setId(42L);

        // When - Manual JSON formatting (or use Jackson)
        String json = String.format("""
                {
                  "id": %d,
                  "name": "%s",
                  "email": "%s",
                  "gender": "%s"
                }""",
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getGender());

        // Then - Approve with .json extension
        Approvals.verify(json, new Options().forFile().withExtension(".json"));
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // COMBINATION APPROVAL (testing multiple inputs)
    // ─────────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Verify all gender combinations")
    void testAllGenderCombinations() {
        // Given
        StringBuilder result = new StringBuilder();

        // When - Test all enum values
        for (Gender gender : Gender.values()) {
            Student student = new Student("Test User", "test@example.com", gender);
            student.setId(1L);
            result.append(String.format("Gender: %s -> %s%n", gender, student.toString()));
        }

        // Then
        Approvals.verify(result.toString());
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // HELPER METHODS
    // ─────────────────────────────────────────────────────────────────────────────

    private Student createStudent(Long id, String name, String email, Gender gender) {
        Student student = new Student(name, email, gender);
        student.setId(id);
        return student;
    }
}
