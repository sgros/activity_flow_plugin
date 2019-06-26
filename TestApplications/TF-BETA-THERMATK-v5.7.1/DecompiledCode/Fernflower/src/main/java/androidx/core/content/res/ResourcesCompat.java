package androidx.core.content.res;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.graphics.TypefaceCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
   public static Drawable getDrawable(Resources var0, int var1, Theme var2) throws NotFoundException {
      return VERSION.SDK_INT >= 21 ? var0.getDrawable(var1, var2) : var0.getDrawable(var1);
   }

   public static Typeface getFont(Context var0, int var1, TypedValue var2, int var3, ResourcesCompat.FontCallback var4) throws NotFoundException {
      return var0.isRestricted() ? null : loadFont(var0, var1, var2, var3, var4, (Handler)null, true);
   }

   private static Typeface loadFont(Context var0, int var1, TypedValue var2, int var3, ResourcesCompat.FontCallback var4, Handler var5, boolean var6) {
      Resources var7 = var0.getResources();
      var7.getValue(var1, var2, true);
      Typeface var8 = loadFont(var0, var7, var2, var1, var3, var4, var5, var6);
      if (var8 == null && var4 == null) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Font resource ID #0x");
         var9.append(Integer.toHexString(var1));
         var9.append(" could not be retrieved.");
         throw new NotFoundException(var9.toString());
      } else {
         return var8;
      }
   }

   private static Typeface loadFont(Context var0, Resources var1, TypedValue var2, int var3, int var4, ResourcesCompat.FontCallback var5, Handler var6, boolean var7) {
      CharSequence var8 = var2.string;
      StringBuilder var24;
      if (var8 == null) {
         var24 = new StringBuilder();
         var24.append("Resource \"");
         var24.append(var1.getResourceName(var3));
         var24.append("\" (");
         var24.append(Integer.toHexString(var3));
         var24.append(") is not a Font: ");
         var24.append(var2);
         throw new NotFoundException(var24.toString());
      } else {
         String var28 = var8.toString();
         if (!var28.startsWith("res/")) {
            if (var5 != null) {
               var5.callbackFailAsync(-3, var6);
            }

            return null;
         } else {
            Typeface var29 = TypefaceCompat.findFromCache(var1, var3, var4);
            if (var29 != null) {
               if (var5 != null) {
                  var5.callbackSuccessAsync(var29, var6);
               }

               return var29;
            } else {
               label132: {
                  XmlPullParserException var31;
                  label104: {
                     IOException var10000;
                     label103: {
                        boolean var10001;
                        FontResourcesParserCompat.FamilyResourceEntry var30;
                        label114: {
                           try {
                              if (var28.toLowerCase().endsWith(".xml")) {
                                 var30 = FontResourcesParserCompat.parse(var1.getXml(var3), var1);
                                 break label114;
                              }
                           } catch (XmlPullParserException var21) {
                              var31 = var21;
                              var10001 = false;
                              break label104;
                           } catch (IOException var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label103;
                           }

                           Typeface var23;
                           try {
                              var23 = TypefaceCompat.createFromResourcesFontFile(var0, var1, var3, var28, var4);
                           } catch (XmlPullParserException var19) {
                              var31 = var19;
                              var10001 = false;
                              break label104;
                           } catch (IOException var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label103;
                           }

                           if (var5 == null) {
                              return var23;
                           }

                           if (var23 != null) {
                              try {
                                 var5.callbackSuccessAsync(var23, var6);
                                 return var23;
                              } catch (XmlPullParserException var15) {
                                 var31 = var15;
                                 var10001 = false;
                                 break label104;
                              } catch (IOException var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label103;
                              }
                           } else {
                              try {
                                 var5.callbackFailAsync(-3, var6);
                                 return var23;
                              } catch (XmlPullParserException var17) {
                                 var31 = var17;
                                 var10001 = false;
                                 break label104;
                              } catch (IOException var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label103;
                              }
                           }
                        }

                        if (var30 == null) {
                           label115: {
                              try {
                                 Log.e("ResourcesCompat", "Failed to find font-family tag");
                              } catch (XmlPullParserException var11) {
                                 var31 = var11;
                                 var10001 = false;
                                 break label104;
                              } catch (IOException var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break label115;
                              }

                              if (var5 == null) {
                                 return null;
                              }

                              try {
                                 var5.callbackFailAsync(-3, var6);
                                 return null;
                              } catch (XmlPullParserException var9) {
                                 var31 = var9;
                                 var10001 = false;
                                 break label104;
                              } catch (IOException var10) {
                                 var10000 = var10;
                                 var10001 = false;
                              }
                           }
                        } else {
                           try {
                              return TypefaceCompat.createFromResourcesFamilyXml(var0, var30, var1, var3, var4, var5, var6, var7);
                           } catch (XmlPullParserException var13) {
                              var31 = var13;
                              var10001 = false;
                              break label104;
                           } catch (IOException var14) {
                              var10000 = var14;
                              var10001 = false;
                           }
                        }
                     }

                     IOException var26 = var10000;
                     var24 = new StringBuilder();
                     var24.append("Failed to read xml resource ");
                     var24.append(var28);
                     Log.e("ResourcesCompat", var24.toString(), var26);
                     break label132;
                  }

                  XmlPullParserException var25 = var31;
                  StringBuilder var27 = new StringBuilder();
                  var27.append("Failed to parse xml resource ");
                  var27.append(var28);
                  Log.e("ResourcesCompat", var27.toString(), var25);
               }

               if (var5 != null) {
                  var5.callbackFailAsync(-3, var6);
               }

               return null;
            }
         }
      }
   }

   public abstract static class FontCallback {
      public final void callbackFailAsync(final int var1, Handler var2) {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler(Looper.getMainLooper());
         }

         var3.post(new Runnable() {
            public void run() {
               FontCallback.this.onFontRetrievalFailed(var1);
            }
         });
      }

      public final void callbackSuccessAsync(final Typeface var1, Handler var2) {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler(Looper.getMainLooper());
         }

         var3.post(new Runnable() {
            public void run() {
               FontCallback.this.onFontRetrieved(var1);
            }
         });
      }

      public abstract void onFontRetrievalFailed(int var1);

      public abstract void onFontRetrieved(Typeface var1);
   }
}
