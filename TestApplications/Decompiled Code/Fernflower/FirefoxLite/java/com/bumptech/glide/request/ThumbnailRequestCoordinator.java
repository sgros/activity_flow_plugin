package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements Request, RequestCoordinator {
   private RequestCoordinator coordinator;
   private Request full;
   private boolean isRunning;
   private Request thumb;

   public ThumbnailRequestCoordinator() {
      this((RequestCoordinator)null);
   }

   public ThumbnailRequestCoordinator(RequestCoordinator var1) {
      this.coordinator = var1;
   }

   private boolean parentCanNotifyStatusChanged() {
      boolean var1;
      if (this.coordinator != null && !this.coordinator.canNotifyStatusChanged(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean parentCanSetImage() {
      boolean var1;
      if (this.coordinator != null && !this.coordinator.canSetImage(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean parentIsAnyResourceSet() {
      boolean var1;
      if (this.coordinator != null && this.coordinator.isAnyResourceSet()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void begin() {
      this.isRunning = true;
      if (!this.thumb.isRunning()) {
         this.thumb.begin();
      }

      if (this.isRunning && !this.full.isRunning()) {
         this.full.begin();
      }

   }

   public boolean canNotifyStatusChanged(Request var1) {
      boolean var2;
      if (this.parentCanNotifyStatusChanged() && var1.equals(this.full) && !this.isAnyResourceSet()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canSetImage(Request var1) {
      boolean var2;
      if (!this.parentCanSetImage() || !var1.equals(this.full) && this.full.isResourceSet()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void clear() {
      this.isRunning = false;
      this.thumb.clear();
      this.full.clear();
   }

   public boolean isAnyResourceSet() {
      boolean var1;
      if (!this.parentIsAnyResourceSet() && !this.isResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isCancelled() {
      return this.full.isCancelled();
   }

   public boolean isComplete() {
      boolean var1;
      if (!this.full.isComplete() && !this.thumb.isComplete()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isEquivalentTo(Request var1) {
      boolean var2 = var1 instanceof ThumbnailRequestCoordinator;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         ThumbnailRequestCoordinator var4 = (ThumbnailRequestCoordinator)var1;
         if (this.full == null) {
            var2 = var3;
            if (var4.full != null) {
               return var2;
            }
         } else {
            var2 = var3;
            if (!this.full.isEquivalentTo(var4.full)) {
               return var2;
            }
         }

         if (this.thumb == null) {
            var2 = var3;
            if (var4.thumb != null) {
               return var2;
            }
         } else {
            var2 = var3;
            if (!this.thumb.isEquivalentTo(var4.thumb)) {
               return var2;
            }
         }

         var2 = true;
         return var2;
      }
   }

   public boolean isResourceSet() {
      boolean var1;
      if (!this.full.isResourceSet() && !this.thumb.isResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isRunning() {
      return this.full.isRunning();
   }

   public void onRequestSuccess(Request var1) {
      if (!var1.equals(this.thumb)) {
         if (this.coordinator != null) {
            this.coordinator.onRequestSuccess(this);
         }

         if (!this.thumb.isComplete()) {
            this.thumb.clear();
         }

      }
   }

   public void pause() {
      this.isRunning = false;
      this.full.pause();
      this.thumb.pause();
   }

   public void recycle() {
      this.full.recycle();
      this.thumb.recycle();
   }

   public void setRequests(Request var1, Request var2) {
      this.full = var1;
      this.thumb = var2;
   }
}
