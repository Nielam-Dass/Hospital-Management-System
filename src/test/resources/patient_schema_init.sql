drop table if exists patient CASCADE;

drop sequence if exists patient_sequence;

create sequence patient_sequence start with 1 increment by 1;

create table patient
(patient_id bigint not null, dob date not null, first_name varchar(255) not null,
insurance varchar(255), last_name varchar(255) not null, phone_number varchar(20) not null,
sex varchar(10) not null, ssn integer not null, primary key (patient_id));
