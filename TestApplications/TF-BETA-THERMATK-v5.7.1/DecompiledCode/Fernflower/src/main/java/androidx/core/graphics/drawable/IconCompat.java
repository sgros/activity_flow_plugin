package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
   static final Mode DEFAULT_TINT_MODE;
   public byte[] mData = null;
   public int mInt1 = 0;
   public int mInt2 = 0;
   Object mObj1;
   public Parcelable mParcelable = null;
   public ColorStateList mTintList = null;
   Mode mTintMode;
   public String mTintModeStr;
   public int mType = -1;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   public IconCompat() {
      this.mTintMode = DEFAULT_TINT_MODE;
      this.mTintModeStr = null;
   }

   private IconCompat(int var1) {
      this.mTintMode = DEFAULT_TINT_MODE;
      this.mTintModeStr = null;
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
      TileMode var10 = TileMode.CLAMP;
      BitmapShader var11 = new BitmapShader(var0, var10, var10);
      Matrix var12 = new Matrix();
      var12.setTranslate((float)(-(var0.getWidth() - var2) / 2), (float)(-(var0.getHeight() - var2) / 2));
      var11.setLocalMatrix(var12);
      var5.setShader(var11);
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

   private static String typeToString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 3) {
               if (var0 != 4) {
                  return var0 != 5 ? "UNKNOWN" : "BITMAP_MASKABLE";
               } else {
                  return "URI";
               }
            } else {
               return "DATA";
            }
         } else {
            return "RESOURCE";
         }
      } else {
         return "BITMAP";
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
      Parcelable var2;
      if (var1 != -1) {
         if (var1 != 1) {
            label47: {
               if (var1 != 2) {
                  if (var1 == 3) {
                     this.mObj1 = this.mData;
                     return;
                  }

                  if (var1 != 4) {
                     if (var1 != 5) {
                        return;
                     }
                     break label47;
                  }
               }

               this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
               return;
            }
         }

         var2 = this.mParcelable;
         if (var2 != null) {
            this.mObj1 = var2;
         } else {
            byte[] var3 = this.mData;
            this.mObj1 = var3;
            this.mType = 3;
            this.mInt1 = 0;
            this.mInt2 = var3.length;
         }
      } else {
         var2 = this.mParcelable;
         if (var2 == null) {
            throw new IllegalArgumentException("Invalid icon");
         }

         this.mObj1 = var2;
      }

   }

   public void onPreParceling(boolean var1) {
      this.mTintModeStr = this.mTintMode.name();
      int var2 = this.mType;
      if (var2 != -1) {
         if (var2 != 1) {
            if (var2 == 2) {
               this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
               return;
            }

            if (var2 == 3) {
               this.mData = (byte[])this.mObj1;
               return;
            }

            if (var2 == 4) {
               this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
               return;
            }

            if (var2 != 5) {
               return;
            }
         }

         if (var1) {
            Bitmap var3 = (Bitmap)this.mObj1;
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            var3.compress(CompressFormat.PNG, 90, var4);
            this.mData = var4.toByteArray();
         } else {
            this.mParcelable = (Parcelable)this.mObj1;
         }
      } else {
         if (var1) {
            throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
         }

         this.mParcelable = (Parcelable)this.mObj1;
      }

   }

   public Bundle toBundle() {
      Bundle var1 = new Bundle();
      int var2 = this.mType;
      if (var2 != -1) {
         label36: {
            label35: {
               if (var2 != 1) {
                  if (var2 == 2) {
                     break label35;
                  }

                  if (var2 == 3) {
                     var1.putByteArray("obj", (byte[])this.mObj1);
                     break label36;
                  }

                  if (var2 == 4) {
                     break label35;
                  }

                  if (var2 != 5) {
                     throw new IllegalArgumentException("Invalid icon");
                  }
               }

               var1.putParcelable("obj", (Bitmap)this.mObj1);
               break label36;
            }

            var1.putString("obj", (String)this.mObj1);
         }
      } else {
         var1.putParcelable("obj", (Parcelable)this.mObj1);
      }

      var1.putInt("type", this.mType);
      var1.putInt("int1", this.mInt1);
      var1.putInt("int2", this.mInt2);
      ColorStateList var3 = this.mTintList;
      if (var3 != null) {
         var1.putParcelable("tint_list", var3);
      }

      Mode var4 = this.mTintMode;
      if (var4 != DEFAULT_TINT_MODE) {
         var1.putString("tint_mode", var4.name());
      }

      return var1;
   }

   public Icon toIcon() {
      int var1 = this.mType;
      if (var1 != -1) {
         Icon var2;
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 4) {
                     if (var1 != 5) {
                        throw new IllegalArgumentException("Unknown type");
                     }

                     if (VERSION.SDK_INT >= 26) {
                        var2 = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                     } else {
                        var2 = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                     }
                  } else {
                     var2 = Icon.createWithContentUri((String)this.mObj1);
                  }
               } else {
                  var2 = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
               }
            } else {
               var2 = Icon.createWithResource(this.getResPackage(), this.mInt1);
            }
         } else {
            var2 = Icon.createWithBitmap((Bitmap)this.mObj1);
         }

         ColorStateList var3 = this.mTintList;
         if (var3 != null) {
            var2.setTintList(var3);
         }

         Mode var4 = this.mTintMode;
         if (var4 != DEFAULT_TINT_MODE) {
            var2.setTintMode(var4);
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
         StringBuilder var1;
         label37: {
            var1 = new StringBuilder("Icon(typ=");
            var1.append(typeToString(this.mType));
            int var2 = this.mType;
            if (var2 != 1) {
               if (var2 == 2) {
                  var1.append(" pkg=");
                  var1.append(this.getResPackage());
                  var1.append(" id=");
                  var1.append(String.format("0x%08x", this.getResId()));
                  break label37;
               }

               if (var2 == 3) {
                  var1.append(" len=");
                  var1.append(this.mInt1);
                  if (this.mInt2 != 0) {
                     var1.append(" off=");
                     var1.append(this.mInt2);
                  }
                  break label37;
               }

               if (var2 == 4) {
                  var1.append(" uri=");
                  var1.append(this.mObj1);
                  break label37;
               }

               if (var2 != 5) {
                  break label37;
               }
            }

            var1.append(" size=");
            var1.append(((Bitmap)this.mObj1).getWidth());
            var1.append("x");
            var1.append(((Bitmap)this.mObj1).getHeight());
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
