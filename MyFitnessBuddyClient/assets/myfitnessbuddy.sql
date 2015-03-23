CREATE TABLE `foods` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,
  `name` varchar(45) NOT NULL,
  `cal` float NOT NULL,
  `protein` float NOT NULL,
  `carbs` float NOT NULL,
  `fat` float NOT NULL
);
CREATE TABLE `intake` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `amount` float NOT NULL,
  `serving` INTEGER NOT NULL,
  `meal` INTEGER NOT NULL,
  CONSTRAINT `fk_intake_meal_ref_meals_id` FOREIGN KEY (`meal`) REFERENCES `meals` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_intake_serving_ref_servings_id` FOREIGN KEY (`serving`) REFERENCES `serving` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE TABLE `meals` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `name` varchar(45) NOT NULL,
  `date` date NOT NULL
);
CREATE TABLE `serving` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `food` INTEGER DEFAULT NULL,
  `grams` float NOT NULL,
  CONSTRAINT `foods` FOREIGN KEY (`food`) REFERENCES `foods` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE TABLE `goals` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `cal` float NOT NULL,
  `protein` float NOT NULL,
  `carbs` float NOT NULL,
  `fat` float NOT NULL,
  `date` date NOT NULL
);
