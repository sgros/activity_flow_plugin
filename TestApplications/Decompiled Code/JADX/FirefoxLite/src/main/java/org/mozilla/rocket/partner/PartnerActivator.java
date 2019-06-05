package org.mozilla.rocket.partner;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.text.TextUtils;
import com.adjust.sdk.Constants;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.httprequest.HttpRequest;

public class PartnerActivator {
    private static Executor executor = Executors.newCachedThreadPool();
    private Activation activation;
    private final Context context;
    private String[] partnerActivateKeys;

    private static class ActivationJobs {
        final PartnerActivator partnerActivator;

        ActivationJobs(PartnerActivator partnerActivator) {
            this.partnerActivator = partnerActivator;
        }
    }

    public enum Status {
        Disabled,
        Default,
        Done,
        Snooze
    }

    private static final class FetchActivation extends ActivationJobs implements Runnable {
        private final String[] activationKeys;
        private final String sourceUrl;

        FetchActivation(PartnerActivator partnerActivator, String str) {
            super(partnerActivator);
            this.sourceUrl = str;
            this.activationKeys = partnerActivator.partnerActivateKeys;
        }

        public void run() {
            try {
                TrafficStats.setThreadStatsTag(Constants.ONE_SECOND);
                JSONArray jSONArray = new JSONObject(HttpRequest.get(new URL(this.sourceUrl), 30000, "")).getJSONArray("data");
                Activation activation = null;
                JSONArray jSONArray2 = null;
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        jSONArray2 = ((JSONObject) jSONArray.get(i)).getJSONObject("partner").getJSONArray("activation");
                    } catch (JSONException e) {
                        PartnerUtil.log(e, "FetchActivation source json format error");
                    }
                }
                if (jSONArray2 == null) {
                    PartnerUtil.log("FetchActivation activation json not found");
                    TrafficStats.clearThreadStatsTag();
                    return;
                }
                int length = jSONArray2.length();
                for (int i2 = 0; i2 < length; i2++) {
                    Activation from = Activation.from((JSONObject) jSONArray2.get(i2));
                    if (from.matchKeys(this.activationKeys)) {
                        activation = from;
                        break;
                    }
                }
                if (activation == null) {
                    PartnerUtil.log("FetchActivation activation not found, disabled");
                    this.partnerActivator.setStatus(Status.Disabled);
                    TrafficStats.clearThreadStatsTag();
                    return;
                }
                this.partnerActivator.activation = activation;
                if (activation.duration != 0) {
                    this.partnerActivator.setSnoozeDuration(activation.duration);
                }
                if (this.partnerActivator.inSnooze()) {
                    this.partnerActivator.setStatus(Status.Snooze);
                    PartnerUtil.log("FetchActivation update snoozed");
                    TrafficStats.clearThreadStatsTag();
                    return;
                }
                this.partnerActivator.setLastCheckedTimestamp(System.currentTimeMillis());
                this.partnerActivator.postWorker(new PingActivation(this.partnerActivator));
                TrafficStats.clearThreadStatsTag();
            } catch (Exception e2) {
                PartnerUtil.log(e2, "FetchActivation Exception");
            } catch (Throwable th) {
                TrafficStats.clearThreadStatsTag();
            }
        }
    }

    private static class PingActivation extends ActivationJobs implements Runnable {
        PingActivation(PartnerActivator partnerActivator) {
            super(partnerActivator);
        }

        public void run() {
            URL url;
            Throwable th;
            Activation access$500 = this.partnerActivator.activation;
            try {
                url = new URL(access$500.url);
            } catch (MalformedURLException e) {
                PartnerUtil.log(e, "PingActivation URL malformed");
                url = null;
            }
            if (url == null) {
                PartnerUtil.log("PingActivation URL not found");
                return;
            }
            try {
                TrafficStats.setThreadStatsTag(Constants.ONE_SECOND);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject.put("fields", jSONObject2);
                jSONObject2.put("device_id", new JSONObject().put("stringValue", PartnerUtil.getDeviceIdentifier(this.partnerActivator.context.getContentResolver())));
                jSONObject2.put("owner", new JSONObject().put("stringValue", access$500.owner));
                jSONObject2.put("version", new JSONObject().put("integerValue", access$500.version));
                jSONObject2.put("manufacture", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.product.manufacturer")));
                jSONObject2.put("model", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.product.model")));
                jSONObject2.put("name", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.product.name")));
                jSONObject2.put("device", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.product.device")));
                jSONObject2.put("brand", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.product.brand")));
                jSONObject2.put("build_id", new JSONObject().put("stringValue", PartnerUtil.getProperty("ro.build.id")));
                byte[] bytes = jSONObject.toString().getBytes(StandardCharsets.UTF_8);
                OutputStream outputStream;
                try {
                    outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode != 200) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("PingActivation server response not OK: ");
                        stringBuilder.append(responseCode);
                        PartnerUtil.log(stringBuilder.toString());
                    } else {
                        this.partnerActivator.setStatus(Status.Done);
                    }
                } catch (Exception e2) {
                    PartnerUtil.log(e2, "PingActivation post exception");
                    throw e2;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } catch (Exception e22) {
                try {
                    PartnerUtil.log(e22, "PingActivation Exception");
                } catch (Throwable th3) {
                    TrafficStats.clearThreadStatsTag();
                }
            }
            TrafficStats.clearThreadStatsTag();
        }
    }

    private static final class QueryActivationStatus extends ActivationJobs implements Runnable {
        QueryActivationStatus(PartnerActivator partnerActivator) {
            super(partnerActivator);
        }

        public void run() {
            if (!this.partnerActivator.statusInvalidate(this.partnerActivator.getStatus())) {
                String property = PartnerUtil.getProperty("ro.vendor.partner");
                if (TextUtils.isEmpty(property)) {
                    PartnerUtil.log("partner key not found, disabled");
                    this.partnerActivator.setStatus(Status.Disabled);
                    return;
                }
                this.partnerActivator.partnerActivateKeys = property.split("/");
                if (this.partnerActivator.partnerActivateKeys == null || this.partnerActivator.partnerActivateKeys.length != 3) {
                    PartnerUtil.log("partner key format invalid");
                } else {
                    this.partnerActivator.postWorker(new FetchActivation(this.partnerActivator, "https://firefox.settings.services.mozilla.com/v1/buckets/main/collections/rocket-prefs/records"));
                }
            }
        }
    }

    public PartnerActivator(Context context) {
        this.context = context.getApplicationContext();
    }

    public void launch() {
        postWorker(new QueryActivationStatus(this));
    }

    private void postWorker(Runnable runnable) {
        executor.execute(runnable);
    }

    private boolean statusInvalidate(Status status) {
        switch (status) {
            case Disabled:
            case Done:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("status: ");
                stringBuilder.append(status);
                PartnerUtil.log(stringBuilder.toString());
                return true;
            case Snooze:
                if (inSnooze()) {
                    PartnerUtil.log("status: inSnooze");
                    return true;
                }
                setStatus(Status.Default);
                return false;
            default:
                return false;
        }
    }

    private Status getStatus() {
        String string = getPreferences(this.context).getString("string_activation_status", Status.Default.toString());
        if (TextUtils.isEmpty(string)) {
            return Status.Default;
        }
        try {
            return Status.valueOf(string);
        } catch (Exception unused) {
            return Status.Default;
        }
    }

    private boolean inSnooze() {
        long lastFetchedTimestamp = getLastFetchedTimestamp();
        long snoozeDuration = getSnoozeDuration();
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = false;
        if (lastFetchedTimestamp <= 0 || snoozeDuration <= 0) {
            return false;
        }
        if (lastFetchedTimestamp + snoozeDuration >= currentTimeMillis) {
            z = true;
        }
        return z;
    }

    private void setStatus(Status status) {
        getPreferences(this.context).edit().putString("string_activation_status", status.toString()).apply();
    }

    private long getSnoozeDuration() {
        return getPreferences(this.context).getLong("long_snooze_duration", 0);
    }

    private void setSnoozeDuration(long j) {
        getPreferences(this.context).edit().putLong("long_snooze_duration", j).apply();
    }

    private long getLastFetchedTimestamp() {
        return getPreferences(this.context).getLong("long_fetch_timestamp", 0);
    }

    private void setLastCheckedTimestamp(long j) {
        getPreferences(this.context).edit().putLong("long_fetch_timestamp", j).apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("partner_activator", 0);
    }
}
