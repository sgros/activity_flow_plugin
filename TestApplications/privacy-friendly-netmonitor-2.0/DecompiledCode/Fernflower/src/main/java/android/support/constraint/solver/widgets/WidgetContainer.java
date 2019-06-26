package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
   protected ArrayList mChildren = new ArrayList();

   public WidgetContainer() {
   }

   public WidgetContainer(int var1, int var2) {
      super(var1, var2);
   }

   public WidgetContainer(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public static Rectangle getBounds(ArrayList var0) {
      Rectangle var1 = new Rectangle();
      if (var0.size() == 0) {
         return var1;
      } else {
         int var2 = var0.size();
         int var3 = Integer.MAX_VALUE;
         int var4 = 0;
         int var5 = Integer.MAX_VALUE;
         int var6 = 0;

         int var7;
         int var12;
         for(var7 = var6; var4 < var2; var7 = var12) {
            ConstraintWidget var8 = (ConstraintWidget)var0.get(var4);
            int var9 = var3;
            if (var8.getX() < var3) {
               var9 = var8.getX();
            }

            int var10 = var5;
            if (var8.getY() < var5) {
               var10 = var8.getY();
            }

            int var11 = var6;
            if (var8.getRight() > var6) {
               var11 = var8.getRight();
            }

            var12 = var7;
            if (var8.getBottom() > var7) {
               var12 = var8.getBottom();
            }

            ++var4;
            var3 = var9;
            var5 = var10;
            var6 = var11;
         }

         var1.setBounds(var3, var5, var6 - var3, var7 - var5);
         return var1;
      }
   }

   public void add(ConstraintWidget var1) {
      this.mChildren.add(var1);
      if (var1.getParent() != null) {
         ((WidgetContainer)var1.getParent()).remove(var1);
      }

      var1.setParent(this);
   }

   public ConstraintWidget findWidget(float var1, float var2) {
      int var3 = this.getDrawX();
      int var4 = this.getDrawY();
      int var5 = this.getWidth();
      int var6 = this.getHeight();
      Object var7;
      if (var1 >= (float)var3 && var1 <= (float)(var5 + var3) && var2 >= (float)var4 && var2 <= (float)(var6 + var4)) {
         var7 = this;
      } else {
         var7 = null;
      }

      var4 = 0;

      Object var9;
      for(var6 = this.mChildren.size(); var4 < var6; var7 = var9) {
         label36: {
            ConstraintWidget var8 = (ConstraintWidget)this.mChildren.get(var4);
            ConstraintWidget var12;
            if (var8 instanceof WidgetContainer) {
               var8 = ((WidgetContainer)var8).findWidget(var1, var2);
               var9 = var7;
               if (var8 == null) {
                  break label36;
               }

               var12 = var8;
            } else {
               int var10 = var8.getDrawX();
               var5 = var8.getDrawY();
               var3 = var8.getWidth();
               int var11 = var8.getHeight();
               var9 = var7;
               if (var1 < (float)var10) {
                  break label36;
               }

               var9 = var7;
               if (var1 > (float)(var3 + var10)) {
                  break label36;
               }

               var9 = var7;
               if (var2 < (float)var5) {
                  break label36;
               }

               var9 = var7;
               if (var2 > (float)(var11 + var5)) {
                  break label36;
               }

               var12 = var8;
            }

            var9 = var12;
         }

         ++var4;
      }

      return (ConstraintWidget)var7;
   }

   public ArrayList findWidgets(int var1, int var2, int var3, int var4) {
      ArrayList var5 = new ArrayList();
      Rectangle var6 = new Rectangle();
      var6.setBounds(var1, var2, var3, var4);
      var2 = this.mChildren.size();

      for(var1 = 0; var1 < var2; ++var1) {
         ConstraintWidget var7 = (ConstraintWidget)this.mChildren.get(var1);
         Rectangle var8 = new Rectangle();
         var8.setBounds(var7.getDrawX(), var7.getDrawY(), var7.getWidth(), var7.getHeight());
         if (var6.intersects(var8)) {
            var5.add(var7);
         }
      }

      return var5;
   }

   public ArrayList getChildren() {
      return this.mChildren;
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

   public void resetGroups() {
      super.resetGroups();
      int var1 = this.mChildren.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((ConstraintWidget)this.mChildren.get(var2)).resetGroups();
      }

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
