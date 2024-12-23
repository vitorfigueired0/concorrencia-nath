INSERT INTO categories (name, description)
SELECT * FROM (
  SELECT 'Video-game', 'Video game category'
 ) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categories)

