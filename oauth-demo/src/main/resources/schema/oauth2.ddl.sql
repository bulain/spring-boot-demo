DROP TABLE IF EXISTS OAUTH_CLIENT_DETAILS;
CREATE TABLE OAUTH_CLIENT_DETAILS (
  CLIENT_ID VARCHAR(255) PRIMARY KEY,
  RESOURCE_IDS VARCHAR(255),
  CLIENT_SECRET VARCHAR(255),
  SCOPE VARCHAR(255),
  AUTHORIZED_GRANT_TYPES VARCHAR(255),
  WEB_SERVER_REDIRECT_URI VARCHAR(255),
  AUTHORITIES VARCHAR(255),
  ACCESS_TOKEN_VALIDITY INTEGER,
  REFRESH_TOKEN_VALIDITY INTEGER,
  ADDITIONAL_INFORMATION TEXT,
  CREATE_TIME TIMESTAMP DEFAULT NOW(),
  ARCHIVED TINYINT(1) DEFAULT '0',
  TRUSTED TINYINT(1) DEFAULT '0',
  AUTOAPPROVE VARCHAR (255) DEFAULT 'FALSE'
) ENGINE=INNODB DEFAULT CHARSET=UTF8;


DROP TABLE IF EXISTS OAUTH_ACCESS_TOKEN;
CREATE TABLE OAUTH_ACCESS_TOKEN (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CREATE_TIME TIMESTAMP DEFAULT NOW(),
  TOKEN_ID VARCHAR(255),
  TOKEN BLOB,
  AUTHENTICATION_ID VARCHAR(255),
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255),
  AUTHENTICATION BLOB,
  REFRESH_TOKEN VARCHAR(255)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;


DROP TABLE IF EXISTS OAUTH_REFRESH_TOKEN;
CREATE TABLE OAUTH_REFRESH_TOKEN (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CREATE_TIME TIMESTAMP DEFAULT NOW(),
  TOKEN_ID VARCHAR(255),
  TOKEN BLOB,
  AUTHENTICATION BLOB
) ENGINE=INNODB DEFAULT CHARSET=UTF8;


DROP TABLE IF EXISTS OAUTH_CODE;
CREATE TABLE OAUTH_CODE (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CREATE_TIME TIMESTAMP DEFAULT NOW(),
  CODE VARCHAR(255),
  AUTHENTICATION BLOB
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

DROP TABLE IF EXISTS OAUTH_APPROVALS;
CREATE TABLE OAUTH_APPROVALS (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  USERID VARCHAR(256),
  CLIENTID VARCHAR(256),
  SCOPE VARCHAR(256),
  STATUS VARCHAR(10),
  EXPIRESAT TIMESTAMP,
  LASTMODIFIEDAT TIMESTAMP
);

DROP TABLE IF EXISTS OAUTH_CLIENT_TOKEN;
CREATE TABLE OAUTH_CLIENT_TOKEN (
  ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CREATE_TIME TIMESTAMP DEFAULT NOW(),
  TOKEN_ID VARCHAR(255),
  TOKEN BLOB,
  AUTHENTICATION_ID VARCHAR(255),
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255),
  AUTHENTICATION BLOB,
  REFRESH_TOKEN VARCHAR(255)
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

-- ADD INDEXES
CREATE INDEX TOKEN_ID_INDEX ON OAUTH_ACCESS_TOKEN (TOKEN_ID);
CREATE INDEX AUTHENTICATION_ID_INDEX ON OAUTH_ACCESS_TOKEN (AUTHENTICATION_ID);
CREATE INDEX USER_NAME_INDEX ON OAUTH_ACCESS_TOKEN (USER_NAME);
CREATE INDEX CLIENT_ID_INDEX ON OAUTH_ACCESS_TOKEN (CLIENT_ID);
CREATE INDEX REFRESH_TOKEN_INDEX ON OAUTH_ACCESS_TOKEN (REFRESH_TOKEN);
CREATE INDEX TOKEN_ID_INDEX ON OAUTH_REFRESH_TOKEN (TOKEN_ID);
CREATE INDEX CODE_INDEX ON OAUTH_CODE (CODE);