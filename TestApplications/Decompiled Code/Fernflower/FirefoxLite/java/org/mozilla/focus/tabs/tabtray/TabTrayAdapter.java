package org.mozilla.focus.tabs.tabtray;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.nightmode.themed.ThemedRecyclerView;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.tabs.Session;

public class TabTrayAdapter extends RecyclerView.Adapter {
   private Session focusedTab;
   private boolean isNight;
   private HashMap localIconCache = new HashMap();
   private RequestManager requestManager;
   private TabTrayAdapter.TabClickListener tabClickListener;
   private List tabs = new ArrayList();

   TabTrayAdapter(RequestManager var1) {
      this.requestManager = var1;
   }

   private String getTitle(Session var1, TabTrayAdapter.ViewHolder var2) {
      String var3 = var1.getTitle();
      String var4 = String.valueOf(var2.websiteTitle.getText());
      if (TextUtils.isEmpty(var3)) {
         var3 = var4;
         if (TextUtils.isEmpty(var4)) {
            var3 = "";
         }

         return var3;
      } else {
         return var3;
      }
   }

   private void loadCachedFavicon(final Session var1, final TabTrayAdapter.ViewHolder var2) {
      RequestOptions var3 = (new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).dontAnimate();
      Bitmap var4 = var1.getFavicon();
      FaviconModel var5 = new FaviconModel(var1.getUrl(), DimenUtils.getFavIconType(var2.itemView.getResources(), var4), var4);
      this.requestManager.load(var5).apply(var3).listener(new RequestListener() {
         public boolean onLoadFailed(GlideException var1x, Object var2x, Target var3, boolean var4) {
            TabTrayAdapter.this.loadGeneratedFavicon(var1, var2);
            return true;
         }

         public boolean onResourceReady(Drawable var1x, Object var2x, Target var3, DataSource var4, boolean var5) {
            return false;
         }
      }).into((Target)(new SimpleTarget() {
         public void onResourceReady(Drawable var1, Transition var2x) {
            TabTrayAdapter.this.updateFavicon(var2, var1);
         }
      }));
   }

   private void loadGeneratedFavicon(Session var1, TabTrayAdapter.ViewHolder var2) {
      Character var3 = FavIconUtils.getRepresentativeCharacter(var1.getUrl());
      Bitmap var5 = var1.getFavicon();
      int var4;
      if (var5 == null) {
         var4 = -1;
      } else {
         var4 = FavIconUtils.getDominantColor(var5);
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(var3.toString());
      var6.append("_");
      var6.append(Integer.toHexString(var4));
      String var7 = var6.toString();
      if (this.localIconCache.containsKey(var7)) {
         this.updateFavicon(var2, (Drawable)this.localIconCache.get(var7));
      } else {
         BitmapDrawable var8 = new BitmapDrawable(var2.itemView.getResources(), DimenUtils.getInitialBitmap(var2.itemView.getResources(), var3, var4));
         this.localIconCache.put(var7, var8);
         this.updateFavicon(var2, var8);
      }

   }

   private void setFavicon(Session var1, TabTrayAdapter.ViewHolder var2) {
      if (!TextUtils.isEmpty(var1.getUrl())) {
         this.loadCachedFavicon(var1, var2);
      }
   }

   private void updateFavicon(TabTrayAdapter.ViewHolder var1, Drawable var2) {
      if (var2 != null) {
         var1.websiteIcon.setImageDrawable(var2);
         var1.websiteIcon.setBackgroundColor(0);
      } else {
         var1.websiteIcon.setImageResource(2131230858);
         var1.websiteIcon.setBackgroundColor(ContextCompat.getColor(var1.websiteIcon.getContext(), 2131099889));
      }

   }

   List getData() {
      return this.tabs;
   }

   Session getFocusedTab() {
      return this.focusedTab;
   }

   public int getItemCount() {
      return this.tabs.size();
   }

   public void onAttachedToRecyclerView(RecyclerView var1) {
      super.onAttachedToRecyclerView(var1);
      if (var1 instanceof ThemedRecyclerView) {
         this.isNight = ((ThemedRecyclerView)var1).isNightMode();
      }

   }

   public void onBindViewHolder(TabTrayAdapter.ViewHolder var1, int var2) {
      View var3 = var1.itemView;
      boolean var4;
      if (this.tabs.get(var2) == this.focusedTab) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3.setSelected(var4);
      Resources var5 = var1.itemView.getResources();
      Session var6 = (Session)this.tabs.get(var2);
      String var7 = this.getTitle(var6, var1);
      ThemedTextView var8 = var1.websiteTitle;
      String var9 = var7;
      if (TextUtils.isEmpty(var7)) {
         var9 = var5.getString(2131755062);
      }

      var8.setText(var9);
      if (!TextUtils.isEmpty(var6.getUrl())) {
         var1.websiteSubtitle.setText(var6.getUrl());
      }

      this.setFavicon(var6, var1);
      var1.rootView.setNightMode(this.isNight);
      var1.websiteTitle.setNightMode(this.isNight);
      var1.websiteSubtitle.setNightMode(this.isNight);
   }

   public TabTrayAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      TabTrayAdapter.ViewHolder var3 = new TabTrayAdapter.ViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492986, var1, false));
      TabTrayAdapter.InternalTabClickListener var4 = new TabTrayAdapter.InternalTabClickListener(var3, this.tabClickListener);
      var3.itemView.setOnClickListener(var4);
      var3.closeButton.setOnClickListener(var4);
      return var3;
   }

   public void onViewRecycled(TabTrayAdapter.ViewHolder var1) {
      var1.websiteTitle.setText("");
      var1.websiteSubtitle.setText("");
      this.updateFavicon(var1, (Drawable)null);
   }

   void setData(List var1) {
      this.tabs.clear();
      this.tabs.addAll(var1);
   }

   void setFocusedTab(Session var1) {
      this.focusedTab = var1;
   }

   void setTabClickListener(TabTrayAdapter.TabClickListener var1) {
      this.tabClickListener = var1;
   }

   static class InternalTabClickListener implements OnClickListener {
      private TabTrayAdapter.ViewHolder holder;
      private TabTrayAdapter.TabClickListener tabClickListener;

      InternalTabClickListener(TabTrayAdapter.ViewHolder var1, TabTrayAdapter.TabClickListener var2) {
         this.holder = var1;
         this.tabClickListener = var2;
      }

      private void dispatchOnClick(View var1, int var2) {
         int var3 = var1.getId();
         if (var3 != 2131296372) {
            if (var3 == 2131296596) {
               this.tabClickListener.onTabClick(var2);
            }
         } else {
            this.tabClickListener.onTabCloseClick(var2);
         }

      }

      public void onClick(View var1) {
         if (this.tabClickListener != null) {
            int var2 = this.holder.getAdapterPosition();
            if (var2 != -1) {
               this.dispatchOnClick(var1, var2);
            }

         }
      }
   }

   public interface TabClickListener {
      void onTabClick(int var1);

      void onTabCloseClick(int var1);
   }

   static class ViewHolder extends RecyclerView.ViewHolder {
      View closeButton;
      ThemedRelativeLayout rootView;
      ImageView websiteIcon;
      ThemedTextView websiteSubtitle;
      ThemedTextView websiteTitle;

      ViewHolder(View var1) {
         super(var1);
         this.rootView = (ThemedRelativeLayout)var1.findViewById(2131296596);
         this.websiteTitle = (ThemedTextView)var1.findViewById(2131296722);
         this.websiteSubtitle = (ThemedTextView)var1.findViewById(2131296721);
         this.closeButton = var1.findViewById(2131296372);
         this.websiteIcon = (ImageView)var1.findViewById(2131296720);
      }
   }
}
