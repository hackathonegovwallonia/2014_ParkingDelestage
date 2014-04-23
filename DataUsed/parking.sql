CREATE TABLE `Tec`.`Parking` (
  `idParking` INT NOT NULL,
  `nom` VARCHAR(45) NULL,
  `prix` VARCHAR(45) NULL,
  `longitude` VARCHAR(45) NULL,
  `latitude` VARCHAR(45) NULL,
  PRIMARY KEY (`idParking`));

ALTER TABLE `Tec`.`Parking` 
CHANGE COLUMN `idParking` `idParking` INT(11) NOT NULL AUTO_INCREMENT ;

INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking hotel de ville', '4.866921', '50.467195');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking leopold', '4.867017', '50.468759');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking du centre', '4.865370', '50.466837');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking gifar', '4.866010', '50.462760');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking beffroi', '4.867497', '50.463419');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking St Nicolas', '4.882992', '50.466811');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking Namur Expo', '4.848264', '50.467032');
INSERT INTO `Tec`.`Parking` (`nom`, `longitude`, `latitude`) VALUES ('parking Grognon', '4.868645', '50.461452');

