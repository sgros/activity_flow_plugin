package com.google.android.exoplayer2.source;

import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseMediaSource implements MediaSource {
   private final MediaSourceEventListener.EventDispatcher eventDispatcher = new MediaSourceEventListener.EventDispatcher();
   private Looper looper;
   private Object manifest;
   private final ArrayList sourceInfoListeners = new ArrayList(1);
   private Timeline timeline;

   public final void addEventListener(Handler var1, MediaSourceEventListener var2) {
      this.eventDispatcher.addEventListener(var1, var2);
   }

   protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(int var1, MediaSource.MediaPeriodId var2, long var3) {
      return this.eventDispatcher.withParameters(var1, var2, var3);
   }

   protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(MediaSource.MediaPeriodId var1) {
      return this.eventDispatcher.withParameters(0, var1, 0L);
   }

   protected final MediaSourceEventListener.EventDispatcher createEventDispatcher(MediaSource.MediaPeriodId var1, long var2) {
      boolean var4;
      if (var1 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Assertions.checkArgument(var4);
      return this.eventDispatcher.withParameters(0, var1, var2);
   }

   public final void prepareSource(MediaSource.SourceInfoRefreshListener var1, TransferListener var2) {
      Looper var3 = Looper.myLooper();
      Looper var4 = this.looper;
      boolean var5;
      if (var4 != null && var4 != var3) {
         var5 = false;
      } else {
         var5 = true;
      }

      Assertions.checkArgument(var5);
      this.sourceInfoListeners.add(var1);
      if (this.looper == null) {
         this.looper = var3;
         this.prepareSourceInternal(var2);
      } else {
         Timeline var6 = this.timeline;
         if (var6 != null) {
            var1.onSourceInfoRefreshed(this, var6, this.manifest);
         }
      }

   }

   protected abstract void prepareSourceInternal(TransferListener var1);

   protected final void refreshSourceInfo(Timeline var1, Object var2) {
      this.timeline = var1;
      this.manifest = var2;
      Iterator var3 = this.sourceInfoListeners.iterator();

      while(var3.hasNext()) {
         ((MediaSource.SourceInfoRefreshListener)var3.next()).onSourceInfoRefreshed(this, var1, var2);
      }

   }

   public final void releaseSource(MediaSource.SourceInfoRefreshListener var1) {
      this.sourceInfoListeners.remove(var1);
      if (this.sourceInfoListeners.isEmpty()) {
         this.looper = null;
         this.timeline = null;
         this.manifest = null;
         this.releaseSourceInternal();
      }

   }

   protected abstract void releaseSourceInternal();

   public final void removeEventListener(MediaSourceEventListener var1) {
      this.eventDispatcher.removeEventListener(var1);
   }
}
