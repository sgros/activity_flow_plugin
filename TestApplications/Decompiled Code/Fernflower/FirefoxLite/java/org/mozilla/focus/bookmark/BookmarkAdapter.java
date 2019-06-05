package org.mozilla.focus.bookmark;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.site.SiteItemViewHolder;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public class BookmarkAdapter extends RecyclerView.Adapter {
   private List bookmarkModels;
   private BookmarkAdapter.BookmarkPanelListener listener;

   public BookmarkAdapter(BookmarkAdapter.BookmarkPanelListener var1) {
      this.listener = var1;
   }

   private BookmarkModel getItem(int var1) {
      return var1 >= 0 && this.bookmarkModels != null && this.bookmarkModels.size() > var1 ? (BookmarkModel)this.bookmarkModels.get(var1) : null;
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$0(BookmarkAdapter var0, BookmarkModel var1, View var2) {
      var0.listener.onItemClicked(var1.getUrl());
   }

   // $FF: synthetic method
   public static boolean lambda$onBindViewHolder$1(BookmarkAdapter var0, BookmarkModel var1, MenuItem var2) {
      if (var2.getItemId() == 2131296587) {
         var0.listener.onItemDeleted(var1);
      }

      if (var2.getItemId() == 2131296420) {
         var0.listener.onItemEdited(var1);
      }

      return false;
   }

   // $FF: synthetic method
   static void lambda$onBindViewHolder$2(PopupMenu var0, View var1) {
      var0.show();
      TelemetryWrapper.showBookmarkContextMenu();
   }

   public int getItemCount() {
      int var1;
      if (this.bookmarkModels != null) {
         var1 = this.bookmarkModels.size();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public void onBindViewHolder(SiteItemViewHolder var1, int var2) {
      BookmarkModel var3 = this.getItem(var2);
      if (var3 != null) {
         var1.rootView.setTag(var3.getId());
         var1.textMain.setText(var3.getTitle());
         var1.textSecondary.setText(var3.getUrl());
         var1.rootView.setOnClickListener(new _$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0(this, var3));
         PopupMenu var4 = new PopupMenu(var1.btnMore.getContext(), var1.btnMore);
         var4.setOnMenuItemClickListener(new _$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y(this, var3));
         var4.inflate(2131558400);
         var1.btnMore.setOnClickListener(new _$$Lambda$BookmarkAdapter$w4NbRco7_1oajISDwewXQSTMa54(var4));
      }
   }

   public SiteItemViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new SiteItemViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492978, var1, false));
   }

   public void setData(List var1) {
      this.bookmarkModels = var1;
      if (this.getItemCount() == 0) {
         this.listener.onStatus(0);
      } else {
         this.listener.onStatus(1);
      }

      this.notifyDataSetChanged();
   }

   public interface BookmarkPanelListener extends PanelFragmentStatusListener {
      void onItemClicked(String var1);

      void onItemDeleted(BookmarkModel var1);

      void onItemEdited(BookmarkModel var1);
   }
}
