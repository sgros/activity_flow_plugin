// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.os.Build$VERSION;
import android.graphics.Paint$Style;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.RectF;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class PhotoFilterBlurControl extends FrameLayout
{
    private static final float BlurInsetProximity;
    private static final float BlurMinimumDifference = 0.02f;
    private static final float BlurMinimumFalloff = 0.1f;
    private static final float BlurViewCenterInset;
    private static final float BlurViewRadiusInset;
    private final int GestureStateBegan;
    private final int GestureStateCancelled;
    private final int GestureStateChanged;
    private final int GestureStateEnded;
    private final int GestureStateFailed;
    private BlurViewActiveControl activeControl;
    private Size actualAreaSize;
    private float angle;
    private Paint arcPaint;
    private RectF arcRect;
    private Point centerPoint;
    private boolean checkForMoving;
    private boolean checkForZooming;
    private PhotoFilterLinearBlurControlDelegate delegate;
    private float falloff;
    private boolean isMoving;
    private boolean isZooming;
    private Paint paint;
    private float pointerScale;
    private float pointerStartX;
    private float pointerStartY;
    private float size;
    private Point startCenterPoint;
    private float startDistance;
    private float startPointerDistance;
    private float startRadius;
    private int type;
    
    static {
        BlurInsetProximity = (float)AndroidUtilities.dp(20.0f);
        BlurViewCenterInset = (float)AndroidUtilities.dp(30.0f);
        BlurViewRadiusInset = (float)AndroidUtilities.dp(30.0f);
    }
    
    public PhotoFilterBlurControl(final Context context) {
        super(context);
        this.GestureStateBegan = 1;
        this.GestureStateChanged = 2;
        this.GestureStateEnded = 3;
        this.GestureStateCancelled = 4;
        this.GestureStateFailed = 5;
        this.startCenterPoint = new Point();
        this.actualAreaSize = new Size();
        this.centerPoint = new Point(0.5f, 0.5f);
        this.falloff = 0.15f;
        this.size = 0.35f;
        this.arcRect = new RectF();
        this.pointerScale = 1.0f;
        this.checkForMoving = true;
        this.paint = new Paint(1);
        this.arcPaint = new Paint(1);
        this.setWillNotDraw(false);
        this.paint.setColor(-1);
        this.arcPaint.setColor(-1);
        this.arcPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.arcPaint.setStyle(Paint$Style.STROKE);
    }
    
    private float degreesToRadians(final float n) {
        return n * 3.1415927f / 180.0f;
    }
    
    private Point getActualCenterPoint() {
        final float n = (float)this.getWidth();
        final float width = this.actualAreaSize.width;
        final float n2 = (n - width) / 2.0f;
        final float x = this.centerPoint.x;
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final float n3 = (float)statusBarHeight;
        final float n4 = (float)this.getHeight();
        final Size actualAreaSize = this.actualAreaSize;
        final float height = actualAreaSize.height;
        final float n5 = (n4 - height) / 2.0f;
        final float width2 = actualAreaSize.width;
        return new Point(n2 + x * width, n3 + n5 - (width2 - height) / 2.0f + this.centerPoint.y * width2);
    }
    
    private float getActualInnerRadius() {
        final Size actualAreaSize = this.actualAreaSize;
        final float width = actualAreaSize.width;
        float height = actualAreaSize.height;
        if (width <= height) {
            height = width;
        }
        return height * this.falloff;
    }
    
    private float getActualOuterRadius() {
        final Size actualAreaSize = this.actualAreaSize;
        float width = actualAreaSize.width;
        final float height = actualAreaSize.height;
        if (width > height) {
            width = height;
        }
        return width * this.size;
    }
    
    private float getDistance(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return 0.0f;
        }
        final float x = motionEvent.getX(0);
        final float y = motionEvent.getY(0);
        final float x2 = motionEvent.getX(1);
        final float y2 = motionEvent.getY(1);
        final float n = x - x2;
        final float n2 = y - y2;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    private void handlePan(int n, final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final Point actualCenterPoint = this.getActualCenterPoint();
        final Point point = new Point(x - actualCenterPoint.x, y - actualCenterPoint.y);
        final float x2 = point.x;
        final float y2 = point.y;
        final float n2 = (float)Math.sqrt(x2 * x2 + y2 * y2);
        final Size actualAreaSize = this.actualAreaSize;
        float width = actualAreaSize.width;
        final float height = actualAreaSize.height;
        if (width > height) {
            width = height;
        }
        final float n3 = this.falloff * width;
        final float n4 = this.size * width;
        final double v = point.x;
        final double v2 = this.degreesToRadians(this.angle);
        Double.isNaN(v2);
        final double cos = Math.cos(v2 + 1.5707963267948966);
        Double.isNaN(v);
        final double v3 = point.y;
        final double v4 = this.degreesToRadians(this.angle);
        Double.isNaN(v4);
        final double sin = Math.sin(v4 + 1.5707963267948966);
        Double.isNaN(v3);
        final float n5 = (float)Math.abs(v * cos + v3 * sin);
        final int n6 = 0;
        final int n7 = 0;
        float blurViewRadiusInset = 0.0f;
        if (n != 1) {
            if (n != 2) {
                if (n == 3 || n == 4 || n == 5) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                    this.setSelected(false, true);
                }
            }
            else {
                n = this.type;
                if (n == 0) {
                    n = PhotoFilterBlurControl$1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
                    if (n != 1) {
                        if (n != 2) {
                            if (n != 3) {
                                if (n == 4) {
                                    final float n8 = x - this.pointerStartX;
                                    final float n9 = y - this.pointerStartY;
                                    if (x > actualCenterPoint.x) {
                                        n = 1;
                                    }
                                    else {
                                        n = 0;
                                    }
                                    final boolean b = y > actualCenterPoint.y;
                                    Label_0567: {
                                        if (n == 0 && !b) {
                                            if (Math.abs(n9) > Math.abs(n8)) {
                                                n = n7;
                                                if (n9 >= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                            else {
                                                n = n7;
                                                if (n8 <= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                        }
                                        else if (n != 0 && !b) {
                                            if (Math.abs(n9) > Math.abs(n8)) {
                                                n = n7;
                                                if (n9 <= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                            else {
                                                n = n7;
                                                if (n8 <= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                        }
                                        else if (n != 0 && b) {
                                            if (Math.abs(n9) > Math.abs(n8)) {
                                                n = n7;
                                                if (n9 <= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                            else {
                                                n = n7;
                                                if (n8 >= 0.0f) {
                                                    break Label_0567;
                                                }
                                            }
                                        }
                                        else if (Math.abs(n9) > Math.abs(n8)) {
                                            n = n7;
                                            if (n9 >= 0.0f) {
                                                break Label_0567;
                                            }
                                        }
                                        else {
                                            n = n7;
                                            if (n8 >= 0.0f) {
                                                break Label_0567;
                                            }
                                        }
                                        n = 1;
                                    }
                                    this.angle += (float)Math.sqrt(n8 * n8 + n9 * n9) * (n * 2 - 1) / 3.1415927f / 1.15f;
                                    this.pointerStartX = x;
                                    this.pointerStartY = y;
                                }
                            }
                            else {
                                this.size = Math.max(this.falloff + 0.02f, (this.startRadius + (n5 - this.startDistance)) / width);
                            }
                        }
                        else {
                            this.falloff = Math.min(Math.max(0.1f, (this.startRadius + (n5 - this.startDistance)) / width), this.size - 0.02f);
                        }
                    }
                    else {
                        final float pointerStartX = this.pointerStartX;
                        final float pointerStartY = this.pointerStartY;
                        final float n10 = (this.getWidth() - this.actualAreaSize.width) / 2.0f;
                        final float n11 = (float)this.getHeight();
                        final Size actualAreaSize2 = this.actualAreaSize;
                        final float height2 = actualAreaSize2.height;
                        final Rect rect = new Rect(n10, (n11 - height2) / 2.0f, actualAreaSize2.width, height2);
                        final float x3 = rect.x;
                        final float max = Math.max(x3, Math.min(rect.width + x3, this.startCenterPoint.x + (x - pointerStartX)));
                        final float y3 = rect.y;
                        final Point point2 = new Point(max, Math.max(y3, Math.min(rect.height + y3, this.startCenterPoint.y + (y - pointerStartY))));
                        final float x4 = point2.x;
                        final float x5 = rect.x;
                        final Size actualAreaSize3 = this.actualAreaSize;
                        final float width2 = actualAreaSize3.width;
                        this.centerPoint = new Point((x4 - x5) / width2, (point2.y - rect.y + (width2 - actualAreaSize3.height) / 2.0f) / width2);
                    }
                }
                else if (n == 1) {
                    n = PhotoFilterBlurControl$1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
                    if (n != 1) {
                        if (n != 2) {
                            if (n == 3) {
                                this.size = Math.max(this.falloff + 0.02f, (this.startRadius + (n2 - this.startDistance)) / width);
                            }
                        }
                        else {
                            this.falloff = Math.min(Math.max(0.1f, (this.startRadius + (n2 - this.startDistance)) / width), this.size - 0.02f);
                        }
                    }
                    else {
                        final float pointerStartX2 = this.pointerStartX;
                        final float pointerStartY2 = this.pointerStartY;
                        final float n12 = (this.getWidth() - this.actualAreaSize.width) / 2.0f;
                        final float n13 = (float)this.getHeight();
                        final Size actualAreaSize4 = this.actualAreaSize;
                        final float height3 = actualAreaSize4.height;
                        final Rect rect2 = new Rect(n12, (n13 - height3) / 2.0f, actualAreaSize4.width, height3);
                        final float x6 = rect2.x;
                        final float max2 = Math.max(x6, Math.min(rect2.width + x6, this.startCenterPoint.x + (x - pointerStartX2)));
                        final float y4 = rect2.y;
                        final Point point3 = new Point(max2, Math.max(y4, Math.min(rect2.height + y4, this.startCenterPoint.y + (y - pointerStartY2))));
                        final float x7 = point3.x;
                        final float x8 = rect2.x;
                        final Size actualAreaSize5 = this.actualAreaSize;
                        final float width3 = actualAreaSize5.width;
                        this.centerPoint = new Point((x7 - x8) / width3, (point3.y - rect2.y + (width3 - actualAreaSize5.height) / 2.0f) / width3);
                    }
                }
                this.invalidate();
                final PhotoFilterLinearBlurControlDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.valueChanged(this.centerPoint, this.falloff, this.size, this.degreesToRadians(this.angle) + 1.5707964f);
                }
            }
        }
        else {
            this.pointerStartX = motionEvent.getX();
            this.pointerStartY = motionEvent.getY();
            n = n6;
            if (Math.abs(n4 - n3) < PhotoFilterBlurControl.BlurInsetProximity) {
                n = 1;
            }
            float blurViewRadiusInset2;
            if (n != 0) {
                blurViewRadiusInset2 = 0.0f;
            }
            else {
                blurViewRadiusInset2 = PhotoFilterBlurControl.BlurViewRadiusInset;
            }
            if (n == 0) {
                blurViewRadiusInset = PhotoFilterBlurControl.BlurViewRadiusInset;
            }
            n = this.type;
            if (n == 0) {
                if (n2 < PhotoFilterBlurControl.BlurViewCenterInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                    this.startCenterPoint = actualCenterPoint;
                }
                else if (n5 > n3 - PhotoFilterBlurControl.BlurViewRadiusInset && n5 < n3 + blurViewRadiusInset2) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                    this.startDistance = n5;
                    this.startRadius = n3;
                }
                else if (n5 > n4 - blurViewRadiusInset && n5 < n4 + PhotoFilterBlurControl.BlurViewRadiusInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                    this.startDistance = n5;
                    this.startRadius = n4;
                }
                else {
                    final float blurViewRadiusInset3 = PhotoFilterBlurControl.BlurViewRadiusInset;
                    if (n5 <= n3 - blurViewRadiusInset3 || n5 >= n4 + blurViewRadiusInset3) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlRotation;
                    }
                }
            }
            else if (n == 1) {
                if (n2 < PhotoFilterBlurControl.BlurViewCenterInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                    this.startCenterPoint = actualCenterPoint;
                }
                else if (n2 > n3 - PhotoFilterBlurControl.BlurViewRadiusInset && n2 < n3 + blurViewRadiusInset2) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                    this.startDistance = n2;
                    this.startRadius = n3;
                }
                else if (n2 > n4 - blurViewRadiusInset && n2 < n4 + PhotoFilterBlurControl.BlurViewRadiusInset) {
                    this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                    this.startDistance = n2;
                    this.startRadius = n4;
                }
            }
            this.setSelected(true, true);
        }
    }
    
    private void handlePinch(final int n, final MotionEvent motionEvent) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3 && n != 4 && n != 5) {
                    return;
                }
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                this.setSelected(false, true);
                return;
            }
        }
        else {
            this.startPointerDistance = this.getDistance(motionEvent);
            this.pointerScale = 1.0f;
            this.activeControl = BlurViewActiveControl.BlurViewActiveControlWholeArea;
            this.setSelected(true, true);
        }
        final float distance = this.getDistance(motionEvent);
        this.pointerScale += (distance - this.startPointerDistance) / AndroidUtilities.density * 0.01f;
        this.falloff = Math.max(0.1f, this.falloff * this.pointerScale);
        this.size = Math.max(this.falloff + 0.02f, this.size * this.pointerScale);
        this.pointerScale = 1.0f;
        this.startPointerDistance = distance;
        this.invalidate();
        final PhotoFilterLinearBlurControlDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.valueChanged(this.centerPoint, this.falloff, this.size, this.degreesToRadians(this.angle) + 1.5707964f);
        }
    }
    
    private void setSelected(final boolean b, final boolean b2) {
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final Point actualCenterPoint = this.getActualCenterPoint();
        final float actualInnerRadius = this.getActualInnerRadius();
        final float actualOuterRadius = this.getActualOuterRadius();
        canvas.translate(actualCenterPoint.x, actualCenterPoint.y);
        final int type = this.type;
        if (type == 0) {
            canvas.rotate(this.angle);
            final float n = (float)AndroidUtilities.dp(6.0f);
            final float n2 = (float)AndroidUtilities.dp(12.0f);
            final float n3 = (float)AndroidUtilities.dp(1.5f);
            for (int i = 0; i < 30; ++i) {
                final float n4 = (float)i;
                final float n5 = n2 + n;
                final float n6 = n4 * n5;
                final float n7 = -actualInnerRadius;
                final float n8 = n6 + n2;
                final float n9 = n3 - actualInnerRadius;
                canvas.drawRect(n6, n7, n8, n9, this.paint);
                final float n10 = -i * n5 - n;
                final float n11 = n10 - n2;
                canvas.drawRect(n11, n7, n10, n9, this.paint);
                final float n12 = n3 + actualInnerRadius;
                canvas.drawRect(n6, actualInnerRadius, n8, n12, this.paint);
                canvas.drawRect(n11, actualInnerRadius, n10, n12, this.paint);
            }
            final float n13 = (float)AndroidUtilities.dp(6.0f);
            for (int j = 0; j < 64; ++j) {
                final float n14 = (float)j;
                final float n15 = n13 + n;
                final float n16 = n14 * n15;
                final float n17 = -actualOuterRadius;
                final float n18 = n13 + n16;
                final float n19 = n3 - actualOuterRadius;
                canvas.drawRect(n16, n17, n18, n19, this.paint);
                final float n20 = -j * n15 - n;
                final float n21 = n20 - n13;
                canvas.drawRect(n21, n17, n20, n19, this.paint);
                final float n22 = n3 + actualOuterRadius;
                canvas.drawRect(n16, actualOuterRadius, n18, n22, this.paint);
                canvas.drawRect(n21, actualOuterRadius, n20, n22, this.paint);
            }
        }
        else if (type == 1) {
            final RectF arcRect = this.arcRect;
            final float n23 = -actualInnerRadius;
            arcRect.set(n23, n23, actualInnerRadius, actualInnerRadius);
            for (int k = 0; k < 22; ++k) {
                canvas.drawArc(this.arcRect, 16.35f * k, 10.2f, false, this.arcPaint);
            }
            final RectF arcRect2 = this.arcRect;
            final float n24 = -actualOuterRadius;
            arcRect2.set(n24, n24, actualOuterRadius, actualOuterRadius);
            for (int l = 0; l < 64; ++l) {
                canvas.drawArc(this.arcRect, 5.62f * l, 3.6f, false, this.arcPaint);
            }
        }
        canvas.drawCircle(0.0f, 0.0f, (float)AndroidUtilities.dp(8.0f), this.paint);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        Label_0122: {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            if (actionMasked == 5) {
                                break Label_0122;
                            }
                            if (actionMasked != 6) {
                                return true;
                            }
                        }
                    }
                    else {
                        if (this.isMoving) {
                            this.handlePan(2, motionEvent);
                            return true;
                        }
                        if (this.isZooming) {
                            this.handlePinch(2, motionEvent);
                            return true;
                        }
                        return true;
                    }
                }
                if (this.isMoving) {
                    this.handlePan(3, motionEvent);
                    this.isMoving = false;
                }
                else if (this.isZooming) {
                    this.handlePinch(3, motionEvent);
                    this.isZooming = false;
                }
                this.checkForMoving = true;
                this.checkForZooming = true;
                return true;
            }
        }
        if (motionEvent.getPointerCount() == 1) {
            if (this.checkForMoving && !this.isMoving) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                final Point actualCenterPoint = this.getActualCenterPoint();
                final Point point = new Point(x - actualCenterPoint.x, y - actualCenterPoint.y);
                final float x2 = point.x;
                final float y2 = point.y;
                final float n = (float)Math.sqrt(x2 * x2 + y2 * y2);
                final float actualInnerRadius = this.getActualInnerRadius();
                final float actualOuterRadius = this.getActualOuterRadius();
                final boolean b = Math.abs(actualOuterRadius - actualInnerRadius) < PhotoFilterBlurControl.BlurInsetProximity;
                float blurViewRadiusInset = 0.0f;
                float blurViewRadiusInset2;
                if (b) {
                    blurViewRadiusInset2 = 0.0f;
                }
                else {
                    blurViewRadiusInset2 = PhotoFilterBlurControl.BlurViewRadiusInset;
                }
                if (!b) {
                    blurViewRadiusInset = PhotoFilterBlurControl.BlurViewRadiusInset;
                }
                final int type = this.type;
                if (type == 0) {
                    final double v = point.x;
                    final double v2 = this.degreesToRadians(this.angle);
                    Double.isNaN(v2);
                    final double cos = Math.cos(v2 + 1.5707963267948966);
                    Double.isNaN(v);
                    final double v3 = point.y;
                    final double v4 = this.degreesToRadians(this.angle);
                    Double.isNaN(v4);
                    final double sin = Math.sin(v4 + 1.5707963267948966);
                    Double.isNaN(v3);
                    final float n2 = (float)Math.abs(v * cos + v3 * sin);
                    if (n < PhotoFilterBlurControl.BlurViewCenterInset) {
                        this.isMoving = true;
                    }
                    else if (n2 > actualInnerRadius - PhotoFilterBlurControl.BlurViewRadiusInset && n2 < blurViewRadiusInset2 + actualInnerRadius) {
                        this.isMoving = true;
                    }
                    else if (n2 > actualOuterRadius - blurViewRadiusInset && n2 < PhotoFilterBlurControl.BlurViewRadiusInset + actualOuterRadius) {
                        this.isMoving = true;
                    }
                    else {
                        final float blurViewRadiusInset3 = PhotoFilterBlurControl.BlurViewRadiusInset;
                        if (n2 <= actualInnerRadius - blurViewRadiusInset3 || n2 >= actualOuterRadius + blurViewRadiusInset3) {
                            this.isMoving = true;
                        }
                    }
                }
                else if (type == 1) {
                    if (n < PhotoFilterBlurControl.BlurViewCenterInset) {
                        this.isMoving = true;
                    }
                    else if (n > actualInnerRadius - PhotoFilterBlurControl.BlurViewRadiusInset && n < actualInnerRadius + blurViewRadiusInset2) {
                        this.isMoving = true;
                    }
                    else if (n > actualOuterRadius - blurViewRadiusInset && n < actualOuterRadius + PhotoFilterBlurControl.BlurViewRadiusInset) {
                        this.isMoving = true;
                    }
                }
                this.checkForMoving = false;
                if (this.isMoving) {
                    this.handlePan(1, motionEvent);
                }
            }
        }
        else {
            if (this.isMoving) {
                this.handlePan(3, motionEvent);
                this.checkForMoving = true;
                this.isMoving = false;
            }
            if (motionEvent.getPointerCount() == 2) {
                if (this.checkForZooming && !this.isZooming) {
                    this.handlePinch(1, motionEvent);
                    this.isZooming = true;
                }
            }
            else {
                this.handlePinch(3, motionEvent);
                this.checkForZooming = true;
                this.isZooming = false;
            }
        }
        return true;
    }
    
    public void setActualAreaSize(final float width, final float height) {
        final Size actualAreaSize = this.actualAreaSize;
        actualAreaSize.width = width;
        actualAreaSize.height = height;
    }
    
    public void setDelegate(final PhotoFilterLinearBlurControlDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setType(final int type) {
        this.type = type;
        this.invalidate();
    }
    
    private enum BlurViewActiveControl
    {
        BlurViewActiveControlCenter, 
        BlurViewActiveControlInnerRadius, 
        BlurViewActiveControlNone, 
        BlurViewActiveControlOuterRadius, 
        BlurViewActiveControlRotation, 
        BlurViewActiveControlWholeArea;
    }
    
    public interface PhotoFilterLinearBlurControlDelegate
    {
        void valueChanged(final Point p0, final float p1, final float p2, final float p3);
    }
}
