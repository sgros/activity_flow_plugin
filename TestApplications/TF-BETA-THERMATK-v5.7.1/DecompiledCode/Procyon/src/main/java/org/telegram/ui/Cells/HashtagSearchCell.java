// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;

public class HashtagSearchCell extends TextView
{
    private boolean needDivider;
    
    public HashtagSearchCell(final Context context) {
        super(context);
        this.setGravity(16);
        this.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        this.setTextSize(1, 17.0f);
        this.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.needDivider) {
            canvas.drawLine(0.0f, (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(48.0f) + 1);
    }
    
    public void setNeedDivider(final boolean needDivider) {
        this.needDivider = needDivider;
    }
}
