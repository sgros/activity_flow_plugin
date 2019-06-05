// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.partner;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.httprequest.HttpRequest;
import java.net.URL;
import android.net.TrafficStats;
import android.text.TextUtils;
import android.content.SharedPreferences;
import java.util.concurrent.Executors;
import android.content.Context;
import java.util.concurrent.Executor;

public class PartnerActivator
{
    private static Executor executor;
    private Activation activation;
    private final Context context;
    private String[] partnerActivateKeys;
    
    static {
        PartnerActivator.executor = Executors.newCachedThreadPool();
    }
    
    public PartnerActivator(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    private long getLastFetchedTimestamp() {
        return getPreferences(this.context).getLong("long_fetch_timestamp", 0L);
    }
    
    private static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences("partner_activator", 0);
    }
    
    private long getSnoozeDuration() {
        return getPreferences(this.context).getLong("long_snooze_duration", 0L);
    }
    
    private Status getStatus() {
        final String string = getPreferences(this.context).getString("string_activation_status", Status.Default.toString());
        Status status;
        if (TextUtils.isEmpty((CharSequence)string)) {
            status = Status.Default;
        }
        else {
            try {
                status = Status.valueOf(string);
            }
            catch (Exception ex) {
                status = Status.Default;
            }
        }
        return status;
    }
    
    private boolean inSnooze() {
        final long lastFetchedTimestamp = this.getLastFetchedTimestamp();
        final long snoozeDuration = this.getSnoozeDuration();
        final long currentTimeMillis = System.currentTimeMillis();
        boolean b = false;
        if (lastFetchedTimestamp > 0L && snoozeDuration > 0L) {
            if (lastFetchedTimestamp + snoozeDuration >= currentTimeMillis) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    private void postWorker(final Runnable runnable) {
        PartnerActivator.executor.execute(runnable);
    }
    
    private void setLastCheckedTimestamp(final long n) {
        getPreferences(this.context).edit().putLong("long_fetch_timestamp", n).apply();
    }
    
    private void setSnoozeDuration(final long n) {
        getPreferences(this.context).edit().putLong("long_snooze_duration", n).apply();
    }
    
    private void setStatus(final Status status) {
        getPreferences(this.context).edit().putString("string_activation_status", status.toString()).apply();
    }
    
    private boolean statusInvalidate(final Status obj) {
        switch (PartnerActivator$1.$SwitchMap$org$mozilla$rocket$partner$PartnerActivator$Status[obj.ordinal()]) {
            default: {
                return false;
            }
            case 3: {
                if (this.inSnooze()) {
                    PartnerUtil.log("status: inSnooze");
                    return true;
                }
                this.setStatus(Status.Default);
                return false;
            }
            case 1:
            case 2: {
                final StringBuilder sb = new StringBuilder();
                sb.append("status: ");
                sb.append(obj);
                PartnerUtil.log(sb.toString());
                return true;
            }
        }
    }
    
    public void launch() {
        this.postWorker(new QueryActivationStatus(this));
    }
    
    private static class ActivationJobs
    {
        final PartnerActivator partnerActivator;
        
        ActivationJobs(final PartnerActivator partnerActivator) {
            this.partnerActivator = partnerActivator;
        }
    }
    
    private static final class FetchActivation extends ActivationJobs implements Runnable
    {
        private final String[] activationKeys;
        private final String sourceUrl;
        
        FetchActivation(final PartnerActivator partnerActivator, final String sourceUrl) {
            super(partnerActivator);
            this.sourceUrl = sourceUrl;
            this.activationKeys = partnerActivator.partnerActivateKeys;
        }
        
        @Override
        public void run() {
            try {
                try {
                    TrafficStats.setThreadStatsTag(1000);
                    final JSONArray jsonArray = new JSONObject(HttpRequest.get(new URL(this.sourceUrl), 30000, "")).getJSONArray("data");
                    final int n = 0;
                    final Activation activation = null;
                    JSONArray jsonArray2 = null;
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        try {
                            jsonArray2 = ((JSONObject)jsonArray.get(i)).getJSONObject("partner").getJSONArray("activation");
                        }
                        catch (JSONException ex) {
                            PartnerUtil.log((Throwable)ex, "FetchActivation source json format error");
                        }
                    }
                    if (jsonArray2 == null) {
                        PartnerUtil.log("FetchActivation activation json not found");
                        TrafficStats.clearThreadStatsTag();
                        return;
                    }
                    final int length = jsonArray2.length();
                    int n2 = n;
                    Activation from;
                    while (true) {
                        from = activation;
                        if (n2 >= length) {
                            break;
                        }
                        from = Activation.from((JSONObject)jsonArray2.get(n2));
                        if (from.matchKeys(this.activationKeys)) {
                            break;
                        }
                        ++n2;
                    }
                    if (from == null) {
                        PartnerUtil.log("FetchActivation activation not found, disabled");
                        this.partnerActivator.setStatus(Status.Disabled);
                        TrafficStats.clearThreadStatsTag();
                        return;
                    }
                    this.partnerActivator.activation = from;
                    if (from.duration != 0L) {
                        this.partnerActivator.setSnoozeDuration(from.duration);
                    }
                    if (this.partnerActivator.inSnooze()) {
                        this.partnerActivator.setStatus(Status.Snooze);
                        PartnerUtil.log("FetchActivation update snoozed");
                        TrafficStats.clearThreadStatsTag();
                        return;
                    }
                    this.partnerActivator.setLastCheckedTimestamp(System.currentTimeMillis());
                    this.partnerActivator.postWorker(new PingActivation(this.partnerActivator));
                }
                finally {}
            }
            catch (Exception ex2) {
                PartnerUtil.log(ex2, "FetchActivation Exception");
            }
            TrafficStats.clearThreadStatsTag();
            return;
            TrafficStats.clearThreadStatsTag();
        }
    }
    
    private static class PingActivation extends ActivationJobs implements Runnable
    {
        PingActivation(final PartnerActivator partnerActivator) {
            super(partnerActivator);
        }
        
        @Override
        public void run() {
            final Activation access$500 = this.partnerActivator.activation;
            final StringBuilder sb = null;
            URL url;
            try {
                url = new URL(access$500.url);
            }
            catch (MalformedURLException ex) {
                PartnerUtil.log(ex, "PingActivation URL malformed");
                url = null;
            }
            if (url == null) {
                PartnerUtil.log("PingActivation URL not found");
                return;
            }
            Label_0555: {
                Label_0551: {
                    try {
                        TrafficStats.setThreadStatsTag(1000);
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.setConnectTimeout(60000);
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        final JSONObject jsonObject = new JSONObject();
                        final JSONObject jsonObject2 = new JSONObject();
                        jsonObject.put("fields", (Object)jsonObject2);
                        jsonObject2.put("device_id", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getDeviceIdentifier(this.partnerActivator.context.getContentResolver())));
                        jsonObject2.put("owner", (Object)new JSONObject().put("stringValue", (Object)access$500.owner));
                        jsonObject2.put("version", (Object)new JSONObject().put("integerValue", access$500.version));
                        jsonObject2.put("manufacture", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.product.manufacturer")));
                        jsonObject2.put("model", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.product.model")));
                        jsonObject2.put("name", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.product.name")));
                        jsonObject2.put("device", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.product.device")));
                        jsonObject2.put("brand", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.product.brand")));
                        jsonObject2.put("build_id", (Object)new JSONObject().put("stringValue", (Object)PartnerUtil.getProperty("ro.build.id")));
                        final byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                        try {
                            final OutputStream outputStream = httpURLConnection.getOutputStream();
                            StringBuilder sb2 = sb;
                            try {
                                try {
                                    outputStream.write(bytes);
                                    sb2 = sb;
                                    outputStream.flush();
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    final int responseCode = httpURLConnection.getResponseCode();
                                    if (responseCode != 200) {
                                        sb2 = new StringBuilder();
                                        sb2.append("PingActivation server response not OK: ");
                                        sb2.append(responseCode);
                                        PartnerUtil.log(sb2.toString());
                                        break Label_0551;
                                    }
                                    this.partnerActivator.setStatus(Status.Done);
                                    break Label_0551;
                                }
                                finally {
                                    if (outputStream != null) {
                                        if (sb2 != null) {
                                            final OutputStream outputStream2 = outputStream;
                                            outputStream2.close();
                                        }
                                        else {
                                            outputStream.close();
                                        }
                                    }
                                }
                            }
                            catch (Throwable t) {}
                            try {
                                final OutputStream outputStream2 = outputStream;
                                outputStream2.close();
                            }
                            catch (Throwable t2) {}
                        }
                        catch (Exception ex2) {
                            PartnerUtil.log(ex2, "PingActivation post exception");
                            throw ex2;
                        }
                    }
                    catch (Exception ex3) {
                        PartnerUtil.log(ex3, "PingActivation Exception");
                    }
                    finally {
                        break Label_0555;
                    }
                }
                TrafficStats.clearThreadStatsTag();
                return;
            }
            TrafficStats.clearThreadStatsTag();
        }
    }
    
    private static final class QueryActivationStatus extends ActivationJobs implements Runnable
    {
        QueryActivationStatus(final PartnerActivator partnerActivator) {
            super(partnerActivator);
        }
        
        @Override
        public void run() {
            if (this.partnerActivator.statusInvalidate(this.partnerActivator.getStatus())) {
                return;
            }
            final String property = PartnerUtil.getProperty("ro.vendor.partner");
            if (TextUtils.isEmpty((CharSequence)property)) {
                PartnerUtil.log("partner key not found, disabled");
                this.partnerActivator.setStatus(Status.Disabled);
                return;
            }
            this.partnerActivator.partnerActivateKeys = property.split("/");
            if (this.partnerActivator.partnerActivateKeys != null && this.partnerActivator.partnerActivateKeys.length == 3) {
                this.partnerActivator.postWorker(new FetchActivation(this.partnerActivator, "https://firefox.settings.services.mozilla.com/v1/buckets/main/collections/rocket-prefs/records"));
                return;
            }
            PartnerUtil.log("partner key format invalid");
        }
    }
    
    public enum Status
    {
        Default, 
        Disabled, 
        Done, 
        Snooze;
    }
}
