package org.mozilla.rocket.banner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.rocket.util.LoggerWrapper;

public class BannerAdapter extends RecyclerView.Adapter {
   private List DAOs = new ArrayList();
   private Context context;
   private OnClickListener onClickListener;

   public BannerAdapter(String[] var1, OnClickListener var2) throws JSONException {
      this.onClickListener = var2;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         JSONObject var4 = new JSONObject(var1[var3]);
         BannerDAO var5 = new BannerDAO();
         var5.type = var4.getString("type");
         var5.values = var4.getJSONArray("values");
         var5.id = var4.getString("id");
         if (this.getItemViewType(var5.type) == -1) {
            LoggerWrapper.throwOrWarn("BannerAdapter", String.format(Locale.US, "Unknown view type: %s in page %d", var5.type, var3));
         } else {
            this.DAOs.add(var5);
         }
      }

   }

   private int getItemViewType(String var1) {
      byte var3;
      label28: {
         int var2 = var1.hashCode();
         if (var2 != 93508654) {
            if (var2 != 1342277043) {
               if (var2 == 1754382089 && var1.equals("single_button")) {
                  var3 = 0;
                  break label28;
               }
            } else if (var1.equals("four_sites")) {
               var3 = 1;
               break label28;
            }
         } else if (var1.equals("basic")) {
            var3 = 2;
            break label28;
         }

         var3 = -1;
      }

      switch(var3) {
      case 0:
         return 1;
      case 1:
         return 2;
      case 2:
         return 0;
      default:
         return -1;
      }
   }

   public String getFirstDAOId() {
      if (this.DAOs.size() <= 0) {
         LoggerWrapper.throwOrWarn("BannerAdapter", "Invalid banner size");
         return "NO_ID";
      } else {
         return ((BannerDAO)this.DAOs.get(0)).id;
      }
   }

   public int getItemCount() {
      return this.DAOs.size();
   }

   public int getItemViewType(int var1) {
      return this.getItemViewType(((BannerDAO)this.DAOs.get(var1)).type);
   }

   public void onAttachedToRecyclerView(RecyclerView var1) {
      this.context = var1.getContext();
   }

   public void onBindViewHolder(BannerViewHolder var1, int var2) {
      var1.onBindViewHolder(this.context, (BannerDAO)this.DAOs.get(var2));
   }

   public BannerViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      switch(var2) {
      case 1:
         return new SingleButtonViewHolder(var1, this.onClickListener);
      case 2:
         return new FourSitesViewHolder(var1, this.onClickListener);
      default:
         return new BasicViewHolder(var1, this.onClickListener);
      }
   }

   public void onDetachedFromRecyclerView(RecyclerView var1) {
      this.context = null;
   }
}
