package org.mozilla.focus.home;

import android.view.View;

/* compiled from: FeatureSurveyViewHelper.kt */
final class FeatureSurveyViewHelper$dismissSurveyView$1 implements Runnable {
    final /* synthetic */ View $btn;
    final /* synthetic */ FeatureSurveyViewHelper this$0;

    FeatureSurveyViewHelper$dismissSurveyView$1(FeatureSurveyViewHelper featureSurveyViewHelper, View view) {
        this.this$0 = featureSurveyViewHelper;
        this.$btn = view;
    }

    public final void run() {
        FeatureSurveyViewHelper.access$getParentView$p(this.this$0).removeView(FeatureSurveyViewHelper.access$getRootView$p(this.this$0));
        this.this$0.isViewInit = false;
        this.$btn.setVisibility(8);
    }
}
