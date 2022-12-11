package com.akmal.provisioningservice.service;

import com.akmal.provisioningservice.configuration.KafkaConfigurationProperties;
import com.akmal.provisioningservice.dto.RegistrationRequest;
import com.akmal.provisioningservice.events.model.ModemProvisioningEvent;
import com.akmal.provisioningservice.exceptions.InvalidMacException;
import com.akmal.provisioningservice.exceptions.MessageRejectedException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningService {
  private static final Logger log = LoggerFactory.getLogger(ProvisioningService.class);
  private final Converter<String, String> macNormalizer;
  private final Predicate<String> macValidator;

  private final KafkaTemplate<String, ModemProvisioningEvent> kafkaTemplate;
  private final KafkaConfigurationProperties kafkaProps;
  public ProvisioningService(@Qualifier("macNormalizer") Converter<String, String> macNormalizer,
      @Qualifier("macValidator") Predicate<String> macValidator,
      KafkaTemplate<String, ModemProvisioningEvent> kafkaTemplate,
      KafkaConfigurationProperties kafkaProps) {
    this.macNormalizer = macNormalizer;
    this.macValidator = macValidator;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaProps = kafkaProps;
  }

  public void provisionModem(RegistrationRequest request) throws InvalidMacException, MessageRejectedException {
    if (!macValidator.test(request.macAddress())) throw new InvalidMacException("Supplied mac is invalid");

    final String normalizedMac = macNormalizer.convert(request.macAddress());
    final Instant timestampUtc = Instant.now();

    try {
      this.kafkaTemplate.send(kafkaProps.getModemProvisioningTopic(), normalizedMac, new ModemProvisioningEvent(normalizedMac, timestampUtc.toEpochMilli()))
          .get(100, TimeUnit.MILLISECONDS);
    } catch (InterruptedException | TimeoutException | ExecutionException ex) {
      if (ex instanceof InterruptedException) Thread.currentThread().interrupt();

      log.error("Could not send provisioning event to the topic", ex);
      throw new MessageRejectedException("Could not provision modem due to internal server error");
    }
  }
}
