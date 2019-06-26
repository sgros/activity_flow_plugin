package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import se.krka.kahlua.stdlib.MathLib;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;

public class ZonePoint implements LuaTable, Serializable {
   public static final double DEG_PI = 57.29577951308232D;
   public static final double LATITUDE_COEF = 110940.00000395167D;
   public static final double METRE_COEF = 9.013881377E-6D;
   public static final double PI_180 = 0.017453292519943295D;
   public static final double PI_2 = 1.5707963267948966D;
   public static final Hashtable conversions = new Hashtable(6);
   public double altitude = 0.0D;
   public double latitude = 0.0D;
   public double longitude = 0.0D;

   static {
      conversions.put("feet", new Double(0.3048D));
      conversions.put("ft", new Double(0.3048D));
      conversions.put("miles", new Double(1609.344D));
      conversions.put("meters", new Double(1.0D));
      conversions.put("kilometers", new Double(1000.0D));
      conversions.put("nauticalmiles", new Double(1852.0D));
   }

   public ZonePoint() {
   }

   public ZonePoint(double var1, double var3, double var5) {
      this.latitude = var1;
      this.longitude = var3;
      this.altitude = var5;
   }

   public ZonePoint(ZonePoint var1) {
      this.latitude = var1.latitude;
      this.longitude = var1.longitude;
      this.altitude = var1.altitude;
   }

   public static double angle2azimuth(double var0) {
      var0 = -((var0 - 1.5707963267948966D) * 57.29577951308232D);

      while(true) {
         double var2 = var0;
         if (var0 >= 0.0D) {
            while(var2 >= 360.0D) {
               var2 -= 360.0D;
            }

            return var2;
         }

         var0 += 360.0D;
      }
   }

   public static double azimuth2angle(double var0) {
      var0 = -(0.017453292519943295D * var0) + 1.5707963267948966D;

      while(true) {
         double var2 = var0;
         if (var0 <= 3.141592653589793D) {
            while(var2 <= -3.141592653589793D) {
               var2 += 6.283185307179586D;
            }

            return var2;
         }

         var0 -= 6.283185307179586D;
      }
   }

   public static double convertDistanceFrom(double var0, String var2) {
      double var3 = var0;
      if (var2 != null) {
         var3 = var0;
         if (conversions.containsKey(var2)) {
            var3 = var0 * (Double)conversions.get(var2);
         }
      }

      return var3;
   }

   public static double convertDistanceTo(double var0, String var2) {
      double var3 = var0;
      if (var2 != null) {
         var3 = var0;
         if (conversions.containsKey(var2)) {
            var3 = var0 / (Double)conversions.get(var2);
         }
      }

      return var3;
   }

   public static ZonePoint copy(ZonePoint var0) {
      if (var0 == null) {
         var0 = null;
      } else {
         var0 = new ZonePoint(var0);
      }

      return var0;
   }

   public static double distance(double var0, double var2, double var4, double var6) {
      var0 = Math.abs(lat2m(var0 - var4));
      var2 = Math.abs(lon2m(var4, var2 - var6));
      return Math.sqrt(var0 * var0 + var2 * var2);
   }

   public static double lat2m(double var0) {
      return 110940.00000395167D * var0;
   }

   public static double lon2m(double var0, double var2) {
      return var2 * 0.017453292519943295D * Math.cos(0.017453292519943295D * var0) * 6367449.0D;
   }

   public static double m2lat(double var0) {
      return 9.013881377E-6D * var0;
   }

   public static double m2lon(double var0, double var2) {
      return var2 / (Math.cos(var0 * 0.017453292519943295D) * 0.017453292519943295D * 6367449.0D);
   }

   public static String makeFriendlyAngle(double var0) {
      boolean var2 = false;
      double var3 = var0;
      if (var0 < 0.0D) {
         var2 = true;
         var3 = var0 * -1.0D;
      }

      int var5 = (int)var3;
      String var6 = String.valueOf((var3 - (double)var5) * 60.0D);
      String var7 = var6;
      if (var6.indexOf(46) != -1) {
         var7 = var6.substring(0, Math.min(var6.length(), var6.indexOf(46) + 5));
      }

      StringBuilder var8 = new StringBuilder();
      if (var2) {
         var6 = "- ";
      } else {
         var6 = "+ ";
      }

      return var8.append(var6).append(String.valueOf(var5)).append("° ").append(var7).toString();
   }

   public static String makeFriendlyDistance(double var0) {
      String var2;
      if (var0 > 1500.0D) {
         var0 = (double)((long)(var0 / 10.0D)) / 100.0D;
         var2 = Double.toString(var0) + " km";
      } else if (var0 > 100.0D) {
         var2 = Double.toString((double)((long)var0)) + " m";
      } else {
         var0 = (double)((long)(var0 * 100.0D)) / 100.0D;
         var2 = Double.toString(var0) + " m";
      }

      return var2;
   }

   public static String makeFriendlyLatitude(double var0) {
      return makeFriendlyAngle(var0).replace('+', 'N').replace('-', 'S');
   }

   public static String makeFriendlyLongitude(double var0) {
      return makeFriendlyAngle(var0).replace('+', 'E').replace('-', 'W');
   }

   public double bearing(double var1, double var3) {
      return MathLib.atan2(lat2m(this.latitude - var1), lon2m(var1, this.longitude - var3));
   }

   public double bearing(ZonePoint var1) {
      return this.bearing(var1.latitude, var1.longitude);
   }

   public void deserialize(DataInputStream var1) throws IOException {
      this.latitude = var1.readDouble();
      this.longitude = var1.readDouble();
      this.altitude = var1.readDouble();
   }

   public double distance(double var1, double var3) {
      return distance(var1, var3, this.latitude, this.longitude);
   }

   public double distance(ZonePoint var1) {
      return distance(var1.latitude, var1.longitude, this.latitude, this.longitude);
   }

   public String friendlyDistance(double var1, double var3) {
      return makeFriendlyDistance(this.distance(var1, var3));
   }

   public LuaTable getMetatable() {
      return null;
   }

   public int len() {
      return 3;
   }

   public Object next(Object var1) {
      return null;
   }

   public Object rawget(Object var1) {
      Object var2 = null;
      Double var4;
      if (var1 == null) {
         var4 = (Double)var2;
      } else {
         String var3 = var1.toString();
         if ("latitude".equals(var3)) {
            var4 = LuaState.toDouble(this.latitude);
         } else if ("longitude".equals(var3)) {
            var4 = LuaState.toDouble(this.longitude);
         } else {
            var4 = (Double)var2;
            if ("altitude".equals(var3)) {
               var4 = LuaState.toDouble(this.altitude);
            }
         }
      }

      return var4;
   }

   public void rawset(Object var1, Object var2) {
      if (var1 != null) {
         String var3 = var1.toString();
         if ("latitude".equals(var3)) {
            this.latitude = LuaState.fromDouble(var2);
         } else if ("longitude".equals(var3)) {
            this.longitude = LuaState.fromDouble(var2);
         } else if ("altitude".equals(var3)) {
            this.altitude = LuaState.fromDouble(var2);
         }
      }

   }

   public void serialize(DataOutputStream var1) throws IOException {
      var1.writeDouble(this.latitude);
      var1.writeDouble(this.longitude);
      var1.writeDouble(this.altitude);
   }

   public void setMetatable(LuaTable var1) {
   }

   public void sync(ZonePoint var1) {
      this.latitude = var1.latitude;
      this.longitude = var1.longitude;
   }

   public String toString() {
      return "ZonePoint(" + this.latitude + "," + this.longitude + "," + this.altitude + ")";
   }

   public ZonePoint translate(double var1, double var3) {
      double var5 = azimuth2angle(var1);
      var1 = m2lat(Math.sin(var5) * var3);
      var3 = m2lon(this.latitude, Math.cos(var5) * var3);
      return new ZonePoint(this.latitude + var1, this.longitude + var3, this.altitude);
   }

   public void updateWeakSettings(boolean var1, boolean var2) {
   }
}
