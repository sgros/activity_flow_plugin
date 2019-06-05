// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.lang.ref.WeakReference;

public class SdkClickHandler implements ISdkClickHandler
{
    private WeakReference<IActivityHandler> activityHandlerWeakRef;
    private BackoffStrategy backoffStrategy;
    private ILogger logger;
    private List<ActivityPackage> packageQueue;
    private boolean paused;
    private CustomScheduledExecutor scheduledExecutor;
    
    public SdkClickHandler(final IActivityHandler activityHandler, final boolean b) {
        this.init(activityHandler, b);
        this.logger = AdjustFactory.getLogger();
        this.scheduledExecutor = new CustomScheduledExecutor("SdkClickHandler", false);
        this.backoffStrategy = AdjustFactory.getSdkClickBackoffStrategy();
    }
    
    private void logErrorMessageI(final ActivityPackage activityPackage, final String s, final Throwable t) {
        this.logger.error(String.format("%s. (%s)", activityPackage.getFailureMessage(), Util.getReasonString(s, t)), new Object[0]);
    }
    
    private void retrySendingI(final ActivityPackage activityPackage) {
        this.logger.error("Retrying sdk_click package for the %d time", activityPackage.increaseRetries());
        this.sendSdkClick(activityPackage);
    }
    
    private void sendNextSdkClick() {
        this.scheduledExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SdkClickHandler.this.sendNextSdkClickI();
            }
        });
    }
    
    private void sendNextSdkClickI() {
        if (this.paused) {
            return;
        }
        if (this.packageQueue.isEmpty()) {
            return;
        }
        final ActivityPackage activityPackage = this.packageQueue.remove(0);
        final int retries = activityPackage.getRetries();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SdkClickHandler.this.sendSdkClickI(activityPackage);
                SdkClickHandler.this.sendNextSdkClick();
            }
        };
        if (retries <= 0) {
            runnable.run();
            return;
        }
        final long waitingTime = Util.getWaitingTime(retries, this.backoffStrategy);
        this.logger.verbose("Waiting for %s seconds before retrying sdk_click for the %d time", Util.SecondsDisplayFormat.format(waitingTime / 1000.0), retries);
        this.scheduledExecutor.schedule(runnable, waitingTime, TimeUnit.MILLISECONDS);
    }
    
    private void sendSdkClickI(final ActivityPackage activityPackage) {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://app.adjust.com");
        sb.append(activityPackage.getPath());
        final String string = sb.toString();
        try {
            final ResponseData postHttpsURLConnection = UtilNetworking.createPOSTHttpsURLConnection(string, activityPackage, this.packageQueue.size() - 1);
            if (postHttpsURLConnection.jsonResponse == null) {
                this.retrySendingI(activityPackage);
                return;
            }
            final IActivityHandler activityHandler = this.activityHandlerWeakRef.get();
            if (activityHandler == null) {
                return;
            }
            activityHandler.finishedTrackingActivity(postHttpsURLConnection);
        }
        catch (Throwable t) {
            this.logErrorMessageI(activityPackage, "Sdk_click runtime exception", t);
        }
        catch (IOException ex) {
            this.logErrorMessageI(activityPackage, "Sdk_click request failed. Will retry later", ex);
            this.retrySendingI(activityPackage);
        }
        catch (SocketTimeoutException ex2) {
            this.logErrorMessageI(activityPackage, "Sdk_click request timed out. Will retry later", ex2);
            this.retrySendingI(activityPackage);
        }
        catch (UnsupportedEncodingException ex3) {
            this.logErrorMessageI(activityPackage, "Sdk_click failed to encode parameters", ex3);
        }
    }
    
    @Override
    public void init(final IActivityHandler referent, final boolean b) {
        this.paused = (b ^ true);
        this.packageQueue = new ArrayList<ActivityPackage>();
        this.activityHandlerWeakRef = new WeakReference<IActivityHandler>(referent);
    }
    
    @Override
    public void pauseSending() {
        this.paused = true;
    }
    
    @Override
    public void resumeSending() {
        this.paused = false;
        this.sendNextSdkClick();
    }
    
    @Override
    public void sendSdkClick(final ActivityPackage activityPackage) {
        this.scheduledExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SdkClickHandler.this.packageQueue.add(activityPackage);
                SdkClickHandler.this.logger.debug("Added sdk_click %d", SdkClickHandler.this.packageQueue.size());
                SdkClickHandler.this.logger.verbose("%s", activityPackage.getExtendedString());
                SdkClickHandler.this.sendNextSdkClick();
            }
        });
    }
    
    @Override
    public void teardown() {
        this.logger.verbose("SdkClickHandler teardown", new Object[0]);
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            }
            catch (SecurityException ex) {}
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
}
