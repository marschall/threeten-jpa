# https://hub.docker.com/r/jacobalberty/firebird/
# enter container console
# docker exec -i -t firebird3 /bin/bash
# -e 'ISC_PASSWORD=masterkey' \
# -e 'EnableLegacyClientAuth=y' \

docker run -d \
    --name jdbc-firebird \
    -e 'FIREBIRD_DATABASE=jdbc' \
    -e 'FIREBIRD_USER=jdbc' \
    -e 'FIREBIRD_PASSWORD=Cent-Quick-Space-Bath-8' \
    -p 3050:3050 \
    jacobalberty/firebird:3.0.3