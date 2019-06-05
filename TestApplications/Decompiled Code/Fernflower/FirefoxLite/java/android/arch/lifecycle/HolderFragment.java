package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class HolderFragment extends Fragment implements ViewModelStoreOwner {
   private static final HolderFragment.HolderFragmentManager sHolderFragmentManager = new HolderFragment.HolderFragmentManager();
   private ViewModelStore mViewModelStore = new ViewModelStore();

   public HolderFragment() {
      this.setRetainInstance(true);
   }

   public static HolderFragment holderFragmentFor(Fragment var0) {
      return sHolderFragmentManager.holderFragmentFor(var0);
   }

   public static HolderFragment holderFragmentFor(FragmentActivity var0) {
      return sHolderFragmentManager.holderFragmentFor(var0);
   }

   public ViewModelStore getViewModelStore() {
      return this.mViewModelStore;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      sHolderFragmentManager.holderFragmentCreated(this);
   }

   public void onDestroy() {
      super.onDestroy();
      this.mViewModelStore.clear();
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
   }

   static class HolderFragmentManager {
      private ActivityLifecycleCallbacks mActivityCallbacks = new EmptyActivityLifecycleCallbacks() {
         public void onActivityDestroyed(Activity var1) {
            if ((HolderFragment)HolderFragmentManager.this.mNotCommittedActivityHolders.remove(var1) != null) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Failed to save a ViewModel for ");
               var2.append(var1);
               Log.e("ViewModelStores", var2.toString());
            }

         }
      };
      private boolean mActivityCallbacksIsAdded = false;
      private Map mNotCommittedActivityHolders = new HashMap();
      private Map mNotCommittedFragmentHolders = new HashMap();
      private FragmentManager.FragmentLifecycleCallbacks mParentDestroyedCallback = new FragmentManager.FragmentLifecycleCallbacks() {
         public void onFragmentDestroyed(FragmentManager var1, Fragment var2) {
            super.onFragmentDestroyed(var1, var2);
            if ((HolderFragment)HolderFragmentManager.this.mNotCommittedFragmentHolders.remove(var2) != null) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Failed to save a ViewModel for ");
               var3.append(var2);
               Log.e("ViewModelStores", var3.toString());
            }

         }
      };

      private static HolderFragment createHolderFragment(FragmentManager var0) {
         HolderFragment var1 = new HolderFragment();
         var0.beginTransaction().add(var1, "android.arch.lifecycle.state.StateProviderHolderFragment").commitAllowingStateLoss();
         return var1;
      }

      private static HolderFragment findHolderFragment(FragmentManager var0) {
         if (!var0.isDestroyed()) {
            Fragment var1 = var0.findFragmentByTag("android.arch.lifecycle.state.StateProviderHolderFragment");
            if (var1 != null && !(var1 instanceof HolderFragment)) {
               throw new IllegalStateException("Unexpected fragment instance was returned by HOLDER_TAG");
            } else {
               return (HolderFragment)var1;
            }
         } else {
            throw new IllegalStateException("Can't access ViewModels from onDestroy");
         }
      }

      void holderFragmentCreated(Fragment var1) {
         Fragment var2 = var1.getParentFragment();
         if (var2 != null) {
            this.mNotCommittedFragmentHolders.remove(var2);
            var2.getFragmentManager().unregisterFragmentLifecycleCallbacks(this.mParentDestroyedCallback);
         } else {
            this.mNotCommittedActivityHolders.remove(var1.getActivity());
         }

      }

      HolderFragment holderFragmentFor(Fragment var1) {
         FragmentManager var2 = var1.getChildFragmentManager();
         HolderFragment var3 = findHolderFragment(var2);
         if (var3 != null) {
            return var3;
         } else {
            var3 = (HolderFragment)this.mNotCommittedFragmentHolders.get(var1);
            if (var3 != null) {
               return var3;
            } else {
               var1.getFragmentManager().registerFragmentLifecycleCallbacks(this.mParentDestroyedCallback, false);
               HolderFragment var4 = createHolderFragment(var2);
               this.mNotCommittedFragmentHolders.put(var1, var4);
               return var4;
            }
         }
      }

      HolderFragment holderFragmentFor(FragmentActivity var1) {
         FragmentManager var2 = var1.getSupportFragmentManager();
         HolderFragment var3 = findHolderFragment(var2);
         if (var3 != null) {
            return var3;
         } else {
            var3 = (HolderFragment)this.mNotCommittedActivityHolders.get(var1);
            if (var3 != null) {
               return var3;
            } else {
               if (!this.mActivityCallbacksIsAdded) {
                  this.mActivityCallbacksIsAdded = true;
                  var1.getApplication().registerActivityLifecycleCallbacks(this.mActivityCallbacks);
               }

               HolderFragment var4 = createHolderFragment(var2);
               this.mNotCommittedActivityHolders.put(var1, var4);
               return var4;
            }
         }
      }
   }
}
