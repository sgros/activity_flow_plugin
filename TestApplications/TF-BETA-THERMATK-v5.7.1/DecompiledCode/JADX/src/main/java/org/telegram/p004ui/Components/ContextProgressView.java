package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.ContextProgressView */
public class ContextProgressView extends View {
    private RectF cicleRect = new RectF();
    private int currentColorType;
    private String innerKey;
    private Paint innerPaint = new Paint(1);
    private long lastUpdateTime;
    private String outerKey;
    private Paint outerPaint = new Paint(1);
    private int radOffset = 0;

    public ContextProgressView(Context context, int i) {
        super(context);
        this.innerPaint.setStyle(Style.STROKE);
        this.innerPaint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        this.outerPaint.setStyle(Style.STROKE);
        this.outerPaint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        this.outerPaint.setStrokeCap(Cap.ROUND);
        if (i == 0) {
            this.innerKey = Theme.key_contextProgressInner1;
            this.outerKey = Theme.key_contextProgressOuter1;
        } else if (i == 1) {
            this.innerKey = Theme.key_contextProgressInner2;
            this.outerKey = Theme.key_contextProgressOuter2;
        } else if (i == 2) {
            this.innerKey = Theme.key_contextProgressInner3;
            this.outerKey = Theme.key_contextProgressOuter3;
        } else if (i == 3) {
            this.innerKey = Theme.key_contextProgressInner4;
            this.outerKey = Theme.key_contextProgressOuter4;
        }
        updateColors();
    }

    public void updateColors() {
        this.innerPaint.setColor(Theme.getColor(this.innerKey));
        this.outerPaint.setColor(Theme.getColor(this.outerKey));
        invalidate();
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (getVisibility() == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            this.radOffset = (int) (((float) this.radOffset) + (((float) (j * 360)) / 1000.0f));
            int measuredWidth = (getMeasuredWidth() / 2) - AndroidUtilities.m26dp(9.0f);
            int measuredHeight = (getMeasuredHeight() / 2) - AndroidUtilities.m26dp(9.0f);
            this.cicleRect.set((float) measuredWidth, (float) measuredHeight, (float) (measuredWidth + AndroidUtilities.m26dp(18.0f)), (float) (measuredHeight + AndroidUtilities.m26dp(18.0f)));
            canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.m26dp(9.0f), this.innerPaint);
            canvas.drawArc(this.cicleRect, (float) (this.radOffset - 90), 90.0f, false, this.outerPaint);
            invalidate();
        }
    }
}
