// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint;

import android.content.res.TypedArray;
import android.util.SparseIntArray;
import android.annotation.TargetApi;
import android.util.Log;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.support.constraint.solver.widgets.Guideline;
import android.os.Build$VERSION;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.content.res.Resources$NotFoundException;
import android.util.AttributeSet;
import android.content.Context;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import java.util.HashMap;
import java.util.ArrayList;
import android.view.View;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ConstraintLayout extends ViewGroup
{
    SparseArray<View> mChildrenByIds;
    private ArrayList<ConstraintHelper> mConstraintHelpers;
    private ConstraintSet mConstraintSet;
    private int mConstraintSetId;
    private HashMap<String, Integer> mDesignIds;
    private boolean mDirtyHierarchy;
    private int mLastMeasureHeight;
    int mLastMeasureHeightMode;
    int mLastMeasureHeightSize;
    private int mLastMeasureWidth;
    int mLastMeasureWidthMode;
    int mLastMeasureWidthSize;
    ConstraintWidgetContainer mLayoutWidget;
    private int mMaxHeight;
    private int mMaxWidth;
    private Metrics mMetrics;
    private int mMinHeight;
    private int mMinWidth;
    private int mOptimizationLevel;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    
    public ConstraintLayout(final Context context) {
        super(context);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 3;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(null);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 3;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(set);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 3;
        this.mConstraintSet = null;
        this.mConstraintSetId = -1;
        this.mDesignIds = new HashMap<String, Integer>();
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
        this.init(set);
    }
    
    private final ConstraintWidget getTargetWidget(final int n) {
        if (n == 0) {
            return this.mLayoutWidget;
        }
        final View view = (View)this.mChildrenByIds.get(n);
        if (view == this) {
            return this.mLayoutWidget;
        }
        ConstraintWidget widget;
        if (view == null) {
            widget = null;
        }
        else {
            widget = ((LayoutParams)view.getLayoutParams()).widget;
        }
        return widget;
    }
    
    private void init(AttributeSet obtainStyledAttributes) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (obtainStyledAttributes != null) {
            obtainStyledAttributes = (AttributeSet)this.getContext().obtainStyledAttributes(obtainStyledAttributes, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = ((TypedArray)obtainStyledAttributes).getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMinWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMinHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMaxWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.mMaxHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = ((TypedArray)obtainStyledAttributes).getInt(index, this.mOptimizationLevel);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    final int resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(index, 0);
                    try {
                        (this.mConstraintSet = new ConstraintSet()).load(this.getContext(), resourceId);
                    }
                    catch (Resources$NotFoundException ex) {
                        this.mConstraintSet = null;
                    }
                    this.mConstraintSetId = resourceId;
                }
            }
            ((TypedArray)obtainStyledAttributes).recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }
    
    private void internalMeasureChildren(final int n, final int n2) {
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        final int n4 = this.getPaddingLeft() + this.getPaddingRight();
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final ConstraintWidget widget = layoutParams.widget;
                if (!layoutParams.isGuideline) {
                    if (!layoutParams.isHelper) {
                        widget.setVisibility(child.getVisibility());
                        final int width = layoutParams.width;
                        final int height = layoutParams.height;
                        boolean b = false;
                        Label_0198: {
                            Label_0195: {
                                if (!layoutParams.horizontalDimensionFixed && !layoutParams.verticalDimensionFixed && (layoutParams.horizontalDimensionFixed || layoutParams.matchConstraintDefaultWidth != 1) && layoutParams.width != -1) {
                                    if (!layoutParams.verticalDimensionFixed) {
                                        if (layoutParams.matchConstraintDefaultHeight == 1) {
                                            break Label_0195;
                                        }
                                        if (layoutParams.height == -1) {
                                            break Label_0195;
                                        }
                                    }
                                    b = false;
                                    break Label_0198;
                                }
                            }
                            b = true;
                        }
                        boolean b2;
                        boolean b3;
                        int measuredWidth;
                        int measuredHeight;
                        if (b) {
                            int n5;
                            if (width == 0) {
                                n5 = getChildMeasureSpec(n, n4, -2);
                                b2 = true;
                            }
                            else if (width == -1) {
                                n5 = getChildMeasureSpec(n, n4, -1);
                                b2 = false;
                            }
                            else {
                                b2 = (width == -2);
                                n5 = getChildMeasureSpec(n, n4, width);
                            }
                            int n6;
                            if (height == 0) {
                                n6 = getChildMeasureSpec(n2, n3, -2);
                                b3 = true;
                            }
                            else if (height == -1) {
                                n6 = getChildMeasureSpec(n2, n3, -1);
                                b3 = false;
                            }
                            else {
                                b3 = (height == -2);
                                n6 = getChildMeasureSpec(n2, n3, height);
                            }
                            child.measure(n5, n6);
                            if (this.mMetrics != null) {
                                final Metrics mMetrics = this.mMetrics;
                                ++mMetrics.measures;
                            }
                            widget.setWidthWrapContent(width == -2);
                            widget.setHeightWrapContent(height == -2);
                            measuredWidth = child.getMeasuredWidth();
                            measuredHeight = child.getMeasuredHeight();
                        }
                        else {
                            b2 = false;
                            b3 = false;
                            measuredHeight = height;
                            measuredWidth = width;
                        }
                        widget.setWidth(measuredWidth);
                        widget.setHeight(measuredHeight);
                        if (b2) {
                            widget.setWrapWidth(measuredWidth);
                        }
                        if (b3) {
                            widget.setWrapHeight(measuredHeight);
                        }
                        if (layoutParams.needsBaseline) {
                            final int baseline = child.getBaseline();
                            if (baseline != -1) {
                                widget.setBaselineDistance(baseline);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void internalMeasureDimensions(final int n, final int n2) {
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        final int n4 = this.getPaddingLeft() + this.getPaddingRight();
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                final ConstraintWidget widget = layoutParams.widget;
                if (!layoutParams.isGuideline) {
                    if (!layoutParams.isHelper) {
                        widget.setVisibility(child.getVisibility());
                        final int width = layoutParams.width;
                        final int height = layoutParams.height;
                        if (width != 0 && height != 0) {
                            final boolean b = width == -2;
                            final int childMeasureSpec = getChildMeasureSpec(n, n4, width);
                            final boolean b2 = height == -2;
                            child.measure(childMeasureSpec, getChildMeasureSpec(n2, n3, height));
                            if (this.mMetrics != null) {
                                final Metrics mMetrics = this.mMetrics;
                                ++mMetrics.measures;
                            }
                            widget.setWidthWrapContent(width == -2);
                            widget.setHeightWrapContent(height == -2);
                            final int measuredWidth = child.getMeasuredWidth();
                            final int measuredHeight = child.getMeasuredHeight();
                            widget.setWidth(measuredWidth);
                            widget.setHeight(measuredHeight);
                            if (b) {
                                widget.setWrapWidth(measuredWidth);
                            }
                            if (b2) {
                                widget.setWrapHeight(measuredHeight);
                            }
                            if (layoutParams.needsBaseline) {
                                final int baseline = child.getBaseline();
                                if (baseline != -1) {
                                    widget.setBaselineDistance(baseline);
                                }
                            }
                            if (layoutParams.horizontalDimensionFixed && layoutParams.verticalDimensionFixed) {
                                widget.getResolutionWidth().resolve(measuredWidth);
                                widget.getResolutionHeight().resolve(measuredHeight);
                            }
                        }
                        else {
                            widget.getResolutionWidth().invalidate();
                            widget.getResolutionHeight().invalidate();
                        }
                    }
                }
            }
        }
        this.mLayoutWidget.solveGraph();
        for (int j = 0; j < childCount; ++j) {
            final View child2 = this.getChildAt(j);
            if (child2.getVisibility() != 8) {
                final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                final ConstraintWidget widget2 = layoutParams2.widget;
                if (!layoutParams2.isGuideline) {
                    if (!layoutParams2.isHelper) {
                        widget2.setVisibility(child2.getVisibility());
                        int width2 = layoutParams2.width;
                        int height2 = layoutParams2.height;
                        if (width2 == 0 || height2 == 0) {
                            final ResolutionAnchor resolutionNode = widget2.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                            final ResolutionAnchor resolutionNode2 = widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                            final boolean b3 = widget2.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && widget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null;
                            final ResolutionAnchor resolutionNode3 = widget2.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                            final ResolutionAnchor resolutionNode4 = widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                            final boolean b4 = widget2.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && widget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null;
                            if (width2 != 0 || height2 != 0 || !b3 || !b4) {
                                final boolean b5 = this.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                                int n5;
                                if (this.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                                    n5 = 1;
                                }
                                else {
                                    n5 = 0;
                                }
                                if (!b5) {
                                    widget2.getResolutionWidth().invalidate();
                                }
                                if (n5 == 0) {
                                    widget2.getResolutionHeight().invalidate();
                                }
                                int n6 = 0;
                                boolean b6 = false;
                                boolean b7 = false;
                                int n7 = 0;
                                Label_0900: {
                                    if (width2 == 0) {
                                        if (!b5 || !widget2.isSpreadWidth() || !b3 || !resolutionNode.isResolved() || !resolutionNode2.isResolved()) {
                                            n6 = getChildMeasureSpec(n, n4, -2);
                                            b6 = false;
                                            b7 = true;
                                            n7 = width2;
                                            break Label_0900;
                                        }
                                        width2 = (int)(resolutionNode2.getResolvedValue() - resolutionNode.getResolvedValue());
                                        widget2.getResolutionWidth().resolve(width2);
                                        n6 = getChildMeasureSpec(n, n4, width2);
                                    }
                                    else {
                                        if (width2 != -1) {
                                            b7 = (width2 == -2);
                                            n6 = getChildMeasureSpec(n, n4, width2);
                                            n7 = width2;
                                            b6 = b5;
                                            break Label_0900;
                                        }
                                        n6 = getChildMeasureSpec(n, n4, -1);
                                    }
                                    b7 = false;
                                    b6 = b5;
                                    n7 = width2;
                                }
                                int n8 = 0;
                                boolean b8 = false;
                                Label_1043: {
                                    if (height2 == 0) {
                                        if (n5 == 0 || !widget2.isSpreadHeight() || !b4 || !resolutionNode3.isResolved() || !resolutionNode4.isResolved()) {
                                            n8 = getChildMeasureSpec(n2, n3, -2);
                                            n5 = 0;
                                            b8 = true;
                                            break Label_1043;
                                        }
                                        height2 = (int)(resolutionNode4.getResolvedValue() - resolutionNode3.getResolvedValue());
                                        widget2.getResolutionHeight().resolve(height2);
                                        n8 = getChildMeasureSpec(n2, n3, height2);
                                    }
                                    else {
                                        final int n9 = n3;
                                        if (height2 != -1) {
                                            b8 = (height2 == -2);
                                            n8 = getChildMeasureSpec(n2, n9, height2);
                                            break Label_1043;
                                        }
                                        n8 = getChildMeasureSpec(n2, n9, -1);
                                    }
                                    b8 = false;
                                }
                                child2.measure(n6, n8);
                                if (this.mMetrics != null) {
                                    final Metrics mMetrics2 = this.mMetrics;
                                    ++mMetrics2.measures;
                                }
                                widget2.setWidthWrapContent(n7 == -2);
                                widget2.setHeightWrapContent(height2 == -2);
                                final int measuredWidth2 = child2.getMeasuredWidth();
                                final int measuredHeight2 = child2.getMeasuredHeight();
                                widget2.setWidth(measuredWidth2);
                                widget2.setHeight(measuredHeight2);
                                if (b7) {
                                    widget2.setWrapWidth(measuredWidth2);
                                }
                                if (b8) {
                                    widget2.setWrapHeight(measuredHeight2);
                                }
                                if (b6) {
                                    widget2.getResolutionWidth().resolve(measuredWidth2);
                                }
                                else {
                                    widget2.getResolutionWidth().remove();
                                }
                                if (n5 != 0) {
                                    widget2.getResolutionHeight().resolve(measuredHeight2);
                                }
                                else {
                                    widget2.getResolutionHeight().remove();
                                }
                                if (layoutParams2.needsBaseline) {
                                    final int baseline2 = child2.getBaseline();
                                    if (baseline2 != -1) {
                                        widget2.setBaselineDistance(baseline2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void setChildrenConstraints() {
        final boolean inEditMode = this.isInEditMode();
        final int childCount = this.getChildCount();
        int guideEnd = 0;
        Label_0112: {
            if (!inEditMode) {
                break Label_0112;
            }
            int n = 0;
        Label_0106_Outer:
            while (true) {
                if (n >= childCount) {
                    break Label_0112;
                }
                final View child = this.getChildAt(n);
                String resourceName;
                int index;
                String substring;
                View child2;
                int n2;
                View child3;
                ConstraintWidget viewWidget;
                int resolvedLeftToLeft;
                ConstraintWidget targetWidget;
                int n3;
                Guideline guideline;
                float guidePercent;
                int n4;
                LayoutParams layoutParams;
                ConstraintWidget targetWidget2;
                int rightToLeft;
                int rightToRight;
                int leftToRight;
                int guideBegin;
                int size;
                int resolvedLeftToRight;
                int n5;
                int resolveGoneRightMargin;
                float horizontalBiasPercent;
                ConstraintWidget targetWidget3;
                ConstraintWidget targetWidget4;
                int leftToLeft;
                View child4;
                ConstraintWidget targetWidget5;
                ConstraintWidget viewWidget2;
                int goneRightMargin;
                ConstraintWidget targetWidget6;
                ConstraintWidget targetWidget7;
                View view;
                LayoutParams layoutParams2;
                ConstraintWidget targetWidget8;
                ConstraintWidget targetWidget9;
                String resourceName2;
                int n6;
                ConstraintWidget targetWidget10;
                Block_17_Outer:Label_0163_Outer:Block_50_Outer:Label_0329_Outer:Label_1836_Outer:
                while (true) {
                    try {
                        resourceName = this.getResources().getResourceName(child.getId());
                        this.setDesignInformation(0, resourceName, child.getId());
                        index = resourceName.indexOf(47);
                        substring = resourceName;
                        if (index != -1) {
                            substring = resourceName.substring(index + 1);
                        }
                        this.getTargetWidget(child.getId()).setDebugName(substring);
                        ++n;
                        continue Label_0106_Outer;
                        // iftrue(Label_0209:, child2.getId() != this.mConstraintSetId || !child2 instanceof Constraints)
                        // iftrue(Label_0363:, viewWidget != null)
                        // iftrue(Label_1185:, targetWidget == null)
                        // iftrue(Label_1421:, targetWidget2 == null)
                        // iftrue(Label_1087:, layoutParams.endToEnd == -1)
                        // iftrue(Label_1087:, n = rightToRight != -1)
                        // iftrue(Label_1472:, layoutParams.bottomToTop == -1)
                        // iftrue(Label_0215:, this.mConstraintSetId == -1)
                        // iftrue(Label_0283:, size <= 0)
                        // iftrue(Label_1098:, Build$VERSION.SDK_INT >= 17)
                        // iftrue(Label_0152:, n >= childCount)
                        // iftrue(Label_1006:, layoutParams.startToEnd == -1)
                        // iftrue(Label_0283:, n >= size)
                        // iftrue(Label_0317:, !child4 instanceof Placeholder)
                        // iftrue(Label_0396:, !layoutParams.helped)
                        // iftrue(Label_0827:, layoutParams.leftToLeft != -1 || layoutParams.leftToRight != -1 || layoutParams.rightToLeft != -1 || layoutParams.rightToRight != -1 || layoutParams.startToStart != -1 || layoutParams.startToEnd != -1 || layoutParams.endToStart != -1 || layoutParams.endToEnd != -1 || layoutParams.topToTop != -1 || layoutParams.topToBottom != -1 || layoutParams.bottomToTop != -1 || layoutParams.bottomToBottom != -1 || layoutParams.baselineToBaseline != -1 || layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1 || layoutParams.circleConstraint != -1 || layoutParams.width == -1)
                        // iftrue(Label_2042:, guideEnd == -1)
                        // iftrue(Label_1818:, layoutParams.horizontalDimensionFixed)
                        // iftrue(Label_1672:, horizontalBiasPercent < 0.0f || horizontalBiasPercent == 0.5f)
                        // iftrue(Label_0639:, guideBegin == -1)
                        // iftrue(Label_1704:, targetWidget5 == null)
                        // iftrue(Label_0661:, !layoutParams.isGuideline)
                        // iftrue(Label_1243:, targetWidget3 == null)
                        // iftrue(Label_1006:, leftToLeft != -1)
                        // iftrue(Label_1421:, layoutParams.topToBottom == -1)
                        // iftrue(Label_1061:, layoutParams.endToStart == -1)
                        // iftrue(Label_1704:, layoutParams.verticalBias < 0.0f || layoutParams.verticalBias == 0.5f)
                        // iftrue(Label_1741:, !inEditMode || layoutParams.editorAbsoluteX == -1 && layoutParams.editorAbsoluteY == -1)
                        // iftrue(Label_1520:, targetWidget7 == null)
                        // iftrue(Label_1283:, guideEnd == -1)
                        // iftrue(Label_1192:, resolvedLeftToLeft == -1)
                        // iftrue(Label_1322:, n3 == -1)
                        // iftrue(Label_1952:, layoutParams.dimensionRatio == null)
                        // iftrue(Label_0141:, viewWidget2 != null)
                        // iftrue(Label_0230:, this.mConstraintSet == null)
                        // iftrue(Label_0491:, !layoutParams.isInPlaceholder)
                        // iftrue(Label_1520:, layoutParams.bottomToBottom == -1)
                        // iftrue(Label_0323:, n >= childCount)
                        // iftrue(Label_2042:, layoutParams.height != -1)
                        // iftrue(Label_0215:, n >= childCount)
                        // iftrue(Label_2052:, n2 >= childCount)
                        // iftrue(Label_1243:, resolvedLeftToRight == -1)
                        // iftrue(Label_1006:, n = leftToRight != -1)
                        // iftrue(Label_0466:, !inEditMode)
                        // iftrue(Label_0533:, layoutParams.verticalDimensionFixed && layoutParams.horizontalDimensionFixed)
                        // iftrue(Label_1322:, targetWidget6 == null)
                        // iftrue(Label_1896:, layoutParams.height != -1)
                        // iftrue(Label_0619:, guidePercent == -1.0f)
                        // iftrue(Label_1649:, layoutParams.baselineToBaseline == -1)
                        // iftrue(Label_1649:, targetWidget8 == null || view == null || !view.getLayoutParams() instanceof LayoutParams)
                        // iftrue(Label_1143:, layoutParams.circleConstraint == -1)
                        // iftrue(Label_1373:, layoutParams.topToTop == -1)
                        // iftrue(Label_0596:, Build$VERSION.SDK_INT >= 17)
                        // iftrue(Label_1421:, targetWidget4 == null)
                        // iftrue(Label_0980:, layoutParams.startToStart == -1)
                        // iftrue(Label_1087:, rightToLeft != -1)
                        // iftrue(Label_1913:, layoutParams.verticalDimensionFixed)
                        // iftrue(Label_1520:, targetWidget10 == null)
                        // iftrue(Label_1801:, layoutParams.width != -1)
                        while (true) {
                            Label_1836:Label_1520_Outer:
                            while (true) {
                            Label_1520:
                                while (true) {
                                    Block_70: {
                                        while (true) {
                                        Label_0254:
                                            while (true) {
                                                Label_0329:Label_0286_Outer:Block_84_Outer:
                                                while (true) {
                                                    Label_2042: {
                                                        while (true) {
                                                        Label_0286:
                                                            while (true) {
                                                            Label_0317:
                                                                while (true) {
                                                                    Label_1006: {
                                                                        Label_0163:Block_25_Outer:Block_64_Outer:
                                                                        while (true) {
                                                                        Block_67_Outer:
                                                                            while (true) {
                                                                                Label_0533_Outer:Block_24_Outer:
                                                                                while (true) {
                                                                                    Block_66: {
                                                                                    Label_1952:
                                                                                        while (true) {
                                                                                            Block_23:Label_0466_Outer:
                                                                                            while (true) {
                                                                                            Label_0466:
                                                                                                while (true) {
                                                                                                Block_54_Outer:
                                                                                                    while (true) {
                                                                                                        Label_1322: {
                                                                                                        Block_69:
                                                                                                            while (true) {
                                                                                                                Block_13: {
                                                                                                                Label_1931:
                                                                                                                    while (true) {
                                                                                                                        Block_27: {
                                                                                                                            Block_85: {
                                                                                                                                Block_19: {
                                                                                                                                    while (true) {
                                                                                                                                    Label_1649_Outer:
                                                                                                                                        while (true) {
                                                                                                                                            Label_0596: {
                                                                                                                                                while (true) {
                                                                                                                                                Label_1704:
                                                                                                                                                    while (true) {
                                                                                                                                                        Block_55: {
                                                                                                                                                            while (true) {
                                                                                                                                                                Block_62: {
                                                                                                                                                                Label_0491:
                                                                                                                                                                    while (true) {
                                                                                                                                                                        Block_57: {
                                                                                                                                                                        Label_1421:
                                                                                                                                                                            while (true) {
                                                                                                                                                                                Block_65: {
                                                                                                                                                                                Block_46:
                                                                                                                                                                                    while (true) {
                                                                                                                                                                                        while (true) {
                                                                                                                                                                                        Label_0827_Outer:
                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                child2 = this.getChildAt(n);
                                                                                                                                                                                            Block_16:
                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                    Block_44: {
                                                                                                                                                                                                    Block_7_Outer:
                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                        Block_6: {
                                                                                                                                                                                                                        Label_0230_Outer:
                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                            Block_59_Outer:
                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                    Label_1243: {
                                                                                                                                                                                                                                        Block_68: {
                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                            Label_1672:
                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                    Block_51: {
                                                                                                                                                                                                                                                        Block_45: {
                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                            Label_0146:
                                                                                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                                                                        Label_1741: {
                                                                                                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                                                                                                Block_49: {
                                                                                                                                                                                                                                                                                    Block_11: {
                                                                                                                                                                                                                                                                                        break Block_11;
                                                                                                                                                                                                                                                                                        child3 = this.getChildAt(n2);
                                                                                                                                                                                                                                                                                        viewWidget = this.getViewWidget(child3);
                                                                                                                                                                                                                                                                                        break Label_0163;
                                                                                                                                                                                                                                                                                        targetWidget = this.getTargetWidget(resolvedLeftToLeft);
                                                                                                                                                                                                                                                                                        break Block_57;
                                                                                                                                                                                                                                                                                        while (true) {
                                                                                                                                                                                                                                                                                            n = 0;
                                                                                                                                                                                                                                                                                            break Label_0163;
                                                                                                                                                                                                                                                                                            n3 = n;
                                                                                                                                                                                                                                                                                            break Label_1243;
                                                                                                                                                                                                                                                                                            guideline.setGuidePercent(guidePercent);
                                                                                                                                                                                                                                                                                            n4 = n;
                                                                                                                                                                                                                                                                                            break Label_2042;
                                                                                                                                                                                                                                                                                            targetWidget2 = this.getTargetWidget(layoutParams.topToTop);
                                                                                                                                                                                                                                                                                            break Block_65;
                                                                                                                                                                                                                                                                                            Label_1061: {
                                                                                                                                                                                                                                                                                                guideEnd = rightToLeft;
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            n = rightToRight;
                                                                                                                                                                                                                                                                                            break Label_0286;
                                                                                                                                                                                                                                                                                            guideEnd = rightToLeft;
                                                                                                                                                                                                                                                                                            break Block_51;
                                                                                                                                                                                                                                                                                            guideEnd = layoutParams.startToStart;
                                                                                                                                                                                                                                                                                            n = leftToRight;
                                                                                                                                                                                                                                                                                            break Label_1006;
                                                                                                                                                                                                                                                                                            break Block_68;
                                                                                                                                                                                                                                                                                            Label_2052:
                                                                                                                                                                                                                                                                                            return;
                                                                                                                                                                                                                                                                                            Label_0152:
                                                                                                                                                                                                                                                                                            continue Label_0163_Outer;
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        guideline.setGuideBegin(guideBegin);
                                                                                                                                                                                                                                                                                        n4 = n;
                                                                                                                                                                                                                                                                                        break Label_2042;
                                                                                                                                                                                                                                                                                        this.mLayoutWidget.removeAllChildren();
                                                                                                                                                                                                                                                                                        size = this.mConstraintHelpers.size();
                                                                                                                                                                                                                                                                                        break Block_13;
                                                                                                                                                                                                                                                                                        resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
                                                                                                                                                                                                                                                                                        resolvedLeftToRight = layoutParams.resolvedLeftToRight;
                                                                                                                                                                                                                                                                                        guideEnd = layoutParams.resolvedRightToLeft;
                                                                                                                                                                                                                                                                                        n = layoutParams.resolvedRightToRight;
                                                                                                                                                                                                                                                                                        n5 = layoutParams.resolveGoneLeftMargin;
                                                                                                                                                                                                                                                                                        resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
                                                                                                                                                                                                                                                                                        horizontalBiasPercent = layoutParams.resolvedHorizontalBias;
                                                                                                                                                                                                                                                                                        break Block_45;
                                                                                                                                                                                                                                                                                        ++n;
                                                                                                                                                                                                                                                                                        break Label_0115;
                                                                                                                                                                                                                                                                                        viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget3, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, n5);
                                                                                                                                                                                                                                                                                        n3 = n;
                                                                                                                                                                                                                                                                                        break Label_1243;
                                                                                                                                                                                                                                                                                        break Block_6;
                                                                                                                                                                                                                                                                                        viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget4, ConstraintAnchor.Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                                                                                                                                                                                                                                                                                        continue Label_1421;
                                                                                                                                                                                                                                                                                        viewWidget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
                                                                                                                                                                                                                                                                                        break Label_1741;
                                                                                                                                                                                                                                                                                        Label_0323: {
                                                                                                                                                                                                                                                                                            n2 = 0;
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        n = guideEnd;
                                                                                                                                                                                                                                                                                        break Label_0329;
                                                                                                                                                                                                                                                                                        Label_0980:
                                                                                                                                                                                                                                                                                        guideEnd = leftToLeft;
                                                                                                                                                                                                                                                                                        n = leftToRight;
                                                                                                                                                                                                                                                                                        break Block_49;
                                                                                                                                                                                                                                                                                        break Label_0329;
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    this.mConstraintSet = ((Constraints)child2).getConstraintSet();
                                                                                                                                                                                                                                                                                    break Block_67_Outer;
                                                                                                                                                                                                                                                                                    child4 = this.getChildAt(n);
                                                                                                                                                                                                                                                                                    break Block_16;
                                                                                                                                                                                                                                                                                    continue Label_0146;
                                                                                                                                                                                                                                                                                    Label_1801: {
                                                                                                                                                                                                                                                                                        viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    viewWidget.setWidth(0);
                                                                                                                                                                                                                                                                                    break Label_1836;
                                                                                                                                                                                                                                                                                    guideEnd = layoutParams.endToStart;
                                                                                                                                                                                                                                                                                    n = rightToRight;
                                                                                                                                                                                                                                                                                    break Label_0254;
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                n = layoutParams.startToEnd;
                                                                                                                                                                                                                                                                                guideEnd = leftToLeft;
                                                                                                                                                                                                                                                                                break Label_1006;
                                                                                                                                                                                                                                                                                Label_0363: {
                                                                                                                                                                                                                                                                                    layoutParams = (LayoutParams)child3.getLayoutParams();
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                layoutParams.validate();
                                                                                                                                                                                                                                                                                break Block_19;
                                                                                                                                                                                                                                                                                n = 0;
                                                                                                                                                                                                                                                                                continue Block_59_Outer;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            Label_0661: {
                                                                                                                                                                                                                                                                                break Block_44;
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            Label_0639:
                                                                                                                                                                                                                                                                            n4 = n;
                                                                                                                                                                                                                                                                            break Block_27;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        break Label_1520;
                                                                                                                                                                                                                                                                        break Label_0146;
                                                                                                                                                                                                                                                                        Label_0619: {
                                                                                                                                                                                                                                                                            continue Label_0230_Outer;
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    targetWidget5 = this.getTargetWidget(layoutParams.circleConstraint);
                                                                                                                                                                                                                                                                    break Block_55;
                                                                                                                                                                                                                                                                    Label_0141: {
                                                                                                                                                                                                                                                                        viewWidget2.reset();
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    continue Label_0146;
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                viewWidget.setHorizontalBiasPercent(horizontalBiasPercent);
                                                                                                                                                                                                                                                                break Label_1672;
                                                                                                                                                                                                                                                                viewWidget.setVisibility(8);
                                                                                                                                                                                                                                                                break Label_0491;
                                                                                                                                                                                                                                                                break Block_23;
                                                                                                                                                                                                                                                                targetWidget3 = this.getTargetWidget(resolvedLeftToRight);
                                                                                                                                                                                                                                                                n3 = n;
                                                                                                                                                                                                                                                                continue Block_67_Outer;
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        leftToLeft = layoutParams.leftToLeft;
                                                                                                                                                                                                                                                        leftToRight = layoutParams.leftToRight;
                                                                                                                                                                                                                                                        rightToLeft = layoutParams.rightToLeft;
                                                                                                                                                                                                                                                        rightToRight = layoutParams.rightToRight;
                                                                                                                                                                                                                                                        n5 = layoutParams.goneLeftMargin;
                                                                                                                                                                                                                                                        goneRightMargin = layoutParams.goneRightMargin;
                                                                                                                                                                                                                                                        horizontalBiasPercent = layoutParams.horizontalBias;
                                                                                                                                                                                                                                                        guideEnd = leftToLeft;
                                                                                                                                                                                                                                                        n = leftToRight;
                                                                                                                                                                                                                                                        break Block_46;
                                                                                                                                                                                                                                                        Label_1373: {
                                                                                                                                                                                                                                                            break Block_66;
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        Label_0283:
                                                                                                                                                                                                                                                        n = 0;
                                                                                                                                                                                                                                                        break Label_0286;
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    continue Label_1649_Outer;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                break Block_7_Outer;
                                                                                                                                                                                                                                                continue Label_0329_Outer;
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            Label_1913: {
                                                                                                                                                                                                                                                viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            viewWidget.setHeight(layoutParams.height);
                                                                                                                                                                                                                                            break Label_1931;
                                                                                                                                                                                                                                            viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget6, ConstraintAnchor.Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                                                                                                                                                                                                                                            break Label_1322;
                                                                                                                                                                                                                                            guideBegin = layoutParams.guideBegin;
                                                                                                                                                                                                                                            guideEnd = layoutParams.guideEnd;
                                                                                                                                                                                                                                            guidePercent = layoutParams.guidePercent;
                                                                                                                                                                                                                                            break Label_0596;
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        targetWidget7 = this.getTargetWidget(layoutParams.bottomToTop);
                                                                                                                                                                                                                                        break Block_69;
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    break Label_1836;
                                                                                                                                                                                                                                    this.mConstraintSet.applyToInternal(this);
                                                                                                                                                                                                                                    continue Label_0827_Outer;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                Label_1143: {
                                                                                                                                                                                                                                    continue Label_0163_Outer;
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            Label_1283: {
                                                                                                                                                                                                                                break Block_62;
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            n4 = 0;
                                                                                                                                                                                                                            break Label_1421;
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        viewWidget2 = this.getViewWidget(this.getChildAt(n));
                                                                                                                                                                                                                        continue Label_1836_Outer;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    Label_0215: {
                                                                                                                                                                                                                        continue Label_0466_Outer;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                                viewWidget.setVisibility(child3.getVisibility());
                                                                                                                                                                                                                continue Label_0533_Outer;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            Label_1472: {
                                                                                                                                                                                                                break Block_70;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            continue Block_7_Outer;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        viewWidget.setVerticalBiasPercent(layoutParams.verticalBias);
                                                                                                                                                                                                        continue Label_1704;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    n4 = n;
                                                                                                                                                                                                    continue Block_67_Outer;
                                                                                                                                                                                                }
                                                                                                                                                                                                ((Placeholder)child4).updatePreLayout(this);
                                                                                                                                                                                                break Label_0317;
                                                                                                                                                                                                continue Block_17_Outer;
                                                                                                                                                                                            }
                                                                                                                                                                                            continue Label_0163_Outer;
                                                                                                                                                                                        }
                                                                                                                                                                                        Label_1192: {
                                                                                                                                                                                            n3 = n;
                                                                                                                                                                                        }
                                                                                                                                                                                        continue Label_0286_Outer;
                                                                                                                                                                                    }
                                                                                                                                                                                    guideEnd = leftToLeft;
                                                                                                                                                                                    break Label_0533_Outer;
                                                                                                                                                                                    Label_0396: {
                                                                                                                                                                                        break Block_54_Outer;
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                                viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget2, ConstraintAnchor.Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                                                                                                                                                                                continue Label_1421;
                                                                                                                                                                            }
                                                                                                                                                                            viewWidget.setDimensionRatio(layoutParams.dimensionRatio);
                                                                                                                                                                            break Label_1952;
                                                                                                                                                                        }
                                                                                                                                                                        viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, n5);
                                                                                                                                                                        continue Block_25_Outer;
                                                                                                                                                                    }
                                                                                                                                                                    viewWidget.setCompanionWidget(child3);
                                                                                                                                                                    this.mLayoutWidget.add(viewWidget);
                                                                                                                                                                    break Label_0466;
                                                                                                                                                                }
                                                                                                                                                                targetWidget6 = this.getTargetWidget(n3);
                                                                                                                                                                continue Block_24_Outer;
                                                                                                                                                            }
                                                                                                                                                            break Block_85;
                                                                                                                                                        }
                                                                                                                                                        viewWidget.connectCircularConstraint(targetWidget5, layoutParams.circleAngle, layoutParams.circleRadius);
                                                                                                                                                        continue Label_1704;
                                                                                                                                                    }
                                                                                                                                                    layoutParams2 = (LayoutParams)view.getLayoutParams();
                                                                                                                                                    layoutParams.needsBaseline = true;
                                                                                                                                                    layoutParams2.needsBaseline = true;
                                                                                                                                                    viewWidget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(targetWidget8.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                                                                                                                                                    viewWidget.getAnchor(ConstraintAnchor.Type.TOP).reset();
                                                                                                                                                    viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                                                                                                                                                    continue Block_54_Outer;
                                                                                                                                                }
                                                                                                                                                Label_1818: {
                                                                                                                                                    viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                                                                                                                                                }
                                                                                                                                                viewWidget.setWidth(layoutParams.width);
                                                                                                                                                break Label_1836;
                                                                                                                                            }
                                                                                                                                            continue Block_64_Outer;
                                                                                                                                        }
                                                                                                                                        view = (View)this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                                                                                                                                        targetWidget8 = this.getTargetWidget(layoutParams.baselineToBaseline);
                                                                                                                                        continue Label_1520_Outer;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                layoutParams.helped = (n != 0);
                                                                                                                                continue Label_0466;
                                                                                                                            }
                                                                                                                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                                                                                                                            viewWidget.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                                                                                                                            viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
                                                                                                                            continue Label_1931;
                                                                                                                        }
                                                                                                                        guideline.setGuideEnd(guideEnd);
                                                                                                                        n4 = n;
                                                                                                                        break Label_2042;
                                                                                                                        Label_1896: {
                                                                                                                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                                                                                                                        }
                                                                                                                        viewWidget.setHeight(0);
                                                                                                                        continue Label_1931;
                                                                                                                    }
                                                                                                                    viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget9, ConstraintAnchor.Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                                                                                                                    break Label_1322;
                                                                                                                }
                                                                                                                n = 0;
                                                                                                                continue Label_0254;
                                                                                                                continue Label_0533_Outer;
                                                                                                            }
                                                                                                            viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget7, ConstraintAnchor.Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                                                                                                            continue Label_1520;
                                                                                                        }
                                                                                                        continue Block_50_Outer;
                                                                                                    }
                                                                                                    try {
                                                                                                        resourceName2 = this.getResources().getResourceName(child3.getId());
                                                                                                        this.setDesignInformation(n, resourceName2, child3.getId());
                                                                                                        this.getTargetWidget(child3.getId()).setDebugName(resourceName2.substring(resourceName2.indexOf("id/") + 3));
                                                                                                    }
                                                                                                    catch (Resources$NotFoundException ex) {}
                                                                                                    continue Label_0466;
                                                                                                }
                                                                                                this.mVariableDimensionsWidgets.add(viewWidget);
                                                                                                continue Label_0286_Outer;
                                                                                            }
                                                                                            guideline = (Guideline)viewWidget;
                                                                                            guideBegin = layoutParams.resolvedGuideBegin;
                                                                                            guideEnd = layoutParams.resolvedGuideEnd;
                                                                                            guidePercent = layoutParams.resolvedGuidePercent;
                                                                                            continue Block_84_Outer;
                                                                                        }
                                                                                        viewWidget.setHorizontalWeight(layoutParams.horizontalWeight);
                                                                                        viewWidget.setVerticalWeight(layoutParams.verticalWeight);
                                                                                        viewWidget.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
                                                                                        viewWidget.setVerticalChainStyle(layoutParams.verticalChainStyle);
                                                                                        viewWidget.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth, layoutParams.matchConstraintPercentWidth);
                                                                                        viewWidget.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight, layoutParams.matchConstraintPercentHeight);
                                                                                        break Label_2042;
                                                                                    }
                                                                                    targetWidget4 = this.getTargetWidget(layoutParams.topToBottom);
                                                                                    continue Label_0329_Outer;
                                                                                }
                                                                                continue Block_67_Outer;
                                                                            }
                                                                            ++n;
                                                                            continue Label_0163;
                                                                        }
                                                                        n4 = n;
                                                                        break Label_2042;
                                                                    }
                                                                    n6 = guideEnd;
                                                                    resolvedLeftToRight = n;
                                                                    guideEnd = rightToLeft;
                                                                    n = rightToRight;
                                                                    continue Label_0329_Outer;
                                                                }
                                                                ++n;
                                                                continue Label_0286;
                                                            }
                                                            n = layoutParams.endToEnd;
                                                            guideEnd = rightToLeft;
                                                            break Label_0254;
                                                            continue Label_1520_Outer;
                                                        }
                                                    }
                                                    ++n2;
                                                    n = n4;
                                                    continue Label_0329;
                                                }
                                                this.mConstraintHelpers.get(n).updatePreLayout(this);
                                                ++n;
                                                continue Label_0254;
                                            }
                                            resolvedLeftToLeft = n6;
                                            resolveGoneRightMargin = goneRightMargin;
                                            continue;
                                        }
                                    }
                                    targetWidget10 = this.getTargetWidget(layoutParams.bottomToBottom);
                                    viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget10, ConstraintAnchor.Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                                    continue Label_1520;
                                }
                                viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                                viewWidget.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                                viewWidget.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
                                continue Label_1836;
                            }
                            targetWidget9 = this.getTargetWidget(guideEnd);
                            continue;
                        }
                    }
                    // iftrue(Label_1322:, targetWidget9 == null)
                    catch (Resources$NotFoundException ex2) {
                        continue;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    private void setSelfDimensionBehaviour(int size, int size2) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        final int mode2 = View$MeasureSpec.getMode(size2);
        size2 = View$MeasureSpec.getSize(size2);
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        this.getLayoutParams();
        Label_0114: {
            if (mode != Integer.MIN_VALUE) {
                if (mode != 0) {
                    if (mode == 1073741824) {
                        size = Math.min(this.mMaxWidth, size) - (paddingLeft + paddingRight);
                        break Label_0114;
                    }
                }
                else {
                    horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                }
                size = 0;
            }
            else {
                horizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }
        }
        Label_0171: {
            if (mode2 != Integer.MIN_VALUE) {
                if (mode2 != 0) {
                    if (mode2 == 1073741824) {
                        size2 = Math.min(this.mMaxHeight, size2) - (paddingTop + paddingBottom);
                        break Label_0171;
                    }
                }
                else {
                    verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                }
                size2 = 0;
            }
            else {
                verticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }
        }
        this.mLayoutWidget.setMinWidth(0);
        this.mLayoutWidget.setMinHeight(0);
        this.mLayoutWidget.setHorizontalDimensionBehaviour(horizontalDimensionBehaviour);
        this.mLayoutWidget.setWidth(size);
        this.mLayoutWidget.setVerticalDimensionBehaviour(verticalDimensionBehaviour);
        this.mLayoutWidget.setHeight(size2);
        this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
        this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
    }
    
    private void updateHierarchy() {
        final int childCount = this.getChildCount();
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= childCount) {
                break;
            }
            if (this.getChildAt(n2).isLayoutRequested()) {
                n3 = 1;
                break;
            }
            ++n2;
        }
        if (n3 != 0) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
        }
    }
    
    private void updatePostMeasures() {
        final int childCount = this.getChildCount();
        final int n = 0;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof Placeholder) {
                ((Placeholder)child).updatePostMeasure(this);
            }
        }
        final int size = this.mConstraintHelpers.size();
        if (size > 0) {
            for (int j = n; j < size; ++j) {
                this.mConstraintHelpers.get(j).updatePostMeasure(this);
            }
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.isInEditMode()) {
            final int childCount = this.getChildCount();
            final float n = (float)this.getWidth();
            final float n2 = (float)this.getHeight();
            for (int i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final Object tag = child.getTag();
                    if (tag != null && tag instanceof String) {
                        final String[] split = ((String)tag).split(",");
                        if (split.length == 4) {
                            final int int1 = Integer.parseInt(split[0]);
                            final int int2 = Integer.parseInt(split[1]);
                            final int int3 = Integer.parseInt(split[2]);
                            final int int4 = Integer.parseInt(split[3]);
                            final int n3 = (int)(int1 / 1080.0f * n);
                            final int n4 = (int)(int2 / 1920.0f * n2);
                            final int n5 = (int)(int3 / 1080.0f * n);
                            final int n6 = (int)(int4 / 1920.0f * n2);
                            final Paint paint = new Paint();
                            paint.setColor(-65536);
                            final float n7 = (float)n3;
                            final float n8 = (float)n4;
                            final float n9 = (float)(n3 + n5);
                            canvas.drawLine(n7, n8, n9, n8, paint);
                            final float n10 = (float)(n4 + n6);
                            canvas.drawLine(n9, n8, n9, n10, paint);
                            canvas.drawLine(n9, n10, n7, n10, paint);
                            canvas.drawLine(n7, n10, n7, n8, paint);
                            paint.setColor(-16711936);
                            canvas.drawLine(n7, n8, n9, n10, paint);
                            canvas.drawLine(n7, n10, n9, n8, paint);
                        }
                    }
                }
            }
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return (ViewGroup$LayoutParams)new LayoutParams(viewGroup$LayoutParams);
    }
    
    public Object getDesignInformation(final int n, final Object o) {
        if (n == 0 && o instanceof String) {
            final String s = (String)o;
            if (this.mDesignIds != null && this.mDesignIds.containsKey(s)) {
                return this.mDesignIds.get(s);
            }
        }
        return null;
    }
    
    public int getMaxHeight() {
        return this.mMaxHeight;
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public int getMinHeight() {
        return this.mMinHeight;
    }
    
    public int getMinWidth() {
        return this.mMinWidth;
    }
    
    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }
    
    public final ConstraintWidget getViewWidget(final View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        ConstraintWidget widget;
        if (view == null) {
            widget = null;
        }
        else {
            widget = ((LayoutParams)view.getLayoutParams()).widget;
        }
        return widget;
    }
    
    protected void onLayout(final boolean b, int i, int n, int n2, int drawY) {
        n2 = this.getChildCount();
        final boolean inEditMode = this.isInEditMode();
        n = 0;
        View child;
        LayoutParams layoutParams;
        ConstraintWidget widget;
        int drawX;
        int n3;
        int n4;
        View content;
        for (i = 0; i < n2; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            widget = layoutParams.widget;
            if (child.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || inEditMode) {
                if (!layoutParams.isInPlaceholder) {
                    drawX = widget.getDrawX();
                    drawY = widget.getDrawY();
                    n3 = widget.getWidth() + drawX;
                    n4 = widget.getHeight() + drawY;
                    child.layout(drawX, drawY, n3, n4);
                    if (child instanceof Placeholder) {
                        content = ((Placeholder)child).getContent();
                        if (content != null) {
                            content.setVisibility(0);
                            content.layout(drawX, drawY, n3, n4);
                        }
                    }
                }
            }
        }
        n2 = this.mConstraintHelpers.size();
        if (n2 > 0) {
            for (i = n; i < n2; ++i) {
                this.mConstraintHelpers.get(i).updatePostLayout(this);
            }
        }
    }
    
    protected void onMeasure(int resolveSizeAndState, int min) {
        System.currentTimeMillis();
        final int mode = View$MeasureSpec.getMode(resolveSizeAndState);
        final int size = View$MeasureSpec.getSize(resolveSizeAndState);
        final int mode2 = View$MeasureSpec.getMode(min);
        final int size2 = View$MeasureSpec.getSize(min);
        if (this.mLastMeasureWidth != -1) {
            final int mLastMeasureHeight = this.mLastMeasureHeight;
        }
        if (mode == 1073741824 && mode2 == 1073741824 && size == this.mLastMeasureWidth) {
            final int mLastMeasureHeight2 = this.mLastMeasureHeight;
        }
        final boolean b = mode == this.mLastMeasureWidthMode && mode2 == this.mLastMeasureHeightMode;
        if (b && size == this.mLastMeasureWidthSize) {
            final int mLastMeasureHeightSize = this.mLastMeasureHeightSize;
        }
        if (b && mode == Integer.MIN_VALUE && mode2 == 1073741824 && size >= this.mLastMeasureWidth) {
            final int mLastMeasureHeight3 = this.mLastMeasureHeight;
        }
        if (b && mode == 1073741824 && mode2 == Integer.MIN_VALUE && size == this.mLastMeasureWidth) {
            final int mLastMeasureHeight4 = this.mLastMeasureHeight;
        }
        this.mLastMeasureWidthMode = mode;
        this.mLastMeasureHeightMode = mode2;
        this.mLastMeasureWidthSize = size;
        this.mLastMeasureHeightSize = size2;
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        this.mLayoutWidget.setX(paddingLeft);
        this.mLayoutWidget.setY(paddingTop);
        this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
        this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
        if (Build$VERSION.SDK_INT >= 17) {
            this.mLayoutWidget.setRtl(this.getLayoutDirection() == 1);
        }
        this.setSelfDimensionBehaviour(resolveSizeAndState, min);
        final int width = this.mLayoutWidget.getWidth();
        final int height = this.mLayoutWidget.getHeight();
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
        }
        final boolean b2 = (this.mOptimizationLevel & 0x8) == 0x8;
        if (b2) {
            this.mLayoutWidget.preOptimize();
            this.mLayoutWidget.optimizeForDimensions(width, height);
            this.internalMeasureDimensions(resolveSizeAndState, min);
        }
        else {
            this.internalMeasureChildren(resolveSizeAndState, min);
        }
        this.updatePostMeasures();
        if (this.getChildCount() > 0) {
            this.solveLinearSystem("First pass");
        }
        final int size3 = this.mVariableDimensionsWidgets.size();
        final int n = paddingTop + this.getPaddingBottom();
        final int n2 = paddingLeft + this.getPaddingRight();
        int n10;
        if (size3 > 0) {
            final boolean b3 = this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            final boolean b4 = this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            int max = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
            int height2 = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
            int i = 0;
            int n3 = 0;
            int combineMeasuredStates = 0;
            while (i < size3) {
                final ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(i);
                final View view = (View)constraintWidget.getCompanionWidget();
                int n7 = 0;
                int n8 = 0;
                Label_1051: {
                    if (view != null) {
                        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                        if (!layoutParams.isHelper && !layoutParams.isGuideline) {
                            if (view.getVisibility() != 8 && (!b2 || !constraintWidget.getResolutionWidth().isResolved() || !constraintWidget.getResolutionHeight().isResolved())) {
                                int n4;
                                if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                                    n4 = getChildMeasureSpec(resolveSizeAndState, n2, layoutParams.width);
                                }
                                else {
                                    n4 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
                                }
                                int n5;
                                if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                                    n5 = getChildMeasureSpec(min, n, layoutParams.height);
                                }
                                else {
                                    n5 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
                                }
                                view.measure(n4, n5);
                                if (this.mMetrics != null) {
                                    final Metrics mMetrics = this.mMetrics;
                                    ++mMetrics.additionalMeasures;
                                }
                                final int measuredWidth = view.getMeasuredWidth();
                                final int measuredHeight = view.getMeasuredHeight();
                                int n6 = n3;
                                int max2 = max;
                                if (measuredWidth != constraintWidget.getWidth()) {
                                    constraintWidget.setWidth(measuredWidth);
                                    if (b2) {
                                        constraintWidget.getResolutionWidth().resolve(measuredWidth);
                                    }
                                    max2 = max;
                                    if (b3 && constraintWidget.getRight() > (max2 = max)) {
                                        max2 = Math.max(max, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                    }
                                    n6 = 1;
                                }
                                if (measuredHeight != constraintWidget.getHeight()) {
                                    constraintWidget.setHeight(measuredHeight);
                                    if (b2) {
                                        constraintWidget.getResolutionHeight().resolve(measuredHeight);
                                    }
                                    if (b4) {
                                        final int bottom = constraintWidget.getBottom();
                                        final int a = height2;
                                        if (bottom > a) {
                                            height2 = Math.max(a, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                        }
                                    }
                                    n6 = 1;
                                }
                                if (layoutParams.needsBaseline) {
                                    final int baseline = view.getBaseline();
                                    n7 = n6;
                                    if (baseline != -1) {
                                        n7 = n6;
                                        if (baseline != constraintWidget.getBaselineDistance()) {
                                            constraintWidget.setBaselineDistance(baseline);
                                            n7 = 1;
                                        }
                                    }
                                }
                                else {
                                    n7 = n6;
                                }
                                if (Build$VERSION.SDK_INT >= 11) {
                                    combineMeasuredStates = combineMeasuredStates(combineMeasuredStates, view.getMeasuredState());
                                    n8 = max2;
                                    break Label_1051;
                                }
                                n8 = max2;
                                break Label_1051;
                            }
                        }
                    }
                    n8 = max;
                    n7 = n3;
                }
                ++i;
                n3 = n7;
                max = n8;
            }
            final int n9 = combineMeasuredStates;
            if (n3 != 0) {
                this.mLayoutWidget.setWidth(width);
                this.mLayoutWidget.setHeight(height);
                if (b2) {
                    this.mLayoutWidget.solveGraph();
                }
                this.solveLinearSystem("2nd pass");
                boolean b5;
                if (this.mLayoutWidget.getWidth() < max) {
                    this.mLayoutWidget.setWidth(max);
                    b5 = true;
                }
                else {
                    b5 = false;
                }
                boolean b6;
                if (this.mLayoutWidget.getHeight() < height2) {
                    this.mLayoutWidget.setHeight(height2);
                    b6 = true;
                }
                else {
                    b6 = b5;
                }
                if (b6) {
                    this.solveLinearSystem("3rd pass");
                }
            }
            int index = 0;
            while (true) {
                n10 = n9;
                if (index >= size3) {
                    break;
                }
                final ConstraintWidget constraintWidget2 = this.mVariableDimensionsWidgets.get(index);
                final View view2 = (View)constraintWidget2.getCompanionWidget();
                if (view2 != null && (view2.getMeasuredWidth() != constraintWidget2.getWidth() || view2.getMeasuredHeight() != constraintWidget2.getHeight()) && constraintWidget2.getVisibility() != 8) {
                    view2.measure(View$MeasureSpec.makeMeasureSpec(constraintWidget2.getWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(constraintWidget2.getHeight(), 1073741824));
                    if (this.mMetrics != null) {
                        final Metrics mMetrics2 = this.mMetrics;
                        ++mMetrics2.additionalMeasures;
                    }
                }
                ++index;
            }
        }
        else {
            n10 = 0;
        }
        final int mLastMeasureWidth = this.mLayoutWidget.getWidth() + n2;
        final int mLastMeasureHeight5 = this.mLayoutWidget.getHeight() + n;
        if (Build$VERSION.SDK_INT >= 11) {
            resolveSizeAndState = resolveSizeAndState(mLastMeasureWidth, resolveSizeAndState, n10);
            final int resolveSizeAndState2 = resolveSizeAndState(mLastMeasureHeight5, min, n10 << 16);
            min = Math.min(this.mMaxWidth, resolveSizeAndState & 0xFFFFFF);
            final int min2 = Math.min(this.mMaxHeight, resolveSizeAndState2 & 0xFFFFFF);
            resolveSizeAndState = min;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                resolveSizeAndState = (min | 0x1000000);
            }
            min = min2;
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                min = (min2 | 0x1000000);
            }
            this.setMeasuredDimension(resolveSizeAndState, min);
            this.mLastMeasureWidth = resolveSizeAndState;
            this.mLastMeasureHeight = min;
        }
        else {
            this.setMeasuredDimension(mLastMeasureWidth, mLastMeasureHeight5);
            this.mLastMeasureWidth = mLastMeasureWidth;
            this.mLastMeasureHeight = mLastMeasureHeight5;
        }
    }
    
    public void onViewAdded(final View view) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        final ConstraintWidget viewWidget = this.getViewWidget(view);
        if (view instanceof android.support.constraint.Guideline && !(viewWidget instanceof Guideline)) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.widget = new Guideline();
            layoutParams.isGuideline = true;
            ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
        }
        if (view instanceof ConstraintHelper) {
            final ConstraintHelper constraintHelper = (ConstraintHelper)view;
            constraintHelper.validateParams();
            ((LayoutParams)view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(constraintHelper)) {
                this.mConstraintHelpers.add(constraintHelper);
            }
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }
    
    public void onViewRemoved(final View o) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewRemoved(o);
        }
        this.mChildrenByIds.remove(o.getId());
        final ConstraintWidget viewWidget = this.getViewWidget(o);
        this.mLayoutWidget.remove(viewWidget);
        this.mConstraintHelpers.remove(o);
        this.mVariableDimensionsWidgets.remove(viewWidget);
        this.mDirtyHierarchy = true;
    }
    
    public void removeView(final View view) {
        super.removeView(view);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }
    
    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }
    
    public void setConstraintSet(final ConstraintSet mConstraintSet) {
        this.mConstraintSet = mConstraintSet;
    }
    
    public void setDesignInformation(int i, final Object o, final Object o2) {
        if (i == 0 && o instanceof String && o2 instanceof Integer) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap<String, Integer>();
            }
            final String s = (String)o;
            i = s.indexOf("/");
            String substring = s;
            if (i != -1) {
                substring = s.substring(i + 1);
            }
            i = (int)o2;
            this.mDesignIds.put(substring, i);
        }
    }
    
    public void setId(final int id) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(id);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }
    
    public void setMaxHeight(final int mMaxHeight) {
        if (mMaxHeight == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = mMaxHeight;
        this.requestLayout();
    }
    
    public void setMaxWidth(final int mMaxWidth) {
        if (mMaxWidth == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = mMaxWidth;
        this.requestLayout();
    }
    
    public void setMinHeight(final int mMinHeight) {
        if (mMinHeight == this.mMinHeight) {
            return;
        }
        this.mMinHeight = mMinHeight;
        this.requestLayout();
    }
    
    public void setMinWidth(final int mMinWidth) {
        if (mMinWidth == this.mMinWidth) {
            return;
        }
        this.mMinWidth = mMinWidth;
        this.requestLayout();
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        this.mLayoutWidget.setOptimizationLevel(optimizationLevel);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    protected void solveLinearSystem(final String s) {
        this.mLayoutWidget.layout();
        if (this.mMetrics != null) {
            final Metrics mMetrics = this.mMetrics;
            ++mMetrics.resolutions;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int baselineToBaseline;
        public int bottomToBottom;
        public int bottomToTop;
        public float circleAngle;
        public int circleConstraint;
        public int circleRadius;
        public boolean constrainedHeight;
        public boolean constrainedWidth;
        public String dimensionRatio;
        int dimensionRatioSide;
        float dimensionRatioValue;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public boolean helped;
        public float horizontalBias;
        public int horizontalChainStyle;
        boolean horizontalDimensionFixed;
        public float horizontalWeight;
        boolean isGuideline;
        boolean isHelper;
        boolean isInPlaceholder;
        public int leftToLeft;
        public int leftToRight;
        public int matchConstraintDefaultHeight;
        public int matchConstraintDefaultWidth;
        public int matchConstraintMaxHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMinWidth;
        public float matchConstraintPercentHeight;
        public float matchConstraintPercentWidth;
        boolean needsBaseline;
        public int orientation;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias;
        int resolvedLeftToLeft;
        int resolvedLeftToRight;
        int resolvedRightToLeft;
        int resolvedRightToRight;
        public int rightToLeft;
        public int rightToRight;
        public int startToEnd;
        public int startToStart;
        public int topToBottom;
        public int topToTop;
        public float verticalBias;
        public int verticalChainStyle;
        boolean verticalDimensionFixed;
        public float verticalWeight;
        ConstraintWidget widget;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        public LayoutParams(Context obtainStyledAttributes, final AttributeSet set) {
            super(obtainStyledAttributes, set);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
            obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            final int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount();
            int n = 0;
        Label_2033_Outer:
            while (true) {
                Label_2039: {
                    if (n >= indexCount) {
                        break Label_2039;
                    }
                    final int index = ((TypedArray)obtainStyledAttributes).getIndex(n);
                    int length;
                    int index2;
                    String substring;
                    int index3;
                    String substring2 = null;
                    String substring3 = null;
                    float float1;
                    float float2;
                    String substring4;
                    Block_21_Outer:Block_35_Outer:
                    while (true) {
                        Label_2019: {
                            switch (Table.map.get(index)) {
                                default: {
                                    break Label_2033;
                                }
                                case 44: {
                                    this.dimensionRatio = ((TypedArray)obtainStyledAttributes).getString(index);
                                    this.dimensionRatioValue = Float.NaN;
                                    this.dimensionRatioSide = -1;
                                    if (this.dimensionRatio == null) {
                                        break Label_2033;
                                    }
                                    length = this.dimensionRatio.length();
                                    index2 = this.dimensionRatio.indexOf(44);
                                    if (index2 > 0 && index2 < length - 1) {
                                        substring = this.dimensionRatio.substring(0, index2);
                                        if (substring.equalsIgnoreCase("W")) {
                                            this.dimensionRatioSide = 0;
                                        }
                                        else if (substring.equalsIgnoreCase("H")) {
                                            this.dimensionRatioSide = 1;
                                        }
                                        ++index2;
                                    }
                                    else {
                                        index2 = 0;
                                    }
                                    index3 = this.dimensionRatio.indexOf(58);
                                    if (index3 < 0 || index3 >= length - 1) {
                                        break Label_2019;
                                    }
                                    substring2 = this.dimensionRatio.substring(index2, index3);
                                    substring3 = this.dimensionRatio.substring(index3 + 1);
                                    if (substring2.length() > 0 && substring3.length() > 0) {
                                        break;
                                    }
                                    break Label_2033;
                                }
                                case 38: {
                                    break Label_2019;
                                }
                                case 37: {
                                    break Label_2019;
                                }
                                case 36: {
                                    break Label_2019;
                                }
                                case 35: {
                                    break Label_2019;
                                }
                                case 34: {
                                    break Label_2019;
                                }
                                case 33: {
                                    break Label_2019;
                                }
                                case 32: {
                                    break Label_2019;
                                }
                                case 31: {
                                    break Label_2019;
                                }
                                case 30: {
                                    break Label_2019;
                                }
                                case 29: {
                                    break Label_2019;
                                }
                                case 28: {
                                    break Label_2019;
                                }
                                case 27: {
                                    break Label_2019;
                                }
                                case 26: {
                                    break Label_2019;
                                }
                                case 25: {
                                    break Label_2019;
                                }
                                case 24: {
                                    break Label_2019;
                                }
                                case 23: {
                                    break Label_2019;
                                }
                                case 22: {
                                    break Label_2019;
                                }
                                case 21: {
                                    break Label_2019;
                                }
                                case 20: {
                                    break Label_2019;
                                }
                                case 19: {
                                    break Label_2019;
                                }
                                case 18: {
                                    break Label_2019;
                                }
                                case 17: {
                                    break Label_2019;
                                }
                                case 16: {
                                    break Label_2019;
                                }
                                case 15: {
                                    break Label_2019;
                                }
                                case 14: {
                                    break Label_2019;
                                }
                                case 13: {
                                    break Label_2019;
                                }
                                case 12: {
                                    break Label_2019;
                                }
                                case 11: {
                                    break Label_2019;
                                }
                                case 10: {
                                    break Label_2019;
                                }
                                case 9: {
                                    break Label_2019;
                                }
                                case 8: {
                                    break Label_2019;
                                }
                                case 7: {
                                    break Label_2019;
                                }
                                case 6: {
                                    break Label_2019;
                                }
                                case 5: {
                                    break Label_2019;
                                }
                                case 4: {
                                    break Label_2019;
                                }
                                case 3: {
                                    break Label_2019;
                                }
                                case 2: {
                                    break Label_2019;
                                }
                                case 1: {
                                    break Label_2019;
                                }
                                case 0:
                                case 39:
                                case 40:
                                case 41:
                                case 42: {
                                    break Label_2033;
                                }
                                case 50: {
                                    this.editorAbsoluteY = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteY);
                                    break Label_2033;
                                }
                                case 49: {
                                    this.editorAbsoluteX = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteX);
                                    break Label_2033;
                                }
                                case 48: {
                                    this.verticalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                    break Label_2033;
                                }
                                case 47: {
                                    this.horizontalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                    break Label_2033;
                                }
                                case 46: {
                                    this.verticalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalWeight);
                                    break Label_2033;
                                }
                                case 45: {
                                    this.horizontalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalWeight);
                                    break Label_2033;
                                }
                            }
                            try {
                                float1 = Float.parseFloat(substring2);
                                float2 = Float.parseFloat(substring3);
                                if (float1 > 0.0f && float2 > 0.0f) {
                                    if (this.dimensionRatioSide == 1) {
                                        this.dimensionRatioValue = Math.abs(float2 / float1);
                                    }
                                    else {
                                        this.dimensionRatioValue = Math.abs(float1 / float2);
                                    }
                                }
                                ++n;
                                continue Label_2033_Outer;
                                this.orientation = ((TypedArray)obtainStyledAttributes).getInt(index, this.orientation);
                                continue;
                                this.matchConstraintPercentHeight = Math.max(0.0f, ((TypedArray)obtainStyledAttributes).getFloat(index, this.matchConstraintPercentHeight));
                                continue;
                                this.startToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToEnd);
                                // iftrue(Label_2033:, this.startToEnd != -1)
                                // iftrue(Label_2033:, this.bottomToBottom != -1)
                                // iftrue(Label_2033:, this.bottomToTop != -1)
                                // iftrue(Label_2033:, this.circleConstraint != -1)
                                // iftrue(Label_2033:, this.topToTop != -1)
                                // iftrue(Label_2033:, this.topToBottom != -1)
                                // iftrue(Label_2033:, this.leftToRight != -1)
                                // iftrue(Label_2033:, substring4.length() <= 0)
                                // iftrue(Label_2033:, this.leftToLeft != -1)
                                // iftrue(Label_2033:, this.endToEnd != -1)
                                // iftrue(Label_2033:, this.rightToRight != -1)
                                // iftrue(Label_2033:, this.matchConstraintDefaultHeight != 1)
                                // iftrue(Label_2033:, this.endToStart != -1)
                                // iftrue(Label_2033:, this.rightToLeft != -1)
                                // iftrue(Label_2033:, this.startToStart != -1)
                                // iftrue(Label_2033:, this.baselineToBaseline != -1)
                                // iftrue(Label_2033:, this.matchConstraintDefaultWidth != 1)
                                // iftrue(Label_2033:, this.circleAngle >= 0.0f)
                                Block_25: {
                                    break Block_25;
                                    this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToBottom);
                                    while (true) {
                                        Block_29_Outer:Block_20_Outer:Block_32_Outer:
                                        while (true) {
                                        Block_24_Outer:
                                            while (true) {
                                                while (true) {
                                                    Block_15: {
                                                        Block_31: {
                                                            Block_33: {
                                                                Block_28: {
                                                                    Block_34: {
                                                                    Block_23_Outer:
                                                                        while (true) {
                                                                            while (true) {
                                                                                while (true) {
                                                                                    Block_36: {
                                                                                        while (true) {
                                                                                            while (true) {
                                                                                                Block_27: {
                                                                                                    break Block_27;
                                                                                                    this.bottomToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToTop);
                                                                                                    break Block_28;
                                                                                                    this.verticalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalBias);
                                                                                                    continue Block_21_Outer;
                                                                                                    this.circleConstraint = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.circleConstraint);
                                                                                                    break Block_36;
                                                                                                    this.goneLeftMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneLeftMargin);
                                                                                                    continue Block_21_Outer;
                                                                                                    Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                                                                                                    continue Block_21_Outer;
                                                                                                    this.topToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                                    continue Block_21_Outer;
                                                                                                    this.horizontalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalBias);
                                                                                                    continue Block_21_Outer;
                                                                                                    this.topToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                                    continue Block_21_Outer;
                                                                                                    try {
                                                                                                        this.matchConstraintMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinHeight);
                                                                                                    }
                                                                                                    catch (Exception ex) {
                                                                                                        if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMinHeight) == -2) {
                                                                                                            this.matchConstraintMinHeight = -2;
                                                                                                        }
                                                                                                    }
                                                                                                    continue Block_21_Outer;
                                                                                                    this.matchConstraintPercentWidth = Math.max(0.0f, ((TypedArray)obtainStyledAttributes).getFloat(index, this.matchConstraintPercentWidth));
                                                                                                    continue Block_21_Outer;
                                                                                                    Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                                                                                                    continue Block_21_Outer;
                                                                                                    this.goneStartMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneStartMargin);
                                                                                                    continue Block_21_Outer;
                                                                                                }
                                                                                                this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                                continue Block_21_Outer;
                                                                                                this.topToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToTop);
                                                                                                continue Block_29_Outer;
                                                                                            }
                                                                                            try {
                                                                                                this.matchConstraintMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinWidth);
                                                                                            }
                                                                                            catch (Exception ex2) {
                                                                                                if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMinWidth) == -2) {
                                                                                                    this.matchConstraintMinWidth = -2;
                                                                                                }
                                                                                            }
                                                                                            continue Block_21_Outer;
                                                                                            this.topToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToBottom);
                                                                                            continue Block_20_Outer;
                                                                                        }
                                                                                        this.leftToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToRight);
                                                                                        break Block_33;
                                                                                        this.guidePercent = ((TypedArray)obtainStyledAttributes).getFloat(index, this.guidePercent);
                                                                                        continue Block_21_Outer;
                                                                                        this.rightToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                        continue Block_21_Outer;
                                                                                    }
                                                                                    this.circleConstraint = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                    continue Block_21_Outer;
                                                                                    this.constrainedHeight = ((TypedArray)obtainStyledAttributes).getBoolean(index, this.constrainedHeight);
                                                                                    continue Block_21_Outer;
                                                                                    substring4 = this.dimensionRatio.substring(index2);
                                                                                    break Block_15;
                                                                                    try {
                                                                                        this.matchConstraintMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                                                                                    }
                                                                                    catch (Exception ex3) {
                                                                                        if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMaxWidth) == -2) {
                                                                                            this.matchConstraintMaxWidth = -2;
                                                                                        }
                                                                                    }
                                                                                    continue Block_21_Outer;
                                                                                    this.endToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                                    continue Block_21_Outer;
                                                                                    this.leftToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToLeft);
                                                                                    break Block_34;
                                                                                    this.endToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToEnd);
                                                                                    break Block_29_Outer;
                                                                                    this.rightToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToRight);
                                                                                    break Block_31;
                                                                                    this.circleAngle = (360.0f - this.circleAngle) % 360.0f;
                                                                                    continue Block_21_Outer;
                                                                                    this.matchConstraintDefaultHeight = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                                                                    continue Block_32_Outer;
                                                                                }
                                                                                this.endToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToStart);
                                                                                continue Block_35_Outer;
                                                                            }
                                                                            this.rightToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToLeft);
                                                                            continue Block_23_Outer;
                                                                        }
                                                                        this.guideBegin = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideBegin);
                                                                        continue Block_21_Outer;
                                                                    }
                                                                    this.leftToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                    continue Block_21_Outer;
                                                                }
                                                                this.bottomToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                                continue Block_21_Outer;
                                                            }
                                                            this.leftToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                            continue Block_21_Outer;
                                                            this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                            continue Block_21_Outer;
                                                            this.circleRadius = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.circleRadius);
                                                            continue Block_21_Outer;
                                                            this.startToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                            continue Block_21_Outer;
                                                        }
                                                        this.rightToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                                        continue Block_21_Outer;
                                                        ((TypedArray)obtainStyledAttributes).recycle();
                                                        this.validate();
                                                        return;
                                                    }
                                                    this.dimensionRatioValue = Float.parseFloat(substring4);
                                                    continue Block_21_Outer;
                                                    this.guideEnd = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideEnd);
                                                    continue Block_21_Outer;
                                                    this.startToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToStart);
                                                    continue;
                                                }
                                                this.goneBottomMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneBottomMargin);
                                                continue Block_21_Outer;
                                                this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.baselineToBaseline);
                                                continue Block_24_Outer;
                                            }
                                            this.constrainedWidth = ((TypedArray)obtainStyledAttributes).getBoolean(index, this.constrainedWidth);
                                            continue Block_21_Outer;
                                            this.goneEndMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneEndMargin);
                                            continue Block_21_Outer;
                                            this.goneTopMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneTopMargin);
                                            continue Block_21_Outer;
                                            try {
                                                this.matchConstraintMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                                            }
                                            catch (Exception ex4) {
                                                if (((TypedArray)obtainStyledAttributes).getInt(index, this.matchConstraintMaxHeight) == -2) {
                                                    this.matchConstraintMaxHeight = -2;
                                                }
                                            }
                                            continue Block_21_Outer;
                                            this.matchConstraintDefaultWidth = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                            continue Block_35_Outer;
                                        }
                                        this.endToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                        continue Block_21_Outer;
                                        this.circleAngle = ((TypedArray)obtainStyledAttributes).getFloat(index, this.circleAngle) % 360.0f;
                                        continue;
                                    }
                                }
                                this.startToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                continue;
                                this.goneRightMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneRightMargin);
                                continue;
                            }
                            catch (NumberFormatException ex5) {
                                continue;
                            }
                        }
                        break;
                    }
                }
            }
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneStartMargin = -1;
            this.goneEndMargin = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.dimensionRatioValue = 0.0f;
            this.dimensionRatioSide = 1;
            this.horizontalWeight = -1.0f;
            this.verticalWeight = -1.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.matchConstraintPercentWidth = 1.0f;
            this.matchConstraintPercentHeight = 1.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.isHelper = false;
            this.isInPlaceholder = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.helped = false;
        }
        
        @TargetApi(17)
        public void resolveLayoutDirection(int layoutDirection) {
            final int leftMargin = this.leftMargin;
            final int rightMargin = this.rightMargin;
            super.resolveLayoutDirection(layoutDirection);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            this.resolvedGuideBegin = this.guideBegin;
            this.resolvedGuideEnd = this.guideEnd;
            this.resolvedGuidePercent = this.guidePercent;
            layoutDirection = this.getLayoutDirection();
            final int n = 0;
            if (1 == layoutDirection) {
                layoutDirection = 1;
            }
            else {
                layoutDirection = 0;
            }
            if (layoutDirection != 0) {
                Label_0160: {
                    if (this.startToEnd != -1) {
                        this.resolvedRightToLeft = this.startToEnd;
                    }
                    else {
                        layoutDirection = n;
                        if (this.startToStart == -1) {
                            break Label_0160;
                        }
                        this.resolvedRightToRight = this.startToStart;
                    }
                    layoutDirection = 1;
                }
                if (this.endToStart != -1) {
                    this.resolvedLeftToRight = this.endToStart;
                    layoutDirection = 1;
                }
                if (this.endToEnd != -1) {
                    this.resolvedLeftToLeft = this.endToEnd;
                    layoutDirection = 1;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneRightMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneEndMargin;
                }
                if (layoutDirection != 0) {
                    this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
                }
                if (this.isGuideline && this.orientation == 1) {
                    if (this.guidePercent != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - this.guidePercent;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    }
                    else if (this.guideBegin != -1) {
                        this.resolvedGuideEnd = this.guideBegin;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                    else if (this.guideEnd != -1) {
                        this.resolvedGuideBegin = this.guideEnd;
                        this.resolvedGuideEnd = -1;
                        this.resolvedGuidePercent = -1.0f;
                    }
                }
            }
            else {
                if (this.startToEnd != -1) {
                    this.resolvedLeftToRight = this.startToEnd;
                }
                if (this.startToStart != -1) {
                    this.resolvedLeftToLeft = this.startToStart;
                }
                if (this.endToStart != -1) {
                    this.resolvedRightToLeft = this.endToStart;
                }
                if (this.endToEnd != -1) {
                    this.resolvedRightToRight = this.endToEnd;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneRightMargin = this.goneEndMargin;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                if (this.rightToLeft != -1) {
                    this.resolvedRightToLeft = this.rightToLeft;
                    if (this.rightMargin <= 0 && rightMargin > 0) {
                        this.rightMargin = rightMargin;
                    }
                }
                else if (this.rightToRight != -1) {
                    this.resolvedRightToRight = this.rightToRight;
                    if (this.rightMargin <= 0 && rightMargin > 0) {
                        this.rightMargin = rightMargin;
                    }
                }
                if (this.leftToLeft != -1) {
                    this.resolvedLeftToLeft = this.leftToLeft;
                    if (this.leftMargin <= 0 && leftMargin > 0) {
                        this.leftMargin = leftMargin;
                    }
                }
                else if (this.leftToRight != -1) {
                    this.resolvedLeftToRight = this.leftToRight;
                    if (this.leftMargin <= 0 && leftMargin > 0) {
                        this.leftMargin = leftMargin;
                    }
                }
            }
        }
        
        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                this.matchConstraintDefaultWidth = 1;
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                this.matchConstraintDefaultHeight = 1;
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline)this.widget).setOrientation(this.orientation);
            }
        }
        
        private static class Table
        {
            public static final SparseIntArray map;
            
            static {
                (map = new SparseIntArray()).append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                Table.map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                Table.map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
            }
        }
    }
}
