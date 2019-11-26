FROM openjdk:13

ADD target/cache-movies-manager.jar /cache-movies-manager.jar
ENTRYPOINT ["java","-jar","/cache-movies-manager.jar"]
