package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayDeque;

public abstract class SimpleDecoder implements Decoder {
   private int availableInputBufferCount;
   private final DecoderInputBuffer[] availableInputBuffers;
   private int availableOutputBufferCount;
   private final OutputBuffer[] availableOutputBuffers;
   private final Thread decodeThread;
   private DecoderInputBuffer dequeuedInputBuffer;
   private Exception exception;
   private boolean flushed;
   private final Object lock = new Object();
   private final ArrayDeque queuedInputBuffers = new ArrayDeque();
   private final ArrayDeque queuedOutputBuffers = new ArrayDeque();
   private boolean released;
   private int skippedOutputBufferCount;

   protected SimpleDecoder(DecoderInputBuffer[] var1, OutputBuffer[] var2) {
      this.availableInputBuffers = var1;
      this.availableInputBufferCount = var1.length;
      byte var3 = 0;

      int var4;
      for(var4 = 0; var4 < this.availableInputBufferCount; ++var4) {
         this.availableInputBuffers[var4] = this.createInputBuffer();
      }

      this.availableOutputBuffers = var2;
      this.availableOutputBufferCount = var2.length;

      for(var4 = var3; var4 < this.availableOutputBufferCount; ++var4) {
         this.availableOutputBuffers[var4] = this.createOutputBuffer();
      }

      this.decodeThread = new Thread() {
         public void run() {
            SimpleDecoder.this.run();
         }
      };
      this.decodeThread.start();
   }

   private boolean canDecodeBuffer() {
      boolean var1;
      if (!this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean decode() throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   private void maybeNotifyDecodeLoop() {
      if (this.canDecodeBuffer()) {
         this.lock.notify();
      }

   }

   private void maybeThrowException() throws Exception {
      Exception var1 = this.exception;
      if (var1 != null) {
         throw var1;
      }
   }

   private void releaseInputBufferInternal(DecoderInputBuffer var1) {
      var1.clear();
      DecoderInputBuffer[] var2 = this.availableInputBuffers;
      int var3 = this.availableInputBufferCount++;
      var2[var3] = var1;
   }

   private void releaseOutputBufferInternal(OutputBuffer var1) {
      var1.clear();
      OutputBuffer[] var2 = this.availableOutputBuffers;
      int var3 = this.availableOutputBufferCount++;
      var2[var3] = var1;
   }

   private void run() {
      boolean var1;
      do {
         try {
            var1 = this.decode();
         } catch (InterruptedException var3) {
            throw new IllegalStateException(var3);
         }
      } while(var1);

   }

   protected abstract DecoderInputBuffer createInputBuffer();

   protected abstract OutputBuffer createOutputBuffer();

   protected abstract Exception createUnexpectedDecodeException(Throwable var1);

   protected abstract Exception decode(DecoderInputBuffer var1, OutputBuffer var2, boolean var3);

   public final DecoderInputBuffer dequeueInputBuffer() throws Exception {
      Object var1 = this.lock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label312: {
         boolean var2;
         label307: {
            label306: {
               try {
                  this.maybeThrowException();
                  if (this.dequeuedInputBuffer == null) {
                     break label306;
                  }
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label312;
               }

               var2 = false;
               break label307;
            }

            var2 = true;
         }

         DecoderInputBuffer var3;
         label313: {
            label298: {
               try {
                  Assertions.checkState(var2);
                  if (this.availableInputBufferCount != 0) {
                     break label298;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label312;
               }

               var3 = null;
               break label313;
            }

            DecoderInputBuffer[] var35;
            int var4;
            try {
               var35 = this.availableInputBuffers;
               var4 = this.availableInputBufferCount - 1;
               this.availableInputBufferCount = var4;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label312;
            }

            var3 = var35[var4];
         }

         label290:
         try {
            this.dequeuedInputBuffer = var3;
            var3 = this.dequeuedInputBuffer;
            return var3;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label290;
         }
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public final OutputBuffer dequeueOutputBuffer() throws Exception {
      Object var1 = this.lock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            this.maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
               return null;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            OutputBuffer var15 = (OutputBuffer)this.queuedOutputBuffers.removeFirst();
            return var15;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public final void flush() {
      Object var1 = this.lock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label322: {
         try {
            this.flushed = true;
            this.skippedOutputBufferCount = 0;
            if (this.dequeuedInputBuffer != null) {
               this.releaseInputBufferInternal(this.dequeuedInputBuffer);
               this.dequeuedInputBuffer = null;
            }
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label322;
         }

         while(true) {
            try {
               if (this.queuedInputBuffers.isEmpty()) {
                  break;
               }

               this.releaseInputBufferInternal((DecoderInputBuffer)this.queuedInputBuffers.removeFirst());
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label322;
            }
         }

         while(true) {
            try {
               if (!this.queuedOutputBuffers.isEmpty()) {
                  ((OutputBuffer)this.queuedOutputBuffers.removeFirst()).release();
                  continue;
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            continue;
         }
      }
   }

   public final void queueInputBuffer(DecoderInputBuffer var1) throws Exception {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var3;
         label133: {
            label132: {
               try {
                  this.maybeThrowException();
                  if (var1 == this.dequeuedInputBuffer) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var3 = false;
               break label133;
            }

            var3 = true;
         }

         label126:
         try {
            Assertions.checkArgument(var3);
            this.queuedInputBuffers.addLast(var1);
            this.maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   protected void releaseOutputBuffer(OutputBuffer param1) {
      // $FF: Couldn't be decompiled
   }

   protected final void setInitialInputBufferSize(int var1) {
      int var2 = this.availableInputBufferCount;
      int var3 = this.availableInputBuffers.length;
      int var4 = 0;
      boolean var5;
      if (var2 == var3) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkState(var5);
      DecoderInputBuffer[] var6 = this.availableInputBuffers;

      for(var2 = var6.length; var4 < var2; ++var4) {
         var6[var4].ensureSpaceForWrite(var1);
      }

   }
}
