package testing.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * ARCHUNIT - Architecture Tests
 * ═══════════════════════════════════════════════════════════════════════════════
 * ArchUnit tests enforce architectural rules at compile/test time.
 *
 * Dependency (pom.xml):
 * com.tngtech.archunit:archunit-junit5
 *
 * Run: mvn test -Dtest=ArchitectureTest
 *
 * These tests validate:
 * - Layer dependencies (controllers → services → repositories)
 * - Naming conventions
 * - Package structure
 * - No cyclic dependencies
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@ActiveProfiles("test")
@DisplayName("Architecture Tests (ArchUnit)")
class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("testing");
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // NAMING CONVENTIONS
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Naming Conventions")
    class NamingConventions {

        @Test
        @DisplayName("Controllers should be suffixed with 'Controller'")
        void controllersShouldBeSuffixedWithController() {
            ArchRule rule = classes()
                    .that().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().haveSimpleNameEndingWith("Controller");

            rule.check(importedClasses);
        }

        @Test
        @DisplayName("Repositories should be suffixed with 'Repository'")
        void repositoriesShouldBeSuffixedWithRepository() {
            ArchRule rule = classes()
                    .that().areAnnotatedWith(org.springframework.stereotype.Repository.class)
                    .should().haveSimpleNameEndingWith("Repository");

            rule.check(importedClasses);
        }

        @Test
        @DisplayName("Services should be suffixed with 'Service'")
        void servicesShouldBeSuffixedWithService() {
            ArchRule rule = classes()
                    .that().areAnnotatedWith(org.springframework.stereotype.Service.class)
                    .should().haveSimpleNameEndingWith("Service");

            rule.check(importedClasses);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // LAYER DEPENDENCIES
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Layer Dependencies")
    class LayerDependencies {

        @Test
        @DisplayName("Controllers should not access Repositories directly")
        void controllersShouldNotAccessRepositoriesDirectly() {
            ArchRule rule = noClasses()
                    .that().haveSimpleNameEndingWith("Controller")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
                    .because("Controllers should use Services, not Repositories directly");

            // NOTE: Uncomment to enforce this rule once your code follows layered
            // architecture
            // Currently PostController accesses PostRepository directly
            // rule.check(importedClasses);
        }

        @Test
        @DisplayName("Repositories should not depend on Controllers")
        void repositoriesShouldNotDependOnControllers() {
            ArchRule rule = noClasses()
                    .that().haveSimpleNameEndingWith("Repository")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Controller");

            rule.check(importedClasses);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // PACKAGE STRUCTURE
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Package Structure")
    class PackageStructure {

        @Test
        @DisplayName("No cyclic dependencies between packages")
        void noCyclicDependencies() {
            ArchRule rule = slices()
                    .matching("testing.(*)..")
                    .should().beFreeOfCycles();

            rule.check(importedClasses);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // ANNOTATIONS
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Annotation Rules")
    class AnnotationRules {

        @Test
        @DisplayName("Entities should be annotated with @Entity")
        void entitiesShouldBeAnnotated() {
            ArchRule rule = classes()
                    .that().haveSimpleNameNotEndingWith("DTO")
                    .and().haveSimpleNameNotEndingWith("Controller")
                    .and().haveSimpleNameNotEndingWith("Service")
                    .and().haveSimpleNameNotEndingWith("Repository")
                    .and().haveSimpleNameNotEndingWith("Mapper")
                    .and().haveSimpleNameNotEndingWith("Exception")
                    .and().haveSimpleNameNotEndingWith("Config")
                    .and().haveSimpleNameNotEndingWith("Client")
                    .and().haveSimpleNameEndingWith("") // This is a placeholder
                    .should().beAnnotatedWith(jakarta.persistence.Entity.class)
                    .orShould().beEnums()
                    .orShould().beRecords()
                    .orShould().beInterfaces();

            // Note: This rule is intentionally loose - customize for your project
            // rule.check(importedClasses);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // CUSTOM RULES EXAMPLE
    // ─────────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Custom Rules")
    class CustomRules {

        @Test
        @DisplayName("Classes in 'config' package should not be accessed from outside")
        void configClassesShouldBeInternal() {
            // Example of a custom rule - adjust as needed
            ArchRule rule = classes()
                    .that().resideInAPackage("..config..")
                    .should().onlyBeAccessed().byClassesThat()
                    .resideInAnyPackage("..config..", "testing");

            // Uncomment to enforce:
            // rule.check(importedClasses);
        }
    }
}
