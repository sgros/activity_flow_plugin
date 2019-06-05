package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Barrier extends Helper {
   private boolean mAllowsGoneWidget = true;
   private int mBarrierType = 0;
   private ArrayList mNodes = new ArrayList(4);

   public void addToSolver(LinearSystem var1) {
      this.mListAnchors[0] = this.mLeft;
      this.mListAnchors[2] = this.mTop;
      this.mListAnchors[1] = this.mRight;
      this.mListAnchors[3] = this.mBottom;

      int var2;
      for(var2 = 0; var2 < this.mListAnchors.length; ++var2) {
         this.mListAnchors[var2].mSolverVariable = var1.createObjectVariable(this.mListAnchors[var2]);
      }

      if (this.mBarrierType >= 0 && this.mBarrierType < 4) {
         ConstraintAnchor var3 = this.mListAnchors[this.mBarrierType];
         var2 = 0;

         ConstraintWidget var4;
         boolean var5;
         while(true) {
            if (var2 >= this.mWidgetsCount) {
               var5 = false;
               break;
            }

            var4 = this.mWidgets[var2];
            if ((this.mAllowsGoneWidget || var4.allowedInBarrier()) && ((this.mBarrierType == 0 || this.mBarrierType == 1) && var4.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || (this.mBarrierType == 2 || this.mBarrierType == 3) && var4.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)) {
               var5 = true;
               break;
            }

            ++var2;
         }

         label130: {
            if (this.mBarrierType != 0 && this.mBarrierType != 1) {
               if (this.getParent().getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  break label130;
               }
            } else if (this.getParent().getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               break label130;
            }

            var5 = false;
         }

         for(var2 = 0; var2 < this.mWidgetsCount; ++var2) {
            var4 = this.mWidgets[var2];
            if (this.mAllowsGoneWidget || var4.allowedInBarrier()) {
               SolverVariable var6 = var1.createObjectVariable(var4.mListAnchors[this.mBarrierType]);
               var4.mListAnchors[this.mBarrierType].mSolverVariable = var6;
               if (this.mBarrierType != 0 && this.mBarrierType != 2) {
                  var1.addGreaterBarrier(var3.mSolverVariable, var6, var5);
               } else {
                  var1.addLowerBarrier(var3.mSolverVariable, var6, var5);
               }
            }
         }

         if (this.mBarrierType == 0) {
            var1.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
            if (!var5) {
               var1.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
            }
         } else if (this.mBarrierType == 1) {
            var1.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
            if (!var5) {
               var1.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
            }
         } else if (this.mBarrierType == 2) {
            var1.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
            if (!var5) {
               var1.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
            }
         } else if (this.mBarrierType == 3) {
            var1.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
            if (!var5) {
               var1.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
            }
         }

      }
   }

   public boolean allowedInBarrier() {
      return true;
   }

   public void analyze(int var1) {
      if (this.mParent != null) {
         if (((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            ResolutionAnchor var2;
            switch(this.mBarrierType) {
            case 0:
               var2 = this.mLeft.getResolutionNode();
               break;
            case 1:
               var2 = this.mRight.getResolutionNode();
               break;
            case 2:
               var2 = this.mTop.getResolutionNode();
               break;
            case 3:
               var2 = this.mBottom.getResolutionNode();
               break;
            default:
               return;
            }

            var2.setType(5);
            if (this.mBarrierType != 0 && this.mBarrierType != 1) {
               this.mLeft.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
               this.mRight.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
            } else {
               this.mTop.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
               this.mBottom.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
            }

            this.mNodes.clear();

            for(var1 = 0; var1 < this.mWidgetsCount; ++var1) {
               ConstraintWidget var3 = this.mWidgets[var1];
               if (this.mAllowsGoneWidget || var3.allowedInBarrier()) {
                  ResolutionAnchor var4;
                  switch(this.mBarrierType) {
                  case 0:
                     var4 = var3.mLeft.getResolutionNode();
                     break;
                  case 1:
                     var4 = var3.mRight.getResolutionNode();
                     break;
                  case 2:
                     var4 = var3.mTop.getResolutionNode();
                     break;
                  case 3:
                     var4 = var3.mBottom.getResolutionNode();
                     break;
                  default:
                     var4 = null;
                  }

                  if (var4 != null) {
                     this.mNodes.add(var4);
                     var4.addDependent(var2);
                  }
               }
            }

         }
      }
   }

   public void resetResolutionNodes() {
      super.resetResolutionNodes();
      this.mNodes.clear();
   }

   public void resolve() {
      int var1;
      float var2;
      ResolutionAnchor var3;
      label51: {
         var1 = this.mBarrierType;
         var2 = Float.MAX_VALUE;
         switch(var1) {
         case 0:
            var3 = this.mLeft.getResolutionNode();
            break label51;
         case 1:
            var3 = this.mRight.getResolutionNode();
            break;
         case 2:
            var3 = this.mTop.getResolutionNode();
            break label51;
         case 3:
            var3 = this.mBottom.getResolutionNode();
            break;
         default:
            return;
         }

         var2 = 0.0F;
      }

      int var4 = this.mNodes.size();
      ResolutionAnchor var5 = null;
      var1 = 0;

      float var6;
      for(var6 = var2; var1 < var4; var6 = var2) {
         ResolutionAnchor var7 = (ResolutionAnchor)this.mNodes.get(var1);
         if (var7.state != 1) {
            return;
         }

         if (this.mBarrierType != 0 && this.mBarrierType != 2) {
            var2 = var6;
            if (var7.resolvedOffset > var6) {
               var2 = var7.resolvedOffset;
               var5 = var7.resolvedTarget;
            }
         } else {
            var2 = var6;
            if (var7.resolvedOffset < var6) {
               var2 = var7.resolvedOffset;
               var5 = var7.resolvedTarget;
            }
         }

         ++var1;
      }

      if (LinearSystem.getMetrics() != null) {
         Metrics var8 = LinearSystem.getMetrics();
         ++var8.barrierConnectionResolved;
      }

      var3.resolvedTarget = var5;
      var3.resolvedOffset = var6;
      var3.didResolve();
      switch(this.mBarrierType) {
      case 0:
         this.mRight.getResolutionNode().resolve(var5, var6);
         break;
      case 1:
         this.mLeft.getResolutionNode().resolve(var5, var6);
         break;
      case 2:
         this.mBottom.getResolutionNode().resolve(var5, var6);
         break;
      case 3:
         this.mTop.getResolutionNode().resolve(var5, var6);
         break;
      default:
         return;
      }

   }

   public void setAllowsGoneWidget(boolean var1) {
      this.mAllowsGoneWidget = var1;
   }

   public void setBarrierType(int var1) {
      this.mBarrierType = var1;
   }
}
