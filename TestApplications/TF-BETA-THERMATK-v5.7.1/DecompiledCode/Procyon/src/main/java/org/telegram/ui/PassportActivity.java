// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.TextUtils$TruncateAt;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.messenger.DownloadController;
import android.content.pm.PackageInfo;
import android.os.Build;
import java.util.TimerTask;
import java.util.Timer;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.graphics.Bitmap;
import org.telegram.messenger.SRPHelper;
import java.util.Locale;
import org.telegram.messenger.MrzRecognizer;
import java.util.Calendar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.graphics.Rect;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberUtils;
import org.telegram.messenger.BuildVars;
import android.app.PendingIntent;
import org.telegram.messenger.SmsReceiver;
import android.telephony.SmsManager;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.widget.Toast;
import java.io.Serializable;
import android.os.Parcelable;
import androidx.core.content.FileProvider;
import android.content.Intent;
import org.telegram.ui.Components.AlertsCreator;
import java.io.File;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.FileLoader;
import android.os.Vibrator;
import org.json.JSONObject;
import org.telegram.messenger.SecureDocumentKey;
import java.util.Iterator;
import java.util.Map;
import android.text.method.MovementMethod;
import org.telegram.messenger.ApplicationLoader;
import android.telephony.TelephonyManager;
import android.view.View$OnKeyListener;
import org.telegram.ui.Components.HintEditText;
import org.telegram.PhoneFormat.PhoneFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.graphics.Typeface;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.UserObject;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.ui.Components.BackupImageView;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.LinearLayout$LayoutParams;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.messenger.MediaController;
import android.widget.TextView$OnEditorActionListener;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.StaticLayout;
import org.telegram.messenger.FileLog;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.Utilities;
import android.content.DialogInterface$OnClickListener;
import android.app.Activity;
import org.telegram.messenger.browser.Browser;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.messenger.SendMessagesHelper;
import android.text.Editable;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.LocaleController;
import android.util.Base64;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import android.widget.ScrollView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.ActionBar.AlertDialog;
import android.app.Dialog;
import android.view.ViewGroup;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.SecureDocument;
import android.view.View;
import android.widget.LinearLayout;
import java.util.HashMap;
import org.telegram.ui.Components.ChatAttachAlert;
import android.widget.FrameLayout;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextSettingsCell;
import android.widget.TextView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PassportActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int FIELD_ADDRESS_COUNT = 6;
    private static final int FIELD_BIRTHDAY = 3;
    private static final int FIELD_CARDNUMBER = 7;
    private static final int FIELD_CITIZENSHIP = 5;
    private static final int FIELD_CITY = 3;
    private static final int FIELD_COUNTRY = 5;
    private static final int FIELD_EMAIL = 0;
    private static final int FIELD_EXPIRE = 8;
    private static final int FIELD_GENDER = 4;
    private static final int FIELD_IDENTITY_COUNT = 9;
    private static final int FIELD_IDENTITY_NODOC_COUNT = 7;
    private static final int FIELD_MIDNAME = 1;
    private static final int FIELD_NAME = 0;
    private static final int FIELD_NATIVE_COUNT = 3;
    private static final int FIELD_NATIVE_MIDNAME = 1;
    private static final int FIELD_NATIVE_NAME = 0;
    private static final int FIELD_NATIVE_SURNAME = 2;
    private static final int FIELD_PASSWORD = 0;
    private static final int FIELD_PHONE = 2;
    private static final int FIELD_PHONECODE = 1;
    private static final int FIELD_PHONECOUNTRY = 0;
    private static final int FIELD_POSTCODE = 2;
    private static final int FIELD_RESIDENCE = 6;
    private static final int FIELD_STATE = 4;
    private static final int FIELD_STREET1 = 0;
    private static final int FIELD_STREET2 = 1;
    private static final int FIELD_SURNAME = 2;
    public static final int TYPE_ADDRESS = 2;
    public static final int TYPE_EMAIL = 4;
    public static final int TYPE_EMAIL_VERIFICATION = 6;
    public static final int TYPE_IDENTITY = 1;
    public static final int TYPE_MANAGE = 8;
    public static final int TYPE_PASSWORD = 5;
    public static final int TYPE_PHONE = 3;
    public static final int TYPE_PHONE_VERIFICATION = 7;
    public static final int TYPE_REQUEST = 0;
    private static final int UPLOADING_TYPE_DOCUMENTS = 0;
    private static final int UPLOADING_TYPE_FRONT = 2;
    private static final int UPLOADING_TYPE_REVERSE = 3;
    private static final int UPLOADING_TYPE_SELFIE = 1;
    private static final int UPLOADING_TYPE_TRANSLATION = 4;
    private static final int attach_document = 4;
    private static final int attach_gallery = 1;
    private static final int attach_photo = 0;
    private static final int done_button = 2;
    private static final int info_item = 1;
    private TextView acceptTextView;
    private TextSettingsCell addDocumentCell;
    private ShadowSectionCell addDocumentSectionCell;
    private boolean allowNonLatinName;
    private ArrayList<TLRPC.TL_secureRequiredType> availableDocumentTypes;
    private TextInfoPrivacyCell bottomCell;
    private TextInfoPrivacyCell bottomCellTranslation;
    private FrameLayout bottomLayout;
    private boolean callbackCalled;
    private ChatAttachAlert chatAttachAlert;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private int currentActivityType;
    private int currentBotId;
    private String currentCallbackUrl;
    private String currentCitizeship;
    private HashMap<String, String> currentDocumentValues;
    private TLRPC.TL_secureRequiredType currentDocumentsType;
    private TLRPC.TL_secureValue currentDocumentsTypeValue;
    private String currentEmail;
    private int[] currentExpireDate;
    private TLRPC.TL_account_authorizationForm currentForm;
    private String currentGender;
    private String currentNonce;
    private TLRPC.TL_account_password currentPassword;
    private String currentPayload;
    private TLRPC.TL_auth_sentCode currentPhoneVerification;
    private LinearLayout currentPhotoViewerLayout;
    private String currentPicturePath;
    private String currentPublicKey;
    private String currentResidence;
    private String currentScope;
    private TLRPC.TL_secureRequiredType currentType;
    private TLRPC.TL_secureValue currentTypeValue;
    private HashMap<String, String> currentValues;
    private int currentViewNum;
    private PassportActivityDelegate delegate;
    private TextSettingsCell deletePassportCell;
    private ArrayList<View> dividers;
    private boolean documentOnly;
    private ArrayList<SecureDocument> documents;
    private HashMap<SecureDocument, SecureDocumentCell> documentsCells;
    private HashMap<String, String> documentsErrors;
    private LinearLayout documentsLayout;
    private HashMap<TLRPC.TL_secureRequiredType, TLRPC.TL_secureRequiredType> documentsToTypesLink;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private int emailCodeLength;
    private ImageView emptyImageView;
    private LinearLayout emptyLayout;
    private TextView emptyTextView1;
    private TextView emptyTextView2;
    private TextView emptyTextView3;
    private EmptyTextProgressView emptyView;
    private HashMap<String, HashMap<String, String>> errorsMap;
    private HashMap<String, String> errorsValues;
    private View extraBackgroundView;
    private View extraBackgroundView2;
    private HashMap<String, String> fieldsErrors;
    private SecureDocument frontDocument;
    private LinearLayout frontLayout;
    private HeaderCell headerCell;
    private boolean ignoreOnFailure;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private String initialValues;
    private EditTextBoldCursor[] inputExtraFields;
    private ViewGroup[] inputFieldContainers;
    private EditTextBoldCursor[] inputFields;
    private HashMap<String, String> languageMap;
    private LinearLayout linearLayout2;
    private HashMap<String, String> mainErrorsMap;
    private TextInfoPrivacyCell nativeInfoCell;
    private boolean needActivityResult;
    private CharSequence noAllDocumentsErrorText;
    private CharSequence noAllTranslationErrorText;
    private ImageView noPasswordImageView;
    private TextView noPasswordSetTextView;
    private TextView noPasswordTextView;
    private boolean[] nonLatinNames;
    private FrameLayout passwordAvatarContainer;
    private TextView passwordForgotButton;
    private TextInfoPrivacyCell passwordInfoRequestTextView;
    private TextInfoPrivacyCell passwordRequestTextView;
    private PassportActivityDelegate pendingDelegate;
    private ErrorRunnable pendingErrorRunnable;
    private Runnable pendingFinishRunnable;
    private String pendingPhone;
    private Dialog permissionsDialog;
    private ArrayList<String> permissionsItems;
    private HashMap<String, String> phoneFormatMap;
    private TextView plusTextView;
    private PassportActivity presentAfterAnimation;
    private AlertDialog progressDialog;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private PhotoViewer.PhotoViewerProvider provider;
    private SecureDocument reverseDocument;
    private LinearLayout reverseLayout;
    private byte[] saltedPassword;
    private byte[] savedPasswordHash;
    private byte[] savedSaltedPassword;
    private TextSettingsCell scanDocumentCell;
    private int scrollHeight;
    private ScrollView scrollView;
    private ShadowSectionCell sectionCell;
    private ShadowSectionCell sectionCell2;
    private byte[] secureSecret;
    private long secureSecretId;
    private SecureDocument selfieDocument;
    private LinearLayout selfieLayout;
    private TextInfoPrivacyCell topErrorCell;
    private ArrayList<SecureDocument> translationDocuments;
    private LinearLayout translationLayout;
    private HashMap<TLRPC.TL_secureRequiredType, HashMap<String, String>> typesValues;
    private HashMap<TLRPC.TL_secureRequiredType, TextDetailSecureCell> typesViews;
    private TextSettingsCell uploadDocumentCell;
    private TextDetailSettingsCell uploadFrontCell;
    private TextDetailSettingsCell uploadReverseCell;
    private TextDetailSettingsCell uploadSelfieCell;
    private TextSettingsCell uploadTranslationCell;
    private HashMap<String, SecureDocument> uploadingDocuments;
    private int uploadingFileType;
    private boolean useCurrentValue;
    private int usingSavedPassword;
    private SlideView[] views;
    
    public PassportActivity(int size, int i, String s, final String currentPublicKey, String value, String str, String field, final TLRPC.TL_account_authorizationForm tl_account_authorizationForm, final TLRPC.TL_account_password tl_account_password) {
        this(size, tl_account_authorizationForm, tl_account_password, null, null, null, null, null, null);
        this.currentBotId = i;
        this.currentPayload = value;
        this.currentNonce = str;
        this.currentScope = s;
        this.currentPublicKey = currentPublicKey;
        this.currentCallbackUrl = field;
        if (size != 0 || tl_account_authorizationForm.errors.isEmpty()) {
            return;
        }
        try {
            Collections.sort(tl_account_authorizationForm.errors, new Comparator<TLRPC.SecureValueError>() {
                @Override
                public int compare(final TLRPC.SecureValueError secureValueError, final TLRPC.SecureValueError secureValueError2) {
                    final int errorValue = this.getErrorValue(secureValueError);
                    final int errorValue2 = this.getErrorValue(secureValueError2);
                    if (errorValue < errorValue2) {
                        return -1;
                    }
                    if (errorValue > errorValue2) {
                        return 1;
                    }
                    return 0;
                }
                
                int getErrorValue(final TLRPC.SecureValueError secureValueError) {
                    if (secureValueError instanceof TLRPC.TL_secureValueError) {
                        return 0;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorFrontSide) {
                        return 1;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorReverseSide) {
                        return 2;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorSelfie) {
                        return 3;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorTranslationFile) {
                        return 4;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorTranslationFiles) {
                        return 5;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorFile) {
                        return 6;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorFiles) {
                        return 7;
                    }
                    if (secureValueError instanceof TLRPC.TL_secureValueErrorData) {
                        return PassportActivity.this.getFieldCost(((TLRPC.TL_secureValueErrorData)secureValueError).field);
                    }
                    return 100;
                }
            });
        Label_0997:
            for (size = tl_account_authorizationForm.errors.size(), i = 0; i < size; ++i) {
                final TLRPC.SecureValueError secureValueError = tl_account_authorizationForm.errors.get(i);
                final boolean b = secureValueError instanceof TLRPC.TL_secureValueErrorFrontSide;
                field = null;
                byte[] array = null;
                Label_0589: {
                    if (b) {
                        final TLRPC.TL_secureValueErrorFrontSide tl_secureValueErrorFrontSide = (TLRPC.TL_secureValueErrorFrontSide)secureValueError;
                        str = this.getNameForType(tl_secureValueErrorFrontSide.type);
                        value = tl_secureValueErrorFrontSide.text;
                        array = tl_secureValueErrorFrontSide.file_hash;
                        s = "front";
                    }
                    else if (secureValueError instanceof TLRPC.TL_secureValueErrorReverseSide) {
                        final TLRPC.TL_secureValueErrorReverseSide tl_secureValueErrorReverseSide = (TLRPC.TL_secureValueErrorReverseSide)secureValueError;
                        str = this.getNameForType(tl_secureValueErrorReverseSide.type);
                        value = tl_secureValueErrorReverseSide.text;
                        array = tl_secureValueErrorReverseSide.file_hash;
                        s = "reverse";
                    }
                    else if (secureValueError instanceof TLRPC.TL_secureValueErrorSelfie) {
                        final TLRPC.TL_secureValueErrorSelfie tl_secureValueErrorSelfie = (TLRPC.TL_secureValueErrorSelfie)secureValueError;
                        str = this.getNameForType(tl_secureValueErrorSelfie.type);
                        value = tl_secureValueErrorSelfie.text;
                        array = tl_secureValueErrorSelfie.file_hash;
                        s = "selfie";
                    }
                    else {
                        if (secureValueError instanceof TLRPC.TL_secureValueErrorTranslationFile) {
                            final TLRPC.TL_secureValueErrorTranslationFile tl_secureValueErrorTranslationFile = (TLRPC.TL_secureValueErrorTranslationFile)secureValueError;
                            str = this.getNameForType(tl_secureValueErrorTranslationFile.type);
                            value = tl_secureValueErrorTranslationFile.text;
                            array = tl_secureValueErrorTranslationFile.file_hash;
                        }
                        else {
                            if (!(secureValueError instanceof TLRPC.TL_secureValueErrorTranslationFiles)) {
                                if (secureValueError instanceof TLRPC.TL_secureValueErrorFile) {
                                    final TLRPC.TL_secureValueErrorFile tl_secureValueErrorFile = (TLRPC.TL_secureValueErrorFile)secureValueError;
                                    str = this.getNameForType(tl_secureValueErrorFile.type);
                                    value = tl_secureValueErrorFile.text;
                                    array = tl_secureValueErrorFile.file_hash;
                                }
                                else if (secureValueError instanceof TLRPC.TL_secureValueErrorFiles) {
                                    final TLRPC.TL_secureValueErrorFiles tl_secureValueErrorFiles = (TLRPC.TL_secureValueErrorFiles)secureValueError;
                                    str = this.getNameForType(tl_secureValueErrorFiles.type);
                                    value = tl_secureValueErrorFiles.text;
                                    array = null;
                                }
                                else {
                                    if (secureValueError instanceof TLRPC.TL_secureValueError) {
                                        final TLRPC.TL_secureValueError tl_secureValueError = (TLRPC.TL_secureValueError)secureValueError;
                                        str = this.getNameForType(tl_secureValueError.type);
                                        value = tl_secureValueError.text;
                                        array = tl_secureValueError.hash;
                                        s = "error_all";
                                        break Label_0589;
                                    }
                                    if (secureValueError instanceof TLRPC.TL_secureValueErrorData) {
                                        final TLRPC.TL_secureValueErrorData tl_secureValueErrorData = (TLRPC.TL_secureValueErrorData)secureValueError;
                                        int j = 0;
                                        while (true) {
                                            while (j < tl_account_authorizationForm.values.size()) {
                                                final TLRPC.TL_secureValue tl_secureValue = tl_account_authorizationForm.values.get(j);
                                                if (tl_secureValue.data != null && Arrays.equals(tl_secureValue.data.data_hash, tl_secureValueErrorData.data_hash)) {
                                                    final boolean b2 = true;
                                                    if (!b2) {
                                                        continue Label_0997;
                                                    }
                                                    str = this.getNameForType(tl_secureValueErrorData.type);
                                                    value = tl_secureValueErrorData.text;
                                                    field = tl_secureValueErrorData.field;
                                                    array = tl_secureValueErrorData.data_hash;
                                                    s = "data";
                                                    break Label_0589;
                                                }
                                                else {
                                                    ++j;
                                                }
                                            }
                                            final boolean b2 = false;
                                            continue;
                                        }
                                    }
                                    continue;
                                }
                                s = "files";
                                break Label_0589;
                            }
                            final TLRPC.TL_secureValueErrorTranslationFiles tl_secureValueErrorTranslationFiles = (TLRPC.TL_secureValueErrorTranslationFiles)secureValueError;
                            str = this.getNameForType(tl_secureValueErrorTranslationFiles.type);
                            value = tl_secureValueErrorTranslationFiles.text;
                            array = null;
                        }
                        s = "translation";
                    }
                }
                HashMap<String, String> value2;
                if ((value2 = this.errorsMap.get(str)) == null) {
                    value2 = new HashMap<String, String>();
                    this.errorsMap.put(str, value2);
                    this.mainErrorsMap.put(str, value);
                }
                if (array != null) {
                    str = Base64.encodeToString(array, 2);
                }
                else {
                    str = "";
                }
                if ("data".equals(s)) {
                    if (field != null) {
                        value2.put(field, value);
                    }
                }
                else if ("files".equals(s)) {
                    if (array != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("files");
                        sb.append(str);
                        value2.put(sb.toString(), value);
                    }
                    else {
                        value2.put("files_all", value);
                    }
                }
                else if ("selfie".equals(s)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("selfie");
                    sb2.append(str);
                    value2.put(sb2.toString(), value);
                }
                else if ("translation".equals(s)) {
                    if (array != null) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("translation");
                        sb3.append(str);
                        value2.put(sb3.toString(), value);
                    }
                    else {
                        value2.put("translation_all", value);
                    }
                }
                else if ("front".equals(s)) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("front");
                    sb4.append(str);
                    value2.put(sb4.toString(), value);
                }
                else if ("reverse".equals(s)) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("reverse");
                    sb5.append(str);
                    value2.put(sb5.toString(), value);
                }
                else if ("error_all".equals(s)) {
                    value2.put("error_all", value);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public PassportActivity(final int currentActivityType, final TLRPC.TL_account_authorizationForm currentForm, final TLRPC.TL_account_password currentPassword, final TLRPC.TL_secureRequiredType currentType, final TLRPC.TL_secureValue currentTypeValue, final TLRPC.TL_secureRequiredType currentDocumentsType, final TLRPC.TL_secureValue currentDocumentsTypeValue, final HashMap<String, String> currentValues, final HashMap<String, String> currentDocumentValues) {
        this.currentCitizeship = "";
        this.currentResidence = "";
        this.currentExpireDate = new int[3];
        this.dividers = new ArrayList<View>();
        this.nonLatinNames = new boolean[3];
        this.allowNonLatinName = true;
        this.countriesArray = new ArrayList<String>();
        this.countriesMap = new HashMap<String, String>();
        this.codesMap = new HashMap<String, String>();
        this.phoneFormatMap = new HashMap<String, String>();
        this.documents = new ArrayList<SecureDocument>();
        this.translationDocuments = new ArrayList<SecureDocument>();
        this.documentsCells = new HashMap<SecureDocument, SecureDocumentCell>();
        this.uploadingDocuments = new HashMap<String, SecureDocument>();
        this.typesValues = new HashMap<TLRPC.TL_secureRequiredType, HashMap<String, String>>();
        this.typesViews = new HashMap<TLRPC.TL_secureRequiredType, TextDetailSecureCell>();
        this.documentsToTypesLink = new HashMap<TLRPC.TL_secureRequiredType, TLRPC.TL_secureRequiredType>();
        this.errorsMap = new HashMap<String, HashMap<String, String>>();
        this.mainErrorsMap = new HashMap<String, String>();
        this.errorsValues = new HashMap<String, String>();
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
            @Override
            public void deleteImageAtIndex(int access$200) {
                SecureDocument key;
                if (PassportActivity.this.uploadingFileType == 1) {
                    key = PassportActivity.this.selfieDocument;
                }
                else if (PassportActivity.this.uploadingFileType == 4) {
                    key = PassportActivity.this.translationDocuments.get(access$200);
                }
                else if (PassportActivity.this.uploadingFileType == 2) {
                    key = PassportActivity.this.frontDocument;
                }
                else if (PassportActivity.this.uploadingFileType == 3) {
                    key = PassportActivity.this.reverseDocument;
                }
                else {
                    key = PassportActivity.this.documents.get(access$200);
                }
                final SecureDocumentCell secureDocumentCell = PassportActivity.this.documentsCells.remove(key);
                if (secureDocumentCell == null) {
                    return;
                }
                final String access$201 = PassportActivity.this.getDocumentHash(key);
                access$200 = PassportActivity.this.uploadingFileType;
                Object o = null;
                if (access$200 == 1) {
                    PassportActivity.this.selfieDocument = null;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("selfie");
                    sb.append(access$201);
                    o = sb.toString();
                }
                else if (PassportActivity.this.uploadingFileType == 4) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("translation");
                    sb2.append(access$201);
                    o = sb2.toString();
                }
                else if (PassportActivity.this.uploadingFileType == 2) {
                    PassportActivity.this.frontDocument = null;
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("front");
                    sb3.append(access$201);
                    o = sb3.toString();
                }
                else if (PassportActivity.this.uploadingFileType == 3) {
                    PassportActivity.this.reverseDocument = null;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("reverse");
                    sb4.append(access$201);
                    o = sb4.toString();
                }
                else if (PassportActivity.this.uploadingFileType == 0) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("files");
                    sb5.append(access$201);
                    o = sb5.toString();
                }
                if (o != null) {
                    if (PassportActivity.this.documentsErrors != null) {
                        PassportActivity.this.documentsErrors.remove(o);
                    }
                    if (PassportActivity.this.errorsValues != null) {
                        PassportActivity.this.errorsValues.remove(o);
                    }
                }
                final PassportActivity this$0 = PassportActivity.this;
                this$0.updateUploadText(this$0.uploadingFileType);
                PassportActivity.this.currentPhotoViewerLayout.removeView((View)secureDocumentCell);
            }
            
            @Override
            public String getDeleteMessageString() {
                if (PassportActivity.this.uploadingFileType == 1) {
                    return LocaleController.formatString("PassportDeleteSelfieAlert", 2131560211, new Object[0]);
                }
                return LocaleController.formatString("PassportDeleteScanAlert", 2131560209, new Object[0]);
            }
            
            @Override
            public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, int statusBarHeight, final boolean b) {
                if (statusBarHeight >= 0 && statusBarHeight < PassportActivity.this.currentPhotoViewerLayout.getChildCount()) {
                    final SecureDocumentCell secureDocumentCell = (SecureDocumentCell)PassportActivity.this.currentPhotoViewerLayout.getChildAt(statusBarHeight);
                    final int[] array = new int[2];
                    secureDocumentCell.imageView.getLocationInWindow(array);
                    final PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                    statusBarHeight = 0;
                    placeProviderObject.viewX = array[0];
                    final int n = array[1];
                    if (Build$VERSION.SDK_INT < 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    placeProviderObject.viewY = n - statusBarHeight;
                    placeProviderObject.parentView = (View)PassportActivity.this.currentPhotoViewerLayout;
                    placeProviderObject.imageReceiver = secureDocumentCell.imageView.getImageReceiver();
                    placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                    return placeProviderObject;
                }
                return null;
            }
        };
        this.currentActivityType = currentActivityType;
        this.currentForm = currentForm;
        this.currentType = currentType;
        final TLRPC.TL_secureRequiredType currentType2 = this.currentType;
        if (currentType2 != null) {
            this.allowNonLatinName = currentType2.native_names;
        }
        this.currentTypeValue = currentTypeValue;
        this.currentDocumentsType = currentDocumentsType;
        this.currentDocumentsTypeValue = currentDocumentsTypeValue;
        this.currentPassword = currentPassword;
        this.currentValues = currentValues;
        this.currentDocumentValues = currentDocumentValues;
        final int currentActivityType2 = this.currentActivityType;
        if (currentActivityType2 == 3) {
            this.permissionsItems = new ArrayList<String>();
        }
        else if (currentActivityType2 == 7) {
            this.views = new SlideView[3];
        }
        if (this.currentValues == null) {
            this.currentValues = new HashMap<String, String>();
        }
        if (this.currentDocumentValues == null) {
            this.currentDocumentValues = new HashMap<String, String>();
        }
        if (currentActivityType == 5) {
            if (UserConfig.getInstance(super.currentAccount).savedPasswordHash != null && UserConfig.getInstance(super.currentAccount).savedSaltedPassword != null) {
                this.usingSavedPassword = 1;
                this.savedPasswordHash = UserConfig.getInstance(super.currentAccount).savedPasswordHash;
                this.savedSaltedPassword = UserConfig.getInstance(super.currentAccount).savedSaltedPassword;
            }
            final TLRPC.TL_account_password currentPassword2 = this.currentPassword;
            if (currentPassword2 == null) {
                this.loadPasswordInfo();
            }
            else {
                TwoStepVerificationActivity.initPasswordNewAlgo(currentPassword2);
                if (this.usingSavedPassword == 1) {
                    this.onPasswordDone(true);
                }
            }
            if (!SharedConfig.isPassportConfigLoaded()) {
                final TLRPC.TL_help_getPassportConfig tl_help_getPassportConfig = new TLRPC.TL_help_getPassportConfig();
                tl_help_getPassportConfig.hash = SharedConfig.passportConfigHash;
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_help_getPassportConfig, (RequestDelegate)_$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM.INSTANCE);
            }
        }
    }
    
    private void addDocumentView(final SecureDocument key, final int n) {
        if (n == 1) {
            this.selfieDocument = key;
            if (this.selfieLayout == null) {
                return;
            }
        }
        else if (n == 4) {
            this.translationDocuments.add(key);
            if (this.translationLayout == null) {
                return;
            }
        }
        else if (n == 2) {
            this.frontDocument = key;
            if (this.frontLayout == null) {
                return;
            }
        }
        else if (n == 3) {
            this.reverseDocument = key;
            if (this.reverseLayout == null) {
                return;
            }
        }
        else {
            this.documents.add(key);
            if (this.documentsLayout == null) {
                return;
            }
        }
        if (this.getParentActivity() == null) {
            return;
        }
        final SecureDocumentCell value = new SecureDocumentCell((Context)this.getParentActivity());
        value.setTag((Object)key);
        value.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.documentsCells.put(key, value);
        final String documentHash = this.getDocumentHash(key);
        String s;
        String s2;
        if (n == 1) {
            s = LocaleController.getString("PassportSelfie", 2131560329);
            this.selfieLayout.addView((View)value, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final StringBuilder sb = new StringBuilder();
            sb.append("selfie");
            sb.append(documentHash);
            s2 = sb.toString();
        }
        else if (n == 4) {
            s = LocaleController.getString("AttachPhoto", 2131558727);
            this.translationLayout.addView((View)value, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("translation");
            sb2.append(documentHash);
            s2 = sb2.toString();
        }
        else if (n == 2) {
            final TLRPC.SecureValueType type = this.currentDocumentsType.type;
            if (!(type instanceof TLRPC.TL_secureValueTypePassport) && !(type instanceof TLRPC.TL_secureValueTypeInternalPassport)) {
                s = LocaleController.getString("PassportFrontSide", 2131560224);
            }
            else {
                s = LocaleController.getString("PassportMainPage", 2131560282);
            }
            this.frontLayout.addView((View)value, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("front");
            sb3.append(documentHash);
            s2 = sb3.toString();
        }
        else if (n == 3) {
            s = LocaleController.getString("PassportReverseSide", 2131560320);
            this.reverseLayout.addView((View)value, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("reverse");
            sb4.append(documentHash);
            s2 = sb4.toString();
        }
        else {
            s = LocaleController.getString("AttachPhoto", 2131558727);
            this.documentsLayout.addView((View)value, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("files");
            sb5.append(documentHash);
            s2 = sb5.toString();
        }
        String formatDateForBan = null;
        Label_0589: {
            if (s2 != null) {
                final HashMap<String, String> documentsErrors = this.documentsErrors;
                if (documentsErrors != null) {
                    formatDateForBan = documentsErrors.get(s2);
                    if (formatDateForBan != null) {
                        value.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                        this.errorsValues.put(s2, "");
                        break Label_0589;
                    }
                }
            }
            formatDateForBan = LocaleController.formatDateForBan(key.secureFile.date);
        }
        value.setTextAndValueAndImage(s, formatDateForBan, key);
        value.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$c_SivFsgu6h4BCXz5VeKlb47xZU(this, n));
        value.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$PassportActivity$eMgihqVNJqkr7oPmUY_yq5VUzi4(this, n, key, value, s2));
    }
    
    private void addDocumentViewInternal(final TLRPC.TL_secureFile tl_secureFile, final int n) {
        this.addDocumentView(new SecureDocument(this.getSecureDocumentKey(tl_secureFile.secret, tl_secureFile.file_hash), tl_secureFile, null, null, null), n);
    }
    
    private void addDocumentViews(final ArrayList<TLRPC.SecureFile> list) {
        this.documents.clear();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final TLRPC.SecureFile secureFile = list.get(i);
            if (secureFile instanceof TLRPC.TL_secureFile) {
                this.addDocumentViewInternal((TLRPC.TL_secureFile)secureFile, 0);
            }
        }
    }
    
    private TextDetailSecureCell addField(final Context context, final TLRPC.TL_secureRequiredType value, final ArrayList<TLRPC.TL_secureRequiredType> list, final boolean b, final boolean b2) {
        int size;
        if (list != null) {
            size = list.size();
        }
        else {
            size = 0;
        }
        final TextDetailSecureCell value2 = new TextDetailSecureCell(context);
        value2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        final TLRPC.SecureValueType type = value.type;
        if (type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            String s;
            if (list != null && !list.isEmpty()) {
                if (b && list.size() == 1) {
                    s = this.getTextForType(list.get(0).type);
                }
                else if (b && list.size() == 2) {
                    s = LocaleController.formatString("PassportTwoDocuments", 2131560338, this.getTextForType(list.get(0).type), this.getTextForType(list.get(1).type));
                }
                else {
                    s = LocaleController.getString("PassportIdentityDocument", 2131560227);
                }
            }
            else {
                s = LocaleController.getString("PassportPersonalDetails", 2131560301);
            }
            value2.setTextAndValue(s, "", b2 ^ true);
        }
        else if (type instanceof TLRPC.TL_secureValueTypeAddress) {
            String s2;
            if (list != null && !list.isEmpty()) {
                if (b && list.size() == 1) {
                    s2 = this.getTextForType(list.get(0).type);
                }
                else if (b && list.size() == 2) {
                    s2 = LocaleController.formatString("PassportTwoDocuments", 2131560338, this.getTextForType(list.get(0).type), this.getTextForType(list.get(1).type));
                }
                else {
                    s2 = LocaleController.getString("PassportResidentialAddress", 2131560319);
                }
            }
            else {
                s2 = LocaleController.getString("PassportAddress", 2131560189);
            }
            value2.setTextAndValue(s2, "", b2 ^ true);
        }
        else if (type instanceof TLRPC.TL_secureValueTypePhone) {
            value2.setTextAndValue(LocaleController.getString("PassportPhone", 2131560304), "", b2 ^ true);
        }
        else if (type instanceof TLRPC.TL_secureValueTypeEmail) {
            value2.setTextAndValue(LocaleController.getString("PassportEmail", 2131560217), "", b2 ^ true);
        }
        if (this.currentActivityType == 8) {
            final LinearLayout linearLayout2 = this.linearLayout2;
            linearLayout2.addView((View)value2, linearLayout2.getChildCount() - 5, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        else {
            this.linearLayout2.addView((View)value2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        value2.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$5Sry1zhVbDTBEYl5VpIUjvovUNY(this, list, value, b));
        this.typesViews.put(value, value2);
        this.typesValues.put(value, new HashMap<String, String>());
        final TLRPC.TL_secureValue valueByType = this.getValueByType(value, false);
        String decryptData = null;
        String s3 = null;
        String decryptData2 = null;
        Label_0635: {
            Label_0628: {
                if (valueByType != null) {
                    final TLRPC.SecurePlainData plain_data = valueByType.plain_data;
                    if (plain_data instanceof TLRPC.TL_securePlainEmail) {
                        s3 = ((TLRPC.TL_securePlainEmail)plain_data).email;
                    }
                    else if (plain_data instanceof TLRPC.TL_securePlainPhone) {
                        s3 = ((TLRPC.TL_securePlainPhone)plain_data).phone;
                    }
                    else {
                        final TLRPC.TL_secureData data = valueByType.data;
                        if (data != null) {
                            decryptData2 = this.decryptData(data.data, this.decryptValueSecret(data.secret, data.data_hash), valueByType.data.data_hash);
                            s3 = null;
                            break Label_0635;
                        }
                        break Label_0628;
                    }
                    decryptData2 = null;
                    break Label_0635;
                }
            }
            s3 = (decryptData2 = null);
        }
        TLRPC.TL_secureRequiredType tl_secureRequiredType3;
        if (list != null && !list.isEmpty()) {
            final int size2 = list.size();
            Object o = decryptData = null;
            int i = 0;
            int n = 0;
            while (i < size2) {
                final TLRPC.TL_secureRequiredType tl_secureRequiredType = list.get(i);
                this.typesValues.put(tl_secureRequiredType, new HashMap<String, String>());
                this.documentsToTypesLink.put(tl_secureRequiredType, value);
                TLRPC.TL_secureRequiredType tl_secureRequiredType2 = (TLRPC.TL_secureRequiredType)o;
                int n2 = n;
                String s4 = decryptData;
                if (n == 0) {
                    final TLRPC.TL_secureValue valueByType2 = this.getValueByType(tl_secureRequiredType, false);
                    tl_secureRequiredType2 = (TLRPC.TL_secureRequiredType)o;
                    n2 = n;
                    s4 = decryptData;
                    if (valueByType2 != null) {
                        final TLRPC.TL_secureData data2 = valueByType2.data;
                        if (data2 != null) {
                            decryptData = this.decryptData(data2.data, this.decryptValueSecret(data2.secret, data2.data_hash), valueByType2.data.data_hash);
                        }
                        tl_secureRequiredType2 = tl_secureRequiredType;
                        n2 = 1;
                        s4 = decryptData;
                    }
                }
                ++i;
                o = tl_secureRequiredType2;
                n = n2;
                decryptData = s4;
            }
            if ((tl_secureRequiredType3 = (TLRPC.TL_secureRequiredType)o) == null) {
                tl_secureRequiredType3 = list.get(0);
            }
        }
        else {
            tl_secureRequiredType3 = null;
        }
        this.setTypeValue(value, s3, decryptData2, tl_secureRequiredType3, decryptData, b, size);
        return value2;
    }
    
    private void addTranslationDocumentViews(final ArrayList<TLRPC.SecureFile> list) {
        this.translationDocuments.clear();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final TLRPC.SecureFile secureFile = list.get(i);
            if (secureFile instanceof TLRPC.TL_secureFile) {
                this.addDocumentViewInternal((TLRPC.TL_secureFile)secureFile, 4);
            }
        }
    }
    
    private void callCallback(final boolean b) {
        if (!this.callbackCalled) {
            if (!TextUtils.isEmpty((CharSequence)this.currentCallbackUrl)) {
                if (b) {
                    final Activity parentActivity = this.getParentActivity();
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.currentCallbackUrl);
                    sb.append("&tg_passport=success");
                    Browser.openUrl((Context)parentActivity, Uri.parse(sb.toString()));
                }
                else if (!this.ignoreOnFailure) {
                    final int currentActivityType = this.currentActivityType;
                    if (currentActivityType == 5 || currentActivityType == 0) {
                        final Activity parentActivity2 = this.getParentActivity();
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.currentCallbackUrl);
                        sb2.append("&tg_passport=cancel");
                        Browser.openUrl((Context)parentActivity2, Uri.parse(sb2.toString()));
                    }
                }
                this.callbackCalled = true;
            }
            else if (this.needActivityResult) {
                Label_0197: {
                    if (!b) {
                        if (this.ignoreOnFailure) {
                            break Label_0197;
                        }
                        final int currentActivityType2 = this.currentActivityType;
                        if (currentActivityType2 != 5 && currentActivityType2 != 0) {
                            break Label_0197;
                        }
                    }
                    final Activity parentActivity3 = this.getParentActivity();
                    int result;
                    if (b) {
                        result = -1;
                    }
                    else {
                        result = 0;
                    }
                    parentActivity3.setResult(result);
                }
                this.callbackCalled = true;
            }
        }
    }
    
    private boolean checkDiscard() {
        if (this.isHasNotAnyChanges()) {
            return false;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setPositiveButton(LocaleController.getString("PassportDiscard", 2131560212), (DialogInterface$OnClickListener)new _$$Lambda$PassportActivity$Z4N0XgwepebvvcdAUdqH_Rebz4Y(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        builder.setTitle(LocaleController.getString("DiscardChanges", 2131559273));
        builder.setMessage(LocaleController.getString("PassportDiscardChanges", 2131560213));
        this.showDialog(builder.create());
        return true;
    }
    
    private void checkFieldForError(final EditTextBoldCursor editTextBoldCursor, String s, final Editable editable, final boolean b) {
        final HashMap<String, String> errorsValues = this.errorsValues;
        Label_0109: {
            if (errorsValues != null) {
                final String s2 = errorsValues.get(s);
                if (s2 != null) {
                    if (!TextUtils.equals((CharSequence)s2, (CharSequence)editable)) {
                        editTextBoldCursor.setErrorText(null);
                        break Label_0109;
                    }
                    final HashMap<String, String> fieldsErrors = this.fieldsErrors;
                    if (fieldsErrors != null) {
                        final String errorText = fieldsErrors.get(s);
                        if (errorText != null) {
                            editTextBoldCursor.setErrorText(errorText);
                            break Label_0109;
                        }
                    }
                    final HashMap<String, String> documentsErrors = this.documentsErrors;
                    if (documentsErrors == null) {
                        break Label_0109;
                    }
                    s = documentsErrors.get(s);
                    if (s != null) {
                        editTextBoldCursor.setErrorText(s);
                    }
                    break Label_0109;
                }
            }
            editTextBoldCursor.setErrorText(null);
        }
        String s3;
        if (b) {
            s3 = "error_document_all";
        }
        else {
            s3 = "error_all";
        }
        final HashMap<String, String> errorsValues2 = this.errorsValues;
        if (errorsValues2 != null && errorsValues2.containsKey(s3)) {
            this.errorsValues.remove(s3);
            this.checkTopErrorCell(false);
        }
    }
    
    private boolean checkFieldsForError() {
        if (this.currentDocumentsType != null) {
            if (this.errorsValues.containsKey("error_all") || this.errorsValues.containsKey("error_document_all")) {
                this.onFieldError((View)this.topErrorCell);
                return true;
            }
            if (this.uploadDocumentCell != null) {
                if (this.documents.isEmpty()) {
                    this.onFieldError((View)this.uploadDocumentCell);
                    return true;
                }
                for (int size = this.documents.size(), i = 0; i < size; ++i) {
                    final SecureDocument key = this.documents.get(i);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("files");
                    sb.append(this.getDocumentHash(key));
                    final String string = sb.toString();
                    if (string != null && this.errorsValues.containsKey(string)) {
                        this.onFieldError((View)this.documentsCells.get(key));
                        return true;
                    }
                }
            }
            if (this.errorsValues.containsKey("files_all") || this.errorsValues.containsKey("translation_all")) {
                this.onFieldError((View)this.bottomCell);
                return true;
            }
            final TextDetailSettingsCell uploadFrontCell = this.uploadFrontCell;
            if (uploadFrontCell != null) {
                if (this.frontDocument == null) {
                    this.onFieldError((View)uploadFrontCell);
                    return true;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("front");
                sb2.append(this.getDocumentHash(this.frontDocument));
                if (this.errorsValues.containsKey(sb2.toString())) {
                    this.onFieldError((View)this.documentsCells.get(this.frontDocument));
                    return true;
                }
            }
            final TLRPC.SecureValueType type = this.currentDocumentsType.type;
            if (type instanceof TLRPC.TL_secureValueTypeIdentityCard || type instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                final TextDetailSettingsCell uploadReverseCell = this.uploadReverseCell;
                if (uploadReverseCell != null) {
                    if (this.reverseDocument == null) {
                        this.onFieldError((View)uploadReverseCell);
                        return true;
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("reverse");
                    sb3.append(this.getDocumentHash(this.reverseDocument));
                    if (this.errorsValues.containsKey(sb3.toString())) {
                        this.onFieldError((View)this.documentsCells.get(this.reverseDocument));
                        return true;
                    }
                }
            }
            final TextDetailSettingsCell uploadSelfieCell = this.uploadSelfieCell;
            if (uploadSelfieCell != null && this.currentBotId != 0) {
                if (this.selfieDocument == null) {
                    this.onFieldError((View)uploadSelfieCell);
                    return true;
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("selfie");
                sb4.append(this.getDocumentHash(this.selfieDocument));
                if (this.errorsValues.containsKey(sb4.toString())) {
                    this.onFieldError((View)this.documentsCells.get(this.selfieDocument));
                    return true;
                }
            }
            if (this.uploadTranslationCell != null && this.currentBotId != 0) {
                if (this.translationDocuments.isEmpty()) {
                    this.onFieldError((View)this.uploadTranslationCell);
                    return true;
                }
                for (int size2 = this.translationDocuments.size(), j = 0; j < size2; ++j) {
                    final SecureDocument key2 = this.translationDocuments.get(j);
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("translation");
                    sb5.append(this.getDocumentHash(key2));
                    final String string2 = sb5.toString();
                    if (string2 != null && this.errorsValues.containsKey(string2)) {
                        this.onFieldError((View)this.documentsCells.get(key2));
                        return true;
                    }
                }
            }
        }
        for (int k = 0; k < 2; ++k) {
            EditTextBoldCursor[] array;
            if (k == 0) {
                array = this.inputFields;
            }
            else {
                final TextInfoPrivacyCell nativeInfoCell = this.nativeInfoCell;
                if (nativeInfoCell != null && nativeInfoCell.getVisibility() == 0) {
                    array = this.inputExtraFields;
                }
                else {
                    array = null;
                }
            }
            if (array != null) {
                for (int l = 0; l < array.length; ++l) {
                    int hasErrorText;
                    final int n = hasErrorText = (array[l].hasErrorText() ? 1 : 0);
                    if (!this.errorsValues.isEmpty()) {
                        final TLRPC.SecureValueType type2 = this.currentType.type;
                        String key3 = null;
                        Label_0984: {
                            Label_0982: {
                                if (type2 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                    if (k == 0) {
                                        switch (l) {
                                            default: {
                                                break Label_0982;
                                            }
                                            case 8: {
                                                key3 = "expiry_date";
                                                break Label_0984;
                                            }
                                            case 7: {
                                                key3 = "document_no";
                                                break Label_0984;
                                            }
                                            case 6: {
                                                key3 = "residence_country_code";
                                                break Label_0984;
                                            }
                                            case 4: {
                                                key3 = "gender";
                                                break Label_0984;
                                            }
                                            case 3: {
                                                key3 = "birth_date";
                                                break Label_0984;
                                            }
                                            case 2: {
                                                key3 = "last_name";
                                                break Label_0984;
                                            }
                                            case 1: {
                                                key3 = "middle_name";
                                                break Label_0984;
                                            }
                                            case 0: {
                                                key3 = "first_name";
                                                break Label_0984;
                                            }
                                            case 5: {
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        if (l == 0) {
                                            key3 = "first_name_native";
                                            break Label_0984;
                                        }
                                        if (l == 1) {
                                            key3 = "middle_name_native";
                                            break Label_0984;
                                        }
                                        if (l != 2) {
                                            break Label_0982;
                                        }
                                        key3 = "last_name_native";
                                        break Label_0984;
                                    }
                                }
                                else {
                                    if (!(type2 instanceof TLRPC.TL_secureValueTypeAddress)) {
                                        break Label_0982;
                                    }
                                    if (l == 0) {
                                        key3 = "street_line1";
                                        break Label_0984;
                                    }
                                    if (l == 1) {
                                        key3 = "street_line2";
                                        break Label_0984;
                                    }
                                    if (l == 2) {
                                        key3 = "post_code";
                                        break Label_0984;
                                    }
                                    if (l == 3) {
                                        key3 = "city";
                                        break Label_0984;
                                    }
                                    if (l == 4) {
                                        key3 = "state";
                                        break Label_0984;
                                    }
                                    if (l != 5) {
                                        break Label_0982;
                                    }
                                }
                                key3 = "country_code";
                                break Label_0984;
                            }
                            key3 = null;
                        }
                        hasErrorText = n;
                        if (key3 != null) {
                            final String s = this.errorsValues.get(key3);
                            hasErrorText = n;
                            if (!TextUtils.isEmpty((CharSequence)s)) {
                                hasErrorText = n;
                                if (s.equals(array[l].getText().toString())) {
                                    hasErrorText = 1;
                                }
                            }
                        }
                    }
                    if (!this.documentOnly || this.currentDocumentsType == null || l >= 7) {
                        int n2;
                        if ((n2 = hasErrorText) == 0) {
                            final int length = array[l].length();
                            final int currentActivityType = this.currentActivityType;
                            boolean b = false;
                            Label_1312: {
                                int n3 = 0;
                                Label_1305: {
                                    if (currentActivityType == 1) {
                                        if (l == 8) {
                                            continue;
                                        }
                                        if ((k == 0 && (l == 0 || l == 2 || l == 1)) || (k == 1 && (l == 0 || l == 1 || l == 2))) {
                                            if (length > 255) {
                                                hasErrorText = 1;
                                            }
                                            if (k != 0 || l != 1) {
                                                n3 = hasErrorText;
                                                if (k != 1) {
                                                    break Label_1305;
                                                }
                                                n3 = hasErrorText;
                                                if (l != 1) {
                                                    break Label_1305;
                                                }
                                            }
                                            b = true;
                                            break Label_1312;
                                        }
                                        n3 = hasErrorText;
                                        if (l != 7) {
                                            break Label_1305;
                                        }
                                        n3 = hasErrorText;
                                        if (length <= 24) {
                                            break Label_1305;
                                        }
                                    }
                                    else {
                                        n3 = hasErrorText;
                                        if (currentActivityType != 2) {
                                            break Label_1305;
                                        }
                                        if (l == 1) {
                                            continue;
                                        }
                                        if (l == 3) {
                                            n3 = hasErrorText;
                                            if (length >= 2) {
                                                break Label_1305;
                                            }
                                        }
                                        else if (l == 4) {
                                            if (!"US".equals(this.currentCitizeship)) {
                                                continue;
                                            }
                                            n3 = hasErrorText;
                                            if (length >= 2) {
                                                break Label_1305;
                                            }
                                        }
                                        else {
                                            n3 = hasErrorText;
                                            if (l != 2) {
                                                break Label_1305;
                                            }
                                            if (length >= 2) {
                                                n3 = hasErrorText;
                                                if (length <= 10) {
                                                    break Label_1305;
                                                }
                                            }
                                        }
                                    }
                                    n3 = 1;
                                }
                                b = false;
                                hasErrorText = n3;
                            }
                            n2 = hasErrorText;
                            if (hasErrorText == 0) {
                                n2 = hasErrorText;
                                if (!b) {
                                    n2 = hasErrorText;
                                    if (length == 0) {
                                        n2 = 1;
                                    }
                                }
                            }
                        }
                        if (n2 != 0) {
                            this.onFieldError((View)array[l]);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void checkNativeFields(final boolean b) {
        if (this.inputExtraFields == null) {
            return;
        }
        final String s = this.languageMap.get(this.currentResidence);
        final String s2 = SharedConfig.getCountryLangs().get(this.currentResidence);
        final boolean native_names = this.currentType.native_names;
        int n = 0;
        if (native_names && !TextUtils.isEmpty((CharSequence)this.currentResidence) && !"EN".equals(s2)) {
            if (this.nativeInfoCell.getVisibility() != 0) {
                this.nativeInfoCell.setVisibility(0);
                this.headerCell.setVisibility(0);
                this.extraBackgroundView2.setVisibility(0);
                int n2 = 0;
                EditTextBoldCursor[] inputExtraFields;
                while (true) {
                    inputExtraFields = this.inputExtraFields;
                    if (n2 >= inputExtraFields.length) {
                        break;
                    }
                    ((View)inputExtraFields[n2].getParent()).setVisibility(0);
                    ++n2;
                }
                if (inputExtraFields[0].length() == 0 && this.inputExtraFields[1].length() == 0 && this.inputExtraFields[2].length() == 0) {
                    int n3 = 0;
                    while (true) {
                        final boolean[] nonLatinNames = this.nonLatinNames;
                        if (n3 >= nonLatinNames.length) {
                            break;
                        }
                        if (nonLatinNames[n3]) {
                            this.inputExtraFields[0].setText((CharSequence)this.inputFields[0].getText());
                            this.inputExtraFields[1].setText((CharSequence)this.inputFields[1].getText());
                            this.inputExtraFields[2].setText((CharSequence)this.inputFields[2].getText());
                            break;
                        }
                        ++n3;
                    }
                }
                this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable((Context)this.getParentActivity(), 2131165394, "windowBackgroundGrayShadow"));
            }
            this.nativeInfoCell.setText(LocaleController.formatString("PassportNativeInfo", 2131560294, s));
            String serverString;
            if (s2 != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("PassportLanguage_");
                sb.append(s2);
                serverString = LocaleController.getServerString(sb.toString());
            }
            else {
                serverString = null;
            }
            if (serverString != null) {
                this.headerCell.setText(LocaleController.formatString("PassportNativeHeaderLang", 2131560293, serverString));
            }
            else {
                this.headerCell.setText(LocaleController.getString("PassportNativeHeader", 2131560292));
            }
            for (int i = 0; i < 3; ++i) {
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            if (serverString != null) {
                                this.inputExtraFields[i].setHintText(LocaleController.getString("PassportSurname", 2131560334));
                            }
                            else {
                                this.inputExtraFields[i].setHintText(LocaleController.formatString("PassportSurnameCountry", 2131560335, s));
                            }
                        }
                    }
                    else if (serverString != null) {
                        this.inputExtraFields[i].setHintText(LocaleController.getString("PassportMidname", 2131560285));
                    }
                    else {
                        this.inputExtraFields[i].setHintText(LocaleController.formatString("PassportMidnameCountry", 2131560286, s));
                    }
                }
                else if (serverString != null) {
                    this.inputExtraFields[i].setHintText(LocaleController.getString("PassportName", 2131560288));
                }
                else {
                    this.inputExtraFields[i].setHintText(LocaleController.formatString("PassportNameCountry", 2131560290, s));
                }
            }
            if (b) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$HspRKy4cKNBUybT_ED_XvHWEg3g(this));
            }
        }
        else if (this.nativeInfoCell.getVisibility() != 8) {
            this.nativeInfoCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.extraBackgroundView2.setVisibility(8);
            while (true) {
                final EditTextBoldCursor[] inputExtraFields2 = this.inputExtraFields;
                if (n >= inputExtraFields2.length) {
                    break;
                }
                ((View)inputExtraFields2[n].getParent()).setVisibility(8);
                ++n;
            }
            if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
                this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable((Context)this.getParentActivity(), 2131165394, "windowBackgroundGrayShadow"));
            }
            else {
                this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable((Context)this.getParentActivity(), 2131165395, "windowBackgroundGrayShadow"));
            }
        }
    }
    
    public static boolean checkSecret(final byte[] array, final Long n) {
        if (array != null && array.length == 32) {
            int i = 0;
            int n2 = 0;
            while (i < array.length) {
                n2 += (array[i] & 0xFF);
                ++i;
            }
            return n2 % 255 == 239 && (n == null || Utilities.bytesToLong(Utilities.computeSHA256(array)) == n);
        }
        return false;
    }
    
    private void checkTopErrorCell(final boolean b) {
        if (this.topErrorCell == null) {
            return;
        }
        Object o2;
        final Object o = o2 = null;
        Label_0092: {
            if (this.fieldsErrors != null) {
                if (!b) {
                    o2 = o;
                    if (!this.errorsValues.containsKey("error_all")) {
                        break Label_0092;
                    }
                }
                final String s = this.fieldsErrors.get("error_all");
                o2 = o;
                if (s != null) {
                    final SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder)(o2 = new SpannableStringBuilder((CharSequence)s));
                    if (b) {
                        this.errorsValues.put("error_all", "");
                        o2 = spannableStringBuilder;
                    }
                }
            }
        }
        Object text = o2;
        Label_0194: {
            if (this.documentsErrors != null) {
                if (!b) {
                    text = o2;
                    if (!this.errorsValues.containsKey("error_document_all")) {
                        break Label_0194;
                    }
                }
                final String s2 = this.documentsErrors.get("error_all");
                text = o2;
                if (s2 != null) {
                    if (o2 == null) {
                        o2 = new SpannableStringBuilder((CharSequence)s2);
                    }
                    else {
                        ((SpannableStringBuilder)o2).append((CharSequence)"\n\n").append((CharSequence)s2);
                    }
                    text = o2;
                    if (b) {
                        this.errorsValues.put("error_document_all", "");
                        text = o2;
                    }
                }
            }
        }
        if (text != null) {
            ((SpannableStringBuilder)text).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, ((SpannableStringBuilder)text).length(), 33);
            this.topErrorCell.setText((CharSequence)text);
            this.topErrorCell.setVisibility(0);
        }
        else if (this.topErrorCell.getVisibility() != 8) {
            this.topErrorCell.setVisibility(8);
        }
    }
    
    private void createAddressInterface(final Context context) {
        this.languageMap = new HashMap<String, String>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                final String[] split = line.split(";");
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        (this.topErrorCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165396, "windowBackgroundGrayShadow"));
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.linearLayout2.addView((View)this.topErrorCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.checkTopErrorCell(true);
        final TLRPC.TL_secureRequiredType currentDocumentsType = this.currentDocumentsType;
        if (currentDocumentsType != null) {
            final TLRPC.SecureValueType type = currentDocumentsType.type;
            if (type instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
            }
            else if (type instanceof TLRPC.TL_secureValueTypeBankStatement) {
                super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
            }
            else if (type instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
            }
            else if (type instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
            }
            else if (type instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
            }
            (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportDocuments", 2131560216));
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.documentsLayout = new LinearLayout(context)).setOrientation(1);
            this.linearLayout2.addView((View)this.documentsLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.uploadDocumentCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView((View)this.uploadDocumentCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.uploadDocumentCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$iWoQRq4LDUs7_2TnB7mjc6JUANI(this));
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            if (this.currentBotId != 0) {
                this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAddressUploadInfo", 2131560164);
            }
            else {
                final TLRPC.SecureValueType type2 = this.currentDocumentsType.type;
                if (type2 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAgreementInfo", 2131560166);
                }
                else if (type2 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBillInfo", 2131560170);
                }
                else if (type2 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddPassportRegistrationInfo", 2131560180);
                }
                else if (type2 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131560182);
                }
                else if (type2 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                    this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBankInfo", 2131560168);
                }
                else {
                    this.noAllDocumentsErrorText = "";
                }
            }
            final CharSequence noAllDocumentsErrorText = this.noAllDocumentsErrorText;
            final HashMap<String, String> documentsErrors = this.documentsErrors;
            Object text = noAllDocumentsErrorText;
            if (documentsErrors != null) {
                final String s = documentsErrors.get("files_all");
                text = noAllDocumentsErrorText;
                if (s != null) {
                    text = new SpannableStringBuilder((CharSequence)s);
                    ((SpannableStringBuilder)text).append((CharSequence)"\n\n");
                    ((SpannableStringBuilder)text).append(this.noAllDocumentsErrorText);
                    ((SpannableStringBuilder)text).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, s.length(), 33);
                    this.errorsValues.put("files_all", "");
                }
            }
            this.bottomCell.setText((CharSequence)text);
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
                (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportTranslation", 2131560337));
                this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.translationLayout = new LinearLayout(context)).setOrientation(1);
                this.linearLayout2.addView((View)this.translationLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.uploadTranslationCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView((View)this.uploadTranslationCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.uploadTranslationCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$zxJkztkjm9AzPkgXb6NmXZ5sri0(this));
                (this.bottomCellTranslation = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
                if (this.currentBotId != 0) {
                    this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131560188);
                }
                else {
                    final TLRPC.SecureValueType type3 = this.currentDocumentsType.type;
                    if (type3 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationAgreementInfo", 2131560183);
                    }
                    else if (type3 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBillInfo", 2131560185);
                    }
                    else if (type3 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationPassportRegistrationInfo", 2131560186);
                    }
                    else if (type3 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationTemporaryRegistrationInfo", 2131560187);
                    }
                    else if (type3 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBankInfo", 2131560184);
                    }
                    else {
                        this.noAllTranslationErrorText = "";
                    }
                }
                final CharSequence noAllTranslationErrorText = this.noAllTranslationErrorText;
                final HashMap<String, String> documentsErrors2 = this.documentsErrors;
                Object text2 = noAllTranslationErrorText;
                if (documentsErrors2 != null) {
                    final String s2 = documentsErrors2.get("translation_all");
                    text2 = noAllTranslationErrorText;
                    if (s2 != null) {
                        text2 = new SpannableStringBuilder((CharSequence)s2);
                        ((SpannableStringBuilder)text2).append((CharSequence)"\n\n");
                        ((SpannableStringBuilder)text2).append(this.noAllTranslationErrorText);
                        ((SpannableStringBuilder)text2).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, s2.length(), 33);
                        this.errorsValues.put("translation_all", "");
                    }
                }
                this.bottomCellTranslation.setText((CharSequence)text2);
                this.linearLayout2.addView((View)this.bottomCellTranslation, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            }
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("PassportAddress", 2131560189));
        }
        (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportAddressHeader", 2131560190));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[6];
        for (int i = 0; i < 6; ++i) {
            final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.inputFields[i] = editTextBoldCursor;
            final FrameLayout frameLayout = new FrameLayout(context) {
                private StaticLayout errorLayout;
                float offsetX;
                
                protected void onDraw(final Canvas canvas) {
                    if (this.errorLayout != null) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor.getLineY() + AndroidUtilities.dp(3.0f));
                        this.errorLayout.draw(canvas);
                        canvas.restore();
                    }
                }
                
                protected void onMeasure(final int n, int measureSpec) {
                    final int n2 = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(34.0f);
                    this.errorLayout = editTextBoldCursor.getErrorLayout(n2);
                    final StaticLayout errorLayout = this.errorLayout;
                    int n3 = measureSpec;
                    if (errorLayout != null) {
                        final int lineCount = errorLayout.getLineCount();
                        int n4 = 0;
                        if (lineCount > 1) {
                            measureSpec = View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                        }
                        n3 = measureSpec;
                        if (LocaleController.isRTL) {
                            float max = 0.0f;
                            while (true) {
                                n3 = measureSpec;
                                if (n4 >= lineCount) {
                                    break;
                                }
                                if (this.errorLayout.getLineLeft(n4) != 0.0f) {
                                    this.offsetX = 0.0f;
                                    n3 = measureSpec;
                                    break;
                                }
                                max = Math.max(max, this.errorLayout.getLineWidth(n4));
                                if (n4 == lineCount - 1) {
                                    this.offsetX = n2 - max;
                                }
                                ++n4;
                            }
                        }
                    }
                    super.onMeasure(n, n3);
                }
            };
            ((ViewGroup)frameLayout).setWillNotDraw(false);
            this.linearLayout2.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            ((ViewGroup)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            int n = 5;
            if (i == 5) {
                (this.extraBackgroundView = new View(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.extraBackgroundView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 6));
            }
            if (this.documentOnly && this.currentDocumentsType != null) {
                ((ViewGroup)frameLayout).setVisibility(8);
                final View extraBackgroundView = this.extraBackgroundView;
                if (extraBackgroundView != null) {
                    extraBackgroundView.setVisibility(8);
                }
            }
            this.inputFields[i].setTag((Object)i);
            this.inputFields[i].setSupportRtlHint(true);
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.inputFields[i].setTransformHintToHeader(true);
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable((Drawable)null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            this.inputFields[i].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            if (i == 5) {
                this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PassportActivity$VjopYxs0FTEpLRu0AyOT8rb_e68(this));
                this.inputFields[i].setInputType(0);
                this.inputFields[i].setFocusable(false);
            }
            else {
                this.inputFields[i].setInputType(16385);
                this.inputFields[i].setImeOptions(268435461);
            }
            String s3;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i != 4) {
                                if (i != 5) {
                                    continue;
                                }
                                this.inputFields[i].setHintText(LocaleController.getString("PassportCountry", 2131560198));
                                s3 = "country_code";
                            }
                            else {
                                this.inputFields[i].setHintText(LocaleController.getString("PassportState", 2131560331));
                                s3 = "state";
                            }
                        }
                        else {
                            this.inputFields[i].setHintText(LocaleController.getString("PassportCity", 2131560196));
                            s3 = "city";
                        }
                    }
                    else {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportPostcode", 2131560312));
                        s3 = "post_code";
                    }
                }
                else {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportStreet2", 2131560333));
                    s3 = "street_line2";
                }
            }
            else {
                this.inputFields[i].setHintText(LocaleController.getString("PassportStreet1", 2131560332));
                s3 = "street_line1";
            }
            this.setFieldValues(this.currentValues, this.inputFields[i], s3);
            if (i == 2) {
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    private boolean ignore;
                    
                    public void afterTextChanged(final Editable editable) {
                        if (this.ignore) {
                            return;
                        }
                        final int n = 1;
                        this.ignore = true;
                        while (true) {
                            for (int i = 0; i < editable.length(); ++i) {
                                final char char1 = editable.charAt(i);
                                if ((char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && (char1 < '0' || char1 > '9') && char1 != '-' && char1 != ' ') {
                                    final int n2 = n;
                                    this.ignore = false;
                                    if (n2 != 0) {
                                        editTextBoldCursor.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131560343));
                                    }
                                    else {
                                        PassportActivity.this.checkFieldForError(editTextBoldCursor, s3, editable, false);
                                    }
                                    return;
                                }
                            }
                            final int n2 = 0;
                            continue;
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
                this.inputFields[i].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(10) });
            }
            else {
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        PassportActivity.this.checkFieldForError(editTextBoldCursor, s3, editable, false);
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[i].setSelection(inputFields[i].length());
            this.inputFields[i].setPadding(0, 0, 0, 0);
            final EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i];
            if (!LocaleController.isRTL) {
                n = 3;
            }
            editTextBoldCursor2.setGravity(n | 0x10);
            ((ViewGroup)frameLayout).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 64.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
            this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$AoVUW01QTsr_TGtxsPo_viSuIG8(this));
        }
        this.sectionCell = new ShadowSectionCell(context);
        this.linearLayout2.addView((View)this.sectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        if (this.documentOnly && this.currentDocumentsType != null) {
            this.headerCell.setVisibility(8);
            this.sectionCell.setVisibility(8);
        }
        if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
            final TLRPC.TL_secureValue currentDocumentsTypeValue = this.currentDocumentsTypeValue;
            if (currentDocumentsTypeValue != null) {
                this.addDocumentViews(currentDocumentsTypeValue.files);
                this.addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
            }
            this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            final TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            if (this.currentDocumentsType == null) {
                textSettingsCell.setText(LocaleController.getString("PassportDeleteInfo", 2131560205), false);
            }
            else {
                textSettingsCell.setText(LocaleController.getString("PassportDeleteDocument", 2131560200), false);
            }
            this.linearLayout2.addView((View)textSettingsCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            textSettingsCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$OrTOOfgxc6UhCIwbWsAhFPEaa80(this));
            (this.sectionCell = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout2.addView((View)this.sectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        else {
            this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            if (this.documentOnly && this.currentDocumentsType != null) {
                this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            }
        }
        this.updateUploadText(0);
        this.updateUploadText(4);
    }
    
    private void createChatAttachView() {
        if (this.getParentActivity() == null) {
            return;
        }
        if (this.chatAttachAlert == null) {
            (this.chatAttachAlert = new ChatAttachAlert((Context)this.getParentActivity(), this)).setDelegate((ChatAttachAlert.ChatAttachViewDelegate)new ChatAttachAlert.ChatAttachViewDelegate() {
                @Override
                public boolean allowGroupPhotos() {
                    return false;
                }
                
                @Override
                public void didPressedButton(int i) {
                    if (PassportActivity.this.getParentActivity() != null) {
                        if (PassportActivity.this.chatAttachAlert != null) {
                            if (i != 8 && i != 7) {
                                if (PassportActivity.this.chatAttachAlert != null) {
                                    PassportActivity.this.chatAttachAlert.dismissWithButtonClick(i);
                                }
                                PassportActivity.this.processSelectedAttach(i);
                                return;
                            }
                            if (i != 8) {
                                PassportActivity.this.chatAttachAlert.dismiss();
                            }
                            final HashMap<Object, Object> selectedPhotos = PassportActivity.this.chatAttachAlert.getSelectedPhotos();
                            final ArrayList<Object> selectedPhotosOrder = PassportActivity.this.chatAttachAlert.getSelectedPhotosOrder();
                            if (!selectedPhotos.isEmpty()) {
                                final ArrayList<SendMessagesHelper.SendingMediaInfo> list = new ArrayList<SendMessagesHelper.SendingMediaInfo>();
                                MediaController.PhotoEntry photoEntry;
                                SendMessagesHelper.SendingMediaInfo e;
                                String imagePath;
                                String path;
                                for (i = 0; i < selectedPhotosOrder.size(); ++i) {
                                    photoEntry = (MediaController.PhotoEntry)selectedPhotos.get(selectedPhotosOrder.get(i));
                                    e = new SendMessagesHelper.SendingMediaInfo();
                                    imagePath = photoEntry.imagePath;
                                    if (imagePath != null) {
                                        e.path = imagePath;
                                    }
                                    else {
                                        path = photoEntry.path;
                                        if (path != null) {
                                            e.path = path;
                                        }
                                    }
                                    list.add(e);
                                    photoEntry.reset();
                                }
                                PassportActivity.this.processSelectedFiles(list);
                            }
                        }
                    }
                }
                
                @Override
                public void didSelectBot(final TLRPC.User user) {
                }
                
                @Override
                public View getRevealView() {
                    return null;
                }
                
                @Override
                public void onCameraOpened() {
                    AndroidUtilities.hideKeyboard(PassportActivity.this.fragmentView.findFocus());
                }
            });
        }
    }
    
    private void createDocumentDeleteAlert() {
        final boolean[] array = { true };
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PassportActivity$izrGH6tzz_c5ZefftonTyrpURyU(this, array));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        if (this.documentOnly && this.currentDocumentsType == null && this.currentType.type instanceof TLRPC.TL_secureValueTypeAddress) {
            builder.setMessage(LocaleController.getString("PassportDeleteAddressAlert", 2131560199));
        }
        else if (this.documentOnly && this.currentDocumentsType == null && this.currentType.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            builder.setMessage(LocaleController.getString("PassportDeletePersonalAlert", 2131560206));
        }
        else {
            builder.setMessage(LocaleController.getString("PassportDeleteDocumentAlert", 2131560202));
        }
        if (!this.documentOnly && this.currentDocumentsType != null) {
            final FrameLayout view = new FrameLayout((Context)this.getParentActivity());
            final CheckBoxCell checkBoxCell = new CheckBoxCell((Context)this.getParentActivity(), 1);
            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            final TLRPC.SecureValueType type = this.currentType.type;
            if (type instanceof TLRPC.TL_secureValueTypeAddress) {
                checkBoxCell.setText(LocaleController.getString("PassportDeleteDocumentAddress", 2131560201), "", true, false);
            }
            else if (type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                checkBoxCell.setText(LocaleController.getString("PassportDeleteDocumentPersonal", 2131560203), "", true, false);
            }
            int n;
            if (LocaleController.isRTL) {
                n = AndroidUtilities.dp(16.0f);
            }
            else {
                n = AndroidUtilities.dp(8.0f);
            }
            int n2;
            if (LocaleController.isRTL) {
                n2 = AndroidUtilities.dp(8.0f);
            }
            else {
                n2 = AndroidUtilities.dp(16.0f);
            }
            checkBoxCell.setPadding(n, 0, n2, 0);
            view.addView((View)checkBoxCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 51));
            checkBoxCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$nDbjMA2U409g1anoYGcsOusiteI(array));
            builder.setView((View)view);
        }
        this.showDialog(builder.create());
    }
    
    private void createEmailInterface(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131560217));
        if (!TextUtils.isEmpty((CharSequence)this.currentEmail)) {
            final TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", 2131560308, this.currentEmail), false);
            this.linearLayout2.addView((View)textSettingsCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            textSettingsCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$B8yMOg0egHKBmo8qqI_zP3hWZKI(this));
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameEmailInfo", 2131560309));
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        this.inputFields = new EditTextBoldCursor[1];
        for (int i = 0; i < 1; ++i) {
            final FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout2.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
            ((ViewGroup)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            (this.inputFields[i] = new EditTextBoldCursor(context)).setTag((Object)i);
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable((Drawable)null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            this.inputFields[i].setInputType(33);
            this.inputFields[i].setImeOptions(268435462);
            if (i == 0) {
                this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingEmailPlaceholder", 2131560394));
                final TLRPC.TL_secureValue currentTypeValue = this.currentTypeValue;
                if (currentTypeValue != null) {
                    final TLRPC.SecurePlainData plain_data = currentTypeValue.plain_data;
                    if (plain_data instanceof TLRPC.TL_securePlainEmail) {
                        final TLRPC.TL_securePlainEmail tl_securePlainEmail = (TLRPC.TL_securePlainEmail)plain_data;
                        if (!TextUtils.isEmpty((CharSequence)tl_securePlainEmail.email)) {
                            this.inputFields[i].setText((CharSequence)tl_securePlainEmail.email);
                        }
                    }
                }
            }
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[i].setSelection(inputFields[i].length());
            this.inputFields[i].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            final EditTextBoldCursor editTextBoldCursor = this.inputFields[i];
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            editTextBoldCursor.setGravity(gravity);
            ((ViewGroup)frameLayout).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$lpvHitH1qNbCguTGgQkc2rG8PIQ(this));
        }
        (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportEmailUploadInfo", 2131560220));
        this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
    }
    
    private void createEmailVerificationInterface(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131560217));
        this.inputFields = new EditTextBoldCursor[1];
        for (int i = 0; i < 1; ++i) {
            final FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout2.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
            ((ViewGroup)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            (this.inputFields[i] = new EditTextBoldCursor(context)).setTag((Object)i);
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable((Drawable)null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            final EditTextBoldCursor editTextBoldCursor = this.inputFields[i];
            int gravity = 3;
            editTextBoldCursor.setInputType(3);
            this.inputFields[i].setImeOptions(268435462);
            if (i == 0) {
                this.inputFields[i].setHint((CharSequence)LocaleController.getString("PassportEmailCode", 2131560218));
            }
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[i].setSelection(inputFields[i].length());
            this.inputFields[i].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            final EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i];
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            editTextBoldCursor2.setGravity(gravity);
            ((ViewGroup)frameLayout).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$z1QQrtghbex8oLf43ona_95yF_0(this));
            this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    if (PassportActivity.this.ignoreOnTextChange) {
                        return;
                    }
                    if (PassportActivity.this.emailCodeLength != 0 && PassportActivity.this.inputFields[0].length() == PassportActivity.this.emailCodeLength) {
                        PassportActivity.this.doneItem.callOnClick();
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
        }
        (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.formatString("PassportEmailVerifyInfo", 2131560221, this.currentValues.get("email")));
        this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
    }
    
    private void createIdentityInterface(final Context context) {
        this.languageMap = new HashMap<String, String>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                final String[] split = line.split(";");
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        (this.topErrorCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165396, "windowBackgroundGrayShadow"));
        this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.linearLayout2.addView((View)this.topErrorCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.checkTopErrorCell(true);
        if (this.currentDocumentsType != null) {
            this.headerCell = new HeaderCell(context);
            if (this.documentOnly) {
                this.headerCell.setText(LocaleController.getString("PassportDocuments", 2131560216));
            }
            else {
                this.headerCell.setText(LocaleController.getString("PassportRequiredDocuments", 2131560317));
            }
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.frontLayout = new LinearLayout(context)).setOrientation(1);
            this.linearLayout2.addView((View)this.frontLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.uploadFrontCell = new TextDetailSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView((View)this.uploadFrontCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.uploadFrontCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$c9kJTe5mHBur0ZKYsiTqGUHyE0o(this));
            (this.reverseLayout = new LinearLayout(context)).setOrientation(1);
            this.linearLayout2.addView((View)this.reverseLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            final boolean selfie_required = this.currentDocumentsType.selfie_required;
            (this.uploadReverseCell = new TextDetailSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.uploadReverseCell.setTextAndValue(LocaleController.getString("PassportReverseSide", 2131560320), LocaleController.getString("PassportReverseSideInfo", 2131560321), selfie_required);
            this.linearLayout2.addView((View)this.uploadReverseCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.uploadReverseCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$jVyNHBa_n1RAjexJFiTwuhwl3wk(this));
            if (this.currentDocumentsType.selfie_required) {
                (this.selfieLayout = new LinearLayout(context)).setOrientation(1);
                this.linearLayout2.addView((View)this.selfieLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.uploadSelfieCell = new TextDetailSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.uploadSelfieCell.setTextAndValue(LocaleController.getString("PassportSelfie", 2131560329), LocaleController.getString("PassportSelfieInfo", 2131560330), this.currentType.translation_required);
                this.linearLayout2.addView((View)this.uploadSelfieCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.uploadSelfieCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$80pcXC9JPBzJ3vgi80hMNDT2KC4(this));
            }
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportPersonalUploadInfo", 2131560303));
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            if (this.currentDocumentsType.translation_required) {
                (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportTranslation", 2131560337));
                this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.translationLayout = new LinearLayout(context)).setOrientation(1);
                this.linearLayout2.addView((View)this.translationLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.uploadTranslationCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.linearLayout2.addView((View)this.uploadTranslationCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.uploadTranslationCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$ijAn79XSwEsHcuejC6hU9kEtOuA(this));
                (this.bottomCellTranslation = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
                if (this.currentBotId != 0) {
                    this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131560188);
                }
                else {
                    final TLRPC.SecureValueType type = this.currentDocumentsType.type;
                    if (type instanceof TLRPC.TL_secureValueTypePassport) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddPassportInfo", 2131560178);
                    }
                    else if (type instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddInternalPassportInfo", 2131560175);
                    }
                    else if (type instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddIdentityCardInfo", 2131560173);
                    }
                    else if (type instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                        this.noAllTranslationErrorText = LocaleController.getString("PassportAddDriverLicenceInfo", 2131560172);
                    }
                    else {
                        this.noAllTranslationErrorText = "";
                    }
                }
                final CharSequence noAllTranslationErrorText = this.noAllTranslationErrorText;
                final HashMap<String, String> documentsErrors = this.documentsErrors;
                Object text = noAllTranslationErrorText;
                if (documentsErrors != null) {
                    final String s = documentsErrors.get("translation_all");
                    text = noAllTranslationErrorText;
                    if (s != null) {
                        text = new SpannableStringBuilder((CharSequence)s);
                        ((SpannableStringBuilder)text).append((CharSequence)"\n\n");
                        ((SpannableStringBuilder)text).append(this.noAllTranslationErrorText);
                        ((SpannableStringBuilder)text).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, s.length(), 33);
                        this.errorsValues.put("translation_all", "");
                    }
                }
                this.bottomCellTranslation.setText((CharSequence)text);
                this.linearLayout2.addView((View)this.bottomCellTranslation, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            }
        }
        else if (Build$VERSION.SDK_INT >= 18) {
            (this.scanDocumentCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.scanDocumentCell.setText(LocaleController.getString("PassportScanPassport", 2131560322), false);
            this.linearLayout2.addView((View)this.scanDocumentCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.scanDocumentCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$76809PrGNfIFwFdK4EfyZ7l0wQM(this));
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            this.bottomCell.setText(LocaleController.getString("PassportScanPassportInfo", 2131560323));
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        this.headerCell = new HeaderCell(context);
        if (this.documentOnly) {
            this.headerCell.setText(LocaleController.getString("PassportDocument", 2131560214));
        }
        else {
            this.headerCell.setText(LocaleController.getString("PassportPersonal", 2131560300));
        }
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        int n;
        if (this.currentDocumentsType != null) {
            n = 9;
        }
        else {
            n = 7;
        }
        this.inputFields = new EditTextBoldCursor[n];
        for (int i = 0; i < n; ++i) {
            final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.inputFields[i] = editTextBoldCursor;
            final FrameLayout frameLayout = new FrameLayout(context) {
                private StaticLayout errorLayout;
                private float offsetX;
                
                protected void onDraw(final Canvas canvas) {
                    if (this.errorLayout != null) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor.getLineY() + AndroidUtilities.dp(3.0f));
                        this.errorLayout.draw(canvas);
                        canvas.restore();
                    }
                }
                
                protected void onMeasure(final int n, int measureSpec) {
                    final int n2 = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(34.0f);
                    this.errorLayout = editTextBoldCursor.getErrorLayout(n2);
                    final StaticLayout errorLayout = this.errorLayout;
                    int n3 = measureSpec;
                    if (errorLayout != null) {
                        final int lineCount = errorLayout.getLineCount();
                        int n4 = 0;
                        if (lineCount > 1) {
                            measureSpec = View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                        }
                        n3 = measureSpec;
                        if (LocaleController.isRTL) {
                            float max = 0.0f;
                            while (true) {
                                n3 = measureSpec;
                                if (n4 >= lineCount) {
                                    break;
                                }
                                if (this.errorLayout.getLineLeft(n4) != 0.0f) {
                                    this.offsetX = 0.0f;
                                    n3 = measureSpec;
                                    break;
                                }
                                max = Math.max(max, this.errorLayout.getLineWidth(n4));
                                if (n4 == lineCount - 1) {
                                    this.offsetX = n2 - max;
                                }
                                ++n4;
                            }
                        }
                    }
                    super.onMeasure(n, n3);
                }
            };
            ((ViewGroup)frameLayout).setWillNotDraw(false);
            this.linearLayout2.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 64));
            ((ViewGroup)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            if (i == n - 1) {
                (this.extraBackgroundView = new View(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.extraBackgroundView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 6));
            }
            if (this.documentOnly && this.currentDocumentsType != null && i < 7) {
                ((ViewGroup)frameLayout).setVisibility(8);
                final View extraBackgroundView = this.extraBackgroundView;
                if (extraBackgroundView != null) {
                    extraBackgroundView.setVisibility(8);
                }
            }
            this.inputFields[i].setTag((Object)i);
            this.inputFields[i].setSupportRtlHint(true);
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.inputFields[i].setTransformHintToHeader(true);
            this.inputFields[i].setBackgroundDrawable((Drawable)null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            this.inputFields[i].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            if (i != 5 && i != 6) {
                if (i != 3 && i != 8) {
                    if (i == 4) {
                        this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PassportActivity$_CPWpKIngk2P0GwmkCG7jNpTBgM(this));
                        this.inputFields[i].setInputType(0);
                        this.inputFields[i].setFocusable(false);
                    }
                    else {
                        this.inputFields[i].setInputType(16385);
                        this.inputFields[i].setImeOptions(268435461);
                    }
                }
                else {
                    this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PassportActivity$TcnlWpKn54098zIjNVHAKGv7mPQ(this, context));
                    this.inputFields[i].setInputType(0);
                    this.inputFields[i].setFocusable(false);
                }
            }
            else {
                this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PassportActivity$BkWsiGp0SACn1vJ9h8YBMYKkLVM(this));
                this.inputFields[i].setInputType(0);
            }
            HashMap<String, String> hashMap = null;
            String s2 = null;
            switch (i) {
                default: {
                    continue;
                }
                case 8: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportExpired", 2131560222));
                    hashMap = this.currentDocumentValues;
                    s2 = "expiry_date";
                    break;
                }
                case 7: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportDocumentNumber", 2131560215));
                    hashMap = this.currentDocumentValues;
                    s2 = "document_no";
                    break;
                }
                case 6: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportResidence", 2131560318));
                    hashMap = this.currentValues;
                    s2 = "residence_country_code";
                    break;
                }
                case 5: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportCitizenship", 2131560195));
                    hashMap = this.currentValues;
                    s2 = "country_code";
                    break;
                }
                case 4: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportGender", 2131560226));
                    hashMap = this.currentValues;
                    s2 = "gender";
                    break;
                }
                case 3: {
                    this.inputFields[i].setHintText(LocaleController.getString("PassportBirthdate", 2131560194));
                    hashMap = this.currentValues;
                    s2 = "birth_date";
                    break;
                }
                case 2: {
                    if (this.currentType.native_names) {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportSurnameLatin", 2131560336));
                    }
                    else {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportSurname", 2131560334));
                    }
                    hashMap = this.currentValues;
                    s2 = "last_name";
                    break;
                }
                case 1: {
                    if (this.currentType.native_names) {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportMidnameLatin", 2131560287));
                    }
                    else {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportMidname", 2131560285));
                    }
                    hashMap = this.currentValues;
                    s2 = "middle_name";
                    break;
                }
                case 0: {
                    if (this.currentType.native_names) {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportNameLatin", 2131560291));
                    }
                    else {
                        this.inputFields[i].setHintText(LocaleController.getString("PassportName", 2131560288));
                    }
                    hashMap = this.currentValues;
                    s2 = "first_name";
                    break;
                }
            }
            this.setFieldValues(hashMap, this.inputFields[i], s2);
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[i].setSelection(inputFields[i].length());
            if (i != 0 && i != 2 && i != 1) {
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        final PassportActivity this$0 = PassportActivity.this;
                        this$0.checkFieldForError(editTextBoldCursor, s2, editable, hashMap == this$0.currentDocumentValues);
                        final int intValue = (int)editTextBoldCursor.getTag();
                        final EditTextBoldCursor editTextBoldCursor = PassportActivity.this.inputFields[intValue];
                        if (intValue == 6) {
                            PassportActivity.this.checkNativeFields(true);
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            else {
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    private boolean ignore;
                    
                    public void afterTextChanged(final Editable editable) {
                        if (this.ignore) {
                            return;
                        }
                        final int intValue = (int)editTextBoldCursor.getTag();
                        while (true) {
                            for (int i = 0; i < editable.length(); ++i) {
                                final char char1 = editable.charAt(i);
                                if ((char1 < '0' || char1 > '9') && (char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && char1 != ' ' && char1 != '\'' && char1 != ',' && char1 != '.' && char1 != '&' && char1 != '-' && char1 != '/') {
                                    final boolean b = true;
                                    if (b && !PassportActivity.this.allowNonLatinName) {
                                        editTextBoldCursor.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131560343));
                                    }
                                    else {
                                        PassportActivity.this.nonLatinNames[intValue] = b;
                                        PassportActivity.this.checkFieldForError(editTextBoldCursor, s2, editable, false);
                                    }
                                    return;
                                }
                            }
                            final boolean b = false;
                            continue;
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            this.inputFields[i].setPadding(0, 0, 0, 0);
            final EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i];
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            editTextBoldCursor2.setGravity(n2 | 0x10);
            ((ViewGroup)frameLayout).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
            this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$IUKnReQZFqjMBZyTSuqCOeb27jI(this));
        }
        this.sectionCell2 = new ShadowSectionCell(context);
        this.linearLayout2.addView((View)this.sectionCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.headerCell = new HeaderCell(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.inputExtraFields = new EditTextBoldCursor[3];
        for (int j = 0; j < 3; ++j) {
            final EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
            this.inputExtraFields[j] = editTextBoldCursor3;
            final FrameLayout frameLayout2 = new FrameLayout(context) {
                private StaticLayout errorLayout;
                private float offsetX;
                
                protected void onDraw(final Canvas canvas) {
                    if (this.errorLayout != null) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(21.0f) + this.offsetX, editTextBoldCursor3.getLineY() + AndroidUtilities.dp(3.0f));
                        this.errorLayout.draw(canvas);
                        canvas.restore();
                    }
                }
                
                protected void onMeasure(final int n, int measureSpec) {
                    final int n2 = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(34.0f);
                    this.errorLayout = editTextBoldCursor3.getErrorLayout(n2);
                    final StaticLayout errorLayout = this.errorLayout;
                    int n3 = measureSpec;
                    if (errorLayout != null) {
                        final int lineCount = errorLayout.getLineCount();
                        int n4 = 0;
                        if (lineCount > 1) {
                            measureSpec = View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.errorLayout.getLineBottom(lineCount - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                        }
                        n3 = measureSpec;
                        if (LocaleController.isRTL) {
                            float max = 0.0f;
                            while (true) {
                                n3 = measureSpec;
                                if (n4 >= lineCount) {
                                    break;
                                }
                                if (this.errorLayout.getLineLeft(n4) != 0.0f) {
                                    this.offsetX = 0.0f;
                                    n3 = measureSpec;
                                    break;
                                }
                                max = Math.max(max, this.errorLayout.getLineWidth(n4));
                                if (n4 == lineCount - 1) {
                                    this.offsetX = n2 - max;
                                }
                                ++n4;
                            }
                        }
                    }
                    super.onMeasure(n, n3);
                }
            };
            ((ViewGroup)frameLayout2).setWillNotDraw(false);
            this.linearLayout2.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 64));
            ((ViewGroup)frameLayout2).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            if (j == 2) {
                (this.extraBackgroundView2 = new View(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.linearLayout2.addView(this.extraBackgroundView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 6));
            }
            this.inputExtraFields[j].setTag((Object)j);
            this.inputExtraFields[j].setSupportRtlHint(true);
            this.inputExtraFields[j].setTextSize(1, 16.0f);
            this.inputExtraFields[j].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputExtraFields[j].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputExtraFields[j].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            this.inputExtraFields[j].setTransformHintToHeader(true);
            this.inputExtraFields[j].setBackgroundDrawable((Drawable)null);
            this.inputExtraFields[j].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputExtraFields[j].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputExtraFields[j].setCursorWidth(1.5f);
            this.inputExtraFields[j].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
            this.inputExtraFields[j].setInputType(16385);
            this.inputExtraFields[j].setImeOptions(268435461);
            HashMap<String, String> hashMap2;
            String s3;
            if (j != 0) {
                if (j != 1) {
                    if (j != 2) {
                        continue;
                    }
                    hashMap2 = this.currentValues;
                    s3 = "last_name_native";
                }
                else {
                    hashMap2 = this.currentValues;
                    s3 = "middle_name_native";
                }
            }
            else {
                hashMap2 = this.currentValues;
                s3 = "first_name_native";
            }
            this.setFieldValues(hashMap2, this.inputExtraFields[j], s3);
            final EditTextBoldCursor[] inputExtraFields = this.inputExtraFields;
            inputExtraFields[j].setSelection(inputExtraFields[j].length());
            if (j == 0 || j == 2 || j == 1) {
                this.inputExtraFields[j].addTextChangedListener((TextWatcher)new TextWatcher() {
                    private boolean ignore;
                    
                    public void afterTextChanged(final Editable editable) {
                        if (this.ignore) {
                            return;
                        }
                        PassportActivity.this.checkFieldForError(editTextBoldCursor3, s3, editable, false);
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            this.inputExtraFields[j].setPadding(0, 0, 0, 0);
            final EditTextBoldCursor editTextBoldCursor4 = this.inputExtraFields[j];
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            editTextBoldCursor4.setGravity(n3 | 0x10);
            ((ViewGroup)frameLayout2).addView((View)this.inputExtraFields[j], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 21.0f, 0.0f, 21.0f, 0.0f));
            this.inputExtraFields[j].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$2R_VniY9bMOiViEoeO5veQ3eJ7w(this));
        }
        this.nativeInfoCell = new TextInfoPrivacyCell(context);
        this.linearLayout2.addView((View)this.nativeInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        if (((this.currentBotId != 0 || this.currentDocumentsType == null) && this.currentTypeValue != null && !this.documentOnly) || this.currentDocumentsTypeValue != null) {
            final TLRPC.TL_secureValue currentDocumentsTypeValue = this.currentDocumentsTypeValue;
            if (currentDocumentsTypeValue != null) {
                this.addDocumentViews(currentDocumentsTypeValue.files);
                final TLRPC.SecureFile front_side = this.currentDocumentsTypeValue.front_side;
                if (front_side instanceof TLRPC.TL_secureFile) {
                    this.addDocumentViewInternal((TLRPC.TL_secureFile)front_side, 2);
                }
                final TLRPC.SecureFile reverse_side = this.currentDocumentsTypeValue.reverse_side;
                if (reverse_side instanceof TLRPC.TL_secureFile) {
                    this.addDocumentViewInternal((TLRPC.TL_secureFile)reverse_side, 3);
                }
                final TLRPC.SecureFile selfie = this.currentDocumentsTypeValue.selfie;
                if (selfie instanceof TLRPC.TL_secureFile) {
                    this.addDocumentViewInternal((TLRPC.TL_secureFile)selfie, 1);
                }
                this.addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
            }
            final TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
            textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            if (this.currentDocumentsType == null) {
                textSettingsCell.setText(LocaleController.getString("PassportDeleteInfo", 2131560205), false);
            }
            else {
                textSettingsCell.setText(LocaleController.getString("PassportDeleteDocument", 2131560200), false);
            }
            this.linearLayout2.addView((View)textSettingsCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            textSettingsCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$gpc1SNZjQfFOYB3jBSNPORk4F_s(this));
            this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            (this.sectionCell = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout2.addView((View)this.sectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        else {
            this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        }
        this.updateInterfaceStringsForDocumentType();
        this.checkNativeFields(false);
    }
    
    private void createManageInterface(final Context context) {
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
        super.actionBar.createMenu().addItem(1, 2131165786);
        (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportProvidedInformation", 2131560313));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.sectionCell = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
        this.linearLayout2.addView((View)this.sectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.addDocumentCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.addDocumentCell.setText(LocaleController.getString("PassportNoDocumentsAdd", 2131560296), true);
        this.linearLayout2.addView((View)this.addDocumentCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.addDocumentCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$yv3E0a1LzZcIvpbSmJY3xiJFxqI(this));
        (this.deletePassportCell = new TextSettingsCell(context)).setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
        this.deletePassportCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.deletePassportCell.setText(LocaleController.getString("TelegramPassportDelete", 2131560875), false);
        this.linearLayout2.addView((View)this.deletePassportCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.deletePassportCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$CCLsFyBhbKH_We0aA4khftAzZjg(this));
        (this.addDocumentSectionCell = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.linearLayout2.addView((View)this.addDocumentSectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.emptyLayout = new LinearLayout(context)).setOrientation(1);
        this.emptyLayout.setGravity(17);
        this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        if (AndroidUtilities.isTablet()) {
            this.linearLayout2.addView((View)this.emptyLayout, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, AndroidUtilities.dp(528.0f) - ActionBar.getCurrentActionBarHeight()));
        }
        else {
            this.linearLayout2.addView((View)this.emptyLayout, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()));
        }
        (this.emptyImageView = new ImageView(context)).setImageResource(2131165692);
        this.emptyImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("sessions_devicesImage"), PorterDuff$Mode.MULTIPLY));
        this.emptyLayout.addView((View)this.emptyImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        (this.emptyTextView1 = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.emptyTextView1.setGravity(17);
        this.emptyTextView1.setTextSize(1, 15.0f);
        this.emptyTextView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTextView1.setText((CharSequence)LocaleController.getString("PassportNoDocuments", 2131560295));
        this.emptyLayout.addView((View)this.emptyTextView1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
        (this.emptyTextView2 = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.emptyTextView2.setGravity(17);
        this.emptyTextView2.setTextSize(1, 14.0f);
        this.emptyTextView2.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.emptyTextView2.setText((CharSequence)LocaleController.getString("PassportNoDocumentsInfo", 2131560297));
        this.emptyLayout.addView((View)this.emptyTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
        (this.emptyTextView3 = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setTextSize(1, 15.0f);
        this.emptyTextView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTextView3.setGravity(17);
        this.emptyTextView3.setText((CharSequence)LocaleController.getString("PassportNoDocumentsAdd", 2131560296).toUpperCase());
        this.emptyLayout.addView((View)this.emptyTextView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 30, 17, 0, 16, 0, 0));
        this.emptyTextView3.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$7cIXxuOb_yUfSpLF2ezyFpul_wg(this));
        for (int size = this.currentForm.values.size(), i = 0; i < size; ++i) {
            final TLRPC.TL_secureValue tl_secureValue = this.currentForm.values.get(i);
            TLRPC.TL_secureRequiredType tl_secureRequiredType2 = null;
            ArrayList<TLRPC.TL_secureRequiredType> list2 = null;
            boolean b = false;
            Label_1081: {
                ArrayList<TLRPC.TL_secureRequiredType> list;
                TLRPC.TL_secureRequiredType tl_secureRequiredType;
                if (this.isPersonalDocument(tl_secureValue.type)) {
                    list = new ArrayList<TLRPC.TL_secureRequiredType>();
                    final TLRPC.TL_secureRequiredType e = new TLRPC.TL_secureRequiredType();
                    e.type = tl_secureValue.type;
                    e.selfie_required = true;
                    e.translation_required = true;
                    list.add(e);
                    tl_secureRequiredType = new TLRPC.TL_secureRequiredType();
                    tl_secureRequiredType.type = new TLRPC.TL_secureValueTypePersonalDetails();
                }
                else {
                    if (!this.isAddressDocument(tl_secureValue.type)) {
                        tl_secureRequiredType2 = new TLRPC.TL_secureRequiredType();
                        tl_secureRequiredType2.type = tl_secureValue.type;
                        list2 = null;
                        b = false;
                        break Label_1081;
                    }
                    list = new ArrayList<TLRPC.TL_secureRequiredType>();
                    final TLRPC.TL_secureRequiredType e2 = new TLRPC.TL_secureRequiredType();
                    e2.type = tl_secureValue.type;
                    e2.translation_required = true;
                    list.add(e2);
                    tl_secureRequiredType = new TLRPC.TL_secureRequiredType();
                    tl_secureRequiredType.type = new TLRPC.TL_secureValueTypeAddress();
                }
                list2 = list;
                b = true;
                tl_secureRequiredType2 = tl_secureRequiredType;
            }
            this.addField(context, tl_secureRequiredType2, list2, b, i == size - 1);
        }
        this.updateManageVisibility();
    }
    
    private void createPasswordInterface(final Context context) {
        TLRPC.User currentUser = null;
        Label_0074: {
            if (this.currentForm != null) {
                for (int i = 0; i < this.currentForm.users.size(); ++i) {
                    currentUser = this.currentForm.users.get(i);
                    if (currentUser.id == this.currentBotId) {
                        break Label_0074;
                    }
                }
                currentUser = null;
            }
            else {
                currentUser = UserConfig.getInstance(super.currentAccount).getCurrentUser();
            }
        }
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
        (this.emptyView = new EmptyTextProgressView(context)).showProgress();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.passwordAvatarContainer = new FrameLayout(context);
        this.linearLayout2.addView((View)this.passwordAvatarContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 100));
        final BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
        this.passwordAvatarContainer.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, 17, 0.0f, 8.0f, 0.0f, 0.0f));
        backupImageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", new AvatarDrawable(currentUser), currentUser);
        this.passwordRequestTextView = new TextInfoPrivacyCell(context);
        this.passwordRequestTextView.getTextView().setGravity(1);
        if (this.currentBotId == 0) {
            this.passwordRequestTextView.setText(LocaleController.getString("PassportSelfRequest", 2131560328));
        }
        else {
            this.passwordRequestTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131560314, UserObject.getFirstName(currentUser))));
        }
        ((FrameLayout$LayoutParams)this.passwordRequestTextView.getTextView().getLayoutParams()).gravity = 1;
        final LinearLayout linearLayout2 = this.linearLayout2;
        final TextInfoPrivacyCell passwordRequestTextView = this.passwordRequestTextView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        linearLayout2.addView((View)passwordRequestTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2 | 0x30, 21.0f, 0.0f, 21.0f, 0.0f));
        (this.noPasswordImageView = new ImageView(context)).setImageResource(2131165693);
        this.noPasswordImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), PorterDuff$Mode.MULTIPLY));
        this.linearLayout2.addView((View)this.noPasswordImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 13, 0, 0));
        (this.noPasswordTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.noPasswordTextView.setGravity(1);
        this.noPasswordTextView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(17.0f));
        this.noPasswordTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.noPasswordTextView.setText((CharSequence)LocaleController.getString("TelegramPassportCreatePasswordInfo", 2131560874));
        final LinearLayout linearLayout3 = this.linearLayout2;
        final TextView noPasswordTextView = this.noPasswordTextView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        linearLayout3.addView((View)noPasswordTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n3 | 0x30, 21.0f, 10.0f, 21.0f, 0.0f));
        (this.noPasswordSetTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText5"));
        this.noPasswordSetTextView.setGravity(17);
        this.noPasswordSetTextView.setTextSize(1, 16.0f);
        this.noPasswordSetTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.noPasswordSetTextView.setText((CharSequence)LocaleController.getString("TelegramPassportCreatePassword", 2131560873));
        final LinearLayout linearLayout4 = this.linearLayout2;
        final TextView noPasswordSetTextView = this.noPasswordSetTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        linearLayout4.addView((View)noPasswordSetTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 24.0f, n4 | 0x30, 21.0f, 9.0f, 21.0f, 0.0f));
        this.noPasswordSetTextView.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$M4nzx3gJGtJxSj4qK5fr27GLS6o(this));
        this.inputFields = new EditTextBoldCursor[1];
        this.inputFieldContainers = new ViewGroup[1];
        for (int j = 0; j < 1; ++j) {
            this.inputFieldContainers[j] = (ViewGroup)new FrameLayout(context);
            this.linearLayout2.addView((View)this.inputFieldContainers[j], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
            this.inputFieldContainers[j].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            (this.inputFields[j] = new EditTextBoldCursor(context)).setTag((Object)j);
            this.inputFields[j].setTextSize(1, 16.0f);
            this.inputFields[j].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[j].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[j].setBackgroundDrawable((Drawable)null);
            this.inputFields[j].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[j].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[j].setCursorWidth(1.5f);
            this.inputFields[j].setInputType(129);
            this.inputFields[j].setMaxLines(1);
            this.inputFields[j].setLines(1);
            this.inputFields[j].setSingleLine(true);
            this.inputFields[j].setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.inputFields[j].setTypeface(Typeface.DEFAULT);
            this.inputFields[j].setImeOptions(268435462);
            this.inputFields[j].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            final EditTextBoldCursor editTextBoldCursor = this.inputFields[j];
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            editTextBoldCursor.setGravity(gravity);
            this.inputFieldContainers[j].addView((View)this.inputFields[j], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            this.inputFields[j].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$It4B3VALo5oSVQDPd8g6gcO_bFY(this));
            this.inputFields[j].setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
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
        }
        (this.passwordInfoRequestTextView = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.passwordInfoRequestTextView.setText(LocaleController.formatString("PassportRequestPasswordInfo", 2131560315, new Object[0]));
        this.linearLayout2.addView((View)this.passwordInfoRequestTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.passwordForgotButton = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.passwordForgotButton.setTextSize(1, 14.0f);
        this.passwordForgotButton.setText((CharSequence)LocaleController.getString("ForgotPassword", 2131559503));
        this.passwordForgotButton.setPadding(0, 0, 0, 0);
        final LinearLayout linearLayout5 = this.linearLayout2;
        final TextView passwordForgotButton = this.passwordForgotButton;
        int n5;
        if (LocaleController.isRTL) {
            n5 = n;
        }
        else {
            n5 = 3;
        }
        linearLayout5.addView((View)passwordForgotButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 30, n5 | 0x30, 21, 0, 21, 0));
        this.passwordForgotButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$rCPvENS17XdkP3jlE3Ii__88K9s(this));
        this.updatePasswordInterface();
    }
    
    private void createPhoneInterface(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131560304));
        this.languageMap = new HashMap<String, String>();
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
                if (split.length > 3) {
                    this.phoneFormatMap.put(split[0], split[3]);
                }
                this.languageMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        Collections.sort(this.countriesArray, (Comparator<? super String>)_$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
        final String phone = UserConfig.getInstance(super.currentAccount).getCurrentUser().phone;
        final TextSettingsCell textSettingsCell = new TextSettingsCell(context);
        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        textSettingsCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        final PhoneFormat instance = PhoneFormat.getInstance();
        final StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(phone);
        textSettingsCell.setText(LocaleController.formatString("PassportPhoneUseSame", 2131560308, instance.format(sb.toString())), false);
        this.linearLayout2.addView((View)textSettingsCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        textSettingsCell.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$ZXxmpnBqGMDCWhhQmZBD_R6_C50(this));
        (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameInfo", 2131560310));
        this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportPhoneUseOther", 2131560307));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[3];
        for (int i = 0; i < 3; ++i) {
            if (i == 2) {
                this.inputFields[i] = new HintEditText(context);
            }
            else {
                this.inputFields[i] = new EditTextBoldCursor(context);
            }
            Object o;
            if (i == 1) {
                o = new LinearLayout(context);
                ((LinearLayout)o).setOrientation(0);
                this.linearLayout2.addView((View)o, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                ((ViewGroup)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            else if (i == 2) {
                o = this.inputFields[1].getParent();
            }
            else {
                o = new FrameLayout(context);
                this.linearLayout2.addView((View)o, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                ((ViewGroup)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            this.inputFields[i].setTag((Object)i);
            this.inputFields[i].setTextSize(1, 16.0f);
            this.inputFields[i].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.inputFields[i].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setBackgroundDrawable((Drawable)null);
            this.inputFields[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.inputFields[i].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i].setCursorWidth(1.5f);
            if (i == 0) {
                this.inputFields[i].setOnTouchListener((View$OnTouchListener)new _$$Lambda$PassportActivity$m7XOq19_n687jJsXOFpNqm9gDgg(this));
                this.inputFields[i].setText((CharSequence)LocaleController.getString("ChooseCountry", 2131559086));
                this.inputFields[i].setInputType(0);
                this.inputFields[i].setFocusable(false);
            }
            else {
                this.inputFields[i].setInputType(3);
                if (i == 2) {
                    this.inputFields[i].setImeOptions(268435462);
                }
                else {
                    this.inputFields[i].setImeOptions(268435461);
                }
            }
            final EditTextBoldCursor[] inputFields = this.inputFields;
            inputFields[i].setSelection(inputFields[i].length());
            int gravity = 5;
            if (i == 1) {
                (this.plusTextView = new TextView(context)).setText((CharSequence)"+");
                this.plusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.plusTextView.setTextSize(1, 16.0f);
                ((ViewGroup)o).addView((View)this.plusTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                this.inputFields[i].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                this.inputFields[i].setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(5) });
                this.inputFields[i].setGravity(19);
                ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 16.0f, 6.0f));
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        if (PassportActivity.this.ignoreOnTextChange) {
                            return;
                        }
                        PassportActivity.this.ignoreOnTextChange = true;
                        String s = PhoneFormat.stripExceptNumbers(PassportActivity.this.inputFields[1].getText().toString());
                        PassportActivity.this.inputFields[1].setText((CharSequence)s);
                        final HintEditText hintEditText = (HintEditText)PassportActivity.this.inputFields[2];
                        if (s.length() == 0) {
                            hintEditText.setHintText(null);
                            hintEditText.setHint((CharSequence)LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                            PassportActivity.this.inputFields[0].setText((CharSequence)LocaleController.getString("ChooseCountry", 2131559086));
                        }
                        else {
                            final int length = s.length();
                            int i = 4;
                            String text = null;
                            int n = 0;
                            Label_0329: {
                                if (length > 4) {
                                    while (true) {
                                        while (i >= 1) {
                                            final String substring = s.substring(0, i);
                                            if (PassportActivity.this.codesMap.get(substring) != null) {
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append(s.substring(i));
                                                sb.append(PassportActivity.this.inputFields[2].getText().toString());
                                                text = sb.toString();
                                                PassportActivity.this.inputFields[1].setText((CharSequence)substring);
                                                n = 1;
                                                s = substring;
                                                if (n == 0) {
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append(s.substring(1));
                                                    sb2.append(PassportActivity.this.inputFields[2].getText().toString());
                                                    text = sb2.toString();
                                                    final EditTextBoldCursor editTextBoldCursor = PassportActivity.this.inputFields[1];
                                                    s = s.substring(0, 1);
                                                    editTextBoldCursor.setText((CharSequence)s);
                                                }
                                                break Label_0329;
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
                            final String o = PassportActivity.this.codesMap.get(s);
                            boolean b = false;
                            Label_0438: {
                                if (o != null) {
                                    final int index = PassportActivity.this.countriesArray.indexOf(o);
                                    if (index != -1) {
                                        PassportActivity.this.inputFields[0].setText((CharSequence)PassportActivity.this.countriesArray.get(index));
                                        final String s2 = PassportActivity.this.phoneFormatMap.get(s);
                                        if (s2 != null) {
                                            hintEditText.setHintText(s2.replace('X', '\u2013'));
                                            hintEditText.setHint((CharSequence)null);
                                        }
                                        b = true;
                                        break Label_0438;
                                    }
                                }
                                b = false;
                            }
                            if (!b) {
                                hintEditText.setHintText(null);
                                hintEditText.setHint((CharSequence)LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                                PassportActivity.this.inputFields[0].setText((CharSequence)LocaleController.getString("WrongCountry", 2131561125));
                            }
                            if (n == 0) {
                                PassportActivity.this.inputFields[1].setSelection(PassportActivity.this.inputFields[1].getText().length());
                            }
                            if (text != null) {
                                hintEditText.requestFocus();
                                hintEditText.setText((CharSequence)text);
                                hintEditText.setSelection(hintEditText.length());
                            }
                        }
                        PassportActivity.this.ignoreOnTextChange = false;
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            else if (i == 2) {
                this.inputFields[i].setPadding(0, 0, 0, 0);
                this.inputFields[i].setGravity(19);
                this.inputFields[i].setHintText(null);
                this.inputFields[i].setHint((CharSequence)LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                    private int actionPosition;
                    private int characterAction = -1;
                    
                    public void afterTextChanged(final Editable editable) {
                        if (PassportActivity.this.ignoreOnPhoneChange) {
                            return;
                        }
                        final HintEditText hintEditText = (HintEditText)PassportActivity.this.inputFields[2];
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
                        PassportActivity.this.ignoreOnPhoneChange = true;
                        final String hintText = hintEditText.getHintText();
                        int length = n;
                        Label_0340: {
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
                                            break Label_0340;
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
                        PassportActivity.this.ignoreOnPhoneChange = false;
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
                final EditTextBoldCursor editTextBoldCursor = this.inputFields[i];
                if (!LocaleController.isRTL) {
                    gravity = 3;
                }
                editTextBoldCursor.setGravity(gravity);
                ((ViewGroup)o).addView((View)this.inputFields[i], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
            }
            this.inputFields[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$Ud8QO9mJkulFHffuDeCmjGoUGKM(this));
            if (i == 2) {
                this.inputFields[i].setOnKeyListener((View$OnKeyListener)new _$$Lambda$PassportActivity$jS_iy1VRiF9kFI3x6V7_e2W45Zc(this));
            }
            if (i == 0) {
                final View e = new View(context);
                this.dividers.add(e);
                e.setBackgroundColor(Theme.getColor("divider"));
                ((ViewGroup)o).addView(e, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
            }
        }
        Object upperCase = null;
        try {
            final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            if (telephonyManager != null) {
                upperCase = telephonyManager.getSimCountryIso().toUpperCase();
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        if (upperCase != null) {
            final String s = this.languageMap.get(upperCase);
            if (s != null && this.countriesArray.indexOf(s) != -1) {
                this.inputFields[1].setText((CharSequence)this.countriesMap.get(s));
            }
        }
        (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.bottomCell.setText(LocaleController.getString("PassportPhoneUploadInfo", 2131560306));
        this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
    }
    
    private void createPhoneVerificationInterface(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131560304));
        final FrameLayout frameLayout = new FrameLayout(context);
        this.scrollView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        for (int i = 0; i < 3; ++i) {
            (this.views[i] = new PhoneConfirmationView(context, i + 2)).setVisibility(8);
            final SlideView slideView = this.views[i];
            final boolean tablet = AndroidUtilities.isTablet();
            float n = 18.0f;
            float n2;
            if (tablet) {
                n2 = 26.0f;
            }
            else {
                n2 = 18.0f;
            }
            if (AndroidUtilities.isTablet()) {
                n = 26.0f;
            }
            frameLayout.addView((View)slideView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, n2, 30.0f, n, 0.0f));
        }
        final Bundle bundle = new Bundle();
        bundle.putString("phone", (String)this.currentValues.get("phone"));
        this.fillNextCodeParams(bundle, this.currentPhoneVerification, false);
    }
    
    private void createRequestInterface(final Context context) {
        TLRPC.User user = null;
        Label_0060: {
            if (this.currentForm != null) {
                for (int i = 0; i < this.currentForm.users.size(); ++i) {
                    user = this.currentForm.users.get(i);
                    if (user.id == this.currentBotId) {
                        break Label_0060;
                    }
                }
            }
            user = null;
        }
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
        super.actionBar.createMenu().addItem(1, 2131165786);
        if (user != null) {
            final FrameLayout frameLayout2 = new FrameLayout(context);
            this.linearLayout2.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 100));
            final BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(32.0f));
            frameLayout2.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, 17, 0.0f, 8.0f, 0.0f, 0.0f));
            backupImageView.setImage(ImageLocation.getForUser(user, false), "50_50", new AvatarDrawable(user), user);
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165396, "windowBackgroundGrayShadow"));
            this.bottomCell.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131560314, UserObject.getFirstName(user))));
            this.bottomCell.getTextView().setGravity(1);
            ((FrameLayout$LayoutParams)this.bottomCell.getTextView().getLayoutParams()).gravity = 1;
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        (this.headerCell = new HeaderCell(context)).setText(LocaleController.getString("PassportRequestedInformation", 2131560316));
        this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout2.addView((View)this.headerCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        final TLRPC.TL_account_authorizationForm currentForm = this.currentForm;
        if (currentForm != null) {
            final int size = currentForm.required_types.size();
            final ArrayList<TLRPC.TL_secureRequiredType> list = new ArrayList<TLRPC.TL_secureRequiredType>();
            final ArrayList<TLRPC.TL_secureRequiredType> list2 = new ArrayList<TLRPC.TL_secureRequiredType>();
            int j = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            while (j < size) {
                final TLRPC.SecureRequiredType secureRequiredType = this.currentForm.required_types.get(j);
                Label_0898: {
                    while (true) {
                        Label_0785: {
                            int n5;
                            int n6;
                            int n7;
                            int n8;
                            if (secureRequiredType instanceof TLRPC.TL_secureRequiredType) {
                                final TLRPC.TL_secureRequiredType tl_secureRequiredType = (TLRPC.TL_secureRequiredType)secureRequiredType;
                                if (this.isPersonalDocument(tl_secureRequiredType.type)) {
                                    list.add(tl_secureRequiredType);
                                    break Label_0785;
                                }
                                if (this.isAddressDocument(tl_secureRequiredType.type)) {
                                    list2.add(tl_secureRequiredType);
                                    n5 = n3 + 1;
                                    n6 = n;
                                    n7 = n2;
                                    n8 = n4;
                                }
                                else {
                                    final TLRPC.SecureValueType type = tl_secureRequiredType.type;
                                    if (type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                        n6 = 1;
                                        n7 = n2;
                                        n5 = n3;
                                        n8 = n4;
                                    }
                                    else {
                                        n6 = n;
                                        n7 = n2;
                                        n5 = n3;
                                        n8 = n4;
                                        if (type instanceof TLRPC.TL_secureValueTypeAddress) {
                                            n8 = 1;
                                            n5 = n3;
                                            n7 = n2;
                                            n6 = n;
                                        }
                                    }
                                }
                            }
                            else {
                                n6 = n;
                                n7 = n2;
                                n5 = n3;
                                n8 = n4;
                                if (secureRequiredType instanceof TLRPC.TL_secureRequiredTypeOneOf) {
                                    final TLRPC.TL_secureRequiredTypeOneOf tl_secureRequiredTypeOneOf = (TLRPC.TL_secureRequiredTypeOneOf)secureRequiredType;
                                    if (tl_secureRequiredTypeOneOf.types.isEmpty()) {
                                        n6 = n;
                                        n7 = n2;
                                        n5 = n3;
                                        n8 = n4;
                                    }
                                    else {
                                        final TLRPC.SecureRequiredType secureRequiredType2 = tl_secureRequiredTypeOneOf.types.get(0);
                                        if (!(secureRequiredType2 instanceof TLRPC.TL_secureRequiredType)) {
                                            n6 = n;
                                            n7 = n2;
                                            n5 = n3;
                                            n8 = n4;
                                        }
                                        else {
                                            final TLRPC.TL_secureRequiredType tl_secureRequiredType2 = (TLRPC.TL_secureRequiredType)secureRequiredType2;
                                            if (this.isPersonalDocument(tl_secureRequiredType2.type)) {
                                                for (int size2 = tl_secureRequiredTypeOneOf.types.size(), k = 0; k < size2; ++k) {
                                                    final TLRPC.SecureRequiredType secureRequiredType3 = tl_secureRequiredTypeOneOf.types.get(k);
                                                    if (secureRequiredType3 instanceof TLRPC.TL_secureRequiredType) {
                                                        list.add((TLRPC.TL_secureRequiredType)secureRequiredType3);
                                                    }
                                                }
                                                break Label_0785;
                                            }
                                            n6 = n;
                                            n7 = n2;
                                            n5 = n3;
                                            n8 = n4;
                                            if (this.isAddressDocument(tl_secureRequiredType2.type)) {
                                                for (int size3 = tl_secureRequiredTypeOneOf.types.size(), l = 0; l < size3; ++l) {
                                                    final TLRPC.SecureRequiredType secureRequiredType4 = tl_secureRequiredTypeOneOf.types.get(l);
                                                    if (secureRequiredType4 instanceof TLRPC.TL_secureRequiredType) {
                                                        list2.add((TLRPC.TL_secureRequiredType)secureRequiredType4);
                                                    }
                                                }
                                                ++n3;
                                                break Label_0898;
                                            }
                                        }
                                    }
                                }
                            }
                            n = n6;
                            n2 = n7;
                            n3 = n5;
                            n4 = n8;
                            break Label_0898;
                        }
                        int n7 = n2 + 1;
                        int n6 = n;
                        int n5 = n3;
                        int n8 = n4;
                        continue;
                    }
                }
                ++j;
            }
            final boolean b = n == 0 || n2 > 1;
            final boolean b2 = n4 == 0 || n3 > 1;
            for (int index = 0, n9 = size; index < n9; ++index) {
                final TLRPC.SecureRequiredType secureRequiredType5 = this.currentForm.required_types.get(index);
                TLRPC.TL_secureRequiredType tl_secureRequiredType3 = null;
                ArrayList<TLRPC.TL_secureRequiredType> list3 = null;
                boolean b3 = false;
                Label_1425: {
                    if (secureRequiredType5 instanceof TLRPC.TL_secureRequiredType) {
                        tl_secureRequiredType3 = (TLRPC.TL_secureRequiredType)secureRequiredType5;
                        final TLRPC.SecureValueType type2 = tl_secureRequiredType3.type;
                        if (!(type2 instanceof TLRPC.TL_secureValueTypePhone) && !(type2 instanceof TLRPC.TL_secureValueTypeEmail)) {
                            Label_1042: {
                                if (type2 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                    if (!b) {
                                        list3 = list;
                                        break Label_1042;
                                    }
                                }
                                else {
                                    if (!(type2 instanceof TLRPC.TL_secureValueTypeAddress)) {
                                        if (b && this.isPersonalDocument(type2)) {
                                            list3 = new ArrayList<TLRPC.TL_secureRequiredType>();
                                            list3.add(tl_secureRequiredType3);
                                            tl_secureRequiredType3 = new TLRPC.TL_secureRequiredType();
                                            tl_secureRequiredType3.type = new TLRPC.TL_secureValueTypePersonalDetails();
                                        }
                                        else {
                                            if (!b2 || !this.isAddressDocument(tl_secureRequiredType3.type)) {
                                                continue;
                                            }
                                            list3 = new ArrayList<TLRPC.TL_secureRequiredType>();
                                            list3.add(tl_secureRequiredType3);
                                            tl_secureRequiredType3 = new TLRPC.TL_secureRequiredType();
                                            tl_secureRequiredType3.type = new TLRPC.TL_secureValueTypeAddress();
                                        }
                                        b3 = true;
                                        break Label_1425;
                                    }
                                    if (!b2) {
                                        list3 = list2;
                                        break Label_1042;
                                    }
                                }
                                list3 = null;
                            }
                        }
                        else {
                            list3 = null;
                        }
                        b3 = false;
                    }
                    else {
                        if (!(secureRequiredType5 instanceof TLRPC.TL_secureRequiredTypeOneOf)) {
                            continue;
                        }
                        final TLRPC.TL_secureRequiredTypeOneOf tl_secureRequiredTypeOneOf2 = (TLRPC.TL_secureRequiredTypeOneOf)secureRequiredType5;
                        if (tl_secureRequiredTypeOneOf2.types.isEmpty()) {
                            continue;
                        }
                        final TLRPC.SecureRequiredType secureRequiredType6 = tl_secureRequiredTypeOneOf2.types.get(0);
                        if (!(secureRequiredType6 instanceof TLRPC.TL_secureRequiredType)) {
                            continue;
                        }
                        final TLRPC.TL_secureRequiredType tl_secureRequiredType4 = (TLRPC.TL_secureRequiredType)secureRequiredType6;
                        if ((!b || !this.isPersonalDocument(tl_secureRequiredType4.type)) && (!b2 || !this.isAddressDocument(tl_secureRequiredType4.type))) {
                            continue;
                        }
                        final ArrayList<TLRPC.TL_secureRequiredType> list4 = new ArrayList<TLRPC.TL_secureRequiredType>();
                        for (int size4 = tl_secureRequiredTypeOneOf2.types.size(), index2 = 0; index2 < size4; ++index2) {
                            final TLRPC.SecureRequiredType secureRequiredType7 = tl_secureRequiredTypeOneOf2.types.get(index2);
                            if (secureRequiredType7 instanceof TLRPC.TL_secureRequiredType) {
                                list4.add((TLRPC.TL_secureRequiredType)secureRequiredType7);
                            }
                        }
                        TLRPC.TL_secureRequiredType tl_secureRequiredType5;
                        if (this.isPersonalDocument(tl_secureRequiredType4.type)) {
                            tl_secureRequiredType5 = new TLRPC.TL_secureRequiredType();
                            tl_secureRequiredType5.type = new TLRPC.TL_secureValueTypePersonalDetails();
                        }
                        else {
                            tl_secureRequiredType5 = new TLRPC.TL_secureRequiredType();
                            tl_secureRequiredType5.type = new TLRPC.TL_secureValueTypeAddress();
                        }
                        b3 = true;
                        tl_secureRequiredType3 = tl_secureRequiredType5;
                        list3 = list4;
                    }
                }
                this.addField(context, tl_secureRequiredType3, list3, b3, index == n9 - 1);
            }
        }
        if (user != null) {
            (this.bottomCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.bottomCell.setLinkTextColorKey("windowBackgroundWhiteGrayText4");
            if (!TextUtils.isEmpty((CharSequence)this.currentForm.privacy_policy_url)) {
                final String formatString = LocaleController.formatString("PassportPolicy", 2131560311, UserObject.getFirstName(user), user.username);
                final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)formatString);
                final int index3 = formatString.indexOf(42);
                final int lastIndex = formatString.lastIndexOf(42);
                if (index3 != -1 && lastIndex != -1) {
                    this.bottomCell.getTextView().setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
                    text.replace(lastIndex, lastIndex + 1, (CharSequence)"");
                    text.replace(index3, index3 + 1, (CharSequence)"");
                    text.setSpan((Object)new LinkSpan(), index3, lastIndex - 1, 33);
                }
                this.bottomCell.setText((CharSequence)text);
            }
            else {
                this.bottomCell.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("PassportNoPolicy", 2131560299, UserObject.getFirstName(user), user.username)));
            }
            this.bottomCell.getTextView().setHighlightColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.bottomCell.getTextView().setGravity(1);
            this.linearLayout2.addView((View)this.bottomCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        (this.bottomLayout = new FrameLayout(context)).setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("passport_authorizeBackground"), Theme.getColor("passport_authorizeBackgroundSelected")));
        frameLayout.addView((View)this.bottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$YcmK_1FiSvvv_xWa8i2cDGfFT0Y(this));
        (this.acceptTextView = new TextView(context)).setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.acceptTextView.setCompoundDrawablesWithIntrinsicBounds(2131165298, 0, 0, 0);
        this.acceptTextView.setTextColor(Theme.getColor("passport_authorizeText"));
        this.acceptTextView.setText((CharSequence)LocaleController.getString("PassportAuthorize", 2131560193));
        this.acceptTextView.setTextSize(1, 14.0f);
        this.acceptTextView.setGravity(17);
        this.acceptTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomLayout.addView((View)this.acceptTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 17));
        (this.progressViewButton = new ContextProgressView(context, 0)).setVisibility(4);
        this.bottomLayout.addView((View)this.progressViewButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        frameLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
    }
    
    private EncryptionResult createSecureDocument(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_1        
        //     5: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //     8: invokevirtual   java/io/File.length:()J
        //    11: l2i            
        //    12: newarray        B
        //    14: astore_2       
        //    15: new             Ljava/io/RandomAccessFile;
        //    18: astore_3       
        //    19: aload_3        
        //    20: aload_1        
        //    21: ldc_w           "rws"
        //    24: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    27: aload_3        
        //    28: aload_2        
        //    29: invokevirtual   java/io/RandomAccessFile.readFully:([B)V
        //    32: aload_3        
        //    33: astore_1       
        //    34: goto            40
        //    37: astore_1       
        //    38: aconst_null    
        //    39: astore_1       
        //    40: aload_0        
        //    41: aload_2        
        //    42: invokespecial   org/telegram/ui/PassportActivity.encryptData:([B)Lorg/telegram/ui/PassportActivity$EncryptionResult;
        //    45: astore_3       
        //    46: aload_1        
        //    47: lconst_0       
        //    48: invokevirtual   java/io/RandomAccessFile.seek:(J)V
        //    51: aload_1        
        //    52: aload_3        
        //    53: getfield        org/telegram/ui/PassportActivity$EncryptionResult.encryptedData:[B
        //    56: invokevirtual   java/io/RandomAccessFile.write:([B)V
        //    59: aload_1        
        //    60: invokevirtual   java/io/RandomAccessFile.close:()V
        //    63: aload_3        
        //    64: areturn        
        //    65: astore_1       
        //    66: aload_3        
        //    67: astore_1       
        //    68: goto            40
        //    71: astore_1       
        //    72: goto            63
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  15     27     37     40     Ljava/lang/Exception;
        //  27     32     65     71     Ljava/lang/Exception;
        //  46     63     71     75     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0063:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private String decryptData(final byte[] array, byte[] array2, final byte[] a2) {
        if (array == null || array2 == null || array2.length != 32 || a2 == null || a2.length != 32) {
            return null;
        }
        final byte[] computeSHA512 = Utilities.computeSHA512(array2, a2);
        array2 = new byte[32];
        System.arraycopy(computeSHA512, 0, array2, 0, 32);
        final byte[] array3 = new byte[16];
        System.arraycopy(computeSHA512, 32, array3, 0, 16);
        final byte[] bytes = new byte[array.length];
        System.arraycopy(array, 0, bytes, 0, array.length);
        Utilities.aesCbcEncryptionByteArraySafe(bytes, array2, array3, 0, bytes.length, 0, 0);
        if (!Arrays.equals(Utilities.computeSHA256(bytes), a2)) {
            return null;
        }
        final int offset = bytes[0] & 0xFF;
        return new String(bytes, offset, bytes.length - offset);
    }
    
    private byte[] decryptSecret(final byte[] array, byte[] array2) {
        if (array != null && array.length == 32) {
            final byte[] array3 = new byte[32];
            System.arraycopy(array2, 0, array3, 0, 32);
            final byte[] array4 = new byte[16];
            System.arraycopy(array2, 32, array4, 0, 16);
            array2 = new byte[32];
            System.arraycopy(array, 0, array2, 0, 32);
            Utilities.aesCbcEncryptionByteArraySafe(array2, array3, array4, 0, array2.length, 0, 0);
            return array2;
        }
        return null;
    }
    
    private byte[] decryptValueSecret(final byte[] array, byte[] array2) {
        if (array == null || array.length != 32 || array2 == null || array2.length != 32) {
            return null;
        }
        final byte[] array3 = new byte[32];
        System.arraycopy(this.saltedPassword, 0, array3, 0, 32);
        final byte[] array4 = new byte[16];
        System.arraycopy(this.saltedPassword, 32, array4, 0, 16);
        final byte[] array5 = new byte[32];
        System.arraycopy(this.secureSecret, 0, array5, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(array5, array3, array4, 0, array5.length, 0, 0);
        if (!checkSecret(array5, null)) {
            return null;
        }
        final byte[] computeSHA512 = Utilities.computeSHA512(array5, array2);
        final byte[] array6 = new byte[32];
        System.arraycopy(computeSHA512, 0, array6, 0, 32);
        array2 = new byte[16];
        System.arraycopy(computeSHA512, 32, array2, 0, 16);
        final byte[] array7 = new byte[32];
        System.arraycopy(array, 0, array7, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(array7, array6, array2, 0, array7.length, 0, 0);
        return array7;
    }
    
    private void deleteValueInternal(final TLRPC.TL_secureRequiredType tl_secureRequiredType, final TLRPC.TL_secureRequiredType tl_secureRequiredType2, final ArrayList<TLRPC.TL_secureRequiredType> list, final boolean b, final Runnable runnable, final ErrorRunnable errorRunnable, final boolean b2) {
        if (tl_secureRequiredType == null) {
            return;
        }
        final TLRPC.TL_account_deleteSecureValue tl_account_deleteSecureValue = new TLRPC.TL_account_deleteSecureValue();
        if (b2 && tl_secureRequiredType2 != null) {
            tl_account_deleteSecureValue.types.add(tl_secureRequiredType2.type);
        }
        else {
            if (b) {
                tl_account_deleteSecureValue.types.add(tl_secureRequiredType.type);
            }
            if (tl_secureRequiredType2 != null) {
                tl_account_deleteSecureValue.types.add(tl_secureRequiredType2.type);
            }
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_deleteSecureValue, new _$$Lambda$PassportActivity$yqjIwJxQHVRYAcsusKtM70xYOHo(this, errorRunnable, b2, tl_secureRequiredType2, tl_secureRequiredType, b, list, runnable));
    }
    
    private EncryptionResult encryptData(byte[] array) {
        final byte[] randomSecret = this.getRandomSecret();
        int n;
        for (n = Utilities.random.nextInt(208) + 32; (array.length + n) % 16 != 0; ++n) {}
        final byte[] bytes = new byte[n];
        Utilities.random.nextBytes(bytes);
        bytes[0] = (byte)n;
        final byte[] array2 = new byte[array.length + n];
        System.arraycopy(bytes, 0, array2, 0, n);
        System.arraycopy(array, 0, array2, n, array.length);
        final byte[] computeSHA256 = Utilities.computeSHA256(array2);
        final byte[] computeSHA257 = Utilities.computeSHA512(randomSecret, computeSHA256);
        final byte[] array3 = new byte[32];
        System.arraycopy(computeSHA257, 0, array3, 0, 32);
        array = new byte[16];
        System.arraycopy(computeSHA257, 32, array, 0, 16);
        Utilities.aesCbcEncryptionByteArraySafe(array2, array3, array, 0, array2.length, 0, 1);
        final byte[] array4 = new byte[32];
        System.arraycopy(this.saltedPassword, 0, array4, 0, 32);
        final byte[] array5 = new byte[16];
        System.arraycopy(this.saltedPassword, 32, array5, 0, 16);
        final byte[] array6 = new byte[32];
        System.arraycopy(this.secureSecret, 0, array6, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(array6, array4, array5, 0, array6.length, 0, 0);
        final byte[] computeSHA258 = Utilities.computeSHA512(array6, computeSHA256);
        final byte[] array7 = new byte[32];
        System.arraycopy(computeSHA258, 0, array7, 0, 32);
        final byte[] array8 = new byte[16];
        System.arraycopy(computeSHA258, 32, array8, 0, 16);
        final byte[] array9 = new byte[32];
        System.arraycopy(randomSecret, 0, array9, 0, 32);
        Utilities.aesCbcEncryptionByteArraySafe(array9, array7, array8, 0, array9.length, 0, 1);
        return new EncryptionResult(array2, array9, randomSecret, computeSHA256, array3, array);
    }
    
    private void fillInitialValues() {
        if (this.initialValues != null) {
            return;
        }
        this.initialValues = this.getCurrentValues();
    }
    
    private void fillNextCodeParams(final Bundle bundle, final TLRPC.TL_auth_sentCode tl_auth_sentCode, final boolean b) {
        bundle.putString("phoneHash", tl_auth_sentCode.phone_code_hash);
        final TLRPC.auth_CodeType next_type = tl_auth_sentCode.next_type;
        if (next_type instanceof TLRPC.TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        }
        if (tl_auth_sentCode.timeout == 0) {
            tl_auth_sentCode.timeout = 60;
        }
        bundle.putInt("timeout", tl_auth_sentCode.timeout * 1000);
        final TLRPC.auth_SentCodeType type = tl_auth_sentCode.type;
        if (type instanceof TLRPC.TL_auth_sentCodeTypeCall) {
            bundle.putInt("type", 4);
            bundle.putInt("length", tl_auth_sentCode.type.length);
            this.setPage(2, b, bundle);
        }
        else if (type instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt("type", 3);
            bundle.putString("pattern", tl_auth_sentCode.type.pattern);
            this.setPage(1, b, bundle);
        }
        else if (type instanceof TLRPC.TL_auth_sentCodeTypeSms) {
            bundle.putInt("type", 2);
            bundle.putInt("length", tl_auth_sentCode.type.length);
            this.setPage(0, b, bundle);
        }
    }
    
    private String getCurrentValues() {
        final StringBuilder sb = new StringBuilder();
        final int n = 0;
        int n2 = 0;
        while (true) {
            final EditTextBoldCursor[] inputFields = this.inputFields;
            if (n2 >= inputFields.length) {
                break;
            }
            sb.append((CharSequence)inputFields[n2].getText());
            sb.append(",");
            ++n2;
        }
        if (this.inputExtraFields != null) {
            int n3 = 0;
            while (true) {
                final EditTextBoldCursor[] inputExtraFields = this.inputExtraFields;
                if (n3 >= inputExtraFields.length) {
                    break;
                }
                sb.append((CharSequence)inputExtraFields[n3].getText());
                sb.append(",");
                ++n3;
            }
        }
        for (int size = this.documents.size(), i = 0; i < size; ++i) {
            sb.append(this.documents.get(i).secureFile.id);
        }
        final SecureDocument frontDocument = this.frontDocument;
        if (frontDocument != null) {
            sb.append(frontDocument.secureFile.id);
        }
        final SecureDocument reverseDocument = this.reverseDocument;
        if (reverseDocument != null) {
            sb.append(reverseDocument.secureFile.id);
        }
        final SecureDocument selfieDocument = this.selfieDocument;
        if (selfieDocument != null) {
            sb.append(selfieDocument.secureFile.id);
        }
        for (int size2 = this.translationDocuments.size(), j = n; j < size2; ++j) {
            sb.append(this.translationDocuments.get(j).secureFile.id);
        }
        return sb.toString();
    }
    
    private String getDocumentHash(final SecureDocument secureDocument) {
        if (secureDocument != null) {
            final TLRPC.TL_secureFile secureFile = secureDocument.secureFile;
            if (secureFile != null) {
                final byte[] file_hash = secureFile.file_hash;
                if (file_hash != null) {
                    return Base64.encodeToString(file_hash, 2);
                }
            }
            final byte[] fileHash = secureDocument.fileHash;
            if (fileHash != null) {
                return Base64.encodeToString(fileHash, 2);
            }
        }
        return "";
    }
    
    private String getErrorsString(final HashMap<String, String> hashMap, final HashMap<String, String> hashMap2) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; ++i) {
            HashMap<String, String> hashMap3;
            if (i == 0) {
                hashMap3 = hashMap;
            }
            else {
                hashMap3 = hashMap2;
            }
            if (hashMap3 != null) {
                final Iterator<Map.Entry<String, String>> iterator = hashMap3.entrySet().iterator();
                while (iterator.hasNext()) {
                    String lowerCase;
                    final String s = lowerCase = iterator.next().getValue();
                    if (sb.length() > 0) {
                        sb.append(", ");
                        lowerCase = s.toLowerCase();
                    }
                    String substring = lowerCase;
                    if (lowerCase.endsWith(".")) {
                        substring = lowerCase.substring(0, lowerCase.length() - 1);
                    }
                    sb.append(substring);
                }
            }
        }
        if (sb.length() > 0) {
            sb.append('.');
        }
        return sb.toString();
    }
    
    private int getFieldCost(final String s) {
        int n = 0;
        Label_0423: {
            switch (s.hashCode()) {
                case 2013122196: {
                    if (s.equals("last_name")) {
                        n = 4;
                        break Label_0423;
                    }
                    break;
                }
                case 2002465324: {
                    if (s.equals("post_code")) {
                        n = 14;
                        break Label_0423;
                    }
                    break;
                }
                case 1481071862: {
                    if (s.equals("country_code")) {
                        n = 8;
                        break Label_0423;
                    }
                    break;
                }
                case 1181577377: {
                    if (s.equals("middle_name_native")) {
                        n = 3;
                        break Label_0423;
                    }
                    break;
                }
                case 1168724782: {
                    if (s.equals("birth_date")) {
                        n = 6;
                        break Label_0423;
                    }
                    break;
                }
                case 506677093: {
                    if (s.equals("document_no")) {
                        n = 10;
                        break Label_0423;
                    }
                    break;
                }
                case 475919162: {
                    if (s.equals("expiry_date")) {
                        n = 11;
                        break Label_0423;
                    }
                    break;
                }
                case 451516732: {
                    if (s.equals("first_name_native")) {
                        n = 1;
                        break Label_0423;
                    }
                    break;
                }
                case 421072629: {
                    if (s.equals("middle_name")) {
                        n = 2;
                        break Label_0423;
                    }
                    break;
                }
                case 109757585: {
                    if (s.equals("state")) {
                        n = 16;
                        break Label_0423;
                    }
                    break;
                }
                case 3053931: {
                    if (s.equals("city")) {
                        n = 15;
                        break Label_0423;
                    }
                    break;
                }
                case -160985414: {
                    if (s.equals("first_name")) {
                        n = 0;
                        break Label_0423;
                    }
                    break;
                }
                case -796150910: {
                    if (s.equals("street_line2")) {
                        n = 13;
                        break Label_0423;
                    }
                    break;
                }
                case -796150911: {
                    if (s.equals("street_line1")) {
                        n = 12;
                        break Label_0423;
                    }
                    break;
                }
                case -1249512767: {
                    if (s.equals("gender")) {
                        n = 7;
                        break Label_0423;
                    }
                    break;
                }
                case -1537298398: {
                    if (s.equals("last_name_native")) {
                        n = 5;
                        break Label_0423;
                    }
                    break;
                }
                case -2006252145: {
                    if (s.equals("residence_country_code")) {
                        n = 9;
                        break Label_0423;
                    }
                    break;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return 100;
            }
            case 16: {
                return 33;
            }
            case 15: {
                return 32;
            }
            case 14: {
                return 31;
            }
            case 13: {
                return 30;
            }
            case 12: {
                return 29;
            }
            case 11: {
                return 28;
            }
            case 10: {
                return 27;
            }
            case 9: {
                return 26;
            }
            case 8: {
                return 25;
            }
            case 7: {
                return 24;
            }
            case 6: {
                return 23;
            }
            case 4:
            case 5: {
                return 22;
            }
            case 2:
            case 3: {
                return 21;
            }
            case 0:
            case 1: {
                return 20;
            }
        }
    }
    
    private int getMaxSelectedDocuments() {
        final int uploadingFileType = this.uploadingFileType;
        int n;
        if (uploadingFileType == 0) {
            n = this.documents.size();
        }
        else {
            if (uploadingFileType != 4) {
                return 1;
            }
            n = this.translationDocuments.size();
        }
        return 20 - n;
    }
    
    private String getNameForType(final TLRPC.SecureValueType secureValueType) {
        if (secureValueType instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            return "personal_details";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypePassport) {
            return "passport";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeInternalPassport) {
            return "internal_passport";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeDriverLicense) {
            return "driver_license";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeIdentityCard) {
            return "identity_card";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeUtilityBill) {
            return "utility_bill";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeAddress) {
            return "address";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeBankStatement) {
            return "bank_statement";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
            return "rental_agreement";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
            return "temporary_registration";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypePassportRegistration) {
            return "passport_registration";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeEmail) {
            return "email";
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypePhone) {
            return "phone";
        }
        return "";
    }
    
    private byte[] getRandomSecret() {
        final byte[] bytes = new byte[32];
        Utilities.random.nextBytes(bytes);
        int i = 0;
        int n = 0;
        while (i < bytes.length) {
            n += (bytes[i] & 0xFF);
            ++i;
        }
        final int n2 = n % 255;
        if (n2 != 239) {
            final int nextInt = Utilities.random.nextInt(32);
            final int n3 = (bytes[nextInt] & 0xFF) + (239 - n2);
            int n4;
            if ((n4 = n3) < 255) {
                n4 = n3 + 255;
            }
            bytes[nextInt] = (byte)(n4 % 255);
        }
        return bytes;
    }
    
    private SecureDocumentKey getSecureDocumentKey(byte[] array, byte[] array2) {
        final byte[] computeSHA512 = Utilities.computeSHA512(this.decryptValueSecret(array, array2), array2);
        array = new byte[32];
        System.arraycopy(computeSHA512, 0, array, 0, 32);
        array2 = new byte[16];
        System.arraycopy(computeSHA512, 32, array2, 0, 16);
        return new SecureDocumentKey(array, array2);
    }
    
    private String getTextForType(final TLRPC.SecureValueType secureValueType) {
        if (secureValueType instanceof TLRPC.TL_secureValueTypePassport) {
            return LocaleController.getString("ActionBotDocumentPassport", 2131558504);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeDriverLicense) {
            return LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeIdentityCard) {
            return LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeUtilityBill) {
            return LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeBankStatement) {
            return LocaleController.getString("ActionBotDocumentBankStatement", 2131558498);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
            return LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeInternalPassport) {
            return LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypePassportRegistration) {
            return LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
            return LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypePhone) {
            return LocaleController.getString("ActionBotDocumentPhone", 2131558506);
        }
        if (secureValueType instanceof TLRPC.TL_secureValueTypeEmail) {
            return LocaleController.getString("ActionBotDocumentEmail", 2131558500);
        }
        return "";
    }
    
    private String getTranslitString(final String s) {
        return LocaleController.getInstance().getTranslitString(s, true);
    }
    
    private TLRPC.TL_secureValue getValueByType(final TLRPC.TL_secureRequiredType tl_secureRequiredType, final boolean b) {
        if (tl_secureRequiredType == null) {
            return null;
        }
        final int size = this.currentForm.values.size();
        final int n = 0;
        int index = 0;
        while (true) {
            Label_0472: {
                if (index >= size) {
                    break Label_0472;
                }
                final TLRPC.TL_secureValue tl_secureValue = this.currentForm.values.get(index);
                Label_0466: {
                    if (tl_secureRequiredType.type.getClass() != tl_secureValue.type.getClass()) {
                        break Label_0466;
                    }
                    if (!b) {
                        return tl_secureValue;
                    }
                    if (tl_secureRequiredType.selfie_required && !(tl_secureValue.selfie instanceof TLRPC.TL_secureFile)) {
                        return null;
                    }
                    if (tl_secureRequiredType.translation_required && tl_secureValue.translation.isEmpty()) {
                        return null;
                    }
                    if (this.isAddressDocument(tl_secureRequiredType.type) && tl_secureValue.files.isEmpty()) {
                        return null;
                    }
                    if (this.isPersonalDocument(tl_secureRequiredType.type) && !(tl_secureValue.front_side instanceof TLRPC.TL_secureFile)) {
                        return null;
                    }
                    final TLRPC.SecureValueType type = tl_secureRequiredType.type;
                    if ((type instanceof TLRPC.TL_secureValueTypeDriverLicense || type instanceof TLRPC.TL_secureValueTypeIdentityCard) && !(tl_secureValue.reverse_side instanceof TLRPC.TL_secureFile)) {
                        return null;
                    }
                    final TLRPC.SecureValueType type2 = tl_secureRequiredType.type;
                    if (!(type2 instanceof TLRPC.TL_secureValueTypePersonalDetails) && !(type2 instanceof TLRPC.TL_secureValueTypeAddress)) {
                        return tl_secureValue;
                    }
                    String[] array;
                    if (tl_secureRequiredType.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                        if (tl_secureRequiredType.native_names) {
                            array = new String[] { "first_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code" };
                        }
                        else {
                            array = new String[] { "first_name", "last_name", "birth_date", "gender", "country_code", "residence_country_code" };
                        }
                    }
                    else {
                        array = new String[] { "street_line1", "street_line2", "post_code", "city", "state", "country_code" };
                    }
                    try {
                        final JSONObject jsonObject = new JSONObject(this.decryptData(tl_secureValue.data.data, this.decryptValueSecret(tl_secureValue.data.secret, tl_secureValue.data.data_hash), tl_secureValue.data.data_hash));
                        int i = n;
                        while (i < array.length) {
                            if (jsonObject.has(array[i])) {
                                if (!TextUtils.isEmpty((CharSequence)jsonObject.getString(array[i]))) {
                                    ++i;
                                    continue;
                                }
                            }
                            return null;
                        }
                        return tl_secureValue;
                        ++index;
                        continue;
                        return null;
                    }
                    catch (Throwable t) {
                        return null;
                    }
                }
            }
        }
    }
    
    private TextDetailSecureCell getViewByType(TLRPC.TL_secureRequiredType key) {
        TextDetailSecureCell textDetailSecureCell2;
        final TextDetailSecureCell textDetailSecureCell = textDetailSecureCell2 = this.typesViews.get(key);
        if (textDetailSecureCell == null) {
            key = this.documentsToTypesLink.get(key);
            textDetailSecureCell2 = textDetailSecureCell;
            if (key != null) {
                textDetailSecureCell2 = this.typesViews.get(key);
            }
        }
        return textDetailSecureCell2;
    }
    
    private boolean hasNotValueForType(final Class<? extends TLRPC.SecureValueType> clazz) {
        for (int size = this.currentForm.values.size(), i = 0; i < size; ++i) {
            if (this.currentForm.values.get(i).type.getClass() == clazz) {
                return false;
            }
        }
        return true;
    }
    
    private boolean hasUnfilledValues() {
        return this.hasNotValueForType(TLRPC.TL_secureValueTypePhone.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeEmail.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypePersonalDetails.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypePassport.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeInternalPassport.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeIdentityCard.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeDriverLicense.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeAddress.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeUtilityBill.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypePassportRegistration.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeTemporaryRegistration.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeBankStatement.class) || this.hasNotValueForType(TLRPC.TL_secureValueTypeRentalAgreement.class);
    }
    
    private boolean isAddressDocument(final TLRPC.SecureValueType secureValueType) {
        return secureValueType instanceof TLRPC.TL_secureValueTypeUtilityBill || secureValueType instanceof TLRPC.TL_secureValueTypeBankStatement || secureValueType instanceof TLRPC.TL_secureValueTypePassportRegistration || secureValueType instanceof TLRPC.TL_secureValueTypeTemporaryRegistration || secureValueType instanceof TLRPC.TL_secureValueTypeRentalAgreement;
    }
    
    private boolean isHasNotAnyChanges() {
        final String initialValues = this.initialValues;
        return initialValues == null || initialValues.equals(this.getCurrentValues());
    }
    
    private boolean isPersonalDocument(final TLRPC.SecureValueType secureValueType) {
        return secureValueType instanceof TLRPC.TL_secureValueTypeDriverLicense || secureValueType instanceof TLRPC.TL_secureValueTypePassport || secureValueType instanceof TLRPC.TL_secureValueTypeInternalPassport || secureValueType instanceof TLRPC.TL_secureValueTypeIdentityCard;
    }
    
    private void loadPasswordInfo() {
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new _$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI(this)), super.classGuid);
    }
    
    private void onFieldError(final View view) {
        if (view == null) {
            return;
        }
        final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        AndroidUtilities.shakeView(view, 2.0f, 0);
        this.scrollToField(view);
    }
    
    private void onPasscodeError(final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(200L);
        }
        if (b) {
            this.inputFields[0].setText((CharSequence)"");
        }
        AndroidUtilities.shakeView((View)this.inputFields[0], 2.0f, 0);
    }
    
    private void onPasswordDone(final boolean b) {
        String string;
        if (b) {
            string = null;
        }
        else {
            string = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty((CharSequence)string)) {
                this.onPasscodeError(false);
                return;
            }
            this.showEditDoneProgress(true, true);
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$aT2vRId8R9seAyB5eJdDvQVsURw(this, b, string));
    }
    
    private void openAddDocumentAlert() {
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<Class<TLRPC.TL_secureValueTypePhone>> list2 = new ArrayList<Class<TLRPC.TL_secureValueTypePhone>>();
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypePhone.class)) {
            list.add(LocaleController.getString("ActionBotDocumentPhone", 2131558506));
            list2.add(TLRPC.TL_secureValueTypePhone.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeEmail.class)) {
            list.add(LocaleController.getString("ActionBotDocumentEmail", 2131558500));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeEmail.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypePersonalDetails.class)) {
            list.add(LocaleController.getString("ActionBotDocumentIdentity", 2131558501));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypePersonalDetails.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypePassport.class)) {
            list.add(LocaleController.getString("ActionBotDocumentPassport", 2131558504));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypePassport.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeInternalPassport.class)) {
            list.add(LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeInternalPassport.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypePassportRegistration.class)) {
            list.add(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypePassportRegistration.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeTemporaryRegistration.class)) {
            list.add(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeTemporaryRegistration.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeIdentityCard.class)) {
            list.add(LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeIdentityCard.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeDriverLicense.class)) {
            list.add(LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeDriverLicense.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeAddress.class)) {
            list.add(LocaleController.getString("ActionBotDocumentAddress", 2131558497));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeAddress.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeUtilityBill.class)) {
            list.add(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeUtilityBill.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeBankStatement.class)) {
            list.add(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeBankStatement.class);
        }
        if (this.hasNotValueForType(TLRPC.TL_secureValueTypeRentalAgreement.class)) {
            list.add(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
            list2.add((Class<TLRPC.TL_secureValueTypePhone>)TLRPC.TL_secureValueTypeRentalAgreement.class);
        }
        if (this.getParentActivity() != null) {
            if (!list.isEmpty()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                builder.setTitle(LocaleController.getString("PassportNoDocumentsAdd", 2131560296));
                builder.setItems(list.toArray(new CharSequence[0]), (DialogInterface$OnClickListener)new _$$Lambda$PassportActivity$_OkER2xuwjfE0_6Y_fzH2uLkAhM(this, list2));
                this.showDialog(builder.create());
            }
        }
    }
    
    private void openAttachMenu() {
        if (this.getParentActivity() == null) {
            return;
        }
        final int uploadingFileType = this.uploadingFileType;
        boolean openWithFrontFaceCamera = false;
        if (uploadingFileType == 0 && this.documents.size() >= 20) {
            this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("PassportUploadMaxReached", 2131560341, LocaleController.formatPluralString("Files", 20)));
            return;
        }
        this.createChatAttachView();
        final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (this.uploadingFileType == 1) {
            openWithFrontFaceCamera = true;
        }
        chatAttachAlert.setOpenWithFrontFaceCamera(openWithFrontFaceCamera);
        this.chatAttachAlert.setMaxSelectedPhotos(this.getMaxSelectedDocuments());
        this.chatAttachAlert.loadGalleryPhotos();
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT == 21 || sdk_INT == 22) {
            AndroidUtilities.hideKeyboard(super.fragmentView.findFocus());
        }
        this.chatAttachAlert.init();
        this.showDialog(this.chatAttachAlert);
    }
    
    private void openTypeActivity(final TLRPC.TL_secureRequiredType key, final TLRPC.TL_secureRequiredType key2, final ArrayList<TLRPC.TL_secureRequiredType> availableDocumentTypes, final boolean documentOnly) {
        int size;
        if (availableDocumentTypes != null) {
            size = availableDocumentTypes.size();
        }
        else {
            size = 0;
        }
        final TLRPC.SecureValueType type = key.type;
        TLRPC.SecureValueType type2;
        if (key2 != null) {
            type2 = key2.type;
        }
        else {
            type2 = null;
        }
        int n;
        if (type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            n = 1;
        }
        else if (type instanceof TLRPC.TL_secureValueTypeAddress) {
            n = 2;
        }
        else if (type instanceof TLRPC.TL_secureValueTypePhone) {
            n = 3;
        }
        else if (type instanceof TLRPC.TL_secureValueTypeEmail) {
            n = 4;
        }
        else {
            n = -1;
        }
        if (n != -1) {
            HashMap<String, String> fieldsErrors;
            if (!documentOnly) {
                fieldsErrors = this.errorsMap.get(this.getNameForType(type));
            }
            else {
                fieldsErrors = null;
            }
            final HashMap<String, String> documentsErrors = this.errorsMap.get(this.getNameForType(type2));
            final TLRPC.TL_secureValue valueByType = this.getValueByType(key, false);
            final TLRPC.TL_secureValue valueByType2 = this.getValueByType(key2, false);
            final TLRPC.TL_account_authorizationForm currentForm = this.currentForm;
            final TLRPC.TL_account_password currentPassword = this.currentPassword;
            final HashMap<String, String> hashMap = this.typesValues.get(key);
            HashMap<String, String> hashMap2;
            if (key2 != null) {
                hashMap2 = this.typesValues.get(key2);
            }
            else {
                hashMap2 = null;
            }
            final PassportActivity passportActivity = new PassportActivity(n, currentForm, currentPassword, key, valueByType, key2, valueByType2, hashMap, hashMap2);
            passportActivity.delegate = (PassportActivityDelegate)new PassportActivityDelegate() {
                private TLRPC.InputSecureFile getInputSecureFile(final SecureDocument secureDocument) {
                    if (secureDocument.inputFile != null) {
                        final TLRPC.TL_inputSecureFileUploaded tl_inputSecureFileUploaded = new TLRPC.TL_inputSecureFileUploaded();
                        final TLRPC.TL_inputFile inputFile = secureDocument.inputFile;
                        tl_inputSecureFileUploaded.id = inputFile.id;
                        tl_inputSecureFileUploaded.parts = inputFile.parts;
                        tl_inputSecureFileUploaded.md5_checksum = inputFile.md5_checksum;
                        tl_inputSecureFileUploaded.file_hash = secureDocument.fileHash;
                        tl_inputSecureFileUploaded.secret = secureDocument.fileSecret;
                        return tl_inputSecureFileUploaded;
                    }
                    final TLRPC.TL_inputSecureFile tl_inputSecureFile = new TLRPC.TL_inputSecureFile();
                    final TLRPC.TL_secureFile secureFile = secureDocument.secureFile;
                    tl_inputSecureFile.id = secureFile.id;
                    tl_inputSecureFile.access_hash = secureFile.access_hash;
                    return tl_inputSecureFile;
                }
                
                private void renameFile(final SecureDocument secureDocument, final TLRPC.TL_secureFile tl_secureFile) {
                    final File pathToAttach = FileLoader.getPathToAttach(secureDocument);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(secureDocument.secureFile.dc_id);
                    sb.append("_");
                    sb.append(secureDocument.secureFile.id);
                    final String string = sb.toString();
                    final File pathToAttach2 = FileLoader.getPathToAttach(tl_secureFile);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(tl_secureFile.dc_id);
                    sb2.append("_");
                    sb2.append(tl_secureFile.id);
                    final String string2 = sb2.toString();
                    pathToAttach.renameTo(pathToAttach2);
                    ImageLoader.getInstance().replaceImageInCache(string, string2, null, false);
                }
                
                @Override
                public void deleteValue(final TLRPC.TL_secureRequiredType tl_secureRequiredType, final TLRPC.TL_secureRequiredType tl_secureRequiredType2, final ArrayList<TLRPC.TL_secureRequiredType> list, final boolean b, final Runnable runnable, final ErrorRunnable errorRunnable) {
                    PassportActivity.this.deleteValueInternal(tl_secureRequiredType, tl_secureRequiredType2, list, b, runnable, errorRunnable, documentOnly);
                }
                
                @Override
                public SecureDocument saveFile(final TLRPC.TL_secureFile tl_secureFile) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(FileLoader.getDirectory(4));
                    sb.append("/");
                    sb.append(tl_secureFile.dc_id);
                    sb.append("_");
                    sb.append(tl_secureFile.id);
                    sb.append(".jpg");
                    final String string = sb.toString();
                    final EncryptionResult access$8400 = PassportActivity.this.createSecureDocument(string);
                    return new SecureDocument(access$8400.secureDocumentKey, tl_secureFile, string, access$8400.fileHash, access$8400.fileSecret);
                }
                
                @Override
                public void saveValue(final TLRPC.TL_secureRequiredType tl_secureRequiredType, final String s, final String s2, final TLRPC.TL_secureRequiredType tl_secureRequiredType2, final String s3, final ArrayList<SecureDocument> list, final SecureDocument secureDocument, final ArrayList<SecureDocument> list2, final SecureDocument secureDocument2, final SecureDocument secureDocument3, final Runnable runnable, final ErrorRunnable errorRunnable) {
                    TLRPC.TL_inputSecureValue value;
                    if (!TextUtils.isEmpty((CharSequence)s2)) {
                        value = new TLRPC.TL_inputSecureValue();
                        value.type = tl_secureRequiredType.type;
                        value.flags |= 0x1;
                        final EncryptionResult access$6900 = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(s2));
                        value.data = new TLRPC.TL_secureData();
                        final TLRPC.TL_secureData data = value.data;
                        data.data = access$6900.encryptedData;
                        data.data_hash = access$6900.fileHash;
                        data.secret = access$6900.fileSecret;
                    }
                    else if (!TextUtils.isEmpty((CharSequence)s)) {
                        final TLRPC.SecureValueType val$type = type;
                        TLRPC.SecurePlainData plain_data;
                        if (val$type instanceof TLRPC.TL_secureValueTypeEmail) {
                            plain_data = new TLRPC.TL_securePlainEmail();
                            ((TLRPC.TL_securePlainEmail)plain_data).email = s;
                        }
                        else {
                            if (!(val$type instanceof TLRPC.TL_secureValueTypePhone)) {
                                return;
                            }
                            plain_data = new TLRPC.TL_securePlainPhone();
                            ((TLRPC.TL_securePlainPhone)plain_data).phone = s;
                        }
                        final TLRPC.TL_inputSecureValue tl_inputSecureValue = new TLRPC.TL_inputSecureValue();
                        tl_inputSecureValue.type = tl_secureRequiredType.type;
                        tl_inputSecureValue.flags |= 0x20;
                        tl_inputSecureValue.plain_data = plain_data;
                        value = tl_inputSecureValue;
                    }
                    else {
                        value = null;
                    }
                    if (!documentOnly && value == null) {
                        if (errorRunnable != null) {
                            errorRunnable.onError(null, null);
                        }
                        return;
                    }
                    TLRPC.TL_inputSecureValue tl_inputSecureValue2 = null;
                    Label_0596: {
                        if (tl_secureRequiredType2 != null) {
                            tl_inputSecureValue2 = new TLRPC.TL_inputSecureValue();
                            tl_inputSecureValue2.type = tl_secureRequiredType2.type;
                            if (!TextUtils.isEmpty((CharSequence)s3)) {
                                tl_inputSecureValue2.flags |= 0x1;
                                final EncryptionResult access$6901 = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(s3));
                                tl_inputSecureValue2.data = new TLRPC.TL_secureData();
                                final TLRPC.TL_secureData data2 = tl_inputSecureValue2.data;
                                data2.data = access$6901.encryptedData;
                                data2.data_hash = access$6901.fileHash;
                                data2.secret = access$6901.fileSecret;
                            }
                            if (secureDocument2 != null) {
                                tl_inputSecureValue2.front_side = this.getInputSecureFile(secureDocument2);
                                tl_inputSecureValue2.flags |= 0x2;
                            }
                            if (secureDocument3 != null) {
                                tl_inputSecureValue2.reverse_side = this.getInputSecureFile(secureDocument3);
                                tl_inputSecureValue2.flags |= 0x4;
                            }
                            if (secureDocument != null) {
                                tl_inputSecureValue2.selfie = this.getInputSecureFile(secureDocument);
                                tl_inputSecureValue2.flags |= 0x8;
                            }
                            if (list2 != null && !list2.isEmpty()) {
                                tl_inputSecureValue2.flags |= 0x40;
                                for (int size = list2.size(), i = 0; i < size; ++i) {
                                    tl_inputSecureValue2.translation.add(this.getInputSecureFile(list2.get(i)));
                                }
                            }
                            if (list != null && !list.isEmpty()) {
                                tl_inputSecureValue2.flags |= 0x10;
                                for (int size2 = list.size(), j = 0; j < size2; ++j) {
                                    tl_inputSecureValue2.files.add(this.getInputSecureFile(list.get(j)));
                                }
                            }
                            if (!documentOnly) {
                                break Label_0596;
                            }
                            value = tl_inputSecureValue2;
                        }
                        tl_inputSecureValue2 = null;
                    }
                    final TLRPC.TL_account_saveSecureValue tl_account_saveSecureValue = new TLRPC.TL_account_saveSecureValue();
                    tl_account_saveSecureValue.value = value;
                    tl_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                    ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_account_saveSecureValue, new RequestDelegate() {
                        final /* synthetic */ PassportActivityDelegate val$currentDelegate;
                        
                        private void onResult(final TLRPC.TL_error tl_error, final TLRPC.TL_secureValue tl_secureValue, final TLRPC.TL_secureValue tl_secureValue2) {
                            final ErrorRunnable val$errorRunnable = errorRunnable;
                            final String val$text = s;
                            final TLRPC.TL_account_saveSecureValue val$req = tl_account_saveSecureValue;
                            final PassportActivityDelegate this$1 = PassportActivityDelegate.this;
                            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$19$1$0kRj7ir3NwvnbVVMIq70mwI_7cA(this, tl_error, val$errorRunnable, val$text, val$req, documentOnly, tl_secureRequiredType2, tl_secureRequiredType, tl_secureValue, tl_secureValue2, list, secureDocument, secureDocument2, secureDocument3, list2, s2, s3, size, runnable));
                        }
                        
                        @Override
                        public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                            if (tl_error != null) {
                                if (tl_error.text.equals("EMAIL_VERIFICATION_NEEDED")) {
                                    final TLRPC.TL_account_sendVerifyEmailCode tl_account_sendVerifyEmailCode = new TLRPC.TL_account_sendVerifyEmailCode();
                                    tl_account_sendVerifyEmailCode.email = s;
                                    ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_account_sendVerifyEmailCode, new _$$Lambda$PassportActivity$19$1$z2A8tnBlw_sIzEUvPsIbORU04FQ(this, s, tl_secureRequiredType, this.val$currentDelegate, errorRunnable));
                                    return;
                                }
                                if (tl_error.text.equals("PHONE_VERIFICATION_NEEDED")) {
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$19$1$PRLUYjV8ZrO0HoPOz4GspIpM_8g(errorRunnable, tl_error, s));
                                    return;
                                }
                            }
                            if (tl_error == null && tl_inputSecureValue2 != null) {
                                final TLRPC.TL_secureValue tl_secureValue = (TLRPC.TL_secureValue)tlObject;
                                final TLRPC.TL_account_saveSecureValue tl_account_saveSecureValue = new TLRPC.TL_account_saveSecureValue();
                                tl_account_saveSecureValue.value = tl_inputSecureValue2;
                                tl_account_saveSecureValue.secure_secret_id = PassportActivity.this.secureSecretId;
                                ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_account_saveSecureValue, new _$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs(this, tl_secureValue));
                            }
                            else {
                                this.onResult(tl_error, (TLRPC.TL_secureValue)tlObject, null);
                            }
                        }
                    });
                }
            };
            passportActivity.currentAccount = super.currentAccount;
            passportActivity.saltedPassword = this.saltedPassword;
            passportActivity.secureSecret = this.secureSecret;
            passportActivity.currentBotId = this.currentBotId;
            passportActivity.fieldsErrors = fieldsErrors;
            passportActivity.documentOnly = documentOnly;
            passportActivity.documentsErrors = documentsErrors;
            passportActivity.availableDocumentTypes = availableDocumentTypes;
            if (n == 4) {
                passportActivity.currentEmail = this.currentEmail;
            }
            this.presentFragment(passportActivity);
        }
    }
    
    private void processSelectedAttach(int sdk_INT) {
        if (sdk_INT == 0) {
            if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                this.getParentActivity().requestPermissions(new String[] { "android.permission.CAMERA" }, 19);
                return;
            }
            try {
                final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                final File generatePicturePath = AndroidUtilities.generatePicturePath();
                if (generatePicturePath != null) {
                    sdk_INT = Build$VERSION.SDK_INT;
                    if (sdk_INT >= 24) {
                        intent.putExtra("output", (Parcelable)FileProvider.getUriForFile((Context)this.getParentActivity(), "org.telegram.messenger.provider", generatePicturePath));
                        intent.addFlags(2);
                        intent.addFlags(1);
                    }
                    else {
                        intent.putExtra("output", (Parcelable)Uri.fromFile(generatePicturePath));
                    }
                    this.currentPicturePath = generatePicturePath.getAbsolutePath();
                }
                this.startActivityForResult(intent, 0);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else if (sdk_INT == 1) {
            if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                this.getParentActivity().requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 4);
                return;
            }
            final PhotoAlbumPickerActivity photoAlbumPickerActivity = new PhotoAlbumPickerActivity(0, false, false, null);
            photoAlbumPickerActivity.setCurrentAccount(super.currentAccount);
            photoAlbumPickerActivity.setMaxSelectedPhotos(this.getMaxSelectedDocuments());
            photoAlbumPickerActivity.setAllowSearchImages(false);
            photoAlbumPickerActivity.setDelegate((PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate)new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
                @Override
                public void didSelectPhotos(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
                    PassportActivity.this.processSelectedFiles(list);
                }
                
                @Override
                public void startPhotoSelectActivity() {
                    try {
                        final Intent intent = new Intent("android.intent.action.PICK");
                        intent.setType("image/*");
                        PassportActivity.this.startActivityForResult(intent, 1);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            });
            this.presentFragment(photoAlbumPickerActivity);
        }
        else if (sdk_INT == 4) {
            if (Build$VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
                this.getParentActivity().requestPermissions(new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 4);
                return;
            }
            final DocumentSelectActivity documentSelectActivity = new DocumentSelectActivity(false);
            documentSelectActivity.setCurrentAccount(super.currentAccount);
            documentSelectActivity.setCanSelectOnlyImageFiles(true);
            documentSelectActivity.setMaxSelectedFiles(this.getMaxSelectedDocuments());
            documentSelectActivity.setDelegate((DocumentSelectActivity.DocumentSelectActivityDelegate)new DocumentSelectActivity.DocumentSelectActivityDelegate() {
                @Override
                public void didSelectFiles(final DocumentSelectActivity documentSelectActivity, final ArrayList<String> list) {
                    documentSelectActivity.finishFragment();
                    final ArrayList<SendMessagesHelper.SendingMediaInfo> list2 = new ArrayList<SendMessagesHelper.SendingMediaInfo>();
                    for (int size = list.size(), i = 0; i < size; ++i) {
                        final SendMessagesHelper.SendingMediaInfo e = new SendMessagesHelper.SendingMediaInfo();
                        e.path = list.get(i);
                        list2.add(e);
                    }
                    PassportActivity.this.processSelectedFiles(list2);
                }
                
                @Override
                public void startDocumentSelectActivity() {
                    try {
                        final Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        if (Build$VERSION.SDK_INT >= 18) {
                            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                        }
                        intent.setType("*/*");
                        PassportActivity.this.startActivityForResult(intent, 21);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            });
            this.presentFragment(documentSelectActivity);
        }
    }
    
    private void processSelectedFiles(final ArrayList<SendMessagesHelper.SendingMediaInfo> list) {
        if (list.isEmpty()) {
            return;
        }
        final int uploadingFileType = this.uploadingFileType;
        boolean b2;
        final boolean b = b2 = false;
        if (uploadingFileType != 1) {
            if (uploadingFileType == 4) {
                b2 = b;
            }
            else {
                b2 = b;
                if (this.currentType.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                    int n = 0;
                    while (true) {
                        final EditTextBoldCursor[] inputFields = this.inputFields;
                        if (n >= inputFields.length) {
                            b2 = true;
                            break;
                        }
                        if (n != 5 && n != 8 && n != 4) {
                            if (n != 6) {
                                if (inputFields[n].length() > 0) {
                                    b2 = b;
                                    break;
                                }
                            }
                        }
                        ++n;
                    }
                }
            }
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$fufT8Jcf6nimD7xnbctUJqRi2PE(this, list, this.uploadingFileType, b2));
    }
    
    private TLRPC.TL_secureValue removeValue(final TLRPC.TL_secureRequiredType tl_secureRequiredType) {
        if (tl_secureRequiredType == null) {
            return null;
        }
        for (int i = 0; i < this.currentForm.values.size(); ++i) {
            if (tl_secureRequiredType.type.getClass() == this.currentForm.values.get(i).type.getClass()) {
                return this.currentForm.values.remove(i);
            }
        }
        return null;
    }
    
    private void scrollToField(View view) {
        while (view != null && this.linearLayout2.indexOfChild(view) < 0) {
            view = (View)view.getParent();
        }
        if (view != null) {
            this.scrollView.smoothScrollTo(0, view.getTop() - (this.scrollView.getMeasuredHeight() - view.getMeasuredHeight()) / 2);
        }
    }
    
    private void setFieldValues(final HashMap<String, String> hashMap, final EditTextBoldCursor editTextBoldCursor, final String key) {
        final String s = hashMap.get(key);
        if (s != null) {
            int n = -1;
            switch (key.hashCode()) {
                case 1481071862: {
                    if (key.equals("country_code")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 475919162: {
                    if (key.equals("expiry_date")) {
                        n = 3;
                        break;
                    }
                    break;
                }
                case -1249512767: {
                    if (key.equals("gender")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case -2006252145: {
                    if (key.equals("residence_country_code")) {
                        n = 1;
                        break;
                    }
                    break;
                }
            }
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            editTextBoldCursor.setText((CharSequence)s);
                        }
                        else {
                            boolean b = false;
                            Label_0243: {
                                if (!TextUtils.isEmpty((CharSequence)s)) {
                                    final String[] split = s.split("\\.");
                                    if (split.length == 3) {
                                        this.currentExpireDate[0] = Utilities.parseInt(split[2]);
                                        this.currentExpireDate[1] = Utilities.parseInt(split[1]);
                                        this.currentExpireDate[2] = Utilities.parseInt(split[0]);
                                        editTextBoldCursor.setText((CharSequence)s);
                                        b = true;
                                        break Label_0243;
                                    }
                                }
                                b = false;
                            }
                            if (!b) {
                                final int[] currentExpireDate = this.currentExpireDate;
                                currentExpireDate[2] = 0;
                                currentExpireDate[currentExpireDate[1] = 0] = 0;
                                editTextBoldCursor.setText((CharSequence)LocaleController.getString("PassportNoExpireDate", 2131560298));
                            }
                        }
                    }
                    else if ("male".equals(s)) {
                        this.currentGender = s;
                        editTextBoldCursor.setText((CharSequence)LocaleController.getString("PassportMale", 2131560284));
                    }
                    else if ("female".equals(s)) {
                        this.currentGender = s;
                        editTextBoldCursor.setText((CharSequence)LocaleController.getString("PassportFemale", 2131560223));
                    }
                }
                else {
                    this.currentResidence = s;
                    final String text = this.languageMap.get(this.currentResidence);
                    if (text != null) {
                        editTextBoldCursor.setText((CharSequence)text);
                    }
                }
            }
            else {
                this.currentCitizeship = s;
                final String text2 = this.languageMap.get(this.currentCitizeship);
                if (text2 != null) {
                    editTextBoldCursor.setText((CharSequence)text2);
                }
            }
        }
        final HashMap<String, String> fieldsErrors = this.fieldsErrors;
        if (fieldsErrors != null) {
            final String errorText = fieldsErrors.get(key);
            if (errorText != null) {
                editTextBoldCursor.setErrorText(errorText);
                this.errorsValues.put(key, editTextBoldCursor.getText().toString());
                return;
            }
        }
        final HashMap<String, String> documentsErrors = this.documentsErrors;
        if (documentsErrors != null) {
            final String errorText2 = documentsErrors.get(key);
            if (errorText2 != null) {
                editTextBoldCursor.setErrorText(errorText2);
                this.errorsValues.put(key, editTextBoldCursor.getText().toString());
            }
        }
    }
    
    private void setTypeValue(TLRPC.TL_secureRequiredType value, String str, final String s, final TLRPC.TL_secureRequiredType tl_secureRequiredType, final String s2, boolean checked, int n) {
        TextDetailSecureCell addField = this.typesViews.get(value);
        if (addField == null) {
            if (this.currentActivityType != 8) {
                return;
            }
            final ArrayList<TLRPC.TL_secureRequiredType> list = new ArrayList<TLRPC.TL_secureRequiredType>();
            if (tl_secureRequiredType != null) {
                list.add(tl_secureRequiredType);
            }
            final LinearLayout linearLayout2 = this.linearLayout2;
            final View child = linearLayout2.getChildAt(linearLayout2.getChildCount() - 6);
            if (child instanceof TextDetailSecureCell) {
                ((TextDetailSecureCell)child).setNeedDivider(true);
            }
            addField = this.addField((Context)this.getParentActivity(), value, list, true, true);
            this.updateManageVisibility();
        }
        final HashMap<String, String> hashMap = this.typesValues.get(value);
        if (tl_secureRequiredType != null) {
            final HashMap<String, String> hashMap2 = this.typesValues.get(tl_secureRequiredType);
        }
        else {
            final HashMap<String, String> hashMap2 = null;
        }
        TLRPC.TL_secureValue valueByType = this.getValueByType(value, true);
        final TLRPC.TL_secureValue valueByType2 = this.getValueByType(tl_secureRequiredType, true);
        if (s != null && this.languageMap == null) {
            this.languageMap = new HashMap<String, String>();
            TLRPC.TL_secureValue tl_secureValue = valueByType;
            try {
                tl_secureValue = valueByType;
                tl_secureValue = valueByType;
                final InputStreamReader in = new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt"));
                tl_secureValue = valueByType;
                final BufferedReader bufferedReader = new BufferedReader(in);
                while (true) {
                    tl_secureValue = valueByType;
                    final String line = bufferedReader.readLine();
                    Label_0301: {
                        if (line == null) {
                            break Label_0301;
                        }
                        tl_secureValue = valueByType;
                        final String[] split = line.split(";");
                        tl_secureValue = valueByType;
                        final HashMap<String, String> languageMap = this.languageMap;
                        try {
                            languageMap.put(split[1], split[2]);
                            continue;
                            tl_secureValue = valueByType;
                            bufferedReader.close();
                            valueByType = tl_secureValue;
                        }
                        catch (Exception ex) {}
                    }
                }
            }
            catch (Exception ex) {
                valueByType = tl_secureValue;
            }
            final Exception ex;
            FileLog.e(ex);
            final TLRPC.TL_secureValue tl_secureValue2 = valueByType;
        }
        else {
            this.languageMap = null;
            final TLRPC.TL_secureValue tl_secureValue2 = valueByType;
        }
        Label_1626: {
            if (str != null) {
                final TLRPC.SecureValueType type = value.type;
                Label_0404: {
                    if (type instanceof TLRPC.TL_secureValueTypePhone) {
                        final PhoneFormat instance = PhoneFormat.getInstance();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(str);
                        str = instance.format(sb.toString());
                        break Label_0404;
                    }
                    if (type instanceof TLRPC.TL_secureValueTypeEmail) {
                        break Label_0404;
                    }
                    break Label_1626;
                }
                break Label_1626;
            }
            if (this.currentActivityType != 8 && tl_secureRequiredType != null && (!TextUtils.isEmpty((CharSequence)s2) || valueByType2 != null)) {
                final Serializable s3 = new StringBuilder();
                if (n > 1) {
                    ((StringBuilder)s3).append(this.getTextForType(tl_secureRequiredType.type));
                    str = (String)s3;
                }
                else {
                    str = (String)s3;
                    if (TextUtils.isEmpty((CharSequence)s2)) {
                        ((StringBuilder)s3).append(LocaleController.getString("PassportDocuments", 2131560216));
                        str = (String)s3;
                    }
                }
            }
            else {
                str = null;
            }
        Label_1612_Outer:
            while (true) {
                Label_0852: {
                    if (s == null && s2 == null) {
                        break Label_0852;
                    }
                    if (hashMap == null) {
                        return;
                    }
                    hashMap.clear();
                    final TLRPC.SecureValueType type2 = value.type;
                    final boolean b = type2 instanceof TLRPC.TL_secureValueTypePersonalDetails;
                    final String s4 = "first_name_native";
                    final String s5 = "middle_name";
                    String[] array;
                    String[] array2;
                    if (b) {
                        if ((this.currentActivityType == 0 && !checked) || (this.currentActivityType == 8 && tl_secureRequiredType == null)) {
                            array = new String[] { "first_name", "middle_name", "last_name", "first_name_native", "middle_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code" };
                        }
                        else {
                            array = null;
                        }
                        final int currentActivityType = this.currentActivityType;
                        if (currentActivityType != 0 && (currentActivityType != 8 || tl_secureRequiredType == null)) {
                            array2 = null;
                        }
                        else {
                            array2 = new String[] { "document_no", "expiry_date" };
                        }
                    }
                    else {
                        if (type2 instanceof TLRPC.TL_secureValueTypeAddress && ((this.currentActivityType == 0 && !checked) || (this.currentActivityType == 8 && tl_secureRequiredType == null))) {
                            array = new String[] { "street_line1", "street_line2", "post_code", "city", "state", "country_code" };
                        }
                        else {
                            array = null;
                        }
                        array2 = null;
                    }
                    if (array == null && array2 == null) {
                        break Label_0852;
                    }
                    final JSONObject jsonObject = null;
                    final int n2 = 0;
                    final String[] array3 = null;
                    final HashMap<String, String> hashMap3 = hashMap;
                    final JSONObject jsonObject2 = jsonObject;
                    break Label_0872;
                }
            Label_1609_Outer:
                while (true) {
                    break Label_1612;
                    String s6 = str;
                Label_0987_Outer:
                    while (true) {
                        int n2 = 0;
                        if (n2 >= 2) {
                            break Label_1609;
                        }
                        Label_0923: {
                            if (n2 != 0) {
                                break Label_0923;
                            }
                            HashMap<String, String> hashMap2 = null;
                            final TLRPC.TL_secureValue tl_secureValue2;
                            String s4 = null;
                            String s5 = null;
                            final String[] array;
                            final String[] array2;
                            String[] array3;
                            HashMap<String, String> hashMap3 = null;
                            JSONObject jsonObject2;
                            HashMap<String, String> hashMap4;
                            HashMap<String, String> hashMap5;
                            String s7;
                            String s8;
                            String s9;
                            Iterator keys;
                            int n3;
                            String string;
                            int hashCode;
                            int n4 = 0;
                            String str2;
                            HashMap<String, String> hashMap6;
                            HashMap<String, String> hashMap7;
                            TLRPC.SecureValueType type3;
                            TLRPC.SecureValueType type4;
                            boolean b2;
                            TextView access$6800;
                            HashMap<String, String> hashMap8;
                            HashMap<String, String> hashMap9;
                            String s10;
                            HashMap<String, String> hashMap10;
                            String s11;
                            Label_1581_Outer:Block_44_Outer:
                            while (true) {
                                if (s == null) {
                                    break Label_0987;
                                }
                                s6 = str;
                                try {
                                    jsonObject2 = new(org.json.JSONObject.class);
                                    s6 = str;
                                    new JSONObject(s);
                                    array3 = array;
                                    hashMap4 = hashMap3;
                                    hashMap5 = hashMap2;
                                    s7 = s4;
                                    s8 = s5;
                                    s9 = str;
                                    Label_1566: {
                                        if (array3 != null) {
                                            if (jsonObject2 != null) {
                                                try {
                                                    keys = jsonObject2.keys();
                                                    while (keys.hasNext()) {
                                                        s6 = keys.next();
                                                        if (n2 == 0) {
                                                            hashMap3.put(s6, jsonObject2.getString(s6));
                                                        }
                                                        else {
                                                            hashMap2.put(s6, jsonObject2.getString(s6));
                                                        }
                                                    }
                                                }
                                                catch (Throwable t) {
                                                    s6 = str;
                                                    FileLog.e(t);
                                                }
                                                n3 = 0;
                                                while (true) {
                                                    hashMap4 = hashMap3;
                                                    hashMap5 = hashMap2;
                                                    s7 = s4;
                                                    s8 = s5;
                                                    s9 = str;
                                                    s6 = str;
                                                    if (n3 >= array3.length) {
                                                        break Label_1566;
                                                    }
                                                    s6 = str;
                                                    if (jsonObject2.has(array3[n3])) {
                                                        if (str == null) {
                                                            s6 = str;
                                                            s6 = str;
                                                            str = (String)new StringBuilder();
                                                        }
                                                        try {
                                                            string = jsonObject2.getString(array3[n3]);
                                                            if (string != null && !TextUtils.isEmpty((CharSequence)string) && !s4.equals(array3[n3]) && !"middle_name_native".equals(array3[n3])) {
                                                                if (!"last_name_native".equals(array3[n3])) {
                                                                    if (((StringBuilder)str).length() > 0) {
                                                                        if (!"last_name".equals(array3[n3]) && !"last_name_native".equals(array3[n3]) && !s5.equals(array3[n3]) && !"middle_name_native".equals(array3[n3])) {
                                                                            ((StringBuilder)str).append(", ");
                                                                        }
                                                                        else {
                                                                            ((StringBuilder)str).append(" ");
                                                                        }
                                                                    }
                                                                    s6 = array3[n3];
                                                                    hashCode = s6.hashCode();
                                                                    Label_1441: {
                                                                        if (hashCode != -2006252145) {
                                                                            if (hashCode != -1249512767) {
                                                                                if (hashCode == 1481071862) {
                                                                                    if (s6.equals("country_code")) {
                                                                                        n4 = 0;
                                                                                        break Label_1441;
                                                                                    }
                                                                                }
                                                                            }
                                                                            else if (s6.equals("gender")) {
                                                                                n4 = 2;
                                                                                break Label_1441;
                                                                            }
                                                                        }
                                                                        else if (s6.equals("residence_country_code")) {
                                                                            n4 = 1;
                                                                            break Label_1441;
                                                                        }
                                                                        n4 = -1;
                                                                    }
                                                                    if (n4 != 0 && n4 != 1) {
                                                                        if (n4 != 2) {
                                                                            ((StringBuilder)str).append(string);
                                                                        }
                                                                        else if ("male".equals(string)) {
                                                                            ((StringBuilder)str).append(LocaleController.getString("PassportMale", 2131560284));
                                                                        }
                                                                        else if ("female".equals(string)) {
                                                                            ((StringBuilder)str).append(LocaleController.getString("PassportFemale", 2131560223));
                                                                        }
                                                                    }
                                                                    else {
                                                                        str2 = this.languageMap.get(string);
                                                                        if (str2 != null) {
                                                                            ((StringBuilder)str).append(str2);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        catch (Exception ex2) {
                                                            break;
                                                        }
                                                    }
                                                    ++n3;
                                                }
                                                if (str != null) {
                                                    str = ((StringBuilder)str).toString();
                                                }
                                                else {
                                                    str = null;
                                                }
                                                if (!checked) {
                                                    hashMap6 = this.errorsMap.get(this.getNameForType(value.type));
                                                }
                                                else {
                                                    hashMap6 = null;
                                                }
                                                if (tl_secureRequiredType != null) {
                                                    hashMap7 = this.errorsMap.get(this.getNameForType(tl_secureRequiredType.type));
                                                }
                                                else {
                                                    hashMap7 = null;
                                                }
                                                if ((hashMap6 != null && hashMap6.size() > 0) || (hashMap7 != null && hashMap7.size() > 0)) {
                                                    if (!checked) {
                                                        value = (TLRPC.TL_secureRequiredType)this.mainErrorsMap.get(this.getNameForType(value.type));
                                                    }
                                                    else {
                                                        value = null;
                                                    }
                                                    if (value == null) {
                                                        value = (TLRPC.TL_secureRequiredType)this.mainErrorsMap.get(this.getNameForType(tl_secureRequiredType.type));
                                                    }
                                                    n = 1;
                                                }
                                                else {
                                                    type3 = value.type;
                                                    Label_2180: {
                                                        Label_1937: {
                                                            if (type3 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                                                if (!TextUtils.isEmpty((CharSequence)str)) {
                                                                    break Label_1937;
                                                                }
                                                                if (tl_secureRequiredType == null) {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportPersonalDetailsInfo", 2131560302);
                                                                }
                                                                else if (this.currentActivityType == 8) {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportDocuments", 2131560216);
                                                                }
                                                                else if (n == 1) {
                                                                    value = (TLRPC.TL_secureRequiredType)tl_secureRequiredType.type;
                                                                    if (value instanceof TLRPC.TL_secureValueTypePassport) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportIdentityPassport", 2131560232);
                                                                    }
                                                                    else if (value instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportIdentityInternalPassport", 2131560231);
                                                                    }
                                                                    else if (value instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportIdentityDriverLicence", 2131560229);
                                                                    }
                                                                    else {
                                                                        if (!(value instanceof TLRPC.TL_secureValueTypeIdentityCard)) {
                                                                            break Label_1937;
                                                                        }
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportIdentityID", 2131560230);
                                                                    }
                                                                }
                                                                else {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportIdentityDocumentInfo", 2131560228);
                                                                }
                                                            }
                                                            else if (type3 instanceof TLRPC.TL_secureValueTypeAddress) {
                                                                if (!TextUtils.isEmpty((CharSequence)str)) {
                                                                    break Label_1937;
                                                                }
                                                                if (tl_secureRequiredType == null) {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddressNoUploadInfo", 2131560192);
                                                                }
                                                                else if (this.currentActivityType == 8) {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportDocuments", 2131560216);
                                                                }
                                                                else if (n == 1) {
                                                                    type4 = tl_secureRequiredType.type;
                                                                    if (type4 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddAgreementInfo", 2131560166);
                                                                    }
                                                                    else if (type4 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddBillInfo", 2131560170);
                                                                    }
                                                                    else if (type4 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddPassportRegistrationInfo", 2131560180);
                                                                    }
                                                                    else if (type4 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131560182);
                                                                    }
                                                                    else {
                                                                        value = (TLRPC.TL_secureRequiredType)str;
                                                                        if (type4 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                                                                            value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddBankInfo", 2131560168);
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportAddressInfo", 2131560191);
                                                                }
                                                            }
                                                            else if (type3 instanceof TLRPC.TL_secureValueTypePhone) {
                                                                value = (TLRPC.TL_secureRequiredType)str;
                                                                if (TextUtils.isEmpty((CharSequence)str)) {
                                                                    value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportPhoneInfo", 2131560305);
                                                                }
                                                            }
                                                            else {
                                                                value = (TLRPC.TL_secureRequiredType)str;
                                                                if (type3 instanceof TLRPC.TL_secureValueTypeEmail) {
                                                                    value = (TLRPC.TL_secureRequiredType)str;
                                                                    if (TextUtils.isEmpty((CharSequence)str)) {
                                                                        value = (TLRPC.TL_secureRequiredType)LocaleController.getString("PassportEmailInfo", 2131560219);
                                                                    }
                                                                }
                                                            }
                                                            break Label_2180;
                                                        }
                                                        value = (TLRPC.TL_secureRequiredType)str;
                                                    }
                                                    n = 0;
                                                }
                                                b2 = true;
                                                addField.setValue((CharSequence)value);
                                                access$6800 = addField.valueTextView;
                                                if (n != 0) {
                                                    value = (TLRPC.TL_secureRequiredType)"windowBackgroundWhiteRedText3";
                                                }
                                                else {
                                                    value = (TLRPC.TL_secureRequiredType)"windowBackgroundWhiteGrayText2";
                                                }
                                                access$6800.setTextColor(Theme.getColor((String)value));
                                                Label_2280: {
                                                    if (n == 0 && this.currentActivityType != 8 && ((checked && tl_secureRequiredType != null) || (!checked && tl_secureValue2 != null))) {
                                                        checked = b2;
                                                        if (tl_secureRequiredType == null) {
                                                            break Label_2280;
                                                        }
                                                        if (valueByType2 != null) {
                                                            checked = b2;
                                                            break Label_2280;
                                                        }
                                                    }
                                                    checked = false;
                                                }
                                                addField.setChecked(checked);
                                                return;
                                            }
                                            hashMap4 = hashMap3;
                                            hashMap5 = hashMap2;
                                            s7 = s4;
                                            s8 = s5;
                                            s9 = str;
                                        }
                                    }
                                    hashMap8 = hashMap4;
                                    hashMap9 = hashMap5;
                                    s10 = s7;
                                    str = s8;
                                    // iftrue(Label_0961:, hashMap2 != null)
                                    while (true) {
                                        while (true) {
                                            ++n2;
                                            s6 = s10;
                                            hashMap3 = hashMap8;
                                            hashMap2 = hashMap9;
                                            s4 = s6;
                                            s5 = str;
                                            str = s9;
                                            continue Label_1612_Outer;
                                            hashMap10 = hashMap2;
                                            s11 = s4;
                                            s6 = s5;
                                            s9 = str;
                                            hashMap8 = hashMap3;
                                            hashMap9 = hashMap10;
                                            s10 = s11;
                                            str = s6;
                                            continue Block_44_Outer;
                                        }
                                        continue;
                                    }
                                    // iftrue(Label_0987:, s2 == null)
                                    Block_45: {
                                        break Block_45;
                                        str = s6;
                                        continue Label_1609_Outer;
                                    }
                                    s6 = str;
                                    jsonObject2 = new JSONObject(s2);
                                    array3 = array2;
                                    continue Label_1581_Outer;
                                }
                                catch (Exception ex3) {
                                    continue Label_0987_Outer;
                                }
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    private void showAlertWithText(final String title, final String message) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        builder.setTitle(title);
        builder.setMessage(message);
        this.showDialog(builder.create());
    }
    
    private void showAttachmentError() {
        if (this.getParentActivity() == null) {
            return;
        }
        Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("UnsupportedAttachment", 2131560946), 0).show();
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
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.doneItem.getImageView().setVisibility(0);
                this.doneItem.setEnabled(true);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.ALPHA, new float[] { 1.0f }) });
            }
            this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(obj)) {
                        PassportActivity.this.doneItemAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(obj)) {
                        if (!b2) {
                            PassportActivity.this.progressView.setVisibility(4);
                        }
                        else {
                            PassportActivity.this.doneItem.getImageView().setVisibility(4);
                        }
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
        else if (this.acceptTextView != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (b2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.acceptTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressViewButton, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.acceptTextView, View.ALPHA, new float[] { 1.0f }) });
            }
            this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(obj)) {
                        PassportActivity.this.doneItemAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(obj)) {
                        if (!b2) {
                            PassportActivity.this.progressViewButton.setVisibility(4);
                        }
                        else {
                            PassportActivity.this.acceptTextView.setVisibility(4);
                        }
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
    }
    
    private void startPhoneVerification(final boolean b, final String s, Runnable pendingFinishRunnable, final ErrorRunnable pendingErrorRunnable, final PassportActivityDelegate pendingDelegate) {
        final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
        final boolean b2 = telephonyManager.getSimState() != 1 && telephonyManager.getPhoneType() != 0;
        boolean b4;
        if (this.getParentActivity() != null && Build$VERSION.SDK_INT >= 23 && b2) {
            final boolean b3 = b4 = (this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0);
            if (b) {
                this.permissionsItems.clear();
                if (!b3) {
                    this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                }
                b4 = b3;
                if (!this.permissionsItems.isEmpty()) {
                    if (this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        builder.setMessage(LocaleController.getString("AllowReadCall", 2131558607));
                        this.permissionsDialog = this.showDialog(builder.create());
                    }
                    else {
                        this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
                    }
                    this.pendingPhone = s;
                    this.pendingErrorRunnable = pendingErrorRunnable;
                    this.pendingFinishRunnable = pendingFinishRunnable;
                    this.pendingDelegate = pendingDelegate;
                    return;
                }
            }
        }
        else {
            b4 = true;
        }
        pendingFinishRunnable = (Runnable)new TLRPC.TL_account_sendVerifyPhoneCode();
        ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).phone_number = s;
        ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings = new TLRPC.TL_codeSettings();
        ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.allow_flashcall = (b2 && b4);
        if (Build$VERSION.SDK_INT >= 26) {
            try {
                ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.app_hash = SmsManager.getDefault().createAppSpecificSmsToken(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class)SmsReceiver.class), 134217728));
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        else {
            final TLRPC.TL_codeSettings settings = ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings;
            settings.app_hash = BuildVars.SMS_HASH;
            settings.app_hash_persistent = true;
        }
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        if (!TextUtils.isEmpty((CharSequence)((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.app_hash)) {
            final TLRPC.TL_codeSettings settings2 = ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings;
            settings2.flags |= 0x8;
            sharedPreferences.edit().putString("sms_hash", ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.app_hash).commit();
        }
        else {
            sharedPreferences.edit().remove("sms_hash").commit();
        }
        if (((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.allow_flashcall) {
            try {
                final String line1Number = telephonyManager.getLine1Number();
                if (!TextUtils.isEmpty((CharSequence)line1Number)) {
                    if (!(((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.current_number = PhoneNumberUtils.compare(s, line1Number))) {
                        ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.allow_flashcall = false;
                    }
                }
                else {
                    ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.current_number = false;
                }
            }
            catch (Exception ex) {
                ((TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable).settings.allow_flashcall = false;
                FileLog.e(ex);
            }
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest((TLObject)pendingFinishRunnable, new _$$Lambda$PassportActivity$ziNY0SahWhkV1q9_2nyx_UlZ_Kc(this, s, pendingDelegate, (TLRPC.TL_account_sendVerifyPhoneCode)pendingFinishRunnable), 2);
    }
    
    private void updateInterfaceStringsForDocumentType() {
        final TLRPC.TL_secureRequiredType currentDocumentsType = this.currentDocumentsType;
        if (currentDocumentsType != null) {
            super.actionBar.setTitle(this.getTextForType(currentDocumentsType.type));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("PassportPersonal", 2131560300));
        }
        this.updateUploadText(2);
        this.updateUploadText(3);
        this.updateUploadText(1);
        this.updateUploadText(4);
    }
    
    private void updateManageVisibility() {
        if (this.currentForm.values.isEmpty()) {
            this.emptyLayout.setVisibility(0);
            this.sectionCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.addDocumentCell.setVisibility(8);
            this.deletePassportCell.setVisibility(8);
            this.addDocumentSectionCell.setVisibility(8);
        }
        else {
            this.emptyLayout.setVisibility(8);
            this.sectionCell.setVisibility(0);
            this.headerCell.setVisibility(0);
            this.deletePassportCell.setVisibility(0);
            this.addDocumentSectionCell.setVisibility(0);
            if (this.hasUnfilledValues()) {
                this.addDocumentCell.setVisibility(0);
            }
            else {
                this.addDocumentCell.setVisibility(8);
            }
        }
    }
    
    private void updatePasswordInterface() {
        if (this.noPasswordImageView == null) {
            return;
        }
        final TLRPC.TL_account_password currentPassword = this.currentPassword;
        if (currentPassword != null && this.usingSavedPassword == 0) {
            if (!currentPassword.has_password) {
                this.passwordRequestTextView.setVisibility(0);
                this.noPasswordImageView.setVisibility(0);
                this.noPasswordTextView.setVisibility(0);
                this.noPasswordSetTextView.setVisibility(0);
                this.passwordAvatarContainer.setVisibility(8);
                this.inputFieldContainers[0].setVisibility(8);
                this.doneItem.setVisibility(8);
                this.passwordForgotButton.setVisibility(8);
                this.passwordInfoRequestTextView.setVisibility(8);
                this.passwordRequestTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 25.0f, 0.0f, 0.0f));
                this.emptyView.setVisibility(8);
            }
            else {
                this.passwordRequestTextView.setVisibility(0);
                this.noPasswordImageView.setVisibility(8);
                this.noPasswordTextView.setVisibility(8);
                this.noPasswordSetTextView.setVisibility(8);
                this.emptyView.setVisibility(8);
                this.passwordAvatarContainer.setVisibility(0);
                this.inputFieldContainers[0].setVisibility(0);
                this.doneItem.setVisibility(0);
                this.passwordForgotButton.setVisibility(0);
                this.passwordInfoRequestTextView.setVisibility(0);
                this.passwordRequestTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
                if (this.inputFields != null) {
                    final TLRPC.TL_account_password currentPassword2 = this.currentPassword;
                    if (currentPassword2 != null && !TextUtils.isEmpty((CharSequence)currentPassword2.hint)) {
                        this.inputFields[0].setHint((CharSequence)this.currentPassword.hint);
                    }
                    else {
                        this.inputFields[0].setHint((CharSequence)LocaleController.getString("LoginPassword", 2131559788));
                    }
                }
            }
        }
        else {
            this.noPasswordImageView.setVisibility(8);
            this.noPasswordTextView.setVisibility(8);
            this.noPasswordSetTextView.setVisibility(8);
            this.passwordAvatarContainer.setVisibility(8);
            this.inputFieldContainers[0].setVisibility(8);
            this.doneItem.setVisibility(8);
            this.passwordForgotButton.setVisibility(8);
            this.passwordInfoRequestTextView.setVisibility(8);
            this.passwordRequestTextView.setVisibility(8);
            this.emptyView.setVisibility(0);
        }
    }
    
    private void updateUploadText(int visibility) {
        final boolean b = true;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        if (visibility == 0) {
            if (this.uploadDocumentCell == null) {
                return;
            }
            if (this.documents.size() >= 1) {
                this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131560339), false);
            }
            else {
                this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadDocument", 2131560340), false);
            }
        }
        else if (visibility == 1) {
            final TextDetailSettingsCell uploadSelfieCell = this.uploadSelfieCell;
            if (uploadSelfieCell == null) {
                return;
            }
            visibility = n3;
            if (this.selfieDocument != null) {
                visibility = 8;
            }
            uploadSelfieCell.setVisibility(visibility);
        }
        else if (visibility == 4) {
            if (this.uploadTranslationCell == null) {
                return;
            }
            if (this.translationDocuments.size() >= 1) {
                this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131560339), false);
            }
            else {
                this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadDocument", 2131560340), false);
            }
        }
        else if (visibility == 2) {
            if (this.uploadFrontCell == null) {
                return;
            }
            final TLRPC.TL_secureRequiredType currentDocumentsType = this.currentDocumentsType;
            boolean b2 = false;
            Label_0246: {
                if (currentDocumentsType != null) {
                    b2 = b;
                    if (currentDocumentsType.selfie_required) {
                        break Label_0246;
                    }
                    final TLRPC.SecureValueType type = currentDocumentsType.type;
                    b2 = b;
                    if (type instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                        break Label_0246;
                    }
                    if (type instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                        b2 = b;
                        break Label_0246;
                    }
                }
                b2 = false;
            }
            final TLRPC.SecureValueType type2 = this.currentDocumentsType.type;
            if (!(type2 instanceof TLRPC.TL_secureValueTypePassport) && !(type2 instanceof TLRPC.TL_secureValueTypeInternalPassport)) {
                this.uploadFrontCell.setTextAndValue(LocaleController.getString("PassportFrontSide", 2131560224), LocaleController.getString("PassportFrontSideInfo", 2131560225), b2);
            }
            else {
                this.uploadFrontCell.setTextAndValue(LocaleController.getString("PassportMainPage", 2131560282), LocaleController.getString("PassportMainPageInfo", 2131560283), b2);
            }
            final TextDetailSettingsCell uploadFrontCell = this.uploadFrontCell;
            visibility = n;
            if (this.frontDocument != null) {
                visibility = 8;
            }
            uploadFrontCell.setVisibility(visibility);
        }
        else if (visibility == 3) {
            if (this.uploadReverseCell == null) {
                return;
            }
            final TLRPC.SecureValueType type3 = this.currentDocumentsType.type;
            if (!(type3 instanceof TLRPC.TL_secureValueTypeIdentityCard) && !(type3 instanceof TLRPC.TL_secureValueTypeDriverLicense)) {
                this.reverseLayout.setVisibility(8);
                this.uploadReverseCell.setVisibility(8);
            }
            else {
                this.reverseLayout.setVisibility(0);
                final TextDetailSettingsCell uploadReverseCell = this.uploadReverseCell;
                visibility = n2;
                if (this.reverseDocument != null) {
                    visibility = 8;
                }
                uploadReverseCell.setVisibility(visibility);
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            private boolean onIdentityDone(final Runnable p0, final ErrorRunnable p1) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //     4: invokestatic    org/telegram/ui/PassportActivity.access$2100:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //     7: invokevirtual   java/util/HashMap.isEmpty:()Z
                //    10: istore_3       
                //    11: iconst_0       
                //    12: istore          4
                //    14: iload_3        
                //    15: ifeq            1471
                //    18: aload_0        
                //    19: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    22: invokestatic    org/telegram/ui/PassportActivity.access$2200:(Lorg/telegram/ui/PassportActivity;)Z
                //    25: ifeq            31
                //    28: goto            1471
                //    31: aload_0        
                //    32: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    35: invokestatic    org/telegram/ui/PassportActivity.access$2300:(Lorg/telegram/ui/PassportActivity;)Z
                //    38: ifeq            481
                //    41: aload_0        
                //    42: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    45: iconst_0       
                //    46: invokestatic    org/telegram/ui/PassportActivity.access$2302:(Lorg/telegram/ui/PassportActivity;Z)Z
                //    49: pop            
                //    50: iconst_0       
                //    51: istore          5
                //    53: iconst_0       
                //    54: istore          6
                //    56: iload           6
                //    58: aload_0        
                //    59: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    62: invokestatic    org/telegram/ui/PassportActivity.access$2400:(Lorg/telegram/ui/PassportActivity;)[Z
                //    65: arraylength    
                //    66: if_icmpge       474
                //    69: iload           5
                //    71: istore          7
                //    73: aload_0        
                //    74: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    77: invokestatic    org/telegram/ui/PassportActivity.access$2400:(Lorg/telegram/ui/PassportActivity;)[Z
                //    80: iload           6
                //    82: baload         
                //    83: ifeq            464
                //    86: aload_0        
                //    87: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    90: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //    93: iload           6
                //    95: aaload         
                //    96: ldc             "PassportUseLatinOnly"
                //    98: ldc             2131560343
                //   100: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   103: invokevirtual   org/telegram/ui/Components/EditTextBoldCursor.setErrorText:(Ljava/lang/CharSequence;)V
                //   106: iload           5
                //   108: istore          7
                //   110: iload           5
                //   112: ifne            464
                //   115: aload_0        
                //   116: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   119: invokestatic    org/telegram/ui/PassportActivity.access$2400:(Lorg/telegram/ui/PassportActivity;)[Z
                //   122: iconst_0       
                //   123: baload         
                //   124: ifeq            156
                //   127: aload_0        
                //   128: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   131: astore          8
                //   133: aload           8
                //   135: aload           8
                //   137: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   140: iconst_0       
                //   141: aaload         
                //   142: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   145: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   148: invokestatic    org/telegram/ui/PassportActivity.access$2700:(Lorg/telegram/ui/PassportActivity;Ljava/lang/String;)Ljava/lang/String;
                //   151: astore          8
                //   153: goto            173
                //   156: aload_0        
                //   157: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   160: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   163: iconst_0       
                //   164: aaload         
                //   165: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   168: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   171: astore          8
                //   173: aload_0        
                //   174: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   177: invokestatic    org/telegram/ui/PassportActivity.access$2400:(Lorg/telegram/ui/PassportActivity;)[Z
                //   180: iconst_1       
                //   181: baload         
                //   182: ifeq            214
                //   185: aload_0        
                //   186: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   189: astore          9
                //   191: aload           9
                //   193: aload           9
                //   195: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   198: iconst_1       
                //   199: aaload         
                //   200: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   203: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   206: invokestatic    org/telegram/ui/PassportActivity.access$2700:(Lorg/telegram/ui/PassportActivity;Ljava/lang/String;)Ljava/lang/String;
                //   209: astore          9
                //   211: goto            231
                //   214: aload_0        
                //   215: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   218: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   221: iconst_1       
                //   222: aaload         
                //   223: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   226: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   229: astore          9
                //   231: aload_0        
                //   232: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   235: invokestatic    org/telegram/ui/PassportActivity.access$2400:(Lorg/telegram/ui/PassportActivity;)[Z
                //   238: iconst_2       
                //   239: baload         
                //   240: ifeq            272
                //   243: aload_0        
                //   244: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   247: astore          10
                //   249: aload           10
                //   251: aload           10
                //   253: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   256: iconst_2       
                //   257: aaload         
                //   258: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   261: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   264: invokestatic    org/telegram/ui/PassportActivity.access$2700:(Lorg/telegram/ui/PassportActivity;Ljava/lang/String;)Ljava/lang/String;
                //   267: astore          10
                //   269: goto            289
                //   272: aload_0        
                //   273: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   276: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   279: iconst_2       
                //   280: aaload         
                //   281: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   284: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   287: astore          10
                //   289: aload           8
                //   291: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
                //   294: ifne            442
                //   297: aload           9
                //   299: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
                //   302: ifne            442
                //   305: aload           10
                //   307: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
                //   310: ifne            442
                //   313: new             Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   316: dup            
                //   317: aload_0        
                //   318: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   321: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
                //   324: invokespecial   org/telegram/ui/ActionBar/AlertDialog$Builder.<init>:(Landroid/content/Context;)V
                //   327: astore          11
                //   329: aload           11
                //   331: ldc             "PassportNameCheckAlert"
                //   333: ldc             2131560289
                //   335: iconst_3       
                //   336: anewarray       Ljava/lang/Object;
                //   339: dup            
                //   340: iconst_0       
                //   341: aload           8
                //   343: aastore        
                //   344: dup            
                //   345: iconst_1       
                //   346: aload           9
                //   348: aastore        
                //   349: dup            
                //   350: iconst_2       
                //   351: aload           10
                //   353: aastore        
                //   354: invokestatic    org/telegram/messenger/LocaleController.formatString:(Ljava/lang/String;I[Ljava/lang/Object;)Ljava/lang/String;
                //   357: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setMessage:(Ljava/lang/CharSequence;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   360: pop            
                //   361: aload           11
                //   363: ldc             "AppName"
                //   365: ldc             2131558635
                //   367: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   370: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setTitle:(Ljava/lang/CharSequence;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   373: pop            
                //   374: aload           11
                //   376: ldc             "Done"
                //   378: ldc             2131559299
                //   380: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   383: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$hBvwZ_d4QGDnNuXdFnmSB9952Bs;
                //   386: dup            
                //   387: aload_0        
                //   388: aload           8
                //   390: aload           9
                //   392: aload           10
                //   394: aload_1        
                //   395: aload_2        
                //   396: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$hBvwZ_d4QGDnNuXdFnmSB9952Bs.<init>:(Lorg/telegram/ui/PassportActivity$3;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)V
                //   399: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setPositiveButton:(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   402: pop            
                //   403: aload           11
                //   405: ldc             "Edit"
                //   407: ldc             2131559301
                //   409: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   412: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$tZIb58L3Zb4a9cJUHmxI2DCu8M8;
                //   415: dup            
                //   416: aload_0        
                //   417: iload           6
                //   419: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$tZIb58L3Zb4a9cJUHmxI2DCu8M8.<init>:(Lorg/telegram/ui/PassportActivity$3;I)V
                //   422: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setNegativeButton:(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   425: pop            
                //   426: aload_0        
                //   427: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   430: aload           11
                //   432: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.create:()Lorg/telegram/ui/ActionBar/AlertDialog;
                //   435: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.showDialog:(Landroid/app/Dialog;)Landroid/app/Dialog;
                //   438: pop            
                //   439: goto            461
                //   442: aload_0        
                //   443: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   446: astore          8
                //   448: aload           8
                //   450: aload           8
                //   452: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   455: iload           6
                //   457: aaload         
                //   458: invokestatic    org/telegram/ui/PassportActivity.access$2800:(Lorg/telegram/ui/PassportActivity;Landroid/view/View;)V
                //   461: iconst_1       
                //   462: istore          7
                //   464: iinc            6, 1
                //   467: iload           7
                //   469: istore          5
                //   471: goto            56
                //   474: iload           5
                //   476: ifeq            481
                //   479: iconst_0       
                //   480: ireturn        
                //   481: aload_0        
                //   482: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   485: invokestatic    org/telegram/ui/PassportActivity.access$2900:(Lorg/telegram/ui/PassportActivity;)Z
                //   488: ifeq            500
                //   491: aload_0        
                //   492: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   495: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.finishFragment:()V
                //   498: iconst_0       
                //   499: ireturn        
                //   500: aconst_null    
                //   501: astore          11
                //   503: aload_0        
                //   504: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   507: invokestatic    org/telegram/ui/PassportActivity.access$3000:(Lorg/telegram/ui/PassportActivity;)Z
                //   510: ifne            967
                //   513: new             Ljava/util/HashMap;
                //   516: astore          12
                //   518: aload           12
                //   520: aload_0        
                //   521: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   524: invokestatic    org/telegram/ui/PassportActivity.access$3100:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   527: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
                //   530: aload_0        
                //   531: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   534: invokestatic    org/telegram/ui/PassportActivity.access$3200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   537: getfield        org/telegram/tgnet/TLRPC$TL_secureRequiredType.native_names:Z
                //   540: ifeq            701
                //   543: aload_0        
                //   544: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   547: invokestatic    org/telegram/ui/PassportActivity.access$3300:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/ui/Cells/TextInfoPrivacyCell;
                //   550: invokevirtual   android/widget/FrameLayout.getVisibility:()I
                //   553: istore          6
                //   555: iload           6
                //   557: ifne            632
                //   560: aload           12
                //   562: ldc             "first_name_native"
                //   564: aload_0        
                //   565: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   568: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   571: iconst_0       
                //   572: aaload         
                //   573: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   576: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   579: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   582: pop            
                //   583: aload           12
                //   585: ldc             "middle_name_native"
                //   587: aload_0        
                //   588: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   591: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   594: iconst_1       
                //   595: aaload         
                //   596: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   599: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   602: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   605: pop            
                //   606: aload           12
                //   608: ldc             "last_name_native"
                //   610: aload_0        
                //   611: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   614: invokestatic    org/telegram/ui/PassportActivity.access$2600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   617: iconst_2       
                //   618: aaload         
                //   619: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   622: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   625: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   628: pop            
                //   629: goto            701
                //   632: aload           12
                //   634: ldc             "first_name_native"
                //   636: aload_0        
                //   637: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   640: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   643: iconst_0       
                //   644: aaload         
                //   645: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   648: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   651: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   654: pop            
                //   655: aload           12
                //   657: ldc             "middle_name_native"
                //   659: aload_0        
                //   660: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   663: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   666: iconst_1       
                //   667: aaload         
                //   668: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   671: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   674: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   677: pop            
                //   678: aload           12
                //   680: ldc             "last_name_native"
                //   682: aload_0        
                //   683: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   686: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   689: iconst_2       
                //   690: aaload         
                //   691: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   694: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   697: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   700: pop            
                //   701: aload           12
                //   703: ldc             "first_name"
                //   705: aload_0        
                //   706: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   709: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   712: iconst_0       
                //   713: aaload         
                //   714: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   717: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   720: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   723: pop            
                //   724: aload           12
                //   726: ldc             "middle_name"
                //   728: aload_0        
                //   729: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   732: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   735: iconst_1       
                //   736: aaload         
                //   737: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   740: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   743: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   746: pop            
                //   747: aload           12
                //   749: ldc             "last_name"
                //   751: aload_0        
                //   752: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   755: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   758: iconst_2       
                //   759: aaload         
                //   760: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   763: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   766: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   769: pop            
                //   770: aload           12
                //   772: ldc             "birth_date"
                //   774: aload_0        
                //   775: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   778: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   781: iconst_3       
                //   782: aaload         
                //   783: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   786: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   789: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   792: pop            
                //   793: aload           12
                //   795: ldc             "gender"
                //   797: aload_0        
                //   798: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   801: invokestatic    org/telegram/ui/PassportActivity.access$3400:(Lorg/telegram/ui/PassportActivity;)Ljava/lang/String;
                //   804: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   807: pop            
                //   808: aload           12
                //   810: ldc             "country_code"
                //   812: aload_0        
                //   813: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   816: invokestatic    org/telegram/ui/PassportActivity.access$3500:(Lorg/telegram/ui/PassportActivity;)Ljava/lang/String;
                //   819: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   822: pop            
                //   823: aload           12
                //   825: ldc             "residence_country_code"
                //   827: aload_0        
                //   828: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   831: invokestatic    org/telegram/ui/PassportActivity.access$3600:(Lorg/telegram/ui/PassportActivity;)Ljava/lang/String;
                //   834: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //   837: pop            
                //   838: new             Lorg/json/JSONObject;
                //   841: astore          10
                //   843: aload           10
                //   845: invokespecial   org/json/JSONObject.<init>:()V
                //   848: aload           10
                //   850: astore          9
                //   852: new             Ljava/util/ArrayList;
                //   855: astore          13
                //   857: aload           10
                //   859: astore          9
                //   861: aload           13
                //   863: aload           12
                //   865: invokevirtual   java/util/HashMap.keySet:()Ljava/util/Set;
                //   868: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
                //   871: aload           10
                //   873: astore          9
                //   875: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$mGr_6qgzmzLZw5uyzhfjuEIAkBg;
                //   878: astore          8
                //   880: aload           10
                //   882: astore          9
                //   884: aload           8
                //   886: aload_0        
                //   887: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$mGr_6qgzmzLZw5uyzhfjuEIAkBg.<init>:(Lorg/telegram/ui/PassportActivity$3;)V
                //   890: aload           10
                //   892: astore          9
                //   894: aload           13
                //   896: aload           8
                //   898: invokestatic    java/util/Collections.sort:(Ljava/util/List;Ljava/util/Comparator;)V
                //   901: aload           10
                //   903: astore          9
                //   905: aload           13
                //   907: invokevirtual   java/util/ArrayList.size:()I
                //   910: istore          5
                //   912: iconst_0       
                //   913: istore          6
                //   915: aload           10
                //   917: astore          8
                //   919: iload           6
                //   921: iload           5
                //   923: if_icmpge       970
                //   926: aload           10
                //   928: astore          9
                //   930: aload           13
                //   932: iload           6
                //   934: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
                //   937: checkcast       Ljava/lang/String;
                //   940: astore          8
                //   942: aload           10
                //   944: astore          9
                //   946: aload           10
                //   948: aload           8
                //   950: aload           12
                //   952: aload           8
                //   954: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
                //   957: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   960: pop            
                //   961: iinc            6, 1
                //   964: goto            915
                //   967: aconst_null    
                //   968: astore          8
                //   970: aload           8
                //   972: astore          10
                //   974: aload           8
                //   976: astore          9
                //   978: aload_0        
                //   979: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   982: invokestatic    org/telegram/ui/PassportActivity.access$3700:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   985: ifnull          1257
                //   988: aload           8
                //   990: astore          9
                //   992: new             Ljava/util/HashMap;
                //   995: astore          13
                //   997: aload           8
                //   999: astore          9
                //  1001: aload           13
                //  1003: aload_0        
                //  1004: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1007: invokestatic    org/telegram/ui/PassportActivity.access$3800:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1010: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
                //  1013: aload           8
                //  1015: astore          9
                //  1017: aload           13
                //  1019: ldc_w           "document_no"
                //  1022: aload_0        
                //  1023: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1026: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //  1029: bipush          7
                //  1031: aaload         
                //  1032: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //  1035: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //  1038: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //  1041: pop            
                //  1042: aload           8
                //  1044: astore          9
                //  1046: aload_0        
                //  1047: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1050: invokestatic    org/telegram/ui/PassportActivity.access$3900:(Lorg/telegram/ui/PassportActivity;)[I
                //  1053: iconst_0       
                //  1054: iaload         
                //  1055: istore          6
                //  1057: iload           6
                //  1059: ifeq            1136
                //  1062: aload           8
                //  1064: astore          9
                //  1066: aload           13
                //  1068: ldc_w           "expiry_date"
                //  1071: getstatic       java/util/Locale.US:Ljava/util/Locale;
                //  1074: ldc_w           "%02d.%02d.%d"
                //  1077: iconst_3       
                //  1078: anewarray       Ljava/lang/Object;
                //  1081: dup            
                //  1082: iconst_0       
                //  1083: aload_0        
                //  1084: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1087: invokestatic    org/telegram/ui/PassportActivity.access$3900:(Lorg/telegram/ui/PassportActivity;)[I
                //  1090: iconst_2       
                //  1091: iaload         
                //  1092: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
                //  1095: aastore        
                //  1096: dup            
                //  1097: iconst_1       
                //  1098: aload_0        
                //  1099: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1102: invokestatic    org/telegram/ui/PassportActivity.access$3900:(Lorg/telegram/ui/PassportActivity;)[I
                //  1105: iconst_1       
                //  1106: iaload         
                //  1107: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
                //  1110: aastore        
                //  1111: dup            
                //  1112: iconst_2       
                //  1113: aload_0        
                //  1114: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1117: invokestatic    org/telegram/ui/PassportActivity.access$3900:(Lorg/telegram/ui/PassportActivity;)[I
                //  1120: iconst_0       
                //  1121: iaload         
                //  1122: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
                //  1125: aastore        
                //  1126: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
                //  1129: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //  1132: pop            
                //  1133: goto            1152
                //  1136: aload           8
                //  1138: astore          9
                //  1140: aload           13
                //  1142: ldc_w           "expiry_date"
                //  1145: ldc_w           ""
                //  1148: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                //  1151: pop            
                //  1152: aload           8
                //  1154: astore          9
                //  1156: new             Lorg/json/JSONObject;
                //  1159: dup            
                //  1160: invokespecial   org/json/JSONObject.<init>:()V
                //  1163: astore          12
                //  1165: new             Ljava/util/ArrayList;
                //  1168: astore          14
                //  1170: aload           14
                //  1172: aload           13
                //  1174: invokevirtual   java/util/HashMap.keySet:()Ljava/util/Set;
                //  1177: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
                //  1180: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$OvzP5ehYJX_5e7BC1WeQR66NW4c;
                //  1183: astore          9
                //  1185: aload           9
                //  1187: aload_0        
                //  1188: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$OvzP5ehYJX_5e7BC1WeQR66NW4c.<init>:(Lorg/telegram/ui/PassportActivity$3;)V
                //  1191: aload           14
                //  1193: aload           9
                //  1195: invokestatic    java/util/Collections.sort:(Ljava/util/List;Ljava/util/Comparator;)V
                //  1198: aload           14
                //  1200: invokevirtual   java/util/ArrayList.size:()I
                //  1203: istore          5
                //  1205: iload           4
                //  1207: istore          6
                //  1209: aload           8
                //  1211: astore          10
                //  1213: aload           12
                //  1215: astore          9
                //  1217: iload           6
                //  1219: iload           5
                //  1221: if_icmpge       1272
                //  1224: aload           14
                //  1226: iload           6
                //  1228: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
                //  1231: checkcast       Ljava/lang/String;
                //  1234: astore          9
                //  1236: aload           12
                //  1238: aload           9
                //  1240: aload           13
                //  1242: aload           9
                //  1244: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
                //  1247: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //  1250: pop            
                //  1251: iinc            6, 1
                //  1254: goto            1209
                //  1257: aconst_null    
                //  1258: astore          9
                //  1260: goto            1272
                //  1263: astore          8
                //  1265: aconst_null    
                //  1266: astore          10
                //  1268: aload           10
                //  1270: astore          9
                //  1272: aload_0        
                //  1273: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1276: invokestatic    org/telegram/ui/PassportActivity.access$4000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1279: ifnull          1292
                //  1282: aload_0        
                //  1283: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1286: invokestatic    org/telegram/ui/PassportActivity.access$4000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1289: invokevirtual   java/util/HashMap.clear:()V
                //  1292: aload_0        
                //  1293: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1296: invokestatic    org/telegram/ui/PassportActivity.access$1000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1299: ifnull          1312
                //  1302: aload_0        
                //  1303: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1306: invokestatic    org/telegram/ui/PassportActivity.access$1000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1309: invokevirtual   java/util/HashMap.clear:()V
                //  1312: aload_0        
                //  1313: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1316: invokestatic    org/telegram/ui/PassportActivity.access$4200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/ui/PassportActivity$PassportActivityDelegate;
                //  1319: astore          12
                //  1321: aload_0        
                //  1322: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1325: invokestatic    org/telegram/ui/PassportActivity.access$3200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //  1328: astore          13
                //  1330: aload           10
                //  1332: ifnull          1345
                //  1335: aload           10
                //  1337: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
                //  1340: astore          8
                //  1342: goto            1348
                //  1345: aconst_null    
                //  1346: astore          8
                //  1348: aload_0        
                //  1349: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1352: invokestatic    org/telegram/ui/PassportActivity.access$3700:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //  1355: astore          14
                //  1357: aload           9
                //  1359: ifnull          1372
                //  1362: aload           9
                //  1364: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
                //  1367: astore          9
                //  1369: goto            1375
                //  1372: aconst_null    
                //  1373: astore          9
                //  1375: aload_0        
                //  1376: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1379: invokestatic    org/telegram/ui/PassportActivity.access$300:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/messenger/SecureDocument;
                //  1382: astore          15
                //  1384: aload_0        
                //  1385: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1388: invokestatic    org/telegram/ui/PassportActivity.access$400:(Lorg/telegram/ui/PassportActivity;)Ljava/util/ArrayList;
                //  1391: astore          16
                //  1393: aload_0        
                //  1394: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1397: invokestatic    org/telegram/ui/PassportActivity.access$500:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/messenger/SecureDocument;
                //  1400: astore          17
                //  1402: aload           11
                //  1404: astore          10
                //  1406: aload_0        
                //  1407: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1410: invokestatic    org/telegram/ui/PassportActivity.access$4100:(Lorg/telegram/ui/PassportActivity;)Landroid/widget/LinearLayout;
                //  1413: ifnull          1442
                //  1416: aload           11
                //  1418: astore          10
                //  1420: aload_0        
                //  1421: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1424: invokestatic    org/telegram/ui/PassportActivity.access$4100:(Lorg/telegram/ui/PassportActivity;)Landroid/widget/LinearLayout;
                //  1427: invokevirtual   android/widget/LinearLayout.getVisibility:()I
                //  1430: ifne            1442
                //  1433: aload_0        
                //  1434: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1437: invokestatic    org/telegram/ui/PassportActivity.access$600:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/messenger/SecureDocument;
                //  1440: astore          10
                //  1442: aload           12
                //  1444: aload           13
                //  1446: aconst_null    
                //  1447: aload           8
                //  1449: aload           14
                //  1451: aload           9
                //  1453: aconst_null    
                //  1454: aload           15
                //  1456: aload           16
                //  1458: aload           17
                //  1460: aload           10
                //  1462: aload_1        
                //  1463: aload_2        
                //  1464: invokeinterface org/telegram/ui/PassportActivity$PassportActivityDelegate.saveValue:(Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Lorg/telegram/messenger/SecureDocument;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)V
                //  1469: iconst_1       
                //  1470: ireturn        
                //  1471: iconst_0       
                //  1472: ireturn        
                //  1473: astore          8
                //  1475: aload           9
                //  1477: astore          10
                //  1479: goto            1257
                //  1482: astore          9
                //  1484: aload           8
                //  1486: astore          10
                //  1488: aload           12
                //  1490: astore          9
                //  1492: goto            1272
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  503    555    1263   1272   Ljava/lang/Exception;
                //  560    629    1263   1272   Ljava/lang/Exception;
                //  632    701    1263   1272   Ljava/lang/Exception;
                //  701    848    1263   1272   Ljava/lang/Exception;
                //  852    857    1473   1482   Ljava/lang/Exception;
                //  861    871    1473   1482   Ljava/lang/Exception;
                //  875    880    1473   1482   Ljava/lang/Exception;
                //  884    890    1473   1482   Ljava/lang/Exception;
                //  894    901    1473   1482   Ljava/lang/Exception;
                //  905    912    1473   1482   Ljava/lang/Exception;
                //  930    942    1473   1482   Ljava/lang/Exception;
                //  946    961    1473   1482   Ljava/lang/Exception;
                //  978    988    1473   1482   Ljava/lang/Exception;
                //  992    997    1473   1482   Ljava/lang/Exception;
                //  1001   1013   1473   1482   Ljava/lang/Exception;
                //  1017   1042   1473   1482   Ljava/lang/Exception;
                //  1046   1057   1473   1482   Ljava/lang/Exception;
                //  1066   1133   1473   1482   Ljava/lang/Exception;
                //  1140   1152   1473   1482   Ljava/lang/Exception;
                //  1156   1165   1473   1482   Ljava/lang/Exception;
                //  1165   1205   1482   1495   Ljava/lang/Exception;
                //  1224   1251   1482   1495   Ljava/lang/Exception;
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index 698 out-of-bounds for length 698
                //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
                //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
                //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
                //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
                //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
                //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public void onItemClick(final int p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: iconst_m1      
                //     2: if_icmpne       55
                //     5: aload_0        
                //     6: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //     9: invokestatic    org/telegram/ui/PassportActivity.access$4300:(Lorg/telegram/ui/PassportActivity;)Z
                //    12: ifeq            16
                //    15: return         
                //    16: aload_0        
                //    17: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    20: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //    23: ifeq            37
                //    26: aload_0        
                //    27: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    30: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //    33: iconst_5       
                //    34: if_icmpne       45
                //    37: aload_0        
                //    38: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    41: iconst_0       
                //    42: invokestatic    org/telegram/ui/PassportActivity.access$4400:(Lorg/telegram/ui/PassportActivity;Z)V
                //    45: aload_0        
                //    46: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    49: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.finishFragment:()V
                //    52: goto            1112
                //    55: aconst_null    
                //    56: astore_2       
                //    57: iload_1        
                //    58: iconst_1       
                //    59: if_icmpne       328
                //    62: aload_0        
                //    63: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    66: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
                //    69: ifnonnull       73
                //    72: return         
                //    73: new             Landroid/widget/TextView;
                //    76: dup            
                //    77: aload_0        
                //    78: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //    81: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
                //    84: invokespecial   android/widget/TextView.<init>:(Landroid/content/Context;)V
                //    87: astore_3       
                //    88: ldc_w           "PassportInfo2"
                //    91: ldc_w           2131560233
                //    94: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //    97: astore          4
                //    99: new             Landroid/text/SpannableStringBuilder;
                //   102: dup            
                //   103: aload           4
                //   105: invokespecial   android/text/SpannableStringBuilder.<init>:(Ljava/lang/CharSequence;)V
                //   108: astore_2       
                //   109: aload           4
                //   111: bipush          42
                //   113: invokevirtual   java/lang/String.indexOf:(I)I
                //   116: istore_1       
                //   117: aload           4
                //   119: bipush          42
                //   121: invokevirtual   java/lang/String.lastIndexOf:(I)I
                //   124: istore          5
                //   126: iload_1        
                //   127: iconst_m1      
                //   128: if_icmpeq       191
                //   131: iload           5
                //   133: iconst_m1      
                //   134: if_icmpeq       191
                //   137: aload_2        
                //   138: iload           5
                //   140: iload           5
                //   142: iconst_1       
                //   143: iadd           
                //   144: ldc_w           ""
                //   147: invokevirtual   android/text/SpannableStringBuilder.replace:(IILjava/lang/CharSequence;)Landroid/text/SpannableStringBuilder;
                //   150: pop            
                //   151: aload_2        
                //   152: iload_1        
                //   153: iload_1        
                //   154: iconst_1       
                //   155: iadd           
                //   156: ldc_w           ""
                //   159: invokevirtual   android/text/SpannableStringBuilder.replace:(IILjava/lang/CharSequence;)Landroid/text/SpannableStringBuilder;
                //   162: pop            
                //   163: aload_2        
                //   164: new             Lorg/telegram/ui/PassportActivity$3$1;
                //   167: dup            
                //   168: aload_0        
                //   169: ldc_w           "PassportInfoUrl"
                //   172: ldc_w           2131560235
                //   175: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   178: invokespecial   org/telegram/ui/PassportActivity$3$1.<init>:(Lorg/telegram/ui/PassportActivity$3;Ljava/lang/String;)V
                //   181: iload_1        
                //   182: iload           5
                //   184: iconst_1       
                //   185: isub           
                //   186: bipush          33
                //   188: invokevirtual   android/text/SpannableStringBuilder.setSpan:(Ljava/lang/Object;III)V
                //   191: aload_3        
                //   192: aload_2        
                //   193: invokevirtual   android/widget/TextView.setText:(Ljava/lang/CharSequence;)V
                //   196: aload_3        
                //   197: iconst_1       
                //   198: ldc_w           16.0
                //   201: invokevirtual   android/widget/TextView.setTextSize:(IF)V
                //   204: aload_3        
                //   205: ldc_w           "dialogTextLink"
                //   208: invokestatic    org/telegram/ui/ActionBar/Theme.getColor:(Ljava/lang/String;)I
                //   211: invokevirtual   android/widget/TextView.setLinkTextColor:(I)V
                //   214: aload_3        
                //   215: ldc_w           "dialogLinkSelection"
                //   218: invokestatic    org/telegram/ui/ActionBar/Theme.getColor:(Ljava/lang/String;)I
                //   221: invokevirtual   android/widget/TextView.setHighlightColor:(I)V
                //   224: aload_3        
                //   225: ldc_w           23.0
                //   228: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
                //   231: iconst_0       
                //   232: ldc_w           23.0
                //   235: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
                //   238: iconst_0       
                //   239: invokevirtual   android/widget/TextView.setPadding:(IIII)V
                //   242: aload_3        
                //   243: new             Lorg/telegram/messenger/AndroidUtilities$LinkMovementMethodMy;
                //   246: dup            
                //   247: invokespecial   org/telegram/messenger/AndroidUtilities$LinkMovementMethodMy.<init>:()V
                //   250: invokevirtual   android/widget/TextView.setMovementMethod:(Landroid/text/method/MovementMethod;)V
                //   253: aload_3        
                //   254: ldc_w           "dialogTextBlack"
                //   257: invokestatic    org/telegram/ui/ActionBar/Theme.getColor:(Ljava/lang/String;)I
                //   260: invokevirtual   android/widget/TextView.setTextColor:(I)V
                //   263: new             Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   266: dup            
                //   267: aload_0        
                //   268: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   271: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
                //   274: invokespecial   org/telegram/ui/ActionBar/AlertDialog$Builder.<init>:(Landroid/content/Context;)V
                //   277: astore_2       
                //   278: aload_2        
                //   279: aload_3        
                //   280: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setView:(Landroid/view/View;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   283: pop            
                //   284: aload_2        
                //   285: ldc_w           "PassportInfoTitle"
                //   288: ldc_w           2131560234
                //   291: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   294: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setTitle:(Ljava/lang/CharSequence;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   297: pop            
                //   298: aload_2        
                //   299: ldc_w           "Close"
                //   302: ldc_w           2131559117
                //   305: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
                //   308: aconst_null    
                //   309: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.setNegativeButton:(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Lorg/telegram/ui/ActionBar/AlertDialog$Builder;
                //   312: pop            
                //   313: aload_0        
                //   314: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   317: aload_2        
                //   318: invokevirtual   org/telegram/ui/ActionBar/AlertDialog$Builder.create:()Lorg/telegram/ui/ActionBar/AlertDialog;
                //   321: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.showDialog:(Landroid/app/Dialog;)Landroid/app/Dialog;
                //   324: pop            
                //   325: goto            1112
                //   328: iload_1        
                //   329: iconst_2       
                //   330: if_icmpne       1112
                //   333: aload_0        
                //   334: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   337: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   340: iconst_5       
                //   341: if_icmpne       353
                //   344: aload_0        
                //   345: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   348: iconst_0       
                //   349: invokestatic    org/telegram/ui/PassportActivity.access$4500:(Lorg/telegram/ui/PassportActivity;Z)V
                //   352: return         
                //   353: aload_0        
                //   354: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   357: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   360: bipush          7
                //   362: if_icmpne       386
                //   365: aload_0        
                //   366: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   369: invokestatic    org/telegram/ui/PassportActivity.access$4600:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/SlideView;
                //   372: aload_0        
                //   373: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   376: invokestatic    org/telegram/ui/PassportActivity.access$4700:(Lorg/telegram/ui/PassportActivity;)I
                //   379: aaload         
                //   380: invokevirtual   org/telegram/ui/Components/SlideView.onNextPressed:()V
                //   383: goto            1112
                //   386: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$GdH_U_rnd96VkbnTuTp9EkJj_aw;
                //   389: dup            
                //   390: aload_0        
                //   391: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$GdH_U_rnd96VkbnTuTp9EkJj_aw.<init>:(Lorg/telegram/ui/PassportActivity$3;)V
                //   394: astore          6
                //   396: new             Lorg/telegram/ui/PassportActivity$3$2;
                //   399: dup            
                //   400: aload_0        
                //   401: aload           6
                //   403: invokespecial   org/telegram/ui/PassportActivity$3$2.<init>:(Lorg/telegram/ui/PassportActivity$3;Ljava/lang/Runnable;)V
                //   406: astore          4
                //   408: aload_0        
                //   409: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   412: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   415: iconst_4       
                //   416: if_icmpne       505
                //   419: aload_0        
                //   420: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   423: invokestatic    org/telegram/ui/PassportActivity.access$5000:(Lorg/telegram/ui/PassportActivity;)Z
                //   426: ifeq            440
                //   429: aload_0        
                //   430: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   433: invokestatic    org/telegram/ui/PassportActivity.access$5100:(Lorg/telegram/ui/PassportActivity;)Ljava/lang/String;
                //   436: astore_3       
                //   437: goto            470
                //   440: aload_0        
                //   441: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   444: invokestatic    org/telegram/ui/PassportActivity.access$2200:(Lorg/telegram/ui/PassportActivity;)Z
                //   447: ifeq            451
                //   450: return         
                //   451: aload_0        
                //   452: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   455: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   458: iconst_0       
                //   459: aaload         
                //   460: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   463: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   466: astore_3       
                //   467: goto            437
                //   470: aload_0        
                //   471: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   474: invokestatic    org/telegram/ui/PassportActivity.access$4200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/ui/PassportActivity$PassportActivityDelegate;
                //   477: aload_0        
                //   478: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   481: invokestatic    org/telegram/ui/PassportActivity.access$3200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   484: aload_3        
                //   485: aconst_null    
                //   486: aconst_null    
                //   487: aconst_null    
                //   488: aconst_null    
                //   489: aconst_null    
                //   490: aconst_null    
                //   491: aconst_null    
                //   492: aconst_null    
                //   493: aload           6
                //   495: aload           4
                //   497: invokeinterface org/telegram/ui/PassportActivity$PassportActivityDelegate.saveValue:(Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Lorg/telegram/messenger/SecureDocument;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)V
                //   502: goto            1103
                //   505: aload_0        
                //   506: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   509: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   512: iconst_3       
                //   513: if_icmpne       648
                //   516: aload_0        
                //   517: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   520: invokestatic    org/telegram/ui/PassportActivity.access$5000:(Lorg/telegram/ui/PassportActivity;)Z
                //   523: ifeq            546
                //   526: aload_0        
                //   527: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   530: invokestatic    org/telegram/ui/PassportActivity.access$5200:(Lorg/telegram/ui/PassportActivity;)I
                //   533: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
                //   536: invokevirtual   org/telegram/messenger/UserConfig.getCurrentUser:()Lorg/telegram/tgnet/TLRPC$User;
                //   539: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
                //   542: astore_3       
                //   543: goto            613
                //   546: aload_0        
                //   547: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   550: invokestatic    org/telegram/ui/PassportActivity.access$2200:(Lorg/telegram/ui/PassportActivity;)Z
                //   553: ifeq            557
                //   556: return         
                //   557: new             Ljava/lang/StringBuilder;
                //   560: dup            
                //   561: invokespecial   java/lang/StringBuilder.<init>:()V
                //   564: astore_3       
                //   565: aload_3        
                //   566: aload_0        
                //   567: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   570: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   573: iconst_1       
                //   574: aaload         
                //   575: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   578: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   581: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   584: pop            
                //   585: aload_3        
                //   586: aload_0        
                //   587: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   590: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   593: iconst_2       
                //   594: aaload         
                //   595: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   598: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   601: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   604: pop            
                //   605: aload_3        
                //   606: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   609: astore_3       
                //   610: goto            543
                //   613: aload_0        
                //   614: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   617: invokestatic    org/telegram/ui/PassportActivity.access$4200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/ui/PassportActivity$PassportActivityDelegate;
                //   620: aload_0        
                //   621: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   624: invokestatic    org/telegram/ui/PassportActivity.access$3200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   627: aload_3        
                //   628: aconst_null    
                //   629: aconst_null    
                //   630: aconst_null    
                //   631: aconst_null    
                //   632: aconst_null    
                //   633: aconst_null    
                //   634: aconst_null    
                //   635: aconst_null    
                //   636: aload           6
                //   638: aload           4
                //   640: invokeinterface org/telegram/ui/PassportActivity$PassportActivityDelegate.saveValue:(Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Lorg/telegram/messenger/SecureDocument;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)V
                //   645: goto            1103
                //   648: aload_0        
                //   649: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   652: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   655: iconst_2       
                //   656: if_icmpne       972
                //   659: aload_0        
                //   660: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   663: invokestatic    org/telegram/ui/PassportActivity.access$2100:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   666: invokevirtual   java/util/HashMap.isEmpty:()Z
                //   669: ifeq            971
                //   672: aload_0        
                //   673: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   676: invokestatic    org/telegram/ui/PassportActivity.access$2200:(Lorg/telegram/ui/PassportActivity;)Z
                //   679: ifeq            685
                //   682: goto            971
                //   685: aload_0        
                //   686: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   689: invokestatic    org/telegram/ui/PassportActivity.access$2900:(Lorg/telegram/ui/PassportActivity;)Z
                //   692: ifeq            703
                //   695: aload_0        
                //   696: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   699: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.finishFragment:()V
                //   702: return         
                //   703: aload_0        
                //   704: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   707: invokestatic    org/telegram/ui/PassportActivity.access$3000:(Lorg/telegram/ui/PassportActivity;)Z
                //   710: ifne            853
                //   713: new             Lorg/json/JSONObject;
                //   716: astore_3       
                //   717: aload_3        
                //   718: invokespecial   org/json/JSONObject.<init>:()V
                //   721: aload_3        
                //   722: ldc_w           "street_line1"
                //   725: aload_0        
                //   726: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   729: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   732: iconst_0       
                //   733: aaload         
                //   734: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   737: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   740: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   743: pop            
                //   744: aload_3        
                //   745: ldc_w           "street_line2"
                //   748: aload_0        
                //   749: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   752: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   755: iconst_1       
                //   756: aaload         
                //   757: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   760: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   763: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   766: pop            
                //   767: aload_3        
                //   768: ldc_w           "post_code"
                //   771: aload_0        
                //   772: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   775: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   778: iconst_2       
                //   779: aaload         
                //   780: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   783: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   786: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   789: pop            
                //   790: aload_3        
                //   791: ldc_w           "city"
                //   794: aload_0        
                //   795: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   798: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   801: iconst_3       
                //   802: aaload         
                //   803: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   806: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   809: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   812: pop            
                //   813: aload_3        
                //   814: ldc_w           "state"
                //   817: aload_0        
                //   818: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   821: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //   824: iconst_4       
                //   825: aaload         
                //   826: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //   829: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //   832: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   835: pop            
                //   836: aload_3        
                //   837: ldc             "country_code"
                //   839: aload_0        
                //   840: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   843: invokestatic    org/telegram/ui/PassportActivity.access$3500:(Lorg/telegram/ui/PassportActivity;)Ljava/lang/String;
                //   846: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                //   849: pop            
                //   850: goto            855
                //   853: aconst_null    
                //   854: astore_3       
                //   855: aload_0        
                //   856: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   859: invokestatic    org/telegram/ui/PassportActivity.access$4000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   862: ifnull          875
                //   865: aload_0        
                //   866: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   869: invokestatic    org/telegram/ui/PassportActivity.access$4000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   872: invokevirtual   java/util/HashMap.clear:()V
                //   875: aload_0        
                //   876: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   879: invokestatic    org/telegram/ui/PassportActivity.access$1000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   882: ifnull          895
                //   885: aload_0        
                //   886: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   889: invokestatic    org/telegram/ui/PassportActivity.access$1000:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //   892: invokevirtual   java/util/HashMap.clear:()V
                //   895: aload_0        
                //   896: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   899: invokestatic    org/telegram/ui/PassportActivity.access$4200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/ui/PassportActivity$PassportActivityDelegate;
                //   902: astore          7
                //   904: aload_0        
                //   905: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   908: invokestatic    org/telegram/ui/PassportActivity.access$3200:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   911: astore          8
                //   913: aload_3        
                //   914: ifnull          922
                //   917: aload_3        
                //   918: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
                //   921: astore_2       
                //   922: aload           7
                //   924: aload           8
                //   926: aconst_null    
                //   927: aload_2        
                //   928: aload_0        
                //   929: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   932: invokestatic    org/telegram/ui/PassportActivity.access$3700:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;
                //   935: aconst_null    
                //   936: aload_0        
                //   937: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   940: invokestatic    org/telegram/ui/PassportActivity.access$700:(Lorg/telegram/ui/PassportActivity;)Ljava/util/ArrayList;
                //   943: aload_0        
                //   944: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   947: invokestatic    org/telegram/ui/PassportActivity.access$300:(Lorg/telegram/ui/PassportActivity;)Lorg/telegram/messenger/SecureDocument;
                //   950: aload_0        
                //   951: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   954: invokestatic    org/telegram/ui/PassportActivity.access$400:(Lorg/telegram/ui/PassportActivity;)Ljava/util/ArrayList;
                //   957: aconst_null    
                //   958: aconst_null    
                //   959: aload           6
                //   961: aload           4
                //   963: invokeinterface org/telegram/ui/PassportActivity$PassportActivityDelegate.saveValue:(Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$TL_secureRequiredType;Ljava/lang/String;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Ljava/util/ArrayList;Lorg/telegram/messenger/SecureDocument;Lorg/telegram/messenger/SecureDocument;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)V
                //   968: goto            1103
                //   971: return         
                //   972: aload_0        
                //   973: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   976: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //   979: iconst_1       
                //   980: if_icmpne       995
                //   983: aload_0        
                //   984: aload           6
                //   986: aload           4
                //   988: invokespecial   org/telegram/ui/PassportActivity$3.onIdentityDone:(Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;)Z
                //   991: ifne            1103
                //   994: return         
                //   995: aload_0        
                //   996: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //   999: invokestatic    org/telegram/ui/PassportActivity.access$1400:(Lorg/telegram/ui/PassportActivity;)I
                //  1002: bipush          6
                //  1004: if_icmpne       1103
                //  1007: new             Lorg/telegram/tgnet/TLRPC$TL_account_verifyEmail;
                //  1010: dup            
                //  1011: invokespecial   org/telegram/tgnet/TLRPC$TL_account_verifyEmail.<init>:()V
                //  1014: astore_3       
                //  1015: aload_3        
                //  1016: aload_0        
                //  1017: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1020: invokestatic    org/telegram/ui/PassportActivity.access$3100:(Lorg/telegram/ui/PassportActivity;)Ljava/util/HashMap;
                //  1023: ldc_w           "email"
                //  1026: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
                //  1029: checkcast       Ljava/lang/String;
                //  1032: putfield        org/telegram/tgnet/TLRPC$TL_account_verifyEmail.email:Ljava/lang/String;
                //  1035: aload_3        
                //  1036: aload_0        
                //  1037: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1040: invokestatic    org/telegram/ui/PassportActivity.access$2500:(Lorg/telegram/ui/PassportActivity;)[Lorg/telegram/ui/Components/EditTextBoldCursor;
                //  1043: iconst_0       
                //  1044: aaload         
                //  1045: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                //  1048: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                //  1051: putfield        org/telegram/tgnet/TLRPC$TL_account_verifyEmail.code:Ljava/lang/String;
                //  1054: aload_0        
                //  1055: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1058: invokestatic    org/telegram/ui/PassportActivity.access$5300:(Lorg/telegram/ui/PassportActivity;)I
                //  1061: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
                //  1064: aload_3        
                //  1065: new             Lorg/telegram/ui/_$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4;
                //  1068: dup            
                //  1069: aload_0        
                //  1070: aload           6
                //  1072: aload           4
                //  1074: aload_3        
                //  1075: invokespecial   org/telegram/ui/_$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4.<init>:(Lorg/telegram/ui/PassportActivity$3;Ljava/lang/Runnable;Lorg/telegram/ui/PassportActivity$ErrorRunnable;Lorg/telegram/tgnet/TLRPC$TL_account_verifyEmail;)V
                //  1078: invokevirtual   org/telegram/tgnet/ConnectionsManager.sendRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/tgnet/RequestDelegate;)I
                //  1081: istore_1       
                //  1082: aload_0        
                //  1083: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1086: invokestatic    org/telegram/ui/PassportActivity.access$5500:(Lorg/telegram/ui/PassportActivity;)I
                //  1089: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
                //  1092: iload_1        
                //  1093: aload_0        
                //  1094: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1097: invokestatic    org/telegram/ui/PassportActivity.access$5400:(Lorg/telegram/ui/PassportActivity;)I
                //  1100: invokevirtual   org/telegram/tgnet/ConnectionsManager.bindRequestToGuid:(II)V
                //  1103: aload_0        
                //  1104: getfield        org/telegram/ui/PassportActivity$3.this$0:Lorg/telegram/ui/PassportActivity;
                //  1107: iconst_1       
                //  1108: iconst_1       
                //  1109: invokestatic    org/telegram/ui/PassportActivity.access$4900:(Lorg/telegram/ui/PassportActivity;ZZ)V
                //  1112: return         
                //  1113: astore_3       
                //  1114: goto            853
                //  1117: astore          7
                //  1119: goto            855
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  703    721    1113   1117   Ljava/lang/Exception;
                //  721    850    1117   1122   Ljava/lang/Exception;
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0853:
                //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
                //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        if (this.currentActivityType == 7) {
            final ScrollView scrollView = new ScrollView(context) {
                protected void onMeasure(final int n, final int n2) {
                    PassportActivity.this.scrollHeight = View$MeasureSpec.getSize(n2) - AndroidUtilities.dp(30.0f);
                    super.onMeasure(n, n2);
                }
                
                protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
                    return false;
                }
                
                public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                    if (PassportActivity.this.currentViewNum == 1 || PassportActivity.this.currentViewNum == 2 || PassportActivity.this.currentViewNum == 4) {
                        rect.bottom += AndroidUtilities.dp(40.0f);
                    }
                    return super.requestChildRectangleOnScreen(view, rect, b);
                }
            };
            this.scrollView = scrollView;
            super.fragmentView = (View)scrollView;
            this.scrollView.setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
        }
        else {
            super.fragmentView = (View)new FrameLayout(context);
            final View fragmentView = super.fragmentView;
            final FrameLayout frameLayout = (FrameLayout)fragmentView;
            fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            (this.scrollView = new ScrollView(context) {
                protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
                    return false;
                }
                
                public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                    rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                    rect.top += AndroidUtilities.dp(20.0f);
                    rect.bottom += AndroidUtilities.dp(50.0f);
                    return super.requestChildRectangleOnScreen(view, rect, b);
                }
            }).setFillViewport(true);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
            final ScrollView scrollView2 = this.scrollView;
            float n;
            if (this.currentActivityType == 0) {
                n = 48.0f;
            }
            else {
                n = 0.0f;
            }
            frameLayout.addView((View)scrollView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, n));
            (this.linearLayout2 = new LinearLayout(context)).setOrientation(1);
            this.scrollView.addView((View)this.linearLayout2, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        }
        final int currentActivityType = this.currentActivityType;
        Label_0397: {
            if (currentActivityType == 0 || currentActivityType == 8) {
                break Label_0397;
            }
            this.doneItem = super.actionBar.createMenu().addItemWithWidth(2, 2131165439, AndroidUtilities.dp(56.0f));
            (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
            this.doneItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            final int currentActivityType2 = this.currentActivityType;
            if (currentActivityType2 != 1 && currentActivityType2 != 2) {
                break Label_0397;
            }
            final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
            if (chatAttachAlert == null) {
                break Label_0397;
            }
            while (true) {
                try {
                    if (chatAttachAlert.isShowing()) {
                        this.chatAttachAlert.dismiss();
                    }
                    this.chatAttachAlert.onDestroy();
                    this.chatAttachAlert = null;
                    final int currentActivityType3 = this.currentActivityType;
                    if (currentActivityType3 == 5) {
                        this.createPasswordInterface(context);
                    }
                    else if (currentActivityType3 == 0) {
                        this.createRequestInterface(context);
                    }
                    else if (currentActivityType3 == 1) {
                        this.createIdentityInterface(context);
                        this.fillInitialValues();
                    }
                    else if (currentActivityType3 == 2) {
                        this.createAddressInterface(context);
                        this.fillInitialValues();
                    }
                    else if (currentActivityType3 == 3) {
                        this.createPhoneInterface(context);
                    }
                    else if (currentActivityType3 == 4) {
                        this.createEmailInterface(context);
                    }
                    else if (currentActivityType3 == 6) {
                        this.createEmailVerificationInterface(context);
                    }
                    else if (currentActivityType3 == 7) {
                        this.createPhoneVerificationInterface(context);
                    }
                    else if (currentActivityType3 == 8) {
                        this.createManageInterface(context);
                    }
                    return super.fragmentView;
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void didReceivedNotification(int n, final int n2, final Object... array) {
        if (n == NotificationCenter.FileDidUpload) {
            final String s = (String)array[0];
            final SecureDocument key = this.uploadingDocuments.get(s);
            if (key != null) {
                key.inputFile = (TLRPC.TL_inputFile)array[1];
                this.uploadingDocuments.remove(s);
                if (this.uploadingDocuments.isEmpty()) {
                    final ActionBarMenuItem doneItem = this.doneItem;
                    if (doneItem != null) {
                        doneItem.setEnabled(true);
                        this.doneItem.setAlpha(1.0f);
                    }
                }
                final HashMap<SecureDocument, SecureDocumentCell> documentsCells = this.documentsCells;
                if (documentsCells != null) {
                    final SecureDocumentCell secureDocumentCell = documentsCells.get(key);
                    if (secureDocumentCell != null) {
                        secureDocumentCell.updateButtonState(true);
                    }
                }
                final HashMap<String, String> errorsValues = this.errorsValues;
                if (errorsValues != null && errorsValues.containsKey("error_document_all")) {
                    this.errorsValues.remove("error_document_all");
                    this.checkTopErrorCell(false);
                }
                n = key.type;
                if (n == 0) {
                    if (this.bottomCell != null && !TextUtils.isEmpty(this.noAllDocumentsErrorText)) {
                        this.bottomCell.setText(this.noAllDocumentsErrorText);
                    }
                    this.errorsValues.remove("files_all");
                }
                else if (n == 4) {
                    if (this.bottomCellTranslation != null && !TextUtils.isEmpty(this.noAllTranslationErrorText)) {
                        this.bottomCellTranslation.setText(this.noAllTranslationErrorText);
                    }
                    this.errorsValues.remove("translation_all");
                }
            }
        }
        else if (n != NotificationCenter.FileDidFailUpload) {
            if (n == NotificationCenter.didSetTwoStepPassword) {
                if (array != null && array.length > 0) {
                    if (array[7] != null) {
                        final EditTextBoldCursor[] inputFields = this.inputFields;
                        if (inputFields[0] != null) {
                            inputFields[0].setText((CharSequence)array[7]);
                        }
                    }
                    if (array[6] == null) {
                        this.currentPassword = new TLRPC.TL_account_password();
                        final TLRPC.TL_account_password currentPassword = this.currentPassword;
                        currentPassword.current_algo = (TLRPC.PasswordKdfAlgo)array[1];
                        currentPassword.new_secure_algo = (TLRPC.SecurePasswordKdfAlgo)array[2];
                        currentPassword.secure_random = (byte[])array[3];
                        currentPassword.has_recovery = (TextUtils.isEmpty((CharSequence)array[4]) ^ true);
                        final TLRPC.TL_account_password currentPassword2 = this.currentPassword;
                        currentPassword2.hint = (String)array[5];
                        currentPassword2.srp_id = -1L;
                        currentPassword2.srp_B = new byte[256];
                        Utilities.random.nextBytes(currentPassword2.srp_B);
                        final EditTextBoldCursor[] inputFields2 = this.inputFields;
                        if (inputFields2[0] != null && inputFields2[0].length() > 0) {
                            this.usingSavedPassword = 2;
                        }
                    }
                }
                else {
                    this.currentPassword = null;
                    this.loadPasswordInfo();
                }
                this.updatePasswordInterface();
            }
            else {
                n = NotificationCenter.didRemoveTwoStepPassword;
            }
        }
    }
    
    @Override
    public void dismissCurrentDialig() {
        final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null && super.visibleDialog == chatAttachAlert) {
            chatAttachAlert.closeCamera(false);
            this.chatAttachAlert.dismissInternal();
            this.chatAttachAlert.hideCamera(true);
            return;
        }
        super.dismissCurrentDialig();
    }
    
    @Override
    public boolean dismissDialogOnPause(final Dialog dialog) {
        return dialog != this.chatAttachAlert && super.dismissDialogOnPause(dialog);
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
        list.add(new ThemeDescription(this.extraBackgroundView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        final View extraBackgroundView2 = this.extraBackgroundView2;
        if (extraBackgroundView2 != null) {
            list.add(new ThemeDescription(extraBackgroundView2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        }
        for (int i = 0; i < this.dividers.size(); ++i) {
            list.add(new ThemeDescription(this.dividers.get(i), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "divider"));
        }
        final Iterator<Map.Entry<SecureDocument, SecureDocumentCell>> iterator = this.documentsCells.entrySet().iterator();
        while (iterator.hasNext()) {
            final SecureDocumentCell secureDocumentCell = iterator.next().getValue();
            list.add(new ThemeDescription((View)secureDocumentCell, ThemeDescription.FLAG_SELECTORWHITE, new Class[] { SecureDocumentCell.class }, null, null, null, "windowBackgroundWhite"));
            list.add(new ThemeDescription((View)secureDocumentCell, 0, new Class[] { SecureDocumentCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription((View)secureDocumentCell, 0, new Class[] { SecureDocumentCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        }
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[] { TextDetailSettingsCell.class }, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[] { TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[] { TextDetailSecureCell.class }, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextDetailSecureCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextDetailSecureCell.class }, null, null, null, "divider"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextDetailSecureCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailSecureCell.class }, new String[] { "checkImageView" }, null, null, null, "featuredStickers_addedIcon"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class }, null, null, null, "windowBackgroundWhite"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"));
        list.add(new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"));
        list.add(new ThemeDescription((View)this.linearLayout2, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"));
        if (this.inputFields != null) {
            int n = 0;
            while (true) {
                final EditTextBoldCursor[] inputFields = this.inputFields;
                if (n >= inputFields.length) {
                    break;
                }
                list.add(new ThemeDescription((View)inputFields[n].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)this.inputFields[n], ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteRedText3"));
                ++n;
            }
        }
        else {
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
            list.add(new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteRedText3"));
        }
        if (this.inputExtraFields != null) {
            int n2 = 0;
            while (true) {
                final EditTextBoldCursor[] inputExtraFields = this.inputExtraFields;
                if (n2 >= inputExtraFields.length) {
                    break;
                }
                list.add(new ThemeDescription((View)inputExtraFields[n2].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "windowBackgroundWhiteBlueHeader"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)this.inputExtraFields[n2], ThemeDescription.FLAG_PROGRESSBAR | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteRedText3"));
                ++n2;
            }
        }
        list.add(new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        list.add(new ThemeDescription((View)this.noPasswordImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chat_messagePanelIcons"));
        list.add(new ThemeDescription((View)this.noPasswordTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText4"));
        list.add(new ThemeDescription((View)this.noPasswordSetTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText5"));
        list.add(new ThemeDescription((View)this.passwordForgotButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        list.add(new ThemeDescription((View)this.plusTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription((View)this.acceptTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "passport_authorizeText"));
        list.add(new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "passport_authorizeBackground"));
        list.add(new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "passport_authorizeBackgroundSelected"));
        list.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner2"));
        list.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter2"));
        list.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressInner2"));
        list.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, "contextProgressOuter2"));
        list.add(new ThemeDescription((View)this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "sessions_devicesImage"));
        list.add(new ThemeDescription((View)this.emptyTextView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.emptyTextView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
        list.add(new ThemeDescription((View)this.emptyTextView3, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        return list.toArray(new ThemeDescription[0]);
    }
    
    public void needHideProgress() {
        final AlertDialog progressDialog = this.progressDialog;
        if (progressDialog == null) {
            return;
        }
        try {
            progressDialog.dismiss();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.progressDialog = null;
    }
    
    public void needShowProgress() {
        if (this.getParentActivity() != null && !this.getParentActivity().isFinishing()) {
            if (this.progressDialog == null) {
                (this.progressDialog = new AlertDialog((Context)this.getParentActivity(), 3)).setCanCacnel(false);
                this.progressDialog.show();
            }
        }
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            if (n != 0 && n != 2) {
                if (n == 1) {
                    if (intent == null || intent.getData() == null) {
                        this.showAttachmentError();
                        return;
                    }
                    final ArrayList<SendMessagesHelper.SendingMediaInfo> list = new ArrayList<SendMessagesHelper.SendingMediaInfo>();
                    final SendMessagesHelper.SendingMediaInfo e = new SendMessagesHelper.SendingMediaInfo();
                    e.uri = intent.getData();
                    list.add(e);
                    this.processSelectedFiles(list);
                }
            }
            else {
                this.createChatAttachView();
                final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
                if (chatAttachAlert != null) {
                    chatAttachAlert.onActivityResultFragment(n, intent, this.currentPicturePath);
                }
                this.currentPicturePath = null;
            }
        }
    }
    
    @Override
    public boolean onBackPressed() {
        final int currentActivityType = this.currentActivityType;
        int n = 0;
        if (currentActivityType == 7) {
            this.views[this.currentViewNum].onBackPressed(true);
            while (true) {
                final SlideView[] views = this.views;
                if (n >= views.length) {
                    break;
                }
                if (views[n] != null) {
                    views[n].onDestroyActivity();
                }
                ++n;
            }
        }
        else if (currentActivityType != 0 && currentActivityType != 5) {
            if (currentActivityType == 1 || currentActivityType == 2) {
                return this.checkDiscard() ^ true;
            }
        }
        else {
            this.callCallback(false);
        }
        return true;
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        if (this.currentActivityType == 3 && Build$VERSION.SDK_INT >= 23 && dialog == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
            this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        int n = 0;
        this.callCallback(false);
        final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.dismissInternal();
            this.chatAttachAlert.onDestroy();
        }
        if (this.currentActivityType == 7) {
            while (true) {
                final SlideView[] views = this.views;
                if (n >= views.length) {
                    break;
                }
                if (views[n] != null) {
                    views[n].onDestroyActivity();
                }
                ++n;
            }
            final AlertDialog progressDialog = this.progressDialog;
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                this.progressDialog = null;
            }
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.onPause();
        }
    }
    
    @Override
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
        final int currentActivityType = this.currentActivityType;
        if (currentActivityType == 1 || currentActivityType == 2) {
            final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
            if (chatAttachAlert != null) {
                if (n == 17 && chatAttachAlert != null) {
                    chatAttachAlert.checkCamera(false);
                    return;
                }
                if (n == 21) {
                    if (this.getParentActivity() == null) {
                        return;
                    }
                    if (array2 != null && array2.length != 0 && array2[0] != 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setMessage(LocaleController.getString("PermissionNoAudioVideo", 2131560415));
                        builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$PassportActivity$saEsQ_rHD2skXoYwyq_hN_ao_V8(this));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        builder.show();
                    }
                    return;
                }
                else {
                    if (n == 19 && array2 != null && array2.length > 0 && array2[0] == 0) {
                        this.processSelectedAttach(0);
                        return;
                    }
                    if (n != 22 || array2 == null || array2.length <= 0 || array2[0] != 0) {
                        return;
                    }
                    final TextSettingsCell scanDocumentCell = this.scanDocumentCell;
                    if (scanDocumentCell != null) {
                        scanDocumentCell.callOnClick();
                    }
                    return;
                }
            }
        }
        if (this.currentActivityType == 3 && n == 6) {
            this.startPhoneVerification(false, this.pendingPhone, this.pendingFinishRunnable, this.pendingErrorRunnable, this.pendingDelegate);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
        if (chatAttachAlert != null) {
            chatAttachAlert.onResume();
        }
        if (this.currentActivityType == 5) {
            final ViewGroup[] inputFieldContainers = this.inputFieldContainers;
            if (inputFieldContainers != null && inputFieldContainers[0] != null && inputFieldContainers[0].getVisibility() == 0) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard((View)this.inputFields[0]);
                AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$1boqEPL5RwKnvvajl3SmsZe7IEk(this), 200L);
            }
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (this.presentAfterAnimation != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$l67xseKhR4lQtrsyuaJeoxN_jJM(this));
        }
        final int currentActivityType = this.currentActivityType;
        if (currentActivityType == 5) {
            if (b) {
                if (this.inputFieldContainers[0].getVisibility() == 0) {
                    this.inputFields[0].requestFocus();
                    AndroidUtilities.showKeyboard((View)this.inputFields[0]);
                }
                if (this.usingSavedPassword == 2) {
                    this.onPasswordDone(false);
                }
            }
        }
        else if (currentActivityType == 7) {
            if (b) {
                this.views[this.currentViewNum].onShow();
            }
        }
        else if (currentActivityType == 4) {
            if (b) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard((View)this.inputFields[0]);
            }
        }
        else if (currentActivityType == 6) {
            if (b) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard((View)this.inputFields[0]);
            }
        }
        else if ((currentActivityType == 2 || currentActivityType == 1) && Build$VERSION.SDK_INT >= 21) {
            this.createChatAttachView();
        }
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        this.currentPicturePath = bundle.getString("path");
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        final String currentPicturePath = this.currentPicturePath;
        if (currentPicturePath != null) {
            bundle.putString("path", currentPicturePath);
        }
    }
    
    public void setNeedActivityResult(final boolean needActivityResult) {
        this.needActivityResult = needActivityResult;
    }
    
    public void setPage(final int currentViewNum, final boolean b, final Bundle bundle) {
        if (currentViewNum == 3) {
            this.doneItem.setVisibility(8);
        }
        final SlideView[] views = this.views;
        final SlideView slideView = views[this.currentViewNum];
        final SlideView slideView2 = views[currentViewNum];
        this.currentViewNum = currentViewNum;
        slideView2.setParams(bundle, false);
        slideView2.onShow();
        if (b) {
            slideView2.setTranslationX((float)AndroidUtilities.displaySize.x);
            final AnimatorSet set = new AnimatorSet();
            set.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
            set.setDuration(300L);
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)slideView, "translationX", new float[] { (float)(-AndroidUtilities.displaySize.x) }), (Animator)ObjectAnimator.ofFloat((Object)slideView2, "translationX", new float[] { 0.0f }) });
            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    slideView.setVisibility(8);
                    slideView.setX(0.0f);
                }
                
                public void onAnimationStart(final Animator animator) {
                    slideView2.setVisibility(0);
                }
            });
            set.start();
        }
        else {
            slideView2.setTranslationX(0.0f);
            slideView2.setVisibility(0);
            if (slideView != slideView2) {
                slideView.setVisibility(8);
            }
        }
    }
    
    private class EncryptionResult
    {
        byte[] decrypyedFileSecret;
        byte[] encryptedData;
        byte[] fileHash;
        byte[] fileSecret;
        SecureDocumentKey secureDocumentKey;
        
        public EncryptionResult(final byte[] encryptedData, final byte[] fileSecret, final byte[] decrypyedFileSecret, final byte[] fileHash, final byte[] array, final byte[] array2) {
            this.encryptedData = encryptedData;
            this.fileSecret = fileSecret;
            this.fileHash = fileHash;
            this.decrypyedFileSecret = decrypyedFileSecret;
            this.secureDocumentKey = new SecureDocumentKey(array, array2);
        }
    }
    
    private interface ErrorRunnable
    {
        void onError(final String p0, final String p1);
    }
    
    public class LinkSpan extends ClickableSpan
    {
        public void onClick(final View view) {
            Browser.openUrl((Context)PassportActivity.this.getParentActivity(), PassportActivity.this.currentForm.privacy_policy_url);
        }
        
        public void updateDrawState(final TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(true);
            textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        }
    }
    
    private interface PassportActivityDelegate
    {
        void deleteValue(final TLRPC.TL_secureRequiredType p0, final TLRPC.TL_secureRequiredType p1, final ArrayList<TLRPC.TL_secureRequiredType> p2, final boolean p3, final Runnable p4, final ErrorRunnable p5);
        
        SecureDocument saveFile(final TLRPC.TL_secureFile p0);
        
        void saveValue(final TLRPC.TL_secureRequiredType p0, final String p1, final String p2, final TLRPC.TL_secureRequiredType p3, final String p4, final ArrayList<SecureDocument> p5, final SecureDocument p6, final ArrayList<SecureDocument> p7, final SecureDocument p8, final SecureDocument p9, final Runnable p10, final ErrorRunnable p11);
    }
    
    public class PhoneConfirmationView extends SlideView implements NotificationCenterDelegate
    {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private int codeTime;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private boolean ignoreOnTextChange;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError;
        private int length;
        private boolean nextPressed;
        private int nextType;
        private int openTime;
        private String pattern;
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private int time;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private final Object timerSync;
        private TextView titleTextView;
        private int verificationType;
        private boolean waitingForEvent;
        
        public PhoneConfirmationView(final Context context, int gravity) {
            super(context);
            this.timerSync = new Object();
            this.time = 60000;
            this.codeTime = 15000;
            this.lastError = "";
            this.pattern = "*";
            this.verificationType = gravity;
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            final TextView titleTextView = this.titleTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 3;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            titleTextView.setGravity(gravity);
            this.titleTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            if (this.verificationType == 3) {
                final TextView confirmTextView = this.confirmTextView;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                confirmTextView.setGravity(gravity | 0x30);
                final FrameLayout frameLayout = new FrameLayout(context);
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                final ImageView imageView = new ImageView(context);
                imageView.setImageResource(2131165739);
                final boolean isRTL2 = LocaleController.isRTL;
                if (isRTL2) {
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    final TextView confirmTextView2 = this.confirmTextView;
                    if (LocaleController.isRTL) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 82.0f, 0.0f, 0.0f, 0.0f));
                }
                else {
                    final TextView confirmTextView3 = this.confirmTextView;
                    if (isRTL2) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 0.0f, 0.0f, 82.0f, 0.0f));
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f));
                }
            }
            else {
                this.confirmTextView.setGravity(49);
                final FrameLayout frameLayout2 = new FrameLayout(context);
                this.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
                if (this.verificationType == 1) {
                    (this.blackImageView = new ImageView(context)).setImageResource(2131165856);
                    this.blackImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blackImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165854);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentAppCodeTitle", 2131560718));
                }
                else {
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165855);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentSmsCodeTitle", 2131560722));
                }
                this.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                this.addView((View)this.confirmTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            (this.codeFieldContainer = new LinearLayout(context)).setOrientation(0);
            this.addView((View)this.codeFieldContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 36, 1));
            if (this.verificationType == 3) {
                this.codeFieldContainer.setVisibility(8);
            }
            (this.timeText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            if (this.verificationType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                final TextView timeText = this.timeText;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                this.progressView = new ProgressView(context);
                final TextView timeText2 = this.timeText;
                gravity = n;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                timeText2.setGravity(gravity);
                this.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f));
            }
            else {
                this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                this.addView((View)this.timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            }
            (this.problemText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.problemText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.verificationType == 1) {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
            }
            else {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCode", 2131559266));
            }
            this.addView((View)this.problemText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener((View$OnClickListener)new _$$Lambda$PassportActivity$PhoneConfirmationView$PSs1EP1O5Wgd6q0a05fTJgjQt4s(this));
        }
        
        private void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$PhoneConfirmationView$4$4TMBClZCqBNuy_tFpKG40rvRb80(this));
                }
            }, 0L, 1000L);
        }
        
        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            (this.timeTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    if (PhoneConfirmationView.this.timeTimer == null) {
                        return;
                    }
                    final double v = (double)System.currentTimeMillis();
                    final double access$10500 = PhoneConfirmationView.this.lastCurrentTime;
                    Double.isNaN(v);
                    final PhoneConfirmationView this$1 = PhoneConfirmationView.this;
                    final double v2 = this$1.time;
                    Double.isNaN(v2);
                    this$1.time = (int)(v2 - (v - access$10500));
                    PhoneConfirmationView.this.lastCurrentTime = v;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (PhoneConfirmationView.this.time >= 1000) {
                                final int n = PhoneConfirmationView.this.time / 1000 / 60;
                                final int n2 = PhoneConfirmationView.this.time / 1000 - n * 60;
                                if (PhoneConfirmationView.this.nextType != 4 && PhoneConfirmationView.this.nextType != 3) {
                                    if (PhoneConfirmationView.this.nextType == 2) {
                                        PhoneConfirmationView.this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, n, n2));
                                    }
                                }
                                else {
                                    PhoneConfirmationView.this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, n, n2));
                                }
                                if (PhoneConfirmationView.this.progressView != null) {
                                    PhoneConfirmationView.this.progressView.setProgress(1.0f - PhoneConfirmationView.this.time / (float)PhoneConfirmationView.this.timeout);
                                }
                            }
                            else {
                                if (PhoneConfirmationView.this.progressView != null) {
                                    PhoneConfirmationView.this.progressView.setProgress(1.0f);
                                }
                                PhoneConfirmationView.this.destroyTimer();
                                if (PhoneConfirmationView.this.verificationType == 3) {
                                    AndroidUtilities.setWaitingForCall(false);
                                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                                    PhoneConfirmationView.this.waitingForEvent = false;
                                    PhoneConfirmationView.this.destroyCodeTimer();
                                    PhoneConfirmationView.this.resendCode();
                                }
                                else if (PhoneConfirmationView.this.verificationType == 2 || PhoneConfirmationView.this.verificationType == 4) {
                                    if (PhoneConfirmationView.this.nextType != 4 && PhoneConfirmationView.this.nextType != 2) {
                                        if (PhoneConfirmationView.this.nextType == 3) {
                                            AndroidUtilities.setWaitingForSms(false);
                                            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                                            PhoneConfirmationView.this.waitingForEvent = false;
                                            PhoneConfirmationView.this.destroyCodeTimer();
                                            PhoneConfirmationView.this.resendCode();
                                        }
                                    }
                                    else {
                                        if (PhoneConfirmationView.this.nextType == 4) {
                                            PhoneConfirmationView.this.timeText.setText((CharSequence)LocaleController.getString("Calling", 2131558887));
                                        }
                                        else {
                                            PhoneConfirmationView.this.timeText.setText((CharSequence)LocaleController.getString("SendingSms", 2131560714));
                                        }
                                        PhoneConfirmationView.this.createCodeTimer();
                                        final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
                                        tl_auth_resendCode.phone_number = PhoneConfirmationView.this.phone;
                                        tl_auth_resendCode.phone_code_hash = PhoneConfirmationView.this.phoneHash;
                                        ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_auth_resendCode, new _$$Lambda$PassportActivity$PhoneConfirmationView$5$1$Q_utOxW1QEIU96NoSBjX04lpg8k(this), 2);
                                    }
                                }
                            }
                        }
                    });
                }
            }, 0L, 1000L);
        }
        
        private void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.codeTimer != null) {
                        this.codeTimer.cancel();
                        this.codeTimer = null;
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
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
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        private String getCode() {
            if (this.codeField == null) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            int n = 0;
            while (true) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (n >= codeField.length) {
                    break;
                }
                sb.append(PhoneFormat.stripExceptNumbers(codeField[n].getText().toString()));
                ++n;
            }
            return sb.toString();
        }
        
        private void resendCode() {
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            this.nextPressed = true;
            PassportActivity.this.needShowProgress();
            final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
            tl_auth_resendCode.phone_number = this.phone;
            tl_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_auth_resendCode, new _$$Lambda$PassportActivity$PhoneConfirmationView$1NsJYQaZadbCmSfsr4dWklizb2g(this, bundle, tl_auth_resendCode), 2);
        }
        
        @Override
        public void didReceivedNotification(final int n, final int n2, final Object... array) {
            if (this.waitingForEvent) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (codeField != null) {
                    if (n == NotificationCenter.didReceiveSmsCode) {
                        final EditTextBoldCursor editTextBoldCursor = codeField[0];
                        final StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(array[0]);
                        editTextBoldCursor.setText((CharSequence)sb.toString());
                        this.onNextPressed();
                    }
                    else if (n == NotificationCenter.didReceiveCall) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(array[0]);
                        final String string = sb2.toString();
                        if (!AndroidUtilities.checkPhonePattern(this.pattern, string)) {
                            return;
                        }
                        this.ignoreOnTextChange = true;
                        this.codeField[0].setText((CharSequence)string);
                        this.ignoreOnTextChange = false;
                        this.onNextPressed();
                    }
                }
            }
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            if (!b) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)PassportActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("StopVerification", 2131560831));
                builder.setPositiveButton(LocaleController.getString("Continue", 2131559153), null);
                builder.setNegativeButton(LocaleController.getString("Stop", 2131560820), (DialogInterface$OnClickListener)new _$$Lambda$PassportActivity$PhoneConfirmationView$WGkFAmcyRqIzKtaNtQuEsZZn98M(this));
                PassportActivity.this.showDialog(builder.create());
                return false;
            }
            final TLRPC.TL_auth_cancelCode tl_auth_cancelCode = new TLRPC.TL_auth_cancelCode();
            tl_auth_cancelCode.phone_number = this.phone;
            tl_auth_cancelCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_auth_cancelCode, (RequestDelegate)_$$Lambda$PassportActivity$PhoneConfirmationView$V0ORURFAuRbPRy3yre_3sf3Qzvs.INSTANCE, 2);
            this.destroyTimer();
            this.destroyCodeTimer();
            this.currentParams = null;
            final int verificationType = this.verificationType;
            if (verificationType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (verificationType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            return true;
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
        }
        
        @Override
        public void onDestroyActivity() {
            super.onDestroyActivity();
            final int verificationType = this.verificationType;
            if (verificationType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (verificationType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            this.destroyTimer();
            this.destroyCodeTimer();
        }
        
        protected void onLayout(final boolean b, int n, int bottom, int n2, final int n3) {
            super.onLayout(b, n, bottom, n2, n3);
            if (this.verificationType != 3 && this.blueImageView != null) {
                bottom = this.confirmTextView.getBottom();
                n = this.getMeasuredHeight() - bottom;
                if (this.problemText.getVisibility() == 0) {
                    n2 = this.problemText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView problemText = this.problemText;
                    problemText.layout(problemText.getLeft(), n, this.problemText.getRight(), n2 + n);
                }
                else if (this.timeText.getVisibility() == 0) {
                    n2 = this.timeText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView timeText = this.timeText;
                    timeText.layout(timeText.getLeft(), n, this.timeText.getRight(), n2 + n);
                }
                else {
                    n += bottom;
                }
                n2 = this.codeFieldContainer.getMeasuredHeight();
                n = (n - bottom - n2) / 2 + bottom;
                final LinearLayout codeFieldContainer = this.codeFieldContainer;
                codeFieldContainer.layout(codeFieldContainer.getLeft(), n, this.codeFieldContainer.getRight(), n2 + n);
            }
        }
        
        protected void onMeasure(int dp, int dp2) {
            super.onMeasure(dp, dp2);
            if (this.verificationType != 3) {
                final ImageView blueImageView = this.blueImageView;
                if (blueImageView != null) {
                    final int n = blueImageView.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0f);
                    dp = AndroidUtilities.dp(80.0f);
                    dp2 = AndroidUtilities.dp(291.0f);
                    if (PassportActivity.this.scrollHeight - n < dp) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), n + dp);
                    }
                    else if (PassportActivity.this.scrollHeight > dp2) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), dp2);
                    }
                    else {
                        this.setMeasuredDimension(this.getMeasuredWidth(), PassportActivity.this.scrollHeight);
                    }
                }
            }
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            final String code = this.getCode();
            if (TextUtils.isEmpty((CharSequence)code)) {
                AndroidUtilities.shakeView((View)this.codeFieldContainer, 2.0f, 0);
                return;
            }
            this.nextPressed = true;
            final int verificationType = this.verificationType;
            if (verificationType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (verificationType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            PassportActivity.this.showEditDoneProgress(true, true);
            final TLRPC.TL_account_verifyPhone tl_account_verifyPhone = new TLRPC.TL_account_verifyPhone();
            tl_account_verifyPhone.phone_number = this.phone;
            tl_account_verifyPhone.phone_code = code;
            tl_account_verifyPhone.phone_code_hash = this.phoneHash;
            this.destroyTimer();
            PassportActivity.this.needShowProgress();
            ConnectionsManager.getInstance(PassportActivity.this.currentAccount).sendRequest(tl_account_verifyPhone, new _$$Lambda$PassportActivity$PhoneConfirmationView$H7W_zFejxtN_G3EXdQMEm98R9oE(this, tl_account_verifyPhone), 2);
        }
        
        @Override
        public void onShow() {
            super.onShow();
            final LinearLayout codeFieldContainer = this.codeFieldContainer;
            if (codeFieldContainer != null && codeFieldContainer.getVisibility() == 0) {
                for (int i = this.codeField.length - 1; i >= 0; --i) {
                    if (i == 0 || this.codeField[i].length() != 0) {
                        this.codeField[i].requestFocus();
                        final EditTextBoldCursor[] codeField = this.codeField;
                        codeField[i].setSelection(codeField[i].length());
                        AndroidUtilities.showKeyboard((View)this.codeField[i]);
                        break;
                    }
                }
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            this.waitingForEvent = true;
            final int verificationType = this.verificationType;
            if (verificationType == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (verificationType == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.currentParams = currentParams;
            this.phone = currentParams.getString("phone");
            this.phoneHash = currentParams.getString("phoneHash");
            final int int1 = currentParams.getInt("timeout");
            this.time = int1;
            this.timeout = int1;
            this.openTime = (int)(System.currentTimeMillis() / 1000L);
            this.nextType = currentParams.getInt("nextType");
            this.pattern = currentParams.getString("pattern");
            this.length = currentParams.getInt("length");
            if (this.length == 0) {
                this.length = 5;
            }
            final EditTextBoldCursor[] codeField = this.codeField;
            Object text = "";
            int n = 8;
            if (codeField != null && codeField.length == this.length) {
                int n2 = 0;
                while (true) {
                    final EditTextBoldCursor[] codeField2 = this.codeField;
                    if (n2 >= codeField2.length) {
                        break;
                    }
                    codeField2[n2].setText((CharSequence)"");
                    ++n2;
                }
            }
            else {
                this.codeField = new EditTextBoldCursor[this.length];
                for (int i = 0; i < this.length; ++i) {
                    (this.codeField[i] = new EditTextBoldCursor(this.getContext())).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.codeField[i].setCursorWidth(1.5f);
                    final Drawable mutate = this.getResources().getDrawable(2131165811).mutate();
                    mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), PorterDuff$Mode.MULTIPLY));
                    this.codeField[i].setBackgroundDrawable(mutate);
                    this.codeField[i].setImeOptions(268435461);
                    this.codeField[i].setTextSize(1, 20.0f);
                    this.codeField[i].setMaxLines(1);
                    this.codeField[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.codeField[i].setPadding(0, 0, 0, 0);
                    this.codeField[i].setGravity(49);
                    if (this.verificationType == 3) {
                        this.codeField[i].setEnabled(false);
                        this.codeField[i].setInputType(0);
                        this.codeField[i].setVisibility(8);
                    }
                    else {
                        this.codeField[i].setInputType(3);
                    }
                    final LinearLayout codeFieldContainer = this.codeFieldContainer;
                    final EditTextBoldCursor editTextBoldCursor = this.codeField[i];
                    int n3;
                    if (i != this.length - 1) {
                        n3 = 7;
                    }
                    else {
                        n3 = 0;
                    }
                    codeFieldContainer.addView((View)editTextBoldCursor, (ViewGroup$LayoutParams)LayoutHelper.createLinear(34, 36, 1, 0, 0, n3, 0));
                    this.codeField[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        public void afterTextChanged(final Editable editable) {
                            if (PhoneConfirmationView.this.ignoreOnTextChange) {
                                return;
                            }
                            final int length = editable.length();
                            if (length >= 1) {
                                if (length > 1) {
                                    final String string = editable.toString();
                                    PhoneConfirmationView.this.ignoreOnTextChange = true;
                                    for (int i = 0; i < Math.min(PhoneConfirmationView.this.length - i, length); ++i) {
                                        if (i == 0) {
                                            editable.replace(0, length, (CharSequence)string.substring(i, i + 1));
                                        }
                                        else {
                                            PhoneConfirmationView.this.codeField[i + i].setText((CharSequence)string.substring(i, i + 1));
                                        }
                                    }
                                    PhoneConfirmationView.this.ignoreOnTextChange = false;
                                }
                                if (i != PhoneConfirmationView.this.length - 1) {
                                    PhoneConfirmationView.this.codeField[i + 1].setSelection(PhoneConfirmationView.this.codeField[i + 1].length());
                                    PhoneConfirmationView.this.codeField[i + 1].requestFocus();
                                }
                                if ((i == PhoneConfirmationView.this.length - 1 || (i == PhoneConfirmationView.this.length - 2 && length >= 2)) && PhoneConfirmationView.this.getCode().length() == PhoneConfirmationView.this.length) {
                                    PhoneConfirmationView.this.onNextPressed();
                                }
                            }
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                    this.codeField[i].setOnKeyListener((View$OnKeyListener)new _$$Lambda$PassportActivity$PhoneConfirmationView$Hi5vGNsckWBWmshMz8y5BhPDMNw(this, i));
                    this.codeField[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PassportActivity$PhoneConfirmationView$IfZlFmJVSS8IJyDQ8zJhpbvhePs(this));
                }
            }
            final ProgressView progressView = this.progressView;
            if (progressView != null) {
                int visibility;
                if (this.nextType != 0) {
                    visibility = 0;
                }
                else {
                    visibility = 8;
                }
                progressView.setVisibility(visibility);
            }
            if (this.phone == null) {
                return;
            }
            final PhoneFormat instance = PhoneFormat.getInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(this.phone);
            final String format = instance.format(sb.toString());
            final int verificationType2 = this.verificationType;
            if (verificationType2 == 2) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131560721, LocaleController.addNbsp(format)));
            }
            else if (verificationType2 == 3) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131560719, LocaleController.addNbsp(format)));
            }
            else if (verificationType2 == 4) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131560720, LocaleController.addNbsp(format)));
            }
            this.confirmTextView.setText((CharSequence)text);
            if (this.verificationType != 3) {
                AndroidUtilities.showKeyboard((View)this.codeField[0]);
                this.codeField[0].requestFocus();
            }
            else {
                AndroidUtilities.hideKeyboard((View)this.codeField[0]);
            }
            this.destroyTimer();
            this.destroyCodeTimer();
            this.lastCurrentTime = (double)System.currentTimeMillis();
            if (this.verificationType == 3) {
                final int nextType = this.nextType;
                if (nextType == 4 || nextType == 2) {
                    this.problemText.setVisibility(8);
                    this.timeText.setVisibility(0);
                    final int nextType2 = this.nextType;
                    if (nextType2 == 4) {
                        this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 1, 0));
                    }
                    else if (nextType2 == 2) {
                        this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 1, 0));
                    }
                    this.createTimer();
                    return;
                }
            }
            if (this.verificationType == 2) {
                final int nextType3 = this.nextType;
                if (nextType3 == 4 || nextType3 == 3) {
                    this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 2, 0));
                    final TextView problemText = this.problemText;
                    int visibility2;
                    if (this.time < 1000) {
                        visibility2 = 0;
                    }
                    else {
                        visibility2 = 8;
                    }
                    problemText.setVisibility(visibility2);
                    final TextView timeText = this.timeText;
                    if (this.time >= 1000) {
                        n = 0;
                    }
                    timeText.setVisibility(n);
                    this.createTimer();
                    return;
                }
            }
            if (this.verificationType == 4 && this.nextType == 2) {
                this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 2, 0));
                final TextView problemText2 = this.problemText;
                int visibility3;
                if (this.time < 1000) {
                    visibility3 = 0;
                }
                else {
                    visibility3 = 8;
                }
                problemText2.setVisibility(visibility3);
                final TextView timeText2 = this.timeText;
                if (this.time >= 1000) {
                    n = 0;
                }
                timeText2.setVisibility(n);
                this.createTimer();
            }
            else {
                this.timeText.setVisibility(8);
                this.problemText.setVisibility(8);
                this.createCodeTimer();
            }
        }
    }
    
    private class ProgressView extends View
    {
        private Paint paint;
        private Paint paint2;
        private float progress;
        
        public ProgressView(final Context context) {
            super(context);
            this.paint = new Paint();
            this.paint2 = new Paint();
            this.paint.setColor(Theme.getColor("login_progressInner"));
            this.paint2.setColor(Theme.getColor("login_progressOuter"));
        }
        
        protected void onDraw(final Canvas canvas) {
            final float n = (float)(int)(this.getMeasuredWidth() * this.progress);
            canvas.drawRect(0.0f, 0.0f, n, (float)this.getMeasuredHeight(), this.paint2);
            canvas.drawRect(n, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
        }
        
        public void setProgress(final float progress) {
            this.progress = progress;
            this.invalidate();
        }
    }
    
    public class SecureDocumentCell extends FrameLayout implements FileDownloadProgressListener
    {
        private int TAG;
        private int buttonState;
        private SecureDocument currentSecureDocument;
        private BackupImageView imageView;
        private RadialProgress radialProgress;
        private TextView textView;
        private TextView valueTextView;
        
        public SecureDocumentCell(final Context context) {
            super(context);
            this.TAG = DownloadController.getInstance(PassportActivity.this.currentAccount).generateObserverTag();
            this.radialProgress = new RadialProgress((View)this);
            this.imageView = new BackupImageView(context);
            final BackupImageView imageView = this.imageView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n2 | 0x30, 21.0f, 8.0f, 21.0f, 0.0f));
            (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            final TextView textView = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            textView.setGravity(n3 | 0x10);
            final TextView textView2 = this.textView;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n5 = 21;
            int n6;
            if (isRTL2) {
                n6 = 21;
            }
            else {
                n6 = 81;
            }
            final float n7 = (float)n6;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 81;
            }
            else {
                n8 = 21;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4 | 0x30, n7, 10.0f, (float)n8, 0.0f));
            (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            final TextView valueTextView = this.valueTextView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            valueTextView.setGravity(gravity);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setPadding(0, 0, 0, 0);
            final TextView valueTextView2 = this.valueTextView;
            int n9;
            if (LocaleController.isRTL) {
                n9 = n;
            }
            else {
                n9 = 3;
            }
            int n10;
            if (LocaleController.isRTL) {
                n10 = 21;
            }
            else {
                n10 = 81;
            }
            final float n11 = (float)n10;
            int n12 = n5;
            if (LocaleController.isRTL) {
                n12 = 81;
            }
            this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n9 | 0x30, n11, 35.0f, (float)n12, 0.0f));
            this.setWillNotDraw(false);
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            final boolean drawChild = super.drawChild(canvas, view, n);
            if (view == this.imageView) {
                this.radialProgress.draw(canvas);
            }
            return drawChild;
        }
        
        public int getObserverTag() {
            return this.TAG;
        }
        
        public void invalidate() {
            super.invalidate();
            this.textView.invalidate();
        }
        
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
        
        public void onFailedDownload(final String s, final boolean b) {
            this.updateButtonState(false);
        }
        
        protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            n2 = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() - AndroidUtilities.dp(24.0f)) / 2;
            n = this.imageView.getTop() + (this.imageView.getMeasuredHeight() - AndroidUtilities.dp(24.0f)) / 2;
            this.radialProgress.setProgressRect(n2, n, AndroidUtilities.dp(24.0f) + n2, AndroidUtilities.dp(24.0f) + n);
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + 1, 1073741824));
        }
        
        public void onProgressDownload(final String s, final float n) {
            this.radialProgress.setProgress(n, true);
            if (this.buttonState != 1) {
                this.updateButtonState(false);
            }
        }
        
        public void onProgressUpload(final String s, final float n, final boolean b) {
            this.radialProgress.setProgress(n, true);
        }
        
        public void onSuccessDownload(final String s) {
            this.radialProgress.setProgress(1.0f, true);
            this.updateButtonState(true);
        }
        
        public void setTextAndValueAndImage(final String text, final CharSequence text2, final SecureDocument currentSecureDocument) {
            this.textView.setText((CharSequence)text);
            this.valueTextView.setText(text2);
            this.imageView.setImage(currentSecureDocument, "48_48");
            this.currentSecureDocument = currentSecureDocument;
            this.updateButtonState(false);
        }
        
        public void setValue(final CharSequence text) {
            this.valueTextView.setText(text);
        }
        
        public void updateButtonState(final boolean b) {
            final String attachFileName = FileLoader.getAttachFileName(this.currentSecureDocument);
            final boolean exists = FileLoader.getPathToAttach(this.currentSecureDocument).exists();
            if (TextUtils.isEmpty((CharSequence)attachFileName)) {
                this.radialProgress.setBackground(null, false, false);
                return;
            }
            final SecureDocument currentSecureDocument = this.currentSecureDocument;
            final String path = currentSecureDocument.path;
            final float n = 0.0f;
            float floatValue = 0.0f;
            if (path != null) {
                if (currentSecureDocument.inputFile != null) {
                    DownloadController.getInstance(PassportActivity.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                    this.radialProgress.setBackground(null, false, b);
                    this.buttonState = -1;
                }
                else {
                    DownloadController.getInstance(PassportActivity.this.currentAccount).addLoadingFileObserver(this.currentSecureDocument.path, (DownloadController.FileDownloadProgressListener)this);
                    this.buttonState = 1;
                    final Float fileProgress = ImageLoader.getInstance().getFileProgress(this.currentSecureDocument.path);
                    this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, b);
                    final RadialProgress radialProgress = this.radialProgress;
                    if (fileProgress != null) {
                        floatValue = fileProgress;
                    }
                    radialProgress.setProgress(floatValue, false);
                    this.invalidate();
                }
            }
            else if (exists) {
                DownloadController.getInstance(PassportActivity.this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                this.buttonState = -1;
                this.radialProgress.setBackground(null, false, b);
                this.invalidate();
            }
            else {
                DownloadController.getInstance(PassportActivity.this.currentAccount).addLoadingFileObserver(attachFileName, (DownloadController.FileDownloadProgressListener)this);
                this.buttonState = 1;
                final Float fileProgress2 = ImageLoader.getInstance().getFileProgress(attachFileName);
                this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, b);
                final RadialProgress radialProgress2 = this.radialProgress;
                float floatValue2 = n;
                if (fileProgress2 != null) {
                    floatValue2 = fileProgress2;
                }
                radialProgress2.setProgress(floatValue2, b);
                this.invalidate();
            }
        }
    }
    
    public class TextDetailSecureCell extends FrameLayout
    {
        private ImageView checkImageView;
        private boolean needDivider;
        private TextView textView;
        private TextView valueTextView;
        
        public TextDetailSecureCell(final Context context) {
            super(context);
            int n;
            if (PassportActivity.this.currentActivityType == 8) {
                n = 21;
            }
            else {
                n = 51;
            }
            (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n2 = 5;
            int n3;
            if (isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            textView.setGravity(n3 | 0x10);
            final TextView textView2 = this.textView;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            int n5;
            if (LocaleController.isRTL) {
                n5 = n;
            }
            else {
                n5 = 21;
            }
            final float n6 = (float)n5;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 21;
            }
            else {
                n7 = n;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4 | 0x30, n6, 10.0f, (float)n7, 0.0f));
            (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextSize(1, 13.0f);
            final TextView valueTextView = this.valueTextView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            valueTextView.setGravity(gravity);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
            final TextView valueTextView2 = this.valueTextView;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 5;
            }
            else {
                n8 = 3;
            }
            int n9;
            if (LocaleController.isRTL) {
                n9 = n;
            }
            else {
                n9 = 21;
            }
            final float n10 = (float)n9;
            if (LocaleController.isRTL) {
                n = 21;
            }
            this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8 | 0x30, n10, 35.0f, (float)n, 0.0f));
            (this.checkImageView = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
            this.checkImageView.setImageResource(2131165858);
            final ImageView checkImageView = this.checkImageView;
            int n11 = n2;
            if (LocaleController.isRTL) {
                n11 = 3;
            }
            this.addView((View)checkImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n11 | 0x30, 21.0f, 25.0f, 21.0f, 0.0f));
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.needDivider) {
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
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }
        
        public void setChecked(final boolean b) {
            final ImageView checkImageView = this.checkImageView;
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            checkImageView.setVisibility(visibility);
        }
        
        public void setNeedDivider(final boolean needDivider) {
            this.needDivider = needDivider;
            this.setWillNotDraw(this.needDivider ^ true);
            this.invalidate();
        }
        
        public void setTextAndValue(final String text, final CharSequence text2, final boolean needDivider) {
            this.textView.setText((CharSequence)text);
            this.valueTextView.setText(text2);
            this.setWillNotDraw((this.needDivider = needDivider) ^ true);
        }
        
        public void setValue(final CharSequence text) {
            this.valueTextView.setText(text);
        }
    }
}
