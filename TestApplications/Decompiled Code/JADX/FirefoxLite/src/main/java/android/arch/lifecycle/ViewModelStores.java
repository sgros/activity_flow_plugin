package android.arch.lifecycle;

import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;

public class ViewModelStores {
    /* renamed from: of */
    public static ViewModelStore m5of(FragmentActivity fragmentActivity) {
        if (fragmentActivity instanceof ViewModelStoreOwner) {
            return fragmentActivity.getViewModelStore();
        }
        return HolderFragment.holderFragmentFor(fragmentActivity).getViewModelStore();
    }

    /* renamed from: of */
    public static ViewModelStore m4of(Fragment fragment) {
        if (fragment instanceof ViewModelStoreOwner) {
            return fragment.getViewModelStore();
        }
        return HolderFragment.holderFragmentFor(fragment).getViewModelStore();
    }
}
