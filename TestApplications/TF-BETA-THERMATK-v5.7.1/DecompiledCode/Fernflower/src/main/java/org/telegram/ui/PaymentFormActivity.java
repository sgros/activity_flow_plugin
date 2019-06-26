package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PaymentInfoCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextPriceCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;

public class PaymentFormActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
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
   private TextInfoPrivacyCell[] bottomCell = new TextInfoPrivacyCell[3];
   private FrameLayout bottomLayout;
   private boolean canceled;
   private String cardName;
   private TextCheckCell checkCell1;
   private EditTextSettingsCell codeFieldCell;
   private HashMap codesMap = new HashMap();
   private ArrayList countriesArray = new ArrayList();
   private HashMap countriesMap = new HashMap();
   private String countryName;
   private String currentBotName;
   private String currentItemName;
   private TLRPC.TL_account_password currentPassword;
   private int currentStep;
   private PaymentFormActivity.PaymentFormActivityDelegate delegate;
   private TextDetailSettingsCell[] detailSettingsCell = new TextDetailSettingsCell[7];
   private ArrayList dividers = new ArrayList();
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private boolean donePressed;
   private int emailCodeLength = 6;
   private HeaderCell[] headerCell = new HeaderCell[3];
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
   private HashMap phoneFormatMap = new HashMap();
   private ContextProgressView progressView;
   private ContextProgressView progressViewButton;
   private RadioCell[] radioCells;
   private TLRPC.TL_payments_validatedRequestedInfo requestedInfo;
   private boolean saveCardInfo;
   private boolean saveShippingInfo;
   private ScrollView scrollView;
   private ShadowSectionCell[] sectionCell = new ShadowSectionCell[3];
   private TextSettingsCell[] settingsCell = new TextSettingsCell[2];
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

   public PaymentFormActivity(MessageObject var1, TLRPC.TL_payments_paymentReceipt var2) {
      this.currentStep = 5;
      this.paymentForm = new TLRPC.TL_payments_paymentForm();
      TLRPC.TL_payments_paymentForm var3 = this.paymentForm;
      var3.bot_id = var2.bot_id;
      var3.invoice = var2.invoice;
      var3.provider_id = var2.provider_id;
      var3.users = var2.users;
      this.shippingOption = var2.shipping;
      this.messageObject = var1;
      this.botUser = MessagesController.getInstance(super.currentAccount).getUser(var2.bot_id);
      TLRPC.User var4 = this.botUser;
      if (var4 != null) {
         this.currentBotName = var4.first_name;
      } else {
         this.currentBotName = "";
      }

      this.currentItemName = var1.messageOwner.media.title;
      if (var2.info != null) {
         this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
         this.validateRequest.info = var2.info;
      }

      this.cardName = var2.credentials_title;
   }

   public PaymentFormActivity(TLRPC.TL_payments_paymentForm var1, MessageObject var2) {
      TLRPC.TL_invoice var3 = var1.invoice;
      boolean var4 = var3.shipping_address_requested;
      byte var5 = 0;
      byte var6 = var5;
      if (!var4) {
         var6 = var5;
         if (!var3.email_requested) {
            var6 = var5;
            if (!var3.name_requested) {
               if (var3.phone_requested) {
                  var6 = var5;
               } else if (var1.saved_credentials != null) {
                  if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
                     UserConfig.getInstance(super.currentAccount).tmpPassword = null;
                     UserConfig.getInstance(super.currentAccount).saveConfig(false);
                  }

                  if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
                     var6 = 4;
                  } else {
                     var6 = 3;
                  }
               } else {
                  var6 = 2;
               }
            }
         }
      }

      this.init(var1, var2, var6, (TLRPC.TL_payments_validatedRequestedInfo)null, (TLRPC.TL_shippingOption)null, (String)null, (String)null, (TLRPC.TL_payments_validateRequestedInfo)null, false, (TLRPC.TL_inputPaymentCredentialsAndroidPay)null);
   }

   private PaymentFormActivity(TLRPC.TL_payments_paymentForm var1, MessageObject var2, int var3, TLRPC.TL_payments_validatedRequestedInfo var4, TLRPC.TL_shippingOption var5, String var6, String var7, TLRPC.TL_payments_validateRequestedInfo var8, boolean var9, TLRPC.TL_inputPaymentCredentialsAndroidPay var10) {
      this.init(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   // $FF: synthetic method
   static String access$100(PaymentFormActivity var0) {
      return var0.cardName;
   }

   // $FF: synthetic method
   static View access$1900(PaymentFormActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static TLRPC.TL_payments_paymentForm access$3900(PaymentFormActivity var0) {
      return var0.paymentForm;
   }

   // $FF: synthetic method
   static boolean access$4002(PaymentFormActivity var0, boolean var1) {
      var0.saveCardInfo = var1;
      return var1;
   }

   // $FF: synthetic method
   static TLRPC.TL_inputPaymentCredentialsAndroidPay access$4102(PaymentFormActivity var0, TLRPC.TL_inputPaymentCredentialsAndroidPay var1) {
      var0.androidPayCredentials = var1;
      return var1;
   }

   // $FF: synthetic method
   static TextDetailSettingsCell[] access$4200(PaymentFormActivity var0) {
      return var0.detailSettingsCell;
   }

   private void checkPassword() {
      if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
         UserConfig.getInstance(super.currentAccount).tmpPassword = null;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
      }

      if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
         this.sendData();
      } else if (this.inputFields[1].length() == 0) {
         Vibrator var3 = (Vibrator)ApplicationLoader.applicationContext.getSystemService("vibrator");
         if (var3 != null) {
            var3.vibrate(200L);
         }

         AndroidUtilities.shakeView(this.inputFields[1], 2.0F, 0);
      } else {
         String var2 = this.inputFields[1].getText().toString();
         this.showEditDoneProgress(true, true);
         this.setDonePressed(true);
         TLRPC.TL_account_getPassword var1 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PaymentFormActivity$tY62nItwlzRcBMKPnqAwPnsARG0(this, var2, var1), 2);
      }
   }

   private TLRPC.TL_paymentRequestedInfo getRequestInfo() {
      TLRPC.TL_paymentRequestedInfo var1 = new TLRPC.TL_paymentRequestedInfo();
      if (this.paymentForm.invoice.name_requested) {
         var1.name = this.inputFields[6].getText().toString();
         var1.flags |= 1;
      }

      if (this.paymentForm.invoice.phone_requested) {
         StringBuilder var2 = new StringBuilder();
         var2.append("+");
         var2.append(this.inputFields[8].getText().toString());
         var2.append(this.inputFields[9].getText().toString());
         var1.phone = var2.toString();
         var1.flags |= 2;
      }

      if (this.paymentForm.invoice.email_requested) {
         var1.email = this.inputFields[7].getText().toString().trim();
         var1.flags |= 4;
      }

      if (this.paymentForm.invoice.shipping_address_requested) {
         var1.shipping_address = new TLRPC.TL_postAddress();
         var1.shipping_address.street_line1 = this.inputFields[0].getText().toString();
         var1.shipping_address.street_line2 = this.inputFields[1].getText().toString();
         var1.shipping_address.city = this.inputFields[2].getText().toString();
         var1.shipping_address.state = this.inputFields[3].getText().toString();
         TLRPC.TL_postAddress var3 = var1.shipping_address;
         String var4 = this.countryName;
         if (var4 == null) {
            var4 = "";
         }

         var3.country_iso2 = var4;
         var1.shipping_address.post_code = this.inputFields[5].getText().toString();
         var1.flags |= 8;
      }

      return var1;
   }

   private String getTotalPriceDecimalString(ArrayList var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         var2 += ((TLRPC.TL_labeledPrice)var1.get(var4)).amount;
      }

      return LocaleController.getInstance().formatCurrencyDecimalString(var2, this.paymentForm.invoice.currency, false);
   }

   private String getTotalPriceString(ArrayList var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         var2 += ((TLRPC.TL_labeledPrice)var1.get(var4)).amount;
      }

      return LocaleController.getInstance().formatCurrencyString(var2, this.paymentForm.invoice.currency);
   }

   private void goToNextStep() {
      int var1 = this.currentStep;
      TLRPC.TL_payments_paymentForm var2;
      byte var4;
      if (var1 == 0) {
         var2 = this.paymentForm;
         if (var2.invoice.flexible) {
            var4 = 1;
         } else if (var2.saved_credentials != null) {
            if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
               UserConfig.getInstance(super.currentAccount).tmpPassword = null;
               UserConfig.getInstance(super.currentAccount).saveConfig(false);
            }

            if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
               var4 = 4;
            } else {
               var4 = 3;
            }
         } else {
            var4 = 2;
         }

         this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, var4, this.requestedInfo, (TLRPC.TL_shippingOption)null, (String)null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
      } else if (var1 == 1) {
         if (this.paymentForm.saved_credentials != null) {
            if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && UserConfig.getInstance(super.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 60) {
               UserConfig.getInstance(super.currentAccount).tmpPassword = null;
               UserConfig.getInstance(super.currentAccount).saveConfig(false);
            }

            if (UserConfig.getInstance(super.currentAccount).tmpPassword != null) {
               var4 = 4;
            } else {
               var4 = 3;
            }
         } else {
            var4 = 2;
         }

         this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, var4, this.requestedInfo, this.shippingOption, (String)null, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
      } else if (var1 == 2) {
         var2 = this.paymentForm;
         if (var2.password_missing) {
            boolean var3 = this.saveCardInfo;
            if (var3) {
               this.passwordFragment = new PaymentFormActivity(var2, this.messageObject, 6, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, var3, this.androidPayCredentials);
               this.passwordFragment.setCurrentPassword(this.currentPassword);
               this.passwordFragment.setDelegate(new PaymentFormActivity.PaymentFormActivityDelegate() {
                  public void currentPasswordUpdated(TLRPC.TL_account_password var1) {
                     PaymentFormActivity.this.currentPassword = var1;
                  }

                  public boolean didSelectNewCard(String var1, String var2, boolean var3, TLRPC.TL_inputPaymentCredentialsAndroidPay var4) {
                     if (PaymentFormActivity.this.delegate != null) {
                        PaymentFormActivity.this.delegate.didSelectNewCard(var1, var2, var3, var4);
                     }

                     if (PaymentFormActivity.this.isWebView) {
                        PaymentFormActivity.this.removeSelfFromStack();
                     }

                     if (PaymentFormActivity.this.delegate != null) {
                        var3 = true;
                     } else {
                        var3 = false;
                     }

                     return var3;
                  }

                  public void onFragmentDestroyed() {
                     PaymentFormActivity.this.passwordFragment = null;
                  }
               });
               this.presentFragment(this.passwordFragment, this.isWebView);
               return;
            }
         }

         PaymentFormActivity.PaymentFormActivityDelegate var5 = this.delegate;
         if (var5 != null) {
            var5.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials);
            this.finishFragment();
         } else {
            this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.isWebView);
         }
      } else if (var1 == 3) {
         if (this.passwordOk) {
            var4 = 4;
         } else {
            var4 = 2;
         }

         this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, var4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), this.passwordOk ^ true);
      } else if (var1 == 4) {
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.paymentFinished);
         this.finishFragment();
      } else if (var1 == 6) {
         if (!this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.androidPayCredentials)) {
            this.presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, 4, this.requestedInfo, this.shippingOption, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.androidPayCredentials), true);
         } else {
            this.finishFragment();
         }
      }

   }

   private void init(TLRPC.TL_payments_paymentForm var1, MessageObject var2, int var3, TLRPC.TL_payments_validatedRequestedInfo var4, TLRPC.TL_shippingOption var5, String var6, String var7, TLRPC.TL_payments_validateRequestedInfo var8, boolean var9, TLRPC.TL_inputPaymentCredentialsAndroidPay var10) {
      this.currentStep = var3;
      this.paymentJson = var6;
      this.androidPayCredentials = var10;
      this.requestedInfo = var4;
      this.paymentForm = var1;
      this.shippingOption = var5;
      this.messageObject = var2;
      this.saveCardInfo = var9;
      boolean var11 = "stripe".equals(this.paymentForm.native_provider);
      boolean var12 = true;
      this.isWebView = var11 ^ true;
      this.botUser = MessagesController.getInstance(super.currentAccount).getUser(var1.bot_id);
      TLRPC.User var14 = this.botUser;
      if (var14 != null) {
         this.currentBotName = var14.first_name;
      } else {
         this.currentBotName = "";
      }

      this.currentItemName = var2.messageOwner.media.title;
      this.validateRequest = var8;
      this.saveShippingInfo = true;
      if (var9) {
         this.saveCardInfo = var9;
      } else {
         if (this.paymentForm.saved_credentials != null) {
            var9 = var12;
         } else {
            var9 = false;
         }

         this.saveCardInfo = var9;
      }

      if (var7 == null) {
         TLRPC.TL_paymentSavedCredentialsCard var13 = var1.saved_credentials;
         if (var13 != null) {
            this.cardName = var13.title;
         }
      } else {
         this.cardName = var7;
      }

   }

   private void initAndroidPay(Context var1) {
   }

   // $FF: synthetic method
   public static void lambda$bzw4OVkpOzFWzvKyIdhX50nRu7A(PaymentFormActivity var0) {
      var0.goToNextStep();
   }

   // $FF: synthetic method
   static boolean lambda$createView$10(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static void lambda$null$17(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$null$34(TLObject var0, TLRPC.TL_error var1) {
   }

   private void loadPasswordInfo() {
      if (!this.loadingPasswordInfo) {
         this.loadingPasswordInfo = true;
         TLRPC.TL_account_getPassword var1 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR_HyfE(this), 10);
      }
   }

   private boolean sendCardData() {
      String[] var1 = this.inputFields[1].getText().toString().split("/");
      Integer var2;
      Integer var5;
      if (var1.length == 2) {
         var2 = Utilities.parseInt(var1[0]);
         var5 = Utilities.parseInt(var1[1]);
      } else {
         var2 = null;
         var5 = var2;
      }

      Card var6 = new Card(this.inputFields[0].getText().toString(), var2, var5, this.inputFields[3].getText().toString(), this.inputFields[2].getText().toString(), (String)null, (String)null, (String)null, (String)null, this.inputFields[5].getText().toString(), this.inputFields[4].getText().toString(), (String)null);
      StringBuilder var7 = new StringBuilder();
      var7.append(var6.getType());
      var7.append(" *");
      var7.append(var6.getLast4());
      this.cardName = var7.toString();
      if (!var6.validateNumber()) {
         this.shakeField(0);
         return false;
      } else if (var6.validateExpMonth() && var6.validateExpYear() && var6.validateExpiryDate()) {
         if (this.need_card_name && this.inputFields[2].length() == 0) {
            this.shakeField(2);
            return false;
         } else if (!var6.validateCVC()) {
            this.shakeField(3);
            return false;
         } else if (this.need_card_country && this.inputFields[4].length() == 0) {
            this.shakeField(4);
            return false;
         } else if (this.need_card_postcode && this.inputFields[5].length() == 0) {
            this.shakeField(5);
            return false;
         } else {
            this.showEditDoneProgress(true, true);

            try {
               Stripe var8 = new Stripe(this.stripeApiKey);
               TokenCallback var3 = new TokenCallback() {
                  // $FF: synthetic method
                  public void lambda$onSuccess$0$PaymentFormActivity$18() {
                     PaymentFormActivity.this.goToNextStep();
                     PaymentFormActivity.this.showEditDoneProgress(true, false);
                     PaymentFormActivity.this.setDonePressed(false);
                  }

                  public void onError(Exception var1) {
                     if (!PaymentFormActivity.this.canceled) {
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.setDonePressed(false);
                        if (!(var1 instanceof APIConnectionException) && !(var1 instanceof APIException)) {
                           AlertsCreator.showSimpleToast(PaymentFormActivity.this, var1.getMessage());
                        } else {
                           AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", 2131560372));
                        }

                     }
                  }

                  public void onSuccess(Token var1) {
                     if (!PaymentFormActivity.this.canceled) {
                        PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", var1.getType(), var1.getId());
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$18$QrVnMuLxiMtm_DJ7v5npJu9dxvA(this));
                     }
                  }
               };
               var8.createToken(var6, var3);
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            return true;
         }
      } else {
         this.shakeField(1);
         return false;
      }
   }

   private void sendData() {
      if (!this.canceled) {
         this.showEditDoneProgress(false, true);
         TLRPC.TL_payments_sendPaymentForm var1 = new TLRPC.TL_payments_sendPaymentForm();
         var1.msg_id = this.messageObject.getId();
         TLRPC.InputPaymentCredentials var3;
         if (UserConfig.getInstance(super.currentAccount).tmpPassword != null && this.paymentForm.saved_credentials != null) {
            var1.credentials = new TLRPC.TL_inputPaymentCredentialsSaved();
            var3 = var1.credentials;
            var3.id = this.paymentForm.saved_credentials.id;
            var3.tmp_password = UserConfig.getInstance(super.currentAccount).tmpPassword.tmp_password;
         } else {
            TLRPC.TL_inputPaymentCredentialsAndroidPay var2 = this.androidPayCredentials;
            if (var2 != null) {
               var1.credentials = var2;
            } else {
               var1.credentials = new TLRPC.TL_inputPaymentCredentials();
               var3 = var1.credentials;
               var3.save = this.saveCardInfo;
               var3.data = new TLRPC.TL_dataJSON();
               var1.credentials.data.data = this.paymentJson;
            }
         }

         TLRPC.TL_payments_validatedRequestedInfo var4 = this.requestedInfo;
         if (var4 != null) {
            String var5 = var4.id;
            if (var5 != null) {
               var1.requested_info_id = var5;
               var1.flags |= 1;
            }
         }

         TLRPC.TL_shippingOption var6 = this.shippingOption;
         if (var6 != null) {
            var1.shipping_option_id = var6.id;
            var1.flags |= 2;
         }

         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PaymentFormActivity$GlqRsRVH4sCdpkPSqoUCnb5fPtQ(this, var1), 2);
      }
   }

   private void sendForm() {
      if (!this.canceled) {
         this.showEditDoneProgress(true, true);
         this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
         TLRPC.TL_payments_validateRequestedInfo var1 = this.validateRequest;
         var1.save = this.saveShippingInfo;
         var1.msg_id = this.messageObject.getId();
         this.validateRequest.info = new TLRPC.TL_paymentRequestedInfo();
         TLRPC.TL_paymentRequestedInfo var3;
         if (this.paymentForm.invoice.name_requested) {
            this.validateRequest.info.name = this.inputFields[6].getText().toString();
            var3 = this.validateRequest.info;
            var3.flags |= 1;
         }

         if (this.paymentForm.invoice.phone_requested) {
            TLRPC.TL_paymentRequestedInfo var2 = this.validateRequest.info;
            StringBuilder var4 = new StringBuilder();
            var4.append("+");
            var4.append(this.inputFields[8].getText().toString());
            var4.append(this.inputFields[9].getText().toString());
            var2.phone = var4.toString();
            var3 = this.validateRequest.info;
            var3.flags |= 2;
         }

         if (this.paymentForm.invoice.email_requested) {
            this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
            var3 = this.validateRequest.info;
            var3.flags |= 4;
         }

         if (this.paymentForm.invoice.shipping_address_requested) {
            this.validateRequest.info.shipping_address = new TLRPC.TL_postAddress();
            this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
            this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
            TLRPC.TL_postAddress var5 = this.validateRequest.info.shipping_address;
            String var6 = this.countryName;
            if (var6 == null) {
               var6 = "";
            }

            var5.country_iso2 = var6;
            this.validateRequest.info.shipping_address.post_code = this.inputFields[5].getText().toString();
            var3 = this.validateRequest.info;
            var3.flags |= 8;
         }

         var1 = this.validateRequest;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(this.validateRequest, new _$$Lambda$PaymentFormActivity$UHlY3SoIRXNL32kFvxEl3vmTkBw(this, var1), 2);
      }
   }

   private void sendSavePassword(boolean var1) {
      String var2;
      if (!var1 && this.codeFieldCell.getVisibility() == 0) {
         var2 = this.codeFieldCell.getText();
         if (var2.length() == 0) {
            this.shakeView(this.codeFieldCell);
            return;
         }

         this.showEditDoneProgress(true, true);
         TLRPC.TL_account_confirmPasswordEmail var10 = new TLRPC.TL_account_confirmPasswordEmail();
         var10.code = var2;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var10, new _$$Lambda$PaymentFormActivity$YjpXWVCXfyYjoMEGmUOokMuT9II(this), 10);
      } else {
         TLRPC.TL_account_updatePasswordSettings var4 = new TLRPC.TL_account_updatePasswordSettings();
         String var3;
         if (var1) {
            this.doneItem.setVisibility(0);
            var4.new_settings = new TLRPC.TL_account_passwordInputSettings();
            TLRPC.TL_account_passwordInputSettings var9 = var4.new_settings;
            var9.flags = 2;
            var9.email = "";
            var4.password = new TLRPC.TL_inputCheckPasswordEmpty();
            var2 = null;
            var3 = var2;
         } else {
            var3 = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(var3)) {
               this.shakeField(0);
               return;
            }

            if (!var3.equals(this.inputFields[1].getText().toString())) {
               try {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("PasswordDoNotMatch", 2131560346), 0).show();
               } catch (Exception var8) {
                  FileLog.e((Throwable)var8);
               }

               this.shakeField(1);
               return;
            }

            var2 = this.inputFields[2].getText().toString();
            if (var2.length() < 3) {
               this.shakeField(2);
               return;
            }

            int var5 = var2.lastIndexOf(46);
            int var6 = var2.lastIndexOf(64);
            if (var6 < 0 || var5 < var6) {
               this.shakeField(2);
               return;
            }

            var4.password = new TLRPC.TL_inputCheckPasswordEmpty();
            var4.new_settings = new TLRPC.TL_account_passwordInputSettings();
            TLRPC.TL_account_passwordInputSettings var7 = var4.new_settings;
            var7.flags |= 1;
            var7.hint = "";
            var7.new_algo = this.currentPassword.new_algo;
            if (var2.length() > 0) {
               var7 = var4.new_settings;
               var7.flags |= 2;
               var7.email = var2.trim();
            }
         }

         this.showEditDoneProgress(true, true);
         Utilities.globalQueue.postRunnable(new _$$Lambda$PaymentFormActivity$v6Le3V4A5uKjdxXgwOCsgIj66wA(this, var1, var2, var3, var4));
      }

   }

   private void setCurrentPassword(TLRPC.TL_account_password var1) {
      if (var1.has_password) {
         if (this.getParentActivity() == null) {
            return;
         }

         this.goToNextStep();
      } else {
         this.currentPassword = var1;
         var1 = this.currentPassword;
         if (var1 != null) {
            this.waitingForEmail = TextUtils.isEmpty(var1.email_unconfirmed_pattern) ^ true;
         }

         this.updatePasswordFields();
      }

   }

   private void setDelegate(PaymentFormActivity.PaymentFormActivityDelegate var1) {
      this.delegate = var1;
   }

   private void setDonePressed(boolean var1) {
      this.donePressed = var1;
      super.swipeBackEnabled = var1 ^ true;
      super.actionBar.getBackButton().setEnabled(this.donePressed ^ true);
      TextDetailSettingsCell[] var2 = this.detailSettingsCell;
      if (var2[0] != null) {
         var2[0].setEnabled(this.donePressed ^ true);
      }

   }

   private void shakeField(int var1) {
      this.shakeView(this.inputFields[var1]);
   }

   private void shakeView(View var1) {
      Vibrator var2 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
      if (var2 != null) {
         var2.vibrate(200L);
      }

      AndroidUtilities.shakeView(var1, 2.0F, 0);
   }

   private void showAlertWithText(String var1, String var2) {
      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
      var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
      var3.setTitle(var1);
      var3.setMessage(var2);
      this.showDialog(var3.create());
   }

   private void showEditDoneProgress(boolean var1, final boolean var2) {
      AnimatorSet var3 = this.doneItemAnimation;
      if (var3 != null) {
         var3.cancel();
      }

      if (var1 && this.doneItem != null) {
         this.doneItemAnimation = new AnimatorSet();
         if (var2) {
            this.progressView.setVisibility(0);
            this.doneItem.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{1.0F})});
         } else if (this.webView != null) {
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0F})});
         } else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(var1)) {
                  PaymentFormActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(var1)) {
                  if (!var2) {
                     PaymentFormActivity.this.progressView.setVisibility(4);
                  } else {
                     PaymentFormActivity.this.doneItem.getImageView().setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      } else if (this.payTextView != null) {
         this.doneItemAnimation = new AnimatorSet();
         if (var2) {
            this.progressViewButton.setVisibility(0);
            this.bottomLayout.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.payTextView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.payTextView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.payTextView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressViewButton, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressViewButton, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressViewButton, "alpha", new float[]{1.0F})});
         } else {
            this.payTextView.setVisibility(0);
            this.bottomLayout.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressViewButton, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressViewButton, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressViewButton, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.payTextView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.payTextView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.payTextView, "alpha", new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(var1)) {
                  PaymentFormActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(var1)) {
                  if (!var2) {
                     PaymentFormActivity.this.progressViewButton.setVisibility(4);
                  } else {
                     PaymentFormActivity.this.payTextView.setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      }

   }

   private void showPayAlert(String var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setTitle(LocaleController.getString("PaymentTransactionReview", 2131560408));
      var2.setMessage(LocaleController.formatString("PaymentTransactionMessage", 2131560407, var1, this.currentBotName, this.currentItemName));
      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PaymentFormActivity$zNiawUMZQLp4e5nGq2pzsvlUWzE(this));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      this.showDialog(var2.create());
   }

   private void updatePasswordFields() {
      if (this.currentStep == 6 && this.bottomCell[2] != null) {
         ActionBarMenuItem var1 = this.doneItem;
         byte var2 = 0;
         byte var3 = 0;
         var1.setVisibility(0);
         int var4;
         int var5;
         if (this.currentPassword == null) {
            this.showEditDoneProgress(true, true);
            this.bottomCell[2].setVisibility(8);
            this.settingsCell[0].setVisibility(8);
            this.settingsCell[1].setVisibility(8);
            this.codeFieldCell.setVisibility(8);
            this.headerCell[0].setVisibility(8);
            this.headerCell[1].setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            var4 = 0;

            while(true) {
               var5 = var3;
               if (var4 >= 3) {
                  while(var5 < this.dividers.size()) {
                     ((View)this.dividers.get(var5)).setVisibility(8);
                     ++var5;
                  }
                  break;
               }

               ((View)this.inputFields[var4].getParent()).setVisibility(8);
               ++var4;
            }
         } else {
            this.showEditDoneProgress(true, false);
            if (this.waitingForEmail) {
               TextInfoPrivacyCell var6 = this.bottomCell[2];
               String var7 = this.currentPassword.email_unconfirmed_pattern;
               if (var7 == null) {
                  var7 = "";
               }

               var6.setText(LocaleController.formatString("EmailPasswordConfirmText2", 2131559329, var7));
               this.bottomCell[2].setVisibility(0);
               this.settingsCell[0].setVisibility(0);
               this.settingsCell[1].setVisibility(0);
               this.codeFieldCell.setVisibility(0);
               this.bottomCell[1].setText("");
               this.headerCell[0].setVisibility(8);
               this.headerCell[1].setVisibility(8);
               this.bottomCell[0].setVisibility(8);
               var5 = 0;

               while(true) {
                  var4 = var2;
                  if (var5 >= 3) {
                     while(var4 < this.dividers.size()) {
                        ((View)this.dividers.get(var4)).setVisibility(8);
                        ++var4;
                     }
                     break;
                  }

                  ((View)this.inputFields[var5].getParent()).setVisibility(8);
                  ++var5;
               }
            } else {
               this.bottomCell[2].setVisibility(8);
               this.settingsCell[0].setVisibility(8);
               this.settingsCell[1].setVisibility(8);
               this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", 2131560379));
               this.codeFieldCell.setVisibility(8);
               this.headerCell[0].setVisibility(0);
               this.headerCell[1].setVisibility(0);
               this.bottomCell[0].setVisibility(0);

               for(var5 = 0; var5 < 3; ++var5) {
                  ((View)this.inputFields[var5].getParent()).setVisibility(0);
               }

               for(var5 = 0; var5 < this.dividers.size(); ++var5) {
                  ((View)this.dividers.get(var5)).setVisibility(0);
               }
            }
         }
      }

   }

   private void updateSavePaymentField() {
      if (this.bottomCell[0] != null && this.sectionCell[2] != null) {
         TLRPC.TL_payments_paymentForm var1 = this.paymentForm;
         ShadowSectionCell[] var7;
         if (var1.password_missing || var1.can_save_credentials) {
            WebView var6 = this.webView;
            if (var6 == null || var6 != null && !this.webviewLoading) {
               SpannableStringBuilder var8 = new SpannableStringBuilder(LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", 2131560359));
               if (this.paymentForm.password_missing) {
                  this.loadPasswordInfo();
                  var8.append("\n");
                  int var2 = var8.length();
                  String var3 = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", 2131560360);
                  int var4 = var3.indexOf(42);
                  int var5 = var3.lastIndexOf(42);
                  var8.append(var3);
                  if (var4 != -1 && var5 != -1) {
                     var4 += var2;
                     var2 += var5;
                     this.bottomCell[0].getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                     var8.replace(var2, var2 + 1, "");
                     var8.replace(var4, var4 + 1, "");
                     var8.setSpan(new PaymentFormActivity.LinkSpan(), var4, var2 - 1, 33);
                  }
               }

               this.checkCell1.setEnabled(true);
               this.bottomCell[0].setText(var8);
               this.checkCell1.setVisibility(0);
               this.bottomCell[0].setVisibility(0);
               var7 = this.sectionCell;
               var7[2].setBackgroundDrawable(Theme.getThemedDrawable(var7[2].getContext(), 2131165394, "windowBackgroundGrayShadow"));
               return;
            }
         }

         this.checkCell1.setVisibility(8);
         this.bottomCell[0].setVisibility(8);
         var7 = this.sectionCell;
         var7[2].setBackgroundDrawable(Theme.getThemedDrawable(var7[2].getContext(), 2131165395, "windowBackgroundGrayShadow"));
      }

   }

   @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
   public View createView(Context var1) {
      int var2 = this.currentStep;
      if (var2 == 0) {
         super.actionBar.setTitle(LocaleController.getString("PaymentShippingInfo", 2131560395));
      } else if (var2 == 1) {
         super.actionBar.setTitle(LocaleController.getString("PaymentShippingMethod", 2131560396));
      } else if (var2 == 2) {
         super.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", 2131560355));
      } else if (var2 == 3) {
         super.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", 2131560355));
      } else if (var2 == 4) {
         if (this.paymentForm.invoice.test) {
            ActionBar var3 = super.actionBar;
            StringBuilder var4 = new StringBuilder();
            var4.append("Test ");
            var4.append(LocaleController.getString("PaymentCheckout", 2131560362));
            var3.setTitle(var4.toString());
         } else {
            super.actionBar.setTitle(LocaleController.getString("PaymentCheckout", 2131560362));
         }
      } else if (var2 == 5) {
         if (this.paymentForm.invoice.test) {
            ActionBar var48 = super.actionBar;
            StringBuilder var41 = new StringBuilder();
            var41.append("Test ");
            var41.append(LocaleController.getString("PaymentReceipt", 2131560388));
            var48.setTitle(var41.toString());
         } else {
            super.actionBar.setTitle(LocaleController.getString("PaymentReceipt", 2131560388));
         }
      } else if (var2 == 6) {
         super.actionBar.setTitle(LocaleController.getString("PaymentPassword", 2131560377));
      }

      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (PaymentFormActivity.this.donePressed) {
                  return;
               }

               PaymentFormActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (PaymentFormActivity.this.donePressed) {
                  return;
               }

               if (PaymentFormActivity.this.currentStep != 3) {
                  AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
               }

               if (PaymentFormActivity.this.currentStep == 0) {
                  PaymentFormActivity.this.setDonePressed(true);
                  PaymentFormActivity.this.sendForm();
               } else {
                  int var2 = PaymentFormActivity.this.currentStep;
                  var1 = 0;
                  if (var2 != 1) {
                     if (PaymentFormActivity.this.currentStep == 2) {
                        PaymentFormActivity.this.sendCardData();
                     } else if (PaymentFormActivity.this.currentStep == 3) {
                        PaymentFormActivity.this.checkPassword();
                     } else if (PaymentFormActivity.this.currentStep == 6) {
                        PaymentFormActivity.this.sendSavePassword(false);
                     }
                  } else {
                     while(var1 < PaymentFormActivity.this.radioCells.length) {
                        if (PaymentFormActivity.this.radioCells[var1].isChecked()) {
                           PaymentFormActivity var3 = PaymentFormActivity.this;
                           var3.shippingOption = (TLRPC.TL_shippingOption)var3.requestedInfo.shipping_options.get(var1);
                           break;
                        }

                        ++var1;
                     }

                     PaymentFormActivity.this.goToNextStep();
                  }
               }
            }

         }
      });
      ActionBarMenu var43 = super.actionBar.createMenu();
      var2 = this.currentStep;
      if (var2 == 0 || var2 == 1 || var2 == 2 || var2 == 3 || var2 == 4 || var2 == 6) {
         this.doneItem = var43.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
         this.progressView = new ContextProgressView(var1, 1);
         this.progressView.setAlpha(0.0F);
         this.progressView.setScaleX(0.1F);
         this.progressView.setScaleY(0.1F);
         this.progressView.setVisibility(4);
         this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      }

      super.fragmentView = new FrameLayout(var1);
      View var45 = super.fragmentView;
      FrameLayout var5 = (FrameLayout)var45;
      var45.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.scrollView = new ScrollView(var1);
      this.scrollView.setFillViewport(true);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
      ScrollView var47 = this.scrollView;
      float var6;
      if (this.currentStep == 4) {
         var6 = 48.0F;
      } else {
         var6 = 0.0F;
      }

      var5.addView(var47, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, var6));
      this.linearLayout2 = new LinearLayout(var1);
      this.linearLayout2.setOrientation(1);
      this.scrollView.addView(this.linearLayout2, new LayoutParams(-1, -2));
      var2 = this.currentStep;
      int var8;
      boolean var9;
      Exception var10000;
      boolean var10001;
      boolean var39;
      byte var46;
      Exception var53;
      String var54;
      TLRPC.TL_postAddress var63;
      TLRPC.User var65;
      String var66;
      LengthFilter var74;
      EditTextBoldCursor var75;
      TLRPC.User var77;
      TLRPC.TL_invoice var79;
      if (var2 == 0) {
         HashMap var56 = new HashMap();
         HashMap var7 = new HashMap();

         label878: {
            label877: {
               BufferedReader var51;
               try {
                  InputStreamReader var50 = new InputStreamReader(var1.getResources().getAssets().open("countries.txt"));
                  var51 = new BufferedReader(var50);
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label877;
               }

               while(true) {
                  try {
                     var54 = var51.readLine();
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break;
                  }

                  if (var54 == null) {
                     try {
                        var51.close();
                        break label878;
                     } catch (Exception var31) {
                        var10000 = var31;
                        var10001 = false;
                        break;
                     }
                  }

                  String[] var57;
                  try {
                     var57 = var54.split(";");
                     this.countriesArray.add(0, var57[2]);
                     this.countriesMap.put(var57[2], var57[0]);
                     this.codesMap.put(var57[0], var57[2]);
                     var7.put(var57[1], var57[2]);
                     if (var57.length > 3) {
                        this.phoneFormatMap.put(var57[0], var57[3]);
                     }
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break;
                  }

                  try {
                     var56.put(var57[1], var57[2]);
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break;
                  }
               }
            }

            var53 = var10000;
            FileLog.e((Throwable)var53);
         }

         Collections.sort(this.countriesArray, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
         this.inputFields = new EditTextBoldCursor[10];

         TLRPC.TL_invoice var58;
         for(var8 = 0; var8 < 10; ++var8) {
            if (var8 == 0) {
               this.headerCell[0] = new HeaderCell(var1);
               this.headerCell[0].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               this.headerCell[0].setText(LocaleController.getString("PaymentShippingAddress", 2131560389));
               this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
            } else if (var8 == 6) {
               this.sectionCell[0] = new ShadowSectionCell(var1);
               this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
               this.headerCell[1] = new HeaderCell(var1);
               this.headerCell[1].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               this.headerCell[1].setText(LocaleController.getString("PaymentShippingReceiver", 2131560399));
               this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
            }

            Object var59;
            if (var8 == 8) {
               var59 = new LinearLayout(var1);
               ((LinearLayout)var59).setOrientation(0);
               this.linearLayout2.addView((View)var59, LayoutHelper.createLinear(-1, 50));
               ((ViewGroup)var59).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (var8 == 9) {
               var59 = (ViewGroup)this.inputFields[8].getParent();
            } else {
               FrameLayout var60 = new FrameLayout(var1);
               this.linearLayout2.addView(var60, LayoutHelper.createLinear(-1, 50));
               var60.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               if (var8 != 5 && var8 != 9) {
                  var39 = true;
               } else {
                  var39 = false;
               }

               var9 = var39;
               if (var39) {
                  label947: {
                     if (var8 != 7 || this.paymentForm.invoice.phone_requested) {
                        var9 = var39;
                        if (var8 != 6) {
                           break label947;
                        }

                        var58 = this.paymentForm.invoice;
                        var9 = var39;
                        if (var58.phone_requested) {
                           break label947;
                        }

                        var9 = var39;
                        if (var58.email_requested) {
                           break label947;
                        }
                     }

                     var9 = false;
                  }
               }

               var59 = var60;
               if (var9) {
                  var45 = new View(var1) {
                     protected void onDraw(Canvas var1) {
                        float var2;
                        if (LocaleController.isRTL) {
                           var2 = 0.0F;
                        } else {
                           var2 = (float)AndroidUtilities.dp(20.0F);
                        }

                        float var3 = (float)(this.getMeasuredHeight() - 1);
                        int var4 = this.getMeasuredWidth();
                        int var5;
                        if (LocaleController.isRTL) {
                           var5 = AndroidUtilities.dp(20.0F);
                        } else {
                           var5 = 0;
                        }

                        var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                     }
                  };
                  var45.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  this.dividers.add(var45);
                  var60.addView(var45, new LayoutParams(-1, 1, 83));
                  var59 = var60;
               }
            }

            if (var8 == 9) {
               this.inputFields[var8] = new HintEditText(var1);
            } else {
               this.inputFields[var8] = new EditTextBoldCursor(var1);
            }

            this.inputFields[var8].setTag(var8);
            this.inputFields[var8].setTextSize(1, 16.0F);
            this.inputFields[var8].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[var8].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[var8].setBackgroundDrawable((Drawable)null);
            this.inputFields[var8].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[var8].setCursorSize(AndroidUtilities.dp(20.0F));
            this.inputFields[var8].setCursorWidth(1.5F);
            if (var8 == 4) {
               this.inputFields[var8].setOnTouchListener(new _$$Lambda$PaymentFormActivity$uDw_faglZkNLuTGLVBNZMQdnOLU(this));
               this.inputFields[var8].setInputType(0);
            }

            if (var8 != 9 && var8 != 8) {
               if (var8 == 7) {
                  this.inputFields[var8].setInputType(1);
               } else {
                  this.inputFields[var8].setInputType(16385);
               }
            } else {
               this.inputFields[var8].setInputType(3);
            }

            this.inputFields[var8].setImeOptions(268435461);
            TLRPC.TL_paymentRequestedInfo var61;
            switch(var8) {
            case 0:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingAddress1Placeholder", 2131560390));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     this.inputFields[var8].setText(var63.street_line1);
                  }
               }
               break;
            case 1:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingAddress2Placeholder", 2131560391));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     this.inputFields[var8].setText(var63.street_line2);
                  }
               }
               break;
            case 2:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingCityPlaceholder", 2131560392));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     this.inputFields[var8].setText(var63.city);
                  }
               }
               break;
            case 3:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingStatePlaceholder", 2131560402));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     this.inputFields[var8].setText(var63.state);
                  }
               }
               break;
            case 4:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingCountry", 2131560393));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     var54 = (String)var7.get(var63.country_iso2);
                     this.countryName = this.paymentForm.saved_info.shipping_address.country_iso2;
                     EditTextBoldCursor var10 = this.inputFields[var8];
                     if (var54 == null) {
                        var54 = this.countryName;
                     }

                     var10.setText(var54);
                  }
               }
               break;
            case 5:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", 2131560403));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var63 = var61.shipping_address;
                  if (var63 != null) {
                     this.inputFields[var8].setText(var63.post_code);
                  }
               }
               break;
            case 6:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingName", 2131560397));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var54 = var61.name;
                  if (var54 != null) {
                     this.inputFields[var8].setText(var54);
                  }
               }
               break;
            case 7:
               this.inputFields[var8].setHint(LocaleController.getString("PaymentShippingEmailPlaceholder", 2131560394));
               var61 = this.paymentForm.saved_info;
               if (var61 != null) {
                  var54 = var61.email;
                  if (var54 != null) {
                     this.inputFields[var8].setText(var54);
                  }
               }
            }

            EditTextBoldCursor[] var73 = this.inputFields;
            var73[var8].setSelection(var73[var8].length());
            if (var8 == 8) {
               this.textView = new TextView(var1);
               this.textView.setText("+");
               this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               this.textView.setTextSize(1, 16.0F);
               ((ViewGroup)var59).addView(this.textView, LayoutHelper.createLinear(-2, -2, 21.0F, 12.0F, 0.0F, 6.0F));
               this.inputFields[var8].setPadding(AndroidUtilities.dp(10.0F), 0, 0, 0);
               this.inputFields[var8].setGravity(19);
               var74 = new LengthFilter(5);
               this.inputFields[var8].setFilters(new InputFilter[]{var74});
               ((ViewGroup)var59).addView(this.inputFields[var8], LayoutHelper.createLinear(55, -2, 0.0F, 12.0F, 21.0F, 6.0F));
               this.inputFields[var8].addTextChangedListener(new TextWatcher() {
                  public void afterTextChanged(Editable var1) {
                     if (!PaymentFormActivity.this.ignoreOnTextChange) {
                        PaymentFormActivity var8 = PaymentFormActivity.this;
                        boolean var2 = true;
                        var8.ignoreOnTextChange = true;
                        String var9 = PhoneFormat.stripExceptNumbers(PaymentFormActivity.this.inputFields[8].getText().toString());
                        PaymentFormActivity.this.inputFields[8].setText(var9);
                        HintEditText var3 = (HintEditText)PaymentFormActivity.this.inputFields[9];
                        if (var9.length() == 0) {
                           var3.setHintText((String)null);
                           var3.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                        } else {
                           int var4 = var9.length();
                           int var5 = 4;
                           String var6;
                           boolean var10;
                           String var12;
                           if (var4 <= 4) {
                              var12 = null;
                              var10 = false;
                           } else {
                              StringBuilder var7;
                              while(true) {
                                 if (var5 < 1) {
                                    var12 = null;
                                    var10 = false;
                                    break;
                                 }

                                 var6 = var9.substring(0, var5);
                                 if ((String)PaymentFormActivity.this.codesMap.get(var6) != null) {
                                    var7 = new StringBuilder();
                                    var7.append(var9.substring(var5));
                                    var7.append(PaymentFormActivity.this.inputFields[9].getText().toString());
                                    var12 = var7.toString();
                                    PaymentFormActivity.this.inputFields[8].setText(var6);
                                    var10 = true;
                                    var9 = var6;
                                    break;
                                 }

                                 --var5;
                              }

                              if (!var10) {
                                 var7 = new StringBuilder();
                                 var7.append(var9.substring(1));
                                 var7.append(PaymentFormActivity.this.inputFields[9].getText().toString());
                                 var12 = var7.toString();
                                 EditTextBoldCursor var11 = PaymentFormActivity.this.inputFields[8];
                                 var9 = var9.substring(0, 1);
                                 var11.setText(var9);
                              }
                           }

                           label41: {
                              var6 = (String)PaymentFormActivity.this.codesMap.get(var9);
                              if (var6 != null && PaymentFormActivity.this.countriesArray.indexOf(var6) != -1) {
                                 var9 = (String)PaymentFormActivity.this.phoneFormatMap.get(var9);
                                 if (var9 != null) {
                                    var3.setHintText(var9.replace('X', '–'));
                                    var3.setHint((CharSequence)null);
                                    break label41;
                                 }
                              }

                              var2 = false;
                           }

                           if (!var2) {
                              var3.setHintText((String)null);
                              var3.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                           }

                           if (!var10) {
                              PaymentFormActivity.this.inputFields[8].setSelection(PaymentFormActivity.this.inputFields[8].getText().length());
                           }

                           if (var12 != null) {
                              var3.requestFocus();
                              var3.setText(var12);
                              var3.setSelection(var3.length());
                           }
                        }

                        PaymentFormActivity.this.ignoreOnTextChange = false;
                     }
                  }

                  public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                  }

                  public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                  }
               });
            } else if (var8 == 9) {
               this.inputFields[var8].setPadding(0, 0, 0, 0);
               this.inputFields[var8].setGravity(19);
               ((ViewGroup)var59).addView(this.inputFields[var8], LayoutHelper.createLinear(-1, -2, 0.0F, 12.0F, 21.0F, 6.0F));
               this.inputFields[var8].addTextChangedListener(new TextWatcher() {
                  private int actionPosition;
                  private int characterAction = -1;

                  public void afterTextChanged(Editable var1) {
                     if (!PaymentFormActivity.this.ignoreOnPhoneChange) {
                        HintEditText var2 = (HintEditText)PaymentFormActivity.this.inputFields[9];
                        int var3 = var2.getSelectionStart();
                        String var4 = var2.getText().toString();
                        int var5 = var3;
                        String var10 = var4;
                        if (this.characterAction == 3) {
                           StringBuilder var11 = new StringBuilder();
                           var11.append(var4.substring(0, this.actionPosition));
                           var11.append(var4.substring(this.actionPosition + 1));
                           var10 = var11.toString();
                           var5 = var3 - 1;
                        }

                        StringBuilder var12 = new StringBuilder(var10.length());

                        int var6;
                        for(var3 = 0; var3 < var10.length(); var3 = var6) {
                           var6 = var3 + 1;
                           String var7 = var10.substring(var3, var6);
                           if ("0123456789".contains(var7)) {
                              var12.append(var7);
                           }
                        }

                        PaymentFormActivity.this.ignoreOnPhoneChange = true;
                        var10 = var2.getHintText();
                        var3 = var5;
                        if (var10 != null) {
                           label71: {
                              for(var3 = 0; var3 < var12.length(); var5 = var6) {
                                 if (var3 >= var10.length()) {
                                    var12.insert(var3, ' ');
                                    if (var5 == var3 + 1) {
                                       var3 = this.characterAction;
                                       if (var3 != 2 && var3 != 3) {
                                          var3 = var5 + 1;
                                          break label71;
                                       }
                                    }
                                    break;
                                 }

                                 int var8 = var3;
                                 var6 = var5;
                                 if (var10.charAt(var3) == ' ') {
                                    var12.insert(var3, ' ');
                                    ++var3;
                                    var8 = var3;
                                    var6 = var5;
                                    if (var5 == var3) {
                                       int var9 = this.characterAction;
                                       var8 = var3;
                                       var6 = var5;
                                       if (var9 != 2) {
                                          var8 = var3;
                                          var6 = var5;
                                          if (var9 != 3) {
                                             var6 = var5 + 1;
                                             var8 = var3;
                                          }
                                       }
                                    }
                                 }

                                 var3 = var8 + 1;
                              }

                              var3 = var5;
                           }
                        }

                        var2.setText(var12);
                        if (var3 >= 0) {
                           if (var3 > var2.length()) {
                              var3 = var2.length();
                           }

                           var2.setSelection(var3);
                        }

                        var2.onTextChange();
                        PaymentFormActivity.this.ignoreOnPhoneChange = false;
                     }
                  }

                  public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     if (var3 == 0 && var4 == 1) {
                        this.characterAction = 1;
                     } else if (var3 == 1 && var4 == 0) {
                        if (var1.charAt(var2) == ' ' && var2 > 0) {
                           this.characterAction = 3;
                           this.actionPosition = var2 - 1;
                        } else {
                           this.characterAction = 2;
                        }
                     } else {
                        this.characterAction = -1;
                     }

                  }

                  public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                  }
               });
            } else {
               this.inputFields[var8].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
               var75 = this.inputFields[var8];
               if (LocaleController.isRTL) {
                  var46 = 5;
               } else {
                  var46 = 3;
               }

               var75.setGravity(var46);
               ((ViewGroup)var59).addView(this.inputFields[var8], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
            }

            this.inputFields[var8].setOnEditorActionListener(new _$$Lambda$PaymentFormActivity$waFpzGxIzLdyjPlQN2X3YYqAdDY(this));
            if (var8 == 9) {
               var58 = this.paymentForm.invoice;
               if (!var58.email_to_provider && !var58.phone_to_provider) {
                  this.sectionCell[1] = new ShadowSectionCell(var1);
                  this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
               } else {
                  var2 = 0;

                  for(var65 = null; var2 < this.paymentForm.users.size(); ++var2) {
                     var77 = (TLRPC.User)this.paymentForm.users.get(var2);
                     if (var77.id == this.paymentForm.provider_id) {
                        var65 = var77;
                     }
                  }

                  if (var65 != null) {
                     var66 = ContactsController.formatName(var65.first_name, var65.last_name);
                  } else {
                     var66 = "";
                  }

                  this.bottomCell[1] = new TextInfoPrivacyCell(var1);
                  this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
                  this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                  var79 = this.paymentForm.invoice;
                  if (var79.email_to_provider && var79.phone_to_provider) {
                     this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneEmailToProvider", 2131560385, var66));
                  } else if (this.paymentForm.invoice.email_to_provider) {
                     this.bottomCell[1].setText(LocaleController.formatString("PaymentEmailToProvider", 2131560373, var66));
                  } else {
                     this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneToProvider", 2131560386, var66));
                  }
               }

               this.checkCell1 = new TextCheckCell(var1);
               this.checkCell1.setBackgroundDrawable(Theme.getSelectorDrawable(true));
               this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentShippingSave", 2131560400), this.saveShippingInfo, false);
               this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
               this.checkCell1.setOnClickListener(new _$$Lambda$PaymentFormActivity$ja6Gof_oEgJFLBPAYcrRraILPks(this));
               this.bottomCell[0] = new TextInfoPrivacyCell(var1);
               this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
               this.bottomCell[0].setText(LocaleController.getString("PaymentShippingSaveInfo", 2131560401));
               this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
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

         TLRPC.TL_invoice var36 = this.paymentForm.invoice;
         if (var36.phone_requested) {
            this.inputFields[9].setImeOptions(268435462);
         } else if (var36.email_requested) {
            this.inputFields[7].setImeOptions(268435462);
         } else if (var36.name_requested) {
            this.inputFields[6].setImeOptions(268435462);
         } else {
            this.inputFields[5].setImeOptions(268435462);
         }

         ShadowSectionCell[] var37 = this.sectionCell;
         if (var37[1] != null) {
            ShadowSectionCell var71 = var37[1];
            var36 = this.paymentForm.invoice;
            if (!var36.name_requested && !var36.phone_requested && !var36.email_requested) {
               var46 = 8;
            } else {
               var46 = 0;
            }

            var71.setVisibility(var46);
         } else {
            TextInfoPrivacyCell[] var38 = this.bottomCell;
            if (var38[1] != null) {
               TextInfoPrivacyCell var69 = var38[1];
               var36 = this.paymentForm.invoice;
               if (!var36.name_requested && !var36.phone_requested && !var36.email_requested) {
                  var46 = 8;
               } else {
                  var46 = 0;
               }

               var69.setVisibility(var46);
            }
         }

         HeaderCell var40 = this.headerCell[1];
         var58 = this.paymentForm.invoice;
         if (!var58.name_requested && !var58.phone_requested && !var58.email_requested) {
            var46 = 8;
         } else {
            var46 = 0;
         }

         var40.setVisibility(var46);
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

         TLRPC.TL_paymentRequestedInfo var42 = this.paymentForm.saved_info;
         if (var42 != null && !TextUtils.isEmpty(var42.phone)) {
            this.fillNumber(this.paymentForm.saved_info.phone);
         } else {
            this.fillNumber((String)null);
         }

         if (this.inputFields[8].length() == 0) {
            TLRPC.TL_payments_paymentForm var44 = this.paymentForm;
            if (var44.invoice.phone_requested) {
               var42 = var44.saved_info;
               if (var42 == null || TextUtils.isEmpty(var42.phone)) {
                  String var55;
                  label608: {
                     label607: {
                        label904: {
                           TelephonyManager var49;
                           try {
                              var49 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label904;
                           }

                           if (var49 == null) {
                              break label607;
                           }

                           try {
                              var55 = var49.getSimCountryIso().toUpperCase();
                              break label608;
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                           }
                        }

                        Exception var52 = var10000;
                        FileLog.e((Throwable)var52);
                     }

                     var55 = null;
                  }

                  if (var55 != null) {
                     var55 = (String)var56.get(var55);
                     if (var55 != null && this.countriesArray.indexOf(var55) != -1) {
                        this.inputFields[8].setText((CharSequence)this.countriesMap.get(var55));
                     }
                  }
               }
            }
         }
      } else {
         FrameLayout var76;
         View var83;
         if (var2 == 2) {
            TLRPC.TL_dataJSON var80 = this.paymentForm.native_params;
            JSONObject var72;
            if (var80 != null) {
               label928: {
                  label906: {
                     try {
                        var72 = new JSONObject(var80.data);
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label906;
                     }

                     try {
                        var54 = var72.getString("android_pay_public_key");
                        if (!TextUtils.isEmpty(var54)) {
                           this.androidPayPublicKey = var54;
                        }
                     } catch (Exception var29) {
                        try {
                           this.androidPayPublicKey = null;
                        } catch (Exception var28) {
                           var10000 = var28;
                           var10001 = false;
                           break label906;
                        }
                     }

                     try {
                        this.androidPayBackgroundColor = var72.getInt("android_pay_bgcolor") | -16777216;
                     } catch (Exception var27) {
                        try {
                           this.androidPayBackgroundColor = -1;
                        } catch (Exception var26) {
                           var10000 = var26;
                           var10001 = false;
                           break label906;
                        }
                     }

                     try {
                        this.androidPayBlackTheme = var72.getBoolean("android_pay_inverse");
                        break label928;
                     } catch (Exception var25) {
                        try {
                           this.androidPayBlackTheme = false;
                           break label928;
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                        }
                     }
                  }

                  var53 = var10000;
                  FileLog.e((Throwable)var53);
               }
            }

            if (this.isWebView) {
               if (this.androidPayPublicKey != null) {
                  this.initAndroidPay(var1);
               }

               this.androidPayContainer = new FrameLayout(var1);
               this.androidPayContainer.setId(4000);
               this.androidPayContainer.setBackgroundDrawable(Theme.getSelectorDrawable(true));
               this.androidPayContainer.setVisibility(8);
               this.linearLayout2.addView(this.androidPayContainer, LayoutHelper.createLinear(-1, 50));
               this.webviewLoading = true;
               this.showEditDoneProgress(true, true);
               this.progressView.setVisibility(0);
               this.doneItem.setEnabled(false);
               this.doneItem.getImageView().setVisibility(4);
               this.webView = new WebView(var1) {
                  protected void onMeasure(int var1, int var2) {
                     super.onMeasure(var1, var2);
                  }

                  public boolean onTouchEvent(MotionEvent var1) {
                     ((ViewGroup)PaymentFormActivity.access$1900(PaymentFormActivity.this)).requestDisallowInterceptTouchEvent(true);
                     return super.onTouchEvent(var1);
                  }
               };
               this.webView.getSettings().setJavaScriptEnabled(true);
               this.webView.getSettings().setDomStorageEnabled(true);
               if (VERSION.SDK_INT >= 21) {
                  this.webView.getSettings().setMixedContentMode(0);
                  CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
               }

               if (VERSION.SDK_INT >= 17) {
                  this.webView.addJavascriptInterface(new PaymentFormActivity.TelegramWebviewProxy(), "TelegramWebviewProxy");
               }

               this.webView.setWebViewClient(new WebViewClient() {
                  public void onLoadResource(WebView var1, String var2) {
                     super.onLoadResource(var1, var2);
                  }

                  public void onPageFinished(WebView var1, String var2) {
                     super.onPageFinished(var1, var2);
                     PaymentFormActivity.this.webviewLoading = false;
                     PaymentFormActivity.this.showEditDoneProgress(true, false);
                     PaymentFormActivity.this.updateSavePaymentField();
                  }

                  public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
                     PaymentFormActivity var3 = PaymentFormActivity.this;
                     var3.shouldNavigateBack = var2.equals(var3.webViewUrl) ^ true;
                     return super.shouldOverrideUrlLoading(var1, var2);
                  }
               });
               this.linearLayout2.addView(this.webView, LayoutHelper.createFrame(-1, -2.0F));
               this.sectionCell[2] = new ShadowSectionCell(var1);
               this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
               this.checkCell1 = new TextCheckCell(var1);
               this.checkCell1.setBackgroundDrawable(Theme.getSelectorDrawable(true));
               this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", 2131560358), this.saveCardInfo, false);
               this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
               this.checkCell1.setOnClickListener(new _$$Lambda$PaymentFormActivity$55th3Bl6h3UNIH_JJ7d61ZMtWHE(this));
               this.bottomCell[0] = new TextInfoPrivacyCell(var1);
               this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
               this.updateSavePaymentField();
               this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
            } else {
               var80 = this.paymentForm.native_params;
               if (var80 != null) {
                  label929: {
                     label910: {
                        try {
                           var72 = new JSONObject(var80.data);
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label910;
                        }

                        try {
                           this.need_card_country = var72.getBoolean("need_country");
                        } catch (Exception var22) {
                           try {
                              this.need_card_country = false;
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label910;
                           }
                        }

                        try {
                           this.need_card_postcode = var72.getBoolean("need_zip");
                        } catch (Exception var20) {
                           try {
                              this.need_card_postcode = false;
                           } catch (Exception var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label910;
                           }
                        }

                        try {
                           this.need_card_name = var72.getBoolean("need_cardholder_name");
                        } catch (Exception var18) {
                           try {
                              this.need_card_name = false;
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label910;
                           }
                        }

                        try {
                           this.stripeApiKey = var72.getString("publishable_key");
                           break label929;
                        } catch (Exception var16) {
                           try {
                              this.stripeApiKey = "";
                              break label929;
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                           }
                        }
                     }

                     var53 = var10000;
                     FileLog.e((Throwable)var53);
                  }
               }

               this.initAndroidPay(var1);
               this.inputFields = new EditTextBoldCursor[6];

               for(var2 = 0; var2 < 6; ++var2) {
                  if (var2 == 0) {
                     this.headerCell[0] = new HeaderCell(var1);
                     this.headerCell[0].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", 2131560361));
                     this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                  } else if (var2 == 4) {
                     this.headerCell[1] = new HeaderCell(var1);
                     this.headerCell[1].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.headerCell[1].setText(LocaleController.getString("PaymentBillingAddress", 2131560352));
                     this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                  }

                  boolean var64;
                  if (var2 == 3 || var2 == 5 || var2 == 4 && !this.need_card_postcode) {
                     var64 = false;
                  } else {
                     var64 = true;
                  }

                  var76 = new FrameLayout(var1);
                  this.linearLayout2.addView(var76, LayoutHelper.createLinear(-1, 50));
                  var76.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  this.inputFields[var2] = new EditTextBoldCursor(var1);
                  this.inputFields[var2].setTag(var2);
                  this.inputFields[var2].setTextSize(1, 16.0F);
                  this.inputFields[var2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                  this.inputFields[var2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.inputFields[var2].setBackgroundDrawable((Drawable)null);
                  this.inputFields[var2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.inputFields[var2].setCursorSize(AndroidUtilities.dp(20.0F));
                  this.inputFields[var2].setCursorWidth(1.5F);
                  if (var2 == 3) {
                     var74 = new LengthFilter(3);
                     this.inputFields[var2].setFilters(new InputFilter[]{var74});
                     this.inputFields[var2].setInputType(130);
                     this.inputFields[var2].setTypeface(Typeface.DEFAULT);
                     this.inputFields[var2].setTransformationMethod(PasswordTransformationMethod.getInstance());
                  } else if (var2 == 0) {
                     this.inputFields[var2].setInputType(3);
                  } else if (var2 == 4) {
                     this.inputFields[var2].setOnTouchListener(new _$$Lambda$PaymentFormActivity$Ueinsx_MGryTGh_0YI94FAwwnHM(this));
                     this.inputFields[var2].setInputType(0);
                  } else if (var2 == 1) {
                     this.inputFields[var2].setInputType(16386);
                  } else if (var2 == 2) {
                     this.inputFields[var2].setInputType(4097);
                  } else {
                     this.inputFields[var2].setInputType(16385);
                  }

                  this.inputFields[var2].setImeOptions(268435461);
                  if (var2 != 0) {
                     if (var2 != 1) {
                        if (var2 != 2) {
                           if (var2 != 3) {
                              if (var2 != 4) {
                                 if (var2 == 5) {
                                    this.inputFields[var2].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", 2131560403));
                                 }
                              } else {
                                 this.inputFields[var2].setHint(LocaleController.getString("PaymentShippingCountry", 2131560393));
                              }
                           } else {
                              this.inputFields[var2].setHint(LocaleController.getString("PaymentCardCvv", 2131560353));
                           }
                        } else {
                           this.inputFields[var2].setHint(LocaleController.getString("PaymentCardName", 2131560356));
                        }
                     } else {
                        this.inputFields[var2].setHint(LocaleController.getString("PaymentCardExpireDate", 2131560354));
                     }
                  } else {
                     this.inputFields[var2].setHint(LocaleController.getString("PaymentCardNumber", 2131560357));
                  }

                  if (var2 == 0) {
                     this.inputFields[var2].addTextChangedListener(new TextWatcher() {
                        public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
                        public static final int MAX_LENGTH_DINERS_CLUB = 14;
                        public static final int MAX_LENGTH_STANDARD = 16;
                        public final String[] PREFIXES_14 = new String[]{"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
                        public final String[] PREFIXES_15 = new String[]{"34", "37"};
                        public final String[] PREFIXES_16 = new String[]{"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35"};
                        private int actionPosition;
                        private int characterAction = -1;

                        public void afterTextChanged(Editable var1) {
                           if (!PaymentFormActivity.this.ignoreOnCardChange) {
                              EditTextBoldCursor var2 = PaymentFormActivity.this.inputFields[0];
                              int var3 = var2.getSelectionStart();
                              String var4 = var2.getText().toString();
                              int var5 = var3;
                              String var6 = var4;
                              if (this.characterAction == 3) {
                                 StringBuilder var16 = new StringBuilder();
                                 var16.append(var4.substring(0, this.actionPosition));
                                 var16.append(var4.substring(this.actionPosition + 1));
                                 var6 = var16.toString();
                                 var5 = var3 - 1;
                              }

                              StringBuilder var7 = new StringBuilder(var6.length());

                              int var8;
                              for(var3 = 0; var3 < var6.length(); var3 = var8) {
                                 var8 = var3 + 1;
                                 var4 = var6.substring(var3, var8);
                                 if ("0123456789".contains(var4)) {
                                    var7.append(var4);
                                 }
                              }

                              PaymentFormActivity.this.ignoreOnCardChange = true;
                              var6 = null;
                              byte var15 = 100;
                              int var10;
                              if (var7.length() > 0) {
                                 String var9 = var7.toString();
                                 var4 = null;
                                 var10 = 0;
                                 byte var17 = 100;

                                 String var11;
                                 byte var12;
                                 while(true) {
                                    var11 = var4;
                                    var12 = var17;
                                    if (var10 >= 3) {
                                       break;
                                    }

                                    String[] var13;
                                    if (var10 != 0) {
                                       if (var10 != 1) {
                                          var13 = this.PREFIXES_14;
                                          var15 = 14;
                                          var6 = "xxxx xxxx xxxx xx";
                                       } else {
                                          var13 = this.PREFIXES_15;
                                          var15 = 15;
                                          var6 = "xxxx xxxx xxxx xxx";
                                       }
                                    } else {
                                       var13 = this.PREFIXES_16;
                                       var15 = 16;
                                       var6 = "xxxx xxxx xxxx xxxx";
                                    }

                                    int var14 = 0;

                                    label116: {
                                       while(true) {
                                          var11 = var4;
                                          var12 = var17;
                                          if (var14 >= var13.length) {
                                             break label116;
                                          }

                                          var11 = var13[var14];
                                          if (var9.length() <= var11.length()) {
                                             if (var11.startsWith(var9)) {
                                                break;
                                             }
                                          } else if (var9.startsWith(var11)) {
                                             break;
                                          }

                                          ++var14;
                                       }

                                       var11 = var6;
                                       var12 = var15;
                                    }

                                    if (var11 != null) {
                                       break;
                                    }

                                    ++var10;
                                    var4 = var11;
                                    var17 = var12;
                                 }

                                 var6 = var11;
                                 var15 = var12;
                                 if (var12 != 0) {
                                    var6 = var11;
                                    var15 = var12;
                                    if (var7.length() > var12) {
                                       var7.setLength(var12);
                                       var15 = var12;
                                       var6 = var11;
                                    }
                                 }
                              }

                              var4 = "windowBackgroundWhiteBlackText";
                              if (var6 != null) {
                                 if (var15 != 0 && var7.length() == var15) {
                                    PaymentFormActivity.this.inputFields[1].requestFocus();
                                 }

                                 var2.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                                 var8 = 0;
                                 var3 = var5;

                                 while(true) {
                                    var5 = var3;
                                    if (var8 >= var7.length()) {
                                       break;
                                    }

                                    if (var8 >= var6.length()) {
                                       var7.insert(var8, ' ');
                                       var5 = var3;
                                       if (var3 == var8 + 1) {
                                          var8 = this.characterAction;
                                          var5 = var3;
                                          if (var8 != 2) {
                                             var5 = var3;
                                             if (var8 != 3) {
                                                var5 = var3 + 1;
                                             }
                                          }
                                       }
                                       break;
                                    }

                                    int var18 = var8;
                                    var5 = var3;
                                    if (var6.charAt(var8) == ' ') {
                                       var7.insert(var8, ' ');
                                       ++var8;
                                       var18 = var8;
                                       var5 = var3;
                                       if (var3 == var8) {
                                          var10 = this.characterAction;
                                          var18 = var8;
                                          var5 = var3;
                                          if (var10 != 2) {
                                             var18 = var8;
                                             var5 = var3;
                                             if (var10 != 3) {
                                                var5 = var3 + 1;
                                                var18 = var8;
                                             }
                                          }
                                       }
                                    }

                                    var8 = var18 + 1;
                                    var3 = var5;
                                 }
                              } else {
                                 var6 = var4;
                                 if (var7.length() > 0) {
                                    var6 = "windowBackgroundWhiteRedText4";
                                 }

                                 var2.setTextColor(Theme.getColor(var6));
                              }

                              if (!var7.toString().equals(var1.toString())) {
                                 var1.replace(0, var1.length(), var7);
                              }

                              if (var5 >= 0) {
                                 if (var5 > var2.length()) {
                                    var5 = var2.length();
                                 }

                                 var2.setSelection(var5);
                              }

                              PaymentFormActivity.this.ignoreOnCardChange = false;
                           }
                        }

                        public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                           if (var3 == 0 && var4 == 1) {
                              this.characterAction = 1;
                           } else if (var3 == 1 && var4 == 0) {
                              if (var1.charAt(var2) == ' ' && var2 > 0) {
                                 this.characterAction = 3;
                                 this.actionPosition = var2 - 1;
                              } else {
                                 this.characterAction = 2;
                              }
                           } else {
                              this.characterAction = -1;
                           }

                        }

                        public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                        }
                     });
                  } else if (var2 == 1) {
                     this.inputFields[var2].addTextChangedListener(new TextWatcher() {
                        private int actionPosition;
                        private int characterAction = -1;
                        private boolean isYear;

                        public void afterTextChanged(Editable var1) {
                           if (!PaymentFormActivity.this.ignoreOnCardChange) {
                              EditTextBoldCursor[] var13 = PaymentFormActivity.this.inputFields;
                              boolean var2 = true;
                              boolean var3 = true;
                              EditTextBoldCursor var4 = var13[1];
                              int var5 = var4.getSelectionStart();
                              String var6 = var4.getText().toString();
                              int var7 = this.characterAction;
                              byte var8 = 3;
                              int var9 = var5;
                              String var14 = var6;
                              if (var7 == 3) {
                                 StringBuilder var15 = new StringBuilder();
                                 var15.append(var6.substring(0, this.actionPosition));
                                 var15.append(var6.substring(this.actionPosition + 1));
                                 var14 = var15.toString();
                                 var9 = var5 - 1;
                              }

                              StringBuilder var20 = new StringBuilder(var14.length());

                              for(var5 = 0; var5 < var14.length(); var5 = var7) {
                                 var7 = var5 + 1;
                                 String var10 = var14.substring(var5, var7);
                                 if ("0123456789".contains(var10)) {
                                    var20.append(var10);
                                 }
                              }

                              PaymentFormActivity.this.ignoreOnCardChange = true;
                              PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                              if (var20.length() > 4) {
                                 var20.setLength(4);
                              }

                              if (var20.length() < 2) {
                                 this.isYear = false;
                              }

                              boolean var21;
                              byte var22;
                              label129: {
                                 if (this.isYear) {
                                    if (var20.length() > 2) {
                                       var22 = 2;
                                    } else {
                                       var22 = 1;
                                    }

                                    String[] var17 = new String[var22];
                                    var17[0] = var20.substring(0, 2);
                                    if (var17.length == 2) {
                                       var17[1] = var20.substring(2);
                                    }

                                    int var19;
                                    if (var20.length() == 4 && var17.length == 2) {
                                       label103: {
                                          int var11 = Utilities.parseInt(var17[0]);
                                          var7 = Utilities.parseInt(var17[1]) + 2000;
                                          Calendar var18 = Calendar.getInstance();
                                          var19 = var18.get(1);
                                          int var12 = var18.get(2);
                                          if (var7 >= var19) {
                                             var5 = var9;
                                             if (var7 != var19) {
                                                break label103;
                                             }

                                             var5 = var9;
                                             if (var11 >= var12 + 1) {
                                                break label103;
                                             }
                                          }

                                          PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                          var21 = var2;
                                       }
                                    } else {
                                       label131: {
                                          var19 = Utilities.parseInt(var17[0]);
                                          if (var19 <= 12) {
                                             var5 = var9;
                                             if (var19 != 0) {
                                                break label131;
                                             }
                                          }

                                          PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                          var21 = var2;
                                       }
                                    }
                                    break label129;
                                 } else if (var20.length() == 1) {
                                    int var16 = Utilities.parseInt(var20.toString());
                                    var5 = var9;
                                    if (var16 != 1) {
                                       var5 = var9;
                                       if (var16 != 0) {
                                          var20.insert(0, "0");
                                          var5 = var9 + 1;
                                       }
                                    }
                                 } else {
                                    var5 = var9;
                                    if (var20.length() == 2) {
                                       var5 = Utilities.parseInt(var20.toString());
                                       if (var5 <= 12 && var5 != 0) {
                                          var21 = false;
                                       } else {
                                          PaymentFormActivity.this.inputFields[1].setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                                          var21 = var3;
                                       }

                                       ++var9;
                                       break label129;
                                    }
                                 }

                                 var2 = false;
                                 var9 = var5;
                                 var21 = var2;
                              }

                              if (!var21 && var20.length() == 4) {
                                 var13 = PaymentFormActivity.this.inputFields;
                                 var22 = var8;
                                 if (PaymentFormActivity.this.need_card_name) {
                                    var22 = 2;
                                 }

                                 var13[var22].requestFocus();
                              }

                              label87: {
                                 if (var20.length() == 2) {
                                    var20.append('/');
                                 } else {
                                    var5 = var9;
                                    if (var20.length() <= 2) {
                                       break label87;
                                    }

                                    var5 = var9;
                                    if (var20.charAt(2) == '/') {
                                       break label87;
                                    }

                                    var20.insert(2, '/');
                                 }

                                 var5 = var9 + 1;
                              }

                              var4.setText(var20);
                              if (var5 >= 0) {
                                 if (var5 > var4.length()) {
                                    var5 = var4.length();
                                 }

                                 var4.setSelection(var5);
                              }

                              PaymentFormActivity.this.ignoreOnCardChange = false;
                           }
                        }

                        public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                           boolean var5 = false;
                           if (var3 == 0 && var4 == 1) {
                              if (TextUtils.indexOf(PaymentFormActivity.this.inputFields[1].getText(), '/') != -1) {
                                 var5 = true;
                              }

                              this.isYear = var5;
                              this.characterAction = 1;
                           } else if (var3 == 1 && var4 == 0) {
                              if (var1.charAt(var2) == '/' && var2 > 0) {
                                 this.isYear = false;
                                 this.characterAction = 3;
                                 this.actionPosition = var2 - 1;
                              } else {
                                 this.characterAction = 2;
                              }
                           } else {
                              this.characterAction = -1;
                           }

                        }

                        public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                        }
                     });
                  }

                  this.inputFields[var2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
                  var75 = this.inputFields[var2];
                  byte var70;
                  if (LocaleController.isRTL) {
                     var70 = 5;
                  } else {
                     var70 = 3;
                  }

                  var75.setGravity(var70);
                  var76.addView(this.inputFields[var2], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
                  this.inputFields[var2].setOnEditorActionListener(new _$$Lambda$PaymentFormActivity$kGOIockVRhCZimnBQEOXCbrCCls(this));
                  if (var2 == 3) {
                     this.sectionCell[0] = new ShadowSectionCell(var1);
                     this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
                  } else if (var2 == 5) {
                     this.sectionCell[2] = new ShadowSectionCell(var1);
                     this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                     this.checkCell1 = new TextCheckCell(var1);
                     this.checkCell1.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                     this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", 2131560358), this.saveCardInfo, false);
                     this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                     this.checkCell1.setOnClickListener(new _$$Lambda$PaymentFormActivity$7nFCu4nozpZFMiRn4XlVrELG_z4(this));
                     this.bottomCell[0] = new TextInfoPrivacyCell(var1);
                     this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
                     this.updateSavePaymentField();
                     this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                  } else if (var2 == 0) {
                     this.androidPayContainer = new FrameLayout(var1);
                     this.androidPayContainer.setId(4000);
                     this.androidPayContainer.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                     this.androidPayContainer.setVisibility(8);
                     var76.addView(this.androidPayContainer, LayoutHelper.createFrame(-2, -2.0F, 21, 0.0F, 0.0F, 4.0F, 0.0F));
                  }

                  if (var64) {
                     var83 = new View(var1) {
                        protected void onDraw(Canvas var1) {
                           float var2;
                           if (LocaleController.isRTL) {
                              var2 = 0.0F;
                           } else {
                              var2 = (float)AndroidUtilities.dp(20.0F);
                           }

                           float var3 = (float)(this.getMeasuredHeight() - 1);
                           int var4 = this.getMeasuredWidth();
                           int var5;
                           if (LocaleController.isRTL) {
                              var5 = AndroidUtilities.dp(20.0F);
                           } else {
                              var5 = 0;
                           }

                           var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                        }
                     };
                     var83.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.dividers.add(var83);
                     var76.addView(var83, new LayoutParams(-1, 1, 83));
                  }

                  if (var2 == 4 && !this.need_card_country || var2 == 5 && !this.need_card_postcode || var2 == 2 && !this.need_card_name) {
                     var76.setVisibility(8);
                  }
               }

               if (!this.need_card_country && !this.need_card_postcode) {
                  this.headerCell[1].setVisibility(8);
                  this.sectionCell[0].setVisibility(8);
               }

               if (this.need_card_postcode) {
                  this.inputFields[5].setImeOptions(268435462);
               } else {
                  this.inputFields[3].setImeOptions(268435462);
               }
            }
         } else {
            TLRPC.TL_shippingOption var84;
            if (var2 == 1) {
               var8 = this.requestedInfo.shipping_options.size();
               this.radioCells = new RadioCell[var8];

               for(var2 = 0; var2 < var8; ++var2) {
                  var84 = (TLRPC.TL_shippingOption)this.requestedInfo.shipping_options.get(var2);
                  this.radioCells[var2] = new RadioCell(var1);
                  this.radioCells[var2].setTag(var2);
                  this.radioCells[var2].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                  RadioCell var78 = this.radioCells[var2];
                  var54 = String.format("%s - %s", this.getTotalPriceString(var84.prices), var84.title);
                  boolean var11;
                  if (var2 == 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  boolean var12;
                  if (var2 != var8 - 1) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  var78.setText(var54, var11, var12);
                  this.radioCells[var2].setOnClickListener(new _$$Lambda$PaymentFormActivity$vEFqYoe1G6TRzc_6QuSxLqi0_ls(this));
                  this.linearLayout2.addView(this.radioCells[var2]);
               }

               this.bottomCell[0] = new TextInfoPrivacyCell(var1);
               this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
               this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
            } else if (var2 == 3) {
               this.inputFields = new EditTextBoldCursor[2];

               for(var8 = 0; var8 < 2; ++var8) {
                  if (var8 == 0) {
                     this.headerCell[0] = new HeaderCell(var1);
                     this.headerCell[0].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", 2131560361));
                     this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                  }

                  var76 = new FrameLayout(var1);
                  this.linearLayout2.addView(var76, LayoutHelper.createLinear(-1, 50));
                  var76.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  if (var8 != 1) {
                     var39 = true;
                  } else {
                     var39 = false;
                  }

                  var9 = var39;
                  if (var39) {
                     label952: {
                        if (var8 != 7 || this.paymentForm.invoice.phone_requested) {
                           var9 = var39;
                           if (var8 != 6) {
                              break label952;
                           }

                           var79 = this.paymentForm.invoice;
                           var9 = var39;
                           if (var79.phone_requested) {
                              break label952;
                           }

                           var9 = var39;
                           if (var79.email_requested) {
                              break label952;
                           }
                        }

                        var9 = false;
                     }
                  }

                  if (var9) {
                     var83 = new View(var1) {
                        protected void onDraw(Canvas var1) {
                           float var2;
                           if (LocaleController.isRTL) {
                              var2 = 0.0F;
                           } else {
                              var2 = (float)AndroidUtilities.dp(20.0F);
                           }

                           float var3 = (float)(this.getMeasuredHeight() - 1);
                           int var4 = this.getMeasuredWidth();
                           int var5;
                           if (LocaleController.isRTL) {
                              var5 = AndroidUtilities.dp(20.0F);
                           } else {
                              var5 = 0;
                           }

                           var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                        }
                     };
                     var83.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.dividers.add(var83);
                     var76.addView(var83, new LayoutParams(-1, 1, 83));
                  }

                  this.inputFields[var8] = new EditTextBoldCursor(var1);
                  this.inputFields[var8].setTag(var8);
                  this.inputFields[var8].setTextSize(1, 16.0F);
                  this.inputFields[var8].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                  this.inputFields[var8].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.inputFields[var8].setBackgroundDrawable((Drawable)null);
                  this.inputFields[var8].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.inputFields[var8].setCursorSize(AndroidUtilities.dp(20.0F));
                  this.inputFields[var8].setCursorWidth(1.5F);
                  if (var8 == 0) {
                     this.inputFields[var8].setOnTouchListener(_$$Lambda$PaymentFormActivity$NaAxpZmEom2J3d4n5KPbjk_hflw.INSTANCE);
                     this.inputFields[var8].setInputType(0);
                  } else {
                     this.inputFields[var8].setInputType(129);
                     this.inputFields[var8].setTypeface(Typeface.DEFAULT);
                  }

                  this.inputFields[var8].setImeOptions(268435462);
                  if (var8 != 0) {
                     if (var8 == 1) {
                        this.inputFields[var8].setHint(LocaleController.getString("LoginPassword", 2131559788));
                        this.inputFields[var8].requestFocus();
                     }
                  } else {
                     this.inputFields[var8].setText(this.paymentForm.saved_credentials.title);
                  }

                  this.inputFields[var8].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
                  var75 = this.inputFields[var8];
                  if (LocaleController.isRTL) {
                     var46 = 5;
                  } else {
                     var46 = 3;
                  }

                  var75.setGravity(var46);
                  var76.addView(this.inputFields[var8], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
                  this.inputFields[var8].setOnEditorActionListener(new _$$Lambda$PaymentFormActivity$7nDt_ueaNatOk32R3AVkL3FoSzw(this));
                  if (var8 == 1) {
                     this.bottomCell[0] = new TextInfoPrivacyCell(var1);
                     this.bottomCell[0].setText(LocaleController.formatString("PaymentConfirmationMessage", 2131560370, this.paymentForm.saved_credentials.title));
                     this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
                     this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                     this.settingsCell[0] = new TextSettingsCell(var1);
                     this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                     this.settingsCell[0].setText(LocaleController.getString("PaymentConfirmationNewCard", 2131560371), false);
                     this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                     this.settingsCell[0].setOnClickListener(new _$$Lambda$PaymentFormActivity$hOneDfBvow_ay5NfpKknN53qGXA(this));
                     this.bottomCell[1] = new TextInfoPrivacyCell(var1);
                     this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
                     this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                  }
               }
            } else if (var2 != 4 && var2 != 5) {
               if (var2 == 6) {
                  this.codeFieldCell = new EditTextSettingsCell(var1);
                  this.codeFieldCell.setTextAndHint("", LocaleController.getString("PasswordCode", 2131560345), false);
                  this.codeFieldCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  EditTextBoldCursor var85 = this.codeFieldCell.getTextView();
                  var85.setInputType(3);
                  var85.setImeOptions(6);
                  var85.setOnEditorActionListener(new _$$Lambda$PaymentFormActivity$zabhB89XpHUfevzEww0brDV8Dzs(this));
                  var85.addTextChangedListener(new TextWatcher() {
                     public void afterTextChanged(Editable var1) {
                        if (PaymentFormActivity.this.emailCodeLength != 0 && var1.length() == PaymentFormActivity.this.emailCodeLength) {
                           PaymentFormActivity.this.sendSavePassword(false);
                        }

                     }

                     public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }

                     public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }
                  });
                  this.linearLayout2.addView(this.codeFieldCell, LayoutHelper.createLinear(-1, -2));
                  this.bottomCell[2] = new TextInfoPrivacyCell(var1);
                  this.bottomCell[2].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
                  this.linearLayout2.addView(this.bottomCell[2], LayoutHelper.createLinear(-1, -2));
                  this.settingsCell[1] = new TextSettingsCell(var1);
                  this.settingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                  this.settingsCell[1].setTag("windowBackgroundWhiteBlackText");
                  this.settingsCell[1].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.settingsCell[1].setText(LocaleController.getString("ResendCode", 2131560581), true);
                  this.linearLayout2.addView(this.settingsCell[1], LayoutHelper.createLinear(-1, -2));
                  this.settingsCell[1].setOnClickListener(new _$$Lambda$PaymentFormActivity$I7CPk7usQoOQw119fB6a_FgtbsU(this));
                  this.settingsCell[0] = new TextSettingsCell(var1);
                  this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                  this.settingsCell[0].setTag("windowBackgroundWhiteRedText3");
                  this.settingsCell[0].setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                  this.settingsCell[0].setText(LocaleController.getString("AbortPassword", 2131558402), false);
                  this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                  this.settingsCell[0].setOnClickListener(new _$$Lambda$PaymentFormActivity$u3SvDLQ9ZpBSRMUM2jg2lBOMsbk(this));
                  this.inputFields = new EditTextBoldCursor[3];

                  for(var2 = 0; var2 < 3; ++var2) {
                     if (var2 == 0) {
                        this.headerCell[0] = new HeaderCell(var1);
                        this.headerCell[0].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[0].setText(LocaleController.getString("PaymentPasswordTitle", 2131560384));
                        this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                     } else if (var2 == 2) {
                        this.headerCell[1] = new HeaderCell(var1);
                        this.headerCell[1].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.headerCell[1].setText(LocaleController.getString("PaymentPasswordEmailTitle", 2131560380));
                        this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                     }

                     var76 = new FrameLayout(var1);
                     this.linearLayout2.addView(var76, LayoutHelper.createLinear(-1, 50));
                     var76.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     if (var2 == 0) {
                        var83 = new View(var1) {
                           protected void onDraw(Canvas var1) {
                              float var2;
                              if (LocaleController.isRTL) {
                                 var2 = 0.0F;
                              } else {
                                 var2 = (float)AndroidUtilities.dp(20.0F);
                              }

                              float var3 = (float)(this.getMeasuredHeight() - 1);
                              int var4 = this.getMeasuredWidth();
                              int var5;
                              if (LocaleController.isRTL) {
                                 var5 = AndroidUtilities.dp(20.0F);
                              } else {
                                 var5 = 0;
                              }

                              var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                           }
                        };
                        var83.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        this.dividers.add(var83);
                        var76.addView(var83, new LayoutParams(-1, 1, 83));
                     }

                     this.inputFields[var2] = new EditTextBoldCursor(var1);
                     this.inputFields[var2].setTag(var2);
                     this.inputFields[var2].setTextSize(1, 16.0F);
                     this.inputFields[var2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                     this.inputFields[var2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                     this.inputFields[var2].setBackgroundDrawable((Drawable)null);
                     this.inputFields[var2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                     this.inputFields[var2].setCursorSize(AndroidUtilities.dp(20.0F));
                     this.inputFields[var2].setCursorWidth(1.5F);
                     if (var2 != 0 && var2 != 1) {
                        this.inputFields[var2].setInputType(33);
                        this.inputFields[var2].setImeOptions(268435462);
                     } else {
                        this.inputFields[var2].setInputType(129);
                        this.inputFields[var2].setTypeface(Typeface.DEFAULT);
                        this.inputFields[var2].setImeOptions(268435461);
                     }

                     if (var2 != 0) {
                        if (var2 != 1) {
                           if (var2 == 2) {
                              this.inputFields[var2].setHint(LocaleController.getString("PaymentPasswordEmail", 2131560378));
                           }
                        } else {
                           this.inputFields[var2].setHint(LocaleController.getString("PaymentPasswordReEnter", 2131560383));
                        }
                     } else {
                        this.inputFields[var2].setHint(LocaleController.getString("PaymentPasswordEnter", 2131560381));
                        this.inputFields[var2].requestFocus();
                     }

                     this.inputFields[var2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
                     var75 = this.inputFields[var2];
                     byte var68;
                     if (LocaleController.isRTL) {
                        var68 = 5;
                     } else {
                        var68 = 3;
                     }

                     var75.setGravity(var68);
                     var76.addView(this.inputFields[var2], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
                     this.inputFields[var2].setOnEditorActionListener(new _$$Lambda$PaymentFormActivity$wikSuuvfxhb2U7Hw9X3oaWcgGS8(this));
                     if (var2 == 1) {
                        this.bottomCell[0] = new TextInfoPrivacyCell(var1);
                        this.bottomCell[0].setText(LocaleController.getString("PaymentPasswordInfo", 2131560382));
                        this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
                        this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                     } else if (var2 == 2) {
                        this.bottomCell[1] = new TextInfoPrivacyCell(var1);
                        this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", 2131560379));
                        this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
                        this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                     }
                  }

                  this.updatePasswordFields();
               }
            } else {
               this.paymentInfoCell = new PaymentInfoCell(var1);
               this.paymentInfoCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               this.paymentInfoCell.setInvoice((TLRPC.TL_messageMediaInvoice)this.messageObject.messageOwner.media, this.currentBotName);
               this.linearLayout2.addView(this.paymentInfoCell, LayoutHelper.createLinear(-1, -2));
               this.sectionCell[0] = new ShadowSectionCell(var1);
               this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
               ArrayList var81 = new ArrayList(this.paymentForm.invoice.prices);
               var84 = this.shippingOption;
               if (var84 != null) {
                  var81.addAll(var84.prices);
               }

               String var62 = this.getTotalPriceString(var81);

               for(var2 = 0; var2 < var81.size(); ++var2) {
                  TLRPC.TL_labeledPrice var67 = (TLRPC.TL_labeledPrice)var81.get(var2);
                  TextPriceCell var86 = new TextPriceCell(var1);
                  var86.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  var86.setTextAndValue(var67.label, LocaleController.getInstance().formatCurrencyString(var67.amount, this.paymentForm.invoice.currency), false);
                  this.linearLayout2.addView(var86);
               }

               TextPriceCell var82 = new TextPriceCell(var1);
               var82.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               var82.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", 2131560409), var62, true);
               this.linearLayout2.addView(var82);
               var45 = new View(var1) {
                  protected void onDraw(Canvas var1) {
                     float var2;
                     if (LocaleController.isRTL) {
                        var2 = 0.0F;
                     } else {
                        var2 = (float)AndroidUtilities.dp(20.0F);
                     }

                     float var3 = (float)(this.getMeasuredHeight() - 1);
                     int var4 = this.getMeasuredWidth();
                     int var5;
                     if (LocaleController.isRTL) {
                        var5 = AndroidUtilities.dp(20.0F);
                     } else {
                        var5 = 0;
                     }

                     var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
                  }
               };
               var45.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               this.dividers.add(var45);
               this.linearLayout2.addView(var45, new LayoutParams(-1, 1, 83));
               this.detailSettingsCell[0] = new TextDetailSettingsCell(var1);
               this.detailSettingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
               this.detailSettingsCell[0].setTextAndValue(this.cardName, LocaleController.getString("PaymentCheckoutMethod", 2131560364), true);
               this.linearLayout2.addView(this.detailSettingsCell[0]);
               if (this.currentStep == 4) {
                  this.detailSettingsCell[0].setOnClickListener(new _$$Lambda$PaymentFormActivity$__v7hZyhHE9vxsissEj7icTtuGM(this));
               }

               var2 = 0;

               for(var65 = null; var2 < this.paymentForm.users.size(); ++var2) {
                  var77 = (TLRPC.User)this.paymentForm.users.get(var2);
                  if (var77.id == this.paymentForm.provider_id) {
                     var65 = var77;
                  }
               }

               if (var65 != null) {
                  this.detailSettingsCell[1] = new TextDetailSettingsCell(var1);
                  this.detailSettingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                  TextDetailSettingsCell var87 = this.detailSettingsCell[1];
                  var66 = ContactsController.formatName(var65.first_name, var65.last_name);
                  var87.setTextAndValue(var66, LocaleController.getString("PaymentCheckoutProvider", 2131560368), true);
                  this.linearLayout2.addView(this.detailSettingsCell[1]);
               } else {
                  var66 = "";
               }

               TLRPC.TL_payments_validateRequestedInfo var88 = this.validateRequest;
               if (var88 != null) {
                  var63 = var88.info.shipping_address;
                  if (var63 != null) {
                     var54 = String.format("%s %s, %s, %s, %s, %s", var63.street_line1, var63.street_line2, var63.city, var63.state, var63.country_iso2, var63.post_code);
                     this.detailSettingsCell[2] = new TextDetailSettingsCell(var1);
                     this.detailSettingsCell[2].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.detailSettingsCell[2].setTextAndValue(var54, LocaleController.getString("PaymentShippingAddress", 2131560389), true);
                     this.linearLayout2.addView(this.detailSettingsCell[2]);
                  }

                  if (this.validateRequest.info.name != null) {
                     this.detailSettingsCell[3] = new TextDetailSettingsCell(var1);
                     this.detailSettingsCell[3].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.detailSettingsCell[3].setTextAndValue(this.validateRequest.info.name, LocaleController.getString("PaymentCheckoutName", 2131560365), true);
                     this.linearLayout2.addView(this.detailSettingsCell[3]);
                  }

                  if (this.validateRequest.info.phone != null) {
                     this.detailSettingsCell[4] = new TextDetailSettingsCell(var1);
                     this.detailSettingsCell[4].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.detailSettingsCell[4].setTextAndValue(PhoneFormat.getInstance().format(this.validateRequest.info.phone), LocaleController.getString("PaymentCheckoutPhoneNumber", 2131560367), true);
                     this.linearLayout2.addView(this.detailSettingsCell[4]);
                  }

                  if (this.validateRequest.info.email != null) {
                     this.detailSettingsCell[5] = new TextDetailSettingsCell(var1);
                     this.detailSettingsCell[5].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.detailSettingsCell[5].setTextAndValue(this.validateRequest.info.email, LocaleController.getString("PaymentCheckoutEmail", 2131560363), true);
                     this.linearLayout2.addView(this.detailSettingsCell[5]);
                  }

                  if (this.shippingOption != null) {
                     this.detailSettingsCell[6] = new TextDetailSettingsCell(var1);
                     this.detailSettingsCell[6].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     this.detailSettingsCell[6].setTextAndValue(this.shippingOption.title, LocaleController.getString("PaymentCheckoutShippingMethod", 2131560369), false);
                     this.linearLayout2.addView(this.detailSettingsCell[6]);
                  }
               }

               if (this.currentStep == 4) {
                  this.bottomLayout = new FrameLayout(var1);
                  this.bottomLayout.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                  var5.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
                  this.bottomLayout.setOnClickListener(new _$$Lambda$PaymentFormActivity$UCCxWFA5zv52WCmPKPbwcSfjXPs(this, var66, var62));
                  this.payTextView = new TextView(var1);
                  this.payTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText6"));
                  this.payTextView.setText(LocaleController.formatString("PaymentCheckoutPay", 2131560366, var62));
                  this.payTextView.setTextSize(1, 14.0F);
                  this.payTextView.setGravity(17);
                  this.payTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  this.bottomLayout.addView(this.payTextView, LayoutHelper.createFrame(-1, -1.0F));
                  this.progressViewButton = new ContextProgressView(var1, 0);
                  this.progressViewButton.setVisibility(4);
                  this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0F));
                  var45 = new View(var1);
                  var45.setBackgroundResource(2131165408);
                  var5.addView(var45, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
                  this.doneItem.setEnabled(false);
                  this.doneItem.getImageView().setVisibility(4);
                  this.webView = new WebView(var1) {
                     public boolean onTouchEvent(MotionEvent var1) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(var1);
                     }
                  };
                  this.webView.setBackgroundColor(-1);
                  this.webView.getSettings().setJavaScriptEnabled(true);
                  this.webView.getSettings().setDomStorageEnabled(true);
                  if (VERSION.SDK_INT >= 21) {
                     this.webView.getSettings().setMixedContentMode(0);
                     CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                  }

                  this.webView.setWebViewClient(new WebViewClient() {
                     public void onLoadResource(WebView var1, String var2) {
                        try {
                           if ("t.me".equals(Uri.parse(var2).getHost())) {
                              PaymentFormActivity.this.goToNextStep();
                              return;
                           }
                        } catch (Exception var4) {
                        }

                        super.onLoadResource(var1, var2);
                     }

                     public void onPageFinished(WebView var1, String var2) {
                        super.onPageFinished(var1, var2);
                        PaymentFormActivity.this.webviewLoading = false;
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.updateSavePaymentField();
                     }

                     public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
                        try {
                           if ("t.me".equals(Uri.parse(var2).getHost())) {
                              PaymentFormActivity.this.goToNextStep();
                              return true;
                           }
                        } catch (Exception var3) {
                        }

                        return false;
                     }
                  });
                  var5.addView(this.webView, LayoutHelper.createFrame(-1, -1.0F));
                  this.webView.setVisibility(8);
               }

               this.sectionCell[1] = new ShadowSectionCell(var1);
               this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
               this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
            }
         }
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      TLRPC.TL_payments_paymentForm var4;
      if (var1 == NotificationCenter.didSetTwoStepPassword) {
         var4 = this.paymentForm;
         var4.password_missing = false;
         var4.can_save_credentials = true;
         this.updateSavePaymentField();
      } else if (var1 == NotificationCenter.didRemoveTwoStepPassword) {
         var4 = this.paymentForm;
         var4.password_missing = true;
         var4.can_save_credentials = false;
         this.updateSavePaymentField();
      } else if (var1 == NotificationCenter.paymentFinished) {
         this.removeSelfFromStack();
      }

   }

   @SuppressLint({"HardwareIds"})
   public void fillNumber(String var1) {
      Exception var10000;
      label118: {
         TelephonyManager var2;
         boolean var10001;
         try {
            var2 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label118;
         }

         if (var1 == null) {
            try {
               if (var2.getSimState() == 1 || var2.getPhoneType() == 0) {
                  return;
               }
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label118;
            }
         }

         boolean var3;
         label103: {
            label102: {
               try {
                  if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
                     break label102;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label118;
               }

               var3 = true;
               break label103;
            }

            var3 = false;
         }

         if (var1 == null && !var3) {
            return;
         }

         String var4 = var1;
         if (var1 == null) {
            try {
               var4 = PhoneFormat.stripExceptNumbers(var2.getLine1Number());
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label118;
            }
         }

         var1 = null;

         int var5;
         try {
            if (TextUtils.isEmpty(var4)) {
               return;
            }

            var5 = var4.length();
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label118;
         }

         int var16 = 4;
         if (var5 > 4) {
            while(true) {
               if (var16 < 1) {
                  var1 = null;
                  var3 = false;
                  break;
               }

               label78: {
                  try {
                     String var14 = var4.substring(0, var16);
                     if ((String)this.codesMap.get(var14) != null) {
                        var1 = var4.substring(var16);
                        this.inputFields[8].setText(var14);
                        break label78;
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label118;
                  }

                  --var16;
                  continue;
               }

               var3 = true;
               break;
            }

            if (!var3) {
               try {
                  var1 = var4.substring(1);
                  this.inputFields[8].setText(var4.substring(0, 1));
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label118;
               }
            }
         }

         if (var1 == null) {
            return;
         }

         try {
            this.inputFields[9].setText(var1);
            this.inputFields[9].setSelection(this.inputFields[9].length());
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var15 = var10000;
      FileLog.e((Throwable)var15);
   }

   public ThemeDescription[] getThemeDescriptions() {
      ArrayList var1 = new ArrayList();
      var1.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder"));
      LinearLayout var2 = this.linearLayout2;
      Paint var3 = Theme.dividerPaint;
      var1.add(new ThemeDescription(var2, 0, new Class[]{View.class}, var3, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      var1.add(new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"));
      var1.add(new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"));
      var1.add(new ThemeDescription(this.progressViewButton, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"));
      var1.add(new ThemeDescription(this.progressViewButton, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"));
      int var4;
      if (this.inputFields != null) {
         var4 = 0;

         while(true) {
            EditTextBoldCursor[] var5 = this.inputFields;
            if (var4 >= var5.length) {
               break;
            }

            var1.add(new ThemeDescription((View)var5[var4].getParent(), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            ++var4;
         }
      } else {
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
      }

      if (this.radioCells != null) {
         var4 = 0;

         while(true) {
            RadioCell[] var6 = this.radioCells;
            if (var4 >= var6.length) {
               break;
            }

            var1.add(new ThemeDescription(var6[var4], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var1.add(new ThemeDescription(this.radioCells[var4], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
            var1.add(new ThemeDescription(this.radioCells[var4], 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var1.add(new ThemeDescription(this.radioCells[var4], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"));
            var1.add(new ThemeDescription(this.radioCells[var4], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"));
            ++var4;
         }
      } else {
         var1.add(new ThemeDescription((View)null, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"));
      }

      var4 = 0;

      while(true) {
         HeaderCell[] var7 = this.headerCell;
         if (var4 >= var7.length) {
            var4 = 0;

            while(true) {
               ShadowSectionCell[] var8 = this.sectionCell;
               if (var4 >= var8.length) {
                  var4 = 0;

                  while(true) {
                     TextInfoPrivacyCell[] var9 = this.bottomCell;
                     if (var4 >= var9.length) {
                        for(var4 = 0; var4 < this.dividers.size(); ++var4) {
                           var1.add(new ThemeDescription((View)this.dividers.get(var4), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                        }

                        var1.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                        var1.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                        var1.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
                        var1.add(new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                        var1.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                        var1.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"));
                        var1.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"));
                        var1.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                        var1.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
                        var4 = 0;

                        while(true) {
                           TextSettingsCell[] var10 = this.settingsCell;
                           if (var4 >= var10.length) {
                              var1.add(new ThemeDescription(this.payTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText6"));
                              var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextPriceCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                              var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                              var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                              var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
                              var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
                              var1.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                              var1.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
                              var4 = 1;

                              while(true) {
                                 TextDetailSettingsCell[] var11 = this.detailSettingsCell;
                                 if (var4 >= var11.length) {
                                    var1.add(new ThemeDescription(this.paymentInfoCell, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                                    var1.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                                    var1.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                                    var1.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailExTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
                                    var1.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                                    var1.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
                                    return (ThemeDescription[])var1.toArray(new ThemeDescription[0]);
                                 }

                                 var1.add(new ThemeDescription(var11[var4], ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                                 var1.add(new ThemeDescription(this.detailSettingsCell[var4], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                                 var1.add(new ThemeDescription(this.detailSettingsCell[var4], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
                                 ++var4;
                              }
                           }

                           var1.add(new ThemeDescription(var10[var4], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
                           var1.add(new ThemeDescription(this.settingsCell[var4], ThemeDescription.FLAG_SELECTORWHITE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
                           var1.add(new ThemeDescription(this.settingsCell[var4], 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                           ++var4;
                        }
                     }

                     var1.add(new ThemeDescription(var9[var4], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
                     var1.add(new ThemeDescription(this.bottomCell[var4], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"));
                     var1.add(new ThemeDescription(this.bottomCell[var4], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"));
                     ++var4;
                  }
               }

               var1.add(new ThemeDescription(var8[var4], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
               ++var4;
            }
         }

         var1.add(new ThemeDescription(var7[var4], ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
         var1.add(new ThemeDescription(this.headerCell[var4], 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
         ++var4;
      }
   }

   // $FF: synthetic method
   public void lambda$checkPassword$45$PaymentFormActivity(String var1, TLRPC.TL_account_getPassword var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$TcVVqZdUtRanjkIhF2Xby_5O01Q(this, var4, var3, var1, var2));
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$PaymentFormActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            CountrySelectActivity var3 = new CountrySelectActivity(false);
            var3.setCountrySelectActivityDelegate(new _$$Lambda$PaymentFormActivity$YL0G4SiCRRdBSsARre0gVqa3im4(this));
            this.presentFragment(var3);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$11$PaymentFormActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.doneItem.performClick();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$12$PaymentFormActivity(View var1) {
      this.passwordOk = false;
      this.goToNextStep();
   }

   // $FF: synthetic method
   public void lambda$createView$13$PaymentFormActivity(View var1) {
      PaymentFormActivity var2 = new PaymentFormActivity(this.paymentForm, this.messageObject, 2, this.requestedInfo, this.shippingOption, (String)null, this.cardName, this.validateRequest, this.saveCardInfo, (TLRPC.TL_inputPaymentCredentialsAndroidPay)null);
      var2.setDelegate(new PaymentFormActivity$12(this));
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public void lambda$createView$15$PaymentFormActivity(String var1, String var2, View var3) {
      TLRPC.User var5 = this.botUser;
      if (var5 != null && !var5.verified) {
         StringBuilder var6 = new StringBuilder();
         var6.append("payment_warning_");
         var6.append(this.botUser.id);
         String var4 = var6.toString();
         SharedPreferences var7 = MessagesController.getNotificationsSettings(super.currentAccount);
         if (!var7.getBoolean(var4, false)) {
            var7.edit().putBoolean(var4, true).commit();
            AlertDialog.Builder var8 = new AlertDialog.Builder(this.getParentActivity());
            var8.setTitle(LocaleController.getString("PaymentWarning", 2131560410));
            var8.setMessage(LocaleController.formatString("PaymentWarningText", 2131560411, this.currentBotName, var1));
            var8.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PaymentFormActivity$MREN73dZdmfuVvIGQi8sgTjprGE(this, var2));
            this.showDialog(var8.create());
         } else {
            this.showPayAlert(var2);
         }
      } else {
         this.showPayAlert(var2);
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$16$PaymentFormActivity(TextView var1, int var2, KeyEvent var3) {
      boolean var4 = false;
      if (var2 == 6) {
         this.sendSavePassword(false);
         var4 = true;
      }

      return var4;
   }

   // $FF: synthetic method
   public void lambda$createView$18$PaymentFormActivity(View var1) {
      TLRPC.TL_account_resendPasswordEmail var2 = new TLRPC.TL_account_resendPasswordEmail();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, _$$Lambda$PaymentFormActivity$kpdlJs1QwZ9_5EeFfvuu3eWN4uE.INSTANCE);
      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
      var3.setMessage(LocaleController.getString("ResendCodeInfo", 2131560582));
      var3.setTitle(LocaleController.getString("AppName", 2131558635));
      var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
      this.showDialog(var3.create());
   }

   // $FF: synthetic method
   public boolean lambda$createView$2$PaymentFormActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 5) {
         if (var2 == 6) {
            this.doneItem.performClick();
            return true;
         } else {
            return false;
         }
      } else {
         var2 = (Integer)var1.getTag();

         while(true) {
            int var4 = var2 + 1;
            EditTextBoldCursor[] var5 = this.inputFields;
            if (var4 >= var5.length) {
               break;
            }

            var2 = var4;
            if (var4 != 4) {
               var2 = var4;
               if (((View)var5[var4].getParent()).getVisibility() == 0) {
                  this.inputFields[var4].requestFocus();
                  break;
               }
            }
         }

         return true;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$20$PaymentFormActivity(View var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      String var3 = LocaleController.getString("TurnPasswordOffQuestion", 2131560918);
      String var4 = var3;
      if (this.currentPassword.has_secure_values) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var3);
         var5.append("\n\n");
         var5.append(LocaleController.getString("TurnPasswordOffPassport", 2131560917));
         var4 = var5.toString();
      }

      var2.setMessage(var4);
      var2.setTitle(LocaleController.getString("AppName", 2131558635));
      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PaymentFormActivity$UsXOIe_Tc2OBHUXrtn6gfLKTiRQ(this));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      this.showDialog(var2.create());
   }

   // $FF: synthetic method
   public boolean lambda$createView$21$PaymentFormActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.doneItem.performClick();
         return true;
      } else {
         if (var2 == 5) {
            var2 = (Integer)var1.getTag();
            if (var2 == 0) {
               this.inputFields[1].requestFocus();
            } else if (var2 == 1) {
               this.inputFields[2].requestFocus();
            }
         }

         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$3$PaymentFormActivity(View var1) {
      this.saveShippingInfo ^= true;
      this.checkCell1.setChecked(this.saveShippingInfo);
   }

   // $FF: synthetic method
   public void lambda$createView$4$PaymentFormActivity(View var1) {
      this.saveCardInfo ^= true;
      this.checkCell1.setChecked(this.saveCardInfo);
   }

   // $FF: synthetic method
   public boolean lambda$createView$6$PaymentFormActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            CountrySelectActivity var3 = new CountrySelectActivity(false);
            var3.setCountrySelectActivityDelegate(new _$$Lambda$PaymentFormActivity$c3rgfJXwF9E12z1XSDEQQZO6iNM(this));
            this.presentFragment(var3);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$7$PaymentFormActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 5) {
         if (var2 == 6) {
            this.doneItem.performClick();
            return true;
         } else {
            return false;
         }
      } else {
         int var4 = (Integer)var1.getTag();

         while(true) {
            ++var4;
            if (var4 >= this.inputFields.length) {
               break;
            }

            var2 = var4;
            if (var4 == 4) {
               var2 = var4 + 1;
            }

            var4 = var2;
            if (((View)this.inputFields[var2].getParent()).getVisibility() == 0) {
               this.inputFields[var2].requestFocus();
               break;
            }
         }

         return true;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$8$PaymentFormActivity(View var1) {
      this.saveCardInfo ^= true;
      this.checkCell1.setChecked(this.saveCardInfo);
   }

   // $FF: synthetic method
   public void lambda$createView$9$PaymentFormActivity(View var1) {
      int var2 = (Integer)var1.getTag();
      int var3 = 0;

      while(true) {
         RadioCell[] var5 = this.radioCells;
         if (var3 >= var5.length) {
            return;
         }

         RadioCell var6 = var5[var3];
         boolean var4;
         if (var2 == var3) {
            var4 = true;
         } else {
            var4 = false;
         }

         var6.setChecked(var4, true);
         ++var3;
      }
   }

   // $FF: synthetic method
   public void lambda$loadPasswordInfo$24$PaymentFormActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x_UeiB3WlNSgQ(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$0$PaymentFormActivity(String var1, String var2) {
      this.inputFields[4].setText(var1);
      this.countryName = var2;
   }

   // $FF: synthetic method
   public void lambda$null$14$PaymentFormActivity(String var1, DialogInterface var2, int var3) {
      this.showPayAlert(var1);
   }

   // $FF: synthetic method
   public void lambda$null$19$PaymentFormActivity(DialogInterface var1, int var2) {
      this.sendSavePassword(true);
   }

   // $FF: synthetic method
   public void lambda$null$22$PaymentFormActivity() {
      if (this.shortPollRunnable != null) {
         this.loadPasswordInfo();
         this.shortPollRunnable = null;
      }
   }

   // $FF: synthetic method
   public void lambda$null$23$PaymentFormActivity(TLRPC.TL_error var1, TLObject var2) {
      this.loadingPasswordInfo = false;
      if (var1 == null) {
         this.currentPassword = (TLRPC.TL_account_password)var2;
         if (!TwoStepVerificationActivity.canHandleCurrentPassword(this.currentPassword, false)) {
            AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
            return;
         }

         TLRPC.TL_payments_paymentForm var3 = this.paymentForm;
         if (var3 != null && this.currentPassword.has_password) {
            var3.password_missing = false;
            var3.can_save_credentials = true;
            this.updateSavePaymentField();
         }

         TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
         PaymentFormActivity var4 = this.passwordFragment;
         if (var4 != null) {
            var4.setCurrentPassword(this.currentPassword);
         }

         if (!this.currentPassword.has_password && this.shortPollRunnable == null) {
            this.shortPollRunnable = new _$$Lambda$PaymentFormActivity$8V0l_YKYFLVQJz5pV_MyAIHFF4o(this);
            AndroidUtilities.runOnUIThread(this.shortPollRunnable, 5000L);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$26$PaymentFormActivity(TLRPC.TL_error var1) {
      this.showEditDoneProgress(true, false);
      if (var1 == null) {
         if (this.getParentActivity() == null) {
            return;
         }

         Runnable var3 = this.shortPollRunnable;
         if (var3 != null) {
            AndroidUtilities.cancelRunOnUIThread(var3);
            this.shortPollRunnable = null;
         }

         this.goToNextStep();
      } else if (var1.text.startsWith("CODE_INVALID")) {
         this.shakeView(this.codeFieldCell);
         this.codeFieldCell.setText("", false);
      } else if (var1.text.startsWith("FLOOD_WAIT")) {
         int var2 = Utilities.parseInt(var1.text);
         String var4;
         if (var2 < 60) {
            var4 = LocaleController.formatPluralString("Seconds", var2);
         } else {
            var4 = LocaleController.formatPluralString("Minutes", var2 / 60);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var4));
      } else {
         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      }

   }

   // $FF: synthetic method
   public void lambda$null$28$PaymentFormActivity(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      if (var1 == null) {
         this.currentPassword = (TLRPC.TL_account_password)var2;
         TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
         this.sendSavePassword(var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$29$PaymentFormActivity(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$O97PWkThOP8HBp4dlwmRsHFZ4Cg(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$30$PaymentFormActivity(String var1, DialogInterface var2, int var3) {
      this.waitingForEmail = true;
      this.currentPassword.email_unconfirmed_pattern = var1;
      this.updatePasswordFields();
   }

   // $FF: synthetic method
   public void lambda$null$31$PaymentFormActivity(TLRPC.TL_error var1, boolean var2, TLObject var3, String var4) {
      if (var1 != null && "SRP_ID_INVALID".equals(var1.text)) {
         TLRPC.TL_account_getPassword var10 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var10, new _$$Lambda$PaymentFormActivity$tcLMevZOApYaPGxte7o40Zbxk2c(this, var2), 8);
      } else {
         this.showEditDoneProgress(true, false);
         if (var2) {
            TLRPC.TL_account_password var6 = this.currentPassword;
            var6.has_password = false;
            var6.current_algo = null;
            this.delegate.currentPasswordUpdated(var6);
            this.finishFragment();
         } else if (var1 == null && var3 instanceof TLRPC.TL_boolTrue) {
            if (this.getParentActivity() == null) {
               return;
            }

            this.goToNextStep();
         } else if (var1 != null) {
            if (!var1.text.equals("EMAIL_UNCONFIRMED") && !var1.text.startsWith("EMAIL_UNCONFIRMED_")) {
               if (var1.text.equals("EMAIL_INVALID")) {
                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.getString("PasswordEmailInvalid", 2131560347));
               } else if (var1.text.startsWith("FLOOD_WAIT")) {
                  int var5 = Utilities.parseInt(var1.text);
                  String var9;
                  if (var5 < 60) {
                     var9 = LocaleController.formatPluralString("Seconds", var5);
                  } else {
                     var9 = LocaleController.formatPluralString("Minutes", var5 / 60);
                  }

                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var9));
               } else {
                  this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
               }
            } else {
               this.emailCodeLength = Utilities.parseInt(var1.text);
               AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
               var7.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PaymentFormActivity$9D_zlo6Zt3tzxdsZk8Ai17yN6Vs(this, var4));
               var7.setMessage(LocaleController.getString("YourEmailAlmostThereText", 2131561146));
               var7.setTitle(LocaleController.getString("YourEmailAlmostThere", 2131561145));
               Dialog var8 = this.showDialog(var7.create());
               if (var8 != null) {
                  var8.setCanceledOnTouchOutside(false);
                  var8.setCancelable(false);
               }
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$32$PaymentFormActivity(boolean var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$sxgdxN6G7g4EkMcZxwoY42jxlj4(this, var4, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$null$35$PaymentFormActivity(TLObject var1) {
      this.requestedInfo = (TLRPC.TL_payments_validatedRequestedInfo)var1;
      if (this.paymentForm.saved_info != null && !this.saveShippingInfo) {
         TLRPC.TL_payments_clearSavedInfo var2 = new TLRPC.TL_payments_clearSavedInfo();
         var2.info = true;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, _$$Lambda$PaymentFormActivity$sO9JvHK_i_UoC8_6WynH4daVgdY.INSTANCE);
      }

      this.goToNextStep();
      this.setDonePressed(false);
      this.showEditDoneProgress(true, false);
   }

   // $FF: synthetic method
   public void lambda$null$36$PaymentFormActivity(TLRPC.TL_error var1, TLObject var2) {
      this.setDonePressed(false);
      this.showEditDoneProgress(true, false);
      if (var1 != null) {
         byte var4;
         label52: {
            String var3 = var1.text;
            switch(var3.hashCode()) {
            case -2092780146:
               if (var3.equals("ADDRESS_CITY_INVALID")) {
                  var4 = 4;
                  break label52;
               }
               break;
            case -1623547228:
               if (var3.equals("ADDRESS_STREET_LINE1_INVALID")) {
                  var4 = 7;
                  break label52;
               }
               break;
            case -1224177757:
               if (var3.equals("ADDRESS_COUNTRY_INVALID")) {
                  var4 = 3;
                  break label52;
               }
               break;
            case -1031752045:
               if (var3.equals("REQ_INFO_NAME_INVALID")) {
                  var4 = 0;
                  break label52;
               }
               break;
            case -274035920:
               if (var3.equals("ADDRESS_POSTCODE_INVALID")) {
                  var4 = 5;
                  break label52;
               }
               break;
            case 417441502:
               if (var3.equals("ADDRESS_STATE_INVALID")) {
                  var4 = 6;
                  break label52;
               }
               break;
            case 708423542:
               if (var3.equals("REQ_INFO_PHONE_INVALID")) {
                  var4 = 1;
                  break label52;
               }
               break;
            case 863965605:
               if (var3.equals("ADDRESS_STREET_LINE2_INVALID")) {
                  var4 = 8;
                  break label52;
               }
               break;
            case 889106340:
               if (var3.equals("REQ_INFO_EMAIL_INVALID")) {
                  var4 = 2;
                  break label52;
               }
            }

            var4 = -1;
         }

         switch(var4) {
         case 0:
            this.shakeField(6);
            break;
         case 1:
            this.shakeField(9);
            break;
         case 2:
            this.shakeField(7);
            break;
         case 3:
            this.shakeField(4);
            break;
         case 4:
            this.shakeField(2);
            break;
         case 5:
            this.shakeField(5);
            break;
         case 6:
            this.shakeField(3);
            break;
         case 7:
            this.shakeField(0);
            break;
         case 8:
            this.shakeField(1);
            break;
         default:
            AlertsCreator.processError(super.currentAccount, var1, this, var2);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$38$PaymentFormActivity(TLObject var1) {
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.paymentFinished);
      this.setDonePressed(false);
      this.webView.setVisibility(0);
      this.webviewLoading = true;
      this.showEditDoneProgress(true, true);
      this.progressView.setVisibility(0);
      this.doneItem.setEnabled(false);
      this.doneItem.getImageView().setVisibility(4);
      WebView var2 = this.webView;
      String var3 = ((TLRPC.TL_payments_paymentVerficationNeeded)var1).url;
      this.webViewUrl = var3;
      var2.loadUrl(var3);
   }

   // $FF: synthetic method
   public void lambda$null$39$PaymentFormActivity(TLRPC.TL_error var1, TLRPC.TL_payments_sendPaymentForm var2) {
      AlertsCreator.processError(super.currentAccount, var1, this, var2);
      this.setDonePressed(false);
      this.showEditDoneProgress(false, false);
   }

   // $FF: synthetic method
   public void lambda$null$41$PaymentFormActivity(TLObject var1, TLRPC.TL_error var2, TLRPC.TL_account_getTmpPassword var3) {
      this.showEditDoneProgress(true, false);
      this.setDonePressed(false);
      if (var1 != null) {
         this.passwordOk = true;
         UserConfig.getInstance(super.currentAccount).tmpPassword = (TLRPC.TL_account_tmpPassword)var1;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
         this.goToNextStep();
      } else if (var2.text.equals("PASSWORD_HASH_INVALID")) {
         Vibrator var4 = (Vibrator)ApplicationLoader.applicationContext.getSystemService("vibrator");
         if (var4 != null) {
            var4.vibrate(200L);
         }

         AndroidUtilities.shakeView(this.inputFields[1], 2.0F, 0);
         this.inputFields[1].setText("");
      } else {
         AlertsCreator.processError(super.currentAccount, var2, this, var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$42$PaymentFormActivity(TLRPC.TL_account_getTmpPassword var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$WEnYGXUuB_UBrzR_7FlX_BgJs2A(this, var2, var3, var1));
   }

   // $FF: synthetic method
   public void lambda$null$43$PaymentFormActivity(TLRPC.TL_account_password var1, byte[] var2) {
      TLRPC.PasswordKdfAlgo var3 = var1.current_algo;
      if (var3 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         var2 = SRPHelper.getX(var2, (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var3);
      } else {
         var2 = null;
      }

      TLRPC.TL_account_getTmpPassword var4 = new TLRPC.TL_account_getTmpPassword();
      var4.period = 1800;
      _$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac var7 = new _$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac(this, var4);
      TLRPC.PasswordKdfAlgo var5 = var1.current_algo;
      TLRPC.TL_error var6;
      if (var5 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var8 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var5;
         var4.password = SRPHelper.startCheck(var2, var1.srp_id, var1.srp_B, var8);
         if (var4.password == null) {
            var6 = new TLRPC.TL_error();
            var6.text = "ALGO_INVALID";
            var7.run((TLObject)null, var6);
            return;
         }

         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, var7, 10);
      } else {
         var6 = new TLRPC.TL_error();
         var6.text = "PASSWORD_HASH_INVALID";
         var7.run((TLObject)null, var6);
      }

   }

   // $FF: synthetic method
   public void lambda$null$44$PaymentFormActivity(TLRPC.TL_error var1, TLObject var2, String var3, TLRPC.TL_account_getPassword var4) {
      if (var1 == null) {
         TLRPC.TL_account_password var5 = (TLRPC.TL_account_password)var2;
         if (!TwoStepVerificationActivity.canHandleCurrentPassword(var5, false)) {
            AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
            return;
         }

         if (!var5.has_password) {
            this.passwordOk = false;
            this.goToNextStep();
         } else {
            byte[] var6 = AndroidUtilities.getStringBytes(var3);
            Utilities.globalQueue.postRunnable(new _$$Lambda$PaymentFormActivity$k9SEAszMH3_nDlyEt9fLhgGlMAA(this, var5, var6));
         }
      } else {
         AlertsCreator.processError(super.currentAccount, var1, this, var4);
         this.showEditDoneProgress(true, false);
         this.setDonePressed(false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$PaymentFormActivity(String var1, String var2) {
      this.inputFields[4].setText(var1);
   }

   // $FF: synthetic method
   public void lambda$sendData$40$PaymentFormActivity(TLRPC.TL_payments_sendPaymentForm var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         if (var2 instanceof TLRPC.TL_payments_paymentResult) {
            MessagesController.getInstance(super.currentAccount).processUpdates(((TLRPC.TL_payments_paymentResult)var2).updates, false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$bzw4OVkpOzFWzvKyIdhX50nRu7A(this));
         } else if (var2 instanceof TLRPC.TL_payments_paymentVerficationNeeded) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$KLyhSsRtirfczI3lfwAKAEvcTeg(this, var2));
         }
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70(this, var3, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$sendForm$37$PaymentFormActivity(TLObject var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 instanceof TLRPC.TL_payments_validatedRequestedInfo) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$7Bb2ya9NV9z4zmrHXo2U4Oeqe_A(this, var2));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$dqb1yiZBfWsIMg61J_sdvcJToMY(this, var3, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$sendSavePassword$27$PaymentFormActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$rp_a840DeP90rN9bDu7luNsoJKE(this, var2));
   }

   // $FF: synthetic method
   public void lambda$sendSavePassword$33$PaymentFormActivity(boolean var1, String var2, String var3, TLRPC.TL_account_updatePasswordSettings var4) {
      _$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA var6 = new _$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA(this, var1, var2);
      if (!var1) {
         byte[] var7 = AndroidUtilities.getStringBytes(var3);
         TLRPC.PasswordKdfAlgo var5 = this.currentPassword.new_algo;
         TLRPC.TL_error var8;
         if (var5 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var9 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var5;
            var4.new_settings.new_password_hash = SRPHelper.getVBytes(var7, var9);
            if (var4.new_settings.new_password_hash == null) {
               var8 = new TLRPC.TL_error();
               var8.text = "ALGO_INVALID";
               var6.run((TLObject)null, var8);
            }

            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, var6, 10);
         } else {
            var8 = new TLRPC.TL_error();
            var8.text = "PASSWORD_HASH_INVALID";
            var6.run((TLObject)null, var8);
         }
      } else {
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, var6, 10);
      }

   }

   // $FF: synthetic method
   public void lambda$showPayAlert$25$PaymentFormActivity(DialogInterface var1, int var2) {
      this.setDonePressed(true);
      this.sendData();
   }

   public boolean onBackPressed() {
      if (this.shouldNavigateBack) {
         this.webView.loadUrl(this.webViewUrl);
         this.shouldNavigateBack = false;
         return false;
      } else {
         return this.donePressed ^ true;
      }
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
      if (this.currentStep != 4) {
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.paymentFinished);
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      PaymentFormActivity.PaymentFormActivityDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.onFragmentDestroyed();
      }

      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
      if (this.currentStep != 4) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
      }

      WebView var8 = this.webView;
      boolean var10001;
      if (var8 != null) {
         label69: {
            Exception var10000;
            label76: {
               ViewParent var9;
               try {
                  var9 = var8.getParent();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label76;
               }

               if (var9 != null) {
                  try {
                     ((ViewGroup)var9).removeView(this.webView);
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label76;
                  }
               }

               try {
                  this.webView.stopLoading();
                  this.webView.loadUrl("about:blank");
                  this.webViewUrl = null;
                  this.webView.destroy();
                  this.webView = null;
                  break label69;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var10 = var10000;
            FileLog.e((Throwable)var10);
         }
      }

      label57: {
         Throwable var12;
         label77: {
            try {
               if (this.currentStep != 2 && this.currentStep != 6) {
                  break label57;
               }
            } catch (Throwable var4) {
               var12 = var4;
               var10001 = false;
               break label77;
            }

            try {
               if (VERSION.SDK_INT < 23 || SharedConfig.passcodeHash.length() != 0 && !SharedConfig.allowScreenCapture) {
                  break label57;
               }
            } catch (Throwable var3) {
               var12 = var3;
               var10001 = false;
               break label77;
            }

            try {
               this.getParentActivity().getWindow().clearFlags(8192);
               break label57;
            } catch (Throwable var2) {
               var12 = var2;
               var10001 = false;
            }
         }

         Throwable var11 = var12;
         FileLog.e(var11);
      }

      super.onFragmentDestroy();
      this.canceled = true;
   }

   public void onPause() {
   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
      if (VERSION.SDK_INT >= 23) {
         try {
            if ((this.currentStep == 2 || this.currentStep == 6) && !this.paymentForm.invoice.test) {
               this.getParentActivity().getWindow().setFlags(8192, 8192);
            } else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
               this.getParentActivity().getWindow().clearFlags(8192);
            }
         } catch (Throwable var2) {
            FileLog.e(var2);
         }
      }

   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2) {
         WebView var3 = this.webView;
         if (var3 != null) {
            if (this.currentStep != 4) {
               String var4 = this.paymentForm.url;
               this.webViewUrl = var4;
               var3.loadUrl(var4);
            }
         } else {
            int var5 = this.currentStep;
            if (var5 == 2) {
               this.inputFields[0].requestFocus();
               AndroidUtilities.showKeyboard(this.inputFields[0]);
            } else if (var5 == 3) {
               this.inputFields[1].requestFocus();
               AndroidUtilities.showKeyboard(this.inputFields[1]);
            } else if (var5 == 6 && !this.waitingForEmail) {
               this.inputFields[0].requestFocus();
               AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
         }
      }

   }

   public class LinkSpan extends ClickableSpan {
      public void onClick(View var1) {
         PaymentFormActivity.this.presentFragment(new TwoStepVerificationActivity(0));
      }

      public void updateDrawState(TextPaint var1) {
         super.updateDrawState(var1);
         var1.setUnderlineText(false);
      }
   }

   private interface PaymentFormActivityDelegate {
      void currentPasswordUpdated(TLRPC.TL_account_password var1);

      boolean didSelectNewCard(String var1, String var2, boolean var3, TLRPC.TL_inputPaymentCredentialsAndroidPay var4);

      void onFragmentDestroyed();
   }

   private class TelegramWebviewProxy {
      private TelegramWebviewProxy() {
      }

      // $FF: synthetic method
      TelegramWebviewProxy(Object var2) {
         this();
      }

      // $FF: synthetic method
      public void lambda$postEvent$0$PaymentFormActivity$TelegramWebviewProxy(String var1, String var2) {
         if (PaymentFormActivity.this.getParentActivity() != null) {
            if (var1.equals("payment_form_submit")) {
               try {
                  JSONObject var3 = new JSONObject(var2);
                  JSONObject var5 = var3.getJSONObject("credentials");
                  PaymentFormActivity.this.paymentJson = var5.toString();
                  PaymentFormActivity.this.cardName = var3.getString("title");
               } catch (Throwable var4) {
                  PaymentFormActivity.this.paymentJson = var2;
                  FileLog.e(var4);
               }

               PaymentFormActivity.this.goToNextStep();
            }

         }
      }

      @JavascriptInterface
      public void postEvent(String var1, String var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PaymentFormActivity$TelegramWebviewProxy$GYkvPF7FkeOF4Jpek1fag25gp2E(this, var1, var2));
      }
   }
}
