-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 15, 2024 at 10:17 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12
-- 
-- MERGED VERSION: Includes original schema with updates applied

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spk_pemilihan_motor_matic`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` char(3) NOT NULL,
  `namalengkap` varchar(20) DEFAULT NULL,
  `user` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `namalengkap`, `user`, `password`) VALUES
('001', 'Neubri Hidayah', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `data_motor`
-- Refactored to only include necessary columns
--

CREATE TABLE `data_motor` (
  `id_motor` varchar(5) NOT NULL,
  `nama_motor` varchar(50) NOT NULL,
  `merek` varchar(30) NOT NULL,
  `tahun_produksi` varchar(10) NOT NULL,
  `warna_motor` varchar(20) NOT NULL,
  `kategori_harga` varchar(30) NOT NULL,
  `kategori_cc` varchar(30) NOT NULL,
  `kategori_irit` varchar(30) NOT NULL,
  `kategori_desain` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_motor`
-- Refactored to only include necessary columns
--

INSERT INTO `data_motor` (`id_motor`, `nama_motor`, `merek`, `tahun_produksi`, `warna_motor`, `kategori_harga`, `kategori_cc`, `kategori_irit`, `kategori_desain`) VALUES
('M001', 'Yamaha Gear 125', 'Yamaha', '2023', 'Merah', 'EKONOMIS ≤ 22 juta', 'Kecil (Entry) 110 – 125 cc', 'Irit ≥ 50 km/l', 'Sporty/Agresif'),
('M002', 'Yamaha Fazzio Hybrid', 'Yamaha', '2024', 'Putih', 'MENENGAH 22 – 35 juta', 'Kecil (Entry) 110 – 125 cc', 'Irit ≥ 50 km/l', 'Retro/Stylish'),
('M003', 'Yamaha XMAX Connected', 'Yamaha', '2024', 'Biru', 'PREMIUM > 35 juta', 'Besar (Premium) > 160 cc', 'Sedang 40–49 km/l', 'Futuristik/Modern'),
('M004', 'Honda Vario 160 CBS', 'Honda', '2023', 'Hitam', 'MENENGAH 22 – 35 juta', 'Sedang (Mid-range) 150 – 160 cc', 'Irit ≥ 50 km/l', 'Sporty/Agresif'),
('M005', 'Honda PCX 160 CBS', 'Honda', '2024', 'Silver', 'MENENGAH 22 – 35 juta', 'Sedang (Mid-range) 150 – 160 cc', 'Irit ≥ 50 km/l', 'Futuristik/Modern'),
('M006', 'Honda ADV 160 CBS', 'Honda', '2023', 'Hitam', 'PREMIUM > 35 juta', 'Sedang (Mid-range) 150 – 160 cc', 'Irit ≥ 50 km/l', 'Futuristik/Modern');

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `kd_kriteria` char(3) NOT NULL,
  `nama_kriteria` varchar(30) NOT NULL,
  `prioritas_kepentingan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`kd_kriteria`, `nama_kriteria`, `prioritas_kepentingan`) VALUES
('K1', 'Harga', 'Sangat Penting ke-1'),
('K2', 'Kapasitas Mesin', 'Penting ke-2'),
('K3', 'Irit Bahan Bakar', 'Cukup Penting ke-3'),
('K4', 'Desain', 'Biasa ke-4');

-- --------------------------------------------------------

--
-- Table structure for table `register`
--

CREATE TABLE `register` (
  `id` int(3) NOT NULL,
  `email` varchar(50) NOT NULL,
  `user` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `register`
--

INSERT INTO `register` (`id`, `email`, `user`, `password`) VALUES
(1, 'mail@gmail.com', 'admin', 'admin'),
(2, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `seleksi`
--

CREATE TABLE `seleksi` (
  `id_motor` char(5) NOT NULL,
  `nama_motor` varchar(50) NOT NULL,
  `merek` varchar(30) NOT NULL,
  `hasil_penilaian` decimal(6,3) NOT NULL,
  `ranking` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seleksi`
--


-- --------------------------------------------------------

--
-- Table structure for table `sub_kriteria`
--

CREATE TABLE `sub_kriteria` (
  `no_sub` int(3) NOT NULL,
  `kd_kriteria` char(3) NOT NULL,
  `nama_kriteria` varchar(100) NOT NULL,
  `nama_sub_kriteria` varchar(100) NOT NULL,
  `prioritas_kepentingan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sub_kriteria`
--

INSERT INTO `sub_kriteria` (`no_sub`, `kd_kriteria`, `nama_kriteria`, `nama_sub_kriteria`, `prioritas_kepentingan`) VALUES
(1, 'K1', 'Harga', 'EKONOMIS ≤ 22 juta', 'Sangat Penting ke-1'),
(2, 'K1', 'Harga', 'MENENGAH 22 – 35 juta', 'Penting ke-2'),
(3, 'K1', 'Harga', 'PREMIUM > 35 juta', 'Cukup Penting ke-3'),
(4, 'K2', 'Kapasitas Mesin', 'Kecil (Entry) 110 – 125 cc', 'Sangat Penting ke-1'),
(5, 'K2', 'Kapasitas Mesin', 'Sedang (Mid-range) 150 – 160 cc', 'Penting ke-2'),
(6, 'K2', 'Kapasitas Mesin', 'Besar (Premium) > 160 cc', 'Cukup Penting ke-3'),
(7, 'K3', 'Irit Bahan Bakar', 'Irit ≥ 50 km/l', 'Sangat Penting ke-1'),
(8, 'K3', 'Irit Bahan Bakar', 'Sedang 40–49 km/l', 'Penting ke-2'),
(9, 'K3', 'Irit Bahan Bakar', 'Boros < 40 km/l', 'Cukup Penting ke-3'),
(10, 'K4', 'Desain', 'Sporty/Agresif', 'Sangat Penting ke-1'),
(11, 'K4', 'Desain', 'Retro/Stylish', 'Penting ke-2'),
(12, 'K4', 'Desain', 'Futuristik/Modern', 'Cukup Penting ke-3');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `data_motor`
--
ALTER TABLE `data_motor`
  ADD PRIMARY KEY (`id_motor`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kd_kriteria`);

--
-- Indexes for table `register`
--
ALTER TABLE `register`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `seleksi`
--
ALTER TABLE `seleksi`
  ADD UNIQUE KEY `id_motor` (`id_motor`);

--
-- Indexes for table `sub_kriteria`
--
ALTER TABLE `sub_kriteria`
  ADD PRIMARY KEY (`no_sub`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `register`
--
ALTER TABLE `register`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
