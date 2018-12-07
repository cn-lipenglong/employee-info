drop table if exists user;
create table user
(
  id       varchar(20) not null,
  username varchar(100)	DEFAULT NULL COMMENT '名称',
  begin_time datetime   DEFAULT NULL COMMENT '开始时间',
  end_time datetime  DEFAULT NULL COMMENT '结束时间',
  work_status int(1)	DEFAULT NULL COMMENT '工作状态 1：占用；0：空闲',
  primary key (id)
);

alter table user comment '用户表';