# ActiveMQ sample

### Requirements:
- java 17
- mvn
- docker

### Run

Run `mvn clean install` inside current folder to build services
<br />
Run `docker-compose up --build` to start all services with-in docker containers. See kafka-producer & kafka-taxi-consumer logs

Run `docker-compose up --build --scale kafka-taxi-consumer=3` to scale consumer up-to 3 instances

Run `curl -X POST http://localhost:8080/taxi-geo -H 'Content-Type: application/json' -d '{"taxiId": 112412426, "lng": "123.32", "ltd": "32.42"}`
