package com.akmal;

public record ModemProvisioningEvent(
    String macAddress,
    long timestamp
) {

}
