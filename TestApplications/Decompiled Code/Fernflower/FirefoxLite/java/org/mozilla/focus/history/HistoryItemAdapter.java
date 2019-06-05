package org.mozilla.focus.history;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.site.SiteItemViewHolder;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.threadutils.ThreadUtils;

public class HistoryItemAdapter extends RecyclerView.Adapter implements OnClickListener, QueryHandler.AsyncDeleteListener, QueryHandler.AsyncQueryListener {
   private Context mContext;
   private int mCurrentCount;
   private ItemClosingPanelFragmentStatusListener mHistoryListener;
   private boolean mIsInitialQuery;
   private boolean mIsLastPage;
   private boolean mIsLoading;
   private List mItems = new ArrayList();
   private RecyclerView mRecyclerView;

   public HistoryItemAdapter(RecyclerView var1, Context var2, ItemClosingPanelFragmentStatusListener var3) {
      this.mRecyclerView = var1;
      this.mContext = var2;
      this.mHistoryListener = var3;
      this.mIsInitialQuery = true;
      this.notifyStatusListener(2);
      this.loadMoreItems();
   }

   private void add(Object var1) {
      if (this.mItems.size() > 0 && isSameDay(((Site)this.mItems.get(this.mItems.size() - 1)).getLastViewTimestamp(), ((Site)var1).getLastViewTimestamp())) {
         this.mItems.add(var1);
         this.notifyItemInserted(this.mItems.size());
      } else {
         this.mItems.add(new DateSection(((Site)var1).getLastViewTimestamp()));
         this.mItems.add(var1);
         this.notifyItemRangeInserted(this.mItems.size() - 2, 2);
      }

      ++this.mCurrentCount;
   }

   private int getItemPositionById(long var1) {
      for(int var3 = 0; var3 < this.mItems.size(); ++var3) {
         Object var4 = this.mItems.get(var3);
         if (var4 instanceof Site && var1 == ((Site)var4).getId()) {
            return var3;
         }
      }

      return -1;
   }

   private static boolean isSameDay(long var0, long var2) {
      Calendar var4 = Calendar.getInstance();
      Calendar var5 = Calendar.getInstance();
      var4.setTimeInMillis(var0);
      var5.setTimeInMillis(var2);
      boolean var6 = true;
      if (var4.get(1) != var5.get(1) || var4.get(6) != var5.get(6)) {
         var6 = false;
      }

      return var6;
   }

   // $FF: synthetic method
   public static boolean lambda$onBindViewHolder$0(HistoryItemAdapter var0, Site var1, MenuItem var2) {
      if (var2.getItemId() == 2131296342) {
         BrowsingHistoryManager.getInstance().delete(var1.getId(), var0);
         TelemetryWrapper.historyRemoveLink();
         String var4 = var1.getFavIconUri();
         if (var4 == null) {
            return false;
         }

         URI var5;
         try {
            var5 = new URI(var4);
         } catch (URISyntaxException var3) {
            var3.printStackTrace();
            return false;
         }

         ThreadUtils.postToBackgroundThread((Runnable)(new FileUtils.DeleteFileRunnable(new File(var5))));
      }

      return false;
   }

   private void loadMoreItems() {
      this.mIsLoading = true;
      BrowsingHistoryManager.getInstance().query(this.mCurrentCount, 50 - this.mCurrentCount % 50, this);
   }

   private void notifyStatusListener(int var1) {
      if (this.mHistoryListener != null) {
         this.mHistoryListener.onStatus(var1);
      }

   }

   private void remove(int var1) {
      if (var1 >= 0 && var1 < this.mItems.size()) {
         Object var2 = null;
         Object var3;
         if (var1 == 0) {
            var3 = null;
         } else {
            var3 = this.mItems.get(var1 - 1);
         }

         int var4 = var1 + 1;
         if (var4 != this.mItems.size()) {
            var2 = this.mItems.get(var4);
         }

         if (!(var3 instanceof Site) && !(var2 instanceof Site)) {
            this.mItems.remove(var1);
            List var5 = this.mItems;
            --var1;
            var5.remove(var1);
            this.notifyItemRangeRemoved(var1, 2);
         } else {
            this.mItems.remove(var1);
            this.notifyItemRemoved(var1);
         }

         --this.mCurrentCount;
      }
   }

   private void setImageViewWithDefaultBitmap(ImageView var1, String var2) {
      var1.setImageBitmap(DimenUtils.getInitialBitmap(var1.getResources(), FavIconUtils.getRepresentativeCharacter(var2), -1));
   }

   public void clear() {
      ThreadUtils.postToBackgroundThread((Runnable)(new FileUtils.DeleteFolderRunnable(FileUtils.getFaviconFolder(this.mContext))));
      BrowsingHistoryManager.getInstance().deleteAll(this);
   }

   public int getItemCount() {
      return this.mItems.size();
   }

   public int getItemViewType(int var1) {
      return this.mItems.get(var1) instanceof DateSection ? 2 : 1;
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      if (var1 instanceof SiteItemViewHolder) {
         final Site var3 = (Site)this.mItems.get(var2);
         if (var3 != null) {
            final SiteItemViewHolder var5 = (SiteItemViewHolder)var1;
            var5.rootView.setOnClickListener(this);
            var5.textMain.setText(var3.getTitle());
            var5.textSecondary.setText(var3.getUrl());
            String var4 = var3.getFavIconUri();
            if (var4 != null) {
               Glide.with(var5.imgFav.getContext()).asBitmap().load(var4).into((Target)(new SimpleTarget() {
                  public void onResourceReady(Bitmap var1, Transition var2) {
                     if (DimenUtils.iconTooBlurry(var5.imgFav.getResources(), var1.getWidth())) {
                        HistoryItemAdapter.this.setImageViewWithDefaultBitmap(var5.imgFav, var3.getUrl());
                     } else {
                        var5.imgFav.setImageBitmap(var1);
                     }

                  }
               }));
            } else {
               this.setImageViewWithDefaultBitmap(var5.imgFav, var3.getUrl());
            }

            final PopupMenu var7 = new PopupMenu(this.mContext, var5.btnMore);
            var7.setOnMenuItemClickListener(new _$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk(this, var3));
            var7.inflate(2131558402);
            var5.btnMore.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  var7.show();
                  TelemetryWrapper.showHistoryContextMenu();
               }
            });
         }
      } else if (var1 instanceof HistoryItemAdapter.DateItemViewHolder) {
         DateSection var6 = (DateSection)this.mItems.get(var2);
         if (var6 != null) {
            ((HistoryItemAdapter.DateItemViewHolder)var1).textDate.setText(DateUtils.getRelativeTimeSpanString(var6.getTimestamp(), System.currentTimeMillis(), 86400000L));
         }
      }

   }

   public void onClick(View var1) {
      int var2 = this.mRecyclerView.getChildAdapterPosition(var1);
      if (var2 != -1 && var2 < this.mItems.size()) {
         Object var3 = this.mItems.get(var2);
         if (var3 instanceof Site && this.mContext instanceof FragmentListener) {
            HomeFragmentViewState.reset();
            ScreenNavigator.get(this.mContext).showBrowserScreen(((Site)var3).getUrl(), true, false);
            this.mHistoryListener.onItemClicked();
            TelemetryWrapper.historyOpenLink();
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      if (var2 == 1) {
         return new SiteItemViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492978, var1, false));
      } else {
         return var2 == 2 ? new HistoryItemAdapter.DateItemViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492977, var1, false)) : null;
      }
   }

   public void onDeleteComplete(int var1, long var2) {
      if (var1 > 0) {
         if (var2 < 0L) {
            var1 = this.mItems.size();
            this.mItems.clear();
            this.notifyItemRangeRemoved(0, var1);
            this.notifyStatusListener(0);
         } else {
            this.remove(this.getItemPositionById(var2));
            if (this.mItems.size() == 0) {
               this.notifyStatusListener(0);
            }
         }
      }

   }

   public void onQueryComplete(List var1) {
      boolean var2;
      if (var1.size() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mIsLastPage = var2;
      if (this.mIsInitialQuery) {
         this.mIsInitialQuery = false;
      }

      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         this.add(var3.next());
      }

      if (this.mItems.size() > 0) {
         this.notifyStatusListener(1);
      } else {
         this.notifyStatusListener(0);
      }

      this.mIsLoading = false;
   }

   public void tryLoadMore() {
      if (!this.mIsLoading && !this.mIsLastPage) {
         this.loadMoreItems();
      }

   }

   private static class DateItemViewHolder extends RecyclerView.ViewHolder {
      private TextView textDate;

      public DateItemViewHolder(View var1) {
         super(var1);
         this.textDate = (TextView)var1.findViewById(2131296458);
      }
   }
}
