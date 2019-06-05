package org.mozilla.focus.home;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.focus.utils.Settings.EventHistory;

/* compiled from: FeatureSurveyViewHelper.kt */
final class FeatureSurveyViewHelper$onClick$1 implements OnClickListener {
    final /* synthetic */ EventHistory $eventHistory;
    /* renamed from: $v */
    final /* synthetic */ View f44$v;
    final /* synthetic */ FeatureSurveyViewHelper this$0;

    FeatureSurveyViewHelper$onClick$1(FeatureSurveyViewHelper featureSurveyViewHelper, EventHistory eventHistory, View view) {
        this.this$0 = featureSurveyViewHelper;
        this.$eventHistory = eventHistory;
        this.f44$v = view;
    }

    public final void onClick(View view) {
        FeatureSurveyViewHelper.access$getParentView$p(this.this$0).removeView(FeatureSurveyViewHelper.access$getRootView$p(this.this$0));
        this.this$0.isViewInit = false;
        if (this.this$0.featureSurvey == SURVEY.WIFI_FINDING) {
            if (this.$eventHistory.contains("feature_survey_wifi_finding")) {
                this.f44$v.setVisibility(8);
            } else {
                TelemetryWrapper.surveyResult("dismiss", "wifi_finder");
            }
        } else if (this.this$0.featureSurvey == SURVEY.VPN) {
            if (this.$eventHistory.contains("feature_survey_vpn")) {
                this.f44$v.setVisibility(8);
            } else {
                TelemetryWrapper.surveyResult("dismiss", "vpn");
            }
        } else if (this.this$0.featureSurvey == SURVEY.VPN_RECOMMENDER) {
            TelemetryWrapper.dismissVpnRecommend();
        }
    }
}
