package com.adjust.sdk;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class AdjustInstance {
   private ActivityHandler activityHandler;
   private String pushToken;
   private String referrer;
   private long referrerClickTime;
   private List sessionParametersActionsArray;

   private boolean checkActivityHandler() {
      if (this.activityHandler == null) {
         getLogger().error("Adjust not initialized correctly");
         return false;
      } else {
         return true;
      }
   }

   private static ILogger getLogger() {
      return AdjustFactory.getLogger();
   }

   public void addSessionCallbackParameter(final String var1, final String var2) {
      if (this.activityHandler != null) {
         this.activityHandler.addSessionCallbackParameter(var1, var2);
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1x) {
               var1x.addSessionCallbackParameterI(var1, var2);
            }
         });
      }
   }

   public void addSessionPartnerParameter(final String var1, final String var2) {
      if (this.activityHandler != null) {
         this.activityHandler.addSessionPartnerParameter(var1, var2);
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1x) {
               var1x.addSessionPartnerParameterI(var1, var2);
            }
         });
      }
   }

   public void appWillOpenUrl(Uri var1) {
      if (this.checkActivityHandler()) {
         long var2 = System.currentTimeMillis();
         this.activityHandler.readOpenUrl(var1, var2);
      }
   }

   public String getAdid() {
      return !this.checkActivityHandler() ? null : this.activityHandler.getAdid();
   }

   public AdjustAttribution getAttribution() {
      return !this.checkActivityHandler() ? null : this.activityHandler.getAttribution();
   }

   public boolean isEnabled() {
      return !this.checkActivityHandler() ? false : this.activityHandler.isEnabled();
   }

   public void onCreate(AdjustConfig var1) {
      if (this.activityHandler != null) {
         getLogger().error("Adjust already initialized");
      } else {
         var1.referrer = this.referrer;
         var1.referrerClickTime = this.referrerClickTime;
         var1.sessionParametersActionsArray = this.sessionParametersActionsArray;
         var1.pushToken = this.pushToken;
         this.activityHandler = ActivityHandler.getInstance(var1);
      }
   }

   public void onPause() {
      if (this.checkActivityHandler()) {
         this.activityHandler.onPause();
      }
   }

   public void onResume() {
      if (this.checkActivityHandler()) {
         this.activityHandler.onResume();
      }
   }

   public void removeSessionCallbackParameter(final String var1) {
      if (this.activityHandler != null) {
         this.activityHandler.removeSessionCallbackParameter(var1);
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1x) {
               var1x.removeSessionCallbackParameterI(var1);
            }
         });
      }
   }

   public void removeSessionPartnerParameter(final String var1) {
      if (this.activityHandler != null) {
         this.activityHandler.removeSessionPartnerParameter(var1);
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1x) {
               var1x.removeSessionPartnerParameterI(var1);
            }
         });
      }
   }

   public void resetSessionCallbackParameters() {
      if (this.activityHandler != null) {
         this.activityHandler.resetSessionCallbackParameters();
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1) {
               var1.resetSessionCallbackParametersI();
            }
         });
      }
   }

   public void resetSessionPartnerParameters() {
      if (this.activityHandler != null) {
         this.activityHandler.resetSessionPartnerParameters();
      } else {
         if (this.sessionParametersActionsArray == null) {
            this.sessionParametersActionsArray = new ArrayList();
         }

         this.sessionParametersActionsArray.add(new IRunActivityHandler() {
            public void run(ActivityHandler var1) {
               var1.resetSessionPartnerParametersI();
            }
         });
      }
   }

   public void sendFirstPackages() {
      if (this.checkActivityHandler()) {
         this.activityHandler.sendFirstPackages();
      }
   }

   public void sendReferrer(String var1) {
      long var2 = System.currentTimeMillis();
      if (this.activityHandler == null) {
         this.referrer = var1;
         this.referrerClickTime = var2;
      } else {
         this.activityHandler.sendReferrer(var1, var2);
      }

   }

   public void setEnabled(boolean var1) {
      if (this.checkActivityHandler()) {
         this.activityHandler.setEnabled(var1);
      }
   }

   public void setOfflineMode(boolean var1) {
      if (this.checkActivityHandler()) {
         this.activityHandler.setOfflineMode(var1);
      }
   }

   public void setPushToken(String var1) {
      this.pushToken = var1;
      if (this.activityHandler != null) {
         this.activityHandler.setPushToken(var1);
      }
   }

   public void teardown(boolean var1) {
      if (this.checkActivityHandler()) {
         this.activityHandler.teardown(var1);
         this.activityHandler = null;
      }
   }

   public void trackEvent(AdjustEvent var1) {
      if (this.checkActivityHandler()) {
         this.activityHandler.trackEvent(var1);
      }
   }
}
