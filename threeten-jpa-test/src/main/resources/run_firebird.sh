# https://hub.docker.com/r/jacobalberty/firebird/
# enter container console
# docker exec -i -t firebird3 /bin/bash
# -e 'ISC_PASSWORD=masterkey' \

docker run --name jdbc-firebird \
 -e 'FIREBIRD_DATABASE=jdbc' \
 -e 'FIREBIRD_USER=jdbc' \
 -e 'FIREBIRD_PASSWORD=Cent-Quick-Space-Bath-8' \
 -p 3050:3050 \
 --mount type=tmpfs,destination=/firebird/data \
 -d jacobalberty/firebird:3.0.4