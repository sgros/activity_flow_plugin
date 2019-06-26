// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.List;
import android.content.Context;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.app.Activity;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Bundle;

public class ReportDetailActivity extends BaseActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427365);
        RunStore.setContext(this);
        final DetailAdapter adapter = new DetailAdapter((Context)this, 2131427411, Collector.sDetailReportList);
        final ListView listView = this.findViewById(2131296446);
        listView.setAdapter((ListAdapter)adapter);
        final View inflate = this.getLayoutInflater().inflate(2131427413, (ViewGroup)null);
        listView.addHeaderView(inflate);
        final Report sDetailReport = Collector.sDetailReport;
        ((ImageView)inflate.findViewById(2131296438)).setImageDrawable(Collector.getIcon(sDetailReport.uid));
        ((TextView)inflate.findViewById(2131296442)).setText((CharSequence)Collector.getLabel(sDetailReport.uid));
        ((TextView)inflate.findViewById(2131296440)).setText((CharSequence)Collector.getPackage(sDetailReport.uid));
        if (this.mSharedPreferences.getBoolean("IS_CERTVAL", false) && Collector.hasHostName(sDetailReport.remoteAdd.getHostAddress()) && Collector.hasGrade(Collector.getDnsHostName(sDetailReport.remoteAdd.getHostAddress()))) {
            final TextView textView = this.findViewById(2131296447);
            textView.setVisibility(0);
            textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("https://www.ssllabs.com/ssltest/analyze.html?d=");
                    sb.append(Collector.getCertHost(Collector.getDnsHostName(sDetailReport.remoteAdd.getHostAddress())));
                    ReportDetailActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                }
            });
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
    }
    
    class DetailAdapter extends ArrayAdapter<String[]>
    {
        DetailAdapter(final Context context, final int n, final List<String[]> list) {
            super(context, n, (List)list);
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            ReportDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
            View inflate = view;
            if (view == null) {
                inflate = LayoutInflater.from(this.getContext()).inflate(2131427411, (ViewGroup)null);
            }
            final String[] array = (String[])this.getItem(n);
            final TextView textView = (TextView)inflate.findViewById(2131296444);
            final TextView textView2 = (TextView)inflate.findViewById(2131296445);
            if (array[0] != null && array[1] != null) {
                textView.setText((CharSequence)array[0]);
                textView2.setText((CharSequence)array[1]);
            }
            else {
                textView.setText((CharSequence)"");
                textView2.setText((CharSequence)"");
            }
            return inflate;
        }
    }
}
