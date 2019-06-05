package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncCertVal;
import org.secuso.privacyfriendlynetmonitor.Assistant.AsyncDNS;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntityDao;

public class Collector {
   private static List appsToExcludeFromScan = new ArrayList();
   private static List appsToIncludeInScan = new ArrayList();
   public static Boolean isCertVal = false;
   private static HashMap knownUIDs = new HashMap();
   public static HashMap mCertValMap = new HashMap();
   private static HashMap mFilteredUidReportMap = new HashMap();
   private static HashMap mUidReportMap = new HashMap();
   private static HashMap sCacheDNS = new HashMap();
   private static HashMap sCacheIcon = new HashMap();
   private static HashMap sCacheLabel = new HashMap();
   private static HashMap sCachePackage = new HashMap();
   public static List sCertValList = new ArrayList();
   public static Report sDetailReport;
   public static ArrayList sDetailReportList = new ArrayList();
   private static ArrayList sReportList = new ArrayList();

   public static void addAppToExcludeFromScan(String var0) {
      appsToExcludeFromScan.add(var0);
   }

   public static void addAppToIncludeInScan(String var0) {
      appsToIncludeInScan.add(var0);
   }

   public static void addKnownUIDs(String var0, String var1) {
      knownUIDs.put(var0, var1);
   }

   private static void addSysPackage() {
      PackageInfo var0 = new PackageInfo();
      var0.packageName = "com.android.system";
      var0.versionCode = 8;
      var0.versionName = "2.0";
      var0.applicationInfo = new ApplicationInfo();
      var0.applicationInfo.name = "System";
      var0.applicationInfo.uid = 0;
      var0.applicationInfo.icon = 0;
      if (!sCachePackage.containsKey(var0.applicationInfo.uid)) {
         sCachePackage.put(var0.applicationInfo.uid, new ArrayList());
      }

      ((List)sCachePackage.get(var0.applicationInfo.uid)).add(var0);
   }

   public static boolean analyseReady(Map var0) {
      String var2 = (String)var0.get("status");
      boolean var1;
      if (var2 != null && var2.equals("READY")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static void buildDetailStrings(ArrayList var0) {
      ArrayList var1 = new ArrayList();
      Report var2 = (Report)var0.get(0);

      boolean var10001;
      StringBuilder var4;
      label130: {
         try {
            PackageInfo var3 = (PackageInfo)((List)sCachePackage.get(var2.uid)).get(0);
            var4 = new StringBuilder();
            var4.append("");
            var4.append(var2.uid);
            var1.add(new String[]{"User ID", var4.toString()});
            var4 = new StringBuilder();
            var4.append("");
            var4.append(var3.versionName);
            var1.add(new String[]{"App Version", var4.toString()});
            if (var2.uid > 10000) {
               StringBuilder var5 = new StringBuilder();
               var5.append("");
               Date var22 = new Date(var3.firstInstallTime);
               var5.append(var22.toString());
               var1.add(new String[]{"Installed On", var5.toString()});
               break label130;
            }
         } catch (NullPointerException var20) {
            var10001 = false;
            return;
         }

         try {
            var1.add(new String[]{"Installed On", "System App"});
         } catch (NullPointerException var19) {
            var10001 = false;
            return;
         }
      }

      StringBuilder var21;
      label120: {
         label143: {
            label117:
            try {
               var1.add(new String[]{"", ""});
               if (var2.type != TLType.tcp6 && var2.type != TLType.udp6) {
                  break label117;
               }
               break label143;
            } catch (NullPointerException var18) {
               var10001 = false;
               return;
            }

            try {
               var1.add(new String[]{"Remote Address", var2.remoteAdd.getHostAddress()});
               break label120;
            } catch (NullPointerException var17) {
               var10001 = false;
               return;
            }
         }

         try {
            var21 = new StringBuilder();
            var21.append(var2.remoteAdd.getHostAddress());
            var21.append("\n(IPv6 translated)");
            var1.add(new String[]{"Remote Address", var21.toString()});
         } catch (NullPointerException var16) {
            var10001 = false;
            return;
         }
      }

      label144: {
         try {
            var1.add(new String[]{"Remote HEX", ToolBox.printHexBinary(var2.remoteAddHex)});
            if (hasHostName(var2.remoteAdd.getHostAddress())) {
               var1.add(new String[]{"Remote Host", getDnsHostName(var2.remoteAdd.getHostAddress())});
               break label144;
            }
         } catch (NullPointerException var15) {
            var10001 = false;
            return;
         }

         try {
            var1.add(new String[]{"Remote Host", "name not resolved"});
         } catch (NullPointerException var14) {
            var10001 = false;
            return;
         }
      }

      label95: {
         label145: {
            label92:
            try {
               if (var2.type != TLType.tcp6 && var2.type != TLType.udp6) {
                  break label92;
               }
               break label145;
            } catch (NullPointerException var13) {
               var10001 = false;
               return;
            }

            try {
               var1.add(new String[]{"Local Address", var2.localAdd.getHostAddress()});
               break label95;
            } catch (NullPointerException var12) {
               var10001 = false;
               return;
            }
         }

         try {
            var21 = new StringBuilder();
            var21.append(var2.localAdd.getHostAddress());
            var21.append("\n(IPv6 translated)");
            var1.add(new String[]{"Local Address", var21.toString()});
         } catch (NullPointerException var11) {
            var10001 = false;
            return;
         }
      }

      try {
         var1.add(new String[]{"Local HEX", ToolBox.printHexBinary(var2.localAddHex)});
         var1.add(new String[]{"", ""});
         var21 = new StringBuilder();
         var21.append("");
         var21.append(var2.remotePort);
         var1.add(new String[]{"Service Port", var21.toString()});
         var21 = new StringBuilder();
         var21.append("");
         var21.append(KnownPorts.resolvePort(var2.remotePort));
         var1.add(new String[]{"Payload Protocol", var21.toString()});
         var21 = new StringBuilder();
         var21.append("");
         var21.append(var2.type);
         var1.add(new String[]{"Transport Protocol", var21.toString()});
         var1.add(new String[]{"Last Seen", var2.timestamp.toString()});
         var1.add(new String[]{"", ""});
         var21 = new StringBuilder();
         var21.append("");
         var21.append(var0.size());
         var1.add(new String[]{"Simultaneous Connections", var21.toString()});
      } catch (NullPointerException var10) {
         var10001 = false;
         return;
      }

      int var6 = 0;

      while(true) {
         Report var23;
         try {
            if (var6 >= var0.size()) {
               break;
            }

            var23 = (Report)var0.get(var6);
            var4 = new StringBuilder();
            var4.append("(");
         } catch (NullPointerException var9) {
            var10001 = false;
            return;
         }

         ++var6;

         try {
            var4.append(var6);
            var4.append(")src port > dst port");
            String var24 = var4.toString();
            var4 = new StringBuilder();
            var4.append(var23.localPort);
            var4.append(" > ");
            var4.append(var23.remotePort);
            var1.add(new String[]{var24, var4.toString()});
            var1.add(new String[]{"    last socket-state ", getTransportState(var2.state)});
         } catch (NullPointerException var8) {
            var10001 = false;
            return;
         }
      }

      try {
         var1.add(new String[]{"", ""});
         sDetailReportList = var1;
      } catch (NullPointerException var7) {
         var10001 = false;
      }

   }

   private static ArrayList deepCopyReportList(ArrayList var0) {
      ArrayList var1 = new ArrayList();
      int var2 = 0;

      while(true) {
         try {
            if (var2 >= var0.size()) {
               break;
            }

            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            ObjectOutputStream var4 = new ObjectOutputStream(var3);
            var4.writeObject(var0.get(var2));
            var4.flush();
            ByteArrayInputStream var7 = new ByteArrayInputStream(var3.toByteArray());
            ObjectInputStream var5 = new ObjectInputStream(var7);
            var1.add(Report.class.cast(var5.readObject()));
         } catch (ClassNotFoundException | IOException var6) {
            var6.printStackTrace();
            break;
         }

         ++var2;
      }

      return var1;
   }

   public static void deleteAppFromIncludeInScan(String var0) {
      appsToIncludeInScan.remove(var0);
   }

   public static void deleteAppToExcludeFromScan(String var0) {
      appsToExcludeFromScan.remove(var0);
   }

   private static void fillCertRequests() {
      Iterator var0 = mFilteredUidReportMap.keySet().iterator();

      while(var0.hasNext()) {
         int var1 = (Integer)var0.next();
         ArrayList var2 = (ArrayList)mFilteredUidReportMap.get(var1);

         for(var1 = 0; var1 < var2.size(); ++var1) {
            Report var3 = (Report)var2.get(var1);
            String var4 = var3.remoteAdd.getHostAddress();
            if (KnownPorts.isTlsPort(var3.remotePort) && hasHostName(var4) && !mCertValMap.containsKey(getDnsHostName(var4)) && !sCertValList.contains(getDnsHostName(var4))) {
               sCertValList.add(getDnsHostName(var4));
            }
         }
      }

   }

   private static void fillPackageInformation() {
      for(int var0 = 0; var0 < sReportList.size(); ++var0) {
         Report var1 = (Report)sReportList.get(var0);
         if (!sCachePackage.containsKey(var1.uid)) {
            updatePackageCache();
         }

         if (sCachePackage.containsKey(var1.uid) && ((List)sCachePackage.get(var1.uid)).size() == 1) {
            PackageInfo var3 = (PackageInfo)((List)sCachePackage.get(var1.uid)).get(0);
            var1.appName = var3.applicationInfo.name;
            var1.packageName = var3.packageName;
         } else if (sCachePackage.containsKey(var1.uid) && ((List)sCachePackage.get(var1.uid)).size() > 1) {
            StringBuilder var2 = new StringBuilder();
            var2.append("UID ");
            var2.append(var1.uid);
            var1.appName = var2.toString();
            var1.appName = "app.unknown";
         } else {
            var1.appName = "Unknown App";
            var1.appName = "app.unknown";
         }
      }

   }

   private static HashMap filterReports() {
      HashMap var0 = new HashMap();
      HashSet var1 = new HashSet();
      Iterator var2 = mUidReportMap.keySet().iterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         var0.put(var3, new ArrayList());
         ArrayList var4 = (ArrayList)mUidReportMap.get(var3);
         ArrayList var5 = (ArrayList)var0.get(var3);
         var1.clear();

         for(var3 = 0; var3 < var4.size(); ++var3) {
            String var6 = ((Report)var4.get(var3)).remoteAdd.getHostAddress();
            if (!var1.contains(var6)) {
               var5.add(var4.get(var3));
               var1.add(var6);
            }
         }
      }

      return var0;
   }

   private static ArrayList filterReportsByAdd(int var0, byte[] var1) {
      List var2 = (List)mUidReportMap.get(var0);
      ArrayList var3 = new ArrayList();

      for(var0 = 0; var0 < var2.size(); ++var0) {
         if (Arrays.equals(((Report)var2.get(var0)).remoteAddHex, var1)) {
            var3.add(var2.get(var0));
         }
      }

      return var3;
   }

   public static List getAppsToExcludeFromScan() {
      return appsToExcludeFromScan;
   }

   public static List getAppsToIncludeInScan() {
      return appsToIncludeInScan;
   }

   public static String getCertHost(String var0) {
      if (mCertValMap.containsKey(var0)) {
         Map var1 = (Map)mCertValMap.get(var0);
         if (analyseReady(var1)) {
            if (var1.containsKey("host")) {
               return (String)var1.get("host");
            }

            return var0;
         }
      }

      return var0;
   }

   private static Drawable getDefaultIcon() {
      return VERSION.SDK_INT >= 21 ? getIconNew(17301651) : getIconOld(17301651);
   }

   public static String getDnsHostName(String var0) {
      return sCacheDNS.containsKey(var0) ? (String)sCacheDNS.get(var0) : var0;
   }

   public static Drawable getIcon(int param0) {
      // $FF: Couldn't be decompiled
   }

   @TargetApi(21)
   private static Drawable getIconNew(int var0) {
      return RunStore.getContext().getDrawable(var0);
   }

   @TargetApi(9)
   private static Drawable getIconOld(int var0) {
      return RunStore.getContext().getResources().getDrawable(var0);
   }

   public static HashMap getKnownUIDs() {
      return knownUIDs;
   }

   public static String getLabel(int var0) {
      if (!sCacheLabel.containsKey(var0)) {
         if (!sCachePackage.containsKey(var0) || ((List)sCachePackage.get(var0)).size() != 1) {
            if (sCachePackage.containsKey(var0) && ((List)sCachePackage.get(var0)).size() > 1) {
               StringBuilder var1 = new StringBuilder();
               var1.append("UID ");
               var1.append(var0);
               return var1.toString();
            }

            return RunStore.getContext().getString(2131624076);
         }

         sCacheLabel.put(var0, (String)((PackageInfo)((List)sCachePackage.get(var0)).get(0)).applicationInfo.loadLabel(RunStore.getContext().getPackageManager()));
      }

      return (String)sCacheLabel.get(var0);
   }

   public static String getMetric(String var0) {
      if (mCertValMap.containsKey(var0)) {
         Map var2 = (Map)mCertValMap.get(var0);
         if (analyseReady(var2)) {
            String var1 = readEndpoints(var2);
            if (var1.equals("no_grade")) {
               return "no_grade";
            } else if (var1.equals("Certificate not valid for domain name")) {
               handleInvalidDomainName(var2);
               return "RESOLVING CERTIFICATE HOSTS";
            } else {
               return var1.equals("no_endpoints") ? "no_endpoints" : var1;
            }
         } else {
            return "PENDING";
         }
      } else {
         return "PENDING";
      }
   }

   public static String getPackage(int var0) {
      return sCachePackage.containsKey(var0) && ((List)sCachePackage.get(var0)).size() == 1 ? ((PackageInfo)((List)sCachePackage.get(var0)).get(0)).packageName : RunStore.getContext().getString(2131624077);
   }

   private static List getPackages(Context param0) {
      // $FF: Couldn't be decompiled
   }

   private static String getTransportState(byte[] var0) {
      String var2;
      byte var3;
      label66: {
         var2 = ToolBox.printHexBinary(var0);
         int var1 = var2.hashCode();
         switch(var1) {
         case 1537:
            if (var2.equals("01")) {
               var3 = 0;
               break label66;
            }
            break;
         case 1538:
            if (var2.equals("02")) {
               var3 = 1;
               break label66;
            }
            break;
         case 1539:
            if (var2.equals("03")) {
               var3 = 2;
               break label66;
            }
            break;
         case 1540:
            if (var2.equals("04")) {
               var3 = 3;
               break label66;
            }
            break;
         case 1541:
            if (var2.equals("05")) {
               var3 = 4;
               break label66;
            }
            break;
         case 1542:
            if (var2.equals("06")) {
               var3 = 5;
               break label66;
            }
            break;
         case 1543:
            if (var2.equals("07")) {
               var3 = 6;
               break label66;
            }
            break;
         case 1544:
            if (var2.equals("08")) {
               var3 = 7;
               break label66;
            }
            break;
         case 1545:
            if (var2.equals("09")) {
               var3 = 8;
               break label66;
            }
            break;
         default:
            switch(var1) {
            case 1553:
               if (var2.equals("0A")) {
                  var3 = 9;
                  break label66;
               }
               break;
            case 1554:
               if (var2.equals("0B")) {
                  var3 = 10;
                  break label66;
               }
               break;
            case 1555:
               if (var2.equals("0C")) {
                  var3 = 11;
                  break label66;
               }
            }
         }

         var3 = -1;
      }

      switch(var3) {
      case 0:
         var2 = "ESTABLISHED";
         break;
      case 1:
         var2 = "SYN_SENT";
         break;
      case 2:
         var2 = "SYN_RECV";
         break;
      case 3:
         var2 = "FIN_WAIT1";
         break;
      case 4:
         var2 = "FIN_WAIT2";
         break;
      case 5:
         var2 = "TIME_WAIT";
         break;
      case 6:
         var2 = "CLOSE";
         break;
      case 7:
         var2 = "CLOSE_WAIT";
         break;
      case 8:
         var2 = "LAST_ACK";
         break;
      case 9:
         var2 = "LISTEN";
         break;
      case 10:
         var2 = "CLOSING";
         break;
      case 11:
         var2 = "NEW_SYN_RECV";
         break;
      default:
         var2 = "UNKNOWN";
      }

      return var2;
   }

   private static void handleInvalidDomainName(Map var0) {
      if (var0.containsKey("certHostnames") && var0.containsKey("host")) {
         ArrayList var1 = (ArrayList)var0.get("certHostnames");
         String var2 = (String)var0.get("host");
         String var3 = ((String)var1.get(0)).replace("*.", "");
         if (mCertValMap.containsKey(var3) && mCertValMap.containsKey(var2)) {
            mCertValMap.put(var2, mCertValMap.get(var3));
            if (sCertValList.contains(var2)) {
               sCertValList.remove(var2);
            }
         } else if (!sCertValList.contains(var3)) {
            sCertValList.add(var3);
         }
      }

   }

   public static boolean hasGrade(String var0) {
      byte var2;
      label22: {
         var0 = getMetric(var0);
         int var1 = var0.hashCode();
         if (var1 != 35394935) {
            if (var1 == 50017003 && var0.equals("RESOLVING CERTIFICATE HOSTS")) {
               var2 = 0;
               break label22;
            }
         } else if (var0.equals("PENDING")) {
            var2 = 1;
            break label22;
         }

         var2 = -1;
      }

      switch(var2) {
      case 0:
         return false;
      case 1:
         return false;
      default:
         return true;
      }
   }

   public static Boolean hasHostName(String var0) {
      return sCacheDNS.containsKey(var0);
   }

   private static void printAllPackages() {
      Iterator var0 = ((ArrayList)getPackages(RunStore.getContext())).iterator();

      while(var0.hasNext()) {
         PackageInfo var1 = (PackageInfo)var0.next();
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.packageName);
         var2.append(" uid_");
         var2.append(var1.applicationInfo.uid);
         Log.d("NetMonitor", var2.toString());
      }

   }

   public static void provideDetail(int var0, byte[] var1) {
      ArrayList var2 = filterReportsByAdd(var0, var1);
      sDetailReport = (Report)var2.get(0);
      buildDetailStrings(var2);
   }

   public static HashMap provideSimpleReports(ReportEntityDao var0) {
      updateReports(var0);
      mFilteredUidReportMap = filterReports();
      mFilteredUidReportMap = sortMapByLabels();
      return mFilteredUidReportMap;
   }

   private static void pull() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = Detector.sReportMap.keySet().iterator();

      while(var1.hasNext()) {
         int var2 = (Integer)var1.next();
         var0.add(Detector.sReportMap.get(var2));
      }

      sReportList = deepCopyReportList(var0);
   }

   private static String readEndpoints(Map var0) {
      String var2;
      if (var0.containsKey("endpoints")) {
         HashMap var1 = (HashMap)((ArrayList)var0.get("endpoints")).get(0);
         if (var1.containsKey("grade")) {
            var2 = (String)var1.get("grade");
         } else if (var1.containsKey("statusMessage")) {
            var2 = (String)var1.get("statusMessage");
         } else {
            var2 = "no_status";
         }
      } else {
         var2 = "no_endpoints";
      }

      return var2;
   }

   public static void resolveHosts() {
      for(int var0 = 0; var0 < sReportList.size(); ++var0) {
         Report var1 = (Report)sReportList.get(var0);
         if (!hasHostName(var1.remoteAdd.getHostAddress())) {
            try {
               String var2 = var1.remoteAdd.getHostName();
               sCacheDNS.put(var1.remoteAdd.getHostAddress(), var2);
            } catch (RuntimeException var3) {
            }
         }
      }

   }

   public static void saveReports(ReportEntityDao var0) {
      appsToIncludeInScan = new ArrayList(new LinkedHashSet(appsToIncludeInScan));
      appsToExcludeFromScan = new ArrayList(new LinkedHashSet(appsToExcludeFromScan));
      if (!sReportList.isEmpty()) {
         Iterator var1 = sReportList.iterator();

         while(true) {
            Report var2;
            ReportEntity var3;
            String var4;
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var2 = (Report)var1.next();
                  var3 = new ReportEntity();
                  var4 = getPackage(var2.uid);
               } while(appsToExcludeFromScan.contains(var4));
            } while(!appsToIncludeInScan.contains(var4));

            if (var4 != null) {
               var3.setAppName(var4);
            } else {
               var3.setAppName("Unknown");
            }

            StringBuilder var5 = new StringBuilder();
            var5.append("");
            var5.append(var2.uid);
            var3.setUserID(var5.toString());
            if (var2.type != TLType.tcp6 && var2.type != TLType.udp6) {
               var4 = var2.remoteAdd.getHostAddress();
            } else {
               var5 = new StringBuilder();
               var5.append(var2.remoteAdd.getHostAddress());
               var5.append(" (IPv6)");
               var4 = var5.toString();
            }

            var3.setRemoteAddress(var4);
            var3.setRemoteHex(ToolBox.printHexBinary(var2.remoteAddHex));
            if (hasHostName(var2.remoteAdd.getHostAddress())) {
               var4 = getDnsHostName(var2.remoteAdd.getHostAddress());
            } else {
               var4 = "name not resolved";
            }

            var3.setRemoteHost(var4);
            if (var2.type != TLType.tcp6 && var2.type != TLType.udp6) {
               var4 = var2.localAdd.getHostAddress();
            } else {
               var5 = new StringBuilder();
               var5.append(var2.localAdd.getHostAddress());
               var5.append(" (IPv6)");
               var4 = var5.toString();
            }

            var3.setLocalAddress(var4);
            var3.setLocalHex(ToolBox.printHexBinary(var2.localAddHex));
            var5 = new StringBuilder();
            var5.append("");
            var5.append(var2.remotePort);
            var3.setServicePort(var5.toString());
            var5 = new StringBuilder();
            var5.append("");
            var5.append(KnownPorts.resolvePort(var2.remotePort));
            var3.setPayloadProtocol(var5.toString());
            var5 = new StringBuilder();
            var5.append("");
            var5.append(var2.type);
            var3.setTransportProtocol(var5.toString());
            var3.setTimeStamp(var2.timestamp.toString());
            var5 = new StringBuilder();
            var5.append("");
            var5.append(var2.localPort);
            var3.setLocalPort(var5.toString());
            var3.setConnectionInfo(KnownPorts.CompileConnectionInfo(var2.remotePort, var2.type));
            var0.insertOrReplace(var3);
         }
      }
   }

   private static void sortListByName(ArrayList var0) {
      int var4;
      for(int var1 = var0.size(); var1 > 1; --var1) {
         for(int var2 = 0; var2 < var1 - 1; var2 = var4) {
            String var3 = getLabel(((Report)((ArrayList)var0.get(var2)).get(0)).uid);
            var4 = var2 + 1;
            if (var3.compareTo(getLabel(((Report)((ArrayList)var0.get(var4)).get(0)).uid)) > 0) {
               ArrayList var5 = (ArrayList)var0.get(var2);
               var0.set(var2, var0.get(var4));
               var0.set(var4, var5);
            }
         }
      }

   }

   private static LinkedHashMap sortMapByLabels() {
      LinkedHashMap var0 = new LinkedHashMap();
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      Iterator var3 = mFilteredUidReportMap.keySet().iterator();

      int var4;
      while(var3.hasNext()) {
         var4 = (Integer)var3.next();
         ArrayList var5 = (ArrayList)mFilteredUidReportMap.get(var4);
         if (((Report)var5.get(0)).uid > 10000) {
            var1.add(var5);
         } else {
            var2.add(var5);
         }
      }

      sortListByName(var1);
      sortListByName(var2);
      var1.addAll(var2);

      for(var4 = 0; var4 < var1.size(); ++var4) {
         var0.put(((Report)((ArrayList)var1.get(var4)).get(0)).uid, var1.get(var4));
      }

      return var0;
   }

   private static void sortReportsToMap() {
      mUidReportMap = new HashMap();

      for(int var0 = 0; var0 < sReportList.size(); ++var0) {
         Report var1 = (Report)sReportList.get(var0);
         if (!mUidReportMap.containsKey(var1.uid)) {
            mUidReportMap.put(var1.uid, new ArrayList());
         }

         ((List)mUidReportMap.get(var1.uid)).add(var1);
      }

   }

   public static void updateCertHostHandler() {
      Iterator var0 = mCertValMap.keySet().iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();
         HashMap var2 = (HashMap)mCertValMap.get(var1);
         if (var2.containsKey("host")) {
            String var3 = (String)var2.get("host");
            if (!var1.equals(var2.get(var1)) && !analyseReady(var2)) {
               mCertValMap.put(var1, mCertValMap.get(var3));
            }
         }
      }

   }

   static void updateCertVal() {
      if (sCertValList.size() > 0) {
         (new AsyncCertVal()).execute(new Void[0]);
      }

   }

   private static void updatePackageCache() {
      sCachePackage = new HashMap();
      Iterator var0 = ((ArrayList)getPackages(RunStore.getContext())).iterator();

      while(var0.hasNext()) {
         PackageInfo var1 = (PackageInfo)var0.next();
         if (var1 != null) {
            if (!sCachePackage.containsKey(var1.applicationInfo.uid)) {
               sCachePackage.put(var1.applicationInfo.uid, new ArrayList());
            }

            ((List)sCachePackage.get(var1.applicationInfo.uid)).add(var1);
         }
      }

      addSysPackage();
   }

   public static void updateReports(ReportEntityDao var0) {
      pull();
      fillPackageInformation();
      (new AsyncDNS()).execute(new String[]{""});
      sortReportsToMap();
      if (isCertVal) {
         fillCertRequests();
      }

      saveReports(var0);
   }

   static void updateSettings() {
      isCertVal = PreferenceManager.getDefaultSharedPreferences(RunStore.getContext()).getBoolean("IS_CERTVAL", false);
   }
}
