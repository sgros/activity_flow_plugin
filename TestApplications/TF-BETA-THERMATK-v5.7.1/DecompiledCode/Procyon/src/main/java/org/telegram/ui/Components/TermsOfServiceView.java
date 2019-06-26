// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.MessageObject;
import android.text.SpannableStringBuilder;
import android.content.DialogInterface;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.FileLog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.view.View$OnClickListener;
import android.text.method.MovementMethod;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ScrollView;
import org.telegram.tgnet.TLRPC;
import android.widget.FrameLayout;

public class TermsOfServiceView extends FrameLayout
{
    private int currentAccount;
    private TLRPC.TL_help_termsOfService currentTos;
    private TermsOfServiceViewDelegate delegate;
    private ScrollView scrollView;
    private TextView textView;
    private TextView titleTextView;
    
    public TermsOfServiceView(final Context context) {
        super(context);
        this.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        int n;
        if (Build$VERSION.SDK_INT >= 21) {
            n = (int)(AndroidUtilities.statusBarHeight / AndroidUtilities.density);
        }
        else {
            n = 0;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final View view = new View(context);
            view.setBackgroundColor(-16777216);
            this.addView(view, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, AndroidUtilities.statusBarHeight));
        }
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(2131165550);
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, (float)(n + 30), 0.0f, 0.0f));
        (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleTextView.setTextSize(1, 17.0f);
        this.titleTextView.setGravity(51);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setText((CharSequence)LocaleController.getString("PrivacyPolicyAndTerms", 2131560503));
        this.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 27.0f, (float)(n + 126), 27.0f, 75.0f));
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView = new ScrollView(context), Theme.getColor("actionBarDefault"));
        this.addView((View)this.scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 51, 27.0f, (float)(n + 160), 27.0f, 75.0f));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
        this.textView.setGravity(51);
        this.textView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
        this.scrollView.addView((View)this.textView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)LocaleController.getString("Decline", 2131559223).toUpperCase());
        textView.setGravity(17);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        textView.setTextSize(1, 16.0f);
        textView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f));
        this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 83, 16.0f, 0.0f, 16.0f, 16.0f));
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$TermsOfServiceView$iC8ls3ZLGFSjERsGKqRFXaObEoI(this));
        final TextView textView2 = new TextView(context);
        textView2.setText((CharSequence)LocaleController.getString("Accept", 2131558484).toUpperCase());
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setTextColor(-1);
        textView2.setTextSize(1, 16.0f);
        textView2.setBackgroundResource(2131165800);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)textView2, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)textView2, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            textView2.setStateListAnimator(stateListAnimator);
        }
        textView2.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f));
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 85, 16.0f, 0.0f, 16.0f, 16.0f));
        textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$TermsOfServiceView$SxA1BjEDdG4D6RyZHS_wxrllcTk(this));
    }
    
    private void accept() {
        this.delegate.onAcceptTerms(this.currentAccount);
        final TLRPC.TL_help_acceptTermsOfService tl_help_acceptTermsOfService = new TLRPC.TL_help_acceptTermsOfService();
        tl_help_acceptTermsOfService.id = this.currentTos.id;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_help_acceptTermsOfService, (RequestDelegate)_$$Lambda$TermsOfServiceView$YbrdSyJlv9WqhaCW47_DkBCPOAM.INSTANCE);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.measureChildWithMargins((View)this.titleTextView, n, 0, n2, 0);
        ((FrameLayout$LayoutParams)this.scrollView.getLayoutParams()).topMargin = AndroidUtilities.dp(156.0f) + this.titleTextView.getMeasuredHeight();
        super.onMeasure(n, n2);
    }
    
    public void setDelegate(final TermsOfServiceViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void show(final int currentAccount, final TLRPC.TL_help_termsOfService currentTos) {
        if (this.getVisibility() != 0) {
            this.setVisibility(0);
        }
        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)currentTos.text);
        MessageObject.addEntitiesToText((CharSequence)text, currentTos.entities, false, 0, false, false, false);
        this.textView.setText((CharSequence)text);
        this.currentTos = currentTos;
        this.currentAccount = currentAccount;
    }
    
    public interface TermsOfServiceViewDelegate
    {
        void onAcceptTerms(final int p0);
        
        void onDeclineTerms(final int p0);
    }
}
