package org.mozilla.focus.home;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.focus.utils.Settings.EventHistory;
import org.mozilla.rocket.C0769R;

/* compiled from: FeatureSurveyViewHelper.kt */
final class FeatureSurveyViewHelper$onClick$2 implements OnClickListener {
    final /* synthetic */ EventHistory $eventHistory;
    /* renamed from: $v */
    final /* synthetic */ View f45$v;
    final /* synthetic */ FeatureSurveyViewHelper this$0;

    FeatureSurveyViewHelper$onClick$2(FeatureSurveyViewHelper featureSurveyViewHelper, EventHistory eventHistory, View view) {
        this.this$0 = featureSurveyViewHelper;
        this.$eventHistory = eventHistory;
        this.f45$v = view;
    }

    public final void onClick(View view) {
        if (this.this$0.featureSurvey == SURVEY.VPN_RECOMMENDER) {
            FeatureSurveyViewHelper.access$getParentView$p(this.this$0).removeView(FeatureSurveyViewHelper.access$getRootView$p(this.this$0));
            this.this$0.isViewInit = false;
            String vpnRecommenderUrl = AppConfigWrapper.getVpnRecommenderUrl(this.this$0.context);
            if (!TextUtils.isEmpty(vpnRecommenderUrl)) {
                ScreenNavigator.get(this.this$0.context).showBrowserScreen(vpnRecommenderUrl, true, false);
            }
            TelemetryWrapper.clickVpnRecommend(true);
            return;
        }
        FeatureSurveyViewHelper.access$getTextContent$p(this.this$0).setText(this.this$0.context.getString(C0769R.string.exp_survey_thanks, new Object[]{"😀"}));
        FeatureSurveyViewHelper.access$getBtnYes$p(this.this$0).setVisibility(8);
        FeatureSurveyViewHelper.access$getBtnNo$p(this.this$0).setVisibility(8);
        if (this.this$0.featureSurvey == SURVEY.WIFI_FINDING) {
            this.$eventHistory.add("feature_survey_wifi_finding");
            TelemetryWrapper.surveyResult("positive", "wifi_finder");
        } else if (this.this$0.featureSurvey == SURVEY.VPN) {
            this.$eventHistory.add("feature_survey_vpn");
            TelemetryWrapper.surveyResult("positive", "vpn");
        }
        this.this$0.dismissSurveyView(this.f45$v);
    }
}
