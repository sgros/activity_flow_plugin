package locus.api.android.utils;

import android.text.Html;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UtilsFormat {
   public static final float ENERGY_CAL_TO_J = 4.185F;
   public static final double UNIT_KILOMETER_TO_METER = 1000.0D;
   public static final double UNIT_METER_TO_FEET = 3.2808D;
   public static final double UNIT_MILE_TO_METER = 1609.344D;
   public static final double UNIT_NMILE_TO_METER = 1852.0D;
   public static final int VALUE_UNITS_ALTITUDE_FEET = 1;
   public static final int VALUE_UNITS_ALTITUDE_METRES = 0;
   public static final int VALUE_UNITS_ANGLE_ANGULAR_MIL = 1;
   public static final int VALUE_UNITS_ANGLE_DEGREE = 0;
   public static final int VALUE_UNITS_ANGLE_RUSSIAN_MIL = 2;
   public static final int VALUE_UNITS_ANGLE_US_ARTILLERY_MIL = 3;
   public static final int VALUE_UNITS_AREA_ACRE = 5;
   public static final int VALUE_UNITS_AREA_FT_SQ = 3;
   public static final int VALUE_UNITS_AREA_HA = 1;
   public static final int VALUE_UNITS_AREA_KM_SQ = 2;
   public static final int VALUE_UNITS_AREA_MI_SQ = 6;
   public static final int VALUE_UNITS_AREA_M_SQ = 0;
   public static final int VALUE_UNITS_AREA_NM_SQ = 7;
   public static final int VALUE_UNITS_AREA_YA_SQ = 4;
   public static final int VALUE_UNITS_DISTANCE_IM_F = 2;
   public static final int VALUE_UNITS_DISTANCE_IM_FM = 3;
   public static final int VALUE_UNITS_DISTANCE_IM_Y = 4;
   public static final int VALUE_UNITS_DISTANCE_IM_YM = 5;
   public static final int VALUE_UNITS_DISTANCE_ME_M = 0;
   public static final int VALUE_UNITS_DISTANCE_ME_MKM = 1;
   public static final int VALUE_UNITS_DISTANCE_NA_MNMI = 6;
   public static final int VALUE_UNITS_ENERGY_KCAL = 1;
   public static final int VALUE_UNITS_ENERGY_KJ = 0;
   public static final int VALUE_UNITS_SLOPE_DEGREE = 1;
   public static final int VALUE_UNITS_SLOPE_PERCENT = 0;
   public static final int VALUE_UNITS_SPEED_KMH = 0;
   public static final int VALUE_UNITS_SPEED_KNOT = 3;
   public static final int VALUE_UNITS_SPEED_MILH = 1;
   public static final int VALUE_UNITS_SPEED_NMIH = 2;
   public static final int VALUE_UNITS_TEMPERATURE_CELSIUS = 0;
   public static final int VALUE_UNITS_TEMPERATURE_FAHRENHEIT = 1;
   public static final int VALUE_UNITS_WEIGHT_KG = 0;
   public static final int VALUE_UNITS_WEIGHT_LB = 1;
   public static final float WEIGHT_KG_TO_LB = 2.204622F;
   public static double angleInAngularMi = 17.453292519943293D;
   public static double angleInRussianMil = 16.666666666666668D;
   public static double angleInUsArttileryMil = 17.77777777777778D;
   private static DecimalFormat[][] formats;

   static {
      DecimalFormat[] var0 = new DecimalFormat[]{new DecimalFormat("#"), new DecimalFormat("#.0"), new DecimalFormat("#.00"), new DecimalFormat("#.000"), new DecimalFormat("#.0000"), new DecimalFormat("#.00000"), new DecimalFormat("#.000000")};
      DecimalFormat[] var1 = new DecimalFormat[]{new DecimalFormat("#0"), new DecimalFormat("#0.0"), new DecimalFormat("#0.00"), new DecimalFormat("#0.000"), new DecimalFormat("#0.0000"), new DecimalFormat("#0.00000"), new DecimalFormat("#0.000000")};
      DecimalFormat[] var2 = new DecimalFormat[]{new DecimalFormat("#00"), new DecimalFormat("#00.0"), new DecimalFormat("#00.00"), new DecimalFormat("#00.000"), new DecimalFormat("#00.0000"), new DecimalFormat("#00.00000"), new DecimalFormat("#00.000000")};
      DecimalFormat var3 = new DecimalFormat("#000");
      DecimalFormat var4 = new DecimalFormat("#000.0");
      DecimalFormat var5 = new DecimalFormat("#000.00");
      DecimalFormat var6 = new DecimalFormat("#000.000");
      DecimalFormat var7 = new DecimalFormat("#000.0000");
      DecimalFormat var8 = new DecimalFormat("#000.00000");
      DecimalFormat var9 = new DecimalFormat("#000.000000");
      DecimalFormat[] var10 = new DecimalFormat[]{new DecimalFormat("#0000"), new DecimalFormat("#0000.0"), new DecimalFormat("#0000.00"), new DecimalFormat("#0000.000"), new DecimalFormat("#0000.0000"), new DecimalFormat("#0000.00000"), new DecimalFormat("#0000.000000")};
      DecimalFormat[] var11 = new DecimalFormat[]{new DecimalFormat("#00000"), new DecimalFormat("#00000.0"), new DecimalFormat("#00000.00"), new DecimalFormat("#00000.000"), new DecimalFormat("#00000.0000"), new DecimalFormat("#00000.00000"), new DecimalFormat("#00000.000000")};
      DecimalFormat var12 = new DecimalFormat("#000000");
      DecimalFormat var13 = new DecimalFormat("#000000.0");
      DecimalFormat var14 = new DecimalFormat("#000000.00");
      DecimalFormat var15 = new DecimalFormat("#000000.000");
      DecimalFormat var16 = new DecimalFormat("#000000.0000");
      DecimalFormat var17 = new DecimalFormat("#000000.00000");
      DecimalFormat var18 = new DecimalFormat("#000000.000000");
      DecimalFormat var19 = new DecimalFormat("#0000000");
      DecimalFormat var20 = new DecimalFormat("#0000000.0");
      DecimalFormat var21 = new DecimalFormat("#0000000.00");
      DecimalFormat var22 = new DecimalFormat("#0000000.000");
      DecimalFormat var23 = new DecimalFormat("#0000000.0000");
      DecimalFormat var24 = new DecimalFormat("#0000000.00000");
      DecimalFormat var25 = new DecimalFormat("#0000000.000000");
      formats = new DecimalFormat[][]{var0, var1, var2, {var3, var4, var5, var6, var7, var8, var9}, var10, var11, {var12, var13, var14, var15, var16, var17, var18}, {var19, var20, var21, var22, var23, var24, var25}};
      DecimalFormat[][] var30 = formats;
      int var26 = var30.length;

      for(int var27 = 0; var27 < var26; ++var27) {
         DecimalFormat[] var31 = var30[var27];
         int var28 = var31.length;

         for(int var29 = 0; var29 < var28; ++var29) {
            var31[var29].setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
         }
      }

   }

   public static String formatAltitude(int var0, double var1, boolean var3) {
      String var4 = formatDouble(formatAltitudeValue(var0, var1), 0);
      String var5 = var4;
      if (var3) {
         var5 = var4 + " " + formatAltitudeUnits(var0);
      }

      return var5;
   }

   public static String formatAltitudeUnits(int var0) {
      String var1;
      if (var0 == 1) {
         var1 = "ft";
      } else {
         var1 = "m";
      }

      return var1;
   }

   public static double formatAltitudeValue(int var0, double var1) {
      double var3 = var1;
      if (var0 == 1) {
         var3 = var1 * 3.2808D;
      }

      return var3;
   }

   public static String formatAngle(int var0, float var1, boolean var2, int var3) {
      double var4 = formatAngleValue(var0, (double)var1, var2, var3);
      String var6 = formatAngleUnits(var0);
      return formatDouble(var4, var3) + var6;
   }

   public static String formatAngleUnits(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "°";
      } else {
         var1 = "";
      }

      return var1;
   }

   public static double formatAngleValue(int var0, double var1, boolean var3, int var4) {
      double var5 = var1;
      if (var3) {
         var5 = optimizeAngleValue(var1, var4);
      }

      if (var0 == 1) {
         var1 = var5 * angleInAngularMi;
      } else if (var0 == 2) {
         var1 = var5 * angleInRussianMil;
      } else {
         var1 = var5;
         if (var0 == 3) {
            var1 = var5 * angleInUsArttileryMil;
         }
      }

      return var1;
   }

   public static CharSequence formatArea(int var0, double var1, boolean var3) {
      StringBuilder var4 = new StringBuilder();
      if (!var3) {
         var4.append(formatAreaValue(var0, var1));
      } else {
         var4.append(formatAreaValue(var0, var1)).append(" ").append(formatAreaUnit(var0, var1));
      }

      return Html.fromHtml(var4.toString());
   }

   public static CharSequence formatAreaUnit(int var0, double var1) {
      String var3 = "";
      switch(var0) {
      case 0:
         var3 = "m&sup2;";
         break;
      case 1:
         var3 = "ha";
         break;
      case 2:
         var3 = "km&sup2;";
         break;
      case 3:
         var3 = "ft&sup2;";
         break;
      case 4:
         var3 = "yd&sup2;";
         break;
      case 5:
         var3 = "acre";
         break;
      case 6:
         var3 = "mi&sup2;";
         break;
      case 7:
         var3 = "nm&sup2;";
      }

      return Html.fromHtml(var3);
   }

   public static String formatAreaValue(int var0, double var1) {
      String var3;
      switch(var0) {
      case 0:
         var3 = formatDouble(var1, 0);
         return var3;
      case 1:
         var1 /= 10000.0D;
         break;
      case 2:
         var1 /= 1000000.0D;
         break;
      case 3:
         var3 = formatDouble(var1 / 0.09290304D, 0);
         return var3;
      case 4:
         var3 = formatDouble(var1 / 0.83612736D, 0);
         return var3;
      case 5:
         var1 /= 4046.8564224D;
         break;
      case 6:
         var1 /= 2589988.110336D;
         break;
      case 7:
         var1 /= 3429904.0D;
         break;
      default:
         var3 = "";
         return var3;
      }

      if (var1 < 100.0D) {
         var3 = formatDouble(var1, 2);
      } else if (var1 < 1000.0D) {
         var3 = formatDouble(var1, 1);
      } else {
         var3 = formatDouble(var1, 0);
      }

      return var3;
   }

   private static String formatDistance(double var0, int var2) {
      String var3;
      if (var0 < 10.0D) {
         var3 = formatDouble(var0, var2);
      } else {
         if (var2 > 0) {
            --var2;
         } else {
            var2 = 0;
         }

         var3 = formatDouble(var0, var2);
      }

      return var3;
   }

   public static String formatDistance(int var0, double var1, UtilsFormat.UnitsPrecision var3, boolean var4) {
      String var5;
      String var8;
      var5 = null;
      double var6;
      label62:
      switch(var0) {
      case 0:
         switch(var3) {
         case LOW:
         case MEDIUM:
            var8 = formatDouble(var1, 0);
            break label62;
         case HIGH:
            var8 = formatDouble(var1, 1);
            break label62;
         default:
            var8 = var5;
            break label62;
         }
      case 1:
         switch(var3) {
         case LOW:
         case MEDIUM:
            if (var1 >= 1000.0D) {
               var6 = var1 / 1000.0D;
               if (var6 >= 100.0D) {
                  var8 = formatDouble(var6, 0);
               } else {
                  var8 = formatDouble(var6, 1);
               }
            } else {
               var8 = formatDistance(var1, 0);
            }
            break label62;
         case HIGH:
            if (var1 >= 1000.0D) {
               var6 = var1 / 1000.0D;
               if (var6 >= 100.0D) {
                  var8 = formatDouble(var6, 1);
               } else {
                  var8 = formatDouble(var6, 2);
               }
            } else {
               var8 = formatDouble(var1, 1);
            }
            break label62;
         default:
            var8 = var5;
            break label62;
         }
      case 2:
         switch(var3) {
         case LOW:
         case MEDIUM:
            var8 = formatDistance(3.2808D * var1, 0);
            break label62;
         case HIGH:
            var8 = formatDistance(3.2808D * var1, 1);
            break label62;
         default:
            var8 = var5;
            break label62;
         }
      case 3:
         var8 = formatDistanceImperial(var1, 3.2808D * var1, var3);
         break;
      case 4:
         switch(var3) {
         case LOW:
         case MEDIUM:
            var8 = formatDistance(1.0936D * var1, 0);
            break label62;
         case HIGH:
            var8 = formatDistance(1.0936D * var1, 1);
            break label62;
         default:
            var8 = var5;
            break label62;
         }
      case 5:
         var8 = formatDistanceImperial(var1, 1.0936D * var1, var3);
         break;
      case 6:
         switch(var3) {
         case LOW:
         case MEDIUM:
            if (var1 > 1852.0D) {
               var6 = var1 / 1852.0D;
               if (var6 >= 100.0D) {
                  var8 = formatDouble(var6, 0);
               } else {
                  var8 = formatDouble(var6, 1);
               }
            } else {
               var8 = formatDistance(var1, 0);
            }
            break label62;
         case HIGH:
            if (var1 > 1852.0D) {
               var6 = var1 / 1852.0D;
               if (var6 >= 100.0D) {
                  var8 = formatDouble(var6, 1);
               } else {
                  var8 = formatDouble(var6, 2);
               }
            } else {
               var8 = formatDistance(var1, 1);
            }
            break label62;
         default:
            var8 = var5;
            break label62;
         }
      default:
         var8 = var5;
      }

      var5 = var8;
      if (var4) {
         var5 = var8 + " " + formatDistanceUnits(var0, var1);
      }

      return var5;
   }

   public static String formatDistance(int var0, double var1, boolean var3) {
      UtilsFormat.UnitsPrecision var4 = UtilsFormat.UnitsPrecision.MEDIUM;
      if (!var3) {
         var3 = true;
      } else {
         var3 = false;
      }

      return formatDistance(var0, var1, var4, var3);
   }

   private static String formatDistanceImperial(double var0, double var2, UtilsFormat.UnitsPrecision var4) {
      String var5;
      switch(var4) {
      case LOW:
      case MEDIUM:
         if (var2 >= 1000.0D) {
            var0 /= 1609.344D;
            if (var0 >= 100.0D) {
               var5 = formatDouble(var0, 0);
            } else if (var0 >= 1.0D) {
               var5 = formatDouble(var0, 1);
            } else {
               var5 = formatDouble(var0, 2);
            }
         } else {
            var5 = formatDistance(var2, 0);
         }
         break;
      case HIGH:
         if (var2 >= 1000.0D) {
            var0 /= 1609.344D;
            if (var0 >= 100.0D) {
               var5 = formatDouble(var0, 1);
            } else if (var0 >= 1.0D) {
               var5 = formatDouble(var0, 2);
            } else {
               var5 = formatDouble(var0, 3);
            }
         } else {
            var5 = formatDistance(var2, 1);
         }
         break;
      default:
         var5 = "";
      }

      return var5;
   }

   public static String formatDistanceUnits(int var0, double var1) {
      String var3;
      switch(var0) {
      case 0:
         var3 = "m";
         break;
      case 1:
         if (var1 >= 1000.0D) {
            var3 = "km";
         } else {
            var3 = "m";
         }
         break;
      case 2:
         var3 = " ft";
         break;
      case 3:
         if (var1 * 3.2808D >= 1000.0D) {
            var3 = "mi";
         } else {
            var3 = "ft";
         }
         break;
      case 4:
         var3 = "yd";
         break;
      case 5:
         if (var1 * 1.0936D >= 1000.0D) {
            var3 = "mi";
         } else {
            var3 = "yd";
         }
         break;
      case 6:
         if (var1 >= 1852.0D) {
            var3 = "nmi";
         } else {
            var3 = "m";
         }
         break;
      default:
         var3 = "";
      }

      return var3;
   }

   public static double formatDistanceValue(int var0, double var1) {
      double var3 = var1;
      switch(var0) {
      case 0:
         break;
      case 1:
         var3 = var1;
         if (var1 >= 1000.0D) {
            var3 = var1 / 1000.0D;
         }
         break;
      case 2:
         var3 = var1 * 3.2808D;
         break;
      case 3:
         var3 = var1 * 3.2808D;
         if (var3 >= 1000.0D) {
            var3 = var1 / 1609.344D;
         }
         break;
      case 4:
         var3 = var1 * 1.0936D;
         break;
      case 5:
         var3 = var1 * 1.0936D;
         if (var3 >= 1000.0D) {
            var3 = var1 / 1609.344D;
         }
         break;
      case 6:
         var3 = var1;
         if (var1 >= 1852.0D) {
            var3 = var1 / 1852.0D;
         }
         break;
      default:
         var3 = var1;
      }

      return var3;
   }

   public static String formatDouble(double var0, int var2) {
      return formatDouble(var0, var2, 1);
   }

   public static String formatDouble(double var0, int var2, int var3) {
      int var4;
      if (var3 < 0) {
         var4 = 0;
      } else {
         var4 = var3;
         if (var3 > formats.length - 1) {
            var4 = formats.length - 1;
         }
      }

      if (var2 < 0) {
         var3 = 0;
      } else {
         var3 = var2;
         if (var2 > formats[0].length - 1) {
            var3 = formats[0].length - 1;
         }
      }

      return formats[var4][var3].format(var0);
   }

   public static String formatEnergy(int var0, int var1, boolean var2) {
      String var3 = formatEnergyValue(var0, var1);
      String var4 = var3;
      if (var2) {
         var4 = var3 + " " + formatEnergyUnit(var0);
      }

      return var4;
   }

   public static String formatEnergyUnit(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "KJ";
      } else {
         var1 = "kcal";
      }

      return var1;
   }

   public static String formatEnergyValue(int var0, int var1) {
      String var2;
      if (var0 == 0) {
         var2 = formatDouble((double)var1 * 1.0D / 1000.0D, 0);
      } else {
         var2 = formatDouble((double)var1 * 1.0D / 4184.999942779541D, 0);
      }

      return var2;
   }

   public static String formatSlope(int var0, double var1, boolean var3) {
      String var4 = formatSlopeValue(var0, var1);
      String var5 = var4;
      if (var3) {
         var5 = var4 + " " + formatSlopeUnit(var0);
      }

      return var5;
   }

   public static String formatSlopeUnit(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "%";
      } else {
         var1 = "°";
      }

      return var1;
   }

   public static String formatSlopeValue(int var0, double var1) {
      String var3;
      if (var0 == 0) {
         var3 = formatDouble(100.0D * var1, 0);
      } else {
         var3 = formatDouble(Math.atan(var1) * 57.29577951308232D, 0);
      }

      return var3;
   }

   public static String formatSpeed(int var0, double var1, boolean var3) {
      String var4;
      if (var1 < 0.0D) {
         var4 = "--";
      } else {
         var1 = formatSpeedValue(var0, var1);
         byte var5;
         if (var1 > 100.0D) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         var4 = formatDouble(var1, var5);
      }

      if (!var3) {
         var4 = var4 + " " + formatSpeedUnits(var0);
      }

      return var4;
   }

   public static String formatSpeedUnits(int var0) {
      String var1;
      if (var0 == 1) {
         var1 = "mi/h";
      } else if (var0 == 2) {
         var1 = "nmi/h";
      } else if (var0 == 3) {
         var1 = "kn";
      } else {
         var1 = "km/h";
      }

      return var1;
   }

   public static double formatSpeedValue(int var0, double var1) {
      if (var0 == 1) {
         var1 *= 2.237D;
      } else if (var0 != 2 && var0 != 3) {
         var1 *= 3.6D;
      } else {
         var1 *= 1.9438444924406046D;
      }

      return var1;
   }

   public static String formatTemperature(int var0, float var1, boolean var2) {
      String var3 = formatTemperatureValue(var0, var1);
      String var4 = var3;
      if (var2) {
         var4 = var3 + " " + formatTemperatureUnit(var0);
      }

      return var4;
   }

   public static String formatTemperatureUnit(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "°C";
      } else {
         var1 = "F";
      }

      return var1;
   }

   public static String formatTemperatureValue(int var0, float var1) {
      String var2;
      if (var0 == 0) {
         var2 = formatDouble((double)var1, 1);
      } else {
         var2 = formatDouble((double)(9.0F * var1 / 5.0F + 32.0F), 1);
      }

      return var2;
   }

   public static String formatWeight(int var0, double var1, boolean var3) {
      String var4 = formatWeightValue(var0, var1);
      String var5 = var4;
      if (var3) {
         var5 = var4 + " " + formatWeightUnit(var0);
      }

      return var5;
   }

   public static String formatWeightUnit(int var0) {
      String var1;
      if (var0 == 0) {
         var1 = "kg";
      } else if (var0 == 1) {
         var1 = "lb";
      } else {
         var1 = "???";
      }

      return var1;
   }

   public static String formatWeightValue(int var0, double var1) {
      String var3;
      if (var0 == 0) {
         var3 = formatDouble(1.0D * var1 / 1000.0D, 1);
      } else {
         var3 = formatDouble(2.2046220302581787D * var1 / 1000.0D, 1);
      }

      return var3;
   }

   private static double optimizeAngleValue(double var0, int var2) {
      int var3 = (int)Math.pow(10.0D, (double)var2);
      var0 = (double)Math.round((double)var3 * var0);
      double var4;
      if (var2 == 0) {
         var4 = var0;
         if (var0 < -0.5D) {
            var4 = var0 + 360.0D;
         }

         var0 = var4;
         if (var4 >= 359.5D) {
            var0 = var4 - 360.0D;
         }
      } else {
         var4 = var0;
         if (var0 < 0.0D) {
            var4 = var0 + (double)var3 * 360.0D;
         }

         var0 = var4;
         if (var4 >= (double)var3 * 360.0D) {
            var0 = var4 - (double)(360.0F * (float)var3);
         }
      }

      return var0 / (double)var3;
   }

   public static enum UnitsPrecision {
      HIGH,
      LOW,
      MEDIUM;
   }
}
