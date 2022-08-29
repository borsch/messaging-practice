# ActiveMQ sample

### Requirements:
- java 17
- mvn
- docker

### Run

Run `mvn clean install` inside current folder to build services
<br />
Run `docker-compose up --build` to start all services with-in docker containers. See amq-producer & amq-consumer logs

Observe `rmq-consumer` logs. Logs from Task2 will occur few times in a row due tue retry
