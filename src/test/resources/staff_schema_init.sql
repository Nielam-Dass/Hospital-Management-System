drop table if exists staff CASCADE;

drop sequence if exists staff_sequence;

create sequence staff_sequence start with 1 increment by 1;

create table staff
(staff_id bigint not null, department varchar(255), dob date not null,
first_name varchar(255) not null, hired_on date not null, last_name varchar(255) not null,
role varchar(255) not null, salary integer not null, primary key (staff_id));
