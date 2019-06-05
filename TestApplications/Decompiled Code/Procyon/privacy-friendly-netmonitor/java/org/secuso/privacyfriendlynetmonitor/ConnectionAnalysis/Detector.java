// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import java.util.Iterator;
import java.util.HashSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;
import java.nio.ByteBuffer;
import java.util.Collection;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ExecCom;
import java.util.ArrayList;
import java.util.HashMap;

class Detector
{
    private static final String commandTcp = "cat /proc/net/tcp";
    private static final String commandTcp6 = "cat /proc/net/tcp6";
    private static final String commandUdp = "cat /proc/net/udp";
    private static final String commandUdp6 = "cat /proc/net/udp6";
    static HashMap<Integer, Report> sReportMap;
    
    static {
        Detector.sReportMap = new HashMap<Integer, Report>();
    }
    
    private static ArrayList<Report> getCurrentConnections() {
        final ArrayList<Report> list = new ArrayList<Report>();
        list.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/tcp"), TLType.tcp));
        list.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/tcp6"), TLType.tcp6));
        list.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/udp"), TLType.udp));
        list.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/udp6"), TLType.udp6));
        return list;
    }
    
    private static Report initReport(String replace, final TLType tlType) {
        while (replace.contains("  ")) {
            replace = replace.replace("  ", " ");
        }
        final String[] split = replace.split("\\s");
        if (tlType != TLType.tcp && tlType != TLType.udp) {
            return initReport6(split, tlType);
        }
        return initReport4(split, tlType);
    }
    
    private static Report initReport4(final String[] array, final TLType tlType) {
        final ByteBuffer allocate = ByteBuffer.allocate(17);
        allocate.position(0);
        allocate.put(ToolBox.hexStringToByteArray(array[1].substring(0, 8)));
        final int index = array[1].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(array[1].substring(index + 1, index + 5)));
        allocate.put(ToolBox.hexStringToByteArray(array[2].substring(0, 8)));
        final int index2 = array[2].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(array[2].substring(index2 + 1, index2 + 5)));
        allocate.putInt(Integer.parseInt(array[7]));
        allocate.put(ToolBox.hexStringToByteArray(array[3]));
        return new Report(allocate, tlType);
    }
    
    private static Report initReport6(final String[] array, final TLType tlType) {
        final ByteBuffer allocate = ByteBuffer.allocate(41);
        allocate.position(0);
        allocate.put(ToolBox.hexStringToByteArray(array[1].substring(0, 32)));
        final int index = array[1].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(array[1].substring(index + 1, index + 5)));
        allocate.put(ToolBox.hexStringToByteArray(array[2].substring(0, 32)));
        final int index2 = array[2].indexOf(":");
        allocate.put(ToolBox.hexStringToByteArray(array[2].substring(index2 + 1, index2 + 5)));
        allocate.putInt(Integer.parseInt(array[7]));
        allocate.put(ToolBox.hexStringToByteArray(array[3]));
        return new Report(allocate, tlType);
    }
    
    private static List<Report> parseNetOutput(final String s, final TLType tlType) {
        final LinkedList<Report> list = new LinkedList<Report>();
        final String[] split = s.split("\\n");
        for (int i = 1; i < split.length; ++i) {
            split[i] = split[i].trim();
            list.add(initReport(split[i], tlType));
        }
        return list;
    }
    
    private static void removeOldReports() {
        final Timestamp ts = new Timestamp(System.currentTimeMillis() - 10000L);
        for (final int intValue : new HashSet<Integer>(Detector.sReportMap.keySet())) {
            if (Detector.sReportMap.get(intValue).timestamp.compareTo(ts) < 0) {
                Detector.sReportMap.remove(intValue);
            }
        }
    }
    
    private static void updateOrAdd(final ArrayList<Report> list) {
        for (int i = 0; i < list.size(); ++i) {
            final int localPort = list.get(i).localPort;
            if (Detector.sReportMap.containsKey(localPort)) {
                final Report report = Detector.sReportMap.get(localPort);
                report.touch();
                report.state = list.get(i).state;
            }
            else {
                Detector.sReportMap.put(localPort, list.get(i));
            }
        }
    }
    
    static void updateReportMap() {
        updateOrAdd(getCurrentConnections());
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(RunStore.getAppContext());
        final boolean boolean1 = defaultSharedPreferences.getBoolean("IS_LOG", false);
        final boolean boolean2 = defaultSharedPreferences.getBoolean("IS_CERTVAL", false);
        if (!boolean1 && !boolean2) {
            removeOldReports();
        }
    }
}
