// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.webkit.JavascriptInterface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.ViewParent;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SRPHelper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.annotation.SuppressLint;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.net.Uri;
import org.telegram.ui.Cells.TextPriceCell;
import java.util.Collection;
import java.util.Calendar;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.graphics.Typeface;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.os.Build$VERSION;
import org.json.JSONObject;
import android.telephony.TelephonyManager;
import android.view.View$OnClickListener;
import org.telegram.messenger.ContactsController;
import android.widget.TextView$OnEditorActionListener;
import org.telegram.PhoneFormat.PhoneFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.HintEditText;
import android.graphics.Canvas;
import android.view.ViewGroup;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import android.text.method.MovementMethod;
import android.text.SpannableStringBuilder;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.widget.Toast;
import android.text.TextUtils;
import org.telegram.messenger.FileLog;
import java.util.Locale;
import com.stripe.android.model.Token;
import org.telegram.ui.Components.AlertsCreator;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.TokenCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import org.telegram.messenger.Utilities;
import android.view.MotionEvent;
import android.content.Context;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import android.os.Vibrator;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import android.webkit.WebView;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.widget.ScrollView;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Cells.PaymentInfoCell;
import android.widget.TextView;
import org.telegram.messenger.MessageObject;
import android.widget.LinearLayout;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.HeaderCell;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.view.View;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.tgnet.TLRPC;
import android.widget.FrameLayout;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PaymentFormActivity extends BaseFragment implements NotificationCenterDelegate
{
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
    private TLRPC.TL_inputPaymentCredentialsAndroidPay androidPayCredentials;
    private String androidPayPublicKey;
    private TLRPC.User botUser;
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
    private TLRPC.TL_account_password currentPassword;
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
    private TLRPC.TL_payments_paymentForm paymentForm;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private HashMap<String, String> phoneFormatMap;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private RadioCell[] radioCells;
    private TLRPC.TL_payments_validatedRequestedInfo requestedInfo;
    private boolean saveCardInfo;
    private boolean saveShippingInfo;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell[] settingsCell;
    private TLRPC.TL_shippingOption shippingOption;
    private Runnable shortPollRunnable;
    private boolean shouldNavigateBack;
    private String stripeApiKey;
    private TextView textView;
    private String totalPriceDecimal;
    private TLRPC.TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private String webViewUrl;
    private boolean webviewLoading;
    
    public PaymentFormActivity(final MessageObject messageObject, final TLRPC.TL_payments_paymentReceipt tl_payments_paymentReceipt) {
        this.countriesArray = new ArrayList<String>();
        this.countriesMap = new HashMap<String, String>();
        this.codesMap = new HashMap<String, String>();
        this.phoneFormatMap = new HashMap<String, String>();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<View>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        this.currentStep = 5;
        this.paymentForm = new TLRPC.TL_payments_paymentForm();
        final TLRPC.TL_payments_paymentForm paymentForm = this.paymentForm;
        paymentForm.bot_id = tl_payments_paymentReceipt.bot_id;
        paymentForm.invoice = tl_payments_paymentReceipt.invoice;
        paymentForm.provider_id = tl_payments_paymentReceipt.provider_id;
        paymentForm.users = tl_payments_paymentReceipt.users;
        this.shippingOption = tl_payments_paymentReceipt.shipping;
        this.messageObject = messageObject;
        this.botUser = MessagesController.getInstance(super.currentAccount).getUser(tl_payments_paymentReceipt.bot_id);
        final TLRPC.User botUser = this.botUser;
        if (botUser != null) {
            this.currentBotName = botUser.first_name;
        }
        else {
            this.currentBotName = "";
        }
        this.currentItemName = messageObject.messageOwner.media.title;
        if (tl_payments_paymentReceipt.info != null) {
            this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
            this.validateRequest.info = tl_payments_paymentReceipt.info;
        }
        this.cardName = tl_payments_paymentReceipt.credentials_title;
    }
    
    public PaymentFormActivity(final TLRPC.TL_payments_paymentForm tl_payments_paymentForm, final MessageObject messageObject) {
        this.countriesArray = new ArrayList<String>();
        this.countriesMap = new HashMap<String, String>();
        this.codesMap = new HashMap<String, String>();
        this.phoneFormatMap = new HashMap<String, String>();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<View>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        final TLRPC.TL_invoice invoice = tl_payments_paymentForm.invoice;
        final boolean shipping_address_requested = invoice.shipping_address_requested;
        int n2;
        final int n = n2 = 0;
        if (!shipping_address_requested) {
            n2 = n;
            if (!invoice.email_requested) {
                n2 = n;
                if (!invoice.name_requested) {
                    if (invoice.phone_requested) {
                        n2 = n;
                    }
                    else if (tl_payments_paymentForm.saved_credentials != null) {
                        if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
                            UserConfig.getInstance(super.currentAccount).tmpPassword = null;
                            UserConfig.getInstance(super.currentAccount).saveConfig(false);
                        }
                        if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
                            n2 = 4;
                        }
                        else {
                            n2 = 3;
                        }
                    }
                    else {
                        n2 = 2;
                    }
                }
            }
        }
        this.init(tl_payments_paymentForm, messageObject, n2, null, null, null, null, null, false, null);
    }
    
    private PaymentFormActivity(final TLRPC.TL_payments_paymentForm tl_payments_paymentForm, final MessageObject messageObject, final int n, final TLRPC.TL_payments_validatedRequestedInfo tl_payments_validatedRequestedInfo, final TLRPC.TL_shippingOption tl_shippingOption, final String s, final String s2, final TLRPC.TL_payments_validateRequestedInfo tl_payments_validateRequestedInfo, final boolean b, final TLRPC.TL_inputPaymentCredentialsAndroidPay tl_inputPaymentCredentialsAndroidPay) {
        this.countriesArray = new ArrayList<String>();
        this.countriesMap = new HashMap<String, String>();
        this.codesMap = new HashMap<String, String>();
        this.phoneFormatMap = new HashMap<String, String>();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<View>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.emailCodeLength = 6;
        this.init(tl_payments_paymentForm, messageObject, n, tl_payments_validatedRequestedInfo, tl_shippingOption, s, s2, tl_payments_validateRequestedInfo, b, tl_inputPaymentCredentialsAndroidPay);
    }
    
    private void checkPassword() {
        if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
            UserConfig.getInstance(super.currentAccount).tmpPassword = null;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
        }
        if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
            this.sendData();
            return;
        }
        if (this.inputFields[1].length() == 0) {
            final Vibrator vibrator = (Vibrator)ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView((View)this.inputFields[1], 2.0f, 0);
            return;
        }
        final String string = this.inputFields[1].getText().toString();
        this.showEditDoneProgress(true, true);
        this.setDonePressed(true);
        final TLRPC.TL_account_getPassword tl_account_getPassword = new TLRPC.TL_account_getPassword();
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_getPassword, new _$$Lambda$PaymentFormActivity$tY62nItwlzRcBMKPnqAwPnsARG0(this, string, tl_account_getPassword), 2);
    }
    
    private TLRPC.TL_paymentRequestedInfo getRequestInfo() {
        final TLRPC.TL_paymentRequestedInfo tl_paymentRequestedInfo = new TLRPC.TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            tl_paymentRequestedInfo.name = this.inputFields[6].getText().toString();
            tl_paymentRequestedInfo.flags |= 0x1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(this.inputFields[8].getText().toString());
            sb.append(this.inputFields[9].getText().toString());
            tl_paymentRequestedInfo.phone = sb.toString();
            tl_paymentRequestedInfo.flags |= 0x2;
        }
        if (this.paymentForm.invoice.email_requested) {
            tl_paymentRequestedInfo.email = this.inputFields[7].getText().toString().trim();
            tl_paymentRequestedInfo.flags |= 0x4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            tl_paymentRequestedInfo.shipping_address = new TLRPC.TL_postAddress();
            tl_paymentRequestedInfo.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            tl_paymentRequestedInfo.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            tl_paymentRequestedInfo.shipping_address.city = this.inputFields[2].getText().toString();
            tl_paymentRequestedInfo.shipping_address.state = this.inputFields[3].getText().toString();
            final TLRPC.TL_postAddress shipping_address = tl_paymentRequestedInfo.shipping_address;
            String countryName = this.countryName;
            if (countryName == null) {
                countryName = "";
            }
            shipping_address.country_iso2 = countryName;
            tl_paymentRequestedInfo.shipping_address.post_code = this.inputFields[5].getText().toString();
            tl_paymentRequestedInfo.flags |= 0x8;
        }
        return tl_paymentRequestedInfo;
    }
    
    private String getTotalPriceDecimalString(final ArrayList<TLRPC.TL_labeledPrice> list) {
        long n = 0L;
        for (int i = 0; i < list.size(); ++i) {
            n += list.get(i).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(n, this.paymentForm.invoice.currency, false);
    }
    
    private String getTotalPriceString(final ArrayList<TLRPC.TL_labeledPrice> list) {
        long n = 0L;
        for (int i = 0; i < list.size(); ++i) {
            n += list.get(i).amount;
        }
        return LocaleController.getInstance().formatCurrencyString(n, this.paymentForm.invoice.currency);
    }
    
    private void goToNextStep() {
        final int currentStep = this.currentStep;
        if (currentStep == 0) {
            final TLRPC.TL_payments_paymentForm paymentForm = this.paymentForm;
            int n;
            if (paymentForm.invoice.flexible) {
                n = 1;
            }
            else if (paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(super.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(super.currentAccount).saveConfig(false);
                }
                if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
                    n = 4;
                }
                else {
                    n = 3;
                }
            }
            else {
                n = 2;
            }
            this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, n, this.requestedInfo, null, null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
        }
        else if (currentStep == 1) {
            int n2;
            if (this.paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(super.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(super.currentAccount).saveConfig(false);
                }
                if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
                    n2 = 4;
                }
                else {
                    n2 = 3;
                }
            }
            else {
                n2 = 2;
            }
            this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, n2, this.requestedInfo, this.shippingOption, null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
        }
        else if (currentStep == 2) {
            final TLRPC.TL_payments_paymentForm paymentForm2 = this.paymentForm;
            if (paymentForm2.password_missing) {
                final boolean saveCardInfo = this.saveCardInfo;
                if (saveCardInfo) {
                    (this.passwordFragment = new PaymentFormActivity(paymentForm2, this.messageObject, 6, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, saveCardInfo, this.androidPayCredentials)).setCurrentPassword(this.currentPassword);
                    this.passwordFragment.setDelegate((PaymentFormActivityDelegate)new PaymentFormActivityDelegate() {
                        @Override
                        public void currentPasswordUpdated(final TLRPC.TL_account_password tl_account_password) {
                            PaymentFormActivity.this.currentPassword = tl_account_password;
                        }
                        
                        @Override
                        public boolean didSelectNewCard(final String s, final String s2, final boolean b, final TLRPC.TL_inputPaymentCredentialsAndroidPay tl_inputPaymentCredentialsAndroidPay) {
                            if (PaymentFormActivity.this.delegate != null) {
                                PaymentFormActivity.this.delegate.didSelectNewCard(s, s2, b, tl_inputPaymentCredentialsAndroidPay);
                            }
                            if (PaymentFormActivity.this.isWebView) {
                                PaymentFormActivity.this.removeSelfFromStack();
                            }
                            return PaymentFormActivity.this.delegate != null;
                        }
                        
                        @Override
                        public void onFragmentDestroyed() {
                            PaymentFormActivity.this.passwordFragment = null;
                        }
                    });
                    this.presentFragment(this.passwordFragment, this.isWebView);
                    return;
                }
            }
            final PaymentFormActivityDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials);
                this.finishFragment();
            }
            else {
                this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
            }
        }
        else if (currentStep == 3) {
            int n3;
            if (this.passwordOk) {
                n3 = 4;
            }
            else {
                n3 = 2;
            }
            this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, n3, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.passwordOk ^ true);
        }
        else if (currentStep == 4) {
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
            this.finishFragment();
        }
        else if (currentStep == 6) {
            if (!this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials)) {
                this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), true);
            }
            else {
                this.finishFragment();
            }
        }
    }
    
    private void init(final TLRPC.TL_payments_paymentForm paymentForm, final MessageObject messageObject, final int currentStep, final TLRPC.TL_payments_validatedRequestedInfo requestedInfo, final TLRPC.TL_shippingOption shippingOption, final String paymentJson, final String cardName, final TLRPC.TL_payments_validateRequestedInfo validateRequest, final boolean b, final TLRPC.TL_inputPaymentCredentialsAndroidPay androidPayCredentials) {
        this.currentStep = currentStep;
        this.paymentJson = paymentJson;
        this.androidPayCredentials = androidPayCredentials;
        this.requestedInfo = requestedInfo;
        this.paymentForm = paymentForm;
        this.shippingOption = shippingOption;
        this.messageObject = messageObject;
        this.saveCardInfo = b;
        final boolean equals = "stripe".equals(this.paymentForm.native_provider);
        final boolean b2 = true;
        this.isWebView = (equals ^ true);
        this.botUser = MessagesController.getInstance(super.currentAccount).getUser(paymentForm.bot_id);
        final TLRPC.User botUser = this.botUser;
        if (botUser != null) {
            this.currentBotName = botUser.first_name;
        }
        else {
            this.currentBotName = "";
        }
        this.currentItemName = messageObject.messageOwner.media.title;
        this.validateRequest = validateRequest;
        this.saveShippingInfo = true;
        if (b) {
            this.saveCardInfo = b;
        }
        else {
            this.saveCardInfo = (this.paymentForm.saved_credentials != null && b2);
        }
        if (cardName == null) {
            final TLRPC.TL_paymentSavedCredentialsCard saved_credentials = paymentForm.saved_credentials;
            if (saved_credentials != null) {
                this.cardName = saved_credentials.title;
            }
        }
        else {
            this.cardName = cardName;
        }
    }
    
    private void initAndroidPay(final Context context) {
    }
    
    private void loadPasswordInfo() {
        if (this.loadingPasswordInfo) {
            return;
        }
        this.loadingPasswordInfo = true;
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new _$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR_HyfE(this), 10);
    }
    
    private boolean sendCardData() {
        final String[] split = this.inputFields[1].getText().toString().split("/");
        Integer int1;
        Integer int2;
        if (split.length == 2) {
            int1 = Utilities.parseInt(split[0]);
            int2 = Utilities.parseInt(split[1]);
        }
        else {
            int1 = (int2 = null);
        }
        final Card card = new Card(this.inputFields[0].getText().toString(), int1, int2, this.inputFields[3].getText().toString(), this.inputFields[2].getText().toString(), null, null, null, null, this.inputFields[5].getText().toString(), this.inputFields[4].getText().toString(), null);
        final StringBuilder sb = new StringBuilder();
        sb.append(card.getType());
        sb.append(" *");
        sb.append(card.getLast4());
        this.cardName = sb.toString();
        if (!card.validateNumber()) {
            this.shakeField(0);
            return false;
        }
        if (!card.validateExpMonth() || !card.validateExpYear() || !card.validateExpiryDate()) {
            this.shakeField(1);
            return false;
        }
        if (this.need_card_name && this.inputFields[2].length() == 0) {
            this.shakeField(2);
            return false;
        }
        if (!card.validateCVC()) {
            this.shakeField(3);
            return false;
        }
        if (this.need_card_country && this.inputFields[4].length() == 0) {
            this.shakeField(4);
            return false;
        }
        if (this.need_card_postcode && this.inputFields[5].length() == 0) {
            this.shakeField(5);
            return false;
        }
        this.showEditDoneProgress(true, true);
        try {
            new Stripe(this.stripeApiKey).createToken(card, new TokenCallback() {
                @Override
                public void onError(final Exception ex) {
                    if (PaymentFormActivity.this.canceled) {
                        return;
                    }
                    PaymentFormActivity.this.showEditDoneProgress(true, false);
                    PaymentFormActivity.this.setDonePressed(false);
                    if (!(ex instanceof APIConnectionException) && !(ex instanceof APIException)) {
                        AlertsCreator.showSimpleToast(PaymentFormActivity.this, ex.getMessage());
                    }
                    else {
                        AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", 2131560372));
                    }
                }
                
                @Override
                public void onSuccess(final Token token) {
                    if (PaymentFormActivity.this.canceled) {
                        return;
                    }
                    PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", token.getType(), token.getId());
                    AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$18$QrVnMuLxiMtm_DJ7v5npJu9dxvA(this));
                }
            });
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return true;
    }
    
    private void sendData() {
        if (this.canceled) {
            return;
        }
        this.showEditDoneProgress(false, true);
        final TLRPC.TL_payments_sendPaymentForm tl_payments_sendPaymentForm = new TLRPC.TL_payments_sendPaymentForm();
        tl_payments_sendPaymentForm.msg_id = this.messageObject.getId();
        if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && this.paymentForm.saved_credentials != null) {
            tl_payments_sendPaymentForm.credentials = new TLRPC.TL_inputPaymentCredentialsSaved();
            final TLRPC.InputPaymentCredentials credentials = tl_payments_sendPaymentForm.credentials;
            credentials.id = this.paymentForm.saved_credentials.id;
            credentials.tmp_password = UserConfig.getInstance(super.currentAccount).tmpPassword.tmp_password;
        }
        else {
            final TLRPC.TL_inputPaymentCredentialsAndroidPay androidPayCredentials = this.androidPayCredentials;
            if (androidPayCredentials != null) {
                tl_payments_sendPaymentForm.credentials = androidPayCredentials;
            }
            else {
                tl_payments_sendPaymentForm.credentials = new TLRPC.TL_inputPaymentCredentials();
                final TLRPC.InputPaymentCredentials credentials2 = tl_payments_sendPaymentForm.credentials;
                credentials2.save = this.saveCardInfo;
                credentials2.data = new TLRPC.TL_dataJSON();
                tl_payments_sendPaymentForm.credentials.data.data = this.paymentJson;
            }
        }
        final TLRPC.TL_payments_validatedRequestedInfo requestedInfo = this.requestedInfo;
        if (requestedInfo != null) {
            final String id = requestedInfo.id;
            if (id != null) {
                tl_payments_sendPaymentForm.requested_info_id = id;
                tl_payments_sendPaymentForm.flags |= 0x1;
            }
        }
        final TLRPC.TL_shippingOption shippingOption = this.shippingOption;
        if (shippingOption != null) {
            tl_payments_sendPaymentForm.shipping_option_id = shippingOption.id;
            tl_payments_sendPaymentForm.flags |= 0x2;
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_payments_sendPaymentForm, new _$$Lambda$PaymentFormActivity$GlqRsRVH4sCdpkPSqoUCnb5fPtQ(this, tl_payments_sendPaymentForm), 2);
    }
    
    private void sendForm() {
        if (this.canceled) {
            return;
        }
        this.showEditDoneProgress(true, true);
        this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
        final TLRPC.TL_payments_validateRequestedInfo validateRequest = this.validateRequest;
        validateRequest.save = this.saveShippingInfo;
        validateRequest.msg_id = this.messageObject.getId();
        this.validateRequest.info = new TLRPC.TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            this.validateRequest.info.name = this.inputFields[6].getText().toString();
            final TLRPC.TL_paymentRequestedInfo info = this.validateRequest.info;
            info.flags |= 0x1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            final TLRPC.TL_paymentRequestedInfo info2 = this.validateRequest.info;
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(this.inputFields[8].getText().toString());
            sb.append(this.inputFields[9].getText().toString());
            info2.phone = sb.toString();
            final TLRPC.TL_paymentRequestedInfo info3 = this.validateRequest.info;
            info3.flags |= 0x2;
        }
        if (this.paymentForm.invoice.email_requested) {
            this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
            final TLRPC.TL_paymentRequestedInfo info4 = this.validateRequest.info;
            info4.flags |= 0x4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            this.validateRequest.info.shipping_address = new TLRPC.TL_postAddress();
            this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
            this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
            final TLRPC.TL_postAddress shipping_address = this.validateRequest.info.shipping_address;
            String countryName = this.countryName;
            if (countryName == null) {
                countryName = "";
            }
            shipping_address.country_iso2 = countryName;
            this.validateRequest.info.shipping_address.post_code = this.inputFields[5].getText().toString();
            final TLRPC.TL_paymentRequestedInfo info5 = this.validateRequest.info;
            info5.flags |= 0x8;
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(this.validateRequest, new _$$Lambda$PaymentFormActivity$UHlY3SoIRXNL32kFvxEl3vmTkBw(this, this.validateRequest), 2);
    }
    
    private void sendSavePassword(final boolean b) {
        if (!b && this.codeFieldCell.getVisibility() == 0) {
            final String text = this.codeFieldCell.getText();
            if (text.length() == 0) {
                this.shakeView((View)this.codeFieldCell);
                return;
            }
            this.showEditDoneProgress(true, true);
            final TLRPC.TL_account_confirmPasswordEmail tl_account_confirmPasswordEmail = new TLRPC.TL_account_confirmPasswordEmail();
            tl_account_confirmPasswordEmail.code = text;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_confirmPasswordEmail, new _$$Lambda$PaymentFormActivity$YjpXWVCXfyYjoMEGmUOokMuT9II(this), 10);
        }
        else {
            final TLRPC.TL_account_updatePasswordSettings tl_account_updatePasswordSettings = new TLRPC.TL_account_updatePasswordSettings();
            String string2;
            String string;
            if (b) {
                this.doneItem.setVisibility(0);
                tl_account_updatePasswordSettings.new_settings = new TLRPC.TL_account_passwordInputSettings();
                final TLRPC.TL_account_passwordInputSettings new_settings = tl_account_updatePasswordSettings.new_settings;
                new_settings.flags = 2;
                new_settings.email = "";
                tl_account_updatePasswordSettings.password = new TLRPC.TL_inputCheckPasswordEmpty();
                string = (string2 = null);
            }
            else {
                string2 = this.inputFields[0].getText().toString();
                if (TextUtils.isEmpty((CharSequence)string2)) {
                    this.shakeField(0);
                    return;
                }
                if (!string2.equals(this.inputFields[1].getText().toString())) {
                    try {
                        Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("PasswordDoNotMatch", 2131560346), 0).show();
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    this.shakeField(1);
                    return;
                }
                string = this.inputFields[2].getText().toString();
                if (string.length() < 3) {
                    this.shakeField(2);
                    return;
                }
                final int lastIndex = string.lastIndexOf(46);
                final int lastIndex2 = string.lastIndexOf(64);
                if (lastIndex2 < 0 || lastIndex < lastIndex2) {
                    this.shakeField(2);
                    return;
                }
                tl_account_updatePasswordSettings.password = new TLRPC.TL_inputCheckPasswordEmpty();
                tl_account_updatePasswordSettings.new_settings = new TLRPC.TL_account_passwordInputSettings();
                final TLRPC.TL_account_passwordInputSettings new_settings2 = tl_account_updatePasswordSettings.new_settings;
                new_settings2.flags |= 0x1;
                new_settings2.hint = "";
                new_settings2.new_algo = this.currentPassword.new_algo;
                if (string.length() > 0) {
                    final TLRPC.TL_account_passwordInputSettings new_settings3 = tl_account_updatePasswordSettings.new_settings;
                    new_settings3.flags |= 0x2;
                    new_settings3.email = string.trim();
                }
            }
            this.showEditDoneProgress(true, true);
            Utilities.globalQueue.postRunnable(new _$$Lambda$PaymentFormActivity$v6Le3V4A5uKjdxXgwOCsgIj66wA(this, b, string, string2, tl_account_updatePasswordSettings));
        }
    }
    
    private void setCurrentPassword(TLRPC.TL_account_password currentPassword) {
        if (currentPassword.has_password) {
            if (this.getParentActivity() == null) {
                return;
            }
            this.goToNextStep();
        }
        else {
            this.currentPassword = currentPassword;
            currentPassword = this.currentPassword;
            if (currentPassword != null) {
                this.waitingForEmail = (TextUtils.isEmpty((CharSequence)currentPassword.email_unconfirmed_pattern) ^ true);
            }
            this.updatePasswordFields();
        }
    }
    
    private void setDelegate(final PaymentFormActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private void setDonePressed(final boolean donePressed) {
        this.donePressed = donePressed;
        super.swipeBackEnabled = (donePressed ^ true);
        super.actionBar.getBackButton().setEnabled(this.donePressed ^ true);
        final TextDetailSettingsCell[] detailSettingsCell = this.detailSettingsCell;
        if (detailSettingsCell[0] != null) {
            detailSettingsCell[0].setEnabled(this.donePressed ^ true);
        }
    }
    
    private void shakeField(final int n) {
        this.shakeView((View)this.inputFields[n]);
    }
    
    private void shakeView(final View view) {
        final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView(view, 2.0f, 0);
    }
    
    private void showAlertWithText(final String title, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        builder.setTitle(title);
        builder.setMessage(message);
        this.showDialog(builder.create());
    }
    
    private void showEditDoneProgress(final boolean b, final boolean b2) {
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        if (b && this.doneItem != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (b2) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 1.0f }) });
            }
            else if (this.webView != null) {
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 0.0f }) });
            }
            else {
                this.doneItem.getImageView().setVisibility(0);
                this.doneItem.setEnabled(true);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 1.0f }) });
            }
            this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(obj)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(obj)) {
                        if (!b2) {
                            PaymentFormActivity.this.progressView.setVisibility(4);
                        }
                        else {
                            PaymentFormActivity.this.doneItem.getImageView().setVisibility(4);
                        }
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
        else if (this.payTextView != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (b2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "alpha", new float[] { 1.0f }) });
            }
            else {
                this.payTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.payTextView, "alpha", new float[] { 1.0f }) });
            }
            this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(obj)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(obj)) {
                        if (!b2) {
                            PaymentFormActivity.this.progressViewButton.setVisibility(4);
                        }
                        else {
                            PaymentFormActivity.this.payTextView.setVisibility(4);
                        }
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
    }
    
    private void showPayAlert(final String s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentTransactionReview", 2131560408));
        builder.setMessage(LocaleController.formatString("PaymentTransactionMessage", 2131560407, s, this.currentBotName, this.currentItemName));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PaymentFormActivity$zNiawUMZQLp4e5nGq2pzsvlUWzE(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        this.showDialog(builder.create());
    }
    
    private void updatePasswordFields() {
        if (this.currentStep == 6) {
            if (this.bottomCell[2] != null) {
                final ActionBarMenuItem doneItem = this.doneItem;
                final int n = 0;
                final int n2 = 0;
                doneItem.setVisibility(0);
                if (this.currentPassword == null) {
                    this.showEditDoneProgress(true, true);
                    this.bottomCell[2].setVisibility(8);
                    this.settingsCell[0].setVisibility(8);
                    this.settingsCell[1].setVisibility(8);
                    this.codeFieldCell.setVisibility(8);
                    this.headerCell[0].setVisibility(8);
                    this.headerCell[1].setVisibility(8);
                    this.bottomCell[0].setVisibility(8);
                    int n3 = 0;
                    int i;
                    while (true) {
                        i = n2;
                        if (n3 >= 3) {
                            break;
                        }
                        ((View)this.inputFields[n3].getParent()).setVisibility(8);
                        ++n3;
                    }
                    while (i < this.dividers.size()) {
                        this.dividers.get(i).setVisibility(8);
                        ++i;
                    }
                }
                else {
                    this.showEditDoneProgress(true, false);
                    if (this.waitingForEmail) {
                        final TextInfoPrivacyCell textInfoPrivacyCell = this.bottomCell[2];
                        String email_unconfirmed_pattern = this.currentPassword.email_unconfirmed_pattern;
                        if (email_unconfirmed_pattern == null) {
                            email_unconfirmed_pattern = "";
                        }
                        textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", 2131559329, email_unconfirmed_pattern));
                        this.bottomCell[2].setVisibility(0);
                        this.settingsCell[0].setVisibility(0);
                        this.settingsCell[1].setVisibility(0);
                        this.codeFieldCell.setVisibility(0);
                        this.bottomCell[1].setText("");
                        this.headerCell[0].setVisibility(8);
                        this.headerCell[1].setVisibility(8);
                        this.bottomCell[0].setVisibility(8);
                        int n4 = 0;
                        int j;
                        while (true) {
                            j = n;
                            if (n4 >= 3) {
                                break;
                            }
                            ((View)this.inputFields[n4].getParent()).setVisibility(8);
                            ++n4;
                        }
                        while (j < this.dividers.size()) {
                            this.dividers.get(j).setVisibility(8);
                            ++j;
                        }
                    }
                    else {
                        this.bottomCell[2].setVisibility(8);
                        this.settingsCell[0].setVisibility(8);
                        this.settingsCell[1].setVisibility(8);
                        this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", 2131560379));
                        this.codeFieldCell.setVisibility(8);
                        this.headerCell[0].setVisibility(0);
                        this.headerCell[1].setVisibility(0);
                        this.bottomCell[0].setVisibility(0);
                        for (int k = 0; k < 3; ++k) {
                            ((View)this.inputFields[k].getParent()).setVisibility(0);
                        }
                        for (int l = 0; l < this.dividers.size(); ++l) {
                            this.dividers.get(l).setVisibility(0);
                        }
                    }
                }
            }
        }
    }
    
    private void updateSavePaymentField() {
        if (this.bottomCell[0] != null) {
            if (this.sectionCell[2] != null) {
                final TLRPC.TL_payments_paymentForm paymentForm = this.paymentForm;
                if (paymentForm.password_missing || paymentForm.can_save_credentials) {
                    final WebView webView = this.webView;
                    if (webView == null || (webView != null && !this.webviewLoading)) {
                        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", 2131560359));
                        if (this.paymentForm.password_missing) {
                            this.loadPasswordInfo();
                            text.append((CharSequence)"\n");
                            final int length = text.length();
                            final String string = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", 2131560360);
                            final int index = string.indexOf(42);
                            final int lastIndex = string.lastIndexOf(42);
                            text.append((CharSequence)string);
                            if (index != -1 && lastIndex != -1) {
                                final int n = index + length;
                                final int n2 = lastIndex + length;
                                this.bottomCell[0].getTextView().setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
                                text.replace(n2, n2 + 1, (CharSequence)"");
                                text.replace(n, n + 1, (CharSequence)"");
                                text.setSpan((Object)new LinkSpan(), n, n2 - 1, 33);
                            }
                        }
                        this.checkCell1.setEnabled(true);
                        this.bottomCell[0].setText((CharSequence)text);
                        this.checkCell1.setVisibility(0);
                        this.bottomCell[0].setVisibility(0);
                        final ShadowSectionCell[] sectionCell = this.sectionCell;
                        sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawable(sectionCell[2].getContext(), 2131165394, "windowBackgroundGrayShadow"));
                        return;
                    }
                }
                this.checkCell1.setVisibility(8);
                this.bottomCell[0].setVisibility(8);
                final ShadowSectionCell[] sectionCell2 = this.sectionCell;
                sectionCell2[2].setBackgroundDrawable(Theme.getThemedDrawable(sectionCell2[2].getContext(), 2131165395, "windowBackgroundGrayShadow"));
            }
        }
    }
    
    @SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
    @Override
    public View createView(final Context context) {
        final int currentStep = this.currentStep;
        if (currentStep == 0) {
            super.actionBar.setTitle(LocaleController.getString("PaymentShippingInfo", 2131560395));
        }
        else if (currentStep == 1) {
            super.actionBar.setTitle(LocaleController.getString("PaymentShippingMethod", 2131560396));
        }
        else if (currentStep == 2) {
            super.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", 2131560355));
        }
        else if (currentStep == 3) {
            super.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", 2131560355));
        }
        else if (currentStep == 4) {
            if (this.paymentForm.invoice.test) {
                final ActionBar actionBar = super.actionBar;
                final StringBuilder sb = new StringBuilder();
                sb.append("Test ");
                sb.append(LocaleController.getString("PaymentCheckout", 2131560362));
                actionBar.setTitle(sb.toString());
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("PaymentCheckout", 2131560362));
            }
        }
        else if (currentStep == 5) {
            if (this.paymentForm.invoice.test) {
                final ActionBar actionBar2 = super.actionBar;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Test ");
                sb2.append(LocaleController.getString("PaymentReceipt", 2131560388));
                actionBar2.setTitle(sb2.toString());
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("PaymentReceipt", 2131560388));
            }
        }
        else if (currentStep == 6) {
            super.actionBar.setTitle(LocaleController.getString("PaymentPassword", 2131560377));
        }
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    if (PaymentFormActivity.this.donePressed) {
                        return;
                    }
                    PaymentFormActivity.this.finishFragment();
                }
                else if (i == 1) {
                    if (PaymentFormActivity.this.donePressed) {
                        return;
                    }
                    if (PaymentFormActivity.this.currentStep != 3) {
                        AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
                    }
                    if (PaymentFormActivity.this.currentStep == 0) {
                        PaymentFormActivity.this.setDonePressed(true);
                        PaymentFormActivity.this.sendForm();
                    }
                    else {
                        final int access$400 = PaymentFormActivity.this.currentStep;
                        i = 0;
                        if (access$400 == 1) {
                            while (i < PaymentFormActivity.this.radioCells.length) {
                                if (PaymentFormActivity.this.radioCells[i].isChecked()) {
                                    final PaymentFormActivity this$0 = PaymentFormActivity.this;
                                    this$0.shippingOption = this$0.requestedInfo.shipping_options.get(i);
                                    break;
                                }
                                ++i;
                            }
                            PaymentFormActivity.this.goToNextStep();
                        }
                        else if (PaymentFormActivity.this.currentStep == 2) {
                            PaymentFormActivity.this.sendCardData();
                        }
                        else if (PaymentFormActivity.this.currentStep == 3) {
                            PaymentFormActivity.this.checkPassword();
                        }
                        else if (PaymentFormActivity.this.currentStep == 6) {
                            PaymentFormActivity.this.sendSavePassword(false);
                        }
                    }
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        final int currentStep2 = this.currentStep;
        if (currentStep2 == 0 || currentStep2 == 1 || currentStep2 == 2 || currentStep2 == 3 || currentStep2 == 4 || currentStep2 == 6) {
            this.doneItem = menu.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
            (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
            this.doneItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        super.fragmentView = (View)new FrameLayout(context);
        final View fragmentView = super.fragmentView;
        final FrameLayout frameLayout = (FrameLayout)fragmentView;
        fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.scrollView = new ScrollView(context)).setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
        final ScrollView scrollView = this.scrollView;
        float n;
        if (this.currentStep == 4) {
            n = 48.0f;
        }
        else {
            n = 0.0f;
        }
        frameLayout.addView((View)scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, n));
        (this.linearLayout2 = new LinearLayout(context)).setOrientation(1);
        this.scrollView.addView((View)this.linearLayout2, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        final int currentStep3 = this.currentStep;
        if (currentStep3 == 0) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    final String[] split = line.split(";");
                    this.countriesArray.add(0, split[2]);
                    this.countriesMap.put(split[2], split[0]);
                    this.codesMap.put(split[0], split[2]);
                    hashMap2.put(split[1], split[2]);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], split[3]);
                    }
                    hashMap.put(split[1], split[2]);
                }
                bufferedReader.close();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            Collections.sort(this.countriesArray, (Comparator<? super String>)_$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
            this.inputFields = new EditTextBoldCursor[10];
            for (int i = 0; i < 10; ++i) {
                if (i == 0) {
                    (this.headerCell[0] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.headerCell[0].setText(LocaleController.getString("PaymentShippingAddress", 2131560389));
                    this.linearLayout2.addView((View)this.headerCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                }
                else if (i == 6) {
                    this.sectionCell[0] = new ShadowSectionCell(context);
                    this.linearLayout2.addView((View)this.sectionCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    (this.headerCell[1] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.headerCell[1].setText(LocaleController.getString("PaymentShippingReceiver", 2131560399));
                    this.linearLayout2.addView((View)this.headerCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                }
                Object o;
                if (i == 8) {
                    o = new LinearLayout(context);
                    ((LinearLayout)o).setOrientation(0);
                    this.linearLayout2.addView((View)o, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                    ((ViewGroup)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
                else if (i == 9) {
                    o = this.inputFields[8].getParent();
                }
                else {
                    final FrameLayout frameLayout2 = new FrameLayout(context);
                    this.linearLayout2.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                    ((ViewGroup)frameLayout2).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    boolean b2;
                    final boolean b = b2 = (i != 5 && i != 9);
                    Label_1211: {
                        if (b) {
                            if (i != 7 || this.paymentForm.invoice.phone_requested) {
                                b2 = b;
                                if (i != 6) {
                                    break Label_1211;
                                }
                                final TLRPC.TL_invoice invoice = this.paymentForm.invoice;
                                b2 = b;
                                if (invoice.phone_requested) {
                                    break Label_1211;
                                }
                                b2 = b;
                                if (invoice.email_requested) {
                                    break Label_1211;
                                }
                            }
                            b2 = false;
                        }
                    }
                    o = frameLayout2;
                    if (b2) {
                        final View e = new View(context) {
                            protected void onDraw(final Canvas canvas) {
                                float n;
                                if (LocaleController.isRTL) {
                                    n = 0.0f;
                                }
                                else {
                                    n = (float)AndroidUtilities.dp(20.0f);
                                }
                                final float n2 = (float)(this.getMeasuredHeight() - 1);
                                final int measuredWidth = this.getMeasuredWidth();
                                int dp;
                                if (LocaleController.isRTL) {
                                    dp = AndroidUtilities.dp(20.0f);
                                }
                                else {
                                    dp = 0;
                                }
                                canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                            }
                        };
                        e.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.dividers.add(e);
                        ((ViewGroup)frameLayout2).addView((View)e, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
                        o = frameLayout2;
                    }
                }
                if (i == 9) {
                    this.inputFields[i] = new HintEditText(context);
                }
                else {
                    this.inputFields[i] = new EditTextBoldCursor(context);
                }
                this.inputFields[i].setTag((Object)i);
                this.inputFields[i].setTextSize(1, 16.0f);
                this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[i].setBackgroundDrawable((Drawable)null);
                this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i].setCursorWidth(1.5f);
                if (i == 4) {
                    this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PaymentFormActivity$uDw_faglZkNLuTGLVBNZMQdnOLU(this));
                    this.inputFields[i].setInputType(0);
                }
                if (i != 9 && i != 8) {
                    if (i == 7) {
                        this.inputFields[i].setInputType(1);
                    }
                    else {
                        this.inputFields[i].setInputType(16385);
                    }
                }
                else {
                    this.inputFields[i].setInputType(3);
                }
                this.inputFields[i].setImeOptions(268435461);
                switch (i) {
                    case 7: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingEmailPlaceholder", 2131560394));
                        final TLRPC.TL_paymentRequestedInfo saved_info = this.paymentForm.saved_info;
                        if (saved_info == null) {
                            break;
                        }
                        final String email = saved_info.email;
                        if (email != null) {
                            this.inputFields[i].setText((CharSequence)email);
                            break;
                        }
                        break;
                    }
                    case 6: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingName", 2131560397));
                        final TLRPC.TL_paymentRequestedInfo saved_info2 = this.paymentForm.saved_info;
                        if (saved_info2 == null) {
                            break;
                        }
                        final String name = saved_info2.name;
                        if (name != null) {
                            this.inputFields[i].setText((CharSequence)name);
                            break;
                        }
                        break;
                    }
                    case 5: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingZipPlaceholder", 2131560403));
                        final TLRPC.TL_paymentRequestedInfo saved_info3 = this.paymentForm.saved_info;
                        if (saved_info3 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address = saved_info3.shipping_address;
                        if (shipping_address != null) {
                            this.inputFields[i].setText((CharSequence)shipping_address.post_code);
                            break;
                        }
                        break;
                    }
                    case 4: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingCountry", 2131560393));
                        final TLRPC.TL_paymentRequestedInfo saved_info4 = this.paymentForm.saved_info;
                        if (saved_info4 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address2 = saved_info4.shipping_address;
                        if (shipping_address2 != null) {
                            String countryName = hashMap2.get(shipping_address2.country_iso2);
                            this.countryName = this.paymentForm.saved_info.shipping_address.country_iso2;
                            final EditTextBoldCursor editTextBoldCursor = this.inputFields[i];
                            if (countryName == null) {
                                countryName = this.countryName;
                            }
                            editTextBoldCursor.setText((CharSequence)countryName);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingStatePlaceholder", 2131560402));
                        final TLRPC.TL_paymentRequestedInfo saved_info5 = this.paymentForm.saved_info;
                        if (saved_info5 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address3 = saved_info5.shipping_address;
                        if (shipping_address3 != null) {
                            this.inputFields[i].setText((CharSequence)shipping_address3.state);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingCityPlaceholder", 2131560392));
                        final TLRPC.TL_paymentRequestedInfo saved_info6 = this.paymentForm.saved_info;
                        if (saved_info6 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address4 = saved_info6.shipping_address;
                        if (shipping_address4 != null) {
                            this.inputFields[i].setText((CharSequence)shipping_address4.city);
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingAddress2Placeholder", 2131560391));
                        final TLRPC.TL_paymentRequestedInfo saved_info7 = this.paymentForm.saved_info;
                        if (saved_info7 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address5 = saved_info7.shipping_address;
                        if (shipping_address5 != null) {
                            this.inputFields[i].setText((CharSequence)shipping_address5.street_line2);
                            break;
                        }
                        break;
                    }
                    case 0: {
                        this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingAddress1Placeholder", 2131560390));
                        final TLRPC.TL_paymentRequestedInfo saved_info8 = this.paymentForm.saved_info;
                        if (saved_info8 == null) {
                            break;
                        }
                        final TLRPC.TL_postAddress shipping_address6 = saved_info8.shipping_address;
                        if (shipping_address6 != null) {
                            this.inputFields[i].setText((CharSequence)shipping_address6.street_line1);
                            break;
                        }
                        break;
                    }
                }
                final EditTextBoldCursor[] inputFields = this.inputFields;
                inputFields[i].setSelection(inputFields[i].length());
                if (i == 8) {
                    (this.textView = new TextView(context)).setText((CharSequence)"+");
                    this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.textView.setTextSize(1, 16.0f);
                    ((ViewGroup)o).addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                    this.inputFields[i].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                    this.inputFields[i].setGravity(19);
                    this.inputFields[i].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(5) });
                    ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        public void afterTextChanged(final Editable editable) {
                            if (PaymentFormActivity.this.ignoreOnTextChange) {
                                return;
                            }
                            final PaymentFormActivity this$0 = PaymentFormActivity.this;
                            boolean b = true;
                            this$0.ignoreOnTextChange = true;
                            String s = PhoneFormat.stripExceptNumbers(PaymentFormActivity.this.inputFields[8].getText().toString());
                            PaymentFormActivity.this.inputFields[8].setText((CharSequence)s);
                            final HintEditText hintEditText = (HintEditText)PaymentFormActivity.this.inputFields[9];
                            if (s.length() == 0) {
                                hintEditText.setHintText(null);
                                hintEditText.setHint((CharSequence)LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                            }
                            else {
                                final int length = s.length();
                                int i = 4;
                                String text = null;
                                int n = 0;
                                Label_0323: {
                                    if (length > 4) {
                                        while (true) {
                                            while (i >= 1) {
                                                final String substring = s.substring(0, i);
                                                if (PaymentFormActivity.this.codesMap.get(substring) != null) {
                                                    final StringBuilder sb = new StringBuilder();
                                                    sb.append(s.substring(i));
                                                    sb.append(PaymentFormActivity.this.inputFields[9].getText().toString());
                                                    text = sb.toString();
                                                    PaymentFormActivity.this.inputFields[8].setText((CharSequence)substring);
                                                    n = 1;
                                                    s = substring;
                                                    if (n == 0) {
                                                        final StringBuilder sb2 = new StringBuilder();
                                                        sb2.append(s.substring(1));
                                                        sb2.append(PaymentFormActivity.this.inputFields[9].getText().toString());
                                                        text = sb2.toString();
                                                        final EditTextBoldCursor editTextBoldCursor = PaymentFormActivity.this.inputFields[8];
                                                        s = s.substring(0, 1);
                                                        editTextBoldCursor.setText((CharSequence)s);
                                                    }
                                                    break Label_0323;
                                                }
                                                else {
                                                    --i;
                                                }
                                            }
                                            text = null;
                                            n = 0;
                                            continue;
                                        }
                                    }
                                    text = null;
                                    n = 0;
                                }
                                final String o = PaymentFormActivity.this.codesMap.get(s);
                                Label_0402: {
                                    if (o != null && PaymentFormActivity.this.countriesArray.indexOf(o) != -1) {
                                        final String s2 = PaymentFormActivity.this.phoneFormatMap.get(s);
                                        if (s2 != null) {
                                            hintEditText.setHintText(s2.replace('X', '\u2013'));
                                            hintEditText.setHint((CharSequence)null);
                                            break Label_0402;
                                        }
                                    }
                                    b = false;
                                }
                                if (!b) {
                                    hintEditText.setHintText(null);
                                    hintEditText.setHint((CharSequence)LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                                }
                                if (n == 0) {
                                    PaymentFormActivity.this.inputFields[8].setSelection(PaymentFormActivity.this.inputFields[8].getText().length());
                                }
                                if (text != null) {
                                    hintEditText.requestFocus();
                                    hintEditText.setText((CharSequence)text);
                                    hintEditText.setSelection(hintEditText.length());
                                }
                            }
                            PaymentFormActivity.this.ignoreOnTextChange = false;
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                }
                else if (i == 9) {
                    this.inputFields[i].setPadding(0, 0, 0, 0);
                    this.inputFields[i].setGravity(19);
                    ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        private int actionPosition;
                        private int characterAction = -1;
                        
                        public void afterTextChanged(final Editable editable) {
                            if (PaymentFormActivity.this.ignoreOnPhoneChange) {
                                return;
                            }
                            final HintEditText hintEditText = (HintEditText)PaymentFormActivity.this.inputFields[9];
                            final int selectionStart = hintEditText.getSelectionStart();
                            final String string = hintEditText.getText().toString();
                            int n = selectionStart;
                            String string2 = string;
                            if (this.characterAction == 3) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(string.substring(0, this.actionPosition));
                                sb.append(string.substring(this.actionPosition + 1));
                                string2 = sb.toString();
                                n = selectionStart - 1;
                            }
                            final StringBuilder text = new StringBuilder(string2.length());
                            int endIndex;
                            for (int i = 0; i < string2.length(); i = endIndex) {
                                endIndex = i + 1;
                                final String substring = string2.substring(i, endIndex);
                                if ("0123456789".contains(substring)) {
                                    text.append(substring);
                                }
                            }
                            PaymentFormActivity.this.ignoreOnPhoneChange = true;
                            final String hintText = hintEditText.getHintText();
                            int length = n;
                            Label_0341: {
                                if (hintText != null) {
                                    int j = 0;
                                    while (j < text.length()) {
                                        if (j < hintText.length()) {
                                            int n2 = j;
                                            int n3 = n;
                                            if (hintText.charAt(j) == ' ') {
                                                text.insert(j, ' ');
                                                n2 = ++j;
                                                if ((n3 = n) == j) {
                                                    final int characterAction = this.characterAction;
                                                    n2 = j;
                                                    n3 = n;
                                                    if (characterAction != 2) {
                                                        n2 = j;
                                                        n3 = n;
                                                        if (characterAction != 3) {
                                                            n3 = n + 1;
                                                            n2 = j;
                                                        }
                                                    }
                                                }
                                            }
                                            j = n2 + 1;
                                            n = n3;
                                        }
                                        else {
                                            text.insert(j, ' ');
                                            if (n != j + 1) {
                                                break;
                                            }
                                            final int characterAction2 = this.characterAction;
                                            if (characterAction2 != 2 && characterAction2 != 3) {
                                                length = n + 1;
                                                break Label_0341;
                                            }
                                            break;
                                        }
                                    }
                                    length = n;
                                }
                            }
                            hintEditText.setText((CharSequence)text);
                            if (length >= 0) {
                                if (length > hintEditText.length()) {
                                    length = hintEditText.length();
                                }
                                hintEditText.setSelection(length);
                            }
                            hintEditText.onTextChange();
                            PaymentFormActivity.this.ignoreOnPhoneChange = false;
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                            if (n2 == 0 && n3 == 1) {
                                this.characterAction = 1;
                            }
                            else if (n2 == 1 && n3 == 0) {
                                if (charSequence.charAt(n) == ' ' && n > 0) {
                                    this.characterAction = 3;
                                    this.actionPosition = n - 1;
                                }
                                else {
                                    this.characterAction = 2;
                                }
                            }
                            else {
                                this.characterAction = -1;
                            }
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                }
                else {
                    this.inputFields[i].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    final EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i];
                    int gravity;
                    if (LocaleController.isRTL) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    editTextBoldCursor2.setGravity(gravity);
                    ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                }
                this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY(this));
                if (i == 9) {
                    final TLRPC.TL_invoice invoice2 = this.paymentForm.invoice;
                    if (!invoice2.email_to_provider && !invoice2.phone_to_provider) {
                        this.sectionCell[1] = new ShadowSectionCell(context);
                        this.linearLayout2.addView((View)this.sectionCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else {
                        int j = 0;
                        TLRPC.User user = null;
                        while (j < this.paymentForm.users.size()) {
                            final TLRPC.User user2 = this.paymentForm.users.get(j);
                            if (user2.id == this.paymentForm.provider_id) {
                                user = user2;
                            }
                            ++j;
                        }
                        String formatName;
                        if (user != null) {
                            formatName = ContactsController.formatName(user.first_name, user.last_name);
                        }
                        else {
                            formatName = "";
                        }
                        (this.bottomCell[1] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                        this.linearLayout2.addView((View)this.bottomCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                        final TLRPC.TL_invoice invoice3 = this.paymentForm.invoice;
                        if (invoice3.email_to_provider && invoice3.phone_to_provider) {
                            this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneEmailToProvider", 2131560385, formatName));
                        }
                        else if (this.paymentForm.invoice.email_to_provider) {
                            this.bottomCell[1].setText(LocaleController.formatString("PaymentEmailToProvider", 2131560373, formatName));
                        }
                        else {
                            this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneToProvider", 2131560386, formatName));
                        }
                    }
                    (this.checkCell1 = new TextCheckCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentShippingSave", 2131560400), this.saveShippingInfo, false);
                    this.linearLayout2.addView((View)this.checkCell1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    this.checkCell1.setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$ja6Gof_oEgJFLBPAYcrRraILPks(this));
                    (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                    this.bottomCell[0].setText(LocaleController.getString("PaymentShippingSaveInfo", 2131560401));
                    this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                }
            }
            if (!this.paymentForm.invoice.name_requested) {
                ((ViewGroup)this.inputFields[6].getParent()).setVisibility(8);
            }
            if (!this.paymentForm.invoice.phone_requested) {
                ((ViewGroup)this.inputFields[8].getParent()).setVisibility(8);
            }
            if (!this.paymentForm.invoice.email_requested) {
                ((ViewGroup)this.inputFields[7].getParent()).setVisibility(8);
            }
            final TLRPC.TL_invoice invoice4 = this.paymentForm.invoice;
            if (invoice4.phone_requested) {
                this.inputFields[9].setImeOptions(268435462);
            }
            else if (invoice4.email_requested) {
                this.inputFields[7].setImeOptions(268435462);
            }
            else if (invoice4.name_requested) {
                this.inputFields[6].setImeOptions(268435462);
            }
            else {
                this.inputFields[5].setImeOptions(268435462);
            }
            final ShadowSectionCell[] sectionCell = this.sectionCell;
            if (sectionCell[1] != null) {
                final ShadowSectionCell shadowSectionCell = sectionCell[1];
                final TLRPC.TL_invoice invoice5 = this.paymentForm.invoice;
                int visibility;
                if (!invoice5.name_requested && !invoice5.phone_requested && !invoice5.email_requested) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                shadowSectionCell.setVisibility(visibility);
            }
            else {
                final TextInfoPrivacyCell[] bottomCell = this.bottomCell;
                if (bottomCell[1] != null) {
                    final TextInfoPrivacyCell textInfoPrivacyCell = bottomCell[1];
                    final TLRPC.TL_invoice invoice6 = this.paymentForm.invoice;
                    int visibility2;
                    if (!invoice6.name_requested && !invoice6.phone_requested && !invoice6.email_requested) {
                        visibility2 = 8;
                    }
                    else {
                        visibility2 = 0;
                    }
                    textInfoPrivacyCell.setVisibility(visibility2);
                }
            }
            final HeaderCell headerCell = this.headerCell[1];
            final TLRPC.TL_invoice invoice7 = this.paymentForm.invoice;
            int visibility3;
            if (!invoice7.name_requested && !invoice7.phone_requested && !invoice7.email_requested) {
                visibility3 = 8;
            }
            else {
                visibility3 = 0;
            }
            headerCell.setVisibility(visibility3);
            if (!this.paymentForm.invoice.shipping_address_requested) {
                this.headerCell[0].setVisibility(8);
                this.sectionCell[0].setVisibility(8);
                ((ViewGroup)this.inputFields[0].getParent()).setVisibility(8);
                ((ViewGroup)this.inputFields[1].getParent()).setVisibility(8);
                ((ViewGroup)this.inputFields[2].getParent()).setVisibility(8);
                ((ViewGroup)this.inputFields[3].getParent()).setVisibility(8);
                ((ViewGroup)this.inputFields[4].getParent()).setVisibility(8);
                ((ViewGroup)this.inputFields[5].getParent()).setVisibility(8);
            }
            final TLRPC.TL_paymentRequestedInfo saved_info9 = this.paymentForm.saved_info;
            if (saved_info9 != null && !TextUtils.isEmpty((CharSequence)saved_info9.phone)) {
                this.fillNumber(this.paymentForm.saved_info.phone);
            }
            else {
                this.fillNumber(null);
            }
            if (this.inputFields[8].length() == 0) {
                final TLRPC.TL_payments_paymentForm paymentForm = this.paymentForm;
                if (paymentForm.invoice.phone_requested) {
                    final TLRPC.TL_paymentRequestedInfo saved_info10 = paymentForm.saved_info;
                    if (saved_info10 != null) {
                        if (!TextUtils.isEmpty((CharSequence)saved_info10.phone)) {
                            return super.fragmentView;
                        }
                    }
                    String upperCase = null;
                    Label_3618: {
                        try {
                            final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                            if (telephonyManager != null) {
                                upperCase = telephonyManager.getSimCountryIso().toUpperCase();
                                break Label_3618;
                            }
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                        upperCase = null;
                    }
                    if (upperCase != null) {
                        final String s = hashMap.get(upperCase);
                        if (s != null && this.countriesArray.indexOf(s) != -1) {
                            this.inputFields[8].setText((CharSequence)this.countriesMap.get(s));
                        }
                    }
                }
            }
        }
        else if (currentStep3 == 2) {
            final TLRPC.TL_dataJSON native_params = this.paymentForm.native_params;
            if (native_params != null) {
                try {
                    final JSONObject jsonObject = new JSONObject(native_params.data);
                    try {
                        final String string = jsonObject.getString("android_pay_public_key");
                        if (!TextUtils.isEmpty((CharSequence)string)) {
                            this.androidPayPublicKey = string;
                        }
                    }
                    catch (Exception ex5) {
                        this.androidPayPublicKey = null;
                    }
                    try {
                        this.androidPayBackgroundColor = (jsonObject.getInt("android_pay_bgcolor") | 0xFF000000);
                    }
                    catch (Exception ex6) {
                        this.androidPayBackgroundColor = -1;
                    }
                    try {
                        this.androidPayBlackTheme = jsonObject.getBoolean("android_pay_inverse");
                    }
                    catch (Exception ex7) {
                        this.androidPayBlackTheme = false;
                    }
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
            }
            if (this.isWebView) {
                if (this.androidPayPublicKey != null) {
                    this.initAndroidPay(context);
                }
                (this.androidPayContainer = new FrameLayout(context)).setId(4000);
                this.androidPayContainer.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.androidPayContainer.setVisibility(8);
                this.linearLayout2.addView((View)this.androidPayContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                this.showEditDoneProgress(this.webviewLoading = true, true);
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                this.doneItem.getImageView().setVisibility(4);
                this.webView = new WebView(context) {
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, n2);
                    }
                    
                    public boolean onTouchEvent(final MotionEvent motionEvent) {
                        ((ViewGroup)PaymentFormActivity.this.fragmentView).requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                if (Build$VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                }
                if (Build$VERSION.SDK_INT >= 17) {
                    this.webView.addJavascriptInterface((Object)new TelegramWebviewProxy(), "TelegramWebviewProxy");
                }
                this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
                    public void onLoadResource(final WebView webView, final String s) {
                        super.onLoadResource(webView, s);
                    }
                    
                    public void onPageFinished(final WebView webView, final String s) {
                        super.onPageFinished(webView, s);
                        PaymentFormActivity.this.webviewLoading = false;
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.updateSavePaymentField();
                    }
                    
                    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                        final PaymentFormActivity this$0 = PaymentFormActivity.this;
                        this$0.shouldNavigateBack = (s.equals(this$0.webViewUrl) ^ true);
                        return super.shouldOverrideUrlLoading(webView, s);
                    }
                });
                this.linearLayout2.addView((View)this.webView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
                this.sectionCell[2] = new ShadowSectionCell(context);
                this.linearLayout2.addView((View)this.sectionCell[2], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.checkCell1 = new TextCheckCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", 2131560358), this.saveCardInfo, false);
                this.linearLayout2.addView((View)this.checkCell1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.checkCell1.setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$55th3Bl6h3UNIH_JJ7d61ZMtWHE(this));
                (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                this.updateSavePaymentField();
                this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            }
            else {
                final TLRPC.TL_dataJSON native_params2 = this.paymentForm.native_params;
                if (native_params2 != null) {
                    try {
                        final JSONObject jsonObject2 = new JSONObject(native_params2.data);
                        try {
                            this.need_card_country = jsonObject2.getBoolean("need_country");
                        }
                        catch (Exception ex8) {
                            this.need_card_country = false;
                        }
                        try {
                            this.need_card_postcode = jsonObject2.getBoolean("need_zip");
                        }
                        catch (Exception ex9) {
                            this.need_card_postcode = false;
                        }
                        try {
                            this.need_card_name = jsonObject2.getBoolean("need_cardholder_name");
                        }
                        catch (Exception ex10) {
                            this.need_card_name = false;
                        }
                        try {
                            this.stripeApiKey = jsonObject2.getString("publishable_key");
                        }
                        catch (Exception ex11) {
                            this.stripeApiKey = "";
                        }
                    }
                    catch (Exception ex4) {
                        FileLog.e(ex4);
                    }
                }
                this.initAndroidPay(context);
                this.inputFields = new EditTextBoldCursor[6];
                for (int k = 0; k < 6; ++k) {
                    if (k == 0) {
                        (this.headerCell[0] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", 2131560361));
                        this.linearLayout2.addView((View)this.headerCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else if (k == 4) {
                        (this.headerCell[1] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[1].setText(LocaleController.getString("PaymentBillingAddress", 2131560352));
                        this.linearLayout2.addView((View)this.headerCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    final boolean b3 = k != 3 && k != 5 && (k != 4 || this.need_card_postcode);
                    final FrameLayout frameLayout3 = new FrameLayout(context);
                    this.linearLayout2.addView((View)frameLayout3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                    ((ViewGroup)frameLayout3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    (this.inputFields[k] = new EditTextBoldCursor(context)).setTag((Object)k);
                    this.inputFields[k].setTextSize(1, 16.0f);
                    this.inputFields[k].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                    this.inputFields[k].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputFields[k].setBackgroundDrawable((Drawable)null);
                    this.inputFields[k].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputFields[k].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[k].setCursorWidth(1.5f);
                    if (k == 3) {
                        this.inputFields[k].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(3) });
                        this.inputFields[k].setInputType(130);
                        this.inputFields[k].setTypeface(Typeface.DEFAULT);
                        this.inputFields[k].setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                    }
                    else if (k == 0) {
                        this.inputFields[k].setInputType(3);
                    }
                    else if (k == 4) {
                        this.inputFields[k].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PaymentFormActivity$Ueinsx_MGryTGh_0YI94FAwwnHM(this));
                        this.inputFields[k].setInputType(0);
                    }
                    else if (k == 1) {
                        this.inputFields[k].setInputType(16386);
                    }
                    else if (k == 2) {
                        this.inputFields[k].setInputType(4097);
                    }
                    else {
                        this.inputFields[k].setInputType(16385);
                    }
                    this.inputFields[k].setImeOptions(268435461);
                    if (k != 0) {
                        if (k != 1) {
                            if (k != 2) {
                                if (k != 3) {
                                    if (k != 4) {
                                        if (k == 5) {
                                            this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentShippingZipPlaceholder", 2131560403));
                                        }
                                    }
                                    else {
                                        this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentShippingCountry", 2131560393));
                                    }
                                }
                                else {
                                    this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentCardCvv", 2131560353));
                                }
                            }
                            else {
                                this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentCardName", 2131560356));
                            }
                        }
                        else {
                            this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentCardExpireDate", 2131560354));
                        }
                    }
                    else {
                        this.inputFields[k].setHint((CharSequence)LocaleController.getString("PaymentCardNumber", 2131560357));
                    }
                    if (k == 0) {
                        this.inputFields[k].addTextChangedListener((TextWatcher)new TextWatcher() {
                            public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
                            public static final int MAX_LENGTH_DINERS_CLUB = 14;
                            public static final int MAX_LENGTH_STANDARD = 16;
                            public final String[] PREFIXES_14 = { "300", "301", "302", "303", "304", "305", "309", "36", "38", "39" };
                            public final String[] PREFIXES_15 = { "34", "37" };
                            public final String[] PREFIXES_16 = { "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35" };
                            private int actionPosition;
                            private int characterAction = -1;
                            
                            public void afterTextChanged(final Editable editable) {
                                if (PaymentFormActivity.this.ignoreOnCardChange) {
                                    return;
                                }
                                final EditTextBoldCursor editTextBoldCursor = PaymentFormActivity.this.inputFields[0];
                                final int selectionStart = editTextBoldCursor.getSelectionStart();
                                final String string = editTextBoldCursor.getText().toString();
                                int length = selectionStart;
                                String string2 = string;
                                if (this.characterAction == 3) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(string.substring(0, this.actionPosition));
                                    sb.append(string.substring(this.actionPosition + 1));
                                    string2 = sb.toString();
                                    length = selectionStart - 1;
                                }
                                final StringBuilder sb2 = new StringBuilder(string2.length());
                                int endIndex;
                                for (int i = 0; i < string2.length(); i = endIndex) {
                                    endIndex = i + 1;
                                    final String substring = string2.substring(i, endIndex);
                                    if ("0123456789".contains(substring)) {
                                        sb2.append(substring);
                                    }
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = true;
                                String s = null;
                                int n = 100;
                                if (sb2.length() > 0) {
                                    final String string3 = sb2.toString();
                                    String s2 = null;
                                    int n2 = 0;
                                    int n3 = 100;
                                    String s3;
                                    int length2;
                                    while (true) {
                                        s3 = s2;
                                        length2 = n3;
                                        if (n2 >= 3) {
                                            break;
                                        }
                                        String[] array;
                                        int n4;
                                        String s4;
                                        if (n2 != 0) {
                                            if (n2 != 1) {
                                                array = this.PREFIXES_14;
                                                n4 = 14;
                                                s4 = "xxxx xxxx xxxx xx";
                                            }
                                            else {
                                                array = this.PREFIXES_15;
                                                n4 = 15;
                                                s4 = "xxxx xxxx xxxx xxx";
                                            }
                                        }
                                        else {
                                            array = this.PREFIXES_16;
                                            n4 = 16;
                                            s4 = "xxxx xxxx xxxx xxxx";
                                        }
                                        int n5 = 0;
                                        Label_0356: {
                                            while (true) {
                                                s3 = s2;
                                                length2 = n3;
                                                if (n5 >= array.length) {
                                                    break Label_0356;
                                                }
                                                final String prefix = array[n5];
                                                if (string3.length() <= prefix.length()) {
                                                    if (prefix.startsWith(string3)) {
                                                        break;
                                                    }
                                                }
                                                else if (string3.startsWith(prefix)) {
                                                    break;
                                                }
                                                ++n5;
                                            }
                                            s3 = s4;
                                            length2 = n4;
                                        }
                                        if (s3 != null) {
                                            break;
                                        }
                                        ++n2;
                                        s2 = s3;
                                        n3 = length2;
                                    }
                                    s = s3;
                                    if ((n = length2) != 0) {
                                        s = s3;
                                        if (sb2.length() > (n = length2)) {
                                            sb2.setLength(length2);
                                            n = length2;
                                            s = s3;
                                        }
                                    }
                                }
                                final String s5 = "windowBackgroundWhiteBlackText";
                                if (s != null) {
                                    if (n != 0 && sb2.length() == n) {
                                        PaymentFormActivity.this.inputFields[1].requestFocus();
                                    }
                                    editTextBoldCursor.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                                    int offset = 0;
                                    int n6 = length;
                                    while (true) {
                                        length = n6;
                                        if (offset >= sb2.length()) {
                                            break;
                                        }
                                        if (offset < s.length()) {
                                            int n7 = offset;
                                            int n8 = n6;
                                            if (s.charAt(offset) == ' ') {
                                                sb2.insert(offset, ' ');
                                                n7 = ++offset;
                                                if ((n8 = n6) == offset) {
                                                    final int characterAction = this.characterAction;
                                                    n7 = offset;
                                                    n8 = n6;
                                                    if (characterAction != 2) {
                                                        n7 = offset;
                                                        n8 = n6;
                                                        if (characterAction != 3) {
                                                            n8 = n6 + 1;
                                                            n7 = offset;
                                                        }
                                                    }
                                                }
                                            }
                                            offset = n7 + 1;
                                            n6 = n8;
                                        }
                                        else {
                                            sb2.insert(offset, ' ');
                                            if ((length = n6) != offset + 1) {
                                                break;
                                            }
                                            final int characterAction2 = this.characterAction;
                                            length = n6;
                                            if (characterAction2 == 2) {
                                                break;
                                            }
                                            length = n6;
                                            if (characterAction2 != 3) {
                                                length = n6 + 1;
                                                break;
                                            }
                                            break;
                                        }
                                    }
                                }
                                else {
                                    String s6 = s5;
                                    if (sb2.length() > 0) {
                                        s6 = "windowBackgroundWhiteRedText4";
                                    }
                                    editTextBoldCursor.setTextColor(Theme.getColor(s6));
                                }
                                if (!sb2.toString().equals(editable.toString())) {
                                    editable.replace(0, editable.length(), (CharSequence)sb2);
                                }
                                if (length >= 0) {
                                    if (length > editTextBoldCursor.length()) {
                                        length = editTextBoldCursor.length();
                                    }
                                    editTextBoldCursor.setSelection(length);
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = false;
                            }
                            
                            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                if (n2 == 0 && n3 == 1) {
                                    this.characterAction = 1;
                                }
                                else if (n2 == 1 && n3 == 0) {
                                    if (charSequence.charAt(n) == ' ' && n > 0) {
                                        this.characterAction = 3;
                                        this.actionPosition = n - 1;
                                    }
                                    else {
                                        this.characterAction = 2;
                                    }
                                }
                                else {
                                    this.characterAction = -1;
                                }
                            }
                            
                            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                            }
                        });
                    }
                    else if (k == 1) {
                        this.inputFields[k].addTextChangedListener((TextWatcher)new TextWatcher() {
                            private int actionPosition;
                            private int characterAction = -1;
                            private boolean isYear;
                            
                            public void afterTextChanged(final Editable editable) {
                                if (PaymentFormActivity.this.ignoreOnCardChange) {
                                    return;
                                }
                                final EditTextBoldCursor[] access$1400 = PaymentFormActivity.this.inputFields;
                                final int n = 1;
                                final int n2 = 1;
                                final EditTextBoldCursor editTextBoldCursor = access$1400[1];
                                final int selectionStart = editTextBoldCursor.getSelectionStart();
                                final String string = editTextBoldCursor.getText().toString();
                                final int characterAction = this.characterAction;
                                final int n3 = 3;
                                int n4 = selectionStart;
                                String string2 = string;
                                if (characterAction == 3) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(string.substring(0, this.actionPosition));
                                    sb.append(string.substring(this.actionPosition + 1));
                                    string2 = sb.toString();
                                    n4 = selectionStart - 1;
                                }
                                final StringBuilder text = new StringBuilder(string2.length());
                                int endIndex;
                                for (int i = 0; i < string2.length(); i = endIndex) {
                                    endIndex = i + 1;
                                    final String substring = string2.substring(i, endIndex);
                                    if ("0123456789".contains(substring)) {
                                        text.append(substring);
                                    }
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = true;
                                PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                                if (text.length() > 4) {
                                    text.setLength(4);
                                }
                                if (text.length() < 2) {
                                    this.isYear = false;
                                }
                                int n8 = 0;
                                Label_0589: {
                                    int n7 = 0;
                                    Label_0580: {
                                        if (this.isYear) {
                                            int n5;
                                            if (text.length() > 2) {
                                                n5 = 2;
                                            }
                                            else {
                                                n5 = 1;
                                            }
                                            final String[] array = new String[n5];
                                            array[0] = text.substring(0, 2);
                                            if (array.length == 2) {
                                                array[1] = text.substring(2);
                                            }
                                            if (text.length() == 4 && array.length == 2) {
                                                final int intValue = Utilities.parseInt(array[0]);
                                                final int n6 = Utilities.parseInt(array[1]) + 2000;
                                                final Calendar instance = Calendar.getInstance();
                                                final int value = instance.get(1);
                                                final int value2 = instance.get(2);
                                                if (n6 >= value) {
                                                    n7 = n4;
                                                    if (n6 != value) {
                                                        break Label_0580;
                                                    }
                                                    n7 = n4;
                                                    if (intValue >= value2 + 1) {
                                                        break Label_0580;
                                                    }
                                                }
                                                PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                                n8 = n;
                                                break Label_0589;
                                            }
                                            final int intValue2 = Utilities.parseInt(array[0]);
                                            if (intValue2 <= 12) {
                                                n7 = n4;
                                                if (intValue2 != 0) {
                                                    break Label_0580;
                                                }
                                            }
                                            PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                            n8 = n;
                                            break Label_0589;
                                        }
                                        else if (text.length() == 1) {
                                            final int intValue3 = Utilities.parseInt(text.toString());
                                            n7 = n4;
                                            if (intValue3 != 1) {
                                                n7 = n4;
                                                if (intValue3 != 0) {
                                                    text.insert(0, "0");
                                                    n7 = n4 + 1;
                                                }
                                            }
                                        }
                                        else {
                                            n7 = n4;
                                            if (text.length() == 2) {
                                                final int intValue4 = Utilities.parseInt(text.toString());
                                                if (intValue4 <= 12 && intValue4 != 0) {
                                                    n8 = 0;
                                                }
                                                else {
                                                    PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                                    n8 = n2;
                                                }
                                                ++n4;
                                                break Label_0589;
                                            }
                                        }
                                    }
                                    final int n9 = 0;
                                    n4 = n7;
                                    n8 = n9;
                                }
                                if (n8 == 0 && text.length() == 4) {
                                    final EditTextBoldCursor[] access$1401 = PaymentFormActivity.this.inputFields;
                                    int n10 = n3;
                                    if (PaymentFormActivity.this.need_card_name) {
                                        n10 = 2;
                                    }
                                    access$1401[n10].requestFocus();
                                }
                                int length = 0;
                                Label_0702: {
                                    if (text.length() == 2) {
                                        text.append('/');
                                    }
                                    else {
                                        length = n4;
                                        if (text.length() <= 2) {
                                            break Label_0702;
                                        }
                                        length = n4;
                                        if (text.charAt(2) == '/') {
                                            break Label_0702;
                                        }
                                        text.insert(2, '/');
                                    }
                                    length = n4 + 1;
                                }
                                editTextBoldCursor.setText((CharSequence)text);
                                if (length >= 0) {
                                    if (length > editTextBoldCursor.length()) {
                                        length = editTextBoldCursor.length();
                                    }
                                    editTextBoldCursor.setSelection(length);
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = false;
                            }
                            
                            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                boolean isYear = false;
                                if (n2 == 0 && n3 == 1) {
                                    if (TextUtils.indexOf((CharSequence)PaymentFormActivity.this.inputFields[1].getText(), '/') != -1) {
                                        isYear = true;
                                    }
                                    this.isYear = isYear;
                                    this.characterAction = 1;
                                }
                                else if (n2 == 1 && n3 == 0) {
                                    if (charSequence.charAt(n) == '/' && n > 0) {
                                        this.isYear = false;
                                        this.characterAction = 3;
                                        this.actionPosition = n - 1;
                                    }
                                    else {
                                        this.characterAction = 2;
                                    }
                                }
                                else {
                                    this.characterAction = -1;
                                }
                            }
                            
                            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                            }
                        });
                    }
                    this.inputFields[k].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    final EditTextBoldCursor editTextBoldCursor3 = this.inputFields[k];
                    int gravity2;
                    if (LocaleController.isRTL) {
                        gravity2 = 5;
                    }
                    else {
                        gravity2 = 3;
                    }
                    editTextBoldCursor3.setGravity(gravity2);
                    ((ViewGroup)frameLayout3).addView((View)this.inputFields[k], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[k].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PaymentFormActivity$kGOIockVRhCZimnBQEOXCbrCCls(this));
                    if (k == 3) {
                        this.sectionCell[0] = new ShadowSectionCell(context);
                        this.linearLayout2.addView((View)this.sectionCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else if (k == 5) {
                        this.sectionCell[2] = new ShadowSectionCell(context);
                        this.linearLayout2.addView((View)this.sectionCell[2], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                        (this.checkCell1 = new TextCheckCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", 2131560358), this.saveCardInfo, false);
                        this.linearLayout2.addView((View)this.checkCell1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                        this.checkCell1.setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$7nFCu4nozpZFMiRn4XlVrELG_z4(this));
                        (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                        this.updateSavePaymentField();
                        this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else if (k == 0) {
                        (this.androidPayContainer = new FrameLayout(context)).setId(4000);
                        this.androidPayContainer.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.androidPayContainer.setVisibility(8);
                        ((ViewGroup)frameLayout3).addView((View)this.androidPayContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 4.0f, 0.0f));
                    }
                    if (b3) {
                        final View e2 = new View(context) {
                            protected void onDraw(final Canvas canvas) {
                                float n;
                                if (LocaleController.isRTL) {
                                    n = 0.0f;
                                }
                                else {
                                    n = (float)AndroidUtilities.dp(20.0f);
                                }
                                final float n2 = (float)(this.getMeasuredHeight() - 1);
                                final int measuredWidth = this.getMeasuredWidth();
                                int dp;
                                if (LocaleController.isRTL) {
                                    dp = AndroidUtilities.dp(20.0f);
                                }
                                else {
                                    dp = 0;
                                }
                                canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                            }
                        };
                        e2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.dividers.add(e2);
                        ((ViewGroup)frameLayout3).addView((View)e2, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
                    }
                    if ((k == 4 && !this.need_card_country) || (k == 5 && !this.need_card_postcode) || (k == 2 && !this.need_card_name)) {
                        ((ViewGroup)frameLayout3).setVisibility(8);
                    }
                }
                if (!this.need_card_country && !this.need_card_postcode) {
                    this.headerCell[1].setVisibility(8);
                    this.sectionCell[0].setVisibility(8);
                }
                if (this.need_card_postcode) {
                    this.inputFields[5].setImeOptions(268435462);
                }
                else {
                    this.inputFields[3].setImeOptions(268435462);
                }
            }
        }
        else if (currentStep3 == 1) {
            final int size = this.requestedInfo.shipping_options.size();
            this.radioCells = new RadioCell[size];
            for (int l = 0; l < size; ++l) {
                final TLRPC.TL_shippingOption tl_shippingOption = this.requestedInfo.shipping_options.get(l);
                (this.radioCells[l] = new RadioCell(context)).setTag((Object)l);
                this.radioCells[l].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.radioCells[l].setText(String.format("%s - %s", this.getTotalPriceString(tl_shippingOption.prices), tl_shippingOption.title), l == 0, l != size - 1);
                this.radioCells[l].setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$vEFqYoe1G6TRzc_6QuSxLqi0_ls(this));
                this.linearLayout2.addView((View)this.radioCells[l]);
            }
            (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        else if (currentStep3 == 3) {
            this.inputFields = new EditTextBoldCursor[2];
            for (int m = 0; m < 2; ++m) {
                if (m == 0) {
                    (this.headerCell[0] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", 2131560361));
                    this.linearLayout2.addView((View)this.headerCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                }
                final FrameLayout frameLayout4 = new FrameLayout(context);
                this.linearLayout2.addView((View)frameLayout4, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                ((ViewGroup)frameLayout4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                boolean b5;
                final boolean b4 = b5 = (m != 1);
                Label_6103: {
                    if (b4) {
                        if (m != 7 || this.paymentForm.invoice.phone_requested) {
                            b5 = b4;
                            if (m != 6) {
                                break Label_6103;
                            }
                            final TLRPC.TL_invoice invoice8 = this.paymentForm.invoice;
                            b5 = b4;
                            if (invoice8.phone_requested) {
                                break Label_6103;
                            }
                            b5 = b4;
                            if (invoice8.email_requested) {
                                break Label_6103;
                            }
                        }
                        b5 = false;
                    }
                }
                if (b5) {
                    final View e3 = new View(context) {
                        protected void onDraw(final Canvas canvas) {
                            float n;
                            if (LocaleController.isRTL) {
                                n = 0.0f;
                            }
                            else {
                                n = (float)AndroidUtilities.dp(20.0f);
                            }
                            final float n2 = (float)(this.getMeasuredHeight() - 1);
                            final int measuredWidth = this.getMeasuredWidth();
                            int dp;
                            if (LocaleController.isRTL) {
                                dp = AndroidUtilities.dp(20.0f);
                            }
                            else {
                                dp = 0;
                            }
                            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                        }
                    };
                    e3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.dividers.add(e3);
                    ((ViewGroup)frameLayout4).addView((View)e3, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
                }
                (this.inputFields[m] = new EditTextBoldCursor(context)).setTag((Object)m);
                this.inputFields[m].setTextSize(1, 16.0f);
                this.inputFields[m].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                this.inputFields[m].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[m].setBackgroundDrawable((Drawable)null);
                this.inputFields[m].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.inputFields[m].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[m].setCursorWidth(1.5f);
                if (m == 0) {
                    this.inputFields[m].setOnTouchListener((View$OnTouchListener)_$$Lambda$PaymentFormActivity$NaAxpZmEom2J3d4n5KPbjk_hflw.INSTANCE);
                    this.inputFields[m].setInputType(0);
                }
                else {
                    this.inputFields[m].setInputType(129);
                    this.inputFields[m].setTypeface(Typeface.DEFAULT);
                }
                this.inputFields[m].setImeOptions(268435462);
                if (m != 0) {
                    if (m == 1) {
                        this.inputFields[m].setHint((CharSequence)LocaleController.getString("LoginPassword", 2131559788));
                        this.inputFields[m].requestFocus();
                    }
                }
                else {
                    this.inputFields[m].setText((CharSequence)this.paymentForm.saved_credentials.title);
                }
                this.inputFields[m].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                final EditTextBoldCursor editTextBoldCursor4 = this.inputFields[m];
                int gravity3;
                if (LocaleController.isRTL) {
                    gravity3 = 5;
                }
                else {
                    gravity3 = 3;
                }
                editTextBoldCursor4.setGravity(gravity3);
                ((ViewGroup)frameLayout4).addView((View)this.inputFields[m], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[m].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw(this));
                if (m == 1) {
                    (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setText(LocaleController.formatString("PaymentConfirmationMessage", 2131560370, this.paymentForm.saved_credentials.title));
                    this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
                    this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    (this.settingsCell[0] = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.settingsCell[0].setText(LocaleController.getString("PaymentConfirmationNewCard", 2131560371), false);
                    this.linearLayout2.addView((View)this.settingsCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0].setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$hOneDfBvow_ay5NfpKknN53qGXA(this));
                    (this.bottomCell[1] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                    this.linearLayout2.addView((View)this.bottomCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                }
            }
        }
        else if (currentStep3 != 4 && currentStep3 != 5) {
            if (currentStep3 == 6) {
                (this.codeFieldCell = new EditTextSettingsCell(context)).setTextAndHint("", LocaleController.getString("PasswordCode", 2131560345), false);
                this.codeFieldCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                final EditTextBoldCursor textView = this.codeFieldCell.getTextView();
                textView.setInputType(3);
                textView.setImeOptions(6);
                textView.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs(this));
                textView.addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        if (PaymentFormActivity.this.emailCodeLength != 0 && editable.length() == PaymentFormActivity.this.emailCodeLength) {
                            PaymentFormActivity.this.sendSavePassword(false);
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
                this.linearLayout2.addView((View)this.codeFieldCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.bottomCell[2] = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
                this.linearLayout2.addView((View)this.bottomCell[2], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.settingsCell[1] = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.settingsCell[1].setTag((Object)"windowBackgroundWhiteBlackText");
                this.settingsCell[1].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.settingsCell[1].setText(LocaleController.getString("ResendCode", 2131560581), true);
                this.linearLayout2.addView((View)this.settingsCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.settingsCell[1].setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$I7CPk7usQoOQw119fB6a_FgtbsU(this));
                (this.settingsCell[0] = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.settingsCell[0].setTag((Object)"windowBackgroundWhiteRedText3");
                this.settingsCell[0].setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                this.settingsCell[0].setText(LocaleController.getString("AbortPassword", 2131558402), false);
                this.linearLayout2.addView((View)this.settingsCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.settingsCell[0].setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$u3SvDLQ9ZpBSRMUM2jg2lBOMsbk(this));
                this.inputFields = new EditTextBoldCursor[3];
                for (int i2 = 0; i2 < 3; ++i2) {
                    if (i2 == 0) {
                        (this.headerCell[0] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[0].setText(LocaleController.getString("PaymentPasswordTitle", 2131560384));
                        this.linearLayout2.addView((View)this.headerCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else if (i2 == 2) {
                        (this.headerCell[1] = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[1].setText(LocaleController.getString("PaymentPasswordEmailTitle", 2131560380));
                        this.linearLayout2.addView((View)this.headerCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    final FrameLayout frameLayout5 = new FrameLayout(context);
                    this.linearLayout2.addView((View)frameLayout5, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                    ((ViewGroup)frameLayout5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    if (i2 == 0) {
                        final View e4 = new View(context) {
                            protected void onDraw(final Canvas canvas) {
                                float n;
                                if (LocaleController.isRTL) {
                                    n = 0.0f;
                                }
                                else {
                                    n = (float)AndroidUtilities.dp(20.0f);
                                }
                                final float n2 = (float)(this.getMeasuredHeight() - 1);
                                final int measuredWidth = this.getMeasuredWidth();
                                int dp;
                                if (LocaleController.isRTL) {
                                    dp = AndroidUtilities.dp(20.0f);
                                }
                                else {
                                    dp = 0;
                                }
                                canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                            }
                        };
                        e4.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.dividers.add(e4);
                        ((ViewGroup)frameLayout5).addView((View)e4, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
                    }
                    (this.inputFields[i2] = new EditTextBoldCursor(context)).setTag((Object)i2);
                    this.inputFields[i2].setTextSize(1, 16.0f);
                    this.inputFields[i2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                    this.inputFields[i2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputFields[i2].setBackgroundDrawable((Drawable)null);
                    this.inputFields[i2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.inputFields[i2].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i2].setCursorWidth(1.5f);
                    if (i2 != 0 && i2 != 1) {
                        this.inputFields[i2].setInputType(33);
                        this.inputFields[i2].setImeOptions(268435462);
                    }
                    else {
                        this.inputFields[i2].setInputType(129);
                        this.inputFields[i2].setTypeface(Typeface.DEFAULT);
                        this.inputFields[i2].setImeOptions(268435461);
                    }
                    if (i2 != 0) {
                        if (i2 != 1) {
                            if (i2 == 2) {
                                this.inputFields[i2].setHint((CharSequence)LocaleController.getString("PaymentPasswordEmail", 2131560378));
                            }
                        }
                        else {
                            this.inputFields[i2].setHint((CharSequence)LocaleController.getString("PaymentPasswordReEnter", 2131560383));
                        }
                    }
                    else {
                        this.inputFields[i2].setHint((CharSequence)LocaleController.getString("PaymentPasswordEnter", 2131560381));
                        this.inputFields[i2].requestFocus();
                    }
                    this.inputFields[i2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    final EditTextBoldCursor editTextBoldCursor5 = this.inputFields[i2];
                    int gravity4;
                    if (LocaleController.isRTL) {
                        gravity4 = 5;
                    }
                    else {
                        gravity4 = 3;
                    }
                    editTextBoldCursor5.setGravity(gravity4);
                    ((ViewGroup)frameLayout5).addView((View)this.inputFields[i2], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i2].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PaymentFormActivity$wikSuuvfxhb2U7Hw9X3oaWcgGS8(this));
                    if (i2 == 1) {
                        (this.bottomCell[0] = new TextInfoPrivacyCell(context)).setText(LocaleController.getString("PaymentPasswordInfo", 2131560382));
                        this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
                        this.linearLayout2.addView((View)this.bottomCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                    else if (i2 == 2) {
                        (this.bottomCell[1] = new TextInfoPrivacyCell(context)).setText(LocaleController.getString("PaymentPasswordEmailInfo", 2131560379));
                        this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
                        this.linearLayout2.addView((View)this.bottomCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    }
                }
                this.updatePasswordFields();
            }
        }
        else {
            (this.paymentInfoCell = new PaymentInfoCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.paymentInfoCell.setInvoice((TLRPC.TL_messageMediaInvoice)this.messageObject.messageOwner.media, this.currentBotName);
            this.linearLayout2.addView((View)this.paymentInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.sectionCell[0] = new ShadowSectionCell(context);
            this.linearLayout2.addView((View)this.sectionCell[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final ArrayList<TLRPC.TL_labeledPrice> list = new ArrayList<TLRPC.TL_labeledPrice>(this.paymentForm.invoice.prices);
            final TLRPC.TL_shippingOption shippingOption = this.shippingOption;
            if (shippingOption != null) {
                list.addAll(shippingOption.prices);
            }
            final String totalPriceString = this.getTotalPriceString(list);
            for (int index = 0; index < list.size(); ++index) {
                final TLRPC.TL_labeledPrice tl_labeledPrice = list.get(index);
                final TextPriceCell textPriceCell = new TextPriceCell(context);
                textPriceCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textPriceCell.setTextAndValue(tl_labeledPrice.label, LocaleController.getInstance().formatCurrencyString(tl_labeledPrice.amount, this.paymentForm.invoice.currency), false);
                this.linearLayout2.addView((View)textPriceCell);
            }
            final TextPriceCell textPriceCell2 = new TextPriceCell(context);
            textPriceCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            textPriceCell2.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", 2131560409), totalPriceString, true);
            this.linearLayout2.addView((View)textPriceCell2);
            final View e5 = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    float n;
                    if (LocaleController.isRTL) {
                        n = 0.0f;
                    }
                    else {
                        n = (float)AndroidUtilities.dp(20.0f);
                    }
                    final float n2 = (float)(this.getMeasuredHeight() - 1);
                    final int measuredWidth = this.getMeasuredWidth();
                    int dp;
                    if (LocaleController.isRTL) {
                        dp = AndroidUtilities.dp(20.0f);
                    }
                    else {
                        dp = 0;
                    }
                    canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                }
            };
            e5.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.dividers.add(e5);
            this.linearLayout2.addView((View)e5, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
            (this.detailSettingsCell[0] = new TextDetailSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.detailSettingsCell[0].setTextAndValue(this.cardName, LocaleController.getString("PaymentCheckoutMethod", 2131560364), true);
            this.linearLayout2.addView((View)this.detailSettingsCell[0]);
            if (this.currentStep == 4) {
                this.detailSettingsCell[0].setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$__v7hZyhHE9vxsissEj7icTtuGM(this));
            }
            int index2 = 0;
            TLRPC.User user3 = null;
            while (index2 < this.paymentForm.users.size()) {
                final TLRPC.User user4 = this.paymentForm.users.get(index2);
                if (user4.id == this.paymentForm.provider_id) {
                    user3 = user4;
                }
                ++index2;
            }
            String formatName2;
            if (user3 != null) {
                (this.detailSettingsCell[1] = new TextDetailSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                final TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[1];
                formatName2 = ContactsController.formatName(user3.first_name, user3.last_name);
                textDetailSettingsCell.setTextAndValue(formatName2, LocaleController.getString("PaymentCheckoutProvider", 2131560368), true);
                this.linearLayout2.addView((View)this.detailSettingsCell[1]);
            }
            else {
                formatName2 = "";
            }
            final TLRPC.TL_payments_validateRequestedInfo validateRequest = this.validateRequest;
            if (validateRequest != null) {
                final TLRPC.TL_postAddress shipping_address7 = validateRequest.info.shipping_address;
                if (shipping_address7 != null) {
                    final String format = String.format("%s %s, %s, %s, %s, %s", shipping_address7.street_line1, shipping_address7.street_line2, shipping_address7.city, shipping_address7.state, shipping_address7.country_iso2, shipping_address7.post_code);
                    (this.detailSettingsCell[2] = new TextDetailSettingsCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.detailSettingsCell[2].setTextAndValue(format, LocaleController.getString("PaymentShippingAddress", 2131560389), true);
                    this.linearLayout2.addView((View)this.detailSettingsCell[2]);
                }
                if (this.validateRequest.info.name != null) {
                    (this.detailSettingsCell[3] = new TextDetailSettingsCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.detailSettingsCell[3].setTextAndValue(this.validateRequest.info.name, LocaleController.getString("PaymentCheckoutName", 2131560365), true);
                    this.linearLayout2.addView((View)this.detailSettingsCell[3]);
                }
                if (this.validateRequest.info.phone != null) {
                    (this.detailSettingsCell[4] = new TextDetailSettingsCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.detailSettingsCell[4].setTextAndValue(PhoneFormat.getInstance().format(this.validateRequest.info.phone), LocaleController.getString("PaymentCheckoutPhoneNumber", 2131560367), true);
                    this.linearLayout2.addView((View)this.detailSettingsCell[4]);
                }
                if (this.validateRequest.info.email != null) {
                    (this.detailSettingsCell[5] = new TextDetailSettingsCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.detailSettingsCell[5].setTextAndValue(this.validateRequest.info.email, LocaleController.getString("PaymentCheckoutEmail", 2131560363), true);
                    this.linearLayout2.addView((View)this.detailSettingsCell[5]);
                }
                if (this.shippingOption != null) {
                    (this.detailSettingsCell[6] = new TextDetailSettingsCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.detailSettingsCell[6].setTextAndValue(this.shippingOption.title, LocaleController.getString("PaymentCheckoutShippingMethod", 2131560369), false);
                    this.linearLayout2.addView((View)this.detailSettingsCell[6]);
                }
            }
            if (this.currentStep == 4) {
                (this.bottomLayout = new FrameLayout(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                frameLayout.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
                this.bottomLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PaymentFormActivity$UCCxWFA5zv52WCmPKPbwcSfjXPs(this, formatName2, totalPriceString));
                (this.payTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText6"));
                this.payTextView.setText((CharSequence)LocaleController.formatString("PaymentCheckoutPay", 2131560366, totalPriceString));
                this.payTextView.setTextSize(1, 14.0f);
                this.payTextView.setGravity(17);
                this.payTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.bottomLayout.addView((View)this.payTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                (this.progressViewButton = new ContextProgressView(context, 0)).setVisibility(4);
                this.bottomLayout.addView((View)this.progressViewButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                final View view = new View(context);
                view.setBackgroundResource(2131165408);
                frameLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
                this.doneItem.setEnabled(false);
                this.doneItem.getImageView().setVisibility(4);
                (this.webView = new WebView(context) {
                    public boolean onTouchEvent(final MotionEvent motionEvent) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(motionEvent);
                    }
                }).setBackgroundColor(-1);
                this.webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                if (Build$VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                }
                this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
                    public void onLoadResource(final WebView webView, final String s) {
                        while (true) {
                            try {
                                if ("t.me".equals(Uri.parse(s).getHost())) {
                                    PaymentFormActivity.this.goToNextStep();
                                    return;
                                }
                                super.onLoadResource(webView, s);
                            }
                            catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                    }
                    
                    public void onPageFinished(final WebView webView, final String s) {
                        super.onPageFinished(webView, s);
                        PaymentFormActivity.this.webviewLoading = false;
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.updateSavePaymentField();
                    }
                    
                    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                        try {
                            if ("t.me".equals(Uri.parse(s).getHost())) {
                                PaymentFormActivity.this.goToNextStep();
                                return true;
                            }
                            return false;
                        }
                        catch (Exception ex) {
                            return false;
                        }
                    }
                });
                frameLayout.addView((View)this.webView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                this.webView.setVisibility(8);
            }
            (this.sectionCell[1] = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout2.addView((View)this.sectionCell[1], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.didSetTwoStepPassword) {
            final TLRPC.TL_payments_paymentForm paymentForm = this.paymentForm;
            paymentForm.password_missing = false;
            paymentForm.can_save_credentials = true;
            this.updateSavePaymentField();
        }
        else if (n == NotificationCenter.didRemoveTwoStepPassword) {
            final TLRPC.TL_payments_paymentForm paymentForm2 = this.paymentForm;
            paymentForm2.password_missing = true;
            paymentForm2.can_save_credentials = false;
            this.updateSavePaymentField();
        }
        else if (n == NotificationCenter.paymentFinished) {
            this.removeSelfFromStack();
        }
    }
    
    @SuppressLint({ "HardwareIds" })
    public void fillNumber(String text) {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            if (text != null || (telephonyManager.getSimState() != 1 && telephonyManager.getPhoneType() != 0)) {
                final boolean b = Build$VERSION.SDK_INT < 23 || this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                if (text != null || b) {
                    String stripExceptNumbers;
                    if ((stripExceptNumbers = text) == null) {
                        stripExceptNumbers = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                    }
                    text = null;
                    if (!TextUtils.isEmpty((CharSequence)stripExceptNumbers)) {
                        final int length = stripExceptNumbers.length();
                        int i = 4;
                        Label_0203: {
                            if (length > 4) {
                                while (true) {
                                    while (i >= 1) {
                                        final String substring = stripExceptNumbers.substring(0, i);
                                        if (this.codesMap.get(substring) != null) {
                                            text = stripExceptNumbers.substring(i);
                                            this.inputFields[8].setText((CharSequence)substring);
                                            final boolean b2 = true;
                                            if (!b2) {
                                                text = stripExceptNumbers.substring(1);
                                                this.inputFields[8].setText((CharSequence)stripExceptNumbers.substring(0, 1));
                                            }
                                            break Label_0203;
                                        }
                                        else {
                                            --i;
                                        }
                                    }
                                    text = null;
                                    final boolean b2 = false;
                                    continue;
                                }
                            }
                        }
                        if (text != null) {
                            this.inputFields[9].setText((CharSequence)text);
                            this.inputFields[9].setSelection(this.inputFields[9].length());
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"));
        list.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner2"));
        list.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter2"));
        list.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressInner2"));
        list.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressOuter2"));
        if (this.inputFields != null) {
            int n = 0;
            while (true) {
                final EditTextBoldCursor[] inputFields = this.inputFields;
                if (n >= inputFields.length) {
                    break;
                }
                list.add(new ThemeDescription((View)inputFields[n].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                ++n;
            }
        }
        else {
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        }
        if (this.radioCells != null) {
            int n2 = 0;
            while (true) {
                final RadioCell[] radioCells = this.radioCells;
                if (n2 >= radioCells.length) {
                    break;
                }
                list.add(new ThemeDescription((View)radioCells[n2], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)this.radioCells[n2], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "listSelectorSDK21"));
                list.add(new ThemeDescription((View)this.radioCells[n2], 0, new Class[] { RadioCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.radioCells[n2], ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"));
                list.add(new ThemeDescription((View)this.radioCells[n2], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"));
                ++n2;
            }
        }
        else {
            list.add(new ThemeDescription(null, 0, new Class[] { RadioCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"));
        }
        int n3 = 0;
        while (true) {
            final HeaderCell[] headerCell = this.headerCell;
            if (n3 >= headerCell.length) {
                break;
            }
            list.add(new ThemeDescription((View)headerCell[n3], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)this.headerCell[n3], 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"));
            ++n3;
        }
        int n4 = 0;
        while (true) {
            final ShadowSectionCell[] sectionCell = this.sectionCell;
            if (n4 >= sectionCell.length) {
                break;
            }
            list.add(new ThemeDescription(sectionCell[n4], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"));
            ++n4;
        }
        int n5 = 0;
        while (true) {
            final TextInfoPrivacyCell[] bottomCell = this.bottomCell;
            if (n5 >= bottomCell.length) {
                break;
            }
            list.add(new ThemeDescription((View)bottomCell[n5], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"));
            list.add(new ThemeDescription((View)this.bottomCell[n5], 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"));
            list.add(new ThemeDescription((View)this.bottomCell[n5], ThemeDescription.FLAG_LINKCOLOR, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteLinkText"));
            ++n5;
        }
        for (int i = 0; i < this.dividers.size(); ++i) {
            list.add(new ThemeDescription(this.dividers.get(i), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        }
        list.add(new ThemeDescription((View)this.codeFieldCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.codeFieldCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.codeFieldCell, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteHintText"));
        list.add(new ThemeDescription((View)this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.checkCell1, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.checkCell1, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"));
        list.add(new ThemeDescription((View)this.checkCell1, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"));
        list.add(new ThemeDescription((View)this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "listSelectorSDK21"));
        int n6 = 0;
        while (true) {
            final TextSettingsCell[] settingsCell = this.settingsCell;
            if (n6 >= settingsCell.length) {
                break;
            }
            list.add(new ThemeDescription((View)settingsCell[n6], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)this.settingsCell[n6], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "listSelectorSDK21"));
            list.add(new ThemeDescription((View)this.settingsCell[n6], 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            ++n6;
        }
        list.add(new ThemeDescription((View)this.payTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText6"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextPriceCell.class }, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextPriceCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextPriceCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextPriceCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextPriceCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "listSelectorSDK21"));
        int n7 = 1;
        while (true) {
            final TextDetailSettingsCell[] detailSettingsCell = this.detailSettingsCell;
            if (n7 >= detailSettingsCell.length) {
                break;
            }
            list.add(new ThemeDescription((View)detailSettingsCell[n7], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)this.detailSettingsCell[n7], 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription((View)this.detailSettingsCell[n7], 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
            ++n7;
        }
        list.add(new ThemeDescription((View)this.paymentInfoCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.paymentInfoCell, 0, new Class[] { PaymentInfoCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.paymentInfoCell, 0, new Class[] { PaymentInfoCell.class }, new String[] { "detailTextView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.paymentInfoCell, 0, new Class[] { PaymentInfoCell.class }, new String[] { "detailExTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, "listSelectorSDK21"));
        return list.toArray(new ThemeDescription[0]);
    }
    
    @Override
    public boolean onBackPressed() {
        if (this.shouldNavigateBack) {
            this.webView.loadUrl(this.webViewUrl);
            return this.shouldNavigateBack = false;
        }
        return this.donePressed ^ true;
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.paymentFinished);
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        final PaymentFormActivityDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onFragmentDestroyed();
        }
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
        }
        final WebView webView = this.webView;
        if (webView != null) {
            try {
                final ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup)parent).removeView((View)this.webView);
                }
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                this.webViewUrl = null;
                this.webView.destroy();
                this.webView = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        try {
            if ((this.currentStep == 2 || this.currentStep == 6) && Build$VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                this.getParentActivity().getWindow().clearFlags(8192);
            }
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
        super.onFragmentDestroy();
        this.canceled = true;
    }
    
    @Override
    public void onPause() {
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        if (Build$VERSION.SDK_INT >= 23) {
            try {
                if ((this.currentStep == 2 || this.currentStep == 6) && !this.paymentForm.invoice.test) {
                    this.getParentActivity().getWindow().setFlags(8192, 8192);
                }
                else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
                    this.getParentActivity().getWindow().clearFlags(8192);
                }
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2) {
            final WebView webView = this.webView;
            if (webView != null) {
                if (this.currentStep != 4) {
                    webView.loadUrl(this.webViewUrl = this.paymentForm.url);
                }
            }
            else {
                final int currentStep = this.currentStep;
                if (currentStep == 2) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard((View)this.inputFields[0]);
                }
                else if (currentStep == 3) {
                    this.inputFields[1].requestFocus();
                    AndroidUtilities.showKeyboard((View)this.inputFields[1]);
                }
                else if (currentStep == 6 && !this.waitingForEmail) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard((View)this.inputFields[0]);
                }
            }
        }
    }
    
    public class LinkSpan extends ClickableSpan
    {
        public void onClick(final View view) {
            PaymentFormActivity.this.presentFragment(new TwoStepVerificationActivity(0));
        }
        
        public void updateDrawState(final TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }
    
    private interface PaymentFormActivityDelegate
    {
        void currentPasswordUpdated(final TLRPC.TL_account_password p0);
        
        boolean didSelectNewCard(final String p0, final String p1, final boolean p2, final TLRPC.TL_inputPaymentCredentialsAndroidPay p3);
        
        void onFragmentDestroyed();
    }
    
    private class TelegramWebviewProxy
    {
        @JavascriptInterface
        public void postEvent(final String s, final String s2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$TelegramWebviewProxy$GYkvPF7FkeOF4Jpek1fag25gp2E(this, s, s2));
        }
    }
}
