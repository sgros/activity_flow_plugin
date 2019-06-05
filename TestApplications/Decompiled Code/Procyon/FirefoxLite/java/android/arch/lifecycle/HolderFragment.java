// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.util.Log;
import java.util.HashMap;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import java.util.Map;
import android.app.Application$ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;

public class HolderFragment extends Fragment implements ViewModelStoreOwner
{
    private static final HolderFragmentManager sHolderFragmentManager;
    private ViewModelStore mViewModelStore;
    
    static {
        sHolderFragmentManager = new HolderFragmentManager();
    }
    
    public HolderFragment() {
        this.mViewModelStore = new ViewModelStore();
        this.setRetainInstance(true);
    }
    
    public static HolderFragment holderFragmentFor(final Fragment fragment) {
        return HolderFragment.sHolderFragmentManager.holderFragmentFor(fragment);
    }
    
    public static HolderFragment holderFragmentFor(final FragmentActivity fragmentActivity) {
        return HolderFragment.sHolderFragmentManager.holderFragmentFor(fragmentActivity);
    }
    
    @Override
    public ViewModelStore getViewModelStore() {
        return this.mViewModelStore;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        HolderFragment.sHolderFragmentManager.holderFragmentCreated(this);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mViewModelStore.clear();
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
    
    static class HolderFragmentManager
    {
        private Application$ActivityLifecycleCallbacks mActivityCallbacks;
        private boolean mActivityCallbacksIsAdded;
        private Map<Activity, HolderFragment> mNotCommittedActivityHolders;
        private Map<Fragment, HolderFragment> mNotCommittedFragmentHolders;
        private FragmentManager.FragmentLifecycleCallbacks mParentDestroyedCallback;
        
        HolderFragmentManager() {
            this.mNotCommittedActivityHolders = new HashMap<Activity, HolderFragment>();
            this.mNotCommittedFragmentHolders = new HashMap<Fragment, HolderFragment>();
            this.mActivityCallbacks = (Application$ActivityLifecycleCallbacks)new EmptyActivityLifecycleCallbacks() {
                @Override
                public void onActivityDestroyed(final Activity obj) {
                    if (HolderFragmentManager.this.mNotCommittedActivityHolders.remove(obj) != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to save a ViewModel for ");
                        sb.append(obj);
                        Log.e("ViewModelStores", sb.toString());
                    }
                }
            };
            this.mActivityCallbacksIsAdded = false;
            this.mParentDestroyedCallback = new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentDestroyed(final FragmentManager fragmentManager, final Fragment obj) {
                    super.onFragmentDestroyed(fragmentManager, obj);
                    if (HolderFragmentManager.this.mNotCommittedFragmentHolders.remove(obj) != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to save a ViewModel for ");
                        sb.append(obj);
                        Log.e("ViewModelStores", sb.toString());
                    }
                }
            };
        }
        
        private static HolderFragment createHolderFragment(final FragmentManager fragmentManager) {
            final HolderFragment holderFragment = new HolderFragment();
            fragmentManager.beginTransaction().add(holderFragment, "android.arch.lifecycle.state.StateProviderHolderFragment").commitAllowingStateLoss();
            return holderFragment;
        }
        
        private static HolderFragment findHolderFragment(final FragmentManager fragmentManager) {
            if (fragmentManager.isDestroyed()) {
                throw new IllegalStateException("Can't access ViewModels from onDestroy");
            }
            final Fragment fragmentByTag = fragmentManager.findFragmentByTag("android.arch.lifecycle.state.StateProviderHolderFragment");
            if (fragmentByTag != null && !(fragmentByTag instanceof HolderFragment)) {
                throw new IllegalStateException("Unexpected fragment instance was returned by HOLDER_TAG");
            }
            return (HolderFragment)fragmentByTag;
        }
        
        void holderFragmentCreated(final Fragment fragment) {
            final Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment != null) {
                this.mNotCommittedFragmentHolders.remove(parentFragment);
                parentFragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(this.mParentDestroyedCallback);
            }
            else {
                this.mNotCommittedActivityHolders.remove(fragment.getActivity());
            }
        }
        
        HolderFragment holderFragmentFor(final Fragment fragment) {
            final FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            final HolderFragment holderFragment = findHolderFragment(childFragmentManager);
            if (holderFragment != null) {
                return holderFragment;
            }
            final HolderFragment holderFragment2 = this.mNotCommittedFragmentHolders.get(fragment);
            if (holderFragment2 != null) {
                return holderFragment2;
            }
            fragment.getFragmentManager().registerFragmentLifecycleCallbacks(this.mParentDestroyedCallback, false);
            final HolderFragment holderFragment3 = createHolderFragment(childFragmentManager);
            this.mNotCommittedFragmentHolders.put(fragment, holderFragment3);
            return holderFragment3;
        }
        
        HolderFragment holderFragmentFor(final FragmentActivity fragmentActivity) {
            final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
            final HolderFragment holderFragment = findHolderFragment(supportFragmentManager);
            if (holderFragment != null) {
                return holderFragment;
            }
            final HolderFragment holderFragment2 = this.mNotCommittedActivityHolders.get(fragmentActivity);
            if (holderFragment2 != null) {
                return holderFragment2;
            }
            if (!this.mActivityCallbacksIsAdded) {
                this.mActivityCallbacksIsAdded = true;
                fragmentActivity.getApplication().registerActivityLifecycleCallbacks(this.mActivityCallbacks);
            }
            final HolderFragment holderFragment3 = createHolderFragment(supportFragmentManager);
            this.mNotCommittedActivityHolders.put(fragmentActivity, holderFragment3);
            return holderFragment3;
        }
    }
}
