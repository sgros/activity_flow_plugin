package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public final class TextViewCompat {
   public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
   public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
   static final TextViewCompat.TextViewCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 26) {
         IMPL = new TextViewCompat.TextViewCompatApi26Impl();
      } else if (VERSION.SDK_INT >= 23) {
         IMPL = new TextViewCompat.TextViewCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new TextViewCompat.TextViewCompatApi18Impl();
      } else if (VERSION.SDK_INT >= 17) {
         IMPL = new TextViewCompat.TextViewCompatApi17Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new TextViewCompat.TextViewCompatApi16Impl();
      } else {
         IMPL = new TextViewCompat.TextViewCompatBaseImpl();
      }

   }

   private TextViewCompat() {
   }

   public static int getAutoSizeMaxTextSize(TextView var0) {
      return IMPL.getAutoSizeMaxTextSize(var0);
   }

   public static int getAutoSizeMinTextSize(TextView var0) {
      return IMPL.getAutoSizeMinTextSize(var0);
   }

   public static int getAutoSizeStepGranularity(TextView var0) {
      return IMPL.getAutoSizeStepGranularity(var0);
   }

   public static int[] getAutoSizeTextAvailableSizes(TextView var0) {
      return IMPL.getAutoSizeTextAvailableSizes(var0);
   }

   public static int getAutoSizeTextType(TextView var0) {
      return IMPL.getAutoSizeTextType(var0);
   }

   public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView var0) {
      return IMPL.getCompoundDrawablesRelative(var0);
   }

   public static int getMaxLines(@NonNull TextView var0) {
      return IMPL.getMaxLines(var0);
   }

   public static int getMinLines(@NonNull TextView var0) {
      return IMPL.getMinLines(var0);
   }

   public static void setAutoSizeTextTypeUniformWithConfiguration(TextView var0, int var1, int var2, int var3, int var4) throws IllegalArgumentException {
      IMPL.setAutoSizeTextTypeUniformWithConfiguration(var0, var1, var2, var3, var4);
   }

   public static void setAutoSizeTextTypeUniformWithPresetSizes(TextView var0, @NonNull int[] var1, int var2) throws IllegalArgumentException {
      IMPL.setAutoSizeTextTypeUniformWithPresetSizes(var0, var1, var2);
   }

   public static void setAutoSizeTextTypeWithDefaults(TextView var0, int var1) {
      IMPL.setAutoSizeTextTypeWithDefaults(var0, var1);
   }

   public static void setCompoundDrawablesRelative(@NonNull TextView var0, @Nullable Drawable var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4) {
      IMPL.setCompoundDrawablesRelative(var0, var1, var2, var3, var4);
   }

   public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var0, @DrawableRes int var1, @DrawableRes int var2, @DrawableRes int var3, @DrawableRes int var4) {
      IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(var0, var1, var2, var3, var4);
   }

   public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var0, @Nullable Drawable var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4) {
      IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(var0, var1, var2, var3, var4);
   }

   public static void setTextAppearance(@NonNull TextView var0, @StyleRes int var1) {
      IMPL.setTextAppearance(var0, var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface AutoSizeTextType {
   }

   @RequiresApi(16)
   static class TextViewCompatApi16Impl extends TextViewCompat.TextViewCompatBaseImpl {
      public int getMaxLines(TextView var1) {
         return var1.getMaxLines();
      }

      public int getMinLines(TextView var1) {
         return var1.getMinLines();
      }
   }

   @RequiresApi(17)
   static class TextViewCompatApi17Impl extends TextViewCompat.TextViewCompatApi16Impl {
      public Drawable[] getCompoundDrawablesRelative(@NonNull TextView var1) {
         int var2 = var1.getLayoutDirection();
         boolean var3 = true;
         if (var2 != 1) {
            var3 = false;
         }

         Drawable[] var4 = var1.getCompoundDrawables();
         if (var3) {
            Drawable var6 = var4[2];
            Drawable var5 = var4[0];
            var4[0] = var6;
            var4[2] = var5;
         }

         return var4;
      }

      public void setCompoundDrawablesRelative(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         int var6 = var1.getLayoutDirection();
         boolean var7 = true;
         if (var6 != 1) {
            var7 = false;
         }

         Drawable var8;
         if (var7) {
            var8 = var4;
         } else {
            var8 = var2;
         }

         if (!var7) {
            var2 = var4;
         }

         var1.setCompoundDrawables(var8, var3, var2, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @DrawableRes int var2, @DrawableRes int var3, @DrawableRes int var4, @DrawableRes int var5) {
         int var6 = var1.getLayoutDirection();
         boolean var7 = true;
         if (var6 != 1) {
            var7 = false;
         }

         if (var7) {
            var6 = var4;
         } else {
            var6 = var2;
         }

         if (!var7) {
            var2 = var4;
         }

         var1.setCompoundDrawablesWithIntrinsicBounds(var6, var3, var2, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         int var6 = var1.getLayoutDirection();
         boolean var7 = true;
         if (var6 != 1) {
            var7 = false;
         }

         Drawable var8;
         if (var7) {
            var8 = var4;
         } else {
            var8 = var2;
         }

         if (!var7) {
            var2 = var4;
         }

         var1.setCompoundDrawablesWithIntrinsicBounds(var8, var3, var2, var5);
      }
   }

   @RequiresApi(18)
   static class TextViewCompatApi18Impl extends TextViewCompat.TextViewCompatApi17Impl {
      public Drawable[] getCompoundDrawablesRelative(@NonNull TextView var1) {
         return var1.getCompoundDrawablesRelative();
      }

      public void setCompoundDrawablesRelative(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         var1.setCompoundDrawablesRelative(var2, var3, var4, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @DrawableRes int var2, @DrawableRes int var3, @DrawableRes int var4, @DrawableRes int var5) {
         var1.setCompoundDrawablesRelativeWithIntrinsicBounds(var2, var3, var4, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         var1.setCompoundDrawablesRelativeWithIntrinsicBounds(var2, var3, var4, var5);
      }
   }

   @RequiresApi(23)
   static class TextViewCompatApi23Impl extends TextViewCompat.TextViewCompatApi18Impl {
      public void setTextAppearance(@NonNull TextView var1, @StyleRes int var2) {
         var1.setTextAppearance(var2);
      }
   }

   @RequiresApi(26)
   static class TextViewCompatApi26Impl extends TextViewCompat.TextViewCompatApi23Impl {
      public int getAutoSizeMaxTextSize(TextView var1) {
         return var1.getAutoSizeMaxTextSize();
      }

      public int getAutoSizeMinTextSize(TextView var1) {
         return var1.getAutoSizeMinTextSize();
      }

      public int getAutoSizeStepGranularity(TextView var1) {
         return var1.getAutoSizeStepGranularity();
      }

      public int[] getAutoSizeTextAvailableSizes(TextView var1) {
         return var1.getAutoSizeTextAvailableSizes();
      }

      public int getAutoSizeTextType(TextView var1) {
         return var1.getAutoSizeTextType();
      }

      public void setAutoSizeTextTypeUniformWithConfiguration(TextView var1, int var2, int var3, int var4, int var5) throws IllegalArgumentException {
         var1.setAutoSizeTextTypeUniformWithConfiguration(var2, var3, var4, var5);
      }

      public void setAutoSizeTextTypeUniformWithPresetSizes(TextView var1, @NonNull int[] var2, int var3) throws IllegalArgumentException {
         var1.setAutoSizeTextTypeUniformWithPresetSizes(var2, var3);
      }

      public void setAutoSizeTextTypeWithDefaults(TextView var1, int var2) {
         var1.setAutoSizeTextTypeWithDefaults(var2);
      }
   }

   static class TextViewCompatBaseImpl {
      private static final int LINES = 1;
      private static final String LOG_TAG = "TextViewCompatBase";
      private static Field sMaxModeField;
      private static boolean sMaxModeFieldFetched;
      private static Field sMaximumField;
      private static boolean sMaximumFieldFetched;
      private static Field sMinModeField;
      private static boolean sMinModeFieldFetched;
      private static Field sMinimumField;
      private static boolean sMinimumFieldFetched;

      private static Field retrieveField(String var0) {
         Field var1;
         label19: {
            try {
               var1 = TextView.class.getDeclaredField(var0);
            } catch (NoSuchFieldException var4) {
               var1 = null;
               break label19;
            }

            try {
               var1.setAccessible(true);
               return var1;
            } catch (NoSuchFieldException var3) {
            }
         }

         StringBuilder var2 = new StringBuilder();
         var2.append("Could not retrieve ");
         var2.append(var0);
         var2.append(" field.");
         Log.e("TextViewCompatBase", var2.toString());
         return var1;
      }

      private static int retrieveIntFromField(Field var0, TextView var1) {
         try {
            int var2 = var0.getInt(var1);
            return var2;
         } catch (IllegalAccessException var3) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Could not retrieve value of ");
            var4.append(var0.getName());
            var4.append(" field.");
            Log.d("TextViewCompatBase", var4.toString());
            return -1;
         }
      }

      public int getAutoSizeMaxTextSize(TextView var1) {
         return var1 instanceof AutoSizeableTextView ? ((AutoSizeableTextView)var1).getAutoSizeMaxTextSize() : -1;
      }

      public int getAutoSizeMinTextSize(TextView var1) {
         return var1 instanceof AutoSizeableTextView ? ((AutoSizeableTextView)var1).getAutoSizeMinTextSize() : -1;
      }

      public int getAutoSizeStepGranularity(TextView var1) {
         return var1 instanceof AutoSizeableTextView ? ((AutoSizeableTextView)var1).getAutoSizeStepGranularity() : -1;
      }

      public int[] getAutoSizeTextAvailableSizes(TextView var1) {
         return var1 instanceof AutoSizeableTextView ? ((AutoSizeableTextView)var1).getAutoSizeTextAvailableSizes() : new int[0];
      }

      public int getAutoSizeTextType(TextView var1) {
         return var1 instanceof AutoSizeableTextView ? ((AutoSizeableTextView)var1).getAutoSizeTextType() : 0;
      }

      public Drawable[] getCompoundDrawablesRelative(@NonNull TextView var1) {
         return var1.getCompoundDrawables();
      }

      public int getMaxLines(TextView var1) {
         if (!sMaxModeFieldFetched) {
            sMaxModeField = retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
         }

         if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, var1) == 1) {
            if (!sMaximumFieldFetched) {
               sMaximumField = retrieveField("mMaximum");
               sMaximumFieldFetched = true;
            }

            if (sMaximumField != null) {
               return retrieveIntFromField(sMaximumField, var1);
            }
         }

         return -1;
      }

      public int getMinLines(TextView var1) {
         if (!sMinModeFieldFetched) {
            sMinModeField = retrieveField("mMinMode");
            sMinModeFieldFetched = true;
         }

         if (sMinModeField != null && retrieveIntFromField(sMinModeField, var1) == 1) {
            if (!sMinimumFieldFetched) {
               sMinimumField = retrieveField("mMinimum");
               sMinimumFieldFetched = true;
            }

            if (sMinimumField != null) {
               return retrieveIntFromField(sMinimumField, var1);
            }
         }

         return -1;
      }

      public void setAutoSizeTextTypeUniformWithConfiguration(TextView var1, int var2, int var3, int var4, int var5) throws IllegalArgumentException {
         if (var1 instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)var1).setAutoSizeTextTypeUniformWithConfiguration(var2, var3, var4, var5);
         }

      }

      public void setAutoSizeTextTypeUniformWithPresetSizes(TextView var1, @NonNull int[] var2, int var3) throws IllegalArgumentException {
         if (var1 instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)var1).setAutoSizeTextTypeUniformWithPresetSizes(var2, var3);
         }

      }

      public void setAutoSizeTextTypeWithDefaults(TextView var1, int var2) {
         if (var1 instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)var1).setAutoSizeTextTypeWithDefaults(var2);
         }

      }

      public void setCompoundDrawablesRelative(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         var1.setCompoundDrawables(var2, var3, var4, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @DrawableRes int var2, @DrawableRes int var3, @DrawableRes int var4, @DrawableRes int var5) {
         var1.setCompoundDrawablesWithIntrinsicBounds(var2, var3, var4, var5);
      }

      public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5) {
         var1.setCompoundDrawablesWithIntrinsicBounds(var2, var3, var4, var5);
      }

      public void setTextAppearance(TextView var1, @StyleRes int var2) {
         var1.setTextAppearance(var1.getContext(), var2);
      }
   }
}
