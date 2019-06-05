package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer {
   private ConstraintHorizontalLayout.ContentAlignment mAlignment;

   public ConstraintHorizontalLayout() {
      this.mAlignment = ConstraintHorizontalLayout.ContentAlignment.MIDDLE;
   }

   public ConstraintHorizontalLayout(int var1, int var2) {
      super(var1, var2);
      this.mAlignment = ConstraintHorizontalLayout.ContentAlignment.MIDDLE;
   }

   public ConstraintHorizontalLayout(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.mAlignment = ConstraintHorizontalLayout.ContentAlignment.MIDDLE;
   }

   public void addToSolver(LinearSystem var1, int var2) {
      if (this.mChildren.size() != 0) {
         int var3 = 0;
         int var4 = this.mChildren.size();

         Object var5;
         ConstraintWidget var6;
         ConstraintAnchor.Strength var7;
         for(var5 = this; var3 < var4; var5 = var6) {
            var6 = (ConstraintWidget)this.mChildren.get(var3);
            if (var5 != this) {
               var6.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var5, ConstraintAnchor.Type.RIGHT);
               ((ConstraintWidget)var5).connect(ConstraintAnchor.Type.RIGHT, var6, ConstraintAnchor.Type.LEFT);
            } else {
               var7 = ConstraintAnchor.Strength.STRONG;
               if (this.mAlignment == ConstraintHorizontalLayout.ContentAlignment.END) {
                  var7 = ConstraintAnchor.Strength.WEAK;
               }

               var6.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var5, ConstraintAnchor.Type.LEFT, 0, var7);
            }

            var6.connect(ConstraintAnchor.Type.TOP, this, ConstraintAnchor.Type.TOP);
            var6.connect(ConstraintAnchor.Type.BOTTOM, this, ConstraintAnchor.Type.BOTTOM);
            ++var3;
         }

         if (var5 != this) {
            var7 = ConstraintAnchor.Strength.STRONG;
            if (this.mAlignment == ConstraintHorizontalLayout.ContentAlignment.BEGIN) {
               var7 = ConstraintAnchor.Strength.WEAK;
            }

            ((ConstraintWidget)var5).connect(ConstraintAnchor.Type.RIGHT, this, ConstraintAnchor.Type.RIGHT, 0, var7);
         }
      }

      super.addToSolver(var1, var2);
   }

   public static enum ContentAlignment {
      BEGIN,
      BOTTOM,
      END,
      LEFT,
      MIDDLE,
      RIGHT,
      TOP,
      VERTICAL_MIDDLE;
   }
}
