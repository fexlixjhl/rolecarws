DROP TABLE IF EXISTS COUNTRIES;
DROP TABLE IF EXISTS CITIES;
DROP TABLE IF EXISTS STATIONS;
CREATE TABLE `countries` (
  `codcountry` varchar(255) NOT NULL,
  `idcountry` int(11) NOT NULL AUTO_INCREMENT,
  `descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idcountry`,`codcountry`),
  KEY `codcountry` (`codcountry`)
) ENGINE=InnoDB AUTO_INCREMENT=2619 DEFAULT CHARSET=utf8;
CREATE TABLE `cities` (
  `idcity` int(11) NOT NULL AUTO_INCREMENT,
  `codcity` varchar(255) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `codcountry` varchar(255) DEFAULT NULL,
  `idprovincia` int(11) DEFAULT '0',
  PRIMARY KEY (`idcity`,`codcity`),
  KEY `codcity` (`codcity`),
  KEY `codcountryFK_idx` (`codcountry`),
  CONSTRAINT `codcountryFK` FOREIGN KEY (`codcountry`) REFERENCES `countries` (`codcountry`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2596 DEFAULT CHARSET=utf8;
CREATE TABLE `stations` (
  `idstation` int(11) NOT NULL AUTO_INCREMENT,
  `codstation` varchar(255) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `codcity` varchar(255) DEFAULT NULL,
  `idprovincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`idstation`,`codstation`),
  KEY `codstation` (`codstation`),
  KEY `codcityFK_idx` (`codcity`)
) ENGINE=InnoDB AUTO_INCREMENT=3763 DEFAULT CHARSET=utf8;
