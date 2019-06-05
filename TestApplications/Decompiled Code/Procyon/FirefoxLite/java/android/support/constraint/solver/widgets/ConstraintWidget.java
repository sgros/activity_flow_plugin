// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintWidget
{
    public static float DEFAULT_BIAS = 0.5f;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintAnchor mBottom;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft;
    protected ConstraintAnchor[] mListAnchors;
    protected DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    protected ConstraintWidget[] mListNextVisibleWidget;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    float mMatchConstraintPercentHeight;
    float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    protected int mMinHeight;
    protected int mMinWidth;
    protected int mOffsetX;
    protected int mOffsetY;
    ConstraintWidget mParent;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    int[] mResolvedMatchConstraintDefault;
    ConstraintAnchor mRight;
    ConstraintAnchor mTop;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    float[] mWeight;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;
    
    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
        this.mCircleConstraintAngle = 0.0f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
        this.mAnchors = new ArrayList<ConstraintAnchor>();
        this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mVerticalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[] { -1.0f, -1.0f };
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
        this.mListNextVisibleWidget = new ConstraintWidget[] { null, null };
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }
    
    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }
    
    private void applyConstraints(final LinearSystem linearSystem, final boolean b, final SolverVariable solverVariable, final SolverVariable solverVariable2, final DimensionBehaviour dimensionBehaviour, final boolean b2, final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, int n, int n2, int n3, int n4, final float n5, final boolean b3, final boolean b4, int b5, int n6, int n7, final float n8, final boolean b6) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintAnchor);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(constraintAnchor2);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        if (linearSystem.graphOptimizer && constraintAnchor.getResolutionNode().state == 1 && constraintAnchor2.getResolutionNode().state == 1) {
            if (LinearSystem.getMetrics() != null) {
                final Metrics metrics = LinearSystem.getMetrics();
                ++metrics.resolvedWidgets;
            }
            constraintAnchor.getResolutionNode().addResolvedValue(linearSystem);
            constraintAnchor2.getResolutionNode().addResolvedValue(linearSystem);
            if (!b4 && b) {
                linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
            }
            return;
        }
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics2 = LinearSystem.getMetrics();
            ++metrics2.nonresolvedWidgets;
        }
        final boolean connected = constraintAnchor.isConnected();
        final boolean connected2 = constraintAnchor2.isConnected();
        final boolean connected3 = this.mCenter.isConnected();
        int n9;
        if (connected) {
            n9 = 1;
        }
        else {
            n9 = 0;
        }
        int n10 = n9;
        if (connected2) {
            n10 = n9 + 1;
        }
        int n11 = n10;
        if (connected3) {
            n11 = n10 + 1;
        }
        int n12;
        if (b3) {
            n12 = 3;
        }
        else {
            n12 = b5;
        }
        Label_0294: {
            switch (ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour[dimensionBehaviour.ordinal()]) {
                case 4: {
                    if (n12 == 4) {
                        break;
                    }
                    b5 = 1;
                    break Label_0294;
                }
            }
            b5 = 0;
        }
        if (this.mVisibility == 8) {
            n2 = 0;
            b5 = 0;
        }
        if (b6) {
            if (!connected && !connected2 && !connected3) {
                linearSystem.addEquality(objectVariable, n);
            }
            else if (connected && !connected2) {
                linearSystem.addEquality(objectVariable, objectVariable3, constraintAnchor.getMargin(), 6);
            }
        }
        if (b5 == 0) {
            if (b2) {
                linearSystem.addEquality(objectVariable2, objectVariable, 0, 3);
                if (n3 > 0) {
                    linearSystem.addGreaterThan(objectVariable2, objectVariable, n3, 6);
                }
                if (n4 < Integer.MAX_VALUE) {
                    linearSystem.addLowerThan(objectVariable2, objectVariable, n4, 6);
                }
            }
            else {
                linearSystem.addEquality(objectVariable2, objectVariable, n2, 6);
            }
            n = n6;
            n6 = n7;
            n7 = n;
        }
        else {
            if (n6 == -2) {
                n = n2;
            }
            else {
                n = n6;
            }
            n4 = n7;
            if (n4 == -2) {
                n4 = n2;
            }
            if (n > 0) {
                if (b) {
                    linearSystem.addGreaterThan(objectVariable2, objectVariable, n, 6);
                }
                else {
                    linearSystem.addGreaterThan(objectVariable2, objectVariable, n, 6);
                }
                n2 = Math.max(n2, n);
            }
            int min = n2;
            if (n4 > 0) {
                if (b) {
                    linearSystem.addLowerThan(objectVariable2, objectVariable, n4, 1);
                }
                else {
                    linearSystem.addLowerThan(objectVariable2, objectVariable, n4, 6);
                }
                min = Math.min(n2, n4);
            }
            Label_0785: {
                if (n12 == 1) {
                    if (b) {
                        linearSystem.addEquality(objectVariable2, objectVariable, min, 6);
                    }
                    else if (b4) {
                        linearSystem.addEquality(objectVariable2, objectVariable, min, 4);
                    }
                    else {
                        linearSystem.addEquality(objectVariable2, objectVariable, min, 1);
                    }
                }
                else if (n12 == 2) {
                    SolverVariable solverVariable3;
                    SolverVariable solverVariable4;
                    if (constraintAnchor.getType() != ConstraintAnchor.Type.TOP && constraintAnchor.getType() != ConstraintAnchor.Type.BOTTOM) {
                        solverVariable3 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                        solverVariable4 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                    }
                    else {
                        solverVariable3 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.TOP));
                        solverVariable4 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                    }
                    linearSystem.addConstraint(linearSystem.createRow().createRowDimensionRatio(objectVariable2, objectVariable, solverVariable4, solverVariable3, n8));
                    n2 = 0;
                    break Label_0785;
                }
                n2 = b5;
            }
            n6 = n4;
            n7 = n;
            b5 = n2;
            if (n2 != 0) {
                n6 = n4;
                n7 = n;
                b5 = n2;
                if (n11 != 2) {
                    n6 = n4;
                    n7 = n;
                    b5 = n2;
                    if (!b3) {
                        b5 = (n2 = Math.max(n, min));
                        if (n4 > 0) {
                            n2 = Math.min(n4, b5);
                        }
                        linearSystem.addEquality(objectVariable2, objectVariable, n2, 6);
                        b5 = 0;
                        n7 = n;
                        n6 = n4;
                    }
                }
            }
        }
        final SolverVariable solverVariable5 = objectVariable3;
        if (b6 && !b4) {
            n4 = 5;
            if (!connected && !connected2 && !connected3) {
                if (b) {
                    linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 5);
                }
            }
            else {
                n = 4;
                if (connected && !connected2) {
                    if (b) {
                        linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 5);
                    }
                }
                else if (!connected && connected2) {
                    linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), 6);
                    if (b) {
                        linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 5);
                    }
                }
                else if (connected && connected2) {
                    Label_1323: {
                        Label_1320: {
                            if (b5 != 0) {
                                if (b && n3 == 0) {
                                    linearSystem.addGreaterThan(objectVariable2, objectVariable, 0, 6);
                                }
                                if (n12 == 0) {
                                    if (n6 <= 0 && n7 <= 0) {
                                        n = 0;
                                        n2 = 6;
                                    }
                                    else {
                                        n3 = 1;
                                        n2 = n;
                                        n = n3;
                                    }
                                    linearSystem.addEquality(objectVariable, solverVariable5, constraintAnchor.getMargin(), n2);
                                    linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), n2);
                                    if (n6 <= 0 && n7 <= 0) {
                                        n2 = 0;
                                    }
                                    else {
                                        n2 = 1;
                                    }
                                    n3 = n;
                                    n = n2;
                                    n2 = n3;
                                    break Label_1323;
                                }
                                n3 = 1;
                                if (n12 == 1) {
                                    n = 6;
                                }
                                else {
                                    if (n12 != 3) {
                                        n = 0;
                                        break Label_1320;
                                    }
                                    if (!b3) {
                                        n2 = n;
                                        if (this.mResolvedDimensionRatioSide != -1) {
                                            n2 = n;
                                            if (n6 <= 0) {
                                                n2 = 6;
                                            }
                                        }
                                    }
                                    else {
                                        n2 = n;
                                    }
                                    linearSystem.addEquality(objectVariable, solverVariable5, constraintAnchor.getMargin(), n2);
                                    linearSystem.addEquality(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), n2);
                                    n = n4;
                                }
                                n2 = 1;
                                n4 = n;
                                n = n3;
                                break Label_1323;
                            }
                            else {
                                n2 = (n = 1);
                                if (b) {
                                    linearSystem.addGreaterThan(objectVariable, solverVariable5, constraintAnchor.getMargin(), 5);
                                    linearSystem.addLowerThan(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), 5);
                                    n = n2;
                                }
                            }
                        }
                        n2 = 0;
                    }
                    if (n != 0) {
                        linearSystem.addCentering(objectVariable, solverVariable5, constraintAnchor.getMargin(), n5, objectVariable4, objectVariable2, constraintAnchor2.getMargin(), n4);
                    }
                    if (n2 != 0) {
                        linearSystem.addGreaterThan(objectVariable, solverVariable5, constraintAnchor.getMargin(), 6);
                        linearSystem.addLowerThan(objectVariable2, objectVariable4, -constraintAnchor2.getMargin(), 6);
                    }
                    if (b) {
                        linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 6);
                    }
                }
            }
            if (b) {
                linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
            }
            return;
        }
        if (n11 < 2 && b) {
            linearSystem.addGreaterThan(objectVariable, solverVariable, 0, 6);
            linearSystem.addGreaterThan(solverVariable2, objectVariable2, 0, 6);
        }
    }
    
    public void addToSolver(final LinearSystem linearSystem) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mLeft);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mRight);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(this.mTop);
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
        boolean b8;
        boolean b9;
        boolean b10;
        boolean b11;
        if (this.mParent != null) {
            final boolean b = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
            final boolean b2 = this.mParent != null && this.mParent.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
            if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget != this.mLeft && this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
            }
            final boolean b3 = (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
            if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget != this.mTop && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
            }
            final boolean b4 = (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
            if (b && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mRight), objectVariable2, 0, 1);
            }
            if (b2 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(this.mParent.mBottom), objectVariable4, 0, 1);
            }
            final boolean b5 = b2;
            final boolean b6 = b3;
            final boolean b7 = b4;
            b8 = b;
            b9 = b5;
            b10 = b6;
            b11 = b7;
        }
        else {
            b8 = false;
            b9 = false;
            b10 = false;
            b11 = false;
        }
        int n;
        if ((n = this.mWidth) < this.mMinWidth) {
            n = this.mMinWidth;
        }
        int n2;
        if ((n2 = this.mHeight) < this.mMinHeight) {
            n2 = this.mMinHeight;
        }
        final boolean b12 = this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean b13 = this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT;
        this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
        this.mResolvedDimensionRatio = this.mDimensionRatio;
        final int mMatchConstraintDefaultWidth = this.mMatchConstraintDefaultWidth;
        final int mMatchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
        int n3 = 0;
        int n8 = 0;
        boolean b14 = false;
        int n15 = 0;
        int n16 = 0;
        Label_1012: {
            int n6 = 0;
            int n9 = 0;
            int n10 = 0;
            Label_0997: {
                if (this.mDimensionRatio > 0.0f && this.mVisibility != 8) {
                    n3 = mMatchConstraintDefaultWidth;
                    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && (n3 = mMatchConstraintDefaultWidth) == 0) {
                        n3 = 3;
                    }
                    int n4 = mMatchConstraintDefaultHeight;
                    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && (n4 = mMatchConstraintDefaultHeight) == 0) {
                        n4 = 3;
                    }
                    int n13 = 0;
                    int n14 = 0;
                    Label_0959: {
                        int n5 = 0;
                        Label_0951: {
                            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && n3 == 3 && n4 == 3) {
                                this.setupDimensionRatio(b8, b9, b12, b13);
                            }
                            else if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && n3 == 3) {
                                this.mResolvedDimensionRatioSide = 0;
                                n5 = (int)(this.mResolvedDimensionRatio * this.mHeight);
                                if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n6 = n5;
                                    final int n7 = n4;
                                    n8 = n2;
                                    n9 = 4;
                                    n10 = n7;
                                    break Label_0997;
                                }
                                break Label_0951;
                            }
                            else if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && n4 == 3) {
                                this.mResolvedDimensionRatioSide = 1;
                                if (this.mDimensionRatioSide == -1) {
                                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                                }
                                final int n11 = (int)(this.mResolvedDimensionRatio * this.mWidth);
                                if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n8 = n11;
                                    final int n12 = n;
                                    n9 = n3;
                                    n10 = 4;
                                    n6 = n12;
                                    break Label_0997;
                                }
                                n13 = n;
                                n14 = n11;
                                break Label_0959;
                            }
                            n5 = n;
                        }
                        n14 = n2;
                        n13 = n5;
                    }
                    b14 = true;
                    n15 = n4;
                    n16 = n13;
                    n8 = n14;
                    break Label_1012;
                }
                final int n17 = mMatchConstraintDefaultWidth;
                n8 = n2;
                n6 = n;
                n10 = mMatchConstraintDefaultHeight;
                n9 = n17;
            }
            b14 = false;
            n16 = n6;
            n15 = n10;
            n3 = n9;
        }
        this.mResolvedMatchConstraintDefault[0] = n3;
        this.mResolvedMatchConstraintDefault[1] = n15;
        final boolean b15 = b14 && (this.mResolvedDimensionRatioSide == 0 || this.mResolvedDimensionRatioSide == -1);
        final boolean b16 = this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        final boolean b17 = this.mCenter.isConnected() ^ true;
        if (this.mHorizontalResolution != 2) {
            SolverVariable objectVariable6;
            if (this.mParent != null) {
                objectVariable6 = linearSystem.createObjectVariable(this.mParent.mRight);
            }
            else {
                objectVariable6 = null;
            }
            SolverVariable objectVariable7;
            if (this.mParent != null) {
                objectVariable7 = linearSystem.createObjectVariable(this.mParent.mLeft);
            }
            else {
                objectVariable7 = null;
            }
            this.applyConstraints(linearSystem, b8, objectVariable7, objectVariable6, this.mListDimensionBehaviors[0], b16, this.mLeft, this.mRight, this.mX, n16, this.mMinWidth, this.mMaxDimension[0], this.mHorizontalBiasPercent, b15, b10, n3, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, b17);
        }
        final SolverVariable solverVariable = objectVariable3;
        final SolverVariable solverVariable2 = objectVariable2;
        if (this.mVerticalResolution == 2) {
            return;
        }
        final boolean b18 = this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        final boolean b19 = b14 && (this.mResolvedDimensionRatioSide == 1 || this.mResolvedDimensionRatioSide == -1);
        boolean b20 = false;
        Label_1396: {
            if (this.mBaselineDistance > 0) {
                if (this.mBaseline.getResolutionNode().state == 1) {
                    this.mBaseline.getResolutionNode().addResolvedValue(linearSystem);
                }
                else {
                    linearSystem.addEquality(objectVariable5, solverVariable, this.getBaselineDistance(), 6);
                    if (this.mBaseline.mTarget != null) {
                        linearSystem.addEquality(objectVariable5, linearSystem.createObjectVariable(this.mBaseline.mTarget), 0, 6);
                        b20 = false;
                        break Label_1396;
                    }
                }
            }
            b20 = b17;
        }
        final SolverVariable solverVariable3 = solverVariable;
        SolverVariable objectVariable8;
        if (this.mParent != null) {
            objectVariable8 = linearSystem.createObjectVariable(this.mParent.mBottom);
        }
        else {
            objectVariable8 = null;
        }
        SolverVariable objectVariable9;
        if (this.mParent != null) {
            objectVariable9 = linearSystem.createObjectVariable(this.mParent.mTop);
        }
        else {
            objectVariable9 = null;
        }
        this.applyConstraints(linearSystem, b9, objectVariable9, objectVariable8, this.mListDimensionBehaviors[1], b18, this.mTop, this.mBottom, this.mY, n8, this.mMinHeight, this.mMaxDimension[1], this.mVerticalBiasPercent, b19, b11, n15, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, b20);
        if (b14) {
            if (this.mResolvedDimensionRatioSide == 1) {
                linearSystem.addRatio(objectVariable4, solverVariable3, solverVariable2, objectVariable, this.mResolvedDimensionRatio, 6);
            }
            else {
                linearSystem.addRatio(solverVariable2, objectVariable, objectVariable4, solverVariable3, this.mResolvedDimensionRatio, 6);
            }
        }
        if (this.mCenter.isConnected()) {
            linearSystem.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
        }
    }
    
    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }
    
    public void analyze(final int n) {
        Optimizer.analyze(n, this);
    }
    
    public void connectCircularConstraint(final ConstraintWidget constraintWidget, final float mCircleConstraintAngle, final int n) {
        this.immediateConnect(ConstraintAnchor.Type.CENTER, constraintWidget, ConstraintAnchor.Type.CENTER, n, 0);
        this.mCircleConstraintAngle = mCircleConstraintAngle;
    }
    
    public void createObjectVariables(final LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }
    
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                throw new AssertionError((Object)type.name());
            }
            case 9: {
                return null;
            }
            case 8: {
                return this.mCenterY;
            }
            case 7: {
                return this.mCenterX;
            }
            case 6: {
                return this.mCenter;
            }
            case 5: {
                return this.mBaseline;
            }
            case 4: {
                return this.mBottom;
            }
            case 3: {
                return this.mRight;
            }
            case 2: {
                return this.mTop;
            }
            case 1: {
                return this.mLeft;
            }
        }
    }
    
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }
    
    public int getBottom() {
        return this.getY() + this.mHeight;
    }
    
    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }
    
    public String getDebugName() {
        return this.mDebugName;
    }
    
    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }
    
    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }
    
    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }
    
    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }
    
    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }
    
    public ConstraintWidget getParent() {
        return this.mParent;
    }
    
    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }
    
    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }
    
    public int getRight() {
        return this.getX() + this.mWidth;
    }
    
    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }
    
    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }
    
    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }
    
    public int getVisibility() {
        return this.mVisibility;
    }
    
    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }
    
    public int getWrapHeight() {
        return this.mWrapHeight;
    }
    
    public int getWrapWidth() {
        return this.mWrapWidth;
    }
    
    public int getX() {
        return this.mX;
    }
    
    public int getY() {
        return this.mY;
    }
    
    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }
    
    public void immediateConnect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }
    
    public boolean isSpreadHeight() {
        final int mMatchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
        boolean b = true;
        if (mMatchConstraintDefaultHeight != 0 || this.mDimensionRatio != 0.0f || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            b = false;
        }
        return b;
    }
    
    public boolean isSpreadWidth() {
        final int mMatchConstraintDefaultWidth = this.mMatchConstraintDefaultWidth;
        boolean b2;
        final boolean b = b2 = false;
        if (mMatchConstraintDefaultWidth == 0) {
            b2 = b;
            if (this.mDimensionRatio == 0.0f) {
                b2 = b;
                if (this.mMatchConstraintMinWidth == 0) {
                    b2 = b;
                    if (this.mMatchConstraintMaxWidth == 0) {
                        b2 = b;
                        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        this.mHorizontalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mVerticalBiasPercent = ConstraintWidget.DEFAULT_BIAS;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mWeight[0] = -1.0f;
        this.mWeight[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMaxDimension[0] = Integer.MAX_VALUE;
        this.mMaxDimension[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        if (this.mResolutionWidth != null) {
            this.mResolutionWidth.reset();
        }
        if (this.mResolutionHeight != null) {
            this.mResolutionHeight.reset();
        }
    }
    
    public void resetAnchors() {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            this.mAnchors.get(i).reset();
        }
    }
    
    public void resetResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }
    
    public void resetSolverVariables(final Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }
    
    public void resolve() {
    }
    
    public void setBaselineDistance(final int mBaselineDistance) {
        this.mBaselineDistance = mBaselineDistance;
    }
    
    public void setCompanionWidget(final Object mCompanionWidget) {
        this.mCompanionWidget = mCompanionWidget;
    }
    
    public void setDebugName(final String mDebugName) {
        this.mDebugName = mDebugName;
    }
    
    public void setDimensionRatio(String s) {
        Label_0263: {
            if (s == null || s.length() == 0) {
                break Label_0263;
            }
            final int n = -1;
            final int length = s.length();
            final int index = s.indexOf(44);
            final int n2 = 0;
            int mDimensionRatioSide = n;
            int n3 = n2;
            if (index > 0) {
                mDimensionRatioSide = n;
                n3 = n2;
                if (index < length - 1) {
                    final String substring = s.substring(0, index);
                    if (substring.equalsIgnoreCase("W")) {
                        mDimensionRatioSide = 0;
                    }
                    else {
                        mDimensionRatioSide = n;
                        if (substring.equalsIgnoreCase("H")) {
                            mDimensionRatioSide = 1;
                        }
                    }
                    n3 = index + 1;
                }
            }
            final int index2 = s.indexOf(58);
            Label_0217: {
                if (index2 < 0 || index2 >= length - 1) {
                    break Label_0217;
                }
                final String substring2 = s.substring(n3, index2);
                s = s.substring(index2 + 1);
            Block_15_Outer:
                while (true) {
                    if (substring2.length() <= 0 || s.length() <= 0) {
                        break Label_0240;
                    }
                    try {
                        final float float1 = Float.parseFloat(substring2);
                        final float float2 = Float.parseFloat(s);
                        while (true) {
                            while (true) {
                                if (float1 > 0.0f && float2 > 0.0f) {
                                    if (mDimensionRatioSide == 1) {
                                        final float mDimensionRatio = Math.abs(float2 / float1);
                                        break Label_0243;
                                    }
                                    final float mDimensionRatio = Math.abs(float1 / float2);
                                    break Label_0243;
                                }
                                float mDimensionRatio = 0.0f;
                                if (mDimensionRatio > 0.0f) {
                                    this.mDimensionRatio = mDimensionRatio;
                                    this.mDimensionRatioSide = mDimensionRatioSide;
                                }
                                return;
                                mDimensionRatio = Float.parseFloat(s);
                                continue Block_15_Outer;
                            }
                            this.mDimensionRatio = 0.0f;
                            return;
                            s = s.substring(n3);
                            continue;
                        }
                    }
                    // iftrue(Label_0240:, s.length() <= 0)
                    catch (NumberFormatException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public void setFrame(int mWidth, int mHeight, int n, final int n2) {
        final int n3 = n - mWidth;
        n = n2 - mHeight;
        this.mX = mWidth;
        this.mY = mHeight;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        mWidth = n3;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && (mWidth = n3) < this.mWidth) {
            mWidth = this.mWidth;
        }
        mHeight = n;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && (mHeight = n) < this.mHeight) {
            mHeight = this.mHeight;
        }
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setHeight(final int mHeight) {
        this.mHeight = mHeight;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    public void setHeightWrapContent(final boolean mIsHeightWrapContent) {
        this.mIsHeightWrapContent = mIsHeightWrapContent;
    }
    
    public void setHorizontalBiasPercent(final float mHorizontalBiasPercent) {
        this.mHorizontalBiasPercent = mHorizontalBiasPercent;
    }
    
    public void setHorizontalChainStyle(final int mHorizontalChainStyle) {
        this.mHorizontalChainStyle = mHorizontalChainStyle;
    }
    
    public void setHorizontalDimension(final int mx, final int n) {
        this.mX = mx;
        this.mWidth = n - mx;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setHorizontalDimensionBehaviour(final DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
        }
    }
    
    public void setHorizontalMatchStyle(final int mMatchConstraintDefaultWidth, final int mMatchConstraintMinWidth, final int mMatchConstraintMaxWidth, final float mMatchConstraintPercentWidth) {
        this.mMatchConstraintDefaultWidth = mMatchConstraintDefaultWidth;
        this.mMatchConstraintMinWidth = mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = mMatchConstraintMaxWidth;
        this.mMatchConstraintPercentWidth = mMatchConstraintPercentWidth;
        if (mMatchConstraintPercentWidth < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }
    
    public void setHorizontalWeight(final float n) {
        this.mWeight[0] = n;
    }
    
    public void setMaxHeight(final int n) {
        this.mMaxDimension[1] = n;
    }
    
    public void setMaxWidth(final int n) {
        this.mMaxDimension[0] = n;
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight < 0) {
            this.mMinHeight = 0;
        }
        else {
            this.mMinHeight = mMinHeight;
        }
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth < 0) {
            this.mMinWidth = 0;
        }
        else {
            this.mMinWidth = mMinWidth;
        }
    }
    
    public void setOffset(final int mOffsetX, final int mOffsetY) {
        this.mOffsetX = mOffsetX;
        this.mOffsetY = mOffsetY;
    }
    
    public void setOrigin(final int mx, final int my) {
        this.mX = mx;
        this.mY = my;
    }
    
    public void setParent(final ConstraintWidget mParent) {
        this.mParent = mParent;
    }
    
    public void setVerticalBiasPercent(final float mVerticalBiasPercent) {
        this.mVerticalBiasPercent = mVerticalBiasPercent;
    }
    
    public void setVerticalChainStyle(final int mVerticalChainStyle) {
        this.mVerticalChainStyle = mVerticalChainStyle;
    }
    
    public void setVerticalDimension(final int my, final int n) {
        this.mY = my;
        this.mHeight = n - my;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    public void setVerticalDimensionBehaviour(final DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
        }
    }
    
    public void setVerticalMatchStyle(final int mMatchConstraintDefaultHeight, final int mMatchConstraintMinHeight, final int mMatchConstraintMaxHeight, final float mMatchConstraintPercentHeight) {
        this.mMatchConstraintDefaultHeight = mMatchConstraintDefaultHeight;
        this.mMatchConstraintMinHeight = mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = mMatchConstraintPercentHeight;
        if (mMatchConstraintPercentHeight < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }
    
    public void setVerticalWeight(final float n) {
        this.mWeight[1] = n;
    }
    
    public void setVisibility(final int mVisibility) {
        this.mVisibility = mVisibility;
    }
    
    public void setWidth(final int mWidth) {
        this.mWidth = mWidth;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }
    
    public void setWidthWrapContent(final boolean mIsWidthWrapContent) {
        this.mIsWidthWrapContent = mIsWidthWrapContent;
    }
    
    public void setWrapHeight(final int mWrapHeight) {
        this.mWrapHeight = mWrapHeight;
    }
    
    public void setWrapWidth(final int mWrapWidth) {
        this.mWrapWidth = mWrapWidth;
    }
    
    public void setX(final int mx) {
        this.mX = mx;
    }
    
    public void setY(final int my) {
        this.mY = my;
    }
    
    public void setupDimensionRatio(final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (b3 && !b4) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!b3 && b4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        }
        else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (b && !b2) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (!b && b2) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            }
            else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && b && b2) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String string;
        if (this.mType != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("type: ");
            sb2.append(this.mType);
            sb2.append(" ");
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        String string2;
        if (this.mDebugName != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("id: ");
            sb3.append(this.mDebugName);
            sb3.append(" ");
            string2 = sb3.toString();
        }
        else {
            string2 = "";
        }
        sb.append(string2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(") wrap: (");
        sb.append(this.mWrapWidth);
        sb.append(" x ");
        sb.append(this.mWrapHeight);
        sb.append(")");
        return sb.toString();
    }
    
    public void updateDrawPosition() {
        final int mx = this.mX;
        final int my = this.mY;
        final int mx2 = this.mX;
        final int mWidth = this.mWidth;
        final int my2 = this.mY;
        final int mHeight = this.mHeight;
        this.mDrawX = mx;
        this.mDrawY = my;
        this.mDrawWidth = mx2 + mWidth - mx;
        this.mDrawHeight = my2 + mHeight - my;
    }
    
    public void updateFromSolver(final LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        final int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        int n;
        if (objectVariableValue3 - objectVariableValue < 0 || objectVariableValue4 - objectVariableValue2 < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || (n = objectVariableValue4) == Integer.MAX_VALUE) {
            n = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        this.setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, n);
    }
    
    public void updateResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }
    
    public enum DimensionBehaviour
    {
        FIXED, 
        MATCH_CONSTRAINT, 
        MATCH_PARENT, 
        WRAP_CONTENT;
    }
}
