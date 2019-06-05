// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class AdjustInstance
{
    private ActivityHandler activityHandler;
    private String pushToken;
    private String referrer;
    private long referrerClickTime;
    private List<IRunActivityHandler> sessionParametersActionsArray;
    
    private boolean checkActivityHandler() {
        if (this.activityHandler == null) {
            getLogger().error("Adjust not initialized correctly", new Object[0]);
            return false;
        }
        return true;
    }
    
    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }
    
    public void addSessionCallbackParameter(final String s, final String s2) {
        if (this.activityHandler != null) {
            this.activityHandler.addSessionCallbackParameter(s, s2);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.addSessionCallbackParameterI(s, s2);
            }
        });
    }
    
    public void addSessionPartnerParameter(final String s, final String s2) {
        if (this.activityHandler != null) {
            this.activityHandler.addSessionPartnerParameter(s, s2);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.addSessionPartnerParameterI(s, s2);
            }
        });
    }
    
    public void appWillOpenUrl(final Uri uri) {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.readOpenUrl(uri, System.currentTimeMillis());
    }
    
    public String getAdid() {
        if (!this.checkActivityHandler()) {
            return null;
        }
        return this.activityHandler.getAdid();
    }
    
    public AdjustAttribution getAttribution() {
        if (!this.checkActivityHandler()) {
            return null;
        }
        return this.activityHandler.getAttribution();
    }
    
    public boolean isEnabled() {
        return this.checkActivityHandler() && this.activityHandler.isEnabled();
    }
    
    public void onCreate(final AdjustConfig adjustConfig) {
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
    
    public void onPause() {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.onPause();
    }
    
    public void onResume() {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.onResume();
    }
    
    public void removeSessionCallbackParameter(final String s) {
        if (this.activityHandler != null) {
            this.activityHandler.removeSessionCallbackParameter(s);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.removeSessionCallbackParameterI(s);
            }
        });
    }
    
    public void removeSessionPartnerParameter(final String s) {
        if (this.activityHandler != null) {
            this.activityHandler.removeSessionPartnerParameter(s);
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.removeSessionPartnerParameterI(s);
            }
        });
    }
    
    public void resetSessionCallbackParameters() {
        if (this.activityHandler != null) {
            this.activityHandler.resetSessionCallbackParameters();
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.resetSessionCallbackParametersI();
            }
        });
    }
    
    public void resetSessionPartnerParameters() {
        if (this.activityHandler != null) {
            this.activityHandler.resetSessionPartnerParameters();
            return;
        }
        if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList<IRunActivityHandler>();
        }
        this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            @Override
            public void run(final ActivityHandler activityHandler) {
                activityHandler.resetSessionPartnerParametersI();
            }
        });
    }
    
    public void sendFirstPackages() {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.sendFirstPackages();
    }
    
    public void sendReferrer(final String referrer) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.activityHandler == null) {
            this.referrer = referrer;
            this.referrerClickTime = currentTimeMillis;
        }
        else {
            this.activityHandler.sendReferrer(referrer, currentTimeMillis);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.setEnabled(enabled);
    }
    
    public void setOfflineMode(final boolean offlineMode) {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.setOfflineMode(offlineMode);
    }
    
    public void setPushToken(final String s) {
        this.pushToken = s;
        if (this.activityHandler != null) {
            this.activityHandler.setPushToken(s);
        }
    }
    
    public void teardown(final boolean b) {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.teardown(b);
        this.activityHandler = null;
    }
    
    public void trackEvent(final AdjustEvent adjustEvent) {
        if (!this.checkActivityHandler()) {
            return;
        }
        this.activityHandler.trackEvent(adjustEvent);
    }
}
