package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pools;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;

public final class SingleRequest implements Request, ResourceCallback, SizeReadyCallback, FactoryPools.Poolable {
   private static final Pools.Pool POOL = FactoryPools.simple(150, new FactoryPools.Factory() {
      public SingleRequest create() {
         return new SingleRequest();
      }
   });
   private static boolean shouldCallAppCompatResources = true;
   private TransitionFactory animationFactory;
   private Engine engine;
   private Drawable errorDrawable;
   private Drawable fallbackDrawable;
   private GlideContext glideContext;
   private int height;
   private boolean isCallingCallbacks;
   private Engine.LoadStatus loadStatus;
   private Object model;
   private int overrideHeight;
   private int overrideWidth;
   private Drawable placeholderDrawable;
   private Priority priority;
   private RequestCoordinator requestCoordinator;
   private RequestListener requestListener;
   private RequestOptions requestOptions;
   private Resource resource;
   private long startTime;
   private final StateVerifier stateVerifier = StateVerifier.newInstance();
   private SingleRequest.Status status;
   private final String tag = String.valueOf(super.hashCode());
   private Target target;
   private Class transcodeClass;
   private int width;

   SingleRequest() {
   }

   private void assertNotCallingCallbacks() {
      if (this.isCallingCallbacks) {
         throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you must do so, consider posting your into() or clear() calls to the main thread using a Handler instead.");
      }
   }

   private boolean canNotifyStatusChanged() {
      boolean var1;
      if (this.requestCoordinator != null && !this.requestCoordinator.canNotifyStatusChanged(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean canSetResource() {
      boolean var1;
      if (this.requestCoordinator != null && !this.requestCoordinator.canSetImage(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private Drawable getErrorDrawable() {
      if (this.errorDrawable == null) {
         this.errorDrawable = this.requestOptions.getErrorPlaceholder();
         if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
            this.errorDrawable = this.loadDrawable(this.requestOptions.getErrorId());
         }
      }

      return this.errorDrawable;
   }

   private Drawable getFallbackDrawable() {
      if (this.fallbackDrawable == null) {
         this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
         if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
            this.fallbackDrawable = this.loadDrawable(this.requestOptions.getFallbackId());
         }
      }

      return this.fallbackDrawable;
   }

   private Drawable getPlaceholderDrawable() {
      if (this.placeholderDrawable == null) {
         this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
         if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
            this.placeholderDrawable = this.loadDrawable(this.requestOptions.getPlaceholderId());
         }
      }

      return this.placeholderDrawable;
   }

   private void init(GlideContext var1, Object var2, Class var3, RequestOptions var4, int var5, int var6, Priority var7, Target var8, RequestListener var9, RequestCoordinator var10, Engine var11, TransitionFactory var12) {
      this.glideContext = var1;
      this.model = var2;
      this.transcodeClass = var3;
      this.requestOptions = var4;
      this.overrideWidth = var5;
      this.overrideHeight = var6;
      this.priority = var7;
      this.target = var8;
      this.requestListener = var9;
      this.requestCoordinator = var10;
      this.engine = var11;
      this.animationFactory = var12;
      this.status = SingleRequest.Status.PENDING;
   }

   private boolean isFirstReadyResource() {
      boolean var1;
      if (this.requestCoordinator != null && this.requestCoordinator.isAnyResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private Drawable loadDrawable(int var1) {
      return shouldCallAppCompatResources ? this.loadDrawableV7(var1) : this.loadDrawableBase(var1);
   }

   private Drawable loadDrawableBase(int var1) {
      return ResourcesCompat.getDrawable(this.glideContext.getResources(), var1, this.requestOptions.getTheme());
   }

   private Drawable loadDrawableV7(int var1) {
      try {
         Drawable var2 = AppCompatResources.getDrawable(this.glideContext, var1);
         return var2;
      } catch (NoClassDefFoundError var3) {
         shouldCallAppCompatResources = false;
         return this.loadDrawableBase(var1);
      }
   }

   private void logV(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append(" this: ");
      var2.append(this.tag);
      Log.v("Request", var2.toString());
   }

   private static int maybeApplySizeMultiplier(int var0, float var1) {
      if (var0 != Integer.MIN_VALUE) {
         var0 = Math.round(var1 * (float)var0);
      }

      return var0;
   }

   private void notifyLoadSuccess() {
      if (this.requestCoordinator != null) {
         this.requestCoordinator.onRequestSuccess(this);
      }

   }

   public static SingleRequest obtain(GlideContext var0, Object var1, Class var2, RequestOptions var3, int var4, int var5, Priority var6, Target var7, RequestListener var8, RequestCoordinator var9, Engine var10, TransitionFactory var11) {
      SingleRequest var12 = (SingleRequest)POOL.acquire();
      SingleRequest var13 = var12;
      if (var12 == null) {
         var13 = new SingleRequest();
      }

      var13.init(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      return var13;
   }

   private void onLoadFailed(GlideException var1, int var2) {
      this.stateVerifier.throwIfRecycled();
      int var3 = this.glideContext.getLogLevel();
      if (var3 <= var2) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Load failed for ");
         var4.append(this.model);
         var4.append(" with size [");
         var4.append(this.width);
         var4.append("x");
         var4.append(this.height);
         var4.append("]");
         Log.w("Glide", var4.toString(), var1);
         if (var3 <= 4) {
            var1.logRootCauses("Glide");
         }
      }

      this.loadStatus = null;
      this.status = SingleRequest.Status.FAILED;
      this.isCallingCallbacks = true;

      try {
         if (this.requestListener == null || !this.requestListener.onLoadFailed(var1, this.model, this.target, this.isFirstReadyResource())) {
            this.setErrorPlaceholder();
         }
      } finally {
         this.isCallingCallbacks = false;
      }

   }

   private void onResourceReady(Resource var1, Object var2, DataSource var3) {
      boolean var4 = this.isFirstReadyResource();
      this.status = SingleRequest.Status.COMPLETE;
      this.resource = var1;
      if (this.glideContext.getLogLevel() <= 3) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Finished loading ");
         var7.append(var2.getClass().getSimpleName());
         var7.append(" from ");
         var7.append(var3);
         var7.append(" for ");
         var7.append(this.model);
         var7.append(" with size [");
         var7.append(this.width);
         var7.append("x");
         var7.append(this.height);
         var7.append("] in ");
         var7.append(LogTime.getElapsedMillis(this.startTime));
         var7.append(" ms");
         Log.d("Glide", var7.toString());
      }

      this.isCallingCallbacks = true;

      try {
         if (this.requestListener == null || !this.requestListener.onResourceReady(var2, this.model, this.target, var3, var4)) {
            Transition var8 = this.animationFactory.build(var3, var4);
            this.target.onResourceReady(var2, var8);
         }
      } finally {
         this.isCallingCallbacks = false;
      }

      this.notifyLoadSuccess();
   }

   private void releaseResource(Resource var1) {
      this.engine.release(var1);
      this.resource = null;
   }

   private void setErrorPlaceholder() {
      if (this.canNotifyStatusChanged()) {
         Drawable var1 = null;
         if (this.model == null) {
            var1 = this.getFallbackDrawable();
         }

         Drawable var2 = var1;
         if (var1 == null) {
            var2 = this.getErrorDrawable();
         }

         var1 = var2;
         if (var2 == null) {
            var1 = this.getPlaceholderDrawable();
         }

         this.target.onLoadFailed(var1);
      }
   }

   public void begin() {
      this.assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      this.startTime = LogTime.getLogTime();
      if (this.model == null) {
         if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            this.width = this.overrideWidth;
            this.height = this.overrideHeight;
         }

         byte var1;
         if (this.getFallbackDrawable() == null) {
            var1 = 5;
         } else {
            var1 = 3;
         }

         this.onLoadFailed(new GlideException("Received null model"), var1);
      } else if (this.status == SingleRequest.Status.RUNNING) {
         throw new IllegalArgumentException("Cannot restart a running request");
      } else if (this.status == SingleRequest.Status.COMPLETE) {
         this.onResourceReady(this.resource, DataSource.MEMORY_CACHE);
      } else {
         this.status = SingleRequest.Status.WAITING_FOR_SIZE;
         if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
            this.onSizeReady(this.overrideWidth, this.overrideHeight);
         } else {
            this.target.getSize(this);
         }

         if ((this.status == SingleRequest.Status.RUNNING || this.status == SingleRequest.Status.WAITING_FOR_SIZE) && this.canNotifyStatusChanged()) {
            this.target.onLoadStarted(this.getPlaceholderDrawable());
         }

         if (Log.isLoggable("Request", 2)) {
            StringBuilder var2 = new StringBuilder();
            var2.append("finished run method in ");
            var2.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(var2.toString());
         }

      }
   }

   void cancel() {
      this.assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      this.target.removeCallback(this);
      this.status = SingleRequest.Status.CANCELLED;
      if (this.loadStatus != null) {
         this.loadStatus.cancel();
         this.loadStatus = null;
      }

   }

   public void clear() {
      Util.assertMainThread();
      this.assertNotCallingCallbacks();
      if (this.status != SingleRequest.Status.CLEARED) {
         this.cancel();
         if (this.resource != null) {
            this.releaseResource(this.resource);
         }

         if (this.canNotifyStatusChanged()) {
            this.target.onLoadCleared(this.getPlaceholderDrawable());
         }

         this.status = SingleRequest.Status.CLEARED;
      }
   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   public boolean isCancelled() {
      boolean var1;
      if (this.status != SingleRequest.Status.CANCELLED && this.status != SingleRequest.Status.CLEARED) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isComplete() {
      boolean var1;
      if (this.status == SingleRequest.Status.COMPLETE) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isEquivalentTo(Request var1) {
      boolean var2 = var1 instanceof SingleRequest;
      boolean var3 = false;
      if (var2) {
         SingleRequest var4 = (SingleRequest)var1;
         var2 = var3;
         if (this.overrideWidth == var4.overrideWidth) {
            var2 = var3;
            if (this.overrideHeight == var4.overrideHeight) {
               var2 = var3;
               if (Util.bothModelsNullEquivalentOrEquals(this.model, var4.model)) {
                  var2 = var3;
                  if (this.transcodeClass.equals(var4.transcodeClass)) {
                     var2 = var3;
                     if (this.requestOptions.equals(var4.requestOptions)) {
                        var2 = var3;
                        if (this.priority == var4.priority) {
                           var2 = true;
                        }
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public boolean isResourceSet() {
      return this.isComplete();
   }

   public boolean isRunning() {
      boolean var1;
      if (this.status != SingleRequest.Status.RUNNING && this.status != SingleRequest.Status.WAITING_FOR_SIZE) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void onLoadFailed(GlideException var1) {
      this.onLoadFailed(var1, 5);
   }

   public void onResourceReady(Resource var1, DataSource var2) {
      this.stateVerifier.throwIfRecycled();
      this.loadStatus = null;
      if (var1 == null) {
         StringBuilder var6 = new StringBuilder();
         var6.append("Expected to receive a Resource<R> with an object of ");
         var6.append(this.transcodeClass);
         var6.append(" inside, but instead got null.");
         this.onLoadFailed(new GlideException(var6.toString()));
      } else {
         Object var3 = var1.get();
         if (var3 != null && this.transcodeClass.isAssignableFrom(var3.getClass())) {
            if (!this.canSetResource()) {
               this.releaseResource(var1);
               this.status = SingleRequest.Status.COMPLETE;
            } else {
               this.onResourceReady(var1, var3, var2);
            }
         } else {
            this.releaseResource(var1);
            StringBuilder var4 = new StringBuilder();
            var4.append("Expected to receive an object of ");
            var4.append(this.transcodeClass);
            var4.append(" but instead got ");
            Object var7;
            if (var3 != null) {
               var7 = var3.getClass();
            } else {
               var7 = "";
            }

            var4.append(var7);
            var4.append("{");
            var4.append(var3);
            var4.append("} inside Resource{");
            var4.append(var1);
            var4.append("}.");
            String var5;
            if (var3 != null) {
               var5 = "";
            } else {
               var5 = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
            }

            var4.append(var5);
            this.onLoadFailed(new GlideException(var4.toString()));
         }
      }
   }

   public void onSizeReady(int var1, int var2) {
      this.stateVerifier.throwIfRecycled();
      StringBuilder var3;
      if (Log.isLoggable("Request", 2)) {
         var3 = new StringBuilder();
         var3.append("Got onSizeReady in ");
         var3.append(LogTime.getElapsedMillis(this.startTime));
         this.logV(var3.toString());
      }

      if (this.status == SingleRequest.Status.WAITING_FOR_SIZE) {
         this.status = SingleRequest.Status.RUNNING;
         float var4 = this.requestOptions.getSizeMultiplier();
         this.width = maybeApplySizeMultiplier(var1, var4);
         this.height = maybeApplySizeMultiplier(var2, var4);
         if (Log.isLoggable("Request", 2)) {
            var3 = new StringBuilder();
            var3.append("finished setup for calling load in ");
            var3.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(var3.toString());
         }

         this.loadStatus = this.engine.load(this.glideContext, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getOnlyRetrieveFromCache(), this);
         if (Log.isLoggable("Request", 2)) {
            var3 = new StringBuilder();
            var3.append("finished onSizeReady in ");
            var3.append(LogTime.getElapsedMillis(this.startTime));
            this.logV(var3.toString());
         }

      }
   }

   public void pause() {
      this.clear();
      this.status = SingleRequest.Status.PAUSED;
   }

   public void recycle() {
      this.assertNotCallingCallbacks();
      this.glideContext = null;
      this.model = null;
      this.transcodeClass = null;
      this.requestOptions = null;
      this.overrideWidth = -1;
      this.overrideHeight = -1;
      this.target = null;
      this.requestListener = null;
      this.requestCoordinator = null;
      this.animationFactory = null;
      this.loadStatus = null;
      this.errorDrawable = null;
      this.placeholderDrawable = null;
      this.fallbackDrawable = null;
      this.width = -1;
      this.height = -1;
      POOL.release(this);
   }

   private static enum Status {
      CANCELLED,
      CLEARED,
      COMPLETE,
      FAILED,
      PAUSED,
      PENDING,
      RUNNING,
      WAITING_FOR_SIZE;
   }
}
