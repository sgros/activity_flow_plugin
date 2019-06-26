package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Components.RecyclerListView;

public class LocationActivitySearchAdapter extends BaseLocationAdapter {
   private Context mContext;

   public LocationActivitySearchAdapter(Context var1) {
      this.mContext = var1;
   }

   public TLRPC.TL_messageMediaVenue getItem(int var1) {
      return var1 >= 0 && var1 < super.places.size() ? (TLRPC.TL_messageMediaVenue)super.places.get(var1) : null;
   }

   public int getItemCount() {
      return super.places.size();
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      return true;
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      LocationCell var3 = (LocationCell)var1.itemView;
      TLRPC.TL_messageMediaVenue var7 = (TLRPC.TL_messageMediaVenue)super.places.get(var2);
      String var4 = (String)super.iconUrls.get(var2);
      int var5 = super.places.size();
      boolean var6 = true;
      if (var2 == var5 - 1) {
         var6 = false;
      }

      var3.setLocation(var7, var4, var6);
   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new RecyclerListView.Holder(new LocationCell(this.mContext));
   }
}
