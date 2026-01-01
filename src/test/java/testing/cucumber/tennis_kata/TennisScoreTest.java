package testing.cucumber.tennis_kata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testing._config.BasicTestTags;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 Test Unitario (JUnit 5)
 * 
 * Este es un test unitario clásico que valida escenarios individuales.
 * A diferencia del test parametrizado, aquí cada método documenta un caso de
 * uso
 * específico con su propio nombre descriptivo.
 */
@BasicTestTags
class TennisScoreTest {
    TennisScore tennisScore = new TennisScore();

    @Test
    @DisplayName("El marcador inicial debe ser Love-Love")
    void loveLove() {
        setPlayerScore(1, 0);
        setPlayerScore(2, 0);
        assertThat(tennisScore.score()).isEqualTo("Love-Love");
    }

    @Test
    @DisplayName("0-15: Un punto para el jugador 2")
    void loveFifteen() {
        setPlayerScore(1, 0);
        setPlayerScore(2, 1);
        assertThat(tennisScore.score()).isEqualTo("Love-Fifteen");
    }

    @Test
    @DisplayName("15-0: Un punto para el jugador 1")
    void fifteenLove() {
        setPlayerScore(1, 1);
        setPlayerScore(2, 0);
        assertThat(tennisScore.score()).isEqualTo("Fifteen-Love");
    }

    @Test
    @DisplayName("30-0: Dos puntos para el jugador 1")
    void thirtyLove() {
        setPlayerScore(1, 2);
        setPlayerScore(2, 0);
        assertThat(tennisScore.score()).isEqualTo("Thirty-Love");
    }

    @Test
    @DisplayName("40-0: Tres puntos para el jugador 1")
    void fortyLove() {
        setPlayerScore(1, 3);
        setPlayerScore(2, 0);
        assertThat(tennisScore.score()).isEqualTo("Forty-Love");
    }

    @Test
    @DisplayName("40-40: Empate a 3 o más es Deuce")
    void deuce() {
        setPlayerScore(1, 3);
        setPlayerScore(2, 3);
        assertThat(tennisScore.score()).isEqualTo("Deuce");
    }

    @Test
    @DisplayName("Ventaja para el jugador 1 tras Deuce")
    void advantagePlayer1() {
        setPlayerScore(1, 7);
        setPlayerScore(2, 6);
        assertThat(tennisScore.score()).isEqualTo("Advantage Player1");
    }

    @Test
    @DisplayName("Ventaja para el jugador 2 tras Deuce")
    void advantagePlayer2() {
        setPlayerScore(1, 3);
        setPlayerScore(2, 4);
        assertThat(tennisScore.score()).isEqualTo("Advantage Player2");
    }

    @Test
    @DisplayName("Victoria rápida (4-0) para el jugador 1")
    void player1WonFast() {
        setPlayerScore(1, 4);
        setPlayerScore(2, 0);
        assertThat(tennisScore.score()).isEqualTo("Game won Player1");
    }

    @Test
    @DisplayName("Victoria para el jugador 1 tras un marcador largo")
    void player1WonAfterDeuce() {
        setPlayerScore(1, 7);
        setPlayerScore(2, 5);
        assertThat(tennisScore.score()).isEqualTo("Game won Player1");
    }

    @Test
    @DisplayName("Victoria para el jugador 2")
    void player2Won() {
        setPlayerScore(1, 0);
        setPlayerScore(2, 4);
        assertThat(tennisScore.score()).isEqualTo("Game won Player2");
    }

    private void setPlayerScore(int playerNumber, int playerScore) {
        for (int i = 0; i < playerScore; i++) {
            tennisScore.addPoint(playerNumber);
        }
    }
}
