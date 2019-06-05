package org.mozilla.rocket.promotion;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.AppConfigWrapper;

/* compiled from: PromotionPresenter.kt */
public final class PromotionPresenter {
    public static final Companion Companion = new Companion();

    /* compiled from: PromotionPresenter.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void runPromotion(PromotionViewContract promotionViewContract, PromotionModel promotionModel) {
            Intrinsics.checkParameterIsNotNull(promotionViewContract, "promotionViewContract");
            Intrinsics.checkParameterIsNotNull(promotionModel, "promotionModel");
            if (!runPromotionFromIntent(promotionViewContract, promotionModel)) {
                if (!promotionModel.getDidShowRateDialog() && ((long) promotionModel.getAppCreateCount()) >= promotionModel.getRateAppDialogThreshold()) {
                    promotionViewContract.showRateAppDialog();
                } else if (promotionModel.getDidDismissRateDialog() && !promotionModel.getDidShowRateAppNotification() && ((long) promotionModel.getAppCreateCount()) >= promotionModel.getRateAppNotificationThreshold()) {
                    promotionViewContract.showRateAppNotification();
                } else if (!promotionModel.getDidShowShareDialog() && ((long) promotionModel.getAppCreateCount()) >= promotionModel.getShareAppDialogThreshold()) {
                    promotionViewContract.showShareAppDialog();
                }
                if (promotionModel.isSurveyEnabled() && promotionModel.getAppCreateCount() >= AppConfigWrapper.getSurveyNotificationLaunchTimeThreshold()) {
                    promotionViewContract.postSurveyNotification();
                }
                if (promotionModel.getShouldShowPrivacyPolicyUpdate()) {
                    promotionViewContract.showPrivacyPolicyUpdateNotification();
                }
            }
        }

        public final boolean runPromotionFromIntent(PromotionViewContract promotionViewContract, PromotionModel promotionModel) {
            Intrinsics.checkParameterIsNotNull(promotionViewContract, "promotionViewContract");
            Intrinsics.checkParameterIsNotNull(promotionModel, "promotionModel");
            if (!promotionModel.getShowRateAppDialogFromIntent()) {
                return false;
            }
            promotionViewContract.showRateAppDialogFromIntent();
            return true;
        }
    }

    public static final void runPromotion(PromotionViewContract promotionViewContract, PromotionModel promotionModel) {
        Companion.runPromotion(promotionViewContract, promotionModel);
    }

    public static final boolean runPromotionFromIntent(PromotionViewContract promotionViewContract, PromotionModel promotionModel) {
        return Companion.runPromotionFromIntent(promotionViewContract, promotionModel);
    }
}
