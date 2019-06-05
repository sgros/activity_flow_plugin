package org.mozilla.focus.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinViewWrapper;

class TopSiteAdapter extends RecyclerView.Adapter {
   private final OnClickListener clickListener;
   private final OnLongClickListener longClickListener;
   private final PinSiteManager pinSiteManager;
   private List sites = new ArrayList();

   TopSiteAdapter(List var1, OnClickListener var2, OnLongClickListener var3, PinSiteManager var4) {
      this.sites.addAll(var1);
      this.clickListener = var2;
      this.longClickListener = var3;
      this.pinSiteManager = var4;
   }

   private int addWhiteToColorCode(int var1, float var2) {
      int var3 = (int)((float)var1 + var2 * 255.0F / 2.0F);
      var1 = var3;
      if (var3 > 255) {
         var1 = 255;
      }

      return var1;
   }

   private int calculateBackgroundColor(Bitmap var1) {
      int var2 = FavIconUtils.getDominantColor(var1);
      return (-16777216 & var2) + (this.addWhiteToColorCode((16711680 & var2) >> 16, 0.25F) << 16) + (this.addWhiteToColorCode(('\uff00' & var2) >> 8, 0.25F) << 8) + this.addWhiteToColorCode(var2 & 255, 0.25F);
   }

   private Bitmap createFavicon(Resources var1, String var2, int var3) {
      return DimenUtils.getInitialBitmap(var1, FavIconUtils.getRepresentativeCharacter(var2), var3);
   }

   private Bitmap getBestFavicon(Resources var1, String var2, Bitmap var3) {
      if (var3 == null) {
         return this.createFavicon(var1, var2, -1);
      } else {
         return DimenUtils.iconTooBlurry(var1, var3.getWidth()) ? this.createFavicon(var1, var2, FavIconUtils.getDominantColor(var3)) : var3;
      }
   }

   private Bitmap getFavicon(Context var1, Site var2) {
      String var3 = var2.getFavIconUri();
      Bitmap var4;
      if (var3 != null) {
         var4 = FavIconUtils.getBitmapFromUri(var1, var3);
      } else {
         var4 = null;
      }

      return this.getBestFavicon(var1.getResources(), var2.getUrl(), var4);
   }

   public int getItemCount() {
      return this.sites.size();
   }

   public void onBindViewHolder(SiteViewHolder var1, int var2) {
      Site var3 = (Site)this.sites.get(var2);
      var1.text.setText(var3.getTitle());
      ThreadPolicy var4 = StrictMode.allowThreadDiskWrites();
      Bitmap var5 = this.getFavicon(var1.itemView.getContext(), var3);
      StrictMode.setThreadPolicy(var4);
      AppCompatImageView var8 = var1.img;
      byte var7 = 0;
      var8.setVisibility(0);
      var1.img.setImageBitmap(var5);
      int var6 = this.calculateBackgroundColor(var5);
      ViewCompat.setBackgroundTintList(var1.img, ColorStateList.valueOf(var6));
      PinViewWrapper var9 = var1.pinView;
      if (!this.pinSiteManager.isPinned(var3)) {
         var7 = 8;
      }

      var9.setVisibility(var7);
      var1.pinView.setPinColor(var6);
      var1.itemView.setTag(var3);
      if (this.clickListener != null) {
         var1.itemView.setOnClickListener(this.clickListener);
      }

      if (this.longClickListener != null) {
         var1.itemView.setOnLongClickListener(this.longClickListener);
      }

   }

   public SiteViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new SiteViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492987, var1, false));
   }

   public void setSites(List var1) {
      this.sites = var1;
      this.notifyDataSetChanged();
   }
}
