package okhttp3.internal.ws;

import okio.ByteString;

public final class WebSocketProtocol {
   static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
   static final int B0_FLAG_FIN = 128;
   static final int B0_FLAG_RSV1 = 64;
   static final int B0_FLAG_RSV2 = 32;
   static final int B0_FLAG_RSV3 = 16;
   static final int B0_MASK_OPCODE = 15;
   static final int B1_FLAG_MASK = 128;
   static final int B1_MASK_LENGTH = 127;
   static final int CLOSE_ABNORMAL_TERMINATION = 1006;
   static final int CLOSE_CLIENT_GOING_AWAY = 1001;
   static final long CLOSE_MESSAGE_MAX = 123L;
   static final int CLOSE_NO_STATUS_CODE = 1005;
   static final int CLOSE_PROTOCOL_EXCEPTION = 1002;
   static final int OPCODE_BINARY = 2;
   static final int OPCODE_CONTINUATION = 0;
   static final int OPCODE_CONTROL_CLOSE = 8;
   static final int OPCODE_CONTROL_PING = 9;
   static final int OPCODE_CONTROL_PONG = 10;
   static final int OPCODE_FLAG_CONTROL = 8;
   static final int OPCODE_TEXT = 1;
   static final long PAYLOAD_BYTE_MAX = 125L;
   static final int PAYLOAD_LONG = 127;
   static final int PAYLOAD_SHORT = 126;
   static final long PAYLOAD_SHORT_MAX = 65535L;

   private WebSocketProtocol() {
      throw new AssertionError("No instances.");
   }

   public static String acceptHeader(String var0) {
      return ByteString.encodeUtf8(var0 + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
   }

   static String closeCodeExceptionMessage(int var0) {
      String var1;
      if (var0 >= 1000 && var0 < 5000) {
         if ((var0 < 1004 || var0 > 1006) && (var0 < 1012 || var0 > 2999)) {
            var1 = null;
         } else {
            var1 = "Code " + var0 + " is reserved and may not be used.";
         }
      } else {
         var1 = "Code must be in range [1000,5000): " + var0;
      }

      return var1;
   }

   static void toggleMask(byte[] var0, long var1, byte[] var3, long var4) {
      int var6 = var3.length;

      for(int var7 = 0; (long)var7 < var1; ++var4) {
         int var8 = (int)(var4 % (long)var6);
         var0[var7] = (byte)((byte)(var0[var7] ^ var3[var8]));
         ++var7;
      }

   }

   static void validateCloseCode(int var0) {
      String var1 = closeCodeExceptionMessage(var0);
      if (var1 != null) {
         throw new IllegalArgumentException(var1);
      }
   }
}
