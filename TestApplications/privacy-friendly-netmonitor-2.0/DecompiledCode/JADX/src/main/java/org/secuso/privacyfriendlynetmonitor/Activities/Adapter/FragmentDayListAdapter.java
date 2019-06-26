package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.p003v7.widget.RecyclerView.C0359Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.PrintStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.HistoryDetailActivity;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;

public class FragmentDayListAdapter extends C0359Adapter<ViewHolder> {
    Context context;
    List<ReportEntity> reportEntities;

    public static class ViewHolder extends android.support.p003v7.widget.RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;

        public ViewHolder(RelativeLayout relativeLayout) {
            super(relativeLayout);
            this.relativeLayout = relativeLayout;
        }
    }

    public FragmentDayListAdapter(List<ReportEntity> list, Context context) {
        this.reportEntities = list;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder((RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(C0501R.layout.fragment_list_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ReportEntity reportEntity = (ReportEntity) this.reportEntities.get(i);
        TextView textView = (TextView) viewHolder.relativeLayout.findViewById(C0501R.C0499id.fragment_timestamp_value);
        TextView textView2 = (TextView) viewHolder.relativeLayout.findViewById(C0501R.C0499id.fragment_conncection_info_value);
        ((TextView) viewHolder.relativeLayout.findViewById(C0501R.C0499id.fragment_appname)).setText(reportEntity.getRemoteAddress());
        textView.setText(reportEntity.getTimeStamp());
        textView2.setText(reportEntity.getConnectionInfo());
        viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List access$000 = FragmentDayListAdapter.this.prepareData(reportEntity);
                Intent intent = new Intent(FragmentDayListAdapter.this.context, HistoryDetailActivity.class);
                intent.putExtra("Details", (ArrayList) access$000);
                FragmentDayListAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.reportEntities.size();
    }

    private List<String> prepareData(ReportEntity reportEntity) {
        PackageInfo packageInfo;
        PackageManager packageManager = this.context.getPackageManager();
        ArrayList arrayList = new ArrayList();
        String appName = reportEntity.getAppName();
        arrayList.add(appName);
        arrayList.add(reportEntity.getUserID());
        try {
            packageInfo = packageManager.getPackageInfo(appName, 0);
        } catch (NameNotFoundException unused) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find package info for ");
            stringBuilder.append(appName);
            stringBuilder.append(".");
            printStream.println(stringBuilder.toString());
            packageInfo = null;
        }
        arrayList.add(packageInfo.versionName);
        arrayList.add(new Date(packageInfo.firstInstallTime).toString());
        arrayList.add(reportEntity.getRemoteAddress());
        arrayList.add(reportEntity.getRemoteHex());
        arrayList.add(reportEntity.getRemoteHost());
        arrayList.add(reportEntity.getLocalAddress());
        arrayList.add(reportEntity.getLocalHex());
        arrayList.add(reportEntity.getServicePort());
        arrayList.add(reportEntity.getPayloadProtocol());
        arrayList.add(reportEntity.getTransportProtocol());
        arrayList.add(reportEntity.getLocalPort());
        arrayList.add(reportEntity.getTimeStamp());
        arrayList.add(reportEntity.getConnectionInfo());
        return arrayList;
    }
}
