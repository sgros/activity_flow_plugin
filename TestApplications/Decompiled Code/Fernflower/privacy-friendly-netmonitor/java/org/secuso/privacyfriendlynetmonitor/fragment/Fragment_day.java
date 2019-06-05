package org.secuso.privacyfriendlynetmonitor.fragment;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.FragmentDayListAdapter;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class Fragment_day extends Fragment {
   private static List entitiesString = new ArrayList();
   private static List filtered_Entities = new ArrayList();
   private static List reportEntities;
   private static ReportEntityDao reportEntityDao;
   private RecyclerView.Adapter mAdapter;
   private RecyclerView.LayoutManager mLayoutManager;
   private RecyclerView mRecyclerView;

   private void fillChart(View var1, BarChart var2) {
      SimpleDateFormat var14 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      Date var15;
      try {
         Date var3 = new Date();
         var15 = var14.parse(var14.format(var3));
      } catch (ParseException var13) {
         var13.printStackTrace();
         var15 = null;
      }

      int var4 = var15.getHours();
      ArrayList var5 = new ArrayList();
      int[] var16 = new int[24];
      int[] var18 = new int[24];
      int[] var6 = new int[24];
      Iterator var7 = filtered_Entities.iterator();

      while(true) {
         boolean var8 = var7.hasNext();
         int var9 = 0;
         int var11;
         if (!var8) {
            int[] var26 = new int[24];
            int[] var12 = new int[24];
            int[] var25 = new int[24];

            for(var9 = 0; var9 < var16.length; ++var9) {
               var11 = (var9 + (23 - var4)) % 24;
               var26[var11] = var16[var9];
               var12[var11] = var18[var9];
               var25[var11] = var6[var9];
            }

            for(var9 = 0; var9 < var26.length; ++var9) {
               var5.add(new BarEntry((float)var9, new float[]{(float)var25[var9], (float)var26[var9], (float)var12[var9]}));
            }

            BarDataSet var20 = new BarDataSet(var5, this.getResources().getString(2131624023));
            var20.setStackLabels(new String[]{this.getResources().getString(2131624075), this.getResources().getString(2131623995), this.getResources().getString(2131624074)});
            var20.setColors(new int[]{ContextCompat.getColor(this.getContext(), 2131099756), ContextCompat.getColor(this.getContext(), 2131099715), ContextCompat.getColor(this.getContext(), 2131099743)});
            final String[] var17 = new String[var16.length];

            for(var9 = 0; var9 < var17.length; ++var9) {
               StringBuilder var23;
               if (var9 == var17.length - 1) {
                  var23 = new StringBuilder();
                  var23.append(var4);
                  var23.append(this.getResources().getString(2131624034));
                  var17[var9] = var23.toString();
               } else {
                  var23 = new StringBuilder();
                  var23.append("- ");
                  var23.append(Integer.toString(23 - var9));
                  var23.append(this.getResources().getString(2131624024));
                  var17[var9] = var23.toString();
               }
            }

            IAxisValueFormatter var19 = new IAxisValueFormatter() {
               public String getFormattedValue(float var1, AxisBase var2) {
                  return var17[(int)var1];
               }
            };
            XAxis var24 = var2.getXAxis();
            var24.setGranularity(1.0F);
            var24.setValueFormatter(var19);
            var24.setPosition(XAxis.XAxisPosition.BOTTOM);
            var2.getAxisLeft().setAxisMinimum(0.0F);
            var2.getAxisRight().setAxisMinimum(0.0F);
            BarData var21 = new BarData(new IBarDataSet[]{var20});
            var21.setBarWidth(0.5F);
            var2.setData(var21);
            var2.setFitBars(true);
            Description var22 = new Description();
            var22.setText("");
            var2.setDescription(var22);
            var2.invalidate();
            return;
         }

         ReportEntity var10 = (ReportEntity)var7.next();
         var11 = this.getEntityHour(var10);
         if (var11 != 24) {
            var9 = var11;
         }

         int var10002;
         if (var10.getConnectionInfo().contains("Encrypted")) {
            var10002 = var16[var9]++;
         } else if (var10.getConnectionInfo().contains("Unencrypted")) {
            var10002 = var18[var9]++;
         } else if (var10.getConnectionInfo().contains("Unknown")) {
            var10002 = var6[var9]++;
         }
      }
   }

   private void fillRecyclerList(View var1, List var2) {
      this.mRecyclerView = (RecyclerView)var1.findViewById(2131296403);
      this.mRecyclerView.setFocusable(false);
      this.mRecyclerView.setNestedScrollingEnabled(false);
      this.mRecyclerView.setHasFixedSize(true);
      this.mLayoutManager = new LinearLayoutManager(this.getContext());
      this.mRecyclerView.setLayoutManager(this.mLayoutManager);
      this.mAdapter = new FragmentDayListAdapter(var2, this.getContext());
      this.mRecyclerView.setAdapter(this.mAdapter);
   }

   private int getEntityHour(ReportEntity var1) {
      String var4 = var1.getTimeStamp();
      SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      Date var5;
      try {
         var5 = var2.parse(var4);
      } catch (ParseException var3) {
         var3.printStackTrace();
         var5 = null;
      }

      return var5.getHours();
   }

   private void loadFilteredList(String var1) {
      filtered_Entities.clear();
      entitiesString.clear();
      reportEntityDao = ((DBApp)this.getActivity().getApplication()).getDaoSession().getReportEntityDao();
      reportEntities = reportEntityDao.loadAll();
      Iterator var2 = reportEntities.iterator();

      while(true) {
         boolean var3;
         ReportEntity var4;
         do {
            var3 = false;
            if (!var2.hasNext()) {
               return;
            }

            var4 = (ReportEntity)var2.next();
         } while(!var4.getAppName().equals(var1));

         String var5 = var4.toStringWithoutTimestamp();
         Iterator var6 = entitiesString.iterator();

         while(var6.hasNext()) {
            if (((String)var6.next()).equals(var5)) {
               var3 = true;
            }
         }

         if (!var3) {
            SimpleDateFormat var10 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            try {
               Date var7 = new Date();
               Date var11 = var10.parse(var10.format(var7));
               Calendar var12 = Calendar.getInstance();
               var12.setTime(var11);
               var12.add(5, -1);
               var11 = var12.getTime();
               String var13 = var4.getTimeStamp();
               SimpleDateFormat var8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
               if (var8.parse(var13).after(var11)) {
                  filtered_Entities.add(var4);
                  entitiesString.add(var5);
               }
            } catch (ParseException var9) {
               var9.printStackTrace();
            }
         }
      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      super.onCreate(var3);
      final View var8 = var1.inflate(2131427389, var2, false);
      ((ScrollView)var8.findViewById(2131296464)).setSmoothScrollingEnabled(true);
      PackageManager var9 = this.getActivity().getPackageManager();
      TextView var4 = (TextView)var8.findViewById(2131296361);
      String var7 = this.getArguments().getString("AppName");
      var4.setText(var7);

      try {
         ImageView var11 = (ImageView)var8.findViewById(2131296360);
         String var5 = (String)var9.getApplicationLabel(var9.getApplicationInfo(var7, 128));
         ((TextView)var8.findViewById(2131296362)).setText(var5);
         var11.setImageDrawable(var9.getApplicationIcon(var7));
      } catch (NameNotFoundException var6) {
         var6.printStackTrace();
      }

      BarChart var10 = (BarChart)var8.findViewById(2131296316);
      this.loadFilteredList(var7);
      this.fillChart(var8, var10);
      this.fillRecyclerList(var8, filtered_Entities);
      var10.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
         public void onNothingSelected() {
            Fragment_day.this.fillRecyclerList(var8, Fragment_day.filtered_Entities);
         }

         public void onValueSelected(Entry var1, Highlight var2) {
            SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            Date var8x;
            try {
               Date var4 = new Date();
               var8x = var3.parse(var3.format(var4));
            } catch (ParseException var7) {
               var7.printStackTrace();
               var8x = null;
            }

            int var5 = var8x.getHours();
            ArrayList var9 = new ArrayList();
            if (var1.getY() != 0.0F) {
               Iterator var6 = Fragment_day.filtered_Entities.iterator();

               while(var6.hasNext()) {
                  ReportEntity var10 = (ReportEntity)var6.next();
                  if ((float)((Fragment_day.this.getEntityHour(var10) + (23 - var5)) % 24) == var1.getX()) {
                     if (var2.getStackIndex() == 0 && var10.getConnectionInfo().contains("Unknown")) {
                        var9.add(var10);
                     }

                     if (var2.getStackIndex() == 1 && var10.getConnectionInfo().contains("Encrypted")) {
                        var9.add(var10);
                     }

                     if (var2.getStackIndex() == 2 && var10.getConnectionInfo().contains("Unencrypted")) {
                        var9.add(var10);
                     }
                  }
               }

               Fragment_day.this.fillRecyclerList(var8, var9);
            }

         }
      });
      return var8;
   }
}
