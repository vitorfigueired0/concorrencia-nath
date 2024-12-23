INSERT INTO products (name, stock, price, category_id)
SELECT * FROM (
   SELECT 'Playstation 5', 1000, 1999.99, (SELECT id FROM categories WHERE name = 'Video-game' LIMIT 1)
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM products);