-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 08 Paź 2016, 21:04
-- Wersja serwera: 10.1.9-MariaDB
-- Wersja PHP: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `notes`
--
CREATE DATABASE IF NOT EXISTS `notes` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;
USE `notes`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `message`
--

CREATE TABLE `message` (
  `message_id` int(11) NOT NULL,
  `text` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `owner` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `message`
--

INSERT INTO `message` (`message_id`, `text`, `owner`) VALUES
(3, 'Siemka ćśźżąóń', 'user1'),
(4, 'Co tam słychać mordy?', 'user2'),
(5, 'adssadsadsaddasdas 23421', 'user2'),
(6, 'Wy też tutaj?', 'user3');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `message_users`
--

CREATE TABLE `message_users` (
  `Message_message_id` int(11) NOT NULL,
  `editors_login` varchar(255) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `message_users`
--

INSERT INTO `message_users` (`Message_message_id`, `editors_login`) VALUES
(3, 'user3'),
(5, 'user1'),
(6, 'user1'),
(6, 'user2');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `login` varchar(255) COLLATE utf8_polish_ci NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`login`, `last_login`, `name`, `password`) VALUES
('user1', '2016-10-08 20:39:54', 'Grzegorz Brzęczyszczykiewicz', 'user1'),
('user2', '2016-10-08 12:48:25', 'Zbigniew Stonoga', 'user2'),
('user3', '2016-10-08 12:49:16', 'Zbychu Zbychowski', 'user3');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `FK_message_owner` (`owner`);

--
-- Indexes for table `message_users`
--
ALTER TABLE `message_users`
  ADD PRIMARY KEY (`Message_message_id`,`editors_login`),
  ADD KEY `FK_message_users_editors_login` (`editors_login`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`login`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `message`
--
ALTER TABLE `message`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK_message_owner` FOREIGN KEY (`owner`) REFERENCES `users` (`login`);

--
-- Ograniczenia dla tabeli `message_users`
--
ALTER TABLE `message_users`
  ADD CONSTRAINT `FK_message_users_Message_message_id` FOREIGN KEY (`Message_message_id`) REFERENCES `message` (`message_id`),
  ADD CONSTRAINT `FK_message_users_editors_login` FOREIGN KEY (`editors_login`) REFERENCES `users` (`login`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
