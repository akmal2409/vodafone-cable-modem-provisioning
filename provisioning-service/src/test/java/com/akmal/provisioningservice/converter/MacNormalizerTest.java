package com.akmal.provisioningservice.converter;


import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.*;

class MacNormalizerTest {

  Converter<String, String> macNormalizer = new MacNormalizer();

  @Test
  void shouldLeaveIntactColonNotation() {
    final var mac48bit = "01:23:45:67:89:AB";
    final var mac64bit = "01:23:45:67:89:AB:CD:EF";

    assertThat(macNormalizer.convert(mac48bit)).isEqualTo(mac48bit);
    assertThat(macNormalizer.convert(mac64bit)).isEqualTo(mac64bit);
  }

  @Test
  void shouldConvertDashNotation() {
    final var expectedMac48bit = "01:23:45:67:89:AB";
    final var expectedMac64bit = "01:23:45:67:89:AB:CD:EF";

    final var mac48bit = "01-23-45-67-89-AB";
    final var mac64bit = "01-23-45-67-89-AB-CD-EF";

    assertThat(macNormalizer.convert(mac48bit)).isEqualTo(expectedMac48bit);
    assertThat(macNormalizer.convert(mac64bit)).isEqualTo(expectedMac64bit);
  }

  @Test
  void shouldConvertDotNotation() {
    final var expectedMac48bit = "01:23:45:67:89:AB";
    final var expectedMac64bit = "01:23:45:67:89:AB:CD:EF";

    final var mac48bit = "0123.4567.89AB";
    final var mac64bit = "0123.4567.89AB.CDEF";

    assertThat(macNormalizer.convert(mac48bit)).isEqualTo(expectedMac48bit);
    assertThat(macNormalizer.convert(mac64bit)).isEqualTo(expectedMac64bit);
  }
}
