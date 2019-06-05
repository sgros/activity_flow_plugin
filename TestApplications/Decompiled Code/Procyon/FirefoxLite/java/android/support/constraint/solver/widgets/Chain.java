// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import android.support.constraint.solver.LinearSystem;

class Chain
{
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n) {
        int i = 0;
        int n2;
        ChainHead[] array;
        int n3;
        if (n == 0) {
            n2 = constraintWidgetContainer.mHorizontalChainsSize;
            array = constraintWidgetContainer.mHorizontalChainsArray;
            n3 = 0;
        }
        else {
            n3 = 2;
            n2 = constraintWidgetContainer.mVerticalChainsSize;
            array = constraintWidgetContainer.mVerticalChainsArray;
        }
        while (i < n2) {
            final ChainHead chainHead = array[i];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, n, n3, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, n, n3, chainHead);
                }
            }
            else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, n, n3, chainHead);
            }
            ++i;
        }
    }
    
    static void applyChainConstraints(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, int n, int margin, final ChainHead chainHead) {
        final ConstraintWidget mFirst = chainHead.mFirst;
        final ConstraintWidget mLast = chainHead.mLast;
        final ConstraintWidget mFirstVisibleWidget = chainHead.mFirstVisibleWidget;
        final ConstraintWidget mLastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget mHead = chainHead.mHead;
        final float mTotalWeight = chainHead.mTotalWeight;
        final ConstraintWidget mFirstMatchConstraintWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget mLastMatchConstraintWidget = chainHead.mLastMatchConstraintWidget;
        final boolean b = constraintWidgetContainer.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        int n3 = 0;
        int n4 = 0;
        boolean b4 = false;
        Label_0150: {
            Label_0147: {
                int n2;
                if (n == 0) {
                    if (mHead.mHorizontalChainStyle == 0) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    final boolean b2 = (n3 = ((mHead.mHorizontalChainStyle == 1) ? 1 : 0)) != 0;
                    n4 = n2;
                    if (mHead.mHorizontalChainStyle != 2) {
                        break Label_0147;
                    }
                    n3 = (b2 ? 1 : 0);
                }
                else {
                    if (mHead.mVerticalChainStyle == 0) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    final boolean b3 = (n3 = ((mHead.mVerticalChainStyle == 1) ? 1 : 0)) != 0;
                    n4 = n2;
                    if (mHead.mVerticalChainStyle != 2) {
                        break Label_0147;
                    }
                    n3 = (b3 ? 1 : 0);
                }
                b4 = true;
                n4 = n2;
                break Label_0150;
            }
            b4 = false;
        }
        ConstraintWidget constraintWidget = mFirst;
        int n5 = 0;
        final boolean b5 = b4;
        while (true) {
            final ConstraintWidget constraintWidget2 = null;
            if (n5 != 0) {
                break;
            }
            final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[margin];
            int n6;
            if (!b && !b5) {
                n6 = 4;
            }
            else {
                n6 = 1;
            }
            int margin2;
            final int n7 = margin2 = constraintAnchor.getMargin();
            if (constraintAnchor.mTarget != null) {
                margin2 = n7;
                if (constraintWidget != mFirst) {
                    margin2 = n7 + constraintAnchor.mTarget.getMargin();
                }
            }
            if (b5 && constraintWidget != mFirst && constraintWidget != mFirstVisibleWidget) {
                n6 = 6;
            }
            else if (n4 != 0 && b) {
                n6 = 4;
            }
            if (constraintAnchor.mTarget != null) {
                if (constraintWidget == mFirstVisibleWidget) {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 5);
                }
                else {
                    linearSystem.addGreaterThan(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, 6);
                }
                linearSystem.addEquality(constraintAnchor.mSolverVariable, constraintAnchor.mTarget.mSolverVariable, margin2, n6);
            }
            if (b) {
                if (constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin + 1].mSolverVariable, constraintWidget.mListAnchors[margin].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget.mListAnchors[margin].mSolverVariable, constraintWidgetContainer.mListAnchors[margin].mSolverVariable, 0, 6);
            }
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[margin + 1].mTarget;
            ConstraintWidget constraintWidget3 = constraintWidget2;
            if (mTarget != null) {
                final ConstraintWidget mOwner = mTarget.mOwner;
                constraintWidget3 = constraintWidget2;
                if (mOwner.mListAnchors[margin].mTarget != null) {
                    if (mOwner.mListAnchors[margin].mTarget.mOwner != constraintWidget) {
                        constraintWidget3 = constraintWidget2;
                    }
                    else {
                        constraintWidget3 = mOwner;
                    }
                }
            }
            if (constraintWidget3 != null) {
                constraintWidget = constraintWidget3;
            }
            else {
                n5 = 1;
            }
        }
        if (mLastVisibleWidget != null) {
            final ConstraintAnchor[] mListAnchors = mLast.mListAnchors;
            final int n8 = margin + 1;
            if (mListAnchors[n8].mTarget != null) {
                final ConstraintAnchor constraintAnchor2 = mLastVisibleWidget.mListAnchors[n8];
                linearSystem.addLowerThan(constraintAnchor2.mSolverVariable, mLast.mListAnchors[n8].mTarget.mSolverVariable, -constraintAnchor2.getMargin(), 5);
            }
        }
        if (b) {
            final ConstraintAnchor[] mListAnchors2 = constraintWidgetContainer.mListAnchors;
            final int n9 = margin + 1;
            linearSystem.addGreaterThan(mListAnchors2[n9].mSolverVariable, mLast.mListAnchors[n9].mSolverVariable, mLast.mListAnchors[n9].getMargin(), 6);
        }
        final ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets = chainHead.mWeightedMatchConstraintsWidgets;
        if (mWeightedMatchConstraintsWidgets != null) {
            final int size = mWeightedMatchConstraintsWidgets.size();
            if (size > 1) {
                float n10;
                if (chainHead.mHasUndefinedWeights && !chainHead.mHasComplexMatchWeights) {
                    n10 = (float)chainHead.mWidgetsMatchCount;
                }
                else {
                    n10 = mTotalWeight;
                }
                ConstraintWidget constraintWidget4 = null;
                int i = 0;
                float n11 = 0.0f;
                while (i < size) {
                    final ConstraintWidget constraintWidget5 = mWeightedMatchConstraintsWidgets.get(i);
                    float n12 = constraintWidget5.mWeight[n];
                    Label_0910: {
                        if (n12 < 0.0f) {
                            if (chainHead.mHasComplexMatchWeights) {
                                linearSystem.addEquality(constraintWidget5.mListAnchors[margin + 1].mSolverVariable, constraintWidget5.mListAnchors[margin].mSolverVariable, 0, 4);
                                break Label_0910;
                            }
                            n12 = 1.0f;
                        }
                        if (n12 == 0.0f) {
                            linearSystem.addEquality(constraintWidget5.mListAnchors[margin + 1].mSolverVariable, constraintWidget5.mListAnchors[margin].mSolverVariable, 0, 6);
                        }
                        else {
                            if (constraintWidget4 != null) {
                                final SolverVariable mSolverVariable = constraintWidget4.mListAnchors[margin].mSolverVariable;
                                final ConstraintAnchor[] mListAnchors3 = constraintWidget4.mListAnchors;
                                final int n13 = margin + 1;
                                final SolverVariable mSolverVariable2 = mListAnchors3[n13].mSolverVariable;
                                final SolverVariable mSolverVariable3 = constraintWidget5.mListAnchors[margin].mSolverVariable;
                                final SolverVariable mSolverVariable4 = constraintWidget5.mListAnchors[n13].mSolverVariable;
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualMatchDimensions(n11, n10, n12, mSolverVariable, mSolverVariable2, mSolverVariable3, mSolverVariable4);
                                linearSystem.addConstraint(row);
                            }
                            constraintWidget4 = constraintWidget5;
                            n11 = n12;
                        }
                    }
                    ++i;
                }
            }
        }
        if (mFirstVisibleWidget != null && (mFirstVisibleWidget == mLastVisibleWidget || b5)) {
            final ConstraintAnchor constraintAnchor3 = mFirst.mListAnchors[margin];
            final ConstraintAnchor[] mListAnchors4 = mLast.mListAnchors;
            final int n14 = margin + 1;
            ConstraintAnchor constraintAnchor4 = mListAnchors4[n14];
            SolverVariable mSolverVariable5;
            if (mFirst.mListAnchors[margin].mTarget != null) {
                mSolverVariable5 = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable5 = null;
            }
            SolverVariable mSolverVariable6;
            if (mLast.mListAnchors[n14].mTarget != null) {
                mSolverVariable6 = mLast.mListAnchors[n14].mTarget.mSolverVariable;
            }
            else {
                mSolverVariable6 = null;
            }
            ConstraintAnchor constraintAnchor5 = constraintAnchor3;
            if (mFirstVisibleWidget == mLastVisibleWidget) {
                constraintAnchor5 = mFirstVisibleWidget.mListAnchors[margin];
                constraintAnchor4 = mFirstVisibleWidget.mListAnchors[n14];
            }
            if (mSolverVariable5 != null && mSolverVariable6 != null) {
                float n15;
                if (n == 0) {
                    n15 = mHead.mHorizontalBiasPercent;
                }
                else {
                    n15 = mHead.mVerticalBiasPercent;
                }
                final int margin3 = constraintAnchor5.getMargin();
                n = constraintAnchor4.getMargin();
                linearSystem.addCentering(constraintAnchor5.mSolverVariable, mSolverVariable5, margin3, n15, mSolverVariable6, constraintAnchor4.mSolverVariable, n, 5);
            }
        }
        else if (n4 != 0 && mFirstVisibleWidget != null) {
            final boolean b6 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget7;
            ConstraintWidget constraintWidget9;
            for (ConstraintWidget constraintWidget6 = constraintWidget7 = mFirstVisibleWidget; constraintWidget6 != null; constraintWidget6 = constraintWidget9) {
                final ConstraintWidget constraintWidget8 = constraintWidget6.mListNextVisibleWidget[n];
                if (constraintWidget8 == null && constraintWidget6 != mLastVisibleWidget) {
                    constraintWidget9 = constraintWidget8;
                }
                else {
                    final ConstraintAnchor constraintAnchor6 = constraintWidget6.mListAnchors[margin];
                    final SolverVariable mSolverVariable7 = constraintAnchor6.mSolverVariable;
                    SolverVariable mSolverVariable8;
                    if (constraintAnchor6.mTarget != null) {
                        mSolverVariable8 = constraintAnchor6.mTarget.mSolverVariable;
                    }
                    else {
                        mSolverVariable8 = null;
                    }
                    SolverVariable solverVariable;
                    if (constraintWidget7 != constraintWidget6) {
                        solverVariable = constraintWidget7.mListAnchors[margin + 1].mSolverVariable;
                    }
                    else {
                        solverVariable = mSolverVariable8;
                        if (constraintWidget6 == mFirstVisibleWidget) {
                            solverVariable = mSolverVariable8;
                            if (constraintWidget7 == constraintWidget6) {
                                if (mFirst.mListAnchors[margin].mTarget != null) {
                                    solverVariable = mFirst.mListAnchors[margin].mTarget.mSolverVariable;
                                }
                                else {
                                    solverVariable = null;
                                }
                            }
                        }
                    }
                    final int margin4 = constraintAnchor6.getMargin();
                    final ConstraintAnchor[] mListAnchors5 = constraintWidget6.mListAnchors;
                    final int n16 = margin + 1;
                    final int margin5 = mListAnchors5[n16].getMargin();
                    ConstraintAnchor mTarget2;
                    SolverVariable mSolverVariable9;
                    SolverVariable solverVariable2;
                    if (constraintWidget8 != null) {
                        mTarget2 = constraintWidget8.mListAnchors[margin];
                        mSolverVariable9 = mTarget2.mSolverVariable;
                        solverVariable2 = constraintWidget6.mListAnchors[n16].mSolverVariable;
                    }
                    else {
                        mTarget2 = mLast.mListAnchors[n16].mTarget;
                        SolverVariable mSolverVariable10;
                        if (mTarget2 != null) {
                            mSolverVariable10 = mTarget2.mSolverVariable;
                        }
                        else {
                            mSolverVariable10 = null;
                        }
                        solverVariable2 = constraintWidget6.mListAnchors[n16].mSolverVariable;
                        mSolverVariable9 = mSolverVariable10;
                    }
                    final ConstraintWidget constraintWidget10 = constraintWidget8;
                    int margin6 = margin5;
                    if (mTarget2 != null) {
                        margin6 = margin5 + mTarget2.getMargin();
                    }
                    int margin7 = margin4;
                    if (constraintWidget7 != null) {
                        margin7 = margin4 + constraintWidget7.mListAnchors[n16].getMargin();
                    }
                    if (mSolverVariable7 != null && solverVariable != null && mSolverVariable9 != null && solverVariable2 != null) {
                        if (constraintWidget6 == mFirstVisibleWidget) {
                            margin7 = mFirstVisibleWidget.mListAnchors[margin].getMargin();
                        }
                        if (constraintWidget6 == mLastVisibleWidget) {
                            margin6 = mLastVisibleWidget.mListAnchors[n16].getMargin();
                        }
                        int n17;
                        if (b6) {
                            n17 = 6;
                        }
                        else {
                            n17 = 4;
                        }
                        linearSystem.addCentering(mSolverVariable7, solverVariable, margin7, 0.5f, mSolverVariable9, solverVariable2, margin6, n17);
                        constraintWidget9 = constraintWidget10;
                    }
                    else {
                        constraintWidget9 = constraintWidget10;
                    }
                }
                constraintWidget7 = constraintWidget6;
            }
        }
        else if (n3 != 0 && mFirstVisibleWidget != null) {
            final boolean b7 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget12;
            ConstraintWidget constraintWidget11 = constraintWidget12 = mFirstVisibleWidget;
            while (true) {
                final ConstraintWidget constraintWidget13 = constraintWidget11;
                if (constraintWidget13 == null) {
                    break;
                }
                ConstraintWidget constraintWidget14 = constraintWidget13.mListNextVisibleWidget[n];
                if (constraintWidget13 != mFirstVisibleWidget && constraintWidget13 != mLastVisibleWidget && constraintWidget14 != null) {
                    if (constraintWidget14 == mLastVisibleWidget) {
                        constraintWidget14 = null;
                    }
                    final ConstraintAnchor constraintAnchor7 = constraintWidget13.mListAnchors[margin];
                    final SolverVariable mSolverVariable11 = constraintAnchor7.mSolverVariable;
                    if (constraintAnchor7.mTarget != null) {
                        final SolverVariable mSolverVariable12 = constraintAnchor7.mTarget.mSolverVariable;
                    }
                    final ConstraintAnchor[] mListAnchors6 = constraintWidget12.mListAnchors;
                    final int n18 = margin + 1;
                    final SolverVariable mSolverVariable13 = mListAnchors6[n18].mSolverVariable;
                    final int margin8 = constraintAnchor7.getMargin();
                    final int margin9 = constraintWidget13.mListAnchors[n18].getMargin();
                    SolverVariable mSolverVariable14;
                    SolverVariable mSolverVariable16;
                    ConstraintAnchor constraintAnchor9;
                    if (constraintWidget14 != null) {
                        final ConstraintAnchor constraintAnchor8 = constraintWidget14.mListAnchors[margin];
                        mSolverVariable14 = constraintAnchor8.mSolverVariable;
                        SolverVariable mSolverVariable15;
                        if (constraintAnchor8.mTarget != null) {
                            mSolverVariable15 = constraintAnchor8.mTarget.mSolverVariable;
                        }
                        else {
                            mSolverVariable15 = null;
                        }
                        mSolverVariable16 = mSolverVariable15;
                        constraintAnchor9 = constraintAnchor8;
                    }
                    else {
                        final ConstraintAnchor mTarget3 = constraintWidget13.mListAnchors[n18].mTarget;
                        SolverVariable mSolverVariable17;
                        if (mTarget3 != null) {
                            mSolverVariable17 = mTarget3.mSolverVariable;
                        }
                        else {
                            mSolverVariable17 = null;
                        }
                        mSolverVariable16 = constraintWidget13.mListAnchors[n18].mSolverVariable;
                        mSolverVariable14 = mSolverVariable17;
                        constraintAnchor9 = mTarget3;
                    }
                    int n19 = margin9;
                    if (constraintAnchor9 != null) {
                        n19 = margin9 + constraintAnchor9.getMargin();
                    }
                    int n20 = margin8;
                    if (constraintWidget12 != null) {
                        n20 = margin8 + constraintWidget12.mListAnchors[n18].getMargin();
                    }
                    int n21;
                    if (b7) {
                        n21 = 6;
                    }
                    else {
                        n21 = 4;
                    }
                    if (mSolverVariable11 != null && mSolverVariable13 != null && mSolverVariable14 != null && mSolverVariable16 != null) {
                        linearSystem.addCentering(mSolverVariable11, mSolverVariable13, n20, 0.5f, mSolverVariable14, mSolverVariable16, n19, n21);
                    }
                }
                constraintWidget12 = constraintWidget13;
                constraintWidget11 = constraintWidget14;
            }
            final ConstraintAnchor constraintAnchor10 = mFirstVisibleWidget.mListAnchors[margin];
            final ConstraintAnchor mTarget4 = mFirst.mListAnchors[margin].mTarget;
            final ConstraintAnchor[] mListAnchors7 = mLastVisibleWidget.mListAnchors;
            n = margin + 1;
            final ConstraintAnchor constraintAnchor11 = mListAnchors7[n];
            final ConstraintAnchor mTarget5 = mLast.mListAnchors[n].mTarget;
            if (mTarget4 != null) {
                if (mFirstVisibleWidget != mLastVisibleWidget) {
                    linearSystem.addEquality(constraintAnchor10.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor10.getMargin(), 5);
                }
                else if (mTarget5 != null) {
                    linearSystem.addCentering(constraintAnchor10.mSolverVariable, mTarget4.mSolverVariable, constraintAnchor10.getMargin(), 0.5f, constraintAnchor11.mSolverVariable, mTarget5.mSolverVariable, constraintAnchor11.getMargin(), 5);
                }
            }
            if (mTarget5 != null && mFirstVisibleWidget != mLastVisibleWidget) {
                linearSystem.addEquality(constraintAnchor11.mSolverVariable, mTarget5.mSolverVariable, -constraintAnchor11.getMargin(), 5);
            }
        }
        if ((n4 != 0 || n3 != 0) && mFirstVisibleWidget != null) {
            ConstraintAnchor constraintAnchor12 = mFirstVisibleWidget.mListAnchors[margin];
            final ConstraintAnchor[] mListAnchors8 = mLastVisibleWidget.mListAnchors;
            n = margin + 1;
            ConstraintAnchor constraintAnchor13 = mListAnchors8[n];
            SolverVariable mSolverVariable18;
            if (constraintAnchor12.mTarget != null) {
                mSolverVariable18 = constraintAnchor12.mTarget.mSolverVariable;
            }
            else {
                mSolverVariable18 = null;
            }
            SolverVariable solverVariable3;
            if (constraintAnchor13.mTarget != null) {
                solverVariable3 = constraintAnchor13.mTarget.mSolverVariable;
            }
            else {
                solverVariable3 = null;
            }
            if (mLast != mLastVisibleWidget) {
                final ConstraintAnchor constraintAnchor14 = mLast.mListAnchors[n];
                if (constraintAnchor14.mTarget != null) {
                    solverVariable3 = constraintAnchor14.mTarget.mSolverVariable;
                }
                else {
                    solverVariable3 = null;
                }
            }
            if (mFirstVisibleWidget == mLastVisibleWidget) {
                constraintAnchor12 = mFirstVisibleWidget.mListAnchors[margin];
                constraintAnchor13 = mFirstVisibleWidget.mListAnchors[n];
            }
            if (mSolverVariable18 != null && solverVariable3 != null) {
                margin = constraintAnchor12.getMargin();
                ConstraintWidget constraintWidget15;
                if (mLastVisibleWidget == null) {
                    constraintWidget15 = mLast;
                }
                else {
                    constraintWidget15 = mLastVisibleWidget;
                }
                n = constraintWidget15.mListAnchors[n].getMargin();
                linearSystem.addCentering(constraintAnchor12.mSolverVariable, mSolverVariable18, margin, 0.5f, solverVariable3, constraintAnchor13.mSolverVariable, n, 5);
            }
        }
    }
}
