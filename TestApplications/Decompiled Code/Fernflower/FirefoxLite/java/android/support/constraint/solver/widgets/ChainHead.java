package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
   private boolean mDefined;
   protected ConstraintWidget mFirst;
   protected ConstraintWidget mFirstMatchConstraintWidget;
   protected ConstraintWidget mFirstVisibleWidget;
   protected boolean mHasComplexMatchWeights;
   protected boolean mHasDefinedWeights;
   protected boolean mHasUndefinedWeights;
   protected ConstraintWidget mHead;
   private boolean mIsRtl = false;
   protected ConstraintWidget mLast;
   protected ConstraintWidget mLastMatchConstraintWidget;
   protected ConstraintWidget mLastVisibleWidget;
   private int mOrientation;
   protected float mTotalWeight = 0.0F;
   protected ArrayList mWeightedMatchConstraintsWidgets;
   protected int mWidgetsCount;
   protected int mWidgetsMatchCount;

   public ChainHead(ConstraintWidget var1, int var2, boolean var3) {
      this.mFirst = var1;
      this.mOrientation = var2;
      this.mIsRtl = var3;
   }

   private void defineChainProperties() {
      int var1 = this.mOrientation * 2;
      ConstraintWidget var2 = this.mFirst;
      ConstraintWidget var3 = this.mFirst;
      boolean var4 = false;
      boolean var5 = false;

      while(!var5) {
         ++this.mWidgetsCount;
         ConstraintWidget[] var11 = var2.mListNextVisibleWidget;
         int var6 = this.mOrientation;
         Object var7 = null;
         var11[var6] = null;
         var2.mListNextMatchConstraintsWidget[this.mOrientation] = null;
         if (var2.getVisibility() != 8) {
            if (this.mFirstVisibleWidget == null) {
               this.mFirstVisibleWidget = var2;
            }

            if (this.mLastVisibleWidget != null) {
               this.mLastVisibleWidget.mListNextVisibleWidget[this.mOrientation] = var2;
            }

            this.mLastVisibleWidget = var2;
            if (var2.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (var2.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || var2.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || var2.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
               ++this.mWidgetsMatchCount;
               float var8 = var2.mWeight[this.mOrientation];
               if (var8 > 0.0F) {
                  this.mTotalWeight += var2.mWeight[this.mOrientation];
               }

               if (isMatchConstraintEqualityCandidate(var2, this.mOrientation)) {
                  if (var8 < 0.0F) {
                     this.mHasUndefinedWeights = true;
                  } else {
                     this.mHasDefinedWeights = true;
                  }

                  if (this.mWeightedMatchConstraintsWidgets == null) {
                     this.mWeightedMatchConstraintsWidgets = new ArrayList();
                  }

                  this.mWeightedMatchConstraintsWidgets.add(var2);
               }

               if (this.mFirstMatchConstraintWidget == null) {
                  this.mFirstMatchConstraintWidget = var2;
               }

               if (this.mLastMatchConstraintWidget != null) {
                  this.mLastMatchConstraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = var2;
               }

               this.mLastMatchConstraintWidget = var2;
            }
         }

         ConstraintAnchor var9 = var2.mListAnchors[var1 + 1].mTarget;
         var3 = (ConstraintWidget)var7;
         if (var9 != null) {
            ConstraintWidget var12 = var9.mOwner;
            var3 = (ConstraintWidget)var7;
            if (var12.mListAnchors[var1].mTarget != null) {
               if (var12.mListAnchors[var1].mTarget.mOwner != var2) {
                  var3 = (ConstraintWidget)var7;
               } else {
                  var3 = var12;
               }
            }
         }

         if (var3 != null) {
            var2 = var3;
         } else {
            var5 = true;
         }
      }

      this.mLast = var2;
      if (this.mOrientation == 0 && this.mIsRtl) {
         this.mHead = this.mLast;
      } else {
         this.mHead = this.mFirst;
      }

      boolean var10 = var4;
      if (this.mHasDefinedWeights) {
         var10 = var4;
         if (this.mHasUndefinedWeights) {
            var10 = true;
         }
      }

      this.mHasComplexMatchWeights = var10;
   }

   private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget var0, int var1) {
      boolean var2;
      if (var0.getVisibility() == 8 || var0.mListDimensionBehaviors[var1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || var0.mResolvedMatchConstraintDefault[var1] != 0 && var0.mResolvedMatchConstraintDefault[var1] != 3) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void define() {
      if (!this.mDefined) {
         this.defineChainProperties();
      }

      this.mDefined = true;
   }
}
