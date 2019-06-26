// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.graphics.Paint$Cap;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class ContextProgressView extends View
{
    private RectF cicleRect;
    private int currentColorType;
    private String innerKey;
    private Paint innerPaint;
    private long lastUpdateTime;
    private String outerKey;
    private Paint outerPaint;
    private int radOffset;
    
    public ContextProgressView(final Context context, final int n) {
        super(context);
        this.innerPaint = new Paint(1);
        this.outerPaint = new Paint(1);
        this.cicleRect = new RectF();
        this.radOffset = 0;
        this.innerPaint.setStyle(Paint$Style.STROKE);
        this.innerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.outerPaint.setStyle(Paint$Style.STROKE);
        this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.outerPaint.setStrokeCap(Paint$Cap.ROUND);
        if (n == 0) {
            this.innerKey = "contextProgressInner1";
            this.outerKey = "contextProgressOuter1";
        }
        else if (n == 1) {
            this.innerKey = "contextProgressInner2";
            this.outerKey = "contextProgressOuter2";
        }
        else if (n == 2) {
            this.innerKey = "contextProgressInner3";
            this.outerKey = "contextProgressOuter3";
        }
        else if (n == 3) {
            this.innerKey = "contextProgressInner4";
            this.outerKey = "contextProgressOuter4";
        }
        this.updateColors();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        final long lastUpdateTime = this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        this.radOffset += (int)((currentTimeMillis - lastUpdateTime) * 360L / 1000.0f);
        final int n = this.getMeasuredWidth() / 2 - AndroidUtilities.dp(9.0f);
        final int n2 = this.getMeasuredHeight() / 2 - AndroidUtilities.dp(9.0f);
        this.cicleRect.set((float)n, (float)n2, (float)(n + AndroidUtilities.dp(18.0f)), (float)(n2 + AndroidUtilities.dp(18.0f)));
        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(9.0f), this.innerPaint);
        canvas.drawArc(this.cicleRect, (float)(this.radOffset - 90), 90.0f, false, this.outerPaint);
        this.invalidate();
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public void updateColors() {
        this.innerPaint.setColor(Theme.getColor(this.innerKey));
        this.outerPaint.setColor(Theme.getColor(this.outerKey));
        this.invalidate();
    }
}
