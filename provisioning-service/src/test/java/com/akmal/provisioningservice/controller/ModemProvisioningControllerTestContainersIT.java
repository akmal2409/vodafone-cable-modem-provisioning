package com.akmal.provisioningservice.controller;


import static org.assertj.core.api.Assertions.assertThat;

import com.akmal.provisioningservice.dto.RegistrationRequest;
import com.akmal.provisioningservice.events.model.ModemProvisioningEvent;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "integration-test")
@DirtiesContext
@Testcontainers
class ModemProvisioningControllerTestContainersIT {

  @Container
  static KafkaContainer kafkaContainer = new KafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:latest")).withEmbeddedZookeeper();
  static final String TOPIC = "test.topic";

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @LocalServerPort
  int port;
  TestRestTemplate testRestTemplate;

  Consumer<String, ModemProvisioningEvent> consumer;

  @DynamicPropertySource
  static void setupProps(DynamicPropertyRegistry registry) {
    registry.add("kafka.bootstrap-server", kafkaContainer::getBootstrapServers);
    registry.add("kafka.modem-provisioning-topic", () -> TOPIC);
  }

  @BeforeEach
  void setup() {
    testRestTemplate = new TestRestTemplate(restTemplateBuilder.rootUri("http://127.0.0.1:" + port));
    DefaultKafkaConsumerFactory<String, ModemProvisioningEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProps());

    consumer = consumerFactory.createConsumer();
    consumer.subscribe(Collections.singletonList(TOPIC));
  }

  private Map<String, Object> getConsumerProps() {
    final var props = new HashMap<String, Object>();
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return props;
  }

  /**
   * Not the best test, flaky and non-deterministic
   */
  @Test
  void shouldProduceEventWhenMacIsValid() {
    final var request = new RegistrationRequest("A8:A8:A8:A8:A8:A8");

    final var response = testRestTemplate.exchange(ModemProvisioningController.BASE_API, HttpMethod.POST, new HttpEntity<>(request), Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(202));
    final ConsumerRecords<String, ModemProvisioningEvent> records = consumer.poll(Duration.of(500, ChronoUnit.MILLIS));

    assertThat(records).isNotNull();
    assertThat(records.count()).isEqualTo(1);
    final var event = records.iterator().next().value();

    assertThat(event).isNotNull().extracting(ModemProvisioningEvent::macAddress).isEqualTo(request.macAddress());
  }


}
