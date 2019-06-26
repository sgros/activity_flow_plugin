package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintTableLayout extends ConstraintWidgetContainer {
   public static final int ALIGN_CENTER = 0;
   private static final int ALIGN_FULL = 3;
   public static final int ALIGN_LEFT = 1;
   public static final int ALIGN_RIGHT = 2;
   private ArrayList mHorizontalGuidelines = new ArrayList();
   private ArrayList mHorizontalSlices = new ArrayList();
   private int mNumCols = 0;
   private int mNumRows = 0;
   private int mPadding = 8;
   private boolean mVerticalGrowth = true;
   private ArrayList mVerticalGuidelines = new ArrayList();
   private ArrayList mVerticalSlices = new ArrayList();
   private LinearSystem system = null;

   public ConstraintTableLayout() {
   }

   public ConstraintTableLayout(int var1, int var2) {
      super(var1, var2);
   }

   public ConstraintTableLayout(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   private void setChildrenConnections() {
      int var1 = this.mChildren.size();
      int var2 = 0;

      for(int var3 = 0; var2 < var1; ++var2) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var2);
         int var5 = var3 + var4.getContainerItemSkip();
         var3 = this.mNumCols;
         int var6 = var5 / this.mNumCols;
         ConstraintTableLayout.HorizontalSlice var7 = (ConstraintTableLayout.HorizontalSlice)this.mHorizontalSlices.get(var6);
         ConstraintTableLayout.VerticalSlice var8 = (ConstraintTableLayout.VerticalSlice)this.mVerticalSlices.get(var5 % var3);
         ConstraintWidget var9 = var8.left;
         ConstraintWidget var10 = var8.right;
         ConstraintWidget var11 = var7.top;
         ConstraintWidget var12 = var7.bottom;
         var4.getAnchor(ConstraintAnchor.Type.LEFT).connect(var9.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
         if (var10 instanceof Guideline) {
            var4.getAnchor(ConstraintAnchor.Type.RIGHT).connect(var10.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
         } else {
            var4.getAnchor(ConstraintAnchor.Type.RIGHT).connect(var10.getAnchor(ConstraintAnchor.Type.RIGHT), this.mPadding);
         }

         switch(var8.alignment) {
         case 1:
            var4.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.STRONG);
            var4.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.WEAK);
            break;
         case 2:
            var4.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.WEAK);
            var4.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.STRONG);
            break;
         case 3:
            var4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
         }

         var4.getAnchor(ConstraintAnchor.Type.TOP).connect(var11.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
         if (var12 instanceof Guideline) {
            var4.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(var12.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
         } else {
            var4.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(var12.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mPadding);
         }

         var3 = var5 + 1;
      }

   }

   private void setHorizontalSlices() {
      this.mHorizontalSlices.clear();
      float var1 = 100.0F / (float)this.mNumRows;
      Object var2 = this;
      int var3 = 0;

      for(float var4 = var1; var3 < this.mNumRows; ++var3) {
         ConstraintTableLayout.HorizontalSlice var5 = new ConstraintTableLayout.HorizontalSlice();
         var5.top = (ConstraintWidget)var2;
         if (var3 < this.mNumRows - 1) {
            Guideline var6 = new Guideline();
            var6.setOrientation(0);
            var6.setParent(this);
            var6.setGuidePercent((int)var4);
            var4 += var1;
            var5.bottom = var6;
            this.mHorizontalGuidelines.add(var6);
         } else {
            var5.bottom = this;
         }

         var2 = var5.bottom;
         this.mHorizontalSlices.add(var5);
      }

      this.updateDebugSolverNames();
   }

   private void setVerticalSlices() {
      this.mVerticalSlices.clear();
      float var1 = 100.0F / (float)this.mNumCols;
      int var2 = 0;
      Object var3 = this;

      for(float var4 = var1; var2 < this.mNumCols; ++var2) {
         ConstraintTableLayout.VerticalSlice var5 = new ConstraintTableLayout.VerticalSlice();
         var5.left = (ConstraintWidget)var3;
         if (var2 < this.mNumCols - 1) {
            Guideline var6 = new Guideline();
            var6.setOrientation(1);
            var6.setParent(this);
            var6.setGuidePercent((int)var4);
            var4 += var1;
            var5.right = var6;
            this.mVerticalGuidelines.add(var6);
         } else {
            var5.right = this;
         }

         var3 = var5.right;
         this.mVerticalSlices.add(var5);
      }

      this.updateDebugSolverNames();
   }

   private void updateDebugSolverNames() {
      if (this.system != null) {
         int var1 = this.mVerticalGuidelines.size();
         byte var2 = 0;

         int var3;
         for(var3 = 0; var3 < var1; ++var3) {
            Guideline var4 = (Guideline)this.mVerticalGuidelines.get(var3);
            LinearSystem var5 = this.system;
            StringBuilder var6 = new StringBuilder();
            var6.append(this.getDebugName());
            var6.append(".VG");
            var6.append(var3);
            var4.setDebugSolverName(var5, var6.toString());
         }

         var1 = this.mHorizontalGuidelines.size();

         for(var3 = var2; var3 < var1; ++var3) {
            Guideline var8 = (Guideline)this.mHorizontalGuidelines.get(var3);
            LinearSystem var9 = this.system;
            StringBuilder var7 = new StringBuilder();
            var7.append(this.getDebugName());
            var7.append(".HG");
            var7.append(var3);
            var8.setDebugSolverName(var9, var7.toString());
         }

      }
   }

   public void addToSolver(LinearSystem var1, int var2) {
      super.addToSolver(var1, var2);
      int var3 = this.mChildren.size();
      if (var3 != 0) {
         this.setTableDimensions();
         if (var1 == this.mSystem) {
            int var4 = this.mVerticalGuidelines.size();
            byte var5 = 0;
            int var6 = 0;

            label40:
            while(true) {
               boolean var7 = true;
               Guideline var8;
               if (var6 >= var4) {
                  int var9 = this.mHorizontalGuidelines.size();
                  var6 = 0;

                  while(true) {
                     var4 = var5;
                     if (var6 >= var9) {
                        while(var4 < var3) {
                           ((ConstraintWidget)this.mChildren.get(var4)).addToSolver(var1, var2);
                           ++var4;
                        }
                        break label40;
                     }

                     var8 = (Guideline)this.mHorizontalGuidelines.get(var6);
                     if (this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        var7 = true;
                     } else {
                        var7 = false;
                     }

                     var8.setPositionRelaxed(var7);
                     var8.addToSolver(var1, var2);
                     ++var6;
                  }
               }

               var8 = (Guideline)this.mVerticalGuidelines.get(var6);
               if (this.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var7 = false;
               }

               var8.setPositionRelaxed(var7);
               var8.addToSolver(var1, var2);
               ++var6;
            }
         }

      }
   }

   public void computeGuidelinesPercentPositions() {
      int var1 = this.mVerticalGuidelines.size();
      byte var2 = 0;

      int var3;
      for(var3 = 0; var3 < var1; ++var3) {
         ((Guideline)this.mVerticalGuidelines.get(var3)).inferRelativePercentPosition();
      }

      var1 = this.mHorizontalGuidelines.size();

      for(var3 = var2; var3 < var1; ++var3) {
         ((Guideline)this.mHorizontalGuidelines.get(var3)).inferRelativePercentPosition();
      }

   }

   public void cycleColumnAlignment(int var1) {
      ConstraintTableLayout.VerticalSlice var2 = (ConstraintTableLayout.VerticalSlice)this.mVerticalSlices.get(var1);
      switch(var2.alignment) {
      case 0:
         var2.alignment = 2;
         break;
      case 1:
         var2.alignment = 0;
         break;
      case 2:
         var2.alignment = 1;
      }

      this.setChildrenConnections();
   }

   public String getColumnAlignmentRepresentation(int var1) {
      ConstraintTableLayout.VerticalSlice var2 = (ConstraintTableLayout.VerticalSlice)this.mVerticalSlices.get(var1);
      if (var2.alignment == 1) {
         return "L";
      } else if (var2.alignment == 0) {
         return "C";
      } else if (var2.alignment == 3) {
         return "F";
      } else {
         return var2.alignment == 2 ? "R" : "!";
      }
   }

   public String getColumnsAlignmentRepresentation() {
      int var1 = this.mVerticalSlices.size();
      String var2 = "";

      String var6;
      for(int var3 = 0; var3 < var1; var2 = var6) {
         ConstraintTableLayout.VerticalSlice var4 = (ConstraintTableLayout.VerticalSlice)this.mVerticalSlices.get(var3);
         StringBuilder var5;
         if (var4.alignment == 1) {
            var5 = new StringBuilder();
            var5.append(var2);
            var5.append("L");
            var6 = var5.toString();
         } else if (var4.alignment == 0) {
            var5 = new StringBuilder();
            var5.append(var2);
            var5.append("C");
            var6 = var5.toString();
         } else if (var4.alignment == 3) {
            var5 = new StringBuilder();
            var5.append(var2);
            var5.append("F");
            var6 = var5.toString();
         } else {
            var6 = var2;
            if (var4.alignment == 2) {
               var5 = new StringBuilder();
               var5.append(var2);
               var5.append("R");
               var6 = var5.toString();
            }
         }

         ++var3;
      }

      return var2;
   }

   public ArrayList getHorizontalGuidelines() {
      return this.mHorizontalGuidelines;
   }

   public int getNumCols() {
      return this.mNumCols;
   }

   public int getNumRows() {
      return this.mNumRows;
   }

   public int getPadding() {
      return this.mPadding;
   }

   public String getType() {
      return "ConstraintTableLayout";
   }

   public ArrayList getVerticalGuidelines() {
      return this.mVerticalGuidelines;
   }

   public boolean handlesInternalConstraints() {
      return true;
   }

   public boolean isVerticalGrowth() {
      return this.mVerticalGrowth;
   }

   public void setColumnAlignment(int var1, int var2) {
      if (var1 < this.mVerticalSlices.size()) {
         ((ConstraintTableLayout.VerticalSlice)this.mVerticalSlices.get(var1)).alignment = var2;
         this.setChildrenConnections();
      }

   }

   public void setColumnAlignment(String var1) {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == 'L') {
            this.setColumnAlignment(var3, 1);
         } else if (var4 == 'C') {
            this.setColumnAlignment(var3, 0);
         } else if (var4 == 'F') {
            this.setColumnAlignment(var3, 3);
         } else if (var4 == 'R') {
            this.setColumnAlignment(var3, 2);
         } else {
            this.setColumnAlignment(var3, 0);
         }
      }

   }

   public void setDebugSolverName(LinearSystem var1, String var2) {
      this.system = var1;
      super.setDebugSolverName(var1, var2);
      this.updateDebugSolverNames();
   }

   public void setNumCols(int var1) {
      if (this.mVerticalGrowth && this.mNumCols != var1) {
         this.mNumCols = var1;
         this.setVerticalSlices();
         this.setTableDimensions();
      }

   }

   public void setNumRows(int var1) {
      if (!this.mVerticalGrowth && this.mNumCols != var1) {
         this.mNumRows = var1;
         this.setHorizontalSlices();
         this.setTableDimensions();
      }

   }

   public void setPadding(int var1) {
      if (var1 > 1) {
         this.mPadding = var1;
      }

   }

   public void setTableDimensions() {
      int var1 = this.mChildren.size();
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var1; ++var2) {
         var3 += ((ConstraintWidget)this.mChildren.get(var2)).getContainerItemSkip();
      }

      var1 += var3;
      if (this.mVerticalGrowth) {
         if (this.mNumCols == 0) {
            this.setNumCols(1);
         }

         var3 = var1 / this.mNumCols;
         var2 = var3;
         if (this.mNumCols * var3 < var1) {
            var2 = var3 + 1;
         }

         if (this.mNumRows == var2 && this.mVerticalGuidelines.size() == this.mNumCols - 1) {
            return;
         }

         this.mNumRows = var2;
         this.setHorizontalSlices();
      } else {
         if (this.mNumRows == 0) {
            this.setNumRows(1);
         }

         var3 = var1 / this.mNumRows;
         var2 = var3;
         if (this.mNumRows * var3 < var1) {
            var2 = var3 + 1;
         }

         if (this.mNumCols == var2 && this.mHorizontalGuidelines.size() == this.mNumRows - 1) {
            return;
         }

         this.mNumCols = var2;
         this.setVerticalSlices();
      }

      this.setChildrenConnections();
   }

   public void setVerticalGrowth(boolean var1) {
      this.mVerticalGrowth = var1;
   }

   public void updateFromSolver(LinearSystem var1, int var2) {
      super.updateFromSolver(var1, var2);
      if (var1 == this.mSystem) {
         int var3 = this.mVerticalGuidelines.size();
         byte var4 = 0;

         int var5;
         for(var5 = 0; var5 < var3; ++var5) {
            ((Guideline)this.mVerticalGuidelines.get(var5)).updateFromSolver(var1, var2);
         }

         var3 = this.mHorizontalGuidelines.size();

         for(var5 = var4; var5 < var3; ++var5) {
            ((Guideline)this.mHorizontalGuidelines.get(var5)).updateFromSolver(var1, var2);
         }
      }

   }

   class HorizontalSlice {
      ConstraintWidget bottom;
      int padding;
      ConstraintWidget top;
   }

   class VerticalSlice {
      int alignment = 1;
      ConstraintWidget left;
      int padding;
      ConstraintWidget right;
   }
}
