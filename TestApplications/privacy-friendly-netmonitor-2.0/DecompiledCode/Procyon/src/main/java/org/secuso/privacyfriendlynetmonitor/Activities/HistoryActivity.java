// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import org.greenrobot.greendao.AbstractDao;
import android.widget.Button;
import android.view.View$OnClickListener;
import android.support.design.widget.FloatingActionButton;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashSet;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import java.io.PrintStream;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.sql.Date;
import android.content.pm.PackageManager$NameNotFoundException;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemLongClickListener;
import java.io.Serializable;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView$OnChildClickListener;
import android.widget.ExpandableListAdapter;
import android.content.Context;
import java.util.Collection;
import java.util.ArrayList;
import android.widget.TextView;
import android.content.SharedPreferences;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import android.widget.ProgressBar;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.List;
import java.util.HashMap;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableHistoryListAdapter;
import android.widget.ExpandableListView;
import android.content.SharedPreferences$Editor;

public class HistoryActivity extends BaseActivity
{
    private SharedPreferences$Editor editor;
    private ExpandableListView expListView;
    private ExpandableHistoryListAdapter historyReportAdapter;
    private HashMap<String, List<ReportEntity>> historyReportMap;
    private List<String> keys;
    private ProgressBar progressBar;
    private ReportEntityDao reportEntityDao;
    private SharedPreferences selectedAppsPreferences;
    
    private void activateHistoryView() {
        this.expListView = this.findViewById(2131296388);
        final HashMap<String, List<ReportEntity>> provideHistoryReports = this.provideHistoryReports();
        final TextView textView = this.findViewById(2131296415);
        if (provideHistoryReports.isEmpty()) {
            if (textView.getVisibility() == 4) {
                textView.setVisibility(0);
            }
        }
        else if (textView.getVisibility() == 0) {
            textView.setVisibility(4);
        }
        this.historyReportAdapter = new ExpandableHistoryListAdapter((Context)this, new ArrayList<String>(provideHistoryReports.keySet()), provideHistoryReports);
        this.expListView.setAdapter((ExpandableListAdapter)this.historyReportAdapter);
        this.expListView.setOnChildClickListener((ExpandableListView$OnChildClickListener)new ExpandableListView$OnChildClickListener() {
            public boolean onChildClick(final ExpandableListView expandableListView, final View view, final int n, final int n2, final long n3) {
                final List access$300 = HistoryActivity.this.prepareData(provideHistoryReports.get(HistoryActivity.this.keys.get(n)).get(n2));
                final Intent intent = new Intent(HistoryActivity.this.getBaseContext(), (Class)HistoryDetailActivity.class);
                intent.putExtra("Details", (Serializable)(ArrayList<?>)access$300);
                HistoryActivity.this.startActivity(intent);
                return false;
            }
        });
        this.expListView.setOnItemLongClickListener((AdapterView$OnItemLongClickListener)new AdapterView$OnItemLongClickListener() {
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final String string = ((TextView)view.findViewById(2131296362)).getText().toString();
                final String string2 = ((TextView)view.findViewById(2131296361)).getText().toString();
                final Intent intent = new Intent((Context)HistoryActivity.this, (Class)AppConnections_Chart.class);
                intent.putExtra("AppName", string);
                intent.putExtra("AppSubName", string2);
                HistoryActivity.this.startActivity(intent);
                return false;
            }
        });
    }
    
    private void deleteConfirmation() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle(2131623986);
        alertDialog$Builder.setPositiveButton(2131624082, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                HistoryActivity.this.reportEntityDao.deleteAll();
                HistoryActivity.this.activateHistoryView();
                Toast.makeText(HistoryActivity.this.getApplicationContext(), (CharSequence)"All reports have been deleted.", 0).show();
            }
        });
        alertDialog$Builder.setNegativeButton(2131624032, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Toast.makeText(HistoryActivity.this.getApplicationContext(), (CharSequence)"Deletion canceled.", 0).show();
            }
        });
        System.out.println("Building complete");
        alertDialog$Builder.create().show();
    }
    
    private List<String> prepareData(final ReportEntity reportEntity) {
        final PackageManager packageManager = this.getPackageManager();
        final ArrayList<String> list = new ArrayList<String>();
        final String appName = reportEntity.getAppName();
        list.add(appName);
        list.add(reportEntity.getUserID());
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(appName, 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find package info for ");
            sb.append(appName);
            sb.append(".");
            out.println(sb.toString());
            packageInfo = null;
        }
        list.add(packageInfo.versionName);
        list.add(new Date(packageInfo.firstInstallTime).toString());
        list.add(reportEntity.getRemoteAddress());
        list.add(reportEntity.getRemoteHex());
        list.add(reportEntity.getRemoteHost());
        list.add(reportEntity.getLocalAddress());
        list.add(reportEntity.getLocalHex());
        list.add(reportEntity.getServicePort());
        list.add(reportEntity.getPayloadProtocol());
        list.add(reportEntity.getTransportProtocol());
        list.add(reportEntity.getLocalPort());
        list.add(reportEntity.getTimeStamp());
        list.add(reportEntity.getConnectionInfo());
        return list;
    }
    
    private HashMap<String, List<ReportEntity>> provideHistoryReports() {
        this.historyReportMap = new HashMap<String, List<ReportEntity>>();
        Object iterator = new ArrayList<String>();
        final ArrayList<String> list = new ArrayList<String>();
        for (final ReportEntity reportEntity : ((AbstractDao<ReportEntity, K>)this.reportEntityDao).loadAll()) {
            final String userID = reportEntity.getUserID();
            if (!list.contains(userID)) {
                list.add(userID);
                final ArrayList<ReportEntity> value = new ArrayList<ReportEntity>();
                value.add(reportEntity);
                if (!((List)iterator).contains(reportEntity.getAppName())) {
                    ((List<String>)iterator).add(reportEntity.getAppName());
                }
                this.historyReportMap.put(userID, value);
            }
            else {
                this.historyReportMap.get(userID).add(reportEntity);
            }
        }
        Object c = Collector.getAppsToIncludeInScan();
        Label_0338: {
            if (((List)c).isEmpty()) {
                break Label_0338;
            }
            c = new ArrayList<Object>(new LinkedHashSet<Object>((Collection<?>)c));
            ((List)c).removeAll((Collection<?>)iterator);
            if (((List)c).isEmpty()) {
                break Label_0338;
            }
            iterator = ((List<String>)c).iterator();
        Block_10_Outer:
            while (true) {
                Label_0331: {
                    if (!((Iterator)iterator).hasNext()) {
                        break Label_0331;
                    }
                    final String s = ((Iterator<String>)iterator).next();
                    try {
                        final int uid = this.getPackageManager().getApplicationInfo(s, 0).uid;
                        if (!Collector.getKnownUIDs().containsKey(uid)) {
                            new String();
                            Collector.addKnownUIDs(String.valueOf(uid), s);
                        }
                        final HashMap<String, List<ReportEntity>> historyReportMap = this.historyReportMap;
                        new String();
                        historyReportMap.put(String.valueOf(uid), new ArrayList<ReportEntity>());
                        continue Block_10_Outer;
                        ((List)c).clear();
                        break Label_0338;
                        Label_0405: {
                            return this.historyReportMap;
                        }
                        while (true) {
                            iterator = ((Iterator<String>)c).next();
                            Collections.reverse(this.historyReportMap.get(iterator));
                            Label_0367: {
                                break Label_0367;
                                this.keys = new ArrayList<String>(this.historyReportMap.keySet());
                                c = this.keys.iterator();
                            }
                            continue;
                        }
                    }
                    // iftrue(Label_0405:, !c.hasNext())
                    catch (PackageManager$NameNotFoundException ex) {
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    @Override
    protected int getNavigationDrawerID() {
        return 2131296408;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427358);
        this.setSupportActionBar(this.findViewById(2131296523));
        (this.progressBar = this.findViewById(2131296478)).setVisibility(8);
        this.reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
        this.selectedAppsPreferences = this.getSharedPreferences("SELECTEDAPPS", 0);
        this.editor = this.selectedAppsPreferences.edit();
        this.findViewById(2131296346).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                HistoryActivity.this.progressBar.setVisibility(0);
                HistoryActivity.this.startActivity(new Intent((Context)HistoryActivity.this, (Class)SelectHistoryAppsActivity.class));
            }
        });
        this.findViewById(2131296329).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
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
