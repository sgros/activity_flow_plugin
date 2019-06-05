package org.mozilla.focus.screenshot;

import android.app.Activity;
import android.support.p004v7.widget.GridLayoutManager;
import android.support.p004v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;
import org.mozilla.focus.glide.GlideApp;
import org.mozilla.focus.history.model.DateSection;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.C0769R;

public class ScreenshotItemAdapter extends Adapter<ViewHolder> implements OnClickListener, AsyncQueryListener {
    private Activity mActivity;
    private int mCurrentCount;
    private boolean mIsInitialQuery;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private List mItems = new ArrayList();
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    SpanSizeLookup mSpanSizeHelper = new C07361();
    private PanelFragmentStatusListener mStatusListener;

    /* renamed from: org.mozilla.focus.screenshot.ScreenshotItemAdapter$1 */
    class C07361 extends SpanSizeLookup {
        C07361() {
        }

        public int getSpanSize(int i) {
            switch (ScreenshotItemAdapter.this.getItemViewType(i)) {
                case 1:
                    return 1;
                case 2:
                    return 3;
                default:
                    return 1;
            }
        }
    }

    private static class DateItemViewHolder extends ViewHolder {
        private TextView textDate;

        public DateItemViewHolder(View view) {
            super(view);
            this.textDate = (TextView) view.findViewById(C0427R.C0426id.screenshot_item_date);
        }
    }

    private static class GirdItemViewHolder extends ViewHolder {
        private ImageView img;
        private View rootView;

        public GirdItemViewHolder(View view) {
            super(view);
            this.img = (ImageView) view.findViewById(C0427R.C0426id.screenshot_grid_img_cell);
            this.rootView = view.findViewById(C0427R.C0426id.screenshot_grid_rootview);
        }
    }

    public ScreenshotItemAdapter(RecyclerView recyclerView, Activity activity, PanelFragmentStatusListener panelFragmentStatusListener, GridLayoutManager gridLayoutManager) {
        this.mRecyclerView = recyclerView;
        this.mActivity = activity;
        this.mStatusListener = panelFragmentStatusListener;
        this.mLayoutManager = gridLayoutManager;
        this.mLayoutManager.setSpanSizeLookup(this.mSpanSizeHelper);
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
            return new GirdItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_screenshot_grid_cell, viewGroup, false));
        }
        return i == 2 ? new DateItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.item_screenshot_date, viewGroup, false)) : null;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder instanceof GirdItemViewHolder) {
            GirdItemViewHolder girdItemViewHolder = (GirdItemViewHolder) viewHolder;
            girdItemViewHolder.rootView.setOnClickListener(this);
            GlideApp.with(this.mActivity).asBitmap().placeholder(2131230955).fitCenter().load(((Screenshot) this.mItems.get(i)).getImageUri()).into(girdItemViewHolder.img);
        } else if (viewHolder instanceof DateItemViewHolder) {
            DateSection dateSection = (DateSection) this.mItems.get(i);
            if (dateSection != null) {
                ((DateItemViewHolder) viewHolder).textDate.setText(DateUtils.getRelativeTimeSpanString(dateSection.getTimestamp(), System.currentTimeMillis(), 86400000));
            }
        }
    }

    public int getItemViewType(int i) {
        return this.mItems.get(i) instanceof DateSection ? 2 : 1;
    }

    public int getAdjustPosition(int i) {
        if (i >= this.mItems.size()) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 <= i; i3++) {
            if (this.mItems.get(i3) instanceof DateSection) {
                i2 = i3;
            }
        }
        return i2 == 0 ? i - 1 : (i - 1) - i2;
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public void onClick(View view) {
        int childLayoutPosition = this.mRecyclerView.getChildLayoutPosition(view);
        if (childLayoutPosition != -1) {
            Object obj = this.mItems.get(childLayoutPosition);
            if (obj instanceof Screenshot) {
                ScreenshotViewerActivity.goScreenshotViewerActivityOnResult(this.mActivity, (Screenshot) obj);
                TelemetryWrapper.openCapture();
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

    public void onItemDelete(long j) {
        if (j >= 0) {
            remove(getItemPositionById(j));
            if (this.mItems.size() == 0) {
                notifyStatusListener(0);
            }
        }
    }

    private void add(Object obj) {
        if (this.mItems.size() <= 0 || !isSameDay(((Screenshot) this.mItems.get(this.mItems.size() - 1)).getTimestamp(), ((Screenshot) obj).getTimestamp())) {
            this.mItems.add(new DateSection(((Screenshot) obj).getTimestamp()));
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
            if ((obj instanceof Screenshot) || (obj2 instanceof Screenshot)) {
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
        ScreenshotManager.getInstance().query(this.mCurrentCount, 20 - (this.mCurrentCount % 20), this);
    }

    private void notifyStatusListener(int i) {
        if (this.mStatusListener != null) {
            this.mStatusListener.onStatus(i);
        }
    }

    private int getItemPositionById(long j) {
        for (int i = 0; i < this.mItems.size(); i++) {
            Object obj = this.mItems.get(i);
            if ((obj instanceof Screenshot) && j == ((Screenshot) obj).getId()) {
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
