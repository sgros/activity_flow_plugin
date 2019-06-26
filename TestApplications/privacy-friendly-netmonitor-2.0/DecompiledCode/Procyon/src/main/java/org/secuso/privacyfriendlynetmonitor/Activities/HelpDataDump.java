// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import android.content.Context;

class HelpDataDump
{
    private Context context;
    
    HelpDataDump(final Context context) {
        this.context = context;
    }
    
    HashMap<String, List<String>> getDataGeneral() {
        final LinkedHashMap<String, ArrayList<String>> linkedHashMap = (LinkedHashMap<String, ArrayList<String>>)new LinkedHashMap<String, List<String>>();
        final ArrayList<String> value = new ArrayList<String>();
        value.add(this.context.getResources().getString(2131624021));
        linkedHashMap.put(this.context.getResources().getString(2131624020), value);
        final ArrayList<String> value2 = new ArrayList<String>();
        value2.add(this.context.getResources().getString(2131624005));
        linkedHashMap.put(this.context.getResources().getString(2131624004), value2);
        final ArrayList<String> value3 = new ArrayList<String>();
        value3.add(this.context.getResources().getString(2131624009));
        linkedHashMap.put(this.context.getResources().getString(2131624008), value3);
        final ArrayList<String> value4 = new ArrayList<String>();
        value4.add(this.context.getResources().getString(2131624007));
        linkedHashMap.put(this.context.getResources().getString(2131624006), value4);
        final ArrayList<String> value5 = new ArrayList<String>();
        value5.add(this.context.getResources().getString(2131624003));
        linkedHashMap.put(this.context.getResources().getString(2131624002), value5);
        final ArrayList<String> value6 = new ArrayList<String>();
        value6.add(this.context.getResources().getString(2131624001));
        linkedHashMap.put(this.context.getResources().getString(2131624000), value6);
        final ArrayList<String> value7 = new ArrayList<String>();
        value7.add(this.context.getResources().getString(2131624013));
        linkedHashMap.put(this.context.getResources().getString(2131624012), value7);
        final ArrayList<String> value8 = new ArrayList<String>();
        value8.add(this.context.getResources().getString(2131624011));
        linkedHashMap.put(this.context.getResources().getString(2131624010), value8);
        final ArrayList<String> value9 = new ArrayList<String>();
        value9.add(this.context.getResources().getString(2131624017));
        linkedHashMap.put(this.context.getResources().getString(2131624016), value9);
        final ArrayList<String> value10 = new ArrayList<String>();
        value10.add(this.context.getResources().getString(2131624019));
        linkedHashMap.put(this.context.getResources().getString(2131624018), value10);
        return (HashMap<String, List<String>>)linkedHashMap;
    }
}
