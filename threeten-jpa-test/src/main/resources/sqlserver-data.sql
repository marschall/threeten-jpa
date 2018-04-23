DELETE FROM JAVA_TIME_42;
INSERT INTO JAVA_TIME_42 (
    ID,
    DATE_COLUMN,
    TIME_COLUMN,
    TIMESTAMP_COLUMN)
VALUES(
    1,
    '1988-12-25',
    '15:09:02.1234567',
    '1980-01-01 23:03:20.123456789');
INSERT INTO JAVA_TIME_42 (
    ID,
    DATE_COLUMN,
    TIME_COLUMN,
    TIMESTAMP_COLUMN)
VALUES(
    2,
    '2016-03-27',
    '02:55:00.1234567',
    '2016-03-27 02:55:00.123456789');

DELETE FROM JAVA_TIME_42_WITH_ZONE;
INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    1,
    '1960-01-01 23:03:20.1234567+02:30');
INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    2,
    '1999-01-23 08:26:56.1234567-05:30');

DELETE FROM JAVA_TIME_42_ZONED;
INSERT INTO JAVA_TIME_42_ZONED (
    ID,
    TIMESTAMP_UTC,
    ZONE_ID)
VALUES(
    1,
    '1999-01-23 08:26:56.123456789Z',
   'America/New_York');
