#!/bin/bash
# https://pythonspeed.com/articles/faster-db-tests/
docker run --name jdbc-postgres \
 -e POSTGRES_PASSWORD= \
 -e POSTGRES_USER=$USER \
 -p 5432:5432 \
 --mount type=tmpfs,destination=/var/lib/postgresql/data \
 -d postgres:11.2-alpine
