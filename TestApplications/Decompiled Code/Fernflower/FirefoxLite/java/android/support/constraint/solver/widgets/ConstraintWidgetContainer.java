package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import java.io.PrintStream;
import java.util.Arrays;

public class ConstraintWidgetContainer extends WidgetContainer {
   int mDebugSolverPassCount = 0;
   private boolean mHeightMeasuredTooSmall = false;
   ChainHead[] mHorizontalChainsArray = new ChainHead[4];
   int mHorizontalChainsSize = 0;
   private boolean mIsRtl = false;
   private int mOptimizationLevel = 3;
   int mPaddingBottom;
   int mPaddingLeft;
   int mPaddingRight;
   int mPaddingTop;
   private Snapshot mSnapshot;
   protected LinearSystem mSystem = new LinearSystem();
   ChainHead[] mVerticalChainsArray = new ChainHead[4];
   int mVerticalChainsSize = 0;
   private boolean mWidthMeasuredTooSmall = false;

   private void addHorizontalChain(ConstraintWidget var1) {
      if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
         this.mHorizontalChainsArray = (ChainHead[])Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
      }

      this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(var1, 0, this.isRtl());
      ++this.mHorizontalChainsSize;
   }

   private void addVerticalChain(ConstraintWidget var1) {
      if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
         this.mVerticalChainsArray = (ChainHead[])Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
      }

      this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(var1, 1, this.isRtl());
      ++this.mVerticalChainsSize;
   }

   private void resetChains() {
      this.mHorizontalChainsSize = 0;
      this.mVerticalChainsSize = 0;
   }

   void addChain(ConstraintWidget var1, int var2) {
      if (var2 == 0) {
         this.addHorizontalChain(var1);
      } else if (var2 == 1) {
         this.addVerticalChain(var1);
      }

   }

   public boolean addChildrenToSolver(LinearSystem var1) {
      this.addToSolver(var1);
      int var2 = this.mChildren.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var3);
         if (var4 instanceof ConstraintWidgetContainer) {
            ConstraintWidget.DimensionBehaviour var5 = var4.mListDimensionBehaviors[0];
            ConstraintWidget.DimensionBehaviour var6 = var4.mListDimensionBehaviors[1];
            if (var5 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            if (var6 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            var4.addToSolver(var1);
            if (var5 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var4.setHorizontalDimensionBehaviour(var5);
            }

            if (var6 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var4.setVerticalDimensionBehaviour(var6);
            }
         } else {
            Optimizer.checkMatchParent(this, var1, var4);
            var4.addToSolver(var1);
         }
      }

      if (this.mHorizontalChainsSize > 0) {
         Chain.applyChainConstraints(this, var1, 0);
      }

      if (this.mVerticalChainsSize > 0) {
         Chain.applyChainConstraints(this, var1, 1);
      }

      return true;
   }

   public void analyze(int var1) {
      super.analyze(var1);
      int var2 = this.mChildren.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ((ConstraintWidget)this.mChildren.get(var3)).analyze(var1);
      }

   }

   public int getOptimizationLevel() {
      return this.mOptimizationLevel;
   }

   public boolean handlesInternalConstraints() {
      return false;
   }

   public boolean isHeightMeasuredTooSmall() {
      return this.mHeightMeasuredTooSmall;
   }

   public boolean isRtl() {
      return this.mIsRtl;
   }

   public boolean isWidthMeasuredTooSmall() {
      return this.mWidthMeasuredTooSmall;
   }

   public void layout() {
      int var1 = this.mX;
      int var2 = this.mY;
      int var3 = Math.max(0, this.getWidth());
      int var4 = Math.max(0, this.getHeight());
      this.mWidthMeasuredTooSmall = false;
      this.mHeightMeasuredTooSmall = false;
      if (this.mParent != null) {
         if (this.mSnapshot == null) {
            this.mSnapshot = new Snapshot(this);
         }

         this.mSnapshot.updateFrom(this);
         this.setX(this.mPaddingLeft);
         this.setY(this.mPaddingTop);
         this.resetAnchors();
         this.resetSolverVariables(this.mSystem.getCache());
      } else {
         this.mX = 0;
         this.mY = 0;
      }

      if (this.mOptimizationLevel != 0) {
         if (!this.optimizeFor(8)) {
            this.optimizeReset();
         }

         this.optimize();
         this.mSystem.graphOptimizer = true;
      } else {
         this.mSystem.graphOptimizer = false;
      }

      ConstraintWidget.DimensionBehaviour var5 = this.mListDimensionBehaviors[1];
      ConstraintWidget.DimensionBehaviour var6 = this.mListDimensionBehaviors[0];
      this.resetChains();
      int var7 = this.mChildren.size();

      ConstraintWidget var9;
      for(int var8 = 0; var8 < var7; ++var8) {
         var9 = (ConstraintWidget)this.mChildren.get(var8);
         if (var9 instanceof WidgetContainer) {
            ((WidgetContainer)var9).layout();
         }
      }

      int var10 = 0;
      boolean var11 = true;

      int var12;
      int var16;
      boolean var23;
      for(var23 = false; var11; var10 = var12) {
         var12 = var10 + 1;

         boolean var13;
         label181: {
            label180: {
               Exception var24;
               label196: {
                  label178: {
                     Exception var10000;
                     label197: {
                        boolean var10001;
                        try {
                           this.mSystem.reset();
                           this.createObjectVariables(this.mSystem);
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label197;
                        }

                        for(var10 = 0; var10 < var7; ++var10) {
                           try {
                              ((ConstraintWidget)this.mChildren.get(var10)).createObjectVariables(this.mSystem);
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label197;
                           }
                        }

                        try {
                           var13 = this.addChildrenToSolver(this.mSystem);
                           break label178;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                        }
                     }

                     var24 = var10000;
                     break label196;
                  }

                  var11 = var13;
                  if (!var13) {
                     break label181;
                  }

                  try {
                     this.mSystem.minimize();
                     break label180;
                  } catch (Exception var19) {
                     var24 = var19;
                     var11 = var13;
                  }
               }

               var24.printStackTrace();
               PrintStream var14 = System.out;
               StringBuilder var15 = new StringBuilder();
               var15.append("EXCEPTION : ");
               var15.append(var24);
               var14.println(var15.toString());
               break label181;
            }

            var11 = var13;
         }

         if (var11) {
            this.updateChildrenFromSolver(this.mSystem, Optimizer.flags);
         } else {
            this.updateFromSolver(this.mSystem);

            for(var10 = 0; var10 < var7; ++var10) {
               var9 = (ConstraintWidget)this.mChildren.get(var10);
               if (var9.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var9.getWidth() < var9.getWrapWidth()) {
                  Optimizer.flags[2] = true;
                  break;
               }

               if (var9.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var9.getHeight() < var9.getWrapHeight()) {
                  Optimizer.flags[2] = true;
                  break;
               }
            }
         }

         if (var12 < 8 && Optimizer.flags[2]) {
            var16 = 0;
            int var17 = 0;

            for(var10 = 0; var16 < var7; ++var16) {
               var9 = (ConstraintWidget)this.mChildren.get(var16);
               var17 = Math.max(var17, var9.mX + var9.getWidth());
               var10 = Math.max(var10, var9.mY + var9.getHeight());
            }

            var16 = Math.max(this.mMinWidth, var17);
            var10 = Math.max(this.mMinHeight, var10);
            if (var6 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.getWidth() < var16) {
               this.setWidth(var16);
               this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
               var11 = true;
               var23 = true;
            } else {
               var11 = false;
            }

            if (var5 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.getHeight() < var10) {
               this.setHeight(var10);
               this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
               var11 = true;
               var23 = true;
            }
         } else {
            var11 = false;
         }

         var10 = Math.max(this.mMinWidth, this.getWidth());
         if (var10 > this.getWidth()) {
            this.setWidth(var10);
            this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
            var11 = true;
            var23 = true;
         }

         var10 = Math.max(this.mMinHeight, this.getHeight());
         if (var10 > this.getHeight()) {
            this.setHeight(var10);
            this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
            var11 = true;
            var23 = true;
         }

         boolean var18 = var11;
         boolean var25 = var23;
         if (!var23) {
            var13 = var11;
            boolean var26 = var23;
            if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var13 = var11;
               var26 = var23;
               if (var3 > 0) {
                  var13 = var11;
                  var26 = var23;
                  if (this.getWidth() > var3) {
                     this.mWidthMeasuredTooSmall = true;
                     this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                     this.setWidth(var3);
                     var13 = true;
                     var26 = true;
                  }
               }
            }

            var18 = var13;
            var25 = var26;
            if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var18 = var13;
               var25 = var26;
               if (var4 > 0) {
                  var18 = var13;
                  var25 = var26;
                  if (this.getHeight() > var4) {
                     this.mHeightMeasuredTooSmall = true;
                     this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                     this.setHeight(var4);
                     var11 = true;
                     var23 = true;
                     continue;
                  }
               }
            }
         }

         var11 = var18;
         var23 = var25;
      }

      if (this.mParent != null) {
         var10 = Math.max(this.mMinWidth, this.getWidth());
         var16 = Math.max(this.mMinHeight, this.getHeight());
         this.mSnapshot.applyTo(this);
         this.setWidth(var10 + this.mPaddingLeft + this.mPaddingRight);
         this.setHeight(var16 + this.mPaddingTop + this.mPaddingBottom);
      } else {
         this.mX = var1;
         this.mY = var2;
      }

      if (var23) {
         this.mListDimensionBehaviors[0] = var6;
         this.mListDimensionBehaviors[1] = var5;
      }

      this.resetSolverVariables(this.mSystem.getCache());
      if (this == this.getRootConstraintContainer()) {
         this.updateDrawPosition();
      }

   }

   public void optimize() {
      if (!this.optimizeFor(8)) {
         this.analyze(this.mOptimizationLevel);
      }

      this.solveGraph();
   }

   public boolean optimizeFor(int var1) {
      boolean var2;
      if ((this.mOptimizationLevel & var1) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void optimizeForDimensions(int var1, int var2) {
      if (this.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null) {
         this.mResolutionWidth.resolve(var1);
      }

      if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
         this.mResolutionHeight.resolve(var2);
      }

   }

   public void optimizeReset() {
      int var1 = this.mChildren.size();
      this.resetResolutionNodes();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((ConstraintWidget)this.mChildren.get(var2)).resetResolutionNodes();
      }

   }

   public void preOptimize() {
      this.optimizeReset();
      this.analyze(this.mOptimizationLevel);
   }

   public void reset() {
      this.mSystem.reset();
      this.mPaddingLeft = 0;
      this.mPaddingRight = 0;
      this.mPaddingTop = 0;
      this.mPaddingBottom = 0;
      super.reset();
   }

   public void setOptimizationLevel(int var1) {
      this.mOptimizationLevel = var1;
   }

   public void setRtl(boolean var1) {
      this.mIsRtl = var1;
   }

   public void solveGraph() {
      ResolutionAnchor var1 = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
      ResolutionAnchor var2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
      var1.resolve((ResolutionAnchor)null, 0.0F);
      var2.resolve((ResolutionAnchor)null, 0.0F);
   }

   public void updateChildrenFromSolver(LinearSystem var1, boolean[] var2) {
      var2[2] = false;
      this.updateFromSolver(var1);
      int var3 = this.mChildren.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         ConstraintWidget var5 = (ConstraintWidget)this.mChildren.get(var4);
         var5.updateFromSolver(var1);
         if (var5.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var5.getWidth() < var5.getWrapWidth()) {
            var2[2] = true;
         }

         if (var5.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var5.getHeight() < var5.getWrapHeight()) {
            var2[2] = true;
         }
      }

   }
}
