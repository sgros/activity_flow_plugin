package org.mozilla.rocket.promotion;

import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.SafeIntent;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.Settings.EventHistory;

/* compiled from: PromotionPresenter.kt */
public final class PromotionModel {
    static final /* synthetic */ KProperty[] $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateDialog", "getDidShowRateDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowShareDialog", "getDidShowShareDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "isSurveyEnabled", "isSurveyEnabled()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateAppNotification", "getDidShowRateAppNotification()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didDismissRateDialog", "getDidDismissRateDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "appCreateCount", "getAppCreateCount()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppDialogThreshold", "getRateAppDialogThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppNotificationThreshold", "getRateAppNotificationThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shareAppDialogThreshold", "getShareAppDialogThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shouldShowPrivacyPolicyUpdate", "getShouldShowPrivacyPolicyUpdate()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "showRateAppDialogFromIntent", "getShowRateAppDialogFromIntent()Z"))};
    private final ReadWriteProperty appCreateCount$delegate;
    private final ReadWriteProperty didDismissRateDialog$delegate;
    private final ReadWriteProperty didShowRateAppNotification$delegate;
    private final ReadWriteProperty didShowRateDialog$delegate;
    private final ReadWriteProperty didShowShareDialog$delegate;
    private final ReadWriteProperty isSurveyEnabled$delegate;
    private final ReadWriteProperty rateAppDialogThreshold$delegate;
    private final ReadWriteProperty rateAppNotificationThreshold$delegate;
    private final ReadWriteProperty shareAppDialogThreshold$delegate;
    private final ReadWriteProperty shouldShowPrivacyPolicyUpdate$delegate;
    private final ReadWriteProperty showRateAppDialogFromIntent$delegate;

    public final int getAppCreateCount() {
        return ((Number) this.appCreateCount$delegate.getValue(this, $$delegatedProperties[5])).intValue();
    }

    public final boolean getDidDismissRateDialog() {
        return ((Boolean) this.didDismissRateDialog$delegate.getValue(this, $$delegatedProperties[4])).booleanValue();
    }

    public final boolean getDidShowRateAppNotification() {
        return ((Boolean) this.didShowRateAppNotification$delegate.getValue(this, $$delegatedProperties[3])).booleanValue();
    }

    public final boolean getDidShowRateDialog() {
        return ((Boolean) this.didShowRateDialog$delegate.getValue(this, $$delegatedProperties[0])).booleanValue();
    }

    public final boolean getDidShowShareDialog() {
        return ((Boolean) this.didShowShareDialog$delegate.getValue(this, $$delegatedProperties[1])).booleanValue();
    }

    public final long getRateAppDialogThreshold() {
        return ((Number) this.rateAppDialogThreshold$delegate.getValue(this, $$delegatedProperties[6])).longValue();
    }

    public final long getRateAppNotificationThreshold() {
        return ((Number) this.rateAppNotificationThreshold$delegate.getValue(this, $$delegatedProperties[7])).longValue();
    }

    public final long getShareAppDialogThreshold() {
        return ((Number) this.shareAppDialogThreshold$delegate.getValue(this, $$delegatedProperties[8])).longValue();
    }

    public final boolean getShouldShowPrivacyPolicyUpdate() {
        return ((Boolean) this.shouldShowPrivacyPolicyUpdate$delegate.getValue(this, $$delegatedProperties[9])).booleanValue();
    }

    public final boolean getShowRateAppDialogFromIntent() {
        return ((Boolean) this.showRateAppDialogFromIntent$delegate.getValue(this, $$delegatedProperties[10])).booleanValue();
    }

    public final boolean isSurveyEnabled() {
        return ((Boolean) this.isSurveyEnabled$delegate.getValue(this, $$delegatedProperties[2])).booleanValue();
    }

    public final void setAppCreateCount(int i) {
        this.appCreateCount$delegate.setValue(this, $$delegatedProperties[5], Integer.valueOf(i));
    }

    public final void setDidDismissRateDialog(boolean z) {
        this.didDismissRateDialog$delegate.setValue(this, $$delegatedProperties[4], Boolean.valueOf(z));
    }

    public final void setDidShowRateAppNotification(boolean z) {
        this.didShowRateAppNotification$delegate.setValue(this, $$delegatedProperties[3], Boolean.valueOf(z));
    }

    public final void setDidShowRateDialog(boolean z) {
        this.didShowRateDialog$delegate.setValue(this, $$delegatedProperties[0], Boolean.valueOf(z));
    }

    public final void setDidShowShareDialog(boolean z) {
        this.didShowShareDialog$delegate.setValue(this, $$delegatedProperties[1], Boolean.valueOf(z));
    }

    public final void setRateAppDialogThreshold(long j) {
        this.rateAppDialogThreshold$delegate.setValue(this, $$delegatedProperties[6], Long.valueOf(j));
    }

    public final void setRateAppNotificationThreshold(long j) {
        this.rateAppNotificationThreshold$delegate.setValue(this, $$delegatedProperties[7], Long.valueOf(j));
    }

    public final void setShareAppDialogThreshold(long j) {
        this.shareAppDialogThreshold$delegate.setValue(this, $$delegatedProperties[8], Long.valueOf(j));
    }

    public final void setShouldShowPrivacyPolicyUpdate(boolean z) {
        this.shouldShowPrivacyPolicyUpdate$delegate.setValue(this, $$delegatedProperties[9], Boolean.valueOf(z));
    }

    public final void setShowRateAppDialogFromIntent(boolean z) {
        this.showRateAppDialogFromIntent$delegate.setValue(this, $$delegatedProperties[10], Boolean.valueOf(z));
    }

    public final void setSurveyEnabled(boolean z) {
        this.isSurveyEnabled$delegate.setValue(this, $$delegatedProperties[2], Boolean.valueOf(z));
    }

    public PromotionModel(Context context, SafeIntent safeIntent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(safeIntent, "safeIntent");
        Settings instance = Settings.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(context)");
        EventHistory eventHistory = instance.getEventHistory();
        Intrinsics.checkExpressionValueIsNotNull(eventHistory, "Settings.getInstance(context).eventHistory");
        NewFeatureNotice instance2 = NewFeatureNotice.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(instance2, "NewFeatureNotice.getInstance(context)");
        this(context, eventHistory, instance2, safeIntent);
    }

    public PromotionModel(Context context, EventHistory eventHistory, NewFeatureNotice newFeatureNotice, SafeIntent safeIntent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(eventHistory, "history");
        Intrinsics.checkParameterIsNotNull(newFeatureNotice, "newFeatureNotice");
        Intrinsics.checkParameterIsNotNull(safeIntent, "safeIntent");
        this.didShowRateDialog$delegate = Delegates.INSTANCE.notNull();
        this.didShowShareDialog$delegate = Delegates.INSTANCE.notNull();
        this.isSurveyEnabled$delegate = Delegates.INSTANCE.notNull();
        this.didShowRateAppNotification$delegate = Delegates.INSTANCE.notNull();
        this.didDismissRateDialog$delegate = Delegates.INSTANCE.notNull();
        this.appCreateCount$delegate = Delegates.INSTANCE.notNull();
        this.rateAppDialogThreshold$delegate = Delegates.INSTANCE.notNull();
        this.rateAppNotificationThreshold$delegate = Delegates.INSTANCE.notNull();
        this.shareAppDialogThreshold$delegate = Delegates.INSTANCE.notNull();
        this.shouldShowPrivacyPolicyUpdate$delegate = Delegates.INSTANCE.notNull();
        this.showRateAppDialogFromIntent$delegate = Delegates.INSTANCE.notNull();
        parseIntent(safeIntent);
        setDidShowRateDialog(eventHistory.contains("show_rate_app_dialog"));
        setDidShowShareDialog(eventHistory.contains("show_share_app_dialog"));
        setDidDismissRateDialog(eventHistory.contains("dismiss_rate_app_dialog"));
        setDidShowRateAppNotification(eventHistory.contains("show_rate_app_notification"));
        boolean z = AppConfigWrapper.isSurveyNotificationEnabled() && !eventHistory.contains("post_survey_notification");
        setSurveyEnabled(z);
        if (accumulateAppCreateCount()) {
            eventHistory.add("app_create");
        }
        setAppCreateCount(eventHistory.getCount("app_create"));
        setRateAppDialogThreshold(AppConfigWrapper.getRateDialogLaunchTimeThreshold(context));
        setRateAppNotificationThreshold(AppConfigWrapper.getRateAppNotificationLaunchTimeThreshold(context));
        setShareAppDialogThreshold(AppConfigWrapper.getShareDialogLaunchTimeThreshold(context, getDidDismissRateDialog()));
        setShouldShowPrivacyPolicyUpdate(newFeatureNotice.shouldShowPrivacyPolicyUpdate());
    }

    public final void parseIntent(SafeIntent safeIntent) {
        boolean z = true;
        if (safeIntent == null || !safeIntent.getBooleanExtra("show_rate_dialog", false)) {
            z = false;
        }
        setShowRateAppDialogFromIntent(z);
    }

    private final boolean accumulateAppCreateCount() {
        return (getDidShowRateDialog() && getDidShowShareDialog() && !isSurveyEnabled() && getDidShowRateAppNotification()) ? false : true;
    }
}
