CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE address
(
    id      BIGINT NOT NULL,
    street  VARCHAR(255),
    suite   VARCHAR(255),
    city    VARCHAR(255),
    zipcode VARCHAR(255),
    lat     DOUBLE PRECISION,
    lng     DOUBLE PRECISION,
    user_id BIGINT,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE zcm_user
(
    id       BIGINT NOT NULL,
    ext_id   BIGINT,
    name     VARCHAR(255),
    username VARCHAR(255),
    email    VARCHAR(255),
    phone    VARCHAR(255),
    website  VARCHAR(255),
    CONSTRAINT pk_zcm_user PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT fk_zcm_user_on_address FOREIGN KEY (user_id) REFERENCES zcm_user (id);
