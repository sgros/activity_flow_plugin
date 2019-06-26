package org.telegram.ui.Components;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class TermsOfServiceView extends FrameLayout {
   private int currentAccount;
   private TLRPC.TL_help_termsOfService currentTos;
   private TermsOfServiceView.TermsOfServiceViewDelegate delegate;
   private ScrollView scrollView;
   private TextView textView;
   private TextView titleTextView;

   public TermsOfServiceView(Context var1) {
      super(var1);
      this.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      int var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = (int)((float)AndroidUtilities.statusBarHeight / AndroidUtilities.density);
      } else {
         var2 = 0;
      }

      if (VERSION.SDK_INT >= 21) {
         View var3 = new View(var1);
         var3.setBackgroundColor(-16777216);
         this.addView(var3, new LayoutParams(-1, AndroidUtilities.statusBarHeight));
      }

      ImageView var6 = new ImageView(var1);
      var6.setImageResource(2131165550);
      this.addView(var6, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, (float)(var2 + 30), 0.0F, 0.0F));
      this.titleTextView = new TextView(var1);
      this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.titleTextView.setTextSize(1, 17.0F);
      this.titleTextView.setGravity(51);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.titleTextView.setText(LocaleController.getString("PrivacyPolicyAndTerms", 2131560503));
      this.addView(this.titleTextView, LayoutHelper.createFrame(-2, -2.0F, 51, 27.0F, (float)(var2 + 126), 27.0F, 75.0F));
      this.scrollView = new ScrollView(var1);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
      this.addView(this.scrollView, LayoutHelper.createFrame(-2, -1.0F, 51, 27.0F, (float)(var2 + 160), 27.0F, 75.0F));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
      this.textView.setGravity(51);
      this.textView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
      this.scrollView.addView(this.textView, new LayoutParams(-2, -2));
      TextView var7 = new TextView(var1);
      var7.setText(LocaleController.getString("Decline", 2131559223).toUpperCase());
      var7.setGravity(17);
      var7.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var7.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
      var7.setTextSize(1, 16.0F);
      var7.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F));
      this.addView(var7, LayoutHelper.createFrame(-2, -2.0F, 83, 16.0F, 0.0F, 16.0F, 16.0F));
      var7.setOnClickListener(new _$$Lambda$TermsOfServiceView$iC8ls3ZLGFSjERsGKqRFXaObEoI(this));
      TextView var5 = new TextView(var1);
      var5.setText(LocaleController.getString("Accept", 2131558484).toUpperCase());
      var5.setGravity(17);
      var5.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var5.setTextColor(-1);
      var5.setTextSize(1, 16.0F);
      var5.setBackgroundResource(2131165800);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var8 = new StateListAnimator();
         ObjectAnimator var4 = ObjectAnimator.ofFloat(var5, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var8.addState(new int[]{16842919}, var4);
         var4 = ObjectAnimator.ofFloat(var5, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var8.addState(new int[0], var4);
         var5.setStateListAnimator(var8);
      }

      var5.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F));
      this.addView(var5, LayoutHelper.createFrame(-2, -2.0F, 85, 16.0F, 0.0F, 16.0F, 16.0F));
      var5.setOnClickListener(new _$$Lambda$TermsOfServiceView$SxA1BjEDdG4D6RyZHS_wxrllcTk(this));
   }

   private void accept() {
      this.delegate.onAcceptTerms(this.currentAccount);
      TLRPC.TL_help_acceptTermsOfService var1 = new TLRPC.TL_help_acceptTermsOfService();
      var1.id = this.currentTos.id;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, _$$Lambda$TermsOfServiceView$YbrdSyJlv9WqhaCW47_DkBCPOAM.INSTANCE);
   }

   // $FF: synthetic method
   static void lambda$accept$7(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   public void lambda$new$4$TermsOfServiceView(View var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(var1.getContext());
      var2.setTitle(LocaleController.getString("TermsOfService", 2131560885));
      var2.setPositiveButton(LocaleController.getString("DeclineDeactivate", 2131559224), new _$$Lambda$TermsOfServiceView$8TrTmo7wbIKz6qyAsVbWamu37pI(this));
      var2.setNegativeButton(LocaleController.getString("Back", 2131558809), (OnClickListener)null);
      var2.setMessage(LocaleController.getString("TosUpdateDecline", 2131560914));
      var2.show();
   }

   // $FF: synthetic method
   public void lambda$new$6$TermsOfServiceView(View var1) {
      if (this.currentTos.min_age_confirm != 0) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(var1.getContext());
         var2.setTitle(LocaleController.getString("TosAgeTitle", 2131560911));
         var2.setPositiveButton(LocaleController.getString("Agree", 2131558599), new _$$Lambda$TermsOfServiceView$SeQmuFflbDIWWWYA0Y43_VRunls(this));
         var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var2.setMessage(LocaleController.formatString("TosAgeText", 2131560910, LocaleController.formatPluralString("Years", this.currentTos.min_age_confirm)));
         var2.show();
      } else {
         this.accept();
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$TermsOfServiceView(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (var2 instanceof TLRPC.TL_boolTrue) {
         MessagesController.getInstance(this.currentAccount).performLogout(0);
      } else {
         String var7 = LocaleController.getString("ErrorOccurred", 2131559375);
         String var5 = var7;
         if (var3 != null) {
            StringBuilder var6 = new StringBuilder();
            var6.append(var7);
            var6.append("\n");
            var6.append(var3.text);
            var5 = var6.toString();
         }

         AlertDialog.Builder var8 = new AlertDialog.Builder(this.getContext());
         var8.setTitle(LocaleController.getString("AppName", 2131558635));
         var8.setMessage(var5);
         var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var8.show();
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$TermsOfServiceView(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$TermsOfServiceView$WGt9_Qk4hrF5TQ_c_TZPa64SZbw(this, var1, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$null$2$TermsOfServiceView(DialogInterface var1, int var2) {
      AlertDialog var4 = new AlertDialog(this.getContext(), 3);
      var4.setCanCacnel(false);
      TLRPC.TL_account_deleteAccount var3 = new TLRPC.TL_account_deleteAccount();
      var3.reason = "Decline ToS update";
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI(this, var4));
      var4.show();
   }

   // $FF: synthetic method
   public void lambda$null$3$TermsOfServiceView(DialogInterface var1, int var2) {
      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getContext());
      var3.setMessage(LocaleController.getString("TosDeclineDeleteAccount", 2131560913));
      var3.setTitle(LocaleController.getString("AppName", 2131558635));
      var3.setPositiveButton(LocaleController.getString("Deactivate", 2131559207), new _$$Lambda$TermsOfServiceView$hBaTjsArXh_sRa7tZwKAz5EvAJE(this));
      var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var3.show();
   }

   // $FF: synthetic method
   public void lambda$null$5$TermsOfServiceView(DialogInterface var1, int var2) {
      this.accept();
   }

   protected void onMeasure(int var1, int var2) {
      this.measureChildWithMargins(this.titleTextView, var1, 0, var2, 0);
      ((LayoutParams)this.scrollView.getLayoutParams()).topMargin = AndroidUtilities.dp(156.0F) + this.titleTextView.getMeasuredHeight();
      super.onMeasure(var1, var2);
   }

   public void setDelegate(TermsOfServiceView.TermsOfServiceViewDelegate var1) {
      this.delegate = var1;
   }

   public void show(int var1, TLRPC.TL_help_termsOfService var2) {
      if (this.getVisibility() != 0) {
         this.setVisibility(0);
      }

      SpannableStringBuilder var3 = new SpannableStringBuilder(var2.text);
      MessageObject.addEntitiesToText(var3, var2.entities, false, 0, false, false, false);
      this.textView.setText(var3);
      this.currentTos = var2;
      this.currentAccount = var1;
   }

   public interface TermsOfServiceViewDelegate {
      void onAcceptTerms(int var1);

      void onDeclineTerms(int var1);
   }
}
