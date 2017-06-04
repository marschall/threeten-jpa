CREATE TABLE JAVA_TIME_42 (
  ID INT NOT NULL,
  DATE_COLUMN DATE,
  TIME_COLUMN TIME,
  TIMESTAMP_COLUMN TIMESTAMP,
  PRIMARY KEY (ID)
);

CREATE TABLE JAVA_TIME_42_WITH_ZONE (
  ID INT NOT NULL,
  OFFSET_TIME TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (ID)
);