DELETE FROM JAVA_TIMESTAMP_42;
INSERT INTO JAVA_TIMESTAMP_42 (
    ID,
    TIMESTAMP_COLUMN)
VALUES(
    1,
    TIMESTAMP '1980-01-01 23:03:20.123456789');
INSERT INTO JAVA_TIMESTAMP_42 (
    ID,
    TIMESTAMP_COLUMN)
VALUES(
    2,
    TIMESTAMP '2016-03-27 02:55:00.123456789');

DELETE FROM JAVA_DATE_42;
INSERT INTO JAVA_DATE_42 (
    ID,
    DATE_COLUMN)
VALUES(
    1,
    DATE '1988-12-25');
INSERT INTO JAVA_DATE_42 (
    ID,
    DATE_COLUMN)
VALUES(
    2,
    DATE '2016-03-27');

DELETE FROM JAVA_TIME_42_WITH_ZONE;
INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    1,
    TIMESTAMP '1960-01-01 23:03:20.123456789+02:30');
INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    2,
    TIMESTAMP '1999-01-23 08:26:56.123456789-05:30');
