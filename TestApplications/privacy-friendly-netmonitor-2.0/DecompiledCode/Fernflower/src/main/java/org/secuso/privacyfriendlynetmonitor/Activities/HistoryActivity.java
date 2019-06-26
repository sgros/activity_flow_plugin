package org.secuso.privacyfriendlynetmonitor.Activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import java.io.PrintStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableHistoryListAdapter;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class HistoryActivity extends BaseActivity {
   private Editor editor;
   private ExpandableListView expListView;
   private ExpandableHistoryListAdapter historyReportAdapter;
   private HashMap historyReportMap;
   private List keys;
   private ProgressBar progressBar;
   private ReportEntityDao reportEntityDao;
   private SharedPreferences selectedAppsPreferences;

   private void activateHistoryView() {
      this.expListView = (ExpandableListView)this.findViewById(2131296388);
      final HashMap var1 = this.provideHistoryReports();
      TextView var2 = (TextView)this.findViewById(2131296415);
      if (var1.isEmpty()) {
         if (var2.getVisibility() == 4) {
            var2.setVisibility(0);
         }
      } else if (var2.getVisibility() == 0) {
         var2.setVisibility(4);
      }

      this.historyReportAdapter = new ExpandableHistoryListAdapter(this, new ArrayList(var1.keySet()), var1);
      this.expListView.setAdapter(this.historyReportAdapter);
      this.expListView.setOnChildClickListener(new OnChildClickListener() {
         public boolean onChildClick(ExpandableListView var1x, View var2, int var3, int var4, long var5) {
            ReportEntity var7 = (ReportEntity)((List)var1.get(HistoryActivity.this.keys.get(var3))).get(var4);
            List var9 = HistoryActivity.this.prepareData(var7);
            Intent var8 = new Intent(HistoryActivity.this.getBaseContext(), HistoryDetailActivity.class);
            var8.putExtra("Details", (ArrayList)var9);
            HistoryActivity.this.startActivity(var8);
            return false;
         }
      });
      this.expListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         public boolean onItemLongClick(AdapterView var1, View var2, int var3, long var4) {
            String var7 = ((TextView)var2.findViewById(2131296362)).getText().toString();
            String var6 = ((TextView)var2.findViewById(2131296361)).getText().toString();
            Intent var8 = new Intent(HistoryActivity.this, AppConnections_Chart.class);
            var8.putExtra("AppName", var7);
            var8.putExtra("AppSubName", var6);
            HistoryActivity.this.startActivity(var8);
            return false;
         }
      });
   }

   private void deleteConfirmation() {
      Builder var1 = new Builder(this);
      var1.setTitle(2131623986);
      var1.setPositiveButton(2131624082, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            HistoryActivity.this.reportEntityDao.deleteAll();
            HistoryActivity.this.activateHistoryView();
            Toast.makeText(HistoryActivity.this.getApplicationContext(), "All reports have been deleted.", 0).show();
         }
      });
      var1.setNegativeButton(2131624032, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            Toast.makeText(HistoryActivity.this.getApplicationContext(), "Deletion canceled.", 0).show();
         }
      });
      System.out.println("Building complete");
      var1.create().show();
   }

   private List prepareData(ReportEntity var1) {
      PackageManager var2 = this.getPackageManager();
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

   private HashMap provideHistoryReports() {
      this.historyReportMap = new HashMap();
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.reportEntityDao.loadAll().iterator();

      ArrayList var6;
      while(var3.hasNext()) {
         ReportEntity var4 = (ReportEntity)var3.next();
         String var5 = var4.getUserID();
         if (!var2.contains(var5)) {
            var2.add(var5);
            var6 = new ArrayList();
            var6.add(var4);
            if (!var1.contains(var4.getAppName())) {
               var1.add(var4.getAppName());
            }

            this.historyReportMap.put(var5, var6);
         } else {
            ((List)this.historyReportMap.get(var5)).add(var4);
         }
      }

      List var12 = Collector.getAppsToIncludeInScan();
      if (!var12.isEmpty()) {
         ArrayList var13 = new ArrayList(new LinkedHashSet(var12));
         var13.removeAll(var1);
         if (!var13.isEmpty()) {
            Iterator var9 = var13.iterator();

            while(var9.hasNext()) {
               String var15 = (String)var9.next();

               try {
                  int var7 = this.getPackageManager().getApplicationInfo(var15, 0).uid;
                  if (!Collector.getKnownUIDs().containsKey(var7)) {
                     new String();
                     Collector.addKnownUIDs(String.valueOf(var7), var15);
                  }

                  HashMap var11 = this.historyReportMap;
                  new String();
                  var6 = new ArrayList();
                  var11.put(String.valueOf(var7), var6);
               } catch (NameNotFoundException var8) {
               }
            }

            var13.clear();
         }
      }

      this.keys = new ArrayList(this.historyReportMap.keySet());
      Iterator var14 = this.keys.iterator();

      while(var14.hasNext()) {
         String var10 = (String)var14.next();
         Collections.reverse((List)this.historyReportMap.get(var10));
      }

      return this.historyReportMap;
   }

   protected int getNavigationDrawerID() {
      return 2131296408;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427358);
      this.setSupportActionBar((Toolbar)this.findViewById(2131296523));
      this.progressBar = (ProgressBar)this.findViewById(2131296478);
      this.progressBar.setVisibility(8);
      this.reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
      this.selectedAppsPreferences = this.getSharedPreferences("SELECTEDAPPS", 0);
      this.editor = this.selectedAppsPreferences.edit();
      ((FloatingActionButton)this.findViewById(2131296346)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            HistoryActivity.this.progressBar.setVisibility(0);
            HistoryActivity.this.startActivity(new Intent(HistoryActivity.this, SelectHistoryAppsActivity.class));
         }
      });
      ((Button)this.findViewById(2131296329)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            HistoryActivity.this.deleteConfirmation();
         }
      });
      this.activateHistoryView();
   }

   public void onPostResume() {
      super.onPostResume();
      this.progressBar.setVisibility(8);
   }

   public void onResume() {
      super.onResume();
      this.activateHistoryView();
   }
}
