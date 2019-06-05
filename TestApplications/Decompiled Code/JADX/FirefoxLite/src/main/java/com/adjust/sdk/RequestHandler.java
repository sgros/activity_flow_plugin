package com.adjust.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

public class RequestHandler implements IRequestHandler {
    private ILogger logger = AdjustFactory.getLogger();
    private WeakReference<IPackageHandler> packageHandlerWeakRef;
    private CustomScheduledExecutor scheduledExecutor = new CustomScheduledExecutor("RequestHandler", false);

    public RequestHandler(IPackageHandler iPackageHandler) {
        init(iPackageHandler);
    }

    public void init(IPackageHandler iPackageHandler) {
        this.packageHandlerWeakRef = new WeakReference(iPackageHandler);
    }

    public void sendPackage(final ActivityPackage activityPackage, final int i) {
        this.scheduledExecutor.submit(new Runnable() {
            public void run() {
                RequestHandler.this.sendI(activityPackage, i);
            }
        });
    }

    public void teardown() {
        this.logger.verbose("RequestHandler teardown", new Object[0]);
        if (this.scheduledExecutor != null) {
            try {
                this.scheduledExecutor.shutdownNow();
            } catch (SecurityException unused) {
            }
        }
        if (this.packageHandlerWeakRef != null) {
            this.packageHandlerWeakRef.clear();
        }
        this.scheduledExecutor = null;
        this.packageHandlerWeakRef = null;
        this.logger = null;
    }

    private void sendI(ActivityPackage activityPackage, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BASE_URL);
        stringBuilder.append(activityPackage.getPath());
        try {
            ResponseData createPOSTHttpsURLConnection = UtilNetworking.createPOSTHttpsURLConnection(stringBuilder.toString(), activityPackage, i);
            IPackageHandler iPackageHandler = (IPackageHandler) this.packageHandlerWeakRef.get();
            if (iPackageHandler != null) {
                if (createPOSTHttpsURLConnection.jsonResponse == null) {
                    iPackageHandler.closeFirstPackage(createPOSTHttpsURLConnection, activityPackage);
                } else {
                    iPackageHandler.sendNextPackage(createPOSTHttpsURLConnection);
                }
            }
        } catch (UnsupportedEncodingException e) {
            sendNextPackageI(activityPackage, "Failed to encode parameters", e);
        } catch (SocketTimeoutException e2) {
            closePackageI(activityPackage, "Request timed out", e2);
        } catch (IOException e3) {
            closePackageI(activityPackage, "Request failed", e3);
        } catch (Throwable th) {
            sendNextPackageI(activityPackage, "Runtime exception", th);
        }
    }

    private void closePackageI(ActivityPackage activityPackage, String str, Throwable th) {
        String failureMessage = activityPackage.getFailureMessage();
        str = Util.getReasonString(str, th);
        str = String.format("%s. (%s) Will retry later", new Object[]{failureMessage, str});
        this.logger.error(str, new Object[0]);
        ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        buildResponseData.message = str;
        IPackageHandler iPackageHandler = (IPackageHandler) this.packageHandlerWeakRef.get();
        if (iPackageHandler != null) {
            iPackageHandler.closeFirstPackage(buildResponseData, activityPackage);
        }
    }

    private void sendNextPackageI(ActivityPackage activityPackage, String str, Throwable th) {
        String failureMessage = activityPackage.getFailureMessage();
        str = Util.getReasonString(str, th);
        str = String.format("%s. (%s)", new Object[]{failureMessage, str});
        this.logger.error(str, new Object[0]);
        ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        buildResponseData.message = str;
        IPackageHandler iPackageHandler = (IPackageHandler) this.packageHandlerWeakRef.get();
        if (iPackageHandler != null) {
            iPackageHandler.sendNextPackage(buildResponseData);
        }
    }
}
