package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.p000v4.p002os.EnvironmentCompat;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableReportAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class MainActivity extends BaseActivity implements OnRefreshListener {
    private static ReportEntityDao reportEntityDao;
    private ExpandableListView expListView;
    private HashMap<Integer, List<Report>> reportMap;
    private SwipeRefreshLayout swipeRefreshLayout;

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.MainActivity$1 */
    class C04861 implements OnClickListener {
        C04861() {
        }

        public void onClick(View view) {
            ((ProgressBar) MainActivity.this.findViewById(C0501R.C0499id.mainProgressBar)).setVisibility(0);
            MainActivity.this.startStopTrigger();
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.MainActivity$2 */
    class C04872 implements Runnable {
        C04872() {
        }

        public void run() {
            MainActivity.this.swipeRefreshLayout.setRefreshing(true);
            MainActivity.this.refreshAdapter();
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.MainActivity$3 */
    class C04903 implements OnChildClickListener {
        C04903() {
        }

        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
            MainActivity.this.expListView = (ExpandableListView) MainActivity.this.findViewById(C0501R.C0499id.list);
            final Report report = (Report) ((ExpandableReportAdapter) MainActivity.this.expListView.getExpandableListAdapter()).getChild(i, i2);
            if (MainActivity.this.mSharedPreferences.getBoolean(Const.IS_DETAIL_MODE, false)) {
                view.animate().setDuration(500).alpha(0.5f).withEndAction(new Runnable() {
                    public void run() {
                        Collector.provideDetail(report.uid, report.remoteAddHex);
                        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ReportDetailActivity.class));
                    }
                });
                return true;
            } else if (MainActivity.this.mSharedPreferences.getBoolean(Const.IS_CERTVAL, false) && Collector.hasHostName(report.remoteAdd.getHostAddress()).booleanValue() && Collector.hasGrade(Collector.getDnsHostName(report.remoteAdd.getHostAddress()))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Const.SSLLABS_URL);
                stringBuilder.append(Collector.getCertHost(Collector.getDnsHostName(report.remoteAdd.getHostAddress())));
                MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())));
                return false;
            } else {
                try {
                    view.animate().setDuration(500).alpha(0.5f).withEndAction(new Runnable() {
                        public void run() {
                            Collector.provideDetail(report.uid, report.remoteAddHex);
                            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ReportDetailActivity.class));
                        }
                    });
                    return false;
                } catch (Exception unused) {
                    return false;
                }
            }
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.MainActivity$4 */
    class C04914 implements OnClickListener {
        C04914() {
        }

        public void onClick(View view) {
            Collector.saveReports(MainActivity.reportEntityDao);
            MainActivity.this.startStopTrigger();
        }
    }

    /* Access modifiers changed, original: protected */
    public int getNavigationDrawerID() {
        return C0501R.C0499id.nav_main;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        RunStore.setContext(this);
        RunStore.setAppContext(getApplicationContext());
        reportEntityDao = ((DBApp) getApplication()).getDaoSession().getReportEntityDao();
        for (String str : getSharedPreferences("SELECTEDAPPS", 0).getAll().values()) {
            if (!Collector.getAppsToIncludeInScan().contains(str)) {
                Collector.addAppToIncludeInScan(str);
            }
        }
        Collector.addAppToExcludeFromScan("app.android.unknown");
        Collector.addAppToExcludeFromScan("app.unknown");
        Collector.addAppToExcludeFromScan(EnvironmentCompat.MEDIA_UNKNOWN);
        if (RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            activateReportView();
        } else {
            activateMainView();
        }
        overridePendingTransition(0, 0);
    }

    private void setButtonListener() {
        ((FloatingActionButton) findViewById(C0501R.C0499id.mainFAB)).setOnClickListener(new C04861());
    }

    private void startStopTrigger() {
        Intent intent;
        if (RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            RunStore.getServiceHandler().stopPassiveService();
            intent = new Intent(RunStore.getContext(), MainActivity.class);
            intent.setFlags(67108864);
            startActivity(intent);
            return;
        }
        RunStore.getServiceHandler().startPassiveService();
        intent = new Intent(RunStore.getContext(), MainActivity.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }

    private void activateMainView() {
        setContentView((int) C0501R.layout.activity_main);
        super.setToolbar();
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(C0501R.C0499id.mainFAB);
        ((TextView) findViewById(C0501R.C0499id.main_text_startstop)).setText(C0501R.string.main_text_stopped);
        setButtonListener();
        getNavigationDrawerID();
    }

    private void activateReportView() {
        setContentView((int) C0501R.layout.activity_report);
        super.setToolbar();
        getNavigationDrawerID();
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0501R.C0499id.swipe_refresh_layout);
        this.reportMap = Collector.provideSimpleReports(reportEntityDao);
        this.expListView = (ExpandableListView) findViewById(C0501R.C0499id.list);
        this.expListView.setAdapter(new ExpandableReportAdapter(this, new ArrayList(this.reportMap.keySet()), this.reportMap));
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.post(new C04872());
        this.expListView.setOnChildClickListener(new C04903());
        ((FloatingActionButton) findViewById(C0501R.C0499id.reportFAB)).setOnClickListener(new C04914());
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void refreshAdapter() {
        boolean z = true;
        this.swipeRefreshLayout.setRefreshing(true);
        this.reportMap = Collector.provideSimpleReports(reportEntityDao);
        ExpandableReportAdapter expandableReportAdapter = new ExpandableReportAdapter(this, new ArrayList(this.reportMap.keySet()), this.reportMap);
        this.expListView.setAdapter(expandableReportAdapter);
        if (expandableReportAdapter.getGroupCount() <= 0) {
            z = false;
        }
        setSwipeInfo(z);
        this.swipeRefreshLayout.setRefreshing(false);
    }

    private void setSwipeInfo(boolean z) {
        ImageView imageView = (ImageView) findViewById(C0501R.C0499id.report_empty_icon);
        TextView textView = (TextView) findViewById(C0501R.C0499id.report_empty_text);
        if (z) {
            imageView.setVisibility(8);
            textView.setVisibility(8);
            return;
        }
        imageView.setVisibility(0);
        textView.setVisibility(0);
    }

    public void onRefresh() {
        refreshAdapter();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0501R.C0499id.action_refresh) {
            refreshAdapter();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (RunStore.getServiceHandler().isServiceRunning(PassiveService.class)) {
            getMenuInflater().inflate(C0501R.C0500menu.toolbar_menu, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
