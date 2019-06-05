package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
   protected ArrayList mChildren = new ArrayList();

   public void add(ConstraintWidget var1) {
      this.mChildren.add(var1);
      if (var1.getParent() != null) {
         ((WidgetContainer)var1.getParent()).remove(var1);
      }

      var1.setParent(this);
   }

   public ConstraintWidgetContainer getRootConstraintContainer() {
      ConstraintWidget var1 = this.getParent();
      ConstraintWidgetContainer var2;
      if (this instanceof ConstraintWidgetContainer) {
         var2 = (ConstraintWidgetContainer)this;
      } else {
         var2 = null;
      }

      ConstraintWidget var3;
      for(; var1 != null; var1 = var3) {
         var3 = var1.getParent();
         if (var1 instanceof ConstraintWidgetContainer) {
            var2 = (ConstraintWidgetContainer)var1;
         }
      }

      return var2;
   }

   public void layout() {
      this.updateDrawPosition();
      if (this.mChildren != null) {
         int var1 = this.mChildren.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ConstraintWidget var3 = (ConstraintWidget)this.mChildren.get(var2);
            if (var3 instanceof WidgetContainer) {
               ((WidgetContainer)var3).layout();
            }
         }

      }
   }

   public void remove(ConstraintWidget var1) {
      this.mChildren.remove(var1);
      var1.setParent((ConstraintWidget)null);
   }

   public void removeAllChildren() {
      this.mChildren.clear();
   }

   public void reset() {
      this.mChildren.clear();
      super.reset();
   }

   public void resetSolverVariables(Cache var1) {
      super.resetSolverVariables(var1);
      int var2 = this.mChildren.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((ConstraintWidget)this.mChildren.get(var3)).resetSolverVariables(var1);
      }

   }

   public void setOffset(int var1, int var2) {
      super.setOffset(var1, var2);
      var2 = this.mChildren.size();

      for(var1 = 0; var1 < var2; ++var1) {
         ((ConstraintWidget)this.mChildren.get(var1)).setOffset(this.getRootX(), this.getRootY());
      }

   }

   public void updateDrawPosition() {
      super.updateDrawPosition();
      if (this.mChildren != null) {
         int var1 = this.mChildren.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            ConstraintWidget var3 = (ConstraintWidget)this.mChildren.get(var2);
            var3.setOffset(this.getDrawX(), this.getDrawY());
            if (!(var3 instanceof ConstraintWidgetContainer)) {
               var3.updateDrawPosition();
            }
         }

      }
   }
}
