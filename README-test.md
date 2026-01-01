# Showcase – Testing Ecosystem

Proyecto para demostrar el uso de diversas herramientas de testing y aseguramiento de calidad.

---

## 🧪 Herramientas de Testing Incluidas

| Herramienta        | Propósito               | Ubicación Config               |
|--------------------|-------------------------|--------------------------------|
| **Jacoco**         | Cobertura de código     | `pom.xml` → plugin             |
| **Pitest**         | Mutation testing        | `pom.xml` → plugin             |
| **ApprovalTests**  | Snapshot testing        | `ApprovalTestsExamples.java`   |
| **Faker**          | Datos fake para tests   | `pom.xml` → javafaker          |
| **JSON-Unit**      | Comparación JSON        | `JsonUnitExamplesTest.java`    |
| **jqwik**          | Property-based tests    | `JqwikExampleTest.java`        |
| **Testcontainers** | Contenedores para tests | `TestContainersIT.java`        |
| **Mockito**        | Mocks y Stubs           | `MockitoShowcaseTest.java`     |
| **WireMock**       | Mocking de APIs HTTP    | `WireMockShowcaseTest.java`    |
| **REST Assured**   | Testing de APIs HTTP    | `RestAssuredShowcaseTest.java` |
| **AssertJ**        | Asserts fluidos         | `AssertJ.java`                 |
| **JUnit 5**        | Framework de testing    | `JUnit5ShowcaseTest.java`      |
| **Cucumber**       | BDD (Gherkin)           | `CucumberShowcaseTest.java`    |
| **Selenium**       | E2E Testing (Browser)   | `SeleniumShowcaseTest.java`    |
| **Awaitility**     | Testing asíncrono       | `AwaitilityShowcaseTest.java`  |

---

## 📚 Guías resumidas

### 🔍 Jacoco (Cobertura de Código)
Genera reportes de cobertura de tests.
```bash
./mvnw test jacoco:report
```

### 🧬 Pitest (Mutation Testing)
Introduce mutaciones en el bytecode para verificar la robustez de los tests.
```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
```

### 🧪 jqwik (Property-Based Testing)
Valida invariantes de negocio generando cientos de entradas aleatorias automáticamente.
[JqwikExampleTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/JqwikTest.java)

### ✅ ApprovalTests (Snapshot Testing)
Compara output con archivos baseline aprobados (`.approved.txt`).
[ApprovalTestsExamples.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/ApprovalTestsExamples.java)

### 📊 JSON-Unit (Comparación JSON)
Assertions fluidas para validación de estructuras JSON.
[JsonUnitExamplesTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/JsonUnitTest.java)

### 🐳 Testcontainers (Infraestructura Real)
Levanta contenedores reales (PostgreSQL, etc.) para tests de integración.
[TestContainersIT.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/testcontainers/TestContainersIT.java)

### 🎭 Mockito (Mocks y Stubs)
Aislamiento de componentes. Incluye stubs dinámicos, secuenciales y mocking estático.
[MockitoShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/mockito/MockitoShowcaseTest.java)

### 🌐 WireMock (Mocking HTTP)
Simulación de servicios HTTP externos para tests sociales.
[WireMockShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/wiremock/WireMockShowcaseTest.java)

### 🧪 REST Assured (Testing API)
Sintaxis fluent `given/when/then` para validar endpoints.
[RestAssuredShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/rest/RestAssuredShowcaseTest.java)

### 💖 AssertJ (Assertions Fluidas)
Aserciones ricas para colecciones, fechas, Soft Assertions y más.
[AssertJ.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/AssertJ.java)

### 🃏 JUnit 5 (Framework Base)
Tests anidados, etiquetas y tests parametrizados avanzados (CSV, MethodSource, File-based).
[JUnit5ShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/junit5/JUnit5ShowcaseTest.java)

### 🥒 Cucumber (BDD)
Especificaciones ejecutables en Gherkin (inglés).
[transformer.feature](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/resources/testing/cucumber/transformer.feature)

### 🌐 Selenium (E2E)
Automatización de flujos de usuario en navegador (Headless).
[SeleniumShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/selenium/SeleniumShowcaseTest.java)

### ⏳ Awaitility (Testing Asíncrono)
Alternativa superior a `Thread.sleep()` que usa **polling** dinámico.
[AwaitilityShowcaseTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/awaitility/AwaitilityShowcaseTest.java)

#### ❓ ¿Por qué es mejor que `Thread.sleep()`?
1. **⏱️ Determinismo**: No espera un tiempo fijo. Si la condición se cumple en 10ms, el test sigue inmediatamente.
2. **🛡️ Robustez**: Permite configurar *timeouts* claros y re-evaluaciones automáticas (*polling interval*).
3. **📖 Semántica**: Describe **qué** esperas, no **cuánto** tiempo duerme el hilo.

---



## 📚 Guias detalladas

### 🔎 Jacoco (Cobertura de Código)
Genera reportes de cobertura de tests.

```bash
./mvnw test jacoco:report
# Abrir: target/site/jacoco/index.html
```

**Configuración (`pom.xml`):**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>
```

---

### 🧬 Pitest (Mutation Testing)

**Concepto**: Herramienta que transforma el código compilado (bytecode) insertando "mutaciones" (bugs artificiales) para verificar si los tests son capaces de detectarlos.
*   **Objetivo**: Obtener una métrica de coverage mucho más realista que la simple cobertura de líneas. Si un mutante sobrevive, significa que el test no es lo suficientemente robusto.
*   **Coste**: Hace que los tests sean mucho más lentos. Usar en situaciones críticas o para aprendizaje.

Introduce mutaciones en el código y verifica que los tests las detecten.

```bash
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage
# Reporte: target/pit-reports/
```

**Ejemplo de mutación:**
```java
// Original
if (balance >= amount) { ... }

// Mutación (cambia >= por >)
if (balance > amount) { ... }  // ¿El test lo detecta?
```

---

### 🧪 jqwik (Property-Based Testing)

Permite validar **invariantes** de negocio generando cientos de entradas aleatorias automáticamente. A diferencia de los tests tradicionales (basados en ejemplos), jqwik busca casos de borde que un humano podría olvidar.

**Uso Recomendado:** Para reglas de negocio complejas, validación de rangos, transformaciones de datos y algoritmos.

**Ejemplo (`JqwikExampleTest.java`):**
```java
@Property
void additionIsCommutative(@ForAll int a, @ForAll int b) {
    assertThat(a + b).isEqualTo(b + a);
}

@Property
void percentageShouldStayInValidRange(
    @ForAll @IntRange(min = 0, max = 100) int percentage
) {
    assertThat(percentage).isBetween(0, 100);
}
```

---

### ✅ ApprovalTests (Snapshot Testing)

Compara output con archivos baseline aprobados.

**Ejemplo (`ApprovalTestsExamples.java`):**
```java
@Test
void testStudentToString() {
    Student student = new Student("John", "john@test.com", Gender.MALE);
    Approvals.verify(student.toString());
}
```

**Archivos generados:**
- `.approved.txt` - Baseline aprobado
- `.received.txt` - Output actual (si difiere)

---

### 📊 JSON-Unit (Comparación JSON)

Assertions fluidas para JSON.

**Ejemplo (`JsonUnitExamplesTest.java`):**
```java
@Test
void shouldMatchJson() {
    String actual = """{"name": "John", "age": 25}""";
    
    assertThatJson(actual)
        .inPath("$.name").isEqualTo("John")
        .inPath("$.age").isNumber();
}

@Test
void shouldIgnoreFields() {
    assertThatJson(actual)
        .whenIgnoringPaths("$.id", "$.createdAt")
        .isEqualTo(expected);
}
```

---

### 🎭 Faker (Datos Fake)

Genera datos aleatorios para tests.

```java
Faker faker = new Faker();
String name = faker.name().fullName();        // "John Smith"
String email = faker.internet().emailAddress(); // "john@test.com"
String phone = faker.phoneNumber().phoneNumber();
```

---

### 🐳 Testcontainers (Contenedores para Tests)

Levanta infraestructura real (Bases de datos, Redis, etc.) en contenedores Docker para los tests de integración (Gestionado por **Maven Failsafe**).

**Configuración Avanzada:**
- **Reutilización (`.withReuse(true)`):** Permite mantener los contenedores vivos entre ejecuciones de tests, acelerando drásticamente el feedback.
    - Requiere el archivo `%USERPROFILE%/.testcontainers.properties` con `testcontainers.reuse.enable=true`.
- **Integración con Spring Boot:**
    - `@ServiceConnection`: Configura automáticamente las propiedades de conexión (JDBC URL, username, password) basándose en el contenedor.
    - **Uso en Desarrollo:** Puedes usar `@Container` + `@Bean` en una clase de configuración de test para levantar la infraestructura automáticamente al ejecutar la aplicación en modo `dev`.

**Ejemplo (`TestContainersIT.java`):**
```java
@Container
@ServiceConnection
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withReuse(true);
```

**Ventajas:**
- Entornos de test idénticos a producción.
- Sin necesidad de instalar bases de datos localmente.
- Soporte para `postgres:16-alpine` para imágenes ligeras.

---


---

### 🎭 Mockito (Mocks y Stubs)

Herramienta esencial para aislar el código bajo test mediante la creación de objetos simulados (mocks). Fundamental para tests unitarios sociales donde queremos controlar el comportamiento de los colaboradores.

**Uso Principal**: Dobles de test (Mocks, Stubs, Spies) y verificaciones de comportamiento.

```bash
./mvnw test -Dtest=MockitoShowcaseTest
```

#### 💡 Conceptos Clave de Mockito

1.  **Stubbing (`when...thenReturn`)**: Define qué debe devolver un método del mock cuando se llama con ciertos parámetros.
2.  **Dynamic Stubs (`thenAnswer`)**: Permite lógica dinámica basada en los argumentos recibidos. Muy útil para simular cálculos o comportamientos complejos.
3.  **Sequential Stubbing**: Permite definir diferentes respuestas para llamadas sucesivas al mismo método (útil para simular **retries** o cambios de estado).
4.  **Verification (`verify`)**: Comprueba que un método ha sido llamado con los parámetros esperados, cuántas veces (`times`, `never`), o incluso el orden (`InOrder`).
5.  **Argument Matchers (`any()`, `eq()`, `argThat()`)**: Permiten hacer stubs o verificaciones flexibles sin necesidad de conocer el valor exacto del parámetro.
6.  **Argument Captor (`ArgumentCaptor`)**: Captura los argumentos pasados a un mock para realizar aserciones detalladas sobre ellos a posteriori.
7.  **Spies (`spy`)**: Mocks parciales que envuelven un objeto real. Ejecutan el código real a menos que se haga un stub específico.
8.  **BDDMockito (`given...willReturn`)**: Estilo sintáctico alineado con BDD (Behavior Driven Development) para mejorar la legibilidad.
9.  **Static Mocking (`mockStatic`)**: Permite mockear métodos estáticos. **Es obligatorio** usar `try-with-resources` para asegurar que el mock se cierra y no afecta a otros tests.

**Ejemplo de Verificación y Captura:**
```java
// Capturar un argumento para inspeccionarlo
verify(service).performAction(stringCaptor.capture());
assertThat(stringCaptor.getValue()).contains("Success");

// Stubbing secuencial (Simular fallo y luego éxito)
when(service.call())
    .thenThrow(new RuntimeException())
    .thenReturn("Success!");
```

---

```

---

### 🌐 WireMock (Mocking de APIs HTTP)

Permite simular servicios HTTP externos (APIs de terceros, microservicios) para realizar tests de integración o tests unitarios sociales sin depender de la red o de la disponibilidad de esos servicios.

**Uso Principal**: Mockear dependencias HTTP externas.

```bash
./mvnw test -Dtest=WireMockShowcaseTest
```

#### 💡 Conceptos Clave de WireMock

1.  **Stubbing (`stubFor`)**: Define qué responder ante una petición HTTP específica.
2.  **Request Matching**: Filtrado potente de peticiones por URL, headers, cookies y cuerpo (JSON, XML, regex).
3.  **Fault Injection**: Simula fallos de red (conexiones cerradas, respuestas lentas, datos corruptos) para probar la resiliencia de la APP.
4.  **Response Templating**: Genera respuestas basadas en los datos de la petición (ej. devolver el mismo ID enviado).

---

### 🧪 REST Assured (Testing de APIs HTTP)

Biblioteca para testar APIs REST de forma fluida y legible, inspirada en BDD (Given/When/Then). Se integra perfectamente con Hamcrest para validaciones potentes.

**Uso Principal**: Validar endpoints de nuestra APP o de mocks (como WireMock).

```bash
./mvnw test -Dtest=RestAssuredShowcaseTest
```

#### 💡 Conceptos Clave de REST Assured

1.  **Sintaxis Gherkin (`given().when().then()`)**: Estructura de test muy legible y semántica.
2.  **Validación de JSON Path**: Permite navegar por estructuras JSON complejas y aplicar matchers.
3.  **Configuración de Base Path/Port**: Facilita el testeo contra diferentes entornos o servidores dinámicos (como Testcontainers o WireMock).

**Ejemplo Combinado (WireMock + REST Assured):**
```java
// Mockear endpoint con WireMock
stubFor(get("/api/user/1").willReturn(okJson("{\"name\":\"Antigravity\"}")));

// Testear con REST Assured
given()
    .port(wiremockPort)
.when()
    .get("/api/user/1")
.then()
    .statusCode(200)
    .body("name", is("Antigravity"));
```

---

---

### 💖 AssertJ (Assertions Fluidas)

Biblioteca de aserciones que permite escribir tests mucho más legibles y fáciles de mantener gracias a su API fluida. Supera ampliamente a las aserciones básicas de JUnit.

**Uso Principal**: Aserciones ricas y legibles en cualquier tipo de test.

```bash
./mvnw test -Dtest=AssertJ
```

#### 💡 Conceptos Clave de AssertJ

1.  **Colecciones**: Aserciones potentes sobre contenido, orden y tamaño sin necesidad de bucles.
    ```java
    assertThat(list).hasSize(3).contains("A", "B").doesNotContain("C");
    assertThat(list).containsExactlyInAnyOrder("B", "A", "C");
    ```
2.  **Mapping y Extracting**: Permite navegar por los atributos de una colección de objetos de forma sencilla.
    ```java
    assertThat(fellowship)
        .extracting(Character::getName, Character::getAge)
        .contains(tuple("Frodo", 33), tuple("Gandalf", 2000));
    ```
3.  **Fechas y Tiempo**: Aserciones amigables para tipos de `java.time`.
    ```java
    assertThat(lastLogin).isCloseTo(now(), byLessThan(1, SECONDS));
    ```
4.  **Soft Assertions**: Permiten ejecutar múltiples aserciones y ver todos los fallos al final, en lugar de detenerse en el primero.
    ```java
    try (var softly = new AutoCloseableSoftAssertions()) {
        softly.assertThat(user.getName()).isEqualTo("John");
        softly.assertThat(user.getAge()).isEqualTo(25);
    } // Lanza todos los fallos aquí
    ```
5.  **Custom Assertions**: Posibilidad de crear tus propias clases de aserción para tu dominio (ej. `VillaAssert`) para ganar expresividad.

---

---

### 🃏 JUnit 5 (Framework de Testing)

El estándar para testing en Java. Proporciona anotaciones para el ciclo de vida, agrupamiento de tests y un potente motor para tests parametrizados.

**Uso Principal**: Orquestación y estructura de los tests.

#### 💡 Conceptos Clave de JUnit 5

1.  **Ciclo de Vida**: `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`.
2.  **Organización**: `@Nested` para jerarquías, `@Tag` para filtrado, `@DisplayName` para legibilidad.
3.  **Tests Parametrizados (`@ParameterizedTest`)**:
    - **Básicos**: `@ValueSource`, `@CsvSource`, `@EnumSource`.
    - **Objetos**: `@MethodSource` para inyectar POJOs complejos.
    - **Ficheros**: Carga dinámica mediante escaneo de recursos (ej. `.in.json` -> `.out.json`).

**Ejemplo de Test Parametrizado desde Fichero:**
```java
@ParameterizedTest(name = "{0}")
@MethodSource("testData")
void fileTest(FileTestCase testCase) {
    var actual = service.execute(testCase.getInput());
    assertThatJson(actual).isEqualTo(testCase.getExpectedOutput());
}
```

---