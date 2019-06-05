// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.widget.ImageView;
import android.graphics.Typeface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.view.LayoutInflater;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.view.ViewGroup;
import android.view.View;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;
import java.util.List;
import java.util.HashMap;
import android.content.Context;
import android.widget.BaseExpandableListAdapter;

public class ExpandableReportAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private HashMap<Integer, List<Report>> reportListDetail;
    private List<Integer> uidList;
    
    public ExpandableReportAdapter(final Context context, final List<Integer> uidList, final HashMap<Integer, List<Report>> reportListDetail) {
        this.context = context;
        this.uidList = uidList;
        this.reportListDetail = reportListDetail;
    }
    
    private int getWarningColor(final String s) {
        if (s.contains("Encrypted") || s.substring(0, 1).equals("A")) {
            return 2131099715;
        }
        if (s.contains("Inconclusive") || s.substring(0, 1).equals("B") || s.substring(0, 1).equals("C")) {
            return 2131099734;
        }
        if (!s.contains("Unencrypted") && !s.substring(0, 1).equals("T") && !s.substring(0, 1).equals("F") && !s.substring(0, 1).equals("D") && !s.substring(0, 1).equals("E")) {
            return 2131099756;
        }
        return 2131099743;
    }
    
    public Object getChild(final int n, final int n2) {
        return this.reportListDetail.get(this.uidList.get(n)).get(n2);
    }
    
    public long getChildId(final int n, final int n2) {
        return n2;
    }
    
    public View getChildView(final int n, final int n2, final boolean b, final View view, final ViewGroup viewGroup) {
        final Report report = (Report)this.getChild(n, n2);
        String text;
        if (Collector.hasHostName(report.remoteAdd.getHostAddress())) {
            text = Collector.getDnsHostName(report.remoteAdd.getHostAddress());
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(report.remoteAdd.getHostAddress());
            text = sb.toString();
        }
        String text2;
        String text3;
        if (Collector.isCertVal && KnownPorts.isTlsPort(report.remotePort) && Collector.hasHostName(report.remoteAdd.getHostAddress())) {
            if (text.equals(Collector.getCertHost(text))) {
                text2 = "SSL Server Rating:";
                text3 = Collector.getMetric(text);
            }
            else {
                text2 = "SSL Server Rating:";
                if (text.equals(Collector.getCertHost(text))) {
                    text3 = Collector.getMetric(text);
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(Collector.getMetric(text));
                    sb2.append(" (");
                    sb2.append(Collector.getCertHost(text));
                    sb2.append(")");
                    text3 = sb2.toString();
                }
            }
        }
        else {
            text3 = KnownPorts.CompileConnectionInfo(report.remotePort, report.type);
            text2 = "Connection Info:";
        }
        View inflate = view;
        if (view == null) {
            inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427414, (ViewGroup)null);
        }
        ((TextView)inflate.findViewById(2131296450)).setText((CharSequence)text);
        ((TextView)inflate.findViewById(2131296451)).setText((CharSequence)text2);
        final TextView textView = (TextView)inflate.findViewById(2131296452);
        textView.setText((CharSequence)text3);
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        textView.setTextColor(this.context.getResources().getColor(this.getWarningColor(text3)));
        if (!defaultSharedPreferences.getBoolean("IS_HIGHLIGHTED", true)) {
            textView.setTextColor(2131099756);
        }
        return inflate;
    }
    
    public int getChildrenCount(final int n) {
        return this.reportListDetail.get(this.uidList.get(n)).size();
    }
    
    public Object getGroup(final int n) {
        return this.uidList.get(n);
    }
    
    public int getGroupCount() {
        return this.uidList.size();
    }
    
    public long getGroupId(final int n) {
        return n;
    }
    
    public View getGroupView(int intValue, final boolean b, final View view, final ViewGroup viewGroup) {
        intValue = (int)this.getGroup(intValue);
        View inflate = view;
        if (view == null) {
            inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427412, (ViewGroup)null);
        }
        final TextView textView = (TextView)inflate.findViewById(2131296441);
        textView.setTypeface((Typeface)null, 1);
        final TextView textView2 = (TextView)inflate.findViewById(2131296439);
        final ImageView imageView = (ImageView)inflate.findViewById(2131296437);
        if (intValue <= 10000) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Collector.getLabel(intValue));
            sb.append(" (");
            sb.append(this.reportListDetail.get(intValue).size());
            sb.append(") [System]");
            textView.setText((CharSequence)sb.toString());
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Collector.getLabel(intValue));
            sb2.append(" (");
            sb2.append(this.reportListDetail.get(intValue).size());
            sb2.append(")");
            textView.setText((CharSequence)sb2.toString());
        }
        textView2.setText((CharSequence)Collector.getPackage(intValue));
        imageView.setImageDrawable(Collector.getIcon(intValue));
        return inflate;
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    public boolean isChildSelectable(final int n, final int n2) {
        return true;
    }
}
