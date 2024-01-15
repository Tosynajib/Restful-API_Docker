FROM openjdk:17
WORKDIR /app
COPY /target/RestApiBlog-0.0.1-SNAPSHOT.jar /app/RestApi-app.jar
EXPOSE 8080
#RUN javac Main.java
CMD ["java", "-jar", "RestApi-app.jar"]