package testing;

/**
 * Checks if a number is positive (including zero).
 */
public class Calculator {
    public boolean isPositive(int number) {
        // Mutation Opportunity:
        // Pitest will try to change ">=" to ">" (Conditional Boundary Mutator).
        // If our test only checks "10", it will pass both conditions.
        // So the mutation will SURVIVE (which is bad/vulnerable).
        if (number >= 0) {
            return true;
        }
        return false;
    }
}
