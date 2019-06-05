// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.fragment;

import org.greenrobot.greendao.AbstractDao;
import android.content.pm.PackageManager;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import android.content.pm.PackageManager$NameNotFoundException;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ScrollView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Calendar;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
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
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.github.mikephil.charting.charts.BarChart;
import android.view.View;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.List;
import android.support.v4.app.Fragment;

public class Fragment_day extends Fragment
{
    private static List<String> entitiesString;
    private static List<ReportEntity> filtered_Entities;
    private static List<ReportEntity> reportEntities;
    private static ReportEntityDao reportEntityDao;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    
    static {
        Fragment_day.filtered_Entities = new ArrayList<ReportEntity>();
        Fragment_day.entitiesString = new ArrayList<String>();
    }
    
    private void fillChart(final View view, final BarChart barChart) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date parse;
        try {
            parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            parse = null;
        }
        final int hours = parse.getHours();
        final ArrayList<BarEntry> list = new ArrayList<BarEntry>();
        final int[] array = new int[24];
        final int[] array2 = new int[24];
        final int[] array3 = new int[24];
        final Iterator<ReportEntity> iterator = Fragment_day.filtered_Entities.iterator();
        while (true) {
            final boolean hasNext = iterator.hasNext();
            int n = 0;
            if (!hasNext) {
                break;
            }
            final ReportEntity reportEntity = iterator.next();
            final int entityHour = this.getEntityHour(reportEntity);
            if (entityHour != 24) {
                n = entityHour;
            }
            if (reportEntity.getConnectionInfo().contains("Encrypted")) {
                ++array[n];
            }
            else if (reportEntity.getConnectionInfo().contains("Unencrypted")) {
                ++array2[n];
            }
            else {
                if (!reportEntity.getConnectionInfo().contains("Unknown")) {
                    continue;
                }
                ++array3[n];
            }
        }
        final int[] array4 = new int[24];
        final int[] array5 = new int[24];
        final int[] array6 = new int[24];
        for (int i = 0; i < array.length; ++i) {
            final int n2 = (i + (23 - hours)) % 24;
            array4[n2] = array[i];
            array5[n2] = array2[i];
            array6[n2] = array3[i];
        }
        for (int j = 0; j < array4.length; ++j) {
            list.add(new BarEntry((float)j, new float[] { (float)array6[j], (float)array4[j], (float)array5[j] }));
        }
        final BarDataSet set = new BarDataSet(list, this.getResources().getString(2131624023));
        set.setStackLabels(new String[] { this.getResources().getString(2131624075), this.getResources().getString(2131623995), this.getResources().getString(2131624074) });
        set.setColors(ContextCompat.getColor(this.getContext(), 2131099756), ContextCompat.getColor(this.getContext(), 2131099715), ContextCompat.getColor(this.getContext(), 2131099743));
        final String[] array7 = new String[array.length];
        for (int k = 0; k < array7.length; ++k) {
            if (k == array7.length - 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(hours);
                sb.append(this.getResources().getString(2131624034));
                array7[k] = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("- ");
                sb2.append(Integer.toString(23 - k));
                sb2.append(this.getResources().getString(2131624024));
                array7[k] = sb2.toString();
            }
        }
        final IAxisValueFormatter valueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(final float n, final AxisBase axisBase) {
                return array7[(int)n];
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
    
    private int getEntityHour(final ReportEntity reportEntity) {
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
        return parse.getHours();
    }
    
    private void loadFilteredList(final String anObject) {
        Fragment_day.filtered_Entities.clear();
        Fragment_day.entitiesString.clear();
        Fragment_day.reportEntityDao = ((DBApp)this.getActivity().getApplication()).getDaoSession().getReportEntityDao();
        Fragment_day.reportEntities = ((AbstractDao<ReportEntity, K>)Fragment_day.reportEntityDao).loadAll();
        final Iterator<ReportEntity> iterator = Fragment_day.reportEntities.iterator();
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
            final Iterator<String> iterator2 = Fragment_day.entitiesString.iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().equals(stringWithoutTimestamp)) {
                    b = true;
                }
            }
            if (b) {
                continue;
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                final Date parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                final Calendar instance = Calendar.getInstance();
                instance.setTime(parse);
                instance.add(5, -1);
                if (!new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(reportEntity.getTimeStamp()).after(instance.getTime())) {
                    continue;
                }
                Fragment_day.filtered_Entities.add(reportEntity);
                Fragment_day.entitiesString.add(stringWithoutTimestamp);
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater string, ViewGroup inflate, final Bundle bundle) {
        super.onCreate(bundle);
        inflate = (ViewGroup)string.inflate(2131427389, inflate, false);
        ((ScrollView)((View)inflate).findViewById(2131296464)).setSmoothScrollingEnabled(true);
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
        final BarChart barChart = (BarChart)((View)inflate).findViewById(2131296316);
        this.loadFilteredList((String)string);
        this.fillChart((View)inflate, barChart);
        this.fillRecyclerList((View)inflate, Fragment_day.filtered_Entities);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onNothingSelected() {
                Fragment_day.this.fillRecyclerList(inflate, Fragment_day.filtered_Entities);
            }
            
            @Override
            public void onValueSelected(final Entry entry, final Highlight highlight) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date parse;
                try {
                    parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                }
                catch (ParseException ex) {
                    ex.printStackTrace();
                    parse = null;
                }
                final int hours = parse.getHours();
                final ArrayList<ReportEntity> list = new ArrayList<ReportEntity>();
                if (entry.getY() != 0.0f) {
                    for (final ReportEntity reportEntity : Fragment_day.filtered_Entities) {
                        if ((Fragment_day.this.getEntityHour(reportEntity) + (23 - hours)) % 24 == entry.getX()) {
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
                    Fragment_day.this.fillRecyclerList(inflate, list);
                }
            }
        });
        return (View)inflate;
    }
}
