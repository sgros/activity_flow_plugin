package android.support.design.transformation;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.design.R;
import android.support.design.animation.MotionSpec;
import android.support.design.animation.Positioning;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import java.util.HashMap;
import java.util.Map;

public class FabTransformationSheetBehavior extends FabTransformationBehavior {
   private Map importantForAccessibilityMap;

   public FabTransformationSheetBehavior() {
   }

   public FabTransformationSheetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void updateImportantForAccessibility(View var1, boolean var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof CoordinatorLayout) {
         CoordinatorLayout var4 = (CoordinatorLayout)var3;
         int var5 = var4.getChildCount();
         if (VERSION.SDK_INT >= 16 && var2) {
            this.importantForAccessibilityMap = new HashMap(var5);
         }

         for(int var6 = 0; var6 < var5; ++var6) {
            View var8 = var4.getChildAt(var6);
            boolean var7;
            if (var8.getLayoutParams() instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)var8.getLayoutParams()).getBehavior() instanceof FabTransformationScrimBehavior) {
               var7 = true;
            } else {
               var7 = false;
            }

            if (var8 != var1 && !var7) {
               if (!var2) {
                  if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(var8)) {
                     ViewCompat.setImportantForAccessibility(var8, (Integer)this.importantForAccessibilityMap.get(var8));
                  }
               } else {
                  if (VERSION.SDK_INT >= 16) {
                     this.importantForAccessibilityMap.put(var8, var8.getImportantForAccessibility());
                  }

                  ViewCompat.setImportantForAccessibility(var8, 4);
               }
            }
         }

         if (!var2) {
            this.importantForAccessibilityMap = null;
         }

      }
   }

   protected FabTransformationBehavior.FabTransformationSpec onCreateMotionSpec(Context var1, boolean var2) {
      int var3;
      if (var2) {
         var3 = R.animator.mtrl_fab_transformation_sheet_expand_spec;
      } else {
         var3 = R.animator.mtrl_fab_transformation_sheet_collapse_spec;
      }

      FabTransformationBehavior.FabTransformationSpec var4 = new FabTransformationBehavior.FabTransformationSpec();
      var4.timings = MotionSpec.createFromResource(var1, var3);
      var4.positioning = new Positioning(17, 0.0F, 0.0F);
      return var4;
   }

   protected boolean onExpandedStateChange(View var1, View var2, boolean var3, boolean var4) {
      this.updateImportantForAccessibility(var2, var3);
      return super.onExpandedStateChange(var1, var2, var3, var4);
   }
}
