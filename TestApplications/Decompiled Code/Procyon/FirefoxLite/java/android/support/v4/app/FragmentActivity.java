// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.view.Window;
import android.view.LayoutInflater;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.view.MenuItem;
import android.view.Menu;
import android.os.Parcelable;
import android.os.Bundle;
import android.content.res.Configuration;
import android.os.Build$VERSION;
import android.app.Activity;
import android.util.Log;
import android.content.Intent;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.util.Iterator;
import android.arch.lifecycle.Lifecycle;
import android.os.Message;
import android.arch.lifecycle.ViewModelStore;
import android.support.v4.util.SparseArrayCompat;
import android.os.Handler;
import android.arch.lifecycle.ViewModelStoreOwner;

public class FragmentActivity extends SupportActivity implements ViewModelStoreOwner, OnRequestPermissionsResultCallback, RequestPermissionsRequestCodeValidator
{
    boolean mCreated;
    final FragmentController mFragments;
    final Handler mHandler;
    int mNextCandidateRequestIndex;
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    boolean mRequestedPermissionsFromFragment;
    boolean mResumed;
    boolean mStartedActivityFromFragment;
    boolean mStartedIntentSenderFromFragment;
    boolean mStopped;
    private ViewModelStore mViewModelStore;
    
    public FragmentActivity() {
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                if (message.what != 2) {
                    super.handleMessage(message);
                }
                else {
                    FragmentActivity.this.onResumeFragments();
                    FragmentActivity.this.mFragments.execPendingActions();
                }
            }
        };
        this.mFragments = FragmentController.createController(new HostCallbacks());
        this.mStopped = true;
    }
    
    private int allocateRequestIndex(final Fragment fragment) {
        if (this.mPendingFragmentActivityResults.size() < 65534) {
            while (this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0) {
                this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % 65534;
            }
            final int mNextCandidateRequestIndex = this.mNextCandidateRequestIndex;
            this.mPendingFragmentActivityResults.put(mNextCandidateRequestIndex, fragment.mWho);
            this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % 65534;
            return mNextCandidateRequestIndex;
        }
        throw new IllegalStateException("Too many pending Fragment activity results.");
    }
    
    static void checkForValidRequestCode(final int n) {
        if ((n & 0xFFFF0000) == 0x0) {
            return;
        }
        throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
    }
    
    private void markFragmentsCreated() {
        while (markState(this.getSupportFragmentManager(), Lifecycle.State.CREATED)) {}
    }
    
    private static boolean markState(final FragmentManager fragmentManager, final Lifecycle.State state) {
        final Iterator<Fragment> iterator = fragmentManager.getFragments().iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final Fragment fragment = iterator.next();
            if (fragment == null) {
                continue;
            }
            boolean b2 = b;
            if (fragment.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                fragment.mLifecycleRegistry.markState(state);
                b2 = true;
            }
            final FragmentManager peekChildFragmentManager = fragment.peekChildFragmentManager();
            b = b2;
            if (peekChildFragmentManager == null) {
                continue;
            }
            b = (b2 | markState(peekChildFragmentManager, state));
        }
        return b;
    }
    
    final View dispatchFragmentsOnCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        return this.mFragments.onCreateView(view, s, context, set);
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        super.dump(s, fileDescriptor, printWriter, array);
        printWriter.print(s);
        printWriter.print("Local FragmentActivity ");
        printWriter.print(Integer.toHexString(System.identityHashCode(this)));
        printWriter.println(" State:");
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("  ");
        final String string = sb.toString();
        printWriter.print(string);
        printWriter.print("mCreated=");
        printWriter.print(this.mCreated);
        printWriter.print(" mResumed=");
        printWriter.print(this.mResumed);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        if (this.getApplication() != null) {
            LoaderManager.getInstance(this).dump(string, fileDescriptor, printWriter, array);
        }
        this.mFragments.getSupportFragmentManager().dump(s, fileDescriptor, printWriter, array);
    }
    
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }
    
    public FragmentManager getSupportFragmentManager() {
        return this.mFragments.getSupportFragmentManager();
    }
    
    @Override
    public ViewModelStore getViewModelStore() {
        if (this.getApplication() != null) {
            if (this.mViewModelStore == null) {
                final NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)this.getLastNonConfigurationInstance();
                if (nonConfigurationInstances != null) {
                    this.mViewModelStore = nonConfigurationInstances.viewModelStore;
                }
                if (this.mViewModelStore == null) {
                    this.mViewModelStore = new ViewModelStore();
                }
            }
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        this.mFragments.noteStateNotSaved();
        int n3 = n >> 16;
        if (n3 != 0) {
            --n3;
            final String str = this.mPendingFragmentActivityResults.get(n3);
            this.mPendingFragmentActivityResults.remove(n3);
            if (str == null) {
                Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
                return;
            }
            final Fragment fragmentByWho = this.mFragments.findFragmentByWho(str);
            if (fragmentByWho == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Activity result no fragment exists for who: ");
                sb.append(str);
                Log.w("FragmentActivity", sb.toString());
            }
            else {
                fragmentByWho.onActivityResult(n & 0xFFFF, n2, intent);
            }
        }
        else {
            final ActivityCompat.PermissionCompatDelegate permissionCompatDelegate = ActivityCompat.getPermissionCompatDelegate();
            if (permissionCompatDelegate != null && permissionCompatDelegate.onActivityResult(this, n, n2, intent)) {
                return;
            }
            super.onActivityResult(n, n2, intent);
        }
    }
    
    public void onAttachFragment(final Fragment fragment) {
    }
    
    public void onBackPressed() {
        final FragmentManager supportFragmentManager = this.mFragments.getSupportFragmentManager();
        final boolean stateSaved = supportFragmentManager.isStateSaved();
        if (stateSaved && Build$VERSION.SDK_INT <= 25) {
            return;
        }
        if (stateSaved || !supportFragmentManager.popBackStackImmediate()) {
            super.onBackPressed();
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFragments.noteStateNotSaved();
        this.mFragments.dispatchConfigurationChanged(configuration);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        final FragmentController mFragments = this.mFragments;
        FragmentManagerNonConfig fragments = null;
        mFragments.attachHost(null);
        super.onCreate(bundle);
        final NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)this.getLastNonConfigurationInstance();
        if (nonConfigurationInstances != null && nonConfigurationInstances.viewModelStore != null && this.mViewModelStore == null) {
            this.mViewModelStore = nonConfigurationInstances.viewModelStore;
        }
        if (bundle != null) {
            final Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            final FragmentController mFragments2 = this.mFragments;
            if (nonConfigurationInstances != null) {
                fragments = nonConfigurationInstances.fragments;
            }
            mFragments2.restoreAllState(parcelable, fragments);
            if (bundle.containsKey("android:support:next_request_index")) {
                this.mNextCandidateRequestIndex = bundle.getInt("android:support:next_request_index");
                final int[] intArray = bundle.getIntArray("android:support:request_indicies");
                final String[] stringArray = bundle.getStringArray("android:support:request_fragment_who");
                if (intArray != null && stringArray != null && intArray.length == stringArray.length) {
                    this.mPendingFragmentActivityResults = new SparseArrayCompat<String>(intArray.length);
                    for (int i = 0; i < intArray.length; ++i) {
                        this.mPendingFragmentActivityResults.put(intArray[i], stringArray[i]);
                    }
                }
                else {
                    Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
                }
            }
        }
        if (this.mPendingFragmentActivityResults == null) {
            this.mPendingFragmentActivityResults = new SparseArrayCompat<String>();
            this.mNextCandidateRequestIndex = 0;
        }
        this.mFragments.dispatchCreate();
    }
    
    public boolean onCreatePanelMenu(final int n, final Menu menu) {
        if (n == 0) {
            return super.onCreatePanelMenu(n, menu) | this.mFragments.dispatchCreateOptionsMenu(menu, this.getMenuInflater());
        }
        return super.onCreatePanelMenu(n, menu);
    }
    
    public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        final View dispatchFragmentsOnCreateView = this.dispatchFragmentsOnCreateView(view, s, context, set);
        if (dispatchFragmentsOnCreateView == null) {
            return super.onCreateView(view, s, context, set);
        }
        return dispatchFragmentsOnCreateView;
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        final View dispatchFragmentsOnCreateView = this.dispatchFragmentsOnCreateView(null, s, context, set);
        if (dispatchFragmentsOnCreateView == null) {
            return super.onCreateView(s, context, set);
        }
        return dispatchFragmentsOnCreateView;
    }
    
    protected void onDestroy() {
        super.onDestroy();
        if (this.mViewModelStore != null && !this.isChangingConfigurations()) {
            this.mViewModelStore.clear();
        }
        this.mFragments.dispatchDestroy();
    }
    
    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.dispatchLowMemory();
    }
    
    public boolean onMenuItemSelected(final int n, final MenuItem menuItem) {
        if (super.onMenuItemSelected(n, menuItem)) {
            return true;
        }
        if (n != 0) {
            return n == 6 && this.mFragments.dispatchContextItemSelected(menuItem);
        }
        return this.mFragments.dispatchOptionsItemSelected(menuItem);
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
        this.mFragments.dispatchMultiWindowModeChanged(b);
    }
    
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.mFragments.noteStateNotSaved();
    }
    
    public void onPanelClosed(final int n, final Menu menu) {
        if (n == 0) {
            this.mFragments.dispatchOptionsMenuClosed(menu);
        }
        super.onPanelClosed(n, menu);
    }
    
    protected void onPause() {
        super.onPause();
        this.mResumed = false;
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
            this.onResumeFragments();
        }
        this.mFragments.dispatchPause();
    }
    
    public void onPictureInPictureModeChanged(final boolean b) {
        this.mFragments.dispatchPictureInPictureModeChanged(b);
    }
    
    protected void onPostResume() {
        super.onPostResume();
        this.mHandler.removeMessages(2);
        this.onResumeFragments();
        this.mFragments.execPendingActions();
    }
    
    protected boolean onPrepareOptionsPanel(final View view, final Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }
    
    public boolean onPreparePanel(final int n, final View view, final Menu menu) {
        if (n == 0 && menu != null) {
            return this.onPrepareOptionsPanel(view, menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
        }
        return super.onPreparePanel(n, view, menu);
    }
    
    @Override
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        this.mFragments.noteStateNotSaved();
        int n2 = n >> 16 & 0xFFFF;
        if (n2 != 0) {
            --n2;
            final String str = this.mPendingFragmentActivityResults.get(n2);
            this.mPendingFragmentActivityResults.remove(n2);
            if (str == null) {
                Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
                return;
            }
            final Fragment fragmentByWho = this.mFragments.findFragmentByWho(str);
            if (fragmentByWho == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Activity result no fragment exists for who: ");
                sb.append(str);
                Log.w("FragmentActivity", sb.toString());
            }
            else {
                fragmentByWho.onRequestPermissionsResult(n & 0xFFFF, array, array2);
            }
        }
    }
    
    protected void onResume() {
        super.onResume();
        this.mHandler.sendEmptyMessage(2);
        this.mResumed = true;
        this.mFragments.execPendingActions();
    }
    
    protected void onResumeFragments() {
        this.mFragments.dispatchResume();
    }
    
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }
    
    public final Object onRetainNonConfigurationInstance() {
        final Object onRetainCustomNonConfigurationInstance = this.onRetainCustomNonConfigurationInstance();
        final FragmentManagerNonConfig retainNestedNonConfig = this.mFragments.retainNestedNonConfig();
        if (retainNestedNonConfig == null && this.mViewModelStore == null && onRetainCustomNonConfigurationInstance == null) {
            return null;
        }
        final NonConfigurationInstances nonConfigurationInstances = new NonConfigurationInstances();
        nonConfigurationInstances.custom = onRetainCustomNonConfigurationInstance;
        nonConfigurationInstances.viewModelStore = this.mViewModelStore;
        nonConfigurationInstances.fragments = retainNestedNonConfig;
        return nonConfigurationInstances;
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.markFragmentsCreated();
        final Parcelable saveAllState = this.mFragments.saveAllState();
        if (saveAllState != null) {
            bundle.putParcelable("android:support:fragments", saveAllState);
        }
        if (this.mPendingFragmentActivityResults.size() > 0) {
            bundle.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
            final int[] array = new int[this.mPendingFragmentActivityResults.size()];
            final String[] array2 = new String[this.mPendingFragmentActivityResults.size()];
            for (int i = 0; i < this.mPendingFragmentActivityResults.size(); ++i) {
                array[i] = this.mPendingFragmentActivityResults.keyAt(i);
                array2[i] = this.mPendingFragmentActivityResults.valueAt(i);
            }
            bundle.putIntArray("android:support:request_indicies", array);
            bundle.putStringArray("android:support:request_fragment_who", array2);
        }
    }
    
    protected void onStart() {
        super.onStart();
        this.mStopped = false;
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.dispatchActivityCreated();
        }
        this.mFragments.noteStateNotSaved();
        this.mFragments.execPendingActions();
        this.mFragments.dispatchStart();
    }
    
    public void onStateNotSaved() {
        this.mFragments.noteStateNotSaved();
    }
    
    protected void onStop() {
        super.onStop();
        this.mStopped = true;
        this.markFragmentsCreated();
        this.mFragments.dispatchStop();
    }
    
    void requestPermissionsFromFragment(final Fragment fragment, final String[] array, final int n) {
        if (n == -1) {
            ActivityCompat.requestPermissions(this, array, n);
            return;
        }
        checkForValidRequestCode(n);
        try {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, array, (this.allocateRequestIndex(fragment) + 1 << 16) + (n & 0xFFFF));
        }
        finally {
            this.mRequestedPermissionsFromFragment = false;
        }
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        if (!this.mStartedActivityFromFragment && n != -1) {
            checkForValidRequestCode(n);
        }
        super.startActivityForResult(intent, n);
    }
    
    public void startActivityForResult(final Intent intent, final int n, final Bundle bundle) {
        if (!this.mStartedActivityFromFragment && n != -1) {
            checkForValidRequestCode(n);
        }
        super.startActivityForResult(intent, n, bundle);
    }
    
    public void startActivityFromFragment(final Fragment fragment, final Intent intent, final int n, final Bundle bundle) {
        this.mStartedActivityFromFragment = true;
        Label_0028: {
            if (n == -1) {
                Label_0061: {
                    try {
                        ActivityCompat.startActivityForResult(this, intent, -1, bundle);
                        this.mStartedActivityFromFragment = false;
                        return;
                    }
                    finally {
                        break Label_0061;
                    }
                    break Label_0028;
                }
                this.mStartedActivityFromFragment = false;
            }
        }
        checkForValidRequestCode(n);
        ActivityCompat.startActivityForResult(this, intent, (this.allocateRequestIndex(fragment) + 1 << 16) + (n & 0xFFFF), bundle);
        this.mStartedActivityFromFragment = false;
    }
    
    public void startIntentSenderForResult(final IntentSender intentSender, final int n, final Intent intent, final int n2, final int n3, final int n4) throws IntentSender$SendIntentException {
        if (!this.mStartedIntentSenderFromFragment && n != -1) {
            checkForValidRequestCode(n);
        }
        super.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
    }
    
    public void startIntentSenderForResult(final IntentSender intentSender, final int n, final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        if (!this.mStartedIntentSenderFromFragment && n != -1) {
            checkForValidRequestCode(n);
        }
        super.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
    }
    
    @Deprecated
    public void supportInvalidateOptionsMenu() {
        this.invalidateOptionsMenu();
    }
    
    @Override
    public final void validateRequestPermissionsRequestCode(final int n) {
        if (!this.mRequestedPermissionsFromFragment && n != -1) {
            checkForValidRequestCode(n);
        }
    }
    
    class HostCallbacks extends FragmentHostCallback<FragmentActivity>
    {
        public HostCallbacks() {
            super(FragmentActivity.this);
        }
        
        public void onAttachFragment(final Fragment fragment) {
            FragmentActivity.this.onAttachFragment(fragment);
        }
        
        @Override
        public void onDump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            FragmentActivity.this.dump(s, fileDescriptor, printWriter, array);
        }
        
        @Override
        public View onFindViewById(final int n) {
            return FragmentActivity.this.findViewById(n);
        }
        
        @Override
        public LayoutInflater onGetLayoutInflater() {
            return FragmentActivity.this.getLayoutInflater().cloneInContext((Context)FragmentActivity.this);
        }
        
        @Override
        public int onGetWindowAnimations() {
            final Window window = FragmentActivity.this.getWindow();
            int windowAnimations;
            if (window == null) {
                windowAnimations = 0;
            }
            else {
                windowAnimations = window.getAttributes().windowAnimations;
            }
            return windowAnimations;
        }
        
        @Override
        public boolean onHasView() {
            final Window window = FragmentActivity.this.getWindow();
            return window != null && window.peekDecorView() != null;
        }
        
        @Override
        public boolean onHasWindowAnimations() {
            return FragmentActivity.this.getWindow() != null;
        }
        
        @Override
        public void onRequestPermissionsFromFragment(final Fragment fragment, final String[] array, final int n) {
            FragmentActivity.this.requestPermissionsFromFragment(fragment, array, n);
        }
        
        @Override
        public boolean onShouldSaveFragmentState(final Fragment fragment) {
            return FragmentActivity.this.isFinishing() ^ true;
        }
        
        @Override
        public boolean onShouldShowRequestPermissionRationale(final String s) {
            return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, s);
        }
        
        @Override
        public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n, final Bundle bundle) {
            FragmentActivity.this.startActivityFromFragment(fragment, intent, n, bundle);
        }
        
        @Override
        public void onSupportInvalidateOptionsMenu() {
            FragmentActivity.this.supportInvalidateOptionsMenu();
        }
    }
    
    static final class NonConfigurationInstances
    {
        Object custom;
        FragmentManagerNonConfig fragments;
        ViewModelStore viewModelStore;
    }
}
