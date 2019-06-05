// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.support.v4.graphics.ColorUtils;
import android.graphics.Shader;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Paint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public class CircularBorderDrawable extends Drawable
{
    private ColorStateList borderTint;
    float borderWidth;
    private int bottomInnerStrokeColor;
    private int bottomOuterStrokeColor;
    private int currentBorderTintColor;
    private boolean invalidateShader;
    final Paint paint;
    final Rect rect;
    final RectF rectF;
    private float rotation;
    final CircularBorderState state;
    private int topInnerStrokeColor;
    private int topOuterStrokeColor;
    
    private Shader createGradientShader() {
        final Rect rect = this.rect;
        this.copyBounds(rect);
        final float n = this.borderWidth / rect.height();
        return (Shader)new LinearGradient(0.0f, (float)rect.top, 0.0f, (float)rect.bottom, new int[] { ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor), ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor) }, new float[] { 0.0f, n, 0.5f, 0.5f, 1.0f - n, 1.0f }, Shader$TileMode.CLAMP);
    }
    
    public void draw(final Canvas canvas) {
        if (this.invalidateShader) {
            this.paint.setShader(this.createGradientShader());
            this.invalidateShader = false;
        }
        final float n = this.paint.getStrokeWidth() / 2.0f;
        final RectF rectF = this.rectF;
        this.copyBounds(this.rect);
        rectF.set(this.rect);
        rectF.left += n;
        rectF.top += n;
        rectF.right -= n;
        rectF.bottom -= n;
        canvas.save();
        canvas.rotate(this.rotation, rectF.centerX(), rectF.centerY());
        canvas.drawOval(rectF, this.paint);
        canvas.restore();
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.state;
    }
    
    public int getOpacity() {
        int n;
        if (this.borderWidth > 0.0f) {
            n = -3;
        }
        else {
            n = -2;
        }
        return n;
    }
    
    public boolean getPadding(final Rect rect) {
        final int round = Math.round(this.borderWidth);
        rect.set(round, round, round, round);
        return true;
    }
    
    public boolean isStateful() {
        return (this.borderTint != null && this.borderTint.isStateful()) || super.isStateful();
    }
    
    protected void onBoundsChange(final Rect rect) {
        this.invalidateShader = true;
    }
    
    protected boolean onStateChange(final int[] array) {
        if (this.borderTint != null) {
            final int colorForState = this.borderTint.getColorForState(array, this.currentBorderTintColor);
            if (colorForState != this.currentBorderTintColor) {
                this.invalidateShader = true;
                this.currentBorderTintColor = colorForState;
            }
        }
        if (this.invalidateShader) {
            this.invalidateSelf();
        }
        return this.invalidateShader;
    }
    
    public void setAlpha(final int alpha) {
        this.paint.setAlpha(alpha);
        this.invalidateSelf();
    }
    
    public void setBorderTint(final ColorStateList borderTint) {
        if (borderTint != null) {
            this.currentBorderTintColor = borderTint.getColorForState(this.getState(), this.currentBorderTintColor);
        }
        this.borderTint = borderTint;
        this.invalidateShader = true;
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }
    
    public final void setRotation(final float rotation) {
        if (rotation != this.rotation) {
            this.rotation = rotation;
            this.invalidateSelf();
        }
    }
    
    private class CircularBorderState extends Drawable$ConstantState
    {
        final /* synthetic */ CircularBorderDrawable this$0;
        
        public int getChangingConfigurations() {
            return 0;
        }
        
        public Drawable newDrawable() {
            return this.this$0;
        }
    }
}
