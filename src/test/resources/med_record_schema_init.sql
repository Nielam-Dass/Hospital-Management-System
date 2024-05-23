drop table if exists medical_record CASCADE;

drop sequence if exists record_sequence;

create sequence record_sequence start with 1 increment by 1;

create table medical_record
(record_id bigint not null, created_on date not null, description varchar(255) not null,
name varchar(255) not null, record_file varchar(255), added_by bigint not null, patient_id bigint not null,
primary key (record_id));
