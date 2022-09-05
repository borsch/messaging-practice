FROM openjdk:17.0.2-jdk

COPY target/kafka-taxi-consumer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/kafka-taxi-consumer-spring-boot.jar"]