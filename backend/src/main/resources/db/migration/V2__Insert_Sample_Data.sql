INSERT INTO products (name, description, category) VALUES
('UltraTech PPC Cement 50 kg', 'High quality PPC cement', 'Cement'),
('TMT Steel Bar 12mm', 'High strength construction steel', 'Steel'),
('AAC Blocks', 'Lightweight construction blocks', 'Blocks');

INSERT INTO sellers (name, email, status) VALUES
('Alice Tech', 'alice@example.com', 'ACTIVE'),
('Bob Furniture', 'bob@example.com', 'ACTIVE'),
('Charlie Suspended', 'charlie@example.com', 'SUSPENDED');

INSERT INTO seller_listings (product_id, seller_id, price, quantity) VALUES
(1, 1, 999.99, 10),
(2, 1, 699.00, 20),
(3, 2, 150.50, 15);
