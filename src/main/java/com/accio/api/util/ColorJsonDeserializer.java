package com.accio.api.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.awt.*;
import java.io.IOException;

public class ColorJsonDeserializer extends JsonDeserializer<Color> {

  @Override
  public Class<?> handledType() {
    return Color.class;
  }

  @Override
  public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    int i = Integer.decode(p.getValueAsString());
    return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, (i >> 24) & 0xFF);
  }
}
