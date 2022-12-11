package com.akmal.provisioningservice.controller;

import static org.mockito.ArgumentMatchers.any;

import com.akmal.provisioningservice.dto.RegistrationRequest;
import com.akmal.provisioningservice.exceptions.InvalidMacException;
import com.akmal.provisioningservice.exceptions.MessageRejectedException;
import com.akmal.provisioningservice.service.ProvisioningService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest(ModemProvisioningController.class)
@ExtendWith(MockitoExtension.class)
class ModemProvisioningControllerIT {


  @MockBean
  ProvisioningService provisioningService;

  @Autowired
  MockMvc mockMvc;


  ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  }


  @Test
  void shouldReturnBadRequestWhenMacInvalid() throws Exception {
    final var request = new RegistrationRequest("A8");

    Mockito.doThrow(new InvalidMacException("Invalid mac")).when(provisioningService).provisionModem(any(RegistrationRequest.class));

    mockMvc.perform(MockMvcRequestBuilders.post(ModemProvisioningController.BASE_API).contentType(
            MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void shouldReturnInternalServerErrorWhenMessageNotSent() throws Exception {
    final var request = new RegistrationRequest("A8:A8:A8:A8:A8:A8");

    Mockito.doThrow(new MessageRejectedException("Rejected")).when(provisioningService).provisionModem(any(RegistrationRequest.class));

    mockMvc.perform(MockMvcRequestBuilders.post(ModemProvisioningController.BASE_API).contentType(
            MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError());
  }

  @Test
  void shouldReturnAcceptedWhenRegistrationSucceeded() throws Exception {
    final var request = new RegistrationRequest("A8:A8:A8:A8:A8:A8");

    Mockito.doNothing().when(provisioningService).provisionModem(any(RegistrationRequest.class));
    mockMvc.perform(MockMvcRequestBuilders.post(ModemProvisioningController.BASE_API).contentType(
        MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
                     .andExpect(MockMvcResultMatchers.status().isAccepted());
  }

}
