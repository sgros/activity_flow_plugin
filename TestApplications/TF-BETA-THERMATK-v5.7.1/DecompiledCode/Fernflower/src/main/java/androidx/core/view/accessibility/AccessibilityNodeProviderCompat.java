package androidx.core.view.accessibility;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
   private final Object mProvider;

   public AccessibilityNodeProviderCompat() {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 19) {
         this.mProvider = new AccessibilityNodeProviderCompat.AccessibilityNodeProviderApi19(this);
      } else if (var1 >= 16) {
         this.mProvider = new AccessibilityNodeProviderCompat.AccessibilityNodeProviderApi16(this);
      } else {
         this.mProvider = null;
      }

   }

   public AccessibilityNodeProviderCompat(Object var1) {
      this.mProvider = var1;
   }

   public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
      return null;
   }

   public List findAccessibilityNodeInfosByText(String var1, int var2) {
      return null;
   }

   public AccessibilityNodeInfoCompat findFocus(int var1) {
      return null;
   }

   public Object getProvider() {
      return this.mProvider;
   }

   public boolean performAction(int var1, int var2, Bundle var3) {
      return false;
   }

   static class AccessibilityNodeProviderApi16 extends AccessibilityNodeProvider {
      final AccessibilityNodeProviderCompat mCompat;

      AccessibilityNodeProviderApi16(AccessibilityNodeProviderCompat var1) {
         this.mCompat = var1;
      }

      public AccessibilityNodeInfo createAccessibilityNodeInfo(int var1) {
         AccessibilityNodeInfoCompat var2 = this.mCompat.createAccessibilityNodeInfo(var1);
         return var2 == null ? null : var2.unwrap();
      }

      public List findAccessibilityNodeInfosByText(String var1, int var2) {
         List var3 = this.mCompat.findAccessibilityNodeInfosByText(var1, var2);
         if (var3 == null) {
            return null;
         } else {
            ArrayList var5 = new ArrayList();
            int var4 = var3.size();

            for(var2 = 0; var2 < var4; ++var2) {
               var5.add(((AccessibilityNodeInfoCompat)var3.get(var2)).unwrap());
            }

            return var5;
         }
      }

      public boolean performAction(int var1, int var2, Bundle var3) {
         return this.mCompat.performAction(var1, var2, var3);
      }
   }

   static class AccessibilityNodeProviderApi19 extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderApi16 {
      AccessibilityNodeProviderApi19(AccessibilityNodeProviderCompat var1) {
         super(var1);
      }

      public AccessibilityNodeInfo findFocus(int var1) {
         AccessibilityNodeInfoCompat var2 = super.mCompat.findFocus(var1);
         return var2 == null ? null : var2.unwrap();
      }
   }
}
