DROP TABLE IF EXISTS COUNTRIES_B;
DROP TABLE IF EXISTS CITIES_B;
DROP TABLE IF EXISTS STATIONS_B;
CREATE TABLE `countries_b` (
  `codcountry` varchar(255) NOT NULL,
  `idcountry` int(11) NOT NULL AUTO_INCREMENT,
  `descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idcountry`,`codcountry`),
  KEY `codcountry` (`codcountry`)
) ENGINE=InnoDB AUTO_INCREMENT=2619 DEFAULT CHARSET=utf8;
CREATE TABLE `cities_b` (
  `idcity` int(11) NOT NULL AUTO_INCREMENT,
  `codcity` varchar(255) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `codcountry` varchar(255) DEFAULT NULL,
  `idprovincia` int(11) DEFAULT '0',
  PRIMARY KEY (`idcity`,`codcity`),
  KEY `codcity_b` (`codcity`),
  KEY `codcountryFK_b_idx` (`codcountry`),
  CONSTRAINT `codcountryFK_b` FOREIGN KEY (`codcountry`) REFERENCES `countries_b` (`codcountry`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2596 DEFAULT CHARSET=utf8;
CREATE TABLE `stations_b` (
  `idstation` int(11) NOT NULL AUTO_INCREMENT,
  `codstation` varchar(255) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `codcity` varchar(255) DEFAULT NULL,
  `idprovincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`idstation`,`codstation`),
  KEY `codstation_b` (`codstation`),
  KEY `codcityFK_b_idx` (`codcity`)
) ENGINE=InnoDB AUTO_INCREMENT=3763 DEFAULT CHARSET=utf8;
