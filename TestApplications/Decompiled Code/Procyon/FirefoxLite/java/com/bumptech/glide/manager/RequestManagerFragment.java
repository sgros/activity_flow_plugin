// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import android.util.Log;
import android.content.Context;
import com.bumptech.glide.Glide;
import android.app.Activity;
import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.annotation.SuppressLint;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;
import android.app.Fragment;

public class RequestManagerFragment extends Fragment
{
    private final HashSet<RequestManagerFragment> childRequestManagerFragments;
    private final ActivityFragmentLifecycle lifecycle;
    private Fragment parentFragmentHint;
    private RequestManager requestManager;
    private final RequestManagerTreeNode requestManagerTreeNode;
    private RequestManagerFragment rootRequestManagerFragment;
    
    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }
    
    @SuppressLint({ "ValidFragment" })
    RequestManagerFragment(final ActivityFragmentLifecycle lifecycle) {
        this.requestManagerTreeNode = new FragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet<RequestManagerFragment>();
        this.lifecycle = lifecycle;
    }
    
    private void addChildRequestManagerFragment(final RequestManagerFragment e) {
        this.childRequestManagerFragments.add(e);
    }
    
    @TargetApi(17)
    private Fragment getParentFragmentUsingHint() {
        Fragment fragment;
        if (Build$VERSION.SDK_INT >= 17) {
            fragment = this.getParentFragment();
        }
        else {
            fragment = null;
        }
        if (fragment == null) {
            fragment = this.parentFragmentHint;
        }
        return fragment;
    }
    
    private void registerFragmentWithRoot(final Activity activity) {
        this.unregisterFragmentWithRoot();
        this.rootRequestManagerFragment = Glide.get((Context)activity).getRequestManagerRetriever().getRequestManagerFragment(activity.getFragmentManager(), null);
        if (this.rootRequestManagerFragment != this) {
            this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }
    
    private void removeChildRequestManagerFragment(final RequestManagerFragment o) {
        this.childRequestManagerFragments.remove(o);
    }
    
    private void unregisterFragmentWithRoot() {
        if (this.rootRequestManagerFragment != null) {
            this.rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            this.rootRequestManagerFragment = null;
        }
    }
    
    ActivityFragmentLifecycle getGlideLifecycle() {
        return this.lifecycle;
    }
    
    public RequestManager getRequestManager() {
        return this.requestManager;
    }
    
    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return this.requestManagerTreeNode;
    }
    
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            this.registerFragmentWithRoot(activity);
        }
        catch (IllegalStateException ex) {
            if (Log.isLoggable("RMFragment", 5)) {
                Log.w("RMFragment", "Unable to register fragment with root", (Throwable)ex);
            }
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
        this.unregisterFragmentWithRoot();
    }
    
    public void onDetach() {
        super.onDetach();
        this.unregisterFragmentWithRoot();
    }
    
    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }
    
    public void onStop() {
        super.onStop();
        this.lifecycle.onStop();
    }
    
    void setParentFragmentHint(final Fragment parentFragmentHint) {
        this.parentFragmentHint = parentFragmentHint;
        if (parentFragmentHint != null && parentFragmentHint.getActivity() != null) {
            this.registerFragmentWithRoot(parentFragmentHint.getActivity());
        }
    }
    
    public void setRequestManager(final RequestManager requestManager) {
        this.requestManager = requestManager;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{parent=");
        sb.append(this.getParentFragmentUsingHint());
        sb.append("}");
        return sb.toString();
    }
    
    private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode
    {
        FragmentRequestManagerTreeNode() {
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append("{fragment=");
            sb.append(RequestManagerFragment.this);
            sb.append("}");
            return sb.toString();
        }
    }
}
