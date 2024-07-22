# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists my_db;

-- 切换库
use my_db;

insert into user (userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole, createTime, updateTime, isDelete)
values
    ('zhangsan', '5f4dcc3b5aa765d61d8327deb882cf99', 'wx1234567890', 'mp_openid_1234', '张三', 'http://example.com/avatar1.jpg', '来自北京的软件工程师。', 'user', '2024-01-15 08:30:00', '2024-07-01 10:00:00', 0),
    ('lisi', 'ee11cbb19052e40b07aac0ca060c23ee', 'wx0987654321', 'mp_openid_5678', '李四', 'http://example.com/avatar2.jpg', '来自上海的产品经理。', 'admin', '2024-02-20 09:45:00', '2024-07-02 11:00:00', 0),
    ('wangwu', '25d55ad283aa400af464c76d713c07ad', null, 'mp_openid_9012', '王五', 'http://example.com/avatar3.jpg', '来自深圳的用户体验设计师。', 'user', '2024-03-10 10:00:00', '2024-07-03 12:00:00', 0),
    ('zhaoliu', 'd8578edf8458ce06fbc5bb76a58c5ca4', 'wx5678901234', null, '赵六', 'http://example.com/avatar4.jpg', '来自广州的数据科学家。', 'ban', '2024-04-15 11:30:00', '2024-07-04 13:00:00', 0),
    ('sunqi', '5f4dcc3b5aa765d61d8327deb882cf99', null, null, '孙七', 'http://example.com/avatar5.jpg', '来自成都的前端开发。', 'user', '2024-05-20 14:20:00', '2024-07-05 14:00:00', 0),
    ('zhouba', 'ee11cbb19052e40b07aac0ca060c23ee', 'wx9876543210', 'mp_openid_3456', '周八', 'http://example.com/avatar6.jpg', '来自杭州的后端开发。', 'user', '2024-06-25 15:40:00', '2024-07-06 15:00:00', 0),
    ('wushi', '25d55ad283aa400af464c76d713c07ad', 'wx6789012345', 'mp_openid_7890', '吴十', 'http://example.com/avatar7.jpg', '来自重庆的产品运营。', 'user', '2024-07-01 16:50:00', '2024-07-07 16:00:00', 0),
    ('chengshi', 'd8578edf8458ce06fbc5bb76a58c5ca4', null, 'mp_openid_1122', '程十一', 'http://example.com/avatar8.jpg', '来自武汉的市场经理。', 'user', '2024-07-05 17:00:00', '2024-07-08 17:00:00', 0),
    ('wangshi', '5f4dcc3b5aa765d61d8327deb882cf99', 'wx1112131415', null, '王十二', 'http://example.com/avatar9.jpg', '来自南京的测试工程师。', 'admin', '2024-07-09 18:00:00', '2024-07-09 18:00:00', 0),
    ('lushi', 'ee11cbb19052e40b07aac0ca060c23ee', null, null, '陆十三', 'http://example.com/avatar10.jpg', '来自西安的运维工程师。', 'ban', '2024-07-10 19:00:00', '2024-07-10 19:00:00', 0);

insert into user (userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole, createTime, updateTime, isDelete)
values
    ('yupi', '5f4dcc3b5aa765d61d8327deb882cf99', 'wx1234567890', 'mp_openid_1234', 'yupi', 'http://example.com/avatar1.jpg', '来自上海的软件工程师。', 'user', '2024-01-15 08:30:00', '2024-07-01 10:00:00', 0);

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
