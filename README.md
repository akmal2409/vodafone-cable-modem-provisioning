# vodafone-cable-modem-provisioning
Assignment 2.6

# Architecture Clarification (Topic Based message broker)
Even though the assignment prescribed to use either RabbitMQ or ActiveMQ. I have opted to use Kafka because I am fairly confident in it and I have been exposed to it before.
Furthermore, the assignment specified the emittion of events and the most sensible tool for this task would be to use a topic based message broker like Kafka that provides
transactions and durability as well as the ordering of messages within the partition, which I believe is critical when dealing with client billing.

# Quick start (One touch set up with docker compose)
## Build provisioning-service
Navigate to the folder
    
    cd provisioning-service

Build the project and install dependencies (generates the jar)

    mvn install

Navigate back

    cd ..

Run provisioning service, kafka and zookeeper instances

    docker compose up

## Running Consumer client
Since the assignment required me to implement a consumer, I have done so via a separate java projects that needs to be built and run.

Navigate to the consumer folder
    
    cd consumer-client

Run (that will install dependencies and build the uber jar with all the dependencies)
    
    mvn install

If you haven't changed any properties in docker compose file, you can simply run the jar and it will use the default connection properties.

    java -jar target/consumer-client-1.0.jar

However, if you have changed the broker's port or hostname, or even the topic name, you can run the following

    java -jar target/consumer-client-1.0.jar <broker's address including port> <topic name>

## Manual launch of the provisioning service
Make sure that kafka broker and zookeeper are running either via docker container or as an ordinary process.

Install the dependencies and build it

    mvn install

And then use the spring-boot maven plugin to run the application

    mvn spring-boot:run

## Run tests (Both integration and unit)
    mvn test

## Run integration tests (only) and generate openapi.json
    mvn verify

## Package into a jar
    mvn clean package

# Testing
You can hit the endpoint stated in the postman collection. Make sure however, that the consumer client is running. Upon POST request you should see something like this in your consumer client's console.

Side note: Endpoint supports both 48-bit and 64-bit MAC addresses in different notations such as dot notation (1ABB.FACC.2EA3), dash notation (1A-BB-FA-CC-2E-A3), or colon notation (1A:BB:FA:CC:2E:A3) (same for 64bit). At the end the MAC address is normalized to the colon notation.

```
20:42:41.141 [main] INFO  com.akmal.Consumer - Creating consumer for bootstrap server localhost:9092
20:42:41.322 [main] INFO  com.akmal.Consumer - Subscribed to topic modem.provisioning.topic
20:42:46.288 [main] INFO  com.akmal.Consumer - Received event {macAddress=01:23:45:67:89:AB, timestamp=1670787766249}
```


