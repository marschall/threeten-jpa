
-- likely bug as we are the owner https://jira.mariadb.org/browse/MDEV-18554

GRANT SELECT ON `mysql`.`proc` TO 'jdbc'@'%';
