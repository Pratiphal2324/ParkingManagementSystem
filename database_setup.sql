-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: parkingmanagementsystem
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `userID` int(255) unsigned NOT NULL,
  PRIMARY KEY (`userID`),
  CONSTRAINT `fk_user` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1),(2),(4),(5),(6),(7),(9),(15),(16),(17),(18),(20),(28),(30),(31);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `floor`
--

DROP TABLE IF EXISTS `floor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `floor` (
  `floorNumber` int(255) NOT NULL,
  `noOfElectricSpaces` int(255) NOT NULL,
  `noOfFuelSpaces` int(255) NOT NULL,
  `totalNoOfSpaces` int(255) NOT NULL,
  PRIMARY KEY (`floorNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `floor`
--

LOCK TABLES `floor` WRITE;
/*!40000 ALTER TABLE `floor` DISABLE KEYS */;
INSERT INTO `floor` VALUES (1,10,20,30),(2,15,15,30),(3,5,25,30),(4,5,25,30);
/*!40000 ALTER TABLE `floor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parkingspace`
--

DROP TABLE IF EXISTS `parkingspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parkingspace` (
  `RowNumber` int(255) NOT NULL,
  `ColumnNumber` int(255) NOT NULL,
  `FloorNumber` int(255) NOT NULL,
  `Type` varchar(50) NOT NULL,
  `Category` varchar(50) NOT NULL,
  `isOccupied` tinyint(4) NOT NULL,
  PRIMARY KEY (`RowNumber`,`ColumnNumber`,`FloorNumber`),
  KEY `fk_space_floor` (`FloorNumber`),
  KEY `fk_space_typecategory` (`Category`,`Type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parkingspace`
--

LOCK TABLES `parkingspace` WRITE;
/*!40000 ALTER TABLE `parkingspace` DISABLE KEYS */;
INSERT INTO `parkingspace` VALUES (1,1,1,'Electric','TwoWheeler',1),(1,1,2,'Electric','TwoWheeler',0),(1,1,3,'Electric','TwoWheeler',0),(1,1,4,'Electric','TwoWheeler',0),(1,2,1,'Electric','TwoWheeler',0),(1,2,2,'Electric','TwoWheeler',0),(1,2,3,'Electric','TwoWheeler',0),(1,2,4,'Electric','TwoWheeler',0),(1,3,1,'Electric','TwoWheeler',0),(1,3,2,'Electric','TwoWheeler',0),(1,3,3,'Electric','TwoWheeler',0),(1,3,4,'Electric','TwoWheeler',0),(1,4,1,'Electric','TwoWheeler',0),(1,4,2,'Electric','TwoWheeler',0),(1,4,3,'Electric','FourWheeler',0),(1,4,4,'Electric','FourWheeler',0),(1,5,1,'Electric','TwoWheeler',0),(1,5,2,'Electric','TwoWheeler',0),(1,5,3,'Electric','FourWheeler',0),(1,5,4,'Electric','FourWheeler',0),(2,1,1,'Electric','FourWheeler',0),(2,1,2,'Electric','TwoWheeler',0),(2,1,3,'Fuel','TwoWheeler',0),(2,1,4,'Fuel','TwoWheeler',0),(2,2,1,'Electric','FourWheeler',0),(2,2,2,'Electric','TwoWheeler',0),(2,2,3,'Fuel','TwoWheeler',0),(2,2,4,'Fuel','TwoWheeler',0),(2,3,1,'Electric','FourWheeler',0),(2,3,2,'Electric','TwoWheeler',0),(2,3,3,'Fuel','TwoWheeler',0),(2,3,4,'Fuel','TwoWheeler',0),(2,4,1,'Electric','FourWheeler',0),(2,4,2,'Electric','FourWheeler',0),(2,4,3,'Fuel','TwoWheeler',0),(2,4,4,'Fuel','TwoWheeler',0),(2,5,1,'Electric','FourWheeler',0),(2,5,2,'Electric','FourWheeler',0),(2,5,3,'Fuel','TwoWheeler',0),(2,5,4,'Fuel','TwoWheeler',0),(3,1,1,'Fuel','TwoWheeler',1),(3,1,2,'Electric','FourWheeler',0),(3,1,3,'Fuel','TwoWheeler',0),(3,1,4,'Fuel','TwoWheeler',0),(3,2,1,'Fuel','TwoWheeler',0),(3,2,2,'Electric','FourWheeler',0),(3,2,3,'Fuel','TwoWheeler',0),(3,2,4,'Fuel','TwoWheeler',0),(3,3,1,'Fuel','TwoWheeler',0),(3,3,2,'Electric','FourWheeler',0),(3,3,3,'Fuel','TwoWheeler',0),(3,3,4,'Fuel','TwoWheeler',0),(3,4,1,'Fuel','TwoWheeler',0),(3,4,2,'Electric','FourWheeler',0),(3,4,3,'Fuel','TwoWheeler',0),(3,4,4,'Fuel','TwoWheeler',0),(3,5,1,'Fuel','TwoWheeler',0),(3,5,2,'Electric','FourWheeler',0),(3,5,3,'Fuel','TwoWheeler',0),(3,5,4,'Fuel','TwoWheeler',0),(4,1,1,'Fuel','TwoWheeler',0),(4,1,2,'Fuel','TwoWheeler',0),(4,1,3,'Fuel','TwoWheeler',0),(4,1,4,'Fuel','TwoWheeler',0),(4,2,1,'Fuel','TwoWheeler',0),(4,2,2,'Fuel','TwoWheeler',0),(4,2,3,'Fuel','TwoWheeler',0),(4,2,4,'Fuel','TwoWheeler',0),(4,3,1,'Fuel','TwoWheeler',0),(4,3,2,'Fuel','TwoWheeler',0),(4,3,3,'Fuel','TwoWheeler',0),(4,3,4,'Fuel','TwoWheeler',0),(4,4,1,'Fuel','TwoWheeler',0),(4,4,2,'Fuel','TwoWheeler',0),(4,4,3,'Fuel','FourWheeler',0),(4,4,4,'Fuel','FourWheeler',0),(4,5,1,'Fuel','TwoWheeler',0),(4,5,2,'Fuel','TwoWheeler',0),(4,5,3,'Fuel','FourWheeler',0),(4,5,4,'Fuel','FourWheeler',0),(5,1,1,'Fuel','FourWheeler',1),(5,1,2,'Fuel','TwoWheeler',0),(5,1,3,'Fuel','FourWheeler',0),(5,1,4,'Fuel','FourWheeler',0),(5,2,1,'Fuel','FourWheeler',1),(5,2,2,'Fuel','TwoWheeler',0),(5,2,3,'Fuel','FourWheeler',0),(5,2,4,'Fuel','FourWheeler',0),(5,3,1,'Fuel','FourWheeler',1),(5,3,2,'Fuel','TwoWheeler',0),(5,3,3,'Fuel','FourWheeler',0),(5,3,4,'Fuel','FourWheeler',0),(5,4,1,'Fuel','FourWheeler',1),(5,4,2,'Fuel','FourWheeler',0),(5,4,3,'Fuel','FourWheeler',0),(5,4,4,'Fuel','FourWheeler',0),(5,5,1,'Fuel','FourWheeler',1),(5,5,2,'Fuel','FourWheeler',0),(5,5,3,'Fuel','FourWheeler',0),(5,5,4,'Fuel','FourWheeler',0),(6,1,1,'Fuel','FourWheeler',1),(6,1,2,'Fuel','FourWheeler',0),(6,1,3,'Fuel','FourWheeler',0),(6,1,4,'Fuel','FourWheeler',0),(6,2,1,'Fuel','FourWheeler',1),(6,2,2,'Fuel','FourWheeler',0),(6,2,3,'Fuel','FourWheeler',0),(6,2,4,'Fuel','FourWheeler',0),(6,3,1,'Fuel','FourWheeler',1),(6,3,2,'Fuel','FourWheeler',0),(6,3,3,'Fuel','FourWheeler',0),(6,3,4,'Fuel','FourWheeler',0),(6,4,1,'Fuel','FourWheeler',0),(6,4,2,'Fuel','FourWheeler',0),(6,4,3,'Fuel','FourWheeler',0),(6,4,4,'Fuel','FourWheeler',0),(6,5,1,'Fuel','FourWheeler',0),(6,5,2,'Fuel','FourWheeler',0),(6,5,3,'Fuel','FourWheeler',0),(6,5,4,'Fuel','FourWheeler',0);
/*!40000 ALTER TABLE `parkingspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricing`
--

DROP TABLE IF EXISTS `pricing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pricing` (
  `vehicleCategory` varchar(50) NOT NULL,
  `vehicleType` varchar(50) NOT NULL,
  `hourlyRate` decimal(10,2) NOT NULL,
  `MinPrice` decimal(10,0) NOT NULL,
  PRIMARY KEY (`vehicleCategory`,`vehicleType`),
  CONSTRAINT `fk_pricing_vehicle` FOREIGN KEY (`vehicleCategory`, `vehicleType`) REFERENCES `vehicle` (`Category`, `Type`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pricing`
--

LOCK TABLES `pricing` WRITE;
/*!40000 ALTER TABLE `pricing` DISABLE KEYS */;
INSERT INTO `pricing` VALUES ('FourWheeler','Electric',45.00,20),('FourWheeler','Fuel',100.00,50),('TwoWheeler','Electric',25.00,10),('TwoWheeler','Fuel',35.00,15);
/*!40000 ALTER TABLE `pricing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staff` (
  `userID` int(255) unsigned NOT NULL,
  `salary` mediumint(255) NOT NULL,
  `shiftTime` time NOT NULL,
  `dateHired` date NOT NULL,
  `jobTitle` varchar(50) NOT NULL,
  PRIMARY KEY (`userID`),
  CONSTRAINT `fk_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (3,42000,'16:00:00','2023-03-12','Accountant'),(8,48000,'00:00:00','2023-05-20','Security Guard'),(11,20000,'08:00:00','2024-01-07','Accountant'),(12,55000,'16:00:00','2024-03-01','Cleaner'),(13,40000,'08:00:00','2024-01-05','Accountant'),(14,40000,'16:00:00','2024-01-05','Accountant'),(21,30000,'06:00:00','2026-01-12','Accountant'),(22,35000,'14:00:00','2026-01-06','Cleaner'),(26,50000,'06:00:00','2026-01-01','Manager'),(27,50000,'14:00:00','2026-01-02','Manager');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `floorNumber` int(255) NOT NULL,
  `TransactionID` mediumint(255) NOT NULL AUTO_INCREMENT,
  `CheckinTime` timestamp(6) NULL DEFAULT NULL,
  `CheckoutTime` timestamp(6) NULL DEFAULT NULL,
  `TotalFee` mediumint(255) DEFAULT NULL,
  `VehiclePlate` varchar(25) NOT NULL,
  `parkingRow` int(255) NOT NULL,
  `parkingColumn` int(255) NOT NULL,
  `driverID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`TransactionID`),
  KEY `fk_transaction_vehicle` (`VehiclePlate`),
  KEY `fk_transaction_space` (`floorNumber`,`parkingRow`,`parkingColumn`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,30,'2026-01-19 06:40:44.000000','2026-01-19 06:45:45.000000',15,'BA-1-PA-7862',3,2,1),(1,31,'2026-01-19 06:43:14.000000','2026-01-19 06:46:26.000000',50,'BA-2-PA-6550',6,4,1);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userID` int(255) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `phone` bigint(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) DEFAULT 'Customer',
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Pratiphal',9860454535,'helloworld','Customer'),(2,'Aashutosh',989999999,'fsidofskodnf','Customer'),(3,'Ram',9801234567,'ram_pass_2026','Staff'),(4,'Alice',9841556677,'alice_wonder','Customer'),(5,'Bob',9861223344,'bob_parking_88','Customer'),(6,'Hari',9811998877,'hari_nepal_123','Customer'),(7,'Binita',9803445566,'bini_power_01','Customer'),(8,'Gita',9841009988,'gita_work_55','Staff'),(9,'Nabin',9851001122,'nabin_nep_90','Customer'),(11,'Durga',9841000001,'pbkdf2:sha256:admin123','Staff'),(12,'Sita',9851012345,'sita_secure_99','Staff'),(13,'Mohan',9841223344,'pass13','Staff'),(14,'Geeta',9851334455,'pass14','Staff'),(15,'Anil',9801445566,'pass15','Customer'),(16,'Bikash',9811556677,'pass16','Customer'),(17,'Shiva',9861667788,'pass17','Customer'),(18,'Maya',9821778899,'pass18','Customer'),(19,'Rijan',9851142002,'hello','Customer'),(20,'Hari',9851142002,'hari@2002','Customer'),(21,'Richard',9811111111,'richard@1234','Staff'),(22,'Aastha',9823232323,'aasthac@123','Staff'),(26,'John',9867676767,'john_','Staff'),(27,'Sameer',9869420699,'sameer_manager','Staff'),(28,'Nehal',9821232123,'nehal@2123','Customer'),(30,'Nehal',9811111111,'nehal','Customer'),(31,'Harihar',9851142002,'harihar@2002','Customer');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehicle` (
  `numberPlate` varchar(25) NOT NULL,
  `Category` varchar(25) NOT NULL,
  `Type` varchar(25) NOT NULL,
  `DriverID` int(255) unsigned NOT NULL,
  PRIMARY KEY (`numberPlate`),
  KEY `idx_cat_type` (`Category`,`Type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES ('BA-1-KA-4040','FourWheeler','Fuel',7),('BA-1-KA-6767','FourWheeler','Fuel',19),('BA-1-PA-1010','FourWheeler','Fuel',7),('BA-1-PA-2123','TwoWheeler','Electric',28),('BA-1-PA-7862','TwoWheeler','Fuel',1),('BA-1-YA-7070','TwoWheeler','Fuel',10),('BA-2-CHA-2020','FourWheeler','Electric',5),('BA-2-LA-8080','FourWheeler','Fuel',11),('BA-2-PA-5050','TwoWheeler','Electric',8),('BA-2-PA-6550','FourWheeler','Fuel',1),('BA-3-CHA-6060','FourWheeler','Electric',9),('BA-3-LA-3030','TwoWheeler','Fuel',6),('BA-3-PA-9090','TwoWheeler','Fuel',12),('EV-1-ME-0001','FourWheeler','Electric',4),('EV-1-ME-1234','TwoWheeler','Electric',31);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-19 14:23:15
