// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import android.util.Log;
import android.os.Message;
import com.bumptech.glide.util.Preconditions;
import android.content.ContextWrapper;
import android.support.v4.app.FragmentActivity;
import android.app.Application;
import com.bumptech.glide.util.Util;
import android.content.Context;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.app.Activity;
import android.os.Looper;
import java.util.HashMap;
import com.bumptech.glide.Glide;
import android.app.Fragment;
import android.view.View;
import android.support.v4.util.ArrayMap;
import android.os.Bundle;
import android.app.FragmentManager;
import java.util.Map;
import android.os.Handler;
import com.bumptech.glide.RequestManager;
import android.os.Handler$Callback;

public class RequestManagerRetriever implements Handler$Callback
{
    private static final RequestManagerFactory DEFAULT_FACTORY;
    private volatile RequestManager applicationManager;
    private final RequestManagerFactory factory;
    private final Handler handler;
    final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments;
    final Map<android.support.v4.app.FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments;
    private final Bundle tempBundle;
    private final ArrayMap<View, Fragment> tempViewToFragment;
    private final ArrayMap<View, android.support.v4.app.Fragment> tempViewToSupportFragment;
    
    static {
        DEFAULT_FACTORY = (RequestManagerFactory)new RequestManagerFactory() {
            @Override
            public RequestManager build(final Glide glide, final Lifecycle lifecycle, final RequestManagerTreeNode requestManagerTreeNode) {
                return new RequestManager(glide, lifecycle, requestManagerTreeNode);
            }
        };
    }
    
    public RequestManagerRetriever(RequestManagerFactory default_FACTORY) {
        this.pendingRequestManagerFragments = new HashMap<FragmentManager, RequestManagerFragment>();
        this.pendingSupportRequestManagerFragments = new HashMap<android.support.v4.app.FragmentManager, SupportRequestManagerFragment>();
        this.tempViewToSupportFragment = new ArrayMap<View, android.support.v4.app.Fragment>();
        this.tempViewToFragment = new ArrayMap<View, Fragment>();
        this.tempBundle = new Bundle();
        if (default_FACTORY == null) {
            default_FACTORY = RequestManagerRetriever.DEFAULT_FACTORY;
        }
        this.factory = default_FACTORY;
        this.handler = new Handler(Looper.getMainLooper(), (Handler$Callback)this);
    }
    
    @TargetApi(17)
    private static void assertNotDestroyed(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }
    
    private RequestManager fragmentGet(final Context context, final FragmentManager fragmentManager, final Fragment fragment) {
        final RequestManagerFragment requestManagerFragment = this.getRequestManagerFragment(fragmentManager, fragment);
        RequestManager requestManager;
        if ((requestManager = requestManagerFragment.getRequestManager()) == null) {
            requestManager = this.factory.build(Glide.get(context), requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode());
            requestManagerFragment.setRequestManager(requestManager);
        }
        return requestManager;
    }
    
    private RequestManager getApplicationManager(final Context context) {
        if (this.applicationManager == null) {
            synchronized (this) {
                if (this.applicationManager == null) {
                    this.applicationManager = this.factory.build(Glide.get(context), new ApplicationLifecycle(), new EmptyRequestManagerTreeNode());
                }
            }
        }
        return this.applicationManager;
    }
    
    private RequestManager supportFragmentGet(final Context context, final android.support.v4.app.FragmentManager fragmentManager, final android.support.v4.app.Fragment fragment) {
        final SupportRequestManagerFragment supportRequestManagerFragment = this.getSupportRequestManagerFragment(fragmentManager, fragment);
        RequestManager requestManager;
        if ((requestManager = supportRequestManagerFragment.getRequestManager()) == null) {
            requestManager = this.factory.build(Glide.get(context), supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode());
            supportRequestManagerFragment.setRequestManager(requestManager);
        }
        return requestManager;
    }
    
    public RequestManager get(final Activity activity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        return this.fragmentGet((Context)activity, activity.getFragmentManager(), null);
    }
    
    public RequestManager get(final Context context) {
        if (context != null) {
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity) {
                    return this.get((FragmentActivity)context);
                }
                if (context instanceof Activity) {
                    return this.get((Activity)context);
                }
                if (context instanceof ContextWrapper) {
                    return this.get(((ContextWrapper)context).getBaseContext());
                }
            }
            return this.getApplicationManager(context);
        }
        throw new IllegalArgumentException("You cannot start a load on a null Context");
    }
    
    public RequestManager get(final android.support.v4.app.Fragment fragment) {
        Preconditions.checkNotNull(fragment.getActivity(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
        if (Util.isOnBackgroundThread()) {
            return this.get(fragment.getActivity().getApplicationContext());
        }
        return this.supportFragmentGet((Context)fragment.getActivity(), fragment.getChildFragmentManager(), fragment);
    }
    
    public RequestManager get(final FragmentActivity fragmentActivity) {
        if (Util.isOnBackgroundThread()) {
            return this.get(fragmentActivity.getApplicationContext());
        }
        assertNotDestroyed(fragmentActivity);
        return this.supportFragmentGet((Context)fragmentActivity, fragmentActivity.getSupportFragmentManager(), null);
    }
    
    @TargetApi(17)
    RequestManagerFragment getRequestManagerFragment(final FragmentManager fragmentManager, final Fragment parentFragmentHint) {
        RequestManagerFragment requestManagerFragment;
        if ((requestManagerFragment = (RequestManagerFragment)fragmentManager.findFragmentByTag("com.bumptech.glide.manager")) == null && (requestManagerFragment = this.pendingRequestManagerFragments.get(fragmentManager)) == null) {
            requestManagerFragment = new RequestManagerFragment();
            requestManagerFragment.setParentFragmentHint(parentFragmentHint);
            this.pendingRequestManagerFragments.put(fragmentManager, requestManagerFragment);
            fragmentManager.beginTransaction().add((Fragment)requestManagerFragment, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(1, (Object)fragmentManager).sendToTarget();
        }
        return requestManagerFragment;
    }
    
    SupportRequestManagerFragment getSupportRequestManagerFragment(final android.support.v4.app.FragmentManager fragmentManager, final android.support.v4.app.Fragment parentFragmentHint) {
        SupportRequestManagerFragment supportRequestManagerFragment;
        if ((supportRequestManagerFragment = (SupportRequestManagerFragment)fragmentManager.findFragmentByTag("com.bumptech.glide.manager")) == null && (supportRequestManagerFragment = this.pendingSupportRequestManagerFragments.get(fragmentManager)) == null) {
            supportRequestManagerFragment = new SupportRequestManagerFragment();
            supportRequestManagerFragment.setParentFragmentHint(parentFragmentHint);
            this.pendingSupportRequestManagerFragments.put(fragmentManager, supportRequestManagerFragment);
            fragmentManager.beginTransaction().add(supportRequestManagerFragment, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(2, (Object)fragmentManager).sendToTarget();
        }
        return supportRequestManagerFragment;
    }
    
    public boolean handleMessage(final Message message) {
        final int what = message.what;
        final Object o = null;
        boolean b = true;
        Object o2 = null;
        Object obj = null;
        switch (what) {
            default: {
                b = false;
                o2 = null;
                obj = o;
                break;
            }
            case 2: {
                obj = message.obj;
                o2 = this.pendingSupportRequestManagerFragments.remove(obj);
                break;
            }
            case 1: {
                obj = message.obj;
                o2 = this.pendingRequestManagerFragments.remove(obj);
                break;
            }
        }
        if (b && o2 == null && Log.isLoggable("RMRetriever", 5)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to remove expected request manager fragment, manager: ");
            sb.append(obj);
            Log.w("RMRetriever", sb.toString());
        }
        return b;
    }
    
    public interface RequestManagerFactory
    {
        RequestManager build(final Glide p0, final Lifecycle p1, final RequestManagerTreeNode p2);
    }
}
