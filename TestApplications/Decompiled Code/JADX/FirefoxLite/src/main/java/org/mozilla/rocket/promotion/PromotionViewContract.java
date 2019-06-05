package org.mozilla.rocket.promotion;

/* compiled from: PromotionPresenter.kt */
public interface PromotionViewContract {
    void postSurveyNotification();

    void showPrivacyPolicyUpdateNotification();

    void showRateAppDialog();

    void showRateAppDialogFromIntent();

    void showRateAppNotification();

    void showShareAppDialog();
}
