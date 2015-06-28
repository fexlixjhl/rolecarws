USE `rolecar`;
DROP procedure IF EXISTS `Copy_Offline`;

DELIMITER $$
USE `rolecar`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `Copy_Offline`()
BEGIN
DECLARE aux_off varchar(2) default '';
DECLARE aux_on varchar(2) default '';
DECLARE sch_off varchar(2) default '';
DECLARE sch_on varchar(2) default '';
DECLARE cont int default 0;
SELECT schema_on, schema_off into sch_on, sch_off FROM SCHEMA_SW;

#asignamos las variables al contrario ya que vamos a cambiar el esquema activo - offLine
IF sch_on = 'B' THEN
	SET aux_off = '_b';
ELSE
	SET aux_on = '_b';
END if;

#cambio esquema activo
UPDATE SCHEMA_SW SET SCHEMA_OFF = sch_on, SCHEMA_ON = sch_off WHERE ID = 1;

 # borramos esquema off
 SET @delete1 = concat ("delete from countries", aux_off);
 SET @delete2 = concat ("delete from cities", aux_off);
 SET @delete3 = concat ("delete from stations", aux_off);
 
prepare stmt from @delete1;
execute stmt;
prepare stmt from @delete2;
execute stmt;
prepare stmt from @delete3;
execute stmt;
 

 SET @insert1 = concat("INSERT INTO COUNTRIES", aux_off, " (codcountry, idcountry, descr) 
	SELECT codcountry, idcountry, descr FROM countries", aux_on,";");
 SET @insert2 = concat("INSERT INTO CITIES", aux_off, "  (idcity, codcity, descr, codcountry, idprovincia) 
	SELECT idcity, codcity, descr, codcountry, idprovincia FROM cities", aux_on, ";");
 SET @insert3 = concat("INSERT INTO STATIONS", aux_off, " (idstation, codstation, descr, codcity, idprovincia) 
	SELECT idstation, codstation, descr, codcity, idprovincia FROM stations", aux_on, ";");
#asignacion de variable a ejecucion
#select concat("***", @insert1) AS '*** DEBUG';
prepare stmt from @insert1;
execute stmt;
prepare stmt from @insert2;
execute stmt;
prepare stmt from @insert3;
execute stmt;

END$$

DELIMITER ;