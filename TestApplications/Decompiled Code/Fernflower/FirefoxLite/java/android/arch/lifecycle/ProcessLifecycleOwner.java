package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class ProcessLifecycleOwner implements LifecycleOwner {
   private static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
   private Runnable mDelayedPauseRunnable = new Runnable() {
      public void run() {
         ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
         ProcessLifecycleOwner.this.dispatchStopIfNeeded();
      }
   };
   private Handler mHandler;
   private ReportFragment.ActivityInitializationListener mInitializationListener = new ReportFragment.ActivityInitializationListener() {
      public void onCreate() {
      }

      public void onResume() {
         ProcessLifecycleOwner.this.activityResumed();
      }

      public void onStart() {
         ProcessLifecycleOwner.this.activityStarted();
      }
   };
   private boolean mPauseSent = true;
   private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
   private int mResumedCounter = 0;
   private int mStartedCounter = 0;
   private boolean mStopSent = true;

   private ProcessLifecycleOwner() {
   }

   private void dispatchPauseIfNeeded() {
      if (this.mResumedCounter == 0) {
         this.mPauseSent = true;
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      }

   }

   private void dispatchStopIfNeeded() {
      if (this.mStartedCounter == 0 && this.mPauseSent) {
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
         this.mStopSent = true;
      }

   }

   static void init(Context var0) {
      sInstance.attach(var0);
   }

   void activityPaused() {
      --this.mResumedCounter;
      if (this.mResumedCounter == 0) {
         this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700L);
      }

   }

   void activityResumed() {
      ++this.mResumedCounter;
      if (this.mResumedCounter == 1) {
         if (this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            this.mPauseSent = false;
         } else {
            this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
         }
      }

   }

   void activityStarted() {
      ++this.mStartedCounter;
      if (this.mStartedCounter == 1 && this.mStopSent) {
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
         this.mStopSent = false;
      }

   }

   void activityStopped() {
      --this.mStartedCounter;
      this.dispatchStopIfNeeded();
   }

   void attach(Context var1) {
      this.mHandler = new Handler();
      this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      ((Application)var1.getApplicationContext()).registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
         public void onActivityCreated(Activity var1, Bundle var2) {
            ReportFragment.get(var1).setProcessListener(ProcessLifecycleOwner.this.mInitializationListener);
         }

         public void onActivityPaused(Activity var1) {
            ProcessLifecycleOwner.this.activityPaused();
         }

         public void onActivityStopped(Activity var1) {
            ProcessLifecycleOwner.this.activityStopped();
         }
      });
   }

   public Lifecycle getLifecycle() {
      return this.mRegistry;
   }
}
