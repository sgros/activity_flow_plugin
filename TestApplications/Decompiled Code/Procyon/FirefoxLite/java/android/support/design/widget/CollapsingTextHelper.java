// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.math.MathUtils;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.content.res.TypedArray;
import android.support.design.animation.AnimationUtils;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.GravityCompat;
import android.graphics.Color;
import android.os.Build$VERSION;
import android.view.View;
import android.text.TextPaint;
import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.Paint;

public final class CollapsingTextHelper
{
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private boolean boundsChanged;
    private final Rect collapsedBounds;
    private float collapsedDrawX;
    private float collapsedDrawY;
    private int collapsedShadowColor;
    private float collapsedShadowDx;
    private float collapsedShadowDy;
    private float collapsedShadowRadius;
    private ColorStateList collapsedTextColor;
    private int collapsedTextGravity;
    private float collapsedTextSize;
    private Typeface collapsedTypeface;
    private final RectF currentBounds;
    private float currentDrawX;
    private float currentDrawY;
    private float currentTextSize;
    private Typeface currentTypeface;
    private boolean drawTitle;
    private final Rect expandedBounds;
    private float expandedDrawX;
    private float expandedDrawY;
    private float expandedFraction;
    private int expandedShadowColor;
    private float expandedShadowDx;
    private float expandedShadowDy;
    private float expandedShadowRadius;
    private ColorStateList expandedTextColor;
    private int expandedTextGravity;
    private float expandedTextSize;
    private Bitmap expandedTitleTexture;
    private Typeface expandedTypeface;
    private boolean isRtl;
    private TimeInterpolator positionInterpolator;
    private float scale;
    private int[] state;
    private CharSequence text;
    private final TextPaint textPaint;
    private TimeInterpolator textSizeInterpolator;
    private CharSequence textToDraw;
    private float textureAscent;
    private float textureDescent;
    private Paint texturePaint;
    private final TextPaint tmpPaint;
    private boolean useTexture;
    private final View view;
    
    static {
        USE_SCALING_TEXTURE = (Build$VERSION.SDK_INT < 18);
        DEBUG_DRAW_PAINT = null;
        if (CollapsingTextHelper.DEBUG_DRAW_PAINT != null) {
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setAntiAlias(true);
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }
    
    public CollapsingTextHelper(final View view) {
        this.expandedTextGravity = 16;
        this.collapsedTextGravity = 16;
        this.expandedTextSize = 15.0f;
        this.collapsedTextSize = 15.0f;
        this.view = view;
        this.textPaint = new TextPaint(129);
        this.tmpPaint = new TextPaint((Paint)this.textPaint);
        this.collapsedBounds = new Rect();
        this.expandedBounds = new Rect();
        this.currentBounds = new RectF();
    }
    
    private static int blendColors(final int n, final int n2, final float n3) {
        final float n4 = 1.0f - n3;
        return Color.argb((int)(Color.alpha(n) * n4 + Color.alpha(n2) * n3), (int)(Color.red(n) * n4 + Color.red(n2) * n3), (int)(Color.green(n) * n4 + Color.green(n2) * n3), (int)(Color.blue(n) * n4 + Color.blue(n2) * n3));
    }
    
    private void calculateBaseOffsets() {
        final float currentTextSize = this.currentTextSize;
        this.calculateUsingTextSize(this.collapsedTextSize);
        final CharSequence textToDraw = this.textToDraw;
        final float n = 0.0f;
        float measureText;
        if (textToDraw != null) {
            measureText = this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length());
        }
        else {
            measureText = 0.0f;
        }
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl ? 1 : 0);
        final int n2 = absoluteGravity & 0x70;
        if (n2 != 48) {
            if (n2 != 80) {
                this.collapsedDrawY = this.collapsedBounds.centerY() + ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f - this.textPaint.descent());
            }
            else {
                this.collapsedDrawY = (float)this.collapsedBounds.bottom;
            }
        }
        else {
            this.collapsedDrawY = this.collapsedBounds.top - this.textPaint.ascent();
        }
        final int n3 = absoluteGravity & 0x800007;
        if (n3 != 1) {
            if (n3 != 5) {
                this.collapsedDrawX = (float)this.collapsedBounds.left;
            }
            else {
                this.collapsedDrawX = this.collapsedBounds.right - measureText;
            }
        }
        else {
            this.collapsedDrawX = this.collapsedBounds.centerX() - measureText / 2.0f;
        }
        this.calculateUsingTextSize(this.expandedTextSize);
        float measureText2 = n;
        if (this.textToDraw != null) {
            measureText2 = this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length());
        }
        final int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0);
        final int n4 = absoluteGravity2 & 0x70;
        if (n4 != 48) {
            if (n4 != 80) {
                this.expandedDrawY = this.expandedBounds.centerY() + ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f - this.textPaint.descent());
            }
            else {
                this.expandedDrawY = (float)this.expandedBounds.bottom;
            }
        }
        else {
            this.expandedDrawY = this.expandedBounds.top - this.textPaint.ascent();
        }
        final int n5 = absoluteGravity2 & 0x800007;
        if (n5 != 1) {
            if (n5 != 5) {
                this.expandedDrawX = (float)this.expandedBounds.left;
            }
            else {
                this.expandedDrawX = this.expandedBounds.right - measureText2;
            }
        }
        else {
            this.expandedDrawX = this.expandedBounds.centerX() - measureText2 / 2.0f;
        }
        this.clearTexture();
        this.setInterpolatedTextSize(currentTextSize);
    }
    
    private void calculateCurrentOffsets() {
        this.calculateOffsets(this.expandedFraction);
    }
    
    private boolean calculateIsRtl(final CharSequence charSequence) {
        final int layoutDirection = ViewCompat.getLayoutDirection(this.view);
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        TextDirectionHeuristicCompat textDirectionHeuristicCompat;
        if (b) {
            textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
        }
        else {
            textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        }
        return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }
    
    private void calculateOffsets(final float n) {
        this.interpolateBounds(n);
        this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, n, this.positionInterpolator);
        this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, n, this.positionInterpolator);
        this.setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, n, this.textSizeInterpolator));
        if (this.collapsedTextColor != this.expandedTextColor) {
            this.textPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), n));
        }
        else {
            this.textPaint.setColor(this.getCurrentCollapsedTextColor());
        }
        this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, n, null), lerp(this.expandedShadowDx, this.collapsedShadowDx, n, null), lerp(this.expandedShadowDy, this.collapsedShadowDy, n, null), blendColors(this.expandedShadowColor, this.collapsedShadowColor, n));
        ViewCompat.postInvalidateOnAnimation(this.view);
    }
    
    private void calculateUsingTextSize(float min) {
        if (this.text == null) {
            return;
        }
        final float n = (float)this.collapsedBounds.width();
        final float b = (float)this.expandedBounds.width();
        final boolean close = isClose(min, this.collapsedTextSize);
        boolean linearText = true;
        float currentTextSize;
        int n2;
        if (close) {
            currentTextSize = this.collapsedTextSize;
            this.scale = 1.0f;
            if (this.currentTypeface != this.collapsedTypeface) {
                this.currentTypeface = this.collapsedTypeface;
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            min = n;
        }
        else {
            currentTextSize = this.expandedTextSize;
            if (this.currentTypeface != this.expandedTypeface) {
                this.currentTypeface = this.expandedTypeface;
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            if (isClose(min, this.expandedTextSize)) {
                this.scale = 1.0f;
            }
            else {
                this.scale = min / this.expandedTextSize;
            }
            min = this.collapsedTextSize / this.expandedTextSize;
            if (b * min > n) {
                min = Math.min(n / min, b);
            }
            else {
                min = b;
            }
        }
        int n3 = n2;
        if (min > 0.0f) {
            final boolean b2 = this.currentTextSize != currentTextSize || this.boundsChanged || n2 != 0;
            this.currentTextSize = currentTextSize;
            this.boundsChanged = false;
            n3 = (b2 ? 1 : 0);
        }
        if (this.textToDraw == null || n3 != 0) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            final TextPaint textPaint = this.textPaint;
            if (this.scale == 1.0f) {
                linearText = false;
            }
            textPaint.setLinearText(linearText);
            final CharSequence ellipsize = TextUtils.ellipsize(this.text, this.textPaint, min, TextUtils$TruncateAt.END);
            if (!TextUtils.equals(ellipsize, this.textToDraw)) {
                this.textToDraw = ellipsize;
                this.isRtl = this.calculateIsRtl(this.textToDraw);
            }
        }
    }
    
    private void clearTexture() {
        if (this.expandedTitleTexture != null) {
            this.expandedTitleTexture.recycle();
            this.expandedTitleTexture = null;
        }
    }
    
    private void ensureExpandedTexture() {
        if (this.expandedTitleTexture != null || this.expandedBounds.isEmpty() || TextUtils.isEmpty(this.textToDraw)) {
            return;
        }
        this.calculateOffsets(0.0f);
        this.textureAscent = this.textPaint.ascent();
        this.textureDescent = this.textPaint.descent();
        final int round = Math.round(this.textPaint.measureText(this.textToDraw, 0, this.textToDraw.length()));
        final int round2 = Math.round(this.textureDescent - this.textureAscent);
        if (round > 0 && round2 > 0) {
            this.expandedTitleTexture = Bitmap.createBitmap(round, round2, Bitmap$Config.ARGB_8888);
            new Canvas(this.expandedTitleTexture).drawText(this.textToDraw, 0, this.textToDraw.length(), 0.0f, round2 - this.textPaint.descent(), (Paint)this.textPaint);
            if (this.texturePaint == null) {
                this.texturePaint = new Paint(3);
            }
        }
    }
    
    private int getCurrentExpandedTextColor() {
        if (this.state != null) {
            return this.expandedTextColor.getColorForState(this.state, 0);
        }
        return this.expandedTextColor.getDefaultColor();
    }
    
    private void getTextPaintCollapsed(final TextPaint textPaint) {
        textPaint.setTextSize(this.collapsedTextSize);
        textPaint.setTypeface(this.collapsedTypeface);
    }
    
    private void interpolateBounds(final float n) {
        this.currentBounds.left = lerp((float)this.expandedBounds.left, (float)this.collapsedBounds.left, n, this.positionInterpolator);
        this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, n, this.positionInterpolator);
        this.currentBounds.right = lerp((float)this.expandedBounds.right, (float)this.collapsedBounds.right, n, this.positionInterpolator);
        this.currentBounds.bottom = lerp((float)this.expandedBounds.bottom, (float)this.collapsedBounds.bottom, n, this.positionInterpolator);
    }
    
    private static boolean isClose(final float n, final float n2) {
        return Math.abs(n - n2) < 0.001f;
    }
    
    private static float lerp(final float n, final float n2, final float n3, final TimeInterpolator timeInterpolator) {
        float interpolation = n3;
        if (timeInterpolator != null) {
            interpolation = timeInterpolator.getInterpolation(n3);
        }
        return AnimationUtils.lerp(n, n2, interpolation);
    }
    
    private Typeface readFontFamilyTypeface(final int n) {
        final TypedArray obtainStyledAttributes = this.view.getContext().obtainStyledAttributes(n, new int[] { 16843692 });
        try {
            final String string = obtainStyledAttributes.getString(0);
            if (string != null) {
                return Typeface.create(string, 0);
            }
            return null;
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    private static boolean rectEquals(final Rect rect, final int n, final int n2, final int n3, final int n4) {
        return rect.left == n && rect.top == n2 && rect.right == n3 && rect.bottom == n4;
    }
    
    private void setInterpolatedTextSize(final float n) {
        this.calculateUsingTextSize(n);
        this.useTexture = (CollapsingTextHelper.USE_SCALING_TEXTURE && this.scale != 1.0f);
        if (this.useTexture) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }
    
    public float calculateCollapsedTextWidth() {
        if (this.text == null) {
            return 0.0f;
        }
        this.getTextPaintCollapsed(this.tmpPaint);
        return this.tmpPaint.measureText(this.text, 0, this.text.length());
    }
    
    public void draw(final Canvas canvas) {
        final int save = canvas.save();
        if (this.textToDraw != null && this.drawTitle) {
            final float currentDrawX = this.currentDrawX;
            final float currentDrawY = this.currentDrawY;
            final boolean b = this.useTexture && this.expandedTitleTexture != null;
            float n;
            if (b) {
                n = this.textureAscent * this.scale;
                final float textureDescent = this.textureDescent;
                final float scale = this.scale;
            }
            else {
                n = this.textPaint.ascent() * this.scale;
                this.textPaint.descent();
                final float scale2 = this.scale;
            }
            float n2 = currentDrawY;
            if (b) {
                n2 = currentDrawY + n;
            }
            if (this.scale != 1.0f) {
                canvas.scale(this.scale, this.scale, currentDrawX, n2);
            }
            if (b) {
                canvas.drawBitmap(this.expandedTitleTexture, currentDrawX, n2, this.texturePaint);
            }
            else {
                canvas.drawText(this.textToDraw, 0, this.textToDraw.length(), currentDrawX, n2, (Paint)this.textPaint);
            }
        }
        canvas.restoreToCount(save);
    }
    
    public void getCollapsedTextActualBounds(final RectF rectF) {
        final boolean calculateIsRtl = this.calculateIsRtl(this.text);
        float left;
        if (!calculateIsRtl) {
            left = (float)this.collapsedBounds.left;
        }
        else {
            left = this.collapsedBounds.right - this.calculateCollapsedTextWidth();
        }
        rectF.left = left;
        rectF.top = (float)this.collapsedBounds.top;
        float right;
        if (!calculateIsRtl) {
            right = rectF.left + this.calculateCollapsedTextWidth();
        }
        else {
            right = (float)this.collapsedBounds.right;
        }
        rectF.right = right;
        rectF.bottom = this.collapsedBounds.top + this.getCollapsedTextHeight();
    }
    
    public ColorStateList getCollapsedTextColor() {
        return this.collapsedTextColor;
    }
    
    public float getCollapsedTextHeight() {
        this.getTextPaintCollapsed(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }
    
    public int getCurrentCollapsedTextColor() {
        if (this.state != null) {
            return this.collapsedTextColor.getColorForState(this.state, 0);
        }
        return this.collapsedTextColor.getDefaultColor();
    }
    
    public float getExpansionFraction() {
        return this.expandedFraction;
    }
    
    public final boolean isStateful() {
        return (this.collapsedTextColor != null && this.collapsedTextColor.isStateful()) || (this.expandedTextColor != null && this.expandedTextColor.isStateful());
    }
    
    void onBoundsChanged() {
        this.drawTitle = (this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0);
    }
    
    public void recalculate() {
        if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
        }
    }
    
    public void setCollapsedBounds(final int n, final int n2, final int n3, final int n4) {
        if (!rectEquals(this.collapsedBounds, n, n2, n3, n4)) {
            this.collapsedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }
    
    public void setCollapsedTextAppearance(final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.view.getContext(), n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.collapsedTextColor = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.collapsedTextSize = (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.collapsedTextSize);
        }
        this.collapsedShadowColor = obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.collapsedShadowDx = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.collapsedShadowDy = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.collapsedShadowRadius = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        obtainStyledAttributes.recycle();
        if (Build$VERSION.SDK_INT >= 16) {
            this.collapsedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }
    
    public void setCollapsedTextColor(final ColorStateList collapsedTextColor) {
        if (this.collapsedTextColor != collapsedTextColor) {
            this.collapsedTextColor = collapsedTextColor;
            this.recalculate();
        }
    }
    
    public void setCollapsedTextGravity(final int collapsedTextGravity) {
        if (this.collapsedTextGravity != collapsedTextGravity) {
            this.collapsedTextGravity = collapsedTextGravity;
            this.recalculate();
        }
    }
    
    public void setExpandedBounds(final int n, final int n2, final int n3, final int n4) {
        if (!rectEquals(this.expandedBounds, n, n2, n3, n4)) {
            this.expandedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }
    
    public void setExpandedTextColor(final ColorStateList expandedTextColor) {
        if (this.expandedTextColor != expandedTextColor) {
            this.expandedTextColor = expandedTextColor;
            this.recalculate();
        }
    }
    
    public void setExpandedTextGravity(final int expandedTextGravity) {
        if (this.expandedTextGravity != expandedTextGravity) {
            this.expandedTextGravity = expandedTextGravity;
            this.recalculate();
        }
    }
    
    public void setExpandedTextSize(final float expandedTextSize) {
        if (this.expandedTextSize != expandedTextSize) {
            this.expandedTextSize = expandedTextSize;
            this.recalculate();
        }
    }
    
    public void setExpansionFraction(float clamp) {
        clamp = MathUtils.clamp(clamp, 0.0f, 1.0f);
        if (clamp != this.expandedFraction) {
            this.expandedFraction = clamp;
            this.calculateCurrentOffsets();
        }
    }
    
    public void setPositionInterpolator(final TimeInterpolator positionInterpolator) {
        this.positionInterpolator = positionInterpolator;
        this.recalculate();
    }
    
    public final boolean setState(final int[] state) {
        this.state = state;
        if (this.isStateful()) {
            this.recalculate();
            return true;
        }
        return false;
    }
    
    public void setText(final CharSequence text) {
        if (text == null || !text.equals(this.text)) {
            this.text = text;
            this.textToDraw = null;
            this.clearTexture();
            this.recalculate();
        }
    }
    
    public void setTextSizeInterpolator(final TimeInterpolator textSizeInterpolator) {
        this.textSizeInterpolator = textSizeInterpolator;
        this.recalculate();
    }
    
    public void setTypefaces(final Typeface typeface) {
        this.expandedTypeface = typeface;
        this.collapsedTypeface = typeface;
        this.recalculate();
    }
}
