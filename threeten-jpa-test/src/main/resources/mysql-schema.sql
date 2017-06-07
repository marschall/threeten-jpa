DROP TABLE IF EXISTS JAVA_TIME_42;
CREATE TABLE JAVA_TIME_42 (
  ID BIGINT NOT NULL,
  DATE_COLUMN DATE,
  TIME_COLUMN TIME,
  TIMESTAMP_COLUMN DATETIME(6),
  PRIMARY KEY (ID)
);
