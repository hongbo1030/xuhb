/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/3/28 10:01:12                           */
/*==============================================================*/
drop table if exists t_sys_info;

drop table if exists t_sys_log;

drop table if exists t_sys_priv;

drop table if exists t_sys_role;

drop table if exists t_sys_role_priv;

drop table if exists t_sys_user;

drop table if exists t_sys_user_role;

drop table if exists t_cal_queue_info;

drop table if exists t_cal_queue_apply;

drop table if exists t_quo_space_queue;

drop table if exists t_sys_config;

drop table if exists t_service;

drop table if exists t_resource_redis;

drop table if exists t_service_databackup;

drop table if exists t_service_add_reduce;

drop table if exists t_service_container;

drop table if exists t_resource_hbase;

drop table if exists t_common_config;

drop table if exists t_resource_apply_approve;

drop table if exists t_appname_relation;

drop table if exists t_service_record;

drop table if exists t_conf_metric_group;

drop table if exists t_platform_kv;

drop table if exists t_metric;

drop table if exists t_conf_machine;

drop table if exists t_alarm_info;

drop table if exists t_service_databackup_history;

drop table if exists t_alarm_message;

drop table if exists t_alarm_message_temp;

drop table if exists t_alarm_rule;

drop table if exists t_alarm_rule_item;

drop table if exists t_alarm_rule_times;

drop table if exists t_alarm_team_user_rel;

drop table if exists t_conf_alarm_team;

drop table if exists t_conf_alarm_user;

drop table if exists t_conf_metric;

drop table if exists t_metric_history;

drop table if exists t_monitor_metric;

drop table if exists t_platform_machine_rel;

drop table if exists t_conf_platform;

drop table if exists t_conf_platform_manager;

drop table if exists t_machine;

drop table if exists t_machine_service;

drop table if exists t_business;

drop table if exists t_conf_business;

drop table if exists t_monitor_storeused;

drop table if exists t_conf_script;

drop table if exists t_script_run_log;

drop table if exists t_alarm_handle_record;

drop table if exists t_conf_machine_group;

drop table if exists t_resource_hive;

drop table if exists t_resource_spark;

drop table if exists t_resource_public_hbase;

drop table if exists t_resource_public_hdfs;

drop table if exists t_tenant_hadoop;

drop table if exists t_tenant_resource;

drop table if exists t_resource_storm;

drop table if exists t_base_config;

drop table if exists t_resource_gbase;


/*==============================================================*/
/* Table: t_sys_info                                            */
/*==============================================================*/
create table t_sys_info
(
  id         bigint not null auto_increment,
  userid     int comment '消息拥有人id',
  msghead    varchar(100) comment '消息头',
  msgdetail  varchar(500) comment '消息体',
  type       int comment '消息类型:0:默认  1:系统消息  2:审批消息',
  status     int comment '消息状态 0:默认状态 1:未读 2:已读',
  createtime timestamp default current_timestamp comment '创建时间',
  primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统消息表';
  
/*==============================================================*/
/* Table: t_sys_log                                             */
/*==============================================================*/
create table t_sys_log
(
   id                   bigint not null auto_increment,
   userid               int comment '用户id',
   username             varchar(50) comment '用户名',
   ip                   varchar(50) comment '用户ip',
   actionmodule         varchar(100) comment '操作模块',
   actiontype           varchar(20) comment '操作类型.query;add;modify;delete',
   actiontime           timestamp comment '操作时间',
   action               mediumtext comment '简要操作内容',
   remark               mediumtext comment '具体操作',
   status               smallint comment '1--成功 2--失败',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

/*==============================================================*/
/* Table: t_sys_priv                                            */
/*==============================================================*/
create table t_sys_priv
(
   id                   int not null auto_increment comment '唯一标识',
   name                 varchar(50) comment '名称',
   fatherid             int comment '上级目录id',
   urlmapping           varchar(200) comment '对应的url地址',
   level                tinyint comment '目录级别',
   seq                  tinyint not null default 0 comment '在上级菜单下的顺序',
   icon                 varchar(50) default null comment '目录页面显示的图标',
   `key`				varchar(50) comment '目录对应的key',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统权限表';

/*==============================================================*/
/* Table: t_sys_role                                            */
/*==============================================================*/
create table t_sys_role
(
   id                   int not null auto_increment comment '角色id',
   name                 varchar(50) comment '角色名',
   type                 int comment '1:系统管理员 2：资源池管理员  3：租户',
   resoursepriv         int comment '1：基础分析库  2：HBase查询库  3：MPP资源库  4：内存数据资源库  5：流处理资源库',
   dscp                 varchar(1000) comment '角色描述',
   primary key (id),
   unique key ak_t_sys_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色表';

/*==============================================================*/
/* Table: t_sys_role_priv                                       */
/*==============================================================*/
create table t_sys_role_priv
(
   id                   int not null auto_increment comment '主键',
   roleid               int comment '角色id',
   privid               int comment '权限id',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

/*==============================================================*/
/* Table: t_sys_user                                            */
/*==============================================================*/
create table t_sys_user
(
   id                   int not null auto_increment comment '唯一标识',
   name                 varchar(50) comment '用户名',
   namezh               varchar(100) comment '姓名',
   password             varchar(128) comment '密码',
   phone                varchar(20) comment '电话号码',
   email                varchar(50) comment '邮箱',
   status               tinyint comment '状态.0-正常;1-停用;2-注销',
   createtime           timestamp default current_timestamp comment '创建时间',
   starttime            datetime comment '生效时间',
   endtime              datetime comment '失效时间',
   remark               varchar(150) comment '描述',
   duty                 varchar(150) comment '职位',
   unit                 varchar(200) comment '单位',
   department           varchar(150) comment '部门',
   `portraitname`       varchar(200)  comment '头像图片名称',
   primary key (id),
   unique key ak_t_sys_user_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统人员表';

/*==============================================================*/
/* Table: t_sys_user_role                                       */
/*==============================================================*/
create table t_sys_user_role
(
   id                   int not null auto_increment comment '唯一标示',
   userid               int comment '用户id',
   roleid               int comment '角色id',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

/*==============================================================*/
/* Table: t_cal_queue_info                                      */
/*==============================================================*/
create table t_cal_queue_info
(
   id                   int not null auto_increment comment '唯一标识',
   queuename            varchar(50) comment '计算队列名称',
   minmem               double(11,1) comment '最小内存',
   maxmem               double(11,1) comment '最大内存',
   minvcore             double(11,2) comment '最小vcore',
   maxvcore             double(11,2) comment '最大vcore',
   createtime           datetime comment '创建时间',
   tenement             varchar(50) comment '租户名',
   userid               int comment '租户id',
   parentid             int comment '父队列id',
   usedmem              double(11,1) comment '已用',
   remark               varchar(200) comment '描述',
   status               varchar(4) comment '状态 1:待审批 2:已开通 3:拒绝 4:删除',
   state                varchar(4) comment '后台状态 1:RUNNING 2:STOPPED',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算队列表';

/*==============================================================*/
/* Table: t_cal_queue_apply                                     */
/*==============================================================*/
create table t_cal_queue_apply
(
   id                   int not null auto_increment comment '唯一标识',
   queueid              int comment '计算队列id',
   queuename            varchar(100) comment '计算队列id',
   minmem               int comment '最小内存',
   maxmem               int comment '最大内存',
   minvcore             int comment '最小vcore',
   maxvcore             int comment '最大vcore',
   applytime            datetime comment '申请时间',
   approvetime          datetime comment '审批时间',
   tenement             varchar(50) comment '租户名',
   userid               int comment '租户id',
   status               varchar(4) comment '状态 1:待审批 2:已开通 3:拒绝',
   applydesc            varchar(200) comment '申请描述',
   approvedesc          varchar(200) comment '审批意见',
   type                 varchar(2) comment '类型 1:新建计算队列 2:调整计算队列',
   parentid             int comment '父队列id',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计算队列申请表';


CREATE TABLE `t_quo_space_queue` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) COMMENT '队列名称',
  `userid` int(11) COMMENT '租户id',
  `assigned` float COMMENT '分配的限额',
  `description` varchar(1000) COMMENT '描述',
  `status` int(11) COMMENT '申请状态: 0-已开通，1-待审批，2-拒绝',
  `createtime` datetime COMMENT '开通时间',
  `parentid` int(11) COMMENT '父队列id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储资源队列表';

CREATE TABLE `t_sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) COMMENT '配置项名称',
  `value` varchar(100) COMMENT '配置项的值',
  `unit` varchar(10) COMMENT '单位',
  `desc` varchar(1000) COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置项表';

CREATE TABLE `t_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) DEFAULT NULL COMMENT '实例名称',
  `type` int(11) DEFAULT NULL COMMENT '''实例类型  1：sqlfile   2：redis   3：streams   4：gbase   5：hive   6：spark    7：impala   8:hbase',
  `remark` varchar(1000) DEFAULT NULL COMMENT '描述',
  `userid` int(11) DEFAULT NULL COMMENT '租户id',
  `username` varchar(128) DEFAULT NULL COMMENT '租户名',
  `access_ip` varchar(128) DEFAULT NULL COMMENT '访问ip',
  `access_port` int(11) DEFAULT NULL COMMENT '访问端口',
  `instance_username` varchar(128) DEFAULT NULL COMMENT '实例用户名',
  `instance_password` varchar(128) DEFAULT NULL COMMENT '实例用户密码',
  `authority` varchar(10) DEFAULT NULL COMMENT '''用户实例权限  默认 000',
  `server_status` int(11) DEFAULT NULL COMMENT '实例服务状态',
  `maintenance_status` int(11) DEFAULT NULL COMMENT '实例维护状态： 0：默认 1:正常运行  2:停止  3:注销  4:未创建 5:创建中 6:启动中 7:停止中 8:注销中 9：扩容中 10：缩容中 11：服务故障',
  `maintenresult_status` int(11) DEFAULT NULL COMMENT '实例维护结果状态 0:默认 1:创建成功 2:创建失败 3:停止失败  4:注销失败  5:启动失败 6：扩容成功  7：扩容失败 8：缩容成功 9：缩容失败  10:备份成功  11:备份失败  12:资源不足 13:停止成功 14：启动成功 15:注销成功',
  `effecttime` datetime DEFAULT NULL COMMENT '生效时间',
  `createtime` datetime DEFAULT NULL COMMENT '服务创建时间',
  `fail_times` int(11) DEFAULT NULL COMMENT '失败次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实例表';

CREATE TABLE `t_resource_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `type` int(11) DEFAULT NULL COMMENT '模式 1、集群 2、主备',
  `mem` int(11) DEFAULT NULL COMMENT '内存 单位G',
  `masternum` int(11) DEFAULT NULL COMMENT 'master个数',
  `slavenum` int(11) DEFAULT NULL COMMENT 'slave个数',
  `calqueueid` int(11) DEFAULT NULL COMMENT '计算资源队列id',
  `calqueuename` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='redis资源表';

CREATE TABLE `t_service_databackup` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `backup_type` int(11) DEFAULT NULL COMMENT '数据备份类型：1：分钟   2：小时   3：天',
  `backup_rate` int(11) DEFAULT NULL COMMENT '数据备份频率',
  `backup_num` int(11) DEFAULT NULL COMMENT '保留备份数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据备份策略表';

CREATE TABLE `t_service_add_reduce` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `add_cpuusage` varchar(10) DEFAULT NULL COMMENT '扩容时cpu平局使用率',
  `add_memusage` varchar(10) DEFAULT NULL COMMENT '扩容时内存平局使用率',
  `add_containerratio` varchar(10) DEFAULT NULL COMMENT '扩容时触发容器比例',
  `add_durationtime` varchar(10) DEFAULT NULL COMMENT '扩容时持续时长',
  `add_num` varchar(10) DEFAULT NULL COMMENT '扩容数量',
  `reduce_cpuusage` varchar(10) DEFAULT NULL COMMENT '缩容cpu平局使用率',
  `reduce_memusage` varchar(10) DEFAULT NULL COMMENT '缩容内存平局使用率',
  `reduce_containerratio` varchar(10) DEFAULT NULL COMMENT '缩容触发容器比例',
  `reduce_durationtime` varchar(10) DEFAULT NULL COMMENT '缩容持续时长',
  `reduce_num` varchar(10) DEFAULT NULL COMMENT '缩容数量',
  `status` int(11) DEFAULT NULL COMMENT '扩缩容计划是否生效1生效0不生效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='扩缩容策略表';

CREATE TABLE `t_service_container` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `role` varchar(20) DEFAULT NULL COMMENT '容器机器角色名称',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `client_port` int(11) DEFAULT NULL COMMENT '客户端端口',
  `intercom_port` int(11) DEFAULT NULL COMMENT '内部通信端口',
  `cpu` int(11) DEFAULT NULL COMMENT 'cpu个数',
  `cpuper` float DEFAULT NULL COMMENT 'cpu百分比',
  `memory` int(11) DEFAULT NULL COMMENT 'memory大小',
  `memoryper` float DEFAULT NULL COMMENT 'memory使用占比',
  `status` int(11) DEFAULT NULL COMMENT '状态 1：可用  2：不可用',
  `yarn_container_name` varchar(256) DEFAULT NULL COMMENT 'yarn名称',
  `docker_container_name` varchar(256) DEFAULT NULL COMMENT 'docker名称',
  `fail_time` int(11) DEFAULT NULL COMMENT '失败次数',
  sub_appname varchar(100) COMMENT '实例名称，字母格式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实例运行容器表';

CREATE TABLE `t_resource_hbase` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `serviceid`  int COMMENT '实例id' ,
  `mastervcore`  int COMMENT 'Hmaster的vcore' ,
  `mastermem`  int COMMENT 'Hmaster的内存' ,
  `masternum`  int COMMENT 'Hmaster的台数' ,
  `regionvcore`  int COMMENT 'RegionServer的vcore' ,
  `regionmem`  int COMMENT 'RegionServer的内存' ,
  `regionnum`  int COMMENT 'RegionServer的台数' ,
  `computqueue`  varchar(200) COMMENT '计算资源队列' ,
  `storagequeue`  varchar(200) COMMENT '存储资源队列' ,
  `createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Hbase实例表';

CREATE TABLE `t_common_config` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `serviceid`  int COMMENT '实例id' ,
  `configelement`  varchar(100) COMMENT '配置项' ,
  `configvalue`  varchar(600) COMMENT '配置项内容' ,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实例基本配置表';

CREATE TABLE `t_resource_apply_approve` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '申请单号',
  `applier_id` int(11) DEFAULT NULL COMMENT '申请人id',
  `applier_name` varchar(128) DEFAULT NULL COMMENT '申请人姓名',
  `apply_date` datetime DEFAULT NULL COMMENT '申请时间',
  `apply_type` int(11) DEFAULT NULL COMMENT '申请类型:0:默认 1:新增存储审资源2:调整存储资源3:新增计算资源4:调整计算资源',
  `apply_desc` varchar(1000) DEFAULT NULL COMMENT '申请描述',
  `approver_id` int(11) DEFAULT NULL COMMENT '审批人id',
  `approve_desc` varchar(1000) DEFAULT NULL COMMENT '审批意见',
  `approve_date` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_status` int(11) DEFAULT NULL COMMENT '审批状态0:全部  1:待审批 2:审批通过 3:审批拒绝',
  `old_quospace` float DEFAULT NULL COMMENT '原存储队列大小',
  `new_quospace` float DEFAULT NULL COMMENT '新申请存储队列大小',
  `old_minmem` double(11,1) DEFAULT NULL COMMENT '原计算队列最小内存',
  `old_maxmem` double(11,1) DEFAULT NULL COMMENT '原计算队列最大内存',
  `old_minvcore` double(11,2) DEFAULT NULL COMMENT '原计算队列最小vcore',
  `old_maxvcore` double(11,2) DEFAULT NULL COMMENT '原计算队列最大vcore',
  `new_minmem` double(11,1) DEFAULT NULL COMMENT '新申请计算队列最小内存',
  `new_maxmem` double(11,1) DEFAULT NULL COMMENT '新申请计算队列最大内存',
  `new_minvcore` double(11,2) DEFAULT NULL COMMENT '新申请计算队列最小vcore',
  `new_maxvcore` double(11,2) DEFAULT NULL COMMENT '新申请计算队列最大vcore',
  `queuename` varchar(128) DEFAULT NULL COMMENT '队列名称',
  `parentqueueid` int(11) DEFAULT NULL COMMENT '父队列id',
  `parentqueuename` varchar(128) DEFAULT NULL COMMENT '队列名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源队列申请审批表';


create table t_appname_relation
(
  id          int(11) not null AUTO_INCREMENT COMMENT '自增主键',
  appname     varchar(100) COMMENT '实例名称，字母格式',
  sub_appname varchar(100) COMMENT '实例名称，字母格式',
  status      int(4) COMMENT '是否为扩容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='APPNAME关联关系表';


create table t_service_record
(
  id          int(11) not null AUTO_INCREMENT COMMENT '自增主键',
  appname       varchar(100),
  restoretime   varchar(100),
  locatormemory int(11),
  servermemory  int(11),
  locatorvcore int(11),
  servervcore int(11),
  redisvcore int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录service的一些信息，后台使用';

CREATE TABLE `t_alarm_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(256) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `rule_title` varchar(256) DEFAULT NULL,
  `rule_item_id` int(11) DEFAULT NULL,
  `metric_key` varchar(500) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `metric_create_time` datetime DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL COMMENT '优先级',
  `metrictype` varchar(256) DEFAULT NULL COMMENT 'is ''指标类型: 1-数字  2-字符串'';',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_machine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modelid` int(11) DEFAULT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `manageip` varchar(15) DEFAULT NULL,
  `bussinessip` varchar(15) DEFAULT NULL,
  `machinetype` tinyint(4) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `os` varchar(1024) DEFAULT NULL,
  `mem` varchar(1024) DEFAULT NULL,
  `harddisk` varchar(1024) DEFAULT NULL,
  `supervisorid` int(11) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  `millionnetip` varchar(15) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `groupid`  int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL,
  `port` varchar(50) DEFAULT NULL,
  `metric_key` varchar(200) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `collect_time` varchar(100) DEFAULT NULL,
  `update_time` varchar(100) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_uk_t_meric_ip_key` (`ip`, `metric_key`) USING BTREE 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_machine_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(1024) DEFAULT NULL,
  `grouptype` tinyint(4) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `grouplevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_platform_kv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(256) DEFAULT NULL,
  `metric_key` varchar(256) DEFAULT NULL,
  `title` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_metric_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_group_name` varchar(256) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `group_level` int(11) DEFAULT NULL,
  `group_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_service_databackup_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceid` int(11) DEFAULT NULL,
  `backuptime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(256) DEFAULT NULL,
  `metric_key` varchar(500) DEFAULT NULL,
  `metric_create_time` datetime DEFAULT NULL,
  `alarm_team_id` int(11) DEFAULT NULL,
  `alarm_type` varchar(256) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `alarm_info_id` int(11) DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `notice_time` datetime DEFAULT NULL,
  `message_create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_message_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(256) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `rule_item_id` int(11) DEFAULT NULL,
  `rule_times_id` int(11) DEFAULT NULL,
  `alarm_info_id` int(11) DEFAULT NULL,
  `metric_key` varchar(500) DEFAULT NULL,
  `metric_create_time` datetime DEFAULT NULL,
  `notice_time` datetime DEFAULT NULL,
  `alarm_team_id` int(11) DEFAULT NULL,
  `alarm_type` varchar(256) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `notice_seq` tinyint(4) DEFAULT NULL,  
  `priority` tinyint(4) DEFAULT NULL COMMENT '优先级',
  `title` varchar(500) DEFAULT NULL COMMENT '告警标题',
  `metrictype` varchar(256) DEFAULT NULL COMMENT '指标类型: 1-数字  2-字符串',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `machine_ids` varchar(4000) DEFAULT NULL,
  `metric_id` int(11) DEFAULT NULL,
  `ignore_time` int(11) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_rule_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_value_min` varchar(256) DEFAULT NULL,
  `metric_value_max` varchar(256) DEFAULT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_rule_times` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_id` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  `notice_type` varchar(10) DEFAULT NULL,
  `interval_time` int(11) DEFAULT NULL,
  `alarm_team_id` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_team_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_alarm_team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_alarm_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `phonenum` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_name` varchar(500) DEFAULT NULL,
  `metric_source` varchar(500) DEFAULT NULL,
  `metric_property` tinyint(4) DEFAULT NULL,
  `metric_desc` tinyint(4) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `machine_ids` varchar(2000) DEFAULT NULL,
  `metric_key` varchar(500) DEFAULT NULL,
  `metric_group_id` int(11) DEFAULT NULL,
  `metric_type` tinyint(4) DEFAULT NULL,
  `unit` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `t_metric_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL,
  `port` varchar(50) DEFAULT NULL,
  `metric_key` varchar(200) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `collect_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_monitor_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceid` int(11) DEFAULT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `metric_key` varchar(100) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `containerid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_platform_machine_rel` (
  `platform_id` int(11) DEFAULT NULL,
  `machine_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_name` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_platform_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platformid` int(11) DEFAULT NULL,
  `manageip` varchar(15) DEFAULT NULL,
  `bussinessip` varchar(15) DEFAULT NULL,
  `port` varchar(5) DEFAULT NULL,
  `password` varchar(1024) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_machine` (
  `id` int(11) NOT NULL,
  `hostname` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_machine_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceid` int(11) DEFAULT NULL,
  `machineid` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `process_names` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_business` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `system_level` char(255) DEFAULT NULL,
  `system_status` char(255) DEFAULT NULL,
  `factory` varchar(20) DEFAULT NULL,
  `biz_level` varchar(2) DEFAULT NULL,
  `parent_biz` int(11) DEFAULT NULL,
  `deleted_flag` char(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_business` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `programlevel` int(11) DEFAULT NULL,
  `programname` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_monitor_storeused` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceid` int(11) DEFAULT NULL,
  `storeused` double DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

CREATE TABLE `t_conf_script` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(256) DEFAULT NULL COMMENT '脚本名称',
  `command` varchar(1000) DEFAULT NULL COMMENT '执行命令',
  `version` varchar(256) DEFAULT NULL COMMENT '版本',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `desc` varchar(1000) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_script_run_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `scriptid` int(11) DEFAULT NULL COMMENT '脚本id',
  `runtime` datetime DEFAULT NULL COMMENT '运行时间',
  `ip` varchar(256) DEFAULT NULL COMMENT '运行设备',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_alarm_handle_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alarmid` int(11) DEFAULT NULL COMMENT '告警id',
  `advice` varchar(2000) DEFAULT NULL COMMENT '处理方案',
  `createtime` datetime DEFAULT NULL COMMENT '处理时间',
  `handleperson` varchar(200) DEFAULT NULL COMMENT '处理人',
  `handleresult` tinyint(4) DEFAULT NULL COMMENT '处理结果 0-默认 1-未处理 2-处理成功 3-处理失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_resource_hive` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `dbname` varchar(200) DEFAULT NULL COMMENT '资源库名称',
  `computqueue` varchar(200) DEFAULT NULL COMMENT '计算资源队列',
  `storagequeue` varchar(200) DEFAULT NULL COMMENT '存储资源队列',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `hadoopaccount` varchar(100) DEFAULT NULL COMMENT 'hadoop账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='hive实例表';

CREATE TABLE `t_resource_spark` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `dbname` varchar(200) DEFAULT NULL COMMENT '资源库名称',
  `computqueue` varchar(200) DEFAULT NULL COMMENT '计算资源队列',
  `storagequeue` varchar(200) DEFAULT NULL COMMENT '存储资源队列',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `hadoopaccount` varchar(100) DEFAULT NULL COMMENT 'hadoop账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='hive实例表';

CREATE TABLE `t_resource_public_hbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `namespace` varchar(200) DEFAULT NULL COMMENT 'namespace名称',
  `assigned` double DEFAULT NULL COMMENT '限额，单位T',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公共hbase实例表';

CREATE TABLE `t_resource_public_hdfs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ' 主键',
  `serviceid` int(11) DEFAULT NULL COMMENT '实例id',
  `hadoopusername` varchar(200) DEFAULT NULL COMMENT 'namespace用户',
  `storagequeue` varchar(255) DEFAULT NULL COMMENT '存储资源队列',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公共hdfs实例表';

CREATE TABLE `t_tenant_hadoop` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `hadoopaccount` varchar(100) DEFAULT NULL COMMENT 'hadoop账号',
  `accounttype` tinyint(4) DEFAULT NULL COMMENT '账号类型 1-管理员分配 2-实例生成',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='hadoop租户表';

CREATE TABLE `t_tenant_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenantid` int(11) DEFAULT NULL COMMENT 'hadoop租户id',
  `resourcename` varchar(200) DEFAULT NULL COMMENT '资源名称',
  `resourcetype` tinyint(4) DEFAULT NULL COMMENT '资源类型 1-hdfs 2-yarn',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='hadoop租户和资源关系表';

/*==============================================================*/
/* Table: t_resource_storm                                      */
/*==============================================================*/
CREATE TABLE `t_resource_storm` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `serviceid`  int COMMENT '实例id' ,
  `nimbusvcore`  int COMMENT 'Nimbus的vcore' ,
  `nimbusmem`  int COMMENT 'Nimbus的内存' ,
  `nimbusnum`  int COMMENT 'Nimbus的台数' ,
  `supervisorvcore`  int COMMENT 'Supervisor的vcore' ,
  `supervisormem`  int COMMENT 'Supervisor的内存' ,
  `supervisornum`  int COMMENT 'Supervisor的台数' ,
  `servervcore`  int COMMENT 'UI Server的vcore' ,
  `servermem`  int COMMENT 'UI Server的内存' ,
  `servernum`  int COMMENT 'UI Server的台数' ,
  `workermem`  int COMMENT 'Worker的内存' ,
  `workernum`  int COMMENT 'Worker的台数' ,
  `calqueuename`  varchar(200) COMMENT '计算资源队列' ,
  `visituser`  varchar(60) COMMENT '访问用户名' ,
  `createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `keytabpath`  varchar(200) COMMENT 'keytab路径' ,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Storm实例表';

/*==============================================================*/
/* Table: t_base_config                                      */
/*==============================================================*/
CREATE TABLE `t_base_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instancetype` varchar(100) COMMENT '实例类型',
  `config` varchar(100) COMMENT '配置项',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基本配置表';

/*==============================================================*/
/* Table: t_resource_gbase                                      */
/*==============================================================*/
CREATE TABLE `t_resource_gbase` (
`id`  int(11) NOT NULL ,
`serviceid`  int(11) NULL ,
`businesstype`  varchar(1024) NULL ,
`databasename`  varchar(1024) NULL ,
`groupid`  int(11) NULL ,
`createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

