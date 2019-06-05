package com.bumptech.glide.request;

public interface Request {
   void begin();

   void clear();

   boolean isCancelled();

   boolean isComplete();

   boolean isEquivalentTo(Request var1);

   boolean isResourceSet();

   boolean isRunning();

   void pause();

   void recycle();
}
