package testing.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class TransformerSteps {

    private String result;

    @Given("I have a transformer service")
    public void iHaveATransformerService() {
        // In a real scenario, this could be a Spring bean injection
    }

    @When("I transform the message {string}")
    public void iTransformTheMessage(String message) {
        this.result = message.toUpperCase();
    }

    @Then("the result should be {string}")
    public void theResultShouldBe(String expected) {
        assertThat(result).isEqualTo(expected);
    }
}
