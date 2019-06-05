package org.secuso.privacyfriendlynetmonitor.Activities.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
   private Context context;
   private HashMap expandableListDetail;
   private List expandableListTitle;

   public ExpandableListAdapter(Context var1, List var2, HashMap var3) {
      this.context = var1;
      this.expandableListTitle = var2;
      this.expandableListDetail = var3;
   }

   public Object getChild(int var1, int var2) {
      return ((List)this.expandableListDetail.get(this.expandableListTitle.get(var1))).get(var2);
   }

   public long getChildId(int var1, int var2) {
      return (long)var2;
   }

   public View getChildView(int var1, int var2, boolean var3, View var4, ViewGroup var5) {
      String var6 = (String)this.getChild(var1, var2);
      View var7 = var4;
      if (var4 == null) {
         var7 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427392, (ViewGroup)null);
      }

      ((TextView)var7.findViewById(2131296344)).setText(var6);
      return var7;
   }

   public int getChildrenCount(int var1) {
      return ((List)this.expandableListDetail.get(this.expandableListTitle.get(var1))).size();
   }

   public Object getGroup(int var1) {
      return this.expandableListTitle.get(var1);
   }

   public int getGroupCount() {
      return this.expandableListTitle.size();
   }

   public long getGroupId(int var1) {
      return (long)var1;
   }

   public View getGroupView(int var1, boolean var2, View var3, ViewGroup var4) {
      String var5 = (String)this.getGroup(var1);
      View var7 = var3;
      if (var3 == null) {
         var7 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2131427391, (ViewGroup)null);
      }

      TextView var6 = (TextView)var7.findViewById(2131296387);
      var6.setTypeface((Typeface)null, 1);
      var6.setText(var5);
      return var7;
   }

   public boolean hasStableIds() {
      return false;
   }

   public boolean isChildSelectable(int var1, int var2) {
      return true;
   }
}
