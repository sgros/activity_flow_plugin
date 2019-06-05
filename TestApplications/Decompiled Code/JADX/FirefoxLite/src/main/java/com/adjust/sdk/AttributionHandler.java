package com.adjust.sdk;

import android.net.Uri;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

public class AttributionHandler implements IAttributionHandler {
    private static final String ATTRIBUTION_TIMER_NAME = "Attribution timer";
    private WeakReference<IActivityHandler> activityHandlerWeakRef;
    private ActivityPackage attributionPackage;
    private ILogger logger = AdjustFactory.getLogger();
    private boolean paused;
    private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("AttributionHandler", false);
    private TimerOnce timer = new TimerOnce(new C03291(), ATTRIBUTION_TIMER_NAME);

    /* renamed from: com.adjust.sdk.AttributionHandler$1 */
    class C03291 implements Runnable {
        C03291() {
        }

        public void run() {
            AttributionHandler.this.sendAttributionRequest();
        }
    }

    /* renamed from: com.adjust.sdk.AttributionHandler$2 */
    class C03302 implements Runnable {
        C03302() {
        }

        public void run() {
            AttributionHandler.this.getAttributionI(0);
        }
    }

    /* renamed from: com.adjust.sdk.AttributionHandler$6 */
    class C03346 implements Runnable {
        C03346() {
        }

        public void run() {
            AttributionHandler.this.sendAttributionRequestI();
        }
    }

    public void teardown() {
        this.logger.verbose("AttributionHandler teardown", new Object[0]);
        if (this.timer != null) {
            this.timer.teardown();
        }
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            } catch (SecurityException unused) {
            }
        }
        if (this.activityHandlerWeakRef != null) {
            this.activityHandlerWeakRef.clear();
        }
        this.scheduledExecutor = null;
        this.activityHandlerWeakRef = null;
        this.logger = null;
        this.attributionPackage = null;
        this.timer = null;
    }

    public AttributionHandler(IActivityHandler iActivityHandler, ActivityPackage activityPackage, boolean z) {
        init(iActivityHandler, activityPackage, z);
    }

    public void init(IActivityHandler iActivityHandler, ActivityPackage activityPackage, boolean z) {
        this.activityHandlerWeakRef = new WeakReference(iActivityHandler);
        this.attributionPackage = activityPackage;
        this.paused = z ^ 1;
    }

    public void getAttribution() {
        this.scheduledExecutor.submit(new C03302());
    }

    public void checkSessionResponse(final SessionResponseData sessionResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                IActivityHandler iActivityHandler = (IActivityHandler) AttributionHandler.this.activityHandlerWeakRef.get();
                if (iActivityHandler != null) {
                    AttributionHandler.this.checkSessionResponseI(iActivityHandler, sessionResponseData);
                }
            }
        });
    }

    public void checkSdkClickResponse(final SdkClickResponseData sdkClickResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                IActivityHandler iActivityHandler = (IActivityHandler) AttributionHandler.this.activityHandlerWeakRef.get();
                if (iActivityHandler != null) {
                    AttributionHandler.this.checkSdkClickResponseI(iActivityHandler, sdkClickResponseData);
                }
            }
        });
    }

    public void checkAttributionResponse(final AttributionResponseData attributionResponseData) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                IActivityHandler iActivityHandler = (IActivityHandler) AttributionHandler.this.activityHandlerWeakRef.get();
                if (iActivityHandler != null) {
                    AttributionHandler.this.checkAttributionResponseI(iActivityHandler, attributionResponseData);
                }
            }
        });
    }

    public void pauseSending() {
        this.paused = true;
    }

    public void resumeSending() {
        this.paused = false;
    }

    public void sendAttributionRequest() {
        this.scheduledExecutor.submit(new C03346());
    }

    private void getAttributionI(long j) {
        if (this.timer.getFireIn() <= j) {
            if (j != 0) {
                String format = Util.SecondsDisplayFormat.format(((double) j) / 1000.0d);
                this.logger.debug("Waiting to query attribution in %s seconds", format);
            }
            this.timer.startIn(j);
        }
    }

    private void checkAttributionI(IActivityHandler iActivityHandler, ResponseData responseData) {
        if (responseData.jsonResponse != null) {
            long optLong = responseData.jsonResponse.optLong("ask_in", -1);
            if (optLong >= 0) {
                iActivityHandler.setAskingAttribution(true);
                getAttributionI(optLong);
                return;
            }
            iActivityHandler.setAskingAttribution(false);
            responseData.attribution = AdjustAttribution.fromJson(responseData.jsonResponse.optJSONObject("attribution"), responseData.adid);
        }
    }

    private void checkSessionResponseI(IActivityHandler iActivityHandler, SessionResponseData sessionResponseData) {
        checkAttributionI(iActivityHandler, sessionResponseData);
        iActivityHandler.launchSessionResponseTasks(sessionResponseData);
    }

    private void checkSdkClickResponseI(IActivityHandler iActivityHandler, SdkClickResponseData sdkClickResponseData) {
        checkAttributionI(iActivityHandler, sdkClickResponseData);
        iActivityHandler.launchSdkClickResponseTasks(sdkClickResponseData);
    }

    private void checkAttributionResponseI(IActivityHandler iActivityHandler, AttributionResponseData attributionResponseData) {
        checkAttributionI(iActivityHandler, attributionResponseData);
        checkDeeplinkI(attributionResponseData);
        iActivityHandler.launchAttributionResponseTasks(attributionResponseData);
    }

    private void checkDeeplinkI(AttributionResponseData attributionResponseData) {
        if (attributionResponseData.jsonResponse != null) {
            JSONObject optJSONObject = attributionResponseData.jsonResponse.optJSONObject("attribution");
            if (optJSONObject != null) {
                String optString = optJSONObject.optString(Constants.DEEPLINK, null);
                if (optString != null) {
                    attributionResponseData.deeplink = Uri.parse(optString);
                }
            }
        }
    }

    private void sendAttributionRequestI() {
        if (this.paused) {
            this.logger.debug("Attribution handler is paused", new Object[0]);
            return;
        }
        this.logger.verbose("%s", this.attributionPackage.getExtendedString());
        try {
            ResponseData createGETHttpsURLConnection = UtilNetworking.createGETHttpsURLConnection(this.attributionPackage);
            if (createGETHttpsURLConnection instanceof AttributionResponseData) {
                checkAttributionResponse((AttributionResponseData) createGETHttpsURLConnection);
            }
        } catch (Exception e) {
            this.logger.error("Failed to get attribution (%s)", e.getMessage());
        }
    }
}
