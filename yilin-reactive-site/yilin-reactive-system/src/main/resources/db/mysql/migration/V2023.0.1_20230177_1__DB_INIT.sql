-- ----------------------------
-- Table structure for yilin_dept
-- ----------------------------
CREATE TABLE `yilin_dept`
(
    `id`            bigint                                                         NOT NULL COMMENT '主键',
    `tenant_id`     varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '000000' COMMENT '租户ID',
    `parent_id`     bigint                                                         NULL DEFAULT 0 COMMENT '父主键',
    `ancestors`     varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '祖级列表',
    `dept_category` int                                                            NULL DEFAULT NULL COMMENT '部门类型',
    `dept_name`     varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '部门名',
    `full_name`     varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '部门全称',
    `sort`          int                                                            NULL DEFAULT NULL COMMENT '排序',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '备注',
    `is_deleted`    int                                                            NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '机构表';

-- ----------------------------
-- Table structure for yilin_dict
-- ----------------------------
CREATE TABLE `yilin_dict`
(
    `id`         bigint                                                        NOT NULL COMMENT '主键',
    `parent_id`  bigint                                                        NULL DEFAULT 0 COMMENT '父主键',
    `code`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
    `dict_key`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
    `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
    `sort`       int                                                           NULL DEFAULT NULL COMMENT '排序',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典备注',
    `is_sealed`  int                                                           NULL DEFAULT 0 COMMENT '是否已封存',
    `is_deleted` int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典表';

-- ----------------------------
-- Table structure for yilin_dict_biz
-- ----------------------------
CREATE TABLE `yilin_dict_biz`
(
    `id`         bigint                                                        NOT NULL COMMENT '主键',
    `tenant_id`  varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '租户ID',
    `parent_id`  bigint                                                        NULL DEFAULT 0 COMMENT '父主键',
    `code`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
    `dict_key`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
    `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
    `sort`       int                                                           NULL DEFAULT NULL COMMENT '排序',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典备注',
    `is_sealed`  int                                                           NULL DEFAULT 0 COMMENT '是否已封存',
    `is_deleted` int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '业务字典表';

-- ----------------------------
-- Table structure for yilin_log
-- ----------------------------
CREATE TABLE `yilin_log`
(
    `id`           bigint                                                         NOT NULL COMMENT '编号',
    `tenant_id`    varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '000000' COMMENT '租户ID',
    `service_id`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '服务ID',
    `server_host`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '服务器名',
    `server_ip`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '服务器IP地址',
    `env`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '服务器环境',
    `type`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL DEFAULT '1' COMMENT '日志类型',
    `title`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '日志标题',
    `method`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '操作方式',
    `request_uri`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '请求URI',
    `user_agent`   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
    `remote_ip`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作IP地址',
    `method_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '方法类',
    `method_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '方法名',
    `params`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          NULL COMMENT '操作提交的数据',
    `time`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '执行时间',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '创建者',
    `create_time`  datetime(0)                                                    NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '接口日志表';

-- ----------------------------
-- Table structure for yilin_menu
-- ----------------------------
CREATE TABLE `yilin_menu`
(
    `id`         bigint                                                        NOT NULL COMMENT '主键',
    `parent_id`  bigint                                                        NULL DEFAULT 0 COMMENT '父级菜单',
    `code`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
    `name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
    `alias`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单别名',
    `path`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
    `source`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单资源',
    `sort`       int                                                           NULL DEFAULT NULL COMMENT '排序',
    `category`   int                                                           NULL DEFAULT NULL COMMENT '菜单类型',
    `action`     int                                                           NULL DEFAULT 0 COMMENT '操作按钮类型',
    `is_open`    int                                                           NULL DEFAULT 1 COMMENT '是否打开新页面',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `is_deleted` int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '菜单表';

-- ----------------------------
-- Table structure for yilin_notice
-- ----------------------------
CREATE TABLE `yilin_notice`
(
    `id`           bigint                                                         NOT NULL COMMENT '主键',
    `tenant_id`    varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '000000' COMMENT '租户ID',
    `title`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '标题',
    `category`     int                                                            NULL DEFAULT NULL COMMENT '类型',
    `release_time` datetime(0)                                                    NULL DEFAULT NULL COMMENT '发布时间',
    `content`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
    `create_user`  bigint                                                         NULL DEFAULT NULL COMMENT '创建人',
    `create_dept`  bigint                                                         NULL DEFAULT NULL COMMENT '创建部门',
    `create_time`  datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    `update_user`  bigint                                                         NULL DEFAULT NULL COMMENT '修改人',
    `update_time`  datetime(0)                                                    NULL DEFAULT NULL COMMENT '修改时间',
    `status`       int                                                            NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`   int                                                            NULL DEFAULT NULL COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知公告表';

-- ----------------------------
-- Table structure for yilin_oss
-- ----------------------------
CREATE TABLE `yilin_oss`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `tenant_id`   varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '000000' COMMENT '租户ID',
    `category`    int                                                           NULL DEFAULT NULL COMMENT '分类',
    `oss_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '资源编号',
    `endpoint`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源地址',
    `access_key`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessKey',
    `secret_key`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'secretKey',
    `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间名',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用ID',
    `region`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地域简称',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `create_user` bigint                                                        NULL DEFAULT NULL COMMENT '创建人',
    `create_dept` bigint                                                        NULL DEFAULT NULL COMMENT '创建部门',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    `update_user` bigint                                                        NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '修改时间',
    `status`      int                                                           NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`  int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '对象存储表';

-- ----------------------------
-- Table structure for yilin_param
-- ----------------------------
CREATE TABLE `yilin_param`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `param_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名',
    `param_key`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数键',
    `param_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `create_user` bigint                                                        NULL DEFAULT NULL COMMENT '创建人',
    `create_dept` bigint                                                        NULL DEFAULT NULL COMMENT '创建部门',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    `update_user` bigint                                                        NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '修改时间',
    `status`      int                                                           NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`  int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '参数表';

-- ----------------------------
-- Table structure for yilin_region
-- ----------------------------
CREATE TABLE `yilin_region`
(
    `code`          varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '区划编号',
    `parent_code`   varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '父区划编号',
    `ancestors`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '祖区划编号',
    `name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '区划名称',
    `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '省级区划编号',
    `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '省级名称',
    `city_code`     varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市级区划编号',
    `city_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市级名称',
    `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '区级区划编号',
    `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '区级名称',
    `town_code`     varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '镇级区划编号',
    `town_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '镇级名称',
    `village_code`  varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '村级区划编号',
    `village_name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '村级名称',
    `region_level`  int                                                           NULL DEFAULT NULL COMMENT '层级',
    `sort`          int                                                           NULL DEFAULT NULL COMMENT '排序',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '行政区划表';

-- ----------------------------
-- Table structure for yilin_role
-- ----------------------------
CREATE TABLE `yilin_role`
(
    `id`         bigint                                                        NOT NULL COMMENT '主键',
    `tenant_id`  varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '000000' COMMENT '租户ID',
    `parent_id`  bigint                                                        NULL DEFAULT 0 COMMENT '父主键',
    `role_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
    `sort`       int                                                           NULL DEFAULT NULL COMMENT '排序',
    `role_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色别名',
    `is_deleted` int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色表';

-- ----------------------------
-- Table structure for yilin_role_menu
-- ----------------------------
CREATE TABLE `yilin_role_menu`
(
    `id`      bigint NOT NULL COMMENT '主键',
    `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单id',
    `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表';

-- ----------------------------
-- Table structure for yilin_role_scope
-- ----------------------------
CREATE TABLE `yilin_role_scope`
(
    `id`             bigint NOT NULL COMMENT '主键',
    `scope_category` int    NULL DEFAULT NULL COMMENT '权限类型(1:数据权限、2:接口权限)',
    `scope_id`       bigint NULL DEFAULT NULL COMMENT '权限id',
    `role_id`        bigint NULL DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色数据权限关联表';

-- ----------------------------
-- Table structure for yilin_scope_api
-- ----------------------------
CREATE TABLE `yilin_scope_api`
(
    `id`            bigint                                                        NOT NULL COMMENT '主键',
    `menu_id`       bigint                                                        NULL DEFAULT NULL COMMENT '菜单主键',
    `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源编号',
    `scope_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口权限名',
    `scope_path`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口权限地址',
    `scope_type`    int                                                           NULL DEFAULT NULL COMMENT '接口权限类型',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口权限备注',
    `create_user`   bigint                                                        NULL DEFAULT NULL COMMENT '创建人',
    `create_dept`   bigint                                                        NULL DEFAULT NULL COMMENT '创建部门',
    `create_time`   datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    `update_user`   bigint                                                        NULL DEFAULT NULL COMMENT '修改人',
    `update_time`   datetime(0)                                                   NULL DEFAULT NULL COMMENT '修改时间',
    `status`        int                                                           NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`    int                                                           NULL DEFAULT NULL COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '接口权限表';

-- ----------------------------
-- Table structure for yilin_scope_data
-- ----------------------------
CREATE TABLE `yilin_scope_data`
(
    `id`            bigint                                                         NOT NULL COMMENT '主键',
    `menu_id`       bigint                                                         NULL DEFAULT NULL COMMENT '菜单主键',
    `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '资源编号',
    `scope_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据权限名称',
    `scope_field`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据权限字段',
    `scope_class`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据权限类名',
    `scope_column`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据权限字段',
    `scope_type`    int                                                            NULL DEFAULT NULL COMMENT '数据权限类型',
    `scope_value`   varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限值域',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据权限备注',
    `create_user`   bigint                                                         NULL DEFAULT NULL COMMENT '创建人',
    `create_dept`   bigint                                                         NULL DEFAULT NULL COMMENT '创建部门',
    `create_time`   datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    `update_user`   bigint                                                         NULL DEFAULT NULL COMMENT '修改人',
    `update_time`   datetime(0)                                                    NULL DEFAULT NULL COMMENT '修改时间',
    `status`        int                                                            NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`    int                                                            NULL DEFAULT NULL COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据权限表';

-- ----------------------------
-- Table structure for yilin_sms
-- ----------------------------
CREATE TABLE `yilin_sms`
(
    `id`          bigint                                                        NOT NULL COMMENT '主键',
    `tenant_id`   varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '000000' COMMENT '租户ID',
    `category`    int                                                           NULL DEFAULT NULL COMMENT '分类',
    `sms_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '资源编号',
    `template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '模板ID',
    `access_key`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessKey',
    `secret_key`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'secretKey',
    `region_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'regionId',
    `sign_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '短信签名',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `create_user` bigint                                                        NULL DEFAULT NULL COMMENT '创建人',
    `create_dept` bigint                                                        NULL DEFAULT NULL COMMENT '创建部门',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    `update_user` bigint                                                        NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '修改时间',
    `status`      int                                                           NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`  int                                                           NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '短信配置表';

-- ----------------------------
-- Table structure for yilin_tenant
-- ----------------------------
CREATE TABLE `yilin_tenant`
(
    `id`             bigint                                                         NOT NULL COMMENT '主键',
    `tenant_id`      varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '000000' COMMENT '租户ID',
    `tenant_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '租户名称',
    `domain`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '域名地址',
    `background_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统背景',
    `linkman`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '联系人',
    `contact_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '联系电话',
    `address`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '联系地址',
    `account_number` int                                                            NULL DEFAULT -1 COMMENT '账号额度',
    `expire_time`    datetime(0)                                                    NULL DEFAULT NULL COMMENT '过期时间',
    `datasource_id`  bigint                                                         NULL DEFAULT NULL COMMENT '数据源ID',
    `license_key`    varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权码',
    `create_user`    bigint                                                         NULL DEFAULT NULL COMMENT '创建人',
    `create_dept`    bigint                                                         NULL DEFAULT NULL COMMENT '创建部门',
    `create_time`    datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    `update_user`    bigint                                                         NULL DEFAULT NULL COMMENT '修改人',
    `update_time`    datetime(0)                                                    NULL DEFAULT NULL COMMENT '修改时间',
    `status`         int                                                            NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`     int                                                            NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '租户表';

-- ----------------------------
-- Table structure for yilin_user
-- ----------------------------
CREATE TABLE `yilin_user`
(
    `id`          bigint                                                         NOT NULL COMMENT '主键',
    `tenant_id`   varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '000000' COMMENT '租户ID',
    `code`        varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '用户编号',
    `user_type`   int                                                            NULL DEFAULT NULL COMMENT '用户平台',
    `account`     varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '账号',
    `password`    varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '密码',
    `name`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '昵称',
    `real_name`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '真名',
    `avatar`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '头像',
    `email`       varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '手机',
    `birthday`    datetime(0)                                                    NULL DEFAULT NULL COMMENT '生日',
    `sex`         int                                                            NULL DEFAULT NULL COMMENT '性别',
    `role_id`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色id',
    `dept_id`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门id',
    `post_id`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位id',
    `create_user` bigint                                                         NULL DEFAULT NULL COMMENT '创建人',
    `create_dept` bigint                                                         NULL DEFAULT NULL COMMENT '创建部门',
    `create_time` datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    `update_user` bigint                                                         NULL DEFAULT NULL COMMENT '修改人',
    `update_time` datetime(0)                                                    NULL DEFAULT NULL COMMENT '修改时间',
    `status`      int                                                            NULL DEFAULT NULL COMMENT '状态',
    `is_deleted`  int                                                            NULL DEFAULT 0 COMMENT '是否已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表';

-- ----------------------------
-- Table structure for yilin_user_app
-- ----------------------------
CREATE TABLE `yilin_user_app`
(
    `id`       bigint                                                        NOT NULL COMMENT '主键',
    `user_id`  bigint                                                        NULL DEFAULT 0 COMMENT '用户ID',
    `user_ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户拓展信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户平台拓展表';

-- ----------------------------
-- Table structure for yilin_user_dept
-- ----------------------------
CREATE TABLE `yilin_user_dept`
(
    `id`      bigint NOT NULL COMMENT '主键',
    `user_id` bigint NULL DEFAULT 0 COMMENT '用户ID',
    `dept_id` bigint NULL DEFAULT 0 COMMENT '部门ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户部门表';

-- ----------------------------
-- Table structure for yilin_user_oauth
-- ----------------------------
CREATE TABLE `yilin_user_oauth`
(
    `id`        bigint                                                         NOT NULL COMMENT '主键',
    `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '租户ID',
    `uuid`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '第三方系统用户ID',
    `user_id`   bigint                                                         NULL DEFAULT NULL COMMENT '用户ID',
    `username`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '账号',
    `nickname`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '用户名',
    `avatar`    varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
    `blog`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '应用主页',
    `company`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '公司名',
    `location`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '地址',
    `email`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '邮件',
    `remark`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '备注',
    `gender`    varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '性别',
    `source`    varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '来源',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户第三方认证表';

-- ----------------------------
-- Table structure for yilin_user_other
-- ----------------------------
CREATE TABLE `yilin_user_other`
(
    `id`       bigint                                                        NOT NULL COMMENT '主键',
    `user_id`  bigint                                                        NULL DEFAULT 0 COMMENT '用户ID',
    `user_ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户拓展信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户平台拓展表';

-- ----------------------------
-- Table structure for yilin_user_web
-- ----------------------------
CREATE TABLE `yilin_user_web`
(
    `id`       bigint                                                        NOT NULL COMMENT '主键',
    `user_id`  bigint                                                        NULL DEFAULT 0 COMMENT '用户ID',
    `user_ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户拓展信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户平台拓展表';

SET FOREIGN_KEY_CHECKS = 1;