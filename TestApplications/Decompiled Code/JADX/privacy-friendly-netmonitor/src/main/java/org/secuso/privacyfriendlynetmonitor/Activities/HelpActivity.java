package org.secuso.privacyfriendlynetmonitor.Activities;

import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableListAdapter;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class HelpActivity extends BaseActivity {
    /* Access modifiers changed, original: protected */
    public int getNavigationDrawerID() {
        return C0501R.C0499id.nav_help;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0501R.layout.activity_help);
        RunStore.setContext(this);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(C0501R.C0499id.generalExpandableListView);
        HashMap dataGeneral = new HelpDataDump(this).getDataGeneral();
        expandableListView.setAdapter(new ExpandableListAdapter(this, new ArrayList(dataGeneral.keySet()), dataGeneral));
        overridePendingTransition(0, 0);
    }
}
