// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.io.PrintStream;
import java.util.Arrays;
import android.support.constraint.solver.LinearSystem;

public class ConstraintWidgetContainer extends WidgetContainer
{
    int mDebugSolverPassCount;
    private boolean mHeightMeasuredTooSmall;
    ChainHead[] mHorizontalChainsArray;
    int mHorizontalChainsSize;
    private boolean mIsRtl;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    ChainHead[] mVerticalChainsArray;
    int mVerticalChainsSize;
    private boolean mWidthMeasuredTooSmall;
    
    public ConstraintWidgetContainer() {
        this.mIsRtl = false;
        this.mSystem = new LinearSystem();
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mVerticalChainsArray = new ChainHead[4];
        this.mHorizontalChainsArray = new ChainHead[4];
        this.mOptimizationLevel = 3;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        this.mDebugSolverPassCount = 0;
    }
    
    private void addHorizontalChain(final ConstraintWidget constraintWidget) {
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, this.isRtl());
        ++this.mHorizontalChainsSize;
    }
    
    private void addVerticalChain(final ConstraintWidget constraintWidget) {
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, this.isRtl());
        ++this.mVerticalChainsSize;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    void addChain(final ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            this.addHorizontalChain(constraintWidget);
        }
        else if (n == 1) {
            this.addVerticalChain(constraintWidget);
        }
    }
    
    public boolean addChildrenToSolver(final LinearSystem linearSystem) {
        this.addToSolver(linearSystem);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour horizontalDimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
                final DimensionBehaviour verticalDimensionBehaviour = constraintWidget.mListDimensionBehaviors[1];
                if (horizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (verticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem);
                if (horizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(horizontalDimensionBehaviour);
                }
                if (verticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(verticalDimensionBehaviour);
                }
            }
            else {
                Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                constraintWidget.addToSolver(linearSystem);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
        return true;
    }
    
    @Override
    public void analyze(final int n) {
        super.analyze(n);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).analyze(n);
        }
    }
    
    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean isRtl() {
        return this.mIsRtl;
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
        if (this.mOptimizationLevel != 0) {
            if (!this.optimizeFor(8)) {
                this.optimizeReset();
            }
            this.optimize();
            this.mSystem.graphOptimizer = true;
        }
        else {
            this.mSystem.graphOptimizer = false;
        }
        final DimensionBehaviour dimensionBehaviour = this.mListDimensionBehaviors[1];
        final DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
        this.resetChains();
        final int size = this.mChildren.size();
        ConstraintWidget obj = null;
        for (int i = 0; i < size; ++i) {
            obj = this.mChildren.get(i);
            if (obj instanceof WidgetContainer) {
                ((WidgetContainer)obj).layout();
            }
        }
        int n = 0;
        int j = 1;
        int n2 = 0;
        while (j != 0) {
            final int n3 = n + 1;
            Label_0394: {
                try {
                    this.mSystem.reset();
                    this.createObjectVariables(this.mSystem);
                    for (int k = 0; k < size; ++k) {
                        this.mChildren.get(k).createObjectVariables(this.mSystem);
                    }
                    final boolean b = (j = (this.addChildrenToSolver(this.mSystem) ? 1 : 0)) != 0;
                    if (!b) {
                        break Label_0394;
                    }
                    try {
                        this.mSystem.minimize();
                        j = (b ? 1 : 0);
                    }
                    catch (Exception obj) {
                        j = (b ? 1 : 0);
                    }
                }
                catch (Exception ex) {}
                ((Exception)obj).printStackTrace();
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("EXCEPTION : ");
                sb.append(obj);
                out.println(sb.toString());
            }
            if (j != 0) {
                this.updateChildrenFromSolver(this.mSystem, Optimizer.flags);
            }
            else {
                this.updateFromSolver(this.mSystem);
                for (int l = 0; l < size; ++l) {
                    obj = this.mChildren.get(l);
                    if (obj.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && obj.getWidth() < obj.getWrapWidth()) {
                        Optimizer.flags[2] = true;
                        break;
                    }
                    if (obj.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && obj.getHeight() < obj.getWrapHeight()) {
                        Optimizer.flags[2] = true;
                        break;
                    }
                }
            }
            boolean b2;
            if (n3 < 8 && Optimizer.flags[2]) {
                int index = 0;
                int max3 = 0;
                int max4 = 0;
                while (index < size) {
                    obj = this.mChildren.get(index);
                    max3 = Math.max(max3, obj.mX + obj.getWidth());
                    max4 = Math.max(max4, obj.mY + obj.getHeight());
                    ++index;
                }
                final int max5 = Math.max(this.mMinWidth, max3);
                final int max6 = Math.max(this.mMinHeight, max4);
                if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT && this.getWidth() < max5) {
                    this.setWidth(max5);
                    this.mListDimensionBehaviors[0] = DimensionBehaviour.WRAP_CONTENT;
                    b2 = true;
                    n2 = 1;
                }
                else {
                    b2 = false;
                }
                if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && this.getHeight() < max6) {
                    this.setHeight(max6);
                    this.mListDimensionBehaviors[1] = DimensionBehaviour.WRAP_CONTENT;
                    b2 = true;
                    n2 = 1;
                }
            }
            else {
                b2 = false;
            }
            final int max7 = Math.max(this.mMinWidth, this.getWidth());
            if (max7 > this.getWidth()) {
                this.setWidth(max7);
                this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                b2 = true;
                n2 = 1;
            }
            final int max8 = Math.max(this.mMinHeight, this.getHeight());
            if (max8 > this.getHeight()) {
                this.setHeight(max8);
                this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                b2 = true;
                n2 = 1;
            }
            boolean b3 = b2;
            Label_0983: {
                int n4;
                if ((n4 = n2) == 0) {
                    boolean b4 = b2;
                    int n5 = n2;
                    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT) {
                        b4 = b2;
                        n5 = n2;
                        if (max > 0) {
                            b4 = b2;
                            n5 = n2;
                            if (this.getWidth() > max) {
                                this.mWidthMeasuredTooSmall = true;
                                this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                                this.setWidth(max);
                                b4 = true;
                                n5 = 1;
                            }
                        }
                    }
                    b3 = b4;
                    n4 = n5;
                    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT) {
                        b3 = b4;
                        n4 = n5;
                        if (max2 > 0) {
                            b3 = b4;
                            n4 = n5;
                            if (this.getHeight() > max2) {
                                this.mHeightMeasuredTooSmall = true;
                                this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                                this.setHeight(max2);
                                j = 1;
                                n2 = 1;
                                break Label_0983;
                            }
                        }
                    }
                }
                j = (b3 ? 1 : 0);
                n2 = n4;
            }
            n = n3;
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
        if (n2 != 0) {
            this.mListDimensionBehaviors[0] = dimensionBehaviour2;
            this.mListDimensionBehaviors[1] = dimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    public void optimize() {
        if (!this.optimizeFor(8)) {
            this.analyze(this.mOptimizationLevel);
        }
        this.solveGraph();
    }
    
    public boolean optimizeFor(final int n) {
        return (this.mOptimizationLevel & n) == n;
    }
    
    public void optimizeForDimensions(final int n, final int n2) {
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null) {
            this.mResolutionWidth.resolve(n);
        }
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
            this.mResolutionHeight.resolve(n2);
        }
    }
    
    public void optimizeReset() {
        final int size = this.mChildren.size();
        this.resetResolutionNodes();
        for (int i = 0; i < size; ++i) {
            this.mChildren.get(i).resetResolutionNodes();
        }
    }
    
    public void preOptimize() {
        this.optimizeReset();
        this.analyze(this.mOptimizationLevel);
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
    
    public void setRtl(final boolean mIsRtl) {
        this.mIsRtl = mIsRtl;
    }
    
    public void solveGraph() {
        final ResolutionAnchor resolutionNode = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        final ResolutionAnchor resolutionNode2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        resolutionNode.resolve(null, 0.0f);
        resolutionNode2.resolve(null, 0.0f);
    }
    
    public void updateChildrenFromSolver(final LinearSystem linearSystem, final boolean[] array) {
        array[2] = false;
        this.updateFromSolver(linearSystem);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem);
            if (constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                array[2] = true;
            }
            if (constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                array[2] = true;
            }
        }
    }
}
