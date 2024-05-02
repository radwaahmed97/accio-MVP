package com.accio.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.awt.*;
import java.io.IOException;

public class ColorJsonSerializer extends JsonSerializer<Color> {
  @Override
  public Class<Color> handledType() {
    return Color.class;
  }

  @Override
  public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      gen.writeNull();
    } else {
      var argb = Integer.toHexString(value.getRGB());
//      if (argb.length() < 8) {
//        argb = "0" + argb;
//      }
      gen.writeString("#" + argb);
    }
  }
}
