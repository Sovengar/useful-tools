package testing.junit5.parameterized;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JUnit 5: Tests Parametrizados Avanzados")
class ParameterizedShowcaseTest {

    @ParameterizedTest
    @EnumSource(java.util.concurrent.TimeUnit.class)
    @DisplayName("Carga desde Enum")
    void enumSource(java.util.concurrent.TimeUnit unit) {
        assertThat(unit).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 3, 5",
            "10, 20, 30"
    })
    @DisplayName("Carga desde CSV")
    void csvSource(int a, int b, int expected) {
        assertThat(a + b).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Usuario: {0}")
    @MethodSource("provideUsers")
    @DisplayName("Carga desde Objetos (MethodSource)")
    void objectSource(User user) {
        assertThat(user.getName()).isNotEmpty();
        assertThat(user.getAge()).isGreaterThan(0);
    }

    static Stream<User> provideUsers() {
        return Stream.of(
                new User("Alice", 25),
                new User("Bob", 30));
    }

    // Clase auxiliar para el ejemplo
    static class User {
        private final String name;
        private final int age;

        User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
