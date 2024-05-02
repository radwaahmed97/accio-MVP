package com.accio.api.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public final class LogUtils {
  private LogUtils() {
  }

  public static String duration(long start) {
    return durationImpl(System.currentTimeMillis() - start);
  }

  static String durationImpl(long millis) {

    if (millis < 1000) {
      return millis + " milli" + (millis == 1 ? "" : "s");
    }
    var seconds = MILLISECONDS.toSeconds(millis);
    if (seconds < 60) {
      return seconds + " second" + (seconds == 1 ? "" : "s");
    }
    var minutes = MILLISECONDS.toMinutes(millis);
    seconds -= MINUTES.toSeconds(minutes);
    return String.format("%d minute%s, %d second%s",
        minutes, minutes == 1 ? "" : "s", seconds, seconds == 1 ? "" : "s");
  }
}
