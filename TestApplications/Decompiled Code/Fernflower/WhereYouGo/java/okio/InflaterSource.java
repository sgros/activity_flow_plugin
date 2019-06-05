package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class InflaterSource implements Source {
   private int bufferBytesHeldByInflater;
   private boolean closed;
   private final Inflater inflater;
   private final BufferedSource source;

   InflaterSource(BufferedSource var1, Inflater var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("inflater == null");
      } else {
         this.source = var1;
         this.inflater = var2;
      }
   }

   public InflaterSource(Source var1, Inflater var2) {
      this(Okio.buffer(var1), var2);
   }

   private void releaseInflatedBytes() throws IOException {
      if (this.bufferBytesHeldByInflater != 0) {
         int var1 = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
         this.bufferBytesHeldByInflater -= var1;
         this.source.skip((long)var1);
      }

   }

   public void close() throws IOException {
      if (!this.closed) {
         this.inflater.end();
         this.closed = true;
         this.source.close();
      }

   }

   public long read(Buffer var1, long var2) throws IOException {
      long var4 = 0L;
      if (var2 < 0L) {
         throw new IllegalArgumentException("byteCount < 0: " + var2);
      } else if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         if (var2 == 0L) {
            var2 = var4;
         } else {
            while(true) {
               boolean var6 = this.refill();

               DataFormatException var10000;
               label65: {
                  Segment var7;
                  int var8;
                  boolean var10001;
                  try {
                     var7 = var1.writableSegment(1);
                     var8 = this.inflater.inflate(var7.data, var7.limit, 8192 - var7.limit);
                  } catch (DataFormatException var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label65;
                  }

                  if (var8 > 0) {
                     label50: {
                        try {
                           var7.limit += var8;
                           var1.size += (long)var8;
                        } catch (DataFormatException var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label50;
                        }

                        var2 = (long)var8;
                     }
                  } else {
                     label76: {
                        label77: {
                           try {
                              if (this.inflater.finished() || this.inflater.needsDictionary()) {
                                 break label77;
                              }
                           } catch (DataFormatException var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label76;
                           }

                           if (!var6) {
                              continue;
                           }

                           try {
                              EOFException var14 = new EOFException("source exhausted prematurely");
                              throw var14;
                           } catch (DataFormatException var9) {
                              var10000 = var9;
                              var10001 = false;
                              break label76;
                           }
                        }

                        try {
                           this.releaseInflatedBytes();
                           if (var7.pos == var7.limit) {
                              var1.head = var7.pop();
                              SegmentPool.recycle(var7);
                           }
                        } catch (DataFormatException var11) {
                           var10000 = var11;
                           var10001 = false;
                           break label76;
                        }

                        var2 = -1L;
                     }
                  }
                  break;
               }

               DataFormatException var15 = var10000;
               throw new IOException(var15);
            }
         }

         return var2;
      }
   }

   public boolean refill() throws IOException {
      boolean var1 = false;
      if (this.inflater.needsInput()) {
         this.releaseInflatedBytes();
         if (this.inflater.getRemaining() != 0) {
            throw new IllegalStateException("?");
         }

         if (this.source.exhausted()) {
            var1 = true;
         } else {
            Segment var2 = this.source.buffer().head;
            this.bufferBytesHeldByInflater = var2.limit - var2.pos;
            this.inflater.setInput(var2.data, var2.pos, this.bufferBytesHeldByInflater);
         }
      }

      return var1;
   }

   public Timeout timeout() {
      return this.source.timeout();
   }
}
