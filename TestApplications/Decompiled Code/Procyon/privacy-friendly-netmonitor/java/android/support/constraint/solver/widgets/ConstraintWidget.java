// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class ConstraintWidget
{
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    DimensionBehaviour mHorizontalDimensionBehaviour;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    float mHorizontalWeight;
    boolean mHorizontalWrapVisited;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    protected int mMinHeight;
    protected int mMinWidth;
    protected int mOffsetX;
    protected int mOffsetY;
    ConstraintWidget mParent;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    private int mSolverBottom;
    private int mSolverLeft;
    private int mSolverRight;
    private int mSolverTop;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    DimensionBehaviour mVerticalDimensionBehaviour;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    float mVerticalWeight;
    boolean mVerticalWrapVisited;
    private int mVisibility;
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
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList<ConstraintAnchor>();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
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
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }
    
    public ConstraintWidget(final int n, final int n2) {
        this(0, 0, n, n2);
    }
    
    public ConstraintWidget(final int mx, final int my, final int mWidth, final int mHeight) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mAnchors = new ArrayList<ConstraintAnchor>();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
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
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = mx;
        this.mY = my;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.addAnchors();
        this.forceUpdateDrawPosition();
    }
    
    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mBaseline);
    }
    
    private void applyConstraints(final LinearSystem linearSystem, final boolean b, boolean b2, final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, int n, int n2, int n3, final int n4, final float n5, final boolean b3, final boolean b4, final int n6, final int n7, final int n8) {
        final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintAnchor);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(constraintAnchor2);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        final int margin = constraintAnchor.getMargin();
        final int margin2 = constraintAnchor2.getMargin();
        if (this.mVisibility == 8) {
            b2 = true;
            n3 = 0;
        }
        if (objectVariable3 == null && objectVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, n));
            if (!b3) {
                if (b) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
                }
                else if (b2) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n3, false));
                }
                else {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, n2));
                }
            }
        }
        else if (objectVariable3 != null && objectVariable4 == null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
            }
            else if (!b3) {
                if (b2) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
                }
                else {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, n2));
                }
            }
        }
        else if (objectVariable3 == null && objectVariable4 != null) {
            linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, -1 * margin2));
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
            }
            else if (!b3) {
                if (b2) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
                }
                else {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, n));
                }
            }
        }
        else if (b2) {
            if (b) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, objectVariable2, objectVariable, n4, true));
            }
            else {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable, n3));
            }
            if (constraintAnchor.getStrength() != constraintAnchor2.getStrength()) {
                if (constraintAnchor.getStrength() == ConstraintAnchor.Strength.STRONG) {
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
                    final SolverVariable slackVariable = linearSystem.createSlackVariable();
                    final ArrayRow row = linearSystem.createRow();
                    row.createRowLowerThan(objectVariable2, objectVariable4, slackVariable, -1 * margin2);
                    linearSystem.addConstraint(row);
                }
                else {
                    final SolverVariable slackVariable2 = linearSystem.createSlackVariable();
                    final ArrayRow row2 = linearSystem.createRow();
                    row2.createRowGreaterThan(objectVariable, objectVariable3, slackVariable2, margin);
                    linearSystem.addConstraint(row2);
                    linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, -1 * margin2));
                }
            }
            else if (objectVariable3 == objectVariable4) {
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, 0, 0.5f, objectVariable4, objectVariable2, 0, true));
            }
            else if (!b4) {
                linearSystem.addConstraint(LinearSystem.createRowGreaterThan(linearSystem, objectVariable, objectVariable3, margin, constraintAnchor.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT));
                linearSystem.addConstraint(LinearSystem.createRowLowerThan(linearSystem, objectVariable2, objectVariable4, -1 * margin2, constraintAnchor2.getConnectionType() != ConstraintAnchor.ConnectionType.STRICT));
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, false));
            }
        }
        else if (b3) {
            linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 3);
            linearSystem.addLowerThan(objectVariable2, objectVariable4, -1 * margin2, 3);
            linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, true));
        }
        else if (!b4) {
            if (n6 == 1) {
                n = n7;
                if (n <= n3) {
                    n = n3;
                }
                n2 = n;
                if (n8 > 0) {
                    if (n8 < n) {
                        n2 = n8;
                    }
                    else {
                        linearSystem.addLowerThan(objectVariable2, objectVariable, n8, 3);
                        n2 = n;
                    }
                }
                linearSystem.addEquality(objectVariable2, objectVariable, n2, 3);
                linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 2);
                linearSystem.addLowerThan(objectVariable2, objectVariable4, -margin2, 2);
                linearSystem.addCentering(objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, 4);
            }
            else if (n7 == 0 && n8 == 0) {
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable, objectVariable3, margin));
                linearSystem.addConstraint(linearSystem.createRow().createRowEquals(objectVariable2, objectVariable4, -1 * margin2));
            }
            else {
                if (n8 > 0) {
                    linearSystem.addLowerThan(objectVariable2, objectVariable, n8, 3);
                }
                linearSystem.addGreaterThan(objectVariable, objectVariable3, margin, 2);
                linearSystem.addLowerThan(objectVariable2, objectVariable4, -margin2, 2);
                linearSystem.addCentering(objectVariable, objectVariable3, margin, n5, objectVariable4, objectVariable2, margin2, 4);
            }
        }
    }
    
    public void addToSolver(final LinearSystem linearSystem) {
        this.addToSolver(linearSystem, Integer.MAX_VALUE);
    }
    
    public void addToSolver(final LinearSystem linearSystem, final int n) {
        SolverVariable objectVariable = null;
        SolverVariable objectVariable2;
        if (n != Integer.MAX_VALUE && this.mLeft.mGroup != n) {
            objectVariable2 = null;
        }
        else {
            objectVariable2 = linearSystem.createObjectVariable(this.mLeft);
        }
        SolverVariable objectVariable3;
        if (n != Integer.MAX_VALUE && this.mRight.mGroup != n) {
            objectVariable3 = null;
        }
        else {
            objectVariable3 = linearSystem.createObjectVariable(this.mRight);
        }
        SolverVariable objectVariable4;
        if (n != Integer.MAX_VALUE && this.mTop.mGroup != n) {
            objectVariable4 = null;
        }
        else {
            objectVariable4 = linearSystem.createObjectVariable(this.mTop);
        }
        SolverVariable objectVariable5;
        if (n != Integer.MAX_VALUE && this.mBottom.mGroup != n) {
            objectVariable5 = null;
        }
        else {
            objectVariable5 = linearSystem.createObjectVariable(this.mBottom);
        }
        if (n == Integer.MAX_VALUE || this.mBaseline.mGroup == n) {
            objectVariable = linearSystem.createObjectVariable(this.mBaseline);
        }
        int n4;
        boolean b;
        if (this.mParent != null) {
            int n2;
            if ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            int n3;
            if ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            if (this.mParent.getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && n2 == 0) {
                if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                    if (this.mLeft.mTarget != null && this.mLeft.mTarget.mOwner == this.mParent) {
                        this.mLeft.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable6 = linearSystem.createObjectVariable(this.mParent.mLeft);
                    final ArrayRow row = linearSystem.createRow();
                    row.createRowGreaterThan(objectVariable2, objectVariable6, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row);
                }
                if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                    if (this.mRight.mTarget != null && this.mRight.mTarget.mOwner == this.mParent) {
                        this.mRight.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable7 = linearSystem.createObjectVariable(this.mParent.mRight);
                    final ArrayRow row2 = linearSystem.createRow();
                    row2.createRowGreaterThan(objectVariable7, objectVariable3, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row2);
                }
            }
            if (this.mParent.getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT && n3 == 0) {
                if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                    if (this.mTop.mTarget != null && this.mTop.mTarget.mOwner == this.mParent) {
                        this.mTop.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable8 = linearSystem.createObjectVariable(this.mParent.mTop);
                    final ArrayRow row3 = linearSystem.createRow();
                    row3.createRowGreaterThan(objectVariable4, objectVariable8, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row3);
                }
                if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                    if (this.mBottom.mTarget != null && this.mBottom.mTarget.mOwner == this.mParent) {
                        this.mBottom.setConnectionType(ConstraintAnchor.ConnectionType.STRICT);
                    }
                }
                else {
                    final SolverVariable objectVariable9 = linearSystem.createObjectVariable(this.mParent.mBottom);
                    final ArrayRow row4 = linearSystem.createRow();
                    row4.createRowGreaterThan(objectVariable9, objectVariable5, linearSystem.createSlackVariable(), 0);
                    linearSystem.addConstraint(row4);
                }
            }
            n4 = n2;
            b = (n3 != 0);
        }
        else {
            n4 = ((b = false) ? 1 : 0);
        }
        int n5;
        if ((n5 = this.mWidth) < this.mMinWidth) {
            n5 = this.mMinWidth;
        }
        int n6;
        if ((n6 = this.mHeight) < this.mMinHeight) {
            n6 = this.mMinHeight;
        }
        final boolean b2 = this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
        final boolean b3 = this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT;
        int n7 = b2 ? 1 : 0;
        Label_0987: {
            if (!b2) {
                n7 = (b2 ? 1 : 0);
                if (this.mLeft != null) {
                    n7 = (b2 ? 1 : 0);
                    if (this.mRight != null) {
                        if (this.mLeft.mTarget != null) {
                            n7 = (b2 ? 1 : 0);
                            if (this.mRight.mTarget != null) {
                                break Label_0987;
                            }
                        }
                        n7 = 1;
                    }
                }
            }
        }
        int n8 = 0;
        Label_1087: {
            if ((n8 = (b3 ? 1 : 0)) == 0) {
                n8 = (b3 ? 1 : 0);
                if (this.mTop != null) {
                    n8 = (b3 ? 1 : 0);
                    if (this.mBottom != null) {
                        if (this.mTop.mTarget != null) {
                            n8 = (b3 ? 1 : 0);
                            if (this.mBottom.mTarget != null) {
                                break Label_1087;
                            }
                        }
                        if (this.mBaselineDistance != 0) {
                            n8 = (b3 ? 1 : 0);
                            if (this.mBaseline == null) {
                                break Label_1087;
                            }
                            if (this.mTop.mTarget != null) {
                                n8 = (b3 ? 1 : 0);
                                if (this.mBaseline.mTarget != null) {
                                    break Label_1087;
                                }
                            }
                        }
                        n8 = 1;
                    }
                }
            }
        }
        int mDimensionRatioSide = this.mDimensionRatioSide;
        float mDimensionRatio = this.mDimensionRatio;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        boolean b4 = false;
        int n15 = 0;
        Label_1343: {
            Label_1319: {
                if (this.mDimensionRatio > 0.0f && this.mVisibility != 8) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        int n9;
                        if (n7 && n8 == 0) {
                            n9 = 0;
                        }
                        else if (n7 == 0 && n8) {
                            if (this.mDimensionRatioSide == -1) {
                                mDimensionRatio = 1.0f / mDimensionRatio;
                            }
                            n9 = 1;
                        }
                        else {
                            n9 = mDimensionRatioSide;
                        }
                        final int n10 = 1;
                        mDimensionRatioSide = n9;
                        n11 = n10;
                        break Label_1319;
                    }
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        n12 = (int)(this.mHeight * mDimensionRatio);
                        n13 = n6;
                        n14 = n8;
                        b4 = true;
                        n15 = 0;
                        n11 = 0;
                        break Label_1343;
                    }
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        float n16 = mDimensionRatio;
                        if (this.mDimensionRatioSide == -1) {
                            n16 = 1.0f / mDimensionRatio;
                        }
                        final int n17 = (int)(this.mWidth * n16);
                        n12 = n5;
                        n13 = n17;
                        mDimensionRatio = n16;
                        n15 = 1;
                        n11 = 0;
                        final int n18 = 1;
                        b4 = (n7 != 0);
                        n14 = n18;
                        break Label_1343;
                    }
                }
                n11 = 0;
            }
            n12 = n5;
            n13 = n6;
            final boolean b5 = n7 != 0;
            n14 = n8;
            n15 = mDimensionRatioSide;
            b4 = b5;
        }
        final boolean b6 = n11 != 0 && (n15 == 0 || n15 == -1);
        final boolean b7 = this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        if (this.mHorizontalResolution != 2 && (n == Integer.MAX_VALUE || (this.mLeft.mGroup == n && this.mRight.mGroup == n))) {
            if (b6 && this.mLeft.mTarget != null && this.mRight.mTarget != null) {
                final SolverVariable objectVariable10 = linearSystem.createObjectVariable(this.mLeft);
                final SolverVariable objectVariable11 = linearSystem.createObjectVariable(this.mRight);
                final SolverVariable objectVariable12 = linearSystem.createObjectVariable(this.mLeft.getTarget());
                final SolverVariable objectVariable13 = linearSystem.createObjectVariable(this.mRight.getTarget());
                linearSystem.addGreaterThan(objectVariable10, objectVariable12, this.mLeft.getMargin(), 3);
                linearSystem.addLowerThan(objectVariable11, objectVariable13, this.mRight.getMargin() * -1, 3);
                if (n4 == 0) {
                    linearSystem.addCentering(objectVariable10, objectVariable12, this.mLeft.getMargin(), this.mHorizontalBiasPercent, objectVariable13, objectVariable11, this.mRight.getMargin(), 4);
                }
            }
            else {
                this.applyConstraints(linearSystem, b7, b4, this.mLeft, this.mRight, this.mX, this.mX + n12, n12, this.mMinWidth, this.mHorizontalBiasPercent, b6, (boolean)(n4 != 0), this.mMatchConstraintDefaultWidth, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth);
            }
        }
        final SolverVariable solverVariable = objectVariable2;
        final SolverVariable solverVariable2 = objectVariable3;
        SolverVariable solverVariable3 = objectVariable4;
        final SolverVariable solverVariable4 = objectVariable5;
        if (this.mVerticalResolution == 2) {
            return;
        }
        final boolean b8 = this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer;
        boolean b9 = false;
        Label_1724: {
            if (n11 != 0) {
                final int n19 = n15;
                if (n19 == 1 || n19 == -1) {
                    b9 = true;
                    break Label_1724;
                }
            }
            b9 = false;
        }
        Label_2150: {
            if (this.mBaselineDistance > 0) {
                ConstraintAnchor constraintAnchor = this.mBottom;
                if (n == Integer.MAX_VALUE || (this.mBottom.mGroup == n && this.mBaseline.mGroup == n)) {
                    linearSystem.addEquality(objectVariable, solverVariable3, this.getBaselineDistance(), 5);
                }
                int mBaselineDistance;
                if (this.mBaseline.mTarget != null) {
                    mBaselineDistance = this.mBaselineDistance;
                    constraintAnchor = this.mBaseline;
                }
                else {
                    mBaselineDistance = n13;
                }
                if (n == Integer.MAX_VALUE || (this.mTop.mGroup == n && constraintAnchor.mGroup == n)) {
                    if (b9 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                        final SolverVariable objectVariable14 = linearSystem.createObjectVariable(this.mTop);
                        final SolverVariable objectVariable15 = linearSystem.createObjectVariable(this.mBottom);
                        final SolverVariable objectVariable16 = linearSystem.createObjectVariable(this.mTop.getTarget());
                        final SolverVariable objectVariable17 = linearSystem.createObjectVariable(this.mBottom.getTarget());
                        linearSystem.addGreaterThan(objectVariable14, objectVariable16, this.mTop.getMargin(), 3);
                        linearSystem.addLowerThan(objectVariable15, objectVariable17, -1 * this.mBottom.getMargin(), 3);
                        if (!b) {
                            linearSystem.addCentering(objectVariable14, objectVariable16, this.mTop.getMargin(), this.mVerticalBiasPercent, objectVariable17, objectVariable15, this.mBottom.getMargin(), 4);
                        }
                    }
                    else {
                        this.applyConstraints(linearSystem, b8, (boolean)(n14 != 0), this.mTop, constraintAnchor, this.mY, this.mY + mBaselineDistance, mBaselineDistance, this.mMinHeight, this.mVerticalBiasPercent, b9, b, this.mMatchConstraintDefaultHeight, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight);
                        linearSystem.addEquality(solverVariable4, solverVariable3, n13, 5);
                    }
                }
            }
            else {
                final SolverVariable solverVariable5 = solverVariable3;
                if (n != Integer.MAX_VALUE) {
                    solverVariable3 = solverVariable5;
                    if (this.mTop.mGroup != n) {
                        break Label_2150;
                    }
                    solverVariable3 = solverVariable5;
                    if (this.mBottom.mGroup != n) {
                        break Label_2150;
                    }
                }
                if (b9 && this.mTop.mTarget != null && this.mBottom.mTarget != null) {
                    final SolverVariable objectVariable18 = linearSystem.createObjectVariable(this.mTop);
                    final SolverVariable objectVariable19 = linearSystem.createObjectVariable(this.mBottom);
                    final SolverVariable objectVariable20 = linearSystem.createObjectVariable(this.mTop.getTarget());
                    final SolverVariable objectVariable21 = linearSystem.createObjectVariable(this.mBottom.getTarget());
                    linearSystem.addGreaterThan(objectVariable18, objectVariable20, this.mTop.getMargin(), 3);
                    linearSystem.addLowerThan(objectVariable19, objectVariable21, -1 * this.mBottom.getMargin(), 3);
                    solverVariable3 = solverVariable5;
                    if (!b) {
                        linearSystem.addCentering(objectVariable18, objectVariable20, this.mTop.getMargin(), this.mVerticalBiasPercent, objectVariable21, objectVariable19, this.mBottom.getMargin(), 4);
                        solverVariable3 = solverVariable5;
                    }
                }
                else {
                    final ConstraintAnchor mTop = this.mTop;
                    final ConstraintAnchor mBottom = this.mBottom;
                    final int my = this.mY;
                    final int my2 = this.mY;
                    final int mMinHeight = this.mMinHeight;
                    final float mVerticalBiasPercent = this.mVerticalBiasPercent;
                    final int mMatchConstraintDefaultHeight = this.mMatchConstraintDefaultHeight;
                    final int mMatchConstraintMinHeight = this.mMatchConstraintMinHeight;
                    final int mMatchConstraintMaxHeight = this.mMatchConstraintMaxHeight;
                    solverVariable3 = solverVariable5;
                    this.applyConstraints(linearSystem, b8, (boolean)(n14 != 0), mTop, mBottom, my, my2 + n13, n13, mMinHeight, mVerticalBiasPercent, b9, b, mMatchConstraintDefaultHeight, mMatchConstraintMinHeight, mMatchConstraintMaxHeight);
                }
            }
        }
        if (n11 != 0) {
            final ArrayRow row5 = linearSystem.createRow();
            if (n == Integer.MAX_VALUE || (this.mLeft.mGroup == n && this.mRight.mGroup == n)) {
                if (n15 == 0) {
                    linearSystem.addConstraint(row5.createRowDimensionRatio(solverVariable2, solverVariable, solverVariable4, solverVariable3, mDimensionRatio));
                }
                else if (n15 == 1) {
                    linearSystem.addConstraint(row5.createRowDimensionRatio(solverVariable4, solverVariable3, solverVariable2, solverVariable, mDimensionRatio));
                }
                else {
                    if (this.mMatchConstraintMinWidth > 0) {
                        linearSystem.addGreaterThan(solverVariable2, solverVariable, this.mMatchConstraintMinWidth, 3);
                    }
                    if (this.mMatchConstraintMinHeight > 0) {
                        linearSystem.addGreaterThan(solverVariable4, solverVariable3, this.mMatchConstraintMinHeight, 3);
                    }
                    row5.createRowDimensionRatio(solverVariable2, solverVariable, solverVariable4, solverVariable3, mDimensionRatio);
                    final SolverVariable errorVariable = linearSystem.createErrorVariable();
                    final SolverVariable errorVariable2 = linearSystem.createErrorVariable();
                    errorVariable.strength = 4;
                    errorVariable2.strength = 4;
                    row5.addError(errorVariable, errorVariable2);
                    linearSystem.addConstraint(row5);
                }
            }
        }
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n) {
        this.connect(type, constraintWidget, type2, n, ConstraintAnchor.Strength.STRONG);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final ConstraintAnchor.Strength strength) {
        this.connect(type, constraintWidget, type2, n, strength, 0);
    }
    
    public void connect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, int n, final ConstraintAnchor.Strength strength, final int n2) {
        final ConstraintAnchor.Type center = ConstraintAnchor.Type.CENTER;
        final int n3 = 0;
        if (type == center) {
            if (type2 == ConstraintAnchor.Type.CENTER) {
                final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
                final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
                final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
                final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                boolean b = true;
                if ((anchor != null && anchor.isConnected()) || (anchor2 != null && anchor2.isConnected())) {
                    n = 0;
                }
                else {
                    this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.RIGHT, constraintWidget, ConstraintAnchor.Type.RIGHT, 0, strength, n2);
                    n = 1;
                }
                if ((anchor3 != null && anchor3.isConnected()) || (anchor4 != null && anchor4.isConnected())) {
                    b = false;
                }
                else {
                    this.connect(ConstraintAnchor.Type.TOP, constraintWidget, ConstraintAnchor.Type.TOP, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, ConstraintAnchor.Type.BOTTOM, 0, strength, n2);
                }
                if (n != 0 && b) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, n2);
                }
                else if (n != 0) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, n2);
                }
                else if (b) {
                    this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, n2);
                }
            }
            else if (type2 != ConstraintAnchor.Type.LEFT && type2 != ConstraintAnchor.Type.RIGHT) {
                if (type2 == ConstraintAnchor.Type.TOP || type2 == ConstraintAnchor.Type.BOTTOM) {
                    this.connect(ConstraintAnchor.Type.TOP, constraintWidget, type2, 0, strength, n2);
                    this.connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type2, 0, strength, n2);
                    this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type2), 0, n2);
                }
            }
            else {
                this.connect(ConstraintAnchor.Type.LEFT, constraintWidget, type2, 0, strength, n2);
                this.connect(ConstraintAnchor.Type.RIGHT, constraintWidget, type2, 0, strength, n2);
                this.getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type2), 0, n2);
            }
        }
        else if (type == ConstraintAnchor.Type.CENTER_X && (type2 == ConstraintAnchor.Type.LEFT || type2 == ConstraintAnchor.Type.RIGHT)) {
            final ConstraintAnchor anchor5 = this.getAnchor(ConstraintAnchor.Type.LEFT);
            final ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            final ConstraintAnchor anchor7 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
            anchor5.connect(anchor6, 0, n2);
            anchor7.connect(anchor6, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(anchor6, 0, n2);
        }
        else if (type == ConstraintAnchor.Type.CENTER_Y && (type2 == ConstraintAnchor.Type.TOP || type2 == ConstraintAnchor.Type.BOTTOM)) {
            final ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(anchor8, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(anchor8, 0, n2);
        }
        else if (type == ConstraintAnchor.Type.CENTER_X && type2 == ConstraintAnchor.Type.CENTER_X) {
            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(type2), 0, n2);
        }
        else if (type == ConstraintAnchor.Type.CENTER_Y && type2 == ConstraintAnchor.Type.CENTER_Y) {
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, n2);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(type2), 0, n2);
        }
        else {
            final ConstraintAnchor anchor9 = this.getAnchor(type);
            final ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
            if (anchor9.isValidConnection(anchor10)) {
                if (type == ConstraintAnchor.Type.BASELINE) {
                    final ConstraintAnchor anchor11 = this.getAnchor(ConstraintAnchor.Type.TOP);
                    final ConstraintAnchor anchor12 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
                    if (anchor11 != null) {
                        anchor11.reset();
                    }
                    n = n3;
                    if (anchor12 != null) {
                        anchor12.reset();
                        n = n3;
                    }
                }
                else if (type != ConstraintAnchor.Type.TOP && type != ConstraintAnchor.Type.BOTTOM) {
                    if (type == ConstraintAnchor.Type.LEFT || type == ConstraintAnchor.Type.RIGHT) {
                        final ConstraintAnchor anchor13 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                        if (anchor13.getTarget() != anchor10) {
                            anchor13.reset();
                        }
                        final ConstraintAnchor opposite = this.getAnchor(type).getOpposite();
                        final ConstraintAnchor anchor14 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                        if (anchor14.isConnected()) {
                            opposite.reset();
                            anchor14.reset();
                        }
                    }
                }
                else {
                    final ConstraintAnchor anchor15 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
                    if (anchor15 != null) {
                        anchor15.reset();
                    }
                    final ConstraintAnchor anchor16 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                    if (anchor16.getTarget() != anchor10) {
                        anchor16.reset();
                    }
                    final ConstraintAnchor opposite2 = this.getAnchor(type).getOpposite();
                    final ConstraintAnchor anchor17 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
                    if (anchor17.isConnected()) {
                        opposite2.reset();
                        anchor17.reset();
                    }
                }
                anchor9.connect(anchor10, n, strength, n2);
                anchor10.getOwner().connectedTo(anchor9.getOwner());
            }
        }
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, 0);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final int n2) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, n2);
    }
    
    public void connect(final ConstraintAnchor constraintAnchor, final ConstraintAnchor constraintAnchor2, final int n, final ConstraintAnchor.Strength strength, final int n2) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n, strength, n2);
        }
    }
    
    public void connectedTo(final ConstraintWidget constraintWidget) {
    }
    
    public void disconnectUnlockedWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int size = anchors.size(), i = 0; i < size; ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget && constraintAnchor.getConnectionCreator() == 2) {
                constraintAnchor.reset();
            }
        }
    }
    
    public void disconnectWidget(final ConstraintWidget constraintWidget) {
        final ArrayList<ConstraintAnchor> anchors = this.getAnchors();
        for (int size = anchors.size(), i = 0; i < size; ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == constraintWidget) {
                constraintAnchor.reset();
            }
        }
    }
    
    public void forceUpdateDrawPosition() {
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
    
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                return null;
            }
            case 8: {
                return this.mCenter;
            }
            case 7: {
                return this.mCenterY;
            }
            case 6: {
                return this.mCenterX;
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
    
    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }
    
    public String getDebugName() {
        return this.mDebugName;
    }
    
    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }
    
    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }
    
    public int getDrawBottom() {
        return this.getDrawY() + this.mDrawHeight;
    }
    
    public int getDrawHeight() {
        return this.mDrawHeight;
    }
    
    public int getDrawRight() {
        return this.getDrawX() + this.mDrawWidth;
    }
    
    public int getDrawWidth() {
        return this.mDrawWidth;
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
    
    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintWidget constraintWidget3;
        if (this.isInHorizontalChain()) {
            ConstraintWidget constraintWidget = this;
            ConstraintWidget constraintWidget2 = null;
            while (true) {
                constraintWidget3 = constraintWidget2;
                if (constraintWidget2 != null) {
                    break;
                }
                constraintWidget3 = constraintWidget2;
                if (constraintWidget == null) {
                    break;
                }
                final ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor target;
                if (anchor == null) {
                    target = null;
                }
                else {
                    target = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target == null) {
                    owner = null;
                }
                else {
                    owner = target.getOwner();
                }
                if (owner == this.getParent()) {
                    constraintWidget3 = constraintWidget;
                    break;
                }
                ConstraintAnchor target2;
                if (owner == null) {
                    target2 = null;
                }
                else {
                    target2 = owner.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (target2 != null && target2.getOwner() != constraintWidget) {
                    constraintWidget2 = constraintWidget;
                }
                else {
                    constraintWidget = owner;
                }
            }
        }
        else {
            constraintWidget3 = null;
        }
        return constraintWidget3;
    }
    
    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }
    
    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mHorizontalDimensionBehaviour;
    }
    
    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }
    
    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }
    
    int getInternalDrawX() {
        return this.mDrawX;
    }
    
    int getInternalDrawY() {
        return this.mDrawY;
    }
    
    public int getLeft() {
        return this.getX();
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getOptimizerWrapHeight() {
        int b = this.mHeight;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            int mHeight;
            if (this.mMatchConstraintDefaultHeight == 1) {
                mHeight = Math.max(this.mMatchConstraintMinHeight, b);
            }
            else if (this.mMatchConstraintMinHeight > 0) {
                mHeight = this.mMatchConstraintMinHeight;
                this.mHeight = mHeight;
            }
            else {
                mHeight = 0;
            }
            b = mHeight;
            if (this.mMatchConstraintMaxHeight > 0 && this.mMatchConstraintMaxHeight < (b = mHeight)) {
                b = this.mMatchConstraintMaxHeight;
            }
        }
        return b;
    }
    
    public int getOptimizerWrapWidth() {
        int b = this.mWidth;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            int mWidth;
            if (this.mMatchConstraintDefaultWidth == 1) {
                mWidth = Math.max(this.mMatchConstraintMinWidth, b);
            }
            else if (this.mMatchConstraintMinWidth > 0) {
                mWidth = this.mMatchConstraintMinWidth;
                this.mWidth = mWidth;
            }
            else {
                mWidth = 0;
            }
            b = mWidth;
            if (this.mMatchConstraintMaxWidth > 0 && this.mMatchConstraintMaxWidth < (b = mWidth)) {
                b = this.mMatchConstraintMaxWidth;
            }
        }
        return b;
    }
    
    public ConstraintWidget getParent() {
        return this.mParent;
    }
    
    public int getRight() {
        return this.getX() + this.mWidth;
    }
    
    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget parent;
        for (parent = this; parent.getParent() != null; parent = parent.getParent()) {}
        if (parent instanceof WidgetContainer) {
            return (WidgetContainer)parent;
        }
        return null;
    }
    
    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }
    
    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }
    
    public int getTop() {
        return this.getY();
    }
    
    public String getType() {
        return this.mType;
    }
    
    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }
    
    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintWidget constraintWidget3;
        if (this.isInVerticalChain()) {
            ConstraintWidget constraintWidget = this;
            ConstraintWidget constraintWidget2 = null;
            while (true) {
                constraintWidget3 = constraintWidget2;
                if (constraintWidget2 != null) {
                    break;
                }
                constraintWidget3 = constraintWidget2;
                if (constraintWidget == null) {
                    break;
                }
                final ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor target;
                if (anchor == null) {
                    target = null;
                }
                else {
                    target = anchor.getTarget();
                }
                ConstraintWidget owner;
                if (target == null) {
                    owner = null;
                }
                else {
                    owner = target.getOwner();
                }
                if (owner == this.getParent()) {
                    constraintWidget3 = constraintWidget;
                    break;
                }
                ConstraintAnchor target2;
                if (owner == null) {
                    target2 = null;
                }
                else {
                    target2 = owner.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (target2 != null && target2.getOwner() != constraintWidget) {
                    constraintWidget2 = constraintWidget;
                }
                else {
                    constraintWidget = owner;
                }
            }
        }
        else {
            constraintWidget3 = null;
        }
        return constraintWidget3;
    }
    
    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }
    
    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mVerticalDimensionBehaviour;
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
    
    public boolean hasAncestor(final ConstraintWidget constraintWidget) {
        final ConstraintWidget parent = this.getParent();
        if (parent == constraintWidget) {
            return true;
        }
        ConstraintWidget parent2;
        if ((parent2 = parent) == constraintWidget.getParent()) {
            return false;
        }
        while (parent2 != null) {
            if (parent2 == constraintWidget) {
                return true;
            }
            if (parent2 == constraintWidget.getParent()) {
                return true;
            }
            parent2 = parent2.getParent();
        }
        return false;
    }
    
    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }
    
    public void immediateConnect(final ConstraintAnchor.Type type, final ConstraintWidget constraintWidget, final ConstraintAnchor.Type type2, final int n, final int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }
    
    public boolean isInHorizontalChain() {
        return (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight);
    }
    
    public boolean isInVerticalChain() {
        return (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom);
    }
    
    public boolean isInsideConstraintLayout() {
        ConstraintWidget constraintWidget;
        if ((constraintWidget = this.getParent()) == null) {
            return false;
        }
        while (constraintWidget != null) {
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                return true;
            }
            constraintWidget = constraintWidget.getParent();
        }
        return false;
    }
    
    public boolean isRoot() {
        return this.mParent == null;
    }
    
    public boolean isRootContainer() {
        return this instanceof ConstraintWidgetContainer && (this.mParent == null || !(this.mParent instanceof ConstraintWidgetContainer));
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
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
    }
    
    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
        if (this instanceof ConstraintWidgetContainer) {
            return;
        }
        if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.getWidth() > this.getMinWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
        if (this.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getHeight() == this.getWrapHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            }
            else if (this.getHeight() > this.getMinHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
    }
    
    public void resetAnchor(final ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        final ConstraintAnchor anchor = this.getAnchor(ConstraintAnchor.Type.LEFT);
        final ConstraintAnchor anchor2 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        final ConstraintAnchor anchor3 = this.getAnchor(ConstraintAnchor.Type.TOP);
        final ConstraintAnchor anchor4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        final ConstraintAnchor anchor5 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        final ConstraintAnchor anchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        final ConstraintAnchor anchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == anchor5) {
            if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                anchor.reset();
                anchor2.reset();
            }
            if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor3.reset();
                anchor4.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor6) {
            if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget().getOwner() == anchor2.getTarget().getOwner()) {
                anchor.reset();
                anchor2.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        }
        else if (constraintAnchor == anchor7) {
            if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget().getOwner() == anchor4.getTarget().getOwner()) {
                anchor3.reset();
                anchor4.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        }
        else if (constraintAnchor != anchor && constraintAnchor != anchor2) {
            if ((constraintAnchor == anchor3 || constraintAnchor == anchor4) && anchor3.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor5.reset();
            }
        }
        else if (anchor.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
            anchor5.reset();
        }
        constraintAnchor.reset();
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
    
    public void resetAnchors(final int n) {
        final ConstraintWidget parent = this.getParent();
        if (parent != null && parent instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        for (int i = 0; i < this.mAnchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = this.mAnchors.get(i);
            if (n == constraintAnchor.getConnectionCreator()) {
                if (constraintAnchor.isVerticalAnchor()) {
                    this.setVerticalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                else {
                    this.setHorizontalBiasPercent(ConstraintWidget.DEFAULT_BIAS);
                }
                constraintAnchor.reset();
            }
        }
    }
    
    public void resetGroups() {
        for (int size = this.mAnchors.size(), i = 0; i < size; ++i) {
            this.mAnchors.get(i).mGroup = Integer.MAX_VALUE;
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
    
    public void setBaselineDistance(final int mBaselineDistance) {
        this.mBaselineDistance = mBaselineDistance;
    }
    
    public void setCompanionWidget(final Object mCompanionWidget) {
        this.mCompanionWidget = mCompanionWidget;
    }
    
    public void setContainerItemSkip(final int mContainerItemSkip) {
        if (mContainerItemSkip >= 0) {
            this.mContainerItemSkip = mContainerItemSkip;
        }
        else {
            this.mContainerItemSkip = 0;
        }
    }
    
    public void setDebugName(final String mDebugName) {
        this.mDebugName = mDebugName;
    }
    
    public void setDebugSolverName(final LinearSystem linearSystem, final String s) {
        this.mDebugName = s;
        final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mLeft);
        final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mTop);
        final SolverVariable objectVariable3 = linearSystem.createObjectVariable(this.mRight);
        final SolverVariable objectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".left");
        objectVariable.setName(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(".top");
        objectVariable2.setName(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(".right");
        objectVariable3.setName(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append(".bottom");
        objectVariable4.setName(sb4.toString());
        if (this.mBaselineDistance > 0) {
            final SolverVariable objectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s);
            sb5.append(".baseline");
            objectVariable5.setName(sb5.toString());
        }
    }
    
    public void setDimension(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
        this.mHeight = mHeight;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }
    
    public void setDimensionRatio(final float mDimensionRatio, final int mDimensionRatioSide) {
        this.mDimensionRatio = mDimensionRatio;
        this.mDimensionRatioSide = mDimensionRatioSide;
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
                while (true) {
                    if (substring2.length() <= 0 || s.length() <= 0) {
                        break Label_0240;
                    }
                    try {
                        final float float1 = Float.parseFloat(substring2);
                        final float float2 = Float.parseFloat(s);
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
                            this.mDimensionRatio = 0.0f;
                            return;
                            s = s.substring(n3);
                            mDimensionRatio = Float.parseFloat(s);
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
    
    public void setDrawHeight(final int mDrawHeight) {
        this.mDrawHeight = mDrawHeight;
    }
    
    public void setDrawOrigin(final int n, final int n2) {
        this.mDrawX = n - this.mOffsetX;
        this.mDrawY = n2 - this.mOffsetY;
        this.mX = this.mDrawX;
        this.mY = this.mDrawY;
    }
    
    public void setDrawWidth(final int mDrawWidth) {
        this.mDrawWidth = mDrawWidth;
    }
    
    public void setDrawX(final int n) {
        this.mDrawX = n - this.mOffsetX;
        this.mX = this.mDrawX;
    }
    
    public void setDrawY(final int n) {
        this.mDrawY = n - this.mOffsetY;
        this.mY = this.mDrawY;
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
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.FIXED && (mWidth = n3) < this.mWidth) {
            mWidth = this.mWidth;
        }
        mHeight = n;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.FIXED && (mHeight = n) < this.mHeight) {
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
    
    public void setGoneMargin(final ConstraintAnchor.Type type, final int n) {
        switch (ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 4: {
                this.mBottom.mGoneMargin = n;
                break;
            }
            case 3: {
                this.mRight.mGoneMargin = n;
                break;
            }
            case 2: {
                this.mTop.mGoneMargin = n;
                break;
            }
            case 1: {
                this.mLeft.mGoneMargin = n;
                break;
            }
        }
    }
    
    public void setHeight(final int mHeight) {
        this.mHeight = mHeight;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
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
    
    public void setHorizontalDimensionBehaviour(final DimensionBehaviour mHorizontalDimensionBehaviour) {
        this.mHorizontalDimensionBehaviour = mHorizontalDimensionBehaviour;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
        }
    }
    
    public void setHorizontalMatchStyle(final int mMatchConstraintDefaultWidth, final int mMatchConstraintMinWidth, final int mMatchConstraintMaxWidth) {
        this.mMatchConstraintDefaultWidth = mMatchConstraintDefaultWidth;
        this.mMatchConstraintMinWidth = mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = mMatchConstraintMaxWidth;
    }
    
    public void setHorizontalWeight(final float mHorizontalWeight) {
        this.mHorizontalWeight = mHorizontalWeight;
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
    
    public void setType(final String mType) {
        this.mType = mType;
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
    
    public void setVerticalDimensionBehaviour(final DimensionBehaviour mVerticalDimensionBehaviour) {
        this.mVerticalDimensionBehaviour = mVerticalDimensionBehaviour;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
        }
    }
    
    public void setVerticalMatchStyle(final int mMatchConstraintDefaultHeight, final int mMatchConstraintMinHeight, final int mMatchConstraintMaxHeight) {
        this.mMatchConstraintDefaultHeight = mMatchConstraintDefaultHeight;
        this.mMatchConstraintMinHeight = mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = mMatchConstraintMaxHeight;
    }
    
    public void setVerticalWeight(final float mVerticalWeight) {
        this.mVerticalWeight = mVerticalWeight;
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
        sb.append(")");
        sb.append(" wrap: (");
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
        this.updateFromSolver(linearSystem, Integer.MAX_VALUE);
    }
    
    public void updateFromSolver(final LinearSystem linearSystem, final int n) {
        if (n == Integer.MAX_VALUE) {
            this.setFrame(linearSystem.getObjectVariableValue(this.mLeft), linearSystem.getObjectVariableValue(this.mTop), linearSystem.getObjectVariableValue(this.mRight), linearSystem.getObjectVariableValue(this.mBottom));
        }
        else if (n == -2) {
            this.setFrame(this.mSolverLeft, this.mSolverTop, this.mSolverRight, this.mSolverBottom);
        }
        else {
            if (this.mLeft.mGroup == n) {
                this.mSolverLeft = linearSystem.getObjectVariableValue(this.mLeft);
            }
            if (this.mTop.mGroup == n) {
                this.mSolverTop = linearSystem.getObjectVariableValue(this.mTop);
            }
            if (this.mRight.mGroup == n) {
                this.mSolverRight = linearSystem.getObjectVariableValue(this.mRight);
            }
            if (this.mBottom.mGroup == n) {
                this.mSolverBottom = linearSystem.getObjectVariableValue(this.mBottom);
            }
        }
    }
    
    public enum ContentAlignment
    {
        BEGIN, 
        BOTTOM, 
        END, 
        LEFT, 
        MIDDLE, 
        RIGHT, 
        TOP, 
        VERTICAL_MIDDLE;
    }
    
    public enum DimensionBehaviour
    {
        FIXED, 
        MATCH_CONSTRAINT, 
        MATCH_PARENT, 
        WRAP_CONTENT;
    }
}
