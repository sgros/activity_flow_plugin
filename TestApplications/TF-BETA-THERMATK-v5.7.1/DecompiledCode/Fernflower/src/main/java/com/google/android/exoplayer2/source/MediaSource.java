package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;

public interface MediaSource {
   void addEventListener(Handler var1, MediaSourceEventListener var2);

   MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3);

   void maybeThrowSourceInfoRefreshError() throws IOException;

   void prepareSource(MediaSource.SourceInfoRefreshListener var1, TransferListener var2);

   void releasePeriod(MediaPeriod var1);

   void releaseSource(MediaSource.SourceInfoRefreshListener var1);

   void removeEventListener(MediaSourceEventListener var1);

   public static final class MediaPeriodId {
      public final int adGroupIndex;
      public final int adIndexInAdGroup;
      public final long endPositionUs;
      public final Object periodUid;
      public final long windowSequenceNumber;

      public MediaPeriodId(Object var1) {
         this(var1, -1L);
      }

      public MediaPeriodId(Object var1, int var2, int var3, long var4) {
         this(var1, var2, var3, var4, -9223372036854775807L);
      }

      private MediaPeriodId(Object var1, int var2, int var3, long var4, long var6) {
         this.periodUid = var1;
         this.adGroupIndex = var2;
         this.adIndexInAdGroup = var3;
         this.windowSequenceNumber = var4;
         this.endPositionUs = var6;
      }

      public MediaPeriodId(Object var1, long var2) {
         this(var1, -1, -1, var2, -9223372036854775807L);
      }

      public MediaPeriodId(Object var1, long var2, long var4) {
         this(var1, -1, -1, var2, var4);
      }

      public MediaSource.MediaPeriodId copyWithPeriodUid(Object var1) {
         MediaSource.MediaPeriodId var2;
         if (this.periodUid.equals(var1)) {
            var2 = this;
         } else {
            var2 = new MediaSource.MediaPeriodId(var1, this.adGroupIndex, this.adIndexInAdGroup, this.windowSequenceNumber, this.endPositionUs);
         }

         return var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && MediaSource.MediaPeriodId.class == var1.getClass()) {
            MediaSource.MediaPeriodId var3 = (MediaSource.MediaPeriodId)var1;
            if (!this.periodUid.equals(var3.periodUid) || this.adGroupIndex != var3.adGroupIndex || this.adIndexInAdGroup != var3.adIndexInAdGroup || this.windowSequenceNumber != var3.windowSequenceNumber || this.endPositionUs != var3.endPositionUs) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ((((527 + this.periodUid.hashCode()) * 31 + this.adGroupIndex) * 31 + this.adIndexInAdGroup) * 31 + (int)this.windowSequenceNumber) * 31 + (int)this.endPositionUs;
      }

      public boolean isAd() {
         boolean var1;
         if (this.adGroupIndex != -1) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public interface SourceInfoRefreshListener {
      void onSourceInfoRefreshed(MediaSource var1, Timeline var2, Object var3);
   }
}
