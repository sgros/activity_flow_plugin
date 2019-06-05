// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
import java.io.Serializable;
import android.content.Intent;
import org.secuso.privacyfriendlynetmonitor.Activities.HistoryDetailActivity;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.TextView;
import java.io.PrintStream;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.sql.Date;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.ArrayList;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class FragmentDayListAdapter extends Adapter<ViewHolder>
{
    Context context;
    List<ReportEntity> reportEntities;
    
    public FragmentDayListAdapter(final List<ReportEntity> reportEntities, final Context context) {
        this.reportEntities = reportEntities;
        this.context = context;
    }
    
    private List<String> prepareData(final ReportEntity reportEntity) {
        final PackageManager packageManager = this.context.getPackageManager();
        final ArrayList<String> list = new ArrayList<String>();
        final String appName = reportEntity.getAppName();
        list.add(appName);
        list.add(reportEntity.getUserID());
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(appName, 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find package info for ");
            sb.append(appName);
            sb.append(".");
            out.println(sb.toString());
            packageInfo = null;
        }
        list.add(packageInfo.versionName);
        list.add(new Date(packageInfo.firstInstallTime).toString());
        list.add(reportEntity.getRemoteAddress());
        list.add(reportEntity.getRemoteHex());
        list.add(reportEntity.getRemoteHost());
        list.add(reportEntity.getLocalAddress());
        list.add(reportEntity.getLocalHex());
        list.add(reportEntity.getServicePort());
        list.add(reportEntity.getPayloadProtocol());
        list.add(reportEntity.getTransportProtocol());
        list.add(reportEntity.getLocalPort());
        list.add(reportEntity.getTimeStamp());
        list.add(reportEntity.getConnectionInfo());
        return list;
    }
    
    @Override
    public int getItemCount() {
        return this.reportEntities.size();
    }
    
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final ReportEntity reportEntity = this.reportEntities.get(n);
        final TextView textView = (TextView)viewHolder.relativeLayout.findViewById(2131296352);
        final TextView textView2 = (TextView)viewHolder.relativeLayout.findViewById(2131296356);
        final TextView textView3 = (TextView)viewHolder.relativeLayout.findViewById(2131296354);
        textView.setText((CharSequence)reportEntity.getRemoteAddress());
        textView2.setText((CharSequence)reportEntity.getTimeStamp());
        textView3.setText((CharSequence)reportEntity.getConnectionInfo());
        viewHolder.relativeLayout.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final List access$000 = FragmentDayListAdapter.this.prepareData(reportEntity);
                final Intent intent = new Intent(FragmentDayListAdapter.this.context, (Class)HistoryDetailActivity.class);
                intent.putExtra("Details", (Serializable)(ArrayList<?>)access$000);
                FragmentDayListAdapter.this.context.startActivity(intent);
            }
        });
    }
    
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new ViewHolder((RelativeLayout)LayoutInflater.from(viewGroup.getContext()).inflate(2131427390, viewGroup, false));
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public RelativeLayout relativeLayout;
        
        public ViewHolder(final RelativeLayout relativeLayout) {
            super((View)relativeLayout);
            this.relativeLayout = relativeLayout;
        }
    }
}
