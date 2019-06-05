// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.arch.lifecycle.Observer;
import org.mozilla.focus.Inject;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Message;
import android.os.Looper;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View$OnClickListener;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import java.util.List;
import org.mozilla.rocket.download.DownloadInfoPack;
import android.view.View;
import org.mozilla.focus.download.DownloadInfo;
import android.content.Intent;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.os.HandlerThread;
import android.os.Handler;
import org.mozilla.focus.widget.DownloadListAdapter;
import android.content.BroadcastReceiver;
import org.mozilla.rocket.download.DownloadInfoViewModel;

public class DownloadsFragment extends PanelFragment implements OnProgressUpdateListener
{
    private BroadcastReceiver broadcastReceiver;
    private DownloadListAdapter downloadListAdapter;
    private Handler handler;
    private HandlerThread handlerThread;
    private RecyclerView recyclerView;
    private DownloadInfoViewModel viewModel;
    
    public DownloadsFragment() {
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction() == "android.intent.action.DOWNLOAD_COMPLETE") {
                    final long longExtra = intent.getLongExtra("extra_download_id", 0L);
                    if (longExtra > 0L) {
                        DownloadsFragment.this.viewModel.notifyDownloadComplete(longExtra);
                    }
                }
                else if (intent.getAction() == "row_updated") {
                    final long longExtra2 = intent.getLongExtra("row id", 0L);
                    if (longExtra2 > 0L) {
                        DownloadsFragment.this.viewModel.notifyRowUpdate(longExtra2);
                    }
                }
            }
        };
    }
    
    private void cleanUp() {
        if (this.handlerThread != null) {
            this.handlerThread.quit();
            this.handlerThread = null;
            this.handler = null;
        }
    }
    
    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }
    
    private void prepare() {
        this.recyclerView.setAdapter((RecyclerView.Adapter)this.downloadListAdapter);
        this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this.getActivity(), 1, false));
        ((SimpleItemAnimator)this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    
    private void startProgressUpdate() {
        if (this.handlerThread == null) {
            (this.handlerThread = new HandlerThread("download-progress")).start();
            this.handler = new Handler(this.handlerThread.getLooper()) {
                public void handleMessage(final Message message) {
                    if (message.what == 1) {
                        DownloadsFragment.this.viewModel.queryDownloadProgress();
                    }
                }
            };
        }
        if (!this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessage(1);
        }
    }
    
    @Override
    public void onCompleteUpdate() {
        if (this.handler != null && !this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessageDelayed(1, 500L);
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.recyclerView = (RecyclerView)layoutInflater.inflate(2131492957, viewGroup, false);
        this.viewModel = Inject.obtainDownloadInfoViewModel(this.getActivity());
        this.downloadListAdapter = new DownloadListAdapter(this.getContext(), this.viewModel);
        this.viewModel.getDownloadInfoObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y(this));
        this.viewModel.getToastMessageObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU(this));
        this.viewModel.getDeleteSnackbarObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug(this));
        return (View)this.recyclerView;
    }
    
    @Override
    public void onDestroy() {
        this.viewModel.markAllItemsAreRead();
        Inject.obtainDownloadIndicatorViewModel(this.getActivity()).updateIndicator();
        this.cleanUp();
        super.onDestroy();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.broadcastReceiver);
        this.getActivity().unregisterReceiver(this.broadcastReceiver);
        this.viewModel.unregisterForProgressUpdate();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter("row_updated"));
        this.getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        this.viewModel.registerForProgressUpdate((DownloadInfoViewModel.OnProgressUpdateListener)this);
    }
    
    @Override
    public void onStartUpdate() {
        this.startProgressUpdate();
    }
    
    @Override
    public void onStatus(final int n) {
    }
    
    @Override
    public void onStopUpdate() {
        if (this.handler != null) {
            this.handler.removeMessages(1);
        }
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.prepare();
    }
    
    @Override
    public void tryLoadMore() {
        this.viewModel.loadMore(false);
    }
}
