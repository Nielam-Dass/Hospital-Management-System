drop table if exists visit CASCADE;

drop sequence if exists visit_sequence;

create sequence visit_sequence start with 1 increment by 1;

create table visit
(visit_id bigint not null, admitted_on date not null, discharged_on date,
reason varchar(255) not null, patient_id bigint not null, primary key (visit_id));
