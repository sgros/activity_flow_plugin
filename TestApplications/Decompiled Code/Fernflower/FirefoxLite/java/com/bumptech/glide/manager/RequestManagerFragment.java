package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;

public class RequestManagerFragment extends Fragment {
   private final HashSet childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private Fragment parentFragmentHint;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private RequestManagerFragment rootRequestManagerFragment;

   public RequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   @SuppressLint({"ValidFragment"})
   RequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new RequestManagerFragment.FragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   @TargetApi(17)
   private Fragment getParentFragmentUsingHint() {
      Fragment var1;
      if (VERSION.SDK_INT >= 17) {
         var1 = this.getParentFragment();
      } else {
         var1 = null;
      }

      if (var1 == null) {
         var1 = this.parentFragmentHint;
      }

      return var1;
   }

   private void registerFragmentWithRoot(Activity var1) {
      this.unregisterFragmentWithRoot();
      this.rootRequestManagerFragment = Glide.get(var1).getRequestManagerRetriever().getRequestManagerFragment(var1.getFragmentManager(), (Fragment)null);
      if (this.rootRequestManagerFragment != this) {
         this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
      }

   }

   private void removeChildRequestManagerFragment(RequestManagerFragment var1) {
      this.childRequestManagerFragments.remove(var1);
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

   public void onAttach(Activity var1) {
      super.onAttach(var1);

      try {
         this.registerFragmentWithRoot(var1);
      } catch (IllegalStateException var2) {
         if (Log.isLoggable("RMFragment", 5)) {
            Log.w("RMFragment", "Unable to register fragment with root", var2);
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

   void setParentFragmentHint(Fragment var1) {
      this.parentFragmentHint = var1;
      if (var1 != null && var1.getActivity() != null) {
         this.registerFragmentWithRoot(var1.getActivity());
      }

   }

   public void setRequestManager(RequestManager var1) {
      this.requestManager = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("{parent=");
      var1.append(this.getParentFragmentUsingHint());
      var1.append("}");
      return var1.toString();
   }

   private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      FragmentRequestManagerTreeNode() {
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append("{fragment=");
         var1.append(RequestManagerFragment.this);
         var1.append("}");
         return var1.toString();
      }
   }
}
