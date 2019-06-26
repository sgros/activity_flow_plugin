package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
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
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SmsReceiver;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.ImageUpdater$ImageUpdaterDelegate$_CC;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SlideView;

@SuppressLint({"HardwareIds"})
public class LoginActivity extends BaseFragment {
   private static final int done_button = 1;
   private boolean checkPermissions = true;
   private boolean checkShowPermissions = true;
   private TLRPC.TL_help_termsOfService currentTermsOfService;
   private int currentViewNum;
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private ContextProgressView doneProgressView;
   private boolean newAccount;
   private Dialog permissionsDialog;
   private ArrayList permissionsItems = new ArrayList();
   private Dialog permissionsShowDialog;
   private ArrayList permissionsShowItems = new ArrayList();
   private int progressRequestId;
   private int scrollHeight;
   private boolean syncContacts = true;
   private SlideView[] views = new SlideView[9];

   public LoginActivity() {
   }

   public LoginActivity(int var1) {
      super.currentAccount = var1;
      this.newAccount = true;
   }

   // $FF: synthetic method
   static int access$2800(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2900(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$5900(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6100(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6200(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6400(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6500(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6600(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6700(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6800(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6900(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7000(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7300(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7400(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7500(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$8300(LoginActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$8400(LoginActivity var0) {
      return var0.currentAccount;
   }

   private void clearCurrentState() {
      Editor var1 = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
      var1.clear();
      var1.commit();
   }

   private void fillNextCodeParams(Bundle var1, TLRPC.TL_auth_sentCode var2) {
      TLRPC.TL_help_termsOfService var3 = var2.terms_of_service;
      if (var3 != null) {
         this.currentTermsOfService = var3;
      }

      var1.putString("phoneHash", var2.phone_code_hash);
      TLRPC.auth_CodeType var4 = var2.next_type;
      if (var4 instanceof TLRPC.TL_auth_codeTypeCall) {
         var1.putInt("nextType", 4);
      } else if (var4 instanceof TLRPC.TL_auth_codeTypeFlashCall) {
         var1.putInt("nextType", 3);
      } else if (var4 instanceof TLRPC.TL_auth_codeTypeSms) {
         var1.putInt("nextType", 2);
      }

      if (var2.type instanceof TLRPC.TL_auth_sentCodeTypeApp) {
         var1.putInt("type", 1);
         var1.putInt("length", var2.type.length);
         this.setPage(1, true, var1, false);
      } else {
         if (var2.timeout == 0) {
            var2.timeout = 60;
         }

         var1.putInt("timeout", var2.timeout * 1000);
         TLRPC.auth_SentCodeType var5 = var2.type;
         if (var5 instanceof TLRPC.TL_auth_sentCodeTypeCall) {
            var1.putInt("type", 4);
            var1.putInt("length", var2.type.length);
            this.setPage(4, true, var1, false);
         } else if (var5 instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
            var1.putInt("type", 3);
            var1.putString("pattern", var2.type.pattern);
            this.setPage(3, true, var1, false);
         } else if (var5 instanceof TLRPC.TL_auth_sentCodeTypeSms) {
            var1.putInt("type", 2);
            var1.putInt("length", var2.type.length);
            this.setPage(2, true, var1, false);
         }
      }

   }

   private Bundle loadCurrentState() {
      if (this.newAccount) {
         return null;
      } else {
         Exception var10000;
         label84: {
            Bundle var1;
            Iterator var2;
            boolean var10001;
            try {
               var1 = new Bundle();
               var2 = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().entrySet().iterator();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label84;
            }

            label81:
            while(true) {
               String var4;
               Object var5;
               label79:
               while(true) {
                  String[] var6;
                  while(true) {
                     try {
                        if (!var2.hasNext()) {
                           return var1;
                        }

                        Entry var3 = (Entry)var2.next();
                        var4 = (String)var3.getKey();
                        var5 = var3.getValue();
                        var6 = var4.split("_\\|_");
                        if (var6.length != 1) {
                           break;
                        }

                        if (!(var5 instanceof String)) {
                           break label79;
                        }

                        var1.putString(var4, (String)var5);
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label81;
                     }
                  }

                  Bundle var14;
                  try {
                     if (var6.length != 2) {
                        continue;
                     }

                     var14 = var1.getBundle(var6[0]);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label81;
                  }

                  Bundle var15 = var14;
                  if (var14 == null) {
                     try {
                        var15 = new Bundle();
                        var1.putBundle(var6[0], var15);
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label81;
                     }
                  }

                  try {
                     if (var5 instanceof String) {
                        var15.putString(var6[1], (String)var5);
                        continue;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label81;
                  }

                  try {
                     if (var5 instanceof Integer) {
                        var15.putInt(var6[1], (Integer)var5);
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label81;
                  }
               }

               try {
                  if (var5 instanceof Integer) {
                     var1.putInt(var4, (Integer)var5);
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var16 = var10000;
         FileLog.e((Throwable)var16);
         return null;
      }
   }

   private void needFinishActivity() {
      this.clearCurrentState();
      if (this.getParentActivity() instanceof LaunchActivity) {
         if (this.newAccount) {
            this.newAccount = false;
            ((LaunchActivity)this.getParentActivity()).switchToAccount(super.currentAccount, false);
            this.finishFragment();
         } else {
            this.presentFragment(new DialogsActivity((Bundle)null), true);
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
         }
      } else if (this.getParentActivity() instanceof ExternalActionActivity) {
         ((ExternalActionActivity)this.getParentActivity()).onFinishLogin();
      }

   }

   private void needShowAlert(String var1, String var2) {
      if (var2 != null && this.getParentActivity() != null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(var1);
         var3.setMessage(var2);
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var3.create());
      }

   }

   private void needShowInvalidAlert(String var1, boolean var2) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(this.getParentActivity());
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         if (var2) {
            var3.setMessage(LocaleController.getString("BannedPhoneNumber", 2131558831));
         } else {
            var3.setMessage(LocaleController.getString("InvalidPhoneNumber", 2131559674));
         }

         var3.setNeutralButton(LocaleController.getString("BotHelp", 2131558850), new _$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4(this, var2, var1));
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var3.create());
      }
   }

   private void needShowProgress(int var1) {
      this.progressRequestId = var1;
      this.showEditDoneProgress(true);
   }

   private void onAuthSuccess(TLRPC.TL_auth_authorization var1) {
      ConnectionsManager.getInstance(super.currentAccount).setUserId(var1.user.id);
      UserConfig.getInstance(super.currentAccount).clearConfig();
      MessagesController.getInstance(super.currentAccount).cleanup();
      UserConfig.getInstance(super.currentAccount).syncContacts = this.syncContacts;
      UserConfig.getInstance(super.currentAccount).setCurrentUser(var1.user);
      UserConfig.getInstance(super.currentAccount).saveConfig(true);
      MessagesStorage.getInstance(super.currentAccount).cleanup(true);
      ArrayList var2 = new ArrayList();
      var2.add(var1.user);
      MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var2, (ArrayList)null, true, true);
      MessagesController.getInstance(super.currentAccount).putUser(var1.user, false);
      ContactsController.getInstance(super.currentAccount).checkAppAccount();
      MessagesController.getInstance(super.currentAccount).getBlockedUsers(true);
      MessagesController.getInstance(super.currentAccount).checkProxyInfo(true);
      ConnectionsManager.getInstance(super.currentAccount).updateDcSettings();
      this.needFinishActivity();
   }

   private void putBundleToEditor(Bundle var1, Editor var2, String var3) {
      Iterator var4 = var1.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Object var6 = var1.get(var5);
         StringBuilder var7;
         if (var6 instanceof String) {
            if (var3 != null) {
               var7 = new StringBuilder();
               var7.append(var3);
               var7.append("_|_");
               var7.append(var5);
               var2.putString(var7.toString(), (String)var6);
            } else {
               var2.putString(var5, (String)var6);
            }
         } else if (var6 instanceof Integer) {
            if (var3 != null) {
               var7 = new StringBuilder();
               var7.append(var3);
               var7.append("_|_");
               var7.append(var5);
               var2.putInt(var7.toString(), (Integer)var6);
            } else {
               var2.putInt(var5, (Integer)var6);
            }
         } else if (var6 instanceof Bundle) {
            this.putBundleToEditor((Bundle)var6, var2, var5);
         }
      }

   }

   private void showEditDoneProgress(final boolean var1) {
      AnimatorSet var2 = this.doneItemAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.doneItemAnimation = new AnimatorSet();
      if (var1) {
         this.doneProgressView.setTag(1);
         this.doneProgressView.setVisibility(0);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneProgressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneProgressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneProgressView, "alpha", new float[]{1.0F})});
      } else {
         this.doneProgressView.setTag((Object)null);
         this.doneItem.getImageView().setVisibility(0);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneProgressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneProgressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneProgressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{1.0F})});
      }

      this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (LoginActivity.this.doneItemAnimation != null && LoginActivity.this.doneItemAnimation.equals(var1x)) {
               LoginActivity.this.doneItemAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (LoginActivity.this.doneItemAnimation != null && LoginActivity.this.doneItemAnimation.equals(var1x)) {
               if (!var1) {
                  LoginActivity.this.doneProgressView.setVisibility(4);
               } else {
                  LoginActivity.this.doneItem.getImageView().setVisibility(4);
               }
            }

         }
      });
      this.doneItemAnimation.setDuration(150L);
      this.doneItemAnimation.start();
   }

   public View createView(Context var1) {
      super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$onItemClick$0$LoginActivity$1(DialogInterface var1, int var2) {
            LoginActivity.this.views[LoginActivity.this.currentViewNum].onCancelPressed();
            LoginActivity.this.needHideProgress(true);
         }

         public void onItemClick(int var1) {
            if (var1 == 1) {
               if (LoginActivity.this.doneProgressView.getTag() != null) {
                  if (LoginActivity.this.getParentActivity() == null) {
                     return;
                  }

                  AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                  var2.setTitle(LocaleController.getString("AppName", 2131558635));
                  var2.setMessage(LocaleController.getString("StopLoading", 2131560827));
                  var2.setPositiveButton(LocaleController.getString("WaitMore", 2131561101), (OnClickListener)null);
                  var2.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$LoginActivity$1$lZ8la8kpyrJX7A2ctFfMuS75IVo(this));
                  LoginActivity.this.showDialog(var2.create());
               } else {
                  LoginActivity.this.views[LoginActivity.this.currentViewNum].onNextPressed();
               }
            } else if (var1 == -1 && LoginActivity.this.onBackPressed()) {
               LoginActivity.this.finishFragment();
            }

         }
      });
      ActionBarMenu var2 = super.actionBar.createMenu();
      super.actionBar.setAllowOverlayTitle(true);
      this.doneItem = var2.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.doneProgressView = new ContextProgressView(var1, 1);
      this.doneProgressView.setAlpha(0.0F);
      this.doneProgressView.setScaleX(0.1F);
      this.doneProgressView.setScaleY(0.1F);
      this.doneProgressView.setVisibility(4);
      this.doneItem.addView(this.doneProgressView, LayoutHelper.createFrame(-1, -1.0F));
      this.doneItem.setContentDescription(LocaleController.getString("Done", 2131559299));
      ScrollView var3 = new ScrollView(var1) {
         protected void onMeasure(int var1, int var2) {
            LoginActivity.this.scrollHeight = MeasureSpec.getSize(var2) - AndroidUtilities.dp(30.0F);
            super.onMeasure(var1, var2);
         }

         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            if (LoginActivity.this.currentViewNum == 1 || LoginActivity.this.currentViewNum == 2 || LoginActivity.this.currentViewNum == 4) {
               var2.bottom += AndroidUtilities.dp(40.0F);
            }

            return super.requestChildRectangleOnScreen(var1, var2, var3);
         }
      };
      var3.setFillViewport(true);
      super.fragmentView = var3;
      FrameLayout var11 = new FrameLayout(var1);
      var3.addView(var11, LayoutHelper.createScroll(-1, -2, 51));
      this.views[0] = new LoginActivity.PhoneView(var1);
      this.views[1] = new LoginActivity.LoginActivitySmsView(var1, 1);
      this.views[2] = new LoginActivity.LoginActivitySmsView(var1, 2);
      this.views[3] = new LoginActivity.LoginActivitySmsView(var1, 3);
      this.views[4] = new LoginActivity.LoginActivitySmsView(var1, 4);
      this.views[5] = new LoginActivity.LoginActivityRegisterView(var1);
      this.views[6] = new LoginActivity.LoginActivityPasswordView(var1);
      this.views[7] = new LoginActivity.LoginActivityRecoverView(var1);
      this.views[8] = new LoginActivity.LoginActivityResetWaitView(var1);
      int var4 = 0;

      while(true) {
         SlideView[] var9 = this.views;
         boolean var6;
         if (var4 >= var9.length) {
            Bundle var13 = this.loadCurrentState();
            Bundle var12 = var13;
            if (var13 != null) {
               label117: {
                  this.currentViewNum = var13.getInt("currentViewNum", 0);
                  if (var13.getInt("syncContacts", 1) == 1) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  this.syncContacts = var6;
                  var4 = this.currentViewNum;
                  if (var4 >= 1 && var4 <= 4) {
                     var4 = var13.getInt("open");
                     var12 = var13;
                     if (var4 == 0) {
                        break label117;
                     }

                     var12 = var13;
                     if (Math.abs(System.currentTimeMillis() / 1000L - (long)var4) < 86400L) {
                        break label117;
                     }

                     this.currentViewNum = 0;
                     this.clearCurrentState();
                  } else {
                     var12 = var13;
                     if (this.currentViewNum != 6) {
                        break label117;
                     }

                     LoginActivity.LoginActivityPasswordView var15 = (LoginActivity.LoginActivityPasswordView)this.views[6];
                     if (var15.passwordType != 0 && var15.current_salt1 != null) {
                        var12 = var13;
                        if (var15.current_salt2 != null) {
                           break label117;
                        }
                     }

                     this.currentViewNum = 0;
                     this.clearCurrentState();
                  }

                  var12 = null;
               }
            }

            var4 = 0;

            while(true) {
               SlideView[] var14 = this.views;
               if (var4 >= var14.length) {
                  super.actionBar.setTitle(var14[this.currentViewNum].getHeaderName());
                  return super.fragmentView;
               }

               if (var12 != null) {
                  if (var4 >= 1 && var4 <= 4) {
                     if (var4 == this.currentViewNum) {
                        var14[var4].restoreStateParams(var12);
                     }
                  } else {
                     this.views[var4].restoreStateParams(var12);
                  }
               }

               if (this.currentViewNum != var4) {
                  this.views[var4].setVisibility(8);
               } else {
                  ActionBar var16 = super.actionBar;
                  int var17;
                  if (!this.views[var4].needBackButton() && !this.newAccount) {
                     var17 = 0;
                  } else {
                     var17 = 2131165409;
                  }

                  var16.setBackButtonImage(var17);
                  this.views[var4].setVisibility(0);
                  this.views[var4].onShow();
                  if (var4 == 3 || var4 == 8) {
                     this.doneItem.setVisibility(8);
                  }
               }

               ++var4;
            }
         }

         SlideView var10 = var9[var4];
         byte var5;
         if (var4 == 0) {
            var5 = 0;
         } else {
            var5 = 8;
         }

         var10.setVisibility(var5);
         var10 = this.views[var4];
         var6 = AndroidUtilities.isTablet();
         float var7 = 18.0F;
         float var8;
         if (var6) {
            var8 = 26.0F;
         } else {
            var8 = 18.0F;
         }

         if (AndroidUtilities.isTablet()) {
            var7 = 26.0F;
         }

         var11.addView(var10, LayoutHelper.createFrame(-1, -1.0F, 51, var8, 30.0F, var7, 0.0F));
         ++var4;
      }
   }

   public ThemeDescription[] getThemeDescriptions() {
      int var1 = 0;

      while(true) {
         SlideView[] var2 = this.views;
         if (var1 >= var2.length) {
            LoginActivity.PhoneView var3 = (LoginActivity.PhoneView)var2[0];
            LoginActivity.LoginActivitySmsView var4 = (LoginActivity.LoginActivitySmsView)var2[1];
            LoginActivity.LoginActivitySmsView var5 = (LoginActivity.LoginActivitySmsView)var2[2];
            LoginActivity.LoginActivitySmsView var6 = (LoginActivity.LoginActivitySmsView)var2[3];
            LoginActivity.LoginActivitySmsView var7 = (LoginActivity.LoginActivitySmsView)var2[4];
            LoginActivity.LoginActivityRegisterView var8 = (LoginActivity.LoginActivityRegisterView)var2[5];
            LoginActivity.LoginActivityPasswordView var9 = (LoginActivity.LoginActivityPasswordView)var2[6];
            LoginActivity.LoginActivityRecoverView var10 = (LoginActivity.LoginActivityRecoverView)var2[7];
            LoginActivity.LoginActivityResetWaitView var11 = (LoginActivity.LoginActivityResetWaitView)var2[8];
            ArrayList var12 = new ArrayList();
            var12.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
            var12.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
            var12.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
            var12.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
            var12.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
            var12.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
            var12.add(new ThemeDescription(var3.countryButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var3.view, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayLine"));
            var12.add(new ThemeDescription(var3.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var3.codeField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var3.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var3.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var3.phoneField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var3.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var12.add(new ThemeDescription(var3.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var3.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var3.textView2, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var9.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var9.codeField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var9.codeField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var12.add(new ThemeDescription(var9.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var9.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var9.cancelButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var9.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText6"));
            var12.add(new ThemeDescription(var9.resetAccountText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var8.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var8.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var12.add(new ThemeDescription(var8.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var8.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var8.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var8.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var12.add(new ThemeDescription(var8.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var8.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var8.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var8.wrongNumber, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var8.privacyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var8.privacyView, ThemeDescription.FLAG_LINKCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"));
            var12.add(new ThemeDescription(var10.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var10.codeField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var10.codeField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"));
            var12.add(new ThemeDescription(var10.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"));
            var12.add(new ThemeDescription(var10.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
            var12.add(new ThemeDescription(var10.cancelButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var11.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var11.resetAccountText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var11.resetAccountTime, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var11.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var11.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText6"));
            var12.add(new ThemeDescription(var4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            if (var4.codeField != null) {
               for(var1 = 0; var1 < var4.codeField.length; ++var1) {
                  var12.add(new ThemeDescription(var4.codeField[var1], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                  var12.add(new ThemeDescription(var4.codeField[var1], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
               }
            }

            var12.add(new ThemeDescription(var4.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var4.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var4.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
            var12.add(new ThemeDescription(var4.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
            var12.add(new ThemeDescription(var4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
            var12.add(new ThemeDescription(var5.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var5.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            if (var5.codeField != null) {
               for(var1 = 0; var1 < var5.codeField.length; ++var1) {
                  var12.add(new ThemeDescription(var5.codeField[var1], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                  var12.add(new ThemeDescription(var5.codeField[var1], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
               }
            }

            var12.add(new ThemeDescription(var5.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var5.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var5.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
            var12.add(new ThemeDescription(var5.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
            var12.add(new ThemeDescription(var5.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var5.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
            var12.add(new ThemeDescription(var6.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var6.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            if (var6.codeField != null) {
               for(var1 = 0; var1 < var6.codeField.length; ++var1) {
                  var12.add(new ThemeDescription(var6.codeField[var1], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                  var12.add(new ThemeDescription(var6.codeField[var1], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
               }
            }

            var12.add(new ThemeDescription(var6.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var6.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var6.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
            var12.add(new ThemeDescription(var6.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
            var12.add(new ThemeDescription(var6.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var6.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
            var12.add(new ThemeDescription(var7.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var7.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            if (var7.codeField != null) {
               for(var1 = 0; var1 < var7.codeField.length; ++var1) {
                  var12.add(new ThemeDescription(var7.codeField[var1], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
                  var12.add(new ThemeDescription(var7.codeField[var1], ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"));
               }
            }

            var12.add(new ThemeDescription(var7.timeText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText6"));
            var12.add(new ThemeDescription(var7.problemText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText4"));
            var12.add(new ThemeDescription(var7.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressInner"));
            var12.add(new ThemeDescription(var7.progressView, 0, new Class[]{LoginActivity.ProgressView.class}, new String[]{"paint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "login_progressOuter"));
            var12.add(new ThemeDescription(var7.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
            var12.add(new ThemeDescription(var7.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
            return (ThemeDescription[])var12.toArray(new ThemeDescription[0]);
         }

         if (var2[var1] == null) {
            return new ThemeDescription[0];
         }

         ++var1;
      }
   }

   // $FF: synthetic method
   public void lambda$needShowInvalidAlert$0$LoginActivity(boolean var1, String var2, DialogInterface var3, int var4) {
      label36: {
         boolean var10001;
         String var5;
         Intent var12;
         try {
            PackageInfo var11 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            var5 = String.format(Locale.US, "%s (%d)", var11.versionName, var11.versionCode);
            var12 = new Intent("android.intent.action.SEND");
            var12.setType("message/rfc822");
            var12.putExtra("android.intent.extra.EMAIL", new String[]{"login@stel.com"});
         } catch (Exception var10) {
            var10001 = false;
            break label36;
         }

         StringBuilder var6;
         if (var1) {
            try {
               var6 = new StringBuilder();
               var6.append("Banned phone number: ");
               var6.append(var2);
               var12.putExtra("android.intent.extra.SUBJECT", var6.toString());
               var6 = new StringBuilder();
               var6.append("I'm trying to use my mobile phone number: ");
               var6.append(var2);
               var6.append("\nBut Telegram says it's banned. Please help.\n\nApp version: ");
               var6.append(var5);
               var6.append("\nOS version: SDK ");
               var6.append(VERSION.SDK_INT);
               var6.append("\nDevice Name: ");
               var6.append(Build.MANUFACTURER);
               var6.append(Build.MODEL);
               var6.append("\nLocale: ");
               var6.append(Locale.getDefault());
               var12.putExtra("android.intent.extra.TEXT", var6.toString());
            } catch (Exception var9) {
               var10001 = false;
               break label36;
            }
         } else {
            try {
               var6 = new StringBuilder();
               var6.append("Invalid phone number: ");
               var6.append(var2);
               var12.putExtra("android.intent.extra.SUBJECT", var6.toString());
               var6 = new StringBuilder();
               var6.append("I'm trying to use my mobile phone number: ");
               var6.append(var2);
               var6.append("\nBut Telegram says it's invalid. Please help.\n\nApp version: ");
               var6.append(var5);
               var6.append("\nOS version: SDK ");
               var6.append(VERSION.SDK_INT);
               var6.append("\nDevice Name: ");
               var6.append(Build.MANUFACTURER);
               var6.append(Build.MODEL);
               var6.append("\nLocale: ");
               var6.append(Locale.getDefault());
               var12.putExtra("android.intent.extra.TEXT", var6.toString());
            } catch (Exception var8) {
               var10001 = false;
               break label36;
            }
         }

         try {
            this.getParentActivity().startActivity(Intent.createChooser(var12, "Send email..."));
            return;
         } catch (Exception var7) {
            var10001 = false;
         }
      }

      this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("NoMailInstalled", 2131559927));
   }

   public void needHideProgress(boolean var1) {
      if (this.progressRequestId != 0) {
         if (var1) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.progressRequestId, true);
         }

         this.progressRequestId = 0;
      }

      this.showEditDoneProgress(false);
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      LoginActivity.LoginActivityRegisterView var4 = (LoginActivity.LoginActivityRegisterView)this.views[5];
      if (var4 != null) {
         var4.imageUpdater.onActivityResult(var1, var2, var3);
      }

   }

   public boolean onBackPressed() {
      int var1 = this.currentViewNum;
      int var2 = 0;
      if (var1 == 0) {
         while(true) {
            SlideView[] var3 = this.views;
            if (var2 >= var3.length) {
               this.clearCurrentState();
               return true;
            }

            if (var3[var2] != null) {
               var3[var2].onDestroyActivity();
            }

            ++var2;
         }
      } else {
         if (var1 == 6) {
            this.views[var1].onBackPressed(true);
            this.setPage(0, true, (Bundle)null, true);
         } else if (var1 != 7 && var1 != 8) {
            if (var1 >= 1 && var1 <= 4) {
               if (this.views[var1].onBackPressed(false)) {
                  this.setPage(0, true, (Bundle)null, true);
               }
            } else {
               var2 = this.currentViewNum;
               if (var2 == 5) {
                  ((LoginActivity.LoginActivityRegisterView)this.views[var2]).wrongNumber.callOnClick();
               }
            }
         } else {
            this.views[this.currentViewNum].onBackPressed(true);
            this.setPage(6, true, (Bundle)null, true);
         }

         return false;
      }
   }

   protected void onDialogDismiss(Dialog var1) {
      if (VERSION.SDK_INT >= 23) {
         boolean var10001;
         if (var1 == this.permissionsDialog && !this.permissionsItems.isEmpty() && this.getParentActivity() != null) {
            try {
               this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
            } catch (Exception var2) {
               var10001 = false;
            }
         } else if (var1 == this.permissionsShowDialog && !this.permissionsShowItems.isEmpty() && this.getParentActivity() != null) {
            try {
               this.getParentActivity().requestPermissions((String[])this.permissionsShowItems.toArray(new String[0]), 7);
            } catch (Exception var3) {
               var10001 = false;
            }
         }
      }

   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      int var1 = 0;

      while(true) {
         SlideView[] var2 = this.views;
         if (var1 >= var2.length) {
            return;
         }

         if (var2[var1] != null) {
            var2[var1].onDestroyActivity();
         }

         ++var1;
      }
   }

   public void onPause() {
      super.onPause();
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
      if (this.newAccount) {
         ConnectionsManager.getInstance(super.currentAccount).setAppPaused(true, false);
      }

   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 6) {
         this.checkPermissions = false;
         var1 = this.currentViewNum;
         if (var1 == 0) {
            this.views[var1].onNextPressed();
         }
      } else if (var1 == 7) {
         this.checkShowPermissions = false;
         var1 = this.currentViewNum;
         if (var1 == 0) {
            ((LoginActivity.PhoneView)this.views[var1]).fillNumber();
         }
      }

   }

   public void onResume() {
      super.onResume();
      if (this.newAccount) {
         ConnectionsManager.getInstance(super.currentAccount).setAppPaused(false, false);
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);

      Exception var10000;
      label43: {
         int var1;
         boolean var10001;
         try {
            if (this.currentViewNum < 1 || this.currentViewNum > 4 || !(this.views[this.currentViewNum] instanceof LoginActivity.LoginActivitySmsView)) {
               return;
            }

            var1 = ((LoginActivity.LoginActivitySmsView)this.views[this.currentViewNum]).openTime;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label43;
         }

         if (var1 == 0) {
            return;
         }

         try {
            if (Math.abs(System.currentTimeMillis() / 1000L - (long)var1) >= 86400L) {
               this.views[this.currentViewNum].onBackPressed(true);
               this.setPage(0, false, (Bundle)null, true);
            }

            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      Exception var2 = var10000;
      FileLog.e((Throwable)var2);
   }

   public void saveSelfArgs(Bundle var1) {
      Exception var10000;
      label57: {
         byte var2;
         boolean var10001;
         label53: {
            label52: {
               try {
                  var1 = new Bundle();
                  var1.putInt("currentViewNum", this.currentViewNum);
                  if (this.syncContacts) {
                     break label52;
                  }
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label57;
               }

               var2 = 0;
               break label53;
            }

            var2 = 1;
         }

         try {
            var1.putInt("syncContacts", var2);
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label57;
         }

         int var10 = 0;

         while(true) {
            SlideView var3;
            try {
               if (var10 > this.currentViewNum) {
                  break;
               }

               var3 = this.views[var10];
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label57;
            }

            if (var3 != null) {
               try {
                  var3.saveStateParams(var1);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label57;
               }
            }

            ++var10;
         }

         try {
            Editor var11 = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
            var11.clear();
            this.putBundleToEditor(var1, var11, (String)null);
            var11.commit();
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var9 = var10000;
      FileLog.e((Throwable)var9);
   }

   public void setPage(int var1, boolean var2, Bundle var3, boolean var4) {
      if (var1 != 3 && var1 != 8) {
         if (var1 == 0) {
            this.checkPermissions = true;
            this.checkShowPermissions = true;
         }

         this.doneItem.setVisibility(0);
      } else {
         this.doneItem.setVisibility(8);
      }

      int var5 = 2131165409;
      if (var2) {
         SlideView[] var6 = this.views;
         final SlideView var7 = var6[this.currentViewNum];
         SlideView var11 = var6[var1];
         this.currentViewNum = var1;
         ActionBar var8 = super.actionBar;
         var1 = var5;
         if (!var11.needBackButton()) {
            if (this.newAccount) {
               var1 = var5;
            } else {
               var1 = 0;
            }
         }

         var8.setBackButtonImage(var1);
         var11.setParams(var3, false);
         super.actionBar.setTitle(var11.getHeaderName());
         this.setParentActivityTitle(var11.getHeaderName());
         var11.onShow();
         if (var4) {
            var1 = -AndroidUtilities.displaySize.x;
         } else {
            var1 = AndroidUtilities.displaySize.x;
         }

         var11.setX((float)var1);
         var11.setVisibility(0);
         AnimatorSet var10 = new AnimatorSet();
         var10.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var7.setVisibility(8);
               var7.setX(0.0F);
            }
         });
         Property var13 = View.TRANSLATION_X;
         if (var4) {
            var1 = AndroidUtilities.displaySize.x;
         } else {
            var1 = -AndroidUtilities.displaySize.x;
         }

         var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(var7, var13, new float[]{(float)var1}), ObjectAnimator.ofFloat(var11, View.TRANSLATION_X, new float[]{0.0F})});
         var10.setDuration(300L);
         var10.setInterpolator(new AccelerateDecelerateInterpolator());
         var10.start();
      } else {
         ActionBar var12 = super.actionBar;
         int var9 = var5;
         if (!this.views[var1].needBackButton()) {
            if (this.newAccount) {
               var9 = var5;
            } else {
               var9 = 0;
            }
         }

         var12.setBackButtonImage(var9);
         this.views[this.currentViewNum].setVisibility(8);
         this.currentViewNum = var1;
         this.views[var1].setParams(var3, false);
         this.views[var1].setVisibility(0);
         super.actionBar.setTitle(this.views[var1].getHeaderName());
         this.setParentActivityTitle(this.views[var1].getHeaderName());
         this.views[var1].onShow();
      }

   }

   public class LoginActivityPasswordView extends SlideView {
      private TextView cancelButton;
      private EditTextBoldCursor codeField;
      private TextView confirmTextView;
      private Bundle currentParams;
      private int current_g;
      private byte[] current_p;
      private byte[] current_salt1;
      private byte[] current_salt2;
      private byte[] current_srp_B;
      private long current_srp_id;
      private String email_unconfirmed_pattern;
      private boolean has_recovery;
      private String hint;
      private boolean nextPressed;
      private int passwordType;
      private String phoneCode;
      private String phoneHash;
      private String requestPhone;
      private TextView resetAccountButton;
      private TextView resetAccountText;

      public LoginActivityPasswordView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.confirmTextView = new TextView(var2);
         this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.confirmTextView.setTextSize(1, 14.0F);
         TextView var6 = this.confirmTextView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5);
         this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.confirmTextView.setText(LocaleController.getString("LoginPasswordText", 2131559789));
         var6 = this.confirmTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5));
         this.codeField = new EditTextBoldCursor(var2);
         this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.codeField.setCursorWidth(1.5F);
         this.codeField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.codeField.setHint(LocaleController.getString("LoginPassword", 2131559788));
         this.codeField.setImeOptions(268435461);
         this.codeField.setTextSize(1, 18.0F);
         this.codeField.setMaxLines(1);
         this.codeField.setPadding(0, 0, 0, 0);
         this.codeField.setInputType(129);
         this.codeField.setTransformationMethod(PasswordTransformationMethod.getInstance());
         this.codeField.setTypeface(Typeface.DEFAULT);
         EditTextBoldCursor var7 = this.codeField;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var7.setGravity(var5);
         this.addView(this.codeField, LayoutHelper.createLinear(-1, 36, 1, 0, 20, 0, 0));
         this.codeField.setOnEditorActionListener(new _$$Lambda$LoginActivity$LoginActivityPasswordView$TmxPaCI1XFUfKacPC6vKEsUBp8g(this));
         this.cancelButton = new TextView(var2);
         var6 = this.cancelButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         this.cancelButton.setText(LocaleController.getString("ForgotPassword", 2131559503));
         this.cancelButton.setTextSize(1, 14.0F);
         this.cancelButton.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.cancelButton.setPadding(0, AndroidUtilities.dp(14.0F), 0, 0);
         var6 = this.cancelButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-1, -2, var5 | 48));
         this.cancelButton.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityPasswordView$XuGku6jlA50Wtx4H_NfhHJUyWxg(this));
         this.resetAccountButton = new TextView(var2);
         var6 = this.resetAccountButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteRedText6"));
         this.resetAccountButton.setVisibility(8);
         this.resetAccountButton.setText(LocaleController.getString("ResetMyAccount", 2131560597));
         this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.resetAccountButton.setTextSize(1, 14.0F);
         this.resetAccountButton.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.resetAccountButton.setPadding(0, AndroidUtilities.dp(14.0F), 0, 0);
         var6 = this.resetAccountButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 48, 0, 34, 0, 0));
         this.resetAccountButton.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityPasswordView$K2ROoTSLPIpCLdI8XOROn58qsk4(this));
         this.resetAccountText = new TextView(var2);
         var6 = this.resetAccountText;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.resetAccountText.setVisibility(8);
         this.resetAccountText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.resetAccountText.setText(LocaleController.getString("ResetMyAccountText", 2131560598));
         this.resetAccountText.setTextSize(1, 14.0F);
         this.resetAccountText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var6 = this.resetAccountText;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 48, 0, 7, 0, 14));
      }

      private void onPasscodeError(boolean var1) {
         if (LoginActivity.this.getParentActivity() != null) {
            Vibrator var2 = (Vibrator)LoginActivity.this.getParentActivity().getSystemService("vibrator");
            if (var2 != null) {
               var2.vibrate(200L);
            }

            if (var1) {
               this.codeField.setText("");
            }

            AndroidUtilities.shakeView(this.confirmTextView, 2.0F, 0);
         }
      }

      public String getHeaderName() {
         return LocaleController.getString("LoginPassword", 2131559788);
      }

      // $FF: synthetic method
      public boolean lambda$new$0$LoginActivity$LoginActivityPasswordView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public void lambda$new$4$LoginActivity$LoginActivityPasswordView(View var1) {
         if (LoginActivity.this.doneProgressView.getTag() == null) {
            if (this.has_recovery) {
               LoginActivity.this.needShowProgress(0);
               TLRPC.TL_auth_requestPasswordRecovery var2 = new TLRPC.TL_auth_requestPasswordRecovery();
               ConnectionsManager.getInstance(LoginActivity.access$6900(LoginActivity.this)).sendRequest(var2, new _$$Lambda$LoginActivity$LoginActivityPasswordView$oCLNhngYy9ZhYZMHU9IaIdZfqM0(this), 10);
            } else {
               this.resetAccountText.setVisibility(0);
               this.resetAccountButton.setVisibility(0);
               AndroidUtilities.hideKeyboard(this.codeField);
               LoginActivity.this.needShowAlert(LocaleController.getString("RestorePasswordNoEitle", 2131560612), LocaleController.getString("RestorePasswordNoEmailText", 2131560611));
            }

         }
      }

      // $FF: synthetic method
      public void lambda$new$8$LoginActivity$LoginActivityPasswordView(View var1) {
         if (LoginActivity.this.doneProgressView.getTag() == null) {
            AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var2.setMessage(LocaleController.getString("ResetMyAccountWarningText", 2131560601));
            var2.setTitle(LocaleController.getString("ResetMyAccountWarning", 2131560599));
            var2.setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", 2131560600), new _$$Lambda$LoginActivity$LoginActivityPasswordView$Dp_mVB_gTl6zcz74Qvp_ffZE3zM(this));
            var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            LoginActivity.this.showDialog(var2.create());
         }
      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$LoginActivityPasswordView(TLRPC.TL_auth_passwordRecovery var1, DialogInterface var2, int var3) {
         Bundle var4 = new Bundle();
         var4.putString("email_unconfirmed_pattern", var1.email_pattern);
         LoginActivity.this.setPage(7, true, var4, false);
      }

      // $FF: synthetic method
      public void lambda$null$10$LoginActivity$LoginActivityPasswordView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$aCM70N9LbmHe_XZ8f0BkvGrFUr4(this, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$null$11$LoginActivity$LoginActivityPasswordView(TLRPC.TL_error var1, TLObject var2) {
         this.nextPressed = false;
         if (var1 != null && "SRP_ID_INVALID".equals(var1.text)) {
            TLRPC.TL_account_getPassword var5 = new TLRPC.TL_account_getPassword();
            ConnectionsManager.getInstance(LoginActivity.access$6600(LoginActivity.this)).sendRequest(var5, new _$$Lambda$LoginActivity$LoginActivityPasswordView$K_Ui4U920vN4BxkMQccwYDwF980(this), 8);
         } else {
            LoginActivity.this.needHideProgress(false);
            if (var1 == null) {
               LoginActivity.this.onAuthSuccess((TLRPC.TL_auth_authorization)var2);
            } else if (var1.text.equals("PASSWORD_HASH_INVALID")) {
               this.onPasscodeError(true);
            } else if (var1.text.startsWith("FLOOD_WAIT")) {
               int var3 = Utilities.parseInt(var1.text);
               String var4;
               if (var3 < 60) {
                  var4 = LocaleController.formatPluralString("Seconds", var3);
               } else {
                  var4 = LocaleController.formatPluralString("Minutes", var3 / 60);
               }

               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var4));
            } else {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
            }

         }
      }

      // $FF: synthetic method
      public void lambda$null$12$LoginActivity$LoginActivityPasswordView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$W_WptRfwGIPlNoTMqmdBUyrFBmQ(this, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$null$2$LoginActivity$LoginActivityPasswordView(TLRPC.TL_error var1, TLObject var2) {
         LoginActivity.this.needHideProgress(false);
         if (var1 == null) {
            TLRPC.TL_auth_passwordRecovery var7 = (TLRPC.TL_auth_passwordRecovery)var2;
            AlertDialog.Builder var4 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var4.setMessage(LocaleController.formatString("RestoreEmailSent", 2131560607, var7.email_pattern));
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LoginActivity$LoginActivityPasswordView$xtZEvWAVK5tRPP93_2SWWR4hemw(this, var7));
            Dialog var5 = LoginActivity.this.showDialog(var4.create());
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

            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var6));
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }

      // $FF: synthetic method
      public void lambda$null$3$LoginActivity$LoginActivityPasswordView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$UcCVsMRXQ_iTPOpCCyDnzwrWa_M(this, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$null$5$LoginActivity$LoginActivityPasswordView(TLRPC.TL_error var1) {
         LoginActivity.this.needHideProgress(false);
         if (var1 == null) {
            Bundle var3 = new Bundle();
            var3.putString("phoneFormated", this.requestPhone);
            var3.putString("phoneHash", this.phoneHash);
            var3.putString("code", this.phoneCode);
            LoginActivity.this.setPage(5, true, var3, false);
         } else if (var1.text.equals("2FA_RECENT_CONFIRM")) {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("ResetAccountCancelledAlert", 2131560586));
         } else if (var1.text.startsWith("2FA_CONFIRM_WAIT_")) {
            Bundle var2 = new Bundle();
            var2.putString("phoneFormated", this.requestPhone);
            var2.putString("phoneHash", this.phoneHash);
            var2.putString("code", this.phoneCode);
            var2.putInt("startTime", ConnectionsManager.getInstance(LoginActivity.access$6800(LoginActivity.this)).getCurrentTime());
            var2.putInt("waitTime", Utilities.parseInt(var1.text.replace("2FA_CONFIRM_WAIT_", "")));
            LoginActivity.this.setPage(8, true, var2, false);
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }

      // $FF: synthetic method
      public void lambda$null$6$LoginActivity$LoginActivityPasswordView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$d6NFC5NiYYxgW_wGlgA6Dyrzfcg(this, var2));
      }

      // $FF: synthetic method
      public void lambda$null$7$LoginActivity$LoginActivityPasswordView(DialogInterface var1, int var2) {
         LoginActivity.this.needShowProgress(0);
         TLRPC.TL_account_deleteAccount var3 = new TLRPC.TL_account_deleteAccount();
         var3.reason = "Forgot password";
         ConnectionsManager.getInstance(LoginActivity.access$6700(LoginActivity.this)).sendRequest(var3, new _$$Lambda$LoginActivity$LoginActivityPasswordView$2nlj3NklnLOo9ydoOLKoDWf5TVE(this), 10);
      }

      // $FF: synthetic method
      public void lambda$null$9$LoginActivity$LoginActivityPasswordView(TLRPC.TL_error var1, TLObject var2) {
         if (var1 == null) {
            TLRPC.TL_account_password var3 = (TLRPC.TL_account_password)var2;
            this.current_srp_B = var3.srp_B;
            this.current_srp_id = var3.srp_id;
            this.onNextPressed();
         }

      }

      // $FF: synthetic method
      public void lambda$onNextPressed$13$LoginActivity$LoginActivityPasswordView(String var1) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var2;
         if (this.passwordType == 1) {
            var2 = new TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow();
            var2.salt1 = this.current_salt1;
            var2.salt2 = this.current_salt2;
            var2.g = this.current_g;
            var2.p = this.current_p;
         } else {
            var2 = null;
         }

         boolean var3 = var2 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
         byte[] var6;
         if (var3) {
            var6 = SRPHelper.getX(AndroidUtilities.getStringBytes(var1), var2);
         } else {
            var6 = null;
         }

         TLRPC.TL_auth_checkPassword var4 = new TLRPC.TL_auth_checkPassword();
         _$$Lambda$LoginActivity$LoginActivityPasswordView$WrnSljxMnTCOO4pqUidVF24UWto var5 = new _$$Lambda$LoginActivity$LoginActivityPasswordView$WrnSljxMnTCOO4pqUidVF24UWto(this);
         if (var3) {
            var2.salt1 = this.current_salt1;
            var2.salt2 = this.current_salt2;
            var2.g = this.current_g;
            var2.p = this.current_p;
            var4.password = SRPHelper.startCheck(var6, this.current_srp_id, this.current_srp_B, var2);
            if (var4.password == null) {
               TLRPC.TL_error var7 = new TLRPC.TL_error();
               var7.text = "PASSWORD_HASH_INVALID";
               var5.run((TLObject)null, var7);
               return;
            }

            ConnectionsManager.getInstance(LoginActivity.access$6500(LoginActivity.this)).sendRequest(var4, var5, 10);
         }

      }

      // $FF: synthetic method
      public void lambda$onShow$14$LoginActivity$LoginActivityPasswordView() {
         EditTextBoldCursor var1 = this.codeField;
         if (var1 != null) {
            var1.requestFocus();
            var1 = this.codeField;
            var1.setSelection(var1.length());
            AndroidUtilities.showKeyboard(this.codeField);
         }

      }

      public boolean needBackButton() {
         return true;
      }

      public boolean onBackPressed(boolean var1) {
         this.nextPressed = false;
         LoginActivity.this.needHideProgress(true);
         this.currentParams = null;
         return true;
      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onNextPressed() {
         if (!this.nextPressed) {
            String var1 = this.codeField.getText().toString();
            if (var1.length() == 0) {
               this.onPasscodeError(false);
            } else {
               this.nextPressed = true;
               LoginActivity.this.needShowProgress(0);
               Utilities.globalQueue.postRunnable(new _$$Lambda$LoginActivity$LoginActivityPasswordView$t8xAmO8Vg_agS_AjQ9_oOEqu8kM(this, var1));
            }
         }
      }

      public void onShow() {
         super.onShow();
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$CMrfS4f7oV_czyRbH6gwt0E8MoQ(this), 100L);
      }

      public void restoreStateParams(Bundle var1) {
         this.currentParams = var1.getBundle("passview_params");
         Bundle var2 = this.currentParams;
         if (var2 != null) {
            this.setParams(var2, true);
         }

         String var3 = var1.getString("passview_code");
         if (var3 != null) {
            this.codeField.setText(var3);
         }

      }

      public void saveStateParams(Bundle var1) {
         String var2 = this.codeField.getText().toString();
         if (var2.length() != 0) {
            var1.putString("passview_code", var2);
         }

         Bundle var3 = this.currentParams;
         if (var3 != null) {
            var1.putBundle("passview_params", var3);
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            boolean var3 = var1.isEmpty();
            var2 = false;
            if (var3) {
               this.resetAccountButton.setVisibility(0);
               this.resetAccountText.setVisibility(0);
               AndroidUtilities.hideKeyboard(this.codeField);
            } else {
               this.resetAccountButton.setVisibility(8);
               this.resetAccountText.setVisibility(8);
               this.codeField.setText("");
               this.currentParams = var1;
               this.current_salt1 = Utilities.hexToBytes(this.currentParams.getString("current_salt1"));
               this.current_salt2 = Utilities.hexToBytes(this.currentParams.getString("current_salt2"));
               this.current_p = Utilities.hexToBytes(this.currentParams.getString("current_p"));
               this.current_g = this.currentParams.getInt("current_g");
               this.current_srp_B = Utilities.hexToBytes(this.currentParams.getString("current_srp_B"));
               this.current_srp_id = this.currentParams.getLong("current_srp_id");
               this.passwordType = this.currentParams.getInt("passwordType");
               this.hint = this.currentParams.getString("hint");
               if (this.currentParams.getInt("has_recovery") == 1) {
                  var2 = true;
               }

               this.has_recovery = var2;
               this.email_unconfirmed_pattern = this.currentParams.getString("email_unconfirmed_pattern");
               this.requestPhone = var1.getString("phoneFormated");
               this.phoneHash = var1.getString("phoneHash");
               this.phoneCode = var1.getString("code");
               String var4 = this.hint;
               if (var4 != null && var4.length() > 0) {
                  this.codeField.setHint(this.hint);
               } else {
                  this.codeField.setHint(LocaleController.getString("LoginPassword", 2131559788));
               }

            }
         }
      }
   }

   public class LoginActivityRecoverView extends SlideView {
      private TextView cancelButton;
      private EditTextBoldCursor codeField;
      private TextView confirmTextView;
      private Bundle currentParams;
      private String email_unconfirmed_pattern;
      private boolean nextPressed;

      public LoginActivityRecoverView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.confirmTextView = new TextView(var2);
         this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.confirmTextView.setTextSize(1, 14.0F);
         TextView var6 = this.confirmTextView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5);
         this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.confirmTextView.setText(LocaleController.getString("RestoreEmailSentInfo", 2131560608));
         var6 = this.confirmTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5));
         this.codeField = new EditTextBoldCursor(var2);
         this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.codeField.setCursorWidth(1.5F);
         this.codeField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.codeField.setHint(LocaleController.getString("PasswordCode", 2131560345));
         this.codeField.setImeOptions(268435461);
         this.codeField.setTextSize(1, 18.0F);
         this.codeField.setMaxLines(1);
         this.codeField.setPadding(0, 0, 0, 0);
         this.codeField.setInputType(3);
         this.codeField.setTransformationMethod(PasswordTransformationMethod.getInstance());
         this.codeField.setTypeface(Typeface.DEFAULT);
         EditTextBoldCursor var7 = this.codeField;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var7.setGravity(var5);
         this.addView(this.codeField, LayoutHelper.createLinear(-1, 36, 1, 0, 20, 0, 0));
         this.codeField.setOnEditorActionListener(new _$$Lambda$LoginActivity$LoginActivityRecoverView$qDtPayVjLCLCB__uwfYE1UHVJU0(this));
         this.cancelButton = new TextView(var2);
         var6 = this.cancelButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 80);
         this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         this.cancelButton.setTextSize(1, 14.0F);
         this.cancelButton.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.cancelButton.setPadding(0, AndroidUtilities.dp(14.0F), 0, 0);
         var6 = this.cancelButton;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 80, 0, 0, 0, 14));
         this.cancelButton.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityRecoverView$T2kwdtaqLpflrB1CM7xmzXbnaj8(this));
      }

      private void onPasscodeError(boolean var1) {
         if (LoginActivity.this.getParentActivity() != null) {
            Vibrator var2 = (Vibrator)LoginActivity.this.getParentActivity().getSystemService("vibrator");
            if (var2 != null) {
               var2.vibrate(200L);
            }

            if (var1) {
               this.codeField.setText("");
            }

            AndroidUtilities.shakeView(this.confirmTextView, 2.0F, 0);
         }
      }

      public String getHeaderName() {
         return LocaleController.getString("LoginPassword", 2131559788);
      }

      // $FF: synthetic method
      public boolean lambda$new$0$LoginActivity$LoginActivityRecoverView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public void lambda$new$2$LoginActivity$LoginActivityRecoverView(View var1) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
         var2.setMessage(LocaleController.getString("RestoreEmailTroubleText", 2131560610));
         var2.setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", 2131560612));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LoginActivity$LoginActivityRecoverView$GHrdHw8XOYZ2EOzzL9oTvnnduAY(this));
         Dialog var3 = LoginActivity.this.showDialog(var2.create());
         if (var3 != null) {
            var3.setCanceledOnTouchOutside(false);
            var3.setCancelable(false);
         }

      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$LoginActivityRecoverView(DialogInterface var1, int var2) {
         LoginActivity.this.setPage(6, true, new Bundle(), true);
      }

      // $FF: synthetic method
      public void lambda$null$3$LoginActivity$LoginActivityRecoverView(TLObject var1, DialogInterface var2, int var3) {
         LoginActivity.this.onAuthSuccess((TLRPC.TL_auth_authorization)var1);
      }

      // $FF: synthetic method
      public void lambda$null$4$LoginActivity$LoginActivityRecoverView(TLRPC.TL_error var1, TLObject var2) {
         LoginActivity.this.needHideProgress(false);
         this.nextPressed = false;
         if (var1 == null) {
            AlertDialog.Builder var4 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LoginActivity$LoginActivityRecoverView$I1bQ5rEP_TTiI07Nb2wIWAeHEMM(this, var2));
            var4.setMessage(LocaleController.getString("PasswordReset", 2131560351));
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            Dialog var5 = LoginActivity.this.showDialog(var4.create());
            if (var5 != null) {
               var5.setCanceledOnTouchOutside(false);
               var5.setCancelable(false);
            }
         } else if (var1.text.startsWith("CODE_INVALID")) {
            this.onPasscodeError(true);
         } else if (var1.text.startsWith("FLOOD_WAIT")) {
            int var3 = Utilities.parseInt(var1.text);
            String var6;
            if (var3 < 60) {
               var6 = LocaleController.formatPluralString("Seconds", var3);
            } else {
               var6 = LocaleController.formatPluralString("Minutes", var3 / 60);
            }

            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var6));
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }

      // $FF: synthetic method
      public void lambda$onNextPressed$5$LoginActivity$LoginActivityRecoverView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRecoverView$xRlt0wartDC6m84eJ3SO5QPoI50(this, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$onShow$6$LoginActivity$LoginActivityRecoverView() {
         EditTextBoldCursor var1 = this.codeField;
         if (var1 != null) {
            var1.requestFocus();
            var1 = this.codeField;
            var1.setSelection(var1.length());
         }

      }

      public boolean needBackButton() {
         return true;
      }

      public boolean onBackPressed(boolean var1) {
         LoginActivity.this.needHideProgress(true);
         this.currentParams = null;
         this.nextPressed = false;
         return true;
      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onNextPressed() {
         if (!this.nextPressed) {
            if (this.codeField.getText().toString().length() == 0) {
               this.onPasscodeError(false);
            } else {
               this.nextPressed = true;
               String var1 = this.codeField.getText().toString();
               if (var1.length() == 0) {
                  this.onPasscodeError(false);
               } else {
                  LoginActivity.this.needShowProgress(0);
                  TLRPC.TL_auth_recoverPassword var2 = new TLRPC.TL_auth_recoverPassword();
                  var2.code = var1;
                  ConnectionsManager.getInstance(LoginActivity.access$7500(LoginActivity.this)).sendRequest(var2, new _$$Lambda$LoginActivity$LoginActivityRecoverView$rgaN2I5iElJnNYqU1iHy0vwcr6w(this), 10);
               }
            }
         }
      }

      public void onShow() {
         super.onShow();
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRecoverView$G4hHYij0gEl198Zvu2E28bVCEaY(this), 100L);
      }

      public void restoreStateParams(Bundle var1) {
         this.currentParams = var1.getBundle("recoveryview_params");
         Bundle var2 = this.currentParams;
         if (var2 != null) {
            this.setParams(var2, true);
         }

         String var3 = var1.getString("recoveryview_code");
         if (var3 != null) {
            this.codeField.setText(var3);
         }

      }

      public void saveStateParams(Bundle var1) {
         String var2 = this.codeField.getText().toString();
         if (var2 != null && var2.length() != 0) {
            var1.putString("recoveryview_code", var2);
         }

         Bundle var3 = this.currentParams;
         if (var3 != null) {
            var1.putBundle("recoveryview_params", var3);
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.codeField.setText("");
            this.currentParams = var1;
            this.email_unconfirmed_pattern = this.currentParams.getString("email_unconfirmed_pattern");
            this.cancelButton.setText(LocaleController.formatString("RestoreEmailTrouble", 2131560609, this.email_unconfirmed_pattern));
            AndroidUtilities.showKeyboard(this.codeField);
            this.codeField.requestFocus();
         }
      }
   }

   public class LoginActivityRegisterView extends SlideView implements ImageUpdater.ImageUpdaterDelegate {
      private TLRPC.FileLocation avatar;
      private AnimatorSet avatarAnimation;
      private TLRPC.FileLocation avatarBig;
      private AvatarDrawable avatarDrawable;
      private ImageView avatarEditor;
      private BackupImageView avatarImage;
      private View avatarOverlay;
      private RadialProgressView avatarProgressView;
      private boolean createAfterUpload;
      private Bundle currentParams;
      private EditTextBoldCursor firstNameField;
      private ImageUpdater imageUpdater;
      private EditTextBoldCursor lastNameField;
      private boolean nextPressed = false;
      private String phoneCode;
      private String phoneHash;
      private TextView privacyView;
      private String requestPhone;
      private TextView textView;
      private TLRPC.InputFile uploadedAvatar;
      private TextView wrongNumber;

      public LoginActivityRegisterView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.imageUpdater = new ImageUpdater();
         this.imageUpdater.setSearchAvailable(false);
         this.imageUpdater.setUploadAfterSelect(false);
         ImageUpdater var3 = this.imageUpdater;
         var3.parentFragment = LoginActivity.this;
         var3.delegate = this;
         this.textView = new TextView(var2);
         this.textView.setText(LocaleController.getString("RegisterText2", 2131560551));
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         TextView var14 = this.textView;
         byte var4;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var14.setGravity(var4);
         this.textView.setTextSize(1, 14.0F);
         var14 = this.textView;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         this.addView(var14, LayoutHelper.createLinear(-2, -2, var4, 0, 0, 0, 0));
         FrameLayout var15 = new FrameLayout(var2);
         this.addView(var15, LayoutHelper.createLinear(-1, -2, 0.0F, 21.0F, 0.0F, 0.0F));
         this.avatarDrawable = new AvatarDrawable();
         this.avatarImage = new BackupImageView(var2) {
            public void invalidate() {
               if (LoginActivityRegisterView.this.avatarOverlay != null) {
                  LoginActivityRegisterView.this.avatarOverlay.invalidate();
               }

               super.invalidate();
            }

            public void invalidate(int var1, int var2, int var3, int var4) {
               if (LoginActivityRegisterView.this.avatarOverlay != null) {
                  LoginActivityRegisterView.this.avatarOverlay.invalidate();
               }

               super.invalidate(var1, var2, var3, var4);
            }
         };
         this.avatarImage.setRoundRadius(AndroidUtilities.dp(32.0F));
         this.avatarDrawable.setInfo(5, (String)null, (String)null, false);
         this.avatarImage.setImageDrawable(this.avatarDrawable);
         BackupImageView var5 = this.avatarImage;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var15.addView(var5, LayoutHelper.createFrame(64, 64.0F, var4 | 48, 0.0F, 16.0F, 0.0F, 0.0F));
         final Paint var16 = new Paint(1);
         var16.setColor(1426063360);
         this.avatarOverlay = new View(var2) {
            protected void onDraw(Canvas var1) {
               if (LoginActivityRegisterView.this.avatarImage != null && LoginActivityRegisterView.this.avatarProgressView.getVisibility() == 0) {
                  var16.setAlpha((int)(LoginActivityRegisterView.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0F * LoginActivityRegisterView.this.avatarProgressView.getAlpha()));
                  var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0F), var16);
               }

            }
         };
         View var17 = this.avatarOverlay;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var15.addView(var17, LayoutHelper.createFrame(64, 64.0F, var4 | 48, 0.0F, 16.0F, 0.0F, 0.0F));
         this.avatarOverlay.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityRegisterView$UrLK1f4_ouBTRVpCoDl3SfCgewQ(this));
         this.avatarEditor = new ImageView(var2) {
            public void invalidate() {
               super.invalidate();
               LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }

            public void invalidate(int var1, int var2, int var3, int var4) {
               super.invalidate(var1, var2, var3, var4);
               LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }
         };
         this.avatarEditor.setScaleType(ScaleType.CENTER);
         this.avatarEditor.setImageResource(2131165276);
         this.avatarEditor.setEnabled(false);
         this.avatarEditor.setClickable(false);
         this.avatarEditor.setPadding(AndroidUtilities.dp(2.0F), 0, 0, 0);
         ImageView var18 = this.avatarEditor;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var15.addView(var18, LayoutHelper.createFrame(64, 64.0F, var4 | 48, 0.0F, 16.0F, 0.0F, 0.0F));
         this.avatarProgressView = new RadialProgressView(var2) {
            public void setAlpha(float var1) {
               super.setAlpha(var1);
               LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }
         };
         this.avatarProgressView.setSize(AndroidUtilities.dp(30.0F));
         this.avatarProgressView.setProgressColor(-1);
         RadialProgressView var9 = this.avatarProgressView;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var15.addView(var9, LayoutHelper.createFrame(64, 64.0F, var4 | 48, 0.0F, 16.0F, 0.0F, 0.0F));
         this.showAvatarProgress(false, false);
         this.firstNameField = new EditTextBoldCursor(var2);
         this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.firstNameField.setCursorWidth(1.5F);
         this.firstNameField.setHint(LocaleController.getString("FirstName", 2131559494));
         this.firstNameField.setImeOptions(268435461);
         this.firstNameField.setTextSize(1, 17.0F);
         this.firstNameField.setMaxLines(1);
         this.firstNameField.setInputType(8192);
         EditTextBoldCursor var10 = this.firstNameField;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         float var6;
         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 85.0F;
         }

         float var7;
         if (LocaleController.isRTL) {
            var7 = 85.0F;
         } else {
            var7 = 0.0F;
         }

         var15.addView(var10, LayoutHelper.createFrame(-1, 36.0F, var4 | 48, var6, 0.0F, var7, 0.0F));
         this.firstNameField.setOnEditorActionListener(new _$$Lambda$LoginActivity$LoginActivityRegisterView$gWZuHFySSOyrQEK_aMCkRgvER6k(this));
         this.lastNameField = new EditTextBoldCursor(var2);
         this.lastNameField.setHint(LocaleController.getString("LastName", 2131559728));
         this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.lastNameField.setCursorWidth(1.5F);
         this.lastNameField.setImeOptions(268435462);
         this.lastNameField.setTextSize(1, 17.0F);
         this.lastNameField.setMaxLines(1);
         this.lastNameField.setInputType(8192);
         var10 = this.lastNameField;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 85.0F;
         }

         if (LocaleController.isRTL) {
            var7 = 85.0F;
         } else {
            var7 = 0.0F;
         }

         var15.addView(var10, LayoutHelper.createFrame(-1, 36.0F, var4 | 48, var6, 51.0F, var7, 0.0F));
         this.lastNameField.setOnEditorActionListener(new _$$Lambda$LoginActivity$LoginActivityRegisterView$EHan9TMOJ2z2Np_xyuMTnMKxl24(this));
         this.wrongNumber = new TextView(var2);
         this.wrongNumber.setText(LocaleController.getString("CancelRegistration", 2131558900));
         TextView var11 = this.wrongNumber;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var11.setGravity(var4 | 1);
         this.wrongNumber.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
         this.wrongNumber.setTextSize(1, 14.0F);
         this.wrongNumber.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.wrongNumber.setPadding(0, AndroidUtilities.dp(24.0F), 0, 0);
         this.wrongNumber.setVisibility(8);
         var11 = this.wrongNumber;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         this.addView(var11, LayoutHelper.createLinear(-2, -2, var4 | 48, 0, 20, 0, 0));
         this.wrongNumber.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityRegisterView$9SHAtLUticbvQmKKGk0ALjL8TaM(this));
         this.privacyView = new TextView(var2);
         this.privacyView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.privacyView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
         this.privacyView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
         this.privacyView.setTextSize(1, 14.0F);
         this.privacyView.setGravity(81);
         this.privacyView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.addView(this.privacyView, LayoutHelper.createLinear(-2, -1, 81, 0, 28, 0, 16));
         String var12 = LocaleController.getString("TermsOfServiceLogin", 2131560886);
         SpannableStringBuilder var13 = new SpannableStringBuilder(var12);
         int var19 = var12.indexOf(42);
         int var8 = var12.lastIndexOf(42);
         if (var19 != -1 && var8 != -1 && var19 != var8) {
            var13.replace(var8, var8 + 1, "");
            var13.replace(var19, var19 + 1, "");
            var13.setSpan(new LoginActivity.LoginActivityRegisterView.LinkSpan(), var19, var8 - 1, 33);
         }

         this.privacyView.setText(var13);
      }

      private void showAvatarProgress(final boolean var1, boolean var2) {
         if (this.avatarEditor != null) {
            AnimatorSet var3 = this.avatarAnimation;
            if (var3 != null) {
               var3.cancel();
               this.avatarAnimation = null;
            }

            if (var2) {
               this.avatarAnimation = new AnimatorSet();
               if (var1) {
                  this.avatarProgressView.setVisibility(0);
                  this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{1.0F})});
               } else {
                  this.avatarEditor.setVisibility(0);
                  this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{0.0F})});
               }

               this.avatarAnimation.setDuration(180L);
               this.avatarAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationCancel(Animator var1x) {
                     LoginActivityRegisterView.this.avatarAnimation = null;
                  }

                  public void onAnimationEnd(Animator var1x) {
                     if (LoginActivityRegisterView.this.avatarAnimation != null && LoginActivityRegisterView.this.avatarEditor != null) {
                        if (var1) {
                           LoginActivityRegisterView.this.avatarEditor.setVisibility(4);
                        } else {
                           LoginActivityRegisterView.this.avatarProgressView.setVisibility(4);
                        }

                        LoginActivityRegisterView.this.avatarAnimation = null;
                     }

                  }
               });
               this.avatarAnimation.start();
            } else if (var1) {
               this.avatarEditor.setAlpha(1.0F);
               this.avatarEditor.setVisibility(4);
               this.avatarProgressView.setAlpha(1.0F);
               this.avatarProgressView.setVisibility(0);
            } else {
               this.avatarEditor.setAlpha(1.0F);
               this.avatarEditor.setVisibility(0);
               this.avatarProgressView.setAlpha(0.0F);
               this.avatarProgressView.setVisibility(4);
            }

         }
      }

      private void showTermsOfService(boolean var1) {
         if (LoginActivity.this.currentTermsOfService != null) {
            AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var2.setTitle(LocaleController.getString("TermsOfService", 2131560885));
            if (var1) {
               var2.setPositiveButton(LocaleController.getString("Accept", 2131558484), new _$$Lambda$LoginActivity$LoginActivityRegisterView$1zSGYizDeZnhefZSwKFasIIewOY(this));
               var2.setNegativeButton(LocaleController.getString("Decline", 2131559223), new _$$Lambda$LoginActivity$LoginActivityRegisterView$q2tVNOkdhWLHwjQp16kkC5ykcIE(this));
            } else {
               var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            }

            SpannableStringBuilder var3 = new SpannableStringBuilder(LoginActivity.this.currentTermsOfService.text);
            MessageObject.addEntitiesToText(var3, LoginActivity.this.currentTermsOfService.entities, false, 0, false, false, false);
            var2.setMessage(var3);
            LoginActivity.this.showDialog(var2.create());
         }
      }

      public void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRegisterView$29x_JtuV_r7woPg6Q9kl8QOB7qw(this, var3, var2));
      }

      public String getHeaderName() {
         return LocaleController.getString("YourName", 2131561154);
      }

      // $FF: synthetic method
      public String getInitialSearchString() {
         return ImageUpdater$ImageUpdaterDelegate$_CC.$default$getInitialSearchString(this);
      }

      // $FF: synthetic method
      public void lambda$didUploadPhoto$9$LoginActivity$LoginActivityRegisterView(TLRPC.PhotoSize var1, TLRPC.PhotoSize var2) {
         this.avatar = var1.location;
         this.avatarBig = var2.location;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForLocal(this.avatar), "50_50", (Drawable)this.avatarDrawable, (Object)null);
      }

      // $FF: synthetic method
      public void lambda$new$5$LoginActivity$LoginActivityRegisterView(View var1) {
         ImageUpdater var3 = this.imageUpdater;
         boolean var2;
         if (this.avatar != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         var3.openMenu(var2, new _$$Lambda$LoginActivity$LoginActivityRegisterView$4Yij9Lzn53ZtYUwU1guXgPxIWfc(this));
      }

      // $FF: synthetic method
      public boolean lambda$new$6$LoginActivity$LoginActivityRegisterView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.lastNameField.requestFocus();
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$new$7$LoginActivity$LoginActivityRegisterView(TextView var1, int var2, KeyEvent var3) {
         if (var2 != 6 && var2 != 5) {
            return false;
         } else {
            this.onNextPressed();
            return true;
         }
      }

      // $FF: synthetic method
      public void lambda$new$8$LoginActivity$LoginActivityRegisterView(View var1) {
         if (LoginActivity.this.doneProgressView.getTag() == null) {
            this.onBackPressed(false);
         }
      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$LoginActivityRegisterView(DialogInterface var1, int var2) {
         LoginActivity.this.currentTermsOfService.popup = false;
         this.onNextPressed();
      }

      // $FF: synthetic method
      public void lambda$null$12$LoginActivity$LoginActivityRegisterView(TLRPC.TL_error var1, TLObject var2) {
         this.nextPressed = false;
         LoginActivity.this.needHideProgress(false);
         if (var1 == null) {
            LoginActivity.this.onAuthSuccess((TLRPC.TL_auth_authorization)var2);
            if (this.avatarBig != null) {
               MessagesController.getInstance(LoginActivity.access$8400(LoginActivity.this)).uploadAndApplyUserAvatar(this.avatarBig);
            }
         } else if (var1.text.contains("PHONE_NUMBER_INVALID")) {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidPhoneNumber", 2131559674));
         } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
            if (var1.text.contains("PHONE_CODE_EXPIRED")) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("CodeExpired", 2131559120));
            } else if (var1.text.contains("FIRSTNAME_INVALID")) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidFirstName", 2131559672));
            } else if (var1.text.contains("LASTNAME_INVALID")) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidLastName", 2131559673));
            } else {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
            }
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidCode", 2131559671));
         }

      }

      // $FF: synthetic method
      public void lambda$null$2$LoginActivity$LoginActivityRegisterView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         LoginActivity.this.setPage(0, true, (Bundle)null, true);
      }

      // $FF: synthetic method
      public void lambda$null$4$LoginActivity$LoginActivityRegisterView() {
         this.avatar = null;
         this.avatarBig = null;
         this.uploadedAvatar = null;
         this.showAvatarProgress(false, true);
         this.avatarImage.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)null);
         this.avatarEditor.setImageResource(2131165276);
      }

      // $FF: synthetic method
      public void lambda$onBackPressed$10$LoginActivity$LoginActivityRegisterView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         LoginActivity.this.setPage(0, true, (Bundle)null, true);
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$13$LoginActivity$LoginActivityRegisterView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRegisterView$7BLOUX6Psn1I67qrNVHHeqDT7F0(this, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$onShow$11$LoginActivity$LoginActivityRegisterView() {
         EditTextBoldCursor var1 = this.firstNameField;
         if (var1 != null) {
            var1.requestFocus();
            var1 = this.firstNameField;
            var1.setSelection(var1.length());
         }

      }

      // $FF: synthetic method
      public void lambda$showTermsOfService$0$LoginActivity$LoginActivityRegisterView(DialogInterface var1, int var2) {
         LoginActivity.this.currentTermsOfService.popup = false;
         this.onNextPressed();
      }

      // $FF: synthetic method
      public void lambda$showTermsOfService$3$LoginActivity$LoginActivityRegisterView(DialogInterface var1, int var2) {
         AlertDialog.Builder var3 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
         var3.setTitle(LocaleController.getString("TermsOfService", 2131560885));
         var3.setMessage(LocaleController.getString("TosDecline", 2131560912));
         var3.setPositiveButton(LocaleController.getString("SignUp", 2131560785), new _$$Lambda$LoginActivity$LoginActivityRegisterView$kqjdj9pPpsBrHJ9ZRVIehID5ZAc(this));
         var3.setNegativeButton(LocaleController.getString("Decline", 2131559223), new _$$Lambda$LoginActivity$LoginActivityRegisterView$XXjD9aNfiwvZEhE6clKqUdqJ0uI(this));
         LoginActivity.this.showDialog(var3.create());
      }

      public boolean needBackButton() {
         return true;
      }

      public boolean onBackPressed(boolean var1) {
         if (!var1) {
            AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var2.setTitle(LocaleController.getString("AppName", 2131558635));
            var2.setMessage(LocaleController.getString("AreYouSureRegistration", 2131558695));
            var2.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$LoginActivity$LoginActivityRegisterView$tgsdp57qGOzyamGoBJL7DAG_UO0(this));
            var2.setPositiveButton(LocaleController.getString("Continue", 2131559153), (OnClickListener)null);
            LoginActivity.this.showDialog(var2.create());
            return false;
         } else {
            LoginActivity.this.needHideProgress(true);
            this.nextPressed = false;
            this.currentParams = null;
            return true;
         }
      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onNextPressed() {
         if (!this.nextPressed) {
            if (LoginActivity.this.currentTermsOfService != null && LoginActivity.this.currentTermsOfService.popup) {
               this.showTermsOfService(true);
            } else {
               this.nextPressed = true;
               TLRPC.TL_auth_signUp var1 = new TLRPC.TL_auth_signUp();
               var1.phone_code = this.phoneCode;
               var1.phone_code_hash = this.phoneHash;
               var1.phone_number = this.requestPhone;
               var1.first_name = this.firstNameField.getText().toString();
               var1.last_name = this.lastNameField.getText().toString();
               LoginActivity.this.needShowProgress(0);
               ConnectionsManager.getInstance(LoginActivity.access$8300(LoginActivity.this)).sendRequest(var1, new _$$Lambda$LoginActivity$LoginActivityRegisterView$f0ZfM2Vj75AOg7vhpvjVYpg5d40(this), 10);
            }
         }
      }

      public void onShow() {
         super.onShow();
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRegisterView$mhiqEuaO3fJj3ukKBPnIzQ_c39Q(this), 100L);
      }

      public void restoreStateParams(Bundle var1) {
         this.currentParams = var1.getBundle("registerview_params");
         Bundle var2 = this.currentParams;
         if (var2 != null) {
            this.setParams(var2, true);
         }

         String var8;
         label42: {
            Exception var10000;
            label47: {
               boolean var10001;
               try {
                  var8 = var1.getString("terms");
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label47;
               }

               if (var8 == null) {
                  break label42;
               }

               byte[] var9;
               try {
                  var9 = Base64.decode(var8, 0);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label47;
               }

               if (var9 == null) {
                  break label42;
               }

               try {
                  SerializedData var3 = new SerializedData(var9);
                  LoginActivity.this.currentTermsOfService = TLRPC.TL_help_termsOfService.TLdeserialize(var3, var3.readInt32(false), false);
                  var3.cleanup();
                  break label42;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }

            Exception var10 = var10000;
            FileLog.e((Throwable)var10);
         }

         var8 = var1.getString("registerview_first");
         if (var8 != null) {
            this.firstNameField.setText(var8);
         }

         String var7 = var1.getString("registerview_last");
         if (var7 != null) {
            this.lastNameField.setText(var7);
         }

      }

      public void saveStateParams(Bundle var1) {
         String var2 = this.firstNameField.getText().toString();
         if (var2.length() != 0) {
            var1.putString("registerview_first", var2);
         }

         var2 = this.lastNameField.getText().toString();
         if (var2.length() != 0) {
            var1.putString("registerview_last", var2);
         }

         if (LoginActivity.this.currentTermsOfService != null) {
            SerializedData var3 = new SerializedData(LoginActivity.this.currentTermsOfService.getObjectSize());
            LoginActivity.this.currentTermsOfService.serializeToStream(var3);
            var1.putString("terms", Base64.encodeToString(var3.toByteArray(), 0));
            var3.cleanup();
         }

         Bundle var4 = this.currentParams;
         if (var4 != null) {
            var1.putBundle("registerview_params", var4);
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.firstNameField.setText("");
            this.lastNameField.setText("");
            this.requestPhone = var1.getString("phoneFormated");
            this.phoneHash = var1.getString("phoneHash");
            this.phoneCode = var1.getString("code");
            this.currentParams = var1;
         }
      }

      public class LinkSpan extends ClickableSpan {
         public void onClick(View var1) {
            LoginActivityRegisterView.this.showTermsOfService(false);
         }

         public void updateDrawState(TextPaint var1) {
            super.updateDrawState(var1);
            var1.setUnderlineText(false);
         }
      }
   }

   public class LoginActivityResetWaitView extends SlideView {
      private TextView confirmTextView;
      private Bundle currentParams;
      private String phoneCode;
      private String phoneHash;
      private String requestPhone;
      private TextView resetAccountButton;
      private TextView resetAccountText;
      private TextView resetAccountTime;
      private int startTime;
      private Runnable timeRunnable;
      private int waitTime;

      public LoginActivityResetWaitView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.confirmTextView = new TextView(var2);
         this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.confirmTextView.setTextSize(1, 14.0F);
         TextView var6 = this.confirmTextView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5);
         this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var6 = this.confirmTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5));
         this.resetAccountText = new TextView(var2);
         var6 = this.resetAccountText;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.resetAccountText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.resetAccountText.setText(LocaleController.getString("ResetAccountStatus", 2131560588));
         this.resetAccountText.setTextSize(1, 14.0F);
         this.resetAccountText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var6 = this.resetAccountText;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 48, 0, 24, 0, 0));
         this.resetAccountTime = new TextView(var2);
         var6 = this.resetAccountTime;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.resetAccountTime.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.resetAccountTime.setTextSize(1, 14.0F);
         this.resetAccountTime.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var6 = this.resetAccountTime;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 48, 0, 2, 0, 0));
         this.resetAccountButton = new TextView(var2);
         var6 = this.resetAccountButton;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var6.setGravity(var5 | 48);
         this.resetAccountButton.setText(LocaleController.getString("ResetAccountButton", 2131560585));
         this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.resetAccountButton.setTextSize(1, 14.0F);
         this.resetAccountButton.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         this.resetAccountButton.setPadding(0, AndroidUtilities.dp(14.0F), 0, 0);
         var6 = this.resetAccountButton;
         if (LocaleController.isRTL) {
            var5 = var4;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5 | 48, 0, 7, 0, 0));
         this.resetAccountButton.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivityResetWaitView$8W_HF39tcDgJINwVsj88SoCtEEI(this));
      }

      private void updateTimeText() {
         int var1 = Math.max(0, this.waitTime - (ConnectionsManager.getInstance(LoginActivity.access$7000(LoginActivity.this)).getCurrentTime() - this.startTime));
         int var2 = var1 / 86400;
         int var3 = var1 - 86400 * var2;
         int var4 = var3 / 3600;
         var3 = (var3 - var4 * 3600) / 60;
         TextView var5;
         StringBuilder var6;
         if (var2 != 0) {
            var5 = this.resetAccountTime;
            var6 = new StringBuilder();
            var6.append(LocaleController.formatPluralString("DaysBold", var2));
            var6.append(" ");
            var6.append(LocaleController.formatPluralString("HoursBold", var4));
            var6.append(" ");
            var6.append(LocaleController.formatPluralString("MinutesBold", var3));
            var5.setText(AndroidUtilities.replaceTags(var6.toString()));
         } else {
            var5 = this.resetAccountTime;
            var6 = new StringBuilder();
            var6.append(LocaleController.formatPluralString("HoursBold", var4));
            var6.append(" ");
            var6.append(LocaleController.formatPluralString("MinutesBold", var3));
            var6.append(" ");
            var6.append(LocaleController.formatPluralString("SecondsBold", var1 % 60));
            var5.setText(AndroidUtilities.replaceTags(var6.toString()));
         }

         if (var1 > 0) {
            this.resetAccountButton.setTag("windowBackgroundWhiteGrayText6");
            this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         } else {
            this.resetAccountButton.setTag("windowBackgroundWhiteRedText6");
            this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteRedText6"));
         }

      }

      public String getHeaderName() {
         return LocaleController.getString("ResetAccount", 2131560584);
      }

      // $FF: synthetic method
      public void lambda$new$3$LoginActivity$LoginActivityResetWaitView(View var1) {
         if (LoginActivity.this.doneProgressView.getTag() == null) {
            if (Math.abs(ConnectionsManager.getInstance(LoginActivity.access$7300(LoginActivity.this)).getCurrentTime() - this.startTime) >= this.waitTime) {
               AlertDialog.Builder var2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
               var2.setMessage(LocaleController.getString("ResetMyAccountWarningText", 2131560601));
               var2.setTitle(LocaleController.getString("ResetMyAccountWarning", 2131560599));
               var2.setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", 2131560600), new _$$Lambda$LoginActivity$LoginActivityResetWaitView$06c4N1_a79zd80tl7oqx4H1YDGI(this));
               var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               LoginActivity.this.showDialog(var2.create());
            }
         }
      }

      // $FF: synthetic method
      public void lambda$null$0$LoginActivity$LoginActivityResetWaitView(TLRPC.TL_error var1) {
         LoginActivity.this.needHideProgress(false);
         if (var1 == null) {
            Bundle var2 = new Bundle();
            var2.putString("phoneFormated", this.requestPhone);
            var2.putString("phoneHash", this.phoneHash);
            var2.putString("code", this.phoneCode);
            LoginActivity.this.setPage(5, true, var2, false);
         } else if (var1.text.equals("2FA_RECENT_CONFIRM")) {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("ResetAccountCancelledAlert", 2131560586));
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$LoginActivityResetWaitView(TLObject var1, TLRPC.TL_error var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityResetWaitView$ehRWxs7c1MtBNOs2EfgHKXhZShY(this, var2));
      }

      // $FF: synthetic method
      public void lambda$null$2$LoginActivity$LoginActivityResetWaitView(DialogInterface var1, int var2) {
         LoginActivity.this.needShowProgress(0);
         TLRPC.TL_account_deleteAccount var3 = new TLRPC.TL_account_deleteAccount();
         var3.reason = "Forgot password";
         ConnectionsManager.getInstance(LoginActivity.access$7400(LoginActivity.this)).sendRequest(var3, new _$$Lambda$LoginActivity$LoginActivityResetWaitView$VRRsiuZ_SrcrIpAgJJI_2VPK_uo(this), 10);
      }

      public boolean needBackButton() {
         return true;
      }

      public boolean onBackPressed(boolean var1) {
         LoginActivity.this.needHideProgress(true);
         AndroidUtilities.cancelRunOnUIThread(this.timeRunnable);
         this.timeRunnable = null;
         this.currentParams = null;
         return true;
      }

      public void restoreStateParams(Bundle var1) {
         this.currentParams = var1.getBundle("resetview_params");
         var1 = this.currentParams;
         if (var1 != null) {
            this.setParams(var1, true);
         }

      }

      public void saveStateParams(Bundle var1) {
         Bundle var2 = this.currentParams;
         if (var2 != null) {
            var1.putBundle("resetview_params", var2);
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.currentParams = var1;
            this.requestPhone = var1.getString("phoneFormated");
            this.phoneHash = var1.getString("phoneHash");
            this.phoneCode = var1.getString("code");
            this.startTime = var1.getInt("startTime");
            this.waitTime = var1.getInt("waitTime");
            TextView var3 = this.confirmTextView;
            PhoneFormat var4 = PhoneFormat.getInstance();
            StringBuilder var5 = new StringBuilder();
            var5.append("+");
            var5.append(this.requestPhone);
            var3.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ResetAccountInfo", 2131560587, LocaleController.addNbsp(var4.format(var5.toString())))));
            this.updateTimeText();
            this.timeRunnable = new Runnable() {
               public void run() {
                  if (LoginActivityResetWaitView.this.timeRunnable == this) {
                     LoginActivityResetWaitView.this.updateTimeText();
                     AndroidUtilities.runOnUIThread(LoginActivityResetWaitView.this.timeRunnable, 1000L);
                  }
               }
            };
            AndroidUtilities.runOnUIThread(this.timeRunnable, 1000L);
         }
      }
   }

   public class LoginActivitySmsView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
      private ImageView blackImageView;
      private ImageView blueImageView;
      private String catchedPhone;
      private EditTextBoldCursor[] codeField;
      private LinearLayout codeFieldContainer;
      private int codeTime = 15000;
      private Timer codeTimer;
      private TextView confirmTextView;
      private Bundle currentParams;
      private int currentType;
      private String emailPhone;
      private boolean ignoreOnTextChange;
      private boolean isRestored;
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
      private LoginActivity.ProgressView progressView;
      private String requestPhone;
      private int time = 60000;
      private TextView timeText;
      private Timer timeTimer;
      private int timeout;
      private final Object timerSync = new Object();
      private TextView titleTextView;
      private boolean waitingForEvent;

      public LoginActivitySmsView(Context var2, int var3) {
         super(var2);
         this.currentType = var3;
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
         if (this.currentType == 3) {
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
            if (var5) {
               var10.addView(var7, LayoutHelper.createFrame(64, 76.0F, 19, 2.0F, 2.0F, 0.0F, 0.0F));
               TextView var11 = this.confirmTextView;
               if (LocaleController.isRTL) {
                  var9 = 5;
               } else {
                  var9 = 3;
               }

               var10.addView(var11, LayoutHelper.createFrame(-1, -2.0F, var9, 82.0F, 0.0F, 0.0F, 0.0F));
            } else {
               TextView var8 = this.confirmTextView;
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
            if (this.currentType == 1) {
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
         if (this.currentType == 3) {
            this.codeFieldContainer.setVisibility(8);
         }

         this.timeText = new TextView(var2) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0F), Integer.MIN_VALUE));
            }
         };
         this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         if (this.currentType == 3) {
            this.timeText.setTextSize(1, 14.0F);
            var4 = this.timeText;
            if (LocaleController.isRTL) {
               var9 = 5;
            } else {
               var9 = 3;
            }

            this.addView(var4, LayoutHelper.createLinear(-2, -2, var9));
            this.progressView = LoginActivity.this.new ProgressView(var2);
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
         if (this.currentType == 1) {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
         } else {
            this.problemText.setText(LocaleController.getString("DidNotGetTheCode", 2131559266));
         }

         this.addView(this.problemText, LayoutHelper.createLinear(-2, -2, 49));
         this.problemText.setOnClickListener(new _$$Lambda$LoginActivity$LoginActivitySmsView$WDlczSWG_Xmyhu8bi5YJvMI6JBs(this));
      }

      private void createCodeTimer() {
         if (this.codeTimer == null) {
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$run$0$LoginActivity$LoginActivitySmsView$4() {
                  double var1 = (double)System.currentTimeMillis();
                  double var3 = LoginActivitySmsView.this.lastCodeTime;
                  Double.isNaN(var1);
                  LoginActivitySmsView.this.lastCodeTime = var1;
                  LoginActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
                  double var6 = (double)var5.codeTime;
                  Double.isNaN(var6);
                  var5.codeTime = (int)(var6 - (var1 - var3));
                  if (LoginActivitySmsView.this.codeTime <= 1000) {
                     LoginActivitySmsView.this.problemText.setVisibility(0);
                     LoginActivitySmsView.this.timeText.setVisibility(8);
                     LoginActivitySmsView.this.destroyCodeTimer();
                  }

               }

               public void run() {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$4$Fr5toso_Gx7wT_8YsmrVs3ysx4Y(this));
               }
            }, 0L, 1000L);
         }
      }

      private void createTimer() {
         if (this.timeTimer == null) {
            this.timeTimer = new Timer();
            this.timeTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$null$0$LoginActivity$LoginActivitySmsView$5(TLRPC.TL_error var1) {
                  LoginActivitySmsView.this.lastError = var1.text;
               }

               // $FF: synthetic method
               public void lambda$null$1$LoginActivity$LoginActivitySmsView$5(TLObject var1, TLRPC.TL_error var2) {
                  if (var2 != null && var2.text != null) {
                     AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$5$ch_quU2kHnrVJ_iz4zDaJIXUckM(this, var2));
                  }

               }

               // $FF: synthetic method
               public void lambda$run$2$LoginActivity$LoginActivitySmsView$5() {
                  double var1 = (double)System.currentTimeMillis();
                  double var3 = LoginActivitySmsView.this.lastCurrentTime;
                  Double.isNaN(var1);
                  LoginActivitySmsView.this.lastCurrentTime = var1;
                  LoginActivity.LoginActivitySmsView var5 = LoginActivitySmsView.this;
                  double var6 = (double)var5.time;
                  Double.isNaN(var6);
                  var5.time = (int)(var6 - (var1 - var3));
                  if (LoginActivitySmsView.this.time >= 1000) {
                     int var8 = LoginActivitySmsView.this.time / 1000 / 60;
                     int var9 = LoginActivitySmsView.this.time / 1000 - var8 * 60;
                     if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 3) {
                        if (LoginActivitySmsView.this.nextType == 2) {
                           LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, var8, var9));
                        }
                     } else {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("CallText", 2131558885, var8, var9));
                     }

                     if (LoginActivitySmsView.this.progressView != null) {
                        LoginActivitySmsView.this.progressView.setProgress(1.0F - (float)LoginActivitySmsView.this.time / (float)LoginActivitySmsView.this.timeout);
                     }
                  } else {
                     if (LoginActivitySmsView.this.progressView != null) {
                        LoginActivitySmsView.this.progressView.setProgress(1.0F);
                     }

                     LoginActivitySmsView.this.destroyTimer();
                     if (LoginActivitySmsView.this.currentType == 3) {
                        AndroidUtilities.setWaitingForCall(false);
                        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                        LoginActivitySmsView.this.waitingForEvent = false;
                        LoginActivitySmsView.this.destroyCodeTimer();
                        LoginActivitySmsView.this.resendCode();
                     } else if (LoginActivitySmsView.this.currentType == 2 || LoginActivitySmsView.this.currentType == 4) {
                        if (LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 2) {
                           if (LoginActivitySmsView.this.nextType == 3) {
                              AndroidUtilities.setWaitingForSms(false);
                              NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                              LoginActivitySmsView.this.waitingForEvent = false;
                              LoginActivitySmsView.this.destroyCodeTimer();
                              LoginActivitySmsView.this.resendCode();
                           }
                        } else {
                           if (LoginActivitySmsView.this.nextType == 4) {
                              LoginActivitySmsView.this.timeText.setText(LocaleController.getString("Calling", 2131558887));
                           } else {
                              LoginActivitySmsView.this.timeText.setText(LocaleController.getString("SendingSms", 2131560714));
                           }

                           LoginActivitySmsView.this.createCodeTimer();
                           TLRPC.TL_auth_resendCode var10 = new TLRPC.TL_auth_resendCode();
                           var10.phone_number = LoginActivitySmsView.this.requestPhone;
                           var10.phone_code_hash = LoginActivitySmsView.this.phoneHash;
                           ConnectionsManager.getInstance(LoginActivity.access$5900(LoginActivity.this)).sendRequest(var10, new _$$Lambda$LoginActivity$LoginActivitySmsView$5$Lug0N7IyxdRqJvwOmLwPEiL6cA0(this), 10);
                        }
                     }
                  }

               }

               public void run() {
                  if (LoginActivitySmsView.this.timeTimer != null) {
                     AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$5$yhZ0JlUwl5Kkxw_R7O3sYzK3tvc(this));
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
      static void lambda$onBackPressed$10(TLObject var0, TLRPC.TL_error var1) {
      }

      private void resendCode() {
         Bundle var1 = new Bundle();
         var1.putString("phone", this.phone);
         var1.putString("ephone", this.emailPhone);
         var1.putString("phoneFormated", this.requestPhone);
         this.nextPressed = true;
         TLRPC.TL_auth_resendCode var2 = new TLRPC.TL_auth_resendCode();
         var2.phone_number = this.requestPhone;
         var2.phone_code_hash = this.phoneHash;
         ConnectionsManager.getInstance(LoginActivity.access$3600(LoginActivity.this)).sendRequest(var2, new _$$Lambda$LoginActivity$LoginActivitySmsView$7Coa87SRXNZ2UoSLGVeBv7VQ3Fs(this, var1), 10);
         LoginActivity.this.needShowProgress(0);
      }

      public void didReceivedNotification(int var1, int var2, Object... var3) {
         if (this.waitingForEvent) {
            EditTextBoldCursor[] var4 = this.codeField;
            if (var4 != null) {
               StringBuilder var7;
               if (var1 == NotificationCenter.didReceiveSmsCode) {
                  EditTextBoldCursor var5 = var4[0];
                  var7 = new StringBuilder();
                  var7.append("");
                  var7.append(var3[0]);
                  var5.setText(var7.toString());
                  this.onNextPressed();
               } else if (var1 == NotificationCenter.didReceiveCall) {
                  var7 = new StringBuilder();
                  var7.append("");
                  var7.append(var3[0]);
                  String var6 = var7.toString();
                  if (!AndroidUtilities.checkPhonePattern(this.pattern, var6)) {
                     return;
                  }

                  if (!this.pattern.equals("*")) {
                     this.catchedPhone = var6;
                     AndroidUtilities.endIncomingCall();
                  }

                  this.ignoreOnTextChange = true;
                  this.codeField[0].setText(var6);
                  this.ignoreOnTextChange = false;
                  this.onNextPressed();
               }
            }
         }

      }

      public String getHeaderName() {
         return this.currentType == 1 ? this.phone : LocaleController.getString("YourCode", 2131561142);
      }

      // $FF: synthetic method
      public void lambda$new$0$LoginActivity$LoginActivitySmsView(View var1) {
         if (!this.nextPressed) {
            boolean var2;
            if ((this.nextType != 4 || this.currentType != 2) && this.nextType != 0) {
               var2 = false;
            } else {
               var2 = true;
            }

            if (!var2) {
               if (LoginActivity.this.doneProgressView.getTag() != null) {
                  return;
               }

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
                  var4.append(this.emailPhone);
                  var3.putExtra("android.intent.extra.SUBJECT", var4.toString());
                  var4 = new StringBuilder();
                  var4.append("Phone: ");
                  var4.append(this.requestPhone);
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
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("NoMailInstalled", 2131559927));
               }
            }

         }
      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$LoginActivitySmsView(TLRPC.TL_error var1, Bundle var2, TLObject var3) {
         this.nextPressed = false;
         if (var1 == null) {
            LoginActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else {
            String var5 = var1.text;
            if (var5 != null) {
               if (var5.contains("PHONE_NUMBER_INVALID")) {
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidPhoneNumber", 2131559674));
               } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
                  if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                     this.onBackPressed(true);
                     LoginActivity.this.setPage(0, true, (Bundle)null, true);
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("CodeExpired", 2131559120));
                  } else if (var1.text.startsWith("FLOOD_WAIT")) {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("FloodWait", 2131559495));
                  } else if (var1.code != -1000) {
                     LoginActivity var6 = LoginActivity.this;
                     String var7 = LocaleController.getString("AppName", 2131558635);
                     StringBuilder var4 = new StringBuilder();
                     var4.append(LocaleController.getString("ErrorOccurred", 2131559375));
                     var4.append("\n");
                     var4.append(var1.text);
                     var6.needShowAlert(var7, var4.toString());
                  }
               } else {
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidCode", 2131559671));
               }
            }
         }

         LoginActivity.this.needHideProgress(false);
      }

      // $FF: synthetic method
      public void lambda$null$5$LoginActivity$LoginActivitySmsView(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_auth_signIn var3) {
         LoginActivity.this.needHideProgress(false);
         if (var1 == null) {
            TLRPC.TL_account_password var4 = (TLRPC.TL_account_password)var2;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(var4, true)) {
               AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
               return;
            }

            Bundle var8 = new Bundle();
            TLRPC.PasswordKdfAlgo var5 = var4.current_algo;
            if (var5 instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
               TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var6 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)var5;
               var8.putString("current_salt1", Utilities.bytesToHex(var6.salt1));
               var8.putString("current_salt2", Utilities.bytesToHex(var6.salt2));
               var8.putString("current_p", Utilities.bytesToHex(var6.p));
               var8.putInt("current_g", var6.g);
               var8.putString("current_srp_B", Utilities.bytesToHex(var4.srp_B));
               var8.putLong("current_srp_id", var4.srp_id);
               var8.putInt("passwordType", 1);
            }

            String var7 = var4.hint;
            if (var7 == null) {
               var7 = "";
            }

            var8.putString("hint", var7);
            var7 = var4.email_unconfirmed_pattern;
            if (var7 == null) {
               var7 = "";
            }

            var8.putString("email_unconfirmed_pattern", var7);
            var8.putString("phoneFormated", this.requestPhone);
            var8.putString("phoneHash", this.phoneHash);
            var8.putString("code", var3.phone_code);
            var8.putInt("has_recovery", var4.has_recovery);
            LoginActivity.this.setPage(6, true, var8, false);
         } else {
            LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
         }

      }

      // $FF: synthetic method
      public void lambda$null$6$LoginActivity$LoginActivitySmsView(TLRPC.TL_auth_signIn var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$u6PnjqH_X1fZ6mUkFF5h0b6RQx0(this, var3, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$null$7$LoginActivity$LoginActivitySmsView(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_auth_signIn var3) {
         this.nextPressed = false;
         boolean var4 = true;
         if (var1 == null) {
            LoginActivity.this.needHideProgress(false);
            this.destroyTimer();
            this.destroyCodeTimer();
            LoginActivity.this.onAuthSuccess((TLRPC.TL_auth_authorization)var2);
         } else {
            String var9 = var1.text;
            this.lastError = var9;
            if (var9.contains("PHONE_NUMBER_UNOCCUPIED")) {
               LoginActivity.this.needHideProgress(false);
               Bundle var6 = new Bundle();
               var6.putString("phoneFormated", this.requestPhone);
               var6.putString("phoneHash", this.phoneHash);
               var6.putString("code", var3.phone_code);
               LoginActivity.this.setPage(5, true, var6, false);
               this.destroyTimer();
               this.destroyCodeTimer();
            } else if (var1.text.contains("SESSION_PASSWORD_NEEDED")) {
               TLRPC.TL_account_getPassword var7 = new TLRPC.TL_account_getPassword();
               ConnectionsManager.getInstance(LoginActivity.access$6400(LoginActivity.this)).sendRequest(var7, new _$$Lambda$LoginActivity$LoginActivitySmsView$ClZInkb3pcX3d_IEp0qMTOZ0dwc(this, var3), 10);
               this.destroyTimer();
               this.destroyCodeTimer();
            } else {
               int var12;
               label82: {
                  label90: {
                     LoginActivity.this.needHideProgress(false);
                     if (this.currentType == 3) {
                        var12 = this.nextType;
                        if (var12 == 4 || var12 == 2) {
                           break label90;
                        }
                     }

                     if (this.currentType == 2) {
                        var12 = this.nextType;
                        if (var12 == 4 || var12 == 3) {
                           break label90;
                        }
                     }

                     if (this.currentType != 4 || this.nextType != 2) {
                        break label82;
                     }
                  }

                  this.createTimer();
               }

               var12 = this.currentType;
               if (var12 == 2) {
                  AndroidUtilities.setWaitingForSms(true);
                  NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
               } else if (var12 == 3) {
                  AndroidUtilities.setWaitingForCall(true);
                  NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
               }

               this.waitingForEvent = true;
               if (this.currentType != 3) {
                  if (var1.text.contains("PHONE_NUMBER_INVALID")) {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidPhoneNumber", 2131559674));
                  } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
                     if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                        this.onBackPressed(true);
                        LoginActivity.this.setPage(0, true, (Bundle)null, true);
                        LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("CodeExpired", 2131559120));
                     } else if (var1.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("FloodWait", 2131559495));
                     } else {
                        LoginActivity var5 = LoginActivity.this;
                        String var11 = LocaleController.getString("AppName", 2131558635);
                        StringBuilder var10 = new StringBuilder();
                        var10.append(LocaleController.getString("ErrorOccurred", 2131559375));
                        var10.append("\n");
                        var10.append(var1.text);
                        var5.needShowAlert(var11, var10.toString());
                     }
                  } else {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidCode", 2131559671));
                     var12 = 0;

                     while(true) {
                        EditTextBoldCursor[] var8 = this.codeField;
                        if (var12 >= var8.length) {
                           var8[0].requestFocus();
                           break;
                        }

                        var8[var12].setText("");
                        ++var12;
                     }
                  }
               }

               var4 = false;
            }
         }

         if (var4 && this.currentType == 3) {
            AndroidUtilities.endIncomingCall();
         }

      }

      // $FF: synthetic method
      public void lambda$onBackPressed$9$LoginActivity$LoginActivitySmsView(DialogInterface var1, int var2) {
         this.onBackPressed(true);
         LoginActivity.this.setPage(0, true, (Bundle)null, true);
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$8$LoginActivity$LoginActivitySmsView(TLRPC.TL_auth_signIn var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$KDcbJgHhkQhO0H1lUrS7lHNHtoc(this, var3, var2, var1));
      }

      // $FF: synthetic method
      public void lambda$onShow$11$LoginActivity$LoginActivitySmsView() {
         EditTextBoldCursor[] var1 = this.codeField;
         if (var1 != null) {
            for(int var2 = var1.length - 1; var2 >= 0; --var2) {
               if (var2 == 0 || this.codeField[var2].length() != 0) {
                  this.codeField[var2].requestFocus();
                  var1 = this.codeField;
                  var1[var2].setSelection(var1[var2].length());
                  AndroidUtilities.showKeyboard(this.codeField[var2]);
                  break;
               }
            }
         }

      }

      // $FF: synthetic method
      public void lambda$resendCode$2$LoginActivity$LoginActivitySmsView(Bundle var1, TLObject var2, TLRPC.TL_error var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$PO7r5AWMTSHLIApv2_6O4ar48qM(this, var3, var1, var2));
      }

      // $FF: synthetic method
      public boolean lambda$setParams$3$LoginActivity$LoginActivitySmsView(int var1, View var2, int var3, KeyEvent var4) {
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
      public boolean lambda$setParams$4$LoginActivity$LoginActivitySmsView(TextView var1, int var2, KeyEvent var3) {
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
            AlertDialog.Builder var4 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            var4.setTitle(LocaleController.getString("AppName", 2131558635));
            var4.setMessage(LocaleController.getString("StopVerification", 2131560831));
            var4.setPositiveButton(LocaleController.getString("Continue", 2131559153), (OnClickListener)null);
            var4.setNegativeButton(LocaleController.getString("Stop", 2131560820), new _$$Lambda$LoginActivity$LoginActivitySmsView$hHDcDlk50TjqyjTury7_j6_VypU(this));
            LoginActivity.this.showDialog(var4.create());
            return false;
         } else {
            this.nextPressed = false;
            LoginActivity.this.needHideProgress(true);
            TLRPC.TL_auth_cancelCode var2 = new TLRPC.TL_auth_cancelCode();
            var2.phone_number = this.requestPhone;
            var2.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(LoginActivity.access$6200(LoginActivity.this)).sendRequest(var2, _$$Lambda$LoginActivity$LoginActivitySmsView$m2HPGKwCNffO48k34_Pfyo6t35w.INSTANCE, 10);
            this.destroyTimer();
            this.destroyCodeTimer();
            this.currentParams = null;
            int var3 = this.currentType;
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
         int var1 = this.currentType;
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
         if (this.currentType != 3 && this.blueImageView != null) {
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
         if (this.currentType != 3) {
            ImageView var3 = this.blueImageView;
            if (var3 != null) {
               var2 = var3.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0F);
               int var4 = AndroidUtilities.dp(80.0F);
               var1 = AndroidUtilities.dp(291.0F);
               if (LoginActivity.this.scrollHeight - var2 < var4) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var2 + var4);
               } else if (LoginActivity.this.scrollHeight > var1) {
                  this.setMeasuredDimension(this.getMeasuredWidth(), var1);
               } else {
                  this.setMeasuredDimension(this.getMeasuredWidth(), LoginActivity.this.scrollHeight);
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
               int var2 = this.currentType;
               if (var2 == 2) {
                  AndroidUtilities.setWaitingForSms(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
               } else if (var2 == 3) {
                  AndroidUtilities.setWaitingForCall(false);
                  NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
               }

               this.waitingForEvent = false;
               TLRPC.TL_auth_signIn var3 = new TLRPC.TL_auth_signIn();
               var3.phone_number = this.requestPhone;
               var3.phone_code = var1;
               var3.phone_code_hash = this.phoneHash;
               this.destroyTimer();
               var2 = ConnectionsManager.getInstance(LoginActivity.access$6100(LoginActivity.this)).sendRequest(var3, new _$$Lambda$LoginActivity$LoginActivitySmsView$QuZqrlgFwDiyZUcg7gbDXVsiiUk(this, var3), 10);
               LoginActivity.this.needShowProgress(var2);
            }
         }
      }

      public void onShow() {
         super.onShow();
         if (this.currentType != 3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$Bj_lU3gGH1xepmEk_N_1k9TPJGE(this), 100L);
         }
      }

      public void restoreStateParams(Bundle var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("smsview_params_");
         var2.append(this.currentType);
         this.currentParams = var1.getBundle(var2.toString());
         Bundle var5 = this.currentParams;
         if (var5 != null) {
            this.setParams(var5, true);
         }

         String var6 = var1.getString("catchedPhone");
         if (var6 != null) {
            this.catchedPhone = var6;
         }

         var2 = new StringBuilder();
         var2.append("smsview_code_");
         var2.append(this.currentType);
         var6 = var1.getString(var2.toString());
         if (var6 != null) {
            EditTextBoldCursor[] var3 = this.codeField;
            if (var3 != null) {
               var3[0].setText(var6);
            }
         }

         int var4 = var1.getInt("time");
         if (var4 != 0) {
            this.time = var4;
         }

         var4 = var1.getInt("open");
         if (var4 != 0) {
            this.openTime = var4;
         }

      }

      public void saveStateParams(Bundle var1) {
         String var2 = this.getCode();
         StringBuilder var3;
         if (var2.length() != 0) {
            var3 = new StringBuilder();
            var3.append("smsview_code_");
            var3.append(this.currentType);
            var1.putString(var3.toString(), var2);
         }

         String var5 = this.catchedPhone;
         if (var5 != null) {
            var1.putString("catchedPhone", var5);
         }

         if (this.currentParams != null) {
            var3 = new StringBuilder();
            var3.append("smsview_params_");
            var3.append(this.currentType);
            var1.putBundle(var3.toString(), this.currentParams);
         }

         int var4 = this.time;
         if (var4 != 0) {
            var1.putInt("time", var4);
         }

         var4 = this.openTime;
         if (var4 != 0) {
            var1.putInt("open", var4);
         }

      }

      public void setParams(Bundle var1, boolean var2) {
         if (var1 != null) {
            this.isRestored = var2;
            this.waitingForEvent = true;
            int var3 = this.currentType;
            if (var3 == 2) {
               AndroidUtilities.setWaitingForSms(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (var3 == 3) {
               AndroidUtilities.setWaitingForCall(true);
               NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }

            this.currentParams = var1;
            this.phone = var1.getString("phone");
            this.emailPhone = var1.getString("ephone");
            this.requestPhone = var1.getString("phoneFormated");
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
            Object var10 = "";
            byte var12 = 8;
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
                  Drawable var13 = this.getResources().getDrawable(2131165811).mutate();
                  var13.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Mode.MULTIPLY));
                  this.codeField[var5].setBackgroundDrawable(var13);
                  this.codeField[var5].setImeOptions(268435461);
                  this.codeField[var5].setTextSize(1, 20.0F);
                  this.codeField[var5].setMaxLines(1);
                  this.codeField[var5].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                  this.codeField[var5].setPadding(0, 0, 0, 0);
                  this.codeField[var5].setGravity(49);
                  if (this.currentType == 3) {
                     this.codeField[var5].setEnabled(false);
                     this.codeField[var5].setInputType(0);
                     this.codeField[var5].setVisibility(8);
                  } else {
                     this.codeField[var5].setInputType(3);
                  }

                  LinearLayout var14 = this.codeFieldContainer;
                  EditTextBoldCursor var6 = this.codeField[var5];
                  byte var7;
                  if (var5 != this.length - 1) {
                     var7 = 7;
                  } else {
                     var7 = 0;
                  }

                  var14.addView(var6, LayoutHelper.createLinear(34, 36, 1, 0, 0, var7, 0));
                  this.codeField[var5].addTextChangedListener(new TextWatcher() {
                     public void afterTextChanged(Editable var1) {
                        if (!LoginActivitySmsView.this.ignoreOnTextChange) {
                           int var2 = var1.length();
                           if (var2 >= 1) {
                              if (var2 > 1) {
                                 String var3 = var1.toString();
                                 LoginActivitySmsView.this.ignoreOnTextChange = true;

                                 for(int var4 = 0; var4 < Math.min(LoginActivitySmsView.this.length - var5, var2); ++var4) {
                                    if (var4 == 0) {
                                       var1.replace(0, var2, var3.substring(var4, var4 + 1));
                                    } else {
                                       LoginActivitySmsView.this.codeField[var5 + var4].setText(var3.substring(var4, var4 + 1));
                                    }
                                 }

                                 LoginActivitySmsView.this.ignoreOnTextChange = false;
                              }

                              if (var5 != LoginActivitySmsView.this.length - 1) {
                                 LoginActivitySmsView.this.codeField[var5 + 1].setSelection(LoginActivitySmsView.this.codeField[var5 + 1].length());
                                 LoginActivitySmsView.this.codeField[var5 + 1].requestFocus();
                              }

                              if ((var5 == LoginActivitySmsView.this.length - 1 || var5 == LoginActivitySmsView.this.length - 2 && var2 >= 2) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                 LoginActivitySmsView.this.onNextPressed();
                              }
                           }

                        }
                     }

                     public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }

                     public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                     }
                  });
                  this.codeField[var5].setOnKeyListener(new _$$Lambda$LoginActivity$LoginActivitySmsView$W4f4bbr6ANrn17O_fi8CZ_C8uvk(this, var5));
                  this.codeField[var5].setOnEditorActionListener(new _$$Lambda$LoginActivity$LoginActivitySmsView$f1ikQgn9Rce7rxNwD6eW9NGEWc4(this));
               }
            }

            LoginActivity.ProgressView var16 = this.progressView;
            byte var18;
            if (var16 != null) {
               if (this.nextType != 0) {
                  var18 = 0;
               } else {
                  var18 = 8;
               }

               var16.setVisibility(var18);
            }

            if (this.phone != null) {
               String var17 = PhoneFormat.getInstance().format(this.phone);
               var5 = this.currentType;
               if (var5 == 1) {
                  var10 = AndroidUtilities.replaceTags(LocaleController.getString("SentAppCode", 2131560717));
               } else if (var5 == 2) {
                  var10 = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131560721, LocaleController.addNbsp(var17)));
               } else if (var5 == 3) {
                  var10 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131560719, LocaleController.addNbsp(var17)));
               } else if (var5 == 4) {
                  var10 = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131560720, LocaleController.addNbsp(var17)));
               }

               this.confirmTextView.setText((CharSequence)var10);
               if (this.currentType != 3) {
                  AndroidUtilities.showKeyboard(this.codeField[0]);
                  this.codeField[0].requestFocus();
               } else {
                  AndroidUtilities.hideKeyboard(this.codeField[0]);
               }

               this.destroyTimer();
               this.destroyCodeTimer();
               this.lastCurrentTime = (double)System.currentTimeMillis();
               var5 = this.currentType;
               if (var5 == 1) {
                  this.problemText.setVisibility(0);
                  this.timeText.setVisibility(8);
               } else {
                  String var15;
                  if (var5 == 3) {
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

                        var15 = this.catchedPhone;
                        if (var15 != null) {
                           this.ignoreOnTextChange = true;
                           this.codeField[0].setText(var15);
                           this.ignoreOnTextChange = false;
                           this.onNextPressed();
                        } else {
                           this.createTimer();
                        }

                        return;
                     }
                  }

                  TextView var11;
                  if (this.currentType == 2) {
                     var5 = this.nextType;
                     if (var5 == 4 || var5 == 3) {
                        this.timeText.setText(LocaleController.formatString("CallText", 2131558885, 2, 0));
                        var11 = this.problemText;
                        if (this.time < 1000) {
                           var18 = 0;
                        } else {
                           var18 = 8;
                        }

                        var11.setVisibility(var18);
                        var11 = this.timeText;
                        if (this.time >= 1000) {
                           var12 = 0;
                        }

                        var11.setVisibility(var12);
                        SharedPreferences var8 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        var4 = null;
                        String var19 = var8.getString("sms_hash", (String)null);
                        var15 = var4;
                        if (!TextUtils.isEmpty(var19)) {
                           String var9 = var8.getString("sms_hash_code", (String)null);
                           var15 = var4;
                           if (var9 != null) {
                              StringBuilder var20 = new StringBuilder();
                              var20.append(var19);
                              var20.append("|");
                              var15 = var4;
                              if (var9.contains(var20.toString())) {
                                 var15 = var9.substring(var9.indexOf(124) + 1);
                              }
                           }
                        }

                        if (var15 != null) {
                           this.codeField[0].setText(var15);
                           this.onNextPressed();
                        } else {
                           this.createTimer();
                        }

                        return;
                     }
                  }

                  if (this.currentType == 4 && this.nextType == 2) {
                     this.timeText.setText(LocaleController.formatString("SmsText", 2131560793, 2, 0));
                     var11 = this.problemText;
                     if (this.time < 1000) {
                        var18 = 0;
                     } else {
                        var18 = 8;
                     }

                     var11.setVisibility(var18);
                     var11 = this.timeText;
                     if (this.time >= 1000) {
                        var12 = 0;
                     }

                     var11.setVisibility(var12);
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
   }

   public class PhoneView extends SlideView implements OnItemSelectedListener {
      private CheckBoxCell checkBoxCell;
      private EditTextBoldCursor codeField;
      private HashMap codesMap = new HashMap();
      private ArrayList countriesArray = new ArrayList();
      private HashMap countriesMap = new HashMap();
      private TextView countryButton;
      private int countryState = 0;
      private boolean ignoreOnPhoneChange = false;
      private boolean ignoreOnTextChange = false;
      private boolean ignoreSelection = false;
      private boolean nextPressed = false;
      private HintEditText phoneField;
      private HashMap phoneFormatMap = new HashMap();
      private TextView textView;
      private TextView textView2;
      private View view;

      public PhoneView(Context var2) {
         super(var2);
         this.setOrientation(1);
         this.countryButton = new TextView(var2);
         this.countryButton.setTextSize(1, 18.0F);
         this.countryButton.setPadding(AndroidUtilities.dp(12.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(12.0F), 0);
         this.countryButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.countryButton.setMaxLines(1);
         this.countryButton.setSingleLine(true);
         this.countryButton.setEllipsize(TruncateAt.END);
         TextView var3 = this.countryButton;
         byte var4;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var3.setGravity(var4 | 1);
         this.countryButton.setBackgroundResource(2131165857);
         this.addView(this.countryButton, LayoutHelper.createLinear(-1, 36, 0.0F, 0.0F, 0.0F, 14.0F));
         this.countryButton.setOnClickListener(new _$$Lambda$LoginActivity$PhoneView$YKPRtOo2JwvFERoOBV4IqiJYOOU(this));
         this.view = new View(var2);
         this.view.setPadding(AndroidUtilities.dp(12.0F), 0, AndroidUtilities.dp(12.0F), 0);
         this.view.setBackgroundColor(Theme.getColor("windowBackgroundWhiteGrayLine"));
         this.addView(this.view, LayoutHelper.createLinear(-1, 1, 4.0F, -17.5F, 4.0F, 0.0F));
         LinearLayout var20 = new LinearLayout(var2);
         var20.setOrientation(0);
         this.addView(var20, LayoutHelper.createLinear(-1, -2, 0.0F, 20.0F, 0.0F, 0.0F));
         this.textView = new TextView(var2);
         this.textView.setText("+");
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 18.0F);
         var20.addView(this.textView, LayoutHelper.createLinear(-2, -2));
         this.codeField = new EditTextBoldCursor(var2);
         this.codeField.setInputType(3);
         this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.codeField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.codeField.setCursorWidth(1.5F);
         this.codeField.setPadding(AndroidUtilities.dp(10.0F), 0, 0, 0);
         this.codeField.setTextSize(1, 18.0F);
         this.codeField.setMaxLines(1);
         this.codeField.setGravity(19);
         this.codeField.setImeOptions(268435461);
         LengthFilter var5 = new LengthFilter(5);
         this.codeField.setFilters(new InputFilter[]{var5});
         var20.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0F, 0.0F, 16.0F, 0.0F));
         this.codeField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               if (!PhoneView.this.ignoreOnTextChange) {
                  PhoneView.this.ignoreOnTextChange = true;
                  String var8 = PhoneFormat.stripExceptNumbers(PhoneView.this.codeField.getText().toString());
                  PhoneView.this.codeField.setText(var8);
                  int var2 = var8.length();
                  Object var3 = null;
                  if (var2 == 0) {
                     PhoneView.this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
                     PhoneView.this.phoneField.setHintText((String)null);
                     PhoneView.this.countryState = 1;
                  } else {
                     int var4 = var8.length();
                     var2 = 4;
                     String var5;
                     boolean var11;
                     String var12;
                     if (var4 <= 4) {
                        var12 = var8;
                        var8 = null;
                        var11 = false;
                     } else {
                        boolean var10;
                        while(true) {
                           if (var2 < 1) {
                              var5 = var8;
                              var8 = null;
                              var10 = false;
                              break;
                           }

                           var5 = var8.substring(0, var2);
                           if ((String)PhoneView.this.codesMap.get(var5) != null) {
                              StringBuilder var6 = new StringBuilder();
                              var6.append(var8.substring(var2));
                              var6.append(PhoneView.this.phoneField.getText().toString());
                              var8 = var6.toString();
                              PhoneView.this.codeField.setText(var5);
                              var10 = true;
                              break;
                           }

                           --var2;
                        }

                        var12 = var5;
                        var11 = var10;
                        if (!var10) {
                           StringBuilder var9 = new StringBuilder();
                           var9.append(var5.substring(1));
                           var9.append(PhoneView.this.phoneField.getText().toString());
                           var8 = var9.toString();
                           EditTextBoldCursor var7 = PhoneView.this.codeField;
                           var12 = var5.substring(0, 1);
                           var7.setText(var12);
                           var11 = var10;
                        }
                     }

                     var5 = (String)PhoneView.this.codesMap.get(var12);
                     if (var5 != null) {
                        var2 = PhoneView.this.countriesArray.indexOf(var5);
                        if (var2 != -1) {
                           PhoneView.this.ignoreSelection = true;
                           PhoneView.this.countryButton.setText((CharSequence)PhoneView.this.countriesArray.get(var2));
                           String var13 = (String)PhoneView.this.phoneFormatMap.get(var12);
                           HintEditText var14 = PhoneView.this.phoneField;
                           var5 = (String)var3;
                           if (var13 != null) {
                              var5 = var13.replace('X', '–');
                           }

                           var14.setHintText(var5);
                           PhoneView.this.countryState = 0;
                        } else {
                           PhoneView.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                           PhoneView.this.phoneField.setHintText((String)null);
                           PhoneView.this.countryState = 2;
                        }
                     } else {
                        PhoneView.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                        PhoneView.this.phoneField.setHintText((String)null);
                        PhoneView.this.countryState = 2;
                     }

                     if (!var11) {
                        PhoneView.this.codeField.setSelection(PhoneView.this.codeField.getText().length());
                     }

                     if (var8 != null) {
                        PhoneView.this.phoneField.requestFocus();
                        PhoneView.this.phoneField.setText(var8);
                        PhoneView.this.phoneField.setSelection(PhoneView.this.phoneField.length());
                     }
                  }

                  PhoneView.this.ignoreOnTextChange = false;
               }
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.codeField.setOnEditorActionListener(new _$$Lambda$LoginActivity$PhoneView$s6X9t4lGGXuJqcRZlLSeul8QD9A(this));
         this.phoneField = new HintEditText(var2) {
            public boolean onTouchEvent(MotionEvent var1) {
               if (var1.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                  this.clearFocus();
                  this.requestFocus();
               }

               return super.onTouchEvent(var1);
            }
         };
         this.phoneField.setInputType(3);
         this.phoneField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.phoneField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(var2, false));
         this.phoneField.setPadding(0, 0, 0, 0);
         this.phoneField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.phoneField.setCursorSize(AndroidUtilities.dp(20.0F));
         this.phoneField.setCursorWidth(1.5F);
         this.phoneField.setTextSize(1, 18.0F);
         this.phoneField.setMaxLines(1);
         this.phoneField.setGravity(19);
         this.phoneField.setImeOptions(268435461);
         var20.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0F));
         this.phoneField.addTextChangedListener(new TextWatcher() {
            private int actionPosition;
            private int characterAction = -1;

            public void afterTextChanged(Editable var1) {
               if (!PhoneView.this.ignoreOnPhoneChange) {
                  int var2 = PhoneView.this.phoneField.getSelectionStart();
                  String var3 = PhoneView.this.phoneField.getText().toString();
                  int var4 = var2;
                  String var5 = var3;
                  if (this.characterAction == 3) {
                     StringBuilder var12 = new StringBuilder();
                     var12.append(var3.substring(0, this.actionPosition));
                     var12.append(var3.substring(this.actionPosition + 1));
                     var5 = var12.toString();
                     var4 = var2 - 1;
                  }

                  StringBuilder var11 = new StringBuilder(var5.length());

                  int var6;
                  for(var2 = 0; var2 < var5.length(); var2 = var6) {
                     var6 = var2 + 1;
                     String var7 = var5.substring(var2, var6);
                     if ("0123456789".contains(var7)) {
                        var11.append(var7);
                     }
                  }

                  PhoneView.this.ignoreOnPhoneChange = true;
                  var5 = PhoneView.this.phoneField.getHintText();
                  var2 = var4;
                  if (var5 != null) {
                     label71: {
                        for(var2 = 0; var2 < var11.length(); var4 = var6) {
                           if (var2 >= var5.length()) {
                              var11.insert(var2, ' ');
                              if (var4 == var2 + 1) {
                                 var2 = this.characterAction;
                                 if (var2 != 2 && var2 != 3) {
                                    var2 = var4 + 1;
                                    break label71;
                                 }
                              }
                              break;
                           }

                           int var8 = var2;
                           var6 = var4;
                           if (var5.charAt(var2) == ' ') {
                              var11.insert(var2, ' ');
                              ++var2;
                              var8 = var2;
                              var6 = var4;
                              if (var4 == var2) {
                                 int var9 = this.characterAction;
                                 var8 = var2;
                                 var6 = var4;
                                 if (var9 != 2) {
                                    var8 = var2;
                                    var6 = var4;
                                    if (var9 != 3) {
                                       var6 = var4 + 1;
                                       var8 = var2;
                                    }
                                 }
                              }
                           }

                           var2 = var8 + 1;
                        }

                        var2 = var4;
                     }
                  }

                  var1.replace(0, var1.length(), var11);
                  if (var2 >= 0) {
                     HintEditText var10 = PhoneView.this.phoneField;
                     if (var2 > PhoneView.this.phoneField.length()) {
                        var2 = PhoneView.this.phoneField.length();
                     }

                     var10.setSelection(var2);
                  }

                  PhoneView.this.phoneField.onTextChange();
                  PhoneView.this.ignoreOnPhoneChange = false;
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
         this.phoneField.setOnEditorActionListener(new _$$Lambda$LoginActivity$PhoneView$TEi0WT_UJWWkmE9ZUo0lhioWQRI(this));
         this.phoneField.setOnKeyListener(new _$$Lambda$LoginActivity$PhoneView$Qv5VLHdHxMblEcKPlwyU2WrV61c(this));
         this.textView2 = new TextView(var2);
         this.textView2.setText(LocaleController.getString("StartText", 2131560805));
         this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         this.textView2.setTextSize(1, 14.0F);
         var3 = this.textView2;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var3.setGravity(var4);
         this.textView2.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         var3 = this.textView2;
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         this.addView(var3, LayoutHelper.createLinear(-2, -2, var4, 0, 28, 0, 10));
         var3 = new TextView(var2);
         var3.setText("You may need to set up a proxy before you login, in case your country or ISP blocks Telegram");
         var3.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
         var3.setTextSize(1, 14.0F);
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         var3.setGravity(var4);
         var3.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }

         this.addView(var3, LayoutHelper.createLinear(-2, -2, var4, 0, 28, 0, 10));
         var3 = new TextView(var2);
         var3.setText("SET A PROXY");
         var3.setGravity(17);
         var3.setTextColor(-1);
         var3.setTextSize(1, 16.0F);
         var3.setBackgroundResource(2131165800);
         if (VERSION.SDK_INT >= 21) {
            StateListAnimator var23 = new StateListAnimator();
            ObjectAnimator var6 = ObjectAnimator.ofFloat(var3, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
            var23.addState(new int[]{16842919}, var6);
            var6 = ObjectAnimator.ofFloat(var3, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
            var23.addState(new int[0], var6);
            var3.setStateListAnimator(var23);
         }

         var3.setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(20.0F), AndroidUtilities.dp(10.0F));
         this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, 81, 10.0F, 0.0F, 10.0F, 10.0F));
         var3.setOnClickListener(new _$$Lambda$LoginActivity$PhoneView$qJHy69D4Arc703i2l6zpx_M85PQ(this));
         if (LoginActivity.this.newAccount) {
            this.checkBoxCell = new CheckBoxCell(var2, 2);
            this.checkBoxCell.setText(LocaleController.getString("SyncContacts", 2131560849), "", LoginActivity.this.syncContacts, false);
            this.addView(this.checkBoxCell, LayoutHelper.createLinear(-2, -1, 51, 0, 0, 0, 0));
            this.checkBoxCell.setOnClickListener(new android.view.View.OnClickListener() {
               private Toast visibleToast;

               public void onClick(View var1) {
                  if (LoginActivity.this.getParentActivity() != null) {
                     CheckBoxCell var2 = (CheckBoxCell)var1;
                     LoginActivity var4 = LoginActivity.this;
                     var4.syncContacts = var4.syncContacts ^ true;
                     var2.setChecked(LoginActivity.this.syncContacts, true);

                     try {
                        if (this.visibleToast != null) {
                           this.visibleToast.cancel();
                        }
                     } catch (Exception var3) {
                        FileLog.e((Throwable)var3);
                     }

                     if (LoginActivity.this.syncContacts) {
                        this.visibleToast = Toast.makeText(LoginActivity.this.getParentActivity(), LocaleController.getString("SyncContactsOn", 2131560856), 0);
                        this.visibleToast.show();
                     } else {
                        this.visibleToast = Toast.makeText(LoginActivity.this.getParentActivity(), LocaleController.getString("SyncContactsOff", 2131560855), 0);
                        this.visibleToast.show();
                     }

                  }
               }
            });
         }

         HashMap var22 = new HashMap();

         Exception var15;
         Exception var10000;
         boolean var10001;
         label114: {
            label113: {
               BufferedReader var14;
               try {
                  InputStreamReader var17 = new InputStreamReader(this.getResources().getAssets().open("countries.txt"));
                  var14 = new BufferedReader(var17);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label113;
               }

               while(true) {
                  String var18;
                  try {
                     var18 = var14.readLine();
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }

                  if (var18 == null) {
                     try {
                        var14.close();
                        break label114;
                     } catch (Exception var9) {
                        var10000 = var9;
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
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break;
                  }

                  try {
                     var22.put(var19[1], var19[2]);
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }
               }
            }

            var15 = var10000;
            FileLog.e((Throwable)var15);
         }

         Collections.sort(this.countriesArray, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
         var2 = null;

         String var16;
         label92: {
            label122: {
               TelephonyManager var24;
               try {
                  var24 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label122;
               }

               var16 = var2;
               if (var24 == null) {
                  break label92;
               }

               try {
                  var16 = var24.getSimCountryIso().toUpperCase();
                  break label92;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            var15 = var10000;
            FileLog.e((Throwable)var15);
            var16 = var2;
         }

         if (var16 != null) {
            var16 = (String)var22.get(var16);
            if (var16 != null && this.countriesArray.indexOf(var16) != -1) {
               this.codeField.setText((CharSequence)this.countriesMap.get(var16));
               this.countryState = 0;
            }
         }

         if (this.codeField.length() == 0) {
            this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
            this.phoneField.setHintText((String)null);
            this.countryState = 1;
         }

         if (this.codeField.length() != 0) {
            this.phoneField.requestFocus();
            HintEditText var21 = this.phoneField;
            var21.setSelection(var21.length());
         } else {
            this.codeField.requestFocus();
         }

      }

      public void fillNumber() {
         Exception var10000;
         label160: {
            TelephonyManager var1;
            int var2;
            boolean var10001;
            try {
               var1 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
               if (var1.getSimState() == 1 || var1.getPhoneType() == 0) {
                  return;
               }

               var2 = VERSION.SDK_INT;
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label160;
            }

            String var3 = null;
            Object var4 = null;
            boolean var5;
            boolean var22;
            if (var2 >= 23) {
               label157: {
                  label146: {
                     label145: {
                        try {
                           if (LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
                              break label145;
                           }
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label160;
                        }

                        var22 = false;
                        break label146;
                     }

                     var22 = true;
                  }

                  var5 = var22;

                  try {
                     if (!LoginActivity.this.checkShowPermissions) {
                        break label157;
                     }
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label160;
                  }

                  var5 = var22;
                  if (!var22) {
                     try {
                        LoginActivity.this.permissionsShowItems.clear();
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label160;
                     }

                     if (!var22) {
                        try {
                           LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_STATE");
                        } catch (Exception var11) {
                           var10000 = var11;
                           var10001 = false;
                           break label160;
                        }
                     }

                     label98: {
                        SharedPreferences var23;
                        label161: {
                           try {
                              if (LoginActivity.this.permissionsShowItems.isEmpty()) {
                                 break label98;
                              }

                              var23 = MessagesController.getGlobalMainSettings();
                              if (var23.getBoolean("firstloginshow", true) || LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                                 break label161;
                              }
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label160;
                           }

                           try {
                              LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsShowItems.toArray(new String[0]), 7);
                              break label98;
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                              break label160;
                           }
                        }

                        try {
                           var23.edit().putBoolean("firstloginshow", false).commit();
                           AlertDialog.Builder var24 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                           var24.setTitle(LocaleController.getString("AppName", 2131558635));
                           var24.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                           var24.setMessage(LocaleController.getString("AllowFillNumber", 2131558606));
                           LoginActivity.this.permissionsShowDialog = LoginActivity.this.showDialog(var24.create());
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break label160;
                        }
                     }

                     try {
                        return;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label160;
                     }
                  }
               }
            } else {
               var5 = true;
            }

            try {
               if (LoginActivity.this.newAccount) {
                  return;
               }
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label160;
            }

            if (!var5) {
               return;
            }

            String var21;
            int var26;
            try {
               var21 = PhoneFormat.stripExceptNumbers(var1.getLine1Number());
               if (TextUtils.isEmpty(var21)) {
                  return;
               }

               var26 = var21.length();
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label160;
            }

            var2 = 4;
            if (var26 > 4) {
               while(true) {
                  label122: {
                     if (var2 >= 1) {
                        try {
                           String var6 = var21.substring(0, var2);
                           if ((String)this.codesMap.get(var6) == null) {
                              break label122;
                           }

                           var3 = var21.substring(var2);
                           this.codeField.setText(var6);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label160;
                        }

                        var22 = true;
                     } else {
                        var22 = false;
                        var3 = (String)var4;
                     }

                     if (!var22) {
                        try {
                           var3 = var21.substring(1);
                           this.codeField.setText(var21.substring(0, 1));
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label160;
                        }
                     }
                     break;
                  }

                  --var2;
               }
            }

            if (var3 == null) {
               return;
            }

            try {
               this.phoneField.requestFocus();
               this.phoneField.setText(var3);
               this.phoneField.setSelection(this.phoneField.length());
               return;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
            }
         }

         Exception var25 = var10000;
         FileLog.e((Throwable)var25);
      }

      public String getHeaderName() {
         return LocaleController.getString("YourPhone", 2131561158);
      }

      // $FF: synthetic method
      public void lambda$new$2$LoginActivity$PhoneView(View var1) {
         CountrySelectActivity var2 = new CountrySelectActivity(true);
         var2.setCountrySelectActivityDelegate(new _$$Lambda$LoginActivity$PhoneView$7SKvAGybNL6bKIPOIaonyBBsyEE(this));
         LoginActivity.this.presentFragment(var2);
      }

      // $FF: synthetic method
      public boolean lambda$new$3$LoginActivity$PhoneView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.phoneField.requestFocus();
            HintEditText var4 = this.phoneField;
            var4.setSelection(var4.length());
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$new$4$LoginActivity$PhoneView(TextView var1, int var2, KeyEvent var3) {
         if (var2 == 5) {
            this.onNextPressed();
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public boolean lambda$new$5$LoginActivity$PhoneView(View var1, int var2, KeyEvent var3) {
         if (var2 == 67 && this.phoneField.length() == 0) {
            this.codeField.requestFocus();
            EditTextBoldCursor var4 = this.codeField;
            var4.setSelection(var4.length());
            this.codeField.dispatchKeyEvent(var3);
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public void lambda$new$6$LoginActivity$PhoneView(View var1) {
         LoginActivity.this.presentFragment(new ProxyListActivity());
      }

      // $FF: synthetic method
      public void lambda$null$0$LoginActivity$PhoneView() {
         AndroidUtilities.showKeyboard(this.phoneField);
      }

      // $FF: synthetic method
      public void lambda$null$1$LoginActivity$PhoneView(String var1, String var2) {
         this.selectCountry(var1, var2);
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$PhoneView$mxS5ICVDzCvqTR0SgGSNeONT9eA(this), 300L);
         this.phoneField.requestFocus();
         HintEditText var3 = this.phoneField;
         var3.setSelection(var3.length());
      }

      // $FF: synthetic method
      public void lambda$null$8$LoginActivity$PhoneView(TLRPC.TL_error var1, Bundle var2, TLObject var3, TLRPC.TL_auth_sendCode var4) {
         this.nextPressed = false;
         if (var1 == null) {
            LoginActivity.this.fillNextCodeParams(var2, (TLRPC.TL_auth_sentCode)var3);
         } else {
            String var5 = var1.text;
            if (var5 != null) {
               if (var5.contains("PHONE_NUMBER_INVALID")) {
                  LoginActivity.this.needShowInvalidAlert(var4.phone_number, false);
               } else if (var1.text.contains("PHONE_PASSWORD_FLOOD")) {
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("FloodWait", 2131559495));
               } else if (var1.text.contains("PHONE_NUMBER_FLOOD")) {
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("PhoneNumberFlood", 2131560430));
               } else if (var1.text.contains("PHONE_NUMBER_BANNED")) {
                  LoginActivity.this.needShowInvalidAlert(var4.phone_number, true);
               } else if (!var1.text.contains("PHONE_CODE_EMPTY") && !var1.text.contains("PHONE_CODE_INVALID")) {
                  if (var1.text.contains("PHONE_CODE_EXPIRED")) {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("CodeExpired", 2131559120));
                  } else if (var1.text.startsWith("FLOOD_WAIT")) {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("FloodWait", 2131559495));
                  } else if (var1.code != -1000) {
                     LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), var1.text);
                  }
               } else {
                  LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidCode", 2131559671));
               }
            }
         }

         LoginActivity.this.needHideProgress(false);
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$7$LoginActivity$PhoneView(int var1, DialogInterface var2, int var3) {
         if (UserConfig.selectedAccount != var1) {
            ((LaunchActivity)LoginActivity.this.getParentActivity()).switchToAccount(var1, false);
         }

         LoginActivity.this.finishFragment();
      }

      // $FF: synthetic method
      public void lambda$onNextPressed$9$LoginActivity$PhoneView(Bundle var1, TLRPC.TL_auth_sendCode var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$PhoneView$TNd9lEaurYyNkjPSnoxbcVOUMMA(this, var4, var1, var3, var2));
      }

      // $FF: synthetic method
      public void lambda$onShow$10$LoginActivity$PhoneView() {
         if (this.phoneField != null) {
            if (this.codeField.length() != 0) {
               this.phoneField.requestFocus();
               HintEditText var1 = this.phoneField;
               var1.setSelection(var1.length());
               AndroidUtilities.showKeyboard(this.phoneField);
            } else {
               this.codeField.requestFocus();
               AndroidUtilities.showKeyboard(this.codeField);
            }
         }

      }

      public void onCancelPressed() {
         this.nextPressed = false;
      }

      public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
         if (this.ignoreSelection) {
            this.ignoreSelection = false;
         } else {
            this.ignoreOnTextChange = true;
            String var6 = (String)this.countriesArray.get(var3);
            this.codeField.setText((CharSequence)this.countriesMap.get(var6));
            this.ignoreOnTextChange = false;
         }
      }

      public void onNextPressed() {
         if (LoginActivity.this.getParentActivity() != null && !this.nextPressed) {
            TelephonyManager var1 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            StringBuilder var2;
            if (BuildVars.DEBUG_VERSION) {
               var2 = new StringBuilder();
               var2.append("sim status = ");
               var2.append(var1.getSimState());
               FileLog.d(var2.toString());
            }

            int var3 = var1.getSimState();
            boolean var4;
            if (var3 != 1 && var3 != 0 && var1.getPhoneType() != 0 && !AndroidUtilities.isAirplaneModeOn()) {
               var4 = true;
            } else {
               var4 = false;
            }

            boolean var6;
            if (VERSION.SDK_INT >= 23 && var4) {
               boolean var21;
               if (LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
                  var21 = true;
               } else {
                  var21 = false;
               }

               boolean var5;
               if (LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var6 = var21;
               if (LoginActivity.this.checkPermissions) {
                  LoginActivity.this.permissionsItems.clear();
                  if (!var21) {
                     LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                  }

                  if (!var5) {
                     LoginActivity.this.permissionsItems.add("android.permission.CALL_PHONE");
                  }

                  var6 = var21;
                  if (!LoginActivity.this.permissionsItems.isEmpty()) {
                     label184: {
                        SharedPreferences var19 = MessagesController.getGlobalMainSettings();
                        if (!var5 && var21) {
                           LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                        } else if (!var19.getBoolean("firstlogin", true) && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                           try {
                              LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                           } catch (Exception var16) {
                              var5 = false;
                              break label184;
                           }
                        } else {
                           var19.edit().putBoolean("firstlogin", false).commit();
                           AlertDialog.Builder var7 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                           var7.setTitle(LocaleController.getString("AppName", 2131558635));
                           var7.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                           var7.setMessage(LocaleController.getString("AllowReadCall", 2131558607));
                           LoginActivity var20 = LoginActivity.this;
                           var20.permissionsDialog = var20.showDialog(var7.create());
                        }

                        var5 = true;
                     }

                     var6 = var21;
                     if (var5) {
                        return;
                     }
                  }
               }
            } else {
               var6 = true;
            }

            var3 = this.countryState;
            if (var3 == 1) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("ChooseCountry", 2131559086));
               return;
            }

            if (var3 == 2 && !BuildVars.DEBUG_VERSION) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("WrongCountry", 2131561125));
               return;
            }

            if (this.codeField.length() == 0) {
               LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidPhoneNumber", 2131559674));
               return;
            }

            var2 = new StringBuilder();
            var2.append("");
            var2.append(this.codeField.getText());
            var2.append(this.phoneField.getText());
            String var22 = PhoneFormat.stripExceptNumbers(var2.toString());
            if (LoginActivity.this.getParentActivity() instanceof LaunchActivity) {
               for(var3 = 0; var3 < 3; ++var3) {
                  UserConfig var24 = UserConfig.getInstance(var3);
                  if (var24.isClientActivated() && PhoneNumberUtils.compare(var22, var24.getCurrentUser().phone)) {
                     AlertDialog.Builder var23 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                     var23.setTitle(LocaleController.getString("AppName", 2131558635));
                     var23.setMessage(LocaleController.getString("AccountAlreadyLoggedIn", 2131558487));
                     var23.setPositiveButton(LocaleController.getString("AccountSwitch", 2131558489), new _$$Lambda$LoginActivity$PhoneView$8svHsEYnVD6CBObp_k25RKsR8iQ(this, var3));
                     var23.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                     LoginActivity.this.showDialog(var23.create());
                     return;
                  }
               }
            }

            ConnectionsManager.getInstance(LoginActivity.access$2800(LoginActivity.this)).cleanup(false);
            TLRPC.TL_auth_sendCode var25 = new TLRPC.TL_auth_sendCode();
            var25.api_hash = BuildVars.APP_HASH;
            var25.api_id = BuildVars.APP_ID;
            var25.phone_number = var22;
            var25.settings = new TLRPC.TL_codeSettings();
            TLRPC.TL_codeSettings var8 = var25.settings;
            boolean var9;
            if (var4 && var6) {
               var9 = true;
            } else {
               var9 = false;
            }

            var8.allow_flashcall = var9;
            if (VERSION.SDK_INT >= 26) {
               try {
                  var8 = var25.settings;
                  SmsManager var10 = SmsManager.getDefault();
                  Context var11 = ApplicationLoader.applicationContext;
                  Intent var12 = new Intent(ApplicationLoader.applicationContext, SmsReceiver.class);
                  var8.app_hash = var10.createAppSpecificSmsToken(PendingIntent.getBroadcast(var11, 0, var12, 134217728));
               } catch (Throwable var15) {
                  FileLog.e(var15);
               }
            } else {
               var8 = var25.settings;
               var8.app_hash = BuildVars.SMS_HASH;
               var8.app_hash_persistent = true;
            }

            SharedPreferences var26 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            if (!TextUtils.isEmpty(var25.settings.app_hash)) {
               TLRPC.TL_codeSettings var28 = var25.settings;
               var28.flags |= 8;
               var26.edit().putString("sms_hash", var25.settings.app_hash).commit();
            } else {
               var26.edit().remove("sms_hash").commit();
            }

            if (var25.settings.allow_flashcall) {
               try {
                  String var17 = var1.getLine1Number();
                  if (!TextUtils.isEmpty(var17)) {
                     var25.settings.current_number = PhoneNumberUtils.compare(var22, var17);
                     if (!var25.settings.current_number) {
                        var25.settings.allow_flashcall = false;
                     }
                  } else if (UserConfig.getActivatedAccountsCount() > 0) {
                     var25.settings.allow_flashcall = false;
                  } else {
                     var25.settings.current_number = false;
                  }
               } catch (Exception var14) {
                  var25.settings.allow_flashcall = false;
                  FileLog.e((Throwable)var14);
               }
            }

            Bundle var18 = new Bundle();
            StringBuilder var27 = new StringBuilder();
            var27.append("+");
            var27.append(this.codeField.getText());
            var27.append(" ");
            var27.append(this.phoneField.getText());
            var18.putString("phone", var27.toString());

            try {
               var27 = new StringBuilder();
               var27.append("+");
               var27.append(PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()));
               var27.append(" ");
               var27.append(PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
               var18.putString("ephone", var27.toString());
            } catch (Exception var13) {
               FileLog.e((Throwable)var13);
               var27 = new StringBuilder();
               var27.append("+");
               var27.append(var22);
               var18.putString("ephone", var27.toString());
            }

            var18.putString("phoneFormated", var22);
            this.nextPressed = true;
            var3 = ConnectionsManager.getInstance(LoginActivity.access$2900(LoginActivity.this)).sendRequest(var25, new _$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX_vOjRSCgCsccc(this, var18, var25), 27);
            LoginActivity.this.needShowProgress(var3);
         }

      }

      public void onNothingSelected(AdapterView var1) {
      }

      public void onShow() {
         super.onShow();
         this.fillNumber();
         CheckBoxCell var1 = this.checkBoxCell;
         if (var1 != null) {
            var1.setChecked(LoginActivity.this.syncContacts, false);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$PhoneView$1Ok40Ao47pjEDI9trMBSQxPJ99s(this), 100L);
      }

      public void restoreStateParams(Bundle var1) {
         String var2 = var1.getString("phoneview_code");
         if (var2 != null) {
            this.codeField.setText(var2);
         }

         String var3 = var1.getString("phoneview_phone");
         if (var3 != null) {
            this.phoneField.setText(var3);
         }

      }

      public void saveStateParams(Bundle var1) {
         String var2 = this.codeField.getText().toString();
         if (var2.length() != 0) {
            var1.putString("phoneview_code", var2);
         }

         var2 = this.phoneField.getText().toString();
         if (var2.length() != 0) {
            var1.putString("phoneview_phone", var2);
         }

      }

      public void selectCountry(String var1, String var2) {
         if (this.countriesArray.indexOf(var1) != -1) {
            this.ignoreOnTextChange = true;
            var2 = (String)this.countriesMap.get(var1);
            this.codeField.setText(var2);
            this.countryButton.setText(var1);
            var1 = (String)this.phoneFormatMap.get(var2);
            HintEditText var3 = this.phoneField;
            if (var1 != null) {
               var1 = var1.replace('X', '–');
            } else {
               var1 = null;
            }

            var3.setHintText(var1);
            this.countryState = 0;
            this.ignoreOnTextChange = false;
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
}
