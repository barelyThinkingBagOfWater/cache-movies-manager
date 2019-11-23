FROM openjdk:13

#ADD tags/tags_1000.csv /tmp/tags.csv

ADD target/cache-movies-manager.jar /cache-movies-manager.jar
ENTRYPOINT ["java","-jar","/cache-movies-manager.jar"]
