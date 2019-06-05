package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
   private static final String DEFAULT_CLASS_NAME = "android.view.View";
   public static final int HOST_ID = -1;
   public static final int INVALID_ID = Integer.MIN_VALUE;
   private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
   private static final FocusStrategy.BoundsAdapter NODE_ADAPTER = new FocusStrategy.BoundsAdapter() {
      public void obtainBounds(AccessibilityNodeInfoCompat var1, Rect var2) {
         var1.getBoundsInParent(var2);
      }
   };
   private static final FocusStrategy.CollectionAdapter SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter() {
      public AccessibilityNodeInfoCompat get(SparseArrayCompat var1, int var2) {
         return (AccessibilityNodeInfoCompat)var1.valueAt(var2);
      }

      public int size(SparseArrayCompat var1) {
         return var1.size();
      }
   };
   private int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
   private final View mHost;
   private int mHoveredVirtualViewId = Integer.MIN_VALUE;
   private int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
   private final AccessibilityManager mManager;
   private ExploreByTouchHelper.MyNodeProvider mNodeProvider;
   private final int[] mTempGlobalRect = new int[2];
   private final Rect mTempParentRect = new Rect();
   private final Rect mTempScreenRect = new Rect();
   private final Rect mTempVisibleRect = new Rect();

   public ExploreByTouchHelper(View var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("View may not be null");
      } else {
         this.mHost = var1;
         this.mManager = (AccessibilityManager)var1.getContext().getSystemService("accessibility");
         var1.setFocusable(true);
         if (ViewCompat.getImportantForAccessibility(var1) == 0) {
            ViewCompat.setImportantForAccessibility(var1, 1);
         }

      }
   }

   private boolean clearAccessibilityFocus(int var1) {
      boolean var2;
      if (this.mAccessibilityFocusedVirtualViewId == var1) {
         this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
         this.mHost.invalidate();
         this.sendEventForVirtualView(var1, 65536);
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean clickKeyboardFocusedVirtualView() {
      boolean var1;
      if (this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE && this.onPerformActionForVirtualView(this.mKeyboardFocusedVirtualViewId, 16, (Bundle)null)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private AccessibilityEvent createEvent(int var1, int var2) {
      AccessibilityEvent var3;
      switch(var1) {
      case -1:
         var3 = this.createEventForHost(var2);
         break;
      default:
         var3 = this.createEventForChild(var1, var2);
      }

      return var3;
   }

   private AccessibilityEvent createEventForChild(int var1, int var2) {
      AccessibilityEvent var3 = AccessibilityEvent.obtain(var2);
      AccessibilityRecordCompat var4 = AccessibilityEventCompat.asRecord(var3);
      AccessibilityNodeInfoCompat var5 = this.obtainAccessibilityNodeInfo(var1);
      var4.getText().add(var5.getText());
      var4.setContentDescription(var5.getContentDescription());
      var4.setScrollable(var5.isScrollable());
      var4.setPassword(var5.isPassword());
      var4.setEnabled(var5.isEnabled());
      var4.setChecked(var5.isChecked());
      this.onPopulateEventForVirtualView(var1, var3);
      if (var3.getText().isEmpty() && var3.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
      } else {
         var4.setClassName(var5.getClassName());
         var4.setSource(this.mHost, var1);
         var3.setPackageName(this.mHost.getContext().getPackageName());
         return var3;
      }
   }

   private AccessibilityEvent createEventForHost(int var1) {
      AccessibilityEvent var2 = AccessibilityEvent.obtain(var1);
      ViewCompat.onInitializeAccessibilityEvent(this.mHost, var2);
      return var2;
   }

   @NonNull
   private AccessibilityNodeInfoCompat createNodeForChild(int var1) {
      AccessibilityNodeInfoCompat var2 = AccessibilityNodeInfoCompat.obtain();
      var2.setEnabled(true);
      var2.setFocusable(true);
      var2.setClassName("android.view.View");
      var2.setBoundsInParent(INVALID_PARENT_BOUNDS);
      var2.setBoundsInScreen(INVALID_PARENT_BOUNDS);
      var2.setParent(this.mHost);
      this.onPopulateNodeForVirtualView(var1, var2);
      if (var2.getText() == null && var2.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
      } else {
         var2.getBoundsInParent(this.mTempParentRect);
         if (this.mTempParentRect.equals(INVALID_PARENT_BOUNDS)) {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
         } else {
            int var3 = var2.getActions();
            if ((var3 & 64) != 0) {
               throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else if ((var3 & 128) != 0) {
               throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else {
               var2.setPackageName(this.mHost.getContext().getPackageName());
               var2.setSource(this.mHost, var1);
               if (this.mAccessibilityFocusedVirtualViewId == var1) {
                  var2.setAccessibilityFocused(true);
                  var2.addAction(128);
               } else {
                  var2.setAccessibilityFocused(false);
                  var2.addAction(64);
               }

               boolean var4;
               if (this.mKeyboardFocusedVirtualViewId == var1) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var4) {
                  var2.addAction(2);
               } else if (var2.isFocusable()) {
                  var2.addAction(1);
               }

               var2.setFocused(var4);
               this.mHost.getLocationOnScreen(this.mTempGlobalRect);
               var2.getBoundsInScreen(this.mTempScreenRect);
               if (this.mTempScreenRect.equals(INVALID_PARENT_BOUNDS)) {
                  var2.getBoundsInParent(this.mTempScreenRect);
                  if (var2.mParentVirtualDescendantId != -1) {
                     AccessibilityNodeInfoCompat var5 = AccessibilityNodeInfoCompat.obtain();

                     for(var1 = var2.mParentVirtualDescendantId; var1 != -1; var1 = var5.mParentVirtualDescendantId) {
                        var5.setParent(this.mHost, -1);
                        var5.setBoundsInParent(INVALID_PARENT_BOUNDS);
                        this.onPopulateNodeForVirtualView(var1, var5);
                        var5.getBoundsInParent(this.mTempParentRect);
                        this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
                     }

                     var5.recycle();
                  }

                  this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
               }

               if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                  this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                  this.mTempScreenRect.intersect(this.mTempVisibleRect);
                  var2.setBoundsInScreen(this.mTempScreenRect);
                  if (this.isVisibleToUser(this.mTempScreenRect)) {
                     var2.setVisibleToUser(true);
                  }
               }

               return var2;
            }
         }
      }
   }

   @NonNull
   private AccessibilityNodeInfoCompat createNodeForHost() {
      AccessibilityNodeInfoCompat var1 = AccessibilityNodeInfoCompat.obtain(this.mHost);
      ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, var1);
      ArrayList var2 = new ArrayList();
      this.getVisibleVirtualViews(var2);
      if (var1.getChildCount() > 0 && var2.size() > 0) {
         throw new RuntimeException("Views cannot have both real and virtual children");
      } else {
         int var3 = 0;

         for(int var4 = var2.size(); var3 < var4; ++var3) {
            var1.addChild(this.mHost, (Integer)var2.get(var3));
         }

         return var1;
      }
   }

   private SparseArrayCompat getAllNodes() {
      ArrayList var1 = new ArrayList();
      this.getVisibleVirtualViews(var1);
      SparseArrayCompat var2 = new SparseArrayCompat();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2.put(var3, this.createNodeForChild(var3));
      }

      return var2;
   }

   private void getBoundsInParent(int var1, Rect var2) {
      this.obtainAccessibilityNodeInfo(var1).getBoundsInParent(var2);
   }

   private static Rect guessPreviouslyFocusedRect(@NonNull View var0, int var1, @NonNull Rect var2) {
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();
      switch(var1) {
      case 17:
         var2.set(var3, 0, var3, var4);
         break;
      case 33:
         var2.set(0, var4, var3, var4);
         break;
      case 66:
         var2.set(-1, 0, -1, var4);
         break;
      case 130:
         var2.set(0, -1, var3, -1);
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      return var2;
   }

   private boolean isVisibleToUser(Rect var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         if (var1.isEmpty()) {
            var3 = var2;
         } else {
            var3 = var2;
            if (this.mHost.getWindowVisibility() == 0) {
               ViewParent var4 = this.mHost.getParent();

               while(true) {
                  if (!(var4 instanceof View)) {
                     var3 = var2;
                     if (var4 != null) {
                        var3 = true;
                     }
                     break;
                  }

                  View var5 = (View)var4;
                  var3 = var2;
                  if (ViewCompat.getAlpha(var5) <= 0.0F) {
                     break;
                  }

                  var3 = var2;
                  if (var5.getVisibility() != 0) {
                     break;
                  }

                  var4 = var5.getParent();
               }
            }
         }
      }

      return var3;
   }

   private static int keyToDirection(int var0) {
      short var1;
      switch(var0) {
      case 19:
         var1 = 33;
         break;
      case 20:
      default:
         var1 = 130;
         break;
      case 21:
         var1 = 17;
         break;
      case 22:
         var1 = 66;
      }

      return var1;
   }

   private boolean moveFocus(int var1, @Nullable Rect var2) {
      SparseArrayCompat var3 = this.getAllNodes();
      int var4 = this.mKeyboardFocusedVirtualViewId;
      AccessibilityNodeInfoCompat var5;
      if (var4 == Integer.MIN_VALUE) {
         var5 = null;
      } else {
         var5 = (AccessibilityNodeInfoCompat)var3.get(var4);
      }

      AccessibilityNodeInfoCompat var8;
      switch(var1) {
      case 1:
      case 2:
         boolean var6;
         if (ViewCompat.getLayoutDirection(this.mHost) == 1) {
            var6 = true;
         } else {
            var6 = false;
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInRelativeDirection(var3, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var1, var6, false);
         break;
      case 17:
      case 33:
      case 66:
      case 130:
         Rect var7 = new Rect();
         if (this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
            this.getBoundsInParent(this.mKeyboardFocusedVirtualViewId, var7);
         } else if (var2 != null) {
            var7.set(var2);
         } else {
            guessPreviouslyFocusedRect(this.mHost, var1, var7);
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInAbsoluteDirection(var3, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var7, var1);
         break;
      default:
         throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
      }

      if (var8 == null) {
         var1 = Integer.MIN_VALUE;
      } else {
         var1 = var3.keyAt(var3.indexOfValue(var8));
      }

      return this.requestKeyboardFocusForVirtualView(var1);
   }

   private boolean performActionForChild(int var1, int var2, Bundle var3) {
      boolean var4;
      switch(var2) {
      case 1:
         var4 = this.requestKeyboardFocusForVirtualView(var1);
         break;
      case 2:
         var4 = this.clearKeyboardFocusForVirtualView(var1);
         break;
      case 64:
         var4 = this.requestAccessibilityFocus(var1);
         break;
      case 128:
         var4 = this.clearAccessibilityFocus(var1);
         break;
      default:
         var4 = this.onPerformActionForVirtualView(var1, var2, var3);
      }

      return var4;
   }

   private boolean performActionForHost(int var1, Bundle var2) {
      return ViewCompat.performAccessibilityAction(this.mHost, var1, var2);
   }

   private boolean requestAccessibilityFocus(int var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (this.mManager.isEnabled()) {
         if (!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            var3 = var2;
         } else {
            var3 = var2;
            if (this.mAccessibilityFocusedVirtualViewId != var1) {
               if (this.mAccessibilityFocusedVirtualViewId != Integer.MIN_VALUE) {
                  this.clearAccessibilityFocus(this.mAccessibilityFocusedVirtualViewId);
               }

               this.mAccessibilityFocusedVirtualViewId = var1;
               this.mHost.invalidate();
               this.sendEventForVirtualView(var1, 32768);
               var3 = true;
            }
         }
      }

      return var3;
   }

   private void updateHoveredVirtualView(int var1) {
      if (this.mHoveredVirtualViewId != var1) {
         int var2 = this.mHoveredVirtualViewId;
         this.mHoveredVirtualViewId = var1;
         this.sendEventForVirtualView(var1, 128);
         this.sendEventForVirtualView(var2, 256);
      }

   }

   public final boolean clearKeyboardFocusForVirtualView(int var1) {
      boolean var2 = false;
      if (this.mKeyboardFocusedVirtualViewId == var1) {
         this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
         this.onVirtualViewKeyboardFocusChanged(var1, false);
         this.sendEventForVirtualView(var1, 8);
         var2 = true;
      }

      return var2;
   }

   public final boolean dispatchHoverEvent(@NonNull MotionEvent var1) {
      boolean var2 = true;
      boolean var3 = false;
      boolean var4 = var3;
      if (this.mManager.isEnabled()) {
         if (!AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            var4 = var3;
         } else {
            switch(var1.getAction()) {
            case 7:
            case 9:
               int var5 = this.getVirtualViewAt(var1.getX(), var1.getY());
               this.updateHoveredVirtualView(var5);
               if (var5 != Integer.MIN_VALUE) {
                  var4 = var2;
               } else {
                  var4 = false;
               }
               break;
            case 8:
            default:
               var4 = var3;
               break;
            case 10:
               var4 = var3;
               if (this.mAccessibilityFocusedVirtualViewId != Integer.MIN_VALUE) {
                  this.updateHoveredVirtualView(Integer.MIN_VALUE);
                  var4 = true;
               }
            }
         }
      }

      return var4;
   }

   public final boolean dispatchKeyEvent(@NonNull KeyEvent var1) {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = var3;
      if (var1.getAction() != 1) {
         int var5 = var1.getKeyCode();
         switch(var5) {
         case 19:
         case 20:
         case 21:
         case 22:
            var4 = var3;
            if (KeyEventCompat.hasNoModifiers(var1)) {
               int var6 = keyToDirection(var5);
               int var7 = var1.getRepeatCount();
               var5 = 0;
               var3 = var2;

               while(true) {
                  var4 = var3;
                  if (var5 >= var7 + 1) {
                     break;
                  }

                  var4 = var3;
                  if (!this.moveFocus(var6, (Rect)null)) {
                     break;
                  }

                  var3 = true;
                  ++var5;
               }
            }
            break;
         case 23:
         case 66:
            var4 = var3;
            if (KeyEventCompat.hasNoModifiers(var1)) {
               var4 = var3;
               if (var1.getRepeatCount() == 0) {
                  this.clickKeyboardFocusedVirtualView();
                  var4 = true;
               }
            }
            break;
         case 61:
            if (KeyEventCompat.hasNoModifiers(var1)) {
               var4 = this.moveFocus(2, (Rect)null);
            } else {
               var4 = var3;
               if (KeyEventCompat.hasModifiers(var1, 1)) {
                  var4 = this.moveFocus(1, (Rect)null);
               }
            }
            break;
         default:
            var4 = var3;
         }
      }

      return var4;
   }

   public final int getAccessibilityFocusedVirtualViewId() {
      return this.mAccessibilityFocusedVirtualViewId;
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      if (this.mNodeProvider == null) {
         this.mNodeProvider = new ExploreByTouchHelper.MyNodeProvider();
      }

      return this.mNodeProvider;
   }

   @Deprecated
   public int getFocusedVirtualView() {
      return this.getAccessibilityFocusedVirtualViewId();
   }

   public final int getKeyboardFocusedVirtualViewId() {
      return this.mKeyboardFocusedVirtualViewId;
   }

   protected abstract int getVirtualViewAt(float var1, float var2);

   protected abstract void getVisibleVirtualViews(List var1);

   public final void invalidateRoot() {
      this.invalidateVirtualView(-1, 1);
   }

   public final void invalidateVirtualView(int var1) {
      this.invalidateVirtualView(var1, 0);
   }

   public final void invalidateVirtualView(int var1, int var2) {
      if (var1 != Integer.MIN_VALUE && this.mManager.isEnabled()) {
         ViewParent var3 = this.mHost.getParent();
         if (var3 != null) {
            AccessibilityEvent var4 = this.createEvent(var1, 2048);
            AccessibilityEventCompat.setContentChangeTypes(var4, var2);
            ViewParentCompat.requestSendAccessibilityEvent(var3, this.mHost, var4);
         }
      }

   }

   @NonNull
   AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int var1) {
      AccessibilityNodeInfoCompat var2;
      if (var1 == -1) {
         var2 = this.createNodeForHost();
      } else {
         var2 = this.createNodeForChild(var1);
      }

      return var2;
   }

   public final void onFocusChanged(boolean var1, int var2, @Nullable Rect var3) {
      if (this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
         this.clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
      }

      if (var1) {
         this.moveFocus(var2, var3);
      }

   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      super.onInitializeAccessibilityEvent(var1, var2);
      this.onPopulateEventForHost(var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      super.onInitializeAccessibilityNodeInfo(var1, var2);
      this.onPopulateNodeForHost(var2);
   }

   protected abstract boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3);

   protected void onPopulateEventForHost(AccessibilityEvent var1) {
   }

   protected void onPopulateEventForVirtualView(int var1, AccessibilityEvent var2) {
   }

   protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat var1) {
   }

   protected abstract void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2);

   protected void onVirtualViewKeyboardFocusChanged(int var1, boolean var2) {
   }

   boolean performAction(int var1, int var2, Bundle var3) {
      boolean var4;
      switch(var1) {
      case -1:
         var4 = this.performActionForHost(var2, var3);
         break;
      default:
         var4 = this.performActionForChild(var1, var2, var3);
      }

      return var4;
   }

   public final boolean requestKeyboardFocusForVirtualView(int var1) {
      boolean var2 = false;
      if ((this.mHost.isFocused() || this.mHost.requestFocus()) && this.mKeyboardFocusedVirtualViewId != var1) {
         if (this.mKeyboardFocusedVirtualViewId != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(this.mKeyboardFocusedVirtualViewId);
         }

         this.mKeyboardFocusedVirtualViewId = var1;
         this.onVirtualViewKeyboardFocusChanged(var1, true);
         this.sendEventForVirtualView(var1, 8);
         var2 = true;
      }

      return var2;
   }

   public final boolean sendEventForVirtualView(int var1, int var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (var1 != Integer.MIN_VALUE) {
         if (!this.mManager.isEnabled()) {
            var4 = var3;
         } else {
            ViewParent var5 = this.mHost.getParent();
            var4 = var3;
            if (var5 != null) {
               AccessibilityEvent var6 = this.createEvent(var1, var2);
               var4 = ViewParentCompat.requestSendAccessibilityEvent(var5, this.mHost, var6);
            }
         }
      }

      return var4;
   }

   private class MyNodeProvider extends AccessibilityNodeProviderCompat {
      MyNodeProvider() {
      }

      public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
         return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(var1));
      }

      public AccessibilityNodeInfoCompat findFocus(int var1) {
         if (var1 == 2) {
            var1 = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId;
         } else {
            var1 = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
         }

         AccessibilityNodeInfoCompat var2;
         if (var1 == Integer.MIN_VALUE) {
            var2 = null;
         } else {
            var2 = this.createAccessibilityNodeInfo(var1);
         }

         return var2;
      }

      public boolean performAction(int var1, int var2, Bundle var3) {
         return ExploreByTouchHelper.this.performAction(var1, var2, var3);
      }
   }
}
