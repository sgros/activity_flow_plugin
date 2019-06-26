package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateReportEntities {
    public static void generateReportEntities(Context context, ReportEntityDao reportEntityDao) {
        System.out.println("Start with entity generation");
        List<String> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.add("com.android.chrome");
        arrayList.add("com.android.vending");
        arrayList.add("com.google.android.youtube");
        int i = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("SELECTEDAPPS", 0);
        Editor edit = sharedPreferences.edit();
        PackageManager packageManager = context.getPackageManager();
        sharedPreferences.getAll().keySet();
        for (String str : arrayList) {
            if (!sharedPreferences.contains(str)) {
                edit.putString(str, str);
                edit.commit();
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        int i2 = 1;
        int i3 = 1;
        int i4 = 0;
        while (i4 < i2) {
            List list;
            List list2;
            PackageManager packageManager2;
            ReportEntityDao reportEntityDao2;
            int i5 = 2018;
            int i6 = i3;
            i3 = 2018;
            while (i3 <= i5) {
                PrintStream printStream = System.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("New year: ");
                stringBuilder.append(i3);
                printStream.println(stringBuilder.toString());
                int i7 = i6;
                i6 = i2;
                while (i6 <= i2) {
                    PrintStream printStream2 = System.out;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("New Month: ");
                    stringBuilder2.append(i6);
                    printStream2.println(stringBuilder2.toString());
                    int i8 = i7;
                    i7 = i2;
                    while (i7 <= 30) {
                        PrintStream printStream3 = System.out;
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("New Day: ");
                        stringBuilder3.append(i7);
                        printStream3.println(stringBuilder3.toString());
                        int i9 = i8;
                        i8 = i;
                        while (i8 < 24) {
                            for (String str2 : arrayList) {
                                PackageInfo packageInfo;
                                String str22;
                                String stringBuilder4;
                                StringBuilder stringBuilder5;
                                String str3;
                                ReportEntity reportEntity = new ReportEntity();
                                try {
                                    packageInfo = packageManager.getPackageInfo(str22, i);
                                } catch (NameNotFoundException e) {
                                    e.printStackTrace();
                                    packageInfo = null;
                                }
                                String str4 = new String();
                                reportEntity.setUserID(String.valueOf(packageInfo.applicationInfo.uid));
                                reportEntity.setAppName(str22);
                                reportEntity.setRemoteAddress(getRandomString());
                                reportEntity.setRemoteHex(getRandomString());
                                reportEntity.setRemoteHost(getRandomString());
                                reportEntity.setLocalAddress(getRandomString());
                                reportEntity.setLocalHex(getRandomString());
                                reportEntity.setServicePort(getRandomString());
                                reportEntity.setPayloadProtocol(getRandomString());
                                reportEntity.setTransportProtocol(getRandomString());
                                reportEntity.setLocalPort(getRandomString());
                                if (Math.random() < 0.5d) {
                                    reportEntity.setConnectionInfo("Encrypted()");
                                } else {
                                    reportEntity.setConnectionInfo("Unencrypted()");
                                }
                                if (i6 < 10) {
                                    StringBuilder stringBuilder6 = new StringBuilder();
                                    stringBuilder6.append("0");
                                    str22 = new String();
                                    stringBuilder6.append(String.valueOf(i6));
                                    stringBuilder4 = stringBuilder6.toString();
                                } else {
                                    stringBuilder4 = new String();
                                    stringBuilder4 = String.valueOf(i6);
                                }
                                if (i7 < 10) {
                                    stringBuilder5 = new StringBuilder();
                                    stringBuilder5.append("0");
                                    str4 = new String();
                                    stringBuilder5.append(String.valueOf(i7));
                                    str4 = stringBuilder5.toString();
                                } else {
                                    str4 = new String();
                                    str4 = String.valueOf(i7);
                                }
                                if (i8 < 10) {
                                    stringBuilder5 = new StringBuilder();
                                    list = list2;
                                    stringBuilder5.append("0");
                                    str3 = new String();
                                    stringBuilder5.append(String.valueOf(i8));
                                    str3 = stringBuilder5.toString();
                                } else {
                                    list = list2;
                                    str3 = new String();
                                    str3 = String.valueOf(i8);
                                }
                                stringBuilder5 = new StringBuilder();
                                packageManager2 = packageManager;
                                String str5 = new String();
                                stringBuilder5.append(String.valueOf(i3));
                                stringBuilder5.append("-");
                                stringBuilder5.append(stringBuilder4);
                                stringBuilder5.append("-");
                                stringBuilder5.append(str4);
                                stringBuilder5.append(" ");
                                stringBuilder5.append(str3);
                                stringBuilder5.append(":04:20.420");
                                reportEntity.setTimeStamp(stringBuilder5.toString());
                                reportEntityDao.insertOrReplace(reportEntity);
                                i9++;
                                list2 = list;
                                packageManager = packageManager2;
                                i = 0;
                            }
                            list = list2;
                            packageManager2 = packageManager;
                            reportEntityDao2 = reportEntityDao;
                            i8++;
                            list2 = list;
                            i = 0;
                        }
                        list = arrayList;
                        packageManager2 = packageManager;
                        reportEntityDao2 = reportEntityDao;
                        i7++;
                        i8 = i9;
                        list2 = list;
                        i = 0;
                    }
                    list = list2;
                    packageManager2 = packageManager;
                    reportEntityDao2 = reportEntityDao;
                    i6++;
                    i7 = i8;
                    list2 = list;
                    i2 = 1;
                    i = 0;
                }
                list = list2;
                packageManager2 = packageManager;
                reportEntityDao2 = reportEntityDao;
                i3++;
                i6 = i7;
                list2 = list;
                i2 = 1;
                i = 0;
                i5 = 2018;
            }
            list = list2;
            packageManager2 = packageManager;
            reportEntityDao2 = reportEntityDao;
            i4++;
            i3 = i6;
            list2 = list;
            i2 = 1;
            i = 0;
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        PrintStream printStream4 = System.out;
        StringBuilder stringBuilder7 = new StringBuilder();
        stringBuilder7.append("Generation needed ");
        stringBuilder7.append((currentTimeMillis2 - currentTimeMillis) / 1000);
        stringBuilder7.append(" seconds, for the generation of ");
        stringBuilder7.append(i3 - 1);
        stringBuilder7.append(" reports");
        printStream4.println(stringBuilder7.toString());
    }

    private static String getRandomString() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < 18) {
            stringBuilder.append(str.charAt((int) (random.nextFloat() * ((float) str.length()))));
        }
        return stringBuilder.toString();
    }
}
