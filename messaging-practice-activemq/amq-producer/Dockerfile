FROM openjdk:17.0.2-jdk

COPY target/amq-producer-spring-boot.jar /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/amq-producer-spring-boot.jar"]