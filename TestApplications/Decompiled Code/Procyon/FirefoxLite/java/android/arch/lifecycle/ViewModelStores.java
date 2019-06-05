// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;

public class ViewModelStores
{
    public static ViewModelStore of(final Fragment fragment) {
        if (fragment instanceof ViewModelStoreOwner) {
            return fragment.getViewModelStore();
        }
        return HolderFragment.holderFragmentFor(fragment).getViewModelStore();
    }
    
    public static ViewModelStore of(final FragmentActivity fragmentActivity) {
        if (fragmentActivity instanceof ViewModelStoreOwner) {
            return fragmentActivity.getViewModelStore();
        }
        return HolderFragment.holderFragmentFor(fragmentActivity).getViewModelStore();
    }
}
