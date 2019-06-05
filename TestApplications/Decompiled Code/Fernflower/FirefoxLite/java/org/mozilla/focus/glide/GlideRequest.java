package org.mozilla.focus.glide;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequest extends RequestBuilder {
   GlideRequest(Glide var1, RequestManager var2, Class var3) {
      super(var1, var2, var3);
   }

   public GlideRequest apply(RequestOptions var1) {
      return (GlideRequest)super.apply(var1);
   }

   public GlideRequest clone() {
      return (GlideRequest)super.clone();
   }

   public GlideRequest fitCenter() {
      if (this.getMutableOptions() instanceof GlideOptions) {
         this.requestOptions = ((GlideOptions)this.getMutableOptions()).fitCenter();
      } else {
         this.requestOptions = (new GlideOptions()).apply(this.requestOptions).fitCenter();
      }

      return this;
   }

   public GlideRequest listener(RequestListener var1) {
      return (GlideRequest)super.listener(var1);
   }

   public GlideRequest load(Object var1) {
      return (GlideRequest)super.load(var1);
   }

   public GlideRequest load(String var1) {
      return (GlideRequest)super.load(var1);
   }

   public GlideRequest placeholder(int var1) {
      if (this.getMutableOptions() instanceof GlideOptions) {
         this.requestOptions = ((GlideOptions)this.getMutableOptions()).placeholder(var1);
      } else {
         this.requestOptions = (new GlideOptions()).apply(this.requestOptions).placeholder(var1);
      }

      return this;
   }
}
