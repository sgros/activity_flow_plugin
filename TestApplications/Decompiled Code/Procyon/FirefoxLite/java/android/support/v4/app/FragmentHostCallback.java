// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.v4.util.Preconditions;
import android.os.Handler;
import android.content.Context;
import android.app.Activity;

public abstract class FragmentHostCallback<E> extends FragmentContainer
{
    private final Activity mActivity;
    private final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private final int mWindowAnimations;
    
    FragmentHostCallback(final Activity mActivity, final Context context, final Handler handler, final int mWindowAnimations) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = mActivity;
        this.mContext = Preconditions.checkNotNull(context, "context == null");
        this.mHandler = Preconditions.checkNotNull(handler, "handler == null");
        this.mWindowAnimations = mWindowAnimations;
    }
    
    FragmentHostCallback(final FragmentActivity fragmentActivity) {
        this(fragmentActivity, (Context)fragmentActivity, fragmentActivity.mHandler, 0);
    }
    
    Activity getActivity() {
        return this.mActivity;
    }
    
    Context getContext() {
        return this.mContext;
    }
    
    FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }
    
    Handler getHandler() {
        return this.mHandler;
    }
    
    void onAttachFragment(final Fragment fragment) {
    }
    
    public void onDump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
    }
    
    @Override
    public View onFindViewById(final int n) {
        return null;
    }
    
    public LayoutInflater onGetLayoutInflater() {
        return LayoutInflater.from(this.mContext);
    }
    
    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }
    
    @Override
    public boolean onHasView() {
        return true;
    }
    
    public boolean onHasWindowAnimations() {
        return true;
    }
    
    public void onRequestPermissionsFromFragment(final Fragment fragment, final String[] array, final int n) {
    }
    
    public boolean onShouldSaveFragmentState(final Fragment fragment) {
        return true;
    }
    
    public boolean onShouldShowRequestPermissionRationale(final String s) {
        return false;
    }
    
    public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n, final Bundle bundle) {
        if (n == -1) {
            this.mContext.startActivity(intent);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }
    
    public void onSupportInvalidateOptionsMenu() {
    }
}
