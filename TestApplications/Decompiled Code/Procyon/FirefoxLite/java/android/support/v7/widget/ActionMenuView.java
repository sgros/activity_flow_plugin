// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuItemImpl;
import android.graphics.drawable.Drawable;
import android.view.Menu;
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
        this.mGeneratedItemPadding = (int)(density * 4.0f);
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
        boolean expandable = true;
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
        if (layoutParams.isOverflowButton || n2 == 0) {
            expandable = false;
        }
        layoutParams.expandable = expandable;
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(n * cellsUsed, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int mMinCellSize) {
        final int mode = View$MeasureSpec.getMode(mMinCellSize);
        final int size = View$MeasureSpec.getSize(i);
        final int size2 = View$MeasureSpec.getSize(mMinCellSize);
        final int paddingLeft = this.getPaddingLeft();
        i = this.getPaddingRight();
        final int n = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(mMinCellSize, n, -2);
        final int n2 = size - (paddingLeft + i);
        i = n2 / this.mMinCellSize;
        mMinCellSize = this.mMinCellSize;
        if (i == 0) {
            this.setMeasuredDimension(n2, 0);
            return;
        }
        final int n3 = this.mMinCellSize + n2 % mMinCellSize / i;
        final int childCount = this.getChildCount();
        int j = 0;
        int max = 0;
        boolean b = false;
        int n4 = 0;
        int max2 = 0;
        int n5 = 0;
        long k = 0L;
        mMinCellSize = size2;
        while (j < childCount) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final boolean b2 = child instanceof ActionMenuItemView;
                ++n4;
                if (b2) {
                    child.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b2 && ((ActionMenuItemView)child).hasText());
                int n6;
                if (layoutParams.isOverflowButton) {
                    n6 = 1;
                }
                else {
                    n6 = i;
                }
                final int measureChildForCells = measureChildForCells(child, n3, n6, childMeasureSpec, n);
                max2 = Math.max(max2, measureChildForCells);
                int n7 = n5;
                if (layoutParams.expandable) {
                    n7 = n5 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    b = true;
                }
                i -= measureChildForCells;
                max = Math.max(max, child.getMeasuredHeight());
                if (measureChildForCells == 1) {
                    k |= 1 << j;
                }
                n5 = n7;
            }
            ++j;
        }
        final boolean b3 = b && n4 == 2;
        final int n8 = 0;
        int n9 = i;
        final int n10 = childCount;
        final int n11 = childMeasureSpec;
        i = n8;
        while (true) {
            while (n5 > 0 && n9 > 0) {
                int l = 0;
                int n12 = 0;
                int n13 = Integer.MAX_VALUE;
                long n14 = 0L;
                while (l < n10) {
                    final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                    int n15;
                    int cellsUsed;
                    long n16;
                    if (!layoutParams2.expandable) {
                        n15 = n12;
                        cellsUsed = n13;
                        n16 = n14;
                    }
                    else if (layoutParams2.cellsUsed < n13) {
                        cellsUsed = layoutParams2.cellsUsed;
                        n16 = 1L << l;
                        n15 = 1;
                    }
                    else {
                        n15 = n12;
                        cellsUsed = n13;
                        n16 = n14;
                        if (layoutParams2.cellsUsed == n13) {
                            n16 = (n14 | 1L << l);
                            n15 = n12 + 1;
                            cellsUsed = n13;
                        }
                    }
                    ++l;
                    n12 = n15;
                    n13 = cellsUsed;
                    n14 = n16;
                }
                k |= n14;
                if (n12 > n9) {
                    final boolean b4 = !b && n4 == 1;
                    if (n9 > 0 && k != 0L && (n9 < n4 - 1 || b4 || max2 > 1)) {
                        float n17 = (float)Long.bitCount(k);
                        if (!b4) {
                            float n18;
                            if ((k & 0x1L) != 0x0L) {
                                n18 = n17;
                                if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                                    n18 = n17 - 0.5f;
                                }
                            }
                            else {
                                n18 = n17;
                            }
                            final int n19 = n10 - 1;
                            n17 = n18;
                            if ((k & (long)(1 << n19)) != 0x0L) {
                                n17 = n18;
                                if (!((LayoutParams)this.getChildAt(n19).getLayoutParams()).preventEdgeOffset) {
                                    n17 = n18 - 0.5f;
                                }
                            }
                        }
                        int n20;
                        if (n17 > 0.0f) {
                            n20 = (int)(n9 * n3 / n17);
                        }
                        else {
                            n20 = 0;
                        }
                        int n23;
                        for (int n21 = n10, n22 = 0; n22 < n21; ++n22, i = n23) {
                            if ((k & (long)(1 << n22)) == 0x0L) {
                                n23 = i;
                            }
                            else {
                                final View child2 = this.getChildAt(n22);
                                final LayoutParams layoutParams3 = (LayoutParams)child2.getLayoutParams();
                                if (child2 instanceof ActionMenuItemView) {
                                    layoutParams3.extraPixels = n20;
                                    layoutParams3.expanded = true;
                                    if (n22 == 0 && !layoutParams3.preventEdgeOffset) {
                                        layoutParams3.leftMargin = -n20 / 2;
                                    }
                                }
                                else if (layoutParams3.isOverflowButton) {
                                    layoutParams3.extraPixels = n20;
                                    layoutParams3.expanded = true;
                                    layoutParams3.rightMargin = -n20 / 2;
                                }
                                else {
                                    if (n22 != 0) {
                                        layoutParams3.leftMargin = n20 / 2;
                                    }
                                    n23 = i;
                                    if (n22 != n21 - 1) {
                                        layoutParams3.rightMargin = n20 / 2;
                                        n23 = i;
                                    }
                                    continue;
                                }
                                n23 = 1;
                            }
                        }
                    }
                    final int n24 = 0;
                    if (i != 0) {
                        View child3;
                        LayoutParams layoutParams4;
                        for (i = n24; i < n10; ++i) {
                            child3 = this.getChildAt(i);
                            layoutParams4 = (LayoutParams)child3.getLayoutParams();
                            if (layoutParams4.expanded) {
                                child3.measure(View$MeasureSpec.makeMeasureSpec(layoutParams4.cellsUsed * n3 + layoutParams4.extraPixels, 1073741824), n11);
                            }
                        }
                    }
                    if (mode != 1073741824) {
                        mMinCellSize = max;
                    }
                    this.setMeasuredDimension(n2, mMinCellSize);
                    return;
                }
                View child4;
                LayoutParams layoutParams5;
                long n25;
                int n26;
                long n27;
                for (i = 0; i < n10; ++i, n9 = n26, k = n27) {
                    child4 = this.getChildAt(i);
                    layoutParams5 = (LayoutParams)child4.getLayoutParams();
                    n25 = 1 << i;
                    if ((n14 & n25) == 0x0L) {
                        n26 = n9;
                        n27 = k;
                        if (layoutParams5.cellsUsed == n13 + 1) {
                            n27 = (k | n25);
                            n26 = n9;
                        }
                    }
                    else {
                        if (b3 && layoutParams5.preventEdgeOffset && n9 == 1) {
                            child4.setPadding(this.mGeneratedItemPadding + n3, 0, this.mGeneratedItemPadding, 0);
                        }
                        ++layoutParams5.cellsUsed;
                        layoutParams5.expanded = true;
                        n26 = n9 - 1;
                        n27 = k;
                    }
                }
                i = 1;
            }
            continue;
        }
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
    
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    public int getWindowAnimations() {
        return 0;
    }
    
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
    
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }
    
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
    protected void onLayout(final boolean b, int i, int j, int n, int n2) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, j, n, n2);
            return;
        }
        final int childCount = this.getChildCount();
        final int n3 = (n2 - j) / 2;
        final int dividerWidth = this.getDividerWidth();
        final int n4 = n - i;
        j = this.getPaddingRight();
        i = this.getPaddingLeft();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        i = n4 - j - i;
        j = 0;
        n2 = 0;
        n = 0;
        while (j < childCount) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    final int n5 = n2 = child.getMeasuredWidth();
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n2 = n5 + dividerWidth;
                    }
                    final int measuredHeight = child.getMeasuredHeight();
                    int n6;
                    int n7;
                    if (layoutRtl) {
                        n6 = this.getPaddingLeft() + layoutParams.leftMargin;
                        n7 = n6 + n2;
                    }
                    else {
                        n7 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                        n6 = n7 - n2;
                    }
                    final int n8 = n3 - measuredHeight / 2;
                    child.layout(n6, n8, n7, measuredHeight + n8);
                    i -= n2;
                    n2 = 1;
                }
                else {
                    i -= child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    this.hasSupportDividerBeforeChildAt(j);
                    ++n;
                }
            }
            ++j;
        }
        if (childCount == 1 && n2 == 0) {
            final View child2 = this.getChildAt(0);
            i = child2.getMeasuredWidth();
            j = child2.getMeasuredHeight();
            n = n4 / 2 - i / 2;
            n2 = n3 - j / 2;
            child2.layout(n, n2, i + n, j + n2);
            return;
        }
        j = n - (n2 ^ 0x1);
        if (j > 0) {
            i /= j;
        }
        else {
            i = 0;
        }
        j = 0;
        n = 0;
        n2 = Math.max(0, i);
        if (layoutRtl) {
            j = this.getWidth() - this.getPaddingRight();
            View child3;
            LayoutParams layoutParams2;
            int measuredHeight2;
            int n9;
            for (i = n; i < childCount; ++i, j = n) {
                child3 = this.getChildAt(i);
                layoutParams2 = (LayoutParams)child3.getLayoutParams();
                n = j;
                if (child3.getVisibility() != 8) {
                    if (layoutParams2.isOverflowButton) {
                        n = j;
                    }
                    else {
                        n = j - layoutParams2.rightMargin;
                        j = child3.getMeasuredWidth();
                        measuredHeight2 = child3.getMeasuredHeight();
                        n9 = n3 - measuredHeight2 / 2;
                        child3.layout(n - j, n9, n, measuredHeight2 + n9);
                        n -= j + layoutParams2.leftMargin + n2;
                    }
                }
            }
        }
        else {
            n = this.getPaddingLeft();
            View child4;
            LayoutParams layoutParams3;
            int n10;
            int measuredWidth;
            for (i = j; i < childCount; ++i, n = j) {
                child4 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child4.getLayoutParams();
                j = n;
                if (child4.getVisibility() != 8) {
                    if (layoutParams3.isOverflowButton) {
                        j = n;
                    }
                    else {
                        n10 = n + layoutParams3.leftMargin;
                        measuredWidth = child4.getMeasuredWidth();
                        n = child4.getMeasuredHeight();
                        j = n3 - n / 2;
                        child4.layout(n10, j, n10 + measuredWidth, n + j);
                        j = n10 + (measuredWidth + layoutParams3.rightMargin + n2);
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
    
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPopupTheme(final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) == 0) {
                this.mPopupContext = this.getContext();
            }
            else {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
            }
        }
    }
    
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }
    
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
