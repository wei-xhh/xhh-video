/*
 Navicat Premium Data Transfer

 Source Server         : MariaDB5
 Source Server Type    : MariaDB
 Source Server Version : 50562
 Source Host           : localhost:3307
 Source Schema         : videos

 Target Server Type    : MariaDB
 Target Server Version : 50562
 File Encoding         : 65001

 Date: 12/04/2020 13:30:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bgm
-- ----------------------------
DROP TABLE IF EXISTS `bgm`;
CREATE TABLE `bgm`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of bgm
-- ----------------------------
INSERT INTO `bgm` VALUES ('1001', '冯曦妤', '我在那一角落患过伤风', '/bgm/shangfeng.aac');
INSERT INTO `bgm` VALUES ('1002', '广东雨神', '广东爱情故事', '/bgm/gdaqgs.m4r');
INSERT INTO `bgm` VALUES ('1003', '陈奕迅', '这一辈子有没有为谁拼过命', '/bgm/pm.m4r');
INSERT INTO `bgm` VALUES ('1004', '跟风超人', '大哥别杀我', '/bgm/dgbsw.m4r');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `father_comment_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `video_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频id',
  `from_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言者，评论的用户id',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课程评论表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES ('1', NULL, NULL, '', '', '', '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for search_records
-- ----------------------------
DROP TABLE IF EXISTS `search_records`;
CREATE TABLE `search_records`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '搜索的内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频搜索的记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of search_records
-- ----------------------------
INSERT INTO `search_records` VALUES ('1', '海贼王');
INSERT INTO `search_records` VALUES ('2', '海贼王');
INSERT INTO `search_records` VALUES ('3', 'chen');
INSERT INTO `search_records` VALUES ('4', 'chen');
INSERT INTO `search_records` VALUES ('5', '测试1');
INSERT INTO `search_records` VALUES ('6', '测试2');
INSERT INTO `search_records` VALUES ('7', '海贼王');
INSERT INTO `search_records` VALUES ('8', 'java');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `face_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '我的头像，如果没有默认给一张',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `fans_counts` int(11) NULL DEFAULT 0 COMMENT '我的粉丝数量',
  `follow_counts` int(11) NULL DEFAULT 0 COMMENT '我关注的人总数',
  `receive_like_counts` int(11) NULL DEFAULT 0 COMMENT '我接受到的赞美/收藏 的数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('200331BGZ8HKW1KP', 'xhh', '4QrcOUm6Wau+VuBX8g+IPg==', '/200331BGZ8HKW1KP/face/tmp_9531f2b97f31142f670f3529b21d40bf.jpg', 'xhh', 0, 0, 0);
INSERT INTO `users` VALUES ('200331C1NRK68ACH', 'chen', '4QrcOUm6Wau+VuBX8g+IPg==', '/200331C1NRK68ACH/face/wxffb5ac56a8cfd162.o6zAJs841N-orcEReKIvIRiDGres.bk6YjF3bWzgSb195e5ca503718a3cbef4d00fe0dd5e1.jpg', 'chen', 4, 1, 10);
INSERT INTO `users` VALUES ('200331F5PW5ZWG9P', 'lin', 'ICy5YqxZB1uWSwcVLSNLcA==', '/200331F5PW5ZWG9P/face/tmp_0aae23b947e75587925a7760e83ab10d.jpg', 'lin', 4, 1, 4);

-- ----------------------------
-- Table structure for users_fans
-- ----------------------------
DROP TABLE IF EXISTS `users_fans`;
CREATE TABLE `users_fans`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户',
  `fan_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '粉丝',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `fan_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户粉丝关联关系表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of users_fans
-- ----------------------------
INSERT INTO `users_fans` VALUES ('2004128HG18AKZF8', '200331C1NRK68ACH', '200331F5PW5ZWG9P');
INSERT INTO `users_fans` VALUES ('200411DWS8GAC46W', '200331F5PW5ZWG9P', '200331C1NRK68ACH');

-- ----------------------------
-- Table structure for users_like_videos
-- ----------------------------
DROP TABLE IF EXISTS `users_like_videos`;
CREATE TABLE `users_like_videos`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户',
  `video_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_video_rel`(`user_id`, `video_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户喜欢的/赞过的视频' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of users_like_videos
-- ----------------------------
INSERT INTO `users_like_videos` VALUES ('2004128C4APP7S3C', '200331C1NRK68ACH', '2004089GDDKHKYW0');
INSERT INTO `users_like_videos` VALUES ('200410A1B8PGN168', '200331C1NRK68ACH', '2004089P1BMFCSFW');
INSERT INTO `users_like_videos` VALUES ('200410A15DZ6530H', '200331C1NRK68ACH', '200408A5Z818HPX4');
INSERT INTO `users_like_videos` VALUES ('200410A5Y33P8MY8', '200331F5PW5ZWG9P', '200407FBHRSS8S5P');
INSERT INTO `users_like_videos` VALUES ('200410A5WAXT4754', '200331F5PW5ZWG9P', '200407FCN5AH3T2W');

-- ----------------------------
-- Table structure for users_report
-- ----------------------------
DROP TABLE IF EXISTS `users_report`;
CREATE TABLE `users_report`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `deal_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被举报用户id',
  `deal_video_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型标题，让用户选择，详情见 枚举',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `userid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报人的id',
  `create_date` datetime(0) NOT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '举报用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for videos
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布者id',
  `audio_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户使用音频的信息',
  `video_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频描述',
  `video_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频存放的路径',
  `video_seconds` float(6, 2) NULL DEFAULT NULL COMMENT '视频秒数',
  `video_width` int(6) NULL DEFAULT NULL COMMENT '视频宽度',
  `video_height` int(6) NULL DEFAULT NULL COMMENT '视频高度',
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频封面图',
  `like_counts` bigint(20) NOT NULL DEFAULT 0 COMMENT '喜欢/赞美的数量',
  `status` int(1) NOT NULL COMMENT '视频状态：\r\n1、发布成功\r\n2、禁止播放，管理员操作',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '视频信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of videos
-- ----------------------------
INSERT INTO `videos` VALUES ('200405C3DSY5W84H', '200331F5PW5ZWG9P', '1003', 'lin测试', '/200331F5PW5ZWG9P/video/a436985a-9e02-4e0d-a3ae-1e4a0a2054a6.mp4', 15.00, 540, 960, '/200331F5PW5ZWG9P/video/tmp_c5a393ac2f7bdb73ad1ebed3a5f70099.jpg', 0, 1, '2020-04-05 16:59:19');
INSERT INTO `videos` VALUES ('200405C4M79RDS80', '200331F5PW5ZWG9P', '1002', 'lin测试2', '/200331F5PW5ZWG9P/video/4fbf9463-7ecc-4185-a3ee-c0af2b25f7e2.mp4', 7.00, 544, 960, '/200331F5PW5ZWG9P/video/tmp_dfc7b88eb4da773fddc1876b0b08f8b6.jpg', 0, 1, '2020-04-05 17:02:48');
INSERT INTO `videos` VALUES ('200405C4WG26DM14', '200331F5PW5ZWG9P', '1001', 'lin测试3', '/200331F5PW5ZWG9P/video/be3f5f1e-d9cb-464d-88b4-5dcdb6409846.mp4', 6.00, 540, 960, '/200331F5PW5ZWG9P/video/tmp_fea5afa67ec03aa840385c9c6abd8507.jpg', 0, 1, '2020-04-05 17:03:28');
INSERT INTO `videos` VALUES ('200405C59A53S614', '200331C1NRK68ACH', '1004', 'chen测试1', '/200331C1NRK68ACH/video/aa0ebe75-8b5b-4a11-aeed-709892465371.mp4', 9.00, 544, 960, '/200331C1NRK68ACH/video/tmp_3ac0e8757c80994fd98313a76a6bf053.jpg', 0, 1, '2020-04-05 17:04:51');
INSERT INTO `videos` VALUES ('20040689HAT7P0M8', '200331C1NRK68ACH', '1002', 'chen测试2', '/200331C1NRK68ACH/video/40ce9bc9-43ab-4ec5-8363-73a879c15982.mp4', 6.00, 540, 960, '/200331C1NRK68ACH/video/tmp_1f87f1363510294f82c196a59c980c51.jpg', 0, 1, '2020-04-06 11:41:20');
INSERT INTO `videos` VALUES ('20040689YAFD16Y8', '200331C1NRK68ACH', '1003', 'chen测试3', '/200331C1NRK68ACH/video/889148cc-110a-450e-b444-3caced164452.mp4', 50.00, 544, 960, '/200331C1NRK68ACH/video/tmp_eaa83824aea6f53681a742497245cc8f.jpg', 0, 1, '2020-04-06 11:42:24');
INSERT INTO `videos` VALUES ('20040698RX55Y32W', '200331C1NRK68ACH', '1003', '海贼王', '/200331C1NRK68ACH/video/3c1d2a6a-940e-44ce-8b92-76b6993ea055.mp4', 46.00, 544, 960, '/200331C1NRK68ACH/video/tmp_9e653b9587dda070a00c00d4ac8636fc.jpg', 0, 1, '2020-04-06 13:02:59');
INSERT INTO `videos` VALUES ('2004069A5HGC1N0H', '200331BGZ8HKW1KP', '1002', '海贼王催泪', '/200331BGZ8HKW1KP/video/18715ce0-39ef-44f7-b3ad-2cc01e069fb5.mp4', 47.00, 544, 960, '/200331BGZ8HKW1KP/video/tmp_cbc330c89204234ad479e2d750f933c7.jpg', 0, 1, '2020-04-06 13:07:15');
INSERT INTO `videos` VALUES ('2004069CDT3DR214', '200331BGZ8HKW1KP', '1003', '海贼王艾斯', '/200331BGZ8HKW1KP/video/106b1abc-41a1-4672-8d01-3f1f24776854.mp4', 47.00, 544, 960, '/200331BGZ8HKW1KP/video/tmp_7a7f446e94d6a959061214ec4422a42d.jpg', 0, 1, '2020-04-06 13:14:08');
INSERT INTO `videos` VALUES ('200407FBHRSS8S5P', '200331C1NRK68ACH', '1001', 'chen哈哈', '/200331C1NRK68ACH/video/012f94e6-4dd3-4f58-acf8-48a1e1661208.mp4', 4.43, 540, 960, '/200331C1NRK68ACH/video/wxffb5ac56a8cfd162.jpg', 1, 1, '2020-04-07 20:11:48');
INSERT INTO `videos` VALUES ('200407FCN5AH3T2W', '200331F5PW5ZWG9P', '1001', 'lin地铁', '/200331F5PW5ZWG9P/video/c65c38ee-028d-4452-9ed9-334ef8eb5277.mp4', 3.02, 540, 960, '/200331F5PW5ZWG9P/video/wxffb5ac56a8cfd162.jpg', 1, 1, '2020-04-07 20:15:04');
INSERT INTO `videos` VALUES ('2004089GDDKHKYW0', '200331C1NRK68ACH', '1001', 'chen测试6', '/200331C1NRK68ACH/video/d9aecfb6-44c1-4c69-8d14-11e96a8851cd.mp4', 5.00, 960, 544, '/200331C1NRK68ACH/video/tmp_c0cc5649f7f558c98738cfb0b0e70f7d.jpg', 1, 1, '2020-04-08 13:23:06');
INSERT INTO `videos` VALUES ('2004089P1BMFCSFW', '200331C1NRK68ACH', '1002', 'Android', '/200331C1NRK68ACH/video/cdfce778-28e7-4a3f-bdab-f305a2116f59.mp4', 3.00, 540, 960, '/200331C1NRK68ACH/video/tmp_a29d4e5b04237ccbfacfacc74701856d.jpg', 2, 1, '2020-04-08 13:36:49');
INSERT INTO `videos` VALUES ('2004089PNSDZ0MK4', '200331C1NRK68ACH', '1001', '少年宫', '/200331C1NRK68ACH/video/ce2630cf-3b23-42a6-b4ad-9310020dee34.mp4', 5.00, 540, 960, '/200331C1NRK68ACH/video/tmp_31894ee347bdcf6cfd8953d0b6d47485.jpg', 0, 1, '2020-04-08 13:38:48');
INSERT INTO `videos` VALUES ('200408A5Z818HPX4', '200331F5PW5ZWG9P', '1001', '少年宫2', '/200331F5PW5ZWG9P/video/38083aa9-8643-4442-8ef6-ac732e4efbf6.mp4', 5.00, 960, 544, '/200331F5PW5ZWG9P/video/tmp_5ac032a54e852f77f1e965b11700c651.jpg', 3, 1, '2020-04-08 14:18:38');

SET FOREIGN_KEY_CHECKS = 1;
