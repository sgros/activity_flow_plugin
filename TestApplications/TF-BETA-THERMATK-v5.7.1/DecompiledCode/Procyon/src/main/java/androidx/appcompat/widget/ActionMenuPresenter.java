// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import androidx.core.graphics.drawable.DrawableCompat;
import android.view.View$OnTouchListener;
import android.util.AttributeSet;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.R$attr;
import androidx.appcompat.view.menu.MenuPopupHelper;
import android.view.ViewParent;
import androidx.appcompat.view.menu.SubMenuBuilder;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.appcompat.view.ActionBarPolicy;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import android.view.ViewGroup;
import android.view.MenuItem;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.R$layout;
import android.content.Context;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import androidx.core.view.ActionProvider;
import androidx.appcompat.view.menu.BaseMenuPresenter;

class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener
{
    private final SparseBooleanArray mActionButtonGroups;
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback;
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;
    
    public ActionMenuPresenter(final Context context) {
        super(context, R$layout.abc_action_menu_layout, R$layout.abc_action_menu_item_layout);
        this.mActionButtonGroups = new SparseBooleanArray();
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }
    
    private View findViewForItem(final MenuItem menuItem) {
        final ViewGroup viewGroup = (ViewGroup)super.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = viewGroup.getChildAt(i);
            if (child instanceof MenuView.ItemView && ((MenuView.ItemView)child).getItemData() == menuItem) {
                return child;
            }
        }
        return null;
    }
    
    @Override
    public void bindItemView(final MenuItemImpl menuItemImpl, final MenuView.ItemView itemView) {
        itemView.initialize(menuItemImpl, 0);
        final ActionMenuView itemInvoker = (ActionMenuView)super.mMenuView;
        final ActionMenuItemView actionMenuItemView = (ActionMenuItemView)itemView;
        actionMenuItemView.setItemInvoker(itemInvoker);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionMenuItemView.setPopupCallback((ActionMenuItemView.PopupCallback)this.mPopupCallback);
    }
    
    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }
    
    public boolean filterLeftoverView(final ViewGroup viewGroup, final int n) {
        return viewGroup.getChildAt(n) != this.mOverflowButton && super.filterLeftoverView(viewGroup, n);
    }
    
    @Override
    public boolean flagActionItems() {
        final MenuBuilder mMenu = super.mMenu;
        ArrayList<MenuItemImpl> visibleItems;
        int size;
        if (mMenu != null) {
            visibleItems = mMenu.getVisibleItems();
            size = visibleItems.size();
        }
        else {
            visibleItems = null;
            size = 0;
        }
        int mMaxItems = this.mMaxItems;
        final int mActionItemWidthLimit = this.mActionItemWidthLimit;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
        final ViewGroup viewGroup = (ViewGroup)super.mMenuView;
        int i = 0;
        boolean b = false;
        int n = 0;
        int n2 = 0;
        while (i < size) {
            final MenuItemImpl menuItemImpl = visibleItems.get(i);
            if (menuItemImpl.requiresActionButton()) {
                ++n;
            }
            else if (menuItemImpl.requestsActionButton()) {
                ++n2;
            }
            else {
                b = true;
            }
            int n3 = mMaxItems;
            if (this.mExpandedActionViewsExclusive) {
                n3 = mMaxItems;
                if (menuItemImpl.isActionViewExpanded()) {
                    n3 = 0;
                }
            }
            ++i;
            mMaxItems = n3;
        }
        int n4 = mMaxItems;
        if (this.mReserveOverflow && (b || n2 + n > (n4 = mMaxItems))) {
            n4 = mMaxItems - 1;
        }
        int n5 = n4 - n;
        final SparseBooleanArray mActionButtonGroups = this.mActionButtonGroups;
        mActionButtonGroups.clear();
        int n6;
        int n7;
        if (this.mStrictWidthLimit) {
            final int mMinCellSize = this.mMinCellSize;
            n6 = mActionItemWidthLimit / mMinCellSize;
            n7 = mMinCellSize + mActionItemWidthLimit % mMinCellSize / n6;
        }
        else {
            n7 = 0;
            n6 = 0;
        }
        int n8 = mActionItemWidthLimit;
        int j = 0;
        int n9 = 0;
        while (j < size) {
            final MenuItemImpl menuItemImpl2 = visibleItems.get(j);
            int n10;
            if (menuItemImpl2.requiresActionButton()) {
                final View itemView = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = itemView;
                }
                if (this.mStrictWidthLimit) {
                    n6 -= ActionMenuView.measureChildForCells(itemView, n7, n6, measureSpec, 0);
                }
                else {
                    itemView.measure(measureSpec, measureSpec);
                }
                final int measuredWidth = itemView.getMeasuredWidth();
                n10 = n8 - measuredWidth;
                if (n9 == 0) {
                    n9 = measuredWidth;
                }
                final int groupId = menuItemImpl2.getGroupId();
                if (groupId != 0) {
                    mActionButtonGroups.put(groupId, true);
                }
                menuItemImpl2.setIsActionButton(true);
            }
            else if (menuItemImpl2.requestsActionButton()) {
                final int groupId2 = menuItemImpl2.getGroupId();
                final boolean value = mActionButtonGroups.get(groupId2);
                boolean isActionButton = (n5 > 0 || value) && n8 > 0 && (!this.mStrictWidthLimit || n6 > 0);
                if (isActionButton) {
                    final View itemView2 = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                    if (this.mScrapActionButtonView == null) {
                        this.mScrapActionButtonView = itemView2;
                    }
                    if (this.mStrictWidthLimit) {
                        final int measureChildForCells = ActionMenuView.measureChildForCells(itemView2, n7, n6, measureSpec, 0);
                        final int n11 = n6 -= measureChildForCells;
                        if (measureChildForCells == 0) {
                            isActionButton = false;
                            n6 = n11;
                        }
                    }
                    else {
                        itemView2.measure(measureSpec, measureSpec);
                    }
                    final int measuredWidth2 = itemView2.getMeasuredWidth();
                    n10 = n8 - measuredWidth2;
                    int n12;
                    if ((n12 = n9) == 0) {
                        n12 = measuredWidth2;
                    }
                    isActionButton &= (this.mStrictWidthLimit ? (n10 >= 0) : (n10 + n12 > 0));
                    n9 = n12;
                }
                else {
                    n10 = n8;
                }
                int n13;
                if (isActionButton && groupId2 != 0) {
                    mActionButtonGroups.put(groupId2, true);
                    n13 = n5;
                }
                else {
                    n13 = n5;
                    if (value) {
                        mActionButtonGroups.put(groupId2, false);
                        int index = 0;
                        while (true) {
                            n13 = n5;
                            if (index >= j) {
                                break;
                            }
                            final MenuItemImpl menuItemImpl3 = visibleItems.get(index);
                            int n14 = n5;
                            if (menuItemImpl3.getGroupId() == groupId2) {
                                n14 = n5;
                                if (menuItemImpl3.isActionButton()) {
                                    n14 = n5 + 1;
                                }
                                menuItemImpl3.setIsActionButton(false);
                            }
                            ++index;
                            n5 = n14;
                        }
                    }
                }
                n5 = n13;
                if (isActionButton) {
                    n5 = n13 - 1;
                }
                menuItemImpl2.setIsActionButton(isActionButton);
            }
            else {
                menuItemImpl2.setIsActionButton(false);
                n10 = n8;
            }
            ++j;
            n8 = n10;
        }
        return true;
    }
    
    @Override
    public View getItemView(final MenuItemImpl menuItemImpl, final View view, final ViewGroup viewGroup) {
        View view2 = menuItemImpl.getActionView();
        if (view2 == null || menuItemImpl.hasCollapsibleActionView()) {
            view2 = super.getItemView(menuItemImpl, view, viewGroup);
        }
        int visibility;
        if (menuItemImpl.isActionViewExpanded()) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        view2.setVisibility(visibility);
        final ActionMenuView actionMenuView = (ActionMenuView)viewGroup;
        final ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            view2.setLayoutParams((ViewGroup$LayoutParams)actionMenuView.generateLayoutParams(layoutParams));
        }
        return view2;
    }
    
    public Drawable getOverflowIcon() {
        final OverflowMenuButton mOverflowButton = this.mOverflowButton;
        if (mOverflowButton != null) {
            return mOverflowButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }
    
    public boolean hideOverflowMenu() {
        final OpenOverflowRunnable mPostedOpenRunnable = this.mPostedOpenRunnable;
        if (mPostedOpenRunnable != null) {
            final MenuView mMenuView = super.mMenuView;
            if (mMenuView != null) {
                ((View)mMenuView).removeCallbacks((Runnable)mPostedOpenRunnable);
                this.mPostedOpenRunnable = null;
                return true;
            }
        }
        final OverflowPopup mOverflowPopup = this.mOverflowPopup;
        if (mOverflowPopup != null) {
            mOverflowPopup.dismiss();
            return true;
        }
        return false;
    }
    
    public boolean hideSubMenus() {
        final ActionButtonSubmenu mActionButtonPopup = this.mActionButtonPopup;
        if (mActionButtonPopup != null) {
            mActionButtonPopup.dismiss();
            return true;
        }
        return false;
    }
    
    @Override
    public void initForMenu(final Context context, final MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        final Resources resources = context.getResources();
        final ActionBarPolicy value = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = value.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = value.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = value.getMaxActionButtons();
        }
        int mWidthLimit = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(super.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(measureSpec, measureSpec);
            }
            mWidthLimit -= this.mOverflowButton.getMeasuredWidth();
        }
        else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = mWidthLimit;
        this.mMinCellSize = (int)(resources.getDisplayMetrics().density * 56.0f);
        this.mScrapActionButtonView = null;
    }
    
    public boolean isOverflowMenuShowing() {
        final OverflowPopup mOverflowPopup = this.mOverflowPopup;
        return mOverflowPopup != null && mOverflowPopup.isShowing();
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, b);
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(super.mContext).getMaxActionButtons();
        }
        final MenuBuilder mMenu = super.mMenu;
        if (mMenu != null) {
            mMenu.onItemsChanged(true);
        }
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        final boolean hasVisibleItems = subMenuBuilder.hasVisibleItems();
        final boolean b = false;
        if (!hasVisibleItems) {
            return false;
        }
        SubMenuBuilder subMenuBuilder2;
        for (subMenuBuilder2 = subMenuBuilder; subMenuBuilder2.getParentMenu() != super.mMenu; subMenuBuilder2 = (SubMenuBuilder)subMenuBuilder2.getParentMenu()) {}
        final View viewForItem = this.findViewForItem(subMenuBuilder2.getItem());
        if (viewForItem == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        final int size = subMenuBuilder.size();
        int n = 0;
        boolean forceShowIcon;
        while (true) {
            forceShowIcon = b;
            if (n >= size) {
                break;
            }
            final MenuItem item = subMenuBuilder.getItem(n);
            if (item.isVisible() && item.getIcon() != null) {
                forceShowIcon = true;
                break;
            }
            ++n;
        }
        (this.mActionButtonPopup = new ActionButtonSubmenu(super.mContext, subMenuBuilder, viewForItem)).setForceShowIcon(forceShowIcon);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }
    
    public void setExpandedActionViewsExclusive(final boolean mExpandedActionViewsExclusive) {
        this.mExpandedActionViewsExclusive = mExpandedActionViewsExclusive;
    }
    
    public void setMenuView(final ActionMenuView mMenuView) {
        ((ActionMenuView)(super.mMenuView = mMenuView)).initialize(super.mMenu);
    }
    
    public void setOverflowIcon(final Drawable drawable) {
        final OverflowMenuButton mOverflowButton = this.mOverflowButton;
        if (mOverflowButton != null) {
            mOverflowButton.setImageDrawable(drawable);
        }
        else {
            this.mPendingOverflowIconSet = true;
            this.mPendingOverflowIcon = drawable;
        }
    }
    
    public void setReserveOverflow(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
        this.mReserveOverflowSet = true;
    }
    
    @Override
    public boolean shouldIncludeItem(final int n, final MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }
    
    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing()) {
            final MenuBuilder mMenu = super.mMenu;
            if (mMenu != null && super.mMenuView != null && this.mPostedOpenRunnable == null && !mMenu.getNonActionItems().isEmpty()) {
                this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(super.mContext, super.mMenu, (View)this.mOverflowButton, true));
                ((View)super.mMenuView).post((Runnable)this.mPostedOpenRunnable);
                super.onSubMenuSelected(null);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        super.updateMenuView(b);
        ((View)super.mMenuView).requestLayout();
        final MenuBuilder mMenu = super.mMenu;
        final boolean b2 = false;
        if (mMenu != null) {
            final ArrayList<MenuItemImpl> actionItems = mMenu.getActionItems();
            for (int size = actionItems.size(), i = 0; i < size; ++i) {
                final ActionProvider supportActionProvider = actionItems.get(i).getSupportActionProvider();
                if (supportActionProvider != null) {
                    supportActionProvider.setSubUiVisibilityListener((ActionProvider.SubUiVisibilityListener)this);
                }
            }
        }
        final MenuBuilder mMenu2 = super.mMenu;
        ArrayList<MenuItemImpl> nonActionItems;
        if (mMenu2 != null) {
            nonActionItems = mMenu2.getNonActionItems();
        }
        else {
            nonActionItems = null;
        }
        int n = b2 ? 1 : 0;
        if (this.mReserveOverflow) {
            n = (b2 ? 1 : 0);
            if (nonActionItems != null) {
                final int size2 = nonActionItems.size();
                if (size2 == 1) {
                    n = ((nonActionItems.get(0).isActionViewExpanded() ^ true) ? 1 : 0);
                }
                else {
                    n = (b2 ? 1 : 0);
                    if (size2 > 0) {
                        n = 1;
                    }
                }
            }
        }
        if (n != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(super.mSystemContext);
            }
            final ViewGroup viewGroup = (ViewGroup)this.mOverflowButton.getParent();
            if (viewGroup != super.mMenuView) {
                if (viewGroup != null) {
                    viewGroup.removeView((View)this.mOverflowButton);
                }
                final ActionMenuView actionMenuView = (ActionMenuView)super.mMenuView;
                actionMenuView.addView((View)this.mOverflowButton, (ViewGroup$LayoutParams)actionMenuView.generateOverflowButtonLayoutParams());
            }
        }
        else {
            final OverflowMenuButton mOverflowButton = this.mOverflowButton;
            if (mOverflowButton != null) {
                final ViewParent parent = mOverflowButton.getParent();
                final MenuView mMenuView = super.mMenuView;
                if (parent == mMenuView) {
                    ((ViewGroup)mMenuView).removeView((View)this.mOverflowButton);
                }
            }
        }
        ((ActionMenuView)super.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }
    
    private class ActionButtonSubmenu extends MenuPopupHelper
    {
        public ActionButtonSubmenu(final Context context, final SubMenuBuilder subMenuBuilder, final View view) {
            super(context, subMenuBuilder, view, false, R$attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl)subMenuBuilder.getItem()).isActionButton()) {
                Object mOverflowButton;
                if ((mOverflowButton = ActionMenuPresenter.this.mOverflowButton) == null) {
                    mOverflowButton = ActionMenuPresenter.this.mMenuView;
                }
                this.setAnchorView((View)mOverflowButton);
            }
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }
        
        @Override
        protected void onDismiss() {
            final ActionMenuPresenter this$0 = ActionMenuPresenter.this;
            this$0.mActionButtonPopup = null;
            this$0.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }
    
    private class ActionMenuPopupCallback extends PopupCallback
    {
        ActionMenuPopupCallback() {
        }
        
        @Override
        public ShowableListMenu getPopup() {
            final ActionButtonSubmenu mActionButtonPopup = ActionMenuPresenter.this.mActionButtonPopup;
            MenuPopup popup;
            if (mActionButtonPopup != null) {
                popup = mActionButtonPopup.getPopup();
            }
            else {
                popup = null;
            }
            return popup;
        }
    }
    
    private class OpenOverflowRunnable implements Runnable
    {
        private OverflowPopup mPopup;
        
        public OpenOverflowRunnable(final OverflowPopup mPopup) {
            this.mPopup = mPopup;
        }
        
        @Override
        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            final View view = (View)ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }
    
    private class OverflowMenuButton extends AppCompatImageView implements ActionMenuChildView
    {
        private final float[] mTempPts;
        
        public OverflowMenuButton(final Context context) {
            super(context, null, R$attr.actionOverflowButtonStyle);
            this.mTempPts = new float[2];
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            TooltipCompat.setTooltipText((View)this, this.getContentDescription());
            this.setOnTouchListener((View$OnTouchListener)new ForwardingListener(this) {
                @Override
                public ShowableListMenu getPopup() {
                    final OverflowPopup mOverflowPopup = ActionMenuPresenter.this.mOverflowPopup;
                    if (mOverflowPopup == null) {
                        return null;
                    }
                    return mOverflowPopup.getPopup();
                }
                
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }
                
                public boolean onForwardingStopped() {
                    final ActionMenuPresenter this$0 = ActionMenuPresenter.this;
                    if (this$0.mPostedOpenRunnable != null) {
                        return false;
                    }
                    this$0.hideOverflowMenu();
                    return true;
                }
            });
        }
        
        @Override
        public boolean needsDividerAfter() {
            return false;
        }
        
        @Override
        public boolean needsDividerBefore() {
            return false;
        }
        
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }
        
        protected boolean setFrame(int n, int height, int paddingBottom, int paddingTop) {
            final boolean setFrame = super.setFrame(n, height, paddingBottom, paddingTop);
            final Drawable drawable = this.getDrawable();
            final Drawable background = this.getBackground();
            if (drawable != null && background != null) {
                final int width = this.getWidth();
                height = this.getHeight();
                n = Math.max(width, height) / 2;
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                paddingTop = this.getPaddingTop();
                paddingBottom = this.getPaddingBottom();
                final int n2 = (width + (paddingLeft - paddingRight)) / 2;
                height = (height + (paddingTop - paddingBottom)) / 2;
                DrawableCompat.setHotspotBounds(background, n2 - n, height - n, n2 + n, height + n);
            }
            return setFrame;
        }
    }
    
    private class OverflowPopup extends MenuPopupHelper
    {
        public OverflowPopup(final Context context, final MenuBuilder menuBuilder, final View view, final boolean b) {
            super(context, menuBuilder, view, b, R$attr.actionOverflowMenuStyle);
            this.setGravity(8388613);
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }
        
        @Override
        protected void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }
    
    private class PopupPresenterCallback implements Callback
    {
        PopupPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
            final Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback != null) {
                callback.onCloseMenu(menuBuilder, b);
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            boolean onOpenSubMenu = false;
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            final Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback != null) {
                onOpenSubMenu = callback.onOpenSubMenu(menuBuilder);
            }
            return onOpenSubMenu;
        }
    }
}
