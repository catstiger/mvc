package com.github.catstiger.mvc.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class ObjectUtils {
  private ObjectUtils() {
    
  }
  
  /**
   * Convert the given array (which may be a primitive array) to an
   * object array (if necessary of primitive wrapper objects).
   * <p>A {@code null} source value will be converted to an
   * empty Object array.
   * @param source the (potentially primitive) array
   * @return the corresponding object array (never {@code null})
   * @throws IllegalArgumentException if the parameter is not an array
   */
  public static Object[] toObjectArray(Object source) {
    if (source instanceof Object[]) {
      return (Object[]) source;
    }
    if (source == null) {
      return new Object[0];
    }
    if (!source.getClass().isArray()) {
      throw new IllegalArgumentException("Source is not an array: " + source);
    }
    int length = Array.getLength(source);
    if (length == 0) {
      return new Object[0];
    }
    Class<?> wrapperType = Array.get(source, 0).getClass();
    Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
    for (int i = 0; i < length; i++) {
      newArray[i] = Array.get(source, i);
    }
    return newArray;
  }
  
  /**
   * Determine if the given objects are equal, returning {@code true}
   * if both are {@code null} or {@code false} if only one is
   * {@code null}.
   * <p>Compares arrays with {@code Arrays.equals}, performing an equality
   * check based on the array elements rather than the array reference.
   * @param o1 first Object to compare
   * @param o2 second Object to compare
   * @return whether the given objects are equal
   * @see java.util.Arrays#equals
   */
  public static boolean nullSafeEquals(Object o1, Object o2) {
    if (o1 == o2) {
      return true;
    }
    if (o1 == null || o2 == null) {
      return false;
    }
    if (o1.equals(o2)) {
      return true;
    }
    if (o1.getClass().isArray() && o2.getClass().isArray()) {
      if (o1 instanceof Object[] && o2 instanceof Object[]) {
        return Arrays.equals((Object[]) o1, (Object[]) o2);
      }
      if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
        return Arrays.equals((boolean[]) o1, (boolean[]) o2);
      }
      if (o1 instanceof byte[] && o2 instanceof byte[]) {
        return Arrays.equals((byte[]) o1, (byte[]) o2);
      }
      if (o1 instanceof char[] && o2 instanceof char[]) {
        return Arrays.equals((char[]) o1, (char[]) o2);
      }
      if (o1 instanceof double[] && o2 instanceof double[]) {
        return Arrays.equals((double[]) o1, (double[]) o2);
      }
      if (o1 instanceof float[] && o2 instanceof float[]) {
        return Arrays.equals((float[]) o1, (float[]) o2);
      }
      if (o1 instanceof int[] && o2 instanceof int[]) {
        return Arrays.equals((int[]) o1, (int[]) o2);
      }
      if (o1 instanceof long[] && o2 instanceof long[]) {
        return Arrays.equals((long[]) o1, (long[]) o2);
      }
      if (o1 instanceof short[] && o2 instanceof short[]) {
        return Arrays.equals((short[]) o1, (short[]) o2);
      }
    }
    return false;
  }
  
  /**
   * Determine whether the given object is empty.
   * <p>This method supports the following object types.
   * <ul>
   * <li>{@code Array}: considered empty if its length is zero</li>
   * <li>{@link CharSequence}: considered empty if its length is zero</li>
   * <li>{@link Collection}: delegates to {@link Collection#isEmpty()}</li>
   * <li>{@link Map}: delegates to {@link Map#isEmpty()}</li>
   * </ul>
   * <p>If the given object is non-null and not one of the aforementioned
   * supported types, this method returns {@code false}.
   * @param obj the object to check
   * @return {@code true} if the object is {@code null} or <em>empty</em>
   * @since 4.2
   * @see ObjectUtils#isEmpty(Object[])
   * @see StringUtils#hasLength(CharSequence)
   * @see StringUtils#isEmpty(Object)
   * @see CollectionUtils#isEmpty(java.util.Collection)
   * @see CollectionUtils#isEmpty(java.util.Map)
   */
  @SuppressWarnings("rawtypes")
  public static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    }

    if (obj.getClass().isArray()) {
      return Array.getLength(obj) == 0;
    }
    if (obj instanceof CharSequence) {
      return ((CharSequence) obj).length() == 0;
    }
    if (obj instanceof Collection) {
      return ((Collection) obj).isEmpty();
    }
    if (obj instanceof Map) {
      return ((Map) obj).isEmpty();
    }

    // else
    return false;
  }


}
