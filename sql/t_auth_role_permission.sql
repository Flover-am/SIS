create table prism063.t_auth_role_permission
(
    id            bigint unsigned auto_increment comment '角色-权限关系id'
        primary key,
    role_id       bigint   not null comment '角色id',
    permission_id bigint   not null comment '权限id',
    create_time   datetime not null comment '数据创建时间',
    update_time   datetime not null comment '数据更新时间'
)
    comment '角色-权限关系表';

