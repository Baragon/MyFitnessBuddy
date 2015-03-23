CREATE TABLE IF NOT EXISTS `foods` (
  `id` INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `cal` float NOT NULL,
  `protein` float NOT NULL,
  `carbs` float NOT NULL,
  `fat` float NOT NULL
);
CREATE TABLE IF NOT EXISTS `serving` (
  `id` INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `food` INTEGER DEFAULT NULL,
  `grams` float NOT NULL,
  CONSTRAINT `foods` FOREIGN KEY (`food`) REFERENCES `foods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);