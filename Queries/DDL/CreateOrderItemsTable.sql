CREATE TABLE order_item (
    id UUID PRIMARY KEY,
    order_entity_id UUID,
    product_id UUID,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (order_entity_id) REFERENCES order_entity(id)
);
