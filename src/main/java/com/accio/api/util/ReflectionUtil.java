package com.accio.api.util;

import java.lang.reflect.Field;

public final class ReflectionUtil {
  private ReflectionUtil() {
  }

  @SuppressWarnings("unchecked")
  public static <V> V get(Object object, String fieldName) {
    var clazz = object.getClass();
    Field field = null;
    var wasAccessible = false;
    while (clazz != null) {
      try {
        field = clazz.getDeclaredField(fieldName);
        wasAccessible = field.canAccess(object);
        if (!wasAccessible) {
          field.setAccessible(true);
        }
        return (V) field.get(object);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      } finally {
        if (field != null && field.canAccess(object) != wasAccessible) {
          field.setAccessible(wasAccessible);
        }
      }
    }
    return null;
  }
}
