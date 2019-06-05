package org.mozilla.focus.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.support.p004v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.mozilla.focus.Inject;
import org.mozilla.focus.widget.DownloadListAdapter;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadInfoPack;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import org.mozilla.rocket.download.DownloadInfoViewModel.OnProgressUpdateListener;

public class DownloadsFragment extends PanelFragment implements OnProgressUpdateListener {
    private BroadcastReceiver broadcastReceiver = new C04691();
    private DownloadListAdapter downloadListAdapter;
    private Handler handler;
    private HandlerThread handlerThread;
    private RecyclerView recyclerView;
    private DownloadInfoViewModel viewModel;

    /* renamed from: org.mozilla.focus.fragment.DownloadsFragment$1 */
    class C04691 extends BroadcastReceiver {
        C04691() {
        }

        public void onReceive(Context context, Intent intent) {
            long longExtra;
            if (intent.getAction() == "android.intent.action.DOWNLOAD_COMPLETE") {
                longExtra = intent.getLongExtra("extra_download_id", 0);
                if (longExtra > 0) {
                    DownloadsFragment.this.viewModel.notifyDownloadComplete(longExtra);
                }
            } else if (intent.getAction() == "row_updated") {
                longExtra = intent.getLongExtra("row id", 0);
                if (longExtra > 0) {
                    DownloadsFragment.this.viewModel.notifyRowUpdate(longExtra);
                }
            }
        }
    }

    public void onStatus(int i) {
    }

    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.recyclerView = (RecyclerView) layoutInflater.inflate(C0769R.layout.fragment_downloads, viewGroup, false);
        this.viewModel = Inject.obtainDownloadInfoViewModel(getActivity());
        this.downloadListAdapter = new DownloadListAdapter(getContext(), this.viewModel);
        this.viewModel.getDownloadInfoObservable().observe(getViewLifecycleOwner(), new C0690-$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y(this));
        this.viewModel.getToastMessageObservable().observe(getViewLifecycleOwner(), new C0692-$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU(this));
        this.viewModel.getDeleteSnackbarObservable().observe(getViewLifecycleOwner(), new C0691-$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug(this));
        return this.recyclerView;
    }

    public static /* synthetic */ void lambda$onCreateView$0(DownloadsFragment downloadsFragment, DownloadInfoPack downloadInfoPack) {
        if (downloadInfoPack != null) {
            switch (downloadInfoPack.getNotifyType()) {
                case 1:
                    downloadsFragment.downloadListAdapter.setList(downloadInfoPack.getList());
                    downloadsFragment.downloadListAdapter.notifyDataSetChanged();
                    return;
                case 2:
                    downloadsFragment.downloadListAdapter.notifyItemInserted((int) downloadInfoPack.getIndex());
                    return;
                case 3:
                    downloadsFragment.downloadListAdapter.notifyItemRemoved((int) downloadInfoPack.getIndex());
                    return;
                case 4:
                    downloadsFragment.downloadListAdapter.notifyItemChanged((int) downloadInfoPack.getIndex());
                    return;
                default:
                    return;
            }
        }
    }

    public void tryLoadMore() {
        this.viewModel.loadMore(false);
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter("row_updated"));
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        this.viewModel.registerForProgressUpdate(this);
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.broadcastReceiver);
        getActivity().unregisterReceiver(this.broadcastReceiver);
        this.viewModel.unregisterForProgressUpdate();
    }

    public void onDestroy() {
        this.viewModel.markAllItemsAreRead();
        Inject.obtainDownloadIndicatorViewModel(getActivity()).updateIndicator();
        cleanUp();
        super.onDestroy();
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        prepare();
    }

    private void prepare() {
        this.recyclerView.setAdapter(this.downloadListAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        ((SimpleItemAnimator) this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void onStartUpdate() {
        startProgressUpdate();
    }

    public void onCompleteUpdate() {
        if (this.handler != null && !this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    public void onStopUpdate() {
        if (this.handler != null) {
            this.handler.removeMessages(1);
        }
    }

    private void startProgressUpdate() {
        if (this.handlerThread == null) {
            this.handlerThread = new HandlerThread("download-progress");
            this.handlerThread.start();
            this.handler = new Handler(this.handlerThread.getLooper()) {
                public void handleMessage(Message message) {
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

    private void cleanUp() {
        if (this.handlerThread != null) {
            this.handlerThread.quit();
            this.handlerThread = null;
            this.handler = null;
        }
    }
}
