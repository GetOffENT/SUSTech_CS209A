CREATE DATABASE IF NOT EXISTS `linkgame` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `linkgame`;

-- --------------------------------------------------------
# user

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `username` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `nickname` varchar(255) NOT NULL,
    `avatar`   varchar(255) NOT NULL DEFAULT 'https://sky-www9989.oss-cn-shenzhen.aliyuncs.com/wukong.jpg',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`
(
    `id`             int(11)  NOT NULL AUTO_INCREMENT,
    `user_id`        int(11)  NOT NULL,
    `opponent_id`    int(11)  NOT NULL,
    `score`          int(11)  NOT NULL,
    `opponent_score` int(11)  NOT NULL,
    `create_at`      datetime NOT NULL,
    `update_at`      datetime NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
