package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;

public class ReportDetailActivity extends BaseActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427365);
      RunStore.setContext(this);
      ReportDetailActivity.DetailAdapter var2 = new ReportDetailActivity.DetailAdapter(this, 2131427411, Collector.sDetailReportList);
      ListView var3 = (ListView)this.findViewById(2131296446);
      var3.setAdapter(var2);
      View var5 = this.getLayoutInflater().inflate(2131427413, (ViewGroup)null);
      var3.addHeaderView(var5);
      final Report var4 = Collector.sDetailReport;
      ((ImageView)var5.findViewById(2131296438)).setImageDrawable(Collector.getIcon(var4.uid));
      ((TextView)var5.findViewById(2131296442)).setText(Collector.getLabel(var4.uid));
      ((TextView)var5.findViewById(2131296440)).setText(Collector.getPackage(var4.uid));
      if (this.mSharedPreferences.getBoolean("IS_CERTVAL", false) && Collector.hasHostName(var4.remoteAdd.getHostAddress()) && Collector.hasGrade(Collector.getDnsHostName(var4.remoteAdd.getHostAddress()))) {
         TextView var6 = (TextView)this.findViewById(2131296447);
         var6.setVisibility(0);
         var6.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               StringBuilder var2 = new StringBuilder();
               var2.append("https://www.ssllabs.com/ssltest/analyze.html?d=");
               var2.append(Collector.getCertHost(Collector.getDnsHostName(var4.remoteAdd.getHostAddress())));
               Intent var3 = new Intent("android.intent.action.VIEW", Uri.parse(var2.toString()));
               ReportDetailActivity.this.startActivity(var3);
            }
         });
      }

   }

   public void onDestroy() {
      super.onDestroy();
   }

   class DetailAdapter extends ArrayAdapter {
      DetailAdapter(Context var2, int var3, List var4) {
         super(var2, var3, var4);
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         ReportDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
         View var7 = var2;
         if (var2 == null) {
            var7 = LayoutInflater.from(this.getContext()).inflate(2131427411, (ViewGroup)null);
         }

         String[] var4 = (String[])this.getItem(var1);
         TextView var6 = (TextView)var7.findViewById(2131296444);
         TextView var5 = (TextView)var7.findViewById(2131296445);
         if (var4[0] != null && var4[1] != null) {
            var6.setText(var4[0]);
            var5.setText(var4[1]);
         } else {
            var6.setText("");
            var5.setText("");
         }

         return var7;
      }
   }
}
