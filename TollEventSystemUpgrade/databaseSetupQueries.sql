-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 09, 2020 at 11:30 AM
-- Server version: 5.7.26
-- PHP Version: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `tollSystemUpgrade`
--

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(60) DEFAULT NULL,
  `customer_address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `customer_name`, `customer_address`) VALUES
(1, 'John', 'Saint Mary Street No 5, Ireland'),
(2, 'Mike', 'Connel Street No 7, Ireland'),
(3, 'Peter', 'West Side Street No 12, Ireland'),
(4, 'Frank', 'East side Street No 66, Ireland'),
(5, 'Obi', 'Nickel Street No 8, Ireland'),
(6, 'Eva', 'Saint Mary Street No 27, Ireland'),
(7, 'Mary', 'Saint Hope Street No 46, Ireland'),
(8, 'Yasmin', 'Sau Street No 139, Ireland');

-- --------------------------------------------------------

--
-- Table structure for table `customer_vehicle`
--

CREATE TABLE `customer_vehicle` (
  `customer_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customer_vehicle`
--

INSERT INTO `customer_vehicle` (`customer_id`, `vehicle_id`) VALUES
(1, 1),
(1, 2),
(2, 3),
(1, 4),
(3, 5),
(4, 6),
(5, 7),
(6, 8),
(7, 9),
(8, 10),
(1, 11),
(1, 12),
(1, 13),
(2, 14),
(2, 15),
(3, 16),
(3, 17),
(3, 18),
(7, 19),
(4, 20),
(4, 21),
(8, 22),
(1, 23),
(7, 24),
(5, 25),
(1, 26),
(8, 27),
(1, 28),
(7, 29),
(8, 30);

-- --------------------------------------------------------

--
-- Table structure for table `tollevents`
--

CREATE TABLE `tollevents` (
  `toll_id` int(11) NOT NULL,
  `toll_booth_id` varchar(20) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `processed` varchar(5) DEFAULT ' '
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tollevents`
--

INSERT INTO `tollevents` (`toll_id`, `toll_booth_id`, `vehicle_id`, `image_id`, `time`, `processed`) VALUES
(21, 'TB_M50', 8, 30402, '2020-02-14 11:15:30', ' '),
(22, 'TB_M31', 15, 30408, '2020-02-14 16:15:36', ' '),
(23, 'TB_M50', 3, 30410, '2020-02-14 23:15:38', ' '),
(24, 'TB_M32', 9, 30412, '2020-02-15 13:15:40', ' '),
(25, 'TB_M32', 11, 30414, '2020-02-15 13:15:42', ' '),
(26, 'TB_M53', 17, 30416, '2020-02-15 13:15:44', ' '),
(27, 'TB_M49', 20, 30418, '2020-02-15 13:15:46', ' '),
(28, 'TB_M33', 2, 30422, '2020-02-16 12:16:50', ' '),
(29, 'TB_M50', 17, 30424, '2020-02-16 12:16:52', ' '),
(31, 'TB_M33', 24, 30428, '2020-02-16 12:16:56', ' '),
(32, 'TB_M44', 26, 30430, '2020-02-16 12:16:58', ' '),
(33, 'TB_M50', 8, 30432, '2020-02-17 14:20:01', ' '),
(34, 'TB_M57', 14, 30434, '2020-02-17 17:20:03', ' '),
(35, 'TB_M50', 4, 30436, '2020-02-17 18:33:05', ' '),
(36, 'TB_M34', 5, 30438, '2020-02-17 19:20:07', ' '),
(37, 'TB_M50', 8, 30402, '2020-02-14 11:15:30', ' '),
(38, 'TB_M31', 15, 30408, '2020-02-14 16:15:36', ' '),
(39, 'TB_M50', 3, 30410, '2020-02-14 23:15:38', ' '),
(40, 'TB_M32', 9, 30412, '2020-02-15 13:15:40', ' '),
(41, 'TB_M32', 11, 30414, '2020-02-15 13:15:42', ' '),
(42, 'TB_M53', 17, 30416, '2020-02-15 13:15:44', ' '),
(43, 'TB_M49', 20, 30418, '2020-02-15 13:15:46', ' '),
(44, 'TB_M33', 2, 30422, '2020-02-16 12:16:50', ' '),
(45, 'TB_M50', 17, 30424, '2020-02-16 12:16:52', ' '),
(47, 'TB_M33', 24, 30428, '2020-02-16 12:16:56', ' '),
(48, 'TB_M44', 26, 30430, '2020-02-16 12:16:58', ' '),
(49, 'TB_M50', 8, 30432, '2020-02-17 14:20:01', ' '),
(50, 'TB_M57', 14, 30434, '2020-02-17 17:20:03', ' '),
(51, 'TB_M50', 4, 30436, '2020-02-17 18:33:05', ' '),
(52, 'TB_M34', 5, 30438, '2020-02-17 19:20:07', ' '),
(53, 'TB_M50', 8, 30402, '2020-02-14 11:15:30', ' '),
(54, 'TB_M31', 15, 30408, '2020-02-14 16:15:36', ' '),
(55, 'TB_M50', 3, 30410, '2020-02-14 23:15:38', ' '),
(56, 'TB_M32', 9, 30412, '2020-02-15 13:15:40', ' '),
(57, 'TB_M32', 11, 30414, '2020-02-15 13:15:42', ' '),
(58, 'TB_M53', 17, 30416, '2020-02-15 13:15:44', ' '),
(59, 'TB_M49', 20, 30418, '2020-02-15 13:15:46', ' '),
(60, 'TB_M33', 2, 30422, '2020-02-16 12:16:50', ' '),
(61, 'TB_M50', 17, 30424, '2020-02-16 12:16:52', ' '),
(63, 'TB_M33', 24, 30428, '2020-02-16 12:16:56', ' '),
(64, 'TB_M44', 26, 30430, '2020-02-16 12:16:58', ' '),
(65, 'TB_M50', 8, 30432, '2020-02-17 14:20:01', ' '),
(66, 'TB_M57', 14, 30434, '2020-02-17 17:20:03', ' '),
(67, 'TB_M50', 4, 30436, '2020-02-17 18:33:05', ' '),
(68, 'TB_M34', 5, 30438, '2020-02-17 19:20:07', ' ');

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `vehicle_id` int(11) NOT NULL,
  `vehicle_registration` varchar(20) DEFAULT NULL,
  `vehicle_type` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`vehicle_id`, `vehicle_registration`, `vehicle_type`) VALUES
(1, '151DL200', 'car'),
(2, '152DL345', 'van'),
(3, '161C3457', 'car'),
(4, '181MH3456', 'car'),
(5, '181MH3458', 'truck'),
(6, '181MH3459', 'truck'),
(7, '181MH3461', 'car'),
(8, '191LH1111', 'car'),
(9, '191LH1112', 'car'),
(10, '191LH1113', 'car'),
(11, '191LH1114', 'truck'),
(12, '192D33457', 'car'),
(13, '201CN3456', 'truck'),
(14, '201CN3457', 'van'),
(15, '201LH3025', 'van'),
(16, '201LH304', 'car'),
(17, '201LH305', 'van'),
(18, '201LH306', 'truck'),
(19, '201LH3064', 'car'),
(20, '201LH307', 'car'),
(21, '201LH307', 'car'),
(22, '201LH308', 'car'),
(23, '201LH3083', 'van'),
(24, '201LH309', 'car'),
(25, '201LH310', 'car'),
(26, '201LH311', 'truck'),
(27, '201LH312', 'van'),
(28, '201LH355', 'car'),
(29, '201LH777', 'truck'),
(30, '151MN666', 'van');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle_type_cost`
--

CREATE TABLE `vehicle_type_cost` (
  `vehicle_type` varchar(20) NOT NULL,
  `vehicle_cost` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vehicle_type_cost`
--

INSERT INTO `vehicle_type_cost` (`vehicle_type`, `vehicle_cost`) VALUES
('car', 4),
('truck', 8),
('van', 6);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `customer_vehicle`
--
ALTER TABLE `customer_vehicle`
  ADD PRIMARY KEY (`customer_id`,`vehicle_id`),
  ADD KEY `vehicle_id` (`vehicle_id`);

--
-- Indexes for table `tollevents`
--
ALTER TABLE `tollevents`
  ADD PRIMARY KEY (`toll_id`),
  ADD KEY `vehicle_id` (`vehicle_id`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`vehicle_id`),
  ADD KEY `vehicle_type` (`vehicle_type`);

--
-- Indexes for table `vehicle_type_cost`
--
ALTER TABLE `vehicle_type_cost`
  ADD PRIMARY KEY (`vehicle_type`,`vehicle_cost`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tollevents`
--
ALTER TABLE `tollevents`
  MODIFY `toll_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer_vehicle`
--
ALTER TABLE `customer_vehicle`
  ADD CONSTRAINT `customer_vehicle_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `customer_vehicle_ibfk_2` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`);

--
-- Constraints for table `tollevents`
--
ALTER TABLE `tollevents`
  ADD CONSTRAINT `tollevents_ibfk_1` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`);

--
-- Constraints for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`vehicle_type`) REFERENCES `vehicle_type_cost` (`vehicle_type`);
