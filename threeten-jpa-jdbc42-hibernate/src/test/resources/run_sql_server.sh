#!/bin/bash
# https://docs.microsoft.com/en-us/sql/linux/sql-server-linux-setup-docker
# sudo docker pull microsoft/mssql-server-linux
# docker run --name jdbc-sqlserver \
#  –e 'ACCEPT_EULA=Y' \
#  –e 'SA_PASSWORD=sa' \
#  -p 1433:1433 \
#  -d microsoft/mssql-server-linux
docker run --name jdbc-sqlserver -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=Cent-Quick-Space-Bath-8' -p 1433:1433 -d microsoft/mssql-server-linux
