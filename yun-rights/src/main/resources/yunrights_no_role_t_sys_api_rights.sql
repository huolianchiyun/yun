create table t_sys_api_rights
(
    api_id            bigint unsigned null,
    api_group         varchar(100)    null comment 'module group',
    api_tag           varchar(100)    null comment 'controller tag',
    api_url           varchar(255)    null comment 'request url',
    api_authorization varchar(255)    null comment 'access authorization of api url ',
    api_description   varchar(255)    null comment 'api description',
    api_create_time   datetime        not null
)
    comment 'api 访问权限';

