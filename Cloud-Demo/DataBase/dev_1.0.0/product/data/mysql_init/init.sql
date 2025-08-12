SET GLOBAL character_set_server = utf8mb4;
SET GLOBAL collation_server = utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS demo_pd
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE demo_pd;

create table pd_product_info (
id int not null AUTO_INCREMENT,
product_name varchar(300) null,
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


insert into pd_product_info (product_name)
values
('手机' );

create table user_info (
id int not null AUTO_INCREMENT,
user_id varchar(100) null unique,
user_name varchar(100) null,
user_name_2 varchar(100) null,
user_name_3 varchar(100) null,
user_type int null,
pwd varchar(500) null,
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


insert into user_info (user_id, user_name, user_name_2, user_name_3, user_type, pwd)
values
('user_1', 'user_1', '','',1, 'user_1' ),
('editor_1', 'editor_1', '','',1, 'editor_1' ),
('adm_1', 'adm_1', '','',1, 'adm_1' );

create table user_role_t (
id int not null AUTO_INCREMENT,
user_id varchar(100) not null,
role_tp varchar(50) not null,
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert into user_role_t (user_id, role_tp)
values
('user_1', 'USER'),
('editor_1', 'EDITOR'),
('adm_1', 'ADMIN');



-- 废弃 （如果权限会迭代的话可以考虑此方案）
-- 方案一表结构  USER 可以感知父和祖的角色是什么
-- 查询方案，先查出本体role的角色信息和对应权限，再写一个or条件将其下子角色的权限（可以根据 grand_role_tp 一次性查出）。
-- 再构建权限树的时候，这个数据结构相对好看一点，但是存在or条件，设计上不是绝对合理

-- create table role_relation_t (
-- id int not null AUTO_INCREMENT,
-- role_tp varchar(50) not null,
-- parrent_role_tp varchar(50) not null,
-- grand_role_tp varchar(50) not null,
-- rela_level int null,
-- primary key (id)
-- ) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--
--
-- insert into role_relation_t (role_tp, parrent_role_tp,grand_role_tp,rela_level)
-- values
-- ('USER', 'EDITOR', 'PRODUCT_ADMIN', 3),
-- ('EDITOR', 'PRODUCT_ADMIN','PRODUCT_ADMIN', 2),
-- ('PRODUCT_ADMIN', 'PRODUCT_ADMIN', 'PRODUCT_ADMIN', 1);

-- 方案二 （角色模型高度有限）
-- 强调角色和角色之间是同级关系，针对角色后所包含的权限，可以做到对另一个角色无感赋权。
-- 不过在加入一个新角色时，需要变更的内容较多。
-- 例如：加一个概念上与USER同样的角色BUSINESS_USER ，
-- 同时要求EDITOR 和 PRODUCT_ADMIN 也要拥有这个角色权限
-- 那么需要对EDITOR 和 PRODUCT_ADMIN两个角色同时插入 才可以实现需求。
-- 基于理解，采用了方案二来搭建本次角色模型。另外角色对应的权限点内容，因为目前模型可以满足需求，就延伸其余内容了。。
create table role_relation_t (
role_tp varchar(50) not null,
role_tp_rela varchar(50) not null
) engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


insert into role_relation_t (role_tp, role_tp_rela)
values
('USER', 'USER'),
('EDITOR', 'EDITOR'),
('EDITOR', 'USER'),
('ADMIN', 'ADMIN'),
('ADMIN', 'USER'),
('ADMIN', 'EDITOR');