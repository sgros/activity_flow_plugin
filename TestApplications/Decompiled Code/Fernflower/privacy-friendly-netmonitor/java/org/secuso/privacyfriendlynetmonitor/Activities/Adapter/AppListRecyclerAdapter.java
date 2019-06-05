package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;

public class AppListRecyclerAdapter extends RecyclerView.Adapter {
   private List app_list_name;
   private Context context;
   private Editor editor;
   private SharedPreferences selectedAppsPreferences;

   public AppListRecyclerAdapter(List var1, Context var2) {
      this.app_list_name = var1;
      this.context = var2;
      this.selectedAppsPreferences = var2.getSharedPreferences("SELECTEDAPPS", 0);
      this.editor = this.selectedAppsPreferences.edit();
   }

   public int getItemCount() {
      return this.app_list_name.size();
   }

   public void onBindViewHolder(final AppListRecyclerAdapter.ViewHolder var1, int var2) {
      String var3 = (String)this.app_list_name.get(var2);
      var1.appFullName = var3;
      if (Collector.getAppsToIncludeInScan().contains(var1.appFullName)) {
         var1.appSwitch.setChecked(true);
      } else {
         var1.appSwitch.setChecked(false);
      }

      PackageManager var4 = this.context.getPackageManager();

      try {
         var1.appGroupTitle.setText((String)var4.getApplicationLabel(var4.getApplicationInfo(var3, 128)));
         SimpleDateFormat var5 = new SimpleDateFormat("dd. MMM yyyy, HH:mm");
         Date var6 = new Date(var4.getPackageInfo(var3, 0).firstInstallTime);
         TextView var7 = var1.appInstallDate;
         StringBuilder var8 = new StringBuilder();
         var8.append("Installed:  ");
         var8.append(var5.format(var6));
         var7.setText(var8.toString());
         var1.appIcon.setImageDrawable(var4.getApplicationIcon(var3));
      } catch (NameNotFoundException var9) {
      }

      var1.appSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1x, boolean var2) {
            String var3 = var1.appFullName;
            if (var2) {
               if (!Collector.getAppsToIncludeInScan().contains(var3)) {
                  Collector.addAppToIncludeInScan(var3);
                  AppListRecyclerAdapter.this.editor.putString(var3, var3);
                  AppListRecyclerAdapter.this.editor.commit();
                  var1.appSwitch.setChecked(true);
               }
            } else if (Collector.getAppsToIncludeInScan().contains(var3)) {
               Collector.deleteAppFromIncludeInScan(var3);
               AppListRecyclerAdapter.this.editor.remove(var3);
               AppListRecyclerAdapter.this.editor.commit();
               var1.appSwitch.setChecked(false);
            }

         }
      });
   }

   public AppListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new AppListRecyclerAdapter.ViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131427370, var1, false));
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      public String appFullName;
      public TextView appGroupTitle;
      public ImageView appIcon;
      public TextView appInstallDate;
      public SwitchCompat appSwitch;

      public ViewHolder(View var1) {
         super(var1);
         this.appGroupTitle = (TextView)var1.findViewById(2131296295);
         this.appInstallDate = (TextView)var1.findViewById(2131296296);
         this.appIcon = (ImageView)var1.findViewById(2131296294);
         this.appSwitch = (SwitchCompat)var1.findViewById(2131296502);
         this.appFullName = "";
      }
   }
}
