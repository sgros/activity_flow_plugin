package org.mozilla.rocket.promotion;

public interface PromotionViewContract {
   void postSurveyNotification();

   void showPrivacyPolicyUpdateNotification();

   void showRateAppDialog();

   void showRateAppDialogFromIntent();

   void showRateAppNotification();

   void showShareAppDialog();
}
