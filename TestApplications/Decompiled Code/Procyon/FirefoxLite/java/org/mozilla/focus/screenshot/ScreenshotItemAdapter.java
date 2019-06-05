// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import android.widget.ImageView;
import android.widget.TextView;
import java.util.Iterator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.view.View;
import android.text.format.DateUtils;
import org.mozilla.focus.glide.GlideApp;
import java.util.Calendar;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.screenshot.model.Screenshot;
import java.util.ArrayList;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import android.support.v7.widget.GridLayoutManager;
import java.util.List;
import android.app.Activity;
import org.mozilla.focus.provider.QueryHandler;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView;

public class ScreenshotItemAdapter extends Adapter<ViewHolder> implements View$OnClickListener, AsyncQueryListener
{
    private Activity mActivity;
    private int mCurrentCount;
    private boolean mIsInitialQuery;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private List mItems;
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    GridLayoutManager.SpanSizeLookup mSpanSizeHelper;
    private PanelFragmentStatusListener mStatusListener;
    
    public ScreenshotItemAdapter(final RecyclerView mRecyclerView, final Activity mActivity, final PanelFragmentStatusListener mStatusListener, final GridLayoutManager mLayoutManager) {
        this.mItems = new ArrayList();
        this.mSpanSizeHelper = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                switch (ScreenshotItemAdapter.this.getItemViewType(n)) {
                    default: {
                        return 1;
                    }
                    case 2: {
                        return 3;
                    }
                    case 1: {
                        return 1;
                    }
                }
            }
        };
        this.mRecyclerView = mRecyclerView;
        this.mActivity = mActivity;
        this.mStatusListener = mStatusListener;
        (this.mLayoutManager = mLayoutManager).setSpanSizeLookup(this.mSpanSizeHelper);
        this.mIsInitialQuery = true;
        this.notifyStatusListener(2);
        this.loadMoreItems();
    }
    
    private void add(final Object o) {
        if (this.mItems.size() > 0 && isSameDay(this.mItems.get(this.mItems.size() - 1).getTimestamp(), ((Screenshot)o).getTimestamp())) {
            this.mItems.add(o);
            ((RecyclerView.Adapter)this).notifyItemInserted(this.mItems.size());
        }
        else {
            this.mItems.add(new DateSection(((Screenshot)o).getTimestamp()));
            this.mItems.add(o);
            ((RecyclerView.Adapter)this).notifyItemRangeInserted(this.mItems.size() - 2, 2);
        }
        ++this.mCurrentCount;
    }
    
    private int getItemPositionById(final long n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final Object value = this.mItems.get(i);
            if (value instanceof Screenshot && n == ((Screenshot)value).getId()) {
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
        ScreenshotManager.getInstance().query(this.mCurrentCount, 20 - this.mCurrentCount % 20, this);
    }
    
    private void notifyStatusListener(final int n) {
        if (this.mStatusListener != null) {
            this.mStatusListener.onStatus(n);
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
            if (!(value2 instanceof Screenshot) && !(value instanceof Screenshot)) {
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
    
    public int getAdjustPosition(final int n) {
        int n2 = n;
        if (n < this.mItems.size()) {
            int i = 0;
            int n3 = 0;
            while (i <= n) {
                if (this.mItems.get(i) instanceof DateSection) {
                    n3 = i;
                }
                ++i;
            }
            if (n3 == 0) {
                n2 = n - 1;
            }
            else {
                n2 = n - 1 - n3;
            }
        }
        return n2;
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
        if (viewHolder instanceof GirdItemViewHolder) {
            final GirdItemViewHolder girdItemViewHolder = (GirdItemViewHolder)viewHolder;
            girdItemViewHolder.rootView.setOnClickListener((View$OnClickListener)this);
            GlideApp.with(this.mActivity).asBitmap().placeholder(2131230955).fitCenter().load(this.mItems.get(n).getImageUri()).into(girdItemViewHolder.img);
        }
        else if (viewHolder instanceof DateItemViewHolder) {
            final DateSection dateSection = this.mItems.get(n);
            if (dateSection != null) {
                ((DateItemViewHolder)viewHolder).textDate.setText(DateUtils.getRelativeTimeSpanString(dateSection.getTimestamp(), System.currentTimeMillis(), 86400000L));
            }
        }
    }
    
    public void onClick(final View view) {
        final int childLayoutPosition = this.mRecyclerView.getChildLayoutPosition(view);
        if (childLayoutPosition != -1) {
            final Object value = this.mItems.get(childLayoutPosition);
            if (value instanceof Screenshot) {
                ScreenshotViewerActivity.goScreenshotViewerActivityOnResult(this.mActivity, (Screenshot)value);
                TelemetryWrapper.openCapture();
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 1) {
            return new GirdItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492981, viewGroup, false));
        }
        if (n == 2) {
            return new DateItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492980, viewGroup, false));
        }
        return null;
    }
    
    public void onItemDelete(final long n) {
        if (n >= 0L) {
            this.remove(this.getItemPositionById(n));
            if (this.mItems.size() == 0) {
                this.notifyStatusListener(0);
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
            this.textDate = (TextView)view.findViewById(2131296608);
        }
    }
    
    private static class GirdItemViewHolder extends ViewHolder
    {
        private ImageView img;
        private View rootView;
        
        public GirdItemViewHolder(final View view) {
            super(view);
            this.img = (ImageView)view.findViewById(2131296604);
            this.rootView = view.findViewById(2131296606);
        }
    }
}
