package com.google.zxing.client.android.camera;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class CameraConfigurationUtils {
   private static final int AREA_PER_1000 = 400;
   private static final float MAX_EXPOSURE_COMPENSATION = 1.5F;
   private static final int MAX_FPS = 20;
   private static final float MIN_EXPOSURE_COMPENSATION = 0.0F;
   private static final int MIN_FPS = 10;
   private static final Pattern SEMICOLON = Pattern.compile(";");
   private static final String TAG = "CameraConfiguration";

   private CameraConfigurationUtils() {
   }

   @TargetApi(15)
   private static List buildMiddleArea(int var0) {
      return Collections.singletonList(new Area(new Rect(-var0, -var0, var0, var0), 1));
   }

   public static String collectStats(Parameters var0) {
      return collectStats((CharSequence)var0.flatten());
   }

   public static String collectStats(CharSequence var0) {
      StringBuilder var1 = new StringBuilder(1000);
      var1.append("BOARD=").append(Build.BOARD).append('\n');
      var1.append("BRAND=").append(Build.BRAND).append('\n');
      var1.append("CPU_ABI=").append(Build.CPU_ABI).append('\n');
      var1.append("DEVICE=").append(Build.DEVICE).append('\n');
      var1.append("DISPLAY=").append(Build.DISPLAY).append('\n');
      var1.append("FINGERPRINT=").append(Build.FINGERPRINT).append('\n');
      var1.append("HOST=").append(Build.HOST).append('\n');
      var1.append("ID=").append(Build.ID).append('\n');
      var1.append("MANUFACTURER=").append(Build.MANUFACTURER).append('\n');
      var1.append("MODEL=").append(Build.MODEL).append('\n');
      var1.append("PRODUCT=").append(Build.PRODUCT).append('\n');
      var1.append("TAGS=").append(Build.TAGS).append('\n');
      var1.append("TIME=").append(Build.TIME).append('\n');
      var1.append("TYPE=").append(Build.TYPE).append('\n');
      var1.append("USER=").append(Build.USER).append('\n');
      var1.append("VERSION.CODENAME=").append(VERSION.CODENAME).append('\n');
      var1.append("VERSION.INCREMENTAL=").append(VERSION.INCREMENTAL).append('\n');
      var1.append("VERSION.RELEASE=").append(VERSION.RELEASE).append('\n');
      var1.append("VERSION.SDK_INT=").append(VERSION.SDK_INT).append('\n');
      if (var0 != null) {
         String[] var4 = SEMICOLON.split(var0);
         Arrays.sort(var4);
         int var2 = var4.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.append(var4[var3]).append('\n');
         }
      }

      return var1.toString();
   }

   private static String findSettableValue(String var0, Collection var1, String... var2) {
      Log.i("CameraConfiguration", "Requesting " + var0 + " value from among: " + Arrays.toString(var2));
      Log.i("CameraConfiguration", "Supported " + var0 + " values: " + var1);
      if (var1 != null) {
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var1.contains(var5)) {
               Log.i("CameraConfiguration", "Can set " + var0 + " to: " + var5);
               var0 = var5;
               return var0;
            }
         }
      }

      Log.i("CameraConfiguration", "No supported values match");
      var0 = null;
      return var0;
   }

   private static Integer indexOfClosestZoom(Parameters var0, double var1) {
      List var3 = var0.getZoomRatios();
      Log.i("CameraConfiguration", "Zoom ratios: " + var3);
      int var4 = var0.getMaxZoom();
      Integer var12;
      if (var3 != null && !var3.isEmpty() && var3.size() == var4 + 1) {
         double var5 = Double.POSITIVE_INFINITY;
         int var7 = 0;

         double var10;
         for(var4 = 0; var4 < var3.size(); var5 = var10) {
            double var8 = Math.abs((double)(Integer)var3.get(var4) - 100.0D * var1);
            var10 = var5;
            if (var8 < var5) {
               var10 = var8;
               var7 = var4;
            }

            ++var4;
         }

         Log.i("CameraConfiguration", "Chose zoom ratio of " + (double)(Integer)var3.get(var7) / 100.0D);
         var12 = var7;
      } else {
         Log.w("CameraConfiguration", "Invalid zoom ratios!");
         var12 = null;
      }

      return var12;
   }

   public static void setBarcodeSceneMode(Parameters var0) {
      if ("barcode".equals(var0.getSceneMode())) {
         Log.i("CameraConfiguration", "Barcode scene mode already set");
      } else {
         String var1 = findSettableValue("scene mode", var0.getSupportedSceneModes(), "barcode");
         if (var1 != null) {
            var0.setSceneMode(var1);
         }
      }

   }

   public static void setBestExposure(Parameters var0, boolean var1) {
      float var2 = 0.0F;
      int var3 = var0.getMinExposureCompensation();
      int var4 = var0.getMaxExposureCompensation();
      float var5 = var0.getExposureCompensationStep();
      if ((var3 != 0 || var4 != 0) && var5 > 0.0F) {
         if (!var1) {
            var2 = 1.5F;
         }

         int var6 = Math.round(var2 / var5);
         var2 = var5 * (float)var6;
         var4 = Math.max(Math.min(var6, var4), var3);
         if (var0.getExposureCompensation() == var4) {
            Log.i("CameraConfiguration", "Exposure compensation already set to " + var4 + " / " + var2);
         } else {
            Log.i("CameraConfiguration", "Setting exposure compensation to " + var4 + " / " + var2);
            var0.setExposureCompensation(var4);
         }
      } else {
         Log.i("CameraConfiguration", "Camera does not support exposure compensation");
      }

   }

   public static void setBestPreviewFPS(Parameters var0) {
      setBestPreviewFPS(var0, 10, 20);
   }

   public static void setBestPreviewFPS(Parameters var0, int var1, int var2) {
      List var3 = var0.getSupportedPreviewFpsRange();
      Log.i("CameraConfiguration", "Supported FPS ranges: " + toString((Collection)var3));
      if (var3 != null && !var3.isEmpty()) {
         Object var4 = null;
         Iterator var5 = var3.iterator();

         int var6;
         int var7;
         int[] var8;
         do {
            var8 = (int[])var4;
            if (!var5.hasNext()) {
               break;
            }

            var8 = (int[])var5.next();
            var6 = var8[0];
            var7 = var8[1];
         } while(var6 < var1 * 1000 || var7 > var2 * 1000);

         if (var8 == null) {
            Log.i("CameraConfiguration", "No suitable FPS range?");
         } else {
            int[] var9 = new int[2];
            var0.getPreviewFpsRange(var9);
            if (Arrays.equals(var9, var8)) {
               Log.i("CameraConfiguration", "FPS range already set to " + Arrays.toString(var8));
            } else {
               Log.i("CameraConfiguration", "Setting FPS range to " + Arrays.toString(var8));
               var0.setPreviewFpsRange(var8[0], var8[1]);
            }
         }
      }

   }

   public static void setFocus(Parameters var0, CameraSettings.FocusMode var1, boolean var2) {
      List var3 = var0.getSupportedFocusModes();
      String var4 = null;
      if (!var2 && var1 != CameraSettings.FocusMode.AUTO) {
         if (var1 == CameraSettings.FocusMode.CONTINUOUS) {
            var4 = findSettableValue("focus mode", var3, "continuous-picture", "continuous-video", "auto");
         } else if (var1 == CameraSettings.FocusMode.INFINITY) {
            var4 = findSettableValue("focus mode", var3, "infinity");
         } else if (var1 == CameraSettings.FocusMode.MACRO) {
            var4 = findSettableValue("focus mode", var3, "macro");
         }
      } else {
         var4 = findSettableValue("focus mode", var3, "auto");
      }

      String var5 = var4;
      if (!var2) {
         var5 = var4;
         if (var4 == null) {
            var5 = findSettableValue("focus mode", var3, "macro", "edof");
         }
      }

      if (var5 != null) {
         if (var5.equals(var0.getFocusMode())) {
            Log.i("CameraConfiguration", "Focus mode already set to " + var5);
         } else {
            var0.setFocusMode(var5);
         }
      }

   }

   @TargetApi(15)
   public static void setFocusArea(Parameters var0) {
      if (var0.getMaxNumFocusAreas() > 0) {
         Log.i("CameraConfiguration", "Old focus areas: " + toString((Iterable)var0.getFocusAreas()));
         List var1 = buildMiddleArea(400);
         Log.i("CameraConfiguration", "Setting focus area to : " + toString((Iterable)var1));
         var0.setFocusAreas(var1);
      } else {
         Log.i("CameraConfiguration", "Device does not support focus areas");
      }

   }

   public static void setInvertColor(Parameters var0) {
      if ("negative".equals(var0.getColorEffect())) {
         Log.i("CameraConfiguration", "Negative effect already set");
      } else {
         String var1 = findSettableValue("color effect", var0.getSupportedColorEffects(), "negative");
         if (var1 != null) {
            var0.setColorEffect(var1);
         }
      }

   }

   @TargetApi(15)
   public static void setMetering(Parameters var0) {
      if (var0.getMaxNumMeteringAreas() > 0) {
         Log.i("CameraConfiguration", "Old metering areas: " + var0.getMeteringAreas());
         List var1 = buildMiddleArea(400);
         Log.i("CameraConfiguration", "Setting metering area to : " + toString((Iterable)var1));
         var0.setMeteringAreas(var1);
      } else {
         Log.i("CameraConfiguration", "Device does not support metering areas");
      }

   }

   public static void setTorch(Parameters var0, boolean var1) {
      List var2 = var0.getSupportedFlashModes();
      String var3;
      if (var1) {
         var3 = findSettableValue("flash mode", var2, "torch", "on");
      } else {
         var3 = findSettableValue("flash mode", var2, "off");
      }

      if (var3 != null) {
         if (var3.equals(var0.getFlashMode())) {
            Log.i("CameraConfiguration", "Flash mode already set to " + var3);
         } else {
            Log.i("CameraConfiguration", "Setting flash mode to " + var3);
            var0.setFlashMode(var3);
         }
      }

   }

   @TargetApi(15)
   public static void setVideoStabilization(Parameters var0) {
      if (var0.isVideoStabilizationSupported()) {
         if (var0.getVideoStabilization()) {
            Log.i("CameraConfiguration", "Video stabilization already enabled");
         } else {
            Log.i("CameraConfiguration", "Enabling video stabilization...");
            var0.setVideoStabilization(true);
         }
      } else {
         Log.i("CameraConfiguration", "This device does not support video stabilization");
      }

   }

   public static void setZoom(Parameters var0, double var1) {
      if (var0.isZoomSupported()) {
         Integer var3 = indexOfClosestZoom(var0, var1);
         if (var3 != null) {
            if (var0.getZoom() == var3) {
               Log.i("CameraConfiguration", "Zoom is already set to " + var3);
            } else {
               Log.i("CameraConfiguration", "Setting zoom to " + var3);
               var0.setZoom(var3);
            }
         }
      } else {
         Log.i("CameraConfiguration", "Zoom is not supported");
      }

   }

   @TargetApi(15)
   private static String toString(Iterable var0) {
      String var3;
      if (var0 == null) {
         var3 = null;
      } else {
         StringBuilder var1 = new StringBuilder();
         Iterator var4 = var0.iterator();

         while(var4.hasNext()) {
            Area var2 = (Area)var4.next();
            var1.append(var2.rect).append(':').append(var2.weight).append(' ');
         }

         var3 = var1.toString();
      }

      return var3;
   }

   private static String toString(Collection var0) {
      String var2;
      if (var0 != null && !var0.isEmpty()) {
         StringBuilder var1 = new StringBuilder();
         var1.append('[');
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            var1.append(Arrays.toString((int[])var3.next()));
            if (var3.hasNext()) {
               var1.append(", ");
            }
         }

         var1.append(']');
         var2 = var1.toString();
      } else {
         var2 = "[]";
      }

      return var2;
   }
}
