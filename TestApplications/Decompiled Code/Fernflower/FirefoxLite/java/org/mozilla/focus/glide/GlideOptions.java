package org.mozilla.focus.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;

public final class GlideOptions extends RequestOptions {
   public GlideOptions apply(RequestOptions var1) {
      return (GlideOptions)super.apply(var1);
   }

   public GlideOptions autoClone() {
      return (GlideOptions)super.autoClone();
   }

   public GlideOptions clone() {
      return (GlideOptions)super.clone();
   }

   public GlideOptions decode(Class var1) {
      return (GlideOptions)super.decode(var1);
   }

   public GlideOptions diskCacheStrategy(DiskCacheStrategy var1) {
      return (GlideOptions)super.diskCacheStrategy(var1);
   }

   public GlideOptions dontAnimate() {
      return (GlideOptions)super.dontAnimate();
   }

   public GlideOptions downsample(DownsampleStrategy var1) {
      return (GlideOptions)super.downsample(var1);
   }

   public GlideOptions fitCenter() {
      return (GlideOptions)super.fitCenter();
   }

   public GlideOptions lock() {
      return (GlideOptions)super.lock();
   }

   public GlideOptions optionalCenterCrop() {
      return (GlideOptions)super.optionalCenterCrop();
   }

   public GlideOptions optionalCenterInside() {
      return (GlideOptions)super.optionalCenterInside();
   }

   public GlideOptions optionalFitCenter() {
      return (GlideOptions)super.optionalFitCenter();
   }

   public GlideOptions optionalTransform(Transformation var1) {
      return (GlideOptions)super.optionalTransform(var1);
   }

   public GlideOptions optionalTransform(Class var1, Transformation var2) {
      return (GlideOptions)super.optionalTransform(var1, var2);
   }

   public GlideOptions placeholder(int var1) {
      return (GlideOptions)super.placeholder(var1);
   }

   public GlideOptions priority(Priority var1) {
      return (GlideOptions)super.priority(var1);
   }

   public GlideOptions set(Option var1, Object var2) {
      return (GlideOptions)super.set(var1, var2);
   }

   public GlideOptions sizeMultiplier(float var1) {
      return (GlideOptions)super.sizeMultiplier(var1);
   }

   public GlideOptions skipMemoryCache(boolean var1) {
      return (GlideOptions)super.skipMemoryCache(var1);
   }

   public GlideOptions transform(Transformation var1) {
      return (GlideOptions)super.transform(var1);
   }

   public GlideOptions transforms(Transformation... var1) {
      return (GlideOptions)super.transforms(var1);
   }
}
