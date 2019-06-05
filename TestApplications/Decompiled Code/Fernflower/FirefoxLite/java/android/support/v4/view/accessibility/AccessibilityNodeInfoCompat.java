package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;

public class AccessibilityNodeInfoCompat {
   private final AccessibilityNodeInfo mInfo;
   public int mParentVirtualDescendantId = -1;

   private AccessibilityNodeInfoCompat(AccessibilityNodeInfo var1) {
      this.mInfo = var1;
   }

   private static String getActionSymbolicName(int var0) {
      switch(var0) {
      case 1:
         return "ACTION_FOCUS";
      case 2:
         return "ACTION_CLEAR_FOCUS";
      case 4:
         return "ACTION_SELECT";
      case 8:
         return "ACTION_CLEAR_SELECTION";
      case 16:
         return "ACTION_CLICK";
      case 32:
         return "ACTION_LONG_CLICK";
      case 64:
         return "ACTION_ACCESSIBILITY_FOCUS";
      case 128:
         return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
      case 256:
         return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
      case 512:
         return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
      case 1024:
         return "ACTION_NEXT_HTML_ELEMENT";
      case 2048:
         return "ACTION_PREVIOUS_HTML_ELEMENT";
      case 4096:
         return "ACTION_SCROLL_FORWARD";
      case 8192:
         return "ACTION_SCROLL_BACKWARD";
      case 16384:
         return "ACTION_COPY";
      case 32768:
         return "ACTION_PASTE";
      case 65536:
         return "ACTION_CUT";
      case 131072:
         return "ACTION_SET_SELECTION";
      default:
         return "ACTION_UNKNOWN";
      }
   }

   public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat var0) {
      return wrap(AccessibilityNodeInfo.obtain(var0.mInfo));
   }

   private void setBooleanProperty(int var1, boolean var2) {
      Bundle var3 = this.getExtras();
      if (var3 != null) {
         int var4 = var3.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0);
         int var5;
         if (var2) {
            var5 = var1;
         } else {
            var5 = 0;
         }

         var3.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", var5 | var4 & var1);
      }

   }

   public static AccessibilityNodeInfoCompat wrap(AccessibilityNodeInfo var0) {
      return new AccessibilityNodeInfoCompat(var0);
   }

   public void addAction(int var1) {
      this.mInfo.addAction(var1);
   }

   public void addChild(View var1) {
      this.mInfo.addChild(var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AccessibilityNodeInfoCompat var2 = (AccessibilityNodeInfoCompat)var1;
         if (this.mInfo == null) {
            if (var2.mInfo != null) {
               return false;
            }
         } else if (!this.mInfo.equals(var2.mInfo)) {
            return false;
         }

         return true;
      }
   }

   public int getActions() {
      return this.mInfo.getActions();
   }

   public void getBoundsInParent(Rect var1) {
      this.mInfo.getBoundsInParent(var1);
   }

   public void getBoundsInScreen(Rect var1) {
      this.mInfo.getBoundsInScreen(var1);
   }

   public CharSequence getClassName() {
      return this.mInfo.getClassName();
   }

   public CharSequence getContentDescription() {
      return this.mInfo.getContentDescription();
   }

   public Bundle getExtras() {
      return VERSION.SDK_INT >= 19 ? this.mInfo.getExtras() : new Bundle();
   }

   public CharSequence getPackageName() {
      return this.mInfo.getPackageName();
   }

   public CharSequence getText() {
      return this.mInfo.getText();
   }

   public String getViewIdResourceName() {
      return VERSION.SDK_INT >= 18 ? this.mInfo.getViewIdResourceName() : null;
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
      return VERSION.SDK_INT >= 16 ? this.mInfo.isAccessibilityFocused() : false;
   }

   public boolean isCheckable() {
      return this.mInfo.isCheckable();
   }

   public boolean isChecked() {
      return this.mInfo.isChecked();
   }

   public boolean isClickable() {
      return this.mInfo.isClickable();
   }

   public boolean isEnabled() {
      return this.mInfo.isEnabled();
   }

   public boolean isFocusable() {
      return this.mInfo.isFocusable();
   }

   public boolean isFocused() {
      return this.mInfo.isFocused();
   }

   public boolean isLongClickable() {
      return this.mInfo.isLongClickable();
   }

   public boolean isPassword() {
      return this.mInfo.isPassword();
   }

   public boolean isScrollable() {
      return this.mInfo.isScrollable();
   }

   public boolean isSelected() {
      return this.mInfo.isSelected();
   }

   public boolean isVisibleToUser() {
      return VERSION.SDK_INT >= 16 ? this.mInfo.isVisibleToUser() : false;
   }

   public void recycle() {
      this.mInfo.recycle();
   }

   public boolean removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat var1) {
      return VERSION.SDK_INT >= 21 ? this.mInfo.removeAction((AccessibilityAction)var1.mAction) : false;
   }

   public void setAccessibilityFocused(boolean var1) {
      if (VERSION.SDK_INT >= 16) {
         this.mInfo.setAccessibilityFocused(var1);
      }

   }

   public void setBoundsInParent(Rect var1) {
      this.mInfo.setBoundsInParent(var1);
   }

   public void setBoundsInScreen(Rect var1) {
      this.mInfo.setBoundsInScreen(var1);
   }

   public void setCheckable(boolean var1) {
      this.mInfo.setCheckable(var1);
   }

   public void setChecked(boolean var1) {
      this.mInfo.setChecked(var1);
   }

   public void setClassName(CharSequence var1) {
      this.mInfo.setClassName(var1);
   }

   public void setClickable(boolean var1) {
      this.mInfo.setClickable(var1);
   }

   public void setCollectionInfo(Object var1) {
      if (VERSION.SDK_INT >= 19) {
         AccessibilityNodeInfo var2 = this.mInfo;
         CollectionInfo var3;
         if (var1 == null) {
            var3 = null;
         } else {
            var3 = (CollectionInfo)((AccessibilityNodeInfoCompat.CollectionInfoCompat)var1).mInfo;
         }

         var2.setCollectionInfo(var3);
      }

   }

   public void setCollectionItemInfo(Object var1) {
      if (VERSION.SDK_INT >= 19) {
         AccessibilityNodeInfo var2 = this.mInfo;
         CollectionItemInfo var3;
         if (var1 == null) {
            var3 = null;
         } else {
            var3 = (CollectionItemInfo)((AccessibilityNodeInfoCompat.CollectionItemInfoCompat)var1).mInfo;
         }

         var2.setCollectionItemInfo(var3);
      }

   }

   public void setContentDescription(CharSequence var1) {
      this.mInfo.setContentDescription(var1);
   }

   public void setContentInvalid(boolean var1) {
      if (VERSION.SDK_INT >= 19) {
         this.mInfo.setContentInvalid(var1);
      }

   }

   public void setDismissable(boolean var1) {
      if (VERSION.SDK_INT >= 19) {
         this.mInfo.setDismissable(var1);
      }

   }

   public void setEnabled(boolean var1) {
      this.mInfo.setEnabled(var1);
   }

   public void setError(CharSequence var1) {
      if (VERSION.SDK_INT >= 21) {
         this.mInfo.setError(var1);
      }

   }

   public void setFocusable(boolean var1) {
      this.mInfo.setFocusable(var1);
   }

   public void setFocused(boolean var1) {
      this.mInfo.setFocused(var1);
   }

   public void setHintText(CharSequence var1) {
      if (VERSION.SDK_INT >= 26) {
         this.mInfo.setHintText(var1);
      } else if (VERSION.SDK_INT >= 19) {
         this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY", var1);
      }

   }

   public void setLongClickable(boolean var1) {
      this.mInfo.setLongClickable(var1);
   }

   public void setPackageName(CharSequence var1) {
      this.mInfo.setPackageName(var1);
   }

   public void setParent(View var1) {
      this.mInfo.setParent(var1);
   }

   public void setScrollable(boolean var1) {
      this.mInfo.setScrollable(var1);
   }

   public void setSelected(boolean var1) {
      this.mInfo.setSelected(var1);
   }

   public void setShowingHintText(boolean var1) {
      if (VERSION.SDK_INT >= 26) {
         this.mInfo.setShowingHintText(var1);
      } else {
         this.setBooleanProperty(4, var1);
      }

   }

   public void setSource(View var1) {
      this.mInfo.setSource(var1);
   }

   public void setText(CharSequence var1) {
      this.mInfo.setText(var1);
   }

   public void setVisibleToUser(boolean var1) {
      if (VERSION.SDK_INT >= 16) {
         this.mInfo.setVisibleToUser(var1);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      Rect var2 = new Rect();
      this.getBoundsInParent(var2);
      StringBuilder var3 = new StringBuilder();
      var3.append("; boundsInParent: ");
      var3.append(var2);
      var1.append(var3.toString());
      this.getBoundsInScreen(var2);
      var3 = new StringBuilder();
      var3.append("; boundsInScreen: ");
      var3.append(var2);
      var1.append(var3.toString());
      var1.append("; packageName: ");
      var1.append(this.getPackageName());
      var1.append("; className: ");
      var1.append(this.getClassName());
      var1.append("; text: ");
      var1.append(this.getText());
      var1.append("; contentDescription: ");
      var1.append(this.getContentDescription());
      var1.append("; viewId: ");
      var1.append(this.getViewIdResourceName());
      var1.append("; checkable: ");
      var1.append(this.isCheckable());
      var1.append("; checked: ");
      var1.append(this.isChecked());
      var1.append("; focusable: ");
      var1.append(this.isFocusable());
      var1.append("; focused: ");
      var1.append(this.isFocused());
      var1.append("; selected: ");
      var1.append(this.isSelected());
      var1.append("; clickable: ");
      var1.append(this.isClickable());
      var1.append("; longClickable: ");
      var1.append(this.isLongClickable());
      var1.append("; enabled: ");
      var1.append(this.isEnabled());
      var1.append("; password: ");
      var1.append(this.isPassword());
      StringBuilder var7 = new StringBuilder();
      var7.append("; scrollable: ");
      var7.append(this.isScrollable());
      var1.append(var7.toString());
      var1.append("; [");
      int var4 = this.getActions();

      while(var4 != 0) {
         int var5 = 1 << Integer.numberOfTrailingZeros(var4);
         int var6 = var4 & var5;
         var1.append(getActionSymbolicName(var5));
         var4 = var6;
         if (var6 != 0) {
            var1.append(", ");
            var4 = var6;
         }
      }

      var1.append("]");
      return var1.toString();
   }

   public AccessibilityNodeInfo unwrap() {
      return this.mInfo;
   }

   public static class AccessibilityActionCompat {
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_FOCUS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_SELECTION;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLICK;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COLLAPSE;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CONTEXT_CLICK;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COPY;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CUT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_DISMISS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_EXPAND;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_FOCUS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_HIDE_TOOLTIP;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_LONG_CLICK;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_MOVE_WINDOW;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PASTE;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_DOWN;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_FORWARD;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_LEFT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_RIGHT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_UP;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SELECT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_PROGRESS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_SELECTION;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_TEXT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SHOW_TOOLTIP;
      final Object mAction;

      static {
         Object var0 = null;
         ACTION_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1, (CharSequence)null);
         ACTION_CLEAR_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2, (CharSequence)null);
         ACTION_SELECT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(4, (CharSequence)null);
         ACTION_CLEAR_SELECTION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(8, (CharSequence)null);
         ACTION_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, (CharSequence)null);
         ACTION_LONG_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(32, (CharSequence)null);
         ACTION_ACCESSIBILITY_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(64, (CharSequence)null);
         ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(128, (CharSequence)null);
         ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(256, (CharSequence)null);
         ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(512, (CharSequence)null);
         ACTION_NEXT_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1024, (CharSequence)null);
         ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2048, (CharSequence)null);
         ACTION_SCROLL_FORWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(4096, (CharSequence)null);
         ACTION_SCROLL_BACKWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(8192, (CharSequence)null);
         ACTION_COPY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16384, (CharSequence)null);
         ACTION_PASTE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(32768, (CharSequence)null);
         ACTION_CUT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(65536, (CharSequence)null);
         ACTION_SET_SELECTION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(131072, (CharSequence)null);
         ACTION_EXPAND = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(262144, (CharSequence)null);
         ACTION_COLLAPSE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(524288, (CharSequence)null);
         ACTION_DISMISS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1048576, (CharSequence)null);
         ACTION_SET_TEXT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2097152, (CharSequence)null);
         AccessibilityAction var1;
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SHOW_ON_SCREEN;
         } else {
            var1 = null;
         }

         ACTION_SHOW_ON_SCREEN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_TO_POSITION;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_TO_POSITION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_UP;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_UP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_LEFT;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_LEFT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_DOWN;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_DOWN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_RIGHT;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_RIGHT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_CONTEXT_CLICK;
         } else {
            var1 = null;
         }

         ACTION_CONTEXT_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 24) {
            var1 = AccessibilityAction.ACTION_SET_PROGRESS;
         } else {
            var1 = null;
         }

         ACTION_SET_PROGRESS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 26) {
            var1 = AccessibilityAction.ACTION_MOVE_WINDOW;
         } else {
            var1 = null;
         }

         ACTION_MOVE_WINDOW = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         if (VERSION.SDK_INT >= 28) {
            var1 = AccessibilityAction.ACTION_SHOW_TOOLTIP;
         } else {
            var1 = null;
         }

         ACTION_SHOW_TOOLTIP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
         var1 = (AccessibilityAction)var0;
         if (VERSION.SDK_INT >= 28) {
            var1 = AccessibilityAction.ACTION_HIDE_TOOLTIP;
         }

         ACTION_HIDE_TOOLTIP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1);
      }

      public AccessibilityActionCompat(int var1, CharSequence var2) {
         AccessibilityAction var3;
         if (VERSION.SDK_INT >= 21) {
            var3 = new AccessibilityAction(var1, var2);
         } else {
            var3 = null;
         }

         this(var3);
      }

      AccessibilityActionCompat(Object var1) {
         this.mAction = var1;
      }
   }

   public static class CollectionInfoCompat {
      final Object mInfo;

      CollectionInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int var0, int var1, boolean var2, int var3) {
         if (VERSION.SDK_INT >= 21) {
            return new AccessibilityNodeInfoCompat.CollectionInfoCompat(CollectionInfo.obtain(var0, var1, var2, var3));
         } else {
            return VERSION.SDK_INT >= 19 ? new AccessibilityNodeInfoCompat.CollectionInfoCompat(CollectionInfo.obtain(var0, var1, var2)) : new AccessibilityNodeInfoCompat.CollectionInfoCompat((Object)null);
         }
      }
   }

   public static class CollectionItemInfoCompat {
      final Object mInfo;

      CollectionItemInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int var0, int var1, int var2, int var3, boolean var4, boolean var5) {
         if (VERSION.SDK_INT >= 21) {
            return new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(CollectionItemInfo.obtain(var0, var1, var2, var3, var4, var5));
         } else {
            return VERSION.SDK_INT >= 19 ? new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(CollectionItemInfo.obtain(var0, var1, var2, var3, var4)) : new AccessibilityNodeInfoCompat.CollectionItemInfoCompat((Object)null);
         }
      }
   }
}
