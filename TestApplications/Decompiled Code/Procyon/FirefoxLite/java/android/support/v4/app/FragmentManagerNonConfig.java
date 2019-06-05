// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.arch.lifecycle.ViewModelStore;
import java.util.List;

public class FragmentManagerNonConfig
{
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;
    private final List<ViewModelStore> mViewModelStores;
    
    FragmentManagerNonConfig(final List<Fragment> mFragments, final List<FragmentManagerNonConfig> mChildNonConfigs, final List<ViewModelStore> mViewModelStores) {
        this.mFragments = mFragments;
        this.mChildNonConfigs = mChildNonConfigs;
        this.mViewModelStores = mViewModelStores;
    }
    
    List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }
    
    List<Fragment> getFragments() {
        return this.mFragments;
    }
    
    List<ViewModelStore> getViewModelStores() {
        return this.mViewModelStores;
    }
}
