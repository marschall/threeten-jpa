#!/bin/bash
# https://hub.docker.com/r/ibmcom/db2
#  -v <db storage dir>:/database
DIRECTORY=`dirname $0`
DIRECTORY=$(realpath $DIRECTORY)

docker run -itd --name jdbc-db2 \
 -p 50000:50000 \
 --privileged=true \
 -e LICENSE=accept \
 -e 'DB2INST1_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e DBNAME=jdbc \
 -e ARCHIVE_LOGS=false \
 ibmcom/db2:11.5.0.0
