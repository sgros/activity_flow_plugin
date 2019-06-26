package okhttp3.internal.http2;

import java.io.IOException;
import okhttp3.internal.Util;
import okio.ByteString;

public final class Http2 {
   static final String[] BINARY = new String[256];
   static final ByteString CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
   static final String[] FLAGS = new String[64];
   static final byte FLAG_ACK = 1;
   static final byte FLAG_COMPRESSED = 32;
   static final byte FLAG_END_HEADERS = 4;
   static final byte FLAG_END_PUSH_PROMISE = 4;
   static final byte FLAG_END_STREAM = 1;
   static final byte FLAG_NONE = 0;
   static final byte FLAG_PADDED = 8;
   static final byte FLAG_PRIORITY = 32;
   private static final String[] FRAME_NAMES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
   static final int INITIAL_MAX_FRAME_SIZE = 16384;
   static final byte TYPE_CONTINUATION = 9;
   static final byte TYPE_DATA = 0;
   static final byte TYPE_GOAWAY = 7;
   static final byte TYPE_HEADERS = 1;
   static final byte TYPE_PING = 6;
   static final byte TYPE_PRIORITY = 2;
   static final byte TYPE_PUSH_PROMISE = 5;
   static final byte TYPE_RST_STREAM = 3;
   static final byte TYPE_SETTINGS = 4;
   static final byte TYPE_WINDOW_UPDATE = 8;

   static {
      int var0;
      for(var0 = 0; var0 < BINARY.length; ++var0) {
         BINARY[var0] = Util.format("%8s", Integer.toBinaryString(var0)).replace(' ', '0');
      }

      FLAGS[0] = "";
      FLAGS[1] = "END_STREAM";
      int[] var1 = new int[]{1};
      FLAGS[8] = "PADDED";
      int var2 = var1.length;

      int var3;
      for(var0 = 0; var0 < var2; ++var0) {
         var3 = var1[var0];
         FLAGS[var3 | 8] = FLAGS[var3] + "|PADDED";
      }

      FLAGS[4] = "END_HEADERS";
      FLAGS[32] = "PRIORITY";
      FLAGS[36] = "END_HEADERS|PRIORITY";
      int[] var4 = new int[]{4, 32, 36};
      var3 = var4.length;

      for(var0 = 0; var0 < var3; ++var0) {
         int var5 = var4[var0];
         int var6 = var1.length;

         for(var2 = 0; var2 < var6; ++var2) {
            int var7 = var1[var2];
            FLAGS[var7 | var5] = FLAGS[var7] + '|' + FLAGS[var5];
            FLAGS[var7 | var5 | 8] = FLAGS[var7] + '|' + FLAGS[var5] + "|PADDED";
         }
      }

      for(var0 = 0; var0 < FLAGS.length; ++var0) {
         if (FLAGS[var0] == null) {
            FLAGS[var0] = BINARY[var0];
         }
      }

   }

   private Http2() {
   }

   static String formatFlags(byte var0, byte var1) {
      String var2;
      if (var1 == 0) {
         var2 = "";
      } else {
         switch(var0) {
         case 2:
         case 3:
         case 7:
         case 8:
            var2 = BINARY[var1];
            break;
         case 4:
         case 6:
            if (var1 == 1) {
               var2 = "ACK";
            } else {
               var2 = BINARY[var1];
            }
            break;
         case 5:
         default:
            if (var1 < FLAGS.length) {
               var2 = FLAGS[var1];
            } else {
               var2 = BINARY[var1];
            }

            if (var0 == 5 && (var1 & 4) != 0) {
               var2 = var2.replace("HEADERS", "PUSH_PROMISE");
            } else if (var0 == 0 && (var1 & 32) != 0) {
               var2 = var2.replace("PRIORITY", "COMPRESSED");
            }
         }
      }

      return var2;
   }

   static String frameLog(boolean var0, int var1, int var2, byte var3, byte var4) {
      String var5;
      if (var3 < FRAME_NAMES.length) {
         var5 = FRAME_NAMES[var3];
      } else {
         var5 = Util.format("0x%02x", var3);
      }

      String var6 = formatFlags(var3, var4);
      String var7;
      if (var0) {
         var7 = "<<";
      } else {
         var7 = ">>";
      }

      return Util.format("%s 0x%08x %5d %-13s %s", var7, var1, var2, var5, var6);
   }

   static IllegalArgumentException illegalArgument(String var0, Object... var1) {
      throw new IllegalArgumentException(Util.format(var0, var1));
   }

   static IOException ioException(String var0, Object... var1) throws IOException {
      throw new IOException(Util.format(var0, var1));
   }
}
