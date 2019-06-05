package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintAnchor.Strength;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import java.util.ArrayList;

public class ConstraintWidget {
    public static float DEFAULT_BIAS = 0.5f;
    protected ArrayList<ConstraintAnchor> mAnchors = new ArrayList();
    ConstraintAnchor mBaseline = new ConstraintAnchor(this, Type.BASELINE);
    int mBaselineDistance = 0;
    ConstraintAnchor mBottom = new ConstraintAnchor(this, Type.BOTTOM);
    ConstraintAnchor mCenter = new ConstraintAnchor(this, Type.CENTER);
    ConstraintAnchor mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
    ConstraintAnchor mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
    private float mCircleConstraintAngle = 0.0f;
    private Object mCompanionWidget;
    private int mContainerItemSkip = 0;
    private String mDebugName = null;
    protected float mDimensionRatio = 0.0f;
    protected int mDimensionRatioSide = -1;
    private int mDrawHeight = 0;
    private int mDrawWidth = 0;
    private int mDrawX = 0;
    private int mDrawY = 0;
    int mHeight = 0;
    float mHorizontalBiasPercent = DEFAULT_BIAS;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle = 0;
    ConstraintWidget mHorizontalNextWidget = null;
    public int mHorizontalResolution = -1;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft = new ConstraintAnchor(this, Type.LEFT);
    protected ConstraintAnchor[] mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
    protected DimensionBehaviour[] mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
    protected ConstraintWidget[] mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
    protected ConstraintWidget[] mListNextVisibleWidget = new ConstraintWidget[]{null, null};
    int mMatchConstraintDefaultHeight = 0;
    int mMatchConstraintDefaultWidth = 0;
    int mMatchConstraintMaxHeight = 0;
    int mMatchConstraintMaxWidth = 0;
    int mMatchConstraintMinHeight = 0;
    int mMatchConstraintMinWidth = 0;
    float mMatchConstraintPercentHeight = 1.0f;
    float mMatchConstraintPercentWidth = 1.0f;
    private int[] mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
    protected int mMinHeight;
    protected int mMinWidth;
    protected int mOffsetX = 0;
    protected int mOffsetY = 0;
    ConstraintWidget mParent = null;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio = 1.0f;
    int mResolvedDimensionRatioSide = -1;
    int[] mResolvedMatchConstraintDefault = new int[2];
    ConstraintAnchor mRight = new ConstraintAnchor(this, Type.RIGHT);
    ConstraintAnchor mTop = new ConstraintAnchor(this, Type.TOP);
    private String mType = null;
    float mVerticalBiasPercent = DEFAULT_BIAS;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle = 0;
    ConstraintWidget mVerticalNextWidget = null;
    public int mVerticalResolution = -1;
    boolean mVerticalWrapVisited;
    private int mVisibility = 0;
    float[] mWeight = new float[]{-1.0f, -1.0f};
    int mWidth = 0;
    private int mWrapHeight;
    private int mWrapWidth;
    /* renamed from: mX */
    protected int f4mX = 0;
    /* renamed from: mY */
    protected int f5mY = 0;

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public void resolve() {
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
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
        this.f4mX = 0;
        this.f5mY = 0;
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
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
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

    public void resetResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }

    public void updateResolutionNodes() {
        for (int i = 0; i < 6; i++) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }

    public void analyze(int i) {
        Optimizer.analyze(i, this);
    }

    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }

    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }

    public ConstraintWidget() {
        addAnchors();
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
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

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void setWidthWrapContent(boolean z) {
        this.mIsWidthWrapContent = z;
    }

    public void setHeightWrapContent(boolean z) {
        this.mIsHeightWrapContent = z;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        immediateConnect(Type.CENTER, constraintWidget, Type.CENTER, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        StringBuilder stringBuilder;
        String stringBuilder2;
        StringBuilder stringBuilder3 = new StringBuilder();
        if (this.mType != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("type: ");
            stringBuilder.append(this.mType);
            stringBuilder.append(" ");
            stringBuilder2 = stringBuilder.toString();
        } else {
            stringBuilder2 = "";
        }
        stringBuilder3.append(stringBuilder2);
        if (this.mDebugName != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("id: ");
            stringBuilder.append(this.mDebugName);
            stringBuilder.append(" ");
            stringBuilder2 = stringBuilder.toString();
        } else {
            stringBuilder2 = "";
        }
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append("(");
        stringBuilder3.append(this.f4mX);
        stringBuilder3.append(", ");
        stringBuilder3.append(this.f5mY);
        stringBuilder3.append(") - (");
        stringBuilder3.append(this.mWidth);
        stringBuilder3.append(" x ");
        stringBuilder3.append(this.mHeight);
        stringBuilder3.append(") wrap: (");
        stringBuilder3.append(this.mWrapWidth);
        stringBuilder3.append(" x ");
        stringBuilder3.append(this.mWrapHeight);
        stringBuilder3.append(")");
        return stringBuilder3.toString();
    }

    public int getX() {
        return this.f4mX;
    }

    public int getY() {
        return this.f5mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    /* Access modifiers changed, original: protected */
    public int getRootX() {
        return this.f4mX + this.mOffsetX;
    }

    /* Access modifiers changed, original: protected */
    public int getRootY() {
        return this.f5mY + this.mOffsetY;
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int i) {
        this.f4mX = i;
    }

    public void setY(int i) {
        this.f5mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.f4mX = i;
        this.f5mY = i2;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void updateDrawPosition() {
        int i = this.f4mX;
        int i2 = this.f5mY;
        int i3 = this.f4mX + this.mWidth;
        int i4 = this.f5mY + this.mHeight;
        this.mDrawX = i;
        this.mDrawY = i2;
        this.mDrawWidth = i3 - i;
        this.mDrawHeight = i4 - i2;
    }

    public void setWidth(int i) {
        this.mWidth = i;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0089  */
    public void setDimensionRatio(java.lang.String r9) {
        /*
        r8 = this;
        r0 = 0;
        if (r9 == 0) goto L_0x008e;
    L_0x0003:
        r1 = r9.length();
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        goto L_0x008e;
    L_0x000b:
        r1 = -1;
        r2 = r9.length();
        r3 = 44;
        r3 = r9.indexOf(r3);
        r4 = 0;
        r5 = 1;
        if (r3 <= 0) goto L_0x0037;
    L_0x001a:
        r6 = r2 + -1;
        if (r3 >= r6) goto L_0x0037;
    L_0x001e:
        r6 = r9.substring(r4, r3);
        r7 = "W";
        r7 = r6.equalsIgnoreCase(r7);
        if (r7 == 0) goto L_0x002c;
    L_0x002a:
        r1 = 0;
        goto L_0x0035;
    L_0x002c:
        r4 = "H";
        r4 = r6.equalsIgnoreCase(r4);
        if (r4 == 0) goto L_0x0035;
    L_0x0034:
        r1 = 1;
    L_0x0035:
        r4 = r3 + 1;
    L_0x0037:
        r3 = 58;
        r3 = r9.indexOf(r3);
        if (r3 < 0) goto L_0x0075;
    L_0x003f:
        r2 = r2 - r5;
        if (r3 >= r2) goto L_0x0075;
    L_0x0042:
        r2 = r9.substring(r4, r3);
        r3 = r3 + r5;
        r9 = r9.substring(r3);
        r3 = r2.length();
        if (r3 <= 0) goto L_0x0084;
    L_0x0051:
        r3 = r9.length();
        if (r3 <= 0) goto L_0x0084;
    L_0x0057:
        r2 = java.lang.Float.parseFloat(r2);	 Catch:{ NumberFormatException -> 0x0084 }
        r9 = java.lang.Float.parseFloat(r9);	 Catch:{ NumberFormatException -> 0x0084 }
        r3 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r3 <= 0) goto L_0x0084;
    L_0x0063:
        r3 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r3 <= 0) goto L_0x0084;
    L_0x0067:
        if (r1 != r5) goto L_0x006f;
    L_0x0069:
        r9 = r9 / r2;
        r9 = java.lang.Math.abs(r9);	 Catch:{ NumberFormatException -> 0x0084 }
        goto L_0x0085;
    L_0x006f:
        r2 = r2 / r9;
        r9 = java.lang.Math.abs(r2);	 Catch:{ NumberFormatException -> 0x0084 }
        goto L_0x0085;
    L_0x0075:
        r9 = r9.substring(r4);
        r2 = r9.length();
        if (r2 <= 0) goto L_0x0084;
    L_0x007f:
        r9 = java.lang.Float.parseFloat(r9);	 Catch:{ NumberFormatException -> 0x0084 }
        goto L_0x0085;
    L_0x0084:
        r9 = 0;
    L_0x0085:
        r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x008d;
    L_0x0089:
        r8.mDimensionRatio = r9;
        r8.mDimensionRatioSide = r1;
    L_0x008d:
        return;
    L_0x008e:
        r8.mDimensionRatio = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.setDimensionRatio(java.lang.String):void");
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setWrapWidth(int i) {
        this.mWrapWidth = i;
    }

    public void setWrapHeight(int i) {
        this.mWrapHeight = i;
    }

    public void setFrame(int i, int i2, int i3, int i4) {
        i3 -= i;
        i4 -= i2;
        this.f4mX = i;
        this.f5mY = i2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i3 < this.mWidth) {
            i3 = this.mWidth;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i4 < this.mHeight) {
            i4 = this.mHeight;
        }
        this.mWidth = i3;
        this.mHeight = i4;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setHorizontalDimension(int i, int i2) {
        this.f4mX = i;
        this.mWidth = i2 - i;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.f5mY = i;
        this.mHeight = i2 - i;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(Type type, ConstraintWidget constraintWidget, Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, Strength.STRONG, 0, true);
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                ((ConstraintAnchor) this.mAnchors.get(i)).reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(Type type) {
        switch (type) {
            case LEFT:
                return this.mLeft;
            case TOP:
                return this.mTop;
            case RIGHT:
                return this.mRight;
            case BOTTOM:
                return this.mBottom;
            case BASELINE:
                return this.mBaseline;
            case CENTER:
                return this.mCenter;
            case CENTER_X:
                return this.mCenterX;
            case CENTER_Y:
                return this.mCenterY;
            case NONE:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setWidth(this.mWrapWidth);
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setHeight(this.mWrapHeight);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:184:0x034e  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0343  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x035e  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x0354  */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x03be  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x0395  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x03c8  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02da  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02d9 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02d9 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02da  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02da  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02d9 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0245  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02d9 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x02da  */
    /* JADX WARNING: Missing block: B:132:0x0232, code skipped:
            if (r15.mResolvedDimensionRatioSide == -1) goto L_0x0236;
     */
    public void addToSolver(android.support.constraint.solver.LinearSystem r39) {
        /*
        r38 = this;
        r15 = r38;
        r14 = r39;
        r0 = r15.mLeft;
        r21 = r14.createObjectVariable(r0);
        r0 = r15.mRight;
        r10 = r14.createObjectVariable(r0);
        r0 = r15.mTop;
        r6 = r14.createObjectVariable(r0);
        r0 = r15.mBottom;
        r4 = r14.createObjectVariable(r0);
        r0 = r15.mBaseline;
        r3 = r14.createObjectVariable(r0);
        r0 = r15.mParent;
        r1 = 8;
        r2 = 1;
        r13 = 0;
        if (r0 == 0) goto L_0x0124;
    L_0x002a:
        r0 = r15.mParent;
        if (r0 == 0) goto L_0x003a;
    L_0x002e:
        r0 = r15.mParent;
        r0 = r0.mListDimensionBehaviors;
        r0 = r0[r13];
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r0 != r5) goto L_0x003a;
    L_0x0038:
        r0 = 1;
        goto L_0x003b;
    L_0x003a:
        r0 = 0;
    L_0x003b:
        r5 = r15.mParent;
        if (r5 == 0) goto L_0x004b;
    L_0x003f:
        r5 = r15.mParent;
        r5 = r5.mListDimensionBehaviors;
        r5 = r5[r2];
        r7 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r5 != r7) goto L_0x004b;
    L_0x0049:
        r5 = 1;
        goto L_0x004c;
    L_0x004b:
        r5 = 0;
    L_0x004c:
        r7 = r15.mLeft;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x0073;
    L_0x0052:
        r7 = r15.mLeft;
        r7 = r7.mTarget;
        r7 = r7.mTarget;
        r8 = r15.mLeft;
        if (r7 == r8) goto L_0x0073;
    L_0x005c:
        r7 = r15.mRight;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x0073;
    L_0x0062:
        r7 = r15.mRight;
        r7 = r7.mTarget;
        r7 = r7.mTarget;
        r8 = r15.mRight;
        if (r7 != r8) goto L_0x0073;
    L_0x006c:
        r7 = r15.mParent;
        r7 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r7;
        r7.addChain(r15, r13);
    L_0x0073:
        r7 = r15.mLeft;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x0083;
    L_0x0079:
        r7 = r15.mLeft;
        r7 = r7.mTarget;
        r7 = r7.mTarget;
        r8 = r15.mLeft;
        if (r7 == r8) goto L_0x0093;
    L_0x0083:
        r7 = r15.mRight;
        r7 = r7.mTarget;
        if (r7 == 0) goto L_0x0095;
    L_0x0089:
        r7 = r15.mRight;
        r7 = r7.mTarget;
        r7 = r7.mTarget;
        r8 = r15.mRight;
        if (r7 != r8) goto L_0x0095;
    L_0x0093:
        r7 = 1;
        goto L_0x0096;
    L_0x0095:
        r7 = 0;
    L_0x0096:
        r8 = r15.mTop;
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00bd;
    L_0x009c:
        r8 = r15.mTop;
        r8 = r8.mTarget;
        r8 = r8.mTarget;
        r9 = r15.mTop;
        if (r8 == r9) goto L_0x00bd;
    L_0x00a6:
        r8 = r15.mBottom;
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00bd;
    L_0x00ac:
        r8 = r15.mBottom;
        r8 = r8.mTarget;
        r8 = r8.mTarget;
        r9 = r15.mBottom;
        if (r8 != r9) goto L_0x00bd;
    L_0x00b6:
        r8 = r15.mParent;
        r8 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r8;
        r8.addChain(r15, r2);
    L_0x00bd:
        r8 = r15.mTop;
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00cd;
    L_0x00c3:
        r8 = r15.mTop;
        r8 = r8.mTarget;
        r8 = r8.mTarget;
        r9 = r15.mTop;
        if (r8 == r9) goto L_0x00dd;
    L_0x00cd:
        r8 = r15.mBottom;
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00df;
    L_0x00d3:
        r8 = r15.mBottom;
        r8 = r8.mTarget;
        r8 = r8.mTarget;
        r9 = r15.mBottom;
        if (r8 != r9) goto L_0x00df;
    L_0x00dd:
        r8 = 1;
        goto L_0x00e0;
    L_0x00df:
        r8 = 0;
    L_0x00e0:
        if (r0 == 0) goto L_0x00fd;
    L_0x00e2:
        r9 = r15.mVisibility;
        if (r9 == r1) goto L_0x00fd;
    L_0x00e6:
        r9 = r15.mLeft;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x00fd;
    L_0x00ec:
        r9 = r15.mRight;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x00fd;
    L_0x00f2:
        r9 = r15.mParent;
        r9 = r9.mRight;
        r9 = r14.createObjectVariable(r9);
        r14.addGreaterThan(r9, r10, r13, r2);
    L_0x00fd:
        if (r5 == 0) goto L_0x011e;
    L_0x00ff:
        r9 = r15.mVisibility;
        if (r9 == r1) goto L_0x011e;
    L_0x0103:
        r9 = r15.mTop;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x011e;
    L_0x0109:
        r9 = r15.mBottom;
        r9 = r9.mTarget;
        if (r9 != 0) goto L_0x011e;
    L_0x010f:
        r9 = r15.mBaseline;
        if (r9 != 0) goto L_0x011e;
    L_0x0113:
        r9 = r15.mParent;
        r9 = r9.mBottom;
        r9 = r14.createObjectVariable(r9);
        r14.addGreaterThan(r9, r4, r13, r2);
    L_0x011e:
        r12 = r5;
        r16 = r7;
        r22 = r8;
        goto L_0x012a;
    L_0x0124:
        r0 = 0;
        r12 = 0;
        r16 = 0;
        r22 = 0;
    L_0x012a:
        r5 = r15.mWidth;
        r7 = r15.mMinWidth;
        if (r5 >= r7) goto L_0x0132;
    L_0x0130:
        r5 = r15.mMinWidth;
    L_0x0132:
        r7 = r15.mHeight;
        r8 = r15.mMinHeight;
        if (r7 >= r8) goto L_0x013a;
    L_0x0138:
        r7 = r15.mMinHeight;
    L_0x013a:
        r8 = r15.mListDimensionBehaviors;
        r8 = r8[r13];
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r8 == r9) goto L_0x0144;
    L_0x0142:
        r8 = 1;
        goto L_0x0145;
    L_0x0144:
        r8 = 0;
    L_0x0145:
        r9 = r15.mListDimensionBehaviors;
        r9 = r9[r2];
        r11 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r9 == r11) goto L_0x014f;
    L_0x014d:
        r9 = 1;
        goto L_0x0150;
    L_0x014f:
        r9 = 0;
    L_0x0150:
        r11 = r15.mDimensionRatioSide;
        r15.mResolvedDimensionRatioSide = r11;
        r11 = r15.mDimensionRatio;
        r15.mResolvedDimensionRatio = r11;
        r11 = r15.mMatchConstraintDefaultWidth;
        r2 = r15.mMatchConstraintDefaultHeight;
        r13 = r15.mDimensionRatio;
        r17 = 0;
        r18 = 4;
        r13 = (r13 > r17 ? 1 : (r13 == r17 ? 0 : -1));
        if (r13 <= 0) goto L_0x0213;
    L_0x0166:
        r13 = r15.mVisibility;
        r1 = 8;
        if (r13 == r1) goto L_0x0213;
    L_0x016c:
        r1 = r15.mListDimensionBehaviors;
        r13 = 0;
        r1 = r1[r13];
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        r27 = r3;
        r3 = 3;
        if (r1 != r13) goto L_0x017b;
    L_0x0178:
        if (r11 != 0) goto L_0x017b;
    L_0x017a:
        r11 = 3;
    L_0x017b:
        r1 = r15.mListDimensionBehaviors;
        r13 = 1;
        r1 = r1[r13];
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r13) goto L_0x0187;
    L_0x0184:
        if (r2 != 0) goto L_0x0187;
    L_0x0186:
        r2 = 3;
    L_0x0187:
        r1 = r15.mListDimensionBehaviors;
        r13 = 0;
        r1 = r1[r13];
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r13) goto L_0x01a2;
    L_0x0190:
        r1 = r15.mListDimensionBehaviors;
        r13 = 1;
        r1 = r1[r13];
        r13 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r13) goto L_0x01a2;
    L_0x0199:
        if (r11 != r3) goto L_0x01a2;
    L_0x019b:
        if (r2 != r3) goto L_0x01a2;
    L_0x019d:
        r15.setupDimensionRatio(r0, r12, r8, r9);
        goto L_0x0208;
    L_0x01a2:
        r1 = r15.mListDimensionBehaviors;
        r8 = 0;
        r1 = r1[r8];
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r9) goto L_0x01ce;
    L_0x01ab:
        if (r11 != r3) goto L_0x01ce;
    L_0x01ad:
        r15.mResolvedDimensionRatioSide = r8;
        r1 = r15.mResolvedDimensionRatio;
        r3 = r15.mHeight;
        r3 = (float) r3;
        r1 = r1 * r3;
        r1 = (int) r1;
        r3 = r15.mListDimensionBehaviors;
        r8 = 1;
        r3 = r3[r8];
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r3 == r5) goto L_0x01c9;
    L_0x01c0:
        r29 = r1;
        r25 = r2;
        r30 = r7;
        r20 = 4;
        goto L_0x021d;
    L_0x01c9:
        r29 = r1;
        r25 = r2;
        goto L_0x020c;
    L_0x01ce:
        r8 = 1;
        r1 = r15.mListDimensionBehaviors;
        r1 = r1[r8];
        r9 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r1 != r9) goto L_0x0208;
    L_0x01d7:
        if (r2 != r3) goto L_0x0208;
    L_0x01d9:
        r15.mResolvedDimensionRatioSide = r8;
        r1 = r15.mDimensionRatioSide;
        r3 = -1;
        if (r1 != r3) goto L_0x01e7;
    L_0x01e0:
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r15.mResolvedDimensionRatio;
        r1 = r1 / r3;
        r15.mResolvedDimensionRatio = r1;
    L_0x01e7:
        r1 = r15.mResolvedDimensionRatio;
        r3 = r15.mWidth;
        r3 = (float) r3;
        r1 = r1 * r3;
        r1 = (int) r1;
        r3 = r15.mListDimensionBehaviors;
        r7 = 0;
        r3 = r3[r7];
        r7 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r3 == r7) goto L_0x0201;
    L_0x01f8:
        r30 = r1;
        r29 = r5;
        r20 = r11;
        r25 = 4;
        goto L_0x021d;
    L_0x0201:
        r30 = r1;
        r25 = r2;
        r29 = r5;
        goto L_0x020e;
    L_0x0208:
        r25 = r2;
        r29 = r5;
    L_0x020c:
        r30 = r7;
    L_0x020e:
        r20 = r11;
        r28 = 1;
        goto L_0x021f;
    L_0x0213:
        r27 = r3;
        r25 = r2;
        r29 = r5;
        r30 = r7;
        r20 = r11;
    L_0x021d:
        r28 = 0;
    L_0x021f:
        r1 = r15.mResolvedMatchConstraintDefault;
        r2 = 0;
        r1[r2] = r20;
        r1 = r15.mResolvedMatchConstraintDefault;
        r2 = 1;
        r1[r2] = r25;
        if (r28 == 0) goto L_0x0239;
    L_0x022b:
        r1 = r15.mResolvedDimensionRatioSide;
        if (r1 == 0) goto L_0x0235;
    L_0x022f:
        r1 = r15.mResolvedDimensionRatioSide;
        r2 = -1;
        if (r1 != r2) goto L_0x023a;
    L_0x0234:
        goto L_0x0236;
    L_0x0235:
        r2 = -1;
    L_0x0236:
        r26 = 1;
        goto L_0x023c;
    L_0x0239:
        r2 = -1;
    L_0x023a:
        r26 = 0;
    L_0x023c:
        r1 = r15.mListDimensionBehaviors;
        r3 = 0;
        r1 = r1[r3];
        r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r1 != r3) goto L_0x024c;
    L_0x0245:
        r1 = r15 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer;
        if (r1 == 0) goto L_0x024c;
    L_0x0249:
        r31 = 1;
        goto L_0x024e;
    L_0x024c:
        r31 = 0;
    L_0x024e:
        r1 = r15.mCenter;
        r1 = r1.isConnected();
        r3 = 1;
        r23 = r1 ^ 1;
        r1 = r15.mHorizontalResolution;
        r13 = 2;
        r32 = 0;
        if (r1 == r13) goto L_0x02c8;
    L_0x025e:
        r1 = r15.mParent;
        if (r1 == 0) goto L_0x026d;
    L_0x0262:
        r1 = r15.mParent;
        r1 = r1.mRight;
        r1 = r14.createObjectVariable(r1);
        r33 = r1;
        goto L_0x026f;
    L_0x026d:
        r33 = r32;
    L_0x026f:
        r1 = r15.mParent;
        if (r1 == 0) goto L_0x027e;
    L_0x0273:
        r1 = r15.mParent;
        r1 = r1.mLeft;
        r1 = r14.createObjectVariable(r1);
        r34 = r1;
        goto L_0x0280;
    L_0x027e:
        r34 = r32;
    L_0x0280:
        r1 = r15.mListDimensionBehaviors;
        r17 = 0;
        r5 = r1[r17];
        r7 = r15.mLeft;
        r8 = r15.mRight;
        r9 = r15.f4mX;
        r11 = r15.mMinWidth;
        r1 = r15.mMaxDimension;
        r1 = r1[r17];
        r24 = r12;
        r12 = r1;
        r1 = r15.mHorizontalBiasPercent;
        r13 = r1;
        r1 = r15.mMatchConstraintMinWidth;
        r17 = r1;
        r1 = r15.mMatchConstraintMaxWidth;
        r18 = r1;
        r1 = r15.mMatchConstraintPercentWidth;
        r19 = r1;
        r35 = r0;
        r0 = r38;
        r1 = r39;
        r2 = r35;
        r36 = r27;
        r3 = r34;
        r27 = r4;
        r4 = r33;
        r37 = r6;
        r6 = r31;
        r31 = r10;
        r10 = r29;
        r14 = r26;
        r15 = r16;
        r16 = r20;
        r20 = r23;
        r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        goto L_0x02d2;
    L_0x02c8:
        r37 = r6;
        r31 = r10;
        r24 = r12;
        r36 = r27;
        r27 = r4;
    L_0x02d2:
        r15 = r38;
        r0 = r15.mVerticalResolution;
        r1 = 2;
        if (r0 != r1) goto L_0x02da;
    L_0x02d9:
        return;
    L_0x02da:
        r0 = r15.mListDimensionBehaviors;
        r14 = 1;
        r0 = r0[r14];
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r0 != r1) goto L_0x02e9;
    L_0x02e3:
        r0 = r15 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer;
        if (r0 == 0) goto L_0x02e9;
    L_0x02e7:
        r6 = 1;
        goto L_0x02ea;
    L_0x02e9:
        r6 = 0;
    L_0x02ea:
        if (r28 == 0) goto L_0x02f8;
    L_0x02ec:
        r0 = r15.mResolvedDimensionRatioSide;
        if (r0 == r14) goto L_0x02f5;
    L_0x02f0:
        r0 = r15.mResolvedDimensionRatioSide;
        r1 = -1;
        if (r0 != r1) goto L_0x02f8;
    L_0x02f5:
        r16 = 1;
        goto L_0x02fa;
    L_0x02f8:
        r16 = 0;
    L_0x02fa:
        r0 = r15.mBaselineDistance;
        if (r0 <= 0) goto L_0x0339;
    L_0x02fe:
        r0 = r15.mBaseline;
        r0 = r0.getResolutionNode();
        r0 = r0.state;
        if (r0 != r14) goto L_0x0316;
    L_0x0308:
        r0 = r15.mBaseline;
        r0 = r0.getResolutionNode();
        r10 = r39;
        r0.addResolvedValue(r10);
        r4 = r37;
        goto L_0x033d;
    L_0x0316:
        r10 = r39;
        r0 = r38.getBaselineDistance();
        r1 = 6;
        r2 = r36;
        r4 = r37;
        r10.addEquality(r2, r4, r0, r1);
        r0 = r15.mBaseline;
        r0 = r0.mTarget;
        if (r0 == 0) goto L_0x033d;
    L_0x032a:
        r0 = r15.mBaseline;
        r0 = r0.mTarget;
        r0 = r10.createObjectVariable(r0);
        r3 = 0;
        r10.addEquality(r2, r0, r3, r1);
        r20 = 0;
        goto L_0x033f;
    L_0x0339:
        r4 = r37;
        r10 = r39;
    L_0x033d:
        r20 = r23;
    L_0x033f:
        r0 = r15.mParent;
        if (r0 == 0) goto L_0x034e;
    L_0x0343:
        r0 = r15.mParent;
        r0 = r0.mBottom;
        r0 = r10.createObjectVariable(r0);
        r23 = r0;
        goto L_0x0350;
    L_0x034e:
        r23 = r32;
    L_0x0350:
        r0 = r15.mParent;
        if (r0 == 0) goto L_0x035e;
    L_0x0354:
        r0 = r15.mParent;
        r0 = r0.mTop;
        r0 = r10.createObjectVariable(r0);
        r3 = r0;
        goto L_0x0360;
    L_0x035e:
        r3 = r32;
    L_0x0360:
        r0 = r15.mListDimensionBehaviors;
        r5 = r0[r14];
        r7 = r15.mTop;
        r8 = r15.mBottom;
        r9 = r15.f5mY;
        r11 = r15.mMinHeight;
        r0 = r15.mMaxDimension;
        r12 = r0[r14];
        r13 = r15.mVerticalBiasPercent;
        r0 = r15.mMatchConstraintMinHeight;
        r17 = r0;
        r0 = r15.mMatchConstraintMaxHeight;
        r18 = r0;
        r0 = r15.mMatchConstraintPercentHeight;
        r19 = r0;
        r0 = r38;
        r1 = r39;
        r2 = r24;
        r24 = r4;
        r4 = r23;
        r10 = r30;
        r14 = r16;
        r15 = r22;
        r16 = r25;
        r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);
        if (r28 == 0) goto L_0x03be;
    L_0x0395:
        r6 = 6;
        r7 = r38;
        r0 = r7.mResolvedDimensionRatioSide;
        r1 = 1;
        if (r0 != r1) goto L_0x03ae;
    L_0x039d:
        r5 = r7.mResolvedDimensionRatio;
        r6 = 6;
        r0 = r39;
        r1 = r27;
        r2 = r24;
        r3 = r31;
        r4 = r21;
        r0.addRatio(r1, r2, r3, r4, r5, r6);
        goto L_0x03c0;
    L_0x03ae:
        r5 = r7.mResolvedDimensionRatio;
        r0 = r39;
        r1 = r31;
        r2 = r21;
        r3 = r27;
        r4 = r24;
        r0.addRatio(r1, r2, r3, r4, r5, r6);
        goto L_0x03c0;
    L_0x03be:
        r7 = r38;
    L_0x03c0:
        r0 = r7.mCenter;
        r0 = r0.isConnected();
        if (r0 == 0) goto L_0x03e8;
    L_0x03c8:
        r0 = r7.mCenter;
        r0 = r0.getTarget();
        r0 = r0.getOwner();
        r1 = r7.mCircleConstraintAngle;
        r2 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r1 = r1 + r2;
        r1 = (double) r1;
        r1 = java.lang.Math.toRadians(r1);
        r1 = (float) r1;
        r2 = r7.mCenter;
        r2 = r2.getMargin();
        r3 = r39;
        r3.addCenterPoint(r7, r0, r1, r2);
    L_0x03e8:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.addToSolver(android.support.constraint.solver.LinearSystem):void");
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && !(this.mLeft.isConnected() && this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && !(this.mTop.isConnected() && this.mBottom.isConnected() && this.mLeft.isConnected() && this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z && !z2) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z && z2) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && z && z2) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:98:0x01de  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02c4  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x02aa  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x02e1  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02cc  */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x02eb  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x02e6  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x02ee  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00dc  */
    private void applyConstraints(android.support.constraint.solver.LinearSystem r31, boolean r32, android.support.constraint.solver.SolverVariable r33, android.support.constraint.solver.SolverVariable r34, android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour r35, boolean r36, android.support.constraint.solver.widgets.ConstraintAnchor r37, android.support.constraint.solver.widgets.ConstraintAnchor r38, int r39, int r40, int r41, int r42, float r43, boolean r44, boolean r45, int r46, int r47, int r48, float r49, boolean r50) {
        /*
        r30 = this;
        r0 = r30;
        r10 = r31;
        r11 = r33;
        r12 = r34;
        r1 = r41;
        r2 = r42;
        r13 = r37;
        r9 = r10.createObjectVariable(r13);
        r8 = r38;
        r7 = r10.createObjectVariable(r8);
        r6 = r37.getTarget();
        r6 = r10.createObjectVariable(r6);
        r14 = r38.getTarget();
        r14 = r10.createObjectVariable(r14);
        r8 = r10.graphOptimizer;
        r15 = 1;
        if (r8 == 0) goto L_0x0066;
    L_0x002e:
        r8 = r37.getResolutionNode();
        r8 = r8.state;
        r13 = 1;
        if (r8 != r13) goto L_0x0066;
    L_0x0037:
        r8 = r38.getResolutionNode();
        r8 = r8.state;
        if (r8 != r13) goto L_0x0066;
    L_0x003f:
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        if (r1 == 0) goto L_0x004e;
    L_0x0045:
        r1 = android.support.constraint.solver.LinearSystem.getMetrics();
        r2 = r1.resolvedWidgets;
        r2 = r2 + r15;
        r1.resolvedWidgets = r2;
    L_0x004e:
        r1 = r37.getResolutionNode();
        r1.addResolvedValue(r10);
        r1 = r38.getResolutionNode();
        r1.addResolvedValue(r10);
        if (r45 != 0) goto L_0x0065;
    L_0x005e:
        if (r32 == 0) goto L_0x0065;
    L_0x0060:
        r1 = 0;
        r2 = 6;
        r10.addGreaterThan(r12, r7, r1, r2);
    L_0x0065:
        return;
    L_0x0066:
        r8 = android.support.constraint.solver.LinearSystem.getMetrics();
        if (r8 == 0) goto L_0x0078;
    L_0x006c:
        r8 = android.support.constraint.solver.LinearSystem.getMetrics();
        r20 = r14;
        r13 = r8.nonresolvedWidgets;
        r13 = r13 + r15;
        r8.nonresolvedWidgets = r13;
        goto L_0x007a;
    L_0x0078:
        r20 = r14;
    L_0x007a:
        r8 = r37.isConnected();
        r13 = r38.isConnected();
        r14 = r0.mCenter;
        r21 = r14.isConnected();
        if (r8 == 0) goto L_0x008c;
    L_0x008a:
        r14 = 1;
        goto L_0x008d;
    L_0x008c:
        r14 = 0;
    L_0x008d:
        if (r13 == 0) goto L_0x0091;
    L_0x008f:
        r14 = r14 + 1;
    L_0x0091:
        if (r21 == 0) goto L_0x0095;
    L_0x0093:
        r14 = r14 + 1;
    L_0x0095:
        if (r44 == 0) goto L_0x0099;
    L_0x0097:
        r11 = 3;
        goto L_0x009b;
    L_0x0099:
        r11 = r46;
    L_0x009b:
        r15 = android.support.constraint.solver.widgets.ConstraintWidget.C00221.f3x27577131;
        r16 = r35.ordinal();
        r15 = r15[r16];
        r12 = 4;
        switch(r15) {
            case 1: goto L_0x00a7;
            case 2: goto L_0x00a7;
            case 3: goto L_0x00a7;
            case 4: goto L_0x00a9;
            default: goto L_0x00a7;
        };
    L_0x00a7:
        r15 = 0;
        goto L_0x00ad;
    L_0x00a9:
        if (r11 != r12) goto L_0x00ac;
    L_0x00ab:
        goto L_0x00a7;
    L_0x00ac:
        r15 = 1;
    L_0x00ad:
        r12 = r0.mVisibility;
        r22 = r14;
        r14 = 8;
        if (r12 != r14) goto L_0x00b8;
    L_0x00b5:
        r12 = 0;
        r15 = 0;
        goto L_0x00ba;
    L_0x00b8:
        r12 = r40;
    L_0x00ba:
        if (r50 == 0) goto L_0x00d7;
    L_0x00bc:
        if (r8 != 0) goto L_0x00c8;
    L_0x00be:
        if (r13 != 0) goto L_0x00c8;
    L_0x00c0:
        if (r21 != 0) goto L_0x00c8;
    L_0x00c2:
        r14 = r39;
        r10.addEquality(r9, r14);
        goto L_0x00d7;
    L_0x00c8:
        if (r8 == 0) goto L_0x00d7;
    L_0x00ca:
        if (r13 != 0) goto L_0x00d7;
    L_0x00cc:
        r14 = r37.getMargin();
        r23 = r13;
        r13 = 6;
        r10.addEquality(r9, r6, r14, r13);
        goto L_0x00da;
    L_0x00d7:
        r23 = r13;
        r13 = 6;
    L_0x00da:
        if (r15 != 0) goto L_0x010a;
    L_0x00dc:
        if (r36 == 0) goto L_0x00f4;
    L_0x00de:
        r13 = 3;
        r14 = 0;
        r10.addEquality(r7, r9, r14, r13);
        if (r1 <= 0) goto L_0x00ea;
    L_0x00e5:
        r14 = 6;
        r10.addGreaterThan(r7, r9, r1, r14);
        goto L_0x00eb;
    L_0x00ea:
        r14 = 6;
    L_0x00eb:
        r12 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r2 >= r12) goto L_0x00f9;
    L_0x00f0:
        r10.addLowerThan(r7, r9, r2, r14);
        goto L_0x00f9;
    L_0x00f4:
        r13 = 3;
        r14 = 6;
        r10.addEquality(r7, r9, r12, r14);
    L_0x00f9:
        r13 = r47;
        r2 = r48;
        r24 = r6;
        r27 = r11;
        r6 = r20;
        r0 = r22;
        r11 = 2;
        r20 = 4;
        goto L_0x01e7;
    L_0x010a:
        r13 = 3;
        r2 = -2;
        r14 = r47;
        if (r14 != r2) goto L_0x0114;
    L_0x0110:
        r14 = r48;
        r13 = r12;
        goto L_0x0117;
    L_0x0114:
        r13 = r14;
        r14 = r48;
    L_0x0117:
        if (r14 != r2) goto L_0x011b;
    L_0x0119:
        r2 = r12;
        goto L_0x011c;
    L_0x011b:
        r2 = r14;
    L_0x011c:
        if (r13 <= 0) goto L_0x012e;
    L_0x011e:
        if (r32 == 0) goto L_0x0125;
    L_0x0120:
        r14 = 6;
        r10.addGreaterThan(r7, r9, r13, r14);
        goto L_0x0129;
    L_0x0125:
        r14 = 6;
        r10.addGreaterThan(r7, r9, r13, r14);
    L_0x0129:
        r12 = java.lang.Math.max(r12, r13);
        goto L_0x012f;
    L_0x012e:
        r14 = 6;
    L_0x012f:
        if (r2 <= 0) goto L_0x0140;
    L_0x0131:
        if (r32 == 0) goto L_0x0139;
    L_0x0133:
        r14 = 1;
        r10.addLowerThan(r7, r9, r2, r14);
        r14 = 6;
        goto L_0x013c;
    L_0x0139:
        r10.addLowerThan(r7, r9, r2, r14);
    L_0x013c:
        r12 = java.lang.Math.min(r12, r2);
    L_0x0140:
        r14 = 1;
        if (r11 != r14) goto L_0x0159;
    L_0x0143:
        if (r32 == 0) goto L_0x014b;
    L_0x0145:
        r14 = 6;
        r10.addEquality(r7, r9, r12, r14);
        goto L_0x01c7;
    L_0x014b:
        if (r45 == 0) goto L_0x0153;
    L_0x014d:
        r14 = 4;
        r10.addEquality(r7, r9, r12, r14);
        goto L_0x01c7;
    L_0x0153:
        r14 = 1;
        r10.addEquality(r7, r9, r12, r14);
        goto L_0x01c7;
    L_0x0159:
        r14 = 2;
        if (r11 != r14) goto L_0x01c7;
    L_0x015c:
        r14 = r37.getType();
        r24 = r6;
        r6 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        if (r14 == r6) goto L_0x018e;
    L_0x0166:
        r6 = r37.getType();
        r14 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        if (r6 != r14) goto L_0x016f;
    L_0x016e:
        goto L_0x018e;
    L_0x016f:
        r6 = r0.mParent;
        r14 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT;
        r6 = r6.getAnchor(r14);
        r6 = r10.createObjectVariable(r6);
        r14 = r0.mParent;
        r25 = r6;
        r6 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT;
        r6 = r14.getAnchor(r6);
        r6 = r10.createObjectVariable(r6);
        r17 = r6;
        r18 = r25;
        goto L_0x01ac;
    L_0x018e:
        r6 = r0.mParent;
        r14 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP;
        r6 = r6.getAnchor(r14);
        r6 = r10.createObjectVariable(r6);
        r14 = r0.mParent;
        r26 = r6;
        r6 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM;
        r6 = r14.getAnchor(r6);
        r6 = r10.createObjectVariable(r6);
        r17 = r6;
        r18 = r26;
    L_0x01ac:
        r14 = r31.createRow();
        r27 = r11;
        r6 = r20;
        r0 = r22;
        r11 = 2;
        r20 = 4;
        r15 = r7;
        r16 = r9;
        r19 = r49;
        r14 = r14.createRowDimensionRatio(r15, r16, r17, r18, r19);
        r10.addConstraint(r14);
        r15 = 0;
        goto L_0x01d2;
    L_0x01c7:
        r24 = r6;
        r27 = r11;
        r6 = r20;
        r0 = r22;
        r11 = 2;
        r20 = 4;
    L_0x01d2:
        if (r15 == 0) goto L_0x01e7;
    L_0x01d4:
        if (r0 == r11) goto L_0x01e7;
    L_0x01d6:
        if (r44 != 0) goto L_0x01e7;
    L_0x01d8:
        r12 = java.lang.Math.max(r13, r12);
        if (r2 <= 0) goto L_0x01e2;
    L_0x01de:
        r12 = java.lang.Math.min(r2, r12);
    L_0x01e2:
        r14 = 6;
        r10.addEquality(r7, r9, r12, r14);
        r15 = 0;
    L_0x01e7:
        if (r50 == 0) goto L_0x02f2;
    L_0x01e9:
        if (r45 == 0) goto L_0x01ed;
    L_0x01eb:
        goto L_0x02f2;
    L_0x01ed:
        r0 = 5;
        if (r8 != 0) goto L_0x0204;
    L_0x01f0:
        if (r23 != 0) goto L_0x0204;
    L_0x01f2:
        if (r21 != 0) goto L_0x0204;
    L_0x01f4:
        if (r32 == 0) goto L_0x01fd;
    L_0x01f6:
        r4 = 0;
        r12 = r34;
        r10.addGreaterThan(r12, r7, r4, r0);
        goto L_0x01ff;
    L_0x01fd:
        r12 = r34;
    L_0x01ff:
        r2 = r7;
        r0 = 0;
        r1 = 6;
        goto L_0x02ec;
    L_0x0204:
        r4 = 0;
        r5 = 4;
        r12 = r34;
        if (r8 == 0) goto L_0x0212;
    L_0x020a:
        if (r23 != 0) goto L_0x0212;
    L_0x020c:
        if (r32 == 0) goto L_0x01ff;
    L_0x020e:
        r10.addGreaterThan(r12, r7, r4, r0);
        goto L_0x01ff;
    L_0x0212:
        if (r8 != 0) goto L_0x0227;
    L_0x0214:
        if (r23 == 0) goto L_0x0227;
    L_0x0216:
        r1 = r38.getMargin();
        r1 = -r1;
        r2 = 6;
        r10.addEquality(r7, r6, r1, r2);
        if (r32 == 0) goto L_0x01ff;
    L_0x0221:
        r14 = r33;
        r10.addGreaterThan(r9, r14, r4, r0);
        goto L_0x01ff;
    L_0x0227:
        r11 = 3;
        r14 = r33;
        if (r8 == 0) goto L_0x01ff;
    L_0x022c:
        if (r23 == 0) goto L_0x01ff;
    L_0x022e:
        if (r15 == 0) goto L_0x0291;
    L_0x0230:
        if (r32 == 0) goto L_0x0238;
    L_0x0232:
        if (r1 != 0) goto L_0x0238;
    L_0x0234:
        r1 = 6;
        r10.addGreaterThan(r7, r9, r4, r1);
    L_0x0238:
        if (r27 != 0) goto L_0x0260;
    L_0x023a:
        if (r2 > 0) goto L_0x0242;
    L_0x023c:
        if (r13 <= 0) goto L_0x023f;
    L_0x023e:
        goto L_0x0242;
    L_0x023f:
        r1 = 0;
        r5 = 6;
        goto L_0x0243;
    L_0x0242:
        r1 = 1;
    L_0x0243:
        r3 = r37.getMargin();
        r8 = r24;
        r10.addEquality(r9, r8, r3, r5);
        r3 = r38.getMargin();
        r3 = -r3;
        r10.addEquality(r7, r6, r3, r5);
        if (r2 > 0) goto L_0x025b;
    L_0x0256:
        if (r13 <= 0) goto L_0x0259;
    L_0x0258:
        goto L_0x025b;
    L_0x0259:
        r13 = 0;
        goto L_0x025c;
    L_0x025b:
        r13 = 1;
    L_0x025c:
        r15 = r1;
        r11 = r30;
        goto L_0x02a8;
    L_0x0260:
        r8 = r24;
        r1 = r27;
        r13 = 1;
        if (r1 != r13) goto L_0x026c;
    L_0x0267:
        r0 = 6;
        r11 = r30;
    L_0x026a:
        r15 = 1;
        goto L_0x02a8;
    L_0x026c:
        if (r1 != r11) goto L_0x028d;
    L_0x026e:
        if (r44 != 0) goto L_0x027b;
    L_0x0270:
        r11 = r30;
        r1 = r11.mResolvedDimensionRatioSide;
        r3 = -1;
        if (r1 == r3) goto L_0x027d;
    L_0x0277:
        if (r2 > 0) goto L_0x027d;
    L_0x0279:
        r5 = 6;
        goto L_0x027d;
    L_0x027b:
        r11 = r30;
    L_0x027d:
        r1 = r37.getMargin();
        r10.addEquality(r9, r8, r1, r5);
        r1 = r38.getMargin();
        r1 = -r1;
        r10.addEquality(r7, r6, r1, r5);
        goto L_0x026a;
    L_0x028d:
        r11 = r30;
        r13 = 0;
        goto L_0x02a7;
    L_0x0291:
        r8 = r24;
        r11 = r30;
        r13 = 1;
        if (r32 == 0) goto L_0x02a7;
    L_0x0298:
        r1 = r37.getMargin();
        r10.addGreaterThan(r9, r8, r1, r0);
        r1 = r38.getMargin();
        r1 = -r1;
        r10.addLowerThan(r7, r6, r1, r0);
    L_0x02a7:
        r15 = 0;
    L_0x02a8:
        if (r13 == 0) goto L_0x02c4;
    L_0x02aa:
        r4 = r37.getMargin();
        r13 = r38.getMargin();
        r1 = r31;
        r2 = r9;
        r3 = r8;
        r5 = r43;
        r28 = r6;
        r29 = r7;
        r11 = r8;
        r8 = r13;
        r13 = r9;
        r9 = r0;
        r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9);
        goto L_0x02ca;
    L_0x02c4:
        r28 = r6;
        r29 = r7;
        r11 = r8;
        r13 = r9;
    L_0x02ca:
        if (r15 == 0) goto L_0x02e1;
    L_0x02cc:
        r0 = r37.getMargin();
        r1 = 6;
        r10.addGreaterThan(r13, r11, r0, r1);
        r0 = r38.getMargin();
        r0 = -r0;
        r3 = r28;
        r2 = r29;
        r10.addLowerThan(r2, r3, r0, r1);
        goto L_0x02e4;
    L_0x02e1:
        r2 = r29;
        r1 = 6;
    L_0x02e4:
        if (r32 == 0) goto L_0x02eb;
    L_0x02e6:
        r0 = 0;
        r10.addGreaterThan(r13, r14, r0, r1);
        goto L_0x02ec;
    L_0x02eb:
        r0 = 0;
    L_0x02ec:
        if (r32 == 0) goto L_0x02f1;
    L_0x02ee:
        r10.addGreaterThan(r12, r2, r0, r1);
    L_0x02f1:
        return;
    L_0x02f2:
        r3 = r0;
        r2 = r7;
        r13 = r9;
        r0 = 0;
        r1 = 6;
        r12 = r34;
        r14 = r33;
        if (r3 >= r11) goto L_0x0305;
    L_0x02fd:
        if (r32 == 0) goto L_0x0305;
    L_0x02ff:
        r10.addGreaterThan(r13, r14, r0, r1);
        r10.addGreaterThan(r12, r2, r0, r1);
    L_0x0305:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.applyConstraints(android.support.constraint.solver.LinearSystem, boolean, android.support.constraint.solver.SolverVariable, android.support.constraint.solver.SolverVariable, android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, android.support.constraint.solver.widgets.ConstraintAnchor, android.support.constraint.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, int, int, int, float, boolean):void");
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue4 = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }
}
