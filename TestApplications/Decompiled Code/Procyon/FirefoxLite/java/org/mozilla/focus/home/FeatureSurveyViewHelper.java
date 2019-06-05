// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.home;

import android.view.ViewParent;
import kotlin.TypeCastException;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.mozilla.focus.utils.RemoteConfigConstants;
import android.content.Context;
import android.widget.Button;
import android.view.View$OnClickListener;

public final class FeatureSurveyViewHelper implements View$OnClickListener
{
    private Button btnNo;
    private Button btnYes;
    private final Context context;
    private final RemoteConfigConstants.SURVEY featureSurvey;
    private ImageView imgLogo;
    private boolean isViewInit;
    private ViewGroup parentView;
    private View rootView;
    private TextView textContent;
    
    public FeatureSurveyViewHelper(final Context context, final RemoteConfigConstants.SURVEY featureSurvey) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(featureSurvey, "featureSurvey");
        this.context = context;
        this.featureSurvey = featureSurvey;
    }
    
    private final void dismissSurveyView(final View view) {
        new Handler().postDelayed((Runnable)new FeatureSurveyViewHelper$dismissSurveyView.FeatureSurveyViewHelper$dismissSurveyView$1(this, view), 5000L);
    }
    
    public void onClick(final View view) {
        Intrinsics.checkParameterIsNotNull(view, "v");
        final ViewParent parent = view.getParent();
        if (parent != null) {
            this.parentView = (ViewGroup)parent;
            if (!this.isViewInit) {
                this.isViewInit = true;
                final LayoutInflater from = LayoutInflater.from(this.context);
                final ViewGroup parentView = this.parentView;
                if (parentView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parentView");
                }
                from.inflate(2131493022, parentView);
                final ViewGroup parentView2 = this.parentView;
                if (parentView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parentView");
                }
                final View viewById = parentView2.findViewById(2131296730);
                Intrinsics.checkExpressionValueIsNotNull(viewById, "parentView.findViewById(R.id.wifi_vpn_root)");
                this.rootView = viewById;
                final View rootView = this.rootView;
                if (rootView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                final View viewById2 = rootView.findViewById(2131296731);
                Intrinsics.checkExpressionValueIsNotNull(viewById2, "rootView.findViewById(R.id.wifi_vpn_text_content)");
                this.textContent = (TextView)viewById2;
                final View rootView2 = this.rootView;
                if (rootView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                final View viewById3 = rootView2.findViewById(2131296726);
                Intrinsics.checkExpressionValueIsNotNull(viewById3, "rootView.findViewById(R.id.wifi_vpn_btn_yes)");
                this.btnYes = (Button)viewById3;
                final View rootView3 = this.rootView;
                if (rootView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                final View viewById4 = rootView3.findViewById(2131296725);
                Intrinsics.checkExpressionValueIsNotNull(viewById4, "rootView.findViewById(R.id.wifi_vpn_btn_no)");
                this.btnNo = (Button)viewById4;
                final View rootView4 = this.rootView;
                if (rootView4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                final View viewById5 = rootView4.findViewById(2131296728);
                Intrinsics.checkExpressionValueIsNotNull(viewById5, "rootView.findViewById(R.id.wifi_vpn_img_logo)");
                this.imgLogo = (ImageView)viewById5;
                final View rootView5 = this.rootView;
                if (rootView5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                final View viewById6 = rootView5.findViewById(2131296727);
                Intrinsics.checkExpressionValueIsNotNull(viewById6, "rootView.findViewById(R.id.wifi_vpn_card)");
                ((CardView)viewById6).setOnClickListener((View$OnClickListener)null);
                if (this.featureSurvey == RemoteConfigConstants.SURVEY.WIFI_FINDING) {
                    final TextView textContent = this.textContent;
                    if (textContent == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textContent.setText(2131755189);
                    TelemetryWrapper.clickWifiFinderSurvey();
                }
                else if (this.featureSurvey == RemoteConfigConstants.SURVEY.VPN) {
                    final TextView textContent2 = this.textContent;
                    if (textContent2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textContent2.setText(2131755188);
                    TelemetryWrapper.clickVpnSurvey();
                }
                else if (this.featureSurvey == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER) {
                    final TextView textContent3 = this.textContent;
                    if (textContent3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("textContent");
                    }
                    textContent3.setText(2131755080);
                    final ImageView imgLogo = this.imgLogo;
                    if (imgLogo == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("imgLogo");
                    }
                    imgLogo.setVisibility(0);
                    TelemetryWrapper.clickVpnRecommender(false);
                }
                final Settings instance = Settings.getInstance(this.context);
                Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(context)");
                final Settings.EventHistory eventHistory = instance.getEventHistory();
                final View rootView6 = this.rootView;
                if (rootView6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("rootView");
                }
                rootView6.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper$onClick.FeatureSurveyViewHelper$onClick$1(this, eventHistory, view));
                final Button btnYes = this.btnYes;
                if (btnYes == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnYes");
                }
                btnYes.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper$onClick.FeatureSurveyViewHelper$onClick$2(this, eventHistory, view));
                final Button btnNo = this.btnNo;
                if (btnNo == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("btnNo");
                }
                btnNo.setOnClickListener((View$OnClickListener)new FeatureSurveyViewHelper$onClick.FeatureSurveyViewHelper$onClick$3(this, view, eventHistory));
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
    }
}
