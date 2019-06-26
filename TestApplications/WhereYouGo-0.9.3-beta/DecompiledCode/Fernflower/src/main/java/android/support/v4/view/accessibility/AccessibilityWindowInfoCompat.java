package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;

public class AccessibilityWindowInfoCompat {
   private static final AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl IMPL;
   public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
   public static final int TYPE_APPLICATION = 1;
   public static final int TYPE_INPUT_METHOD = 2;
   public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
   public static final int TYPE_SYSTEM = 3;
   private static final int UNDEFINED = -1;
   private Object mInfo;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi24Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi21Impl();
      } else {
         IMPL = new AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl();
      }

   }

   private AccessibilityWindowInfoCompat(Object var1) {
      this.mInfo = var1;
   }

   public static AccessibilityWindowInfoCompat obtain() {
      return wrapNonNullInstance(IMPL.obtain());
   }

   public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat var0) {
      if (var0 == null) {
         var0 = null;
      } else {
         var0 = wrapNonNullInstance(IMPL.obtain(var0.mInfo));
      }

      return var0;
   }

   private static String typeToString(int var0) {
      String var1;
      switch(var0) {
      case 1:
         var1 = "TYPE_APPLICATION";
         break;
      case 2:
         var1 = "TYPE_INPUT_METHOD";
         break;
      case 3:
         var1 = "TYPE_SYSTEM";
         break;
      case 4:
         var1 = "TYPE_ACCESSIBILITY_OVERLAY";
         break;
      default:
         var1 = "<UNKNOWN>";
      }

      return var1;
   }

   static AccessibilityWindowInfoCompat wrapNonNullInstance(Object var0) {
      AccessibilityWindowInfoCompat var1;
      if (var0 != null) {
         var1 = new AccessibilityWindowInfoCompat(var0);
      } else {
         var1 = null;
      }

      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (var1 == null) {
            var2 = false;
         } else if (this.getClass() != var1.getClass()) {
            var2 = false;
         } else {
            AccessibilityWindowInfoCompat var3 = (AccessibilityWindowInfoCompat)var1;
            if (this.mInfo == null) {
               if (var3.mInfo != null) {
                  var2 = false;
               }
            } else if (!this.mInfo.equals(var3.mInfo)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public AccessibilityNodeInfoCompat getAnchor() {
      return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getAnchor(this.mInfo));
   }

   public void getBoundsInScreen(Rect var1) {
      IMPL.getBoundsInScreen(this.mInfo, var1);
   }

   public AccessibilityWindowInfoCompat getChild(int var1) {
      return wrapNonNullInstance(IMPL.getChild(this.mInfo, var1));
   }

   public int getChildCount() {
      return IMPL.getChildCount(this.mInfo);
   }

   public int getId() {
      return IMPL.getId(this.mInfo);
   }

   public int getLayer() {
      return IMPL.getLayer(this.mInfo);
   }

   public AccessibilityWindowInfoCompat getParent() {
      return wrapNonNullInstance(IMPL.getParent(this.mInfo));
   }

   public AccessibilityNodeInfoCompat getRoot() {
      return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getRoot(this.mInfo));
   }

   public CharSequence getTitle() {
      return IMPL.getTitle(this.mInfo);
   }

   public int getType() {
      return IMPL.getType(this.mInfo);
   }

   public int hashCode() {
      int var1;
      if (this.mInfo == null) {
         var1 = 0;
      } else {
         var1 = this.mInfo.hashCode();
      }

      return var1;
   }

   public boolean isAccessibilityFocused() {
      return IMPL.isAccessibilityFocused(this.mInfo);
   }

   public boolean isActive() {
      return IMPL.isActive(this.mInfo);
   }

   public boolean isFocused() {
      return IMPL.isFocused(this.mInfo);
   }

   public void recycle() {
      IMPL.recycle(this.mInfo);
   }

   public String toString() {
      boolean var1 = true;
      StringBuilder var2 = new StringBuilder();
      Rect var3 = new Rect();
      this.getBoundsInScreen(var3);
      var2.append("AccessibilityWindowInfo[");
      var2.append("id=").append(this.getId());
      var2.append(", type=").append(typeToString(this.getType()));
      var2.append(", layer=").append(this.getLayer());
      var2.append(", bounds=").append(var3);
      var2.append(", focused=").append(this.isFocused());
      var2.append(", active=").append(this.isActive());
      StringBuilder var5 = var2.append(", hasParent=");
      boolean var4;
      if (this.getParent() != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      var5.append(var4);
      var5 = var2.append(", hasChildren=");
      if (this.getChildCount() > 0) {
         var4 = var1;
      } else {
         var4 = false;
      }

      var5.append(var4);
      var2.append(']');
      return var2.toString();
   }

   private static class AccessibilityWindowInfoApi21Impl extends AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl {
      AccessibilityWindowInfoApi21Impl() {
      }

      public void getBoundsInScreen(Object var1, Rect var2) {
         AccessibilityWindowInfoCompatApi21.getBoundsInScreen(var1, var2);
      }

      public Object getChild(Object var1, int var2) {
         return AccessibilityWindowInfoCompatApi21.getChild(var1, var2);
      }

      public int getChildCount(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getChildCount(var1);
      }

      public int getId(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getId(var1);
      }

      public int getLayer(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getLayer(var1);
      }

      public Object getParent(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getParent(var1);
      }

      public Object getRoot(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getRoot(var1);
      }

      public int getType(Object var1) {
         return AccessibilityWindowInfoCompatApi21.getType(var1);
      }

      public boolean isAccessibilityFocused(Object var1) {
         return AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(var1);
      }

      public boolean isActive(Object var1) {
         return AccessibilityWindowInfoCompatApi21.isActive(var1);
      }

      public boolean isFocused(Object var1) {
         return AccessibilityWindowInfoCompatApi21.isFocused(var1);
      }

      public Object obtain() {
         return AccessibilityWindowInfoCompatApi21.obtain();
      }

      public Object obtain(Object var1) {
         return AccessibilityWindowInfoCompatApi21.obtain(var1);
      }

      public void recycle(Object var1) {
         AccessibilityWindowInfoCompatApi21.recycle(var1);
      }
   }

   private static class AccessibilityWindowInfoApi24Impl extends AccessibilityWindowInfoCompat.AccessibilityWindowInfoApi21Impl {
      AccessibilityWindowInfoApi24Impl() {
      }

      public Object getAnchor(Object var1) {
         return AccessibilityWindowInfoCompatApi24.getAnchor(var1);
      }

      public CharSequence getTitle(Object var1) {
         return AccessibilityWindowInfoCompatApi24.getTitle(var1);
      }
   }

   private interface AccessibilityWindowInfoImpl {
      Object getAnchor(Object var1);

      void getBoundsInScreen(Object var1, Rect var2);

      Object getChild(Object var1, int var2);

      int getChildCount(Object var1);

      int getId(Object var1);

      int getLayer(Object var1);

      Object getParent(Object var1);

      Object getRoot(Object var1);

      CharSequence getTitle(Object var1);

      int getType(Object var1);

      boolean isAccessibilityFocused(Object var1);

      boolean isActive(Object var1);

      boolean isFocused(Object var1);

      Object obtain();

      Object obtain(Object var1);

      void recycle(Object var1);
   }

   private static class AccessibilityWindowInfoStubImpl implements AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl {
      AccessibilityWindowInfoStubImpl() {
      }

      public Object getAnchor(Object var1) {
         return null;
      }

      public void getBoundsInScreen(Object var1, Rect var2) {
      }

      public Object getChild(Object var1, int var2) {
         return null;
      }

      public int getChildCount(Object var1) {
         return 0;
      }

      public int getId(Object var1) {
         return -1;
      }

      public int getLayer(Object var1) {
         return -1;
      }

      public Object getParent(Object var1) {
         return null;
      }

      public Object getRoot(Object var1) {
         return null;
      }

      public CharSequence getTitle(Object var1) {
         return null;
      }

      public int getType(Object var1) {
         return -1;
      }

      public boolean isAccessibilityFocused(Object var1) {
         return true;
      }

      public boolean isActive(Object var1) {
         return true;
      }

      public boolean isFocused(Object var1) {
         return true;
      }

      public Object obtain() {
         return null;
      }

      public Object obtain(Object var1) {
         return null;
      }

      public void recycle(Object var1) {
      }
   }
}
