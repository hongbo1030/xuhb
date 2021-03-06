/*==============================================================*/
/* DBMS name:      MySQL                                    */
/* Created on:     2018-01-10                           */
/*==============================================================*/
drop table if exists t_user_info;


/*==============================================================*/
/* Table: t_user_info                                            */
/*==============================================================*/
create table t_user_info
(
  userid        bigint not null auto_increment,
  username      varchar(128) comment '用户名',
  usernamezh    varchar(128) comment '用户中文名',
  password      varchar(128) comment '密码',
  status        tinyint comment '状态.0-正常',
  createtime    DATETIME default now() comment '创建时间',
  updatetime    DATETIME comment '消息状态 0:默认状态 1:未读 2:已读',
  dscp          varchar(1024)  comment '描述',
  primary key (userid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
  


