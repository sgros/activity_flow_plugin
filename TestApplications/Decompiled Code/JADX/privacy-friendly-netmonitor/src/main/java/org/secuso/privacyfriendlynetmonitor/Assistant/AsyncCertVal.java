package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import p004de.bjoernr.ssllabs.Api;
import p004de.bjoernr.ssllabs.ConsoleUtilities;

public class AsyncCertVal extends AsyncTask<Void, Void, Void> {
    private Api mSSLLabsApi = new Api();

    public Void doInBackground(Void... voidArr) {
        if (Collector.sCertValList.size() > 0) {
            fetchHostInfo(Collector.sCertValList);
        }
        return null;
    }

    private void fetchHostInfo(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (int maxAssessments = getMaxAssessments(); maxAssessments > 0 && list.size() > 0; maxAssessments--) {
            Map jsonToMap;
            String str = (String) list.get(0);
            try {
                jsonToMap = ConsoleUtilities.jsonToMap(this.mSSLLabsApi.fetchHostInformationCached(str, null, false, false));
            } catch (JSONException unused) {
                jsonToMap = null;
            }
            if (jsonToMap != null && jsonToMap.size() > 0) {
                Collector.mCertValMap.put(str, jsonToMap);
            }
            if (!(jsonToMap == null || jsonToMap.size() <= 0 || Collector.analyseReady(jsonToMap))) {
                arrayList.add(str);
            }
            list.remove(0);
        }
        Collector.sCertValList.addAll(arrayList);
        Collector.updateCertHostHandler();
    }

    private int getMaxAssessments() {
        Map jsonToMap;
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(this.mSSLLabsApi.fetchApiInfo());
        } catch (JSONException unused) {
            jsonToMap = null;
        }
        return jsonToMap.containsKey("maxAssessments") ? ((Integer) jsonToMap.get("maxAssessments")).intValue() : 0;
    }
}
