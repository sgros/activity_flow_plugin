package android.support.p004v7.recyclerview.extensions;

import android.support.p004v7.recyclerview.extensions.AsyncDifferConfig.Builder;
import android.support.p004v7.util.AdapterListUpdateCallback;
import android.support.p004v7.util.DiffUtil.ItemCallback;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import java.util.List;

/* renamed from: android.support.v7.recyclerview.extensions.ListAdapter */
public abstract class ListAdapter<T, VH extends ViewHolder> extends Adapter<VH> {
    private final AsyncListDiffer<T> mHelper;

    protected ListAdapter(ItemCallback<T> itemCallback) {
        this.mHelper = new AsyncListDiffer(new AdapterListUpdateCallback(this), new Builder(itemCallback).build());
    }

    public void submitList(List<T> list) {
        this.mHelper.submitList(list);
    }

    /* Access modifiers changed, original: protected */
    public T getItem(int i) {
        return this.mHelper.getCurrentList().get(i);
    }

    public int getItemCount() {
        return this.mHelper.getCurrentList().size();
    }
}
