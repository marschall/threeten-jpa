#!/bin/bash
docker run --name jdbc-mariadb \
 -e 'MYSQL_ROOT_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e 'MYSQL_USER=jdbc' \
 -e 'MYSQL_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e 'MYSQL_DATABASE=jdbc' \
 -p 3307:3306 \
 -d mariadb:10.3
