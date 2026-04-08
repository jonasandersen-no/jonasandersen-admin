CREATE TABLE events
(
    event_id          UUID                        NOT NULL,
    aggregate_root_id UUID,
    version           bigint                      not null,
    recorded_at       TIMESTAMP WITHOUT TIME ZONE not null,
    event_type        VARCHAR                     not null,
    content              VARCHAR                     not null,
    CONSTRAINT pk_events PRIMARY KEY (event_id),
    UNIQUE (aggregate_root_id, version)

);

create index idx_aggregate on events (aggregate_root_id, version asc);