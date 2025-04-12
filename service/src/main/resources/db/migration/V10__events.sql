create table events
(
    aggregate_root_id uuid,
    recorded_at       timestamp not null,
    event_id          integer   not null,
    event_type        text      not null,
    content           json      not null
);