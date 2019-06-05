package com.bumptech.glide.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Callback {
    private static final RequestManagerFactory DEFAULT_FACTORY = new C04101();
    private volatile RequestManager applicationManager;
    private final RequestManagerFactory factory;
    private final Handler handler;
    final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap();
    final Map<android.support.p001v4.app.FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap();
    private final Bundle tempBundle = new Bundle();
    private final ArrayMap<View, Fragment> tempViewToFragment = new ArrayMap();
    private final ArrayMap<View, android.support.p001v4.app.Fragment> tempViewToSupportFragment = new ArrayMap();

    public interface RequestManagerFactory {
        RequestManager build(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode);
    }

    /* renamed from: com.bumptech.glide.manager.RequestManagerRetriever$1 */
    static class C04101 implements RequestManagerFactory {
        C04101() {
        }

        public RequestManager build(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode) {
            return new RequestManager(glide, lifecycle, requestManagerTreeNode);
        }
    }

    public RequestManagerRetriever(RequestManagerFactory requestManagerFactory) {
        if (requestManagerFactory == null) {
            requestManagerFactory = DEFAULT_FACTORY;
        }
        this.factory = requestManagerFactory;
        this.handler = new Handler(Looper.getMainLooper(), this);
    }

    private RequestManager getApplicationManager(Context context) {
        if (this.applicationManager == null) {
            synchronized (this) {
                if (this.applicationManager == null) {
                    this.applicationManager = this.factory.build(Glide.get(context), new ApplicationLifecycle(), new EmptyRequestManagerTreeNode());
                }
            }
        }
        return this.applicationManager;
    }

    public RequestManager get(Context context) {
        if (context != null) {
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity) {
                    return get((FragmentActivity) context);
                }
                if (context instanceof Activity) {
                    return get((Activity) context);
                }
                if (context instanceof ContextWrapper) {
                    return get(((ContextWrapper) context).getBaseContext());
                }
            }
            return getApplicationManager(context);
        }
        throw new IllegalArgumentException("You cannot start a load on a null Context");
    }

    public RequestManager get(FragmentActivity fragmentActivity) {
        if (Util.isOnBackgroundThread()) {
            return get(fragmentActivity.getApplicationContext());
        }
        assertNotDestroyed(fragmentActivity);
        return supportFragmentGet(fragmentActivity, fragmentActivity.getSupportFragmentManager(), null);
    }

    public RequestManager get(android.support.p001v4.app.Fragment fragment) {
        Preconditions.checkNotNull(fragment.getActivity(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
        if (Util.isOnBackgroundThread()) {
            return get(fragment.getActivity().getApplicationContext());
        }
        return supportFragmentGet(fragment.getActivity(), fragment.getChildFragmentManager(), fragment);
    }

    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        return fragmentGet(activity, activity.getFragmentManager(), null);
    }

    @TargetApi(17)
    private static void assertNotDestroyed(Activity activity) {
        if (VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    /* Access modifiers changed, original: 0000 */
    @TargetApi(17)
    public RequestManagerFragment getRequestManagerFragment(FragmentManager fragmentManager, Fragment fragment) {
        RequestManagerFragment requestManagerFragment = (RequestManagerFragment) fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (requestManagerFragment != null) {
            return requestManagerFragment;
        }
        requestManagerFragment = (RequestManagerFragment) this.pendingRequestManagerFragments.get(fragmentManager);
        if (requestManagerFragment != null) {
            return requestManagerFragment;
        }
        requestManagerFragment = new RequestManagerFragment();
        requestManagerFragment.setParentFragmentHint(fragment);
        this.pendingRequestManagerFragments.put(fragmentManager, requestManagerFragment);
        fragmentManager.beginTransaction().add(requestManagerFragment, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(1, fragmentManager).sendToTarget();
        return requestManagerFragment;
    }

    private RequestManager fragmentGet(Context context, FragmentManager fragmentManager, Fragment fragment) {
        RequestManagerFragment requestManagerFragment = getRequestManagerFragment(fragmentManager, fragment);
        RequestManager requestManager = requestManagerFragment.getRequestManager();
        if (requestManager != null) {
            return requestManager;
        }
        requestManager = this.factory.build(Glide.get(context), requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode());
        requestManagerFragment.setRequestManager(requestManager);
        return requestManager;
    }

    /* Access modifiers changed, original: 0000 */
    public SupportRequestManagerFragment getSupportRequestManagerFragment(android.support.p001v4.app.FragmentManager fragmentManager, android.support.p001v4.app.Fragment fragment) {
        SupportRequestManagerFragment supportRequestManagerFragment = (SupportRequestManagerFragment) fragmentManager.findFragmentByTag("com.bumptech.glide.manager");
        if (supportRequestManagerFragment != null) {
            return supportRequestManagerFragment;
        }
        supportRequestManagerFragment = (SupportRequestManagerFragment) this.pendingSupportRequestManagerFragments.get(fragmentManager);
        if (supportRequestManagerFragment != null) {
            return supportRequestManagerFragment;
        }
        supportRequestManagerFragment = new SupportRequestManagerFragment();
        supportRequestManagerFragment.setParentFragmentHint(fragment);
        this.pendingSupportRequestManagerFragments.put(fragmentManager, supportRequestManagerFragment);
        fragmentManager.beginTransaction().add(supportRequestManagerFragment, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(2, fragmentManager).sendToTarget();
        return supportRequestManagerFragment;
    }

    private RequestManager supportFragmentGet(Context context, android.support.p001v4.app.FragmentManager fragmentManager, android.support.p001v4.app.Fragment fragment) {
        SupportRequestManagerFragment supportRequestManagerFragment = getSupportRequestManagerFragment(fragmentManager, fragment);
        RequestManager requestManager = supportRequestManagerFragment.getRequestManager();
        if (requestManager != null) {
            return requestManager;
        }
        requestManager = this.factory.build(Glide.get(context), supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode());
        supportRequestManagerFragment.setRequestManager(requestManager);
        return requestManager;
    }

    public boolean handleMessage(Message message) {
        Object obj = null;
        boolean z = true;
        Object remove;
        switch (message.what) {
            case 1:
                obj = (FragmentManager) message.obj;
                remove = this.pendingRequestManagerFragments.remove(obj);
                break;
            case 2:
                obj = (android.support.p001v4.app.FragmentManager) message.obj;
                remove = this.pendingSupportRequestManagerFragments.remove(obj);
                break;
            default:
                z = false;
                remove = null;
                break;
        }
        if (z && remove == null && Log.isLoggable("RMRetriever", 5)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to remove expected request manager fragment, manager: ");
            stringBuilder.append(obj);
            Log.w("RMRetriever", stringBuilder.toString());
        }
        return z;
    }
}
