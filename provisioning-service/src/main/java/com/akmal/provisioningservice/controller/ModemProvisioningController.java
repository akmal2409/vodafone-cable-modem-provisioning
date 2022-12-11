package com.akmal.provisioningservice.controller;

import com.akmal.provisioningservice.dto.RegistrationRequest;
import com.akmal.provisioningservice.exceptions.InvalidMacException;
import com.akmal.provisioningservice.exceptions.MessageRejectedException;
import com.akmal.provisioningservice.service.ProvisioningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ModemProvisioningController.BASE_API)
public class ModemProvisioningController {
  public static final String BASE_API = "/api/v1/modems";
  private final ProvisioningService provisioningService;

  public ModemProvisioningController(ProvisioningService provisioningService) {
    this.provisioningService = provisioningService;
  }

  @PostMapping
  public ResponseEntity<Void> registerByMac(@RequestBody RegistrationRequest registrationRequest) {
    try {
      provisioningService.provisionModem(registrationRequest);
      return ResponseEntity.status(HttpStatus.ACCEPTED.value()).build();
    } catch (InvalidMacException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
    } catch (MessageRejectedException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
  }
}
