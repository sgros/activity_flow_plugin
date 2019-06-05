// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class RequestHandler implements IRequestHandler
{
    private ILogger logger;
    private WeakReference<IPackageHandler> packageHandlerWeakRef;
    private CustomScheduledExecutor scheduledExecutor;
    
    public RequestHandler(final IPackageHandler packageHandler) {
        this.logger = AdjustFactory.getLogger();
        this.scheduledExecutor = new CustomScheduledExecutor("RequestHandler", false);
        this.init(packageHandler);
    }
    
    private void closePackageI(final ActivityPackage activityPackage, final String s, final Throwable t) {
        final String format = String.format("%s. (%s) Will retry later", activityPackage.getFailureMessage(), Util.getReasonString(s, t));
        this.logger.error(format, new Object[0]);
        final ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        buildResponseData.message = format;
        final IPackageHandler packageHandler = this.packageHandlerWeakRef.get();
        if (packageHandler == null) {
            return;
        }
        packageHandler.closeFirstPackage(buildResponseData, activityPackage);
    }
    
    private void sendI(final ActivityPackage activityPackage, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://app.adjust.com");
        sb.append(activityPackage.getPath());
        final String string = sb.toString();
        try {
            final ResponseData postHttpsURLConnection = UtilNetworking.createPOSTHttpsURLConnection(string, activityPackage, n);
            final IPackageHandler packageHandler = this.packageHandlerWeakRef.get();
            if (packageHandler == null) {
                return;
            }
            if (postHttpsURLConnection.jsonResponse == null) {
                packageHandler.closeFirstPackage(postHttpsURLConnection, activityPackage);
                return;
            }
            packageHandler.sendNextPackage(postHttpsURLConnection);
        }
        catch (Throwable t) {
            this.sendNextPackageI(activityPackage, "Runtime exception", t);
        }
        catch (IOException ex) {
            this.closePackageI(activityPackage, "Request failed", ex);
        }
        catch (SocketTimeoutException ex2) {
            this.closePackageI(activityPackage, "Request timed out", ex2);
        }
        catch (UnsupportedEncodingException ex3) {
            this.sendNextPackageI(activityPackage, "Failed to encode parameters", ex3);
        }
    }
    
    private void sendNextPackageI(final ActivityPackage activityPackage, String format, final Throwable t) {
        format = String.format("%s. (%s)", activityPackage.getFailureMessage(), Util.getReasonString(format, t));
        this.logger.error(format, new Object[0]);
        final ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        buildResponseData.message = format;
        final IPackageHandler packageHandler = this.packageHandlerWeakRef.get();
        if (packageHandler == null) {
            return;
        }
        packageHandler.sendNextPackage(buildResponseData);
    }
    
    @Override
    public void init(final IPackageHandler referent) {
        this.packageHandlerWeakRef = new WeakReference<IPackageHandler>(referent);
    }
    
    @Override
    public void sendPackage(final ActivityPackage activityPackage, final int n) {
        this.scheduledExecutor.submit(new Runnable() {
            @Override
            public void run() {
                RequestHandler.this.sendI(activityPackage, n);
            }
        });
    }
    
    @Override
    public void teardown() {
        this.logger.verbose("RequestHandler teardown", new Object[0]);
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            }
            catch (SecurityException ex) {}
        }
        if (this.packageHandlerWeakRef != null) {
            this.packageHandlerWeakRef.clear();
        }
        this.scheduledExecutor = null;
        this.packageHandlerWeakRef = null;
        this.logger = null;
    }
}
