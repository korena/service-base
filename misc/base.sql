-- Host: localhost
-- Generation Time: Feb 09, 2016 at 09:58 PM
-- Server version: 5.5.47-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `base`
--

-- --------------------------------------------------------

--
-- Table structure for table `android`
--

CREATE TABLE IF NOT EXISTS `android` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer` text NOT NULL,
  `model` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '2015-12-31 16:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `android`
--

INSERT INTO `android` (`id`, `customer`, `model`, `status`, `updated`, `created`) VALUES
(1, 'moaz korena', 'Android M', 0, '2016-01-18 16:00:14', '2016-01-18 16:00:14'),
(2, 'moaz korena', 'Android L', 0, '2016-01-18 16:05:20', '2016-01-18 16:05:20'),
(3, 'moaz korena', 'Android A', 0, '2016-01-18 16:26:21', '2016-01-18 16:26:21'),
(7, 'moaz korenado', 'Android T', 0, '2016-02-09 12:03:24', '2016-02-09 12:03:24');

-- --------------------------------------------------------

--
-- Table structure for table `iphone`
--

CREATE TABLE IF NOT EXISTS `iphone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer` text NOT NULL,
  `model` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '2015-12-31 16:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `iphone`
--

INSERT INTO `iphone` (`id`, `customer`, `model`, `status`, `updated`, `created`) VALUES
(1, 'moaz korena', 'iOS 1', 0, '2016-01-18 16:37:39', '2016-01-18 16:37:39'),
(2, 'moaz korena', 'iOS 7', 0, '2016-01-19 10:17:07', '2016-01-19 10:17:07'),
(3, 'moaz korena', 'iOS 6', 0, '2016-01-24 20:42:33', '2016-01-24 20:42:33'),
(4, 'moaz korena', 'iOS 5', 0, '2016-01-24 20:50:00', '2016-01-24 20:50:00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
