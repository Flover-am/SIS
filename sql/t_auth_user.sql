create table prism030.t_auth_user
(
    id          bigint unsigned auto_increment comment '用户id'
        primary key,
    username    varchar(20)  not null comment '用户名',
    password    varchar(200) not null comment '用户密码',
    create_time datetime     not null comment '数据创建时间',
    update_time datetime     not null comment '数据修改时间',
    constraint uk_username
        unique (username)
)
    comment '用户表';

