package android.support.constraint.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget {
   protected ConstraintWidget[] mWidgets = new ConstraintWidget[4];
   protected int mWidgetsCount = 0;

   public void add(ConstraintWidget var1) {
      if (this.mWidgetsCount + 1 > this.mWidgets.length) {
         this.mWidgets = (ConstraintWidget[])Arrays.copyOf(this.mWidgets, this.mWidgets.length * 2);
      }

      this.mWidgets[this.mWidgetsCount] = var1;
      ++this.mWidgetsCount;
   }

   public void removeAllIds() {
      this.mWidgetsCount = 0;
   }
}
