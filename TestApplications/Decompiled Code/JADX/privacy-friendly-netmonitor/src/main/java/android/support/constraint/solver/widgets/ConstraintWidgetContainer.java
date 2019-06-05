package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintWidgetContainer extends WidgetContainer {
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

    public String getType() {
        return "ConstraintLayout";
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

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

    public ConstraintWidgetContainer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
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

    public ConstraintWidgetContainer(int i, int i2) {
        super(i, i2);
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

    public void setOptimizationLevel(int i) {
        this.mOptimizationLevel = i;
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public static ConstraintWidgetContainer createContainer(ConstraintWidgetContainer constraintWidgetContainer, String str, ArrayList<ConstraintWidget> arrayList, int i) {
        Rectangle bounds = WidgetContainer.getBounds(arrayList);
        if (bounds.width == 0 || bounds.height == 0) {
            return null;
        }
        int min;
        if (i > 0) {
            min = Math.min(bounds.f10x, bounds.f11y);
            if (i > min) {
                i = min;
            }
            bounds.grow(i, i);
        }
        constraintWidgetContainer.setOrigin(bounds.f10x, bounds.f11y);
        constraintWidgetContainer.setDimension(bounds.width, bounds.height);
        constraintWidgetContainer.setDebugName(str);
        int i2 = 0;
        ConstraintWidget parent = ((ConstraintWidget) arrayList.get(0)).getParent();
        min = arrayList.size();
        while (i2 < min) {
            ConstraintWidget constraintWidget = (ConstraintWidget) arrayList.get(i2);
            if (constraintWidget.getParent() == parent) {
                constraintWidgetContainer.add(constraintWidget);
                constraintWidget.setX(constraintWidget.getX() - bounds.f10x);
                constraintWidget.setY(constraintWidget.getY() - bounds.f11y);
            }
            i2++;
        }
        return constraintWidgetContainer;
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem, int i) {
        boolean z;
        addToSolver(linearSystem, i);
        int size = this.mChildren.size();
        int i2 = 0;
        if (this.mOptimizationLevel != 2 && this.mOptimizationLevel != 4) {
            z = USE_SNAPSHOT;
        } else if (optimize(linearSystem)) {
            return false;
        } else {
            z = false;
        }
        while (i2 < size) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i2);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                DimensionBehaviour dimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
                DimensionBehaviour dimensionBehaviour2 = constraintWidget.mVerticalDimensionBehaviour;
                if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem, i);
                if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
                }
            } else {
                if (z) {
                    Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                }
                constraintWidget.addToSolver(linearSystem, i);
            }
            i2++;
        }
        if (this.mHorizontalChainsSize > 0) {
            applyHorizontalChain(linearSystem);
        }
        if (this.mVerticalChainsSize > 0) {
            applyVerticalChain(linearSystem);
        }
        return USE_SNAPSHOT;
    }

    private boolean optimize(LinearSystem linearSystem) {
        int i;
        int size = this.mChildren.size();
        for (i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            constraintWidget.mHorizontalResolution = -1;
            constraintWidget.mVerticalResolution = -1;
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mHorizontalResolution = 1;
                constraintWidget.mVerticalResolution = 1;
            }
        }
        boolean z = false;
        boolean z2 = z;
        boolean z3 = z2;
        while (!z) {
            int i2 = 0;
            boolean z4 = i2;
            boolean z5 = z4;
            while (i2 < size) {
                ConstraintWidget constraintWidget2 = (ConstraintWidget) this.mChildren.get(i2);
                if (constraintWidget2.mHorizontalResolution == -1) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mHorizontalResolution = 1;
                    } else {
                        Optimizer.checkHorizontalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mVerticalResolution = 1;
                    } else {
                        Optimizer.checkVerticalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    z4++;
                }
                if (constraintWidget2.mHorizontalResolution == -1) {
                    z5++;
                }
                i2++;
            }
            if (!(z4 || z5) || (z2 == z4 && z3 == z5)) {
                z = true;
            }
            z2 = z4;
            z3 = z5;
        }
        int i3 = 0;
        i = i3;
        int i4 = i;
        while (i3 < size) {
            ConstraintWidget constraintWidget3 = (ConstraintWidget) this.mChildren.get(i3);
            if (constraintWidget3.mHorizontalResolution == 1 || constraintWidget3.mHorizontalResolution == -1) {
                i++;
            }
            if (constraintWidget3.mVerticalResolution == 1 || constraintWidget3.mVerticalResolution == -1) {
                i4++;
            }
            i3++;
        }
        return (i == 0 && i4 == 0) ? USE_SNAPSHOT : false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:225:0x04bb A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x04b9  */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x04b9  */
    /* JADX WARNING: Removed duplicated region for block: B:225:0x04bb A:{SYNTHETIC} */
    private void applyHorizontalChain(android.support.constraint.solver.LinearSystem r35) {
        /*
        r34 = this;
        r6 = r34;
        r15 = r35;
        r14 = 0;
        r13 = r14;
    L_0x0006:
        r0 = r6.mHorizontalChainsSize;
        if (r13 >= r0) goto L_0x051c;
    L_0x000a:
        r0 = r6.mHorizontalChainsArray;
        r12 = r0[r13];
        r2 = r6.mChainEnds;
        r0 = r6.mHorizontalChainsArray;
        r3 = r0[r13];
        r4 = 0;
        r5 = r6.flags;
        r0 = r6;
        r1 = r15;
        r0 = r0.countMatchConstraintsChainedWidgets(r1, r2, r3, r4, r5);
        r1 = r6.mChainEnds;
        r2 = 2;
        r1 = r1[r2];
        if (r1 != 0) goto L_0x002a;
    L_0x0024:
        r4 = r13;
        r19 = r14;
        r3 = r15;
        goto L_0x0513;
    L_0x002a:
        r3 = r6.flags;
        r4 = 1;
        r3 = r3[r4];
        if (r3 == 0) goto L_0x0055;
    L_0x0031:
        r0 = r12.getDrawX();
    L_0x0035:
        if (r1 == 0) goto L_0x0024;
    L_0x0037:
        r2 = r1.mLeft;
        r2 = r2.mSolverVariable;
        r15.addEquality(r2, r0);
        r2 = r1.mHorizontalNextWidget;
        r3 = r1.mLeft;
        r3 = r3.getMargin();
        r4 = r1.getWidth();
        r3 = r3 + r4;
        r1 = r1.mRight;
        r1 = r1.getMargin();
        r3 = r3 + r1;
        r0 = r0 + r3;
        r1 = r2;
        goto L_0x0035;
    L_0x0055:
        r3 = r12.mHorizontalChainStyle;
        if (r3 != 0) goto L_0x005b;
    L_0x0059:
        r3 = r4;
        goto L_0x005c;
    L_0x005b:
        r3 = r14;
    L_0x005c:
        r5 = r12.mHorizontalChainStyle;
        if (r5 != r2) goto L_0x0062;
    L_0x0060:
        r5 = r4;
        goto L_0x0063;
    L_0x0062:
        r5 = r14;
    L_0x0063:
        r7 = r6.mHorizontalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r7 != r8) goto L_0x006b;
    L_0x0069:
        r7 = r4;
        goto L_0x006c;
    L_0x006b:
        r7 = r14;
    L_0x006c:
        r8 = r6.mOptimizationLevel;
        if (r8 == r2) goto L_0x0076;
    L_0x0070:
        r8 = r6.mOptimizationLevel;
        r9 = 8;
        if (r8 != r9) goto L_0x008c;
    L_0x0076:
        r8 = r6.flags;
        r8 = r8[r14];
        if (r8 == 0) goto L_0x008c;
    L_0x007c:
        r8 = r12.mHorizontalChainFixedPosition;
        if (r8 == 0) goto L_0x008c;
    L_0x0080:
        if (r5 != 0) goto L_0x008c;
    L_0x0082:
        if (r7 != 0) goto L_0x008c;
    L_0x0084:
        r7 = r12.mHorizontalChainStyle;
        if (r7 != 0) goto L_0x008c;
    L_0x0088:
        android.support.constraint.solver.widgets.Optimizer.applyDirectResolutionHorizontalChain(r6, r15, r0, r12);
        goto L_0x0024;
    L_0x008c:
        r11 = 3;
        r16 = 0;
        if (r0 == 0) goto L_0x0349;
    L_0x0091:
        if (r5 == 0) goto L_0x0095;
    L_0x0093:
        goto L_0x0349;
    L_0x0095:
        r3 = 0;
        r5 = r3;
        r3 = r16;
    L_0x0099:
        if (r1 == 0) goto L_0x015d;
    L_0x009b:
        r7 = r1.mHorizontalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r7 == r8) goto L_0x0117;
    L_0x00a1:
        r7 = r1.mLeft;
        r7 = r7.getMargin();
        if (r3 == 0) goto L_0x00b0;
    L_0x00a9:
        r3 = r3.mRight;
        r3 = r3.getMargin();
        r7 = r7 + r3;
    L_0x00b0:
        r3 = r1.mLeft;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mHorizontalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r3 != r8) goto L_0x00be;
    L_0x00bc:
        r3 = r2;
        goto L_0x00bf;
    L_0x00be:
        r3 = r11;
    L_0x00bf:
        r8 = r1.mLeft;
        r8 = r8.mSolverVariable;
        r9 = r1.mLeft;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r15.addGreaterThan(r8, r9, r7, r3);
        r3 = r1.mRight;
        r3 = r3.getMargin();
        r7 = r1.mRight;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mLeft;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x00f9;
    L_0x00de:
        r7 = r1.mRight;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mLeft;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        if (r7 != r1) goto L_0x00f9;
    L_0x00ec:
        r7 = r1.mRight;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mLeft;
        r7 = r7.getMargin();
        r3 = r3 + r7;
    L_0x00f9:
        r7 = r1.mRight;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mHorizontalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r7 != r8) goto L_0x0107;
    L_0x0105:
        r7 = r2;
        goto L_0x0108;
    L_0x0107:
        r7 = r11;
    L_0x0108:
        r8 = r1.mRight;
        r8 = r8.mSolverVariable;
        r9 = r1.mRight;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r3 = -r3;
        r15.addLowerThan(r8, r9, r3, r7);
        goto L_0x0154;
    L_0x0117:
        r3 = r1.mHorizontalWeight;
        r5 = r5 + r3;
        r3 = r1.mRight;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x013a;
    L_0x0120:
        r3 = r1.mRight;
        r3 = r3.getMargin();
        r7 = r6.mChainEnds;
        r7 = r7[r11];
        if (r1 == r7) goto L_0x013b;
    L_0x012c:
        r7 = r1.mRight;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mLeft;
        r7 = r7.getMargin();
        r3 = r3 + r7;
        goto L_0x013b;
    L_0x013a:
        r3 = r14;
    L_0x013b:
        r7 = r1.mRight;
        r7 = r7.mSolverVariable;
        r8 = r1.mLeft;
        r8 = r8.mSolverVariable;
        r15.addGreaterThan(r7, r8, r14, r4);
        r7 = r1.mRight;
        r7 = r7.mSolverVariable;
        r8 = r1.mRight;
        r8 = r8.mTarget;
        r8 = r8.mSolverVariable;
        r3 = -r3;
        r15.addLowerThan(r7, r8, r3, r4);
    L_0x0154:
        r3 = r1.mHorizontalNextWidget;
        r33 = r3;
        r3 = r1;
        r1 = r33;
        goto L_0x0099;
    L_0x015d:
        if (r0 != r4) goto L_0x01e4;
    L_0x015f:
        r0 = r6.mMatchConstraintsChainedWidgets;
        r0 = r0[r14];
        r1 = r0.mLeft;
        r1 = r1.getMargin();
        r3 = r0.mLeft;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0178;
    L_0x016f:
        r3 = r0.mLeft;
        r3 = r3.mTarget;
        r3 = r3.getMargin();
        r1 = r1 + r3;
    L_0x0178:
        r3 = r0.mRight;
        r3 = r3.getMargin();
        r5 = r0.mRight;
        r5 = r5.mTarget;
        if (r5 == 0) goto L_0x018d;
    L_0x0184:
        r5 = r0.mRight;
        r5 = r5.mTarget;
        r5 = r5.getMargin();
        r3 = r3 + r5;
    L_0x018d:
        r5 = r12.mRight;
        r5 = r5.mTarget;
        r5 = r5.mSolverVariable;
        r7 = r6.mChainEnds;
        r7 = r7[r11];
        if (r0 != r7) goto L_0x01a3;
    L_0x0199:
        r5 = r6.mChainEnds;
        r5 = r5[r4];
        r5 = r5.mRight;
        r5 = r5.mTarget;
        r5 = r5.mSolverVariable;
    L_0x01a3:
        r7 = r0.mMatchConstraintDefaultWidth;
        if (r7 != r4) goto L_0x01cd;
    L_0x01a7:
        r0 = r12.mLeft;
        r0 = r0.mSolverVariable;
        r7 = r12.mLeft;
        r7 = r7.mTarget;
        r7 = r7.mSolverVariable;
        r15.addGreaterThan(r0, r7, r1, r4);
        r0 = r12.mRight;
        r0 = r0.mSolverVariable;
        r1 = -r3;
        r15.addLowerThan(r0, r5, r1, r4);
        r0 = r12.mRight;
        r0 = r0.mSolverVariable;
        r1 = r12.mLeft;
        r1 = r1.mSolverVariable;
        r3 = r12.getWidth();
        r15.addEquality(r0, r1, r3, r2);
        goto L_0x0024;
    L_0x01cd:
        r2 = r0.mLeft;
        r2 = r2.mSolverVariable;
        r7 = r0.mLeft;
        r7 = r7.mTarget;
        r7 = r7.mSolverVariable;
        r15.addEquality(r2, r7, r1, r4);
        r0 = r0.mRight;
        r0 = r0.mSolverVariable;
        r1 = -r3;
        r15.addEquality(r0, r5, r1, r4);
        goto L_0x0024;
    L_0x01e4:
        r1 = r14;
    L_0x01e5:
        r3 = r0 + -1;
        if (r1 >= r3) goto L_0x0024;
    L_0x01e9:
        r7 = r6.mMatchConstraintsChainedWidgets;
        r7 = r7[r1];
        r8 = r6.mMatchConstraintsChainedWidgets;
        r1 = r1 + 1;
        r8 = r8[r1];
        r9 = r7.mLeft;
        r9 = r9.mSolverVariable;
        r10 = r7.mRight;
        r10 = r10.mSolverVariable;
        r14 = r8.mLeft;
        r14 = r14.mSolverVariable;
        r2 = r8.mRight;
        r2 = r2.mSolverVariable;
        r4 = r6.mChainEnds;
        r4 = r4[r11];
        if (r8 != r4) goto L_0x0212;
    L_0x0209:
        r2 = r6.mChainEnds;
        r4 = 1;
        r2 = r2[r4];
        r2 = r2.mRight;
        r2 = r2.mSolverVariable;
    L_0x0212:
        r4 = r7.mLeft;
        r4 = r4.getMargin();
        r11 = r7.mLeft;
        r11 = r11.mTarget;
        if (r11 == 0) goto L_0x0245;
    L_0x021e:
        r11 = r7.mLeft;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mRight;
        r11 = r11.mTarget;
        if (r11 == 0) goto L_0x0245;
    L_0x022a:
        r11 = r7.mLeft;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mRight;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        if (r11 != r7) goto L_0x0245;
    L_0x0238:
        r11 = r7.mLeft;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mRight;
        r11 = r11.getMargin();
        r4 = r4 + r11;
    L_0x0245:
        r11 = r7.mLeft;
        r11 = r11.mTarget;
        r11 = r11.mSolverVariable;
        r28 = r0;
        r0 = 2;
        r15.addGreaterThan(r9, r11, r4, r0);
        r0 = r7.mRight;
        r0 = r0.getMargin();
        r4 = r7.mRight;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0274;
    L_0x025d:
        r4 = r7.mHorizontalNextWidget;
        if (r4 == 0) goto L_0x0274;
    L_0x0261:
        r4 = r7.mHorizontalNextWidget;
        r4 = r4.mLeft;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0272;
    L_0x0269:
        r4 = r7.mHorizontalNextWidget;
        r4 = r4.mLeft;
        r4 = r4.getMargin();
        goto L_0x0273;
    L_0x0272:
        r4 = 0;
    L_0x0273:
        r0 = r0 + r4;
    L_0x0274:
        r4 = r7.mRight;
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        r0 = -r0;
        r11 = 2;
        r15.addLowerThan(r10, r4, r0, r11);
        if (r1 != r3) goto L_0x0301;
    L_0x0281:
        r0 = r8.mLeft;
        r0 = r0.getMargin();
        r3 = r8.mLeft;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x02b4;
    L_0x028d:
        r3 = r8.mLeft;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mRight;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x02b4;
    L_0x0299:
        r3 = r8.mLeft;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mRight;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        if (r3 != r8) goto L_0x02b4;
    L_0x02a7:
        r3 = r8.mLeft;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mRight;
        r3 = r3.getMargin();
        r0 = r0 + r3;
    L_0x02b4:
        r3 = r8.mLeft;
        r3 = r3.mTarget;
        r3 = r3.mSolverVariable;
        r4 = 2;
        r15.addGreaterThan(r14, r3, r0, r4);
        r0 = r8.mRight;
        r3 = r6.mChainEnds;
        r4 = 3;
        r3 = r3[r4];
        if (r8 != r3) goto L_0x02ce;
    L_0x02c7:
        r0 = r6.mChainEnds;
        r3 = 1;
        r0 = r0[r3];
        r0 = r0.mRight;
    L_0x02ce:
        r3 = r0.getMargin();
        r4 = r0.mTarget;
        if (r4 == 0) goto L_0x02f7;
    L_0x02d6:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mLeft;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x02f7;
    L_0x02e0:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mLeft;
        r4 = r4.mTarget;
        r4 = r4.mOwner;
        if (r4 != r8) goto L_0x02f7;
    L_0x02ec:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mLeft;
        r4 = r4.getMargin();
        r3 = r3 + r4;
    L_0x02f7:
        r0 = r0.mTarget;
        r0 = r0.mSolverVariable;
        r3 = -r3;
        r4 = 2;
        r15.addLowerThan(r2, r0, r3, r4);
        goto L_0x0302;
    L_0x0301:
        r4 = 2;
    L_0x0302:
        r0 = r12.mMatchConstraintMaxWidth;
        if (r0 <= 0) goto L_0x030b;
    L_0x0306:
        r0 = r12.mMatchConstraintMaxWidth;
        r15.addLowerThan(r10, r9, r0, r4);
    L_0x030b:
        r0 = r35.createRow();
        r3 = r7.mHorizontalWeight;
        r11 = r8.mHorizontalWeight;
        r4 = r7.mLeft;
        r21 = r4.getMargin();
        r4 = r7.mRight;
        r23 = r4.getMargin();
        r4 = r8.mLeft;
        r25 = r4.getMargin();
        r4 = r8.mRight;
        r27 = r4.getMargin();
        r16 = r0;
        r17 = r3;
        r18 = r5;
        r19 = r11;
        r20 = r9;
        r22 = r10;
        r24 = r14;
        r26 = r2;
        r16.createRowEqualDimension(r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27);
        r15.addConstraint(r0);
        r0 = r28;
        r2 = 2;
        r4 = 1;
        r11 = 3;
        r14 = 0;
        goto L_0x01e5;
    L_0x0349:
        r0 = r1;
        r2 = r16;
        r4 = r2;
        r7 = 0;
    L_0x034e:
        if (r0 == 0) goto L_0x04ca;
    L_0x0350:
        r8 = r0.mHorizontalNextWidget;
        if (r8 != 0) goto L_0x035c;
    L_0x0354:
        r2 = r6.mChainEnds;
        r7 = 1;
        r2 = r2[r7];
        r14 = r2;
        r2 = 1;
        goto L_0x035e;
    L_0x035c:
        r14 = r2;
        r2 = r7;
    L_0x035e:
        if (r5 == 0) goto L_0x03b6;
    L_0x0360:
        r7 = r0.mLeft;
        r9 = r7.getMargin();
        if (r4 == 0) goto L_0x036f;
    L_0x0368:
        r4 = r4.mRight;
        r4 = r4.getMargin();
        r9 = r9 + r4;
    L_0x036f:
        if (r1 == r0) goto L_0x0373;
    L_0x0371:
        r4 = 3;
        goto L_0x0374;
    L_0x0373:
        r4 = 1;
    L_0x0374:
        r10 = r7.mSolverVariable;
        r11 = r7.mTarget;
        r11 = r11.mSolverVariable;
        r15.addGreaterThan(r10, r11, r9, r4);
        r4 = r0.mHorizontalDimensionBehaviour;
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r4 != r9) goto L_0x03b4;
    L_0x0383:
        r4 = r0.mRight;
        r9 = r0.mMatchConstraintDefaultWidth;
        r10 = 1;
        if (r9 != r10) goto L_0x039e;
    L_0x038a:
        r9 = r0.mMatchConstraintMinWidth;
        r10 = r0.getWidth();
        r9 = java.lang.Math.max(r9, r10);
        r4 = r4.mSolverVariable;
        r7 = r7.mSolverVariable;
        r11 = 3;
        r15.addEquality(r4, r7, r9, r11);
        goto L_0x0410;
    L_0x039e:
        r11 = 3;
        r9 = r7.mSolverVariable;
        r10 = r7.mTarget;
        r10 = r10.mSolverVariable;
        r6 = r7.mMargin;
        r15.addGreaterThan(r9, r10, r6, r11);
        r4 = r4.mSolverVariable;
        r6 = r7.mSolverVariable;
        r7 = r0.mMatchConstraintMinWidth;
        r15.addLowerThan(r4, r6, r7, r11);
        goto L_0x0410;
    L_0x03b4:
        r11 = 3;
        goto L_0x0410;
    L_0x03b6:
        r11 = 3;
        r6 = 5;
        if (r3 != 0) goto L_0x03e5;
    L_0x03ba:
        if (r2 == 0) goto L_0x03e5;
    L_0x03bc:
        if (r4 == 0) goto L_0x03e5;
    L_0x03be:
        r4 = r0.mRight;
        r4 = r4.mTarget;
        if (r4 != 0) goto L_0x03d0;
    L_0x03c4:
        r4 = r0.mRight;
        r4 = r4.mSolverVariable;
        r6 = r0.getDrawRight();
        r15.addEquality(r4, r6);
        goto L_0x0410;
    L_0x03d0:
        r4 = r0.mRight;
        r4 = r4.getMargin();
        r7 = r0.mRight;
        r7 = r7.mSolverVariable;
        r9 = r14.mRight;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r4 = -r4;
        r15.addEquality(r7, r9, r4, r6);
        goto L_0x0410;
    L_0x03e5:
        if (r3 != 0) goto L_0x041d;
    L_0x03e7:
        if (r2 != 0) goto L_0x041d;
    L_0x03e9:
        if (r4 != 0) goto L_0x041d;
    L_0x03eb:
        r4 = r0.mLeft;
        r4 = r4.mTarget;
        if (r4 != 0) goto L_0x03fd;
    L_0x03f1:
        r4 = r0.mLeft;
        r4 = r4.mSolverVariable;
        r6 = r0.getDrawX();
        r15.addEquality(r4, r6);
        goto L_0x0410;
    L_0x03fd:
        r4 = r0.mLeft;
        r4 = r4.getMargin();
        r7 = r0.mLeft;
        r7 = r7.mSolverVariable;
        r9 = r12.mLeft;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r15.addEquality(r7, r9, r4, r6);
    L_0x0410:
        r29 = r0;
        r30 = r3;
        r0 = r12;
        r4 = r13;
        r18 = r14;
        r3 = r15;
        r19 = 0;
        goto L_0x04b7;
    L_0x041d:
        r6 = r0.mLeft;
        r7 = r0.mRight;
        r10 = r6.getMargin();
        r9 = r7.getMargin();
        r11 = r6.mSolverVariable;
        r29 = r0;
        r0 = r6.mTarget;
        r0 = r0.mSolverVariable;
        r30 = r3;
        r3 = 1;
        r15.addGreaterThan(r11, r0, r10, r3);
        r0 = r7.mSolverVariable;
        r11 = r7.mTarget;
        r11 = r11.mSolverVariable;
        r31 = r13;
        r13 = -r9;
        r15.addLowerThan(r0, r11, r13, r3);
        r0 = r6.mTarget;
        if (r0 == 0) goto L_0x044c;
    L_0x0447:
        r0 = r6.mTarget;
        r0 = r0.mSolverVariable;
        goto L_0x044e;
    L_0x044c:
        r0 = r16;
    L_0x044e:
        if (r4 != 0) goto L_0x045f;
    L_0x0450:
        r0 = r12.mLeft;
        r0 = r0.mTarget;
        if (r0 == 0) goto L_0x045d;
    L_0x0456:
        r0 = r12.mLeft;
        r0 = r0.mTarget;
        r0 = r0.mSolverVariable;
        goto L_0x045f;
    L_0x045d:
        r0 = r16;
    L_0x045f:
        if (r8 != 0) goto L_0x0471;
    L_0x0461:
        r3 = r14.mRight;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x046f;
    L_0x0467:
        r3 = r14.mRight;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r8 = r3;
        goto L_0x0471;
    L_0x046f:
        r8 = r16;
    L_0x0471:
        r3 = r8;
        if (r3 == 0) goto L_0x04ab;
    L_0x0474:
        r4 = r3.mLeft;
        r4 = r4.mSolverVariable;
        if (r2 == 0) goto L_0x0489;
    L_0x047a:
        r4 = r14.mRight;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0487;
    L_0x0480:
        r4 = r14.mRight;
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        goto L_0x0489;
    L_0x0487:
        r4 = r16;
    L_0x0489:
        if (r0 == 0) goto L_0x04ab;
    L_0x048b:
        if (r4 == 0) goto L_0x04ab;
    L_0x048d:
        r8 = r6.mSolverVariable;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r13 = r7.mSolverVariable;
        r6 = 4;
        r7 = r15;
        r17 = r9;
        r9 = r0;
        r0 = 3;
        r0 = r12;
        r12 = r4;
        r4 = r31;
        r18 = r14;
        r19 = 0;
        r14 = r17;
        r32 = r3;
        r3 = r15;
        r15 = r6;
        r7.addCentering(r8, r9, r10, r11, r12, r13, r14, r15);
        goto L_0x04b5;
    L_0x04ab:
        r32 = r3;
        r0 = r12;
        r18 = r14;
        r3 = r15;
        r4 = r31;
        r19 = 0;
    L_0x04b5:
        r8 = r32;
    L_0x04b7:
        if (r2 == 0) goto L_0x04bb;
    L_0x04b9:
        r8 = r16;
    L_0x04bb:
        r12 = r0;
        r7 = r2;
        r15 = r3;
        r13 = r4;
        r0 = r8;
        r2 = r18;
        r4 = r29;
        r3 = r30;
        r6 = r34;
        goto L_0x034e;
    L_0x04ca:
        r0 = r12;
        r4 = r13;
        r3 = r15;
        r19 = 0;
        if (r5 == 0) goto L_0x0513;
    L_0x04d1:
        r1 = r1.mLeft;
        r5 = r2.mRight;
        r10 = r1.getMargin();
        r14 = r5.getMargin();
        r6 = r0.mLeft;
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x04eb;
    L_0x04e3:
        r6 = r0.mLeft;
        r6 = r6.mTarget;
        r6 = r6.mSolverVariable;
        r9 = r6;
        goto L_0x04ed;
    L_0x04eb:
        r9 = r16;
    L_0x04ed:
        r6 = r2.mRight;
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x04fb;
    L_0x04f3:
        r2 = r2.mRight;
        r2 = r2.mTarget;
        r2 = r2.mSolverVariable;
        r12 = r2;
        goto L_0x04fd;
    L_0x04fb:
        r12 = r16;
    L_0x04fd:
        if (r9 == 0) goto L_0x0513;
    L_0x04ff:
        if (r12 == 0) goto L_0x0513;
    L_0x0501:
        r2 = r5.mSolverVariable;
        r6 = -r14;
        r7 = 1;
        r3.addLowerThan(r2, r12, r6, r7);
        r8 = r1.mSolverVariable;
        r11 = r0.mHorizontalBiasPercent;
        r13 = r5.mSolverVariable;
        r15 = 4;
        r7 = r3;
        r7.addCentering(r8, r9, r10, r11, r12, r13, r14, r15);
    L_0x0513:
        r13 = r4 + 1;
        r15 = r3;
        r14 = r19;
        r6 = r34;
        goto L_0x0006;
    L_0x051c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.applyHorizontalChain(android.support.constraint.solver.LinearSystem):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:235:0x04de A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x04dc  */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x04dc  */
    /* JADX WARNING: Removed duplicated region for block: B:235:0x04de A:{SYNTHETIC} */
    private void applyVerticalChain(android.support.constraint.solver.LinearSystem r35) {
        /*
        r34 = this;
        r6 = r34;
        r15 = r35;
        r14 = 0;
        r13 = r14;
    L_0x0006:
        r0 = r6.mVerticalChainsSize;
        if (r13 >= r0) goto L_0x053f;
    L_0x000a:
        r0 = r6.mVerticalChainsArray;
        r12 = r0[r13];
        r2 = r6.mChainEnds;
        r0 = r6.mVerticalChainsArray;
        r3 = r0[r13];
        r4 = 1;
        r5 = r6.flags;
        r0 = r6;
        r1 = r15;
        r0 = r0.countMatchConstraintsChainedWidgets(r1, r2, r3, r4, r5);
        r1 = r6.mChainEnds;
        r2 = 2;
        r1 = r1[r2];
        if (r1 != 0) goto L_0x002a;
    L_0x0024:
        r4 = r13;
        r19 = r14;
        r3 = r15;
        goto L_0x0536;
    L_0x002a:
        r3 = r6.flags;
        r4 = 1;
        r3 = r3[r4];
        if (r3 == 0) goto L_0x0055;
    L_0x0031:
        r0 = r12.getDrawY();
    L_0x0035:
        if (r1 == 0) goto L_0x0024;
    L_0x0037:
        r2 = r1.mTop;
        r2 = r2.mSolverVariable;
        r15.addEquality(r2, r0);
        r2 = r1.mVerticalNextWidget;
        r3 = r1.mTop;
        r3 = r3.getMargin();
        r4 = r1.getHeight();
        r3 = r3 + r4;
        r1 = r1.mBottom;
        r1 = r1.getMargin();
        r3 = r3 + r1;
        r0 = r0 + r3;
        r1 = r2;
        goto L_0x0035;
    L_0x0055:
        r3 = r12.mVerticalChainStyle;
        if (r3 != 0) goto L_0x005b;
    L_0x0059:
        r3 = r4;
        goto L_0x005c;
    L_0x005b:
        r3 = r14;
    L_0x005c:
        r5 = r12.mVerticalChainStyle;
        if (r5 != r2) goto L_0x0062;
    L_0x0060:
        r5 = r4;
        goto L_0x0063;
    L_0x0062:
        r5 = r14;
    L_0x0063:
        r7 = r6.mVerticalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r7 != r8) goto L_0x006b;
    L_0x0069:
        r7 = r4;
        goto L_0x006c;
    L_0x006b:
        r7 = r14;
    L_0x006c:
        r8 = r6.mOptimizationLevel;
        if (r8 == r2) goto L_0x0076;
    L_0x0070:
        r8 = r6.mOptimizationLevel;
        r9 = 8;
        if (r8 != r9) goto L_0x008c;
    L_0x0076:
        r8 = r6.flags;
        r8 = r8[r14];
        if (r8 == 0) goto L_0x008c;
    L_0x007c:
        r8 = r12.mVerticalChainFixedPosition;
        if (r8 == 0) goto L_0x008c;
    L_0x0080:
        if (r5 != 0) goto L_0x008c;
    L_0x0082:
        if (r7 != 0) goto L_0x008c;
    L_0x0084:
        r7 = r12.mVerticalChainStyle;
        if (r7 != 0) goto L_0x008c;
    L_0x0088:
        android.support.constraint.solver.widgets.Optimizer.applyDirectResolutionVerticalChain(r6, r15, r0, r12);
        goto L_0x0024;
    L_0x008c:
        r11 = 3;
        r16 = 0;
        if (r0 == 0) goto L_0x0349;
    L_0x0091:
        if (r5 == 0) goto L_0x0095;
    L_0x0093:
        goto L_0x0349;
    L_0x0095:
        r3 = 0;
        r5 = r3;
        r3 = r16;
    L_0x0099:
        if (r1 == 0) goto L_0x015d;
    L_0x009b:
        r7 = r1.mVerticalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r7 == r8) goto L_0x0117;
    L_0x00a1:
        r7 = r1.mTop;
        r7 = r7.getMargin();
        if (r3 == 0) goto L_0x00b0;
    L_0x00a9:
        r3 = r3.mBottom;
        r3 = r3.getMargin();
        r7 = r7 + r3;
    L_0x00b0:
        r3 = r1.mTop;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mVerticalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r3 != r8) goto L_0x00be;
    L_0x00bc:
        r3 = r2;
        goto L_0x00bf;
    L_0x00be:
        r3 = r11;
    L_0x00bf:
        r8 = r1.mTop;
        r8 = r8.mSolverVariable;
        r9 = r1.mTop;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r15.addGreaterThan(r8, r9, r7, r3);
        r3 = r1.mBottom;
        r3 = r3.getMargin();
        r7 = r1.mBottom;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mTop;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x00f9;
    L_0x00de:
        r7 = r1.mBottom;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mTop;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        if (r7 != r1) goto L_0x00f9;
    L_0x00ec:
        r7 = r1.mBottom;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mTop;
        r7 = r7.getMargin();
        r3 = r3 + r7;
    L_0x00f9:
        r7 = r1.mBottom;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mVerticalDimensionBehaviour;
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r7 != r8) goto L_0x0107;
    L_0x0105:
        r7 = r2;
        goto L_0x0108;
    L_0x0107:
        r7 = r11;
    L_0x0108:
        r8 = r1.mBottom;
        r8 = r8.mSolverVariable;
        r9 = r1.mBottom;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r3 = -r3;
        r15.addLowerThan(r8, r9, r3, r7);
        goto L_0x0154;
    L_0x0117:
        r3 = r1.mVerticalWeight;
        r5 = r5 + r3;
        r3 = r1.mBottom;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x013a;
    L_0x0120:
        r3 = r1.mBottom;
        r3 = r3.getMargin();
        r7 = r6.mChainEnds;
        r7 = r7[r11];
        if (r1 == r7) goto L_0x013b;
    L_0x012c:
        r7 = r1.mBottom;
        r7 = r7.mTarget;
        r7 = r7.mOwner;
        r7 = r7.mTop;
        r7 = r7.getMargin();
        r3 = r3 + r7;
        goto L_0x013b;
    L_0x013a:
        r3 = r14;
    L_0x013b:
        r7 = r1.mBottom;
        r7 = r7.mSolverVariable;
        r8 = r1.mTop;
        r8 = r8.mSolverVariable;
        r15.addGreaterThan(r7, r8, r14, r4);
        r7 = r1.mBottom;
        r7 = r7.mSolverVariable;
        r8 = r1.mBottom;
        r8 = r8.mTarget;
        r8 = r8.mSolverVariable;
        r3 = -r3;
        r15.addLowerThan(r7, r8, r3, r4);
    L_0x0154:
        r3 = r1.mVerticalNextWidget;
        r33 = r3;
        r3 = r1;
        r1 = r33;
        goto L_0x0099;
    L_0x015d:
        if (r0 != r4) goto L_0x01e4;
    L_0x015f:
        r0 = r6.mMatchConstraintsChainedWidgets;
        r0 = r0[r14];
        r1 = r0.mTop;
        r1 = r1.getMargin();
        r3 = r0.mTop;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0178;
    L_0x016f:
        r3 = r0.mTop;
        r3 = r3.mTarget;
        r3 = r3.getMargin();
        r1 = r1 + r3;
    L_0x0178:
        r3 = r0.mBottom;
        r3 = r3.getMargin();
        r5 = r0.mBottom;
        r5 = r5.mTarget;
        if (r5 == 0) goto L_0x018d;
    L_0x0184:
        r5 = r0.mBottom;
        r5 = r5.mTarget;
        r5 = r5.getMargin();
        r3 = r3 + r5;
    L_0x018d:
        r5 = r12.mBottom;
        r5 = r5.mTarget;
        r5 = r5.mSolverVariable;
        r7 = r6.mChainEnds;
        r7 = r7[r11];
        if (r0 != r7) goto L_0x01a3;
    L_0x0199:
        r5 = r6.mChainEnds;
        r5 = r5[r4];
        r5 = r5.mBottom;
        r5 = r5.mTarget;
        r5 = r5.mSolverVariable;
    L_0x01a3:
        r7 = r0.mMatchConstraintDefaultHeight;
        if (r7 != r4) goto L_0x01cd;
    L_0x01a7:
        r0 = r12.mTop;
        r0 = r0.mSolverVariable;
        r7 = r12.mTop;
        r7 = r7.mTarget;
        r7 = r7.mSolverVariable;
        r15.addGreaterThan(r0, r7, r1, r4);
        r0 = r12.mBottom;
        r0 = r0.mSolverVariable;
        r1 = -r3;
        r15.addLowerThan(r0, r5, r1, r4);
        r0 = r12.mBottom;
        r0 = r0.mSolverVariable;
        r1 = r12.mTop;
        r1 = r1.mSolverVariable;
        r3 = r12.getHeight();
        r15.addEquality(r0, r1, r3, r2);
        goto L_0x0024;
    L_0x01cd:
        r2 = r0.mTop;
        r2 = r2.mSolverVariable;
        r7 = r0.mTop;
        r7 = r7.mTarget;
        r7 = r7.mSolverVariable;
        r15.addEquality(r2, r7, r1, r4);
        r0 = r0.mBottom;
        r0 = r0.mSolverVariable;
        r1 = -r3;
        r15.addEquality(r0, r5, r1, r4);
        goto L_0x0024;
    L_0x01e4:
        r1 = r14;
    L_0x01e5:
        r3 = r0 + -1;
        if (r1 >= r3) goto L_0x0024;
    L_0x01e9:
        r7 = r6.mMatchConstraintsChainedWidgets;
        r7 = r7[r1];
        r8 = r6.mMatchConstraintsChainedWidgets;
        r1 = r1 + 1;
        r8 = r8[r1];
        r9 = r7.mTop;
        r9 = r9.mSolverVariable;
        r10 = r7.mBottom;
        r10 = r10.mSolverVariable;
        r14 = r8.mTop;
        r14 = r14.mSolverVariable;
        r2 = r8.mBottom;
        r2 = r2.mSolverVariable;
        r4 = r6.mChainEnds;
        r4 = r4[r11];
        if (r8 != r4) goto L_0x0212;
    L_0x0209:
        r2 = r6.mChainEnds;
        r4 = 1;
        r2 = r2[r4];
        r2 = r2.mBottom;
        r2 = r2.mSolverVariable;
    L_0x0212:
        r4 = r7.mTop;
        r4 = r4.getMargin();
        r11 = r7.mTop;
        r11 = r11.mTarget;
        if (r11 == 0) goto L_0x0245;
    L_0x021e:
        r11 = r7.mTop;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mBottom;
        r11 = r11.mTarget;
        if (r11 == 0) goto L_0x0245;
    L_0x022a:
        r11 = r7.mTop;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mBottom;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        if (r11 != r7) goto L_0x0245;
    L_0x0238:
        r11 = r7.mTop;
        r11 = r11.mTarget;
        r11 = r11.mOwner;
        r11 = r11.mBottom;
        r11 = r11.getMargin();
        r4 = r4 + r11;
    L_0x0245:
        r11 = r7.mTop;
        r11 = r11.mTarget;
        r11 = r11.mSolverVariable;
        r28 = r0;
        r0 = 2;
        r15.addGreaterThan(r9, r11, r4, r0);
        r0 = r7.mBottom;
        r0 = r0.getMargin();
        r4 = r7.mBottom;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0274;
    L_0x025d:
        r4 = r7.mVerticalNextWidget;
        if (r4 == 0) goto L_0x0274;
    L_0x0261:
        r4 = r7.mVerticalNextWidget;
        r4 = r4.mTop;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0272;
    L_0x0269:
        r4 = r7.mVerticalNextWidget;
        r4 = r4.mTop;
        r4 = r4.getMargin();
        goto L_0x0273;
    L_0x0272:
        r4 = 0;
    L_0x0273:
        r0 = r0 + r4;
    L_0x0274:
        r4 = r7.mBottom;
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        r0 = -r0;
        r11 = 2;
        r15.addLowerThan(r10, r4, r0, r11);
        if (r1 != r3) goto L_0x0301;
    L_0x0281:
        r0 = r8.mTop;
        r0 = r0.getMargin();
        r3 = r8.mTop;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x02b4;
    L_0x028d:
        r3 = r8.mTop;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mBottom;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x02b4;
    L_0x0299:
        r3 = r8.mTop;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mBottom;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        if (r3 != r8) goto L_0x02b4;
    L_0x02a7:
        r3 = r8.mTop;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r3 = r3.mBottom;
        r3 = r3.getMargin();
        r0 = r0 + r3;
    L_0x02b4:
        r3 = r8.mTop;
        r3 = r3.mTarget;
        r3 = r3.mSolverVariable;
        r4 = 2;
        r15.addGreaterThan(r14, r3, r0, r4);
        r0 = r8.mBottom;
        r3 = r6.mChainEnds;
        r4 = 3;
        r3 = r3[r4];
        if (r8 != r3) goto L_0x02ce;
    L_0x02c7:
        r0 = r6.mChainEnds;
        r3 = 1;
        r0 = r0[r3];
        r0 = r0.mBottom;
    L_0x02ce:
        r3 = r0.getMargin();
        r4 = r0.mTarget;
        if (r4 == 0) goto L_0x02f7;
    L_0x02d6:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mTop;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x02f7;
    L_0x02e0:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mTop;
        r4 = r4.mTarget;
        r4 = r4.mOwner;
        if (r4 != r8) goto L_0x02f7;
    L_0x02ec:
        r4 = r0.mTarget;
        r4 = r4.mOwner;
        r4 = r4.mTop;
        r4 = r4.getMargin();
        r3 = r3 + r4;
    L_0x02f7:
        r0 = r0.mTarget;
        r0 = r0.mSolverVariable;
        r3 = -r3;
        r4 = 2;
        r15.addLowerThan(r2, r0, r3, r4);
        goto L_0x0302;
    L_0x0301:
        r4 = 2;
    L_0x0302:
        r0 = r12.mMatchConstraintMaxHeight;
        if (r0 <= 0) goto L_0x030b;
    L_0x0306:
        r0 = r12.mMatchConstraintMaxHeight;
        r15.addLowerThan(r10, r9, r0, r4);
    L_0x030b:
        r0 = r35.createRow();
        r3 = r7.mVerticalWeight;
        r11 = r8.mVerticalWeight;
        r4 = r7.mTop;
        r21 = r4.getMargin();
        r4 = r7.mBottom;
        r23 = r4.getMargin();
        r4 = r8.mTop;
        r25 = r4.getMargin();
        r4 = r8.mBottom;
        r27 = r4.getMargin();
        r16 = r0;
        r17 = r3;
        r18 = r5;
        r19 = r11;
        r20 = r9;
        r22 = r10;
        r24 = r14;
        r26 = r2;
        r16.createRowEqualDimension(r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27);
        r15.addConstraint(r0);
        r0 = r28;
        r2 = 2;
        r4 = 1;
        r11 = 3;
        r14 = 0;
        goto L_0x01e5;
    L_0x0349:
        r0 = r1;
        r2 = r16;
        r4 = r2;
        r7 = 0;
    L_0x034e:
        if (r0 == 0) goto L_0x04ed;
    L_0x0350:
        r8 = r0.mVerticalNextWidget;
        if (r8 != 0) goto L_0x035c;
    L_0x0354:
        r2 = r6.mChainEnds;
        r7 = 1;
        r2 = r2[r7];
        r14 = r2;
        r2 = 1;
        goto L_0x035e;
    L_0x035c:
        r14 = r2;
        r2 = r7;
    L_0x035e:
        if (r5 == 0) goto L_0x03d9;
    L_0x0360:
        r7 = r0.mTop;
        r9 = r7.getMargin();
        if (r4 == 0) goto L_0x036f;
    L_0x0368:
        r4 = r4.mBottom;
        r4 = r4.getMargin();
        r9 = r9 + r4;
    L_0x036f:
        if (r1 == r0) goto L_0x0373;
    L_0x0371:
        r4 = 3;
        goto L_0x0374;
    L_0x0373:
        r4 = 1;
    L_0x0374:
        r10 = r7.mTarget;
        if (r10 == 0) goto L_0x037f;
    L_0x0378:
        r10 = r7.mSolverVariable;
        r11 = r7.mTarget;
        r11 = r11.mSolverVariable;
        goto L_0x0399;
    L_0x037f:
        r10 = r0.mBaseline;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x0396;
    L_0x0385:
        r10 = r0.mBaseline;
        r10 = r10.mSolverVariable;
        r11 = r0.mBaseline;
        r11 = r11.mTarget;
        r11 = r11.mSolverVariable;
        r17 = r7.getMargin();
        r9 = r9 - r17;
        goto L_0x0399;
    L_0x0396:
        r10 = r16;
        r11 = r10;
    L_0x0399:
        if (r10 == 0) goto L_0x03a0;
    L_0x039b:
        if (r11 == 0) goto L_0x03a0;
    L_0x039d:
        r15.addGreaterThan(r10, r11, r9, r4);
    L_0x03a0:
        r4 = r0.mVerticalDimensionBehaviour;
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r4 != r9) goto L_0x03d7;
    L_0x03a6:
        r4 = r0.mBottom;
        r9 = r0.mMatchConstraintDefaultHeight;
        r10 = 1;
        if (r9 != r10) goto L_0x03c1;
    L_0x03ad:
        r9 = r0.mMatchConstraintMinHeight;
        r10 = r0.getHeight();
        r9 = java.lang.Math.max(r9, r10);
        r4 = r4.mSolverVariable;
        r7 = r7.mSolverVariable;
        r11 = 3;
        r15.addEquality(r4, r7, r9, r11);
        goto L_0x0433;
    L_0x03c1:
        r11 = 3;
        r9 = r7.mSolverVariable;
        r10 = r7.mTarget;
        r10 = r10.mSolverVariable;
        r6 = r7.mMargin;
        r15.addGreaterThan(r9, r10, r6, r11);
        r4 = r4.mSolverVariable;
        r6 = r7.mSolverVariable;
        r7 = r0.mMatchConstraintMinHeight;
        r15.addLowerThan(r4, r6, r7, r11);
        goto L_0x0433;
    L_0x03d7:
        r11 = 3;
        goto L_0x0433;
    L_0x03d9:
        r11 = 3;
        r6 = 5;
        if (r3 != 0) goto L_0x0408;
    L_0x03dd:
        if (r2 == 0) goto L_0x0408;
    L_0x03df:
        if (r4 == 0) goto L_0x0408;
    L_0x03e1:
        r4 = r0.mBottom;
        r4 = r4.mTarget;
        if (r4 != 0) goto L_0x03f3;
    L_0x03e7:
        r4 = r0.mBottom;
        r4 = r4.mSolverVariable;
        r6 = r0.getDrawBottom();
        r15.addEquality(r4, r6);
        goto L_0x0433;
    L_0x03f3:
        r4 = r0.mBottom;
        r4 = r4.getMargin();
        r7 = r0.mBottom;
        r7 = r7.mSolverVariable;
        r9 = r14.mBottom;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r4 = -r4;
        r15.addEquality(r7, r9, r4, r6);
        goto L_0x0433;
    L_0x0408:
        if (r3 != 0) goto L_0x0440;
    L_0x040a:
        if (r2 != 0) goto L_0x0440;
    L_0x040c:
        if (r4 != 0) goto L_0x0440;
    L_0x040e:
        r4 = r0.mTop;
        r4 = r4.mTarget;
        if (r4 != 0) goto L_0x0420;
    L_0x0414:
        r4 = r0.mTop;
        r4 = r4.mSolverVariable;
        r6 = r0.getDrawY();
        r15.addEquality(r4, r6);
        goto L_0x0433;
    L_0x0420:
        r4 = r0.mTop;
        r4 = r4.getMargin();
        r7 = r0.mTop;
        r7 = r7.mSolverVariable;
        r9 = r12.mTop;
        r9 = r9.mTarget;
        r9 = r9.mSolverVariable;
        r15.addEquality(r7, r9, r4, r6);
    L_0x0433:
        r29 = r0;
        r30 = r3;
        r0 = r12;
        r4 = r13;
        r18 = r14;
        r3 = r15;
        r19 = 0;
        goto L_0x04da;
    L_0x0440:
        r6 = r0.mTop;
        r7 = r0.mBottom;
        r10 = r6.getMargin();
        r9 = r7.getMargin();
        r11 = r6.mSolverVariable;
        r29 = r0;
        r0 = r6.mTarget;
        r0 = r0.mSolverVariable;
        r30 = r3;
        r3 = 1;
        r15.addGreaterThan(r11, r0, r10, r3);
        r0 = r7.mSolverVariable;
        r11 = r7.mTarget;
        r11 = r11.mSolverVariable;
        r31 = r13;
        r13 = -r9;
        r15.addLowerThan(r0, r11, r13, r3);
        r0 = r6.mTarget;
        if (r0 == 0) goto L_0x046f;
    L_0x046a:
        r0 = r6.mTarget;
        r0 = r0.mSolverVariable;
        goto L_0x0471;
    L_0x046f:
        r0 = r16;
    L_0x0471:
        if (r4 != 0) goto L_0x0482;
    L_0x0473:
        r0 = r12.mTop;
        r0 = r0.mTarget;
        if (r0 == 0) goto L_0x0480;
    L_0x0479:
        r0 = r12.mTop;
        r0 = r0.mTarget;
        r0 = r0.mSolverVariable;
        goto L_0x0482;
    L_0x0480:
        r0 = r16;
    L_0x0482:
        if (r8 != 0) goto L_0x0494;
    L_0x0484:
        r3 = r14.mBottom;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0492;
    L_0x048a:
        r3 = r14.mBottom;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r8 = r3;
        goto L_0x0494;
    L_0x0492:
        r8 = r16;
    L_0x0494:
        r3 = r8;
        if (r3 == 0) goto L_0x04ce;
    L_0x0497:
        r4 = r3.mTop;
        r4 = r4.mSolverVariable;
        if (r2 == 0) goto L_0x04ac;
    L_0x049d:
        r4 = r14.mBottom;
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x04aa;
    L_0x04a3:
        r4 = r14.mBottom;
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        goto L_0x04ac;
    L_0x04aa:
        r4 = r16;
    L_0x04ac:
        if (r0 == 0) goto L_0x04ce;
    L_0x04ae:
        if (r4 == 0) goto L_0x04ce;
    L_0x04b0:
        r8 = r6.mSolverVariable;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r13 = r7.mSolverVariable;
        r6 = 4;
        r7 = r15;
        r17 = r9;
        r9 = r0;
        r0 = 3;
        r0 = r12;
        r12 = r4;
        r4 = r31;
        r18 = r14;
        r19 = 0;
        r14 = r17;
        r32 = r3;
        r3 = r15;
        r15 = r6;
        r7.addCentering(r8, r9, r10, r11, r12, r13, r14, r15);
        goto L_0x04d8;
    L_0x04ce:
        r32 = r3;
        r0 = r12;
        r18 = r14;
        r3 = r15;
        r4 = r31;
        r19 = 0;
    L_0x04d8:
        r8 = r32;
    L_0x04da:
        if (r2 == 0) goto L_0x04de;
    L_0x04dc:
        r8 = r16;
    L_0x04de:
        r12 = r0;
        r7 = r2;
        r15 = r3;
        r13 = r4;
        r0 = r8;
        r2 = r18;
        r4 = r29;
        r3 = r30;
        r6 = r34;
        goto L_0x034e;
    L_0x04ed:
        r0 = r12;
        r4 = r13;
        r3 = r15;
        r19 = 0;
        if (r5 == 0) goto L_0x0536;
    L_0x04f4:
        r1 = r1.mTop;
        r5 = r2.mBottom;
        r10 = r1.getMargin();
        r14 = r5.getMargin();
        r6 = r0.mTop;
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x050e;
    L_0x0506:
        r6 = r0.mTop;
        r6 = r6.mTarget;
        r6 = r6.mSolverVariable;
        r9 = r6;
        goto L_0x0510;
    L_0x050e:
        r9 = r16;
    L_0x0510:
        r6 = r2.mBottom;
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x051e;
    L_0x0516:
        r2 = r2.mBottom;
        r2 = r2.mTarget;
        r2 = r2.mSolverVariable;
        r12 = r2;
        goto L_0x0520;
    L_0x051e:
        r12 = r16;
    L_0x0520:
        if (r9 == 0) goto L_0x0536;
    L_0x0522:
        if (r12 == 0) goto L_0x0536;
    L_0x0524:
        r2 = r5.mSolverVariable;
        r6 = -r14;
        r7 = 1;
        r3.addLowerThan(r2, r12, r6, r7);
        r8 = r1.mSolverVariable;
        r11 = r0.mVerticalBiasPercent;
        r13 = r5.mSolverVariable;
        r15 = 4;
        r7 = r3;
        r7.addCentering(r8, r9, r10, r11, r12, r13, r14, r15);
    L_0x0536:
        r13 = r4 + 1;
        r15 = r3;
        r14 = r19;
        r6 = r34;
        goto L_0x0006;
    L_0x053f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.applyVerticalChain(android.support.constraint.solver.LinearSystem):void");
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, int i, boolean[] zArr) {
        int i2 = 0;
        zArr[2] = false;
        updateFromSolver(linearSystem, i);
        int size = this.mChildren.size();
        while (i2 < size) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i2);
            constraintWidget.updateFromSolver(linearSystem, i);
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                zArr[2] = USE_SNAPSHOT;
            }
            if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                zArr[2] = USE_SNAPSHOT;
            }
            i2++;
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingTop = i2;
        this.mPaddingRight = i3;
        this.mPaddingBottom = i4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x010e  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01e4  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01ef  */
    public void layout() {
        /*
        r18 = this;
        r1 = r18;
        r2 = r1.f6mX;
        r3 = r1.f7mY;
        r4 = r18.getWidth();
        r5 = 0;
        r4 = java.lang.Math.max(r5, r4);
        r6 = r18.getHeight();
        r6 = java.lang.Math.max(r5, r6);
        r1.mWidthMeasuredTooSmall = r5;
        r1.mHeightMeasuredTooSmall = r5;
        r7 = r1.mParent;
        if (r7 == 0) goto L_0x0046;
    L_0x001f:
        r7 = r1.mSnapshot;
        if (r7 != 0) goto L_0x002a;
    L_0x0023:
        r7 = new android.support.constraint.solver.widgets.Snapshot;
        r7.<init>(r1);
        r1.mSnapshot = r7;
    L_0x002a:
        r7 = r1.mSnapshot;
        r7.updateFrom(r1);
        r7 = r1.mPaddingLeft;
        r1.setX(r7);
        r7 = r1.mPaddingTop;
        r1.setY(r7);
        r18.resetAnchors();
        r7 = r1.mSystem;
        r7 = r7.getCache();
        r1.resetSolverVariables(r7);
        goto L_0x004a;
    L_0x0046:
        r1.f6mX = r5;
        r1.f7mY = r5;
    L_0x004a:
        r7 = r1.mVerticalDimensionBehaviour;
        r8 = r1.mHorizontalDimensionBehaviour;
        r9 = r1.mOptimizationLevel;
        r10 = 2;
        r11 = 1;
        if (r9 != r10) goto L_0x00bd;
    L_0x0054:
        r9 = r1.mVerticalDimensionBehaviour;
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r9 == r12) goto L_0x0060;
    L_0x005a:
        r9 = r1.mHorizontalDimensionBehaviour;
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r9 != r12) goto L_0x00bd;
    L_0x0060:
        r9 = r1.mChildren;
        r12 = r1.flags;
        r1.findWrapSize(r9, r12);
        r9 = r1.flags;
        r9 = r9[r5];
        if (r4 <= 0) goto L_0x0078;
    L_0x006d:
        if (r6 <= 0) goto L_0x0078;
    L_0x006f:
        r12 = r1.mWrapWidth;
        if (r12 > r4) goto L_0x0077;
    L_0x0073:
        r12 = r1.mWrapHeight;
        if (r12 <= r6) goto L_0x0078;
    L_0x0077:
        r9 = r5;
    L_0x0078:
        if (r9 == 0) goto L_0x00be;
    L_0x007a:
        r12 = r1.mHorizontalDimensionBehaviour;
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r12 != r13) goto L_0x009b;
    L_0x0080:
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mHorizontalDimensionBehaviour = r12;
        if (r4 <= 0) goto L_0x0090;
    L_0x0086:
        r12 = r1.mWrapWidth;
        if (r4 >= r12) goto L_0x0090;
    L_0x008a:
        r1.mWidthMeasuredTooSmall = r11;
        r1.setWidth(r4);
        goto L_0x009b;
    L_0x0090:
        r12 = r1.mMinWidth;
        r13 = r1.mWrapWidth;
        r12 = java.lang.Math.max(r12, r13);
        r1.setWidth(r12);
    L_0x009b:
        r12 = r1.mVerticalDimensionBehaviour;
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r12 != r13) goto L_0x00be;
    L_0x00a1:
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mVerticalDimensionBehaviour = r12;
        if (r6 <= 0) goto L_0x00b1;
    L_0x00a7:
        r12 = r1.mWrapHeight;
        if (r6 >= r12) goto L_0x00b1;
    L_0x00ab:
        r1.mHeightMeasuredTooSmall = r11;
        r1.setHeight(r6);
        goto L_0x00be;
    L_0x00b1:
        r12 = r1.mMinHeight;
        r13 = r1.mWrapHeight;
        r12 = java.lang.Math.max(r12, r13);
        r1.setHeight(r12);
        goto L_0x00be;
    L_0x00bd:
        r9 = r5;
    L_0x00be:
        r18.resetChains();
        r12 = r1.mChildren;
        r12 = r12.size();
        r13 = r5;
    L_0x00c8:
        if (r13 >= r12) goto L_0x00de;
    L_0x00ca:
        r14 = r1.mChildren;
        r14 = r14.get(r13);
        r14 = (android.support.constraint.solver.widgets.ConstraintWidget) r14;
        r15 = r14 instanceof android.support.constraint.solver.widgets.WidgetContainer;
        if (r15 == 0) goto L_0x00db;
    L_0x00d6:
        r14 = (android.support.constraint.solver.widgets.WidgetContainer) r14;
        r14.layout();
    L_0x00db:
        r13 = r13 + 1;
        goto L_0x00c8;
    L_0x00de:
        r13 = r5;
        r14 = r9;
        r9 = r11;
    L_0x00e1:
        if (r9 == 0) goto L_0x022c;
    L_0x00e3:
        r13 = r13 + r11;
        r15 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r5 = r1.mSystem;	 Catch:{ Exception -> 0x00fd }
        r5.reset();	 Catch:{ Exception -> 0x00fd }
        r5 = r1.mSystem;	 Catch:{ Exception -> 0x00fd }
        r5 = r1.addChildrenToSolver(r5, r15);	 Catch:{ Exception -> 0x00fd }
        if (r5 == 0) goto L_0x0103;
    L_0x00f4:
        r9 = r1.mSystem;	 Catch:{ Exception -> 0x00fa }
        r9.minimize();	 Catch:{ Exception -> 0x00fa }
        goto L_0x0103;
    L_0x00fa:
        r0 = move-exception;
        r9 = r5;
        goto L_0x00fe;
    L_0x00fd:
        r0 = move-exception;
    L_0x00fe:
        r5 = r0;
        r5.printStackTrace();
        r5 = r9;
    L_0x0103:
        if (r5 == 0) goto L_0x010e;
    L_0x0105:
        r5 = r1.mSystem;
        r9 = r1.flags;
        r1.updateChildrenFromSolver(r5, r15, r9);
    L_0x010c:
        r9 = r10;
        goto L_0x0150;
    L_0x010e:
        r5 = r1.mSystem;
        r1.updateFromSolver(r5, r15);
        r5 = 0;
    L_0x0114:
        if (r5 >= r12) goto L_0x010c;
    L_0x0116:
        r9 = r1.mChildren;
        r9 = r9.get(r5);
        r9 = (android.support.constraint.solver.widgets.ConstraintWidget) r9;
        r15 = r9.mHorizontalDimensionBehaviour;
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r15 != r10) goto L_0x0134;
    L_0x0124:
        r10 = r9.getWidth();
        r15 = r9.getWrapWidth();
        if (r10 >= r15) goto L_0x0134;
    L_0x012e:
        r5 = r1.flags;
        r10 = 2;
        r5[r10] = r11;
        goto L_0x010c;
    L_0x0134:
        r10 = 2;
        r15 = r9.mVerticalDimensionBehaviour;
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r15 != r10) goto L_0x014b;
    L_0x013b:
        r10 = r9.getHeight();
        r9 = r9.getWrapHeight();
        if (r10 >= r9) goto L_0x014b;
    L_0x0145:
        r5 = r1.flags;
        r9 = 2;
        r5[r9] = r11;
        goto L_0x0150;
    L_0x014b:
        r9 = 2;
        r5 = r5 + 1;
        r10 = r9;
        goto L_0x0114;
    L_0x0150:
        r5 = 8;
        if (r13 >= r5) goto L_0x01b9;
    L_0x0154:
        r5 = r1.flags;
        r5 = r5[r9];
        if (r5 == 0) goto L_0x01b9;
    L_0x015a:
        r5 = 0;
        r10 = 0;
        r15 = 0;
    L_0x015d:
        if (r5 >= r12) goto L_0x0183;
    L_0x015f:
        r9 = r1.mChildren;
        r9 = r9.get(r5);
        r9 = (android.support.constraint.solver.widgets.ConstraintWidget) r9;
        r11 = r9.f6mX;
        r16 = r9.getWidth();
        r11 = r11 + r16;
        r10 = java.lang.Math.max(r10, r11);
        r11 = r9.f7mY;
        r9 = r9.getHeight();
        r11 = r11 + r9;
        r15 = java.lang.Math.max(r15, r11);
        r5 = r5 + 1;
        r9 = 2;
        r11 = 1;
        goto L_0x015d;
    L_0x0183:
        r5 = r1.mMinWidth;
        r5 = java.lang.Math.max(r5, r10);
        r9 = r1.mMinHeight;
        r9 = java.lang.Math.max(r9, r15);
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r8 != r10) goto L_0x01a3;
    L_0x0193:
        r10 = r18.getWidth();
        if (r10 >= r5) goto L_0x01a3;
    L_0x0199:
        r1.setWidth(r5);
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        r1.mHorizontalDimensionBehaviour = r5;
        r5 = 1;
        r11 = 1;
        goto L_0x01a5;
    L_0x01a3:
        r11 = r14;
        r5 = 0;
    L_0x01a5:
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r7 != r10) goto L_0x01bb;
    L_0x01a9:
        r10 = r18.getHeight();
        if (r10 >= r9) goto L_0x01bb;
    L_0x01af:
        r1.setHeight(r9);
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        r1.mVerticalDimensionBehaviour = r5;
        r5 = 1;
        r11 = 1;
        goto L_0x01bb;
    L_0x01b9:
        r11 = r14;
        r5 = 0;
    L_0x01bb:
        r9 = r1.mMinWidth;
        r10 = r18.getWidth();
        r9 = java.lang.Math.max(r9, r10);
        r10 = r18.getWidth();
        if (r9 <= r10) goto L_0x01d4;
    L_0x01cb:
        r1.setWidth(r9);
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mHorizontalDimensionBehaviour = r5;
        r5 = 1;
        r11 = 1;
    L_0x01d4:
        r9 = r1.mMinHeight;
        r10 = r18.getHeight();
        r9 = java.lang.Math.max(r9, r10);
        r10 = r18.getHeight();
        if (r9 <= r10) goto L_0x01ed;
    L_0x01e4:
        r1.setHeight(r9);
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mVerticalDimensionBehaviour = r5;
        r5 = 1;
        r11 = 1;
    L_0x01ed:
        if (r11 != 0) goto L_0x0224;
    L_0x01ef:
        r9 = r1.mHorizontalDimensionBehaviour;
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r9 != r10) goto L_0x0209;
    L_0x01f5:
        if (r4 <= 0) goto L_0x0209;
    L_0x01f7:
        r9 = r18.getWidth();
        if (r9 <= r4) goto L_0x0209;
    L_0x01fd:
        r9 = 1;
        r1.mWidthMeasuredTooSmall = r9;
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mHorizontalDimensionBehaviour = r5;
        r1.setWidth(r4);
        r5 = 1;
        r11 = 1;
    L_0x0209:
        r9 = r1.mVerticalDimensionBehaviour;
        r10 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r9 != r10) goto L_0x0224;
    L_0x020f:
        if (r6 <= 0) goto L_0x0224;
    L_0x0211:
        r9 = r18.getHeight();
        if (r9 <= r6) goto L_0x0224;
    L_0x0217:
        r9 = 1;
        r1.mHeightMeasuredTooSmall = r9;
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED;
        r1.mVerticalDimensionBehaviour = r5;
        r1.setHeight(r6);
        r5 = r9;
        r14 = r5;
        goto L_0x0226;
    L_0x0224:
        r9 = 1;
        r14 = r11;
    L_0x0226:
        r11 = r9;
        r10 = 2;
        r9 = r5;
        r5 = 0;
        goto L_0x00e1;
    L_0x022c:
        r4 = r1.mParent;
        if (r4 == 0) goto L_0x025c;
    L_0x0230:
        r2 = r1.mMinWidth;
        r3 = r18.getWidth();
        r2 = java.lang.Math.max(r2, r3);
        r3 = r1.mMinHeight;
        r4 = r18.getHeight();
        r3 = java.lang.Math.max(r3, r4);
        r4 = r1.mSnapshot;
        r4.applyTo(r1);
        r4 = r1.mPaddingLeft;
        r2 = r2 + r4;
        r4 = r1.mPaddingRight;
        r2 = r2 + r4;
        r1.setWidth(r2);
        r2 = r1.mPaddingTop;
        r3 = r3 + r2;
        r2 = r1.mPaddingBottom;
        r3 = r3 + r2;
        r1.setHeight(r3);
        goto L_0x0260;
    L_0x025c:
        r1.f6mX = r2;
        r1.f7mY = r3;
    L_0x0260:
        if (r14 == 0) goto L_0x0266;
    L_0x0262:
        r1.mHorizontalDimensionBehaviour = r8;
        r1.mVerticalDimensionBehaviour = r7;
    L_0x0266:
        r2 = r1.mSystem;
        r2 = r2.getCache();
        r1.resetSolverVariables(r2);
        r2 = r18.getRootConstraintContainer();
        if (r1 != r2) goto L_0x0278;
    L_0x0275:
        r18.updateDrawPosition();
    L_0x0278:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.layout():void");
    }

    static int setGroup(ConstraintAnchor constraintAnchor, int i) {
        int i2 = constraintAnchor.mGroup;
        if (constraintAnchor.mOwner.getParent() == null) {
            return i;
        }
        if (i2 <= i) {
            return i2;
        }
        constraintAnchor.mGroup = i;
        ConstraintAnchor opposite = constraintAnchor.getOpposite();
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (opposite != null) {
            i = setGroup(opposite, i);
        }
        if (constraintAnchor2 != null) {
            i = setGroup(constraintAnchor2, i);
        }
        if (opposite != null) {
            i = setGroup(opposite, i);
        }
        constraintAnchor.mGroup = i;
        return i;
    }

    public int layoutFindGroupsSimple() {
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            constraintWidget.mLeft.mGroup = 0;
            constraintWidget.mRight.mGroup = 0;
            constraintWidget.mTop.mGroup = 1;
            constraintWidget.mBottom.mGroup = 1;
            constraintWidget.mBaseline.mGroup = 1;
        }
        return 2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:117:0x01bb  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01bb  */
    public void findHorizontalWrapRecursive(android.support.constraint.solver.widgets.ConstraintWidget r8, boolean[] r9) {
        /*
        r7 = this;
        r0 = r8.mHorizontalDimensionBehaviour;
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        r2 = 0;
        r3 = 0;
        if (r0 != r1) goto L_0x0017;
    L_0x0008:
        r0 = r8.mVerticalDimensionBehaviour;
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r0 != r1) goto L_0x0017;
    L_0x000e:
        r0 = r8.mDimensionRatio;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 <= 0) goto L_0x0017;
    L_0x0014:
        r9[r3] = r3;
        return;
    L_0x0017:
        r0 = r8.getOptimizerWrapWidth();
        r1 = r8.mHorizontalDimensionBehaviour;
        r4 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r4) goto L_0x0030;
    L_0x0021:
        r1 = r8.mVerticalDimensionBehaviour;
        r4 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 == r4) goto L_0x0030;
    L_0x0027:
        r1 = r8.mDimensionRatio;
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 <= 0) goto L_0x0030;
    L_0x002d:
        r9[r3] = r3;
        return;
    L_0x0030:
        r1 = 1;
        r8.mHorizontalWrapVisited = r1;
        r2 = r8 instanceof android.support.constraint.solver.widgets.Guideline;
        if (r2 == 0) goto L_0x0060;
    L_0x0037:
        r9 = r8;
        r9 = (android.support.constraint.solver.widgets.Guideline) r9;
        r2 = r9.getOrientation();
        if (r2 != r1) goto L_0x005c;
    L_0x0040:
        r0 = r9.getRelativeBegin();
        r1 = -1;
        if (r0 == r1) goto L_0x004e;
    L_0x0047:
        r9 = r9.getRelativeBegin();
        r0 = r3;
        r3 = r9;
        goto L_0x005d;
    L_0x004e:
        r0 = r9.getRelativeEnd();
        if (r0 == r1) goto L_0x005a;
    L_0x0054:
        r9 = r9.getRelativeEnd();
        r0 = r9;
        goto L_0x005d;
    L_0x005a:
        r0 = r3;
        goto L_0x005d;
    L_0x005c:
        r3 = r0;
    L_0x005d:
        r5 = r0;
        goto L_0x01b3;
    L_0x0060:
        r2 = r8.mRight;
        r2 = r2.isConnected();
        if (r2 != 0) goto L_0x0077;
    L_0x0068:
        r2 = r8.mLeft;
        r2 = r2.isConnected();
        if (r2 != 0) goto L_0x0077;
    L_0x0070:
        r9 = r8.getX();
        r3 = r0 + r9;
        goto L_0x005d;
    L_0x0077:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        if (r2 == 0) goto L_0x00a8;
    L_0x007d:
        r2 = r8.mLeft;
        r2 = r2.mTarget;
        if (r2 == 0) goto L_0x00a8;
    L_0x0083:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        r4 = r8.mLeft;
        r4 = r4.mTarget;
        if (r2 == r4) goto L_0x00a5;
    L_0x008d:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        r2 = r2.mOwner;
        r4 = r8.mLeft;
        r4 = r4.mTarget;
        r4 = r4.mOwner;
        if (r2 != r4) goto L_0x00a8;
    L_0x009b:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        r2 = r2.mOwner;
        r4 = r8.mParent;
        if (r2 == r4) goto L_0x00a8;
    L_0x00a5:
        r9[r3] = r3;
        return;
    L_0x00a8:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        r4 = 0;
        if (r2 == 0) goto L_0x00ca;
    L_0x00af:
        r2 = r8.mRight;
        r2 = r2.mTarget;
        r2 = r2.mOwner;
        r5 = r8.mRight;
        r5 = r5.getMargin();
        r5 = r5 + r0;
        r6 = r2.isRoot();
        if (r6 != 0) goto L_0x00cc;
    L_0x00c2:
        r6 = r2.mHorizontalWrapVisited;
        if (r6 != 0) goto L_0x00cc;
    L_0x00c6:
        r7.findHorizontalWrapRecursive(r2, r9);
        goto L_0x00cc;
    L_0x00ca:
        r5 = r0;
        r2 = r4;
    L_0x00cc:
        r6 = r8.mLeft;
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x00ec;
    L_0x00d2:
        r4 = r8.mLeft;
        r4 = r4.mTarget;
        r4 = r4.mOwner;
        r6 = r8.mLeft;
        r6 = r6.getMargin();
        r0 = r0 + r6;
        r6 = r4.isRoot();
        if (r6 != 0) goto L_0x00ec;
    L_0x00e5:
        r6 = r4.mHorizontalWrapVisited;
        if (r6 != 0) goto L_0x00ec;
    L_0x00e9:
        r7.findHorizontalWrapRecursive(r4, r9);
    L_0x00ec:
        r9 = r8.mRight;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x014e;
    L_0x00f2:
        r9 = r2.isRoot();
        if (r9 != 0) goto L_0x014e;
    L_0x00f8:
        r9 = r8.mRight;
        r9 = r9.mTarget;
        r9 = r9.mType;
        r6 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT;
        if (r9 != r6) goto L_0x010b;
    L_0x0102:
        r9 = r2.mDistToRight;
        r6 = r2.getOptimizerWrapWidth();
        r9 = r9 - r6;
        r5 = r5 + r9;
        goto L_0x011a;
    L_0x010b:
        r9 = r8.mRight;
        r9 = r9.mTarget;
        r9 = r9.getType();
        r6 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT;
        if (r9 != r6) goto L_0x011a;
    L_0x0117:
        r9 = r2.mDistToRight;
        r5 = r5 + r9;
    L_0x011a:
        r9 = r2.mRightHasCentered;
        if (r9 != 0) goto L_0x0133;
    L_0x011e:
        r9 = r2.mLeft;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x0131;
    L_0x0124:
        r9 = r2.mRight;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x0131;
    L_0x012a:
        r9 = r2.mHorizontalDimensionBehaviour;
        r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r9 == r6) goto L_0x0131;
    L_0x0130:
        goto L_0x0133;
    L_0x0131:
        r9 = r3;
        goto L_0x0134;
    L_0x0133:
        r9 = r1;
    L_0x0134:
        r8.mRightHasCentered = r9;
        r9 = r8.mRightHasCentered;
        if (r9 == 0) goto L_0x014e;
    L_0x013a:
        r9 = r2.mLeft;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x0141;
    L_0x0140:
        goto L_0x0149;
    L_0x0141:
        r9 = r2.mLeft;
        r9 = r9.mTarget;
        r9 = r9.mOwner;
        if (r9 == r8) goto L_0x014e;
    L_0x0149:
        r9 = r2.mDistToRight;
        r9 = r5 - r9;
        r5 = r5 + r9;
    L_0x014e:
        r9 = r8.mLeft;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x01b2;
    L_0x0154:
        r9 = r4.isRoot();
        if (r9 != 0) goto L_0x01b2;
    L_0x015a:
        r9 = r8.mLeft;
        r9 = r9.mTarget;
        r9 = r9.getType();
        r2 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT;
        if (r9 != r2) goto L_0x016f;
    L_0x0166:
        r9 = r4.mDistToLeft;
        r2 = r4.getOptimizerWrapWidth();
        r9 = r9 - r2;
        r0 = r0 + r9;
        goto L_0x017e;
    L_0x016f:
        r9 = r8.mLeft;
        r9 = r9.mTarget;
        r9 = r9.getType();
        r2 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT;
        if (r9 != r2) goto L_0x017e;
    L_0x017b:
        r9 = r4.mDistToLeft;
        r0 = r0 + r9;
    L_0x017e:
        r9 = r4.mLeftHasCentered;
        if (r9 != 0) goto L_0x0196;
    L_0x0182:
        r9 = r4.mLeft;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x0195;
    L_0x0188:
        r9 = r4.mRight;
        r9 = r9.mTarget;
        if (r9 == 0) goto L_0x0195;
    L_0x018e:
        r9 = r4.mHorizontalDimensionBehaviour;
        r2 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r9 == r2) goto L_0x0195;
    L_0x0194:
        goto L_0x0196;
    L_0x0195:
        r1 = r3;
    L_0x0196:
        r8.mLeftHasCentered = r1;
        r9 = r8.mLeftHasCentered;
        if (r9 == 0) goto L_0x01b2;
    L_0x019c:
        r9 = r4.mRight;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x01a3;
    L_0x01a2:
        goto L_0x01ab;
    L_0x01a3:
        r9 = r4.mRight;
        r9 = r9.mTarget;
        r9 = r9.mOwner;
        if (r9 == r8) goto L_0x01b2;
    L_0x01ab:
        r9 = r4.mDistToLeft;
        r9 = r0 - r9;
        r3 = r0 + r9;
        goto L_0x01b3;
    L_0x01b2:
        r3 = r0;
    L_0x01b3:
        r9 = r8.getVisibility();
        r0 = 8;
        if (r9 != r0) goto L_0x01c1;
    L_0x01bb:
        r9 = r8.mWidth;
        r3 = r3 - r9;
        r9 = r8.mWidth;
        r5 = r5 - r9;
    L_0x01c1:
        r8.mDistToLeft = r3;
        r8.mDistToRight = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.findHorizontalWrapRecursive(android.support.constraint.solver.widgets.ConstraintWidget, boolean[]):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:128:0x020b  */
    public void findVerticalWrapRecursive(android.support.constraint.solver.widgets.ConstraintWidget r9, boolean[] r10) {
        /*
        r8 = this;
        r0 = r9.mVerticalDimensionBehaviour;
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        r2 = 0;
        if (r0 != r1) goto L_0x0017;
    L_0x0007:
        r0 = r9.mHorizontalDimensionBehaviour;
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r0 == r1) goto L_0x0017;
    L_0x000d:
        r0 = r9.mDimensionRatio;
        r1 = 0;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 <= 0) goto L_0x0017;
    L_0x0014:
        r10[r2] = r2;
        return;
    L_0x0017:
        r0 = r9.getOptimizerWrapHeight();
        r1 = 1;
        r9.mVerticalWrapVisited = r1;
        r3 = r9 instanceof android.support.constraint.solver.widgets.Guideline;
        r4 = 8;
        if (r3 == 0) goto L_0x004d;
    L_0x0024:
        r10 = r9;
        r10 = (android.support.constraint.solver.widgets.Guideline) r10;
        r1 = r10.getOrientation();
        if (r1 != 0) goto L_0x0049;
    L_0x002d:
        r0 = r10.getRelativeBegin();
        r1 = -1;
        if (r0 == r1) goto L_0x003b;
    L_0x0034:
        r10 = r10.getRelativeBegin();
        r0 = r2;
        r2 = r10;
        goto L_0x004a;
    L_0x003b:
        r0 = r10.getRelativeEnd();
        if (r0 == r1) goto L_0x0047;
    L_0x0041:
        r10 = r10.getRelativeEnd();
        r0 = r10;
        goto L_0x004a;
    L_0x0047:
        r0 = r2;
        goto L_0x004a;
    L_0x0049:
        r2 = r0;
    L_0x004a:
        r6 = r2;
        goto L_0x0205;
    L_0x004d:
        r3 = r9.mBaseline;
        r3 = r3.mTarget;
        if (r3 != 0) goto L_0x0066;
    L_0x0053:
        r3 = r9.mTop;
        r3 = r3.mTarget;
        if (r3 != 0) goto L_0x0066;
    L_0x0059:
        r3 = r9.mBottom;
        r3 = r3.mTarget;
        if (r3 != 0) goto L_0x0066;
    L_0x005f:
        r10 = r9.getY();
        r2 = r0 + r10;
        goto L_0x004a;
    L_0x0066:
        r3 = r9.mBottom;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0097;
    L_0x006c:
        r3 = r9.mTop;
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0097;
    L_0x0072:
        r3 = r9.mBottom;
        r3 = r3.mTarget;
        r5 = r9.mTop;
        r5 = r5.mTarget;
        if (r3 == r5) goto L_0x0094;
    L_0x007c:
        r3 = r9.mBottom;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r5 = r9.mTop;
        r5 = r5.mTarget;
        r5 = r5.mOwner;
        if (r3 != r5) goto L_0x0097;
    L_0x008a:
        r3 = r9.mBottom;
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        r5 = r9.mParent;
        if (r3 == r5) goto L_0x0097;
    L_0x0094:
        r10[r2] = r2;
        return;
    L_0x0097:
        r3 = r9.mBaseline;
        r3 = r3.isConnected();
        if (r3 == 0) goto L_0x00d3;
    L_0x009f:
        r1 = r9.mBaseline;
        r1 = r1.mTarget;
        r1 = r1.getOwner();
        r2 = r1.mVerticalWrapVisited;
        if (r2 != 0) goto L_0x00ae;
    L_0x00ab:
        r8.findVerticalWrapRecursive(r1, r10);
    L_0x00ae:
        r10 = r1.mDistToTop;
        r2 = r1.mHeight;
        r10 = r10 - r2;
        r10 = r10 + r0;
        r10 = java.lang.Math.max(r10, r0);
        r2 = r1.mDistToBottom;
        r1 = r1.mHeight;
        r2 = r2 - r1;
        r2 = r2 + r0;
        r0 = java.lang.Math.max(r2, r0);
        r1 = r9.getVisibility();
        if (r1 != r4) goto L_0x00ce;
    L_0x00c8:
        r1 = r9.mHeight;
        r10 = r10 - r1;
        r1 = r9.mHeight;
        r0 = r0 - r1;
    L_0x00ce:
        r9.mDistToTop = r10;
        r9.mDistToBottom = r0;
        return;
    L_0x00d3:
        r3 = r9.mTop;
        r3 = r3.isConnected();
        r5 = 0;
        if (r3 == 0) goto L_0x00f9;
    L_0x00dc:
        r3 = r9.mTop;
        r3 = r3.mTarget;
        r3 = r3.getOwner();
        r6 = r9.mTop;
        r6 = r6.getMargin();
        r6 = r6 + r0;
        r7 = r3.isRoot();
        if (r7 != 0) goto L_0x00fb;
    L_0x00f1:
        r7 = r3.mVerticalWrapVisited;
        if (r7 != 0) goto L_0x00fb;
    L_0x00f5:
        r8.findVerticalWrapRecursive(r3, r10);
        goto L_0x00fb;
    L_0x00f9:
        r6 = r0;
        r3 = r5;
    L_0x00fb:
        r7 = r9.mBottom;
        r7 = r7.isConnected();
        if (r7 == 0) goto L_0x011f;
    L_0x0103:
        r5 = r9.mBottom;
        r5 = r5.mTarget;
        r5 = r5.getOwner();
        r7 = r9.mBottom;
        r7 = r7.getMargin();
        r0 = r0 + r7;
        r7 = r5.isRoot();
        if (r7 != 0) goto L_0x011f;
    L_0x0118:
        r7 = r5.mVerticalWrapVisited;
        if (r7 != 0) goto L_0x011f;
    L_0x011c:
        r8.findVerticalWrapRecursive(r5, r10);
    L_0x011f:
        r10 = r9.mTop;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x0193;
    L_0x0125:
        r10 = r3.isRoot();
        if (r10 != 0) goto L_0x0193;
    L_0x012b:
        r10 = r9.mTop;
        r10 = r10.mTarget;
        r10 = r10.getType();
        r7 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        if (r10 != r7) goto L_0x0140;
    L_0x0137:
        r10 = r3.mDistToTop;
        r7 = r3.getOptimizerWrapHeight();
        r10 = r10 - r7;
        r6 = r6 + r10;
        goto L_0x014f;
    L_0x0140:
        r10 = r9.mTop;
        r10 = r10.mTarget;
        r10 = r10.getType();
        r7 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        if (r10 != r7) goto L_0x014f;
    L_0x014c:
        r10 = r3.mDistToTop;
        r6 = r6 + r10;
    L_0x014f:
        r10 = r3.mTopHasCentered;
        if (r10 != 0) goto L_0x0178;
    L_0x0153:
        r10 = r3.mTop;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x0176;
    L_0x0159:
        r10 = r3.mTop;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x0176;
    L_0x0161:
        r10 = r3.mBottom;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x0176;
    L_0x0167:
        r10 = r3.mBottom;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x0176;
    L_0x016f:
        r10 = r3.mVerticalDimensionBehaviour;
        r7 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r10 == r7) goto L_0x0176;
    L_0x0175:
        goto L_0x0178;
    L_0x0176:
        r10 = r2;
        goto L_0x0179;
    L_0x0178:
        r10 = r1;
    L_0x0179:
        r9.mTopHasCentered = r10;
        r10 = r9.mTopHasCentered;
        if (r10 == 0) goto L_0x0193;
    L_0x017f:
        r10 = r3.mBottom;
        r10 = r10.mTarget;
        if (r10 != 0) goto L_0x0186;
    L_0x0185:
        goto L_0x018e;
    L_0x0186:
        r10 = r3.mBottom;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x0193;
    L_0x018e:
        r10 = r3.mDistToTop;
        r10 = r6 - r10;
        r6 = r6 + r10;
    L_0x0193:
        r10 = r9.mBottom;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x0205;
    L_0x0199:
        r10 = r5.isRoot();
        if (r10 != 0) goto L_0x0205;
    L_0x019f:
        r10 = r9.mBottom;
        r10 = r10.mTarget;
        r10 = r10.getType();
        r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        if (r10 != r3) goto L_0x01b4;
    L_0x01ab:
        r10 = r5.mDistToBottom;
        r3 = r5.getOptimizerWrapHeight();
        r10 = r10 - r3;
        r0 = r0 + r10;
        goto L_0x01c3;
    L_0x01b4:
        r10 = r9.mBottom;
        r10 = r10.mTarget;
        r10 = r10.getType();
        r3 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        if (r10 != r3) goto L_0x01c3;
    L_0x01c0:
        r10 = r5.mDistToBottom;
        r0 = r0 + r10;
    L_0x01c3:
        r10 = r5.mBottomHasCentered;
        if (r10 != 0) goto L_0x01eb;
    L_0x01c7:
        r10 = r5.mTop;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x01ea;
    L_0x01cd:
        r10 = r5.mTop;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x01ea;
    L_0x01d5:
        r10 = r5.mBottom;
        r10 = r10.mTarget;
        if (r10 == 0) goto L_0x01ea;
    L_0x01db:
        r10 = r5.mBottom;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x01ea;
    L_0x01e3:
        r10 = r5.mVerticalDimensionBehaviour;
        r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r10 == r3) goto L_0x01ea;
    L_0x01e9:
        goto L_0x01eb;
    L_0x01ea:
        r1 = r2;
    L_0x01eb:
        r9.mBottomHasCentered = r1;
        r10 = r9.mBottomHasCentered;
        if (r10 == 0) goto L_0x0205;
    L_0x01f1:
        r10 = r5.mTop;
        r10 = r10.mTarget;
        if (r10 != 0) goto L_0x01f8;
    L_0x01f7:
        goto L_0x0200;
    L_0x01f8:
        r10 = r5.mTop;
        r10 = r10.mTarget;
        r10 = r10.mOwner;
        if (r10 == r9) goto L_0x0205;
    L_0x0200:
        r10 = r5.mDistToBottom;
        r10 = r0 - r10;
        r0 = r0 + r10;
    L_0x0205:
        r10 = r9.getVisibility();
        if (r10 != r4) goto L_0x0211;
    L_0x020b:
        r10 = r9.mHeight;
        r6 = r6 - r10;
        r10 = r9.mHeight;
        r0 = r0 - r10;
    L_0x0211:
        r9.mDistToTop = r6;
        r9.mDistToBottom = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetContainer.findVerticalWrapRecursive(android.support.constraint.solver.widgets.ConstraintWidget, boolean[]):void");
    }

    public void findWrapSize(ArrayList<ConstraintWidget> arrayList, boolean[] zArr) {
        ArrayList<ConstraintWidget> arrayList2 = arrayList;
        boolean[] zArr2 = zArr;
        int size = arrayList.size();
        int i = 0;
        zArr2[0] = USE_SNAPSHOT;
        int i2 = 0;
        int i3 = i2;
        int i4 = i3;
        int i5 = i4;
        int i6 = i5;
        int i7 = i6;
        int i8 = i7;
        while (i2 < size) {
            ConstraintWidget constraintWidget = (ConstraintWidget) arrayList2.get(i2);
            if (!constraintWidget.isRoot()) {
                if (!constraintWidget.mHorizontalWrapVisited) {
                    findHorizontalWrapRecursive(constraintWidget, zArr2);
                }
                if (!constraintWidget.mVerticalWrapVisited) {
                    findVerticalWrapRecursive(constraintWidget, zArr2);
                }
                if (zArr2[i]) {
                    int height = (constraintWidget.mDistToTop + constraintWidget.mDistToBottom) - constraintWidget.getHeight();
                    i = constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT ? (constraintWidget.getWidth() + constraintWidget.mLeft.mMargin) + constraintWidget.mRight.mMargin : (constraintWidget.mDistToLeft + constraintWidget.mDistToRight) - constraintWidget.getWidth();
                    int height2 = constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT ? (constraintWidget.getHeight() + constraintWidget.mTop.mMargin) + constraintWidget.mBottom.mMargin : height;
                    if (constraintWidget.getVisibility() == 8) {
                        i = 0;
                        height2 = 0;
                    }
                    i3 = Math.max(i3, constraintWidget.mDistToLeft);
                    i4 = Math.max(i4, constraintWidget.mDistToRight);
                    i7 = Math.max(i7, constraintWidget.mDistToBottom);
                    i6 = Math.max(i6, constraintWidget.mDistToTop);
                    i = Math.max(i5, i);
                    i8 = Math.max(i8, height2);
                    i5 = i;
                } else {
                    return;
                }
            }
            i2++;
            i = 0;
        }
        this.mWrapWidth = Math.max(this.mMinWidth, Math.max(Math.max(i3, i4), i5));
        this.mWrapHeight = Math.max(this.mMinHeight, Math.max(Math.max(i6, i7), i8));
        for (int i9 = 0; i9 < size; i9++) {
            ConstraintWidget constraintWidget2 = (ConstraintWidget) arrayList2.get(i9);
            constraintWidget2.mHorizontalWrapVisited = false;
            constraintWidget2.mVerticalWrapVisited = false;
            constraintWidget2.mLeftHasCentered = false;
            constraintWidget2.mRightHasCentered = false;
            constraintWidget2.mTopHasCentered = false;
            constraintWidget2.mBottomHasCentered = false;
        }
    }

    public int layoutFindGroups() {
        int i;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor;
        int i2;
        r0 = new Type[5];
        int i3 = 0;
        r0[0] = Type.LEFT;
        r0[1] = Type.RIGHT;
        r0[2] = Type.TOP;
        r0[3] = Type.BASELINE;
        r0[4] = Type.BOTTOM;
        int size = this.mChildren.size();
        int i4 = 1;
        for (i = 0; i < size; i++) {
            constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            ConstraintAnchor constraintAnchor2 = constraintWidget.mLeft;
            if (constraintAnchor2.mTarget == null) {
                constraintAnchor2.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(constraintAnchor2, i4) == i4) {
                i4++;
            }
            constraintAnchor2 = constraintWidget.mTop;
            if (constraintAnchor2.mTarget == null) {
                constraintAnchor2.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(constraintAnchor2, i4) == i4) {
                i4++;
            }
            constraintAnchor2 = constraintWidget.mRight;
            if (constraintAnchor2.mTarget == null) {
                constraintAnchor2.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(constraintAnchor2, i4) == i4) {
                i4++;
            }
            constraintAnchor2 = constraintWidget.mBottom;
            if (constraintAnchor2.mTarget == null) {
                constraintAnchor2.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(constraintAnchor2, i4) == i4) {
                i4++;
            }
            constraintAnchor = constraintWidget.mBaseline;
            if (constraintAnchor.mTarget == null) {
                constraintAnchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(constraintAnchor, i4) == i4) {
                i4++;
            }
        }
        i = 1;
        while (i != 0) {
            i = 0;
            i4 = i;
            while (i < size) {
                constraintWidget = (ConstraintWidget) this.mChildren.get(i);
                i2 = i4;
                for (Type type : r0) {
                    ConstraintAnchor constraintAnchor3 = null;
                    switch (type) {
                        case LEFT:
                            constraintAnchor3 = constraintWidget.mLeft;
                            break;
                        case TOP:
                            constraintAnchor3 = constraintWidget.mTop;
                            break;
                        case RIGHT:
                            constraintAnchor3 = constraintWidget.mRight;
                            break;
                        case BOTTOM:
                            constraintAnchor3 = constraintWidget.mBottom;
                            break;
                        case BASELINE:
                            constraintAnchor3 = constraintWidget.mBaseline;
                            break;
                    }
                    ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
                    if (constraintAnchor4 != null) {
                        if (!(constraintAnchor4.mOwner.getParent() == null || constraintAnchor4.mGroup == constraintAnchor3.mGroup)) {
                            i2 = constraintAnchor3.mGroup > constraintAnchor4.mGroup ? constraintAnchor4.mGroup : constraintAnchor3.mGroup;
                            constraintAnchor3.mGroup = i2;
                            constraintAnchor4.mGroup = i2;
                            i2 = 1;
                        }
                        constraintAnchor4 = constraintAnchor4.getOpposite();
                        if (!(constraintAnchor4 == null || constraintAnchor4.mGroup == constraintAnchor3.mGroup)) {
                            i2 = constraintAnchor3.mGroup > constraintAnchor4.mGroup ? constraintAnchor4.mGroup : constraintAnchor3.mGroup;
                            constraintAnchor3.mGroup = i2;
                            constraintAnchor4.mGroup = i2;
                            i2 = 1;
                        }
                    }
                }
                i++;
                i4 = i2;
            }
            i = i4;
        }
        int[] iArr = new int[((this.mChildren.size() * r0.length) + 1)];
        Arrays.fill(iArr, -1);
        int i5 = 0;
        while (i3 < size) {
            int i6;
            ConstraintWidget constraintWidget2 = (ConstraintWidget) this.mChildren.get(i3);
            constraintAnchor = constraintWidget2.mLeft;
            if (constraintAnchor.mGroup != Integer.MAX_VALUE) {
                i2 = constraintAnchor.mGroup;
                if (iArr[i2] == -1) {
                    i6 = i5 + 1;
                    iArr[i2] = i5;
                    i5 = i6;
                }
                constraintAnchor.mGroup = iArr[i2];
            }
            constraintAnchor = constraintWidget2.mTop;
            if (constraintAnchor.mGroup != Integer.MAX_VALUE) {
                i2 = constraintAnchor.mGroup;
                if (iArr[i2] == -1) {
                    i6 = i5 + 1;
                    iArr[i2] = i5;
                    i5 = i6;
                }
                constraintAnchor.mGroup = iArr[i2];
            }
            constraintAnchor = constraintWidget2.mRight;
            if (constraintAnchor.mGroup != Integer.MAX_VALUE) {
                i2 = constraintAnchor.mGroup;
                if (iArr[i2] == -1) {
                    i6 = i5 + 1;
                    iArr[i2] = i5;
                    i5 = i6;
                }
                constraintAnchor.mGroup = iArr[i2];
            }
            constraintAnchor = constraintWidget2.mBottom;
            if (constraintAnchor.mGroup != Integer.MAX_VALUE) {
                i2 = constraintAnchor.mGroup;
                if (iArr[i2] == -1) {
                    i6 = i5 + 1;
                    iArr[i2] = i5;
                    i5 = i6;
                }
                constraintAnchor.mGroup = iArr[i2];
            }
            ConstraintAnchor constraintAnchor5 = constraintWidget2.mBaseline;
            if (constraintAnchor5.mGroup != Integer.MAX_VALUE) {
                int i7 = constraintAnchor5.mGroup;
                if (iArr[i7] == -1) {
                    i2 = i5 + 1;
                    iArr[i7] = i5;
                    i5 = i2;
                }
                constraintAnchor5.mGroup = iArr[i7];
            }
            i3++;
        }
        return i5;
    }

    public void layoutWithGroup(int i) {
        int i2 = this.f6mX;
        int i3 = this.f7mY;
        int i4 = 0;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.f6mX = 0;
            this.f7mY = 0;
            resetAnchors();
            resetSolverVariables(this.mSystem.getCache());
        } else {
            this.f6mX = 0;
            this.f7mY = 0;
        }
        int size = this.mChildren.size();
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i5);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer) constraintWidget).layout();
            }
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        while (i4 < i) {
            try {
                addToSolver(this.mSystem, i4);
                this.mSystem.minimize();
                updateFromSolver(this.mSystem, i4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateFromSolver(this.mSystem, -2);
            i4++;
        }
        if (this.mParent != null) {
            i = getWidth();
            i2 = getHeight();
            this.mSnapshot.applyTo(this);
            setWidth(i);
            setHeight(i2);
        } else {
            this.f6mX = i2;
            this.f7mY = i3;
        }
        if (this == getRootConstraintContainer()) {
            updateDrawPosition();
        }
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList arrayList = new ArrayList();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 1) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList arrayList = new ArrayList();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 0) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void addChain(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            while (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget == constraintWidget.mLeft && constraintWidget.mLeft.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mLeft.mTarget.mOwner;
            }
            addHorizontalChain(constraintWidget);
        } else if (i == 1) {
            while (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget == constraintWidget.mTop && constraintWidget.mTop.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mTop.mTarget.mOwner;
            }
            addVerticalChain(constraintWidget);
        }
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int i = 0;
        while (i < this.mHorizontalChainsSize) {
            if (this.mHorizontalChainsArray[i] != constraintWidget) {
                i++;
            } else {
                return;
            }
        }
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = (ConstraintWidget[]) Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = constraintWidget;
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int i = 0;
        while (i < this.mVerticalChainsSize) {
            if (this.mVerticalChainsArray[i] != constraintWidget) {
                i++;
            } else {
                return;
            }
        }
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = (ConstraintWidget[]) Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = constraintWidget;
        this.mVerticalChainsSize++;
    }

    private int countMatchConstraintsChainedWidgets(LinearSystem linearSystem, ConstraintWidget[] constraintWidgetArr, ConstraintWidget constraintWidget, int i, boolean[] zArr) {
        int i2;
        LinearSystem linearSystem2 = linearSystem;
        ConstraintWidget constraintWidget2 = constraintWidget;
        zArr[0] = USE_SNAPSHOT;
        zArr[1] = false;
        ConstraintWidget constraintWidget3 = null;
        constraintWidgetArr[0] = null;
        constraintWidgetArr[2] = null;
        constraintWidgetArr[1] = null;
        constraintWidgetArr[3] = null;
        float f = 0.0f;
        int i3 = 5;
        int i4 = 8;
        ConstraintWidget constraintWidget4;
        ConstraintWidget constraintWidget5;
        int i5;
        int i6;
        if (i == 0) {
            boolean z = (constraintWidget2.mLeft.mTarget == null || constraintWidget2.mLeft.mTarget.mOwner == this) ? USE_SNAPSHOT : false;
            constraintWidget2.mHorizontalNextWidget = null;
            i2 = 0;
            ConstraintWidget constraintWidget6 = null;
            constraintWidget4 = constraintWidget.getVisibility() != 8 ? constraintWidget2 : null;
            ConstraintWidget constraintWidget7 = constraintWidget4;
            constraintWidget5 = constraintWidget2;
            while (constraintWidget5.mRight.mTarget != null) {
                constraintWidget5.mHorizontalNextWidget = constraintWidget3;
                if (constraintWidget5.getVisibility() != 8) {
                    if (constraintWidget7 == null) {
                        constraintWidget7 = constraintWidget5;
                    }
                    if (!(constraintWidget4 == null || constraintWidget4 == constraintWidget5)) {
                        constraintWidget4.mHorizontalNextWidget = constraintWidget5;
                    }
                    constraintWidget4 = constraintWidget5;
                } else {
                    linearSystem2.addEquality(constraintWidget5.mLeft.mSolverVariable, constraintWidget5.mLeft.mTarget.mSolverVariable, 0, 5);
                    linearSystem2.addEquality(constraintWidget5.mRight.mSolverVariable, constraintWidget5.mLeft.mSolverVariable, 0, 5);
                }
                if (constraintWidget5.getVisibility() != 8 && constraintWidget5.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (constraintWidget5.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        zArr[0] = false;
                    }
                    if (constraintWidget5.mDimensionRatio <= f) {
                        zArr[0] = false;
                        i5 = i2 + 1;
                        if (i5 >= this.mMatchConstraintsChainedWidgets.length) {
                            this.mMatchConstraintsChainedWidgets = (ConstraintWidget[]) Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                        }
                        this.mMatchConstraintsChainedWidgets[i2] = constraintWidget5;
                        i2 = i5;
                    }
                }
                if (constraintWidget5.mRight.mTarget.mOwner.mLeft.mTarget == null || constraintWidget5.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != constraintWidget5 || constraintWidget5.mRight.mTarget.mOwner == constraintWidget5) {
                    break;
                }
                constraintWidget6 = constraintWidget5.mRight.mTarget.mOwner;
                constraintWidget5 = constraintWidget6;
                constraintWidget3 = null;
                f = 0.0f;
            }
            if (!(constraintWidget5.mRight.mTarget == null || constraintWidget5.mRight.mTarget.mOwner == this)) {
                z = false;
            }
            if (constraintWidget2.mLeft.mTarget == null || constraintWidget6.mRight.mTarget == null) {
                i6 = 1;
                zArr[1] = USE_SNAPSHOT;
            } else {
                i6 = 1;
            }
            constraintWidget2.mHorizontalChainFixedPosition = z;
            constraintWidget6.mHorizontalNextWidget = null;
            constraintWidgetArr[0] = constraintWidget2;
            constraintWidgetArr[2] = constraintWidget7;
            constraintWidgetArr[i6] = constraintWidget6;
            constraintWidgetArr[3] = constraintWidget4;
        } else {
            boolean z2 = (constraintWidget2.mTop.mTarget == null || constraintWidget2.mTop.mTarget.mOwner == this) ? USE_SNAPSHOT : false;
            constraintWidget3 = null;
            constraintWidget2.mVerticalNextWidget = null;
            int i7 = 0;
            constraintWidget5 = null;
            constraintWidget4 = constraintWidget.getVisibility() != 8 ? constraintWidget2 : null;
            ConstraintWidget constraintWidget8 = constraintWidget4;
            ConstraintWidget constraintWidget9 = constraintWidget2;
            while (constraintWidget9.mBottom.mTarget != null) {
                constraintWidget9.mVerticalNextWidget = constraintWidget3;
                if (constraintWidget9.getVisibility() != i4) {
                    if (constraintWidget4 == null) {
                        constraintWidget4 = constraintWidget9;
                    }
                    if (!(constraintWidget8 == null || constraintWidget8 == constraintWidget9)) {
                        constraintWidget8.mVerticalNextWidget = constraintWidget9;
                    }
                    constraintWidget8 = constraintWidget9;
                } else {
                    linearSystem2.addEquality(constraintWidget9.mTop.mSolverVariable, constraintWidget9.mTop.mTarget.mSolverVariable, 0, i3);
                    linearSystem2.addEquality(constraintWidget9.mBottom.mSolverVariable, constraintWidget9.mTop.mSolverVariable, 0, i3);
                }
                if (constraintWidget9.getVisibility() != i4 && constraintWidget9.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (constraintWidget9.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        zArr[0] = false;
                    }
                    if (constraintWidget9.mDimensionRatio <= 0.0f) {
                        zArr[0] = false;
                        i5 = i7 + 1;
                        if (i5 >= this.mMatchConstraintsChainedWidgets.length) {
                            this.mMatchConstraintsChainedWidgets = (ConstraintWidget[]) Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                        }
                        this.mMatchConstraintsChainedWidgets[i7] = constraintWidget9;
                        i7 = i5;
                    }
                }
                if (constraintWidget9.mBottom.mTarget.mOwner.mTop.mTarget == null || constraintWidget9.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != constraintWidget9 || constraintWidget9.mBottom.mTarget.mOwner == constraintWidget9) {
                    break;
                }
                constraintWidget5 = constraintWidget9.mBottom.mTarget.mOwner;
                constraintWidget9 = constraintWidget5;
                constraintWidget3 = null;
                i3 = 5;
                i4 = 8;
            }
            i2 = i7;
            if (!(constraintWidget9.mBottom.mTarget == null || constraintWidget9.mBottom.mTarget.mOwner == this)) {
                z2 = false;
            }
            if (constraintWidget2.mTop.mTarget == null || constraintWidget5.mBottom.mTarget == null) {
                i6 = 1;
                zArr[1] = USE_SNAPSHOT;
            } else {
                i6 = 1;
            }
            constraintWidget2.mVerticalChainFixedPosition = z2;
            constraintWidget5.mVerticalNextWidget = null;
            constraintWidgetArr[0] = constraintWidget2;
            constraintWidgetArr[2] = constraintWidget4;
            constraintWidgetArr[i6] = constraintWidget5;
            constraintWidgetArr[3] = constraintWidget8;
        }
        return i2;
    }
}
