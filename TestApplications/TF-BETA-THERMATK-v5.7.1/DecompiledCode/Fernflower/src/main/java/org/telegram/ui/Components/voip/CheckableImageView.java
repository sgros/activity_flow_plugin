package org.telegram.ui.Components.voip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private boolean mChecked;

   public CheckableImageView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CheckableImageView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public CheckableImageView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public boolean isChecked() {
      return this.mChecked;
   }

   public int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      if (this.isChecked()) {
         ImageView.mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setCheckable(true);
      var1.setChecked(this.isChecked());
   }

   public void setChecked(boolean var1) {
      if (this.mChecked != var1) {
         this.mChecked = var1;
         this.refreshDrawableState();
      }

   }

   public void toggle() {
      this.setChecked(this.mChecked ^ true);
   }
}
