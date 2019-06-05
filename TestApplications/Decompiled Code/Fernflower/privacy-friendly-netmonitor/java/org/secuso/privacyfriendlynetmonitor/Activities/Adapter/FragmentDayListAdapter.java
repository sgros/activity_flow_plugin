package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.PrintStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.HistoryDetailActivity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;

public class FragmentDayListAdapter extends RecyclerView.Adapter {
   Context context;
   List reportEntities;

   public FragmentDayListAdapter(List var1, Context var2) {
      this.reportEntities = var1;
      this.context = var2;
   }

   private List prepareData(ReportEntity var1) {
      PackageManager var2 = this.context.getPackageManager();
      ArrayList var3 = new ArrayList();
      String var4 = var1.getAppName();
      var3.add(var4);
      var3.add(var1.getUserID());

      PackageInfo var8;
      try {
         var8 = var2.getPackageInfo(var4, 0);
      } catch (NameNotFoundException var6) {
         PrintStream var7 = System.out;
         StringBuilder var5 = new StringBuilder();
         var5.append("Could not find package info for ");
         var5.append(var4);
         var5.append(".");
         var7.println(var5.toString());
         var8 = null;
      }

      var3.add(var8.versionName);
      var3.add((new Date(var8.firstInstallTime)).toString());
      var3.add(var1.getRemoteAddress());
      var3.add(var1.getRemoteHex());
      var3.add(var1.getRemoteHost());
      var3.add(var1.getLocalAddress());
      var3.add(var1.getLocalHex());
      var3.add(var1.getServicePort());
      var3.add(var1.getPayloadProtocol());
      var3.add(var1.getTransportProtocol());
      var3.add(var1.getLocalPort());
      var3.add(var1.getTimeStamp());
      var3.add(var1.getConnectionInfo());
      return var3;
   }

   public int getItemCount() {
      return this.reportEntities.size();
   }

   public void onBindViewHolder(FragmentDayListAdapter.ViewHolder var1, int var2) {
      final ReportEntity var3 = (ReportEntity)this.reportEntities.get(var2);
      TextView var4 = (TextView)var1.relativeLayout.findViewById(2131296352);
      TextView var5 = (TextView)var1.relativeLayout.findViewById(2131296356);
      TextView var6 = (TextView)var1.relativeLayout.findViewById(2131296354);
      var4.setText(var3.getRemoteAddress());
      var5.setText(var3.getTimeStamp());
      var6.setText(var3.getConnectionInfo());
      var1.relativeLayout.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            List var2 = FragmentDayListAdapter.this.prepareData(var3);
            Intent var3x = new Intent(FragmentDayListAdapter.this.context, HistoryDetailActivity.class);
            var3x.putExtra("Details", (ArrayList)var2);
            FragmentDayListAdapter.this.context.startActivity(var3x);
         }
      });
   }

   public FragmentDayListAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new FragmentDayListAdapter.ViewHolder((RelativeLayout)LayoutInflater.from(var1.getContext()).inflate(2131427390, var1, false));
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      public RelativeLayout relativeLayout;

      public ViewHolder(RelativeLayout var1) {
         super(var1);
         this.relativeLayout = var1;
      }
   }
}
