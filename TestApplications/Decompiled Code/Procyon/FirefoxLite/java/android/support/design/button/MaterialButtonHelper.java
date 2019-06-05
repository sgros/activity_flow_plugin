// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.button;

import android.view.View;
import android.support.v4.view.ViewCompat;
import android.graphics.Paint$Style;
import android.support.design.resources.MaterialResources;
import android.support.design.internal.ViewUtils;
import android.support.design.R;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.RippleDrawable;
import android.annotation.TargetApi;
import android.graphics.drawable.InsetDrawable;
import android.support.design.ripple.RippleUtils;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;

class MaterialButtonHelper
{
    private static final boolean IS_LOLLIPOP;
    private GradientDrawable backgroundDrawableLollipop;
    private boolean backgroundOverwritten;
    private ColorStateList backgroundTint;
    private PorterDuff$Mode backgroundTintMode;
    private final Rect bounds;
    private final Paint buttonStrokePaint;
    private GradientDrawable colorableBackgroundDrawableCompat;
    private int cornerRadius;
    private int insetBottom;
    private int insetLeft;
    private int insetRight;
    private int insetTop;
    private GradientDrawable maskDrawableLollipop;
    private final MaterialButton materialButton;
    private final RectF rectF;
    private ColorStateList rippleColor;
    private GradientDrawable rippleDrawableCompat;
    private ColorStateList strokeColor;
    private GradientDrawable strokeDrawableLollipop;
    private int strokeWidth;
    private Drawable tintableBackgroundDrawableCompat;
    private Drawable tintableRippleDrawableCompat;
    
    static {
        IS_LOLLIPOP = (Build$VERSION.SDK_INT >= 21);
    }
    
    public MaterialButtonHelper(final MaterialButton materialButton) {
        this.buttonStrokePaint = new Paint(1);
        this.bounds = new Rect();
        this.rectF = new RectF();
        this.backgroundOverwritten = false;
        this.materialButton = materialButton;
    }
    
    private Drawable createBackgroundCompat() {
        (this.colorableBackgroundDrawableCompat = new GradientDrawable()).setCornerRadius(this.cornerRadius + 1.0E-5f);
        this.colorableBackgroundDrawableCompat.setColor(-1);
        DrawableCompat.setTintList(this.tintableBackgroundDrawableCompat = DrawableCompat.wrap((Drawable)this.colorableBackgroundDrawableCompat), this.backgroundTint);
        if (this.backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, this.backgroundTintMode);
        }
        (this.rippleDrawableCompat = new GradientDrawable()).setCornerRadius(this.cornerRadius + 1.0E-5f);
        this.rippleDrawableCompat.setColor(-1);
        DrawableCompat.setTintList(this.tintableRippleDrawableCompat = DrawableCompat.wrap((Drawable)this.rippleDrawableCompat), this.rippleColor);
        return (Drawable)this.wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[] { this.tintableBackgroundDrawableCompat, this.tintableRippleDrawableCompat }));
    }
    
    @TargetApi(21)
    private Drawable createBackgroundLollipop() {
        (this.backgroundDrawableLollipop = new GradientDrawable()).setCornerRadius(this.cornerRadius + 1.0E-5f);
        this.backgroundDrawableLollipop.setColor(-1);
        this.updateTintAndTintModeLollipop();
        (this.strokeDrawableLollipop = new GradientDrawable()).setCornerRadius(this.cornerRadius + 1.0E-5f);
        this.strokeDrawableLollipop.setColor(0);
        this.strokeDrawableLollipop.setStroke(this.strokeWidth, this.strokeColor);
        final InsetDrawable wrapDrawableWithInset = this.wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[] { (Drawable)this.backgroundDrawableLollipop, (Drawable)this.strokeDrawableLollipop }));
        (this.maskDrawableLollipop = new GradientDrawable()).setCornerRadius(this.cornerRadius + 1.0E-5f);
        this.maskDrawableLollipop.setColor(-1);
        return (Drawable)new MaterialButtonBackgroundDrawable(RippleUtils.convertToRippleDrawableColor(this.rippleColor), wrapDrawableWithInset, (Drawable)this.maskDrawableLollipop);
    }
    
    private GradientDrawable unwrapBackgroundDrawable() {
        if (MaterialButtonHelper.IS_LOLLIPOP && this.materialButton.getBackground() != null) {
            return (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(0);
        }
        return null;
    }
    
    private GradientDrawable unwrapStrokeDrawable() {
        if (MaterialButtonHelper.IS_LOLLIPOP && this.materialButton.getBackground() != null) {
            return (GradientDrawable)((LayerDrawable)((InsetDrawable)((RippleDrawable)this.materialButton.getBackground()).getDrawable(0)).getDrawable()).getDrawable(1);
        }
        return null;
    }
    
    private void updateStroke() {
        if (MaterialButtonHelper.IS_LOLLIPOP && this.strokeDrawableLollipop != null) {
            this.materialButton.setInternalBackground(this.createBackgroundLollipop());
        }
        else if (!MaterialButtonHelper.IS_LOLLIPOP) {
            this.materialButton.invalidate();
        }
    }
    
    private void updateTintAndTintModeLollipop() {
        if (this.backgroundDrawableLollipop != null) {
            DrawableCompat.setTintList((Drawable)this.backgroundDrawableLollipop, this.backgroundTint);
            if (this.backgroundTintMode != null) {
                DrawableCompat.setTintMode((Drawable)this.backgroundDrawableLollipop, this.backgroundTintMode);
            }
        }
    }
    
    private InsetDrawable wrapDrawableWithInset(final Drawable drawable) {
        return new InsetDrawable(drawable, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
    }
    
    void drawStroke(final Canvas canvas) {
        if (canvas != null && this.strokeColor != null && this.strokeWidth > 0) {
            this.bounds.set(this.materialButton.getBackground().getBounds());
            this.rectF.set(this.bounds.left + this.strokeWidth / 2.0f + this.insetLeft, this.bounds.top + this.strokeWidth / 2.0f + this.insetTop, this.bounds.right - this.strokeWidth / 2.0f - this.insetRight, this.bounds.bottom - this.strokeWidth / 2.0f - this.insetBottom);
            final float n = this.cornerRadius - this.strokeWidth / 2.0f;
            canvas.drawRoundRect(this.rectF, n, n, this.buttonStrokePaint);
        }
    }
    
    int getCornerRadius() {
        return this.cornerRadius;
    }
    
    ColorStateList getRippleColor() {
        return this.rippleColor;
    }
    
    ColorStateList getStrokeColor() {
        return this.strokeColor;
    }
    
    int getStrokeWidth() {
        return this.strokeWidth;
    }
    
    ColorStateList getSupportBackgroundTintList() {
        return this.backgroundTint;
    }
    
    PorterDuff$Mode getSupportBackgroundTintMode() {
        return this.backgroundTintMode;
    }
    
    boolean isBackgroundOverwritten() {
        return this.backgroundOverwritten;
    }
    
    public void loadFromAttributes(final TypedArray typedArray) {
        final int materialButton_android_insetLeft = R.styleable.MaterialButton_android_insetLeft;
        int colorForState = 0;
        this.insetLeft = typedArray.getDimensionPixelOffset(materialButton_android_insetLeft, 0);
        this.insetRight = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
        this.insetTop = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
        this.insetBottom = typedArray.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
        this.cornerRadius = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, 0);
        this.strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
        this.backgroundTintMode = ViewUtils.parseTintMode(typedArray.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff$Mode.SRC_IN);
        this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_backgroundTint);
        this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_strokeColor);
        this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), typedArray, R.styleable.MaterialButton_rippleColor);
        this.buttonStrokePaint.setStyle(Paint$Style.STROKE);
        this.buttonStrokePaint.setStrokeWidth((float)this.strokeWidth);
        final Paint buttonStrokePaint = this.buttonStrokePaint;
        if (this.strokeColor != null) {
            colorForState = this.strokeColor.getColorForState(this.materialButton.getDrawableState(), 0);
        }
        buttonStrokePaint.setColor(colorForState);
        final int paddingStart = ViewCompat.getPaddingStart((View)this.materialButton);
        final int paddingTop = this.materialButton.getPaddingTop();
        final int paddingEnd = ViewCompat.getPaddingEnd((View)this.materialButton);
        final int paddingBottom = this.materialButton.getPaddingBottom();
        final MaterialButton materialButton = this.materialButton;
        Drawable internalBackground;
        if (MaterialButtonHelper.IS_LOLLIPOP) {
            internalBackground = this.createBackgroundLollipop();
        }
        else {
            internalBackground = this.createBackgroundCompat();
        }
        materialButton.setInternalBackground(internalBackground);
        ViewCompat.setPaddingRelative((View)this.materialButton, paddingStart + this.insetLeft, paddingTop + this.insetTop, paddingEnd + this.insetRight, paddingBottom + this.insetBottom);
    }
    
    void setBackgroundColor(final int n) {
        if (MaterialButtonHelper.IS_LOLLIPOP && this.backgroundDrawableLollipop != null) {
            this.backgroundDrawableLollipop.setColor(n);
        }
        else if (!MaterialButtonHelper.IS_LOLLIPOP && this.colorableBackgroundDrawableCompat != null) {
            this.colorableBackgroundDrawableCompat.setColor(n);
        }
    }
    
    void setBackgroundOverwritten() {
        this.backgroundOverwritten = true;
        this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
        this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
    }
    
    void setCornerRadius(final int cornerRadius) {
        if (this.cornerRadius != cornerRadius) {
            this.cornerRadius = cornerRadius;
            if (MaterialButtonHelper.IS_LOLLIPOP && this.backgroundDrawableLollipop != null && this.strokeDrawableLollipop != null && this.maskDrawableLollipop != null) {
                if (Build$VERSION.SDK_INT == 21) {
                    final GradientDrawable unwrapBackgroundDrawable = this.unwrapBackgroundDrawable();
                    final float n = cornerRadius + 1.0E-5f;
                    unwrapBackgroundDrawable.setCornerRadius(n);
                    this.unwrapStrokeDrawable().setCornerRadius(n);
                }
                final GradientDrawable backgroundDrawableLollipop = this.backgroundDrawableLollipop;
                final float cornerRadius2 = cornerRadius + 1.0E-5f;
                backgroundDrawableLollipop.setCornerRadius(cornerRadius2);
                this.strokeDrawableLollipop.setCornerRadius(cornerRadius2);
                this.maskDrawableLollipop.setCornerRadius(cornerRadius2);
            }
            else if (!MaterialButtonHelper.IS_LOLLIPOP && this.colorableBackgroundDrawableCompat != null && this.rippleDrawableCompat != null) {
                final GradientDrawable colorableBackgroundDrawableCompat = this.colorableBackgroundDrawableCompat;
                final float n2 = cornerRadius + 1.0E-5f;
                colorableBackgroundDrawableCompat.setCornerRadius(n2);
                this.rippleDrawableCompat.setCornerRadius(n2);
                this.materialButton.invalidate();
            }
        }
    }
    
    void setRippleColor(final ColorStateList list) {
        if (this.rippleColor != list) {
            this.rippleColor = list;
            if (MaterialButtonHelper.IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawable) {
                ((RippleDrawable)this.materialButton.getBackground()).setColor(list);
            }
            else if (!MaterialButtonHelper.IS_LOLLIPOP && this.tintableRippleDrawableCompat != null) {
                DrawableCompat.setTintList(this.tintableRippleDrawableCompat, list);
            }
        }
    }
    
    void setStrokeColor(final ColorStateList strokeColor) {
        if (this.strokeColor != strokeColor) {
            this.strokeColor = strokeColor;
            final Paint buttonStrokePaint = this.buttonStrokePaint;
            int colorForState = 0;
            if (strokeColor != null) {
                colorForState = strokeColor.getColorForState(this.materialButton.getDrawableState(), 0);
            }
            buttonStrokePaint.setColor(colorForState);
            this.updateStroke();
        }
    }
    
    void setStrokeWidth(final int strokeWidth) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = strokeWidth;
            this.buttonStrokePaint.setStrokeWidth((float)strokeWidth);
            this.updateStroke();
        }
    }
    
    void setSupportBackgroundTintList(final ColorStateList backgroundTint) {
        if (this.backgroundTint != backgroundTint) {
            this.backgroundTint = backgroundTint;
            if (MaterialButtonHelper.IS_LOLLIPOP) {
                this.updateTintAndTintModeLollipop();
            }
            else if (this.tintableBackgroundDrawableCompat != null) {
                DrawableCompat.setTintList(this.tintableBackgroundDrawableCompat, this.backgroundTint);
            }
        }
    }
    
    void setSupportBackgroundTintMode(final PorterDuff$Mode backgroundTintMode) {
        if (this.backgroundTintMode != backgroundTintMode) {
            this.backgroundTintMode = backgroundTintMode;
            if (MaterialButtonHelper.IS_LOLLIPOP) {
                this.updateTintAndTintModeLollipop();
            }
            else if (this.tintableBackgroundDrawableCompat != null && this.backgroundTintMode != null) {
                DrawableCompat.setTintMode(this.tintableBackgroundDrawableCompat, this.backgroundTintMode);
            }
        }
    }
    
    void updateMaskBounds(final int n, final int n2) {
        if (this.maskDrawableLollipop != null) {
            this.maskDrawableLollipop.setBounds(this.insetLeft, this.insetTop, n2 - this.insetRight, n - this.insetBottom);
        }
    }
}
