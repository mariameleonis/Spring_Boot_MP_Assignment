CREATE TABLE person (
                        id BIGSERIAL NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
);

ALTER TABLE person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);

ALTER TABLE person
    ADD CONSTRAINT person_email_key UNIQUE (email);