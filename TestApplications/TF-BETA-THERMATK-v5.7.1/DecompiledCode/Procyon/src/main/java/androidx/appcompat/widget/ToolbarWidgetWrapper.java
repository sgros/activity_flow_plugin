// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import androidx.appcompat.content.res.AppCompatResources;
import android.content.Context;
import android.view.ViewGroup$LayoutParams;
import android.view.MenuItem;
import androidx.appcompat.view.menu.ActionMenuItem;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.appcompat.R$drawable;
import androidx.appcompat.R$string;
import android.view.Window$Callback;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ToolbarWidgetWrapper implements DecorToolbar
{
    private View mCustomView;
    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;
    private int mDisplayOpts;
    private CharSequence mHomeDescription;
    private Drawable mIcon;
    private Drawable mLogo;
    boolean mMenuPrepared;
    private Drawable mNavIcon;
    private int mNavigationMode;
    private CharSequence mSubtitle;
    CharSequence mTitle;
    private boolean mTitleSet;
    Toolbar mToolbar;
    Window$Callback mWindowCallback;
    
    public ToolbarWidgetWrapper(final Toolbar toolbar, final boolean b) {
        this(toolbar, b, R$string.abc_action_bar_up_description, R$drawable.abc_ic_ab_back_material);
    }
    
    public ToolbarWidgetWrapper(final Toolbar mToolbar, final boolean b, final int defaultNavigationContentDescription, int popupTheme) {
        this.mNavigationMode = 0;
        this.mDefaultNavigationContentDescription = 0;
        this.mToolbar = mToolbar;
        this.mTitle = mToolbar.getTitle();
        this.mSubtitle = mToolbar.getSubtitle();
        this.mTitleSet = (this.mTitle != null);
        this.mNavIcon = mToolbar.getNavigationIcon();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(mToolbar.getContext(), null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
        this.mDefaultNavigationIcon = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_homeAsUpIndicator);
        if (b) {
            final CharSequence text = obtainStyledAttributes.getText(R$styleable.ActionBar_title);
            if (!TextUtils.isEmpty(text)) {
                this.setTitle(text);
            }
            final CharSequence text2 = obtainStyledAttributes.getText(R$styleable.ActionBar_subtitle);
            if (!TextUtils.isEmpty(text2)) {
                this.setSubtitle(text2);
            }
            final Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_logo);
            if (drawable != null) {
                this.setLogo(drawable);
            }
            final Drawable drawable2 = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_icon);
            if (drawable2 != null) {
                this.setIcon(drawable2);
            }
            if (this.mNavIcon == null) {
                final Drawable mDefaultNavigationIcon = this.mDefaultNavigationIcon;
                if (mDefaultNavigationIcon != null) {
                    this.setNavigationIcon(mDefaultNavigationIcon);
                }
            }
            this.setDisplayOptions(obtainStyledAttributes.getInt(R$styleable.ActionBar_displayOptions, 0));
            popupTheme = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_customNavigationLayout, 0);
            if (popupTheme != 0) {
                this.setCustomView(LayoutInflater.from(this.mToolbar.getContext()).inflate(popupTheme, (ViewGroup)this.mToolbar, false));
                this.setDisplayOptions(this.mDisplayOpts | 0x10);
            }
            popupTheme = obtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_height, 0);
            if (popupTheme > 0) {
                final ViewGroup$LayoutParams layoutParams = this.mToolbar.getLayoutParams();
                layoutParams.height = popupTheme;
                this.mToolbar.setLayoutParams(layoutParams);
            }
            popupTheme = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetStart, -1);
            final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ActionBar_contentInsetEnd, -1);
            if (popupTheme >= 0 || dimensionPixelOffset >= 0) {
                this.mToolbar.setContentInsetsRelative(Math.max(popupTheme, 0), Math.max(dimensionPixelOffset, 0));
            }
            popupTheme = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_titleTextStyle, 0);
            if (popupTheme != 0) {
                final Toolbar mToolbar2 = this.mToolbar;
                mToolbar2.setTitleTextAppearance(mToolbar2.getContext(), popupTheme);
            }
            popupTheme = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_subtitleTextStyle, 0);
            if (popupTheme != 0) {
                final Toolbar mToolbar3 = this.mToolbar;
                mToolbar3.setSubtitleTextAppearance(mToolbar3.getContext(), popupTheme);
            }
            popupTheme = obtainStyledAttributes.getResourceId(R$styleable.ActionBar_popupTheme, 0);
            if (popupTheme != 0) {
                this.mToolbar.setPopupTheme(popupTheme);
            }
        }
        else {
            this.mDisplayOpts = this.detectDisplayOptions();
        }
        obtainStyledAttributes.recycle();
        this.setDefaultNavigationContentDescription(defaultNavigationContentDescription);
        this.mHomeDescription = this.mToolbar.getNavigationContentDescription();
        this.mToolbar.setNavigationOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final ActionMenuItem mNavItem = new ActionMenuItem(ToolbarWidgetWrapper.this.mToolbar.getContext(), 0, 16908332, 0, 0, ToolbarWidgetWrapper.this.mTitle);
            
            public void onClick(final View view) {
                final ToolbarWidgetWrapper this$0 = ToolbarWidgetWrapper.this;
                final Window$Callback mWindowCallback = this$0.mWindowCallback;
                if (mWindowCallback != null && this$0.mMenuPrepared) {
                    mWindowCallback.onMenuItemSelected(0, (MenuItem)this.mNavItem);
                }
            }
        });
    }
    
    private int detectDisplayOptions() {
        int n;
        if (this.mToolbar.getNavigationIcon() != null) {
            n = 15;
            this.mDefaultNavigationIcon = this.mToolbar.getNavigationIcon();
        }
        else {
            n = 11;
        }
        return n;
    }
    
    private void setTitleInt(final CharSequence charSequence) {
        this.mTitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setTitle(charSequence);
        }
    }
    
    private void updateHomeAccessibility() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            if (TextUtils.isEmpty(this.mHomeDescription)) {
                this.mToolbar.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
            }
            else {
                this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
            }
        }
    }
    
    private void updateNavigationIcon() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            final Toolbar mToolbar = this.mToolbar;
            Drawable navigationIcon = this.mNavIcon;
            if (navigationIcon == null) {
                navigationIcon = this.mDefaultNavigationIcon;
            }
            mToolbar.setNavigationIcon(navigationIcon);
        }
        else {
            this.mToolbar.setNavigationIcon(null);
        }
    }
    
    private void updateToolbarLogo() {
        final int mDisplayOpts = this.mDisplayOpts;
        Drawable logo;
        if ((mDisplayOpts & 0x2) != 0x0) {
            if ((mDisplayOpts & 0x1) != 0x0) {
                logo = this.mLogo;
                if (logo == null) {
                    logo = this.mIcon;
                }
            }
            else {
                logo = this.mIcon;
            }
        }
        else {
            logo = null;
        }
        this.mToolbar.setLogo(logo);
    }
    
    public Context getContext() {
        return this.mToolbar.getContext();
    }
    
    @Override
    public CharSequence getTitle() {
        return this.mToolbar.getTitle();
    }
    
    public void setCustomView(final View mCustomView) {
        final View mCustomView2 = this.mCustomView;
        if (mCustomView2 != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.removeView(mCustomView2);
        }
        if ((this.mCustomView = mCustomView) != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.addView(this.mCustomView);
        }
    }
    
    public void setDefaultNavigationContentDescription(final int mDefaultNavigationContentDescription) {
        if (mDefaultNavigationContentDescription == this.mDefaultNavigationContentDescription) {
            return;
        }
        this.mDefaultNavigationContentDescription = mDefaultNavigationContentDescription;
        if (TextUtils.isEmpty(this.mToolbar.getNavigationContentDescription())) {
            this.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
        }
    }
    
    public void setDisplayOptions(final int mDisplayOpts) {
        final int n = this.mDisplayOpts ^ mDisplayOpts;
        this.mDisplayOpts = mDisplayOpts;
        if (n != 0) {
            if ((n & 0x4) != 0x0) {
                if ((mDisplayOpts & 0x4) != 0x0) {
                    this.updateHomeAccessibility();
                }
                this.updateNavigationIcon();
            }
            if ((n & 0x3) != 0x0) {
                this.updateToolbarLogo();
            }
            if ((n & 0x8) != 0x0) {
                if ((mDisplayOpts & 0x8) != 0x0) {
                    this.mToolbar.setTitle(this.mTitle);
                    this.mToolbar.setSubtitle(this.mSubtitle);
                }
                else {
                    this.mToolbar.setTitle(null);
                    this.mToolbar.setSubtitle(null);
                }
            }
            if ((n & 0x10) != 0x0) {
                final View mCustomView = this.mCustomView;
                if (mCustomView != null) {
                    if ((mDisplayOpts & 0x10) != 0x0) {
                        this.mToolbar.addView(mCustomView);
                    }
                    else {
                        this.mToolbar.removeView(mCustomView);
                    }
                }
            }
        }
    }
    
    @Override
    public void setIcon(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setIcon(drawable);
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        this.updateToolbarLogo();
    }
    
    @Override
    public void setLogo(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setLogo(drawable);
    }
    
    public void setLogo(final Drawable mLogo) {
        this.mLogo = mLogo;
        this.updateToolbarLogo();
    }
    
    public void setNavigationContentDescription(final int n) {
        CharSequence string;
        if (n == 0) {
            string = null;
        }
        else {
            string = this.getContext().getString(n);
        }
        this.setNavigationContentDescription(string);
    }
    
    public void setNavigationContentDescription(final CharSequence mHomeDescription) {
        this.mHomeDescription = mHomeDescription;
        this.updateHomeAccessibility();
    }
    
    public void setNavigationIcon(final Drawable mNavIcon) {
        this.mNavIcon = mNavIcon;
        this.updateNavigationIcon();
    }
    
    public void setSubtitle(final CharSequence charSequence) {
        this.mSubtitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setSubtitle(charSequence);
        }
    }
    
    public void setTitle(final CharSequence titleInt) {
        this.mTitleSet = true;
        this.setTitleInt(titleInt);
    }
    
    @Override
    public void setWindowCallback(final Window$Callback mWindowCallback) {
        this.mWindowCallback = mWindowCallback;
    }
    
    @Override
    public void setWindowTitle(final CharSequence titleInt) {
        if (!this.mTitleSet) {
            this.setTitleInt(titleInt);
        }
    }
}
