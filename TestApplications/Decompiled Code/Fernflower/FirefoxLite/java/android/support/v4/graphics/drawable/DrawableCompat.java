package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableCompat {
   private static Method sGetLayoutDirectionMethod;
   private static boolean sGetLayoutDirectionMethodFetched;
   private static Method sSetLayoutDirectionMethod;
   private static boolean sSetLayoutDirectionMethodFetched;

   public static void applyTheme(Drawable var0, Theme var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.applyTheme(var1);
      }

   }

   public static boolean canApplyTheme(Drawable var0) {
      return VERSION.SDK_INT >= 21 ? var0.canApplyTheme() : false;
   }

   public static void clearColorFilter(Drawable var0) {
      if (VERSION.SDK_INT >= 23) {
         var0.clearColorFilter();
      } else if (VERSION.SDK_INT >= 21) {
         var0.clearColorFilter();
         if (var0 instanceof InsetDrawable) {
            clearColorFilter(((InsetDrawable)var0).getDrawable());
         } else if (var0 instanceof WrappedDrawable) {
            clearColorFilter(((WrappedDrawable)var0).getWrappedDrawable());
         } else if (var0 instanceof DrawableContainer) {
            DrawableContainerState var1 = (DrawableContainerState)((DrawableContainer)var0).getConstantState();
            if (var1 != null) {
               int var2 = 0;

               for(int var3 = var1.getChildCount(); var2 < var3; ++var2) {
                  var0 = var1.getChild(var2);
                  if (var0 != null) {
                     clearColorFilter(var0);
                  }
               }
            }
         }
      } else {
         var0.clearColorFilter();
      }

   }

   public static int getAlpha(Drawable var0) {
      return VERSION.SDK_INT >= 19 ? var0.getAlpha() : 0;
   }

   public static ColorFilter getColorFilter(Drawable var0) {
      return VERSION.SDK_INT >= 21 ? var0.getColorFilter() : null;
   }

   public static int getLayoutDirection(Drawable var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getLayoutDirection();
      } else if (VERSION.SDK_INT >= 17) {
         if (!sGetLayoutDirectionMethodFetched) {
            try {
               sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection");
               sGetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var3) {
               Log.i("DrawableCompat", "Failed to retrieve getLayoutDirection() method", var3);
            }

            sGetLayoutDirectionMethodFetched = true;
         }

         if (sGetLayoutDirectionMethod != null) {
            try {
               int var2 = (Integer)sGetLayoutDirectionMethod.invoke(var0);
               return var2;
            } catch (Exception var4) {
               Log.i("DrawableCompat", "Failed to invoke getLayoutDirection() via reflection", var4);
               sGetLayoutDirectionMethod = null;
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void inflate(Drawable var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      if (VERSION.SDK_INT >= 21) {
         var0.inflate(var1, var2, var3, var4);
      } else {
         var0.inflate(var1, var2, var3);
      }

   }

   public static boolean isAutoMirrored(Drawable var0) {
      return VERSION.SDK_INT >= 19 ? var0.isAutoMirrored() : false;
   }

   @Deprecated
   public static void jumpToCurrentState(Drawable var0) {
      var0.jumpToCurrentState();
   }

   public static void setAutoMirrored(Drawable var0, boolean var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setAutoMirrored(var1);
      }

   }

   public static void setHotspot(Drawable var0, float var1, float var2) {
      if (VERSION.SDK_INT >= 21) {
         var0.setHotspot(var1, var2);
      }

   }

   public static void setHotspotBounds(Drawable var0, int var1, int var2, int var3, int var4) {
      if (VERSION.SDK_INT >= 21) {
         var0.setHotspotBounds(var1, var2, var3, var4);
      }

   }

   public static boolean setLayoutDirection(Drawable var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         return var0.setLayoutDirection(var1);
      } else if (VERSION.SDK_INT >= 17) {
         if (!sSetLayoutDirectionMethodFetched) {
            try {
               sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
               sSetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var3) {
               Log.i("DrawableCompat", "Failed to retrieve setLayoutDirection(int) method", var3);
            }

            sSetLayoutDirectionMethodFetched = true;
         }

         if (sSetLayoutDirectionMethod != null) {
            try {
               sSetLayoutDirectionMethod.invoke(var0, var1);
               return true;
            } catch (Exception var4) {
               Log.i("DrawableCompat", "Failed to invoke setLayoutDirection(int) via reflection", var4);
               sSetLayoutDirectionMethod = null;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void setTint(Drawable var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setTint(var1);
      } else if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTint(var1);
      }

   }

   public static void setTintList(Drawable var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setTintList(var1);
      } else if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTintList(var1);
      }

   }

   public static void setTintMode(Drawable var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setTintMode(var1);
      } else if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTintMode(var1);
      }

   }

   public static Drawable wrap(Drawable var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0;
      } else if (VERSION.SDK_INT >= 21) {
         return (Drawable)(!(var0 instanceof TintAwareDrawable) ? new WrappedDrawableApi21(var0) : var0);
      } else {
         return (Drawable)(!(var0 instanceof TintAwareDrawable) ? new WrappedDrawableApi14(var0) : var0);
      }
   }
}
