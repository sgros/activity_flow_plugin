package menion.android.whereyougo.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class LogWriter {
   private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

   static void log(String var0, String var1) {
      if (var1 != null) {
         try {
            StringBuilder var2 = new StringBuilder();
            var0 = var2.append(FileSystem.getRoot()).append(File.separator).append(var0).toString();
            FileWriter var7 = new FileWriter(var0, true);
            PrintWriter var6 = new PrintWriter(var7);
            var2 = new StringBuilder();
            var2 = var2.append("");
            DateFormat var3 = dateFormat;
            Date var4 = new Date();
            var6.println(var2.append(var3.format(var4)).append("\n").append(var1).toString());
            var6.close();
         } catch (Exception var5) {
         }
      }

   }

   static void log(String var0, String var1, Throwable var2) {
      if (var2 != null) {
         try {
            StringBuilder var3 = new StringBuilder();
            var0 = var3.append(FileSystem.getRoot()).append(File.separator).append(var0).toString();
            FileWriter var8 = new FileWriter(var0, true);
            PrintWriter var7 = new PrintWriter(var8);
            var3 = new StringBuilder();
            StringBuilder var4 = var3.append("");
            DateFormat var9 = dateFormat;
            Date var5 = new Date();
            var7.println(var4.append(var9.format(var5)).append("\n").append(var1).toString());
            var2.printStackTrace(var7);
            var7.close();
         } catch (Exception var6) {
         }
      }

   }

   static void log(String var0, Throwable var1) {
      if (var1 != null) {
         try {
            StringBuilder var2 = new StringBuilder();
            var0 = var2.append(FileSystem.getRoot()).append(File.separator).append(var0).toString();
            FileWriter var7 = new FileWriter(var0, true);
            PrintWriter var6 = new PrintWriter(var7);
            var2 = new StringBuilder();
            StringBuilder var3 = var2.append("");
            DateFormat var4 = dateFormat;
            Date var8 = new Date();
            var6.println(var3.append(var4.format(var8)).toString());
            var1.printStackTrace(var6);
            var6.close();
         } catch (Exception var5) {
         }
      }

   }
}
