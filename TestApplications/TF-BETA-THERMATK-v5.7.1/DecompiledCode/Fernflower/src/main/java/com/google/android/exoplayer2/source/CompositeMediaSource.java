package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public abstract class CompositeMediaSource extends BaseMediaSource {
   private final HashMap childSources = new HashMap();
   private Handler eventHandler;
   private TransferListener mediaTransferListener;

   protected CompositeMediaSource() {
   }

   protected abstract MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(Object var1, MediaSource.MediaPeriodId var2);

   protected long getMediaTimeForChildMediaTime(Object var1, long var2) {
      return var2;
   }

   protected int getWindowIndexForChildWindowIndex(Object var1, int var2) {
      return var2;
   }

   // $FF: synthetic method
   public void lambda$prepareChildSource$0$CompositeMediaSource(Object var1, MediaSource var2, Timeline var3, Object var4) {
      this.onChildSourceInfoRefreshed(var1, var2, var3, var4);
   }

   public void maybeThrowSourceInfoRefreshError() throws IOException {
      Iterator var1 = this.childSources.values().iterator();

      while(var1.hasNext()) {
         ((CompositeMediaSource.MediaSourceAndListener)var1.next()).mediaSource.maybeThrowSourceInfoRefreshError();
      }

   }

   protected abstract void onChildSourceInfoRefreshed(Object var1, MediaSource var2, Timeline var3, Object var4);

   protected final void prepareChildSource(Object var1, MediaSource var2) {
      Assertions.checkArgument(this.childSources.containsKey(var1) ^ true);
      _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco var3 = new _$$Lambda$CompositeMediaSource$ahAPO18YbnzL6kKRAWdp4FR_Vco(this, var1);
      CompositeMediaSource.ForwardingEventListener var4 = new CompositeMediaSource.ForwardingEventListener(var1);
      this.childSources.put(var1, new CompositeMediaSource.MediaSourceAndListener(var2, var3, var4));
      Handler var5 = this.eventHandler;
      Assertions.checkNotNull(var5);
      var2.addEventListener((Handler)var5, var4);
      var2.prepareSource(var3, this.mediaTransferListener);
   }

   public void prepareSourceInternal(TransferListener var1) {
      this.mediaTransferListener = var1;
      this.eventHandler = new Handler();
   }

   public void releaseSourceInternal() {
      Iterator var1 = this.childSources.values().iterator();

      while(var1.hasNext()) {
         CompositeMediaSource.MediaSourceAndListener var2 = (CompositeMediaSource.MediaSourceAndListener)var1.next();
         var2.mediaSource.releaseSource(var2.listener);
         var2.mediaSource.removeEventListener(var2.eventListener);
      }

      this.childSources.clear();
   }

   private final class ForwardingEventListener implements MediaSourceEventListener {
      private MediaSourceEventListener.EventDispatcher eventDispatcher = CompositeMediaSource.this.createEventDispatcher((MediaSource.MediaPeriodId)null);
      private final Object id;

      public ForwardingEventListener(Object var2) {
         this.id = var2;
      }

      private boolean maybeUpdateEventDispatcher(int var1, MediaSource.MediaPeriodId var2) {
         if (var2 != null) {
            MediaSource.MediaPeriodId var3 = CompositeMediaSource.this.getMediaPeriodIdForChildMediaPeriodId(this.id, var2);
            var2 = var3;
            if (var3 == null) {
               return false;
            }
         } else {
            var2 = null;
         }

         CompositeMediaSource.this.getWindowIndexForChildWindowIndex(this.id, var1);
         MediaSourceEventListener.EventDispatcher var4 = this.eventDispatcher;
         if (var4.windowIndex != var1 || !Util.areEqual(var4.mediaPeriodId, var2)) {
            this.eventDispatcher = CompositeMediaSource.this.createEventDispatcher(var1, var2, 0L);
         }

         return true;
      }

      private MediaSourceEventListener.MediaLoadData maybeUpdateMediaLoadData(MediaSourceEventListener.MediaLoadData var1) {
         CompositeMediaSource var2 = CompositeMediaSource.this;
         Object var3 = this.id;
         long var4 = var1.mediaStartTimeMs;
         var2.getMediaTimeForChildMediaTime(var3, var4);
         CompositeMediaSource var9 = CompositeMediaSource.this;
         Object var8 = this.id;
         long var6 = var1.mediaEndTimeMs;
         var9.getMediaTimeForChildMediaTime(var8, var6);
         return var4 == var1.mediaStartTimeMs && var6 == var1.mediaEndTimeMs ? var1 : new MediaSourceEventListener.MediaLoadData(var1.dataType, var1.trackType, var1.trackFormat, var1.trackSelectionReason, var1.trackSelectionData, var4, var6);
      }

      public void onDownstreamFormatChanged(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.downstreamFormatChanged(this.maybeUpdateMediaLoadData(var3));
         }

      }

      public void onLoadCanceled(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.loadCanceled(var3, this.maybeUpdateMediaLoadData(var4));
         }

      }

      public void onLoadCompleted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.loadCompleted(var3, this.maybeUpdateMediaLoadData(var4));
         }

      }

      public void onLoadError(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4, IOException var5, boolean var6) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.loadError(var3, this.maybeUpdateMediaLoadData(var4), var5, var6);
         }

      }

      public void onLoadStarted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.loadStarted(var3, this.maybeUpdateMediaLoadData(var4));
         }

      }

      public void onMediaPeriodCreated(int var1, MediaSource.MediaPeriodId var2) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.mediaPeriodCreated();
         }

      }

      public void onMediaPeriodReleased(int var1, MediaSource.MediaPeriodId var2) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.mediaPeriodReleased();
         }

      }

      public void onReadingStarted(int var1, MediaSource.MediaPeriodId var2) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.readingStarted();
         }

      }

      public void onUpstreamDiscarded(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
         if (this.maybeUpdateEventDispatcher(var1, var2)) {
            this.eventDispatcher.upstreamDiscarded(this.maybeUpdateMediaLoadData(var3));
         }

      }
   }

   private static final class MediaSourceAndListener {
      public final MediaSourceEventListener eventListener;
      public final MediaSource.SourceInfoRefreshListener listener;
      public final MediaSource mediaSource;

      public MediaSourceAndListener(MediaSource var1, MediaSource.SourceInfoRefreshListener var2, MediaSourceEventListener var3) {
         this.mediaSource = var1;
         this.listener = var2;
         this.eventListener = var3;
      }
   }
}
