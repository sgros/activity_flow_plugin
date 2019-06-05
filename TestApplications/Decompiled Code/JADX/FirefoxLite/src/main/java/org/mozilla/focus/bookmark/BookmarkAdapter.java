package org.mozilla.focus.bookmark;

import android.support.p004v7.widget.PopupMenu;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.site.SiteItemViewHolder;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.C0769R;

public class BookmarkAdapter extends Adapter<SiteItemViewHolder> {
    private List<BookmarkModel> bookmarkModels;
    private BookmarkPanelListener listener;

    public interface BookmarkPanelListener extends PanelFragmentStatusListener {
        void onItemClicked(String str);

        void onItemDeleted(BookmarkModel bookmarkModel);

        void onItemEdited(BookmarkModel bookmarkModel);
    }

    public BookmarkAdapter(BookmarkPanelListener bookmarkPanelListener) {
        this.listener = bookmarkPanelListener;
    }

    public SiteItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SiteItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_history_website, viewGroup, false));
    }

    public void onBindViewHolder(SiteItemViewHolder siteItemViewHolder, int i) {
        BookmarkModel item = getItem(i);
        if (item != null) {
            siteItemViewHolder.rootView.setTag(item.getId());
            siteItemViewHolder.textMain.setText(item.getTitle());
            siteItemViewHolder.textSecondary.setText(item.getUrl());
            siteItemViewHolder.rootView.setOnClickListener(new C0441-$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0(this, item));
            PopupMenu popupMenu = new PopupMenu(siteItemViewHolder.btnMore.getContext(), siteItemViewHolder.btnMore);
            popupMenu.setOnMenuItemClickListener(new C0682-$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y(this, item));
            popupMenu.inflate(2131558400);
            siteItemViewHolder.btnMore.setOnClickListener(new C0442-$$Lambda$BookmarkAdapter$w4NbRco7_1oajISDwewXQSTMa54(popupMenu));
        }
    }

    public static /* synthetic */ boolean lambda$onBindViewHolder$1(BookmarkAdapter bookmarkAdapter, BookmarkModel bookmarkModel, MenuItem menuItem) {
        if (menuItem.getItemId() == C0427R.C0426id.remove) {
            bookmarkAdapter.listener.onItemDeleted(bookmarkModel);
        }
        if (menuItem.getItemId() == C0427R.C0426id.edit) {
            bookmarkAdapter.listener.onItemEdited(bookmarkModel);
        }
        return false;
    }

    static /* synthetic */ void lambda$onBindViewHolder$2(PopupMenu popupMenu, View view) {
        popupMenu.show();
        TelemetryWrapper.showBookmarkContextMenu();
    }

    public int getItemCount() {
        return this.bookmarkModels != null ? this.bookmarkModels.size() : 0;
    }

    public void setData(List<BookmarkModel> list) {
        this.bookmarkModels = list;
        if (getItemCount() == 0) {
            this.listener.onStatus(0);
        } else {
            this.listener.onStatus(1);
        }
        notifyDataSetChanged();
    }

    private BookmarkModel getItem(int i) {
        return (i < 0 || this.bookmarkModels == null || this.bookmarkModels.size() <= i) ? null : (BookmarkModel) this.bookmarkModels.get(i);
    }
}
