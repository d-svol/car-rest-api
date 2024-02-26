DROP TABLE if EXISTS car;
DROP TABLE if EXISTS category;

CREATE TABLE category (
   id SERIAL PRIMARY KEY not null,
   name VARCHAR(255) NOT NULL
);

CREATE TABLE car (
   id SERIAL PRIMARY KEY,
   object_id VARCHAR(255),
   model VARCHAR(255),
   brand VARCHAR(255),
   year INTEGER,
);

CREATE TABLE car_category (
   id SERIAL PRIMARY KEY,
   car_id INTEGER NOT NULL,
   category_id INTEGER NOT NULL,
   FOREIGN KEY (category_id) REFERENCES category ON DELETE CASCADE,
   FOREIGN KEY (car_id) REFERENCES carON DELETE CASCADE
);