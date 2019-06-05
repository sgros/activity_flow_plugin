package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class ConfigurationHelper {
   private static final ConfigurationHelper.ConfigurationHelperImpl IMPL;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 17) {
         IMPL = new ConfigurationHelper.JellybeanMr1Impl();
      } else if (var0 >= 13) {
         IMPL = new ConfigurationHelper.HoneycombMr2Impl();
      } else {
         IMPL = new ConfigurationHelper.GingerbreadImpl();
      }

   }

   private ConfigurationHelper() {
   }

   public static int getDensityDpi(@NonNull Resources var0) {
      return IMPL.getDensityDpi(var0);
   }

   public static int getScreenHeightDp(@NonNull Resources var0) {
      return IMPL.getScreenHeightDp(var0);
   }

   public static int getScreenWidthDp(@NonNull Resources var0) {
      return IMPL.getScreenWidthDp(var0);
   }

   public static int getSmallestScreenWidthDp(@NonNull Resources var0) {
      return IMPL.getSmallestScreenWidthDp(var0);
   }

   private interface ConfigurationHelperImpl {
      int getDensityDpi(@NonNull Resources var1);

      int getScreenHeightDp(@NonNull Resources var1);

      int getScreenWidthDp(@NonNull Resources var1);

      int getSmallestScreenWidthDp(@NonNull Resources var1);
   }

   private static class GingerbreadImpl implements ConfigurationHelper.ConfigurationHelperImpl {
      GingerbreadImpl() {
      }

      public int getDensityDpi(@NonNull Resources var1) {
         return ConfigurationHelperGingerbread.getDensityDpi(var1);
      }

      public int getScreenHeightDp(@NonNull Resources var1) {
         return ConfigurationHelperGingerbread.getScreenHeightDp(var1);
      }

      public int getScreenWidthDp(@NonNull Resources var1) {
         return ConfigurationHelperGingerbread.getScreenWidthDp(var1);
      }

      public int getSmallestScreenWidthDp(@NonNull Resources var1) {
         return ConfigurationHelperGingerbread.getSmallestScreenWidthDp(var1);
      }
   }

   private static class HoneycombMr2Impl extends ConfigurationHelper.GingerbreadImpl {
      HoneycombMr2Impl() {
      }

      public int getScreenHeightDp(@NonNull Resources var1) {
         return ConfigurationHelperHoneycombMr2.getScreenHeightDp(var1);
      }

      public int getScreenWidthDp(@NonNull Resources var1) {
         return ConfigurationHelperHoneycombMr2.getScreenWidthDp(var1);
      }

      public int getSmallestScreenWidthDp(@NonNull Resources var1) {
         return ConfigurationHelperHoneycombMr2.getSmallestScreenWidthDp(var1);
      }
   }

   private static class JellybeanMr1Impl extends ConfigurationHelper.HoneycombMr2Impl {
      JellybeanMr1Impl() {
      }

      public int getDensityDpi(@NonNull Resources var1) {
         return ConfigurationHelperJellybeanMr1.getDensityDpi(var1);
      }
   }
}
