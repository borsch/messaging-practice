FROM openjdk:17.0.2-jdk

COPY target/kafka-producer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/kafka-producer-spring-boot.jar"]