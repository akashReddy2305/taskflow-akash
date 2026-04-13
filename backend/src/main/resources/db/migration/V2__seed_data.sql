-- V2__seed_data.sql
-- Seeds test data: 1 user, 1 project, 3 tasks with different statuses
-- Test credentials:  test@example.com / password123

-- User: password is "password123" hashed with bcrypt cost 12
INSERT INTO users (id, name, email, password, created_at) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Test User', 'test@example.com',
     '$2b$12$PXb3y1izCF6jexdLGzXxs.oBC43tny83anAxspfC5kNq8xeIOJjnC', NOW());

-- Project owned by test user
INSERT INTO projects (id, name, description, owner_id, created_at) VALUES
    ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Website Redesign', 'Q2 redesign of the company website', 
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', NOW());

-- 3 Tasks with different statuses
INSERT INTO tasks (id, title, description, status, priority, project_id, assignee_id, created_by, due_date, created_at, updated_at) VALUES
    ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Design homepage mockup', 'Create wireframes and high-fidelity mockups for the new homepage',
     'TODO', 'HIGH', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '2026-04-20', NOW(), NOW()),

    ('d4e5f6a7-b8c9-0123-defa-234567890123', 'Implement authentication', 'Set up JWT-based login and registration flow',
     'IN_PROGRESS', 'MEDIUM', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '2026-04-25', NOW(), NOW()),

    ('e5f6a7b8-c9d0-1234-efab-345678901234', 'Write API documentation', 'Document all REST endpoints with request/response examples',
     'DONE', 'LOW', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', NULL,
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '2026-04-15', NOW(), NOW());
