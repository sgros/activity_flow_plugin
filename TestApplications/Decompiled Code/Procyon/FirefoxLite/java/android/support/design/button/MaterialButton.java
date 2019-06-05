// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.button;

import android.graphics.Paint;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.widget.TextView;
import android.support.v4.widget.TextViewCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.res.TypedArray;
import android.support.design.resources.MaterialResources;
import android.support.design.internal.ViewUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;

public class MaterialButton extends AppCompatButton
{
    private Drawable icon;
    private int iconGravity;
    private int iconLeft;
    private int iconPadding;
    private int iconSize;
    private ColorStateList iconTint;
    private PorterDuff$Mode iconTintMode;
    private final MaterialButtonHelper materialButtonHelper;
    
    public MaterialButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.materialButtonStyle);
    }
    
    public MaterialButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, set, R.styleable.MaterialButton, n, R.style.Widget_MaterialComponents_Button, new int[0]);
        this.iconPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialButton_iconPadding, 0);
        this.iconTintMode = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.MaterialButton_iconTintMode, -1), PorterDuff$Mode.SRC_IN);
        this.iconTint = MaterialResources.getColorStateList(this.getContext(), obtainStyledAttributes, R.styleable.MaterialButton_iconTint);
        this.icon = MaterialResources.getDrawable(this.getContext(), obtainStyledAttributes, R.styleable.MaterialButton_icon);
        this.iconGravity = obtainStyledAttributes.getInteger(R.styleable.MaterialButton_iconGravity, 1);
        this.iconSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialButton_iconSize, 0);
        (this.materialButtonHelper = new MaterialButtonHelper(this)).loadFromAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        this.setCompoundDrawablePadding(this.iconPadding);
        this.updateIcon();
    }
    
    private boolean isLayoutRTL() {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    private boolean isUsingOriginalBackground() {
        return this.materialButtonHelper != null && !this.materialButtonHelper.isBackgroundOverwritten();
    }
    
    private void updateIcon() {
        if (this.icon != null) {
            DrawableCompat.setTintList(this.icon = this.icon.mutate(), this.iconTint);
            if (this.iconTintMode != null) {
                DrawableCompat.setTintMode(this.icon, this.iconTintMode);
            }
            int n;
            if (this.iconSize != 0) {
                n = this.iconSize;
            }
            else {
                n = this.icon.getIntrinsicWidth();
            }
            int n2;
            if (this.iconSize != 0) {
                n2 = this.iconSize;
            }
            else {
                n2 = this.icon.getIntrinsicHeight();
            }
            this.icon.setBounds(this.iconLeft, 0, this.iconLeft + n, n2);
        }
        TextViewCompat.setCompoundDrawablesRelative((TextView)this, this.icon, null, null, null);
    }
    
    public ColorStateList getBackgroundTintList() {
        return this.getSupportBackgroundTintList();
    }
    
    public PorterDuff$Mode getBackgroundTintMode() {
        return this.getSupportBackgroundTintMode();
    }
    
    public int getCornerRadius() {
        int cornerRadius;
        if (this.isUsingOriginalBackground()) {
            cornerRadius = this.materialButtonHelper.getCornerRadius();
        }
        else {
            cornerRadius = 0;
        }
        return cornerRadius;
    }
    
    public Drawable getIcon() {
        return this.icon;
    }
    
    public int getIconGravity() {
        return this.iconGravity;
    }
    
    public int getIconPadding() {
        return this.iconPadding;
    }
    
    public int getIconSize() {
        return this.iconSize;
    }
    
    public ColorStateList getIconTint() {
        return this.iconTint;
    }
    
    public PorterDuff$Mode getIconTintMode() {
        return this.iconTintMode;
    }
    
    public ColorStateList getRippleColor() {
        ColorStateList rippleColor;
        if (this.isUsingOriginalBackground()) {
            rippleColor = this.materialButtonHelper.getRippleColor();
        }
        else {
            rippleColor = null;
        }
        return rippleColor;
    }
    
    public ColorStateList getStrokeColor() {
        ColorStateList strokeColor;
        if (this.isUsingOriginalBackground()) {
            strokeColor = this.materialButtonHelper.getStrokeColor();
        }
        else {
            strokeColor = null;
        }
        return strokeColor;
    }
    
    public int getStrokeWidth() {
        int strokeWidth;
        if (this.isUsingOriginalBackground()) {
            strokeWidth = this.materialButtonHelper.getStrokeWidth();
        }
        else {
            strokeWidth = 0;
        }
        return strokeWidth;
    }
    
    @Override
    public ColorStateList getSupportBackgroundTintList() {
        if (this.isUsingOriginalBackground()) {
            return this.materialButtonHelper.getSupportBackgroundTintList();
        }
        return super.getSupportBackgroundTintList();
    }
    
    @Override
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        if (this.isUsingOriginalBackground()) {
            return this.materialButtonHelper.getSupportBackgroundTintMode();
        }
        return super.getSupportBackgroundTintMode();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (Build$VERSION.SDK_INT < 21 && this.isUsingOriginalBackground()) {
            this.materialButtonHelper.drawStroke(canvas);
        }
    }
    
    @Override
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (Build$VERSION.SDK_INT == 21 && this.materialButtonHelper != null) {
            this.materialButtonHelper.updateMaskBounds(n4 - n2, n3 - n);
        }
    }
    
    protected void onMeasure(int iconLeft, int n) {
        super.onMeasure(iconLeft, n);
        if (this.icon != null && this.iconGravity == 2) {
            n = (int)((Paint)this.getPaint()).measureText(this.getText().toString());
            if (this.iconSize == 0) {
                iconLeft = this.icon.getIntrinsicWidth();
            }
            else {
                iconLeft = this.iconSize;
            }
            n = (iconLeft = (this.getMeasuredWidth() - n - ViewCompat.getPaddingEnd((View)this) - iconLeft - this.iconPadding - ViewCompat.getPaddingStart((View)this)) / 2);
            if (this.isLayoutRTL()) {
                iconLeft = -n;
            }
            if (this.iconLeft != iconLeft) {
                this.iconLeft = iconLeft;
                this.updateIcon();
            }
        }
    }
    
    public void setBackground(final Drawable backgroundDrawable) {
        this.setBackgroundDrawable(backgroundDrawable);
    }
    
    public void setBackgroundColor(final int n) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setBackgroundColor(n);
        }
        else {
            super.setBackgroundColor(n);
        }
    }
    
    @Override
    public void setBackgroundDrawable(final Drawable drawable) {
        if (this.isUsingOriginalBackground()) {
            if (drawable != this.getBackground()) {
                Log.i("MaterialButton", "Setting a custom background is not supported.");
                this.materialButtonHelper.setBackgroundOverwritten();
                super.setBackgroundDrawable(drawable);
            }
            else {
                this.getBackground().setState(drawable.getState());
            }
        }
        else {
            super.setBackgroundDrawable(drawable);
        }
    }
    
    @Override
    public void setBackgroundResource(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setBackgroundDrawable(drawable);
    }
    
    public void setBackgroundTintList(final ColorStateList supportBackgroundTintList) {
        this.setSupportBackgroundTintList(supportBackgroundTintList);
    }
    
    public void setBackgroundTintMode(final PorterDuff$Mode supportBackgroundTintMode) {
        this.setSupportBackgroundTintMode(supportBackgroundTintMode);
    }
    
    public void setCornerRadius(final int cornerRadius) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setCornerRadius(cornerRadius);
        }
    }
    
    public void setCornerRadiusResource(final int n) {
        if (this.isUsingOriginalBackground()) {
            this.setCornerRadius(this.getResources().getDimensionPixelSize(n));
        }
    }
    
    public void setIcon(final Drawable icon) {
        if (this.icon != icon) {
            this.icon = icon;
            this.updateIcon();
        }
    }
    
    public void setIconGravity(final int iconGravity) {
        this.iconGravity = iconGravity;
    }
    
    public void setIconPadding(final int iconPadding) {
        if (this.iconPadding != iconPadding) {
            this.setCompoundDrawablePadding(this.iconPadding = iconPadding);
        }
    }
    
    public void setIconResource(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setIcon(drawable);
    }
    
    public void setIconSize(final int iconSize) {
        if (iconSize >= 0) {
            if (this.iconSize != iconSize) {
                this.iconSize = iconSize;
                this.updateIcon();
            }
            return;
        }
        throw new IllegalArgumentException("iconSize cannot be less than 0");
    }
    
    public void setIconTint(final ColorStateList iconTint) {
        if (this.iconTint != iconTint) {
            this.iconTint = iconTint;
            this.updateIcon();
        }
    }
    
    public void setIconTintMode(final PorterDuff$Mode iconTintMode) {
        if (this.iconTintMode != iconTintMode) {
            this.iconTintMode = iconTintMode;
            this.updateIcon();
        }
    }
    
    public void setIconTintResource(final int n) {
        this.setIconTint(AppCompatResources.getColorStateList(this.getContext(), n));
    }
    
    void setInternalBackground(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
    }
    
    public void setRippleColor(final ColorStateList rippleColor) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setRippleColor(rippleColor);
        }
    }
    
    public void setRippleColorResource(final int n) {
        if (this.isUsingOriginalBackground()) {
            this.setRippleColor(AppCompatResources.getColorStateList(this.getContext(), n));
        }
    }
    
    public void setStrokeColor(final ColorStateList strokeColor) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeColor(strokeColor);
        }
    }
    
    public void setStrokeColorResource(final int n) {
        if (this.isUsingOriginalBackground()) {
            this.setStrokeColor(AppCompatResources.getColorStateList(this.getContext(), n));
        }
    }
    
    public void setStrokeWidth(final int strokeWidth) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeWidth(strokeWidth);
        }
    }
    
    public void setStrokeWidthResource(final int n) {
        if (this.isUsingOriginalBackground()) {
            this.setStrokeWidth(this.getResources().getDimensionPixelSize(n));
        }
    }
    
    @Override
    public void setSupportBackgroundTintList(final ColorStateList list) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintList(list);
        }
        else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintList(list);
        }
    }
    
    @Override
    public void setSupportBackgroundTintMode(final PorterDuff$Mode porterDuff$Mode) {
        if (this.isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintMode(porterDuff$Mode);
        }
        else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintMode(porterDuff$Mode);
        }
    }
}
