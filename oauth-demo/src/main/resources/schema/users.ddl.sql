DROP TABLE IF EXISTS AUTHORITIES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
	USERNAME VARCHAR(50) NOT NULL PRIMARY KEY,
	PASSWORD VARCHAR(500) NOT NULL,
	ENABLED BOOLEAN NOT NULL
);

CREATE TABLE AUTHORITIES (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	USERNAME VARCHAR(50) NOT NULL,
	AUTHORITY VARCHAR(50) NOT NULL,
	
	CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME)
);

CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES (USERNAME, AUTHORITY);

CREATE TABLE GROUPS (
	ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	GROUP_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE GROUP_AUTHORITIES (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	GROUP_ID BIGINT(20) NOT NULL,
	AUTHORITY VARCHAR(50) NOT NULL,
	
	CONSTRAINT FK_GROUP_AUTHORITIES_GROUP FOREIGN KEY(GROUP_ID) REFERENCES GROUPS(ID)
);

CREATE TABLE GROUP_MEMBERS (
	ID BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	USERNAME VARCHAR(50) NOT NULL,
	GROUP_ID BIGINT(20) NOT NULL,
	
	CONSTRAINT FK_GROUP_MEMBERS_GROUP FOREIGN KEY(GROUP_ID) REFERENCES GROUPS(ID)
);