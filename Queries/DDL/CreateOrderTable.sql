CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID,
    status VARCHAR(255),
    created_at TIMESTAMP
);
