package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AndroidUtilities.LinkMovementMethodMy;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.EditTextSettingsCell;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.PaymentInfoCell;
import org.telegram.p004ui.Cells.RadioCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextDetailSettingsCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextPriceCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.ContextProgressView;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.HintEditText;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.C1158xb6caa888;
import org.telegram.tgnet.TLRPC.InputPaymentCredentials;
import org.telegram.tgnet.TLRPC.PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC.TL_account_confirmPasswordEmail;
import org.telegram.tgnet.TLRPC.TL_account_getPassword;
import org.telegram.tgnet.TLRPC.TL_account_getTmpPassword;
import org.telegram.tgnet.TLRPC.TL_account_password;
import org.telegram.tgnet.TLRPC.TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC.TL_account_resendPasswordEmail;
import org.telegram.tgnet.TLRPC.TL_account_tmpPassword;
import org.telegram.tgnet.TLRPC.TL_account_updatePasswordSettings;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_dataJSON;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputCheckPasswordEmpty;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentials;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentialsAndroidPay;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentialsSaved;
import org.telegram.tgnet.TLRPC.TL_invoice;
import org.telegram.tgnet.TLRPC.TL_labeledPrice;
import org.telegram.tgnet.TLRPC.TL_paymentRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_paymentSavedCredentialsCard;
import org.telegram.tgnet.TLRPC.TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC.TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC.TL_payments_paymentResult;
import org.telegram.tgnet.TLRPC.TL_payments_paymentVerficationNeeded;
import org.telegram.tgnet.TLRPC.TL_payments_sendPaymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_validateRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_payments_validatedRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_postAddress;
import org.telegram.tgnet.TLRPC.TL_shippingOption;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.PaymentFormActivity */
public class PaymentFormActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int FIELDS_COUNT_ADDRESS = 10;
    private static final int FIELDS_COUNT_CARD = 6;
    private static final int FIELDS_COUNT_PASSWORD = 3;
    private static final int FIELDS_COUNT_SAVEDCARD = 2;
    private static final int FIELD_CARD = 0;
    private static final int FIELD_CARDNAME = 2;
    private static final int FIELD_CARD_COUNTRY = 4;
    private static final int FIELD_CARD_POSTCODE = 5;
    private static final int FIELD_CITY = 2;
    private static final int FIELD_COUNTRY = 4;
    private static final int FIELD_CVV = 3;
    private static final int FIELD_EMAIL = 7;
    private static final int FIELD_ENTERPASSWORD = 0;
    private static final int FIELD_ENTERPASSWORDEMAIL = 2;
    private static final int FIELD_EXPIRE_DATE = 1;
    private static final int FIELD_NAME = 6;
    private static final int FIELD_PHONE = 9;
    private static final int FIELD_PHONECODE = 8;
    private static final int FIELD_POSTCODE = 5;
    private static final int FIELD_REENTERPASSWORD = 1;
    private static final int FIELD_SAVEDCARD = 0;
    private static final int FIELD_SAVEDPASSWORD = 1;
    private static final int FIELD_STATE = 3;
    private static final int FIELD_STREET1 = 0;
    private static final int FIELD_STREET2 = 1;
    private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;
    private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
    private static final int done_button = 1;
    private static final int fragment_container_id = 4000;
    private int androidPayBackgroundColor;
    private boolean androidPayBlackTheme;
    private FrameLayout androidPayContainer;
    private TL_inputPaymentCredentialsAndroidPay androidPayCredentials;
    private String androidPayPublicKey;
    private User botUser;
    private TextInfoPrivacyCell[] bottomCell;
    private FrameLayout bottomLayout;
    private boolean canceled;
    private String cardName;
    private TextCheckCell checkCell1;
    private EditTextSettingsCell codeFieldCell;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private String countryName;
    private String currentBotName;
    private String currentItemName;
    private TL_account_password currentPassword;
    private int currentStep;
    private PaymentFormActivityDelegate delegate;
    private TextDetailSettingsCell[] detailSettingsCell;
    private ArrayList<View> dividers;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private boolean donePressed;
    private int emailCodeLength;
    private HeaderCell[] headerCell;
    private boolean ignoreOnCardChange;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private EditTextBoldCursor[] inputFields;
    private boolean isWebView;
    private LinearLayout linearLayout2;
    private boolean loadingPasswordInfo;
    private MessageObject messageObject;
    private boolean need_card_country;
    private boolean need_card_name;
    private boolean need_card_postcode;
    private PaymentFormActivity passwordFragment;
    private boolean passwordOk;
    private TextView payTextView;
    private TL_payments_paymentForm paymentForm;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private HashMap<String, String> phoneFormatMap;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private RadioCell[] radioCells;
    private TL_payments_validatedRequestedInfo requestedInfo;
    private boolean saveCardInfo;
    private boolean saveShippingInfo;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell[] settingsCell;
    private TL_shippingOption shippingOption;
    private Runnable shortPollRunnable;
    private boolean shouldNavigateBack;
    private String stripeApiKey;
    private TextView textView;
    private String totalPriceDecimal;
    private TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private String webViewUrl;
    private boolean webviewLoading;

    /* renamed from: org.telegram.ui.PaymentFormActivity$14 */
    class C313614 extends WebViewClient {
        C313614() {
        }

        public void onLoadResource(WebView webView, String str) {
            try {
                if ("t.me".equals(Uri.parse(str).getHost())) {
                    PaymentFormActivity.this.goToNextStep();
                    return;
                }
            } catch (Exception unused) {
            }
            super.onLoadResource(webView, str);
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            PaymentFormActivity.this.webviewLoading = false;
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.updateSavePaymentField();
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            try {
                if ("t.me".equals(Uri.parse(str).getHost())) {
                    PaymentFormActivity.this.goToNextStep();
                    return true;
                }
            } catch (Exception unused) {
            }
            return false;
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$15 */
    class C313715 implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C313715() {
        }

        public void afterTextChanged(Editable editable) {
            if (PaymentFormActivity.this.emailCodeLength != 0 && editable.length() == PaymentFormActivity.this.emailCodeLength) {
                PaymentFormActivity.this.sendSavePassword(false);
            }
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$3 */
    class C31423 implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C31423() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x011c  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0128  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x0145  */
        public void afterTextChanged(android.text.Editable r12) {
            /*
            r11 = this;
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r12 = r12.ignoreOnTextChange;
            if (r12 == 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r0 = 1;
            r12.ignoreOnTextChange = r0;
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r12 = r12.inputFields;
            r1 = 8;
            r12 = r12[r1];
            r12 = r12.getText();
            r12 = r12.toString();
            r12 = org.telegram.PhoneFormat.C0278PhoneFormat.stripExceptNumbers(r12);
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r2 = r2[r1];
            r2.setText(r12);
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r3 = 9;
            r2 = r2[r3];
            r2 = (org.telegram.p004ui.Components.HintEditText) r2;
            r4 = r12.length();
            r5 = 2131560398; // 0x7f0d07ce float:1.8746167E38 double:1.0531307647E-314;
            r6 = "PaymentShippingPhoneNumber";
            r7 = 0;
            r8 = 0;
            if (r4 != 0) goto L_0x0055;
        L_0x0049:
            r2.setHintText(r7);
            r12 = org.telegram.messenger.LocaleController.getString(r6, r5);
            r2.setHint(r12);
            goto L_0x0152;
        L_0x0055:
            r4 = r12.length();
            r9 = 4;
            if (r4 <= r9) goto L_0x00de;
        L_0x005c:
            if (r9 < r0) goto L_0x00a4;
        L_0x005e:
            r4 = r12.substring(r8, r9);
            r10 = org.telegram.p004ui.PaymentFormActivity.this;
            r10 = r10.codesMap;
            r10 = r10.get(r4);
            r10 = (java.lang.String) r10;
            if (r10 == 0) goto L_0x00a1;
        L_0x0070:
            r10 = new java.lang.StringBuilder;
            r10.<init>();
            r12 = r12.substring(r9);
            r10.append(r12);
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r12 = r12.inputFields;
            r12 = r12[r3];
            r12 = r12.getText();
            r12 = r12.toString();
            r10.append(r12);
            r12 = r10.toString();
            r9 = org.telegram.p004ui.PaymentFormActivity.this;
            r9 = r9.inputFields;
            r9 = r9[r1];
            r9.setText(r4);
            r9 = r12;
            r12 = 1;
            goto L_0x00a7;
        L_0x00a1:
            r9 = r9 + -1;
            goto L_0x005c;
        L_0x00a4:
            r4 = r12;
            r9 = r7;
            r12 = 0;
        L_0x00a7:
            if (r12 != 0) goto L_0x00dc;
        L_0x00a9:
            r9 = new java.lang.StringBuilder;
            r9.<init>();
            r10 = r4.substring(r0);
            r9.append(r10);
            r10 = org.telegram.p004ui.PaymentFormActivity.this;
            r10 = r10.inputFields;
            r3 = r10[r3];
            r3 = r3.getText();
            r3 = r3.toString();
            r9.append(r3);
            r3 = r9.toString();
            r9 = org.telegram.p004ui.PaymentFormActivity.this;
            r9 = r9.inputFields;
            r9 = r9[r1];
            r4 = r4.substring(r8, r0);
            r9.setText(r4);
            goto L_0x00e1;
        L_0x00dc:
            r3 = r9;
            goto L_0x00e1;
        L_0x00de:
            r4 = r12;
            r3 = r7;
            r12 = 0;
        L_0x00e1:
            r9 = org.telegram.p004ui.PaymentFormActivity.this;
            r9 = r9.codesMap;
            r9 = r9.get(r4);
            r9 = (java.lang.String) r9;
            if (r9 == 0) goto L_0x0119;
        L_0x00ef:
            r10 = org.telegram.p004ui.PaymentFormActivity.this;
            r10 = r10.countriesArray;
            r9 = r10.indexOf(r9);
            r10 = -1;
            if (r9 == r10) goto L_0x0119;
        L_0x00fc:
            r9 = org.telegram.p004ui.PaymentFormActivity.this;
            r9 = r9.phoneFormatMap;
            r4 = r9.get(r4);
            r4 = (java.lang.String) r4;
            if (r4 == 0) goto L_0x0119;
        L_0x010a:
            r9 = 88;
            r10 = 8211; // 0x2013 float:1.1506E-41 double:4.057E-320;
            r4 = r4.replace(r9, r10);
            r2.setHintText(r4);
            r2.setHint(r7);
            goto L_0x011a;
        L_0x0119:
            r0 = 0;
        L_0x011a:
            if (r0 != 0) goto L_0x0126;
        L_0x011c:
            r2.setHintText(r7);
            r0 = org.telegram.messenger.LocaleController.getString(r6, r5);
            r2.setHint(r0);
        L_0x0126:
            if (r12 != 0) goto L_0x0143;
        L_0x0128:
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r12 = r12.inputFields;
            r12 = r12[r1];
            r0 = org.telegram.p004ui.PaymentFormActivity.this;
            r0 = r0.inputFields;
            r0 = r0[r1];
            r0 = r0.getText();
            r0 = r0.length();
            r12.setSelection(r0);
        L_0x0143:
            if (r3 == 0) goto L_0x0152;
        L_0x0145:
            r2.requestFocus();
            r2.setText(r3);
            r12 = r2.length();
            r2.setSelection(r12);
        L_0x0152:
            r12 = org.telegram.p004ui.PaymentFormActivity.this;
            r12.ignoreOnTextChange = r8;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PaymentFormActivity$C31423.afterTextChanged(android.text.Editable):void");
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$4 */
    class C31434 implements TextWatcher {
        private int actionPosition;
        private int characterAction = -1;

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C31434() {
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
            if (!PaymentFormActivity.this.ignoreOnPhoneChange) {
                StringBuilder stringBuilder;
                int i;
                HintEditText hintEditText = (HintEditText) PaymentFormActivity.this.inputFields[9];
                int selectionStart = hintEditText.getSelectionStart();
                String obj = hintEditText.getText().toString();
                if (this.characterAction == 3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(obj.substring(0, this.actionPosition));
                    stringBuilder.append(obj.substring(this.actionPosition + 1));
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
                PaymentFormActivity.this.ignoreOnPhoneChange = true;
                obj = hintEditText.getHintText();
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
                hintEditText.setText(stringBuilder);
                if (selectionStart >= 0) {
                    if (selectionStart > hintEditText.length()) {
                        selectionStart = hintEditText.length();
                    }
                    hintEditText.setSelection(selectionStart);
                }
                hintEditText.onTextChange();
                PaymentFormActivity.this.ignoreOnPhoneChange = false;
            }
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$6 */
    class C31456 extends WebViewClient {
        C31456() {
        }

        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
            paymentFormActivity.shouldNavigateBack = str.equals(paymentFormActivity.webViewUrl) ^ 1;
            return super.shouldOverrideUrlLoading(webView, str);
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            PaymentFormActivity.this.webviewLoading = false;
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.updateSavePaymentField();
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$7 */
    class C31467 implements TextWatcher {
        public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
        public static final int MAX_LENGTH_DINERS_CLUB = 14;
        public static final int MAX_LENGTH_STANDARD = 16;
        public final String[] PREFIXES_14 = new String[]{"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
        public final String[] PREFIXES_15 = new String[]{"34", "37"};
        public final String[] PREFIXES_16 = new String[]{"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35"};
        private int actionPosition;
        private int characterAction = -1;

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C31467() {
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
            if (!PaymentFormActivity.this.ignoreOnCardChange) {
                StringBuilder stringBuilder;
                String substring;
                EditText editText = PaymentFormActivity.this.inputFields[0];
                int selectionStart = editText.getSelectionStart();
                String obj = editText.getText().toString();
                int i = 3;
                if (this.characterAction == 3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(obj.substring(0, this.actionPosition));
                    stringBuilder.append(obj.substring(this.actionPosition + 1));
                    obj = stringBuilder.toString();
                    selectionStart--;
                }
                stringBuilder = new StringBuilder(obj.length());
                int i2 = 0;
                while (i2 < obj.length()) {
                    int i3 = i2 + 1;
                    substring = obj.substring(i2, i3);
                    if ("0123456789".contains(substring)) {
                        stringBuilder.append(substring);
                    }
                    i2 = i3;
                }
                PaymentFormActivity.this.ignoreOnCardChange = true;
                obj = null;
                i2 = 100;
                if (stringBuilder.length() > 0) {
                    String stringBuilder2 = stringBuilder.toString();
                    substring = null;
                    int i4 = 0;
                    int i5 = 100;
                    while (i4 < i) {
                        String[] strArr;
                        int i6;
                        String str;
                        if (i4 == 0) {
                            strArr = this.PREFIXES_16;
                            i6 = 16;
                            str = "xxxx xxxx xxxx xxxx";
                        } else if (i4 != 1) {
                            strArr = this.PREFIXES_14;
                            i6 = 14;
                            str = "xxxx xxxx xxxx xx";
                        } else {
                            strArr = this.PREFIXES_15;
                            i6 = 15;
                            str = "xxxx xxxx xxxx xxx";
                        }
                        int i7 = 0;
                        while (i7 < strArr.length) {
                            String str2 = strArr[i7];
                            if (stringBuilder2.length() <= str2.length()) {
                                if (!str2.startsWith(stringBuilder2)) {
                                    i7++;
                                }
                            } else if (!stringBuilder2.startsWith(str2)) {
                                i7++;
                            }
                            i5 = i6;
                            substring = str;
                        }
                        if (substring != null) {
                            break;
                        }
                        i4++;
                        i = 3;
                    }
                    obj = substring;
                    i2 = i5;
                    if (i2 != 0 && stringBuilder.length() > i2) {
                        stringBuilder.setLength(i2);
                    }
                }
                String str3 = Theme.key_windowBackgroundWhiteBlackText;
                if (obj != null) {
                    if (i2 != 0 && stringBuilder.length() == i2) {
                        PaymentFormActivity.this.inputFields[1].requestFocus();
                    }
                    editText.setTextColor(Theme.getColor(str3));
                    int i8 = 0;
                    while (i8 < stringBuilder.length()) {
                        if (i8 < obj.length()) {
                            if (obj.charAt(i8) == ' ') {
                                stringBuilder.insert(i8, ' ');
                                i8++;
                                if (selectionStart == i8) {
                                    i = this.characterAction;
                                    if (!(i == 2 || i == 3)) {
                                        selectionStart++;
                                    }
                                }
                            }
                            i8++;
                        } else {
                            stringBuilder.insert(i8, ' ');
                            if (selectionStart == i8 + 1) {
                                i8 = this.characterAction;
                                if (!(i8 == 2 || i8 == 3)) {
                                    selectionStart++;
                                }
                            }
                        }
                    }
                } else {
                    if (stringBuilder.length() > 0) {
                        str3 = Theme.key_windowBackgroundWhiteRedText4;
                    }
                    editText.setTextColor(Theme.getColor(str3));
                }
                if (!stringBuilder.toString().equals(editable.toString())) {
                    editable.replace(0, editable.length(), stringBuilder);
                }
                if (selectionStart >= 0) {
                    if (selectionStart > editText.length()) {
                        selectionStart = editText.length();
                    }
                    editText.setSelection(selectionStart);
                }
                PaymentFormActivity.this.ignoreOnCardChange = false;
            }
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$8 */
    class C31478 implements TextWatcher {
        private int actionPosition;
        private int characterAction = -1;
        private boolean isYear;

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C31478() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            boolean z = false;
            if (i2 == 0 && i3 == 1) {
                if (TextUtils.indexOf(PaymentFormActivity.this.inputFields[1].getText(), '/') != -1) {
                    z = true;
                }
                this.isYear = z;
                this.characterAction = 1;
            } else if (i2 != 1 || i3 != 0) {
                this.characterAction = -1;
            } else if (charSequence.charAt(i) != '/' || i <= 0) {
                this.characterAction = 2;
            } else {
                this.isYear = false;
                this.characterAction = 3;
                this.actionPosition = i - 1;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:74:0x0199  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x0170  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x0184  */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x017e  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x0199  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x0170  */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x017e  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x0184  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x0199  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x0170  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x0184  */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x017e  */
        /* JADX WARNING: Removed duplicated region for block: B:74:0x0199  */
        public void afterTextChanged(android.text.Editable r13) {
            /*
            r12 = this;
            r13 = org.telegram.p004ui.PaymentFormActivity.this;
            r13 = r13.ignoreOnCardChange;
            if (r13 == 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r13 = org.telegram.p004ui.PaymentFormActivity.this;
            r13 = r13.inputFields;
            r0 = 1;
            r13 = r13[r0];
            r1 = r13.getSelectionStart();
            r2 = r13.getText();
            r2 = r2.toString();
            r3 = r12.characterAction;
            r4 = 3;
            r5 = 0;
            if (r3 != r4) goto L_0x0042;
        L_0x0024:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r6 = r12.actionPosition;
            r6 = r2.substring(r5, r6);
            r3.append(r6);
            r6 = r12.actionPosition;
            r6 = r6 + r0;
            r2 = r2.substring(r6);
            r3.append(r2);
            r2 = r3.toString();
            r1 = r1 + -1;
        L_0x0042:
            r3 = new java.lang.StringBuilder;
            r6 = r2.length();
            r3.<init>(r6);
            r6 = 0;
        L_0x004c:
            r7 = r2.length();
            if (r6 >= r7) goto L_0x0065;
        L_0x0052:
            r7 = r6 + 1;
            r6 = r2.substring(r6, r7);
            r8 = "0123456789";
            r8 = r8.contains(r6);
            if (r8 == 0) goto L_0x0063;
        L_0x0060:
            r3.append(r6);
        L_0x0063:
            r6 = r7;
            goto L_0x004c;
        L_0x0065:
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2.ignoreOnCardChange = r0;
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r2 = r2[r0];
            r6 = "windowBackgroundWhiteBlackText";
            r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
            r2.setTextColor(r6);
            r2 = r3.length();
            r6 = 4;
            if (r2 <= r6) goto L_0x0085;
        L_0x0082:
            r3.setLength(r6);
        L_0x0085:
            r2 = r3.length();
            r7 = 2;
            if (r2 >= r7) goto L_0x008e;
        L_0x008c:
            r12.isYear = r5;
        L_0x008e:
            r2 = r12.isYear;
            r8 = 12;
            r9 = "windowBackgroundWhiteRedText4";
            if (r2 == 0) goto L_0x0110;
        L_0x0096:
            r2 = r3.length();
            if (r2 <= r7) goto L_0x009e;
        L_0x009c:
            r2 = 2;
            goto L_0x009f;
        L_0x009e:
            r2 = 1;
        L_0x009f:
            r2 = new java.lang.String[r2];
            r10 = r3.substring(r5, r7);
            r2[r5] = r10;
            r10 = r2.length;
            if (r10 != r7) goto L_0x00b0;
        L_0x00aa:
            r10 = r3.substring(r7);
            r2[r0] = r10;
        L_0x00b0:
            r10 = r3.length();
            if (r10 != r6) goto L_0x00f2;
        L_0x00b6:
            r10 = r2.length;
            if (r10 != r7) goto L_0x00f2;
        L_0x00b9:
            r8 = r2[r5];
            r8 = org.telegram.messenger.Utilities.parseInt(r8);
            r8 = r8.intValue();
            r2 = r2[r0];
            r2 = org.telegram.messenger.Utilities.parseInt(r2);
            r2 = r2.intValue();
            r2 = r2 + 2000;
            r10 = java.util.Calendar.getInstance();
            r11 = r10.get(r0);
            r10 = r10.get(r7);
            r10 = r10 + r0;
            if (r2 < r11) goto L_0x00e2;
        L_0x00de:
            if (r2 != r11) goto L_0x0159;
        L_0x00e0:
            if (r8 >= r10) goto L_0x0159;
        L_0x00e2:
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r2 = r2[r0];
            r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);
            r2.setTextColor(r8);
            goto L_0x015a;
        L_0x00f2:
            r2 = r2[r5];
            r2 = org.telegram.messenger.Utilities.parseInt(r2);
            r2 = r2.intValue();
            if (r2 > r8) goto L_0x0100;
        L_0x00fe:
            if (r2 != 0) goto L_0x0159;
        L_0x0100:
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r2 = r2[r0];
            r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);
            r2.setTextColor(r8);
            goto L_0x015a;
        L_0x0110:
            r2 = r3.length();
            if (r2 != r0) goto L_0x012e;
        L_0x0116:
            r2 = r3.toString();
            r2 = org.telegram.messenger.Utilities.parseInt(r2);
            r2 = r2.intValue();
            if (r2 == r0) goto L_0x0159;
        L_0x0124:
            if (r2 == 0) goto L_0x0159;
        L_0x0126:
            r0 = "0";
            r3.insert(r5, r0);
            r1 = r1 + 1;
            goto L_0x0159;
        L_0x012e:
            r2 = r3.length();
            if (r2 != r7) goto L_0x0159;
        L_0x0134:
            r2 = r3.toString();
            r2 = org.telegram.messenger.Utilities.parseInt(r2);
            r2 = r2.intValue();
            if (r2 > r8) goto L_0x0147;
        L_0x0142:
            if (r2 != 0) goto L_0x0145;
        L_0x0144:
            goto L_0x0147;
        L_0x0145:
            r0 = 0;
            goto L_0x0156;
        L_0x0147:
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.inputFields;
            r2 = r2[r0];
            r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);
            r2.setTextColor(r8);
        L_0x0156:
            r1 = r1 + 1;
            goto L_0x015a;
        L_0x0159:
            r0 = 0;
        L_0x015a:
            if (r0 != 0) goto L_0x0176;
        L_0x015c:
            r0 = r3.length();
            if (r0 != r6) goto L_0x0176;
        L_0x0162:
            r0 = org.telegram.p004ui.PaymentFormActivity.this;
            r0 = r0.inputFields;
            r2 = org.telegram.p004ui.PaymentFormActivity.this;
            r2 = r2.need_card_name;
            if (r2 == 0) goto L_0x0171;
        L_0x0170:
            r4 = 2;
        L_0x0171:
            r0 = r0[r4];
            r0.requestFocus();
        L_0x0176:
            r0 = r3.length();
            r2 = 47;
            if (r0 != r7) goto L_0x0184;
        L_0x017e:
            r3.append(r2);
        L_0x0181:
            r1 = r1 + 1;
            goto L_0x0194;
        L_0x0184:
            r0 = r3.length();
            if (r0 <= r7) goto L_0x0194;
        L_0x018a:
            r0 = r3.charAt(r7);
            if (r0 == r2) goto L_0x0194;
        L_0x0190:
            r3.insert(r7, r2);
            goto L_0x0181;
        L_0x0194:
            r13.setText(r3);
            if (r1 < 0) goto L_0x01a7;
        L_0x0199:
            r0 = r13.length();
            if (r1 > r0) goto L_0x01a0;
        L_0x019f:
            goto L_0x01a4;
        L_0x01a0:
            r1 = r13.length();
        L_0x01a4:
            r13.setSelection(r1);
        L_0x01a7:
            r13 = org.telegram.p004ui.PaymentFormActivity.this;
            r13.ignoreOnCardChange = r5;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PaymentFormActivity$C31478.afterTextChanged(android.text.Editable):void");
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$LinkSpan */
    public class LinkSpan extends ClickableSpan {
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }

        public void onClick(View view) {
            PaymentFormActivity.this.presentFragment(new TwoStepVerificationActivity(0));
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$PaymentFormActivityDelegate */
    private interface PaymentFormActivityDelegate {
        void currentPasswordUpdated(TL_account_password tL_account_password);

        boolean didSelectNewCard(String str, String str2, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay);

        void onFragmentDestroyed();
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$TelegramWebviewProxy */
    private class TelegramWebviewProxy {
        private TelegramWebviewProxy() {
        }

        /* synthetic */ TelegramWebviewProxy(PaymentFormActivity paymentFormActivity, C42621 c42621) {
            this();
        }

        @JavascriptInterface
        public void postEvent(String str, String str2) {
            AndroidUtilities.runOnUIThread(new C1807xb90c3c14(this, str, str2));
        }

        public /* synthetic */ void lambda$postEvent$0$PaymentFormActivity$TelegramWebviewProxy(String str, String str2) {
            if (PaymentFormActivity.this.getParentActivity() != null && str.equals("payment_form_submit")) {
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    PaymentFormActivity.this.paymentJson = jSONObject.getJSONObject("credentials").toString();
                    PaymentFormActivity.this.cardName = jSONObject.getString("title");
                } catch (Throwable th) {
                    PaymentFormActivity.this.paymentJson = str2;
                    FileLog.m30e(th);
                }
                PaymentFormActivity.this.goToNextStep();
            }
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$12 */
    class C425912 implements PaymentFormActivityDelegate {
        public void currentPasswordUpdated(TL_account_password tL_account_password) {
        }

        public void onFragmentDestroyed() {
        }

        C425912() {
        }

        public boolean didSelectNewCard(String str, String str2, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay) {
            PaymentFormActivity.this.paymentForm.saved_credentials = null;
            PaymentFormActivity.this.paymentJson = str;
            PaymentFormActivity.this.saveCardInfo = z;
            PaymentFormActivity.this.cardName = str2;
            PaymentFormActivity.this.androidPayCredentials = tL_inputPaymentCredentialsAndroidPay;
            PaymentFormActivity.this.detailSettingsCell[0].setTextAndValue(PaymentFormActivity.this.cardName, LocaleController.getString("PaymentCheckoutMethod", C1067R.string.PaymentCheckoutMethod), true);
            return false;
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$17 */
    class C426017 implements PaymentFormActivityDelegate {
        C426017() {
        }

        public boolean didSelectNewCard(String str, String str2, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay) {
            if (PaymentFormActivity.this.delegate != null) {
                PaymentFormActivity.this.delegate.didSelectNewCard(str, str2, z, tL_inputPaymentCredentialsAndroidPay);
            }
            if (PaymentFormActivity.this.isWebView) {
                PaymentFormActivity.this.removeSelfFromStack();
            }
            return PaymentFormActivity.this.delegate != null;
        }

        public void onFragmentDestroyed() {
            PaymentFormActivity.this.passwordFragment = null;
        }

        public void currentPasswordUpdated(TL_account_password tL_account_password) {
            PaymentFormActivity.this.currentPassword = tL_account_password;
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$18 */
    class C426118 implements TokenCallback {
        C426118() {
        }

        public void onSuccess(Token token) {
            if (!PaymentFormActivity.this.canceled) {
                PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", new Object[]{token.getType(), token.getId()});
                AndroidUtilities.runOnUIThread(new C1792-$$Lambda$PaymentFormActivity$18$QrVnMuLxiMtm_DJ7v5npJu9dxvA(this));
            }
        }

        public /* synthetic */ void lambda$onSuccess$0$PaymentFormActivity$18() {
            PaymentFormActivity.this.goToNextStep();
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.setDonePressed(false);
        }

        public void onError(Exception exception) {
            if (!PaymentFormActivity.this.canceled) {
                PaymentFormActivity.this.showEditDoneProgress(true, false);
                PaymentFormActivity.this.setDonePressed(false);
                if ((exception instanceof APIConnectionException) || (exception instanceof APIException)) {
                    AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", C1067R.string.PaymentConnectionFailed));
                } else {
                    AlertsCreator.showSimpleToast(PaymentFormActivity.this, exception.getMessage());
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$1 */
    class C42621 extends ActionBarMenuOnItemClick {
        C42621() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                if (!PaymentFormActivity.this.donePressed) {
                    PaymentFormActivity.this.finishFragment();
                }
            } else if (i == 1 && !PaymentFormActivity.this.donePressed) {
                if (PaymentFormActivity.this.currentStep != 3) {
                    AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
                }
                if (PaymentFormActivity.this.currentStep == 0) {
                    PaymentFormActivity.this.setDonePressed(true);
                    PaymentFormActivity.this.sendForm();
                } else {
                    int i2 = 0;
                    if (PaymentFormActivity.this.currentStep == 1) {
                        while (i2 < PaymentFormActivity.this.radioCells.length) {
                            if (PaymentFormActivity.this.radioCells[i2].isChecked()) {
                                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                paymentFormActivity.shippingOption = (TL_shippingOption) paymentFormActivity.requestedInfo.shipping_options.get(i2);
                                break;
                            }
                            i2++;
                        }
                        PaymentFormActivity.this.goToNextStep();
                    } else if (PaymentFormActivity.this.currentStep == 2) {
                        PaymentFormActivity.this.sendCardData();
                    } else if (PaymentFormActivity.this.currentStep == 3) {
                        PaymentFormActivity.this.checkPassword();
                    } else if (PaymentFormActivity.this.currentStep == 6) {
                        PaymentFormActivity.this.sendSavePassword(false);
                    }
                }
            }
        }
    }

    private void initAndroidPay(Context context) {
    }

    static /* synthetic */ void lambda$null$17(TLObject tLObject, TL_error tL_error) {
    }

    static /* synthetic */ void lambda$null$34(TLObject tLObject, TL_error tL_error) {
    }

    public void onPause() {
    }

    public PaymentFormActivity(MessageObject messageObject, TL_payments_paymentReceipt tL_payments_paymentReceipt) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        this.currentStep = 5;
        this.paymentForm = new TL_payments_paymentForm();
        TL_payments_paymentForm tL_payments_paymentForm = this.paymentForm;
        tL_payments_paymentForm.bot_id = tL_payments_paymentReceipt.bot_id;
        tL_payments_paymentForm.invoice = tL_payments_paymentReceipt.invoice;
        tL_payments_paymentForm.provider_id = tL_payments_paymentReceipt.provider_id;
        tL_payments_paymentForm.users = tL_payments_paymentReceipt.users;
        this.shippingOption = tL_payments_paymentReceipt.shipping;
        this.messageObject = messageObject;
        this.botUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(tL_payments_paymentReceipt.bot_id));
        User user = this.botUser;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = messageObject.messageOwner.media.title;
        if (tL_payments_paymentReceipt.info != null) {
            this.validateRequest = new TL_payments_validateRequestedInfo();
            this.validateRequest.info = tL_payments_paymentReceipt.info;
        }
        this.cardName = tL_payments_paymentReceipt.credentials_title;
    }

    public PaymentFormActivity(TL_payments_paymentForm tL_payments_paymentForm, MessageObject messageObject) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        TL_invoice tL_invoice = tL_payments_paymentForm.invoice;
        int i = 0;
        if (!(tL_invoice.shipping_address_requested || tL_invoice.email_requested || tL_invoice.name_requested || tL_invoice.phone_requested)) {
            if (tL_payments_paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                i = UserConfig.getInstance(this.currentAccount).tmpPassword != null ? 4 : 3;
            } else {
                i = 2;
            }
        }
        init(tL_payments_paymentForm, messageObject, i, null, null, null, null, null, false, null);
    }

    private PaymentFormActivity(TL_payments_paymentForm tL_payments_paymentForm, MessageObject messageObject, int i, TL_payments_validatedRequestedInfo tL_payments_validatedRequestedInfo, TL_shippingOption tL_shippingOption, String str, String str2, TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        init(tL_payments_paymentForm, messageObject, i, tL_payments_validatedRequestedInfo, tL_shippingOption, str, str2, tL_payments_validateRequestedInfo, z, tL_inputPaymentCredentialsAndroidPay);
    }

    private void setCurrentPassword(TL_account_password tL_account_password) {
        if (!tL_account_password.has_password) {
            this.currentPassword = tL_account_password;
            tL_account_password = this.currentPassword;
            if (tL_account_password != null) {
                this.waitingForEmail = TextUtils.isEmpty(tL_account_password.email_unconfirmed_pattern) ^ 1;
            }
            updatePasswordFields();
        } else if (getParentActivity() != null) {
            goToNextStep();
        }
    }

    private void setDelegate(PaymentFormActivityDelegate paymentFormActivityDelegate) {
        this.delegate = paymentFormActivityDelegate;
    }

    private void init(TL_payments_paymentForm tL_payments_paymentForm, MessageObject messageObject, int i, TL_payments_validatedRequestedInfo tL_payments_validatedRequestedInfo, TL_shippingOption tL_shippingOption, String str, String str2, TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay) {
        this.currentStep = i;
        this.paymentJson = str;
        this.androidPayCredentials = tL_inputPaymentCredentialsAndroidPay;
        this.requestedInfo = tL_payments_validatedRequestedInfo;
        this.paymentForm = tL_payments_paymentForm;
        this.shippingOption = tL_shippingOption;
        this.messageObject = messageObject;
        this.saveCardInfo = z;
        boolean equals = "stripe".equals(this.paymentForm.native_provider);
        boolean z2 = true;
        this.isWebView = equals ^ 1;
        this.botUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(tL_payments_paymentForm.bot_id));
        User user = this.botUser;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = messageObject.messageOwner.media.title;
        this.validateRequest = tL_payments_validateRequestedInfo;
        this.saveShippingInfo = true;
        if (z) {
            this.saveCardInfo = z;
        } else {
            if (this.paymentForm.saved_credentials == null) {
                z2 = false;
            }
            this.saveCardInfo = z2;
        }
        if (str2 == null) {
            TL_paymentSavedCredentialsCard tL_paymentSavedCredentialsCard = tL_payments_paymentForm.saved_credentials;
            if (tL_paymentSavedCredentialsCard != null) {
                this.cardName = tL_paymentSavedCredentialsCard.title;
                return;
            }
            return;
        }
        this.cardName = str2;
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (VERSION.SDK_INT >= 23) {
            try {
                if ((this.currentStep == 2 || this.currentStep == 6) && !this.paymentForm.invoice.test) {
                    getParentActivity().getWindow().setFlags(MessagesController.UPDATE_MASK_CHAT, MessagesController.UPDATE_MASK_CHAT);
                } else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
                    getParentActivity().getWindow().clearFlags(MessagesController.UPDATE_MASK_CHAT);
                }
            } catch (Throwable th) {
                FileLog.m30e(th);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:258:0x0919  */
    /* JADX WARNING: Removed duplicated region for block: B:484:0x130c  */
    /* JADX WARNING: Removed duplicated region for block: B:496:0x13e5  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x13bd  */
    /* JADX WARNING: Removed duplicated region for block: B:500:0x1413  */
    /* JADX WARNING: Removed duplicated region for block: B:499:0x1411  */
    /* JADX WARNING: Removed duplicated region for block: B:504:0x1475  */
    /* JADX WARNING: Removed duplicated region for block: B:503:0x143e  */
    /* JADX WARNING: Missing exception handler attribute for start block: B:315:0x0ad0 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:319:0x0adb */
    /* JADX WARNING: Missing exception handler attribute for start block: B:307:0x0aba */
    /* JADX WARNING: Missing exception handler attribute for start block: B:285:0x0977 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:311:0x0ac5 */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Can't wrap try/catch for region: R(11:267|268|269|270|(1:272)|277|278|283|284|285|286) */
    /* JADX WARNING: Can't wrap try/catch for region: R(18:303|304|305|306|307|308|309|310|311|312|313|314|315|316|317|318|319|320) */
    /* JADX WARNING: Missing block: B:79:0x0320, code skipped:
            if (r7.email_requested == false) goto L_0x0311;
     */
    /* JADX WARNING: Missing block: B:321:0x0ae0, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:322:0x0ae1, code skipped:
            org.telegram.messenger.FileLog.m30e(r0);
     */
    /* JADX WARNING: Missing block: B:448:0x0f96, code skipped:
            if (r6.email_requested == false) goto L_0x0f87;
     */
    @android.annotation.SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public android.view.View createView(android.content.Context r29) {
        /*
        r28 = this;
        r1 = r28;
        r2 = r29;
        r0 = r1.currentStep;
        r3 = 6;
        r4 = 5;
        r5 = 4;
        r6 = 3;
        r7 = 2;
        r8 = 1;
        if (r0 != 0) goto L_0x001e;
    L_0x000e:
        r0 = r1.actionBar;
        r9 = 2131560395; // 0x7f0d07cb float:1.8746161E38 double:1.053130763E-314;
        r10 = "PaymentShippingInfo";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x001e:
        if (r0 != r8) goto L_0x0030;
    L_0x0020:
        r0 = r1.actionBar;
        r9 = 2131560396; // 0x7f0d07cc float:1.8746163E38 double:1.0531307637E-314;
        r10 = "PaymentShippingMethod";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x0030:
        if (r0 != r7) goto L_0x0042;
    L_0x0032:
        r0 = r1.actionBar;
        r9 = 2131560355; // 0x7f0d07a3 float:1.874608E38 double:1.0531307434E-314;
        r10 = "PaymentCardInfo";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x0042:
        if (r0 != r6) goto L_0x0054;
    L_0x0044:
        r0 = r1.actionBar;
        r9 = 2131560355; // 0x7f0d07a3 float:1.874608E38 double:1.0531307434E-314;
        r10 = "PaymentCardInfo";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x0054:
        if (r0 != r5) goto L_0x008d;
    L_0x0056:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.test;
        if (r0 == 0) goto L_0x007e;
    L_0x005e:
        r0 = r1.actionBar;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Test ";
        r9.append(r10);
        r10 = 2131560362; // 0x7f0d07aa float:1.8746094E38 double:1.053130747E-314;
        r11 = "PaymentCheckout";
        r10 = org.telegram.messenger.LocaleController.getString(r11, r10);
        r9.append(r10);
        r9 = r9.toString();
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x007e:
        r0 = r1.actionBar;
        r9 = 2131560362; // 0x7f0d07aa float:1.8746094E38 double:1.053130747E-314;
        r10 = "PaymentCheckout";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x008d:
        if (r0 != r4) goto L_0x00c6;
    L_0x008f:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.test;
        if (r0 == 0) goto L_0x00b7;
    L_0x0097:
        r0 = r1.actionBar;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Test ";
        r9.append(r10);
        r10 = 2131560388; // 0x7f0d07c4 float:1.8746147E38 double:1.0531307597E-314;
        r11 = "PaymentReceipt";
        r10 = org.telegram.messenger.LocaleController.getString(r11, r10);
        r9.append(r10);
        r9 = r9.toString();
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x00b7:
        r0 = r1.actionBar;
        r9 = 2131560388; // 0x7f0d07c4 float:1.8746147E38 double:1.0531307597E-314;
        r10 = "PaymentReceipt";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
        goto L_0x00d6;
    L_0x00c6:
        if (r0 != r3) goto L_0x00d6;
    L_0x00c8:
        r0 = r1.actionBar;
        r9 = 2131560377; // 0x7f0d07b9 float:1.8746125E38 double:1.0531307543E-314;
        r10 = "PaymentPassword";
        r9 = org.telegram.messenger.LocaleController.getString(r10, r9);
        r0.setTitle(r9);
    L_0x00d6:
        r0 = r1.actionBar;
        r9 = 2131165409; // 0x7f0700e1 float:1.7945034E38 double:1.052935614E-314;
        r0.setBackButtonImage(r9);
        r0 = r1.actionBar;
        r0.setAllowOverlayTitle(r8);
        r0 = r1.actionBar;
        r9 = new org.telegram.ui.PaymentFormActivity$1;
        r9.<init>();
        r0.setActionBarMenuOnItemClick(r9);
        r0 = r1.actionBar;
        r0 = r0.createMenu();
        r9 = r1.currentStep;
        r10 = -1;
        if (r9 == 0) goto L_0x0102;
    L_0x00f8:
        if (r9 == r8) goto L_0x0102;
    L_0x00fa:
        if (r9 == r7) goto L_0x0102;
    L_0x00fc:
        if (r9 == r6) goto L_0x0102;
    L_0x00fe:
        if (r9 == r5) goto L_0x0102;
    L_0x0100:
        if (r9 != r3) goto L_0x013d;
    L_0x0102:
        r9 = 2131165439; // 0x7f0700ff float:1.7945095E38 double:1.052935629E-314;
        r11 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r0 = r0.addItemWithWidth(r8, r9, r11);
        r1.doneItem = r0;
        r0 = new org.telegram.ui.Components.ContextProgressView;
        r0.<init>(r2, r8);
        r1.progressView = r0;
        r0 = r1.progressView;
        r9 = 0;
        r0.setAlpha(r9);
        r0 = r1.progressView;
        r9 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
        r0.setScaleX(r9);
        r0 = r1.progressView;
        r0.setScaleY(r9);
        r0 = r1.progressView;
        r0.setVisibility(r5);
        r0 = r1.doneItem;
        r9 = r1.progressView;
        r11 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r11 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r10, r11);
        r0.addView(r9, r11);
    L_0x013d:
        r0 = new android.widget.FrameLayout;
        r0.<init>(r2);
        r1.fragmentView = r0;
        r0 = r1.fragmentView;
        r9 = r0;
        r9 = (android.widget.FrameLayout) r9;
        r11 = "windowBackgroundGray";
        r11 = org.telegram.p004ui.ActionBar.Theme.getColor(r11);
        r0.setBackgroundColor(r11);
        r0 = new android.widget.ScrollView;
        r0.<init>(r2);
        r1.scrollView = r0;
        r0 = r1.scrollView;
        r0.setFillViewport(r8);
        r0 = r1.scrollView;
        r11 = "actionBarDefault";
        r11 = org.telegram.p004ui.ActionBar.Theme.getColor(r11);
        org.telegram.messenger.AndroidUtilities.setScrollViewEdgeEffectColor(r0, r11);
        r0 = r1.scrollView;
        r11 = -1;
        r12 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r13 = 51;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r4 = r1.currentStep;
        if (r4 != r5) goto L_0x017d;
    L_0x0178:
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r17 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        goto L_0x0180;
    L_0x017d:
        r4 = 0;
        r17 = 0;
    L_0x0180:
        r4 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r11, r12, r13, r14, r15, r16, r17);
        r9.addView(r0, r4);
        r0 = new android.widget.LinearLayout;
        r0.<init>(r2);
        r1.linearLayout2 = r0;
        r0 = r1.linearLayout2;
        r0.setOrientation(r8);
        r0 = r1.scrollView;
        r4 = r1.linearLayout2;
        r11 = new android.widget.FrameLayout$LayoutParams;
        r12 = -2;
        r11.<init>(r10, r12);
        r0.addView(r4, r11);
        r0 = r1.currentStep;
        r13 = "windowBackgroundWhiteBlackText";
        r14 = "windowBackgroundGrayShadow";
        r15 = "windowBackgroundWhite";
        r5 = 0;
        if (r0 != 0) goto L_0x093d;
    L_0x01ab:
        r9 = new java.util.HashMap;
        r9.<init>();
        r11 = new java.util.HashMap;
        r11.<init>();
        r0 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0212 }
        r4 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0212 }
        r18 = r29.getResources();	 Catch:{ Exception -> 0x0212 }
        r3 = r18.getAssets();	 Catch:{ Exception -> 0x0212 }
        r10 = "countries.txt";
        r3 = r3.open(r10);	 Catch:{ Exception -> 0x0212 }
        r4.<init>(r3);	 Catch:{ Exception -> 0x0212 }
        r0.<init>(r4);	 Catch:{ Exception -> 0x0212 }
    L_0x01cd:
        r3 = r0.readLine();	 Catch:{ Exception -> 0x0212 }
        if (r3 == 0) goto L_0x020e;
    L_0x01d3:
        r4 = ";";
        r3 = r3.split(r4);	 Catch:{ Exception -> 0x0212 }
        r4 = r1.countriesArray;	 Catch:{ Exception -> 0x0212 }
        r10 = r3[r7];	 Catch:{ Exception -> 0x0212 }
        r4.add(r5, r10);	 Catch:{ Exception -> 0x0212 }
        r4 = r1.countriesMap;	 Catch:{ Exception -> 0x0212 }
        r10 = r3[r7];	 Catch:{ Exception -> 0x0212 }
        r12 = r3[r5];	 Catch:{ Exception -> 0x0212 }
        r4.put(r10, r12);	 Catch:{ Exception -> 0x0212 }
        r4 = r1.codesMap;	 Catch:{ Exception -> 0x0212 }
        r10 = r3[r5];	 Catch:{ Exception -> 0x0212 }
        r12 = r3[r7];	 Catch:{ Exception -> 0x0212 }
        r4.put(r10, r12);	 Catch:{ Exception -> 0x0212 }
        r4 = r3[r8];	 Catch:{ Exception -> 0x0212 }
        r10 = r3[r7];	 Catch:{ Exception -> 0x0212 }
        r11.put(r4, r10);	 Catch:{ Exception -> 0x0212 }
        r4 = r3.length;	 Catch:{ Exception -> 0x0212 }
        if (r4 <= r6) goto L_0x0205;
    L_0x01fc:
        r4 = r1.phoneFormatMap;	 Catch:{ Exception -> 0x0212 }
        r10 = r3[r5];	 Catch:{ Exception -> 0x0212 }
        r12 = r3[r6];	 Catch:{ Exception -> 0x0212 }
        r4.put(r10, r12);	 Catch:{ Exception -> 0x0212 }
    L_0x0205:
        r4 = r3[r8];	 Catch:{ Exception -> 0x0212 }
        r3 = r3[r7];	 Catch:{ Exception -> 0x0212 }
        r9.put(r4, r3);	 Catch:{ Exception -> 0x0212 }
        r12 = -2;
        goto L_0x01cd;
    L_0x020e:
        r0.close();	 Catch:{ Exception -> 0x0212 }
        goto L_0x0216;
    L_0x0212:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0216:
        r0 = r1.countriesArray;
        r3 = org.telegram.p004ui.C2079-$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE;
        java.util.Collections.sort(r0, r3);
        r0 = 10;
        r0 = new org.telegram.p004ui.Components.EditTextBoldCursor[r0];
        r1.inputFields = r0;
        r0 = 0;
    L_0x0224:
        r3 = 10;
        if (r0 >= r3) goto L_0x0771;
    L_0x0228:
        if (r0 != 0) goto L_0x0261;
    L_0x022a:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = 2131560389; // 0x7f0d07c5 float:1.8746149E38 double:1.05313076E-314;
        r10 = "PaymentShippingAddress";
        r4 = org.telegram.messenger.LocaleController.getString(r10, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r5];
        r10 = -2;
        r12 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r12, r10);
        r3.addView(r4, r7);
        r3 = 8;
        r10 = -1;
        goto L_0x02b3;
    L_0x0261:
        r3 = 6;
        r10 = -2;
        r12 = -1;
        if (r0 != r3) goto L_0x02b0;
    L_0x0266:
        r3 = r1.sectionCell;
        r4 = new org.telegram.ui.Cells.ShadowSectionCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.linearLayout2;
        r4 = r1.sectionCell;
        r4 = r4[r5];
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r12, r10);
        r3.addView(r4, r7);
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = 2131560399; // 0x7f0d07cf float:1.874617E38 double:1.053130765E-314;
        r7 = "PaymentShippingReceiver";
        r4 = org.telegram.messenger.LocaleController.getString(r7, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r8];
        r7 = -2;
        r10 = -1;
        r12 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r7);
        r3.addView(r4, r12);
        goto L_0x02b1;
    L_0x02b0:
        r10 = -1;
    L_0x02b1:
        r3 = 8;
    L_0x02b3:
        if (r0 != r3) goto L_0x02d1;
    L_0x02b5:
        r3 = new android.widget.LinearLayout;
        r3.<init>(r2);
        r3.setOrientation(r5);
        r4 = r1.linearLayout2;
        r7 = 50;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r7);
        r4.addView(r3, r7);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        goto L_0x0341;
    L_0x02d1:
        r3 = 9;
        if (r0 != r3) goto L_0x02e2;
    L_0x02d5:
        r3 = r1.inputFields;
        r4 = 8;
        r3 = r3[r4];
        r3 = r3.getParent();
        r3 = (android.view.ViewGroup) r3;
        goto L_0x0341;
    L_0x02e2:
        r3 = new android.widget.FrameLayout;
        r3.<init>(r2);
        r4 = r1.linearLayout2;
        r7 = 50;
        r10 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r7);
        r4.addView(r3, r7);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r4 = 5;
        if (r0 == r4) goto L_0x0303;
    L_0x02fd:
        r4 = 9;
        if (r0 == r4) goto L_0x0303;
    L_0x0301:
        r4 = 1;
        goto L_0x0304;
    L_0x0303:
        r4 = 0;
    L_0x0304:
        if (r4 == 0) goto L_0x0323;
    L_0x0306:
        r7 = 7;
        if (r0 != r7) goto L_0x0313;
    L_0x0309:
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.phone_requested;
        if (r7 != 0) goto L_0x0313;
    L_0x0311:
        r4 = 0;
        goto L_0x0323;
    L_0x0313:
        r7 = 6;
        if (r0 != r7) goto L_0x0323;
    L_0x0316:
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r10 = r7.phone_requested;
        if (r10 != 0) goto L_0x0323;
    L_0x031e:
        r7 = r7.email_requested;
        if (r7 != 0) goto L_0x0323;
    L_0x0322:
        goto L_0x0311;
    L_0x0323:
        if (r4 == 0) goto L_0x0341;
    L_0x0325:
        r4 = new org.telegram.ui.PaymentFormActivity$2;
        r4.<init>(r2);
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r4.setBackgroundColor(r7);
        r7 = r1.dividers;
        r7.add(r4);
        r7 = new android.widget.FrameLayout$LayoutParams;
        r10 = 83;
        r12 = -1;
        r7.<init>(r12, r8, r10);
        r3.addView(r4, r7);
    L_0x0341:
        r4 = 9;
        if (r0 != r4) goto L_0x034f;
    L_0x0345:
        r4 = r1.inputFields;
        r7 = new org.telegram.ui.Components.HintEditText;
        r7.<init>(r2);
        r4[r0] = r7;
        goto L_0x0358;
    L_0x034f:
        r4 = r1.inputFields;
        r7 = new org.telegram.ui.Components.EditTextBoldCursor;
        r7.<init>(r2);
        r4[r0] = r7;
    L_0x0358:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = java.lang.Integer.valueOf(r0);
        r4.setTag(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r4.setTextSize(r8, r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = "windowBackgroundWhiteHintText";
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r7);
        r4.setHintTextColor(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setTextColor(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 0;
        r4.setBackgroundDrawable(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setCursorColor(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r4.setCursorSize(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r4.setCursorWidth(r7);
        r4 = 4;
        if (r0 != r4) goto L_0x03c3;
    L_0x03b0:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$uDw-faglZkNLuTGLVBNZMQdnOLU;
        r7.<init>(r1);
        r4.setOnTouchListener(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.setInputType(r5);
    L_0x03c3:
        r4 = 9;
        if (r0 == r4) goto L_0x03e1;
    L_0x03c7:
        r4 = 8;
        if (r0 != r4) goto L_0x03cc;
    L_0x03cb:
        goto L_0x03e1;
    L_0x03cc:
        r4 = 7;
        if (r0 != r4) goto L_0x03d7;
    L_0x03cf:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.setInputType(r8);
        goto L_0x03e8;
    L_0x03d7:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 16385; // 0x4001 float:2.296E-41 double:8.0953E-320;
        r4.setInputType(r7);
        goto L_0x03e8;
    L_0x03e1:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.setInputType(r6);
    L_0x03e8:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r4.setImeOptions(r7);
        switch(r0) {
            case 0: goto L_0x0508;
            case 1: goto L_0x04e4;
            case 2: goto L_0x04c0;
            case 3: goto L_0x049c;
            case 4: goto L_0x0462;
            case 5: goto L_0x043d;
            case 6: goto L_0x041a;
            case 7: goto L_0x03f7;
            default: goto L_0x03f5;
        };
    L_0x03f5:
        goto L_0x052b;
    L_0x03f7:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560394; // 0x7f0d07ca float:1.874616E38 double:1.0531307627E-314;
        r10 = "PaymentShippingEmailPlaceholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x040d:
        r4 = r4.email;
        if (r4 == 0) goto L_0x052b;
    L_0x0411:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r7.setText(r4);
        goto L_0x052b;
    L_0x041a:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560397; // 0x7f0d07cd float:1.8746165E38 double:1.053130764E-314;
        r10 = "PaymentShippingName";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x0430:
        r4 = r4.name;
        if (r4 == 0) goto L_0x052b;
    L_0x0434:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r7.setText(r4);
        goto L_0x052b;
    L_0x043d:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560403; // 0x7f0d07d3 float:1.8746177E38 double:1.053130767E-314;
        r10 = "PaymentShippingZipPlaceholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x0453:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x0457:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r4 = r4.post_code;
        r7.setText(r4);
        goto L_0x052b;
    L_0x0462:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560393; // 0x7f0d07c9 float:1.8746157E38 double:1.053130762E-314;
        r10 = "PaymentShippingCountry";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x0478:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x047c:
        r4 = r4.country_iso2;
        r4 = r11.get(r4);
        r4 = (java.lang.String) r4;
        r7 = r1.paymentForm;
        r7 = r7.saved_info;
        r7 = r7.shipping_address;
        r7 = r7.country_iso2;
        r1.countryName = r7;
        r7 = r1.inputFields;
        r7 = r7[r0];
        if (r4 == 0) goto L_0x0495;
    L_0x0494:
        goto L_0x0497;
    L_0x0495:
        r4 = r1.countryName;
    L_0x0497:
        r7.setText(r4);
        goto L_0x052b;
    L_0x049c:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560402; // 0x7f0d07d2 float:1.8746175E38 double:1.0531307667E-314;
        r10 = "PaymentShippingStatePlaceholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x04b2:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x04b6:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r4 = r4.state;
        r7.setText(r4);
        goto L_0x052b;
    L_0x04c0:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560392; // 0x7f0d07c8 float:1.8746155E38 double:1.0531307617E-314;
        r10 = "PaymentShippingCityPlaceholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x04d6:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x04da:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r4 = r4.city;
        r7.setText(r4);
        goto L_0x052b;
    L_0x04e4:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560391; // 0x7f0d07c7 float:1.8746153E38 double:1.053130761E-314;
        r10 = "PaymentShippingAddress2Placeholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x04fa:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x04fe:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r4 = r4.street_line2;
        r7.setText(r4);
        goto L_0x052b;
    L_0x0508:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 2131560390; // 0x7f0d07c6 float:1.874615E38 double:1.0531307607E-314;
        r10 = "PaymentShippingAddress1Placeholder";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r4.setHint(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x052b;
    L_0x051e:
        r4 = r4.shipping_address;
        if (r4 == 0) goto L_0x052b;
    L_0x0522:
        r7 = r1.inputFields;
        r7 = r7[r0];
        r4 = r4.street_line1;
        r7.setText(r4);
    L_0x052b:
        r4 = r1.inputFields;
        r7 = r4[r0];
        r4 = r4[r0];
        r4 = r4.length();
        r7.setSelection(r4);
        r4 = 8;
        if (r0 != r4) goto L_0x05b4;
    L_0x053c:
        r4 = new android.widget.TextView;
        r4.<init>(r2);
        r1.textView = r4;
        r4 = r1.textView;
        r7 = "+";
        r4.setText(r7);
        r4 = r1.textView;
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setTextColor(r7);
        r4 = r1.textView;
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r4.setTextSize(r8, r7);
        r4 = r1.textView;
        r20 = -2;
        r21 = -2;
        r22 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r23 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r24 = 0;
        r25 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r20, r21, r22, r23, r24, r25);
        r3.addView(r4, r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r4.setPadding(r7, r5, r5, r5);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 19;
        r4.setGravity(r7);
        r4 = new android.text.InputFilter[r8];
        r7 = new android.text.InputFilter$LengthFilter;
        r10 = 5;
        r7.<init>(r10);
        r4[r5] = r7;
        r7 = r1.inputFields;
        r7 = r7[r0];
        r7.setFilters(r4);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r20 = 55;
        r22 = 0;
        r24 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r20, r21, r22, r23, r24, r25);
        r3.addView(r4, r7);
        r3 = r1.inputFields;
        r3 = r3[r0];
        r4 = new org.telegram.ui.PaymentFormActivity$3;
        r4.<init>();
        r3.addTextChangedListener(r4);
        goto L_0x0620;
    L_0x05b4:
        r4 = 9;
        if (r0 != r4) goto L_0x05ec;
    L_0x05b8:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.setPadding(r5, r5, r5, r5);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 19;
        r4.setGravity(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r20 = -1;
        r21 = -2;
        r22 = 0;
        r23 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r24 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r25 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r20, r21, r22, r23, r24, r25);
        r3.addView(r4, r7);
        r3 = r1.inputFields;
        r3 = r3[r0];
        r4 = new org.telegram.ui.PaymentFormActivity$4;
        r4.<init>();
        r3.addTextChangedListener(r4);
        goto L_0x0620;
    L_0x05ec:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r4.setPadding(r5, r5, r5, r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = org.telegram.messenger.LocaleController.isRTL;
        if (r7 == 0) goto L_0x0603;
    L_0x0601:
        r7 = 5;
        goto L_0x0604;
    L_0x0603:
        r7 = 3;
    L_0x0604:
        r4.setGravity(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r20 = -1;
        r21 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r22 = 51;
        r23 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r24 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r25 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r26 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r20, r21, r22, r23, r24, r25, r26);
        r3.addView(r4, r7);
    L_0x0620:
        r3 = r1.inputFields;
        r3 = r3[r0];
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY;
        r4.<init>(r1);
        r3.setOnEditorActionListener(r4);
        r3 = 9;
        if (r0 != r3) goto L_0x076b;
    L_0x0630:
        r3 = r1.paymentForm;
        r3 = r3.invoice;
        r4 = r3.email_to_provider;
        if (r4 != 0) goto L_0x0657;
    L_0x0638:
        r3 = r3.phone_to_provider;
        if (r3 == 0) goto L_0x063d;
    L_0x063c:
        goto L_0x0657;
    L_0x063d:
        r3 = r1.sectionCell;
        r4 = new org.telegram.ui.Cells.ShadowSectionCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.linearLayout2;
        r4 = r1.sectionCell;
        r4 = r4[r8];
        r7 = -2;
        r10 = -1;
        r12 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r7);
        r3.addView(r4, r12);
        goto L_0x06fe;
    L_0x0657:
        r3 = 0;
        r4 = 0;
    L_0x0659:
        r7 = r1.paymentForm;
        r7 = r7.users;
        r7 = r7.size();
        if (r3 >= r7) goto L_0x0679;
    L_0x0663:
        r7 = r1.paymentForm;
        r7 = r7.users;
        r7 = r7.get(r3);
        r7 = (org.telegram.tgnet.TLRPC.User) r7;
        r10 = r7.f534id;
        r12 = r1.paymentForm;
        r12 = r12.provider_id;
        if (r10 != r12) goto L_0x0676;
    L_0x0675:
        r4 = r7;
    L_0x0676:
        r3 = r3 + 1;
        goto L_0x0659;
    L_0x0679:
        if (r4 == 0) goto L_0x0684;
    L_0x067b:
        r3 = r4.first_name;
        r4 = r4.last_name;
        r3 = org.telegram.messenger.ContactsController.formatName(r3, r4);
        goto L_0x0686;
    L_0x0684:
        r3 = "";
    L_0x0686:
        r4 = r1.bottomCell;
        r7 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r7.<init>(r2);
        r4[r8] = r7;
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r7 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r10 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r7, r14);
        r4.setBackgroundDrawable(r10);
        r4 = r1.linearLayout2;
        r7 = r1.bottomCell;
        r7 = r7[r8];
        r10 = -2;
        r12 = -1;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r12, r10);
        r4.addView(r7, r6);
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r6 = r4.email_to_provider;
        if (r6 == 0) goto L_0x06cd;
    L_0x06b4:
        r4 = r4.phone_to_provider;
        if (r4 == 0) goto L_0x06cd;
    L_0x06b8:
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r6 = 2131560385; // 0x7f0d07c1 float:1.874614E38 double:1.0531307583E-314;
        r7 = new java.lang.Object[r8];
        r7[r5] = r3;
        r3 = "PaymentPhoneEmailToProvider";
        r3 = org.telegram.messenger.LocaleController.formatString(r3, r6, r7);
        r4.setText(r3);
        goto L_0x06fe;
    L_0x06cd:
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.email_to_provider;
        if (r4 == 0) goto L_0x06ea;
    L_0x06d5:
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r6 = 2131560373; // 0x7f0d07b5 float:1.8746116E38 double:1.0531307523E-314;
        r7 = new java.lang.Object[r8];
        r7[r5] = r3;
        r3 = "PaymentEmailToProvider";
        r3 = org.telegram.messenger.LocaleController.formatString(r3, r6, r7);
        r4.setText(r3);
        goto L_0x06fe;
    L_0x06ea:
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r6 = 2131560386; // 0x7f0d07c2 float:1.8746143E38 double:1.053130759E-314;
        r7 = new java.lang.Object[r8];
        r7[r5] = r3;
        r3 = "PaymentPhoneToProvider";
        r3 = org.telegram.messenger.LocaleController.formatString(r3, r6, r7);
        r4.setText(r3);
    L_0x06fe:
        r3 = new org.telegram.ui.Cells.TextCheckCell;
        r3.<init>(r2);
        r1.checkCell1 = r3;
        r3 = r1.checkCell1;
        r4 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r3.setBackgroundDrawable(r4);
        r3 = r1.checkCell1;
        r4 = 2131560400; // 0x7f0d07d0 float:1.8746171E38 double:1.0531307657E-314;
        r6 = "PaymentShippingSave";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r6 = r1.saveShippingInfo;
        r3.setTextAndCheck(r4, r6, r5);
        r3 = r1.linearLayout2;
        r4 = r1.checkCell1;
        r6 = -2;
        r7 = -1;
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r10);
        r3 = r1.checkCell1;
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$ja6Gof-oEgJFLBPAYcrRraILPks;
        r4.<init>(r1);
        r3.setOnClickListener(r4);
        r3 = r1.bottomCell;
        r4 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r4, r14);
        r3.setBackgroundDrawable(r6);
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131560401; // 0x7f0d07d1 float:1.8746173E38 double:1.053130766E-314;
        r6 = "PaymentShippingSaveInfo";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.bottomCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r10);
    L_0x076b:
        r0 = r0 + 1;
        r6 = 3;
        r7 = 2;
        goto L_0x0224;
    L_0x0771:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.name_requested;
        if (r0 != 0) goto L_0x078a;
    L_0x0779:
        r0 = r1.inputFields;
        r2 = 6;
        r0 = r0[r2];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r2 = 8;
        r0.setVisibility(r2);
        goto L_0x078c;
    L_0x078a:
        r2 = 8;
    L_0x078c:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.phone_requested;
        if (r0 != 0) goto L_0x07a1;
    L_0x0794:
        r0 = r1.inputFields;
        r0 = r0[r2];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
    L_0x07a1:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.email_requested;
        if (r0 != 0) goto L_0x07b9;
    L_0x07a9:
        r0 = r1.inputFields;
        r2 = 7;
        r0 = r0[r2];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r2 = 8;
        r0.setVisibility(r2);
    L_0x07b9:
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r2 = r0.phone_requested;
        if (r2 == 0) goto L_0x07ce;
    L_0x07c1:
        r0 = r1.inputFields;
        r2 = 9;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
        goto L_0x07f9;
    L_0x07ce:
        r2 = r0.email_requested;
        if (r2 == 0) goto L_0x07de;
    L_0x07d2:
        r0 = r1.inputFields;
        r2 = 7;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
        goto L_0x07f9;
    L_0x07de:
        r0 = r0.name_requested;
        if (r0 == 0) goto L_0x07ee;
    L_0x07e2:
        r0 = r1.inputFields;
        r2 = 6;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
        goto L_0x07f9;
    L_0x07ee:
        r0 = r1.inputFields;
        r2 = 5;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
    L_0x07f9:
        r0 = r1.sectionCell;
        r2 = r0[r8];
        if (r2 == 0) goto L_0x081a;
    L_0x07ff:
        r0 = r0[r8];
        r2 = r1.paymentForm;
        r2 = r2.invoice;
        r3 = r2.name_requested;
        if (r3 != 0) goto L_0x0815;
    L_0x0809:
        r3 = r2.phone_requested;
        if (r3 != 0) goto L_0x0815;
    L_0x080d:
        r2 = r2.email_requested;
        if (r2 == 0) goto L_0x0812;
    L_0x0811:
        goto L_0x0815;
    L_0x0812:
        r2 = 8;
        goto L_0x0816;
    L_0x0815:
        r2 = 0;
    L_0x0816:
        r0.setVisibility(r2);
        goto L_0x083a;
    L_0x081a:
        r0 = r1.bottomCell;
        r2 = r0[r8];
        if (r2 == 0) goto L_0x083a;
    L_0x0820:
        r0 = r0[r8];
        r2 = r1.paymentForm;
        r2 = r2.invoice;
        r3 = r2.name_requested;
        if (r3 != 0) goto L_0x0836;
    L_0x082a:
        r3 = r2.phone_requested;
        if (r3 != 0) goto L_0x0836;
    L_0x082e:
        r2 = r2.email_requested;
        if (r2 == 0) goto L_0x0833;
    L_0x0832:
        goto L_0x0836;
    L_0x0833:
        r2 = 8;
        goto L_0x0837;
    L_0x0836:
        r2 = 0;
    L_0x0837:
        r0.setVisibility(r2);
    L_0x083a:
        r0 = r1.headerCell;
        r0 = r0[r8];
        r2 = r1.paymentForm;
        r2 = r2.invoice;
        r3 = r2.name_requested;
        if (r3 != 0) goto L_0x0852;
    L_0x0846:
        r3 = r2.phone_requested;
        if (r3 != 0) goto L_0x0852;
    L_0x084a:
        r2 = r2.email_requested;
        if (r2 == 0) goto L_0x084f;
    L_0x084e:
        goto L_0x0852;
    L_0x084f:
        r2 = 8;
        goto L_0x0853;
    L_0x0852:
        r2 = 0;
    L_0x0853:
        r0.setVisibility(r2);
        r0 = r1.paymentForm;
        r0 = r0.invoice;
        r0 = r0.shipping_address_requested;
        if (r0 != 0) goto L_0x08c0;
    L_0x085e:
        r0 = r1.headerCell;
        r0 = r0[r5];
        r2 = 8;
        r0.setVisibility(r2);
        r0 = r1.sectionCell;
        r0 = r0[r5];
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r0 = r0[r5];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r0 = r0[r8];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r3 = 2;
        r0 = r0[r3];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r3 = 3;
        r0 = r0[r3];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r3 = 4;
        r0 = r0[r3];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
        r0 = r1.inputFields;
        r3 = 5;
        r0 = r0[r3];
        r0 = r0.getParent();
        r0 = (android.view.ViewGroup) r0;
        r0.setVisibility(r2);
    L_0x08c0:
        r0 = r1.paymentForm;
        r0 = r0.saved_info;
        if (r0 == 0) goto L_0x08d8;
    L_0x08c6:
        r0 = r0.phone;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x08d8;
    L_0x08ce:
        r0 = r1.paymentForm;
        r0 = r0.saved_info;
        r0 = r0.phone;
        r1.fillNumber(r0);
        goto L_0x08dc;
    L_0x08d8:
        r2 = 0;
        r1.fillNumber(r2);
    L_0x08dc:
        r0 = r1.inputFields;
        r2 = 8;
        r0 = r0[r2];
        r0 = r0.length();
        if (r0 != 0) goto L_0x18b9;
    L_0x08e8:
        r0 = r1.paymentForm;
        r2 = r0.invoice;
        r2 = r2.phone_requested;
        if (r2 == 0) goto L_0x18b9;
    L_0x08f0:
        r0 = r0.saved_info;
        if (r0 == 0) goto L_0x08fc;
    L_0x08f4:
        r0 = r0.phone;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x18b9;
    L_0x08fc:
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0912 }
        r2 = "phone";
        r0 = r0.getSystemService(r2);	 Catch:{ Exception -> 0x0912 }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x0912 }
        if (r0 == 0) goto L_0x0916;
    L_0x0908:
        r0 = r0.getSimCountryIso();	 Catch:{ Exception -> 0x0912 }
        r0 = r0.toUpperCase();	 Catch:{ Exception -> 0x0912 }
        r11 = r0;
        goto L_0x0917;
    L_0x0912:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0916:
        r11 = 0;
    L_0x0917:
        if (r11 == 0) goto L_0x18b9;
    L_0x0919:
        r0 = r9.get(r11);
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x18b9;
    L_0x0921:
        r2 = r1.countriesArray;
        r2 = r2.indexOf(r0);
        r3 = -1;
        if (r2 == r3) goto L_0x18b9;
    L_0x092a:
        r2 = r1.inputFields;
        r3 = 8;
        r2 = r2[r3];
        r3 = r1.countriesMap;
        r0 = r3.get(r0);
        r0 = (java.lang.CharSequence) r0;
        r2.setText(r0);
        goto L_0x18b9;
    L_0x093d:
        r3 = 2;
        if (r0 != r3) goto L_0x0e7a;
    L_0x0940:
        r0 = r1.paymentForm;
        r0 = r0.native_params;
        if (r0 == 0) goto L_0x097e;
    L_0x0946:
        r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x097a }
        r0 = r0.data;	 Catch:{ Exception -> 0x097a }
        r3.<init>(r0);	 Catch:{ Exception -> 0x097a }
        r0 = "android_pay_public_key";
        r0 = r3.getString(r0);	 Catch:{ Exception -> 0x095c }
        r4 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x095c }
        if (r4 != 0) goto L_0x095f;
    L_0x0959:
        r1.androidPayPublicKey = r0;	 Catch:{ Exception -> 0x095c }
        goto L_0x095f;
    L_0x095c:
        r4 = 0;
        r1.androidPayPublicKey = r4;	 Catch:{ Exception -> 0x097a }
    L_0x095f:
        r0 = "android_pay_bgcolor";
        r0 = r3.getInt(r0);	 Catch:{ Exception -> 0x096b }
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = r0 | r4;
        r1.androidPayBackgroundColor = r0;	 Catch:{ Exception -> 0x096b }
        goto L_0x096e;
    L_0x096b:
        r4 = -1;
        r1.androidPayBackgroundColor = r4;	 Catch:{ Exception -> 0x097a }
    L_0x096e:
        r0 = "android_pay_inverse";
        r0 = r3.getBoolean(r0);	 Catch:{ Exception -> 0x0977 }
        r1.androidPayBlackTheme = r0;	 Catch:{ Exception -> 0x0977 }
        goto L_0x097e;
    L_0x0977:
        r1.androidPayBlackTheme = r5;	 Catch:{ Exception -> 0x097a }
        goto L_0x097e;
    L_0x097a:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x097e:
        r0 = r1.isWebView;
        if (r0 == 0) goto L_0x0aa4;
    L_0x0982:
        r0 = r1.androidPayPublicKey;
        if (r0 == 0) goto L_0x0989;
    L_0x0986:
        r28.initAndroidPay(r29);
    L_0x0989:
        r0 = new android.widget.FrameLayout;
        r0.<init>(r2);
        r1.androidPayContainer = r0;
        r0 = r1.androidPayContainer;
        r3 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        r0.setId(r3);
        r0 = r1.androidPayContainer;
        r3 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r0.setBackgroundDrawable(r3);
        r0 = r1.androidPayContainer;
        r3 = 8;
        r0.setVisibility(r3);
        r0 = r1.linearLayout2;
        r3 = r1.androidPayContainer;
        r4 = 50;
        r6 = -1;
        r4 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r4);
        r1.webviewLoading = r8;
        r1.showEditDoneProgress(r8, r8);
        r0 = r1.progressView;
        r0.setVisibility(r5);
        r0 = r1.doneItem;
        r0.setEnabled(r5);
        r0 = r1.doneItem;
        r0 = r0.getImageView();
        r3 = 4;
        r0.setVisibility(r3);
        r0 = new org.telegram.ui.PaymentFormActivity$5;
        r0.<init>(r2);
        r1.webView = r0;
        r0 = r1.webView;
        r0 = r0.getSettings();
        r0.setJavaScriptEnabled(r8);
        r0 = r1.webView;
        r0 = r0.getSettings();
        r0.setDomStorageEnabled(r8);
        r0 = android.os.Build.VERSION.SDK_INT;
        r3 = 21;
        if (r0 < r3) goto L_0x09ff;
    L_0x09ed:
        r0 = r1.webView;
        r0 = r0.getSettings();
        r0.setMixedContentMode(r5);
        r0 = android.webkit.CookieManager.getInstance();
        r3 = r1.webView;
        r0.setAcceptThirdPartyCookies(r3, r8);
    L_0x09ff:
        r0 = android.os.Build.VERSION.SDK_INT;
        r3 = 17;
        if (r0 < r3) goto L_0x0a12;
    L_0x0a05:
        r0 = r1.webView;
        r3 = new org.telegram.ui.PaymentFormActivity$TelegramWebviewProxy;
        r4 = 0;
        r3.<init>(r1, r4);
        r4 = "TelegramWebviewProxy";
        r0.addJavascriptInterface(r3, r4);
    L_0x0a12:
        r0 = r1.webView;
        r3 = new org.telegram.ui.PaymentFormActivity$6;
        r3.<init>();
        r0.setWebViewClient(r3);
        r0 = r1.linearLayout2;
        r3 = r1.webView;
        r4 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r6 = -1;
        r4 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r6, r4);
        r0.addView(r3, r4);
        r0 = r1.sectionCell;
        r3 = new org.telegram.ui.Cells.ShadowSectionCell;
        r3.<init>(r2);
        r4 = 2;
        r0[r4] = r3;
        r0 = r1.linearLayout2;
        r3 = r1.sectionCell;
        r3 = r3[r4];
        r4 = -2;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = new org.telegram.ui.Cells.TextCheckCell;
        r0.<init>(r2);
        r1.checkCell1 = r0;
        r0 = r1.checkCell1;
        r3 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r0.setBackgroundDrawable(r3);
        r0 = r1.checkCell1;
        r3 = 2131560358; // 0x7f0d07a6 float:1.8746086E38 double:1.053130745E-314;
        r4 = "PaymentCardSavePaymentInformation";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = r1.saveCardInfo;
        r0.setTextAndCheck(r3, r4, r5);
        r0 = r1.linearLayout2;
        r3 = r1.checkCell1;
        r4 = -2;
        r6 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = r1.checkCell1;
        r3 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$55th3Bl6h3UNIH_JJ7d61ZMtWHE;
        r3.<init>(r1);
        r0.setOnClickListener(r3);
        r0 = r1.bottomCell;
        r3 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r3.<init>(r2);
        r0[r5] = r3;
        r0 = r1.bottomCell;
        r0 = r0[r5];
        r3 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r2 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r3, r14);
        r0.setBackgroundDrawable(r2);
        r28.updateSavePaymentField();
        r0 = r1.linearLayout2;
        r2 = r1.bottomCell;
        r2 = r2[r5];
        r3 = -2;
        r4 = -1;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r4, r3);
        r0.addView(r2, r3);
        goto L_0x18b9;
    L_0x0aa4:
        r0 = r1.paymentForm;
        r0 = r0.native_params;
        if (r0 == 0) goto L_0x0ae4;
    L_0x0aaa:
        r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0ae0 }
        r0 = r0.data;	 Catch:{ Exception -> 0x0ae0 }
        r3.<init>(r0);	 Catch:{ Exception -> 0x0ae0 }
        r0 = "need_country";
        r0 = r3.getBoolean(r0);	 Catch:{ Exception -> 0x0aba }
        r1.need_card_country = r0;	 Catch:{ Exception -> 0x0aba }
        goto L_0x0abc;
    L_0x0aba:
        r1.need_card_country = r5;	 Catch:{ Exception -> 0x0ae0 }
    L_0x0abc:
        r0 = "need_zip";
        r0 = r3.getBoolean(r0);	 Catch:{ Exception -> 0x0ac5 }
        r1.need_card_postcode = r0;	 Catch:{ Exception -> 0x0ac5 }
        goto L_0x0ac7;
    L_0x0ac5:
        r1.need_card_postcode = r5;	 Catch:{ Exception -> 0x0ae0 }
    L_0x0ac7:
        r0 = "need_cardholder_name";
        r0 = r3.getBoolean(r0);	 Catch:{ Exception -> 0x0ad0 }
        r1.need_card_name = r0;	 Catch:{ Exception -> 0x0ad0 }
        goto L_0x0ad2;
    L_0x0ad0:
        r1.need_card_name = r5;	 Catch:{ Exception -> 0x0ae0 }
    L_0x0ad2:
        r0 = "publishable_key";
        r0 = r3.getString(r0);	 Catch:{ Exception -> 0x0adb }
        r1.stripeApiKey = r0;	 Catch:{ Exception -> 0x0adb }
        goto L_0x0ae4;
    L_0x0adb:
        r0 = "";
        r1.stripeApiKey = r0;	 Catch:{ Exception -> 0x0ae0 }
        goto L_0x0ae4;
    L_0x0ae0:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0ae4:
        r28.initAndroidPay(r29);
        r3 = 6;
        r0 = new org.telegram.p004ui.Components.EditTextBoldCursor[r3];
        r1.inputFields = r0;
        r0 = 0;
    L_0x0aed:
        if (r0 >= r3) goto L_0x0e44;
    L_0x0aef:
        if (r0 != 0) goto L_0x0b25;
    L_0x0af1:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = 2131560361; // 0x7f0d07a9 float:1.8746092E38 double:1.0531307464E-314;
        r6 = "PaymentCardTitle";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        goto L_0x0b5b;
    L_0x0b25:
        r3 = 4;
        if (r0 != r3) goto L_0x0b5b;
    L_0x0b28:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = 2131560352; // 0x7f0d07a0 float:1.8746074E38 double:1.053130742E-314;
        r6 = "PaymentBillingAddress";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r8];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
    L_0x0b5b:
        r3 = 3;
        if (r0 == r3) goto L_0x0b6a;
    L_0x0b5e:
        r3 = 5;
        if (r0 == r3) goto L_0x0b6a;
    L_0x0b61:
        r3 = 4;
        if (r0 != r3) goto L_0x0b68;
    L_0x0b64:
        r3 = r1.need_card_postcode;
        if (r3 == 0) goto L_0x0b6a;
    L_0x0b68:
        r3 = 1;
        goto L_0x0b6b;
    L_0x0b6a:
        r3 = 0;
    L_0x0b6b:
        r4 = new android.widget.FrameLayout;
        r4.<init>(r2);
        r6 = r1.linearLayout2;
        r7 = 50;
        r9 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r9, r7);
        r6.addView(r4, r7);
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r4.setBackgroundColor(r6);
        r6 = r1.inputFields;
        r7 = new org.telegram.ui.Components.EditTextBoldCursor;
        r7.<init>(r2);
        r6[r0] = r7;
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = java.lang.Integer.valueOf(r0);
        r6.setTag(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r6.setTextSize(r8, r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = "windowBackgroundWhiteHintText";
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r7);
        r6.setHintTextColor(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r6.setTextColor(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 0;
        r6.setBackgroundDrawable(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r6.setCursorColor(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r6.setCursorSize(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r6.setCursorWidth(r7);
        r6 = 3;
        if (r0 != r6) goto L_0x0c12;
    L_0x0be4:
        r7 = new android.text.InputFilter[r8];
        r9 = new android.text.InputFilter$LengthFilter;
        r9.<init>(r6);
        r7[r5] = r9;
        r6 = r1.inputFields;
        r6 = r6[r0];
        r6.setFilters(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 130; // 0x82 float:1.82E-43 double:6.4E-322;
        r6.setInputType(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = android.graphics.Typeface.DEFAULT;
        r6.setTypeface(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = android.text.method.PasswordTransformationMethod.getInstance();
        r6.setTransformationMethod(r7);
        goto L_0x0c56;
    L_0x0c12:
        if (r0 != 0) goto L_0x0c1d;
    L_0x0c14:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 3;
        r6.setInputType(r7);
        goto L_0x0c56;
    L_0x0c1d:
        r6 = 4;
        if (r0 != r6) goto L_0x0c34;
    L_0x0c20:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$Ueinsx-MGryTGh-0YI94FAwwnHM;
        r7.<init>(r1);
        r6.setOnTouchListener(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r6.setInputType(r5);
        goto L_0x0c56;
    L_0x0c34:
        if (r0 != r8) goto L_0x0c40;
    L_0x0c36:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 16386; // 0x4002 float:2.2962E-41 double:8.096E-320;
        r6.setInputType(r7);
        goto L_0x0c56;
    L_0x0c40:
        r6 = 2;
        if (r0 != r6) goto L_0x0c4d;
    L_0x0c43:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
        r6.setInputType(r7);
        goto L_0x0c56;
    L_0x0c4d:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 16385; // 0x4001 float:2.296E-41 double:8.0953E-320;
        r6.setInputType(r7);
    L_0x0c56:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r6.setImeOptions(r7);
        if (r0 == 0) goto L_0x0cc6;
    L_0x0c62:
        if (r0 == r8) goto L_0x0cb5;
    L_0x0c64:
        r6 = 2;
        if (r0 == r6) goto L_0x0ca4;
    L_0x0c67:
        r6 = 3;
        if (r0 == r6) goto L_0x0c93;
    L_0x0c6a:
        r6 = 4;
        if (r0 == r6) goto L_0x0c82;
    L_0x0c6d:
        r6 = 5;
        if (r0 == r6) goto L_0x0c71;
    L_0x0c70:
        goto L_0x0cd6;
    L_0x0c71:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560403; // 0x7f0d07d3 float:1.8746177E38 double:1.053130767E-314;
        r9 = "PaymentShippingZipPlaceholder";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
        goto L_0x0cd6;
    L_0x0c82:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560393; // 0x7f0d07c9 float:1.8746157E38 double:1.053130762E-314;
        r9 = "PaymentShippingCountry";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
        goto L_0x0cd6;
    L_0x0c93:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560353; // 0x7f0d07a1 float:1.8746076E38 double:1.0531307425E-314;
        r9 = "PaymentCardCvv";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
        goto L_0x0cd6;
    L_0x0ca4:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560356; // 0x7f0d07a4 float:1.8746082E38 double:1.053130744E-314;
        r9 = "PaymentCardName";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
        goto L_0x0cd6;
    L_0x0cb5:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560354; // 0x7f0d07a2 float:1.8746078E38 double:1.053130743E-314;
        r9 = "PaymentCardExpireDate";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
        goto L_0x0cd6;
    L_0x0cc6:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 2131560357; // 0x7f0d07a5 float:1.8746084E38 double:1.0531307444E-314;
        r9 = "PaymentCardNumber";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r6.setHint(r7);
    L_0x0cd6:
        if (r0 != 0) goto L_0x0ce5;
    L_0x0cd8:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = new org.telegram.ui.PaymentFormActivity$7;
        r7.<init>();
        r6.addTextChangedListener(r7);
        goto L_0x0cf3;
    L_0x0ce5:
        if (r0 != r8) goto L_0x0cf3;
    L_0x0ce7:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = new org.telegram.ui.PaymentFormActivity$8;
        r7.<init>();
        r6.addTextChangedListener(r7);
    L_0x0cf3:
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r6.setPadding(r5, r5, r5, r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = org.telegram.messenger.LocaleController.isRTL;
        if (r7 == 0) goto L_0x0d0a;
    L_0x0d08:
        r7 = 5;
        goto L_0x0d0b;
    L_0x0d0a:
        r7 = 3;
    L_0x0d0b:
        r6.setGravity(r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r21 = -1;
        r22 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r23 = 51;
        r24 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r25 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r26 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r27 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r21, r22, r23, r24, r25, r26, r27);
        r4.addView(r6, r7);
        r6 = r1.inputFields;
        r6 = r6[r0];
        r7 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$kGOIockVRhCZimnBQEOXCbrCCls;
        r7.<init>(r1);
        r6.setOnEditorActionListener(r7);
        r6 = 3;
        if (r0 != r6) goto L_0x0d50;
    L_0x0d36:
        r6 = r1.sectionCell;
        r7 = new org.telegram.ui.Cells.ShadowSectionCell;
        r7.<init>(r2);
        r6[r5] = r7;
        r6 = r1.linearLayout2;
        r7 = r1.sectionCell;
        r7 = r7[r5];
        r9 = -2;
        r10 = -1;
        r11 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r9);
        r6.addView(r7, r11);
        goto L_0x0e04;
    L_0x0d50:
        r6 = 5;
        r9 = -2;
        r10 = -1;
        if (r0 != r6) goto L_0x0dcd;
    L_0x0d55:
        r6 = r1.sectionCell;
        r7 = new org.telegram.ui.Cells.ShadowSectionCell;
        r7.<init>(r2);
        r11 = 2;
        r6[r11] = r7;
        r6 = r1.linearLayout2;
        r7 = r1.sectionCell;
        r7 = r7[r11];
        r11 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r9);
        r6.addView(r7, r11);
        r6 = new org.telegram.ui.Cells.TextCheckCell;
        r6.<init>(r2);
        r1.checkCell1 = r6;
        r6 = r1.checkCell1;
        r7 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r6.setBackgroundDrawable(r7);
        r6 = r1.checkCell1;
        r7 = 2131560358; // 0x7f0d07a6 float:1.8746086E38 double:1.053130745E-314;
        r9 = "PaymentCardSavePaymentInformation";
        r7 = org.telegram.messenger.LocaleController.getString(r9, r7);
        r9 = r1.saveCardInfo;
        r6.setTextAndCheck(r7, r9, r5);
        r6 = r1.linearLayout2;
        r7 = r1.checkCell1;
        r9 = -2;
        r10 = -1;
        r11 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r9);
        r6.addView(r7, r11);
        r6 = r1.checkCell1;
        r7 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$7nFCu4nozpZFMiRn4XlVrELG-z4;
        r7.<init>(r1);
        r6.setOnClickListener(r7);
        r6 = r1.bottomCell;
        r7 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r7.<init>(r2);
        r6[r5] = r7;
        r6 = r1.bottomCell;
        r6 = r6[r5];
        r7 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r9 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r7, r14);
        r6.setBackgroundDrawable(r9);
        r28.updateSavePaymentField();
        r6 = r1.linearLayout2;
        r7 = r1.bottomCell;
        r7 = r7[r5];
        r9 = -2;
        r10 = -1;
        r11 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r10, r9);
        r6.addView(r7, r11);
        goto L_0x0e04;
    L_0x0dcd:
        if (r0 != 0) goto L_0x0e04;
    L_0x0dcf:
        r6 = new android.widget.FrameLayout;
        r6.<init>(r2);
        r1.androidPayContainer = r6;
        r6 = r1.androidPayContainer;
        r7 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        r6.setId(r7);
        r6 = r1.androidPayContainer;
        r7 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r6.setBackgroundDrawable(r7);
        r6 = r1.androidPayContainer;
        r7 = 8;
        r6.setVisibility(r7);
        r6 = r1.androidPayContainer;
        r21 = -2;
        r22 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r23 = 21;
        r24 = 0;
        r25 = 0;
        r26 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r27 = 0;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r21, r22, r23, r24, r25, r26, r27);
        r4.addView(r6, r7);
    L_0x0e04:
        if (r3 == 0) goto L_0x0e22;
    L_0x0e06:
        r3 = new org.telegram.ui.PaymentFormActivity$9;
        r3.<init>(r2);
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r6);
        r6 = r1.dividers;
        r6.add(r3);
        r6 = new android.widget.FrameLayout$LayoutParams;
        r7 = 83;
        r9 = -1;
        r6.<init>(r9, r8, r7);
        r4.addView(r3, r6);
    L_0x0e22:
        r3 = 4;
        if (r0 != r3) goto L_0x0e2d;
    L_0x0e25:
        r3 = r1.need_card_country;
        if (r3 == 0) goto L_0x0e2a;
    L_0x0e29:
        goto L_0x0e2d;
    L_0x0e2a:
        r3 = 8;
        goto L_0x0e3c;
    L_0x0e2d:
        r3 = 5;
        if (r0 != r3) goto L_0x0e34;
    L_0x0e30:
        r3 = r1.need_card_postcode;
        if (r3 == 0) goto L_0x0e2a;
    L_0x0e34:
        r3 = 2;
        if (r0 != r3) goto L_0x0e3f;
    L_0x0e37:
        r3 = r1.need_card_name;
        if (r3 != 0) goto L_0x0e3f;
    L_0x0e3b:
        goto L_0x0e2a;
    L_0x0e3c:
        r4.setVisibility(r3);
    L_0x0e3f:
        r0 = r0 + 1;
        r3 = 6;
        goto L_0x0aed;
    L_0x0e44:
        r0 = r1.need_card_country;
        if (r0 != 0) goto L_0x0e5c;
    L_0x0e48:
        r0 = r1.need_card_postcode;
        if (r0 != 0) goto L_0x0e5c;
    L_0x0e4c:
        r0 = r1.headerCell;
        r0 = r0[r8];
        r2 = 8;
        r0.setVisibility(r2);
        r0 = r1.sectionCell;
        r0 = r0[r5];
        r0.setVisibility(r2);
    L_0x0e5c:
        r0 = r1.need_card_postcode;
        if (r0 == 0) goto L_0x0e6d;
    L_0x0e60:
        r0 = r1.inputFields;
        r2 = 5;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
        goto L_0x18b9;
    L_0x0e6d:
        r0 = r1.inputFields;
        r2 = 3;
        r0 = r0[r2];
        r2 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r0.setImeOptions(r2);
        goto L_0x18b9;
    L_0x0e7a:
        if (r0 != r8) goto L_0x0f1c;
    L_0x0e7c:
        r0 = r1.requestedInfo;
        r0 = r0.shipping_options;
        r0 = r0.size();
        r3 = new org.telegram.p004ui.Cells.RadioCell[r0];
        r1.radioCells = r3;
        r3 = 0;
    L_0x0e89:
        if (r3 >= r0) goto L_0x0ef4;
    L_0x0e8b:
        r4 = r1.requestedInfo;
        r4 = r4.shipping_options;
        r4 = r4.get(r3);
        r4 = (org.telegram.tgnet.TLRPC.TL_shippingOption) r4;
        r6 = r1.radioCells;
        r7 = new org.telegram.ui.Cells.RadioCell;
        r7.<init>(r2);
        r6[r3] = r7;
        r6 = r1.radioCells;
        r6 = r6[r3];
        r7 = java.lang.Integer.valueOf(r3);
        r6.setTag(r7);
        r6 = r1.radioCells;
        r6 = r6[r3];
        r7 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r6.setBackgroundDrawable(r7);
        r6 = r1.radioCells;
        r6 = r6[r3];
        r7 = 2;
        r9 = new java.lang.Object[r7];
        r7 = r4.prices;
        r7 = r1.getTotalPriceString(r7);
        r9[r5] = r7;
        r4 = r4.title;
        r9[r8] = r4;
        r4 = "%s - %s";
        r4 = java.lang.String.format(r4, r9);
        if (r3 != 0) goto L_0x0ed1;
    L_0x0ecf:
        r7 = 1;
        goto L_0x0ed2;
    L_0x0ed1:
        r7 = 0;
    L_0x0ed2:
        r9 = r0 + -1;
        if (r3 == r9) goto L_0x0ed8;
    L_0x0ed6:
        r9 = 1;
        goto L_0x0ed9;
    L_0x0ed8:
        r9 = 0;
    L_0x0ed9:
        r6.setText(r4, r7, r9);
        r4 = r1.radioCells;
        r4 = r4[r3];
        r6 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$vEFqYoe1G6TRzc-6QuSxLqi0_ls;
        r6.<init>(r1);
        r4.setOnClickListener(r6);
        r4 = r1.linearLayout2;
        r6 = r1.radioCells;
        r6 = r6[r3];
        r4.addView(r6);
        r3 = r3 + 1;
        goto L_0x0e89;
    L_0x0ef4:
        r0 = r1.bottomCell;
        r3 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r3.<init>(r2);
        r0[r5] = r3;
        r0 = r1.bottomCell;
        r0 = r0[r5];
        r3 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r2 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r3, r14);
        r0.setBackgroundDrawable(r2);
        r0 = r1.linearLayout2;
        r2 = r1.bottomCell;
        r2 = r2[r5];
        r3 = -2;
        r4 = -1;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r4, r3);
        r0.addView(r2, r3);
        goto L_0x18b9;
    L_0x0f1c:
        r3 = 3;
        if (r0 != r3) goto L_0x115a;
    L_0x0f1f:
        r3 = 2;
        r0 = new org.telegram.p004ui.Components.EditTextBoldCursor[r3];
        r1.inputFields = r0;
        r0 = 0;
    L_0x0f25:
        if (r0 >= r3) goto L_0x18b9;
    L_0x0f27:
        if (r0 != 0) goto L_0x0f5d;
    L_0x0f29:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = 2131560361; // 0x7f0d07a9 float:1.8746092E38 double:1.0531307464E-314;
        r6 = "PaymentCardTitle";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        goto L_0x0f5e;
    L_0x0f5d:
        r7 = -1;
    L_0x0f5e:
        r3 = new android.widget.FrameLayout;
        r3.<init>(r2);
        r4 = r1.linearLayout2;
        r6 = 50;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r4.addView(r3, r6);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        if (r0 == r8) goto L_0x0f79;
    L_0x0f77:
        r4 = 1;
        goto L_0x0f7a;
    L_0x0f79:
        r4 = 0;
    L_0x0f7a:
        if (r4 == 0) goto L_0x0f99;
    L_0x0f7c:
        r6 = 7;
        if (r0 != r6) goto L_0x0f89;
    L_0x0f7f:
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.phone_requested;
        if (r6 != 0) goto L_0x0f89;
    L_0x0f87:
        r4 = 0;
        goto L_0x0f99;
    L_0x0f89:
        r6 = 6;
        if (r0 != r6) goto L_0x0f99;
    L_0x0f8c:
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r7 = r6.phone_requested;
        if (r7 != 0) goto L_0x0f99;
    L_0x0f94:
        r6 = r6.email_requested;
        if (r6 != 0) goto L_0x0f99;
    L_0x0f98:
        goto L_0x0f87;
    L_0x0f99:
        if (r4 == 0) goto L_0x0fb7;
    L_0x0f9b:
        r4 = new org.telegram.ui.PaymentFormActivity$10;
        r4.<init>(r2);
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r4.setBackgroundColor(r6);
        r6 = r1.dividers;
        r6.add(r4);
        r6 = new android.widget.FrameLayout$LayoutParams;
        r7 = 83;
        r9 = -1;
        r6.<init>(r9, r8, r7);
        r3.addView(r4, r6);
    L_0x0fb7:
        r4 = r1.inputFields;
        r6 = new org.telegram.ui.Components.EditTextBoldCursor;
        r6.<init>(r2);
        r4[r0] = r6;
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = java.lang.Integer.valueOf(r0);
        r4.setTag(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r4.setTextSize(r8, r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = "windowBackgroundWhiteHintText";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r4.setHintTextColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setTextColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 0;
        r4.setBackgroundDrawable(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setCursorColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4.setCursorSize(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r4.setCursorWidth(r6);
        if (r0 != 0) goto L_0x1028;
    L_0x1017:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.p004ui.C1804-$$Lambda$PaymentFormActivity$NaAxpZmEom2J3d4n5KPbjk_hflw.INSTANCE;
        r4.setOnTouchListener(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.setInputType(r5);
        goto L_0x103a;
    L_0x1028:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r4.setInputType(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = android.graphics.Typeface.DEFAULT;
        r4.setTypeface(r6);
    L_0x103a:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4.setImeOptions(r6);
        if (r0 == 0) goto L_0x1061;
    L_0x1046:
        if (r0 == r8) goto L_0x1049;
    L_0x1048:
        goto L_0x106e;
    L_0x1049:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 2131559788; // 0x7f0d056c float:1.874493E38 double:1.0531304633E-314;
        r7 = "LoginPassword";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r4.setHint(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.requestFocus();
        goto L_0x106e;
    L_0x1061:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = r1.paymentForm;
        r6 = r6.saved_credentials;
        r6 = r6.title;
        r4.setText(r6);
    L_0x106e:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4.setPadding(r5, r5, r5, r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.messenger.LocaleController.isRTL;
        if (r6 == 0) goto L_0x1085;
    L_0x1083:
        r6 = 5;
        goto L_0x1086;
    L_0x1085:
        r6 = 3;
    L_0x1086:
        r4.setGravity(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r21 = -1;
        r22 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r23 = 51;
        r24 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r25 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r26 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r27 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r21, r22, r23, r24, r25, r26, r27);
        r3.addView(r4, r6);
        r3 = r1.inputFields;
        r3 = r3[r0];
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw;
        r4.<init>(r1);
        r3.setOnEditorActionListener(r4);
        if (r0 != r8) goto L_0x1155;
    L_0x10b0:
        r3 = r1.bottomCell;
        r4 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131560370; // 0x7f0d07b2 float:1.874611E38 double:1.053130751E-314;
        r6 = new java.lang.Object[r8];
        r7 = r1.paymentForm;
        r7 = r7.saved_credentials;
        r7 = r7.title;
        r6[r5] = r7;
        r7 = "PaymentConfirmationMessage";
        r4 = org.telegram.messenger.LocaleController.formatString(r7, r4, r6);
        r3.setText(r4);
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131165394; // 0x7f0700d2 float:1.7945004E38 double:1.052935607E-314;
        r4 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r4, r14);
        r3.setBackgroundDrawable(r4);
        r3 = r1.linearLayout2;
        r4 = r1.bottomCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        r3 = r1.settingsCell;
        r4 = new org.telegram.ui.Cells.TextSettingsCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.settingsCell;
        r3 = r3[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r3.setBackgroundDrawable(r4);
        r3 = r1.settingsCell;
        r3 = r3[r5];
        r4 = 2131560371; // 0x7f0d07b3 float:1.8746112E38 double:1.0531307513E-314;
        r6 = "PaymentConfirmationNewCard";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4, r5);
        r3 = r1.linearLayout2;
        r4 = r1.settingsCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        r3 = r1.settingsCell;
        r3 = r3[r5];
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$hOneDfBvow-ay5NfpKknN53qGXA;
        r4.<init>(r1);
        r3.setOnClickListener(r4);
        r3 = r1.bottomCell;
        r4 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.bottomCell;
        r3 = r3[r8];
        r4 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r4, r14);
        r3.setBackgroundDrawable(r6);
        r3 = r1.linearLayout2;
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
    L_0x1155:
        r0 = r0 + 1;
        r3 = 2;
        goto L_0x0f25;
    L_0x115a:
        r3 = 4;
        if (r0 == r3) goto L_0x14b8;
    L_0x115d:
        r3 = 5;
        if (r0 != r3) goto L_0x1162;
    L_0x1160:
        goto L_0x14b8;
    L_0x1162:
        r3 = 6;
        if (r0 != r3) goto L_0x18b9;
    L_0x1165:
        r0 = new org.telegram.ui.Cells.EditTextSettingsCell;
        r0.<init>(r2);
        r1.codeFieldCell = r0;
        r0 = r1.codeFieldCell;
        r3 = 2131560345; // 0x7f0d0799 float:1.874606E38 double:1.0531307385E-314;
        r4 = "PasswordCode";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = "";
        r0.setTextAndHint(r4, r3, r5);
        r0 = r1.codeFieldCell;
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r3);
        r0 = r1.codeFieldCell;
        r0 = r0.getTextView();
        r3 = 3;
        r0.setInputType(r3);
        r3 = 6;
        r0.setImeOptions(r3);
        r3 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs;
        r3.<init>(r1);
        r0.setOnEditorActionListener(r3);
        r3 = new org.telegram.ui.PaymentFormActivity$15;
        r3.<init>();
        r0.addTextChangedListener(r3);
        r0 = r1.linearLayout2;
        r3 = r1.codeFieldCell;
        r4 = -2;
        r6 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = r1.bottomCell;
        r3 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r3.<init>(r2);
        r4 = 2;
        r0[r4] = r3;
        r0 = r1.bottomCell;
        r0 = r0[r4];
        r3 = 2131165394; // 0x7f0700d2 float:1.7945004E38 double:1.052935607E-314;
        r3 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r3, r14);
        r0.setBackgroundDrawable(r3);
        r0 = r1.linearLayout2;
        r3 = r1.bottomCell;
        r3 = r3[r4];
        r4 = -2;
        r6 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = r1.settingsCell;
        r3 = new org.telegram.ui.Cells.TextSettingsCell;
        r3.<init>(r2);
        r0[r8] = r3;
        r0 = r1.settingsCell;
        r0 = r0[r8];
        r3 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r0.setBackgroundDrawable(r3);
        r0 = r1.settingsCell;
        r0 = r0[r8];
        r0.setTag(r13);
        r0 = r1.settingsCell;
        r0 = r0[r8];
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r0.setTextColor(r3);
        r0 = r1.settingsCell;
        r0 = r0[r8];
        r3 = 2131560581; // 0x7f0d0885 float:1.8746538E38 double:1.053130855E-314;
        r4 = "ResendCode";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r0.setText(r3, r8);
        r0 = r1.linearLayout2;
        r3 = r1.settingsCell;
        r3 = r3[r8];
        r4 = -2;
        r6 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = r1.settingsCell;
        r0 = r0[r8];
        r3 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$I7CPk7usQoOQw119fB6a-FgtbsU;
        r3.<init>(r1);
        r0.setOnClickListener(r3);
        r0 = r1.settingsCell;
        r3 = new org.telegram.ui.Cells.TextSettingsCell;
        r3.<init>(r2);
        r0[r5] = r3;
        r0 = r1.settingsCell;
        r0 = r0[r5];
        r3 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r8);
        r0.setBackgroundDrawable(r3);
        r0 = r1.settingsCell;
        r0 = r0[r5];
        r3 = "windowBackgroundWhiteRedText3";
        r0.setTag(r3);
        r0 = r1.settingsCell;
        r0 = r0[r5];
        r3 = "windowBackgroundWhiteRedText3";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r0.setTextColor(r3);
        r0 = r1.settingsCell;
        r0 = r0[r5];
        r3 = 2131558402; // 0x7f0d0002 float:1.8742119E38 double:1.0531297785E-314;
        r4 = "AbortPassword";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r0.setText(r3, r5);
        r0 = r1.linearLayout2;
        r3 = r1.settingsCell;
        r3 = r3[r5];
        r4 = -2;
        r6 = -1;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r7);
        r0 = r1.settingsCell;
        r0 = r0[r5];
        r3 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$u3SvDLQ9ZpBSRMUM2jg2lBOMsbk;
        r3.<init>(r1);
        r0.setOnClickListener(r3);
        r3 = 3;
        r0 = new org.telegram.p004ui.Components.EditTextBoldCursor[r3];
        r1.inputFields = r0;
        r0 = 0;
    L_0x1283:
        if (r0 >= r3) goto L_0x14b3;
    L_0x1285:
        if (r0 != 0) goto L_0x12bb;
    L_0x1287:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r5];
        r4 = 2131560384; // 0x7f0d07c0 float:1.8746139E38 double:1.053130758E-314;
        r6 = "PaymentPasswordTitle";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r5];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        goto L_0x12f2;
    L_0x12bb:
        r3 = 2;
        if (r0 != r3) goto L_0x12f2;
    L_0x12be:
        r3 = r1.headerCell;
        r4 = new org.telegram.ui.Cells.HeaderCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        r3 = r1.headerCell;
        r3 = r3[r8];
        r4 = 2131560380; // 0x7f0d07bc float:1.874613E38 double:1.053130756E-314;
        r6 = "PaymentPasswordEmailTitle";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.linearLayout2;
        r4 = r1.headerCell;
        r4 = r4[r8];
        r6 = -2;
        r7 = -1;
        r9 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r3.addView(r4, r9);
        goto L_0x12f3;
    L_0x12f2:
        r7 = -1;
    L_0x12f3:
        r3 = new android.widget.FrameLayout;
        r3.<init>(r2);
        r4 = r1.linearLayout2;
        r6 = 50;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r7, r6);
        r4.addView(r3, r6);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r3.setBackgroundColor(r4);
        if (r0 != 0) goto L_0x1328;
    L_0x130c:
        r4 = new org.telegram.ui.PaymentFormActivity$16;
        r4.<init>(r2);
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r4.setBackgroundColor(r6);
        r6 = r1.dividers;
        r6.add(r4);
        r6 = new android.widget.FrameLayout$LayoutParams;
        r7 = 83;
        r9 = -1;
        r6.<init>(r9, r8, r7);
        r3.addView(r4, r6);
    L_0x1328:
        r4 = r1.inputFields;
        r6 = new org.telegram.ui.Components.EditTextBoldCursor;
        r6.<init>(r2);
        r4[r0] = r6;
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = java.lang.Integer.valueOf(r0);
        r4.setTag(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r4.setTextSize(r8, r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = "windowBackgroundWhiteHintText";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r4.setHintTextColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setTextColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r7 = 0;
        r4.setBackgroundDrawable(r7);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r13);
        r4.setCursorColor(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4.setCursorSize(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r4.setCursorWidth(r6);
        if (r0 == 0) goto L_0x139f;
    L_0x1388:
        if (r0 != r8) goto L_0x138b;
    L_0x138a:
        goto L_0x139f;
    L_0x138b:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 33;
        r4.setInputType(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4.setImeOptions(r6);
        goto L_0x13bb;
    L_0x139f:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r4.setInputType(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = android.graphics.Typeface.DEFAULT;
        r4.setTypeface(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r4.setImeOptions(r6);
    L_0x13bb:
        if (r0 == 0) goto L_0x13e5;
    L_0x13bd:
        if (r0 == r8) goto L_0x13d4;
    L_0x13bf:
        r4 = 2;
        if (r0 == r4) goto L_0x13c3;
    L_0x13c2:
        goto L_0x13fc;
    L_0x13c3:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 2131560378; // 0x7f0d07ba float:1.8746127E38 double:1.053130755E-314;
        r9 = "PaymentPasswordEmail";
        r6 = org.telegram.messenger.LocaleController.getString(r9, r6);
        r4.setHint(r6);
        goto L_0x13fc;
    L_0x13d4:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 2131560383; // 0x7f0d07bf float:1.8746137E38 double:1.0531307573E-314;
        r9 = "PaymentPasswordReEnter";
        r6 = org.telegram.messenger.LocaleController.getString(r9, r6);
        r4.setHint(r6);
        goto L_0x13fc;
    L_0x13e5:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 2131560381; // 0x7f0d07bd float:1.8746133E38 double:1.0531307563E-314;
        r9 = "PaymentPasswordEnter";
        r6 = org.telegram.messenger.LocaleController.getString(r9, r6);
        r4.setHint(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r4.requestFocus();
    L_0x13fc:
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r4.setPadding(r5, r5, r5, r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r6 = org.telegram.messenger.LocaleController.isRTL;
        if (r6 == 0) goto L_0x1413;
    L_0x1411:
        r6 = 5;
        goto L_0x1414;
    L_0x1413:
        r6 = 3;
    L_0x1414:
        r4.setGravity(r6);
        r4 = r1.inputFields;
        r4 = r4[r0];
        r21 = -1;
        r22 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r23 = 51;
        r24 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r25 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r26 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r27 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r21, r22, r23, r24, r25, r26, r27);
        r3.addView(r4, r6);
        r3 = r1.inputFields;
        r3 = r3[r0];
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$wikSuuvfxhb2U7Hw9X3oaWcgGS8;
        r4.<init>(r1);
        r3.setOnEditorActionListener(r4);
        if (r0 != r8) goto L_0x1475;
    L_0x143e:
        r3 = r1.bottomCell;
        r4 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r4.<init>(r2);
        r3[r5] = r4;
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131560382; // 0x7f0d07be float:1.8746135E38 double:1.053130757E-314;
        r6 = "PaymentPasswordInfo";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.bottomCell;
        r3 = r3[r5];
        r4 = 2131165394; // 0x7f0700d2 float:1.7945004E38 double:1.052935607E-314;
        r4 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r4, r14);
        r3.setBackgroundDrawable(r4);
        r3 = r1.linearLayout2;
        r4 = r1.bottomCell;
        r4 = r4[r5];
        r6 = -2;
        r9 = -1;
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r9, r6);
        r3.addView(r4, r10);
        goto L_0x14ae;
    L_0x1475:
        r3 = 2;
        if (r0 != r3) goto L_0x14ae;
    L_0x1478:
        r3 = r1.bottomCell;
        r4 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r4.<init>(r2);
        r3[r8] = r4;
        r3 = r1.bottomCell;
        r3 = r3[r8];
        r4 = 2131560379; // 0x7f0d07bb float:1.8746129E38 double:1.0531307553E-314;
        r6 = "PaymentPasswordEmailInfo";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r3.setText(r4);
        r3 = r1.bottomCell;
        r3 = r3[r8];
        r4 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r4, r14);
        r3.setBackgroundDrawable(r6);
        r3 = r1.linearLayout2;
        r4 = r1.bottomCell;
        r4 = r4[r8];
        r6 = -2;
        r9 = -1;
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r9, r6);
        r3.addView(r4, r10);
    L_0x14ae:
        r0 = r0 + 1;
        r3 = 3;
        goto L_0x1283;
    L_0x14b3:
        r28.updatePasswordFields();
        goto L_0x18b9;
    L_0x14b8:
        r7 = 0;
        r0 = new org.telegram.ui.Cells.PaymentInfoCell;
        r0.<init>(r2);
        r1.paymentInfoCell = r0;
        r0 = r1.paymentInfoCell;
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r3);
        r0 = r1.paymentInfoCell;
        r3 = r1.messageObject;
        r3 = r3.messageOwner;
        r3 = r3.media;
        r3 = (org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) r3;
        r4 = r1.currentBotName;
        r0.setInvoice(r3, r4);
        r0 = r1.linearLayout2;
        r3 = r1.paymentInfoCell;
        r4 = -2;
        r6 = -1;
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r10);
        r0 = r1.sectionCell;
        r3 = new org.telegram.ui.Cells.ShadowSectionCell;
        r3.<init>(r2);
        r0[r5] = r3;
        r0 = r1.linearLayout2;
        r3 = r1.sectionCell;
        r3 = r3[r5];
        r10 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r6, r4);
        r0.addView(r3, r10);
        r0 = new java.util.ArrayList;
        r3 = r1.paymentForm;
        r3 = r3.invoice;
        r3 = r3.prices;
        r0.<init>(r3);
        r3 = r1.shippingOption;
        if (r3 == 0) goto L_0x150f;
    L_0x150a:
        r3 = r3.prices;
        r0.addAll(r3);
    L_0x150f:
        r3 = r1.getTotalPriceString(r0);
        r4 = 0;
    L_0x1514:
        r6 = r0.size();
        if (r4 >= r6) goto L_0x154b;
    L_0x151a:
        r6 = r0.get(r4);
        r6 = (org.telegram.tgnet.TLRPC.TL_labeledPrice) r6;
        r10 = new org.telegram.ui.Cells.TextPriceCell;
        r10.<init>(r2);
        r11 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r10.setBackgroundColor(r11);
        r11 = r6.label;
        r12 = org.telegram.messenger.LocaleController.getInstance();
        r7 = r6.amount;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.currency;
        r6 = r12.formatCurrencyString(r7, r6);
        r10.setTextAndValue(r11, r6, r5);
        r6 = r1.linearLayout2;
        r6.addView(r10);
        r4 = r4 + 1;
        r7 = 0;
        r8 = 1;
        goto L_0x1514;
    L_0x154b:
        r0 = new org.telegram.ui.Cells.TextPriceCell;
        r0.<init>(r2);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r4);
        r4 = 2131560409; // 0x7f0d07d9 float:1.874619E38 double:1.05313077E-314;
        r6 = "PaymentTransactionTotal";
        r4 = org.telegram.messenger.LocaleController.getString(r6, r4);
        r6 = 1;
        r0.setTextAndValue(r4, r3, r6);
        r4 = r1.linearLayout2;
        r4.addView(r0);
        r0 = new org.telegram.ui.PaymentFormActivity$11;
        r0.<init>(r2);
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r4);
        r4 = r1.dividers;
        r4.add(r0);
        r4 = r1.linearLayout2;
        r6 = new android.widget.FrameLayout$LayoutParams;
        r7 = 83;
        r8 = -1;
        r10 = 1;
        r6.<init>(r8, r10, r7);
        r4.addView(r0, r6);
        r0 = r1.detailSettingsCell;
        r4 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r4.<init>(r2);
        r0[r5] = r4;
        r0 = r1.detailSettingsCell;
        r0 = r0[r5];
        r4 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r10);
        r0.setBackgroundDrawable(r4);
        r0 = r1.detailSettingsCell;
        r0 = r0[r5];
        r4 = r1.cardName;
        r6 = 2131560364; // 0x7f0d07ac float:1.8746098E38 double:1.053130748E-314;
        r7 = "PaymentCheckoutMethod";
        r6 = org.telegram.messenger.LocaleController.getString(r7, r6);
        r0.setTextAndValue(r4, r6, r10);
        r0 = r1.linearLayout2;
        r4 = r1.detailSettingsCell;
        r4 = r4[r5];
        r0.addView(r4);
        r0 = r1.currentStep;
        r4 = 4;
        if (r0 != r4) goto L_0x15c8;
    L_0x15bc:
        r0 = r1.detailSettingsCell;
        r0 = r0[r5];
        r4 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$-_v7hZyhHE9vxsissEj7icTtuGM;
        r4.<init>(r1);
        r0.setOnClickListener(r4);
    L_0x15c8:
        r0 = 0;
        r4 = 0;
    L_0x15ca:
        r6 = r1.paymentForm;
        r6 = r6.users;
        r6 = r6.size();
        if (r0 >= r6) goto L_0x15ea;
    L_0x15d4:
        r6 = r1.paymentForm;
        r6 = r6.users;
        r6 = r6.get(r0);
        r6 = (org.telegram.tgnet.TLRPC.User) r6;
        r7 = r6.f534id;
        r8 = r1.paymentForm;
        r8 = r8.provider_id;
        if (r7 != r8) goto L_0x15e7;
    L_0x15e6:
        r4 = r6;
    L_0x15e7:
        r0 = r0 + 1;
        goto L_0x15ca;
    L_0x15ea:
        if (r4 == 0) goto L_0x1623;
    L_0x15ec:
        r0 = r1.detailSettingsCell;
        r6 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r6.<init>(r2);
        r7 = 1;
        r0[r7] = r6;
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r7);
        r0.setBackgroundDrawable(r6);
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = r4.first_name;
        r4 = r4.last_name;
        r4 = org.telegram.messenger.ContactsController.formatName(r6, r4);
        r6 = 2131560368; // 0x7f0d07b0 float:1.8746106E38 double:1.05313075E-314;
        r8 = "PaymentCheckoutProvider";
        r6 = org.telegram.messenger.LocaleController.getString(r8, r6);
        r0.setTextAndValue(r4, r6, r7);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r6 = r6[r7];
        r0.addView(r6);
        goto L_0x1625;
    L_0x1623:
        r4 = "";
    L_0x1625:
        r0 = r1.validateRequest;
        if (r0 == 0) goto L_0x177a;
    L_0x1629:
        r0 = r0.info;
        r0 = r0.shipping_address;
        if (r0 == 0) goto L_0x1684;
    L_0x162f:
        r6 = 6;
        r7 = new java.lang.Object[r6];
        r6 = r0.street_line1;
        r7[r5] = r6;
        r6 = r0.street_line2;
        r8 = 1;
        r7[r8] = r6;
        r6 = r0.city;
        r8 = 2;
        r7[r8] = r6;
        r6 = r0.state;
        r8 = 3;
        r7[r8] = r6;
        r6 = r0.country_iso2;
        r8 = 4;
        r7[r8] = r6;
        r0 = r0.post_code;
        r6 = 5;
        r7[r6] = r0;
        r0 = "%s %s, %s, %s, %s, %s";
        r0 = java.lang.String.format(r0, r7);
        r6 = r1.detailSettingsCell;
        r7 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r7.<init>(r2);
        r8 = 2;
        r6[r8] = r7;
        r6 = r1.detailSettingsCell;
        r6 = r6[r8];
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r6.setBackgroundColor(r7);
        r6 = r1.detailSettingsCell;
        r6 = r6[r8];
        r7 = 2131560389; // 0x7f0d07c5 float:1.8746149E38 double:1.05313076E-314;
        r10 = "PaymentShippingAddress";
        r7 = org.telegram.messenger.LocaleController.getString(r10, r7);
        r10 = 1;
        r6.setTextAndValue(r0, r7, r10);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r6 = r6[r8];
        r0.addView(r6);
    L_0x1684:
        r0 = r1.validateRequest;
        r0 = r0.info;
        r0 = r0.name;
        if (r0 == 0) goto L_0x16c1;
    L_0x168c:
        r0 = r1.detailSettingsCell;
        r6 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r6.<init>(r2);
        r7 = 3;
        r0[r7] = r6;
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r6);
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = r1.validateRequest;
        r6 = r6.info;
        r6 = r6.name;
        r8 = 2131560365; // 0x7f0d07ad float:1.87461E38 double:1.0531307484E-314;
        r10 = "PaymentCheckoutName";
        r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
        r10 = 1;
        r0.setTextAndValue(r6, r8, r10);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r6 = r6[r7];
        r0.addView(r6);
    L_0x16c1:
        r0 = r1.validateRequest;
        r0 = r0.info;
        r0 = r0.phone;
        if (r0 == 0) goto L_0x1707;
    L_0x16c9:
        r0 = r1.detailSettingsCell;
        r6 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r6.<init>(r2);
        r7 = 4;
        r0[r7] = r6;
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r6);
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.PhoneFormat.C0278PhoneFormat.getInstance();
        r7 = r1.validateRequest;
        r7 = r7.info;
        r7 = r7.phone;
        r6 = r6.format(r7);
        r7 = 2131560367; // 0x7f0d07af float:1.8746104E38 double:1.0531307494E-314;
        r8 = "PaymentCheckoutPhoneNumber";
        r7 = org.telegram.messenger.LocaleController.getString(r8, r7);
        r8 = 1;
        r0.setTextAndValue(r6, r7, r8);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r7 = 4;
        r6 = r6[r7];
        r0.addView(r6);
    L_0x1707:
        r0 = r1.validateRequest;
        r0 = r0.info;
        r0 = r0.email;
        if (r0 == 0) goto L_0x1744;
    L_0x170f:
        r0 = r1.detailSettingsCell;
        r6 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r6.<init>(r2);
        r7 = 5;
        r0[r7] = r6;
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r6);
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = r1.validateRequest;
        r6 = r6.info;
        r6 = r6.email;
        r8 = 2131560363; // 0x7f0d07ab float:1.8746096E38 double:1.0531307474E-314;
        r10 = "PaymentCheckoutEmail";
        r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
        r10 = 1;
        r0.setTextAndValue(r6, r8, r10);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r6 = r6[r7];
        r0.addView(r6);
    L_0x1744:
        r0 = r1.shippingOption;
        if (r0 == 0) goto L_0x177a;
    L_0x1748:
        r0 = r1.detailSettingsCell;
        r6 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r6.<init>(r2);
        r7 = 6;
        r0[r7] = r6;
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r15);
        r0.setBackgroundColor(r6);
        r0 = r1.detailSettingsCell;
        r0 = r0[r7];
        r6 = r1.shippingOption;
        r6 = r6.title;
        r8 = 2131560369; // 0x7f0d07b1 float:1.8746108E38 double:1.0531307504E-314;
        r10 = "PaymentCheckoutShippingMethod";
        r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
        r0.setTextAndValue(r6, r8, r5);
        r0 = r1.linearLayout2;
        r6 = r1.detailSettingsCell;
        r6 = r6[r7];
        r0.addView(r6);
    L_0x177a:
        r0 = r1.currentStep;
        r6 = 4;
        if (r0 != r6) goto L_0x1892;
    L_0x177f:
        r0 = new android.widget.FrameLayout;
        r0.<init>(r2);
        r1.bottomLayout = r0;
        r0 = r1.bottomLayout;
        r6 = 1;
        r7 = org.telegram.p004ui.ActionBar.Theme.getSelectorDrawable(r6);
        r0.setBackgroundDrawable(r7);
        r0 = r1.bottomLayout;
        r6 = 48;
        r7 = 80;
        r8 = -1;
        r6 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r8, r6, r7);
        r9.addView(r0, r6);
        r0 = r1.bottomLayout;
        r6 = new org.telegram.ui.-$$Lambda$PaymentFormActivity$UCCxWFA5zv52WCmPKPbwcSfjXPs;
        r6.<init>(r1, r4, r3);
        r0.setOnClickListener(r6);
        r0 = new android.widget.TextView;
        r0.<init>(r2);
        r1.payTextView = r0;
        r0 = r1.payTextView;
        r4 = "windowBackgroundWhiteBlueText6";
        r4 = org.telegram.p004ui.ActionBar.Theme.getColor(r4);
        r0.setTextColor(r4);
        r0 = r1.payTextView;
        r4 = 2131560366; // 0x7f0d07ae float:1.8746102E38 double:1.053130749E-314;
        r6 = 1;
        r7 = new java.lang.Object[r6];
        r7[r5] = r3;
        r3 = "PaymentCheckoutPay";
        r3 = org.telegram.messenger.LocaleController.formatString(r3, r4, r7);
        r0.setText(r3);
        r0 = r1.payTextView;
        r3 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0.setTextSize(r6, r3);
        r0 = r1.payTextView;
        r3 = 17;
        r0.setGravity(r3);
        r0 = r1.payTextView;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r0.setTypeface(r3);
        r0 = r1.bottomLayout;
        r3 = r1.payTextView;
        r4 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r6 = -1;
        r4 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r6, r4);
        r0.addView(r3, r4);
        r0 = new org.telegram.ui.Components.ContextProgressView;
        r0.<init>(r2, r5);
        r1.progressViewButton = r0;
        r0 = r1.progressViewButton;
        r3 = 4;
        r0.setVisibility(r3);
        r0 = r1.bottomLayout;
        r3 = r1.progressViewButton;
        r4 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r4 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r6, r4);
        r0.addView(r3, r4);
        r0 = new android.view.View;
        r0.<init>(r2);
        r3 = 2131165408; // 0x7f0700e0 float:1.7945032E38 double:1.0529356137E-314;
        r0.setBackgroundResource(r3);
        r19 = -1;
        r20 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r21 = 83;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r19, r20, r21, r22, r23, r24, r25);
        r9.addView(r0, r3);
        r0 = r1.doneItem;
        r0.setEnabled(r5);
        r0 = r1.doneItem;
        r0 = r0.getImageView();
        r3 = 4;
        r0.setVisibility(r3);
        r0 = new org.telegram.ui.PaymentFormActivity$13;
        r0.<init>(r2);
        r1.webView = r0;
        r0 = r1.webView;
        r3 = -1;
        r0.setBackgroundColor(r3);
        r0 = r1.webView;
        r0 = r0.getSettings();
        r3 = 1;
        r0.setJavaScriptEnabled(r3);
        r0 = r1.webView;
        r0 = r0.getSettings();
        r0.setDomStorageEnabled(r3);
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 21;
        if (r0 < r4) goto L_0x1875;
    L_0x1863:
        r0 = r1.webView;
        r0 = r0.getSettings();
        r0.setMixedContentMode(r5);
        r0 = android.webkit.CookieManager.getInstance();
        r4 = r1.webView;
        r0.setAcceptThirdPartyCookies(r4, r3);
    L_0x1875:
        r0 = r1.webView;
        r3 = new org.telegram.ui.PaymentFormActivity$14;
        r3.<init>();
        r0.setWebViewClient(r3);
        r0 = r1.webView;
        r3 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r4 = -1;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r4, r3);
        r9.addView(r0, r3);
        r0 = r1.webView;
        r3 = 8;
        r0.setVisibility(r3);
    L_0x1892:
        r0 = r1.sectionCell;
        r3 = new org.telegram.ui.Cells.ShadowSectionCell;
        r3.<init>(r2);
        r4 = 1;
        r0[r4] = r3;
        r0 = r1.sectionCell;
        r0 = r0[r4];
        r3 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
        r2 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r2, r3, r14);
        r0.setBackgroundDrawable(r2);
        r0 = r1.linearLayout2;
        r2 = r1.sectionCell;
        r2 = r2[r4];
        r3 = -2;
        r4 = -1;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r4, r3);
        r0.addView(r2, r3);
    L_0x18b9:
        r0 = r1.fragmentView;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PaymentFormActivity.createView(android.content.Context):android.view.View");
    }

    public /* synthetic */ boolean lambda$createView$1$PaymentFormActivity(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new C3796-$$Lambda$PaymentFormActivity$YL0G4SiCRRdBSsARre0gVqa3im4(this));
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    public /* synthetic */ void lambda$null$0$PaymentFormActivity(String str, String str2) {
        this.inputFields[4].setText(str);
        this.countryName = str2;
    }

    public /* synthetic */ boolean lambda$createView$2$PaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue();
            while (true) {
                intValue++;
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (intValue < editTextBoldCursorArr.length) {
                    if (intValue != 4 && ((View) editTextBoldCursorArr[intValue].getParent()).getVisibility() == 0) {
                        this.inputFields[intValue].requestFocus();
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        } else if (i != 6) {
            return false;
        } else {
            this.doneItem.performClick();
            return true;
        }
    }

    public /* synthetic */ void lambda$createView$3$PaymentFormActivity(View view) {
        this.saveShippingInfo ^= 1;
        this.checkCell1.setChecked(this.saveShippingInfo);
    }

    public /* synthetic */ void lambda$createView$4$PaymentFormActivity(View view) {
        this.saveCardInfo ^= 1;
        this.checkCell1.setChecked(this.saveCardInfo);
    }

    public /* synthetic */ boolean lambda$createView$6$PaymentFormActivity(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setCountrySelectActivityDelegate(new C3798-$$Lambda$PaymentFormActivity$c3rgfJXwF9E12z1XSDEQQZO6iNM(this));
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    public /* synthetic */ void lambda$null$5$PaymentFormActivity(String str, String str2) {
        this.inputFields[4].setText(str);
    }

    public /* synthetic */ boolean lambda$createView$7$PaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue();
            do {
                intValue++;
                if (intValue >= this.inputFields.length) {
                    break;
                } else if (intValue == 4) {
                    intValue++;
                }
            } while (((View) this.inputFields[intValue].getParent()).getVisibility() != 0);
            this.inputFields[intValue].requestFocus();
            return true;
        } else if (i != 6) {
            return false;
        } else {
            this.doneItem.performClick();
            return true;
        }
    }

    public /* synthetic */ void lambda$createView$8$PaymentFormActivity(View view) {
        this.saveCardInfo ^= 1;
        this.checkCell1.setChecked(this.saveCardInfo);
    }

    public /* synthetic */ void lambda$createView$9$PaymentFormActivity(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        int i = 0;
        while (true) {
            RadioCell[] radioCellArr = this.radioCells;
            if (i < radioCellArr.length) {
                radioCellArr[i].setChecked(intValue == i, true);
                i++;
            } else {
                return;
            }
        }
    }

    public /* synthetic */ boolean lambda$createView$11$PaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        this.doneItem.performClick();
        return true;
    }

    public /* synthetic */ void lambda$createView$12$PaymentFormActivity(View view) {
        this.passwordOk = false;
        goToNextStep();
    }

    public /* synthetic */ void lambda$createView$13$PaymentFormActivity(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.paymentForm, this.messageObject, 2, this.requestedInfo, this.shippingOption, null, this.cardName, this.validateRequest, this.saveCardInfo, null);
        paymentFormActivity.setDelegate(new C425912());
        presentFragment(paymentFormActivity);
    }

    public /* synthetic */ void lambda$createView$15$PaymentFormActivity(String str, String str2, View view) {
        User user = this.botUser;
        if (user == null || user.verified) {
            showPayAlert(str2);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("payment_warning_");
        stringBuilder.append(this.botUser.f534id);
        String stringBuilder2 = stringBuilder.toString();
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        if (notificationsSettings.getBoolean(stringBuilder2, false)) {
            showPayAlert(str2);
            return;
        }
        notificationsSettings.edit().putBoolean(stringBuilder2, true).commit();
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentWarning", C1067R.string.PaymentWarning));
        builder.setMessage(LocaleController.formatString("PaymentWarningText", C1067R.string.PaymentWarningText, this.currentBotName, str));
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1803-$$Lambda$PaymentFormActivity$MREN73dZdmfuVvIGQi8sgTjprGE(this, str2));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$14$PaymentFormActivity(String str, DialogInterface dialogInterface, int i) {
        showPayAlert(str);
    }

    public /* synthetic */ boolean lambda$createView$16$PaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        sendSavePassword(false);
        return true;
    }

    public /* synthetic */ void lambda$createView$18$PaymentFormActivity(View view) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_resendPasswordEmail(), C3799-$$Lambda$PaymentFormActivity$kpdlJs1QwZ9_5EeFfvuu3eWN4uE.INSTANCE);
        Builder builder = new Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("ResendCodeInfo", C1067R.string.ResendCodeInfo));
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$createView$20$PaymentFormActivity(View view) {
        Builder builder = new Builder(getParentActivity());
        CharSequence string = LocaleController.getString("TurnPasswordOffQuestion", C1067R.string.TurnPasswordOffQuestion);
        if (this.currentPassword.has_secure_values) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string);
            stringBuilder.append("\n\n");
            stringBuilder.append(LocaleController.getString("TurnPasswordOffPassport", C1067R.string.TurnPasswordOffPassport));
            string = stringBuilder.toString();
        }
        builder.setMessage(string);
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1810-$$Lambda$PaymentFormActivity$UsXOIe_Tc2OBHUXrtn6gfLKTiRQ(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$19$PaymentFormActivity(DialogInterface dialogInterface, int i) {
        sendSavePassword(true);
    }

    public /* synthetic */ boolean lambda$createView$21$PaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        }
        if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue();
            if (intValue == 0) {
                this.inputFields[1].requestFocus();
            } else if (intValue == 1) {
                this.inputFields[2].requestFocus();
            }
        }
        return false;
    }

    private void updatePasswordFields() {
        if (this.currentStep == 6 && this.bottomCell[2] != null) {
            int i = 0;
            this.doneItem.setVisibility(0);
            int i2;
            if (this.currentPassword == null) {
                showEditDoneProgress(true, true);
                this.bottomCell[2].setVisibility(8);
                this.settingsCell[0].setVisibility(8);
                this.settingsCell[1].setVisibility(8);
                this.codeFieldCell.setVisibility(8);
                this.headerCell[0].setVisibility(8);
                this.headerCell[1].setVisibility(8);
                this.bottomCell[0].setVisibility(8);
                for (i2 = 0; i2 < 3; i2++) {
                    ((View) this.inputFields[i2].getParent()).setVisibility(8);
                }
                while (i < this.dividers.size()) {
                    ((View) this.dividers.get(i)).setVisibility(8);
                    i++;
                }
                return;
            }
            showEditDoneProgress(true, false);
            if (this.waitingForEmail) {
                TextInfoPrivacyCell textInfoPrivacyCell = this.bottomCell[2];
                Object[] objArr = new Object[1];
                String str = this.currentPassword.email_unconfirmed_pattern;
                String str2 = "";
                if (str == null) {
                    str = str2;
                }
                objArr[0] = str;
                textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", C1067R.string.EmailPasswordConfirmText2, objArr));
                this.bottomCell[2].setVisibility(0);
                this.settingsCell[0].setVisibility(0);
                this.settingsCell[1].setVisibility(0);
                this.codeFieldCell.setVisibility(0);
                this.bottomCell[1].setText(str2);
                this.headerCell[0].setVisibility(8);
                this.headerCell[1].setVisibility(8);
                this.bottomCell[0].setVisibility(8);
                for (i2 = 0; i2 < 3; i2++) {
                    ((View) this.inputFields[i2].getParent()).setVisibility(8);
                }
                while (i < this.dividers.size()) {
                    ((View) this.dividers.get(i)).setVisibility(8);
                    i++;
                }
                return;
            }
            this.bottomCell[2].setVisibility(8);
            this.settingsCell[0].setVisibility(8);
            this.settingsCell[1].setVisibility(8);
            this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", C1067R.string.PaymentPasswordEmailInfo));
            this.codeFieldCell.setVisibility(8);
            this.headerCell[0].setVisibility(0);
            this.headerCell[1].setVisibility(0);
            this.bottomCell[0].setVisibility(0);
            for (i2 = 0; i2 < 3; i2++) {
                ((View) this.inputFields[i2].getParent()).setVisibility(0);
            }
            for (i2 = 0; i2 < this.dividers.size(); i2++) {
                ((View) this.dividers.get(i2)).setVisibility(0);
            }
        }
    }

    private void loadPasswordInfo() {
        if (!this.loadingPasswordInfo) {
            this.loadingPasswordInfo = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getPassword(), new C3804-$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR-HyfE(this), 10);
        }
    }

    public /* synthetic */ void lambda$loadPasswordInfo$24$PaymentFormActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1793-$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x-UeiB3WlNSgQ(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$23$PaymentFormActivity(TL_error tL_error, TLObject tLObject) {
        this.loadingPasswordInfo = false;
        if (tL_error == null) {
            this.currentPassword = (TL_account_password) tLObject;
            if (TwoStepVerificationActivity.canHandleCurrentPassword(this.currentPassword, false)) {
                TL_payments_paymentForm tL_payments_paymentForm = this.paymentForm;
                if (tL_payments_paymentForm != null && this.currentPassword.has_password) {
                    tL_payments_paymentForm.password_missing = false;
                    tL_payments_paymentForm.can_save_credentials = true;
                    updateSavePaymentField();
                }
                TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
                PaymentFormActivity paymentFormActivity = this.passwordFragment;
                if (paymentFormActivity != null) {
                    paymentFormActivity.setCurrentPassword(this.currentPassword);
                }
                if (!this.currentPassword.has_password && this.shortPollRunnable == null) {
                    this.shortPollRunnable = new C1798-$$Lambda$PaymentFormActivity$8V0l-YKYFLVQJz5pV-MyAIHFF4o(this);
                    AndroidUtilities.runOnUIThread(this.shortPollRunnable, 5000);
                }
            } else {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", C1067R.string.UpdateAppAlert), true);
            }
        }
    }

    public /* synthetic */ void lambda$null$22$PaymentFormActivity() {
        if (this.shortPollRunnable != null) {
            loadPasswordInfo();
            this.shortPollRunnable = null;
        }
    }

    private void showAlertWithText(String str, String str2) {
        Builder builder = new Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void showPayAlert(String str) {
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentTransactionReview", C1067R.string.PaymentTransactionReview));
        builder.setMessage(LocaleController.formatString("PaymentTransactionMessage", C1067R.string.PaymentTransactionMessage, str, this.currentBotName, this.currentItemName));
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1826-$$Lambda$PaymentFormActivity$zNiawUMZQLp4e5nGq2pzsvlUWzE(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$showPayAlert$25$PaymentFormActivity(DialogInterface dialogInterface, int i) {
        setDonePressed(true);
        sendData();
    }

    private String getTotalPriceString(ArrayList<TL_labeledPrice> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += ((TL_labeledPrice) arrayList.get(i)).amount;
        }
        return LocaleController.getInstance().formatCurrencyString(j, this.paymentForm.invoice.currency);
    }

    private String getTotalPriceDecimalString(ArrayList<TL_labeledPrice> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += ((TL_labeledPrice) arrayList.get(i)).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(j, this.paymentForm.invoice.currency, false);
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.paymentFinished);
        }
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
        if (paymentFormActivityDelegate != null) {
            paymentFormActivityDelegate.onFragmentDestroyed();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
        }
        WebView webView = this.webView;
        if (webView != null) {
            try {
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this.webView);
                }
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                this.webViewUrl = null;
                this.webView.destroy();
                this.webView = null;
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        try {
            if ((this.currentStep == 2 || this.currentStep == 6) && VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                getParentActivity().getWindow().clearFlags(MessagesController.UPDATE_MASK_CHAT);
            }
        } catch (Throwable e2) {
            FileLog.m30e(e2);
        }
        super.onFragmentDestroy();
        this.canceled = true;
    }

    /* Access modifiers changed, original: protected */
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z && !z2) {
            WebView webView = this.webView;
            if (webView == null) {
                int i = this.currentStep;
                if (i == 2) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard(this.inputFields[0]);
                } else if (i == 3) {
                    this.inputFields[1].requestFocus();
                    AndroidUtilities.showKeyboard(this.inputFields[1]);
                } else if (i == 6 && !this.waitingForEmail) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard(this.inputFields[0]);
                }
            } else if (this.currentStep != 4) {
                String str = this.paymentForm.url;
                this.webViewUrl = str;
                webView.loadUrl(str);
            }
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TL_payments_paymentForm tL_payments_paymentForm;
        if (i == NotificationCenter.didSetTwoStepPassword) {
            tL_payments_paymentForm = this.paymentForm;
            tL_payments_paymentForm.password_missing = false;
            tL_payments_paymentForm.can_save_credentials = true;
            updateSavePaymentField();
        } else if (i == NotificationCenter.didRemoveTwoStepPassword) {
            tL_payments_paymentForm = this.paymentForm;
            tL_payments_paymentForm.password_missing = true;
            tL_payments_paymentForm.can_save_credentials = false;
            updateSavePaymentField();
        } else if (i == NotificationCenter.paymentFinished) {
            removeSelfFromStack();
        }
    }

    private void goToNextStep() {
        int i = this.currentStep;
        int i2;
        if (i == 0) {
            TL_payments_paymentForm tL_payments_paymentForm = this.paymentForm;
            if (tL_payments_paymentForm.invoice.flexible) {
                i2 = 1;
            } else if (tL_payments_paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                i2 = UserConfig.getInstance(this.currentAccount).tmpPassword != null ? 4 : 3;
            } else {
                i2 = 2;
            }
            presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, i2, this.requestedInfo, null, null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
        } else if (i == 1) {
            if (this.paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                i2 = UserConfig.getInstance(this.currentAccount).tmpPassword != null ? 4 : 3;
            } else {
                i2 = 2;
            }
            presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, i2, this.requestedInfo, this.shippingOption, null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
        } else if (i == 2) {
            TL_payments_paymentForm tL_payments_paymentForm2 = this.paymentForm;
            if (tL_payments_paymentForm2.password_missing) {
                boolean z = this.saveCardInfo;
                if (z) {
                    this.passwordFragment = new PaymentFormActivity(tL_payments_paymentForm2, this.messageObject, 6, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, z, this.androidPayCredentials);
                    this.passwordFragment.setCurrentPassword(this.currentPassword);
                    this.passwordFragment.setDelegate(new C426017());
                    presentFragment(this.passwordFragment, this.isWebView);
                    return;
                }
            }
            PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
            if (paymentFormActivityDelegate != null) {
                paymentFormActivityDelegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials);
                finishFragment();
                return;
            }
            presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
        } else if (i == 3) {
            presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.passwordOk ? 4 : 2, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.passwordOk ^ 1);
        } else if (i == 4) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
            finishFragment();
        } else if (i != 6) {
        } else {
            if (this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials)) {
                finishFragment();
            } else {
                presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), true);
            }
        }
    }

    private void updateSavePaymentField() {
        if (this.bottomCell[0] != null && this.sectionCell[2] != null) {
            ShadowSectionCell[] shadowSectionCellArr;
            TL_payments_paymentForm tL_payments_paymentForm = this.paymentForm;
            boolean z = tL_payments_paymentForm.password_missing;
            String str = Theme.key_windowBackgroundGrayShadow;
            if (z || tL_payments_paymentForm.can_save_credentials) {
                WebView webView = this.webView;
                if (webView == null || !(webView == null || this.webviewLoading)) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", C1067R.string.PaymentCardSavePaymentInformationInfoLine1));
                    if (this.paymentForm.password_missing) {
                        loadPasswordInfo();
                        spannableStringBuilder.append("\n");
                        int length = spannableStringBuilder.length();
                        String string = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", C1067R.string.PaymentCardSavePaymentInformationInfoLine2);
                        int indexOf = string.indexOf(42);
                        int lastIndexOf = string.lastIndexOf(42);
                        spannableStringBuilder.append(string);
                        if (!(indexOf == -1 || lastIndexOf == -1)) {
                            indexOf += length;
                            lastIndexOf += length;
                            this.bottomCell[0].getTextView().setMovementMethod(new LinkMovementMethodMy());
                            string = "";
                            spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, string);
                            spannableStringBuilder.replace(indexOf, indexOf + 1, string);
                            spannableStringBuilder.setSpan(new LinkSpan(), indexOf, lastIndexOf - 1, 33);
                        }
                    }
                    this.checkCell1.setEnabled(true);
                    this.bottomCell[0].setText(spannableStringBuilder);
                    this.checkCell1.setVisibility(0);
                    this.bottomCell[0].setVisibility(0);
                    shadowSectionCellArr = this.sectionCell;
                    shadowSectionCellArr[2].setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCellArr[2].getContext(), (int) C1067R.C1065drawable.greydivider, str));
                    return;
                }
            }
            this.checkCell1.setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            shadowSectionCellArr = this.sectionCell;
            shadowSectionCellArr[2].setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCellArr[2].getContext(), (int) C1067R.C1065drawable.greydivider_bottom, str));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036 A:{Catch:{ Exception -> 0x009f }} */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0045 A:{Catch:{ Exception -> 0x009f }} */
    @android.annotation.SuppressLint({"HardwareIds"})
    public void fillNumber(java.lang.String r8) {
        /*
        r7 = this;
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x009f }
        r1 = "phone";
        r0 = r0.getSystemService(r1);	 Catch:{ Exception -> 0x009f }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x009f }
        r1 = 1;
        if (r8 != 0) goto L_0x0019;
    L_0x000d:
        r2 = r0.getSimState();	 Catch:{ Exception -> 0x009f }
        if (r2 == r1) goto L_0x00a3;
    L_0x0013:
        r2 = r0.getPhoneType();	 Catch:{ Exception -> 0x009f }
        if (r2 == 0) goto L_0x00a3;
    L_0x0019:
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x009f }
        r3 = 23;
        r4 = 0;
        if (r2 < r3) goto L_0x002f;
    L_0x0020:
        r2 = r7.getParentActivity();	 Catch:{ Exception -> 0x009f }
        r3 = "android.permission.READ_PHONE_STATE";
        r2 = r2.checkSelfPermission(r3);	 Catch:{ Exception -> 0x009f }
        if (r2 != 0) goto L_0x002d;
    L_0x002c:
        goto L_0x002f;
    L_0x002d:
        r2 = 0;
        goto L_0x0030;
    L_0x002f:
        r2 = 1;
    L_0x0030:
        if (r8 != 0) goto L_0x0034;
    L_0x0032:
        if (r2 == 0) goto L_0x00a3;
    L_0x0034:
        if (r8 != 0) goto L_0x003e;
    L_0x0036:
        r8 = r0.getLine1Number();	 Catch:{ Exception -> 0x009f }
        r8 = org.telegram.PhoneFormat.C0278PhoneFormat.stripExceptNumbers(r8);	 Catch:{ Exception -> 0x009f }
    L_0x003e:
        r0 = 0;
        r2 = android.text.TextUtils.isEmpty(r8);	 Catch:{ Exception -> 0x009f }
        if (r2 != 0) goto L_0x00a3;
    L_0x0045:
        r2 = r8.length();	 Catch:{ Exception -> 0x009f }
        r3 = 4;
        if (r2 <= r3) goto L_0x0084;
    L_0x004c:
        r2 = 8;
        if (r3 < r1) goto L_0x006f;
    L_0x0050:
        r5 = r8.substring(r4, r3);	 Catch:{ Exception -> 0x009f }
        r6 = r7.codesMap;	 Catch:{ Exception -> 0x009f }
        r6 = r6.get(r5);	 Catch:{ Exception -> 0x009f }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x009f }
        if (r6 == 0) goto L_0x006c;
    L_0x005e:
        r0 = r8.substring(r3);	 Catch:{ Exception -> 0x009f }
        r3 = r7.inputFields;	 Catch:{ Exception -> 0x009f }
        r3 = r3[r2];	 Catch:{ Exception -> 0x009f }
        r3.setText(r5);	 Catch:{ Exception -> 0x009f }
        r3 = r0;
        r0 = 1;
        goto L_0x0071;
    L_0x006c:
        r3 = r3 + -1;
        goto L_0x004c;
    L_0x006f:
        r3 = r0;
        r0 = 0;
    L_0x0071:
        if (r0 != 0) goto L_0x0083;
    L_0x0073:
        r0 = r8.substring(r1);	 Catch:{ Exception -> 0x009f }
        r3 = r7.inputFields;	 Catch:{ Exception -> 0x009f }
        r2 = r3[r2];	 Catch:{ Exception -> 0x009f }
        r8 = r8.substring(r4, r1);	 Catch:{ Exception -> 0x009f }
        r2.setText(r8);	 Catch:{ Exception -> 0x009f }
        goto L_0x0084;
    L_0x0083:
        r0 = r3;
    L_0x0084:
        if (r0 == 0) goto L_0x00a3;
    L_0x0086:
        r8 = r7.inputFields;	 Catch:{ Exception -> 0x009f }
        r1 = 9;
        r8 = r8[r1];	 Catch:{ Exception -> 0x009f }
        r8.setText(r0);	 Catch:{ Exception -> 0x009f }
        r8 = r7.inputFields;	 Catch:{ Exception -> 0x009f }
        r8 = r8[r1];	 Catch:{ Exception -> 0x009f }
        r0 = r7.inputFields;	 Catch:{ Exception -> 0x009f }
        r0 = r0[r1];	 Catch:{ Exception -> 0x009f }
        r0 = r0.length();	 Catch:{ Exception -> 0x009f }
        r8.setSelection(r0);	 Catch:{ Exception -> 0x009f }
        goto L_0x00a3;
    L_0x009f:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);
    L_0x00a3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PaymentFormActivity.fillNumber(java.lang.String):void");
    }

    private void sendSavePassword(boolean z) {
        if (z || this.codeFieldCell.getVisibility() != 0) {
            String str;
            String str2;
            TL_account_updatePasswordSettings tL_account_updatePasswordSettings = new TL_account_updatePasswordSettings();
            String str3 = "";
            if (z) {
                this.doneItem.setVisibility(0);
                tL_account_updatePasswordSettings.new_settings = new TL_account_passwordInputSettings();
                TL_account_passwordInputSettings tL_account_passwordInputSettings = tL_account_updatePasswordSettings.new_settings;
                tL_account_passwordInputSettings.flags = 2;
                tL_account_passwordInputSettings.email = str3;
                tL_account_updatePasswordSettings.password = new TL_inputCheckPasswordEmpty();
                str = null;
                str2 = str;
            } else {
                String obj = this.inputFields[0].getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    shakeField(0);
                    return;
                } else if (obj.equals(this.inputFields[1].getText().toString())) {
                    String obj2 = this.inputFields[2].getText().toString();
                    if (obj2.length() < 3) {
                        shakeField(2);
                        return;
                    }
                    int lastIndexOf = obj2.lastIndexOf(46);
                    int lastIndexOf2 = obj2.lastIndexOf(64);
                    if (lastIndexOf2 < 0 || lastIndexOf < lastIndexOf2) {
                        shakeField(2);
                        return;
                    }
                    tL_account_updatePasswordSettings.password = new TL_inputCheckPasswordEmpty();
                    tL_account_updatePasswordSettings.new_settings = new TL_account_passwordInputSettings();
                    TL_account_passwordInputSettings tL_account_passwordInputSettings2 = tL_account_updatePasswordSettings.new_settings;
                    tL_account_passwordInputSettings2.flags |= 1;
                    tL_account_passwordInputSettings2.hint = str3;
                    tL_account_passwordInputSettings2.new_algo = this.currentPassword.new_algo;
                    if (obj2.length() > 0) {
                        TL_account_passwordInputSettings tL_account_passwordInputSettings3 = tL_account_updatePasswordSettings.new_settings;
                        tL_account_passwordInputSettings3.flags = 2 | tL_account_passwordInputSettings3.flags;
                        tL_account_passwordInputSettings3.email = obj2.trim();
                    }
                    str2 = obj;
                    str = obj2;
                } else {
                    try {
                        Toast.makeText(getParentActivity(), LocaleController.getString("PasswordDoNotMatch", C1067R.string.PasswordDoNotMatch), 0).show();
                    } catch (Exception e) {
                        FileLog.m30e(e);
                    }
                    shakeField(1);
                    return;
                }
            }
            showEditDoneProgress(true, true);
            Utilities.globalQueue.postRunnable(new C1822-$$Lambda$PaymentFormActivity$v6Le3V4A5uKjdxXgwOCsgIj66wA(this, z, str, str2, tL_account_updatePasswordSettings));
        } else {
            String text = this.codeFieldCell.getText();
            if (text.length() == 0) {
                shakeView(this.codeFieldCell);
                return;
            }
            showEditDoneProgress(true, true);
            TL_account_confirmPasswordEmail tL_account_confirmPasswordEmail = new TL_account_confirmPasswordEmail();
            tL_account_confirmPasswordEmail.code = text;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_confirmPasswordEmail, new C3797-$$Lambda$PaymentFormActivity$YjpXWVCXfyYjoMEGmUOokMuT9II(this), 10);
        }
    }

    public /* synthetic */ void lambda$sendSavePassword$27$PaymentFormActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1818-$$Lambda$PaymentFormActivity$rp_a840DeP90rN9bDu7luNsoJKE(this, tL_error));
    }

    public /* synthetic */ void lambda$null$26$PaymentFormActivity(TL_error tL_error) {
        showEditDoneProgress(true, false);
        if (tL_error == null) {
            if (getParentActivity() != null) {
                Runnable runnable = this.shortPollRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.shortPollRunnable = null;
                }
                goToNextStep();
            }
        } else if (tL_error.text.startsWith("CODE_INVALID")) {
            shakeView(this.codeFieldCell);
            this.codeFieldCell.setText("", false);
        } else {
            String str = "AppName";
            if (tL_error.text.startsWith("FLOOD_WAIT")) {
                String formatPluralString;
                int intValue = Utilities.parseInt(tL_error.text).intValue();
                if (intValue < 60) {
                    formatPluralString = LocaleController.formatPluralString("Seconds", intValue);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60);
                }
                showAlertWithText(LocaleController.getString(str, C1067R.string.AppName), LocaleController.formatString("FloodWaitTime", C1067R.string.FloodWaitTime, formatPluralString));
            } else {
                showAlertWithText(LocaleController.getString(str, C1067R.string.AppName), tL_error.text);
            }
        }
    }

    public /* synthetic */ void lambda$null$32$PaymentFormActivity(boolean z, String str, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1819-$$Lambda$PaymentFormActivity$sxgdxN6G7g4EkMcZxwoY42jxlj4(this, tL_error, z, tLObject, str));
    }

    public /* synthetic */ void lambda$sendSavePassword$33$PaymentFormActivity(boolean z, String str, String str2, TL_account_updatePasswordSettings tL_account_updatePasswordSettings) {
        C3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA c3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA = new C3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA(this, z, str);
        if (z) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_updatePasswordSettings, c3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA, 10);
            return;
        }
        byte[] stringBytes = AndroidUtilities.getStringBytes(str2);
        PasswordKdfAlgo passwordKdfAlgo = this.currentPassword.new_algo;
        TL_error tL_error;
        if (passwordKdfAlgo instanceof C1158xb6caa888) {
            C1158xb6caa888 c1158xb6caa888 = (C1158xb6caa888) passwordKdfAlgo;
            tL_account_updatePasswordSettings.new_settings.new_password_hash = SRPHelper.getVBytes(stringBytes, c1158xb6caa888);
            if (tL_account_updatePasswordSettings.new_settings.new_password_hash == null) {
                tL_error = new TL_error();
                tL_error.text = "ALGO_INVALID";
                c3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA.run(null, tL_error);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_updatePasswordSettings, c3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA, 10);
            return;
        }
        tL_error = new TL_error();
        tL_error.text = "PASSWORD_HASH_INVALID";
        c3795-$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA.run(null, tL_error);
    }

    public /* synthetic */ void lambda$null$31$PaymentFormActivity(TL_error tL_error, boolean z, TLObject tLObject, String str) {
        if (tL_error != null) {
            if ("SRP_ID_INVALID".equals(tL_error.text)) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getPassword(), new C3802-$$Lambda$PaymentFormActivity$tcLMevZOApYaPGxte7o40Zbxk2c(this, z), 8);
                return;
            }
        }
        showEditDoneProgress(true, false);
        if (z) {
            TL_account_password tL_account_password = this.currentPassword;
            tL_account_password.has_password = false;
            tL_account_password.current_algo = null;
            this.delegate.currentPasswordUpdated(tL_account_password);
            finishFragment();
        } else if (tL_error == null && (tLObject instanceof TL_boolTrue)) {
            if (getParentActivity() != null) {
                goToNextStep();
            }
        } else if (tL_error != null) {
            if (tL_error.text.equals("EMAIL_UNCONFIRMED") || tL_error.text.startsWith("EMAIL_UNCONFIRMED_")) {
                this.emailCodeLength = Utilities.parseInt(tL_error.text).intValue();
                Builder builder = new Builder(getParentActivity());
                builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1799-$$Lambda$PaymentFormActivity$9D-zlo6Zt3tzxdsZk8Ai17yN6Vs(this, str));
                builder.setMessage(LocaleController.getString("YourEmailAlmostThereText", C1067R.string.YourEmailAlmostThereText));
                builder.setTitle(LocaleController.getString("YourEmailAlmostThere", C1067R.string.YourEmailAlmostThere));
                Dialog showDialog = showDialog(builder.create());
                if (showDialog != null) {
                    showDialog.setCanceledOnTouchOutside(false);
                    showDialog.setCancelable(false);
                }
            } else {
                str = "AppName";
                if (tL_error.text.equals("EMAIL_INVALID")) {
                    showAlertWithText(LocaleController.getString(str, C1067R.string.AppName), LocaleController.getString("PasswordEmailInvalid", C1067R.string.PasswordEmailInvalid));
                } else if (tL_error.text.startsWith("FLOOD_WAIT")) {
                    String formatPluralString;
                    int intValue = Utilities.parseInt(tL_error.text).intValue();
                    if (intValue < 60) {
                        formatPluralString = LocaleController.formatPluralString("Seconds", intValue);
                    } else {
                        formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60);
                    }
                    showAlertWithText(LocaleController.getString(str, C1067R.string.AppName), LocaleController.formatString("FloodWaitTime", C1067R.string.FloodWaitTime, formatPluralString));
                } else {
                    showAlertWithText(LocaleController.getString(str, C1067R.string.AppName), tL_error.text);
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$29$PaymentFormActivity(boolean z, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1805-$$Lambda$PaymentFormActivity$O97PWkThOP8HBp4dlwmRsHFZ4Cg(this, tL_error, tLObject, z));
    }

    public /* synthetic */ void lambda$null$28$PaymentFormActivity(TL_error tL_error, TLObject tLObject, boolean z) {
        if (tL_error == null) {
            this.currentPassword = (TL_account_password) tLObject;
            TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
            sendSavePassword(z);
        }
    }

    public /* synthetic */ void lambda$null$30$PaymentFormActivity(String str, DialogInterface dialogInterface, int i) {
        this.waitingForEmail = true;
        this.currentPassword.email_unconfirmed_pattern = str;
        updatePasswordFields();
    }

    private boolean sendCardData() {
        Integer parseInt;
        Integer num;
        String[] split = this.inputFields[1].getText().toString().split("/");
        if (split.length == 2) {
            Integer parseInt2 = Utilities.parseInt(split[0]);
            parseInt = Utilities.parseInt(split[1]);
            num = parseInt2;
        } else {
            num = null;
            parseInt = num;
        }
        Card card = new Card(this.inputFields[0].getText().toString(), num, parseInt, this.inputFields[3].getText().toString(), this.inputFields[2].getText().toString(), null, null, null, null, this.inputFields[5].getText().toString(), this.inputFields[4].getText().toString(), null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(card.getType());
        stringBuilder.append(" *");
        stringBuilder.append(card.getLast4());
        this.cardName = stringBuilder.toString();
        if (!card.validateNumber()) {
            shakeField(0);
            return false;
        } else if (!card.validateExpMonth() || !card.validateExpYear() || !card.validateExpiryDate()) {
            shakeField(1);
            return false;
        } else if (this.need_card_name && this.inputFields[2].length() == 0) {
            shakeField(2);
            return false;
        } else if (!card.validateCVC()) {
            shakeField(3);
            return false;
        } else if (this.need_card_country && this.inputFields[4].length() == 0) {
            shakeField(4);
            return false;
        } else if (this.need_card_postcode && this.inputFields[5].length() == 0) {
            shakeField(5);
            return false;
        } else {
            showEditDoneProgress(true, true);
            try {
                new Stripe(this.stripeApiKey).createToken(card, new C426118());
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            return true;
        }
    }

    private void sendForm() {
        if (!this.canceled) {
            TL_paymentRequestedInfo tL_paymentRequestedInfo;
            showEditDoneProgress(true, true);
            this.validateRequest = new TL_payments_validateRequestedInfo();
            TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo = this.validateRequest;
            tL_payments_validateRequestedInfo.save = this.saveShippingInfo;
            tL_payments_validateRequestedInfo.msg_id = this.messageObject.getId();
            this.validateRequest.info = new TL_paymentRequestedInfo();
            if (this.paymentForm.invoice.name_requested) {
                this.validateRequest.info.name = this.inputFields[6].getText().toString();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 1;
            }
            if (this.paymentForm.invoice.phone_requested) {
                tL_paymentRequestedInfo = this.validateRequest.info;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("+");
                stringBuilder.append(this.inputFields[8].getText().toString());
                stringBuilder.append(this.inputFields[9].getText().toString());
                tL_paymentRequestedInfo.phone = stringBuilder.toString();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 2;
            }
            if (this.paymentForm.invoice.email_requested) {
                this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 4;
            }
            if (this.paymentForm.invoice.shipping_address_requested) {
                this.validateRequest.info.shipping_address = new TL_postAddress();
                this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
                this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
                this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
                this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
                TL_postAddress tL_postAddress = this.validateRequest.info.shipping_address;
                String str = this.countryName;
                if (str == null) {
                    str = "";
                }
                tL_postAddress.country_iso2 = str;
                this.validateRequest.info.shipping_address.post_code = this.inputFields[5].getText().toString();
                TL_paymentRequestedInfo tL_paymentRequestedInfo2 = this.validateRequest.info;
                tL_paymentRequestedInfo2.flags |= 8;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(this.validateRequest, new C3794-$$Lambda$PaymentFormActivity$UHlY3SoIRXNL32kFvxEl3vmTkBw(this, this.validateRequest), 2);
        }
    }

    public /* synthetic */ void lambda$sendForm$37$PaymentFormActivity(TLObject tLObject, TLObject tLObject2, TL_error tL_error) {
        if (tLObject2 instanceof TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new C1795-$$Lambda$PaymentFormActivity$7Bb2ya9NV9z4zmrHXo2U4Oeqe_A(this, tLObject2));
        } else {
            AndroidUtilities.runOnUIThread(new C1813-$$Lambda$PaymentFormActivity$dqb1yiZBfWsIMg61J_sdvcJToMY(this, tL_error, tLObject));
        }
    }

    public /* synthetic */ void lambda$null$35$PaymentFormActivity(TLObject tLObject) {
        this.requestedInfo = (TL_payments_validatedRequestedInfo) tLObject;
        if (!(this.paymentForm.saved_info == null || this.saveShippingInfo)) {
            TL_payments_clearSavedInfo tL_payments_clearSavedInfo = new TL_payments_clearSavedInfo();
            tL_payments_clearSavedInfo.info = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_clearSavedInfo, C3800-$$Lambda$PaymentFormActivity$sO9JvHK-i_UoC8_6WynH4daVgdY.INSTANCE);
        }
        goToNextStep();
        setDonePressed(false);
        showEditDoneProgress(true, false);
    }

    public /* synthetic */ void lambda$null$36$PaymentFormActivity(org.telegram.tgnet.TLRPC.TL_error r12, org.telegram.tgnet.TLObject r13) {
        /*
        r11 = this;
        r0 = 0;
        r11.setDonePressed(r0);
        r1 = 1;
        r11.showEditDoneProgress(r1, r0);
        if (r12 == 0) goto L_0x00a7;
    L_0x000a:
        r2 = r12.text;
        r3 = -1;
        r4 = r2.hashCode();
        r5 = 3;
        r6 = 5;
        r7 = 2;
        r8 = 4;
        r9 = 7;
        r10 = 6;
        switch(r4) {
            case -2092780146: goto L_0x006c;
            case -1623547228: goto L_0x0062;
            case -1224177757: goto L_0x0058;
            case -1031752045: goto L_0x004e;
            case -274035920: goto L_0x0044;
            case 417441502: goto L_0x003a;
            case 708423542: goto L_0x0030;
            case 863965605: goto L_0x0025;
            case 889106340: goto L_0x001b;
            default: goto L_0x001a;
        };
    L_0x001a:
        goto L_0x0076;
    L_0x001b:
        r4 = "REQ_INFO_EMAIL_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0023:
        r2 = 2;
        goto L_0x0077;
    L_0x0025:
        r4 = "ADDRESS_STREET_LINE2_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x002d:
        r2 = 8;
        goto L_0x0077;
    L_0x0030:
        r4 = "REQ_INFO_PHONE_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0038:
        r2 = 1;
        goto L_0x0077;
    L_0x003a:
        r4 = "ADDRESS_STATE_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0042:
        r2 = 6;
        goto L_0x0077;
    L_0x0044:
        r4 = "ADDRESS_POSTCODE_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x004c:
        r2 = 5;
        goto L_0x0077;
    L_0x004e:
        r4 = "REQ_INFO_NAME_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0056:
        r2 = 0;
        goto L_0x0077;
    L_0x0058:
        r4 = "ADDRESS_COUNTRY_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0060:
        r2 = 3;
        goto L_0x0077;
    L_0x0062:
        r4 = "ADDRESS_STREET_LINE1_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x006a:
        r2 = 7;
        goto L_0x0077;
    L_0x006c:
        r4 = "ADDRESS_CITY_INVALID";
        r2 = r2.equals(r4);
        if (r2 == 0) goto L_0x0076;
    L_0x0074:
        r2 = 4;
        goto L_0x0077;
    L_0x0076:
        r2 = -1;
    L_0x0077:
        switch(r2) {
            case 0: goto L_0x00a4;
            case 1: goto L_0x009e;
            case 2: goto L_0x009a;
            case 3: goto L_0x0096;
            case 4: goto L_0x0092;
            case 5: goto L_0x008e;
            case 6: goto L_0x008a;
            case 7: goto L_0x0086;
            case 8: goto L_0x0082;
            default: goto L_0x007a;
        };
    L_0x007a:
        r1 = r11.currentAccount;
        r0 = new java.lang.Object[r0];
        org.telegram.p004ui.Components.AlertsCreator.processError(r1, r12, r11, r13, r0);
        goto L_0x00a7;
    L_0x0082:
        r11.shakeField(r1);
        goto L_0x00a7;
    L_0x0086:
        r11.shakeField(r0);
        goto L_0x00a7;
    L_0x008a:
        r11.shakeField(r5);
        goto L_0x00a7;
    L_0x008e:
        r11.shakeField(r6);
        goto L_0x00a7;
    L_0x0092:
        r11.shakeField(r7);
        goto L_0x00a7;
    L_0x0096:
        r11.shakeField(r8);
        goto L_0x00a7;
    L_0x009a:
        r11.shakeField(r9);
        goto L_0x00a7;
    L_0x009e:
        r12 = 9;
        r11.shakeField(r12);
        goto L_0x00a7;
    L_0x00a4:
        r11.shakeField(r10);
    L_0x00a7:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PaymentFormActivity.lambda$null$36$PaymentFormActivity(org.telegram.tgnet.TLRPC$TL_error, org.telegram.tgnet.TLObject):void");
    }

    private TL_paymentRequestedInfo getRequestInfo() {
        TL_paymentRequestedInfo tL_paymentRequestedInfo = new TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            tL_paymentRequestedInfo.name = this.inputFields[6].getText().toString();
            tL_paymentRequestedInfo.flags |= 1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("+");
            stringBuilder.append(this.inputFields[8].getText().toString());
            stringBuilder.append(this.inputFields[9].getText().toString());
            tL_paymentRequestedInfo.phone = stringBuilder.toString();
            tL_paymentRequestedInfo.flags |= 2;
        }
        if (this.paymentForm.invoice.email_requested) {
            tL_paymentRequestedInfo.email = this.inputFields[7].getText().toString().trim();
            tL_paymentRequestedInfo.flags |= 4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            tL_paymentRequestedInfo.shipping_address = new TL_postAddress();
            tL_paymentRequestedInfo.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            tL_paymentRequestedInfo.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            tL_paymentRequestedInfo.shipping_address.city = this.inputFields[2].getText().toString();
            tL_paymentRequestedInfo.shipping_address.state = this.inputFields[3].getText().toString();
            TL_postAddress tL_postAddress = tL_paymentRequestedInfo.shipping_address;
            String str = this.countryName;
            if (str == null) {
                str = "";
            }
            tL_postAddress.country_iso2 = str;
            tL_paymentRequestedInfo.shipping_address.post_code = this.inputFields[5].getText().toString();
            tL_paymentRequestedInfo.flags |= 8;
        }
        return tL_paymentRequestedInfo;
    }

    private void sendData() {
        if (!this.canceled) {
            showEditDoneProgress(false, true);
            TL_payments_sendPaymentForm tL_payments_sendPaymentForm = new TL_payments_sendPaymentForm();
            tL_payments_sendPaymentForm.msg_id = this.messageObject.getId();
            InputPaymentCredentials inputPaymentCredentials;
            if (UserConfig.getInstance(this.currentAccount).tmpPassword == null || this.paymentForm.saved_credentials == null) {
                TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay = this.androidPayCredentials;
                if (tL_inputPaymentCredentialsAndroidPay != null) {
                    tL_payments_sendPaymentForm.credentials = tL_inputPaymentCredentialsAndroidPay;
                } else {
                    tL_payments_sendPaymentForm.credentials = new TL_inputPaymentCredentials();
                    inputPaymentCredentials = tL_payments_sendPaymentForm.credentials;
                    inputPaymentCredentials.save = this.saveCardInfo;
                    inputPaymentCredentials.data = new TL_dataJSON();
                    tL_payments_sendPaymentForm.credentials.data.data = this.paymentJson;
                }
            } else {
                tL_payments_sendPaymentForm.credentials = new TL_inputPaymentCredentialsSaved();
                inputPaymentCredentials = tL_payments_sendPaymentForm.credentials;
                inputPaymentCredentials.f458id = this.paymentForm.saved_credentials.f521id;
                inputPaymentCredentials.tmp_password = UserConfig.getInstance(this.currentAccount).tmpPassword.tmp_password;
            }
            TL_payments_validatedRequestedInfo tL_payments_validatedRequestedInfo = this.requestedInfo;
            if (tL_payments_validatedRequestedInfo != null) {
                String str = tL_payments_validatedRequestedInfo.f522id;
                if (str != null) {
                    tL_payments_sendPaymentForm.requested_info_id = str;
                    tL_payments_sendPaymentForm.flags = 1 | tL_payments_sendPaymentForm.flags;
                }
            }
            TL_shippingOption tL_shippingOption = this.shippingOption;
            if (tL_shippingOption != null) {
                tL_payments_sendPaymentForm.shipping_option_id = tL_shippingOption.f529id;
                tL_payments_sendPaymentForm.flags |= 2;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_sendPaymentForm, new C3793-$$Lambda$PaymentFormActivity$GlqRsRVH4sCdpkPSqoUCnb5fPtQ(this, tL_payments_sendPaymentForm), 2);
        }
    }

    public /* synthetic */ void lambda$sendData$40$PaymentFormActivity(TL_payments_sendPaymentForm tL_payments_sendPaymentForm, TLObject tLObject, TL_error tL_error) {
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new C1801-$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70(this, tL_error, tL_payments_sendPaymentForm));
        } else if (tLObject instanceof TL_payments_paymentResult) {
            MessagesController.getInstance(this.currentAccount).processUpdates(((TL_payments_paymentResult) tLObject).updates, false);
            AndroidUtilities.runOnUIThread(new C1812-$$Lambda$PaymentFormActivity$bzw4OVkpOzFWzvKyIdhX50nRu7A(this));
        } else if (tLObject instanceof TL_payments_paymentVerficationNeeded) {
            AndroidUtilities.runOnUIThread(new C1802-$$Lambda$PaymentFormActivity$KLyhSsRtirfczI3lfwAKAEvcTeg(this, tLObject));
        }
    }

    public /* synthetic */ void lambda$null$38$PaymentFormActivity(TLObject tLObject) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
        setDonePressed(false);
        this.webView.setVisibility(0);
        this.webviewLoading = true;
        showEditDoneProgress(true, true);
        this.progressView.setVisibility(0);
        this.doneItem.setEnabled(false);
        this.doneItem.getImageView().setVisibility(4);
        WebView webView = this.webView;
        String str = ((TL_payments_paymentVerficationNeeded) tLObject).url;
        this.webViewUrl = str;
        webView.loadUrl(str);
    }

    public /* synthetic */ void lambda$null$39$PaymentFormActivity(TL_error tL_error, TL_payments_sendPaymentForm tL_payments_sendPaymentForm) {
        AlertsCreator.processError(this.currentAccount, tL_error, this, tL_payments_sendPaymentForm, new Object[0]);
        setDonePressed(false);
        showEditDoneProgress(false, false);
    }

    private void shakeField(int i) {
        shakeView(this.inputFields[i]);
    }

    private void shakeView(View view) {
        Vibrator vibrator = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
        AndroidUtilities.shakeView(view, 2.0f, 0);
    }

    private void setDonePressed(boolean z) {
        this.donePressed = z;
        this.swipeBackEnabled = z ^ 1;
        this.actionBar.getBackButton().setEnabled(this.donePressed ^ 1);
        TextDetailSettingsCell[] textDetailSettingsCellArr = this.detailSettingsCell;
        if (textDetailSettingsCellArr[0] != null) {
            textDetailSettingsCellArr[0].setEnabled(this.donePressed ^ 1);
        }
    }

    private void checkPassword() {
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
            UserConfig.getInstance(this.currentAccount).tmpPassword = null;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
            sendData();
        } else if (this.inputFields[1].length() == 0) {
            Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200);
            }
            AndroidUtilities.shakeView(this.inputFields[1], 2.0f, 0);
        } else {
            String obj = this.inputFields[1].getText().toString();
            showEditDoneProgress(true, true);
            setDonePressed(true);
            TL_account_getPassword tL_account_getPassword = new TL_account_getPassword();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getPassword, new C3801-$$Lambda$PaymentFormActivity$tY62nItwlzRcBMKPnqAwPnsARG0(this, obj, tL_account_getPassword), 2);
        }
    }

    public /* synthetic */ void lambda$checkPassword$45$PaymentFormActivity(String str, TL_account_getPassword tL_account_getPassword, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1806-$$Lambda$PaymentFormActivity$TcVVqZdUtRanjkIhF2Xby-5O01Q(this, tL_error, tLObject, str, tL_account_getPassword));
    }

    public /* synthetic */ void lambda$null$44$PaymentFormActivity(TL_error tL_error, TLObject tLObject, String str, TL_account_getPassword tL_account_getPassword) {
        if (tL_error == null) {
            TL_account_password tL_account_password = (TL_account_password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tL_account_password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", C1067R.string.UpdateAppAlert), true);
            } else if (tL_account_password.has_password) {
                Utilities.globalQueue.postRunnable(new C1816-$$Lambda$PaymentFormActivity$k9SEAszMH3-nDlyEt9fLhgGlMAA(this, tL_account_password, AndroidUtilities.getStringBytes(str)));
            } else {
                this.passwordOk = false;
                goToNextStep();
            }
        } else {
            AlertsCreator.processError(this.currentAccount, tL_error, this, tL_account_getPassword, new Object[0]);
            showEditDoneProgress(true, false);
            setDonePressed(false);
        }
    }

    public /* synthetic */ void lambda$null$43$PaymentFormActivity(TL_account_password tL_account_password, byte[] bArr) {
        PasswordKdfAlgo passwordKdfAlgo = tL_account_password.current_algo;
        bArr = passwordKdfAlgo instanceof C1158xb6caa888 ? SRPHelper.getX(bArr, (C1158xb6caa888) passwordKdfAlgo) : null;
        TL_account_getTmpPassword tL_account_getTmpPassword = new TL_account_getTmpPassword();
        tL_account_getTmpPassword.period = 1800;
        C3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac c3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac = new C3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac(this, tL_account_getTmpPassword);
        PasswordKdfAlgo passwordKdfAlgo2 = tL_account_password.current_algo;
        TL_error tL_error;
        if (passwordKdfAlgo2 instanceof C1158xb6caa888) {
            tL_account_getTmpPassword.password = SRPHelper.startCheck(bArr, tL_account_password.srp_id, tL_account_password.srp_B, (C1158xb6caa888) passwordKdfAlgo2);
            if (tL_account_getTmpPassword.password == null) {
                tL_error = new TL_error();
                tL_error.text = "ALGO_INVALID";
                c3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac.run(null, tL_error);
                return;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getTmpPassword, c3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac, 10);
        } else {
            tL_error = new TL_error();
            tL_error.text = "PASSWORD_HASH_INVALID";
            c3803-$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac.run(null, tL_error);
        }
    }

    public /* synthetic */ void lambda$null$42$PaymentFormActivity(TL_account_getTmpPassword tL_account_getTmpPassword, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1811-$$Lambda$PaymentFormActivity$WEnYGXUuB-UBrzR-7FlX-BgJs2A(this, tLObject, tL_error, tL_account_getTmpPassword));
    }

    public /* synthetic */ void lambda$null$41$PaymentFormActivity(TLObject tLObject, TL_error tL_error, TL_account_getTmpPassword tL_account_getTmpPassword) {
        showEditDoneProgress(true, false);
        setDonePressed(false);
        if (tLObject != null) {
            this.passwordOk = true;
            UserConfig.getInstance(this.currentAccount).tmpPassword = (TL_account_tmpPassword) tLObject;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            goToNextStep();
        } else if (tL_error.text.equals("PASSWORD_HASH_INVALID")) {
            Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200);
            }
            AndroidUtilities.shakeView(this.inputFields[1], 2.0f, 0);
            this.inputFields[1].setText("");
        } else {
            AlertsCreator.processError(this.currentAccount, tL_error, this, tL_account_getTmpPassword, new Object[0]);
        }
    }

    private void showEditDoneProgress(boolean z, boolean z2) {
        final boolean z3 = z2;
        AnimatorSet animatorSet = this.doneItemAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        String str = "alpha";
        String str2 = "scaleY";
        String str3 = "scaleX";
        AnimatorSet animatorSet2;
        Animator[] animatorArr;
        if (z && this.doneItem != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (z3) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                animatorSet2 = this.doneItemAnimation;
                Animator[] animatorArr2 = new Animator[6];
                animatorArr2[0] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str3, new float[]{0.1f});
                animatorArr2[1] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str2, new float[]{0.1f});
                animatorArr2[2] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str, new float[]{0.0f});
                animatorArr2[3] = ObjectAnimator.ofFloat(this.progressView, str3, new float[]{1.0f});
                animatorArr2[4] = ObjectAnimator.ofFloat(this.progressView, str2, new float[]{1.0f});
                animatorArr2[5] = ObjectAnimator.ofFloat(this.progressView, str, new float[]{1.0f});
                animatorSet2.playTogether(animatorArr2);
            } else if (this.webView != null) {
                animatorSet2 = this.doneItemAnimation;
                animatorArr = new Animator[3];
                animatorArr[0] = ObjectAnimator.ofFloat(this.progressView, str3, new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.progressView, str2, new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(this.progressView, str, new float[]{0.0f});
                animatorSet2.playTogether(animatorArr);
            } else {
                this.doneItem.getImageView().setVisibility(0);
                this.doneItem.setEnabled(true);
                animatorSet2 = this.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(this.progressView, str3, new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.progressView, str2, new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(this.progressView, str, new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str3, new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str2, new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(this.doneItem.getImageView(), str, new float[]{1.0f});
                animatorSet2.playTogether(animatorArr);
            }
            this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        if (z3) {
                            PaymentFormActivity.this.doneItem.getImageView().setVisibility(4);
                        } else {
                            PaymentFormActivity.this.progressView.setVisibility(4);
                        }
                    }
                }

                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            this.doneItemAnimation.setDuration(150);
            this.doneItemAnimation.start();
        } else if (this.payTextView != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (z3) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                animatorSet2 = this.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(this.payTextView, str3, new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.payTextView, str2, new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(this.payTextView, str, new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(this.progressViewButton, str3, new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(this.progressViewButton, str2, new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(this.progressViewButton, str, new float[]{1.0f});
                animatorSet2.playTogether(animatorArr);
            } else {
                this.payTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                animatorSet2 = this.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(this.progressViewButton, str3, new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.progressViewButton, str2, new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(this.progressViewButton, str, new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(this.payTextView, str3, new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(this.payTextView, str2, new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(this.payTextView, str, new float[]{1.0f});
                animatorSet2.playTogether(animatorArr);
            }
            this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        if (z3) {
                            PaymentFormActivity.this.payTextView.setVisibility(4);
                        } else {
                            PaymentFormActivity.this.progressViewButton.setVisibility(4);
                        }
                    }
                }

                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            this.doneItemAnimation.setDuration(150);
            this.doneItemAnimation.start();
        }
    }

    public boolean onBackPressed() {
        if (!this.shouldNavigateBack) {
            return this.donePressed ^ 1;
        }
        this.webView.loadUrl(this.webViewUrl);
        this.shouldNavigateBack = false;
        return false;
    }

    public ThemeDescription[] getThemeDescriptions() {
        int i;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        if (this.inputFields != null) {
            i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i >= editTextBoldCursorArr.length) {
                    break;
                }
                arrayList.add(new ThemeDescription((View) editTextBoldCursorArr[i].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.inputFields[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.inputFields[i], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
                i++;
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        }
        String str = "radioButton";
        String str2 = "textView";
        if (this.radioCells != null) {
            i = 0;
            while (true) {
                RadioCell[] radioCellArr = this.radioCells;
                if (i >= radioCellArr.length) {
                    break;
                }
                arrayList.add(new ThemeDescription(radioCellArr[i], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.radioCells[i], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
                arrayList.add(new ThemeDescription(this.radioCells[i], 0, new Class[]{RadioCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.radioCells[i], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{str}, null, null, null, Theme.key_radioBackground));
                arrayList.add(new ThemeDescription(this.radioCells[i], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{str}, null, null, null, Theme.key_radioBackgroundChecked));
                i++;
            }
        } else {
            arrayList.add(new ThemeDescription(null, 0, new Class[]{RadioCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{str}, null, null, null, Theme.key_radioBackground));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{str}, null, null, null, Theme.key_radioBackgroundChecked));
        }
        i = 0;
        while (true) {
            HeaderCell[] headerCellArr = this.headerCell;
            if (i >= headerCellArr.length) {
                break;
            }
            arrayList.add(new ThemeDescription(headerCellArr[i], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.headerCell[i], 0, new Class[]{HeaderCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));
            i++;
        }
        i = 0;
        while (true) {
            ShadowSectionCell[] shadowSectionCellArr = this.sectionCell;
            if (i >= shadowSectionCellArr.length) {
                break;
            }
            arrayList.add(new ThemeDescription(shadowSectionCellArr[i], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            i++;
        }
        i = 0;
        while (true) {
            TextInfoPrivacyCell[] textInfoPrivacyCellArr = this.bottomCell;
            if (i >= textInfoPrivacyCellArr.length) {
                break;
            }
            arrayList.add(new ThemeDescription(textInfoPrivacyCellArr[i], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(this.bottomCell[i], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4));
            arrayList.add(new ThemeDescription(this.bottomCell[i], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteLinkText));
            i++;
        }
        for (i = 0; i < this.dividers.size(); i++) {
            arrayList.add(new ThemeDescription((View) this.dividers.get(i), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        }
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        View view = this.codeFieldCell;
        View view2 = view;
        arrayList.add(new ThemeDescription(view2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        view = this.checkCell1;
        Class[] clsArr = new Class[]{TextCheckCell.class};
        String[] strArr = new String[1];
        strArr[0] = "checkBox";
        arrayList.add(new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        i = 0;
        while (true) {
            TextSettingsCell[] textSettingsCellArr = this.settingsCell;
            if (i >= textSettingsCellArr.length) {
                break;
            }
            arrayList.add(new ThemeDescription(textSettingsCellArr[i], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.settingsCell[i], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
            arrayList.add(new ThemeDescription(this.settingsCell[i], 0, new Class[]{TextSettingsCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            i++;
        }
        arrayList.add(new ThemeDescription(this.payTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText6));
        view = this.linearLayout2;
        view2 = view;
        arrayList.add(new ThemeDescription(view2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextPriceCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        view = this.linearLayout2;
        int i2 = ThemeDescription.FLAG_CHECKTAG;
        clsArr = new Class[]{TextPriceCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        arrayList.add(new ThemeDescription(view, i2, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        view = this.linearLayout2;
        View view3 = view;
        arrayList.add(new ThemeDescription(view3, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        i = 1;
        while (true) {
            TextDetailSettingsCell[] textDetailSettingsCellArr = this.detailSettingsCell;
            if (i < textDetailSettingsCellArr.length) {
                arrayList.add(new ThemeDescription(textDetailSettingsCellArr[i], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.detailSettingsCell[i], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{str2}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.detailSettingsCell[i], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{r7}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
                i++;
            } else {
                arrayList.add(new ThemeDescription(this.paymentInfoCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailExTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
                arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
                return (ThemeDescription[]) arrayList.toArray(new ThemeDescription[0]);
            }
        }
    }
}
