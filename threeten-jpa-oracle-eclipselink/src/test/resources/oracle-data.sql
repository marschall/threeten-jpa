-- use a named time zone
INSERT INTO JAVA_TIME (ID, OFFSET_COLUMN,                            ZONE_COLUMN,                               CALENDAR_COLUMN,                           YEAR_TO_MONTH_COLUMN,              DAY_TO_SECOND_COLUMN)
VALUES(                1, TIMESTAMP '1997-01-31 09:26:56.66 +02:00', TIMESTAMP '1999-01-15 8:00:00 US/Pacific', TIMESTAMP '1999-01-15 8:00:00 US/Pacific', INTERVAL '123-2' YEAR(3) TO MONTH, INTERVAL '4 5:12:10.222' DAY TO SECOND(3));
-- use a negative time zone offset
-- #17
INSERT INTO JAVA_TIME (ID, OFFSET_COLUMN,            ZONE_COLUMN,                                   CALENDAR_COLUMN,                           YEAR_TO_MONTH_COLUMN,              DAY_TO_SECOND_COLUMN)
VALUES(2, TIMESTAMP '1999-01-15 08:26:56.66 -03:30', TIMESTAMP '2016-09-22 17:00:00 Europe/Berlin', TIMESTAMP '1999-01-15 8:00:00 US/Pacific', INTERVAL '123-2' YEAR(3) TO MONTH, INTERVAL '4 5:12:10.222' DAY TO SECOND(3));
