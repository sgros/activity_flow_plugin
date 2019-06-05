package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.p000v4.view.MenuItemCompat;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.DividerItemDecoration;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.C0359Adapter;
import android.support.p003v7.widget.RecyclerView.LayoutManager;
import android.support.p003v7.widget.SearchView;
import android.support.p003v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.AppListRecyclerAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class SelectHistoryAppsActivity extends AppCompatActivity {
    private C0359Adapter appListRecyclerAdapter;
    private RecyclerView appListRecyclerView;
    private List<String> app_list_name;
    private Editor editor;
    private LayoutManager recyclerLayoutManager;
    private ReportEntityDao reportEntityDao;
    private SharedPreferences selectedAppsPreferences;
    private Map<String, String> sortMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    class AscComparator implements Comparator {
        Map map;

        public AscComparator(Map map) {
            this.map = map;
        }

        public int compare(Object obj, Object obj2) {
            return obj.toString().compareToIgnoreCase(obj2.toString());
        }
    }

    class DescComparator implements Comparator {
        Map map;

        public DescComparator(Map map) {
            this.map = map;
        }

        public int compare(Object obj, Object obj2) {
            return obj2.toString().compareToIgnoreCase(obj.toString());
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.SelectHistoryAppsActivity$1 */
    class C05521 implements OnQueryTextListener {
        public boolean onQueryTextSubmit(String str) {
            return false;
        }

        C05521() {
        }

        public boolean onQueryTextChange(String str) {
            SelectHistoryAppsActivity.this.app_list_name.clear();
            for (Entry entry : SelectHistoryAppsActivity.this.sortMap.entrySet()) {
                if (((String) entry.getKey()).toLowerCase().contains(str.toLowerCase())) {
                    SelectHistoryAppsActivity.this.app_list_name.add(((String) entry.getValue()).toString());
                }
            }
            SelectHistoryAppsActivity.this.appListRecyclerView = (RecyclerView) SelectHistoryAppsActivity.this.findViewById(C0501R.C0499id.list_selection_app_recycler);
            SelectHistoryAppsActivity.this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
            SelectHistoryAppsActivity.this.appListRecyclerView.setLayoutManager(SelectHistoryAppsActivity.this.recyclerLayoutManager);
            SelectHistoryAppsActivity.this.appListRecyclerAdapter = new AppListRecyclerAdapter(SelectHistoryAppsActivity.this.app_list_name, SelectHistoryAppsActivity.this);
            SelectHistoryAppsActivity.this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(SelectHistoryAppsActivity.this, 1));
            SelectHistoryAppsActivity.this.appListRecyclerView.setAdapter(SelectHistoryAppsActivity.this.appListRecyclerAdapter);
            return false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0501R.layout.activity_select_history_apps);
        this.reportEntityDao = ((DBApp) getApplication()).getDaoSession().getReportEntityDao();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        this.selectedAppsPreferences = getSharedPreferences("SELECTEDAPPS", 0);
        this.editor = this.selectedAppsPreferences.edit();
        show_APP_list();
    }

    private void show_APP_list() {
        this.app_list_name = provideAppList();
        this.appListRecyclerView = (RecyclerView) findViewById(C0501R.C0499id.list_selection_app_recycler);
        this.recyclerLayoutManager = new LinearLayoutManager(this);
        this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
        this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, this);
        this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
    }

    private List<String> provideAppList() {
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = getPackageManager();
        List installedPackages = packageManager.getInstalledPackages(0);
        List installedPackages2 = packageManager.getInstalledPackages(4096);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);
            PackageInfo packageInfo2 = (PackageInfo) installedPackages2.get(i);
            if (packageInfo2.requestedPermissions != null) {
                for (CharSequence equals : packageInfo2.requestedPermissions) {
                    if (TextUtils.equals(equals, "android.permission.INTERNET")) {
                        this.sortMap.put(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(), packageInfo.packageName);
                        break;
                    }
                }
            }
        }
        for (Entry value : this.sortMap.entrySet()) {
            arrayList.add(value.getValue().toString());
        }
        return arrayList;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0501R.C0500menu.applistseletion_menu, menu);
        ((SearchView) MenuItemCompat.getActionView(menu.findItem(C0501R.C0499id.action_search))).setOnQueryTextListener(new C05521());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == C0501R.C0499id.action_sort_alphabetical_asc) {
            menuItem.setChecked(true);
            sortAlphabetic_asc();
        } else if (itemId == C0501R.C0499id.action_sort_alphabetical_desc) {
            menuItem.setChecked(true);
            sortAlphabetic_desc();
        } else if (itemId == C0501R.C0499id.action_sort_installdate_asc) {
            menuItem.setChecked(true);
            sortInstalledDate_asc();
        } else if (itemId == C0501R.C0499id.action_sort_installdate_desc) {
            menuItem.setChecked(true);
            sortInstalledDate_desc();
        }
        this.appListRecyclerView = (RecyclerView) findViewById(C0501R.C0499id.list_selection_app_recycler);
        this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
        this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
        this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, this);
        this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
        return super.onOptionsItemSelected(menuItem);
    }

    private void sortAlphabetic_asc() {
        this.app_list_name.clear();
        TreeMap treeMap = new TreeMap(new AscComparator(this.sortMap));
        treeMap.putAll(this.sortMap);
        this.sortMap = treeMap;
        for (Entry value : this.sortMap.entrySet()) {
            this.app_list_name.add(((String) value.getValue()).toString());
        }
    }

    private void sortAlphabetic_desc() {
        this.app_list_name.clear();
        TreeMap treeMap = new TreeMap(new DescComparator(this.sortMap));
        treeMap.putAll(this.sortMap);
        this.sortMap = treeMap;
        for (Entry value : this.sortMap.entrySet()) {
            this.app_list_name.add(((String) value.getValue()).toString());
        }
    }

    private void sortInstalledDate_asc() {
        TreeMap treeMap = new TreeMap();
        PackageManager packageManager = getPackageManager();
        for (int i = 0; i < this.app_list_name.size(); i++) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo((String) this.app_list_name.get(i), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            long j = packageInfo.firstInstallTime;
            if (treeMap.containsKey(Long.valueOf(j))) {
                ((List) treeMap.get(Long.valueOf(j))).add(this.app_list_name.get(i));
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.app_list_name.get(i));
                treeMap.put(Long.valueOf(j), arrayList);
            }
        }
        TreeMap treeMap2 = new TreeMap(new AscComparator(treeMap));
        treeMap2.putAll(treeMap);
        this.app_list_name.clear();
        for (Long l : treeMap2.keySet()) {
            for (String add : (List) treeMap2.get(l)) {
                this.app_list_name.add(add);
            }
        }
    }

    private void sortInstalledDate_desc() {
        TreeMap treeMap = new TreeMap();
        PackageManager packageManager = getPackageManager();
        for (int i = 0; i < this.app_list_name.size(); i++) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo((String) this.app_list_name.get(i), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            long j = packageInfo.firstInstallTime;
            if (treeMap.containsKey(Long.valueOf(j))) {
                ((List) treeMap.get(Long.valueOf(j))).add(this.app_list_name.get(i));
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.app_list_name.get(i));
                treeMap.put(Long.valueOf(j), arrayList);
            }
        }
        TreeMap treeMap2 = new TreeMap(new DescComparator(treeMap));
        treeMap2.putAll(treeMap);
        this.app_list_name.clear();
        for (Long l : treeMap2.keySet()) {
            for (String add : (List) treeMap2.get(l)) {
                this.app_list_name.add(add);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
