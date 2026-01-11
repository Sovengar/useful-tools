package testing;

import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testing._utils.ConcurrentTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lombok Showcase Test
 *
 * Esta clase muestra las funcionalidades más importantes de Lombok.
 * Lombok reduce el boilerplate (código repetitivo) mediante anotaciones.
 */
@ConcurrentTest
class LombokShowcaseTest {

    @Nested
    @DisplayName("1. @Data & @Builder (Mutable Objects)")
    class MutableObjects {

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        static class User {
            private String id;
            private String name;
            private String email;
        }

        @Test
        @DisplayName("Uso de @Data (Getters, Setters, toString, equals, hashCode)")
        void dataAnnotation() {
            User user = new User();
            user.setName("John");
            user.setEmail("john@test.com");

            assertThat(user.getName()).isEqualTo("John");
            assertThat(user.toString()).contains("name=John");
        }

        @Test
        @DisplayName("Uso de @Builder para creación fluida")
        void builderAnnotation() {
            User user = User.builder()
                    .id("1")
                    .name("Jane")
                    .email("jane@test.com")
                    .build();

            assertThat(user.getName()).isEqualTo("Jane");
        }
    }

    @Nested
    @DisplayName("2. @Value (Immutable Objects)")
    class ImmutableObjects {

        @Value
        @Builder
        static class ImmutableUser {
            String id;
            String name;
        }

        @Test
        @DisplayName("Uso de @Value para objetos inmutables")
        void valueAnnotation() {
            ImmutableUser user = new ImmutableUser("1", "John");

            // user.setName("Jane"); // No compila: campos son final
            assertThat(user.getName()).isEqualTo("John");
        }
    }

    @Nested
    @DisplayName("3. @With (Wither methods)")
    class WitherMethod {

        @Value
        @With
        static class Price {
            double amount;
            String currency;
        }

        @Test
        @DisplayName("Uso de @With para crear copias con un campo modificado")
        void withAnnotation() {
            Price original = new Price(100.0, "EUR");
            Price modified = original.withAmount(150.0);

            assertThat(original.getAmount()).isEqualTo(100.0);
            assertThat(modified.getAmount()).isEqualTo(150.0);
            assertThat(modified.getCurrency()).isEqualTo("EUR");
        }
    }

    @Nested
    @DisplayName("4. @Slf4j & UtilityClass")
    class Utilities {

        @lombok.extern.slf4j.Slf4j
        static class LoggerExample {
            void logSomething() {
                log.info("Lombok automatically created this logger!");
            }
        }

        @Test
        @DisplayName("Verificación de que Slf4j funciona (vía reflexión para el test)")
        void loggerCheck() throws NoSuchFieldException {
            assertThat(LoggerExample.class.getDeclaredField("log")).isNotNull();
        }
    }
}
