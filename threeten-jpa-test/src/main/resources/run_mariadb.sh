#!/bin/bash
DIRECTORY=`dirname $0`
DIRECTORY=$(realpath $DIRECTORY)

docker run --name jdbc-mariadb \
 -e 'MYSQL_ROOT_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e 'MYSQL_USER=jdbc' \
 -e 'MYSQL_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e 'MYSQL_DATABASE=jdbc' \
 -p 3307:3306 \
 --mount type=tmpfs,destination=/var/lib/mysql \
 -v ${DIRECTORY}/mariadb:/docker-entrypoint-initdb.d \
 -d mariadb:10.4.12
