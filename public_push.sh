mvn clean install &&
docker build . -t xbarrelet/cache-movies-manager:1.0.0 &&
docker push xbarrelet/cache-movies-manager:1.0.0 
