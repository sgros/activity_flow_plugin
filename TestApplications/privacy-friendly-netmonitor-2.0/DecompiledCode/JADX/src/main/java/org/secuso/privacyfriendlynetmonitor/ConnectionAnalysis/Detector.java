package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.ExecCom;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;

class Detector {
    private static final String commandTcp = "cat /proc/net/tcp";
    private static final String commandTcp6 = "cat /proc/net/tcp6";
    private static final String commandUdp = "cat /proc/net/udp";
    private static final String commandUdp6 = "cat /proc/net/udp6";
    static HashMap<Integer, Report> sReportMap = new HashMap();

    Detector() {
    }

    static void updateReportMap() {
        updateOrAdd(getCurrentConnections());
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(RunStore.getAppContext());
        boolean z = defaultSharedPreferences.getBoolean(Const.IS_LOG, false);
        boolean z2 = defaultSharedPreferences.getBoolean(Const.IS_CERTVAL, false);
        if (!z && !z2) {
            removeOldReports();
        }
    }

    private static void updateOrAdd(ArrayList<Report> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = ((Report) arrayList.get(i)).localPort;
            if (sReportMap.containsKey(Integer.valueOf(i2))) {
                Report report = (Report) sReportMap.get(Integer.valueOf(i2));
                report.touch();
                report.state = ((Report) arrayList.get(i)).state;
            } else {
                sReportMap.put(Integer.valueOf(i2), arrayList.get(i));
            }
        }
    }

    private static void removeOldReports() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() - Const.REPORT_TTL_DEFAULT);
        Iterator it = new HashSet(sReportMap.keySet()).iterator();
        while (it.hasNext()) {
            int intValue = ((Integer) it.next()).intValue();
            if (((Report) sReportMap.get(Integer.valueOf(intValue))).timestamp.compareTo(timestamp) < 0) {
                sReportMap.remove(Integer.valueOf(intValue));
            }
        }
    }

    private static ArrayList<Report> getCurrentConnections() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(parseNetOutput(ExecCom.userForResult(commandTcp), TLType.tcp));
        arrayList.addAll(parseNetOutput(ExecCom.userForResult(commandTcp6), TLType.tcp6));
        arrayList.addAll(parseNetOutput(ExecCom.userForResult(commandUdp), TLType.udp));
        arrayList.addAll(parseNetOutput(ExecCom.userForResult(commandUdp6), TLType.udp6));
        return arrayList;
    }

    private static List<Report> parseNetOutput(String str, TLType tLType) {
        LinkedList linkedList = new LinkedList();
        String[] split = str.split("\\n");
        for (int i = 1; i < split.length; i++) {
            split[i] = split[i].trim();
            linkedList.add(initReport(split[i], tLType));
        }
        return linkedList;
    }

    private static Report initReport(String str, TLType tLType) {
        while (str.contains("  ")) {
            str = str.replace("  ", " ");
        }
        String[] split = str.split("\\s");
        if (tLType == TLType.tcp || tLType == TLType.udp) {
            return initReport4(split, tLType);
        }
        return initReport6(split, tLType);
    }

    private static Report initReport4(String[] strArr, TLType tLType) {
        ByteBuffer allocate = ByteBuffer.allocate(17);
        allocate.position(0);
        allocate.put(ToolBox.hexStringToByteArray(strArr[1].substring(0, 8)));
        int indexOf = strArr[1].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(strArr[1].substring(indexOf + 1, indexOf + 5)));
        allocate.put(ToolBox.hexStringToByteArray(strArr[2].substring(0, 8)));
        int indexOf2 = strArr[2].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(strArr[2].substring(indexOf2 + 1, indexOf2 + 5)));
        allocate.putInt(Integer.parseInt(strArr[7]));
        allocate.put(ToolBox.hexStringToByteArray(strArr[3]));
        return new Report(allocate, tLType);
    }

    private static Report initReport6(String[] strArr, TLType tLType) {
        ByteBuffer allocate = ByteBuffer.allocate(41);
        allocate.position(0);
        allocate.put(ToolBox.hexStringToByteArray(strArr[1].substring(0, 32)));
        int indexOf = strArr[1].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(strArr[1].substring(indexOf + 1, indexOf + 5)));
        allocate.put(ToolBox.hexStringToByteArray(strArr[2].substring(0, 32)));
        int indexOf2 = strArr[2].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(strArr[2].substring(indexOf2 + 1, indexOf2 + 5)));
        allocate.putInt(Integer.parseInt(strArr[7]));
        allocate.put(ToolBox.hexStringToByteArray(strArr[3]));
        return new Report(allocate, tLType);
    }
}
