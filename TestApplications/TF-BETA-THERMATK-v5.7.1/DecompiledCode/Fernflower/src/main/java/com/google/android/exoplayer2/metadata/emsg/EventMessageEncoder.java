package com.google.android.exoplayer2.metadata.emsg;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class EventMessageEncoder {
   private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
   private final DataOutputStream dataOutputStream;

   public EventMessageEncoder() {
      this.dataOutputStream = new DataOutputStream(this.byteArrayOutputStream);
   }

   private static void writeNullTerminatedString(DataOutputStream var0, String var1) throws IOException {
      var0.writeBytes(var1);
      var0.writeByte(0);
   }

   private static void writeUnsignedInt(DataOutputStream var0, long var1) throws IOException {
      var0.writeByte((int)(var1 >>> 24) & 255);
      var0.writeByte((int)(var1 >>> 16) & 255);
      var0.writeByte((int)(var1 >>> 8) & 255);
      var0.writeByte((int)var1 & 255);
   }

   public byte[] encode(EventMessage var1, long var2) {
      boolean var4;
      if (var2 >= 0L) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkArgument(var4);
      this.byteArrayOutputStream.reset();

      IOException var10000;
      label38: {
         boolean var10001;
         String var5;
         label32: {
            try {
               writeNullTerminatedString(this.dataOutputStream, var1.schemeIdUri);
               if (var1.value != null) {
                  var5 = var1.value;
                  break label32;
               }
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label38;
            }

            var5 = "";
         }

         try {
            writeNullTerminatedString(this.dataOutputStream, var5);
            writeUnsignedInt(this.dataOutputStream, var2);
            long var6 = Util.scaleLargeTimestamp(var1.presentationTimeUs, var2, 1000000L);
            writeUnsignedInt(this.dataOutputStream, var6);
            var2 = Util.scaleLargeTimestamp(var1.durationMs, var2, 1000L);
            writeUnsignedInt(this.dataOutputStream, var2);
            writeUnsignedInt(this.dataOutputStream, var1.id);
            this.dataOutputStream.write(var1.messageData);
            this.dataOutputStream.flush();
            byte[] var11 = this.byteArrayOutputStream.toByteArray();
            return var11;
         } catch (IOException var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      IOException var10 = var10000;
      throw new RuntimeException(var10);
   }
}
