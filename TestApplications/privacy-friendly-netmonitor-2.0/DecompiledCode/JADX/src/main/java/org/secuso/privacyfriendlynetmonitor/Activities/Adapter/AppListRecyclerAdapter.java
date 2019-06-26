package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.p003v7.widget.RecyclerView.C0359Adapter;
import android.support.p003v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;

public class AppListRecyclerAdapter extends C0359Adapter<ViewHolder> {
    private List<String> app_list_name;
    private Context context;
    private Editor editor = this.selectedAppsPreferences.edit();
    private SharedPreferences selectedAppsPreferences;

    public static class ViewHolder extends android.support.p003v7.widget.RecyclerView.ViewHolder {
        public String appFullName = "";
        public TextView appGroupTitle;
        public ImageView appIcon;
        public TextView appInstallDate;
        public SwitchCompat appSwitch;

        public ViewHolder(View view) {
            super(view);
            this.appGroupTitle = (TextView) view.findViewById(C0501R.C0499id.appGroupTitle);
            this.appInstallDate = (TextView) view.findViewById(C0501R.C0499id.appInstalledOn);
            this.appIcon = (ImageView) view.findViewById(C0501R.C0499id.appGroupIcon);
            this.appSwitch = (SwitchCompat) view.findViewById(C0501R.C0499id.switchAppOnOffHistory);
        }
    }

    public AppListRecyclerAdapter(List<String> list, Context context) {
        this.app_list_name = list;
        this.context = context;
        this.selectedAppsPreferences = context.getSharedPreferences("SELECTEDAPPS", 0);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0501R.layout.app_list_group, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String str = (String) this.app_list_name.get(i);
        viewHolder.appFullName = str;
        if (Collector.getAppsToIncludeInScan().contains(viewHolder.appFullName)) {
            viewHolder.appSwitch.setChecked(true);
        } else {
            viewHolder.appSwitch.setChecked(false);
        }
        PackageManager packageManager = this.context.getPackageManager();
        try {
            viewHolder.appGroupTitle.setText((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MMM yyyy, HH:mm");
            Date date = new Date(packageManager.getPackageInfo(str, 0).firstInstallTime);
            TextView textView = viewHolder.appInstallDate;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Installed:  ");
            stringBuilder.append(simpleDateFormat.format(date));
            textView.setText(stringBuilder.toString());
            viewHolder.appIcon.setImageDrawable(packageManager.getApplicationIcon(str));
        } catch (NameNotFoundException unused) {
        }
        viewHolder.appSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                String str = viewHolder.appFullName;
                if (z) {
                    if (!Collector.getAppsToIncludeInScan().contains(str)) {
                        Collector.addAppToIncludeInScan(str);
                        AppListRecyclerAdapter.this.editor.putString(str, str);
                        AppListRecyclerAdapter.this.editor.commit();
                        viewHolder.appSwitch.setChecked(true);
                    }
                } else if (Collector.getAppsToIncludeInScan().contains(str)) {
                    Collector.deleteAppFromIncludeInScan(str);
                    AppListRecyclerAdapter.this.editor.remove(str);
                    AppListRecyclerAdapter.this.editor.commit();
                    viewHolder.appSwitch.setChecked(false);
                }
            }
        });
    }

    public int getItemCount() {
        return this.app_list_name.size();
    }
}
