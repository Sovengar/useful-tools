# Spring Boot Testing Demo

Proyecto para probar herramientas utiles.

---

## 🛠️ Herramientas Incluidas

| Herramienta       | Propósito               | Ubicación Config                     |
|-------------------|-------------------------|--------------------------------------|
| **Jacoco**        | Cobertura de código     | `pom.xml` → plugin                   |
| **Pitest**        | Mutation testing        | `pom.xml` → plugin                   |
| **Flyway**        | Migraciones de BD       | `FlywayConfig.java`                 |
| **ArchUnit**      | Tests de arquitectura   | `ArchitectureTest.java`              |
| **P6Spy**         | Logging SQL             | `src/main/resources/spy.properties`  |
| **QueryDSL**      | Queries type-safe       | `StudentQueryRepository.java`        |
| **OpenRewrite**   | Refactoring automático  | `rewrite.yml`                        |
| **Error Prone**   | Análisis estático       | `pom.xml` → compiler plugin          |
| **ApprovalTests** | Snapshot testing        | `ApprovalTestsExamples.java`         |
| **Faker**         | Datos fake para tests   | `pom.xml` → javafaker                |
| **JSON-Unit**     | Comparación JSON        | `JsonUnitExamplesTest.java`          |
| **Testcontainers**| Contenedores para tests | `TestContainersIT.java`              |

---

## 📦 Comandos Principales

```bash
# ═══════════════════════════════════════════════════════════════════
# COMPILAR (genera Q-classes de QueryDSL)
# ═══════════════════════════════════════════════════════════════════
./mvnw clean compile

# ═══════════════════════════════════════════════════════════════════
# TESTS + COBERTURA JACOCO
# Reporte: target/site/jacoco/index.html
# ═══════════════════════════════════════════════════════════════════
./mvnw test jacoco:report

# ═══════════════════════════════════════════════════════════════════
# MUTATION TESTING (PITEST)
# Reporte: target/pit-reports/
# ═══════════════════════════════════════════════════════════════════
./mvnw test-compile org.pitest:pitest-maven:mutationCoverage

# ═══════════════════════════════════════════════════════════════════
# OPENREWRITE
# ═══════════════════════════════════════════════════════════════════
./mvnw rewrite:dryRun    # Ver cambios sin aplicar
./mvnw rewrite:run       # Aplicar refactoring
./mvnw rewrite:discover  # Listar recetas disponibles

# ═══════════════════════════════════════════════════════════════════
# EJECUTAR TESTS ESPECÍFICOS
# ═══════════════════════════════════════════════════════════════════
./mvnw test -Dtest=ArchitectureTest        # ArchUnit
./mvnw test -Dtest=ApprovalTestsExamples   # ApprovalTests
./mvnw test -Dtest=JsonUnitExamplesTest    # JSON-Unit

# ═══════════════════════════════════════════════════════════════════
# INTEGRATION TESTS (FAILSAFE - mvn verify)
# ═══════════════════════════════════════════════════════════════════
./mvnw verify -Dit.test=QueryDslIT        # QueryDSL Integration
./mvnw verify -Dit.test=TestContainersIT   # TestContainers examples
```

---

## 📚 Guía de Uso por Herramienta

### 🔍 Jacoco (Cobertura de Código)

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

### 🗄️ Flyway (Migraciones de BD)

Versiona el esquema de base de datos de forma programática.

**Configuración (`FlywayConfig.java`):**
- Gestiona localizaciones dinámicamente (`db/migrations` y `db/dev` en perfil `dev`).
- Ejecuta `migrate()` automáticamente solo en perfil `dev`.
- Permite comportamiento flexible para H2/Tests mediante `flyway.h2-behavior`.

**Ubicación:** `src/main/resources/db/migrations/`

**Convención de nombres:**
- `V1__create_tables.sql` (inicial)
- `R__Load_data.sql` (datos repetibles)

```sql
-- V1__create_tables.sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);
```

---

### 🏛️ ArchUnit (Tests de Arquitectura)

Valida reglas de arquitectura en tiempo de test.

**Ejemplo (`ArchitectureTest.java`):**
```java
@Test
void controllersShouldNotAccessRepositoriesDirectly() {
    noClasses()
        .that().haveSimpleNameEndingWith("Controller")
        .should().dependOnClassesThat()
        .haveSimpleNameEndingWith("Repository")
        .check(importedClasses);
}
```

---

### 🔎 P6Spy (SQL Logging)

Intercepta y loguea todas las queries SQL. Habilitado por defecto en los perfiles `dev` y `test`.

**Configuración (`spy.properties`):**
```properties
appender=com.p6spy.engine.spy.appender.Slf4JLogger
customLogMessageFormat=P6SPY | %(executionTime)ms | %(category) | %(effectiveSql)
```

**Output:**
```
P6SPY | 5ms | statement | SELECT * FROM student WHERE email = 'john@test.com'
```

---

### 🔎 QueryDSL (Queries Type-Safe)

Queries compiladas en tiempo de compilación.

**Ejemplo (`StudentQueryRepository.java`):**
```java
public List<Student> searchStudents(String name, Gender gender) {
    QStudent student = QStudent.student;
    
    BooleanExpression predicate = student.isNotNull();
    if (name != null) {
        predicate = predicate.and(student.name.containsIgnoreCase(name));
    }
    if (gender != null) {
        predicate = predicate.and(student.gender.eq(gender));
    }
    
    return queryFactory.selectFrom(student).where(predicate).fetch();
}
```

---

### 🔄 OpenRewrite (Refactoring Automático)

Aplica transformaciones de código automáticamente.

**Configuración (`rewrite.yml`):**
```yaml
type: specs.openrewrite.org/v1beta/recipe
name: com.example.CustomRecipes
recipeList:
  - org.openrewrite.java.format.AutoFormat
```

```bash
./mvnw rewrite:dryRun  # Preview
./mvnw rewrite:run     # Apply
```

---

### ⚠️ Error Prone (Análisis Estático)

Detecta errores comunes en tiempo de compilación.

**Detecta:**
- Null pointer dereferences
- Comparaciones incorrectas
- Uso incorrecto de APIs

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

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/testing/
│   │   ├── studentModel/
│   │   │   ├── Student.java
│   │   │   └── StudentRepository.java
│   │   └── ...
│   └── resources/
│       ├── application.properties
│       ├── spy.properties              ← P6Spy config
│       └── db/migration/
│           └── V1__create_tables.sql   ← Flyway
├── test/
│   └── java/testing/
│       ├── approvaltests/
│       │   └── ApprovalTestsExamples.java ← ApprovalTests
│       ├── architecture/
│       │   └── ArchitectureTest.java      ← ArchUnit
│       ├── jsonunit/
│       │   └── JsonUnitExamplesTest.java  ← JSON-Unit
│       ├── querydsl/
│       │   └── QueryDslIT.java             ← QueryDSL (Failsafe)
│       ├── testcontainers/
│       │   └── TestContainersIT.java       ← Testcontainers (Failsafe)
│       └── studentModel/
│           └── ... (Tests de dominio)
└── rewrite.yml                          ← OpenRewrite config
```

---

## 🚀 Requisitos

- **Java 21**
- **Docker** (para TestContainers)
- **Maven 3.9+**