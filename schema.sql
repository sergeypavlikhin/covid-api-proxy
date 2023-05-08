CREATE TABLE countries
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE cases_data
(
    id         BIGSERIAL PRIMARY KEY,
    country_id BIGINT REFERENCES countries (id) NOT NULL,
    date       TIMESTAMP                        NOT NULL,
    new_cases  INT                              NOT NULL,
    UNIQUE (country_id, date)
);
