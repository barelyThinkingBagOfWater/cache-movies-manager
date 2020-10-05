FROM openjdk:14

ADD target/cache-movies-manager.jar /cache-movies-manager.jar
ENTRYPOINT ["java","-jar","/cache-movies-manager.jar"]
