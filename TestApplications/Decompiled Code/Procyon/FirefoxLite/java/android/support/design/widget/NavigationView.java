// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.v4.view.WindowInsetsCompat;
import android.view.Menu;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.support.v7.widget.TintTypedArray;
import android.view.ViewGroup;
import android.support.v7.view.menu.MenuPresenter;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuBuilder;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.internal.NavigationMenuPresenter;
import android.view.MenuInflater;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.ScrimInsetsFrameLayout;

public class NavigationView extends ScrimInsetsFrameLayout
{
    private static final int[] CHECKED_STATE_SET;
    private static final int[] DISABLED_STATE_SET;
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private final NavigationMenuPresenter presenter;
    
    static {
        CHECKED_STATE_SET = new int[] { 16842912 };
        DISABLED_STATE_SET = new int[] { -16842910 };
    }
    
    public NavigationView(final Context context) {
        this(context, null);
    }
    
    public NavigationView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.navigationViewStyle);
    }
    
    public NavigationView(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.presenter = new NavigationMenuPresenter();
        this.menu = new NavigationMenu(context);
        final TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context, set, R.styleable.NavigationView, n, R.style.Widget_Design_NavigationView, new int[0]);
        ViewCompat.setBackground((View)this, obtainTintedStyledAttributes.getDrawable(R.styleable.NavigationView_android_background));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation((View)this, (float)obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows((View)this, obtainTintedStyledAttributes.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.maxWidth = obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList itemIconTintList;
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_itemIconTint)) {
            itemIconTintList = obtainTintedStyledAttributes.getColorStateList(R.styleable.NavigationView_itemIconTint);
        }
        else {
            itemIconTintList = this.createDefaultColorStateList(16842808);
        }
        int resourceId;
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
            resourceId = obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
            n = 1;
        }
        else {
            n = 0;
            resourceId = 0;
        }
        ColorStateList colorStateList = null;
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_itemTextColor)) {
            colorStateList = obtainTintedStyledAttributes.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        ColorStateList defaultColorStateList = colorStateList;
        if (n == 0 && (defaultColorStateList = colorStateList) == null) {
            defaultColorStateList = this.createDefaultColorStateList(16842806);
        }
        final Drawable drawable = obtainTintedStyledAttributes.getDrawable(R.styleable.NavigationView_itemBackground);
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_itemHorizontalPadding)) {
            this.presenter.setItemHorizontalPadding(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_itemHorizontalPadding, 0));
        }
        final int dimensionPixelSize = obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_itemIconPadding, 0);
        this.menu.setCallback((MenuBuilder.Callback)new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
                return NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(menuItem);
            }
            
            @Override
            public void onMenuModeChange(final MenuBuilder menuBuilder) {
            }
        });
        this.presenter.setId(1);
        this.presenter.initForMenu(context, this.menu);
        this.presenter.setItemIconTintList(itemIconTintList);
        if (n != 0) {
            this.presenter.setItemTextAppearance(resourceId);
        }
        this.presenter.setItemTextColor(defaultColorStateList);
        this.presenter.setItemBackground(drawable);
        this.presenter.setItemIconPadding(dimensionPixelSize);
        this.menu.addMenuPresenter(this.presenter);
        this.addView((View)this.presenter.getMenuView((ViewGroup)this));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_menu)) {
            this.inflateMenu(obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationView_headerLayout)) {
            this.inflateHeaderView(obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        obtainTintedStyledAttributes.recycle();
    }
    
    private ColorStateList createDefaultColorStateList(int defaultColor) {
        final TypedValue typedValue = new TypedValue();
        if (!this.getContext().getTheme().resolveAttribute(defaultColor, typedValue, true)) {
            return null;
        }
        final ColorStateList colorStateList = AppCompatResources.getColorStateList(this.getContext(), typedValue.resourceId);
        if (!this.getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        final int data = typedValue.data;
        defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[][] { NavigationView.DISABLED_STATE_SET, NavigationView.CHECKED_STATE_SET, NavigationView.EMPTY_STATE_SET }, new int[] { colorStateList.getColorForState(NavigationView.DISABLED_STATE_SET, defaultColor), data, defaultColor });
    }
    
    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.menuInflater;
    }
    
    public MenuItem getCheckedItem() {
        return (MenuItem)this.presenter.getCheckedItem();
    }
    
    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }
    
    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }
    
    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }
    
    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }
    
    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }
    
    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }
    
    public Menu getMenu() {
        return (Menu)this.menu;
    }
    
    public View inflateHeaderView(final int n) {
        return this.presenter.inflateHeaderView(n);
    }
    
    public void inflateMenu(final int n) {
        this.presenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }
    
    @Override
    protected void onInsetsChanged(final WindowInsetsCompat windowInsetsCompat) {
        this.presenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }
    
    protected void onMeasure(int n, final int n2) {
        final int mode = View$MeasureSpec.getMode(n);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 0) {
                n = View$MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
            }
        }
        else {
            n = View$MeasureSpec.makeMeasureSpec(Math.min(View$MeasureSpec.getSize(n), this.maxWidth), 1073741824);
        }
        super.onMeasure(n, n2);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.menu.restorePresenterStates(savedState.menuState);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
        return (Parcelable)savedState;
    }
    
    public void setCheckedItem(final int n) {
        final MenuItem item = this.menu.findItem(n);
        if (item != null) {
            this.presenter.setCheckedItem((MenuItemImpl)item);
        }
    }
    
    public void setCheckedItem(MenuItem item) {
        item = this.menu.findItem(item.getItemId());
        if (item != null) {
            this.presenter.setCheckedItem((MenuItemImpl)item);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }
    
    public void setItemBackground(final Drawable itemBackground) {
        this.presenter.setItemBackground(itemBackground);
    }
    
    public void setItemBackgroundResource(final int n) {
        this.setItemBackground(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setItemHorizontalPadding(final int itemHorizontalPadding) {
        this.presenter.setItemHorizontalPadding(itemHorizontalPadding);
    }
    
    public void setItemHorizontalPaddingResource(final int n) {
        this.presenter.setItemHorizontalPadding(this.getResources().getDimensionPixelSize(n));
    }
    
    public void setItemIconPadding(final int itemIconPadding) {
        this.presenter.setItemIconPadding(itemIconPadding);
    }
    
    public void setItemIconPaddingResource(final int n) {
        this.presenter.setItemIconPadding(this.getResources().getDimensionPixelSize(n));
    }
    
    public void setItemIconTintList(final ColorStateList itemIconTintList) {
        this.presenter.setItemIconTintList(itemIconTintList);
    }
    
    public void setItemTextAppearance(final int itemTextAppearance) {
        this.presenter.setItemTextAppearance(itemTextAppearance);
    }
    
    public void setItemTextColor(final ColorStateList itemTextColor) {
        this.presenter.setItemTextColor(itemTextColor);
    }
    
    public void setNavigationItemSelectedListener(final OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }
    
    public interface OnNavigationItemSelectedListener
    {
        boolean onNavigationItemSelected(final MenuItem p0);
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public Bundle menuState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuState);
        }
    }
}
