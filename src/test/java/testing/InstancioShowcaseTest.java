package testing;

import lombok.With;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import testing._utils.ConcurrentTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

/**
 * Instancio Showcase Test
 *
 * Esta clase muestra las funcionalidades más importantes de Instancio.
 * Instancio automatiza la creación de POJOs complejos con datos aleatorios.
 */
@ExtendWith(InstancioExtension.class)
@ConcurrentTest
class InstancioShowcaseTest {

    // Domain models para los ejemplos
    record User(UUID id, String name, String email, LocalDate birthDate, Address address, List<String> roles) {
    }

    record Address(String street, String city, String country, String zipCode) {
    }

    @Nested
    @DisplayName("1. Basic Creation")
    class BasicCreation {

        @Test
        @DisplayName("Creación básica de un objeto con datos aleatorios")
        void createBasicObject() {
            User user = Instancio.create(User.class);

            assertThat(user).isNotNull();
            assertThat(user.id()).isNotNull();
            assertThat(user.name()).isNotEmpty();
            assertThat(user.address()).isNotNull();
            assertThat(user.roles()).isNotEmpty();
        }

        @Test
        @DisplayName("Creación de una lista de objetos")
        void createList() {
            List<User> users = Instancio.ofList(User.class).size(5).create();

            assertThat(users).hasSize(5).allSatisfy(user -> {
                assertThat(user.name()).isNotNull();
                assertThat(user.address()).isNotNull();
            });
        }

        @Test
        @DisplayName("Creación de tipos genéricos usando TypeToken")
        void createGenericType() {
            List<String> strings = Instancio.create(new TypeToken<List<String>>() {
            });
            assertThat(strings).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("2. Customizing Fields")
    class CustomizingFields {

        @Test
        @DisplayName("Personalizar campos específicos usando selectores")
        void customizeFields() {
            User user = Instancio.of(User.class)
                    .set(field(User::name), "Fixed Name")
                    .set(field(Address::country), "Spain")
                    .generate(field(User::email), gen -> gen.net().email())
                    .create();

            assertThat(user.name()).isEqualTo("Fixed Name");
            assertThat(user.address().country()).isEqualTo("Spain");
            assertThat(user.email()).contains("@");
        }

        @Test
        @DisplayName("Ignorar campos")
        void ignoreFields() {
            User user = Instancio.of(User.class)
                    .ignore(all(UUID.class)) // Ignora todos los UUIDs
                    .ignore(field(User::roles))
                    .create();

            assertThat(user.id()).isNull();
            assertThat(user.roles()).isNull();
        }

        @Test
        @DisplayName("Reglas condicionales y supply")
        void supplyAndConditionals() {
            User user = Instancio.of(User.class)
                    .supply(all(LocalDate.class), () -> LocalDate.now().minusYears(20))
                    .create();

            assertThat(user.birthDate()).isEqualTo(LocalDate.now().minusYears(20));
        }
    }

    @Nested
    @DisplayName("3. Models and Reusability")
    class Models {

        // Los modelos permiten definir plantillas reutilizables
        private static final Model<User> ADMIN_MODEL = Instancio.of(User.class)
                .set(field(User::roles), List.of("ADMIN", "USER"))
                .toModel();

        @Test
        @DisplayName("Uso de modelos para crear objetos pre-configurados")
        void useModel() {
            User admin = Instancio.create(ADMIN_MODEL);

            assertThat(admin.roles()).containsExactly("ADMIN", "USER");
            assertThat(admin.name()).isNotNull(); // El resto de campos sigue siendo aleatorio
        }
    }

    @Nested
    @DisplayName("4. Settings and Bean Validation")
    class SettingsExample {

        // Podemos inyectar settings globales o específicos para el test
        @WithSettings
        private final Settings settings = Settings.create()
                .set(Keys.COLLECTION_MIN_SIZE, 10)
                .set(Keys.COLLECTION_MAX_SIZE, 10);

        @Test
        @DisplayName("Uso de Settings para controlar el comportamiento global de la generación")
        void useSettings() {
            User user = Instancio.create(User.class);
            assertThat(user.roles()).hasSize(10);
        }

        @Test
        @DisplayName("Generar strings con formatos específicos")
        void specificFormats() {
            String hex = Instancio.gen().string().hex().length(10).get();
            assertThat(hex).matches("^[0-9A-F]{10}$");
        }
    }

    @Nested
    @DisplayName("5. Advanced: Blank and Nil")
    class Advanced {

        @Test
        @DisplayName("Forzar valores nulos o vacíos")
        void nilAndEmpty() {
            // Útil para testear validaciones o casos de borde
            User user = Instancio.of(User.class)
                    .set(all(String.class), null)
                    .create();

            assertThat(user.name()).isNull();
            assertThat(user.address().city()).isNull();
        }
    }

    @Nested
    @DisplayName("6. Fusion: Instancio + Lombok @With")
    class FusionExample {

        // Los records de Java son geniales, pero a veces queremos cambiar solo un campo
        // de un objeto ya generado. Lombok @With lo soluciona.
        @With
        record Product(UUID id, String name, double price, String category) {
        }

        @Test
        @DisplayName("Generar con Instancio y modificar con Lombok @With")
        void instancioWithLombokWither() {
            // 1. Generamos un producto totalmente aleatorio
            Product randomProduct = Instancio.create(Product.class);

            // 2. Creamos una copia exacta pero con el precio modificado usando @With
            Product promoProduct = randomProduct.withPrice(0.99);

            assertThat(promoProduct.id()).isEqualTo(randomProduct.id());
            assertThat(promoProduct.name()).isEqualTo(randomProduct.name());
            assertThat(promoProduct.price()).isEqualTo(0.99);
        }
    }
}
