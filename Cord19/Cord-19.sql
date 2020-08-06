-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 06, 2020 at 06:07 AM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.2.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Cord-19`
--

-- --------------------------------------------------------

--
-- Table structure for table `Article`
--

CREATE TABLE `Article` (
  `cordID` varchar(255) NOT NULL,
  `sha` text NOT NULL,
  `source` text NOT NULL,
  `title` text NOT NULL,
  `abstract` longtext NOT NULL,
  `publishTime` text NOT NULL,
  `authors` text NOT NULL,
  `journal` text NOT NULL,
  `pathToPDF` text NOT NULL,
  `pathToPMC` text NOT NULL,
  `URL` text NOT NULL,
  `corpusID` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `ArticleToFullArticle`
--

CREATE TABLE `ArticleToFullArticle` (
  `id` int(11) NOT NULL,
  `cordID` varchar(255) DEFAULT NULL,
  `paperID` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `FullArticle`
--

CREATE TABLE `FullArticle` (
  `paperID` varchar(255) NOT NULL,
  `numberOfRef` int(11) NOT NULL,
  `numberOfBib` int(11) NOT NULL,
  `hasText` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Article`
--
ALTER TABLE `Article`
  ADD PRIMARY KEY (`cordID`);

--
-- Indexes for table `ArticleToFullArticle`
--
ALTER TABLE `ArticleToFullArticle`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cordID` (`cordID`),
  ADD KEY `paperID` (`paperID`);

--
-- Indexes for table `FullArticle`
--
ALTER TABLE `FullArticle`
  ADD PRIMARY KEY (`paperID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ArticleToFullArticle`
--
ALTER TABLE `ArticleToFullArticle`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ArticleToFullArticle`
--
ALTER TABLE `ArticleToFullArticle`
  ADD CONSTRAINT `articletofullarticle_ibfk_1` FOREIGN KEY (`cordID`) REFERENCES `Article` (`cordID`),
  ADD CONSTRAINT `articletofullarticle_ibfk_2` FOREIGN KEY (`paperID`) REFERENCES `FullArticle` (`paperID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
