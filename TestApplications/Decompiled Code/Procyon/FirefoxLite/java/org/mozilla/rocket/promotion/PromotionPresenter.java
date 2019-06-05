// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.promotion;

import org.mozilla.focus.utils.AppConfigWrapper;
import kotlin.jvm.internal.Intrinsics;

public final class PromotionPresenter
{
    public static final Companion Companion;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final void runPromotion(final PromotionViewContract promotionViewContract, final PromotionModel promotionModel) {
        PromotionPresenter.Companion.runPromotion(promotionViewContract, promotionModel);
    }
    
    public static final boolean runPromotionFromIntent(final PromotionViewContract promotionViewContract, final PromotionModel promotionModel) {
        return PromotionPresenter.Companion.runPromotionFromIntent(promotionViewContract, promotionModel);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final void runPromotion(final PromotionViewContract promotionViewContract, final PromotionModel promotionModel) {
            Intrinsics.checkParameterIsNotNull(promotionViewContract, "promotionViewContract");
            Intrinsics.checkParameterIsNotNull(promotionModel, "promotionModel");
            if (this.runPromotionFromIntent(promotionViewContract, promotionModel)) {
                return;
            }
            if (!promotionModel.getDidShowRateDialog() && promotionModel.getAppCreateCount() >= promotionModel.getRateAppDialogThreshold()) {
                promotionViewContract.showRateAppDialog();
            }
            else if (promotionModel.getDidDismissRateDialog() && !promotionModel.getDidShowRateAppNotification() && promotionModel.getAppCreateCount() >= promotionModel.getRateAppNotificationThreshold()) {
                promotionViewContract.showRateAppNotification();
            }
            else if (!promotionModel.getDidShowShareDialog() && promotionModel.getAppCreateCount() >= promotionModel.getShareAppDialogThreshold()) {
                promotionViewContract.showShareAppDialog();
            }
            if (promotionModel.isSurveyEnabled() && promotionModel.getAppCreateCount() >= AppConfigWrapper.getSurveyNotificationLaunchTimeThreshold()) {
                promotionViewContract.postSurveyNotification();
            }
            if (promotionModel.getShouldShowPrivacyPolicyUpdate()) {
                promotionViewContract.showPrivacyPolicyUpdateNotification();
            }
        }
        
        public final boolean runPromotionFromIntent(final PromotionViewContract promotionViewContract, final PromotionModel promotionModel) {
            Intrinsics.checkParameterIsNotNull(promotionViewContract, "promotionViewContract");
            Intrinsics.checkParameterIsNotNull(promotionModel, "promotionModel");
            if (promotionModel.getShowRateAppDialogFromIntent()) {
                promotionViewContract.showRateAppDialogFromIntent();
                return true;
            }
            return false;
        }
    }
}
