package testing.awaitility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * 🚀 Awaitility Showcase
 *
 * ❓ ¿Por qué es mejor que Thread.sleep()?
 * 1. ⏱️ Determinismo: En lugar de esperar un tiempo fijo (ej. 5s), Awaitility
 * consulta (polling)
 * continuamente la condición. Si la condición se cumple en 100ms, el test
 * continúa DE INMEDIATO.
 * 2. 🛡️ Tolerancia a Fallos: Permite configurar intervalos de poleo, ignores
 * de excepciones y timeouts claros.
 * 3. 📖 Legibilidad: El código describe LA INTENCIÓN (esperar a que ocurra X)
 * no la acción técnica (dormir hilo).
 * 4. 📉 Eficiencia: Reduce drásticamente el tiempo de ejecución de las suites
 * de tests asíncronos.
 */
@Tag("async")
class AwaitilityShowcaseTest {

    @Test
    @DisplayName("Ejemplo básico: Esperar a que un flag cambie")
    void basicAwaitility() {
        AtomicBoolean conditionMet = new AtomicBoolean(false);

        // Simulamos un proceso asíncrono
        new Thread(() -> {
            try {
                Thread.sleep(500);
                conditionMet.set(true);
            } catch (InterruptedException ignored) {
            }
        }).start();

        // ❌ MAL: Thread.sleep(1000); -> Esperas 1s aunque el proceso termine en 500ms.
        // ✅ BIEN: Awaitility continua apenas el flag es true.
        await()
                .atMost(Duration.ofSeconds(2))
                .untilTrue(conditionMet);

        assertThat(conditionMet.get()).isTrue();
    }

    @Test
    @DisplayName("Configuración avanzada: Poll interval y Timeouts")
    void advancedConfiguration() {
        AtomicBoolean conditionMet = new AtomicBoolean(false);

        new Thread(() -> {
            try {
                Thread.sleep(300);
                conditionMet.set(true);
            } catch (InterruptedException ignored) {
            }
        }).start();

        await()
                .alias("Esperando a que el estado sea consistente")
                .atMost(Duration.ofSeconds(1)) // Máximo tiempo de espera
                .pollInterval(Duration.ofMillis(50)) // Cada cuánto tiempo re-evalúa la condición
                .until(conditionMet::get); // Condición (Callable o Atomic)
    }

    @Test
    @DisplayName("Ignorar excepciones durante el periodo de polling")
    void ignoreExceptions() {
        // Útil si el objeto que consultamos aún no existe (ej:
        // ResourceNotFoundException)
        class Service {
            int calls = 0;

            String getData() {
                if (calls++ < 3)
                    throw new IllegalStateException("Not ready yet!");
                return "SUCCESS";
            }
        }

        final Service service = new Service();

        await()
                .atMost(Duration.ofSeconds(2))
                .ignoreExceptionsMatching(e -> e instanceof IllegalStateException)
                .until(() -> service.getData().equals("SUCCESS"));
    }
}
