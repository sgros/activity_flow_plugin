// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.graphics.Typeface;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import java.util.List;
import java.util.HashMap;
import android.content.Context;
import android.widget.BaseExpandableListAdapter;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private HashMap<String, List<String>> expandableListDetail;
    private List<String> expandableListTitle;
    
    public ExpandableListAdapter(final Context context, final List<String> expandableListTitle, final HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }
    
    public Object getChild(final int n, final int n2) {
        return this.expandableListDetail.get(this.expandableListTitle.get(n)).get(n2);
    }
    
    public long getChildId(final int n, final int n2) {
        return n2;
    }
    
    public View getChildView(final int n, final int n2, final boolean b, final View view, final ViewGroup viewGroup) {
        final String text = (String)this.getChild(n, n2);
        View inflate = view;
        if (view == null) {
            inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427392, (ViewGroup)null);
        }
        ((TextView)inflate.findViewById(2131296344)).setText((CharSequence)text);
        return inflate;
    }
    
    public int getChildrenCount(final int n) {
        return this.expandableListDetail.get(this.expandableListTitle.get(n)).size();
    }
    
    public Object getGroup(final int n) {
        return this.expandableListTitle.get(n);
    }
    
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }
    
    public long getGroupId(final int n) {
        return n;
    }
    
    public View getGroupView(final int n, final boolean b, final View view, final ViewGroup viewGroup) {
        final String text = (String)this.getGroup(n);
        View inflate = view;
        if (view == null) {
            inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427391, (ViewGroup)null);
        }
        final TextView textView = (TextView)inflate.findViewById(2131296387);
        textView.setTypeface((Typeface)null, 1);
        textView.setText((CharSequence)text);
        return inflate;
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    public boolean isChildSelectable(final int n, final int n2) {
        return true;
    }
}
