package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.metadata.emsg.EventMessageEncoder;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.dash.manifest.EventStream;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class EventSampleStream implements SampleStream {
   private int currentIndex;
   private final EventMessageEncoder eventMessageEncoder;
   private EventStream eventStream;
   private boolean eventStreamAppendable;
   private long[] eventTimesUs;
   private boolean isFormatSentDownstream;
   private long pendingSeekPositionUs;
   private final Format upstreamFormat;

   public EventSampleStream(EventStream var1, Format var2, boolean var3) {
      this.upstreamFormat = var2;
      this.eventStream = var1;
      this.eventMessageEncoder = new EventMessageEncoder();
      this.pendingSeekPositionUs = -9223372036854775807L;
      this.eventTimesUs = var1.presentationTimesUs;
      this.updateEventStream(var1, var3);
   }

   public String eventStreamId() {
      return this.eventStream.id();
   }

   public boolean isReady() {
      return true;
   }

   public void maybeThrowError() throws IOException {
   }

   public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
      if (!var3 && this.isFormatSentDownstream) {
         int var4 = this.currentIndex;
         if (var4 == this.eventTimesUs.length) {
            if (!this.eventStreamAppendable) {
               var2.setFlags(4);
               return -4;
            } else {
               return -3;
            }
         } else {
            this.currentIndex = var4 + 1;
            EventMessageEncoder var5 = this.eventMessageEncoder;
            EventStream var6 = this.eventStream;
            byte[] var7 = var5.encode(var6.events[var4], var6.timescale);
            if (var7 != null) {
               var2.ensureSpaceForWrite(var7.length);
               var2.setFlags(1);
               var2.data.put(var7);
               var2.timeUs = this.eventTimesUs[var4];
               return -4;
            } else {
               return -3;
            }
         }
      } else {
         var1.format = this.upstreamFormat;
         this.isFormatSentDownstream = true;
         return -5;
      }
   }

   public void seekToUs(long var1) {
      long[] var3 = this.eventTimesUs;
      boolean var4 = false;
      this.currentIndex = Util.binarySearchCeil(var3, var1, true, false);
      boolean var5 = var4;
      if (this.eventStreamAppendable) {
         var5 = var4;
         if (this.currentIndex == this.eventTimesUs.length) {
            var5 = true;
         }
      }

      if (!var5) {
         var1 = -9223372036854775807L;
      }

      this.pendingSeekPositionUs = var1;
   }

   public int skipData(long var1) {
      int var3 = Math.max(this.currentIndex, Util.binarySearchCeil(this.eventTimesUs, var1, true, false));
      int var4 = this.currentIndex;
      this.currentIndex = var3;
      return var3 - var4;
   }

   public void updateEventStream(EventStream var1, boolean var2) {
      int var3 = this.currentIndex;
      long var4;
      if (var3 == 0) {
         var4 = -9223372036854775807L;
      } else {
         var4 = this.eventTimesUs[var3 - 1];
      }

      this.eventStreamAppendable = var2;
      this.eventStream = var1;
      this.eventTimesUs = var1.presentationTimesUs;
      long var6 = this.pendingSeekPositionUs;
      if (var6 != -9223372036854775807L) {
         this.seekToUs(var6);
      } else if (var4 != -9223372036854775807L) {
         this.currentIndex = Util.binarySearchCeil(this.eventTimesUs, var4, false, false);
      }

   }
}
