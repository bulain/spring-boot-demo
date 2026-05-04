-- // create RBAC tables: sys_users, sys_roles, sys_permissions, sys_user_roles, sys_role_permissions
-- Migration SQL that creates the standard RBAC (Role-Based Access Control) tables

-- Users table
create table sys_users
(
    id                bigint      not null auto_increment,
    username          varchar(50) not null comment '用户名',
    password          varchar(255) not null comment '密码（BCrypt加密）',
    name              varchar(50) comment '姓名',
    email             varchar(100) comment '邮箱',
    phone             varchar(20) comment '手机号',
    wechat_openid     varchar(100) comment '微信OpenID',
    dingtalk_userid   varchar(100) comment '钉钉UserID',
    status            tinyint     default 1 comment '状态：1-启用，0-禁用',

    created_by        bigint comment '创建人ID',
    created_at        datetime    default current_timestamp comment '创建时间',
    updated_by        bigint comment '修改人ID',
    updated_at        datetime    default current_timestamp on update current_timestamp comment '更新时间',
    pubts             bigint      default 0 comment '乐观锁版本号，更新时赋值当前毫秒时间戳',
    dr                bigint      default 0 comment '逻辑删除时间戳，0表示未删除',

    primary key (id)
) engine=InnoDB default charset=utf8mb4 comment='用户表';

create unique index uk_sys_users_username on sys_users (username, dr);
create unique index uk_sys_users_email on sys_users (email, dr);
create unique index uk_sys_users_phone on sys_users (phone, dr);
create unique index uk_sys_users_wechat on sys_users (wechat_openid, dr);
create unique index uk_sys_users_dingtalk on sys_users (dingtalk_userid, dr);
create index idx_sys_users_status on sys_users (status);
create index idx_sys_users_name on sys_users (name);
create index idx_sys_users_created_by on sys_users (created_by);
create index idx_sys_users_updated_by on sys_users (updated_by);
create index idx_sys_users_dr on sys_users (dr);

-- Roles table
create table sys_roles
(
    id                bigint      not null auto_increment,
    name              varchar(50) not null comment '角色名称',
    code              varchar(50) not null comment '角色编码',
    description       varchar(255) comment '角色描述',

    created_by        bigint comment '创建人ID',
    created_at        datetime    default current_timestamp comment '创建时间',
    updated_by        bigint comment '修改人ID',
    updated_at        datetime    default current_timestamp on update current_timestamp comment '更新时间',
    pubts             bigint      default 0 comment '乐观锁版本号，更新时赋值当前毫秒时间戳',
    dr                bigint      default 0 comment '逻辑删除时间戳，0表示未删除',

    primary key (id)
) engine=InnoDB default charset=utf8mb4 comment='角色表';

create unique index uk_sys_roles_code on sys_roles (code, dr);
create index idx_sys_roles_name on sys_roles (name);
create index idx_sys_roles_created_by on sys_roles (created_by);
create index idx_sys_roles_updated_by on sys_roles (updated_by);
create index idx_sys_roles_dr on sys_roles (dr);

-- Permissions table
create table sys_permissions
(
    id                bigint      not null auto_increment,
    name              varchar(50) not null comment '权限名称',
    code              varchar(100) not null comment '权限编码',
    resource_type     varchar(50) comment '资源类型：menu, button, api',
    description       varchar(255) comment '权限描述',

    created_by        bigint comment '创建人ID',
    created_at        datetime    default current_timestamp comment '创建时间',
    updated_by        bigint comment '修改人ID',
    updated_at        datetime    default current_timestamp on update current_timestamp comment '更新时间',
    pubts             bigint      default 0 comment '乐观锁版本号，更新时赋值当前毫秒时间戳',

    primary key (id)
) engine=InnoDB default charset=utf8mb4 comment='权限表';

create unique index uk_sys_permissions_code on sys_permissions (code);
create index idx_sys_permissions_name on sys_permissions (name);
create index idx_sys_permissions_resource_type on sys_permissions (resource_type);
create index idx_sys_permissions_created_by on sys_permissions (created_by);
create index idx_sys_permissions_updated_by on sys_permissions (updated_by);

-- User-Role association table
create table sys_user_roles
(
    id                bigint      not null auto_increment,
    user_id           bigint      not null comment '用户ID',
    role_id           bigint      not null comment '角色ID',

    created_by        bigint comment '创建人ID',
    created_at        datetime    default current_timestamp comment '创建时间',
    updated_by        bigint comment '修改人ID',
    updated_at        datetime    default current_timestamp on update current_timestamp comment '更新时间',
    pubts             bigint      default 0 comment '乐观锁版本号，更新时赋值当前毫秒时间戳',

    primary key (id)
) engine=InnoDB default charset=utf8mb4 comment='用户角色关联表';

create unique index uk_sys_user_roles_user_role on sys_user_roles (user_id, role_id);
create index idx_sys_user_roles_user_id on sys_user_roles (user_id);
create index idx_sys_user_roles_role_id on sys_user_roles (role_id);
create index idx_sys_user_roles_created_by on sys_user_roles (created_by);
create index idx_sys_user_roles_updated_by on sys_user_roles (updated_by);

-- Role-Permission association table
create table sys_role_permissions
(
    id                bigint      not null auto_increment,
    role_id           bigint      not null comment '角色ID',
    permission_id     bigint      not null comment '权限ID',

    created_by        bigint comment '创建人ID',
    created_at        datetime    default current_timestamp comment '创建时间',
    updated_by        bigint comment '修改人ID',
    updated_at        datetime    default current_timestamp on update current_timestamp comment '更新时间',
    pubts             bigint      default 0 comment '乐观锁版本号，更新时赋值当前毫秒时间戳',

    primary key (id)
) engine=InnoDB default charset=utf8mb4 comment='角色权限关联表';

create unique index uk_sys_role_permissions_role_permission on sys_role_permissions (role_id, permission_id);
create index idx_sys_role_permissions_role_id on sys_role_permissions (role_id);
create index idx_sys_role_permissions_permission_id on sys_role_permissions (permission_id);
create index idx_sys_role_permissions_created_by on sys_role_permissions (created_by);
create index idx_sys_role_permissions_updated_by on sys_role_permissions (updated_by);
