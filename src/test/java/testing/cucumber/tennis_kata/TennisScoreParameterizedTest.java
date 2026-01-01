package testing.cucumber.tennis_kata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testing._config.BasicTestTags;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 🧪 Test Parametrizado (JUnit 5)
 * 
 * ❓ ¿Cómo se compara con Cucumber?
 * 1. 🏗️ Estructura: JUnit es puramente técnico y vive cerca del código.
 * Cucumber es semántico y vive en ficheros .feature.
 * 2. 👥 Audiencia: JUnit es para desarrolladores.
 * Cucumber permite que analistas de negocio o QA lean los requerimientos.
 * 3. 🛠️ Mantenimiento: JUnit suele ser más rápido de escribir y refactorizar.
 * Cucumber requiere mantener el "Glue Code" (Step Definitions).
 * 4. 📊 Datos: Aquí usamos @MethodSource para alimentar el test con una tabla
 * técnica.
 */
@BasicTestTags
class TennisScoreParameterizedTest {
    TennisScore tennisScore = new TennisScore();

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(1, 1, "Fifteen-Fifteen"),
                Arguments.of(1, 0, "Fifteen-Love"),
                Arguments.of(0, 1, "Love-Fifteen"),
                Arguments.of(3, 3, "Deuce"),
                Arguments.of(4, 4, "Deuce"),
                Arguments.of(4, 2, "Game won Player1"), // Victoria directa
                Arguments.of(2, 4, "Game won Player2"), // Victoria directa
                Arguments.of(4, 3, "Advantage Player1"), // Tras Deuce
                Arguments.of(3, 4, "Advantage Player2"), // Tras Deuce
                Arguments.of(5, 3, "Game won Player1"), // Victoria tras Advantage
                Arguments.of(3, 5, "Game won Player2") // Victoria tras Advantage
        );
    }

    @ParameterizedTest(name = "P1: {0} puntos, P2: {1} puntos => Marcador: {2}")
    @DisplayName("Cualquier combinación de puntos debe resultar en el marcador correcto")
    @MethodSource("data")
    void tennisScoreTest(int player1Points, int player2Points, String expectedScore) {
        addPointsToPlayer(1, player1Points);
        addPointsToPlayer(2, player2Points);

        assertThat(tennisScore.score()).isEqualTo(expectedScore);
    }

    private void addPointsToPlayer(int playerNumber, int points) {
        for (int i = 0; i < points; i++) {
            tennisScore.addPoint(playerNumber);
        }
    }
}
