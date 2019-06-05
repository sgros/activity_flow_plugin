package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.arch.lifecycle.ViewModelProvider.Factory;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;

public class ViewModelProviders {
    private static Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application != null) {
            return application;
        }
        throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call.");
    }

    private static Activity checkActivity(Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            return activity;
        }
        throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
    }

    /* renamed from: of */
    public static ViewModelProvider m0of(Fragment fragment) {
        return m1of(fragment, null);
    }

    /* renamed from: of */
    public static ViewModelProvider m2of(FragmentActivity fragmentActivity) {
        return m3of(fragmentActivity, null);
    }

    /* renamed from: of */
    public static ViewModelProvider m1of(Fragment fragment, Factory factory) {
        Application checkApplication = checkApplication(checkActivity(fragment));
        if (factory == null) {
            factory = AndroidViewModelFactory.getInstance(checkApplication);
        }
        return new ViewModelProvider(ViewModelStores.m4of(fragment), factory);
    }

    /* renamed from: of */
    public static ViewModelProvider m3of(FragmentActivity fragmentActivity, Factory factory) {
        Application checkApplication = checkApplication(fragmentActivity);
        if (factory == null) {
            factory = AndroidViewModelFactory.getInstance(checkApplication);
        }
        return new ViewModelProvider(ViewModelStores.m5of(fragmentActivity), factory);
    }
}
