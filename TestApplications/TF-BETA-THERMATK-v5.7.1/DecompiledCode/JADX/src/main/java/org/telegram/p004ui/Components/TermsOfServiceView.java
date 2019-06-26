package org.telegram.p004ui.Components;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AndroidUtilities.LinkMovementMethodMy;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_deleteAccount;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_help_acceptTermsOfService;
import org.telegram.tgnet.TLRPC.TL_help_termsOfService;

/* renamed from: org.telegram.ui.Components.TermsOfServiceView */
public class TermsOfServiceView extends FrameLayout {
    private int currentAccount;
    private TL_help_termsOfService currentTos;
    private TermsOfServiceViewDelegate delegate;
    private ScrollView scrollView;
    private TextView textView;
    private TextView titleTextView;

    /* renamed from: org.telegram.ui.Components.TermsOfServiceView$TermsOfServiceViewDelegate */
    public interface TermsOfServiceViewDelegate {
        void onAcceptTerms(int i);

        void onDeclineTerms(int i);
    }

    static /* synthetic */ void lambda$accept$7(TLObject tLObject, TL_error tL_error) {
    }

    public TermsOfServiceView(Context context) {
        Context context2 = context;
        super(context);
        setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        int i = VERSION.SDK_INT >= 21 ? (int) (((float) AndroidUtilities.statusBarHeight) / AndroidUtilities.density) : 0;
        if (VERSION.SDK_INT >= 21) {
            View view = new View(context2);
            view.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
            addView(view, new LayoutParams(-1, AndroidUtilities.statusBarHeight));
        }
        ImageView imageView = new ImageView(context2);
        imageView.setImageResource(C1067R.C1065drawable.logo_middle);
        addView(imageView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, (float) (i + 30), 0.0f, 0.0f));
        this.titleTextView = new TextView(context2);
        TextView textView = this.titleTextView;
        String str = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(Theme.getColor(str));
        this.titleTextView.setTextSize(1, 17.0f);
        this.titleTextView.setGravity(51);
        String str2 = "fonts/rmedium.ttf";
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface(str2));
        this.titleTextView.setText(LocaleController.getString("PrivacyPolicyAndTerms", C1067R.string.PrivacyPolicyAndTerms));
        addView(this.titleTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 27.0f, (float) (i + 126), 27.0f, 75.0f));
        this.scrollView = new ScrollView(context2);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_actionBarDefault));
        addView(this.scrollView, LayoutHelper.createFrame(-2, -1.0f, 51, 27.0f, (float) (i + 160), 27.0f, 75.0f));
        this.textView = new TextView(context2);
        this.textView.setTextColor(Theme.getColor(str));
        this.textView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setMovementMethod(new LinkMovementMethodMy());
        this.textView.setGravity(51);
        this.textView.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
        this.scrollView.addView(this.textView, new LayoutParams(-2, -2));
        TextView textView2 = new TextView(context2);
        textView2.setText(LocaleController.getString("Decline", C1067R.string.Decline).toUpperCase());
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.getTypeface(str2));
        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        textView2.setTextSize(1, 16.0f);
        textView2.setPadding(AndroidUtilities.m26dp(20.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(20.0f), AndroidUtilities.m26dp(10.0f));
        addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 83, 16.0f, 0.0f, 16.0f, 16.0f));
        textView2.setOnClickListener(new C2695-$$Lambda$TermsOfServiceView$iC8ls3ZLGFSjERsGKqRFXaObEoI(this));
        textView2 = new TextView(context2);
        textView2.setText(LocaleController.getString("Accept", C1067R.string.Accept).toUpperCase());
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.getTypeface(str2));
        textView2.setTextColor(-1);
        textView2.setTextSize(1, 16.0f);
        textView2.setBackgroundResource(C1067R.C1065drawable.regbtn_states);
        if (VERSION.SDK_INT >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            String str3 = "translationZ";
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(textView2, str3, new float[]{(float) AndroidUtilities.m26dp(2.0f), (float) AndroidUtilities.m26dp(4.0f)}).setDuration(200));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(textView2, str3, new float[]{(float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(2.0f)}).setDuration(200));
            textView2.setStateListAnimator(stateListAnimator);
        }
        textView2.setPadding(AndroidUtilities.m26dp(20.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(20.0f), AndroidUtilities.m26dp(10.0f));
        addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 85, 16.0f, 0.0f, 16.0f, 16.0f));
        textView2.setOnClickListener(new C2692-$$Lambda$TermsOfServiceView$SxA1BjEDdG4D6RyZHS_wxrllcTk(this));
    }

    public /* synthetic */ void lambda$new$4$TermsOfServiceView(View view) {
        Builder builder = new Builder(view.getContext());
        builder.setTitle(LocaleController.getString("TermsOfService", C1067R.string.TermsOfService));
        builder.setPositiveButton(LocaleController.getString("DeclineDeactivate", C1067R.string.DeclineDeactivate), new C2690-$$Lambda$TermsOfServiceView$8TrTmo7wbIKz6qyAsVbWamu37pI(this));
        builder.setNegativeButton(LocaleController.getString("Back", C1067R.string.Back), null);
        builder.setMessage(LocaleController.getString("TosUpdateDecline", C1067R.string.TosUpdateDecline));
        builder.show();
    }

    public /* synthetic */ void lambda$null$3$TermsOfServiceView(DialogInterface dialogInterface, int i) {
        Builder builder = new Builder(getContext());
        builder.setMessage(LocaleController.getString("TosDeclineDeleteAccount", C1067R.string.TosDeclineDeleteAccount));
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("Deactivate", C1067R.string.Deactivate), new C2694-$$Lambda$TermsOfServiceView$hBaTjsArXh_sRa7tZwKAz5EvAJE(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        builder.show();
    }

    public /* synthetic */ void lambda$null$2$TermsOfServiceView(DialogInterface dialogInterface, int i) {
        AlertDialog alertDialog = new AlertDialog(getContext(), 3);
        alertDialog.setCanCacnel(false);
        TL_account_deleteAccount tL_account_deleteAccount = new TL_account_deleteAccount();
        tL_account_deleteAccount.reason = "Decline ToS update";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_deleteAccount, new C4067-$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI(this, alertDialog));
        alertDialog.show();
    }

    public /* synthetic */ void lambda$null$1$TermsOfServiceView(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C2693-$$Lambda$TermsOfServiceView$WGt9_Qk4hrF5TQ_c-TZPa64SZbw(this, alertDialog, tLObject, tL_error));
    }

    public /* synthetic */ void lambda$null$0$TermsOfServiceView(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject instanceof TL_boolTrue) {
            MessagesController.getInstance(this.currentAccount).performLogout(0);
            return;
        }
        CharSequence string = LocaleController.getString("ErrorOccurred", C1067R.string.ErrorOccurred);
        if (tL_error != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string);
            stringBuilder.append("\n");
            stringBuilder.append(tL_error.text);
            string = stringBuilder.toString();
        }
        Builder builder = new Builder(getContext());
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setMessage(string);
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
        builder.show();
    }

    public /* synthetic */ void lambda$new$6$TermsOfServiceView(View view) {
        if (this.currentTos.min_age_confirm != 0) {
            Builder builder = new Builder(view.getContext());
            builder.setTitle(LocaleController.getString("TosAgeTitle", C1067R.string.TosAgeTitle));
            builder.setPositiveButton(LocaleController.getString("Agree", C1067R.string.Agree), new C2691-$$Lambda$TermsOfServiceView$SeQmuFflbDIWWWYA0Y43_VRunls(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
            Object[] objArr = new Object[1];
            objArr[0] = LocaleController.formatPluralString("Years", this.currentTos.min_age_confirm);
            builder.setMessage(LocaleController.formatString("TosAgeText", C1067R.string.TosAgeText, objArr));
            builder.show();
            return;
        }
        accept();
    }

    public /* synthetic */ void lambda$null$5$TermsOfServiceView(DialogInterface dialogInterface, int i) {
        accept();
    }

    private void accept() {
        this.delegate.onAcceptTerms(this.currentAccount);
        TL_help_acceptTermsOfService tL_help_acceptTermsOfService = new TL_help_acceptTermsOfService();
        tL_help_acceptTermsOfService.f489id = this.currentTos.f490id;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_help_acceptTermsOfService, C4066-$$Lambda$TermsOfServiceView$YbrdSyJlv9WqhaCW47-DkBCPOAM.INSTANCE);
    }

    public void show(int i, TL_help_termsOfService tL_help_termsOfService) {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tL_help_termsOfService.text);
        MessageObject.addEntitiesToText(spannableStringBuilder, tL_help_termsOfService.entities, false, 0, false, false, false);
        this.textView.setText(spannableStringBuilder);
        this.currentTos = tL_help_termsOfService;
        this.currentAccount = i;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        measureChildWithMargins(this.titleTextView, i, 0, i2, 0);
        ((LayoutParams) this.scrollView.getLayoutParams()).topMargin = AndroidUtilities.m26dp(156.0f) + this.titleTextView.getMeasuredHeight();
        super.onMeasure(i, i2);
    }

    public void setDelegate(TermsOfServiceViewDelegate termsOfServiceViewDelegate) {
        this.delegate = termsOfServiceViewDelegate;
    }
}
