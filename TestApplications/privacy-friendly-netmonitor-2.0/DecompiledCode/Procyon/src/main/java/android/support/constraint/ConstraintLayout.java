// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint;

import android.annotation.TargetApi;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.os.Build$VERSION;
import android.support.constraint.solver.widgets.Guideline;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.view.View;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ConstraintLayout extends ViewGroup
{
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean SIMPLE_LAYOUT = true;
    private static final String TAG = "ConstraintLayout";
    public static final String VERSION = "ConstraintLayout-1.0.0";
    SparseArray<View> mChildrenByIds;
    private ConstraintSet mConstraintSet;
    private boolean mDirtyHierarchy;
    ConstraintWidgetContainer mLayoutWidget;
    private int mMaxHeight;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMinWidth;
    private int mOptimizationLevel;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets;
    
    public ConstraintLayout(final Context context) {
        super(context);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
        this.init(null);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
        this.init(set);
    }
    
    public ConstraintLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mChildrenByIds = (SparseArray<View>)new SparseArray();
        this.mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
        this.mLayoutWidget = new ConstraintWidgetContainer();
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mMaxHeight = Integer.MAX_VALUE;
        this.mDirtyHierarchy = true;
        this.mOptimizationLevel = 2;
        this.mConstraintSet = null;
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
    
    private final ConstraintWidget getViewWidget(final View view) {
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
    
    private void init(final AttributeSet set) {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMinHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxWidth);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = obtainStyledAttributes.getDimensionPixelOffset(index, this.mMaxHeight);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = obtainStyledAttributes.getInt(index, this.mOptimizationLevel);
                }
                else if (index == R.styleable.ConstraintLayout_Layout_constraintSet) {
                    (this.mConstraintSet = new ConstraintSet()).load(this.getContext(), obtainStyledAttributes.getResourceId(index, 0));
                }
            }
            obtainStyledAttributes.recycle();
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
                    final int width = layoutParams.width;
                    final int height = layoutParams.height;
                    final boolean horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
                    boolean b = true;
                    boolean b2 = false;
                    Label_0187: {
                        Label_0184: {
                            if (!horizontalDimensionFixed && !layoutParams.verticalDimensionFixed && (layoutParams.horizontalDimensionFixed || layoutParams.matchConstraintDefaultWidth != 1) && layoutParams.width != -1) {
                                if (!layoutParams.verticalDimensionFixed) {
                                    if (layoutParams.matchConstraintDefaultHeight == 1) {
                                        break Label_0184;
                                    }
                                    if (layoutParams.height == -1) {
                                        break Label_0184;
                                    }
                                }
                                b2 = false;
                                break Label_0187;
                            }
                        }
                        b2 = true;
                    }
                    boolean b3;
                    int measuredWidth;
                    int measuredHeight;
                    if (b2) {
                        int n5;
                        if (width != 0 && width != -1) {
                            n5 = getChildMeasureSpec(n, n4, width);
                            b3 = false;
                        }
                        else {
                            n5 = getChildMeasureSpec(n, n4, -2);
                            b3 = true;
                        }
                        int n6;
                        if (height != 0 && height != -1) {
                            n6 = getChildMeasureSpec(n2, n3, height);
                            b = false;
                        }
                        else {
                            n6 = getChildMeasureSpec(n2, n3, -2);
                        }
                        child.measure(n5, n6);
                        measuredWidth = child.getMeasuredWidth();
                        measuredHeight = child.getMeasuredHeight();
                    }
                    else {
                        b = (b3 = false);
                        measuredHeight = height;
                        measuredWidth = width;
                    }
                    widget.setWidth(measuredWidth);
                    widget.setHeight(measuredHeight);
                    if (b3) {
                        widget.setWrapWidth(measuredWidth);
                    }
                    if (b) {
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
    
    private void setChildrenConstraints() {
        if (this.mConstraintSet != null) {
            this.mConstraintSet.applyToInternal(this);
        }
        final int childCount = this.getChildCount();
        this.mLayoutWidget.removeAllChildren();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            final ConstraintWidget viewWidget = this.getViewWidget(child);
            if (viewWidget != null) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                viewWidget.reset();
                viewWidget.setVisibility(child.getVisibility());
                viewWidget.setCompanionWidget(child);
                this.mLayoutWidget.add(viewWidget);
                if (!layoutParams.verticalDimensionFixed || !layoutParams.horizontalDimensionFixed) {
                    this.mVariableDimensionsWidgets.add(viewWidget);
                }
                if (layoutParams.isGuideline) {
                    final Guideline guideline = (Guideline)viewWidget;
                    if (layoutParams.guideBegin != -1) {
                        guideline.setGuideBegin(layoutParams.guideBegin);
                    }
                    if (layoutParams.guideEnd != -1) {
                        guideline.setGuideEnd(layoutParams.guideEnd);
                    }
                    if (layoutParams.guidePercent != -1.0f) {
                        guideline.setGuidePercent(layoutParams.guidePercent);
                    }
                }
                else if (layoutParams.resolvedLeftToLeft != -1 || layoutParams.resolvedLeftToRight != -1 || layoutParams.resolvedRightToLeft != -1 || layoutParams.resolvedRightToRight != -1 || layoutParams.topToTop != -1 || layoutParams.topToBottom != -1 || layoutParams.bottomToTop != -1 || layoutParams.bottomToBottom != -1 || layoutParams.baselineToBaseline != -1 || layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1 || layoutParams.width == -1 || layoutParams.height == -1) {
                    int resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
                    int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
                    int n = layoutParams.resolvedRightToLeft;
                    int n2 = layoutParams.resolvedRightToRight;
                    int resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
                    int resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
                    float resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
                    if (Build$VERSION.SDK_INT < 17) {
                        final int leftToLeft = layoutParams.leftToLeft;
                        final int leftToRight = layoutParams.leftToRight;
                        final int rightToLeft = layoutParams.rightToLeft;
                        final int rightToRight = layoutParams.rightToRight;
                        final int goneLeftMargin = layoutParams.goneLeftMargin;
                        final int goneRightMargin = layoutParams.goneRightMargin;
                        final float horizontalBias = layoutParams.horizontalBias;
                        int startToStart = leftToLeft;
                        int startToEnd = leftToRight;
                        if (leftToLeft == -1) {
                            startToStart = leftToLeft;
                            if ((startToEnd = leftToRight) == -1) {
                                if (layoutParams.startToStart != -1) {
                                    startToStart = layoutParams.startToStart;
                                    startToEnd = leftToRight;
                                }
                                else {
                                    startToStart = leftToLeft;
                                    startToEnd = leftToRight;
                                    if (layoutParams.startToEnd != -1) {
                                        startToEnd = layoutParams.startToEnd;
                                        startToStart = leftToLeft;
                                    }
                                }
                            }
                        }
                        resolvedLeftToLeft = startToStart;
                        resolvedLeftToRight = startToEnd;
                        n = rightToLeft;
                        n2 = rightToRight;
                        resolveGoneLeftMargin = goneLeftMargin;
                        resolveGoneRightMargin = goneRightMargin;
                        resolvedHorizontalBias = horizontalBias;
                        if (rightToLeft == -1) {
                            resolvedLeftToLeft = startToStart;
                            resolvedLeftToRight = startToEnd;
                            n = rightToLeft;
                            n2 = rightToRight;
                            resolveGoneLeftMargin = goneLeftMargin;
                            resolveGoneRightMargin = goneRightMargin;
                            resolvedHorizontalBias = horizontalBias;
                            if (rightToRight == -1) {
                                if (layoutParams.endToStart != -1) {
                                    n = layoutParams.endToStart;
                                    resolvedLeftToLeft = startToStart;
                                    resolvedLeftToRight = startToEnd;
                                    n2 = rightToRight;
                                    resolveGoneLeftMargin = goneLeftMargin;
                                    resolveGoneRightMargin = goneRightMargin;
                                    resolvedHorizontalBias = horizontalBias;
                                }
                                else {
                                    resolvedLeftToLeft = startToStart;
                                    resolvedLeftToRight = startToEnd;
                                    n = rightToLeft;
                                    n2 = rightToRight;
                                    resolveGoneLeftMargin = goneLeftMargin;
                                    resolveGoneRightMargin = goneRightMargin;
                                    resolvedHorizontalBias = horizontalBias;
                                    if (layoutParams.endToEnd != -1) {
                                        n2 = layoutParams.endToEnd;
                                        resolvedHorizontalBias = horizontalBias;
                                        resolveGoneRightMargin = goneRightMargin;
                                        resolveGoneLeftMargin = goneLeftMargin;
                                        n = rightToLeft;
                                        resolvedLeftToRight = startToEnd;
                                        resolvedLeftToLeft = startToStart;
                                    }
                                }
                            }
                        }
                    }
                    if (resolvedLeftToLeft != -1) {
                        final ConstraintWidget targetWidget = this.getTargetWidget(resolvedLeftToLeft);
                        if (targetWidget != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget, ConstraintAnchor.Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    }
                    else if (resolvedLeftToRight != -1) {
                        final ConstraintWidget targetWidget2 = this.getTargetWidget(resolvedLeftToRight);
                        if (targetWidget2 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.LEFT, targetWidget2, ConstraintAnchor.Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    }
                    if (n != -1) {
                        final ConstraintWidget targetWidget3 = this.getTargetWidget(n);
                        if (targetWidget3 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget3, ConstraintAnchor.Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    }
                    else if (n2 != -1) {
                        final ConstraintWidget targetWidget4 = this.getTargetWidget(n2);
                        if (targetWidget4 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.RIGHT, targetWidget4, ConstraintAnchor.Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    }
                    if (layoutParams.topToTop != -1) {
                        final ConstraintWidget targetWidget5 = this.getTargetWidget(layoutParams.topToTop);
                        if (targetWidget5 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget5, ConstraintAnchor.Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    }
                    else if (layoutParams.topToBottom != -1) {
                        final ConstraintWidget targetWidget6 = this.getTargetWidget(layoutParams.topToBottom);
                        if (targetWidget6 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.TOP, targetWidget6, ConstraintAnchor.Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    }
                    if (layoutParams.bottomToTop != -1) {
                        final ConstraintWidget targetWidget7 = this.getTargetWidget(layoutParams.bottomToTop);
                        if (targetWidget7 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget7, ConstraintAnchor.Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    }
                    else if (layoutParams.bottomToBottom != -1) {
                        final ConstraintWidget targetWidget8 = this.getTargetWidget(layoutParams.bottomToBottom);
                        if (targetWidget8 != null) {
                            viewWidget.immediateConnect(ConstraintAnchor.Type.BOTTOM, targetWidget8, ConstraintAnchor.Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    }
                    if (layoutParams.baselineToBaseline != -1) {
                        final View view = (View)this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                        final ConstraintWidget targetWidget9 = this.getTargetWidget(layoutParams.baselineToBaseline);
                        if (targetWidget9 != null && view != null && view.getLayoutParams() instanceof LayoutParams) {
                            final LayoutParams layoutParams2 = (LayoutParams)view.getLayoutParams();
                            layoutParams.needsBaseline = true;
                            layoutParams2.needsBaseline = true;
                            viewWidget.getAnchor(ConstraintAnchor.Type.BASELINE).connect(targetWidget9.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                            viewWidget.getAnchor(ConstraintAnchor.Type.TOP).reset();
                            viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                        }
                    }
                    if (resolvedHorizontalBias >= 0.0f && resolvedHorizontalBias != 0.5f) {
                        viewWidget.setHorizontalBiasPercent(resolvedHorizontalBias);
                    }
                    if (layoutParams.verticalBias >= 0.0f && layoutParams.verticalBias != 0.5f) {
                        viewWidget.setVerticalBiasPercent(layoutParams.verticalBias);
                    }
                    if (this.isInEditMode() && (layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1)) {
                        viewWidget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
                    }
                    if (!layoutParams.horizontalDimensionFixed) {
                        if (layoutParams.width == -1) {
                            viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = layoutParams.leftMargin;
                            viewWidget.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = layoutParams.rightMargin;
                        }
                        else {
                            viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget.setWidth(0);
                        }
                    }
                    else {
                        viewWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget.setWidth(layoutParams.width);
                    }
                    if (!layoutParams.verticalDimensionFixed) {
                        if (layoutParams.height == -1) {
                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                            viewWidget.getAnchor(ConstraintAnchor.Type.TOP).mMargin = layoutParams.topMargin;
                            viewWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = layoutParams.bottomMargin;
                        }
                        else {
                            viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                            viewWidget.setHeight(0);
                        }
                    }
                    else {
                        viewWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                        viewWidget.setHeight(layoutParams.height);
                    }
                    if (layoutParams.dimensionRatio != null) {
                        viewWidget.setDimensionRatio(layoutParams.dimensionRatio);
                    }
                    viewWidget.setHorizontalWeight(layoutParams.horizontalWeight);
                    viewWidget.setVerticalWeight(layoutParams.verticalWeight);
                    viewWidget.setHorizontalChainStyle(layoutParams.horizontalChainStyle);
                    viewWidget.setVerticalChainStyle(layoutParams.verticalChainStyle);
                    viewWidget.setHorizontalMatchStyle(layoutParams.matchConstraintDefaultWidth, layoutParams.matchConstraintMinWidth, layoutParams.matchConstraintMaxWidth);
                    viewWidget.setVerticalMatchStyle(layoutParams.matchConstraintDefaultHeight, layoutParams.matchConstraintMinHeight, layoutParams.matchConstraintMaxHeight);
                }
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
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        if (Build$VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
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
    
    protected void onLayout(final boolean b, int i, int childCount, int drawX, int drawY) {
        childCount = this.getChildCount();
        final boolean inEditMode = this.isInEditMode();
        View child;
        LayoutParams layoutParams;
        ConstraintWidget widget;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            if (child.getVisibility() != 8 || layoutParams.isGuideline || inEditMode) {
                widget = layoutParams.widget;
                drawX = widget.getDrawX();
                drawY = widget.getDrawY();
                child.layout(drawX, drawY, widget.getWidth() + drawX, widget.getHeight() + drawY);
            }
        }
    }
    
    protected void onMeasure(int b, int resolveSizeAndState) {
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        this.mLayoutWidget.setX(paddingLeft);
        this.mLayoutWidget.setY(paddingTop);
        this.setSelfDimensionBehaviour(b, resolveSizeAndState);
        final boolean mDirtyHierarchy = this.mDirtyHierarchy;
        int i = 0;
        if (mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
        }
        this.internalMeasureChildren(b, resolveSizeAndState);
        if (this.getChildCount() > 0) {
            this.solveLinearSystem();
        }
        final int size = this.mVariableDimensionsWidgets.size();
        final int n = paddingTop + this.getPaddingBottom();
        final int n2 = paddingLeft + this.getPaddingRight();
        int n10;
        if (size > 0) {
            final boolean b2 = this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            final boolean b3 = this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            int n4;
            int n3 = n4 = 0;
            while (i < size) {
                final ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(i);
                int n9 = 0;
                int combineMeasuredStates = 0;
                Label_0581: {
                    if (!(constraintWidget instanceof Guideline)) {
                        final View view = (View)constraintWidget.getCompanionWidget();
                        if (view != null) {
                            if (view.getVisibility() != 8) {
                                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                                int n5;
                                if (layoutParams.width == -2) {
                                    n5 = getChildMeasureSpec(b, n2, layoutParams.width);
                                }
                                else {
                                    n5 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
                                }
                                int n6;
                                if (layoutParams.height == -2) {
                                    n6 = getChildMeasureSpec(resolveSizeAndState, n, layoutParams.height);
                                }
                                else {
                                    n6 = View$MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
                                }
                                view.measure(n5, n6);
                                final int measuredWidth = view.getMeasuredWidth();
                                final int measuredHeight = view.getMeasuredHeight();
                                if (measuredWidth != constraintWidget.getWidth()) {
                                    constraintWidget.setWidth(measuredWidth);
                                    if (b2 && constraintWidget.getRight() > this.mLayoutWidget.getWidth()) {
                                        this.mLayoutWidget.setWidth(Math.max(this.mMinWidth, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()));
                                    }
                                    n3 = 1;
                                }
                                int n7 = n3;
                                if (measuredHeight != constraintWidget.getHeight()) {
                                    constraintWidget.setHeight(measuredHeight);
                                    if (b3 && constraintWidget.getBottom() > this.mLayoutWidget.getHeight()) {
                                        this.mLayoutWidget.setHeight(Math.max(this.mMinHeight, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()));
                                    }
                                    n7 = 1;
                                }
                                int n8 = n7;
                                if (layoutParams.needsBaseline) {
                                    final int baseline = view.getBaseline();
                                    n8 = n7;
                                    if (baseline != -1) {
                                        n8 = n7;
                                        if (baseline != constraintWidget.getBaselineDistance()) {
                                            constraintWidget.setBaselineDistance(baseline);
                                            n8 = 1;
                                        }
                                    }
                                }
                                n9 = n8;
                                combineMeasuredStates = n4;
                                if (Build$VERSION.SDK_INT >= 11) {
                                    combineMeasuredStates = combineMeasuredStates(n4, view.getMeasuredState());
                                    n9 = n8;
                                }
                                break Label_0581;
                            }
                        }
                    }
                    n9 = n3;
                    combineMeasuredStates = n4;
                }
                ++i;
                n3 = n9;
                n4 = combineMeasuredStates;
            }
            n10 = n4;
            if (n3 != 0) {
                this.solveLinearSystem();
                n10 = n4;
            }
        }
        else {
            n10 = 0;
        }
        final int n11 = this.mLayoutWidget.getWidth() + n2;
        final int n12 = this.mLayoutWidget.getHeight() + n;
        if (Build$VERSION.SDK_INT >= 11) {
            b = resolveSizeAndState(n11, b, n10);
            resolveSizeAndState = resolveSizeAndState(n12, resolveSizeAndState, n10 << 16);
            b = Math.min(this.mMaxWidth, b);
            final int min = Math.min(this.mMaxHeight, resolveSizeAndState);
            resolveSizeAndState = (b & 0xFFFFFF);
            final int n13 = min & 0xFFFFFF;
            b = resolveSizeAndState;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                b = (resolveSizeAndState | 0x1000000);
            }
            resolveSizeAndState = n13;
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                resolveSizeAndState = (n13 | 0x1000000);
            }
            this.setMeasuredDimension(b, resolveSizeAndState);
        }
        else {
            this.setMeasuredDimension(n11, n12);
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
            final ConstraintWidget widget = layoutParams.widget;
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }
    
    public void onViewRemoved(final View view) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(this.getViewWidget(view));
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
    }
    
    public void setConstraintSet(final ConstraintSet mConstraintSet) {
        this.mConstraintSet = mConstraintSet;
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
    
    protected void solveLinearSystem() {
        this.mLayoutWidget.layout();
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline;
        public int bottomToBottom;
        public int bottomToTop;
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
        public float horizontalBias;
        public int horizontalChainStyle;
        boolean horizontalDimensionFixed;
        public float horizontalWeight;
        boolean isGuideline;
        public int leftToLeft;
        public int leftToRight;
        public int matchConstraintDefaultHeight;
        public int matchConstraintDefaultWidth;
        public int matchConstraintMaxHeight;
        public int matchConstraintMaxWidth;
        public int matchConstraintMinHeight;
        public int matchConstraintMinWidth;
        boolean needsBaseline;
        public int orientation;
        int resolveGoneLeftMargin;
        int resolveGoneRightMargin;
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
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
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
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            final int indexCount = ((TypedArray)obtainStyledAttributes).getIndexCount();
            int n = 0;
        Label_1792_Outer:
            while (true) {
                Label_1798: {
                    if (n >= indexCount) {
                        break Label_1798;
                    }
                    final int index = ((TypedArray)obtainStyledAttributes).getIndex(n);
                    int length;
                    int index2;
                    String substring;
                    int index3;
                    String substring2;
                    String substring3;
                    float float1;
                    float float2;
                    int constraintLayout_Layout_layout_constraintBaseline_creator;
                    String substring4;
                    Block_58_Outer:Block_62_Outer:Block_68_Outer:
                    while (true) {
                        Label_1483: {
                            if (index == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
                                this.leftToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToLeft);
                                if (this.leftToLeft == -1) {
                                    this.leftToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
                                this.leftToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.leftToRight);
                                if (this.leftToRight == -1) {
                                    this.leftToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
                                this.rightToLeft = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToLeft);
                                if (this.rightToLeft == -1) {
                                    this.rightToLeft = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
                                this.rightToRight = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.rightToRight);
                                if (this.rightToRight == -1) {
                                    this.rightToRight = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
                                this.topToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToTop);
                                if (this.topToTop == -1) {
                                    this.topToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
                                this.topToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.topToBottom);
                                if (this.topToBottom == -1) {
                                    this.topToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
                                this.bottomToTop = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToTop);
                                if (this.bottomToTop == -1) {
                                    this.bottomToTop = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
                                this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.bottomToBottom);
                                if (this.bottomToBottom == -1) {
                                    this.bottomToBottom = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
                                this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.baselineToBaseline);
                                if (this.baselineToBaseline == -1) {
                                    this.baselineToBaseline = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                    break Label_1792;
                                }
                                break Label_1792;
                            }
                            else {
                                if (index == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
                                    this.editorAbsoluteX = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteX);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
                                    this.editorAbsoluteY = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.editorAbsoluteY);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
                                    this.guideBegin = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideBegin);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
                                    this.guideEnd = ((TypedArray)obtainStyledAttributes).getDimensionPixelOffset(index, this.guideEnd);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
                                    this.guidePercent = ((TypedArray)obtainStyledAttributes).getFloat(index, this.guidePercent);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_android_orientation) {
                                    this.orientation = ((TypedArray)obtainStyledAttributes).getInt(index, this.orientation);
                                    break Label_1792;
                                }
                                if (index == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
                                    this.startToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToEnd);
                                    if (this.startToEnd == -1) {
                                        this.startToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                        break Label_1792;
                                    }
                                    break Label_1792;
                                }
                                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
                                    this.startToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.startToStart);
                                    if (this.startToStart == -1) {
                                        this.startToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                        break Label_1792;
                                    }
                                    break Label_1792;
                                }
                                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
                                    this.endToStart = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToStart);
                                    if (this.endToStart == -1) {
                                        this.endToStart = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                        break Label_1792;
                                    }
                                    break Label_1792;
                                }
                                else if (index == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
                                    this.endToEnd = ((TypedArray)obtainStyledAttributes).getResourceId(index, this.endToEnd);
                                    if (this.endToEnd == -1) {
                                        this.endToEnd = ((TypedArray)obtainStyledAttributes).getInt(index, -1);
                                        break Label_1792;
                                    }
                                    break Label_1792;
                                }
                                else {
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
                                        this.goneLeftMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneLeftMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
                                        this.goneTopMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneTopMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
                                        this.goneRightMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneRightMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
                                        this.goneBottomMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneBottomMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
                                        this.goneStartMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneStartMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
                                        this.goneEndMargin = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.goneEndMargin);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
                                        this.horizontalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.horizontalBias);
                                        break Label_1792;
                                    }
                                    if (index == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
                                        this.verticalBias = ((TypedArray)obtainStyledAttributes).getFloat(index, this.verticalBias);
                                        break Label_1792;
                                    }
                                    if (index != R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
                                        break Label_1483;
                                    }
                                    this.dimensionRatio = ((TypedArray)obtainStyledAttributes).getString(index);
                                    this.dimensionRatioValue = Float.NaN;
                                    this.dimensionRatioSide = -1;
                                    if (this.dimensionRatio == null) {
                                        break Label_1792;
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
                                        break Label_1483;
                                    }
                                    substring2 = this.dimensionRatio.substring(index2, index3);
                                    substring3 = this.dimensionRatio.substring(index3 + 1);
                                    if (substring2.length() <= 0 || substring3.length() <= 0) {
                                        break Label_1792;
                                    }
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
                                continue Label_1792_Outer;
                                // iftrue(Label_1693:, index != R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max)
                                // iftrue(Label_1533:, index != R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight)
                                // iftrue(Label_1718:, index != R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min)
                                // iftrue(Label_1743:, index != R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max)
                                // iftrue(Label_1787:, index != R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator)
                                // iftrue(Label_1599:, index != R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle)
                                // iftrue(Label_1754:, index != R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator)
                                // iftrue(Label_1577:, index != R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle)
                                // iftrue(Label_1776:, index != R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator)
                                // iftrue(Label_1668:, index != R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min)
                                // iftrue(Label_1621:, index != R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default)
                                // iftrue(Label_1792:, substring4.length() <= 0)
                                // iftrue(Label_1555:, index != R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight)
                                // iftrue(Label_1643:, index != R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default)
                                while (true) {
                                    while (true) {
                                        Block_66: {
                                            Block_57_Outer:Block_60_Outer:Block_63_Outer:Block_56_Outer:
                                            while (true) {
                                                this.verticalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, 0.0f);
                                                continue Block_58_Outer;
                                                while (true) {
                                                Block_69:
                                                    while (true) {
                                                    Block_59_Outer:
                                                        while (true) {
                                                            while (true) {
                                                                while (true) {
                                                                    while (true) {
                                                                        this.horizontalWeight = ((TypedArray)obtainStyledAttributes).getFloat(index, 0.0f);
                                                                        continue Block_58_Outer;
                                                                        this.verticalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                                                        continue Block_58_Outer;
                                                                        Label_1668: {
                                                                            this.matchConstraintMaxWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxWidth);
                                                                        }
                                                                        continue Block_58_Outer;
                                                                        continue Block_60_Outer;
                                                                    }
                                                                    Block_65: {
                                                                        break Block_65;
                                                                        this.matchConstraintMinWidth = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinWidth);
                                                                        continue Block_58_Outer;
                                                                    }
                                                                    this.matchConstraintMinHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMinHeight);
                                                                    continue Block_58_Outer;
                                                                    this.horizontalChainStyle = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                                                    continue Block_58_Outer;
                                                                    Label_1718: {
                                                                        break Block_66;
                                                                    }
                                                                    Label_1776:
                                                                    break Block_57_Outer;
                                                                    this.matchConstraintDefaultHeight = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                                                    continue Block_58_Outer;
                                                                    Label_1787:
                                                                    constraintLayout_Layout_layout_constraintBaseline_creator = R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator;
                                                                    continue Block_58_Outer;
                                                                    this.matchConstraintDefaultWidth = ((TypedArray)obtainStyledAttributes).getInt(index, 0);
                                                                    continue Block_58_Outer;
                                                                    Label_1577:
                                                                    continue Block_63_Outer;
                                                                }
                                                                Label_1743: {
                                                                    break Block_69;
                                                                }
                                                                Label_1555:
                                                                continue Block_62_Outer;
                                                            }
                                                            Label_1765: {
                                                                break Block_69;
                                                            }
                                                            this.dimensionRatioValue = Float.parseFloat(substring4);
                                                            continue Block_58_Outer;
                                                            Label_1643:
                                                            continue Block_59_Outer;
                                                        }
                                                        Label_1599: {
                                                            continue Block_56_Outer;
                                                        }
                                                    }
                                                    continue Block_58_Outer;
                                                    substring4 = this.dimensionRatio.substring(index2);
                                                    continue;
                                                }
                                                Label_1533: {
                                                    continue Block_57_Outer;
                                                }
                                            }
                                            continue Block_58_Outer;
                                            ((TypedArray)obtainStyledAttributes).recycle();
                                            this.validate();
                                            return;
                                        }
                                        this.matchConstraintMaxHeight = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(index, this.matchConstraintMaxHeight);
                                        continue Block_58_Outer;
                                        Label_1621: {
                                            continue Block_68_Outer;
                                        }
                                    }
                                    Label_1754: {
                                        continue;
                                    }
                                }
                            }
                            // iftrue(Label_1765:, index != R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator)
                            catch (NumberFormatException ex) {
                                continue;
                            }
                        }
                        break;
                    }
                }
            }
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
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
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.guidePercent = layoutParams.guidePercent;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.goneLeftMargin = layoutParams.goneLeftMargin;
            this.goneTopMargin = layoutParams.goneTopMargin;
            this.goneRightMargin = layoutParams.goneRightMargin;
            this.goneBottomMargin = layoutParams.goneBottomMargin;
            this.goneStartMargin = layoutParams.goneStartMargin;
            this.goneEndMargin = layoutParams.goneEndMargin;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.dimensionRatioValue = layoutParams.dimensionRatioValue;
            this.dimensionRatioSide = layoutParams.dimensionRatioSide;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.matchConstraintDefaultWidth = layoutParams.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = layoutParams.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = layoutParams.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = layoutParams.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = layoutParams.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = layoutParams.matchConstraintMaxHeight;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
            this.verticalDimensionFixed = layoutParams.verticalDimensionFixed;
            this.needsBaseline = layoutParams.needsBaseline;
            this.isGuideline = layoutParams.isGuideline;
            this.resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            this.resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            this.resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            this.resolvedRightToRight = layoutParams.resolvedRightToRight;
            this.resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            this.resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            this.widget = layoutParams.widget;
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
            this.horizontalWeight = 0.0f;
            this.verticalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.matchConstraintDefaultWidth = 0;
            this.matchConstraintDefaultHeight = 0;
            this.matchConstraintMinWidth = 0;
            this.matchConstraintMinHeight = 0;
            this.matchConstraintMaxWidth = 0;
            this.matchConstraintMaxHeight = 0;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            this.needsBaseline = false;
            this.isGuideline = false;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolvedHorizontalBias = 0.5f;
            this.widget = new ConstraintWidget();
        }
        
        @TargetApi(17)
        public void resolveLayoutDirection(int n) {
            super.resolveLayoutDirection(n);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            final int layoutDirection = this.getLayoutDirection();
            n = 1;
            if (layoutDirection == 0) {
                n = 0;
            }
            if (n != 0) {
                if (this.startToEnd != -1) {
                    this.resolvedRightToLeft = this.startToEnd;
                }
                else if (this.startToStart != -1) {
                    this.resolvedRightToRight = this.startToStart;
                }
                if (this.endToStart != -1) {
                    this.resolvedLeftToRight = this.endToStart;
                }
                if (this.endToEnd != -1) {
                    this.resolvedLeftToLeft = this.endToEnd;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneRightMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneEndMargin;
                }
                this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
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
            if (this.endToStart == -1 && this.endToEnd == -1) {
                if (this.rightToLeft != -1) {
                    this.resolvedRightToLeft = this.rightToLeft;
                }
                else if (this.rightToRight != -1) {
                    this.resolvedRightToRight = this.rightToRight;
                }
            }
            if (this.startToStart == -1 && this.startToEnd == -1) {
                if (this.leftToLeft != -1) {
                    this.resolvedLeftToLeft = this.leftToLeft;
                }
                else if (this.leftToRight != -1) {
                    this.resolvedLeftToRight = this.leftToRight;
                }
            }
        }
        
        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
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
    }
}
