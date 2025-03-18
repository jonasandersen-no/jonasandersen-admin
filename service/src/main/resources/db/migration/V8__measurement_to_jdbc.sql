alter sequence measurement_dbo_seq increment by 1;

select setval('measurement_dbo_seq', (select max(id) from measurement_dbo));

ALTER TABLE measurement_dbo
    ALTER COLUMN id SET DEFAULT nextval('measurement_dbo_seq');

alter sequence measurement_dbo_seq owned by measurement_dbo.id;