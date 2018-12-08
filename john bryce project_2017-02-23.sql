# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.17)
# Database: john bryce project
# Generation Time: 2017-02-23 17:15:56 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Company`;

CREATE TABLE `Company` (
  `ID` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `COMP_NAME` varchar(50) NOT NULL DEFAULT '',
  `PASSWORD` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Company` WRITE;
/*!40000 ALTER TABLE `Company` DISABLE KEYS */;

INSERT INTO `Company` (`ID`, `COMP_NAME`, `PASSWORD`, `EMAIL`)
VALUES
	(2,'Hush Puppies','1111','hush@gmail.com'),
	(7,'PureFit','3333','Fitp@pure.com'),
	(16,'Angel','4444','angel@ang.comm'),
	(17,'Food Market','5555','foodmarket@gmail.com'),
	(18,'Food Factory','6666','foodf@walla.com');

/*!40000 ALTER TABLE `Company` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Company_Coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Company_Coupon`;

CREATE TABLE `Company_Coupon` (
  `COMP_ID` decimal(10,0) NOT NULL,
  `COUPON_ID` decimal(10,0) NOT NULL,
  PRIMARY KEY (`COMP_ID`,`COUPON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Company_Coupon` WRITE;
/*!40000 ALTER TABLE `Company_Coupon` DISABLE KEYS */;

INSERT INTO `Company_Coupon` (`COMP_ID`, `COUPON_ID`)
VALUES
	(2,11),
	(2,117),
	(2,118),
	(2,119),
	(2,120),
	(2,122),
	(7,107),
	(7,123),
	(7,124),
	(7,125),
	(7,126),
	(16,108),
	(16,127),
	(17,109),
	(18,110);

/*!40000 ALTER TABLE `Company_Coupon` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Coupon`;

CREATE TABLE `Coupon` (
  `ID` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(50) NOT NULL DEFAULT '',
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `AMOUNT` int(20) DEFAULT NULL,
  `TYPE` varchar(50) DEFAULT '',
  `MESSAGE` varchar(50) DEFAULT NULL,
  `PRICE` double DEFAULT NULL,
  `IMAGE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Coupon` WRITE;
/*!40000 ALTER TABLE `Coupon` DISABLE KEYS */;

INSERT INTO `Coupon` (`ID`, `TITLE`, `START_DATE`, `END_DATE`, `AMOUNT`, `TYPE`, `MESSAGE`, `PRICE`, `IMAGE`)
VALUES
	(11,'Health Benefits','2023-11-04','2025-11-05',149,'HEALTH','Get special health care service',150,'medical'),
	(107,'Food Taste','2019-06-06','2019-12-12',298,'FOOD','A food tasting tour',199,'food plate'),
	(108,'Special vacation','2020-01-01','2020-12-31',498,'TRAVELLING','A vacation for buying products at 10,000 price',2000,'kosamoi'),
	(109,'Drinks','2018-03-02','2018-12-31',248,'RESTURANTS','4 drinks at the Kako resurant',99,'coffee'),
	(110,'Electrics','2017-08-08','2017-12-12',899,'ELECTRICITY','A big food proccesor at a special price',400,'foo proccesor'),
	(117,'Health Care','2018-01-01','2019-01-01',999,'HEALTH','A Special health care coupon',300,'doctor'),
	(118,'Camping fun','2020-02-02','2020-12-12',399,'CAMPING','Camping kit for all use',200,'fire'),
	(119,'Sport star','2019-04-04','2021-05-05',600,'SPORTS','Sport special perk',400,'fitness'),
	(120,'Dinning','2025-01-01','2025-09-09',300,'RESTURANTS','Speical dinning offer - all you can eat',500,'food'),
	(122,'Health for you','2017-02-20','2017-02-21',20,'HEALTH','Health treatment by a master',200,'bed'),
	(123,'Breakfast','2020-03-03','2022-03-03',1000,'RESTURANTS','Breakfast in the nature',300,'nature'),
	(124,'New TV','2021-03-04','2023-03-04',200,'ELECTRICITY','Get a big screen tv after big purchase',2000,'tv flat'),
	(125,'Sport Gear','2022-01-01','2022-12-31',450,'SPORTS','A complete sport gear kit',500,'sport kit'),
	(126,'Food market','2019-01-01','2019-08-08',99,'FOOD','A big food basket from the market',200,'basket'),
	(127,'Beauty sallon','2021-04-05','2022-05-05',349,'HEALTH','Beauty and health special care',150,'woman');

/*!40000 ALTER TABLE `Coupon` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Customer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Customer`;

CREATE TABLE `Customer` (
  `ID` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `CUST_NAME` varchar(50) NOT NULL DEFAULT '',
  `PASSWORD` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Customer` WRITE;
/*!40000 ALTER TABLE `Customer` DISABLE KEYS */;

INSERT INTO `Customer` (`ID`, `CUST_NAME`, `PASSWORD`)
VALUES
	(1,'John Day','111'),
	(3,'Martha Halo','222'),
	(6,'Erik Dan','333'),
	(7,'Abraham Mozes','444'),
	(8,'Tilda Tochter','555');

/*!40000 ALTER TABLE `Customer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Customer_Coupon
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Customer_Coupon`;

CREATE TABLE `Customer_Coupon` (
  `CUST_ID` decimal(10,0) NOT NULL,
  `COUPON_ID` decimal(10,0) NOT NULL,
  PRIMARY KEY (`CUST_ID`,`COUPON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Customer_Coupon` WRITE;
/*!40000 ALTER TABLE `Customer_Coupon` DISABLE KEYS */;

INSERT INTO `Customer_Coupon` (`CUST_ID`, `COUPON_ID`)
VALUES
	(1,11),
	(1,107),
	(1,108),
	(1,109),
	(3,110),
	(3,117),
	(3,118),
	(6,107),
	(6,127),
	(7,108),
	(7,126),
	(8,109);

/*!40000 ALTER TABLE `Customer_Coupon` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
