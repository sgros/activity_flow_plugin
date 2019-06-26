package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayDeque;
import java.util.PriorityQueue;

abstract class CeaDecoder implements SubtitleDecoder {
   private final ArrayDeque availableInputBuffers = new ArrayDeque();
   private final ArrayDeque availableOutputBuffers;
   private CeaDecoder.CeaInputBuffer dequeuedInputBuffer;
   private long playbackPositionUs;
   private long queuedInputBufferCount;
   private final PriorityQueue queuedInputBuffers;

   public CeaDecoder() {
      byte var1 = 0;

      int var2;
      for(var2 = 0; var2 < 10; ++var2) {
         this.availableInputBuffers.add(new CeaDecoder.CeaInputBuffer());
      }

      this.availableOutputBuffers = new ArrayDeque();

      for(var2 = var1; var2 < 2; ++var2) {
         this.availableOutputBuffers.add(new CeaDecoder.CeaOutputBuffer());
      }

      this.queuedInputBuffers = new PriorityQueue();
   }

   private void releaseInputBuffer(CeaDecoder.CeaInputBuffer var1) {
      var1.clear();
      this.availableInputBuffers.add(var1);
   }

   protected abstract Subtitle createSubtitle();

   protected abstract void decode(SubtitleInputBuffer var1);

   public SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
      boolean var1;
      if (this.dequeuedInputBuffer == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkState(var1);
      if (this.availableInputBuffers.isEmpty()) {
         return null;
      } else {
         this.dequeuedInputBuffer = (CeaDecoder.CeaInputBuffer)this.availableInputBuffers.pollFirst();
         return this.dequeuedInputBuffer;
      }
   }

   public SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
      if (this.availableOutputBuffers.isEmpty()) {
         return null;
      } else {
         CeaDecoder.CeaInputBuffer var1;
         for(; !this.queuedInputBuffers.isEmpty() && ((CeaDecoder.CeaInputBuffer)this.queuedInputBuffers.peek()).timeUs <= this.playbackPositionUs; this.releaseInputBuffer(var1)) {
            var1 = (CeaDecoder.CeaInputBuffer)this.queuedInputBuffers.poll();
            SubtitleOutputBuffer var2;
            if (var1.isEndOfStream()) {
               var2 = (SubtitleOutputBuffer)this.availableOutputBuffers.pollFirst();
               var2.addFlag(4);
               this.releaseInputBuffer(var1);
               return var2;
            }

            this.decode(var1);
            if (this.isNewSubtitleDataAvailable()) {
               Subtitle var3 = this.createSubtitle();
               if (!var1.isDecodeOnly()) {
                  var2 = (SubtitleOutputBuffer)this.availableOutputBuffers.pollFirst();
                  var2.setContent(var1.timeUs, var3, Long.MAX_VALUE);
                  this.releaseInputBuffer(var1);
                  return var2;
               }
            }
         }

         return null;
      }
   }

   public void flush() {
      this.queuedInputBufferCount = 0L;
      this.playbackPositionUs = 0L;

      while(!this.queuedInputBuffers.isEmpty()) {
         this.releaseInputBuffer((CeaDecoder.CeaInputBuffer)this.queuedInputBuffers.poll());
      }

      CeaDecoder.CeaInputBuffer var1 = this.dequeuedInputBuffer;
      if (var1 != null) {
         this.releaseInputBuffer(var1);
         this.dequeuedInputBuffer = null;
      }

   }

   protected abstract boolean isNewSubtitleDataAvailable();

   public void queueInputBuffer(SubtitleInputBuffer var1) throws SubtitleDecoderException {
      boolean var2;
      if (var1 == this.dequeuedInputBuffer) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkArgument(var2);
      if (var1.isDecodeOnly()) {
         this.releaseInputBuffer(this.dequeuedInputBuffer);
      } else {
         CeaDecoder.CeaInputBuffer var5 = this.dequeuedInputBuffer;
         long var3 = (long)(this.queuedInputBufferCount++);
         var5.queuedInputBufferCount = var3;
         this.queuedInputBuffers.add(this.dequeuedInputBuffer);
      }

      this.dequeuedInputBuffer = null;
   }

   public void release() {
   }

   protected void releaseOutputBuffer(SubtitleOutputBuffer var1) {
      var1.clear();
      this.availableOutputBuffers.add(var1);
   }

   public void setPositionUs(long var1) {
      this.playbackPositionUs = var1;
   }

   private static final class CeaInputBuffer extends SubtitleInputBuffer implements Comparable {
      private long queuedInputBufferCount;

      private CeaInputBuffer() {
      }

      // $FF: synthetic method
      CeaInputBuffer(Object var1) {
         this();
      }

      public int compareTo(CeaDecoder.CeaInputBuffer var1) {
         boolean var2 = this.isEndOfStream();
         boolean var3 = var1.isEndOfStream();
         byte var4 = 1;
         byte var5 = 1;
         if (var2 != var3) {
            if (!this.isEndOfStream()) {
               var5 = -1;
            }

            return var5;
         } else {
            long var6 = super.timeUs - var1.timeUs;
            long var8 = var6;
            if (var6 == 0L) {
               var6 = this.queuedInputBufferCount - var1.queuedInputBufferCount;
               var8 = var6;
               if (var6 == 0L) {
                  return 0;
               }
            }

            if (var8 > 0L) {
               var5 = var4;
            } else {
               var5 = -1;
            }

            return var5;
         }
      }
   }

   private final class CeaOutputBuffer extends SubtitleOutputBuffer {
      private CeaOutputBuffer() {
      }

      // $FF: synthetic method
      CeaOutputBuffer(Object var2) {
         this();
      }

      public final void release() {
         CeaDecoder.this.releaseOutputBuffer(this);
      }
   }
}
