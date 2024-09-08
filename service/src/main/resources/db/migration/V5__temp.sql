CREATE SEQUENCE IF NOT EXISTS measurement_dbo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE measurement_dbo
(
    id          BIGINT  NOT NULL,
    temperature VARCHAR(255),
    humidity    INTEGER NOT NULL,
    timestamp   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_measurementdbo PRIMARY KEY (id)
);