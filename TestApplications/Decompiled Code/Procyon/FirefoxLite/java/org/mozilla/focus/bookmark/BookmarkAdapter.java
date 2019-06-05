// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.bookmark;

import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import org.mozilla.focus.persistence.BookmarkModel;
import java.util.List;
import org.mozilla.focus.site.SiteItemViewHolder;
import android.support.v7.widget.RecyclerView;

public class BookmarkAdapter extends Adapter<SiteItemViewHolder>
{
    private List<BookmarkModel> bookmarkModels;
    private BookmarkPanelListener listener;
    
    public BookmarkAdapter(final BookmarkPanelListener listener) {
        this.listener = listener;
    }
    
    private BookmarkModel getItem(final int n) {
        if (n >= 0 && this.bookmarkModels != null && this.bookmarkModels.size() > n) {
            return this.bookmarkModels.get(n);
        }
        return null;
    }
    
    @Override
    public int getItemCount() {
        int size;
        if (this.bookmarkModels != null) {
            size = this.bookmarkModels.size();
        }
        else {
            size = 0;
        }
        return size;
    }
    
    public void onBindViewHolder(final SiteItemViewHolder siteItemViewHolder, final int n) {
        final BookmarkModel item = this.getItem(n);
        if (item == null) {
            return;
        }
        siteItemViewHolder.rootView.setTag((Object)item.getId());
        siteItemViewHolder.textMain.setText((CharSequence)item.getTitle());
        siteItemViewHolder.textSecondary.setText((CharSequence)item.getUrl());
        siteItemViewHolder.rootView.setOnClickListener((View$OnClickListener)new _$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0(this, item));
        final PopupMenu popupMenu = new PopupMenu(siteItemViewHolder.btnMore.getContext(), (View)siteItemViewHolder.btnMore);
        popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)new _$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y(this, item));
        popupMenu.inflate(2131558400);
        siteItemViewHolder.btnMore.setOnClickListener((View$OnClickListener)new _$$Lambda$BookmarkAdapter$w4NbRco7_1oajISDwewXQSTMa54(popupMenu));
    }
    
    public SiteItemViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new SiteItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492978, viewGroup, false));
    }
    
    public void setData(final List<BookmarkModel> bookmarkModels) {
        this.bookmarkModels = bookmarkModels;
        if (this.getItemCount() == 0) {
            this.listener.onStatus(0);
        }
        else {
            this.listener.onStatus(1);
        }
        ((RecyclerView.Adapter)this).notifyDataSetChanged();
    }
    
    public interface BookmarkPanelListener extends PanelFragmentStatusListener
    {
        void onItemClicked(final String p0);
        
        void onItemDeleted(final BookmarkModel p0);
        
        void onItemEdited(final BookmarkModel p0);
    }
}
