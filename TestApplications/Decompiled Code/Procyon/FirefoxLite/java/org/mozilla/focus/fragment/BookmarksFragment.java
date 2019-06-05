// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.content.Intent;
import org.mozilla.focus.activity.EditBookmarkActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import org.mozilla.focus.repository.BookmarkRepository;
import android.content.Context;
import org.mozilla.focus.persistence.BookmarksDatabase;
import android.os.Bundle;
import org.mozilla.focus.persistence.BookmarkModel;
import java.util.List;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.mozilla.focus.bookmark.BookmarkAdapter;

public class BookmarksFragment extends PanelFragment implements BookmarkPanelListener
{
    private BookmarkAdapter adapter;
    private View emptyView;
    private RecyclerView recyclerView;
    private BookmarkViewModel viewModel;
    
    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        final BookmarkViewModel.Factory factory = new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance((Context)this.getActivity())));
        final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this.getActivity());
        this.adapter = new BookmarkAdapter((BookmarkAdapter.BookmarkPanelListener)this);
        this.recyclerView.setAdapter((RecyclerView.Adapter)this.adapter);
        this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.viewModel = ViewModelProviders.of(this.getActivity(), factory).get(BookmarkViewModel.class);
        this.viewModel.getBookmarks().observe(this, new _$$Lambda$BookmarksFragment$w55Mdo4yDA85aix7wseyaw7HYyA(this));
        this.onStatus(1);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492953, viewGroup, false);
        this.recyclerView = (RecyclerView)inflate.findViewById(2131296585);
        this.emptyView = inflate.findViewById(2131296422);
        return inflate;
    }
    
    @Override
    public void onItemClicked(final String s) {
        ScreenNavigator.get(this.getContext()).showBrowserScreen(s, true, false);
        this.closePanel();
        TelemetryWrapper.bookmarkOpenItem();
    }
    
    @Override
    public void onItemDeleted(final BookmarkModel bookmarkModel) {
        this.viewModel.deleteBookmark(bookmarkModel);
        TelemetryWrapper.bookmarkRemoveItem();
    }
    
    @Override
    public void onItemEdited(final BookmarkModel bookmarkModel) {
        this.startActivity(new Intent(this.getContext(), (Class)EditBookmarkActivity.class).putExtra("ITEM_UUID_KEY", bookmarkModel.getId()));
        TelemetryWrapper.bookmarkEditItem();
    }
    
    @Override
    public void onStatus(final int n) {
        if (n == 0) {
            this.recyclerView.setVisibility(8);
            this.emptyView.setVisibility(0);
        }
        else if (1 == n) {
            this.recyclerView.setVisibility(0);
            this.emptyView.setVisibility(8);
        }
        else {
            this.recyclerView.setVisibility(8);
            this.emptyView.setVisibility(8);
        }
    }
    
    @Override
    public void tryLoadMore() {
    }
}
