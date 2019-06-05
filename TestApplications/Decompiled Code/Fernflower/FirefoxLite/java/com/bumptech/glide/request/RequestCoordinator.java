package com.bumptech.glide.request;

public interface RequestCoordinator {
   boolean canNotifyStatusChanged(Request var1);

   boolean canSetImage(Request var1);

   boolean isAnyResourceSet();

   void onRequestSuccess(Request var1);
}
