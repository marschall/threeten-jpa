#!/bin/bash
# https://docs.microsoft.com/en-us/sql/linux/sql-server-linux-setup-docker
# https://github.com/Microsoft/mssql-docker/issues/110
# --mount type=tmpfs,destination=/var/opt/mssql
docker run --name jdbc-sqlserver \
 -e 'ACCEPT_EULA=Y' \
 -e 'SA_PASSWORD=Cent-Quick-Space-Bath-8' \
 -p 1433:1433 \
 -d mcr.microsoft.com/mssql/server:2019-latest
