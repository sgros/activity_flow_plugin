package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;

public class ReportDetailActivity extends BaseActivity {

    class DetailAdapter extends ArrayAdapter<String[]> {
        DetailAdapter(Context context, int i, List<String[]> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ReportDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(C0501R.layout.report_detail_item, null);
            }
            String[] strArr = (String[]) getItem(i);
            TextView textView = (TextView) view.findViewById(C0501R.C0499id.report_detail_item_type);
            TextView textView2 = (TextView) view.findViewById(C0501R.C0499id.report_detail_item_value);
            if (strArr[0] == null || strArr[1] == null) {
                textView.setText("");
                textView2.setText("");
            } else {
                textView.setText(strArr[0]);
                textView2.setText(strArr[1]);
            }
            return view;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0501R.layout.activity_report_detail);
        RunStore.setContext(this);
        ListView listView = (ListView) findViewById(C0501R.C0499id.report_detail_list_view);
        listView.setAdapter(new DetailAdapter(this, C0501R.layout.report_detail_item, Collector.sDetailReportList));
        View inflate = getLayoutInflater().inflate(C0501R.layout.report_list_group_header, null);
        listView.addHeaderView(inflate);
        final Report report = Collector.sDetailReport;
        ((ImageView) inflate.findViewById(C0501R.C0499id.reportGroupIcon_header)).setImageDrawable(Collector.getIcon(report.uid));
        ((TextView) inflate.findViewById(C0501R.C0499id.reportGroupTitle_header)).setText(Collector.getLabel(report.uid));
        ((TextView) inflate.findViewById(C0501R.C0499id.reportGroupSubtitle_header)).setText(Collector.getPackage(report.uid));
        if (this.mSharedPreferences.getBoolean(Const.IS_CERTVAL, false) && Collector.hasHostName(report.remoteAdd.getHostAddress()).booleanValue() && Collector.hasGrade(Collector.getDnsHostName(report.remoteAdd.getHostAddress()))) {
            TextView textView = (TextView) findViewById(C0501R.C0499id.report_detail_ssllabs_result);
            textView.setVisibility(0);
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Const.SSLLABS_URL);
                    stringBuilder.append(Collector.getCertHost(Collector.getDnsHostName(report.remoteAdd.getHostAddress())));
                    ReportDetailActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())));
                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
