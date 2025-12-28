package testing;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * PITEST - MUTATION TESTING DEMO
 * ═══════════════════════════════════════════════════════════════════════════════
 * This test is INTENTIONALLY WEAK.
 * It checks if 10 is positive, but it does NOT check 0.
 *
 * Use command:
 * ./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
 *
 * Expected Result:
 * - A mutation "changed conditional boundary" (>= to >) will SURVIVE.
 * - This shows that our test suite is not robust enough.
 * ═══════════════════════════════════════════════════════════════════════════════
 */
class PitestTest {

    @Test
    void testIsPositive_WeakTest() {
        Calculator calc = new Calculator();

        // Weak Test: Only checks a number widely inside the range.
        // It fails to detect if logic changes to (number > 0)
        assertThat(calc.isPositive(10)).isTrue();
    }
}

