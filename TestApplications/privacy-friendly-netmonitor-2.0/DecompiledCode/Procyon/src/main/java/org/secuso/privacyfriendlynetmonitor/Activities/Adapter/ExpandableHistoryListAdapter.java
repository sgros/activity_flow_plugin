// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.content.pm.PackageManager$NameNotFoundException;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.List;
import java.util.HashMap;
import android.content.Context;
import android.widget.BaseExpandableListAdapter;

public class ExpandableHistoryListAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private HashMap<String, List<ReportEntity>> reportListDetail;
    private List<String> uidList;
    
    public ExpandableHistoryListAdapter(final Context context, final List<String> uidList, final HashMap<String, List<ReportEntity>> reportListDetail) {
        this.context = context;
        this.uidList = uidList;
        this.reportListDetail = reportListDetail;
    }
    
    public Object getChild(final int n, final int n2) {
        return this.reportListDetail.get(this.uidList.get(n)).get(n2);
    }
    
    public long getChildId(final int n, final int n2) {
        return n2;
    }
    
    public View getChildView(final int n, final int n2, final boolean b, final View view, final ViewGroup viewGroup) {
        final ReportEntity reportEntity = (ReportEntity)this.getChild(n, n2);
        final String remoteHost = reportEntity.getRemoteHost();
        View inflate = view;
        if (view == null) {
            inflate = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427394, (ViewGroup)null);
        }
        ((TextView)inflate.findViewById(2131296363)).setText((CharSequence)remoteHost);
        ((TextView)inflate.findViewById(2131296364)).setText((CharSequence)"Time Stamp: ");
        ((TextView)inflate.findViewById(2131296365)).setText((CharSequence)reportEntity.getTimeStamp());
        return inflate;
    }
    
    public int getChildrenCount(final int n) {
        return this.reportListDetail.get(this.uidList.get(n)).size();
    }
    
    public Object getGroup(final int i) {
        return this.reportListDetail.get(i);
    }
    
    public int getGroupCount() {
        return this.reportListDetail.size();
    }
    
    public long getGroupId(final int n) {
        return n;
    }
    
    public View getGroupView(final int n, final boolean b, View view, ViewGroup inflate) {
        inflate = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427393, (ViewGroup)null);
        final PackageManager packageManager = this.context.getPackageManager();
        try {
            view = (View)this.reportListDetail.get(this.uidList.get(n)).get(0).getAppName();
        }
        catch (IndexOutOfBoundsException ex2) {
            if (Collector.getKnownUIDs().containsKey(this.uidList.get(n))) {
                view = (View)Collector.getKnownUIDs().get(this.uidList.get(n));
            }
            else {
                view = (View)packageManager.getNameForUid((int)new Integer(this.uidList.get(n)));
            }
        }
        final TextView textView = (TextView)((View)inflate).findViewById(2131296362);
        final TextView textView2 = (TextView)((View)inflate).findViewById(2131296361);
        try {
            if (this.getChildrenCount(n) != 0) {
                textView.setText((CharSequence)packageManager.getApplicationLabel(packageManager.getApplicationInfo((String)view, 128)));
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append((String)packageManager.getApplicationLabel(packageManager.getApplicationInfo((String)view, 128)));
                sb.append(" ");
                sb.append(this.context.getString(2131624022));
                textView.setText((CharSequence)sb.toString());
            }
        }
        catch (PackageManager$NameNotFoundException ex3) {
            textView.setText((CharSequence)view);
        }
        textView2.setText((CharSequence)view);
        final ImageView imageView = (ImageView)((View)inflate).findViewById(2131296360);
        try {
            imageView.setImageDrawable(packageManager.getApplicationIcon((String)view));
        }
        catch (PackageManager$NameNotFoundException ex) {
            ex.printStackTrace();
        }
        return (View)inflate;
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    public boolean isChildSelectable(final int n, final int n2) {
        return true;
    }
}
