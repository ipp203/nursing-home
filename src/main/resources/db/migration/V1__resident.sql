-- create table if not exists residents (id bigint not null auto_increment,
--     date_of_birth date not null,
--     gender varchar(255),
--     name varchar(255) not null,
--     status varchar(255),
--     primary key (id));

CREATE TABLE `residents` (`id` bigint not null AUTO_INCREMENT PRIMARY KEY,
                          `date_of_birth` DATE NOT NULL,
                          `gender` VARCHAR(255),
                          `name` VARCHAR(255) NOT NULL,
                          `status` VARCHAR(255));
