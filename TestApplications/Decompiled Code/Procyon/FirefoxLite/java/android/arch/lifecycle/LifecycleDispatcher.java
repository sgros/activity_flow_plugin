// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import android.os.Bundle;
import android.app.Activity;
import java.util.Iterator;
import java.util.List;
import android.support.v4.app.FragmentManager;
import android.app.Application$ActivityLifecycleCallbacks;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import java.util.concurrent.atomic.AtomicBoolean;

class LifecycleDispatcher
{
    private static AtomicBoolean sInitialized;
    
    static {
        LifecycleDispatcher.sInitialized = new AtomicBoolean(false);
    }
    
    private static void dispatchIfLifecycleOwner(final Fragment fragment, final Lifecycle.Event event) {
        if (fragment instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner)fragment).getLifecycle().handleLifecycleEvent(event);
        }
    }
    
    static void init(final Context context) {
        if (LifecycleDispatcher.sInitialized.getAndSet(true)) {
            return;
        }
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)new DispatcherActivityCallback());
    }
    
    private static void markState(final FragmentActivity fragmentActivity, final Lifecycle.State state) {
        markStateIn(fragmentActivity, state);
        markState(fragmentActivity.getSupportFragmentManager(), state);
    }
    
    private static void markState(final FragmentManager fragmentManager, final Lifecycle.State state) {
        final List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) {
            return;
        }
        for (final Fragment fragment : fragments) {
            if (fragment == null) {
                continue;
            }
            markStateIn(fragment, state);
            if (!fragment.isAdded()) {
                continue;
            }
            markState(fragment.getChildFragmentManager(), state);
        }
    }
    
    private static void markStateIn(final Object o, final Lifecycle.State state) {
        if (o instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner)o).getLifecycle().markState(state);
        }
    }
    
    public static class DestructionReportFragment extends Fragment
    {
        protected void dispatch(final Lifecycle.Event event) {
            dispatchIfLifecycleOwner(this.getParentFragment(), event);
        }
        
        @Override
        public void onDestroy() {
            super.onDestroy();
            this.dispatch(Lifecycle.Event.ON_DESTROY);
        }
        
        @Override
        public void onPause() {
            super.onPause();
            this.dispatch(Lifecycle.Event.ON_PAUSE);
        }
        
        @Override
        public void onStop() {
            super.onStop();
            this.dispatch(Lifecycle.Event.ON_STOP);
        }
    }
    
    static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks
    {
        private final FragmentCallback mFragmentCallback;
        
        DispatcherActivityCallback() {
            this.mFragmentCallback = new FragmentCallback();
        }
        
        @Override
        public void onActivityCreated(final Activity activity, final Bundle bundle) {
            if (activity instanceof FragmentActivity) {
                ((FragmentActivity)activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks((FragmentManager.FragmentLifecycleCallbacks)this.mFragmentCallback, true);
            }
            ReportFragment.injectIfNeededIn(activity);
        }
        
        @Override
        public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
            if (activity instanceof FragmentActivity) {
                markState((FragmentActivity)activity, Lifecycle.State.CREATED);
            }
        }
        
        @Override
        public void onActivityStopped(final Activity activity) {
            if (activity instanceof FragmentActivity) {
                markState((FragmentActivity)activity, Lifecycle.State.CREATED);
            }
        }
    }
    
    static class FragmentCallback extends FragmentLifecycleCallbacks
    {
        @Override
        public void onFragmentCreated(final FragmentManager fragmentManager, final Fragment fragment, final Bundle bundle) {
            dispatchIfLifecycleOwner(fragment, Lifecycle.Event.ON_CREATE);
            if (!(fragment instanceof LifecycleRegistryOwner)) {
                return;
            }
            if (fragment.getChildFragmentManager().findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
                fragment.getChildFragmentManager().beginTransaction().add(new DestructionReportFragment(), "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
            }
        }
        
        @Override
        public void onFragmentResumed(final FragmentManager fragmentManager, final Fragment fragment) {
            dispatchIfLifecycleOwner(fragment, Lifecycle.Event.ON_RESUME);
        }
        
        @Override
        public void onFragmentStarted(final FragmentManager fragmentManager, final Fragment fragment) {
            dispatchIfLifecycleOwner(fragment, Lifecycle.Event.ON_START);
        }
    }
}
