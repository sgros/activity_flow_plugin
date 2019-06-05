package com.adjust.sdk;

import android.net.Uri;

public interface IActivityHandler {
   void addSessionCallbackParameter(String var1, String var2);

   void addSessionPartnerParameter(String var1, String var2);

   void finishedTrackingActivity(ResponseData var1);

   void init(AdjustConfig var1);

   boolean isEnabled();

   void launchAttributionResponseTasks(AttributionResponseData var1);

   void launchEventResponseTasks(EventResponseData var1);

   void launchSdkClickResponseTasks(SdkClickResponseData var1);

   void launchSessionResponseTasks(SessionResponseData var1);

   void onPause();

   void onResume();

   void readOpenUrl(Uri var1, long var2);

   void removeSessionCallbackParameter(String var1);

   void removeSessionPartnerParameter(String var1);

   void resetSessionCallbackParameters();

   void resetSessionPartnerParameters();

   void sendFirstPackages();

   void sendReferrer(String var1, long var2);

   void setAskingAttribution(boolean var1);

   void setEnabled(boolean var1);

   void setOfflineMode(boolean var1);

   void setPushToken(String var1);

   void teardown(boolean var1);

   void trackEvent(AdjustEvent var1);

   boolean updateAttributionI(AdjustAttribution var1);
}
