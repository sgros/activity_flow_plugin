package com.adjust.sdk;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class AdjustInstance {
    private ActivityHandler activityHandler;
    private String pushToken;
    private String referrer;
    private long referrerClickTime;
    private List<IRunActivityHandler> sessionParametersActionsArray;

    /* renamed from: com.adjust.sdk.AdjustInstance$5 */
    class C03275 implements IRunActivityHandler {
        C03275() {
        }

        public void run(ActivityHandler activityHandler) {
            activityHandler.resetSessionCallbackParametersI();
        }
    }

    /* renamed from: com.adjust.sdk.AdjustInstance$6 */
    class C03286 implements IRunActivityHandler {
        C03286() {
        }

        public void run(ActivityHandler activityHandler) {
            activityHandler.resetSessionPartnerParametersI();
        }
    }

    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }

    public void onCreate(AdjustConfig adjustConfig) {
        if (this.activityHandler != null) {
            getLogger().error("Adjust already initialized", new Object[0]);
            return;
        }
        adjustConfig.referrer = this.referrer;
        adjustConfig.referrerClickTime = this.referrerClickTime;
        adjustConfig.sessionParametersActionsArray = this.sessionParametersActionsArray;
        adjustConfig.pushToken = this.pushToken;
        this.activityHandler = ActivityHandler.getInstance(adjustConfig);
    }

    public void trackEvent(AdjustEvent adjustEvent) {
        if (checkActivityHandler()) {
            this.activityHandler.trackEvent(adjustEvent);
        }
    }

    public void onResume() {
        if (checkActivityHandler()) {
            this.activityHandler.onResume();
        }
    }

    public void onPause() {
        if (checkActivityHandler()) {
            this.activityHandler.onPause();
        }
    }

    public void setEnabled(boolean z) {
        if (checkActivityHandler()) {
            this.activityHandler.setEnabled(z);
        }
    }

    public boolean isEnabled() {
        if (checkActivityHandler()) {
            return this.activityHandler.isEnabled();
        }
        return false;
    }

    public void appWillOpenUrl(Uri uri) {
        if (checkActivityHandler()) {
            this.activityHandler.readOpenUrl(uri, System.currentTimeMillis());
        }
    }

    public void sendReferrer(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.activityHandler == null) {
            this.referrer = str;
            this.referrerClickTime = currentTimeMillis;
            return;
        }
        this.activityHandler.sendReferrer(str, currentTimeMillis);
    }

    public void setOfflineMode(boolean z) {
        if (checkActivityHandler()) {
            this.activityHandler.setOfflineMode(z);
        }
    }

    public void sendFirstPackages() {
        if (checkActivityHandler()) {
            this.activityHandler.sendFirstPackages();
        }
    }

    public void addSessionCallbackParameter(final String str, final String str2) {
        if (this.activityHandler != null) {
            this.activityHandler.addSessionCallbackParameter(str, str2);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler activityHandler) {
                activityHandler.addSessionCallbackParameterI(str, str2);
            }
        });
    }

    public void addSessionPartnerParameter(final String str, final String str2) {
        if (this.activityHandler != null) {
            this.activityHandler.addSessionPartnerParameter(str, str2);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler activityHandler) {
                activityHandler.addSessionPartnerParameterI(str, str2);
            }
        });
    }

    public void removeSessionCallbackParameter(final String str) {
        if (this.activityHandler != null) {
            this.activityHandler.removeSessionCallbackParameter(str);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler activityHandler) {
                activityHandler.removeSessionCallbackParameterI(str);
            }
        });
    }

    public void removeSessionPartnerParameter(final String str) {
        if (this.activityHandler != null) {
            this.activityHandler.removeSessionPartnerParameter(str);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler activityHandler) {
                activityHandler.removeSessionPartnerParameterI(str);
            }
        });
    }

    public void resetSessionCallbackParameters() {
        if (this.activityHandler != null) {
            this.activityHandler.resetSessionCallbackParameters();
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new C03275());
    }

    public void resetSessionPartnerParameters() {
        if (this.activityHandler != null) {
            this.activityHandler.resetSessionPartnerParameters();
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
        }
        this.sessionParametersActionsArray.add(new C03286());
    }

    public void teardown(boolean z) {
        if (checkActivityHandler()) {
            this.activityHandler.teardown(z);
            this.activityHandler = null;
        }
    }

    public void setPushToken(String str) {
        this.pushToken = str;
        if (this.activityHandler != null) {
            this.activityHandler.setPushToken(str);
        }
    }

    public String getAdid() {
        if (checkActivityHandler()) {
            return this.activityHandler.getAdid();
        }
        return null;
    }

    public AdjustAttribution getAttribution() {
        if (checkActivityHandler()) {
            return this.activityHandler.getAttribution();
        }
        return null;
    }

    private boolean checkActivityHandler() {
        if (this.activityHandler != null) {
            return true;
        }
        getLogger().error("Adjust not initialized correctly", new Object[0]);
        return false;
    }
}
