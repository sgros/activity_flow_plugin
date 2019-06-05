// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.history;

import android.widget.TextView;
import java.util.Iterator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.focus.widget.FragmentListener;
import android.text.format.DateUtils;
import android.view.View;
import android.support.v7.widget.PopupMenu;
import com.bumptech.glide.request.transition.Transition;
import android.graphics.Bitmap;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.Glide;
import org.mozilla.focus.site.SiteItemViewHolder;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.icon.FavIconUtils;
import java.net.URISyntaxException;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.fileutils.FileUtils;
import java.io.File;
import java.net.URI;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.view.MenuItem;
import java.util.Calendar;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.history.model.Site;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import android.content.Context;
import org.mozilla.focus.provider.QueryHandler;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView;

public class HistoryItemAdapter extends Adapter<ViewHolder> implements View$OnClickListener, AsyncDeleteListener, AsyncQueryListener
{
    private Context mContext;
    private int mCurrentCount;
    private ItemClosingPanelFragmentStatusListener mHistoryListener;
    private boolean mIsInitialQuery;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private List mItems;
    private RecyclerView mRecyclerView;
    
    public HistoryItemAdapter(final RecyclerView mRecyclerView, final Context mContext, final ItemClosingPanelFragmentStatusListener mHistoryListener) {
        this.mItems = new ArrayList();
        this.mRecyclerView = mRecyclerView;
        this.mContext = mContext;
        this.mHistoryListener = mHistoryListener;
        this.mIsInitialQuery = true;
        this.notifyStatusListener(2);
        this.loadMoreItems();
    }
    
    private void add(final Object o) {
        if (this.mItems.size() > 0 && isSameDay(this.mItems.get(this.mItems.size() - 1).getLastViewTimestamp(), ((Site)o).getLastViewTimestamp())) {
            this.mItems.add(o);
            ((RecyclerView.Adapter)this).notifyItemInserted(this.mItems.size());
        }
        else {
            this.mItems.add(new DateSection(((Site)o).getLastViewTimestamp()));
            this.mItems.add(o);
            ((RecyclerView.Adapter)this).notifyItemRangeInserted(this.mItems.size() - 2, 2);
        }
        ++this.mCurrentCount;
    }
    
    private int getItemPositionById(final long n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final Object value = this.mItems.get(i);
            if (value instanceof Site && n == ((Site)value).getId()) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean isSameDay(final long timeInMillis, final long timeInMillis2) {
        final Calendar instance = Calendar.getInstance();
        final Calendar instance2 = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        instance2.setTimeInMillis(timeInMillis2);
        boolean b = true;
        if (instance.get(1) != instance2.get(1) || instance.get(6) != instance2.get(6)) {
            b = false;
        }
        return b;
    }
    
    private void loadMoreItems() {
        this.mIsLoading = true;
        BrowsingHistoryManager.getInstance().query(this.mCurrentCount, 50 - this.mCurrentCount % 50, this);
    }
    
    private void notifyStatusListener(final int n) {
        if (this.mHistoryListener != null) {
            this.mHistoryListener.onStatus(n);
        }
    }
    
    private void remove(int n) {
        if (n >= 0 && n < this.mItems.size()) {
            Object value = null;
            if (n == 0) {
                final Object value2 = null;
            }
            else {
                final Object value2 = this.mItems.get(n - 1);
            }
            final int n2 = n + 1;
            if (n2 != this.mItems.size()) {
                value = this.mItems.get(n2);
            }
            Object value2;
            if (!(value2 instanceof Site) && !(value instanceof Site)) {
                this.mItems.remove(n);
                final List mItems = this.mItems;
                --n;
                mItems.remove(n);
                ((RecyclerView.Adapter)this).notifyItemRangeRemoved(n, 2);
            }
            else {
                this.mItems.remove(n);
                ((RecyclerView.Adapter)this).notifyItemRemoved(n);
            }
            --this.mCurrentCount;
        }
    }
    
    private void setImageViewWithDefaultBitmap(final ImageView imageView, final String s) {
        imageView.setImageBitmap(DimenUtils.getInitialBitmap(imageView.getResources(), FavIconUtils.getRepresentativeCharacter(s), -1));
    }
    
    public void clear() {
        ThreadUtils.postToBackgroundThread(new FileUtils.DeleteFolderRunnable(FileUtils.getFaviconFolder(this.mContext)));
        BrowsingHistoryManager.getInstance().deleteAll(this);
    }
    
    @Override
    public int getItemCount() {
        return this.mItems.size();
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (this.mItems.get(n) instanceof DateSection) {
            return 2;
        }
        return 1;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        if (viewHolder instanceof SiteItemViewHolder) {
            final Site site = this.mItems.get(n);
            if (site != null) {
                final SiteItemViewHolder siteItemViewHolder = (SiteItemViewHolder)viewHolder;
                siteItemViewHolder.rootView.setOnClickListener((View$OnClickListener)this);
                siteItemViewHolder.textMain.setText((CharSequence)site.getTitle());
                siteItemViewHolder.textSecondary.setText((CharSequence)site.getUrl());
                final String favIconUri = site.getFavIconUri();
                if (favIconUri != null) {
                    Glide.with(siteItemViewHolder.imgFav.getContext()).asBitmap().load(favIconUri).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap imageBitmap, final Transition<? super Bitmap> transition) {
                            if (DimenUtils.iconTooBlurry(siteItemViewHolder.imgFav.getResources(), imageBitmap.getWidth())) {
                                HistoryItemAdapter.this.setImageViewWithDefaultBitmap(siteItemViewHolder.imgFav, site.getUrl());
                            }
                            else {
                                siteItemViewHolder.imgFav.setImageBitmap(imageBitmap);
                            }
                        }
                    });
                }
                else {
                    this.setImageViewWithDefaultBitmap(siteItemViewHolder.imgFav, site.getUrl());
                }
                final PopupMenu popupMenu = new PopupMenu(this.mContext, (View)siteItemViewHolder.btnMore);
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)new _$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk(this, site));
                popupMenu.inflate(2131558402);
                siteItemViewHolder.btnMore.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        popupMenu.show();
                        TelemetryWrapper.showHistoryContextMenu();
                    }
                });
            }
        }
        else if (viewHolder instanceof DateItemViewHolder) {
            final DateSection dateSection = this.mItems.get(n);
            if (dateSection != null) {
                ((DateItemViewHolder)viewHolder).textDate.setText(DateUtils.getRelativeTimeSpanString(dateSection.getTimestamp(), System.currentTimeMillis(), 86400000L));
            }
        }
    }
    
    public void onClick(final View view) {
        final int childAdapterPosition = this.mRecyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition != -1 && childAdapterPosition < this.mItems.size()) {
            final Object value = this.mItems.get(childAdapterPosition);
            if (value instanceof Site && this.mContext instanceof FragmentListener) {
                HomeFragmentViewState.reset();
                ScreenNavigator.get(this.mContext).showBrowserScreen(((Site)value).getUrl(), true, false);
                this.mHistoryListener.onItemClicked();
                TelemetryWrapper.historyOpenLink();
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 1) {
            return new SiteItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492978, viewGroup, false));
        }
        if (n == 2) {
            return new DateItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492977, viewGroup, false));
        }
        return null;
    }
    
    public void onDeleteComplete(int size, final long n) {
        if (size > 0) {
            if (n < 0L) {
                size = this.mItems.size();
                this.mItems.clear();
                ((RecyclerView.Adapter)this).notifyItemRangeRemoved(0, size);
                this.notifyStatusListener(0);
            }
            else {
                this.remove(this.getItemPositionById(n));
                if (this.mItems.size() == 0) {
                    this.notifyStatusListener(0);
                }
            }
        }
    }
    
    public void onQueryComplete(final List list) {
        this.mIsLastPage = (list.size() == 0);
        if (this.mIsInitialQuery) {
            this.mIsInitialQuery = false;
        }
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
        if (this.mItems.size() > 0) {
            this.notifyStatusListener(1);
        }
        else {
            this.notifyStatusListener(0);
        }
        this.mIsLoading = false;
    }
    
    public void tryLoadMore() {
        if (!this.mIsLoading && !this.mIsLastPage) {
            this.loadMoreItems();
        }
    }
    
    private static class DateItemViewHolder extends ViewHolder
    {
        private TextView textDate;
        
        public DateItemViewHolder(final View view) {
            super(view);
            this.textDate = (TextView)view.findViewById(2131296458);
        }
    }
}
