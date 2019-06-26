package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.Build.VERSION;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;

@SuppressLint({"NewApi"})
public class ForegroundDetector implements ActivityLifecycleCallbacks {
   private static ForegroundDetector Instance;
   private long enterBackgroundTime = 0L;
   private CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
   private int refs;
   private boolean wasInBackground = true;

   public ForegroundDetector(Application var1) {
      Instance = this;
      var1.registerActivityLifecycleCallbacks(this);
   }

   public static ForegroundDetector getInstance() {
      return Instance;
   }

   public void addListener(ForegroundDetector.Listener var1) {
      this.listeners.add(var1);
   }

   public boolean isBackground() {
      boolean var1;
      if (this.refs == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isForeground() {
      boolean var1;
      if (this.refs > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isWasInBackground(boolean var1) {
      if (var1 && VERSION.SDK_INT >= 21 && System.currentTimeMillis() - this.enterBackgroundTime < 200L) {
         this.wasInBackground = false;
      }

      return this.wasInBackground;
   }

   public void onActivityCreated(Activity var1, Bundle var2) {
   }

   public void onActivityDestroyed(Activity var1) {
   }

   public void onActivityPaused(Activity var1) {
   }

   public void onActivityResumed(Activity var1) {
   }

   public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
   }

   public void onActivityStarted(Activity var1) {
      int var2 = this.refs + 1;
      this.refs = var2;
      if (var2 == 1) {
         if (System.currentTimeMillis() - this.enterBackgroundTime < 200L) {
            this.wasInBackground = false;
         }

         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("switch to foreground");
         }

         Iterator var5 = this.listeners.iterator();

         while(var5.hasNext()) {
            ForegroundDetector.Listener var3 = (ForegroundDetector.Listener)var5.next();

            try {
               var3.onBecameForeground();
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }
      }

   }

   public void onActivityStopped(Activity var1) {
      int var2 = this.refs - 1;
      this.refs = var2;
      if (var2 == 0) {
         this.enterBackgroundTime = System.currentTimeMillis();
         this.wasInBackground = true;
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("switch to background");
         }

         Iterator var5 = this.listeners.iterator();

         while(var5.hasNext()) {
            ForegroundDetector.Listener var3 = (ForegroundDetector.Listener)var5.next();

            try {
               var3.onBecameBackground();
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }
      }

   }

   public void removeListener(ForegroundDetector.Listener var1) {
      this.listeners.remove(var1);
   }

   public void resetBackgroundVar() {
      this.wasInBackground = false;
   }

   public interface Listener {
      void onBecameBackground();

      void onBecameForeground();
   }
}
