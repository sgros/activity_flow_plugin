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
import org.secuso.privacyfriendlynetmonitor.Assistant.ExecCom;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;

class Detector {
   private static final String commandTcp = "cat /proc/net/tcp";
   private static final String commandTcp6 = "cat /proc/net/tcp6";
   private static final String commandUdp = "cat /proc/net/udp";
   private static final String commandUdp6 = "cat /proc/net/udp6";
   static HashMap sReportMap = new HashMap();

   private static ArrayList getCurrentConnections() {
      ArrayList var0 = new ArrayList();
      var0.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/tcp"), TLType.tcp));
      var0.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/tcp6"), TLType.tcp6));
      var0.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/udp"), TLType.udp));
      var0.addAll(parseNetOutput(ExecCom.userForResult("cat /proc/net/udp6"), TLType.udp6));
      return var0;
   }

   private static Report initReport(String var0, TLType var1) {
      while(var0.contains("  ")) {
         var0 = var0.replace("  ", " ");
      }

      String[] var2 = var0.split("\\s");
      if (var1 != TLType.tcp && var1 != TLType.udp) {
         return initReport6(var2, var1);
      } else {
         return initReport4(var2, var1);
      }
   }

   private static Report initReport4(String[] var0, TLType var1) {
      ByteBuffer var2 = ByteBuffer.allocate(17);
      var2.position(0);
      var2.put(ToolBox.hexStringToByteArray(var0[1].substring(0, 8)));
      int var3 = var0[1].indexOf(":");
      var2.put(ToolBox.hexStringToByteArray(var0[1].substring(var3 + 1, var3 + 5)));
      var2.put(ToolBox.hexStringToByteArray(var0[2].substring(0, 8)));
      var3 = var0[2].indexOf(":");
      var2.put(ToolBox.hexStringToByteArray(var0[2].substring(var3 + 1, var3 + 5)));
      var2.putInt(Integer.parseInt(var0[7]));
      var2.put(ToolBox.hexStringToByteArray(var0[3]));
      return new Report(var2, var1);
   }

   private static Report initReport6(String[] var0, TLType var1) {
      ByteBuffer var2 = ByteBuffer.allocate(41);
      var2.position(0);
      var2.put(ToolBox.hexStringToByteArray(var0[1].substring(0, 32)));
      int var3 = var0[1].indexOf(":");
      var2.put(ToolBox.hexStringToByteArray(var0[1].substring(var3 + 1, var3 + 5)));
      var2.put(ToolBox.hexStringToByteArray(var0[2].substring(0, 32)));
      var3 = var0[2].indexOf(":");
      var2.put(ToolBox.hexStringToByteArray(var0[2].substring(var3 + 1, var3 + 5)));
      var2.putInt(Integer.parseInt(var0[7]));
      var2.put(ToolBox.hexStringToByteArray(var0[3]));
      return new Report(var2, var1);
   }

   private static List parseNetOutput(String var0, TLType var1) {
      LinkedList var2 = new LinkedList();
      String[] var4 = var0.split("\\n");

      for(int var3 = 1; var3 < var4.length; ++var3) {
         var4[var3] = var4[var3].trim();
         var2.add(initReport(var4[var3], var1));
      }

      return var2;
   }

   private static void removeOldReports() {
      Timestamp var0 = new Timestamp(System.currentTimeMillis() - 10000L);
      Iterator var1 = (new HashSet(sReportMap.keySet())).iterator();

      while(var1.hasNext()) {
         int var2 = (Integer)var1.next();
         if (((Report)sReportMap.get(var2)).timestamp.compareTo(var0) < 0) {
            sReportMap.remove(var2);
         }
      }

   }

   private static void updateOrAdd(ArrayList var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         int var2 = ((Report)var0.get(var1)).localPort;
         if (sReportMap.containsKey(var2)) {
            Report var3 = (Report)sReportMap.get(var2);
            var3.touch();
            var3.state = ((Report)var0.get(var1)).state;
         } else {
            sReportMap.put(var2, var0.get(var1));
         }
      }

   }

   static void updateReportMap() {
      updateOrAdd(getCurrentConnections());
      SharedPreferences var0 = PreferenceManager.getDefaultSharedPreferences(RunStore.getAppContext());
      boolean var1 = var0.getBoolean("IS_LOG", false);
      boolean var2 = var0.getBoolean("IS_CERTVAL", false);
      if (!var1 && !var2) {
         removeOldReports();
      }

   }
}
