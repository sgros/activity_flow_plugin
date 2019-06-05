package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Report;

public class ExpandableReportAdapter extends BaseExpandableListAdapter {
   private Context context;
   private HashMap reportListDetail;
   private List uidList;

   public ExpandableReportAdapter(Context var1, List var2, HashMap var3) {
      this.context = var1;
      this.uidList = var2;
      this.reportListDetail = var3;
   }

   private int getWarningColor(String var1) {
      if (!var1.contains("Encrypted") && !var1.substring(0, 1).equals("A")) {
         if (!var1.contains("Inconclusive") && !var1.substring(0, 1).equals("B") && !var1.substring(0, 1).equals("C")) {
            return !var1.contains("Unencrypted") && !var1.substring(0, 1).equals("T") && !var1.substring(0, 1).equals("F") && !var1.substring(0, 1).equals("D") && !var1.substring(0, 1).equals("E") ? 2131099756 : 2131099743;
         } else {
            return 2131099734;
         }
      } else {
         return 2131099715;
      }
   }

   public Object getChild(int var1, int var2) {
      return ((List)this.reportListDetail.get(this.uidList.get(var1))).get(var2);
   }

   public long getChildId(int var1, int var2) {
      return (long)var2;
   }

   public View getChildView(int var1, int var2, boolean var3, View var4, ViewGroup var5) {
      Report var10 = (Report)this.getChild(var1, var2);
      String var6;
      if (Collector.hasHostName(var10.remoteAdd.getHostAddress())) {
         var6 = Collector.getDnsHostName(var10.remoteAdd.getHostAddress());
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("");
         var7.append(var10.remoteAdd.getHostAddress());
         var6 = var7.toString();
      }

      String var11;
      String var13;
      if (Collector.isCertVal && KnownPorts.isTlsPort(var10.remotePort) && Collector.hasHostName(var10.remoteAdd.getHostAddress())) {
         if (var6.equals(Collector.getCertHost(var6))) {
            var13 = "SSL Server Rating:";
            var11 = Collector.getMetric(var6);
         } else {
            var13 = "SSL Server Rating:";
            if (var6.equals(Collector.getCertHost(var6))) {
               var11 = Collector.getMetric(var6);
            } else {
               StringBuilder var12 = new StringBuilder();
               var12.append(Collector.getMetric(var6));
               var12.append(" (");
               var12.append(Collector.getCertHost(var6));
               var12.append(")");
               var11 = var12.toString();
            }
         }
      } else {
         var11 = KnownPorts.CompileConnectionInfo(var10.remotePort, var10.type);
         var13 = "Connection Info:";
      }

      View var8 = var4;
      if (var4 == null) {
         var8 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427414, (ViewGroup)null);
      }

      ((TextView)var8.findViewById(2131296450)).setText(var6);
      ((TextView)var8.findViewById(2131296451)).setText(var13);
      TextView var9 = (TextView)var8.findViewById(2131296452);
      var9.setText(var11);
      SharedPreferences var14 = PreferenceManager.getDefaultSharedPreferences(this.context);
      var9.setTextColor(this.context.getResources().getColor(this.getWarningColor(var11)));
      if (!var14.getBoolean("IS_HIGHLIGHTED", true)) {
         var9.setTextColor(2131099756);
      }

      return var8;
   }

   public int getChildrenCount(int var1) {
      return ((List)this.reportListDetail.get(this.uidList.get(var1))).size();
   }

   public Object getGroup(int var1) {
      return this.uidList.get(var1);
   }

   public int getGroupCount() {
      return this.uidList.size();
   }

   public long getGroupId(int var1) {
      return (long)var1;
   }

   public View getGroupView(int var1, boolean var2, View var3, ViewGroup var4) {
      var1 = (Integer)this.getGroup(var1);
      View var9 = var3;
      if (var3 == null) {
         var9 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427412, (ViewGroup)null);
      }

      TextView var5 = (TextView)var9.findViewById(2131296441);
      var5.setTypeface((Typeface)null, 1);
      TextView var8 = (TextView)var9.findViewById(2131296439);
      ImageView var6 = (ImageView)var9.findViewById(2131296437);
      StringBuilder var7;
      if (var1 <= 10000) {
         var7 = new StringBuilder();
         var7.append(Collector.getLabel(var1));
         var7.append(" (");
         var7.append(((List)this.reportListDetail.get(var1)).size());
         var7.append(") [System]");
         var5.setText(var7.toString());
      } else {
         var7 = new StringBuilder();
         var7.append(Collector.getLabel(var1));
         var7.append(" (");
         var7.append(((List)this.reportListDetail.get(var1)).size());
         var7.append(")");
         var5.setText(var7.toString());
      }

      var8.setText(Collector.getPackage(var1));
      var6.setImageDrawable(Collector.getIcon(var1));
      return var9;
   }

   public boolean hasStableIds() {
      return false;
   }

   public boolean isChildSelectable(int var1, int var2) {
      return true;
   }
}
