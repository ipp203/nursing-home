create table if not exists resident (id bigint not null auto_increment,
    date_of_birth date,
    gender varchar(255),
    name varchar(255),
    status varchar(255),
    primary key (id));