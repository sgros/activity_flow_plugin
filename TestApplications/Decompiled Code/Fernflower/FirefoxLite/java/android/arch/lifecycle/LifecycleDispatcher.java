package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class LifecycleDispatcher {
   private static AtomicBoolean sInitialized = new AtomicBoolean(false);

   private static void dispatchIfLifecycleOwner(Fragment var0, Lifecycle.Event var1) {
      if (var0 instanceof LifecycleRegistryOwner) {
         ((LifecycleRegistryOwner)var0).getLifecycle().handleLifecycleEvent(var1);
      }

   }

   static void init(Context var0) {
      if (!sInitialized.getAndSet(true)) {
         ((Application)var0.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleDispatcher.DispatcherActivityCallback());
      }
   }

   private static void markState(FragmentActivity var0, Lifecycle.State var1) {
      markStateIn(var0, var1);
      markState(var0.getSupportFragmentManager(), var1);
   }

   private static void markState(FragmentManager var0, Lifecycle.State var1) {
      List var3 = var0.getFragments();
      if (var3 != null) {
         Iterator var2 = var3.iterator();

         while(var2.hasNext()) {
            Fragment var4 = (Fragment)var2.next();
            if (var4 != null) {
               markStateIn(var4, var1);
               if (var4.isAdded()) {
                  markState(var4.getChildFragmentManager(), var1);
               }
            }
         }

      }
   }

   private static void markStateIn(Object var0, Lifecycle.State var1) {
      if (var0 instanceof LifecycleRegistryOwner) {
         ((LifecycleRegistryOwner)var0).getLifecycle().markState(var1);
      }

   }

   public static class DestructionReportFragment extends Fragment {
      protected void dispatch(Lifecycle.Event var1) {
         LifecycleDispatcher.dispatchIfLifecycleOwner(this.getParentFragment(), var1);
      }

      public void onDestroy() {
         super.onDestroy();
         this.dispatch(Lifecycle.Event.ON_DESTROY);
      }

      public void onPause() {
         super.onPause();
         this.dispatch(Lifecycle.Event.ON_PAUSE);
      }

      public void onStop() {
         super.onStop();
         this.dispatch(Lifecycle.Event.ON_STOP);
      }
   }

   static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {
      private final LifecycleDispatcher.FragmentCallback mFragmentCallback = new LifecycleDispatcher.FragmentCallback();

      public void onActivityCreated(Activity var1, Bundle var2) {
         if (var1 instanceof FragmentActivity) {
            ((FragmentActivity)var1).getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.mFragmentCallback, true);
         }

         ReportFragment.injectIfNeededIn(var1);
      }

      public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
         if (var1 instanceof FragmentActivity) {
            LifecycleDispatcher.markState((FragmentActivity)var1, Lifecycle.State.CREATED);
         }

      }

      public void onActivityStopped(Activity var1) {
         if (var1 instanceof FragmentActivity) {
            LifecycleDispatcher.markState((FragmentActivity)var1, Lifecycle.State.CREATED);
         }

      }
   }

   static class FragmentCallback extends FragmentManager.FragmentLifecycleCallbacks {
      public void onFragmentCreated(FragmentManager var1, Fragment var2, Bundle var3) {
         LifecycleDispatcher.dispatchIfLifecycleOwner(var2, Lifecycle.Event.ON_CREATE);
         if (var2 instanceof LifecycleRegistryOwner) {
            if (var2.getChildFragmentManager().findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
               var2.getChildFragmentManager().beginTransaction().add(new LifecycleDispatcher.DestructionReportFragment(), "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
            }

         }
      }

      public void onFragmentResumed(FragmentManager var1, Fragment var2) {
         LifecycleDispatcher.dispatchIfLifecycleOwner(var2, Lifecycle.Event.ON_RESUME);
      }

      public void onFragmentStarted(FragmentManager var1, Fragment var2) {
         LifecycleDispatcher.dispatchIfLifecycleOwner(var2, Lifecycle.Event.ON_START);
      }
   }
}
