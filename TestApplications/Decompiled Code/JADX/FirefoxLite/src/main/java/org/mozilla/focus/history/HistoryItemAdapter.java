package org.mozilla.focus.history;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.p004v7.widget.PopupMenu;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.fileutils.FileUtils.DeleteFileRunnable;
import org.mozilla.fileutils.FileUtils.DeleteFolderRunnable;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteListener;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;
import org.mozilla.focus.site.SiteItemViewHolder;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DimenUtils;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.threadutils.ThreadUtils;

public class HistoryItemAdapter extends Adapter<ViewHolder> implements OnClickListener, AsyncDeleteListener, AsyncQueryListener {
    private Context mContext;
    private int mCurrentCount;
    private ItemClosingPanelFragmentStatusListener mHistoryListener;
    private boolean mIsInitialQuery;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private List mItems = new ArrayList();
    private RecyclerView mRecyclerView;

    private static class DateItemViewHolder extends ViewHolder {
        private TextView textDate;

        public DateItemViewHolder(View view) {
            super(view);
            this.textDate = (TextView) view.findViewById(C0427R.C0426id.history_item_date);
        }
    }

    public HistoryItemAdapter(RecyclerView recyclerView, Context context, ItemClosingPanelFragmentStatusListener itemClosingPanelFragmentStatusListener) {
        this.mRecyclerView = recyclerView;
        this.mContext = context;
        this.mHistoryListener = itemClosingPanelFragmentStatusListener;
        this.mIsInitialQuery = true;
        notifyStatusListener(2);
        loadMoreItems();
    }

    public void tryLoadMore() {
        if (!this.mIsLoading && !this.mIsLastPage) {
            loadMoreItems();
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new SiteItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_history_website, viewGroup, false));
        }
        return i == 2 ? new DateItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_history_date, viewGroup, false)) : null;
    }

    private void setImageViewWithDefaultBitmap(ImageView imageView, String str) {
        imageView.setImageBitmap(DimenUtils.getInitialBitmap(imageView.getResources(), FavIconUtils.getRepresentativeCharacter(str), -1));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SiteItemViewHolder) {
            final Site site = (Site) this.mItems.get(i);
            if (site != null) {
                final SiteItemViewHolder siteItemViewHolder = (SiteItemViewHolder) viewHolder;
                siteItemViewHolder.rootView.setOnClickListener(this);
                siteItemViewHolder.textMain.setText(site.getTitle());
                siteItemViewHolder.textSecondary.setText(site.getUrl());
                String favIconUri = site.getFavIconUri();
                if (favIconUri != null) {
                    Glide.with(siteItemViewHolder.imgFav.getContext()).asBitmap().load(favIconUri).into(new SimpleTarget<Bitmap>() {
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            if (DimenUtils.iconTooBlurry(siteItemViewHolder.imgFav.getResources(), bitmap.getWidth())) {
                                HistoryItemAdapter.this.setImageViewWithDefaultBitmap(siteItemViewHolder.imgFav, site.getUrl());
                            } else {
                                siteItemViewHolder.imgFav.setImageBitmap(bitmap);
                            }
                        }
                    });
                } else {
                    setImageViewWithDefaultBitmap(siteItemViewHolder.imgFav, site.getUrl());
                }
                final PopupMenu popupMenu = new PopupMenu(this.mContext, siteItemViewHolder.btnMore);
                popupMenu.setOnMenuItemClickListener(new C0698-$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk(this, site));
                popupMenu.inflate(2131558402);
                siteItemViewHolder.btnMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        popupMenu.show();
                        TelemetryWrapper.showHistoryContextMenu();
                    }
                });
            }
        } else if (viewHolder instanceof DateItemViewHolder) {
            DateSection dateSection = (DateSection) this.mItems.get(i);
            if (dateSection != null) {
                ((DateItemViewHolder) viewHolder).textDate.setText(DateUtils.getRelativeTimeSpanString(dateSection.getTimestamp(), System.currentTimeMillis(), 86400000));
            }
        }
    }

    public static /* synthetic */ boolean lambda$onBindViewHolder$0(HistoryItemAdapter historyItemAdapter, Site site, MenuItem menuItem) {
        if (menuItem.getItemId() == C0427R.C0426id.browsing_history_menu_delete) {
            BrowsingHistoryManager.getInstance().delete(site.getId(), historyItemAdapter);
            TelemetryWrapper.historyRemoveLink();
            String favIconUri = site.getFavIconUri();
            if (favIconUri == null) {
                return false;
            }
            try {
                ThreadUtils.postToBackgroundThread(new DeleteFileRunnable(new File(new URI(favIconUri))));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public int getItemViewType(int i) {
        return this.mItems.get(i) instanceof DateSection ? 2 : 1;
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public void onClick(View view) {
        int childAdapterPosition = this.mRecyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition != -1 && childAdapterPosition < this.mItems.size()) {
            Object obj = this.mItems.get(childAdapterPosition);
            if ((obj instanceof Site) && (this.mContext instanceof FragmentListener)) {
                HomeFragmentViewState.reset();
                ScreenNavigator.get(this.mContext).showBrowserScreen(((Site) obj).getUrl(), true, false);
                this.mHistoryListener.onItemClicked();
                TelemetryWrapper.historyOpenLink();
            }
        }
    }

    public void onQueryComplete(List list) {
        this.mIsLastPage = list.size() == 0;
        if (this.mIsInitialQuery) {
            this.mIsInitialQuery = false;
        }
        for (Object add : list) {
            add(add);
        }
        if (this.mItems.size() > 0) {
            notifyStatusListener(1);
        } else {
            notifyStatusListener(0);
        }
        this.mIsLoading = false;
    }

    public void onDeleteComplete(int i, long j) {
        if (i <= 0) {
            return;
        }
        if (j < 0) {
            int size = this.mItems.size();
            this.mItems.clear();
            notifyItemRangeRemoved(0, size);
            notifyStatusListener(0);
            return;
        }
        remove(getItemPositionById(j));
        if (this.mItems.size() == 0) {
            notifyStatusListener(0);
        }
    }

    public void clear() {
        ThreadUtils.postToBackgroundThread(new DeleteFolderRunnable(FileUtils.getFaviconFolder(this.mContext)));
        BrowsingHistoryManager.getInstance().deleteAll(this);
    }

    private void add(Object obj) {
        if (this.mItems.size() <= 0 || !isSameDay(((Site) this.mItems.get(this.mItems.size() - 1)).getLastViewTimestamp(), ((Site) obj).getLastViewTimestamp())) {
            this.mItems.add(new DateSection(((Site) obj).getLastViewTimestamp()));
            this.mItems.add(obj);
            notifyItemRangeInserted(this.mItems.size() - 2, 2);
        } else {
            this.mItems.add(obj);
            notifyItemInserted(this.mItems.size());
        }
        this.mCurrentCount++;
    }

    private void remove(int i) {
        if (i >= 0 && i < this.mItems.size()) {
            Object obj;
            Object obj2 = null;
            if (i == 0) {
                obj = null;
            } else {
                obj = this.mItems.get(i - 1);
            }
            int i2 = i + 1;
            if (i2 != this.mItems.size()) {
                obj2 = this.mItems.get(i2);
            }
            if ((obj instanceof Site) || (obj2 instanceof Site)) {
                this.mItems.remove(i);
                notifyItemRemoved(i);
            } else {
                this.mItems.remove(i);
                i--;
                this.mItems.remove(i);
                notifyItemRangeRemoved(i, 2);
            }
            this.mCurrentCount--;
        }
    }

    private void loadMoreItems() {
        this.mIsLoading = true;
        BrowsingHistoryManager.getInstance().query(this.mCurrentCount, 50 - (this.mCurrentCount % 50), this);
    }

    private void notifyStatusListener(int i) {
        if (this.mHistoryListener != null) {
            this.mHistoryListener.onStatus(i);
        }
    }

    private int getItemPositionById(long j) {
        for (int i = 0; i < this.mItems.size(); i++) {
            Object obj = this.mItems.get(i);
            if ((obj instanceof Site) && j == ((Site) obj).getId()) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isSameDay(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance2.setTimeInMillis(j2);
        if (instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6)) {
            return true;
        }
        return false;
    }
}
