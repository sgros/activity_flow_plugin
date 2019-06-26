package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.appcompat.app.ActionBar$LayoutParams;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuPresenter.Callback;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import java.util.ArrayList;
import java.util.List;
import org.telegram.p004ui.ActionBar.Theme;

public class Toolbar extends ViewGroup {
    private Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final androidx.appcompat.widget.ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    /* renamed from: androidx.appcompat.widget.Toolbar$2 */
    class C00302 implements Runnable {
        C00302() {
        }

        public void run() {
            Toolbar.this.showOverflowMenu();
        }
    }

    /* renamed from: androidx.appcompat.widget.Toolbar$3 */
    class C00313 implements OnClickListener {
        C00313() {
        }

        public void onClick(View view) {
            Toolbar.this.collapseActionView();
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    /* renamed from: androidx.appcompat.widget.Toolbar$1 */
    class C00331 implements androidx.appcompat.widget.ActionMenuView.OnMenuItemClickListener {
        C00331() {
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            OnMenuItemClickListener onMenuItemClickListener = Toolbar.this.mOnMenuItemClickListener;
            return onMenuItemClickListener != null ? onMenuItemClickListener.onMenuItemClick(menuItem) : false;
        }
    }

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        public boolean flagActionItems() {
            return false;
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        ExpandedActionViewMenuPresenter() {
        }

        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuBuilder menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null) {
                MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    menuBuilder2.collapseItemActionView(menuItemImpl);
                }
            }
            this.mMenu = menuBuilder;
        }

        public void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                Object obj = null;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    for (int i = 0; i < size; i++) {
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            obj = 1;
                            break;
                        }
                    }
                }
                if (obj == null) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            Toolbar toolbar;
            Toolbar.this.ensureCollapseButtonView();
            ViewParent parent = Toolbar.this.mCollapseButtonView.getParent();
            ViewParent viewParent = Toolbar.this;
            if (parent != viewParent) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(viewParent.mCollapseButtonView);
                }
                toolbar = Toolbar.this;
                toolbar.addView(toolbar.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = menuItemImpl.getActionView();
            this.mCurrentExpandedItem = menuItemImpl;
            parent = Toolbar.this.mExpandedActionView.getParent();
            viewParent = Toolbar.this;
            if (parent != viewParent) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(viewParent.mExpandedActionView);
                }
                LayoutParams generateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                Toolbar toolbar2 = Toolbar.this;
                generateDefaultLayoutParams.gravity = 8388611 | (toolbar2.mButtonGravity & 112);
                generateDefaultLayoutParams.mViewType = 2;
                toolbar2.mExpandedActionView.setLayoutParams(generateDefaultLayoutParams);
                toolbar = Toolbar.this;
                toolbar.addView(toolbar.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            View view = Toolbar.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            View view = Toolbar.this.mExpandedActionView;
            if (view instanceof CollapsibleActionView) {
                ((CollapsibleActionView) view).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mCollapseButtonView);
            toolbar = Toolbar.this;
            toolbar.mExpandedActionView = null;
            toolbar.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }
    }

    public static class LayoutParams extends ActionBar$LayoutParams {
        int mViewType;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mViewType = 0;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ActionBar$LayoutParams) layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }

        public LayoutParams(ActionBar$LayoutParams actionBar$LayoutParams) {
            super(actionBar$LayoutParams);
            this.mViewType = 0;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super((android.view.ViewGroup.LayoutParams) marginLayoutParams);
            this.mViewType = 0;
            copyMarginsFromCompat(marginLayoutParams);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public void copyMarginsFromCompat(MarginLayoutParams marginLayoutParams) {
            this.leftMargin = marginLayoutParams.leftMargin;
            this.topMargin = marginLayoutParams.topMargin;
            this.rightMargin = marginLayoutParams.rightMargin;
            this.bottomMargin = marginLayoutParams.bottomMargin;
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C00321();
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /* renamed from: androidx.appcompat.widget.Toolbar$SavedState$1 */
        static class C00321 implements ClassLoaderCreator<SavedState> {
            C00321() {
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
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.toolbarStyle);
    }

    public Toolbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList();
        this.mHiddenViews = new ArrayList();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new C00331();
        this.mShowOverflowMenuRunnable = new C00302();
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet, R$styleable.Toolbar, i, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(R$styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(R$styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R$styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = obtainStyledAttributes.getInteger(R$styleable.Toolbar_buttonGravity, 48);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMargin, 0);
        if (obtainStyledAttributes.hasValue(R$styleable.Toolbar_titleMargins)) {
            dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMargins, dimensionPixelOffset);
        }
        this.mTitleMarginBottom = dimensionPixelOffset;
        this.mTitleMarginTop = dimensionPixelOffset;
        this.mTitleMarginEnd = dimensionPixelOffset;
        this.mTitleMarginStart = dimensionPixelOffset;
        dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginStart, -1);
        if (dimensionPixelOffset >= 0) {
            this.mTitleMarginStart = dimensionPixelOffset;
        }
        dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginEnd, -1);
        if (dimensionPixelOffset >= 0) {
            this.mTitleMarginEnd = dimensionPixelOffset;
        }
        dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginTop, -1);
        if (dimensionPixelOffset >= 0) {
            this.mTitleMarginTop = dimensionPixelOffset;
        }
        dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginBottom, -1);
        if (dimensionPixelOffset >= 0) {
            this.mTitleMarginBottom = dimensionPixelOffset;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Toolbar_maxButtonHeight, -1);
        dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Toolbar_contentInsetLeft, 0);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelSize, dimensionPixelSize2);
        if (!(dimensionPixelOffset == Integer.MIN_VALUE && dimensionPixelOffset2 == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(dimensionPixelOffset, dimensionPixelOffset2);
        }
        this.mContentInsetStartWithNavigation = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(R$styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = obtainStyledAttributes.getText(R$styleable.Toolbar_collapseContentDescription);
        CharSequence text = obtainStyledAttributes.getText(R$styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        text = obtainStyledAttributes.getText(R$styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text)) {
            setSubtitle(text);
        }
        this.mPopupContext = getContext();
        setPopupTheme(obtainStyledAttributes.getResourceId(R$styleable.Toolbar_popupTheme, 0));
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        text = obtainStyledAttributes.getText(R$styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text)) {
            setNavigationContentDescription(text);
        }
        drawable = obtainStyledAttributes.getDrawable(R$styleable.Toolbar_logo);
        if (drawable != null) {
            setLogo(drawable);
        }
        text = obtainStyledAttributes.getText(R$styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text)) {
            setLogoDescription(text);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(obtainStyledAttributes.getColor(R$styleable.Toolbar_titleTextColor, -1));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(obtainStyledAttributes.getColor(R$styleable.Toolbar_subtitleTextColor, -1));
        }
        obtainStyledAttributes.recycle();
    }

    public void setPopupTheme(int i) {
        if (this.mPopupTheme != i) {
            this.mPopupTheme = i;
            if (i == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), i);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public void setTitleMarginStart(int i) {
        this.mTitleMarginStart = i;
        requestLayout();
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public void setTitleMarginTop(int i) {
        this.mTitleMarginTop = i;
        requestLayout();
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public void setTitleMarginEnd(int i) {
        this.mTitleMarginEnd = i;
        requestLayout();
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public void setTitleMarginBottom(int i) {
        this.mTitleMarginBottom = i;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int i) {
        if (VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(i);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    public void setLogo(int i) {
        setLogo(AppCompatResources.getDrawable(getContext(), i));
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }

    public void setLogo(Drawable drawable) {
        ImageView imageView;
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            imageView = this.mLogoView;
            if (imageView != null && isChildOrHidden(imageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getDrawable() : null;
    }

    public void setLogoDescription(int i) {
        setLogoDescription(getContext().getText(i));
    }

    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getContentDescription() : null;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(int i) {
        setTitle(getContext().getText(i));
    }

    public void setTitle(CharSequence charSequence) {
        TextView textView;
        if (TextUtils.isEmpty(charSequence)) {
            textView = this.mTitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        } else {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new AppCompatTextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mTitleTextColor;
                if (i2 != 0) {
                    this.mTitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        }
        textView = this.mTitleTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public void setSubtitle(int i) {
        setSubtitle(getContext().getText(i));
    }

    public void setSubtitle(CharSequence charSequence) {
        TextView textView;
        if (TextUtils.isEmpty(charSequence)) {
            textView = this.mSubtitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                this.mSubtitleTextView = new AppCompatTextView(context);
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mSubtitleTextColor;
                if (i2 != 0) {
                    this.mSubtitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        }
        textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    public void setTitleTextAppearance(Context context, int i) {
        this.mTitleTextAppearance = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setSubtitleTextAppearance(Context context, int i) {
        this.mSubtitleTextAppearance = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setTitleTextColor(int i) {
        this.mTitleTextColor = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public void setSubtitleTextColor(int i) {
        this.mSubtitleTextColor = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public void setNavigationContentDescription(int i) {
        setNavigationContentDescription(i != 0 ? getContext().getText(i) : null);
    }

    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setNavigationIcon(int i) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setNavigationIcon(Drawable drawable) {
        ImageButton imageButton;
        if (drawable != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            imageButton = this.mNavButtonView;
            if (imageButton != null && isChildOrHidden(imageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(drawable);
        }
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    public void setNavigationOnClickListener(OnClickListener onClickListener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public void setOverflowIcon(Drawable drawable) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(drawable);
    }

    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            this.mMenuView = new ActionMenuView(getContext());
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388613 | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(generateDefaultLayoutParams);
            addSystemView(this.mMenuView, false);
        }
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setContentInsetsRelative(int i, int i2) {
        ensureContentInsets();
        this.mContentInsets.setRelative(i, i2);
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getStart() : 0;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getEnd() : 0;
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getLeft() : 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getRight() : 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        return i != Integer.MIN_VALUE ? i : getContentInsetStart();
    }

    public void setContentInsetStartWithNavigation(int i) {
        if (i < 0) {
            i = Integer.MIN_VALUE;
        }
        if (i != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = i;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        return i != Integer.MIN_VALUE ? i : getContentInsetEnd();
    }

    public void setContentInsetEndWithActions(int i) {
        if (i < 0) {
            i = Integer.MIN_VALUE;
        }
        if (i != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = i;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getCurrentContentInsetStart() {
        if (getNavigationIcon() != null) {
            return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return getContentInsetStart();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0016  */
    public int getCurrentContentInsetEnd() {
        /*
        r3 = this;
        r0 = r3.mMenuView;
        r1 = 0;
        if (r0 == 0) goto L_0x0013;
    L_0x0005:
        r0 = r0.peekMenu();
        if (r0 == 0) goto L_0x0013;
    L_0x000b:
        r0 = r0.hasVisibleItems();
        if (r0 == 0) goto L_0x0013;
    L_0x0011:
        r0 = 1;
        goto L_0x0014;
    L_0x0013:
        r0 = 0;
    L_0x0014:
        if (r0 == 0) goto L_0x0025;
    L_0x0016:
        r0 = r3.getContentInsetEnd();
        r2 = r3.mContentInsetEndWithActions;
        r1 = java.lang.Math.max(r2, r1);
        r0 = java.lang.Math.max(r0, r1);
        goto L_0x0029;
    L_0x0025:
        r0 = r3.getContentInsetEnd();
    L_0x0029:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.getCurrentContentInsetEnd():int");
    }

    public int getCurrentContentInsetLeft() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetEnd();
        }
        return getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetStart();
        }
        return getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), null, R$attr.toolbarNavigationButtonStyle);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(generateDefaultLayoutParams);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            this.mCollapseButtonView = new AppCompatImageButton(getContext(), null, R$attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener(new C00313());
        }
    }

    private void addSystemView(View view, boolean z) {
        LayoutParams generateDefaultLayoutParams;
        android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            generateDefaultLayoutParams = generateDefaultLayoutParams();
        } else if (checkLayoutParams(layoutParams)) {
            generateDefaultLayoutParams = (LayoutParams) layoutParams;
        } else {
            generateDefaultLayoutParams = generateLayoutParams(layoutParams);
        }
        generateDefaultLayoutParams.mViewType = 1;
        if (!z || this.mExpandedActionView == null) {
            addView(view, generateDefaultLayoutParams);
            return;
        }
        view.setLayoutParams(generateDefaultLayoutParams);
        this.mHiddenViews.add(view);
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null) {
            MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter.mCurrentExpandedItem;
            if (menuItemImpl != null) {
                savedState.expandedMenuItemId = menuItemImpl.getItemId();
            }
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        return savedState;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            ActionMenuView actionMenuView = this.mMenuView;
            Menu peekMenu = actionMenuView != null ? actionMenuView.peekMenu() : null;
            int i = savedState.expandedMenuItemId;
            if (!(i == 0 || this.mExpandedMenuPresenter == null || peekMenu == null)) {
                MenuItem findItem = peekMenu.findItem(i);
                if (findItem != null) {
                    findItem.expandActionView();
                }
            }
            if (savedState.isOverflowOpen) {
                postShowOverflowMenu();
            }
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    private void measureChildConstrained(View view, int i, int i2, int i3, int i4, int i5) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        i = ViewGroup.getChildMeasureSpec(i, (((getPaddingLeft() + getPaddingRight()) + marginLayoutParams.leftMargin) + marginLayoutParams.rightMargin) + i2, marginLayoutParams.width);
        i2 = ViewGroup.getChildMeasureSpec(i3, (((getPaddingTop() + getPaddingBottom()) + marginLayoutParams.topMargin) + marginLayoutParams.bottomMargin) + i4, marginLayoutParams.height);
        i3 = MeasureSpec.getMode(i2);
        if (i3 != 1073741824 && i5 >= 0) {
            if (i3 != 0) {
                i5 = Math.min(MeasureSpec.getSize(i2), i5);
            }
            i2 = MeasureSpec.makeMeasureSpec(i5, 1073741824);
        }
        view.measure(i, i2);
    }

    private int measureChildCollapseMargins(View view, int i, int i2, int i3, int i4, int[] iArr) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        int i5 = marginLayoutParams.leftMargin - iArr[0];
        int i6 = marginLayoutParams.rightMargin - iArr[1];
        int max = Math.max(0, i5) + Math.max(0, i6);
        iArr[0] = Math.max(0, -i5);
        iArr[1] = Math.max(0, -i6);
        view.measure(ViewGroup.getChildMeasureSpec(i, ((getPaddingLeft() + getPaddingRight()) + max) + i2, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i3, (((getPaddingTop() + getPaddingBottom()) + marginLayoutParams.topMargin) + marginLayoutParams.bottomMargin) + i4, marginLayoutParams.height));
        return view.getMeasuredWidth() + max;
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int measuredWidth;
        int max;
        int combineMeasuredStates;
        int measuredHeight;
        int combineMeasuredStates2;
        int[] iArr = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            i3 = 1;
            i4 = 0;
        } else {
            i3 = 0;
            i4 = 1;
        }
        if (shouldLayout(this.mNavButtonView)) {
            measureChildConstrained(this.mNavButtonView, i, 0, i2, 0, this.mMaxButtonHeight);
            measuredWidth = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            max = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            combineMeasuredStates = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        } else {
            measuredWidth = 0;
            max = 0;
            combineMeasuredStates = 0;
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, i, 0, i2, 0, this.mMaxButtonHeight);
            measuredWidth = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            max = Math.max(max, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, this.mCollapseButtonView.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int max2 = 0 + Math.max(currentContentInsetStart, measuredWidth);
        iArr[i3] = Math.max(0, currentContentInsetStart - measuredWidth);
        if (shouldLayout(this.mMenuView)) {
            measureChildConstrained(this.mMenuView, i, max2, i2, 0, this.mMaxButtonHeight);
            measuredWidth = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            max = Math.max(max, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, this.mMenuView.getMeasuredState());
        } else {
            measuredWidth = 0;
        }
        currentContentInsetStart = getCurrentContentInsetEnd();
        i3 = max2 + Math.max(currentContentInsetStart, measuredWidth);
        iArr[i4] = Math.max(0, currentContentInsetStart - measuredWidth);
        if (shouldLayout(this.mExpandedActionView)) {
            i3 += measureChildCollapseMargins(this.mExpandedActionView, i, i3, i2, 0, iArr);
            max = Math.max(max, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, this.mExpandedActionView.getMeasuredState());
        }
        if (shouldLayout(this.mLogoView)) {
            i3 += measureChildCollapseMargins(this.mLogoView, i, i3, i2, 0, iArr);
            max = Math.max(max, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, this.mLogoView.getMeasuredState());
        }
        i4 = getChildCount();
        max2 = max;
        max = i3;
        for (i3 = 0; i3 < i4; i3++) {
            View childAt = getChildAt(i3);
            if (((LayoutParams) childAt.getLayoutParams()).mViewType == 0 && shouldLayout(childAt)) {
                max += measureChildCollapseMargins(childAt, i, max, i2, 0, iArr);
                max2 = Math.max(max2, childAt.getMeasuredHeight() + getVerticalMargins(childAt));
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, childAt.getMeasuredState());
            }
        }
        i3 = this.mTitleMarginTop + this.mTitleMarginBottom;
        i4 = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (shouldLayout(this.mTitleTextView)) {
            measureChildCollapseMargins(this.mTitleTextView, i, max + i4, i2, i3, iArr);
            measuredWidth = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
            measuredHeight = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, this.mTitleTextView.getMeasuredState());
            combineMeasuredStates = measuredWidth;
        } else {
            combineMeasuredStates2 = combineMeasuredStates;
            combineMeasuredStates = 0;
            measuredHeight = 0;
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            int i5 = measuredHeight + i3;
            i3 = combineMeasuredStates2;
            combineMeasuredStates = Math.max(combineMeasuredStates, measureChildCollapseMargins(this.mSubtitleTextView, i, max + i4, i2, i5, iArr));
            measuredHeight += this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            combineMeasuredStates2 = View.combineMeasuredStates(i3, this.mSubtitleTextView.getMeasuredState());
        } else {
            i3 = combineMeasuredStates2;
        }
        max += combineMeasuredStates;
        measuredWidth = Math.max(max2, measuredHeight) + (getPaddingTop() + getPaddingBottom());
        int i6 = i;
        currentContentInsetStart = View.resolveSizeAndState(Math.max(max + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), i6, Theme.ACTION_BAR_VIDEO_EDIT_COLOR & combineMeasuredStates2);
        measuredWidth = View.resolveSizeAndState(Math.max(measuredWidth, getSuggestedMinimumHeight()), i2, combineMeasuredStates2 << 16);
        if (shouldCollapse()) {
            measuredWidth = 0;
        }
        setMeasuredDimension(currentContentInsetStart, measuredWidth);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x02a6 A:{LOOP_END, LOOP:0: B:101:0x02a4->B:102:0x02a6} */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x02c8 A:{LOOP_END, LOOP:1: B:104:0x02c6->B:105:0x02c8} */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x02f3  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0302 A:{LOOP_END, LOOP:2: B:112:0x0300->B:113:0x0302} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0137  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x016a  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x022c  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01b9  */
    public void onLayout(boolean r20, int r21, int r22, int r23, int r24) {
        /*
        r19 = this;
        r0 = r19;
        r1 = androidx.core.view.ViewCompat.getLayoutDirection(r19);
        r2 = 1;
        r3 = 0;
        if (r1 != r2) goto L_0x000c;
    L_0x000a:
        r1 = 1;
        goto L_0x000d;
    L_0x000c:
        r1 = 0;
    L_0x000d:
        r4 = r19.getWidth();
        r5 = r19.getHeight();
        r6 = r19.getPaddingLeft();
        r7 = r19.getPaddingRight();
        r8 = r19.getPaddingTop();
        r9 = r19.getPaddingBottom();
        r10 = r4 - r7;
        r11 = r0.mTempMargins;
        r11[r2] = r3;
        r11[r3] = r3;
        r12 = androidx.core.view.ViewCompat.getMinimumHeight(r19);
        if (r12 < 0) goto L_0x003a;
    L_0x0033:
        r13 = r24 - r22;
        r12 = java.lang.Math.min(r12, r13);
        goto L_0x003b;
    L_0x003a:
        r12 = 0;
    L_0x003b:
        r13 = r0.mNavButtonView;
        r13 = r0.shouldLayout(r13);
        if (r13 == 0) goto L_0x0055;
    L_0x0043:
        if (r1 == 0) goto L_0x004e;
    L_0x0045:
        r13 = r0.mNavButtonView;
        r13 = r0.layoutChildRight(r13, r10, r11, r12);
        r14 = r13;
        r13 = r6;
        goto L_0x0057;
    L_0x004e:
        r13 = r0.mNavButtonView;
        r13 = r0.layoutChildLeft(r13, r6, r11, r12);
        goto L_0x0056;
    L_0x0055:
        r13 = r6;
    L_0x0056:
        r14 = r10;
    L_0x0057:
        r15 = r0.mCollapseButtonView;
        r15 = r0.shouldLayout(r15);
        if (r15 == 0) goto L_0x006e;
    L_0x005f:
        if (r1 == 0) goto L_0x0068;
    L_0x0061:
        r15 = r0.mCollapseButtonView;
        r14 = r0.layoutChildRight(r15, r14, r11, r12);
        goto L_0x006e;
    L_0x0068:
        r15 = r0.mCollapseButtonView;
        r13 = r0.layoutChildLeft(r15, r13, r11, r12);
    L_0x006e:
        r15 = r0.mMenuView;
        r15 = r0.shouldLayout(r15);
        if (r15 == 0) goto L_0x0085;
    L_0x0076:
        if (r1 == 0) goto L_0x007f;
    L_0x0078:
        r15 = r0.mMenuView;
        r13 = r0.layoutChildLeft(r15, r13, r11, r12);
        goto L_0x0085;
    L_0x007f:
        r15 = r0.mMenuView;
        r14 = r0.layoutChildRight(r15, r14, r11, r12);
    L_0x0085:
        r15 = r19.getCurrentContentInsetLeft();
        r16 = r19.getCurrentContentInsetRight();
        r2 = r15 - r13;
        r2 = java.lang.Math.max(r3, r2);
        r11[r3] = r2;
        r2 = r10 - r14;
        r2 = r16 - r2;
        r2 = java.lang.Math.max(r3, r2);
        r17 = 1;
        r11[r17] = r2;
        r2 = java.lang.Math.max(r13, r15);
        r10 = r10 - r16;
        r10 = java.lang.Math.min(r14, r10);
        r13 = r0.mExpandedActionView;
        r13 = r0.shouldLayout(r13);
        if (r13 == 0) goto L_0x00c2;
    L_0x00b3:
        if (r1 == 0) goto L_0x00bc;
    L_0x00b5:
        r13 = r0.mExpandedActionView;
        r10 = r0.layoutChildRight(r13, r10, r11, r12);
        goto L_0x00c2;
    L_0x00bc:
        r13 = r0.mExpandedActionView;
        r2 = r0.layoutChildLeft(r13, r2, r11, r12);
    L_0x00c2:
        r13 = r0.mLogoView;
        r13 = r0.shouldLayout(r13);
        if (r13 == 0) goto L_0x00d9;
    L_0x00ca:
        if (r1 == 0) goto L_0x00d3;
    L_0x00cc:
        r13 = r0.mLogoView;
        r10 = r0.layoutChildRight(r13, r10, r11, r12);
        goto L_0x00d9;
    L_0x00d3:
        r13 = r0.mLogoView;
        r2 = r0.layoutChildLeft(r13, r2, r11, r12);
    L_0x00d9:
        r13 = r0.mTitleTextView;
        r13 = r0.shouldLayout(r13);
        r14 = r0.mSubtitleTextView;
        r14 = r0.shouldLayout(r14);
        if (r13 == 0) goto L_0x0100;
    L_0x00e7:
        r15 = r0.mTitleTextView;
        r15 = r15.getLayoutParams();
        r15 = (androidx.appcompat.widget.Toolbar.LayoutParams) r15;
        r3 = r15.topMargin;
        r23 = r7;
        r7 = r0.mTitleTextView;
        r7 = r7.getMeasuredHeight();
        r3 = r3 + r7;
        r7 = r15.bottomMargin;
        r3 = r3 + r7;
        r7 = 0;
        r3 = r3 + r7;
        goto L_0x0103;
    L_0x0100:
        r23 = r7;
        r3 = 0;
    L_0x0103:
        if (r14 == 0) goto L_0x011d;
    L_0x0105:
        r7 = r0.mSubtitleTextView;
        r7 = r7.getLayoutParams();
        r7 = (androidx.appcompat.widget.Toolbar.LayoutParams) r7;
        r15 = r7.topMargin;
        r16 = r4;
        r4 = r0.mSubtitleTextView;
        r4 = r4.getMeasuredHeight();
        r15 = r15 + r4;
        r4 = r7.bottomMargin;
        r15 = r15 + r4;
        r3 = r3 + r15;
        goto L_0x011f;
    L_0x011d:
        r16 = r4;
    L_0x011f:
        if (r13 != 0) goto L_0x012b;
    L_0x0121:
        if (r14 == 0) goto L_0x0124;
    L_0x0123:
        goto L_0x012b;
    L_0x0124:
        r17 = r6;
        r22 = r12;
    L_0x0128:
        r7 = 0;
        goto L_0x0296;
    L_0x012b:
        if (r13 == 0) goto L_0x0130;
    L_0x012d:
        r4 = r0.mTitleTextView;
        goto L_0x0132;
    L_0x0130:
        r4 = r0.mSubtitleTextView;
    L_0x0132:
        if (r14 == 0) goto L_0x0137;
    L_0x0134:
        r7 = r0.mSubtitleTextView;
        goto L_0x0139;
    L_0x0137:
        r7 = r0.mTitleTextView;
    L_0x0139:
        r4 = r4.getLayoutParams();
        r4 = (androidx.appcompat.widget.Toolbar.LayoutParams) r4;
        r7 = r7.getLayoutParams();
        r7 = (androidx.appcompat.widget.Toolbar.LayoutParams) r7;
        if (r13 == 0) goto L_0x014f;
    L_0x0147:
        r15 = r0.mTitleTextView;
        r15 = r15.getMeasuredWidth();
        if (r15 > 0) goto L_0x0159;
    L_0x014f:
        if (r14 == 0) goto L_0x015d;
    L_0x0151:
        r15 = r0.mSubtitleTextView;
        r15 = r15.getMeasuredWidth();
        if (r15 <= 0) goto L_0x015d;
    L_0x0159:
        r17 = r6;
        r15 = 1;
        goto L_0x0160;
    L_0x015d:
        r17 = r6;
        r15 = 0;
    L_0x0160:
        r6 = r0.mGravity;
        r6 = r6 & 112;
        r22 = r12;
        r12 = 48;
        if (r6 == r12) goto L_0x01a8;
    L_0x016a:
        r12 = 80;
        if (r6 == r12) goto L_0x019a;
    L_0x016e:
        r6 = r5 - r8;
        r6 = r6 - r9;
        r6 = r6 - r3;
        r6 = r6 / 2;
        r12 = r4.topMargin;
        r24 = r2;
        r2 = r0.mTitleMarginTop;
        r18 = r14;
        r14 = r12 + r2;
        if (r6 >= r14) goto L_0x0183;
    L_0x0180:
        r6 = r12 + r2;
        goto L_0x0198;
    L_0x0183:
        r5 = r5 - r9;
        r5 = r5 - r3;
        r5 = r5 - r6;
        r5 = r5 - r8;
        r2 = r4.bottomMargin;
        r3 = r0.mTitleMarginBottom;
        r2 = r2 + r3;
        if (r5 >= r2) goto L_0x0198;
    L_0x018e:
        r2 = r7.bottomMargin;
        r2 = r2 + r3;
        r2 = r2 - r5;
        r6 = r6 - r2;
        r2 = 0;
        r6 = java.lang.Math.max(r2, r6);
    L_0x0198:
        r8 = r8 + r6;
        goto L_0x01b7;
    L_0x019a:
        r24 = r2;
        r18 = r14;
        r5 = r5 - r9;
        r2 = r7.bottomMargin;
        r5 = r5 - r2;
        r2 = r0.mTitleMarginBottom;
        r5 = r5 - r2;
        r8 = r5 - r3;
        goto L_0x01b7;
    L_0x01a8:
        r24 = r2;
        r18 = r14;
        r2 = r19.getPaddingTop();
        r3 = r4.topMargin;
        r2 = r2 + r3;
        r3 = r0.mTitleMarginTop;
        r8 = r2 + r3;
    L_0x01b7:
        if (r1 == 0) goto L_0x022c;
    L_0x01b9:
        if (r15 == 0) goto L_0x01bf;
    L_0x01bb:
        r3 = r0.mTitleMarginStart;
        r1 = 1;
        goto L_0x01c1;
    L_0x01bf:
        r1 = 1;
        r3 = 0;
    L_0x01c1:
        r2 = r11[r1];
        r3 = r3 - r2;
        r2 = 0;
        r4 = java.lang.Math.max(r2, r3);
        r10 = r10 - r4;
        r3 = -r3;
        r3 = java.lang.Math.max(r2, r3);
        r11[r1] = r3;
        if (r13 == 0) goto L_0x01f7;
    L_0x01d3:
        r1 = r0.mTitleTextView;
        r1 = r1.getLayoutParams();
        r1 = (androidx.appcompat.widget.Toolbar.LayoutParams) r1;
        r2 = r0.mTitleTextView;
        r2 = r2.getMeasuredWidth();
        r2 = r10 - r2;
        r3 = r0.mTitleTextView;
        r3 = r3.getMeasuredHeight();
        r3 = r3 + r8;
        r4 = r0.mTitleTextView;
        r4.layout(r2, r8, r10, r3);
        r4 = r0.mTitleMarginEnd;
        r2 = r2 - r4;
        r1 = r1.bottomMargin;
        r8 = r3 + r1;
        goto L_0x01f8;
    L_0x01f7:
        r2 = r10;
    L_0x01f8:
        if (r18 == 0) goto L_0x0220;
    L_0x01fa:
        r1 = r0.mSubtitleTextView;
        r1 = r1.getLayoutParams();
        r1 = (androidx.appcompat.widget.Toolbar.LayoutParams) r1;
        r3 = r1.topMargin;
        r8 = r8 + r3;
        r3 = r0.mSubtitleTextView;
        r3 = r3.getMeasuredWidth();
        r3 = r10 - r3;
        r4 = r0.mSubtitleTextView;
        r4 = r4.getMeasuredHeight();
        r4 = r4 + r8;
        r5 = r0.mSubtitleTextView;
        r5.layout(r3, r8, r10, r4);
        r3 = r0.mTitleMarginEnd;
        r3 = r10 - r3;
        r1 = r1.bottomMargin;
        goto L_0x0221;
    L_0x0220:
        r3 = r10;
    L_0x0221:
        if (r15 == 0) goto L_0x0228;
    L_0x0223:
        r1 = java.lang.Math.min(r2, r3);
        r10 = r1;
    L_0x0228:
        r2 = r24;
        goto L_0x0128;
    L_0x022c:
        if (r15 == 0) goto L_0x0231;
    L_0x022e:
        r3 = r0.mTitleMarginStart;
        goto L_0x0232;
    L_0x0231:
        r3 = 0;
    L_0x0232:
        r7 = 0;
        r1 = r11[r7];
        r3 = r3 - r1;
        r1 = java.lang.Math.max(r7, r3);
        r2 = r24 + r1;
        r1 = -r3;
        r1 = java.lang.Math.max(r7, r1);
        r11[r7] = r1;
        if (r13 == 0) goto L_0x0268;
    L_0x0245:
        r1 = r0.mTitleTextView;
        r1 = r1.getLayoutParams();
        r1 = (androidx.appcompat.widget.Toolbar.LayoutParams) r1;
        r3 = r0.mTitleTextView;
        r3 = r3.getMeasuredWidth();
        r3 = r3 + r2;
        r4 = r0.mTitleTextView;
        r4 = r4.getMeasuredHeight();
        r4 = r4 + r8;
        r5 = r0.mTitleTextView;
        r5.layout(r2, r8, r3, r4);
        r5 = r0.mTitleMarginEnd;
        r3 = r3 + r5;
        r1 = r1.bottomMargin;
        r8 = r4 + r1;
        goto L_0x0269;
    L_0x0268:
        r3 = r2;
    L_0x0269:
        if (r18 == 0) goto L_0x028f;
    L_0x026b:
        r1 = r0.mSubtitleTextView;
        r1 = r1.getLayoutParams();
        r1 = (androidx.appcompat.widget.Toolbar.LayoutParams) r1;
        r4 = r1.topMargin;
        r8 = r8 + r4;
        r4 = r0.mSubtitleTextView;
        r4 = r4.getMeasuredWidth();
        r4 = r4 + r2;
        r5 = r0.mSubtitleTextView;
        r5 = r5.getMeasuredHeight();
        r5 = r5 + r8;
        r6 = r0.mSubtitleTextView;
        r6.layout(r2, r8, r4, r5);
        r5 = r0.mTitleMarginEnd;
        r4 = r4 + r5;
        r1 = r1.bottomMargin;
        goto L_0x0290;
    L_0x028f:
        r4 = r2;
    L_0x0290:
        if (r15 == 0) goto L_0x0296;
    L_0x0292:
        r2 = java.lang.Math.max(r3, r4);
    L_0x0296:
        r1 = r0.mTempViews;
        r3 = 3;
        r0.addCustomViewsWithGravity(r1, r3);
        r1 = r0.mTempViews;
        r1 = r1.size();
        r3 = r2;
        r2 = 0;
    L_0x02a4:
        if (r2 >= r1) goto L_0x02b7;
    L_0x02a6:
        r4 = r0.mTempViews;
        r4 = r4.get(r2);
        r4 = (android.view.View) r4;
        r12 = r22;
        r3 = r0.layoutChildLeft(r4, r3, r11, r12);
        r2 = r2 + 1;
        goto L_0x02a4;
    L_0x02b7:
        r12 = r22;
        r1 = r0.mTempViews;
        r2 = 5;
        r0.addCustomViewsWithGravity(r1, r2);
        r1 = r0.mTempViews;
        r1 = r1.size();
        r2 = 0;
    L_0x02c6:
        if (r2 >= r1) goto L_0x02d7;
    L_0x02c8:
        r4 = r0.mTempViews;
        r4 = r4.get(r2);
        r4 = (android.view.View) r4;
        r10 = r0.layoutChildRight(r4, r10, r11, r12);
        r2 = r2 + 1;
        goto L_0x02c6;
    L_0x02d7:
        r1 = r0.mTempViews;
        r2 = 1;
        r0.addCustomViewsWithGravity(r1, r2);
        r1 = r0.mTempViews;
        r1 = r0.getViewListMeasuredWidth(r1, r11);
        r4 = r16 - r17;
        r4 = r4 - r23;
        r4 = r4 / 2;
        r6 = r17 + r4;
        r2 = r1 / 2;
        r2 = r6 - r2;
        r1 = r1 + r2;
        if (r2 >= r3) goto L_0x02f3;
    L_0x02f2:
        goto L_0x02fa;
    L_0x02f3:
        if (r1 <= r10) goto L_0x02f9;
    L_0x02f5:
        r1 = r1 - r10;
        r3 = r2 - r1;
        goto L_0x02fa;
    L_0x02f9:
        r3 = r2;
    L_0x02fa:
        r1 = r0.mTempViews;
        r1 = r1.size();
    L_0x0300:
        if (r7 >= r1) goto L_0x0311;
    L_0x0302:
        r2 = r0.mTempViews;
        r2 = r2.get(r7);
        r2 = (android.view.View) r2;
        r3 = r0.layoutChildLeft(r2, r3, r11, r12);
        r7 = r7 + 1;
        goto L_0x0300;
    L_0x0311:
        r1 = r0.mTempViews;
        r1.clear();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    private int getViewListMeasuredWidth(List<View> list, int[] iArr) {
        int i = iArr[0];
        int i2 = iArr[1];
        int size = list.size();
        int i3 = i2;
        int i4 = i;
        i2 = 0;
        i = 0;
        while (i2 < size) {
            View view = (View) list.get(i2);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i5 = layoutParams.leftMargin - i4;
            i4 = layoutParams.rightMargin - i3;
            i3 = Math.max(0, i5);
            int max = Math.max(0, i4);
            i5 = Math.max(0, -i5);
            i += (i3 + view.getMeasuredWidth()) + max;
            i2++;
            i3 = Math.max(0, -i4);
            i4 = i5;
        }
        return i;
    }

    private int layoutChildLeft(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = layoutParams.leftMargin - iArr[0];
        i += Math.max(0, i3);
        iArr[0] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        i2 = view.getMeasuredWidth();
        view.layout(i, childTop, i + i2, view.getMeasuredHeight() + childTop);
        return i + (i2 + layoutParams.rightMargin);
    }

    private int layoutChildRight(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = layoutParams.rightMargin - iArr[1];
        i -= Math.max(0, i3);
        iArr[1] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        i2 = view.getMeasuredWidth();
        view.layout(i - i2, childTop, i, view.getMeasuredHeight() + childTop);
        return i - (i2 + layoutParams.leftMargin);
    }

    private int getChildTop(View view, int i) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        i = i > 0 ? (measuredHeight - i) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(layoutParams.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - i;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - layoutParams.bottomMargin) - i;
        }
        i = getPaddingTop();
        childVerticalGravity = getPaddingBottom();
        int height = getHeight();
        int i2 = (((height - i) - childVerticalGravity) - measuredHeight) / 2;
        int i3 = layoutParams.topMargin;
        if (i2 < i3) {
            i2 = i3;
        } else {
            height = (((height - childVerticalGravity) - measuredHeight) - i2) - i;
            measuredHeight = layoutParams.bottomMargin;
            if (height < measuredHeight) {
                i2 = Math.max(0, i2 - (measuredHeight - height));
            }
        }
        return i + i2;
    }

    private int getChildVerticalGravity(int i) {
        i &= 112;
        return (i == 16 || i == 48 || i == 80) ? i : this.mGravity & 112;
    }

    private void addCustomViewsWithGravity(List<View> list, int i) {
        Object obj = ViewCompat.getLayoutDirection(this) == 1 ? 1 : null;
        int childCount = getChildCount();
        i = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        list.clear();
        View childAt;
        if (obj != null) {
            for (childCount--; childCount >= 0; childCount--) {
                childAt = getChildAt(childCount);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == i) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            childAt = getChildAt(i2);
            LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams2.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams2.gravity) == i) {
                list.add(childAt);
            }
        }
    }

    private int getChildHorizontalGravity(int i) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        i = GravityCompat.getAbsoluteGravity(i, layoutDirection) & 7;
        if (i != 1) {
            int i2 = 3;
            if (!(i == 3 || i == 5)) {
                if (layoutDirection == 1) {
                    i2 = 5;
                }
                return i2;
            }
        }
        return i;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private int getHorizontalMargins(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
    }

    private int getVerticalMargins(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ActionBar$LayoutParams) {
            return new LayoutParams((ActionBar$LayoutParams) layoutParams);
        }
        if (layoutParams instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeChildrenForExpandedActionView() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (!(((LayoutParams) childAt.getLayoutParams()).mViewType == 2 || childAt == this.mMenuView)) {
                removeViewAt(childCount);
                this.mHiddenViews.add(childAt);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void addChildrenForExpandedActionView() {
        for (int size = this.mHiddenViews.size() - 1; size >= 0; size--) {
            addView((View) this.mHiddenViews.get(size));
        }
        this.mHiddenViews.clear();
    }

    private boolean isChildOrHidden(View view) {
        return view.getParent() == this || this.mHiddenViews.contains(view);
    }

    public void setCollapsible(boolean z) {
        this.mCollapsible = z;
        requestLayout();
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    /* Access modifiers changed, original: 0000 */
    public Context getPopupContext() {
        return this.mPopupContext;
    }
}
