package android.support.p001v4.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* renamed from: android.support.v4.app.FragmentManager */
public abstract class FragmentManager {

    /* renamed from: android.support.v4.app.FragmentManager$BackStackEntry */
    public interface BackStackEntry {
        String getName();
    }

    /* renamed from: android.support.v4.app.FragmentManager$FragmentLifecycleCallbacks */
    public static abstract class FragmentLifecycleCallbacks {
        public void onFragmentActivityCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentDetached(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPaused(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPreAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentStarted(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentStopped(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
        }

        public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }
    }

    /* renamed from: android.support.v4.app.FragmentManager$OnBackStackChangedListener */
    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    public abstract void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener);

    public abstract FragmentTransaction beginTransaction();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract boolean executePendingTransactions();

    public abstract Fragment findFragmentById(int i);

    public abstract Fragment findFragmentByTag(String str);

    public abstract BackStackEntry getBackStackEntryAt(int i);

    public abstract int getBackStackEntryCount();

    public abstract List<Fragment> getFragments();

    public abstract boolean isDestroyed();

    public abstract boolean isStateSaved();

    public abstract void popBackStack();

    public abstract void popBackStack(int i, int i2);

    public abstract boolean popBackStackImmediate();

    public abstract void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z);

    public abstract void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener);

    public abstract void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks);
}
