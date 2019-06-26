package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
   private static final byte FCOMMENT = 4;
   private static final byte FEXTRA = 2;
   private static final byte FHCRC = 1;
   private static final byte FNAME = 3;
   private static final byte SECTION_BODY = 1;
   private static final byte SECTION_DONE = 3;
   private static final byte SECTION_HEADER = 0;
   private static final byte SECTION_TRAILER = 2;
   private final CRC32 crc = new CRC32();
   private final Inflater inflater;
   private final InflaterSource inflaterSource;
   private int section = 0;
   private final BufferedSource source;

   public GzipSource(Source var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         this.inflater = new Inflater(true);
         this.source = Okio.buffer(var1);
         this.inflaterSource = new InflaterSource(this.source, this.inflater);
      }
   }

   private void checkEqual(String var1, int var2, int var3) throws IOException {
      if (var3 != var2) {
         throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", var1, var3, var2));
      }
   }

   private void consumeHeader() throws IOException {
      this.source.require(10L);
      byte var1 = this.source.buffer().getByte(3L);
      boolean var2;
      if ((var1 >> 1 & 1) == 1) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         this.updateCrc(this.source.buffer(), 0L, 10L);
      }

      this.checkEqual("ID1ID2", 8075, this.source.readShort());
      this.source.skip(8L);
      if ((var1 >> 2 & 1) == 1) {
         this.source.require(2L);
         if (var2) {
            this.updateCrc(this.source.buffer(), 0L, 2L);
         }

         short var3 = this.source.buffer().readShortLe();
         this.source.require((long)var3);
         if (var2) {
            this.updateCrc(this.source.buffer(), 0L, (long)var3);
         }

         this.source.skip((long)var3);
      }

      long var4;
      if ((var1 >> 3 & 1) == 1) {
         var4 = this.source.indexOf((byte)0);
         if (var4 == -1L) {
            throw new EOFException();
         }

         if (var2) {
            this.updateCrc(this.source.buffer(), 0L, 1L + var4);
         }

         this.source.skip(1L + var4);
      }

      if ((var1 >> 4 & 1) == 1) {
         var4 = this.source.indexOf((byte)0);
         if (var4 == -1L) {
            throw new EOFException();
         }

         if (var2) {
            this.updateCrc(this.source.buffer(), 0L, 1L + var4);
         }

         this.source.skip(1L + var4);
      }

      if (var2) {
         this.checkEqual("FHCRC", this.source.readShortLe(), (short)((int)this.crc.getValue()));
         this.crc.reset();
      }

   }

   private void consumeTrailer() throws IOException {
      this.checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
      this.checkEqual("ISIZE", this.source.readIntLe(), (int)this.inflater.getBytesWritten());
   }

   private void updateCrc(Buffer var1, long var2, long var4) {
      Segment var13 = var1.head;

      while(true) {
         Segment var6 = var13;
         long var7 = var2;
         long var9 = var4;
         if (var2 < (long)(var13.limit - var13.pos)) {
            while(var9 > 0L) {
               int var11 = (int)((long)var6.pos + var7);
               int var12 = (int)Math.min((long)(var6.limit - var11), var9);
               this.crc.update(var6.data, var11, var12);
               var9 -= (long)var12;
               var7 = 0L;
               var6 = var6.next;
            }

            return;
         }

         var2 -= (long)(var13.limit - var13.pos);
         var13 = var13.next;
      }
   }

   public void close() throws IOException {
      this.inflaterSource.close();
   }

   public long read(Buffer var1, long var2) throws IOException {
      long var4 = 0L;
      if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else {
         if (var2 == 0L) {
            var2 = var4;
         } else {
            if (this.section == 0) {
               this.consumeHeader();
               this.section = 1;
            }

            if (this.section == 1) {
               var4 = var1.size;
               var2 = this.inflaterSource.read(var1, var2);
               if (var2 != -1L) {
                  this.updateCrc(var1, var4, var2);
                  return var2;
               }

               this.section = 2;
            }

            if (this.section == 2) {
               this.consumeTrailer();
               this.section = 3;
               if (!this.source.exhausted()) {
                  throw new IOException("gzip finished without exhausting source");
               }
            }

            var2 = -1L;
         }

         return var2;
      }
   }

   public Timeout timeout() {
      return this.source.timeout();
   }
}
