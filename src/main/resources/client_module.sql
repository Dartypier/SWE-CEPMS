drop table if exists quarantine;
drop table if exists endpoint;
drop table if exists scan_schedule;

create table endpoint
(
    id   serial primary key,
    name varchar not null,
    otk  varchar
);

create table scan_schedule
(
    id          serial primary key,
    cron        varchar not null,
    scan_type   varchar not null,
    job_key     varchar not null,
    trigger_key varchar not null
);

create table quarantine
(
    id               serial primary key,
    scan_schedule_id int,
    endpoint_id      int  not null,
    name             varchar not null,
    details          varchar,
    discovered_date  varchar not null,
    FOREIGN KEY (scan_schedule_id) references scan_schedule ON DELETE set null
);
