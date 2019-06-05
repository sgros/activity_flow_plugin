// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.pm.PackageManager;
import android.view.View;
import java.util.ArrayList;
import android.content.pm.PackageManager$NameNotFoundException;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Context;
import java.util.List;
import android.app.Activity;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Bundle;

public class HistoryDetailActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427365);
        RunStore.setContext(this);
        final ArrayList stringArrayListExtra = this.getIntent().getStringArrayListExtra("Details");
        final DetailAdapter adapter = new DetailAdapter((Context)this, 2131427411, this.prepareData(stringArrayListExtra));
        bundle = (Bundle)this.findViewById(2131296446);
        ((ListView)bundle).setAdapter((ListAdapter)adapter);
        final View inflate = this.getLayoutInflater().inflate(2131427413, (ViewGroup)null);
        final PackageManager packageManager = this.getPackageManager();
        final String text = stringArrayListExtra.get(0);
        final ImageView imageView = (ImageView)inflate.findViewById(2131296438);
        final TextView textView = (TextView)inflate.findViewById(2131296442);
        final TextView textView2 = (TextView)inflate.findViewById(2131296440);
        while (true) {
            try {
                imageView.setImageDrawable(packageManager.getApplicationIcon(text));
                textView.setText((CharSequence)packageManager.getApplicationLabel(packageManager.getApplicationInfo(text, 128)));
                textView2.setText((CharSequence)text);
                ((ListView)bundle).addHeaderView(inflate);
            }
            catch (PackageManager$NameNotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    public List<String[]> prepareData(final List<String> list) {
        final ArrayList<String[]> list2 = new ArrayList<String[]>();
        list2.add(new String[] { "UID", list.get(1) });
        list2.add(new String[] { "APP VERSION", list.get(2) });
        list2.add(new String[] { "INSTALLED ON", list.get(3) });
        list2.add(new String[] { "", "" });
        list2.add(new String[] { "REMOTE ADDRESS", list.get(4) });
        list2.add(new String[] { "REMOTE HEX", list.get(5) });
        list2.add(new String[] { "REMOTE HOST", list.get(6) });
        list2.add(new String[] { "LOCAL ADDRESS", list.get(7) });
        list2.add(new String[] { "LOCAL HEX", list.get(8) });
        list2.add(new String[] { "", "" });
        list2.add(new String[] { "SERVICE PORT", list.get(9) });
        list2.add(new String[] { "PAYLOAD PROTOCOL", list.get(10) });
        list2.add(new String[] { "TRANSPORT PROTOCOL", list.get(11) });
        list2.add(new String[] { "LOCAL PORT", list.get(12) });
        list2.add(new String[] { "", "" });
        list2.add(new String[] { "TIMESTAMP", list.get(13) });
        list2.add(new String[] { "CONNECTION INFO", list.get(14) });
        return list2;
    }
    
    public class DetailAdapter extends ArrayAdapter<String[]>
    {
        DetailAdapter(final Context context, final int n, final List<String[]> list) {
            super(context, n, (List)list);
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            HistoryDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
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
