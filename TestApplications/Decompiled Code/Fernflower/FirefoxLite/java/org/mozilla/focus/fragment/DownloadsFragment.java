package org.mozilla.focus.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.mozilla.focus.Inject;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.widget.DownloadListAdapter;
import org.mozilla.rocket.download.DownloadInfoPack;
import org.mozilla.rocket.download.DownloadInfoViewModel;

public class DownloadsFragment extends PanelFragment implements DownloadInfoViewModel.OnProgressUpdateListener {
   private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         long var3;
         if (var2.getAction() == "android.intent.action.DOWNLOAD_COMPLETE") {
            var3 = var2.getLongExtra("extra_download_id", 0L);
            if (var3 > 0L) {
               DownloadsFragment.this.viewModel.notifyDownloadComplete(var3);
            }
         } else if (var2.getAction() == "row_updated") {
            var3 = var2.getLongExtra("row id", 0L);
            if (var3 > 0L) {
               DownloadsFragment.this.viewModel.notifyRowUpdate(var3);
            }
         }

      }
   };
   private DownloadListAdapter downloadListAdapter;
   private Handler handler;
   private HandlerThread handlerThread;
   private RecyclerView recyclerView;
   private DownloadInfoViewModel viewModel;

   private void cleanUp() {
      if (this.handlerThread != null) {
         this.handlerThread.quit();
         this.handlerThread = null;
         this.handler = null;
      }

   }

   // $FF: synthetic method
   public static void lambda$null$2(DownloadsFragment var0, DownloadInfo var1, View var2) {
      var0.viewModel.add(var1);
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$0(DownloadsFragment var0, DownloadInfoPack var1) {
      if (var1 != null) {
         switch(var1.getNotifyType()) {
         case 1:
            var0.downloadListAdapter.setList(var1.getList());
            var0.downloadListAdapter.notifyDataSetChanged();
            break;
         case 2:
            var0.downloadListAdapter.notifyItemInserted((int)var1.getIndex());
            break;
         case 3:
            var0.downloadListAdapter.notifyItemRemoved((int)var1.getIndex());
            break;
         case 4:
            var0.downloadListAdapter.notifyItemChanged((int)var1.getIndex());
         }
      }

   }

   // $FF: synthetic method
   public static void lambda$onCreateView$1(DownloadsFragment var0, Integer var1) {
      String var2 = var0.getString(var1);
      Toast.makeText(var0.getActivity(), var2, 0).show();
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$3(final DownloadsFragment var0, final DownloadInfo var1) {
      String var2 = var0.getString(2131755105, new Object[]{var1.getFileName()});
      ((Snackbar)Snackbar.make(var0.recyclerView, var2, -1).addCallback(new BaseTransientBottomBar.BaseCallback() {
         public void onDismissed(Snackbar var1x, int var2) {
            super.onDismissed(var1x, var2);
            if (var2 != 1) {
               var0.viewModel.confirmDelete(var1);
            }

         }

         public void onShown(Snackbar var1x) {
            super.onShown(var1x);
            var0.viewModel.hide(var1.getRowId());
         }
      })).setAction(2131755424, new _$$Lambda$DownloadsFragment$0VaTEEPTvN8aDQrBVULu865voMw(var0, var1)).show();
   }

   public static DownloadsFragment newInstance() {
      return new DownloadsFragment();
   }

   private void prepare() {
      this.recyclerView.setAdapter(this.downloadListAdapter);
      this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), 1, false));
      ((SimpleItemAnimator)this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
   }

   private void startProgressUpdate() {
      if (this.handlerThread == null) {
         this.handlerThread = new HandlerThread("download-progress");
         this.handlerThread.start();
         this.handler = new Handler(this.handlerThread.getLooper()) {
            public void handleMessage(Message var1) {
               if (var1.what == 1) {
                  DownloadsFragment.this.viewModel.queryDownloadProgress();
               }

            }
         };
      }

      if (!this.handler.hasMessages(1)) {
         this.handler.sendEmptyMessage(1);
      }

   }

   public void onCompleteUpdate() {
      if (this.handler != null && !this.handler.hasMessages(1)) {
         this.handler.sendEmptyMessageDelayed(1, 500L);
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.recyclerView = (RecyclerView)var1.inflate(2131492957, var2, false);
      this.viewModel = Inject.obtainDownloadInfoViewModel(this.getActivity());
      this.downloadListAdapter = new DownloadListAdapter(this.getContext(), this.viewModel);
      this.viewModel.getDownloadInfoObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y(this));
      this.viewModel.getToastMessageObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU(this));
      this.viewModel.getDeleteSnackbarObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug(this));
      return this.recyclerView;
   }

   public void onDestroy() {
      this.viewModel.markAllItemsAreRead();
      Inject.obtainDownloadIndicatorViewModel(this.getActivity()).updateIndicator();
      this.cleanUp();
      super.onDestroy();
   }

   public void onPause() {
      super.onPause();
      LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
      this.getActivity().unregisterReceiver(this.broadcastReceiver);
      this.viewModel.unregisterForProgressUpdate();
   }

   public void onResume() {
      super.onResume();
      LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter("row_updated"));
      this.getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
      this.viewModel.registerForProgressUpdate(this);
   }

   public void onStartUpdate() {
      this.startProgressUpdate();
   }

   public void onStatus(int var1) {
   }

   public void onStopUpdate() {
      if (this.handler != null) {
         this.handler.removeMessages(1);
      }

   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      this.prepare();
   }

   public void tryLoadMore() {
      this.viewModel.loadMore(false);
   }
}
