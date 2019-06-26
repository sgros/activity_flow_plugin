package menion.android.whereyougo.utils;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

public class Logger {
   private static final String TAG = "Logger";
   private static final String logFileName = "error.log";

   public static void d(String var0, String var1) {
      if (Const.STATE_DEBUG_LOGS) {
         if (var1 == null) {
            var1 = "null";
         }

         Log.d(var0, var1);
      }

   }

   public static void e(String var0, String var1) {
      Log.e(var0, var1);
      LogWriter.log("error.log", "[" + var0 + "] " + var1);
   }

   public static void e(String var0, String var1, Exception var2) {
      String var3;
      if (var1 != null) {
         var3 = var1;
      } else {
         var3 = "";
      }

      Log.e(var0, var3, var2);
      LogWriter.log("error.log", "[" + var0 + "] " + var1, var2);
   }

   public static void e(String var0, String var1, Throwable var2) {
      Log.e(var0, var1, new Exception(var2.toString()));
      LogWriter.log("error.log", "[" + var0 + "] " + var1, var2);
   }

   public static void i(String var0, String var1) {
      if (Const.STATE_DEBUG_LOGS) {
         if (var1 == null) {
            var1 = "null";
         }

         Log.i(var0, var1);
      }

   }

   public static void printParserState(XmlPullParser var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         d("Logger", var1.append("event:").append(var0.getEventType()).append(", attCount:").append(var0.getAttributeCount()).append(", columnNum:").append(var0.getColumnNumber()).append(", depth:").append(var0.getDepth()).append(", ln:").append(var0.getLineNumber()).append(", ").append(var0.getPositionDescription()).toString());
      } catch (Exception var2) {
         e("Logger", "printParserState()");
      }

   }

   public static void v(String var0, String var1) {
      if (Const.STATE_DEBUG_LOGS) {
         if (var1 == null) {
            var1 = "null";
         }

         Log.v(var0, var1);
      }

   }

   public static void w(String var0, String var1) {
      if (Const.STATE_DEBUG_LOGS) {
         if (var1 == null) {
            var1 = "null";
         }

         Log.w(var0, var1);
      }

   }
}
