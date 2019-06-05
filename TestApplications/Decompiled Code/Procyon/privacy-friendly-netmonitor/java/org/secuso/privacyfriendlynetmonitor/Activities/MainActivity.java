// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.MenuItem;
import android.view.Menu;
import java.util.Iterator;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import android.app.Activity;
import android.os.Bundle;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.view.View$OnClickListener;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView$OnChildClickListener;
import android.widget.ExpandableListAdapter;
import android.content.Context;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableReportAdapter;
import java.util.Collection;
import java.util.ArrayList;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;
import java.util.List;
import java.util.HashMap;
import android.widget.ExpandableListView;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import android.support.v4.widget.SwipeRefreshLayout;

public class MainActivity extends BaseActivity implements OnRefreshListener
{
    private static ReportEntityDao reportEntityDao;
    private ExpandableListView expListView;
    private HashMap<Integer, List<Report>> reportMap;
    private SwipeRefreshLayout swipeRefreshLayout;
    
    private void activateMainView() {
        this.setContentView(2131427359);
        super.setToolbar();
        final FloatingActionButton floatingActionButton = this.findViewById(2131296391);
        this.findViewById(2131296396).setText(2131624027);
        this.setButtonListener();
        this.getNavigationDrawerID();
    }
    
    private void activateReportView() {
        this.setContentView(2131427362);
        super.setToolbar();
        this.getNavigationDrawerID();
        this.swipeRefreshLayout = this.findViewById(2131296501);
        this.reportMap = Collector.provideSimpleReports(MainActivity.reportEntityDao);
        (this.expListView = this.findViewById(2131296385)).setAdapter((ExpandableListAdapter)new ExpandableReportAdapter((Context)this, new ArrayList<Integer>(this.reportMap.keySet()), this.reportMap));
        this.swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.swipeRefreshLayout.post((Runnable)new Runnable() {
            @Override
            public void run() {
                MainActivity.this.swipeRefreshLayout.setRefreshing(true);
                MainActivity.this.refreshAdapter();
            }
        });
        this.expListView.setOnChildClickListener((ExpandableListView$OnChildClickListener)new ExpandableListView$OnChildClickListener() {
            public boolean onChildClick(final ExpandableListView expandableListView, final View view, final int n, final int n2, final long n3) {
                MainActivity.this.expListView = MainActivity.this.findViewById(2131296385);
                final Report report = (Report)((ExpandableReportAdapter)MainActivity.this.expListView.getExpandableListAdapter()).getChild(n, n2);
                if (MainActivity.this.mSharedPreferences.getBoolean("IS_DETAIL_MODE", false)) {
                    view.animate().setDuration(500L).alpha(0.5f).withEndAction((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Collector.provideDetail(report.uid, report.remoteAddHex);
                            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), (Class)ReportDetailActivity.class));
                        }
                    });
                    return true;
                }
                if (MainActivity.this.mSharedPreferences.getBoolean("IS_CERTVAL", false) && Collector.hasHostName(report.remoteAdd.getHostAddress()) && Collector.hasGrade(Collector.getDnsHostName(report.remoteAdd.getHostAddress()))) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("https://www.ssllabs.com/ssltest/analyze.html?d=");
                    sb.append(Collector.getCertHost(Collector.getDnsHostName(report.remoteAdd.getHostAddress())));
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    return false;
                }
                try {
                    view.animate().setDuration(500L).alpha(0.5f).withEndAction((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Collector.provideDetail(report.uid, report.remoteAddHex);
                            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), (Class)ReportDetailActivity.class));
                        }
                    });
                    return false;
                }
                catch (Exception ex) {
                    return false;
                }
            }
        });
        this.findViewById(2131296436).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Collector.saveReports(MainActivity.reportEntityDao);
                MainActivity.this.startStopTrigger();
            }
        });
    }
    
    private void setButtonListener() {
        this.findViewById(2131296391).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                MainActivity.this.findViewById(2131296392).setVisibility(0);
                MainActivity.this.startStopTrigger();
            }
        });
    }
    
    private void setSwipeInfo(final boolean b) {
        final ImageView imageView = this.findViewById(2131296448);
        final TextView textView = this.findViewById(2131296449);
        if (b) {
            imageView.setVisibility(8);
            textView.setVisibility(8);
        }
        else {
            imageView.setVisibility(0);
            textView.setVisibility(0);
        }
    }
    
    private void startStopTrigger() {
        if (!RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            RunStore.getServiceHandler().startPassiveService();
            final Intent intent = new Intent(RunStore.getContext(), (Class)MainActivity.class);
            intent.setFlags(67108864);
            this.startActivity(intent);
        }
        else {
            RunStore.getServiceHandler().stopPassiveService();
            final Intent intent2 = new Intent(RunStore.getContext(), (Class)MainActivity.class);
            intent2.setFlags(67108864);
            this.startActivity(intent2);
        }
    }
    
    @Override
    protected int getNavigationDrawerID() {
        return 2131296409;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        RunStore.setContext(this);
        RunStore.setAppContext(this.getApplicationContext());
        MainActivity.reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
        for (final String s : this.getSharedPreferences("SELECTEDAPPS", 0).getAll().values()) {
            if (!Collector.getAppsToIncludeInScan().contains(s)) {
                Collector.addAppToIncludeInScan(s);
            }
        }
        Collector.addAppToExcludeFromScan("app.android.unknown");
        Collector.addAppToExcludeFromScan("app.unknown");
        Collector.addAppToExcludeFromScan("unknown");
        if (!RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            this.activateMainView();
        }
        else {
            this.activateReportView();
        }
        this.overridePendingTransition(0, 0);
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    
    public void onDestroy() {
        super.onDestroy();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 2131296280) {
            this.refreshAdapter();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.clear();
        if (RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            this.getMenuInflater().inflate(2131492866, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public void onRefresh() {
        this.refreshAdapter();
    }
    
    public void refreshAdapter() {
        final SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        boolean swipeInfo = true;
        swipeRefreshLayout.setRefreshing(true);
        this.reportMap = Collector.provideSimpleReports(MainActivity.reportEntityDao);
        final ExpandableReportAdapter adapter = new ExpandableReportAdapter((Context)this, new ArrayList<Integer>(this.reportMap.keySet()), this.reportMap);
        this.expListView.setAdapter((ExpandableListAdapter)adapter);
        if (adapter.getGroupCount() <= 0) {
            swipeInfo = false;
        }
        this.setSwipeInfo(swipeInfo);
        this.swipeRefreshLayout.setRefreshing(false);
    }
}
