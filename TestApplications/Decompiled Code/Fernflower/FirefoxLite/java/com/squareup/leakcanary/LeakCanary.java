package com.squareup.leakcanary;

import android.app.Application;
import android.content.Context;

public final class LeakCanary {
   private LeakCanary() {
      throw new AssertionError();
   }

   public static RefWatcher install(Application var0) {
      return RefWatcher.DISABLED;
   }

   public static RefWatcher installedRefWatcher() {
      return RefWatcher.DISABLED;
   }

   public static boolean isInAnalyzerProcess(Context var0) {
      return false;
   }
}
