DELETE FROM JAVA_TIME_42;
INSERT INTO JAVA_TIME_42 (
    ID,
    DATE_COLUMN,
    TIME_COLUMN,
    TIMESTAMP_COLUMN)
VALUES(
    1,
    DATE '1988-12-25',
    TIME '15:09:02',
    TIMESTAMP '1980-01-01 23:03:20.123456789');
INSERT INTO JAVA_TIME_42 (
    ID,
    DATE_COLUMN,
    TIME_COLUMN,
    TIMESTAMP_COLUMN)
VALUES(
    2,
    DATE '2016-03-27',
    TIME '02:55:00',
    TIMESTAMP '2016-03-27 02:55:00.123456789');

DELETE FROM JAVA_TIME_42_WITH_ZONE;
INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    1,
    CAST('1960-01-01 23:03:20.123456789+02:00' AS TIMESTAMP WITH TIME ZONE) + INTERVAL '2:30' HOUR TO MINUTE);

INSERT INTO JAVA_TIME_42_WITH_ZONE (
    ID,
    OFFSET_DATE_TIME_COLUMN)
VALUES(
    2,
    CAST('1960-01-01 23:03:20.123456789-05:00' AS TIMESTAMP WITH TIME ZONE) - INTERVAL '5:30' HOUR TO MINUTE);
