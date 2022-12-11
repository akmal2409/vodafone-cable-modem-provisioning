package com.akmal.provisioningservice.validator;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class MacValidatorTest {

  Predicate<String> macValidator = new MacValidator();

  @Test
  void shouldTestValidWhen64BitValidMac() {
    final var mac64BitColon = "9F:01:50:31:A6:B7:76:25";
    final var mac64BitDash = "9F-01-50-31-A6-B7-76-25";
    final var mac64BitDot = "9F01.5031.A6B7.7625";

    assertThat(macValidator.test(mac64BitColon)).isTrue();
    assertThat(macValidator.test(mac64BitDash)).isTrue();
    assertThat(macValidator.test(mac64BitDot)).isTrue();
  }

  @Test
  void shouldFailWhenNotationIsMixed64bit() {
    final var mac64BitColon = "9F-01:50:31:A6:B7:76:25";
    final var mac64BitDash = "9F-01:50-31-A6-B7-76-25";
    final var mac64BitDot = "9F01.5031:A6B7.7625";

    assertThat(macValidator.test(mac64BitColon)).isFalse();
    assertThat(macValidator.test(mac64BitDash)).isFalse();
    assertThat(macValidator.test(mac64BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenInvalidHexDigit64bit() {
    final var mac64BitColon = "9F:01:50:31:A6:B7:7Z:25";
    final var mac64BitDash = "9F-01-50-31-A6-B7-Z6-25";
    final var mac64BitDot = "9F01.5031.Z6B7.7625";

    assertThat(macValidator.test(mac64BitColon)).isFalse();
    assertThat(macValidator.test(mac64BitDash)).isFalse();
    assertThat(macValidator.test(mac64BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenShorterThan64bit() {
    final var mac64BitColon = "9F:01:50:31:A6:B7:7B";
    final var mac64BitDash = "9F-01-50-31-A6-B7-7B";
    final var mac64BitDot = "9F01.5031.B6B7.BA";

    assertThat(macValidator.test(mac64BitColon)).isFalse();
    assertThat(macValidator.test(mac64BitDash)).isFalse();
    assertThat(macValidator.test(mac64BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenLongerThan64bit() {
    final var mac64BitColon = "9F:01:50:31:A6:B7:7B:8A:8A";
    final var mac64BitDash = "9F-01-50-31-A6-B7-7B-8A-8A";
    final var mac64BitDot = "9F01.5031.B6B7.BA8A.8A8A";

    assertThat(macValidator.test(mac64BitColon)).isFalse();
    assertThat(macValidator.test(mac64BitDash)).isFalse();
    assertThat(macValidator.test(mac64BitDot)).isFalse();
  }

  @Test
  void shouldTestValidWhen48BitValidMac() {
    final var mac48BitColon = "9F:01:50:31:A6:B7";
    final var mac48BitDash = "9F-01-50-31-A6-B7";
    final var mac48BitDot = "9F01.5031.A6B7";

    assertThat(macValidator.test(mac48BitColon)).isTrue();
    assertThat(macValidator.test(mac48BitDash)).isTrue();
    assertThat(macValidator.test(mac48BitDot)).isTrue();
  }

  @Test
  void shouldFailWheNotationIsMixed48Bit() {
    final var mac48BitColon = "9F-01:50:31:A6:B7";
    final var mac48BitDash = "9F-01:50-31-A6-B7";
    final var mac48BitDot = "9F01:5031.A6B7";

    assertThat(macValidator.test(mac48BitColon)).isFalse();
    assertThat(macValidator.test(mac48BitDash)).isFalse();
    assertThat(macValidator.test(mac48BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenInvalidHexDigit48bit() {
    final var mac48BitColon = "9F:01:50:31:A6:G7";
    final var mac48BitDash = "9F-01-50-31-A6-_7";
    final var mac48BitDot = "9F01.5031.ZGB7";

    assertThat(macValidator.test(mac48BitColon)).isFalse();
    assertThat(macValidator.test(mac48BitDash)).isFalse();
    assertThat(macValidator.test(mac48BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenShorterThan48bit() {
    final var mac48BitColon = "9F:01:50:31:A6";
    final var mac48BitDash = "9F-01-50-31-A6";
    final var mac48BitDot = "9F01.5031";

    assertThat(macValidator.test(mac48BitColon)).isFalse();
    assertThat(macValidator.test(mac48BitDash)).isFalse();
    assertThat(macValidator.test(mac48BitDot)).isFalse();
  }

  @Test
  void shouldFailWhenLongerThan48bit() {
    final var mac48BitColon = "9F:01:50:31:A6:A8:A8";
    final var mac48BitDash = "9F-01-50-31-A6-A8-A8";

    assertThat(macValidator.test(mac48BitColon)).isFalse();
    assertThat(macValidator.test(mac48BitDash)).isFalse();
  }
}
