package android.arch.lifecycle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ViewModelStores {
   public static ViewModelStore of(Fragment var0) {
      return var0 instanceof ViewModelStoreOwner ? ((ViewModelStoreOwner)var0).getViewModelStore() : HolderFragment.holderFragmentFor(var0).getViewModelStore();
   }

   public static ViewModelStore of(FragmentActivity var0) {
      return var0 instanceof ViewModelStoreOwner ? ((ViewModelStoreOwner)var0).getViewModelStore() : HolderFragment.holderFragmentFor(var0).getViewModelStore();
   }
}
