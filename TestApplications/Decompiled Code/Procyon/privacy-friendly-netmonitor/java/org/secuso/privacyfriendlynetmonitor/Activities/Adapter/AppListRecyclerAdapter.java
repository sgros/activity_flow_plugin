// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.view.View;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;
import android.content.Context;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class AppListRecyclerAdapter extends Adapter<ViewHolder>
{
    private List<String> app_list_name;
    private Context context;
    private SharedPreferences$Editor editor;
    private SharedPreferences selectedAppsPreferences;
    
    public AppListRecyclerAdapter(final List<String> app_list_name, final Context context) {
        this.app_list_name = app_list_name;
        this.context = context;
        this.selectedAppsPreferences = context.getSharedPreferences("SELECTEDAPPS", 0);
        this.editor = this.selectedAppsPreferences.edit();
    }
    
    @Override
    public int getItemCount() {
        return this.app_list_name.size();
    }
    
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final String appFullName = this.app_list_name.get(n);
        viewHolder.appFullName = appFullName;
        if (Collector.getAppsToIncludeInScan().contains(viewHolder.appFullName)) {
            viewHolder.appSwitch.setChecked(true);
        }
        else {
            viewHolder.appSwitch.setChecked(false);
        }
        final PackageManager packageManager = this.context.getPackageManager();
        while (true) {
            try {
                viewHolder.appGroupTitle.setText((CharSequence)packageManager.getApplicationLabel(packageManager.getApplicationInfo(appFullName, 128)));
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MMM yyyy, HH:mm");
                final Date date = new Date(packageManager.getPackageInfo(appFullName, 0).firstInstallTime);
                final TextView appInstallDate = viewHolder.appInstallDate;
                final StringBuilder sb = new StringBuilder();
                sb.append("Installed:  ");
                sb.append(simpleDateFormat.format(date));
                appInstallDate.setText((CharSequence)sb.toString());
                viewHolder.appIcon.setImageDrawable(packageManager.getApplicationIcon(appFullName));
                viewHolder.appSwitch.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                        final String appFullName = viewHolder.appFullName;
                        if (b) {
                            if (!Collector.getAppsToIncludeInScan().contains(appFullName)) {
                                Collector.addAppToIncludeInScan(appFullName);
                                AppListRecyclerAdapter.this.editor.putString(appFullName, appFullName);
                                AppListRecyclerAdapter.this.editor.commit();
                                viewHolder.appSwitch.setChecked(true);
                            }
                        }
                        else if (Collector.getAppsToIncludeInScan().contains(appFullName)) {
                            Collector.deleteAppFromIncludeInScan(appFullName);
                            AppListRecyclerAdapter.this.editor.remove(appFullName);
                            AppListRecyclerAdapter.this.editor.commit();
                            viewHolder.appSwitch.setChecked(false);
                        }
                    }
                });
            }
            catch (PackageManager$NameNotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131427370, viewGroup, false));
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public String appFullName;
        public TextView appGroupTitle;
        public ImageView appIcon;
        public TextView appInstallDate;
        public SwitchCompat appSwitch;
        
        public ViewHolder(final View view) {
            super(view);
            this.appGroupTitle = (TextView)view.findViewById(2131296295);
            this.appInstallDate = (TextView)view.findViewById(2131296296);
            this.appIcon = (ImageView)view.findViewById(2131296294);
            this.appSwitch = (SwitchCompat)view.findViewById(2131296502);
            this.appFullName = "";
        }
    }
}
