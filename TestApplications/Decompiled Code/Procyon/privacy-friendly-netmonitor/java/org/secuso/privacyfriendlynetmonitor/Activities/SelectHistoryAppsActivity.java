// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.MenuItem;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.support.v7.app.ActionBar;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import android.os.Bundle;
import android.content.pm.PackageManager$NameNotFoundException;
import android.support.v7.widget.DividerItemDecoration;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.AppListRecyclerAdapter;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import java.util.Iterator;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.content.pm.PackageInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;
import android.content.SharedPreferences;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import android.content.SharedPreferences$Editor;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;

public class SelectHistoryAppsActivity extends AppCompatActivity
{
    private RecyclerView.Adapter appListRecyclerAdapter;
    private RecyclerView appListRecyclerView;
    private List<String> app_list_name;
    private SharedPreferences$Editor editor;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private ReportEntityDao reportEntityDao;
    private SharedPreferences selectedAppsPreferences;
    private Map<String, String> sortMap;
    
    public SelectHistoryAppsActivity() {
        this.sortMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    }
    
    private List<String> provideAppList() {
        final ArrayList<String> list = new ArrayList<String>();
        final PackageManager packageManager = this.getPackageManager();
        final List installedPackages = packageManager.getInstalledPackages(0);
        final List installedPackages2 = packageManager.getInstalledPackages(4096);
        for (int i = 0; i < installedPackages.size(); ++i) {
            final PackageInfo packageInfo = installedPackages.get(i);
            final PackageInfo packageInfo2 = installedPackages2.get(i);
            if (packageInfo2.requestedPermissions != null) {
                final String[] requestedPermissions = packageInfo2.requestedPermissions;
                for (int length = requestedPermissions.length, j = 0; j < length; ++j) {
                    if (TextUtils.equals((CharSequence)requestedPermissions[j], (CharSequence)"android.permission.INTERNET")) {
                        this.sortMap.put(packageInfo.applicationInfo.loadLabel(this.getPackageManager()).toString(), packageInfo.packageName);
                        break;
                    }
                }
            }
        }
        final Iterator<Map.Entry<String, String>> iterator = this.sortMap.entrySet().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getValue().toString());
        }
        return list;
    }
    
    private void show_APP_list() {
        this.app_list_name = this.provideAppList();
        this.appListRecyclerView = this.findViewById(2131296390);
        this.recyclerLayoutManager = new LinearLayoutManager((Context)this);
        this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
        this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, (Context)this);
        this.appListRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new DividerItemDecoration((Context)this, 1));
        this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
    }
    
    private void sortAlphabetic_asc() {
        this.app_list_name.clear();
        final TreeMap<String, String> sortMap = new TreeMap<String, String>(new AscComparator(this.sortMap));
        sortMap.putAll((Map<?, ?>)this.sortMap);
        this.sortMap = sortMap;
        final Iterator<Map.Entry<String, String>> iterator = this.sortMap.entrySet().iterator();
        while (iterator.hasNext()) {
            this.app_list_name.add(iterator.next().getValue().toString());
        }
    }
    
    private void sortAlphabetic_desc() {
        this.app_list_name.clear();
        final TreeMap<String, String> sortMap = new TreeMap<String, String>(new DescComparator(this.sortMap));
        sortMap.putAll((Map<?, ?>)this.sortMap);
        this.sortMap = sortMap;
        final Iterator<Map.Entry<String, String>> iterator = this.sortMap.entrySet().iterator();
        while (iterator.hasNext()) {
            this.app_list_name.add(iterator.next().getValue().toString());
        }
    }
    
    private void sortInstalledDate_asc() {
        final TreeMap<Long, List<String>> treeMap = (TreeMap<Long, List<String>>)new TreeMap<Object, List<String>>();
        final PackageManager packageManager = this.getPackageManager();
        for (int i = 0; i < this.app_list_name.size(); ++i) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo((String)this.app_list_name.get(i), 0);
            }
            catch (PackageManager$NameNotFoundException ex) {
                ex.printStackTrace();
            }
            final long firstInstallTime = packageInfo.firstInstallTime;
            if (treeMap.containsKey(firstInstallTime)) {
                treeMap.get(firstInstallTime).add(this.app_list_name.get(i));
            }
            else {
                final ArrayList<String> list = new ArrayList<String>();
                list.add(this.app_list_name.get(i));
                treeMap.put(firstInstallTime, list);
            }
        }
        final TreeMap treeMap2 = new TreeMap<Object, Object>(new AscComparator(treeMap));
        treeMap2.putAll((Map<?, ?>)treeMap);
        this.app_list_name.clear();
        final Iterator<Long> iterator = treeMap2.keySet().iterator();
        while (iterator.hasNext()) {
            final Iterator<String> iterator2 = treeMap2.get(iterator.next()).iterator();
            while (iterator2.hasNext()) {
                this.app_list_name.add(iterator2.next());
            }
        }
    }
    
    private void sortInstalledDate_desc() {
        final TreeMap<Long, List<String>> treeMap = (TreeMap<Long, List<String>>)new TreeMap<Object, List<String>>();
        final PackageManager packageManager = this.getPackageManager();
        for (int i = 0; i < this.app_list_name.size(); ++i) {
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo((String)this.app_list_name.get(i), 0);
            }
            catch (PackageManager$NameNotFoundException ex) {
                ex.printStackTrace();
            }
            final long firstInstallTime = packageInfo.firstInstallTime;
            if (treeMap.containsKey(firstInstallTime)) {
                treeMap.get(firstInstallTime).add(this.app_list_name.get(i));
            }
            else {
                final ArrayList<String> list = new ArrayList<String>();
                list.add(this.app_list_name.get(i));
                treeMap.put(firstInstallTime, list);
            }
        }
        final TreeMap treeMap2 = new TreeMap<Object, Object>(new DescComparator(treeMap));
        treeMap2.putAll((Map<?, ?>)treeMap);
        this.app_list_name.clear();
        final Iterator<Long> iterator = treeMap2.keySet().iterator();
        while (iterator.hasNext()) {
            final Iterator<String> iterator2 = treeMap2.get(iterator.next()).iterator();
            while (iterator2.hasNext()) {
                this.app_list_name.add(iterator2.next());
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427367);
        this.reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        this.selectedAppsPreferences = this.getSharedPreferences("SELECTEDAPPS", 0);
        this.editor = this.selectedAppsPreferences.edit();
        this.show_APP_list();
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131492864, menu);
        ((SearchView)MenuItemCompat.getActionView(menu.findItem(2131296281))).setOnQueryTextListener((SearchView.OnQueryTextListener)new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String s) {
                SelectHistoryAppsActivity.this.app_list_name.clear();
                for (final Map.Entry<String, V> entry : SelectHistoryAppsActivity.this.sortMap.entrySet()) {
                    if (entry.getKey().toLowerCase().contains(s.toLowerCase())) {
                        SelectHistoryAppsActivity.this.app_list_name.add(((String)entry.getValue()).toString());
                    }
                }
                SelectHistoryAppsActivity.this.appListRecyclerView = SelectHistoryAppsActivity.this.findViewById(2131296390);
                SelectHistoryAppsActivity.this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
                SelectHistoryAppsActivity.this.appListRecyclerView.setLayoutManager(SelectHistoryAppsActivity.this.recyclerLayoutManager);
                SelectHistoryAppsActivity.this.appListRecyclerAdapter = new AppListRecyclerAdapter(SelectHistoryAppsActivity.this.app_list_name, (Context)SelectHistoryAppsActivity.this);
                SelectHistoryAppsActivity.this.appListRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new DividerItemDecoration((Context)SelectHistoryAppsActivity.this, 1));
                SelectHistoryAppsActivity.this.appListRecyclerView.setAdapter(SelectHistoryAppsActivity.this.appListRecyclerAdapter);
                return false;
            }
            
            @Override
            public boolean onQueryTextSubmit(final String s) {
                return false;
            }
        });
        return true;
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        final int itemId = menuItem.getItemId();
        if (itemId == 2131296283) {
            menuItem.setChecked(true);
            this.sortAlphabetic_asc();
        }
        else if (itemId == 2131296284) {
            menuItem.setChecked(true);
            this.sortAlphabetic_desc();
        }
        else if (itemId == 2131296285) {
            menuItem.setChecked(true);
            this.sortInstalledDate_asc();
        }
        else if (itemId == 2131296286) {
            menuItem.setChecked(true);
            this.sortInstalledDate_desc();
        }
        this.appListRecyclerView = this.findViewById(2131296390);
        this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
        this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
        this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, (Context)this);
        this.appListRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new DividerItemDecoration((Context)this, 1));
        this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
        return super.onOptionsItemSelected(menuItem);
    }
    
    class AscComparator implements Comparator
    {
        Map map;
        
        public AscComparator(final Map map) {
            this.map = map;
        }
        
        @Override
        public int compare(final Object o, final Object o2) {
            return o.toString().compareToIgnoreCase(o2.toString());
        }
    }
    
    class DescComparator implements Comparator
    {
        Map map;
        
        public DescComparator(final Map map) {
            this.map = map;
        }
        
        @Override
        public int compare(final Object o, final Object o2) {
            return o2.toString().compareToIgnoreCase(o.toString());
        }
    }
}
