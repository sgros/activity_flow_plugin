// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.graphics.Shader$TileMode;
import org.telegram.ui.Components.RecyclableDrawable;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.Shader;
import org.telegram.ui.Components.AnimatedFileDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.PorterDuff$Mode;
import android.graphics.Matrix;
import android.graphics.Paint;
import org.telegram.tgnet.TLRPC;
import android.view.View;
import android.graphics.BitmapShader;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuffColorFilter;

public class ImageReceiver implements NotificationCenterDelegate
{
    private static final int TYPE_CROSSFDADE = 2;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MEDIA = 3;
    public static final int TYPE_THUMB = 1;
    private static PorterDuffColorFilter selectedColorFilter;
    private static PorterDuffColorFilter selectedGroupColorFilter;
    private boolean allowDecodeSingleFrame;
    private boolean allowStartAnimation;
    private RectF bitmapRect;
    private boolean canceledLoading;
    private boolean centerRotation;
    private ColorFilter colorFilter;
    private byte crossfadeAlpha;
    private Drawable crossfadeImage;
    private String crossfadeKey;
    private BitmapShader crossfadeShader;
    private boolean crossfadeWithOldImage;
    private boolean crossfadeWithThumb;
    private boolean crossfadingWithThumb;
    private int currentAccount;
    private float currentAlpha;
    private int currentCacheType;
    private String currentExt;
    private Drawable currentImageDrawable;
    private String currentImageFilter;
    private String currentImageKey;
    private ImageLocation currentImageLocation;
    private boolean currentKeyQuality;
    private Drawable currentMediaDrawable;
    private String currentMediaFilter;
    private String currentMediaKey;
    private ImageLocation currentMediaLocation;
    private Object currentParentObject;
    private int currentSize;
    private Drawable currentThumbDrawable;
    private String currentThumbFilter;
    private String currentThumbKey;
    private ImageLocation currentThumbLocation;
    private ImageReceiverDelegate delegate;
    private RectF drawRegion;
    private boolean forceCrossfade;
    private boolean forceLoding;
    private boolean forcePreview;
    private int imageH;
    private int imageOrientation;
    private BitmapShader imageShader;
    private int imageTag;
    private int imageW;
    private int imageX;
    private int imageY;
    private boolean invalidateAll;
    private boolean isAspectFit;
    private int isPressed;
    private boolean isVisible;
    private long lastUpdateAlphaTime;
    private boolean manualAlphaAnimator;
    private BitmapShader mediaShader;
    private int mediaTag;
    private boolean needsQualityThumb;
    private float overrideAlpha;
    private int param;
    private View parentView;
    private TLRPC.Document qulityThumbDocument;
    private Paint roundPaint;
    private int roundRadius;
    private RectF roundRect;
    private SetImageBackup setImageBackup;
    private Matrix shaderMatrix;
    private boolean shouldGenerateQualityThumb;
    private float sideClip;
    private Drawable staticThumbDrawable;
    private ImageLocation strippedLocation;
    private int thumbOrientation;
    private BitmapShader thumbShader;
    private int thumbTag;
    private boolean useSharedAnimationQueue;
    
    static {
        ImageReceiver.selectedColorFilter = new PorterDuffColorFilter(-2236963, PorterDuff$Mode.MULTIPLY);
        ImageReceiver.selectedGroupColorFilter = new PorterDuffColorFilter(-4473925, PorterDuff$Mode.MULTIPLY);
    }
    
    public ImageReceiver() {
        this(null);
    }
    
    public ImageReceiver(final View parentView) {
        this.allowStartAnimation = true;
        this.drawRegion = new RectF();
        this.isVisible = true;
        this.roundRect = new RectF();
        this.bitmapRect = new RectF();
        this.shaderMatrix = new Matrix();
        this.overrideAlpha = 1.0f;
        this.crossfadeAlpha = 1;
        this.parentView = parentView;
        this.roundPaint = new Paint(3);
        this.currentAccount = UserConfig.selectedAccount;
    }
    
    private void checkAlphaAnimation(final boolean b) {
        if (this.manualAlphaAnimator) {
            return;
        }
        if (this.currentAlpha != 1.0f) {
            if (!b) {
                long n;
                if ((n = System.currentTimeMillis() - this.lastUpdateAlphaTime) > 18L) {
                    n = 18L;
                }
                this.currentAlpha += n / 150.0f;
                if (this.currentAlpha > 1.0f) {
                    this.currentAlpha = 1.0f;
                    if (this.crossfadeImage != null) {
                        this.recycleBitmap(null, 2);
                        this.crossfadeShader = null;
                    }
                }
            }
            this.lastUpdateAlphaTime = System.currentTimeMillis();
            final View parentView = this.parentView;
            if (parentView != null) {
                if (this.invalidateAll) {
                    parentView.invalidate();
                }
                else {
                    final int imageX = this.imageX;
                    final int imageY = this.imageY;
                    parentView.invalidate(imageX, imageY, this.imageW + imageX, this.imageH + imageY);
                }
            }
        }
    }
    
    private void drawDrawable(final Canvas canvas, final Drawable drawable, int n, final BitmapShader bitmapShader, int n2) {
        final boolean b = drawable instanceof BitmapDrawable;
        float min = 1.0f;
        if (b) {
            final BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            Paint paint;
            if (bitmapShader != null) {
                paint = this.roundPaint;
            }
            else {
                paint = bitmapDrawable.getPaint();
            }
            final boolean b2 = paint != null && paint.getColorFilter() != null;
            if (b2 && this.isPressed == 0) {
                if (bitmapShader != null) {
                    this.roundPaint.setColorFilter((ColorFilter)null);
                }
                else if (this.staticThumbDrawable != drawable) {
                    bitmapDrawable.setColorFilter((ColorFilter)null);
                }
            }
            else if (!b2) {
                final int isPressed = this.isPressed;
                if (isPressed != 0) {
                    if (isPressed == 1) {
                        if (bitmapShader != null) {
                            this.roundPaint.setColorFilter((ColorFilter)ImageReceiver.selectedColorFilter);
                        }
                        else {
                            bitmapDrawable.setColorFilter((ColorFilter)ImageReceiver.selectedColorFilter);
                        }
                    }
                    else if (bitmapShader != null) {
                        this.roundPaint.setColorFilter((ColorFilter)ImageReceiver.selectedGroupColorFilter);
                    }
                    else {
                        bitmapDrawable.setColorFilter((ColorFilter)ImageReceiver.selectedGroupColorFilter);
                    }
                }
            }
            final ColorFilter colorFilter = this.colorFilter;
            if (colorFilter != null) {
                if (bitmapShader != null) {
                    this.roundPaint.setColorFilter(colorFilter);
                }
                else {
                    bitmapDrawable.setColorFilter(colorFilter);
                }
            }
            final boolean b3 = bitmapDrawable instanceof AnimatedFileDrawable;
            int n4;
            int n5;
            if (b3) {
                final int n3 = n2 % 360;
                if (n3 != 90 && n3 != 270) {
                    n4 = bitmapDrawable.getIntrinsicWidth();
                    n5 = bitmapDrawable.getIntrinsicHeight();
                }
                else {
                    n4 = bitmapDrawable.getIntrinsicHeight();
                    n5 = bitmapDrawable.getIntrinsicWidth();
                }
            }
            else {
                final int n6 = n2 % 360;
                if (n6 != 90 && n6 != 270) {
                    n4 = bitmapDrawable.getBitmap().getWidth();
                    n5 = bitmapDrawable.getBitmap().getHeight();
                }
                else {
                    n4 = bitmapDrawable.getBitmap().getHeight();
                    n5 = bitmapDrawable.getBitmap().getWidth();
                }
            }
            final int imageW = this.imageW;
            final float n7 = (float)imageW;
            final float sideClip = this.sideClip;
            final float n8 = n7 - sideClip * 2.0f;
            final float n9 = this.imageH - sideClip * 2.0f;
            float a;
            if (imageW == 0) {
                a = 1.0f;
            }
            else {
                a = n4 / n8;
            }
            float b4;
            if (this.imageH == 0) {
                b4 = 1.0f;
            }
            else {
                b4 = n5 / n9;
            }
            if (bitmapShader != null) {
                if (this.isAspectFit) {
                    final float max = Math.max(a, b4);
                    n2 = (int)(n4 / max);
                    final int n10 = (int)(n5 / max);
                    final RectF drawRegion = this.drawRegion;
                    final int imageX = this.imageX;
                    final int imageW2 = this.imageW;
                    final float n11 = (float)((imageW2 - n2) / 2 + imageX);
                    final int imageY = this.imageY;
                    final int imageH = this.imageH;
                    drawRegion.set(n11, (float)((imageH - n10) / 2 + imageY), (float)(imageX + (imageW2 + n2) / 2), (float)(imageY + (imageH + n10) / 2));
                    if (this.isVisible) {
                        this.roundPaint.setShader((Shader)bitmapShader);
                        this.shaderMatrix.reset();
                        final Matrix shaderMatrix = this.shaderMatrix;
                        final RectF drawRegion2 = this.drawRegion;
                        shaderMatrix.setTranslate(drawRegion2.left, drawRegion2.top);
                        final Matrix shaderMatrix2 = this.shaderMatrix;
                        final float n12 = 1.0f / max;
                        shaderMatrix2.preScale(n12, n12);
                        bitmapShader.setLocalMatrix(this.shaderMatrix);
                        this.roundPaint.setAlpha(n);
                        this.roundRect.set(this.drawRegion);
                        final RectF roundRect = this.roundRect;
                        n = this.roundRadius;
                        canvas.drawRoundRect(roundRect, (float)n, (float)n, this.roundPaint);
                    }
                }
                else {
                    this.roundPaint.setShader((Shader)bitmapShader);
                    final float n13 = 1.0f / Math.min(a, b4);
                    final RectF roundRect2 = this.roundRect;
                    final int imageX2 = this.imageX;
                    final float n14 = (float)imageX2;
                    final float sideClip2 = this.sideClip;
                    final int imageY2 = this.imageY;
                    roundRect2.set(n14 + sideClip2, imageY2 + sideClip2, imageX2 + this.imageW - sideClip2, imageY2 + this.imageH - sideClip2);
                    this.shaderMatrix.reset();
                    if (Math.abs(a - b4) > 5.0E-4f) {
                        final float n15 = n4 / b4;
                        if (n15 > n8) {
                            final int n16 = (int)n15;
                            final RectF drawRegion3 = this.drawRegion;
                            final int imageX3 = this.imageX;
                            final float n17 = (float)imageX3;
                            final float n18 = (float)n16;
                            final float n19 = (n18 - n8) / 2.0f;
                            final int imageY3 = this.imageY;
                            drawRegion3.set(n17 - n19, (float)imageY3, imageX3 + (n18 + n8) / 2.0f, imageY3 + n9);
                        }
                        else {
                            final int n20 = (int)(n5 / a);
                            final RectF drawRegion4 = this.drawRegion;
                            final int imageX4 = this.imageX;
                            final float n21 = (float)imageX4;
                            final int imageY4 = this.imageY;
                            final float n22 = (float)imageY4;
                            final float n23 = (float)n20;
                            drawRegion4.set(n21, n22 - (n23 - n9) / 2.0f, imageX4 + n8, imageY4 + (n23 + n9) / 2.0f);
                        }
                    }
                    else {
                        final RectF drawRegion5 = this.drawRegion;
                        final int imageX5 = this.imageX;
                        final float n24 = (float)imageX5;
                        final int imageY5 = this.imageY;
                        drawRegion5.set(n24, (float)imageY5, imageX5 + n8, imageY5 + n9);
                    }
                    if (this.isVisible) {
                        this.shaderMatrix.reset();
                        final Matrix shaderMatrix3 = this.shaderMatrix;
                        final RectF drawRegion6 = this.drawRegion;
                        final float left = drawRegion6.left;
                        final float sideClip3 = this.sideClip;
                        shaderMatrix3.setTranslate(left + sideClip3, drawRegion6.top + sideClip3);
                        if (n2 == 90) {
                            this.shaderMatrix.preRotate(90.0f);
                            this.shaderMatrix.preTranslate(0.0f, -this.drawRegion.width());
                        }
                        else if (n2 == 180) {
                            this.shaderMatrix.preRotate(180.0f);
                            this.shaderMatrix.preTranslate(-this.drawRegion.width(), -this.drawRegion.height());
                        }
                        else if (n2 == 270) {
                            this.shaderMatrix.preRotate(270.0f);
                            this.shaderMatrix.preTranslate(-this.drawRegion.height(), 0.0f);
                        }
                        this.shaderMatrix.preScale(n13, n13);
                        bitmapShader.setLocalMatrix(this.shaderMatrix);
                        this.roundPaint.setAlpha(n);
                        final RectF roundRect3 = this.roundRect;
                        n = this.roundRadius;
                        canvas.drawRoundRect(roundRect3, (float)n, (float)n, this.roundPaint);
                    }
                }
            }
            else if (this.isAspectFit) {
                final float max2 = Math.max(a, b4);
                canvas.save();
                n2 = (int)(n4 / max2);
                final int n25 = (int)(n5 / max2);
                final RectF drawRegion7 = this.drawRegion;
                final int imageX6 = this.imageX;
                final float n26 = (float)imageX6;
                final int imageW3 = this.imageW;
                final float n27 = (imageW3 - n2) / 2.0f;
                final int imageY6 = this.imageY;
                final float n28 = (float)imageY6;
                final int imageH2 = this.imageH;
                drawRegion7.set(n26 + n27, n28 + (imageH2 - n25) / 2.0f, imageX6 + (imageW3 + n2) / 2.0f, imageY6 + (imageH2 + n25) / 2.0f);
                final RectF drawRegion8 = this.drawRegion;
                bitmapDrawable.setBounds((int)drawRegion8.left, (int)drawRegion8.top, (int)drawRegion8.right, (int)drawRegion8.bottom);
                if (b3) {
                    final AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable)bitmapDrawable;
                    final RectF drawRegion9 = this.drawRegion;
                    animatedFileDrawable.setActualDrawRect(drawRegion9.left, drawRegion9.top, drawRegion9.width(), this.drawRegion.height());
                }
                if (this.isVisible) {
                    try {
                        bitmapDrawable.setAlpha(n);
                        bitmapDrawable.draw(canvas);
                    }
                    catch (Exception ex) {
                        this.onBitmapException((Drawable)bitmapDrawable);
                        FileLog.e(ex);
                    }
                }
                canvas.restore();
            }
            else if (Math.abs(a - b4) > 1.0E-5f) {
                canvas.save();
                final int imageX7 = this.imageX;
                final int imageY7 = this.imageY;
                canvas.clipRect(imageX7, imageY7, this.imageW + imageX7, this.imageH + imageY7);
                final int n29 = n2 % 360;
                if (n29 != 0) {
                    if (this.centerRotation) {
                        canvas.rotate((float)n2, (float)(this.imageW / 2), (float)(this.imageH / 2));
                    }
                    else {
                        canvas.rotate((float)n2, 0.0f, 0.0f);
                    }
                }
                final float n30 = n4 / b4;
                n2 = this.imageW;
                if (n30 > n2) {
                    final int n31 = (int)n30;
                    final RectF drawRegion10 = this.drawRegion;
                    final int imageX8 = this.imageX;
                    final float n32 = (float)imageX8;
                    final float n33 = (n31 - n2) / 2.0f;
                    final int imageY8 = this.imageY;
                    drawRegion10.set(n32 - n33, (float)imageY8, imageX8 + (n31 + n2) / 2.0f, (float)(imageY8 + this.imageH));
                }
                else {
                    final int n34 = (int)(n5 / a);
                    final RectF drawRegion11 = this.drawRegion;
                    final int imageX9 = this.imageX;
                    final float n35 = (float)imageX9;
                    final int imageY9 = this.imageY;
                    final float n36 = (float)imageY9;
                    final int imageH3 = this.imageH;
                    drawRegion11.set(n35, n36 - (n34 - imageH3) / 2.0f, (float)(imageX9 + n2), imageY9 + (n34 + imageH3) / 2.0f);
                }
                if (b3) {
                    ((AnimatedFileDrawable)bitmapDrawable).setActualDrawRect((float)this.imageX, (float)this.imageY, (float)this.imageW, (float)this.imageH);
                }
                if (n29 != 90 && n29 != 270) {
                    final RectF drawRegion12 = this.drawRegion;
                    bitmapDrawable.setBounds((int)drawRegion12.left, (int)drawRegion12.top, (int)drawRegion12.right, (int)drawRegion12.bottom);
                }
                else {
                    final float n37 = this.drawRegion.width() / 2.0f;
                    final float n38 = this.drawRegion.height() / 2.0f;
                    final float centerX = this.drawRegion.centerX();
                    final float centerY = this.drawRegion.centerY();
                    bitmapDrawable.setBounds((int)(centerX - n38), (int)(centerY - n37), (int)(centerX + n38), (int)(centerY + n37));
                }
                if (this.isVisible) {
                    try {
                        bitmapDrawable.setAlpha(n);
                        bitmapDrawable.draw(canvas);
                    }
                    catch (Exception ex2) {
                        this.onBitmapException((Drawable)bitmapDrawable);
                        FileLog.e(ex2);
                    }
                }
                canvas.restore();
            }
            else {
                canvas.save();
                final int n39 = n2 % 360;
                if (n39 != 0) {
                    if (this.centerRotation) {
                        canvas.rotate((float)n2, (float)(this.imageW / 2), (float)(this.imageH / 2));
                    }
                    else {
                        canvas.rotate((float)n2, 0.0f, 0.0f);
                    }
                }
                final RectF drawRegion13 = this.drawRegion;
                n2 = this.imageX;
                final float n40 = (float)n2;
                final int imageY10 = this.imageY;
                drawRegion13.set(n40, (float)imageY10, (float)(n2 + this.imageW), (float)(imageY10 + this.imageH));
                if (b3) {
                    ((AnimatedFileDrawable)bitmapDrawable).setActualDrawRect((float)this.imageX, (float)this.imageY, (float)this.imageW, (float)this.imageH);
                }
                if (n39 != 90 && n39 != 270) {
                    final RectF drawRegion14 = this.drawRegion;
                    bitmapDrawable.setBounds((int)drawRegion14.left, (int)drawRegion14.top, (int)drawRegion14.right, (int)drawRegion14.bottom);
                }
                else {
                    final float n41 = this.drawRegion.width() / 2.0f;
                    final float n42 = this.drawRegion.height() / 2.0f;
                    final float centerX2 = this.drawRegion.centerX();
                    final float centerY2 = this.drawRegion.centerY();
                    bitmapDrawable.setBounds((int)(centerX2 - n42), (int)(centerY2 - n41), (int)(centerX2 + n42), (int)(centerY2 + n41));
                }
                if (this.isVisible) {
                    try {
                        bitmapDrawable.setAlpha(n);
                        bitmapDrawable.draw(canvas);
                    }
                    catch (Exception ex3) {
                        this.onBitmapException((Drawable)bitmapDrawable);
                        FileLog.e(ex3);
                    }
                }
                canvas.restore();
            }
        }
        else {
            final RectF drawRegion15 = this.drawRegion;
            n2 = this.imageX;
            final float n43 = (float)n2;
            final int imageY11 = this.imageY;
            drawRegion15.set(n43, (float)imageY11, (float)(n2 + this.imageW), (float)(imageY11 + this.imageH));
            final RectF drawRegion16 = this.drawRegion;
            drawable.setBounds((int)drawRegion16.left, (int)drawRegion16.top, (int)drawRegion16.right, (int)drawRegion16.bottom);
            if (this.isVisible) {
                final boolean b5 = drawable instanceof LottieDrawable;
                if (b5) {
                    canvas.save();
                    n2 = this.imageX;
                    n2 = this.imageY;
                    final int bitmapWidth = this.getBitmapWidth();
                    final int bitmapHeight = this.getBitmapHeight();
                    int n44 = 0;
                    Label_2465: {
                        if (bitmapWidth <= this.imageW) {
                            n44 = bitmapWidth;
                            if ((n2 = bitmapHeight) <= this.imageH) {
                                break Label_2465;
                            }
                        }
                        final float n45 = (float)this.imageW;
                        final float n46 = (float)bitmapWidth;
                        final float a2 = n45 / n46;
                        final float n47 = (float)this.imageH;
                        final float n48 = (float)bitmapHeight;
                        min = Math.min(a2, n47 / n48);
                        n44 = (int)(n46 * min);
                        n2 = (int)(n48 * min);
                        canvas.scale(min, min);
                    }
                    canvas.translate((this.imageX + (this.imageW - n44) / 2) / min, (this.imageY + (this.imageH - n2) / 2) / min);
                    final View parentView = this.parentView;
                    if (parentView != null) {
                        if (this.invalidateAll) {
                            parentView.invalidate();
                        }
                        else {
                            final int imageX10 = this.imageX;
                            n2 = this.imageY;
                            parentView.invalidate(imageX10, n2, this.imageW + imageX10, this.imageH + n2);
                        }
                    }
                }
                try {
                    drawable.setAlpha(n);
                    drawable.draw(canvas);
                }
                catch (Exception ex4) {
                    FileLog.e(ex4);
                }
                if (b5) {
                    canvas.restore();
                }
            }
        }
    }
    
    private void onBitmapException(final Drawable drawable) {
        if (drawable == this.currentMediaDrawable && this.currentMediaKey != null) {
            ImageLoader.getInstance().removeImage(this.currentMediaKey);
            this.currentMediaKey = null;
        }
        else if (drawable == this.currentImageDrawable && this.currentImageKey != null) {
            ImageLoader.getInstance().removeImage(this.currentImageKey);
            this.currentImageKey = null;
        }
        else if (drawable == this.currentThumbDrawable && this.currentThumbKey != null) {
            ImageLoader.getInstance().removeImage(this.currentThumbKey);
            this.currentThumbKey = null;
        }
        this.setImage(this.currentMediaLocation, this.currentMediaFilter, this.currentImageLocation, this.currentImageFilter, this.currentThumbLocation, this.currentThumbFilter, this.currentThumbDrawable, this.currentSize, this.currentExt, this.currentParentObject, this.currentCacheType);
    }
    
    private void recycleBitmap(final String s, final int n) {
        String s2;
        Drawable drawable;
        if (n == 3) {
            s2 = this.currentMediaKey;
            drawable = this.currentMediaDrawable;
        }
        else if (n == 2) {
            s2 = this.crossfadeKey;
            drawable = this.crossfadeImage;
        }
        else if (n == 1) {
            s2 = this.currentThumbKey;
            drawable = this.currentThumbDrawable;
        }
        else {
            s2 = this.currentImageKey;
            drawable = this.currentImageDrawable;
        }
        String anObject = s2;
        if (s2 != null) {
            anObject = s2;
            if (s2.startsWith("-")) {
                final String replacedKey = ImageLoader.getInstance().getReplacedKey(s2);
                anObject = s2;
                if (replacedKey != null) {
                    anObject = replacedKey;
                }
            }
        }
        ImageLoader.getInstance().getReplacedKey(anObject);
        if (anObject != null && (s == null || !s.equals(anObject)) && drawable != null) {
            if (drawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable)drawable).recycle();
            }
            else if (drawable instanceof BitmapDrawable) {
                final Bitmap bitmap = ((AnimatedFileDrawable)drawable).getBitmap();
                final boolean decrementUseCount = ImageLoader.getInstance().decrementUseCount(anObject);
                if (!ImageLoader.getInstance().isInCache(anObject) && decrementUseCount) {
                    bitmap.recycle();
                }
            }
        }
        if (n == 3) {
            this.currentMediaKey = null;
            this.currentMediaDrawable = null;
        }
        else if (n == 2) {
            this.crossfadeKey = null;
            this.crossfadeImage = null;
        }
        else if (n == 1) {
            this.currentThumbDrawable = null;
            this.currentThumbKey = null;
        }
        else {
            this.currentImageDrawable = null;
            this.currentImageKey = null;
        }
    }
    
    public boolean canInvertBitmap() {
        return this.currentMediaDrawable instanceof ExtendedBitmapDrawable || this.currentImageDrawable instanceof ExtendedBitmapDrawable || this.currentThumbDrawable instanceof ExtendedBitmapDrawable || this.staticThumbDrawable instanceof ExtendedBitmapDrawable;
    }
    
    public void cancelLoadImage() {
        this.forceLoding = false;
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
        this.canceledLoading = true;
    }
    
    public void clearImage() {
        for (int i = 0; i < 4; ++i) {
            this.recycleBitmap(null, i);
        }
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.didReplacedPhotoInMemCache) {
            final String anObject = (String)array[0];
            final String currentMediaKey = this.currentMediaKey;
            if (currentMediaKey != null && currentMediaKey.equals(anObject)) {
                this.currentMediaKey = (String)array[1];
                this.currentMediaLocation = (ImageLocation)array[2];
                final SetImageBackup setImageBackup = this.setImageBackup;
                if (setImageBackup != null) {
                    setImageBackup.mediaLocation = (ImageLocation)array[2];
                }
            }
            final String currentImageKey = this.currentImageKey;
            if (currentImageKey != null && currentImageKey.equals(anObject)) {
                this.currentImageKey = (String)array[1];
                this.currentImageLocation = (ImageLocation)array[2];
                final SetImageBackup setImageBackup2 = this.setImageBackup;
                if (setImageBackup2 != null) {
                    setImageBackup2.imageLocation = (ImageLocation)array[2];
                }
            }
            final String currentThumbKey = this.currentThumbKey;
            if (currentThumbKey != null && currentThumbKey.equals(anObject)) {
                this.currentThumbKey = (String)array[1];
                this.currentThumbLocation = (ImageLocation)array[2];
                final SetImageBackup setImageBackup3 = this.setImageBackup;
                if (setImageBackup3 != null) {
                    setImageBackup3.thumbLocation = (ImageLocation)array[2];
                }
            }
        }
    }
    
    public boolean draw(final Canvas canvas) {
        try {
            final AnimatedFileDrawable animation = this.getAnimation();
            boolean b = animation != null && !animation.hasBitmap();
            int n = 0;
            Object currentImageDrawable = null;
            BitmapShader imageShader = null;
            Label_0217: {
                Drawable drawable;
                BitmapShader bitmapShader;
                if (!this.forcePreview && this.currentMediaDrawable != null && !b) {
                    drawable = this.currentMediaDrawable;
                    bitmapShader = this.mediaShader;
                    n = this.imageOrientation;
                }
                else {
                    if (!this.forcePreview && this.currentImageDrawable != null && (!b || this.currentMediaDrawable != null)) {
                        currentImageDrawable = this.currentImageDrawable;
                        imageShader = this.imageShader;
                        n = this.imageOrientation;
                        b = false;
                        break Label_0217;
                    }
                    if (this.crossfadeImage != null && !this.crossfadingWithThumb) {
                        drawable = this.crossfadeImage;
                        bitmapShader = this.crossfadeShader;
                        n = this.imageOrientation;
                    }
                    else if (this.staticThumbDrawable instanceof BitmapDrawable) {
                        drawable = this.staticThumbDrawable;
                        bitmapShader = this.thumbShader;
                        n = this.thumbOrientation;
                    }
                    else {
                        if (this.currentThumbDrawable == null) {
                            currentImageDrawable = (imageShader = null);
                            n = 0;
                            break Label_0217;
                        }
                        drawable = this.currentThumbDrawable;
                        bitmapShader = this.thumbShader;
                        n = this.thumbOrientation;
                    }
                }
                currentImageDrawable = drawable;
                imageShader = bitmapShader;
            }
            if (currentImageDrawable != null) {
                if (this.crossfadeAlpha != 0) {
                    if (this.crossfadeWithThumb && b) {
                        this.drawDrawable(canvas, (Drawable)currentImageDrawable, (int)(this.overrideAlpha * 255.0f), imageShader, n);
                    }
                    else {
                        if (this.crossfadeWithThumb && this.currentAlpha != 1.0f) {
                            Object o = null;
                            BitmapShader bitmapShader2 = null;
                            Label_0440: {
                                Label_0435: {
                                    if (currentImageDrawable != this.currentImageDrawable && currentImageDrawable != this.currentMediaDrawable) {
                                        if (currentImageDrawable != this.currentThumbDrawable && currentImageDrawable != this.crossfadeImage) {
                                            if (currentImageDrawable != this.staticThumbDrawable || this.crossfadeImage == null) {
                                                break Label_0435;
                                            }
                                            o = this.crossfadeImage;
                                            bitmapShader2 = this.crossfadeShader;
                                        }
                                        else {
                                            if (this.staticThumbDrawable == null) {
                                                break Label_0435;
                                            }
                                            o = this.staticThumbDrawable;
                                            bitmapShader2 = this.thumbShader;
                                        }
                                    }
                                    else if (this.crossfadeImage != null) {
                                        o = this.crossfadeImage;
                                        bitmapShader2 = this.crossfadeShader;
                                    }
                                    else if (this.currentThumbDrawable != null) {
                                        o = this.currentThumbDrawable;
                                        bitmapShader2 = this.thumbShader;
                                    }
                                    else {
                                        if (this.staticThumbDrawable == null) {
                                            break Label_0435;
                                        }
                                        o = this.staticThumbDrawable;
                                        bitmapShader2 = this.thumbShader;
                                    }
                                    break Label_0440;
                                }
                                o = (bitmapShader2 = null);
                            }
                            if (o != null) {
                                this.drawDrawable(canvas, (Drawable)o, (int)(this.overrideAlpha * 255.0f), bitmapShader2, this.thumbOrientation);
                            }
                        }
                        this.drawDrawable(canvas, (Drawable)currentImageDrawable, (int)(this.overrideAlpha * this.currentAlpha * 255.0f), imageShader, n);
                    }
                }
                else {
                    this.drawDrawable(canvas, (Drawable)currentImageDrawable, (int)(this.overrideAlpha * 255.0f), imageShader, n);
                }
                this.checkAlphaAnimation(b && this.crossfadeWithThumb);
                return true;
            }
            if (this.staticThumbDrawable != null) {
                this.drawDrawable(canvas, this.staticThumbDrawable, (int)(this.overrideAlpha * 255.0f), null, this.thumbOrientation);
                this.checkAlphaAnimation(b);
                return true;
            }
            this.checkAlphaAnimation(b);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return false;
    }
    
    public int getAnimatedOrientation() {
        final AnimatedFileDrawable animation = this.getAnimation();
        int orientation;
        if (animation != null) {
            orientation = animation.getOrientation();
        }
        else {
            orientation = 0;
        }
        return orientation;
    }
    
    public AnimatedFileDrawable getAnimation() {
        final Drawable currentMediaDrawable = this.currentMediaDrawable;
        if (currentMediaDrawable instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable)currentMediaDrawable;
        }
        final Drawable currentImageDrawable = this.currentImageDrawable;
        if (currentImageDrawable instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable)currentImageDrawable;
        }
        final Drawable currentThumbDrawable = this.currentThumbDrawable;
        if (currentThumbDrawable instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable)currentThumbDrawable;
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable)staticThumbDrawable;
        }
        return null;
    }
    
    public Bitmap getBitmap() {
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null && animation.hasBitmap()) {
            return animation.getAnimatedBitmap();
        }
        final Drawable currentMediaDrawable = this.currentMediaDrawable;
        if (currentMediaDrawable instanceof BitmapDrawable && !(currentMediaDrawable instanceof AnimatedFileDrawable)) {
            return ((BitmapDrawable)currentMediaDrawable).getBitmap();
        }
        final Drawable currentImageDrawable = this.currentImageDrawable;
        if (currentImageDrawable instanceof BitmapDrawable && !(currentImageDrawable instanceof AnimatedFileDrawable)) {
            return ((BitmapDrawable)currentImageDrawable).getBitmap();
        }
        final Drawable currentThumbDrawable = this.currentThumbDrawable;
        if (currentThumbDrawable instanceof BitmapDrawable && !(currentThumbDrawable instanceof AnimatedFileDrawable)) {
            return ((BitmapDrawable)currentThumbDrawable).getBitmap();
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)staticThumbDrawable).getBitmap();
        }
        return null;
    }
    
    public int getBitmapHeight() {
        final Drawable drawable = this.getDrawable();
        if (drawable instanceof LottieDrawable) {
            return drawable.getIntrinsicHeight();
        }
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null) {
            final int imageOrientation = this.imageOrientation;
            int n;
            if (imageOrientation % 360 != 0 && imageOrientation % 360 != 180) {
                n = animation.getIntrinsicWidth();
            }
            else {
                n = animation.getIntrinsicHeight();
            }
            return n;
        }
        final Bitmap bitmap = this.getBitmap();
        if (bitmap != null) {
            final int imageOrientation2 = this.imageOrientation;
            int n2;
            if (imageOrientation2 % 360 != 0 && imageOrientation2 % 360 != 180) {
                n2 = bitmap.getWidth();
            }
            else {
                n2 = bitmap.getHeight();
            }
            return n2;
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable != null) {
            return staticThumbDrawable.getIntrinsicHeight();
        }
        return 1;
    }
    
    public BitmapHolder getBitmapSafe() {
        final AnimatedFileDrawable animation = this.getAnimation();
        BitmapHolder bitmapHolder = null;
        Object o = null;
        String s = null;
        Label_0160: {
            if (animation != null && animation.hasBitmap()) {
                o = animation.getAnimatedBitmap();
            }
            else {
                final Drawable currentMediaDrawable = this.currentMediaDrawable;
                if (currentMediaDrawable instanceof BitmapDrawable && !(currentMediaDrawable instanceof AnimatedFileDrawable)) {
                    o = ((BitmapDrawable)currentMediaDrawable).getBitmap();
                    s = this.currentMediaKey;
                    break Label_0160;
                }
                final Drawable currentImageDrawable = this.currentImageDrawable;
                if (currentImageDrawable instanceof BitmapDrawable && !(currentImageDrawable instanceof AnimatedFileDrawable)) {
                    o = ((BitmapDrawable)currentImageDrawable).getBitmap();
                    s = this.currentImageKey;
                    break Label_0160;
                }
                final Drawable currentThumbDrawable = this.currentThumbDrawable;
                if (currentThumbDrawable instanceof BitmapDrawable && !(currentThumbDrawable instanceof AnimatedFileDrawable)) {
                    o = ((BitmapDrawable)currentThumbDrawable).getBitmap();
                    s = this.currentThumbKey;
                    break Label_0160;
                }
                final Drawable staticThumbDrawable = this.staticThumbDrawable;
                if (!(staticThumbDrawable instanceof BitmapDrawable)) {
                    o = (s = null);
                    break Label_0160;
                }
                o = ((BitmapDrawable)staticThumbDrawable).getBitmap();
            }
            s = null;
        }
        if (o != null) {
            bitmapHolder = new BitmapHolder((Bitmap)o, s);
        }
        return bitmapHolder;
    }
    
    public int getBitmapWidth() {
        final Drawable drawable = this.getDrawable();
        if (drawable instanceof LottieDrawable) {
            return drawable.getIntrinsicWidth();
        }
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null) {
            final int imageOrientation = this.imageOrientation;
            int n;
            if (imageOrientation % 360 != 0 && imageOrientation % 360 != 180) {
                n = animation.getIntrinsicHeight();
            }
            else {
                n = animation.getIntrinsicWidth();
            }
            return n;
        }
        final Bitmap bitmap = this.getBitmap();
        if (bitmap != null) {
            final int imageOrientation2 = this.imageOrientation;
            int n2;
            if (imageOrientation2 % 360 != 0 && imageOrientation2 % 360 != 180) {
                n2 = bitmap.getHeight();
            }
            else {
                n2 = bitmap.getWidth();
            }
            return n2;
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable != null) {
            return staticThumbDrawable.getIntrinsicWidth();
        }
        return 1;
    }
    
    public int getCacheType() {
        return this.currentCacheType;
    }
    
    public float getCenterX() {
        return this.imageX + this.imageW / 2.0f;
    }
    
    public float getCenterY() {
        return this.imageY + this.imageH / 2.0f;
    }
    
    public int getCurrentAccount() {
        return this.currentAccount;
    }
    
    public float getCurrentAlpha() {
        return this.currentAlpha;
    }
    
    public RectF getDrawRegion() {
        return this.drawRegion;
    }
    
    public Drawable getDrawable() {
        final Drawable currentMediaDrawable = this.currentMediaDrawable;
        if (currentMediaDrawable != null) {
            return currentMediaDrawable;
        }
        final Drawable currentImageDrawable = this.currentImageDrawable;
        if (currentImageDrawable != null) {
            return currentImageDrawable;
        }
        final Drawable currentThumbDrawable = this.currentThumbDrawable;
        if (currentThumbDrawable != null) {
            return currentThumbDrawable;
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable != null) {
            return staticThumbDrawable;
        }
        return null;
    }
    
    public String getExt() {
        return this.currentExt;
    }
    
    public float getImageAspectRatio() {
        float n;
        float n2;
        if (this.imageOrientation % 180 != 0) {
            n = this.drawRegion.height();
            n2 = this.drawRegion.width();
        }
        else {
            n = this.drawRegion.width();
            n2 = this.drawRegion.height();
        }
        return n / n2;
    }
    
    public String getImageFilter() {
        return this.currentImageFilter;
    }
    
    public int getImageHeight() {
        return this.imageH;
    }
    
    public String getImageKey() {
        return this.currentImageKey;
    }
    
    public ImageLocation getImageLocation() {
        return this.currentImageLocation;
    }
    
    public int getImageWidth() {
        return this.imageW;
    }
    
    public int getImageX() {
        return this.imageX;
    }
    
    public int getImageX2() {
        return this.imageX + this.imageW;
    }
    
    public int getImageY() {
        return this.imageY;
    }
    
    public int getImageY2() {
        return this.imageY + this.imageH;
    }
    
    public String getMediaFilter() {
        return this.currentMediaFilter;
    }
    
    public String getMediaKey() {
        return this.currentMediaKey;
    }
    
    public ImageLocation getMediaLocation() {
        return this.currentMediaLocation;
    }
    
    public int getOrientation() {
        return this.imageOrientation;
    }
    
    public int getParam() {
        return this.param;
    }
    
    public Object getParentObject() {
        return this.currentParentObject;
    }
    
    public boolean getPressed() {
        return this.isPressed != 0;
    }
    
    public TLRPC.Document getQulityThumbDocument() {
        return this.qulityThumbDocument;
    }
    
    public int getRoundRadius() {
        return this.roundRadius;
    }
    
    public int getSize() {
        return this.currentSize;
    }
    
    public Drawable getStaticThumb() {
        return this.staticThumbDrawable;
    }
    
    public ImageLocation getStrippedLocation() {
        return this.strippedLocation;
    }
    
    protected int getTag(final int n) {
        if (n == 1) {
            return this.thumbTag;
        }
        if (n == 3) {
            return this.mediaTag;
        }
        return this.imageTag;
    }
    
    public Bitmap getThumbBitmap() {
        final Drawable currentThumbDrawable = this.currentThumbDrawable;
        if (currentThumbDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)currentThumbDrawable).getBitmap();
        }
        final Drawable staticThumbDrawable = this.staticThumbDrawable;
        if (staticThumbDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)staticThumbDrawable).getBitmap();
        }
        return null;
    }
    
    public BitmapHolder getThumbBitmapSafe() {
        final Drawable currentThumbDrawable = this.currentThumbDrawable;
        final boolean b = currentThumbDrawable instanceof BitmapDrawable;
        BitmapHolder bitmapHolder = null;
        Object o;
        String currentThumbKey;
        if (b) {
            o = ((BitmapDrawable)currentThumbDrawable).getBitmap();
            currentThumbKey = this.currentThumbKey;
        }
        else {
            final Drawable staticThumbDrawable = this.staticThumbDrawable;
            if (staticThumbDrawable instanceof BitmapDrawable) {
                o = ((BitmapDrawable)staticThumbDrawable).getBitmap();
                currentThumbKey = null;
            }
            else {
                o = (currentThumbKey = null);
            }
        }
        if (o != null) {
            bitmapHolder = new BitmapHolder((Bitmap)o, currentThumbKey);
        }
        return bitmapHolder;
    }
    
    public String getThumbFilter() {
        return this.currentThumbFilter;
    }
    
    public String getThumbKey() {
        return this.currentThumbKey;
    }
    
    public ImageLocation getThumbLocation() {
        return this.currentThumbLocation;
    }
    
    public boolean getVisible() {
        return this.isVisible;
    }
    
    public boolean hasBitmapImage() {
        return this.currentImageDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentMediaDrawable != null;
    }
    
    public boolean hasImageSet() {
        return this.currentImageDrawable != null || this.currentMediaDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentImageKey != null || this.currentMediaKey != null;
    }
    
    public boolean hasNotThumb() {
        return this.currentImageDrawable != null || this.currentMediaDrawable != null;
    }
    
    public boolean hasStaticThumb() {
        return this.staticThumbDrawable != null;
    }
    
    public boolean isAllowStartAnimation() {
        return this.allowStartAnimation;
    }
    
    public boolean isAnimationRunning() {
        final AnimatedFileDrawable animation = this.getAnimation();
        return animation != null && animation.isRunning();
    }
    
    public boolean isAspectFit() {
        return this.isAspectFit;
    }
    
    public boolean isCurrentKeyQuality() {
        return this.currentKeyQuality;
    }
    
    public boolean isForceLoding() {
        return this.forceLoding;
    }
    
    public boolean isForcePreview() {
        return this.forcePreview;
    }
    
    public boolean isInsideImage(final float n, final float n2) {
        final int imageX = this.imageX;
        if (n >= imageX && n <= imageX + this.imageW) {
            final int imageY = this.imageY;
            if (n2 >= imageY && n2 <= imageY + this.imageH) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isNeedsQualityThumb() {
        return this.needsQualityThumb;
    }
    
    public boolean isShouldGenerateQualityThumb() {
        return this.shouldGenerateQualityThumb;
    }
    
    public boolean onAttachedToWindow() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
        final SetImageBackup setImageBackup = this.setImageBackup;
        if (setImageBackup != null && (setImageBackup.imageLocation != null || setImageBackup.thumbLocation != null || setImageBackup.mediaLocation != null || setImageBackup.thumb != null)) {
            final SetImageBackup setImageBackup2 = this.setImageBackup;
            this.setImage(setImageBackup2.mediaLocation, setImageBackup2.mediaFilter, setImageBackup2.imageLocation, setImageBackup2.imageFilter, setImageBackup2.thumbLocation, setImageBackup2.thumbFilter, setImageBackup2.thumb, setImageBackup2.size, setImageBackup2.ext, setImageBackup2.parentObject, setImageBackup2.cacheType);
            return true;
        }
        return false;
    }
    
    public void onDetachedFromWindow() {
        if (this.currentImageLocation != null || this.currentMediaLocation != null || this.currentThumbLocation != null || this.staticThumbDrawable != null) {
            if (this.setImageBackup == null) {
                this.setImageBackup = new SetImageBackup();
            }
            final SetImageBackup setImageBackup = this.setImageBackup;
            setImageBackup.mediaLocation = this.currentMediaLocation;
            setImageBackup.mediaFilter = this.currentMediaFilter;
            setImageBackup.imageLocation = this.currentImageLocation;
            setImageBackup.imageFilter = this.currentImageFilter;
            setImageBackup.thumbLocation = this.currentThumbLocation;
            setImageBackup.thumbFilter = this.currentThumbFilter;
            setImageBackup.thumb = this.staticThumbDrawable;
            setImageBackup.size = this.currentSize;
            setImageBackup.ext = this.currentExt;
            setImageBackup.cacheType = this.currentCacheType;
            setImageBackup.parentObject = this.currentParentObject;
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
        this.clearImage();
    }
    
    public void setAllowDecodeSingleFrame(final boolean allowDecodeSingleFrame) {
        this.allowDecodeSingleFrame = allowDecodeSingleFrame;
    }
    
    public void setAllowStartAnimation(final boolean allowStartAnimation) {
        this.allowStartAnimation = allowStartAnimation;
    }
    
    public void setAlpha(final float overrideAlpha) {
        this.overrideAlpha = overrideAlpha;
    }
    
    public void setAspectFit(final boolean isAspectFit) {
        this.isAspectFit = isAspectFit;
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
    }
    
    public void setCrossfadeAlpha(final byte b) {
        this.crossfadeAlpha = b;
    }
    
    public void setCrossfadeWithOldImage(final boolean crossfadeWithOldImage) {
        this.crossfadeWithOldImage = crossfadeWithOldImage;
    }
    
    public void setCurrentAccount(final int currentAccount) {
        this.currentAccount = currentAccount;
    }
    
    public void setCurrentAlpha(final float currentAlpha) {
        this.currentAlpha = currentAlpha;
    }
    
    public void setDelegate(final ImageReceiverDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setForceCrossfade(final boolean forceCrossfade) {
        this.forceCrossfade = forceCrossfade;
    }
    
    public void setForceLoading(final boolean forceLoding) {
        this.forceLoding = forceLoding;
    }
    
    public void setForcePreview(final boolean forcePreview) {
        this.forcePreview = forcePreview;
    }
    
    public void setImage(final String s, final String s2, final Drawable drawable, final String s3, final int n) {
        this.setImage(ImageLocation.getForPath(s), s2, null, null, drawable, n, s3, null, 1);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Drawable drawable, final int n, final String s2, final Object o, final int n2) {
        this.setImage(imageLocation, s, null, null, drawable, n, s2, o, n2);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Drawable drawable, final String s2, final Object o, final int n) {
        this.setImage(imageLocation, s, null, null, drawable, 0, s2, o, n);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, final int n, final String s3, final Object o, final int n2) {
        this.setImage(imageLocation, s, imageLocation2, s2, null, n, s3, o, n2);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, final Drawable drawable, final int n, final String s3, final Object o, final int n2) {
        this.setImage(null, null, imageLocation, s, imageLocation2, s2, drawable, n, s3, o, n2);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, final String s3, final Object o, final int n) {
        this.setImage(imageLocation, s, imageLocation2, s2, null, 0, s3, o, n);
    }
    
    public void setImage(ImageLocation currentImageLocation, final String s, ImageLocation currentMediaLocation, final String s2, final ImageLocation currentThumbLocation, final String s3, final Drawable drawable, int i, final String s4, final Object currentParentObject, int currentCacheType) {
        final ImageLocation imageLocation = currentImageLocation;
        final ImageLocation imageLocation2 = currentMediaLocation;
        final SetImageBackup setImageBackup = this.setImageBackup;
        if (setImageBackup != null) {
            setImageBackup.imageLocation = null;
            setImageBackup.thumbLocation = null;
            setImageBackup.mediaLocation = null;
            setImageBackup.thumb = null;
        }
        boolean b = true;
        if (imageLocation2 == null && currentThumbLocation == null && imageLocation == null) {
            for (i = 0; i < 4; ++i) {
                this.recycleBitmap(null, i);
            }
            this.currentImageLocation = null;
            this.currentImageFilter = null;
            this.currentImageKey = null;
            this.currentMediaLocation = null;
            this.currentMediaFilter = null;
            this.currentMediaKey = null;
            this.currentThumbLocation = null;
            this.currentThumbFilter = null;
            this.currentThumbKey = null;
            this.currentMediaDrawable = null;
            this.mediaShader = null;
            this.currentImageDrawable = null;
            this.imageShader = null;
            this.thumbShader = null;
            this.crossfadeShader = null;
            this.currentExt = s4;
            this.currentParentObject = null;
            this.currentCacheType = 0;
            this.staticThumbDrawable = drawable;
            this.currentAlpha = 1.0f;
            this.currentSize = 0;
            ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
            final View parentView = this.parentView;
            if (parentView != null) {
                if (this.invalidateAll) {
                    parentView.invalidate();
                }
                else {
                    currentCacheType = this.imageX;
                    i = this.imageY;
                    parentView.invalidate(currentCacheType, i, this.imageW + currentCacheType, this.imageH + i);
                }
            }
            final ImageReceiverDelegate delegate = this.delegate;
            if (delegate != null) {
                final boolean b2 = this.currentImageDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentMediaDrawable != null;
                if (this.currentImageDrawable != null || this.currentMediaDrawable != null) {
                    b = false;
                }
                delegate.didSetImage(this, b2, b);
            }
            return;
        }
        String str;
        if (imageLocation2 != null) {
            str = imageLocation2.getKey(currentParentObject, null);
        }
        else {
            str = null;
        }
        currentImageLocation = imageLocation2;
        if (str == null && (currentImageLocation = imageLocation2) != null) {
            currentImageLocation = null;
        }
        this.currentKeyQuality = false;
        if (str == null && this.needsQualityThumb && (currentParentObject instanceof MessageObject || this.qulityThumbDocument != null)) {
            TLRPC.Document document = this.qulityThumbDocument;
            if (document == null) {
                document = ((MessageObject)currentParentObject).getDocument();
            }
            if (document != null && document.dc_id != 0 && document.id != 0L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("q_");
                sb.append(document.dc_id);
                sb.append("_");
                sb.append(document.id);
                str = sb.toString();
                this.currentKeyQuality = true;
            }
        }
        String string;
        if ((string = str) != null) {
            string = str;
            if (s2 != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append("@");
                sb2.append(s2);
                string = sb2.toString();
            }
        }
        String key;
        if (imageLocation != null) {
            key = imageLocation.getKey(currentParentObject, null);
        }
        else {
            key = null;
        }
        currentMediaLocation = imageLocation;
        if (key == null && (currentMediaLocation = imageLocation) != null) {
            currentMediaLocation = null;
        }
        String string2;
        if ((string2 = key) != null) {
            string2 = key;
            if (s != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(key);
                sb3.append("@");
                sb3.append(s);
                string2 = sb3.toString();
            }
        }
        Label_0798: {
            Label_0697: {
                if (string2 == null) {
                    final String currentImageKey = this.currentImageKey;
                    if (currentImageKey != null && currentImageKey.equals(string)) {
                        break Label_0697;
                    }
                }
                final String currentMediaKey = this.currentMediaKey;
                if (currentMediaKey == null || !currentMediaKey.equals(string2)) {
                    break Label_0798;
                }
            }
            final ImageReceiverDelegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.didSetImage(this, this.currentImageDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentMediaDrawable != null, this.currentImageDrawable == null && this.currentMediaDrawable == null);
            }
            if (!this.canceledLoading && !this.forcePreview) {
                return;
            }
        }
        ImageLocation strippedLocation = this.strippedLocation;
        if (strippedLocation == null) {
            if (currentMediaLocation != null) {
                strippedLocation = currentMediaLocation;
            }
            else {
                strippedLocation = currentImageLocation;
            }
        }
        String key2;
        if (currentThumbLocation != null) {
            key2 = currentThumbLocation.getKey(currentParentObject, strippedLocation);
        }
        else {
            key2 = null;
        }
        String string3 = key2;
        if (key2 != null) {
            string3 = key2;
            if (s3 != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(key2);
                sb4.append("@");
                sb4.append(s3);
                string3 = sb4.toString();
            }
        }
        if (this.crossfadeWithOldImage) {
            if (this.currentImageDrawable != null) {
                this.recycleBitmap(string3, 1);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(string2, 3);
                this.crossfadeShader = this.imageShader;
                this.crossfadeImage = this.currentImageDrawable;
                this.crossfadeKey = this.currentImageKey;
                this.crossfadingWithThumb = false;
                this.currentImageDrawable = null;
                this.currentImageKey = null;
            }
            else if (this.currentThumbDrawable != null) {
                this.recycleBitmap(string, 0);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(string2, 3);
                this.crossfadeShader = this.thumbShader;
                this.crossfadeImage = this.currentThumbDrawable;
                this.crossfadeKey = this.currentThumbKey;
                this.crossfadingWithThumb = false;
                this.currentThumbDrawable = null;
                this.currentThumbKey = null;
            }
            else if (this.staticThumbDrawable != null) {
                this.recycleBitmap(string, 0);
                this.recycleBitmap(string3, 1);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(string2, 3);
                this.crossfadeShader = this.thumbShader;
                this.crossfadeImage = this.staticThumbDrawable;
                this.crossfadingWithThumb = false;
                this.crossfadeKey = null;
                this.currentThumbDrawable = null;
                this.currentThumbKey = null;
            }
            else {
                this.recycleBitmap(string, 0);
                this.recycleBitmap(string3, 1);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(string2, 3);
                this.crossfadeShader = null;
            }
        }
        else {
            this.recycleBitmap(string, 0);
            this.recycleBitmap(string3, 1);
            this.recycleBitmap(null, 2);
            this.recycleBitmap(string2, 3);
            this.crossfadeShader = null;
        }
        boolean b3 = true;
        this.currentImageLocation = currentImageLocation;
        this.currentImageFilter = s2;
        this.currentImageKey = string;
        this.currentMediaLocation = currentMediaLocation;
        this.currentMediaFilter = s;
        this.currentMediaKey = string2;
        this.currentThumbLocation = currentThumbLocation;
        this.currentThumbFilter = s3;
        this.currentThumbKey = string3;
        this.currentParentObject = currentParentObject;
        this.currentExt = s4;
        this.currentSize = i;
        this.currentCacheType = currentCacheType;
        this.staticThumbDrawable = drawable;
        this.imageShader = null;
        this.thumbShader = null;
        this.mediaShader = null;
        this.currentAlpha = 1.0f;
        final ImageReceiverDelegate delegate3 = this.delegate;
        if (delegate3 != null) {
            final boolean b4 = this.currentImageDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentMediaDrawable != null;
            if (this.currentImageDrawable != null || this.currentMediaDrawable != null) {
                b3 = false;
            }
            delegate3.didSetImage(this, b4, b3);
        }
        ImageLoader.getInstance().loadImageForImageReceiver(this);
        final View parentView2 = this.parentView;
        if (parentView2 != null) {
            if (this.invalidateAll) {
                parentView2.invalidate();
            }
            else {
                i = this.imageX;
                currentCacheType = this.imageY;
                parentView2.invalidate(i, currentCacheType, this.imageW + i, this.imageH + currentCacheType);
            }
        }
    }
    
    public void setImageBitmap(final Bitmap bitmap) {
        Object imageBitmap = null;
        if (bitmap != null) {
            imageBitmap = new BitmapDrawable((Resources)null, bitmap);
        }
        this.setImageBitmap((Drawable)imageBitmap);
    }
    
    public void setImageBitmap(final Drawable staticThumbDrawable) {
        final ImageLoader instance = ImageLoader.getInstance();
        final boolean b = true;
        instance.cancelLoadingForImageReceiver(this, true);
        if (this.crossfadeWithOldImage) {
            if (this.currentImageDrawable != null) {
                this.recycleBitmap(null, 1);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(null, 3);
                this.crossfadeShader = this.imageShader;
                this.crossfadeImage = this.currentImageDrawable;
                this.crossfadeKey = this.currentImageKey;
                this.crossfadingWithThumb = true;
            }
            else if (this.currentThumbDrawable != null) {
                this.recycleBitmap(null, 0);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(null, 3);
                this.crossfadeShader = this.thumbShader;
                this.crossfadeImage = this.currentThumbDrawable;
                this.crossfadeKey = this.currentThumbKey;
                this.crossfadingWithThumb = true;
            }
            else if (this.staticThumbDrawable != null) {
                this.recycleBitmap(null, 0);
                this.recycleBitmap(null, 1);
                this.recycleBitmap(null, 2);
                this.recycleBitmap(null, 3);
                this.crossfadeShader = this.thumbShader;
                this.crossfadeImage = this.staticThumbDrawable;
                this.crossfadingWithThumb = true;
                this.crossfadeKey = null;
            }
            else {
                for (int i = 0; i < 4; ++i) {
                    this.recycleBitmap(null, i);
                }
                this.crossfadeShader = null;
            }
        }
        else {
            for (int j = 0; j < 4; ++j) {
                this.recycleBitmap(null, j);
            }
        }
        final Drawable staticThumbDrawable2 = this.staticThumbDrawable;
        if (staticThumbDrawable2 instanceof RecyclableDrawable) {
            ((RecyclableDrawable)staticThumbDrawable2).recycle();
        }
        final boolean b2 = staticThumbDrawable instanceof AnimatedFileDrawable;
        if (b2) {
            final AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable)staticThumbDrawable;
            animatedFileDrawable.setParentView(this.parentView);
            animatedFileDrawable.setUseSharedQueue(this.useSharedAnimationQueue);
            if (this.allowStartAnimation) {
                animatedFileDrawable.start();
            }
            animatedFileDrawable.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
        }
        this.staticThumbDrawable = staticThumbDrawable;
        final int roundRadius = this.roundRadius;
        if (roundRadius != 0 && staticThumbDrawable instanceof BitmapDrawable) {
            if (b2) {
                ((AnimatedFileDrawable)staticThumbDrawable).setRoundRadius(roundRadius);
            }
            else {
                final Bitmap bitmap = ((BitmapDrawable)staticThumbDrawable).getBitmap();
                final Shader$TileMode clamp = Shader$TileMode.CLAMP;
                this.thumbShader = new BitmapShader(bitmap, clamp, clamp);
            }
        }
        else {
            this.thumbShader = null;
        }
        this.currentMediaLocation = null;
        this.currentMediaFilter = null;
        this.currentMediaDrawable = null;
        this.currentMediaKey = null;
        this.mediaShader = null;
        this.currentImageLocation = null;
        this.currentImageFilter = null;
        this.currentImageDrawable = null;
        this.currentImageKey = null;
        this.imageShader = null;
        this.currentThumbLocation = null;
        this.currentThumbFilter = null;
        this.currentThumbKey = null;
        this.currentKeyQuality = false;
        this.currentExt = null;
        this.currentSize = 0;
        this.currentCacheType = 0;
        this.currentAlpha = 1.0f;
        final SetImageBackup setImageBackup = this.setImageBackup;
        if (setImageBackup != null) {
            setImageBackup.imageLocation = null;
            setImageBackup.thumbLocation = null;
            setImageBackup.mediaLocation = null;
            setImageBackup.thumb = null;
        }
        final ImageReceiverDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.didSetImage(this, this.currentThumbDrawable != null || this.staticThumbDrawable != null, true);
        }
        final View parentView = this.parentView;
        if (parentView != null) {
            if (this.invalidateAll) {
                parentView.invalidate();
            }
            else {
                final int imageX = this.imageX;
                final int imageY = this.imageY;
                parentView.invalidate(imageX, imageY, this.imageW + imageX, this.imageH + imageY);
            }
        }
        if (this.forceCrossfade && this.crossfadeWithOldImage && this.crossfadeImage != null) {
            this.currentAlpha = 0.0f;
            this.lastUpdateAlphaTime = System.currentTimeMillis();
            boolean crossfadeWithThumb = b;
            if (this.currentThumbDrawable == null) {
                crossfadeWithThumb = (this.staticThumbDrawable != null && b);
            }
            this.crossfadeWithThumb = crossfadeWithThumb;
        }
    }
    
    protected boolean setImageBitmapByKey(final Drawable currentThumbDrawable, final String s, int roundRadius, final boolean b) {
        final boolean b2 = false;
        if (currentThumbDrawable != null && s != null) {
            if (roundRadius == 0) {
                if (!s.equals(this.currentImageKey)) {
                    return false;
                }
                final boolean b3 = currentThumbDrawable instanceof AnimatedFileDrawable;
                if (!b3 && !(currentThumbDrawable instanceof LottieDrawable)) {
                    ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
                }
                this.currentImageDrawable = currentThumbDrawable;
                if (currentThumbDrawable instanceof ExtendedBitmapDrawable) {
                    this.imageOrientation = ((ExtendedBitmapDrawable)currentThumbDrawable).getOrientation();
                }
                roundRadius = this.roundRadius;
                if (roundRadius != 0 && currentThumbDrawable instanceof BitmapDrawable) {
                    if (b3) {
                        ((AnimatedFileDrawable)currentThumbDrawable).setRoundRadius(roundRadius);
                    }
                    else {
                        final Bitmap bitmap = ((BitmapDrawable)currentThumbDrawable).getBitmap();
                        final Shader$TileMode clamp = Shader$TileMode.CLAMP;
                        this.imageShader = new BitmapShader(bitmap, clamp, clamp);
                    }
                }
                else {
                    this.imageShader = null;
                }
                if ((!b && !this.forcePreview) || this.forceCrossfade) {
                    final Drawable currentMediaDrawable = this.currentMediaDrawable;
                    if (currentMediaDrawable instanceof AnimatedFileDrawable && ((AnimatedFileDrawable)currentMediaDrawable).hasBitmap()) {
                        roundRadius = 0;
                    }
                    else {
                        roundRadius = 1;
                    }
                    if (roundRadius != 0 && ((this.currentThumbDrawable == null && this.staticThumbDrawable == null) || this.currentAlpha == 1.0f || this.forceCrossfade)) {
                        this.currentAlpha = 0.0f;
                        this.lastUpdateAlphaTime = System.currentTimeMillis();
                        this.crossfadeWithThumb = (this.crossfadeImage != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null);
                    }
                }
                else {
                    this.currentAlpha = 1.0f;
                }
            }
            else if (roundRadius == 3) {
                if (!s.equals(this.currentMediaKey)) {
                    return false;
                }
                final boolean b4 = currentThumbDrawable instanceof AnimatedFileDrawable;
                if (!b4 && !(currentThumbDrawable instanceof LottieDrawable)) {
                    ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
                }
                this.currentMediaDrawable = currentThumbDrawable;
                roundRadius = this.roundRadius;
                if (roundRadius != 0 && currentThumbDrawable instanceof BitmapDrawable) {
                    if (b4) {
                        ((AnimatedFileDrawable)currentThumbDrawable).setRoundRadius(roundRadius);
                    }
                    else {
                        final Bitmap bitmap2 = ((BitmapDrawable)currentThumbDrawable).getBitmap();
                        final Shader$TileMode clamp2 = Shader$TileMode.CLAMP;
                        this.mediaShader = new BitmapShader(bitmap2, clamp2, clamp2);
                    }
                }
                else {
                    this.mediaShader = null;
                }
                if (this.currentImageDrawable == null) {
                    if ((!b && !this.forcePreview) || this.forceCrossfade) {
                        if ((this.currentThumbDrawable == null && this.staticThumbDrawable == null) || this.currentAlpha == 1.0f || this.forceCrossfade) {
                            this.currentAlpha = 0.0f;
                            this.lastUpdateAlphaTime = System.currentTimeMillis();
                            this.crossfadeWithThumb = (this.crossfadeImage != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null);
                        }
                    }
                    else {
                        this.currentAlpha = 1.0f;
                    }
                }
            }
            else if (roundRadius == 1) {
                if (this.currentThumbDrawable != null) {
                    return false;
                }
                Label_0613: {
                    if (!this.forcePreview) {
                        final AnimatedFileDrawable animation = this.getAnimation();
                        if (animation != null && animation.hasBitmap()) {
                            return false;
                        }
                        final Drawable currentImageDrawable = this.currentImageDrawable;
                        if (currentImageDrawable == null || currentImageDrawable instanceof AnimatedFileDrawable) {
                            final Drawable currentMediaDrawable2 = this.currentMediaDrawable;
                            if (currentMediaDrawable2 == null || currentMediaDrawable2 instanceof AnimatedFileDrawable) {
                                break Label_0613;
                            }
                        }
                        return false;
                    }
                }
                if (!s.equals(this.currentThumbKey)) {
                    return false;
                }
                ImageLoader.getInstance().incrementUseCount(this.currentThumbKey);
                this.currentThumbDrawable = currentThumbDrawable;
                if (currentThumbDrawable instanceof ExtendedBitmapDrawable) {
                    this.thumbOrientation = ((ExtendedBitmapDrawable)currentThumbDrawable).getOrientation();
                }
                roundRadius = this.roundRadius;
                if (roundRadius != 0 && currentThumbDrawable instanceof BitmapDrawable) {
                    if (currentThumbDrawable instanceof AnimatedFileDrawable) {
                        ((AnimatedFileDrawable)currentThumbDrawable).setRoundRadius(roundRadius);
                    }
                    else {
                        final Bitmap bitmap3 = ((BitmapDrawable)currentThumbDrawable).getBitmap();
                        final Shader$TileMode clamp3 = Shader$TileMode.CLAMP;
                        this.thumbShader = new BitmapShader(bitmap3, clamp3, clamp3);
                    }
                }
                else {
                    this.thumbShader = null;
                }
                if (!b && this.crossfadeAlpha != 2) {
                    final Object currentParentObject = this.currentParentObject;
                    if (currentParentObject instanceof MessageObject && ((MessageObject)currentParentObject).isRoundVideo() && ((MessageObject)this.currentParentObject).isSending()) {
                        this.currentAlpha = 1.0f;
                    }
                    else {
                        this.currentAlpha = 0.0f;
                        this.lastUpdateAlphaTime = System.currentTimeMillis();
                        this.crossfadeWithThumb = (this.staticThumbDrawable != null && this.currentImageKey == null && this.currentMediaKey == null);
                    }
                }
                else {
                    this.currentAlpha = 1.0f;
                }
            }
            if (currentThumbDrawable instanceof AnimatedFileDrawable) {
                final AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable)currentThumbDrawable;
                animatedFileDrawable.setParentView(this.parentView);
                animatedFileDrawable.setUseSharedQueue(this.useSharedAnimationQueue);
                if (this.allowStartAnimation) {
                    animatedFileDrawable.start();
                }
                animatedFileDrawable.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
            }
            final View parentView = this.parentView;
            if (parentView != null) {
                if (this.invalidateAll) {
                    parentView.invalidate();
                }
                else {
                    roundRadius = this.imageX;
                    final int imageY = this.imageY;
                    parentView.invalidate(roundRadius, imageY, this.imageW + roundRadius, this.imageH + imageY);
                }
            }
            final ImageReceiverDelegate delegate = this.delegate;
            if (delegate != null) {
                final boolean b5 = this.currentImageDrawable != null || this.currentThumbDrawable != null || this.staticThumbDrawable != null || this.currentMediaDrawable != null;
                boolean b6 = b2;
                if (this.currentImageDrawable == null) {
                    b6 = b2;
                    if (this.currentMediaDrawable == null) {
                        b6 = true;
                    }
                }
                delegate.didSetImage(this, b5, b6);
            }
            return true;
        }
        return false;
    }
    
    public void setImageCoords(final int imageX, final int imageY, final int imageW, final int imageH) {
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageW = imageW;
        this.imageH = imageH;
    }
    
    public void setImageWidth(final int imageW) {
        this.imageW = imageW;
    }
    
    public void setImageX(final int imageX) {
        this.imageX = imageX;
    }
    
    public void setImageY(final int imageY) {
        this.imageY = imageY;
    }
    
    public void setInvalidateAll(final boolean invalidateAll) {
        this.invalidateAll = invalidateAll;
    }
    
    public void setManualAlphaAnimator(final boolean manualAlphaAnimator) {
        this.manualAlphaAnimator = manualAlphaAnimator;
    }
    
    public void setNeedsQualityThumb(final boolean needsQualityThumb) {
        this.needsQualityThumb = needsQualityThumb;
    }
    
    public void setOrientation(int n, final boolean centerRotation) {
        int i;
        while (true) {
            i = n;
            if (n >= 0) {
                break;
            }
            n += 360;
        }
        while (i > 360) {
            i -= 360;
        }
        this.thumbOrientation = i;
        this.imageOrientation = i;
        this.centerRotation = centerRotation;
    }
    
    public void setParam(final int param) {
        this.param = param;
    }
    
    public void setParentView(final View parentView) {
        this.parentView = parentView;
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null) {
            animation.setParentView(this.parentView);
        }
    }
    
    public void setPressed(final int isPressed) {
        this.isPressed = isPressed;
    }
    
    public void setQualityThumbDocument(final TLRPC.Document qulityThumbDocument) {
        this.qulityThumbDocument = qulityThumbDocument;
    }
    
    public void setRoundRadius(final int roundRadius) {
        this.roundRadius = roundRadius;
    }
    
    public void setShouldGenerateQualityThumb(final boolean shouldGenerateQualityThumb) {
        this.shouldGenerateQualityThumb = shouldGenerateQualityThumb;
    }
    
    public void setSideClip(final float sideClip) {
        this.sideClip = sideClip;
    }
    
    public void setStrippedLocation(final ImageLocation strippedLocation) {
        this.strippedLocation = strippedLocation;
    }
    
    protected void setTag(final int imageTag, final int n) {
        if (n == 1) {
            this.thumbTag = imageTag;
        }
        else if (n == 3) {
            this.mediaTag = imageTag;
        }
        else {
            this.imageTag = imageTag;
        }
    }
    
    public void setUseSharedAnimationQueue(final boolean useSharedAnimationQueue) {
        this.useSharedAnimationQueue = useSharedAnimationQueue;
    }
    
    public void setVisible(final boolean isVisible, final boolean b) {
        if (this.isVisible == isVisible) {
            return;
        }
        this.isVisible = isVisible;
        if (b) {
            final View parentView = this.parentView;
            if (parentView != null) {
                if (this.invalidateAll) {
                    parentView.invalidate();
                }
                else {
                    final int imageX = this.imageX;
                    final int imageY = this.imageY;
                    parentView.invalidate(imageX, imageY, this.imageW + imageX, this.imageH + imageY);
                }
            }
        }
    }
    
    public void startAnimation() {
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null) {
            animation.setUseSharedQueue(this.useSharedAnimationQueue);
            animation.start();
        }
    }
    
    public void stopAnimation() {
        final AnimatedFileDrawable animation = this.getAnimation();
        if (animation != null) {
            animation.stop();
        }
    }
    
    public static class BitmapHolder
    {
        public Bitmap bitmap;
        private String key;
        private boolean recycleOnRelease;
        
        public BitmapHolder(final Bitmap bitmap) {
            this.bitmap = bitmap;
            this.recycleOnRelease = true;
        }
        
        public BitmapHolder(final Bitmap bitmap, final String key) {
            this.bitmap = bitmap;
            this.key = key;
            if (this.key != null) {
                ImageLoader.getInstance().incrementUseCount(this.key);
            }
        }
        
        public int getHeight() {
            final Bitmap bitmap = this.bitmap;
            int height;
            if (bitmap != null) {
                height = bitmap.getHeight();
            }
            else {
                height = 0;
            }
            return height;
        }
        
        public int getWidth() {
            final Bitmap bitmap = this.bitmap;
            int width;
            if (bitmap != null) {
                width = bitmap.getWidth();
            }
            else {
                width = 0;
            }
            return width;
        }
        
        public boolean isRecycled() {
            final Bitmap bitmap = this.bitmap;
            return bitmap == null || bitmap.isRecycled();
        }
        
        public void release() {
            if (this.key == null) {
                if (this.recycleOnRelease) {
                    final Bitmap bitmap = this.bitmap;
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
                this.bitmap = null;
                return;
            }
            final boolean decrementUseCount = ImageLoader.getInstance().decrementUseCount(this.key);
            if (!ImageLoader.getInstance().isInCache(this.key) && decrementUseCount) {
                this.bitmap.recycle();
            }
            this.key = null;
            this.bitmap = null;
        }
    }
    
    public interface ImageReceiverDelegate
    {
        void didSetImage(final ImageReceiver p0, final boolean p1, final boolean p2);
    }
    
    private class SetImageBackup
    {
        public int cacheType;
        public String ext;
        public String imageFilter;
        public ImageLocation imageLocation;
        public String mediaFilter;
        public ImageLocation mediaLocation;
        public Object parentObject;
        public int size;
        public Drawable thumb;
        public String thumbFilter;
        public ImageLocation thumbLocation;
    }
}
