package org.mozilla.focus.home;

import android.content.Context;
import android.os.Handler;
import android.support.p004v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.RemoteConfigConstants.SURVEY;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Settings.EventHistory;
import org.mozilla.rocket.C0769R;

/* compiled from: FeatureSurveyViewHelper.kt */
public final class FeatureSurveyViewHelper implements OnClickListener {
    private Button btnNo;
    private Button btnYes;
    private final Context context;
    private final SURVEY featureSurvey;
    private ImageView imgLogo;
    private boolean isViewInit;
    private ViewGroup parentView;
    private View rootView;
    private TextView textContent;

    public FeatureSurveyViewHelper(Context context, SURVEY survey) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(survey, "featureSurvey");
        this.context = context;
        this.featureSurvey = survey;
    }

    public static final /* synthetic */ Button access$getBtnNo$p(FeatureSurveyViewHelper featureSurveyViewHelper) {
        Button button = featureSurveyViewHelper.btnNo;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnNo");
        }
        return button;
    }

    public static final /* synthetic */ Button access$getBtnYes$p(FeatureSurveyViewHelper featureSurveyViewHelper) {
        Button button = featureSurveyViewHelper.btnYes;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btnYes");
        }
        return button;
    }

    public static final /* synthetic */ ViewGroup access$getParentView$p(FeatureSurveyViewHelper featureSurveyViewHelper) {
        ViewGroup viewGroup = featureSurveyViewHelper.parentView;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parentView");
        }
        return viewGroup;
    }

    public static final /* synthetic */ View access$getRootView$p(FeatureSurveyViewHelper featureSurveyViewHelper) {
        View view = featureSurveyViewHelper.rootView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootView");
        }
        return view;
    }

    public static final /* synthetic */ TextView access$getTextContent$p(FeatureSurveyViewHelper featureSurveyViewHelper) {
        TextView textView = featureSurveyViewHelper.textContent;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textContent");
        }
        return textView;
    }

    public void onClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "v");
        ViewParent parent = view.getParent();
        if (parent != null) {
            this.parentView = (ViewGroup) parent;
            if (!this.isViewInit) {
                this.isViewInit = true;
                LayoutInflater from = LayoutInflater.from(this.context);
                ViewGroup viewGroup = this.parentView;
                if (viewGroup == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parentView");
                }
                from.inflate(C0769R.layout.wifi_vpn_survey, viewGroup);
                ViewGroup viewGroup2 = this.parentView;
                if (viewGroup2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parentView");
                }
                View findViewById = viewGroup2.findViewById(C0427R.C0426id.wifi_vpn_root);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "parentView.findViewById(R.id.wifi_vpn_root)");
                this.rootView = findViewById;
                findViewById = this.rootView;
                if (findViewById == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                findViewById = findViewById.findViewById(C0427R.C0426id.wifi_vpn_text_content);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView.findViewById(R.id.wifi_vpn_text_content)");
                this.textContent = (TextView) findViewById;
                findViewById = this.rootView;
                if (findViewById == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                findViewById = findViewById.findViewById(C0427R.C0426id.wifi_vpn_btn_yes);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView.findViewById(R.id.wifi_vpn_btn_yes)");
                this.btnYes = (Button) findViewById;
                findViewById = this.rootView;
                if (findViewById == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                findViewById = findViewById.findViewById(C0427R.C0426id.wifi_vpn_btn_no);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView.findViewById(R.id.wifi_vpn_btn_no)");
                this.btnNo = (Button) findViewById;
                findViewById = this.rootView;
                if (findViewById == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                findViewById = findViewById.findViewById(C0427R.C0426id.wifi_vpn_img_logo);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView.findViewById(R.id.wifi_vpn_img_logo)");
                this.imgLogo = (ImageView) findViewById;
                findViewById = this.rootView;
                if (findViewById == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                findViewById = findViewById.findViewById(C0427R.C0426id.wifi_vpn_card);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView.findViewById(R.id.wifi_vpn_card)");
                ((CardView) findViewById).setOnClickListener(null);
                TextView textView;
                if (this.featureSurvey == SURVEY.WIFI_FINDING) {
                    textView = this.textContent;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textView.setText(C0769R.string.exp_survey_wifi);
                    TelemetryWrapper.clickWifiFinderSurvey();
                } else if (this.featureSurvey == SURVEY.VPN) {
                    textView = this.textContent;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textView.setText(C0769R.string.exp_survey_vpn);
                    TelemetryWrapper.clickVpnSurvey();
                } else if (this.featureSurvey == SURVEY.VPN_RECOMMENDER) {
                    textView = this.textContent;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textView.setText(C0769R.string.btn_vpn);
                    ImageView imageView = this.imgLogo;
                    if (imageView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("imgLogo");
                    }
                    imageView.setVisibility(0);
                    TelemetryWrapper.clickVpnRecommender(false);
                }
                Settings instance = Settings.getInstance(this.context);
                Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(context)");
                EventHistory eventHistory = instance.getEventHistory();
                View view2 = this.rootView;
                if (view2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                view2.setOnClickListener(new FeatureSurveyViewHelper$onClick$1(this, eventHistory, view));
                Button button = this.btnYes;
                if (button == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnYes");
                }
                button.setOnClickListener(new FeatureSurveyViewHelper$onClick$2(this, eventHistory, view));
                button = this.btnNo;
                if (button == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnNo");
                }
                button.setOnClickListener(new FeatureSurveyViewHelper$onClick$3(this, view, eventHistory));
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    private final void dismissSurveyView(View view) {
        new Handler().postDelayed(new FeatureSurveyViewHelper$dismissSurveyView$1(this, view), 5000);
    }
}
