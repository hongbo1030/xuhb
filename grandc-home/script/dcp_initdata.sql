#预置菜单
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('10','首页','1',NULL,'1','1',NULL,'index');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('1001','管理员','10',NULL,'2','1',NULL,'admin');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('1002','租户','10',NULL,'2','2',NULL,'rent'); 
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('20','实例管理','1',NULL,'1','2',NULL,'instances');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('30','资源管理','1',NULL,'1','3',NULL,'resource');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('3001','资源池资源','30',NULL,'2','1',NULL,'pool');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300101','存储资源队列','3001',NULL,'3','1',NULL,'poolStoreQueue');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300102','计算资源队列','3001',NULL,'3','2',NULL,'poolCalculationQueue');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('3002','我的资源','30',NULL,'2','2',NULL,'my');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300201','存储资源队列','3002',NULL,'3','1',NULL,'myStoreQueue');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('300202','计算资源队列','3002',NULL,'3','2',NULL,'myCalculationQueue');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('40','告警监控','1',NULL,'1','4',NULL,'monitor');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4001','告警中心','40',NULL,'2','1',NULL,'alarm');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4002','平台监控','40',NULL,'2','2',NULL,'platform');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4003','设备监控','40',NULL,'2','3',NULL,'equipment');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4004','业务监控','40',NULL,'2','4',NULL,'business');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('4005','故障知识库','40',NULL,'2','5',NULL,'fault');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('50','我的审批','1',NULL,'1','5',NULL,'aapprove');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('60','门户配置','1',NULL,'1','6',NULL,'config');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('6001','维护管理配置','60',NULL,'2','1',NULL,'weihuguanli');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600101','系统配置','6001',NULL,'3','1',NULL,'asystem');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600102','主机配置','6001',NULL,'3','2',NULL,'ahost');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600103','平台配置','6001',NULL,'3','3',NULL,'aplatform	');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600104','服务配置','6001',NULL,'3','4',NULL,'aservice');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('6002','用户管理配置','60',NULL,'2','2',NULL,'userrole');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600201','用户配置','6002',NULL,'3','1',NULL,'auser');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600202','角色配置','6002',NULL,'3','2',NULL,'arole');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('6003','监控告警配置管理','60',NULL,'2','3',NULL,'monitoralarm');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600301','指标配置','6003',NULL,'3','1',NULL,'aquota');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600302','告警通知人配置','6003',NULL,'3','2',NULL,'aalarm');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('600303','紧急处理脚本配置','6003',NULL,'3','3',NULL,'ascript');
insert into `t_sys_priv` (`id`, `name`, `fatherid`, `urlmapping`, `level`, `seq`, `icon`, `key`) 
values('70','租户管理','1',NULL,'1','7',NULL,'rentmng');

#预置角色
insert into t_sys_role (`id`, `name`, `type`, `resoursepriv`, `dscp`)
values (1, '管理员', 1, 0, '');

insert into t_sys_role (`id`, `name`, `type`, `resoursepriv`, `dscp`)
values (99, '普通租户', 3, 0, '');

#预置角色和菜单的关联关系
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 10);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 1001);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 20);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 30);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 3001);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300101);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300102);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 3002);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300201);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 300202);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 40);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4001);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4002);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4003);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4004);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 4005);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 50);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 60);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 6001);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600101);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600102);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600103);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600104);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 6002);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600201);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600202);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 6003);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600301);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600302);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 600303);
insert into t_sys_role_priv(`roleid`, `privid`) values (1, 70);

insert into t_sys_role_priv(`roleid`, `privid`) values (99, 10);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 1002);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 20);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 30);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 3002);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 300201);
insert into t_sys_role_priv(`roleid`, `privid`) values (99, 300202);

#预置用户
insert into t_sys_user (`id`, `name`, `namezh`, `password`, `phone`, `email`, `status`, `createtime`, `starttime`, `endtime`, `remark`)
values (1, 'admin', 'admin', 'B24CF48E4E6DFF1220B6C808A47881CF', '1', '1', 0, '2016-01-01 00:00:00', '2016-01-01 00:00:00', '2050-01-01 00:00:00', '');

insert into t_sys_user (`id`, `name`, `namezh`, `password`, `phone`, `email`, `status`, `createtime`, `starttime`, `endtime`, `remark`)
values (99, 'normal', 'normal', '711444BD57F5CA613A70149057694804', '1', '1', 0, '2016-01-01 00:00:00', '2016-01-01 00:00:00', '2050-01-01 00:00:00', '');

#预置角色和用户的关联关系
insert into t_sys_user_role (`userid`, `roleid`)
values (1, 1);

insert into t_sys_user_role (`userid`, `roleid`)
values (99, 99);

#预置系统配置表
#insert into t_sys_config (`name`, `value`, `unit`, `desc`)
#values ('quo_space_total', '10000', 'T', '集群存储空间的大小');

insert into t_sys_config (`name`, `value`, `unit`, `desc`)
values ('hdfs_dir_prefix', '/home/dcp', NULL, '集群hdfs前缀路径');

#预置主机配置树一级节点
insert into t_conf_machine_group (`id`, `groupname`, `grouptype`, `parentid`, `grouplevel`)
values ('1', '中国移动云服务平台', '1', '0', '1');



commit;



