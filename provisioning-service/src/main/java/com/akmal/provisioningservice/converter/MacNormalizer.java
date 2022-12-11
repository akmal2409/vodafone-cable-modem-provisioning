package com.akmal.provisioningservice.converter;

import com.akmal.provisioningservice.validator.MacValidator;
import org.springframework.core.convert.converter.Converter;

/**
 * Normalizes 48-bit and 64-bit mac addresses to the colon notation.
 * Example: A8-A8-A8-A8-A8-A8 to A8:A8:A8:A8:A8:A8.
 * Invariant: Supplied mac must conform to the valid form. See {@link MacValidator}
 */
public class MacNormalizer implements Converter<String, String> {

  @Override
  public String convert(String mac) {
    if (mac == null || mac.trim().isBlank()) throw new IllegalArgumentException("Supplied mac was null or empty");
    else if (mac.contains(":")) return mac; // since our invariant is that supplied mac should be valid, once we know that it contains ':' as a separator and its a valid mac, we can just return it
    else if (mac.contains("-")) return mac.replaceAll("-", ":");
    else if (mac.contains(".")) {
      // following part assumes valid 64 or 48 bit mac, in such case we can easily compute the permutation of letters and digits in a new string.
      final int periodCount = (mac.length()>>>2) - 1;
      final int digitCount = mac.length() - periodCount;
      final int colonCount = (digitCount>>>1) - 1;

      final char[] normalized = new char[digitCount + colonCount];
      int i = 0;
      int shift = 0;

      for (char character: mac.toCharArray()) {
        if (!Character.isLetterOrDigit(character)) continue;
        normalized[shift + i++] = character;

        if (shift + i < normalized.length - 2 && i % 2 == 0) {
          // after each pair we need to place the colon, however, we should not increment i because then we will loose the group ordering.
          normalized[shift + i] = ':';
          shift++;
        }
      }

      return new String(normalized);
    } else {
      throw new IllegalArgumentException("No valid separator found for a provided mac address");
    }
  }

}
