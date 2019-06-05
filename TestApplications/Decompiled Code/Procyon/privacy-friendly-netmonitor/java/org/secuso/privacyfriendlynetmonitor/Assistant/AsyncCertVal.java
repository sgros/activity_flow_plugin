// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import java.util.Map;
import org.json.JSONObject;
import java.util.Collection;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;
import org.json.JSONException;
import de.bjoernr.ssllabs.ConsoleUtilities;
import java.util.ArrayList;
import java.util.List;
import de.bjoernr.ssllabs.Api;
import android.os.AsyncTask;

public class AsyncCertVal extends AsyncTask<Void, Void, Void>
{
    private Api mSSLLabsApi;
    
    public AsyncCertVal() {
        this.mSSLLabsApi = new Api();
    }
    
    private void fetchHostInfo(final List<String> list) {
        int maxAssessments = this.getMaxAssessments();
        final ArrayList<String> list2 = new ArrayList<String>();
        while (maxAssessments > 0 && list.size() > 0) {
            final String s = list.get(0);
            final JSONObject fetchHostInformationCached = this.mSSLLabsApi.fetchHostInformationCached(s, null, false, false);
            Map<String, Object> jsonToMap;
            try {
                jsonToMap = ConsoleUtilities.jsonToMap(fetchHostInformationCached);
            }
            catch (JSONException ex) {
                jsonToMap = null;
            }
            if (jsonToMap != null && jsonToMap.size() > 0) {
                Collector.mCertValMap.put(s, jsonToMap);
            }
            if (jsonToMap != null && jsonToMap.size() > 0 && !Collector.analyseReady(jsonToMap)) {
                list2.add(s);
            }
            list.remove(0);
            --maxAssessments;
        }
        Collector.sCertValList.addAll(list2);
        Collector.updateCertHostHandler();
    }
    
    private int getMaxAssessments() {
        final JSONObject fetchApiInfo = this.mSSLLabsApi.fetchApiInfo();
        Object jsonToMap;
        try {
            jsonToMap = ConsoleUtilities.jsonToMap(fetchApiInfo);
        }
        catch (JSONException ex) {
            jsonToMap = null;
        }
        if (((Map)jsonToMap).containsKey("maxAssessments")) {
            return ((Map<K, Integer>)jsonToMap).get("maxAssessments");
        }
        return 0;
    }
    
    public Void doInBackground(final Void... array) {
        if (Collector.sCertValList.size() > 0) {
            this.fetchHostInfo(Collector.sCertValList);
        }
        return null;
    }
}
