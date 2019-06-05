package org.mozilla.focus.screenshot;

import android.graphics.Rect;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.ItemDecoration;
import android.support.p004v7.widget.RecyclerView.State;
import android.view.View;

public class ItemOffsetDecoration extends ItemDecoration {
    private int spacing;
    private int spanCount;

    public ItemOffsetDecoration(int i, int i2) {
        this.spanCount = i;
        this.spacing = i2;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition >= 0 && recyclerView.getAdapter().getItemViewType(childAdapterPosition) == 1) {
            childAdapterPosition = ((ScreenshotItemAdapter) recyclerView.getAdapter()).getAdjustPosition(childAdapterPosition);
            int i = childAdapterPosition % this.spanCount;
            rect.left = this.spacing - ((this.spacing * i) / this.spanCount);
            rect.right = ((i + 1) * this.spacing) / this.spanCount;
            if (childAdapterPosition < this.spanCount) {
                rect.top = this.spacing;
            }
            rect.bottom = this.spacing;
        }
    }
}
