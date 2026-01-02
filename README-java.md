# Showcase – Java & Infrastructure

Herramientas para el desarrollo, infraestructura y refactorización automática.

## 🛠️ Herramientas de Desarrollo

| Herramienta     | Propósito              | Ubicación Config                    |
|-----------------|------------------------|-------------------------------------|
| **Flyway**      | Migraciones de BD      | `FlywayConfig.java`                 |
| **ArchUnit**    | Tests de arquitectura  | `ArchUnitTest.java`                 |
| **P6Spy**       | Logging SQL            | `src/main/resources/spy.properties` |
| **QueryDSL**    | Queries type-safe      | `StudentQueryRepository.java`       |
| **OpenRewrite** | Refactoring automático | `rewrite.yml`                       |
| **Error Prone** | Análisis estático      | `pom.xml` → compiler plugin         |
| **Lombok**      | Reducción Boilerplate  | `LombokShowcaseTest.java`           |
| **Swagger**     | Documentación de API   | `SwaggerShowcaseController.java`    |

---

## 📚 Guías Detalladas

### 🗄️ Flyway (Migraciones de BD)

Versiona el esquema de base de datos de forma programática.

**Configuración (`FlywayConfig.java`):**
- Gestiona localizaciones dinámicamente (`db/migrations` y `db/dev` en perfil `dev`).
- Ejecuta `migrate()` automáticamente solo en perfil `dev`).
- Permite comportamiento flexible para H2/Tests mediante `flyway.h2-behavior`.

**Ubicación:** `src/main/resources/db/migrations/`

#### 💡 Conceptos Clave de Flyway

1. **`flyway_schema_history`**: Es la tabla que Flyway crea automáticamente en tu base de datos para llevar el control. Guarda el checksum de cada script, quién lo ejecutó y cuándo. Si intentas modificar un script `V` que ya ha sido aplicado, Flyway lanzará un error de validación.
2. **Convención de Nombres**: Los archivos deben seguir el patrón `<Prefijo><Versión>__<Descripción>.sql`.
   - **IMPORTANTE**: Se usan **dos guiones bajos (`__`)** para separar la versión de la descripción. Sin ellos, Flyway no reconocerá el archivo.
3. **Estrategias de Versión (`V`)**:
   - **Secuencial**: `V1__init.sql`, `V2__add_col.sql`. Ideal para proyectos pequeños o con un solo equipo.
   - **Timestamp**: `V2024_12_28_2300__add_index.sql`. Muy recomendado en entornos con múltiples desarrolladores para evitar conflictos de números de versión al fusionar ramas.
4. **Migraciones Repetibles (`R`)**:
   - No tienen versión fija. Se ejecutan **siempre que su contenido cambie** (el checksum sea distinto).
   - Ejemplo: `R__Load_data.sql`. Ideal para cargar vistas, procedimientos almacenados o datos maestros que necesitas actualizar frecuentemente.
5. **Propiedad Custom `flyway.h2-behavior`**:
   - Es una propiedad casera definida en `FlywayConfig.java`.
   - Si se establece en `true`, Flyway ejecutará un `clean()` al arrancar la aplicación.
   - **Propósito**: Imitar el comportamiento de una base de datos H2 (en memoria), donde cada vez que arrancas la aplicación, la base de datos está vacía y se reconstruye desde cero. Muy útil para desarrollo rápido si quieres garantizar un estado limpio sin recrear contenedores.

**Ejemplo de estructura:**
```sql
-- V1__create_tables.sql (Versionada)
CREATE TABLE student (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

-- R__Load_data.sql (Repetible)
INSERT INTO student (id, name, email) VALUES (1, 'John', 'john@test.com')
ON CONFLICT (id) DO NOTHING;
```

---

### 🐳 Docker Compose Integration (Spring Boot)

Spring Boot gestiona automáticamente la infraestructura local necesaria para el desarrollo.

**Funcionamiento:**
- Al arrancar la aplicación en local, Spring Boot detecta el archivo `compose.yaml` (o `compose.yml`).
- Levanta automáticamente los servicios definidos (ej. PostgreSQL) utilizando Docker Compose.
- Inyecta dinámicamente las propiedades de conexión (JDBC URL, usuario, password) en el contexto de Spring, eliminando la necesidad de configurarlas manualmente en `application.properties`.

**Archivo de Configuración:** `compose.yaml`

---

### 🏛️ ArchUnit (Tests de Arquitectura)

Valida reglas de arquitectura (ej. controladores no acceden a repositorios directamente) en tiempo de test.
[ArchUnitTest.java](file:///c:/Users/buble/OneDrive/DEV/Projects/Infra/useful-tools/src/test/java/testing/ArchUnitTest.java)

**Ejemplo (`ArchUnitTest.java`):**
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
# MultiLineFormat permite ver la query original y la query con valores
logMessageFormat=com.p6spy.engine.spy.appender.MultiLineFormat
```

**Output:**
```
P6SPY | took 1ms | statement | connection 10
insert into student (email,gender,name,id) values (?,?,?,?)
insert into student (email,gender,name,id) values ('john@test.com','MALE','John',10);
```

---

### 🔎 QueryDSL (Queries Type-Safe)

Queries compiladas en tiempo de compilación.

**Ejemplo (`StudentQueryRepository.java`):**
```java
public List<Student> searchStudents(String name, Gender gender) {
    QStudent student = QStudent.student;
    return queryFactory.selectFrom(student)
        .where(student.name.containsIgnoreCase(name))
        .fetch();
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

Detecta errores comunes (Null pointers, comparaciones incorrectas) en tiempo de compilación.

**Detecta:**
- Null pointer dereferences
- Comparaciones incorrectas
- Uso incorrecto de APIs

---

### 🌶️ Lombok (Reducción de Boilerplate)

**Concepto**: Biblioteca que se integra con el compilador para generar automáticamente código repetitivo (Getters, Setters, Builders, etc.) mediante anotaciones.

**Uso Recomendado**: Siempre que se usen clases POJO/DTOs tradicionales o para añadir funcionalidades extra a Records (como `@With`).

**Ejemplo (`LombokShowcaseTest.java`):**
```java
@Data @Builder
class User {
    private String name;
}

// Builder fluido
User user = User.builder().name("John").build();

// Inmutabilidad (Wither Pattern)
@Value @With
class Price {
    double amount;
}
Price price = new Price(10.0).withAmount(20.0);
```

**Ventajas clave:**
- **Cleaner code**: Enfócate en la importancia del dominio, no en el ruido técnico.
- **Consistency**: El código generado siempre sigue el estándar definido.
- **Wither pattern**: Facilita enormemente trabajar con inmutabilidad.
- **Logging**: `@Slf4j` inyecta automáticamente un logger privado y estático.

---

### 📝 Swagger / OpenAPI (Documentación de API)

**Concepto**: Generación automática de documentación interactiva para APIs REST. Permite visualizar y probar los endpoints directamente desde el navegador.

**URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

**Generación Estática**:
Para generar el fichero `openapi.json` de forma estática (útil para CI/CD), ejecuta:
```bash
./mvnw verify -DskipTests # Genera target/openapi.json
```

**Uso Recomendado**: Siempre que se expongan servicios REST para facilitar la integración con el frontend u otros equipos.

**Ejemplo (`SwaggerShowcaseController.java`):**
```java
@Tag(name = "Showcase", description = "Endpoints de ejemplo")
@RestController
public class ShowcaseController {

    @Operation(summary = "Obtener item", description = "Retorna un item por su ID")
    @GetMapping("/{id}")
    public Item getById(@PathVariable UUID id) { ... }
}
```

**Ventajas clave:**
- **Auto-generado**: La documentación siempre está sincronizada con el código.
- **Interactivo**: Interfaz amigable para realizar pruebas sin herramientas externas.
- **Estandarizado**: Basado en la especificación OpenAPI 3.0.
- **Schemas**: Documentación detallada de los modelos de datos (DTOs).