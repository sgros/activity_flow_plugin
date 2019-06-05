package org.mozilla.focus.screenshot;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.focus.glide.GlideApp;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public class ScreenshotItemAdapter extends RecyclerView.Adapter implements OnClickListener, QueryHandler.AsyncQueryListener {
   private Activity mActivity;
   private int mCurrentCount;
   private boolean mIsInitialQuery;
   private boolean mIsLastPage;
   private boolean mIsLoading;
   private List mItems = new ArrayList();
   private GridLayoutManager mLayoutManager;
   private RecyclerView mRecyclerView;
   GridLayoutManager.SpanSizeLookup mSpanSizeHelper = new GridLayoutManager.SpanSizeLookup() {
      public int getSpanSize(int var1) {
         switch(ScreenshotItemAdapter.this.getItemViewType(var1)) {
         case 1:
            return 1;
         case 2:
            return 3;
         default:
            return 1;
         }
      }
   };
   private PanelFragmentStatusListener mStatusListener;

   public ScreenshotItemAdapter(RecyclerView var1, Activity var2, PanelFragmentStatusListener var3, GridLayoutManager var4) {
      this.mRecyclerView = var1;
      this.mActivity = var2;
      this.mStatusListener = var3;
      this.mLayoutManager = var4;
      this.mLayoutManager.setSpanSizeLookup(this.mSpanSizeHelper);
      this.mIsInitialQuery = true;
      this.notifyStatusListener(2);
      this.loadMoreItems();
   }

   private void add(Object var1) {
      if (this.mItems.size() > 0 && isSameDay(((Screenshot)this.mItems.get(this.mItems.size() - 1)).getTimestamp(), ((Screenshot)var1).getTimestamp())) {
         this.mItems.add(var1);
         this.notifyItemInserted(this.mItems.size());
      } else {
         this.mItems.add(new DateSection(((Screenshot)var1).getTimestamp()));
         this.mItems.add(var1);
         this.notifyItemRangeInserted(this.mItems.size() - 2, 2);
      }

      ++this.mCurrentCount;
   }

   private int getItemPositionById(long var1) {
      for(int var3 = 0; var3 < this.mItems.size(); ++var3) {
         Object var4 = this.mItems.get(var3);
         if (var4 instanceof Screenshot && var1 == ((Screenshot)var4).getId()) {
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

   private void loadMoreItems() {
      this.mIsLoading = true;
      ScreenshotManager.getInstance().query(this.mCurrentCount, 20 - this.mCurrentCount % 20, this);
   }

   private void notifyStatusListener(int var1) {
      if (this.mStatusListener != null) {
         this.mStatusListener.onStatus(var1);
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

         if (!(var3 instanceof Screenshot) && !(var2 instanceof Screenshot)) {
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

   public int getAdjustPosition(int var1) {
      int var2 = var1;
      if (var1 < this.mItems.size()) {
         var2 = 0;

         int var3;
         for(var3 = 0; var2 <= var1; ++var2) {
            if (this.mItems.get(var2) instanceof DateSection) {
               var3 = var2;
            }
         }

         if (var3 == 0) {
            var2 = var1 - 1;
         } else {
            var2 = var1 - 1 - var3;
         }
      }

      return var2;
   }

   public int getItemCount() {
      return this.mItems.size();
   }

   public int getItemViewType(int var1) {
      return this.mItems.get(var1) instanceof DateSection ? 2 : 1;
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      if (var1 instanceof ScreenshotItemAdapter.GirdItemViewHolder) {
         ScreenshotItemAdapter.GirdItemViewHolder var4 = (ScreenshotItemAdapter.GirdItemViewHolder)var1;
         var4.rootView.setOnClickListener(this);
         Screenshot var3 = (Screenshot)this.mItems.get(var2);
         GlideApp.with(this.mActivity).asBitmap().placeholder(2131230955).fitCenter().load(var3.getImageUri()).into(var4.img);
      } else if (var1 instanceof ScreenshotItemAdapter.DateItemViewHolder) {
         DateSection var5 = (DateSection)this.mItems.get(var2);
         if (var5 != null) {
            ((ScreenshotItemAdapter.DateItemViewHolder)var1).textDate.setText(DateUtils.getRelativeTimeSpanString(var5.getTimestamp(), System.currentTimeMillis(), 86400000L));
         }
      }

   }

   public void onClick(View var1) {
      int var2 = this.mRecyclerView.getChildLayoutPosition(var1);
      if (var2 != -1) {
         Object var3 = this.mItems.get(var2);
         if (var3 instanceof Screenshot) {
            ScreenshotViewerActivity.goScreenshotViewerActivityOnResult(this.mActivity, (Screenshot)var3);
            TelemetryWrapper.openCapture();
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      if (var2 == 1) {
         return new ScreenshotItemAdapter.GirdItemViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492981, var1, false));
      } else {
         return var2 == 2 ? new ScreenshotItemAdapter.DateItemViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492980, var1, false)) : null;
      }
   }

   public void onItemDelete(long var1) {
      if (var1 >= 0L) {
         this.remove(this.getItemPositionById(var1));
         if (this.mItems.size() == 0) {
            this.notifyStatusListener(0);
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
         this.textDate = (TextView)var1.findViewById(2131296608);
      }
   }

   private static class GirdItemViewHolder extends RecyclerView.ViewHolder {
      private ImageView img;
      private View rootView;

      public GirdItemViewHolder(View var1) {
         super(var1);
         this.img = (ImageView)var1.findViewById(2131296604);
         this.rootView = var1.findViewById(2131296606);
      }
   }
}
