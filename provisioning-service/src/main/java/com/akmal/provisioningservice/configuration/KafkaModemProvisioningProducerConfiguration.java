package com.akmal.provisioningservice.configuration;

import com.akmal.provisioningservice.events.model.ModemProvisioningEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaModemProvisioningProducerConfiguration {
  private final KafkaConfigurationProperties configurationProperties;

  public KafkaModemProvisioningProducerConfiguration(
      KafkaConfigurationProperties configurationProperties) {
    this.configurationProperties = configurationProperties;
  }

  @Bean
  public ProducerFactory<String, ModemProvisioningEvent> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerProps());
  }

  private Map<String, Object> producerProps() {
    final var props = new HashMap<String, Object>();

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configurationProperties.getBootstrapServer());
    props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
    return props;
  }


  @Bean
  public KafkaTemplate<String, ModemProvisioningEvent> kafkaTemplate(ProducerFactory<String, ModemProvisioningEvent> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }
}
