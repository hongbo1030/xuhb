#预置用户
insert into t_user_info (userid, username, usernamezh, password, status, createtime, updatetime, dscp)
values (1, 'admin', 'admin', '1qaz!QAZ', 1, '2018-01-01 00:00:00', '2018-01-01 00:00:00', '');

insert into t_user_info (userid, username, usernamezh, password, status, createtime, updatetime, dscp)
values (99, 'normal', 'normal', '1qaz!QAZ', 1, '2018-01-01 00:00:00', '2018-01-01 00:00:00', '');

commit;

