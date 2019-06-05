package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.C0028R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.internal.ThemeEnforcement;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.view.AbsSavedState;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.WindowInsetsCompat;
import android.support.p004v7.appcompat.C0187R;
import android.support.p004v7.content.res.AppCompatResources;
import android.support.p004v7.view.SupportMenuInflater;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuBuilder.Callback;
import android.support.p004v7.view.menu.MenuItemImpl;
import android.support.p004v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;

public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private final NavigationMenuPresenter presenter;

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    /* renamed from: android.support.design.widget.NavigationView$1 */
    class C06131 implements Callback {
        public void onMenuModeChange(MenuBuilder menuBuilder) {
        }

        C06131() {
        }

        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(menuItem);
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C00731();
        public Bundle menuState;

        /* renamed from: android.support.design.widget.NavigationView$SavedState$1 */
        static class C00731 implements ClassLoaderCreator<SavedState> {
            C00731() {
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuState);
        }
    }

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0028R.attr.navigationViewStyle);
    }

    public NavigationView(Context context, AttributeSet attributeSet, int i) {
        ColorStateList colorStateList;
        int resourceId;
        Object obj;
        super(context, attributeSet, i);
        this.presenter = new NavigationMenuPresenter();
        this.menu = new NavigationMenu(context);
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context, attributeSet, C0028R.styleable.NavigationView, i, C0028R.style.Widget_Design_NavigationView, new int[0]);
        ViewCompat.setBackground(this, obtainTintedStyledAttributes.getDrawable(C0028R.styleable.NavigationView_android_background));
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) obtainTintedStyledAttributes.getDimensionPixelSize(C0028R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows(this, obtainTintedStyledAttributes.getBoolean(C0028R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.maxWidth = obtainTintedStyledAttributes.getDimensionPixelSize(C0028R.styleable.NavigationView_android_maxWidth, 0);
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_itemIconTint)) {
            colorStateList = obtainTintedStyledAttributes.getColorStateList(C0028R.styleable.NavigationView_itemIconTint);
        } else {
            colorStateList = createDefaultColorStateList(16842808);
        }
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_itemTextAppearance)) {
            resourceId = obtainTintedStyledAttributes.getResourceId(C0028R.styleable.NavigationView_itemTextAppearance, 0);
            obj = 1;
        } else {
            obj = null;
            resourceId = 0;
        }
        ColorStateList colorStateList2 = null;
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_itemTextColor)) {
            colorStateList2 = obtainTintedStyledAttributes.getColorStateList(C0028R.styleable.NavigationView_itemTextColor);
        }
        if (obj == null && r4 == null) {
            colorStateList2 = createDefaultColorStateList(16842806);
        }
        Drawable drawable = obtainTintedStyledAttributes.getDrawable(C0028R.styleable.NavigationView_itemBackground);
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_itemHorizontalPadding)) {
            this.presenter.setItemHorizontalPadding(obtainTintedStyledAttributes.getDimensionPixelSize(C0028R.styleable.NavigationView_itemHorizontalPadding, 0));
        }
        int dimensionPixelSize = obtainTintedStyledAttributes.getDimensionPixelSize(C0028R.styleable.NavigationView_itemIconPadding, 0);
        this.menu.setCallback(new C06131());
        this.presenter.setId(1);
        this.presenter.initForMenu(context, this.menu);
        this.presenter.setItemIconTintList(colorStateList);
        if (obj != null) {
            this.presenter.setItemTextAppearance(resourceId);
        }
        this.presenter.setItemTextColor(colorStateList2);
        this.presenter.setItemBackground(drawable);
        this.presenter.setItemIconPadding(dimensionPixelSize);
        this.menu.addMenuPresenter(this.presenter);
        addView((View) this.presenter.getMenuView(this));
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_menu)) {
            inflateMenu(obtainTintedStyledAttributes.getResourceId(C0028R.styleable.NavigationView_menu, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(C0028R.styleable.NavigationView_headerLayout)) {
            inflateHeaderView(obtainTintedStyledAttributes.getResourceId(C0028R.styleable.NavigationView_headerLayout, 0));
        }
        obtainTintedStyledAttributes.recycle();
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.menu.restorePresenterStates(savedState.menuState);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.listener = onNavigationItemSelectedListener;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            i = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(i), this.maxWidth), 1073741824);
        } else if (mode == 0) {
            i = MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
        }
        super.onMeasure(i, i2);
    }

    /* Access modifiers changed, original: protected */
    public void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.presenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    public void inflateMenu(int i) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public View inflateHeaderView(int i) {
        return this.presenter.inflateHeaderView(i);
    }

    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }

    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.presenter.setItemIconTintList(colorStateList);
    }

    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.presenter.setItemTextColor(colorStateList);
    }

    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }

    public void setItemBackgroundResource(int i) {
        setItemBackground(ContextCompat.getDrawable(getContext(), i));
    }

    public void setItemBackground(Drawable drawable) {
        this.presenter.setItemBackground(drawable);
    }

    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }

    public void setItemHorizontalPadding(int i) {
        this.presenter.setItemHorizontalPadding(i);
    }

    public void setItemHorizontalPaddingResource(int i) {
        this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(i));
    }

    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }

    public void setItemIconPadding(int i) {
        this.presenter.setItemIconPadding(i);
    }

    public void setItemIconPaddingResource(int i) {
        this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(i));
    }

    public void setCheckedItem(int i) {
        MenuItem findItem = this.menu.findItem(i);
        if (findItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl) findItem);
        }
    }

    public void setCheckedItem(MenuItem menuItem) {
        menuItem = this.menu.findItem(menuItem.getItemId());
        if (menuItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl) menuItem);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }

    public MenuItem getCheckedItem() {
        return this.presenter.getCheckedItem();
    }

    public void setItemTextAppearance(int i) {
        this.presenter.setItemTextAppearance(i);
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    private ColorStateList createDefaultColorStateList(int i) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0187R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(DISABLED_STATE_SET, defaultColor), i2, defaultColor});
    }
}
