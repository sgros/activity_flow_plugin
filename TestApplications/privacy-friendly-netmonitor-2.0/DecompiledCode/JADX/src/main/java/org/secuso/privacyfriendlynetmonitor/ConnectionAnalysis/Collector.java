package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.support.p000v4.app.NotificationCompat;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncCertVal;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncDNS;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;
import org.secuso.privacyfriendlynetmonitor.BuildConfig;
import org.secuso.privacyfriendlynetmonitor.C0501R;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class Collector {
    private static List<String> appsToExcludeFromScan = new ArrayList();
    private static List<String> appsToIncludeInScan = new ArrayList();
    public static Boolean isCertVal = Boolean.valueOf(false);
    private static HashMap<String, String> knownUIDs = new HashMap();
    public static HashMap<String, Map<String, Object>> mCertValMap = new HashMap();
    private static HashMap<Integer, List<Report>> mFilteredUidReportMap = new HashMap();
    private static HashMap<Integer, List<Report>> mUidReportMap = new HashMap();
    private static HashMap<String, String> sCacheDNS = new HashMap();
    private static HashMap<Integer, Drawable> sCacheIcon = new HashMap();
    private static HashMap<Integer, String> sCacheLabel = new HashMap();
    private static HashMap<Integer, List<PackageInfo>> sCachePackage = new HashMap();
    public static List<String> sCertValList = new ArrayList();
    public static Report sDetailReport;
    public static ArrayList<String[]> sDetailReportList = new ArrayList();
    private static ArrayList<Report> sReportList = new ArrayList();

    public static HashMap<Integer, List<Report>> provideSimpleReports(ReportEntityDao reportEntityDao) {
        updateReports(reportEntityDao);
        mFilteredUidReportMap = filterReports();
        mFilteredUidReportMap = sortMapByLabels();
        return mFilteredUidReportMap;
    }

    private static LinkedHashMap<Integer, List<Report>> sortMapByLabels() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Integer intValue : mFilteredUidReportMap.keySet()) {
            ArrayList arrayList3 = (ArrayList) mFilteredUidReportMap.get(Integer.valueOf(intValue.intValue()));
            if (((Report) arrayList3.get(0)).uid > 10000) {
                arrayList.add(arrayList3);
            } else {
                arrayList2.add(arrayList3);
            }
        }
        sortListByName(arrayList);
        sortListByName(arrayList2);
        arrayList.addAll(arrayList2);
        for (int i = 0; i < arrayList.size(); i++) {
            linkedHashMap.put(Integer.valueOf(((Report) ((ArrayList) arrayList.get(i)).get(0)).uid), arrayList.get(i));
        }
        return linkedHashMap;
    }

    private static void sortListByName(ArrayList<ArrayList<Report>> arrayList) {
        for (int size = arrayList.size(); size > 1; size--) {
            int i = 0;
            while (i < size - 1) {
                int i2 = i + 1;
                if (getLabel(((Report) ((ArrayList) arrayList.get(i)).get(0)).uid).compareTo(getLabel(((Report) ((ArrayList) arrayList.get(i2)).get(0)).uid)) > 0) {
                    ArrayList arrayList2 = (ArrayList) arrayList.get(i);
                    arrayList.set(i, arrayList.get(i2));
                    arrayList.set(i2, arrayList2);
                }
                i = i2;
            }
        }
    }

    private static HashMap<Integer, List<Report>> filterReports() {
        HashMap hashMap = new HashMap();
        HashSet hashSet = new HashSet();
        for (Integer intValue : mUidReportMap.keySet()) {
            int intValue2 = intValue.intValue();
            hashMap.put(Integer.valueOf(intValue2), new ArrayList());
            ArrayList arrayList = (ArrayList) mUidReportMap.get(Integer.valueOf(intValue2));
            ArrayList arrayList2 = (ArrayList) hashMap.get(Integer.valueOf(intValue2));
            hashSet.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                String hostAddress = ((Report) arrayList.get(i)).remoteAdd.getHostAddress();
                if (!hashSet.contains(hostAddress)) {
                    arrayList2.add(arrayList.get(i));
                    hashSet.add(hostAddress);
                }
            }
        }
        return hashMap;
    }

    static void updateSettings() {
        isCertVal = Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(RunStore.getContext()).getBoolean(Const.IS_CERTVAL, false));
    }

    public static void saveReports(ReportEntityDao reportEntityDao) {
        appsToIncludeInScan = new ArrayList(new LinkedHashSet(appsToIncludeInScan));
        appsToExcludeFromScan = new ArrayList(new LinkedHashSet(appsToExcludeFromScan));
        if (!sReportList.isEmpty()) {
            Iterator it = sReportList.iterator();
            while (it.hasNext()) {
                Report report = (Report) it.next();
                ReportEntity reportEntity = new ReportEntity();
                String str = getPackage(report.uid);
                if (!appsToExcludeFromScan.contains(str) && appsToIncludeInScan.contains(str)) {
                    if (str != null) {
                        reportEntity.setAppName(str);
                    } else {
                        reportEntity.setAppName(Const.STATUS_UNKNOWN);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(report.uid);
                    reportEntity.setUserID(stringBuilder.toString());
                    if (report.type == TLType.tcp6 || report.type == TLType.udp6) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(report.remoteAdd.getHostAddress());
                        stringBuilder.append(" (IPv6)");
                        str = stringBuilder.toString();
                    } else {
                        str = report.remoteAdd.getHostAddress();
                    }
                    reportEntity.setRemoteAddress(str);
                    reportEntity.setRemoteHex(ToolBox.printHexBinary(report.remoteAddHex));
                    reportEntity.setRemoteHost(hasHostName(report.remoteAdd.getHostAddress()).booleanValue() ? getDnsHostName(report.remoteAdd.getHostAddress()) : "name not resolved");
                    if (report.type == TLType.tcp6 || report.type == TLType.udp6) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(report.localAdd.getHostAddress());
                        stringBuilder.append(" (IPv6)");
                        str = stringBuilder.toString();
                    } else {
                        str = report.localAdd.getHostAddress();
                    }
                    reportEntity.setLocalAddress(str);
                    reportEntity.setLocalHex(ToolBox.printHexBinary(report.localAddHex));
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(report.remotePort);
                    reportEntity.setServicePort(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(KnownPorts.resolvePort(report.remotePort));
                    reportEntity.setPayloadProtocol(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(report.type);
                    reportEntity.setTransportProtocol(stringBuilder.toString());
                    reportEntity.setTimeStamp(report.timestamp.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(report.localPort);
                    reportEntity.setLocalPort(stringBuilder.toString());
                    reportEntity.setConnectionInfo(KnownPorts.CompileConnectionInfo(report.remotePort, report.type));
                    reportEntityDao.insertOrReplace(reportEntity);
                }
            }
        }
    }

    public static void updateReports(ReportEntityDao reportEntityDao) {
        pull();
        fillPackageInformation();
        new AsyncDNS().execute(new String[]{""});
        sortReportsToMap();
        if (isCertVal.booleanValue()) {
            fillCertRequests();
        }
        saveReports(reportEntityDao);
    }

    private static void fillCertRequests() {
        for (Integer intValue : mFilteredUidReportMap.keySet()) {
            ArrayList arrayList = (ArrayList) mFilteredUidReportMap.get(Integer.valueOf(intValue.intValue()));
            for (int i = 0; i < arrayList.size(); i++) {
                Report report = (Report) arrayList.get(i);
                String hostAddress = report.remoteAdd.getHostAddress();
                if (KnownPorts.isTlsPort(report.remotePort) && hasHostName(hostAddress).booleanValue() && !mCertValMap.containsKey(getDnsHostName(hostAddress)) && !sCertValList.contains(getDnsHostName(hostAddress))) {
                    sCertValList.add(getDnsHostName(hostAddress));
                }
            }
        }
    }

    private static void sortReportsToMap() {
        mUidReportMap = new HashMap();
        for (int i = 0; i < sReportList.size(); i++) {
            Report report = (Report) sReportList.get(i);
            if (!mUidReportMap.containsKey(Integer.valueOf(report.uid))) {
                mUidReportMap.put(Integer.valueOf(report.uid), new ArrayList());
            }
            ((List) mUidReportMap.get(Integer.valueOf(report.uid))).add(report);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002d A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002f A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002e A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x002d A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002f A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002e A:{RETURN} */
    public static boolean hasGrade(java.lang.String r4) {
        /*
        r4 = getMetric(r4);
        r0 = r4.hashCode();
        r1 = 35394935; // 0x21c1577 float:1.146723E-37 double:1.74874214E-316;
        r2 = 1;
        r3 = 0;
        if (r0 == r1) goto L_0x001f;
    L_0x000f:
        r1 = 50017003; // 0x2fb32eb float:3.691036E-37 double:2.4711683E-316;
        if (r0 == r1) goto L_0x0015;
    L_0x0014:
        goto L_0x0029;
    L_0x0015:
        r0 = "RESOLVING CERTIFICATE HOSTS";
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0029;
    L_0x001d:
        r4 = r3;
        goto L_0x002a;
    L_0x001f:
        r0 = "PENDING";
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0029;
    L_0x0027:
        r4 = r2;
        goto L_0x002a;
    L_0x0029:
        r4 = -1;
    L_0x002a:
        switch(r4) {
            case 0: goto L_0x002f;
            case 1: goto L_0x002e;
            default: goto L_0x002d;
        };
    L_0x002d:
        return r2;
    L_0x002e:
        return r3;
    L_0x002f:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector.hasGrade(java.lang.String):boolean");
    }

    private static void pull() {
        ArrayList arrayList = new ArrayList();
        for (Integer intValue : Detector.sReportMap.keySet()) {
            arrayList.add(Detector.sReportMap.get(Integer.valueOf(intValue.intValue())));
        }
        sReportList = deepCopyReportList(arrayList);
    }

    public static void resolveHosts() {
        for (int i = 0; i < sReportList.size(); i++) {
            Report report = (Report) sReportList.get(i);
            if (!hasHostName(report.remoteAdd.getHostAddress()).booleanValue()) {
                try {
                    sCacheDNS.put(report.remoteAdd.getHostAddress(), report.remoteAdd.getHostName());
                } catch (RuntimeException unused) {
                }
            }
        }
    }

    static void updateCertVal() {
        if (sCertValList.size() > 0) {
            new AsyncCertVal().execute(new Void[0]);
        }
    }

    private static void fillPackageInformation() {
        for (int i = 0; i < sReportList.size(); i++) {
            Report report = (Report) sReportList.get(i);
            if (!sCachePackage.containsKey(Integer.valueOf(report.uid))) {
                updatePackageCache();
            }
            if (sCachePackage.containsKey(Integer.valueOf(report.uid)) && ((List) sCachePackage.get(Integer.valueOf(report.uid))).size() == 1) {
                PackageInfo packageInfo = (PackageInfo) ((List) sCachePackage.get(Integer.valueOf(report.uid))).get(0);
                report.appName = packageInfo.applicationInfo.name;
                report.packageName = packageInfo.packageName;
            } else if (!sCachePackage.containsKey(Integer.valueOf(report.uid)) || ((List) sCachePackage.get(Integer.valueOf(report.uid))).size() <= 1) {
                report.appName = "Unknown App";
                report.appName = "app.unknown";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UID ");
                stringBuilder.append(report.uid);
                report.appName = stringBuilder.toString();
                report.appName = "app.unknown";
            }
        }
    }

    private static ArrayList<Report> deepCopyReportList(ArrayList<Report> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        while (i < arrayList.size()) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(arrayList.get(i));
                objectOutputStream.flush();
                arrayList2.add(Report.class.cast(new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject()));
                i++;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return arrayList2;
    }

    private static void updatePackageCache() {
        sCachePackage = new HashMap();
        Iterator it = ((ArrayList) getPackages(RunStore.getContext())).iterator();
        while (it.hasNext()) {
            PackageInfo packageInfo = (PackageInfo) it.next();
            if (packageInfo != null) {
                if (!sCachePackage.containsKey(Integer.valueOf(packageInfo.applicationInfo.uid))) {
                    sCachePackage.put(Integer.valueOf(packageInfo.applicationInfo.uid), new ArrayList());
                }
                ((List) sCachePackage.get(Integer.valueOf(packageInfo.applicationInfo.uid))).add(packageInfo);
            }
        }
        addSysPackage();
    }

    private static void addSysPackage() {
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = "com.android.system";
        packageInfo.versionCode = 8;
        packageInfo.versionName = BuildConfig.VERSION_NAME;
        packageInfo.applicationInfo = new ApplicationInfo();
        packageInfo.applicationInfo.name = "System";
        packageInfo.applicationInfo.uid = 0;
        packageInfo.applicationInfo.icon = 0;
        if (!sCachePackage.containsKey(Integer.valueOf(packageInfo.applicationInfo.uid))) {
            sCachePackage.put(Integer.valueOf(packageInfo.applicationInfo.uid), new ArrayList());
        }
        ((List) sCachePackage.get(Integer.valueOf(packageInfo.applicationInfo.uid))).add(packageInfo);
    }

    private static List<PackageInfo> getPackages(Context context) {
        ArrayList arrayList;
        synchronized (context.getApplicationContext()) {
            arrayList = new ArrayList(context.getPackageManager().getInstalledPackages(0));
        }
        return arrayList;
    }

    private static void printAllPackages() {
        Iterator it = ((ArrayList) getPackages(RunStore.getContext())).iterator();
        while (it.hasNext()) {
            PackageInfo packageInfo = (PackageInfo) it.next();
            String str = Const.LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageInfo.packageName);
            stringBuilder.append(" uid_");
            stringBuilder.append(packageInfo.applicationInfo.uid);
            Log.d(str, stringBuilder.toString());
        }
    }

    public static Drawable getIcon(int i) {
        try {
            if (!sCacheIcon.containsKey(Integer.valueOf(i))) {
                if (!sCachePackage.containsKey(Integer.valueOf(i)) || ((List) sCachePackage.get(Integer.valueOf(i))).size() != 1) {
                    return getDefaultIcon();
                }
                sCacheIcon.put(Integer.valueOf(i), ((PackageInfo) ((List) sCachePackage.get(Integer.valueOf(i))).get(0)).applicationInfo.loadIcon(RunStore.getContext().getPackageManager()));
            }
            return (Drawable) sCacheIcon.get(Integer.valueOf(i));
        } catch (NullPointerException unused) {
            String str = Const.LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not load icon of: ");
            stringBuilder.append(((PackageInfo) ((List) sCachePackage.get(Integer.valueOf(i))).get(0)).packageName);
            Log.e(str, stringBuilder.toString());
            return getDefaultIcon();
        }
    }

    private static Drawable getDefaultIcon() {
        if (VERSION.SDK_INT >= 21) {
            return getIconNew(17301651);
        }
        return getIconOld(17301651);
    }

    @TargetApi(9)
    private static Drawable getIconOld(int i) {
        return RunStore.getContext().getResources().getDrawable(i);
    }

    @TargetApi(21)
    private static Drawable getIconNew(int i) {
        return RunStore.getContext().getDrawable(i);
    }

    public static String getLabel(int i) {
        if (!sCacheLabel.containsKey(Integer.valueOf(i))) {
            if (sCachePackage.containsKey(Integer.valueOf(i)) && ((List) sCachePackage.get(Integer.valueOf(i))).size() == 1) {
                sCacheLabel.put(Integer.valueOf(i), (String) ((PackageInfo) ((List) sCachePackage.get(Integer.valueOf(i))).get(0)).applicationInfo.loadLabel(RunStore.getContext().getPackageManager()));
            } else if (!sCachePackage.containsKey(Integer.valueOf(i)) || ((List) sCachePackage.get(Integer.valueOf(i))).size() <= 1) {
                return RunStore.getContext().getString(C0501R.string.unknown_app);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UID ");
                stringBuilder.append(i);
                return stringBuilder.toString();
            }
        }
        return (String) sCacheLabel.get(Integer.valueOf(i));
    }

    public static String getPackage(int i) {
        if (sCachePackage.containsKey(Integer.valueOf(i)) && ((List) sCachePackage.get(Integer.valueOf(i))).size() == 1) {
            return ((PackageInfo) ((List) sCachePackage.get(Integer.valueOf(i))).get(0)).packageName;
        }
        return RunStore.getContext().getString(C0501R.string.unknown_package);
    }

    public static String getDnsHostName(String str) {
        return sCacheDNS.containsKey(str) ? (String) sCacheDNS.get(str) : str;
    }

    public static Boolean hasHostName(String str) {
        return Boolean.valueOf(sCacheDNS.containsKey(str));
    }

    public static String getCertHost(String str) {
        if (mCertValMap.containsKey(str)) {
            Map map = (Map) mCertValMap.get(str);
            return (analyseReady(map) && map.containsKey("host")) ? (String) map.get("host") : str;
        }
        return str;
    }

    public static String getMetric(String str) {
        if (!mCertValMap.containsKey(str)) {
            return "PENDING";
        }
        Map map = (Map) mCertValMap.get(str);
        if (!analyseReady(map)) {
            return "PENDING";
        }
        String readEndpoints = readEndpoints(map);
        if (readEndpoints.equals("no_grade")) {
            return "no_grade";
        }
        if (!readEndpoints.equals("Certificate not valid for domain name")) {
            return readEndpoints.equals("no_endpoints") ? "no_endpoints" : readEndpoints;
        } else {
            handleInvalidDomainName(map);
            return "RESOLVING CERTIFICATE HOSTS";
        }
    }

    private static String readEndpoints(Map<String, Object> map) {
        if (!map.containsKey("endpoints")) {
            return "no_endpoints";
        }
        HashMap hashMap = (HashMap) ((ArrayList) map.get("endpoints")).get(0);
        if (hashMap.containsKey("grade")) {
            return (String) hashMap.get("grade");
        }
        return hashMap.containsKey("statusMessage") ? (String) hashMap.get("statusMessage") : "no_status";
    }

    private static void handleInvalidDomainName(Map<String, Object> map) {
        if (map.containsKey("certHostnames") && map.containsKey("host")) {
            ArrayList arrayList = (ArrayList) map.get("certHostnames");
            String str = (String) map.get("host");
            String replace = ((String) arrayList.get(0)).replace("*.", "");
            if (mCertValMap.containsKey(replace) && mCertValMap.containsKey(str)) {
                mCertValMap.put(str, mCertValMap.get(replace));
                if (sCertValList.contains(str)) {
                    sCertValList.remove(str);
                }
            } else if (!sCertValList.contains(replace)) {
                sCertValList.add(replace);
            }
        }
    }

    public static void updateCertHostHandler() {
        for (String str : mCertValMap.keySet()) {
            HashMap hashMap = (HashMap) mCertValMap.get(str);
            if (hashMap.containsKey("host")) {
                String str2 = (String) hashMap.get("host");
                if (!(str.equals(hashMap.get(str)) || analyseReady(hashMap))) {
                    mCertValMap.put(str, mCertValMap.get(str2));
                }
            }
        }
    }

    public static boolean analyseReady(Map<String, Object> map) {
        String str = (String) map.get(NotificationCompat.CATEGORY_STATUS);
        return str != null && str.equals("READY");
    }

    public static void provideDetail(int i, byte[] bArr) {
        ArrayList filterReportsByAdd = filterReportsByAdd(i, bArr);
        sDetailReport = (Report) filterReportsByAdd.get(0);
        buildDetailStrings(filterReportsByAdd);
    }

    private static ArrayList<Report> filterReportsByAdd(int i, byte[] bArr) {
        List list = (List) mUidReportMap.get(Integer.valueOf(i));
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (Arrays.equals(((Report) list.get(i2)).remoteAddHex, bArr)) {
                arrayList.add(list.get(i2));
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x022d A:{LOOP_END, Catch:{ NullPointerException -> 0x028d }, LOOP:0: B:25:0x0227->B:27:0x022d} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x011e A:{Catch:{ NullPointerException -> 0x028d }} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0108 A:{Catch:{ NullPointerException -> 0x028d }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0131 A:{Catch:{ NullPointerException -> 0x028d }} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x022d A:{LOOP_END, Catch:{ NullPointerException -> 0x028d }, LOOP:0: B:25:0x0227->B:27:0x022d} */
    private static void buildDetailStrings(java.util.ArrayList<org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report> r11) {
        /*
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = 0;
        r2 = r11.get(r1);
        r2 = (org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report) r2;
        r3 = sCachePackage;	 Catch:{ NullPointerException -> 0x028d }
        r4 = r2.uid;	 Catch:{ NullPointerException -> 0x028d }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r3.get(r4);	 Catch:{ NullPointerException -> 0x028d }
        r3 = (java.util.List) r3;	 Catch:{ NullPointerException -> 0x028d }
        r3 = r3.get(r1);	 Catch:{ NullPointerException -> 0x028d }
        r3 = (android.content.pm.PackageInfo) r3;	 Catch:{ NullPointerException -> 0x028d }
        r4 = 2;
        r5 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r6 = "User ID";
        r5[r1] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r6 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r6.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r7 = "";
        r6.append(r7);	 Catch:{ NullPointerException -> 0x028d }
        r7 = r2.uid;	 Catch:{ NullPointerException -> 0x028d }
        r6.append(r7);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r6.toString();	 Catch:{ NullPointerException -> 0x028d }
        r7 = 1;
        r5[r7] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r5);	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r6 = "App Version";
        r5[r1] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r6 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r6.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r8 = "";
        r6.append(r8);	 Catch:{ NullPointerException -> 0x028d }
        r8 = r3.versionName;	 Catch:{ NullPointerException -> 0x028d }
        r6.append(r8);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r6.toString();	 Catch:{ NullPointerException -> 0x028d }
        r5[r7] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r5);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.uid;	 Catch:{ NullPointerException -> 0x028d }
        r6 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r5 <= r6) goto L_0x008c;
    L_0x0064:
        r5 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r6 = "Installed On";
        r5[r1] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r6 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r6.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r8 = "";
        r6.append(r8);	 Catch:{ NullPointerException -> 0x028d }
        r8 = new java.util.Date;	 Catch:{ NullPointerException -> 0x028d }
        r9 = r3.firstInstallTime;	 Catch:{ NullPointerException -> 0x028d }
        r8.<init>(r9);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r8.toString();	 Catch:{ NullPointerException -> 0x028d }
        r6.append(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r6.toString();	 Catch:{ NullPointerException -> 0x028d }
        r5[r7] = r3;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r5);	 Catch:{ NullPointerException -> 0x028d }
        goto L_0x0099;
    L_0x008c:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Installed On";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = "System App";
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
    L_0x0099:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r2.type;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.TLType.tcp6;	 Catch:{ NullPointerException -> 0x028d }
        if (r3 == r5) goto L_0x00c5;
    L_0x00ac:
        r3 = r2.type;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.TLType.udp6;	 Catch:{ NullPointerException -> 0x028d }
        if (r3 != r5) goto L_0x00b3;
    L_0x00b2:
        goto L_0x00c5;
    L_0x00b3:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Remote Address";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.remoteAdd;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        goto L_0x00e7;
    L_0x00c5:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Remote Address";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.remoteAdd;	 Catch:{ NullPointerException -> 0x028d }
        r6 = r6.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = "\n(IPv6 translated)";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
    L_0x00e7:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Remote HEX";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.remoteAddHex;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox.printHexBinary(r5);	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r2.remoteAdd;	 Catch:{ NullPointerException -> 0x028d }
        r3 = r3.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r3 = hasHostName(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r3.booleanValue();	 Catch:{ NullPointerException -> 0x028d }
        if (r3 == 0) goto L_0x011e;
    L_0x0108:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Remote Host";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.remoteAdd;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r5 = getDnsHostName(r5);	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        goto L_0x012b;
    L_0x011e:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Remote Host";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = "name not resolved";
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
    L_0x012b:
        r3 = r2.type;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.TLType.tcp6;	 Catch:{ NullPointerException -> 0x028d }
        if (r3 == r5) goto L_0x014a;
    L_0x0131:
        r3 = r2.type;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.TLType.udp6;	 Catch:{ NullPointerException -> 0x028d }
        if (r3 != r5) goto L_0x0138;
    L_0x0137:
        goto L_0x014a;
    L_0x0138:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Local Address";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.localAdd;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        goto L_0x016c;
    L_0x014a:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Local Address";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.localAdd;	 Catch:{ NullPointerException -> 0x028d }
        r6 = r6.getHostAddress();	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = "\n(IPv6 translated)";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
    L_0x016c:
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Local HEX";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.localAddHex;	 Catch:{ NullPointerException -> 0x028d }
        r5 = org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox.printHexBinary(r5);	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Service Port";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = "";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.remotePort;	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Payload Protocol";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = "";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.remotePort;	 Catch:{ NullPointerException -> 0x028d }
        r6 = org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts.resolvePort(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Transport Protocol";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = "";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.type;	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Last Seen";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r2.timestamp;	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = "";
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r5 = "Simultaneous Connections";
        r3[r1] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r5.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r6 = "";
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r6 = r11.size();	 Catch:{ NullPointerException -> 0x028d }
        r5.append(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.toString();	 Catch:{ NullPointerException -> 0x028d }
        r3[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r3);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r1;
    L_0x0227:
        r5 = r11.size();	 Catch:{ NullPointerException -> 0x028d }
        if (r3 >= r5) goto L_0x027e;
    L_0x022d:
        r5 = r11.get(r3);	 Catch:{ NullPointerException -> 0x028d }
        r5 = (org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report) r5;	 Catch:{ NullPointerException -> 0x028d }
        r6 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r8 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r8.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r9 = "(";
        r8.append(r9);	 Catch:{ NullPointerException -> 0x028d }
        r3 = r3 + 1;
        r8.append(r3);	 Catch:{ NullPointerException -> 0x028d }
        r9 = ")src port > dst port";
        r8.append(r9);	 Catch:{ NullPointerException -> 0x028d }
        r8 = r8.toString();	 Catch:{ NullPointerException -> 0x028d }
        r6[r1] = r8;	 Catch:{ NullPointerException -> 0x028d }
        r8 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x028d }
        r8.<init>();	 Catch:{ NullPointerException -> 0x028d }
        r9 = r5.localPort;	 Catch:{ NullPointerException -> 0x028d }
        r8.append(r9);	 Catch:{ NullPointerException -> 0x028d }
        r9 = " > ";
        r8.append(r9);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r5.remotePort;	 Catch:{ NullPointerException -> 0x028d }
        r8.append(r5);	 Catch:{ NullPointerException -> 0x028d }
        r5 = r8.toString();	 Catch:{ NullPointerException -> 0x028d }
        r6[r7] = r5;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r6 = "    last socket-state ";
        r5[r1] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r6 = r2.state;	 Catch:{ NullPointerException -> 0x028d }
        r6 = getTransportState(r6);	 Catch:{ NullPointerException -> 0x028d }
        r5[r7] = r6;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r5);	 Catch:{ NullPointerException -> 0x028d }
        goto L_0x0227;
    L_0x027e:
        r11 = new java.lang.String[r4];	 Catch:{ NullPointerException -> 0x028d }
        r2 = "";
        r11[r1] = r2;	 Catch:{ NullPointerException -> 0x028d }
        r1 = "";
        r11[r7] = r1;	 Catch:{ NullPointerException -> 0x028d }
        r0.add(r11);	 Catch:{ NullPointerException -> 0x028d }
        sDetailReportList = r0;	 Catch:{ NullPointerException -> 0x028d }
    L_0x028d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector.buildDetailStrings(java.util.ArrayList):void");
    }

    private static java.lang.String getTransportState(byte[] r1) {
        /*
        r1 = org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox.printHexBinary(r1);
        r0 = r1.hashCode();
        switch(r0) {
            case 1537: goto L_0x0084;
            case 1538: goto L_0x007a;
            case 1539: goto L_0x0070;
            case 1540: goto L_0x0066;
            case 1541: goto L_0x005c;
            case 1542: goto L_0x0052;
            case 1543: goto L_0x0048;
            case 1544: goto L_0x003e;
            case 1545: goto L_0x0033;
            default: goto L_0x000b;
        };
    L_0x000b:
        switch(r0) {
            case 1553: goto L_0x0028;
            case 1554: goto L_0x001c;
            case 1555: goto L_0x0010;
            default: goto L_0x000e;
        };
    L_0x000e:
        goto L_0x008e;
    L_0x0010:
        r0 = "0C";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0018:
        r1 = 11;
        goto L_0x008f;
    L_0x001c:
        r0 = "0B";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0024:
        r1 = 10;
        goto L_0x008f;
    L_0x0028:
        r0 = "0A";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0030:
        r1 = 9;
        goto L_0x008f;
    L_0x0033:
        r0 = "09";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x003b:
        r1 = 8;
        goto L_0x008f;
    L_0x003e:
        r0 = "08";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0046:
        r1 = 7;
        goto L_0x008f;
    L_0x0048:
        r0 = "07";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0050:
        r1 = 6;
        goto L_0x008f;
    L_0x0052:
        r0 = "06";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x005a:
        r1 = 5;
        goto L_0x008f;
    L_0x005c:
        r0 = "05";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0064:
        r1 = 4;
        goto L_0x008f;
    L_0x0066:
        r0 = "04";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x006e:
        r1 = 3;
        goto L_0x008f;
    L_0x0070:
        r0 = "03";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0078:
        r1 = 2;
        goto L_0x008f;
    L_0x007a:
        r0 = "02";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x0082:
        r1 = 1;
        goto L_0x008f;
    L_0x0084:
        r0 = "01";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x008e;
    L_0x008c:
        r1 = 0;
        goto L_0x008f;
    L_0x008e:
        r1 = -1;
    L_0x008f:
        switch(r1) {
            case 0: goto L_0x00b6;
            case 1: goto L_0x00b3;
            case 2: goto L_0x00b0;
            case 3: goto L_0x00ad;
            case 4: goto L_0x00aa;
            case 5: goto L_0x00a7;
            case 6: goto L_0x00a4;
            case 7: goto L_0x00a1;
            case 8: goto L_0x009e;
            case 9: goto L_0x009b;
            case 10: goto L_0x0098;
            case 11: goto L_0x0095;
            default: goto L_0x0092;
        };
    L_0x0092:
        r1 = "UNKNOWN";
        goto L_0x00b8;
    L_0x0095:
        r1 = "NEW_SYN_RECV";
        goto L_0x00b8;
    L_0x0098:
        r1 = "CLOSING";
        goto L_0x00b8;
    L_0x009b:
        r1 = "LISTEN";
        goto L_0x00b8;
    L_0x009e:
        r1 = "LAST_ACK";
        goto L_0x00b8;
    L_0x00a1:
        r1 = "CLOSE_WAIT";
        goto L_0x00b8;
    L_0x00a4:
        r1 = "CLOSE";
        goto L_0x00b8;
    L_0x00a7:
        r1 = "TIME_WAIT";
        goto L_0x00b8;
    L_0x00aa:
        r1 = "FIN_WAIT2";
        goto L_0x00b8;
    L_0x00ad:
        r1 = "FIN_WAIT1";
        goto L_0x00b8;
    L_0x00b0:
        r1 = "SYN_RECV";
        goto L_0x00b8;
    L_0x00b3:
        r1 = "SYN_SENT";
        goto L_0x00b8;
    L_0x00b6:
        r1 = "ESTABLISHED";
    L_0x00b8:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector.getTransportState(byte[]):java.lang.String");
    }

    public static List<String> getAppsToIncludeInScan() {
        return appsToIncludeInScan;
    }

    public static void addAppToIncludeInScan(String str) {
        appsToIncludeInScan.add(str);
    }

    public static void deleteAppFromIncludeInScan(String str) {
        appsToIncludeInScan.remove(str);
    }

    public static List<String> getAppsToExcludeFromScan() {
        return appsToExcludeFromScan;
    }

    public static void addAppToExcludeFromScan(String str) {
        appsToExcludeFromScan.add(str);
    }

    public static void deleteAppToExcludeFromScan(String str) {
        appsToExcludeFromScan.remove(str);
    }

    public static HashMap<String, String> getKnownUIDs() {
        return knownUIDs;
    }

    public static void addKnownUIDs(String str, String str2) {
        knownUIDs.put(str, str2);
    }
}
