-- MySQL dump 10.13  Distrib 5.7.17, for Linux (i686)
--
-- Host: localhost    Database: blogsystem
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.2

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

--原来是嵌入到博客系统中
--drop database blogsystem if exists "blogsystem"; --直接删除数据库，不提醒
--create database blogsystem; --创建数据库 
use blogsystem; --选择数据库

--
-- Table structure for table `bll_crawltask`
--

DROP TABLE IF EXISTS `bll_crawltask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bll_crawltask` (
  `ID` varchar(60) NOT NULL,
  `CreateBy` varchar(45) DEFAULT NULL,
  `CrawlURL` varchar(100) DEFAULT NULL,
  `KeyWords` varchar(100) DEFAULT NULL,
  `CreateTime` datetime DEFAULT NULL,
  `State` int(11) DEFAULT NULL COMMENT '0：创建；1：执行中；2：执行成功；3：执行失败。',
  `FinishTime` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bll_pageinfo`
--

DROP TABLE IF EXISTS `bll_pageinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bll_pageinfo` (
  `ID` varchar(60) NOT NULL,
  `URL` varchar(100) DEFAULT NULL,
  `Title` varchar(100) DEFAULT NULL,
  `PostTime` varchar(60) DEFAULT NULL,
  `Content` text,
  `Author` varchar(45) DEFAULT NULL,
  `AuthorPage` varchar(60) DEFAULT NULL,
  `CreateTime` datetime DEFAULT NULL,
  `CreateBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dump completed on 2017-04-26 21:12:05
