package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class HistoryDetailActivity extends BaseActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427365);
      RunStore.setContext(this);
      ArrayList var2 = this.getIntent().getStringArrayListExtra("Details");
      HistoryDetailActivity.DetailAdapter var3 = new HistoryDetailActivity.DetailAdapter(this, 2131427411, this.prepareData(var2));
      ListView var9 = (ListView)this.findViewById(2131296446);
      var9.setAdapter(var3);
      View var11 = this.getLayoutInflater().inflate(2131427413, (ViewGroup)null);
      PackageManager var4 = this.getPackageManager();
      String var5 = (String)var2.get(0);
      ImageView var6 = (ImageView)var11.findViewById(2131296438);
      TextView var7 = (TextView)var11.findViewById(2131296442);
      TextView var10 = (TextView)var11.findViewById(2131296440);

      try {
         var6.setImageDrawable(var4.getApplicationIcon(var5));
         var7.setText((String)var4.getApplicationLabel(var4.getApplicationInfo(var5, 128)));
      } catch (NameNotFoundException var8) {
      }

      var10.setText(var5);
      var9.addHeaderView(var11);
   }

   public List prepareData(List var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new String[]{"UID", (String)var1.get(1)});
      var2.add(new String[]{"APP VERSION", (String)var1.get(2)});
      var2.add(new String[]{"INSTALLED ON", (String)var1.get(3)});
      var2.add(new String[]{"", ""});
      var2.add(new String[]{"REMOTE ADDRESS", (String)var1.get(4)});
      var2.add(new String[]{"REMOTE HEX", (String)var1.get(5)});
      var2.add(new String[]{"REMOTE HOST", (String)var1.get(6)});
      var2.add(new String[]{"LOCAL ADDRESS", (String)var1.get(7)});
      var2.add(new String[]{"LOCAL HEX", (String)var1.get(8)});
      var2.add(new String[]{"", ""});
      var2.add(new String[]{"SERVICE PORT", (String)var1.get(9)});
      var2.add(new String[]{"PAYLOAD PROTOCOL", (String)var1.get(10)});
      var2.add(new String[]{"TRANSPORT PROTOCOL", (String)var1.get(11)});
      var2.add(new String[]{"LOCAL PORT", (String)var1.get(12)});
      var2.add(new String[]{"", ""});
      var2.add(new String[]{"TIMESTAMP", (String)var1.get(13)});
      var2.add(new String[]{"CONNECTION INFO", (String)var1.get(14)});
      return var2;
   }

   public class DetailAdapter extends ArrayAdapter {
      DetailAdapter(Context var2, int var3, List var4) {
         super(var2, var3, var4);
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         HistoryDetailActivity.this.mDrawerLayout.setDrawerLockMode(1);
         View var7 = var2;
         if (var2 == null) {
            var7 = LayoutInflater.from(this.getContext()).inflate(2131427411, (ViewGroup)null);
         }

         String[] var4 = (String[])this.getItem(var1);
         TextView var5 = (TextView)var7.findViewById(2131296444);
         TextView var6 = (TextView)var7.findViewById(2131296445);
         if (var4[0] != null && var4[1] != null) {
            var5.setText(var4[0]);
            var6.setText(var4[1]);
         } else {
            var5.setText("");
            var6.setText("");
         }

         return var7;
      }
   }
}
