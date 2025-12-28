-- ═══════════════════════════════════════════════════════════════════════════════
-- FLYWAY MIGRATION: V1__create_tables.sql
-- ═══════════════════════════════════════════════════════════════════════════════
-- Flyway is a database migration tool that versions your database schema.
--
-- Dependencies: org.flywaydb:flyway-core, org.flywaydb:flyway-database-postgresql (pom.xml)
-- Location: src/main/resources/db/migration/
--
-- Naming convention: V{version}__{description}.sql
--   - V1__create_tables.sql (initial)
--   - V2__add_column_x.sql (next migration)
--
-- Commands:
--   Spring Boot auto-runs migrations on startup when flyway is on classpath
--   Manual: mvn flyway:migrate / flyway:info / flyway:clean
-- ═══════════════════════════════════════════════════════════════════════════════

-- ─────────────────────────────────────────────────────────────────────────────────
-- STUDENT TABLE
-- Entity: testing.studentModel.Student
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE SEQUENCE IF NOT EXISTS student_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY DEFAULT nextval('student_sequence'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    gender VARCHAR(50) NOT NULL
);

-- ─────────────────────────────────────────────────────────────────────────────────
-- POST TABLE
-- Entity: testing.postModel.Post
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE SEQUENCE IF NOT EXISTS post_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS post (
    id BIGINT PRIMARY KEY DEFAULT nextval('post_sequence'),
    user_id INTEGER,
    title VARCHAR(500),
    body TEXT
);

-- ─────────────────────────────────────────────────────────────────────────────────
-- STUDENT2 TABLE
-- Entity: testing.student2Model.Student2
-- ─────────────────────────────────────────────────────────────────────────────────
CREATE SEQUENCE IF NOT EXISTS student2_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS student2 (
    id BIGINT PRIMARY KEY DEFAULT nextval('student2_sequence'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
