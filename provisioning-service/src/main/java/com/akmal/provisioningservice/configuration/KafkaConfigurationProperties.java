package com.akmal.provisioningservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigurationProperties {

  private String bootstrapServer;
  private String modemProvisioningTopic;

  public String getBootstrapServer() {
    return bootstrapServer;
  }

  public void setBootstrapServer(String bootstrapServer) {
    this.bootstrapServer = bootstrapServer;
  }

  public String getModemProvisioningTopic() {
    return modemProvisioningTopic;
  }

  public void setModemProvisioningTopic(String modemProvisioningTopic) {
    this.modemProvisioningTopic = modemProvisioningTopic;
  }
}
