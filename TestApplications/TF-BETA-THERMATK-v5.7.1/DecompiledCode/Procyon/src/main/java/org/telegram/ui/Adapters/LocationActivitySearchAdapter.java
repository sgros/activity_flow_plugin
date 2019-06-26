// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import android.view.ViewGroup;
import org.telegram.ui.Cells.LocationCell;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.tgnet.TLRPC;
import android.content.Context;

public class LocationActivitySearchAdapter extends BaseLocationAdapter
{
    private Context mContext;
    
    public LocationActivitySearchAdapter(final Context mContext) {
        this.mContext = mContext;
    }
    
    public TLRPC.TL_messageMediaVenue getItem(final int index) {
        if (index >= 0 && index < super.places.size()) {
            return super.places.get(index);
        }
        return null;
    }
    
    @Override
    public int getItemCount() {
        return super.places.size();
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        return true;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final LocationCell locationCell = (LocationCell)viewHolder.itemView;
        final TLRPC.TL_messageMediaVenue tl_messageMediaVenue = super.places.get(n);
        final String s = super.iconUrls.get(n);
        final int size = super.places.size();
        boolean b = true;
        if (n == size - 1) {
            b = false;
        }
        locationCell.setLocation(tl_messageMediaVenue, s, b);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new RecyclerListView.Holder((View)new LocationCell(this.mContext));
    }
}
