CREATE TABLE `residents` (`id` bigint not null AUTO_INCREMENT PRIMARY KEY,
                          `date_of_birth` DATE NOT NULL,
                          `gender` VARCHAR(8),
                          `name` VARCHAR(255) NOT NULL,
                          `status` VARCHAR(16),
                          `room_id` BIGINT,
                          constraint foreign KEY(`room_id`) references rooms(`id`));
