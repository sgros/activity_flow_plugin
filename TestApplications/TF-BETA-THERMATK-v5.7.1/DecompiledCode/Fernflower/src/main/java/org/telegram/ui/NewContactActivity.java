package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;

public class NewContactActivity extends BaseFragment implements OnItemSelectedListener {
   private static final int done_button = 1;
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImage;
   private EditTextBoldCursor codeField;
   private HashMap codesMap = new HashMap();
   private ArrayList countriesArray = new ArrayList();
   private HashMap countriesMap = new HashMap();
   private TextView countryButton;
   private int countryState;
   private boolean donePressed;
   private ActionBarMenuItem editDoneItem;
   private AnimatorSet editDoneItemAnimation;
   private ContextProgressView editDoneItemProgress;
   private EditTextBoldCursor firstNameField;
   private boolean ignoreOnPhoneChange;
   private boolean ignoreOnTextChange;
   private boolean ignoreSelection;
   private EditTextBoldCursor lastNameField;
   private View lineView;
   private HintEditText phoneField;
   private HashMap phoneFormatMap = new HashMap();
   private TextView textView;

   // $FF: synthetic method
   static int access$1000(NewContactActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1100(NewContactActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$600(NewContactActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(NewContactActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$800(NewContactActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(NewContactActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   private void showEditDoneProgress(final boolean var1, boolean var2) {
      AnimatorSet var3 = this.editDoneItemAnimation;
      if (var3 != null) {
         var3.cancel();
      }

      if (!var2) {
         if (var1) {
            this.editDoneItem.getImageView().setScaleX(0.1F);
            this.editDoneItem.getImageView().setScaleY(0.1F);
            this.editDoneItem.getImageView().setAlpha(0.0F);
            this.editDoneItemProgress.setScaleX(1.0F);
            this.editDoneItemProgress.setScaleY(1.0F);
            this.editDoneItemProgress.setAlpha(1.0F);
            this.editDoneItem.getImageView().setVisibility(4);
            this.editDoneItemProgress.setVisibility(0);
            this.editDoneItem.setEnabled(false);
         } else {
            this.editDoneItemProgress.setScaleX(0.1F);
            this.editDoneItemProgress.setScaleY(0.1F);
            this.editDoneItemProgress.setAlpha(0.0F);
            this.editDoneItem.getImageView().setScaleX(1.0F);
            this.editDoneItem.getImageView().setScaleY(1.0F);
            this.editDoneItem.getImageView().setAlpha(1.0F);
            this.editDoneItem.getImageView().setVisibility(0);
            this.editDoneItemProgress.setVisibility(4);
            this.editDoneItem.setEnabled(true);
         }
      } else {
         this.editDoneItemAnimation = new AnimatorSet();
         if (var1) {
            this.editDoneItemProgress.setVisibility(0);
            this.editDoneItem.setEnabled(false);
            this.editDoneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.editDoneItemProgress, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.editDoneItemProgress, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.editDoneItemProgress, "alpha", new float[]{1.0F})});
         } else {
            this.editDoneItem.getImageView().setVisibility(0);
            this.editDoneItem.setEnabled(true);
            this.editDoneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.editDoneItemProgress, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.editDoneItemProgress, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.editDoneItemProgress, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.editDoneItem.getImageView(), "alpha", new float[]{1.0F})});
         }

         this.editDoneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(var1x)) {
                  NewContactActivity.this.editDoneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(var1x)) {
                  if (!var1) {
                     NewContactActivity.this.editDoneItemProgress.setVisibility(4);
                  } else {
                     NewContactActivity.this.editDoneItem.getImageView().setVisibility(4);
                  }
               }

            }
         });
         this.editDoneItemAnimation.setDuration(150L);
         this.editDoneItemAnimation.start();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("AddContactTitle", 2131558569));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$null$0$NewContactActivity$1(TLRPC.TL_inputPhoneContact var1, DialogInterface var2, int var3) {
            try {
               Intent var5 = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", var1.phone, (String)null));
               var5.putExtra("sms_body", ContactsController.getInstance(NewContactActivity.access$1100(NewContactActivity.this)).getInviteText(1));
               NewContactActivity.this.getParentActivity().startActivityForResult(var5, 500);
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

         }

         // $FF: synthetic method
         public void lambda$null$1$NewContactActivity$1(TLRPC.TL_contacts_importedContacts var1, TLRPC.TL_inputPhoneContact var2, TLRPC.TL_error var3, TLRPC.TL_contacts_importContacts var4) {
            NewContactActivity.this.donePressed = false;
            if (var1 != null) {
               if (!var1.users.isEmpty()) {
                  MessagesController.getInstance(NewContactActivity.access$900(NewContactActivity.this)).putUsers(var1.users, false);
                  MessagesController.openChatOrProfileWith((TLRPC.User)var1.users.get(0), (TLRPC.Chat)null, NewContactActivity.this, 1, true);
               } else {
                  if (NewContactActivity.this.getParentActivity() == null) {
                     return;
                  }

                  NewContactActivity.this.showEditDoneProgress(false, true);
                  AlertDialog.Builder var5 = new AlertDialog.Builder(NewContactActivity.this.getParentActivity());
                  var5.setTitle(LocaleController.getString("AppName", 2131558635));
                  var5.setMessage(LocaleController.formatString("ContactNotRegistered", 2131559145, ContactsController.formatName(var2.first_name, var2.last_name)));
                  var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  var5.setPositiveButton(LocaleController.getString("Invite", 2131559676), new _$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84(this, var2));
                  NewContactActivity.this.showDialog(var5.create());
               }
            } else {
               NewContactActivity.this.showEditDoneProgress(false, true);
               AlertsCreator.processError(NewContactActivity.access$1000(NewContactActivity.this), var3, NewContactActivity.this, var4);
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$2$NewContactActivity$1(TLRPC.TL_inputPhoneContact var1, TLRPC.TL_contacts_importContacts var2, TLObject var3, TLRPC.TL_error var4) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs(this, (TLRPC.TL_contacts_importedContacts)var3, var1, var4, var2));
         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               NewContactActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (NewContactActivity.this.donePressed) {
                  return;
               }

               Vibrator var5;
               if (NewContactActivity.this.firstNameField.length() == 0) {
                  var5 = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                  if (var5 != null) {
                     var5.vibrate(200L);
                  }

                  AndroidUtilities.shakeView(NewContactActivity.this.firstNameField, 2.0F, 0);
                  return;
               }

               if (NewContactActivity.this.codeField.length() == 0) {
                  var5 = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                  if (var5 != null) {
                     var5.vibrate(200L);
                  }

                  AndroidUtilities.shakeView(NewContactActivity.this.codeField, 2.0F, 0);
                  return;
               }

               if (NewContactActivity.this.phoneField.length() == 0) {
                  var5 = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                  if (var5 != null) {
                     var5.vibrate(200L);
                  }

                  AndroidUtilities.shakeView(NewContactActivity.this.phoneField, 2.0F, 0);
                  return;
               }

               NewContactActivity.this.donePressed = true;
               NewContactActivity.this.showEditDoneProgress(true, true);
               TLRPC.TL_contacts_importContacts var2 = new TLRPC.TL_contacts_importContacts();
               TLRPC.TL_inputPhoneContact var3 = new TLRPC.TL_inputPhoneContact();
               var3.first_name = NewContactActivity.this.firstNameField.getText().toString();
               var3.last_name = NewContactActivity.this.lastNameField.getText().toString();
               StringBuilder var4 = new StringBuilder();
               var4.append("+");
               var4.append(NewContactActivity.this.codeField.getText().toString());
               var4.append(NewContactActivity.this.phoneField.getText().toString());
               var3.phone = var4.toString();
               var2.contacts.add(var3);
               var1 = ConnectionsManager.getInstance(NewContactActivity.access$600(NewContactActivity.this)).sendRequest(var2, new _$$Lambda$NewContactActivity$1$WRq0Ss_PBCngsAibqDEMoSm52R4(this, var3, var2), 2);
               ConnectionsManager.getInstance(NewContactActivity.access$800(NewContactActivity.this)).bindRequestToGuid(var1, NewContactActivity.access$700(NewContactActivity.this));
            }

         }
      });
      this.avatarDrawable = new AvatarDrawable();
      this.avatarDrawable.setInfo(5, "", "", false);
      this.editDoneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.editDoneItem.setContentDescription(LocaleController.getString("Done", 2131559299));
      this.editDoneItemProgress = new ContextProgressView(var1, 1);
      this.editDoneItem.addView(this.editDoneItemProgress, LayoutHelper.createFrame(-1, -1.0F));
      this.editDoneItemProgress.setVisibility(4);
      super.fragmentView = new ScrollView(var1);
      LinearLayout var2 = new LinearLayout(var1);
      var2.setPadding(AndroidUtilities.dp(24.0F), 0, AndroidUtilities.dp(24.0F), 0);
      var2.setOrientation(1);
      ((ScrollView)super.fragmentView).addView(var2, LayoutHelper.createScroll(-1, -2, 51));
      var2.setOnTouchListener(_$$Lambda$NewContactActivity$dyt1ArQHbLSL06GO_wtQQZkRhQE.INSTANCE);
      FrameLayout var3 = new FrameLayout(var1);
      var2.addView(var3, LayoutHelper.createLinear(-1, -2, 0.0F, 24.0F, 0.0F, 0.0F));
      this.avatarImage = new BackupImageView(var1);
      this.avatarImage.setImageDrawable(this.avatarDrawable);
      var3.addView(this.avatarImage, LayoutHelper.createFrame(60, 60.0F, 51, 0.0F, 9.0F, 0.0F, 0.0F));
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setMaxLines(1);
      this.firstNameField.setLines(1);
      this.firstNameField.setSingleLine(true);
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.firstNameField.setGravity(3);
      this.firstNameField.setInputType(49152);
      this.firstNameField.setImeOptions(5);
      this.firstNameField.setHint(LocaleController.getString("FirstName", 2131559494));
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      var3.addView(this.firstNameField, LayoutHelper.createFrame(-1, 34.0F, 51, 84.0F, 0.0F, 0.0F, 0.0F));
      this.firstNameField.setOnEditorActionListener(new _$$Lambda$NewContactActivity$OEffd5rsJU1asHgIO5gqt5wMyr4(this));
      this.firstNameField.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
            NewContactActivity.this.avatarImage.invalidate();
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.lastNameField = new EditTextBoldCursor(var1);
      this.lastNameField.setTextSize(1, 18.0F);
      this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.lastNameField.setMaxLines(1);
      this.lastNameField.setLines(1);
      this.lastNameField.setSingleLine(true);
      this.lastNameField.setGravity(3);
      this.lastNameField.setInputType(49152);
      this.lastNameField.setImeOptions(5);
      this.lastNameField.setHint(LocaleController.getString("LastName", 2131559728));
      this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.lastNameField.setCursorWidth(1.5F);
      var3.addView(this.lastNameField, LayoutHelper.createFrame(-1, 34.0F, 51, 84.0F, 44.0F, 0.0F, 0.0F));
      this.lastNameField.setOnEditorActionListener(new _$$Lambda$NewContactActivity$caCQM7G1cFeQM5WDamEHC5G1jDk(this));
      this.lastNameField.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
            NewContactActivity.this.avatarImage.invalidate();
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.countryButton = new TextView(var1);
      this.countryButton.setTextSize(1, 18.0F);
      this.countryButton.setPadding(AndroidUtilities.dp(6.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(6.0F), 0);
      this.countryButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.countryButton.setMaxLines(1);
      this.countryButton.setSingleLine(true);
      this.countryButton.setEllipsize(TruncateAt.END);
      this.countryButton.setGravity(3);
      this.countryButton.setBackgroundResource(2131165857);
      var2.addView(this.countryButton, LayoutHelper.createLinear(-1, 36, 0.0F, 24.0F, 0.0F, 14.0F));
      this.countryButton.setOnClickListener(new _$$Lambda$NewContactActivity$PQwWlWssBZjUKNZQJ0dIzeJV9OI(this));
      this.lineView = new View(var1);
      this.lineView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), 0);
      this.lineView.setBackgroundColor(Theme.getColor("windowBackgroundWhiteGrayLine"));
      var2.addView(this.lineView, LayoutHelper.createLinear(-1, 1, 0.0F, -17.5F, 0.0F, 0.0F));
      LinearLayout var16 = new LinearLayout(var1);
      var16.setOrientation(0);
      var2.addView(var16, LayoutHelper.createLinear(-1, -2, 0.0F, 20.0F, 0.0F, 0.0F));
      this.textView = new TextView(var1);
      this.textView.setText("+");
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 18.0F);
      this.textView.setImportantForAccessibility(2);
      var16.addView(this.textView, LayoutHelper.createLinear(-2, -2));
      this.codeField = new EditTextBoldCursor(var1);
      this.codeField.setInputType(3);
      this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.codeField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.codeField.setCursorWidth(1.5F);
      this.codeField.setPadding(AndroidUtilities.dp(10.0F), 0, 0, 0);
      this.codeField.setTextSize(1, 18.0F);
      this.codeField.setMaxLines(1);
      this.codeField.setGravity(19);
      this.codeField.setImeOptions(268435461);
      var16.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0F, 0.0F, 16.0F, 0.0F));
      this.codeField.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            if (!NewContactActivity.this.ignoreOnTextChange) {
               NewContactActivity.this.ignoreOnTextChange = true;
               String var2 = PhoneFormat.stripExceptNumbers(NewContactActivity.this.codeField.getText().toString());
               NewContactActivity.this.codeField.setText(var2);
               int var3 = var2.length();
               Object var4 = null;
               if (var3 == 0) {
                  NewContactActivity.this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
                  NewContactActivity.this.phoneField.setHintText((String)null);
                  NewContactActivity.this.countryState = 1;
               } else {
                  int var5 = var2.length();
                  var3 = 4;
                  String var6;
                  String var9;
                  boolean var12;
                  if (var5 <= 4) {
                     var9 = null;
                     var12 = false;
                  } else {
                     NewContactActivity.this.ignoreOnTextChange = true;

                     StringBuilder var8;
                     boolean var11;
                     while(true) {
                        if (var3 < 1) {
                           var6 = var2;
                           var9 = null;
                           var11 = false;
                           break;
                        }

                        var6 = var2.substring(0, var3);
                        if ((String)NewContactActivity.this.codesMap.get(var6) != null) {
                           var8 = new StringBuilder();
                           var8.append(var2.substring(var3, var2.length()));
                           var8.append(NewContactActivity.this.phoneField.getText().toString());
                           var9 = var8.toString();
                           NewContactActivity.this.codeField.setText(var6);
                           var11 = true;
                           break;
                        }

                        --var3;
                     }

                     var2 = var6;
                     var12 = var11;
                     if (!var11) {
                        NewContactActivity.this.ignoreOnTextChange = true;
                        var8 = new StringBuilder();
                        var8.append(var6.substring(1, var6.length()));
                        var8.append(NewContactActivity.this.phoneField.getText().toString());
                        var9 = var8.toString();
                        EditTextBoldCursor var7 = NewContactActivity.this.codeField;
                        var2 = var6.substring(0, 1);
                        var7.setText(var2);
                        var12 = var11;
                     }
                  }

                  var6 = (String)NewContactActivity.this.codesMap.get(var2);
                  if (var6 != null) {
                     var3 = NewContactActivity.this.countriesArray.indexOf(var6);
                     if (var3 != -1) {
                        NewContactActivity.this.ignoreSelection = true;
                        NewContactActivity.this.countryButton.setText((CharSequence)NewContactActivity.this.countriesArray.get(var3));
                        String var13 = (String)NewContactActivity.this.phoneFormatMap.get(var2);
                        HintEditText var10 = NewContactActivity.this.phoneField;
                        var6 = (String)var4;
                        if (var13 != null) {
                           var6 = var13.replace('X', '–');
                        }

                        var10.setHintText(var6);
                        NewContactActivity.this.countryState = 0;
                     } else {
                        NewContactActivity.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                        NewContactActivity.this.phoneField.setHintText((String)null);
                        NewContactActivity.this.countryState = 2;
                     }
                  } else {
                     NewContactActivity.this.countryButton.setText(LocaleController.getString("WrongCountry", 2131561125));
                     NewContactActivity.this.phoneField.setHintText((String)null);
                     NewContactActivity.this.countryState = 2;
                  }

                  if (!var12) {
                     NewContactActivity.this.codeField.setSelection(NewContactActivity.this.codeField.getText().length());
                  }

                  if (var9 != null) {
                     NewContactActivity.this.phoneField.requestFocus();
                     NewContactActivity.this.phoneField.setText(var9);
                     NewContactActivity.this.phoneField.setSelection(NewContactActivity.this.phoneField.length());
                  }
               }

               NewContactActivity.this.ignoreOnTextChange = false;
            }
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.codeField.setOnEditorActionListener(new _$$Lambda$NewContactActivity$sAQJMXy_aH9t_IBkPAm16jh4ito(this));
      this.phoneField = new HintEditText(var1);
      this.phoneField.setInputType(3);
      this.phoneField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.phoneField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      this.phoneField.setPadding(0, 0, 0, 0);
      this.phoneField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.phoneField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.phoneField.setCursorWidth(1.5F);
      this.phoneField.setTextSize(1, 18.0F);
      this.phoneField.setMaxLines(1);
      this.phoneField.setGravity(19);
      this.phoneField.setImeOptions(268435462);
      var16.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0F));
      this.phoneField.addTextChangedListener(new TextWatcher() {
         private int actionPosition;
         private int characterAction = -1;

         public void afterTextChanged(Editable var1) {
            if (!NewContactActivity.this.ignoreOnPhoneChange) {
               int var2 = NewContactActivity.this.phoneField.getSelectionStart();
               String var3 = NewContactActivity.this.phoneField.getText().toString();
               String var9 = var3;
               int var4 = var2;
               if (this.characterAction == 3) {
                  StringBuilder var10 = new StringBuilder();
                  var10.append(var3.substring(0, this.actionPosition));
                  var10.append(var3.substring(this.actionPosition + 1, var3.length()));
                  var9 = var10.toString();
                  var4 = var2 - 1;
               }

               StringBuilder var12 = new StringBuilder(var9.length());

               int var5;
               for(var2 = 0; var2 < var9.length(); var2 = var5) {
                  var5 = var2 + 1;
                  String var6 = var9.substring(var2, var5);
                  if ("0123456789".contains(var6)) {
                     var12.append(var6);
                  }
               }

               NewContactActivity.this.ignoreOnPhoneChange = true;
               var9 = NewContactActivity.this.phoneField.getHintText();
               var2 = var4;
               if (var9 != null) {
                  label71: {
                     for(var2 = 0; var2 < var12.length(); var4 = var5) {
                        if (var2 >= var9.length()) {
                           var12.insert(var2, ' ');
                           if (var4 == var2 + 1) {
                              var2 = this.characterAction;
                              if (var2 != 2 && var2 != 3) {
                                 var2 = var4 + 1;
                                 break label71;
                              }
                           }
                           break;
                        }

                        var5 = var4;
                        int var7 = var2;
                        if (var9.charAt(var2) == ' ') {
                           var12.insert(var2, ' ');
                           ++var2;
                           var5 = var4;
                           var7 = var2;
                           if (var4 == var2) {
                              int var8 = this.characterAction;
                              var5 = var4;
                              var7 = var2;
                              if (var8 != 2) {
                                 var5 = var4;
                                 var7 = var2;
                                 if (var8 != 3) {
                                    var5 = var4 + 1;
                                    var7 = var2;
                                 }
                              }
                           }
                        }

                        var2 = var7 + 1;
                     }

                     var2 = var4;
                  }
               }

               NewContactActivity.this.phoneField.setText(var12);
               if (var2 >= 0) {
                  HintEditText var11 = NewContactActivity.this.phoneField;
                  if (var2 > NewContactActivity.this.phoneField.length()) {
                     var2 = NewContactActivity.this.phoneField.length();
                  }

                  var11.setSelection(var2);
               }

               NewContactActivity.this.phoneField.onTextChange();
               NewContactActivity.this.ignoreOnPhoneChange = false;
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
      this.phoneField.setOnEditorActionListener(new _$$Lambda$NewContactActivity$6q60KF1tjXtvySPg1IX8F4PNNEY(this));
      this.phoneField.setOnKeyListener(new _$$Lambda$NewContactActivity$5Ca3pvZCNy2Se_fsqStW34es8nQ(this));
      HashMap var17 = new HashMap();

      String var12;
      Exception var14;
      Exception var10000;
      boolean var10001;
      label79: {
         label78: {
            BufferedReader var15;
            try {
               InputStreamReader var4 = new InputStreamReader(var1.getResources().getAssets().open("countries.txt"));
               var15 = new BufferedReader(var4);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label78;
            }

            while(true) {
               try {
                  var12 = var15.readLine();
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (var12 == null) {
                  try {
                     var15.close();
                     break label79;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }
               }

               String[] var13;
               try {
                  var13 = var12.split(";");
                  this.countriesArray.add(0, var13[2]);
                  this.countriesMap.put(var13[2], var13[0]);
                  this.codesMap.put(var13[0], var13[2]);
                  if (var13.length > 3) {
                     this.phoneFormatMap.put(var13[0], var13[3]);
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               try {
                  var17.put(var13[1], var13[2]);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }
         }

         var14 = var10000;
         FileLog.e((Throwable)var14);
      }

      Collections.sort(this.countriesArray, _$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
      var2 = null;

      label57: {
         label84: {
            TelephonyManager var18;
            try {
               var18 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label84;
            }

            var12 = var2;
            if (var18 == null) {
               break label57;
            }

            try {
               var12 = var18.getSimCountryIso().toUpperCase();
               break label57;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         var14 = var10000;
         FileLog.e((Throwable)var14);
         var12 = var2;
      }

      if (var12 != null) {
         var12 = (String)var17.get(var12);
         if (var12 != null && this.countriesArray.indexOf(var12) != -1) {
            this.codeField.setText((CharSequence)this.countriesMap.get(var12));
            this.countryState = 0;
         }
      }

      if (this.codeField.length() == 0) {
         this.countryButton.setText(LocaleController.getString("ChooseCountry", 2131559086));
         this.phoneField.setHintText((String)null);
         this.countryState = 1;
      }

      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk var1 = new _$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk(this);
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.codeField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.phoneField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.lineView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayLine"), new ThemeDescription(this.countryButton, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editDoneItemProgress, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"), new ThemeDescription(this.editDoneItemProgress, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$NewContactActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 5) {
         this.lastNameField.requestFocus();
         EditTextBoldCursor var4 = this.lastNameField;
         var4.setSelection(var4.length());
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$2$NewContactActivity(TextView var1, int var2, KeyEvent var3) {
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
   public void lambda$createView$5$NewContactActivity(View var1) {
      CountrySelectActivity var2 = new CountrySelectActivity(true);
      var2.setCountrySelectActivityDelegate(new _$$Lambda$NewContactActivity$RKjBEG5T2_fQQhQFnlm7ULenE4I(this));
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public boolean lambda$createView$6$NewContactActivity(TextView var1, int var2, KeyEvent var3) {
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
   public boolean lambda$createView$7$NewContactActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.editDoneItem.performClick();
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$8$NewContactActivity(View var1, int var2, KeyEvent var3) {
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
   public void lambda$getThemeDescriptions$9$NewContactActivity() {
      if (this.avatarImage != null) {
         this.avatarDrawable.setInfo(5, this.firstNameField.getText().toString(), this.lastNameField.getText().toString(), false);
         this.avatarImage.invalidate();
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$NewContactActivity() {
      AndroidUtilities.showKeyboard(this.phoneField);
   }

   // $FF: synthetic method
   public void lambda$null$4$NewContactActivity(String var1, String var2) {
      this.selectCountry(var1);
      AndroidUtilities.runOnUIThread(new _$$Lambda$NewContactActivity$kdGKhx_RKvrLvJRlZLpJl1Jr720(this), 300L);
      this.phoneField.requestFocus();
      HintEditText var3 = this.phoneField;
      var3.setSelection(var3.length());
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

   public void onNothingSelected(AdapterView var1) {
   }

   public void onResume() {
      super.onResume();
      if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
         this.firstNameField.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         this.firstNameField.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }

   public void selectCountry(String var1) {
      if (this.countriesArray.indexOf(var1) != -1) {
         this.ignoreOnTextChange = true;
         String var2 = (String)this.countriesMap.get(var1);
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
