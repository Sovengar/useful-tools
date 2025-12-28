package testing.jqwik;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.NotEmpty;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * JQWIK - PROPERTY-BASED TESTING EXAMPLE
 * ═══════════════════════════════════════════════════════════════════════════════
 * Instead of testing specific examples, we test "Properties" (invariants) that
 * must hold true for ALL generated inputs.
 *
 * Dependencies (pom.xml):
 * - net.jqwik:jqwik
 * ═══════════════════════════════════════════════════════════════════════════════
 */
class JqwikExampleTest {

    @Property
    void lengthOfConcatenatedStringIsSumOfLengths(
            @ForAll @NotEmpty String a,
            @ForAll @NotEmpty String b) {
        String combined = a + b;
        assertThat(combined.length()).isEqualTo(a.length() + b.length());
    }

    @Property
    void additionIsCommutative(
            @ForAll int a,
            @ForAll int b) {
        assertThat(a + b).isEqualTo(b + a);
    }

    @Property
    void absoluteValueIsAlwaysNonNegative(
            @ForAll int value) {
        // Edge case found by jqwik: Math.abs(Integer.MIN_VALUE) is still negative!
        if (value == Integer.MIN_VALUE)
            return;

        assertThat(Math.abs(value)).isGreaterThanOrEqualTo(0);
    }

    @Property
    void stringContainsItself(
            @ForAll String value) {
        assertThat(value).contains(value);
    }

    @Property
    void percentageShouldStayInValidRange(
            @ForAll @IntRange(min = 0, max = 100) int percentage) {
        // Business rule simulation: A percentage must always be between 0 and 100
        assertThat(percentage).isBetween(0, 100);
    }
}
