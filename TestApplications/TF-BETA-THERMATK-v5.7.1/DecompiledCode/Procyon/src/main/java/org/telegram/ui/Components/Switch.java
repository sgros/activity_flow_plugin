// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.Drawable$Callback;
import android.content.res.ColorStateList;
import android.util.StateSet;
import android.graphics.Rect;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import androidx.annotation.Keep;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.animation.ObjectAnimator;
import android.view.View;

public class Switch extends View
{
    private boolean attachedToWindow;
    private boolean bitmapsCreated;
    private ObjectAnimator checkAnimator;
    private int colorSet;
    private int drawIconType;
    private boolean drawRipple;
    private ObjectAnimator iconAnimator;
    private Drawable iconDrawable;
    private float iconProgress;
    private boolean isChecked;
    private int lastIconColor;
    private OnCheckedChangeListener onCheckedChangeListener;
    private Bitmap[] overlayBitmap;
    private Canvas[] overlayCanvas;
    private float overlayCx;
    private float overlayCy;
    private Paint overlayEraserPaint;
    private Bitmap overlayMaskBitmap;
    private Canvas overlayMaskCanvas;
    private Paint overlayMaskPaint;
    private float overlayRad;
    private int overrideColorProgress;
    private Paint paint;
    private Paint paint2;
    private int[] pressedState;
    private float progress;
    private RectF rectF;
    private RippleDrawable rippleDrawable;
    private Paint ripplePaint;
    private String thumbCheckedColorKey;
    private String thumbColorKey;
    private String trackCheckedColorKey;
    private String trackColorKey;
    
    public Switch(final Context context) {
        super(context);
        this.iconProgress = 1.0f;
        this.trackColorKey = "switch2Track";
        this.trackCheckedColorKey = "switch2TrackChecked";
        this.thumbColorKey = "windowBackgroundWhite";
        this.thumbCheckedColorKey = "windowBackgroundWhite";
        this.pressedState = new int[] { 16842910, 16842919 };
        this.rectF = new RectF();
        this.paint = new Paint(1);
        (this.paint2 = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.paint2.setStrokeCap(Paint$Cap.ROUND);
        this.paint2.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
    }
    
    private void animateIcon(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.iconAnimator = ObjectAnimator.ofFloat((Object)this, "iconProgress", new float[] { n })).setDuration(250L);
        this.iconAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Switch.this.iconAnimator = null;
            }
        });
        this.iconAnimator.start();
    }
    
    private void animateToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).setDuration(250L);
        this.checkAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Switch.this.checkAnimator = null;
            }
        });
        this.checkAnimator.start();
    }
    
    private void cancelCheckAnimator() {
        final ObjectAnimator checkAnimator = this.checkAnimator;
        if (checkAnimator != null) {
            checkAnimator.cancel();
            this.checkAnimator = null;
        }
    }
    
    private void cancelIconAnimator() {
        final ObjectAnimator iconAnimator = this.iconAnimator;
        if (iconAnimator != null) {
            iconAnimator.cancel();
            this.iconAnimator = null;
        }
    }
    
    @Keep
    public float getIconProgress() {
        return this.iconProgress;
    }
    
    @Keep
    public float getProgress() {
        return this.progress;
    }
    
    public boolean hasIcon() {
        return this.iconDrawable != null;
    }
    
    public boolean isChecked() {
        return this.isChecked;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        final int dp = AndroidUtilities.dp(31.0f);
        AndroidUtilities.dp(20.0f);
        final int n = (this.getMeasuredWidth() - dp) / 2;
        final float n2 = (this.getMeasuredHeight() - AndroidUtilities.dpf2(14.0f)) / 2.0f;
        final int n3 = AndroidUtilities.dp(7.0f) + n + (int)(AndroidUtilities.dp(17.0f) * this.progress);
        final int n4 = this.getMeasuredHeight() / 2;
        for (int i = 0; i < 2; ++i) {
            if (i != 1 || this.overrideColorProgress != 0) {
                Canvas canvas2;
                if (i == 0) {
                    canvas2 = canvas;
                }
                else {
                    canvas2 = this.overlayCanvas[0];
                }
                if (i == 1) {
                    this.overlayBitmap[0].eraseColor(0);
                    this.paint.setColor(-16777216);
                    this.overlayMaskCanvas.drawRect(0.0f, 0.0f, (float)this.overlayMaskBitmap.getWidth(), (float)this.overlayMaskBitmap.getHeight(), this.paint);
                    this.overlayMaskCanvas.drawCircle(this.overlayCx - this.getX(), this.overlayCy - this.getY(), this.overlayRad, this.overlayEraserPaint);
                }
                final int overrideColorProgress = this.overrideColorProgress;
                float progress = 0.0f;
                Label_0254: {
                    Label_0228: {
                        if (overrideColorProgress == 1) {
                            if (i != 0) {
                                break Label_0228;
                            }
                        }
                        else {
                            if (overrideColorProgress != 2) {
                                progress = this.progress;
                                break Label_0254;
                            }
                            if (i == 0) {
                                break Label_0228;
                            }
                        }
                        progress = 0.0f;
                        break Label_0254;
                    }
                    progress = 1.0f;
                }
                final int color = Theme.getColor(this.trackColorKey);
                final int color2 = Theme.getColor(this.trackCheckedColorKey);
                if (i == 0 && this.iconDrawable != null) {
                    final int lastIconColor = this.lastIconColor;
                    int n5;
                    if (this.isChecked) {
                        n5 = color2;
                    }
                    else {
                        n5 = color;
                    }
                    if (lastIconColor != n5) {
                        final Drawable iconDrawable = this.iconDrawable;
                        int lastIconColor2;
                        if (this.isChecked) {
                            lastIconColor2 = color2;
                        }
                        else {
                            lastIconColor2 = color;
                        }
                        this.lastIconColor = lastIconColor2;
                        iconDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(lastIconColor2, PorterDuff$Mode.MULTIPLY));
                    }
                }
                final int red = Color.red(color);
                final int red2 = Color.red(color2);
                final int green = Color.green(color);
                final int green2 = Color.green(color2);
                final int blue = Color.blue(color);
                final int blue2 = Color.blue(color2);
                final int alpha = Color.alpha(color);
                final int n6 = ((int)(red + (red2 - red) * progress) & 0xFF) << 16 | ((int)(alpha + (Color.alpha(color2) - alpha) * progress) & 0xFF) << 24 | ((int)(green + (green2 - green) * progress) & 0xFF) << 8 | ((int)(blue + (blue2 - blue) * progress) & 0xFF);
                this.paint.setColor(n6);
                this.paint2.setColor(n6);
                this.rectF.set((float)n, n2, (float)(n + dp), AndroidUtilities.dpf2(14.0f) + n2);
                canvas2.drawRoundRect(this.rectF, AndroidUtilities.dpf2(7.0f), AndroidUtilities.dpf2(7.0f), this.paint);
                canvas2.drawCircle((float)n3, (float)n4, AndroidUtilities.dpf2(10.0f), this.paint);
                if (i == 0) {
                    final RippleDrawable rippleDrawable = this.rippleDrawable;
                    if (rippleDrawable != null) {
                        rippleDrawable.setBounds(n3 - AndroidUtilities.dp(18.0f), n4 - AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f) + n3, AndroidUtilities.dp(18.0f) + n4);
                        this.rippleDrawable.draw(canvas2);
                        continue;
                    }
                }
                if (i == 1) {
                    canvas2.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                }
            }
        }
        if (this.overrideColorProgress != 0) {
            canvas.drawBitmap(this.overlayBitmap[0], 0.0f, 0.0f, (Paint)null);
        }
        int j = 0;
        int n7 = n4;
        int n8 = n3;
        while (j < 2) {
            if (j != 1 || this.overrideColorProgress != 0) {
                Canvas canvas3;
                if (j == 0) {
                    canvas3 = canvas;
                }
                else {
                    canvas3 = this.overlayCanvas[1];
                }
                if (j == 1) {
                    this.overlayBitmap[1].eraseColor(0);
                }
                final int overrideColorProgress2 = this.overrideColorProgress;
                float progress2 = 0.0f;
                Label_0840: {
                    Label_0814: {
                        if (overrideColorProgress2 == 1) {
                            if (j != 0) {
                                break Label_0814;
                            }
                        }
                        else {
                            if (overrideColorProgress2 != 2) {
                                progress2 = this.progress;
                                break Label_0840;
                            }
                            if (j == 0) {
                                break Label_0814;
                            }
                        }
                        progress2 = 0.0f;
                        break Label_0840;
                    }
                    progress2 = 1.0f;
                }
                final int color3 = Theme.getColor(this.thumbColorKey);
                final int color4 = Theme.getColor(this.thumbCheckedColorKey);
                final int red3 = Color.red(color3);
                final int red4 = Color.red(color4);
                final int green3 = Color.green(color3);
                final int green4 = Color.green(color4);
                final int blue3 = Color.blue(color3);
                final int blue4 = Color.blue(color4);
                final int alpha2 = Color.alpha(color3);
                this.paint.setColor(((int)(alpha2 + (Color.alpha(color4) - alpha2) * progress2) & 0xFF) << 24 | ((int)(red3 + (red4 - red3) * progress2) & 0xFF) << 16 | ((int)(green3 + (green4 - green3) * progress2) & 0xFF) << 8 | ((int)(blue3 + (blue4 - blue3) * progress2) & 0xFF));
                final float n9 = (float)n8;
                final float n10 = (float)n7;
                canvas3.drawCircle(n9, n10, (float)AndroidUtilities.dp(8.0f), this.paint);
                if (j == 0) {
                    final Drawable iconDrawable2 = this.iconDrawable;
                    if (iconDrawable2 != null) {
                        iconDrawable2.setBounds(n8 - iconDrawable2.getIntrinsicWidth() / 2, n7 - this.iconDrawable.getIntrinsicHeight() / 2, this.iconDrawable.getIntrinsicWidth() / 2 + n8, this.iconDrawable.getIntrinsicHeight() / 2 + n7);
                        this.iconDrawable.draw(canvas3);
                    }
                    else {
                        final int drawIconType = this.drawIconType;
                        if (drawIconType == 1) {
                            n8 = (int)(n9 - (AndroidUtilities.dp(10.8f) - AndroidUtilities.dp(1.3f) * this.progress));
                            n7 = (int)(n10 - (AndroidUtilities.dp(8.5f) - AndroidUtilities.dp(0.5f) * this.progress));
                            final int n11 = (int)AndroidUtilities.dpf2(4.6f) + n8;
                            final int n12 = (int)(AndroidUtilities.dpf2(9.5f) + n7);
                            final int dp2 = AndroidUtilities.dp(2.0f);
                            final int dp3 = AndroidUtilities.dp(2.0f);
                            final int n13 = (int)AndroidUtilities.dpf2(7.5f) + n8;
                            final int n14 = (int)AndroidUtilities.dpf2(5.4f) + n7;
                            final int n15 = n13 + AndroidUtilities.dp(7.0f);
                            final int n16 = n14 + AndroidUtilities.dp(7.0f);
                            final float n17 = (float)n13;
                            final float n18 = (float)(n11 - n13);
                            final float progress3 = this.progress;
                            canvas3.drawLine((float)(int)(n17 + n18 * progress3), (float)(int)(n14 + (n12 - n14) * progress3), (float)(int)(n15 + (dp2 + n11 - n15) * progress3), (float)(int)(n16 + (dp3 + n12 - n16) * progress3), this.paint2);
                            final int n19 = (int)AndroidUtilities.dpf2(7.5f) + n8;
                            final int n20 = (int)AndroidUtilities.dpf2(12.5f) + n7;
                            canvas3.drawLine((float)n19, (float)n20, (float)(AndroidUtilities.dp(7.0f) + n19), (float)(n20 - AndroidUtilities.dp(7.0f)), this.paint2);
                        }
                        else if (drawIconType == 2 || this.iconAnimator != null) {
                            this.paint2.setAlpha((int)((1.0f - this.iconProgress) * 255.0f));
                            canvas3.drawLine(n9, n10, n9, (float)(n7 - AndroidUtilities.dp(5.0f)), this.paint2);
                            canvas3.save();
                            canvas3.rotate(this.iconProgress * -90.0f, n9, n10);
                            canvas3.drawLine(n9, n10, (float)(AndroidUtilities.dp(4.0f) + n8), n10, this.paint2);
                            canvas3.restore();
                        }
                    }
                }
                if (j == 1) {
                    canvas3.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                }
            }
            ++j;
        }
        if (this.overrideColorProgress != 0) {
            canvas.drawBitmap(this.overlayBitmap[1], 0.0f, 0.0f, (Paint)null);
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked);
    }
    
    public void setChecked(final boolean isChecked, final int drawIconType, final boolean b) {
        final boolean isChecked2 = this.isChecked;
        final float n = 1.0f;
        if (isChecked != isChecked2) {
            this.isChecked = isChecked;
            if (this.attachedToWindow && b) {
                this.animateToCheckedState(isChecked);
            }
            else {
                this.cancelCheckAnimator();
                float progress;
                if (isChecked) {
                    progress = 1.0f;
                }
                else {
                    progress = 0.0f;
                }
                this.setProgress(progress);
            }
            final OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(this, isChecked);
            }
        }
        if (this.drawIconType != drawIconType) {
            this.drawIconType = drawIconType;
            if (this.attachedToWindow && b) {
                this.animateIcon(drawIconType == 0);
            }
            else {
                this.cancelIconAnimator();
                float iconProgress;
                if (drawIconType == 0) {
                    iconProgress = n;
                }
                else {
                    iconProgress = 0.0f;
                }
                this.setIconProgress(iconProgress);
            }
        }
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.setChecked(b, this.drawIconType, b2);
    }
    
    public void setColors(final String trackColorKey, final String trackCheckedColorKey, final String thumbColorKey, final String thumbCheckedColorKey) {
        this.trackColorKey = trackColorKey;
        this.trackCheckedColorKey = trackCheckedColorKey;
        this.thumbColorKey = thumbColorKey;
        this.thumbCheckedColorKey = thumbCheckedColorKey;
    }
    
    public void setDrawIconType(final int drawIconType) {
        this.drawIconType = drawIconType;
    }
    
    public void setDrawRipple(final boolean drawRipple) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (drawRipple != this.drawRipple) {
                this.drawRipple = drawRipple;
                final RippleDrawable rippleDrawable = this.rippleDrawable;
                int colorSet = 1;
                if (rippleDrawable == null) {
                    (this.ripplePaint = new Paint(1)).setColor(-1);
                    Drawable drawable;
                    if (Build$VERSION.SDK_INT >= 23) {
                        drawable = null;
                    }
                    else {
                        drawable = new Drawable() {
                            public void draw(final Canvas canvas) {
                                final Rect bounds = this.getBounds();
                                canvas.drawCircle((float)bounds.centerX(), (float)bounds.centerY(), (float)AndroidUtilities.dp(18.0f), Switch.this.ripplePaint);
                            }
                            
                            public int getOpacity() {
                                return 0;
                            }
                            
                            public void setAlpha(final int n) {
                            }
                            
                            public void setColorFilter(final ColorFilter colorFilter) {
                            }
                        };
                    }
                    this.rippleDrawable = new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { 0 }), (Drawable)null, drawable);
                    if (Build$VERSION.SDK_INT >= 23) {
                        this.rippleDrawable.setRadius(AndroidUtilities.dp(18.0f));
                    }
                    this.rippleDrawable.setCallback((Drawable$Callback)this);
                }
                if ((this.isChecked && this.colorSet != 2) || (!this.isChecked && this.colorSet != 1)) {
                    String s;
                    if (this.isChecked) {
                        s = "switchTrackBlueSelectorChecked";
                    }
                    else {
                        s = "switchTrackBlueSelector";
                    }
                    this.rippleDrawable.setColor(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { Theme.getColor(s) }));
                    if (this.isChecked) {
                        colorSet = 2;
                    }
                    this.colorSet = colorSet;
                }
                if (Build$VERSION.SDK_INT >= 28 && drawRipple) {
                    final RippleDrawable rippleDrawable2 = this.rippleDrawable;
                    float n;
                    if (this.isChecked) {
                        n = 0.0f;
                    }
                    else {
                        n = (float)AndroidUtilities.dp(100.0f);
                    }
                    rippleDrawable2.setHotspot(n, (float)AndroidUtilities.dp(18.0f));
                }
                final RippleDrawable rippleDrawable3 = this.rippleDrawable;
                int[] state;
                if (drawRipple) {
                    state = this.pressedState;
                }
                else {
                    state = StateSet.NOTHING;
                }
                rippleDrawable3.setState(state);
                this.invalidate();
            }
        }
    }
    
    public void setIcon(int color) {
        if (color != 0) {
            this.iconDrawable = this.getResources().getDrawable(color).mutate();
            final Drawable iconDrawable = this.iconDrawable;
            if (iconDrawable != null) {
                String s;
                if (this.isChecked) {
                    s = this.trackCheckedColorKey;
                }
                else {
                    s = this.trackColorKey;
                }
                color = Theme.getColor(s);
                this.lastIconColor = color;
                iconDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(color, PorterDuff$Mode.MULTIPLY));
            }
        }
        else {
            this.iconDrawable = null;
        }
    }
    
    @Keep
    public void setIconProgress(final float iconProgress) {
        if (this.iconProgress == iconProgress) {
            return;
        }
        this.iconProgress = iconProgress;
        this.invalidate();
    }
    
    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
    
    public void setOverrideColor(final int overrideColorProgress) {
        if (this.overrideColorProgress == overrideColorProgress) {
            return;
        }
        if (this.overlayBitmap == null) {
            try {
                this.overlayBitmap = new Bitmap[2];
                this.overlayCanvas = new Canvas[2];
                for (int i = 0; i < 2; ++i) {
                    this.overlayBitmap[i] = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Bitmap$Config.ARGB_8888);
                    this.overlayCanvas[i] = new Canvas(this.overlayBitmap[i]);
                }
                this.overlayMaskBitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Bitmap$Config.ARGB_8888);
                this.overlayMaskCanvas = new Canvas(this.overlayMaskBitmap);
                (this.overlayEraserPaint = new Paint(1)).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
                (this.overlayMaskPaint = new Paint(1)).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
                this.bitmapsCreated = true;
            }
            catch (Throwable t) {
                return;
            }
        }
        if (!this.bitmapsCreated) {
            return;
        }
        this.overrideColorProgress = overrideColorProgress;
        this.overlayCx = 0.0f;
        this.overlayCy = 0.0f;
        this.overlayRad = 0.0f;
        this.invalidate();
    }
    
    public void setOverrideColorProgress(final float overlayCx, final float overlayCy, final float overlayRad) {
        this.overlayCx = overlayCx;
        this.overlayCy = overlayCy;
        this.overlayRad = overlayRad;
        this.invalidate();
    }
    
    @Keep
    public void setProgress(final float progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        if (!super.verifyDrawable(drawable)) {
            final RippleDrawable rippleDrawable = this.rippleDrawable;
            if (rippleDrawable == null || drawable != rippleDrawable) {
                return false;
            }
        }
        return true;
    }
    
    public interface OnCheckedChangeListener
    {
        void onCheckedChanged(final Switch p0, final boolean p1);
    }
}
