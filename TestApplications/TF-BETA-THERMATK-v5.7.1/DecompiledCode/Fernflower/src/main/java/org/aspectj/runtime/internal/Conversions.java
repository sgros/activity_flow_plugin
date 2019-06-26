package org.aspectj.runtime.internal;

public final class Conversions {
   public static Object booleanObject(boolean var0) {
      return new Boolean(var0);
   }

   public static Object doubleObject(double var0) {
      return new Double(var0);
   }

   public static Object floatObject(float var0) {
      return new Float(var0);
   }

   public static Object intObject(int var0) {
      return new Integer(var0);
   }

   public static Object longObject(long var0) {
      return new Long(var0);
   }
}
