FROM openjdk:17.0.2-jdk

COPY target/rmq-consumer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/rmq-consumer-spring-boot.jar"]