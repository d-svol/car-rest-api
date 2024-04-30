INSERT INTO car (object_id, make, year, model) VALUES
('objectId1', 'make1', 2022, 'model1'),
('objectId2', 'make2', 2023, 'model2'),
('a1B2c3D4', 'Toyota', 2022, 'Camry');

INSERT INTO category (name) VALUES
('category1'),
('category2'),
('Sedan'),
('Pickup');

INSERT INTO car_category (car_id, category_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(3, 4);
