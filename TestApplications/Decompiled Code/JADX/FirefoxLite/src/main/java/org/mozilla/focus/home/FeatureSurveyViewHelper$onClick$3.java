package org.mozilla.focus.home;

import android.view.View;
import android.view.View.OnClickListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Settings.EventHistory;
import org.mozilla.rocket.C0769R;

/* compiled from: FeatureSurveyViewHelper.kt */
final class FeatureSurveyViewHelper$onClick$3 implements OnClickListener {
    final /* synthetic */ EventHistory $eventHistory;
    /* renamed from: $v */
    final /* synthetic */ View f46$v;
    final /* synthetic */ FeatureSurveyViewHelper this$0;

    FeatureSurveyViewHelper$onClick$3(FeatureSurveyViewHelper featureSurveyViewHelper, View view, EventHistory eventHistory) {
        this.this$0 = featureSurveyViewHelper;
        this.f46$v = view;
        this.$eventHistory = eventHistory;
    }

    public final void onClick(View view) {
        if (this.this$0.featureSurvey == SURVEY.VPN_RECOMMENDER) {
            Settings instance = Settings.getInstance(this.this$0.context);
            Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(context)");
            instance.getEventHistory().add("vpn_recommender_ignore");
            FeatureSurveyViewHelper.access$getParentView$p(this.this$0).removeView(FeatureSurveyViewHelper.access$getRootView$p(this.this$0));
            this.this$0.isViewInit = false;
            this.f46$v.setVisibility(8);
            TelemetryWrapper.clickVpnRecommend(false);
            return;
        }
        FeatureSurveyViewHelper.access$getTextContent$p(this.this$0).setText(this.this$0.context.getString(C0769R.string.exp_survey_thanks, new Object[]{"😀"}));
        FeatureSurveyViewHelper.access$getBtnYes$p(this.this$0).setVisibility(8);
        FeatureSurveyViewHelper.access$getBtnNo$p(this.this$0).setVisibility(8);
        if (this.this$0.featureSurvey == SURVEY.WIFI_FINDING) {
            this.$eventHistory.add("feature_survey_wifi_finding");
            TelemetryWrapper.surveyResult("negative", "wifi_finder");
        } else if (this.this$0.featureSurvey == SURVEY.VPN) {
            this.$eventHistory.add("feature_survey_vpn");
            TelemetryWrapper.surveyResult("negative", "vpn");
        } else {
            this.this$0.dismissSurveyView(this.f46$v);
        }
    }
}
