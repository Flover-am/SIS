create table prism063.t_auth_role
(
    id          bigint unsigned auto_increment comment '角色id'
        primary key,
    role_name   varchar(20)                    not null comment '角色名',
    description varchar(20) default '普通用户' not null comment '角色描述',
    create_time datetime                       not null comment '数据创建时间',
    update_time datetime                       not null comment '数据更新时间'
)
    comment '角色表';

