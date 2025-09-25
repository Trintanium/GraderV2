
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT true,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    role VARCHAR(50) DEFAULT 'USER',
    last_name VARCHAR(100),
    student_id VARCHAR(50)
);

INSERT INTO users (id, created_date, email, enabled, password_hash, first_name, role, last_name, student_id) 
VALUES (
    2, 
    '2025-09-15 10:09:33.782584',
    'trin.mees@gmail.com',
    true,
    '$argon2id$v=19$m=16384,t=2,p=1$ZN0lRG477S0yAvo9s/CB0A$sVIuunWPCnxP8QuPG49wgX9foyDr9TcV4mxobxFHdhc',
    '',
    'ADMIN',
    '',
    'NK2'
) ON CONFLICT (email) DO NOTHING;

SELECT setval('users_id_seq', (SELECT GREATEST(MAX(id), 1) FROM users));


CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_student_id ON users(student_id);

