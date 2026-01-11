package testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import testing._utils.ConcurrentTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

/**
 * Mockito Showcase Test
 *
 * Esta clase muestra las funcionalidades más importantes de Mockito.
 * Se utiliza MockitoExtension para inicializar los mocks automáticamente
 * con @Mock.
 */
@ExtendWith(MockitoExtension.class)
@ConcurrentTest
class MockitoShowcaseTest {

    @Mock
    private ExternalService externalService;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    // Interface para mockear en los ejemplos
    interface ExternalService {
        String fetchData(String id);

        int process(int value);

        void performAction(String data);
    }

    // Clase de utilidad estática para el ejemplo de mocking estático
    static class StaticUtils {
        static String staticMethod(String input) {
            return "Real: " + input;
        }
    }

    @Nested
    @DisplayName("1. Basic Stubbing & Dynamic Stubs")
    class Stubbing {

        @Test
        @DisplayName("Stubbing básico con thenReturn")
        void basicStubbing() {
            // Given
            when(externalService.fetchData("1")).thenReturn("Data 1");

            // When
            String result = externalService.fetchData("1");

            // Then
            assertThat(result).isEqualTo("Data 1");
        }

        @Test
        @DisplayName("Dynamic stubbing con thenAnswer (Cálculos dinámicos)")
        void dynamicStubbing() {
            // Permite ejecutar lógica custom al llamar al mock
            when(externalService.process(anyInt())).thenAnswer(invocation -> {
                int arg = invocation.getArgument(0);
                return arg * 2;
            });

            assertThat(externalService.process(10)).isEqualTo(20);
            assertThat(externalService.process(5)).isEqualTo(10);
        }

        @Test
        @DisplayName("Stubbing secuencial (Diferentes retornos para la misma llamada)")
        void sequentialStubbing() {
            // Mockito permite encadenar respuestas
            when(externalService.fetchData("id"))
                    .thenReturn("First Call")
                    .thenReturn("Second Call")
                    .thenThrow(new RuntimeException("Third Call Failed"));

            assertThat(externalService.fetchData("id")).isEqualTo("First Call");
            assertThat(externalService.fetchData("id")).isEqualTo("Second Call");
            assertThatThrownBy(() -> externalService.fetchData("id"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Stubbing con retry simulado (Secuencial)")
        void stubbingWithRetrySimulation() {
            // Simulamos que falla las dos primeras veces y funciona a la tercera
            when(externalService.fetchData("retry-id"))
                    .thenThrow(new RuntimeException("Service Unavailable"))
                    .thenThrow(new RuntimeException("Service Unavailable"))
                    .thenReturn("Success after retry");

            // Primer intento
            assertThatThrownBy(() -> externalService.fetchData("retry-id")).hasMessage("Service Unavailable");
            // Segundo intento
            assertThatThrownBy(() -> externalService.fetchData("retry-id")).hasMessage("Service Unavailable");
            // Tercer intento (Success!)
            assertThat(externalService.fetchData("retry-id")).isEqualTo("Success after retry");
        }
    }

    @Nested
    @DisplayName("2. Verification & Matchers")
    class Verification {

        @Test
        @DisplayName("Verificar número de llamadas")
        void verificationTimes() {
            externalService.performAction("data");
            externalService.performAction("data");

            // Verificaciones básicas
            verify(externalService, times(2)).performAction("data");
            verify(externalService, atLeastOnce()).performAction("data");
            verify(externalService, never()).performAction("other");
        }

        @Test
        @DisplayName("Verificar orden de las llamadas (InOrder)")
        void verificationOrder() {
            externalService.fetchData("1");
            externalService.performAction("done");

            InOrder inOrder = inOrder(externalService);

            inOrder.verify(externalService).fetchData("1");
            inOrder.verify(externalService).performAction("done");
        }

        @Test
        @DisplayName("Argument Matchers (any, eq, startsWith)")
        void argumentMatchers() {
            when(externalService.fetchData(argThat(s -> s.length() > 5))).thenReturn("Long ID");
            when(externalService.fetchData(startsWith("user"))).thenReturn("User Search");

            assertThat(externalService.fetchData("123456")).isEqualTo("Long ID");
            assertThat(externalService.fetchData("user-99")).isEqualTo("User Search");
        }

        @Test
        @DisplayName("Argument Captor (Capturar parámetros para aserciones complejas)")
        void argumentCaptors() {
            externalService.performAction("Complex payload with multiple fields");

            verify(externalService).performAction(stringCaptor.capture());

            assertThat(stringCaptor.getValue())
                    .contains("Complex payload")
                    .hasSizeGreaterThan(10);
        }
    }

    @Nested
    @DisplayName("3. Spies")
    class Spies {

        @Test
        @DisplayName("Spies (Mocks parciales de objetos reales)")
        void spiesExample() {
            // Un Spy usa el objeto REAL por defecto
            List<String> list = new ArrayList<>();
            List<String> spyList = spy(list);

            spyList.add("one");
            spyList.add("two");

            verify(spyList).add("one");
            assertThat(spyList).hasSize(2);

            // Podemos mockear solo un método del spy
            // IMPORTANTE: Usar doReturn().when() con spies para evitar llamar al método
            // real durante el stubbing
            doReturn(100).when(spyList).size();

            assertThat(spyList.size()).isEqualTo(100);
            assertThat(spyList.get(0)).isEqualTo("one"); // Sigue funcionando el objeto real
        }
    }

    @Nested
    @DisplayName("4. BDD Mockito (Given/Then/Should)")
    class BddStyle {

        @Test
        @DisplayName("Estilo BDD (Mas legible)")
        void bddStyleTests() {
            // Given (Antiguamente when)
            given(externalService.fetchData("bdd")).willReturn("BDD Value");

            // When
            String result = externalService.fetchData("bdd");

            // Then (Antiguamente verify)
            assertThat(result).isEqualTo("BDD Value");
            then(externalService).should().fetchData("bdd");
        }
    }

    @Nested
    @DisplayName("5. Static Mocking")
    class StaticMocking {

        @Test
        @DisplayName("Mocking métodos estáticos con try-with-resources")
        void staticMockExample() {
            // El mock estático DEBE cerrarse para no contaminar otros tests,
            // por eso es obligatorio el uso de try-with-resources.
            try (MockedStatic<StaticUtils> mockedStatic = mockStatic(StaticUtils.class)) {

                mockedStatic.when(() -> StaticUtils.staticMethod("hi"))
                        .thenReturn("Mocked Hi!");

                assertThat(StaticUtils.staticMethod("hi")).isEqualTo("Mocked Hi!");

                // Verificación del mock estático
                mockedStatic.verify(() -> StaticUtils.staticMethod("hi"));
            }

            // Fuera del try-with-resources, el método vuelve a su comportamiento original
            assertThat(StaticUtils.staticMethod("hi")).isEqualTo("Real: hi");
        }
    }

    @Nested
    @DisplayName("6. Deep Stubs (RETURNS_DEEP_STUBS)")
    class DeepStubs {

        @Test
        @DisplayName("Deep stubs para mockear cadenas de llamadas (Nested objects)")
        void deepStubsExample() {
            // RETURNS_DEEP_STUBS permite mockear una cadena de llamadas (ej:
            // a.getB().getC().doSomething())
            // sin tener que mockear cada objeto intermedio manualmente.

            // ADVERTENCIA: Su uso suele indicar un diseño pobre y violación de la Ley de
            // Demeter.
            // Es útil principalmente para trabajar con código legado difícil de
            // refactorizar.

            ComplexService complexService = mock(ComplexService.class, RETURNS_DEEP_STUBS);

            // Stubbing de la cadena completa
            when(complexService.getDeepLevel().getNestedData().getValue())
                    .thenReturn("Deep Value Found!");

            // Verificación
            assertThat(complexService.getDeepLevel().getNestedData().getValue())
                    .isEqualTo("Deep Value Found!");
        }

        interface ComplexService {
            DeepLevel getDeepLevel();
        }

        interface DeepLevel {
            NestedData getNestedData();
        }

        interface NestedData {
            String getValue();
        }
    }
}
