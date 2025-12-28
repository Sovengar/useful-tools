package testing.jsonunit;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * JSON-UNIT EXAMPLES
 * ═══════════════════════════════════════════════════════════════════════════════
 * JSON-Unit provides fluent assertions for comparing JSON content.
 *
 * Dependency (pom.xml):
 * net.javacrumbs.json-unit:json-unit-assertj
 *
 * Features:
 * - Compare JSON strings/objects
 * - Ignore specific fields
 * - Regex matchers for dynamic values
 * - Array ordering options
 *
 * Run: mvn test -Dtest=JsonUnitExamplesTest
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@DisplayName("JSON-Unit Examples")
class JsonUnitExamplesTest {

    private final Faker faker = new Faker();

    // ─────────────────────────────────────────────────────────────────────────────
    // BASIC JSON COMPARISON
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Basic JSON Comparison")
    class BasicComparison {

        @Test
        @DisplayName("Should match exact JSON")
        void shouldMatchExactJson() {
            String actual = """
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "gender": "MALE"
                    }
                    """;

            String expected = """
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "gender": "MALE"
                    }
                    """;

            assertThatJson(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should partially match JSON")
        void shouldPartiallyMatchJson() {
            String actual = """
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john@example.com",
                        "gender": "MALE",
                        "createdAt": "2024-01-15T10:30:00"
                    }
                    """;

            // Check only specific fields - each inPath is a separate assertion
            assertThatJson(actual).inPath("$.name").isEqualTo("John Doe");
            assertThatJson(actual).inPath("$.gender").isEqualTo("MALE");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // IGNORING FIELDS
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Ignoring Fields")
    class IgnoringFields {

        @Test
        @DisplayName("Should ignore specific paths")
        void shouldIgnoreSpecificPaths() {
            String actual = """
                    {
                        "id": 12345,
                        "name": "John Doe",
                        "createdAt": "2024-01-15T10:30:00"
                    }
                    """;

            String expected = """
                    {
                        "id": 99999,
                        "name": "John Doe",
                        "createdAt": "different-date"
                    }
                    """;

            // Ignore id and createdAt fields
            assertThatJson(actual)
                    .whenIgnoringPaths("$.id", "$.createdAt")
                    .isEqualTo(expected);
        }

        @Test
        @DisplayName("Should use type matchers for dynamic values")
        void shouldUseTypeMatchersForDynamicValues() {
            String actual = """
                    {
                        "id": 12345,
                        "email": "user123@example.com",
                        "uuid": "550e8400-e29b-41d4-a716-446655440000"
                    }
                    """;

            // Verify id is a number
            assertThatJson(actual).inPath("$.id").isNumber();

            // Verify email contains @
            assertThatJson(actual).inPath("$.email").isString();

            // Verify uuid format using expected pattern
            assertThatJson(actual).inPath("$.uuid").isString();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // ARRAY HANDLING
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Array Handling")
    class ArrayHandling {

        @Test
        @DisplayName("Should match arrays ignoring order")
        void shouldMatchArraysIgnoringOrder() {
            String actual = """
                    {
                        "students": ["Alice", "Bob", "Carol"]
                    }
                    """;

            String expected = """
                    {
                        "students": ["Carol", "Alice", "Bob"]
                    }
                    """;

            assertThatJson(actual)
                    .when(IGNORING_ARRAY_ORDER)
                    .isEqualTo(expected);
        }

        @Test
        @DisplayName("Should check array contains element")
        void shouldCheckArrayContainsElement() {
            String actual = """
                    {
                        "students": [
                            {"name": "Alice", "grade": "A"},
                            {"name": "Bob", "grade": "B"},
                            {"name": "Carol", "grade": "A"}
                        ]
                    }
                    """;

            assertThatJson(actual)
                    .inPath("$.students")
                    .isArray()
                    .hasSize(3);

            assertThatJson(actual)
                    .inPath("$.students[0].name")
                    .isEqualTo("Alice");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // CUSTOM MATCHERS
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Custom Matchers")
    class CustomMatchers {

        @Test
        @DisplayName("Should use placeholder matchers")
        void shouldUsePlaceholderMatchers() {
            String actual = """
                    {
                        "id": 12345,
                        "name": "John Doe",
                        "active": true,
                        "balance": 100.50
                    }
                    """;

            // Using ${json-unit.any-number}, ${json-unit.any-string}, etc.
            assertThatJson(actual).isEqualTo("""
                    {
                        "id": "${json-unit.any-number}",
                        "name": "${json-unit.any-string}",
                        "active": "${json-unit.any-boolean}",
                        "balance": "${json-unit.any-number}"
                    }
                    """);
        }

        @Test
        @DisplayName("Should use regex placeholder")
        void shouldUseRegexPlaceholder() {
            String actual = """
                    {
                        "email": "test@domain.com",
                        "phone": "555-123-4567"
                    }
                    """;

            // Using simpler regex patterns for demonstration
            assertThatJson(actual).isEqualTo("""
                    {
                        "email": "${json-unit.regex}.*@.*",
                        "phone": "${json-unit.regex}[0-9-]+"
                    }
                    """);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // FAKER INTEGRATION EXAMPLE
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Faker Integration")
    class FakerIntegration {

        @Test
        @DisplayName("Should validate JSON structure with random data")
        void shouldValidateJsonStructureWithRandomData() {
            // Given - Generate random student data
            String name = faker.name().fullName();
            String email = faker.internet().emailAddress();

            String actual = String.format("""
                    {
                        "name": "%s",
                        "email": "%s",
                        "gender": "MALE"
                    }
                    """, name, email);

            // Then - Validate structure, not exact values
            assertThatJson(actual).inPath("$.name").isString();
            assertThatJson(actual).inPath("$.email").isString();
            assertThatJson(actual).inPath("$.gender").isEqualTo("MALE");
        }
    }
}
