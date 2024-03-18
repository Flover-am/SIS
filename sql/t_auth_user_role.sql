create table prism030.t_auth_user_role
(
    id          bigint unsigned auto_increment comment '用户-角色关系id'
        primary key,
    user_id     bigint   not null comment '用户id',
    role_id     bigint   not null comment '角色id',
    create_time datetime not null comment '数据创建时间',
    update_time datetime not null comment '数据更新时间'
)
    comment '用户-角色关系表';

