// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.support.v4.app.Fragment;

public class ViewModelProviders
{
    private static Activity checkActivity(final Fragment fragment) {
        final FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            return activity;
        }
        throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
    }
    
    private static Application checkApplication(final Activity activity) {
        final Application application = activity.getApplication();
        if (application != null) {
            return application;
        }
        throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call.");
    }
    
    public static ViewModelProvider of(final Fragment fragment) {
        return of(fragment, null);
    }
    
    public static ViewModelProvider of(final Fragment fragment, final ViewModelProvider.Factory factory) {
        final Application checkApplication = checkApplication(checkActivity(fragment));
        ViewModelProvider.Factory instance = factory;
        if (factory == null) {
            instance = ViewModelProvider.AndroidViewModelFactory.getInstance(checkApplication);
        }
        return new ViewModelProvider(ViewModelStores.of(fragment), instance);
    }
    
    public static ViewModelProvider of(final FragmentActivity fragmentActivity) {
        return of(fragmentActivity, null);
    }
    
    public static ViewModelProvider of(final FragmentActivity fragmentActivity, final ViewModelProvider.Factory factory) {
        final Application checkApplication = checkApplication(fragmentActivity);
        ViewModelProvider.Factory instance = factory;
        if (factory == null) {
            instance = ViewModelProvider.AndroidViewModelFactory.getInstance(checkApplication);
        }
        return new ViewModelProvider(ViewModelStores.of(fragmentActivity), instance);
    }
}
