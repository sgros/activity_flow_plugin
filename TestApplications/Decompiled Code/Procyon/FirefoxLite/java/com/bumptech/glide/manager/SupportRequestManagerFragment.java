// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import android.util.Log;
import android.content.Context;
import com.bumptech.glide.Glide;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;
import android.support.v4.app.Fragment;

public class SupportRequestManagerFragment extends Fragment
{
    private final HashSet<SupportRequestManagerFragment> childRequestManagerFragments;
    private final ActivityFragmentLifecycle lifecycle;
    private Fragment parentFragmentHint;
    private RequestManager requestManager;
    private final RequestManagerTreeNode requestManagerTreeNode;
    private SupportRequestManagerFragment rootRequestManagerFragment;
    
    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }
    
    @SuppressLint({ "ValidFragment" })
    public SupportRequestManagerFragment(final ActivityFragmentLifecycle lifecycle) {
        this.requestManagerTreeNode = new SupportFragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet<SupportRequestManagerFragment>();
        this.lifecycle = lifecycle;
    }
    
    private void addChildRequestManagerFragment(final SupportRequestManagerFragment e) {
        this.childRequestManagerFragments.add(e);
    }
    
    private Fragment getParentFragmentUsingHint() {
        Fragment fragment = this.getParentFragment();
        if (fragment == null) {
            fragment = this.parentFragmentHint;
        }
        return fragment;
    }
    
    private void registerFragmentWithRoot(final FragmentActivity fragmentActivity) {
        this.unregisterFragmentWithRoot();
        this.rootRequestManagerFragment = Glide.get((Context)fragmentActivity).getRequestManagerRetriever().getSupportRequestManagerFragment(fragmentActivity.getSupportFragmentManager(), null);
        if (this.rootRequestManagerFragment != this) {
            this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }
    
    private void removeChildRequestManagerFragment(final SupportRequestManagerFragment o) {
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
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            this.registerFragmentWithRoot(this.getActivity());
        }
        catch (IllegalStateException ex) {
            if (Log.isLoggable("SupportRMFragment", 5)) {
                Log.w("SupportRMFragment", "Unable to register fragment with root", (Throwable)ex);
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
        this.unregisterFragmentWithRoot();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        this.parentFragmentHint = null;
        this.unregisterFragmentWithRoot();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }
    
    @Override
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
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{parent=");
        sb.append(this.getParentFragmentUsingHint());
        sb.append("}");
        return sb.toString();
    }
    
    private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode
    {
        SupportFragmentRequestManagerTreeNode() {
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append("{fragment=");
            sb.append(SupportRequestManagerFragment.this);
            sb.append("}");
            return sb.toString();
        }
    }
}
