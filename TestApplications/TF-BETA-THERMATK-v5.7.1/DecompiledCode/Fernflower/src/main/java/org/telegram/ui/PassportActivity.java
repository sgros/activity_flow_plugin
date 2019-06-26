package org.telegram.ui;

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
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import androidx.core.content.FileProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SecureDocument;
import org.telegram.messenger.SecureDocumentKey;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SmsReceiver;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class PassportActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
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
   private ArrayList availableDocumentTypes;
   private TextInfoPrivacyCell bottomCell;
   private TextInfoPrivacyCell bottomCellTranslation;
   private FrameLayout bottomLayout;
   private boolean callbackCalled;
   private ChatAttachAlert chatAttachAlert;
   private HashMap codesMap;
   private ArrayList countriesArray;
   private HashMap countriesMap;
   private int currentActivityType;
   private int currentBotId;
   private String currentCallbackUrl;
   private String currentCitizeship;
   private HashMap currentDocumentValues;
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
   private HashMap currentValues;
   private int currentViewNum;
   private PassportActivity.PassportActivityDelegate delegate;
   private TextSettingsCell deletePassportCell;
   private ArrayList dividers;
   private boolean documentOnly;
   private ArrayList documents;
   private HashMap documentsCells;
   private HashMap documentsErrors;
   private LinearLayout documentsLayout;
   private HashMap documentsToTypesLink;
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private int emailCodeLength;
   private ImageView emptyImageView;
   private LinearLayout emptyLayout;
   private TextView emptyTextView1;
   private TextView emptyTextView2;
   private TextView emptyTextView3;
   private EmptyTextProgressView emptyView;
   private HashMap errorsMap;
   private HashMap errorsValues;
   private View extraBackgroundView;
   private View extraBackgroundView2;
   private HashMap fieldsErrors;
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
   private HashMap languageMap;
   private LinearLayout linearLayout2;
   private HashMap mainErrorsMap;
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
   private PassportActivity.PassportActivityDelegate pendingDelegate;
   private PassportActivity.ErrorRunnable pendingErrorRunnable;
   private Runnable pendingFinishRunnable;
   private String pendingPhone;
   private Dialog permissionsDialog;
   private ArrayList permissionsItems;
   private HashMap phoneFormatMap;
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
   private ArrayList translationDocuments;
   private LinearLayout translationLayout;
   private HashMap typesValues;
   private HashMap typesViews;
   private TextSettingsCell uploadDocumentCell;
   private TextDetailSettingsCell uploadFrontCell;
   private TextDetailSettingsCell uploadReverseCell;
   private TextDetailSettingsCell uploadSelfieCell;
   private TextSettingsCell uploadTranslationCell;
   private HashMap uploadingDocuments;
   private int uploadingFileType;
   private boolean useCurrentValue;
   private int usingSavedPassword;
   private SlideView[] views;

   public PassportActivity(int var1, int var2, String var3, String var4, String var5, String var6, String var7, TLRPC.TL_account_authorizationForm var8, TLRPC.TL_account_password var9) {
      TLRPC.TL_account_authorizationForm var10 = var8;
      this(var1, var8, var9, (TLRPC.TL_secureRequiredType)null, (TLRPC.TL_secureValue)null, (TLRPC.TL_secureRequiredType)null, (TLRPC.TL_secureValue)null, (HashMap)null, (HashMap)null);
      this.currentBotId = var2;
      this.currentPayload = var5;
      this.currentNonce = var6;
      this.currentScope = var3;
      this.currentPublicKey = var4;
      this.currentCallbackUrl = var7;
      if (var1 == 0 && !var8.errors.isEmpty()) {
         boolean var10001;
         try {
            ArrayList var44 = var10.errors;
            Comparator var41 = new Comparator() {
               public int compare(TLRPC.SecureValueError var1, TLRPC.SecureValueError var2) {
                  int var3 = this.getErrorValue(var1);
                  int var4 = this.getErrorValue(var2);
                  if (var3 < var4) {
                     return -1;
                  } else {
                     return var3 > var4 ? 1 : 0;
                  }
               }

               int getErrorValue(TLRPC.SecureValueError var1) {
                  if (var1 instanceof TLRPC.TL_secureValueError) {
                     return 0;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorFrontSide) {
                     return 1;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorReverseSide) {
                     return 2;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorSelfie) {
                     return 3;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorTranslationFile) {
                     return 4;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorTranslationFiles) {
                     return 5;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorFile) {
                     return 6;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorFiles) {
                     return 7;
                  } else if (var1 instanceof TLRPC.TL_secureValueErrorData) {
                     TLRPC.TL_secureValueErrorData var2 = (TLRPC.TL_secureValueErrorData)var1;
                     return PassportActivity.this.getFieldCost(var2.field);
                  } else {
                     return 100;
                  }
               }
            };
            Collections.sort(var44, var41);
            var1 = var10.errors.size();
         } catch (Exception var40) {
            var10001 = false;
            return;
         }

         var2 = 0;

         while(true) {
            TLRPC.TL_account_authorizationForm var42 = var8;
            if (var2 >= var1) {
               break;
            }

            boolean var11;
            TLRPC.SecureValueError var45;
            try {
               var45 = (TLRPC.SecureValueError)var42.errors.get(var2);
               var11 = var45 instanceof TLRPC.TL_secureValueErrorFrontSide;
            } catch (Exception var24) {
               var10001 = false;
               break;
            }

            label295: {
               var7 = null;
               byte[] var47;
               if (var11) {
                  try {
                     TLRPC.TL_secureValueErrorFrontSide var43 = (TLRPC.TL_secureValueErrorFrontSide)var45;
                     var6 = this.getNameForType(var43.type);
                     var5 = var43.text;
                     var47 = var43.file_hash;
                  } catch (Exception var23) {
                     var10001 = false;
                     break;
                  }

                  var3 = "front";
               } else {
                  label296: {
                     label278: {
                        try {
                           if (!(var45 instanceof TLRPC.TL_secureValueErrorReverseSide)) {
                              break label278;
                           }

                           TLRPC.TL_secureValueErrorReverseSide var46 = (TLRPC.TL_secureValueErrorReverseSide)var45;
                           var6 = this.getNameForType(var46.type);
                           var5 = var46.text;
                           var47 = var46.file_hash;
                        } catch (Exception var39) {
                           var10001 = false;
                           break;
                        }

                        var3 = "reverse";
                        break label296;
                     }

                     label271: {
                        try {
                           if (!(var45 instanceof TLRPC.TL_secureValueErrorSelfie)) {
                              break label271;
                           }

                           TLRPC.TL_secureValueErrorSelfie var48 = (TLRPC.TL_secureValueErrorSelfie)var45;
                           var6 = this.getNameForType(var48.type);
                           var5 = var48.text;
                           var47 = var48.file_hash;
                        } catch (Exception var38) {
                           var10001 = false;
                           break;
                        }

                        var3 = "selfie";
                        break label296;
                     }

                     label297: {
                        try {
                           if (var45 instanceof TLRPC.TL_secureValueErrorTranslationFile) {
                              TLRPC.TL_secureValueErrorTranslationFile var55 = (TLRPC.TL_secureValueErrorTranslationFile)var45;
                              var6 = this.getNameForType(var55.type);
                              var5 = var55.text;
                              var47 = var55.file_hash;
                              break label297;
                           }
                        } catch (Exception var36) {
                           var10001 = false;
                           break;
                        }

                        label298: {
                           try {
                              if (var45 instanceof TLRPC.TL_secureValueErrorTranslationFiles) {
                                 TLRPC.TL_secureValueErrorTranslationFiles var54 = (TLRPC.TL_secureValueErrorTranslationFiles)var45;
                                 var6 = this.getNameForType(var54.type);
                                 var5 = var54.text;
                                 break label298;
                              }
                           } catch (Exception var37) {
                              var10001 = false;
                              break;
                           }

                           label299: {
                              try {
                                 if (var45 instanceof TLRPC.TL_secureValueErrorFile) {
                                    TLRPC.TL_secureValueErrorFile var53 = (TLRPC.TL_secureValueErrorFile)var45;
                                    var6 = this.getNameForType(var53.type);
                                    var5 = var53.text;
                                    var47 = var53.file_hash;
                                    break label299;
                                 }
                              } catch (Exception var34) {
                                 var10001 = false;
                                 break;
                              }

                              label300: {
                                 try {
                                    if (var45 instanceof TLRPC.TL_secureValueErrorFiles) {
                                       TLRPC.TL_secureValueErrorFiles var52 = (TLRPC.TL_secureValueErrorFiles)var45;
                                       var6 = this.getNameForType(var52.type);
                                       var5 = var52.text;
                                       break label300;
                                    }
                                 } catch (Exception var35) {
                                    var10001 = false;
                                    break;
                                 }

                                 label236: {
                                    try {
                                       if (!(var45 instanceof TLRPC.TL_secureValueError)) {
                                          break label236;
                                       }

                                       TLRPC.TL_secureValueError var49 = (TLRPC.TL_secureValueError)var45;
                                       var6 = this.getNameForType(var49.type);
                                       var5 = var49.text;
                                       var47 = var49.hash;
                                    } catch (Exception var33) {
                                       var10001 = false;
                                       break;
                                    }

                                    var3 = "error_all";
                                    break label296;
                                 }

                                 TLRPC.TL_secureValueErrorData var50;
                                 try {
                                    if (!(var45 instanceof TLRPC.TL_secureValueErrorData)) {
                                       break label295;
                                    }

                                    var50 = (TLRPC.TL_secureValueErrorData)var45;
                                 } catch (Exception var31) {
                                    var10001 = false;
                                    break;
                                 }

                                 int var12 = 0;

                                 boolean var59;
                                 label228: {
                                    while(true) {
                                       label225: {
                                          try {
                                             if (var12 < var42.values.size()) {
                                                TLRPC.TL_secureValue var51 = (TLRPC.TL_secureValue)var42.values.get(var12);
                                                if (var51.data != null && Arrays.equals(var51.data.data_hash, var50.data_hash)) {
                                                   break;
                                                }
                                                break label225;
                                             }
                                          } catch (Exception var32) {
                                             var10001 = false;
                                             return;
                                          }

                                          var59 = false;
                                          break label228;
                                       }

                                       ++var12;
                                    }

                                    var59 = true;
                                 }

                                 if (!var59) {
                                    break label295;
                                 }

                                 try {
                                    var6 = this.getNameForType(var50.type);
                                    var5 = var50.text;
                                    var7 = var50.field;
                                    var47 = var50.data_hash;
                                 } catch (Exception var22) {
                                    var10001 = false;
                                    break;
                                 }

                                 var3 = "data";
                                 break label296;
                              }

                              var47 = null;
                           }

                           var3 = "files";
                           break label296;
                        }

                        var47 = null;
                     }

                     var3 = "translation";
                  }
               }

               HashMap var58;
               try {
                  var58 = (HashMap)this.errorsMap.get(var6);
               } catch (Exception var21) {
                  var10001 = false;
                  break;
               }

               HashMap var56 = var58;
               if (var58 == null) {
                  try {
                     var56 = new HashMap();
                     this.errorsMap.put(var6, var56);
                     this.mainErrorsMap.put(var6, var5);
                  } catch (Exception var20) {
                     var10001 = false;
                     break;
                  }
               }

               if (var47 != null) {
                  try {
                     var6 = Base64.encodeToString(var47, 2);
                  } catch (Exception var19) {
                     var10001 = false;
                     break;
                  }
               } else {
                  var6 = "";
               }

               label200: {
                  try {
                     if (!"data".equals(var3)) {
                        break label200;
                     }
                  } catch (Exception var30) {
                     var10001 = false;
                     break;
                  }

                  if (var7 != null) {
                     try {
                        var56.put(var7, var5);
                     } catch (Exception var18) {
                        var10001 = false;
                        break;
                     }
                  }
                  break label295;
               }

               StringBuilder var57;
               label302: {
                  try {
                     if ("files".equals(var3)) {
                        break label302;
                     }
                  } catch (Exception var29) {
                     var10001 = false;
                     break;
                  }

                  try {
                     if ("selfie".equals(var3)) {
                        var57 = new StringBuilder();
                        var57.append("selfie");
                        var57.append(var6);
                        var56.put(var57.toString(), var5);
                        break label295;
                     }
                  } catch (Exception var28) {
                     var10001 = false;
                     break;
                  }

                  label182: {
                     try {
                        if (!"translation".equals(var3)) {
                           break label182;
                        }
                     } catch (Exception var27) {
                        var10001 = false;
                        break;
                     }

                     if (var47 != null) {
                        try {
                           var57 = new StringBuilder();
                           var57.append("translation");
                           var57.append(var6);
                           var56.put(var57.toString(), var5);
                           break label295;
                        } catch (Exception var15) {
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           var56.put("translation_all", var5);
                           break label295;
                        } catch (Exception var14) {
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  try {
                     if ("front".equals(var3)) {
                        var57 = new StringBuilder();
                        var57.append("front");
                        var57.append(var6);
                        var56.put(var57.toString(), var5);
                        break label295;
                     }
                  } catch (Exception var26) {
                     var10001 = false;
                     break;
                  }

                  try {
                     if ("reverse".equals(var3)) {
                        var57 = new StringBuilder();
                        var57.append("reverse");
                        var57.append(var6);
                        var56.put(var57.toString(), var5);
                        break label295;
                     }
                  } catch (Exception var25) {
                     var10001 = false;
                     break;
                  }

                  try {
                     if ("error_all".equals(var3)) {
                        var56.put("error_all", var5);
                     }
                     break label295;
                  } catch (Exception var13) {
                     var10001 = false;
                     break;
                  }
               }

               if (var47 != null) {
                  try {
                     var57 = new StringBuilder();
                     var57.append("files");
                     var57.append(var6);
                     var56.put(var57.toString(), var5);
                  } catch (Exception var17) {
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     var56.put("files_all", var5);
                  } catch (Exception var16) {
                     var10001 = false;
                     break;
                  }
               }
            }

            ++var2;
         }
      }

   }

   public PassportActivity(int var1, TLRPC.TL_account_authorizationForm var2, TLRPC.TL_account_password var3, TLRPC.TL_secureRequiredType var4, TLRPC.TL_secureValue var5, TLRPC.TL_secureRequiredType var6, TLRPC.TL_secureValue var7, HashMap var8, HashMap var9) {
      this.currentCitizeship = "";
      this.currentResidence = "";
      this.currentExpireDate = new int[3];
      this.dividers = new ArrayList();
      this.nonLatinNames = new boolean[3];
      this.allowNonLatinName = true;
      this.countriesArray = new ArrayList();
      this.countriesMap = new HashMap();
      this.codesMap = new HashMap();
      this.phoneFormatMap = new HashMap();
      this.documents = new ArrayList();
      this.translationDocuments = new ArrayList();
      this.documentsCells = new HashMap();
      this.uploadingDocuments = new HashMap();
      this.typesValues = new HashMap();
      this.typesViews = new HashMap();
      this.documentsToTypesLink = new HashMap();
      this.errorsMap = new HashMap();
      this.mainErrorsMap = new HashMap();
      this.errorsValues = new HashMap();
      this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
         public void deleteImageAtIndex(int var1) {
            SecureDocument var2;
            if (PassportActivity.this.uploadingFileType == 1) {
               var2 = PassportActivity.this.selfieDocument;
            } else if (PassportActivity.this.uploadingFileType == 4) {
               var2 = (SecureDocument)PassportActivity.this.translationDocuments.get(var1);
            } else if (PassportActivity.this.uploadingFileType == 2) {
               var2 = PassportActivity.this.frontDocument;
            } else if (PassportActivity.this.uploadingFileType == 3) {
               var2 = PassportActivity.this.reverseDocument;
            } else {
               var2 = (SecureDocument)PassportActivity.this.documents.get(var1);
            }

            PassportActivity.SecureDocumentCell var3 = (PassportActivity.SecureDocumentCell)PassportActivity.this.documentsCells.remove(var2);
            if (var3 != null) {
               String var4 = PassportActivity.this.getDocumentHash(var2);
               var1 = PassportActivity.this.uploadingFileType;
               String var5 = null;
               StringBuilder var6;
               if (var1 == 1) {
                  PassportActivity.this.selfieDocument = null;
                  var6 = new StringBuilder();
                  var6.append("selfie");
                  var6.append(var4);
                  var5 = var6.toString();
               } else if (PassportActivity.this.uploadingFileType == 4) {
                  var6 = new StringBuilder();
                  var6.append("translation");
                  var6.append(var4);
                  var5 = var6.toString();
               } else if (PassportActivity.this.uploadingFileType == 2) {
                  PassportActivity.this.frontDocument = null;
                  var6 = new StringBuilder();
                  var6.append("front");
                  var6.append(var4);
                  var5 = var6.toString();
               } else if (PassportActivity.this.uploadingFileType == 3) {
                  PassportActivity.this.reverseDocument = null;
                  var6 = new StringBuilder();
                  var6.append("reverse");
                  var6.append(var4);
                  var5 = var6.toString();
               } else if (PassportActivity.this.uploadingFileType == 0) {
                  var6 = new StringBuilder();
                  var6.append("files");
                  var6.append(var4);
                  var5 = var6.toString();
               }

               if (var5 != null) {
                  if (PassportActivity.this.documentsErrors != null) {
                     PassportActivity.this.documentsErrors.remove(var5);
                  }

                  if (PassportActivity.this.errorsValues != null) {
                     PassportActivity.this.errorsValues.remove(var5);
                  }
               }

               PassportActivity var7 = PassportActivity.this;
               var7.updateUploadText(var7.uploadingFileType);
               PassportActivity.this.currentPhotoViewerLayout.removeView(var3);
            }
         }

         public String getDeleteMessageString() {
            return PassportActivity.this.uploadingFileType == 1 ? LocaleController.formatString("PassportDeleteSelfieAlert", 2131560211) : LocaleController.formatString("PassportDeleteScanAlert", 2131560209);
         }

         public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
            if (var3 >= 0 && var3 < PassportActivity.this.currentPhotoViewerLayout.getChildCount()) {
               PassportActivity.SecureDocumentCell var8 = (PassportActivity.SecureDocumentCell)PassportActivity.this.currentPhotoViewerLayout.getChildAt(var3);
               int[] var7 = new int[2];
               var8.imageView.getLocationInWindow(var7);
               PhotoViewer.PlaceProviderObject var5 = new PhotoViewer.PlaceProviderObject();
               var3 = 0;
               var5.viewX = var7[0];
               int var6 = var7[1];
               if (VERSION.SDK_INT < 21) {
                  var3 = AndroidUtilities.statusBarHeight;
               }

               var5.viewY = var6 - var3;
               var5.parentView = PassportActivity.this.currentPhotoViewerLayout;
               var5.imageReceiver = var8.imageView.getImageReceiver();
               var5.thumb = var5.imageReceiver.getBitmapSafe();
               return var5;
            } else {
               return null;
            }
         }
      };
      this.currentActivityType = var1;
      this.currentForm = var2;
      this.currentType = var4;
      TLRPC.TL_secureRequiredType var11 = this.currentType;
      if (var11 != null) {
         this.allowNonLatinName = var11.native_names;
      }

      this.currentTypeValue = var5;
      this.currentDocumentsType = var6;
      this.currentDocumentsTypeValue = var7;
      this.currentPassword = var3;
      this.currentValues = var8;
      this.currentDocumentValues = var9;
      int var10 = this.currentActivityType;
      if (var10 == 3) {
         this.permissionsItems = new ArrayList();
      } else if (var10 == 7) {
         this.views = new SlideView[3];
      }

      if (this.currentValues == null) {
         this.currentValues = new HashMap();
      }

      if (this.currentDocumentValues == null) {
         this.currentDocumentValues = new HashMap();
      }

      if (var1 == 5) {
         if (UserConfig.getInstance(super.currentAccount).savedPasswordHash != null && UserConfig.getInstance(super.currentAccount).savedSaltedPassword != null) {
            this.usingSavedPassword = 1;
            this.savedPasswordHash = UserConfig.getInstance(super.currentAccount).savedPasswordHash;
            this.savedSaltedPassword = UserConfig.getInstance(super.currentAccount).savedSaltedPassword;
         }

         TLRPC.TL_account_password var12 = this.currentPassword;
         if (var12 == null) {
            this.loadPasswordInfo();
         } else {
            TwoStepVerificationActivity.initPasswordNewAlgo(var12);
            if (this.usingSavedPassword == 1) {
               this.onPasswordDone(true);
            }
         }

         if (!SharedConfig.isPassportConfigLoaded()) {
            TLRPC.TL_help_getPassportConfig var13 = new TLRPC.TL_help_getPassportConfig();
            var13.hash = SharedConfig.passportConfigHash;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var13, _$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM.INSTANCE);
         }
      }

   }

   // $FF: synthetic method
   static int access$11700(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$11900(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$12000(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$12100(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$12300(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$12400(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static boolean access$12502(PassportActivity var0, boolean var1) {
      var0.ignoreOnFailure = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$12600(PassportActivity var0) {
      return var0.currentBotId;
   }

   // $FF: synthetic method
   static String access$12700(PassportActivity var0) {
      return var0.currentScope;
   }

   // $FF: synthetic method
   static String access$12800(PassportActivity var0) {
      return var0.currentPublicKey;
   }

   // $FF: synthetic method
   static String access$12900(PassportActivity var0) {
      return var0.currentPayload;
   }

   // $FF: synthetic method
   static String access$13000(PassportActivity var0) {
      return var0.currentNonce;
   }

   // $FF: synthetic method
   static TLRPC.TL_account_authorizationForm access$1302(PassportActivity var0, TLRPC.TL_account_authorizationForm var1) {
      var0.currentForm = var1;
      return var1;
   }

   // $FF: synthetic method
   static String access$13100(PassportActivity var0) {
      return var0.currentCallbackUrl;
   }

   // $FF: synthetic method
   static int access$13202(PassportActivity var0, int var1) {
      var0.currentAccount = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$13300(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static boolean access$13400(PassportActivity var0) {
      return var0.needActivityResult;
   }

   // $FF: synthetic method
   static boolean access$13402(PassportActivity var0, boolean var1) {
      var0.needActivityResult = var1;
      return var1;
   }

   // $FF: synthetic method
   static ActionBarLayout access$13500(PassportActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$13600(PassportActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static PassportActivity access$13702(PassportActivity var0, PassportActivity var1) {
      var0.presentAfterAnimation = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$13800(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$13900(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$14000(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$14102(PassportActivity var0, int var1) {
      var0.usingSavedPassword = var1;
      return var1;
   }

   // $FF: synthetic method
   static void access$14200(PassportActivity var0) {
      var0.updatePasswordInterface();
   }

   // $FF: synthetic method
   static ViewGroup[] access$14300(PassportActivity var0) {
      return var0.inputFieldContainers;
   }

   // $FF: synthetic method
   static void access$14400(PassportActivity var0, boolean var1) {
      var0.onPasscodeError(var1);
   }

   // $FF: synthetic method
   static byte[] access$14500(PassportActivity var0) {
      return var0.savedSaltedPassword;
   }

   // $FF: synthetic method
   static byte[] access$14600(PassportActivity var0, byte[] var1, byte[] var2) {
      return var0.decryptSecret(var1, var2);
   }

   // $FF: synthetic method
   static int access$14700(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$14800(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static byte[] access$14900(PassportActivity var0) {
      return var0.getRandomSecret();
   }

   // $FF: synthetic method
   static int access$1500(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$15000(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$15100(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$15200(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1600(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1700(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static String access$5102(PassportActivity var0, String var1) {
      var0.currentEmail = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$5200(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5300(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5400(PassportActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$5500(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5600(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static long access$7002(PassportActivity var0, long var1) {
      var0.secureSecretId = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$7100(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7200(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static TLRPC.TL_account_password access$7302(PassportActivity var0, TLRPC.TL_account_password var1) {
      var0.currentPassword = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$7402(PassportActivity var0, int var1) {
      var0.currentAccount = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$7500(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7900(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$8300(PassportActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static View access$9300(PassportActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static int access$9400(PassportActivity var0) {
      return var0.currentAccount;
   }

   private void addDocumentView(SecureDocument var1, int var2) {
      if (var2 == 1) {
         this.selfieDocument = var1;
         if (this.selfieLayout == null) {
            return;
         }
      } else if (var2 == 4) {
         this.translationDocuments.add(var1);
         if (this.translationLayout == null) {
            return;
         }
      } else if (var2 == 2) {
         this.frontDocument = var1;
         if (this.frontLayout == null) {
            return;
         }
      } else if (var2 == 3) {
         this.reverseDocument = var1;
         if (this.reverseLayout == null) {
            return;
         }
      } else {
         this.documents.add(var1);
         if (this.documentsLayout == null) {
            return;
         }
      }

      if (this.getParentActivity() != null) {
         PassportActivity.SecureDocumentCell var3 = new PassportActivity.SecureDocumentCell(this.getParentActivity());
         var3.setTag(var1);
         var3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.documentsCells.put(var1, var3);
         String var4 = this.getDocumentHash(var1);
         String var5;
         StringBuilder var6;
         if (var2 == 1) {
            var5 = LocaleController.getString("PassportSelfie", 2131560329);
            this.selfieLayout.addView(var3, LayoutHelper.createLinear(-1, -2));
            var6 = new StringBuilder();
            var6.append("selfie");
            var6.append(var4);
            var4 = var6.toString();
         } else if (var2 == 4) {
            var5 = LocaleController.getString("AttachPhoto", 2131558727);
            this.translationLayout.addView(var3, LayoutHelper.createLinear(-1, -2));
            var6 = new StringBuilder();
            var6.append("translation");
            var6.append(var4);
            var4 = var6.toString();
         } else if (var2 == 2) {
            TLRPC.SecureValueType var7 = this.currentDocumentsType.type;
            if (!(var7 instanceof TLRPC.TL_secureValueTypePassport) && !(var7 instanceof TLRPC.TL_secureValueTypeInternalPassport)) {
               var5 = LocaleController.getString("PassportFrontSide", 2131560224);
            } else {
               var5 = LocaleController.getString("PassportMainPage", 2131560282);
            }

            this.frontLayout.addView(var3, LayoutHelper.createLinear(-1, -2));
            var6 = new StringBuilder();
            var6.append("front");
            var6.append(var4);
            var4 = var6.toString();
         } else if (var2 == 3) {
            var5 = LocaleController.getString("PassportReverseSide", 2131560320);
            this.reverseLayout.addView(var3, LayoutHelper.createLinear(-1, -2));
            var6 = new StringBuilder();
            var6.append("reverse");
            var6.append(var4);
            var4 = var6.toString();
         } else {
            var5 = LocaleController.getString("AttachPhoto", 2131558727);
            this.documentsLayout.addView(var3, LayoutHelper.createLinear(-1, -2));
            var6 = new StringBuilder();
            var6.append("files");
            var6.append(var4);
            var4 = var6.toString();
         }

         String var9;
         label58: {
            if (var4 != null) {
               HashMap var8 = this.documentsErrors;
               if (var8 != null) {
                  var9 = (String)var8.get(var4);
                  if (var9 != null) {
                     var3.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
                     this.errorsValues.put(var4, "");
                     break label58;
                  }
               }
            }

            var9 = LocaleController.formatDateForBan((long)var1.secureFile.date);
         }

         var3.setTextAndValueAndImage(var5, var9, var1);
         var3.setOnClickListener(new _$$Lambda$PassportActivity$c_SivFsgu6h4BCXz5VeKlb47xZU(this, var2));
         var3.setOnLongClickListener(new _$$Lambda$PassportActivity$eMgihqVNJqkr7oPmUY_yq5VUzi4(this, var2, var1, var3, var4));
      }
   }

   private void addDocumentViewInternal(TLRPC.TL_secureFile var1, int var2) {
      this.addDocumentView(new SecureDocument(this.getSecureDocumentKey(var1.secret, var1.file_hash), var1, (String)null, (byte[])null, (byte[])null), var2);
   }

   private void addDocumentViews(ArrayList var1) {
      this.documents.clear();
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         TLRPC.SecureFile var4 = (TLRPC.SecureFile)var1.get(var3);
         if (var4 instanceof TLRPC.TL_secureFile) {
            this.addDocumentViewInternal((TLRPC.TL_secureFile)var4, 0);
         }
      }

   }

   private PassportActivity.TextDetailSecureCell addField(Context var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, boolean var4, boolean var5) {
      int var6;
      if (var3 != null) {
         var6 = var3.size();
      } else {
         var6 = 0;
      }

      PassportActivity.TextDetailSecureCell var7 = new PassportActivity.TextDetailSecureCell(var1);
      var7.setBackgroundDrawable(Theme.getSelectorDrawable(true));
      TLRPC.SecureValueType var19 = var2.type;
      String var20;
      if (var19 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
         if (var3 != null && !var3.isEmpty()) {
            if (var4 && var3.size() == 1) {
               var20 = this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(0)).type);
            } else if (var4 && var3.size() == 2) {
               var20 = LocaleController.formatString("PassportTwoDocuments", 2131560338, this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(0)).type), this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(1)).type));
            } else {
               var20 = LocaleController.getString("PassportIdentityDocument", 2131560227);
            }
         } else {
            var20 = LocaleController.getString("PassportPersonalDetails", 2131560301);
         }

         var7.setTextAndValue(var20, "", var5 ^ true);
      } else if (var19 instanceof TLRPC.TL_secureValueTypeAddress) {
         if (var3 != null && !var3.isEmpty()) {
            if (var4 && var3.size() == 1) {
               var20 = this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(0)).type);
            } else if (var4 && var3.size() == 2) {
               var20 = LocaleController.formatString("PassportTwoDocuments", 2131560338, this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(0)).type), this.getTextForType(((TLRPC.TL_secureRequiredType)var3.get(1)).type));
            } else {
               var20 = LocaleController.getString("PassportResidentialAddress", 2131560319);
            }
         } else {
            var20 = LocaleController.getString("PassportAddress", 2131560189);
         }

         var7.setTextAndValue(var20, "", var5 ^ true);
      } else if (var19 instanceof TLRPC.TL_secureValueTypePhone) {
         var7.setTextAndValue(LocaleController.getString("PassportPhone", 2131560304), "", var5 ^ true);
      } else if (var19 instanceof TLRPC.TL_secureValueTypeEmail) {
         var7.setTextAndValue(LocaleController.getString("PassportEmail", 2131560217), "", var5 ^ true);
      }

      if (this.currentActivityType == 8) {
         LinearLayout var21 = this.linearLayout2;
         var21.addView(var7, var21.getChildCount() - 5, LayoutHelper.createLinear(-1, -2));
      } else {
         this.linearLayout2.addView(var7, LayoutHelper.createLinear(-1, -2));
      }

      Object var22;
      String var23;
      String var25;
      label94: {
         label93: {
            var7.setOnClickListener(new _$$Lambda$PassportActivity$5Sry1zhVbDTBEYl5VpIUjvovUNY(this, var3, var2, var4));
            this.typesViews.put(var2, var7);
            this.typesValues.put(var2, new HashMap());
            TLRPC.TL_secureValue var8 = this.getValueByType(var2, false);
            var22 = null;
            if (var8 != null) {
               TLRPC.SecurePlainData var9 = var8.plain_data;
               if (var9 instanceof TLRPC.TL_securePlainEmail) {
                  var23 = ((TLRPC.TL_securePlainEmail)var9).email;
                  break label93;
               }

               if (var9 instanceof TLRPC.TL_securePlainPhone) {
                  var23 = ((TLRPC.TL_securePlainPhone)var9).phone;
                  break label93;
               }

               TLRPC.TL_secureData var24 = var8.data;
               if (var24 != null) {
                  var25 = this.decryptData(var24.data, this.decryptValueSecret(var24.secret, var24.data_hash), var8.data.data_hash);
                  var23 = null;
                  break label94;
               }
            }

            var23 = null;
            var25 = var23;
            break label94;
         }

         var25 = null;
      }

      TLRPC.TL_secureRequiredType var17;
      if (var3 != null && !var3.isEmpty()) {
         int var10 = var3.size();
         TLRPC.TL_secureRequiredType var11 = null;
         var22 = var11;
         int var12 = 0;

         Object var27;
         for(boolean var13 = false; var12 < var10; var22 = var27) {
            TLRPC.TL_secureRequiredType var14 = (TLRPC.TL_secureRequiredType)var3.get(var12);
            this.typesValues.put(var14, new HashMap());
            this.documentsToTypesLink.put(var14, var2);
            TLRPC.TL_secureRequiredType var15 = var11;
            boolean var16 = var13;
            var27 = var22;
            if (!var13) {
               TLRPC.TL_secureValue var18 = this.getValueByType(var14, false);
               var15 = var11;
               var16 = var13;
               var27 = var22;
               if (var18 != null) {
                  TLRPC.TL_secureData var26 = var18.data;
                  if (var26 != null) {
                     var22 = this.decryptData(var26.data, this.decryptValueSecret(var26.secret, var26.data_hash), var18.data.data_hash);
                  }

                  var15 = var14;
                  var16 = true;
                  var27 = var22;
               }
            }

            ++var12;
            var11 = var15;
            var13 = var16;
         }

         var17 = var11;
         if (var11 == null) {
            var17 = (TLRPC.TL_secureRequiredType)var3.get(0);
         }
      } else {
         var17 = null;
      }

      this.setTypeValue(var2, var23, var25, var17, (String)var22, var4, var6);
      return var7;
   }

   private void addTranslationDocumentViews(ArrayList var1) {
      this.translationDocuments.clear();
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         TLRPC.SecureFile var4 = (TLRPC.SecureFile)var1.get(var3);
         if (var4 instanceof TLRPC.TL_secureFile) {
            this.addDocumentViewInternal((TLRPC.TL_secureFile)var4, 4);
         }
      }

   }

   private void callCallback(boolean var1) {
      if (!this.callbackCalled) {
         int var4;
         Activity var5;
         if (!TextUtils.isEmpty(this.currentCallbackUrl)) {
            if (var1) {
               var5 = this.getParentActivity();
               StringBuilder var6 = new StringBuilder();
               var6.append(this.currentCallbackUrl);
               var6.append("&tg_passport=success");
               Browser.openUrl(var5, (Uri)Uri.parse(var6.toString()));
            } else if (!this.ignoreOnFailure) {
               var4 = this.currentActivityType;
               if (var4 == 5 || var4 == 0) {
                  Activity var3 = this.getParentActivity();
                  StringBuilder var2 = new StringBuilder();
                  var2.append(this.currentCallbackUrl);
                  var2.append("&tg_passport=cancel");
                  Browser.openUrl(var3, (Uri)Uri.parse(var2.toString()));
               }
            }

            this.callbackCalled = true;
         } else if (this.needActivityResult) {
            label48: {
               if (!var1) {
                  if (this.ignoreOnFailure) {
                     break label48;
                  }

                  var4 = this.currentActivityType;
                  if (var4 != 5 && var4 != 0) {
                     break label48;
                  }
               }

               var5 = this.getParentActivity();
               byte var7;
               if (var1) {
                  var7 = -1;
               } else {
                  var7 = 0;
               }

               var5.setResult(var7);
            }

            this.callbackCalled = true;
         }
      }

   }

   private boolean checkDiscard() {
      if (this.isHasNotAnyChanges()) {
         return false;
      } else {
         AlertDialog.Builder var1 = new AlertDialog.Builder(this.getParentActivity());
         var1.setPositiveButton(LocaleController.getString("PassportDiscard", 2131560212), new _$$Lambda$PassportActivity$Z4N0XgwepebvvcdAUdqH_Rebz4Y(this));
         var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var1.setTitle(LocaleController.getString("DiscardChanges", 2131559273));
         var1.setMessage(LocaleController.getString("PassportDiscardChanges", 2131560213));
         this.showDialog(var1.create());
         return true;
      }
   }

   private void checkFieldForError(EditTextBoldCursor var1, String var2, Editable var3, boolean var4) {
      label37: {
         HashMap var5 = this.errorsValues;
         if (var5 != null) {
            String var10 = (String)var5.get(var2);
            if (var10 != null) {
               if (TextUtils.equals(var10, var3)) {
                  HashMap var8 = this.fieldsErrors;
                  if (var8 != null) {
                     String var9 = (String)var8.get(var2);
                     if (var9 != null) {
                        var1.setErrorText(var9);
                        break label37;
                     }
                  }

                  var8 = this.documentsErrors;
                  if (var8 != null) {
                     var2 = (String)var8.get(var2);
                     if (var2 != null) {
                        var1.setErrorText(var2);
                     }
                  }
               } else {
                  var1.setErrorText((CharSequence)null);
               }
               break label37;
            }
         }

         var1.setErrorText((CharSequence)null);
      }

      String var6;
      if (var4) {
         var6 = "error_document_all";
      } else {
         var6 = "error_all";
      }

      HashMap var7 = this.errorsValues;
      if (var7 != null && var7.containsKey(var6)) {
         this.errorsValues.remove(var6);
         this.checkTopErrorCell(false);
      }

   }

   private boolean checkFieldsForError() {
      int var1;
      int var2;
      String var11;
      TLRPC.SecureValueType var12;
      if (this.currentDocumentsType != null) {
         label376: {
            if (!this.errorsValues.containsKey("error_all") && !this.errorsValues.containsKey("error_document_all")) {
               SecureDocument var3;
               StringBuilder var4;
               String var13;
               if (this.uploadDocumentCell != null) {
                  if (this.documents.isEmpty()) {
                     this.onFieldError(this.uploadDocumentCell);
                     return true;
                  }

                  var1 = this.documents.size();

                  for(var2 = 0; var2 < var1; ++var2) {
                     var3 = (SecureDocument)this.documents.get(var2);
                     var4 = new StringBuilder();
                     var4.append("files");
                     var4.append(this.getDocumentHash(var3));
                     var13 = var4.toString();
                     if (var13 != null && this.errorsValues.containsKey(var13)) {
                        this.onFieldError((View)this.documentsCells.get(var3));
                        return true;
                     }
                  }
               }

               if (!this.errorsValues.containsKey("files_all") && !this.errorsValues.containsKey("translation_all")) {
                  TextDetailSettingsCell var9 = this.uploadFrontCell;
                  StringBuilder var10;
                  if (var9 != null) {
                     if (this.frontDocument == null) {
                        this.onFieldError(var9);
                        return true;
                     }

                     var10 = new StringBuilder();
                     var10.append("front");
                     var10.append(this.getDocumentHash(this.frontDocument));
                     var11 = var10.toString();
                     if (this.errorsValues.containsKey(var11)) {
                        this.onFieldError((View)this.documentsCells.get(this.frontDocument));
                        return true;
                     }
                  }

                  var12 = this.currentDocumentsType.type;
                  if (var12 instanceof TLRPC.TL_secureValueTypeIdentityCard || var12 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                     var9 = this.uploadReverseCell;
                     if (var9 != null) {
                        if (this.reverseDocument == null) {
                           this.onFieldError(var9);
                           return true;
                        }

                        var10 = new StringBuilder();
                        var10.append("reverse");
                        var10.append(this.getDocumentHash(this.reverseDocument));
                        var11 = var10.toString();
                        if (this.errorsValues.containsKey(var11)) {
                           this.onFieldError((View)this.documentsCells.get(this.reverseDocument));
                           return true;
                        }
                     }
                  }

                  var9 = this.uploadSelfieCell;
                  if (var9 != null && this.currentBotId != 0) {
                     if (this.selfieDocument == null) {
                        this.onFieldError(var9);
                        return true;
                     }

                     var10 = new StringBuilder();
                     var10.append("selfie");
                     var10.append(this.getDocumentHash(this.selfieDocument));
                     var11 = var10.toString();
                     if (this.errorsValues.containsKey(var11)) {
                        this.onFieldError((View)this.documentsCells.get(this.selfieDocument));
                        return true;
                     }
                  }

                  if (this.uploadTranslationCell != null && this.currentBotId != 0) {
                     if (this.translationDocuments.isEmpty()) {
                        this.onFieldError(this.uploadTranslationCell);
                        return true;
                     }

                     var1 = this.translationDocuments.size();

                     for(var2 = 0; var2 < var1; ++var2) {
                        var3 = (SecureDocument)this.translationDocuments.get(var2);
                        var4 = new StringBuilder();
                        var4.append("translation");
                        var4.append(this.getDocumentHash(var3));
                        var13 = var4.toString();
                        if (var13 != null && this.errorsValues.containsKey(var13)) {
                           this.onFieldError((View)this.documentsCells.get(var3));
                           return true;
                        }
                     }
                  }
                  break label376;
               }

               this.onFieldError(this.bottomCell);
               return true;
            }

            this.onFieldError(this.topErrorCell);
            return true;
         }
      }

      for(var2 = 0; var2 < 2; ++var2) {
         EditTextBoldCursor[] var14;
         if (var2 == 0) {
            var14 = this.inputFields;
         } else {
            TextInfoPrivacyCell var15 = this.nativeInfoCell;
            if (var15 != null && var15.getVisibility() == 0) {
               var14 = this.inputExtraFields;
            } else {
               var14 = null;
            }
         }

         if (var14 != null) {
            for(var1 = 0; var1 < var14.length; ++var1) {
               boolean var5 = var14[var1].hasErrorText();
               boolean var6 = var5;
               if (!this.errorsValues.isEmpty()) {
                  label249: {
                     label248: {
                        var12 = this.currentType.type;
                        if (var12 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                           if (var2 == 0) {
                              switch(var1) {
                              case 0:
                                 var11 = "first_name";
                                 break label249;
                              case 1:
                                 var11 = "middle_name";
                                 break label249;
                              case 2:
                                 var11 = "last_name";
                                 break label249;
                              case 3:
                                 var11 = "birth_date";
                                 break label249;
                              case 4:
                                 var11 = "gender";
                                 break label249;
                              case 5:
                                 break label248;
                              case 6:
                                 var11 = "residence_country_code";
                                 break label249;
                              case 7:
                                 var11 = "document_no";
                                 break label249;
                              case 8:
                                 var11 = "expiry_date";
                                 break label249;
                              }
                           } else {
                              if (var1 == 0) {
                                 var11 = "first_name_native";
                                 break label249;
                              }

                              if (var1 == 1) {
                                 var11 = "middle_name_native";
                                 break label249;
                              }

                              if (var1 == 2) {
                                 var11 = "last_name_native";
                                 break label249;
                              }
                           }
                        } else if (var12 instanceof TLRPC.TL_secureValueTypeAddress) {
                           if (var1 == 0) {
                              var11 = "street_line1";
                              break label249;
                           }

                           if (var1 == 1) {
                              var11 = "street_line2";
                              break label249;
                           }

                           if (var1 == 2) {
                              var11 = "post_code";
                              break label249;
                           }

                           if (var1 == 3) {
                              var11 = "city";
                              break label249;
                           }

                           if (var1 == 4) {
                              var11 = "state";
                              break label249;
                           }

                           if (var1 == 5) {
                              break label248;
                           }
                        }

                        var11 = null;
                        break label249;
                     }

                     var11 = "country_code";
                  }

                  var6 = var5;
                  if (var11 != null) {
                     var11 = (String)this.errorsValues.get(var11);
                     var6 = var5;
                     if (!TextUtils.isEmpty(var11)) {
                        var6 = var5;
                        if (var11.equals(var14[var1].getText().toString())) {
                           var6 = true;
                        }
                     }
                  }
               }

               if (!this.documentOnly || this.currentDocumentsType == null || var1 >= 7) {
                  var5 = var6;
                  if (!var6) {
                     int var7;
                     boolean var16;
                     label315: {
                        label314: {
                           label384: {
                              var7 = var14[var1].length();
                              int var8 = this.currentActivityType;
                              if (var8 == 1) {
                                 if (var1 == 8) {
                                    continue;
                                 }

                                 if (var2 == 0 && (var1 == 0 || var1 == 2 || var1 == 1) || var2 == 1 && (var1 == 0 || var1 == 1 || var1 == 2)) {
                                    if (var7 > 255) {
                                       var6 = true;
                                    }

                                    if (var2 == 0 && var1 == 1) {
                                       break label314;
                                    }

                                    var5 = var6;
                                    if (var2 == 1) {
                                       var5 = var6;
                                       if (var1 == 1) {
                                          break label314;
                                       }
                                    }
                                    break label384;
                                 }

                                 var5 = var6;
                                 if (var1 != 7) {
                                    break label384;
                                 }

                                 var5 = var6;
                                 if (var7 <= 24) {
                                    break label384;
                                 }
                              } else {
                                 var5 = var6;
                                 if (var8 != 2) {
                                    break label384;
                                 }

                                 if (var1 == 1) {
                                    continue;
                                 }

                                 if (var1 == 3) {
                                    var5 = var6;
                                    if (var7 >= 2) {
                                       break label384;
                                    }
                                 } else if (var1 == 4) {
                                    if (!"US".equals(this.currentCitizeship)) {
                                       continue;
                                    }

                                    var5 = var6;
                                    if (var7 >= 2) {
                                       break label384;
                                    }
                                 } else {
                                    var5 = var6;
                                    if (var1 != 2) {
                                       break label384;
                                    }

                                    if (var7 >= 2) {
                                       var5 = var6;
                                       if (var7 <= 10) {
                                          break label384;
                                       }
                                    }
                                 }
                              }

                              var5 = true;
                           }

                           var16 = false;
                           var6 = var5;
                           break label315;
                        }

                        var16 = true;
                     }

                     var5 = var6;
                     if (!var6) {
                        var5 = var6;
                        if (!var16) {
                           var5 = var6;
                           if (var7 == 0) {
                              var5 = true;
                           }
                        }
                     }
                  }

                  if (var5) {
                     this.onFieldError(var14[var1]);
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private void checkNativeFields(boolean var1) {
      if (this.inputExtraFields != null) {
         String var2 = (String)this.languageMap.get(this.currentResidence);
         String var3 = (String)SharedConfig.getCountryLangs().get(this.currentResidence);
         boolean var4 = this.currentType.native_names;
         int var5 = 0;
         if (var4 && !TextUtils.isEmpty(this.currentResidence) && !"EN".equals(var3)) {
            if (this.nativeInfoCell.getVisibility() != 0) {
               this.nativeInfoCell.setVisibility(0);
               this.headerCell.setVisibility(0);
               this.extraBackgroundView2.setVisibility(0);
               var5 = 0;

               while(true) {
                  EditTextBoldCursor[] var6 = this.inputExtraFields;
                  if (var5 >= var6.length) {
                     if (var6[0].length() == 0 && this.inputExtraFields[1].length() == 0 && this.inputExtraFields[2].length() == 0) {
                        var5 = 0;

                        while(true) {
                           boolean[] var8 = this.nonLatinNames;
                           if (var5 >= var8.length) {
                              break;
                           }

                           if (var8[var5]) {
                              this.inputExtraFields[0].setText(this.inputFields[0].getText());
                              this.inputExtraFields[1].setText(this.inputFields[1].getText());
                              this.inputExtraFields[2].setText(this.inputFields[2].getText());
                              break;
                           }

                           ++var5;
                        }
                     }

                     this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(this.getParentActivity(), 2131165394, "windowBackgroundGrayShadow"));
                     break;
                  }

                  ((View)var6[var5].getParent()).setVisibility(0);
                  ++var5;
               }
            }

            this.nativeInfoCell.setText(LocaleController.formatString("PassportNativeInfo", 2131560294, var2));
            if (var3 != null) {
               StringBuilder var9 = new StringBuilder();
               var9.append("PassportLanguage_");
               var9.append(var3);
               var3 = LocaleController.getServerString(var9.toString());
            } else {
               var3 = null;
            }

            if (var3 != null) {
               this.headerCell.setText(LocaleController.formatString("PassportNativeHeaderLang", 2131560293, var3));
            } else {
               this.headerCell.setText(LocaleController.getString("PassportNativeHeader", 2131560292));
            }

            for(var5 = 0; var5 < 3; ++var5) {
               if (var5 != 0) {
                  if (var5 != 1) {
                     if (var5 == 2) {
                        if (var3 != null) {
                           this.inputExtraFields[var5].setHintText(LocaleController.getString("PassportSurname", 2131560334));
                        } else {
                           this.inputExtraFields[var5].setHintText(LocaleController.formatString("PassportSurnameCountry", 2131560335, var2));
                        }
                     }
                  } else if (var3 != null) {
                     this.inputExtraFields[var5].setHintText(LocaleController.getString("PassportMidname", 2131560285));
                  } else {
                     this.inputExtraFields[var5].setHintText(LocaleController.formatString("PassportMidnameCountry", 2131560286, var2));
                  }
               } else if (var3 != null) {
                  this.inputExtraFields[var5].setHintText(LocaleController.getString("PassportName", 2131560288));
               } else {
                  this.inputExtraFields[var5].setHintText(LocaleController.formatString("PassportNameCountry", 2131560290, var2));
               }
            }

            if (var1) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$HspRKy4cKNBUybT_ED_XvHWEg3g(this));
            }
         } else if (this.nativeInfoCell.getVisibility() != 8) {
            this.nativeInfoCell.setVisibility(8);
            this.headerCell.setVisibility(8);
            this.extraBackgroundView2.setVisibility(8);

            while(true) {
               EditTextBoldCursor[] var7 = this.inputExtraFields;
               if (var5 >= var7.length) {
                  if ((this.currentBotId == 0 && this.currentDocumentsType != null || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
                     this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(this.getParentActivity(), 2131165395, "windowBackgroundGrayShadow"));
                  } else {
                     this.sectionCell2.setBackgroundDrawable(Theme.getThemedDrawable(this.getParentActivity(), 2131165394, "windowBackgroundGrayShadow"));
                  }
                  break;
               }

               ((View)var7[var5].getParent()).setVisibility(8);
               ++var5;
            }
         }

      }
   }

   public static boolean checkSecret(byte[] var0, Long var1) {
      if (var0 != null && var0.length == 32) {
         int var2 = 0;

         int var3;
         for(var3 = 0; var2 < var0.length; ++var2) {
            var3 += var0[var2] & 255;
         }

         if (var3 % 255 != 239) {
            return false;
         } else {
            return var1 == null || Utilities.bytesToLong(Utilities.computeSHA256(var0)) == var1;
         }
      } else {
         return false;
      }
   }

   private void checkTopErrorCell(boolean var1) {
      if (this.topErrorCell != null) {
         SpannableStringBuilder var2 = null;
         SpannableStringBuilder var3 = var2;
         String var4;
         if (this.fieldsErrors != null) {
            label45: {
               if (!var1) {
                  var3 = var2;
                  if (!this.errorsValues.containsKey("error_all")) {
                     break label45;
                  }
               }

               var4 = (String)this.fieldsErrors.get("error_all");
               var3 = var2;
               if (var4 != null) {
                  var2 = new SpannableStringBuilder(var4);
                  var3 = var2;
                  if (var1) {
                     this.errorsValues.put("error_all", "");
                     var3 = var2;
                  }
               }
            }
         }

         var2 = var3;
         if (this.documentsErrors != null) {
            label39: {
               if (!var1) {
                  var2 = var3;
                  if (!this.errorsValues.containsKey("error_document_all")) {
                     break label39;
                  }
               }

               var4 = (String)this.documentsErrors.get("error_all");
               var2 = var3;
               if (var4 != null) {
                  if (var3 == null) {
                     var3 = new SpannableStringBuilder(var4);
                  } else {
                     var3.append("\n\n").append(var4);
                  }

                  var2 = var3;
                  if (var1) {
                     this.errorsValues.put("error_document_all", "");
                     var2 = var3;
                  }
               }
            }
         }

         if (var2 != null) {
            var2.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, var2.length(), 33);
            this.topErrorCell.setText(var2);
            this.topErrorCell.setVisibility(0);
         } else if (this.topErrorCell.getVisibility() != 8) {
            this.topErrorCell.setVisibility(8);
         }

      }
   }

   private void createAddressInterface(Context var1) {
      this.languageMap = new HashMap();

      label209: {
         Exception var10000;
         label208: {
            BufferedReader var2;
            boolean var10001;
            try {
               InputStreamReader var3 = new InputStreamReader(var1.getResources().getAssets().open("countries.txt"));
               var2 = new BufferedReader(var3);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label208;
            }

            while(true) {
               String var14;
               try {
                  var14 = var2.readLine();
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (var14 == null) {
                  try {
                     var2.close();
                     break label209;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  String[] var15 = var14.split(";");
                  this.languageMap.put(var15[1], var15[2]);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var11 = var10000;
         FileLog.e((Throwable)var11);
      }

      this.topErrorCell = new TextInfoPrivacyCell(var1);
      this.topErrorCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165396, "windowBackgroundGrayShadow"));
      this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0F), 0, 0);
      this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
      this.checkTopErrorCell(true);
      TLRPC.TL_secureRequiredType var12 = this.currentDocumentsType;
      if (var12 != null) {
         TLRPC.SecureValueType var13 = var12.type;
         if (var13 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
            super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
         } else if (var13 instanceof TLRPC.TL_secureValueTypeBankStatement) {
            super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
         } else if (var13 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
            super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
         } else if (var13 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
            super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
         } else if (var13 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
            super.actionBar.setTitle(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
         }

         this.headerCell = new HeaderCell(var1);
         this.headerCell.setText(LocaleController.getString("PassportDocuments", 2131560216));
         this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
         this.documentsLayout = new LinearLayout(var1);
         this.documentsLayout.setOrientation(1);
         this.linearLayout2.addView(this.documentsLayout, LayoutHelper.createLinear(-1, -2));
         this.uploadDocumentCell = new TextSettingsCell(var1);
         this.uploadDocumentCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.linearLayout2.addView(this.uploadDocumentCell, LayoutHelper.createLinear(-1, -2));
         this.uploadDocumentCell.setOnClickListener(new _$$Lambda$PassportActivity$iWoQRq4LDUs7_2TnB7mjc6JUANI(this));
         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         if (this.currentBotId != 0) {
            this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAddressUploadInfo", 2131560164);
         } else {
            var13 = this.currentDocumentsType.type;
            if (var13 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
               this.noAllDocumentsErrorText = LocaleController.getString("PassportAddAgreementInfo", 2131560166);
            } else if (var13 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
               this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBillInfo", 2131560170);
            } else if (var13 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
               this.noAllDocumentsErrorText = LocaleController.getString("PassportAddPassportRegistrationInfo", 2131560180);
            } else if (var13 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
               this.noAllDocumentsErrorText = LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131560182);
            } else if (var13 instanceof TLRPC.TL_secureValueTypeBankStatement) {
               this.noAllDocumentsErrorText = LocaleController.getString("PassportAddBankInfo", 2131560168);
            } else {
               this.noAllDocumentsErrorText = "";
            }
         }

         CharSequence var17 = this.noAllDocumentsErrorText;
         HashMap var4 = this.documentsErrors;
         Object var16 = var17;
         String var18;
         if (var4 != null) {
            var18 = (String)var4.get("files_all");
            var16 = var17;
            if (var18 != null) {
               var16 = new SpannableStringBuilder(var18);
               ((SpannableStringBuilder)var16).append("\n\n");
               ((SpannableStringBuilder)var16).append(this.noAllDocumentsErrorText);
               ((SpannableStringBuilder)var16).setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, var18.length(), 33);
               this.errorsValues.put("files_all", "");
            }
         }

         this.bottomCell.setText((CharSequence)var16);
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
         if (this.currentDocumentsType.translation_required) {
            this.headerCell = new HeaderCell(var1);
            this.headerCell.setText(LocaleController.getString("PassportTranslation", 2131560337));
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            this.translationLayout = new LinearLayout(var1);
            this.translationLayout.setOrientation(1);
            this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
            this.uploadTranslationCell = new TextSettingsCell(var1);
            this.uploadTranslationCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
            this.uploadTranslationCell.setOnClickListener(new _$$Lambda$PassportActivity$zxJkztkjm9AzPkgXb6NmXZ5sri0(this));
            this.bottomCellTranslation = new TextInfoPrivacyCell(var1);
            this.bottomCellTranslation.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
            if (this.currentBotId != 0) {
               this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131560188);
            } else {
               var13 = this.currentDocumentsType.type;
               if (var13 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationAgreementInfo", 2131560183);
               } else if (var13 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBillInfo", 2131560185);
               } else if (var13 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationPassportRegistrationInfo", 2131560186);
               } else if (var13 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationTemporaryRegistrationInfo", 2131560187);
               } else if (var13 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationBankInfo", 2131560184);
               } else {
                  this.noAllTranslationErrorText = "";
               }
            }

            var17 = this.noAllTranslationErrorText;
            var4 = this.documentsErrors;
            var16 = var17;
            if (var4 != null) {
               var18 = (String)var4.get("translation_all");
               var16 = var17;
               if (var18 != null) {
                  var16 = new SpannableStringBuilder(var18);
                  ((SpannableStringBuilder)var16).append("\n\n");
                  ((SpannableStringBuilder)var16).append(this.noAllTranslationErrorText);
                  ((SpannableStringBuilder)var16).setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, var18.length(), 33);
                  this.errorsValues.put("translation_all", "");
               }
            }

            this.bottomCellTranslation.setText((CharSequence)var16);
            this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
         }
      } else {
         super.actionBar.setTitle(LocaleController.getString("PassportAddress", 2131560189));
      }

      this.headerCell = new HeaderCell(var1);
      this.headerCell.setText(LocaleController.getString("PassportAddressHeader", 2131560190));
      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      this.inputFields = new EditTextBoldCursor[6];

      for(int var5 = 0; var5 < 6; ++var5) {
         final EditTextBoldCursor var20 = new EditTextBoldCursor(var1);
         this.inputFields[var5] = var20;
         FrameLayout var19 = new FrameLayout(var1) {
            private StaticLayout errorLayout;
            float offsetX;

            protected void onDraw(Canvas var1) {
               if (this.errorLayout != null) {
                  var1.save();
                  var1.translate((float)AndroidUtilities.dp(21.0F) + this.offsetX, var20.getLineY() + (float)AndroidUtilities.dp(3.0F));
                  this.errorLayout.draw(var1);
                  var1.restore();
               }

            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(34.0F);
               this.errorLayout = var20.getErrorLayout(var3);
               StaticLayout var4 = this.errorLayout;
               int var5 = var2;
               if (var4 != null) {
                  int var6 = var4.getLineCount();
                  int var7 = 0;
                  if (var6 > 1) {
                     var2 = MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + (this.errorLayout.getLineBottom(var6 - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                  }

                  var5 = var2;
                  if (LocaleController.isRTL) {
                     float var8 = 0.0F;

                     while(true) {
                        var5 = var2;
                        if (var7 >= var6) {
                           break;
                        }

                        if (this.errorLayout.getLineLeft(var7) != 0.0F) {
                           this.offsetX = 0.0F;
                           var5 = var2;
                           break;
                        }

                        var8 = Math.max(var8, this.errorLayout.getLineWidth(var7));
                        if (var7 == var6 - 1) {
                           this.offsetX = (float)var3 - var8;
                        }

                        ++var7;
                     }
                  }
               }

               super.onMeasure(var1, var5);
            }
         };
         var19.setWillNotDraw(false);
         this.linearLayout2.addView(var19, LayoutHelper.createLinear(-1, -2));
         var19.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         byte var6 = 5;
         if (var5 == 5) {
            this.extraBackgroundView = new View(var1);
            this.extraBackgroundView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(-1, 6));
         }

         if (this.documentOnly && this.currentDocumentsType != null) {
            var19.setVisibility(8);
            View var21 = this.extraBackgroundView;
            if (var21 != null) {
               var21.setVisibility(8);
            }
         }

         this.inputFields[var5].setTag(var5);
         this.inputFields[var5].setSupportRtlHint(true);
         this.inputFields[var5].setTextSize(1, 16.0F);
         this.inputFields[var5].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var5].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         this.inputFields[var5].setTransformHintToHeader(true);
         this.inputFields[var5].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var5].setBackgroundDrawable((Drawable)null);
         this.inputFields[var5].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var5].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var5].setCursorWidth(1.5F);
         this.inputFields[var5].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
         if (var5 == 5) {
            this.inputFields[var5].setOnTouchListener(new _$$Lambda$PassportActivity$VjopYxs0FTEpLRu0AyOT8rb_e68(this));
            this.inputFields[var5].setInputType(0);
            this.inputFields[var5].setFocusable(false);
         } else {
            this.inputFields[var5].setInputType(16385);
            this.inputFields[var5].setImeOptions(268435461);
         }

         final String var22;
         if (var5 != 0) {
            if (var5 != 1) {
               if (var5 != 2) {
                  if (var5 != 3) {
                     if (var5 != 4) {
                        if (var5 != 5) {
                           continue;
                        }

                        this.inputFields[var5].setHintText(LocaleController.getString("PassportCountry", 2131560198));
                        var22 = "country_code";
                     } else {
                        this.inputFields[var5].setHintText(LocaleController.getString("PassportState", 2131560331));
                        var22 = "state";
                     }
                  } else {
                     this.inputFields[var5].setHintText(LocaleController.getString("PassportCity", 2131560196));
                     var22 = "city";
                  }
               } else {
                  this.inputFields[var5].setHintText(LocaleController.getString("PassportPostcode", 2131560312));
                  var22 = "post_code";
               }
            } else {
               this.inputFields[var5].setHintText(LocaleController.getString("PassportStreet2", 2131560333));
               var22 = "street_line2";
            }
         } else {
            this.inputFields[var5].setHintText(LocaleController.getString("PassportStreet1", 2131560332));
            var22 = "street_line1";
         }

         this.setFieldValues(this.currentValues, this.inputFields[var5], var22);
         if (var5 == 2) {
            this.inputFields[var5].addTextChangedListener(new TextWatcher() {
               private boolean ignore;

               public void afterTextChanged(Editable var1) {
                  if (!this.ignore) {
                     boolean var2 = true;
                     this.ignore = true;
                     int var3 = 0;

                     boolean var5;
                     while(true) {
                        if (var3 >= var1.length()) {
                           var5 = false;
                           break;
                        }

                        char var4 = var1.charAt(var3);
                        if ((var4 < 'a' || var4 > 'z') && (var4 < 'A' || var4 > 'Z') && (var4 < '0' || var4 > '9') && var4 != '-' && var4 != ' ') {
                           var5 = var2;
                           break;
                        }

                        ++var3;
                     }

                     this.ignore = false;
                     if (var5) {
                        var20.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131560343));
                     } else {
                        PassportActivity.this.checkFieldForError(var20, var22, var1, false);
                     }

                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
            LengthFilter var23 = new LengthFilter(10);
            this.inputFields[var5].setFilters(new InputFilter[]{var23});
         } else {
            this.inputFields[var5].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  PassportActivity.this.checkFieldForError(var20, var22, var1, false);
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         }

         EditTextBoldCursor[] var24 = this.inputFields;
         var24[var5].setSelection(var24[var5].length());
         this.inputFields[var5].setPadding(0, 0, 0, 0);
         EditTextBoldCursor var25 = this.inputFields[var5];
         if (!LocaleController.isRTL) {
            var6 = 3;
         }

         var25.setGravity(var6 | 16);
         var19.addView(this.inputFields[var5], LayoutHelper.createFrame(-1, 64.0F, 51, 21.0F, 0.0F, 21.0F, 0.0F));
         this.inputFields[var5].setOnEditorActionListener(new _$$Lambda$PassportActivity$AoVUW01QTsr_TGtxsPo_viSuIG8(this));
      }

      this.sectionCell = new ShadowSectionCell(var1);
      this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
      if (this.documentOnly && this.currentDocumentsType != null) {
         this.headerCell.setVisibility(8);
         this.sectionCell.setVisibility(8);
      }

      if ((this.currentBotId == 0 && this.currentDocumentsType != null || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
         this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         if (this.documentOnly && this.currentDocumentsType != null) {
            this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         }
      } else {
         TLRPC.TL_secureValue var26 = this.currentDocumentsTypeValue;
         if (var26 != null) {
            this.addDocumentViews(var26.files);
            this.addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
         }

         this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         TextSettingsCell var27 = new TextSettingsCell(var1);
         var27.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
         var27.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         if (this.currentDocumentsType == null) {
            var27.setText(LocaleController.getString("PassportDeleteInfo", 2131560205), false);
         } else {
            var27.setText(LocaleController.getString("PassportDeleteDocument", 2131560200), false);
         }

         this.linearLayout2.addView(var27, LayoutHelper.createLinear(-1, -2));
         var27.setOnClickListener(new _$$Lambda$PassportActivity$OrTOOfgxc6UhCIwbWsAhFPEaa80(this));
         this.sectionCell = new ShadowSectionCell(var1);
         this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
      }

      this.updateUploadText(0);
      this.updateUploadText(4);
   }

   private void createChatAttachView() {
      if (this.getParentActivity() != null) {
         if (this.chatAttachAlert == null) {
            this.chatAttachAlert = new ChatAttachAlert(this.getParentActivity(), this);
            this.chatAttachAlert.setDelegate(new ChatAttachAlert.ChatAttachViewDelegate() {
               public boolean allowGroupPhotos() {
                  return false;
               }

               public void didPressedButton(int var1) {
                  if (PassportActivity.this.getParentActivity() != null && PassportActivity.this.chatAttachAlert != null) {
                     if (var1 != 8 && var1 != 7) {
                        if (PassportActivity.this.chatAttachAlert != null) {
                           PassportActivity.this.chatAttachAlert.dismissWithButtonClick(var1);
                        }

                        PassportActivity.this.processSelectedAttach(var1);
                        return;
                     }

                     if (var1 != 8) {
                        PassportActivity.this.chatAttachAlert.dismiss();
                     }

                     HashMap var2 = PassportActivity.this.chatAttachAlert.getSelectedPhotos();
                     ArrayList var3 = PassportActivity.this.chatAttachAlert.getSelectedPhotosOrder();
                     if (!var2.isEmpty()) {
                        ArrayList var4 = new ArrayList();

                        for(var1 = 0; var1 < var3.size(); ++var1) {
                           MediaController.PhotoEntry var5 = (MediaController.PhotoEntry)var2.get(var3.get(var1));
                           SendMessagesHelper.SendingMediaInfo var6 = new SendMessagesHelper.SendingMediaInfo();
                           String var7 = var5.imagePath;
                           if (var7 != null) {
                              var6.path = var7;
                           } else {
                              var7 = var5.path;
                              if (var7 != null) {
                                 var6.path = var7;
                              }
                           }

                           var4.add(var6);
                           var5.reset();
                        }

                        PassportActivity.this.processSelectedFiles(var4);
                     }
                  }

               }

               public void didSelectBot(TLRPC.User var1) {
               }

               public View getRevealView() {
                  return null;
               }

               public void onCameraOpened() {
                  AndroidUtilities.hideKeyboard(PassportActivity.access$9300(PassportActivity.this).findFocus());
               }
            });
         }

      }
   }

   private void createDocumentDeleteAlert() {
      boolean[] var1 = new boolean[]{true};
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PassportActivity$izrGH6tzz_c5ZefftonTyrpURyU(this, var1));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var2.setTitle(LocaleController.getString("AppName", 2131558635));
      if (this.documentOnly && this.currentDocumentsType == null && this.currentType.type instanceof TLRPC.TL_secureValueTypeAddress) {
         var2.setMessage(LocaleController.getString("PassportDeleteAddressAlert", 2131560199));
      } else if (this.documentOnly && this.currentDocumentsType == null && this.currentType.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
         var2.setMessage(LocaleController.getString("PassportDeletePersonalAlert", 2131560206));
      } else {
         var2.setMessage(LocaleController.getString("PassportDeleteDocumentAlert", 2131560202));
      }

      if (!this.documentOnly && this.currentDocumentsType != null) {
         FrameLayout var3 = new FrameLayout(this.getParentActivity());
         CheckBoxCell var4 = new CheckBoxCell(this.getParentActivity(), 1);
         var4.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         TLRPC.SecureValueType var5 = this.currentType.type;
         if (var5 instanceof TLRPC.TL_secureValueTypeAddress) {
            var4.setText(LocaleController.getString("PassportDeleteDocumentAddress", 2131560201), "", true, false);
         } else if (var5 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            var4.setText(LocaleController.getString("PassportDeleteDocumentPersonal", 2131560203), "", true, false);
         }

         int var6;
         if (LocaleController.isRTL) {
            var6 = AndroidUtilities.dp(16.0F);
         } else {
            var6 = AndroidUtilities.dp(8.0F);
         }

         int var7;
         if (LocaleController.isRTL) {
            var7 = AndroidUtilities.dp(8.0F);
         } else {
            var7 = AndroidUtilities.dp(16.0F);
         }

         var4.setPadding(var6, 0, var7, 0);
         var3.addView(var4, LayoutHelper.createFrame(-1, 48, 51));
         var4.setOnClickListener(new _$$Lambda$PassportActivity$nDbjMA2U409g1anoYGcsOusiteI(var1));
         var2.setView(var3);
      }

      this.showDialog(var2.create());
   }

   private void createEmailInterface(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131560217));
      if (!TextUtils.isEmpty(this.currentEmail)) {
         TextSettingsCell var2 = new TextSettingsCell(var1);
         var2.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         var2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         var2.setText(LocaleController.formatString("PassportPhoneUseSame", 2131560308, this.currentEmail), false);
         this.linearLayout2.addView(var2, LayoutHelper.createLinear(-1, -2));
         var2.setOnClickListener(new _$$Lambda$PassportActivity$B8yMOg0egHKBmo8qqI_zP3hWZKI(this));
         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameEmailInfo", 2131560309));
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      }

      this.inputFields = new EditTextBoldCursor[1];

      for(int var3 = 0; var3 < 1; ++var3) {
         FrameLayout var6 = new FrameLayout(var1);
         this.linearLayout2.addView(var6, LayoutHelper.createLinear(-1, 50));
         var6.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.inputFields[var3] = new EditTextBoldCursor(var1);
         this.inputFields[var3].setTag(var3);
         this.inputFields[var3].setTextSize(1, 16.0F);
         this.inputFields[var3].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var3].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var3].setBackgroundDrawable((Drawable)null);
         this.inputFields[var3].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var3].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var3].setCursorWidth(1.5F);
         this.inputFields[var3].setInputType(33);
         this.inputFields[var3].setImeOptions(268435462);
         if (var3 == 0) {
            this.inputFields[var3].setHint(LocaleController.getString("PaymentShippingEmailPlaceholder", 2131560394));
            TLRPC.TL_secureValue var4 = this.currentTypeValue;
            if (var4 != null) {
               TLRPC.SecurePlainData var7 = var4.plain_data;
               if (var7 instanceof TLRPC.TL_securePlainEmail) {
                  TLRPC.TL_securePlainEmail var8 = (TLRPC.TL_securePlainEmail)var7;
                  if (!TextUtils.isEmpty(var8.email)) {
                     this.inputFields[var3].setText(var8.email);
                  }
               }
            }
         }

         EditTextBoldCursor[] var9 = this.inputFields;
         var9[var3].setSelection(var9[var3].length());
         this.inputFields[var3].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
         EditTextBoldCursor var10 = this.inputFields[var3];
         byte var5;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var10.setGravity(var5);
         var6.addView(this.inputFields[var3], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
         this.inputFields[var3].setOnEditorActionListener(new _$$Lambda$PassportActivity$lpvHitH1qNbCguTGgQkc2rG8PIQ(this));
      }

      this.bottomCell = new TextInfoPrivacyCell(var1);
      this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.bottomCell.setText(LocaleController.getString("PassportEmailUploadInfo", 2131560220));
      this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
   }

   private void createEmailVerificationInterface(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("PassportEmail", 2131560217));
      this.inputFields = new EditTextBoldCursor[1];

      for(int var2 = 0; var2 < 1; ++var2) {
         FrameLayout var3 = new FrameLayout(var1);
         this.linearLayout2.addView(var3, LayoutHelper.createLinear(-1, 50));
         var3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.inputFields[var2] = new EditTextBoldCursor(var1);
         this.inputFields[var2].setTag(var2);
         this.inputFields[var2].setTextSize(1, 16.0F);
         this.inputFields[var2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var2].setBackgroundDrawable((Drawable)null);
         this.inputFields[var2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var2].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var2].setCursorWidth(1.5F);
         EditTextBoldCursor var4 = this.inputFields[var2];
         byte var5 = 3;
         var4.setInputType(3);
         this.inputFields[var2].setImeOptions(268435462);
         if (var2 == 0) {
            this.inputFields[var2].setHint(LocaleController.getString("PassportEmailCode", 2131560218));
         }

         EditTextBoldCursor[] var6 = this.inputFields;
         var6[var2].setSelection(var6[var2].length());
         this.inputFields[var2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
         var4 = this.inputFields[var2];
         if (LocaleController.isRTL) {
            var5 = 5;
         }

         var4.setGravity(var5);
         var3.addView(this.inputFields[var2], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
         this.inputFields[var2].setOnEditorActionListener(new _$$Lambda$PassportActivity$z1QQrtghbex8oLf43ona_95yF_0(this));
         this.inputFields[var2].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               if (!PassportActivity.this.ignoreOnTextChange) {
                  if (PassportActivity.this.emailCodeLength != 0 && PassportActivity.this.inputFields[0].length() == PassportActivity.this.emailCodeLength) {
                     PassportActivity.this.doneItem.callOnClick();
                  }

               }
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
      }

      this.bottomCell = new TextInfoPrivacyCell(var1);
      this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.bottomCell.setText(LocaleController.formatString("PassportEmailVerifyInfo", 2131560221, this.currentValues.get("email")));
      this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
   }

   private void createIdentityInterface(Context var1) {
      this.languageMap = new HashMap();

      final String var18;
      label263: {
         Exception var10000;
         label262: {
            BufferedReader var2;
            boolean var10001;
            try {
               InputStreamReader var3 = new InputStreamReader(var1.getResources().getAssets().open("countries.txt"));
               var2 = new BufferedReader(var3);
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label262;
            }

            while(true) {
               try {
                  var18 = var2.readLine();
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break;
               }

               if (var18 == null) {
                  try {
                     var2.close();
                     break label263;
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  String[] var19 = var18.split(";");
                  this.languageMap.put(var19[1], var19[2]);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var15 = var10000;
         FileLog.e((Throwable)var15);
      }

      this.topErrorCell = new TextInfoPrivacyCell(var1);
      this.topErrorCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165396, "windowBackgroundGrayShadow"));
      this.topErrorCell.setPadding(0, AndroidUtilities.dp(7.0F), 0, 0);
      this.linearLayout2.addView(this.topErrorCell, LayoutHelper.createLinear(-1, -2));
      this.checkTopErrorCell(true);
      if (this.currentDocumentsType != null) {
         this.headerCell = new HeaderCell(var1);
         if (this.documentOnly) {
            this.headerCell.setText(LocaleController.getString("PassportDocuments", 2131560216));
         } else {
            this.headerCell.setText(LocaleController.getString("PassportRequiredDocuments", 2131560317));
         }

         this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
         this.frontLayout = new LinearLayout(var1);
         this.frontLayout.setOrientation(1);
         this.linearLayout2.addView(this.frontLayout, LayoutHelper.createLinear(-1, -2));
         this.uploadFrontCell = new TextDetailSettingsCell(var1);
         this.uploadFrontCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.linearLayout2.addView(this.uploadFrontCell, LayoutHelper.createLinear(-1, -2));
         this.uploadFrontCell.setOnClickListener(new _$$Lambda$PassportActivity$c9kJTe5mHBur0ZKYsiTqGUHyE0o(this));
         this.reverseLayout = new LinearLayout(var1);
         this.reverseLayout.setOrientation(1);
         this.linearLayout2.addView(this.reverseLayout, LayoutHelper.createLinear(-1, -2));
         boolean var4 = this.currentDocumentsType.selfie_required;
         this.uploadReverseCell = new TextDetailSettingsCell(var1);
         this.uploadReverseCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.uploadReverseCell.setTextAndValue(LocaleController.getString("PassportReverseSide", 2131560320), LocaleController.getString("PassportReverseSideInfo", 2131560321), var4);
         this.linearLayout2.addView(this.uploadReverseCell, LayoutHelper.createLinear(-1, -2));
         this.uploadReverseCell.setOnClickListener(new _$$Lambda$PassportActivity$jVyNHBa_n1RAjexJFiTwuhwl3wk(this));
         if (this.currentDocumentsType.selfie_required) {
            this.selfieLayout = new LinearLayout(var1);
            this.selfieLayout.setOrientation(1);
            this.linearLayout2.addView(this.selfieLayout, LayoutHelper.createLinear(-1, -2));
            this.uploadSelfieCell = new TextDetailSettingsCell(var1);
            this.uploadSelfieCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.uploadSelfieCell.setTextAndValue(LocaleController.getString("PassportSelfie", 2131560329), LocaleController.getString("PassportSelfieInfo", 2131560330), this.currentType.translation_required);
            this.linearLayout2.addView(this.uploadSelfieCell, LayoutHelper.createLinear(-1, -2));
            this.uploadSelfieCell.setOnClickListener(new _$$Lambda$PassportActivity$80pcXC9JPBzJ3vgi80hMNDT2KC4(this));
         }

         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         this.bottomCell.setText(LocaleController.getString("PassportPersonalUploadInfo", 2131560303));
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
         if (this.currentDocumentsType.translation_required) {
            this.headerCell = new HeaderCell(var1);
            this.headerCell.setText(LocaleController.getString("PassportTranslation", 2131560337));
            this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
            this.translationLayout = new LinearLayout(var1);
            this.translationLayout.setOrientation(1);
            this.linearLayout2.addView(this.translationLayout, LayoutHelper.createLinear(-1, -2));
            this.uploadTranslationCell = new TextSettingsCell(var1);
            this.uploadTranslationCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            this.linearLayout2.addView(this.uploadTranslationCell, LayoutHelper.createLinear(-1, -2));
            this.uploadTranslationCell.setOnClickListener(new _$$Lambda$PassportActivity$ijAn79XSwEsHcuejC6hU9kEtOuA(this));
            this.bottomCellTranslation = new TextInfoPrivacyCell(var1);
            this.bottomCellTranslation.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
            if (this.currentBotId != 0) {
               this.noAllTranslationErrorText = LocaleController.getString("PassportAddTranslationUploadInfo", 2131560188);
            } else {
               TLRPC.SecureValueType var16 = this.currentDocumentsType.type;
               if (var16 instanceof TLRPC.TL_secureValueTypePassport) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddPassportInfo", 2131560178);
               } else if (var16 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddInternalPassportInfo", 2131560175);
               } else if (var16 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddIdentityCardInfo", 2131560173);
               } else if (var16 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                  this.noAllTranslationErrorText = LocaleController.getString("PassportAddDriverLicenceInfo", 2131560172);
               } else {
                  this.noAllTranslationErrorText = "";
               }
            }

            CharSequence var20 = this.noAllTranslationErrorText;
            HashMap var5 = this.documentsErrors;
            Object var17 = var20;
            if (var5 != null) {
               String var23 = (String)var5.get("translation_all");
               var17 = var20;
               if (var23 != null) {
                  var17 = new SpannableStringBuilder(var23);
                  ((SpannableStringBuilder)var17).append("\n\n");
                  ((SpannableStringBuilder)var17).append(this.noAllTranslationErrorText);
                  ((SpannableStringBuilder)var17).setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteRedText3")), 0, var23.length(), 33);
                  this.errorsValues.put("translation_all", "");
               }
            }

            this.bottomCellTranslation.setText((CharSequence)var17);
            this.linearLayout2.addView(this.bottomCellTranslation, LayoutHelper.createLinear(-1, -2));
         }
      } else if (VERSION.SDK_INT >= 18) {
         this.scanDocumentCell = new TextSettingsCell(var1);
         this.scanDocumentCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         this.scanDocumentCell.setText(LocaleController.getString("PassportScanPassport", 2131560322), false);
         this.linearLayout2.addView(this.scanDocumentCell, LayoutHelper.createLinear(-1, -2));
         this.scanDocumentCell.setOnClickListener(new _$$Lambda$PassportActivity$76809PrGNfIFwFdK4EfyZ7l0wQM(this));
         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         this.bottomCell.setText(LocaleController.getString("PassportScanPassportInfo", 2131560323));
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      }

      this.headerCell = new HeaderCell(var1);
      if (this.documentOnly) {
         this.headerCell.setText(LocaleController.getString("PassportDocument", 2131560214));
      } else {
         this.headerCell.setText(LocaleController.getString("PassportPersonal", 2131560300));
      }

      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      byte var6;
      if (this.currentDocumentsType != null) {
         var6 = 9;
      } else {
         var6 = 7;
      }

      this.inputFields = new EditTextBoldCursor[var6];

      final EditTextBoldCursor var8;
      FrameLayout var24;
      EditTextBoldCursor var28;
      for(int var7 = 0; var7 < var6; ++var7) {
         var8 = new EditTextBoldCursor(var1);
         this.inputFields[var7] = var8;
         var24 = new FrameLayout(var1) {
            private StaticLayout errorLayout;
            private float offsetX;

            protected void onDraw(Canvas var1) {
               if (this.errorLayout != null) {
                  var1.save();
                  var1.translate((float)AndroidUtilities.dp(21.0F) + this.offsetX, var8.getLineY() + (float)AndroidUtilities.dp(3.0F));
                  this.errorLayout.draw(var1);
                  var1.restore();
               }

            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(34.0F);
               this.errorLayout = var8.getErrorLayout(var3);
               StaticLayout var4 = this.errorLayout;
               int var5 = var2;
               if (var4 != null) {
                  int var6 = var4.getLineCount();
                  int var7 = 0;
                  if (var6 > 1) {
                     var2 = MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + (this.errorLayout.getLineBottom(var6 - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                  }

                  var5 = var2;
                  if (LocaleController.isRTL) {
                     float var8x = 0.0F;

                     while(true) {
                        var5 = var2;
                        if (var7 >= var6) {
                           break;
                        }

                        if (this.errorLayout.getLineLeft(var7) != 0.0F) {
                           this.offsetX = 0.0F;
                           var5 = var2;
                           break;
                        }

                        var8x = Math.max(var8x, this.errorLayout.getLineWidth(var7));
                        if (var7 == var6 - 1) {
                           this.offsetX = (float)var3 - var8x;
                        }

                        ++var7;
                     }
                  }
               }

               super.onMeasure(var1, var5);
            }
         };
         var24.setWillNotDraw(false);
         this.linearLayout2.addView(var24, LayoutHelper.createLinear(-1, 64));
         var24.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         if (var7 == var6 - 1) {
            this.extraBackgroundView = new View(var1);
            this.extraBackgroundView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.extraBackgroundView, LayoutHelper.createLinear(-1, 6));
         }

         if (this.documentOnly && this.currentDocumentsType != null && var7 < 7) {
            var24.setVisibility(8);
            View var21 = this.extraBackgroundView;
            if (var21 != null) {
               var21.setVisibility(8);
            }
         }

         this.inputFields[var7].setTag(var7);
         this.inputFields[var7].setSupportRtlHint(true);
         this.inputFields[var7].setTextSize(1, 16.0F);
         this.inputFields[var7].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var7].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var7].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         this.inputFields[var7].setTransformHintToHeader(true);
         this.inputFields[var7].setBackgroundDrawable((Drawable)null);
         this.inputFields[var7].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var7].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var7].setCursorWidth(1.5F);
         this.inputFields[var7].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
         if (var7 != 5 && var7 != 6) {
            if (var7 != 3 && var7 != 8) {
               if (var7 == 4) {
                  this.inputFields[var7].setOnTouchListener(new _$$Lambda$PassportActivity$_CPWpKIngk2P0GwmkCG7jNpTBgM(this));
                  this.inputFields[var7].setInputType(0);
                  this.inputFields[var7].setFocusable(false);
               } else {
                  this.inputFields[var7].setInputType(16385);
                  this.inputFields[var7].setImeOptions(268435461);
               }
            } else {
               this.inputFields[var7].setOnTouchListener(new _$$Lambda$PassportActivity$TcnlWpKn54098zIjNVHAKGv7mPQ(this, var1));
               this.inputFields[var7].setInputType(0);
               this.inputFields[var7].setFocusable(false);
            }
         } else {
            this.inputFields[var7].setOnTouchListener(new _$$Lambda$PassportActivity$BkWsiGp0SACn1vJ9h8YBMYKkLVM(this));
            this.inputFields[var7].setInputType(0);
         }

         final HashMap var22;
         switch(var7) {
         case 0:
            if (this.currentType.native_names) {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportNameLatin", 2131560291));
            } else {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportName", 2131560288));
            }

            var22 = this.currentValues;
            var18 = "first_name";
            break;
         case 1:
            if (this.currentType.native_names) {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportMidnameLatin", 2131560287));
            } else {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportMidname", 2131560285));
            }

            var22 = this.currentValues;
            var18 = "middle_name";
            break;
         case 2:
            if (this.currentType.native_names) {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportSurnameLatin", 2131560336));
            } else {
               this.inputFields[var7].setHintText(LocaleController.getString("PassportSurname", 2131560334));
            }

            var22 = this.currentValues;
            var18 = "last_name";
            break;
         case 3:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportBirthdate", 2131560194));
            var22 = this.currentValues;
            var18 = "birth_date";
            break;
         case 4:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportGender", 2131560226));
            var22 = this.currentValues;
            var18 = "gender";
            break;
         case 5:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportCitizenship", 2131560195));
            var22 = this.currentValues;
            var18 = "country_code";
            break;
         case 6:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportResidence", 2131560318));
            var22 = this.currentValues;
            var18 = "residence_country_code";
            break;
         case 7:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportDocumentNumber", 2131560215));
            var22 = this.currentDocumentValues;
            var18 = "document_no";
            break;
         case 8:
            this.inputFields[var7].setHintText(LocaleController.getString("PassportExpired", 2131560222));
            var22 = this.currentDocumentValues;
            var18 = "expiry_date";
            break;
         default:
            continue;
         }

         this.setFieldValues(var22, this.inputFields[var7], var18);
         EditTextBoldCursor[] var9 = this.inputFields;
         var9[var7].setSelection(var9[var7].length());
         if (var7 != 0 && var7 != 2 && var7 != 1) {
            this.inputFields[var7].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  PassportActivity var2 = PassportActivity.this;
                  EditTextBoldCursor var3 = var8;
                  String var4 = var18;
                  boolean var5;
                  if (var22 == var2.currentDocumentValues) {
                     var5 = true;
                  } else {
                     var5 = false;
                  }

                  var2.checkFieldForError(var3, var4, var1, var5);
                  int var6 = (Integer)var8.getTag();
                  EditTextBoldCursor var7 = PassportActivity.this.inputFields[var6];
                  if (var6 == 6) {
                     PassportActivity.this.checkNativeFields(true);
                  }

               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         } else {
            this.inputFields[var7].addTextChangedListener(new TextWatcher() {
               private boolean ignore;

               public void afterTextChanged(Editable var1) {
                  if (!this.ignore) {
                     int var2 = (Integer)var8.getTag();
                     int var3 = 0;

                     boolean var5;
                     while(true) {
                        if (var3 >= var1.length()) {
                           var5 = false;
                           break;
                        }

                        char var4 = var1.charAt(var3);
                        if ((var4 < '0' || var4 > '9') && (var4 < 'a' || var4 > 'z') && (var4 < 'A' || var4 > 'Z') && var4 != ' ' && var4 != '\'' && var4 != ',' && var4 != '.' && var4 != '&' && var4 != '-' && var4 != '/') {
                           var5 = true;
                           break;
                        }

                        ++var3;
                     }

                     if (var5 && !PassportActivity.this.allowNonLatinName) {
                        var8.setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131560343));
                     } else {
                        PassportActivity.this.nonLatinNames[var2] = var5;
                        PassportActivity.this.checkFieldForError(var8, var18, var1, false);
                     }

                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         }

         this.inputFields[var7].setPadding(0, 0, 0, 0);
         var28 = this.inputFields[var7];
         byte var10;
         if (LocaleController.isRTL) {
            var10 = 5;
         } else {
            var10 = 3;
         }

         var28.setGravity(var10 | 16);
         var24.addView(this.inputFields[var7], LayoutHelper.createFrame(-1, -1.0F, 51, 21.0F, 0.0F, 21.0F, 0.0F));
         this.inputFields[var7].setOnEditorActionListener(new _$$Lambda$PassportActivity$IUKnReQZFqjMBZyTSuqCOeb27jI(this));
      }

      this.sectionCell2 = new ShadowSectionCell(var1);
      this.linearLayout2.addView(this.sectionCell2, LayoutHelper.createLinear(-1, -2));
      this.headerCell = new HeaderCell(var1);
      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      this.inputExtraFields = new EditTextBoldCursor[3];

      for(int var25 = 0; var25 < 3; ++var25) {
         var8 = new EditTextBoldCursor(var1);
         this.inputExtraFields[var25] = var8;
         var24 = new FrameLayout(var1) {
            private StaticLayout errorLayout;
            private float offsetX;

            protected void onDraw(Canvas var1) {
               if (this.errorLayout != null) {
                  var1.save();
                  var1.translate((float)AndroidUtilities.dp(21.0F) + this.offsetX, var8.getLineY() + (float)AndroidUtilities.dp(3.0F));
                  this.errorLayout.draw(var1);
                  var1.restore();
               }

            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(34.0F);
               this.errorLayout = var8.getErrorLayout(var3);
               StaticLayout var4 = this.errorLayout;
               int var5 = var2;
               if (var4 != null) {
                  int var6 = var4.getLineCount();
                  int var7 = 0;
                  if (var6 > 1) {
                     var2 = MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + (this.errorLayout.getLineBottom(var6 - 1) - this.errorLayout.getLineBottom(0)), 1073741824);
                  }

                  var5 = var2;
                  if (LocaleController.isRTL) {
                     float var8x = 0.0F;

                     while(true) {
                        var5 = var2;
                        if (var7 >= var6) {
                           break;
                        }

                        if (this.errorLayout.getLineLeft(var7) != 0.0F) {
                           this.offsetX = 0.0F;
                           var5 = var2;
                           break;
                        }

                        var8x = Math.max(var8x, this.errorLayout.getLineWidth(var7));
                        if (var7 == var6 - 1) {
                           this.offsetX = (float)var3 - var8x;
                        }

                        ++var7;
                     }
                  }
               }

               super.onMeasure(var1, var5);
            }
         };
         var24.setWillNotDraw(false);
         this.linearLayout2.addView(var24, LayoutHelper.createLinear(-1, 64));
         var24.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         if (var25 == 2) {
            this.extraBackgroundView2 = new View(var1);
            this.extraBackgroundView2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout2.addView(this.extraBackgroundView2, LayoutHelper.createLinear(-1, 6));
         }

         this.inputExtraFields[var25].setTag(var25);
         this.inputExtraFields[var25].setSupportRtlHint(true);
         this.inputExtraFields[var25].setTextSize(1, 16.0F);
         this.inputExtraFields[var25].setHintColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputExtraFields[var25].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputExtraFields[var25].setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         this.inputExtraFields[var25].setTransformHintToHeader(true);
         this.inputExtraFields[var25].setBackgroundDrawable((Drawable)null);
         this.inputExtraFields[var25].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputExtraFields[var25].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputExtraFields[var25].setCursorWidth(1.5F);
         this.inputExtraFields[var25].setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
         this.inputExtraFields[var25].setInputType(16385);
         this.inputExtraFields[var25].setImeOptions(268435461);
         HashMap var26;
         final String var29;
         if (var25 != 0) {
            if (var25 != 1) {
               if (var25 != 2) {
                  continue;
               }

               var26 = this.currentValues;
               var29 = "last_name_native";
            } else {
               var26 = this.currentValues;
               var29 = "middle_name_native";
            }
         } else {
            var26 = this.currentValues;
            var29 = "first_name_native";
         }

         this.setFieldValues(var26, this.inputExtraFields[var25], var29);
         EditTextBoldCursor[] var30 = this.inputExtraFields;
         var30[var25].setSelection(var30[var25].length());
         if (var25 == 0 || var25 == 2 || var25 == 1) {
            this.inputExtraFields[var25].addTextChangedListener(new TextWatcher() {
               private boolean ignore;

               public void afterTextChanged(Editable var1) {
                  if (!this.ignore) {
                     PassportActivity.this.checkFieldForError(var8, var29, var1, false);
                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         }

         this.inputExtraFields[var25].setPadding(0, 0, 0, 0);
         var28 = this.inputExtraFields[var25];
         byte var27;
         if (LocaleController.isRTL) {
            var27 = 5;
         } else {
            var27 = 3;
         }

         var28.setGravity(var27 | 16);
         var24.addView(this.inputExtraFields[var25], LayoutHelper.createFrame(-1, -1.0F, 51, 21.0F, 0.0F, 21.0F, 0.0F));
         this.inputExtraFields[var25].setOnEditorActionListener(new _$$Lambda$PassportActivity$2R_VniY9bMOiViEoeO5veQ3eJ7w(this));
      }

      this.nativeInfoCell = new TextInfoPrivacyCell(var1);
      this.linearLayout2.addView(this.nativeInfoCell, LayoutHelper.createLinear(-1, -2));
      if ((this.currentBotId == 0 && this.currentDocumentsType != null || this.currentTypeValue == null || this.documentOnly) && this.currentDocumentsTypeValue == null) {
         this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      } else {
         TLRPC.TL_secureValue var31 = this.currentDocumentsTypeValue;
         if (var31 != null) {
            this.addDocumentViews(var31.files);
            TLRPC.SecureFile var32 = this.currentDocumentsTypeValue.front_side;
            if (var32 instanceof TLRPC.TL_secureFile) {
               this.addDocumentViewInternal((TLRPC.TL_secureFile)var32, 2);
            }

            var32 = this.currentDocumentsTypeValue.reverse_side;
            if (var32 instanceof TLRPC.TL_secureFile) {
               this.addDocumentViewInternal((TLRPC.TL_secureFile)var32, 3);
            }

            var32 = this.currentDocumentsTypeValue.selfie;
            if (var32 instanceof TLRPC.TL_secureFile) {
               this.addDocumentViewInternal((TLRPC.TL_secureFile)var32, 1);
            }

            this.addTranslationDocumentViews(this.currentDocumentsTypeValue.translation);
         }

         TextSettingsCell var33 = new TextSettingsCell(var1);
         var33.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
         var33.setBackgroundDrawable(Theme.getSelectorDrawable(true));
         if (this.currentDocumentsType == null) {
            var33.setText(LocaleController.getString("PassportDeleteInfo", 2131560205), false);
         } else {
            var33.setText(LocaleController.getString("PassportDeleteDocument", 2131560200), false);
         }

         this.linearLayout2.addView(var33, LayoutHelper.createLinear(-1, -2));
         var33.setOnClickListener(new _$$Lambda$PassportActivity$gpc1SNZjQfFOYB3jBSNPORk4F_s(this));
         this.nativeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         this.sectionCell = new ShadowSectionCell(var1);
         this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
      }

      this.updateInterfaceStringsForDocumentType();
      this.checkNativeFields(false);
   }

   private void createManageInterface(Context var1) {
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
      super.actionBar.createMenu().addItem(1, 2131165786);
      this.headerCell = new HeaderCell(var1);
      this.headerCell.setText(LocaleController.getString("PassportProvidedInformation", 2131560313));
      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      this.sectionCell = new ShadowSectionCell(var1);
      this.sectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
      this.linearLayout2.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
      this.addDocumentCell = new TextSettingsCell(var1);
      this.addDocumentCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
      this.addDocumentCell.setText(LocaleController.getString("PassportNoDocumentsAdd", 2131560296), true);
      this.linearLayout2.addView(this.addDocumentCell, LayoutHelper.createLinear(-1, -2));
      this.addDocumentCell.setOnClickListener(new _$$Lambda$PassportActivity$yv3E0a1LzZcIvpbSmJY3xiJFxqI(this));
      this.deletePassportCell = new TextSettingsCell(var1);
      this.deletePassportCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText3"));
      this.deletePassportCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
      this.deletePassportCell.setText(LocaleController.getString("TelegramPassportDelete", 2131560875), false);
      this.linearLayout2.addView(this.deletePassportCell, LayoutHelper.createLinear(-1, -2));
      this.deletePassportCell.setOnClickListener(new _$$Lambda$PassportActivity$CCLsFyBhbKH_We0aA4khftAzZjg(this));
      this.addDocumentSectionCell = new ShadowSectionCell(var1);
      this.addDocumentSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.linearLayout2.addView(this.addDocumentSectionCell, LayoutHelper.createLinear(-1, -2));
      this.emptyLayout = new LinearLayout(var1);
      this.emptyLayout.setOrientation(1);
      this.emptyLayout.setGravity(17);
      this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      if (AndroidUtilities.isTablet()) {
         this.linearLayout2.addView(this.emptyLayout, new LayoutParams(-1, AndroidUtilities.dp(528.0F) - ActionBar.getCurrentActionBarHeight()));
      } else {
         this.linearLayout2.addView(this.emptyLayout, new LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()));
      }

      this.emptyImageView = new ImageView(var1);
      this.emptyImageView.setImageResource(2131165692);
      this.emptyImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("sessions_devicesImage"), Mode.MULTIPLY));
      this.emptyLayout.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
      this.emptyTextView1 = new TextView(var1);
      this.emptyTextView1.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.emptyTextView1.setGravity(17);
      this.emptyTextView1.setTextSize(1, 15.0F);
      this.emptyTextView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.emptyTextView1.setText(LocaleController.getString("PassportNoDocuments", 2131560295));
      this.emptyLayout.addView(this.emptyTextView1, LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
      this.emptyTextView2 = new TextView(var1);
      this.emptyTextView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.emptyTextView2.setGravity(17);
      this.emptyTextView2.setTextSize(1, 14.0F);
      this.emptyTextView2.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.emptyTextView2.setText(LocaleController.getString("PassportNoDocumentsInfo", 2131560297));
      this.emptyLayout.addView(this.emptyTextView2, LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
      this.emptyTextView3 = new TextView(var1);
      this.emptyTextView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
      this.emptyTextView3.setGravity(17);
      this.emptyTextView3.setTextSize(1, 15.0F);
      this.emptyTextView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.emptyTextView3.setGravity(17);
      this.emptyTextView3.setText(LocaleController.getString("PassportNoDocumentsAdd", 2131560296).toUpperCase());
      this.emptyLayout.addView(this.emptyTextView3, LayoutHelper.createLinear(-2, 30, 17, 0, 16, 0, 0));
      this.emptyTextView3.setOnClickListener(new _$$Lambda$PassportActivity$7cIXxuOb_yUfSpLF2ezyFpul_wg(this));
      int var3 = this.currentForm.values.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         boolean var7;
         TLRPC.TL_secureRequiredType var10;
         ArrayList var12;
         label29: {
            TLRPC.TL_secureValue var5 = (TLRPC.TL_secureValue)this.currentForm.values.get(var4);
            TLRPC.TL_secureRequiredType var6;
            ArrayList var9;
            TLRPC.TL_secureRequiredType var11;
            if (this.isPersonalDocument(var5.type)) {
               var9 = new ArrayList();
               var6 = new TLRPC.TL_secureRequiredType();
               var6.type = var5.type;
               var6.selfie_required = true;
               var6.translation_required = true;
               var9.add(var6);
               var11 = new TLRPC.TL_secureRequiredType();
               var11.type = new TLRPC.TL_secureValueTypePersonalDetails();
            } else {
               if (!this.isAddressDocument(var5.type)) {
                  var10 = new TLRPC.TL_secureRequiredType();
                  var10.type = var5.type;
                  var12 = null;
                  var7 = false;
                  break label29;
               }

               var9 = new ArrayList();
               var6 = new TLRPC.TL_secureRequiredType();
               var6.type = var5.type;
               var6.translation_required = true;
               var9.add(var6);
               var11 = new TLRPC.TL_secureRequiredType();
               var11.type = new TLRPC.TL_secureValueTypeAddress();
            }

            var12 = var9;
            var7 = true;
            var10 = var11;
         }

         boolean var8;
         if (var4 == var3 - 1) {
            var8 = true;
         } else {
            var8 = false;
         }

         this.addField(var1, var10, var12, var7, var8);
      }

      this.updateManageVisibility();
   }

   private void createPasswordInterface(Context var1) {
      int var2;
      TLRPC.User var3;
      if (this.currentForm != null) {
         var2 = 0;

         while(true) {
            if (var2 >= this.currentForm.users.size()) {
               var3 = null;
               break;
            }

            var3 = (TLRPC.User)this.currentForm.users.get(var2);
            if (var3.id == this.currentBotId) {
               break;
            }

            ++var2;
         }
      } else {
         var3 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
      }

      FrameLayout var4 = (FrameLayout)super.fragmentView;
      super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showProgress();
      var4.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.passwordAvatarContainer = new FrameLayout(var1);
      this.linearLayout2.addView(this.passwordAvatarContainer, LayoutHelper.createLinear(-1, 100));
      BackupImageView var5 = new BackupImageView(var1);
      var5.setRoundRadius(AndroidUtilities.dp(32.0F));
      this.passwordAvatarContainer.addView(var5, LayoutHelper.createFrame(64, 64.0F, 17, 0.0F, 8.0F, 0.0F, 0.0F));
      AvatarDrawable var11 = new AvatarDrawable(var3);
      var5.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)var11, (Object)var3);
      this.passwordRequestTextView = new TextInfoPrivacyCell(var1);
      this.passwordRequestTextView.getTextView().setGravity(1);
      if (this.currentBotId == 0) {
         this.passwordRequestTextView.setText(LocaleController.getString("PassportSelfRequest", 2131560328));
      } else {
         this.passwordRequestTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131560314, UserObject.getFirstName(var3))));
      }

      ((android.widget.FrameLayout.LayoutParams)this.passwordRequestTextView.getTextView().getLayoutParams()).gravity = 1;
      LinearLayout var12 = this.linearLayout2;
      TextInfoPrivacyCell var13 = this.passwordRequestTextView;
      boolean var6 = LocaleController.isRTL;
      byte var7 = 5;
      byte var10;
      if (var6) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      var12.addView(var13, LayoutHelper.createFrame(-2, -2.0F, var10 | 48, 21.0F, 0.0F, 21.0F, 0.0F));
      this.noPasswordImageView = new ImageView(var1);
      this.noPasswordImageView.setImageResource(2131165693);
      this.noPasswordImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_messagePanelIcons"), Mode.MULTIPLY));
      this.linearLayout2.addView(this.noPasswordImageView, LayoutHelper.createLinear(-2, -2, 49, 0, 13, 0, 0));
      this.noPasswordTextView = new TextView(var1);
      this.noPasswordTextView.setTextSize(1, 14.0F);
      this.noPasswordTextView.setGravity(1);
      this.noPasswordTextView.setPadding(AndroidUtilities.dp(21.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(21.0F), AndroidUtilities.dp(17.0F));
      this.noPasswordTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.noPasswordTextView.setText(LocaleController.getString("TelegramPassportCreatePasswordInfo", 2131560874));
      var12 = this.linearLayout2;
      TextView var14 = this.noPasswordTextView;
      if (LocaleController.isRTL) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      var12.addView(var14, LayoutHelper.createFrame(-2, -2.0F, var10 | 48, 21.0F, 10.0F, 21.0F, 0.0F));
      this.noPasswordSetTextView = new TextView(var1);
      this.noPasswordSetTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText5"));
      this.noPasswordSetTextView.setGravity(17);
      this.noPasswordSetTextView.setTextSize(1, 16.0F);
      this.noPasswordSetTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.noPasswordSetTextView.setText(LocaleController.getString("TelegramPassportCreatePassword", 2131560873));
      var12 = this.linearLayout2;
      var14 = this.noPasswordSetTextView;
      if (LocaleController.isRTL) {
         var10 = 5;
      } else {
         var10 = 3;
      }

      var12.addView(var14, LayoutHelper.createFrame(-1, 24.0F, var10 | 48, 21.0F, 9.0F, 21.0F, 0.0F));
      this.noPasswordSetTextView.setOnClickListener(new _$$Lambda$PassportActivity$M4nzx3gJGtJxSj4qK5fr27GLS6o(this));
      this.inputFields = new EditTextBoldCursor[1];
      this.inputFieldContainers = new ViewGroup[1];

      for(var2 = 0; var2 < 1; ++var2) {
         this.inputFieldContainers[var2] = new FrameLayout(var1);
         this.linearLayout2.addView(this.inputFieldContainers[var2], LayoutHelper.createLinear(-1, 50));
         this.inputFieldContainers[var2].setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.inputFields[var2] = new EditTextBoldCursor(var1);
         this.inputFields[var2].setTag(var2);
         this.inputFields[var2].setTextSize(1, 16.0F);
         this.inputFields[var2].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var2].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var2].setBackgroundDrawable((Drawable)null);
         this.inputFields[var2].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var2].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var2].setCursorWidth(1.5F);
         this.inputFields[var2].setInputType(129);
         this.inputFields[var2].setMaxLines(1);
         this.inputFields[var2].setLines(1);
         this.inputFields[var2].setSingleLine(true);
         this.inputFields[var2].setTransformationMethod(PasswordTransformationMethod.getInstance());
         this.inputFields[var2].setTypeface(Typeface.DEFAULT);
         this.inputFields[var2].setImeOptions(268435462);
         this.inputFields[var2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
         EditTextBoldCursor var15 = this.inputFields[var2];
         byte var8;
         if (LocaleController.isRTL) {
            var8 = 5;
         } else {
            var8 = 3;
         }

         var15.setGravity(var8);
         this.inputFieldContainers[var2].addView(this.inputFields[var2], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
         this.inputFields[var2].setOnEditorActionListener(new _$$Lambda$PassportActivity$It4B3VALo5oSVQDPd8g6gcO_bFY(this));
         this.inputFields[var2].setCustomSelectionActionModeCallback(new Callback() {
            public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
               return false;
            }

            public boolean onCreateActionMode(ActionMode var1, Menu var2) {
               return false;
            }

            public void onDestroyActionMode(ActionMode var1) {
            }

            public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
               return false;
            }
         });
      }

      this.passwordInfoRequestTextView = new TextInfoPrivacyCell(var1);
      this.passwordInfoRequestTextView.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.passwordInfoRequestTextView.setText(LocaleController.formatString("PassportRequestPasswordInfo", 2131560315));
      this.linearLayout2.addView(this.passwordInfoRequestTextView, LayoutHelper.createLinear(-1, -2));
      this.passwordForgotButton = new TextView(var1);
      this.passwordForgotButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
      this.passwordForgotButton.setTextSize(1, 14.0F);
      this.passwordForgotButton.setText(LocaleController.getString("ForgotPassword", 2131559503));
      this.passwordForgotButton.setPadding(0, 0, 0, 0);
      LinearLayout var9 = this.linearLayout2;
      TextView var16 = this.passwordForgotButton;
      if (LocaleController.isRTL) {
         var10 = var7;
      } else {
         var10 = 3;
      }

      var9.addView(var16, LayoutHelper.createLinear(-2, 30, var10 | 48, 21, 0, 21, 0));
      this.passwordForgotButton.setOnClickListener(new _$$Lambda$PassportActivity$rCPvENS17XdkP3jlE3Ii__88K9s(this));
      this.updatePasswordInterface();
   }

   private void createPhoneInterface(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131560304));
      this.languageMap = new HashMap();

      Exception var10000;
      boolean var10001;
      String var18;
      label129: {
         label128: {
            BufferedReader var2;
            try {
               InputStreamReader var3 = new InputStreamReader(var1.getResources().getAssets().open("countries.txt"));
               var2 = new BufferedReader(var3);
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label128;
            }

            while(true) {
               try {
                  var18 = var2.readLine();
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               if (var18 == null) {
                  try {
                     var2.close();
                     break label129;
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }
               }

               String[] var19;
               try {
                  var19 = var18.split(";");
                  this.countriesArray.add(0, var19[2]);
                  this.countriesMap.put(var19[2], var19[0]);
                  this.codesMap.put(var19[0], var19[2]);
                  if (var19.length > 3) {
                     this.phoneFormatMap.put(var19[0], var19[3]);
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break;
               }

               try {
                  this.languageMap.put(var19[1], var19[2]);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var15 = var10000;
         FileLog.e((Throwable)var15);
      }

      Collections.sort(this.countriesArray, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
      String var16 = UserConfig.getInstance(super.currentAccount).getCurrentUser().phone;
      TextSettingsCell var4 = new TextSettingsCell(var1);
      var4.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
      var4.setBackgroundDrawable(Theme.getSelectorDrawable(true));
      PhoneFormat var20 = PhoneFormat.getInstance();
      StringBuilder var5 = new StringBuilder();
      var5.append("+");
      var5.append(var16);
      var4.setText(LocaleController.formatString("PassportPhoneUseSame", 2131560308, var20.format(var5.toString())), false);
      this.linearLayout2.addView(var4, LayoutHelper.createLinear(-1, -2));
      var4.setOnClickListener(new _$$Lambda$PassportActivity$ZXxmpnBqGMDCWhhQmZBD_R6_C50(this));
      this.bottomCell = new TextInfoPrivacyCell(var1);
      this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.bottomCell.setText(LocaleController.getString("PassportPhoneUseSameInfo", 2131560310));
      this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      this.headerCell = new HeaderCell(var1);
      this.headerCell.setText(LocaleController.getString("PassportPhoneUseOther", 2131560307));
      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      this.inputFields = new EditTextBoldCursor[3];

      for(int var6 = 0; var6 < 3; ++var6) {
         if (var6 == 2) {
            this.inputFields[var6] = new HintEditText(var1);
         } else {
            this.inputFields[var6] = new EditTextBoldCursor(var1);
         }

         Object var17;
         if (var6 == 1) {
            var17 = new LinearLayout(var1);
            ((LinearLayout)var17).setOrientation(0);
            this.linearLayout2.addView((View)var17, LayoutHelper.createLinear(-1, 50));
            ((ViewGroup)var17).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         } else if (var6 == 2) {
            var17 = (ViewGroup)this.inputFields[1].getParent();
         } else {
            var17 = new FrameLayout(var1);
            this.linearLayout2.addView((View)var17, LayoutHelper.createLinear(-1, 50));
            ((ViewGroup)var17).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         this.inputFields[var6].setTag(var6);
         this.inputFields[var6].setTextSize(1, 16.0F);
         this.inputFields[var6].setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.inputFields[var6].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var6].setBackgroundDrawable((Drawable)null);
         this.inputFields[var6].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.inputFields[var6].setCursorSize(AndroidUtilities.dp(20.0F));
         this.inputFields[var6].setCursorWidth(1.5F);
         if (var6 == 0) {
            this.inputFields[var6].setOnTouchListener(new _$$Lambda$PassportActivity$m7XOq19_n687jJsXOFpNqm9gDgg(this));
            this.inputFields[var6].setText(LocaleController.getString("ChooseCountry", 2131559086));
            this.inputFields[var6].setInputType(0);
            this.inputFields[var6].setFocusable(false);
         } else {
            this.inputFields[var6].setInputType(3);
            if (var6 == 2) {
               this.inputFields[var6].setImeOptions(268435462);
            } else {
               this.inputFields[var6].setImeOptions(268435461);
            }
         }

         EditTextBoldCursor[] var21 = this.inputFields;
         var21[var6].setSelection(var21[var6].length());
         byte var7 = 5;
         if (var6 == 1) {
            this.plusTextView = new TextView(var1);
            this.plusTextView.setText("+");
            this.plusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.plusTextView.setTextSize(1, 16.0F);
            ((ViewGroup)var17).addView(this.plusTextView, LayoutHelper.createLinear(-2, -2, 21.0F, 12.0F, 0.0F, 6.0F));
            this.inputFields[var6].setPadding(AndroidUtilities.dp(10.0F), 0, 0, 0);
            LengthFilter var22 = new LengthFilter(5);
            this.inputFields[var6].setFilters(new InputFilter[]{var22});
            this.inputFields[var6].setGravity(19);
            ((ViewGroup)var17).addView(this.inputFields[var6], LayoutHelper.createLinear(55, -2, 0.0F, 12.0F, 16.0F, 6.0F));
            this.inputFields[var6].addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  if (!PassportActivity.this.ignoreOnTextChange) {
                     PassportActivity.this.ignoreOnTextChange = true;
                     String var7 = PhoneFormat.stripExceptNumbers(PassportActivity.this.inputFields[1].getText().toString());
                     PassportActivity.this.inputFields[1].setText(var7);
                     HintEditText var2 = (HintEditText)PassportActivity.this.inputFields[2];
                     if (var7.length() == 0) {
                        var2.setHintText((String)null);
                        var2.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                        PassportActivity.this.inputFields[0].setText(LocaleController.getString("ChooseCountry", 2131559086));
                     } else {
                        int var3 = var7.length();
                        int var4 = 4;
                        String var5;
                        boolean var9;
                        String var11;
                        if (var3 <= 4) {
                           var11 = null;
                           var9 = false;
                        } else {
                           StringBuilder var6;
                           while(true) {
                              if (var4 < 1) {
                                 var11 = null;
                                 var9 = false;
                                 break;
                              }

                              var5 = var7.substring(0, var4);
                              if ((String)PassportActivity.this.codesMap.get(var5) != null) {
                                 var6 = new StringBuilder();
                                 var6.append(var7.substring(var4));
                                 var6.append(PassportActivity.this.inputFields[2].getText().toString());
                                 var11 = var6.toString();
                                 PassportActivity.this.inputFields[1].setText(var5);
                                 var9 = true;
                                 var7 = var5;
                                 break;
                              }

                              --var4;
                           }

                           if (!var9) {
                              var6 = new StringBuilder();
                              var6.append(var7.substring(1));
                              var6.append(PassportActivity.this.inputFields[2].getText().toString());
                              var11 = var6.toString();
                              EditTextBoldCursor var10 = PassportActivity.this.inputFields[1];
                              var7 = var7.substring(0, 1);
                              var10.setText(var7);
                           }
                        }

                        boolean var8;
                        label43: {
                           var5 = (String)PassportActivity.this.codesMap.get(var7);
                           if (var5 != null) {
                              var3 = PassportActivity.this.countriesArray.indexOf(var5);
                              if (var3 != -1) {
                                 PassportActivity.this.inputFields[0].setText((CharSequence)PassportActivity.this.countriesArray.get(var3));
                                 var7 = (String)PassportActivity.this.phoneFormatMap.get(var7);
                                 if (var7 != null) {
                                    var2.setHintText(var7.replace('X', '–'));
                                    var2.setHint((CharSequence)null);
                                 }

                                 var8 = true;
                                 break label43;
                              }
                           }

                           var8 = false;
                        }

                        if (!var8) {
                           var2.setHintText((String)null);
                           var2.setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
                           PassportActivity.this.inputFields[0].setText(LocaleController.getString("WrongCountry", 2131561125));
                        }

                        if (!var9) {
                           PassportActivity.this.inputFields[1].setSelection(PassportActivity.this.inputFields[1].getText().length());
                        }

                        if (var11 != null) {
                           var2.requestFocus();
                           var2.setText(var11);
                           var2.setSelection(var2.length());
                        }
                     }

                     PassportActivity.this.ignoreOnTextChange = false;
                  }
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
         } else if (var6 == 2) {
            this.inputFields[var6].setPadding(0, 0, 0, 0);
            this.inputFields[var6].setGravity(19);
            this.inputFields[var6].setHintText((String)null);
            this.inputFields[var6].setHint(LocaleController.getString("PaymentShippingPhoneNumber", 2131560398));
            ((ViewGroup)var17).addView(this.inputFields[var6], LayoutHelper.createLinear(-1, -2, 0.0F, 12.0F, 21.0F, 6.0F));
            this.inputFields[var6].addTextChangedListener(new TextWatcher() {
               private int actionPosition;
               private int characterAction = -1;

               public void afterTextChanged(Editable var1) {
                  if (!PassportActivity.this.ignoreOnPhoneChange) {
                     HintEditText var2 = (HintEditText)PassportActivity.this.inputFields[2];
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

                     PassportActivity.this.ignoreOnPhoneChange = true;
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
                     PassportActivity.this.ignoreOnPhoneChange = false;
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
            this.inputFields[var6].setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
            EditTextBoldCursor var23 = this.inputFields[var6];
            if (!LocaleController.isRTL) {
               var7 = 3;
            }

            var23.setGravity(var7);
            ((ViewGroup)var17).addView(this.inputFields[var6], LayoutHelper.createFrame(-1, -2.0F, 51, 21.0F, 12.0F, 21.0F, 6.0F));
         }

         this.inputFields[var6].setOnEditorActionListener(new _$$Lambda$PassportActivity$Ud8QO9mJkulFHffuDeCmjGoUGKM(this));
         if (var6 == 2) {
            this.inputFields[var6].setOnKeyListener(new _$$Lambda$PassportActivity$jS_iy1VRiF9kFI3x6V7_e2W45Zc(this));
         }

         if (var6 == 0) {
            View var24 = new View(var1);
            this.dividers.add(var24);
            var24.setBackgroundColor(Theme.getColor("divider"));
            ((ViewGroup)var17).addView(var24, new android.widget.FrameLayout.LayoutParams(-1, 1, 83));
         }
      }

      var16 = null;

      label101: {
         label100: {
            label135: {
               TelephonyManager var25;
               try {
                  var25 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label135;
               }

               if (var25 == null) {
                  break label101;
               }

               try {
                  var18 = var25.getSimCountryIso().toUpperCase();
                  break label100;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }

            Exception var26 = var10000;
            FileLog.e((Throwable)var26);
            break label101;
         }

         var16 = var18;
      }

      if (var16 != null) {
         var16 = (String)this.languageMap.get(var16);
         if (var16 != null && this.countriesArray.indexOf(var16) != -1) {
            this.inputFields[1].setText((CharSequence)this.countriesMap.get(var16));
         }
      }

      this.bottomCell = new TextInfoPrivacyCell(var1);
      this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      this.bottomCell.setText(LocaleController.getString("PassportPhoneUploadInfo", 2131560306));
      this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
   }

   private void createPhoneVerificationInterface(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("PassportPhone", 2131560304));
      FrameLayout var2 = new FrameLayout(var1);
      this.scrollView.addView(var2, LayoutHelper.createScroll(-1, -2, 51));

      for(int var3 = 0; var3 < 3; ++var3) {
         this.views[var3] = new PassportActivity.PhoneConfirmationView(var1, var3 + 2);
         this.views[var3].setVisibility(8);
         SlideView var4 = this.views[var3];
         boolean var5 = AndroidUtilities.isTablet();
         float var6 = 18.0F;
         float var7;
         if (var5) {
            var7 = 26.0F;
         } else {
            var7 = 18.0F;
         }

         if (AndroidUtilities.isTablet()) {
            var6 = 26.0F;
         }

         var2.addView(var4, LayoutHelper.createFrame(-1, -1.0F, 51, var7, 30.0F, var6, 0.0F));
      }

      Bundle var8 = new Bundle();
      var8.putString("phone", (String)this.currentValues.get("phone"));
      this.fillNextCodeParams(var8, this.currentPhoneVerification, false);
   }

   private void createRequestInterface(Context var1) {
      int var2;
      TLRPC.User var3;
      label243: {
         if (this.currentForm != null) {
            for(var2 = 0; var2 < this.currentForm.users.size(); ++var2) {
               var3 = (TLRPC.User)this.currentForm.users.get(var2);
               if (var3.id == this.currentBotId) {
                  break label243;
               }
            }
         }

         var3 = null;
      }

      FrameLayout var4 = (FrameLayout)super.fragmentView;
      super.actionBar.setTitle(LocaleController.getString("TelegramPassport", 2131560872));
      super.actionBar.createMenu().addItem(1, 2131165786);
      if (var3 != null) {
         FrameLayout var5 = new FrameLayout(var1);
         this.linearLayout2.addView(var5, LayoutHelper.createLinear(-1, 100));
         BackupImageView var6 = new BackupImageView(var1);
         var6.setRoundRadius(AndroidUtilities.dp(32.0F));
         var5.addView(var6, LayoutHelper.createFrame(64, 64.0F, 17, 0.0F, 8.0F, 0.0F, 0.0F));
         AvatarDrawable var24 = new AvatarDrawable(var3);
         var6.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)var24, (Object)var3);
         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165396, "windowBackgroundGrayShadow"));
         this.bottomCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportRequest", 2131560314, UserObject.getFirstName(var3))));
         this.bottomCell.getTextView().setGravity(1);
         ((android.widget.FrameLayout.LayoutParams)this.bottomCell.getTextView().getLayoutParams()).gravity = 1;
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      }

      this.headerCell = new HeaderCell(var1);
      this.headerCell.setText(LocaleController.getString("PassportRequestedInformation", 2131560316));
      this.headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.linearLayout2.addView(this.headerCell, LayoutHelper.createLinear(-1, -2));
      TLRPC.TL_account_authorizationForm var27 = this.currentForm;
      int var12;
      if (var27 != null) {
         int var7 = var27.required_types.size();
         ArrayList var29 = new ArrayList();
         ArrayList var8 = new ArrayList();
         int var9 = 0;
         boolean var10 = false;
         int var11 = 0;
         var12 = 0;

         int var13;
         TLRPC.SecureRequiredType var17;
         boolean var23;
         TLRPC.SecureRequiredType var25;
         TLRPC.TL_secureRequiredType var26;
         TLRPC.SecureValueType var28;
         TLRPC.TL_secureRequiredTypeOneOf var30;
         TLRPC.TL_secureRequiredType var34;
         int var35;
         for(var23 = false; var9 < var7; ++var9) {
            boolean var14;
            int var15;
            boolean var16;
            label224: {
               var25 = (TLRPC.SecureRequiredType)this.currentForm.required_types.get(var9);
               if (var25 instanceof TLRPC.TL_secureRequiredType) {
                  var26 = (TLRPC.TL_secureRequiredType)var25;
                  if (!this.isPersonalDocument(var26.type)) {
                     if (this.isAddressDocument(var26.type)) {
                        var8.add(var26);
                        var13 = var12 + 1;
                        var14 = var10;
                        var15 = var11;
                        var16 = var23;
                     } else {
                        var28 = var26.type;
                        if (var28 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                           var14 = true;
                           var15 = var11;
                           var13 = var12;
                           var16 = var23;
                        } else {
                           var14 = var10;
                           var15 = var11;
                           var13 = var12;
                           var16 = var23;
                           if (var28 instanceof TLRPC.TL_secureValueTypeAddress) {
                              var16 = true;
                              var13 = var12;
                              var15 = var11;
                              var14 = var10;
                           }
                        }
                     }
                     break label224;
                  }

                  var29.add(var26);
               } else {
                  var14 = var10;
                  var15 = var11;
                  var13 = var12;
                  var16 = var23;
                  if (!(var25 instanceof TLRPC.TL_secureRequiredTypeOneOf)) {
                     break label224;
                  }

                  var30 = (TLRPC.TL_secureRequiredTypeOneOf)var25;
                  if (var30.types.isEmpty()) {
                     var14 = var10;
                     var15 = var11;
                     var13 = var12;
                     var16 = var23;
                     break label224;
                  }

                  var17 = (TLRPC.SecureRequiredType)var30.types.get(0);
                  if (!(var17 instanceof TLRPC.TL_secureRequiredType)) {
                     var14 = var10;
                     var15 = var11;
                     var13 = var12;
                     var16 = var23;
                     break label224;
                  }

                  var34 = (TLRPC.TL_secureRequiredType)var17;
                  if (!this.isPersonalDocument(var34.type)) {
                     var14 = var10;
                     var15 = var11;
                     var13 = var12;
                     var16 = var23;
                     if (this.isAddressDocument(var34.type)) {
                        var13 = var30.types.size();

                        for(var35 = 0; var35 < var13; ++var35) {
                           var17 = (TLRPC.SecureRequiredType)var30.types.get(var35);
                           if (var17 instanceof TLRPC.TL_secureRequiredType) {
                              var8.add((TLRPC.TL_secureRequiredType)var17);
                           }
                        }

                        ++var12;
                        continue;
                     }
                     break label224;
                  }

                  var35 = var30.types.size();

                  for(var13 = 0; var13 < var35; ++var13) {
                     var17 = (TLRPC.SecureRequiredType)var30.types.get(var13);
                     if (var17 instanceof TLRPC.TL_secureRequiredType) {
                        var29.add((TLRPC.TL_secureRequiredType)var17);
                     }
                  }
               }

               var15 = var11 + 1;
               var14 = var10;
               var13 = var12;
               var16 = var23;
            }

            var10 = var14;
            var11 = var15;
            var12 = var13;
            var23 = var16;
         }

         if (var10 && var11 <= 1) {
            var10 = false;
         } else {
            var10 = true;
         }

         if (var23 && var12 <= 1) {
            var23 = false;
         } else {
            var23 = true;
         }

         var11 = 0;

         for(var12 = var7; var11 < var12; ++var11) {
            var25 = (TLRPC.SecureRequiredType)this.currentForm.required_types.get(var11);
            boolean var18;
            ArrayList var32;
            if (var25 instanceof TLRPC.TL_secureRequiredType) {
               label260: {
                  var34 = (TLRPC.TL_secureRequiredType)var25;
                  var28 = var34.type;
                  if (!(var28 instanceof TLRPC.TL_secureValueTypePhone) && !(var28 instanceof TLRPC.TL_secureValueTypeEmail)) {
                     label175: {
                        if (var28 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                           if (!var10) {
                              var32 = var29;
                              break label175;
                           }
                        } else {
                           if (!(var28 instanceof TLRPC.TL_secureValueTypeAddress)) {
                              if (var10 && this.isPersonalDocument(var28)) {
                                 var32 = new ArrayList();
                                 var32.add(var34);
                                 var34 = new TLRPC.TL_secureRequiredType();
                                 var34.type = new TLRPC.TL_secureValueTypePersonalDetails();
                              } else {
                                 if (!var23 || !this.isAddressDocument(var34.type)) {
                                    continue;
                                 }

                                 var32 = new ArrayList();
                                 var32.add(var34);
                                 var34 = new TLRPC.TL_secureRequiredType();
                                 var34.type = new TLRPC.TL_secureValueTypeAddress();
                              }

                              var18 = true;
                              break label260;
                           }

                           if (!var23) {
                              var32 = var8;
                              break label175;
                           }
                        }

                        var32 = null;
                     }
                  } else {
                     var32 = null;
                  }

                  var18 = false;
               }
            } else {
               if (!(var25 instanceof TLRPC.TL_secureRequiredTypeOneOf)) {
                  continue;
               }

               var30 = (TLRPC.TL_secureRequiredTypeOneOf)var25;
               if (var30.types.isEmpty()) {
                  continue;
               }

               var17 = (TLRPC.SecureRequiredType)var30.types.get(0);
               if (!(var17 instanceof TLRPC.TL_secureRequiredType)) {
                  continue;
               }

               var34 = (TLRPC.TL_secureRequiredType)var17;
               if ((!var10 || !this.isPersonalDocument(var34.type)) && (!var23 || !this.isAddressDocument(var34.type))) {
                  continue;
               }

               ArrayList var19 = new ArrayList();
               var13 = var30.types.size();

               for(var35 = 0; var35 < var13; ++var35) {
                  TLRPC.SecureRequiredType var20 = (TLRPC.SecureRequiredType)var30.types.get(var35);
                  if (var20 instanceof TLRPC.TL_secureRequiredType) {
                     var19.add((TLRPC.TL_secureRequiredType)var20);
                  }
               }

               if (this.isPersonalDocument(var34.type)) {
                  var26 = new TLRPC.TL_secureRequiredType();
                  var26.type = new TLRPC.TL_secureValueTypePersonalDetails();
               } else {
                  var26 = new TLRPC.TL_secureRequiredType();
                  var26.type = new TLRPC.TL_secureValueTypeAddress();
               }

               var18 = true;
               var34 = var26;
               var32 = var19;
            }

            boolean var21;
            if (var11 == var12 - 1) {
               var21 = true;
            } else {
               var21 = false;
            }

            this.addField(var1, var34, var32, var18, var21);
         }
      }

      if (var3 != null) {
         this.bottomCell = new TextInfoPrivacyCell(var1);
         this.bottomCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.bottomCell.setLinkTextColorKey("windowBackgroundWhiteGrayText4");
         if (!TextUtils.isEmpty(this.currentForm.privacy_policy_url)) {
            String var31 = LocaleController.formatString("PassportPolicy", 2131560311, UserObject.getFirstName(var3), var3.username);
            SpannableStringBuilder var33 = new SpannableStringBuilder(var31);
            var2 = var31.indexOf(42);
            var12 = var31.lastIndexOf(42);
            if (var2 != -1 && var12 != -1) {
               this.bottomCell.getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
               var33.replace(var12, var12 + 1, "");
               var33.replace(var2, var2 + 1, "");
               var33.setSpan(new PassportActivity.LinkSpan(), var2, var12 - 1, 33);
            }

            this.bottomCell.setText(var33);
         } else {
            this.bottomCell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PassportNoPolicy", 2131560299, UserObject.getFirstName(var3), var3.username)));
         }

         this.bottomCell.getTextView().setHighlightColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
         this.bottomCell.getTextView().setGravity(1);
         this.linearLayout2.addView(this.bottomCell, LayoutHelper.createLinear(-1, -2));
      }

      this.bottomLayout = new FrameLayout(var1);
      this.bottomLayout.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("passport_authorizeBackground"), Theme.getColor("passport_authorizeBackgroundSelected")));
      var4.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
      this.bottomLayout.setOnClickListener(new _$$Lambda$PassportActivity$YcmK_1FiSvvv_xWa8i2cDGfFT0Y(this));
      this.acceptTextView = new TextView(var1);
      this.acceptTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      this.acceptTextView.setCompoundDrawablesWithIntrinsicBounds(2131165298, 0, 0, 0);
      this.acceptTextView.setTextColor(Theme.getColor("passport_authorizeText"));
      this.acceptTextView.setText(LocaleController.getString("PassportAuthorize", 2131560193));
      this.acceptTextView.setTextSize(1, 14.0F);
      this.acceptTextView.setGravity(17);
      this.acceptTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.bottomLayout.addView(this.acceptTextView, LayoutHelper.createFrame(-2, -1, 17));
      this.progressViewButton = new ContextProgressView(var1, 0);
      this.progressViewButton.setVisibility(4);
      this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0F));
      View var22 = new View(var1);
      var22.setBackgroundResource(2131165408);
      var4.addView(var22, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
   }

   private PassportActivity.EncryptionResult createSecureDocument(String var1) {
      byte[] var2 = new byte[(int)(new File(var1)).length()];

      RandomAccessFile var7;
      label30: {
         RandomAccessFile var3;
         try {
            var3 = new RandomAccessFile(var1, "rws");
         } catch (Exception var6) {
            var7 = null;
            break label30;
         }

         try {
            var3.readFully(var2);
         } catch (Exception var5) {
            var7 = var3;
            break label30;
         }

         var7 = var3;
      }

      PassportActivity.EncryptionResult var8 = this.encryptData(var2);

      try {
         var7.seek(0L);
         var7.write(var8.encryptedData);
         var7.close();
      } catch (Exception var4) {
      }

      return var8;
   }

   private String decryptData(byte[] var1, byte[] var2, byte[] var3) {
      if (var1 != null && var2 != null && var2.length == 32 && var3 != null && var3.length == 32) {
         byte[] var4 = Utilities.computeSHA512(var2, var3);
         var2 = new byte[32];
         System.arraycopy(var4, 0, var2, 0, 32);
         byte[] var5 = new byte[16];
         System.arraycopy(var4, 32, var5, 0, 16);
         var4 = new byte[var1.length];
         System.arraycopy(var1, 0, var4, 0, var1.length);
         Utilities.aesCbcEncryptionByteArraySafe(var4, var2, var5, 0, var4.length, 0, 0);
         if (!Arrays.equals(Utilities.computeSHA256(var4), var3)) {
            return null;
         } else {
            int var6 = var4[0] & 255;
            return new String(var4, var6, var4.length - var6);
         }
      } else {
         return null;
      }
   }

   private byte[] decryptSecret(byte[] var1, byte[] var2) {
      if (var1 != null && var1.length == 32) {
         byte[] var3 = new byte[32];
         System.arraycopy(var2, 0, var3, 0, 32);
         byte[] var4 = new byte[16];
         System.arraycopy(var2, 32, var4, 0, 16);
         var2 = new byte[32];
         System.arraycopy(var1, 0, var2, 0, 32);
         Utilities.aesCbcEncryptionByteArraySafe(var2, var3, var4, 0, var2.length, 0, 0);
         return var2;
      } else {
         return null;
      }
   }

   private byte[] decryptValueSecret(byte[] var1, byte[] var2) {
      if (var1 != null && var1.length == 32 && var2 != null && var2.length == 32) {
         byte[] var3 = new byte[32];
         System.arraycopy(this.saltedPassword, 0, var3, 0, 32);
         byte[] var4 = new byte[16];
         System.arraycopy(this.saltedPassword, 32, var4, 0, 16);
         byte[] var5 = new byte[32];
         System.arraycopy(this.secureSecret, 0, var5, 0, 32);
         Utilities.aesCbcEncryptionByteArraySafe(var5, var3, var4, 0, var5.length, 0, 0);
         if (!checkSecret(var5, (Long)null)) {
            return null;
         } else {
            var4 = Utilities.computeSHA512(var5, var2);
            var3 = new byte[32];
            System.arraycopy(var4, 0, var3, 0, 32);
            var2 = new byte[16];
            System.arraycopy(var4, 32, var2, 0, 16);
            var4 = new byte[32];
            System.arraycopy(var1, 0, var4, 0, 32);
            Utilities.aesCbcEncryptionByteArraySafe(var4, var3, var2, 0, var4.length, 0, 0);
            return var4;
         }
      } else {
         return null;
      }
   }

   private void deleteValueInternal(TLRPC.TL_secureRequiredType var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, boolean var4, Runnable var5, PassportActivity.ErrorRunnable var6, boolean var7) {
      if (var1 != null) {
         TLRPC.TL_account_deleteSecureValue var8 = new TLRPC.TL_account_deleteSecureValue();
         if (var7 && var2 != null) {
            var8.types.add(var2.type);
         } else {
            if (var4) {
               var8.types.add(var1.type);
            }

            if (var2 != null) {
               var8.types.add(var2.type);
            }
         }

         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var8, new _$$Lambda$PassportActivity$yqjIwJxQHVRYAcsusKtM70xYOHo(this, var6, var7, var2, var1, var4, var3, var5));
      }
   }

   private PassportActivity.EncryptionResult encryptData(byte[] var1) {
      byte[] var2 = this.getRandomSecret();

      int var3;
      for(var3 = Utilities.random.nextInt(208) + 32; (var1.length + var3) % 16 != 0; ++var3) {
      }

      byte[] var4 = new byte[var3];
      Utilities.random.nextBytes(var4);
      var4[0] = (byte)((byte)var3);
      byte[] var5 = new byte[var1.length + var3];
      System.arraycopy(var4, 0, var5, 0, var3);
      System.arraycopy(var1, 0, var5, var3, var1.length);
      var4 = Utilities.computeSHA256(var5);
      byte[] var6 = Utilities.computeSHA512(var2, var4);
      byte[] var7 = new byte[32];
      System.arraycopy(var6, 0, var7, 0, 32);
      var1 = new byte[16];
      System.arraycopy(var6, 32, var1, 0, 16);
      Utilities.aesCbcEncryptionByteArraySafe(var5, var7, var1, 0, var5.length, 0, 1);
      var6 = new byte[32];
      System.arraycopy(this.saltedPassword, 0, var6, 0, 32);
      byte[] var8 = new byte[16];
      System.arraycopy(this.saltedPassword, 32, var8, 0, 16);
      byte[] var9 = new byte[32];
      System.arraycopy(this.secureSecret, 0, var9, 0, 32);
      Utilities.aesCbcEncryptionByteArraySafe(var9, var6, var8, 0, var9.length, 0, 0);
      var8 = Utilities.computeSHA512(var9, var4);
      var6 = new byte[32];
      System.arraycopy(var8, 0, var6, 0, 32);
      var9 = new byte[16];
      System.arraycopy(var8, 32, var9, 0, 16);
      var8 = new byte[32];
      System.arraycopy(var2, 0, var8, 0, 32);
      Utilities.aesCbcEncryptionByteArraySafe(var8, var6, var9, 0, var8.length, 0, 1);
      return new PassportActivity.EncryptionResult(var5, var8, var2, var4, var7, var1);
   }

   private void fillInitialValues() {
      if (this.initialValues == null) {
         this.initialValues = this.getCurrentValues();
      }
   }

   private void fillNextCodeParams(Bundle var1, TLRPC.TL_auth_sentCode var2, boolean var3) {
      var1.putString("phoneHash", var2.phone_code_hash);
      TLRPC.auth_CodeType var4 = var2.next_type;
      if (var4 instanceof TLRPC.TL_auth_codeTypeCall) {
         var1.putInt("nextType", 4);
      } else if (var4 instanceof TLRPC.TL_auth_codeTypeFlashCall) {
         var1.putInt("nextType", 3);
      } else if (var4 instanceof TLRPC.TL_auth_codeTypeSms) {
         var1.putInt("nextType", 2);
      }

      if (var2.timeout == 0) {
         var2.timeout = 60;
      }

      var1.putInt("timeout", var2.timeout * 1000);
      TLRPC.auth_SentCodeType var5 = var2.type;
      if (var5 instanceof TLRPC.TL_auth_sentCodeTypeCall) {
         var1.putInt("type", 4);
         var1.putInt("length", var2.type.length);
         this.setPage(2, var3, var1);
      } else if (var5 instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
         var1.putInt("type", 3);
         var1.putString("pattern", var2.type.pattern);
         this.setPage(1, var3, var1);
      } else if (var5 instanceof TLRPC.TL_auth_sentCodeTypeSms) {
         var1.putInt("type", 2);
         var1.putInt("length", var2.type.length);
         this.setPage(0, var3, var1);
      }

   }

   private String getCurrentValues() {
      StringBuilder var1 = new StringBuilder();
      byte var2 = 0;
      int var3 = 0;

      while(true) {
         EditTextBoldCursor[] var4 = this.inputFields;
         if (var3 >= var4.length) {
            if (this.inputExtraFields != null) {
               var3 = 0;

               while(true) {
                  var4 = this.inputExtraFields;
                  if (var3 >= var4.length) {
                     break;
                  }

                  var1.append(var4[var3].getText());
                  var1.append(",");
                  ++var3;
               }
            }

            int var5 = this.documents.size();

            for(var3 = 0; var3 < var5; ++var3) {
               var1.append(((SecureDocument)this.documents.get(var3)).secureFile.id);
            }

            SecureDocument var6 = this.frontDocument;
            if (var6 != null) {
               var1.append(var6.secureFile.id);
            }

            var6 = this.reverseDocument;
            if (var6 != null) {
               var1.append(var6.secureFile.id);
            }

            var6 = this.selfieDocument;
            if (var6 != null) {
               var1.append(var6.secureFile.id);
            }

            var5 = this.translationDocuments.size();

            for(var3 = var2; var3 < var5; ++var3) {
               var1.append(((SecureDocument)this.translationDocuments.get(var3)).secureFile.id);
            }

            return var1.toString();
         }

         var1.append(var4[var3].getText());
         var1.append(",");
         ++var3;
      }
   }

   private String getDocumentHash(SecureDocument var1) {
      if (var1 != null) {
         TLRPC.TL_secureFile var2 = var1.secureFile;
         if (var2 != null) {
            byte[] var4 = var2.file_hash;
            if (var4 != null) {
               return Base64.encodeToString(var4, 2);
            }
         }

         byte[] var3 = var1.fileHash;
         if (var3 != null) {
            return Base64.encodeToString(var3, 2);
         }
      }

      return "";
   }

   private String getErrorsString(HashMap var1, HashMap var2) {
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < 2; ++var4) {
         HashMap var5;
         if (var4 == 0) {
            var5 = var1;
         } else {
            var5 = var2;
         }

         String var7;
         if (var5 != null) {
            for(Iterator var6 = var5.entrySet().iterator(); var6.hasNext(); var3.append(var7)) {
               var7 = (String)((Entry)var6.next()).getValue();
               String var8 = var7;
               if (var3.length() > 0) {
                  var3.append(", ");
                  var8 = var7.toLowerCase();
               }

               var7 = var8;
               if (var8.endsWith(".")) {
                  var7 = var8.substring(0, var8.length() - 1);
               }
            }
         }
      }

      if (var3.length() > 0) {
         var3.append('.');
      }

      return var3.toString();
   }

   private int getFieldCost(String var1) {
      byte var2;
      label79: {
         switch(var1.hashCode()) {
         case -2006252145:
            if (var1.equals("residence_country_code")) {
               var2 = 9;
               break label79;
            }
            break;
         case -1537298398:
            if (var1.equals("last_name_native")) {
               var2 = 5;
               break label79;
            }
            break;
         case -1249512767:
            if (var1.equals("gender")) {
               var2 = 7;
               break label79;
            }
            break;
         case -796150911:
            if (var1.equals("street_line1")) {
               var2 = 12;
               break label79;
            }
            break;
         case -796150910:
            if (var1.equals("street_line2")) {
               var2 = 13;
               break label79;
            }
            break;
         case -160985414:
            if (var1.equals("first_name")) {
               var2 = 0;
               break label79;
            }
            break;
         case 3053931:
            if (var1.equals("city")) {
               var2 = 15;
               break label79;
            }
            break;
         case 109757585:
            if (var1.equals("state")) {
               var2 = 16;
               break label79;
            }
            break;
         case 421072629:
            if (var1.equals("middle_name")) {
               var2 = 2;
               break label79;
            }
            break;
         case 451516732:
            if (var1.equals("first_name_native")) {
               var2 = 1;
               break label79;
            }
            break;
         case 475919162:
            if (var1.equals("expiry_date")) {
               var2 = 11;
               break label79;
            }
            break;
         case 506677093:
            if (var1.equals("document_no")) {
               var2 = 10;
               break label79;
            }
            break;
         case 1168724782:
            if (var1.equals("birth_date")) {
               var2 = 6;
               break label79;
            }
            break;
         case 1181577377:
            if (var1.equals("middle_name_native")) {
               var2 = 3;
               break label79;
            }
            break;
         case 1481071862:
            if (var1.equals("country_code")) {
               var2 = 8;
               break label79;
            }
            break;
         case 2002465324:
            if (var1.equals("post_code")) {
               var2 = 14;
               break label79;
            }
            break;
         case 2013122196:
            if (var1.equals("last_name")) {
               var2 = 4;
               break label79;
            }
         }

         var2 = -1;
      }

      switch(var2) {
      case 0:
      case 1:
         return 20;
      case 2:
      case 3:
         return 21;
      case 4:
      case 5:
         return 22;
      case 6:
         return 23;
      case 7:
         return 24;
      case 8:
         return 25;
      case 9:
         return 26;
      case 10:
         return 27;
      case 11:
         return 28;
      case 12:
         return 29;
      case 13:
         return 30;
      case 14:
         return 31;
      case 15:
         return 32;
      case 16:
         return 33;
      default:
         return 100;
      }
   }

   private int getMaxSelectedDocuments() {
      int var1 = this.uploadingFileType;
      if (var1 == 0) {
         var1 = this.documents.size();
      } else {
         if (var1 != 4) {
            return 1;
         }

         var1 = this.translationDocuments.size();
      }

      return 20 - var1;
   }

   private String getNameForType(TLRPC.SecureValueType var1) {
      if (var1 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
         return "personal_details";
      } else if (var1 instanceof TLRPC.TL_secureValueTypePassport) {
         return "passport";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
         return "internal_passport";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
         return "driver_license";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
         return "identity_card";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
         return "utility_bill";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeAddress) {
         return "address";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeBankStatement) {
         return "bank_statement";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
         return "rental_agreement";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
         return "temporary_registration";
      } else if (var1 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
         return "passport_registration";
      } else if (var1 instanceof TLRPC.TL_secureValueTypeEmail) {
         return "email";
      } else {
         return var1 instanceof TLRPC.TL_secureValueTypePhone ? "phone" : "";
      }
   }

   private byte[] getRandomSecret() {
      byte[] var1 = new byte[32];
      Utilities.random.nextBytes(var1);
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var1.length; ++var2) {
         var3 += var1[var2] & 255;
      }

      var2 = var3 % 255;
      if (var2 != 239) {
         int var4 = Utilities.random.nextInt(32);
         var3 = (var1[var4] & 255) + (239 - var2);
         var2 = var3;
         if (var3 < 255) {
            var2 = var3 + 255;
         }

         var1[var4] = (byte)((byte)(var2 % 255));
      }

      return var1;
   }

   private SecureDocumentKey getSecureDocumentKey(byte[] var1, byte[] var2) {
      byte[] var3 = Utilities.computeSHA512(this.decryptValueSecret(var1, var2), var2);
      var1 = new byte[32];
      System.arraycopy(var3, 0, var1, 0, 32);
      var2 = new byte[16];
      System.arraycopy(var3, 32, var2, 0, 16);
      return new SecureDocumentKey(var1, var2);
   }

   private String getTextForType(TLRPC.SecureValueType var1) {
      if (var1 instanceof TLRPC.TL_secureValueTypePassport) {
         return LocaleController.getString("ActionBotDocumentPassport", 2131558504);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
         return LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
         return LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
         return LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeBankStatement) {
         return LocaleController.getString("ActionBotDocumentBankStatement", 2131558498);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
         return LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
         return LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503);
      } else if (var1 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
         return LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505);
      } else if (var1 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
         return LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508);
      } else if (var1 instanceof TLRPC.TL_secureValueTypePhone) {
         return LocaleController.getString("ActionBotDocumentPhone", 2131558506);
      } else {
         return var1 instanceof TLRPC.TL_secureValueTypeEmail ? LocaleController.getString("ActionBotDocumentEmail", 2131558500) : "";
      }
   }

   private String getTranslitString(String var1) {
      return LocaleController.getInstance().getTranslitString(var1, true);
   }

   private TLRPC.TL_secureValue getValueByType(TLRPC.TL_secureRequiredType var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         int var3 = this.currentForm.values.size();
         byte var4 = 0;

         for(int var5 = 0; var5 < var3; ++var5) {
            TLRPC.TL_secureValue var6 = (TLRPC.TL_secureValue)this.currentForm.values.get(var5);
            if (var1.type.getClass() == var6.type.getClass()) {
               if (var2) {
                  if (var1.selfie_required && !(var6.selfie instanceof TLRPC.TL_secureFile)) {
                     return null;
                  }

                  if (var1.translation_required && var6.translation.isEmpty()) {
                     return null;
                  }

                  if (this.isAddressDocument(var1.type) && var6.files.isEmpty()) {
                     return null;
                  }

                  if (this.isPersonalDocument(var1.type) && !(var6.front_side instanceof TLRPC.TL_secureFile)) {
                     return null;
                  }

                  TLRPC.SecureValueType var7 = var1.type;
                  if ((var7 instanceof TLRPC.TL_secureValueTypeDriverLicense || var7 instanceof TLRPC.TL_secureValueTypeIdentityCard) && !(var6.reverse_side instanceof TLRPC.TL_secureFile)) {
                     return null;
                  }

                  var7 = var1.type;
                  if (var7 instanceof TLRPC.TL_secureValueTypePersonalDetails || var7 instanceof TLRPC.TL_secureValueTypeAddress) {
                     String[] var10;
                     if (var1.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                        if (var1.native_names) {
                           var10 = new String[]{"first_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"};
                        } else {
                           var10 = new String[]{"first_name", "last_name", "birth_date", "gender", "country_code", "residence_country_code"};
                        }
                     } else {
                        var10 = new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                     }

                     boolean var10001;
                     JSONObject var11;
                     try {
                        var11 = new JSONObject(this.decryptData(var6.data.data, this.decryptValueSecret(var6.data.secret, var6.data.data_hash), var6.data.data_hash));
                     } catch (Throwable var9) {
                        var10001 = false;
                        return null;
                     }

                     var5 = var4;

                     while(true) {
                        try {
                           if (var5 >= var10.length) {
                              break;
                           }

                           if (!var11.has(var10[var5])) {
                              return null;
                           }

                           var2 = TextUtils.isEmpty(var11.getString(var10[var5]));
                        } catch (Throwable var8) {
                           var10001 = false;
                           return null;
                        }

                        if (var2) {
                           return null;
                        }

                        ++var5;
                     }
                  }
               }

               return var6;
            }
         }

         return null;
      }
   }

   private PassportActivity.TextDetailSecureCell getViewByType(TLRPC.TL_secureRequiredType var1) {
      PassportActivity.TextDetailSecureCell var2 = (PassportActivity.TextDetailSecureCell)this.typesViews.get(var1);
      PassportActivity.TextDetailSecureCell var3 = var2;
      if (var2 == null) {
         var1 = (TLRPC.TL_secureRequiredType)this.documentsToTypesLink.get(var1);
         var3 = var2;
         if (var1 != null) {
            var3 = (PassportActivity.TextDetailSecureCell)this.typesViews.get(var1);
         }
      }

      return var3;
   }

   private boolean hasNotValueForType(Class var1) {
      int var2 = this.currentForm.values.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (((TLRPC.TL_secureValue)this.currentForm.values.get(var3)).type.getClass() == var1) {
            return false;
         }
      }

      return true;
   }

   private boolean hasUnfilledValues() {
      boolean var1;
      if (!this.hasNotValueForType(TLRPC.TL_secureValueTypePhone.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeEmail.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypePersonalDetails.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypePassport.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeInternalPassport.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeIdentityCard.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeDriverLicense.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeAddress.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeUtilityBill.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypePassportRegistration.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeTemporaryRegistration.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeBankStatement.class) && !this.hasNotValueForType(TLRPC.TL_secureValueTypeRentalAgreement.class)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean isAddressDocument(TLRPC.SecureValueType var1) {
      boolean var2;
      if (!(var1 instanceof TLRPC.TL_secureValueTypeUtilityBill) && !(var1 instanceof TLRPC.TL_secureValueTypeBankStatement) && !(var1 instanceof TLRPC.TL_secureValueTypePassportRegistration) && !(var1 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) && !(var1 instanceof TLRPC.TL_secureValueTypeRentalAgreement)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean isHasNotAnyChanges() {
      String var1 = this.initialValues;
      boolean var2;
      if (var1 != null && !var1.equals(this.getCurrentValues())) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean isPersonalDocument(TLRPC.SecureValueType var1) {
      boolean var2;
      if (!(var1 instanceof TLRPC.TL_secureValueTypeDriverLicense) && !(var1 instanceof TLRPC.TL_secureValueTypePassport) && !(var1 instanceof TLRPC.TL_secureValueTypeInternalPassport) && !(var1 instanceof TLRPC.TL_secureValueTypeIdentityCard)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   // $FF: synthetic method
   static void lambda$createDocumentDeleteAlert$39(boolean[] var0, View var1) {
      if (var1.isEnabled()) {
         CheckBoxCell var2 = (CheckBoxCell)var1;
         var0[0] ^= true;
         var2.setChecked(var0[0], true);
      }
   }

   // $FF: synthetic method
   static void lambda$new$1(TLObject var0, TLRPC.TL_error var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$fK7dwz8bOfjtlF5bGs64fAkaNiU(var0));
   }

   // $FF: synthetic method
   static void lambda$null$0(TLObject var0) {
      if (var0 instanceof TLRPC.TL_help_passportConfig) {
         TLRPC.TL_help_passportConfig var1 = (TLRPC.TL_help_passportConfig)var0;
         SharedConfig.setPassportConfig(var1.countries_langs.data, var1.hash);
      } else {
         SharedConfig.getCountryLangs();
      }

   }

   private void loadPasswordInfo() {
      TLRPC.TL_account_getPassword var1 = new TLRPC.TL_account_getPassword();
      int var2 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI(this));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var2, super.classGuid);
   }

   private void onFieldError(View var1) {
      if (var1 != null) {
         Vibrator var2 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
         if (var2 != null) {
            var2.vibrate(200L);
         }

         AndroidUtilities.shakeView(var1, 2.0F, 0);
         this.scrollToField(var1);
      }
   }

   private void onPasscodeError(boolean var1) {
      if (this.getParentActivity() != null) {
         Vibrator var2 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
         if (var2 != null) {
            var2.vibrate(200L);
         }

         if (var1) {
            this.inputFields[0].setText("");
         }

         AndroidUtilities.shakeView(this.inputFields[0], 2.0F, 0);
      }
   }

   private void onPasswordDone(boolean var1) {
      String var2;
      if (var1) {
         var2 = null;
      } else {
         var2 = this.inputFields[0].getText().toString();
         if (TextUtils.isEmpty(var2)) {
            this.onPasscodeError(false);
            return;
         }

         this.showEditDoneProgress(true, true);
      }

      Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$aT2vRId8R9seAyB5eJdDvQVsURw(this, var1, var2));
   }

   private void openAddDocumentAlert() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      if (this.hasNotValueForType(TLRPC.TL_secureValueTypePhone.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentPhone", 2131558506));
         var2.add(TLRPC.TL_secureValueTypePhone.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeEmail.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentEmail", 2131558500));
         var2.add(TLRPC.TL_secureValueTypeEmail.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypePersonalDetails.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentIdentity", 2131558501));
         var2.add(TLRPC.TL_secureValueTypePersonalDetails.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypePassport.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentPassport", 2131558504));
         var2.add(TLRPC.TL_secureValueTypePassport.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeInternalPassport.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentInternalPassport", 2131558503));
         var2.add(TLRPC.TL_secureValueTypeInternalPassport.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypePassportRegistration.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentPassportRegistration", 2131558505));
         var2.add(TLRPC.TL_secureValueTypePassportRegistration.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeTemporaryRegistration.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentTemporaryRegistration", 2131558508));
         var2.add(TLRPC.TL_secureValueTypeTemporaryRegistration.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeIdentityCard.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentIdentityCard", 2131558502));
         var2.add(TLRPC.TL_secureValueTypeIdentityCard.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeDriverLicense.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentDriverLicence", 2131558499));
         var2.add(TLRPC.TL_secureValueTypeDriverLicense.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeAddress.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentAddress", 2131558497));
         var2.add(TLRPC.TL_secureValueTypeAddress.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeUtilityBill.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentUtilityBill", 2131558509));
         var2.add(TLRPC.TL_secureValueTypeUtilityBill.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeBankStatement.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentBankStatement", 2131558498));
         var2.add(TLRPC.TL_secureValueTypeBankStatement.class);
      }

      if (this.hasNotValueForType(TLRPC.TL_secureValueTypeRentalAgreement.class)) {
         var1.add(LocaleController.getString("ActionBotDocumentRentalAgreement", 2131558507));
         var2.add(TLRPC.TL_secureValueTypeRentalAgreement.class);
      }

      if (this.getParentActivity() != null && !var1.isEmpty()) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(LocaleController.getString("PassportNoDocumentsAdd", 2131560296));
         var3.setItems((CharSequence[])var1.toArray(new CharSequence[0]), new _$$Lambda$PassportActivity$_OkER2xuwjfE0_6Y_fzH2uLkAhM(this, var2));
         this.showDialog(var3.create());
      }

   }

   private void openAttachMenu() {
      if (this.getParentActivity() != null) {
         int var1 = this.uploadingFileType;
         boolean var2 = false;
         if (var1 == 0 && this.documents.size() >= 20) {
            this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("PassportUploadMaxReached", 2131560341, LocaleController.formatPluralString("Files", 20)));
         } else {
            this.createChatAttachView();
            ChatAttachAlert var3 = this.chatAttachAlert;
            if (this.uploadingFileType == 1) {
               var2 = true;
            }

            var3.setOpenWithFrontFaceCamera(var2);
            this.chatAttachAlert.setMaxSelectedPhotos(this.getMaxSelectedDocuments());
            this.chatAttachAlert.loadGalleryPhotos();
            var1 = VERSION.SDK_INT;
            if (var1 == 21 || var1 == 22) {
               AndroidUtilities.hideKeyboard(super.fragmentView.findFocus());
            }

            this.chatAttachAlert.init();
            this.showDialog(this.chatAttachAlert);
         }
      }
   }

   private void openTypeActivity(TLRPC.TL_secureRequiredType var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, final boolean var4) {
      final int var5;
      if (var3 != null) {
         var5 = var3.size();
      } else {
         var5 = 0;
      }

      final TLRPC.SecureValueType var6 = var1.type;
      TLRPC.SecureValueType var7;
      if (var2 != null) {
         var7 = var2.type;
      } else {
         var7 = null;
      }

      byte var8;
      if (var6 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
         var8 = 1;
      } else if (var6 instanceof TLRPC.TL_secureValueTypeAddress) {
         var8 = 2;
      } else if (var6 instanceof TLRPC.TL_secureValueTypePhone) {
         var8 = 3;
      } else if (var6 instanceof TLRPC.TL_secureValueTypeEmail) {
         var8 = 4;
      } else {
         var8 = -1;
      }

      if (var8 != -1) {
         HashMap var9;
         if (!var4) {
            var9 = (HashMap)this.errorsMap.get(this.getNameForType(var6));
         } else {
            var9 = null;
         }

         HashMap var10 = (HashMap)this.errorsMap.get(this.getNameForType(var7));
         TLRPC.TL_secureValue var11 = this.getValueByType(var1, false);
         TLRPC.TL_secureValue var12 = this.getValueByType(var2, false);
         TLRPC.TL_account_authorizationForm var13 = this.currentForm;
         TLRPC.TL_account_password var14 = this.currentPassword;
         HashMap var15 = (HashMap)this.typesValues.get(var1);
         HashMap var17;
         if (var2 != null) {
            var17 = (HashMap)this.typesValues.get(var2);
         } else {
            var17 = null;
         }

         PassportActivity var16 = new PassportActivity(var8, var13, var14, var1, var11, var2, var12, var15, var17);
         var16.delegate = new PassportActivity.PassportActivityDelegate() {
            private TLRPC.InputSecureFile getInputSecureFile(SecureDocument var1) {
               if (var1.inputFile != null) {
                  TLRPC.TL_inputSecureFileUploaded var5x = new TLRPC.TL_inputSecureFileUploaded();
                  TLRPC.TL_inputFile var3 = var1.inputFile;
                  var5x.id = var3.id;
                  var5x.parts = var3.parts;
                  var5x.md5_checksum = var3.md5_checksum;
                  var5x.file_hash = var1.fileHash;
                  var5x.secret = var1.fileSecret;
                  return var5x;
               } else {
                  TLRPC.TL_inputSecureFile var2 = new TLRPC.TL_inputSecureFile();
                  TLRPC.TL_secureFile var4x = var1.secureFile;
                  var2.id = var4x.id;
                  var2.access_hash = var4x.access_hash;
                  return var2;
               }
            }

            private void renameFile(SecureDocument var1, TLRPC.TL_secureFile var2) {
               File var3 = FileLoader.getPathToAttach(var1);
               StringBuilder var4x = new StringBuilder();
               var4x.append(var1.secureFile.dc_id);
               var4x.append("_");
               var4x.append(var1.secureFile.id);
               String var8 = var4x.toString();
               File var6x = FileLoader.getPathToAttach(var2);
               StringBuilder var5x = new StringBuilder();
               var5x.append(var2.dc_id);
               var5x.append("_");
               var5x.append(var2.id);
               String var7 = var5x.toString();
               var3.renameTo(var6x);
               ImageLoader.getInstance().replaceImageInCache(var8, var7, (ImageLocation)null, false);
            }

            public void deleteValue(TLRPC.TL_secureRequiredType var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, boolean var4x, Runnable var5x, PassportActivity.ErrorRunnable var6x) {
               PassportActivity.this.deleteValueInternal(var1, var2, var3, var4x, var5x, var6x, var4);
            }

            public SecureDocument saveFile(TLRPC.TL_secureFile var1) {
               StringBuilder var2 = new StringBuilder();
               var2.append(FileLoader.getDirectory(4));
               var2.append("/");
               var2.append(var1.dc_id);
               var2.append("_");
               var2.append(var1.id);
               var2.append(".jpg");
               String var4x = var2.toString();
               PassportActivity.EncryptionResult var3 = PassportActivity.this.createSecureDocument(var4x);
               return new SecureDocument(var3.secureDocumentKey, var1, var4x, var3.fileHash, var3.fileSecret);
            }

            public void saveValue(final TLRPC.TL_secureRequiredType var1, final String var2, final String var3, final TLRPC.TL_secureRequiredType var4x, final String var5x, final ArrayList var6x, final SecureDocument var7, final ArrayList var8, final SecureDocument var9, final SecureDocument var10, final Runnable var11, final PassportActivity.ErrorRunnable var12) {
               TLRPC.TL_inputSecureValue var13;
               PassportActivity.EncryptionResult var14;
               final TLRPC.TL_inputSecureValue var22;
               if (!TextUtils.isEmpty(var3)) {
                  var13 = new TLRPC.TL_inputSecureValue();
                  var13.type = var1.type;
                  var13.flags |= 1;
                  var14 = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(var3));
                  var13.data = new TLRPC.TL_secureData();
                  TLRPC.TL_secureData var15 = var13.data;
                  var15.data = var14.encryptedData;
                  var15.data_hash = var14.fileHash;
                  var15.secret = var14.fileSecret;
               } else if (!TextUtils.isEmpty(var2)) {
                  TLRPC.SecureValueType var19 = var6;
                  Object var20;
                  if (var19 instanceof TLRPC.TL_secureValueTypeEmail) {
                     var20 = new TLRPC.TL_securePlainEmail();
                     ((TLRPC.TL_securePlainEmail)var20).email = var2;
                  } else {
                     if (!(var19 instanceof TLRPC.TL_secureValueTypePhone)) {
                        return;
                     }

                     var20 = new TLRPC.TL_securePlainPhone();
                     ((TLRPC.TL_securePlainPhone)var20).phone = var2;
                  }

                  var22 = new TLRPC.TL_inputSecureValue();
                  var22.type = var1.type;
                  var22.flags |= 32;
                  var22.plain_data = (TLRPC.SecurePlainData)var20;
                  var13 = var22;
               } else {
                  var13 = null;
               }

               if (!var4 && var13 == null) {
                  if (var12 != null) {
                     var12.onError((String)null, (String)null);
                  }

               } else {
                  label86: {
                     if (var4x != null) {
                        var22 = new TLRPC.TL_inputSecureValue();
                        var22.type = var4x.type;
                        if (!TextUtils.isEmpty(var5x)) {
                           var22.flags |= 1;
                           var14 = PassportActivity.this.encryptData(AndroidUtilities.getStringBytes(var5x));
                           var22.data = new TLRPC.TL_secureData();
                           TLRPC.TL_secureData var16 = var22.data;
                           var16.data = var14.encryptedData;
                           var16.data_hash = var14.fileHash;
                           var16.secret = var14.fileSecret;
                        }

                        if (var9 != null) {
                           var22.front_side = this.getInputSecureFile(var9);
                           var22.flags |= 2;
                        }

                        if (var10 != null) {
                           var22.reverse_side = this.getInputSecureFile(var10);
                           var22.flags |= 4;
                        }

                        if (var7 != null) {
                           var22.selfie = this.getInputSecureFile(var7);
                           var22.flags |= 8;
                        }

                        int var17;
                        int var18;
                        if (var8 != null && !var8.isEmpty()) {
                           var22.flags |= 64;
                           var17 = var8.size();

                           for(var18 = 0; var18 < var17; ++var18) {
                              var22.translation.add(this.getInputSecureFile((SecureDocument)var8.get(var18)));
                           }
                        }

                        if (var6x != null && !var6x.isEmpty()) {
                           var22.flags |= 16;
                           var17 = var6x.size();

                           for(var18 = 0; var18 < var17; ++var18) {
                              var22.files.add(this.getInputSecureFile((SecureDocument)var6x.get(var18)));
                           }
                        }

                        if (!var4) {
                           break label86;
                        }

                        var13 = var22;
                     }

                     var22 = null;
                  }

                  final TLRPC.TL_account_saveSecureValue var21 = new TLRPC.TL_account_saveSecureValue();
                  var21.value = var13;
                  var21.secure_secret_id = PassportActivity.this.secureSecretId;
                  ConnectionsManager.getInstance(PassportActivity.access$8300(PassportActivity.this)).sendRequest(var21, new RequestDelegate() {
                     // $FF: synthetic method
                     static void lambda$run$3(PassportActivity.ErrorRunnable var0, TLRPC.TL_error var1x, String var2x) {
                        var0.onError(var1x.text, var2x);
                     }

                     private void onResult(TLRPC.TL_error var1x, TLRPC.TL_secureValue var2x, TLRPC.TL_secureValue var3x) {
                        PassportActivity.ErrorRunnable var4xx = var12;
                        String var5xx = var2;
                        TLRPC.TL_account_saveSecureValue var6xx = var21;
                        <undefinedtype> var7x = <VAR_NAMELESS_ENCLOSURE>;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$19$1$0kRj7ir3NwvnbVVMIq70mwI_7cA(this, var1x, var4xx, var5xx, var6xx, var4, var4x, var1, var2x, var3x, var6x, var7, var9, var10, var8, var3, var5x, var5, var11));
                     }

                     // $FF: synthetic method
                     public void lambda$null$1$PassportActivity$19$1(TLObject var1x, String var2x, TLRPC.TL_secureRequiredType var3x, PassportActivity.PassportActivityDelegate var4xx, TLRPC.TL_error var5xx, PassportActivity.ErrorRunnable var6xx) {
                        if (var1x != null) {
                           TLRPC.TL_account_sentEmailCode var7x = (TLRPC.TL_account_sentEmailCode)var1x;
                           HashMap var9x = new HashMap();
                           var9x.put("email", var2x);
                           var9x.put("pattern", var7x.email_pattern);
                           PassportActivity var8x = new PassportActivity(6, PassportActivity.this.currentForm, PassportActivity.this.currentPassword, var3x, (TLRPC.TL_secureValue)null, (TLRPC.TL_secureRequiredType)null, (TLRPC.TL_secureValue)null, var9x, (HashMap)null);
                           PassportActivity.access$7402(var8x, PassportActivity.access$7500(PassportActivity.this));
                           var8x.emailCodeLength = var7x.length;
                           var8x.saltedPassword = PassportActivity.this.saltedPassword;
                           var8x.secureSecret = PassportActivity.this.secureSecret;
                           var8x.delegate = var4xx;
                           PassportActivity.this.presentFragment(var8x, true);
                        } else {
                           PassportActivity.this.showAlertWithText(LocaleController.getString("PassportEmail", 2131560217), var5xx.text);
                           if (var6xx != null) {
                              var6xx.onError(var5xx.text, var2x);
                           }
                        }

                     }

                     // $FF: synthetic method
                     public void lambda$onResult$0$PassportActivity$19$1(TLRPC.TL_error var1x, PassportActivity.ErrorRunnable var2x, String var3x, TLRPC.TL_account_saveSecureValue var4xx, boolean var5xx, TLRPC.TL_secureRequiredType var6xx, TLRPC.TL_secureRequiredType var7x, TLRPC.TL_secureValue var8x, TLRPC.TL_secureValue var9x, ArrayList var10x, SecureDocument var11x, SecureDocument var12x, SecureDocument var13, ArrayList var14, String var15, String var16, int var17, Runnable var18) {
                        if (var1x != null) {
                           if (var2x != null) {
                              var2x.onError(var1x.text, var3x);
                           }

                           AlertsCreator.processError(PassportActivity.access$7900(PassportActivity.this), var1x, PassportActivity.this, var4xx, var3x);
                        } else {
                           if (var5xx) {
                              if (var6xx != null) {
                                 PassportActivity.this.removeValue(var6xx);
                              } else {
                                 PassportActivity.this.removeValue(var7x);
                              }
                           } else {
                              PassportActivity.this.removeValue(var7x);
                              PassportActivity.this.removeValue(var6xx);
                           }

                           if (var8x != null) {
                              PassportActivity.this.currentForm.values.add(var8x);
                           }

                           if (var9x != null) {
                              PassportActivity.this.currentForm.values.add(var9x);
                           }

                           int var19;
                           int var20;
                           int var21x;
                           int var23;
                           SecureDocument var24;
                           TLRPC.SecureFile var27;
                           TLRPC.TL_secureFile var28;
                           if (var10x != null && !var10x.isEmpty()) {
                              var19 = var10x.size();

                              label121:
                              for(var20 = 0; var20 < var19; ++var20) {
                                 var24 = (SecureDocument)var10x.get(var20);
                                 var21x = var19;
                                 if (var24.inputFile != null) {
                                    int var22x = var8x.files.size();
                                    var23 = 0;

                                    while(true) {
                                       var21x = var19;
                                       if (var23 >= var22x) {
                                          break;
                                       }

                                       var27 = (TLRPC.SecureFile)var8x.files.get(var23);
                                       if (var27 instanceof TLRPC.TL_secureFile) {
                                          var28 = (TLRPC.TL_secureFile)var27;
                                          if (Utilities.arraysEquals(var24.fileSecret, 0, var28.secret, 0)) {
                                             renameFile(var24, var28);
                                             continue label121;
                                          }
                                       }

                                       ++var23;
                                    }
                                 }

                                 var19 = var21x;
                              }
                           }

                           TLRPC.SecureFile var25;
                           TLRPC.TL_secureFile var26;
                           if (var11x != null && var11x.inputFile != null) {
                              var25 = var8x.selfie;
                              if (var25 instanceof TLRPC.TL_secureFile) {
                                 var26 = (TLRPC.TL_secureFile)var25;
                                 if (Utilities.arraysEquals(var11x.fileSecret, 0, var26.secret, 0)) {
                                    renameFile(var11x, var26);
                                 }
                              }
                           }

                           if (var12x != null && var12x.inputFile != null) {
                              var25 = var8x.front_side;
                              if (var25 instanceof TLRPC.TL_secureFile) {
                                 var26 = (TLRPC.TL_secureFile)var25;
                                 if (Utilities.arraysEquals(var12x.fileSecret, 0, var26.secret, 0)) {
                                    renameFile(var12x, var26);
                                 }
                              }
                           }

                           if (var13 != null && var13.inputFile != null) {
                              var25 = var8x.reverse_side;
                              if (var25 instanceof TLRPC.TL_secureFile) {
                                 var26 = (TLRPC.TL_secureFile)var25;
                                 if (Utilities.arraysEquals(var13.fileSecret, 0, var26.secret, 0)) {
                                    renameFile(var13, var26);
                                 }
                              }
                           }

                           if (var14 != null && !var14.isEmpty()) {
                              var20 = var14.size();

                              for(var19 = 0; var19 < var20; ++var19) {
                                 var24 = (SecureDocument)var14.get(var19);
                                 if (var24.inputFile != null) {
                                    var23 = var8x.translation.size();

                                    for(var21x = 0; var21x < var23; ++var21x) {
                                       var27 = (TLRPC.SecureFile)var8x.translation.get(var21x);
                                       if (var27 instanceof TLRPC.TL_secureFile) {
                                          var28 = (TLRPC.TL_secureFile)var27;
                                          if (Utilities.arraysEquals(var24.fileSecret, 0, var28.secret, 0)) {
                                             renameFile(var24, var28);
                                             break;
                                          }
                                       }
                                    }
                                 }
                              }
                           }

                           PassportActivity.this.setTypeValue(var7x, var3x, var15, var6xx, var16, var5xx, var17);
                           if (var18 != null) {
                              var18.run();
                           }
                        }

                     }

                     // $FF: synthetic method
                     public void lambda$run$2$PassportActivity$19$1(String var1x, TLRPC.TL_secureRequiredType var2x, PassportActivity.PassportActivityDelegate var3x, PassportActivity.ErrorRunnable var4xx, TLObject var5xx, TLRPC.TL_error var6xx) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs(this, var5xx, var1x, var2x, var3x, var6xx, var4xx));
                     }

                     // $FF: synthetic method
                     public void lambda$run$4$PassportActivity$19$1(TLRPC.TL_secureValue var1x, TLObject var2x, TLRPC.TL_error var3x) {
                        this.onResult(var3x, (TLRPC.TL_secureValue)var2x, var1x);
                     }

                     public void run(TLObject var1x, TLRPC.TL_error var2x) {
                        if (var2x != null) {
                           if (var2x.text.equals("EMAIL_VERIFICATION_NEEDED")) {
                              TLRPC.TL_account_sendVerifyEmailCode var4xx = new TLRPC.TL_account_sendVerifyEmailCode();
                              var4xx.email = var2;
                              ConnectionsManager.getInstance(PassportActivity.access$7100(PassportActivity.this)).sendRequest(var4xx, new _$$Lambda$PassportActivity$19$1$z2A8tnBlw_sIzEUvPsIbORU04FQ(this, var2, var1, <VAR_NAMELESS_ENCLOSURE>, var12));
                              return;
                           }

                           if (var2x.text.equals("PHONE_VERIFICATION_NEEDED")) {
                              AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$19$1$PRLUYjV8ZrO0HoPOz4GspIpM_8g(var12, var2x, var2));
                              return;
                           }
                        }

                        if (var2x == null && var22 != null) {
                           TLRPC.TL_secureValue var3x = (TLRPC.TL_secureValue)var1x;
                           TLRPC.TL_account_saveSecureValue var5xx = new TLRPC.TL_account_saveSecureValue();
                           var5xx.value = var22;
                           var5xx.secure_secret_id = PassportActivity.this.secureSecretId;
                           ConnectionsManager.getInstance(PassportActivity.access$7200(PassportActivity.this)).sendRequest(var5xx, new _$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs(this, var3x));
                        } else {
                           this.onResult(var2x, (TLRPC.TL_secureValue)var1x, (TLRPC.TL_secureValue)null);
                        }

                     }
                  });
               }
            }
         };
         var16.currentAccount = super.currentAccount;
         var16.saltedPassword = this.saltedPassword;
         var16.secureSecret = this.secureSecret;
         var16.currentBotId = this.currentBotId;
         var16.fieldsErrors = var9;
         var16.documentOnly = var4;
         var16.documentsErrors = var10;
         var16.availableDocumentTypes = var3;
         if (var8 == 4) {
            var16.currentEmail = this.currentEmail;
         }

         this.presentFragment(var16);
      }

   }

   private void processSelectedAttach(int var1) {
      if (var1 == 0) {
         if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
            this.getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 19);
            return;
         }

         Exception var10000;
         label75: {
            Intent var2;
            File var3;
            boolean var10001;
            try {
               var2 = new Intent("android.media.action.IMAGE_CAPTURE");
               var3 = AndroidUtilities.generatePicturePath();
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label75;
            }

            if (var3 != null) {
               try {
                  var1 = VERSION.SDK_INT;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label75;
               }

               if (var1 >= 24) {
                  try {
                     var2.putExtra("output", FileProvider.getUriForFile(this.getParentActivity(), "org.telegram.messenger.provider", var3));
                     var2.addFlags(2);
                     var2.addFlags(1);
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label75;
                  }
               } else {
                  try {
                     var2.putExtra("output", Uri.fromFile(var3));
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label75;
                  }
               }

               try {
                  this.currentPicturePath = var3.getAbsolutePath();
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label75;
               }
            }

            try {
               this.startActivityForResult(var2, 0);
               return;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         Exception var10 = var10000;
         FileLog.e((Throwable)var10);
      } else if (var1 == 1) {
         if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            this.getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
            return;
         }

         PhotoAlbumPickerActivity var11 = new PhotoAlbumPickerActivity(0, false, false, (ChatActivity)null);
         var11.setCurrentAccount(super.currentAccount);
         var11.setMaxSelectedPhotos(this.getMaxSelectedDocuments());
         var11.setAllowSearchImages(false);
         var11.setDelegate(new PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate() {
            public void didSelectPhotos(ArrayList var1) {
               PassportActivity.this.processSelectedFiles(var1);
            }

            public void startPhotoSelectActivity() {
               try {
                  Intent var1 = new Intent("android.intent.action.PICK");
                  var1.setType("image/*");
                  PassportActivity.this.startActivityForResult(var1, 1);
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

            }
         });
         this.presentFragment(var11);
      } else if (var1 == 4) {
         if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            this.getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
            return;
         }

         DocumentSelectActivity var12 = new DocumentSelectActivity(false);
         var12.setCurrentAccount(super.currentAccount);
         var12.setCanSelectOnlyImageFiles(true);
         var12.setMaxSelectedFiles(this.getMaxSelectedDocuments());
         var12.setDelegate(new DocumentSelectActivity.DocumentSelectActivityDelegate() {
            public void didSelectFiles(DocumentSelectActivity var1, ArrayList var2) {
               var1.finishFragment();
               ArrayList var3 = new ArrayList();
               int var4 = var2.size();

               for(int var5 = 0; var5 < var4; ++var5) {
                  SendMessagesHelper.SendingMediaInfo var6 = new SendMessagesHelper.SendingMediaInfo();
                  var6.path = (String)var2.get(var5);
                  var3.add(var6);
               }

               PassportActivity.this.processSelectedFiles(var3);
            }

            public void startDocumentSelectActivity() {
               try {
                  Intent var1 = new Intent("android.intent.action.GET_CONTENT");
                  if (VERSION.SDK_INT >= 18) {
                     var1.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                  }

                  var1.setType("*/*");
                  PassportActivity.this.startActivityForResult(var1, 21);
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

            }

            // $FF: synthetic method
            public void startMusicSelectActivity(BaseFragment var1) {
               DocumentSelectActivity$DocumentSelectActivityDelegate$_CC.$default$startMusicSelectActivity(this, var1);
            }
         });
         this.presentFragment(var12);
      }

   }

   private void processSelectedFiles(ArrayList var1) {
      if (!var1.isEmpty()) {
         int var2 = this.uploadingFileType;
         boolean var3 = false;
         boolean var4 = var3;
         if (var2 != 1) {
            if (var2 == 4) {
               var4 = var3;
            } else {
               var4 = var3;
               if (this.currentType.type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                  var2 = 0;

                  while(true) {
                     EditTextBoldCursor[] var5 = this.inputFields;
                     if (var2 >= var5.length) {
                        var4 = true;
                        break;
                     }

                     if (var2 != 5 && var2 != 8 && var2 != 4 && var2 != 6 && var5[var2].length() > 0) {
                        var4 = var3;
                        break;
                     }

                     ++var2;
                  }
               }
            }
         }

         var2 = this.uploadingFileType;
         Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$fufT8Jcf6nimD7xnbctUJqRi2PE(this, var1, var2, var4));
      }
   }

   private TLRPC.TL_secureValue removeValue(TLRPC.TL_secureRequiredType var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = 0;

         for(int var3 = this.currentForm.values.size(); var2 < var3; ++var2) {
            TLRPC.TL_secureValue var4 = (TLRPC.TL_secureValue)this.currentForm.values.get(var2);
            if (var1.type.getClass() == var4.type.getClass()) {
               return (TLRPC.TL_secureValue)this.currentForm.values.remove(var2);
            }
         }

         return null;
      }
   }

   private void scrollToField(View var1) {
      while(var1 != null && this.linearLayout2.indexOfChild(var1) < 0) {
         var1 = (View)var1.getParent();
      }

      if (var1 != null) {
         this.scrollView.smoothScrollTo(0, var1.getTop() - (this.scrollView.getMeasuredHeight() - var1.getMeasuredHeight()) / 2);
      }

   }

   private void setFieldValues(HashMap var1, EditTextBoldCursor var2, String var3) {
      String var4 = (String)var1.get(var3);
      String var8;
      if (var4 != null) {
         byte var5 = -1;
         switch(var3.hashCode()) {
         case -2006252145:
            if (var3.equals("residence_country_code")) {
               var5 = 1;
            }
            break;
         case -1249512767:
            if (var3.equals("gender")) {
               var5 = 2;
            }
            break;
         case 475919162:
            if (var3.equals("expiry_date")) {
               var5 = 3;
            }
            break;
         case 1481071862:
            if (var3.equals("country_code")) {
               var5 = 0;
            }
         }

         if (var5 != 0) {
            if (var5 != 1) {
               if (var5 != 2) {
                  if (var5 != 3) {
                     var2.setText(var4);
                  } else {
                     boolean var9;
                     label63: {
                        if (!TextUtils.isEmpty(var4)) {
                           String[] var6 = var4.split("\\.");
                           if (var6.length == 3) {
                              this.currentExpireDate[0] = Utilities.parseInt(var6[2]);
                              this.currentExpireDate[1] = Utilities.parseInt(var6[1]);
                              this.currentExpireDate[2] = Utilities.parseInt(var6[0]);
                              var2.setText(var4);
                              var9 = true;
                              break label63;
                           }
                        }

                        var9 = false;
                     }

                     if (!var9) {
                        int[] var7 = this.currentExpireDate;
                        var7[2] = 0;
                        var7[1] = 0;
                        var7[0] = 0;
                        var2.setText(LocaleController.getString("PassportNoExpireDate", 2131560298));
                     }
                  }
               } else if ("male".equals(var4)) {
                  this.currentGender = var4;
                  var2.setText(LocaleController.getString("PassportMale", 2131560284));
               } else if ("female".equals(var4)) {
                  this.currentGender = var4;
                  var2.setText(LocaleController.getString("PassportFemale", 2131560223));
               }
            } else {
               this.currentResidence = var4;
               var8 = (String)this.languageMap.get(this.currentResidence);
               if (var8 != null) {
                  var2.setText(var8);
               }
            }
         } else {
            this.currentCitizeship = var4;
            var8 = (String)this.languageMap.get(this.currentCitizeship);
            if (var8 != null) {
               var2.setText(var8);
            }
         }
      }

      var1 = this.fieldsErrors;
      if (var1 != null) {
         var8 = (String)var1.get(var3);
         if (var8 != null) {
            var2.setErrorText(var8);
            this.errorsValues.put(var3, var2.getText().toString());
            return;
         }
      }

      var1 = this.documentsErrors;
      if (var1 != null) {
         var8 = (String)var1.get(var3);
         if (var8 != null) {
            var2.setErrorText(var8);
            this.errorsValues.put(var3, var2.getText().toString());
         }
      }

   }

   private void setTypeValue(TLRPC.TL_secureRequiredType var1, String var2, String var3, TLRPC.TL_secureRequiredType var4, String var5, boolean var6, int var7) {
      PassportActivity.TextDetailSecureCell var9 = (PassportActivity.TextDetailSecureCell)this.typesViews.get(var1);
      if (var9 == null) {
         if (this.currentActivityType != 8) {
            return;
         }

         ArrayList var10 = new ArrayList();
         if (var4 != null) {
            var10.add(var4);
         }

         LinearLayout var11 = this.linearLayout2;
         View var75 = var11.getChildAt(var11.getChildCount() - 6);
         if (var75 instanceof PassportActivity.TextDetailSecureCell) {
            ((PassportActivity.TextDetailSecureCell)var75).setNeedDivider(true);
         }

         var9 = this.addField(this.getParentActivity(), var1, var10, true, true);
         this.updateManageVisibility();
      }

      HashMap var12 = (HashMap)this.typesValues.get(var1);
      HashMap var13;
      if (var4 != null) {
         var13 = (HashMap)this.typesValues.get(var4);
      } else {
         var13 = null;
      }

      TLRPC.TL_secureValue var74 = this.getValueByType(var1, true);
      TLRPC.TL_secureValue var14 = this.getValueByType(var4, true);
      boolean var10001;
      TLRPC.TL_secureValue var18;
      String[] var88;
      if (var3 != null && this.languageMap == null) {
         this.languageMap = new HashMap();
         TLRPC.TL_secureValue var76 = var74;

         label660: {
            label659: {
               Exception var85;
               label730: {
                  Exception var10000;
                  label656: {
                     label675: {
                        BufferedReader var15;
                        try {
                           var15 = new BufferedReader;
                        } catch (Exception var63) {
                           var10000 = var63;
                           var10001 = false;
                           break label675;
                        }

                        var76 = var74;

                        InputStreamReader var16;
                        try {
                           var16 = new InputStreamReader;
                        } catch (Exception var62) {
                           var10000 = var62;
                           var10001 = false;
                           break label675;
                        }

                        var76 = var74;

                        try {
                           var16.<init>(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt"));
                        } catch (Exception var61) {
                           var10000 = var61;
                           var10001 = false;
                           break label675;
                        }

                        var76 = var74;

                        try {
                           var15.<init>(var16);
                        } catch (Exception var60) {
                           var10000 = var60;
                           var10001 = false;
                           break label675;
                        }

                        while(true) {
                           var76 = var74;

                           String var87;
                           try {
                              var87 = var15.readLine();
                           } catch (Exception var59) {
                              var10000 = var59;
                              var10001 = false;
                              break;
                           }

                           if (var87 == null) {
                              try {
                                 var15.close();
                                 break label659;
                              } catch (Exception var55) {
                                 var10000 = var55;
                                 var10001 = false;
                                 break label656;
                              }
                           }

                           var76 = var74;

                           try {
                              var88 = var87.split(";");
                           } catch (Exception var58) {
                              var10000 = var58;
                              var10001 = false;
                              break;
                           }

                           var76 = var74;

                           HashMap var17;
                           try {
                              var17 = this.languageMap;
                           } catch (Exception var57) {
                              var10000 = var57;
                              var10001 = false;
                              break;
                           }

                           try {
                              var17.put(var88[1], var88[2]);
                           } catch (Exception var56) {
                              var10000 = var56;
                              var10001 = false;
                              break label656;
                           }
                        }
                     }

                     var85 = var10000;
                     var74 = var76;
                     break label730;
                  }

                  var85 = var10000;
               }

               FileLog.e((Throwable)var85);
               break label660;
            }

            var74 = var74;
         }

         var18 = var74;
      } else {
         this.languageMap = null;
         var18 = var74;
      }

      boolean var19;
      TLRPC.SecureValueType var67;
      label617: {
         if (var2 != null) {
            var67 = var1.type;
            if (var67 instanceof TLRPC.TL_secureValueTypePhone) {
               PhoneFormat var68 = PhoneFormat.getInstance();
               StringBuilder var70 = new StringBuilder();
               var70.append("+");
               var70.append(var2);
               var2 = var68.format(var70.toString());
               break label617;
            }

            if (var67 instanceof TLRPC.TL_secureValueTypeEmail) {
               break label617;
            }
         } else {
            StringBuilder var65;
            if (this.currentActivityType == 8 || var4 == null || TextUtils.isEmpty(var5) && var14 == null) {
               var65 = null;
            } else {
               StringBuilder var77 = new StringBuilder();
               if (var7 > 1) {
                  var77.append(this.getTextForType(var4.type));
                  var65 = var77;
               } else {
                  var65 = var77;
                  if (TextUtils.isEmpty(var5)) {
                     var77.append(LocaleController.getString("PassportDocuments", 2131560216));
                     var65 = var77;
                  }
               }
            }

            if (var3 != null || var5 != null) {
               if (var12 == null) {
                  return;
               }

               var12.clear();
               TLRPC.SecureValueType var78 = var1.type;
               var19 = var78 instanceof TLRPC.TL_secureValueTypePersonalDetails;
               String var86 = "first_name_native";
               String var82 = "middle_name";
               int var20;
               String[] var90;
               if (var19) {
                  if ((this.currentActivityType != 0 || var6) && (this.currentActivityType != 8 || var4 != null)) {
                     var88 = null;
                  } else {
                     var88 = new String[]{"first_name", "middle_name", "last_name", "first_name_native", "middle_name_native", "last_name_native", "birth_date", "gender", "country_code", "residence_country_code"};
                  }

                  var20 = this.currentActivityType;
                  if (var20 == 0 || var20 == 8 && var4 != null) {
                     var90 = new String[]{"document_no", "expiry_date"};
                  } else {
                     var90 = null;
                  }
               } else {
                  if (!(var78 instanceof TLRPC.TL_secureValueTypeAddress) || (this.currentActivityType != 0 || var6) && (this.currentActivityType != 8 || var4 != null)) {
                     var88 = null;
                  } else {
                     var88 = new String[]{"street_line1", "street_line2", "post_code", "city", "state", "country_code"};
                  }

                  var90 = null;
               }

               if (var88 != null || var90 != null) {
                  label703: {
                     String var21 = null;
                     int var22 = 0;
                     String[] var8 = null;
                     HashMap var79 = var12;
                     JSONObject var80 = var21;

                     StringBuilder var24;
                     label568:
                     while(true) {
                        String var23 = var5;
                        var21 = var3;
                        var24 = var65;
                        if (var22 >= 2) {
                           break;
                        }

                        String var81;
                        HashMap var84;
                        HashMap var89;
                        StringBuilder var92;
                        label725: {
                           if (var22 == 0) {
                              if (var3 != null) {
                                 var24 = var65;

                                 try {
                                    var80 = new JSONObject;
                                 } catch (Exception var54) {
                                    var10001 = false;
                                    break;
                                 }

                                 var24 = var65;

                                 try {
                                    var80.<init>(var21);
                                 } catch (Exception var53) {
                                    var10001 = false;
                                    break;
                                 }

                                 var8 = var88;
                              }
                           } else {
                              if (var13 == null) {
                                 var92 = var65;
                                 var89 = var79;
                                 var84 = var13;
                                 var81 = var86;
                                 var2 = var82;
                                 break label725;
                              }

                              if (var5 != null) {
                                 var24 = var65;

                                 try {
                                    var80 = new JSONObject(var23);
                                 } catch (Exception var52) {
                                    var10001 = false;
                                    break;
                                 }

                                 var8 = var90;
                              }
                           }

                           HashMap var25 = var79;
                           HashMap var26 = var13;
                           String var27 = var86;
                           var23 = var82;
                           var92 = var65;
                           if (var8 != null) {
                              if (var80 == null) {
                                 var25 = var79;
                                 var26 = var13;
                                 var27 = var86;
                                 var23 = var82;
                                 var92 = var65;
                              } else {
                                 String var96;
                                 label685: {
                                    Throwable var97;
                                    label543: {
                                       Iterator var93;
                                       try {
                                          var93 = var80.keys();
                                       } catch (Throwable var51) {
                                          var97 = var51;
                                          var10001 = false;
                                          break label543;
                                       }

                                       while(true) {
                                          try {
                                             if (!var93.hasNext()) {
                                                break label685;
                                             }

                                             var96 = (String)var93.next();
                                          } catch (Throwable var50) {
                                             var97 = var50;
                                             var10001 = false;
                                             break;
                                          }

                                          if (var22 == 0) {
                                             try {
                                                var79.put(var96, var80.getString(var96));
                                             } catch (Throwable var48) {
                                                var97 = var48;
                                                var10001 = false;
                                                break;
                                             }
                                          } else {
                                             try {
                                                var13.put(var96, var80.getString(var96));
                                             } catch (Throwable var49) {
                                                var97 = var49;
                                                var10001 = false;
                                                break;
                                             }
                                          }
                                       }
                                    }

                                    Throwable var94 = var97;
                                    var24 = var65;

                                    try {
                                       FileLog.e(var94);
                                    } catch (Exception var47) {
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 int var28 = 0;

                                 while(true) {
                                    var25 = var79;
                                    var26 = var13;
                                    var27 = var86;
                                    var23 = var82;
                                    var92 = var65;
                                    var24 = var65;

                                    try {
                                       if (var28 >= var8.length) {
                                          break;
                                       }
                                    } catch (Exception var31) {
                                       var10001 = false;
                                       break label568;
                                    }

                                    var24 = var65;

                                    label713: {
                                       try {
                                          if (!var80.has(var8[var28])) {
                                             break label713;
                                          }
                                       } catch (Exception var32) {
                                          var10001 = false;
                                          break label568;
                                       }

                                       if (var65 == null) {
                                          var24 = var65;

                                          try {
                                             var92 = new StringBuilder;
                                          } catch (Exception var30) {
                                             var10001 = false;
                                             break label568;
                                          }

                                          var24 = var65;

                                          try {
                                             var92.<init>();
                                          } catch (Exception var29) {
                                             var10001 = false;
                                             break label568;
                                          }

                                          var65 = var92;
                                       }

                                       try {
                                          var21 = var80.getString(var8[var28]);
                                       } catch (Exception var41) {
                                          var10001 = false;
                                          break label703;
                                       }

                                       if (var21 != null) {
                                          label718: {
                                             try {
                                                if (TextUtils.isEmpty(var21) || var86.equals(var8[var28]) || "middle_name_native".equals(var8[var28]) || "last_name_native".equals(var8[var28])) {
                                                   break label718;
                                                }
                                             } catch (Exception var40) {
                                                var10001 = false;
                                                break label703;
                                             }

                                             label515: {
                                                label728: {
                                                   try {
                                                      if (var65.length() <= 0) {
                                                         break label515;
                                                      }

                                                      if ("last_name".equals(var8[var28]) || "last_name_native".equals(var8[var28]) || var82.equals(var8[var28]) || "middle_name_native".equals(var8[var28])) {
                                                         break label728;
                                                      }
                                                   } catch (Exception var46) {
                                                      var10001 = false;
                                                      break label703;
                                                   }

                                                   try {
                                                      var65.append(", ");
                                                      break label515;
                                                   } catch (Exception var39) {
                                                      var10001 = false;
                                                      break label703;
                                                   }
                                                }

                                                try {
                                                   var65.append(" ");
                                                } catch (Exception var38) {
                                                   var10001 = false;
                                                   break label703;
                                                }
                                             }

                                             var96 = var8[var28];

                                             try {
                                                var20 = var96.hashCode();
                                             } catch (Exception var37) {
                                                var10001 = false;
                                                break label703;
                                             }

                                             byte var91;
                                             label497: {
                                                label496: {
                                                   label495: {
                                                      label494: {
                                                         if (var20 != -2006252145) {
                                                            if (var20 != -1249512767) {
                                                               if (var20 == 1481071862) {
                                                                  try {
                                                                     if (var96.equals("country_code")) {
                                                                        break label496;
                                                                     }
                                                                  } catch (Exception var43) {
                                                                     var10001 = false;
                                                                     break label703;
                                                                  }
                                                               }
                                                            } else {
                                                               try {
                                                                  if (var96.equals("gender")) {
                                                                     break label495;
                                                                  }
                                                               } catch (Exception var44) {
                                                                  var10001 = false;
                                                                  break label703;
                                                               }
                                                            }
                                                         } else {
                                                            try {
                                                               if (var96.equals("residence_country_code")) {
                                                                  break label494;
                                                               }
                                                            } catch (Exception var45) {
                                                               var10001 = false;
                                                               break label703;
                                                            }
                                                         }

                                                         var91 = -1;
                                                         break label497;
                                                      }

                                                      var91 = 1;
                                                      break label497;
                                                   }

                                                   var91 = 2;
                                                   break label497;
                                                }

                                                var91 = 0;
                                             }

                                             if (var91 != 0 && var91 != 1) {
                                                if (var91 != 2) {
                                                   try {
                                                      var65.append(var21);
                                                   } catch (Exception var33) {
                                                      var10001 = false;
                                                      break label703;
                                                   }
                                                } else {
                                                   label705: {
                                                      try {
                                                         if ("male".equals(var21)) {
                                                            var65.append(LocaleController.getString("PassportMale", 2131560284));
                                                            break label705;
                                                         }
                                                      } catch (Exception var42) {
                                                         var10001 = false;
                                                         break label703;
                                                      }

                                                      try {
                                                         if ("female".equals(var21)) {
                                                            var65.append(LocaleController.getString("PassportFemale", 2131560223));
                                                         }
                                                      } catch (Exception var34) {
                                                         var10001 = false;
                                                         break label703;
                                                      }
                                                   }
                                                }
                                             } else {
                                                try {
                                                   var21 = (String)this.languageMap.get(var21);
                                                } catch (Exception var36) {
                                                   var10001 = false;
                                                   break label703;
                                                }

                                                if (var21 != null) {
                                                   try {
                                                      var65.append(var21);
                                                   } catch (Exception var35) {
                                                      var10001 = false;
                                                      break label703;
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }

                                    ++var28;
                                 }
                              }
                           }

                           var89 = var25;
                           var84 = var26;
                           var81 = var27;
                           var2 = var23;
                        }

                        ++var22;
                        var79 = var89;
                        var13 = var84;
                        var86 = var81;
                        var82 = var2;
                        var65 = var92;
                     }

                     var65 = var24;
                  }
               }
            }

            if (var65 != null) {
               var2 = var65.toString();
               break label617;
            }
         }

         var2 = null;
      }

      HashMap var69;
      if (!var6) {
         var69 = (HashMap)this.errorsMap.get(this.getNameForType(var1.type));
      } else {
         var69 = null;
      }

      HashMap var71;
      if (var4 != null) {
         var71 = (HashMap)this.errorsMap.get(this.getNameForType(var4.type));
      } else {
         var71 = null;
      }

      String var64;
      boolean var72;
      if ((var69 == null || var69.size() <= 0) && (var71 == null || var71.size() <= 0)) {
         label398: {
            var67 = var1.type;
            if (var67 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
               if (TextUtils.isEmpty(var2)) {
                  if (var4 == null) {
                     var64 = LocaleController.getString("PassportPersonalDetailsInfo", 2131560302);
                     break label398;
                  }

                  if (this.currentActivityType == 8) {
                     var64 = LocaleController.getString("PassportDocuments", 2131560216);
                     break label398;
                  }

                  if (var7 != 1) {
                     var64 = LocaleController.getString("PassportIdentityDocumentInfo", 2131560228);
                     break label398;
                  }

                  TLRPC.SecureValueType var66 = var4.type;
                  if (var66 instanceof TLRPC.TL_secureValueTypePassport) {
                     var64 = LocaleController.getString("PassportIdentityPassport", 2131560232);
                     break label398;
                  }

                  if (var66 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                     var64 = LocaleController.getString("PassportIdentityInternalPassport", 2131560231);
                     break label398;
                  }

                  if (var66 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                     var64 = LocaleController.getString("PassportIdentityDriverLicence", 2131560229);
                     break label398;
                  }

                  if (var66 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                     var64 = LocaleController.getString("PassportIdentityID", 2131560230);
                     break label398;
                  }
               }
            } else {
               if (!(var67 instanceof TLRPC.TL_secureValueTypeAddress)) {
                  if (var67 instanceof TLRPC.TL_secureValueTypePhone) {
                     var64 = var2;
                     if (TextUtils.isEmpty(var2)) {
                        var64 = LocaleController.getString("PassportPhoneInfo", 2131560305);
                     }
                  } else {
                     var64 = var2;
                     if (var67 instanceof TLRPC.TL_secureValueTypeEmail) {
                        var64 = var2;
                        if (TextUtils.isEmpty(var2)) {
                           var64 = LocaleController.getString("PassportEmailInfo", 2131560219);
                        }
                     }
                  }
                  break label398;
               }

               if (TextUtils.isEmpty(var2)) {
                  if (var4 == null) {
                     var64 = LocaleController.getString("PassportAddressNoUploadInfo", 2131560192);
                  } else if (this.currentActivityType == 8) {
                     var64 = LocaleController.getString("PassportDocuments", 2131560216);
                  } else if (var7 == 1) {
                     var67 = var4.type;
                     if (var67 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                        var64 = LocaleController.getString("PassportAddAgreementInfo", 2131560166);
                     } else if (var67 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                        var64 = LocaleController.getString("PassportAddBillInfo", 2131560170);
                     } else if (var67 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                        var64 = LocaleController.getString("PassportAddPassportRegistrationInfo", 2131560180);
                     } else if (var67 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                        var64 = LocaleController.getString("PassportAddTemporaryRegistrationInfo", 2131560182);
                     } else {
                        var64 = var2;
                        if (var67 instanceof TLRPC.TL_secureValueTypeBankStatement) {
                           var64 = LocaleController.getString("PassportAddBankInfo", 2131560168);
                        }
                     }
                  } else {
                     var64 = LocaleController.getString("PassportAddressInfo", 2131560191);
                  }
                  break label398;
               }
            }

            var64 = var2;
         }

         var72 = false;
      } else {
         if (!var6) {
            var64 = (String)this.mainErrorsMap.get(this.getNameForType(var1.type));
         } else {
            var64 = null;
         }

         if (var64 == null) {
            var64 = (String)this.mainErrorsMap.get(this.getNameForType(var4.type));
         }

         var72 = true;
      }

      var19 = true;
      var9.setValue(var64);
      TextView var73 = var9.valueTextView;
      if (var72) {
         var64 = "windowBackgroundWhiteRedText3";
      } else {
         var64 = "windowBackgroundWhiteGrayText2";
      }

      label376: {
         var73.setTextColor(Theme.getColor(var64));
         if (!var72 && this.currentActivityType != 8 && (var6 && var4 != null || !var6 && var18 != null)) {
            var6 = var19;
            if (var4 == null) {
               break label376;
            }

            if (var14 != null) {
               var6 = var19;
               break label376;
            }
         }

         var6 = false;
      }

      var9.setChecked(var6);
   }

   private void showAlertWithText(String var1, String var2) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var3.setTitle(var1);
         var3.setMessage(var2);
         this.showDialog(var3.create());
      }
   }

   private void showAttachmentError() {
      if (this.getParentActivity() != null) {
         Toast.makeText(this.getParentActivity(), LocaleController.getString("UnsupportedAttachment", 2131560946), 0).show();
      }
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
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{1.0F})});
         } else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.ALPHA, new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(var1)) {
                  PassportActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(var1)) {
                  if (!var2) {
                     PassportActivity.this.progressView.setVisibility(4);
                  } else {
                     PassportActivity.this.doneItem.getImageView().setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      } else if (this.acceptTextView != null) {
         this.doneItemAnimation = new AnimatorSet();
         if (var2) {
            this.progressViewButton.setVisibility(0);
            this.bottomLayout.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.acceptTextView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, new float[]{1.0F})});
         } else {
            this.acceptTextView.setVisibility(0);
            this.bottomLayout.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.acceptTextView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.acceptTextView, View.ALPHA, new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1) {
               if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(var1)) {
                  PassportActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1) {
               if (PassportActivity.this.doneItemAnimation != null && PassportActivity.this.doneItemAnimation.equals(var1)) {
                  if (!var2) {
                     PassportActivity.this.progressViewButton.setVisibility(4);
                  } else {
                     PassportActivity.this.acceptTextView.setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      }

   }

   private void startPhoneVerification(boolean var1, String var2, Runnable var3, PassportActivity.ErrorRunnable var4, PassportActivity.PassportActivityDelegate var5) {
      TelephonyManager var6 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
      boolean var7;
      if (var6.getSimState() != 1 && var6.getPhoneType() != 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var9;
      if (this.getParentActivity() != null && VERSION.SDK_INT >= 23 && var7) {
         boolean var8;
         if (this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         var9 = var8;
         if (var1) {
            this.permissionsItems.clear();
            if (!var8) {
               this.permissionsItems.add("android.permission.READ_PHONE_STATE");
            }

            var9 = var8;
            if (!this.permissionsItems.isEmpty()) {
               if (this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                  AlertDialog.Builder var19 = new AlertDialog.Builder(this.getParentActivity());
                  var19.setTitle(LocaleController.getString("AppName", 2131558635));
                  var19.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var19.setMessage(LocaleController.getString("AllowReadCall", 2131558607));
                  this.permissionsDialog = this.showDialog(var19.create());
               } else {
                  this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
               }

               this.pendingPhone = var2;
               this.pendingErrorRunnable = var4;
               this.pendingFinishRunnable = var3;
               this.pendingDelegate = var5;
               return;
            }
         }
      } else {
         var9 = true;
      }

      TLRPC.TL_account_sendVerifyPhoneCode var15 = new TLRPC.TL_account_sendVerifyPhoneCode();
      var15.phone_number = var2;
      var15.settings = new TLRPC.TL_codeSettings();
      TLRPC.TL_codeSettings var16 = var15.settings;
      if (var7 && var9) {
         var1 = true;
      } else {
         var1 = false;
      }

      var16.allow_flashcall = var1;
      if (VERSION.SDK_INT >= 26) {
         try {
            TLRPC.TL_codeSettings var10 = var15.settings;
            SmsManager var11 = SmsManager.getDefault();
            Context var17 = ApplicationLoader.applicationContext;
            Intent var12 = new Intent(ApplicationLoader.applicationContext, SmsReceiver.class);
            var10.app_hash = var11.createAppSpecificSmsToken(PendingIntent.getBroadcast(var17, 0, var12, 134217728));
         } catch (Throwable var14) {
            FileLog.e(var14);
         }
      } else {
         var16 = var15.settings;
         var16.app_hash = BuildVars.SMS_HASH;
         var16.app_hash_persistent = true;
      }

      SharedPreferences var20 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
      if (!TextUtils.isEmpty(var15.settings.app_hash)) {
         var16 = var15.settings;
         var16.flags |= 8;
         var20.edit().putString("sms_hash", var15.settings.app_hash).commit();
      } else {
         var20.edit().remove("sms_hash").commit();
      }

      if (var15.settings.allow_flashcall) {
         try {
            String var18 = var6.getLine1Number();
            if (!TextUtils.isEmpty(var18)) {
               var15.settings.current_number = PhoneNumberUtils.compare(var2, var18);
               if (!var15.settings.current_number) {
                  var15.settings.allow_flashcall = false;
               }
            } else {
               var15.settings.current_number = false;
            }
         } catch (Exception var13) {
            var15.settings.allow_flashcall = false;
            FileLog.e((Throwable)var13);
         }
      }

      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var15, new _$$Lambda$PassportActivity$ziNY0SahWhkV1q9_2nyx_UlZ_Kc(this, var2, var5, var15), 2);
   }

   private void updateInterfaceStringsForDocumentType() {
      TLRPC.TL_secureRequiredType var1 = this.currentDocumentsType;
      if (var1 != null) {
         super.actionBar.setTitle(this.getTextForType(var1.type));
      } else {
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
      } else {
         this.emptyLayout.setVisibility(8);
         this.sectionCell.setVisibility(0);
         this.headerCell.setVisibility(0);
         this.deletePassportCell.setVisibility(0);
         this.addDocumentSectionCell.setVisibility(0);
         if (this.hasUnfilledValues()) {
            this.addDocumentCell.setVisibility(0);
         } else {
            this.addDocumentCell.setVisibility(8);
         }
      }

   }

   private void updatePasswordInterface() {
      if (this.noPasswordImageView != null) {
         TLRPC.TL_account_password var1 = this.currentPassword;
         if (var1 != null && this.usingSavedPassword == 0) {
            if (!var1.has_password) {
               this.passwordRequestTextView.setVisibility(0);
               this.noPasswordImageView.setVisibility(0);
               this.noPasswordTextView.setVisibility(0);
               this.noPasswordSetTextView.setVisibility(0);
               this.passwordAvatarContainer.setVisibility(8);
               this.inputFieldContainers[0].setVisibility(8);
               this.doneItem.setVisibility(8);
               this.passwordForgotButton.setVisibility(8);
               this.passwordInfoRequestTextView.setVisibility(8);
               this.passwordRequestTextView.setLayoutParams(LayoutHelper.createLinear(-1, -2, 0.0F, 25.0F, 0.0F, 0.0F));
               this.emptyView.setVisibility(8);
            } else {
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
               this.passwordRequestTextView.setLayoutParams(LayoutHelper.createLinear(-1, -2, 0.0F, 0.0F, 0.0F, 0.0F));
               if (this.inputFields != null) {
                  var1 = this.currentPassword;
                  if (var1 != null && !TextUtils.isEmpty(var1.hint)) {
                     this.inputFields[0].setHint(this.currentPassword.hint);
                  } else {
                     this.inputFields[0].setHint(LocaleController.getString("LoginPassword", 2131559788));
                  }
               }
            }
         } else {
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
   }

   private void updateUploadText(int var1) {
      boolean var2 = true;
      byte var3 = 0;
      byte var4 = 0;
      byte var5 = 0;
      if (var1 == 0) {
         if (this.uploadDocumentCell == null) {
            return;
         }

         if (this.documents.size() >= 1) {
            this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131560339), false);
         } else {
            this.uploadDocumentCell.setText(LocaleController.getString("PassportUploadDocument", 2131560340), false);
         }
      } else {
         TextDetailSettingsCell var6;
         byte var8;
         if (var1 == 1) {
            var6 = this.uploadSelfieCell;
            if (var6 == null) {
               return;
            }

            var8 = var5;
            if (this.selfieDocument != null) {
               var8 = 8;
            }

            var6.setVisibility(var8);
         } else if (var1 == 4) {
            if (this.uploadTranslationCell == null) {
               return;
            }

            if (this.translationDocuments.size() >= 1) {
               this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadAdditinalDocument", 2131560339), false);
            } else {
               this.uploadTranslationCell.setText(LocaleController.getString("PassportUploadDocument", 2131560340), false);
            }
         } else {
            TLRPC.SecureValueType var10;
            if (var1 == 2) {
               if (this.uploadFrontCell == null) {
                  return;
               }

               boolean var7;
               label78: {
                  TLRPC.TL_secureRequiredType var9 = this.currentDocumentsType;
                  if (var9 != null) {
                     var7 = var2;
                     if (var9.selfie_required) {
                        break label78;
                     }

                     var10 = var9.type;
                     var7 = var2;
                     if (var10 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                        break label78;
                     }

                     if (var10 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                        var7 = var2;
                        break label78;
                     }
                  }

                  var7 = false;
               }

               var10 = this.currentDocumentsType.type;
               if (!(var10 instanceof TLRPC.TL_secureValueTypePassport) && !(var10 instanceof TLRPC.TL_secureValueTypeInternalPassport)) {
                  this.uploadFrontCell.setTextAndValue(LocaleController.getString("PassportFrontSide", 2131560224), LocaleController.getString("PassportFrontSideInfo", 2131560225), var7);
               } else {
                  this.uploadFrontCell.setTextAndValue(LocaleController.getString("PassportMainPage", 2131560282), LocaleController.getString("PassportMainPageInfo", 2131560283), var7);
               }

               var6 = this.uploadFrontCell;
               var8 = var3;
               if (this.frontDocument != null) {
                  var8 = 8;
               }

               var6.setVisibility(var8);
            } else if (var1 == 3) {
               if (this.uploadReverseCell == null) {
                  return;
               }

               var10 = this.currentDocumentsType.type;
               if (!(var10 instanceof TLRPC.TL_secureValueTypeIdentityCard) && !(var10 instanceof TLRPC.TL_secureValueTypeDriverLicense)) {
                  this.reverseLayout.setVisibility(8);
                  this.uploadReverseCell.setVisibility(8);
               } else {
                  this.reverseLayout.setVisibility(0);
                  var6 = this.uploadReverseCell;
                  var8 = var4;
                  if (this.reverseDocument != null) {
                     var8 = 8;
                  }

                  var6.setVisibility(var8);
               }
            }
         }
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         private boolean onIdentityDone(Runnable var1, PassportActivity.ErrorRunnable var2) {
            boolean var3 = PassportActivity.this.uploadingDocuments.isEmpty();
            byte var4 = 0;
            if (var3 && !PassportActivity.this.checkFieldsForError()) {
               int var6;
               AlertDialog.Builder var11;
               String var41;
               String var42;
               if (PassportActivity.this.allowNonLatinName) {
                  PassportActivity.this.allowNonLatinName = false;
                  boolean var5 = false;

                  boolean var7;
                  for(var6 = 0; var6 < PassportActivity.this.nonLatinNames.length; var5 = var7) {
                     var7 = var5;
                     if (PassportActivity.this.nonLatinNames[var6]) {
                        PassportActivity.this.inputFields[var6].setErrorText(LocaleController.getString("PassportUseLatinOnly", 2131560343));
                        var7 = var5;
                        if (!var5) {
                           PassportActivity var8;
                           if (PassportActivity.this.nonLatinNames[0]) {
                              var8 = PassportActivity.this;
                              var41 = var8.getTranslitString(var8.inputExtraFields[0].getText().toString());
                           } else {
                              var41 = PassportActivity.this.inputFields[0].getText().toString();
                           }

                           if (PassportActivity.this.nonLatinNames[1]) {
                              PassportActivity var9 = PassportActivity.this;
                              var42 = var9.getTranslitString(var9.inputExtraFields[1].getText().toString());
                           } else {
                              var42 = PassportActivity.this.inputFields[1].getText().toString();
                           }

                           String var46;
                           if (PassportActivity.this.nonLatinNames[2]) {
                              PassportActivity var10 = PassportActivity.this;
                              var46 = var10.getTranslitString(var10.inputExtraFields[2].getText().toString());
                           } else {
                              var46 = PassportActivity.this.inputFields[2].getText().toString();
                           }

                           if (!TextUtils.isEmpty(var41) && !TextUtils.isEmpty(var42) && !TextUtils.isEmpty(var46)) {
                              var11 = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                              var11.setMessage(LocaleController.formatString("PassportNameCheckAlert", 2131560289, var41, var42, var46));
                              var11.setTitle(LocaleController.getString("AppName", 2131558635));
                              var11.setPositiveButton(LocaleController.getString("Done", 2131559299), new _$$Lambda$PassportActivity$3$hBvwZ_d4QGDnNuXdFnmSB9952Bs(this, var41, var42, var46, var1, var2));
                              var11.setNegativeButton(LocaleController.getString("Edit", 2131559301), new _$$Lambda$PassportActivity$3$tZIb58L3Zb4a9cJUHmxI2DCu8M8(this, var6));
                              PassportActivity.this.showDialog(var11.create());
                           } else {
                              var8 = PassportActivity.this;
                              var8.onFieldError(var8.inputFields[var6]);
                           }

                           var7 = true;
                        }
                     }

                     ++var6;
                  }

                  if (var5) {
                     return false;
                  }
               }

               if (PassportActivity.this.isHasNotAnyChanges()) {
                  PassportActivity.this.finishFragment();
                  return false;
               } else {
                  var11 = null;

                  JSONObject var45;
                  JSONObject var47;
                  label261: {
                     label260: {
                        label259: {
                           label293: {
                              boolean var10001;
                              int var40;
                              JSONObject var44;
                              label294: {
                                 HashMap var12;
                                 label256: {
                                    label255: {
                                       try {
                                          if (PassportActivity.this.documentOnly) {
                                             break label255;
                                          }

                                          var12 = new HashMap(PassportActivity.this.currentValues);
                                          if (!PassportActivity.this.currentType.native_names) {
                                             break label256;
                                          }

                                          var6 = PassportActivity.this.nativeInfoCell.getVisibility();
                                       } catch (Exception var39) {
                                          var10001 = false;
                                          break label259;
                                       }

                                       if (var6 == 0) {
                                          try {
                                             var12.put("first_name_native", PassportActivity.this.inputExtraFields[0].getText().toString());
                                             var12.put("middle_name_native", PassportActivity.this.inputExtraFields[1].getText().toString());
                                             var12.put("last_name_native", PassportActivity.this.inputExtraFields[2].getText().toString());
                                             break label256;
                                          } catch (Exception var23) {
                                             var10001 = false;
                                             break label259;
                                          }
                                       } else {
                                          try {
                                             var12.put("first_name_native", PassportActivity.this.inputFields[0].getText().toString());
                                             var12.put("middle_name_native", PassportActivity.this.inputFields[1].getText().toString());
                                             var12.put("last_name_native", PassportActivity.this.inputFields[2].getText().toString());
                                             break label256;
                                          } catch (Exception var22) {
                                             var10001 = false;
                                             break label259;
                                          }
                                       }
                                    }

                                    var44 = null;
                                    break label294;
                                 }

                                 try {
                                    var12.put("first_name", PassportActivity.this.inputFields[0].getText().toString());
                                    var12.put("middle_name", PassportActivity.this.inputFields[1].getText().toString());
                                    var12.put("last_name", PassportActivity.this.inputFields[2].getText().toString());
                                    var12.put("birth_date", PassportActivity.this.inputFields[3].getText().toString());
                                    var12.put("gender", PassportActivity.this.currentGender);
                                    var12.put("country_code", PassportActivity.this.currentCitizeship);
                                    var12.put("residence_country_code", PassportActivity.this.currentResidence);
                                    var47 = new JSONObject();
                                 } catch (Exception var21) {
                                    var10001 = false;
                                    break label259;
                                 }

                                 var45 = var47;

                                 ArrayList var13;
                                 try {
                                    var13 = new ArrayList;
                                 } catch (Exception var38) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var45 = var47;

                                 try {
                                    var13.<init>(var12.keySet());
                                 } catch (Exception var37) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var45 = var47;

                                 _$$Lambda$PassportActivity$3$mGr_6qgzmzLZw5uyzhfjuEIAkBg var43;
                                 try {
                                    var43 = new _$$Lambda$PassportActivity$3$mGr_6qgzmzLZw5uyzhfjuEIAkBg;
                                 } catch (Exception var36) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var45 = var47;

                                 try {
                                    var43.<init>(this);
                                 } catch (Exception var35) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var45 = var47;

                                 try {
                                    Collections.sort(var13, var43);
                                 } catch (Exception var34) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var45 = var47;

                                 try {
                                    var40 = var13.size();
                                 } catch (Exception var33) {
                                    var10001 = false;
                                    break label293;
                                 }

                                 var6 = 0;

                                 while(true) {
                                    var44 = var47;
                                    if (var6 >= var40) {
                                       break;
                                    }

                                    var45 = var47;

                                    try {
                                       var41 = (String)var13.get(var6);
                                    } catch (Exception var32) {
                                       var10001 = false;
                                       break label293;
                                    }

                                    var45 = var47;

                                    try {
                                       var47.put(var41, var12.get(var41));
                                    } catch (Exception var31) {
                                       var10001 = false;
                                       break label293;
                                    }

                                    ++var6;
                                 }
                              }

                              var47 = var44;
                              var45 = var44;

                              try {
                                 if (PassportActivity.this.currentDocumentsType == null) {
                                    break label260;
                                 }
                              } catch (Exception var30) {
                                 var10001 = false;
                                 break label293;
                              }

                              var45 = var44;

                              HashMap var50;
                              try {
                                 var50 = new HashMap;
                              } catch (Exception var29) {
                                 var10001 = false;
                                 break label293;
                              }

                              var45 = var44;

                              try {
                                 var50.<init>(PassportActivity.this.currentDocumentValues);
                              } catch (Exception var28) {
                                 var10001 = false;
                                 break label293;
                              }

                              var45 = var44;

                              try {
                                 var50.put("document_no", PassportActivity.this.inputFields[7].getText().toString());
                              } catch (Exception var27) {
                                 var10001 = false;
                                 break label293;
                              }

                              var45 = var44;

                              try {
                                 var6 = PassportActivity.this.currentExpireDate[0];
                              } catch (Exception var26) {
                                 var10001 = false;
                                 break label293;
                              }

                              if (var6 != 0) {
                                 var45 = var44;

                                 try {
                                    var50.put("expiry_date", String.format(Locale.US, "%02d.%02d.%d", PassportActivity.this.currentExpireDate[2], PassportActivity.this.currentExpireDate[1], PassportActivity.this.currentExpireDate[0]));
                                 } catch (Exception var25) {
                                    var10001 = false;
                                    break label293;
                                 }
                              } else {
                                 var45 = var44;

                                 try {
                                    var50.put("expiry_date", "");
                                 } catch (Exception var24) {
                                    var10001 = false;
                                    break label293;
                                 }
                              }

                              var45 = var44;

                              JSONObject var48;
                              try {
                                 var48 = new JSONObject();
                              } catch (Exception var20) {
                                 var10001 = false;
                                 break label293;
                              }

                              label297: {
                                 ArrayList var14;
                                 try {
                                    var14 = new ArrayList(var50.keySet());
                                    _$$Lambda$PassportActivity$3$OvzP5ehYJX_5e7BC1WeQR66NW4c var54 = new _$$Lambda$PassportActivity$3$OvzP5ehYJX_5e7BC1WeQR66NW4c(this);
                                    Collections.sort(var14, var54);
                                    var40 = var14.size();
                                 } catch (Exception var19) {
                                    var10001 = false;
                                    break label297;
                                 }

                                 var6 = var4;

                                 while(true) {
                                    var47 = var44;
                                    var45 = var48;
                                    if (var6 >= var40) {
                                       break label261;
                                    }

                                    try {
                                       var42 = (String)var14.get(var6);
                                       var48.put(var42, var50.get(var42));
                                    } catch (Exception var18) {
                                       var10001 = false;
                                       break;
                                    }

                                    ++var6;
                                 }
                              }

                              var47 = var44;
                              var45 = var48;
                              break label261;
                           }

                           var47 = var45;
                           break label260;
                        }

                        var47 = null;
                        var45 = var47;
                        break label261;
                     }

                     var45 = null;
                  }

                  if (PassportActivity.this.fieldsErrors != null) {
                     PassportActivity.this.fieldsErrors.clear();
                  }

                  if (PassportActivity.this.documentsErrors != null) {
                     PassportActivity.this.documentsErrors.clear();
                  }

                  PassportActivity.PassportActivityDelegate var49 = PassportActivity.this.delegate;
                  TLRPC.TL_secureRequiredType var51 = PassportActivity.this.currentType;
                  if (var47 != null) {
                     var41 = var47.toString();
                  } else {
                     var41 = null;
                  }

                  TLRPC.TL_secureRequiredType var53 = PassportActivity.this.currentDocumentsType;
                  if (var45 != null) {
                     var42 = var45.toString();
                  } else {
                     var42 = null;
                  }

                  SecureDocument var15 = PassportActivity.this.selfieDocument;
                  ArrayList var16 = PassportActivity.this.translationDocuments;
                  SecureDocument var17 = PassportActivity.this.frontDocument;
                  SecureDocument var52 = var11;
                  if (PassportActivity.this.reverseLayout != null) {
                     var52 = var11;
                     if (PassportActivity.this.reverseLayout.getVisibility() == 0) {
                        var52 = PassportActivity.this.reverseDocument;
                     }
                  }

                  var49.saveValue(var51, (String)null, var41, var53, var42, (ArrayList)null, var15, var16, var17, var52, var1, var2);
                  return true;
               }
            } else {
               return false;
            }
         }

         // $FF: synthetic method
         public void lambda$null$5$PassportActivity$3(TLRPC.TL_error var1, Runnable var2, PassportActivity.ErrorRunnable var3, TLRPC.TL_account_verifyEmail var4) {
            if (var1 == null) {
               PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, (String)PassportActivity.this.currentValues.get("email"), (String)null, (TLRPC.TL_secureRequiredType)null, (String)null, (ArrayList)null, (SecureDocument)null, (ArrayList)null, (SecureDocument)null, (SecureDocument)null, var2, var3);
            } else {
               AlertsCreator.processError(PassportActivity.access$5600(PassportActivity.this), var1, PassportActivity.this, var4);
               var3.onError((String)null, (String)null);
            }

         }

         // $FF: synthetic method
         public void lambda$onIdentityDone$0$PassportActivity$3(String var1, String var2, String var3, Runnable var4, PassportActivity.ErrorRunnable var5, DialogInterface var6, int var7) {
            PassportActivity.this.inputFields[0].setText(var1);
            PassportActivity.this.inputFields[1].setText(var2);
            PassportActivity.this.inputFields[2].setText(var3);
            PassportActivity.this.showEditDoneProgress(true, true);
            this.onIdentityDone(var4, var5);
         }

         // $FF: synthetic method
         public void lambda$onIdentityDone$1$PassportActivity$3(int var1, DialogInterface var2, int var3) {
            PassportActivity var4 = PassportActivity.this;
            var4.onFieldError(var4.inputFields[var1]);
         }

         // $FF: synthetic method
         public int lambda$onIdentityDone$2$PassportActivity$3(String var1, String var2) {
            int var3 = PassportActivity.this.getFieldCost(var1);
            int var4 = PassportActivity.this.getFieldCost(var2);
            if (var3 < var4) {
               return -1;
            } else {
               return var3 > var4 ? 1 : 0;
            }
         }

         // $FF: synthetic method
         public int lambda$onIdentityDone$3$PassportActivity$3(String var1, String var2) {
            int var3 = PassportActivity.this.getFieldCost(var1);
            int var4 = PassportActivity.this.getFieldCost(var2);
            if (var3 < var4) {
               return -1;
            } else {
               return var3 > var4 ? 1 : 0;
            }
         }

         // $FF: synthetic method
         public void lambda$onItemClick$4$PassportActivity$3() {
            PassportActivity.this.finishFragment();
         }

         // $FF: synthetic method
         public void lambda$onItemClick$6$PassportActivity$3(Runnable var1, PassportActivity.ErrorRunnable var2, TLRPC.TL_account_verifyEmail var3, TLObject var4, TLRPC.TL_error var5) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8(this, var5, var1, var2, var3));
         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (PassportActivity.this.checkDiscard()) {
                  return;
               }

               if (PassportActivity.this.currentActivityType == 0 || PassportActivity.this.currentActivityType == 5) {
                  PassportActivity.this.callCallback(false);
               }

               PassportActivity.this.finishFragment();
            } else {
               String var2 = null;
               if (var1 == 1) {
                  if (PassportActivity.this.getParentActivity() == null) {
                     return;
                  }

                  TextView var17 = new TextView(PassportActivity.this.getParentActivity());
                  String var13 = LocaleController.getString("PassportInfo2", 2131560233);
                  SpannableStringBuilder var11 = new SpannableStringBuilder(var13);
                  var1 = var13.indexOf(42);
                  int var5 = var13.lastIndexOf(42);
                  if (var1 != -1 && var5 != -1) {
                     var11.replace(var5, var5 + 1, "");
                     var11.replace(var1, var1 + 1, "");
                     var11.setSpan(new URLSpanNoUnderline(LocaleController.getString("PassportInfoUrl", 2131560235)) {
                        public void onClick(View var1) {
                           PassportActivity.this.dismissCurrentDialig();
                           super.onClick(var1);
                        }
                     }, var1, var5 - 1, 33);
                  }

                  var17.setText(var11);
                  var17.setTextSize(1, 16.0F);
                  var17.setLinkTextColor(Theme.getColor("dialogTextLink"));
                  var17.setHighlightColor(Theme.getColor("dialogLinkSelection"));
                  var17.setPadding(AndroidUtilities.dp(23.0F), 0, AndroidUtilities.dp(23.0F), 0);
                  var17.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                  var17.setTextColor(Theme.getColor("dialogTextBlack"));
                  AlertDialog.Builder var12 = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
                  var12.setView(var17);
                  var12.setTitle(LocaleController.getString("PassportInfoTitle", 2131560234));
                  var12.setNegativeButton(LocaleController.getString("Close", 2131559117), (OnClickListener)null);
                  PassportActivity.this.showDialog(var12.create());
               } else if (var1 == 2) {
                  if (PassportActivity.this.currentActivityType == 5) {
                     PassportActivity.this.onPasswordDone(false);
                     return;
                  }

                  if (PassportActivity.this.currentActivityType == 7) {
                     PassportActivity.this.views[PassportActivity.this.currentViewNum].onNextPressed();
                  } else {
                     final _$$Lambda$PassportActivity$3$GdH_U_rnd96VkbnTuTp9EkJj_aw var6 = new _$$Lambda$PassportActivity$3$GdH_U_rnd96VkbnTuTp9EkJj_aw(this);
                     PassportActivity.ErrorRunnable var4 = new PassportActivity.ErrorRunnable() {
                        public void onError(String var1, String var2) {
                           if ("PHONE_VERIFICATION_NEEDED".equals(var1)) {
                              PassportActivity var3 = PassportActivity.this;
                              var3.startPhoneVerification(true, var2, var6, this, var3.delegate);
                           } else {
                              PassportActivity.this.showEditDoneProgress(true, false);
                           }

                        }
                     };
                     String var3;
                     if (PassportActivity.this.currentActivityType == 4) {
                        if (PassportActivity.this.useCurrentValue) {
                           var3 = PassportActivity.this.currentEmail;
                        } else {
                           if (PassportActivity.this.checkFieldsForError()) {
                              return;
                           }

                           var3 = PassportActivity.this.inputFields[0].getText().toString();
                        }

                        PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, var3, (String)null, (TLRPC.TL_secureRequiredType)null, (String)null, (ArrayList)null, (SecureDocument)null, (ArrayList)null, (SecureDocument)null, (SecureDocument)null, var6, var4);
                     } else if (PassportActivity.this.currentActivityType == 3) {
                        if (PassportActivity.this.useCurrentValue) {
                           var3 = UserConfig.getInstance(PassportActivity.access$5200(PassportActivity.this)).getCurrentUser().phone;
                        } else {
                           if (PassportActivity.this.checkFieldsForError()) {
                              return;
                           }

                           StringBuilder var14 = new StringBuilder();
                           var14.append(PassportActivity.this.inputFields[1].getText().toString());
                           var14.append(PassportActivity.this.inputFields[2].getText().toString());
                           var3 = var14.toString();
                        }

                        PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, var3, (String)null, (TLRPC.TL_secureRequiredType)null, (String)null, (ArrayList)null, (SecureDocument)null, (ArrayList)null, (SecureDocument)null, (SecureDocument)null, var6, var4);
                     } else if (PassportActivity.this.currentActivityType == 2) {
                        if (!PassportActivity.this.uploadingDocuments.isEmpty() || PassportActivity.this.checkFieldsForError()) {
                           return;
                        }

                        if (PassportActivity.this.isHasNotAnyChanges()) {
                           PassportActivity.this.finishFragment();
                           return;
                        }

                        JSONObject var15;
                        label120: {
                           label119: {
                              try {
                                 if (PassportActivity.this.documentOnly) {
                                    break label119;
                                 }

                                 var15 = new JSONObject();
                              } catch (Exception var10) {
                                 break label119;
                              }

                              try {
                                 var15.put("street_line1", PassportActivity.this.inputFields[0].getText().toString());
                                 var15.put("street_line2", PassportActivity.this.inputFields[1].getText().toString());
                                 var15.put("post_code", PassportActivity.this.inputFields[2].getText().toString());
                                 var15.put("city", PassportActivity.this.inputFields[3].getText().toString());
                                 var15.put("state", PassportActivity.this.inputFields[4].getText().toString());
                                 var15.put("country_code", PassportActivity.this.currentCitizeship);
                              } catch (Exception var9) {
                              }
                              break label120;
                           }

                           var15 = null;
                        }

                        if (PassportActivity.this.fieldsErrors != null) {
                           PassportActivity.this.fieldsErrors.clear();
                        }

                        if (PassportActivity.this.documentsErrors != null) {
                           PassportActivity.this.documentsErrors.clear();
                        }

                        PassportActivity.PassportActivityDelegate var7 = PassportActivity.this.delegate;
                        TLRPC.TL_secureRequiredType var8 = PassportActivity.this.currentType;
                        if (var15 != null) {
                           var2 = var15.toString();
                        }

                        var7.saveValue(var8, (String)null, var2, PassportActivity.this.currentDocumentsType, (String)null, PassportActivity.this.documents, PassportActivity.this.selfieDocument, PassportActivity.this.translationDocuments, (SecureDocument)null, (SecureDocument)null, var6, var4);
                     } else if (PassportActivity.this.currentActivityType == 1) {
                        if (!this.onIdentityDone(var6, var4)) {
                           return;
                        }
                     } else if (PassportActivity.this.currentActivityType == 6) {
                        TLRPC.TL_account_verifyEmail var16 = new TLRPC.TL_account_verifyEmail();
                        var16.email = (String)PassportActivity.this.currentValues.get("email");
                        var16.code = PassportActivity.this.inputFields[0].getText().toString();
                        var1 = ConnectionsManager.getInstance(PassportActivity.access$5300(PassportActivity.this)).sendRequest(var16, new _$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4(this, var6, var4, var16));
                        ConnectionsManager.getInstance(PassportActivity.access$5500(PassportActivity.this)).bindRequestToGuid(var1, PassportActivity.access$5400(PassportActivity.this));
                     }

                     PassportActivity.this.showEditDoneProgress(true, true);
                  }
               }
            }

         }
      });
      if (this.currentActivityType == 7) {
         ScrollView var2 = new ScrollView(var1) {
            protected void onMeasure(int var1, int var2) {
               PassportActivity.this.scrollHeight = MeasureSpec.getSize(var2) - AndroidUtilities.dp(30.0F);
               super.onMeasure(var1, var2);
            }

            protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
               return false;
            }

            public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
               if (PassportActivity.this.currentViewNum == 1 || PassportActivity.this.currentViewNum == 2 || PassportActivity.this.currentViewNum == 4) {
                  var2.bottom += AndroidUtilities.dp(40.0F);
               }

               return super.requestChildRectangleOnScreen(var1, var2, var3);
            }
         };
         this.scrollView = var2;
         super.fragmentView = var2;
         this.scrollView.setFillViewport(true);
         AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
      } else {
         super.fragmentView = new FrameLayout(var1);
         View var3 = super.fragmentView;
         FrameLayout var7 = (FrameLayout)var3;
         var3.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
         this.scrollView = new ScrollView(var1) {
            protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
               return false;
            }

            public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
               var2.offset(var1.getLeft() - var1.getScrollX(), var1.getTop() - var1.getScrollY());
               var2.top += AndroidUtilities.dp(20.0F);
               var2.bottom += AndroidUtilities.dp(50.0F);
               return super.requestChildRectangleOnScreen(var1, var2, var3);
            }
         };
         this.scrollView.setFillViewport(true);
         AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("actionBarDefault"));
         ScrollView var9 = this.scrollView;
         float var4;
         if (this.currentActivityType == 0) {
            var4 = 48.0F;
         } else {
            var4 = 0.0F;
         }

         var7.addView(var9, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, var4));
         this.linearLayout2 = new LinearLayout(var1);
         this.linearLayout2.setOrientation(1);
         this.scrollView.addView(this.linearLayout2, new android.widget.FrameLayout.LayoutParams(-1, -2));
      }

      int var5 = this.currentActivityType;
      if (var5 != 0 && var5 != 8) {
         this.doneItem = super.actionBar.createMenu().addItemWithWidth(2, 2131165439, AndroidUtilities.dp(56.0F));
         this.progressView = new ContextProgressView(var1, 1);
         this.progressView.setAlpha(0.0F);
         this.progressView.setScaleX(0.1F);
         this.progressView.setScaleY(0.1F);
         this.progressView.setVisibility(4);
         this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
         var5 = this.currentActivityType;
         if (var5 == 1 || var5 == 2) {
            ChatAttachAlert var8 = this.chatAttachAlert;
            if (var8 != null) {
               try {
                  if (var8.isShowing()) {
                     this.chatAttachAlert.dismiss();
                  }
               } catch (Exception var6) {
               }

               this.chatAttachAlert.onDestroy();
               this.chatAttachAlert = null;
            }
         }
      }

      var5 = this.currentActivityType;
      if (var5 == 5) {
         this.createPasswordInterface(var1);
      } else if (var5 == 0) {
         this.createRequestInterface(var1);
      } else if (var5 == 1) {
         this.createIdentityInterface(var1);
         this.fillInitialValues();
      } else if (var5 == 2) {
         this.createAddressInterface(var1);
         this.fillInitialValues();
      } else if (var5 == 3) {
         this.createPhoneInterface(var1);
      } else if (var5 == 4) {
         this.createEmailInterface(var1);
      } else if (var5 == 6) {
         this.createEmailVerificationInterface(var1);
      } else if (var5 == 7) {
         this.createPhoneVerificationInterface(var1);
      } else if (var5 == 8) {
         this.createManageInterface(var1);
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.FileDidUpload) {
         String var4 = (String)var3[0];
         SecureDocument var5 = (SecureDocument)this.uploadingDocuments.get(var4);
         if (var5 != null) {
            var5.inputFile = (TLRPC.TL_inputFile)var3[1];
            this.uploadingDocuments.remove(var4);
            if (this.uploadingDocuments.isEmpty()) {
               ActionBarMenuItem var6 = this.doneItem;
               if (var6 != null) {
                  var6.setEnabled(true);
                  this.doneItem.setAlpha(1.0F);
               }
            }

            HashMap var7 = this.documentsCells;
            if (var7 != null) {
               PassportActivity.SecureDocumentCell var8 = (PassportActivity.SecureDocumentCell)var7.get(var5);
               if (var8 != null) {
                  var8.updateButtonState(true);
               }
            }

            var7 = this.errorsValues;
            if (var7 != null && var7.containsKey("error_document_all")) {
               this.errorsValues.remove("error_document_all");
               this.checkTopErrorCell(false);
            }

            var1 = var5.type;
            if (var1 == 0) {
               if (this.bottomCell != null && !TextUtils.isEmpty(this.noAllDocumentsErrorText)) {
                  this.bottomCell.setText(this.noAllDocumentsErrorText);
               }

               this.errorsValues.remove("files_all");
            } else if (var1 == 4) {
               if (this.bottomCellTranslation != null && !TextUtils.isEmpty(this.noAllTranslationErrorText)) {
                  this.bottomCellTranslation.setText(this.noAllTranslationErrorText);
               }

               this.errorsValues.remove("translation_all");
            }
         }
      } else if (var1 != NotificationCenter.FileDidFailUpload) {
         if (var1 == NotificationCenter.didSetTwoStepPassword) {
            if (var3 != null && var3.length > 0) {
               if (var3[7] != null) {
                  EditTextBoldCursor[] var10 = this.inputFields;
                  if (var10[0] != null) {
                     var10[0].setText((String)var3[7]);
                  }
               }

               if (var3[6] == null) {
                  this.currentPassword = new TLRPC.TL_account_password();
                  TLRPC.TL_account_password var11 = this.currentPassword;
                  var11.current_algo = (TLRPC.PasswordKdfAlgo)var3[1];
                  var11.new_secure_algo = (TLRPC.SecurePasswordKdfAlgo)var3[2];
                  var11.secure_random = (byte[])var3[3];
                  var11.has_recovery = TextUtils.isEmpty((String)var3[4]) ^ true;
                  var11 = this.currentPassword;
                  var11.hint = (String)var3[5];
                  var11.srp_id = -1L;
                  var11.srp_B = new byte[256];
                  Utilities.random.nextBytes(var11.srp_B);
                  EditTextBoldCursor[] var9 = this.inputFields;
                  if (var9[0] != null && var9[0].length() > 0) {
                     this.usingSavedPassword = 2;
                  }
               }
            } else {
               this.currentPassword = null;
               this.loadPasswordInfo();
            }

            this.updatePasswordInterface();
         } else {
            var1 = NotificationCenter.didRemoveTwoStepPassword;
         }
      }

   }

   public void dismissCurrentDialig() {
      ChatAttachAlert var1 = this.chatAttachAlert;
      if (var1 != null && super.visibleDialog == var1) {
         var1.closeCamera(false);
         this.chatAttachAlert.dismissInternal();
         this.chatAttachAlert.hideCamera(true);
      } else {
         super.dismissCurrentDialig();
      }
   }

   public boolean dismissDialogOnPause(Dialog var1) {
      boolean var2;
      if (var1 != this.chatAttachAlert && super.dismissDialogOnPause(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
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
      var1.add(new ThemeDescription(this.extraBackgroundView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      View var6 = this.extraBackgroundView2;
      if (var6 != null) {
         var1.add(new ThemeDescription(var6, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      }

      int var4;
      for(var4 = 0; var4 < this.dividers.size(); ++var4) {
         var1.add(new ThemeDescription((View)this.dividers.get(var4), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      }

      Iterator var9 = this.documentsCells.entrySet().iterator();

      while(var9.hasNext()) {
         PassportActivity.SecureDocumentCell var7 = (PassportActivity.SecureDocumentCell)((Entry)var9.next()).getValue();
         var1.add(new ThemeDescription(var7, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{PassportActivity.SecureDocumentCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
         var1.add(new ThemeDescription(var7, 0, new Class[]{PassportActivity.SecureDocumentCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(var7, 0, new Class[]{PassportActivity.SecureDocumentCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
      }

      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextDetailSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_SELECTORWHITE, new Class[]{PassportActivity.TextDetailSecureCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PassportActivity.TextDetailSecureCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PassportActivity.TextDetailSecureCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PassportActivity.TextDetailSecureCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{PassportActivity.TextDetailSecureCell.class}, new String[]{"checkImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
      var1.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
      var1.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"));
      int var5;
      EditTextBoldCursor[] var8;
      EditTextBoldCursor var10;
      if (this.inputFields != null) {
         var4 = 0;

         while(true) {
            var8 = this.inputFields;
            if (var4 >= var8.length) {
               break;
            }

            var1.add(new ThemeDescription((View)var8[var4].getParent(), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var1.add(new ThemeDescription(this.inputFields[var4], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var10 = this.inputFields[var4];
            var5 = ThemeDescription.FLAG_BACKGROUNDFILTER;
            var1.add(new ThemeDescription(var10, ThemeDescription.FLAG_PROGRESSBAR | var5, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText3"));
            ++var4;
         }
      } else {
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
         var4 = ThemeDescription.FLAG_HINTTEXTCOLOR;
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_PROGRESSBAR | var4, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
         var4 = ThemeDescription.FLAG_BACKGROUNDFILTER;
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | var4, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
         var1.add(new ThemeDescription((View)null, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText3"));
      }

      if (this.inputExtraFields != null) {
         var4 = 0;

         while(true) {
            var8 = this.inputExtraFields;
            if (var4 >= var8.length) {
               break;
            }

            var1.add(new ThemeDescription((View)var8[var4].getParent(), ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var1.add(new ThemeDescription(this.inputExtraFields[var4], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CURSORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var1.add(new ThemeDescription(this.inputExtraFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var1.add(new ThemeDescription(this.inputExtraFields[var4], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"));
            var1.add(new ThemeDescription(this.inputExtraFields[var4], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var1.add(new ThemeDescription(this.inputExtraFields[var4], ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var10 = this.inputExtraFields[var4];
            var5 = ThemeDescription.FLAG_BACKGROUNDFILTER;
            var1.add(new ThemeDescription(var10, ThemeDescription.FLAG_PROGRESSBAR | var5, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText3"));
            ++var4;
         }
      }

      var1.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
      var1.add(new ThemeDescription(this.noPasswordImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_messagePanelIcons"));
      var1.add(new ThemeDescription(this.noPasswordTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"));
      var1.add(new ThemeDescription(this.noPasswordSetTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText5"));
      var1.add(new ThemeDescription(this.passwordForgotButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      var1.add(new ThemeDescription(this.plusTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var1.add(new ThemeDescription(this.acceptTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeText"));
      var1.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeBackground"));
      FrameLayout var11 = this.bottomLayout;
      var4 = ThemeDescription.FLAG_BACKGROUNDFILTER;
      var1.add(new ThemeDescription(var11, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | var4, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeBackgroundSelected"));
      var1.add(new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"));
      var1.add(new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"));
      var1.add(new ThemeDescription(this.progressViewButton, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"));
      var1.add(new ThemeDescription(this.progressViewButton, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"));
      var1.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sessions_devicesImage"));
      var1.add(new ThemeDescription(this.emptyTextView1, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
      var1.add(new ThemeDescription(this.emptyTextView2, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
      var1.add(new ThemeDescription(this.emptyTextView3, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
      return (ThemeDescription[])var1.toArray(new ThemeDescription[0]);
   }

   // $FF: synthetic method
   public void lambda$addDocumentView$56$PassportActivity(int var1, View var2) {
      this.uploadingFileType = var1;
      if (var1 == 1) {
         this.currentPhotoViewerLayout = this.selfieLayout;
      } else if (var1 == 4) {
         this.currentPhotoViewerLayout = this.translationLayout;
      } else if (var1 == 2) {
         this.currentPhotoViewerLayout = this.frontLayout;
      } else if (var1 == 3) {
         this.currentPhotoViewerLayout = this.reverseLayout;
      } else {
         this.currentPhotoViewerLayout = this.documentsLayout;
      }

      SecureDocument var5 = (SecureDocument)var2.getTag();
      PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
      PhotoViewer var3;
      ArrayList var4;
      if (var1 == 0) {
         var3 = PhotoViewer.getInstance();
         var4 = this.documents;
         var3.openPhoto(var4, var4.indexOf(var5), this.provider);
      } else {
         var3 = PhotoViewer.getInstance();
         var4 = this.translationDocuments;
         var3.openPhoto(var4, var4.indexOf(var5), this.provider);
      }

   }

   // $FF: synthetic method
   public boolean lambda$addDocumentView$58$PassportActivity(int var1, SecureDocument var2, PassportActivity.SecureDocumentCell var3, String var4, View var5) {
      AlertDialog.Builder var6 = new AlertDialog.Builder(this.getParentActivity());
      if (var1 == 1) {
         var6.setMessage(LocaleController.getString("PassportDeleteSelfie", 2131560210));
      } else {
         var6.setMessage(LocaleController.getString("PassportDeleteScan", 2131560208));
      }

      var6.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var6.setTitle(LocaleController.getString("AppName", 2131558635));
      var6.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PassportActivity$Aj_Wenld4quF9ly9Ap8983X_MQo(this, var2, var1, var3, var4));
      this.showDialog(var6.create());
      return true;
   }

   // $FF: synthetic method
   public void lambda$addField$65$PassportActivity(ArrayList var1, TLRPC.TL_secureRequiredType var2, boolean var3, View var4) {
      int var5;
      int var6;
      TLRPC.TL_secureRequiredType var11;
      label95: {
         if (var1 != null) {
            var5 = var1.size();

            for(var6 = 0; var6 < var5; ++var6) {
               TLRPC.TL_secureRequiredType var7 = (TLRPC.TL_secureRequiredType)var1.get(var6);
               var11 = var7;
               if (this.getValueByType(var7, false) != null) {
                  break label95;
               }

               if (var5 == 1) {
                  var11 = var7;
                  break label95;
               }
            }
         }

         var11 = null;
      }

      TLRPC.SecureValueType var13 = var2.type;
      AlertDialog.Builder var12;
      if (!(var13 instanceof TLRPC.TL_secureValueTypePersonalDetails) && !(var13 instanceof TLRPC.TL_secureValueTypeAddress)) {
         boolean var8 = var13 instanceof TLRPC.TL_secureValueTypePhone;
         if ((var8 || var13 instanceof TLRPC.TL_secureValueTypeEmail) && this.getValueByType(var2, false) != null) {
            var12 = new AlertDialog.Builder(this.getParentActivity());
            var12.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PassportActivity$SBPx7fdXp_MbcirnzsQkO7Z6gg0(this, var2, var3));
            var12.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            var12.setTitle(LocaleController.getString("AppName", 2131558635));
            String var10;
            if (var8) {
               var6 = 2131560207;
               var10 = "PassportDeletePhoneAlert";
            } else {
               var6 = 2131560204;
               var10 = "PassportDeleteEmailAlert";
            }

            var12.setMessage(LocaleController.getString(var10, var6));
            this.showDialog(var12.create());
            return;
         }
      } else if (var11 == null && var1 != null && !var1.isEmpty()) {
         var12 = new AlertDialog.Builder(this.getParentActivity());
         var12.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var13 = var2.type;
         if (var13 instanceof TLRPC.TL_secureValueTypePersonalDetails) {
            var12.setTitle(LocaleController.getString("PassportIdentityDocument", 2131560227));
         } else if (var13 instanceof TLRPC.TL_secureValueTypeAddress) {
            var12.setTitle(LocaleController.getString("PassportAddress", 2131560189));
         }

         ArrayList var9 = new ArrayList();
         var5 = var1.size();

         for(var6 = 0; var6 < var5; ++var6) {
            var13 = ((TLRPC.TL_secureRequiredType)var1.get(var6)).type;
            if (var13 instanceof TLRPC.TL_secureValueTypeDriverLicense) {
               var9.add(LocaleController.getString("PassportAddLicence", 2131560176));
            } else if (var13 instanceof TLRPC.TL_secureValueTypePassport) {
               var9.add(LocaleController.getString("PassportAddPassport", 2131560177));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeInternalPassport) {
               var9.add(LocaleController.getString("PassportAddInternalPassport", 2131560174));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeIdentityCard) {
               var9.add(LocaleController.getString("PassportAddCard", 2131560171));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeUtilityBill) {
               var9.add(LocaleController.getString("PassportAddBill", 2131560169));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeBankStatement) {
               var9.add(LocaleController.getString("PassportAddBank", 2131560167));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
               var9.add(LocaleController.getString("PassportAddAgreement", 2131560165));
            } else if (var13 instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
               var9.add(LocaleController.getString("PassportAddTemporaryRegistration", 2131560181));
            } else if (var13 instanceof TLRPC.TL_secureValueTypePassportRegistration) {
               var9.add(LocaleController.getString("PassportAddPassportRegistration", 2131560179));
            }
         }

         var12.setItems((CharSequence[])var9.toArray(new CharSequence[0]), new _$$Lambda$PassportActivity$oX34r1oAXwJXLUOfYXGW2rjRrMs(this, var2, var1, var3));
         this.showDialog(var12.create());
         return;
      }

      this.openTypeActivity(var2, var11, var1, var3);
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$70$PassportActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$checkNativeFields$59$PassportActivity() {
      EditTextBoldCursor[] var1 = this.inputExtraFields;
      if (var1 != null) {
         this.scrollToField(var1[0]);
      }

   }

   // $FF: synthetic method
   public void lambda$createAddressInterface$32$PassportActivity(View var1) {
      this.uploadingFileType = 0;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public void lambda$createAddressInterface$33$PassportActivity(View var1) {
      this.uploadingFileType = 4;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public boolean lambda$createAddressInterface$35$PassportActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            CountrySelectActivity var3 = new CountrySelectActivity(false);
            var3.setCountrySelectActivityDelegate(new _$$Lambda$PassportActivity$FVAHEan3uDvz73NCL5GKR6mgp_0(this));
            this.presentFragment(var3);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createAddressInterface$36$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         var2 = (Integer)var1.getTag() + 1;
         EditTextBoldCursor[] var4 = this.inputFields;
         if (var2 < var4.length) {
            if (var4[var2].isFocusable()) {
               this.inputFields[var2].requestFocus();
            } else {
               this.inputFields[var2].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0F, 0.0F, 0));
               var1.clearFocus();
               AndroidUtilities.hideKeyboard(var1);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createAddressInterface$37$PassportActivity(View var1) {
      this.createDocumentDeleteAlert();
   }

   // $FF: synthetic method
   public void lambda$createDocumentDeleteAlert$38$PassportActivity(boolean[] var1, DialogInterface var2, int var3) {
      if (!this.documentOnly) {
         this.currentValues.clear();
      }

      this.currentDocumentValues.clear();
      this.delegate.deleteValue(this.currentType, this.currentDocumentsType, this.availableDocumentTypes, var1[0], (Runnable)null, (PassportActivity.ErrorRunnable)null);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createEmailInterface$24$PassportActivity(View var1) {
      this.useCurrentValue = true;
      this.doneItem.callOnClick();
      this.useCurrentValue = false;
   }

   // $FF: synthetic method
   public boolean lambda$createEmailInterface$25$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 6 && var2 != 5) {
         return false;
      } else {
         this.doneItem.callOnClick();
         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createEmailVerificationInterface$5$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 6 && var2 != 5) {
         return false;
      } else {
         this.doneItem.callOnClick();
         return true;
      }
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$40$PassportActivity(View var1) {
      this.uploadingFileType = 2;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$41$PassportActivity(View var1) {
      this.uploadingFileType = 3;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$42$PassportActivity(View var1) {
      this.uploadingFileType = 1;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$43$PassportActivity(View var1) {
      this.uploadingFileType = 4;
      this.openAttachMenu();
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$45$PassportActivity(View var1) {
      if (VERSION.SDK_INT >= 23 && this.getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
         this.getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 22);
      } else {
         MrzCameraActivity var2 = new MrzCameraActivity();
         var2.setDelegate(new _$$Lambda$PassportActivity$jIvyCfLzFIjgzntur0HgCfk6_Pc(this));
         this.presentFragment(var2);
      }
   }

   // $FF: synthetic method
   public boolean lambda$createIdentityInterface$47$PassportActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            CountrySelectActivity var3 = new CountrySelectActivity(false);
            var3.setCountrySelectActivityDelegate(new _$$Lambda$PassportActivity$bEhtGHiukso4FocfB8ZhvxICXI8(this, var1));
            this.presentFragment(var3);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createIdentityInterface$50$PassportActivity(Context var1, View var2, MotionEvent var3) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var3.getAction() == 1) {
            Calendar var23 = Calendar.getInstance();
            var23.get(1);
            var23.get(2);
            var23.get(5);

            Exception var10000;
            label81: {
               int var4;
               boolean var10001;
               EditTextBoldCursor var24;
               try {
                  var24 = (EditTextBoldCursor)var2;
                  var4 = (Integer)var24.getTag();
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label81;
               }

               byte var5;
               byte var6;
               byte var7;
               String var22;
               if (var4 == 8) {
                  try {
                     var22 = LocaleController.getString("PassportSelectExpiredDate", 2131560325);
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label81;
                  }

                  var5 = 0;
                  var6 = 20;
                  var7 = 0;
               } else {
                  try {
                     var22 = LocaleController.getString("PassportSelectBithdayDate", 2131560324);
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label81;
                  }

                  var5 = -120;
                  var6 = 0;
                  var7 = -18;
               }

               int var9;
               int var10;
               int var11;
               label62: {
                  try {
                     String[] var8 = var24.getText().toString().split("\\.");
                     if (var8.length == 3) {
                        var9 = Utilities.parseInt(var8[0]);
                        var10 = Utilities.parseInt(var8[1]);
                        var11 = Utilities.parseInt(var8[2]);
                        break label62;
                     }
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label81;
                  }

                  var9 = -1;
                  var10 = -1;
                  var11 = -1;
               }

               boolean var12;
               if (var4 == 8) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               AlertDialog.Builder var20;
               try {
                  _$$Lambda$PassportActivity$PBoR_i1NCFH_2XahUVDKl9oJw5I var25 = new _$$Lambda$PassportActivity$PBoR_i1NCFH_2XahUVDKl9oJw5I(this, var4, var24);
                  var20 = AlertsCreator.createDatePickerDialog(var1, var5, var6, var7, var9, var10, var11, var22, var12, var25);
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label81;
               }

               if (var4 == 8) {
                  try {
                     var22 = LocaleController.getString("PassportSelectNotExpire", 2131560327);
                     _$$Lambda$PassportActivity$hkQKt9FoeeVbVy2IQ1nJvNtE5mU var26 = new _$$Lambda$PassportActivity$hkQKt9FoeeVbVy2IQ1nJvNtE5mU(this, var24);
                     var20.setNegativeButton(var22, var26);
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label81;
                  }
               }

               try {
                  this.showDialog(var20.create());
                  return true;
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
               }
            }

            Exception var21 = var10000;
            FileLog.e((Throwable)var21);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createIdentityInterface$52$PassportActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
            var5.setTitle(LocaleController.getString("PassportSelectGender", 2131560326));
            String var3 = LocaleController.getString("PassportMale", 2131560284);
            String var4 = LocaleController.getString("PassportFemale", 2131560223);
            _$$Lambda$PassportActivity$xyrt_3Facds_cGPGxpzwS2nYi4s var6 = new _$$Lambda$PassportActivity$xyrt_3Facds_cGPGxpzwS2nYi4s(this);
            var5.setItems(new CharSequence[]{var3, var4}, var6);
            var5.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            this.showDialog(var5.create());
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createIdentityInterface$53$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         var2 = (Integer)var1.getTag() + 1;
         EditTextBoldCursor[] var4 = this.inputFields;
         if (var2 < var4.length) {
            if (var4[var2].isFocusable()) {
               this.inputFields[var2].requestFocus();
            } else {
               this.inputFields[var2].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0F, 0.0F, 0));
               var1.clearFocus();
               AndroidUtilities.hideKeyboard(var1);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createIdentityInterface$54$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         var2 = (Integer)var1.getTag() + 1;
         EditTextBoldCursor[] var4 = this.inputExtraFields;
         if (var2 < var4.length) {
            if (var4[var2].isFocusable()) {
               this.inputExtraFields[var2].requestFocus();
            } else {
               this.inputExtraFields[var2].dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 1, 0.0F, 0.0F, 0));
               var1.clearFocus();
               AndroidUtilities.hideKeyboard(var1);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createIdentityInterface$55$PassportActivity(View var1) {
      this.createDocumentDeleteAlert();
   }

   // $FF: synthetic method
   public void lambda$createManageInterface$17$PassportActivity(View var1) {
      this.openAddDocumentAlert();
   }

   // $FF: synthetic method
   public void lambda$createManageInterface$21$PassportActivity(View var1) {
      AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
      var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PassportActivity$TerssJhszDc47oVgt9PLJkXFg6k(this));
      var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var2.setTitle(LocaleController.getString("AppName", 2131558635));
      var2.setMessage(LocaleController.getString("TelegramPassportDeleteAlert", 2131560876));
      this.showDialog(var2.create());
   }

   // $FF: synthetic method
   public void lambda$createManageInterface$22$PassportActivity(View var1) {
      this.openAddDocumentAlert();
   }

   // $FF: synthetic method
   public void lambda$createPasswordInterface$12$PassportActivity(View var1) {
      if (this.currentPassword.has_recovery) {
         this.needShowProgress();
         TLRPC.TL_auth_requestPasswordRecovery var3 = new TLRPC.TL_auth_requestPasswordRecovery();
         int var2 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$PassportActivity$lgU77TEJCVB2ahunM2AyZkIsQ_Q(this), 10);
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var2, super.classGuid);
      } else {
         if (this.getParentActivity() == null) {
            return;
         }

         AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var4.setNegativeButton(LocaleController.getString("RestorePasswordResetAccount", 2131560613), new _$$Lambda$PassportActivity$I_dc4hA_LGNwWgzhXirKJ8kizjk(this));
         var4.setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", 2131560612));
         var4.setMessage(LocaleController.getString("RestorePasswordNoEmailText", 2131560611));
         this.showDialog(var4.create());
      }

   }

   // $FF: synthetic method
   public void lambda$createPasswordInterface$6$PassportActivity(View var1) {
      TwoStepVerificationActivity var2 = new TwoStepVerificationActivity(super.currentAccount, 1);
      var2.setCloseAfterSet(true);
      TLRPC.TL_account_password var3 = this.currentPassword;
      var2.setCurrentPasswordInfo(new byte[0], var3);
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public boolean lambda$createPasswordInterface$7$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 != 5 && var2 != 6) {
         return false;
      } else {
         this.doneItem.callOnClick();
         return true;
      }
   }

   // $FF: synthetic method
   public void lambda$createPhoneInterface$26$PassportActivity(View var1) {
      this.useCurrentValue = true;
      this.doneItem.callOnClick();
      this.useCurrentValue = false;
   }

   // $FF: synthetic method
   public boolean lambda$createPhoneInterface$29$PassportActivity(View var1, MotionEvent var2) {
      if (this.getParentActivity() == null) {
         return false;
      } else {
         if (var2.getAction() == 1) {
            CountrySelectActivity var3 = new CountrySelectActivity(false);
            var3.setCountrySelectActivityDelegate(new _$$Lambda$PassportActivity$BwoW6ssGVQlkA38H5aqDej4Yyd0(this));
            this.presentFragment(var3);
         }

         return true;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createPhoneInterface$30$PassportActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         this.inputFields[2].requestFocus();
         return true;
      } else if (var2 == 6) {
         this.doneItem.callOnClick();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createPhoneInterface$31$PassportActivity(View var1, int var2, KeyEvent var3) {
      if (var2 == 67 && this.inputFields[2].length() == 0) {
         this.inputFields[1].requestFocus();
         EditTextBoldCursor[] var4 = this.inputFields;
         var4[1].setSelection(var4[1].length());
         this.inputFields[1].dispatchKeyEvent(var3);
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$createRequestInterface$16$PassportActivity(View var1) {
      ArrayList var2 = new ArrayList();
      int var3 = this.currentForm.required_types.size();

      int var4;
      int var6;
      int var7;
      for(var4 = 0; var4 < var3; ++var4) {
         TLRPC.SecureRequiredType var81 = (TLRPC.SecureRequiredType)this.currentForm.required_types.get(var4);
         TLRPC.TL_secureRequiredType var82;
         if (var81 instanceof TLRPC.TL_secureRequiredType) {
            var82 = (TLRPC.TL_secureRequiredType)var81;
         } else {
            if (!(var81 instanceof TLRPC.TL_secureRequiredTypeOneOf)) {
               continue;
            }

            TLRPC.TL_secureRequiredTypeOneOf var5 = (TLRPC.TL_secureRequiredTypeOneOf)var81;
            if (var5.types.isEmpty()) {
               continue;
            }

            var81 = (TLRPC.SecureRequiredType)var5.types.get(0);
            if (!(var81 instanceof TLRPC.TL_secureRequiredType)) {
               continue;
            }

            var82 = (TLRPC.TL_secureRequiredType)var81;
            var6 = var5.types.size();

            for(var7 = 0; var7 < var6; ++var7) {
               TLRPC.SecureRequiredType var8 = (TLRPC.SecureRequiredType)var5.types.get(var7);
               if (var8 instanceof TLRPC.TL_secureRequiredType) {
                  TLRPC.TL_secureRequiredType var92 = (TLRPC.TL_secureRequiredType)var8;
                  if (this.getValueByType(var92, true) != null) {
                     var82 = var92;
                     break;
                  }
               }
            }
         }

         TLRPC.TL_secureValue var93 = this.getValueByType(var82, true);
         Vibrator var83;
         if (var93 == null) {
            var83 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
            if (var83 != null) {
               var83.vibrate(200L);
            }

            AndroidUtilities.shakeView(this.getViewByType(var82), 2.0F, 0);
            return;
         }

         String var87 = this.getNameForType(var82.type);
         HashMap var88 = (HashMap)this.errorsMap.get(var87);
         if (var88 != null && !var88.isEmpty()) {
            var83 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
            if (var83 != null) {
               var83.vibrate(200L);
            }

            AndroidUtilities.shakeView(this.getViewByType(var82), 2.0F, 0);
            return;
         }

         var2.add(new PassportActivity$1ValueToSend(this, var93, var82.selfie_required, var82.translation_required));
      }

      this.showEditDoneProgress(false, true);
      TLRPC.TL_account_acceptAuthorization var94 = new TLRPC.TL_account_acceptAuthorization();
      var94.bot_id = this.currentBotId;
      var94.scope = this.currentScope;
      var94.public_key = this.currentPublicKey;
      JSONObject var89 = new JSONObject();
      var4 = var2.size();
      var3 = 0;

      for(ArrayList var85 = var2; var3 < var4; ++var3) {
         TLRPC.TL_secureValue var10;
         label556: {
            PassportActivity$1ValueToSend var9 = (PassportActivity$1ValueToSend)var85.get(var3);
            var10 = var9.value;
            JSONObject var11 = new JSONObject();
            TLRPC.SecurePlainData var12 = var10.plain_data;
            if (var12 != null) {
               if (var12 instanceof TLRPC.TL_securePlainEmail) {
                  TLRPC.TL_securePlainEmail var84 = (TLRPC.TL_securePlainEmail)var12;
                  var2 = var85;
                  var7 = var4;
               } else {
                  var2 = var85;
                  var7 = var4;
                  if (var12 instanceof TLRPC.TL_securePlainPhone) {
                     TLRPC.TL_securePlainPhone var86 = (TLRPC.TL_securePlainPhone)var12;
                     var7 = var4;
                     var2 = var85;
                  }
               }
            } else {
               label606: {
                  var2 = var85;
                  var7 = var4;

                  boolean var10001;
                  JSONObject var104;
                  try {
                     var104 = new JSONObject;
                  } catch (Exception var80) {
                     var10001 = false;
                     break label606;
                  }

                  var2 = var85;
                  var7 = var4;

                  try {
                     var104.<init>();
                  } catch (Exception var79) {
                     var10001 = false;
                     break label606;
                  }

                  var2 = var85;
                  var7 = var4;

                  TLRPC.TL_secureData var13;
                  try {
                     var13 = var10.data;
                  } catch (Exception var78) {
                     var10001 = false;
                     break label606;
                  }

                  byte[] var105;
                  if (var13 != null) {
                     var2 = var85;
                     var7 = var4;

                     try {
                        var105 = this.decryptValueSecret(var10.data.secret, var10.data.data_hash);
                     } catch (Exception var77) {
                        var10001 = false;
                        break label606;
                     }

                     var2 = var85;
                     var7 = var4;

                     try {
                        var11.put("data_hash", Base64.encodeToString(var10.data.data_hash, 2));
                     } catch (Exception var76) {
                        var10001 = false;
                        break label606;
                     }

                     var2 = var85;
                     var7 = var4;

                     try {
                        var11.put("secret", Base64.encodeToString(var105, 2));
                     } catch (Exception var75) {
                        var10001 = false;
                        break label606;
                     }

                     var2 = var85;
                     var7 = var4;

                     try {
                        var104.put("data", var11);
                     } catch (Exception var74) {
                        var10001 = false;
                        break label606;
                     }
                  }

                  var2 = var85;
                  var7 = var4;

                  boolean var14;
                  try {
                     var14 = var10.files.isEmpty();
                  } catch (Exception var73) {
                     var10001 = false;
                     break label606;
                  }

                  int var15;
                  byte[] var16;
                  TLRPC.TL_secureFile var106;
                  if (var14) {
                     var7 = var4;
                     var2 = var85;
                  } else {
                     var2 = var85;
                     var7 = var4;

                     JSONArray var100;
                     try {
                        var100 = new JSONArray;
                     } catch (Exception var65) {
                        var10001 = false;
                        break label606;
                     }

                     var2 = var85;
                     var7 = var4;

                     try {
                        var100.<init>();
                     } catch (Exception var64) {
                        var10001 = false;
                        break label606;
                     }

                     var2 = var85;
                     var7 = var4;

                     try {
                        var6 = var10.files.size();
                     } catch (Exception var63) {
                        var10001 = false;
                        break label606;
                     }

                     var15 = 0;

                     while(true) {
                        if (var15 >= var6) {
                           var2 = var85;
                           var7 = var4;
                           var85 = var85;
                           var4 = var4;

                           try {
                              var104.put("files", var100);
                              break;
                           } catch (Exception var66) {
                              var10001 = false;
                              break label556;
                           }
                        }

                        var2 = var85;
                        var7 = var4;

                        try {
                           var106 = (TLRPC.TL_secureFile)var10.files.get(var15);
                        } catch (Exception var62) {
                           var10001 = false;
                           break label606;
                        }

                        var2 = var85;

                        try {
                           var16 = var106.secret;
                        } catch (Exception var22) {
                           var85 = var85;
                           break label556;
                        }

                        var7 = var4;
                        var85 = var85;
                        var4 = var4;

                        try {
                           var16 = this.decryptValueSecret(var16, var106.file_hash);
                        } catch (Exception var72) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        JSONObject var17;
                        try {
                           var17 = new JSONObject;
                        } catch (Exception var71) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var17.<init>();
                        } catch (Exception var70) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var17.put("file_hash", Base64.encodeToString(var106.file_hash, 2));
                        } catch (Exception var69) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var17.put("secret", Base64.encodeToString(var16, 2));
                        } catch (Exception var68) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var100.put(var17);
                        } catch (Exception var67) {
                           var10001 = false;
                           break label556;
                        }

                        ++var15;
                        var85 = var2;
                        var4 = var7;
                     }
                  }

                  var85 = var2;
                  var4 = var7;

                  TLRPC.TL_secureFile var101;
                  label593: {
                     try {
                        if (!(var10.front_side instanceof TLRPC.TL_secureFile)) {
                           break label593;
                        }
                     } catch (Exception var61) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var101 = (TLRPC.TL_secureFile)var10.front_side;
                     } catch (Exception var60) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var16 = this.decryptValueSecret(var101.secret, var101.file_hash);
                     } catch (Exception var59) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     JSONObject var107;
                     try {
                        var107 = new JSONObject;
                     } catch (Exception var58) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var107.<init>();
                     } catch (Exception var57) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var107.put("file_hash", Base64.encodeToString(var101.file_hash, 2));
                     } catch (Exception var56) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var107.put("secret", Base64.encodeToString(var16, 2));
                     } catch (Exception var55) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var104.put("front_side", var107);
                     } catch (Exception var54) {
                        var10001 = false;
                        break label556;
                     }
                  }

                  var85 = var2;
                  var4 = var7;

                  JSONObject var109;
                  label594: {
                     try {
                        if (!(var10.reverse_side instanceof TLRPC.TL_secureFile)) {
                           break label594;
                        }
                     } catch (Exception var53) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var106 = (TLRPC.TL_secureFile)var10.reverse_side;
                     } catch (Exception var52) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     byte[] var103;
                     try {
                        var103 = this.decryptValueSecret(var106.secret, var106.file_hash);
                     } catch (Exception var51) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var109 = new JSONObject;
                     } catch (Exception var50) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var109.<init>();
                     } catch (Exception var49) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var109.put("file_hash", Base64.encodeToString(var106.file_hash, 2));
                     } catch (Exception var48) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var109.put("secret", Base64.encodeToString(var103, 2));
                     } catch (Exception var47) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var104.put("reverse_side", var109);
                     } catch (Exception var46) {
                        var10001 = false;
                        break label556;
                     }
                  }

                  var85 = var2;
                  var4 = var7;

                  label595: {
                     try {
                        if (!var9.selfie_required) {
                           break label595;
                        }
                     } catch (Exception var45) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        if (!(var10.selfie instanceof TLRPC.TL_secureFile)) {
                           break label595;
                        }
                     } catch (Exception var44) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     TLRPC.TL_secureFile var110;
                     try {
                        var110 = (TLRPC.TL_secureFile)var10.selfie;
                     } catch (Exception var43) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var105 = this.decryptValueSecret(var110.secret, var110.file_hash);
                     } catch (Exception var42) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var11 = new JSONObject;
                     } catch (Exception var41) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var11.<init>();
                     } catch (Exception var40) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var11.put("file_hash", Base64.encodeToString(var110.file_hash, 2));
                     } catch (Exception var39) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var11.put("secret", Base64.encodeToString(var105, 2));
                     } catch (Exception var38) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var104.put("selfie", var11);
                     } catch (Exception var37) {
                        var10001 = false;
                        break label556;
                     }
                  }

                  var85 = var2;
                  var4 = var7;

                  label596: {
                     try {
                        if (!var9.translation_required) {
                           break label596;
                        }
                     } catch (Exception var36) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        if (var10.translation.isEmpty()) {
                           break label596;
                        }
                     } catch (Exception var35) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     JSONArray var108;
                     try {
                        var108 = new JSONArray;
                     } catch (Exception var34) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var108.<init>();
                     } catch (Exception var33) {
                        var10001 = false;
                        break label556;
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var15 = var10.translation.size();
                     } catch (Exception var32) {
                        var10001 = false;
                        break label556;
                     }

                     for(var6 = 0; var6 < var15; ++var6) {
                        var85 = var2;
                        var4 = var7;

                        try {
                           var101 = (TLRPC.TL_secureFile)var10.translation.get(var6);
                        } catch (Exception var31) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        byte[] var95;
                        try {
                           var95 = this.decryptValueSecret(var101.secret, var101.file_hash);
                        } catch (Exception var30) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var109 = new JSONObject;
                        } catch (Exception var29) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var109.<init>();
                        } catch (Exception var28) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var109.put("file_hash", Base64.encodeToString(var101.file_hash, 2));
                        } catch (Exception var27) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var109.put("secret", Base64.encodeToString(var95, 2));
                        } catch (Exception var26) {
                           var10001 = false;
                           break label556;
                        }

                        var85 = var2;
                        var4 = var7;

                        try {
                           var108.put(var109);
                        } catch (Exception var25) {
                           var10001 = false;
                           break label556;
                        }
                     }

                     var85 = var2;
                     var4 = var7;

                     try {
                        var104.put("translation", var108);
                     } catch (Exception var24) {
                        var10001 = false;
                        break label556;
                     }
                  }

                  var85 = var2;
                  var4 = var7;

                  try {
                     var89.put(this.getNameForType(var10.type), var104);
                  } catch (Exception var23) {
                     var10001 = false;
                     break label556;
                  }

                  var4 = var7;
                  var85 = var2;
                  break label556;
               }
            }

            var85 = var2;
            var4 = var7;
         }

         TLRPC.TL_secureValueHash var96 = new TLRPC.TL_secureValueHash();
         var96.type = var10.type;
         var96.hash = var10.hash;
         var94.value_hashes.add(var96);
      }

      JSONObject var111 = new JSONObject();

      try {
         var111.put("secure_data", var89);
      } catch (Exception var21) {
      }

      String var98 = this.currentPayload;
      if (var98 != null) {
         try {
            var111.put("payload", var98);
         } catch (Exception var20) {
         }
      }

      var98 = this.currentNonce;
      if (var98 != null) {
         try {
            var111.put("nonce", var98);
         } catch (Exception var19) {
         }
      }

      PassportActivity.EncryptionResult var112 = this.encryptData(AndroidUtilities.getStringBytes(var111.toString()));
      var94.credentials = new TLRPC.TL_secureCredentialsEncrypted();
      TLRPC.TL_secureCredentialsEncrypted var99 = var94.credentials;
      var99.hash = var112.fileHash;
      var99.data = var112.encryptedData;

      try {
         var98 = this.currentPublicKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
         KeyFactory var97 = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec var90 = new X509EncodedKeySpec(Base64.decode(var98, 0));
         RSAPublicKey var91 = (RSAPublicKey)var97.generatePublic(var90);
         Cipher var102 = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
         var102.init(1, var91);
         var94.credentials.secret = var102.doFinal(var112.decrypyedFileSecret);
      } catch (Exception var18) {
         FileLog.e((Throwable)var18);
      }

      var4 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var94, new _$$Lambda$PassportActivity$OXx8osKL_PnehH20iIhS6RoDsW4(this));
      ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var4, super.classGuid);
   }

   // $FF: synthetic method
   public void lambda$deleteValueInternal$61$PassportActivity(PassportActivity.ErrorRunnable var1, boolean var2, TLRPC.TL_secureRequiredType var3, TLRPC.TL_secureRequiredType var4, boolean var5, ArrayList var6, Runnable var7, TLObject var8, TLRPC.TL_error var9) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$b8qjtHw_SwhVCQIFBk7EnKFy_9c(this, var9, var1, var2, var3, var4, var5, var6, var7));
   }

   // $FF: synthetic method
   public void lambda$loadPasswordInfo$4$PassportActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$QH_KW6UpOuxsybHXF9jp7bpwNpc(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$10$PassportActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$6lQsSYr5slO_jvBJxggKac2g49o(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$11$PassportActivity(DialogInterface var1, int var2) {
      Activity var4 = this.getParentActivity();
      StringBuilder var3 = new StringBuilder();
      var3.append("https://telegram.org/deactivate?phone=");
      var3.append(UserConfig.getInstance(super.currentAccount).getClientPhone());
      Browser.openUrl(var4, (String)var3.toString());
   }

   // $FF: synthetic method
   public void lambda$null$14$PassportActivity(TLRPC.TL_error var1) {
      if (var1 == null) {
         this.ignoreOnFailure = true;
         this.callCallback(true);
         this.finishFragment();
      } else {
         this.showEditDoneProgress(false, false);
         if ("APP_VERSION_OUTDATED".equals(var1.text)) {
            AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
         } else {
            this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$15$PassportActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8rAsiN00Bfu9wX2d3bVxDe5_3UA(this, var2));
   }

   // $FF: synthetic method
   public void lambda$null$18$PassportActivity() {
      int var3;
      for(int var1 = 0; var1 < this.linearLayout2.getChildCount(); var1 = var3 + 1) {
         View var2 = this.linearLayout2.getChildAt(var1);
         var3 = var1;
         if (var2 instanceof PassportActivity.TextDetailSecureCell) {
            this.linearLayout2.removeView(var2);
            var3 = var1 - 1;
         }
      }

      this.needHideProgress();
      this.typesViews.clear();
      this.typesValues.clear();
      this.currentForm.values.clear();
      this.updateManageVisibility();
   }

   // $FF: synthetic method
   public void lambda$null$19$PassportActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$1OMOcheofF5mpc6UFu_0Xy9UK6I(this));
   }

   // $FF: synthetic method
   public void lambda$null$20$PassportActivity(DialogInterface var1, int var2) {
      TLRPC.TL_account_deleteSecureValue var3 = new TLRPC.TL_account_deleteSecureValue();

      for(var2 = 0; var2 < this.currentForm.values.size(); ++var2) {
         var3.types.add(((TLRPC.TL_secureValue)this.currentForm.values.get(var2)).type);
      }

      this.needShowProgress();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$PassportActivity$VYlmj64RpYyoUgzz406EWs6DcyI(this));
   }

   // $FF: synthetic method
   public void lambda$null$27$PassportActivity() {
      AndroidUtilities.showKeyboard(this.inputFields[2]);
   }

   // $FF: synthetic method
   public void lambda$null$28$PassportActivity(String var1, String var2) {
      this.inputFields[0].setText(var1);
      if (this.countriesArray.indexOf(var1) != -1) {
         this.ignoreOnTextChange = true;
         var1 = (String)this.countriesMap.get(var1);
         this.inputFields[1].setText(var1);
         var1 = (String)this.phoneFormatMap.get(var1);
         EditTextBoldCursor var3 = this.inputFields[2];
         if (var1 != null) {
            var1 = var1.replace('X', '–');
         } else {
            var1 = null;
         }

         var3.setHintText(var1);
         this.ignoreOnTextChange = false;
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$YJlZD6OdhwSywpXiGxUfp0RoQoQ(this), 300L);
      this.inputFields[2].requestFocus();
      EditTextBoldCursor[] var4 = this.inputFields;
      var4[2].setSelection(var4[2].length());
   }

   // $FF: synthetic method
   public void lambda$null$3$PassportActivity(TLObject var1) {
      if (var1 != null) {
         this.currentPassword = (TLRPC.TL_account_password)var1;
         if (!TwoStepVerificationActivity.canHandleCurrentPassword(this.currentPassword, false)) {
            AlertsCreator.showUpdateAppAlert(this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
            return;
         }

         TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
         this.updatePasswordInterface();
         if (this.inputFieldContainers[0].getVisibility() == 0) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
         }

         if (this.usingSavedPassword == 1) {
            this.onPasswordDone(true);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$34$PassportActivity(String var1, String var2) {
      this.inputFields[5].setText(var1);
      this.currentCitizeship = var2;
   }

   // $FF: synthetic method
   public void lambda$null$44$PassportActivity(MrzRecognizer.Result var1) {
      if (!TextUtils.isEmpty(var1.firstName)) {
         this.inputFields[0].setText(var1.firstName);
      }

      if (!TextUtils.isEmpty(var1.middleName)) {
         this.inputFields[1].setText(var1.middleName);
      }

      if (!TextUtils.isEmpty(var1.lastName)) {
         this.inputFields[2].setText(var1.lastName);
      }

      int var2 = var1.gender;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 == 2) {
               this.currentGender = "female";
               this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131560223));
            }
         } else {
            this.currentGender = "male";
            this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131560284));
         }
      }

      String var3;
      if (!TextUtils.isEmpty(var1.nationality)) {
         this.currentCitizeship = var1.nationality;
         var3 = (String)this.languageMap.get(this.currentCitizeship);
         if (var3 != null) {
            this.inputFields[5].setText(var3);
         }
      }

      if (!TextUtils.isEmpty(var1.issuingCountry)) {
         this.currentResidence = var1.issuingCountry;
         var3 = (String)this.languageMap.get(this.currentResidence);
         if (var3 != null) {
            this.inputFields[6].setText(var3);
         }
      }

      var2 = var1.birthDay;
      if (var2 > 0 && var1.birthMonth > 0 && var1.birthYear > 0) {
         this.inputFields[3].setText(String.format(Locale.US, "%02d.%02d.%d", var2, var1.birthMonth, var1.birthYear));
      }

   }

   // $FF: synthetic method
   public void lambda$null$46$PassportActivity(View var1, String var2, String var3) {
      int var4 = (Integer)var1.getTag();
      EditTextBoldCursor var5 = this.inputFields[var4];
      if (var4 == 5) {
         this.currentCitizeship = var3;
      } else {
         this.currentResidence = var3;
      }

      var5.setText(var2);
   }

   // $FF: synthetic method
   public void lambda$null$48$PassportActivity(int var1, EditTextBoldCursor var2, int var3, int var4, int var5) {
      if (var1 == 8) {
         int[] var6 = this.currentExpireDate;
         var6[0] = var3;
         var6[1] = var4 + 1;
         var6[2] = var5;
      }

      var2.setText(String.format(Locale.US, "%02d.%02d.%d", var5, var4 + 1, var3));
   }

   // $FF: synthetic method
   public void lambda$null$49$PassportActivity(EditTextBoldCursor var1, DialogInterface var2, int var3) {
      int[] var4 = this.currentExpireDate;
      var4[2] = 0;
      var4[1] = 0;
      var4[0] = 0;
      var1.setText(LocaleController.getString("PassportNoExpireDate", 2131560298));
   }

   // $FF: synthetic method
   public void lambda$null$51$PassportActivity(DialogInterface var1, int var2) {
      if (var2 == 0) {
         this.currentGender = "male";
         this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131560284));
      } else if (var2 == 1) {
         this.currentGender = "female";
         this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131560223));
      }

   }

   // $FF: synthetic method
   public void lambda$null$57$PassportActivity(SecureDocument var1, int var2, PassportActivity.SecureDocumentCell var3, String var4, DialogInterface var5, int var6) {
      this.documentsCells.remove(var1);
      if (var2 == 1) {
         this.selfieDocument = null;
         this.selfieLayout.removeView(var3);
      } else if (var2 == 4) {
         this.translationDocuments.remove(var1);
         this.translationLayout.removeView(var3);
      } else if (var2 == 2) {
         this.frontDocument = null;
         this.frontLayout.removeView(var3);
      } else if (var2 == 3) {
         this.reverseDocument = null;
         this.reverseLayout.removeView(var3);
      } else {
         this.documents.remove(var1);
         this.documentsLayout.removeView(var3);
      }

      if (var4 != null) {
         HashMap var7 = this.documentsErrors;
         if (var7 != null) {
            var7.remove(var4);
         }

         var7 = this.errorsValues;
         if (var7 != null) {
            var7.remove(var4);
         }
      }

      this.updateUploadText(var2);
      String var8 = var1.path;
      if (var8 != null && this.uploadingDocuments.remove(var8) != null) {
         if (this.uploadingDocuments.isEmpty()) {
            this.doneItem.setEnabled(true);
            this.doneItem.setAlpha(1.0F);
         }

         FileLoader.getInstance(super.currentAccount).cancelUploadFile(var1.path, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$60$PassportActivity(TLRPC.TL_error var1, PassportActivity.ErrorRunnable var2, boolean var3, TLRPC.TL_secureRequiredType var4, TLRPC.TL_secureRequiredType var5, boolean var6, ArrayList var7, Runnable var8) {
      Object var9 = null;
      if (var1 != null) {
         if (var2 != null) {
            var2.onError(var1.text, (String)null);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      } else {
         if (var3) {
            if (var4 != null) {
               this.removeValue(var4);
            } else {
               this.removeValue(var5);
            }
         } else {
            if (var6) {
               this.removeValue(var5);
            }

            this.removeValue(var4);
         }

         if (this.currentActivityType == 8) {
            PassportActivity.TextDetailSecureCell var19 = (PassportActivity.TextDetailSecureCell)this.typesViews.remove(var5);
            if (var19 != null) {
               this.linearLayout2.removeView(var19);
               LinearLayout var21 = this.linearLayout2;
               View var22 = var21.getChildAt(var21.getChildCount() - 6);
               if (var22 instanceof PassportActivity.TextDetailSecureCell) {
                  ((PassportActivity.TextDetailSecureCell)var22).setNeedDivider(false);
               }
            }

            this.updateManageVisibility();
         } else {
            int var11;
            TLRPC.TL_secureValue var12;
            TLRPC.TL_secureRequiredType var14;
            String var16;
            if (var4 != null && var7 != null && var7.size() > 1) {
               int var10 = var7.size();
               var11 = 0;

               String var15;
               TLRPC.TL_secureRequiredType var17;
               label79: {
                  while(true) {
                     if (var11 >= var10) {
                        var14 = var4;
                        break;
                     }

                     var17 = (TLRPC.TL_secureRequiredType)var7.get(var11);
                     var12 = this.getValueByType(var17, false);
                     if (var12 != null) {
                        TLRPC.TL_secureData var18 = var12.data;
                        var14 = var17;
                        if (var18 != null) {
                           var15 = this.decryptData(var18.data, this.decryptValueSecret(var18.secret, var18.data_hash), var12.data.data_hash);
                           break label79;
                        }
                        break;
                     }

                     ++var11;
                  }

                  var4 = null;
                  var17 = var14;
                  var15 = var4;
               }

               if (var17 == null) {
                  var4 = (TLRPC.TL_secureRequiredType)var7.get(0);
                  var16 = var15;
                  var14 = var4;
               } else {
                  var14 = var17;
                  var16 = var15;
               }
            } else {
               var14 = var4;
               var16 = null;
            }

            if (var6) {
               if (var7 != null) {
                  var11 = var7.size();
               } else {
                  var11 = 0;
               }

               this.setTypeValue(var5, (String)null, (String)null, var14, var16, var3, var11);
            } else {
               var12 = this.getValueByType(var5, false);
               String var20 = (String)var9;
               if (var12 != null) {
                  TLRPC.TL_secureData var13 = var12.data;
                  var20 = (String)var9;
                  if (var13 != null) {
                     var20 = this.decryptData(var13.data, this.decryptValueSecret(var13.secret, var13.data_hash), var12.data.data_hash);
                  }
               }

               if (var7 != null) {
                  var11 = var7.size();
               } else {
                  var11 = 0;
               }

               this.setTypeValue(var5, (String)null, var20, var14, var16, var3, var11);
            }
         }

         if (var8 != null) {
            var8.run();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$62$PassportActivity(TLRPC.TL_secureRequiredType var1, ArrayList var2, boolean var3, DialogInterface var4, int var5) {
      this.openTypeActivity(var1, (TLRPC.TL_secureRequiredType)var2.get(var5), var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$63$PassportActivity(String var1, String var2) {
      this.needHideProgress();
   }

   // $FF: synthetic method
   public void lambda$null$64$PassportActivity(TLRPC.TL_secureRequiredType var1, boolean var2, DialogInterface var3, int var4) {
      this.needShowProgress();
      this.deleteValueInternal(var1, (TLRPC.TL_secureRequiredType)null, (ArrayList)null, true, new _$$Lambda$7J3LPgacD3LbZtGqatVXuN86YjY(this), new _$$Lambda$PassportActivity$3vC5Cf8HrMsDxrDg3Ea7D_w0Ct0(this), var2);
   }

   // $FF: synthetic method
   public void lambda$null$66$PassportActivity(TLRPC.TL_error var1, String var2, PassportActivity.PassportActivityDelegate var3, TLObject var4, TLRPC.TL_account_sendVerifyPhoneCode var5) {
      if (var1 == null) {
         HashMap var6 = new HashMap();
         var6.put("phone", var2);
         PassportActivity var7 = new PassportActivity(7, this.currentForm, this.currentPassword, this.currentType, (TLRPC.TL_secureValue)null, (TLRPC.TL_secureRequiredType)null, (TLRPC.TL_secureValue)null, var6, (HashMap)null);
         var7.currentAccount = super.currentAccount;
         var7.saltedPassword = this.saltedPassword;
         var7.secureSecret = this.secureSecret;
         var7.delegate = var3;
         var7.currentPhoneVerification = (TLRPC.TL_auth_sentCode)var4;
         this.presentFragment(var7, true);
      } else {
         AlertsCreator.processError(super.currentAccount, var1, this, var5, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$null$71$PassportActivity(SecureDocument var1, int var2) {
      int var3 = this.uploadingFileType;
      SecureDocument var4;
      PassportActivity.SecureDocumentCell var5;
      if (var3 == 1) {
         var4 = this.selfieDocument;
         if (var4 != null) {
            var5 = (PassportActivity.SecureDocumentCell)this.documentsCells.remove(var4);
            if (var5 != null) {
               this.selfieLayout.removeView(var5);
            }

            this.selfieDocument = null;
         }
      } else if (var3 == 4) {
         if (this.translationDocuments.size() >= 20) {
            return;
         }
      } else if (var3 == 2) {
         var4 = this.frontDocument;
         if (var4 != null) {
            var5 = (PassportActivity.SecureDocumentCell)this.documentsCells.remove(var4);
            if (var5 != null) {
               this.frontLayout.removeView(var5);
            }

            this.frontDocument = null;
         }
      } else if (var3 == 3) {
         var4 = this.reverseDocument;
         if (var4 != null) {
            var5 = (PassportActivity.SecureDocumentCell)this.documentsCells.remove(var4);
            if (var5 != null) {
               this.reverseLayout.removeView(var5);
            }

            this.reverseDocument = null;
         }
      } else if (var3 == 0 && this.documents.size() >= 20) {
         return;
      }

      this.uploadingDocuments.put(var1.path, var1);
      this.doneItem.setEnabled(false);
      this.doneItem.setAlpha(0.5F);
      FileLoader.getInstance(super.currentAccount).uploadFile(var1.path, false, true, 16777216);
      this.addDocumentView(var1, var2);
      this.updateUploadText(var2);
   }

   // $FF: synthetic method
   public void lambda$null$72$PassportActivity(MrzRecognizer.Result var1) {
      int var2 = var1.type;
      int var3;
      TLRPC.TL_secureRequiredType var4;
      if (var2 == 2) {
         if (!(this.currentDocumentsType.type instanceof TLRPC.TL_secureValueTypeIdentityCard)) {
            var3 = this.availableDocumentTypes.size();

            for(var2 = 0; var2 < var3; ++var2) {
               var4 = (TLRPC.TL_secureRequiredType)this.availableDocumentTypes.get(var2);
               if (var4.type instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                  this.currentDocumentsType = var4;
                  this.updateInterfaceStringsForDocumentType();
                  break;
               }
            }
         }
      } else if (var2 == 1) {
         if (!(this.currentDocumentsType.type instanceof TLRPC.TL_secureValueTypePassport)) {
            var3 = this.availableDocumentTypes.size();

            for(var2 = 0; var2 < var3; ++var2) {
               var4 = (TLRPC.TL_secureRequiredType)this.availableDocumentTypes.get(var2);
               if (var4.type instanceof TLRPC.TL_secureValueTypePassport) {
                  this.currentDocumentsType = var4;
                  this.updateInterfaceStringsForDocumentType();
                  break;
               }
            }
         }
      } else if (var2 == 3) {
         if (!(this.currentDocumentsType.type instanceof TLRPC.TL_secureValueTypeInternalPassport)) {
            var3 = this.availableDocumentTypes.size();

            for(var2 = 0; var2 < var3; ++var2) {
               var4 = (TLRPC.TL_secureRequiredType)this.availableDocumentTypes.get(var2);
               if (var4.type instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                  this.currentDocumentsType = var4;
                  this.updateInterfaceStringsForDocumentType();
                  break;
               }
            }
         }
      } else if (var2 == 4 && !(this.currentDocumentsType.type instanceof TLRPC.TL_secureValueTypeDriverLicense)) {
         var3 = this.availableDocumentTypes.size();

         for(var2 = 0; var2 < var3; ++var2) {
            var4 = (TLRPC.TL_secureRequiredType)this.availableDocumentTypes.get(var2);
            if (var4.type instanceof TLRPC.TL_secureValueTypeDriverLicense) {
               this.currentDocumentsType = var4;
               this.updateInterfaceStringsForDocumentType();
               break;
            }
         }
      }

      if (!TextUtils.isEmpty(var1.firstName)) {
         this.inputFields[0].setText(var1.firstName);
      }

      if (!TextUtils.isEmpty(var1.middleName)) {
         this.inputFields[1].setText(var1.middleName);
      }

      if (!TextUtils.isEmpty(var1.lastName)) {
         this.inputFields[2].setText(var1.lastName);
      }

      if (!TextUtils.isEmpty(var1.number)) {
         this.inputFields[7].setText(var1.number);
      }

      var2 = var1.gender;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 == 2) {
               this.currentGender = "female";
               this.inputFields[4].setText(LocaleController.getString("PassportFemale", 2131560223));
            }
         } else {
            this.currentGender = "male";
            this.inputFields[4].setText(LocaleController.getString("PassportMale", 2131560284));
         }
      }

      String var7;
      if (!TextUtils.isEmpty(var1.nationality)) {
         this.currentCitizeship = var1.nationality;
         var7 = (String)this.languageMap.get(this.currentCitizeship);
         if (var7 != null) {
            this.inputFields[5].setText(var7);
         }
      }

      if (!TextUtils.isEmpty(var1.issuingCountry)) {
         this.currentResidence = var1.issuingCountry;
         var7 = (String)this.languageMap.get(this.currentResidence);
         if (var7 != null) {
            this.inputFields[6].setText(var7);
         }
      }

      var2 = var1.birthDay;
      if (var2 > 0 && var1.birthMonth > 0 && var1.birthYear > 0) {
         this.inputFields[3].setText(String.format(Locale.US, "%02d.%02d.%d", var2, var1.birthMonth, var1.birthYear));
      }

      var3 = var1.expiryDay;
      if (var3 > 0) {
         var2 = var1.expiryMonth;
         if (var2 > 0) {
            int var5 = var1.expiryYear;
            if (var5 > 0) {
               int[] var8 = this.currentExpireDate;
               var8[0] = var5;
               var8[1] = var2;
               var8[2] = var3;
               this.inputFields[8].setText(String.format(Locale.US, "%02d.%02d.%d", var3, var1.expiryMonth, var1.expiryYear));
               return;
            }
         }
      }

      int[] var6 = this.currentExpireDate;
      var6[2] = 0;
      var6[1] = 0;
      var6[0] = 0;
      this.inputFields[8].setText(LocaleController.getString("PassportNoExpireDate", 2131560298));
   }

   // $FF: synthetic method
   public void lambda$null$8$PassportActivity(TLRPC.TL_auth_passwordRecovery var1, DialogInterface var2, int var3) {
      TwoStepVerificationActivity var4 = new TwoStepVerificationActivity(super.currentAccount, 1);
      var4.setRecoveryParams(this.currentPassword);
      this.currentPassword.email_unconfirmed_pattern = var1.email_pattern;
      this.presentFragment(var4);
   }

   // $FF: synthetic method
   public void lambda$null$9$PassportActivity(TLRPC.TL_error var1, TLObject var2) {
      this.needHideProgress();
      if (var1 == null) {
         TLRPC.TL_auth_passwordRecovery var4 = (TLRPC.TL_auth_passwordRecovery)var2;
         AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
         var7.setMessage(LocaleController.formatString("RestoreEmailSent", 2131560607, var4.email_pattern));
         var7.setTitle(LocaleController.getString("AppName", 2131558635));
         var7.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PassportActivity$hLNmBmiskobIF7R8TJhAQSbqU04(this, var4));
         Dialog var5 = this.showDialog(var7.create());
         if (var5 != null) {
            var5.setCanceledOnTouchOutside(false);
            var5.setCancelable(false);
         }
      } else if (var1.text.startsWith("FLOOD_WAIT")) {
         int var3 = Utilities.parseInt(var1.text);
         String var6;
         if (var3 < 60) {
            var6 = LocaleController.formatPluralString("Seconds", var3);
         } else {
            var6 = LocaleController.formatPluralString("Minutes", var3 / 60);
         }

         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var6));
      } else {
         this.showAlertWithText(LocaleController.getString("AppName", 2131558635), var1.text);
      }

   }

   // $FF: synthetic method
   public void lambda$onPasswordDone$13$PassportActivity(boolean var1, String var2) {
      TLRPC.TL_account_getPasswordSettings var3 = new TLRPC.TL_account_getPasswordSettings();
      byte[] var4;
      if (var1) {
         var4 = this.savedPasswordHash;
      } else if (this.currentPassword.current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         var4 = SRPHelper.getX(AndroidUtilities.getStringBytes(var2), (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)this.currentPassword.current_algo);
      } else {
         var4 = null;
      }

      PassportActivity$8 var8 = new PassportActivity$8(this, var1, var4, var3, var2);
      TLRPC.TL_account_password var5 = this.currentPassword;
      TLRPC.PasswordKdfAlgo var6 = var5.current_algo;
      TLRPC.TL_error var9;
      if (var6 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var10 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var6;
         var3.password = SRPHelper.startCheck(var4, var5.srp_id, var5.srp_B, var10);
         if (var3.password == null) {
            var9 = new TLRPC.TL_error();
            var9.text = "ALGO_INVALID";
            var8.run((TLObject)null, var9);
            return;
         }

         int var7 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, var8, 10);
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var7, super.classGuid);
      } else {
         var9 = new TLRPC.TL_error();
         var9.text = "PASSWORD_HASH_INVALID";
         var8.run((TLObject)null, var9);
      }

   }

   // $FF: synthetic method
   public void lambda$onRequestPermissionsResultFragment$69$PassportActivity(DialogInterface var1, int var2) {
      try {
         Intent var3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
         StringBuilder var5 = new StringBuilder();
         var5.append("package:");
         var5.append(ApplicationLoader.applicationContext.getPackageName());
         var3.setData(Uri.parse(var5.toString()));
         this.getParentActivity().startActivity(var3);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$onResume$2$PassportActivity() {
      ViewGroup[] var1 = this.inputFieldContainers;
      if (var1 != null && var1[0] != null && var1[0].getVisibility() == 0) {
         this.inputFields[0].requestFocus();
         AndroidUtilities.showKeyboard(this.inputFields[0]);
      }

   }

   // $FF: synthetic method
   public void lambda$onTransitionAnimationEnd$68$PassportActivity() {
      this.presentFragment(this.presentAfterAnimation, true);
      this.presentAfterAnimation = null;
   }

   // $FF: synthetic method
   public void lambda$openAddDocumentAlert$23$PassportActivity(ArrayList var1, DialogInterface var2, int var3) {
      TLRPC.TL_secureRequiredType var9;
      TLRPC.TL_secureRequiredType var10;
      label34: {
         try {
            var10 = new TLRPC.TL_secureRequiredType();
         } catch (Exception var8) {
            var9 = null;
            break label34;
         }

         try {
            var10.type = (TLRPC.SecureValueType)((Class)var1.get(var3)).newInstance();
         } catch (Exception var7) {
            var9 = var10;
            break label34;
         }

         var9 = var10;
      }

      boolean var4 = this.isPersonalDocument(var9.type);
      boolean var5 = true;
      TLRPC.TL_secureRequiredType var6;
      if (var4) {
         var9.selfie_required = true;
         var9.translation_required = true;
         var6 = new TLRPC.TL_secureRequiredType();
         var6.type = new TLRPC.TL_secureValueTypePersonalDetails();
         var10 = var9;
         var9 = var6;
      } else if (this.isAddressDocument(var9.type)) {
         var6 = new TLRPC.TL_secureRequiredType();
         var6.type = new TLRPC.TL_secureValueTypeAddress();
         var10 = var9;
         var9 = var6;
      } else {
         var10 = null;
      }

      ArrayList var11 = new ArrayList();
      if (var10 == null) {
         var5 = false;
      }

      this.openTypeActivity(var9, var10, var11, var5);
   }

   // $FF: synthetic method
   public void lambda$processSelectedFiles$73$PassportActivity(ArrayList var1, int var2, boolean var3) {
      int var4 = this.uploadingFileType;
      byte var13;
      if (var4 != 0 && var4 != 4) {
         var13 = 1;
      } else {
         var13 = 20;
      }

      int var5 = Math.min(var13, var1.size());
      int var6 = 0;

      boolean var8;
      for(boolean var14 = false; var6 < var5; var14 = var8) {
         SendMessagesHelper.SendingMediaInfo var7 = (SendMessagesHelper.SendingMediaInfo)var1.get(var6);
         Bitmap var15 = ImageLoader.loadBitmap(var7.path, var7.uri, 2048.0F, 2048.0F, false);
         if (var15 == null) {
            var8 = var14;
         } else {
            TLRPC.PhotoSize var9 = ImageLoader.scaleAndSaveImage(var15, 2048.0F, 2048.0F, 89, false, 320, 320);
            if (var9 == null) {
               var8 = var14;
            } else {
               TLRPC.TL_secureFile var10 = new TLRPC.TL_secureFile();
               TLRPC.FileLocation var18 = var9.location;
               var10.dc_id = (int)var18.volume_id;
               var10.id = (long)var18.local_id;
               var10.date = (int)(System.currentTimeMillis() / 1000L);
               SecureDocument var19 = this.delegate.saveFile(var10);
               var19.type = var2;
               AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$2i_R1Bni7j2ywjhyD3jD3ME3dwM(this, var19, var2));
               var8 = var14;
               if (var3) {
                  var8 = var14;
                  if (!var14) {
                     label64: {
                        label44: {
                           Throwable var16;
                           label60: {
                              MrzRecognizer.Result var17;
                              try {
                                 var17 = MrzRecognizer.recognize(var15, this.currentDocumentsType.type instanceof TLRPC.TL_secureValueTypeDriverLicense);
                              } catch (Throwable var12) {
                                 var16 = var12;
                                 break label60;
                              }

                              var8 = var14;
                              if (var17 == null) {
                                 break label64;
                              }

                              try {
                                 _$$Lambda$PassportActivity$BRwn9uK4ua32ZIGkCWaMIr8OwBQ var20 = new _$$Lambda$PassportActivity$BRwn9uK4ua32ZIGkCWaMIr8OwBQ(this, var17);
                                 AndroidUtilities.runOnUIThread(var20);
                                 break label44;
                              } catch (Throwable var11) {
                                 var16 = var11;
                                 var14 = true;
                              }
                           }

                           FileLog.e(var16);
                           var8 = var14;
                           break label64;
                        }

                        var8 = true;
                     }
                  }
               }
            }
         }

         ++var6;
      }

      SharedConfig.saveConfig();
   }

   // $FF: synthetic method
   public void lambda$startPhoneVerification$67$PassportActivity(String var1, PassportActivity.PassportActivityDelegate var2, TLRPC.TL_account_sendVerifyPhoneCode var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8(this, var5, var1, var2, var4, var3));
   }

   public void needHideProgress() {
      AlertDialog var1 = this.progressDialog;
      if (var1 != null) {
         try {
            var1.dismiss();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         this.progressDialog = null;
      }
   }

   public void needShowProgress() {
      if (this.getParentActivity() != null && !this.getParentActivity().isFinishing() && this.progressDialog == null) {
         this.progressDialog = new AlertDialog(this.getParentActivity(), 3);
         this.progressDialog.setCanCacnel(false);
         this.progressDialog.show();
      }

   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      if (var2 == -1) {
         if (var1 != 0 && var1 != 2) {
            if (var1 == 1) {
               if (var3 == null || var3.getData() == null) {
                  this.showAttachmentError();
                  return;
               }

               ArrayList var4 = new ArrayList();
               SendMessagesHelper.SendingMediaInfo var6 = new SendMessagesHelper.SendingMediaInfo();
               var6.uri = var3.getData();
               var4.add(var6);
               this.processSelectedFiles(var4);
            }
         } else {
            this.createChatAttachView();
            ChatAttachAlert var5 = this.chatAttachAlert;
            if (var5 != null) {
               var5.onActivityResultFragment(var1, var3, this.currentPicturePath);
            }

            this.currentPicturePath = null;
         }
      }

   }

   public boolean onBackPressed() {
      int var1 = this.currentActivityType;
      int var2 = 0;
      if (var1 == 7) {
         this.views[this.currentViewNum].onBackPressed(true);

         while(true) {
            SlideView[] var3 = this.views;
            if (var2 >= var3.length) {
               break;
            }

            if (var3[var2] != null) {
               var3[var2].onDestroyActivity();
            }

            ++var2;
         }
      } else if (var1 != 0 && var1 != 5) {
         if (var1 == 1 || var1 == 2) {
            return this.checkDiscard() ^ true;
         }
      } else {
         this.callCallback(false);
      }

      return true;
   }

   protected void onDialogDismiss(Dialog var1) {
      if (this.currentActivityType == 3 && VERSION.SDK_INT >= 23 && var1 == this.permissionsDialog && !this.permissionsItems.isEmpty()) {
         this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
      }

   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.FileDidFailUpload);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
      int var1 = 0;
      this.callCallback(false);
      ChatAttachAlert var2 = this.chatAttachAlert;
      if (var2 != null) {
         var2.dismissInternal();
         this.chatAttachAlert.onDestroy();
      }

      if (this.currentActivityType == 7) {
         while(true) {
            SlideView[] var4 = this.views;
            if (var1 >= var4.length) {
               AlertDialog var5 = this.progressDialog;
               if (var5 != null) {
                  try {
                     var5.dismiss();
                  } catch (Exception var3) {
                     FileLog.e((Throwable)var3);
                  }

                  this.progressDialog = null;
               }
               break;
            }

            if (var4[var1] != null) {
               var4[var1].onDestroyActivity();
            }

            ++var1;
         }
      }

   }

   public void onPause() {
      super.onPause();
      ChatAttachAlert var1 = this.chatAttachAlert;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      int var4 = this.currentActivityType;
      if (var4 == 1 || var4 == 2) {
         ChatAttachAlert var5 = this.chatAttachAlert;
         if (var5 != null) {
            if (var1 == 17 && var5 != null) {
               var5.checkCamera(false);
               return;
            } else if (var1 == 21) {
               if (this.getParentActivity() == null) {
                  return;
               }

               if (var3 != null && var3.length != 0 && var3[0] != 0) {
                  AlertDialog.Builder var6 = new AlertDialog.Builder(this.getParentActivity());
                  var6.setTitle(LocaleController.getString("AppName", 2131558635));
                  var6.setMessage(LocaleController.getString("PermissionNoAudioVideo", 2131560415));
                  var6.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$PassportActivity$saEsQ_rHD2skXoYwyq_hN_ao_V8(this));
                  var6.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var6.show();
                  return;
               }

               return;
            } else {
               if (var1 == 19 && var3 != null && var3.length > 0 && var3[0] == 0) {
                  this.processSelectedAttach(0);
               } else if (var1 == 22 && var3 != null && var3.length > 0 && var3[0] == 0) {
                  TextSettingsCell var7 = this.scanDocumentCell;
                  if (var7 != null) {
                     var7.callOnClick();
                  }

                  return;
               }

               return;
            }
         }
      }

      if (this.currentActivityType == 3 && var1 == 6) {
         this.startPhoneVerification(false, this.pendingPhone, this.pendingFinishRunnable, this.pendingErrorRunnable, this.pendingDelegate);
      }

   }

   public void onResume() {
      super.onResume();
      ChatAttachAlert var1 = this.chatAttachAlert;
      if (var1 != null) {
         var1.onResume();
      }

      if (this.currentActivityType == 5) {
         ViewGroup[] var2 = this.inputFieldContainers;
         if (var2 != null && var2[0] != null && var2[0].getVisibility() == 0) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$1boqEPL5RwKnvvajl3SmsZe7IEk(this), 200L);
         }
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (this.presentAfterAnimation != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$l67xseKhR4lQtrsyuaJeoxN_jJM(this));
      }

      int var3 = this.currentActivityType;
      if (var3 == 5) {
         if (var1) {
            if (this.inputFieldContainers[0].getVisibility() == 0) {
               this.inputFields[0].requestFocus();
               AndroidUtilities.showKeyboard(this.inputFields[0]);
            }

            if (this.usingSavedPassword == 2) {
               this.onPasswordDone(false);
            }
         }
      } else if (var3 == 7) {
         if (var1) {
            this.views[this.currentViewNum].onShow();
         }
      } else if (var3 == 4) {
         if (var1) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
         }
      } else if (var3 == 6) {
         if (var1) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
         }
      } else if ((var3 == 2 || var3 == 1) && VERSION.SDK_INT >= 21) {
         this.createChatAttachView();
      }

   }

   public void restoreSelfArgs(Bundle var1) {
      this.currentPicturePath = var1.getString("path");
   }

   public void saveSelfArgs(Bundle var1) {
      String var2 = this.currentPicturePath;
      if (var2 != null) {
         var1.putString("path", var2);
      }

   }

   public void setNeedActivityResult(boolean var1) {
      this.needActivityResult = var1;
   }

   public void setPage(int var1, boolean var2, Bundle var3) {
      if (var1 == 3) {
         this.doneItem.setVisibility(8);
      }

      SlideView[] var4 = this.views;
      final SlideView var5 = var4[this.currentViewNum];
      final SlideView var7 = var4[var1];
      this.currentViewNum = var1;
      var7.setParams(var3, false);
      var7.onShow();
      if (var2) {
         var7.setTranslationX((float)AndroidUtilities.displaySize.x);
         AnimatorSet var6 = new AnimatorSet();
         var6.setInterpolator(new AccelerateDecelerateInterpolator());
         var6.setDuration(300L);
         var6.playTogether(new Animator[]{ObjectAnimator.ofFloat(var5, "translationX", new float[]{(float)(-AndroidUtilities.displaySize.x)}), ObjectAnimator.ofFloat(var7, "translationX", new float[]{0.0F})});
         var6.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var5.setVisibility(8);
               var5.setX(0.0F);
            }

            public void onAnimationStart(Animator var1) {
               var7.setVisibility(0);
            }
         });
         var6.start();
      } else {
         var7.setTranslationX(0.0F);
         var7.setVisibility(0);
         if (var5 != var7) {
            var5.setVisibility(8);
         }
      }

   }

   private class EncryptionResult {
      byte[] decrypyedFileSecret;
      byte[] encryptedData;
      byte[] fileHash;
      byte[] fileSecret;
      SecureDocumentKey secureDocumentKey;

      public EncryptionResult(byte[] var2, byte[] var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7) {
         this.encryptedData = var2;
         this.fileSecret = var3;
         this.fileHash = var5;
         this.decrypyedFileSecret = var4;
         this.secureDocumentKey = new SecureDocumentKey(var6, var7);
      }
   }

   private interface ErrorRunnable {
      void onError(String var1, String var2);
   }

   public class LinkSpan extends ClickableSpan {
      public void onClick(View var1) {
         Browser.openUrl(PassportActivity.this.getParentActivity(), (String)PassportActivity.this.currentForm.privacy_policy_url);
      }

      public void updateDrawState(TextPaint var1) {
         super.updateDrawState(var1);
         var1.setUnderlineText(true);
         var1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      }
   }

   private interface PassportActivityDelegate {
      void deleteValue(TLRPC.TL_secureRequiredType var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, boolean var4, Runnable var5, PassportActivity.ErrorRunnable var6);

      SecureDocument saveFile(TLRPC.TL_secureFile var1);

      void saveValue(TLRPC.TL_secureRequiredType var1, String var2, String var3, TLRPC.TL_secureRequiredType var4, String var5, ArrayList var6, SecureDocument var7, ArrayList var8, SecureDocument var9, SecureDocument var10, Runnable var11, PassportActivity.ErrorRunnable var12);
   }

   public class PhoneConfirmationView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
      private ImageView blackImageView;
      private ImageView blueImageView;
      private EditTextBoldCursor[] codeField;
      private LinearLayout codeFieldContainer;
      private int codeTime = 15000;
      private Timer codeTimer;
      private TextView confirmTextView;
      private Bundle currentParams;
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
      private PassportActivity.ProgressView progressView;
      private int time = 60000;
      private TextView timeText;
      private Timer timeTimer;
      private int timeout;
      private final Object timerSync = new Object();
      private TextView titleTextView;
      private int verificationType;
      private boolean waitingForEvent;

      public PhoneConfirmationView(Context var2, int var3) {
         super(var2);
         this.verificationType = var3;
         this.setOrientation(1);
         this.confirmTextView = new TextView(var2);
         this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.confirmTextView.setTextSize(1, 14.0F);
         this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.titleTextView = new TextView(var2);
         this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.titleTextView.setTextSize(1, 18.0F);
         this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         TextView var4 = this.titleTextView;
         boolean var5 = LocaleController.isRTL;
         byte var6 = 3;
         byte var9;
         if (var5) {
            var9 = 5;
         } else {
            var9 = 3;
         }

         var4.setGravity(var9);
         this.titleTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.titleTextView.setGravity(49);
         FrameLayout var10;
         if (this.verificationType == 3) {
            var4 = this.confirmTextView;
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            var4.setGravity(var9 | 48);
            var10 = new FrameLayout(var2);
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            this.addView(var10, LayoutHelper.createLinear(-2, -2, var9));
            ImageView var7 = new ImageView(var2);
            var7.setImageResource(2131165739);
            var5 = LocaleController.isRTL;
            TextView var8;
            if (var5) {
               var10.addView(var7, LayoutHelper.createFrame(64, 76.0F, 19, 2.0F, 2.0F, 0.0F, 0.0F));
               var8 = this.confirmTextView;
               if (LocaleController.isRTL) {
                  var9 = 5;
               } else {
                  var9 = 3;
               }

               var10.addView(var8, LayoutHelper.createFrame(-1, -2.0F, var9, 82.0F, 0.0F, 0.0F, 0.0F));
            } else {
               var8 = this.confirmTextView;
               if (var5) {
                  var9 = 5;
               } else {
                  var9 = 3;
               }

               var10.addView(var8, LayoutHelper.createFrame(-1, -2.0F, var9, 0.0F, 0.0F, 82.0F, 0.0F));
               var10.addView(var7, LayoutHelper.createFrame(64, 76.0F, 21, 0.0F, 2.0F, 0.0F, 2.0F));
            }
         } else {
            this.confirmTextView.setGravity(49);
            var10 = new FrameLayout(var2);
            this.addView(var10, LayoutHelper.createLinear(-2, -2, 49));
            if (this.verificationType == 1) {
               this.blackImageView = new ImageView(var2);
               this.blackImageView.setImageResource(2131165856);
               this.blackImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), Mode.MULTIPLY));
               var10.addView(this.blackImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.blueImageView = new ImageView(var2);
               this.blueImageView.setImageResource(2131165854);
               this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), Mode.MULTIPLY));
               var10.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setText(LocaleController.getString("SentAppCodeTitle", 2131560718));
            } else {
               this.blueImageView = new ImageView(var2);
               this.blueImageView.setImageResource(2131165855);
               this.blueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), Mode.MULTIPLY));
               var10.addView(this.blueImageView, LayoutHelper.createFrame(-2, -2.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setText(LocaleController.getString("SentSmsCodeTitle", 2131560722));
            }

            this.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
            this.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
         }

         this.codeFieldContainer = new LinearLayout(var2);
         this.codeFieldContainer.setOrientation(0);
         this.addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 36, 1));
         if (this.verificationType == 3) {
            this.codeFieldContainer.setVisibility(8);
         }

         this.timeText = new TextView(var2) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), Integer.MIN_VALUE));
            }
         };
         this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         if (this.verificationType == 3) {
            this.timeText.setTextSize(1, 14.0F);
            var4 = this.timeText;
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            this.addView(var4, LayoutHelper.createLinear(-2, -2, var9));
            this.progressView = PassportActivity.this.new ProgressView(var2);
            var4 = this.timeText;
            var9 = var6;
            if (LocaleController.isRTL) {
               var9 = 5;
            }

            var4.setGravity(var9);
            this.addView(this.progressView, LayoutHelper.createLinear(-1, 3, 0.0F, 12.0F, 0.0F, 0.0F));
         } else {
            this.timeText.setPadding(0, AndroidUtilities.dp(2.0F), 0, AndroidUtilities.dp(10.0F));
            this.timeText.setTextSize(1, 15.0F);
            this.timeText.setGravity(49);
            this.addView(this.timeText, LayoutHelper.createLinear(-2, -2, 49));
         }

         this.problemText = new TextView(var2) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), Integer.MIN_VALUE));
            }
         };
         this.problemText.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         this.problemText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.problemText.setPadding(0, AndroidUtilities.dp(2.0F), 0, AndroidUtilities.dp(10.0F));
         this.problemText.setTextSize(1, 15.0F);
         this.problemText.setGravity(49);
         if (this.verificationType == 1) {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
         } else {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCode", 2131559266));
         }

         this.addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
         this.problemText.setOnClickListener(new _$$Lambda$PassportActivity$PhoneConfirmationView$PSs1EP1O5Wgd6q0a05fTJgjQt4s(this));
      }

      private void createCodeTimer() {
         if (this.codeTimer == null) {
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$run$0$PassportActivity$PhoneConfirmationView$4() {
                  double var1 = (double)System.currentTimeMillis();
                  double var3 = PhoneConfirmationView.this.lastCodeTime;
                  Double.isNaN(var1);
                  PhoneConfirmationView.this.lastCodeTime = var1;
                  PassportActivity.PhoneConfirmationView var5 = PhoneConfirmationView.this;
                  double var6 = (double)var5.codeTime;
                  Double.isNaN(var6);
                  var5.codeTime = (int)(var6 - (var1 - var3));
                  if (PhoneConfirmationView.this.codeTime <= 1000) {
                     PhoneConfirmationView.this.problemText.setVisibility(0);
                     PhoneConfirmationView.this.timeText.setVisibility(8);
                     PhoneConfirmationView.this.destroyCodeTimer();
                  }

               }

               public void run() {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$PhoneConfirmationView$4$4TMBClZCqBNuy_tFpKG40rvRb80(this));
               }
            }, 0L, 1000L);
         }
      }

      private void createTimer() {
         if (this.timeTimer == null) {
            this.timeTimer = new Timer();
            this.timeTimer.schedule(new TimerTask() {
               public void run() {
                  if (PhoneConfirmationView.this.timeTimer != null) {
                     double var1 = (double)System.currentTimeMillis();
                     double var3 = PhoneConfirmationView.this.lastCurrentTime;
                     Double.isNaN(var1);
                     PassportActivity.PhoneConfirmationView var5 = PhoneConfirmationView.this;
                     double var6 = (double)var5.time;
                     Double.isNaN(var6);
                     var5.time = (int)(var6 - (var1 - var3));
                     PhoneConfirmationView.this.lastCurrentTime = var1;
                     AndroidUtilities.runOnUIThread(new Runnable() {
                        // $FF: synthetic method
                        public void lambda$null$0$PassportActivity$PhoneConfirmationView$5$1(TLRPC.TL_error var1) {
                           PhoneConfirmationView.this.lastError = var1.text;
                        }

                        // $FF: synthetic method
                        public void lambda$run$1$PassportActivity$PhoneConfirmationView$5$1(TLObject var1, TLRPC.TL_error var2) {
                           if (var2 != null && var2.text != null) {
                              AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$PhoneConfirmationView$5$1$_XxiWYM9U_4LvqFMJJ2CZp8aBO0(this, var2));
                           }

                        }

                        public void run() {
                           if (PhoneConfirmationView.this.time >= 1000) {
                              int var1 = PhoneConfirmationView.this.time / 1000 / 60;
                              int var2 = PhoneConfirmationView.this.time / 1000 - var1 * 60;
                              if (PhoneConfirmationView.this.nextType != 4 && PhoneConfirmationView.this.nextType != 3) {
                                 if (PhoneConfirmationView.this.nextType == 2) {
                                    PhoneConfirmationView.this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, var1, var2));
                                 }
                              } else {
                                 PhoneConfirmationView.this.timeText.setText(LocaleController.formatString("CallText", 2131558885, var1, var2));
                              }

                              if (PhoneConfirmationView.this.progressView != null) {
                                 PhoneConfirmationView.this.progressView.setProgress(1.0F - (float)PhoneConfirmationView.this.time / (float)PhoneConfirmationView.this.timeout);
                              }
                           } else {
                              if (PhoneConfirmationView.this.progressView != null) {
                                 PhoneConfirmationView.this.progressView.setProgress(1.0F);
                              }

                              PhoneConfirmationView.this.destroyTimer();
                              if (PhoneConfirmationView.this.verificationType == 3) {
                                 AndroidUtilities.setWaitingForCall(false);
                                 NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                                 PhoneConfirmationView.this.waitingForEvent = false;
                                 PhoneConfirmationView.this.destroyCodeTimer();
                                 PhoneConfirmationView.this.resendCode();
                              } else if (PhoneConfirmationView.this.verificationType == 2 || PhoneConfirmationView.this.verificationType == 4) {
                                 if (PhoneConfirmationView.this.nextType != 4 && PhoneConfirmationView.this.nextType != 2) {
                                    if (PhoneConfirmationView.this.nextType == 3) {
                                       AndroidUtilities.setWaitingForSms(false);
                                       NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                                       PhoneConfirmationView.this.waitingForEvent = false;
                                       PhoneConfirmationView.this.destroyCodeTimer();
                                       PhoneConfirmationView.this.resendCode();
                                    }
                                 } else {
                                    if (PhoneConfirmationView.this.nextType == 4) {
                                       PhoneConfirmationView.this.timeText.setText(LocaleController.getString("Calling", 2131558887));
                                    } else {
                                       PhoneConfirmationView.this.timeText.setText(LocaleController.getString("SendingSms", 2131560714));
                                    }

                                    PhoneConfirmationView.this.createCodeTimer();
                                    TLRPC.TL_auth_resendCode var3 = new TLRPC.TL_auth_resendCode();
                                    var3.phone_number = PhoneConfirmationView.this.phone;
                                    var3.phone_code_hash = PhoneConfirmationView.this.phoneHash;
                                    ConnectionsManager.getInstance(PassportActivity.access$11700(PassportActivity.this)).sendRequest(var3, new _$$Lambda$PassportActivity$PhoneConfirmationView$5$1$Q_utOxW1QEIU96NoSBjX04lpg8k(this), 2);
                                 }
                              }
                           }

                        }
                     });
                  }
               }
            }, 0L, 1000L);
         }
      }

      private void destroyCodeTimer() {
         // $FF: Couldn't be decompiled
      }

      private void destroyTimer() {
         // $FF: Couldn't be decompiled
      }

      private String getCode() {
         if (this.codeField == null) {
            return "";
         } else {
            StringBuilder var1 = new StringBuilder();
            int var2 = 0;

            while(true) {
               EditTextBoldCursor[] var3 = this.codeField;
               if (var2 >= var3.length) {
                  return var1.toString();
               }

               var1.append(PhoneFormat.stripExceptNumbers(var3[var2].getText().toString()));
               ++var2;
            }
         }
      }

      // $FF: synthetic method
      static void lambda$onBackPressed$9(TLObject var0, TLRPC.TL_error var1) {
      }

      private void resendCode() {
         Bundle var1 = new Bundle();
         var1.putString("phone", this.phone);
         this.nextPressed = true;
         PassportActivity.this.needShowProgress();
         TLRPC.TL_auth_resendCode var2 = new TLRPC.TL_auth_resendCode();
         var2.phone_number = this.phone;
         var2.phone_code_hash = this.phoneHash;
         ConnectionsManager.getInstance(PassportActivity.access$9400(PassportActivity.this)).sendRequest(var2, new _$$Lambda$PassportActivity$PhoneConfirmationView$1NsJYQaZadbCmSfsr4dWklizb2g(this, var1, var2), 2);
      }

      public void didReceivedNotification(int var1, int var2, Object... var3) {
         if (this.waitingForEvent) {
            EditTextBoldCursor[] var4 = this.codeField;
            if (var4 != null) {
               if (var1 == NotificationCenter.didReceiveSmsCode) {
                  EditTextBoldCursor var7 = var4[0];
                  StringBuilder var5 = new StringBuilder();
                  var5.append("");
                  var5.append(var3[0]);
                  var7.setText(var5.toString());
                  this.onNextPressed();
               } else if (var1 == NotificationCenter.didReceiveCall) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("");
                  var8.append(var3[0]);
                  String var6 = var8.toString();
                  if (!AndroidUtilities.checkPhonePattern(this.pattern, var6)) {
                     return;
                  }

                  this.ignoreOnTextChange = true;
                  this.codeField[0].setText(var6);
                  this.ignoreOnTextChange = false;
                  this.onNextPressed();
               }
            }
         }

      }

      // $FF: synthetic method
      public void lambda$new$0$PassportActivity$PhoneConfirmationView(View var1) {
         if (!this.nextPressed) {
            boolean var2;
            if ((this.nextType != 4 || this.verificationType != 2) && this.nextType != 0) {
               var2 = false;
            } else {
               var2 = true;
            }

            if (!var2) {
               this.resendCode();
            } else {
               try {
                  PackageInfo var6 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                  String var7 = String.format(Locale.US, "%s (%d)", var6.versionName, var6.versionCode);
                  Intent var3 = new Intent("android.intent.action.SEND");
                  var3.setType("message/rfc822");
                  var3.putExtra("android.intent.extra.EMAIL", new String[]{"sms@stel.com"});
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Android registration/login issue ");
                  var4.append(var7);
                  var4.append(" ");
                  var4.append(this.phone);
                  var3.putExtra("android.intent.extra.SUBJECT", var4.toString());
                  var4 = new StringBuilder();
                  var4.append("Phone: ");
                  var4.append(this.phone);
                  var4.append("\nApp version: ");
                  var4.append(var7);
                  var4.append("\nOS version: SDK ");
                  var4.append(VERSION.SDK_INT);
                  var4.append("\nDevice Name: ");
                  var4.append(Build.MANUFACTURER);
                  var4.append(Build.MODEL);
                  var4.append("\nLocale: ");
                  var4.append(Locale.getDefault());
                  var4.append("\nError: ");
                  var4.append(this.lastError);
                  var3.putExtra("android.intent.extra.TEXT", var4.toString());
                  this.getContext().startActivity(Intent.createChooser(var3, "Send email..."));
               } catch (Exception var5) {
                  AlertsCreator.showSimpleAlert(PassportActivity.this, LocaleController.getString("NoMailInstalled", 2131559927));
               }
            }

         }
      }

      // $FF: synthetic method
      public void lambda$null$1$PassportActivity$PhoneConfirmationView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         PassportActivity.this.finishFragment();
      }

      // $FF: synthetic method
      public void lambda$null$2$PassportActivity$PhoneConfirmationView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_auth_resendCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            PassportActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3, true);
         } else {
            AlertDialog var5 = (AlertDialog)AlertsCreator.processError(PassportActivity.access$12300(PassportActivity.this), var1, PassportActivity.this, var4);
            if (var5 != null && var1.text.contains("PHONE_CODE_EXPIRED")) {
               var5.setPositiveButtonListener(new _$$Lambda$PassportActivity$PhoneConfirmationView$igbayS6dw7tSDYDaiFu83_vyLoc(this));
            }
         }

         PassportActivity.this.needHideProgress();
      }

      // $FF: synthetic method
      public void lambda$null$6$PassportActivity$PhoneConfirmationView(TLRPC.TL_error var1, TLRPC.TL_account_verifyPhone var2) {
         PassportActivity.this.needHideProgress();
         this.nextPressed = false;
         if (var1 == null) {
            this.destroyTimer();
            this.destroyCodeTimer();
            PassportActivity.this.delegate.saveValue(PassportActivity.this.currentType, (String)PassportActivity.this.currentValues.get("phone"), (String)null, (TLRPC.TL_secureRequiredType)null, (String)null, (ArrayList)null, (SecureDocument)null, (ArrayList)null, (SecureDocument)null, (SecureDocument)null, new _$$Lambda$3CKII8dAtyIA_WQAJjP7Ab16CU8(PassportActivity.this), (PassportActivity.ErrorRunnable)null);
         } else {
            int var3;
            label63: {
               label68: {
                  this.lastError = var1.text;
                  if (this.verificationType == 3) {
                     var3 = this.nextType;
                     if (var3 == 4 || var3 == 2) {
                        break label68;
                     }
                  }

                  if (this.verificationType == 2) {
                     var3 = this.nextType;
                     if (var3 == 4 || var3 == 3) {
                        break label68;
                     }
                  }

                  if (this.verificationType != 4 || this.nextType != 2) {
                     break label63;
                  }
               }

               this.createTimer();
            }

            var3 = this.verificationType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }

            this.waitingForEvent = true;
            if (this.verificationType != 3) {
               AlertsCreator.processError(PassportActivity.access$12100(PassportActivity.this), var1, PassportActivity.this, var2);
            }

            PassportActivity.this.showEditDoneProgress(true, false);
            if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
               if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                  this.onBackPressed(true);
                  PassportActivity.this.setPage(0, true, (Bundle)null);
               }
            } else {
               var3 = 0;

               while(true) {
                  EditTextBoldCursor[] var4 = this.codeField;
                  if (var3 >= var4.length) {
                     var4[0].requestFocus();
                     break;
                  }

                  var4[var3].setText("");
                  ++var3;
               }
            }
         }

      }

      // $FF: synthetic method
      public void lambda$onBackPressed$8$PassportActivity$PhoneConfirmationView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         PassportActivity.this.setPage(0, true, (Bundle)null);
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$7$PassportActivity$PhoneConfirmationView(TLRPC.TL_account_verifyPhone var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$PhoneConfirmationView$pKHVwoJ6geCuurl3AWwR7b5outU(this, var3, var1));
      }

      // $FF: synthetic method
      public void lambda$resendCode$3$PassportActivity$PhoneConfirmationView(Bundle var1, TLRPC.TL_auth_resendCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$PhoneConfirmationView$UHXDno5AXqEJ78FjOAmHbJDCDI4(this, var4, var1, var3, var2));
      }

      // $FF: synthetic method
      public boolean lambda$setParams$4$PassportActivity$PhoneConfirmationView(int var1, View var2, int var3, KeyEvent var4) {
         if (var3 == 67 && this.codeField[var1].length() == 0 && var1 > 0) {
            EditTextBoldCursor[] var5 = this.codeField;
            --var1;
            var5[var1].setSelection(var5[var1].length());
            this.codeField[var1].requestFocus();
            this.codeField[var1].dispatchKeyEvent(var4);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$setParams$5$PassportActivity$PhoneConfirmationView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      public boolean needBackButton() {
         return true;
      }

      public boolean onBackPressed(boolean var1) {
         if (!var1) {
            AlertDialog.Builder var4 = new AlertDialog.Builder(PassportActivity.this.getParentActivity());
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setMessage(LocaleController.getString("StopVerification", 2131560831));
            var4.setPositiveButton(LocaleController.getString("Continue", 2131559153), (OnClickListener)null);
            var4.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$PassportActivity$PhoneConfirmationView$WGkFAmcyRqIzKtaNtQuEsZZn98M(this));
            PassportActivity.this.showDialog(var4.create());
            return false;
         } else {
            TLRPC.TL_auth_cancelCode var2 = new TLRPC.TL_auth_cancelCode();
            var2.phone_number = this.phone;
            var2.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(PassportActivity.access$12000(PassportActivity.this)).sendRequest(var2, _$$Lambda$PassportActivity$PhoneConfirmationView$V0ORURFAuRbPRy3yre_3sf3Qzvs.INSTANCE, 2);
            this.destroyTimer();
            this.destroyCodeTimer();
            this.currentParams = null;
            int var3 = this.verificationType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(false);
               NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(false);
               NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }

            this.waitingForEvent = false;
            return true;
         }
      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onDestroyActivity() {
         super.onDestroyActivity();
         int var1 = this.verificationType;
         if (var1 == 2) {
            AndroidUtilities.setWaitingForSms(false);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
         } else if (var1 == 3) {
            AndroidUtilities.setWaitingForCall(false);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
         }

         this.waitingForEvent = false;
         this.destroyTimer();
         this.destroyCodeTimer();
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         if (this.verificationType != 3 && this.blueImageView != null) {
            var3 = this.confirmTextView.getBottom();
            var2 = this.getMeasuredHeight() - var3;
            TextView var6;
            if (this.problemText.getVisibility() == 0) {
               var4 = this.problemText.getMeasuredHeight();
               var2 = var2 + var3 - var4;
               var6 = this.problemText;
               var6.layout(var6.getLeft(), var2, this.problemText.getRight(), var4 + var2);
            } else if (this.timeText.getVisibility() == 0) {
               var4 = this.timeText.getMeasuredHeight();
               var2 = var2 + var3 - var4;
               var6 = this.timeText;
               var6.layout(var6.getLeft(), var2, this.timeText.getRight(), var4 + var2);
            } else {
               var2 += var3;
            }

            var4 = this.codeFieldContainer.getMeasuredHeight();
            var2 = (var2 - var3 - var4) / 2 + var3;
            LinearLayout var7 = this.codeFieldContainer;
            var7.layout(var7.getLeft(), var2, this.codeFieldContainer.getRight(), var4 + var2);
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (this.verificationType != 3) {
            ImageView var3 = this.blueImageView;
            if (var3 != null) {
               int var4 = var3.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0F);
               var1 = AndroidUtilities.dp(80.0F);
               var2 = AndroidUtilities.dp(291.0F);
               if (PassportActivity.this.scrollHeight - var4 < var1) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var4 + var1);
               } else if (PassportActivity.this.scrollHeight > var2) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var2);
               } else {
                  this.setMeasuredDimension(this.getMeasuredWidth(), PassportActivity.this.scrollHeight);
               }
            }
         }

      }

      public void onNextPressed() {
         if (!this.nextPressed) {
            String var1 = this.getCode();
            if (TextUtils.isEmpty(var1)) {
               AndroidUtilities.shakeView(this.codeFieldContainer, 2.0F, 0);
            } else {
               this.nextPressed = true;
               int var2 = this.verificationType;
               if (var2 == 2) {
                  AndroidUtilities.setWaitingForSms(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
               } else if (var2 == 3) {
                  AndroidUtilities.setWaitingForCall(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
               }

               this.waitingForEvent = false;
               PassportActivity.this.showEditDoneProgress(true, true);
               TLRPC.TL_account_verifyPhone var3 = new TLRPC.TL_account_verifyPhone();
               var3.phone_number = this.phone;
               var3.phone_code = var1;
               var3.phone_code_hash = this.phoneHash;
               this.destroyTimer();
               PassportActivity.this.needShowProgress();
               ConnectionsManager.getInstance(PassportActivity.access$11900(PassportActivity.this)).sendRequest(var3, new _$$Lambda$PassportActivity$PhoneConfirmationView$H7W_zFejxtN_G3EXdQMEm98R9oE(this, var3), 2);
            }
         }
      }

      public void onShow() {
         super.onShow();
         LinearLayout var1 = this.codeFieldContainer;
         if (var1 != null && var1.getVisibility() == 0) {
            for(int var2 = this.codeField.length - 1; var2 >= 0; --var2) {
               if (var2 == 0 || this.codeField[var2].length() != 0) {
                  this.codeField[var2].requestFocus();
                  EditTextBoldCursor[] var3 = this.codeField;
                  var3[var2].setSelection(var3[var2].length());
                  AndroidUtilities.showKeyboard(this.codeField[var2]);
                  break;
               }
            }
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.waitingForEvent = true;
            int var3 = this.verificationType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }

            this.currentParams = var1;
            this.phone = var1.getString("phone");
            this.phoneHash = var1.getString("phoneHash");
            var3 = var1.getInt("timeout");
            this.time = var3;
            this.timeout = var3;
            this.openTime = (int)(System.currentTimeMillis() / 1000L);
            this.nextType = var1.getInt("nextType");
            this.pattern = var1.getString("pattern");
            this.length = var1.getInt("length");
            if (this.length == 0) {
               this.length = 5;
            }

            EditTextBoldCursor[] var4 = this.codeField;
            Object var8 = "";
            byte var10 = 8;
            final int var5;
            if (var4 != null && var4.length == this.length) {
               var5 = 0;

               while(true) {
                  var4 = this.codeField;
                  if (var5 >= var4.length) {
                     break;
                  }

                  var4[var5].setText("");
                  ++var5;
               }
            } else {
               this.codeField = new EditTextBoldCursor[this.length];

               for(var5 = 0; var5 < this.length; ++var5) {
                  this.codeField[var5] = new EditTextBoldCursor(this.getContext());
                  this.codeField[var5].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var5].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                  this.codeField[var5].setCursorSize(AndroidUtilities.dp(20.0F));
                  this.codeField[var5].setCursorWidth(1.5F);
                  Drawable var11 = this.getResources().getDrawable(2131165811).mutate();
                  var11.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Mode.MULTIPLY));
                  this.codeField[var5].setBackgroundDrawable(var11);
                  this.codeField[var5].setImeOptions(268435461);
                  this.codeField[var5].setTextSize(1, 20.0F);
                  this.codeField[var5].setMaxLines(1);
                  this.codeField[var5].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  this.codeField[var5].setPadding(0, 0, 0, 0);
                  this.codeField[var5].setGravity(49);
                  if (this.verificationType == 3) {
                     this.codeField[var5].setEnabled(false);
                     this.codeField[var5].setInputType(0);
                     this.codeField[var5].setVisibility(8);
                  } else {
                     this.codeField[var5].setInputType(3);
                  }

                  LinearLayout var12 = this.codeFieldContainer;
                  EditTextBoldCursor var6 = this.codeField[var5];
                  byte var7;
                  if (var5 != this.length - 1) {
                     var7 = 7;
                  } else {
                     var7 = 0;
                  }

                  var12.addView(var6, LayoutHelper.createLinear(34, 36, 1, 0, 0, var7, 0));
                  this.codeField[var5].addTextChangedListener(new TextWatcher() {
                     public void afterTextChanged(Editable var1) {
                        if (!PhoneConfirmationView.this.ignoreOnTextChange) {
                           int var2 = var1.length();
                           if (var2 >= 1) {
                              if (var2 > 1) {
                                 String var3 = var1.toString();
                                 PhoneConfirmationView.this.ignoreOnTextChange = true;

                                 for(int var4 = 0; var4 < Math.min(PhoneConfirmationView.this.length - var5, var2); ++var4) {
                                    if (var4 == 0) {
                                       var1.replace(0, var2, var3.substring(var4, var4 + 1));
                                    } else {
                                       PhoneConfirmationView.this.codeField[var5 + var4].setText(var3.substring(var4, var4 + 1));
                                    }
                                 }

                                 PhoneConfirmationView.this.ignoreOnTextChange = false;
                              }

                              if (var5 != PhoneConfirmationView.this.length - 1) {
                                 PhoneConfirmationView.this.codeField[var5 + 1].setSelection(PhoneConfirmationView.this.codeField[var5 + 1].length());
                                 PhoneConfirmationView.this.codeField[var5 + 1].requestFocus();
                              }

                              if ((var5 == PhoneConfirmationView.this.length - 1 || var5 == PhoneConfirmationView.this.length - 2 && var2 >= 2) && PhoneConfirmationView.this.getCode().length() == PhoneConfirmationView.this.length) {
                                 PhoneConfirmationView.this.onNextPressed();
                              }
                           }

                        }
                     }

                     public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }

                     public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }
                  });
                  this.codeField[var5].setOnKeyListener(new _$$Lambda$PassportActivity$PhoneConfirmationView$Hi5vGNsckWBWmshMz8y5BhPDMNw(this, var5));
                  this.codeField[var5].setOnEditorActionListener(new _$$Lambda$PassportActivity$PhoneConfirmationView$IfZlFmJVSS8IJyDQ8zJhpbvhePs(this));
               }
            }

            PassportActivity.ProgressView var13 = this.progressView;
            byte var16;
            if (var13 != null) {
               if (this.nextType != 0) {
                  var16 = 0;
               } else {
                  var16 = 8;
               }

               var13.setVisibility(var16);
            }

            if (this.phone != null) {
               PhoneFormat var14 = PhoneFormat.getInstance();
               StringBuilder var17 = new StringBuilder();
               var17.append("+");
               var17.append(this.phone);
               String var15 = var14.format(var17.toString());
               var5 = this.verificationType;
               if (var5 == 2) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131560721, LocaleController.addNbsp(var15)));
               } else if (var5 == 3) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131560719, LocaleController.addNbsp(var15)));
               } else if (var5 == 4) {
                  var8 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131560720, LocaleController.addNbsp(var15)));
               }

               this.confirmTextView.setText((CharSequence)var8);
               if (this.verificationType != 3) {
                  AndroidUtilities.showKeyboard(this.codeField[0]);
                  this.codeField[0].requestFocus();
               } else {
                  AndroidUtilities.hideKeyboard(this.codeField[0]);
               }

               this.destroyTimer();
               this.destroyCodeTimer();
               this.lastCurrentTime = (double)System.currentTimeMillis();
               if (this.verificationType == 3) {
                  var5 = this.nextType;
                  if (var5 == 4 || var5 == 2) {
                     this.problemText.setVisibility(8);
                     this.timeText.setVisibility(0);
                     var3 = this.nextType;
                     if (var3 == 4) {
                        this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 1, 0));
                     } else if (var3 == 2) {
                        this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 1, 0));
                     }

                     this.createTimer();
                     return;
                  }
               }

               TextView var9;
               if (this.verificationType == 2) {
                  var5 = this.nextType;
                  if (var5 == 4 || var5 == 3) {
                     this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 2, 0));
                     var9 = this.problemText;
                     if (this.time < 1000) {
                        var16 = 0;
                     } else {
                        var16 = 8;
                     }

                     var9.setVisibility(var16);
                     var9 = this.timeText;
                     if (this.time >= 1000) {
                        var10 = 0;
                     }

                     var9.setVisibility(var10);
                     this.createTimer();
                     return;
                  }
               }

               if (this.verificationType == 4 && this.nextType == 2) {
                  this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 2, 0));
                  var9 = this.problemText;
                  if (this.time < 1000) {
                     var16 = 0;
                  } else {
                     var16 = 8;
                  }

                  var9.setVisibility(var16);
                  var9 = this.timeText;
                  if (this.time >= 1000) {
                     var10 = 0;
                  }

                  var9.setVisibility(var10);
                  this.createTimer();
               } else {
                  this.timeText.setVisibility(8);
                  this.problemText.setVisibility(8);
                  this.createCodeTimer();
               }

            }
         }
      }
   }

   private class ProgressView extends View {
      private Paint paint = new Paint();
      private Paint paint2 = new Paint();
      private float progress;

      public ProgressView(Context var2) {
         super(var2);
         this.paint.setColor(Theme.getColor("login_progressInner"));
         this.paint2.setColor(Theme.getColor("login_progressOuter"));
      }

      protected void onDraw(Canvas var1) {
         float var2 = (float)((int)((float)this.getMeasuredWidth() * this.progress));
         var1.drawRect(0.0F, 0.0F, var2, (float)this.getMeasuredHeight(), this.paint2);
         var1.drawRect(var2, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
      }

      public void setProgress(float var1) {
         this.progress = var1;
         this.invalidate();
      }
   }

   public class SecureDocumentCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
      private int TAG = DownloadController.getInstance(PassportActivity.access$1500(PassportActivity.this)).generateObserverTag();
      private int buttonState;
      private SecureDocument currentSecureDocument;
      private BackupImageView imageView;
      private RadialProgress radialProgress = new RadialProgress(this);
      private TextView textView;
      private TextView valueTextView;

      public SecureDocumentCell(Context var2) {
         super(var2);
         this.imageView = new BackupImageView(var2);
         BackupImageView var9 = this.imageView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var9, LayoutHelper.createFrame(48, 48.0F, var5 | 48, 21.0F, 8.0F, 21.0F, 0.0F));
         this.textView = new TextView(var2);
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         this.textView.setEllipsize(TruncateAt.END);
         TextView var10 = this.textView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var10.setGravity(var5 | 16);
         var10 = this.textView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var3 = LocaleController.isRTL;
         byte var6 = 21;
         byte var7;
         if (var3) {
            var7 = 21;
         } else {
            var7 = 81;
         }

         float var8 = (float)var7;
         if (LocaleController.isRTL) {
            var7 = 81;
         } else {
            var7 = 21;
         }

         this.addView(var10, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var8, 10.0F, (float)var7, 0.0F));
         this.valueTextView = new TextView(var2);
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.valueTextView.setTextSize(1, 13.0F);
         var10 = this.valueTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var10.setGravity(var5);
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setPadding(0, 0, 0, 0);
         var10 = this.valueTextView;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 21;
         } else {
            var7 = 81;
         }

         var8 = (float)var7;
         var7 = var6;
         if (LocaleController.isRTL) {
            var7 = 81;
         }

         this.addView(var10, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var8, 35.0F, (float)var7, 0.0F));
         this.setWillNotDraw(false);
      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         boolean var5 = super.drawChild(var1, var2, var3);
         if (var2 == this.imageView) {
            this.radialProgress.draw(var1);
         }

         return var5;
      }

      public int getObserverTag() {
         return this.TAG;
      }

      public void invalidate() {
         super.invalidate();
         this.textView.invalidate();
      }

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

      public void onFailedDownload(String var1, boolean var2) {
         this.updateButtonState(false);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         var3 = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() - AndroidUtilities.dp(24.0F)) / 2;
         var2 = this.imageView.getTop() + (this.imageView.getMeasuredHeight() - AndroidUtilities.dp(24.0F)) / 2;
         this.radialProgress.setProgressRect(var3, var2, AndroidUtilities.dp(24.0F) + var3, AndroidUtilities.dp(24.0F) + var2);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + 1, 1073741824));
      }

      public void onProgressDownload(String var1, float var2) {
         this.radialProgress.setProgress(var2, true);
         if (this.buttonState != 1) {
            this.updateButtonState(false);
         }

      }

      public void onProgressUpload(String var1, float var2, boolean var3) {
         this.radialProgress.setProgress(var2, true);
      }

      public void onSuccessDownload(String var1) {
         this.radialProgress.setProgress(1.0F, true);
         this.updateButtonState(true);
      }

      public void setTextAndValueAndImage(String var1, CharSequence var2, SecureDocument var3) {
         this.textView.setText(var1);
         this.valueTextView.setText(var2);
         this.imageView.setImage(var3, "48_48");
         this.currentSecureDocument = var3;
         this.updateButtonState(false);
      }

      public void setValue(CharSequence var1) {
         this.valueTextView.setText(var1);
      }

      public void updateButtonState(boolean var1) {
         String var2 = FileLoader.getAttachFileName(this.currentSecureDocument);
         boolean var3 = FileLoader.getPathToAttach(this.currentSecureDocument).exists();
         if (TextUtils.isEmpty(var2)) {
            this.radialProgress.setBackground((Drawable)null, false, false);
         } else {
            SecureDocument var4 = this.currentSecureDocument;
            String var5 = var4.path;
            float var6 = 0.0F;
            float var7 = 0.0F;
            Float var8;
            RadialProgress var9;
            if (var5 != null) {
               if (var4.inputFile != null) {
                  DownloadController.getInstance(PassportActivity.access$1600(PassportActivity.this)).removeLoadingFileObserver(this);
                  this.radialProgress.setBackground((Drawable)null, false, var1);
                  this.buttonState = -1;
               } else {
                  DownloadController.getInstance(PassportActivity.access$1700(PassportActivity.this)).addLoadingFileObserver(this.currentSecureDocument.path, this);
                  this.buttonState = 1;
                  var8 = ImageLoader.getInstance().getFileProgress(this.currentSecureDocument.path);
                  this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, var1);
                  var9 = this.radialProgress;
                  if (var8 != null) {
                     var7 = var8;
                  }

                  var9.setProgress(var7, false);
                  this.invalidate();
               }
            } else if (var3) {
               DownloadController.getInstance(PassportActivity.access$1800(PassportActivity.this)).removeLoadingFileObserver(this);
               this.buttonState = -1;
               this.radialProgress.setBackground((Drawable)null, false, var1);
               this.invalidate();
            } else {
               DownloadController.getInstance(PassportActivity.access$1900(PassportActivity.this)).addLoadingFileObserver(var2, this);
               this.buttonState = 1;
               var8 = ImageLoader.getInstance().getFileProgress(var2);
               this.radialProgress.setBackground(Theme.chat_photoStatesDrawables[5][0], true, var1);
               var9 = this.radialProgress;
               var7 = var6;
               if (var8 != null) {
                  var7 = var8;
               }

               var9.setProgress(var7, var1);
               this.invalidate();
            }

         }
      }
   }

   public class TextDetailSecureCell extends FrameLayout {
      private ImageView checkImageView;
      private boolean needDivider;
      private TextView textView;
      private TextView valueTextView;

      public TextDetailSecureCell(Context var2) {
         super(var2);
         byte var3;
         if (PassportActivity.this.currentActivityType == 8) {
            var3 = 21;
         } else {
            var3 = 51;
         }

         this.textView = new TextView(var2);
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         this.textView.setEllipsize(TruncateAt.END);
         TextView var9 = this.textView;
         boolean var4 = LocaleController.isRTL;
         byte var5 = 5;
         byte var6;
         if (var4) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var9.setGravity(var6 | 16);
         var9 = this.textView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         byte var7;
         if (LocaleController.isRTL) {
            var7 = var3;
         } else {
            var7 = 21;
         }

         float var8 = (float)var7;
         if (LocaleController.isRTL) {
            var7 = 21;
         } else {
            var7 = var3;
         }

         this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var6 | 48, var8, 10.0F, (float)var7, 0.0F));
         this.valueTextView = new TextView(var2);
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.valueTextView.setTextSize(1, 13.0F);
         var9 = this.valueTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var9.setGravity(var6);
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setEllipsize(TruncateAt.END);
         this.valueTextView.setPadding(0, 0, 0, 0);
         var9 = this.valueTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = var3;
         } else {
            var7 = 21;
         }

         var8 = (float)var7;
         if (LocaleController.isRTL) {
            var3 = 21;
         }

         this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var6 | 48, var8, 35.0F, (float)var3, 0.0F));
         this.checkImageView = new ImageView(var2);
         this.checkImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
         this.checkImageView.setImageResource(2131165858);
         ImageView var10 = this.checkImageView;
         var3 = var5;
         if (LocaleController.isRTL) {
            var3 = 3;
         }

         this.addView(var10, LayoutHelper.createFrame(-2, -2.0F, var3 | 48, 21.0F, 25.0F, 21.0F, 0.0F));
      }

      protected void onDraw(Canvas var1) {
         if (this.needDivider) {
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

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
      }

      public void setChecked(boolean var1) {
         ImageView var2 = this.checkImageView;
         byte var3;
         if (var1) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
      }

      public void setNeedDivider(boolean var1) {
         this.needDivider = var1;
         this.setWillNotDraw(this.needDivider ^ true);
         this.invalidate();
      }

      public void setTextAndValue(String var1, CharSequence var2, boolean var3) {
         this.textView.setText(var1);
         this.valueTextView.setText(var2);
         this.needDivider = var3;
         this.setWillNotDraw(var3 ^ true);
      }

      public void setValue(CharSequence var1) {
         this.valueTextView.setText(var1);
      }
   }
}
