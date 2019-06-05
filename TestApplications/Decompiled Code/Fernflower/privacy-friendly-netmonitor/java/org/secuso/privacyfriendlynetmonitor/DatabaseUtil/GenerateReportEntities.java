package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GenerateReportEntities {
   public static void generateReportEntities(Context var0, ReportEntityDao var1) {
      System.out.println("Start with entity generation");
      ArrayList var2 = new ArrayList();
      new ArrayList();
      var2.add("com.android.chrome");
      var2.add("com.android.vending");
      var2.add("com.google.android.youtube");
      SharedPreferences var3 = var0.getSharedPreferences("SELECTEDAPPS", 0);
      Editor var4 = var3.edit();
      PackageManager var5 = var0.getPackageManager();
      var3.getAll().keySet();
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         String var20 = (String)var6.next();
         if (!var3.contains(var20)) {
            var4.putString(var20, var20);
            var4.commit();
         }
      }

      long var7 = System.currentTimeMillis();
      int var9 = 1;
      int var10 = 0;

      for(ArrayList var21 = var2; var10 < 1; ++var10) {
         for(int var11 = 2018; var11 <= 2018; ++var11) {
            PrintStream var24 = System.out;
            StringBuilder var26 = new StringBuilder();
            var26.append("New year: ");
            var26.append(var11);
            var24.println(var26.toString());

            for(int var12 = 1; var12 <= 1; ++var12) {
               var24 = System.out;
               var26 = new StringBuilder();
               var26.append("New Month: ");
               var26.append(var12);
               var24.println(var26.toString());

               for(int var13 = 1; var13 <= 30; ++var13) {
                  PrintStream var28 = System.out;
                  StringBuilder var25 = new StringBuilder();
                  var25.append("New Day: ");
                  var25.append(var13);
                  var28.println(var25.toString());

                  for(int var14 = 0; var14 < 24; ++var14) {
                     for(var6 = var21.iterator(); var6.hasNext(); ++var9) {
                        String var29 = (String)var6.next();
                        ReportEntity var15 = new ReportEntity();

                        PackageInfo var27;
                        try {
                           var27 = var5.getPackageInfo(var29, 0);
                        } catch (NameNotFoundException var19) {
                           var19.printStackTrace();
                           var27 = null;
                        }

                        new String();
                        var15.setUserID(String.valueOf(var27.applicationInfo.uid));
                        var15.setAppName(var29);
                        var15.setRemoteAddress(getRandomString());
                        var15.setRemoteHex(getRandomString());
                        var15.setRemoteHost(getRandomString());
                        var15.setLocalAddress(getRandomString());
                        var15.setLocalHex(getRandomString());
                        var15.setServicePort(getRandomString());
                        var15.setPayloadProtocol(getRandomString());
                        var15.setTransportProtocol(getRandomString());
                        var15.setLocalPort(getRandomString());
                        if (Math.random() < 0.5D) {
                           var15.setConnectionInfo("Encrypted()");
                        } else {
                           var15.setConnectionInfo("Unencrypted()");
                        }

                        String var31;
                        if (var12 < 10) {
                           var25 = new StringBuilder();
                           var25.append("0");
                           new String();
                           var25.append(String.valueOf(var12));
                           var31 = var25.toString();
                        } else {
                           new String();
                           var31 = String.valueOf(var12);
                        }

                        if (var13 < 10) {
                           var26 = new StringBuilder();
                           var26.append("0");
                           new String();
                           var26.append(String.valueOf(var13));
                           var29 = var26.toString();
                        } else {
                           new String();
                           var29 = String.valueOf(var13);
                        }

                        String var32;
                        if (var14 < 10) {
                           StringBuilder var30 = new StringBuilder();
                           var30.append("0");
                           new String();
                           var30.append(String.valueOf(var14));
                           var32 = var30.toString();
                        } else {
                           new String();
                           var32 = String.valueOf(var14);
                        }

                        StringBuilder var16 = new StringBuilder();
                        new String();
                        var16.append(String.valueOf(var11));
                        var16.append("-");
                        var16.append(var31);
                        var16.append("-");
                        var16.append(var29);
                        var16.append(" ");
                        var16.append(var32);
                        var16.append(":04:20.420");
                        var15.setTimeStamp(var16.toString());
                        var1.insertOrReplace(var15);
                     }
                  }
               }
            }
         }
      }

      long var17 = System.currentTimeMillis();
      PrintStream var23 = System.out;
      StringBuilder var22 = new StringBuilder();
      var22.append("Generation needed ");
      var22.append((var17 - var7) / 1000L);
      var22.append(" seconds, for the generation of ");
      var22.append(var9 - 1);
      var22.append(" reports");
      var23.println(var22.toString());
   }

   private static String getRandomString() {
      StringBuilder var0 = new StringBuilder();
      Random var1 = new Random();

      while(var0.length() < 18) {
         var0.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt((int)(var1.nextFloat() * (float)"ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length())));
      }

      return var0.toString();
   }
}
