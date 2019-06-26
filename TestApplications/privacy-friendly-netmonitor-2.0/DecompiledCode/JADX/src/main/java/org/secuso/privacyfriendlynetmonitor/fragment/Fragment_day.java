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
import android.widget.ScrollView;
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

public class Fragment_day extends Fragment {
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
        ((ScrollView) inflate.findViewById(C0501R.C0499id.scrollViewChart)).setSmoothScrollingEnabled(true);
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
        BarChart barChart = (BarChart) inflate.findViewById(C0501R.C0499id.chart);
        loadFilteredList(string);
        fillChart(inflate, barChart);
        fillRecyclerList(inflate, filtered_Entities);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            public void onValueSelected(Entry entry, Highlight highlight) {
                Date parse;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                try {
                    parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    parse = null;
                }
                int hours = 23 - parse.getHours();
                ArrayList arrayList = new ArrayList();
                if (entry.getY() != 0.0f) {
                    for (ReportEntity reportEntity : Fragment_day.filtered_Entities) {
                        if (((float) ((Fragment_day.this.getEntityHour(reportEntity) + hours) % 24)) == entry.getX()) {
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
                    Fragment_day.this.fillRecyclerList(inflate, arrayList);
                }
            }

            public void onNothingSelected() {
                Fragment_day.this.fillRecyclerList(inflate, Fragment_day.filtered_Entities);
            }
        });
        return inflate;
    }

    private void fillChart(View view, BarChart barChart) {
        Date parse;
        BarChart barChart2 = barChart;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            parse = null;
        }
        int hours = parse.getHours();
        ArrayList arrayList = new ArrayList();
        int[] iArr = new int[24];
        int[] iArr2 = new int[24];
        int[] iArr3 = new int[24];
        Iterator it = filtered_Entities.iterator();
        while (true) {
            int i = 0;
            if (!it.hasNext()) {
                break;
            }
            ReportEntity reportEntity = (ReportEntity) it.next();
            int entityHour = getEntityHour(reportEntity);
            if (entityHour != 24) {
                i = entityHour;
            }
            if (reportEntity.getConnectionInfo().contains(Const.STATUS_TLS)) {
                iArr[i] = iArr[i] + 1;
            } else if (reportEntity.getConnectionInfo().contains(Const.STATUS_UNSECURE)) {
                iArr2[i] = iArr2[i] + 1;
            } else if (reportEntity.getConnectionInfo().contains(Const.STATUS_UNKNOWN)) {
                iArr3[i] = iArr3[i] + 1;
            }
        }
        int i2 = 23 - hours;
        int[] iArr4 = new int[24];
        int[] iArr5 = new int[24];
        int[] iArr6 = new int[24];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            int i4 = (i3 + i2) % 24;
            iArr4[i4] = iArr[i3];
            iArr5[i4] = iArr2[i3];
            iArr6[i4] = iArr3[i3];
        }
        for (int i5 = 0; i5 < iArr4.length; i5++) {
            arrayList.add(new BarEntry((float) i5, new float[]{(float) iArr6[i5], (float) iArr4[i5], (float) iArr5[i5]}));
        }
        BarDataSet barDataSet = new BarDataSet(arrayList, getResources().getString(C0501R.string.hours));
        barDataSet.setStackLabels(new String[]{getResources().getString(C0501R.string.unknown), getResources().getString(C0501R.string.encrypted), getResources().getString(C0501R.string.unencrypted)});
        barDataSet.setColors(ContextCompat.getColor(getContext(), C0501R.color.text_dark), ContextCompat.getColor(getContext(), C0501R.color.green), ContextCompat.getColor(getContext(), C0501R.color.red));
        final String[] strArr = new String[iArr.length];
        for (int i6 = 0; i6 < strArr.length; i6++) {
            StringBuilder stringBuilder;
            if (i6 == strArr.length - 1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(hours);
                stringBuilder.append(getResources().getString(C0501R.string.oclock));
                strArr[i6] = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("- ");
                stringBuilder.append(Integer.toString(23 - i6));
                stringBuilder.append(getResources().getString(C0501R.string.f82hr));
                strArr[i6] = stringBuilder.toString();
            }
        }
        C05552 c05552 = new IAxisValueFormatter() {
            public String getFormattedValue(float f, AxisBase axisBase) {
                return strArr[(int) f];
            }
        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setValueFormatter(c05552);
        xAxis.setPosition(XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setAxisMinimum(0.0f);
        barChart.getAxisRight().setAxisMinimum(0.0f);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barChart2.setData(barData);
        barChart2.setFitBars(true);
        Description description = new Description();
        description.setText("");
        barChart2.setDescription(description);
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        try {
                            Date parse = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                            Calendar instance = Calendar.getInstance();
                            instance.setTime(parse);
                            instance.add(5, -1);
                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(reportEntity.getTimeStamp()).after(instance.getTime())) {
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

    private int getEntityHour(ReportEntity reportEntity) {
        Date parse;
        try {
            parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(reportEntity.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
            parse = null;
        }
        return parse.getHours();
    }
}
