CREATE TABLE `shortlink_url_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hash_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'hash码',
  `hash_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '短链接',
  `really_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原url',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '是否有效 1 有效 0 无效',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `end_time_number` int NOT NULL DEFAULT '0' COMMENT '失效时间 单位：分钟',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `hash_value` (`hash_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短链接生成记录';