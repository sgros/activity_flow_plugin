package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableListAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class HelpActivity extends BaseActivity {
   protected int getNavigationDrawerID() {
      return 2131296407;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427356);
      RunStore.setContext(this);
      HelpDataDump var2 = new HelpDataDump(this);
      ExpandableListView var3 = (ExpandableListView)this.findViewById(2131296357);
      HashMap var4 = var2.getDataGeneral();
      var3.setAdapter(new ExpandableListAdapter(this, new ArrayList(var4.keySet()), var4));
      this.overridePendingTransition(0, 0);
   }
}
