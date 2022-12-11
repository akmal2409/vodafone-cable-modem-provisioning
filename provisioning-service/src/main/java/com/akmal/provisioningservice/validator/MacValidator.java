package com.akmal.provisioningservice.validator;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class MacValidator implements Predicate<String> {

  /**
   * The following patten is able to match both 64 bit and more common 48 bit mac addresses.
   * The following notations are supported:
   * 1) 64-bit A7:B8:89:01:A7:B8:89:01 or A7-B8-89-01-A7-B8-89-01 or A7B8.8901.A7B8.8901
   * 1) 48-bit A7:B8:89:01:A7:B8 or A7-B8-89-01-A7-B8 or A7B8.8901.A7B8
   */
  private static final Pattern MAC_48_64_PATTERN = Pattern.compile("^(?!.*[_G-Z])(?:(?:\\w\\w([:-]?)(?:(?:\\w\\w\\1){6}|(?:\\w\\w\\1){4})\\w\\w$)|^(?:\\w{4}\\.){2,3}\\w{4})$");
  @Override
  public boolean test(String s) {
    if (s == null) return false;

    return MAC_48_64_PATTERN.matcher(s).matches();
  }
}
