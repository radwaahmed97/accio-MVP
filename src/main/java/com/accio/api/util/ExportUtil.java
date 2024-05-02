package com.accio.api.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ExportUtil {
  private ExportUtil() {
  }

  public enum ExportType {
    csv, xlsx, pdf
  }

  @NonNull
  public static String getContentType(ExportType type) {
    return switch (type) {
      case xlsx -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
      case pdf -> MediaType.APPLICATION_PDF_VALUE;
      case csv -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
    };
  }

  public static String convertToUserFriendlyDateFormat(String date) {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a");
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, inputFormatter);
    ZonedDateTime convertedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    return convertedDateTime.format(outputFormatter);
  }

  public static void setNoCache(HttpServletResponse response) {
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
    response.setHeader(HttpHeaders.PRAGMA, "no-cache");
    response.setHeader(HttpHeaders.EXPIRES, "0");
  }

  public static String convertArraysToString(List<String> list) {
    if (list == null) return "";
    return String.join(",", list);
  }

}
