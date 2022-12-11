package com.akmal.provisioningservice.configuration;

import com.akmal.provisioningservice.converter.MacNormalizer;
import com.akmal.provisioningservice.validator.MacValidator;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class MacOperationsConfiguration {

  @Bean
  public Converter<String, String> macNormalizer() {
    return new MacNormalizer();
  }

  @Bean
  public Predicate<String> macValidator() {
    return new MacValidator();
  }
}
