-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: toilet
-- ------------------------------------------------------
-- Server version	5.7.20-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `comment` text,
  `time` timestamp NULL DEFAULT NULL,
  `voteTotal` int(11) DEFAULT NULL,
  `flagCount` int(11) DEFAULT NULL,
  `building` varchar(3) DEFAULT NULL,
  `floor` int(11) DEFAULT NULL,
  `locationDetailText` text,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,6,4,'chris ayoubbbbb','2017-10-29 09:27:22',-1,0,'GDC',5,NULL),(2,6,2,'','2017-10-29 09:31:45',-1,0,'GDC',5,NULL),(3,6,2,'','2017-10-29 09:33:31',-1,0,'GDC',5,NULL),(4,6,2,'','2017-10-29 09:45:11',-1,0,'GDC',5,NULL),(5,6,4,'as\nasd','2017-10-29 09:55:10',-1,0,'ADH',2,NULL),(6,6,5,'This is a test post!','2017-10-29 11:19:07',-1,0,'ADH',4,NULL),(7,6,2,'asdasd','2017-10-29 11:21:50',-1,0,'ECJ',2,NULL),(8,6,2,'','2017-10-29 11:37:10',0,0,'ECG',2,NULL),(9,6,2,'','2017-10-29 11:40:31',-1,0,'GRE',2,NULL),(10,6,1,'','2017-10-29 11:42:06',1,0,'RLM',9,NULL),(11,6,5,'hell ya','2017-10-29 14:46:26',-1,0,'GDC',7,NULL),(12,6,4,'','2017-10-29 14:50:14',-1,0,'GDC',5,NULL),(13,6,3,'This is a test post!','2017-10-29 16:10:54',-1,0,'EER',2,NULL),(14,6,3,'I love HackTX!','2017-10-29 18:03:33',-1,0,'RLM',18,NULL),(15,6,4,'','2017-10-29 18:07:16',0,0,'GDC',5,NULL),(16,6,4,'','2017-10-29 18:10:27',-1,0,'RLM',4,NULL),(17,6,4,'It was grest!','2017-10-29 18:12:41',-1,0,'GDC',5,NULL),(18,6,4,'','2017-10-29 18:17:43',0,0,'RLM',5,NULL),(19,6,4,'','2017-10-29 18:27:49',-1,0,'RLM',4,NULL),(20,6,3,'','2017-10-29 18:41:49',0,0,'RLM',6,NULL),(21,6,5,'','2017-10-29 18:43:10',-1,0,'GDC',4,NULL),(22,6,3,'Woo HackTX!','2017-10-29 18:44:56',-1,0,'MRH',4,NULL),(23,6,3,'Note!','2017-10-29 18:57:34',0,0,'RLM',4,NULL),(24,6,1,'so bad\n\n\n\n','2017-10-29 19:01:42',-1,0,'JES',6,NULL),(25,6,5,'','2017-10-29 19:06:40',0,0,'GDC',5,NULL),(26,6,5,'jkljlkj\n\n','2017-10-29 19:11:39',0,0,'GDC',3,NULL),(27,6,3,'sucks\n','2017-10-29 19:13:57',0,0,'RLM',17,NULL),(28,6,5,'it was great','2017-10-29 19:21:25',-1,0,'GDC',4,NULL),(29,6,4,'','2017-10-29 19:28:04',0,0,'RLM',14,NULL),(30,6,5,'very clen','2017-10-29 19:31:19',0,0,'RLM',5,NULL),(31,6,4,'Yi','2017-10-29 19:34:18',0,0,'GDC',3,NULL),(32,6,1,'notp','2017-10-29 19:38:16',1,0,'GDC',1,NULL),(33,6,5,'Awesome!!!!','2017-10-29 20:10:49',0,0,'GDC',4,NULL);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1),(2),(3),(4),(5),(6),(7);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `postId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `vote` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `postId` (`postId`),
  KEY `userId` (`userId`),
  CONSTRAINT `votes_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `posts` (`id`),
  CONSTRAINT `votes_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votes`
--

LOCK TABLES `votes` WRITE;
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
INSERT INTO `votes` VALUES (1,1,6,-1),(2,2,6,-1),(3,3,6,-1),(4,4,6,-1),(5,10,6,1),(6,5,6,-1),(7,7,6,-1),(8,11,6,-1),(9,13,6,-1),(10,15,6,0),(11,16,6,-1),(12,19,6,-1),(13,21,6,-1),(14,22,6,-1),(15,14,6,-1),(16,24,6,-1),(17,28,6,-1),(18,6,6,-1),(19,9,6,-1),(20,12,6,-1),(21,32,6,1),(22,17,6,-1);
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-13  5:51:30
