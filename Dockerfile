FROM maven:3-openjdk-18-slim
USER root
COPY . /usr/server
WORKDIR /usr/server
RUN mvn package -DskipTests
RUN cp /usr/server/target/*jar /usr/server/app.jar
WORKDIR /usr/server
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx1024m", "-jar","app.jar"]
