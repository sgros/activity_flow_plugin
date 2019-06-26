package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import androidx.core.R$id;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeInfoCompat {
   private static int sClickableSpanId;
   private final AccessibilityNodeInfo mInfo;
   public int mParentVirtualDescendantId = -1;

   private AccessibilityNodeInfoCompat(AccessibilityNodeInfo var1) {
      this.mInfo = var1;
   }

   private void addSpanLocationToExtras(ClickableSpan var1, Spanned var2, int var3) {
      this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").add(var2.getSpanStart(var1));
      this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY").add(var2.getSpanEnd(var1));
      this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY").add(var2.getSpanFlags(var1));
      this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY").add(var3);
   }

   private void clearExtrasSpans() {
      if (VERSION.SDK_INT >= 19) {
         this.mInfo.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
         this.mInfo.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
         this.mInfo.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
         this.mInfo.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
      }

   }

   private List extrasIntList(String var1) {
      if (VERSION.SDK_INT < 19) {
         return new ArrayList();
      } else {
         ArrayList var2 = this.mInfo.getExtras().getIntegerArrayList(var1);
         ArrayList var3 = var2;
         if (var2 == null) {
            var3 = new ArrayList();
            this.mInfo.getExtras().putIntegerArrayList(var1, var3);
         }

         return var3;
      }
   }

   private static String getActionSymbolicName(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            switch(var0) {
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
         } else {
            return "ACTION_CLEAR_FOCUS";
         }
      } else {
         return "ACTION_FOCUS";
      }
   }

   public static ClickableSpan[] getClickableSpans(CharSequence var0) {
      return var0 instanceof Spanned ? (ClickableSpan[])((Spanned)var0).getSpans(0, var0.length(), ClickableSpan.class) : null;
   }

   private SparseArray getOrCreateSpansFromViewTags(View var1) {
      SparseArray var2 = this.getSpansFromViewTags(var1);
      SparseArray var3 = var2;
      if (var2 == null) {
         var3 = new SparseArray();
         var1.setTag(R$id.tag_accessibility_clickable_spans, var3);
      }

      return var3;
   }

   private SparseArray getSpansFromViewTags(View var1) {
      return (SparseArray)var1.getTag(R$id.tag_accessibility_clickable_spans);
   }

   private boolean hasSpans() {
      return this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").isEmpty() ^ true;
   }

   private int idForClickableSpan(ClickableSpan var1, SparseArray var2) {
      int var3;
      if (var2 != null) {
         for(var3 = 0; var3 < var2.size(); ++var3) {
            if (var1.equals((ClickableSpan)((WeakReference)var2.valueAt(var3)).get())) {
               return var2.keyAt(var3);
            }
         }
      }

      var3 = sClickableSpanId++;
      return var3;
   }

   public static AccessibilityNodeInfoCompat obtain() {
      return wrap(AccessibilityNodeInfo.obtain());
   }

   public static AccessibilityNodeInfoCompat obtain(View var0) {
      return wrap(AccessibilityNodeInfo.obtain(var0));
   }

   public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat var0) {
      return wrap(AccessibilityNodeInfo.obtain(var0.mInfo));
   }

   private void removeCollectedSpans(View var1) {
      SparseArray var6 = this.getSpansFromViewTags(var1);
      if (var6 != null) {
         ArrayList var2 = new ArrayList();
         byte var3 = 0;
         int var4 = 0;

         while(true) {
            int var5 = var3;
            if (var4 >= var6.size()) {
               while(var5 < var2.size()) {
                  var6.remove((Integer)var2.get(var5));
                  ++var5;
               }
               break;
            }

            if (((WeakReference)var6.valueAt(var4)).get() == null) {
               var2.add(var4);
            }

            ++var4;
         }
      }

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

         var3.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", var5 | var4 & ~var1);
      }

   }

   public static AccessibilityNodeInfoCompat wrap(AccessibilityNodeInfo var0) {
      return new AccessibilityNodeInfoCompat(var0);
   }

   public void addAction(int var1) {
      this.mInfo.addAction(var1);
   }

   public void addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat var1) {
      if (VERSION.SDK_INT >= 21) {
         this.mInfo.addAction((AccessibilityAction)var1.mAction);
      }

   }

   public void addChild(View var1, int var2) {
      if (VERSION.SDK_INT >= 16) {
         this.mInfo.addChild(var1, var2);
      }

   }

   public void addSpansToExtras(CharSequence var1, View var2) {
      int var3 = VERSION.SDK_INT;
      if (var3 >= 19 && var3 < 26) {
         this.clearExtrasSpans();
         this.removeCollectedSpans(var2);
         ClickableSpan[] var4 = getClickableSpans(var1);
         if (var4 != null && var4.length > 0) {
            this.getExtras().putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY", R$id.accessibility_action_clickable_span);
            SparseArray var6 = this.getOrCreateSpansFromViewTags(var2);

            for(var3 = 0; var4 != null && var3 < var4.length; ++var3) {
               int var5 = this.idForClickableSpan(var4[var3], var6);
               var6.put(var5, new WeakReference(var4[var3]));
               this.addSpanLocationToExtras(var4[var3], (Spanned)var1, var5);
            }
         }
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (AccessibilityNodeInfoCompat.class != var1.getClass()) {
         return false;
      } else {
         AccessibilityNodeInfoCompat var3 = (AccessibilityNodeInfoCompat)var1;
         AccessibilityNodeInfo var2 = this.mInfo;
         if (var2 == null) {
            if (var3.mInfo != null) {
               return false;
            }
         } else if (!var2.equals(var3.mInfo)) {
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

   public int getChildCount() {
      return this.mInfo.getChildCount();
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
      if (!this.hasSpans()) {
         return this.mInfo.getText();
      } else {
         List var1 = this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
         List var2 = this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
         List var3 = this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
         List var4 = this.extrasIntList("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
         CharSequence var5 = this.mInfo.getText();
         int var6 = this.mInfo.getText().length();
         int var7 = 0;

         SpannableString var8;
         for(var8 = new SpannableString(TextUtils.substring(var5, 0, var6)); var7 < var1.size(); ++var7) {
            var8.setSpan(new AccessibilityClickableSpanCompat((Integer)var4.get(var7), this, this.getExtras().getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY")), (Integer)var1.get(var7), (Integer)var2.get(var7), (Integer)var3.get(var7));
         }

         return var8;
      }
   }

   public String getViewIdResourceName() {
      return VERSION.SDK_INT >= 18 ? this.mInfo.getViewIdResourceName() : null;
   }

   public int hashCode() {
      AccessibilityNodeInfo var1 = this.mInfo;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.hashCode();
      }

      return var2;
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

   public boolean performAction(int var1, Bundle var2) {
      return VERSION.SDK_INT >= 16 ? this.mInfo.performAction(var1, var2) : false;
   }

   public void recycle() {
      this.mInfo.recycle();
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

   public void setClassName(CharSequence var1) {
      this.mInfo.setClassName(var1);
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

   public void setEnabled(boolean var1) {
      this.mInfo.setEnabled(var1);
   }

   public void setFocusable(boolean var1) {
      this.mInfo.setFocusable(var1);
   }

   public void setFocused(boolean var1) {
      this.mInfo.setFocused(var1);
   }

   public void setHeading(boolean var1) {
      if (VERSION.SDK_INT >= 28) {
         this.mInfo.setHeading(var1);
      } else {
         this.setBooleanProperty(2, var1);
      }

   }

   public void setPackageName(CharSequence var1) {
      this.mInfo.setPackageName(var1);
   }

   public void setPaneTitle(CharSequence var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 28) {
         this.mInfo.setPaneTitle(var1);
      } else if (var2 >= 19) {
         this.mInfo.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY", var1);
      }

   }

   public void setParent(View var1) {
      this.mInfo.setParent(var1);
   }

   public void setParent(View var1, int var2) {
      this.mParentVirtualDescendantId = var2;
      if (VERSION.SDK_INT >= 16) {
         this.mInfo.setParent(var1, var2);
      }

   }

   public void setScreenReaderFocusable(boolean var1) {
      if (VERSION.SDK_INT >= 28) {
         this.mInfo.setScreenReaderFocusable(var1);
      } else {
         this.setBooleanProperty(1, var1);
      }

   }

   public void setScrollable(boolean var1) {
      this.mInfo.setScrollable(var1);
   }

   public void setSource(View var1, int var2) {
      if (VERSION.SDK_INT >= 16) {
         this.mInfo.setSource(var1, var2);
      }

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
         int var6 = var4 & ~var5;
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
      protected final AccessibilityViewCommand mCommand;
      private final int mId;
      private final CharSequence mLabel;
      private final Class mViewCommandArgumentClass;

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
         ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(256, (CharSequence)null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
         ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(512, (CharSequence)null, AccessibilityViewCommand.MoveAtGranularityArguments.class);
         ACTION_NEXT_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1024, (CharSequence)null, AccessibilityViewCommand.MoveHtmlArguments.class);
         ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2048, (CharSequence)null, AccessibilityViewCommand.MoveHtmlArguments.class);
         ACTION_SCROLL_FORWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(4096, (CharSequence)null);
         ACTION_SCROLL_BACKWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(8192, (CharSequence)null);
         ACTION_COPY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16384, (CharSequence)null);
         ACTION_PASTE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(32768, (CharSequence)null);
         ACTION_CUT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(65536, (CharSequence)null);
         ACTION_SET_SELECTION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(131072, (CharSequence)null, AccessibilityViewCommand.SetSelectionArguments.class);
         ACTION_EXPAND = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(262144, (CharSequence)null);
         ACTION_COLLAPSE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(524288, (CharSequence)null);
         ACTION_DISMISS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1048576, (CharSequence)null);
         ACTION_SET_TEXT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2097152, (CharSequence)null, AccessibilityViewCommand.SetTextArguments.class);
         AccessibilityAction var1;
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SHOW_ON_SCREEN;
         } else {
            var1 = null;
         }

         ACTION_SHOW_ON_SCREEN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908342, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_TO_POSITION;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_TO_POSITION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908343, (CharSequence)null, (AccessibilityViewCommand)null, AccessibilityViewCommand.ScrollToPositionArguments.class);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_UP;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_UP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908344, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_LEFT;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_LEFT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908345, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_DOWN;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_DOWN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908346, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_SCROLL_RIGHT;
         } else {
            var1 = null;
         }

         ACTION_SCROLL_RIGHT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908347, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 23) {
            var1 = AccessibilityAction.ACTION_CONTEXT_CLICK;
         } else {
            var1 = null;
         }

         ACTION_CONTEXT_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908348, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         if (VERSION.SDK_INT >= 24) {
            var1 = AccessibilityAction.ACTION_SET_PROGRESS;
         } else {
            var1 = null;
         }

         ACTION_SET_PROGRESS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908349, (CharSequence)null, (AccessibilityViewCommand)null, AccessibilityViewCommand.SetProgressArguments.class);
         if (VERSION.SDK_INT >= 26) {
            var1 = AccessibilityAction.ACTION_MOVE_WINDOW;
         } else {
            var1 = null;
         }

         ACTION_MOVE_WINDOW = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908354, (CharSequence)null, (AccessibilityViewCommand)null, AccessibilityViewCommand.MoveWindowArguments.class);
         if (VERSION.SDK_INT >= 28) {
            var1 = AccessibilityAction.ACTION_SHOW_TOOLTIP;
         } else {
            var1 = null;
         }

         ACTION_SHOW_TOOLTIP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908356, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
         var1 = (AccessibilityAction)var0;
         if (VERSION.SDK_INT >= 28) {
            var1 = AccessibilityAction.ACTION_HIDE_TOOLTIP;
         }

         ACTION_HIDE_TOOLTIP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var1, 16908357, (CharSequence)null, (AccessibilityViewCommand)null, (Class)null);
      }

      public AccessibilityActionCompat(int var1, CharSequence var2) {
         this((Object)null, var1, var2, (AccessibilityViewCommand)null, (Class)null);
      }

      private AccessibilityActionCompat(int var1, CharSequence var2, Class var3) {
         this((Object)null, var1, var2, (AccessibilityViewCommand)null, var3);
      }

      AccessibilityActionCompat(Object var1, int var2, CharSequence var3, AccessibilityViewCommand var4, Class var5) {
         this.mId = var2;
         this.mLabel = var3;
         this.mCommand = var4;
         if (VERSION.SDK_INT >= 21 && var1 == null) {
            this.mAction = new AccessibilityAction(var2, var3);
         } else {
            this.mAction = var1;
         }

         this.mViewCommandArgumentClass = var5;
      }

      public int getId() {
         return VERSION.SDK_INT >= 21 ? ((AccessibilityAction)this.mAction).getId() : 0;
      }

      public boolean perform(View var1, Bundle var2) {
         if (this.mCommand == null) {
            return false;
         } else {
            AccessibilityViewCommand.CommandArguments var3 = null;
            Class var4 = null;
            Class var5 = this.mViewCommandArgumentClass;
            if (var5 != null) {
               AccessibilityViewCommand.CommandArguments var8;
               Exception var9;
               label28: {
                  try {
                     var3 = (AccessibilityViewCommand.CommandArguments)var5.getDeclaredConstructor().newInstance();
                  } catch (Exception var7) {
                     var9 = var7;
                     var8 = var4;
                     break label28;
                  }

                  try {
                     var3.setBundle(var2);
                     return this.mCommand.perform(var1, var3);
                  } catch (Exception var6) {
                     var8 = var3;
                     var9 = var6;
                  }
               }

               var4 = this.mViewCommandArgumentClass;
               String var10;
               if (var4 == null) {
                  var10 = "null";
               } else {
                  var10 = var4.getName();
               }

               StringBuilder var11 = new StringBuilder();
               var11.append("Failed to execute command with argument class ViewCommandArgument: ");
               var11.append(var10);
               Log.e("A11yActionCompat", var11.toString(), var9);
               var3 = var8;
            }

            return this.mCommand.perform(var1, var3);
         }
      }
   }

   public static class CollectionInfoCompat {
      final Object mInfo;

      CollectionInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int var0, int var1, boolean var2, int var3) {
         int var4 = VERSION.SDK_INT;
         if (var4 >= 21) {
            return new AccessibilityNodeInfoCompat.CollectionInfoCompat(CollectionInfo.obtain(var0, var1, var2, var3));
         } else {
            return var4 >= 19 ? new AccessibilityNodeInfoCompat.CollectionInfoCompat(CollectionInfo.obtain(var0, var1, var2)) : new AccessibilityNodeInfoCompat.CollectionInfoCompat((Object)null);
         }
      }
   }

   public static class CollectionItemInfoCompat {
      final Object mInfo;

      CollectionItemInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int var0, int var1, int var2, int var3, boolean var4, boolean var5) {
         int var6 = VERSION.SDK_INT;
         if (var6 >= 21) {
            return new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(CollectionItemInfo.obtain(var0, var1, var2, var3, var4, var5));
         } else {
            return var6 >= 19 ? new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(CollectionItemInfo.obtain(var0, var1, var2, var3, var4)) : new AccessibilityNodeInfoCompat.CollectionItemInfoCompat((Object)null);
         }
      }
   }
}
