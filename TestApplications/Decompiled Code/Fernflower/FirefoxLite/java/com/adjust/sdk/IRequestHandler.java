package com.adjust.sdk;

public interface IRequestHandler {
   void init(IPackageHandler var1);

   void sendPackage(ActivityPackage var1, int var2);

   void teardown();
}
