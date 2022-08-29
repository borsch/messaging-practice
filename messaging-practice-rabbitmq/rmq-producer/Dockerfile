FROM openjdk:17.0.2-jdk

COPY target/rmq-producer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/rmq-producer-spring-boot.jar"]