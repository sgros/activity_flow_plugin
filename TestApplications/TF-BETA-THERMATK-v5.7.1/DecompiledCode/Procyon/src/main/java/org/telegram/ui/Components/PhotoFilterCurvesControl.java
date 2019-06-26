// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import java.util.Locale;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.text.TextPaint;
import android.graphics.Path;
import android.graphics.Paint;
import android.view.View;

public class PhotoFilterCurvesControl extends View
{
    private static final int CurvesSegmentBlacks = 1;
    private static final int CurvesSegmentHighlights = 4;
    private static final int CurvesSegmentMidtones = 3;
    private static final int CurvesSegmentNone = 0;
    private static final int CurvesSegmentShadows = 2;
    private static final int CurvesSegmentWhites = 5;
    private static final int GestureStateBegan = 1;
    private static final int GestureStateCancelled = 4;
    private static final int GestureStateChanged = 2;
    private static final int GestureStateEnded = 3;
    private static final int GestureStateFailed = 5;
    private int activeSegment;
    private Rect actualArea;
    private boolean checkForMoving;
    private PhotoFilterView.CurvesToolValue curveValue;
    private PhotoFilterCurvesControlDelegate delegate;
    private boolean isMoving;
    private float lastX;
    private float lastY;
    private Paint paint;
    private Paint paintCurve;
    private Paint paintDash;
    private Path path;
    private TextPaint textPaint;
    
    public PhotoFilterCurvesControl(final Context context, final PhotoFilterView.CurvesToolValue curveValue) {
        super(context);
        this.activeSegment = 0;
        this.checkForMoving = true;
        this.actualArea = new Rect();
        this.paint = new Paint(1);
        this.paintDash = new Paint(1);
        this.paintCurve = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.path = new Path();
        this.setWillNotDraw(false);
        this.curveValue = curveValue;
        this.paint.setColor(-1711276033);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        this.paint.setStyle(Paint$Style.STROKE);
        this.paintDash.setColor(-1711276033);
        this.paintDash.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.paintDash.setStyle(Paint$Style.STROKE);
        this.paintCurve.setColor(-1);
        this.paintCurve.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.paintCurve.setStyle(Paint$Style.STROKE);
        this.textPaint.setColor(-4210753);
        this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
    }
    
    private void handlePan(int n, final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        if (n != 1) {
            if (n != 2) {
                if (n == 3 || n == 4 || n == 5) {
                    this.unselectSegments();
                }
            }
            else {
                final float min = Math.min(2.0f, (this.lastY - y) / 8.0f);
                PhotoFilterView.CurvesValue curvesValue = null;
                final PhotoFilterView.CurvesToolValue curveValue = this.curveValue;
                n = curveValue.activeType;
                if (n != 0) {
                    if (n != 1) {
                        if (n != 2) {
                            if (n == 3) {
                                curvesValue = curveValue.blueCurve;
                            }
                        }
                        else {
                            curvesValue = curveValue.greenCurve;
                        }
                    }
                    else {
                        curvesValue = curveValue.redCurve;
                    }
                }
                else {
                    curvesValue = curveValue.luminanceCurve;
                }
                n = this.activeSegment;
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n == 5) {
                                    curvesValue.whitesLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.whitesLevel + min));
                                }
                            }
                            else {
                                curvesValue.highlightsLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.highlightsLevel + min));
                            }
                        }
                        else {
                            curvesValue.midtonesLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.midtonesLevel + min));
                        }
                    }
                    else {
                        curvesValue.shadowsLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.shadowsLevel + min));
                    }
                }
                else {
                    curvesValue.blacksLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.blacksLevel + min));
                }
                this.invalidate();
                final PhotoFilterCurvesControlDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.valueChanged();
                }
                this.lastX = x;
                this.lastY = y;
            }
        }
        else {
            this.selectSegmentWithPoint(x);
        }
    }
    
    private void selectSegmentWithPoint(final float n) {
        if (this.activeSegment != 0) {
            return;
        }
        final Rect actualArea = this.actualArea;
        this.activeSegment = (int)Math.floor((n - actualArea.x) / (actualArea.width / 5.0f) + 1.0f);
    }
    
    private void unselectSegments() {
        if (this.activeSegment == 0) {
            return;
        }
        this.activeSegment = 0;
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onDraw(final Canvas canvas) {
        final float n = this.actualArea.width / 5.0f;
        final int n2 = 0;
        for (int i = 0; i < 4; ++i) {
            final Rect actualArea = this.actualArea;
            final float x = actualArea.x;
            final float n3 = i * n;
            final float y = actualArea.y;
            canvas.drawLine(x + n + n3, y, x + n + n3, y + actualArea.height, this.paint);
        }
        final Rect actualArea2 = this.actualArea;
        final float x2 = actualArea2.x;
        final float y2 = actualArea2.y;
        canvas.drawLine(x2, y2 + actualArea2.height, x2 + actualArea2.width, y2, this.paintDash);
        PhotoFilterView.CurvesValue curvesValue = null;
        final int activeType = this.curveValue.activeType;
        if (activeType != 0) {
            if (activeType != 1) {
                if (activeType != 2) {
                    if (activeType == 3) {
                        this.paintCurve.setColor(-13404165);
                        curvesValue = this.curveValue.blueCurve;
                    }
                }
                else {
                    this.paintCurve.setColor(-15667555);
                    curvesValue = this.curveValue.greenCurve;
                }
            }
            else {
                this.paintCurve.setColor(-1229492);
                curvesValue = this.curveValue.redCurve;
            }
        }
        else {
            this.paintCurve.setColor(-1);
            curvesValue = this.curveValue.luminanceCurve;
        }
        for (int j = 0; j < 5; ++j) {
            String s;
            if (j != 0) {
                if (j != 1) {
                    if (j != 2) {
                        if (j != 3) {
                            if (j != 4) {
                                s = "";
                            }
                            else {
                                s = String.format(Locale.US, "%.2f", curvesValue.whitesLevel / 100.0f);
                            }
                        }
                        else {
                            s = String.format(Locale.US, "%.2f", curvesValue.highlightsLevel / 100.0f);
                        }
                    }
                    else {
                        s = String.format(Locale.US, "%.2f", curvesValue.midtonesLevel / 100.0f);
                    }
                }
                else {
                    s = String.format(Locale.US, "%.2f", curvesValue.shadowsLevel / 100.0f);
                }
            }
            else {
                s = String.format(Locale.US, "%.2f", curvesValue.blacksLevel / 100.0f);
            }
            final float measureText = this.textPaint.measureText(s);
            final Rect actualArea3 = this.actualArea;
            canvas.drawText(s, actualArea3.x + (n - measureText) / 2.0f + j * n, actualArea3.y + actualArea3.height - AndroidUtilities.dp(4.0f), (Paint)this.textPaint);
        }
        final float[] interpolateCurve = curvesValue.interpolateCurve();
        this.invalidate();
        this.path.reset();
        for (int k = n2; k < interpolateCurve.length / 2; ++k) {
            if (k == 0) {
                final Path path = this.path;
                final Rect actualArea4 = this.actualArea;
                final float x3 = actualArea4.x;
                final int n4 = k * 2;
                path.moveTo(x3 + interpolateCurve[n4] * actualArea4.width, actualArea4.y + (1.0f - interpolateCurve[n4 + 1]) * actualArea4.height);
            }
            else {
                final Path path2 = this.path;
                final Rect actualArea5 = this.actualArea;
                final float x4 = actualArea5.x;
                final int n5 = k * 2;
                path2.lineTo(x4 + interpolateCurve[n5] * actualArea5.width, actualArea5.y + (1.0f - interpolateCurve[n5 + 1]) * actualArea5.height);
            }
        }
        canvas.drawPath(this.path, this.paintCurve);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        Label_0080: {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            if (actionMasked == 5) {
                                break Label_0080;
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
                        return true;
                    }
                }
                if (this.isMoving) {
                    this.handlePan(3, motionEvent);
                    this.isMoving = false;
                }
                this.checkForMoving = true;
                return true;
            }
        }
        if (motionEvent.getPointerCount() == 1) {
            if (this.checkForMoving && !this.isMoving) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                this.lastX = x;
                this.lastY = y;
                final Rect actualArea = this.actualArea;
                final float x2 = actualArea.x;
                if (x >= x2 && x <= x2 + actualArea.width) {
                    final float y2 = actualArea.y;
                    if (y >= y2 && y <= y2 + actualArea.height) {
                        this.isMoving = true;
                    }
                }
                this.checkForMoving = false;
                if (this.isMoving) {
                    this.handlePan(1, motionEvent);
                }
            }
        }
        else if (this.isMoving) {
            this.handlePan(3, motionEvent);
            this.checkForMoving = true;
            this.isMoving = false;
        }
        return true;
    }
    
    public void setActualArea(final float x, final float y, final float width, final float height) {
        final Rect actualArea = this.actualArea;
        actualArea.x = x;
        actualArea.y = y;
        actualArea.width = width;
        actualArea.height = height;
    }
    
    public void setDelegate(final PhotoFilterCurvesControlDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface PhotoFilterCurvesControlDelegate
    {
        void valueChanged();
    }
}
