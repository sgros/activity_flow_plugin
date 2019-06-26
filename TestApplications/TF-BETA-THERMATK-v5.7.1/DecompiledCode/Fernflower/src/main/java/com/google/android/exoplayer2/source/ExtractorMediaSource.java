package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

public final class ExtractorMediaSource extends BaseMediaSource implements ExtractorMediaPeriod.Listener {
   private final int continueLoadingCheckIntervalBytes;
   private final String customCacheKey;
   private final DataSource.Factory dataSourceFactory;
   private final ExtractorsFactory extractorsFactory;
   private final LoadErrorHandlingPolicy loadableLoadErrorHandlingPolicy;
   private final Object tag;
   private long timelineDurationUs;
   private boolean timelineIsSeekable;
   private TransferListener transferListener;
   private final Uri uri;

   @Deprecated
   public ExtractorMediaSource(Uri var1, DataSource.Factory var2, ExtractorsFactory var3, Handler var4, ExtractorMediaSource.EventListener var5) {
      this(var1, var2, var3, var4, var5, (String)null);
   }

   @Deprecated
   public ExtractorMediaSource(Uri var1, DataSource.Factory var2, ExtractorsFactory var3, Handler var4, ExtractorMediaSource.EventListener var5, String var6) {
      this(var1, var2, var3, var4, var5, var6, 1048576);
   }

   @Deprecated
   public ExtractorMediaSource(Uri var1, DataSource.Factory var2, ExtractorsFactory var3, Handler var4, ExtractorMediaSource.EventListener var5, String var6, int var7) {
      this(var1, var2, var3, new DefaultLoadErrorHandlingPolicy(), var6, var7, (Object)null);
      if (var5 != null && var4 != null) {
         this.addEventListener(var4, new ExtractorMediaSource.EventListenerWrapper(var5));
      }

   }

   private ExtractorMediaSource(Uri var1, DataSource.Factory var2, ExtractorsFactory var3, LoadErrorHandlingPolicy var4, String var5, int var6, Object var7) {
      this.uri = var1;
      this.dataSourceFactory = var2;
      this.extractorsFactory = var3;
      this.loadableLoadErrorHandlingPolicy = var4;
      this.customCacheKey = var5;
      this.continueLoadingCheckIntervalBytes = var6;
      this.timelineDurationUs = -9223372036854775807L;
      this.tag = var7;
   }

   private void notifySourceInfoRefreshed(long var1, boolean var3) {
      this.timelineDurationUs = var1;
      this.timelineIsSeekable = var3;
      this.refreshSourceInfo(new SinglePeriodTimeline(this.timelineDurationUs, this.timelineIsSeekable, false, this.tag), (Object)null);
   }

   public MediaPeriod createPeriod(MediaSource.MediaPeriodId var1, Allocator var2, long var3) {
      DataSource var5 = this.dataSourceFactory.createDataSource();
      TransferListener var6 = this.transferListener;
      if (var6 != null) {
         var5.addTransferListener(var6);
      }

      return new ExtractorMediaPeriod(this.uri, var5, this.extractorsFactory.createExtractors(), this.loadableLoadErrorHandlingPolicy, this.createEventDispatcher(var1), this, var2, this.customCacheKey, this.continueLoadingCheckIntervalBytes);
   }

   public void maybeThrowSourceInfoRefreshError() throws IOException {
   }

   public void onSourceInfoRefreshed(long var1, boolean var3) {
      long var4 = var1;
      if (var1 == -9223372036854775807L) {
         var4 = this.timelineDurationUs;
      }

      if (this.timelineDurationUs != var4 || this.timelineIsSeekable != var3) {
         this.notifySourceInfoRefreshed(var4, var3);
      }
   }

   public void prepareSourceInternal(TransferListener var1) {
      this.transferListener = var1;
      this.notifySourceInfoRefreshed(this.timelineDurationUs, this.timelineIsSeekable);
   }

   public void releasePeriod(MediaPeriod var1) {
      ((ExtractorMediaPeriod)var1).release();
   }

   public void releaseSourceInternal() {
   }

   @Deprecated
   public interface EventListener {
      void onLoadError(IOException var1);
   }

   @Deprecated
   private static final class EventListenerWrapper extends DefaultMediaSourceEventListener {
      private final ExtractorMediaSource.EventListener eventListener;

      public EventListenerWrapper(ExtractorMediaSource.EventListener var1) {
         Assertions.checkNotNull(var1);
         this.eventListener = (ExtractorMediaSource.EventListener)var1;
      }

      public void onLoadError(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4, IOException var5, boolean var6) {
         this.eventListener.onLoadError(var5);
      }
   }
}
