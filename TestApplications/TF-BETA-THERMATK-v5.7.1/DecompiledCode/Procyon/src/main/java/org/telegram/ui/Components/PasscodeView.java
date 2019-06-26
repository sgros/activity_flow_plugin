// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Collection;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.InputFilter;
import android.view.View$MeasureSpec;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.view.View$OnTouchListener;
import org.telegram.messenger.NotificationCenter;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.os.SystemClock;
import android.widget.RelativeLayout$LayoutParams;
import android.os.Handler;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.Theme;
import android.widget.RelativeLayout;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ApplicationLoader;
import android.os.Build$VERSION;
import android.app.Activity;
import android.view.View$OnLongClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.Locale;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import org.telegram.messenger.SharedConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.Typeface;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.Rect;
import java.util.ArrayList;
import android.widget.TextView;
import org.telegram.ui.ActionBar.AlertDialog;
import android.widget.ImageView;
import androidx.core.os.CancellationSignal;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class PasscodeView extends FrameLayout
{
    private static final int id_fingerprint_imageview = 1001;
    private static final int id_fingerprint_textview = 1000;
    private static final int[] ids;
    private Drawable backgroundDrawable;
    private FrameLayout backgroundFrameLayout;
    private CancellationSignal cancellationSignal;
    private ImageView checkImage;
    private Runnable checkRunnable;
    private PasscodeViewDelegate delegate;
    private ImageView eraseView;
    private AlertDialog fingerprintDialog;
    private ImageView fingerprintImageView;
    private TextView fingerprintStatusTextView;
    private int keyboardHeight;
    private int lastValue;
    private ArrayList<TextView> lettersTextViews;
    private ArrayList<FrameLayout> numberFrameLayouts;
    private ArrayList<TextView> numberTextViews;
    private FrameLayout numbersFrameLayout;
    private TextView passcodeTextView;
    private EditTextBoldCursor passwordEditText;
    private AnimatingTextView passwordEditText2;
    private FrameLayout passwordFrameLayout;
    private Rect rect;
    private TextView retryTextView;
    private boolean selfCancelled;
    
    static {
        ids = new int[] { 2131230861, 2131230862, 2131230863, 2131230864, 2131230865, 2131230866, 2131230867, 2131230868, 2131230869, 2131230870, 2131230871 };
    }
    
    public PasscodeView(final Context context) {
        super(context);
        int n = 0;
        this.keyboardHeight = 0;
        this.rect = new Rect();
        this.checkRunnable = new Runnable() {
            @Override
            public void run() {
                PasscodeView.this.checkRetryTextView();
                AndroidUtilities.runOnUIThread(PasscodeView.this.checkRunnable, 100L);
            }
        };
        this.setWillNotDraw(false);
        this.setVisibility(8);
        this.addView((View)(this.backgroundFrameLayout = new FrameLayout(context)));
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.backgroundFrameLayout.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        this.backgroundFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.addView((View)(this.passwordFrameLayout = new FrameLayout(context)));
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.passwordFrameLayout.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.gravity = 51;
        this.passwordFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView$ScaleType.FIT_XY);
        imageView.setImageResource(2131165735);
        this.passwordFrameLayout.addView((View)imageView);
        final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)imageView.getLayoutParams();
        if (AndroidUtilities.density < 1.0f) {
            layoutParams3.width = AndroidUtilities.dp(30.0f);
            layoutParams3.height = AndroidUtilities.dp(30.0f);
        }
        else {
            layoutParams3.width = AndroidUtilities.dp(40.0f);
            layoutParams3.height = AndroidUtilities.dp(40.0f);
        }
        layoutParams3.gravity = 81;
        layoutParams3.bottomMargin = AndroidUtilities.dp(100.0f);
        imageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        (this.passcodeTextView = new TextView(context)).setTextColor(-1);
        this.passcodeTextView.setTextSize(1, 14.0f);
        this.passcodeTextView.setGravity(1);
        this.passwordFrameLayout.addView((View)this.passcodeTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 62.0f));
        (this.retryTextView = new TextView(context)).setTextColor(-1);
        this.retryTextView.setTextSize(1, 15.0f);
        this.retryTextView.setGravity(1);
        this.retryTextView.setVisibility(4);
        this.addView((View)this.retryTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        this.passwordEditText2 = new AnimatingTextView(context);
        this.passwordFrameLayout.addView((View)this.passwordEditText2);
        final FrameLayout$LayoutParams layoutParams4 = (FrameLayout$LayoutParams)this.passwordEditText2.getLayoutParams();
        layoutParams4.height = -2;
        layoutParams4.width = -1;
        layoutParams4.leftMargin = AndroidUtilities.dp(70.0f);
        layoutParams4.rightMargin = AndroidUtilities.dp(70.0f);
        layoutParams4.bottomMargin = AndroidUtilities.dp(6.0f);
        layoutParams4.gravity = 81;
        this.passwordEditText2.setLayoutParams((ViewGroup$LayoutParams)layoutParams4);
        (this.passwordEditText = new EditTextBoldCursor(context)).setTextSize(1, 36.0f);
        this.passwordEditText.setTextColor(-1);
        this.passwordEditText.setMaxLines(1);
        this.passwordEditText.setLines(1);
        this.passwordEditText.setGravity(1);
        this.passwordEditText.setSingleLine(true);
        this.passwordEditText.setImeOptions(6);
        this.passwordEditText.setTypeface(Typeface.DEFAULT);
        this.passwordEditText.setBackgroundDrawable((Drawable)null);
        this.passwordEditText.setCursorColor(-1);
        this.passwordEditText.setCursorSize(AndroidUtilities.dp(32.0f));
        this.passwordFrameLayout.addView((View)this.passwordEditText);
        final FrameLayout$LayoutParams layoutParams5 = (FrameLayout$LayoutParams)this.passwordEditText.getLayoutParams();
        layoutParams5.height = -2;
        layoutParams5.width = -1;
        layoutParams5.leftMargin = AndroidUtilities.dp(70.0f);
        layoutParams5.rightMargin = AndroidUtilities.dp(70.0f);
        layoutParams5.gravity = 81;
        this.passwordEditText.setLayoutParams((ViewGroup$LayoutParams)layoutParams5);
        this.passwordEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PasscodeView$9N26YW5Tms2wrtUaIRATApaOSjo(this));
        this.passwordEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (PasscodeView.this.passwordEditText.length() == 4 && SharedConfig.passcodeType == 0) {
                    PasscodeView.this.processDone(false);
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.passwordEditText.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                return false;
            }
            
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
            
            public void onDestroyActionMode(final ActionMode actionMode) {
            }
            
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
        });
        (this.checkImage = new ImageView(context)).setImageResource(2131165733);
        this.checkImage.setScaleType(ImageView$ScaleType.CENTER);
        this.checkImage.setBackgroundResource(2131165301);
        this.passwordFrameLayout.addView((View)this.checkImage);
        final FrameLayout$LayoutParams layoutParams6 = (FrameLayout$LayoutParams)this.checkImage.getLayoutParams();
        layoutParams6.width = AndroidUtilities.dp(60.0f);
        layoutParams6.height = AndroidUtilities.dp(60.0f);
        layoutParams6.bottomMargin = AndroidUtilities.dp(4.0f);
        layoutParams6.rightMargin = AndroidUtilities.dp(10.0f);
        layoutParams6.gravity = 85;
        this.checkImage.setLayoutParams((ViewGroup$LayoutParams)layoutParams6);
        this.checkImage.setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        this.checkImage.setOnClickListener((View$OnClickListener)new _$$Lambda$PasscodeView$EpKy6ofjnRtgAQLp4vVETcSEcUk(this));
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(654311423);
        this.passwordFrameLayout.addView((View)frameLayout);
        final FrameLayout$LayoutParams layoutParams7 = (FrameLayout$LayoutParams)frameLayout.getLayoutParams();
        layoutParams7.width = -1;
        layoutParams7.height = AndroidUtilities.dp(1.0f);
        layoutParams7.gravity = 83;
        layoutParams7.leftMargin = AndroidUtilities.dp(20.0f);
        layoutParams7.rightMargin = AndroidUtilities.dp(20.0f);
        frameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams7);
        this.addView((View)(this.numbersFrameLayout = new FrameLayout(context)));
        final FrameLayout$LayoutParams layoutParams8 = (FrameLayout$LayoutParams)this.numbersFrameLayout.getLayoutParams();
        layoutParams8.width = -1;
        layoutParams8.height = -1;
        layoutParams8.gravity = 51;
        this.numbersFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams8);
        final int n2 = 10;
        this.lettersTextViews = new ArrayList<TextView>(10);
        this.numberTextViews = new ArrayList<TextView>(10);
        this.numberFrameLayouts = new ArrayList<FrameLayout>(10);
        for (int i = 0; i < 10; ++i) {
            final TextView e = new TextView(context);
            e.setTextColor(-1);
            e.setTextSize(1, 36.0f);
            e.setGravity(17);
            e.setText((CharSequence)String.format(Locale.US, "%d", i));
            this.numbersFrameLayout.addView((View)e);
            final FrameLayout$LayoutParams layoutParams9 = (FrameLayout$LayoutParams)e.getLayoutParams();
            layoutParams9.width = AndroidUtilities.dp(50.0f);
            layoutParams9.height = AndroidUtilities.dp(50.0f);
            layoutParams9.gravity = 51;
            e.setLayoutParams((ViewGroup$LayoutParams)layoutParams9);
            e.setImportantForAccessibility(2);
            this.numberTextViews.add(e);
            final TextView e2 = new TextView(context);
            e2.setTextSize(1, 12.0f);
            e2.setTextColor(Integer.MAX_VALUE);
            e2.setGravity(17);
            this.numbersFrameLayout.addView((View)e2);
            final FrameLayout$LayoutParams layoutParams10 = (FrameLayout$LayoutParams)e2.getLayoutParams();
            layoutParams10.width = AndroidUtilities.dp(50.0f);
            layoutParams10.height = AndroidUtilities.dp(20.0f);
            layoutParams10.gravity = 51;
            e2.setLayoutParams((ViewGroup$LayoutParams)layoutParams10);
            e2.setImportantForAccessibility(2);
            if (i != 0) {
                switch (i) {
                    case 9: {
                        e2.setText((CharSequence)"WXYZ");
                        break;
                    }
                    case 8: {
                        e2.setText((CharSequence)"TUV");
                        break;
                    }
                    case 7: {
                        e2.setText((CharSequence)"PQRS");
                        break;
                    }
                    case 6: {
                        e2.setText((CharSequence)"MNO");
                        break;
                    }
                    case 5: {
                        e2.setText((CharSequence)"JKL");
                        break;
                    }
                    case 4: {
                        e2.setText((CharSequence)"GHI");
                        break;
                    }
                    case 3: {
                        e2.setText((CharSequence)"DEF");
                        break;
                    }
                    case 2: {
                        e2.setText((CharSequence)"ABC");
                        break;
                    }
                }
            }
            else {
                e2.setText((CharSequence)"+");
            }
            this.lettersTextViews.add(e2);
        }
        (this.eraseView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.eraseView.setImageResource(2131165734);
        this.numbersFrameLayout.addView((View)this.eraseView);
        final FrameLayout$LayoutParams layoutParams11 = (FrameLayout$LayoutParams)this.eraseView.getLayoutParams();
        layoutParams11.width = AndroidUtilities.dp(50.0f);
        layoutParams11.height = AndroidUtilities.dp(50.0f);
        layoutParams11.gravity = 51;
        this.eraseView.setLayoutParams((ViewGroup$LayoutParams)layoutParams11);
        int j;
        while (true) {
            j = n2;
            if (n >= 11) {
                break;
            }
            final FrameLayout e3 = new FrameLayout(context) {
                public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Button");
                }
            };
            e3.setBackgroundResource(2131165301);
            e3.setTag((Object)n);
            if (n == 10) {
                e3.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$PasscodeView$dQyZ_TMnXOE3cSzmJWAd_Tm7_ow(this));
                e3.setContentDescription((CharSequence)LocaleController.getString("AccDescrBackspace", 2131558414));
                this.setNextFocus((View)e3, 2131230862);
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(n);
                sb.append("");
                e3.setContentDescription((CharSequence)sb.toString());
                if (n == 0) {
                    this.setNextFocus((View)e3, 2131230871);
                }
                else if (n == 9) {
                    this.setNextFocus((View)e3, 2131230861);
                }
                else {
                    this.setNextFocus((View)e3, PasscodeView.ids[n + 1]);
                }
            }
            e3.setId(PasscodeView.ids[n]);
            e3.setOnClickListener((View$OnClickListener)new _$$Lambda$PasscodeView$9JJmImU9HuNDiFCYKoINiryr54k(this));
            this.numberFrameLayouts.add(e3);
            ++n;
        }
        while (j >= 0) {
            final FrameLayout frameLayout2 = this.numberFrameLayouts.get(j);
            this.numbersFrameLayout.addView((View)frameLayout2);
            final FrameLayout$LayoutParams layoutParams12 = (FrameLayout$LayoutParams)frameLayout2.getLayoutParams();
            layoutParams12.width = AndroidUtilities.dp(100.0f);
            layoutParams12.height = AndroidUtilities.dp(100.0f);
            layoutParams12.gravity = 51;
            frameLayout2.setLayoutParams((ViewGroup$LayoutParams)layoutParams12);
            --j;
        }
    }
    
    private void checkFingerprint() {
        final Activity activity = (Activity)this.getContext();
        if (Build$VERSION.SDK_INT < 23 || activity == null || !SharedConfig.useFingerprint || ApplicationLoader.mainInterfacePaused) {
            return;
        }
        try {
            if (this.fingerprintDialog != null && this.fingerprintDialog.isShowing()) {
                return;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            final FingerprintManagerCompat from = FingerprintManagerCompat.from(ApplicationLoader.applicationContext);
            if (from.isHardwareDetected() && from.hasEnrolledFingerprints()) {
                final RelativeLayout view = new RelativeLayout(this.getContext());
                view.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                final TextView textView = new TextView(this.getContext());
                textView.setId(1000);
                textView.setTextAppearance(16974344);
                textView.setTextColor(Theme.getColor("dialogTextBlack"));
                textView.setText((CharSequence)LocaleController.getString("FingerprintInfo", 2131559492));
                view.addView((View)textView);
                final RelativeLayout$LayoutParams relative = LayoutHelper.createRelative(-2, -2);
                relative.addRule(10);
                relative.addRule(20);
                textView.setLayoutParams((ViewGroup$LayoutParams)relative);
                (this.fingerprintImageView = new ImageView(this.getContext())).setImageResource(2131165442);
                this.fingerprintImageView.setId(1001);
                view.addView((View)this.fingerprintImageView, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-2.0f, -2.0f, 0, 20, 0, 0, 20, 3, 1000));
                (this.fingerprintStatusTextView = new TextView(this.getContext())).setGravity(16);
                this.fingerprintStatusTextView.setText((CharSequence)LocaleController.getString("FingerprintHelp", 2131559491));
                this.fingerprintStatusTextView.setTextAppearance(16974320);
                this.fingerprintStatusTextView.setTextColor(Theme.getColor("dialogTextBlack") & 0x42FFFFFF);
                view.addView((View)this.fingerprintStatusTextView);
                final RelativeLayout$LayoutParams relative2 = LayoutHelper.createRelative(-2, -2);
                relative2.setMarginStart(AndroidUtilities.dp(16.0f));
                relative2.addRule(8, 1001);
                relative2.addRule(6, 1001);
                relative2.addRule(17, 1001);
                this.fingerprintStatusTextView.setLayoutParams((ViewGroup$LayoutParams)relative2);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setView((View)view);
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                builder.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$PasscodeView$TCcTv4oyYUzBTnrh4HFlzxWR_gM(this));
                if (this.fingerprintDialog != null) {
                    try {
                        if (this.fingerprintDialog.isShowing()) {
                            this.fingerprintDialog.dismiss();
                        }
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
                this.fingerprintDialog = builder.show();
                this.cancellationSignal = new CancellationSignal();
                this.selfCancelled = false;
                from.authenticate(null, 0, this.cancellationSignal, (FingerprintManagerCompat.AuthenticationCallback)new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(final int n, final CharSequence charSequence) {
                        if (!PasscodeView.this.selfCancelled && n != 5) {
                            PasscodeView.this.showFingerprintError(charSequence);
                        }
                    }
                    
                    @Override
                    public void onAuthenticationFailed() {
                        PasscodeView.this.showFingerprintError(LocaleController.getString("FingerprintNotRecognized", 2131559493));
                    }
                    
                    @Override
                    public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
                        PasscodeView.this.showFingerprintError(charSequence);
                    }
                    
                    @Override
                    public void onAuthenticationSucceeded(final AuthenticationResult authenticationResult) {
                        try {
                            if (PasscodeView.this.fingerprintDialog.isShowing()) {
                                PasscodeView.this.fingerprintDialog.dismiss();
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        PasscodeView.this.fingerprintDialog = null;
                        PasscodeView.this.processDone(true);
                    }
                }, null);
            }
        }
        catch (Throwable t) {}
    }
    
    private void checkRetryTextView() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime > SharedConfig.lastUptimeMillis) {
            SharedConfig.passcodeRetryInMs -= elapsedRealtime - SharedConfig.lastUptimeMillis;
            if (SharedConfig.passcodeRetryInMs < 0L) {
                SharedConfig.passcodeRetryInMs = 0L;
            }
        }
        SharedConfig.lastUptimeMillis = elapsedRealtime;
        SharedConfig.saveConfig();
        final long passcodeRetryInMs = SharedConfig.passcodeRetryInMs;
        if (passcodeRetryInMs > 0L) {
            final double v = (double)passcodeRetryInMs;
            Double.isNaN(v);
            final int max = Math.max(1, (int)Math.ceil(v / 1000.0));
            if (max != this.lastValue) {
                this.retryTextView.setText((CharSequence)LocaleController.formatString("TooManyTries", 2131560909, LocaleController.formatPluralString("Seconds", max)));
                this.lastValue = max;
            }
            if (this.retryTextView.getVisibility() != 0) {
                this.retryTextView.setVisibility(0);
                this.passwordFrameLayout.setVisibility(4);
                if (this.numbersFrameLayout.getVisibility() == 0) {
                    this.numbersFrameLayout.setVisibility(4);
                }
                AndroidUtilities.hideKeyboard((View)this.passwordEditText);
                AndroidUtilities.cancelRunOnUIThread(this.checkRunnable);
                AndroidUtilities.runOnUIThread(this.checkRunnable, 100L);
            }
        }
        else {
            AndroidUtilities.cancelRunOnUIThread(this.checkRunnable);
            if (this.passwordFrameLayout.getVisibility() != 0) {
                this.retryTextView.setVisibility(4);
                this.passwordFrameLayout.setVisibility(0);
                final int passcodeType = SharedConfig.passcodeType;
                if (passcodeType == 0) {
                    this.numbersFrameLayout.setVisibility(0);
                }
                else if (passcodeType == 1) {
                    AndroidUtilities.showKeyboard((View)this.passwordEditText);
                }
            }
        }
    }
    
    private void onPasscodeError() {
        final Vibrator vibrator = (Vibrator)this.getContext().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        this.shakeTextView(2.0f, 0);
    }
    
    private void processDone(final boolean b) {
        if (!b) {
            if (SharedConfig.passcodeRetryInMs > 0L) {
                return;
            }
            final int passcodeType = SharedConfig.passcodeType;
            String s;
            if (passcodeType == 0) {
                s = this.passwordEditText2.getString();
            }
            else if (passcodeType == 1) {
                s = this.passwordEditText.getText().toString();
            }
            else {
                s = "";
            }
            if (s.length() == 0) {
                this.onPasscodeError();
                return;
            }
            if (!SharedConfig.checkPasscode(s)) {
                SharedConfig.increaseBadPasscodeTries();
                if (SharedConfig.passcodeRetryInMs > 0L) {
                    this.checkRetryTextView();
                }
                this.passwordEditText.setText((CharSequence)"");
                this.passwordEditText2.eraseAllCharacters(true);
                this.onPasscodeError();
                return;
            }
        }
        SharedConfig.badPasscodeTries = 0;
        this.passwordEditText.clearFocus();
        AndroidUtilities.hideKeyboard((View)this.passwordEditText);
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(200L);
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "translationY", new float[] { (float)AndroidUtilities.dp(20.0f) }), (Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { (float)AndroidUtilities.dp(0.0f) }) });
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                PasscodeView.this.setVisibility(8);
            }
        });
        set.start();
        SharedConfig.appLocked = false;
        SharedConfig.saveConfig();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
        this.setOnTouchListener((View$OnTouchListener)null);
        final PasscodeViewDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.didAcceptedPassword();
        }
    }
    
    private void setNextFocus(final View view, final int n) {
        view.setNextFocusForwardId(n);
        if (Build$VERSION.SDK_INT >= 22) {
            view.setAccessibilityTraversalBefore(n);
        }
    }
    
    private void shakeTextView(final float n, final int n2) {
        if (n2 == 6) {
            return;
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.passcodeTextView, "translationX", new float[] { (float)AndroidUtilities.dp(n) }) });
        set.setDuration(50L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                final PasscodeView this$0 = PasscodeView.this;
                float n;
                if (n2 == 5) {
                    n = 0.0f;
                }
                else {
                    n = -n;
                }
                this$0.shakeTextView(n, n2 + 1);
            }
        });
        set.start();
    }
    
    private void showFingerprintError(final CharSequence text) {
        this.fingerprintImageView.setImageResource(2131165441);
        this.fingerprintStatusTextView.setText(text);
        this.fingerprintStatusTextView.setTextColor(-765666);
        final Vibrator vibrator = (Vibrator)this.getContext().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView((View)this.fingerprintStatusTextView, 2.0f, 0);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        final Drawable backgroundDrawable = this.backgroundDrawable;
        if (backgroundDrawable != null) {
            if (backgroundDrawable instanceof ColorDrawable) {
                backgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
            }
            else {
                final float n = this.getMeasuredWidth() / (float)this.backgroundDrawable.getIntrinsicWidth();
                final float n2 = (this.getMeasuredHeight() + this.keyboardHeight) / (float)this.backgroundDrawable.getIntrinsicHeight();
                float n3 = n;
                if (n < n2) {
                    n3 = n2;
                }
                final int n4 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicWidth() * n3);
                final int n5 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicHeight() * n3);
                final int n6 = (this.getMeasuredWidth() - n4) / 2;
                final int n7 = (this.getMeasuredHeight() - n5 + this.keyboardHeight) / 2;
                this.backgroundDrawable.setBounds(n6, n7, n4 + n6, n5 + n7);
                this.backgroundDrawable.draw(canvas);
            }
        }
        else {
            super.onDraw(canvas);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        final View rootView = this.getRootView();
        final int height = rootView.getHeight();
        final int statusBarHeight = AndroidUtilities.statusBarHeight;
        final int viewInset = AndroidUtilities.getViewInset(rootView);
        this.getWindowVisibleDisplayFrame(this.rect);
        final Rect rect = this.rect;
        this.keyboardHeight = height - statusBarHeight - viewInset - (rect.bottom - rect.top);
        if (SharedConfig.passcodeType == 1 && (AndroidUtilities.isTablet() || this.getContext().getResources().getConfiguration().orientation != 2)) {
            final Object tag = this.passwordFrameLayout.getTag();
            int statusBarHeight2 = 0;
            int intValue;
            if (tag != null) {
                intValue = (int)this.passwordFrameLayout.getTag();
            }
            else {
                intValue = 0;
            }
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.passwordFrameLayout.getLayoutParams();
            final int height2 = layoutParams.height;
            final int n5 = this.keyboardHeight / 2;
            if (Build$VERSION.SDK_INT >= 21) {
                statusBarHeight2 = AndroidUtilities.statusBarHeight;
            }
            layoutParams.topMargin = intValue + height2 - n5 - statusBarHeight2;
            this.passwordFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        super.onLayout(b, n, n2, n3, n4);
    }
    
    protected void onMeasure(final int n, final int n2) {
        int n3 = View$MeasureSpec.getSize(n);
        final int y = AndroidUtilities.displaySize.y;
        final int sdk_INT = Build$VERSION.SDK_INT;
        final int n4 = 0;
        int statusBarHeight;
        if (sdk_INT >= 21) {
            statusBarHeight = 0;
        }
        else {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        int dp = y - statusBarHeight;
        FrameLayout$LayoutParams frameLayout$LayoutParams;
        if (!AndroidUtilities.isTablet() && this.getContext().getResources().getConfiguration().orientation == 2) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.passwordFrameLayout.getLayoutParams();
            int width;
            if (SharedConfig.passcodeType == 0) {
                width = n3 / 2;
            }
            else {
                width = n3;
            }
            layoutParams.width = width;
            layoutParams.height = AndroidUtilities.dp(140.0f);
            layoutParams.topMargin = (dp - AndroidUtilities.dp(140.0f)) / 2;
            this.passwordFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.numbersFrameLayout.getLayoutParams();
            frameLayout$LayoutParams.height = dp;
            final int n5 = n3 / 2;
            frameLayout$LayoutParams.leftMargin = n5;
            frameLayout$LayoutParams.topMargin = dp - frameLayout$LayoutParams.height;
            frameLayout$LayoutParams.width = n5;
            this.numbersFrameLayout.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams);
        }
        else {
            int n9;
            int n10;
            if (AndroidUtilities.isTablet()) {
                int n6;
                if (n3 > AndroidUtilities.dp(498.0f)) {
                    n6 = (n3 - AndroidUtilities.dp(498.0f)) / 2;
                    n3 = AndroidUtilities.dp(498.0f);
                }
                else {
                    n6 = 0;
                }
                if (dp > AndroidUtilities.dp(528.0f)) {
                    final int n7 = (dp - AndroidUtilities.dp(528.0f)) / 2;
                    dp = AndroidUtilities.dp(528.0f);
                    final int n8 = n6;
                    n9 = n7;
                    n10 = n8;
                }
                else {
                    n10 = n6;
                    n9 = 0;
                }
            }
            else {
                n9 = 0;
                n10 = 0;
            }
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.passwordFrameLayout.getLayoutParams();
            final int height = dp / 3;
            layoutParams2.height = height;
            layoutParams2.width = n3;
            layoutParams2.topMargin = n9;
            layoutParams2.leftMargin = n10;
            this.passwordFrameLayout.setTag((Object)n9);
            this.passwordFrameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.numbersFrameLayout.getLayoutParams();
            frameLayout$LayoutParams.height = height * 2;
            frameLayout$LayoutParams.leftMargin = n10;
            frameLayout$LayoutParams.topMargin = dp - frameLayout$LayoutParams.height + n9;
            frameLayout$LayoutParams.width = n3;
            this.numbersFrameLayout.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams);
        }
        final int n11 = (frameLayout$LayoutParams.width - AndroidUtilities.dp(50.0f) * 3) / 4;
        final int n12 = (frameLayout$LayoutParams.height - AndroidUtilities.dp(50.0f) * 4) / 5;
        int index = n4;
        while (true) {
            int n13 = 11;
            if (index >= 11) {
                break;
            }
            if (index == 0) {
                n13 = 10;
            }
            else if (index != 10) {
                n13 = index - 1;
            }
            final int n14 = n13 / 3;
            final int n15 = n13 % 3;
            FrameLayout$LayoutParams frameLayout$LayoutParams2;
            int n16;
            if (index < 10) {
                final TextView textView = this.numberTextViews.get(index);
                final TextView textView2 = this.lettersTextViews.get(index);
                frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)textView.getLayoutParams();
                final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)textView2.getLayoutParams();
                n16 = (AndroidUtilities.dp(50.0f) + n12) * n14 + n12;
                frameLayout$LayoutParams2.topMargin = n16;
                layoutParams3.topMargin = n16;
                final int n17 = (AndroidUtilities.dp(50.0f) + n11) * n15 + n11;
                frameLayout$LayoutParams2.leftMargin = n17;
                layoutParams3.leftMargin = n17;
                layoutParams3.topMargin += AndroidUtilities.dp(40.0f);
                textView.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams2);
                textView2.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
            }
            else {
                frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.eraseView.getLayoutParams();
                final int topMargin = (AndroidUtilities.dp(50.0f) + n12) * n14 + n12 + AndroidUtilities.dp(8.0f);
                frameLayout$LayoutParams2.topMargin = topMargin;
                frameLayout$LayoutParams2.leftMargin = (AndroidUtilities.dp(50.0f) + n11) * n15 + n11;
                n16 = topMargin - AndroidUtilities.dp(8.0f);
                this.eraseView.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams2);
            }
            final FrameLayout frameLayout = this.numberFrameLayouts.get(index);
            final FrameLayout$LayoutParams layoutParams4 = (FrameLayout$LayoutParams)frameLayout.getLayoutParams();
            layoutParams4.topMargin = n16 - AndroidUtilities.dp(17.0f);
            layoutParams4.leftMargin = frameLayout$LayoutParams2.leftMargin - AndroidUtilities.dp(25.0f);
            frameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams4);
            ++index;
        }
        super.onMeasure(n, n2);
    }
    
    public void onPause() {
        AndroidUtilities.cancelRunOnUIThread(this.checkRunnable);
        final AlertDialog fingerprintDialog = this.fingerprintDialog;
        if (fingerprintDialog != null) {
            try {
                if (fingerprintDialog.isShowing()) {
                    this.fingerprintDialog.dismiss();
                }
                this.fingerprintDialog = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        try {
            if (Build$VERSION.SDK_INT >= 23 && this.cancellationSignal != null) {
                this.cancellationSignal.cancel();
                this.cancellationSignal = null;
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    public void onResume() {
        this.checkRetryTextView();
        if (this.retryTextView.getVisibility() != 0) {
            if (SharedConfig.passcodeType == 1) {
                final EditTextBoldCursor passwordEditText = this.passwordEditText;
                if (passwordEditText != null) {
                    passwordEditText.requestFocus();
                    AndroidUtilities.showKeyboard((View)this.passwordEditText);
                }
                AndroidUtilities.runOnUIThread(new _$$Lambda$PasscodeView$NTxy4jfUAbP6Dz3o64FD_qu_vbw(this), 200L);
            }
            this.checkFingerprint();
        }
    }
    
    public void onShow() {
        this.checkRetryTextView();
        final Activity activity = (Activity)this.getContext();
        if (SharedConfig.passcodeType == 1) {
            if (this.retryTextView.getVisibility() != 0) {
                final EditTextBoldCursor passwordEditText = this.passwordEditText;
                if (passwordEditText != null) {
                    passwordEditText.requestFocus();
                    AndroidUtilities.showKeyboard((View)this.passwordEditText);
                }
            }
        }
        else if (activity != null) {
            final View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
                AndroidUtilities.hideKeyboard(((Activity)this.getContext()).getCurrentFocus());
            }
        }
        if (this.retryTextView.getVisibility() != 0) {
            this.checkFingerprint();
        }
        if (this.getVisibility() == 0) {
            return;
        }
        this.setAlpha(1.0f);
        this.setTranslationY(0.0f);
        if (Theme.isCustomTheme()) {
            this.backgroundDrawable = Theme.getCachedWallpaper();
            this.backgroundFrameLayout.setBackgroundColor(-1090519040);
        }
        else if (Theme.getSelectedBackgroundId() == 1000001L) {
            this.backgroundFrameLayout.setBackgroundColor(-11436898);
        }
        else {
            this.backgroundDrawable = Theme.getCachedWallpaper();
            if (this.backgroundDrawable != null) {
                this.backgroundFrameLayout.setBackgroundColor(-1090519040);
            }
            else {
                this.backgroundFrameLayout.setBackgroundColor(-11436898);
            }
        }
        this.passcodeTextView.setText((CharSequence)LocaleController.getString("EnterYourPasscode", 2131559374));
        final int passcodeType = SharedConfig.passcodeType;
        if (passcodeType == 0) {
            if (this.retryTextView.getVisibility() != 0) {
                this.numbersFrameLayout.setVisibility(0);
            }
            this.passwordEditText.setVisibility(8);
            this.passwordEditText2.setVisibility(0);
            this.checkImage.setVisibility(8);
        }
        else if (passcodeType == 1) {
            this.passwordEditText.setFilters(new InputFilter[0]);
            this.passwordEditText.setInputType(129);
            this.numbersFrameLayout.setVisibility(8);
            this.passwordEditText.setFocusable(true);
            this.passwordEditText.setFocusableInTouchMode(true);
            this.passwordEditText.setVisibility(0);
            this.passwordEditText2.setVisibility(8);
            this.checkImage.setVisibility(0);
        }
        this.setVisibility(0);
        this.passwordEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
        this.passwordEditText.setText((CharSequence)"");
        this.passwordEditText2.eraseAllCharacters(false);
        this.setOnTouchListener((View$OnTouchListener)_$$Lambda$PasscodeView$KE0jvuBZJ1oZX5aNavDxSwZtWh0.INSTANCE);
    }
    
    public void setDelegate(final PasscodeViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class AnimatingTextView extends FrameLayout
    {
        private String DOT;
        private ArrayList<TextView> characterTextViews;
        private AnimatorSet currentAnimation;
        private Runnable dotRunnable;
        private ArrayList<TextView> dotTextViews;
        private StringBuilder stringBuilder;
        
        public AnimatingTextView(final Context context) {
            super(context);
            this.DOT = "\u2022";
            this.characterTextViews = new ArrayList<TextView>(4);
            this.dotTextViews = new ArrayList<TextView>(4);
            this.stringBuilder = new StringBuilder(4);
            for (int i = 0; i < 4; ++i) {
                final TextView e = new TextView(context);
                e.setTextColor(-1);
                e.setTextSize(1, 36.0f);
                e.setGravity(17);
                e.setAlpha(0.0f);
                e.setPivotX((float)AndroidUtilities.dp(25.0f));
                e.setPivotY((float)AndroidUtilities.dp(25.0f));
                this.addView((View)e);
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)e.getLayoutParams();
                layoutParams.width = AndroidUtilities.dp(50.0f);
                layoutParams.height = AndroidUtilities.dp(50.0f);
                layoutParams.gravity = 51;
                e.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                this.characterTextViews.add(e);
                final TextView e2 = new TextView(context);
                e2.setTextColor(-1);
                e2.setTextSize(1, 36.0f);
                e2.setGravity(17);
                e2.setAlpha(0.0f);
                e2.setText((CharSequence)this.DOT);
                e2.setPivotX((float)AndroidUtilities.dp(25.0f));
                e2.setPivotY((float)AndroidUtilities.dp(25.0f));
                this.addView((View)e2);
                final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)e2.getLayoutParams();
                layoutParams2.width = AndroidUtilities.dp(50.0f);
                layoutParams2.height = AndroidUtilities.dp(50.0f);
                layoutParams2.gravity = 51;
                e2.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                this.dotTextViews.add(e2);
            }
        }
        
        private void eraseAllCharacters(final boolean b) {
            if (this.stringBuilder.length() == 0) {
                return;
            }
            final Runnable dotRunnable = this.dotRunnable;
            if (dotRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(dotRunnable);
                this.dotRunnable = null;
            }
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
                this.currentAnimation = null;
            }
            final StringBuilder stringBuilder = this.stringBuilder;
            final int length = stringBuilder.length();
            int i = 0;
            stringBuilder.delete(0, length);
            if (b) {
                final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                for (int j = 0; j < 4; ++j) {
                    final TextView textView = this.characterTextViews.get(j);
                    if (textView.getAlpha() != 0.0f) {
                        list.add(ObjectAnimator.ofFloat((Object)textView, "scaleX", new float[] { 0.0f }));
                        list.add(ObjectAnimator.ofFloat((Object)textView, "scaleY", new float[] { 0.0f }));
                        list.add(ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { 0.0f }));
                    }
                    final TextView textView2 = this.dotTextViews.get(j);
                    if (textView2.getAlpha() != 0.0f) {
                        list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleX", new float[] { 0.0f }));
                        list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleY", new float[] { 0.0f }));
                        list.add(ObjectAnimator.ofFloat((Object)textView2, "alpha", new float[] { 0.0f }));
                    }
                }
                (this.currentAnimation = new AnimatorSet()).setDuration(150L);
                this.currentAnimation.playTogether((Collection)list);
                this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(obj)) {
                            AnimatingTextView.this.currentAnimation = null;
                        }
                    }
                });
                this.currentAnimation.start();
            }
            else {
                while (i < 4) {
                    this.characterTextViews.get(i).setAlpha(0.0f);
                    this.dotTextViews.get(i).setAlpha(0.0f);
                    ++i;
                }
            }
        }
        
        private int getXForTextView(final int n) {
            return (this.getMeasuredWidth() - this.stringBuilder.length() * AndroidUtilities.dp(30.0f)) / 2 + n * AndroidUtilities.dp(30.0f) - AndroidUtilities.dp(10.0f);
        }
        
        public void appendCharacter(final String s) {
            if (this.stringBuilder.length() == 4) {
                return;
            }
            try {
                this.performHapticFeedback(3);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final int length = this.stringBuilder.length();
            this.stringBuilder.append(s);
            final TextView textView = this.characterTextViews.get(length);
            textView.setText((CharSequence)s);
            textView.setTranslationX((float)this.getXForTextView(length));
            list.add(ObjectAnimator.ofFloat((Object)textView, "scaleX", new float[] { 0.0f, 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)textView, "scaleY", new float[] { 0.0f, 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { 0.0f, 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)textView, "translationY", new float[] { (float)AndroidUtilities.dp(20.0f), 0.0f }));
            final TextView textView2 = this.dotTextViews.get(length);
            textView2.setTranslationX((float)this.getXForTextView(length));
            textView2.setAlpha(0.0f);
            list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleX", new float[] { 0.0f, 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleY", new float[] { 0.0f, 1.0f }));
            list.add(ObjectAnimator.ofFloat((Object)textView2, "translationY", new float[] { (float)AndroidUtilities.dp(20.0f), 0.0f }));
            for (int i = length + 1; i < 4; ++i) {
                final TextView textView3 = this.characterTextViews.get(i);
                if (textView3.getAlpha() != 0.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)textView3, "scaleX", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView3, "scaleY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView3, "alpha", new float[] { 0.0f }));
                }
                final TextView textView4 = this.dotTextViews.get(i);
                if (textView4.getAlpha() != 0.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)textView4, "scaleX", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView4, "scaleY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView4, "alpha", new float[] { 0.0f }));
                }
            }
            final Runnable dotRunnable = this.dotRunnable;
            if (dotRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(dotRunnable);
            }
            AndroidUtilities.runOnUIThread(this.dotRunnable = new Runnable() {
                @Override
                public void run() {
                    if (AnimatingTextView.this.dotRunnable != this) {
                        return;
                    }
                    final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                    final TextView textView = AnimatingTextView.this.characterTextViews.get(length);
                    list.add(ObjectAnimator.ofFloat((Object)textView, "scaleX", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "scaleY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { 0.0f }));
                    final TextView textView2 = AnimatingTextView.this.dotTextViews.get(length);
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleX", new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleY", new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "alpha", new float[] { 1.0f }));
                    AnimatingTextView.this.currentAnimation = new AnimatorSet();
                    AnimatingTextView.this.currentAnimation.setDuration(150L);
                    AnimatingTextView.this.currentAnimation.playTogether((Collection)list);
                    AnimatingTextView.this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(obj)) {
                                AnimatingTextView.this.currentAnimation = null;
                            }
                        }
                    });
                    AnimatingTextView.this.currentAnimation.start();
                }
            }, 1500L);
            for (int j = 0; j < length; ++j) {
                final TextView textView5 = this.characterTextViews.get(j);
                list.add(ObjectAnimator.ofFloat((Object)textView5, "translationX", new float[] { (float)this.getXForTextView(j) }));
                list.add(ObjectAnimator.ofFloat((Object)textView5, "scaleX", new float[] { 0.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView5, "scaleY", new float[] { 0.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView5, "alpha", new float[] { 0.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView5, "translationY", new float[] { 0.0f }));
                final TextView textView6 = this.dotTextViews.get(j);
                list.add(ObjectAnimator.ofFloat((Object)textView6, "translationX", new float[] { (float)this.getXForTextView(j) }));
                list.add(ObjectAnimator.ofFloat((Object)textView6, "scaleX", new float[] { 1.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView6, "scaleY", new float[] { 1.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView6, "alpha", new float[] { 1.0f }));
                list.add(ObjectAnimator.ofFloat((Object)textView6, "translationY", new float[] { 0.0f }));
            }
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
            }
            (this.currentAnimation = new AnimatorSet()).setDuration(150L);
            this.currentAnimation.playTogether((Collection)list);
            this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator obj) {
                    if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(obj)) {
                        AnimatingTextView.this.currentAnimation = null;
                    }
                }
            });
            this.currentAnimation.start();
        }
        
        public void eraseLastCharacter() {
            if (this.stringBuilder.length() == 0) {
                return;
            }
            try {
                this.performHapticFeedback(3);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final int n = this.stringBuilder.length() - 1;
            if (n != 0) {
                this.stringBuilder.deleteCharAt(n);
            }
            for (int i = n; i < 4; ++i) {
                final TextView textView = this.characterTextViews.get(i);
                if (textView.getAlpha() != 0.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)textView, "scaleX", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "scaleY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "translationY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView, "translationX", new float[] { (float)this.getXForTextView(i) }));
                }
                final TextView textView2 = this.dotTextViews.get(i);
                if (textView2.getAlpha() != 0.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleX", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "scaleY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "alpha", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "translationY", new float[] { 0.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)textView2, "translationX", new float[] { (float)this.getXForTextView(i) }));
                }
            }
            if (n == 0) {
                this.stringBuilder.deleteCharAt(n);
            }
            for (int j = 0; j < n; ++j) {
                list.add(ObjectAnimator.ofFloat((Object)this.characterTextViews.get(j), "translationX", new float[] { (float)this.getXForTextView(j) }));
                list.add(ObjectAnimator.ofFloat((Object)this.dotTextViews.get(j), "translationX", new float[] { (float)this.getXForTextView(j) }));
            }
            final Runnable dotRunnable = this.dotRunnable;
            if (dotRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(dotRunnable);
                this.dotRunnable = null;
            }
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
            }
            (this.currentAnimation = new AnimatorSet()).setDuration(150L);
            this.currentAnimation.playTogether((Collection)list);
            this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator obj) {
                    if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(obj)) {
                        AnimatingTextView.this.currentAnimation = null;
                    }
                }
            });
            this.currentAnimation.start();
        }
        
        public String getString() {
            return this.stringBuilder.toString();
        }
        
        public int length() {
            return this.stringBuilder.length();
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            final Runnable dotRunnable = this.dotRunnable;
            if (dotRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(dotRunnable);
                this.dotRunnable = null;
            }
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
                this.currentAnimation = null;
            }
            for (int i = 0; i < 4; ++i) {
                if (i < this.stringBuilder.length()) {
                    final TextView textView = this.characterTextViews.get(i);
                    textView.setAlpha(0.0f);
                    textView.setScaleX(1.0f);
                    textView.setScaleY(1.0f);
                    textView.setTranslationY(0.0f);
                    textView.setTranslationX((float)this.getXForTextView(i));
                    final TextView textView2 = this.dotTextViews.get(i);
                    textView2.setAlpha(1.0f);
                    textView2.setScaleX(1.0f);
                    textView2.setScaleY(1.0f);
                    textView2.setTranslationY(0.0f);
                    textView2.setTranslationX((float)this.getXForTextView(i));
                }
                else {
                    this.characterTextViews.get(i).setAlpha(0.0f);
                    this.dotTextViews.get(i).setAlpha(0.0f);
                }
            }
            super.onLayout(b, n, n2, n3, n4);
        }
    }
    
    public interface PasscodeViewDelegate
    {
        void didAcceptedPassword();
    }
}
