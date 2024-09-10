
SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
drop table if exists user;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`         int          NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `username`   varchar(255) NULL DEFAULT NULL COMMENT '用户名',
    `password`   varchar(255) NULL DEFAULT NULL COMMENT '密码',
    `name`       varchar(255) NULL DEFAULT NULL COMMENT '姓名',
    `avatar`     varchar(255) NULL DEFAULT NULL COMMENT '头像',
    `role`       varchar(255) NULL DEFAULT NULL COMMENT '角色：ADMIN:系统管理员，MANAGER：店长；TECHNICIAN: 技师；CUSTOMER：顾客',
    `phone`      varchar(255) NULL DEFAULT NULL COMMENT '电话',
    `email`      varchar(255) NULL DEFAULT NULL COMMENT '邮箱',
    `salon_id`   int          NULL DEFAULT NULL COMMENT '店铺ID',
    `created_at` datetime          DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表';

INSERT INTO `user`
VALUES (1, 'admin', '123456', '超级管理员', '1715488042681', 'ADMIN', '1234234324', 'admin@nau.com', 1,now(),now());
INSERT INTO `user`
VALUES (2, 'manager1', '123456', '小王店长', '1715480997156', 'MANAGER', '15212654654', 'admin1@nau.com', 1,now(),now());
INSERT INTO `user`
VALUES (3, 'manager2', '123456', '李子店长', '1715480997156', 'MANAGER', '15212654655', 'admin1@nau.com', 2,now(),now());
INSERT INTO `user`
VALUES (4, 'tech1', '123456', '技师-小草', '1715663517309', 'TECHNICIAN', '13236954655', 'admin2@nau.com', 1,now(),now());
INSERT INTO `user`
VALUES (5, 'tech2', '123456', '技师-艾青', '1715663517319', 'TECHNICIAN', '13236954656', 'admin2@nau.com', 1,now(),now());
INSERT INTO `user`
VALUES (6, 'tech3', '123456', '技师-文青', '1715663517329', 'TECHNICIAN', '13236954657', 'admin2@nau.com', 1,now(),now());
INSERT INTO `user`
VALUES (7, 'tech4', '123456', '技师-玫瑰', '1715663517309', 'TECHNICIAN', '13236954658', 'admin2@nau.com', 2,now(),now());
INSERT INTO `user`
VALUES (8, 'tech5', '123456', '技师-百合', '1715663517319', 'TECHNICIAN', '13236954659', 'admin2@nau.com', 2,now(),now());
INSERT INTO `user`
VALUES (9, 'tech6', '123456', '技师-水仙', '1715663517329', 'TECHNICIAN', '13236954665', 'admin2@nau.com', 2,now(),now());
INSERT INTO `user`
VALUES (12, 'user1', '123456', '顾客1', '1715663517329', 'USER', '13236954665', 'user1@nau.com', null,now(),now());
INSERT INTO `user`
VALUES (13, 'user2', '123456', '顾客2', '1715663517329', 'USER', '13236954665', 'user1@nau.com', null,now(),now());
INSERT INTO `user`
VALUES (14, 'user3', '123456', '顾客3', '1715663517329', 'USER', '13236954665', 'user1@nau.com', null,now(),now());

-- 店铺
drop table if exists salon;
CREATE TABLE salon
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL comment '店铺名称',
    manager_id INT           NULL comment '店长ID',
    address    VARCHAR(255) NOT NULL comment '店铺地址',
    phone      VARCHAR(20)   NULL comment '店铺电话',
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
insert into salon (name, manager_id, address, phone)
values ('上海-长宁龙之梦店', 1, '上海市长宁区1018号5楼203号', '021-12345678');
insert into salon (name, manager_id, address, phone)
values ('上海-静安大融城店', 2, '上海市静安区803号6楼203号', '021-12345679');

-- 服务表
drop table if exists service;
CREATE TABLE service
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    category      varchar(255) comment '服务分类',
    remark        VARCHAR(255) comment '简短卖点',
    time          int comment '服务时长，单位：分钟',
    description   TEXT comment '服务描述',
    pic           VARCHAR(1024) comment '服务图片地址',
    pic_introduce VARCHAR(1024) comment '服务详情介绍图片地址',
    created_at    datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into service (name, category, remark, time, description, pic, pic_introduce)
values ('珍珠养发', '养发SPA', '发丝润亮泽', 30, '', 'url', 'url_detail');
insert into service (name, category, remark, time, description, pic, pic_introduce)
values ('活性肽滋养', '养发SPA', '科技增效，育发新高度', 30, '', 'url', 'url_detail');
insert into service (name, category, remark, time, description, pic, pic_introduce)
values ('新玫瑰养发', '润养健发', '柔顺滑养回来', 30, '', 'url', 'url_detail');

-- 预约表
drop table if exists appointment;
CREATE TABLE appointment
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT          NOT NULL comment '顾客ID',
    user_name        VARCHAR(255) comment '顾客ID',
    technician_id    INT          NOT NULL comment '顾客ID',
    technician_name  VARCHAR(255) comment '顾客ID',
    service_id       INT          NOT NULL comment '服务ID',
    service_name     VARCHAR(255) NULL comment '服务名称',
    salon_id         INT          NOT NULL comment '店铺ID',
    salon_name       VARCHAR(255) NULL comment '店铺名称',
    appointment_time DATETIME     NOT NULL,
    time             INT          NOT NULL comment '服务时长，单位：分钟',
    finish_time      DATETIME     NOT NULL comment '服务结束时间',
    status           VARCHAR(45)  not null comment '预约状态：  PENDING:待审核；APPROVED:预约成功；REJECTED:已拒绝；CANCELED:已取消；COMPLETED:已完成',
    created_at       datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at       datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
insert into appointment (user_id, user_name, technician_id, technician_name, service_id, service_name, salon_id, salon_name, appointment_time, time, finish_time, status)
values (12, '顾客1', 4, '技师-小草', 1, '珍珠养发', 1, '上海-长宁龙之梦店', '2024-09-13 09:30:00', 30, '2024-09-13 10:00:00', 'PENDING');
insert into appointment (user_id, user_name, technician_id, technician_name, service_id, service_name, salon_id, salon_name, appointment_time, time, finish_time, status)
values (13, '顾客1', 5, '技师-艾青', 1, '珍珠养发', 1, '上海-长宁龙之梦店', '2024-09-13 09:30:00', 30, '2024-09-13 10:00:00', 'PENDING');
insert into appointment (user_id, user_name, technician_id, technician_name, service_id, service_name, salon_id, salon_name, appointment_time, time, finish_time, status)
values (14, '顾客1', 6, '技师-文青', 1, '珍珠养发', 1, '上海-长宁龙之梦店', '2024-09-13 09:30:00', 30, '2024-09-13 10:00:00', 'PENDING');
insert into appointment (user_id, user_name, technician_id, technician_name, service_id, service_name, salon_id, salon_name, appointment_time, time, finish_time, status)
values (12, '顾客1', 5, '技师-艾青', 1, '珍珠养发', 1, '上海-长宁龙之梦店', '2024-09-13 10:00:00', 60, '2024-09-13 11:00:00', 'PENDING');

--
--
-- -- ----------------------------
-- -- Table structure for admin
-- -- ----------------------------
-- DROP TABLE IF EXISTS `admin`;
-- CREATE TABLE `admin`
-- (
--     `id`       int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
--     `username` varchar(255) NULL DEFAULT NULL COMMENT '用户名',
--     `password` varchar(255) NULL DEFAULT NULL COMMENT '密码',
--     `name`     varchar(255) NULL DEFAULT NULL COMMENT '姓名',
--     `avatar`   varchar(255) NULL DEFAULT NULL COMMENT '头像',
--     `role`     varchar(255) NULL DEFAULT NULL COMMENT '角色',
--     `phone`    varchar(255) NULL DEFAULT NULL COMMENT '电话',
--     `email`    varchar(255) NULL DEFAULT NULL COMMENT '邮箱',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 5
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of admin
-- -- ----------------------------
-- INSERT INTO `admin`
-- VALUES (1, 'admin', '123456', '超级管理员', '1715488042681', 'ADMIN', '1234234324', 'admin@nau.com');
-- INSERT INTO `admin`
-- VALUES (3, 'admin1', '123456', '超级管理员1', '1715480997156', 'ADMIN', '15212654654', 'admin1@nau.com');
-- INSERT INTO `admin`
-- VALUES (4, 'admin2', '123456', '超级管理员233', '1715663517309', 'ADMIN', '13236954655', 'admin2@nau.com');

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`       int          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`     varchar(255) NULL DEFAULT NULL COMMENT '操作内容',
    `username` varchar(255) NULL DEFAULT NULL COMMENT '操作人',
    `time`     varchar(255) NULL DEFAULT NULL COMMENT '操作时间',
    `ip`       varchar(255) NULL DEFAULT NULL COMMENT '操作人ip',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 57
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log`
VALUES (1, '登录该系统', 'admin', '2024-08-13 19:48:27', '0:0:0:0:0:0:0:1');
INSERT INTO `log`
VALUES (2, '登录该系统', '超级管理员', '2024-08-13 23:11:27', '0:0:0:0:0:0:0:1');
INSERT INTO `log`
VALUES (3, '登录该系统', '张三', '2024-08-13 23:13:57', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(255) NULL DEFAULT NULL COMMENT '顾客姓名',
    `description` varchar(255) NULL DEFAULT NULL COMMENT '预约养发服务的信息',
    `time`        varchar(255) NULL DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约通知'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice`
VALUES (1, '张三', '预约A1技师成功，请留意预约时间进行使用', '2024-08-13 15:39:19');
INSERT INTO `notice`
VALUES (2, '李四', '预约A2技师成功，请留意预约时间进行使用', '2024-08-13 19:41:56');
INSERT INTO `notice`
VALUES (3, '李四', '预约A2技师失败，管理员审核不通过', '2024-08-13 19:46:06');
--
-- -- ----------------------------
-- -- Table structure for reserve
-- -- ----------------------------
-- DROP TABLE IF EXISTS `reserve`;
-- CREATE TABLE `reserve`
-- (
--     `id`             int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
--     `name`           varchar(255) NULL DEFAULT NULL COMMENT '养发服务名称',
--     `student_name`   varchar(255) NULL DEFAULT NULL COMMENT '顾客名字',
--     `teacher_name`   varchar(255) NULL DEFAULT NULL COMMENT '教师名字',
--     `time`           varchar(255) NULL DEFAULT NULL COMMENT '操作时间',
--     `use_time`       varchar(255) NULL DEFAULT NULL COMMENT '使用时间',
--     `reserve_status` int NULL DEFAULT NULL COMMENT '预约状态',
--     `use_status`     int NULL DEFAULT NULL COMMENT '使用状态',
--     `room_id`        int NULL DEFAULT NULL COMMENT '养发服务信息ID',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 20
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '养发服务预约记录表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of reserve
-- -- ----------------------------
-- INSERT INTO `reserve`
-- VALUES (1, 'A2-305', '张三', '黄老师', '2024-08-13 01:05:38', '18:44:41-22:21:46', 1, 3, 5);
-- INSERT INTO `reserve`
-- VALUES (2, 'A3-203', '张三', '陈老师', '2024-08-13 01:14:06', '08:00:00-12:00:00', 1, 3, 4);
-- INSERT INTO `reserve`
-- VALUES (3, 'A2-203', '李四', '黄老师', '2024-08-13 02:09:34', '18:00:00-21:00:00', 1, 3, 3);
-- INSERT INTO `reserve`
-- VALUES (4, 'A1-102', '张三', '张老师', '2024-08-13 11:38:29', '09:00:00-12:00:00', 1, 3, 2);
--
-- -- ----------------------------
-- -- Table structure for room
-- -- ----------------------------
-- DROP TABLE IF EXISTS `room`;
-- CREATE TABLE `room`
-- (
--     `id`          int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
--     `name`        varchar(255) NULL DEFAULT NULL COMMENT '养发服务编号',
--     `description` varchar(255) NULL DEFAULT NULL COMMENT '养发服务介绍',
--     `avatar`      varchar(255) NULL DEFAULT NULL COMMENT '养发服务图片',
--     `start`       varchar(255) NULL DEFAULT NULL COMMENT '开始时间',
--     `end`         varchar(255) NULL DEFAULT NULL COMMENT '闭门时间',
--     `status`      int NULL DEFAULT NULL COMMENT '使用状态，0空闲中，1使用中',
--     `type_id`     int NULL DEFAULT NULL COMMENT '养发服务分类ID',
--     `teacher_id`  int NULL DEFAULT NULL COMMENT '教师管理员ID',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 6
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '养发服务信息表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of room
-- -- ----------------------------
-- INSERT INTO `room`
-- VALUES (1, 'A1-101', '这里是101养发服务，容量50人', '1715515555473', '20:07:04', '22:30:00', 0, 1, 1);
-- INSERT INTO `room`
-- VALUES (2, 'A1-102', '这里是102养发服务，容量40人', '1715517750960', '09:00:00', '12:00:00', 1, 1, 1);
-- INSERT INTO `room`
-- VALUES (3, 'A2-203', '这里是203养发服务，容量为30人', '1715518260901', '18:00:00', '21:00:00', 1, 3, 2);
-- INSERT INTO `room`
-- VALUES (4, 'A3-203', '这里是203养发服务，容量15人', '1715525006610', '08:00:00', '12:00:00', 1, 5, 3);
-- INSERT INTO `room`
-- VALUES (5, 'A2-305', '这里是305养发服务，容量20人', '1715525102891', '18:44:41', '22:21:46', 1, 3, 2);
-- INSERT INTO `room`
-- VALUES (6, 'A4-302', '这里是A4-302养发服务，容量20人', '1715595109476', '16:10:10', '20:11:28', 1, 6, 3);
--
-- -- ----------------------------
-- -- Table structure for student
-- -- ----------------------------
-- DROP TABLE IF EXISTS `student`;
-- CREATE TABLE `student`
-- (
--     `id`       int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
--     `username` varchar(255) NULL DEFAULT NULL COMMENT '用户名',
--     `password` varchar(255) NULL DEFAULT NULL COMMENT '密码',
--     `name`     varchar(255) NULL DEFAULT NULL COMMENT '姓名',
--     `avatar`   varchar(255) NULL DEFAULT NULL COMMENT '头像',
--     `role`     varchar(255) NULL DEFAULT NULL COMMENT '角色',
--     `phone`    varchar(255) NULL DEFAULT NULL COMMENT '电话',
--     `email`    varchar(255) NULL DEFAULT NULL COMMENT '邮箱',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 8
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '顾客用户表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of student
-- -- ----------------------------
-- INSERT INTO `student`
-- VALUES (1, 'zhangsan', '123456', '张三', '1715490023053', 'STUDENT', '14858748746', 'zhangsan@nau.com');
-- INSERT INTO `student`
-- VALUES (2, 'lisi', '123456', '李四', '1715480777845', 'STUDENT', '15236948523', 'lisi@nau.com');
-- INSERT INTO `student`
-- VALUES (3, 'wangwu', '123456', '王五', '1715480788146', 'STUDENT', '17820453139', '564981866@nau.com');
-- INSERT INTO `student`
-- VALUES (4, 'zhaoliu', '123456', '赵六', '1715480811645', 'STUDENT', '15236458621', 'zhaoliu@nau.com');
-- INSERT INTO `student`
-- VALUES (5, 'chenqi', '123456', '陈七', '1715490111808', 'STUDENT', '14758246698', 'chenqi@nau.com');
-- INSERT INTO `student`
-- VALUES (6, 'gaoba', '123456', '高八', '1715490493371', 'STUDENT', '18546546564', 'gaoba@qq.cpm');
-- INSERT INTO `student`
-- VALUES (7, 'moyan', '123456', '陌颜', '1715496353813', 'STUDENT', '15234659875', 'moyan@nau.com');
-- INSERT INTO `student`
-- VALUES (8, 'moyk', '123456', 'moyk', '1715749039029', 'STUDENT', '14656536321', 'moyk@nau.com');
-- --
-- -- ----------------------------
-- -- Table structure for teacher
-- -- ----------------------------
-- DROP TABLE IF EXISTS `teacher`;
-- CREATE TABLE `teacher`
-- (
--     `id`       int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
--     `username` varchar(255) NULL DEFAULT NULL COMMENT '用户名',
--     `password` varchar(255) NULL DEFAULT NULL COMMENT '密码',
--     `name`     varchar(255) NULL DEFAULT NULL COMMENT '姓名',
--     `avatar`   varchar(255) NULL DEFAULT NULL COMMENT '头像',
--     `role`     varchar(255) NULL DEFAULT NULL COMMENT '角色',
--     `phone`    varchar(255) NULL DEFAULT NULL COMMENT '电话',
--     `email`    varchar(255) NULL DEFAULT NULL COMMENT '邮箱',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 4
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师管理员表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of teacher
-- -- ----------------------------
-- INSERT INTO `teacher`
-- VALUES (1, 'zhang', '123456', '张老师', '1715481004288', 'TEACHER', '13545656556', 'zhang@nau.com');
-- INSERT INTO `teacher`
-- VALUES (2, 'huang', '123456', '黄老师', '1715481009981', 'TEACHER', '17825655436', 'huang@nau.com');
-- INSERT INTO `teacher`
-- VALUES (3, 'chen', '123456', '陈老师', '1715481195774', 'TEACHER', '156365489754', 'chen@nau.com');
-- INSERT INTO `teacher`
-- VALUES (4, 'liang', '123456', '梁老师', '1715619965239', 'TEACHER', '18536422512', 'liang@nau.com');
--
-- -- ----------------------------
-- -- Table structure for type
-- -- ----------------------------
-- DROP TABLE IF EXISTS `type`;
-- CREATE TABLE `type`
-- (
--     `id`          int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
--     `name`        varchar(255) NULL DEFAULT NULL COMMENT '养发服务分类名称',
--     `description` varchar(255) NULL DEFAULT NULL COMMENT '养发服务描述',
--     `teacher_id`  int NULL DEFAULT NULL COMMENT '教师管理员id',
--     PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB
--   AUTO_INCREMENT = 7
--   CHARACTER SET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci COMMENT = '养发服务分类表'
--   ROW_FORMAT = Dynamic;
--
-- -- ----------------------------
-- -- Records of type
-- -- ----------------------------
-- INSERT INTO `type`
-- VALUES (1, 'A1', '这里是A1栋养发服务', 1);
-- INSERT INTO `type`
-- VALUES (3, 'A2', '这里是A2栋养发服务', 2);
-- INSERT INTO `type`
-- VALUES (5, 'A3', '这里是A3栋养发服务', 3);
-- INSERT INTO `type`
-- VALUES (6, 'A4', '这里是A4栋养发服务', 4);

SET
    FOREIGN_KEY_CHECKS = 1;
