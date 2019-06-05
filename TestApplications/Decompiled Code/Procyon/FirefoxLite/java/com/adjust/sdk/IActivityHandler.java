// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import android.net.Uri;

public interface IActivityHandler
{
    void addSessionCallbackParameter(final String p0, final String p1);
    
    void addSessionPartnerParameter(final String p0, final String p1);
    
    void finishedTrackingActivity(final ResponseData p0);
    
    void init(final AdjustConfig p0);
    
    boolean isEnabled();
    
    void launchAttributionResponseTasks(final AttributionResponseData p0);
    
    void launchEventResponseTasks(final EventResponseData p0);
    
    void launchSdkClickResponseTasks(final SdkClickResponseData p0);
    
    void launchSessionResponseTasks(final SessionResponseData p0);
    
    void onPause();
    
    void onResume();
    
    void readOpenUrl(final Uri p0, final long p1);
    
    void removeSessionCallbackParameter(final String p0);
    
    void removeSessionPartnerParameter(final String p0);
    
    void resetSessionCallbackParameters();
    
    void resetSessionPartnerParameters();
    
    void sendFirstPackages();
    
    void sendReferrer(final String p0, final long p1);
    
    void setAskingAttribution(final boolean p0);
    
    void setEnabled(final boolean p0);
    
    void setOfflineMode(final boolean p0);
    
    void setPushToken(final String p0);
    
    void teardown(final boolean p0);
    
    void trackEvent(final AdjustEvent p0);
    
    boolean updateAttributionI(final AdjustAttribution p0);
}
