// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.content.Context;
import android.widget.FrameLayout;

public class EmptyCell extends FrameLayout
{
    int cellHeight;
    
    public EmptyCell(final Context context) {
        this(context, 8);
    }
    
    public EmptyCell(final Context context, final int cellHeight) {
        super(context);
        this.cellHeight = cellHeight;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(this.cellHeight, 1073741824));
    }
    
    public void setHeight(final int cellHeight) {
        this.cellHeight = cellHeight;
        this.requestLayout();
    }
}
