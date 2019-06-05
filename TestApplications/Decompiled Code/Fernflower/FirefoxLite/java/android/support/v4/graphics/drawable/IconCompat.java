package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
   static final Mode DEFAULT_TINT_MODE;
   public byte[] mData;
   public int mInt1;
   public int mInt2;
   Object mObj1;
   public Parcelable mParcelable;
   public ColorStateList mTintList = null;
   Mode mTintMode;
   public String mTintModeStr;
   public int mType;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   public IconCompat() {
      this.mTintMode = DEFAULT_TINT_MODE;
   }

   private IconCompat(int var1) {
      this.mTintMode = DEFAULT_TINT_MODE;
      this.mType = var1;
   }

   static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap var0, boolean var1) {
      int var2 = (int)((float)Math.min(var0.getWidth(), var0.getHeight()) * 0.6666667F);
      Bitmap var3 = Bitmap.createBitmap(var2, var2, Config.ARGB_8888);
      Canvas var4 = new Canvas(var3);
      Paint var5 = new Paint(3);
      float var6 = (float)var2;
      float var7 = 0.5F * var6;
      float var8 = 0.9166667F * var7;
      if (var1) {
         float var9 = 0.010416667F * var6;
         var5.setColor(0);
         var5.setShadowLayer(var9, 0.0F, var6 * 0.020833334F, 1023410176);
         var4.drawCircle(var7, var7, var8, var5);
         var5.setShadowLayer(var9, 0.0F, 0.0F, 503316480);
         var4.drawCircle(var7, var7, var8, var5);
         var5.clearShadowLayer();
      }

      var5.setColor(-16777216);
      BitmapShader var10 = new BitmapShader(var0, TileMode.CLAMP, TileMode.CLAMP);
      Matrix var11 = new Matrix();
      var11.setTranslate((float)(-(var0.getWidth() - var2) / 2), (float)(-(var0.getHeight() - var2) / 2));
      var10.setLocalMatrix(var11);
      var5.setShader(var10);
      var4.drawCircle(var7, var7, var8, var5);
      var4.setBitmap((Bitmap)null);
      return var3;
   }

   public static IconCompat createWithBitmap(Bitmap var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(1);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Bitmap must not be null.");
      }
   }

   private static int getResId(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getResId();
      } else {
         try {
            int var1 = (Integer)var0.getClass().getMethod("getResId").invoke(var0);
            return var1;
         } catch (IllegalAccessException var2) {
            Log.e("IconCompat", "Unable to get icon resource", var2);
            return 0;
         } catch (InvocationTargetException var3) {
            Log.e("IconCompat", "Unable to get icon resource", var3);
            return 0;
         } catch (NoSuchMethodException var4) {
            Log.e("IconCompat", "Unable to get icon resource", var4);
            return 0;
         }
      }
   }

   private static String getResPackage(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getResPackage();
      } else {
         try {
            String var4 = (String)var0.getClass().getMethod("getResPackage").invoke(var0);
            return var4;
         } catch (IllegalAccessException var1) {
            Log.e("IconCompat", "Unable to get icon package", var1);
            return null;
         } catch (InvocationTargetException var2) {
            Log.e("IconCompat", "Unable to get icon package", var2);
            return null;
         } catch (NoSuchMethodException var3) {
            Log.e("IconCompat", "Unable to get icon package", var3);
            return null;
         }
      }
   }

   private static Resources getResources(Context var0, String var1) {
      if ("android".equals(var1)) {
         return Resources.getSystem();
      } else {
         PackageManager var2 = var0.getPackageManager();

         NameNotFoundException var10000;
         label31: {
            boolean var10001;
            ApplicationInfo var5;
            try {
               var5 = var2.getApplicationInfo(var1, 8192);
            } catch (NameNotFoundException var4) {
               var10000 = var4;
               var10001 = false;
               break label31;
            }

            if (var5 == null) {
               return null;
            }

            try {
               Resources var7 = var2.getResourcesForApplication(var5);
               return var7;
            } catch (NameNotFoundException var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         NameNotFoundException var6 = var10000;
         Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", var1), var6);
         return null;
      }
   }

   private static String typeToString(int var0) {
      switch(var0) {
      case 1:
         return "BITMAP";
      case 2:
         return "RESOURCE";
      case 3:
         return "DATA";
      case 4:
         return "URI";
      case 5:
         return "BITMAP_MASKABLE";
      default:
         return "UNKNOWN";
      }
   }

   public void addToShortcutIntent(Intent var1, Drawable var2, Context var3) {
      this.checkResource(var3);
      int var4 = this.mType;
      Bitmap var16;
      if (var4 == 5) {
         var16 = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
      } else {
         switch(var4) {
         case 1:
            Bitmap var17 = (Bitmap)this.mObj1;
            var16 = var17;
            if (var2 != null) {
               var16 = var17.copy(var17.getConfig(), true);
            }
            break;
         case 2:
            NameNotFoundException var10000;
            label68: {
               boolean var10001;
               try {
                  var3 = var3.createPackageContext(this.getResPackage(), 0);
               } catch (NameNotFoundException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label68;
               }

               if (var2 == null) {
                  try {
                     var1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(var3, this.mInt1));
                     return;
                  } catch (NameNotFoundException var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               } else {
                  label64: {
                     Drawable var5;
                     label63: {
                        label79: {
                           label60:
                           try {
                              var5 = ContextCompat.getDrawable(var3, this.mInt1);
                              if (var5.getIntrinsicWidth() > 0 && var5.getIntrinsicHeight() > 0) {
                                 break label60;
                              }
                              break label79;
                           } catch (NameNotFoundException var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label64;
                           }

                           try {
                              var16 = Bitmap.createBitmap(var5.getIntrinsicWidth(), var5.getIntrinsicHeight(), Config.ARGB_8888);
                              break label63;
                           } catch (NameNotFoundException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label64;
                           }
                        }

                        try {
                           var4 = ((ActivityManager)var3.getSystemService("activity")).getLauncherLargeIconSize();
                           var16 = Bitmap.createBitmap(var4, var4, Config.ARGB_8888);
                        } catch (NameNotFoundException var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label64;
                        }
                     }

                     try {
                        var5.setBounds(0, 0, var16.getWidth(), var16.getHeight());
                        Canvas var6 = new Canvas(var16);
                        var5.draw(var6);
                        break;
                     } catch (NameNotFoundException var9) {
                        var10000 = var9;
                        var10001 = false;
                     }
                  }
               }
            }

            NameNotFoundException var15 = var10000;
            StringBuilder var14 = new StringBuilder();
            var14.append("Can't find package ");
            var14.append(this.mObj1);
            throw new IllegalArgumentException(var14.toString(), var15);
         default:
            throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
         }
      }

      if (var2 != null) {
         int var7 = var16.getWidth();
         var4 = var16.getHeight();
         var2.setBounds(var7 / 2, var4 / 2, var7, var4);
         var2.draw(new Canvas(var16));
      }

      var1.putExtra("android.intent.extra.shortcut.ICON", var16);
   }

   public void checkResource(Context var1) {
      if (this.mType == 2) {
         String var2 = (String)this.mObj1;
         if (!var2.contains(":")) {
            return;
         }

         String var3 = var2.split(":", -1)[1];
         String var4 = var3.split("/", -1)[0];
         var3 = var3.split("/", -1)[1];
         var2 = var2.split(":", -1)[0];
         int var5 = getResources(var1, var2).getIdentifier(var3, var4, var2);
         if (this.mInt1 != var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Id has changed for ");
            var6.append(var2);
            var6.append("/");
            var6.append(var3);
            Log.i("IconCompat", var6.toString());
            this.mInt1 = var5;
         }
      }

   }

   public int getResId() {
      if (this.mType == -1 && VERSION.SDK_INT >= 23) {
         return getResId((Icon)this.mObj1);
      } else if (this.mType == 2) {
         return this.mInt1;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("called getResId() on ");
         var1.append(this);
         throw new IllegalStateException(var1.toString());
      }
   }

   public String getResPackage() {
      if (this.mType == -1 && VERSION.SDK_INT >= 23) {
         return getResPackage((Icon)this.mObj1);
      } else if (this.mType == 2) {
         return ((String)this.mObj1).split(":", -1)[0];
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("called getResPackage() on ");
         var1.append(this);
         throw new IllegalStateException(var1.toString());
      }
   }

   public void onPostParceling() {
      this.mTintMode = Mode.valueOf(this.mTintModeStr);
      int var1 = this.mType;
      if (var1 != -1) {
         switch(var1) {
         case 1:
         case 5:
            if (this.mParcelable != null) {
               this.mObj1 = this.mParcelable;
            } else {
               this.mObj1 = this.mData;
               this.mType = 3;
               this.mInt1 = 0;
               this.mInt2 = this.mData.length;
            }
            break;
         case 2:
         case 4:
            this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
            break;
         case 3:
            this.mObj1 = this.mData;
         }
      } else {
         if (this.mParcelable == null) {
            throw new IllegalArgumentException("Invalid icon");
         }

         this.mObj1 = this.mParcelable;
      }

   }

   public void onPreParceling(boolean var1) {
      this.mTintModeStr = this.mTintMode.name();
      int var2 = this.mType;
      if (var2 != -1) {
         switch(var2) {
         case 1:
         case 5:
            if (var1) {
               Bitmap var3 = (Bitmap)this.mObj1;
               ByteArrayOutputStream var4 = new ByteArrayOutputStream();
               var3.compress(CompressFormat.PNG, 90, var4);
               this.mData = var4.toByteArray();
            } else {
               this.mParcelable = (Parcelable)this.mObj1;
            }
            break;
         case 2:
            this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
            break;
         case 3:
            this.mData = (byte[])this.mObj1;
            break;
         case 4:
            this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
         }
      } else {
         if (var1) {
            throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
         }

         this.mParcelable = (Parcelable)this.mObj1;
      }

   }

   public Icon toIcon() {
      int var1 = this.mType;
      if (var1 != -1) {
         Icon var2;
         switch(var1) {
         case 1:
            var2 = Icon.createWithBitmap((Bitmap)this.mObj1);
            break;
         case 2:
            var2 = Icon.createWithResource(this.getResPackage(), this.mInt1);
            break;
         case 3:
            var2 = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
            break;
         case 4:
            var2 = Icon.createWithContentUri((String)this.mObj1);
            break;
         case 5:
            if (VERSION.SDK_INT >= 26) {
               var2 = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
            } else {
               var2 = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
            }
            break;
         default:
            throw new IllegalArgumentException("Unknown type");
         }

         if (this.mTintList != null) {
            var2.setTintList(this.mTintList);
         }

         if (this.mTintMode != DEFAULT_TINT_MODE) {
            var2.setTintMode(this.mTintMode);
         }

         return var2;
      } else {
         return (Icon)this.mObj1;
      }
   }

   public String toString() {
      if (this.mType == -1) {
         return String.valueOf(this.mObj1);
      } else {
         StringBuilder var1 = new StringBuilder("Icon(typ=");
         var1.append(typeToString(this.mType));
         switch(this.mType) {
         case 1:
         case 5:
            var1.append(" size=");
            var1.append(((Bitmap)this.mObj1).getWidth());
            var1.append("x");
            var1.append(((Bitmap)this.mObj1).getHeight());
            break;
         case 2:
            var1.append(" pkg=");
            var1.append(this.getResPackage());
            var1.append(" id=");
            var1.append(String.format("0x%08x", this.getResId()));
            break;
         case 3:
            var1.append(" len=");
            var1.append(this.mInt1);
            if (this.mInt2 != 0) {
               var1.append(" off=");
               var1.append(this.mInt2);
            }
            break;
         case 4:
            var1.append(" uri=");
            var1.append(this.mObj1);
         }

         if (this.mTintList != null) {
            var1.append(" tint=");
            var1.append(this.mTintList);
         }

         if (this.mTintMode != DEFAULT_TINT_MODE) {
            var1.append(" mode=");
            var1.append(this.mTintMode);
         }

         var1.append(")");
         return var1.toString();
      }
   }
}
