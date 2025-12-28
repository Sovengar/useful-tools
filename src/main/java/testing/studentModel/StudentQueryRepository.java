package testing.studentModel;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * QUERYDSL REPOSITORY EXAMPLE
 * ═══════════════════════════════════════════════════════════════════════════════
 * QueryDSL provides type-safe queries using generated Q-classes.
 *
 * Dependencies (pom.xml):
 * - com.querydsl:querydsl-jpa (runtime)
 * - com.querydsl:querydsl-apt (annotation processor)
 *
 * Q-classes are generated in: target/generated-sources/annotations/
 * Run: mvn compile (to generate Q-classes)
 *
 * Usage:
 * QStudent.student.name.eq("John") -> type-safe query
 * vs JPQL: "SELECT s FROM Student s WHERE s.name = :name"
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Repository
public class StudentQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QStudent student = QStudent.student;

    public StudentQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // BASIC QUERYDSL EXAMPLES
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Find all students ordered by name
     */
    public List<Student> findAllOrderedByName() {
        return queryFactory
                .selectFrom(student)
                .orderBy(student.name.asc())
                .fetch();
    }

    /**
     * Find student by email (exact match)
     */
    public Optional<Student> findByEmail(String email) {
        Student result = queryFactory
                .selectFrom(student)
                .where(student.email.eq(email))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * Find students by gender
     */
    public List<Student> findByGender(Gender gender) {
        return queryFactory
                .selectFrom(student)
                .where(student.gender.eq(gender))
                .fetch();
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // DYNAMIC QUERIES WITH PREDICATES
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Search students with optional filters (dynamic query)
     * 
     * @param nameContains Filter by name containing (case insensitive)
     * @param gender       Filter by gender
     * @param emailDomain  Filter by email domain (e.g., "@gmail.com")
     */
    public List<Student> searchStudents(String nameContains, Gender gender, String emailDomain) {
        BooleanExpression predicate = student.isNotNull();

        if (nameContains != null && !nameContains.isBlank()) {
            predicate = predicate.and(student.name.containsIgnoreCase(nameContains));
        }

        if (gender != null) {
            predicate = predicate.and(student.gender.eq(gender));
        }

        if (emailDomain != null && !emailDomain.isBlank()) {
            predicate = predicate.and(student.email.endsWith(emailDomain));
        }

        return queryFactory
                .selectFrom(student)
                .where(predicate)
                .orderBy(student.name.asc())
                .fetch();
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // PAGINATION EXAMPLE
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Find students with pagination
     */
    public List<Student> findWithPagination(int page, int size) {
        return queryFactory
                .selectFrom(student)
                .orderBy(student.id.asc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    /**
     * Count total students
     */
    public long count() {
        return queryFactory
                .selectFrom(student)
                .fetchCount();
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // PROJECTION EXAMPLE
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Get only names of all students
     */
    public List<String> findAllNames() {
        return queryFactory
                .select(student.name)
                .from(student)
                .orderBy(student.name.asc())
                .fetch();
    }
}
