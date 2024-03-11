create table t_news
(
    id            bigint auto_increment comment '新闻id'
        primary key,
    title         varchar(100) not null comment '新闻标题',
    content       mediumtext   not null comment '新闻内容',
    origin_source varchar(100) not null comment '新闻来源',
    source_time   datetime     not null comment '新闻发布时间',
    link          varchar(255) null comment '新闻源链接',
    source_link   varchar(255) null comment '新闻原始链接',
    category      int          null comment '新闻类别',
    create_time   datetime     not null comment '当前字段创建时间',
    update_time   datetime     not null comment '当前字段最近更新时间'
);