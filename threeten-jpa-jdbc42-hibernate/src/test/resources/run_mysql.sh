#!/bin/bash
docker run --name jdbc-mysql \
 -e MYSQL_ROOT_PASSWORD=$USER \
 -e MYSQL_USER=$USER \
 -e MYSQL_PASSWORD=$USER \
 -e MYSQL_DATABASE=$USER \
 -p 3306:3306 \
 -d mysql:8.0.1
