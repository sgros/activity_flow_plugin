package android.support.v4.app;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;

public class FragmentActivity extends SupportActivity implements ViewModelStoreOwner, ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator {
   boolean mCreated;
   final FragmentController mFragments = FragmentController.createController(new FragmentActivity.HostCallbacks());
   final Handler mHandler = new Handler() {
      public void handleMessage(Message var1) {
         if (var1.what != 2) {
            super.handleMessage(var1);
         } else {
            FragmentActivity.this.onResumeFragments();
            FragmentActivity.this.mFragments.execPendingActions();
         }

      }
   };
   int mNextCandidateRequestIndex;
   SparseArrayCompat mPendingFragmentActivityResults;
   boolean mRequestedPermissionsFromFragment;
   boolean mResumed;
   boolean mStartedActivityFromFragment;
   boolean mStartedIntentSenderFromFragment;
   boolean mStopped = true;
   private ViewModelStore mViewModelStore;

   private int allocateRequestIndex(Fragment var1) {
      if (this.mPendingFragmentActivityResults.size() >= 65534) {
         throw new IllegalStateException("Too many pending Fragment activity results.");
      } else {
         while(this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0) {
            this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         }

         int var2 = this.mNextCandidateRequestIndex;
         this.mPendingFragmentActivityResults.put(var2, var1.mWho);
         this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         return var2;
      }
   }

   static void checkForValidRequestCode(int var0) {
      if ((var0 & -65536) != 0) {
         throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
      }
   }

   private void markFragmentsCreated() {
      while(markState(this.getSupportFragmentManager(), Lifecycle.State.CREATED)) {
      }

   }

   private static boolean markState(FragmentManager var0, Lifecycle.State var1) {
      Iterator var5 = var0.getFragments().iterator();
      boolean var2 = false;

      while(var5.hasNext()) {
         Fragment var3 = (Fragment)var5.next();
         if (var3 != null) {
            boolean var4 = var2;
            if (var3.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
               var3.mLifecycleRegistry.markState(var1);
               var4 = true;
            }

            FragmentManager var6 = var3.peekChildFragmentManager();
            var2 = var4;
            if (var6 != null) {
               var2 = var4 | markState(var6, var1);
            }
         }
      }

      return var2;
   }

   final View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mFragments.onCreateView(var1, var2, var3, var4);
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      super.dump(var1, var2, var3, var4);
      var3.print(var1);
      var3.print("Local FragmentActivity ");
      var3.print(Integer.toHexString(System.identityHashCode(this)));
      var3.println(" State:");
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("  ");
      String var6 = var5.toString();
      var3.print(var6);
      var3.print("mCreated=");
      var3.print(this.mCreated);
      var3.print(" mResumed=");
      var3.print(this.mResumed);
      var3.print(" mStopped=");
      var3.print(this.mStopped);
      if (this.getApplication() != null) {
         LoaderManager.getInstance(this).dump(var6, var2, var3, var4);
      }

      this.mFragments.getSupportFragmentManager().dump(var1, var2, var3, var4);
   }

   public Lifecycle getLifecycle() {
      return super.getLifecycle();
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mFragments.getSupportFragmentManager();
   }

   public ViewModelStore getViewModelStore() {
      if (this.getApplication() != null) {
         if (this.mViewModelStore == null) {
            FragmentActivity.NonConfigurationInstances var1 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
            if (var1 != null) {
               this.mViewModelStore = var1.viewModelStore;
            }

            if (this.mViewModelStore == null) {
               this.mViewModelStore = new ViewModelStore();
            }
         }

         return this.mViewModelStore;
      } else {
         throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
      }
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      this.mFragments.noteStateNotSaved();
      int var4 = var1 >> 16;
      if (var4 != 0) {
         --var4;
         String var8 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if (var8 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
         } else {
            Fragment var6 = this.mFragments.findFragmentByWho(var8);
            if (var6 == null) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Activity result no fragment exists for who: ");
               var7.append(var8);
               Log.w("FragmentActivity", var7.toString());
            } else {
               var6.onActivityResult(var1 & '\uffff', var2, var3);
            }

         }
      } else {
         ActivityCompat.PermissionCompatDelegate var5 = ActivityCompat.getPermissionCompatDelegate();
         if (var5 == null || !var5.onActivityResult(this, var1, var2, var3)) {
            super.onActivityResult(var1, var2, var3);
         }
      }
   }

   public void onAttachFragment(Fragment var1) {
   }

   public void onBackPressed() {
      FragmentManager var1 = this.mFragments.getSupportFragmentManager();
      boolean var2 = var1.isStateSaved();
      if (!var2 || VERSION.SDK_INT > 25) {
         if (var2 || !var1.popBackStackImmediate()) {
            super.onBackPressed();
         }

      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mFragments.noteStateNotSaved();
      this.mFragments.dispatchConfigurationChanged(var1);
   }

   protected void onCreate(Bundle var1) {
      FragmentController var2 = this.mFragments;
      FragmentManagerNonConfig var3 = null;
      var2.attachHost((Fragment)null);
      super.onCreate(var1);
      FragmentActivity.NonConfigurationInstances var4 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      if (var4 != null && var4.viewModelStore != null && this.mViewModelStore == null) {
         this.mViewModelStore = var4.viewModelStore;
      }

      if (var1 != null) {
         Parcelable var5 = var1.getParcelable("android:support:fragments");
         var2 = this.mFragments;
         if (var4 != null) {
            var3 = var4.fragments;
         }

         var2.restoreAllState(var5, var3);
         if (var1.containsKey("android:support:next_request_index")) {
            this.mNextCandidateRequestIndex = var1.getInt("android:support:next_request_index");
            int[] var8 = var1.getIntArray("android:support:request_indicies");
            String[] var7 = var1.getStringArray("android:support:request_fragment_who");
            if (var8 != null && var7 != null && var8.length == var7.length) {
               this.mPendingFragmentActivityResults = new SparseArrayCompat(var8.length);

               for(int var6 = 0; var6 < var8.length; ++var6) {
                  this.mPendingFragmentActivityResults.put(var8[var6], var7[var6]);
               }
            } else {
               Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
            }
         }
      }

      if (this.mPendingFragmentActivityResults == null) {
         this.mPendingFragmentActivityResults = new SparseArrayCompat();
         this.mNextCandidateRequestIndex = 0;
      }

      this.mFragments.dispatchCreate();
   }

   public boolean onCreatePanelMenu(int var1, Menu var2) {
      return var1 == 0 ? super.onCreatePanelMenu(var1, var2) | this.mFragments.dispatchCreateOptionsMenu(var2, this.getMenuInflater()) : super.onCreatePanelMenu(var1, var2);
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      View var5 = this.dispatchFragmentsOnCreateView(var1, var2, var3, var4);
      return var5 == null ? super.onCreateView(var1, var2, var3, var4) : var5;
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      View var4 = this.dispatchFragmentsOnCreateView((View)null, var1, var2, var3);
      return var4 == null ? super.onCreateView(var1, var2, var3) : var4;
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

   public boolean onMenuItemSelected(int var1, MenuItem var2) {
      if (super.onMenuItemSelected(var1, var2)) {
         return true;
      } else if (var1 != 0) {
         return var1 != 6 ? false : this.mFragments.dispatchContextItemSelected(var2);
      } else {
         return this.mFragments.dispatchOptionsItemSelected(var2);
      }
   }

   public void onMultiWindowModeChanged(boolean var1) {
      this.mFragments.dispatchMultiWindowModeChanged(var1);
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.mFragments.noteStateNotSaved();
   }

   public void onPanelClosed(int var1, Menu var2) {
      if (var1 == 0) {
         this.mFragments.dispatchOptionsMenuClosed(var2);
      }

      super.onPanelClosed(var1, var2);
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

   public void onPictureInPictureModeChanged(boolean var1) {
      this.mFragments.dispatchPictureInPictureModeChanged(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.mHandler.removeMessages(2);
      this.onResumeFragments();
      this.mFragments.execPendingActions();
   }

   protected boolean onPrepareOptionsPanel(View var1, Menu var2) {
      return super.onPreparePanel(0, var1, var2);
   }

   public boolean onPreparePanel(int var1, View var2, Menu var3) {
      return var1 == 0 && var3 != null ? this.onPrepareOptionsPanel(var2, var3) | this.mFragments.dispatchPrepareOptionsMenu(var3) : super.onPreparePanel(var1, var2, var3);
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      this.mFragments.noteStateNotSaved();
      int var4 = var1 >> 16 & '\uffff';
      if (var4 != 0) {
         --var4;
         String var5 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if (var5 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
            return;
         }

         Fragment var6 = this.mFragments.findFragmentByWho(var5);
         if (var6 == null) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Activity result no fragment exists for who: ");
            var7.append(var5);
            Log.w("FragmentActivity", var7.toString());
         } else {
            var6.onRequestPermissionsResult(var1 & '\uffff', var2, var3);
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
      Object var1 = this.onRetainCustomNonConfigurationInstance();
      FragmentManagerNonConfig var2 = this.mFragments.retainNestedNonConfig();
      if (var2 == null && this.mViewModelStore == null && var1 == null) {
         return null;
      } else {
         FragmentActivity.NonConfigurationInstances var3 = new FragmentActivity.NonConfigurationInstances();
         var3.custom = var1;
         var3.viewModelStore = this.mViewModelStore;
         var3.fragments = var2;
         return var3;
      }
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      this.markFragmentsCreated();
      Parcelable var2 = this.mFragments.saveAllState();
      if (var2 != null) {
         var1.putParcelable("android:support:fragments", var2);
      }

      if (this.mPendingFragmentActivityResults.size() > 0) {
         var1.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
         int[] var5 = new int[this.mPendingFragmentActivityResults.size()];
         String[] var3 = new String[this.mPendingFragmentActivityResults.size()];

         for(int var4 = 0; var4 < this.mPendingFragmentActivityResults.size(); ++var4) {
            var5[var4] = this.mPendingFragmentActivityResults.keyAt(var4);
            var3[var4] = (String)this.mPendingFragmentActivityResults.valueAt(var4);
         }

         var1.putIntArray("android:support:request_indicies", var5);
         var1.putStringArray("android:support:request_fragment_who", var3);
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

   void requestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
      if (var3 == -1) {
         ActivityCompat.requestPermissions(this, var2, var3);
      } else {
         checkForValidRequestCode(var3);

         try {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + (var3 & '\uffff'));
         } finally {
            this.mRequestedPermissionsFromFragment = false;
         }

      }
   }

   public void startActivityForResult(Intent var1, int var2) {
      if (!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2);
   }

   public void startActivityForResult(Intent var1, int var2, Bundle var3) {
      if (!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2, var3);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3, Bundle var4) {
      this.mStartedActivityFromFragment = true;
      Throwable var10000;
      boolean var10001;
      if (var3 == -1) {
         label56: {
            try {
               ActivityCompat.startActivityForResult(this, var2, -1, var4);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label56;
            }

            this.mStartedActivityFromFragment = false;
            return;
         }
      } else {
         label59: {
            try {
               checkForValidRequestCode(var3);
               ActivityCompat.startActivityForResult(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + (var3 & '\uffff'), var4);
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label59;
            }

            this.mStartedActivityFromFragment = false;
            return;
         }
      }

      Throwable var11 = var10000;
      this.mStartedActivityFromFragment = false;
      throw var11;
   }

   public void startIntentSenderForResult(IntentSender var1, int var2, Intent var3, int var4, int var5, int var6) throws SendIntentException {
      if (!this.mStartedIntentSenderFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startIntentSenderForResult(var1, var2, var3, var4, var5, var6);
   }

   public void startIntentSenderForResult(IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      if (!this.mStartedIntentSenderFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startIntentSenderForResult(var1, var2, var3, var4, var5, var6, var7);
   }

   @Deprecated
   public void supportInvalidateOptionsMenu() {
      this.invalidateOptionsMenu();
   }

   public final void validateRequestPermissionsRequestCode(int var1) {
      if (!this.mRequestedPermissionsFromFragment && var1 != -1) {
         checkForValidRequestCode(var1);
      }

   }

   class HostCallbacks extends FragmentHostCallback {
      public HostCallbacks() {
         super(FragmentActivity.this);
      }

      public void onAttachFragment(Fragment var1) {
         FragmentActivity.this.onAttachFragment(var1);
      }

      public void onDump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         FragmentActivity.this.dump(var1, var2, var3, var4);
      }

      public View onFindViewById(int var1) {
         return FragmentActivity.this.findViewById(var1);
      }

      public LayoutInflater onGetLayoutInflater() {
         return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
      }

      public int onGetWindowAnimations() {
         Window var1 = FragmentActivity.this.getWindow();
         int var2;
         if (var1 == null) {
            var2 = 0;
         } else {
            var2 = var1.getAttributes().windowAnimations;
         }

         return var2;
      }

      public boolean onHasView() {
         Window var1 = FragmentActivity.this.getWindow();
         boolean var2;
         if (var1 != null && var1.peekDecorView() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean onHasWindowAnimations() {
         boolean var1;
         if (FragmentActivity.this.getWindow() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onRequestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
         FragmentActivity.this.requestPermissionsFromFragment(var1, var2, var3);
      }

      public boolean onShouldSaveFragmentState(Fragment var1) {
         return FragmentActivity.this.isFinishing() ^ true;
      }

      public boolean onShouldShowRequestPermissionRationale(String var1) {
         return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, var1);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3, Bundle var4) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3, var4);
      }

      public void onSupportInvalidateOptionsMenu() {
         FragmentActivity.this.supportInvalidateOptionsMenu();
      }
   }

   static final class NonConfigurationInstances {
      Object custom;
      FragmentManagerNonConfig fragments;
      ViewModelStore viewModelStore;
   }
}
