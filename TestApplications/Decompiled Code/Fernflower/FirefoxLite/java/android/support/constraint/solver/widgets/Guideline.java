package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class Guideline extends ConstraintWidget {
   private ConstraintAnchor mAnchor;
   private Rectangle mHead;
   private int mHeadSize;
   private boolean mIsPositionRelaxed;
   private int mMinimumPosition;
   private int mOrientation;
   protected int mRelativeBegin = -1;
   protected int mRelativeEnd = -1;
   protected float mRelativePercent = -1.0F;

   public Guideline() {
      this.mAnchor = this.mTop;
      int var1 = 0;
      this.mOrientation = 0;
      this.mIsPositionRelaxed = false;
      this.mMinimumPosition = 0;
      this.mHead = new Rectangle();
      this.mHeadSize = 8;
      this.mAnchors.clear();
      this.mAnchors.add(this.mAnchor);

      for(int var2 = this.mListAnchors.length; var1 < var2; ++var1) {
         this.mListAnchors[var1] = this.mAnchor;
      }

   }

   public void addToSolver(LinearSystem var1) {
      ConstraintWidgetContainer var2 = (ConstraintWidgetContainer)this.getParent();
      if (var2 != null) {
         ConstraintAnchor var3 = var2.getAnchor(ConstraintAnchor.Type.LEFT);
         ConstraintAnchor var4 = var2.getAnchor(ConstraintAnchor.Type.RIGHT);
         boolean var5;
         if (this.mParent != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (this.mOrientation == 0) {
            var3 = var2.getAnchor(ConstraintAnchor.Type.TOP);
            var4 = var2.getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var5 = true;
            } else {
               var5 = false;
            }
         }

         SolverVariable var6;
         if (this.mRelativeBegin != -1) {
            var6 = var1.createObjectVariable(this.mAnchor);
            var1.addEquality(var6, var1.createObjectVariable(var3), this.mRelativeBegin, 6);
            if (var5) {
               var1.addGreaterThan(var1.createObjectVariable(var4), var6, 0, 5);
            }
         } else if (this.mRelativeEnd != -1) {
            var6 = var1.createObjectVariable(this.mAnchor);
            SolverVariable var7 = var1.createObjectVariable(var4);
            var1.addEquality(var6, var7, -this.mRelativeEnd, 6);
            if (var5) {
               var1.addGreaterThan(var6, var1.createObjectVariable(var3), 0, 5);
               var1.addGreaterThan(var7, var6, 0, 5);
            }
         } else if (this.mRelativePercent != -1.0F) {
            var1.addConstraint(LinearSystem.createRowDimensionPercent(var1, var1.createObjectVariable(this.mAnchor), var1.createObjectVariable(var3), var1.createObjectVariable(var4), this.mRelativePercent, this.mIsPositionRelaxed));
         }

      }
   }

   public boolean allowedInBarrier() {
      return true;
   }

   public void analyze(int var1) {
      ConstraintWidget var2 = this.getParent();
      if (var2 != null) {
         if (this.getOrientation() == 1) {
            this.mTop.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), 0);
            this.mBottom.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
               this.mLeft.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), this.mRelativeBegin);
               this.mRight.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), this.mRelativeBegin);
            } else if (this.mRelativeEnd != -1) {
               this.mLeft.getResolutionNode().dependsOn(1, var2.mRight.getResolutionNode(), -this.mRelativeEnd);
               this.mRight.getResolutionNode().dependsOn(1, var2.mRight.getResolutionNode(), -this.mRelativeEnd);
            } else if (this.mRelativePercent != -1.0F && var2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
               var1 = (int)((float)var2.mWidth * this.mRelativePercent);
               this.mLeft.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), var1);
               this.mRight.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), var1);
            }
         } else {
            this.mLeft.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), 0);
            this.mRight.getResolutionNode().dependsOn(1, var2.mLeft.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
               this.mTop.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), this.mRelativeBegin);
               this.mBottom.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), this.mRelativeBegin);
            } else if (this.mRelativeEnd != -1) {
               this.mTop.getResolutionNode().dependsOn(1, var2.mBottom.getResolutionNode(), -this.mRelativeEnd);
               this.mBottom.getResolutionNode().dependsOn(1, var2.mBottom.getResolutionNode(), -this.mRelativeEnd);
            } else if (this.mRelativePercent != -1.0F && var2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
               var1 = (int)((float)var2.mHeight * this.mRelativePercent);
               this.mTop.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), var1);
               this.mBottom.getResolutionNode().dependsOn(1, var2.mTop.getResolutionNode(), var1);
            }
         }

      }
   }

   public ConstraintAnchor getAnchor(ConstraintAnchor.Type var1) {
      switch(var1) {
      case LEFT:
      case RIGHT:
         if (this.mOrientation == 1) {
            return this.mAnchor;
         }
         break;
      case TOP:
      case BOTTOM:
         if (this.mOrientation == 0) {
            return this.mAnchor;
         }
         break;
      case BASELINE:
      case CENTER:
      case CENTER_X:
      case CENTER_Y:
      case NONE:
         return null;
      }

      throw new AssertionError(var1.name());
   }

   public ArrayList getAnchors() {
      return this.mAnchors;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public void setGuideBegin(int var1) {
      if (var1 > -1) {
         this.mRelativePercent = -1.0F;
         this.mRelativeBegin = var1;
         this.mRelativeEnd = -1;
      }

   }

   public void setGuideEnd(int var1) {
      if (var1 > -1) {
         this.mRelativePercent = -1.0F;
         this.mRelativeBegin = -1;
         this.mRelativeEnd = var1;
      }

   }

   public void setGuidePercent(float var1) {
      if (var1 > -1.0F) {
         this.mRelativePercent = var1;
         this.mRelativeBegin = -1;
         this.mRelativeEnd = -1;
      }

   }

   public void setOrientation(int var1) {
      if (this.mOrientation != var1) {
         this.mOrientation = var1;
         this.mAnchors.clear();
         if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
         } else {
            this.mAnchor = this.mTop;
         }

         this.mAnchors.add(this.mAnchor);
         int var2 = this.mListAnchors.length;

         for(var1 = 0; var1 < var2; ++var1) {
            this.mListAnchors[var1] = this.mAnchor;
         }

      }
   }

   public void updateFromSolver(LinearSystem var1) {
      if (this.getParent() != null) {
         int var2 = var1.getObjectVariableValue(this.mAnchor);
         if (this.mOrientation == 1) {
            this.setX(var2);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
         } else {
            this.setX(0);
            this.setY(var2);
            this.setWidth(this.getParent().getWidth());
            this.setHeight(0);
         }

      }
   }
}
