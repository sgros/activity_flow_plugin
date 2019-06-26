package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

class HelpDataDump {
   private Context context;

   HelpDataDump(Context var1) {
      this.context = var1;
   }

   HashMap getDataGeneral() {
      LinkedHashMap var1 = new LinkedHashMap();
      ArrayList var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624021));
      var1.put(this.context.getResources().getString(2131624020), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624005));
      var1.put(this.context.getResources().getString(2131624004), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624009));
      var1.put(this.context.getResources().getString(2131624008), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624007));
      var1.put(this.context.getResources().getString(2131624006), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624003));
      var1.put(this.context.getResources().getString(2131624002), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624001));
      var1.put(this.context.getResources().getString(2131624000), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624013));
      var1.put(this.context.getResources().getString(2131624012), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624011));
      var1.put(this.context.getResources().getString(2131624010), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624017));
      var1.put(this.context.getResources().getString(2131624016), var2);
      var2 = new ArrayList();
      var2.add(this.context.getResources().getString(2131624019));
      var1.put(this.context.getResources().getString(2131624018), var2);
      return var1;
   }
}
