package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class HistoryDetailActivity extends BaseActivity {

    public class DetailAdapter extends ArrayAdapter<String[]> {
        DetailAdapter(Context context, int i, List<String[]> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            HistoryDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
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
        ArrayList stringArrayListExtra = getIntent().getStringArrayListExtra("Details");
        ListView listView = (ListView) findViewById(C0501R.C0499id.report_detail_list_view);
        listView.setAdapter(new DetailAdapter(this, C0501R.layout.report_detail_item, prepareData(stringArrayListExtra)));
        View inflate = getLayoutInflater().inflate(C0501R.layout.report_list_group_header, null);
        PackageManager packageManager = getPackageManager();
        String str = (String) stringArrayListExtra.get(0);
        TextView textView = (TextView) inflate.findViewById(C0501R.C0499id.reportGroupTitle_header);
        TextView textView2 = (TextView) inflate.findViewById(C0501R.C0499id.reportGroupSubtitle_header);
        try {
            ((ImageView) inflate.findViewById(C0501R.C0499id.reportGroupIcon_header)).setImageDrawable(packageManager.getApplicationIcon(str));
            textView.setText((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)));
        } catch (NameNotFoundException unused) {
        }
        textView2.setText(str);
        listView.addHeaderView(inflate);
    }

    public List<String[]> prepareData(List<String> list) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new String[]{"UID", (String) list.get(1)});
        arrayList.add(new String[]{"APP VERSION", (String) list.get(2)});
        arrayList.add(new String[]{"INSTALLED ON", (String) list.get(3)});
        arrayList.add(new String[]{"", ""});
        arrayList.add(new String[]{"REMOTE ADDRESS", (String) list.get(4)});
        arrayList.add(new String[]{"REMOTE HEX", (String) list.get(5)});
        arrayList.add(new String[]{"REMOTE HOST", (String) list.get(6)});
        arrayList.add(new String[]{"LOCAL ADDRESS", (String) list.get(7)});
        arrayList.add(new String[]{"LOCAL HEX", (String) list.get(8)});
        arrayList.add(new String[]{"", ""});
        arrayList.add(new String[]{"SERVICE PORT", (String) list.get(9)});
        arrayList.add(new String[]{"PAYLOAD PROTOCOL", (String) list.get(10)});
        arrayList.add(new String[]{"TRANSPORT PROTOCOL", (String) list.get(11)});
        arrayList.add(new String[]{"LOCAL PORT", (String) list.get(12)});
        arrayList.add(new String[]{"", ""});
        arrayList.add(new String[]{"TIMESTAMP", (String) list.get(13)});
        arrayList.add(new String[]{"CONNECTION INFO", (String) list.get(14)});
        return arrayList;
    }
}
