-- // Create Changelog

-- Default DDL for changelog table that will keep
-- a record of the migrations that have been run.

-- You can modify this to suit your database before
-- running your first migration.

-- Be sure that ID and DESCRIPTION fields exist in
-- BigInteger and String compatible fields respectively.

create table ${changelog} (
id numeric(20,0) not null,
applied_at varchar(25) not null,
description varchar(255) not null
);

alter table ${changelog}
add constraint pk_${changelog}
primary key (id);

-- //@UNDO

drop table ${changelog};
