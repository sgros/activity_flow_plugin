package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.secuso.privacyfriendlynetmonitor.C0501R;

class HelpDataDump {
    private Context context;

    HelpDataDump(Context context) {
        this.context = context;
    }

    /* Access modifiers changed, original: 0000 */
    public HashMap<String, List<String>> getDataGeneral() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_whatis_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_whatis), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_feature_one_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_feature_one), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_feature_two_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_feature_two), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_feature_three_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_feature_three), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_feature_four_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_feature_four), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_feature_five_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_feature_five), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_privacy_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_privacy), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_permission_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_permission), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_sysuser_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_sysuser), arrayList);
        arrayList = new ArrayList();
        arrayList.add(this.context.getResources().getString(C0501R.string.help_un_encrypted_ports_answer));
        linkedHashMap.put(this.context.getResources().getString(C0501R.string.help_un_encrypted_ports), arrayList);
        return linkedHashMap;
    }
}
