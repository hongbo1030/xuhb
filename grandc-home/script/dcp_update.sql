#增加Storm的url字段
ALTER TABLE `t_service`
ADD COLUMN `url`  varchar(128) NULL COMMENT 'Storm实例Url' AFTER `username`;


#2016-09-07 增加
# 增加ZK集群选择项目
insert into t_sys_config (`name`, `value`, `unit`, `desc`)
values ('zookeeper_connections', 'dcp11:2181,dcp12:2181,dcp13:2181', NULL, '1个/多个zk集群连接信息,多个之间以分号分隔');
   
#增加zk集群和路径
ALTER TABLE `t_resource_hbase`
ADD COLUMN `zookeeperclusters`  varchar(200) NULL AFTER `createtime`,
ADD COLUMN `zookeeperpath`  varchar(200) NULL AFTER `zookeeperclusters`;

#2016-09-12 增加
#增加zk集群和路径
ALTER TABLE `t_resource_storm`
ADD COLUMN `zookeeperclusters`  varchar(200) NULL AFTER `keytabpath`,
ADD COLUMN `zookeeperpath`  varchar(200) NULL AFTER `zookeeperclusters`;

#增加基本配置默认值
ALTER TABLE `t_base_config`
ADD COLUMN `configvalue`  integer(100) NULL AFTER `config`;

#2016-09-13 增加
#新增storm拓扑管理表
drop table if exists t_storm_topology;
CREATE TABLE `t_storm_topology` (
 id         bigint not null auto_increment,
`topologyname`  varchar(100) NULL COMMENT '拓扑名称' ,
`topologypath`  varchar(200) NULL COMMENT '上传文件路径' ,
`command`  varchar(200) NULL COMMENT '执行命令' ,
`serviceid`  int(11) NULL ,
`updatetime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP,
primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='storm拓扑管理表';


#2016-09-14 增加
#增加基本配置默认值的单位和类型
ALTER TABLE `t_base_config`
MODIFY COLUMN `configvalue`  varchar(100) NULL DEFAULT NULL AFTER `config`,
ADD COLUMN `configtype`  varchar(100) NULL AFTER `configvalue`,
ADD COLUMN `configunit`  varchar(100) NULL AFTER `configtype`;

ALTER TABLE `t_common_config`
ADD COLUMN `configtype`  varchar(100) NULL AFTER `configvalue`,
ADD COLUMN `configunit`  varchar(100) NULL AFTER `configtype`;


ALTER TABLE `t_cal_queue_info` 
ADD COLUMN `labeldesc`  varchar(3) NULL COMMENT '1: 表示长任务 2 :表示短任务' AFTER `userid`;

ALTER TABLE `t_resource_apply_approve` 
ADD COLUMN `old_labeldesc`  varchar(3) NULL COMMENT '原资源标签 1: 表示长任务 2 :表示短任务' AFTER `old_maxvcore`;

ALTER TABLE `t_resource_apply_approve` 
ADD COLUMN `new_labeldesc`  varchar(3) NULL COMMENT '新资源标签 1: 表示长任务 2 :表示短任务' AFTER `new_maxvcore`;

#主机配置增加字段
ALTER TABLE `t_conf_machine`
ADD COLUMN `storagetype`  varchar(100) NULL COMMENT '存储类型' AFTER `groupid`,
ADD COLUMN `hostmodel`  varchar(200) NULL COMMENT '主机型号' AFTER `storagetype`,
ADD COLUMN `supervisoremail`  varchar(100) NULL COMMENT '紧急联系人邮箱' AFTER `hostmodel`,
ADD COLUMN `supervisorphone`  varchar(100) NULL COMMENT '紧急联系人电话' AFTER `supervisoremail`;

#2016-09-19 增加
#增加文件名称
ALTER TABLE `t_storm_topology`
MODIFY COLUMN `topologypath`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传文件路径' AFTER `topologyname`,
ADD COLUMN `filename`  varchar(100) NULL AFTER `updatetime`;

#2016-09-20 增加
#修改字段长度
ALTER TABLE `t_conf_machine`
MODIFY COLUMN `supervisorid`  varchar(1024) NULL DEFAULT NULL AFTER `harddisk`;

#2016-09-20 add by sunxl3
#增加集群配置菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600105','集群配置','6001',NULL,'3','5',NULL,'acluster');

#把集群配置菜单给role 1
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600105);

#计算资源队列增加字段
ALTER TABLE `t_cal_queue_info`
ADD COLUMN `minper` float COMMENT '最小百分比' AFTER `state`,
ADD COLUMN `maxper` float COMMENT '最大百分比' AFTER `minper`,
MODIFY COLUMN `minvcore` double(11,1) after maxmem,
MODIFY COLUMN `maxvcore` double(11,1) after minvcore;

#2016-09-26 add by chenyf7
#初始化基本配置数据
INSERT INTO `t_base_config` VALUES ('1', '8', 'hbase.regionserver.handler.count', '10', '1', '秒');
INSERT INTO `t_base_config` VALUES ('3', '8', 'hbase.client.write.buffer', '2097152', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('5', '8', 'hbase.client.retries.number', '10', '1', '秒');
INSERT INTO `t_base_config` VALUES ('7', '8', 'hbase.client.scanner.caching', '1', '1', '秒');
INSERT INTO `t_base_config` VALUES ('9', '8', 'hbase.client.keyvalue.maxsize', '10485760', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('11', '8', 'hbase.regionserver.lease.period', '60000', '1', '秒');
INSERT INTO `t_base_config` VALUES ('13', '11', 'nimbus.task.timeout.secs', '30', '1', '秒');
INSERT INTO `t_base_config` VALUES ('15', '11', 'nimbus.monitor.freq.secs', '10', '1', '秒');
INSERT INTO `t_base_config` VALUES ('17', '11', 'nimbus.supervisor.timeout.secs', '60', '1', '秒');
INSERT INTO `t_base_config` VALUES ('19', '11', 'nimbus.task.launch.secs', '120', '1', '秒');
INSERT INTO `t_base_config` VALUES ('21', '11', 'topology.workers', '1', '1', '');
INSERT INTO `t_base_config` VALUES ('23', '11', 'topology.max.task.parallelism', '1', '1', null);


#新增gbase资源表
drop table if exists t_resource_gbase;
CREATE TABLE `t_resource_gbase` (
`id`  int(11) NOT NULL ,
`serviceid`  int(11) NULL ,
`businesstype`  varchar(1024) NULL ,
`databasename`  varchar(1024) NULL ,
`groupid`  int(11) NULL ,
`createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

ALTER TABLE `t_resource_gbase`
MODIFY COLUMN `serviceid`  int(11) NULL DEFAULT NULL COMMENT '服务id' AFTER `id`,
MODIFY COLUMN `businesstype`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选择模式 1 表示主仓 2 表示集市' AFTER `serviceid`,
MODIFY COLUMN `databasename`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称' AFTER `businesstype`,
MODIFY COLUMN `groupid`  int(11) NULL DEFAULT NULL COMMENT '关联资源组id' AFTER `databasename`,
MODIFY COLUMN `createtime`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `groupid`;


#2016-09-27 add by chenyf7
#Mpp资源表  
drop table if exists t_service_gbase_groupinfo;
CREATE TABLE `t_service_gbase_groupinfo` (
`id`  int(11) NOT NULL COMMENT '主键' ,
`groupname`  varchar(1024) NULL COMMENT '组名称' ,
`groupcpu`  varchar(1024) NULL COMMENT '组cpu数量' ,
`priorityvalue`  varchar(1024) NULL COMMENT '优先级,0123,越大越高。如果指定失败或未指定默认为2。' ,
`groupvalue`  varchar(1024) NULL COMMENT '组id，暂定1-15,0默认' ,
`price`  varchar(1024) NULL COMMENT '价格' ,
PRIMARY KEY (`id`)
)
;

#2016-09-27 add by sunxl3
#计算资源队列修改字段大小
ALTER TABLE `t_cal_queue_info`
MODIFY COLUMN `remark` varchar(2000) after usedmem,
MODIFY COLUMN `queuename` varchar(128) after id;
ALTER TABLE `t_quo_space_queue`
MODIFY COLUMN `description` varchar(2000) after assigned;


#2016-09-28 add by lingyf--资源管理增加MPP资源
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300103','MPP主仓','3001',NULL,'3','3',NULL,'amainStore');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300203','MPP主仓','3002',NULL,'3','3',NULL,'myamainStore');
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300103);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300203);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 300203);


#2016-09-28 add by sunxl
ALTER TABLE `t_resource_apply_approve`
MODIFY COLUMN `apply_desc` varchar(2000) after approver_id;

#2016-09-27 add by chenyf7
#Mpp资源表 增加字段
ALTER TABLE `t_service_gbase_groupinfo`
ADD COLUMN `type`  int(11) NULL COMMENT '类型 1表示主仓 2表示集市' AFTER `price`,
ADD COLUMN `userid`  int(11) NULL COMMENT '租户id' AFTER `type`;

#2016-09-29 add by sunxl3
ALTER TABLE `t_conf_metric_group`
ADD COLUMN `metric_type` int(11) NULL COMMENT '指标类型 1集群指标 2实例指标 3主机指标' AFTER group_type;

#2016-09-29 add by chenyf7
#主键设置为自动增长
ALTER TABLE `t_service_gbase_groupinfo`
MODIFY COLUMN `id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' FIRST ;

ALTER TABLE `t_resource_gbase`
MODIFY COLUMN `id`  int(11) NOT NULL AUTO_INCREMENT FIRST ;

#2016-10-11 add by chenyf7
#实例表描述字段类型修改
ALTER TABLE `t_service`
MODIFY COLUMN `remark`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述' AFTER `type`;

#2016-10-13 增加
# 增加Redis节点同步超时时间
insert into t_sys_config (`name`, `value`, `unit`, `desc`)
values ('redis_repl_timeout', '1800', NULL, 'redis节点同步超时时间，设为半小时，原默认为60s');

#2016-10-13 add by chenyf7
#更新告警监控模块菜单项
UPDATE `t_sys_priv` SET `name`='告警中心', `key`='alarmCentre' WHERE (`id`='4001');
UPDATE `t_sys_priv` SET `name`='实例监控', `key`='jointShow' WHERE (`id`='4002');
UPDATE `t_sys_priv` SET `name`='集群监控', `key`='clusterMonitor' WHERE (`id`='4003');
UPDATE `t_sys_priv` SET `name`='主机监控', `key`='hostMonitor' WHERE (`id`='4004');
UPDATE `t_sys_priv` SET `name`='故障知识库', `key`='fault' WHERE (`id`='4005');

#2016-10-24 add by chenyf7
#角色表描述字段类型修改
ALTER TABLE `t_sys_role`
MODIFY COLUMN `dscp`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '角色描述' AFTER `resoursepriv`;

#角色表name字段长度修改
ALTER TABLE `t_sys_role`
MODIFY COLUMN `name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名' AFTER `id`;

#2016-10-27 add by chenyf7
#初始化集群配置左侧栏数据
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(1, 'Hadoop集群', 0, 1, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(2, 'Namenode', 1, 2, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(3, 'Datanode', 1, 2, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(4, 'Resourcemanager', 1, 2, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(5, 'Nodemanager', 1, 2, 1, 1);

insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(6, 'Zookeeper集群', 0, 1, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(7, 'GBase集群', 0, 1, 1, 1);

insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(8, 'Kafka', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(9, 'Storm', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(10, 'HBase', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(11, 'Redis', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(12, 'GBase', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(13, 'Hive', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(14, 'Spark', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(15, 'Spark', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(16, 'FTP', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(17, 'MySQL', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(18, 'Tomcat', 0, 1, 1, 2);

#storm拓扑表增加状态字段
ALTER TABLE `t_storm_topology`
ADD COLUMN `status`  varchar(200) NULL COMMENT '状态' AFTER `filename`;

#2016-10-27 add by chenyf7
#删除多余菜单，故障知识库，系统配置，平台配置，服务配置，紧急处理脚本配置
DELETE FROM `t_sys_priv` WHERE (`id`='4005');
DELETE FROM `t_sys_priv` WHERE (`id`='600101');
DELETE FROM `t_sys_priv` WHERE (`id`='600103');
DELETE FROM `t_sys_priv` WHERE (`id`='600104');
DELETE FROM `t_sys_priv` WHERE (`id`='600303');

#2016-11-04 add by chenyf7
#修改告警通知相关表字段类型
ALTER TABLE `t_alarm_rule_times`
MODIFY COLUMN `alarm_team_id`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `interval_time`;

ALTER TABLE `t_alarm_message`
MODIFY COLUMN `alarm_team_id`  varchar(500) NULL DEFAULT NULL AFTER `metric_create_time`;

ALTER TABLE `t_alarm_message_temp`
MODIFY COLUMN `alarm_team_id`  varchar(500) NULL DEFAULT NULL AFTER `notice_time`;


#新增默认指标

delete from t_conf_metric_group where id in (1,4,5,20,21);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(1, 'HDFS', 0, 1, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(20, 'ambari', 0, 1, 1, 2);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(21, 'yarn', 0, 1, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(4, 'Resourcemanager', 21, 2, 1, 1);
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(5, 'Nodemanager', 21, 2, 1, 1);

DELETE FROM t_conf_metric;

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('CPU空闲百分比', 'host@cpu@idle', 1, 19, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('CPU个数', 'host@cpu@total', 1, 19, 1, '个', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('空闲交换分区', 'host@swap@free', 1, 19, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('空闲内存', 'host@mem@free', 1, 19, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('内存总量', 'host@mem@total', 1, 19, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('交换分区总量', 'host@swap@total', 1, 19, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('空闲存储', 'host@disk@free', 1, 19, 1, 'GB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('磁盘总量', 'host@disk@total', 1, 19, 1, 'GB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('网络输出', 'host@net@out', 1, 19, 1, 'bytes/sec', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('网络输入', 'host@net@in', 1, 19, 1, 'bytes/sec', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('系统启动时间', 'host@os@boottime', 1, 19, 1, 's', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('操作系统名称', 'host@os@name', 1, 19, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ntp时间差额', 'host@ntp@offset', 1, 19, 1, 's', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('系统状态', 'host@os@state', 1, 19, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('操作系统版本', 'host@os@release', 1, 19, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('系统启动时间', 'host@os@boottime', 1, 19, 1, 's', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Hdfs存储总量', 'pf@dfs@storage@total', 1, 1, 1, 'GB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Hdfs空闲存储', 'pf@dfs@storage@free', 1, 1, 1, 'GB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Hdfs已使用存储', 'pf@dfs@storage@used', 1, 1, 1, 'GB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Hdfs Block块数量', 'pf@dfs@block@total', 1, 1, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('缺少副本的块的数量', 'pf@dfs@block@under', 1, 1, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('丢失的块的数量', 'pf@dfs@block@missing', 1, 1, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('坏掉的块的数量', 'pf@dfs@block@corrupt', 1, 1, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('active的namenode', 'pf@dfs@activeNameNode', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('standby的namenode', 'pf@dfs@standbyNameNode', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Decommissione的datanode数量', 'pf@dfs@NumDecommissioningDataNodes', 1, 3, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('Volume failed的datanode数量', 'pf@dfs@datanode@NumFailedVolumes', 1, 3, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('块大小', 'pf@dfs@block@size', 1, 1, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('节点Hdfs存储总量', 'pf@dfs@datanode@total', 1, 3, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('节点Hdfs空闲存储', 'pf@dfs@datanode@free', 1, 3, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('节点Hdfs已使用存储', 'pf@dfs@datanode@used', 1, 3, 1, 'KB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper连接数', 'pf@zookeeper@conn@count', 1, 6, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper被监控的path数', 'pf@zookeeper@watched@path@count', 1, 6, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper的watcher数', 'pf@zookeeper@watcher@count', 1, 6, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper访问延迟最小值', 'pf@zookeeper@latency@min', 1, 6, 1, 'ms', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper访问延迟平均值', 'pf@zookeeper@latency@avg', 1, 6, 1, 'ms', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper访问延迟最大值', 'pf@zookeeper@latency@max', 1, 6, 1, 'ms', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper打开文件句柄数', 'pf@zookeeper@open@file', 1, 6, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper打开文件理论最大数', 'pf@zookeeper@max@file', 1, 6, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('当前机器zookeeper数据库大小', 'pf@zookeeper@data@size', 1, 6, 1, 'KB', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('hivemetastore的cpu使用百分比', 'pf@proc@hivemetastore@cpu', 1, 13, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('hivemetastore的运行时长', 'pf@proc@hivemetastore@etime', 1, 13, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('hivemetastore使用的内存', 'pf@proc@hivemetastore@mem', 1, 13, 1, 'bytes', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('hivemetastore的pid', 'pf@proc@hivemetastore@pid', 1, 13, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('hivemetastore的程序状态', 'pf@proc@hivemetastore@state', 1, 13, 1, '%', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariagent的cpu使用百分比', 'pf@proc@ambariagent@cpu', 1, 20, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariagent的运行时长', 'pf@proc@ambariagent@etime', 1, 20, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariagent使用的内存', 'pf@proc@ambariagent@mem', 1, 20, 1, 'bytes', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariagent的pid', 'pf@proc@ambariagent@pid', 1, 20, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariagent的程序状态', 'pf@proc@ambariagent@state', 1, 20, 1, '%', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariserver的cpu使用百分比', 'pf@proc@ambariserver@cpu', 1, 20, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariserver的运行时长', 'pf@proc@ambariserver@etime', 1, 20, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariserver使用的内存', 'pf@proc@ambariserver@mem', 1, 20, 1, 'bytes', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariserver的pid', 'pf@proc@ambariserver@pid', 1, 20, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('ambariserver的程序状态', 'pf@proc@ambariserver@state', 1, 20, 1, '%', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('default队列的资源最大值', 'pf@yarn@queue@default@max', 1, 21, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('default队列的资源最小值', 'pf@yarn@queue@default@min', 1, 21, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('default队列的已使用资源', 'pf@yarn@queue@default@used', 1, 21, 1, '%', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('default队列的已使用内存', 'pf@yarn@queue@default@usedmemory', 1, 21, 1, 'MB', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('default队列的已使用vcore', 'pf@yarn@queue@default@vcores', 1, 21, 1, '个', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('active的resourcemanager', 'pf@yarn@activeresourcemanage', 1, 4, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('standby的resourcemanager', 'pf@yarn@standbyresourcemanage', 1, 4, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('aitive状态的nodemanager数量', 'pf@yarn@queues@NodeManager@NumActiveNMs', 1, 5, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`,  `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('黑名单中的nodemanager数量', 'pf@yarn@queues@NodeManager@NumRebootedNMs', 1, 5, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('unhealth的nodemanager数量', 'pf@yarn@queues@NodeManager@NumUnhealthyNMs', 1, 5, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('decommissione的nodemanager数量', 'pf@yarn@queues@NodeManager@NumDecommissionedNMs', 1, 5, 1, '', '');

insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('dead的nodemanger数量', 'pf@yarn@queues@NodeManager@NumLostNMs', 1, 5, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('yarn分配的内存总量', 'pf@yarn@mem@total', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('yarn空闲尚可分配内存', 'pf@yarn@mem@free', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('正在运行应用个数', 'pf@yarn@applicatioin@running', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('已完成应用个数', 'pf@yarn@application@completed', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('正在等待应用个数', 'pf@yarn@application@pending', 1, 2, 1, '', '');
insert into t_conf_metric(`metric_name`, `metric_key`, `metric_property`, `metric_group_id`, `metric_type`, `unit`, `remark`) 
values('提交应用个数', 'pf@yarn@application@submitted', 1, 2, 1, '', '');

#2016-11-07 add by chenyf7
#修改告警通知人描述字段类型
ALTER TABLE `t_conf_alarm_user`
MODIFY COLUMN `remark`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `email`;

#2016-11-10 add by chenyf7
#新增kafka实例表
drop table if exists t_resource_kafka;

CREATE TABLE `t_resource_kafka` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `serviceid`  int COMMENT '实例id' ,
  `brokervcore`  int COMMENT 'Broker的vcore' ,
  `brokermem`  int COMMENT 'Broker的内存' ,
  `brokernum`  int COMMENT 'Broker的个数' ,
  `zookeeperclusters`  varchar(200) COMMENT 'ZK集群名称' ,
  `zookeeperpath`  varchar(200) COMMENT 'ZK集群路径' ,
  `calqueuename`  varchar(200) COMMENT '计算资源队列' ,
  `storagequeue`  varchar(60) COMMENT '存储队列' ,
  `createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Kafka实例表';

#2016-11-14 add by chenyf7
#修改字段类型
ALTER TABLE `t_sys_user`
MODIFY COLUMN `remark`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述' AFTER `endtime`;


#2016-11-17 add by chenyf7
#增加status
ALTER TABLE `t_resource_apply_approve`
ADD COLUMN `status`  varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态 1:未删除 2:已删除 ' AFTER `parentqueueid`;

insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) 
values(19, '主机指标', 0, 1, 1, 3);

#2016-11-29 add by chenyf7
#修改账号字段类型 区分大小写
ALTER TABLE `t_sys_user`
MODIFY COLUMN `name`  varchar(50) binary;


#2016-12-28 add by chenyf7
#新增紧急处理相关表
drop table if exists T_ALARM_RULE_EXECUTE_LOG;
CREATE TABLE `T_ALARM_RULE_EXECUTE_LOG` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`rule_id`  int(11) NULL COMMENT '告警处理表id值',
`result`  varchar(4000) NULL COMMENT '正常运行的执行结果' ,
`execute_time`  timestamp NULL COMMENT '开始命令的时间' ,
`end_time`  timestamp NULL  COMMENT '执行结束的时间' ,
`used_time`  int(11) NULL COMMENT '执行的毫秒数，用于排序' ,
`ip` varchar(128) NULL COMMENT '执行命令的机器ip' ,
`success` varchar(1) NULL COMMENT '0成功其他失败' ,
`alarm_id` varchar(128) NULL COMMENT '触发脚本执行的告警的id', 

primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='紧急处理脚本执行结果表';


drop table if exists T_ALARM_RULE_CONF;
CREATE TABLE `T_ALARM_RULE_CONF` (
 `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`username`  varchar(128) NULL COMMENT '执行命令的用户名' ,
`password` varchar(128) NULL COMMENT '执行命令的密码' ,
`cmd` varchar(4000) NULL COMMENT '需要执行的命令' ,
`machine_ips` varchar(1024) NULL COMMENT '需要执行命令的机器ip，多个逗号分隔',
`rule_id`  int(11) NULL COMMENT '所属规则id',

primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='执行命令配置表';


#2016-12-29 add by chenyf7
#新增紧急处理相关菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600303','紧急处理脚本配置','6003',NULL,'3','3',NULL,'ascript');

#新增脚本配置表
drop table if exists T_ALARM_RULE_SCRIPT;
CREATE TABLE `T_ALARM_RULE_SCRIPT` (
 `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`file_name`  varchar(50) NULL COMMENT '脚本文件名称' ,
`path` varchar(50) NULL COMMENT '脚本名称' ,
`cmd` varchar(1024) NULL COMMENT '脚本存储绝对路径' ,
`machine_ids` varchar(4000) NULL COMMENT '关联主机id,逗号分隔',
`rule_id`  int(11) NULL COMMENT '告警规则id值，t_alarm_rule.id',

primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='脚本配置表';

drop table if exists T_ALARM_RULE_SCRIPT_LOG;
CREATE TABLE `T_ALARM_RULE_SCRIPT_LOG` (
 `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`script_id`  int(11) NULL COMMENT '告警处理脚本表id值，t_alarm_rule_script.id' ,
`content` varchar(4000) NULL COMMENT '脚本处理日志' ,
`create_time` timestamp NULL COMMENT '记录生成时间' ,

primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='脚本执行日志表';

# 2016-12-31 by lingyf 增加kafka log.dirs 主机地址前缀目录，多个目录以逗号分隔
insert into t_sys_config (`name`, `value`, `unit`, `desc`)
values ('kafka_log_dirs', '/data/kafka', NULL, '存储kafka日志文件的本地目录前缀');

#2017-1-3 add by lingyf
#初始化kafka实例基本配置项
INSERT INTO `t_base_config` VALUES ('25', '12', 'num.network.threads', '3', '1', '');
INSERT INTO `t_base_config` VALUES ('27', '12', 'num.io.threads', '8', '1', '');
INSERT INTO `t_base_config` VALUES ('29', '12', 'socket.send.buffer.bytes', '102400', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('31', '12', 'socket.receive.buffer.bytes', '102400', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('33', '12', 'socket.request.max.bytes', '104857600', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('35', '12', 'num.partitions', '1', '1', '');
INSERT INTO `t_base_config` VALUES ('37', '12', 'num.recovery.threads.per.data.dir', '1', '1', '');
INSERT INTO `t_base_config` VALUES ('39', '12', 'log.retention.hours', '168', '1', '小时');
INSERT INTO `t_base_config` VALUES ('41', '12', 'log.segment.bytes', '1073741824', '1', 'byte');
INSERT INTO `t_base_config` VALUES ('43', '12', 'log.retention.check.interval.ms', '300000', '1', 'ms');

#2016-12-20 add by xuhb
#新增组织机构表
drop table if exists t_sys_group;
CREATE TABLE `t_sys_group` (
 groupid         int not null auto_increment,
`groupname`  varchar(50) not NULL comment '组织机构唯一标识。大小写英文、字母',
`groupcnname`  varchar(100) not NULL COMMENT '组织机构名称',
fathergroupid int not NULL default 0 comment '0表示没有父节点。用于和groupid表示父子关系',
`remark`  varchar(150) comment '描述',
`updatetime`  timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
primary key (groupid),
unique key uk_t_sys_group (groupname)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构表';

#新增组织机构和用户关联表
drop table if exists t_sys_groupuser;
CREATE TABLE `t_sys_groupuser` (
 groupuserid         int not null auto_increment,
 `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联',
`userid`  int not NULL comment '用户编号。和t_sys_user.id关联',
roleid int not NULL default 0 comment '用户在组织机构中的角色。2-组织结构管理员;3-普通成员',
`updatetime`  timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
primary key (groupuserid),
unique key uk_t_sys_groupuser (groupid, userid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构和用户关联表';

ALTER TABLE `t_service`
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

ALTER TABLE t_cal_queue_info
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

ALTER TABLE t_quo_space_queue
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

ALTER TABLE t_service_gbase_groupinfo
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

ALTER TABLE t_resource_apply_approve
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

#增加部门配置菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600203','部门配置','6002',NULL,'3','3',NULL,'agroup');
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600203);
commit;

ALTER TABLE t_tenant_hadoop
ADD COLUMN `groupid`  int not NULL COMMENT '用户组编号。和t_sys_group.groupid关联';

#2016-12-23 add by xuhb
#新增DACP登录表
drop table if exists t_sys_dacplogin;
CREATE TABLE `t_sys_dacplogin` (
 loginid         int not null auto_increment,
`username`  varchar(50) not NULL comment '用户登录名.t_sys_user.name',
`dt`  varchar(14) not NULL COMMENT 'dacp登录时带入的dt.yyyymmddmiss格式的时间戳',
`token`  varchar(50) not NULL COMMENT 'dacp登录时带入的token.校验位',
primary key (loginid),
unique key uk_t_sys_dacplogin (username)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DACP登录表';

#2017-1-5 add by lingyf
#优化指标历史表，增加时间索引和分区，分区会由定时器定时增加
drop table if EXISTS t_metric_history;
CREATE TABLE `t_metric_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL,
  `port` varchar(50) DEFAULT NULL,
  `metric_key` varchar(200) NOT NULL,
  `metric_value` varchar(1024) DEFAULT NULL,
  `title` varchar(500) DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `collect_time` datetime DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (id,update_time),
  INDEX indxkey (metric_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
partition by range(to_days(update_time))
( 
partition P_T_METRIC_HISTORY_20170101 values less than (to_days('2017-01-01')),
partition P_T_METRIC_HISTORY_20170102 values less than (to_days('2017-01-02')),
partition P_T_METRIC_HISTORY_20170103 values less than (to_days('2017-01-03')),
partition P_T_METRIC_HISTORY_20170104 values less than (to_days('2017-01-04')),
partition P_T_METRIC_HISTORY_20170105 values less than (to_days('2017-01-05')),
partition P_T_METRIC_HISTORY_20170106 values less than (to_days('2017-01-06')),
partition P_T_METRIC_HISTORY_20170107 values less than (to_days('2017-01-07')),
partition P_T_METRIC_HISTORY_20170108 values less than (to_days('2017-01-08')),
partition P_T_METRIC_HISTORY_20170109 values less than (to_days('2017-01-09')),
partition P_T_METRIC_HISTORY_20170110 values less than (to_days('2017-01-10')),
partition P_T_METRIC_HISTORY_20170111 values less than (to_days('2017-01-11')),
partition P_T_METRIC_HISTORY_20170112 values less than (to_days('2017-01-12')),
partition P_T_METRIC_HISTORY_20170113 values less than (to_days('2017-01-13')),
partition P_T_METRIC_HISTORY_20170114 values less than (to_days('2017-01-14')),
partition P_T_METRIC_HISTORY_20170115 values less than (to_days('2017-01-15')),
partition P_T_METRIC_HISTORY_20170116 values less than (to_days('2017-01-16')),
partition P_T_METRIC_HISTORY_20170117 values less than (to_days('2017-01-17')),
partition P_T_METRIC_HISTORY_20170118 values less than (to_days('2017-01-18')),
partition P_T_METRIC_HISTORY_20170119 values less than (to_days('2017-01-19')),
partition P_T_METRIC_HISTORY_20170120 values less than (to_days('2017-01-20')),
partition P_T_METRIC_HISTORY_20170121 values less than (to_days('2017-01-21')),
partition P_T_METRIC_HISTORY_20170122 values less than (to_days('2017-01-22')),
partition P_T_METRIC_HISTORY_20170123 values less than (to_days('2017-01-23')),
partition P_T_METRIC_HISTORY_20170124 values less than (to_days('2017-01-24')),
partition P_T_METRIC_HISTORY_20170125 values less than (to_days('2017-01-25')),
partition P_T_METRIC_HISTORY_20170126 values less than (to_days('2017-01-26')),
partition P_T_METRIC_HISTORY_20170127 values less than (to_days('2017-01-27')),
partition P_T_METRIC_HISTORY_20170128 values less than (to_days('2017-01-28')),
partition P_T_METRIC_HISTORY_20170129 values less than (to_days('2017-01-29')),
partition P_T_METRIC_HISTORY_20170130 values less than (to_days('2017-01-30')),
partition P_T_METRIC_HISTORY_20170131 values less than (to_days('2017-01-31'))
);

# 增加字段表明消息是否已通知
ALTER TABLE `t_alarm_message`
add COLUMN status int NOT NULL DEFAULT 0 COMMENT '消息发送状态.0-未发送;2-发送成功';
ALTER TABLE `t_alarm_message`
add COLUMN sendtime datetime NULL DEFAULT NULL COMMENT '消息发送时间.';

#增加hue菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('80','HUE','1',NULL,'1','8',NULL,'hue');
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 80);
commit;

#gbase增加用户名密码字段
ALTER TABLE t_resource_gbase
ADD COLUMN gbaseusername  varchar(128) NULL COMMENT 'gbase用户名';
ALTER TABLE t_resource_gbase
ADD COLUMN gbasepassword  varchar(128) NULL COMMENT 'gbase密码，采取base64存储';

#接口默认角色
insert into t_sys_role (`id`, `name`, `type`, `resoursepriv`, `dscp`) values (98, '接口默认角色', 3, 0, '');
delete from t_sys_role_priv where roleid=98;
insert into t_sys_role_priv(roleid,privid) select 98,privid from t_sys_role_priv where roleid=1;
commit;

#2017-2-9 add by lingyf
#增加kafka实例delete.topic.enable配置项
INSERT INTO `t_base_config` VALUES ('45', '12', 'delete.topic.enable', 'false', '4', '');
commit;

#2017-2-27 add by lingyf
#去掉集群配置里多余的spark组
delete from t_conf_metric_group where id=15;
commit;

#2017-2-28 add by chenyf7
#去掉集群配置中ftp、mysql、tomcat
delete from t_conf_metric_group where id=16;
delete from t_conf_metric_group where id=17;
delete from t_conf_metric_group where id=18;

UPDATE `t_conf_metric_group` SET `metric_type`='1' WHERE (`id`='20');
commit;

#2017-3-7 add by chenyf7
#新增t_resource_mysql表
drop table if exists t_resource_mysql;

CREATE TABLE `t_resource_mysql` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `serviceid`  int COMMENT '实例id' ,
  `servervcore`  int COMMENT 'server的vcore' ,
  `servermem`  int COMMENT 'server的内存' ,
  `serverrnum`  int COMMENT 'server的个数' ,
   `adminaccount`  varchar(200) COMMENT 'admin账号' ,
  `adminpassword`  varchar(200) COMMENT 'admin密码' ,
  `calqueuename`  varchar(200) COMMENT '计算资源队列' ,
  `createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Mysql实例表';

#2017-3-9 add by chenyf7
#初始化flume集群数据
insert into t_conf_metric_group(`id`, `metric_group_name`, `parent_id`, `group_level`, `group_type`, `metric_type`) values(22, 'Flume集群', 0, 1, 1, 1);

#初始化flume组件配置菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600106','Flume组件配置','6001',NULL,'3','6',NULL,'aflume');

insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600106);

#初始化组件相关表
drop table if exists t_conf_component;
CREATE TABLE `t_conf_component` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `name`  varchar(200) COMMENT '组件名称' ,
  `username`  varchar(200) COMMENT '访问用户' ,
  `userpassword`  varchar(200) COMMENT '访问密码' ,
  `remark`  text COMMENT '申请描述' ,
  `userid`  int COMMENT '创建者用户id' ,
  `type`  varchar(200) COMMENT '组件类型 ' ,
  `createtime`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组件配置表';


drop table if exists t_conf_component_host;
CREATE TABLE `t_conf_component_host` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `ip`  varchar(100) COMMENT 'ip' ,
  `port`  varchar(100) COMMENT '端口' ,
  `role`  varchar(200) COMMENT '机器角色 ' ,
  `componentid`  int COMMENT 't_conf_component表id' ,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组件关联主机表';

drop table if exists t_conf_component_group;
CREATE TABLE `t_conf_component_group` (
  `id`  int NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
  `groupid`  int COMMENT '部门id' ,
  `groupname`  varchar(200) COMMENT '部门名称' ,
  `componentid`  int COMMENT 't_conf_component表id' ,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组件关联部门表';


#2017-3-20 add by chenyf7
#新增hadoop租户与用户关系表
CREATE TABLE `t_tenant_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenantid` int(11) DEFAULT NULL COMMENT 'hadoop租户id',
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `name`  varchar(200) COMMENT '用户名称' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='hadoop租户与用户关系表';


#2017-3-28 add by chenyf7
#初始化组件监控
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4006','组件监控','40',NULL,'2','6',NULL,'componentMonitor');

insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4006);

#2017-3-31 add by xuhb
delete from t_sys_role_priv where roleid=98 and privid in (3001,300101,300102,300103,50);
commit;

#2017-4-5 add by chenyf7

ALTER TABLE `t_resource_redis`
ADD COLUMN `ispersistence`  varchar(50) NULL AFTER `calqueuename`,
ADD COLUMN `copynum`  int(11) NULL AFTER `ispersistence`;

drop table if exists t_redis_node_relation;
create table t_redis_node_relation
(
  id          int(11) not null AUTO_INCREMENT COMMENT '自增主键',
  appname     varchar(100) COMMENT '实例名称，字母格式',
  sub_appname varchar(100) COMMENT '实例名称，字母格式',  
  nodeId 	  varchar(100) COMMENT '节点ID',
  filePath        varchar(200) COMMENT '映射目录',
  ip        varchar(100) COMMENT 'ip',
  port        varchar(100) COMMENT 'port',
  dockerContainerName        varchar(200) COMMENT 'docker容器名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Redis的nodeId的映射关系表';

ALTER TABLE `t_service`
ADD COLUMN `updatetime` datetime DEFAULT NULL COMMENT '更新时间';

# dcp共享根目录
insert into t_sys_config (`name`, `value`, `unit`, `desc`)
values ('dcp_data_root_path', '/data/dcpdata', NULL, 'dcp共享根目录');


#2017-5-6 add by wangyl5
# 去掉Gbase菜单相关
delete from t_conf_metric_group where id=7 or id=12;
delete from t_sys_priv where id=300103;
delete from t_sys_priv where id=300203;
delete from t_sys_role_priv where privid=300103 or privid=300203;

# 去掉Flume菜单相关
delete from t_conf_metric_group where id=22;
delete from t_sys_priv where id=600106;
delete from t_sys_role_priv where privid=600106;


#2017-5-8 add by wangyl5
# 去掉组件监控菜单相关
delete from t_sys_priv where id=4006;
delete from t_sys_role_priv where privid=4006;

#2017-05-23 add by lingyf
#修改系统配置表字段长度
alter table t_sys_config change value value varchar(512) DEFAULT NULL;

#2017-6-26 add by lingyf
#增加集群配置表
drop table if exists t_conf_cluster;
create table t_conf_cluster
(
  id          int(11) not null AUTO_INCREMENT COMMENT '自增主键',
  name     varchar(100) COMMENT '集群名称',
  remark        varchar(200) COMMENT '集群描述',
  hadoopurl		varchar(200) COMMENT 'hadoop manager url地址',
  hbaseurl		varchar(200) COMMENT 'hbase manager url地址',
  redisurl		varchar(200) COMMENT 'redis manager url地址',
  stormurl		varchar(200) COMMENT 'storm manager url地址',
  kafkaurl      varchar(200) COMMENT 'kafka manager url地址',
  updatetime    varchar(100) comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='dcp多集群信息配置表';

#2017-6-26 add by lingyf
#系统配置表增加集群id列
alter table t_sys_config add COLUMN clusterid int(11) not null COMMENT '集群id';
#存储队列表增加集群id列
alter table t_quo_space_queue add COLUMN clusterid int(11) not null COMMENT '集群id';
#资源申请表增加集群id列
alter table t_resource_apply_approve add COLUMN clusterid int(11) not null COMMENT '集群id';
#计算队列表增加集群id列
alter table t_cal_queue_info add COLUMN clusterid int(11) not null COMMENT '集群id';

#2017-7-3 add by lingyf
#去掉首页菜单
delete from t_sys_priv where id=10 or id=1001 or id=1002;
#去掉HUE菜单
delete from t_sys_priv where id=80;
#2017-7-3 add by lingyf
#租户账号表增加集群id列
alter table t_tenant_hadoop add COLUMN clusterid int(11) not null COMMENT '集群id';

#2017-7-4 add by wangyl5
alter table t_service add COLUMN clusterid int(11) not null COMMENT '集群id';

#2017-07-05 增加
# 增加ZK集群选择项目
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('zookeeper_connections', 'dcp15:2181,dcp241:2181,dcp79:2181', NULL, '1个/多个zk集群连接信息,多个之间以分号分隔',1);
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('zookeeper_connections', 'dcp11:2181,dcp12:2181,dcp13:2181', NULL, '1个/多个zk集群连接信息,多个之间以分号分隔',2);
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('zookeeper_connections', 'dcp15:2181,dcp241:2181,dcp79:2181', NULL, '1个/多个zk集群连接信息,多个之间以分号分隔',3);
# 增加hdfs集群选择项目
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('hdfs_dir_prefix', '/home/dcp', NULL, '集群hdfs前缀路径',1);
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('hdfs_dir_prefix', '/home/dcp', NULL, '集群hdfs前缀路径',2);
insert into t_sys_config (`name`, `value`, `unit`, `desc`,clusterid)
values ('hdfs_dir_prefix', '/home/dcp', NULL, '集群hdfs前缀路径',3);

#2017-07-05 add by xuhb
alter table t_conf_machine add COLUMN clusterid int(11) not null COMMENT '集群id';

#2017-07-07 add by lingyf
#增加实例表索引
ALTER  TABLE  `t_service`  ADD  UNIQUE (`id` ) ;