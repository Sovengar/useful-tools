package testing.config;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

// use via @DisplayNameGeneration on a test class
public class PrettyTestNames implements DisplayNameGenerator {
    private PrettyTestNames() {
    }

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        String name = testClass.getSimpleName();
        return replaceCapitals(name);
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        String name = nestedClass.getSimpleName();
        return replaceCapitals(name);
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        String name = testMethod.getName();
        return replaceUnderscoresAndTrim(name);
    }

    private String replaceUnderscoresAndTrim(final String name) {
        return name.replaceAll("_", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String replaceCapitals(String name) {
        // First, handle special suffixes
        name = name.replaceAll("(IT|DTO|API)$", "$1");

        // Then handle a regular camel case
        name = name.replaceAll("([A-Z])([A-Z][a-z])", "$1 $2")
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("_", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase();
        return name;
    }
}
