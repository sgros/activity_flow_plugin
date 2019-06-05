package org.mozilla.focus.glide;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.RequestOptions;

public class GlideRequests extends RequestManager {
   public GlideRequests(Glide var1, Lifecycle var2, RequestManagerTreeNode var3) {
      super(var1, var2, var3);
   }

   public GlideRequest as(Class var1) {
      return new GlideRequest(this.glide, this, var1);
   }

   public GlideRequest asBitmap() {
      return (GlideRequest)super.asBitmap();
   }

   public GlideRequest asDrawable() {
      return (GlideRequest)super.asDrawable();
   }

   public GlideRequest load(Object var1) {
      return (GlideRequest)super.load(var1);
   }

   protected void setRequestOptions(RequestOptions var1) {
      if (var1 instanceof GlideOptions) {
         super.setRequestOptions(var1);
      } else {
         super.setRequestOptions((new GlideOptions()).apply(var1));
      }

   }
}
