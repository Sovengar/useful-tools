package testing.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import testing._config.BasicTestTags;
import testing.cucumber.tennis_kata.config.TennisCucumberContext;
import testing.cucumber.tennis_kata.TennisScore;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🔌 TennisScoreFeatureSteps
 * 
 * Estas son las "Step Definitions" que mapean el lenguaje Gherkin a código
 * Java.
 * 
 * 🛠️ Inyección de Dependencias:
 * Usamos el constructor para recibir el 'TennisCucumberContext'. Cucumber y
 * Spring
 * se encargan de que todos los steps de un mismo escenario compartan la misma
 * instancia.
 */
@BasicTestTags
public class TennisScoreFeatureSteps {

    private final TennisCucumberContext context;

    public TennisScoreFeatureSteps(TennisCucumberContext context) {
        this.context = context;
    }

    @Given("A new tennis game")
    public void a_new_tennis_game() {
        // Inicializamos el juego en el contexto compartido
        context.setTennisScore(new TennisScore());
    }

    @When("Player{int} scores")
    public void playerScores(int playerNo) {
        context.getTennisScore().addPoint(playerNo);
    }

    @When("Player{int} scores {int} points")
    public void playerScoresPoints(int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            context.getTennisScore().addPoint(playerNo);
        }
    }

    @Then("Score is {string}")
    public void score_is(String expected) {
        assertThat(context.getTennisScore().score()).isEqualTo(expected);
    }
}
