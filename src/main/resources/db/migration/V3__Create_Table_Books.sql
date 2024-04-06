CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    author varchar(150),
    launch_date DATE NOT NULL,
    price numeric(10, 2) NOT NULL,
    title varchar(200)
);
