-- --------------------------------------------------------
-- 主机:                           111.230.220.37
-- 服务器版本:                        8.0.18 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 mall-test 的数据库结构
CREATE DATABASE IF NOT EXISTS `mall-test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mall-test`;

-- 导出  表 mall-test.cms_help 结构
CREATE TABLE IF NOT EXISTS `cms_help` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `show_status` int(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `read_count` int(1) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帮助表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_help_category 结构
CREATE TABLE IF NOT EXISTS `cms_help_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '分类图标',
  `help_count` int(11) DEFAULT NULL COMMENT '专题数量',
  `show_status` int(2) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帮助分类表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_member_report 结构
CREATE TABLE IF NOT EXISTS `cms_member_report` (
  `id` bigint(20) DEFAULT NULL,
  `report_type` int(1) DEFAULT NULL COMMENT '举报类型：0->商品评价；1->话题内容；2->用户评论',
  `report_member_name` varchar(100) DEFAULT NULL COMMENT '举报人',
  `create_time` datetime DEFAULT NULL,
  `report_object` varchar(100) DEFAULT NULL,
  `report_status` int(1) DEFAULT NULL COMMENT '举报状态：0->未处理；1->已处理',
  `handle_status` int(1) DEFAULT NULL COMMENT '处理结果：0->无效；1->有效；2->恶意',
  `note` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户举报表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_prefrence_area 结构
CREATE TABLE IF NOT EXISTS `cms_prefrence_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sub_title` varchar(255) DEFAULT NULL,
  `pic` varbinary(500) DEFAULT NULL COMMENT '展示图片',
  `sort` int(11) DEFAULT NULL,
  `show_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='优选专区';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_prefrence_area_product_relation 结构
CREATE TABLE IF NOT EXISTS `cms_prefrence_area_product_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prefrence_area_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='优选专区和产品关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_subject 结构
CREATE TABLE IF NOT EXISTS `cms_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `pic` varchar(500) DEFAULT NULL COMMENT '专题主图',
  `product_count` int(11) DEFAULT NULL COMMENT '关联产品数量',
  `recommend_status` int(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `collect_count` int(11) DEFAULT NULL,
  `read_count` int(11) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `album_pics` varchar(1000) DEFAULT NULL COMMENT '画册图片用逗号分割',
  `description` varchar(1000) DEFAULT NULL,
  `show_status` int(1) DEFAULT NULL COMMENT '显示状态：0->不显示；1->显示',
  `content` text,
  `forward_count` int(11) DEFAULT NULL COMMENT '转发数',
  `category_name` varchar(200) DEFAULT NULL COMMENT '专题分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='专题表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_subject_category 结构
CREATE TABLE IF NOT EXISTS `cms_subject_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '分类图标',
  `subject_count` int(11) DEFAULT NULL COMMENT '专题数量',
  `show_status` int(2) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='专题分类表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_subject_comment 结构
CREATE TABLE IF NOT EXISTS `cms_subject_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject_id` bigint(20) DEFAULT NULL,
  `member_nick_name` varchar(255) DEFAULT NULL,
  `member_icon` varchar(255) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `show_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专题评论表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_subject_product_relation 结构
CREATE TABLE IF NOT EXISTS `cms_subject_product_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='专题商品关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_topic 结构
CREATE TABLE IF NOT EXISTS `cms_topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `attend_count` int(11) DEFAULT NULL COMMENT '参与人数',
  `attention_count` int(11) DEFAULT NULL COMMENT '关注人数',
  `read_count` int(11) DEFAULT NULL,
  `award_name` varchar(100) DEFAULT NULL COMMENT '奖品名称',
  `attend_type` varchar(100) DEFAULT NULL COMMENT '参与方式',
  `content` text COMMENT '话题内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='话题表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_topic_category 结构
CREATE TABLE IF NOT EXISTS `cms_topic_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '分类图标',
  `subject_count` int(11) DEFAULT NULL COMMENT '专题数量',
  `show_status` int(2) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='话题分类表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.cms_topic_comment 结构
CREATE TABLE IF NOT EXISTS `cms_topic_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_nick_name` varchar(255) DEFAULT NULL,
  `topic_id` bigint(20) DEFAULT NULL,
  `member_icon` varchar(255) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `show_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专题评论表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.dict 结构
CREATE TABLE IF NOT EXISTS `dict` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `parent_id` bigint(64) unsigned NOT NULL DEFAULT '0' COMMENT '父主键，无则为0',
  `code` varchar(255) DEFAULT NULL COMMENT '字典码',
  `dict_key` varchar(255) DEFAULT NULL COMMENT '字典值',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序，从大到小',
  `remark` varchar(255) DEFAULT NULL COMMENT '字典备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据字典';

-- 数据导出被取消选择。

-- 导出  表 mall-test.mms_login_log 结构
CREATE TABLE IF NOT EXISTS `mms_login_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `ip` varchar(64) DEFAULT NULL COMMENT 'ip地址',
  `login_type` tinyint(1) unsigned DEFAULT NULL COMMENT '登录类型：0->PC；1->android;2->ios;3->小程序',
  PRIMARY KEY (`id`),
  KEY `idx_mid` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 COMMENT='会员登录记录';

-- 数据导出被取消选择。

-- 导出  表 mall-test.mms_member 结构
CREATE TABLE IF NOT EXISTS `mms_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` char(11) NOT NULL COMMENT '手机号码',
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别：0->未知；1->男；2->女',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `integration` int(11) DEFAULT '0' COMMENT '积分',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '帐号启用状态:0->禁用；1->启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=294005 DEFAULT CHARSET=utf8 COMMENT='会员表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.mms_receive_address 结构
CREATE TABLE IF NOT EXISTS `mms_receive_address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `name` varchar(64) DEFAULT NULL COMMENT '收货人名称',
  `phone_number` char(11) DEFAULT NULL COMMENT '收货人手机号',
  `province` varchar(64) DEFAULT NULL COMMENT '省份/直辖市',
  `city` varchar(64) DEFAULT NULL COMMENT '城市',
  `region` varchar(64) DEFAULT NULL COMMENT '区',
  `street` varchar(64) DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址(街道)',
  `is_default` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否为默认，0->不是，1->是',
  PRIMARY KEY (`id`),
  KEY `idx_mid` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='会员收货地址表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_cart_product 结构
CREATE TABLE IF NOT EXISTS `oms_cart_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品的id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品sku的id',
  `brand_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品品牌id',
  `product_category_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品分类id',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `member_username` varchar(64) DEFAULT NULL COMMENT '会员用户名',
  `name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '商品品牌名称',
  `product_category_name` varchar(64) DEFAULT NULL COMMENT '商品分类名称',
  `product_sn` varchar(64) DEFAULT NULL COMMENT '商品货号',
  `sku_code` varchar(64) DEFAULT NULL COMMENT '商品sku编码',
  `sku_pic` varchar(255) DEFAULT NULL COMMENT '商品sku图片',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格（json格式）',
  `quantity` int(11) unsigned DEFAULT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8 COMMENT='购物车商品表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_company_address 结构
CREATE TABLE IF NOT EXISTS `oms_company_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `receiver_name` varchar(64) DEFAULT NULL COMMENT '收发货人姓名',
  `receiver_phone` varchar(64) DEFAULT NULL COMMENT '收发货人电话',
  `province` varchar(64) DEFAULT NULL COMMENT '省/直辖市',
  `city` varchar(64) DEFAULT NULL COMMENT '市',
  `region` varchar(64) DEFAULT NULL COMMENT '区',
  `street` varchar(64) DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `is_send_default` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认发货地址：0->否；1->是',
  `is_receive_default` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认收货地址：0->否；1->是',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '启用状态；0->未启用；1->已启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=362 DEFAULT CHARSET=utf8 COMMENT='公司收发货地址表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order 结构
CREATE TABLE IF NOT EXISTS `oms_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `receive_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `member_id` bigint(20) unsigned NOT NULL COMMENT '会员id',
  `member_username` varchar(64) DEFAULT NULL COMMENT '会员用户名',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '订单状态：0->待付款；1->待发货；2->已发货；3->待评价；4->交易完成；5->申请仅退款；6->申请退货退款；7->仅退款中；8->退货退款中；9->交易关闭',
  `order_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '订单类型：0->正常订单；1->秒杀订单',
  `source_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '订单来源：0->PC订单；1->APP订单',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额（实际支付金额）',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额（商品金额和运费）',
  `freight_amount` decimal(10,2) DEFAULT NULL COMMENT '运费金额',
  `promotion_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '促销金额（促销价、满减、阶梯价）',
  `integration_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '积分抵扣金额',
  `coupon_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠券抵扣金额',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '管理员后台调整订单使用的折扣金额',
  `pay_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式：0->未支付；1->支付宝；2->微信',
  `delivery_company` varchar(64) DEFAULT NULL COMMENT '物流公司(配送方式)',
  `delivery_sn` varchar(64) DEFAULT NULL COMMENT '物流单号',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receiver_name` varchar(64) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) DEFAULT NULL COMMENT '收货人电话',
  `receiver_province` varchar(32) DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` varchar(32) DEFAULT NULL COMMENT '城市',
  `receiver_region` varchar(32) DEFAULT NULL COMMENT '区',
  `receiver_street` varchar(32) DEFAULT NULL COMMENT '街道',
  `receiver_detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `coupon_ids` varchar(50) DEFAULT NULL COMMENT '优惠券id，逗号隔开',
  `use_integration` int(11) unsigned DEFAULT '0' COMMENT '下单时使用的积分',
  `integration` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '获得的积分',
  `promotion_info` varchar(100) DEFAULT NULL COMMENT '活动信息',
  `note` varchar(500) DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sn` (`order_sn`),
  KEY `idx_mid_ctime` (`member_id`,`create_time`),
  KEY `idx_status_ctime` (`status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=379 DEFAULT CHARSET=utf8 COMMENT='订单表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_operate_log 结构
CREATE TABLE IF NOT EXISTS `oms_order_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `order_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单id',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人：用户；系统；后台管理员',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `order_from_status` tinyint(2) unsigned DEFAULT NULL COMMENT '订单现态',
  `order_to_status` tinyint(2) unsigned DEFAULT NULL COMMENT '订单次态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='订单操作记录';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_product 结构
CREATE TABLE IF NOT EXISTS `oms_order_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品sku主键',
  `product_sku_code` varchar(64) DEFAULT NULL COMMENT '商品sku编码',
  `product_sn` varchar(64) DEFAULT NULL COMMENT '商品货号',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '商品品牌名称',
  `product_category_name` varchar(64) DEFAULT NULL COMMENT '商品分类名称',
  `product_pic` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `product_quantity` int(11) unsigned DEFAULT NULL COMMENT '购买数量',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格（json格式）',
  `promotion_name` varchar(200) DEFAULT NULL COMMENT '商品促销名称',
  `promotion_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品促销金额',
  `coupon_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠券优惠金额',
  `integration_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '积分优惠金额',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '该商品经过优惠后的金额，即实际支付金额',
  `integration` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '商品赠送积分',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '商品状态：0->未购买；1->已购买；3->售后中；4->售后完成',
  `is_comment` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否评价：0->未评价；1->已评价',
  PRIMARY KEY (`id`),
  KEY `idx_oid` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=499 DEFAULT CHARSET=utf8 COMMENT='订单中的商品';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_refund_apply 结构
CREATE TABLE IF NOT EXISTS `oms_order_refund_apply` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_id` bigint(20) unsigned NOT NULL COMMENT '订单id',
  `member_id` bigint(20) unsigned NOT NULL COMMENT '会员id',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '退货商品id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT '退货商品sku主键',
  `refund_reason_id` bigint(20) unsigned DEFAULT NULL COMMENT '仅退款原因id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `member_username` varchar(64) DEFAULT NULL COMMENT '会员用户名',
  `return_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `reason_name` varchar(50) DEFAULT NULL COMMENT '退款原因',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `proof_pics` varchar(1000) DEFAULT NULL COMMENT '凭证图片，以逗号隔开',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_note` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '申请状态：0->申请中；1->已拒绝；2->处理中；3->已完成；4->已取消',
  PRIMARY KEY (`id`),
  KEY `idx_oid` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单仅退款申请';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_refund_apply_product 结构
CREATE TABLE IF NOT EXISTS `oms_order_refund_apply_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_refund_apply_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单仅退款申请id',
  `order_product_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单商品id',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品sku主键',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '商品品牌',
  `product_category_name` varchar(64) DEFAULT NULL COMMENT '商品分类名称',
  `product_pic` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格（json格式）',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '商品实际支付单价',
  `return_quantity` int(11) unsigned DEFAULT NULL COMMENT '仅退款数量',
  PRIMARY KEY (`id`),
  KEY `idx_aid` (`order_refund_apply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单仅退款申请商品';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_refund_reason 结构
CREATE TABLE IF NOT EXISTS `oms_order_refund_reason` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `name` varchar(50) DEFAULT NULL COMMENT '仅退款原因名称',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序，从大到小',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态：0->不启用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=320 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单仅退款原因';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_return_apply 结构
CREATE TABLE IF NOT EXISTS `oms_order_return_apply` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_id` bigint(20) unsigned NOT NULL COMMENT '订单id',
  `member_id` bigint(20) unsigned NOT NULL COMMENT '会员id',
  `company_address_id` bigint(20) unsigned DEFAULT NULL COMMENT '收货地址表id',
  `return_reason_id` bigint(20) unsigned DEFAULT NULL COMMENT '退货原因id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `member_username` varchar(64) DEFAULT NULL COMMENT '会员用户名',
  `return_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `reason_name` varchar(50) DEFAULT NULL COMMENT '退货原因',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `proof_pics` varchar(1000) DEFAULT NULL COMMENT '凭证图片，以逗号隔开',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_note` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `receive_note` varchar(500) DEFAULT NULL COMMENT '收货备注',
  `company_address_name` varchar(200) DEFAULT NULL COMMENT '公司地址名称',
  `receiver_name` varchar(64) DEFAULT NULL COMMENT '收发货人姓名',
  `receiver_phone` varchar(64) DEFAULT NULL COMMENT '收发货人电话',
  `province` varchar(64) DEFAULT NULL COMMENT '省/直辖市',
  `city` varchar(64) DEFAULT NULL COMMENT '市',
  `region` varchar(64) DEFAULT NULL COMMENT '区',
  `street` varchar(64) DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '申请状态：0->申请中；1->已拒绝；2->处理中；3->已完成；4->已取消',
  PRIMARY KEY (`id`),
  KEY `idx_oid` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8 COMMENT='订单退货退款申请';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_return_apply_product 结构
CREATE TABLE IF NOT EXISTS `oms_order_return_apply_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_return_apply_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单退货退款申请id',
  `order_product_id` bigint(20) unsigned DEFAULT NULL COMMENT '订单商品id',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品sku主键',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '商品品牌',
  `product_category_name` varchar(64) DEFAULT NULL COMMENT '商品分类名称',
  `product_pic` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格（json格式）',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '商品实际支付单价',
  `return_quantity` int(11) unsigned DEFAULT NULL COMMENT '退货数量',
  PRIMARY KEY (`id`),
  KEY `idx_aid` (`order_return_apply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单退货退款申请的商品';

-- 数据导出被取消选择。

-- 导出  表 mall-test.oms_order_return_reason 结构
CREATE TABLE IF NOT EXISTS `oms_order_return_reason` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `name` varchar(50) DEFAULT NULL COMMENT '退货原因名称',
  `sort` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '排序，从大到小',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态：0->不启用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8 COMMENT='退货原因表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_brand 结构
CREATE TABLE IF NOT EXISTS `pms_brand` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `first_letter` char(1) DEFAULT NULL COMMENT '首字母',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `is_factory` tinyint(3) unsigned DEFAULT NULL COMMENT '是否为品牌制造商：0->不是；1->是',
  `is_show` tinyint(3) unsigned DEFAULT NULL COMMENT '是否显示：0->不显示，1->显示',
  `product_count` int(11) DEFAULT NULL COMMENT '产品数量',
  `product_comment_count` int(11) DEFAULT NULL COMMENT '产品评论数量',
  `logo` varchar(255) DEFAULT NULL COMMENT '品牌logo',
  `big_pic` varchar(255) DEFAULT NULL COMMENT '专区大图',
  `brand_story` text COMMENT '品牌故事',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3901 DEFAULT CHARSET=utf8 COMMENT='品牌表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_comment 结构
CREATE TABLE IF NOT EXISTS `pms_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `member_id` bigint(20) unsigned DEFAULT NULL COMMENT '会员id',
  `member_nickname` varchar(64) DEFAULT NULL COMMENT '会员昵称',
  `member_icon` varchar(500) DEFAULT NULL COMMENT '评论用户头像',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格（json格式）',
  `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `pics` varchar(1000) DEFAULT NULL COMMENT '上传图片地址，以逗号隔开',
  `star` tinyint(1) unsigned DEFAULT NULL COMMENT '评价星数：0->5',
  `like_count` int(11) unsigned DEFAULT '0' COMMENT '点赞数',
  `read_count` int(11) unsigned DEFAULT '0' COMMENT '阅读数',
  `replay_count` int(11) unsigned DEFAULT '0' COMMENT '回复数',
  `is_show` tinyint(1) unsigned DEFAULT '1' COMMENT '是否显示，0->不显示，1->显示',
  `is_pic` tinyint(1) unsigned DEFAULT '1' COMMENT '是否有图，0->无图，1->有图',
  PRIMARY KEY (`id`),
  KEY `idx_pid_lc_isshow_star_ispic` (`product_id`,`like_count`,`is_show`,`star`,`is_pic`)
) ENGINE=InnoDB AUTO_INCREMENT=1417567 DEFAULT CHARSET=utf8 COMMENT='商品评价表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_comment_replay 结构
CREATE TABLE IF NOT EXISTS `pms_comment_replay` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `comment_id` bigint(20) unsigned DEFAULT NULL COMMENT '评论id',
  `member_nickname` varchar(64) DEFAULT NULL COMMENT '会员昵称',
  `member_icon` varchar(500) DEFAULT NULL COMMENT '会员头像',
  `content` varchar(1000) DEFAULT NULL COMMENT '回复内容',
  `type` tinyint(3) unsigned DEFAULT '0' COMMENT '评论人员类型；0->会员；1->管理员',
  PRIMARY KEY (`id`),
  KEY `idx_cid` (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1852613 DEFAULT CHARSET=utf8 COMMENT='商品评价回复表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_feight_template 结构
CREATE TABLE IF NOT EXISTS `pms_feight_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `charge_type` int(1) DEFAULT NULL COMMENT '计费类型:0->按重量；1->按件数',
  `first_weight` decimal(10,2) DEFAULT NULL COMMENT '首重kg',
  `first_fee` decimal(10,2) DEFAULT NULL COMMENT '首费（元）',
  `continue_weight` decimal(10,2) DEFAULT NULL,
  `continme_fee` decimal(10,2) DEFAULT NULL,
  `dest` varchar(255) DEFAULT NULL COMMENT '目的地（省、市）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运费模版';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product 结构
CREATE TABLE IF NOT EXISTS `pms_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `brand_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品品牌id，0表示没有品牌',
  `product_category_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品分类id，0表示没有分类',
  `product_sn` varchar(64) DEFAULT NULL COMMENT '货号',
  `name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '品牌名称',
  `product_category_name` varchar(64) DEFAULT NULL COMMENT '商品分类名称',
  `pic` varchar(255) DEFAULT NULL COMMENT '图片url，用于列表显示',
  `sub_title` varchar(100) DEFAULT NULL COMMENT '副标题，用于列表显示',
  `description` varchar(100) DEFAULT NULL COMMENT '商品简要描述，用于列表显示',
  `detail_title` varchar(64) DEFAULT NULL COMMENT '详细页标题',
  `detail_description` text COMMENT '详细页描述',
  `detail_html` text COMMENT '详情网页内容',
  `detail_mobile_html` text COMMENT '移动端网页详情',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
  `sale` int(11) unsigned DEFAULT NULL COMMENT '销量',
  `stock` int(11) unsigned DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) unsigned DEFAULT NULL COMMENT '库存预警值',
  `unit` char(5) DEFAULT NULL COMMENT '计量单位，最大长度为5',
  `note` varchar(255) DEFAULT NULL COMMENT '商品备注',
  `keywords` varchar(255) DEFAULT NULL COMMENT '商品关键词',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序，从大到小',
  `gift_point` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '赠送的积分',
  `use_point_limit` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '限制使用的积分数',
  `service_ids` varchar(64) DEFAULT NULL COMMENT '以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮',
  `promotion_type` int(10) NOT NULL DEFAULT '0' COMMENT '促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购，默认为0',
  `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
  `is_publish` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '上架状态：0->下架；1->上架，默认为0',
  `is_new` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '新品状态:0->不是新品；1->新品，默认为0',
  `is_recommend` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '推荐状态；0->不推荐；1->推荐，默认为0',
  `is_verify` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态：0->未审核；1->审核通过，默认为0',
  `is_preview` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否为预告商品：0->不是；1->是，默认为0',
  PRIMARY KEY (`id`),
  KEY `idx_sort_ct` (`sort`,`create_time`),
  KEY `idx_bid` (`brand_id`),
  KEY `idx_pcid` (`product_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=454110 DEFAULT CHARSET=utf8 COMMENT='商品信息';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_category 结构
CREATE TABLE IF NOT EXISTS `pms_product_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '上级分类的编号：0表示一级分类',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  `product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
  `is_nav` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否显示在导航栏：0->不显示；1->显示',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序，从大到小排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3453 DEFAULT CHARSET=utf8 COMMENT='商品分类';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_image 结构
CREATE TABLE IF NOT EXISTS `pms_product_image` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `image` varchar(255) DEFAULT NULL COMMENT '图片url',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2761483 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品图片';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_param 结构
CREATE TABLE IF NOT EXISTS `pms_product_param` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `name` varchar(50) DEFAULT NULL COMMENT '参数名称',
  `value` varchar(255) DEFAULT NULL COMMENT '参数值',
  `type` int(10) DEFAULT '0' COMMENT '参数类型：0->纯文本，1->图文',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3268362 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品参数';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_sale_full_reduction 结构
CREATE TABLE IF NOT EXISTS `pms_product_sale_full_reduction` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `full_price` decimal(10,2) DEFAULT NULL COMMENT '商品满足金额',
  `reduce_price` decimal(10,2) DEFAULT NULL COMMENT '商品减少金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8 COMMENT='产品满减表(只针对同商品)';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_sale_ladder 结构
CREATE TABLE IF NOT EXISTS `pms_product_sale_ladder` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `count` int(11) DEFAULT NULL COMMENT '满足的商品数量',
  `discount` decimal(10,2) DEFAULT NULL COMMENT '折扣',
  `price` decimal(10,2) DEFAULT NULL COMMENT '折后价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 COMMENT='产品阶梯价格表(只针对同商品)';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_sale_member_price 结构
CREATE TABLE IF NOT EXISTS `pms_product_sale_member_price` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `member_level_id` bigint(20) DEFAULT NULL COMMENT '会员等级id',
  `member_price` decimal(10,2) DEFAULT NULL COMMENT '会员价格',
  `member_level_name` varchar(100) DEFAULT NULL COMMENT '会员等级名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8 COMMENT='商品会员价格表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_sale_promotion 结构
CREATE TABLE IF NOT EXISTS `pms_product_sale_promotion` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `start_time` datetime DEFAULT NULL COMMENT '促销开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '促销结束时间',
  `price` decimal(10,2) DEFAULT NULL COMMENT '促销价格',
  `limit` int(11) DEFAULT NULL COMMENT '活动限购数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品特惠促销';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_specification 结构
CREATE TABLE IF NOT EXISTS `pms_product_specification` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `name` varchar(50) DEFAULT NULL COMMENT '规格名称',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=907041 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品规格';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_specification_value 结构
CREATE TABLE IF NOT EXISTS `pms_product_specification_value` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_specification_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品规格id',
  `value` varchar(255) DEFAULT NULL COMMENT '选项值',
  `type` int(10) DEFAULT '0' COMMENT '选项类型：0->纯文本，1->图文',
  PRIMARY KEY (`id`),
  KEY `idx_psid` (`product_specification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1863847 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品规格选项';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_product_verify_log 结构
CREATE TABLE IF NOT EXISTS `pms_product_verify_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned NOT NULL COMMENT '商品id',
  `admin_id` bigint(20) unsigned DEFAULT NULL COMMENT '操作用户id',
  `username` varchar(64) DEFAULT NULL COMMENT '操作用户名',
  `note` varchar(1000) DEFAULT NULL COMMENT '审核备注',
  `is_verify` tinyint(1) unsigned NOT NULL COMMENT '审核后状态：0->未审核；1->审核通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品审核记录';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_sku 结构
CREATE TABLE IF NOT EXISTS `pms_sku` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `product_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品id',
  `sku_code` varchar(64) DEFAULT NULL COMMENT 'sku编码',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `promotion_price` decimal(10,2) DEFAULT NULL COMMENT '单品促销价格',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) unsigned DEFAULT NULL COMMENT '预警库存',
  `sale` int(11) unsigned DEFAULT NULL COMMENT '销量',
  `pic` varchar(255) DEFAULT NULL COMMENT '默认展示图片url',
  `specification` varchar(500) DEFAULT NULL COMMENT '商品规格，json格式',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2997230 DEFAULT CHARSET=utf8 COMMENT='商品sku';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_sku_image 结构
CREATE TABLE IF NOT EXISTS `pms_sku_image` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT 'sku主键',
  `image` varchar(255) DEFAULT NULL COMMENT '图片url',
  PRIMARY KEY (`id`),
  KEY `idx_sid` (`sku_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12660878 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sku图片';

-- 数据导出被取消选择。

-- 导出  表 mall-test.pms_sku_specification_relationship 结构
CREATE TABLE IF NOT EXISTS `pms_sku_specification_relationship` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `sku_id` bigint(20) unsigned DEFAULT NULL COMMENT 'sku主键',
  `specification_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品规格id',
  `specification_value_id` bigint(20) unsigned DEFAULT NULL COMMENT '商品规格选项id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_skuid_sid_vid` (`sku_id`,`specification_id`,`specification_value_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8515518 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sku和商品规格关系';

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_BLOB_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_CALENDARS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(190) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_CRON_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_FIRED_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `INSTANCE_NAME` varchar(190) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(190) DEFAULT NULL,
  `JOB_GROUP` varchar(190) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_JOB_DETAILS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(190) NOT NULL,
  `JOB_GROUP` varchar(190) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_LOCKS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_PAUSED_TRIGGER_GRPS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_SCHEDULER_STATE 结构
CREATE TABLE IF NOT EXISTS `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(190) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_SIMPLE_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_SIMPROP_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.QRTZ_TRIGGERS 结构
CREATE TABLE IF NOT EXISTS `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `JOB_NAME` varchar(190) NOT NULL,
  `JOB_GROUP` varchar(190) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_coupon 结构
CREATE TABLE IF NOT EXISTS `sms_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '优惠卷类型；0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `platform` int(1) DEFAULT NULL COMMENT '使用平台：0->全部；1->移动；2->PC',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `per_limit` int(11) DEFAULT NULL COMMENT '每人限领张数',
  `min_point` decimal(10,2) DEFAULT NULL COMMENT '使用门槛；0表示无门槛',
  `start_time` datetime DEFAULT NULL COMMENT '开始使用时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束使用时间',
  `use_type` int(1) DEFAULT NULL COMMENT '使用类型：0->全场通用；1->指定分类；2->指定商品',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `publish_count` int(11) DEFAULT NULL COMMENT '发行数量',
  `use_count` int(11) DEFAULT NULL COMMENT '已使用数量',
  `receive_count` int(11) DEFAULT NULL COMMENT '领取数量',
  `enable_time` datetime DEFAULT NULL COMMENT '可以领取的日期',
  `code` varchar(64) DEFAULT NULL COMMENT '优惠码',
  `member_level` int(1) DEFAULT NULL COMMENT '可领取的会员类型：0->无限时',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='优惠卷表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_coupon_history 结构
CREATE TABLE IF NOT EXISTS `sms_coupon_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券id',
  `member_id` bigint(20) DEFAULT NULL COMMENT '会员id',
  `coupon_code` varchar(64) DEFAULT NULL COMMENT '订单id',
  `member_nickname` varchar(64) DEFAULT NULL COMMENT '领取人昵称',
  `get_type` int(1) DEFAULT NULL COMMENT '获取类型：0->后台赠送；1->主动获取',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `use_status` int(1) DEFAULT NULL COMMENT '使用状态：0->未使用；1->已使用；2->已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `order_sn` varchar(100) DEFAULT NULL COMMENT '订单号码',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`) USING BTREE,
  KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='优惠券使用、领取历史表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_coupon_product_category_relation 结构
CREATE TABLE IF NOT EXISTS `sms_coupon_product_category_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券id',
  `product_category_id` bigint(20) DEFAULT NULL COMMENT '商品分类id',
  `product_category_name` varchar(200) DEFAULT NULL COMMENT '产品分类名称',
  `parent_category_name` varchar(200) DEFAULT NULL COMMENT '父分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='优惠券和产品分类关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_coupon_product_relation 结构
CREATE TABLE IF NOT EXISTS `sms_coupon_product_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(500) DEFAULT NULL COMMENT '商品名称',
  `product_sn` varchar(200) DEFAULT NULL COMMENT '商品编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='优惠券和产品的关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_flash_promotion 结构
CREATE TABLE IF NOT EXISTS `sms_flash_promotion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `status` int(1) DEFAULT NULL COMMENT '上下线状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='限时购表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_flash_promotion_log 结构
CREATE TABLE IF NOT EXISTS `sms_flash_promotion_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) DEFAULT NULL COMMENT '会员id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `member_phone` varchar(64) DEFAULT NULL COMMENT '会员电话',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `subscribe_time` datetime DEFAULT NULL COMMENT '会员订阅时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='限时购通知记录';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_flash_promotion_product_relation 结构
CREATE TABLE IF NOT EXISTS `sms_flash_promotion_product_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `flash_promotion_id` bigint(20) DEFAULT NULL COMMENT '限时购id',
  `flash_promotion_session_id` bigint(20) DEFAULT NULL COMMENT '限时购场次id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `flash_promotion_price` decimal(10,2) DEFAULT NULL COMMENT '限时购价格',
  `flash_promotion_count` int(11) DEFAULT NULL COMMENT '限时购数量',
  `flash_promotion_limit` int(11) DEFAULT NULL COMMENT '每人限购数量',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='商品限时购与商品关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_flash_promotion_session 结构
CREATE TABLE IF NOT EXISTS `sms_flash_promotion_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) DEFAULT NULL COMMENT '场次名称',
  `start_time` time DEFAULT NULL COMMENT '每日开始时间',
  `end_time` time DEFAULT NULL COMMENT '每日结束时间',
  `status` int(1) DEFAULT NULL COMMENT '启用状态：0->不启用；1->启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='限时购场次表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_home_advertise 结构
CREATE TABLE IF NOT EXISTS `sms_home_advertise` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `type` int(1) DEFAULT NULL COMMENT '轮播位置：0->PC首页轮播；1->app首页轮播',
  `pic` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(1) DEFAULT NULL COMMENT '上下线状态：0->下线；1->上线',
  `click_count` int(11) DEFAULT NULL COMMENT '点击数',
  `order_count` int(11) DEFAULT NULL COMMENT '下单数',
  `url` varchar(500) DEFAULT NULL COMMENT '链接地址',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='首页轮播广告表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_home_brand 结构
CREATE TABLE IF NOT EXISTS `sms_home_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_id` bigint(20) DEFAULT NULL COMMENT '商品品牌id',
  `brand_name` varchar(64) DEFAULT NULL COMMENT '商品品牌名称',
  `recommend_status` int(1) DEFAULT NULL COMMENT '推荐状态：0->不推荐;1->推荐',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='首页推荐品牌表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_home_new_product 结构
CREATE TABLE IF NOT EXISTS `sms_home_new_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `recommend_status` int(1) DEFAULT NULL COMMENT '推荐状态：0->不推荐;1->推荐',
  `sort` int(1) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='新鲜好物表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_home_recommend_product 结构
CREATE TABLE IF NOT EXISTS `sms_home_recommend_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `recommend_status` int(1) DEFAULT NULL,
  `sort` int(1) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='人气推荐商品表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.sms_home_recommend_subject 结构
CREATE TABLE IF NOT EXISTS `sms_home_recommend_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject_id` bigint(20) DEFAULT NULL COMMENT '专题id',
  `subject_name` varchar(64) DEFAULT NULL COMMENT '专题名称',
  `recommend_status` int(1) DEFAULT NULL COMMENT '推荐状态：0->不推荐;1->推荐',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='首页推荐专题表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_admin 结构
CREATE TABLE IF NOT EXISTS `ums_admin` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint(2) unsigned DEFAULT NULL COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1054694 DEFAULT CHARSET=utf8 COMMENT='后台用户表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_admin_login_log 结构
CREATE TABLE IF NOT EXISTS `ums_admin_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '后台用户id',
  `ip` varchar(64) DEFAULT NULL COMMENT 'ip地址',
  `address` varchar(100) DEFAULT NULL,
  `user_agent` varchar(100) DEFAULT NULL COMMENT '浏览器登录类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8 COMMENT='后台用户登录日志表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_admin_role_relation 结构
CREATE TABLE IF NOT EXISTS `ums_admin_role_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `admin_id` bigint(20) unsigned DEFAULT NULL COMMENT '后台用户id',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `idx_aid_rid` (`admin_id`,`role_id`),
  KEY `idx_rid` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3018921 DEFAULT CHARSET=utf8 COMMENT='后台用户和角色关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_menu 结构
CREATE TABLE IF NOT EXISTS `ums_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父级ID，0->无上级菜单',
  `title` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `level` int(4) DEFAULT NULL COMMENT '菜单级数，0->一级菜单，1->二级菜单',
  `sort` int(4) DEFAULT NULL COMMENT '菜单排序，从大到小排序',
  `name` varchar(100) DEFAULT NULL COMMENT '前端名称',
  `icon` varchar(200) DEFAULT NULL COMMENT '前端图标',
  `hidden` tinyint(1) unsigned DEFAULT NULL COMMENT '前端隐藏，0->显示，1->隐藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=375 DEFAULT CHARSET=utf8 COMMENT='后台菜单表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_resource 结构
CREATE TABLE IF NOT EXISTS `ums_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `category_id` bigint(20) unsigned DEFAULT NULL COMMENT '资源分类ID，没有分类则为0',
  `name` varchar(200) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(200) DEFAULT NULL COMMENT '资源URL',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1137 DEFAULT CHARSET=utf8 COMMENT='后台资源表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_resource_category 结构
CREATE TABLE IF NOT EXISTS `ums_resource_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `sort` int(4) DEFAULT NULL COMMENT '排序，从大到小',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='资源分类表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_role 结构
CREATE TABLE IF NOT EXISTS `ums_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `admin_count` int(11) NOT NULL DEFAULT '0' COMMENT '后台用户数量',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=516 DEFAULT CHARSET=utf8 COMMENT='后台用户角色表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_role_menu_relation 结构
CREATE TABLE IF NOT EXISTS `ums_role_menu_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) unsigned DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=967 DEFAULT CHARSET=utf8 COMMENT='后台角色菜单关系表';

-- 数据导出被取消选择。

-- 导出  表 mall-test.ums_role_resource_relation 结构
CREATE TABLE IF NOT EXISTS `ums_role_resource_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除，0->未删除，1->已删除',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint(20) unsigned DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1559 DEFAULT CHARSET=utf8 COMMENT='后台角色资源关系表';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
