package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FragmentController {
   private final FragmentHostCallback mHost;

   private FragmentController(FragmentHostCallback var1) {
      this.mHost = var1;
   }

   public static FragmentController createController(FragmentHostCallback var0) {
      return new FragmentController(var0);
   }

   public void attachHost(Fragment var1) {
      this.mHost.mFragmentManager.attachController(this.mHost, this.mHost, var1);
   }

   public void dispatchActivityCreated() {
      this.mHost.mFragmentManager.dispatchActivityCreated();
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      this.mHost.mFragmentManager.dispatchConfigurationChanged(var1);
   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      return this.mHost.mFragmentManager.dispatchContextItemSelected(var1);
   }

   public void dispatchCreate() {
      this.mHost.mFragmentManager.dispatchCreate();
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(var1, var2);
   }

   public void dispatchDestroy() {
      this.mHost.mFragmentManager.dispatchDestroy();
   }

   public void dispatchLowMemory() {
      this.mHost.mFragmentManager.dispatchLowMemory();
   }

   public void dispatchMultiWindowModeChanged(boolean var1) {
      this.mHost.mFragmentManager.dispatchMultiWindowModeChanged(var1);
   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      return this.mHost.mFragmentManager.dispatchOptionsItemSelected(var1);
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      this.mHost.mFragmentManager.dispatchOptionsMenuClosed(var1);
   }

   public void dispatchPause() {
      this.mHost.mFragmentManager.dispatchPause();
   }

   public void dispatchPictureInPictureModeChanged(boolean var1) {
      this.mHost.mFragmentManager.dispatchPictureInPictureModeChanged(var1);
   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(var1);
   }

   public void dispatchResume() {
      this.mHost.mFragmentManager.dispatchResume();
   }

   public void dispatchStart() {
      this.mHost.mFragmentManager.dispatchStart();
   }

   public void dispatchStop() {
      this.mHost.mFragmentManager.dispatchStop();
   }

   public boolean execPendingActions() {
      return this.mHost.mFragmentManager.execPendingActions();
   }

   public Fragment findFragmentByWho(String var1) {
      return this.mHost.mFragmentManager.findFragmentByWho(var1);
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mHost.getFragmentManagerImpl();
   }

   public void noteStateNotSaved() {
      this.mHost.mFragmentManager.noteStateNotSaved();
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mHost.mFragmentManager.onCreateView(var1, var2, var3, var4);
   }

   public void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      this.mHost.mFragmentManager.restoreAllState(var1, var2);
   }

   public FragmentManagerNonConfig retainNestedNonConfig() {
      return this.mHost.mFragmentManager.retainNonConfig();
   }

   public Parcelable saveAllState() {
      return this.mHost.mFragmentManager.saveAllState();
   }
}
