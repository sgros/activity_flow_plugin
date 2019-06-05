// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.widget.TextView;
import android.view.SubMenu;
import java.util.ArrayList;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.SparseArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.design.R;
import android.support.v7.view.menu.MenuView;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.MenuItem;
import android.view.View;
import android.view.View$OnClickListener;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.widget.LinearLayout;
import android.support.v7.view.menu.MenuPresenter;

public class NavigationMenuPresenter implements MenuPresenter
{
    NavigationMenuAdapter adapter;
    private Callback callback;
    LinearLayout headerLayout;
    ColorStateList iconTintList;
    private int id;
    Drawable itemBackground;
    int itemHorizontalPadding;
    int itemIconPadding;
    LayoutInflater layoutInflater;
    MenuBuilder menu;
    private NavigationMenuView menuView;
    final View$OnClickListener onClickListener;
    int paddingSeparator;
    private int paddingTopDefault;
    int textAppearance;
    boolean textAppearanceSet;
    ColorStateList textColor;
    
    public NavigationMenuPresenter() {
        this.onClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)view;
                NavigationMenuPresenter.this.setUpdateSuspended(true);
                final MenuItemImpl itemData = navigationMenuItemView.getItemData();
                final boolean performItemAction = NavigationMenuPresenter.this.menu.performItemAction((MenuItem)itemData, NavigationMenuPresenter.this, 0);
                if (itemData != null && itemData.isCheckable() && performItemAction) {
                    NavigationMenuPresenter.this.adapter.setCheckedItem(itemData);
                }
                NavigationMenuPresenter.this.setUpdateSuspended(false);
                NavigationMenuPresenter.this.updateMenuView(false);
            }
        };
    }
    
    public void addHeaderView(final View view) {
        this.headerLayout.addView(view);
        this.menuView.setPadding(0, 0, 0, this.menuView.getPaddingBottom());
    }
    
    @Override
    public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    public void dispatchApplyWindowInsets(final WindowInsetsCompat windowInsetsCompat) {
        final int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
        if (this.paddingTopDefault != systemWindowInsetTop) {
            this.paddingTopDefault = systemWindowInsetTop;
            if (this.headerLayout.getChildCount() == 0) {
                this.menuView.setPadding(0, this.paddingTopDefault, 0, this.menuView.getPaddingBottom());
            }
        }
        ViewCompat.dispatchApplyWindowInsets((View)this.headerLayout, windowInsetsCompat);
    }
    
    @Override
    public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    @Override
    public boolean flagActionItems() {
        return false;
    }
    
    public MenuItemImpl getCheckedItem() {
        return this.adapter.getCheckedItem();
    }
    
    public int getHeaderCount() {
        return this.headerLayout.getChildCount();
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    public Drawable getItemBackground() {
        return this.itemBackground;
    }
    
    public int getItemHorizontalPadding() {
        return this.itemHorizontalPadding;
    }
    
    public int getItemIconPadding() {
        return this.itemIconPadding;
    }
    
    public ColorStateList getItemTextColor() {
        return this.textColor;
    }
    
    public ColorStateList getItemTintList() {
        return this.iconTintList;
    }
    
    public MenuView getMenuView(final ViewGroup viewGroup) {
        if (this.menuView == null) {
            this.menuView = (NavigationMenuView)this.layoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.adapter == null) {
                this.adapter = new NavigationMenuAdapter();
            }
            this.headerLayout = (LinearLayout)this.layoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.menuView, false);
            this.menuView.setAdapter((RecyclerView.Adapter)this.adapter);
        }
        return this.menuView;
    }
    
    public View inflateHeaderView(final int n) {
        final View inflate = this.layoutInflater.inflate(n, (ViewGroup)this.headerLayout, false);
        this.addHeaderView(inflate);
        return inflate;
    }
    
    @Override
    public void initForMenu(final Context context, final MenuBuilder menu) {
        this.layoutInflater = LayoutInflater.from(context);
        this.menu = menu;
        this.paddingSeparator = context.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.callback != null) {
            this.callback.onCloseMenu(menuBuilder, b);
        }
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            final Bundle bundle = (Bundle)parcelable;
            final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.menuView.restoreHierarchyState(sparseParcelableArray);
            }
            final Bundle bundle2 = bundle.getBundle("android:menu:adapter");
            if (bundle2 != null) {
                this.adapter.restoreInstanceState(bundle2);
            }
            final SparseArray sparseParcelableArray2 = bundle.getSparseParcelableArray("android:menu:header");
            if (sparseParcelableArray2 != null) {
                this.headerLayout.restoreHierarchyState(sparseParcelableArray2);
            }
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        if (this.menuView != null) {
            final SparseArray sparseArray = new SparseArray();
            this.menuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        if (this.adapter != null) {
            bundle.putBundle("android:menu:adapter", this.adapter.createInstanceState());
        }
        if (this.headerLayout != null) {
            final SparseArray sparseArray2 = new SparseArray();
            this.headerLayout.saveHierarchyState(sparseArray2);
            bundle.putSparseParcelableArray("android:menu:header", sparseArray2);
        }
        return (Parcelable)bundle;
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        return false;
    }
    
    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    public void setCheckedItem(final MenuItemImpl checkedItem) {
        this.adapter.setCheckedItem(checkedItem);
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setItemBackground(final Drawable itemBackground) {
        this.itemBackground = itemBackground;
        this.updateMenuView(false);
    }
    
    public void setItemHorizontalPadding(final int itemHorizontalPadding) {
        this.itemHorizontalPadding = itemHorizontalPadding;
        this.updateMenuView(false);
    }
    
    public void setItemIconPadding(final int itemIconPadding) {
        this.itemIconPadding = itemIconPadding;
        this.updateMenuView(false);
    }
    
    public void setItemIconTintList(final ColorStateList iconTintList) {
        this.iconTintList = iconTintList;
        this.updateMenuView(false);
    }
    
    public void setItemTextAppearance(final int textAppearance) {
        this.textAppearance = textAppearance;
        this.textAppearanceSet = true;
        this.updateMenuView(false);
    }
    
    public void setItemTextColor(final ColorStateList textColor) {
        this.textColor = textColor;
        this.updateMenuView(false);
    }
    
    public void setUpdateSuspended(final boolean updateSuspended) {
        if (this.adapter != null) {
            this.adapter.setUpdateSuspended(updateSuspended);
        }
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        if (this.adapter != null) {
            this.adapter.update();
        }
    }
    
    private static class HeaderViewHolder extends ViewHolder
    {
        public HeaderViewHolder(final View view) {
            super(view);
        }
    }
    
    private class NavigationMenuAdapter extends Adapter<NavigationMenuPresenter.ViewHolder>
    {
        private MenuItemImpl checkedItem;
        private final ArrayList<NavigationMenuItem> items;
        private boolean updateSuspended;
        
        NavigationMenuAdapter() {
            this.items = new ArrayList<NavigationMenuItem>();
            this.prepareMenuItems();
        }
        
        private void appendTransparentIconIfMissing(int i, final int n) {
            while (i < n) {
                this.items.get(i).needsEmptyIcon = true;
                ++i;
            }
        }
        
        private void prepareMenuItems() {
            if (this.updateSuspended) {
                return;
            }
            this.updateSuspended = true;
            this.items.clear();
            this.items.add(new NavigationMenuHeaderItem());
            final int size = NavigationMenuPresenter.this.menu.getVisibleItems().size();
            int i = 0;
            int n = -1;
            int n2 = 0;
            int n3 = 0;
            while (i < size) {
                final MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.menu.getVisibleItems().get(i);
                if (menuItemImpl.isChecked()) {
                    this.setCheckedItem(menuItemImpl);
                }
                if (menuItemImpl.isCheckable()) {
                    menuItemImpl.setExclusiveCheckable(false);
                }
                int groupId;
                int needsEmptyIcon;
                int n4;
                if (menuItemImpl.hasSubMenu()) {
                    final SubMenu subMenu = menuItemImpl.getSubMenu();
                    groupId = n;
                    needsEmptyIcon = n2;
                    n4 = n3;
                    if (subMenu.hasVisibleItems()) {
                        if (i != 0) {
                            this.items.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, 0));
                        }
                        this.items.add(new NavigationMenuTextItem(menuItemImpl));
                        final int size2 = this.items.size();
                        final int size3 = subMenu.size();
                        int j = 0;
                        int n5 = 0;
                        while (j < size3) {
                            final MenuItemImpl menuItemImpl2 = (MenuItemImpl)subMenu.getItem(j);
                            int n6 = n5;
                            if (menuItemImpl2.isVisible()) {
                                if ((n6 = n5) == 0) {
                                    n6 = n5;
                                    if (menuItemImpl2.getIcon() != null) {
                                        n6 = 1;
                                    }
                                }
                                if (menuItemImpl2.isCheckable()) {
                                    menuItemImpl2.setExclusiveCheckable(false);
                                }
                                if (menuItemImpl.isChecked()) {
                                    this.setCheckedItem(menuItemImpl);
                                }
                                this.items.add(new NavigationMenuTextItem(menuItemImpl2));
                            }
                            ++j;
                            n5 = n6;
                        }
                        groupId = n;
                        needsEmptyIcon = n2;
                        n4 = n3;
                        if (n5 != 0) {
                            this.appendTransparentIconIfMissing(size2, this.items.size());
                            groupId = n;
                            needsEmptyIcon = n2;
                            n4 = n3;
                        }
                    }
                }
                else {
                    groupId = menuItemImpl.getGroupId();
                    int n7;
                    if (groupId != n) {
                        final int size4 = this.items.size();
                        needsEmptyIcon = ((menuItemImpl.getIcon() != null) ? 1 : 0);
                        n7 = size4;
                        if (i != 0) {
                            n7 = size4 + 1;
                            this.items.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, NavigationMenuPresenter.this.paddingSeparator));
                        }
                    }
                    else {
                        needsEmptyIcon = n2;
                        n7 = n3;
                        if (n2 == 0) {
                            needsEmptyIcon = n2;
                            n7 = n3;
                            if (menuItemImpl.getIcon() != null) {
                                this.appendTransparentIconIfMissing(n3, this.items.size());
                                needsEmptyIcon = 1;
                                n7 = n3;
                            }
                        }
                    }
                    final NavigationMenuTextItem e = new NavigationMenuTextItem(menuItemImpl);
                    e.needsEmptyIcon = (needsEmptyIcon != 0);
                    this.items.add(e);
                    n4 = n7;
                }
                ++i;
                n = groupId;
                n2 = needsEmptyIcon;
                n3 = n4;
            }
            this.updateSuspended = false;
        }
        
        public Bundle createInstanceState() {
            final Bundle bundle = new Bundle();
            if (this.checkedItem != null) {
                bundle.putInt("android:menu:checked", this.checkedItem.getItemId());
            }
            final SparseArray sparseArray = new SparseArray();
            for (int i = 0; i < this.items.size(); ++i) {
                final NavigationMenuItem navigationMenuItem = this.items.get(i);
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    final MenuItemImpl menuItem = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem();
                    View actionView;
                    if (menuItem != null) {
                        actionView = menuItem.getActionView();
                    }
                    else {
                        actionView = null;
                    }
                    if (actionView != null) {
                        final ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState((SparseArray)parcelableSparseArray);
                        sparseArray.put(menuItem.getItemId(), (Object)parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
            return bundle;
        }
        
        public MenuItemImpl getCheckedItem() {
            return this.checkedItem;
        }
        
        @Override
        public int getItemCount() {
            return this.items.size();
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int index) {
            final NavigationMenuItem navigationMenuItem = this.items.get(index);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (!(navigationMenuItem instanceof NavigationMenuTextItem)) {
                throw new RuntimeException("Unknown item type.");
            }
            if (((NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu()) {
                return 1;
            }
            return 0;
        }
        
        public void onBindViewHolder(final NavigationMenuPresenter.ViewHolder viewHolder, final int index) {
            switch (this.getItemViewType(index)) {
                case 2: {
                    final NavigationMenuSeparatorItem navigationMenuSeparatorItem = this.items.get(index);
                    viewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
                    break;
                }
                case 1: {
                    ((TextView)viewHolder.itemView).setText(((NavigationMenuTextItem)this.items.get(index)).getMenuItem().getTitle());
                    break;
                }
                case 0: {
                    final NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)viewHolder.itemView;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.iconTintList);
                    if (NavigationMenuPresenter.this.textAppearanceSet) {
                        navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.textAppearance);
                    }
                    if (NavigationMenuPresenter.this.textColor != null) {
                        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.textColor);
                    }
                    Drawable drawable;
                    if (NavigationMenuPresenter.this.itemBackground != null) {
                        drawable = NavigationMenuPresenter.this.itemBackground.getConstantState().newDrawable();
                    }
                    else {
                        drawable = null;
                    }
                    ViewCompat.setBackground((View)navigationMenuItemView, drawable);
                    final NavigationMenuTextItem navigationMenuTextItem = this.items.get(index);
                    navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
                    navigationMenuItemView.setHorizontalPadding(NavigationMenuPresenter.this.itemHorizontalPadding);
                    navigationMenuItemView.setIconPadding(NavigationMenuPresenter.this.itemIconPadding);
                    navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
                    break;
                }
            }
        }
        
        public NavigationMenuPresenter.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            switch (n) {
                default: {
                    return null;
                }
                case 3: {
                    return new HeaderViewHolder((View)NavigationMenuPresenter.this.headerLayout);
                }
                case 2: {
                    return new SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, viewGroup);
                }
                case 1: {
                    return new SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, viewGroup);
                }
                case 0: {
                    return new NormalViewHolder(NavigationMenuPresenter.this.layoutInflater, viewGroup, NavigationMenuPresenter.this.onClickListener);
                }
            }
        }
        
        public void onViewRecycled(final NavigationMenuPresenter.ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView)viewHolder.itemView).recycle();
            }
        }
        
        public void restoreInstanceState(final Bundle bundle) {
            final int n = 0;
            final int int1 = bundle.getInt("android:menu:checked", 0);
            if (int1 != 0) {
                this.updateSuspended = true;
                for (int size = this.items.size(), i = 0; i < size; ++i) {
                    final NavigationMenuItem navigationMenuItem = this.items.get(i);
                    if (navigationMenuItem instanceof NavigationMenuTextItem) {
                        final MenuItemImpl menuItem = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem();
                        if (menuItem != null && menuItem.getItemId() == int1) {
                            this.setCheckedItem(menuItem);
                            break;
                        }
                    }
                }
                this.updateSuspended = false;
                this.prepareMenuItems();
            }
            final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:action_views");
            if (sparseParcelableArray != null) {
                for (int size2 = this.items.size(), j = n; j < size2; ++j) {
                    final NavigationMenuItem navigationMenuItem2 = this.items.get(j);
                    if (navigationMenuItem2 instanceof NavigationMenuTextItem) {
                        final MenuItemImpl menuItem2 = ((NavigationMenuTextItem)navigationMenuItem2).getMenuItem();
                        if (menuItem2 != null) {
                            final View actionView = menuItem2.getActionView();
                            if (actionView != null) {
                                final ParcelableSparseArray parcelableSparseArray = (ParcelableSparseArray)sparseParcelableArray.get(menuItem2.getItemId());
                                if (parcelableSparseArray != null) {
                                    actionView.restoreHierarchyState((SparseArray)parcelableSparseArray);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        public void setCheckedItem(final MenuItemImpl checkedItem) {
            if (this.checkedItem != checkedItem && checkedItem.isCheckable()) {
                if (this.checkedItem != null) {
                    this.checkedItem.setChecked(false);
                }
                (this.checkedItem = checkedItem).setChecked(true);
            }
        }
        
        public void setUpdateSuspended(final boolean updateSuspended) {
            this.updateSuspended = updateSuspended;
        }
        
        public void update() {
            this.prepareMenuItems();
            ((RecyclerView.Adapter)this).notifyDataSetChanged();
        }
    }
    
    private static class NavigationMenuHeaderItem implements NavigationMenuItem
    {
        NavigationMenuHeaderItem() {
        }
    }
    
    private interface NavigationMenuItem
    {
    }
    
    private static class NavigationMenuSeparatorItem implements NavigationMenuItem
    {
        private final int paddingBottom;
        private final int paddingTop;
        
        public NavigationMenuSeparatorItem(final int paddingTop, final int paddingBottom) {
            this.paddingTop = paddingTop;
            this.paddingBottom = paddingBottom;
        }
        
        public int getPaddingBottom() {
            return this.paddingBottom;
        }
        
        public int getPaddingTop() {
            return this.paddingTop;
        }
    }
    
    private static class NavigationMenuTextItem implements NavigationMenuItem
    {
        private final MenuItemImpl menuItem;
        boolean needsEmptyIcon;
        
        NavigationMenuTextItem(final MenuItemImpl menuItem) {
            this.menuItem = menuItem;
        }
        
        public MenuItemImpl getMenuItem() {
            return this.menuItem;
        }
    }
    
    private static class NormalViewHolder extends ViewHolder
    {
        public NormalViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final View$OnClickListener onClickListener) {
            super(layoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }
    
    private static class SeparatorViewHolder extends ViewHolder
    {
        public SeparatorViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }
    
    private static class SubheaderViewHolder extends ViewHolder
    {
        public SubheaderViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }
    
    private abstract static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(final View view) {
            super(view);
        }
    }
}
