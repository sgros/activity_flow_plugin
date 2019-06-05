package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;

public class SupportRequestManagerFragment extends Fragment {
   private final HashSet childRequestManagerFragments;
   private final ActivityFragmentLifecycle lifecycle;
   private Fragment parentFragmentHint;
   private RequestManager requestManager;
   private final RequestManagerTreeNode requestManagerTreeNode;
   private SupportRequestManagerFragment rootRequestManagerFragment;

   public SupportRequestManagerFragment() {
      this(new ActivityFragmentLifecycle());
   }

   @SuppressLint({"ValidFragment"})
   public SupportRequestManagerFragment(ActivityFragmentLifecycle var1) {
      this.requestManagerTreeNode = new SupportRequestManagerFragment.SupportFragmentRequestManagerTreeNode();
      this.childRequestManagerFragments = new HashSet();
      this.lifecycle = var1;
   }

   private void addChildRequestManagerFragment(SupportRequestManagerFragment var1) {
      this.childRequestManagerFragments.add(var1);
   }

   private Fragment getParentFragmentUsingHint() {
      Fragment var1 = this.getParentFragment();
      if (var1 == null) {
         var1 = this.parentFragmentHint;
      }

      return var1;
   }

   private void registerFragmentWithRoot(FragmentActivity var1) {
      this.unregisterFragmentWithRoot();
      this.rootRequestManagerFragment = Glide.get(var1).getRequestManagerRetriever().getSupportRequestManagerFragment(var1.getSupportFragmentManager(), (Fragment)null);
      if (this.rootRequestManagerFragment != this) {
         this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
      }

   }

   private void removeChildRequestManagerFragment(SupportRequestManagerFragment var1) {
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

   public void onAttach(Context var1) {
      super.onAttach(var1);

      try {
         this.registerFragmentWithRoot(this.getActivity());
      } catch (IllegalStateException var2) {
         if (Log.isLoggable("SupportRMFragment", 5)) {
            Log.w("SupportRMFragment", "Unable to register fragment with root", var2);
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
      this.parentFragmentHint = null;
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

   private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
      SupportFragmentRequestManagerTreeNode() {
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append("{fragment=");
         var1.append(SupportRequestManagerFragment.this);
         var1.append("}");
         return var1.toString();
      }
   }
}
