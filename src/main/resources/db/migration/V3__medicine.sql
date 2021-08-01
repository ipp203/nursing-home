CREATE TABLE `medicines` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `daily_dose` INTEGER NOT NULL,
                          `name` VARCHAR(255) NOT NULL,
                          `type` VARCHAR(16) NOT NULL,
                          `resident_id` BIGINT,
                          constraint foreign KEY(`resident_id`) references residents(`id`));