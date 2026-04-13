-- U1__drop_tables.sql
-- Undo migration for V1: drops all tables in reverse dependency order

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;
