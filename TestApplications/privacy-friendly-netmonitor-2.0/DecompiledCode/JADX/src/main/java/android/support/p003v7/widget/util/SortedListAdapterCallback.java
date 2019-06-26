package android.support.p003v7.widget.util;

import android.support.p003v7.util.SortedList.Callback;
import android.support.p003v7.widget.RecyclerView.C0359Adapter;

/* renamed from: android.support.v7.widget.util.SortedListAdapterCallback */
public abstract class SortedListAdapterCallback<T2> extends Callback<T2> {
    final C0359Adapter mAdapter;

    public SortedListAdapterCallback(C0359Adapter c0359Adapter) {
        this.mAdapter = c0359Adapter;
    }

    public void onInserted(int i, int i2) {
        this.mAdapter.notifyItemRangeInserted(i, i2);
    }

    public void onRemoved(int i, int i2) {
        this.mAdapter.notifyItemRangeRemoved(i, i2);
    }

    public void onMoved(int i, int i2) {
        this.mAdapter.notifyItemMoved(i, i2);
    }

    public void onChanged(int i, int i2) {
        this.mAdapter.notifyItemRangeChanged(i, i2);
    }
}
