package com.bumptech.glide.load.engine;

import android.os.Looper;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

class EngineResource implements Resource {
   private int acquired;
   private final boolean isCacheable;
   private boolean isRecycled;
   private Key key;
   private EngineResource.ResourceListener listener;
   private final Resource resource;

   EngineResource(Resource var1, boolean var2) {
      this.resource = (Resource)Preconditions.checkNotNull(var1);
      this.isCacheable = var2;
   }

   void acquire() {
      if (!this.isRecycled) {
         if (Looper.getMainLooper().equals(Looper.myLooper())) {
            ++this.acquired;
         } else {
            throw new IllegalThreadStateException("Must call acquire on the main thread");
         }
      } else {
         throw new IllegalStateException("Cannot acquire a recycled resource");
      }
   }

   public Object get() {
      return this.resource.get();
   }

   public Class getResourceClass() {
      return this.resource.getResourceClass();
   }

   public int getSize() {
      return this.resource.getSize();
   }

   boolean isCacheable() {
      return this.isCacheable;
   }

   public void recycle() {
      if (this.acquired <= 0) {
         if (!this.isRecycled) {
            this.isRecycled = true;
            this.resource.recycle();
         } else {
            throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
         }
      } else {
         throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
      }
   }

   void release() {
      if (this.acquired > 0) {
         if (Looper.getMainLooper().equals(Looper.myLooper())) {
            int var1 = this.acquired - 1;
            this.acquired = var1;
            if (var1 == 0) {
               this.listener.onResourceReleased(this.key, this);
            }

         } else {
            throw new IllegalThreadStateException("Must call release on the main thread");
         }
      } else {
         throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
      }
   }

   void setResourceListener(Key var1, EngineResource.ResourceListener var2) {
      this.key = var1;
      this.listener = var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("EngineResource{isCacheable=");
      var1.append(this.isCacheable);
      var1.append(", listener=");
      var1.append(this.listener);
      var1.append(", key=");
      var1.append(this.key);
      var1.append(", acquired=");
      var1.append(this.acquired);
      var1.append(", isRecycled=");
      var1.append(this.isRecycled);
      var1.append(", resource=");
      var1.append(this.resource);
      var1.append('}');
      return var1.toString();
   }

   interface ResourceListener {
      void onResourceReleased(Key var1, EngineResource var2);
   }
}
