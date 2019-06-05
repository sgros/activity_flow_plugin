package org.mozilla.focus.home;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.RemoteConfigConstants;
import org.mozilla.focus.utils.Settings;

public final class FeatureSurveyViewHelper implements OnClickListener {
   private Button btnNo;
   private Button btnYes;
   private final Context context;
   private final RemoteConfigConstants.SURVEY featureSurvey;
   private ImageView imgLogo;
   private boolean isViewInit;
   private ViewGroup parentView;
   private View rootView;
   private TextView textContent;

   public FeatureSurveyViewHelper(Context var1, RemoteConfigConstants.SURVEY var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "featureSurvey");
      super();
      this.context = var1;
      this.featureSurvey = var2;
   }

   // $FF: synthetic method
   public static final Button access$getBtnNo$p(FeatureSurveyViewHelper var0) {
      Button var1 = var0.btnNo;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("btnNo");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final Button access$getBtnYes$p(FeatureSurveyViewHelper var0) {
      Button var1 = var0.btnYes;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("btnYes");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final ViewGroup access$getParentView$p(FeatureSurveyViewHelper var0) {
      ViewGroup var1 = var0.parentView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("parentView");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final View access$getRootView$p(FeatureSurveyViewHelper var0) {
      View var1 = var0.rootView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("rootView");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final TextView access$getTextContent$p(FeatureSurveyViewHelper var0) {
      TextView var1 = var0.textContent;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("textContent");
      }

      return var1;
   }

   private final void dismissSurveyView(final View var1) {
      (new Handler()).postDelayed((Runnable)(new Runnable() {
         public final void run() {
            FeatureSurveyViewHelper.access$getParentView$p(FeatureSurveyViewHelper.this).removeView(FeatureSurveyViewHelper.access$getRootView$p(FeatureSurveyViewHelper.this));
            FeatureSurveyViewHelper.this.isViewInit = false;
            var1.setVisibility(8);
         }
      }), 5000L);
   }

   public void onClick(final View var1) {
      Intrinsics.checkParameterIsNotNull(var1, "v");
      ViewParent var2 = var1.getParent();
      if (var2 != null) {
         this.parentView = (ViewGroup)var2;
         if (!this.isViewInit) {
            this.isViewInit = true;
            LayoutInflater var4 = LayoutInflater.from(this.context);
            ViewGroup var3 = this.parentView;
            if (var3 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("parentView");
            }

            var4.inflate(2131493022, var3);
            ViewGroup var5 = this.parentView;
            if (var5 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("parentView");
            }

            View var6 = var5.findViewById(2131296730);
            Intrinsics.checkExpressionValueIsNotNull(var6, "parentView.findViewById(R.id.wifi_vpn_root)");
            this.rootView = var6;
            var6 = this.rootView;
            if (var6 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var6 = var6.findViewById(2131296731);
            Intrinsics.checkExpressionValueIsNotNull(var6, "rootView.findViewById(R.id.wifi_vpn_text_content)");
            this.textContent = (TextView)var6;
            var6 = this.rootView;
            if (var6 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var6 = var6.findViewById(2131296726);
            Intrinsics.checkExpressionValueIsNotNull(var6, "rootView.findViewById(R.id.wifi_vpn_btn_yes)");
            this.btnYes = (Button)var6;
            var6 = this.rootView;
            if (var6 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var6 = var6.findViewById(2131296725);
            Intrinsics.checkExpressionValueIsNotNull(var6, "rootView.findViewById(R.id.wifi_vpn_btn_no)");
            this.btnNo = (Button)var6;
            var6 = this.rootView;
            if (var6 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var6 = var6.findViewById(2131296728);
            Intrinsics.checkExpressionValueIsNotNull(var6, "rootView.findViewById(R.id.wifi_vpn_img_logo)");
            this.imgLogo = (ImageView)var6;
            var6 = this.rootView;
            if (var6 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var6 = var6.findViewById(2131296727);
            Intrinsics.checkExpressionValueIsNotNull(var6, "rootView.findViewById(R.id.wifi_vpn_card)");
            ((CardView)var6).setOnClickListener((OnClickListener)null);
            TextView var9;
            if (this.featureSurvey == RemoteConfigConstants.SURVEY.WIFI_FINDING) {
               var9 = this.textContent;
               if (var9 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("textContent");
               }

               var9.setText(2131755189);
               TelemetryWrapper.clickWifiFinderSurvey();
            } else if (this.featureSurvey == RemoteConfigConstants.SURVEY.VPN) {
               var9 = this.textContent;
               if (var9 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("textContent");
               }

               var9.setText(2131755188);
               TelemetryWrapper.clickVpnSurvey();
            } else if (this.featureSurvey == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER) {
               var9 = this.textContent;
               if (var9 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("textContent");
               }

               var9.setText(2131755080);
               ImageView var10 = this.imgLogo;
               if (var10 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("imgLogo");
               }

               var10.setVisibility(0);
               TelemetryWrapper.clickVpnRecommender(false);
            }

            Settings var11 = Settings.getInstance(this.context);
            Intrinsics.checkExpressionValueIsNotNull(var11, "Settings.getInstance(context)");
            final Settings.EventHistory var12 = var11.getEventHistory();
            View var7 = this.rootView;
            if (var7 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("rootView");
            }

            var7.setOnClickListener((OnClickListener)(new OnClickListener() {
               public final void onClick(View var1x) {
                  FeatureSurveyViewHelper.access$getParentView$p(FeatureSurveyViewHelper.this).removeView(FeatureSurveyViewHelper.access$getRootView$p(FeatureSurveyViewHelper.this));
                  FeatureSurveyViewHelper.this.isViewInit = false;
                  if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.WIFI_FINDING) {
                     if (var12.contains("feature_survey_wifi_finding")) {
                        var1.setVisibility(8);
                     } else {
                        TelemetryWrapper.surveyResult("dismiss", "wifi_finder");
                     }
                  } else if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN) {
                     if (var12.contains("feature_survey_vpn")) {
                        var1.setVisibility(8);
                     } else {
                        TelemetryWrapper.surveyResult("dismiss", "vpn");
                     }
                  } else if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER) {
                     TelemetryWrapper.dismissVpnRecommend();
                  }

               }
            }));
            Button var8 = this.btnYes;
            if (var8 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("btnYes");
            }

            var8.setOnClickListener((OnClickListener)(new OnClickListener() {
               public final void onClick(View var1x) {
                  if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER) {
                     FeatureSurveyViewHelper.access$getParentView$p(FeatureSurveyViewHelper.this).removeView(FeatureSurveyViewHelper.access$getRootView$p(FeatureSurveyViewHelper.this));
                     FeatureSurveyViewHelper.this.isViewInit = false;
                     String var2 = AppConfigWrapper.getVpnRecommenderUrl(FeatureSurveyViewHelper.this.context);
                     if (!TextUtils.isEmpty((CharSequence)var2)) {
                        ScreenNavigator.get(FeatureSurveyViewHelper.this.context).showBrowserScreen(var2, true, false);
                     }

                     TelemetryWrapper.clickVpnRecommend(true);
                  } else {
                     FeatureSurveyViewHelper.access$getTextContent$p(FeatureSurveyViewHelper.this).setText((CharSequence)FeatureSurveyViewHelper.this.context.getString(2131755187, new Object[]{"\ud83d\ude00"}));
                     FeatureSurveyViewHelper.access$getBtnYes$p(FeatureSurveyViewHelper.this).setVisibility(8);
                     FeatureSurveyViewHelper.access$getBtnNo$p(FeatureSurveyViewHelper.this).setVisibility(8);
                     if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.WIFI_FINDING) {
                        var12.add("feature_survey_wifi_finding");
                        TelemetryWrapper.surveyResult("positive", "wifi_finder");
                     } else if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN) {
                        var12.add("feature_survey_vpn");
                        TelemetryWrapper.surveyResult("positive", "vpn");
                     }

                     FeatureSurveyViewHelper.this.dismissSurveyView(var1);
                  }

               }
            }));
            var8 = this.btnNo;
            if (var8 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("btnNo");
            }

            var8.setOnClickListener((OnClickListener)(new OnClickListener() {
               public final void onClick(View var1x) {
                  if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN_RECOMMENDER) {
                     Settings var2 = Settings.getInstance(FeatureSurveyViewHelper.this.context);
                     Intrinsics.checkExpressionValueIsNotNull(var2, "Settings.getInstance(context)");
                     var2.getEventHistory().add("vpn_recommender_ignore");
                     FeatureSurveyViewHelper.access$getParentView$p(FeatureSurveyViewHelper.this).removeView(FeatureSurveyViewHelper.access$getRootView$p(FeatureSurveyViewHelper.this));
                     FeatureSurveyViewHelper.this.isViewInit = false;
                     var1.setVisibility(8);
                     TelemetryWrapper.clickVpnRecommend(false);
                  } else {
                     FeatureSurveyViewHelper.access$getTextContent$p(FeatureSurveyViewHelper.this).setText((CharSequence)FeatureSurveyViewHelper.this.context.getString(2131755187, new Object[]{"\ud83d\ude00"}));
                     FeatureSurveyViewHelper.access$getBtnYes$p(FeatureSurveyViewHelper.this).setVisibility(8);
                     FeatureSurveyViewHelper.access$getBtnNo$p(FeatureSurveyViewHelper.this).setVisibility(8);
                     if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.WIFI_FINDING) {
                        var12.add("feature_survey_wifi_finding");
                        TelemetryWrapper.surveyResult("negative", "wifi_finder");
                     } else if (FeatureSurveyViewHelper.this.featureSurvey == RemoteConfigConstants.SURVEY.VPN) {
                        var12.add("feature_survey_vpn");
                        TelemetryWrapper.surveyResult("negative", "vpn");
                     } else {
                        FeatureSurveyViewHelper.this.dismissSurveyView(var1);
                     }
                  }

               }
            }));
         }

      } else {
         throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
      }
   }
}
