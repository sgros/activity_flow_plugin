package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;

public class ExpandableReportAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<Integer, List<Report>> reportListDetail;
    private List<Integer> uidList;

    public long getChildId(int i, int i2) {
        return (long) i2;
    }

    public long getGroupId(int i) {
        return (long) i;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public ExpandableReportAdapter(Context context, List<Integer> list, HashMap<Integer, List<Report>> hashMap) {
        this.context = context;
        this.uidList = list;
        this.reportListDetail = hashMap;
    }

    public Object getChild(int i, int i2) {
        return ((List) this.reportListDetail.get(this.uidList.get(i))).get(i2);
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        String dnsHostName;
        CharSequence CompileConnectionInfo;
        CharSequence charSequence;
        Report report = (Report) getChild(i, i2);
        if (Collector.hasHostName(report.remoteAdd.getHostAddress()).booleanValue()) {
            dnsHostName = Collector.getDnsHostName(report.remoteAdd.getHostAddress());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(report.remoteAdd.getHostAddress());
            dnsHostName = stringBuilder.toString();
        }
        if (!Collector.isCertVal.booleanValue() || !KnownPorts.isTlsPort(report.remotePort) || !Collector.hasHostName(report.remoteAdd.getHostAddress()).booleanValue()) {
            CompileConnectionInfo = KnownPorts.CompileConnectionInfo(report.remotePort, report.type);
            charSequence = "Connection Info:";
        } else if (dnsHostName.equals(Collector.getCertHost(dnsHostName))) {
            charSequence = "SSL Server Rating:";
            CompileConnectionInfo = Collector.getMetric(dnsHostName);
        } else {
            charSequence = "SSL Server Rating:";
            if (dnsHostName.equals(Collector.getCertHost(dnsHostName))) {
                CompileConnectionInfo = Collector.getMetric(dnsHostName);
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(Collector.getMetric(dnsHostName));
                stringBuilder2.append(" (");
                stringBuilder2.append(Collector.getCertHost(dnsHostName));
                stringBuilder2.append(")");
                CompileConnectionInfo = stringBuilder2.toString();
            }
        }
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.report_list_item, null);
        }
        ((TextView) view.findViewById(C0501R.C0499id.report_item_1)).setText(dnsHostName);
        ((TextView) view.findViewById(C0501R.C0499id.report_item_2_type)).setText(charSequence);
        TextView textView = (TextView) view.findViewById(C0501R.C0499id.report_item_2_val);
        textView.setText(CompileConnectionInfo);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        textView.setTextColor(this.context.getResources().getColor(getWarningColor(CompileConnectionInfo)));
        if (!defaultSharedPreferences.getBoolean(Const.IS_HIGHLIGHTED, true)) {
            textView.setTextColor(C0501R.color.text_dark);
        }
        return view;
    }

    private int getWarningColor(String str) {
        if (str.contains(Const.STATUS_TLS) || str.substring(0, 1).equals("A")) {
            return C0501R.color.green;
        }
        if (str.contains(Const.STATUS_INCONCLUSIVE) || str.substring(0, 1).equals("B") || str.substring(0, 1).equals("C")) {
            return C0501R.color.orange;
        }
        return (str.contains(Const.STATUS_UNSECURE) || str.substring(0, 1).equals("T") || str.substring(0, 1).equals("F") || str.substring(0, 1).equals("D") || str.substring(0, 1).equals("E")) ? C0501R.color.red : C0501R.color.text_dark;
    }

    public int getChildrenCount(int i) {
        return ((List) this.reportListDetail.get(this.uidList.get(i))).size();
    }

    public Object getGroup(int i) {
        return this.uidList.get(i);
    }

    public int getGroupCount() {
        return this.uidList.size();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        i = ((Integer) getGroup(i)).intValue();
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.report_list_group, null);
        }
        TextView textView = (TextView) view.findViewById(C0501R.C0499id.reportGroupTitle);
        textView.setTypeface(null, 1);
        TextView textView2 = (TextView) view.findViewById(C0501R.C0499id.reportGroupSubtitle);
        ImageView imageView = (ImageView) view.findViewById(C0501R.C0499id.reportGroupIcon);
        StringBuilder stringBuilder;
        if (i <= 10000) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(Collector.getLabel(i));
            stringBuilder.append(" (");
            stringBuilder.append(((List) this.reportListDetail.get(Integer.valueOf(i))).size());
            stringBuilder.append(") [System]");
            textView.setText(stringBuilder.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(Collector.getLabel(i));
            stringBuilder.append(" (");
            stringBuilder.append(((List) this.reportListDetail.get(Integer.valueOf(i))).size());
            stringBuilder.append(")");
            textView.setText(stringBuilder.toString());
        }
        textView2.setText(Collector.getPackage(i));
        imageView.setImageDrawable(Collector.getIcon(i));
        return view;
    }
}
