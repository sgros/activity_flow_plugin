package com.adjust.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SdkClickHandler implements ISdkClickHandler {
    private WeakReference<IActivityHandler> activityHandlerWeakRef;
    private BackoffStrategy backoffStrategy = AdjustFactory.getSdkClickBackoffStrategy();
    private ILogger logger = AdjustFactory.getLogger();
    private List<ActivityPackage> packageQueue;
    private boolean paused;
    private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("SdkClickHandler", false);

    /* renamed from: com.adjust.sdk.SdkClickHandler$2 */
    class C03472 implements Runnable {
        C03472() {
        }

        public void run() {
            SdkClickHandler.this.sendNextSdkClickI();
        }
    }

    public void teardown() {
        this.logger.verbose("SdkClickHandler teardown", new Object[0]);
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            } catch (SecurityException unused) {
            }
        }
        if (this.packageQueue != null) {
            this.packageQueue.clear();
        }
        if (this.activityHandlerWeakRef != null) {
            this.activityHandlerWeakRef.clear();
        }
        this.scheduledExecutor = null;
        this.logger = null;
        this.packageQueue = null;
        this.backoffStrategy = null;
    }

    public SdkClickHandler(IActivityHandler iActivityHandler, boolean z) {
        init(iActivityHandler, z);
    }

    public void init(IActivityHandler iActivityHandler, boolean z) {
        this.paused = z ^ 1;
        this.packageQueue = new ArrayList();
        this.activityHandlerWeakRef = new WeakReference(iActivityHandler);
    }

    public void pauseSending() {
        this.paused = true;
    }

    public void resumeSending() {
        this.paused = false;
        sendNextSdkClick();
    }

    public void sendSdkClick(final ActivityPackage activityPackage) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                SdkClickHandler.this.packageQueue.add(activityPackage);
                SdkClickHandler.this.logger.debug("Added sdk_click %d", Integer.valueOf(SdkClickHandler.this.packageQueue.size()));
                SdkClickHandler.this.logger.verbose("%s", activityPackage.getExtendedString());
                SdkClickHandler.this.sendNextSdkClick();
            }
        });
    }

    private void sendNextSdkClick() {
        this.scheduledExecutor.submit(new C03472());
    }

    private void sendNextSdkClickI() {
        if (!this.paused && !this.packageQueue.isEmpty()) {
            final ActivityPackage activityPackage = (ActivityPackage) this.packageQueue.remove(0);
            int retries = activityPackage.getRetries();
            C03483 c03483 = new Runnable() {
                public void run() {
                    SdkClickHandler.this.sendSdkClickI(activityPackage);
                    SdkClickHandler.this.sendNextSdkClick();
                }
            };
            if (retries <= 0) {
                c03483.run();
                return;
            }
            long waitingTime = Util.getWaitingTime(retries, this.backoffStrategy);
            String format = Util.SecondsDisplayFormat.format(((double) waitingTime) / 1000.0d);
            this.logger.verbose("Waiting for %s seconds before retrying sdk_click for the %d time", format, Integer.valueOf(retries));
            this.scheduledExecutor.schedule(c03483, waitingTime, TimeUnit.MILLISECONDS);
        }
    }

    private void sendSdkClickI(ActivityPackage activityPackage) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BASE_URL);
        stringBuilder.append(activityPackage.getPath());
        try {
            ResponseData createPOSTHttpsURLConnection = UtilNetworking.createPOSTHttpsURLConnection(stringBuilder.toString(), activityPackage, this.packageQueue.size() - 1);
            if (createPOSTHttpsURLConnection.jsonResponse == null) {
                retrySendingI(activityPackage);
                return;
            }
            IActivityHandler iActivityHandler = (IActivityHandler) this.activityHandlerWeakRef.get();
            if (iActivityHandler != null) {
                iActivityHandler.finishedTrackingActivity(createPOSTHttpsURLConnection);
            }
        } catch (UnsupportedEncodingException e) {
            logErrorMessageI(activityPackage, "Sdk_click failed to encode parameters", e);
        } catch (SocketTimeoutException e2) {
            logErrorMessageI(activityPackage, "Sdk_click request timed out. Will retry later", e2);
            retrySendingI(activityPackage);
        } catch (IOException e3) {
            logErrorMessageI(activityPackage, "Sdk_click request failed. Will retry later", e3);
            retrySendingI(activityPackage);
        } catch (Throwable th) {
            logErrorMessageI(activityPackage, "Sdk_click runtime exception", th);
        }
    }

    private void retrySendingI(ActivityPackage activityPackage) {
        int increaseRetries = activityPackage.increaseRetries();
        this.logger.error("Retrying sdk_click package for the %d time", Integer.valueOf(increaseRetries));
        sendSdkClick(activityPackage);
    }

    private void logErrorMessageI(ActivityPackage activityPackage, String str, Throwable th) {
        String failureMessage = activityPackage.getFailureMessage();
        str = Util.getReasonString(str, th);
        this.logger.error(String.format("%s. (%s)", new Object[]{failureMessage, str}), new Object[0]);
    }
}
