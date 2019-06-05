package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableReportAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
   private static ReportEntityDao reportEntityDao;
   private ExpandableListView expListView;
   private HashMap reportMap;
   private SwipeRefreshLayout swipeRefreshLayout;

   private void activateMainView() {
      this.setContentView(2131427359);
      super.setToolbar();
      FloatingActionButton var1 = (FloatingActionButton)this.findViewById(2131296391);
      ((TextView)this.findViewById(2131296396)).setText(2131624027);
      this.setButtonListener();
      this.getNavigationDrawerID();
   }

   private void activateReportView() {
      this.setContentView(2131427362);
      super.setToolbar();
      this.getNavigationDrawerID();
      this.swipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(2131296501);
      this.reportMap = Collector.provideSimpleReports(reportEntityDao);
      this.expListView = (ExpandableListView)this.findViewById(2131296385);
      ExpandableReportAdapter var1 = new ExpandableReportAdapter(this, new ArrayList(this.reportMap.keySet()), this.reportMap);
      this.expListView.setAdapter(var1);
      this.swipeRefreshLayout.setOnRefreshListener(this);
      this.swipeRefreshLayout.post(new Runnable() {
         public void run() {
            MainActivity.this.swipeRefreshLayout.setRefreshing(true);
            MainActivity.this.refreshAdapter();
         }
      });
      this.expListView.setOnChildClickListener(new OnChildClickListener() {
         public boolean onChildClick(ExpandableListView var1, View var2, int var3, int var4, long var5) {
            MainActivity.this.expListView = (ExpandableListView)MainActivity.this.findViewById(2131296385);
            final Report var9 = (Report)((ExpandableReportAdapter)MainActivity.this.expListView.getExpandableListAdapter()).getChild(var3, var4);
            if (MainActivity.this.mSharedPreferences.getBoolean("IS_DETAIL_MODE", false)) {
               var2.animate().setDuration(500L).alpha(0.5F).withEndAction(new Runnable() {
                  public void run() {
                     Collector.provideDetail(var9.uid, var9.remoteAddHex);
                     Intent var1 = new Intent(MainActivity.this.getApplicationContext(), ReportDetailActivity.class);
                     MainActivity.this.startActivity(var1);
                  }
               });
               return true;
            } else if (MainActivity.this.mSharedPreferences.getBoolean("IS_CERTVAL", false) && Collector.hasHostName(var9.remoteAdd.getHostAddress()) && Collector.hasGrade(Collector.getDnsHostName(var9.remoteAdd.getHostAddress()))) {
               StringBuilder var12 = new StringBuilder();
               var12.append("https://www.ssllabs.com/ssltest/analyze.html?d=");
               var12.append(Collector.getCertHost(Collector.getDnsHostName(var9.remoteAdd.getHostAddress())));
               Intent var10 = new Intent("android.intent.action.VIEW", Uri.parse(var12.toString()));
               MainActivity.this.startActivity(var10);
               return false;
            } else {
               try {
                  ViewPropertyAnimator var11 = var2.animate().setDuration(500L).alpha(0.5F);
                  Runnable var7 = new Runnable() {
                     public void run() {
                        Collector.provideDetail(var9.uid, var9.remoteAddHex);
                        Intent var1 = new Intent(MainActivity.this.getApplicationContext(), ReportDetailActivity.class);
                        MainActivity.this.startActivity(var1);
                     }
                  };
                  var11.withEndAction(var7);
                  return false;
               } catch (Exception var8) {
                  return false;
               }
            }
         }
      });
      ((FloatingActionButton)this.findViewById(2131296436)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Collector.saveReports(MainActivity.reportEntityDao);
            MainActivity.this.startStopTrigger();
         }
      });
   }

   private void setButtonListener() {
      ((FloatingActionButton)this.findViewById(2131296391)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ((ProgressBar)MainActivity.this.findViewById(2131296392)).setVisibility(0);
            MainActivity.this.startStopTrigger();
         }
      });
   }

   private void setSwipeInfo(boolean var1) {
      ImageView var2 = (ImageView)this.findViewById(2131296448);
      TextView var3 = (TextView)this.findViewById(2131296449);
      if (var1) {
         var2.setVisibility(8);
         var3.setVisibility(8);
      } else {
         var2.setVisibility(0);
         var3.setVisibility(0);
      }

   }

   private void startStopTrigger() {
      Intent var1;
      if (!RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
         RunStore.getServiceHandler().startPassiveService();
         var1 = new Intent(RunStore.getContext(), MainActivity.class);
         var1.setFlags(67108864);
         this.startActivity(var1);
      } else {
         RunStore.getServiceHandler().stopPassiveService();
         var1 = new Intent(RunStore.getContext(), MainActivity.class);
         var1.setFlags(67108864);
         this.startActivity(var1);
      }

   }

   protected int getNavigationDrawerID() {
      return 2131296409;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      RunStore.setContext(this);
      RunStore.setAppContext(this.getApplicationContext());
      reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
      Iterator var3 = this.getSharedPreferences("SELECTEDAPPS", 0).getAll().values().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         if (!Collector.getAppsToIncludeInScan().contains(var2)) {
            Collector.addAppToIncludeInScan(var2);
         }
      }

      Collector.addAppToExcludeFromScan("app.android.unknown");
      Collector.addAppToExcludeFromScan("app.unknown");
      Collector.addAppToExcludeFromScan("unknown");
      if (!RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
         this.activateMainView();
      } else {
         this.activateReportView();
      }

      this.overridePendingTransition(0, 0);
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      return super.onCreateOptionsMenu(var1);
   }

   public void onDestroy() {
      super.onDestroy();
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      if (var1.getItemId() == 2131296280) {
         this.refreshAdapter();
      }

      return super.onOptionsItemSelected(var1);
   }

   public boolean onPrepareOptionsMenu(Menu var1) {
      var1.clear();
      if (RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
         this.getMenuInflater().inflate(2131492866, var1);
      }

      return super.onPrepareOptionsMenu(var1);
   }

   public void onRefresh() {
      this.refreshAdapter();
   }

   public void refreshAdapter() {
      SwipeRefreshLayout var1 = this.swipeRefreshLayout;
      boolean var2 = true;
      var1.setRefreshing(true);
      this.reportMap = Collector.provideSimpleReports(reportEntityDao);
      ExpandableReportAdapter var3 = new ExpandableReportAdapter(this, new ArrayList(this.reportMap.keySet()), this.reportMap);
      this.expListView.setAdapter(var3);
      if (var3.getGroupCount() <= 0) {
         var2 = false;
      }

      this.setSwipeInfo(var2);
      this.swipeRefreshLayout.setRefreshing(false);
   }
}
