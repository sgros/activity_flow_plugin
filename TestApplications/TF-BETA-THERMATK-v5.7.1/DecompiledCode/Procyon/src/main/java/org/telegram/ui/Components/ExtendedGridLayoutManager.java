// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.util.SparseIntArray;
import androidx.recyclerview.widget.GridLayoutManager;

public class ExtendedGridLayoutManager extends GridLayoutManager
{
    private int calculatedWidth;
    private int firstRowMax;
    private SparseIntArray itemSpans;
    private SparseIntArray itemsToRow;
    private int rowsCount;
    
    public ExtendedGridLayoutManager(final Context context, final int n) {
        super(context, n);
        this.itemSpans = new SparseIntArray();
        this.itemsToRow = new SparseIntArray();
    }
    
    private void checkLayout() {
        if (this.itemSpans.size() != this.getFlowItemCount() || this.calculatedWidth != ((RecyclerView.LayoutManager)this).getWidth()) {
            this.calculatedWidth = ((RecyclerView.LayoutManager)this).getWidth();
            this.prepareLayout((float)((RecyclerView.LayoutManager)this).getWidth());
        }
    }
    
    private void prepareLayout(final float n) {
        float n2 = n;
        if (n == 0.0f) {
            n2 = 100.0f;
        }
        this.itemSpans.clear();
        this.itemsToRow.clear();
        this.rowsCount = 0;
        this.firstRowMax = 0;
        final int dp = AndroidUtilities.dp(100.0f);
        final int flowItemCount = this.getFlowItemCount();
        int spanCount;
        final int a = spanCount = this.getSpanCount();
        int i = 0;
        int n3 = 0;
        while (i < flowItemCount) {
            final Size sizeForItem = this.sizeForItem(i);
            final int min = Math.min(a, (int)Math.floor(a * (sizeForItem.width / sizeForItem.height * dp / n2)));
            int n8;
            int n9;
            int n10;
            if (spanCount < min || (min > 33 && spanCount < min - 15)) {
                if (spanCount != 0) {
                    final int n4 = spanCount / n3;
                    int n6;
                    final int n5 = n6 = i - n3;
                    while (true) {
                        final int n7 = n5 + n3;
                        if (n6 >= n7) {
                            break;
                        }
                        if (n6 == n7 - 1) {
                            final SparseIntArray itemSpans = this.itemSpans;
                            itemSpans.put(n6, itemSpans.get(n6) + spanCount);
                        }
                        else {
                            final SparseIntArray itemSpans2 = this.itemSpans;
                            itemSpans2.put(n6, itemSpans2.get(n6) + n4);
                        }
                        spanCount -= n4;
                        ++n6;
                    }
                    this.itemsToRow.put(i - 1, this.rowsCount);
                }
                ++this.rowsCount;
                n8 = a;
                n9 = 0;
                n10 = min;
            }
            else {
                n8 = spanCount;
                n9 = n3;
                if (spanCount < (n10 = min)) {
                    n10 = spanCount;
                    n9 = n3;
                    n8 = spanCount;
                }
            }
            if (this.rowsCount == 0) {
                this.firstRowMax = Math.max(this.firstRowMax, i);
            }
            if (i == flowItemCount - 1) {
                this.itemsToRow.put(i, this.rowsCount);
            }
            n3 = n9 + 1;
            spanCount = n8 - n10;
            this.itemSpans.put(i, n10);
            ++i;
        }
        if (flowItemCount != 0) {
            ++this.rowsCount;
        }
    }
    
    private Size sizeForItem(final int n) {
        final Size sizeForItem = this.getSizeForItem(n);
        if (sizeForItem.width == 0.0f) {
            sizeForItem.width = 100.0f;
        }
        if (sizeForItem.height == 0.0f) {
            sizeForItem.height = 100.0f;
        }
        final float n2 = sizeForItem.width / sizeForItem.height;
        if (n2 > 4.0f || n2 < 0.2f) {
            final float max = Math.max(sizeForItem.width, sizeForItem.height);
            sizeForItem.width = max;
            sizeForItem.height = max;
        }
        return sizeForItem;
    }
    
    protected int getFlowItemCount() {
        return ((RecyclerView.LayoutManager)this).getItemCount();
    }
    
    public int getRowsCount(final int n) {
        if (this.rowsCount == 0) {
            this.prepareLayout((float)n);
        }
        return this.rowsCount;
    }
    
    protected Size getSizeForItem(final int n) {
        return new Size(100.0f, 100.0f);
    }
    
    public int getSpanSizeForItem(final int n) {
        this.checkLayout();
        return this.itemSpans.get(n);
    }
    
    public boolean isFirstRow(final int n) {
        this.checkLayout();
        return n <= this.firstRowMax;
    }
    
    public boolean isLastInRow(final int n) {
        this.checkLayout();
        return this.itemsToRow.get(n, Integer.MAX_VALUE) != Integer.MAX_VALUE;
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
