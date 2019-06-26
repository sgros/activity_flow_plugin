// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Crop;

import android.view.ViewGroup;
import android.view.MotionEvent;
import android.graphics.Bitmap$Config;
import android.graphics.Canvas;
import android.view.animation.Interpolator;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.os.Build$VERSION;
import androidx.annotation.Keep;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.animation.Animator;
import android.graphics.RectF;
import android.view.View;

public class CropAreaView extends View
{
    private Control activeControl;
    private RectF actualRect;
    private Animator animator;
    private RectF bottomEdge;
    private RectF bottomLeftCorner;
    private float bottomPadding;
    private RectF bottomRightCorner;
    private Bitmap circleBitmap;
    Paint dimPaint;
    private boolean dimVisibile;
    private Paint eraserPaint;
    Paint framePaint;
    private boolean frameVisible;
    private boolean freeform;
    private Animator gridAnimator;
    private float gridProgress;
    private GridType gridType;
    Paint handlePaint;
    AccelerateDecelerateInterpolator interpolator;
    private boolean isDragging;
    private RectF leftEdge;
    Paint linePaint;
    private AreaViewListener listener;
    private float lockAspectRatio;
    private float minWidth;
    private GridType previousGridType;
    private int previousX;
    private int previousY;
    private RectF rightEdge;
    Paint shadowPaint;
    private float sidePadding;
    private RectF tempRect;
    private RectF topEdge;
    private RectF topLeftCorner;
    private RectF topRightCorner;
    
    public CropAreaView(final Context context) {
        super(context);
        this.topLeftCorner = new RectF();
        this.topRightCorner = new RectF();
        this.bottomLeftCorner = new RectF();
        this.bottomRightCorner = new RectF();
        this.topEdge = new RectF();
        this.leftEdge = new RectF();
        this.bottomEdge = new RectF();
        this.rightEdge = new RectF();
        this.actualRect = new RectF();
        this.tempRect = new RectF();
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.freeform = true;
        this.frameVisible = true;
        this.dimVisibile = true;
        this.sidePadding = (float)AndroidUtilities.dp(16.0f);
        this.minWidth = (float)AndroidUtilities.dp(32.0f);
        this.gridType = GridType.NONE;
        (this.dimPaint = new Paint()).setColor(-872415232);
        (this.shadowPaint = new Paint()).setStyle(Paint$Style.FILL);
        this.shadowPaint.setColor(436207616);
        this.shadowPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        (this.linePaint = new Paint()).setStyle(Paint$Style.FILL);
        this.linePaint.setColor(-1);
        this.linePaint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        (this.handlePaint = new Paint()).setStyle(Paint$Style.FILL);
        this.handlePaint.setColor(-1);
        (this.framePaint = new Paint()).setStyle(Paint$Style.FILL);
        this.framePaint.setColor(-1291845633);
        (this.eraserPaint = new Paint(1)).setColor(0);
        this.eraserPaint.setStyle(Paint$Style.FILL);
        this.eraserPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
    }
    
    private void constrainRectByHeight(final RectF rectF, final float n) {
        final float height = rectF.height();
        rectF.right = rectF.left + n * height;
        rectF.bottom = rectF.top + height;
    }
    
    private void constrainRectByWidth(final RectF rectF, float n) {
        final float width = rectF.width();
        n = width / n;
        rectF.right = rectF.left + width;
        rectF.bottom = rectF.top + n;
    }
    
    private float getGridProgress() {
        return this.gridProgress;
    }
    
    @Keep
    private void setCropBottom(final float bottom) {
        this.actualRect.bottom = bottom;
        this.invalidate();
    }
    
    @Keep
    private void setCropLeft(final float left) {
        this.actualRect.left = left;
        this.invalidate();
    }
    
    @Keep
    private void setCropRight(final float right) {
        this.actualRect.right = right;
        this.invalidate();
    }
    
    @Keep
    private void setCropTop(final float top) {
        this.actualRect.top = top;
        this.invalidate();
    }
    
    @Keep
    private void setGridProgress(final float gridProgress) {
        this.gridProgress = gridProgress;
        this.invalidate();
    }
    
    private void updateTouchAreas() {
        final int dp = AndroidUtilities.dp(16.0f);
        final RectF topLeftCorner = this.topLeftCorner;
        final RectF actualRect = this.actualRect;
        final float left = actualRect.left;
        final float n = (float)dp;
        final float top = actualRect.top;
        topLeftCorner.set(left - n, top - n, left + n, top + n);
        final RectF topRightCorner = this.topRightCorner;
        final RectF actualRect2 = this.actualRect;
        final float right = actualRect2.right;
        final float top2 = actualRect2.top;
        topRightCorner.set(right - n, top2 - n, right + n, top2 + n);
        final RectF bottomLeftCorner = this.bottomLeftCorner;
        final RectF actualRect3 = this.actualRect;
        final float left2 = actualRect3.left;
        final float bottom = actualRect3.bottom;
        bottomLeftCorner.set(left2 - n, bottom - n, left2 + n, bottom + n);
        final RectF bottomRightCorner = this.bottomRightCorner;
        final RectF actualRect4 = this.actualRect;
        final float right2 = actualRect4.right;
        final float bottom2 = actualRect4.bottom;
        bottomRightCorner.set(right2 - n, bottom2 - n, right2 + n, bottom2 + n);
        final RectF topEdge = this.topEdge;
        final RectF actualRect5 = this.actualRect;
        final float left3 = actualRect5.left;
        final float top3 = actualRect5.top;
        topEdge.set(left3 + n, top3 - n, actualRect5.right - n, top3 + n);
        final RectF leftEdge = this.leftEdge;
        final RectF actualRect6 = this.actualRect;
        final float left4 = actualRect6.left;
        leftEdge.set(left4 - n, actualRect6.top + n, left4 + n, actualRect6.bottom - n);
        final RectF rightEdge = this.rightEdge;
        final RectF actualRect7 = this.actualRect;
        final float right3 = actualRect7.right;
        rightEdge.set(right3 - n, actualRect7.top + n, right3 + n, actualRect7.bottom - n);
        final RectF bottomEdge = this.bottomEdge;
        final RectF actualRect8 = this.actualRect;
        final float left5 = actualRect8.left;
        final float bottom3 = actualRect8.bottom;
        bottomEdge.set(left5 + n, bottom3 - n, actualRect8.right - n, bottom3 + n);
    }
    
    public void calculateRect(final RectF rectF, float n) {
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final float n2 = (float)statusBarHeight;
        final float b = this.getMeasuredHeight() - this.bottomPadding - n2;
        final float n3 = this.getMeasuredWidth() / b;
        final float min = Math.min((float)this.getMeasuredWidth(), b);
        final float sidePadding = this.sidePadding;
        final float n4 = (float)this.getMeasuredWidth();
        final float sidePadding2 = this.sidePadding;
        final float n5 = n4 - sidePadding2 * 2.0f;
        final float n6 = b - sidePadding2 * 2.0f;
        final float n7 = this.getMeasuredWidth() / 2.0f;
        final float n8 = n2 + b / 2.0f;
        float n9;
        float n10;
        float n11;
        if (Math.abs(1.0f - n) < 1.0E-4) {
            n = (min - sidePadding * 2.0f) / 2.0f;
            n9 = n7 - n;
            n10 = n8 - n;
            n11 = n7 + n;
            n += n8;
        }
        else if (n > n3) {
            final float n12 = n5 / 2.0f;
            final float n13 = n5 / n / 2.0f;
            n11 = n7 + n12;
            n = n8 + n13;
            n10 = n8 - n13;
            n9 = n7 - n12;
        }
        else {
            final float n14 = n * n6 / 2.0f;
            final float n15 = n6 / 2.0f;
            n11 = n7 + n14;
            n = n8 + n15;
            n9 = n7 - n14;
            n10 = n8 - n15;
        }
        rectF.set(n9, n10, n11, n);
    }
    
    public void fill(final RectF actualRect, final Animator animator, final boolean b) {
        if (b) {
            final Animator animator2 = this.animator;
            if (animator2 != null) {
                animator2.cancel();
                this.animator = null;
            }
            final AnimatorSet animator3 = new AnimatorSet();
            ((AnimatorSet)(this.animator = (Animator)animator3)).setDuration(300L);
            final Animator[] array = new Animator[5];
            (array[0] = (Animator)ObjectAnimator.ofFloat((Object)this, "cropLeft", new float[] { actualRect.left })).setInterpolator((TimeInterpolator)this.interpolator);
            (array[1] = (Animator)ObjectAnimator.ofFloat((Object)this, "cropTop", new float[] { actualRect.top })).setInterpolator((TimeInterpolator)this.interpolator);
            (array[2] = (Animator)ObjectAnimator.ofFloat((Object)this, "cropRight", new float[] { actualRect.right })).setInterpolator((TimeInterpolator)this.interpolator);
            (array[3] = (Animator)ObjectAnimator.ofFloat((Object)this, "cropBottom", new float[] { actualRect.bottom })).setInterpolator((TimeInterpolator)this.interpolator);
            (array[4] = animator).setInterpolator((TimeInterpolator)this.interpolator);
            animator3.playTogether(array);
            animator3.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    CropAreaView.this.setActualRect(actualRect);
                    CropAreaView.this.animator = null;
                }
            });
            animator3.start();
        }
        else {
            this.setActualRect(actualRect);
        }
    }
    
    public float getAspectRatio() {
        final RectF actualRect = this.actualRect;
        return (actualRect.right - actualRect.left) / (actualRect.bottom - actualRect.top);
    }
    
    public float getCropBottom() {
        return this.actualRect.bottom;
    }
    
    public float getCropCenterX() {
        final RectF actualRect = this.actualRect;
        final float left = actualRect.left;
        return left + (actualRect.right - left) / 2.0f;
    }
    
    public float getCropCenterY() {
        final RectF actualRect = this.actualRect;
        final float top = actualRect.top;
        return top + (actualRect.bottom - top) / 2.0f;
    }
    
    public float getCropHeight() {
        final RectF actualRect = this.actualRect;
        return actualRect.bottom - actualRect.top;
    }
    
    public float getCropLeft() {
        return this.actualRect.left;
    }
    
    public void getCropRect(final RectF rectF) {
        rectF.set(this.actualRect);
    }
    
    public float getCropRight() {
        return this.actualRect.right;
    }
    
    public float getCropTop() {
        return this.actualRect.top;
    }
    
    public float getCropWidth() {
        final RectF actualRect = this.actualRect;
        return actualRect.right - actualRect.left;
    }
    
    public Interpolator getInterpolator() {
        return (Interpolator)this.interpolator;
    }
    
    public float getLockAspectRatio() {
        return this.lockAspectRatio;
    }
    
    public RectF getTargetRectToFill() {
        final RectF rectF = new RectF();
        this.calculateRect(rectF, this.getAspectRatio());
        return rectF;
    }
    
    public boolean isDragging() {
        return this.isDragging;
    }
    
    protected void onDraw(final Canvas canvas) {
        while (true) {
            if (this.freeform) {
                int dp = AndroidUtilities.dp(2.0f);
                int dp2 = AndroidUtilities.dp(16.0f);
                final int dp3 = AndroidUtilities.dp(3.0f);
                final RectF actualRect = this.actualRect;
                final float left = actualRect.left;
                final int n = (int)left - dp;
                final float top = actualRect.top;
                final int n2 = (int)top - dp;
                final int n3 = (int)(actualRect.right - left);
                final int n4 = dp * 2;
                int n5 = n3 + n4;
                int n6 = (int)(actualRect.bottom - top) + n4;
                if (this.dimVisibile) {
                    final float n7 = (float)this.getWidth();
                    final float n8 = (float)(n2 + dp);
                    canvas.drawRect(0.0f, 0.0f, n7, n8, this.dimPaint);
                    final float n9 = (float)(n + dp);
                    final float n10 = (float)(n2 + n6 - dp);
                    canvas.drawRect(0.0f, n8, n9, n10, this.dimPaint);
                    canvas.drawRect((float)(n + n5 - dp), n8, (float)this.getWidth(), n10, this.dimPaint);
                    canvas.drawRect(0.0f, n10, (float)this.getWidth(), (float)this.getHeight(), this.dimPaint);
                }
                if (!this.frameVisible) {
                    return;
                }
                final int n11 = dp3 - dp;
                final int n12 = dp3 * 2;
                final int n13 = n5 - n12;
                final int n14 = n6 - n12;
                final GridType gridType = this.gridType;
                GridType previousGridType;
                if ((previousGridType = gridType) == GridType.NONE) {
                    previousGridType = gridType;
                    if (this.gridProgress > 0.0f) {
                        previousGridType = this.previousGridType;
                    }
                }
                this.shadowPaint.setAlpha((int)(this.gridProgress * 26.0f));
                this.linePaint.setAlpha((int)(this.gridProgress * 178.0f));
                int n43;
                for (int i = 0; i < 3; i = n43) {
                    int n27;
                    int n28;
                    int n29;
                    int n30;
                    if (previousGridType == GridType.MINOR) {
                        for (int j = 1; j < 4; ++j) {
                            if (i != 2 || j != 3) {
                                final int n15 = n + dp3;
                                final int n16 = n13 / 3;
                                final float n17 = (float)(n15 + n16 / 3 * j + n16 * i);
                                final int n18 = n2 + dp3;
                                final float n19 = (float)n18;
                                final float n20 = (float)(n18 + n14);
                                canvas.drawLine(n17, n19, n17, n20, this.shadowPaint);
                                canvas.drawLine(n17, n19, n17, n20, this.linePaint);
                                final float n21 = (float)n15;
                                final int n22 = n14 / 3;
                                final float n23 = (float)(n18 + n22 / 3 * j + n22 * i);
                                final float n24 = (float)(n15 + n13);
                                canvas.drawLine(n21, n23, n24, n23, this.shadowPaint);
                                canvas.drawLine(n21, n23, n24, n23, this.linePaint);
                            }
                        }
                        final int n25 = n6;
                        final int n26 = n5;
                        n27 = dp2;
                        n28 = n25;
                        n29 = dp;
                        n30 = n26;
                    }
                    else {
                        final int n31 = dp;
                        final int n32 = dp2;
                        final int n33 = n6;
                        final int n34 = n5;
                        n27 = n32;
                        n28 = n33;
                        n29 = n31;
                        n30 = n34;
                        if (previousGridType == GridType.MAJOR) {
                            n27 = n32;
                            n28 = n33;
                            n29 = n31;
                            n30 = n34;
                            if (i > 0) {
                                final int n35 = n + dp3;
                                final float n36 = (float)(n13 / 3 * i + n35);
                                final int n37 = n2 + dp3;
                                final float n38 = (float)n37;
                                final float n39 = (float)(n37 + n14);
                                canvas.drawLine(n36, n38, n36, n39, this.shadowPaint);
                                canvas.drawLine(n36, n38, n36, n39, this.linePaint);
                                final float n40 = (float)n35;
                                final float n41 = (float)(n37 + n14 / 3 * i);
                                final float n42 = (float)(n35 + n13);
                                canvas.drawLine(n40, n41, n42, n41, this.shadowPaint);
                                canvas.drawLine(n40, n41, n42, n41, this.linePaint);
                                n30 = n34;
                                n29 = n31;
                                n28 = n33;
                                n27 = n32;
                            }
                        }
                    }
                    n43 = i + 1;
                    final int n44 = n28;
                    final int n45 = n29;
                    n5 = n30;
                    dp = n45;
                    dp2 = n27;
                    n6 = n44;
                }
                final int n46 = n + n11;
                final float n47 = (float)n46;
                final int n48 = n2 + n11;
                final float n49 = (float)n48;
                final int n50 = n + n5;
                final int n51 = n50 - n11;
                final float n52 = (float)n51;
                canvas.drawRect(n47, n49, n52, (float)(n48 + dp), this.framePaint);
                final float n53 = (float)(n46 + dp);
                final int n54 = n2 + n6;
                final int n55 = n54 - n11;
                final float n56 = (float)n55;
                canvas.drawRect(n47, n49, n53, n56, this.framePaint);
                canvas.drawRect(n47, (float)(n55 - dp), n52, n56, this.framePaint);
                canvas.drawRect((float)(n51 - dp), n49, n52, n56, this.framePaint);
                final float n57 = (float)n;
                final float n58 = (float)n2;
                final float n59 = (float)(n + dp2);
                final float n60 = (float)(n2 + dp3);
                canvas.drawRect(n57, n58, n59, n60, this.handlePaint);
                final float n61 = (float)(n + dp3);
                final float n62 = (float)(n2 + dp2);
                canvas.drawRect(n57, n58, n61, n62, this.handlePaint);
                final float n63 = (float)(n50 - dp2);
                final float n64 = (float)n50;
                canvas.drawRect(n63, n58, n64, n60, this.handlePaint);
                final float n65 = (float)(n50 - dp3);
                canvas.drawRect(n65, n58, n64, n62, this.handlePaint);
                final float n66 = (float)(n54 - dp3);
                final float n67 = (float)n54;
                canvas.drawRect(n57, n66, n59, n67, this.handlePaint);
                final float n68 = (float)(n54 - dp2);
                canvas.drawRect(n57, n68, n61, n67, this.handlePaint);
                canvas.drawRect(n63, n66, n64, n67, this.handlePaint);
                canvas.drawRect(n65, n68, n64, n67, this.handlePaint);
                return;
            }
            else {
                final Bitmap circleBitmap = this.circleBitmap;
                if (circleBitmap != null && circleBitmap.getWidth() == this.actualRect.width()) {
                    break Label_1268;
                }
                final Bitmap circleBitmap2 = this.circleBitmap;
                if (circleBitmap2 != null) {
                    circleBitmap2.recycle();
                    this.circleBitmap = null;
                }
            }
            try {
                this.circleBitmap = Bitmap.createBitmap((int)this.actualRect.width(), (int)this.actualRect.height(), Bitmap$Config.ARGB_8888);
                final Canvas canvas2 = new Canvas(this.circleBitmap);
                canvas2.drawRect(0.0f, 0.0f, this.actualRect.width(), this.actualRect.height(), this.dimPaint);
                canvas2.drawCircle(this.actualRect.width() / 2.0f, this.actualRect.height() / 2.0f, this.actualRect.width() / 2.0f, this.eraserPaint);
                canvas2.setBitmap((Bitmap)null);
                canvas.drawRect(0.0f, 0.0f, (float)this.getWidth(), (float)(int)this.actualRect.top, this.dimPaint);
                final RectF actualRect2 = this.actualRect;
                canvas.drawRect(0.0f, (float)(int)actualRect2.top, (float)(int)actualRect2.left, (float)(int)actualRect2.bottom, this.dimPaint);
                final RectF actualRect3 = this.actualRect;
                canvas.drawRect((float)(int)actualRect3.right, (float)(int)actualRect3.top, (float)this.getWidth(), (float)(int)this.actualRect.bottom, this.dimPaint);
                canvas.drawRect(0.0f, (float)(int)this.actualRect.bottom, (float)this.getWidth(), (float)this.getHeight(), this.dimPaint);
                final Bitmap circleBitmap3 = this.circleBitmap;
                final RectF actualRect4 = this.actualRect;
                canvas.drawBitmap(circleBitmap3, (float)(int)actualRect4.left, (float)(int)actualRect4.top, (Paint)null);
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int n = (int)(motionEvent.getX() - ((ViewGroup)this.getParent()).getX());
        final int n2 = (int)(motionEvent.getY() - ((ViewGroup)this.getParent()).getY());
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final float n3 = (float)statusBarHeight;
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            if (this.freeform) {
                final RectF topLeftCorner = this.topLeftCorner;
                final float n4 = (float)n;
                final float n5 = (float)n2;
                if (topLeftCorner.contains(n4, n5)) {
                    this.activeControl = Control.TOP_LEFT;
                }
                else if (this.topRightCorner.contains(n4, n5)) {
                    this.activeControl = Control.TOP_RIGHT;
                }
                else if (this.bottomLeftCorner.contains(n4, n5)) {
                    this.activeControl = Control.BOTTOM_LEFT;
                }
                else if (this.bottomRightCorner.contains(n4, n5)) {
                    this.activeControl = Control.BOTTOM_RIGHT;
                }
                else if (this.leftEdge.contains(n4, n5)) {
                    this.activeControl = Control.LEFT;
                }
                else if (this.topEdge.contains(n4, n5)) {
                    this.activeControl = Control.TOP;
                }
                else if (this.rightEdge.contains(n4, n5)) {
                    this.activeControl = Control.RIGHT;
                }
                else {
                    if (!this.bottomEdge.contains(n4, n5)) {
                        this.activeControl = Control.NONE;
                        return false;
                    }
                    this.activeControl = Control.BOTTOM;
                }
                this.previousX = n;
                this.previousY = n2;
                this.setGridType(GridType.MAJOR, false);
                this.isDragging = true;
                final AreaViewListener listener = this.listener;
                if (listener != null) {
                    listener.onAreaChangeBegan();
                }
                return true;
            }
            this.activeControl = Control.NONE;
            return false;
        }
        else if (actionMasked != 1 && actionMasked != 3) {
            if (actionMasked != 2) {
                return false;
            }
            if (this.activeControl == Control.NONE) {
                return false;
            }
            this.tempRect.set(this.actualRect);
            final float n6 = (float)(n - this.previousX);
            final float n7 = (float)(n2 - this.previousY);
            this.previousX = n;
            this.previousY = n2;
            switch (CropAreaView$3.$SwitchMap$org$telegram$ui$Components$Crop$CropAreaView$Control[this.activeControl.ordinal()]) {
                case 8: {
                    final RectF tempRect = this.tempRect;
                    tempRect.bottom += n7;
                    final float lockAspectRatio = this.lockAspectRatio;
                    if (lockAspectRatio > 0.0f) {
                        this.constrainRectByHeight(tempRect, lockAspectRatio);
                        break;
                    }
                    break;
                }
                case 7: {
                    final RectF tempRect2 = this.tempRect;
                    tempRect2.right += n6;
                    final float lockAspectRatio2 = this.lockAspectRatio;
                    if (lockAspectRatio2 > 0.0f) {
                        this.constrainRectByWidth(tempRect2, lockAspectRatio2);
                        break;
                    }
                    break;
                }
                case 6: {
                    final RectF tempRect3 = this.tempRect;
                    tempRect3.left += n6;
                    final float lockAspectRatio3 = this.lockAspectRatio;
                    if (lockAspectRatio3 > 0.0f) {
                        this.constrainRectByWidth(tempRect3, lockAspectRatio3);
                        break;
                    }
                    break;
                }
                case 5: {
                    final RectF tempRect4 = this.tempRect;
                    tempRect4.top += n7;
                    final float lockAspectRatio4 = this.lockAspectRatio;
                    if (lockAspectRatio4 > 0.0f) {
                        this.constrainRectByHeight(tempRect4, lockAspectRatio4);
                        break;
                    }
                    break;
                }
                case 4: {
                    final RectF tempRect5 = this.tempRect;
                    tempRect5.right += n6;
                    tempRect5.bottom += n7;
                    if (this.lockAspectRatio <= 0.0f) {
                        break;
                    }
                    if (Math.abs(n6) > Math.abs(n7)) {
                        this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                        break;
                    }
                    this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                    break;
                }
                case 3: {
                    final RectF tempRect6 = this.tempRect;
                    tempRect6.left += n6;
                    tempRect6.bottom += n7;
                    if (this.lockAspectRatio > 0.0f) {
                        final float width = tempRect6.width();
                        if (Math.abs(n6) > Math.abs(n7)) {
                            this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                        }
                        else {
                            this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                        }
                        final RectF tempRect7 = this.tempRect;
                        tempRect7.left -= tempRect7.width() - width;
                        break;
                    }
                    break;
                }
                case 2: {
                    final RectF tempRect8 = this.tempRect;
                    tempRect8.right += n6;
                    tempRect8.top += n7;
                    if (this.lockAspectRatio > 0.0f) {
                        final float height = tempRect8.height();
                        if (Math.abs(n6) > Math.abs(n7)) {
                            this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                        }
                        else {
                            this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                        }
                        final RectF tempRect9 = this.tempRect;
                        tempRect9.top -= tempRect9.width() - height;
                        break;
                    }
                    break;
                }
                case 1: {
                    final RectF tempRect10 = this.tempRect;
                    tempRect10.left += n6;
                    tempRect10.top += n7;
                    if (this.lockAspectRatio > 0.0f) {
                        final float width2 = tempRect10.width();
                        final float height2 = this.tempRect.height();
                        if (Math.abs(n6) > Math.abs(n7)) {
                            this.constrainRectByWidth(this.tempRect, this.lockAspectRatio);
                        }
                        else {
                            this.constrainRectByHeight(this.tempRect, this.lockAspectRatio);
                        }
                        final RectF tempRect11 = this.tempRect;
                        tempRect11.left -= tempRect11.width() - width2;
                        final RectF tempRect12 = this.tempRect;
                        tempRect12.top -= tempRect12.width() - height2;
                        break;
                    }
                    break;
                }
            }
            final RectF tempRect13 = this.tempRect;
            final float left = tempRect13.left;
            final float sidePadding = this.sidePadding;
            if (left < sidePadding) {
                final float lockAspectRatio5 = this.lockAspectRatio;
                if (lockAspectRatio5 > 0.0f) {
                    tempRect13.bottom = tempRect13.top + (tempRect13.right - sidePadding) / lockAspectRatio5;
                }
                this.tempRect.left = this.sidePadding;
            }
            else if (tempRect13.right > this.getWidth() - this.sidePadding) {
                this.tempRect.right = this.getWidth() - this.sidePadding;
                if (this.lockAspectRatio > 0.0f) {
                    final RectF tempRect14 = this.tempRect;
                    tempRect14.bottom = tempRect14.top + tempRect14.width() / this.lockAspectRatio;
                }
            }
            final float sidePadding2 = this.sidePadding;
            final float top = n3 + sidePadding2;
            final float n8 = this.bottomPadding + sidePadding2;
            final RectF tempRect15 = this.tempRect;
            if (tempRect15.top < top) {
                final float lockAspectRatio6 = this.lockAspectRatio;
                if (lockAspectRatio6 > 0.0f) {
                    tempRect15.right = tempRect15.left + (tempRect15.bottom - top) * lockAspectRatio6;
                }
                this.tempRect.top = top;
            }
            else if (tempRect15.bottom > this.getHeight() - n8) {
                this.tempRect.bottom = this.getHeight() - n8;
                if (this.lockAspectRatio > 0.0f) {
                    final RectF tempRect16 = this.tempRect;
                    tempRect16.right = tempRect16.left + tempRect16.height() * this.lockAspectRatio;
                }
            }
            final float width3 = this.tempRect.width();
            final float minWidth = this.minWidth;
            if (width3 < minWidth) {
                final RectF tempRect17 = this.tempRect;
                tempRect17.right = tempRect17.left + minWidth;
            }
            final float height3 = this.tempRect.height();
            final float minWidth2 = this.minWidth;
            if (height3 < minWidth2) {
                final RectF tempRect18 = this.tempRect;
                tempRect18.bottom = tempRect18.top + minWidth2;
            }
            final float lockAspectRatio7 = this.lockAspectRatio;
            if (lockAspectRatio7 > 0.0f) {
                if (lockAspectRatio7 < 1.0f) {
                    final float width4 = this.tempRect.width();
                    final float minWidth3 = this.minWidth;
                    if (width4 <= minWidth3) {
                        final RectF tempRect19 = this.tempRect;
                        tempRect19.right = tempRect19.left + minWidth3;
                        tempRect19.bottom = tempRect19.top + tempRect19.width() / this.lockAspectRatio;
                    }
                }
                else {
                    final float height4 = this.tempRect.height();
                    final float minWidth4 = this.minWidth;
                    if (height4 <= minWidth4) {
                        final RectF tempRect20 = this.tempRect;
                        tempRect20.bottom = tempRect20.top + minWidth4;
                        tempRect20.right = tempRect20.left + tempRect20.height() * this.lockAspectRatio;
                    }
                }
            }
            this.setActualRect(this.tempRect);
            final AreaViewListener listener2 = this.listener;
            if (listener2 != null) {
                listener2.onAreaChange();
            }
            return true;
        }
        else {
            this.isDragging = false;
            final Control activeControl = this.activeControl;
            final Control none = Control.NONE;
            if (activeControl == none) {
                return false;
            }
            this.activeControl = none;
            final AreaViewListener listener3 = this.listener;
            if (listener3 != null) {
                listener3.onAreaChangeEnded();
            }
            return true;
        }
    }
    
    public void resetAnimator() {
        final Animator animator = this.animator;
        if (animator != null) {
            animator.cancel();
            this.animator = null;
        }
    }
    
    public void setActualRect(final float n) {
        this.calculateRect(this.actualRect, n);
        this.updateTouchAreas();
        this.invalidate();
    }
    
    public void setActualRect(final RectF rectF) {
        this.actualRect.set(rectF);
        this.updateTouchAreas();
        this.invalidate();
    }
    
    public void setBitmap(final Bitmap bitmap, final boolean b, final boolean freeform) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                this.freeform = freeform;
                float n;
                int n2;
                if (b) {
                    n = (float)bitmap.getHeight();
                    n2 = bitmap.getWidth();
                }
                else {
                    n = (float)bitmap.getWidth();
                    n2 = bitmap.getHeight();
                }
                float actualRect = n / n2;
                if (!this.freeform) {
                    this.lockAspectRatio = 1.0f;
                    actualRect = 1.0f;
                }
                this.setActualRect(actualRect);
            }
        }
    }
    
    public void setBottomPadding(final float bottomPadding) {
        this.bottomPadding = bottomPadding;
    }
    
    public void setDimVisibility(final boolean dimVisibile) {
        this.dimVisibile = dimVisibile;
    }
    
    public void setFrameVisibility(final boolean frameVisible) {
        this.frameVisible = frameVisible;
    }
    
    public void setFreeform(final boolean freeform) {
        this.freeform = freeform;
    }
    
    public void setGridType(final GridType gridType, final boolean b) {
        if (this.gridAnimator != null && (!b || this.gridType != gridType)) {
            this.gridAnimator.cancel();
            this.gridAnimator = null;
        }
        final GridType gridType2 = this.gridType;
        if (gridType2 == gridType) {
            return;
        }
        this.previousGridType = gridType2;
        float gridProgress;
        if ((this.gridType = gridType) == GridType.NONE) {
            gridProgress = 0.0f;
        }
        else {
            gridProgress = 1.0f;
        }
        if (!b) {
            this.gridProgress = gridProgress;
            this.invalidate();
        }
        else {
            (this.gridAnimator = (Animator)ObjectAnimator.ofFloat((Object)this, "gridProgress", new float[] { this.gridProgress, gridProgress })).setDuration(200L);
            this.gridAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    CropAreaView.this.gridAnimator = null;
                }
            });
            if (gridType == GridType.NONE) {
                this.gridAnimator.setStartDelay(200L);
            }
            this.gridAnimator.start();
        }
    }
    
    public void setListener(final AreaViewListener listener) {
        this.listener = listener;
    }
    
    public void setLockedAspectRatio(final float lockAspectRatio) {
        this.lockAspectRatio = lockAspectRatio;
    }
    
    interface AreaViewListener
    {
        void onAreaChange();
        
        void onAreaChangeBegan();
        
        void onAreaChangeEnded();
    }
    
    private enum Control
    {
        BOTTOM, 
        BOTTOM_LEFT, 
        BOTTOM_RIGHT, 
        LEFT, 
        NONE, 
        RIGHT, 
        TOP, 
        TOP_LEFT, 
        TOP_RIGHT;
    }
    
    enum GridType
    {
        MAJOR, 
        MINOR, 
        NONE;
    }
}
