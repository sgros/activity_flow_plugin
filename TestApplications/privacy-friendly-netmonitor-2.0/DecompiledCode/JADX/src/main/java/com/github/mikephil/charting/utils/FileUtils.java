package com.github.mikephil.charting.utils;

import android.os.Environment;
import android.util.Log;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String LOG = "MPChart-FileUtils";

    public static List<Entry> loadEntriesFromFile(String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split("#");
                int i = 0;
                if (split.length <= 2) {
                    arrayList.add(new Entry(Float.parseFloat(split[0]), (float) Integer.parseInt(split[1])));
                } else {
                    float[] fArr = new float[(split.length - 1)];
                    while (i < fArr.length) {
                        fArr[i] = Float.parseFloat(split[i]);
                        i++;
                    }
                    arrayList.add(new BarEntry((float) Integer.parseInt(split[split.length - 1]), fArr));
                }
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x007f A:{SYNTHETIC, Splitter:B:28:0x007f} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0090 A:{SYNTHETIC, Splitter:B:34:0x0090} */
    public static java.util.List<com.github.mikephil.charting.data.Entry> loadEntriesFromAssets(android.content.res.AssetManager r5, java.lang.String r6) {
        /*
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = 0;
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0073 }
        r3 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0073 }
        r5 = r5.open(r6);	 Catch:{ IOException -> 0x0073 }
        r6 = "UTF-8";
        r3.<init>(r5, r6);	 Catch:{ IOException -> 0x0073 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0073 }
        r5 = r2.readLine();	 Catch:{ IOException -> 0x006d, all -> 0x006b }
    L_0x001a:
        if (r5 == 0) goto L_0x0065;
    L_0x001c:
        r6 = "#";
        r5 = r5.split(r6);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r6 = r5.length;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r1 = 2;
        r3 = 0;
        r4 = 1;
        if (r6 > r1) goto L_0x003d;
    L_0x0028:
        r6 = new com.github.mikephil.charting.data.Entry;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r1 = r5[r4];	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r1 = java.lang.Float.parseFloat(r1);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r5 = r5[r3];	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r5 = java.lang.Float.parseFloat(r5);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r6.<init>(r1, r5);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r0.add(r6);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        goto L_0x0060;
    L_0x003d:
        r6 = r5.length;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r6 = r6 - r4;
        r6 = new float[r6];	 Catch:{ IOException -> 0x006d, all -> 0x006b }
    L_0x0041:
        r1 = r6.length;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        if (r3 >= r1) goto L_0x004f;
    L_0x0044:
        r1 = r5[r3];	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r1 = java.lang.Float.parseFloat(r1);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r6[r3] = r1;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r3 = r3 + 1;
        goto L_0x0041;
    L_0x004f:
        r1 = new com.github.mikephil.charting.data.BarEntry;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r3 = r5.length;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r3 = r3 - r4;
        r5 = r5[r3];	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r5 = java.lang.Integer.parseInt(r5);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r5 = (float) r5;	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r1.<init>(r5, r6);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        r0.add(r1);	 Catch:{ IOException -> 0x006d, all -> 0x006b }
    L_0x0060:
        r5 = r2.readLine();	 Catch:{ IOException -> 0x006d, all -> 0x006b }
        goto L_0x001a;
    L_0x0065:
        if (r2 == 0) goto L_0x008d;
    L_0x0067:
        r2.close();	 Catch:{ IOException -> 0x0083 }
        goto L_0x008d;
    L_0x006b:
        r5 = move-exception;
        goto L_0x008e;
    L_0x006d:
        r5 = move-exception;
        r1 = r2;
        goto L_0x0074;
    L_0x0070:
        r5 = move-exception;
        r2 = r1;
        goto L_0x008e;
    L_0x0073:
        r5 = move-exception;
    L_0x0074:
        r6 = "MPChart-FileUtils";
        r5 = r5.toString();	 Catch:{ all -> 0x0070 }
        android.util.Log.e(r6, r5);	 Catch:{ all -> 0x0070 }
        if (r1 == 0) goto L_0x008d;
    L_0x007f:
        r1.close();	 Catch:{ IOException -> 0x0083 }
        goto L_0x008d;
    L_0x0083:
        r5 = move-exception;
        r6 = "MPChart-FileUtils";
        r5 = r5.toString();
        android.util.Log.e(r6, r5);
    L_0x008d:
        return r0;
    L_0x008e:
        if (r2 == 0) goto L_0x009e;
    L_0x0090:
        r2.close();	 Catch:{ IOException -> 0x0094 }
        goto L_0x009e;
    L_0x0094:
        r6 = move-exception;
        r0 = "MPChart-FileUtils";
        r6 = r6.toString();
        android.util.Log.e(r0, r6);
    L_0x009e:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.utils.FileUtils.loadEntriesFromAssets(android.content.res.AssetManager, java.lang.String):java.util.List");
    }

    public static void saveToSdCard(List<Entry> list, String str) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(LOG, e.toString());
            }
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            for (Entry entry : list) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(entry.getY());
                stringBuilder.append("#");
                stringBuilder.append(entry.getX());
                bufferedWriter.append(stringBuilder.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e2) {
            Log.e(LOG, e2.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0057 A:{SYNTHETIC, Splitter:B:20:0x0057} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0068 A:{SYNTHETIC, Splitter:B:26:0x0068} */
    public static java.util.List<com.github.mikephil.charting.data.BarEntry> loadBarEntriesFromAssets(android.content.res.AssetManager r4, java.lang.String r5) {
        /*
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = 0;
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x004b }
        r3 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x004b }
        r4 = r4.open(r5);	 Catch:{ IOException -> 0x004b }
        r5 = "UTF-8";
        r3.<init>(r4, r5);	 Catch:{ IOException -> 0x004b }
        r2.<init>(r3);	 Catch:{ IOException -> 0x004b }
        r4 = r2.readLine();	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
    L_0x001a:
        if (r4 == 0) goto L_0x003d;
    L_0x001c:
        r5 = "#";
        r4 = r4.split(r5);	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r5 = new com.github.mikephil.charting.data.BarEntry;	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r1 = 1;
        r1 = r4[r1];	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r1 = java.lang.Float.parseFloat(r1);	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r3 = 0;
        r4 = r4[r3];	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r4 = java.lang.Float.parseFloat(r4);	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r5.<init>(r1, r4);	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r0.add(r5);	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        r4 = r2.readLine();	 Catch:{ IOException -> 0x0045, all -> 0x0043 }
        goto L_0x001a;
    L_0x003d:
        if (r2 == 0) goto L_0x0065;
    L_0x003f:
        r2.close();	 Catch:{ IOException -> 0x005b }
        goto L_0x0065;
    L_0x0043:
        r4 = move-exception;
        goto L_0x0066;
    L_0x0045:
        r4 = move-exception;
        r1 = r2;
        goto L_0x004c;
    L_0x0048:
        r4 = move-exception;
        r2 = r1;
        goto L_0x0066;
    L_0x004b:
        r4 = move-exception;
    L_0x004c:
        r5 = "MPChart-FileUtils";
        r4 = r4.toString();	 Catch:{ all -> 0x0048 }
        android.util.Log.e(r5, r4);	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x0065;
    L_0x0057:
        r1.close();	 Catch:{ IOException -> 0x005b }
        goto L_0x0065;
    L_0x005b:
        r4 = move-exception;
        r5 = "MPChart-FileUtils";
        r4 = r4.toString();
        android.util.Log.e(r5, r4);
    L_0x0065:
        return r0;
    L_0x0066:
        if (r2 == 0) goto L_0x0076;
    L_0x0068:
        r2.close();	 Catch:{ IOException -> 0x006c }
        goto L_0x0076;
    L_0x006c:
        r5 = move-exception;
        r0 = "MPChart-FileUtils";
        r5 = r5.toString();
        android.util.Log.e(r0, r5);
    L_0x0076:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.utils.FileUtils.loadBarEntriesFromAssets(android.content.res.AssetManager, java.lang.String):java.util.List");
    }
}
