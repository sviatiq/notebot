DROP TABLE IF EXISTS shedule;
CREATE TABLE shedule(

week varchar(1) not null,
date varchar(15) not null,
time varchar(20) not null,
location varchar(50) not null,
description varchar(100) not null
)


CREATE TABLE university_shedule(

week_num varchar(1) not null,
lesson_date varchar(20),
lesson_name varchar(50),
room varchar(7) ,
lesson_time varchar(20) ,
teacher varchar(50)

);
