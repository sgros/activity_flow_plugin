package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.ReportEntity;

public class ExpandableHistoryListAdapter extends BaseExpandableListAdapter {
   private Context context;
   private HashMap reportListDetail;
   private List uidList;

   public ExpandableHistoryListAdapter(Context var1, List var2, HashMap var3) {
      this.context = var1;
      this.uidList = var2;
      this.reportListDetail = var3;
   }

   public Object getChild(int var1, int var2) {
      return ((List)this.reportListDetail.get(this.uidList.get(var1))).get(var2);
   }

   public long getChildId(int var1, int var2) {
      return (long)var2;
   }

   public View getChildView(int var1, int var2, boolean var3, View var4, ViewGroup var5) {
      ReportEntity var6 = (ReportEntity)this.getChild(var1, var2);
      String var7 = var6.getRemoteHost();
      View var8 = var4;
      if (var4 == null) {
         var8 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427394, (ViewGroup)null);
      }

      ((TextView)var8.findViewById(2131296363)).setText(var7);
      ((TextView)var8.findViewById(2131296364)).setText("Time Stamp: ");
      ((TextView)var8.findViewById(2131296365)).setText(var6.getTimeStamp());
      return var8;
   }

   public int getChildrenCount(int var1) {
      return ((List)this.reportListDetail.get(this.uidList.get(var1))).size();
   }

   public Object getGroup(int var1) {
      return this.reportListDetail.get(var1);
   }

   public int getGroupCount() {
      return this.reportListDetail.size();
   }

   public long getGroupId(int var1) {
      return (long)var1;
   }

   public View getGroupView(int var1, boolean var2, View var3, ViewGroup var4) {
      View var13 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427393, (ViewGroup)null);
      PackageManager var5 = this.context.getPackageManager();

      String var12;
      try {
         var12 = ((ReportEntity)((List)this.reportListDetail.get(this.uidList.get(var1))).get(0)).getAppName();
      } catch (IndexOutOfBoundsException var11) {
         if (Collector.getKnownUIDs().containsKey(this.uidList.get(var1))) {
            var12 = (String)Collector.getKnownUIDs().get(this.uidList.get(var1));
         } else {
            var12 = var5.getNameForUid(new Integer((String)this.uidList.get(var1)));
         }
      }

      TextView var6 = (TextView)var13.findViewById(2131296362);
      TextView var7 = (TextView)var13.findViewById(2131296361);

      try {
         if (this.getChildrenCount(var1) != 0) {
            var6.setText((String)var5.getApplicationLabel(var5.getApplicationInfo(var12, 128)));
         } else {
            StringBuilder var8 = new StringBuilder();
            var8.append((String)var5.getApplicationLabel(var5.getApplicationInfo(var12, 128)));
            var8.append(" ");
            var8.append(this.context.getString(2131624022));
            var6.setText(var8.toString());
         }
      } catch (NameNotFoundException var10) {
         var6.setText(var12);
      }

      var7.setText(var12);
      ImageView var14 = (ImageView)var13.findViewById(2131296360);

      try {
         var14.setImageDrawable(var5.getApplicationIcon(var12));
      } catch (NameNotFoundException var9) {
         var9.printStackTrace();
      }

      return var13;
   }

   public boolean hasStableIds() {
      return false;
   }

   public boolean isChildSelectable(int var1, int var2) {
      return true;
   }
}
