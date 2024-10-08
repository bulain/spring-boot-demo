-- // create table demo_blog
-- Migration SQL that makes the change goes here.
CREATE TABLE DEMO_BLOG
(
    ID          VARCHAR2(20) NOT NULL,

    TITLE       VARCHAR2(50) NOT NULL,
    DESCR       VARCHAR2(50),
    ACTIVE_FLAG VARCHAR2(50),

    CREATED_VIA VARCHAR2(50),
    REMARKS     VARCHAR2(500),
    CREATED_AT  DATE,
    CREATED_BY  VARCHAR2(50),
    UPDATED_AT  DATE,
    UPDATED_BY  VARCHAR2(50),

    PRIMARY KEY (ID)
);

CREATE INDEX IDX_DEMO_BLOG_1 ON DEMO_BLOG (TITLE);
CREATE INDEX IDX_DEMO_BLOG_2 ON DEMO_BLOG (DESCR);
