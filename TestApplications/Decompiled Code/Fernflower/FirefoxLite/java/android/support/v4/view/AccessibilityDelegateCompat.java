package android.support.v4.view;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

public class AccessibilityDelegateCompat {
   private static final AccessibilityDelegate DEFAULT_DELEGATE = new AccessibilityDelegate();
   private final AccessibilityDelegate mBridge = new AccessibilityDelegateCompat.AccessibilityDelegateAdapter(this);

   public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      return DEFAULT_DELEGATE.dispatchPopulateAccessibilityEvent(var1, var2);
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      if (VERSION.SDK_INT >= 16) {
         AccessibilityNodeProvider var2 = DEFAULT_DELEGATE.getAccessibilityNodeProvider(var1);
         if (var2 != null) {
            return new AccessibilityNodeProviderCompat(var2);
         }
      }

      return null;
   }

   AccessibilityDelegate getBridge() {
      return this.mBridge;
   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.onInitializeAccessibilityEvent(var1, var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      DEFAULT_DELEGATE.onInitializeAccessibilityNodeInfo(var1, var2.unwrap());
   }

   public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.onPopulateAccessibilityEvent(var1, var2);
   }

   public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
      return DEFAULT_DELEGATE.onRequestSendAccessibilityEvent(var1, var2, var3);
   }

   public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
      return VERSION.SDK_INT >= 16 ? DEFAULT_DELEGATE.performAccessibilityAction(var1, var2, var3) : false;
   }

   public void sendAccessibilityEvent(View var1, int var2) {
      DEFAULT_DELEGATE.sendAccessibilityEvent(var1, var2);
   }

   public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.sendAccessibilityEventUnchecked(var1, var2);
   }

   private static final class AccessibilityDelegateAdapter extends AccessibilityDelegate {
      private final AccessibilityDelegateCompat mCompat;

      AccessibilityDelegateAdapter(AccessibilityDelegateCompat var1) {
         this.mCompat = var1;
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         return this.mCompat.dispatchPopulateAccessibilityEvent(var1, var2);
      }

      public AccessibilityNodeProvider getAccessibilityNodeProvider(View var1) {
         AccessibilityNodeProviderCompat var2 = this.mCompat.getAccessibilityNodeProvider(var1);
         AccessibilityNodeProvider var3;
         if (var2 != null) {
            var3 = (AccessibilityNodeProvider)var2.getProvider();
         } else {
            var3 = null;
         }

         return var3;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         this.mCompat.onInitializeAccessibilityEvent(var1, var2);
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfo var2) {
         this.mCompat.onInitializeAccessibilityNodeInfo(var1, AccessibilityNodeInfoCompat.wrap(var2));
      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         this.mCompat.onPopulateAccessibilityEvent(var1, var2);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return this.mCompat.onRequestSendAccessibilityEvent(var1, var2, var3);
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return this.mCompat.performAccessibilityAction(var1, var2, var3);
      }

      public void sendAccessibilityEvent(View var1, int var2) {
         this.mCompat.sendAccessibilityEvent(var1, var2);
      }

      public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
         this.mCompat.sendAccessibilityEventUnchecked(var1, var2);
      }
   }
}
