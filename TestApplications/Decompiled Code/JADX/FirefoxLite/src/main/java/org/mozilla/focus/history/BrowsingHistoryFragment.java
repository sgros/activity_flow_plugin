package org.mozilla.focus.history;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.p004v7.app.AlertDialog.Builder;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import org.mozilla.focus.fragment.PanelFragment;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.widget.FragmentListener.C0572-CC;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.rocket.C0769R;

public class BrowsingHistoryFragment extends PanelFragment implements OnClickListener, ItemClosingPanelFragmentStatusListener {
    private HistoryItemAdapter mAdapter;
    private ViewGroup mContainerEmptyView;
    private ViewGroup mContainerRecyclerView;
    private RecyclerView mRecyclerView;

    public static BrowsingHistoryFragment newInstance() {
        return new BrowsingHistoryFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.fragment_browsing_history, viewGroup, false);
        inflate.findViewById(C0427R.C0426id.browsing_history_btn_clear).setOnClickListener(this);
        this.mContainerRecyclerView = (ViewGroup) inflate.findViewById(C0427R.C0426id.browsing_history_recycler_view_container);
        this.mContainerEmptyView = (ViewGroup) inflate.findViewById(C0427R.C0426id.empty_view_container);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(C0427R.C0426id.browsing_history_recycler_view);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        this.mAdapter = new HistoryItemAdapter(this.mRecyclerView, getActivity(), this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void onClick(View view) {
        if (view.getId() == C0427R.C0426id.browsing_history_btn_clear) {
            final Context context = getContext();
            Builder builder = new Builder(getActivity(), C0769R.style.AlertDialogStyle);
            builder.setTitle((int) C0769R.string.browsing_history_dialog_confirm_clear_message);
            builder.setPositiveButton((int) C0769R.string.browsing_history_dialog_btn_clear, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (context != null) {
                        BrowsingHistoryFragment.this.mAdapter.clear();
                        TopSitesUtils.getDefaultSitesJsonArrayFromAssets(context);
                        C0572-CC.notifyParent(BrowsingHistoryFragment.this, TYPE.REFRESH_TOP_SITE, null);
                        TelemetryWrapper.clearHistory();
                    }
                }
            });
            builder.setNegativeButton((int) C0769R.string.action_cancel, null);
            builder.create().show();
        }
    }

    public void onStatus(int i) {
        if (i == 0) {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(0);
        } else if (1 == i) {
            this.mContainerRecyclerView.setVisibility(0);
            this.mContainerEmptyView.setVisibility(8);
        } else {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(8);
        }
    }

    public void onItemClicked() {
        closePanel();
    }

    public void tryLoadMore() {
        this.mAdapter.tryLoadMore();
    }
}
