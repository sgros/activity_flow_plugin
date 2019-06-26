package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.AppListRecyclerAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class SelectHistoryAppsActivity extends AppCompatActivity {
   private RecyclerView.Adapter appListRecyclerAdapter;
   private RecyclerView appListRecyclerView;
   private List app_list_name;
   private Editor editor;
   private RecyclerView.LayoutManager recyclerLayoutManager;
   private ReportEntityDao reportEntityDao;
   private SharedPreferences selectedAppsPreferences;
   private Map sortMap;

   public SelectHistoryAppsActivity() {
      this.sortMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
   }

   private List provideAppList() {
      ArrayList var1 = new ArrayList();
      PackageManager var2 = this.getPackageManager();
      List var3 = var2.getInstalledPackages(0);
      List var9 = var2.getInstalledPackages(4096);

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         PackageInfo var5 = (PackageInfo)var3.get(var4);
         PackageInfo var6 = (PackageInfo)var9.get(var4);
         if (var6.requestedPermissions != null) {
            String[] var11 = var6.requestedPermissions;
            int var7 = var11.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               if (TextUtils.equals(var11[var8], "android.permission.INTERNET")) {
                  this.sortMap.put(var5.applicationInfo.loadLabel(this.getPackageManager()).toString(), var5.packageName);
                  break;
               }
            }
         }
      }

      Iterator var10 = this.sortMap.entrySet().iterator();

      while(var10.hasNext()) {
         var1.add(((Entry)var10.next()).getValue().toString());
      }

      return var1;
   }

   private void show_APP_list() {
      this.app_list_name = this.provideAppList();
      this.appListRecyclerView = (RecyclerView)this.findViewById(2131296390);
      this.recyclerLayoutManager = new LinearLayoutManager(this);
      this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
      this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, this);
      this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
      this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
   }

   private void sortAlphabetic_asc() {
      this.app_list_name.clear();
      TreeMap var1 = new TreeMap(new SelectHistoryAppsActivity.AscComparator(this.sortMap));
      var1.putAll(this.sortMap);
      this.sortMap = var1;
      Iterator var3 = this.sortMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.app_list_name.add(((String)var2.getValue()).toString());
      }

   }

   private void sortAlphabetic_desc() {
      this.app_list_name.clear();
      TreeMap var1 = new TreeMap(new SelectHistoryAppsActivity.DescComparator(this.sortMap));
      var1.putAll(this.sortMap);
      this.sortMap = var1;
      Iterator var3 = this.sortMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.app_list_name.add(((String)var2.getValue()).toString());
      }

   }

   private void sortInstalledDate_asc() {
      TreeMap var1 = new TreeMap();
      PackageManager var2 = this.getPackageManager();

      for(int var3 = 0; var3 < this.app_list_name.size(); ++var3) {
         PackageInfo var4 = null;

         label38: {
            PackageInfo var5;
            try {
               var5 = var2.getPackageInfo((String)this.app_list_name.get(var3), 0);
            } catch (NameNotFoundException var8) {
               var8.printStackTrace();
               break label38;
            }

            var4 = var5;
         }

         long var6 = var4.firstInstallTime;
         if (var1.containsKey(var6)) {
            ((List)var1.get(var6)).add(this.app_list_name.get(var3));
         } else {
            ArrayList var11 = new ArrayList();
            var11.add(this.app_list_name.get(var3));
            var1.put(var6, var11);
         }
      }

      TreeMap var12 = new TreeMap(new SelectHistoryAppsActivity.AscComparator(var1));
      var12.putAll(var1);
      this.app_list_name.clear();
      Iterator var10 = var12.keySet().iterator();

      while(var10.hasNext()) {
         Iterator var9 = ((List)var12.get((Long)var10.next())).iterator();

         while(var9.hasNext()) {
            String var13 = (String)var9.next();
            this.app_list_name.add(var13);
         }
      }

   }

   private void sortInstalledDate_desc() {
      TreeMap var1 = new TreeMap();
      PackageManager var2 = this.getPackageManager();

      for(int var3 = 0; var3 < this.app_list_name.size(); ++var3) {
         PackageInfo var4 = null;

         label38: {
            PackageInfo var5;
            try {
               var5 = var2.getPackageInfo((String)this.app_list_name.get(var3), 0);
            } catch (NameNotFoundException var8) {
               var8.printStackTrace();
               break label38;
            }

            var4 = var5;
         }

         long var6 = var4.firstInstallTime;
         if (var1.containsKey(var6)) {
            ((List)var1.get(var6)).add(this.app_list_name.get(var3));
         } else {
            ArrayList var11 = new ArrayList();
            var11.add(this.app_list_name.get(var3));
            var1.put(var6, var11);
         }
      }

      TreeMap var12 = new TreeMap(new SelectHistoryAppsActivity.DescComparator(var1));
      var12.putAll(var1);
      this.app_list_name.clear();
      Iterator var9 = var12.keySet().iterator();

      while(var9.hasNext()) {
         Iterator var10 = ((List)var12.get((Long)var9.next())).iterator();

         while(var10.hasNext()) {
            String var13 = (String)var10.next();
            this.app_list_name.add(var13);
         }
      }

   }

   public void onBackPressed() {
      super.onBackPressed();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427367);
      this.reportEntityDao = ((DBApp)this.getApplication()).getDaoSession().getReportEntityDao();
      ActionBar var2 = this.getSupportActionBar();
      if (var2 != null) {
         var2.setDisplayShowHomeEnabled(true);
      }

      this.selectedAppsPreferences = this.getSharedPreferences("SELECTEDAPPS", 0);
      this.editor = this.selectedAppsPreferences.edit();
      this.show_APP_list();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131492864, var1);
      ((SearchView)MenuItemCompat.getActionView(var1.findItem(2131296281))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         public boolean onQueryTextChange(String var1) {
            SelectHistoryAppsActivity.this.app_list_name.clear();
            Iterator var2 = SelectHistoryAppsActivity.this.sortMap.entrySet().iterator();

            while(var2.hasNext()) {
               Entry var3 = (Entry)var2.next();
               if (((String)var3.getKey()).toLowerCase().contains(var1.toLowerCase())) {
                  SelectHistoryAppsActivity.this.app_list_name.add(((String)var3.getValue()).toString());
               }
            }

            SelectHistoryAppsActivity.this.appListRecyclerView = (RecyclerView)SelectHistoryAppsActivity.this.findViewById(2131296390);
            SelectHistoryAppsActivity.this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
            SelectHistoryAppsActivity.this.appListRecyclerView.setLayoutManager(SelectHistoryAppsActivity.this.recyclerLayoutManager);
            SelectHistoryAppsActivity.this.appListRecyclerAdapter = new AppListRecyclerAdapter(SelectHistoryAppsActivity.this.app_list_name, SelectHistoryAppsActivity.this);
            SelectHistoryAppsActivity.this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(SelectHistoryAppsActivity.this, 1));
            SelectHistoryAppsActivity.this.appListRecyclerView.setAdapter(SelectHistoryAppsActivity.this.appListRecyclerAdapter);
            return false;
         }

         public boolean onQueryTextSubmit(String var1) {
            return false;
         }
      });
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      int var2 = var1.getItemId();
      if (var2 == 2131296283) {
         var1.setChecked(true);
         this.sortAlphabetic_asc();
      } else if (var2 == 2131296284) {
         var1.setChecked(true);
         this.sortAlphabetic_desc();
      } else if (var2 == 2131296285) {
         var1.setChecked(true);
         this.sortInstalledDate_asc();
      } else if (var2 == 2131296286) {
         var1.setChecked(true);
         this.sortInstalledDate_desc();
      }

      this.appListRecyclerView = (RecyclerView)this.findViewById(2131296390);
      this.recyclerLayoutManager = new LinearLayoutManager(RunStore.getContext());
      this.appListRecyclerView.setLayoutManager(this.recyclerLayoutManager);
      this.appListRecyclerAdapter = new AppListRecyclerAdapter(this.app_list_name, this);
      this.appListRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
      this.appListRecyclerView.setAdapter(this.appListRecyclerAdapter);
      return super.onOptionsItemSelected(var1);
   }

   class AscComparator implements Comparator {
      Map map;

      public AscComparator(Map var2) {
         this.map = var2;
      }

      public int compare(Object var1, Object var2) {
         return var1.toString().compareToIgnoreCase(var2.toString());
      }
   }

   class DescComparator implements Comparator {
      Map map;

      public DescComparator(Map var2) {
         this.map = var2;
      }

      public int compare(Object var1, Object var2) {
         return var2.toString().compareToIgnoreCase(var1.toString());
      }
   }
}
