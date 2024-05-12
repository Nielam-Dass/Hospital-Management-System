truncate table patient;

insert into patient (dob, first_name, insurance, last_name, phone_number, sex, ssn, patient_id) values
('1998-08-04', 'Adam', 'XHealth', 'Smith', '555-555-5555', 'Male', 111223333, 1),
('2004-04-18', 'Adam', null, 'Smith', '222-222-2222', 'Male', 222334444, 2),
('2000-09-04', 'Jane', 'XHealth', 'Smith', '444-444-4444', 'Female', 333221111, 3),
('1970-01-11', 'Corey', 'GoldShield', 'White', '100-200-3000', 'Male', 333445555, 4),
('1971-07-10', 'Lisa', 'GoldShield', 'White', '100-200-3000', 'Female', 555443333, 5),
('2012-06-07', 'James', 'GoldShield', 'White', '100-200-3000', 'Male', 444556666, 6),
('2012-06-07', 'Henry', 'GoldShield', 'White', '100-200-3000', 'Male', 555667777, 7);

alter sequence patient_sequence restart with 8;
