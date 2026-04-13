-- U2__remove_seed_data.sql
-- Undo migration for V2: removes seeded test data

DELETE FROM tasks WHERE id IN (
    'c3d4e5f6-a7b8-9012-cdef-123456789012',
    'd4e5f6a7-b8c9-0123-defa-234567890123',
    'e5f6a7b8-c9d0-1234-efab-345678901234'
);

DELETE FROM projects WHERE id = 'b2c3d4e5-f6a7-8901-bcde-f12345678901';

DELETE FROM users WHERE id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';
