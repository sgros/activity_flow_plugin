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

public class Fragment_month extends Fragment {
   static final long ONE_HOUR = 3600000L;
   private static Date currentDate;
   private static Date dateBefore1month;
   private static List entitiesString = new ArrayList();
   private static List filtered_Entities = new ArrayList();
   private static List reportEntities;
   private static ReportEntityDao reportEntityDao;
   private RecyclerView.Adapter mAdapter;
   private RecyclerView.LayoutManager mLayoutManager;
   private RecyclerView mRecyclerView;

   private void fillChart(View var1, BarChart var2) {
      int var3 = currentDate.getDate();
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[30];
      int[] var6 = new int[30];
      int[] var7 = new int[30];
      Iterator var8 = filtered_Entities.iterator();

      int var9;
      while(var8.hasNext()) {
         ReportEntity var10 = (ReportEntity)var8.next();
         var9 = this.getDaysBetween(dateBefore1month, this.getEntityDate(var10));
         int var10002;
         if (var10.getConnectionInfo().contains("Encrypted")) {
            var10002 = var5[var9]++;
         } else if (var10.getConnectionInfo().contains("Unencrypted")) {
            var10002 = var6[var9]++;
         } else if (var10.getConnectionInfo().contains("Unknown")) {
            var10002 = var7[var9]++;
         }
      }

      for(var9 = 0; var9 < var5.length; ++var9) {
         var4.add(new BarEntry((float)var9, new float[]{(float)var7[var9], (float)var5[var9], (float)var6[var9]}));
      }

      BarDataSet var11 = new BarDataSet(var4, this.getResources().getString(2131623983));
      var11.setStackLabels(new String[]{this.getResources().getString(2131624075), this.getResources().getString(2131623995), this.getResources().getString(2131624074)});
      var11.setColors(new int[]{ContextCompat.getColor(this.getContext(), 2131099756), ContextCompat.getColor(this.getContext(), 2131099715), ContextCompat.getColor(this.getContext(), 2131099743)});
      final String[] var16 = new String[var5.length];

      for(var9 = 0; var9 < var16.length; ++var9) {
         StringBuilder var14;
         if (var9 == var16.length - 1) {
            var14 = new StringBuilder();
            var14.append(var3);
            var14.append(" .");
            var16[var9] = var14.toString();
         } else {
            var14 = new StringBuilder();
            var14.append("- ");
            var14.append(Integer.toString(29 - var9));
            var14.append(this.getResources().getString(2131623982));
            var16[var9] = var14.toString();
         }
      }

      IAxisValueFormatter var15 = new IAxisValueFormatter() {
         public String getFormattedValue(float var1, AxisBase var2) {
            return var16[(int)var1];
         }
      };
      XAxis var17 = var2.getXAxis();
      var17.setGranularity(1.0F);
      var17.setValueFormatter(var15);
      var17.setPosition(XAxis.XAxisPosition.BOTTOM);
      var2.getAxisLeft().setAxisMinimum(0.0F);
      var2.getAxisRight().setAxisMinimum(0.0F);
      BarData var12 = new BarData(new IBarDataSet[]{var11});
      var12.setBarWidth(0.5F);
      var2.setData(var12);
      var2.setFitBars(true);
      Description var13 = new Description();
      var13.setText("");
      var2.setDescription(var13);
      var2.invalidate();
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

   private Date getEntityDate(ReportEntity var1) {
      String var2 = var1.getTimeStamp();
      SimpleDateFormat var4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      Date var5;
      try {
         var5 = var4.parse(var2);
      } catch (ParseException var3) {
         var3.printStackTrace();
         var5 = null;
      }

      return var5;
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
            String var9 = var4.getTimeStamp();
            SimpleDateFormat var7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            try {
               if (var7.parse(var9).after(dateBefore1month)) {
                  filtered_Entities.add(var4);
                  entitiesString.add(var5);
               }
            } catch (ParseException var8) {
               var8.printStackTrace();
            }
         }
      }
   }

   public int getDaysBetween(Date var1, Date var2) {
      return (int)((var2.getTime() - var1.getTime() + 3600000L) / 86400000L);
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      super.onCreate(var3);
      final View var9 = var1.inflate(2131427389, var2, false);
      PackageManager var10 = this.getActivity().getPackageManager();
      TextView var4 = (TextView)var9.findViewById(2131296361);
      String var8 = this.getArguments().getString("AppName");
      var4.setText(var8);

      try {
         ImageView var5 = (ImageView)var9.findViewById(2131296360);
         String var13 = (String)var10.getApplicationLabel(var10.getApplicationInfo(var8, 128));
         ((TextView)var9.findViewById(2131296362)).setText(var13);
         var5.setImageDrawable(var10.getApplicationIcon(var8));
      } catch (NameNotFoundException var7) {
         var7.printStackTrace();
      }

      SimpleDateFormat var11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      try {
         Date var15 = new Date();
         currentDate = var11.parse(var11.format(var15));
      } catch (ParseException var6) {
         var6.printStackTrace();
      }

      Calendar var12 = Calendar.getInstance();
      var12.setTime(currentDate);
      var12.add(5, -29);
      dateBefore1month = var12.getTime();
      BarChart var14 = (BarChart)var9.findViewById(2131296316);
      this.loadFilteredList(var8);
      this.fillChart(var9, var14);
      this.fillRecyclerList(var9, filtered_Entities);
      var14.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
         public void onNothingSelected() {
            Fragment_month.this.fillRecyclerList(var9, Fragment_month.filtered_Entities);
         }

         public void onValueSelected(Entry var1, Highlight var2) {
            ArrayList var3 = new ArrayList();
            if (var1.getY() != 0.0F) {
               Iterator var4 = Fragment_month.filtered_Entities.iterator();

               while(var4.hasNext()) {
                  ReportEntity var5 = (ReportEntity)var4.next();
                  if ((float)Fragment_month.this.getDaysBetween(Fragment_month.dateBefore1month, Fragment_month.this.getEntityDate(var5)) == var1.getX()) {
                     if (var2.getStackIndex() == 0 && var5.getConnectionInfo().contains("Unknown")) {
                        var3.add(var5);
                     }

                     if (var2.getStackIndex() == 1 && var5.getConnectionInfo().contains("Encrypted")) {
                        var3.add(var5);
                     }

                     if (var2.getStackIndex() == 2 && var5.getConnectionInfo().contains("Unencrypted")) {
                        var3.add(var5);
                     }
                  }
               }

               Fragment_month.this.fillRecyclerList(var9, var3);
            }

         }
      });
      return var9;
   }
}
