// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.fragment;

import org.greenrobot.greendao.AbstractDao;
import android.content.pm.PackageManager;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.Calendar;
import android.content.pm.PackageManager$NameNotFoundException;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.FragmentDayListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import java.util.Iterator;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import android.support.v4.content.ContextCompat;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.BarChart;
import android.view.View;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.List;
import java.util.Date;
import android.support.v4.app.Fragment;

public class Fragment_week extends Fragment
{
    static final long ONE_HOUR = 3600000L;
    private static Date currentDate;
    private static Date dateBefore1week;
    private static List<String> entitiesString;
    private static List<ReportEntity> filtered_Entities;
    private static List<ReportEntity> reportEntities;
    private static ReportEntityDao reportEntityDao;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    
    static {
        Fragment_week.filtered_Entities = new ArrayList<ReportEntity>();
        Fragment_week.entitiesString = new ArrayList<String>();
    }
    
    private void fillChart(final View view, final BarChart barChart) {
        final int date = Fragment_week.currentDate.getDate();
        final ArrayList<BarEntry> list = new ArrayList<BarEntry>();
        final int[] array = new int[7];
        final int[] array2 = new int[7];
        final int[] array3 = new int[7];
        for (final ReportEntity reportEntity : Fragment_week.filtered_Entities) {
            final int daysBetween = this.getDaysBetween(Fragment_week.dateBefore1week, this.getEntityDate(reportEntity));
            if (reportEntity.getConnectionInfo().contains("Encrypted")) {
                ++array[daysBetween];
            }
            else if (reportEntity.getConnectionInfo().contains("Unencrypted")) {
                ++array2[daysBetween];
            }
            else {
                if (!reportEntity.getConnectionInfo().contains("Unknown")) {
                    continue;
                }
                ++array3[daysBetween];
            }
        }
        for (int i = 0; i < array.length; ++i) {
            list.add(new BarEntry((float)i, new float[] { (float)array3[i], (float)array[i], (float)array2[i] }));
        }
        final BarDataSet set = new BarDataSet(list, this.getResources().getString(2131623983));
        set.setStackLabels(new String[] { this.getResources().getString(2131624075), this.getResources().getString(2131623995), this.getResources().getString(2131624074) });
        set.setColors(ContextCompat.getColor(this.getContext(), 2131099756), ContextCompat.getColor(this.getContext(), 2131099715), ContextCompat.getColor(this.getContext(), 2131099743));
        final String[] array4 = new String[array.length];
        for (int j = 0; j < array4.length; ++j) {
            if (j == array4.length - 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(date);
                sb.append(" .");
                array4[j] = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("- ");
                sb2.append(Integer.toString(6 - j));
                sb2.append(this.getResources().getString(2131623982));
                array4[j] = sb2.toString();
            }
        }
        final IAxisValueFormatter valueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(final float n, final AxisBase axisBase) {
                return array4[(int)n];
            }
        };
        final XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setValueFormatter(valueFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setAxisMinimum(0.0f);
        barChart.getAxisRight().setAxisMinimum(0.0f);
        final BarData data = new BarData(new IBarDataSet[] { set });
        data.setBarWidth(0.5f);
        barChart.setData(data);
        barChart.setFitBars(true);
        final Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.invalidate();
    }
    
    private void fillRecyclerList(final View view, final List<ReportEntity> list) {
        (this.mRecyclerView = (RecyclerView)view.findViewById(2131296403)).setFocusable(false);
        this.mRecyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this.getContext());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new FragmentDayListAdapter(list, this.getContext());
        this.mRecyclerView.setAdapter(this.mAdapter);
    }
    
    private Date getEntityDate(final ReportEntity reportEntity) {
        final String timeStamp = reportEntity.getTimeStamp();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date parse;
        try {
            parse = simpleDateFormat.parse(timeStamp);
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            parse = null;
        }
        return parse;
    }
    
    private void loadFilteredList(final String anObject) {
        Fragment_week.filtered_Entities.clear();
        Fragment_week.entitiesString.clear();
        Fragment_week.reportEntityDao = ((DBApp)this.getActivity().getApplication()).getDaoSession().getReportEntityDao();
        Fragment_week.reportEntities = ((AbstractDao<ReportEntity, K>)Fragment_week.reportEntityDao).loadAll();
        final Iterator<ReportEntity> iterator = Fragment_week.reportEntities.iterator();
        while (true) {
            boolean b = false;
            if (!iterator.hasNext()) {
                break;
            }
            final ReportEntity reportEntity = iterator.next();
            if (!reportEntity.getAppName().equals(anObject)) {
                continue;
            }
            final String stringWithoutTimestamp = reportEntity.toStringWithoutTimestamp();
            final Iterator<String> iterator2 = Fragment_week.entitiesString.iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().equals(stringWithoutTimestamp)) {
                    b = true;
                }
            }
            if (b) {
                continue;
            }
            final String timeStamp = reportEntity.getTimeStamp();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                if (!simpleDateFormat.parse(timeStamp).after(Fragment_week.dateBefore1week)) {
                    continue;
                }
                Fragment_week.filtered_Entities.add(reportEntity);
                Fragment_week.entitiesString.add(stringWithoutTimestamp);
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public int getDaysBetween(final Date date, final Date date2) {
        return (int)((date2.getTime() - date.getTime() + 3600000L) / 86400000L);
    }
    
    @Override
    public View onCreateView(LayoutInflater string, ViewGroup inflate, final Bundle bundle) {
        super.onCreate(bundle);
        inflate = (ViewGroup)string.inflate(2131427389, inflate, false);
        final PackageManager packageManager = this.getActivity().getPackageManager();
        final TextView textView = (TextView)((View)inflate).findViewById(2131296361);
        string = (LayoutInflater)this.getArguments().getString("AppName");
        textView.setText((CharSequence)string);
        try {
            final ImageView imageView = (ImageView)((View)inflate).findViewById(2131296360);
            ((TextView)((View)inflate).findViewById(2131296362)).setText((CharSequence)packageManager.getApplicationLabel(packageManager.getApplicationInfo((String)string, 128)));
            imageView.setImageDrawable(packageManager.getApplicationIcon((String)string));
        }
        catch (PackageManager$NameNotFoundException ex) {
            ex.printStackTrace();
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Fragment_week.currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        }
        catch (ParseException ex2) {
            ex2.printStackTrace();
        }
        final Calendar instance = Calendar.getInstance();
        instance.setTime(Fragment_week.currentDate);
        instance.add(5, -6);
        Fragment_week.dateBefore1week = instance.getTime();
        final BarChart barChart = (BarChart)((View)inflate).findViewById(2131296316);
        this.loadFilteredList((String)string);
        this.fillChart((View)inflate, barChart);
        this.fillRecyclerList((View)inflate, Fragment_week.filtered_Entities);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onNothingSelected() {
                Fragment_week.this.fillRecyclerList(inflate, Fragment_week.filtered_Entities);
            }
            
            @Override
            public void onValueSelected(final Entry entry, final Highlight highlight) {
                Fragment_week.currentDate.getDate();
                final ArrayList<ReportEntity> list = new ArrayList<ReportEntity>();
                if (entry.getY() != 0.0f) {
                    for (final ReportEntity reportEntity : Fragment_week.filtered_Entities) {
                        if (Fragment_week.this.getDaysBetween(Fragment_week.dateBefore1week, Fragment_week.this.getEntityDate(reportEntity)) == entry.getX()) {
                            if (highlight.getStackIndex() == 0 && reportEntity.getConnectionInfo().contains("Unknown")) {
                                list.add(reportEntity);
                            }
                            if (highlight.getStackIndex() == 1 && reportEntity.getConnectionInfo().contains("Encrypted")) {
                                list.add(reportEntity);
                            }
                            if (highlight.getStackIndex() != 2 || !reportEntity.getConnectionInfo().contains("Unencrypted")) {
                                continue;
                            }
                            list.add(reportEntity);
                        }
                    }
                    Fragment_week.this.fillRecyclerList(inflate, list);
                }
            }
        });
        return (View)inflate;
    }
}
