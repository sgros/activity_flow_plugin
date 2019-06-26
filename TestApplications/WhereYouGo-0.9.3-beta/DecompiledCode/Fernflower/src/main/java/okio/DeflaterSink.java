package okio;

import java.io.IOException;
import java.util.zip.Deflater;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink implements Sink {
   private boolean closed;
   private final Deflater deflater;
   private final BufferedSink sink;

   DeflaterSink(BufferedSink var1, Deflater var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("inflater == null");
      } else {
         this.sink = var1;
         this.deflater = var2;
      }
   }

   public DeflaterSink(Sink var1, Deflater var2) {
      this(Okio.buffer(var1), var2);
   }

   @IgnoreJRERequirement
   private void deflate(boolean var1) throws IOException {
      Buffer var2 = this.sink.buffer();

      while(true) {
         Segment var3 = var2.writableSegment(1);
         int var4;
         if (var1) {
            var4 = this.deflater.deflate(var3.data, var3.limit, 8192 - var3.limit, 2);
         } else {
            var4 = this.deflater.deflate(var3.data, var3.limit, 8192 - var3.limit);
         }

         if (var4 > 0) {
            var3.limit += var4;
            var2.size += (long)var4;
            this.sink.emitCompleteSegments();
         } else if (this.deflater.needsInput()) {
            if (var3.pos == var3.limit) {
               var2.head = var3.pop();
               SegmentPool.recycle(var3);
            }

            return;
         }
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         Throwable var1 = null;

         try {
            this.finishDeflate();
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

   void finishDeflate() throws IOException {
      this.deflater.finish();
      this.deflate(false);
   }

   public void flush() throws IOException {
      this.deflate(true);
      this.sink.flush();
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public String toString() {
      return "DeflaterSink(" + this.sink + ")";
   }

   public void write(Buffer var1, long var2) throws IOException {
      Util.checkOffsetAndCount(var1.size, 0L, var2);

      int var5;
      for(; var2 > 0L; var2 -= (long)var5) {
         Segment var4 = var1.head;
         var5 = (int)Math.min(var2, (long)(var4.limit - var4.pos));
         this.deflater.setInput(var4.data, var4.pos, var5);
         this.deflate(false);
         var1.size -= (long)var5;
         var4.pos += var5;
         if (var4.pos == var4.limit) {
            var1.head = var4.pop();
            SegmentPool.recycle(var4);
         }
      }

   }
}
