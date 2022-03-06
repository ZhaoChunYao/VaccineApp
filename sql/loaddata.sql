-- Include your INSERT SQL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO cs421;

-- Remember to put the INSERT statements for the tables with foreign key references
--    ONLY AFTER the parent tables!

-- This is only an example of how you add INSERT statements to this file.
--   You may remove it.
INSERT INTO MYTEST01 (id, value) VALUES(4, 1300);
-- A more complex syntax that saves you typing effort.
INSERT INTO MYTEST01 (id, value) VALUES
 (7, 5144)
,(3, 73423)
,(6, -1222)
;

insert into Location values('Jewish General','Montreal','Code A','Street A');
insert into Location values('Hospital A','Montreal','Code B','Street B');
insert into Location values('Hospital B','Montreal','Code C','Street C');
insert into Location values('Hospital C','nonMontreal','Code D','Street D');
insert into Location values('Hospital D','nonMontreal','Code E','Street E');

insert into Nurse values(1,'Nurse A','Jewish General');
insert into Nurse values(2,'Nurse B','Hospital A');
insert into Nurse values(3,'Nurse C','Hospital B');
insert into Nurse values(4,'Nurse D','Hospital C');
insert into Nurse values(5,'Nurse D','Hospital D');

insert into Registrant values(1,'Montreal','Code 1','Street 1','Jane Doe','female',111,'2020-01-01','Children below 10',2);
insert into Registrant values(2,'City A','Code 2','Street 2','B','female',222,'2000-01-01','Everyone Else',4);
insert into Registrant values(3,'City B','Code 3','Street 3','C','male',333,'1999-01-01','Essential Service Workers',3);
insert into Registrant values(4,'City C','Code 4','Street 1','D','female',444,'2001-01-01','Health Care workers',1);
insert into Registrant values(5,'City D','Code 5','Street 3','E','male',555,'2020-02-01','Children below 10',2);

insert into Assignment values(1,'Jewish General','2021-3-20');
insert into Assignment values(2,'Jewish General','2021-3-20');
insert into Assignment values(1,'Hospital A','2021-2-6');
insert into Assignment values(3,'Hospital C','2021-2-6');
insert into Assignment values(4,'Jewish General','2021-1-19');
insert into Assignment values(5,'Hospital D','2021-1-18');

insert into Vaccine values('Pfizer-BioNTech',3,3);
insert into Vaccine values('D',3,3);
insert into Vaccine values('C',3,3);
insert into Vaccine values('B',3,3);
insert into Vaccine values('A',3,3);

insert into Batch values('Pfizer-BioNTech',1,'2021-1-11','2021-1-25',100,'Hospital D');
insert into Batch values('Pfizer-BioNTech',2,'2021-1-11','2021-1-25',100,'Jewish General');
insert into Batch values('Pfizer-BioNTech',3,'2021-1-29','2021-2-13',100,'Hospital C');
insert into Batch values('Pfizer-BioNTech',4,'2021-1-29','2021-2-13',100,'Hospital A');
insert into Batch values('Pfizer-BioNTech',5,'2021-3-13','2021-3-27',100,'Jewish General');

insert into Vial values('Pfizer-BioNTech',1,10,5,5);
insert into Vial values('Pfizer-BioNTech',2,22,4,1);
insert into Vial values('Pfizer-BioNTech',3,13,3,3);
insert into Vial values('Pfizer-BioNTech',2,43,1,1);
insert into Vial values('Pfizer-BioNTech',5,31,2,2);

insert into Slot values('Hospital D',1,'2021-1-18','00.00.00','2021-1-17','00.00.00',5,'Pfizer-BioNTech',1,10);
insert into Slot values('Jewish General',1,'2021-1-19','00.00.00','2021-1-18','00.00.00',1,'Pfizer-BioNTech',2,22);
insert into Slot values('Hospital C',1,'2021-2-6','00.00.00','2021-2-5','00.00.00',3,'Pfizer-BioNTech',3,13);
insert into Slot values('Hospital A',1,'2021-2-6','00.00.00','2021-2-5','00.00.00',1,'Pfizer-BioNTech',2,43);
insert into Slot values('Jewish General',1,'2021-3-20','00.00.00','2021-3-19','00.00.00',2,'Pfizer-BioNTech',5,31);
insert into Slot(name,slotNo,date,time) values('Jewish General',4,'2021-3-20','08.30.00');

