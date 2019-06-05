package com.bumptech.glide.request;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public class RequestOptions implements Cloneable {
   private DiskCacheStrategy diskCacheStrategy;
   private int errorId;
   private Drawable errorPlaceholder;
   private Drawable fallbackDrawable;
   private int fallbackId;
   private int fields;
   private boolean isAutoCloneEnabled;
   private boolean isCacheable;
   private boolean isLocked;
   private boolean isScaleOnlyOrNoTransform;
   private boolean isTransformationAllowed;
   private boolean isTransformationRequired;
   private boolean onlyRetrieveFromCache;
   private Options options;
   private int overrideHeight;
   private int overrideWidth;
   private Drawable placeholderDrawable;
   private int placeholderId;
   private Priority priority;
   private Class resourceClass;
   private Key signature;
   private float sizeMultiplier = 1.0F;
   private Theme theme;
   private Map transformations;
   private boolean useUnlimitedSourceGeneratorsPool;

   public RequestOptions() {
      this.diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
      this.priority = Priority.NORMAL;
      this.isCacheable = true;
      this.overrideHeight = -1;
      this.overrideWidth = -1;
      this.signature = EmptySignature.obtain();
      this.isTransformationAllowed = true;
      this.options = new Options();
      this.transformations = new HashMap();
      this.resourceClass = Object.class;
      this.isScaleOnlyOrNoTransform = true;
   }

   public static RequestOptions decodeTypeOf(Class var0) {
      return (new RequestOptions()).decode(var0);
   }

   public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy var0) {
      return (new RequestOptions()).diskCacheStrategy(var0);
   }

   private boolean isSet(int var1) {
      return isSet(this.fields, var1);
   }

   private static boolean isSet(int var0, int var1) {
      boolean var2;
      if ((var0 & var1) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private RequestOptions optionalScaleOnlyTransform(DownsampleStrategy var1, Transformation var2) {
      return this.scaleOnlyTransform(var1, var2, false);
   }

   private RequestOptions scaleOnlyTransform(DownsampleStrategy var1, Transformation var2) {
      return this.scaleOnlyTransform(var1, var2, true);
   }

   private RequestOptions scaleOnlyTransform(DownsampleStrategy var1, Transformation var2, boolean var3) {
      RequestOptions var4;
      if (var3) {
         var4 = this.transform(var1, var2);
      } else {
         var4 = this.optionalTransform(var1, var2);
      }

      var4.isScaleOnlyOrNoTransform = true;
      return var4;
   }

   private RequestOptions selfOrThrowIfLocked() {
      if (!this.isLocked) {
         return this;
      } else {
         throw new IllegalStateException("You cannot modify locked RequestOptions, consider clone()");
      }
   }

   public RequestOptions apply(RequestOptions var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().apply(var1);
      } else {
         if (isSet(var1.fields, 2)) {
            this.sizeMultiplier = var1.sizeMultiplier;
         }

         if (isSet(var1.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = var1.useUnlimitedSourceGeneratorsPool;
         }

         if (isSet(var1.fields, 4)) {
            this.diskCacheStrategy = var1.diskCacheStrategy;
         }

         if (isSet(var1.fields, 8)) {
            this.priority = var1.priority;
         }

         if (isSet(var1.fields, 16)) {
            this.errorPlaceholder = var1.errorPlaceholder;
         }

         if (isSet(var1.fields, 32)) {
            this.errorId = var1.errorId;
         }

         if (isSet(var1.fields, 64)) {
            this.placeholderDrawable = var1.placeholderDrawable;
         }

         if (isSet(var1.fields, 128)) {
            this.placeholderId = var1.placeholderId;
         }

         if (isSet(var1.fields, 256)) {
            this.isCacheable = var1.isCacheable;
         }

         if (isSet(var1.fields, 512)) {
            this.overrideWidth = var1.overrideWidth;
            this.overrideHeight = var1.overrideHeight;
         }

         if (isSet(var1.fields, 1024)) {
            this.signature = var1.signature;
         }

         if (isSet(var1.fields, 4096)) {
            this.resourceClass = var1.resourceClass;
         }

         if (isSet(var1.fields, 8192)) {
            this.fallbackDrawable = var1.fallbackDrawable;
         }

         if (isSet(var1.fields, 16384)) {
            this.fallbackId = var1.fallbackId;
         }

         if (isSet(var1.fields, 32768)) {
            this.theme = var1.theme;
         }

         if (isSet(var1.fields, 65536)) {
            this.isTransformationAllowed = var1.isTransformationAllowed;
         }

         if (isSet(var1.fields, 131072)) {
            this.isTransformationRequired = var1.isTransformationRequired;
         }

         if (isSet(var1.fields, 2048)) {
            this.transformations.putAll(var1.transformations);
            this.isScaleOnlyOrNoTransform = var1.isScaleOnlyOrNoTransform;
         }

         if (isSet(var1.fields, 524288)) {
            this.onlyRetrieveFromCache = var1.onlyRetrieveFromCache;
         }

         if (!this.isTransformationAllowed) {
            this.transformations.clear();
            this.fields &= -2049;
            this.isTransformationRequired = false;
            this.fields &= -131073;
            this.isScaleOnlyOrNoTransform = true;
         }

         this.fields |= var1.fields;
         this.options.putAll(var1.options);
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions autoClone() {
      if (this.isLocked && !this.isAutoCloneEnabled) {
         throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
      } else {
         this.isAutoCloneEnabled = true;
         return this.lock();
      }
   }

   public RequestOptions clone() {
      try {
         RequestOptions var1 = (RequestOptions)super.clone();
         Options var2 = new Options();
         var1.options = var2;
         var1.options.putAll(this.options);
         HashMap var4 = new HashMap();
         var1.transformations = var4;
         var1.transformations.putAll(this.transformations);
         var1.isLocked = false;
         var1.isAutoCloneEnabled = false;
         return var1;
      } catch (CloneNotSupportedException var3) {
         throw new RuntimeException(var3);
      }
   }

   public RequestOptions decode(Class var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().decode(var1);
      } else {
         this.resourceClass = (Class)Preconditions.checkNotNull(var1);
         this.fields |= 4096;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions diskCacheStrategy(DiskCacheStrategy var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().diskCacheStrategy(var1);
      } else {
         this.diskCacheStrategy = (DiskCacheStrategy)Preconditions.checkNotNull(var1);
         this.fields |= 4;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions dontAnimate() {
      return this.isAutoCloneEnabled ? this.clone().dontAnimate() : this.selfOrThrowIfLocked();
   }

   public RequestOptions downsample(DownsampleStrategy var1) {
      return this.set(Downsampler.DOWNSAMPLE_STRATEGY, Preconditions.checkNotNull(var1));
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof RequestOptions;
      boolean var3 = false;
      if (var2) {
         RequestOptions var4 = (RequestOptions)var1;
         var2 = var3;
         if (Float.compare(var4.sizeMultiplier, this.sizeMultiplier) == 0) {
            var2 = var3;
            if (this.errorId == var4.errorId) {
               var2 = var3;
               if (Util.bothNullOrEqual(this.errorPlaceholder, var4.errorPlaceholder)) {
                  var2 = var3;
                  if (this.placeholderId == var4.placeholderId) {
                     var2 = var3;
                     if (Util.bothNullOrEqual(this.placeholderDrawable, var4.placeholderDrawable)) {
                        var2 = var3;
                        if (this.fallbackId == var4.fallbackId) {
                           var2 = var3;
                           if (Util.bothNullOrEqual(this.fallbackDrawable, var4.fallbackDrawable)) {
                              var2 = var3;
                              if (this.isCacheable == var4.isCacheable) {
                                 var2 = var3;
                                 if (this.overrideHeight == var4.overrideHeight) {
                                    var2 = var3;
                                    if (this.overrideWidth == var4.overrideWidth) {
                                       var2 = var3;
                                       if (this.isTransformationRequired == var4.isTransformationRequired) {
                                          var2 = var3;
                                          if (this.isTransformationAllowed == var4.isTransformationAllowed) {
                                             var2 = var3;
                                             if (this.useUnlimitedSourceGeneratorsPool == var4.useUnlimitedSourceGeneratorsPool) {
                                                var2 = var3;
                                                if (this.onlyRetrieveFromCache == var4.onlyRetrieveFromCache) {
                                                   var2 = var3;
                                                   if (this.diskCacheStrategy.equals(var4.diskCacheStrategy)) {
                                                      var2 = var3;
                                                      if (this.priority == var4.priority) {
                                                         var2 = var3;
                                                         if (this.options.equals(var4.options)) {
                                                            var2 = var3;
                                                            if (this.transformations.equals(var4.transformations)) {
                                                               var2 = var3;
                                                               if (this.resourceClass.equals(var4.resourceClass)) {
                                                                  var2 = var3;
                                                                  if (Util.bothNullOrEqual(this.signature, var4.signature)) {
                                                                     var2 = var3;
                                                                     if (Util.bothNullOrEqual(this.theme, var4.theme)) {
                                                                        var2 = true;
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
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

   public RequestOptions fitCenter() {
      return this.scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
   }

   public final DiskCacheStrategy getDiskCacheStrategy() {
      return this.diskCacheStrategy;
   }

   public final int getErrorId() {
      return this.errorId;
   }

   public final Drawable getErrorPlaceholder() {
      return this.errorPlaceholder;
   }

   public final Drawable getFallbackDrawable() {
      return this.fallbackDrawable;
   }

   public final int getFallbackId() {
      return this.fallbackId;
   }

   public final boolean getOnlyRetrieveFromCache() {
      return this.onlyRetrieveFromCache;
   }

   public final Options getOptions() {
      return this.options;
   }

   public final int getOverrideHeight() {
      return this.overrideHeight;
   }

   public final int getOverrideWidth() {
      return this.overrideWidth;
   }

   public final Drawable getPlaceholderDrawable() {
      return this.placeholderDrawable;
   }

   public final int getPlaceholderId() {
      return this.placeholderId;
   }

   public final Priority getPriority() {
      return this.priority;
   }

   public final Class getResourceClass() {
      return this.resourceClass;
   }

   public final Key getSignature() {
      return this.signature;
   }

   public final float getSizeMultiplier() {
      return this.sizeMultiplier;
   }

   public final Theme getTheme() {
      return this.theme;
   }

   public final Map getTransformations() {
      return this.transformations;
   }

   public final boolean getUseUnlimitedSourceGeneratorsPool() {
      return this.useUnlimitedSourceGeneratorsPool;
   }

   public int hashCode() {
      int var1 = Util.hashCode(this.sizeMultiplier);
      var1 = Util.hashCode(this.errorId, var1);
      var1 = Util.hashCode(this.errorPlaceholder, var1);
      var1 = Util.hashCode(this.placeholderId, var1);
      var1 = Util.hashCode(this.placeholderDrawable, var1);
      var1 = Util.hashCode(this.fallbackId, var1);
      var1 = Util.hashCode(this.fallbackDrawable, var1);
      var1 = Util.hashCode(this.isCacheable, var1);
      var1 = Util.hashCode(this.overrideHeight, var1);
      var1 = Util.hashCode(this.overrideWidth, var1);
      var1 = Util.hashCode(this.isTransformationRequired, var1);
      var1 = Util.hashCode(this.isTransformationAllowed, var1);
      var1 = Util.hashCode(this.useUnlimitedSourceGeneratorsPool, var1);
      var1 = Util.hashCode(this.onlyRetrieveFromCache, var1);
      var1 = Util.hashCode(this.diskCacheStrategy, var1);
      var1 = Util.hashCode(this.priority, var1);
      var1 = Util.hashCode(this.options, var1);
      var1 = Util.hashCode(this.transformations, var1);
      var1 = Util.hashCode(this.resourceClass, var1);
      var1 = Util.hashCode(this.signature, var1);
      return Util.hashCode(this.theme, var1);
   }

   public final boolean isMemoryCacheable() {
      return this.isCacheable;
   }

   public final boolean isPrioritySet() {
      return this.isSet(8);
   }

   public boolean isScaleOnlyOrNoTransform() {
      return this.isScaleOnlyOrNoTransform;
   }

   public final boolean isTransformationAllowed() {
      return this.isTransformationAllowed;
   }

   public final boolean isTransformationRequired() {
      return this.isTransformationRequired;
   }

   public final boolean isTransformationSet() {
      return this.isSet(2048);
   }

   public final boolean isValidOverride() {
      return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
   }

   public RequestOptions lock() {
      this.isLocked = true;
      return this;
   }

   public RequestOptions optionalCenterCrop() {
      return this.optionalTransform((DownsampleStrategy)DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
   }

   public RequestOptions optionalCenterInside() {
      return this.optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
   }

   public RequestOptions optionalFitCenter() {
      return this.optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
   }

   public RequestOptions optionalTransform(Transformation var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().optionalTransform(var1);
      } else {
         this.optionalTransform(Bitmap.class, var1);
         this.optionalTransform((Class)BitmapDrawable.class, new BitmapDrawableTransformation(var1));
         return this.selfOrThrowIfLocked();
      }
   }

   final RequestOptions optionalTransform(DownsampleStrategy var1, Transformation var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().optionalTransform(var1, var2);
      } else {
         this.downsample(var1);
         return this.optionalTransform(var2);
      }
   }

   public RequestOptions optionalTransform(Class var1, Transformation var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().optionalTransform(var1, var2);
      } else {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         this.transformations.put(var1, var2);
         this.fields |= 2048;
         this.isTransformationAllowed = true;
         this.fields |= 65536;
         this.isScaleOnlyOrNoTransform = false;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions placeholder(int var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().placeholder(var1);
      } else {
         this.placeholderId = var1;
         this.fields |= 128;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions priority(Priority var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().priority(var1);
      } else {
         this.priority = (Priority)Preconditions.checkNotNull(var1);
         this.fields |= 8;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions set(Option var1, Object var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().set(var1, var2);
      } else {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         this.options.set(var1, var2);
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions sizeMultiplier(float var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().sizeMultiplier(var1);
      } else if (var1 >= 0.0F && var1 <= 1.0F) {
         this.sizeMultiplier = var1;
         this.fields |= 2;
         return this.selfOrThrowIfLocked();
      } else {
         throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
      }
   }

   public RequestOptions skipMemoryCache(boolean var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().skipMemoryCache(true);
      } else {
         this.isCacheable = var1 ^ true;
         this.fields |= 256;
         return this.selfOrThrowIfLocked();
      }
   }

   public RequestOptions transform(Transformation var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transform(var1);
      } else {
         this.optionalTransform(var1);
         this.isTransformationRequired = true;
         this.fields |= 131072;
         return this.selfOrThrowIfLocked();
      }
   }

   final RequestOptions transform(DownsampleStrategy var1, Transformation var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transform(var1, var2);
      } else {
         this.downsample(var1);
         return this.transform(var2);
      }
   }

   public RequestOptions transforms(Transformation... var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transforms(var1);
      } else {
         this.optionalTransform(new MultiTransformation(var1));
         this.isTransformationRequired = true;
         this.fields |= 131072;
         return this.selfOrThrowIfLocked();
      }
   }
}
