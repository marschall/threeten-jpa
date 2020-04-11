#!/bin/bash
# https://github.com/oracle/docker-images/blob/master/OracleDatabase/SingleInstance/README.md#running-oracle-database-enterprise-and-standard-edition-2-in-a-docker-container
DIRECTORY=`dirname $0`
DIRECTORY=$(realpath $DIRECTORY)

docker run --name jdbc-oracle \
 -p 1521:1521 -p 5500:5500 \
 --shm-size=1g \
 -v ${DIRECTORY}/oracle:/docker-entrypoint-initdb.d/setup \
 -d oracle/database:19.3.0-se2
