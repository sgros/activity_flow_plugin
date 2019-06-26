// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import java.util.HashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.Activities.Adapter.ExpandableListAdapter;
import java.util.Collection;
import java.util.ArrayList;
import android.widget.ExpandableListView;
import android.content.Context;
import android.app.Activity;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.os.Bundle;

public class HelpActivity extends BaseActivity
{
    @Override
    protected int getNavigationDrawerID() {
        return 2131296407;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427356);
        RunStore.setContext(this);
        final HelpDataDump helpDataDump = new HelpDataDump((Context)this);
        final ExpandableListView expandableListView = this.findViewById(2131296357);
        final HashMap<String, List<String>> dataGeneral = helpDataDump.getDataGeneral();
        expandableListView.setAdapter((android.widget.ExpandableListAdapter)new ExpandableListAdapter((Context)this, new ArrayList<String>(dataGeneral.keySet()), dataGeneral));
        this.overridePendingTransition(0, 0);
    }
}
