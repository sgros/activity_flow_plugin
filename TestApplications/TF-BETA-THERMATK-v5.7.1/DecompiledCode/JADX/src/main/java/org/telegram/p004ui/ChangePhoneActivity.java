package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.C0278PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SmsReceiver;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.HintEditText;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.SlideView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_changePhone;
import org.telegram.tgnet.TLRPC.TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC.TL_auth_cancelCode;
import org.telegram.tgnet.TLRPC.TL_auth_codeTypeCall;
import org.telegram.tgnet.TLRPC.TL_auth_codeTypeFlashCall;
import org.telegram.tgnet.TLRPC.TL_auth_codeTypeSms;
import org.telegram.tgnet.TLRPC.TL_auth_resendCode;
import org.telegram.tgnet.TLRPC.TL_auth_sentCode;
import org.telegram.tgnet.TLRPC.TL_auth_sentCodeTypeApp;
import org.telegram.tgnet.TLRPC.TL_auth_sentCodeTypeCall;
import org.telegram.tgnet.TLRPC.TL_auth_sentCodeTypeFlashCall;
import org.telegram.tgnet.TLRPC.TL_auth_sentCodeTypeSms;
import org.telegram.tgnet.TLRPC.TL_codeSettings;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.auth_CodeType;
import org.telegram.tgnet.TLRPC.auth_SentCodeType;

/* renamed from: org.telegram.ui.ChangePhoneActivity */
public class ChangePhoneActivity extends BaseFragment {
    private static final int done_button = 1;
    private boolean checkPermissions = true;
    private int currentViewNum = 0;
    private View doneButton;
    private Dialog permissionsDialog;
    private ArrayList<String> permissionsItems = new ArrayList();
    private AlertDialog progressDialog;
    private int scrollHeight;
    private SlideView[] views = new SlideView[5];

    /* renamed from: org.telegram.ui.ChangePhoneActivity$ProgressView */
    private class ProgressView extends View {
        private Paint paint = new Paint();
        private Paint paint2 = new Paint();
        private float progress;

        public ProgressView(Context context) {
            super(context);
            this.paint.setColor(Theme.getColor(Theme.key_login_progressInner));
            this.paint2.setColor(Theme.getColor(Theme.key_login_progressOuter));
        }

        public void setProgress(float f) {
            this.progress = f;
            invalidate();
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            float measuredWidth = (float) ((int) (((float) getMeasuredWidth()) * this.progress));
            canvas.drawRect(0.0f, 0.0f, measuredWidth, (float) getMeasuredHeight(), this.paint2);
            canvas.drawRect(measuredWidth, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), this.paint);
        }
    }

    /* renamed from: org.telegram.ui.ChangePhoneActivity$1 */
    class C39451 extends ActionBarMenuOnItemClick {
        C39451() {
        }

        public void onItemClick(int i) {
            if (i == 1) {
                ChangePhoneActivity.this.views[ChangePhoneActivity.this.currentViewNum].onNextPressed();
            } else if (i == -1) {
                ChangePhoneActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.ChangePhoneActivity$LoginActivitySmsView */
    public class LoginActivitySmsView extends SlideView implements NotificationCenterDelegate {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private int codeTime = 15000;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int currentType;
        private String emailPhone;
        private boolean ignoreOnTextChange;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError = "";
        private int length;
        private boolean nextPressed;
        private int nextType;
        private int openTime;
        private String pattern = "*";
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private String requestPhone;
        final /* synthetic */ ChangePhoneActivity this$0;
        private int time = 60000;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private final Object timerSync = new Object();
        private TextView titleTextView;
        private boolean waitingForEvent;

        /* renamed from: org.telegram.ui.ChangePhoneActivity$LoginActivitySmsView$4 */
        class C23874 extends TimerTask {
            C23874() {
            }

            public void run() {
                AndroidUtilities.runOnUIThread(new C1238x8b7bed81(this));
            }

            public /* synthetic */ void lambda$run$0$ChangePhoneActivity$LoginActivitySmsView$4() {
                double currentTimeMillis = (double) System.currentTimeMillis();
                double access$2400 = LoginActivitySmsView.this.lastCodeTime;
                Double.isNaN(currentTimeMillis);
                access$2400 = currentTimeMillis - access$2400;
                LoginActivitySmsView.this.lastCodeTime = currentTimeMillis;
                LoginActivitySmsView loginActivitySmsView = LoginActivitySmsView.this;
                double access$2500 = (double) loginActivitySmsView.codeTime;
                Double.isNaN(access$2500);
                loginActivitySmsView.codeTime = (int) (access$2500 - access$2400);
                if (LoginActivitySmsView.this.codeTime <= 1000) {
                    LoginActivitySmsView.this.problemText.setVisibility(0);
                    LoginActivitySmsView.this.timeText.setVisibility(8);
                    LoginActivitySmsView.this.destroyCodeTimer();
                }
            }
        }

        /* renamed from: org.telegram.ui.ChangePhoneActivity$LoginActivitySmsView$5 */
        class C23895 extends TimerTask {

            /* renamed from: org.telegram.ui.ChangePhoneActivity$LoginActivitySmsView$5$1 */
            class C23881 implements Runnable {
                C23881() {
                }

                public void run() {
                    double currentTimeMillis = (double) System.currentTimeMillis();
                    double access$3000 = LoginActivitySmsView.this.lastCurrentTime;
                    Double.isNaN(currentTimeMillis);
                    access$3000 = currentTimeMillis - access$3000;
                    LoginActivitySmsView loginActivitySmsView = LoginActivitySmsView.this;
                    double access$3100 = (double) loginActivitySmsView.time;
                    Double.isNaN(access$3100);
                    loginActivitySmsView.time = (int) (access$3100 - access$3000);
                    LoginActivitySmsView.this.lastCurrentTime = currentTimeMillis;
                    if (LoginActivitySmsView.this.time >= 1000) {
                        int access$31002 = (LoginActivitySmsView.this.time / 1000) - (((LoginActivitySmsView.this.time / 1000) / 60) * 60);
                        if (LoginActivitySmsView.this.nextType == 4 || LoginActivitySmsView.this.nextType == 3) {
                            LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("CallText", C1067R.string.CallText, Integer.valueOf(r0), Integer.valueOf(access$31002)));
                        } else if (LoginActivitySmsView.this.nextType == 2) {
                            LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("SmsText", C1067R.string.SmsText, Integer.valueOf(r0), Integer.valueOf(access$31002)));
                        }
                        if (LoginActivitySmsView.this.progressView != null) {
                            LoginActivitySmsView.this.progressView.setProgress(1.0f - (((float) LoginActivitySmsView.this.time) / ((float) LoginActivitySmsView.this.timeout)));
                            return;
                        }
                        return;
                    }
                    if (LoginActivitySmsView.this.progressView != null) {
                        LoginActivitySmsView.this.progressView.setProgress(1.0f);
                    }
                    LoginActivitySmsView.this.destroyTimer();
                    if (LoginActivitySmsView.this.currentType == 3) {
                        AndroidUtilities.setWaitingForCall(false);
                        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                        LoginActivitySmsView.this.waitingForEvent = false;
                        LoginActivitySmsView.this.destroyCodeTimer();
                        LoginActivitySmsView.this.resendCode();
                    } else if (LoginActivitySmsView.this.currentType != 2 && LoginActivitySmsView.this.currentType != 4) {
                    } else {
                        if (LoginActivitySmsView.this.nextType == 4 || LoginActivitySmsView.this.nextType == 2) {
                            if (LoginActivitySmsView.this.nextType == 4) {
                                LoginActivitySmsView.this.timeText.setText(LocaleController.getString("Calling", C1067R.string.Calling));
                            } else {
                                LoginActivitySmsView.this.timeText.setText(LocaleController.getString("SendingSms", C1067R.string.SendingSms));
                            }
                            LoginActivitySmsView.this.createCodeTimer();
                            TL_auth_resendCode tL_auth_resendCode = new TL_auth_resendCode();
                            tL_auth_resendCode.phone_number = LoginActivitySmsView.this.requestPhone;
                            tL_auth_resendCode.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                            ConnectionsManager.getInstance(LoginActivitySmsView.this.this$0.currentAccount).sendRequest(tL_auth_resendCode, new C3598x79ce6ee9(this), 2);
                        } else if (LoginActivitySmsView.this.nextType == 3) {
                            AndroidUtilities.setWaitingForSms(false);
                            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                            LoginActivitySmsView.this.waitingForEvent = false;
                            LoginActivitySmsView.this.destroyCodeTimer();
                            LoginActivitySmsView.this.resendCode();
                        }
                    }
                }

                public /* synthetic */ void lambda$run$1$ChangePhoneActivity$LoginActivitySmsView$5$1(TLObject tLObject, TL_error tL_error) {
                    if (tL_error != null && tL_error.text != null) {
                        AndroidUtilities.runOnUIThread(new C1239x3bd3bd3f(this, tL_error));
                    }
                }

                public /* synthetic */ void lambda$null$0$ChangePhoneActivity$LoginActivitySmsView$5$1(TL_error tL_error) {
                    LoginActivitySmsView.this.lastError = tL_error.text;
                }
            }

            C23895() {
            }

            public void run() {
                if (LoginActivitySmsView.this.timeTimer != null) {
                    AndroidUtilities.runOnUIThread(new C23881());
                }
            }
        }

        static /* synthetic */ void lambda$onBackPressed$9(TLObject tLObject, TL_error tL_error) {
        }

        public boolean needBackButton() {
            return true;
        }

        public LoginActivitySmsView(ChangePhoneActivity changePhoneActivity, Context context, int i) {
            final ChangePhoneActivity changePhoneActivity2 = changePhoneActivity;
            Context context2 = context;
            this.this$0 = changePhoneActivity2;
            super(context2);
            this.currentType = i;
            setOrientation(1);
            this.confirmTextView = new TextView(context2);
            TextView textView = this.confirmTextView;
            String str = Theme.key_windowBackgroundWhiteGrayText6;
            textView.setTextColor(Theme.getColor(str));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
            this.titleTextView = new TextView(context2);
            textView = this.titleTextView;
            String str2 = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(str2));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            int i2 = 3;
            this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.titleTextView.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            FrameLayout frameLayout;
            if (this.currentType == 3) {
                this.confirmTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                frameLayout = new FrameLayout(context2);
                addView(frameLayout, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                ImageView imageView = new ImageView(context2);
                imageView.setImageResource(C1067R.C1065drawable.phone_activate);
                boolean z = LocaleController.isRTL;
                if (z) {
                    frameLayout.addView(imageView, LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    frameLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 82.0f, 0.0f, 0.0f, 0.0f));
                } else {
                    frameLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-1, -2.0f, z ? 5 : 3, 0.0f, 0.0f, 82.0f, 0.0f));
                    frameLayout.addView(imageView, LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f));
                }
            } else {
                this.confirmTextView.setGravity(49);
                frameLayout = new FrameLayout(context2);
                addView(frameLayout, LayoutHelper.createLinear(-2, -2, 49));
                int i3 = this.currentType;
                String str3 = Theme.key_chats_actionBackground;
                if (i3 == 1) {
                    this.blackImageView = new ImageView(context2);
                    this.blackImageView.setImageResource(C1067R.C1065drawable.sms_devices);
                    this.blackImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str2), Mode.MULTIPLY));
                    frameLayout.addView(this.blackImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.blueImageView = new ImageView(context2);
                    this.blueImageView.setImageResource(C1067R.C1065drawable.sms_bubble);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str3), Mode.MULTIPLY));
                    frameLayout.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText(LocaleController.getString("SentAppCodeTitle", C1067R.string.SentAppCodeTitle));
                } else {
                    this.blueImageView = new ImageView(context2);
                    this.blueImageView.setImageResource(C1067R.C1065drawable.sms_code);
                    this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str3), Mode.MULTIPLY));
                    frameLayout.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText(LocaleController.getString("SentSmsCodeTitle", C1067R.string.SentSmsCodeTitle));
                }
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            this.codeFieldContainer = new LinearLayout(context2);
            this.codeFieldContainer.setOrientation(0);
            addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 36, 1));
            if (this.currentType == 3) {
                this.codeFieldContainer.setVisibility(8);
            }
            this.timeText = new TextView(context2) {
                /* Access modifiers changed, original: protected */
                public void onMeasure(int i, int i2) {
                    super.onMeasure(i, MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(100.0f), Integer.MIN_VALUE));
                }
            };
            this.timeText.setTextColor(Theme.getColor(str));
            this.timeText.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
            if (this.currentType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                addView(this.timeText, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3));
                this.progressView = new ProgressView(context2);
                textView = this.timeText;
                if (LocaleController.isRTL) {
                    i2 = 5;
                }
                textView.setGravity(i2);
                addView(this.progressView, LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f));
            } else {
                this.timeText.setPadding(0, AndroidUtilities.m26dp(2.0f), 0, AndroidUtilities.m26dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                addView(this.timeText, LayoutHelper.createLinear(-2, -2, 49));
            }
            this.problemText = new TextView(context2) {
                /* Access modifiers changed, original: protected */
                public void onMeasure(int i, int i2) {
                    super.onMeasure(i, MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(100.0f), Integer.MIN_VALUE));
                }
            };
            this.problemText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
            this.problemText.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.m26dp(2.0f), 0, AndroidUtilities.m26dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.currentType == 1) {
                this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", C1067R.string.DidNotGetTheCodeSms));
            } else {
                this.problemText.setText(LocaleController.getString("DidNotGetTheCode", C1067R.string.DidNotGetTheCode));
            }
            addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener(new C1246xd205703d(this));
        }

        public /* synthetic */ void lambda$new$0$ChangePhoneActivity$LoginActivitySmsView(View view) {
            if (!this.nextPressed) {
                Object obj = ((this.nextType == 4 && this.currentType == 2) || this.nextType == 0) ? 1 : null;
                if (obj == null) {
                    resendCode();
                } else {
                    try {
                        PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                        String format = String.format(Locale.US, "%s (%d)", new Object[]{packageInfo.versionName, Integer.valueOf(packageInfo.versionCode)});
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("message/rfc822");
                        intent.putExtra("android.intent.extra.EMAIL", new String[]{"sms@stel.com"});
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Android registration/login issue ");
                        stringBuilder.append(format);
                        stringBuilder.append(" ");
                        stringBuilder.append(this.emailPhone);
                        intent.putExtra("android.intent.extra.SUBJECT", stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Phone: ");
                        stringBuilder.append(this.requestPhone);
                        stringBuilder.append("\nApp version: ");
                        stringBuilder.append(format);
                        stringBuilder.append("\nOS version: SDK ");
                        stringBuilder.append(VERSION.SDK_INT);
                        stringBuilder.append("\nDevice Name: ");
                        stringBuilder.append(Build.MANUFACTURER);
                        stringBuilder.append(Build.MODEL);
                        stringBuilder.append("\nLocale: ");
                        stringBuilder.append(Locale.getDefault());
                        stringBuilder.append("\nError: ");
                        stringBuilder.append(this.lastError);
                        intent.putExtra("android.intent.extra.TEXT", stringBuilder.toString());
                        getContext().startActivity(Intent.createChooser(intent, "Send email..."));
                    } catch (Exception unused) {
                        AlertsCreator.showSimpleAlert(this.this$0, LocaleController.getString("NoMailInstalled", C1067R.string.NoMailInstalled));
                    }
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (this.currentType != 3) {
                ImageView imageView = this.blueImageView;
                if (imageView != null) {
                    i = ((imageView.getMeasuredHeight() + this.titleTextView.getMeasuredHeight()) + this.confirmTextView.getMeasuredHeight()) + AndroidUtilities.m26dp(35.0f);
                    i2 = AndroidUtilities.m26dp(80.0f);
                    int dp = AndroidUtilities.m26dp(291.0f);
                    if (this.this$0.scrollHeight - i < i2) {
                        setMeasuredDimension(getMeasuredWidth(), i + i2);
                    } else if (this.this$0.scrollHeight > dp) {
                        setMeasuredDimension(getMeasuredWidth(), dp);
                    } else {
                        setMeasuredDimension(getMeasuredWidth(), this.this$0.scrollHeight);
                    }
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (this.currentType != 3 && this.blueImageView != null) {
                int bottom = this.confirmTextView.getBottom();
                i = getMeasuredHeight() - bottom;
                TextView textView;
                if (this.problemText.getVisibility() == 0) {
                    i2 = this.problemText.getMeasuredHeight();
                    i = (i + bottom) - i2;
                    textView = this.problemText;
                    textView.layout(textView.getLeft(), i, this.problemText.getRight(), i2 + i);
                } else if (this.timeText.getVisibility() == 0) {
                    i2 = this.timeText.getMeasuredHeight();
                    i = (i + bottom) - i2;
                    textView = this.timeText;
                    textView.layout(textView.getLeft(), i, this.timeText.getRight(), i2 + i);
                } else {
                    i += bottom;
                }
                i -= bottom;
                i2 = this.codeFieldContainer.getMeasuredHeight();
                i = ((i - i2) / 2) + bottom;
                LinearLayout linearLayout = this.codeFieldContainer;
                linearLayout.layout(linearLayout.getLeft(), i, this.codeFieldContainer.getRight(), i2 + i);
            }
        }

        private void resendCode() {
            Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            this.nextPressed = true;
            this.this$0.needShowProgress();
            TL_auth_resendCode tL_auth_resendCode = new TL_auth_resendCode();
            tL_auth_resendCode.phone_number = this.requestPhone;
            tL_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(tL_auth_resendCode, new C3597x5a250e9d(this, bundle, tL_auth_resendCode), 2);
        }

        public /* synthetic */ void lambda$resendCode$3$ChangePhoneActivity$LoginActivitySmsView(Bundle bundle, TL_auth_resendCode tL_auth_resendCode, TLObject tLObject, TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new C1240x8176dea9(this, tL_error, bundle, tLObject, tL_auth_resendCode));
        }

        public /* synthetic */ void lambda$null$2$ChangePhoneActivity$LoginActivitySmsView(TL_error tL_error, Bundle bundle, TLObject tLObject, TL_auth_resendCode tL_auth_resendCode) {
            this.nextPressed = false;
            if (tL_error == null) {
                this.this$0.fillNextCodeParams(bundle, (TL_auth_sentCode) tLObject);
            } else {
                AlertDialog alertDialog = (AlertDialog) AlertsCreator.processError(this.this$0.currentAccount, tL_error, this.this$0, tL_auth_resendCode, new Object[0]);
                if (alertDialog != null && tL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    alertDialog.setPositiveButtonListener(new C1242xfeea6455(this));
                }
            }
            this.this$0.needHideProgress();
        }

        public /* synthetic */ void lambda$null$1$ChangePhoneActivity$LoginActivitySmsView(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            this.this$0.finishFragment();
        }

        public String getHeaderName() {
            if (this.currentType == 1) {
                return this.phone;
            }
            return LocaleController.getString("YourCode", C1067R.string.YourCode);
        }

        public void onCancelPressed() {
            this.nextPressed = false;
        }

        public void setParams(Bundle bundle, boolean z) {
            Bundle bundle2 = bundle;
            if (bundle2 != null) {
                int i;
                this.waitingForEvent = true;
                int i2 = this.currentType;
                if (i2 == 2) {
                    AndroidUtilities.setWaitingForSms(true);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i2 == 3) {
                    AndroidUtilities.setWaitingForCall(true);
                    NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
                }
                this.currentParams = bundle2;
                this.phone = bundle2.getString("phone");
                this.emailPhone = bundle2.getString("ephone");
                this.requestPhone = bundle2.getString("phoneFormated");
                this.phoneHash = bundle2.getString("phoneHash");
                i2 = bundle2.getInt("timeout");
                this.time = i2;
                this.timeout = i2;
                this.openTime = (int) (System.currentTimeMillis() / 1000);
                this.nextType = bundle2.getInt("nextType");
                this.pattern = bundle2.getString("pattern");
                this.length = bundle2.getInt("length");
                if (this.length == 0) {
                    this.length = 5;
                }
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                CharSequence charSequence = "";
                int i3 = 8;
                if (editTextBoldCursorArr != null && editTextBoldCursorArr.length == this.length) {
                    i = 0;
                    while (true) {
                        EditTextBoldCursor[] editTextBoldCursorArr2 = this.codeField;
                        if (i >= editTextBoldCursorArr2.length) {
                            break;
                        }
                        editTextBoldCursorArr2[i].setText(charSequence);
                        i++;
                    }
                } else {
                    this.codeField = new EditTextBoldCursor[this.length];
                    i = 0;
                    while (i < this.length) {
                        this.codeField[i] = new EditTextBoldCursor(getContext());
                        EditText editText = this.codeField[i];
                        String str = Theme.key_windowBackgroundWhiteBlackText;
                        editText.setTextColor(Theme.getColor(str));
                        this.codeField[i].setCursorColor(Theme.getColor(str));
                        this.codeField[i].setCursorSize(AndroidUtilities.m26dp(20.0f));
                        this.codeField[i].setCursorWidth(1.5f);
                        Drawable mutate = getResources().getDrawable(C1067R.C1065drawable.search_dark_activated).mutate();
                        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Mode.MULTIPLY));
                        this.codeField[i].setBackgroundDrawable(mutate);
                        this.codeField[i].setImeOptions(268435461);
                        this.codeField[i].setTextSize(1, 20.0f);
                        this.codeField[i].setMaxLines(1);
                        this.codeField[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        this.codeField[i].setPadding(0, 0, 0, 0);
                        this.codeField[i].setGravity(49);
                        if (this.currentType == 3) {
                            this.codeField[i].setEnabled(false);
                            this.codeField[i].setInputType(0);
                            this.codeField[i].setVisibility(8);
                        } else {
                            this.codeField[i].setInputType(3);
                        }
                        this.codeFieldContainer.addView(this.codeField[i], LayoutHelper.createLinear(34, 36, 1, 0, 0, i != this.length - 1 ? 7 : 0, 0));
                        this.codeField[i].addTextChangedListener(new TextWatcher() {
                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            }

                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            }

                            public void afterTextChanged(Editable editable) {
                                if (!LoginActivitySmsView.this.ignoreOnTextChange) {
                                    int length = editable.length();
                                    if (length >= 1) {
                                        if (length > 1) {
                                            String obj = editable.toString();
                                            LoginActivitySmsView.this.ignoreOnTextChange = true;
                                            for (int i = 0; i < Math.min(LoginActivitySmsView.this.length - i, length); i++) {
                                                if (i == 0) {
                                                    editable.replace(0, length, obj.substring(i, i + 1));
                                                } else {
                                                    LoginActivitySmsView.this.codeField[i + i].setText(obj.substring(i, i + 1));
                                                }
                                            }
                                            LoginActivitySmsView.this.ignoreOnTextChange = false;
                                        }
                                        if (i != LoginActivitySmsView.this.length - 1) {
                                            LoginActivitySmsView.this.codeField[i + 1].setSelection(LoginActivitySmsView.this.codeField[i + 1].length());
                                            LoginActivitySmsView.this.codeField[i + 1].requestFocus();
                                        }
                                        if ((i == LoginActivitySmsView.this.length - 1 || (i == LoginActivitySmsView.this.length - 2 && length >= 2)) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                            LoginActivitySmsView.this.onNextPressed();
                                        }
                                    }
                                }
                            }
                        });
                        this.codeField[i].setOnKeyListener(new C1243x3c71d5c9(this, i));
                        this.codeField[i].setOnEditorActionListener(new C1247xe61975aa(this));
                        i++;
                    }
                }
                ProgressView progressView = this.progressView;
                if (progressView != null) {
                    progressView.setVisibility(this.nextType != 0 ? 0 : 8);
                }
                if (this.phone != null) {
                    String format = C0278PhoneFormat.getInstance().format(this.phone);
                    int i4 = this.currentType;
                    if (i4 == 1) {
                        charSequence = AndroidUtilities.replaceTags(LocaleController.getString("SentAppCode", C1067R.string.SentAppCode));
                    } else if (i4 == 2) {
                        charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", C1067R.string.SentSmsCode, LocaleController.addNbsp(format)));
                    } else if (i4 == 3) {
                        charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", C1067R.string.SentCallCode, LocaleController.addNbsp(format)));
                    } else if (i4 == 4) {
                        charSequence = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", C1067R.string.SentCallOnly, LocaleController.addNbsp(format)));
                    }
                    this.confirmTextView.setText(charSequence);
                    if (this.currentType != 3) {
                        AndroidUtilities.showKeyboard(this.codeField[0]);
                        this.codeField[0].requestFocus();
                    } else {
                        AndroidUtilities.hideKeyboard(this.codeField[0]);
                    }
                    destroyTimer();
                    destroyCodeTimer();
                    this.lastCurrentTime = (double) System.currentTimeMillis();
                    i = this.currentType;
                    if (i == 1) {
                        this.problemText.setVisibility(0);
                        this.timeText.setVisibility(8);
                    } else {
                        TextView textView;
                        String str2 = "SmsText";
                        String str3 = "CallText";
                        if (i == 3) {
                            i = this.nextType;
                            if (i == 4 || i == 2) {
                                this.problemText.setVisibility(8);
                                this.timeText.setVisibility(0);
                                i = this.nextType;
                                if (i == 4) {
                                    this.timeText.setText(LocaleController.formatString(str3, C1067R.string.CallText, Integer.valueOf(1), Integer.valueOf(0)));
                                } else if (i == 2) {
                                    this.timeText.setText(LocaleController.formatString(str2, C1067R.string.SmsText, Integer.valueOf(1), Integer.valueOf(0)));
                                }
                                createTimer();
                            }
                        }
                        if (this.currentType == 2) {
                            i = this.nextType;
                            if (i == 4 || i == 3) {
                                this.timeText.setText(LocaleController.formatString(str3, C1067R.string.CallText, Integer.valueOf(2), Integer.valueOf(0)));
                                this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                                textView = this.timeText;
                                if (this.time >= 1000) {
                                    i3 = 0;
                                }
                                textView.setVisibility(i3);
                                createTimer();
                            }
                        }
                        if (this.currentType == 4 && this.nextType == 2) {
                            this.timeText.setText(LocaleController.formatString(str2, C1067R.string.SmsText, Integer.valueOf(2), Integer.valueOf(0)));
                            this.problemText.setVisibility(this.time < 1000 ? 0 : 8);
                            textView = this.timeText;
                            if (this.time >= 1000) {
                                i3 = 0;
                            }
                            textView.setVisibility(i3);
                            createTimer();
                        } else {
                            this.timeText.setVisibility(8);
                            this.problemText.setVisibility(8);
                            createCodeTimer();
                        }
                    }
                }
            }
        }

        public /* synthetic */ boolean lambda$setParams$4$ChangePhoneActivity$LoginActivitySmsView(int i, View view, int i2, KeyEvent keyEvent) {
            if (i2 != 67 || this.codeField[i].length() != 0 || i <= 0) {
                return false;
            }
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            i--;
            editTextBoldCursorArr[i].setSelection(editTextBoldCursorArr[i].length());
            this.codeField[i].requestFocus();
            this.codeField[i].dispatchKeyEvent(keyEvent);
            return true;
        }

        public /* synthetic */ boolean lambda$setParams$5$ChangePhoneActivity$LoginActivitySmsView(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            onNextPressed();
            return true;
        }

        private void createCodeTimer() {
            if (this.codeTimer == null) {
                this.codeTime = 15000;
                this.codeTimer = new Timer();
                this.lastCodeTime = (double) System.currentTimeMillis();
                this.codeTimer.schedule(new C23874(), 0, 1000);
            }
        }

        private void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.codeTimer != null) {
                        this.codeTimer.cancel();
                        this.codeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }

        private void createTimer() {
            if (this.timeTimer == null) {
                this.timeTimer = new Timer();
                this.timeTimer.schedule(new C23895(), 0, 1000);
            }
        }

        private void destroyTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.timeTimer != null) {
                        this.timeTimer.cancel();
                        this.timeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }

        private String getCode() {
            if (this.codeField == null) {
                return "";
            }
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i >= editTextBoldCursorArr.length) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(C0278PhoneFormat.stripExceptNumbers(editTextBoldCursorArr[i].getText().toString()));
                i++;
            }
        }

        public void onNextPressed() {
            if (!this.nextPressed) {
                String code = getCode();
                if (TextUtils.isEmpty(code)) {
                    AndroidUtilities.shakeView(this.codeFieldContainer, 2.0f, 0);
                    return;
                }
                this.nextPressed = true;
                int i = this.currentType;
                if (i == 2) {
                    AndroidUtilities.setWaitingForSms(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                }
                this.waitingForEvent = false;
                TL_account_changePhone tL_account_changePhone = new TL_account_changePhone();
                tL_account_changePhone.phone_number = this.requestPhone;
                tL_account_changePhone.phone_code = code;
                tL_account_changePhone.phone_code_hash = this.phoneHash;
                destroyTimer();
                this.this$0.needShowProgress();
                ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(tL_account_changePhone, new C3599x43bdcb51(this, tL_account_changePhone), 2);
            }
        }

        public /* synthetic */ void lambda$onNextPressed$7$ChangePhoneActivity$LoginActivitySmsView(TL_account_changePhone tL_account_changePhone, TLObject tLObject, TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new C1245x4b4db056(this, tL_error, tLObject, tL_account_changePhone));
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x009f  */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0092  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x00b3  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00f8 A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:36:0x00ee A:{LOOP_END, LOOP:0: B:34:0x00e9->B:36:0x00ee} */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0092  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x009f  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x00b3  */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x00ca  */
        /* JADX WARNING: Removed duplicated region for block: B:36:0x00ee A:{LOOP_END, LOOP:0: B:34:0x00e9->B:36:0x00ee} */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00f8 A:{SYNTHETIC} */
        /* JADX WARNING: Missing block: B:7:0x0077, code skipped:
            if (r8 != 2) goto L_0x0079;
     */
        /* JADX WARNING: Missing block: B:12:0x0081, code skipped:
            if (r8 != 3) goto L_0x0083;
     */
        /* JADX WARNING: Missing block: B:16:0x0089, code skipped:
            if (r6.nextType == 2) goto L_0x008b;
     */
        public /* synthetic */ void lambda$null$6$ChangePhoneActivity$LoginActivitySmsView(org.telegram.tgnet.TLRPC.TL_error r7, org.telegram.tgnet.TLObject r8, org.telegram.tgnet.TLRPC.TL_account_changePhone r9) {
            /*
            r6 = this;
            r0 = r6.this$0;
            r0.needHideProgress();
            r0 = 0;
            r6.nextPressed = r0;
            r1 = 0;
            r2 = 1;
            if (r7 != 0) goto L_0x0068;
        L_0x000c:
            r8 = (org.telegram.tgnet.TLRPC.User) r8;
            r6.destroyTimer();
            r6.destroyCodeTimer();
            r7 = r6.this$0;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.UserConfig.getInstance(r7);
            r7.setCurrentUser(r8);
            r7 = r6.this$0;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.UserConfig.getInstance(r7);
            r7.saveConfig(r2);
            r7 = new java.util.ArrayList;
            r7.<init>();
            r7.add(r8);
            r9 = r6.this$0;
            r9 = r9.currentAccount;
            r9 = org.telegram.messenger.MessagesStorage.getInstance(r9);
            r9.putUsersAndChats(r7, r1, r2, r2);
            r7 = r6.this$0;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.MessagesController.getInstance(r7);
            r7.putUser(r8, r0);
            r7 = r6.this$0;
            r7.finishFragment();
            r7 = r6.this$0;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.NotificationCenter.getInstance(r7);
            r8 = org.telegram.messenger.NotificationCenter.mainUserInfoChanged;
            r9 = new java.lang.Object[r0];
            r7.postNotificationName(r8, r9);
            goto L_0x00fd;
        L_0x0068:
            r8 = r7.text;
            r6.lastError = r8;
            r8 = r6.currentType;
            r3 = 4;
            r4 = 2;
            r5 = 3;
            if (r8 != r5) goto L_0x0079;
        L_0x0073:
            r8 = r6.nextType;
            if (r8 == r3) goto L_0x008b;
        L_0x0077:
            if (r8 == r4) goto L_0x008b;
        L_0x0079:
            r8 = r6.currentType;
            if (r8 != r4) goto L_0x0083;
        L_0x007d:
            r8 = r6.nextType;
            if (r8 == r3) goto L_0x008b;
        L_0x0081:
            if (r8 == r5) goto L_0x008b;
        L_0x0083:
            r8 = r6.currentType;
            if (r8 != r3) goto L_0x008e;
        L_0x0087:
            r8 = r6.nextType;
            if (r8 != r4) goto L_0x008e;
        L_0x008b:
            r6.createTimer();
        L_0x008e:
            r8 = r6.currentType;
            if (r8 != r4) goto L_0x009f;
        L_0x0092:
            org.telegram.messenger.AndroidUtilities.setWaitingForSms(r2);
            r8 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
            r3 = org.telegram.messenger.NotificationCenter.didReceiveSmsCode;
            r8.addObserver(r6, r3);
            goto L_0x00ad;
        L_0x009f:
            if (r8 != r5) goto L_0x00ad;
        L_0x00a1:
            org.telegram.messenger.AndroidUtilities.setWaitingForCall(r2);
            r8 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
            r3 = org.telegram.messenger.NotificationCenter.didReceiveCall;
            r8.addObserver(r6, r3);
        L_0x00ad:
            r6.waitingForEvent = r2;
            r8 = r6.currentType;
            if (r8 == r5) goto L_0x00c0;
        L_0x00b3:
            r8 = r6.this$0;
            r8 = r8.currentAccount;
            r3 = r6.this$0;
            r4 = new java.lang.Object[r0];
            org.telegram.p004ui.Components.AlertsCreator.processError(r8, r7, r3, r9, r4);
        L_0x00c0:
            r8 = r7.text;
            r9 = "PHONE_CODE_EMPTY";
            r8 = r8.contains(r9);
            if (r8 != 0) goto L_0x00e8;
        L_0x00ca:
            r8 = r7.text;
            r9 = "PHONE_CODE_INVALID";
            r8 = r8.contains(r9);
            if (r8 == 0) goto L_0x00d5;
        L_0x00d4:
            goto L_0x00e8;
        L_0x00d5:
            r7 = r7.text;
            r8 = "PHONE_CODE_EXPIRED";
            r7 = r7.contains(r8);
            if (r7 == 0) goto L_0x00fd;
        L_0x00df:
            r6.onBackPressed(r2);
            r7 = r6.this$0;
            r7.setPage(r0, r2, r1, r2);
            goto L_0x00fd;
        L_0x00e8:
            r7 = 0;
        L_0x00e9:
            r8 = r6.codeField;
            r9 = r8.length;
            if (r7 >= r9) goto L_0x00f8;
        L_0x00ee:
            r8 = r8[r7];
            r9 = "";
            r8.setText(r9);
            r7 = r7 + 1;
            goto L_0x00e9;
        L_0x00f8:
            r7 = r8[r0];
            r7.requestFocus();
        L_0x00fd:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChangePhoneActivity$LoginActivitySmsView.lambda$null$6$ChangePhoneActivity$LoginActivitySmsView(org.telegram.tgnet.TLRPC$TL_error, org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_account_changePhone):void");
        }

        public boolean onBackPressed(boolean z) {
            if (z) {
                TL_auth_cancelCode tL_auth_cancelCode = new TL_auth_cancelCode();
                tL_auth_cancelCode.phone_number = this.requestPhone;
                tL_auth_cancelCode.phone_code_hash = this.phoneHash;
                ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(tL_auth_cancelCode, C3600x4b5a7c12.INSTANCE, 10);
                destroyTimer();
                destroyCodeTimer();
                this.currentParams = null;
                int i = this.currentType;
                if (i == 2) {
                    AndroidUtilities.setWaitingForSms(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                }
                this.waitingForEvent = false;
                return true;
            }
            Builder builder = new Builder(this.this$0.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            builder.setMessage(LocaleController.getString("StopVerification", C1067R.string.StopVerification));
            builder.setPositiveButton(LocaleController.getString("Continue", C1067R.string.Continue), null);
            builder.setNegativeButton(LocaleController.getString("Stop", C1067R.string.Stop), new C1244xf39de2f7(this));
            this.this$0.showDialog(builder.create());
            return false;
        }

        public /* synthetic */ void lambda$onBackPressed$8$ChangePhoneActivity$LoginActivitySmsView(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            this.this$0.setPage(0, true, null, true);
        }

        public void onDestroyActivity() {
            super.onDestroyActivity();
            int i = this.currentType;
            if (i == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            destroyTimer();
            destroyCodeTimer();
        }

        public void onShow() {
            super.onShow();
            if (this.currentType != 3) {
                AndroidUtilities.runOnUIThread(new C1241x1e9b6982(this), 100);
            }
        }

        public /* synthetic */ void lambda$onShow$10$ChangePhoneActivity$LoginActivitySmsView() {
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            if (editTextBoldCursorArr != null) {
                int length = editTextBoldCursorArr.length - 1;
                while (length >= 0) {
                    if (length == 0 || this.codeField[length].length() != 0) {
                        this.codeField[length].requestFocus();
                        EditTextBoldCursor[] editTextBoldCursorArr2 = this.codeField;
                        editTextBoldCursorArr2[length].setSelection(editTextBoldCursorArr2[length].length());
                        AndroidUtilities.showKeyboard(this.codeField[length]);
                        return;
                    }
                    length--;
                }
            }
        }

        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (this.waitingForEvent) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (editTextBoldCursorArr != null) {
                    String str = "";
                    if (i == NotificationCenter.didReceiveSmsCode) {
                        EditText editText = editTextBoldCursorArr[0];
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(objArr[0]);
                        editText.setText(stringBuilder.toString());
                        onNextPressed();
                    } else if (i == NotificationCenter.didReceiveCall) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(objArr[0]);
                        String stringBuilder3 = stringBuilder2.toString();
                        if (AndroidUtilities.checkPhonePattern(this.pattern, stringBuilder3)) {
                            this.ignoreOnTextChange = true;
                            this.codeField[0].setText(stringBuilder3);
                            this.ignoreOnTextChange = false;
                            onNextPressed();
                        }
                    }
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ChangePhoneActivity$PhoneView */
    public class PhoneView extends SlideView implements OnItemSelectedListener {
        private EditTextBoldCursor codeField;
        private HashMap<String, String> codesMap = new HashMap();
        private ArrayList<String> countriesArray = new ArrayList();
        private HashMap<String, String> countriesMap = new HashMap();
        private TextView countryButton;
        private int countryState = 0;
        private boolean ignoreOnPhoneChange = false;
        private boolean ignoreOnTextChange = false;
        private boolean ignoreSelection = false;
        private boolean nextPressed = false;
        private HintEditText phoneField;
        private HashMap<String, String> phoneFormatMap = new HashMap();
        private TextView textView;
        private TextView textView2;
        final /* synthetic */ ChangePhoneActivity this$0;
        private View view;

        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public PhoneView(ChangePhoneActivity changePhoneActivity, Context context) {
            final ChangePhoneActivity changePhoneActivity2 = changePhoneActivity;
            Context context2 = context;
            this.this$0 = changePhoneActivity2;
            super(context2);
            setOrientation(1);
            this.countryButton = new TextView(context2);
            this.countryButton.setTextSize(1, 18.0f);
            this.countryButton.setPadding(AndroidUtilities.m26dp(12.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(12.0f), 0);
            TextView textView = this.countryButton;
            String str = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(str));
            this.countryButton.setMaxLines(1);
            this.countryButton.setSingleLine(true);
            this.countryButton.setEllipsize(TruncateAt.END);
            this.countryButton.setGravity((LocaleController.isRTL ? 5 : 3) | 1);
            this.countryButton.setBackgroundResource(C1067R.C1065drawable.spinner_states);
            addView(this.countryButton, LayoutHelper.createLinear(-1, 36, 0.0f, 0.0f, 0.0f, 14.0f));
            this.countryButton.setOnClickListener(new C1250xc6cdc08d(this));
            this.view = new View(context2);
            this.view.setPadding(AndroidUtilities.m26dp(12.0f), 0, AndroidUtilities.m26dp(12.0f), 0);
            this.view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayLine));
            addView(this.view, LayoutHelper.createLinear(-1, 1, 4.0f, -17.5f, 4.0f, 0.0f));
            LinearLayout linearLayout = new LinearLayout(context2);
            linearLayout.setOrientation(0);
            addView(linearLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 20.0f, 0.0f, 0.0f));
            this.textView = new TextView(context2);
            this.textView.setText("+");
            this.textView.setTextColor(Theme.getColor(str));
            this.textView.setTextSize(1, 18.0f);
            linearLayout.addView(this.textView, LayoutHelper.createLinear(-2, -2));
            this.codeField = new EditTextBoldCursor(context2);
            this.codeField.setInputType(3);
            this.codeField.setTextColor(Theme.getColor(str));
            this.codeField.setCursorColor(Theme.getColor(str));
            this.codeField.setCursorSize(AndroidUtilities.m26dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
            this.codeField.setPadding(AndroidUtilities.m26dp(10.0f), 0, 0, 0);
            this.codeField.setTextSize(1, 18.0f);
            this.codeField.setMaxLines(1);
            this.codeField.setGravity(19);
            this.codeField.setImeOptions(268435461);
            this.codeField.setFilters(new InputFilter[]{new LengthFilter(5)});
            linearLayout.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 16.0f, 0.0f));
            this.codeField.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!PhoneView.this.ignoreOnTextChange) {
                        PhoneView.this.ignoreOnTextChange = true;
                        String stripExceptNumbers = C0278PhoneFormat.stripExceptNumbers(PhoneView.this.codeField.getText().toString());
                        PhoneView.this.codeField.setText(stripExceptNumbers);
                        String str = null;
                        if (stripExceptNumbers.length() == 0) {
                            PhoneView.this.countryButton.setText(LocaleController.getString("ChooseCountry", C1067R.string.ChooseCountry));
                            PhoneView.this.phoneField.setHintText(null);
                            PhoneView.this.countryState = 1;
                        } else {
                            Object substring;
                            CharSequence charSequence;
                            Object obj;
                            int i = 4;
                            if (stripExceptNumbers.length() > 4) {
                                PhoneView.this.ignoreOnTextChange = true;
                                while (i >= 1) {
                                    substring = stripExceptNumbers.substring(0, i);
                                    if (((String) PhoneView.this.codesMap.get(substring)) != null) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(stripExceptNumbers.substring(i, stripExceptNumbers.length()));
                                        stringBuilder.append(PhoneView.this.phoneField.getText().toString());
                                        stripExceptNumbers = stringBuilder.toString();
                                        PhoneView.this.codeField.setText(substring);
                                        charSequence = stripExceptNumbers;
                                        obj = 1;
                                        break;
                                    }
                                    i--;
                                }
                                substring = stripExceptNumbers;
                                charSequence = null;
                                obj = null;
                                if (obj == null) {
                                    PhoneView.this.ignoreOnTextChange = true;
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(substring.substring(1, substring.length()));
                                    stringBuilder2.append(PhoneView.this.phoneField.getText().toString());
                                    charSequence = stringBuilder2.toString();
                                    EditTextBoldCursor access$400 = PhoneView.this.codeField;
                                    substring = substring.substring(0, 1);
                                    access$400.setText(substring);
                                }
                            } else {
                                substring = stripExceptNumbers;
                                charSequence = null;
                                obj = null;
                            }
                            String str2 = (String) PhoneView.this.codesMap.get(substring);
                            String str3 = "WrongCountry";
                            if (str2 != null) {
                                int indexOf = PhoneView.this.countriesArray.indexOf(str2);
                                if (indexOf != -1) {
                                    PhoneView.this.ignoreSelection = true;
                                    PhoneView.this.countryButton.setText((CharSequence) PhoneView.this.countriesArray.get(indexOf));
                                    String str4 = (String) PhoneView.this.phoneFormatMap.get(substring);
                                    HintEditText access$600 = PhoneView.this.phoneField;
                                    if (str4 != null) {
                                        str = str4.replace('X', 8211);
                                    }
                                    access$600.setHintText(str);
                                    PhoneView.this.countryState = 0;
                                } else {
                                    PhoneView.this.countryButton.setText(LocaleController.getString(str3, C1067R.string.WrongCountry));
                                    PhoneView.this.phoneField.setHintText(null);
                                    PhoneView.this.countryState = 2;
                                }
                            } else {
                                PhoneView.this.countryButton.setText(LocaleController.getString(str3, C1067R.string.WrongCountry));
                                PhoneView.this.phoneField.setHintText(null);
                                PhoneView.this.countryState = 2;
                            }
                            if (obj == null) {
                                PhoneView.this.codeField.setSelection(PhoneView.this.codeField.getText().length());
                            }
                            if (charSequence != null) {
                                PhoneView.this.phoneField.requestFocus();
                                PhoneView.this.phoneField.setText(charSequence);
                                PhoneView.this.phoneField.setSelection(PhoneView.this.phoneField.length());
                            }
                        }
                        PhoneView.this.ignoreOnTextChange = false;
                    }
                }
            });
            this.codeField.setOnEditorActionListener(new C1253x51ebe6ac(this));
            this.phoneField = new HintEditText(context2);
            this.phoneField.setInputType(3);
            this.phoneField.setTextColor(Theme.getColor(str));
            this.phoneField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
            this.phoneField.setPadding(0, 0, 0, 0);
            this.phoneField.setCursorColor(Theme.getColor(str));
            this.phoneField.setCursorSize(AndroidUtilities.m26dp(20.0f));
            this.phoneField.setCursorWidth(1.5f);
            this.phoneField.setTextSize(1, 18.0f);
            this.phoneField.setMaxLines(1);
            this.phoneField.setGravity(19);
            this.phoneField.setImeOptions(268435461);
            linearLayout.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0f));
            this.phoneField.addTextChangedListener(new TextWatcher() {
                private int actionPosition;
                private int characterAction = -1;

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (i2 == 0 && i3 == 1) {
                        this.characterAction = 1;
                    } else if (i2 != 1 || i3 != 0) {
                        this.characterAction = -1;
                    } else if (charSequence.charAt(i) != ' ' || i <= 0) {
                        this.characterAction = 2;
                    } else {
                        this.characterAction = 3;
                        this.actionPosition = i - 1;
                    }
                }

                public void afterTextChanged(Editable editable) {
                    if (!PhoneView.this.ignoreOnPhoneChange) {
                        StringBuilder stringBuilder;
                        int i;
                        int selectionStart = PhoneView.this.phoneField.getSelectionStart();
                        String obj = PhoneView.this.phoneField.getText().toString();
                        if (this.characterAction == 3) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(obj.substring(0, this.actionPosition));
                            stringBuilder.append(obj.substring(this.actionPosition + 1, obj.length()));
                            obj = stringBuilder.toString();
                            selectionStart--;
                        }
                        stringBuilder = new StringBuilder(obj.length());
                        int i2 = 0;
                        while (i2 < obj.length()) {
                            i = i2 + 1;
                            String substring = obj.substring(i2, i);
                            if ("0123456789".contains(substring)) {
                                stringBuilder.append(substring);
                            }
                            i2 = i;
                        }
                        PhoneView.this.ignoreOnPhoneChange = true;
                        obj = PhoneView.this.phoneField.getHintText();
                        if (obj != null) {
                            i2 = selectionStart;
                            selectionStart = 0;
                            while (selectionStart < stringBuilder.length()) {
                                if (selectionStart < obj.length()) {
                                    if (obj.charAt(selectionStart) == ' ') {
                                        stringBuilder.insert(selectionStart, ' ');
                                        selectionStart++;
                                        if (i2 == selectionStart) {
                                            i = this.characterAction;
                                            if (!(i == 2 || i == 3)) {
                                                i2++;
                                            }
                                        }
                                    }
                                    selectionStart++;
                                } else {
                                    stringBuilder.insert(selectionStart, ' ');
                                    if (i2 == selectionStart + 1) {
                                        selectionStart = this.characterAction;
                                        if (!(selectionStart == 2 || selectionStart == 3)) {
                                            selectionStart = i2 + 1;
                                        }
                                    }
                                    selectionStart = i2;
                                }
                            }
                            selectionStart = i2;
                        }
                        PhoneView.this.phoneField.setText(stringBuilder);
                        if (selectionStart >= 0) {
                            HintEditText access$600 = PhoneView.this.phoneField;
                            if (selectionStart > PhoneView.this.phoneField.length()) {
                                selectionStart = PhoneView.this.phoneField.length();
                            }
                            access$600.setSelection(selectionStart);
                        }
                        PhoneView.this.phoneField.onTextChange();
                        PhoneView.this.ignoreOnPhoneChange = false;
                    }
                }
            });
            this.phoneField.setOnEditorActionListener(new C1251xbafe7d6f(this));
            this.phoneField.setOnKeyListener(new C1248xaa944fd1(this));
            this.textView2 = new TextView(context2);
            this.textView2.setText(LocaleController.getString("ChangePhoneHelp", C1067R.string.ChangePhoneHelp));
            this.textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
            this.textView2.setTextSize(1, 14.0f);
            this.textView2.setGravity(LocaleController.isRTL ? 5 : 3);
            this.textView2.setLineSpacing((float) AndroidUtilities.m26dp(2.0f), 1.0f);
            addView(this.textView2, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 0, 28, 0, 10));
            HashMap hashMap = new HashMap();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("countries.txt")));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String[] split = readLine.split(";");
                    this.countriesArray.add(0, split[2]);
                    this.countriesMap.put(split[2], split[0]);
                    this.codesMap.put(split[0], split[2]);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], split[3]);
                    }
                    hashMap.put(split[1], split[2]);
                }
                bufferedReader.close();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            Collections.sort(this.countriesArray, C2079-$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
            Object obj = null;
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                if (telephonyManager != null) {
                    obj = telephonyManager.getSimCountryIso().toUpperCase();
                }
            } catch (Exception e2) {
                FileLog.m30e(e2);
            }
            if (obj != null) {
                String str2 = (String) hashMap.get(obj);
                if (!(str2 == null || this.countriesArray.indexOf(str2) == -1)) {
                    this.codeField.setText((CharSequence) this.countriesMap.get(str2));
                    this.countryState = 0;
                }
            }
            if (this.codeField.length() == 0) {
                this.countryButton.setText(LocaleController.getString("ChooseCountry", C1067R.string.ChooseCountry));
                this.phoneField.setHintText(null);
                this.countryState = 1;
            }
            if (this.codeField.length() != 0) {
                AndroidUtilities.showKeyboard(this.phoneField);
                this.phoneField.requestFocus();
                HintEditText hintEditText = this.phoneField;
                hintEditText.setSelection(hintEditText.length());
                return;
            }
            AndroidUtilities.showKeyboard(this.codeField);
            this.codeField.requestFocus();
        }

        public /* synthetic */ void lambda$new$2$ChangePhoneActivity$PhoneView(View view) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(true);
            countrySelectActivity.setCountrySelectActivityDelegate(new C3601xb1269e64(this));
            this.this$0.presentFragment(countrySelectActivity);
        }

        public /* synthetic */ void lambda$null$1$ChangePhoneActivity$PhoneView(String str, String str2) {
            selectCountry(str);
            AndroidUtilities.runOnUIThread(new C1252xb4778489(this), 300);
            this.phoneField.requestFocus();
            HintEditText hintEditText = this.phoneField;
            hintEditText.setSelection(hintEditText.length());
        }

        public /* synthetic */ void lambda$null$0$ChangePhoneActivity$PhoneView() {
            AndroidUtilities.showKeyboard(this.phoneField);
        }

        public /* synthetic */ boolean lambda$new$3$ChangePhoneActivity$PhoneView(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            this.phoneField.requestFocus();
            HintEditText hintEditText = this.phoneField;
            hintEditText.setSelection(hintEditText.length());
            return true;
        }

        public /* synthetic */ boolean lambda$new$4$ChangePhoneActivity$PhoneView(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5) {
                return false;
            }
            onNextPressed();
            return true;
        }

        public /* synthetic */ boolean lambda$new$5$ChangePhoneActivity$PhoneView(View view, int i, KeyEvent keyEvent) {
            if (i != 67 || this.phoneField.length() != 0) {
                return false;
            }
            this.codeField.requestFocus();
            EditTextBoldCursor editTextBoldCursor = this.codeField;
            editTextBoldCursor.setSelection(editTextBoldCursor.length());
            this.codeField.dispatchKeyEvent(keyEvent);
            return true;
        }

        public void selectCountry(String str) {
            if (this.countriesArray.indexOf(str) != -1) {
                this.ignoreOnTextChange = true;
                String str2 = (String) this.countriesMap.get(str);
                this.codeField.setText(str2);
                this.countryButton.setText(str);
                str = (String) this.phoneFormatMap.get(str2);
                this.phoneField.setHintText(str != null ? str.replace('X', 8211) : null);
                this.countryState = 0;
                this.ignoreOnTextChange = false;
            }
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (this.ignoreSelection) {
                this.ignoreSelection = false;
                return;
            }
            this.ignoreOnTextChange = true;
            this.codeField.setText((CharSequence) this.countriesMap.get((String) this.countriesArray.get(i)));
            this.ignoreOnTextChange = false;
        }

        public void onCancelPressed() {
            this.nextPressed = false;
        }

        public void onNextPressed() {
            String str = "ephone";
            if (!(this.this$0.getParentActivity() == null || this.nextPressed)) {
                Object obj;
                String str2;
                String str3 = "phone";
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService(str3);
                Object obj2 = (telephonyManager.getSimState() == 1 || telephonyManager.getPhoneType() == 0) ? null : 1;
                if (VERSION.SDK_INT < 23 || obj2 == null) {
                    obj = 1;
                } else {
                    String str4 = "android.permission.READ_PHONE_STATE";
                    obj = this.this$0.getParentActivity().checkSelfPermission(str4) == 0 ? 1 : null;
                    if (this.this$0.checkPermissions) {
                        this.this$0.permissionsItems.clear();
                        if (obj == null) {
                            this.this$0.permissionsItems.add(str4);
                        }
                        if (!this.this$0.permissionsItems.isEmpty()) {
                            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                            str2 = "firstlogin";
                            if (globalMainSettings.getBoolean(str2, true) || this.this$0.getParentActivity().shouldShowRequestPermissionRationale(str4)) {
                                globalMainSettings.edit().putBoolean(str2, false).commit();
                                Builder builder = new Builder(this.this$0.getParentActivity());
                                builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                                builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
                                builder.setMessage(LocaleController.getString("AllowReadCall", C1067R.string.AllowReadCall));
                                ChangePhoneActivity changePhoneActivity = this.this$0;
                                changePhoneActivity.permissionsDialog = changePhoneActivity.showDialog(builder.create());
                            } else {
                                this.this$0.getParentActivity().requestPermissions((String[]) this.this$0.permissionsItems.toArray(new String[this.this$0.permissionsItems.size()]), 6);
                            }
                            return;
                        }
                    }
                }
                if (this.countryState == 1) {
                    AlertsCreator.showSimpleAlert(this.this$0, LocaleController.getString("ChooseCountry", C1067R.string.ChooseCountry));
                } else if (this.codeField.length() == 0) {
                    AlertsCreator.showSimpleAlert(this.this$0, LocaleController.getString("InvalidPhoneNumber", C1067R.string.InvalidPhoneNumber));
                } else {
                    TL_account_sendChangePhoneCode tL_account_sendChangePhoneCode = new TL_account_sendChangePhoneCode();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(this.codeField.getText());
                    stringBuilder.append(this.phoneField.getText());
                    String stripExceptNumbers = C0278PhoneFormat.stripExceptNumbers(stringBuilder.toString());
                    tL_account_sendChangePhoneCode.phone_number = stripExceptNumbers;
                    tL_account_sendChangePhoneCode.settings = new TL_codeSettings();
                    TL_codeSettings tL_codeSettings = tL_account_sendChangePhoneCode.settings;
                    boolean z = (obj2 == null || obj == null) ? false : true;
                    tL_codeSettings.allow_flashcall = z;
                    if (VERSION.SDK_INT >= 26) {
                        try {
                            tL_account_sendChangePhoneCode.settings.app_hash = SmsManager.getDefault().createAppSpecificSmsToken(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, SmsReceiver.class), 134217728));
                        } catch (Throwable th) {
                            FileLog.m30e(th);
                        }
                    } else {
                        TL_codeSettings tL_codeSettings2 = tL_account_sendChangePhoneCode.settings;
                        tL_codeSettings2.app_hash = BuildVars.SMS_HASH;
                        tL_codeSettings2.app_hash_persistent = true;
                    }
                    SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                    String str5 = "sms_hash";
                    if (TextUtils.isEmpty(tL_account_sendChangePhoneCode.settings.app_hash)) {
                        sharedPreferences.edit().remove(str5).commit();
                    } else {
                        TL_codeSettings tL_codeSettings3 = tL_account_sendChangePhoneCode.settings;
                        tL_codeSettings3.flags |= 8;
                        sharedPreferences.edit().putString(str5, tL_account_sendChangePhoneCode.settings.app_hash).commit();
                    }
                    if (tL_account_sendChangePhoneCode.settings.allow_flashcall) {
                        try {
                            str2 = telephonyManager.getLine1Number();
                            if (TextUtils.isEmpty(str2)) {
                                tL_account_sendChangePhoneCode.settings.current_number = false;
                            } else {
                                tL_account_sendChangePhoneCode.settings.current_number = PhoneNumberUtils.compare(stripExceptNumbers, str2);
                                if (!tL_account_sendChangePhoneCode.settings.current_number) {
                                    tL_account_sendChangePhoneCode.settings.allow_flashcall = false;
                                }
                            }
                        } catch (Exception e) {
                            tL_account_sendChangePhoneCode.settings.allow_flashcall = false;
                            FileLog.m30e(e);
                        }
                    }
                    Bundle bundle = new Bundle();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    String str6 = "+";
                    stringBuilder2.append(str6);
                    stringBuilder2.append(this.codeField.getText());
                    String str7 = " ";
                    stringBuilder2.append(str7);
                    stringBuilder2.append(this.phoneField.getText());
                    bundle.putString(str3, stringBuilder2.toString());
                    StringBuilder stringBuilder3;
                    try {
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str6);
                        stringBuilder3.append(C0278PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()));
                        stringBuilder3.append(str7);
                        stringBuilder3.append(C0278PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                        bundle.putString(str, stringBuilder3.toString());
                    } catch (Exception e2) {
                        FileLog.m30e(e2);
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str6);
                        stringBuilder3.append(stripExceptNumbers);
                        bundle.putString(str, stringBuilder3.toString());
                    }
                    bundle.putString("phoneFormated", stripExceptNumbers);
                    this.nextPressed = true;
                    this.this$0.needShowProgress();
                    ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(tL_account_sendChangePhoneCode, new C3602xc5c8cace(this, bundle, tL_account_sendChangePhoneCode), 2);
                }
            }
        }

        public /* synthetic */ void lambda$onNextPressed$7$ChangePhoneActivity$PhoneView(Bundle bundle, TL_account_sendChangePhoneCode tL_account_sendChangePhoneCode, TLObject tLObject, TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new C1249x41d0fa5a(this, tL_error, bundle, tLObject, tL_account_sendChangePhoneCode));
        }

        public /* synthetic */ void lambda$null$6$ChangePhoneActivity$PhoneView(TL_error tL_error, Bundle bundle, TLObject tLObject, TL_account_sendChangePhoneCode tL_account_sendChangePhoneCode) {
            this.nextPressed = false;
            if (tL_error == null) {
                this.this$0.fillNextCodeParams(bundle, (TL_auth_sentCode) tLObject);
            } else {
                AlertsCreator.processError(this.this$0.currentAccount, tL_error, this.this$0, tL_account_sendChangePhoneCode, bundle.getString("phone"));
            }
            this.this$0.needHideProgress();
        }

        public void onShow() {
            super.onShow();
            if (this.phoneField == null) {
                return;
            }
            if (this.codeField.length() != 0) {
                AndroidUtilities.showKeyboard(this.phoneField);
                this.phoneField.requestFocus();
                HintEditText hintEditText = this.phoneField;
                hintEditText.setSelection(hintEditText.length());
                return;
            }
            AndroidUtilities.showKeyboard(this.codeField);
            this.codeField.requestFocus();
        }

        public String getHeaderName() {
            return LocaleController.getString("ChangePhoneNewNumber", C1067R.string.ChangePhoneNewNumber);
        }
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        int i = 0;
        while (true) {
            SlideView[] slideViewArr = this.views;
            if (i >= slideViewArr.length) {
                break;
            }
            if (slideViewArr[i] != null) {
                slideViewArr[i].onDestroyActivity();
            }
            i++;
        }
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            this.progressDialog = null;
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    public View createView(Context context) {
        this.actionBar.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setActionBarMenuOnItemClick(new C39451());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
        C23822 c23822 = new ScrollView(context) {
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                if (ChangePhoneActivity.this.currentViewNum == 1 || ChangePhoneActivity.this.currentViewNum == 2 || ChangePhoneActivity.this.currentViewNum == 4) {
                    rect.bottom += AndroidUtilities.m26dp(40.0f);
                }
                return super.requestChildRectangleOnScreen(view, rect, z);
            }

            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                ChangePhoneActivity.this.scrollHeight = MeasureSpec.getSize(i2) - AndroidUtilities.m26dp(30.0f);
                super.onMeasure(i, i2);
            }
        };
        c23822.setFillViewport(true);
        this.fragmentView = c23822;
        FrameLayout frameLayout = new FrameLayout(context);
        c23822.addView(frameLayout, LayoutHelper.createScroll(-1, -2, 51));
        this.views[0] = new PhoneView(this, context);
        this.views[1] = new LoginActivitySmsView(this, context, 1);
        this.views[2] = new LoginActivitySmsView(this, context, 2);
        this.views[3] = new LoginActivitySmsView(this, context, 3);
        this.views[4] = new LoginActivitySmsView(this, context, 4);
        int i = 0;
        while (true) {
            SlideView[] slideViewArr = this.views;
            if (i < slideViewArr.length) {
                slideViewArr[i].setVisibility(i == 0 ? 0 : 8);
                frameLayout.addView(this.views[i], LayoutHelper.createFrame(-1, i == 0 ? -2.0f : -1.0f, 51, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 30.0f, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 0.0f));
                i++;
            } else {
                this.actionBar.setTitle(slideViewArr[0].getHeaderName());
                return this.fragmentView;
            }
        }
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i == 6) {
            this.checkPermissions = false;
            i = this.currentViewNum;
            if (i == 0) {
                this.views[i].onNextPressed();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDialogDismiss(Dialog dialog) {
        if (VERSION.SDK_INT >= 23 && dialog == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
            Activity parentActivity = getParentActivity();
            ArrayList arrayList = this.permissionsItems;
            parentActivity.requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 6);
        }
    }

    public boolean onBackPressed() {
        int i = this.currentViewNum;
        int i2 = 0;
        if (i == 0) {
            while (true) {
                SlideView[] slideViewArr = this.views;
                if (i2 >= slideViewArr.length) {
                    return true;
                }
                if (slideViewArr[i2] != null) {
                    slideViewArr[i2].onDestroyActivity();
                }
                i2++;
            }
        } else {
            if (this.views[i].onBackPressed(false)) {
                setPage(0, true, null, true);
            }
            return false;
        }
    }

    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.views[this.currentViewNum].onShow();
        }
    }

    public void needShowProgress() {
        if (getParentActivity() != null && !getParentActivity().isFinishing() && this.progressDialog == null) {
            this.progressDialog = new AlertDialog(getParentActivity(), 3);
            this.progressDialog.setCanCacnel(false);
            this.progressDialog.show();
        }
    }

    public void needHideProgress() {
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            this.progressDialog = null;
        }
    }

    public void setPage(int i, boolean z, Bundle bundle, boolean z2) {
        if (i == 3) {
            this.doneButton.setVisibility(8);
        } else {
            if (i == 0) {
                this.checkPermissions = true;
            }
            this.doneButton.setVisibility(0);
        }
        SlideView[] slideViewArr = this.views;
        final Object obj = slideViewArr[this.currentViewNum];
        final SlideView slideView = slideViewArr[i];
        this.currentViewNum = i;
        slideView.setParams(bundle, false);
        this.actionBar.setTitle(slideView.getHeaderName());
        slideView.onShow();
        slideView.setX((float) (z2 ? -AndroidUtilities.displaySize.x : AndroidUtilities.displaySize.x));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);
        Animator[] animatorArr = new Animator[2];
        float[] fArr = new float[1];
        fArr[0] = (float) (z2 ? AndroidUtilities.displaySize.x : -AndroidUtilities.displaySize.x);
        String str = "translationX";
        animatorArr[0] = ObjectAnimator.ofFloat(obj, str, fArr);
        animatorArr[1] = ObjectAnimator.ofFloat(slideView, str, new float[]{0.0f});
        animatorSet.playTogether(animatorArr);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                slideView.setVisibility(0);
            }

            public void onAnimationEnd(Animator animator) {
                obj.setVisibility(8);
                obj.setX(0.0f);
            }
        });
        animatorSet.start();
    }

    private void fillNextCodeParams(Bundle bundle, TL_auth_sentCode tL_auth_sentCode) {
        bundle.putString("phoneHash", tL_auth_sentCode.phone_code_hash);
        auth_CodeType auth_codetype = tL_auth_sentCode.next_type;
        String str = "nextType";
        if (auth_codetype instanceof TL_auth_codeTypeCall) {
            bundle.putInt(str, 4);
        } else if (auth_codetype instanceof TL_auth_codeTypeFlashCall) {
            bundle.putInt(str, 3);
        } else if (auth_codetype instanceof TL_auth_codeTypeSms) {
            bundle.putInt(str, 2);
        }
        String str2 = "length";
        String str3 = "type";
        if (tL_auth_sentCode.type instanceof TL_auth_sentCodeTypeApp) {
            bundle.putInt(str3, 1);
            bundle.putInt(str2, tL_auth_sentCode.type.length);
            setPage(1, true, bundle, false);
            return;
        }
        if (tL_auth_sentCode.timeout == 0) {
            tL_auth_sentCode.timeout = 60;
        }
        bundle.putInt("timeout", tL_auth_sentCode.timeout * 1000);
        auth_SentCodeType auth_sentcodetype = tL_auth_sentCode.type;
        if (auth_sentcodetype instanceof TL_auth_sentCodeTypeCall) {
            bundle.putInt(str3, 4);
            bundle.putInt(str2, tL_auth_sentCode.type.length);
            setPage(4, true, bundle, false);
        } else if (auth_sentcodetype instanceof TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt(str3, 3);
            bundle.putString("pattern", tL_auth_sentCode.type.pattern);
            setPage(3, true, bundle, false);
        } else if (auth_sentcodetype instanceof TL_auth_sentCodeTypeSms) {
            bundle.putInt(str3, 2);
            bundle.putInt(str2, tL_auth_sentCode.type.length);
            setPage(2, true, bundle, false);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        int i;
        SlideView[] slideViewArr = this.views;
        PhoneView phoneView = (PhoneView) slideViewArr[0];
        LoginActivitySmsView loginActivitySmsView = (LoginActivitySmsView) slideViewArr[1];
        LoginActivitySmsView loginActivitySmsView2 = (LoginActivitySmsView) slideViewArr[2];
        LoginActivitySmsView loginActivitySmsView3 = (LoginActivitySmsView) slideViewArr[3];
        LoginActivitySmsView loginActivitySmsView4 = (LoginActivitySmsView) slideViewArr[4];
        ArrayList arrayList = new ArrayList();
        ThemeDescription themeDescription = r9;
        ThemeDescription themeDescription2 = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        arrayList.add(themeDescription);
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(phoneView.countryButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(phoneView.view, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhiteGrayLine));
        arrayList.add(new ThemeDescription(phoneView.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(phoneView.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(phoneView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(phoneView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        arrayList.add(new ThemeDescription(phoneView.phoneField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(phoneView.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(phoneView.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(phoneView.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        arrayList.add(new ThemeDescription(phoneView.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        if (loginActivitySmsView.codeField != null) {
            for (i = 0; i < loginActivitySmsView.codeField.length; i++) {
                arrayList.add(new ThemeDescription(loginActivitySmsView.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(loginActivitySmsView.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
            }
        }
        arrayList.add(new ThemeDescription(loginActivitySmsView.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        View access$3300 = loginActivitySmsView.progressView;
        Class[] clsArr = new Class[]{ProgressView.class};
        String[] strArr = new String[1];
        strArr[0] = "paint";
        arrayList.add(new ThemeDescription(access$3300, 0, clsArr, strArr, null, null, null, Theme.key_login_progressInner));
        arrayList.add(new ThemeDescription(loginActivitySmsView.progressView, 0, new Class[]{ProgressView.class}, new String[]{"paint"}, null, null, null, Theme.key_login_progressOuter));
        arrayList.add(new ThemeDescription(loginActivitySmsView.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(loginActivitySmsView.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        if (loginActivitySmsView2.codeField != null) {
            for (i = 0; i < loginActivitySmsView2.codeField.length; i++) {
                arrayList.add(new ThemeDescription(loginActivitySmsView2.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(loginActivitySmsView2.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
            }
        }
        arrayList.add(new ThemeDescription(loginActivitySmsView2.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressInner));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressOuter));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(loginActivitySmsView2.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        if (loginActivitySmsView3.codeField != null) {
            for (i = 0; i < loginActivitySmsView3.codeField.length; i++) {
                arrayList.add(new ThemeDescription(loginActivitySmsView3.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(loginActivitySmsView3.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
            }
        }
        arrayList.add(new ThemeDescription(loginActivitySmsView3.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressInner));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressOuter));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(loginActivitySmsView3.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        if (loginActivitySmsView4.codeField != null) {
            for (i = 0; i < loginActivitySmsView4.codeField.length; i++) {
                arrayList.add(new ThemeDescription(loginActivitySmsView4.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(loginActivitySmsView4.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
            }
        }
        arrayList.add(new ThemeDescription(loginActivitySmsView4.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressInner));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[]{ProgressView.class}, new String[]{r3}, null, null, null, Theme.key_login_progressOuter));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(loginActivitySmsView4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionBackground));
        return (ThemeDescription[]) arrayList.toArray(new ThemeDescription[arrayList.size()]);
    }
}