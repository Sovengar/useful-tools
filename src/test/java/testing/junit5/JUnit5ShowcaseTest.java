package testing.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JUnit 5 Showcase: Anotaciones y Ciclo de Vida")
@Tag("unit")
class JUnit5ShowcaseTest {

    @BeforeAll
    static void initAll() {
        // Se ejecuta una vez antes de todos los tests de la clase
    }

    @BeforeEach
    void init() {
        // Se ejecuta antes de cada test
    }

    @Test
    @DisplayName("✔ Test básico con nombre personalizado")
    void basicTest() {
        assertThat(true).isTrue();
    }

    @ParameterizedTest(name = "Iteración {index}: valor={0}")
    @ValueSource(strings = { "JUnit", "AssertJ", "Mockito" })
    @DisplayName("Tests parametrizados simples")
    void simpleParameterizedTest(String word) {
        assertThat(word).isNotEmpty();
    }

    @Nested
    @DisplayName("Tests Agrupados (Nested)")
    class NestedTests {
        @Test
        @DisplayName("Test dentro de clase anidada")
        void nestedTest() {
            assertThat(1 + 1).isEqualTo(2);
        }
    }

    @Test
    @Disabled("Ejemplo de test deshabilitado")
    void disabledTest() {
        // No se ejecutará
    }

    @AfterEach
    void tearDown() {
        // Se ejecuta después de cada test
    }

    @AfterAll
    static void tearDownAll() {
        // Se ejecuta una vez después de todos los tests
    }
}
