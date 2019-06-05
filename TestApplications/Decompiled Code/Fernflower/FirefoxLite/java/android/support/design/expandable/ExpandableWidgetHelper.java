package android.support.design.expandable;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewParent;

public final class ExpandableWidgetHelper {
   private boolean expanded;
   private int expandedComponentIdHint;
   private final View widget;

   private void dispatchExpandedStateChanged() {
      ViewParent var1 = this.widget.getParent();
      if (var1 instanceof CoordinatorLayout) {
         ((CoordinatorLayout)var1).dispatchDependentViewsChanged(this.widget);
      }

   }

   public int getExpandedComponentIdHint() {
      return this.expandedComponentIdHint;
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void onRestoreInstanceState(Bundle var1) {
      this.expanded = var1.getBoolean("expanded", false);
      this.expandedComponentIdHint = var1.getInt("expandedComponentIdHint", 0);
      if (this.expanded) {
         this.dispatchExpandedStateChanged();
      }

   }

   public Bundle onSaveInstanceState() {
      Bundle var1 = new Bundle();
      var1.putBoolean("expanded", this.expanded);
      var1.putInt("expandedComponentIdHint", this.expandedComponentIdHint);
      return var1;
   }

   public void setExpandedComponentIdHint(int var1) {
      this.expandedComponentIdHint = var1;
   }
}
