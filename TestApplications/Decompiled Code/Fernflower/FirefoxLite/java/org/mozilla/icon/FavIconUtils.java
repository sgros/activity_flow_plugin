package org.mozilla.icon;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.urlutils.UrlUtils;

public class FavIconUtils {
   public static boolean ensureDir(File var0) {
      boolean var1 = var0.mkdirs();
      boolean var2 = true;
      if (var1) {
         return true;
      } else {
         if (!var0.exists() || !var0.isDirectory() || !var0.canWrite()) {
            var2 = false;
         }

         return var2;
      }
   }

   public static String generateMD5(String var0) throws NoSuchAlgorithmException {
      MessageDigest var1 = MessageDigest.getInstance("MD5");
      var1.update(var0.getBytes(Charset.defaultCharset()));
      byte[] var5 = var1.digest();
      StringBuilder var4 = new StringBuilder();
      int var2 = var5.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var4.append(String.format("%02X", var5[var3]));
      }

      return var4.toString();
   }

   public static Bitmap getBitmapFromUri(Context var0, String var1) {
      return var1.contains("//android_asset/") ? getIconFromAssets(var0, var1.substring(var1.indexOf("//android_asset/") + "//android_asset/".length())) : BitmapFactory.decodeFile(Uri.parse(var1).getPath(), new Options());
   }

   private static int getContractColor(int var0) {
      if (1.0D - ((double)Color.red(var0) * 0.299D + (double)Color.green(var0) * 0.587D + (double)Color.blue(var0) * 0.114D) / 255.0D < 0.5D) {
         var0 = -16777216;
      } else {
         var0 = -1;
      }

      return var0;
   }

   public static int getDominantColor(Bitmap var0) {
      return getDominantColor(var0, true);
   }

   private static int getDominantColor(Bitmap var0, boolean var1) {
      if (var0 == null) {
         return Color.argb(255, 255, 255, 255);
      } else {
         int[] var2 = new int[36];
         float[] var3 = new float[36];
         float[] var4 = new float[36];
         float[] var5 = new float[36];
         float[] var6 = new float[3];
         int var7 = var0.getHeight();
         int var8 = var0.getWidth();
         int[] var9 = new int[var8 * var7];
         var0.getPixels(var9, 0, var8, 0, 0, var8, var7);
         int var10 = 0;

         int var11;
         int var13;
         for(var11 = -1; var10 < var7; ++var10) {
            for(int var12 = 0; var12 < var8; var11 = var13) {
               var13 = var9[var10 * var8 + var12];
               if (Color.alpha(var13) < 128) {
                  var13 = var11;
               } else {
                  label55: {
                     Color.colorToHSV(var13, var6);
                     if (var1) {
                        var13 = var11;
                        if (var6[1] <= 0.35F) {
                           break label55;
                        }

                        if (var6[2] <= 0.35F) {
                           var13 = var11;
                           break label55;
                        }
                     }

                     int var14 = (int)Math.floor((double)(var6[0] / 10.0F));
                     var3[var14] += var6[0];
                     var4[var14] += var6[1];
                     var5[var14] += var6[2];
                     int var10002 = var2[var14]++;
                     if (var11 >= 0) {
                        var13 = var11;
                        if (var2[var14] <= var2[var11]) {
                           break label55;
                        }
                     }

                     var13 = var14;
                  }
               }

               ++var12;
            }
         }

         if (var11 < 0) {
            return Color.argb(255, 255, 255, 255);
         } else {
            var6[0] = var3[var11] / (float)var2[var11];
            var6[1] = var4[var11] / (float)var2[var11];
            var6[2] = var5[var11] / (float)var2[var11];
            return Color.HSVToColor(var6);
         }
      }
   }

   public static Bitmap getIconFromAssets(Context var0, String var1) {
      AssetManager var3 = var0.getAssets();

      Bitmap var4;
      try {
         var4 = BitmapFactory.decodeStream(var3.open(var1));
      } catch (IOException var2) {
         var4 = null;
      }

      return var4;
   }

   public static Bitmap getInitialBitmap(char var0, int var1, float var2, int var3) {
      char[] var4 = new char[]{(char)var0};
      int var9 = getContractColor(var1);
      Paint var5 = new Paint();
      var5.setTextSize(var2);
      var5.setColor(var9);
      var5.setTextAlign(Align.CENTER);
      var5.setAntiAlias(true);
      Rect var6 = new Rect();
      var5.getTextBounds(var4, 0, 1, var6);
      Bitmap var7 = Bitmap.createBitmap(var3, var3, Config.ARGB_8888);
      Canvas var8 = new Canvas(var7);
      var8.drawColor(var1);
      var8.drawText(var4, 0, 1, (float)var3 / 2.0F, (float)(var3 + var6.height()) / 2.0F, var5);
      return var7;
   }

   public static Bitmap getInitialBitmap(Bitmap var0, char var1, float var2, int var3) {
      return getInitialBitmap(var1, getDominantColor(var0), var2, var3);
   }

   public static char getRepresentativeCharacter(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return '?';
      } else {
         var0 = getRepresentativeSnippet(var0);

         for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            if (Character.isLetterOrDigit(var2)) {
               return Character.toUpperCase(var2);
            }
         }

         return '?';
      }
   }

   private static String getRepresentativeSnippet(String var0) {
      Uri var1 = Uri.parse(var0);
      String var2 = var1.getHost();
      var0 = var2;
      if (TextUtils.isEmpty(var2)) {
         var0 = var1.getPath();
      }

      return TextUtils.isEmpty(var0) ? "?" : UrlUtils.stripCommonSubdomains(var0);
   }

   public static String saveBitmapToDirectory(File var0, String var1, Bitmap var2, CompressFormat var3, int var4) {
      String var5 = Uri.encode(var1);

      try {
         var1 = generateMD5(var1);
      } catch (NoSuchAlgorithmException var6) {
         var6.printStackTrace();
         var1 = var5;
      }

      return saveBitmapToFile(var0, var1, var2, var3, var4);
   }

   private static String saveBitmapToFile(File param0, String param1, Bitmap param2, CompressFormat param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public interface Consumer {
      void accept(Object var1);
   }

   public static class SaveBitmapTask extends AsyncTask {
      private Bitmap bitmap;
      private FavIconUtils.Consumer callback;
      private final CompressFormat compressFormat;
      private File directory;
      private final int quality;
      private String url;

      public SaveBitmapTask(File var1, String var2, Bitmap var3, FavIconUtils.Consumer var4, CompressFormat var5, int var6) {
         this.directory = var1;
         this.url = var2;
         this.bitmap = var3;
         this.callback = var4;
         this.compressFormat = var5;
         this.quality = var6;
      }

      protected String doInBackground(Void... var1) {
         return FavIconUtils.saveBitmapToDirectory(this.directory, this.url, this.bitmap, this.compressFormat, this.quality);
      }

      protected void onPostExecute(String var1) {
         this.callback.accept(var1);
      }
   }

   public static class SaveBitmapsTask extends AsyncTask {
      private List bytesList;
      private FavIconUtils.Consumer callback;
      private final CompressFormat compressFormat;
      private File directory;
      private final int quality;
      private List urls;

      public SaveBitmapsTask(File var1, List var2, List var3, FavIconUtils.Consumer var4, CompressFormat var5, int var6) {
         this.directory = var1;
         this.urls = var2;
         this.bytesList = var3;
         this.callback = var4;
         this.compressFormat = var5;
         this.quality = var6;
      }

      protected List doInBackground(Void... var1) {
         ArrayList var4 = new ArrayList();

         for(int var2 = 0; var2 < this.urls.size(); ++var2) {
            byte[] var3 = (byte[])this.bytesList.get(var2);
            Bitmap var5 = BitmapFactory.decodeByteArray(var3, 0, var3.length);
            var4.add(FavIconUtils.saveBitmapToDirectory(this.directory, (String)this.urls.get(var2), var5, this.compressFormat, this.quality));
         }

         return var4;
      }

      protected void onPostExecute(List var1) {
         this.callback.accept(var1);
      }
   }
}
