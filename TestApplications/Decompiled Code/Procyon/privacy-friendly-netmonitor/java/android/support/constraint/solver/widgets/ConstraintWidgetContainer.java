// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import java.util.Arrays;
import android.support.constraint.solver.LinearSystem;

public class ConstraintWidgetContainer extends WidgetContainer
{
    static boolean ALLOW_ROOT_GROUP = true;
    private static final int CHAIN_FIRST = 0;
    private static final int CHAIN_FIRST_VISIBLE = 2;
    private static final int CHAIN_LAST = 1;
    private static final int CHAIN_LAST_VISIBLE = 3;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_OPTIMIZE = false;
    private static final int FLAG_CHAIN_DANGLING = 1;
    private static final int FLAG_CHAIN_OPTIMIZE = 0;
    private static final int FLAG_RECOMPUTE_BOUNDS = 2;
    private static final int MAX_ITERATIONS = 8;
    public static final int OPTIMIZATION_ALL = 2;
    public static final int OPTIMIZATION_BASIC = 4;
    public static final int OPTIMIZATION_CHAIN = 8;
    public static final int OPTIMIZATION_NONE = 1;
    private static final boolean USE_SNAPSHOT = true;
    private static final boolean USE_THREAD = false;
    private boolean[] flags;
    protected LinearSystem mBackgroundSystem;
    private ConstraintWidget[] mChainEnds;
    private boolean mHeightMeasuredTooSmall;
    private ConstraintWidget[] mHorizontalChainsArray;
    private int mHorizontalChainsSize;
    private ConstraintWidget[] mMatchConstraintsChainedWidgets;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    private ConstraintWidget[] mVerticalChainsArray;
    private int mVerticalChainsSize;
    private boolean mWidthMeasuredTooSmall;
    int mWrapHeight;
    int mWrapWidth;
    
    public ConstraintWidgetContainer() {
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    private void addHorizontalChain(final ConstraintWidget constraintWidget) {
        for (int i = 0; i < this.mHorizontalChainsSize; ++i) {
            if (this.mHorizontalChainsArray[i] == constraintWidget) {
                return;
            }
        }
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = constraintWidget;
        ++this.mHorizontalChainsSize;
    }
    
    private void addVerticalChain(final ConstraintWidget constraintWidget) {
        for (int i = 0; i < this.mVerticalChainsSize; ++i) {
            if (this.mVerticalChainsArray[i] == constraintWidget) {
                return;
            }
        }
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = constraintWidget;
        ++this.mVerticalChainsSize;
    }
    
    private void applyHorizontalChain(final LinearSystem linearSystem) {
        LinearSystem linearSystem2 = linearSystem;
        int n = 0;
        LinearSystem linearSystem6;
        for (int i = 0; i < this.mHorizontalChainsSize; ++i, linearSystem2 = linearSystem6) {
            final ConstraintWidget constraintWidget = this.mHorizontalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem2, this.mChainEnds, this.mHorizontalChainsArray[i], 0, this.flags);
            ConstraintWidget constraintWidget2 = this.mChainEnds[2];
            int n2;
            if (constraintWidget2 == null) {
                n2 = n;
            }
            else if (this.flags[1]) {
                int drawX = constraintWidget.getDrawX();
                while (true) {
                    n2 = n;
                    if (constraintWidget2 == null) {
                        break;
                    }
                    linearSystem2.addEquality(constraintWidget2.mLeft.mSolverVariable, drawX);
                    final ConstraintWidget mHorizontalNextWidget = constraintWidget2.mHorizontalNextWidget;
                    drawX += constraintWidget2.mLeft.getMargin() + constraintWidget2.getWidth() + constraintWidget2.mRight.getMargin();
                    constraintWidget2 = mHorizontalNextWidget;
                }
            }
            else {
                int n3;
                if (constraintWidget.mHorizontalChainStyle == 0) {
                    n3 = 1;
                }
                else {
                    n3 = n;
                }
                int n4;
                if (constraintWidget.mHorizontalChainStyle == 2) {
                    n4 = 1;
                }
                else {
                    n4 = n;
                }
                int n5;
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    n5 = 1;
                }
                else {
                    n5 = n;
                }
                if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[n] && constraintWidget.mHorizontalChainFixedPosition && n4 == 0 && n5 == 0 && constraintWidget.mHorizontalChainStyle == 0) {
                    Optimizer.applyDirectResolutionHorizontalChain(this, linearSystem2, countMatchConstraintsChainedWidgets, constraintWidget);
                    n2 = n;
                }
                else if (countMatchConstraintsChainedWidgets != 0 && n4 == 0) {
                    float n6 = 0.0f;
                    ConstraintWidget constraintWidget3 = null;
                    while (constraintWidget2 != null) {
                        if (constraintWidget2.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                            int margin = constraintWidget2.mLeft.getMargin();
                            if (constraintWidget3 != null) {
                                margin += constraintWidget3.mRight.getMargin();
                            }
                            int n7;
                            if (constraintWidget2.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n7 = 2;
                            }
                            else {
                                n7 = 3;
                            }
                            linearSystem2.addGreaterThan(constraintWidget2.mLeft.mSolverVariable, constraintWidget2.mLeft.mTarget.mSolverVariable, margin, n7);
                            int margin2;
                            final int n8 = margin2 = constraintWidget2.mRight.getMargin();
                            if (constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget != null) {
                                margin2 = n8;
                                if (constraintWidget2.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == constraintWidget2) {
                                    margin2 = n8 + constraintWidget2.mRight.mTarget.mOwner.mLeft.getMargin();
                                }
                            }
                            int n9;
                            if (constraintWidget2.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n9 = 2;
                            }
                            else {
                                n9 = 3;
                            }
                            linearSystem2.addLowerThan(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mRight.mTarget.mSolverVariable, -margin2, n9);
                        }
                        else {
                            n6 += constraintWidget2.mHorizontalWeight;
                            int margin3;
                            if (constraintWidget2.mRight.mTarget != null) {
                                margin3 = constraintWidget2.mRight.getMargin();
                                if (constraintWidget2 != this.mChainEnds[3]) {
                                    margin3 += constraintWidget2.mRight.mTarget.mOwner.mLeft.getMargin();
                                }
                            }
                            else {
                                margin3 = n;
                            }
                            linearSystem2.addGreaterThan(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mLeft.mSolverVariable, n, 1);
                            linearSystem2.addLowerThan(constraintWidget2.mRight.mSolverVariable, constraintWidget2.mRight.mTarget.mSolverVariable, -margin3, 1);
                        }
                        final ConstraintWidget mHorizontalNextWidget2 = constraintWidget2.mHorizontalNextWidget;
                        constraintWidget3 = constraintWidget2;
                        constraintWidget2 = mHorizontalNextWidget2;
                    }
                    if (countMatchConstraintsChainedWidgets == 1) {
                        final ConstraintWidget constraintWidget4 = this.mMatchConstraintsChainedWidgets[n];
                        int margin4;
                        final int n10 = margin4 = constraintWidget4.mLeft.getMargin();
                        if (constraintWidget4.mLeft.mTarget != null) {
                            margin4 = n10 + constraintWidget4.mLeft.mTarget.getMargin();
                        }
                        int margin5;
                        final int n11 = margin5 = constraintWidget4.mRight.getMargin();
                        if (constraintWidget4.mRight.mTarget != null) {
                            margin5 = n11 + constraintWidget4.mRight.mTarget.getMargin();
                        }
                        SolverVariable solverVariable = constraintWidget.mRight.mTarget.mSolverVariable;
                        if (constraintWidget4 == this.mChainEnds[3]) {
                            solverVariable = this.mChainEnds[1].mRight.mTarget.mSolverVariable;
                        }
                        if (constraintWidget4.mMatchConstraintDefaultWidth == 1) {
                            linearSystem2.addGreaterThan(constraintWidget.mLeft.mSolverVariable, constraintWidget.mLeft.mTarget.mSolverVariable, margin4, 1);
                            linearSystem2.addLowerThan(constraintWidget.mRight.mSolverVariable, solverVariable, -margin5, 1);
                            linearSystem2.addEquality(constraintWidget.mRight.mSolverVariable, constraintWidget.mLeft.mSolverVariable, constraintWidget.getWidth(), 2);
                            n2 = n;
                        }
                        else {
                            linearSystem2.addEquality(constraintWidget4.mLeft.mSolverVariable, constraintWidget4.mLeft.mTarget.mSolverVariable, margin4, 1);
                            linearSystem2.addEquality(constraintWidget4.mRight.mSolverVariable, solverVariable, -margin5, 1);
                            n2 = n;
                        }
                    }
                    else {
                        int n12 = n;
                        final int n13 = countMatchConstraintsChainedWidgets;
                        while (true) {
                            final int n14 = n13 - 1;
                            n2 = n;
                            if (n12 >= n14) {
                                break;
                            }
                            final ConstraintWidget constraintWidget5 = this.mMatchConstraintsChainedWidgets[n12];
                            final ConstraintWidget[] mMatchConstraintsChainedWidgets = this.mMatchConstraintsChainedWidgets;
                            ++n12;
                            final ConstraintWidget constraintWidget6 = mMatchConstraintsChainedWidgets[n12];
                            final SolverVariable mSolverVariable = constraintWidget5.mLeft.mSolverVariable;
                            final SolverVariable mSolverVariable2 = constraintWidget5.mRight.mSolverVariable;
                            final SolverVariable mSolverVariable3 = constraintWidget6.mLeft.mSolverVariable;
                            SolverVariable solverVariable2 = constraintWidget6.mRight.mSolverVariable;
                            if (constraintWidget6 == this.mChainEnds[3]) {
                                solverVariable2 = this.mChainEnds[1].mRight.mSolverVariable;
                            }
                            int margin6;
                            final int n15 = margin6 = constraintWidget5.mLeft.getMargin();
                            if (constraintWidget5.mLeft.mTarget != null) {
                                margin6 = n15;
                                if (constraintWidget5.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                    margin6 = n15;
                                    if (constraintWidget5.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget5) {
                                        margin6 = n15 + constraintWidget5.mLeft.mTarget.mOwner.mRight.getMargin();
                                    }
                                }
                            }
                            linearSystem2.addGreaterThan(mSolverVariable, constraintWidget5.mLeft.mTarget.mSolverVariable, margin6, 2);
                            int margin7;
                            final int n16 = margin7 = constraintWidget5.mRight.getMargin();
                            if (constraintWidget5.mRight.mTarget != null) {
                                margin7 = n16;
                                if (constraintWidget5.mHorizontalNextWidget != null) {
                                    int margin8;
                                    if (constraintWidget5.mHorizontalNextWidget.mLeft.mTarget != null) {
                                        margin8 = constraintWidget5.mHorizontalNextWidget.mLeft.getMargin();
                                    }
                                    else {
                                        margin8 = 0;
                                    }
                                    margin7 = n16 + margin8;
                                }
                            }
                            linearSystem2.addLowerThan(mSolverVariable2, constraintWidget5.mRight.mTarget.mSolverVariable, -margin7, 2);
                            if (n12 == n14) {
                                int margin9;
                                final int n17 = margin9 = constraintWidget6.mLeft.getMargin();
                                if (constraintWidget6.mLeft.mTarget != null) {
                                    margin9 = n17;
                                    if (constraintWidget6.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                        margin9 = n17;
                                        if (constraintWidget6.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget6) {
                                            margin9 = n17 + constraintWidget6.mLeft.mTarget.mOwner.mRight.getMargin();
                                        }
                                    }
                                }
                                linearSystem2.addGreaterThan(mSolverVariable3, constraintWidget6.mLeft.mTarget.mSolverVariable, margin9, 2);
                                ConstraintAnchor constraintAnchor = constraintWidget6.mRight;
                                if (constraintWidget6 == this.mChainEnds[3]) {
                                    constraintAnchor = this.mChainEnds[1].mRight;
                                }
                                int margin10;
                                final int n18 = margin10 = constraintAnchor.getMargin();
                                if (constraintAnchor.mTarget != null) {
                                    margin10 = n18;
                                    if (constraintAnchor.mTarget.mOwner.mLeft.mTarget != null) {
                                        margin10 = n18;
                                        if (constraintAnchor.mTarget.mOwner.mLeft.mTarget.mOwner == constraintWidget6) {
                                            margin10 = n18 + constraintAnchor.mTarget.mOwner.mLeft.getMargin();
                                        }
                                    }
                                }
                                linearSystem2.addLowerThan(solverVariable2, constraintAnchor.mTarget.mSolverVariable, -margin10, 2);
                            }
                            if (constraintWidget.mMatchConstraintMaxWidth > 0) {
                                linearSystem2.addLowerThan(mSolverVariable2, mSolverVariable, constraintWidget.mMatchConstraintMaxWidth, 2);
                            }
                            final ArrayRow row = linearSystem.createRow();
                            row.createRowEqualDimension(constraintWidget5.mHorizontalWeight, n6, constraintWidget6.mHorizontalWeight, mSolverVariable, constraintWidget5.mLeft.getMargin(), mSolverVariable2, constraintWidget5.mRight.getMargin(), mSolverVariable3, constraintWidget6.mLeft.getMargin(), solverVariable2, constraintWidget6.mRight.getMargin());
                            linearSystem2.addConstraint(row);
                            n = 0;
                        }
                    }
                }
                else {
                    ConstraintWidget constraintWidget7 = constraintWidget2;
                    ConstraintWidget constraintWidget9;
                    final ConstraintWidget constraintWidget8 = constraintWidget9 = null;
                    boolean b = false;
                    LinearSystem linearSystem3 = linearSystem2;
                    ConstraintWidget constraintWidget10 = constraintWidget8;
                    while (true) {
                        final ConstraintWidget constraintWidget11 = constraintWidget7;
                        if (constraintWidget11 == null) {
                            break;
                        }
                        final ConstraintWidget mHorizontalNextWidget3 = constraintWidget11.mHorizontalNextWidget;
                        ConstraintWidget constraintWidget12;
                        if (mHorizontalNextWidget3 == null) {
                            constraintWidget12 = this.mChainEnds[1];
                            b = true;
                        }
                        else {
                            constraintWidget12 = constraintWidget10;
                        }
                        ConstraintWidget mOwner = null;
                        Label_2323: {
                            if (n4 != 0) {
                                final ConstraintAnchor mLeft = constraintWidget11.mLeft;
                                int margin11 = mLeft.getMargin();
                                if (constraintWidget9 != null) {
                                    margin11 += constraintWidget9.mRight.getMargin();
                                }
                                int n19;
                                if (constraintWidget2 != constraintWidget11) {
                                    n19 = 3;
                                }
                                else {
                                    n19 = 1;
                                }
                                linearSystem3.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, margin11, n19);
                                if (constraintWidget11.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    final ConstraintAnchor mRight = constraintWidget11.mRight;
                                    if (constraintWidget11.mMatchConstraintDefaultWidth == 1) {
                                        linearSystem3.addEquality(mRight.mSolverVariable, mLeft.mSolverVariable, Math.max(constraintWidget11.mMatchConstraintMinWidth, constraintWidget11.getWidth()), 3);
                                    }
                                    else {
                                        linearSystem3.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, mLeft.mMargin, 3);
                                        linearSystem3.addLowerThan(mRight.mSolverVariable, mLeft.mSolverVariable, constraintWidget11.mMatchConstraintMinWidth, 3);
                                    }
                                }
                            }
                            else if (n3 == 0 && b && constraintWidget9 != null) {
                                if (constraintWidget11.mRight.mTarget == null) {
                                    linearSystem3.addEquality(constraintWidget11.mRight.mSolverVariable, constraintWidget11.getDrawRight());
                                }
                                else {
                                    linearSystem3.addEquality(constraintWidget11.mRight.mSolverVariable, constraintWidget12.mRight.mTarget.mSolverVariable, -constraintWidget11.mRight.getMargin(), 5);
                                }
                            }
                            else if (n3 == 0 && !b && constraintWidget9 == null) {
                                if (constraintWidget11.mLeft.mTarget == null) {
                                    linearSystem3.addEquality(constraintWidget11.mLeft.mSolverVariable, constraintWidget11.getDrawX());
                                }
                                else {
                                    linearSystem3.addEquality(constraintWidget11.mLeft.mSolverVariable, constraintWidget.mLeft.mTarget.mSolverVariable, constraintWidget11.mLeft.getMargin(), 5);
                                }
                            }
                            else {
                                final ConstraintAnchor mLeft2 = constraintWidget11.mLeft;
                                final ConstraintAnchor mRight2 = constraintWidget11.mRight;
                                final int margin12 = mLeft2.getMargin();
                                final int margin13 = mRight2.getMargin();
                                linearSystem3.addGreaterThan(mLeft2.mSolverVariable, mLeft2.mTarget.mSolverVariable, margin12, 1);
                                linearSystem3.addLowerThan(mRight2.mSolverVariable, mRight2.mTarget.mSolverVariable, -margin13, 1);
                                SolverVariable solverVariable3;
                                if (mLeft2.mTarget != null) {
                                    solverVariable3 = mLeft2.mTarget.mSolverVariable;
                                }
                                else {
                                    solverVariable3 = null;
                                }
                                if (constraintWidget9 == null) {
                                    if (constraintWidget.mLeft.mTarget != null) {
                                        solverVariable3 = constraintWidget.mLeft.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable3 = null;
                                    }
                                }
                                if ((mOwner = mHorizontalNextWidget3) == null) {
                                    if (constraintWidget12.mRight.mTarget != null) {
                                        mOwner = constraintWidget12.mRight.mTarget.mOwner;
                                    }
                                    else {
                                        mOwner = null;
                                    }
                                }
                                if (mOwner == null) {
                                    break Label_2323;
                                }
                                SolverVariable solverVariable4 = mOwner.mLeft.mSolverVariable;
                                if (b) {
                                    if (constraintWidget12.mRight.mTarget != null) {
                                        solverVariable4 = constraintWidget12.mRight.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable4 = null;
                                    }
                                }
                                if (solverVariable3 != null && solverVariable4 != null) {
                                    linearSystem3.addCentering(mLeft2.mSolverVariable, solverVariable3, margin12, 0.5f, solverVariable4, mRight2.mSolverVariable, margin13, 4);
                                }
                                break Label_2323;
                            }
                            mOwner = mHorizontalNextWidget3;
                        }
                        if (b) {
                            mOwner = null;
                        }
                        final LinearSystem linearSystem4 = linearSystem3;
                        final ConstraintWidget constraintWidget13 = constraintWidget12;
                        constraintWidget9 = constraintWidget11;
                        constraintWidget7 = mOwner;
                        constraintWidget10 = constraintWidget13;
                        linearSystem3 = linearSystem4;
                    }
                    final int n20 = i;
                    final LinearSystem linearSystem5 = linearSystem3;
                    final int n21 = 0;
                    linearSystem6 = linearSystem5;
                    i = n20;
                    n = n21;
                    if (n4 == 0) {
                        continue;
                    }
                    final ConstraintAnchor mLeft3 = constraintWidget2.mLeft;
                    final ConstraintAnchor mRight3 = constraintWidget10.mRight;
                    final int margin14 = mLeft3.getMargin();
                    final int margin15 = mRight3.getMargin();
                    SolverVariable mSolverVariable4;
                    if (constraintWidget.mLeft.mTarget != null) {
                        mSolverVariable4 = constraintWidget.mLeft.mTarget.mSolverVariable;
                    }
                    else {
                        mSolverVariable4 = null;
                    }
                    SolverVariable mSolverVariable5;
                    if (constraintWidget10.mRight.mTarget != null) {
                        mSolverVariable5 = constraintWidget10.mRight.mTarget.mSolverVariable;
                    }
                    else {
                        mSolverVariable5 = null;
                    }
                    linearSystem6 = linearSystem5;
                    i = n20;
                    n = n21;
                    if (mSolverVariable4 == null) {
                        continue;
                    }
                    linearSystem6 = linearSystem5;
                    i = n20;
                    n = n21;
                    if (mSolverVariable5 != null) {
                        linearSystem5.addLowerThan(mRight3.mSolverVariable, mSolverVariable5, -margin15, 1);
                        linearSystem5.addCentering(mLeft3.mSolverVariable, mSolverVariable4, margin14, constraintWidget.mHorizontalBiasPercent, mSolverVariable5, mRight3.mSolverVariable, margin15, 4);
                        n = n21;
                        i = n20;
                        linearSystem6 = linearSystem5;
                    }
                    continue;
                }
            }
            n = n2;
            linearSystem6 = linearSystem2;
        }
    }
    
    private void applyVerticalChain(final LinearSystem linearSystem) {
        LinearSystem linearSystem2 = linearSystem;
        int n = 0;
        LinearSystem linearSystem6;
        for (int i = 0; i < this.mVerticalChainsSize; ++i, linearSystem2 = linearSystem6) {
            final ConstraintWidget constraintWidget = this.mVerticalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem2, this.mChainEnds, this.mVerticalChainsArray[i], 1, this.flags);
            ConstraintWidget constraintWidget2 = this.mChainEnds[2];
            int n2;
            if (constraintWidget2 == null) {
                n2 = n;
            }
            else if (this.flags[1]) {
                int drawY = constraintWidget.getDrawY();
                while (true) {
                    n2 = n;
                    if (constraintWidget2 == null) {
                        break;
                    }
                    linearSystem2.addEquality(constraintWidget2.mTop.mSolverVariable, drawY);
                    final ConstraintWidget mVerticalNextWidget = constraintWidget2.mVerticalNextWidget;
                    drawY += constraintWidget2.mTop.getMargin() + constraintWidget2.getHeight() + constraintWidget2.mBottom.getMargin();
                    constraintWidget2 = mVerticalNextWidget;
                }
            }
            else {
                int n3;
                if (constraintWidget.mVerticalChainStyle == 0) {
                    n3 = 1;
                }
                else {
                    n3 = n;
                }
                int n4;
                if (constraintWidget.mVerticalChainStyle == 2) {
                    n4 = 1;
                }
                else {
                    n4 = n;
                }
                int n5;
                if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    n5 = 1;
                }
                else {
                    n5 = n;
                }
                if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[n] && constraintWidget.mVerticalChainFixedPosition && n4 == 0 && n5 == 0 && constraintWidget.mVerticalChainStyle == 0) {
                    Optimizer.applyDirectResolutionVerticalChain(this, linearSystem2, countMatchConstraintsChainedWidgets, constraintWidget);
                    n2 = n;
                }
                else if (countMatchConstraintsChainedWidgets != 0 && n4 == 0) {
                    float n6 = 0.0f;
                    ConstraintWidget constraintWidget3 = null;
                    while (constraintWidget2 != null) {
                        if (constraintWidget2.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                            int margin = constraintWidget2.mTop.getMargin();
                            if (constraintWidget3 != null) {
                                margin += constraintWidget3.mBottom.getMargin();
                            }
                            int n7;
                            if (constraintWidget2.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n7 = 2;
                            }
                            else {
                                n7 = 3;
                            }
                            linearSystem2.addGreaterThan(constraintWidget2.mTop.mSolverVariable, constraintWidget2.mTop.mTarget.mSolverVariable, margin, n7);
                            int margin2;
                            final int n8 = margin2 = constraintWidget2.mBottom.getMargin();
                            if (constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget != null) {
                                margin2 = n8;
                                if (constraintWidget2.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == constraintWidget2) {
                                    margin2 = n8 + constraintWidget2.mBottom.mTarget.mOwner.mTop.getMargin();
                                }
                            }
                            int n9;
                            if (constraintWidget2.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                n9 = 2;
                            }
                            else {
                                n9 = 3;
                            }
                            linearSystem2.addLowerThan(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mBottom.mTarget.mSolverVariable, -margin2, n9);
                        }
                        else {
                            n6 += constraintWidget2.mVerticalWeight;
                            int margin3;
                            if (constraintWidget2.mBottom.mTarget != null) {
                                margin3 = constraintWidget2.mBottom.getMargin();
                                if (constraintWidget2 != this.mChainEnds[3]) {
                                    margin3 += constraintWidget2.mBottom.mTarget.mOwner.mTop.getMargin();
                                }
                            }
                            else {
                                margin3 = n;
                            }
                            linearSystem2.addGreaterThan(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mTop.mSolverVariable, n, 1);
                            linearSystem2.addLowerThan(constraintWidget2.mBottom.mSolverVariable, constraintWidget2.mBottom.mTarget.mSolverVariable, -margin3, 1);
                        }
                        final ConstraintWidget mVerticalNextWidget2 = constraintWidget2.mVerticalNextWidget;
                        constraintWidget3 = constraintWidget2;
                        constraintWidget2 = mVerticalNextWidget2;
                    }
                    if (countMatchConstraintsChainedWidgets == 1) {
                        final ConstraintWidget constraintWidget4 = this.mMatchConstraintsChainedWidgets[n];
                        int margin4;
                        final int n10 = margin4 = constraintWidget4.mTop.getMargin();
                        if (constraintWidget4.mTop.mTarget != null) {
                            margin4 = n10 + constraintWidget4.mTop.mTarget.getMargin();
                        }
                        int margin5;
                        final int n11 = margin5 = constraintWidget4.mBottom.getMargin();
                        if (constraintWidget4.mBottom.mTarget != null) {
                            margin5 = n11 + constraintWidget4.mBottom.mTarget.getMargin();
                        }
                        SolverVariable solverVariable = constraintWidget.mBottom.mTarget.mSolverVariable;
                        if (constraintWidget4 == this.mChainEnds[3]) {
                            solverVariable = this.mChainEnds[1].mBottom.mTarget.mSolverVariable;
                        }
                        if (constraintWidget4.mMatchConstraintDefaultHeight == 1) {
                            linearSystem2.addGreaterThan(constraintWidget.mTop.mSolverVariable, constraintWidget.mTop.mTarget.mSolverVariable, margin4, 1);
                            linearSystem2.addLowerThan(constraintWidget.mBottom.mSolverVariable, solverVariable, -margin5, 1);
                            linearSystem2.addEquality(constraintWidget.mBottom.mSolverVariable, constraintWidget.mTop.mSolverVariable, constraintWidget.getHeight(), 2);
                            n2 = n;
                        }
                        else {
                            linearSystem2.addEquality(constraintWidget4.mTop.mSolverVariable, constraintWidget4.mTop.mTarget.mSolverVariable, margin4, 1);
                            linearSystem2.addEquality(constraintWidget4.mBottom.mSolverVariable, solverVariable, -margin5, 1);
                            n2 = n;
                        }
                    }
                    else {
                        int n12 = n;
                        final int n13 = countMatchConstraintsChainedWidgets;
                        while (true) {
                            final int n14 = n13 - 1;
                            n2 = n;
                            if (n12 >= n14) {
                                break;
                            }
                            final ConstraintWidget constraintWidget5 = this.mMatchConstraintsChainedWidgets[n12];
                            final ConstraintWidget[] mMatchConstraintsChainedWidgets = this.mMatchConstraintsChainedWidgets;
                            ++n12;
                            final ConstraintWidget constraintWidget6 = mMatchConstraintsChainedWidgets[n12];
                            final SolverVariable mSolverVariable = constraintWidget5.mTop.mSolverVariable;
                            final SolverVariable mSolverVariable2 = constraintWidget5.mBottom.mSolverVariable;
                            final SolverVariable mSolverVariable3 = constraintWidget6.mTop.mSolverVariable;
                            SolverVariable solverVariable2 = constraintWidget6.mBottom.mSolverVariable;
                            if (constraintWidget6 == this.mChainEnds[3]) {
                                solverVariable2 = this.mChainEnds[1].mBottom.mSolverVariable;
                            }
                            int margin6;
                            final int n15 = margin6 = constraintWidget5.mTop.getMargin();
                            if (constraintWidget5.mTop.mTarget != null) {
                                margin6 = n15;
                                if (constraintWidget5.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                    margin6 = n15;
                                    if (constraintWidget5.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget5) {
                                        margin6 = n15 + constraintWidget5.mTop.mTarget.mOwner.mBottom.getMargin();
                                    }
                                }
                            }
                            linearSystem2.addGreaterThan(mSolverVariable, constraintWidget5.mTop.mTarget.mSolverVariable, margin6, 2);
                            int margin7;
                            final int n16 = margin7 = constraintWidget5.mBottom.getMargin();
                            if (constraintWidget5.mBottom.mTarget != null) {
                                margin7 = n16;
                                if (constraintWidget5.mVerticalNextWidget != null) {
                                    int margin8;
                                    if (constraintWidget5.mVerticalNextWidget.mTop.mTarget != null) {
                                        margin8 = constraintWidget5.mVerticalNextWidget.mTop.getMargin();
                                    }
                                    else {
                                        margin8 = 0;
                                    }
                                    margin7 = n16 + margin8;
                                }
                            }
                            linearSystem2.addLowerThan(mSolverVariable2, constraintWidget5.mBottom.mTarget.mSolverVariable, -margin7, 2);
                            if (n12 == n14) {
                                int margin9;
                                final int n17 = margin9 = constraintWidget6.mTop.getMargin();
                                if (constraintWidget6.mTop.mTarget != null) {
                                    margin9 = n17;
                                    if (constraintWidget6.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                        margin9 = n17;
                                        if (constraintWidget6.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget6) {
                                            margin9 = n17 + constraintWidget6.mTop.mTarget.mOwner.mBottom.getMargin();
                                        }
                                    }
                                }
                                linearSystem2.addGreaterThan(mSolverVariable3, constraintWidget6.mTop.mTarget.mSolverVariable, margin9, 2);
                                ConstraintAnchor constraintAnchor = constraintWidget6.mBottom;
                                if (constraintWidget6 == this.mChainEnds[3]) {
                                    constraintAnchor = this.mChainEnds[1].mBottom;
                                }
                                int margin10;
                                final int n18 = margin10 = constraintAnchor.getMargin();
                                if (constraintAnchor.mTarget != null) {
                                    margin10 = n18;
                                    if (constraintAnchor.mTarget.mOwner.mTop.mTarget != null) {
                                        margin10 = n18;
                                        if (constraintAnchor.mTarget.mOwner.mTop.mTarget.mOwner == constraintWidget6) {
                                            margin10 = n18 + constraintAnchor.mTarget.mOwner.mTop.getMargin();
                                        }
                                    }
                                }
                                linearSystem2.addLowerThan(solverVariable2, constraintAnchor.mTarget.mSolverVariable, -margin10, 2);
                            }
                            if (constraintWidget.mMatchConstraintMaxHeight > 0) {
                                linearSystem2.addLowerThan(mSolverVariable2, mSolverVariable, constraintWidget.mMatchConstraintMaxHeight, 2);
                            }
                            final ArrayRow row = linearSystem.createRow();
                            row.createRowEqualDimension(constraintWidget5.mVerticalWeight, n6, constraintWidget6.mVerticalWeight, mSolverVariable, constraintWidget5.mTop.getMargin(), mSolverVariable2, constraintWidget5.mBottom.getMargin(), mSolverVariable3, constraintWidget6.mTop.getMargin(), solverVariable2, constraintWidget6.mBottom.getMargin());
                            linearSystem2.addConstraint(row);
                            n = 0;
                        }
                    }
                }
                else {
                    ConstraintWidget constraintWidget7 = constraintWidget2;
                    ConstraintWidget constraintWidget9;
                    final ConstraintWidget constraintWidget8 = constraintWidget9 = null;
                    boolean b = false;
                    LinearSystem linearSystem3 = linearSystem2;
                    final int n19 = n3;
                    ConstraintWidget constraintWidget10 = constraintWidget8;
                    while (true) {
                        final ConstraintWidget constraintWidget11 = constraintWidget7;
                        if (constraintWidget11 == null) {
                            break;
                        }
                        final ConstraintWidget mVerticalNextWidget3 = constraintWidget11.mVerticalNextWidget;
                        ConstraintWidget constraintWidget12;
                        if (mVerticalNextWidget3 == null) {
                            constraintWidget12 = this.mChainEnds[1];
                            b = true;
                        }
                        else {
                            constraintWidget12 = constraintWidget10;
                        }
                        ConstraintWidget mOwner = null;
                        Label_2410: {
                            if (n4 != 0) {
                                final ConstraintAnchor mTop = constraintWidget11.mTop;
                                int margin11;
                                final int n20 = margin11 = mTop.getMargin();
                                if (constraintWidget9 != null) {
                                    margin11 = n20 + constraintWidget9.mBottom.getMargin();
                                }
                                int n21;
                                if (constraintWidget2 != constraintWidget11) {
                                    n21 = 3;
                                }
                                else {
                                    n21 = 1;
                                }
                                SolverVariable solverVariable3;
                                SolverVariable solverVariable4;
                                if (mTop.mTarget != null) {
                                    solverVariable3 = mTop.mSolverVariable;
                                    solverVariable4 = mTop.mTarget.mSolverVariable;
                                }
                                else if (constraintWidget11.mBaseline.mTarget != null) {
                                    solverVariable3 = constraintWidget11.mBaseline.mSolverVariable;
                                    solverVariable4 = constraintWidget11.mBaseline.mTarget.mSolverVariable;
                                    margin11 -= mTop.getMargin();
                                }
                                else {
                                    solverVariable3 = (solverVariable4 = null);
                                }
                                if (solverVariable3 != null && solverVariable4 != null) {
                                    linearSystem3.addGreaterThan(solverVariable3, solverVariable4, margin11, n21);
                                }
                                if (constraintWidget11.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    final ConstraintAnchor mBottom = constraintWidget11.mBottom;
                                    if (constraintWidget11.mMatchConstraintDefaultHeight == 1) {
                                        linearSystem3.addEquality(mBottom.mSolverVariable, mTop.mSolverVariable, Math.max(constraintWidget11.mMatchConstraintMinHeight, constraintWidget11.getHeight()), 3);
                                    }
                                    else {
                                        linearSystem3.addGreaterThan(mTop.mSolverVariable, mTop.mTarget.mSolverVariable, mTop.mMargin, 3);
                                        linearSystem3.addLowerThan(mBottom.mSolverVariable, mTop.mSolverVariable, constraintWidget11.mMatchConstraintMinHeight, 3);
                                    }
                                }
                            }
                            else if (n19 == 0 && b && constraintWidget9 != null) {
                                if (constraintWidget11.mBottom.mTarget == null) {
                                    linearSystem3.addEquality(constraintWidget11.mBottom.mSolverVariable, constraintWidget11.getDrawBottom());
                                }
                                else {
                                    linearSystem3.addEquality(constraintWidget11.mBottom.mSolverVariable, constraintWidget12.mBottom.mTarget.mSolverVariable, -constraintWidget11.mBottom.getMargin(), 5);
                                }
                            }
                            else if (n19 == 0 && !b && constraintWidget9 == null) {
                                if (constraintWidget11.mTop.mTarget == null) {
                                    linearSystem3.addEquality(constraintWidget11.mTop.mSolverVariable, constraintWidget11.getDrawY());
                                }
                                else {
                                    linearSystem3.addEquality(constraintWidget11.mTop.mSolverVariable, constraintWidget.mTop.mTarget.mSolverVariable, constraintWidget11.mTop.getMargin(), 5);
                                }
                            }
                            else {
                                final ConstraintAnchor mTop2 = constraintWidget11.mTop;
                                final ConstraintAnchor mBottom2 = constraintWidget11.mBottom;
                                final int margin12 = mTop2.getMargin();
                                final int margin13 = mBottom2.getMargin();
                                linearSystem3.addGreaterThan(mTop2.mSolverVariable, mTop2.mTarget.mSolverVariable, margin12, 1);
                                linearSystem3.addLowerThan(mBottom2.mSolverVariable, mBottom2.mTarget.mSolverVariable, -margin13, 1);
                                SolverVariable solverVariable5;
                                if (mTop2.mTarget != null) {
                                    solverVariable5 = mTop2.mTarget.mSolverVariable;
                                }
                                else {
                                    solverVariable5 = null;
                                }
                                if (constraintWidget9 == null) {
                                    if (constraintWidget.mTop.mTarget != null) {
                                        solverVariable5 = constraintWidget.mTop.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable5 = null;
                                    }
                                }
                                if ((mOwner = mVerticalNextWidget3) == null) {
                                    if (constraintWidget12.mBottom.mTarget != null) {
                                        mOwner = constraintWidget12.mBottom.mTarget.mOwner;
                                    }
                                    else {
                                        mOwner = null;
                                    }
                                }
                                if (mOwner == null) {
                                    break Label_2410;
                                }
                                SolverVariable solverVariable6 = mOwner.mTop.mSolverVariable;
                                if (b) {
                                    if (constraintWidget12.mBottom.mTarget != null) {
                                        solverVariable6 = constraintWidget12.mBottom.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable6 = null;
                                    }
                                }
                                if (solverVariable5 != null && solverVariable6 != null) {
                                    linearSystem3.addCentering(mTop2.mSolverVariable, solverVariable5, margin12, 0.5f, solverVariable6, mBottom2.mSolverVariable, margin13, 4);
                                }
                                break Label_2410;
                            }
                            mOwner = mVerticalNextWidget3;
                        }
                        if (b) {
                            mOwner = null;
                        }
                        final LinearSystem linearSystem4 = linearSystem3;
                        final ConstraintWidget constraintWidget13 = mOwner;
                        constraintWidget10 = constraintWidget12;
                        constraintWidget7 = constraintWidget13;
                        constraintWidget9 = constraintWidget11;
                        linearSystem3 = linearSystem4;
                    }
                    final int n22 = i;
                    final LinearSystem linearSystem5 = linearSystem3;
                    final int n23 = 0;
                    linearSystem6 = linearSystem5;
                    i = n22;
                    n = n23;
                    if (n4 == 0) {
                        continue;
                    }
                    final ConstraintAnchor mTop3 = constraintWidget2.mTop;
                    final ConstraintAnchor mBottom3 = constraintWidget10.mBottom;
                    final int margin14 = mTop3.getMargin();
                    final int margin15 = mBottom3.getMargin();
                    SolverVariable mSolverVariable4;
                    if (constraintWidget.mTop.mTarget != null) {
                        mSolverVariable4 = constraintWidget.mTop.mTarget.mSolverVariable;
                    }
                    else {
                        mSolverVariable4 = null;
                    }
                    SolverVariable mSolverVariable5;
                    if (constraintWidget10.mBottom.mTarget != null) {
                        mSolverVariable5 = constraintWidget10.mBottom.mTarget.mSolverVariable;
                    }
                    else {
                        mSolverVariable5 = null;
                    }
                    linearSystem6 = linearSystem5;
                    i = n22;
                    n = n23;
                    if (mSolverVariable4 == null) {
                        continue;
                    }
                    linearSystem6 = linearSystem5;
                    i = n22;
                    n = n23;
                    if (mSolverVariable5 != null) {
                        linearSystem5.addLowerThan(mBottom3.mSolverVariable, mSolverVariable5, -margin15, 1);
                        linearSystem5.addCentering(mTop3.mSolverVariable, mSolverVariable4, margin14, constraintWidget.mVerticalBiasPercent, mSolverVariable5, mBottom3.mSolverVariable, margin15, 4);
                        n = n23;
                        i = n22;
                        linearSystem6 = linearSystem5;
                    }
                    continue;
                }
            }
            n = n2;
            linearSystem6 = linearSystem2;
        }
    }
    
    private int countMatchConstraintsChainedWidgets(final LinearSystem linearSystem, final ConstraintWidget[] array, final ConstraintWidget constraintWidget, int n, final boolean[] array2) {
        array2[0] = true;
        array2[1] = false;
        array[2] = (array[0] = null);
        array[3] = (array[1] = null);
        int n2 = 0;
        if (n == 0) {
            final boolean b = constraintWidget.mLeft.mTarget == null || constraintWidget.mLeft.mTarget.mOwner == this;
            constraintWidget.mHorizontalNextWidget = null;
            ConstraintWidget constraintWidget2;
            if (constraintWidget.getVisibility() != 8) {
                constraintWidget2 = constraintWidget;
            }
            else {
                constraintWidget2 = null;
            }
            n = 0;
            ConstraintWidget constraintWidget3 = null;
            ConstraintWidget constraintWidget4 = constraintWidget2;
            ConstraintWidget mOwner = constraintWidget;
            ConstraintWidget constraintWidget5;
            ConstraintWidget constraintWidget6;
            while (true) {
                constraintWidget5 = constraintWidget2;
                constraintWidget6 = constraintWidget4;
                n2 = n;
                if (mOwner.mRight.mTarget == null) {
                    break;
                }
                mOwner.mHorizontalNextWidget = null;
                if (mOwner.getVisibility() != 8) {
                    ConstraintWidget constraintWidget7;
                    if ((constraintWidget7 = constraintWidget4) == null) {
                        constraintWidget7 = mOwner;
                    }
                    if (constraintWidget2 != null && constraintWidget2 != mOwner) {
                        constraintWidget2.mHorizontalNextWidget = mOwner;
                    }
                    constraintWidget2 = mOwner;
                    constraintWidget4 = constraintWidget7;
                }
                else {
                    linearSystem.addEquality(mOwner.mLeft.mSolverVariable, mOwner.mLeft.mTarget.mSolverVariable, 0, 5);
                    linearSystem.addEquality(mOwner.mRight.mSolverVariable, mOwner.mLeft.mSolverVariable, 0, 5);
                }
                n2 = n;
                if (mOwner.getVisibility() != 8) {
                    n2 = n;
                    if (mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                            array2[0] = false;
                        }
                        n2 = n;
                        if (mOwner.mDimensionRatio <= 0.0f) {
                            array2[0] = false;
                            n2 = n + 1;
                            if (n2 >= this.mMatchConstraintsChainedWidgets.length) {
                                this.mMatchConstraintsChainedWidgets = Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                            }
                            this.mMatchConstraintsChainedWidgets[n] = mOwner;
                        }
                    }
                }
                if (mOwner.mRight.mTarget.mOwner.mLeft.mTarget == null) {
                    constraintWidget5 = constraintWidget2;
                    constraintWidget6 = constraintWidget4;
                    break;
                }
                if (mOwner.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != mOwner) {
                    constraintWidget5 = constraintWidget2;
                    constraintWidget6 = constraintWidget4;
                    break;
                }
                if (mOwner.mRight.mTarget.mOwner == mOwner) {
                    constraintWidget5 = constraintWidget2;
                    constraintWidget6 = constraintWidget4;
                    break;
                }
                constraintWidget3 = (mOwner = mOwner.mRight.mTarget.mOwner);
                n = n2;
            }
            boolean mHorizontalChainFixedPosition = b;
            if (mOwner.mRight.mTarget != null) {
                mHorizontalChainFixedPosition = b;
                if (mOwner.mRight.mTarget.mOwner != this) {
                    mHorizontalChainFixedPosition = false;
                }
            }
            if (constraintWidget.mLeft.mTarget == null || constraintWidget3.mRight.mTarget == null) {
                array2[1] = true;
            }
            constraintWidget.mHorizontalChainFixedPosition = mHorizontalChainFixedPosition;
            constraintWidget3.mHorizontalNextWidget = null;
            array[0] = constraintWidget;
            array[2] = constraintWidget6;
            array[1] = constraintWidget3;
            array[3] = constraintWidget5;
        }
        else {
            final boolean b2 = constraintWidget.mTop.mTarget == null || constraintWidget.mTop.mTarget.mOwner == this;
            constraintWidget.mVerticalNextWidget = null;
            ConstraintWidget constraintWidget8;
            if (constraintWidget.getVisibility() != 8) {
                constraintWidget8 = constraintWidget;
            }
            else {
                constraintWidget8 = null;
            }
            n = 0;
            ConstraintWidget constraintWidget9 = null;
            ConstraintWidget constraintWidget10 = constraintWidget8;
            ConstraintWidget mOwner2 = constraintWidget;
            ConstraintWidget constraintWidget11 = null;
            ConstraintWidget constraintWidget12 = null;
            Label_0973: {
                while (true) {
                    constraintWidget11 = constraintWidget8;
                    constraintWidget12 = constraintWidget10;
                    n2 = n;
                    if (mOwner2.mBottom.mTarget == null) {
                        break Label_0973;
                    }
                    mOwner2.mVerticalNextWidget = null;
                    if (mOwner2.getVisibility() != 8) {
                        ConstraintWidget constraintWidget13;
                        if ((constraintWidget13 = constraintWidget8) == null) {
                            constraintWidget13 = mOwner2;
                        }
                        if (constraintWidget10 != null && constraintWidget10 != mOwner2) {
                            constraintWidget10.mVerticalNextWidget = mOwner2;
                        }
                        constraintWidget10 = mOwner2;
                        constraintWidget8 = constraintWidget13;
                    }
                    else {
                        linearSystem.addEquality(mOwner2.mTop.mSolverVariable, mOwner2.mTop.mTarget.mSolverVariable, 0, 5);
                        linearSystem.addEquality(mOwner2.mBottom.mSolverVariable, mOwner2.mTop.mSolverVariable, 0, 5);
                    }
                    if (mOwner2.getVisibility() != 8 && mOwner2.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (mOwner2.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                            array2[0] = false;
                        }
                        n2 = n;
                        if (mOwner2.mDimensionRatio <= 0.0f) {
                            array2[0] = false;
                            n2 = n + 1;
                            if (n2 >= this.mMatchConstraintsChainedWidgets.length) {
                                this.mMatchConstraintsChainedWidgets = Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                            }
                            this.mMatchConstraintsChainedWidgets[n] = mOwner2;
                        }
                    }
                    else {
                        n2 = n;
                    }
                    if (mOwner2.mBottom.mTarget.mOwner.mTop.mTarget == null) {
                        break;
                    }
                    if (mOwner2.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != mOwner2) {
                        break;
                    }
                    if (mOwner2.mBottom.mTarget.mOwner == mOwner2) {
                        break;
                    }
                    constraintWidget9 = (mOwner2 = mOwner2.mBottom.mTarget.mOwner);
                    n = n2;
                }
                constraintWidget11 = constraintWidget8;
                constraintWidget12 = constraintWidget10;
            }
            boolean mVerticalChainFixedPosition = b2;
            if (mOwner2.mBottom.mTarget != null) {
                mVerticalChainFixedPosition = b2;
                if (mOwner2.mBottom.mTarget.mOwner != this) {
                    mVerticalChainFixedPosition = false;
                }
            }
            if (constraintWidget.mTop.mTarget == null || constraintWidget9.mBottom.mTarget == null) {
                array2[1] = true;
            }
            constraintWidget.mVerticalChainFixedPosition = mVerticalChainFixedPosition;
            constraintWidget9.mVerticalNextWidget = null;
            array[0] = constraintWidget;
            array[2] = constraintWidget11;
            array[1] = constraintWidget9;
            array[3] = constraintWidget12;
        }
        return n2;
    }
    
    public static ConstraintWidgetContainer createContainer(final ConstraintWidgetContainer constraintWidgetContainer, final String debugName, final ArrayList<ConstraintWidget> list, int i) {
        final Rectangle bounds = WidgetContainer.getBounds(list);
        if (bounds.width != 0 && bounds.height != 0) {
            if (i > 0) {
                final int min = Math.min(bounds.x, bounds.y);
                int n;
                if ((n = i) > min) {
                    n = min;
                }
                bounds.grow(n, n);
            }
            constraintWidgetContainer.setOrigin(bounds.x, bounds.y);
            constraintWidgetContainer.setDimension(bounds.width, bounds.height);
            constraintWidgetContainer.setDebugName(debugName);
            i = 0;
            final ConstraintWidget parent = list.get(0).getParent();
            while (i < list.size()) {
                final ConstraintWidget constraintWidget = list.get(i);
                if (constraintWidget.getParent() == parent) {
                    constraintWidgetContainer.add(constraintWidget);
                    constraintWidget.setX(constraintWidget.getX() - bounds.x);
                    constraintWidget.setY(constraintWidget.getY() - bounds.y);
                }
                ++i;
            }
            return constraintWidgetContainer;
        }
        return null;
    }
    
    private boolean optimize(final LinearSystem linearSystem) {
        final int size = this.mChildren.size();
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.mHorizontalResolution = -1;
            constraintWidget.mVerticalResolution = -1;
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mHorizontalResolution = 1;
                constraintWidget.mVerticalResolution = 1;
            }
        }
        int j = 0;
        int n2;
        int n = n2 = j;
        while (j == 0) {
            int k = 0;
            int n4;
            int n3 = n4 = k;
            while (k < size) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(k);
                if (constraintWidget2.mHorizontalResolution == -1) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mHorizontalResolution = 1;
                    }
                    else {
                        Optimizer.checkHorizontalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mVerticalResolution = 1;
                    }
                    else {
                        Optimizer.checkVerticalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                int n5 = n3;
                if (constraintWidget2.mVerticalResolution == -1) {
                    n5 = n3 + 1;
                }
                int n6 = n4;
                if (constraintWidget2.mHorizontalResolution == -1) {
                    n6 = n4 + 1;
                }
                ++k;
                n3 = n5;
                n4 = n6;
            }
            int n7 = 0;
            Label_0285: {
                if (n3 != 0 || n4 != 0) {
                    n7 = j;
                    if (n2 != n3) {
                        break Label_0285;
                    }
                    n7 = j;
                    if (n != n4) {
                        break Label_0285;
                    }
                }
                n7 = 1;
            }
            j = n7;
            n2 = n3;
            n = n4;
        }
        int l = 0;
        int n9;
        int n8 = n9 = l;
        while (l < size) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(l);
            int n10 = 0;
            Label_0354: {
                if (constraintWidget3.mHorizontalResolution != 1) {
                    n10 = n9;
                    if (constraintWidget3.mHorizontalResolution != -1) {
                        break Label_0354;
                    }
                }
                n10 = n9 + 1;
            }
            int n11 = 0;
            Label_0380: {
                if (constraintWidget3.mVerticalResolution != 1) {
                    n11 = n8;
                    if (constraintWidget3.mVerticalResolution != -1) {
                        break Label_0380;
                    }
                }
                n11 = n8 + 1;
            }
            ++l;
            n9 = n10;
            n8 = n11;
        }
        return n9 == 0 && n8 == 0;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    static int setGroup(final ConstraintAnchor constraintAnchor, int setGroup) {
        final int mGroup = constraintAnchor.mGroup;
        if (constraintAnchor.mOwner.getParent() == null) {
            return setGroup;
        }
        if (mGroup <= setGroup) {
            return mGroup;
        }
        constraintAnchor.mGroup = setGroup;
        final ConstraintAnchor opposite = constraintAnchor.getOpposite();
        final ConstraintAnchor mTarget = constraintAnchor.mTarget;
        int setGroup2 = setGroup;
        if (opposite != null) {
            setGroup2 = setGroup(opposite, setGroup);
        }
        setGroup = setGroup2;
        if (mTarget != null) {
            setGroup = setGroup(mTarget, setGroup2);
        }
        int setGroup3 = setGroup;
        if (opposite != null) {
            setGroup3 = setGroup(opposite, setGroup);
        }
        return constraintAnchor.mGroup = setGroup3;
    }
    
    void addChain(ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            while (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget == constraintWidget.mLeft && constraintWidget.mLeft.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mLeft.mTarget.mOwner;
            }
            this.addHorizontalChain(constraintWidget);
        }
        else if (n == 1) {
            while (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget == constraintWidget.mTop && constraintWidget.mTop.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mTop.mTarget.mOwner;
            }
            this.addVerticalChain(constraintWidget);
        }
    }
    
    public boolean addChildrenToSolver(final LinearSystem linearSystem, final int n) {
        this.addToSolver(linearSystem, n);
        final int size = this.mChildren.size();
        final int mOptimizationLevel = this.mOptimizationLevel;
        int i = 0;
        boolean b;
        if (mOptimizationLevel != 2 && this.mOptimizationLevel != 4) {
            b = true;
        }
        else {
            if (this.optimize(linearSystem)) {
                return false;
            }
            b = false;
        }
        while (i < size) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour mHorizontalDimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
                final DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem, n);
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(mHorizontalDimensionBehaviour);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(mVerticalDimensionBehaviour);
                }
            }
            else {
                if (b) {
                    Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                }
                constraintWidget.addToSolver(linearSystem, n);
            }
            ++i;
        }
        if (this.mHorizontalChainsSize > 0) {
            this.applyHorizontalChain(linearSystem);
        }
        if (this.mVerticalChainsSize > 0) {
            this.applyVerticalChain(linearSystem);
        }
        return true;
    }
    
    public void findHorizontalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final DimensionBehaviour mHorizontalDimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
        final DimensionBehaviour match_CONSTRAINT = DimensionBehaviour.MATCH_CONSTRAINT;
        int relativeBegin = 0;
        if (mHorizontalDimensionBehaviour == match_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        final int optimizerWrapWidth = constraintWidget.getOptimizerWrapWidth();
        if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        final boolean b = true;
        constraintWidget.mHorizontalWrapVisited = true;
        int n5 = 0;
        int n9 = 0;
        Label_0886: {
            int relativeEnd;
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 1) {
                    if (guideline.getRelativeBegin() != -1) {
                        relativeBegin = guideline.getRelativeBegin();
                        relativeEnd = 0;
                    }
                    else if (guideline.getRelativeEnd() != -1) {
                        relativeEnd = guideline.getRelativeEnd();
                    }
                    else {
                        relativeEnd = 0;
                    }
                }
                else {
                    relativeBegin = optimizerWrapWidth;
                    relativeEnd = optimizerWrapWidth;
                }
            }
            else if (!constraintWidget.mRight.isConnected() && !constraintWidget.mLeft.isConnected()) {
                relativeBegin = optimizerWrapWidth + constraintWidget.getX();
                relativeEnd = optimizerWrapWidth;
            }
            else {
                if (constraintWidget.mRight.mTarget != null && constraintWidget.mLeft.mTarget != null && (constraintWidget.mRight.mTarget == constraintWidget.mLeft.mTarget || (constraintWidget.mRight.mTarget.mOwner == constraintWidget.mLeft.mTarget.mOwner && constraintWidget.mRight.mTarget.mOwner != constraintWidget.mParent))) {
                    array[0] = false;
                    return;
                }
                final ConstraintAnchor mTarget = constraintWidget.mRight.mTarget;
                ConstraintWidget constraintWidget2 = null;
                ConstraintWidget constraintWidget3;
                int n2;
                if (mTarget != null) {
                    final ConstraintWidget mOwner = constraintWidget.mRight.mTarget.mOwner;
                    final int n = constraintWidget.mRight.getMargin() + optimizerWrapWidth;
                    constraintWidget3 = mOwner;
                    n2 = n;
                    if (!mOwner.isRoot()) {
                        constraintWidget3 = mOwner;
                        n2 = n;
                        if (!mOwner.mHorizontalWrapVisited) {
                            this.findHorizontalWrapRecursive(mOwner, array);
                            constraintWidget3 = mOwner;
                            n2 = n;
                        }
                    }
                }
                else {
                    n2 = optimizerWrapWidth;
                    constraintWidget3 = null;
                }
                int n3 = optimizerWrapWidth;
                if (constraintWidget.mLeft.mTarget != null) {
                    final ConstraintWidget mOwner2 = constraintWidget.mLeft.mTarget.mOwner;
                    final int n4 = n3 = optimizerWrapWidth + constraintWidget.mLeft.getMargin();
                    constraintWidget2 = mOwner2;
                    if (!mOwner2.isRoot()) {
                        n3 = n4;
                        constraintWidget2 = mOwner2;
                        if (!mOwner2.mHorizontalWrapVisited) {
                            this.findHorizontalWrapRecursive(mOwner2, array);
                            constraintWidget2 = mOwner2;
                            n3 = n4;
                        }
                    }
                }
                n5 = n2;
                Label_0681: {
                    if (constraintWidget.mRight.mTarget != null) {
                        n5 = n2;
                        if (!constraintWidget3.isRoot()) {
                            int n6;
                            if (constraintWidget.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                                n6 = n2 + (constraintWidget3.mDistToRight - constraintWidget3.getOptimizerWrapWidth());
                            }
                            else {
                                n6 = n2;
                                if (constraintWidget.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                                    n6 = n2 + constraintWidget3.mDistToRight;
                                }
                            }
                            constraintWidget.mRightHasCentered = (constraintWidget3.mRightHasCentered || (constraintWidget3.mLeft.mTarget != null && constraintWidget3.mRight.mTarget != null && constraintWidget3.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                            n5 = n6;
                            if (constraintWidget.mRightHasCentered) {
                                if (constraintWidget3.mLeft.mTarget != null) {
                                    n5 = n6;
                                    if (constraintWidget3.mLeft.mTarget.mOwner == constraintWidget) {
                                        break Label_0681;
                                    }
                                }
                                n5 = n6 + (n6 - constraintWidget3.mDistToRight);
                            }
                        }
                    }
                }
                int n7 = n3;
                Label_0882: {
                    if (constraintWidget.mLeft.mTarget != null) {
                        n7 = n3;
                        if (!constraintWidget2.isRoot()) {
                            int n8;
                            if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                                n8 = n3 + (constraintWidget2.mDistToLeft - constraintWidget2.getOptimizerWrapWidth());
                            }
                            else {
                                n8 = n3;
                                if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                                    n8 = n3 + constraintWidget2.mDistToLeft;
                                }
                            }
                            boolean mLeftHasCentered = b;
                            if (!constraintWidget2.mLeftHasCentered) {
                                mLeftHasCentered = (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mRight.mTarget != null && constraintWidget2.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && b);
                            }
                            constraintWidget.mLeftHasCentered = mLeftHasCentered;
                            n7 = n8;
                            if (constraintWidget.mLeftHasCentered) {
                                if (constraintWidget2.mRight.mTarget != null) {
                                    n7 = n8;
                                    if (constraintWidget2.mRight.mTarget.mOwner == constraintWidget) {
                                        break Label_0882;
                                    }
                                }
                                n9 = n8 + (n8 - constraintWidget2.mDistToLeft);
                                break Label_0886;
                            }
                        }
                    }
                }
                n9 = n7;
                break Label_0886;
            }
            final int n10 = relativeEnd;
            n9 = relativeBegin;
            n5 = n10;
        }
        int mDistToLeft = n9;
        int mDistToRight = n5;
        if (constraintWidget.getVisibility() == 8) {
            mDistToLeft = n9 - constraintWidget.mWidth;
            mDistToRight = n5 - constraintWidget.mWidth;
        }
        constraintWidget.mDistToLeft = mDistToLeft;
        constraintWidget.mDistToRight = mDistToRight;
    }
    
    public void findVerticalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
        final DimensionBehaviour match_CONSTRAINT = DimensionBehaviour.MATCH_CONSTRAINT;
        int relativeBegin = 0;
        if (mVerticalDimensionBehaviour == match_CONSTRAINT && constraintWidget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        int n = constraintWidget.getOptimizerWrapHeight();
        final boolean b = true;
        constraintWidget.mVerticalWrapVisited = true;
        int n8 = 0;
        Label_1036: {
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 0) {
                    if (guideline.getRelativeBegin() != -1) {
                        relativeBegin = guideline.getRelativeBegin();
                        n = 0;
                    }
                    else if (guideline.getRelativeEnd() != -1) {
                        n = guideline.getRelativeEnd();
                    }
                    else {
                        n = 0;
                    }
                }
                else {
                    relativeBegin = n;
                }
            }
            else if (constraintWidget.mBaseline.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                relativeBegin = n + constraintWidget.getY();
            }
            else {
                if (constraintWidget.mBottom.mTarget != null && constraintWidget.mTop.mTarget != null && (constraintWidget.mBottom.mTarget == constraintWidget.mTop.mTarget || (constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mTop.mTarget.mOwner && constraintWidget.mBottom.mTarget.mOwner != constraintWidget.mParent))) {
                    array[0] = false;
                    return;
                }
                if (constraintWidget.mBaseline.isConnected()) {
                    final ConstraintWidget owner = constraintWidget.mBaseline.mTarget.getOwner();
                    if (!owner.mVerticalWrapVisited) {
                        this.findVerticalWrapRecursive(owner, array);
                    }
                    final int max = Math.max(owner.mDistToTop - owner.mHeight + n, n);
                    int max2 = Math.max(owner.mDistToBottom - owner.mHeight + n, n);
                    int mDistToTop = max;
                    if (constraintWidget.getVisibility() == 8) {
                        mDistToTop = max - constraintWidget.mHeight;
                        max2 -= constraintWidget.mHeight;
                    }
                    constraintWidget.mDistToTop = mDistToTop;
                    constraintWidget.mDistToBottom = max2;
                    return;
                }
                final boolean connected = constraintWidget.mTop.isConnected();
                ConstraintWidget constraintWidget2 = null;
                ConstraintWidget constraintWidget3;
                int n3;
                if (connected) {
                    final ConstraintWidget owner2 = constraintWidget.mTop.mTarget.getOwner();
                    final int n2 = constraintWidget.mTop.getMargin() + n;
                    constraintWidget3 = owner2;
                    n3 = n2;
                    if (!owner2.isRoot()) {
                        constraintWidget3 = owner2;
                        n3 = n2;
                        if (!owner2.mVerticalWrapVisited) {
                            this.findVerticalWrapRecursive(owner2, array);
                            constraintWidget3 = owner2;
                            n3 = n2;
                        }
                    }
                }
                else {
                    n3 = n;
                    constraintWidget3 = null;
                }
                int n4 = n;
                if (constraintWidget.mBottom.isConnected()) {
                    final ConstraintWidget owner3 = constraintWidget.mBottom.mTarget.getOwner();
                    final int n5 = n4 = n + constraintWidget.mBottom.getMargin();
                    constraintWidget2 = owner3;
                    if (!owner3.isRoot()) {
                        n4 = n5;
                        constraintWidget2 = owner3;
                        if (!owner3.mVerticalWrapVisited) {
                            this.findVerticalWrapRecursive(owner3, array);
                            constraintWidget2 = owner3;
                            n4 = n5;
                        }
                    }
                }
                int n6 = n3;
                Label_0777: {
                    if (constraintWidget.mTop.mTarget != null) {
                        n6 = n3;
                        if (!constraintWidget3.isRoot()) {
                            int n7;
                            if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                                n7 = n3 + (constraintWidget3.mDistToTop - constraintWidget3.getOptimizerWrapHeight());
                            }
                            else {
                                n7 = n3;
                                if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                                    n7 = n3 + constraintWidget3.mDistToTop;
                                }
                            }
                            constraintWidget.mTopHasCentered = (constraintWidget3.mTopHasCentered || (constraintWidget3.mTop.mTarget != null && constraintWidget3.mTop.mTarget.mOwner != constraintWidget && constraintWidget3.mBottom.mTarget != null && constraintWidget3.mBottom.mTarget.mOwner != constraintWidget && constraintWidget3.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                            n6 = n7;
                            if (constraintWidget.mTopHasCentered) {
                                if (constraintWidget3.mBottom.mTarget != null) {
                                    n6 = n7;
                                    if (constraintWidget3.mBottom.mTarget.mOwner == constraintWidget) {
                                        break Label_0777;
                                    }
                                }
                                n6 = n7 + (n7 - constraintWidget3.mDistToTop);
                            }
                        }
                    }
                }
                n = n4;
                n8 = n6;
                if (constraintWidget.mBottom.mTarget == null) {
                    break Label_1036;
                }
                n = n4;
                n8 = n6;
                if (constraintWidget2.isRoot()) {
                    break Label_1036;
                }
                int n9;
                if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                    n9 = n4 + (constraintWidget2.mDistToBottom - constraintWidget2.getOptimizerWrapHeight());
                }
                else {
                    n9 = n4;
                    if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                        n9 = n4 + constraintWidget2.mDistToBottom;
                    }
                }
                boolean mBottomHasCentered = b;
                if (!constraintWidget2.mBottomHasCentered) {
                    mBottomHasCentered = (constraintWidget2.mTop.mTarget != null && constraintWidget2.mTop.mTarget.mOwner != constraintWidget && constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.mOwner != constraintWidget && constraintWidget2.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && b);
                }
                constraintWidget.mBottomHasCentered = mBottomHasCentered;
                n = n9;
                n8 = n6;
                if (constraintWidget.mBottomHasCentered) {
                    if (constraintWidget2.mTop.mTarget != null) {
                        n = n9;
                        n8 = n6;
                        if (constraintWidget2.mTop.mTarget.mOwner == constraintWidget) {
                            break Label_1036;
                        }
                    }
                    n = n9 + (n9 - constraintWidget2.mDistToBottom);
                    n8 = n6;
                }
                break Label_1036;
            }
            n8 = relativeBegin;
        }
        int mDistToBottom = n;
        int mDistToTop2 = n8;
        if (constraintWidget.getVisibility() == 8) {
            mDistToTop2 = n8 - constraintWidget.mHeight;
            mDistToBottom = n - constraintWidget.mHeight;
        }
        constraintWidget.mDistToTop = mDistToTop2;
        constraintWidget.mDistToBottom = mDistToBottom;
    }
    
    public void findWrapSize(final ArrayList<ConstraintWidget> list, final boolean[] array) {
        final int size = list.size();
        array[0] = true;
        final int n = 0;
        final int n3;
        final int n2 = n3 = n;
        final int n5;
        final int n4 = n5 = n3;
        int max2;
        int max = max2 = n5;
        int max3 = n5;
        int max4 = n4;
        int max5 = n3;
        int max6 = n2;
        for (int i = n; i < size; ++i) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (!constraintWidget.isRoot()) {
                if (!constraintWidget.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive(constraintWidget, array);
                }
                if (!constraintWidget.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(constraintWidget, array);
                }
                if (!array[0]) {
                    return;
                }
                final int mDistToLeft = constraintWidget.mDistToLeft;
                final int mDistToRight = constraintWidget.mDistToRight;
                final int width = constraintWidget.getWidth();
                final int mDistToTop = constraintWidget.mDistToTop;
                final int mDistToBottom = constraintWidget.mDistToBottom;
                final int height = constraintWidget.getHeight();
                int b;
                if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    b = constraintWidget.getWidth() + constraintWidget.mLeft.mMargin + constraintWidget.mRight.mMargin;
                }
                else {
                    b = mDistToLeft + mDistToRight - width;
                }
                int b2;
                if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    b2 = constraintWidget.getHeight() + constraintWidget.mTop.mMargin + constraintWidget.mBottom.mMargin;
                }
                else {
                    b2 = mDistToTop + mDistToBottom - height;
                }
                if (constraintWidget.getVisibility() == 8) {
                    b = 0;
                    b2 = 0;
                }
                max6 = Math.max(max6, constraintWidget.mDistToLeft);
                max5 = Math.max(max5, constraintWidget.mDistToRight);
                max2 = Math.max(max2, constraintWidget.mDistToBottom);
                max3 = Math.max(max3, constraintWidget.mDistToTop);
                max4 = Math.max(max4, b);
                max = Math.max(max, b2);
            }
        }
        this.mWrapWidth = Math.max(this.mMinWidth, Math.max(Math.max(max6, max5), max4));
        this.mWrapHeight = Math.max(this.mMinHeight, Math.max(Math.max(max3, max2), max));
        for (int j = 0; j < size; ++j) {
            final ConstraintWidget constraintWidget2 = list.get(j);
            constraintWidget2.mHorizontalWrapVisited = false;
            constraintWidget2.mVerticalWrapVisited = false;
            constraintWidget2.mLeftHasCentered = false;
            constraintWidget2.mRightHasCentered = false;
            constraintWidget2.mTopHasCentered = false;
            constraintWidget2.mBottomHasCentered = false;
        }
    }
    
    public ArrayList<Guideline> getHorizontalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline e = (Guideline)constraintWidget;
                if (e.getOrientation() == 0) {
                    list.add(e);
                }
            }
        }
        return list;
    }
    
    public LinearSystem getSystem() {
        return this.mSystem;
    }
    
    @Override
    public String getType() {
        return "ConstraintLayout";
    }
    
    public ArrayList<Guideline> getVerticalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline e = (Guideline)constraintWidget;
                if (e.getOrientation() == 1) {
                    list.add(e);
                }
            }
        }
        return list;
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }
    
    @Override
    public void layout() {
        final int mx = this.mX;
        final int my = this.mY;
        final int max = Math.max(0, this.getWidth());
        final int max2 = Math.max(0, this.getHeight());
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
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        final DimensionBehaviour mVerticalDimensionBehaviour = this.mVerticalDimensionBehaviour;
        final DimensionBehaviour mHorizontalDimensionBehaviour = this.mHorizontalDimensionBehaviour;
        final int mOptimizationLevel = this.mOptimizationLevel;
        final int n = 1;
        int n4;
        if (mOptimizationLevel == 2 && (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT || this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)) {
            this.findWrapSize(this.mChildren, this.flags);
            int n3;
            final int n2 = n3 = (this.flags[0] ? 1 : 0);
            Label_0225: {
                if (max > 0) {
                    n3 = n2;
                    if (max2 > 0) {
                        if (this.mWrapWidth <= max) {
                            n3 = n2;
                            if (this.mWrapHeight <= max2) {
                                break Label_0225;
                            }
                        }
                        n3 = 0;
                    }
                }
            }
            if ((n4 = n3) != 0) {
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                    if (max > 0 && max < this.mWrapWidth) {
                        this.mWidthMeasuredTooSmall = true;
                        this.setWidth(max);
                    }
                    else {
                        this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
                    }
                }
                n4 = n3;
                if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                    if (max2 > 0 && max2 < this.mWrapHeight) {
                        this.mHeightMeasuredTooSmall = true;
                        this.setHeight(max2);
                        n4 = n3;
                    }
                    else {
                        this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
                        n4 = n3;
                    }
                }
            }
        }
        else {
            n4 = 0;
        }
        this.resetChains();
        final int size = this.mChildren.size();
        Object flags = null;
        for (int i = 0; i < size; ++i) {
            flags = this.mChildren.get(i);
            if (flags instanceof WidgetContainer) {
                ((WidgetContainer)flags).layout();
            }
        }
        int n5 = 0;
        int n6 = n4;
        int j = 1;
        int n7 = n;
        while (j != 0) {
            final int n8 = n5 + n7;
            Label_0513: {
                try {
                    this.mSystem.reset();
                    final boolean b = (j = (this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE) ? 1 : 0)) != 0;
                    if (!b) {
                        break Label_0513;
                    }
                    try {
                        this.mSystem.minimize();
                        j = (b ? 1 : 0);
                    }
                    catch (Exception flags) {
                        j = (b ? 1 : 0);
                    }
                }
                catch (Exception ex) {}
                ((Exception)flags).printStackTrace();
            }
            int n10 = 0;
            Label_0661: {
                if (j != 0) {
                    this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
                }
                else {
                    this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);
                    for (int k = 0; k < size; ++k) {
                        flags = this.mChildren.get(k);
                        if (((ConstraintWidget)flags).mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && ((ConstraintWidget)flags).getWidth() < ((ConstraintWidget)flags).getWrapWidth()) {
                            this.flags[2] = (n7 != 0);
                            break;
                        }
                        if (((ConstraintWidget)flags).mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && ((ConstraintWidget)flags).getHeight() < ((ConstraintWidget)flags).getWrapHeight()) {
                            flags = this.flags;
                            final int n9 = 2;
                            flags[2] = n7;
                            n10 = n9;
                            break Label_0661;
                        }
                    }
                }
                n10 = 2;
            }
            int n12;
            if (n8 < 8 && this.flags[n10]) {
                int l = 0;
                int max3 = 0;
                int max4 = 0;
                while (l < size) {
                    flags = this.mChildren.get(l);
                    max3 = Math.max(max3, ((ConstraintWidget)flags).mX + ((ConstraintWidget)flags).getWidth());
                    max4 = Math.max(max4, ((ConstraintWidget)flags).mY + ((ConstraintWidget)flags).getHeight());
                    ++l;
                }
                final int max5 = Math.max(this.mMinWidth, max3);
                final int max6 = Math.max(this.mMinHeight, max4);
                boolean b2;
                int n11;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this.getWidth() < max5) {
                    this.setWidth(max5);
                    this.mHorizontalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                    b2 = true;
                    n11 = 1;
                }
                else {
                    b2 = false;
                    n11 = n6;
                }
                n12 = (b2 ? 1 : 0);
                n6 = n11;
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    n12 = (b2 ? 1 : 0);
                    n6 = n11;
                    if (this.getHeight() < max6) {
                        this.setHeight(max6);
                        this.mVerticalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                        n12 = 1;
                        n6 = 1;
                    }
                }
            }
            else {
                n12 = 0;
            }
            final int max7 = Math.max(this.mMinWidth, this.getWidth());
            int n13 = n12;
            int n14 = n6;
            if (max7 > this.getWidth()) {
                this.setWidth(max7);
                this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                n13 = 1;
                n14 = 1;
            }
            final int max8 = Math.max(this.mMinHeight, this.getHeight());
            int n15 = n13;
            if (max8 > this.getHeight()) {
                this.setHeight(max8);
                this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                n15 = 1;
                n14 = 1;
            }
            int n16 = n15;
            Label_1132: {
                int n17;
                if ((n17 = n14) == 0) {
                    int n18 = n15;
                    int n19 = n14;
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        n18 = n15;
                        n19 = n14;
                        if (max > 0) {
                            n18 = n15;
                            n19 = n14;
                            if (this.getWidth() > max) {
                                this.mWidthMeasuredTooSmall = true;
                                this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                                this.setWidth(max);
                                n18 = 1;
                                n19 = 1;
                            }
                        }
                    }
                    n16 = n18;
                    n17 = n19;
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        n16 = n18;
                        n17 = n19;
                        if (max2 > 0) {
                            n16 = n18;
                            n17 = n19;
                            if (this.getHeight() > max2) {
                                this.mHeightMeasuredTooSmall = true;
                                this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                                this.setHeight(max2);
                                n16 = (n6 = 1);
                                break Label_1132;
                            }
                        }
                    }
                }
                n6 = n17;
            }
            n7 = 1;
            j = n16;
            n5 = n8;
        }
        if (this.mParent != null) {
            final int max9 = Math.max(this.mMinWidth, this.getWidth());
            final int max10 = Math.max(this.mMinHeight, this.getHeight());
            this.mSnapshot.applyTo(this);
            this.setWidth(max9 + this.mPaddingLeft + this.mPaddingRight);
            this.setHeight(max10 + this.mPaddingTop + this.mPaddingBottom);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (n6 != 0) {
            this.mHorizontalDimensionBehaviour = mHorizontalDimensionBehaviour;
            this.mVerticalDimensionBehaviour = mVerticalDimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    public int layoutFindGroups() {
        final ConstraintAnchor.Type[] array = new ConstraintAnchor.Type[5];
        final ConstraintAnchor.Type left = ConstraintAnchor.Type.LEFT;
        final int n = 0;
        array[0] = left;
        array[1] = ConstraintAnchor.Type.RIGHT;
        array[2] = ConstraintAnchor.Type.TOP;
        array[3] = ConstraintAnchor.Type.BASELINE;
        array[4] = ConstraintAnchor.Type.BOTTOM;
        final int size = this.mChildren.size();
        int i = 0;
        int n2 = 1;
        while (i < size) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            final ConstraintAnchor mLeft = constraintWidget.mLeft;
            int n3;
            if (mLeft.mTarget != null) {
                if (setGroup(mLeft, n2) == (n3 = n2)) {
                    n3 = n2 + 1;
                }
            }
            else {
                mLeft.mGroup = Integer.MAX_VALUE;
                n3 = n2;
            }
            final ConstraintAnchor mTop = constraintWidget.mTop;
            int n4;
            if (mTop.mTarget != null) {
                if (setGroup(mTop, n3) == (n4 = n3)) {
                    n4 = n3 + 1;
                }
            }
            else {
                mTop.mGroup = Integer.MAX_VALUE;
                n4 = n3;
            }
            final ConstraintAnchor mRight = constraintWidget.mRight;
            int n5;
            if (mRight.mTarget != null) {
                if (setGroup(mRight, n4) == (n5 = n4)) {
                    n5 = n4 + 1;
                }
            }
            else {
                mRight.mGroup = Integer.MAX_VALUE;
                n5 = n4;
            }
            final ConstraintAnchor mBottom = constraintWidget.mBottom;
            int n6;
            if (mBottom.mTarget != null) {
                if (setGroup(mBottom, n5) == (n6 = n5)) {
                    n6 = n5 + 1;
                }
            }
            else {
                mBottom.mGroup = Integer.MAX_VALUE;
                n6 = n5;
            }
            final ConstraintAnchor mBaseline = constraintWidget.mBaseline;
            if (mBaseline.mTarget != null) {
                if (setGroup(mBaseline, n6) == (n2 = n6)) {
                    n2 = n6 + 1;
                }
            }
            else {
                mBaseline.mGroup = Integer.MAX_VALUE;
                n2 = n6;
            }
            ++i;
        }
        int j = 1;
        while (j != 0) {
            for (int k = j = 0; k < size; ++k) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(k);
                for (int l = 0; l < array.length; ++l) {
                    final ConstraintAnchor.Type type = array[l];
                    ConstraintAnchor constraintAnchor = null;
                    switch (ConstraintWidgetContainer$2.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
                        case 5: {
                            constraintAnchor = constraintWidget2.mBaseline;
                            break;
                        }
                        case 4: {
                            constraintAnchor = constraintWidget2.mBottom;
                            break;
                        }
                        case 3: {
                            constraintAnchor = constraintWidget2.mRight;
                            break;
                        }
                        case 2: {
                            constraintAnchor = constraintWidget2.mTop;
                            break;
                        }
                        case 1: {
                            constraintAnchor = constraintWidget2.mLeft;
                            break;
                        }
                    }
                    final ConstraintAnchor mTarget = constraintAnchor.mTarget;
                    if (mTarget != null) {
                        int n7 = j;
                        if (mTarget.mOwner.getParent() != null) {
                            n7 = j;
                            if (mTarget.mGroup != constraintAnchor.mGroup) {
                                int n8;
                                if (constraintAnchor.mGroup > mTarget.mGroup) {
                                    n8 = mTarget.mGroup;
                                }
                                else {
                                    n8 = constraintAnchor.mGroup;
                                }
                                constraintAnchor.mGroup = n8;
                                mTarget.mGroup = n8;
                                n7 = 1;
                            }
                        }
                        final ConstraintAnchor opposite = mTarget.getOpposite();
                        j = n7;
                        if (opposite != null) {
                            j = n7;
                            if (opposite.mGroup != constraintAnchor.mGroup) {
                                int n9;
                                if (constraintAnchor.mGroup > opposite.mGroup) {
                                    n9 = opposite.mGroup;
                                }
                                else {
                                    n9 = constraintAnchor.mGroup;
                                }
                                constraintAnchor.mGroup = n9;
                                opposite.mGroup = n9;
                                j = 1;
                            }
                        }
                    }
                }
            }
        }
        final int[] a = new int[this.mChildren.size() * array.length + 1];
        Arrays.fill(a, -1);
        int n10 = 0;
        for (int index = n; index < size; ++index) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(index);
            final ConstraintAnchor mLeft2 = constraintWidget3.mLeft;
            int n11 = n10;
            if (mLeft2.mGroup != Integer.MAX_VALUE) {
                final int mGroup = mLeft2.mGroup;
                n11 = n10;
                if (a[mGroup] == -1) {
                    a[mGroup] = n10;
                    n11 = n10 + 1;
                }
                mLeft2.mGroup = a[mGroup];
            }
            final ConstraintAnchor mTop2 = constraintWidget3.mTop;
            int n12 = n11;
            if (mTop2.mGroup != Integer.MAX_VALUE) {
                final int mGroup2 = mTop2.mGroup;
                n12 = n11;
                if (a[mGroup2] == -1) {
                    a[mGroup2] = n11;
                    n12 = n11 + 1;
                }
                mTop2.mGroup = a[mGroup2];
            }
            final ConstraintAnchor mRight2 = constraintWidget3.mRight;
            int n13 = n12;
            if (mRight2.mGroup != Integer.MAX_VALUE) {
                final int mGroup3 = mRight2.mGroup;
                n13 = n12;
                if (a[mGroup3] == -1) {
                    a[mGroup3] = n12;
                    n13 = n12 + 1;
                }
                mRight2.mGroup = a[mGroup3];
            }
            final ConstraintAnchor mBottom2 = constraintWidget3.mBottom;
            int n14 = n13;
            if (mBottom2.mGroup != Integer.MAX_VALUE) {
                final int mGroup4 = mBottom2.mGroup;
                n14 = n13;
                if (a[mGroup4] == -1) {
                    a[mGroup4] = n13;
                    n14 = n13 + 1;
                }
                mBottom2.mGroup = a[mGroup4];
            }
            final ConstraintAnchor mBaseline2 = constraintWidget3.mBaseline;
            n10 = n14;
            if (mBaseline2.mGroup != Integer.MAX_VALUE) {
                final int mGroup5 = mBaseline2.mGroup;
                n10 = n14;
                if (a[mGroup5] == -1) {
                    a[mGroup5] = n14;
                    n10 = n14 + 1;
                }
                mBaseline2.mGroup = a[mGroup5];
            }
        }
        return n10;
    }
    
    public int layoutFindGroupsSimple() {
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.mLeft.mGroup = 0;
            constraintWidget.mRight.mGroup = 0;
            constraintWidget.mTop.mGroup = 1;
            constraintWidget.mBottom.mGroup = 1;
            constraintWidget.mBaseline.mGroup = 1;
        }
        return 2;
    }
    
    public void layoutWithGroup(int width) {
        final int mx = this.mX;
        final int my = this.mY;
        final ConstraintWidget mParent = this.mParent;
        final int n = 0;
        if (mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.mX = 0;
            this.mY = 0;
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        for (int j = n; j < width; ++j) {
            try {
                this.addToSolver(this.mSystem, j);
                this.mSystem.minimize();
                this.updateFromSolver(this.mSystem, j);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            this.updateFromSolver(this.mSystem, -2);
        }
        if (this.mParent != null) {
            width = this.getWidth();
            final int height = this.getHeight();
            this.mSnapshot.applyTo(this);
            this.setWidth(width);
            this.setHeight(height);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        super.reset();
    }
    
    public void setOptimizationLevel(final int mOptimizationLevel) {
        this.mOptimizationLevel = mOptimizationLevel;
    }
    
    public void setPadding(final int mPaddingLeft, final int mPaddingTop, final int mPaddingRight, final int mPaddingBottom) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingTop = mPaddingTop;
        this.mPaddingRight = mPaddingRight;
        this.mPaddingBottom = mPaddingBottom;
    }
    
    public void updateChildrenFromSolver(final LinearSystem linearSystem, final int n, final boolean[] array) {
        int i = 0;
        array[2] = false;
        this.updateFromSolver(linearSystem, n);
        while (i < this.mChildren.size()) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem, n);
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                array[2] = true;
            }
            if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                array[2] = true;
            }
            ++i;
        }
    }
}
