package org.secuso.privacyfriendlynetmonitor.Activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.p003v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableHistoryListAdapter;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class HistoryActivity extends BaseActivity {
    private Editor editor;
    private ExpandableListView expListView;
    private ExpandableHistoryListAdapter historyReportAdapter;
    private HashMap<String, List<ReportEntity>> historyReportMap;
    private List<String> keys;
    private ProgressBar progressBar;
    private ReportEntityDao reportEntityDao;
    private SharedPreferences selectedAppsPreferences;

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.HistoryActivity$1 */
    class C04801 implements OnClickListener {
        C04801() {
        }

        public void onClick(View view) {
            HistoryActivity.this.progressBar.setVisibility(0);
            HistoryActivity.this.startActivity(new Intent(HistoryActivity.this, SelectHistoryAppsActivity.class));
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.HistoryActivity$2 */
    class C04812 implements OnClickListener {
        C04812() {
        }

        public void onClick(View view) {
            HistoryActivity.this.deleteConfirmation();
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.HistoryActivity$4 */
    class C04834 implements OnItemLongClickListener {
        C04834() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            String charSequence = ((TextView) view.findViewById(C0501R.C0499id.historyGroupTitle)).getText().toString();
            String charSequence2 = ((TextView) view.findViewById(C0501R.C0499id.historyGroupSubtitle)).getText().toString();
            Intent intent = new Intent(HistoryActivity.this, AppConnections_Chart.class);
            intent.putExtra("AppName", charSequence);
            intent.putExtra("AppSubName", charSequence2);
            HistoryActivity.this.startActivity(intent);
            return false;
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.HistoryActivity$5 */
    class C04845 implements DialogInterface.OnClickListener {
        C04845() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            HistoryActivity.this.reportEntityDao.deleteAll();
            HistoryActivity.this.activateHistoryView();
            Toast.makeText(HistoryActivity.this.getApplicationContext(), "All reports have been deleted.", 0).show();
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.HistoryActivity$6 */
    class C04856 implements DialogInterface.OnClickListener {
        C04856() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(HistoryActivity.this.getApplicationContext(), "Deletion canceled.", 0).show();
        }
    }

    /* Access modifiers changed, original: protected */
    public int getNavigationDrawerID() {
        return C0501R.C0499id.nav_history;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0501R.layout.activity_history);
        setSupportActionBar((Toolbar) findViewById(C0501R.C0499id.toolbar));
        this.progressBar = (ProgressBar) findViewById(C0501R.C0499id.selectHistoryProgressBar);
        this.progressBar.setVisibility(8);
        this.reportEntityDao = ((DBApp) getApplication()).getDaoSession().getReportEntityDao();
        this.selectedAppsPreferences = getSharedPreferences("SELECTEDAPPS", 0);
        this.editor = this.selectedAppsPreferences.edit();
        ((FloatingActionButton) findViewById(C0501R.C0499id.fab)).setOnClickListener(new C04801());
        ((Button) findViewById(C0501R.C0499id.deleteDB)).setOnClickListener(new C04812());
        activateHistoryView();
    }

    public void onPostResume() {
        super.onPostResume();
        this.progressBar.setVisibility(8);
    }

    private void activateHistoryView() {
        this.expListView = (ExpandableListView) findViewById(C0501R.C0499id.list_history);
        final HashMap provideHistoryReports = provideHistoryReports();
        TextView textView = (TextView) findViewById(C0501R.C0499id.noData);
        if (provideHistoryReports.isEmpty()) {
            if (textView.getVisibility() == 4) {
                textView.setVisibility(0);
            }
        } else if (textView.getVisibility() == 0) {
            textView.setVisibility(4);
        }
        this.historyReportAdapter = new ExpandableHistoryListAdapter(this, new ArrayList(provideHistoryReports.keySet()), provideHistoryReports);
        this.expListView.setAdapter(this.historyReportAdapter);
        this.expListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                List access$300 = HistoryActivity.this.prepareData((ReportEntity) ((List) provideHistoryReports.get(HistoryActivity.this.keys.get(i))).get(i2));
                Intent intent = new Intent(HistoryActivity.this.getBaseContext(), HistoryDetailActivity.class);
                intent.putExtra("Details", (ArrayList) access$300);
                HistoryActivity.this.startActivity(intent);
                return false;
            }
        });
        this.expListView.setOnItemLongClickListener(new C04834());
    }

    private List<String> prepareData(ReportEntity reportEntity) {
        PackageInfo packageInfo;
        PackageManager packageManager = getPackageManager();
        ArrayList arrayList = new ArrayList();
        String appName = reportEntity.getAppName();
        arrayList.add(appName);
        arrayList.add(reportEntity.getUserID());
        try {
            packageInfo = packageManager.getPackageInfo(appName, 0);
        } catch (NameNotFoundException unused) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find package info for ");
            stringBuilder.append(appName);
            stringBuilder.append(".");
            printStream.println(stringBuilder.toString());
            packageInfo = null;
        }
        arrayList.add(packageInfo.versionName);
        arrayList.add(new Date(packageInfo.firstInstallTime).toString());
        arrayList.add(reportEntity.getRemoteAddress());
        arrayList.add(reportEntity.getRemoteHex());
        arrayList.add(reportEntity.getRemoteHost());
        arrayList.add(reportEntity.getLocalAddress());
        arrayList.add(reportEntity.getLocalHex());
        arrayList.add(reportEntity.getServicePort());
        arrayList.add(reportEntity.getPayloadProtocol());
        arrayList.add(reportEntity.getTransportProtocol());
        arrayList.add(reportEntity.getLocalPort());
        arrayList.add(reportEntity.getTimeStamp());
        arrayList.add(reportEntity.getConnectionInfo());
        return arrayList;
    }

    private HashMap<String, List<ReportEntity>> provideHistoryReports() {
        String userID;
        this.historyReportMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (ReportEntity reportEntity : this.reportEntityDao.loadAll()) {
            userID = reportEntity.getUserID();
            if (arrayList2.contains(userID)) {
                ((List) this.historyReportMap.get(userID)).add(reportEntity);
            } else {
                arrayList2.add(userID);
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(reportEntity);
                if (!arrayList.contains(reportEntity.getAppName())) {
                    arrayList.add(reportEntity.getAppName());
                }
                this.historyReportMap.put(userID, arrayList3);
            }
        }
        List appsToIncludeInScan = Collector.getAppsToIncludeInScan();
        if (!appsToIncludeInScan.isEmpty()) {
            ArrayList<String> arrayList4 = new ArrayList(new LinkedHashSet(appsToIncludeInScan));
            arrayList4.removeAll(arrayList);
            if (!arrayList4.isEmpty()) {
                for (String str : arrayList4) {
                    try {
                        int i = getPackageManager().getApplicationInfo(str, 0).uid;
                        if (!Collector.getKnownUIDs().containsKey(Integer.valueOf(i))) {
                            userID = new String();
                            Collector.addKnownUIDs(String.valueOf(i), str);
                        }
                        HashMap hashMap = this.historyReportMap;
                        userID = new String();
                        hashMap.put(String.valueOf(i), new ArrayList());
                    } catch (NameNotFoundException unused) {
                    }
                }
                arrayList4.clear();
            }
        }
        this.keys = new ArrayList(this.historyReportMap.keySet());
        for (String str2 : this.keys) {
            Collections.reverse((List) this.historyReportMap.get(str2));
        }
        return this.historyReportMap;
    }

    public void onResume() {
        super.onResume();
        activateHistoryView();
    }

    private void deleteConfirmation() {
        Builder builder = new Builder(this);
        builder.setTitle(C0501R.string.dialogTitle);
        builder.setPositiveButton(C0501R.string.yes, new C04845());
        builder.setNegativeButton(C0501R.string.f83no, new C04856());
        System.out.println("Building complete");
        builder.create().show();
    }
}
