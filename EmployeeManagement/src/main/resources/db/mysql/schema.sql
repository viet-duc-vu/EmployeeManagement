CREATE TABLE IF NOT EXISTS employee (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    email VARCHAR(255),
    department VARCHAR(255)
) engine=InnoDB;