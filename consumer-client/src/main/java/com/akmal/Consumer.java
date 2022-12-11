package com.akmal;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

  private final static Logger log = LoggerFactory.getLogger(Consumer.class);

  public static void main(String[] args) {
    String bootstrapServer = "localhost:9092";
    String topicName = "modem.provisioning.topic";


    if (args.length > 0) {
      // try to parse bootstrap server
      bootstrapServer = args[0];
    }

    if (args.length > 1) {
      topicName = args[1];
    }

    log.info("Creating consumer for bootstrap server {}", bootstrapServer);
    final KafkaConsumer<String, ModemProvisioningEvent> consumer = new KafkaConsumer<>(getConsumerProps(bootstrapServer));

    consumer.subscribe(Collections.singletonList(topicName));
    log.info("Subscribed to topic {}", topicName);

    Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

    try {
      while (true) {
        ConsumerRecords<String, ModemProvisioningEvent> records = consumer.poll(Duration.ofMillis(100));

        for (ConsumerRecord<String, ModemProvisioningEvent> dataRecord: records) {
          log.info("Received event {}", dataRecord.value());
        }
      }
    } catch (WakeupException e) {
      // ignore, since we are shutting it down from the shutdown hook we must wrap it in a try catch block.
      // Kafka consumer can only be shutdown from another thread via invoking wakeup()
    }
  }

  private static Properties getConsumerProps(String bootstrapServer) {
    final var props = new Properties();

    props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonDeserializer.class.getName());

    return props;
  }
}
