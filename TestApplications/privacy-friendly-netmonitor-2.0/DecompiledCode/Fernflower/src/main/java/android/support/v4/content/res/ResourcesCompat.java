package android.support.v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.TypefaceCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
   private static final String TAG = "ResourcesCompat";

   private ResourcesCompat() {
   }

   @ColorInt
   public static int getColor(@NonNull Resources var0, @ColorRes int var1, @Nullable Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 23 ? var0.getColor(var1, var2) : var0.getColor(var1);
   }

   @Nullable
   public static ColorStateList getColorStateList(@NonNull Resources var0, @ColorRes int var1, @Nullable Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 23 ? var0.getColorStateList(var1, var2) : var0.getColorStateList(var1);
   }

   @Nullable
   public static Drawable getDrawable(@NonNull Resources var0, @DrawableRes int var1, @Nullable Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 21 ? var0.getDrawable(var1, var2) : var0.getDrawable(var1);
   }

   @Nullable
   public static Drawable getDrawableForDensity(@NonNull Resources var0, @DrawableRes int var1, int var2, @Nullable Theme var3) throws NotFoundException {
      if (VERSION.SDK_INT >= 21) {
         return var0.getDrawableForDensity(var1, var2, var3);
      } else {
         return VERSION.SDK_INT >= 15 ? var0.getDrawableForDensity(var1, var2) : var0.getDrawable(var1);
      }
   }

   @Nullable
   public static Typeface getFont(@NonNull Context var0, @FontRes int var1) throws NotFoundException {
      return var0.isRestricted() ? null : loadFont(var0, var1, new TypedValue(), 0, (TextView)null);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static Typeface getFont(@NonNull Context var0, @FontRes int var1, TypedValue var2, int var3, @Nullable TextView var4) throws NotFoundException {
      return var0.isRestricted() ? null : loadFont(var0, var1, var2, var3, var4);
   }

   private static Typeface loadFont(@NonNull Context var0, int var1, TypedValue var2, int var3, @Nullable TextView var4) {
      Resources var5 = var0.getResources();
      var5.getValue(var1, var2, true);
      Typeface var6 = loadFont(var0, var5, var2, var1, var3, var4);
      if (var6 != null) {
         return var6;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Font resource ID #0x");
         var7.append(Integer.toHexString(var1));
         throw new NotFoundException(var7.toString());
      }
   }

   private static Typeface loadFont(@NonNull Context var0, Resources var1, TypedValue var2, int var3, int var4, @Nullable TextView var5) {
      StringBuilder var15;
      if (var2.string == null) {
         var15 = new StringBuilder();
         var15.append("Resource \"");
         var15.append(var1.getResourceName(var3));
         var15.append("\" (");
         var15.append(Integer.toHexString(var3));
         var15.append(") is not a Font: ");
         var15.append(var2);
         throw new NotFoundException(var15.toString());
      } else {
         String var20 = var2.string.toString();
         if (!var20.startsWith("res/")) {
            return null;
         } else {
            Typeface var6 = TypefaceCompat.findFromCache(var1, var3, var4);
            if (var6 != null) {
               return var6;
            } else {
               XmlPullParserException var22;
               label72: {
                  IOException var10000;
                  label56: {
                     boolean var10001;
                     label55: {
                        FontResourcesParserCompat.FamilyResourceEntry var21;
                        try {
                           if (!var20.toLowerCase().endsWith(".xml")) {
                              break label55;
                           }

                           var21 = FontResourcesParserCompat.parse(var1.getXml(var3), var1);
                        } catch (XmlPullParserException var13) {
                           var22 = var13;
                           var10001 = false;
                           break label72;
                        } catch (IOException var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label56;
                        }

                        if (var21 == null) {
                           try {
                              Log.e("ResourcesCompat", "Failed to find font-family tag");
                              return null;
                           } catch (XmlPullParserException var7) {
                              var22 = var7;
                              var10001 = false;
                              break label72;
                           } catch (IOException var8) {
                              var10000 = var8;
                              var10001 = false;
                              break label56;
                           }
                        } else {
                           try {
                              return TypefaceCompat.createFromResourcesFamilyXml(var0, var21, var1, var3, var4, var5);
                           } catch (XmlPullParserException var9) {
                              var22 = var9;
                              var10001 = false;
                              break label72;
                           } catch (IOException var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label56;
                           }
                        }
                     }

                     try {
                        Typeface var17 = TypefaceCompat.createFromResourcesFontFile(var0, var1, var3, var20, var4);
                        return var17;
                     } catch (XmlPullParserException var11) {
                        var22 = var11;
                        var10001 = false;
                        break label72;
                     } catch (IOException var12) {
                        var10000 = var12;
                        var10001 = false;
                     }
                  }

                  IOException var18 = var10000;
                  var15 = new StringBuilder();
                  var15.append("Failed to read xml resource ");
                  var15.append(var20);
                  Log.e("ResourcesCompat", var15.toString(), var18);
                  return null;
               }

               XmlPullParserException var16 = var22;
               StringBuilder var19 = new StringBuilder();
               var19.append("Failed to parse xml resource ");
               var19.append(var20);
               Log.e("ResourcesCompat", var19.toString(), var16);
               return null;
            }
         }
      }
   }
}
