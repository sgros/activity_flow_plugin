package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<String>> expandableListDetail;
    private List<String> expandableListTitle;

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

    public ExpandableListAdapter(Context context, List<String> list, HashMap<String, List<String>> hashMap) {
        this.context = context;
        this.expandableListTitle = list;
        this.expandableListDetail = hashMap;
    }

    public Object getChild(int i, int i2) {
        return ((List) this.expandableListDetail.get(this.expandableListTitle.get(i))).get(i2);
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        String str = (String) getChild(i, i2);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.help_list_item, null);
        }
        ((TextView) view.findViewById(C0501R.C0499id.expandedListItem)).setText(str);
        return view;
    }

    public int getChildrenCount(int i) {
        return ((List) this.expandableListDetail.get(this.expandableListTitle.get(i))).size();
    }

    public Object getGroup(int i) {
        return this.expandableListTitle.get(i);
    }

    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        String str = (String) getGroup(i);
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0501R.layout.help_list_group, null);
        }
        TextView textView = (TextView) view.findViewById(C0501R.C0499id.listTitle);
        textView.setTypeface(null, 1);
        textView.setText(str);
        return view;
    }
}
