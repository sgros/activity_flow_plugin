// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import java.util.Locale;
import android.graphics.Paint$Style;
import android.graphics.Bitmap$Config;
import android.graphics.Color;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.RectF;
import android.view.View;
import org.telegram.messenger.ImageReceiver;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class RadialProgress2
{
    private int backgroundStroke;
    private int circleColor;
    private String circleColorKey;
    private Paint circleMiniPaint;
    private Paint circlePaint;
    private int circlePressedColor;
    private String circlePressedColorKey;
    private int circleRadius;
    private boolean drawBackground;
    private boolean drawMiniIcon;
    private int iconColor;
    private String iconColorKey;
    private int iconPressedColor;
    private String iconPressedColorKey;
    private boolean isPressed;
    private boolean isPressedMini;
    private MediaActionDrawable mediaActionDrawable;
    private Bitmap miniDrawBitmap;
    private Canvas miniDrawCanvas;
    private MediaActionDrawable miniMediaActionDrawable;
    private Paint miniProgressBackgroundPaint;
    private ImageReceiver overlayImageView;
    private Paint overlayPaint;
    private float overrideAlpha;
    private View parent;
    private boolean previousCheckDrawable;
    private int progressColor;
    private RectF progressRect;
    
    public RadialProgress2(final View parent) {
        this.progressRect = new RectF();
        this.progressColor = -1;
        this.overlayPaint = new Paint(1);
        this.circlePaint = new Paint(1);
        this.circleMiniPaint = new Paint(1);
        this.drawBackground = true;
        this.overrideAlpha = 1.0f;
        this.miniProgressBackgroundPaint = new Paint(1);
        this.parent = parent;
        (this.overlayImageView = new ImageReceiver(parent)).setInvalidateAll(true);
        this.mediaActionDrawable = new MediaActionDrawable();
        final MediaActionDrawable mediaActionDrawable = this.mediaActionDrawable;
        parent.getClass();
        mediaActionDrawable.setDelegate((MediaActionDrawable.MediaActionDrawableDelegate)new _$$Lambda$F8rg4UBMmP_S27QL_K3VXBnPS_E(parent));
        this.miniMediaActionDrawable = new MediaActionDrawable();
        final MediaActionDrawable miniMediaActionDrawable = this.miniMediaActionDrawable;
        parent.getClass();
        miniMediaActionDrawable.setDelegate((MediaActionDrawable.MediaActionDrawableDelegate)new _$$Lambda$F8rg4UBMmP_S27QL_K3VXBnPS_E(parent));
        this.miniMediaActionDrawable.setMini(true);
        this.miniMediaActionDrawable.setIcon(4, false);
        this.circleRadius = AndroidUtilities.dp(22.0f);
        this.overlayImageView.setRoundRadius(this.circleRadius);
        this.overlayPaint.setColor(1677721600);
    }
    
    private void invalidateParent() {
        final int dp = AndroidUtilities.dp(2.0f);
        final View parent = this.parent;
        final RectF progressRect = this.progressRect;
        final int n = (int)progressRect.left;
        final int n2 = (int)progressRect.top;
        final int n3 = (int)progressRect.right;
        final int n4 = dp * 2;
        parent.invalidate(n - dp, n2 - dp, n3 + n4, (int)progressRect.bottom + n4);
    }
    
    public void draw(final Canvas canvas) {
        if (this.mediaActionDrawable.getCurrentIcon() == 4 && this.mediaActionDrawable.getTransitionProgress() >= 1.0f) {
            return;
        }
        final int currentIcon = this.mediaActionDrawable.getCurrentIcon();
        final int previousIcon = this.mediaActionDrawable.getPreviousIcon();
        float n2 = 0.0f;
        Label_0157: {
            Label_0087: {
                float n;
                if (this.backgroundStroke != 0) {
                    if (currentIcon == 3) {
                        n = this.mediaActionDrawable.getTransitionProgress();
                    }
                    else {
                        if (previousIcon == 3) {
                            n2 = this.mediaActionDrawable.getTransitionProgress();
                            break Label_0157;
                        }
                        break Label_0087;
                    }
                }
                else {
                    if ((currentIcon == 3 || currentIcon == 6 || currentIcon == 10 || currentIcon == 8 || currentIcon == 0) && previousIcon == 4) {
                        n2 = this.mediaActionDrawable.getTransitionProgress();
                        break Label_0157;
                    }
                    if (currentIcon != 4) {
                        break Label_0087;
                    }
                    n = this.mediaActionDrawable.getTransitionProgress();
                }
                n2 = 1.0f - n;
                break Label_0157;
            }
            n2 = 1.0f;
        }
        if (this.isPressedMini) {
            final String iconPressedColorKey = this.iconPressedColorKey;
            if (iconPressedColorKey != null) {
                this.miniMediaActionDrawable.setColor(Theme.getColor(iconPressedColorKey));
            }
            else {
                this.miniMediaActionDrawable.setColor(this.iconPressedColor);
            }
            final String circlePressedColorKey = this.circlePressedColorKey;
            if (circlePressedColorKey != null) {
                this.circleMiniPaint.setColor(Theme.getColor(circlePressedColorKey));
            }
            else {
                this.circleMiniPaint.setColor(this.circlePressedColor);
            }
        }
        else {
            final String iconColorKey = this.iconColorKey;
            if (iconColorKey != null) {
                this.miniMediaActionDrawable.setColor(Theme.getColor(iconColorKey));
            }
            else {
                this.miniMediaActionDrawable.setColor(this.iconColor);
            }
            final String circleColorKey = this.circleColorKey;
            if (circleColorKey != null) {
                this.circleMiniPaint.setColor(Theme.getColor(circleColorKey));
            }
            else {
                this.circleMiniPaint.setColor(this.circleColor);
            }
        }
        int n3;
        if (this.isPressed) {
            final String iconPressedColorKey2 = this.iconPressedColorKey;
            if (iconPressedColorKey2 != null) {
                final MediaActionDrawable mediaActionDrawable = this.mediaActionDrawable;
                n3 = Theme.getColor(iconPressedColorKey2);
                mediaActionDrawable.setColor(n3);
            }
            else {
                final MediaActionDrawable mediaActionDrawable2 = this.mediaActionDrawable;
                n3 = this.iconPressedColor;
                mediaActionDrawable2.setColor(n3);
            }
            final String circlePressedColorKey2 = this.circlePressedColorKey;
            if (circlePressedColorKey2 != null) {
                this.circlePaint.setColor(Theme.getColor(circlePressedColorKey2));
            }
            else {
                this.circlePaint.setColor(this.circlePressedColor);
            }
        }
        else {
            final String iconColorKey2 = this.iconColorKey;
            if (iconColorKey2 != null) {
                final MediaActionDrawable mediaActionDrawable3 = this.mediaActionDrawable;
                n3 = Theme.getColor(iconColorKey2);
                mediaActionDrawable3.setColor(n3);
            }
            else {
                final MediaActionDrawable mediaActionDrawable4 = this.mediaActionDrawable;
                n3 = this.iconColor;
                mediaActionDrawable4.setColor(n3);
            }
            final String circleColorKey2 = this.circleColorKey;
            if (circleColorKey2 != null) {
                this.circlePaint.setColor(Theme.getColor(circleColorKey2));
            }
            else {
                this.circlePaint.setColor(this.circleColor);
            }
        }
        if (this.drawMiniIcon && this.miniDrawCanvas != null) {
            this.miniDrawBitmap.eraseColor(0);
        }
        this.circlePaint.setAlpha((int)(this.circlePaint.getAlpha() * n2 * this.overrideAlpha));
        this.circleMiniPaint.setAlpha((int)(this.circleMiniPaint.getAlpha() * n2 * this.overrideAlpha));
        int n4;
        int n5;
        if (this.drawMiniIcon && this.miniDrawCanvas != null) {
            n4 = (int)(this.progressRect.width() / 2.0f);
            n5 = (int)(this.progressRect.height() / 2.0f);
        }
        else {
            n4 = (int)this.progressRect.centerX();
            n5 = (int)this.progressRect.centerY();
        }
        final boolean hasBitmapImage = this.overlayImageView.hasBitmapImage();
        final int n6 = 2;
        boolean b;
        if (hasBitmapImage) {
            final float currentAlpha = this.overlayImageView.getCurrentAlpha();
            this.overlayPaint.setAlpha((int)(100.0f * currentAlpha * n2 * this.overrideAlpha));
            int argb;
            if (currentAlpha >= 1.0f) {
                argb = -1;
                b = false;
            }
            else {
                final int red = Color.red(n3);
                final int green = Color.green(n3);
                final int blue = Color.blue(n3);
                final int alpha = Color.alpha(n3);
                argb = Color.argb(alpha + (int)((255 - alpha) * currentAlpha), red + (int)((255 - red) * currentAlpha), green + (int)((255 - green) * currentAlpha), blue + (int)((255 - blue) * currentAlpha));
                b = true;
            }
            this.mediaActionDrawable.setColor(argb);
            final ImageReceiver overlayImageView = this.overlayImageView;
            final int circleRadius = this.circleRadius;
            overlayImageView.setImageCoords(n4 - circleRadius, n5 - circleRadius, circleRadius * 2, circleRadius * 2);
        }
        else {
            b = true;
        }
        Label_0965: {
            if (b && this.drawBackground) {
                if (this.drawMiniIcon) {
                    final Canvas miniDrawCanvas = this.miniDrawCanvas;
                    if (miniDrawCanvas != null) {
                        miniDrawCanvas.drawCircle((float)n4, (float)n5, (float)this.circleRadius, this.circlePaint);
                        break Label_0965;
                    }
                }
                if (currentIcon != 4 || n2 != 0.0f) {
                    if (this.backgroundStroke != 0) {
                        canvas.drawCircle((float)n4, (float)n5, (float)(this.circleRadius - AndroidUtilities.dp(3.5f)), this.circlePaint);
                    }
                    else {
                        canvas.drawCircle((float)n4, (float)n5, this.circleRadius * n2, this.circlePaint);
                    }
                }
            }
        }
        Label_1070: {
            if (this.overlayImageView.hasBitmapImage()) {
                this.overlayImageView.setAlpha(n2 * this.overrideAlpha);
                if (this.drawMiniIcon) {
                    final Canvas miniDrawCanvas2 = this.miniDrawCanvas;
                    if (miniDrawCanvas2 != null) {
                        this.overlayImageView.draw(miniDrawCanvas2);
                        this.miniDrawCanvas.drawCircle((float)n4, (float)n5, (float)this.circleRadius, this.overlayPaint);
                        break Label_1070;
                    }
                }
                this.overlayImageView.draw(canvas);
                canvas.drawCircle((float)n4, (float)n5, (float)this.circleRadius, this.overlayPaint);
            }
        }
        final MediaActionDrawable mediaActionDrawable5 = this.mediaActionDrawable;
        final int circleRadius2 = this.circleRadius;
        mediaActionDrawable5.setBounds(n4 - circleRadius2, n5 - circleRadius2, n4 + circleRadius2, n5 + circleRadius2);
        if (this.drawMiniIcon) {
            final Canvas miniDrawCanvas3 = this.miniDrawCanvas;
            if (miniDrawCanvas3 != null) {
                this.mediaActionDrawable.draw(miniDrawCanvas3);
            }
            else {
                this.mediaActionDrawable.draw(canvas);
            }
        }
        else {
            this.mediaActionDrawable.setOverrideAlpha(this.overrideAlpha);
            this.mediaActionDrawable.draw(canvas);
        }
        if (this.drawMiniIcon) {
            int n7;
            float n9;
            float n10;
            int n11;
            if (Math.abs(this.progressRect.width() - AndroidUtilities.dp(44.0f)) < AndroidUtilities.density) {
                n7 = 20;
                final float centerX = this.progressRect.centerX();
                final float n8 = 16;
                n9 = centerX + AndroidUtilities.dp(n8);
                n10 = this.progressRect.centerY() + AndroidUtilities.dp(n8);
                n11 = 0;
            }
            else {
                n7 = 22;
                n9 = this.progressRect.centerX() + AndroidUtilities.dp(18.0f);
                n10 = this.progressRect.centerY() + AndroidUtilities.dp(18.0f);
                n11 = n6;
            }
            final int n12 = n7 / 2;
            float n13;
            if (this.miniMediaActionDrawable.getCurrentIcon() != 4) {
                n13 = 1.0f;
            }
            else {
                n13 = 1.0f - this.miniMediaActionDrawable.getTransitionProgress();
            }
            if (n13 == 0.0f) {
                this.drawMiniIcon = false;
            }
            final Canvas miniDrawCanvas4 = this.miniDrawCanvas;
            if (miniDrawCanvas4 != null) {
                final float n14 = (float)(n7 + 18 + n11);
                miniDrawCanvas4.drawCircle((float)AndroidUtilities.dp(n14), (float)AndroidUtilities.dp(n14), AndroidUtilities.dp((float)(n12 + 1)) * n13, Theme.checkboxSquare_eraserPaint);
            }
            else {
                this.miniProgressBackgroundPaint.setColor(this.progressColor);
                canvas.drawCircle(n9, n10, (float)AndroidUtilities.dp(12.0f), this.miniProgressBackgroundPaint);
            }
            if (this.miniDrawCanvas != null) {
                final Bitmap miniDrawBitmap = this.miniDrawBitmap;
                final RectF progressRect = this.progressRect;
                canvas.drawBitmap(miniDrawBitmap, (float)(int)progressRect.left, (float)(int)progressRect.top, (Paint)null);
            }
            final float n15 = (float)n12;
            canvas.drawCircle(n9, n10, AndroidUtilities.dp(n15) * n13, this.circleMiniPaint);
            this.miniMediaActionDrawable.setBounds((int)(n9 - AndroidUtilities.dp(n15) * n13), (int)(n10 - AndroidUtilities.dp(n15) * n13), (int)(n9 + AndroidUtilities.dp(n15) * n13), (int)(n10 + AndroidUtilities.dp(n15) * n13));
            this.miniMediaActionDrawable.draw(canvas);
        }
    }
    
    public int getIcon() {
        return this.mediaActionDrawable.getCurrentIcon();
    }
    
    public int getMiniIcon() {
        return this.miniMediaActionDrawable.getCurrentIcon();
    }
    
    public RectF getProgressRect() {
        return this.progressRect;
    }
    
    public void initMiniIcons() {
        if (this.miniDrawBitmap != null) {
            return;
        }
        try {
            this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0f), AndroidUtilities.dp(48.0f), Bitmap$Config.ARGB_8888);
            this.miniDrawCanvas = new Canvas(this.miniDrawBitmap);
        }
        catch (Throwable t) {}
    }
    
    public void onAttachedToWindow() {
        this.overlayImageView.onAttachedToWindow();
    }
    
    public void onDetachedFromWindow() {
        this.overlayImageView.onDetachedFromWindow();
    }
    
    public void setBackgroundStroke(final int backgroundStroke) {
        this.backgroundStroke = backgroundStroke;
        this.circlePaint.setStrokeWidth((float)backgroundStroke);
        this.circlePaint.setStyle(Paint$Style.STROKE);
        this.invalidateParent();
    }
    
    public void setCircleRadius(final int circleRadius) {
        this.circleRadius = circleRadius;
        this.overlayImageView.setRoundRadius(this.circleRadius);
    }
    
    public void setColors(final int circleColor, final int circlePressedColor, final int iconColor, final int iconPressedColor) {
        this.circleColor = circleColor;
        this.circlePressedColor = circlePressedColor;
        this.iconColor = iconColor;
        this.iconPressedColor = iconPressedColor;
        this.circleColorKey = null;
        this.circlePressedColorKey = null;
        this.iconColorKey = null;
        this.iconPressedColorKey = null;
    }
    
    public void setColors(final String circleColorKey, final String circlePressedColorKey, final String iconColorKey, final String iconPressedColorKey) {
        this.circleColorKey = circleColorKey;
        this.circlePressedColorKey = circlePressedColorKey;
        this.iconColorKey = iconColorKey;
        this.iconPressedColorKey = iconPressedColorKey;
    }
    
    public void setDrawBackground(final boolean drawBackground) {
        this.drawBackground = drawBackground;
    }
    
    public void setIcon(final int n, final boolean b, final boolean b2) {
        if (b && n == this.mediaActionDrawable.getCurrentIcon()) {
            return;
        }
        this.mediaActionDrawable.setIcon(n, b2);
        if (!b2) {
            this.parent.invalidate();
        }
        else {
            this.invalidateParent();
        }
    }
    
    public void setImageOverlay(final String s) {
        final ImageReceiver overlayImageView = this.overlayImageView;
        String format;
        if (s != null) {
            format = String.format(Locale.US, "%d_%d", this.circleRadius * 2, this.circleRadius * 2);
        }
        else {
            format = null;
        }
        overlayImageView.setImage(s, format, null, null, -1);
    }
    
    public void setImageOverlay(final TLRPC.PhotoSize photoSize, final TLRPC.Document document, final Object o) {
        this.overlayImageView.setImage(ImageLocation.getForDocument(photoSize, document), String.format(Locale.US, "%d_%d", this.circleRadius * 2, this.circleRadius * 2), null, null, o, 1);
    }
    
    public void setMiniIcon(final int n, final boolean b, final boolean b2) {
        if (n != 2 && n != 3 && n != 4) {
            return;
        }
        if (b && n == this.miniMediaActionDrawable.getCurrentIcon()) {
            return;
        }
        this.miniMediaActionDrawable.setIcon(n, b2);
        this.drawMiniIcon = (n != 4 || this.miniMediaActionDrawable.getTransitionProgress() < 1.0f);
        if (this.drawMiniIcon) {
            this.initMiniIcons();
        }
        if (!b2) {
            this.parent.invalidate();
        }
        else {
            this.invalidateParent();
        }
    }
    
    public void setMiniProgressBackgroundColor(final int color) {
        this.miniProgressBackgroundPaint.setColor(color);
    }
    
    public void setOverrideAlpha(final float overrideAlpha) {
        this.overrideAlpha = overrideAlpha;
    }
    
    public void setPressed(final boolean b, final boolean b2) {
        if (b2) {
            this.isPressedMini = b;
        }
        else {
            this.isPressed = b;
        }
        this.invalidateParent();
    }
    
    public void setProgress(final float n, final boolean b) {
        if (this.drawMiniIcon) {
            this.miniMediaActionDrawable.setProgress(n, b);
        }
        else {
            this.mediaActionDrawable.setProgress(n, b);
        }
    }
    
    public void setProgressColor(final int progressColor) {
        this.progressColor = progressColor;
    }
    
    public void setProgressRect(final int n, final int n2, final int n3, final int n4) {
        this.progressRect.set((float)n, (float)n2, (float)n3, (float)n4);
    }
    
    public boolean swapIcon(final int n) {
        return this.mediaActionDrawable.setIcon(n, false);
    }
}
