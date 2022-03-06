-- Include your create table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO cs421;

-- Remember to put the create table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables has already been created.

-- This is only an example of how you add create table ddls to this file.
--   You may remove it.
CREATE TABLE MYTEST01
(
  id INTEGER NOT NULL
 ,value INTEGER
 ,PRIMARY KEY(id)
);

create table Registrant(healthInsuranceNumber int not null,city varchar(20),postalCode varchar(7),streetAddr varchar(100),name varchar(20),gender varchar(6),phone int,dateOfBirth date,category varchar(50),priority int,primary key (healthInsuranceNumber),check (priority>=1 and priority<=4));

create table Location(name varchar(20) not null,city varchar(20),postalCode varchar(7),streetAddr varchar(100),primary key (name));

create table Nurse(licenseNo int not null,name varchar(20),employer varchar(20),primary key (licenseNo));

create table Assignment(licenseNo int not null,name varchar(20) not null,date date not null,primary key (licenseNo,name,date),foreign key (licenseNo) references Nurse,foreign key (name) references Location);

create table Vaccine(vaccineName varchar(20) not null,waitingPeriod int,doeses int,primary key (vaccineName));

create table Batch(vaccineName varchar(20) not null, batchNo int not null,manufacturedDate date,expireDate date,totalNumberOfVials int,name varchar(20) not null,primary key (vaccineName,batchNo),foreign key (name) references Location);

create table Vial(vaccineName varchar(20) not null,batchNo int not null,vialNo int not null,licenseNo int not null,healthInsuranceNUmber int not null,primary key (vaccineName,batchNo,vialNo),foreign key (licenseNo) references Nurse, foreign key (healthInsuranceNumber) references Registrant);

create table Slot(name varchar(20) not null,slotNo int not null,date date not null,time time not null,allocationDate date,allocationTime time,healthInsuranceNumber int,vaccineName varchar(20),batchno int,vialNo int,primary key (name,slotNo,date,time),foreign key (name) references Location, foreign key (healthInsuranceNumber) references Registrant,foreign key (vaccineName,batchno,vialNo) references Vial);
