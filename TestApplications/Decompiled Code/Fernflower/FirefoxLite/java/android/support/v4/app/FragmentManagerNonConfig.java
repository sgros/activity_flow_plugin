package android.support.v4.app;

import java.util.List;

public class FragmentManagerNonConfig {
   private final List mChildNonConfigs;
   private final List mFragments;
   private final List mViewModelStores;

   FragmentManagerNonConfig(List var1, List var2, List var3) {
      this.mFragments = var1;
      this.mChildNonConfigs = var2;
      this.mViewModelStores = var3;
   }

   List getChildNonConfigs() {
      return this.mChildNonConfigs;
   }

   List getFragments() {
      return this.mFragments;
   }

   List getViewModelStores() {
      return this.mViewModelStores;
   }
}
