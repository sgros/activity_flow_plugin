package com.bumptech.glide;

import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class RequestBuilder implements Cloneable {
   protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
   private final GlideContext context;
   private final RequestOptions defaultRequestOptions;
   private final Glide glide;
   private boolean isDefaultTransitionOptionsSet = true;
   private boolean isModelSet;
   private boolean isThumbnailBuilt;
   private Object model;
   private RequestListener requestListener;
   private final RequestManager requestManager;
   protected RequestOptions requestOptions;
   private Float thumbSizeMultiplier;
   private RequestBuilder thumbnailBuilder;
   private final Class transcodeClass;
   private TransitionOptions transitionOptions;

   static {
      DOWNLOAD_ONLY_OPTIONS = (new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
   }

   protected RequestBuilder(Glide var1, RequestManager var2, Class var3) {
      this.glide = var1;
      this.requestManager = var2;
      this.context = var1.getGlideContext();
      this.transcodeClass = var3;
      this.defaultRequestOptions = var2.getDefaultRequestOptions();
      this.transitionOptions = var2.getDefaultTransitionOptions(var3);
      this.requestOptions = this.defaultRequestOptions;
   }

   private Request buildRequest(Target var1, RequestOptions var2) {
      return this.buildRequestRecursive(var1, (ThumbnailRequestCoordinator)null, this.transitionOptions, var2.getPriority(), var2.getOverrideWidth(), var2.getOverrideHeight(), var2);
   }

   private Request buildRequestRecursive(Target var1, ThumbnailRequestCoordinator var2, TransitionOptions var3, Priority var4, int var5, int var6, RequestOptions var7) {
      if (this.thumbnailBuilder != null) {
         if (!this.isThumbnailBuilt) {
            TransitionOptions var8 = this.thumbnailBuilder.transitionOptions;
            if (this.thumbnailBuilder.isDefaultTransitionOptionsSet) {
               var8 = var3;
            }

            Priority var9;
            if (this.thumbnailBuilder.requestOptions.isPrioritySet()) {
               var9 = this.thumbnailBuilder.requestOptions.getPriority();
            } else {
               var9 = this.getThumbnailPriority(var4);
            }

            int var10 = this.thumbnailBuilder.requestOptions.getOverrideWidth();
            int var11 = this.thumbnailBuilder.requestOptions.getOverrideHeight();
            int var12 = var10;
            int var13 = var11;
            if (Util.isValidDimensions(var5, var6)) {
               var12 = var10;
               var13 = var11;
               if (!this.thumbnailBuilder.requestOptions.isValidOverride()) {
                  var12 = var7.getOverrideWidth();
                  var13 = var7.getOverrideHeight();
               }
            }

            var2 = new ThumbnailRequestCoordinator(var2);
            Request var15 = this.obtainRequest(var1, var7, var2, var3, var4, var5, var6);
            this.isThumbnailBuilt = true;
            Request var14 = this.thumbnailBuilder.buildRequestRecursive(var1, var2, var8, var9, var12, var13, this.thumbnailBuilder.requestOptions);
            this.isThumbnailBuilt = false;
            var2.setRequests(var15, var14);
            return var2;
         } else {
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
         }
      } else if (this.thumbSizeMultiplier != null) {
         var2 = new ThumbnailRequestCoordinator(var2);
         var2.setRequests(this.obtainRequest(var1, var7, var2, var3, var4, var5, var6), this.obtainRequest(var1, var7.clone().sizeMultiplier(this.thumbSizeMultiplier), var2, var3, this.getThumbnailPriority(var4), var5, var6));
         return var2;
      } else {
         return this.obtainRequest(var1, var7, var2, var3, var4, var5, var6);
      }
   }

   private Priority getThumbnailPriority(Priority var1) {
      switch(var1) {
      case LOW:
         return Priority.NORMAL;
      case NORMAL:
         return Priority.HIGH;
      case HIGH:
      case IMMEDIATE:
         return Priority.IMMEDIATE;
      default:
         StringBuilder var2 = new StringBuilder();
         var2.append("unknown priority: ");
         var2.append(this.requestOptions.getPriority());
         throw new IllegalArgumentException(var2.toString());
      }
   }

   private Target into(Target var1, RequestOptions var2) {
      Util.assertMainThread();
      Preconditions.checkNotNull(var1);
      if (this.isModelSet) {
         Request var4 = this.buildRequest(var1, var2.autoClone());
         Request var3 = var1.getRequest();
         if (var4.isEquivalentTo(var3)) {
            var4.recycle();
            if (!((Request)Preconditions.checkNotNull(var3)).isRunning()) {
               var3.begin();
            }

            return var1;
         } else {
            this.requestManager.clear(var1);
            var1.setRequest(var4);
            this.requestManager.track(var1, var4);
            return var1;
         }
      } else {
         throw new IllegalArgumentException("You must call #load() before calling #into()");
      }
   }

   private RequestBuilder loadGeneric(Object var1) {
      this.model = var1;
      this.isModelSet = true;
      return this;
   }

   private Request obtainRequest(Target var1, RequestOptions var2, RequestCoordinator var3, TransitionOptions var4, Priority var5, int var6, int var7) {
      return SingleRequest.obtain(this.context, this.model, this.transcodeClass, var2, var6, var7, var5, var1, this.requestListener, var3, this.context.getEngine(), var4.getTransitionFactory());
   }

   public RequestBuilder apply(RequestOptions var1) {
      Preconditions.checkNotNull(var1);
      this.requestOptions = this.getMutableOptions().apply(var1);
      return this;
   }

   public RequestBuilder clone() {
      try {
         RequestBuilder var1 = (RequestBuilder)super.clone();
         var1.requestOptions = var1.requestOptions.clone();
         var1.transitionOptions = var1.transitionOptions.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   protected RequestOptions getMutableOptions() {
      RequestOptions var1;
      if (this.defaultRequestOptions == this.requestOptions) {
         var1 = this.requestOptions.clone();
      } else {
         var1 = this.requestOptions;
      }

      return var1;
   }

   public Target into(ImageView var1) {
      Util.assertMainThread();
      Preconditions.checkNotNull(var1);
      RequestOptions var2 = this.requestOptions;
      RequestOptions var3 = var2;
      if (!var2.isTransformationSet()) {
         var3 = var2;
         if (var2.isTransformationAllowed()) {
            var3 = var2;
            if (var1.getScaleType() != null) {
               switch(var1.getScaleType()) {
               case CENTER_CROP:
                  var3 = var2.clone().optionalCenterCrop();
                  break;
               case CENTER_INSIDE:
                  var3 = var2.clone().optionalCenterInside();
                  break;
               case FIT_CENTER:
               case FIT_START:
               case FIT_END:
                  var3 = var2.clone().optionalFitCenter();
                  break;
               case FIT_XY:
                  var3 = var2.clone().optionalCenterInside();
                  break;
               default:
                  var3 = var2;
               }
            }
         }
      }

      return this.into(this.context.buildImageViewTarget(var1, this.transcodeClass), var3);
   }

   public Target into(Target var1) {
      return this.into(var1, this.getMutableOptions());
   }

   public RequestBuilder listener(RequestListener var1) {
      this.requestListener = var1;
      return this;
   }

   public RequestBuilder load(Object var1) {
      return this.loadGeneric(var1);
   }

   public RequestBuilder load(String var1) {
      return this.loadGeneric(var1);
   }
}
