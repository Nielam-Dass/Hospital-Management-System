truncate table visit;

insert into visit (visit_id, patient_id, admitted_on, discharged_on, reason) values
(1, 1, '2024-05-11', null, 'Shoulder dislocation'),
(2, 5, '2024-05-12', null, 'Wrist injury'),
(3, 2, '2024-05-09', null, 'Gun shot wound'),
(4, 5, '2012-06-07', '2012-06-09', 'Going into labor');

alter sequence visit_sequence restart with 5;
