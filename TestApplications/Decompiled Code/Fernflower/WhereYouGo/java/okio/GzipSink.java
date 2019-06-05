package okio;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public final class GzipSink implements Sink {
   private boolean closed;
   private final CRC32 crc = new CRC32();
   private final Deflater deflater;
   private final DeflaterSink deflaterSink;
   private final BufferedSink sink;

   public GzipSink(Sink var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("sink == null");
      } else {
         this.deflater = new Deflater(-1, true);
         this.sink = Okio.buffer(var1);
         this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
         this.writeHeader();
      }
   }

   private void updateCrc(Buffer var1, long var2) {
      for(Segment var5 = var1.head; var2 > 0L; var5 = var5.next) {
         int var4 = (int)Math.min(var2, (long)(var5.limit - var5.pos));
         this.crc.update(var5.data, var5.pos, var4);
         var2 -= (long)var4;
      }

   }

   private void writeFooter() throws IOException {
      this.sink.writeIntLe((int)this.crc.getValue());
      this.sink.writeIntLe((int)this.deflater.getBytesRead());
   }

   private void writeHeader() {
      Buffer var1 = this.sink.buffer();
      var1.writeShort(8075);
      var1.writeByte(8);
      var1.writeByte(0);
      var1.writeInt(0);
      var1.writeByte(0);
      var1.writeByte(0);
   }

   public void close() throws IOException {
      if (!this.closed) {
         Throwable var1 = null;

         try {
            this.deflaterSink.finishDeflate();
            this.writeFooter();
         } catch (Throwable var4) {
            var1 = var4;
         }

         Throwable var2;
         label36: {
            try {
               this.deflater.end();
            } catch (Throwable var6) {
               var2 = var1;
               if (var1 == null) {
                  var2 = var6;
               }
               break label36;
            }

            var2 = var1;
         }

         label30: {
            try {
               this.sink.close();
            } catch (Throwable var5) {
               var1 = var2;
               if (var2 == null) {
                  var1 = var5;
               }
               break label30;
            }

            var1 = var2;
         }

         this.closed = true;
         if (var1 != null) {
            Util.sneakyRethrow(var1);
         }
      }

   }

   public Deflater deflater() {
      return this.deflater;
   }

   public void flush() throws IOException {
      this.deflaterSink.flush();
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public void write(Buffer var1, long var2) throws IOException {
      if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else {
         if (var2 != 0L) {
            this.updateCrc(var1, var2);
            this.deflaterSink.write(var1, var2);
         }

      }
   }
}
