// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import android.view.View;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public class ItemOffsetDecoration extends ItemDecoration
{
    private int spacing;
    private int spanCount;
    
    public ItemOffsetDecoration(final int spanCount, final int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition >= 0 && recyclerView.getAdapter().getItemViewType(childAdapterPosition) == 1) {
            final int adjustPosition = ((ScreenshotItemAdapter)recyclerView.getAdapter()).getAdjustPosition(childAdapterPosition);
            final int n = adjustPosition % this.spanCount;
            rect.left = this.spacing - this.spacing * n / this.spanCount;
            rect.right = (n + 1) * this.spacing / this.spanCount;
            if (adjustPosition < this.spanCount) {
                rect.top = this.spacing;
            }
            rect.bottom = this.spacing;
        }
    }
}
