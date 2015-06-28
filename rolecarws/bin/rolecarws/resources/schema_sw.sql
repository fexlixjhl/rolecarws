DROP TABLE IF EXISTS SCHEMA_SW;
CREATE TABLE `schema_sw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_off` varchar(45) NOT NULL,
  `schema_on` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Schema switch';
INSERT INTO `rolecar`.`schema_sw`
(`id`,
`schema_off`,
`schema_on`)
VALUES
(1,
'B',
'A');
 commit;