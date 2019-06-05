package org.mozilla.focus.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.mozilla.focus.activity.EditBookmarkActivity;
import org.mozilla.focus.bookmark.BookmarkAdapter;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.viewmodel.BookmarkViewModel;

public class BookmarksFragment extends PanelFragment implements BookmarkAdapter.BookmarkPanelListener {
   private BookmarkAdapter adapter;
   private View emptyView;
   private RecyclerView recyclerView;
   private BookmarkViewModel viewModel;

   // $FF: synthetic method
   public static void lambda$onActivityCreated$0(BookmarksFragment var0, List var1) {
      var0.adapter.setData(var1);
   }

   public static BookmarksFragment newInstance() {
      return new BookmarksFragment();
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      BookmarkViewModel.Factory var3 = new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance(this.getActivity())));
      LinearLayoutManager var2 = new LinearLayoutManager(this.getActivity());
      this.adapter = new BookmarkAdapter(this);
      this.recyclerView.setAdapter(this.adapter);
      this.recyclerView.setLayoutManager(var2);
      this.viewModel = (BookmarkViewModel)ViewModelProviders.of((FragmentActivity)this.getActivity(), var3).get(BookmarkViewModel.class);
      this.viewModel.getBookmarks().observe(this, new _$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA(this));
      this.onStatus(1);
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(2131492953, var2, false);
      this.recyclerView = (RecyclerView)var4.findViewById(2131296585);
      this.emptyView = var4.findViewById(2131296422);
      return var4;
   }

   public void onItemClicked(String var1) {
      ScreenNavigator.get(this.getContext()).showBrowserScreen(var1, true, false);
      this.closePanel();
      TelemetryWrapper.bookmarkOpenItem();
   }

   public void onItemDeleted(BookmarkModel var1) {
      this.viewModel.deleteBookmark(var1);
      TelemetryWrapper.bookmarkRemoveItem();
   }

   public void onItemEdited(BookmarkModel var1) {
      this.startActivity((new Intent(this.getContext(), EditBookmarkActivity.class)).putExtra("ITEM_UUID_KEY", var1.getId()));
      TelemetryWrapper.bookmarkEditItem();
   }

   public void onStatus(int var1) {
      if (var1 == 0) {
         this.recyclerView.setVisibility(8);
         this.emptyView.setVisibility(0);
      } else if (1 == var1) {
         this.recyclerView.setVisibility(0);
         this.emptyView.setVisibility(8);
      } else {
         this.recyclerView.setVisibility(8);
         this.emptyView.setVisibility(8);
      }

   }

   public void tryLoadMore() {
   }
}
