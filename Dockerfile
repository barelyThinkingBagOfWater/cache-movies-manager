FROM openjdk:15

ADD target/cache-movies-manager.jar /cache-movies-manager.jar
ENTRYPOINT ["java","-jar","-XX:MaxRAMPercentage=75","/cache-movies-manager.jar"]
