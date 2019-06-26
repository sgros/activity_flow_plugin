// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.math.MathUtils;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
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
import android.view.animation.Interpolator;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.Paint;

final class CollapsingTextHelper
{
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private boolean mBoundsChanged;
    private final Rect mCollapsedBounds;
    private float mCollapsedDrawX;
    private float mCollapsedDrawY;
    private int mCollapsedShadowColor;
    private float mCollapsedShadowDx;
    private float mCollapsedShadowDy;
    private float mCollapsedShadowRadius;
    private ColorStateList mCollapsedTextColor;
    private int mCollapsedTextGravity;
    private float mCollapsedTextSize;
    private Typeface mCollapsedTypeface;
    private final RectF mCurrentBounds;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private float mCurrentTextSize;
    private Typeface mCurrentTypeface;
    private boolean mDrawTitle;
    private final Rect mExpandedBounds;
    private float mExpandedDrawX;
    private float mExpandedDrawY;
    private float mExpandedFraction;
    private int mExpandedShadowColor;
    private float mExpandedShadowDx;
    private float mExpandedShadowDy;
    private float mExpandedShadowRadius;
    private ColorStateList mExpandedTextColor;
    private int mExpandedTextGravity;
    private float mExpandedTextSize;
    private Bitmap mExpandedTitleTexture;
    private Typeface mExpandedTypeface;
    private boolean mIsRtl;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private int[] mState;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private float mTextureAscent;
    private float mTextureDescent;
    private Paint mTexturePaint;
    private boolean mUseTexture;
    private final View mView;
    
    static {
        USE_SCALING_TEXTURE = (Build$VERSION.SDK_INT < 18);
        if (CollapsingTextHelper.DEBUG_DRAW_PAINT != null) {
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setAntiAlias(true);
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }
    
    public CollapsingTextHelper(final View mView) {
        this.mExpandedTextGravity = 16;
        this.mCollapsedTextGravity = 16;
        this.mExpandedTextSize = 15.0f;
        this.mCollapsedTextSize = 15.0f;
        this.mView = mView;
        this.mTextPaint = new TextPaint(129);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
    }
    
    private boolean areTypefacesDifferent(final Typeface typeface, final Typeface typeface2) {
        return (typeface != null && !typeface.equals((Object)typeface2)) || (typeface == null && typeface2 != null);
    }
    
    private static int blendColors(final int n, final int n2, final float n3) {
        final float n4 = 1.0f - n3;
        return Color.argb((int)(Color.alpha(n) * n4 + Color.alpha(n2) * n3), (int)(Color.red(n) * n4 + Color.red(n2) * n3), (int)(Color.green(n) * n4 + Color.green(n2) * n3), (int)(Color.blue(n) * n4 + Color.blue(n2) * n3));
    }
    
    private void calculateBaseOffsets() {
        final float mCurrentTextSize = this.mCurrentTextSize;
        this.calculateUsingTextSize(this.mCollapsedTextSize);
        final CharSequence mTextToDraw = this.mTextToDraw;
        final float n = 0.0f;
        float measureText;
        if (mTextToDraw != null) {
            measureText = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
        }
        else {
            measureText = 0.0f;
        }
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(this.mCollapsedTextGravity, this.mIsRtl ? 1 : 0);
        final int n2 = absoluteGravity & 0x70;
        if (n2 != 48) {
            if (n2 != 80) {
                this.mCollapsedDrawY = this.mCollapsedBounds.centerY() + ((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f - this.mTextPaint.descent());
            }
            else {
                this.mCollapsedDrawY = (float)this.mCollapsedBounds.bottom;
            }
        }
        else {
            this.mCollapsedDrawY = this.mCollapsedBounds.top - this.mTextPaint.ascent();
        }
        final int n3 = absoluteGravity & 0x800007;
        if (n3 != 1) {
            if (n3 != 5) {
                this.mCollapsedDrawX = (float)this.mCollapsedBounds.left;
            }
            else {
                this.mCollapsedDrawX = this.mCollapsedBounds.right - measureText;
            }
        }
        else {
            this.mCollapsedDrawX = this.mCollapsedBounds.centerX() - measureText / 2.0f;
        }
        this.calculateUsingTextSize(this.mExpandedTextSize);
        float measureText2 = n;
        if (this.mTextToDraw != null) {
            measureText2 = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
        }
        final int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.mExpandedTextGravity, this.mIsRtl ? 1 : 0);
        final int n4 = absoluteGravity2 & 0x70;
        if (n4 != 48) {
            if (n4 != 80) {
                this.mExpandedDrawY = this.mExpandedBounds.centerY() + ((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f - this.mTextPaint.descent());
            }
            else {
                this.mExpandedDrawY = (float)this.mExpandedBounds.bottom;
            }
        }
        else {
            this.mExpandedDrawY = this.mExpandedBounds.top - this.mTextPaint.ascent();
        }
        final int n5 = absoluteGravity2 & 0x800007;
        if (n5 != 1) {
            if (n5 != 5) {
                this.mExpandedDrawX = (float)this.mExpandedBounds.left;
            }
            else {
                this.mExpandedDrawX = this.mExpandedBounds.right - measureText2;
            }
        }
        else {
            this.mExpandedDrawX = this.mExpandedBounds.centerX() - measureText2 / 2.0f;
        }
        this.clearTexture();
        this.setInterpolatedTextSize(mCurrentTextSize);
    }
    
    private void calculateCurrentOffsets() {
        this.calculateOffsets(this.mExpandedFraction);
    }
    
    private boolean calculateIsRtl(final CharSequence charSequence) {
        final int layoutDirection = ViewCompat.getLayoutDirection(this.mView);
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
        this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, n, this.mPositionInterpolator);
        this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, n, this.mPositionInterpolator);
        this.setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, n, this.mTextSizeInterpolator));
        if (this.mCollapsedTextColor != this.mExpandedTextColor) {
            this.mTextPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), n));
        }
        else {
            this.mTextPaint.setColor(this.getCurrentCollapsedTextColor());
        }
        this.mTextPaint.setShadowLayer(lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, n, null), lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, n, null), lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, n, null), blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, n));
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }
    
    private void calculateUsingTextSize(float min) {
        if (this.mText == null) {
            return;
        }
        final float n = (float)this.mCollapsedBounds.width();
        final float b = (float)this.mExpandedBounds.width();
        final boolean close = isClose(min, this.mCollapsedTextSize);
        boolean linearText = true;
        float mCurrentTextSize;
        int n2;
        if (close) {
            mCurrentTextSize = this.mCollapsedTextSize;
            this.mScale = 1.0f;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mCollapsedTypeface)) {
                this.mCurrentTypeface = this.mCollapsedTypeface;
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            min = n;
        }
        else {
            mCurrentTextSize = this.mExpandedTextSize;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mExpandedTypeface)) {
                this.mCurrentTypeface = this.mExpandedTypeface;
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            if (isClose(min, this.mExpandedTextSize)) {
                this.mScale = 1.0f;
            }
            else {
                this.mScale = min / this.mExpandedTextSize;
            }
            min = this.mCollapsedTextSize / this.mExpandedTextSize;
            if (b * min > n) {
                min = Math.min(n / min, b);
            }
            else {
                min = b;
            }
        }
        int n3 = n2;
        if (min > 0.0f) {
            final boolean b2 = this.mCurrentTextSize != mCurrentTextSize || this.mBoundsChanged || n2 != 0;
            this.mCurrentTextSize = mCurrentTextSize;
            this.mBoundsChanged = false;
            n3 = (b2 ? 1 : 0);
        }
        if (this.mTextToDraw == null || n3 != 0) {
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            this.mTextPaint.setTypeface(this.mCurrentTypeface);
            final TextPaint mTextPaint = this.mTextPaint;
            if (this.mScale == 1.0f) {
                linearText = false;
            }
            mTextPaint.setLinearText(linearText);
            final CharSequence ellipsize = TextUtils.ellipsize(this.mText, this.mTextPaint, min, TextUtils$TruncateAt.END);
            if (!TextUtils.equals(ellipsize, this.mTextToDraw)) {
                this.mTextToDraw = ellipsize;
                this.mIsRtl = this.calculateIsRtl(this.mTextToDraw);
            }
        }
    }
    
    private void clearTexture() {
        if (this.mExpandedTitleTexture != null) {
            this.mExpandedTitleTexture.recycle();
            this.mExpandedTitleTexture = null;
        }
    }
    
    private void ensureExpandedTexture() {
        if (this.mExpandedTitleTexture != null || this.mExpandedBounds.isEmpty() || TextUtils.isEmpty(this.mTextToDraw)) {
            return;
        }
        this.calculateOffsets(0.0f);
        this.mTextureAscent = this.mTextPaint.ascent();
        this.mTextureDescent = this.mTextPaint.descent();
        final int round = Math.round(this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()));
        final int round2 = Math.round(this.mTextureDescent - this.mTextureAscent);
        if (round > 0 && round2 > 0) {
            this.mExpandedTitleTexture = Bitmap.createBitmap(round, round2, Bitmap$Config.ARGB_8888);
            new Canvas(this.mExpandedTitleTexture).drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), 0.0f, round2 - this.mTextPaint.descent(), (Paint)this.mTextPaint);
            if (this.mTexturePaint == null) {
                this.mTexturePaint = new Paint(3);
            }
        }
    }
    
    @ColorInt
    private int getCurrentCollapsedTextColor() {
        if (this.mState != null) {
            return this.mCollapsedTextColor.getColorForState(this.mState, 0);
        }
        return this.mCollapsedTextColor.getDefaultColor();
    }
    
    @ColorInt
    private int getCurrentExpandedTextColor() {
        if (this.mState != null) {
            return this.mExpandedTextColor.getColorForState(this.mState, 0);
        }
        return this.mExpandedTextColor.getDefaultColor();
    }
    
    private void interpolateBounds(final float n) {
        this.mCurrentBounds.left = lerp((float)this.mExpandedBounds.left, (float)this.mCollapsedBounds.left, n, this.mPositionInterpolator);
        this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, n, this.mPositionInterpolator);
        this.mCurrentBounds.right = lerp((float)this.mExpandedBounds.right, (float)this.mCollapsedBounds.right, n, this.mPositionInterpolator);
        this.mCurrentBounds.bottom = lerp((float)this.mExpandedBounds.bottom, (float)this.mCollapsedBounds.bottom, n, this.mPositionInterpolator);
    }
    
    private static boolean isClose(final float n, final float n2) {
        return Math.abs(n - n2) < 0.001f;
    }
    
    private static float lerp(final float n, final float n2, final float n3, final Interpolator interpolator) {
        float interpolation = n3;
        if (interpolator != null) {
            interpolation = interpolator.getInterpolation(n3);
        }
        return AnimationUtils.lerp(n, n2, interpolation);
    }
    
    private Typeface readFontFamilyTypeface(final int n) {
        final TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(n, new int[] { 16843692 });
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
        this.mUseTexture = (CollapsingTextHelper.USE_SCALING_TEXTURE && this.mScale != 1.0f);
        if (this.mUseTexture) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }
    
    public void draw(final Canvas canvas) {
        final int save = canvas.save();
        if (this.mTextToDraw != null && this.mDrawTitle) {
            final float mCurrentDrawX = this.mCurrentDrawX;
            final float mCurrentDrawY = this.mCurrentDrawY;
            final boolean b = this.mUseTexture && this.mExpandedTitleTexture != null;
            float n;
            if (b) {
                n = this.mTextureAscent * this.mScale;
                final float mTextureDescent = this.mTextureDescent;
                final float mScale = this.mScale;
            }
            else {
                n = this.mTextPaint.ascent() * this.mScale;
                this.mTextPaint.descent();
                final float mScale2 = this.mScale;
            }
            float n2 = mCurrentDrawY;
            if (b) {
                n2 = mCurrentDrawY + n;
            }
            if (this.mScale != 1.0f) {
                canvas.scale(this.mScale, this.mScale, mCurrentDrawX, n2);
            }
            if (b) {
                canvas.drawBitmap(this.mExpandedTitleTexture, mCurrentDrawX, n2, this.mTexturePaint);
            }
            else {
                canvas.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), mCurrentDrawX, n2, (Paint)this.mTextPaint);
            }
        }
        canvas.restoreToCount(save);
    }
    
    ColorStateList getCollapsedTextColor() {
        return this.mCollapsedTextColor;
    }
    
    int getCollapsedTextGravity() {
        return this.mCollapsedTextGravity;
    }
    
    float getCollapsedTextSize() {
        return this.mCollapsedTextSize;
    }
    
    Typeface getCollapsedTypeface() {
        Typeface typeface;
        if (this.mCollapsedTypeface != null) {
            typeface = this.mCollapsedTypeface;
        }
        else {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }
    
    ColorStateList getExpandedTextColor() {
        return this.mExpandedTextColor;
    }
    
    int getExpandedTextGravity() {
        return this.mExpandedTextGravity;
    }
    
    float getExpandedTextSize() {
        return this.mExpandedTextSize;
    }
    
    Typeface getExpandedTypeface() {
        Typeface typeface;
        if (this.mExpandedTypeface != null) {
            typeface = this.mExpandedTypeface;
        }
        else {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }
    
    float getExpansionFraction() {
        return this.mExpandedFraction;
    }
    
    CharSequence getText() {
        return this.mText;
    }
    
    final boolean isStateful() {
        return (this.mCollapsedTextColor != null && this.mCollapsedTextColor.isStateful()) || (this.mExpandedTextColor != null && this.mExpandedTextColor.isStateful());
    }
    
    void onBoundsChanged() {
        this.mDrawTitle = (this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0);
    }
    
    public void recalculate() {
        if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
        }
    }
    
    void setCollapsedBounds(final int n, final int n2, final int n3, final int n4) {
        if (!rectEquals(this.mCollapsedBounds, n, n2, n3, n4)) {
            this.mCollapsedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
        }
    }
    
    void setCollapsedTextAppearance(final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize);
        }
        this.mCollapsedShadowColor = obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mCollapsedShadowDx = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mCollapsedShadowDy = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mCollapsedShadowRadius = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        obtainStyledAttributes.recycle();
        if (Build$VERSION.SDK_INT >= 16) {
            this.mCollapsedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }
    
    void setCollapsedTextColor(final ColorStateList mCollapsedTextColor) {
        if (this.mCollapsedTextColor != mCollapsedTextColor) {
            this.mCollapsedTextColor = mCollapsedTextColor;
            this.recalculate();
        }
    }
    
    void setCollapsedTextGravity(final int mCollapsedTextGravity) {
        if (this.mCollapsedTextGravity != mCollapsedTextGravity) {
            this.mCollapsedTextGravity = mCollapsedTextGravity;
            this.recalculate();
        }
    }
    
    void setCollapsedTextSize(final float mCollapsedTextSize) {
        if (this.mCollapsedTextSize != mCollapsedTextSize) {
            this.mCollapsedTextSize = mCollapsedTextSize;
            this.recalculate();
        }
    }
    
    void setCollapsedTypeface(final Typeface mCollapsedTypeface) {
        if (this.areTypefacesDifferent(this.mCollapsedTypeface, mCollapsedTypeface)) {
            this.mCollapsedTypeface = mCollapsedTypeface;
            this.recalculate();
        }
    }
    
    void setExpandedBounds(final int n, final int n2, final int n3, final int n4) {
        if (!rectEquals(this.mExpandedBounds, n, n2, n3, n4)) {
            this.mExpandedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
        }
    }
    
    void setExpandedTextAppearance(final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize);
        }
        this.mExpandedShadowColor = obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mExpandedShadowDx = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mExpandedShadowDy = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mExpandedShadowRadius = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        obtainStyledAttributes.recycle();
        if (Build$VERSION.SDK_INT >= 16) {
            this.mExpandedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }
    
    void setExpandedTextColor(final ColorStateList mExpandedTextColor) {
        if (this.mExpandedTextColor != mExpandedTextColor) {
            this.mExpandedTextColor = mExpandedTextColor;
            this.recalculate();
        }
    }
    
    void setExpandedTextGravity(final int mExpandedTextGravity) {
        if (this.mExpandedTextGravity != mExpandedTextGravity) {
            this.mExpandedTextGravity = mExpandedTextGravity;
            this.recalculate();
        }
    }
    
    void setExpandedTextSize(final float mExpandedTextSize) {
        if (this.mExpandedTextSize != mExpandedTextSize) {
            this.mExpandedTextSize = mExpandedTextSize;
            this.recalculate();
        }
    }
    
    void setExpandedTypeface(final Typeface mExpandedTypeface) {
        if (this.areTypefacesDifferent(this.mExpandedTypeface, mExpandedTypeface)) {
            this.mExpandedTypeface = mExpandedTypeface;
            this.recalculate();
        }
    }
    
    void setExpansionFraction(float clamp) {
        clamp = MathUtils.clamp(clamp, 0.0f, 1.0f);
        if (clamp != this.mExpandedFraction) {
            this.mExpandedFraction = clamp;
            this.calculateCurrentOffsets();
        }
    }
    
    void setPositionInterpolator(final Interpolator mPositionInterpolator) {
        this.mPositionInterpolator = mPositionInterpolator;
        this.recalculate();
    }
    
    final boolean setState(final int[] mState) {
        this.mState = mState;
        if (this.isStateful()) {
            this.recalculate();
            return true;
        }
        return false;
    }
    
    void setText(final CharSequence mText) {
        if (mText == null || !mText.equals(this.mText)) {
            this.mText = mText;
            this.mTextToDraw = null;
            this.clearTexture();
            this.recalculate();
        }
    }
    
    void setTextSizeInterpolator(final Interpolator mTextSizeInterpolator) {
        this.mTextSizeInterpolator = mTextSizeInterpolator;
        this.recalculate();
    }
    
    void setTypefaces(final Typeface typeface) {
        this.mExpandedTypeface = typeface;
        this.mCollapsedTypeface = typeface;
        this.recalculate();
    }
}
