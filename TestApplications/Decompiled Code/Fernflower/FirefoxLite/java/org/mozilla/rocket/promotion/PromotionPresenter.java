package org.mozilla.rocket.promotion;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.AppConfigWrapper;

public final class PromotionPresenter {
   public static final PromotionPresenter.Companion Companion = new PromotionPresenter.Companion((DefaultConstructorMarker)null);

   public static final void runPromotion(PromotionViewContract var0, PromotionModel var1) {
      Companion.runPromotion(var0, var1);
   }

   public static final boolean runPromotionFromIntent(PromotionViewContract var0, PromotionModel var1) {
      return Companion.runPromotionFromIntent(var0, var1);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final void runPromotion(PromotionViewContract var1, PromotionModel var2) {
         Intrinsics.checkParameterIsNotNull(var1, "promotionViewContract");
         Intrinsics.checkParameterIsNotNull(var2, "promotionModel");
         if (!((PromotionPresenter.Companion)this).runPromotionFromIntent(var1, var2)) {
            if (!var2.getDidShowRateDialog() && (long)var2.getAppCreateCount() >= var2.getRateAppDialogThreshold()) {
               var1.showRateAppDialog();
            } else if (var2.getDidDismissRateDialog() && !var2.getDidShowRateAppNotification() && (long)var2.getAppCreateCount() >= var2.getRateAppNotificationThreshold()) {
               var1.showRateAppNotification();
            } else if (!var2.getDidShowShareDialog() && (long)var2.getAppCreateCount() >= var2.getShareAppDialogThreshold()) {
               var1.showShareAppDialog();
            }

            if (var2.isSurveyEnabled() && var2.getAppCreateCount() >= AppConfigWrapper.getSurveyNotificationLaunchTimeThreshold()) {
               var1.postSurveyNotification();
            }

            if (var2.getShouldShowPrivacyPolicyUpdate()) {
               var1.showPrivacyPolicyUpdateNotification();
            }

         }
      }

      public final boolean runPromotionFromIntent(PromotionViewContract var1, PromotionModel var2) {
         Intrinsics.checkParameterIsNotNull(var1, "promotionViewContract");
         Intrinsics.checkParameterIsNotNull(var2, "promotionModel");
         if (var2.getShowRateAppDialogFromIntent()) {
            var1.showRateAppDialogFromIntent();
            return true;
         } else {
            return false;
         }
      }
   }
}
