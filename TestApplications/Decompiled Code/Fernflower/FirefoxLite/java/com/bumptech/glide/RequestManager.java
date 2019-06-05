package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Iterator;

public class RequestManager implements LifecycleListener {
   private static final RequestOptions DECODE_TYPE_BITMAP = RequestOptions.decodeTypeOf(Bitmap.class).lock();
   private static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
   private final Runnable addSelfToLifecycle;
   private final ConnectivityMonitor connectivityMonitor;
   protected final Glide glide;
   final Lifecycle lifecycle;
   private final Handler mainHandler;
   private RequestOptions requestOptions;
   private final RequestTracker requestTracker;
   private final TargetTracker targetTracker;
   private final RequestManagerTreeNode treeNode;

   static {
      DOWNLOAD_ONLY_OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
   }

   public RequestManager(Glide var1, Lifecycle var2, RequestManagerTreeNode var3) {
      this(var1, var2, var3, new RequestTracker(), var1.getConnectivityMonitorFactory());
   }

   RequestManager(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, RequestTracker var4, ConnectivityMonitorFactory var5) {
      this.targetTracker = new TargetTracker();
      this.addSelfToLifecycle = new Runnable() {
         public void run() {
            RequestManager.this.lifecycle.addListener(RequestManager.this);
         }
      };
      this.mainHandler = new Handler(Looper.getMainLooper());
      this.glide = var1;
      this.lifecycle = var2;
      this.treeNode = var3;
      this.requestTracker = var4;
      this.connectivityMonitor = var5.build(var1.getGlideContext().getBaseContext(), new RequestManager.RequestManagerConnectivityListener(var4));
      if (Util.isOnBackgroundThread()) {
         this.mainHandler.post(this.addSelfToLifecycle);
      } else {
         var2.addListener(this);
      }

      var2.addListener(this.connectivityMonitor);
      this.setRequestOptions(var1.getGlideContext().getDefaultRequestOptions());
      var1.registerRequestManager(this);
   }

   private void untrackOrDelegate(Target var1) {
      if (!this.untrack(var1)) {
         this.glide.removeFromManagers(var1);
      }

   }

   public RequestBuilder as(Class var1) {
      return new RequestBuilder(this.glide, this, var1);
   }

   public RequestBuilder asBitmap() {
      return this.as(Bitmap.class).apply(DECODE_TYPE_BITMAP);
   }

   public RequestBuilder asDrawable() {
      return this.as(Drawable.class);
   }

   public void clear(final Target var1) {
      if (var1 != null) {
         if (Util.isOnMainThread()) {
            this.untrackOrDelegate(var1);
         } else {
            this.mainHandler.post(new Runnable() {
               public void run() {
                  RequestManager.this.clear(var1);
               }
            });
         }

      }
   }

   RequestOptions getDefaultRequestOptions() {
      return this.requestOptions;
   }

   TransitionOptions getDefaultTransitionOptions(Class var1) {
      return this.glide.getGlideContext().getDefaultTransitionOptions(var1);
   }

   public RequestBuilder load(Object var1) {
      return this.asDrawable().load(var1);
   }

   public void onDestroy() {
      this.targetTracker.onDestroy();
      Iterator var1 = this.targetTracker.getAll().iterator();

      while(var1.hasNext()) {
         this.clear((Target)var1.next());
      }

      this.targetTracker.clear();
      this.requestTracker.clearRequests();
      this.lifecycle.removeListener(this);
      this.lifecycle.removeListener(this.connectivityMonitor);
      this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
      this.glide.unregisterRequestManager(this);
   }

   public void onStart() {
      this.resumeRequests();
      this.targetTracker.onStart();
   }

   public void onStop() {
      this.pauseRequests();
      this.targetTracker.onStop();
   }

   public void pauseRequests() {
      Util.assertMainThread();
      this.requestTracker.pauseRequests();
   }

   public void resumeRequests() {
      Util.assertMainThread();
      this.requestTracker.resumeRequests();
   }

   protected void setRequestOptions(RequestOptions var1) {
      this.requestOptions = var1.clone().autoClone();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("{tracker=");
      var1.append(this.requestTracker);
      var1.append(", treeNode=");
      var1.append(this.treeNode);
      var1.append("}");
      return var1.toString();
   }

   void track(Target var1, Request var2) {
      this.targetTracker.track(var1);
      this.requestTracker.runRequest(var2);
   }

   boolean untrack(Target var1) {
      Request var2 = var1.getRequest();
      if (var2 == null) {
         return true;
      } else if (this.requestTracker.clearRemoveAndRecycle(var2)) {
         this.targetTracker.untrack(var1);
         var1.setRequest((Request)null);
         return true;
      } else {
         return false;
      }
   }

   private static class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
      private final RequestTracker requestTracker;

      public RequestManagerConnectivityListener(RequestTracker var1) {
         this.requestTracker = var1;
      }

      public void onConnectivityChanged(boolean var1) {
         if (var1) {
            this.requestTracker.restartRequests();
         }

      }
   }
}
