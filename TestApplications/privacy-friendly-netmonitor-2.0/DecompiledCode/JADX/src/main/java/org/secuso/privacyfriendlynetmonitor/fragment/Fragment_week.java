package org.secuso.privacyfriendlynetmonitor.fragment;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.C0359Adapter;
import android.support.p003v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.FragmentDayListAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DBApp;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class Fragment_week extends Fragment {
    static final long ONE_HOUR = 3600000;
    private static Date currentDate;
    private static Date dateBefore1week;
    private static List<String> entitiesString = new ArrayList();
    private static List<ReportEntity> filtered_Entities = new ArrayList();
    private static List<ReportEntity> reportEntities;
    private static ReportEntityDao reportEntityDao;
    private C0359Adapter mAdapter;
    private LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreate(bundle);
        final View inflate = layoutInflater.inflate(C0501R.layout.fragment_charts, viewGroup, false);
        PackageManager packageManager = getActivity().getPackageManager();
        TextView textView = (TextView) inflate.findViewById(C0501R.C0499id.historyGroupSubtitle);
        String string = getArguments().getString("AppName");
        textView.setText(string);
        try {
            ImageView imageView = (ImageView) inflate.findViewById(C0501R.C0499id.historyGroupIcon);
            ((TextView) inflate.findViewById(C0501R.C0499id.historyGroupTitle)).setText((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(string, 128)));
            imageView.setImageDrawable(packageManager.getApplicationIcon(string));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(currentDate);
        instance.add(5, -6);
        dateBefore1week = instance.getTime();
        BarChart barChart = (BarChart) inflate.findViewById(C0501R.C0499id.chart);
        loadFilteredList(string);
        fillChart(inflate, barChart);
        fillRecyclerList(inflate, filtered_Entities);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onValueSelected(Entry entry, Highlight highlight) {
                Fragment_week.currentDate.getDate();
                ArrayList arrayList = new ArrayList();
                if (entry.getY() != 0.0f) {
                    for (ReportEntity reportEntity : Fragment_week.filtered_Entities) {
                        if (((float) Fragment_week.this.getDaysBetween(Fragment_week.dateBefore1week, Fragment_week.this.getEntityDate(reportEntity))) == entry.getX()) {
                            if (highlight.getStackIndex() == 0 && reportEntity.getConnectionInfo().contains(Const.STATUS_UNKNOWN)) {
                                arrayList.add(reportEntity);
                            }
                            if (highlight.getStackIndex() == 1 && reportEntity.getConnectionInfo().contains(Const.STATUS_TLS)) {
                                arrayList.add(reportEntity);
                            }
                            if (highlight.getStackIndex() == 2 && reportEntity.getConnectionInfo().contains(Const.STATUS_UNSECURE)) {
                                arrayList.add(reportEntity);
                            }
                        }
                    }
                    Fragment_week.this.fillRecyclerList(inflate, arrayList);
                }
            }

            public void onNothingSelected() {
                Fragment_week.this.fillRecyclerList(inflate, Fragment_week.filtered_Entities);
            }
        });
        return inflate;
    }

    private void fillChart(View view, BarChart barChart) {
        int date = currentDate.getDate();
        ArrayList arrayList = new ArrayList();
        int[] iArr = new int[7];
        int[] iArr2 = new int[7];
        int[] iArr3 = new int[7];
        for (ReportEntity reportEntity : filtered_Entities) {
            int daysBetween = getDaysBetween(dateBefore1week, getEntityDate(reportEntity));
            if (reportEntity.getConnectionInfo().contains(Const.STATUS_TLS)) {
                iArr[daysBetween] = iArr[daysBetween] + 1;
            } else if (reportEntity.getConnectionInfo().contains(Const.STATUS_UNSECURE)) {
                iArr2[daysBetween] = iArr2[daysBetween] + 1;
            } else if (reportEntity.getConnectionInfo().contains(Const.STATUS_UNKNOWN)) {
                iArr3[daysBetween] = iArr3[daysBetween] + 1;
            }
        }
        for (int i = 0; i < iArr.length; i++) {
            arrayList.add(new BarEntry((float) i, new float[]{(float) iArr3[i], (float) iArr[i], (float) iArr2[i]}));
        }
        BarDataSet barDataSet = new BarDataSet(arrayList, getResources().getString(C0501R.string.days));
        barDataSet.setStackLabels(new String[]{getResources().getString(C0501R.string.unknown), getResources().getString(C0501R.string.encrypted), getResources().getString(C0501R.string.unencrypted)});
        barDataSet.setColors(ContextCompat.getColor(getContext(), C0501R.color.text_dark), ContextCompat.getColor(getContext(), C0501R.color.green), ContextCompat.getColor(getContext(), C0501R.color.red));
        final String[] strArr = new String[iArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            StringBuilder stringBuilder;
            if (i2 == strArr.length - 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(date);
                stringBuilder.append(" .");
                strArr[i2] = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("- ");
                stringBuilder.append(Integer.toString(6 - i2));
                stringBuilder.append(getResources().getString(C0501R.string.f81d));
                strArr[i2] = stringBuilder.toString();
            }
        }
        C05592 c05592 = new IAxisValueFormatter() {
            public String getFormattedValue(float f, AxisBase axisBase) {
                return strArr[(int) f];
            }
        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setValueFormatter(c05592);
        xAxis.setPosition(XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setAxisMinimum(0.0f);
        barChart.getAxisRight().setAxisMinimum(0.0f);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.invalidate();
    }

    private void loadFilteredList(String str) {
        filtered_Entities.clear();
        entitiesString.clear();
        reportEntityDao = ((DBApp) getActivity().getApplication()).getDaoSession().getReportEntityDao();
        reportEntities = reportEntityDao.loadAll();
        Iterator it = reportEntities.iterator();
        while (true) {
            Object obj = null;
            if (it.hasNext()) {
                ReportEntity reportEntity = (ReportEntity) it.next();
                if (reportEntity.getAppName().equals(str)) {
                    String toStringWithoutTimestamp = reportEntity.toStringWithoutTimestamp();
                    for (String equals : entitiesString) {
                        if (equals.equals(toStringWithoutTimestamp)) {
                            obj = 1;
                        }
                    }
                    if (obj == null) {
                        try {
                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(reportEntity.getTimeStamp()).after(dateBefore1week)) {
                                filtered_Entities.add(reportEntity);
                                entitiesString.add(toStringWithoutTimestamp);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private void fillRecyclerList(View view, List<ReportEntity> list) {
        this.mRecyclerView = (RecyclerView) view.findViewById(C0501R.C0499id.my_recycler_view);
        this.mRecyclerView.setFocusable(false);
        this.mRecyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new FragmentDayListAdapter(list, getContext());
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private Date getEntityDate(ReportEntity reportEntity) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(reportEntity.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDaysBetween(Date date, Date date2) {
        return (int) (((date2.getTime() - date.getTime()) + ONE_HOUR) / 86400000);
    }
}
