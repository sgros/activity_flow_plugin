// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.promotion;

import org.mozilla.focus.utils.AppConfigWrapper;
import kotlin.properties.Delegates;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.Settings;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.SafeIntent;
import android.content.Context;
import kotlin.jvm.internal.MutablePropertyReference1;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KMutableProperty1;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

public final class PromotionModel
{
    static final /* synthetic */ KProperty[] $$delegatedProperties;
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
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateDialog", "getDidShowRateDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowShareDialog", "getDidShowShareDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "isSurveyEnabled", "isSurveyEnabled()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateAppNotification", "getDidShowRateAppNotification()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didDismissRateDialog", "getDidDismissRateDialog()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "appCreateCount", "getAppCreateCount()I")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppDialogThreshold", "getRateAppDialogThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppNotificationThreshold", "getRateAppNotificationThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shareAppDialogThreshold", "getShareAppDialogThreshold()J")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shouldShowPrivacyPolicyUpdate", "getShouldShowPrivacyPolicyUpdate()Z")), Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "showRateAppDialogFromIntent", "getShowRateAppDialogFromIntent()Z")) };
    }
    
    public PromotionModel(final Context context, final SafeIntent safeIntent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(safeIntent, "safeIntent");
        final Settings instance = Settings.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(context)");
        final Settings.EventHistory eventHistory = instance.getEventHistory();
        Intrinsics.checkExpressionValueIsNotNull(eventHistory, "Settings.getInstance(context).eventHistory");
        final NewFeatureNotice instance2 = NewFeatureNotice.getInstance(context);
        Intrinsics.checkExpressionValueIsNotNull(instance2, "NewFeatureNotice.getInstance(context)");
        this(context, eventHistory, instance2, safeIntent);
    }
    
    public PromotionModel(final Context context, final Settings.EventHistory eventHistory, final NewFeatureNotice newFeatureNotice, final SafeIntent safeIntent) {
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
        this.parseIntent(safeIntent);
        this.setDidShowRateDialog(eventHistory.contains("show_rate_app_dialog"));
        this.setDidShowShareDialog(eventHistory.contains("show_share_app_dialog"));
        this.setDidDismissRateDialog(eventHistory.contains("dismiss_rate_app_dialog"));
        this.setDidShowRateAppNotification(eventHistory.contains("show_rate_app_notification"));
        this.setSurveyEnabled(AppConfigWrapper.isSurveyNotificationEnabled() && !eventHistory.contains("post_survey_notification"));
        if (this.accumulateAppCreateCount()) {
            eventHistory.add("app_create");
        }
        this.setAppCreateCount(eventHistory.getCount("app_create"));
        this.setRateAppDialogThreshold(AppConfigWrapper.getRateDialogLaunchTimeThreshold(context));
        this.setRateAppNotificationThreshold(AppConfigWrapper.getRateAppNotificationLaunchTimeThreshold(context));
        this.setShareAppDialogThreshold(AppConfigWrapper.getShareDialogLaunchTimeThreshold(context, this.getDidDismissRateDialog()));
        this.setShouldShowPrivacyPolicyUpdate(newFeatureNotice.shouldShowPrivacyPolicyUpdate());
    }
    
    private final boolean accumulateAppCreateCount() {
        return !this.getDidShowRateDialog() || !this.getDidShowShareDialog() || this.isSurveyEnabled() || !this.getDidShowRateAppNotification();
    }
    
    public final int getAppCreateCount() {
        return this.appCreateCount$delegate.getValue(this, PromotionModel.$$delegatedProperties[5]).intValue();
    }
    
    public final boolean getDidDismissRateDialog() {
        return this.didDismissRateDialog$delegate.getValue(this, PromotionModel.$$delegatedProperties[4]);
    }
    
    public final boolean getDidShowRateAppNotification() {
        return this.didShowRateAppNotification$delegate.getValue(this, PromotionModel.$$delegatedProperties[3]);
    }
    
    public final boolean getDidShowRateDialog() {
        return this.didShowRateDialog$delegate.getValue(this, PromotionModel.$$delegatedProperties[0]);
    }
    
    public final boolean getDidShowShareDialog() {
        return this.didShowShareDialog$delegate.getValue(this, PromotionModel.$$delegatedProperties[1]);
    }
    
    public final long getRateAppDialogThreshold() {
        return this.rateAppDialogThreshold$delegate.getValue(this, PromotionModel.$$delegatedProperties[6]).longValue();
    }
    
    public final long getRateAppNotificationThreshold() {
        return this.rateAppNotificationThreshold$delegate.getValue(this, PromotionModel.$$delegatedProperties[7]).longValue();
    }
    
    public final long getShareAppDialogThreshold() {
        return this.shareAppDialogThreshold$delegate.getValue(this, PromotionModel.$$delegatedProperties[8]).longValue();
    }
    
    public final boolean getShouldShowPrivacyPolicyUpdate() {
        return this.shouldShowPrivacyPolicyUpdate$delegate.getValue(this, PromotionModel.$$delegatedProperties[9]);
    }
    
    public final boolean getShowRateAppDialogFromIntent() {
        return this.showRateAppDialogFromIntent$delegate.getValue(this, PromotionModel.$$delegatedProperties[10]);
    }
    
    public final boolean isSurveyEnabled() {
        return this.isSurveyEnabled$delegate.getValue(this, PromotionModel.$$delegatedProperties[2]);
    }
    
    public final void parseIntent(final SafeIntent safeIntent) {
        boolean showRateAppDialogFromIntent = true;
        if (safeIntent == null || !safeIntent.getBooleanExtra("show_rate_dialog", false)) {
            showRateAppDialogFromIntent = false;
        }
        this.setShowRateAppDialogFromIntent(showRateAppDialogFromIntent);
    }
    
    public final void setAppCreateCount(final int i) {
        this.appCreateCount$delegate.setValue(this, PromotionModel.$$delegatedProperties[5], i);
    }
    
    public final void setDidDismissRateDialog(final boolean b) {
        this.didDismissRateDialog$delegate.setValue(this, PromotionModel.$$delegatedProperties[4], b);
    }
    
    public final void setDidShowRateAppNotification(final boolean b) {
        this.didShowRateAppNotification$delegate.setValue(this, PromotionModel.$$delegatedProperties[3], b);
    }
    
    public final void setDidShowRateDialog(final boolean b) {
        this.didShowRateDialog$delegate.setValue(this, PromotionModel.$$delegatedProperties[0], b);
    }
    
    public final void setDidShowShareDialog(final boolean b) {
        this.didShowShareDialog$delegate.setValue(this, PromotionModel.$$delegatedProperties[1], b);
    }
    
    public final void setRateAppDialogThreshold(final long l) {
        this.rateAppDialogThreshold$delegate.setValue(this, PromotionModel.$$delegatedProperties[6], l);
    }
    
    public final void setRateAppNotificationThreshold(final long l) {
        this.rateAppNotificationThreshold$delegate.setValue(this, PromotionModel.$$delegatedProperties[7], l);
    }
    
    public final void setShareAppDialogThreshold(final long l) {
        this.shareAppDialogThreshold$delegate.setValue(this, PromotionModel.$$delegatedProperties[8], l);
    }
    
    public final void setShouldShowPrivacyPolicyUpdate(final boolean b) {
        this.shouldShowPrivacyPolicyUpdate$delegate.setValue(this, PromotionModel.$$delegatedProperties[9], b);
    }
    
    public final void setShowRateAppDialogFromIntent(final boolean b) {
        this.showRateAppDialogFromIntent$delegate.setValue(this, PromotionModel.$$delegatedProperties[10], b);
    }
    
    public final void setSurveyEnabled(final boolean b) {
        this.isSurveyEnabled$delegate.setValue(this, PromotionModel.$$delegatedProperties[2], b);
    }
}
