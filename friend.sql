/*
SQLyog v10.2 
MySQL - 8.0.36 : Database - friend
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`friend` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `friend`;

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
  `from_id` bigint NOT NULL COMMENT '发送消息id',
  `to_id` bigint DEFAULT NULL COMMENT '接收消息id',
  `text` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `chat_type` tinyint NOT NULL COMMENT '聊天类型 1-私聊 2-群聊',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `team_id` bigint DEFAULT NULL,
  `is_delete` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='聊天消息表';

/*Data for the table `chat` */

/*Table structure for table `friends` */

DROP TABLE IF EXISTS `friends`;

CREATE TABLE `friends` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '好友申请id',
  `from_id` bigint DEFAULT NULL COMMENT '发送申请的用户id',
  `receive_id` bigint DEFAULT NULL COMMENT '接收申请的用户id ',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读(0-未读 1-已读)',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除',
  `remark` varchar(214) DEFAULT NULL COMMENT '好友申请备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='好友申请管理表';

/*Data for the table `friends` */

insert  into `friends`(`id`,`from_id`,`receive_id`,`is_read`,`status`,`create_time`,`update_time`,`is_delete`,`remark`) values (2,59,63,0,1,'2024-06-19 15:50:21','2024-06-19 15:50:21',0,'nisi irure'),(3,59,61,0,3,'2024-06-19 15:50:54','2024-06-19 15:50:54',0,'nisi irure');

/*Table structure for table `team` */

DROP TABLE IF EXISTS `team`;

CREATE TABLE `team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(256) NOT NULL COMMENT '队伍名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `max_num` int NOT NULL DEFAULT '1' COMMENT '最大人数',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `status` int NOT NULL DEFAULT '0' COMMENT '0 - 公开，1 - 私有，2 - 加密',
  `password` varchar(512) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

/*Data for the table `team` */

insert  into `team`(`id`,`name`,`description`,`cover_image`,`max_num`,`expire_time`,`user_id`,`status`,`password`,`create_time`,`update_time`,`is_delete`) values (3,'东根真究群象','下己而即证油党平林影条就万。二率管铁事角那族科进我里称。为重江立口员商角没红处从必名。采后多海变头育级火气区记备料在较。需受群称置再感才现火称层话空。',NULL,76,NULL,59,2,'Lorem tempor cupidatat laboris est',NULL,'2024-06-18 21:02:33',NULL),(4,'万路内动写','两空布青引也压务角已信求身验事。路业自类南先情识气根片照月难真使。志江所己心些那道按派面运面质。你查系人水和容公角南口院历。统解非社求及包经了论形革程知历准。表们等素万员约团厂长组是。体局规车件教其也四种证织被速。',NULL,87,NULL,59,0,NULL,NULL,NULL,NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(45) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(512) NOT NULL,
  `profile` varchar(512) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `status` int DEFAULT '0',
  `role` int DEFAULT '0',
  `tage` varchar(512) DEFAULT NULL,
  `is_delete` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb3;

/*Data for the table `user` */

insert  into `user`(`id`,`phone`,`username`,`password`,`profile`,`gender`,`status`,`role`,`tage`,`is_delete`) values (30,'18176214185','方强','1023443bc26911d6fd25745899e17348',NULL,NULL,1,0,'[]',0),(32,'13442383022','乔霞','b80bb7740288fda1f201890375a60c8f',NULL,NULL,0,0,'[]',0),(33,'19831070496','邱磊','87d4eeb7dec7686410748d174c0e0a11',NULL,NULL,0,0,'[]',0),(58,'18133818057','武秀兰','57ec043012b6d8c0e384b9c1b8ba72a6','dolore quis','男',0,0,'[\"lol\",\"java\",\"c\"]',0),(59,'18167374667','乔敏','de9f5b3ccaca7013cc8ab5f3c82ace2d',NULL,NULL,0,1,'[]',0),(60,'18615461812','黎平','92bd3f7a44c8203ebb2a5dc6e8ff8294',NULL,NULL,0,0,'[]',0),(61,'19868721231','任霞','24042f4c74619b0f1992bbf07e9f3c5e',NULL,NULL,0,0,'[]',0),(62,'18675280753','蒋芳','2dbd5806b987acdc15f06b52da3fa5f4',NULL,NULL,0,0,'[]',0),(63,'18602363062','锺军','c4d4a6d631f8f61ba08c661e5306a987',NULL,NULL,0,0,'[]',0);

/*Table structure for table `user_team` */

DROP TABLE IF EXISTS `user_team`;

CREATE TABLE `user_team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `team_id` bigint DEFAULT NULL COMMENT '队伍id',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='用户队伍关系';

/*Data for the table `user_team` */

insert  into `user_team`(`id`,`user_id`,`team_id`,`join_time`,`create_time`,`update_time`,`is_delete`) values (1,59,4,'2024-06-18 20:06:29',NULL,NULL,NULL),(3,60,4,'2024-06-19 12:15:45',NULL,NULL,NULL),(6,62,3,'2024-06-19 12:45:07',NULL,NULL,NULL),(7,62,4,'2024-06-19 12:47:28',NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
