package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public interface MediaSourceEventListener {
   void onDownstreamFormatChanged(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3);

   void onLoadCanceled(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4);

   void onLoadCompleted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4);

   void onLoadError(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4, IOException var5, boolean var6);

   void onLoadStarted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4);

   void onMediaPeriodCreated(int var1, MediaSource.MediaPeriodId var2);

   void onMediaPeriodReleased(int var1, MediaSource.MediaPeriodId var2);

   void onReadingStarted(int var1, MediaSource.MediaPeriodId var2);

   void onUpstreamDiscarded(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3);

   public static final class EventDispatcher {
      private final CopyOnWriteArrayList listenerAndHandlers;
      public final MediaSource.MediaPeriodId mediaPeriodId;
      private final long mediaTimeOffsetMs;
      public final int windowIndex;

      public EventDispatcher() {
         this(new CopyOnWriteArrayList(), 0, (MediaSource.MediaPeriodId)null, 0L);
      }

      private EventDispatcher(CopyOnWriteArrayList var1, int var2, MediaSource.MediaPeriodId var3, long var4) {
         this.listenerAndHandlers = var1;
         this.windowIndex = var2;
         this.mediaPeriodId = var3;
         this.mediaTimeOffsetMs = var4;
      }

      private long adjustMediaTime(long var1) {
         long var3 = C.usToMs(var1);
         var1 = -9223372036854775807L;
         if (var3 != -9223372036854775807L) {
            var1 = this.mediaTimeOffsetMs + var3;
         }

         return var1;
      }

      private void postOrRun(Handler var1, Runnable var2) {
         if (var1.getLooper() == Looper.myLooper()) {
            var2.run();
         } else {
            var1.post(var2);
         }

      }

      public void addEventListener(Handler var1, MediaSourceEventListener var2) {
         boolean var3;
         if (var1 != null && var2 != null) {
            var3 = true;
         } else {
            var3 = false;
         }

         Assertions.checkArgument(var3);
         this.listenerAndHandlers.add(new MediaSourceEventListener.EventDispatcher.ListenerAndHandler(var1, var2));
      }

      public void downstreamFormatChanged(int var1, Format var2, int var3, Object var4, long var5) {
         this.downstreamFormatChanged(new MediaSourceEventListener.MediaLoadData(1, var1, var2, var3, var4, this.adjustMediaTime(var5), -9223372036854775807L));
      }

      public void downstreamFormatChanged(MediaSourceEventListener.MediaLoadData var1) {
         Iterator var2 = this.listenerAndHandlers.iterator();

         while(var2.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var3 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var2.next();
            MediaSourceEventListener var4 = var3.listener;
            this.postOrRun(var3.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$ES4FdQzWtupQEe6zuV_1M9_f9xU(this, var4, var1));
         }

      }

      // $FF: synthetic method
      public void lambda$downstreamFormatChanged$8$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSourceEventListener.MediaLoadData var2) {
         var1.onDownstreamFormatChanged(this.windowIndex, this.mediaPeriodId, var2);
      }

      // $FF: synthetic method
      public void lambda$loadCanceled$4$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
         var1.onLoadCanceled(this.windowIndex, this.mediaPeriodId, var2, var3);
      }

      // $FF: synthetic method
      public void lambda$loadCompleted$3$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
         var1.onLoadCompleted(this.windowIndex, this.mediaPeriodId, var2, var3);
      }

      // $FF: synthetic method
      public void lambda$loadError$5$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3, IOException var4, boolean var5) {
         var1.onLoadError(this.windowIndex, this.mediaPeriodId, var2, var3, var4, var5);
      }

      // $FF: synthetic method
      public void lambda$loadStarted$2$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
         var1.onLoadStarted(this.windowIndex, this.mediaPeriodId, var2, var3);
      }

      // $FF: synthetic method
      public void lambda$mediaPeriodCreated$0$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSource.MediaPeriodId var2) {
         var1.onMediaPeriodCreated(this.windowIndex, var2);
      }

      // $FF: synthetic method
      public void lambda$mediaPeriodReleased$1$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSource.MediaPeriodId var2) {
         var1.onMediaPeriodReleased(this.windowIndex, var2);
      }

      // $FF: synthetic method
      public void lambda$readingStarted$6$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSource.MediaPeriodId var2) {
         var1.onReadingStarted(this.windowIndex, var2);
      }

      // $FF: synthetic method
      public void lambda$upstreamDiscarded$7$MediaSourceEventListener$EventDispatcher(MediaSourceEventListener var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
         var1.onUpstreamDiscarded(this.windowIndex, var2, var3);
      }

      public void loadCanceled(MediaSourceEventListener.LoadEventInfo var1, MediaSourceEventListener.MediaLoadData var2) {
         Iterator var3 = this.listenerAndHandlers.iterator();

         while(var3.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var4 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var3.next();
            MediaSourceEventListener var5 = var4.listener;
            this.postOrRun(var4.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$1_VoN1d1C8yHbFOrB_mXtUwAn3M(this, var5, var1, var2));
         }

      }

      public void loadCanceled(DataSpec var1, Uri var2, Map var3, int var4, int var5, Format var6, int var7, Object var8, long var9, long var11, long var13, long var15, long var17) {
         this.loadCanceled(new MediaSourceEventListener.LoadEventInfo(var1, var2, var3, var13, var15, var17), new MediaSourceEventListener.MediaLoadData(var4, var5, var6, var7, var8, this.adjustMediaTime(var9), this.adjustMediaTime(var11)));
      }

      public void loadCanceled(DataSpec var1, Uri var2, Map var3, int var4, long var5, long var7, long var9) {
         this.loadCanceled(var1, var2, var3, var4, -1, (Format)null, 0, (Object)null, -9223372036854775807L, -9223372036854775807L, var5, var7, var9);
      }

      public void loadCompleted(MediaSourceEventListener.LoadEventInfo var1, MediaSourceEventListener.MediaLoadData var2) {
         Iterator var3 = this.listenerAndHandlers.iterator();

         while(var3.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var4 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var3.next();
            MediaSourceEventListener var5 = var4.listener;
            this.postOrRun(var4.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$IejPnkXyHgj2V1iyO1dqtBKfihI(this, var5, var1, var2));
         }

      }

      public void loadCompleted(DataSpec var1, Uri var2, Map var3, int var4, int var5, Format var6, int var7, Object var8, long var9, long var11, long var13, long var15, long var17) {
         this.loadCompleted(new MediaSourceEventListener.LoadEventInfo(var1, var2, var3, var13, var15, var17), new MediaSourceEventListener.MediaLoadData(var4, var5, var6, var7, var8, this.adjustMediaTime(var9), this.adjustMediaTime(var11)));
      }

      public void loadCompleted(DataSpec var1, Uri var2, Map var3, int var4, long var5, long var7, long var9) {
         this.loadCompleted(var1, var2, var3, var4, -1, (Format)null, 0, (Object)null, -9223372036854775807L, -9223372036854775807L, var5, var7, var9);
      }

      public void loadError(MediaSourceEventListener.LoadEventInfo var1, MediaSourceEventListener.MediaLoadData var2, IOException var3, boolean var4) {
         Iterator var5 = this.listenerAndHandlers.iterator();

         while(var5.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var6 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var5.next();
            MediaSourceEventListener var7 = var6.listener;
            this.postOrRun(var6.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$0X_TAsNqR4TUW1yA_ZD1_p3oT84(this, var7, var1, var2, var3, var4));
         }

      }

      public void loadError(DataSpec var1, Uri var2, Map var3, int var4, int var5, Format var6, int var7, Object var8, long var9, long var11, long var13, long var15, long var17, IOException var19, boolean var20) {
         this.loadError(new MediaSourceEventListener.LoadEventInfo(var1, var2, var3, var13, var15, var17), new MediaSourceEventListener.MediaLoadData(var4, var5, var6, var7, var8, this.adjustMediaTime(var9), this.adjustMediaTime(var11)), var19, var20);
      }

      public void loadError(DataSpec var1, Uri var2, Map var3, int var4, long var5, long var7, long var9, IOException var11, boolean var12) {
         this.loadError(var1, var2, var3, var4, -1, (Format)null, 0, (Object)null, -9223372036854775807L, -9223372036854775807L, var5, var7, var9, var11, var12);
      }

      public void loadStarted(MediaSourceEventListener.LoadEventInfo var1, MediaSourceEventListener.MediaLoadData var2) {
         Iterator var3 = this.listenerAndHandlers.iterator();

         while(var3.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var4 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var3.next();
            MediaSourceEventListener var5 = var4.listener;
            this.postOrRun(var4.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$WQKVpIh5ilpOizOGmbnyUThugMU(this, var5, var1, var2));
         }

      }

      public void loadStarted(DataSpec var1, int var2, int var3, Format var4, int var5, Object var6, long var7, long var9, long var11) {
         this.loadStarted(new MediaSourceEventListener.LoadEventInfo(var1, var1.uri, Collections.emptyMap(), var11, 0L, 0L), new MediaSourceEventListener.MediaLoadData(var2, var3, var4, var5, var6, this.adjustMediaTime(var7), this.adjustMediaTime(var9)));
      }

      public void loadStarted(DataSpec var1, int var2, long var3) {
         this.loadStarted(var1, var2, -1, (Format)null, 0, (Object)null, -9223372036854775807L, -9223372036854775807L, var3);
      }

      public void mediaPeriodCreated() {
         MediaSource.MediaPeriodId var1 = this.mediaPeriodId;
         Assertions.checkNotNull(var1);
         MediaSource.MediaPeriodId var2 = (MediaSource.MediaPeriodId)var1;
         Iterator var3 = this.listenerAndHandlers.iterator();

         while(var3.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var4 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var3.next();
            MediaSourceEventListener var5 = var4.listener;
            this.postOrRun(var4.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$N_EOPAK5UK0__YMNjezq7UM3UNI(this, var5, var2));
         }

      }

      public void mediaPeriodReleased() {
         MediaSource.MediaPeriodId var1 = this.mediaPeriodId;
         Assertions.checkNotNull(var1);
         MediaSource.MediaPeriodId var2 = (MediaSource.MediaPeriodId)var1;
         Iterator var5 = this.listenerAndHandlers.iterator();

         while(var5.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var3 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var5.next();
            MediaSourceEventListener var4 = var3.listener;
            this.postOrRun(var3.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$zyck4ebRbqvR6eQIjdzRcIBkRbI(this, var4, var2));
         }

      }

      public void readingStarted() {
         MediaSource.MediaPeriodId var1 = this.mediaPeriodId;
         Assertions.checkNotNull(var1);
         var1 = (MediaSource.MediaPeriodId)var1;
         Iterator var2 = this.listenerAndHandlers.iterator();

         while(var2.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var3 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var2.next();
            MediaSourceEventListener var4 = var3.listener;
            this.postOrRun(var3.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$PV8wmqGm7vRMJNlt__V3zhXfxiE(this, var4, var1));
         }

      }

      public void removeEventListener(MediaSourceEventListener var1) {
         Iterator var2 = this.listenerAndHandlers.iterator();

         while(var2.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var3 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var2.next();
            if (var3.listener == var1) {
               this.listenerAndHandlers.remove(var3);
            }
         }

      }

      public void upstreamDiscarded(int var1, long var2, long var4) {
         this.upstreamDiscarded(new MediaSourceEventListener.MediaLoadData(1, var1, (Format)null, 3, (Object)null, this.adjustMediaTime(var2), this.adjustMediaTime(var4)));
      }

      public void upstreamDiscarded(MediaSourceEventListener.MediaLoadData var1) {
         MediaSource.MediaPeriodId var2 = this.mediaPeriodId;
         Assertions.checkNotNull(var2);
         MediaSource.MediaPeriodId var3 = (MediaSource.MediaPeriodId)var2;
         Iterator var4 = this.listenerAndHandlers.iterator();

         while(var4.hasNext()) {
            MediaSourceEventListener.EventDispatcher.ListenerAndHandler var5 = (MediaSourceEventListener.EventDispatcher.ListenerAndHandler)var4.next();
            MediaSourceEventListener var6 = var5.listener;
            this.postOrRun(var5.handler, new _$$Lambda$MediaSourceEventListener$EventDispatcher$BtPa14lQQTv1oUeMy_9QaCysWHY(this, var6, var3, var1));
         }

      }

      public MediaSourceEventListener.EventDispatcher withParameters(int var1, MediaSource.MediaPeriodId var2, long var3) {
         return new MediaSourceEventListener.EventDispatcher(this.listenerAndHandlers, var1, var2, var3);
      }

      private static final class ListenerAndHandler {
         public final Handler handler;
         public final MediaSourceEventListener listener;

         public ListenerAndHandler(Handler var1, MediaSourceEventListener var2) {
            this.handler = var1;
            this.listener = var2;
         }
      }
   }

   public static final class LoadEventInfo {
      public final long bytesLoaded;
      public final DataSpec dataSpec;
      public final long elapsedRealtimeMs;
      public final long loadDurationMs;
      public final Map responseHeaders;
      public final Uri uri;

      public LoadEventInfo(DataSpec var1, Uri var2, Map var3, long var4, long var6, long var8) {
         this.dataSpec = var1;
         this.uri = var2;
         this.responseHeaders = var3;
         this.elapsedRealtimeMs = var4;
         this.loadDurationMs = var6;
         this.bytesLoaded = var8;
      }
   }

   public static final class MediaLoadData {
      public final int dataType;
      public final long mediaEndTimeMs;
      public final long mediaStartTimeMs;
      public final Format trackFormat;
      public final Object trackSelectionData;
      public final int trackSelectionReason;
      public final int trackType;

      public MediaLoadData(int var1, int var2, Format var3, int var4, Object var5, long var6, long var8) {
         this.dataType = var1;
         this.trackType = var2;
         this.trackFormat = var3;
         this.trackSelectionReason = var4;
         this.trackSelectionData = var5;
         this.mediaStartTimeMs = var6;
         this.mediaEndTimeMs = var8;
      }
   }
}
