-- Creating The Schema
CREATE SCHEMA `wms` ;

-- Creating the `users` Table
CREATE TABLE `wms`.`users` (
  `id` INT NOT NULL DEFAULT 1000 AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `position` ENUM('ADMIN', 'WAREHOUSE_OPERATOR', 'PRODUCTION_OPERATOR') NOT NULL,
  `status` ENUM('PENDING', 'APPROVED', 'DENIED') NULL DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);
ALTER TABLE `wms`.`users`
  ADD COLUMN `display_name` VARCHAR(50) NOT NULL AFTER `password`;
  
-- Creating the `reports` Table
  CREATE TABLE `wms`.`reports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sender` INT NULL,
  `message` VARCHAR(300) NULL,
  `timestamp` DATE NULL,
  PRIMARY KEY (`id`));
  
-- Creating Foreign Keys
ALTER TABLE `wms`.`reports` 
ADD CONSTRAINT `FK_sender_id`
  FOREIGN KEY (`sender`)
  REFERENCES `wms`.`users` (`id`);

-- Creating the `products` Table
  CREATE TABLE `wms`.`products` (
    `id` INT NOT NULL DEFAULT 1000,
    `description` VARCHAR(100) NULL,
    `shelf` INT NULL,
    PRIMARY KEY (`id`));

-- Adding Constraint to the `products` Table
ALTER TABLE `wms`.`products`
    ADD CONSTRAINT `CK_Valid_shelf` CHECK (shelf>=1 AND shelf<=10);

-- Creating the `requests` Table
CREATE TABLE `wms`.`requests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sender` INT NULL,
  `receiver` INT NULL,
  `product_id` INT NULL,
  `quantity` INT NULL,
  `timestamp` DATE NULL,
  `status` ENUM('PENDING', 'APPROVED', 'DENIED') NULL DEFAULT 'PENDING',
  PRIMARY KEY (`id`));

-- Adding Foreign Keys and Constraints
ALTER TABLE `wms`.`requests`
ADD CONSTRAINT `FK_request_sender`
  FOREIGN KEY (`sender`)
  REFERENCES `wms`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_request_receiver`
  FOREIGN KEY (`receiver`)
  REFERENCES `wms`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_product_id`
  FOREIGN KEY (`product_id`)
  REFERENCES `wms`.`products` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
ALTER TABLE `wms`.`requests`
	ADD CONSTRAINT `CK_quantity_valid` CHECK (quantity>0);

