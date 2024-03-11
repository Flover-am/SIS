create table prism063.t_auth_permission
(
    id              bigint unsigned auto_increment comment '权限id'
        primary key,
    permission_name varchar(20) not null comment '权限名',
    description     varchar(20) not null comment '权限描述',
    create_time     datetime    not null comment '数据创建时间',
    update_time     datetime    not null comment '数据更新时间'
)
    comment '权限表';

