// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import android.text.TextUtils;
import android.widget.PopupMenu$OnMenuItemClickListener;
import org.mozilla.threadutils.ThreadUtils;
import java.io.File;
import java.net.URI;
import android.widget.Toast;
import org.mozilla.focus.utils.IntentUtils;
import android.view.View;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.view.MenuItem;
import android.widget.PopupMenu;
import java.util.Arrays;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import org.mozilla.focus.download.DownloadInfo;
import android.content.Context;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class DownloadListAdapter extends Adapter<ViewHolder>
{
    private static final List<String> SPECIFIC_FILE_EXTENSION;
    private Context mContext;
    private List<DownloadInfo> mDownloadInfo;
    private DownloadInfoViewModel viewModel;
    
    static {
        SPECIFIC_FILE_EXTENSION = Arrays.asList("apk", "zip", "gz", "tar", "7z", "rar", "war");
    }
    
    public DownloadListAdapter(final Context mContext, final DownloadInfoViewModel viewModel) {
        this.mContext = mContext;
        (this.viewModel = viewModel).loadMore(true);
        this.viewModel.setOpening(true);
    }
    
    private int mappingIcon(final DownloadInfo downloadInfo) {
        if (DownloadListAdapter.SPECIFIC_FILE_EXTENSION.contains(downloadInfo.getFileExtension())) {
            int n;
            if ("apk".equals(downloadInfo.getFileExtension())) {
                n = 2131230859;
            }
            else {
                n = 2131230860;
            }
            return n;
        }
        if (TextUtils.isEmpty((CharSequence)downloadInfo.getMimeType())) {
            return 2131230861;
        }
        final String mimeType = downloadInfo.getMimeType();
        final int index = downloadInfo.getMimeType().indexOf("/");
        int n2 = 0;
        final String substring = mimeType.substring(0, index);
        final int hashCode = substring.hashCode();
        Label_0175: {
            if (hashCode != 3556653) {
                if (hashCode != 93166550) {
                    if (hashCode != 100313435) {
                        if (hashCode == 112202875) {
                            if (substring.equals("video")) {
                                n2 = 3;
                                break Label_0175;
                            }
                        }
                    }
                    else if (substring.equals("image")) {
                        n2 = 1;
                        break Label_0175;
                    }
                }
                else if (substring.equals("audio")) {
                    n2 = 2;
                    break Label_0175;
                }
            }
            else if (substring.equals("text")) {
                break Label_0175;
            }
            n2 = -1;
        }
        switch (n2) {
            default: {
                return 2131230861;
            }
            case 3: {
                return 2131230864;
            }
            case 2: {
                return 2131230863;
            }
            case 1: {
                return 2131230862;
            }
            case 0: {
                return 2131230861;
            }
        }
    }
    
    private String statusConvertStr(final int n) {
        if (n == 4) {
            return this.mContext.getResources().getString(2131755285);
        }
        if (n == 8) {
            return this.mContext.getResources().getString(2131755414);
        }
        if (n == 16) {
            return this.mContext.getResources().getString(2131755195);
        }
        switch (n) {
            default: {
                return this.mContext.getResources().getString(2131755425);
            }
            case 2: {
                return this.mContext.getResources().getString(2131755373);
            }
            case 1: {
                return this.mContext.getResources().getString(2131755286);
            }
        }
    }
    
    @Override
    public int getItemCount() {
        if (this.mDownloadInfo != null && !this.mDownloadInfo.isEmpty()) {
            return this.mDownloadInfo.size();
        }
        return 1;
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (this.viewModel.isOpening()) {
            return 2;
        }
        if (this.mDownloadInfo != null && !this.mDownloadInfo.isEmpty()) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int n) {
        if (viewHolder instanceof DownloadViewHolder) {
            final DownloadViewHolder downloadViewHolder = (DownloadViewHolder)viewHolder;
            final DownloadInfo tag = this.mDownloadInfo.get(n);
            if (!TextUtils.isEmpty((CharSequence)tag.getFileName())) {
                downloadViewHolder.title.setText((CharSequence)tag.getFileName());
            }
            else {
                downloadViewHolder.title.setText(2131755425);
            }
            downloadViewHolder.icon.setImageResource(this.mappingIcon(tag));
            String text;
            if (8 == tag.getStatus()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(tag.getSize());
                sb.append(", ");
                sb.append(tag.getDate());
                text = sb.toString();
                downloadViewHolder.progressBar.setVisibility(8);
                downloadViewHolder.action.setImageLevel(0);
            }
            else if (2 == tag.getStatus()) {
                n = (int)(tag.getSizeSoFar() * 100.0 / tag.getSizeTotal());
                downloadViewHolder.progressBar.setProgress(n);
                downloadViewHolder.progressBar.setVisibility(0);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(n);
                sb2.append("%");
                text = sb2.toString();
                downloadViewHolder.action.setImageLevel(1);
            }
            else {
                text = this.statusConvertStr(tag.getStatus());
                downloadViewHolder.progressBar.setVisibility(8);
                downloadViewHolder.action.setImageLevel(0);
            }
            downloadViewHolder.subtitle.setText((CharSequence)text);
            downloadViewHolder.action.setTag(2131296669, (Object)tag.getStatus());
            downloadViewHolder.action.setTag(2131296597, (Object)tag.getRowId());
            downloadViewHolder.action.setOnClickListener((View$OnClickListener)new _$$Lambda$DownloadListAdapter$0rq30Ts28y_ROX4x8guNtYgA6uM(this));
            downloadViewHolder.itemView.setTag((Object)tag);
            downloadViewHolder.itemView.setOnClickListener((View$OnClickListener)new _$$Lambda$DownloadListAdapter$rmqxPk6X72MzbSacIqXzLvkcKPY(this));
        }
        else if (viewHolder instanceof OnOpeningViewHolder) {
            viewHolder.itemView.setVisibility(8);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (1 == n) {
            return new DownloadViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492946, viewGroup, false));
        }
        if (2 == n) {
            return new OnOpeningViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492944, viewGroup, false));
        }
        return new DownloadEmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131492944, viewGroup, false));
    }
    
    public void setList(final List<DownloadInfo> mDownloadInfo) {
        if (this.mDownloadInfo == null) {
            this.mDownloadInfo = mDownloadInfo;
        }
    }
    
    public static class DownloadEmptyViewHolder extends ViewHolder
    {
        ImageView imag;
        
        DownloadEmptyViewHolder(final View view) {
            super(view);
            this.imag = (ImageView)view.findViewById(2131296480);
        }
    }
    
    public static class DownloadViewHolder extends ViewHolder
    {
        ImageView action;
        ImageView icon;
        ProgressBar progressBar;
        TextView subtitle;
        TextView title;
        
        DownloadViewHolder(final View view) {
            super(view);
            this.icon = (ImageView)view.findViewById(2131296480);
            this.title = (TextView)view.findViewById(2131296697);
            this.subtitle = (TextView)view.findViewById(2131296675);
            this.action = (ImageView)view.findViewById(2131296504);
            this.progressBar = (ProgressBar)view.findViewById(2131296576);
        }
    }
    
    public static class OnOpeningViewHolder extends ViewHolder
    {
        OnOpeningViewHolder(final View view) {
            super(view);
            view.setVisibility(8);
        }
    }
}
