// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;

public class GroupCreateDividerItemDecoration extends ItemDecoration
{
    private boolean searching;
    private boolean single;
    private int skipRows;
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.top = 1;
    }
    
    @Override
    public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final State state) {
        final int width = recyclerView.getWidth();
        for (int n = recyclerView.getChildCount() - ((this.single ^ true) ? 1 : 0), i = 0; i < n; ++i) {
            final View child = recyclerView.getChildAt(i);
            View child2;
            if (i < n - 1) {
                child2 = recyclerView.getChildAt(i + 1);
            }
            else {
                child2 = null;
            }
            if (recyclerView.getChildAdapterPosition(child) >= this.skipRows && !(child instanceof GroupCreateSectionCell)) {
                if (!(child2 instanceof GroupCreateSectionCell)) {
                    final int bottom = child.getBottom();
                    float n2;
                    if (LocaleController.isRTL) {
                        n2 = 0.0f;
                    }
                    else {
                        n2 = (float)AndroidUtilities.dp(72.0f);
                    }
                    final float n3 = (float)bottom;
                    int dp;
                    if (LocaleController.isRTL) {
                        dp = AndroidUtilities.dp(72.0f);
                    }
                    else {
                        dp = 0;
                    }
                    canvas.drawLine(n2, n3, (float)(width - dp), n3, Theme.dividerPaint);
                }
            }
        }
    }
    
    public void setSearching(final boolean searching) {
        this.searching = searching;
    }
    
    public void setSingle(final boolean single) {
        this.single = single;
    }
    
    public void setSkipRows(final int skipRows) {
        this.skipRows = skipRows;
    }
}
