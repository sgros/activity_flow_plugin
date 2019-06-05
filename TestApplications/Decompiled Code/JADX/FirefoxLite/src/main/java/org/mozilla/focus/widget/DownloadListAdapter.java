package org.mozilla.focus.widget;

import android.content.Context;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import org.mozilla.threadutils.ThreadUtils;

public class DownloadListAdapter extends Adapter<ViewHolder> {
    private static final List<String> SPECIFIC_FILE_EXTENSION = Arrays.asList(new String[]{"apk", "zip", "gz", "tar", "7z", "rar", "war"});
    private Context mContext;
    private List<DownloadInfo> mDownloadInfo;
    private DownloadInfoViewModel viewModel;

    public static class DownloadEmptyViewHolder extends ViewHolder {
        ImageView imag;

        DownloadEmptyViewHolder(View view) {
            super(view);
            this.imag = (ImageView) view.findViewById(C0427R.C0426id.img);
        }
    }

    public static class DownloadViewHolder extends ViewHolder {
        ImageView action;
        ImageView icon;
        ProgressBar progressBar;
        TextView subtitle;
        TextView title;

        DownloadViewHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(C0427R.C0426id.img);
            this.title = (TextView) view.findViewById(2131296697);
            this.subtitle = (TextView) view.findViewById(C0427R.C0426id.subtitle);
            this.action = (ImageView) view.findViewById(C0427R.C0426id.menu_action);
            this.progressBar = (ProgressBar) view.findViewById(C0427R.C0426id.progress);
        }
    }

    public static class OnOpeningViewHolder extends ViewHolder {
        OnOpeningViewHolder(View view) {
            super(view);
            view.setVisibility(8);
        }
    }

    public DownloadListAdapter(Context context, DownloadInfoViewModel downloadInfoViewModel) {
        this.mContext = context;
        this.viewModel = downloadInfoViewModel;
        this.viewModel.loadMore(true);
        this.viewModel.setOpening(true);
    }

    public void setList(List<DownloadInfo> list) {
        if (this.mDownloadInfo == null) {
            this.mDownloadInfo = list;
        }
    }

    public int getItemViewType(int i) {
        if (this.viewModel.isOpening()) {
            return 2;
        }
        return (this.mDownloadInfo == null || this.mDownloadInfo.isEmpty()) ? 0 : 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (1 == i) {
            return new DownloadViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.download_menu_cell, viewGroup, false));
        }
        if (2 == i) {
            return new OnOpeningViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.download_empty, viewGroup, false));
        }
        return new DownloadEmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0769R.layout.download_empty, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder instanceof DownloadViewHolder) {
            CharSequence stringBuilder;
            DownloadViewHolder downloadViewHolder = (DownloadViewHolder) viewHolder;
            DownloadInfo downloadInfo = (DownloadInfo) this.mDownloadInfo.get(i);
            if (TextUtils.isEmpty(downloadInfo.getFileName())) {
                downloadViewHolder.title.setText(C0769R.string.unknown);
            } else {
                downloadViewHolder.title.setText(downloadInfo.getFileName());
            }
            downloadViewHolder.icon.setImageResource(mappingIcon(downloadInfo));
            if (8 == downloadInfo.getStatus()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(downloadInfo.getSize());
                stringBuilder2.append(", ");
                stringBuilder2.append(downloadInfo.getDate());
                stringBuilder = stringBuilder2.toString();
                downloadViewHolder.progressBar.setVisibility(8);
                downloadViewHolder.action.setImageLevel(0);
            } else if (2 == downloadInfo.getStatus()) {
                int sizeSoFar = (int) ((downloadInfo.getSizeSoFar() * 100.0d) / downloadInfo.getSizeTotal());
                downloadViewHolder.progressBar.setProgress(sizeSoFar);
                downloadViewHolder.progressBar.setVisibility(0);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(sizeSoFar);
                stringBuilder3.append("%");
                stringBuilder = stringBuilder3.toString();
                downloadViewHolder.action.setImageLevel(1);
            } else {
                stringBuilder = statusConvertStr(downloadInfo.getStatus());
                downloadViewHolder.progressBar.setVisibility(8);
                downloadViewHolder.action.setImageLevel(0);
            }
            downloadViewHolder.subtitle.setText(stringBuilder);
            downloadViewHolder.action.setTag(C0427R.C0426id.status, Integer.valueOf(downloadInfo.getStatus()));
            downloadViewHolder.action.setTag(C0427R.C0426id.row_id, downloadInfo.getRowId());
            downloadViewHolder.action.setOnClickListener(new C0560-$$Lambda$DownloadListAdapter$0rq30Ts28y-ROX4x8guNtYgA6uM(this));
            downloadViewHolder.itemView.setTag(downloadInfo);
            downloadViewHolder.itemView.setOnClickListener(new C0564-$$Lambda$DownloadListAdapter$rmqxPk6X72MzbSacIqXzLvkcKPY(this));
        } else if (viewHolder instanceof OnOpeningViewHolder) {
            viewHolder.itemView.setVisibility(8);
        }
    }

    public static /* synthetic */ void lambda$onBindViewHolder$1(DownloadListAdapter downloadListAdapter, View view) {
        long longValue = ((Long) view.getTag(C0427R.C0426id.row_id)).longValue();
        if (((Integer) view.getTag(C0427R.C0426id.status)).intValue() == 2) {
            downloadListAdapter.viewModel.cancel(longValue);
        } else {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(2131558403, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new C0561-$$Lambda$DownloadListAdapter$2RfKvQKet9-QfTnm5kGkUXuOnrw(downloadListAdapter, longValue, popupMenu));
            popupMenu.show();
        }
        TelemetryWrapper.showFileContextMenu();
    }

    public static /* synthetic */ boolean lambda$null$0(DownloadListAdapter downloadListAdapter, long j, PopupMenu popupMenu, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == C0427R.C0426id.delete) {
            downloadListAdapter.viewModel.delete(j);
            TelemetryWrapper.downloadDeleteFile();
            popupMenu.dismiss();
            return true;
        } else if (itemId != C0427R.C0426id.remove) {
            return false;
        } else {
            downloadListAdapter.viewModel.remove(j);
            TelemetryWrapper.downloadRemoveFile();
            popupMenu.dismiss();
            return true;
        }
    }

    public static /* synthetic */ void lambda$onBindViewHolder$4(DownloadListAdapter downloadListAdapter, View view) {
        DownloadInfo downloadInfo = (DownloadInfo) view.getTag();
        if (downloadInfo.getStatus() == 8) {
            TelemetryWrapper.downloadOpenFile(false);
            ThreadUtils.postToBackgroundThread(new C0563-$$Lambda$DownloadListAdapter$qK4PYdp6P8b6gF442lg254YtYgw(downloadListAdapter, downloadInfo, view));
        }
    }

    public static /* synthetic */ void lambda$null$2(DownloadListAdapter downloadListAdapter, boolean z, View view, DownloadInfo downloadInfo) {
        if (z) {
            IntentUtils.intentOpenFile(view.getContext(), downloadInfo.getFileUri(), downloadInfo.getMimeType());
        } else {
            Toast.makeText(downloadListAdapter.mContext, C0769R.string.cannot_find_the_file, 1).show();
        }
    }

    public int getItemCount() {
        return (this.mDownloadInfo == null || this.mDownloadInfo.isEmpty()) ? 1 : this.mDownloadInfo.size();
    }

    private String statusConvertStr(int i) {
        if (i == 4) {
            return this.mContext.getResources().getString(C0769R.string.pause);
        }
        if (i == 8) {
            return this.mContext.getResources().getString(C0769R.string.successful);
        }
        if (i == 16) {
            return this.mContext.getResources().getString(C0769R.string.failed);
        }
        switch (i) {
            case 1:
                return this.mContext.getResources().getString(C0769R.string.pending);
            case 2:
                return this.mContext.getResources().getString(C0769R.string.running);
            default:
                return this.mContext.getResources().getString(C0769R.string.unknown);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0085 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0092 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0085 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0092 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0085 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0092 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0086  */
    /* JADX WARNING: Missing block: B:27:0x007e, code skipped:
            if (r6.equals("text") != false) goto L_0x0082;
     */
    private int mappingIcon(org.mozilla.focus.download.DownloadInfo r6) {
        /*
        r5 = this;
        r0 = SPECIFIC_FILE_EXTENSION;
        r1 = r6.getFileExtension();
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0020;
    L_0x000c:
        r0 = "apk";
        r6 = r6.getFileExtension();
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x001c;
    L_0x0018:
        r6 = 2131230859; // 0x7f08008b float:1.8077783E38 double:1.052967951E-314;
        goto L_0x001f;
    L_0x001c:
        r6 = 2131230860; // 0x7f08008c float:1.8077785E38 double:1.0529679513E-314;
    L_0x001f:
        return r6;
    L_0x0020:
        r0 = r6.getMimeType();
        r0 = android.text.TextUtils.isEmpty(r0);
        r1 = 2131230861; // 0x7f08008d float:1.8077787E38 double:1.052967952E-314;
        if (r0 != 0) goto L_0x0093;
    L_0x002d:
        r0 = r6.getMimeType();
        r6 = r6.getMimeType();
        r2 = "/";
        r6 = r6.indexOf(r2);
        r2 = 0;
        r6 = r0.substring(r2, r6);
        r0 = -1;
        r3 = r6.hashCode();
        r4 = 3556653; // 0x36452d float:4.983932E-39 double:1.75722E-317;
        if (r3 == r4) goto L_0x0078;
    L_0x004a:
        r2 = 93166550; // 0x58d9bd6 float:1.3316821E-35 double:4.60303917E-316;
        if (r3 == r2) goto L_0x006e;
    L_0x004f:
        r2 = 100313435; // 0x5faa95b float:2.3572098E-35 double:4.9561422E-316;
        if (r3 == r2) goto L_0x0064;
    L_0x0054:
        r2 = 112202875; // 0x6b0147b float:6.6233935E-35 double:5.5435586E-316;
        if (r3 == r2) goto L_0x005a;
    L_0x0059:
        goto L_0x0081;
    L_0x005a:
        r2 = "video";
        r6 = r6.equals(r2);
        if (r6 == 0) goto L_0x0081;
    L_0x0062:
        r2 = 3;
        goto L_0x0082;
    L_0x0064:
        r2 = "image";
        r6 = r6.equals(r2);
        if (r6 == 0) goto L_0x0081;
    L_0x006c:
        r2 = 1;
        goto L_0x0082;
    L_0x006e:
        r2 = "audio";
        r6 = r6.equals(r2);
        if (r6 == 0) goto L_0x0081;
    L_0x0076:
        r2 = 2;
        goto L_0x0082;
    L_0x0078:
        r3 = "text";
        r6 = r6.equals(r3);
        if (r6 == 0) goto L_0x0081;
    L_0x0080:
        goto L_0x0082;
    L_0x0081:
        r2 = -1;
    L_0x0082:
        switch(r2) {
            case 0: goto L_0x0092;
            case 1: goto L_0x008e;
            case 2: goto L_0x008a;
            case 3: goto L_0x0086;
            default: goto L_0x0085;
        };
    L_0x0085:
        return r1;
    L_0x0086:
        r6 = 2131230864; // 0x7f080090 float:1.8077793E38 double:1.0529679533E-314;
        return r6;
    L_0x008a:
        r6 = 2131230863; // 0x7f08008f float:1.807779E38 double:1.052967953E-314;
        return r6;
    L_0x008e:
        r6 = 2131230862; // 0x7f08008e float:1.8077789E38 double:1.0529679523E-314;
        return r6;
    L_0x0092:
        return r1;
    L_0x0093:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.widget.DownloadListAdapter.mappingIcon(org.mozilla.focus.download.DownloadInfo):int");
    }
}
