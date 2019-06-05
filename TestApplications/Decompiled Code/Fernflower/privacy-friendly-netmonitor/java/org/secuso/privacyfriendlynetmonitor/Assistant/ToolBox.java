package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import java.io.File;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;

public class ToolBox {
   private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

   public static long fourBytesToLong(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.allocate(8);
      var1.position(4);
      var1.put(var0);
      var1.position(0);
      return var1.getLong();
   }

   public static InetAddress getLocalAddress() {
      // $FF: Couldn't be decompiled
   }

   public static byte[] hexStringToByteArray(String var0) {
      byte[] var1 = new byte[var0.length() / 2];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         int var3 = var2 * 2;
         var1[var2] = (byte)((byte)Integer.parseInt(var0.substring(var3, var3 + 2), 16));
      }

      return var1;
   }

   private int hexToBin(char var1) {
      if ('0' <= var1 && var1 <= '9') {
         return var1 - 48;
      } else if ('A' <= var1 && var1 <= 'F') {
         return var1 - 65 + 10;
      } else {
         return 'a' <= var1 && var1 <= 'f' ? var1 - 97 + 10 : -1;
      }
   }

   public static String hexToIp4(String var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = var0.length() - 1; var2 >= 0; var2 -= 2) {
         var1.append(Integer.parseInt(var0.substring(var2 - 1, var2 + 1), 16));
         var1.append(".");
      }

      return var1.substring(0, var1.length() - 1).toString();
   }

   public static String hexToIp6(String var0) {
      StringBuilder var1 = new StringBuilder();

      int var3;
      for(int var2 = 0; var2 < var0.length(); var2 = var3) {
         var3 = var2 + 8;
         String var4 = var0.substring(var2, var3);

         for(var2 = var4.length() - 1; var2 >= 0; var2 -= 2) {
            var1.append(var4.substring(var2 - 1, var2 + 1));
            String var5;
            if (var2 == 5) {
               var5 = ":";
            } else {
               var5 = "";
            }

            var1.append(var5);
         }

         var1.append(":");
      }

      return var1.substring(0, var1.length() - 1).toString();
   }

   public static byte[] intToTwoBytes(int var0) {
      ByteBuffer var1 = ByteBuffer.allocate(4);
      byte[] var2 = new byte[2];
      var1.putInt(var0);
      var1.position(2);
      var1.get(var2);
      return var2;
   }

   public static boolean isAnalyzerServiceRunning() {
      Iterator var0 = ((ActivityManager)RunStore.getContext().getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();

      RunningServiceInfo var1;
      do {
         if (!var0.hasNext()) {
            return false;
         }

         var1 = (RunningServiceInfo)var0.next();
      } while(!PassiveService.class.getName().equals(var1.service.getClassName()));

      return true;
   }

   public static byte[] longToFourBytes(long var0) {
      ByteBuffer var2 = ByteBuffer.allocate(8);
      byte[] var3 = new byte[4];
      var2.putLong(var0);
      var2.position(4);
      var2.get(var3);
      return var3;
   }

   public static String printExportHexString(byte[] var0) {
      String var1 = printHexBinary(var0);
      String var5 = "000000 ";

      int var4;
      for(int var2 = 0; var2 + 1 < var1.length(); var2 = var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var5);
         var3.append(" ");
         var4 = var2 + 2;
         var3.append(var1.substring(var2, var4));
         var5 = var3.toString();
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(var5);
      var6.append(" ......");
      return var6.toString();
   }

   public static String printHexBinary(byte[] var0) {
      int var1 = 0;
      StringBuilder var2 = new StringBuilder(var0.length * 2);

      for(int var3 = var0.length; var1 < var3; ++var1) {
         byte var4 = var0[var1];
         var2.append(hexCode[var4 >> 4 & 15]);
         var2.append(hexCode[var4 & 15]);
      }

      return var2.toString();
   }

   public static byte[] reverseByteArray(byte[] var0) {
      int var1 = 0;
      byte[] var2 = new byte[var0.length];

      for(int var3 = var0.length - 1; var3 >= 0; --var3) {
         var2[var1] = (byte)var0[var3];
         ++var1;
      }

      return var2;
   }

   public static int searchByteArray(byte[] var0, byte[] var1) {
      Byte[] var2 = new Byte[var1.length];

      int var3;
      for(var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3];
      }

      byte var4 = -1;
      ArrayDeque var6 = new ArrayDeque(var0.length);
      int var5 = 0;

      while(true) {
         var3 = var4;
         if (var5 >= var0.length) {
            break;
         }

         if (var6.size() == var2.length) {
            if (Arrays.equals((Byte[])var6.toArray(new Byte[0]), var2)) {
               var3 = var5 - var2.length;
               break;
            }

            var6.pop();
            var6.addLast(var0[var5]);
         } else {
            var6.addLast(var0[var5]);
         }

         ++var5;
      }

      return var3;
   }

   public static int twoBytesToInt(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.allocate(4);
      var1.position(2);
      var1.put(var0);
      var1.position(0);
      return var1.getInt();
   }

   public String getIfs(Context var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1.getFilesDir().getAbsolutePath());
      var2.append(File.separator);
      var2.append("iflist");
      String var3 = var2.toString();
      var2 = new StringBuilder();
      var2.append("rm ");
      var2.append(var3);
      ExecCom.user(var2.toString());
      var2 = new StringBuilder();
      var2.append("netcfg | grep UP -> ");
      var2.append(var3);
      ExecCom.user(var2.toString());
      var2 = new StringBuilder();
      var2.append("cat ");
      var2.append(var3);
      return ExecCom.userForResult(var2.toString());
   }
}
