// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.history;

import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.support.v4.app.Fragment;
import org.mozilla.focus.widget.FragmentListener$_CC;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.utils.TopSitesUtils;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import android.view.View$OnClickListener;
import org.mozilla.focus.fragment.PanelFragment;

public class BrowsingHistoryFragment extends PanelFragment implements View$OnClickListener, ItemClosingPanelFragmentStatusListener
{
    private HistoryItemAdapter mAdapter;
    private ViewGroup mContainerEmptyView;
    private ViewGroup mContainerRecyclerView;
    private RecyclerView mRecyclerView;
    
    public static BrowsingHistoryFragment newInstance() {
        return new BrowsingHistoryFragment();
    }
    
    public void onClick(final View view) {
        if (view.getId() == 2131296341) {
            final Context context = this.getContext();
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getActivity(), 2131820546);
            builder.setTitle(2131755077);
            builder.setPositiveButton(2131755076, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    if (context == null) {
                        return;
                    }
                    BrowsingHistoryFragment.this.mAdapter.clear();
                    TopSitesUtils.getDefaultSitesJsonArrayFromAssets(context);
                    FragmentListener$_CC.notifyParent(BrowsingHistoryFragment.this, FragmentListener.TYPE.REFRESH_TOP_SITE, null);
                    TelemetryWrapper.clearHistory();
                }
            });
            builder.setNegativeButton(2131755060, null);
            builder.create().show();
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492956, viewGroup, false);
        inflate.findViewById(2131296341).setOnClickListener((View$OnClickListener)this);
        this.mContainerRecyclerView = (ViewGroup)inflate.findViewById(2131296344);
        this.mContainerEmptyView = (ViewGroup)inflate.findViewById(2131296422);
        this.mRecyclerView = (RecyclerView)inflate.findViewById(2131296343);
        return inflate;
    }
    
    public void onItemClicked() {
        this.closePanel();
    }
    
    public void onStatus(final int n) {
        if (n == 0) {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(0);
        }
        else if (1 == n) {
            this.mContainerRecyclerView.setVisibility(0);
            this.mContainerEmptyView.setVisibility(8);
        }
        else {
            this.mContainerRecyclerView.setVisibility(8);
            this.mContainerEmptyView.setVisibility(8);
        }
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this.getActivity());
        this.mAdapter = new HistoryItemAdapter(this.mRecyclerView, (Context)this.getActivity(), this);
        this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mAdapter);
        this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
    }
    
    @Override
    public void tryLoadMore() {
        this.mAdapter.tryLoadMore();
    }
}
