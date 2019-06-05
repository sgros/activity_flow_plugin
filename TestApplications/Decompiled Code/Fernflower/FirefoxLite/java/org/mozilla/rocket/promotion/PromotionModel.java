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

public final class PromotionModel {
   // $FF: synthetic field
   static final KProperty[] $$delegatedProperties = new KProperty[]{(KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateDialog", "getDidShowRateDialog()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowShareDialog", "getDidShowShareDialog()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "isSurveyEnabled", "isSurveyEnabled()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didShowRateAppNotification", "getDidShowRateAppNotification()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "didDismissRateDialog", "getDidDismissRateDialog()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "appCreateCount", "getAppCreateCount()I")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppDialogThreshold", "getRateAppDialogThreshold()J")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "rateAppNotificationThreshold", "getRateAppNotificationThreshold()J")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shareAppDialogThreshold", "getShareAppDialogThreshold()J")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "shouldShowPrivacyPolicyUpdate", "getShouldShowPrivacyPolicyUpdate()Z")), (KProperty)Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(PromotionModel.class), "showRateAppDialogFromIntent", "getShowRateAppDialogFromIntent()Z"))};
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

   public PromotionModel(Context var1, SafeIntent var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "safeIntent");
      Settings var3 = Settings.getInstance(var1);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Settings.getInstance(context)");
      Settings.EventHistory var4 = var3.getEventHistory();
      Intrinsics.checkExpressionValueIsNotNull(var4, "Settings.getInstance(context).eventHistory");
      NewFeatureNotice var5 = NewFeatureNotice.getInstance(var1);
      Intrinsics.checkExpressionValueIsNotNull(var5, "NewFeatureNotice.getInstance(context)");
      this(var1, var4, var5, var2);
   }

   public PromotionModel(Context var1, Settings.EventHistory var2, NewFeatureNotice var3, SafeIntent var4) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "history");
      Intrinsics.checkParameterIsNotNull(var3, "newFeatureNotice");
      Intrinsics.checkParameterIsNotNull(var4, "safeIntent");
      super();
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
      this.parseIntent(var4);
      this.setDidShowRateDialog(var2.contains("show_rate_app_dialog"));
      this.setDidShowShareDialog(var2.contains("show_share_app_dialog"));
      this.setDidDismissRateDialog(var2.contains("dismiss_rate_app_dialog"));
      this.setDidShowRateAppNotification(var2.contains("show_rate_app_notification"));
      boolean var5;
      if (AppConfigWrapper.isSurveyNotificationEnabled() && !var2.contains("post_survey_notification")) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.setSurveyEnabled(var5);
      if (this.accumulateAppCreateCount()) {
         var2.add("app_create");
      }

      this.setAppCreateCount(var2.getCount("app_create"));
      this.setRateAppDialogThreshold(AppConfigWrapper.getRateDialogLaunchTimeThreshold(var1));
      this.setRateAppNotificationThreshold(AppConfigWrapper.getRateAppNotificationLaunchTimeThreshold(var1));
      this.setShareAppDialogThreshold(AppConfigWrapper.getShareDialogLaunchTimeThreshold(var1, this.getDidDismissRateDialog()));
      this.setShouldShowPrivacyPolicyUpdate(var3.shouldShowPrivacyPolicyUpdate());
   }

   private final boolean accumulateAppCreateCount() {
      boolean var1;
      if (this.getDidShowRateDialog() && this.getDidShowShareDialog() && !this.isSurveyEnabled() && this.getDidShowRateAppNotification()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public final int getAppCreateCount() {
      return ((Number)this.appCreateCount$delegate.getValue(this, $$delegatedProperties[5])).intValue();
   }

   public final boolean getDidDismissRateDialog() {
      return (Boolean)this.didDismissRateDialog$delegate.getValue(this, $$delegatedProperties[4]);
   }

   public final boolean getDidShowRateAppNotification() {
      return (Boolean)this.didShowRateAppNotification$delegate.getValue(this, $$delegatedProperties[3]);
   }

   public final boolean getDidShowRateDialog() {
      return (Boolean)this.didShowRateDialog$delegate.getValue(this, $$delegatedProperties[0]);
   }

   public final boolean getDidShowShareDialog() {
      return (Boolean)this.didShowShareDialog$delegate.getValue(this, $$delegatedProperties[1]);
   }

   public final long getRateAppDialogThreshold() {
      return ((Number)this.rateAppDialogThreshold$delegate.getValue(this, $$delegatedProperties[6])).longValue();
   }

   public final long getRateAppNotificationThreshold() {
      return ((Number)this.rateAppNotificationThreshold$delegate.getValue(this, $$delegatedProperties[7])).longValue();
   }

   public final long getShareAppDialogThreshold() {
      return ((Number)this.shareAppDialogThreshold$delegate.getValue(this, $$delegatedProperties[8])).longValue();
   }

   public final boolean getShouldShowPrivacyPolicyUpdate() {
      return (Boolean)this.shouldShowPrivacyPolicyUpdate$delegate.getValue(this, $$delegatedProperties[9]);
   }

   public final boolean getShowRateAppDialogFromIntent() {
      return (Boolean)this.showRateAppDialogFromIntent$delegate.getValue(this, $$delegatedProperties[10]);
   }

   public final boolean isSurveyEnabled() {
      return (Boolean)this.isSurveyEnabled$delegate.getValue(this, $$delegatedProperties[2]);
   }

   public final void parseIntent(SafeIntent var1) {
      boolean var2 = true;
      if (var1 == null || !var1.getBooleanExtra("show_rate_dialog", false)) {
         var2 = false;
      }

      this.setShowRateAppDialogFromIntent(var2);
   }

   public final void setAppCreateCount(int var1) {
      this.appCreateCount$delegate.setValue(this, $$delegatedProperties[5], var1);
   }

   public final void setDidDismissRateDialog(boolean var1) {
      this.didDismissRateDialog$delegate.setValue(this, $$delegatedProperties[4], var1);
   }

   public final void setDidShowRateAppNotification(boolean var1) {
      this.didShowRateAppNotification$delegate.setValue(this, $$delegatedProperties[3], var1);
   }

   public final void setDidShowRateDialog(boolean var1) {
      this.didShowRateDialog$delegate.setValue(this, $$delegatedProperties[0], var1);
   }

   public final void setDidShowShareDialog(boolean var1) {
      this.didShowShareDialog$delegate.setValue(this, $$delegatedProperties[1], var1);
   }

   public final void setRateAppDialogThreshold(long var1) {
      this.rateAppDialogThreshold$delegate.setValue(this, $$delegatedProperties[6], var1);
   }

   public final void setRateAppNotificationThreshold(long var1) {
      this.rateAppNotificationThreshold$delegate.setValue(this, $$delegatedProperties[7], var1);
   }

   public final void setShareAppDialogThreshold(long var1) {
      this.shareAppDialogThreshold$delegate.setValue(this, $$delegatedProperties[8], var1);
   }

   public final void setShouldShowPrivacyPolicyUpdate(boolean var1) {
      this.shouldShowPrivacyPolicyUpdate$delegate.setValue(this, $$delegatedProperties[9], var1);
   }

   public final void setShowRateAppDialogFromIntent(boolean var1) {
      this.showRateAppDialogFromIntent$delegate.setValue(this, $$delegatedProperties[10], var1);
   }

   public final void setSurveyEnabled(boolean var1) {
      this.isSurveyEnabled$delegate.setValue(this, $$delegatedProperties[2], var1);
   }
}
