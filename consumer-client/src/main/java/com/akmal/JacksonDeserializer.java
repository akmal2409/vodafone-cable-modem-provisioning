package com.akmal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;

public class JacksonDeserializer<T> implements Deserializer<T> {
  private ObjectMapper objectMapper;

  @Override
  public T deserialize(String topic, byte[] bytes) {
    try {
      return this.objectMapper.readValue(bytes, new TypeReference<T>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not deserialize json from bytes");
    }
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    this.objectMapper = new ObjectMapper();
  }
}
