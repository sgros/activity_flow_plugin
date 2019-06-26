package com.github.mikephil.charting.utils;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtils {
   private static final String LOG = "MPChart-FileUtils";

   public static List loadBarEntriesFromAssets(AssetManager param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static List loadEntriesFromAssets(AssetManager param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static List loadEntriesFromFile(String var0) {
      File var1 = new File(Environment.getExternalStorageDirectory(), var0);
      ArrayList var14 = new ArrayList();

      IOException var10000;
      label61: {
         BufferedReader var2;
         boolean var10001;
         try {
            FileReader var3 = new FileReader(var1);
            var2 = new BufferedReader(var3);
         } catch (IOException var13) {
            var10000 = var13;
            var10001 = false;
            break label61;
         }

         label58:
         while(true) {
            String var15;
            try {
               var15 = var2.readLine();
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            if (var15 == null) {
               return var14;
            }

            int var4;
            String[] var16;
            try {
               var16 = var15.split("#");
               var4 = var16.length;
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break;
            }

            int var5 = 0;
            if (var4 <= 2) {
               try {
                  Entry var19 = new Entry(Float.parseFloat(var16[0]), (float)Integer.parseInt(var16[1]));
                  var14.add(var19);
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            } else {
               float[] var6;
               try {
                  var6 = new float[var16.length - 1];
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }

               while(true) {
                  try {
                     if (var5 >= var6.length) {
                        break;
                     }

                     var6[var5] = Float.parseFloat(var16[var5]);
                  } catch (IOException var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label58;
                  }

                  ++var5;
               }

               try {
                  BarEntry var18 = new BarEntry((float)Integer.parseInt(var16[var16.length - 1]), var6);
                  var14.add(var18);
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      IOException var17 = var10000;
      Log.e("MPChart-FileUtils", var17.toString());
      return var14;
   }

   public static void saveToSdCard(List var0, String var1) {
      File var2 = new File(Environment.getExternalStorageDirectory(), var1);
      if (!var2.exists()) {
         try {
            var2.createNewFile();
         } catch (IOException var4) {
            Log.e("MPChart-FileUtils", var4.toString());
         }
      }

      IOException var10000;
      label39: {
         Iterator var8;
         BufferedWriter var10;
         boolean var10001;
         try {
            FileWriter var3 = new FileWriter(var2, true);
            var10 = new BufferedWriter(var3);
            var8 = var0.iterator();
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
            break label39;
         }

         while(true) {
            try {
               if (var8.hasNext()) {
                  Entry var11 = (Entry)var8.next();
                  StringBuilder var12 = new StringBuilder();
                  var12.append(var11.getY());
                  var12.append("#");
                  var12.append(var11.getX());
                  var10.append(var12.toString());
                  var10.newLine();
                  continue;
               }
            } catch (IOException var7) {
               var10000 = var7;
               var10001 = false;
               break;
            }

            try {
               var10.close();
               return;
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }
         }
      }

      IOException var9 = var10000;
      Log.e("MPChart-FileUtils", var9.toString());
   }
}
