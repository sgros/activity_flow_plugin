package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class BottomSheetDialog extends AppCompatDialog {
   private BottomSheetBehavior behavior;
   private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
      public void onSlide(View var1, float var2) {
      }

      public void onStateChanged(View var1, int var2) {
         if (var2 == 5) {
            BottomSheetDialog.this.cancel();
         }

      }
   };
   boolean cancelable = true;
   private boolean canceledOnTouchOutside = true;
   private boolean canceledOnTouchOutsideSet;

   public BottomSheetDialog(Context var1, int var2) {
      super(var1, getThemeResId(var1, var2));
      this.supportRequestWindowFeature(1);
   }

   private static int getThemeResId(Context var0, int var1) {
      int var2 = var1;
      if (var1 == 0) {
         TypedValue var3 = new TypedValue();
         if (var0.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, var3, true)) {
            var2 = var3.resourceId;
         } else {
            var2 = R.style.Theme_Design_Light_BottomSheetDialog;
         }
      }

      return var2;
   }

   private View wrapInBottomSheet(int var1, View var2, LayoutParams var3) {
      FrameLayout var4 = (FrameLayout)View.inflate(this.getContext(), R.layout.design_bottom_sheet_dialog, (ViewGroup)null);
      CoordinatorLayout var5 = (CoordinatorLayout)var4.findViewById(R.id.coordinator);
      View var6 = var2;
      if (var1 != 0) {
         var6 = var2;
         if (var2 == null) {
            var6 = this.getLayoutInflater().inflate(var1, var5, false);
         }
      }

      FrameLayout var7 = (FrameLayout)var5.findViewById(R.id.design_bottom_sheet);
      this.behavior = BottomSheetBehavior.from(var7);
      this.behavior.setBottomSheetCallback(this.bottomSheetCallback);
      this.behavior.setHideable(this.cancelable);
      if (var3 == null) {
         var7.addView(var6);
      } else {
         var7.addView(var6, var3);
      }

      var5.findViewById(R.id.touch_outside).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
               BottomSheetDialog.this.cancel();
            }

         }
      });
      ViewCompat.setAccessibilityDelegate(var7, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            if (BottomSheetDialog.this.cancelable) {
               var2.addAction(1048576);
               var2.setDismissable(true);
            } else {
               var2.setDismissable(false);
            }

         }

         public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
            if (var2 == 1048576 && BottomSheetDialog.this.cancelable) {
               BottomSheetDialog.this.cancel();
               return true;
            } else {
               return super.performAccessibilityAction(var1, var2, var3);
            }
         }
      });
      var7.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            return true;
         }
      });
      return var4;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Window var2 = this.getWindow();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 21) {
            var2.clearFlags(67108864);
            var2.addFlags(Integer.MIN_VALUE);
         }

         var2.setLayout(-1, -1);
      }

   }

   protected void onStart() {
      super.onStart();
      if (this.behavior != null && this.behavior.getState() == 5) {
         this.behavior.setState(4);
      }

   }

   public void setCancelable(boolean var1) {
      super.setCancelable(var1);
      if (this.cancelable != var1) {
         this.cancelable = var1;
         if (this.behavior != null) {
            this.behavior.setHideable(var1);
         }
      }

   }

   public void setCanceledOnTouchOutside(boolean var1) {
      super.setCanceledOnTouchOutside(var1);
      if (var1 && !this.cancelable) {
         this.cancelable = true;
      }

      this.canceledOnTouchOutside = var1;
      this.canceledOnTouchOutsideSet = true;
   }

   public void setContentView(int var1) {
      super.setContentView(this.wrapInBottomSheet(var1, (View)null, (LayoutParams)null));
   }

   public void setContentView(View var1) {
      super.setContentView(this.wrapInBottomSheet(0, var1, (LayoutParams)null));
   }

   public void setContentView(View var1, LayoutParams var2) {
      super.setContentView(this.wrapInBottomSheet(0, var1, var2));
   }

   boolean shouldWindowCloseOnTouchOutside() {
      if (!this.canceledOnTouchOutsideSet) {
         TypedArray var1 = this.getContext().obtainStyledAttributes(new int[]{16843611});
         this.canceledOnTouchOutside = var1.getBoolean(0, true);
         var1.recycle();
         this.canceledOnTouchOutsideSet = true;
      }

      return this.canceledOnTouchOutside;
   }
}
