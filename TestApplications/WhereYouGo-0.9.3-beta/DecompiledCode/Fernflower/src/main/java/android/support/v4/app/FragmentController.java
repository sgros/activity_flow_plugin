package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FragmentController {
   private final FragmentHostCallback mHost;

   private FragmentController(FragmentHostCallback var1) {
      this.mHost = var1;
   }

   public static final FragmentController createController(FragmentHostCallback var0) {
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

   public void dispatchDestroyView() {
      this.mHost.mFragmentManager.dispatchDestroyView();
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

   public void dispatchReallyStop() {
      this.mHost.mFragmentManager.dispatchReallyStop();
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

   public void doLoaderDestroy() {
      this.mHost.doLoaderDestroy();
   }

   public void doLoaderRetain() {
      this.mHost.doLoaderRetain();
   }

   public void doLoaderStart() {
      this.mHost.doLoaderStart();
   }

   public void doLoaderStop(boolean var1) {
      this.mHost.doLoaderStop(var1);
   }

   public void dumpLoaders(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      this.mHost.dumpLoaders(var1, var2, var3, var4);
   }

   public boolean execPendingActions() {
      return this.mHost.mFragmentManager.execPendingActions();
   }

   @Nullable
   public Fragment findFragmentByWho(String var1) {
      return this.mHost.mFragmentManager.findFragmentByWho(var1);
   }

   public List getActiveFragments(List var1) {
      Object var3;
      if (this.mHost.mFragmentManager.mActive == null) {
         var3 = null;
      } else {
         Object var2 = var1;
         if (var1 == null) {
            var2 = new ArrayList(this.getActiveFragmentsCount());
         }

         ((List)var2).addAll(this.mHost.mFragmentManager.mActive);
         var3 = var2;
      }

      return (List)var3;
   }

   public int getActiveFragmentsCount() {
      ArrayList var1 = this.mHost.mFragmentManager.mActive;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.size();
      }

      return var2;
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mHost.getFragmentManagerImpl();
   }

   public LoaderManager getSupportLoaderManager() {
      return this.mHost.getLoaderManagerImpl();
   }

   public void noteStateNotSaved() {
      this.mHost.mFragmentManager.noteStateNotSaved();
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mHost.mFragmentManager.onCreateView(var1, var2, var3, var4);
   }

   public void reportLoaderStart() {
      this.mHost.reportLoaderStart();
   }

   public void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      this.mHost.mFragmentManager.restoreAllState(var1, var2);
   }

   @Deprecated
   public void restoreAllState(Parcelable var1, List var2) {
      this.mHost.mFragmentManager.restoreAllState(var1, new FragmentManagerNonConfig(var2, (List)null));
   }

   public void restoreLoaderNonConfig(SimpleArrayMap var1) {
      this.mHost.restoreLoaderNonConfig(var1);
   }

   public SimpleArrayMap retainLoaderNonConfig() {
      return this.mHost.retainLoaderNonConfig();
   }

   public FragmentManagerNonConfig retainNestedNonConfig() {
      return this.mHost.mFragmentManager.retainNonConfig();
   }

   @Deprecated
   public List retainNonConfig() {
      FragmentManagerNonConfig var1 = this.mHost.mFragmentManager.retainNonConfig();
      List var2;
      if (var1 != null) {
         var2 = var1.getFragments();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Parcelable saveAllState() {
      return this.mHost.mFragmentManager.saveAllState();
   }
}
