INSERT INTO roles (name) VALUES
     ('USER'),
     ('ADMIN')
ON CONFLICT (name) DO NOTHING