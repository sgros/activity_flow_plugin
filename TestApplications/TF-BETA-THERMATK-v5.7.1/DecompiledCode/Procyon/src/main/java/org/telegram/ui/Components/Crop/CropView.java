// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Crop;

import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.LocaleController;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.animation.TimeInterpolator;
import android.graphics.PointF;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;

public class CropView extends FrameLayout implements AreaViewListener, CropGestureListener
{
    private static final float EPSILON = 1.0E-5f;
    private static final float MAX_SCALE = 30.0f;
    private static final int RESULT_SIDE = 1280;
    private boolean animating;
    private CropAreaView areaView;
    private View backView;
    private Bitmap bitmap;
    private float bottomPadding;
    private CropGestureDetector detector;
    private boolean freeform;
    private boolean hasAspectRatioDialog;
    private ImageView imageView;
    private RectF initialAreaRect;
    private CropViewListener listener;
    private Matrix presentationMatrix;
    private RectF previousAreaRect;
    private float rotationStartScale;
    private CropState state;
    private Matrix tempMatrix;
    private CropRectangle tempRect;
    
    public CropView(final Context context) {
        super(context);
        this.previousAreaRect = new RectF();
        this.initialAreaRect = new RectF();
        this.presentationMatrix = new Matrix();
        this.tempRect = new CropRectangle();
        this.tempMatrix = new Matrix();
        this.animating = false;
        (this.backView = new View(context)).setBackgroundColor(-16777216);
        this.backView.setVisibility(4);
        this.addView(this.backView);
        (this.imageView = new ImageView(context)).setDrawingCacheEnabled(true);
        this.imageView.setScaleType(ImageView$ScaleType.MATRIX);
        this.addView((View)this.imageView);
        (this.detector = new CropGestureDetector(context)).setOnGestureListener((CropGestureDetector.CropGestureListener)this);
        (this.areaView = new CropAreaView(context)).setListener((CropAreaView.AreaViewListener)this);
        this.addView((View)this.areaView);
    }
    
    private void fillAreaView(final RectF rectF, final boolean b) {
        int statusBarHeight = 0;
        float max = Math.max(rectF.width() / this.areaView.getCropWidth(), rectF.height() / this.areaView.getCropHeight());
        boolean b2;
        if (this.state.getScale() * max > 30.0f) {
            max = 30.0f / this.state.getScale();
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        final float n = (float)statusBarHeight;
        final float n2 = (rectF.centerX() - this.imageView.getWidth() / 2) / this.areaView.getCropWidth();
        final float access$1200 = this.state.getOrientedWidth();
        final float n3 = (rectF.centerY() - (this.imageView.getHeight() - this.bottomPadding + n) / 2.0f) / this.areaView.getCropHeight();
        final float access$1201 = this.state.getOrientedHeight();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$CropView$u7JJSQis3TQtsCeT54hdvCdMU_Y(this, max, new float[] { 1.0f }, access$1200 * n2, n3 * access$1201));
        ofFloat.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (b2) {
                    CropView.this.fitContentInBounds(false, false, true);
                }
            }
        });
        this.areaView.fill(rectF, (Animator)ofFloat, true);
        this.initialAreaRect.set(rectF);
    }
    
    private void fitContentInBounds(final boolean b, final boolean b2, final boolean b3) {
        this.fitContentInBounds(b, b2, b3, false);
    }
    
    private void fitContentInBounds(final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        if (this.state == null) {
            return;
        }
        final float cropWidth = this.areaView.getCropWidth();
        final float cropHeight = this.areaView.getCropHeight();
        final float access$1200 = this.state.getOrientedWidth();
        final float access$1201 = this.state.getOrientedHeight();
        final float access$1202 = this.state.getRotation();
        final float n = (float)Math.toRadians(access$1202);
        final RectF calculateBoundingBox = this.calculateBoundingBox(cropWidth, cropHeight, access$1202);
        final RectF rect = new RectF(0.0f, 0.0f, access$1200, access$1201);
        final float n2 = (cropWidth - access$1200) / 2.0f;
        final float n3 = (cropHeight - access$1201) / 2.0f;
        final float access$1203 = this.state.getScale();
        this.tempRect.setRect(rect);
        final Matrix access$1204 = this.state.getMatrix();
        access$1204.preTranslate(n2 / access$1203, n3 / access$1203);
        this.tempMatrix.reset();
        this.tempMatrix.setTranslate(rect.centerX(), rect.centerY());
        final Matrix tempMatrix = this.tempMatrix;
        tempMatrix.setConcat(tempMatrix, access$1204);
        this.tempMatrix.preTranslate(-rect.centerX(), -rect.centerY());
        this.tempRect.applyMatrix(this.tempMatrix);
        this.tempMatrix.reset();
        this.tempMatrix.preRotate(-access$1202, access$1200 / 2.0f, access$1201 / 2.0f);
        this.tempRect.applyMatrix(this.tempMatrix);
        this.tempRect.getRect(rect);
        final PointF pointF = new PointF(this.state.getX(), this.state.getY());
        float n4;
        if (!rect.contains(calculateBoundingBox)) {
            if (b && (calculateBoundingBox.width() > rect.width() || calculateBoundingBox.height() > rect.height())) {
                n4 = this.fitScale(rect, access$1203, calculateBoundingBox.width() / this.scaleWidthToMaxSize(calculateBoundingBox, rect));
            }
            else {
                n4 = access$1203;
            }
            this.fitTranslation(rect, calculateBoundingBox, pointF, n);
        }
        else if (b2 && this.rotationStartScale > 0.0f) {
            float n5;
            if (this.state.getScale() * (n5 = calculateBoundingBox.width() / this.scaleWidthToMaxSize(calculateBoundingBox, rect)) < this.rotationStartScale) {
                n5 = 1.0f;
            }
            n4 = this.fitScale(rect, access$1203, n5);
            this.fitTranslation(rect, calculateBoundingBox, pointF, n);
        }
        else {
            n4 = access$1203;
        }
        final float a = pointF.x - this.state.getX();
        final float a2 = pointF.y - this.state.getY();
        if (b3) {
            final float n6 = n4 / access$1203;
            if (Math.abs(n6 - 1.0f) < 1.0E-5f && Math.abs(a) < 1.0E-5f && Math.abs(a2) < 1.0E-5f) {
                return;
            }
            this.animating = true;
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
            ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new _$$Lambda$CropView$wiih4K5GW50uIgzkO67FnI_u_xc(this, a, new float[] { 1.0f, 0.0f, 0.0f }, a2, n6));
            ofFloat.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    CropView.this.animating = false;
                    if (!b4) {
                        CropView.this.fitContentInBounds(b, b2, b3, true);
                    }
                }
            });
            ofFloat.setInterpolator((TimeInterpolator)this.areaView.getInterpolator());
            long duration;
            if (b4) {
                duration = 100L;
            }
            else {
                duration = 200L;
            }
            ofFloat.setDuration(duration);
            ofFloat.start();
        }
        else {
            this.state.translate(a, a2);
            this.state.scale(n4 / access$1203, 0.0f, 0.0f);
            this.updateMatrix();
        }
    }
    
    private float fitScale(final RectF rectF, final float n, final float n2) {
        final float n3 = rectF.width() * n2;
        final float n4 = rectF.height() * n2;
        final float n5 = (rectF.width() - n3) / 2.0f;
        final float n6 = (rectF.height() - n4) / 2.0f;
        final float left = rectF.left;
        final float top = rectF.top;
        rectF.set(left + n5, top + n6, left + n5 + n3, top + n6 + n4);
        return n * n2;
    }
    
    private void fitTranslation(final RectF rectF, final RectF rectF2, final PointF pointF, float n) {
        final float left = rectF2.left;
        final float top = rectF2.top;
        final float right = rectF2.right;
        final float bottom = rectF2.bottom;
        final float left2 = rectF.left;
        float n2 = left;
        float n3 = right;
        if (left2 > left) {
            n3 = right + (left2 - left);
            n2 = left2;
        }
        final float top2 = rectF.top;
        float n4 = top;
        float n5 = bottom;
        if (top2 > top) {
            n5 = bottom + (top2 - top);
            n4 = top2;
        }
        final float right2 = rectF.right;
        float n6 = n2;
        if (right2 < n3) {
            n6 = n2 + (right2 - n3);
        }
        final float bottom2 = rectF.bottom;
        float n7 = n4;
        if (bottom2 < n5) {
            n7 = n4 + (bottom2 - n5);
        }
        final float centerX = rectF2.centerX();
        final float n8 = rectF2.width() / 2.0f;
        final float centerY = rectF2.centerY();
        final float n9 = rectF2.height() / 2.0f;
        final double n10 = n;
        Double.isNaN(n10);
        final double n11 = 1.5707963267948966 - n10;
        final double sin = Math.sin(n11);
        final double n12 = centerX - (n6 + n8);
        Double.isNaN(n12);
        n = (float)(sin * n12);
        final double cos = Math.cos(n11);
        Double.isNaN(n12);
        final float n13 = (float)(cos * n12);
        Double.isNaN(n10);
        final double n14 = n10 + 1.5707963267948966;
        final double cos2 = Math.cos(n14);
        final double n15 = centerY - (n7 + n9);
        Double.isNaN(n15);
        final float n16 = (float)(cos2 * n15);
        final double sin2 = Math.sin(n14);
        Double.isNaN(n15);
        pointF.set(pointF.x + n + n16, pointF.y + n13 + (float)(sin2 * n15));
    }
    
    private void resetRotationStartScale() {
        this.rotationStartScale = 0.0f;
    }
    
    private void setLockedAspectRatio(final float lockedAspectRatio) {
        this.areaView.setLockedAspectRatio(lockedAspectRatio);
        final RectF rectF = new RectF();
        this.areaView.calculateRect(rectF, lockedAspectRatio);
        this.fillAreaView(rectF, true);
        final CropViewListener listener = this.listener;
        if (listener != null) {
            listener.onChange(false);
            this.listener.onAspectLock(true);
        }
    }
    
    public RectF calculateBoundingBox(final float n, final float n2, final float n3) {
        final RectF rectF = new RectF(0.0f, 0.0f, n, n2);
        final Matrix matrix = new Matrix();
        matrix.postRotate(n3, n / 2.0f, n2 / 2.0f);
        matrix.mapRect(rectF);
        return rectF;
    }
    
    public float getCropHeight() {
        return this.areaView.getCropHeight();
    }
    
    public float getCropLeft() {
        return this.areaView.getCropLeft();
    }
    
    public float getCropTop() {
        return this.areaView.getCropTop();
    }
    
    public float getCropWidth() {
        return this.areaView.getCropWidth();
    }
    
    public Bitmap getResult() {
        final CropState state = this.state;
        if (state != null && (state.hasChanges() || this.state.getBaseRotation() >= 1.0E-5f || !this.freeform)) {
            final RectF rectF = new RectF();
            this.areaView.getCropRect(rectF);
            final int n = (int)Math.ceil(this.scaleWidthToMaxSize(rectF, new RectF(0.0f, 0.0f, 1280.0f, 1280.0f)));
            final float n2 = (float)n;
            final int n3 = (int)Math.ceil(n2 / this.areaView.getAspectRatio());
            final Bitmap bitmap = Bitmap.createBitmap(n, n3, Bitmap$Config.ARGB_8888);
            final Matrix matrix = new Matrix();
            matrix.postTranslate(-this.state.getWidth() / 2.0f, -this.state.getHeight() / 2.0f);
            matrix.postRotate(this.state.getOrientation());
            this.state.getConcatMatrix(matrix);
            final float n4 = n2 / this.areaView.getCropWidth();
            matrix.postScale(n4, n4);
            matrix.postTranslate((float)(n / 2), (float)(n3 / 2));
            new Canvas(bitmap).drawBitmap(this.bitmap, matrix, new Paint(2));
            return bitmap;
        }
        return this.bitmap;
    }
    
    public void hide() {
        this.backView.setVisibility(4);
        this.imageView.setVisibility(4);
        this.areaView.setDimVisibility(false);
        this.areaView.setFrameVisibility(false);
        this.areaView.invalidate();
    }
    
    public void hideBackView() {
        this.backView.setVisibility(4);
    }
    
    public boolean isReady() {
        return !this.detector.isScaling() && !this.detector.isDragging() && !this.areaView.isDragging();
    }
    
    public void onAreaChange() {
        this.areaView.setGridType(GridType.MAJOR, false);
        this.state.translate(this.previousAreaRect.centerX() - this.areaView.getCropCenterX(), this.previousAreaRect.centerY() - this.areaView.getCropCenterY());
        this.updateMatrix();
        this.areaView.getCropRect(this.previousAreaRect);
        this.fitContentInBounds(true, false, false);
    }
    
    public void onAreaChangeBegan() {
        this.areaView.getCropRect(this.previousAreaRect);
        this.resetRotationStartScale();
        final CropViewListener listener = this.listener;
        if (listener != null) {
            listener.onChange(false);
        }
    }
    
    public void onAreaChangeEnded() {
        this.areaView.setGridType(GridType.NONE, true);
        this.fillAreaView(this.areaView.getTargetRectToFill(), false);
    }
    
    public void onDrag(final float n, final float n2) {
        if (this.animating) {
            return;
        }
        this.state.translate(n, n2);
        this.updateMatrix();
    }
    
    public void onFling(final float n, final float n2, final float n3, final float n4) {
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return true;
    }
    
    public void onRotationBegan() {
        this.areaView.setGridType(GridType.MINOR, false);
        if (this.rotationStartScale < 1.0E-5f) {
            this.rotationStartScale = this.state.getScale();
        }
    }
    
    public void onRotationEnded() {
        this.areaView.setGridType(GridType.NONE, true);
    }
    
    public void onScale(float n, float access$1200, float n2) {
        if (this.animating) {
            return;
        }
        float n3 = n;
        if (this.state.getScale() * n > 30.0f) {
            n3 = 30.0f / this.state.getScale();
        }
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final float n4 = (float)statusBarHeight;
        n = (access$1200 - this.imageView.getWidth() / 2) / this.areaView.getCropWidth();
        access$1200 = this.state.getOrientedWidth();
        n2 = (n2 - (this.imageView.getHeight() - this.bottomPadding - n4) / 2.0f) / this.areaView.getCropHeight();
        this.state.scale(n3, n * access$1200, n2 * this.state.getOrientedHeight());
        this.updateMatrix();
    }
    
    public void onScrollChangeBegan() {
        if (this.animating) {
            return;
        }
        this.areaView.setGridType(GridType.MAJOR, true);
        this.resetRotationStartScale();
        final CropViewListener listener = this.listener;
        if (listener != null) {
            listener.onChange(false);
        }
    }
    
    public void onScrollChangeEnded() {
        this.areaView.setGridType(GridType.NONE, true);
        this.fitContentInBounds(true, false, true);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.animating) {
            return true;
        }
        final boolean b = false;
        if (this.areaView.onTouchEvent(motionEvent)) {
            return true;
        }
        final int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1 || action == 3) {
                this.onScrollChangeEnded();
            }
        }
        else {
            this.onScrollChangeBegan();
        }
        try {
            return this.detector.onTouchEvent(motionEvent);
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    public void reset() {
        this.areaView.resetAnimator();
        this.areaView.setBitmap(this.bitmap, this.state.getBaseRotation() % 180.0f != 0.0f, this.freeform);
        final CropAreaView areaView = this.areaView;
        float lockedAspectRatio;
        if (this.freeform) {
            lockedAspectRatio = 0.0f;
        }
        else {
            lockedAspectRatio = 1.0f;
        }
        areaView.setLockedAspectRatio(lockedAspectRatio);
        this.state.reset(this.areaView, 0.0f, this.freeform);
        this.areaView.getCropRect(this.initialAreaRect);
        this.updateMatrix();
        this.resetRotationStartScale();
        final CropViewListener listener = this.listener;
        if (listener != null) {
            listener.onChange(true);
            this.listener.onAspectLock(false);
        }
    }
    
    public void rotate90Degrees() {
        if (this.state == null) {
            return;
        }
        this.areaView.resetAnimator();
        this.resetRotationStartScale();
        final float n = (this.state.getOrientation() - this.state.getBaseRotation() - 90.0f) % 360.0f;
        final boolean freeform = this.freeform;
        final boolean b = true;
        boolean b2;
        if (freeform && this.areaView.getLockAspectRatio() > 0.0f) {
            final CropAreaView areaView = this.areaView;
            areaView.setLockedAspectRatio(1.0f / areaView.getLockAspectRatio());
            final CropAreaView areaView2 = this.areaView;
            areaView2.setActualRect(areaView2.getLockAspectRatio());
            b2 = false;
        }
        else {
            this.areaView.setBitmap(this.bitmap, (this.state.getBaseRotation() + n) % 180.0f != 0.0f, this.freeform);
            b2 = freeform;
        }
        this.state.reset(this.areaView, n, b2);
        this.updateMatrix();
        final CropViewListener listener = this.listener;
        if (listener != null) {
            listener.onChange(n == 0.0f && this.areaView.getLockAspectRatio() == 0.0f && b);
        }
    }
    
    public float scaleWidthToMaxSize(final RectF rectF, final RectF rectF2) {
        float width;
        if ((float)Math.floor(rectF.height() * (width = rectF2.width()) / rectF.width()) > rectF2.height()) {
            width = (float)Math.floor(rectF2.height() * rectF.width() / rectF.height());
        }
        return width;
    }
    
    public void setAspectRatio(final float actualRect) {
        this.areaView.setActualRect(actualRect);
    }
    
    public void setBitmap(final Bitmap bitmap, final int n, final boolean freeform, final boolean b) {
        this.freeform = freeform;
        if (bitmap == null) {
            this.bitmap = null;
            this.state = null;
            this.imageView.setImageDrawable((Drawable)null);
        }
        else {
            this.bitmap = bitmap;
            final CropState state = this.state;
            if (state != null && b) {
                state.updateBitmap(this.bitmap, n);
            }
            else {
                this.state = new CropState(this.bitmap, n);
                this.imageView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                    public boolean onPreDraw() {
                        CropView.this.reset();
                        CropView.this.imageView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                        return false;
                    }
                });
            }
            this.imageView.setImageBitmap(this.bitmap);
        }
    }
    
    public void setBottomPadding(final float n) {
        this.bottomPadding = n;
        this.areaView.setBottomPadding(n);
    }
    
    public void setFreeform(final boolean b) {
        this.areaView.setFreeform(b);
        this.freeform = b;
    }
    
    public void setListener(final CropViewListener listener) {
        this.listener = listener;
    }
    
    public void setRotation(final float n) {
        this.state.rotate(n - this.state.getRotation(), 0.0f, 0.0f);
        this.fitContentInBounds(true, true, false);
    }
    
    public void show() {
        this.backView.setVisibility(0);
        this.imageView.setVisibility(0);
        this.areaView.setDimVisibility(true);
        this.areaView.setFrameVisibility(true);
        this.areaView.invalidate();
    }
    
    public void showAspectRatioDialog() {
        if (this.areaView.getLockAspectRatio() > 0.0f) {
            this.areaView.setLockedAspectRatio(0.0f);
            final CropViewListener listener = this.listener;
            if (listener != null) {
                listener.onAspectLock(false);
            }
            return;
        }
        if (this.hasAspectRatioDialog) {
            return;
        }
        this.hasAspectRatioDialog = true;
        final String[] array = new String[8];
        final Integer[][] array2 = { { 3, 2 }, { 5, 3 }, { 4, 3 }, { 5, 4 }, { 7, 5 }, { 16, 9 } };
        array[0] = LocaleController.getString("CropOriginal", 2131559177);
        array[1] = LocaleController.getString("CropSquare", 2131559179);
        final int length = array2.length;
        int i = 0;
        int n = 2;
        while (i < length) {
            final Integer[] array3 = array2[i];
            if (this.areaView.getAspectRatio() > 1.0f) {
                array[n] = String.format("%d:%d", array3[0], array3[1]);
            }
            else {
                array[n] = String.format("%d:%d", array3[1], array3[0]);
            }
            ++n;
            ++i;
        }
        final AlertDialog create = new AlertDialog.Builder(this.getContext()).setItems(array, (DialogInterface$OnClickListener)new _$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM(this, array2)).create();
        create.setCanceledOnTouchOutside(true);
        create.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$CropView$MBImByPOxBcWFdcV4k41dkfucIY(this));
        create.show();
    }
    
    public void showBackView() {
        this.backView.setVisibility(0);
    }
    
    public void updateLayout() {
        final float cropWidth = this.areaView.getCropWidth();
        final CropState state = this.state;
        if (state != null) {
            this.areaView.calculateRect(this.initialAreaRect, state.getWidth() / this.state.getHeight());
            final CropAreaView areaView = this.areaView;
            areaView.setActualRect(areaView.getAspectRatio());
            this.areaView.getCropRect(this.previousAreaRect);
            this.state.scale(this.areaView.getCropWidth() / cropWidth, 0.0f, 0.0f);
            this.updateMatrix();
        }
    }
    
    public void updateMatrix() {
        this.presentationMatrix.reset();
        this.presentationMatrix.postTranslate(-this.state.getWidth() / 2.0f, -this.state.getHeight() / 2.0f);
        this.presentationMatrix.postRotate(this.state.getOrientation());
        this.state.getConcatMatrix(this.presentationMatrix);
        this.presentationMatrix.postTranslate(this.areaView.getCropCenterX(), this.areaView.getCropCenterY());
        this.imageView.setImageMatrix(this.presentationMatrix);
    }
    
    public void willShow() {
        this.areaView.setFrameVisibility(true);
        this.areaView.setDimVisibility(true);
        this.areaView.invalidate();
    }
    
    private class CropRectangle
    {
        float[] coords;
        
        CropRectangle() {
            this.coords = new float[8];
        }
        
        void applyMatrix(final Matrix matrix) {
            matrix.mapPoints(this.coords);
        }
        
        void getRect(final RectF rectF) {
            final float[] coords = this.coords;
            rectF.set(coords[0], coords[1], coords[2], coords[7]);
        }
        
        void setRect(final RectF rectF) {
            final float[] coords = this.coords;
            final float left = rectF.left;
            coords[0] = left;
            final float top = rectF.top;
            coords[1] = top;
            final float right = rectF.right;
            coords[2] = right;
            coords[3] = top;
            coords[4] = right;
            final float bottom = rectF.bottom;
            coords[5] = bottom;
            coords[6] = left;
            coords[7] = bottom;
        }
    }
    
    private class CropState
    {
        private float baseRotation;
        private float height;
        private Matrix matrix;
        private float minimumScale;
        private float orientation;
        private float rotation;
        private float scale;
        private float width;
        private float x;
        private float y;
        
        private CropState(final Bitmap bitmap, final int n) {
            this.width = (float)bitmap.getWidth();
            this.height = (float)bitmap.getHeight();
            this.x = 0.0f;
            this.y = 0.0f;
            this.scale = 1.0f;
            this.baseRotation = (float)n;
            this.rotation = 0.0f;
            this.matrix = new Matrix();
        }
        
        private float getBaseRotation() {
            return this.baseRotation;
        }
        
        private void getConcatMatrix(final Matrix matrix) {
            matrix.postConcat(this.matrix);
        }
        
        private float getHeight() {
            return this.height;
        }
        
        private Matrix getMatrix() {
            final Matrix matrix = new Matrix();
            matrix.set(this.matrix);
            return matrix;
        }
        
        private float getMinimumScale() {
            return this.minimumScale;
        }
        
        private float getOrientation() {
            return this.orientation + this.baseRotation;
        }
        
        private float getOrientedHeight() {
            float n;
            if ((this.orientation + this.baseRotation) % 180.0f != 0.0f) {
                n = this.width;
            }
            else {
                n = this.height;
            }
            return n;
        }
        
        private float getOrientedWidth() {
            float n;
            if ((this.orientation + this.baseRotation) % 180.0f != 0.0f) {
                n = this.height;
            }
            else {
                n = this.width;
            }
            return n;
        }
        
        private float getRotation() {
            return this.rotation;
        }
        
        private float getScale() {
            return this.scale;
        }
        
        private float getWidth() {
            return this.width;
        }
        
        private float getX() {
            return this.x;
        }
        
        private float getY() {
            return this.y;
        }
        
        private boolean hasChanges() {
            return Math.abs(this.x) > 1.0E-5f || Math.abs(this.y) > 1.0E-5f || Math.abs(this.scale - this.minimumScale) > 1.0E-5f || Math.abs(this.rotation) > 1.0E-5f || Math.abs(this.orientation) > 1.0E-5f;
        }
        
        private void reset(final CropAreaView cropAreaView, float scale, final boolean b) {
            this.matrix.reset();
            this.x = 0.0f;
            this.y = 0.0f;
            this.rotation = 0.0f;
            this.orientation = scale;
            this.updateMinimumScale();
            this.scale = this.minimumScale;
            final Matrix matrix = this.matrix;
            scale = this.scale;
            matrix.postScale(scale, scale);
        }
        
        private void rotate(final float n, final float n2, final float n3) {
            this.rotation += n;
            this.matrix.postRotate(n, n2, n3);
        }
        
        private void scale(final float n, final float n2, final float n3) {
            this.scale *= n;
            this.matrix.postScale(n, n, n2, n3);
        }
        
        private void translate(final float n, final float n2) {
            this.x += n;
            this.y += n2;
            this.matrix.postTranslate(n, n2);
        }
        
        private void updateBitmap(final Bitmap bitmap, final int n) {
            this.scale *= this.width / bitmap.getWidth();
            this.width = (float)bitmap.getWidth();
            this.height = (float)bitmap.getHeight();
            this.updateMinimumScale();
            final float[] array = new float[9];
            this.matrix.getValues(array);
            this.matrix.reset();
            final Matrix matrix = this.matrix;
            final float scale = this.scale;
            matrix.postScale(scale, scale);
            this.matrix.postTranslate(array[2], array[5]);
            CropView.this.updateMatrix();
        }
        
        private void updateMinimumScale() {
            float n;
            if ((this.orientation + this.baseRotation) % 180.0f != 0.0f) {
                n = this.height;
            }
            else {
                n = this.width;
            }
            float n2;
            if ((this.orientation + this.baseRotation) % 180.0f != 0.0f) {
                n2 = this.width;
            }
            else {
                n2 = this.height;
            }
            if (CropView.this.freeform) {
                this.minimumScale = CropView.this.areaView.getCropWidth() / n;
            }
            else {
                this.minimumScale = Math.max(CropView.this.areaView.getCropWidth() / n, CropView.this.areaView.getCropHeight() / n2);
            }
        }
    }
    
    public interface CropViewListener
    {
        void onAspectLock(final boolean p0);
        
        void onChange(final boolean p0);
    }
}
