// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable$Orientation;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.text.TextPaint;
import android.text.SpannableStringBuilder;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Drawable$Callback;
import android.view.View;

public class SimpleTextView extends View implements Drawable$Callback
{
    private static final int DIST_BETWEEN_SCROLLING_TEXT = 16;
    private static final int PIXELS_PER_SECOND = 50;
    private static final int PIXELS_PER_SECOND_SLOW = 30;
    private static final int SCROLL_DELAY_MS = 500;
    private static final int SCROLL_SLOWDOWN_PX = 100;
    private int currentScrollDelay;
    private int drawablePadding;
    private GradientDrawable fadeDrawable;
    private GradientDrawable fadeDrawableBack;
    private int gravity;
    private long lastUpdateTime;
    private int lastWidth;
    private Layout layout;
    private Drawable leftDrawable;
    private int leftDrawableTopPadding;
    private int offsetX;
    private int offsetY;
    private Drawable rightDrawable;
    private int rightDrawableTopPadding;
    private boolean scrollNonFitText;
    private float scrollingOffset;
    private SpannableStringBuilder spannableStringBuilder;
    private CharSequence text;
    private boolean textDoesNotFit;
    private int textHeight;
    private TextPaint textPaint;
    private int textWidth;
    private int totalWidth;
    private boolean wasLayout;
    
    public SimpleTextView(final Context context) {
        super(context);
        this.gravity = 51;
        this.drawablePadding = AndroidUtilities.dp(4.0f);
        this.textPaint = new TextPaint(1);
        this.setImportantForAccessibility(1);
    }
    
    private void calcOffset(final int n) {
        if (this.layout.getLineCount() > 0) {
            final Layout layout = this.layout;
            boolean textDoesNotFit = false;
            this.textWidth = (int)Math.ceil(layout.getLineWidth(0));
            this.textHeight = this.layout.getLineBottom(0);
            if ((this.gravity & 0x70) == 0x10) {
                this.offsetY = (this.getMeasuredHeight() - this.textHeight) / 2;
            }
            else {
                this.offsetY = 0;
            }
            if ((this.gravity & 0x7) == 0x3) {
                this.offsetX = -(int)this.layout.getLineLeft(0);
            }
            else if (this.layout.getLineLeft(0) == 0.0f) {
                this.offsetX = n - this.textWidth;
            }
            else {
                this.offsetX = -AndroidUtilities.dp(8.0f);
            }
            this.offsetX += this.getPaddingLeft();
            if (this.textWidth > n) {
                textDoesNotFit = true;
            }
            this.textDoesNotFit = textDoesNotFit;
        }
    }
    
    private boolean createLayout(int n) {
        Label_0165: {
            if (this.text == null) {
                break Label_0165;
            }
            int n2 = n;
            while (true) {
                try {
                    if (this.leftDrawable != null) {
                        n2 = n - this.leftDrawable.getIntrinsicWidth() - this.drawablePadding;
                    }
                    n = n2;
                    if (this.rightDrawable != null) {
                        n = n2 - this.rightDrawable.getIntrinsicWidth() - this.drawablePadding;
                    }
                    CharSequence charSequence;
                    if (this.scrollNonFitText) {
                        charSequence = this.text;
                    }
                    else {
                        charSequence = TextUtils.ellipsize(this.text, this.textPaint, (float)n, TextUtils$TruncateAt.END);
                    }
                    final int length = charSequence.length();
                    final TextPaint textPaint = this.textPaint;
                    int dp;
                    if (this.scrollNonFitText) {
                        dp = AndroidUtilities.dp(2000.0f);
                    }
                    else {
                        dp = AndroidUtilities.dp(8.0f) + n;
                    }
                    this.layout = (Layout)new StaticLayout(charSequence, 0, length, textPaint, dp, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.calcOffset(n);
                    this.invalidate();
                    return true;
                    this.layout = null;
                    this.textWidth = 0;
                    this.textHeight = 0;
                    continue;
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private boolean recreateLayoutMaybe() {
        if (this.wasLayout && this.getMeasuredHeight() != 0) {
            return this.createLayout(this.getMeasuredWidth());
        }
        this.requestLayout();
        return true;
    }
    
    private void updateScrollAnimation() {
        if (this.scrollNonFitText) {
            if (this.textDoesNotFit || this.scrollingOffset != 0.0f) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                long n;
                if ((n = uptimeMillis - this.lastUpdateTime) > 17L) {
                    n = 17L;
                }
                final int currentScrollDelay = this.currentScrollDelay;
                if (currentScrollDelay > 0) {
                    this.currentScrollDelay = (int)(currentScrollDelay - n);
                }
                else {
                    final int n2 = this.totalWidth + AndroidUtilities.dp(16.0f);
                    final float scrollingOffset = this.scrollingOffset;
                    final float n3 = (float)AndroidUtilities.dp(100.0f);
                    float n4 = 50.0f;
                    if (scrollingOffset < n3) {
                        n4 = this.scrollingOffset / AndroidUtilities.dp(100.0f) * 20.0f + 30.0f;
                    }
                    else if (this.scrollingOffset >= n2 - AndroidUtilities.dp(100.0f)) {
                        n4 = 50.0f - (this.scrollingOffset - (n2 - AndroidUtilities.dp(100.0f))) / AndroidUtilities.dp(100.0f) * 20.0f;
                    }
                    this.scrollingOffset += n / 1000.0f * AndroidUtilities.dp(n4);
                    this.lastUpdateTime = uptimeMillis;
                    if (this.scrollingOffset > n2) {
                        this.scrollingOffset = 0.0f;
                        this.currentScrollDelay = 500;
                    }
                }
                this.invalidate();
            }
        }
    }
    
    public Paint getPaint() {
        return (Paint)this.textPaint;
    }
    
    public Drawable getRightDrawable() {
        return this.rightDrawable;
    }
    
    public int getSideDrawablesSize() {
        final Drawable leftDrawable = this.leftDrawable;
        int n = 0;
        if (leftDrawable != null) {
            n = 0 + (leftDrawable.getIntrinsicWidth() + this.drawablePadding);
        }
        final Drawable rightDrawable = this.rightDrawable;
        int n2 = n;
        if (rightDrawable != null) {
            n2 = n + (rightDrawable.getIntrinsicWidth() + this.drawablePadding);
        }
        return n2;
    }
    
    public CharSequence getText() {
        CharSequence text;
        if ((text = this.text) == null) {
            text = "";
        }
        return text;
    }
    
    public int getTextHeight() {
        return this.textHeight;
    }
    
    public TextPaint getTextPaint() {
        return this.textPaint;
    }
    
    public int getTextStartX() {
        final Layout layout = this.layout;
        final int n = 0;
        if (layout == null) {
            return 0;
        }
        final Drawable leftDrawable = this.leftDrawable;
        int n2 = n;
        if (leftDrawable != null) {
            n2 = n;
            if ((this.gravity & 0x7) == 0x3) {
                n2 = 0 + (this.drawablePadding + leftDrawable.getIntrinsicWidth());
            }
        }
        return (int)this.getX() + this.offsetX + n2;
    }
    
    public int getTextStartY() {
        if (this.layout == null) {
            return 0;
        }
        return (int)this.getY();
    }
    
    public int getTextWidth() {
        return this.textWidth;
    }
    
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        final Drawable leftDrawable = this.leftDrawable;
        if (drawable == leftDrawable) {
            this.invalidate(leftDrawable.getBounds());
        }
        else {
            final Drawable rightDrawable = this.rightDrawable;
            if (drawable == rightDrawable) {
                this.invalidate(rightDrawable.getBounds());
            }
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.wasLayout = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        this.totalWidth = this.textWidth;
        final Drawable leftDrawable = this.leftDrawable;
        int n3;
        if (leftDrawable != null) {
            final int n = (int)(-this.scrollingOffset);
            final int n2 = (this.textHeight - leftDrawable.getIntrinsicHeight()) / 2 + this.leftDrawableTopPadding;
            final Drawable leftDrawable2 = this.leftDrawable;
            leftDrawable2.setBounds(n, n2, leftDrawable2.getIntrinsicWidth() + n, this.leftDrawable.getIntrinsicHeight() + n2);
            this.leftDrawable.draw(canvas);
            if ((this.gravity & 0x7) == 0x3) {
                n3 = this.drawablePadding + this.leftDrawable.getIntrinsicWidth() + 0;
            }
            else {
                n3 = 0;
            }
            this.totalWidth += this.drawablePadding + this.leftDrawable.getIntrinsicWidth();
        }
        else {
            n3 = 0;
        }
        final Drawable rightDrawable = this.rightDrawable;
        if (rightDrawable != null) {
            final int n4 = this.textWidth + n3 + this.drawablePadding + (int)(-this.scrollingOffset);
            final int n5 = (this.textHeight - rightDrawable.getIntrinsicHeight()) / 2 + this.rightDrawableTopPadding;
            final Drawable rightDrawable2 = this.rightDrawable;
            rightDrawable2.setBounds(n4, n5, rightDrawable2.getIntrinsicWidth() + n4, this.rightDrawable.getIntrinsicHeight() + n5);
            this.rightDrawable.draw(canvas);
            this.totalWidth += this.drawablePadding + this.rightDrawable.getIntrinsicWidth();
        }
        final int n6 = this.totalWidth + AndroidUtilities.dp(16.0f);
        final float scrollingOffset = this.scrollingOffset;
        if (scrollingOffset != 0.0f) {
            final Drawable leftDrawable3 = this.leftDrawable;
            if (leftDrawable3 != null) {
                final int n7 = (int)(-scrollingOffset) + n6;
                final int n8 = (this.textHeight - leftDrawable3.getIntrinsicHeight()) / 2 + this.leftDrawableTopPadding;
                final Drawable leftDrawable4 = this.leftDrawable;
                leftDrawable4.setBounds(n7, n8, leftDrawable4.getIntrinsicWidth() + n7, this.leftDrawable.getIntrinsicHeight() + n8);
                this.leftDrawable.draw(canvas);
            }
            final Drawable rightDrawable3 = this.rightDrawable;
            if (rightDrawable3 != null) {
                final int n9 = this.textWidth + n3 + this.drawablePadding + (int)(-this.scrollingOffset) + n6;
                final int n10 = (this.textHeight - rightDrawable3.getIntrinsicHeight()) / 2 + this.rightDrawableTopPadding;
                final Drawable rightDrawable4 = this.rightDrawable;
                rightDrawable4.setBounds(n9, n10, rightDrawable4.getIntrinsicWidth() + n9, this.rightDrawable.getIntrinsicHeight() + n10);
                this.rightDrawable.draw(canvas);
            }
        }
        if (this.layout != null) {
            if (this.offsetX + n3 != 0 || this.offsetY != 0 || this.scrollingOffset != 0.0f) {
                canvas.save();
                canvas.translate(this.offsetX + n3 - this.scrollingOffset, (float)this.offsetY);
                final float scrollingOffset2 = this.scrollingOffset;
            }
            this.layout.draw(canvas);
            if (this.scrollingOffset != 0.0f) {
                canvas.translate((float)n6, 0.0f);
                this.layout.draw(canvas);
            }
            if (this.offsetX + n3 != 0 || this.offsetY != 0 || this.scrollingOffset != 0.0f) {
                canvas.restore();
            }
            if (this.scrollNonFitText && (this.textDoesNotFit || this.scrollingOffset != 0.0f)) {
                if (this.scrollingOffset < AndroidUtilities.dp(10.0f)) {
                    this.fadeDrawable.setAlpha((int)(this.scrollingOffset / AndroidUtilities.dp(10.0f) * 255.0f));
                }
                else if (this.scrollingOffset > this.totalWidth + AndroidUtilities.dp(16.0f) - AndroidUtilities.dp(10.0f)) {
                    this.fadeDrawable.setAlpha((int)((1.0f - (this.scrollingOffset - (this.totalWidth + AndroidUtilities.dp(16.0f) - AndroidUtilities.dp(10.0f))) / AndroidUtilities.dp(10.0f)) * 255.0f));
                }
                else {
                    this.fadeDrawable.setAlpha(255);
                }
                this.fadeDrawable.setBounds(0, 0, AndroidUtilities.dp(6.0f), this.getMeasuredHeight());
                this.fadeDrawable.draw(canvas);
                this.fadeDrawableBack.setBounds(this.getMeasuredWidth() - AndroidUtilities.dp(6.0f), 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                this.fadeDrawableBack.draw(canvas);
            }
            this.updateScrollAnimation();
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setVisibleToUser(true);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.TextView");
        accessibilityNodeInfo.setText(this.text);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.wasLayout = true;
    }
    
    protected void onMeasure(int n, final int n2) {
        final int size = View$MeasureSpec.getSize(n);
        n = View$MeasureSpec.getSize(n2);
        final int lastWidth = this.lastWidth;
        final int x = AndroidUtilities.displaySize.x;
        if (lastWidth != x) {
            this.lastWidth = x;
            this.scrollingOffset = 0.0f;
            this.currentScrollDelay = 500;
        }
        this.createLayout(size - this.getPaddingLeft() - this.getPaddingRight());
        if (View$MeasureSpec.getMode(n2) != 1073741824) {
            n = this.textHeight;
        }
        this.setMeasuredDimension(size, n);
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        if (this.scrollNonFitText) {
            final GradientDrawable fadeDrawable = this.fadeDrawable;
            if (fadeDrawable != null) {
                fadeDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(backgroundColor, PorterDuff$Mode.MULTIPLY));
                this.fadeDrawableBack.setColorFilter((ColorFilter)new PorterDuffColorFilter(backgroundColor, PorterDuff$Mode.MULTIPLY));
            }
        }
        else {
            super.setBackgroundColor(backgroundColor);
        }
    }
    
    public void setDrawablePadding(final int drawablePadding) {
        if (this.drawablePadding == drawablePadding) {
            return;
        }
        this.drawablePadding = drawablePadding;
        if (!this.recreateLayoutMaybe()) {
            this.invalidate();
        }
    }
    
    public void setGravity(final int gravity) {
        this.gravity = gravity;
    }
    
    public void setLeftDrawable(final int n) {
        Drawable drawable;
        if (n == 0) {
            drawable = null;
        }
        else {
            drawable = this.getContext().getResources().getDrawable(n);
        }
        this.setLeftDrawable(drawable);
    }
    
    public void setLeftDrawable(final Drawable leftDrawable) {
        final Drawable leftDrawable2 = this.leftDrawable;
        if (leftDrawable2 == leftDrawable) {
            return;
        }
        if (leftDrawable2 != null) {
            leftDrawable2.setCallback((Drawable$Callback)null);
        }
        if ((this.leftDrawable = leftDrawable) != null) {
            leftDrawable.setCallback((Drawable$Callback)this);
        }
        if (!this.recreateLayoutMaybe()) {
            this.invalidate();
        }
    }
    
    public void setLeftDrawableTopPadding(final int leftDrawableTopPadding) {
        this.leftDrawableTopPadding = leftDrawableTopPadding;
    }
    
    public void setLinkTextColor(final int linkColor) {
        this.textPaint.linkColor = linkColor;
        this.invalidate();
    }
    
    public void setRightDrawable(final int n) {
        Drawable drawable;
        if (n == 0) {
            drawable = null;
        }
        else {
            drawable = this.getContext().getResources().getDrawable(n);
        }
        this.setRightDrawable(drawable);
    }
    
    public void setRightDrawable(final Drawable rightDrawable) {
        final Drawable rightDrawable2 = this.rightDrawable;
        if (rightDrawable2 == rightDrawable) {
            return;
        }
        if (rightDrawable2 != null) {
            rightDrawable2.setCallback((Drawable$Callback)null);
        }
        if ((this.rightDrawable = rightDrawable) != null) {
            rightDrawable.setCallback((Drawable$Callback)this);
        }
        if (!this.recreateLayoutMaybe()) {
            this.invalidate();
        }
    }
    
    public void setRightDrawableTopPadding(final int rightDrawableTopPadding) {
        this.rightDrawableTopPadding = rightDrawableTopPadding;
    }
    
    public void setScrollNonFitText(final boolean scrollNonFitText) {
        if (this.scrollNonFitText == scrollNonFitText) {
            return;
        }
        this.scrollNonFitText = scrollNonFitText;
        if (this.scrollNonFitText) {
            this.fadeDrawable = new GradientDrawable(GradientDrawable$Orientation.LEFT_RIGHT, new int[] { -1, 0 });
            this.fadeDrawableBack = new GradientDrawable(GradientDrawable$Orientation.LEFT_RIGHT, new int[] { 0, -1 });
        }
        this.requestLayout();
    }
    
    public void setSideDrawablesColor(final int n) {
        Theme.setDrawableColor(this.rightDrawable, n);
        Theme.setDrawableColor(this.leftDrawable, n);
    }
    
    public boolean setText(final CharSequence charSequence) {
        return this.setText(charSequence, false);
    }
    
    public boolean setText(final CharSequence charSequence, final boolean b) {
        if (this.text != null || charSequence != null) {
            if (!b) {
                final CharSequence text = this.text;
                if (text != null && text.equals(charSequence)) {
                    return false;
                }
            }
            this.text = charSequence;
            this.scrollingOffset = 0.0f;
            this.currentScrollDelay = 500;
            this.recreateLayoutMaybe();
            return true;
        }
        return false;
    }
    
    public void setTextColor(final int color) {
        this.textPaint.setColor(color);
        this.invalidate();
    }
    
    public void setTextSize(final int n) {
        final float textSize = (float)AndroidUtilities.dp((float)n);
        if (textSize == this.textPaint.getTextSize()) {
            return;
        }
        this.textPaint.setTextSize(textSize);
        if (!this.recreateLayoutMaybe()) {
            this.invalidate();
        }
    }
    
    public void setTypeface(final Typeface typeface) {
        this.textPaint.setTypeface(typeface);
    }
}
