drop table if exists users;
drop table if exists endpoint CASCADE;
drop table if exists scan_schedule CASCADE;
drop table if exists quarantine_file cascade;
drop table if exists map_scan_schedule_to_endpoint;
drop table if exists report CASCADE;
drop table if exists map_report_to_quarantine_files;
-- drop type if exists "ScanType" CASCADE;

-- for now not used
-- create type "ScanRecurrence" as enum('OneTime', 'Daily', 'Weekly', 'Monthly');
-- create type "ScanType" as enum('FastScan', 'FullScan');

create table users
(
    email            varchar primary key,
    name             varchar not null,
    surname          varchar not null,
    encoded_password varchar not null
);

create table endpoint
(
    id   serial primary key,
    name varchar not null,
    otk  varchar
);


create table scan_schedule
(
    id        serial primary key,
    name      varchar not null,
    cron      varchar not null,
    scan_type varchar not null,
    enabled   bool    not null
);

create table quarantine_file
(
    id               serial primary key,
    scan_schedule_id int,
    endpoint_id      int  not null,
    name             varchar not null,
    details          varchar,
    discovered_date  varchar not null,
    foreign key (scan_schedule_id) references scan_schedule on delete set null,
    foreign key (endpoint_id) references endpoint on delete set null
);


create table map_scan_schedule_to_endpoint
(
    scan_schedule_id int,
    endpoint_id      int,
    PRIMARY KEY (scan_schedule_id, endpoint_id),
    FOREIGN KEY (scan_schedule_id) REFERENCES scan_schedule ON DELETE CASCADE,
    FOREIGN KEY (endpoint_id) REFERENCES endpoint ON DELETE CASCADE
);

create table report
(
    id               serial primary key,
    name             varchar not null,
    scan_schedule_id int unique,
    FOREIGN KEY (scan_schedule_id) references scan_schedule ON DELETE SET NULL
);

create table map_report_to_quarantine_files
(
    report_id          int,
    quarantine_file_id int,
    PRIMARY KEY (report_id, quarantine_file_id),
    FOREIGN KEY (report_id) references report ON DELETE CASCADE,
    FOREIGN KEY (quarantine_file_id) references quarantine_file ON DELETE CASCADE
);