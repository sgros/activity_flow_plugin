package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;

public class ExpandableHistoryListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<ReportEntity>> reportListDetail;
    private List<String> uidList;

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

    public ExpandableHistoryListAdapter(Context context, List<String> list, HashMap<String, List<ReportEntity>> hashMap) {
        this.context = context;
        this.uidList = list;
        this.reportListDetail = hashMap;
    }

    public Object getChild(int i, int i2) {
        return ((List) this.reportListDetail.get(this.uidList.get(i))).get(i2);
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        ReportEntity reportEntity = (ReportEntity) getChild(i, i2);
        String remoteHost = reportEntity.getRemoteHost();
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.history_list_item, null);
        }
        ((TextView) view.findViewById(C0501R.C0499id.history_item_1)).setText(remoteHost);
        ((TextView) view.findViewById(C0501R.C0499id.history_item_2_type)).setText("Time Stamp: ");
        ((TextView) view.findViewById(C0501R.C0499id.history_item_2_val)).setText(reportEntity.getTimeStamp());
        return view;
    }

    public int getChildrenCount(int i) {
        return ((List) this.reportListDetail.get(this.uidList.get(i))).size();
    }

    public Object getGroup(int i) {
        return this.reportListDetail.get(Integer.valueOf(i));
    }

    public int getGroupCount() {
        return this.reportListDetail.size();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        String appName;
        View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.history_list_group, null);
        PackageManager packageManager = this.context.getPackageManager();
        try {
            appName = ((ReportEntity) ((List) this.reportListDetail.get(this.uidList.get(i))).get(0)).getAppName();
        } catch (IndexOutOfBoundsException unused) {
            if (Collector.getKnownUIDs().containsKey(this.uidList.get(i))) {
                appName = (String) Collector.getKnownUIDs().get(this.uidList.get(i));
            } else {
                appName = packageManager.getNameForUid(new Integer((String) this.uidList.get(i)).intValue());
            }
        }
        TextView textView = (TextView) inflate.findViewById(C0501R.C0499id.historyGroupTitle);
        TextView textView2 = (TextView) inflate.findViewById(C0501R.C0499id.historyGroupSubtitle);
        try {
            if (getChildrenCount(i) != 0) {
                textView.setText((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(appName, 128)));
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(appName, 128)));
                stringBuilder.append(" ");
                stringBuilder.append(this.context.getString(C0501R.string.history_no_data_collected));
                textView.setText(stringBuilder.toString());
            }
        } catch (NameNotFoundException unused2) {
            textView.setText(appName);
        }
        textView2.setText(appName);
        try {
            ((ImageView) inflate.findViewById(C0501R.C0499id.historyGroupIcon)).setImageDrawable(packageManager.getApplicationIcon(appName));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return inflate;
    }
}
