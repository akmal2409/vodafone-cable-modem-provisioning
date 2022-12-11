package com.akmal.provisioningservice.events.model;

public record ModemProvisioningEvent(
    String macAddress,
    long timestamp
) {

}
