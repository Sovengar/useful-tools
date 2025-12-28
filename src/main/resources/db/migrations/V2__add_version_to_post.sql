-- ═══════════════════════════════════════════════════════════════════════════════
-- FLYWAY MIGRATION: V2__add_version_to_post.sql
-- ═══════════════════════════════════════════════════════════════════════════════

ALTER TABLE post ADD COLUMN version INTEGER DEFAULT 0;
