package org.mozilla.focus.fragment;

import android.arch.lifecycle.ViewModelProvider.Factory;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.EditBookmarkActivity;
import org.mozilla.focus.bookmark.BookmarkAdapter;
import org.mozilla.focus.bookmark.BookmarkAdapter.BookmarkPanelListener;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import org.mozilla.rocket.C0769R;

public class BookmarksFragment extends PanelFragment implements BookmarkPanelListener {
    private BookmarkAdapter adapter;
    private View emptyView;
    private RecyclerView recyclerView;
    private BookmarkViewModel viewModel;

    public void tryLoadMore() {
    }

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_bookmarks, viewGroup, false);
        this.recyclerView = (RecyclerView) inflate.findViewById(C0427R.C0426id.recyclerview);
        this.emptyView = inflate.findViewById(C0427R.C0426id.empty_view_container);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Factory factory = new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance(getActivity())));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        this.adapter = new BookmarkAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.viewModel = (BookmarkViewModel) ViewModelProviders.m3of(getActivity(), factory).get(BookmarkViewModel.class);
        this.viewModel.getBookmarks().observe(this, new C0686-$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA(this));
        onStatus(1);
    }

    public void onStatus(int i) {
        if (i == 0) {
            this.recyclerView.setVisibility(8);
            this.emptyView.setVisibility(0);
        } else if (1 == i) {
            this.recyclerView.setVisibility(0);
            this.emptyView.setVisibility(8);
        } else {
            this.recyclerView.setVisibility(8);
            this.emptyView.setVisibility(8);
        }
    }

    public void onItemClicked(String str) {
        ScreenNavigator.get(getContext()).showBrowserScreen(str, true, false);
        closePanel();
        TelemetryWrapper.bookmarkOpenItem();
    }

    public void onItemDeleted(BookmarkModel bookmarkModel) {
        this.viewModel.deleteBookmark(bookmarkModel);
        TelemetryWrapper.bookmarkRemoveItem();
    }

    public void onItemEdited(BookmarkModel bookmarkModel) {
        startActivity(new Intent(getContext(), EditBookmarkActivity.class).putExtra("ITEM_UUID_KEY", bookmarkModel.getId()));
        TelemetryWrapper.bookmarkEditItem();
    }
}
