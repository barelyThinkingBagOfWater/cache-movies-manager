mvn clean install &&
docker build . -t xbarrelet/cache-movies-manager &&
docker push xbarrelet/cache-movies-manager
