package com.google.android.exoplayer2.source;

public abstract class DefaultMediaSourceEventListener implements MediaSourceEventListener {
   public void onDownstreamFormatChanged(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
   }

   public void onLoadCanceled(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
   }

   public void onLoadCompleted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
   }

   public void onLoadStarted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
   }

   public void onMediaPeriodCreated(int var1, MediaSource.MediaPeriodId var2) {
   }

   public void onMediaPeriodReleased(int var1, MediaSource.MediaPeriodId var2) {
   }

   public void onReadingStarted(int var1, MediaSource.MediaPeriodId var2) {
   }

   public void onUpstreamDiscarded(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
   }
}
