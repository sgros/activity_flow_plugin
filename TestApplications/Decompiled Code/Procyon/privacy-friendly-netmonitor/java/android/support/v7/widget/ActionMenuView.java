// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.support.annotation.RestrictTo;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuBuilder;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView
{
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context mPopupContext, final AttributeSet set) {
        super(mPopupContext, set);
        this.setBaselineAligned(false);
        final float density = mPopupContext.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        this.mPopupContext = mPopupContext;
        this.mPopupTheme = 0;
    }
    
    static int measureChildForCells(final View view, final int n, int cellsUsed, int n2, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2) - n3, View$MeasureSpec.getMode(n2));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        final boolean b = false;
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        n3 = 2;
        if (cellsUsed > 0 && (n2 == 0 || cellsUsed >= 2)) {
            view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, Integer.MIN_VALUE), measureSpec);
            final int measuredWidth = view.getMeasuredWidth();
            final int n4 = cellsUsed = measuredWidth / n;
            if (measuredWidth % n != 0) {
                cellsUsed = n4 + 1;
            }
            if (n2 != 0 && cellsUsed < 2) {
                cellsUsed = n3;
            }
        }
        else {
            cellsUsed = 0;
        }
        boolean expandable = b;
        if (!layoutParams.isOverflowButton) {
            expandable = b;
            if (n2 != 0) {
                expandable = true;
            }
        }
        layoutParams.expandable = expandable;
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(n * cellsUsed, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int n) {
        final int mode = View$MeasureSpec.getMode(n);
        final int size = View$MeasureSpec.getSize(i);
        final int size2 = View$MeasureSpec.getSize(n);
        final int paddingLeft = this.getPaddingLeft();
        i = this.getPaddingRight();
        final int n2 = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(n, n2, -2);
        final int n3 = size - (paddingLeft + i);
        final int n4 = n3 / this.mMinCellSize;
        i = this.mMinCellSize;
        if (n4 == 0) {
            this.setMeasuredDimension(n3, 0);
            return;
        }
        final int n5 = this.mMinCellSize + n3 % i / n4;
        final int childCount = this.getChildCount();
        int max;
        int j = max = 0;
        int n6 = n = max;
        int n7;
        i = (n7 = n);
        long k = 0L;
        int a = i;
        i = n4;
        int n8 = n;
        n = size2;
        while (j < childCount) {
            final View child = this.getChildAt(j);
            int n9;
            int max2;
            if (child.getVisibility() == 8) {
                n9 = n8;
                max2 = a;
            }
            else {
                final boolean b = child instanceof ActionMenuItemView;
                n9 = n8 + 1;
                if (b) {
                    child.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b && ((ActionMenuItemView)child).hasText());
                int n10;
                if (layoutParams.isOverflowButton) {
                    n10 = 1;
                }
                else {
                    n10 = i;
                }
                final int measureChildForCells = measureChildForCells(child, n5, n10, childMeasureSpec, n2);
                max2 = Math.max(a, measureChildForCells);
                int n11 = n7;
                if (layoutParams.expandable) {
                    n11 = n7 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    n6 = 1;
                }
                i -= measureChildForCells;
                max = Math.max(max, child.getMeasuredHeight());
                if (measureChildForCells == 1) {
                    k |= 1 << j;
                    n7 = n11;
                }
                else {
                    n7 = n11;
                }
            }
            ++j;
            n8 = n9;
            a = max2;
        }
        final boolean b2 = n6 != 0 && n8 == 2;
        final int n12 = 0;
        int n13 = i;
        final int n14 = childCount;
        final int n15 = childMeasureSpec;
        i = n12;
        while (n7 > 0 && n13 > 0) {
            int n16 = Integer.MAX_VALUE;
            int l = 0;
            int n17 = 0;
            long n18 = 0L;
            while (l < n14) {
                final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                int n19;
                int cellsUsed;
                long n20;
                if (!layoutParams2.expandable) {
                    n19 = n17;
                    cellsUsed = n16;
                    n20 = n18;
                }
                else if (layoutParams2.cellsUsed < n16) {
                    cellsUsed = layoutParams2.cellsUsed;
                    n20 = 1 << l;
                    n19 = 1;
                }
                else {
                    n19 = n17;
                    cellsUsed = n16;
                    n20 = n18;
                    if (layoutParams2.cellsUsed == n16) {
                        final long n21 = 1 << l;
                        n19 = n17 + 1;
                        n20 = (n18 | n21);
                        cellsUsed = n16;
                    }
                }
                ++l;
                n17 = n19;
                n16 = cellsUsed;
                n18 = n20;
            }
            k |= n18;
            if (n17 > n13) {
                break;
            }
            long n24;
            for (int n22 = 0; n22 < n14; ++n22, n13 = i, k = n24) {
                final View child2 = this.getChildAt(n22);
                final LayoutParams layoutParams3 = (LayoutParams)child2.getLayoutParams();
                final long n23 = 1 << n22;
                if ((n18 & n23) == 0x0L) {
                    i = n13;
                    n24 = k;
                    if (layoutParams3.cellsUsed == n16 + 1) {
                        n24 = (k | n23);
                        i = n13;
                    }
                }
                else {
                    if (b2 && layoutParams3.preventEdgeOffset && n13 == 1) {
                        child2.setPadding(this.mGeneratedItemPadding + n5, 0, this.mGeneratedItemPadding, 0);
                    }
                    ++layoutParams3.cellsUsed;
                    layoutParams3.expanded = true;
                    i = n13 - 1;
                    n24 = k;
                }
            }
            i = 1;
        }
        final boolean b3 = n6 == 0 && n8 == 1;
        int n31;
        if (n13 > 0 && k != 0L && (n13 < n8 - 1 || b3 || a > 1)) {
            float n25 = (float)Long.bitCount(k);
            if (!b3) {
                float n26;
                if ((k & 0x1L) != 0x0L) {
                    n26 = n25;
                    if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        n26 = n25 - 0.5f;
                    }
                }
                else {
                    n26 = n25;
                }
                final int n27 = n14 - 1;
                n25 = n26;
                if ((k & (long)(1 << n27)) != 0x0L) {
                    n25 = n26;
                    if (!((LayoutParams)this.getChildAt(n27).getLayoutParams()).preventEdgeOffset) {
                        n25 = n26 - 0.5f;
                    }
                }
            }
            int n28;
            if (n25 > 0.0f) {
                n28 = (int)(n13 * n5 / n25);
            }
            else {
                n28 = 0;
            }
            int n29 = 0;
            final int n30 = n14;
            while (true) {
                n31 = i;
                if (n29 >= n30) {
                    break;
                }
                int n32;
                if ((k & (long)(1 << n29)) == 0x0L) {
                    n32 = i;
                }
                else {
                    final View child3 = this.getChildAt(n29);
                    final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                    if (child3 instanceof ActionMenuItemView) {
                        layoutParams4.extraPixels = n28;
                        layoutParams4.expanded = true;
                        if (n29 == 0 && !layoutParams4.preventEdgeOffset) {
                            layoutParams4.leftMargin = -n28 / 2;
                        }
                        n32 = 1;
                    }
                    else if (layoutParams4.isOverflowButton) {
                        layoutParams4.extraPixels = n28;
                        layoutParams4.expanded = true;
                        layoutParams4.rightMargin = -n28 / 2;
                        n32 = 1;
                    }
                    else {
                        if (n29 != 0) {
                            layoutParams4.leftMargin = n28 / 2;
                        }
                        n32 = i;
                        if (n29 != n30 - 1) {
                            layoutParams4.rightMargin = n28 / 2;
                            n32 = i;
                        }
                    }
                }
                ++n29;
                i = n32;
            }
        }
        else {
            n31 = i;
        }
        i = 0;
        if (n31 != 0) {
            while (i < n14) {
                final View child4 = this.getChildAt(i);
                final LayoutParams layoutParams5 = (LayoutParams)child4.getLayoutParams();
                if (layoutParams5.expanded) {
                    child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n5 + layoutParams5.extraPixels, 1073741824), n15);
                }
                ++i;
            }
        }
        if (mode != 1073741824) {
            n = max;
        }
        this.setMeasuredDimension(n3, n);
    }
    
    @Override
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
        }
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams != null) {
            LayoutParams layoutParams;
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            else {
                layoutParams = new LayoutParams(viewGroup$LayoutParams);
            }
            if (layoutParams.gravity <= 0) {
                layoutParams.gravity = 16;
            }
            return layoutParams;
        }
        return this.generateDefaultLayoutParams();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    public Menu getMenu() {
        if (this.mMenu == null) {
            final Context context = this.getContext();
            (this.mMenu = new MenuBuilder(context)).setCallback((MenuBuilder.Callback)new MenuBuilderCallback());
            (this.mPresenter = new ActionMenuPresenter(context)).setReserveOverflow(true);
            final ActionMenuPresenter mPresenter = this.mPresenter;
            MenuPresenter.Callback mActionMenuPresenterCallback;
            if (this.mActionMenuPresenterCallback != null) {
                mActionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            }
            else {
                mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            mPresenter.setCallback(mActionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return (Menu)this.mMenu;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        final boolean b = false;
        if (n == 0) {
            return false;
        }
        final View child = this.getChildAt(n - 1);
        final View child2 = this.getChildAt(n);
        boolean b2 = b;
        if (n < this.getChildCount()) {
            b2 = b;
            if (child instanceof ActionMenuChildView) {
                b2 = (false | ((ActionMenuChildView)child).needsDividerAfter());
            }
        }
        boolean b3 = b2;
        if (n > 0) {
            b3 = b2;
            if (child2 instanceof ActionMenuChildView) {
                b3 = (b2 | ((ActionMenuChildView)child2).needsDividerBefore());
            }
        }
        return b3;
    }
    
    public boolean hideOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.hideOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }
    
    @Override
    protected void onLayout(final boolean b, int i, int j, int measuredWidth, int n) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, j, measuredWidth, n);
            return;
        }
        final int childCount = this.getChildCount();
        final int n2 = (n - j) / 2;
        final int dividerWidth = this.getDividerWidth();
        final int n3 = measuredWidth - i;
        i = this.getPaddingRight();
        j = this.getPaddingLeft();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        i = n3 - i - j;
        j = 0;
        n = 0;
        measuredWidth = 0;
        while (j < childCount) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    final int n4 = n = child.getMeasuredWidth();
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n = n4 + dividerWidth;
                    }
                    final int measuredHeight = child.getMeasuredHeight();
                    int n5;
                    int n6;
                    if (layoutRtl) {
                        n5 = this.getPaddingLeft() + layoutParams.leftMargin;
                        n6 = n5 + n;
                    }
                    else {
                        n6 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                        n5 = n6 - n;
                    }
                    final int n7 = n2 - measuredHeight / 2;
                    child.layout(n5, n7, n6, measuredHeight + n7);
                    i -= n;
                    n = 1;
                }
                else {
                    i -= child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    this.hasSupportDividerBeforeChildAt(j);
                    ++measuredWidth;
                }
            }
            ++j;
        }
        if (childCount == 1 && n == 0) {
            final View child2 = this.getChildAt(0);
            i = child2.getMeasuredWidth();
            j = child2.getMeasuredHeight();
            measuredWidth = n3 / 2 - i / 2;
            n = n2 - j / 2;
            child2.layout(measuredWidth, n, i + measuredWidth, j + n);
            return;
        }
        j = measuredWidth - (n ^ 0x1);
        if (j > 0) {
            i /= j;
        }
        else {
            i = 0;
        }
        measuredWidth = 0;
        j = 0;
        n = Math.max(0, i);
        if (layoutRtl) {
            measuredWidth = this.getWidth() - this.getPaddingRight();
            View child3;
            LayoutParams layoutParams2;
            int measuredHeight2;
            int n8;
            for (i = j; i < childCount; ++i, measuredWidth = j) {
                child3 = this.getChildAt(i);
                layoutParams2 = (LayoutParams)child3.getLayoutParams();
                j = measuredWidth;
                if (child3.getVisibility() != 8) {
                    if (layoutParams2.isOverflowButton) {
                        j = measuredWidth;
                    }
                    else {
                        j = measuredWidth - layoutParams2.rightMargin;
                        measuredWidth = child3.getMeasuredWidth();
                        measuredHeight2 = child3.getMeasuredHeight();
                        n8 = n2 - measuredHeight2 / 2;
                        child3.layout(j - measuredWidth, n8, j, measuredHeight2 + n8);
                        j -= measuredWidth + layoutParams2.leftMargin + n;
                    }
                }
            }
        }
        else {
            j = this.getPaddingLeft();
            View child4;
            LayoutParams layoutParams3;
            int measuredHeight3;
            int n9;
            for (i = measuredWidth; i < childCount; ++i, j = measuredWidth) {
                child4 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child4.getLayoutParams();
                measuredWidth = j;
                if (child4.getVisibility() != 8) {
                    if (layoutParams3.isOverflowButton) {
                        measuredWidth = j;
                    }
                    else {
                        measuredWidth = j + layoutParams3.leftMargin;
                        j = child4.getMeasuredWidth();
                        measuredHeight3 = child4.getMeasuredHeight();
                        n9 = n2 - measuredHeight3 / 2;
                        child4.layout(measuredWidth, n9, measuredWidth + j, measuredHeight3 + n9);
                        measuredWidth += j + layoutParams3.rightMargin + n;
                    }
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        this.mFormatItems = (View$MeasureSpec.getMode(n) == 1073741824);
        if (mFormatItems != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        final int size = View$MeasureSpec.getSize(n);
        if (this.mFormatItems && this.mMenu != null && size != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = size;
            this.mMenu.onItemsChanged(true);
        }
        final int childCount = this.getChildCount();
        if (this.mFormatItems && childCount > 0) {
            this.onMeasureExactFormat(n, n2);
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
                layoutParams.rightMargin = 0;
                layoutParams.leftMargin = 0;
            }
            super.onMeasure(n, n2);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPopupTheme(@StyleRes final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) == 0) {
                this.mPopupContext = this.getContext();
            }
            else {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    private static class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        ActionMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            return false;
        }
    }
    
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
    }
    
    private class MenuBuilderCallback implements Callback
    {
        MenuBuilderCallback() {
        }
        
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
