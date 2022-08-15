FROM openjdk:17.0.2-jdk

COPY target/amq-consumer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/amq-consumer-spring-boot.jar"]