DROP TABLE IF EXISTS person;
CREATE TABLE person (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE
);
INSERT INTO person (id, name, email) VALUES
                                     (25, 'John Doe', 'john.doe@example.com'),
                                     (26, 'Jane Smith', 'jane.smith@example.com'),
                                     (27, 'Michael Johnson', 'michael.johnson@example.com'),
                                     (28, 'Emily Davis', 'emily.davis@example.com'),
                                     (29, 'David Brown', 'david.brown@example.com');