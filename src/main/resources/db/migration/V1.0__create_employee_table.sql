create table company
(
id int   AUTO_INCREMENT  PRIMARY KEY,
company_name varchar(255)
);
create table employee
(
id int  AUTO_INCREMENT PRIMARY KEY,
name varchar(255),
age int,
gender varchar(255),
salary int,
company_id int,
foreign key (company_id) references  company (id)
);