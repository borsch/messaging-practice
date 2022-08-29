# ActiveMQ sample

### Requirements:
- java 17
- mvn
- docker

### Run

Run `mvn clean install` inside current folder to build services
<br />
Run `docker-compose up --build` to start all services with-in docker containers. See amq-producer & amq-consumer logs


#### Publish/Subscriber with durable & non-durable messages

1. Start all services as describer in Run step
2. Push single test message using following command `curl http://localhost:8082/topic?message=some-test-message` and observe `amq-consumer` logs.
    There should be both logs - from durable & non-durable listeners
3. Stop `amq-consumer` container
4. Publish few messages using command from step 2
5. Start `amq-consumer` again
6. Observe that messages from step 4 were picked only by durable subscriber


#### Request & Reply

1. Start all services as describer in Run step
2. Push message using following command `curl http://localhost:8082/send-and-receive?message=some-test-message`, observe service logs & check response to http request

#### Virtual topics

1. Start all services as describer in Run step
2. Push message using following command `http://localhost:8082/virtual-topic?message=some-test-message`, observe service logs.
   Note that two different services (ie `amq-consumer` & `amq-consumer-2`) receive them message
3. Restart docker containers using command `docker-compose up --build --scale amq-consumer-2=3` which will start three instances of `amq-consumer-2` service
4. Repeat start 2 few times and observe logs. You should see that different instances of `amq-consumer-2` receives message, but never two or more receive same message
