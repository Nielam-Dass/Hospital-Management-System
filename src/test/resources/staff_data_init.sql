truncate table staff;

insert into staff (department, dob, first_name, hired_on, last_name, role, salary, staff_id) values
('Cardiology', '1979-01-09', 'Max', '2015-03-05', 'Anderson', 'Heart Surgeon', 195000, 1),
(null, '1975-04-17', 'Daniel', '2012-03-20', 'Carter', 'CEO', 150000, 2),
('Cardiology', '1985-06-04', 'Larry', '2014-02-22', 'Davis', 'ECG Technician', 90000, 3),
('Cleaning', '1987-05-15', 'Christopher', '2021-11-07', 'Wilson', 'Janitor', 50000, 4),
('Nursing', '1988-08-16', 'Ben', '2016-10-02', 'Robinson', 'Nurse', 85000, 5);

alter sequence staff_sequence restart with 6;
