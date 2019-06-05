package com.adjust.sdk;

public interface ISdkClickHandler {
   void init(IActivityHandler var1, boolean var2);

   void pauseSending();

   void resumeSending();

   void sendSdkClick(ActivityPackage var1);

   void teardown();
}
