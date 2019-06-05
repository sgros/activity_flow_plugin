// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import org.greenrobot.greendao.AbstractDao;
import java.util.Random;
import android.content.pm.PackageInfo;
import java.io.PrintStream;
import java.util.Iterator;
import android.content.pm.PackageManager;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import java.util.List;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.ArrayList;
import android.content.Context;

public class GenerateReportEntities
{
    public static void generateReportEntities(Context context, final ReportEntityDao reportEntityDao) {
        System.out.println("Start with entity generation");
        final List<String> list = new ArrayList<String>();
        new ArrayList();
        list.add("com.android.chrome");
        list.add("com.android.vending");
        list.add("com.google.android.youtube");
        final SharedPreferences sharedPreferences = context.getSharedPreferences("SELECTEDAPPS", 0);
        final SharedPreferences$Editor edit = sharedPreferences.edit();
        final PackageManager packageManager = context.getPackageManager();
        sharedPreferences.getAll().keySet();
        for (final String s : list) {
            if (!sharedPreferences.contains(s)) {
                edit.putString(s, s);
                edit.commit();
            }
        }
        final long currentTimeMillis = System.currentTimeMillis();
        int n = 1;
        int i = 0;
        context = (Context)list;
        while (i < 1) {
            for (int j = 2018; j <= 2018; ++j) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("New year: ");
                sb.append(j);
                out.println(sb.toString());
                for (int k = 1; k <= 1; ++k) {
                    final PrintStream out2 = System.out;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("New Month: ");
                    sb2.append(k);
                    out2.println(sb2.toString());
                    for (int l = 1; l <= 30; ++l) {
                        final PrintStream out3 = System.out;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("New Day: ");
                        sb3.append(l);
                        out3.println(sb3.toString());
                        for (int n2 = 0; n2 < 24; ++n2) {
                            for (final String appName : context) {
                                final ReportEntity reportEntity = new ReportEntity();
                                PackageInfo packageInfo;
                                try {
                                    packageInfo = packageManager.getPackageInfo(appName, 0);
                                }
                                catch (PackageManager$NameNotFoundException ex) {
                                    ex.printStackTrace();
                                    packageInfo = null;
                                }
                                new String();
                                reportEntity.setUserID(String.valueOf(packageInfo.applicationInfo.uid));
                                reportEntity.setAppName(appName);
                                reportEntity.setRemoteAddress(getRandomString());
                                reportEntity.setRemoteHex(getRandomString());
                                reportEntity.setRemoteHost(getRandomString());
                                reportEntity.setLocalAddress(getRandomString());
                                reportEntity.setLocalHex(getRandomString());
                                reportEntity.setServicePort(getRandomString());
                                reportEntity.setPayloadProtocol(getRandomString());
                                reportEntity.setTransportProtocol(getRandomString());
                                reportEntity.setLocalPort(getRandomString());
                                if (Math.random() < 0.5) {
                                    reportEntity.setConnectionInfo("Encrypted()");
                                }
                                else {
                                    reportEntity.setConnectionInfo("Unencrypted()");
                                }
                                String str;
                                if (k < 10) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("0");
                                    new String();
                                    sb4.append(String.valueOf(k));
                                    str = sb4.toString();
                                }
                                else {
                                    new String();
                                    str = String.valueOf(k);
                                }
                                String str2;
                                if (l < 10) {
                                    final StringBuilder sb5 = new StringBuilder();
                                    sb5.append("0");
                                    new String();
                                    sb5.append(String.valueOf(l));
                                    str2 = sb5.toString();
                                }
                                else {
                                    new String();
                                    str2 = String.valueOf(l);
                                }
                                String str3;
                                if (n2 < 10) {
                                    final StringBuilder sb6 = new StringBuilder();
                                    sb6.append("0");
                                    new String();
                                    sb6.append(String.valueOf(n2));
                                    str3 = sb6.toString();
                                }
                                else {
                                    new String();
                                    str3 = String.valueOf(n2);
                                }
                                final StringBuilder sb7 = new StringBuilder();
                                new String();
                                sb7.append(String.valueOf(j));
                                sb7.append("-");
                                sb7.append(str);
                                sb7.append("-");
                                sb7.append(str2);
                                sb7.append(" ");
                                sb7.append(str3);
                                sb7.append(":04:20.420");
                                reportEntity.setTimeStamp(sb7.toString());
                                ((AbstractDao<ReportEntity, K>)reportEntityDao).insertOrReplace(reportEntity);
                                ++n;
                            }
                        }
                    }
                }
            }
            ++i;
        }
        final long currentTimeMillis2 = System.currentTimeMillis();
        final PrintStream out4 = System.out;
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("Generation needed ");
        sb8.append((currentTimeMillis2 - currentTimeMillis) / 1000L);
        sb8.append(" seconds, for the generation of ");
        sb8.append(n - 1);
        sb8.append(" reports");
        out4.println(sb8.toString());
    }
    
    private static String getRandomString() {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        while (sb.length() < 18) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt((int)(random.nextFloat() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length())));
        }
        return sb.toString();
    }
}
