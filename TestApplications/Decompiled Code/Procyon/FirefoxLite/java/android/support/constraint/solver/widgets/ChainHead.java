// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead
{
    private boolean mDefined;
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mFirstVisibleWidget;
    protected boolean mHasComplexMatchWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasUndefinedWeights;
    protected ConstraintWidget mHead;
    private boolean mIsRtl;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ConstraintWidget mLastVisibleWidget;
    private int mOrientation;
    protected float mTotalWeight;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;
    
    public ChainHead(final ConstraintWidget mFirst, final int mOrientation, final boolean mIsRtl) {
        this.mTotalWeight = 0.0f;
        this.mIsRtl = false;
        this.mFirst = mFirst;
        this.mOrientation = mOrientation;
        this.mIsRtl = mIsRtl;
    }
    
    private void defineChainProperties() {
        final int n = this.mOrientation * 2;
        ConstraintWidget mFirst = this.mFirst;
        final ConstraintWidget mFirst2 = this.mFirst;
        final boolean b = false;
        int i = 0;
        while (i == 0) {
            ++this.mWidgetsCount;
            final ConstraintWidget[] mListNextVisibleWidget = mFirst.mListNextVisibleWidget;
            final int mOrientation = this.mOrientation;
            final ConstraintWidget constraintWidget = null;
            mListNextVisibleWidget[mOrientation] = null;
            mFirst.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (mFirst.getVisibility() != 8) {
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = mFirst;
                }
                if (this.mLastVisibleWidget != null) {
                    this.mLastVisibleWidget.mListNextVisibleWidget[this.mOrientation] = mFirst;
                }
                this.mLastVisibleWidget = mFirst;
                if (mFirst.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (mFirst.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || mFirst.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || mFirst.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
                    ++this.mWidgetsMatchCount;
                    final float n2 = mFirst.mWeight[this.mOrientation];
                    if (n2 > 0.0f) {
                        this.mTotalWeight += mFirst.mWeight[this.mOrientation];
                    }
                    if (isMatchConstraintEqualityCandidate(mFirst, this.mOrientation)) {
                        if (n2 < 0.0f) {
                            this.mHasUndefinedWeights = true;
                        }
                        else {
                            this.mHasDefinedWeights = true;
                        }
                        if (this.mWeightedMatchConstraintsWidgets == null) {
                            this.mWeightedMatchConstraintsWidgets = new ArrayList<ConstraintWidget>();
                        }
                        this.mWeightedMatchConstraintsWidgets.add(mFirst);
                    }
                    if (this.mFirstMatchConstraintWidget == null) {
                        this.mFirstMatchConstraintWidget = mFirst;
                    }
                    if (this.mLastMatchConstraintWidget != null) {
                        this.mLastMatchConstraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = mFirst;
                    }
                    this.mLastMatchConstraintWidget = mFirst;
                }
            }
            final ConstraintAnchor mTarget = mFirst.mListAnchors[n + 1].mTarget;
            ConstraintWidget constraintWidget2 = constraintWidget;
            if (mTarget != null) {
                final ConstraintWidget mOwner = mTarget.mOwner;
                constraintWidget2 = constraintWidget;
                if (mOwner.mListAnchors[n].mTarget != null) {
                    if (mOwner.mListAnchors[n].mTarget.mOwner != mFirst) {
                        constraintWidget2 = constraintWidget;
                    }
                    else {
                        constraintWidget2 = mOwner;
                    }
                }
            }
            if (constraintWidget2 != null) {
                mFirst = constraintWidget2;
            }
            else {
                i = 1;
            }
        }
        this.mLast = mFirst;
        if (this.mOrientation == 0 && this.mIsRtl) {
            this.mHead = this.mLast;
        }
        else {
            this.mHead = this.mFirst;
        }
        boolean mHasComplexMatchWeights = b;
        if (this.mHasDefinedWeights) {
            mHasComplexMatchWeights = b;
            if (this.mHasUndefinedWeights) {
                mHasComplexMatchWeights = true;
            }
        }
        this.mHasComplexMatchWeights = mHasComplexMatchWeights;
    }
    
    private static boolean isMatchConstraintEqualityCandidate(final ConstraintWidget constraintWidget, final int n) {
        return constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[n] == 0 || constraintWidget.mResolvedMatchConstraintDefault[n] == 3);
    }
    
    public void define() {
        if (!this.mDefined) {
            this.defineChainProperties();
        }
        this.mDefined = true;
    }
}
