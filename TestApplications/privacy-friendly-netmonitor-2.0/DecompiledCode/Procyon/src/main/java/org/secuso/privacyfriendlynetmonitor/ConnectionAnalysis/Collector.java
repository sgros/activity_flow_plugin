// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import org.greenrobot.greendao.AbstractDao;
import android.preference.PreferenceManager;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncDNS;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncCertVal;
import java.util.LinkedHashMap;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import java.util.LinkedHashSet;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;
import java.util.Collection;
import android.content.Context;
import android.annotation.TargetApi;
import android.util.Log;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Build$VERSION;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import java.util.Date;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Collector
{
    private static List<String> appsToExcludeFromScan;
    private static List<String> appsToIncludeInScan;
    public static Boolean isCertVal;
    private static HashMap<String, String> knownUIDs;
    public static HashMap<String, Map<String, Object>> mCertValMap;
    private static HashMap<Integer, List<Report>> mFilteredUidReportMap;
    private static HashMap<Integer, List<Report>> mUidReportMap;
    private static HashMap<String, String> sCacheDNS;
    private static HashMap<Integer, Drawable> sCacheIcon;
    private static HashMap<Integer, String> sCacheLabel;
    private static HashMap<Integer, List<PackageInfo>> sCachePackage;
    public static List<String> sCertValList;
    public static Report sDetailReport;
    public static ArrayList<String[]> sDetailReportList;
    private static ArrayList<Report> sReportList;
    
    static {
        Collector.sCachePackage = new HashMap<Integer, List<PackageInfo>>();
        Collector.sCacheIcon = new HashMap<Integer, Drawable>();
        Collector.sCacheLabel = new HashMap<Integer, String>();
        Collector.sCacheDNS = new HashMap<String, String>();
        Collector.isCertVal = false;
        Collector.mCertValMap = new HashMap<String, Map<String, Object>>();
        Collector.sCertValList = new ArrayList<String>();
        Collector.sDetailReportList = new ArrayList<String[]>();
        Collector.sReportList = new ArrayList<Report>();
        Collector.mUidReportMap = new HashMap<Integer, List<Report>>();
        Collector.mFilteredUidReportMap = new HashMap<Integer, List<Report>>();
        Collector.appsToIncludeInScan = new ArrayList<String>();
        Collector.appsToExcludeFromScan = new ArrayList<String>();
        Collector.knownUIDs = new HashMap<String, String>();
    }
    
    public static void addAppToExcludeFromScan(final String s) {
        Collector.appsToExcludeFromScan.add(s);
    }
    
    public static void addAppToIncludeInScan(final String s) {
        Collector.appsToIncludeInScan.add(s);
    }
    
    public static void addKnownUIDs(final String key, final String value) {
        Collector.knownUIDs.put(key, value);
    }
    
    private static void addSysPackage() {
        final PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = "com.android.system";
        packageInfo.versionCode = 8;
        packageInfo.versionName = "2.0";
        packageInfo.applicationInfo = new ApplicationInfo();
        packageInfo.applicationInfo.name = "System";
        packageInfo.applicationInfo.uid = 0;
        packageInfo.applicationInfo.icon = 0;
        if (!Collector.sCachePackage.containsKey(packageInfo.applicationInfo.uid)) {
            Collector.sCachePackage.put(packageInfo.applicationInfo.uid, new ArrayList<PackageInfo>());
        }
        Collector.sCachePackage.get(packageInfo.applicationInfo.uid).add(packageInfo);
    }
    
    public static boolean analyseReady(final Map<String, Object> map) {
        final String s = map.get("status");
        return s != null && s.equals("READY");
    }
    
    private static void buildDetailStrings(final ArrayList<Report> list) {
        final ArrayList<String[]> sDetailReportList = new ArrayList<String[]>();
        final Report report = list.get(0);
        try {
            final PackageInfo packageInfo = Collector.sCachePackage.get(report.uid).get(0);
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(report.uid);
            sDetailReportList.add(new String[] { "User ID", sb.toString() });
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(packageInfo.versionName);
            sDetailReportList.add(new String[] { "App Version", sb2.toString() });
            if (report.uid > 10000) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("");
                sb3.append(new Date(packageInfo.firstInstallTime).toString());
                sDetailReportList.add(new String[] { "Installed On", sb3.toString() });
            }
            else {
                sDetailReportList.add(new String[] { "Installed On", "System App" });
            }
            sDetailReportList.add(new String[] { "", "" });
            if (report.type != TLType.tcp6 && report.type != TLType.udp6) {
                sDetailReportList.add(new String[] { "Remote Address", report.remoteAdd.getHostAddress() });
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(report.remoteAdd.getHostAddress());
                sb4.append("\n(IPv6 translated)");
                sDetailReportList.add(new String[] { "Remote Address", sb4.toString() });
            }
            sDetailReportList.add(new String[] { "Remote HEX", ToolBox.printHexBinary(report.remoteAddHex) });
            if (hasHostName(report.remoteAdd.getHostAddress())) {
                sDetailReportList.add(new String[] { "Remote Host", getDnsHostName(report.remoteAdd.getHostAddress()) });
            }
            else {
                sDetailReportList.add(new String[] { "Remote Host", "name not resolved" });
            }
            if (report.type != TLType.tcp6 && report.type != TLType.udp6) {
                sDetailReportList.add(new String[] { "Local Address", report.localAdd.getHostAddress() });
            }
            else {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(report.localAdd.getHostAddress());
                sb5.append("\n(IPv6 translated)");
                sDetailReportList.add(new String[] { "Local Address", sb5.toString() });
            }
            sDetailReportList.add(new String[] { "Local HEX", ToolBox.printHexBinary(report.localAddHex) });
            sDetailReportList.add(new String[] { "", "" });
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("");
            sb6.append(report.remotePort);
            sDetailReportList.add(new String[] { "Service Port", sb6.toString() });
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("");
            sb7.append(KnownPorts.resolvePort(report.remotePort));
            sDetailReportList.add(new String[] { "Payload Protocol", sb7.toString() });
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("");
            sb8.append(report.type);
            sDetailReportList.add(new String[] { "Transport Protocol", sb8.toString() });
            sDetailReportList.add(new String[] { "Last Seen", report.timestamp.toString() });
            sDetailReportList.add(new String[] { "", "" });
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("");
            sb9.append(list.size());
            sDetailReportList.add(new String[] { "Simultaneous Connections", sb9.toString() });
            int n = 0;
            while (true) {
                Label_0979: {
                    if (n >= list.size()) {
                        break Label_0979;
                    }
                    final Report report2 = list.get(n);
                    final StringBuilder sb10 = new StringBuilder();
                    sb10.append("(");
                    ++n;
                    try {
                        sb10.append(n);
                        sb10.append(")src port > dst port");
                        final String string = sb10.toString();
                        final StringBuilder sb11 = new StringBuilder();
                        sb11.append(report2.localPort);
                        sb11.append(" > ");
                        sb11.append(report2.remotePort);
                        sDetailReportList.add(new String[] { string, sb11.toString() });
                        sDetailReportList.add(new String[] { "    last socket-state ", getTransportState(report.state) });
                        continue;
                        sDetailReportList.add(new String[] { "", "" });
                        Collector.sDetailReportList = sDetailReportList;
                    }
                    catch (NullPointerException ex) {}
                }
            }
        }
        catch (NullPointerException ex2) {}
    }
    
    private static ArrayList<Report> deepCopyReportList(final ArrayList<Report> list) {
        final ArrayList<Report> list2 = new ArrayList<Report>();
        int i = 0;
        try {
            while (i < list.size()) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(list.get(i));
                objectOutputStream.flush();
                list2.add(Report.class.cast(new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject()));
                ++i;
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            final Throwable t;
            t.printStackTrace();
        }
        return list2;
    }
    
    public static void deleteAppFromIncludeInScan(final String s) {
        Collector.appsToIncludeInScan.remove(s);
    }
    
    public static void deleteAppToExcludeFromScan(final String s) {
        Collector.appsToExcludeFromScan.remove(s);
    }
    
    private static void fillCertRequests() {
        final Iterator<Integer> iterator = Collector.mFilteredUidReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            final ArrayList<Report> list = Collector.mFilteredUidReportMap.get(iterator.next());
            for (int i = 0; i < list.size(); ++i) {
                final Report report = list.get(i);
                final String hostAddress = report.remoteAdd.getHostAddress();
                if (KnownPorts.isTlsPort(report.remotePort) && hasHostName(hostAddress) && !Collector.mCertValMap.containsKey(getDnsHostName(hostAddress)) && !Collector.sCertValList.contains(getDnsHostName(hostAddress))) {
                    Collector.sCertValList.add(getDnsHostName(hostAddress));
                }
            }
        }
    }
    
    private static void fillPackageInformation() {
        for (int i = 0; i < Collector.sReportList.size(); ++i) {
            final Report report = Collector.sReportList.get(i);
            if (!Collector.sCachePackage.containsKey(report.uid)) {
                updatePackageCache();
            }
            if (Collector.sCachePackage.containsKey(report.uid) && Collector.sCachePackage.get(report.uid).size() == 1) {
                final PackageInfo packageInfo = Collector.sCachePackage.get(report.uid).get(0);
                report.appName = packageInfo.applicationInfo.name;
                report.packageName = packageInfo.packageName;
            }
            else if (Collector.sCachePackage.containsKey(report.uid) && Collector.sCachePackage.get(report.uid).size() > 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("UID ");
                sb.append(report.uid);
                report.appName = sb.toString();
                report.appName = "app.unknown";
            }
            else {
                report.appName = "Unknown App";
                report.appName = "app.unknown";
            }
        }
    }
    
    private static HashMap<Integer, List<Report>> filterReports() {
        final HashMap<Integer, ArrayList<Report>> hashMap = (HashMap<Integer, ArrayList<Report>>)new HashMap<Integer, List<Report>>();
        final HashSet<String> set = new HashSet<String>();
        for (final int intValue : Collector.mUidReportMap.keySet()) {
            hashMap.put(intValue, new ArrayList<Report>());
            final ArrayList<Report> list = Collector.mUidReportMap.get(intValue);
            final ArrayList<Report> list2 = hashMap.get(intValue);
            set.clear();
            for (int i = 0; i < list.size(); ++i) {
                final String hostAddress = list.get(i).remoteAdd.getHostAddress();
                if (!set.contains(hostAddress)) {
                    list2.add(list.get(i));
                    set.add(hostAddress);
                }
            }
        }
        return (HashMap<Integer, List<Report>>)hashMap;
    }
    
    private static ArrayList<Report> filterReportsByAdd(int i, final byte[] a2) {
        final List<Report> list = Collector.mUidReportMap.get(i);
        final ArrayList<Report> list2 = new ArrayList<Report>();
        for (i = 0; i < list.size(); ++i) {
            if (Arrays.equals(list.get(i).remoteAddHex, a2)) {
                list2.add(list.get(i));
            }
        }
        return list2;
    }
    
    public static List<String> getAppsToExcludeFromScan() {
        return Collector.appsToExcludeFromScan;
    }
    
    public static List<String> getAppsToIncludeInScan() {
        return Collector.appsToIncludeInScan;
    }
    
    public static String getCertHost(final String s) {
        if (Collector.mCertValMap.containsKey(s)) {
            final Map<String, Object> map = Collector.mCertValMap.get(s);
            if (analyseReady(map)) {
                if (map.containsKey("host")) {
                    return (String)map.get("host");
                }
                return s;
            }
        }
        return s;
    }
    
    private static Drawable getDefaultIcon() {
        if (Build$VERSION.SDK_INT >= 21) {
            return getIconNew(17301651);
        }
        return getIconOld(17301651);
    }
    
    public static String getDnsHostName(final String s) {
        if (Collector.sCacheDNS.containsKey(s)) {
            return Collector.sCacheDNS.get(s);
        }
        return s;
    }
    
    public static Drawable getIcon(final int i) {
        try {
            if (!Collector.sCacheIcon.containsKey(i)) {
                if (!Collector.sCachePackage.containsKey(i) || Collector.sCachePackage.get(i).size() != 1) {
                    return getDefaultIcon();
                }
                Collector.sCacheIcon.put(i, Collector.sCachePackage.get(i).get(0).applicationInfo.loadIcon(RunStore.getContext().getPackageManager()));
            }
            return Collector.sCacheIcon.get(i);
        }
        catch (NullPointerException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not load icon of: ");
            sb.append(Collector.sCachePackage.get(i).get(0).packageName);
            Log.e("NetMonitor", sb.toString());
            return getDefaultIcon();
        }
    }
    
    @TargetApi(21)
    private static Drawable getIconNew(final int n) {
        return RunStore.getContext().getDrawable(n);
    }
    
    @TargetApi(9)
    private static Drawable getIconOld(final int n) {
        return RunStore.getContext().getResources().getDrawable(n);
    }
    
    public static HashMap<String, String> getKnownUIDs() {
        return Collector.knownUIDs;
    }
    
    public static String getLabel(final int i) {
        if (!Collector.sCacheLabel.containsKey(i)) {
            if (Collector.sCachePackage.containsKey(i) && Collector.sCachePackage.get(i).size() == 1) {
                Collector.sCacheLabel.put(i, (String)Collector.sCachePackage.get(i).get(0).applicationInfo.loadLabel(RunStore.getContext().getPackageManager()));
            }
            else {
                if (Collector.sCachePackage.containsKey(i) && Collector.sCachePackage.get(i).size() > 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("UID ");
                    sb.append(i);
                    return sb.toString();
                }
                return RunStore.getContext().getString(2131624076);
            }
        }
        return Collector.sCacheLabel.get(i);
    }
    
    public static String getMetric(final String s) {
        if (!Collector.mCertValMap.containsKey(s)) {
            return "PENDING";
        }
        final Map<String, Object> map = Collector.mCertValMap.get(s);
        if (!analyseReady(map)) {
            return "PENDING";
        }
        final String endpoints = readEndpoints(map);
        if (endpoints.equals("no_grade")) {
            return "no_grade";
        }
        if (endpoints.equals("Certificate not valid for domain name")) {
            handleInvalidDomainName(map);
            return "RESOLVING CERTIFICATE HOSTS";
        }
        if (endpoints.equals("no_endpoints")) {
            return "no_endpoints";
        }
        return endpoints;
    }
    
    public static String getPackage(final int i) {
        if (Collector.sCachePackage.containsKey(i) && Collector.sCachePackage.get(i).size() == 1) {
            return Collector.sCachePackage.get(i).get(0).packageName;
        }
        return RunStore.getContext().getString(2131624077);
    }
    
    private static List<PackageInfo> getPackages(final Context context) {
        synchronized (context.getApplicationContext()) {
            return new ArrayList<PackageInfo>(context.getPackageManager().getInstalledPackages(0));
        }
    }
    
    private static String getTransportState(final byte[] array) {
        final String printHexBinary = ToolBox.printHexBinary(array);
        final int hashCode = printHexBinary.hashCode();
        int n = 0;
        Label_0279: {
            Label_0277: {
                switch (hashCode) {
                    default: {
                        switch (hashCode) {
                            default: {
                                break Label_0277;
                            }
                            case 1555: {
                                if (printHexBinary.equals("0C")) {
                                    n = 11;
                                    break Label_0279;
                                }
                                break Label_0277;
                            }
                            case 1554: {
                                if (printHexBinary.equals("0B")) {
                                    n = 10;
                                    break Label_0279;
                                }
                                break Label_0277;
                            }
                            case 1553: {
                                if (printHexBinary.equals("0A")) {
                                    n = 9;
                                    break Label_0279;
                                }
                                break Label_0277;
                            }
                        }
                        break;
                    }
                    case 1545: {
                        if (printHexBinary.equals("09")) {
                            n = 8;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1544: {
                        if (printHexBinary.equals("08")) {
                            n = 7;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1543: {
                        if (printHexBinary.equals("07")) {
                            n = 6;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1542: {
                        if (printHexBinary.equals("06")) {
                            n = 5;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1541: {
                        if (printHexBinary.equals("05")) {
                            n = 4;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1540: {
                        if (printHexBinary.equals("04")) {
                            n = 3;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1539: {
                        if (printHexBinary.equals("03")) {
                            n = 2;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1538: {
                        if (printHexBinary.equals("02")) {
                            n = 1;
                            break Label_0279;
                        }
                        break;
                    }
                    case 1537: {
                        if (printHexBinary.equals("01")) {
                            n = 0;
                            break Label_0279;
                        }
                        break;
                    }
                }
            }
            n = -1;
        }
        String s = null;
        switch (n) {
            default: {
                s = "UNKNOWN";
                break;
            }
            case 11: {
                s = "NEW_SYN_RECV";
                break;
            }
            case 10: {
                s = "CLOSING";
                break;
            }
            case 9: {
                s = "LISTEN";
                break;
            }
            case 8: {
                s = "LAST_ACK";
                break;
            }
            case 7: {
                s = "CLOSE_WAIT";
                break;
            }
            case 6: {
                s = "CLOSE";
                break;
            }
            case 5: {
                s = "TIME_WAIT";
                break;
            }
            case 4: {
                s = "FIN_WAIT2";
                break;
            }
            case 3: {
                s = "FIN_WAIT1";
                break;
            }
            case 2: {
                s = "SYN_RECV";
                break;
            }
            case 1: {
                s = "SYN_SENT";
                break;
            }
            case 0: {
                s = "ESTABLISHED";
                break;
            }
        }
        return s;
    }
    
    private static void handleInvalidDomainName(final Map<String, Object> map) {
        if (map.containsKey("certHostnames") && map.containsKey("host")) {
            final ArrayList<String> list = map.get("certHostnames");
            final String s = (String)map.get("host");
            final String replace = list.get(0).replace("*.", "");
            if (Collector.mCertValMap.containsKey(replace) && Collector.mCertValMap.containsKey(s)) {
                Collector.mCertValMap.put(s, Collector.mCertValMap.get(replace));
                if (Collector.sCertValList.contains(s)) {
                    Collector.sCertValList.remove(s);
                }
            }
            else if (!Collector.sCertValList.contains(replace)) {
                Collector.sCertValList.add(replace);
            }
        }
    }
    
    public static boolean hasGrade(String metric) {
        metric = getMetric(metric);
        final int hashCode = metric.hashCode();
        int n = 0;
        Label_0059: {
            if (hashCode != 35394935) {
                if (hashCode == 50017003) {
                    if (metric.equals("RESOLVING CERTIFICATE HOSTS")) {
                        n = 0;
                        break Label_0059;
                    }
                }
            }
            else if (metric.equals("PENDING")) {
                n = 1;
                break Label_0059;
            }
            n = -1;
        }
        switch (n) {
            default: {
                return true;
            }
            case 1: {
                return false;
            }
            case 0: {
                return false;
            }
        }
    }
    
    public static Boolean hasHostName(final String key) {
        return Collector.sCacheDNS.containsKey(key);
    }
    
    private static void printAllPackages() {
        for (final PackageInfo packageInfo : (ArrayList)getPackages(RunStore.getContext())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(packageInfo.packageName);
            sb.append(" uid_");
            sb.append(packageInfo.applicationInfo.uid);
            Log.d("NetMonitor", sb.toString());
        }
    }
    
    public static void provideDetail(final int n, final byte[] array) {
        final ArrayList<Report> filterReportsByAdd = filterReportsByAdd(n, array);
        Collector.sDetailReport = filterReportsByAdd.get(0);
        buildDetailStrings(filterReportsByAdd);
    }
    
    public static HashMap<Integer, List<Report>> provideSimpleReports(final ReportEntityDao reportEntityDao) {
        updateReports(reportEntityDao);
        Collector.mFilteredUidReportMap = filterReports();
        return Collector.mFilteredUidReportMap = sortMapByLabels();
    }
    
    private static void pull() {
        final ArrayList<Report> list = new ArrayList<Report>();
        final Iterator<Integer> iterator = Detector.sReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            list.add(Detector.sReportMap.get(iterator.next()));
        }
        Collector.sReportList = deepCopyReportList(list);
    }
    
    private static String readEndpoints(final Map<String, Object> map) {
        String s;
        if (map.containsKey("endpoints")) {
            final HashMap<K, String> hashMap = map.get("endpoints").get(0);
            if (hashMap.containsKey("grade")) {
                s = hashMap.get("grade");
            }
            else if (hashMap.containsKey("statusMessage")) {
                s = hashMap.get("statusMessage");
            }
            else {
                s = "no_status";
            }
        }
        else {
            s = "no_endpoints";
        }
        return s;
    }
    
    public static void resolveHosts() {
        int index = 0;
    Label_0062_Outer:
        while (true) {
            if (index >= Collector.sReportList.size()) {
                return;
            }
            final Report report = Collector.sReportList.get(index);
            while (true) {
                if (hasHostName(report.remoteAdd.getHostAddress())) {
                    break Label_0062;
                }
                try {
                    Collector.sCacheDNS.put(report.remoteAdd.getHostAddress(), report.remoteAdd.getHostName());
                    ++index;
                    continue Label_0062_Outer;
                }
                catch (RuntimeException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static void saveReports(final ReportEntityDao reportEntityDao) {
        Collector.appsToIncludeInScan = new ArrayList<String>(new LinkedHashSet<String>(Collector.appsToIncludeInScan));
        Collector.appsToExcludeFromScan = new ArrayList<String>(new LinkedHashSet<String>(Collector.appsToExcludeFromScan));
        if (!Collector.sReportList.isEmpty()) {
            for (final Report report : Collector.sReportList) {
                final ReportEntity reportEntity = new ReportEntity();
                final String package1 = getPackage(report.uid);
                if (!Collector.appsToExcludeFromScan.contains(package1) && Collector.appsToIncludeInScan.contains(package1)) {
                    if (package1 != null) {
                        reportEntity.setAppName(package1);
                    }
                    else {
                        reportEntity.setAppName("Unknown");
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(report.uid);
                    reportEntity.setUserID(sb.toString());
                    String remoteAddress;
                    if (report.type != TLType.tcp6 && report.type != TLType.udp6) {
                        remoteAddress = report.remoteAdd.getHostAddress();
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(report.remoteAdd.getHostAddress());
                        sb2.append(" (IPv6)");
                        remoteAddress = sb2.toString();
                    }
                    reportEntity.setRemoteAddress(remoteAddress);
                    reportEntity.setRemoteHex(ToolBox.printHexBinary(report.remoteAddHex));
                    String dnsHostName;
                    if (hasHostName(report.remoteAdd.getHostAddress())) {
                        dnsHostName = getDnsHostName(report.remoteAdd.getHostAddress());
                    }
                    else {
                        dnsHostName = "name not resolved";
                    }
                    reportEntity.setRemoteHost(dnsHostName);
                    String localAddress;
                    if (report.type != TLType.tcp6 && report.type != TLType.udp6) {
                        localAddress = report.localAdd.getHostAddress();
                    }
                    else {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(report.localAdd.getHostAddress());
                        sb3.append(" (IPv6)");
                        localAddress = sb3.toString();
                    }
                    reportEntity.setLocalAddress(localAddress);
                    reportEntity.setLocalHex(ToolBox.printHexBinary(report.localAddHex));
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("");
                    sb4.append(report.remotePort);
                    reportEntity.setServicePort(sb4.toString());
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("");
                    sb5.append(KnownPorts.resolvePort(report.remotePort));
                    reportEntity.setPayloadProtocol(sb5.toString());
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("");
                    sb6.append(report.type);
                    reportEntity.setTransportProtocol(sb6.toString());
                    reportEntity.setTimeStamp(report.timestamp.toString());
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("");
                    sb7.append(report.localPort);
                    reportEntity.setLocalPort(sb7.toString());
                    reportEntity.setConnectionInfo(KnownPorts.CompileConnectionInfo(report.remotePort, report.type));
                    ((AbstractDao<ReportEntity, K>)reportEntityDao).insertOrReplace(reportEntity);
                }
            }
        }
    }
    
    private static void sortListByName(final ArrayList<ArrayList<Report>> list) {
        for (int i = list.size(); i > 1; --i) {
            int index;
            for (int j = 0; j < i - 1; j = index) {
                final String label = getLabel(list.get(j).get(0).uid);
                index = j + 1;
                if (label.compareTo(getLabel(list.get(index).get(0).uid)) > 0) {
                    final ArrayList<Report> element = list.get(j);
                    list.set(j, list.get(index));
                    list.set(index, element);
                }
            }
        }
    }
    
    private static LinkedHashMap<Integer, List<Report>> sortMapByLabels() {
        final LinkedHashMap<Integer, ArrayList<Report>> linkedHashMap = (LinkedHashMap<Integer, ArrayList<Report>>)new LinkedHashMap<Integer, List<Report>>();
        final ArrayList<ArrayList<Report>> list = new ArrayList<ArrayList<Report>>();
        final ArrayList<ArrayList<Report>> c = new ArrayList<ArrayList<Report>>();
        final Iterator<Integer> iterator = Collector.mFilteredUidReportMap.keySet().iterator();
        while (iterator.hasNext()) {
            final ArrayList<Report> list2 = Collector.mFilteredUidReportMap.get(iterator.next());
            if (list2.get(0).uid > 10000) {
                list.add(list2);
            }
            else {
                c.add(list2);
            }
        }
        sortListByName(list);
        sortListByName(c);
        list.addAll(c);
        for (int i = 0; i < list.size(); ++i) {
            linkedHashMap.put(list.get(i).get(0).uid, list.get(i));
        }
        return (LinkedHashMap<Integer, List<Report>>)linkedHashMap;
    }
    
    private static void sortReportsToMap() {
        Collector.mUidReportMap = new HashMap<Integer, List<Report>>();
        for (int i = 0; i < Collector.sReportList.size(); ++i) {
            final Report report = Collector.sReportList.get(i);
            if (!Collector.mUidReportMap.containsKey(report.uid)) {
                Collector.mUidReportMap.put(report.uid, new ArrayList<Report>());
            }
            Collector.mUidReportMap.get(report.uid).add(report);
        }
    }
    
    public static void updateCertHostHandler() {
        for (final String s : Collector.mCertValMap.keySet()) {
            final HashMap<String, Object> hashMap = Collector.mCertValMap.get(s);
            if (hashMap.containsKey("host")) {
                final String key = hashMap.get("host");
                if (s.equals(hashMap.get(s)) || analyseReady(hashMap)) {
                    continue;
                }
                Collector.mCertValMap.put(s, Collector.mCertValMap.get(key));
            }
        }
    }
    
    static void updateCertVal() {
        if (Collector.sCertValList.size() > 0) {
            new AsyncCertVal().execute((Object[])new Void[0]);
        }
    }
    
    private static void updatePackageCache() {
        Collector.sCachePackage = new HashMap<Integer, List<PackageInfo>>();
        for (final PackageInfo packageInfo : (ArrayList)getPackages(RunStore.getContext())) {
            if (packageInfo != null) {
                if (!Collector.sCachePackage.containsKey(packageInfo.applicationInfo.uid)) {
                    Collector.sCachePackage.put(packageInfo.applicationInfo.uid, new ArrayList<PackageInfo>());
                }
                Collector.sCachePackage.get(packageInfo.applicationInfo.uid).add(packageInfo);
            }
        }
        addSysPackage();
    }
    
    public static void updateReports(final ReportEntityDao reportEntityDao) {
        pull();
        fillPackageInformation();
        new AsyncDNS().execute((Object[])new String[] { "" });
        sortReportsToMap();
        if (Collector.isCertVal) {
            fillCertRequests();
        }
        saveReports(reportEntityDao);
    }
    
    static void updateSettings() {
        Collector.isCertVal = PreferenceManager.getDefaultSharedPreferences(RunStore.getContext()).getBoolean("IS_CERTVAL", false);
    }
}
