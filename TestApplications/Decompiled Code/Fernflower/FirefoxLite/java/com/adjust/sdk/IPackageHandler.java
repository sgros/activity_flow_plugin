package com.adjust.sdk;

import android.content.Context;

public interface IPackageHandler {
   void addPackage(ActivityPackage var1);

   void closeFirstPackage(ResponseData var1, ActivityPackage var2);

   void init(IActivityHandler var1, Context var2, boolean var3);

   void pauseSending();

   void resumeSending();

   void sendFirstPackage();

   void sendNextPackage(ResponseData var1);

   void teardown(boolean var1);

   void updatePackages(SessionParameters var1);
}