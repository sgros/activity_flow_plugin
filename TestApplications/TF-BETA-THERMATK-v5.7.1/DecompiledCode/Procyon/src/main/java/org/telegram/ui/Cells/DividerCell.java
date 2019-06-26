// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.View;

public class DividerCell extends View
{
    public DividerCell(final Context context) {
        super(context);
        this.setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        canvas.drawLine((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)this.getPaddingTop(), Theme.dividerPaint);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), this.getPaddingTop() + this.getPaddingBottom() + 1);
    }
}
