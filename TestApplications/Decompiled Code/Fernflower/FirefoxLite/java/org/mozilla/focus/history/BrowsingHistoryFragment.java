package org.mozilla.focus.history;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import org.mozilla.focus.fragment.ItemClosingPanelFragmentStatusListener;
import org.mozilla.focus.fragment.PanelFragment;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener$_CC;

public class BrowsingHistoryFragment extends PanelFragment implements OnClickListener, ItemClosingPanelFragmentStatusListener {
   private HistoryItemAdapter mAdapter;
   private ViewGroup mContainerEmptyView;
   private ViewGroup mContainerRecyclerView;
   private RecyclerView mRecyclerView;

   public static BrowsingHistoryFragment newInstance() {
      return new BrowsingHistoryFragment();
   }

   public void onClick(View var1) {
      if (var1.getId() == 2131296341) {
         final Context var2 = this.getContext();
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getActivity(), 2131820546);
         var3.setTitle(2131755077);
         var3.setPositiveButton(2131755076, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2x) {
               if (var2 != null) {
                  BrowsingHistoryFragment.this.mAdapter.clear();
                  TopSitesUtils.getDefaultSitesJsonArrayFromAssets(var2);
                  FragmentListener$_CC.notifyParent(BrowsingHistoryFragment.this, FragmentListener.TYPE.REFRESH_TOP_SITE, (Object)null);
                  TelemetryWrapper.clearHistory();
               }
            }
         });
         var3.setNegativeButton(2131755060, (android.content.DialogInterface.OnClickListener)null);
         var3.create().show();
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(2131492956, var2, false);
      var4.findViewById(2131296341).setOnClickListener(this);
      this.mContainerRecyclerView = (ViewGroup)var4.findViewById(2131296344);
      this.mContainerEmptyView = (ViewGroup)var4.findViewById(2131296422);
      this.mRecyclerView = (RecyclerView)var4.findViewById(2131296343);
      return var4;
   }

   public void onItemClicked() {
      this.closePanel();
   }

   public void onStatus(int var1) {
      if (var1 == 0) {
         this.mContainerRecyclerView.setVisibility(8);
         this.mContainerEmptyView.setVisibility(0);
      } else if (1 == var1) {
         this.mContainerRecyclerView.setVisibility(0);
         this.mContainerEmptyView.setVisibility(8);
      } else {
         this.mContainerRecyclerView.setVisibility(8);
         this.mContainerEmptyView.setVisibility(8);
      }

   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      LinearLayoutManager var3 = new LinearLayoutManager(this.getActivity());
      this.mAdapter = new HistoryItemAdapter(this.mRecyclerView, this.getActivity(), this);
      this.mRecyclerView.setAdapter(this.mAdapter);
      this.mRecyclerView.setLayoutManager(var3);
   }

   public void tryLoadMore() {
      this.mAdapter.tryLoadMore();
   }
}
