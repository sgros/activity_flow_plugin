package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PlayingGameDrawable;
import org.telegram.ui.Components.PopupAudioView;
import org.telegram.ui.Components.RecordStatusDrawable;
import org.telegram.ui.Components.RoundStatusDrawable;
import org.telegram.ui.Components.SendingFileDrawable;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TypingDotsDrawable;

public class PopupNotificationActivity extends Activity implements NotificationCenter.NotificationCenterDelegate {
   private static final int id_chat_compose_panel = 1000;
   private ActionBar actionBar;
   private boolean animationInProgress = false;
   private long animationStartTime = 0L;
   private ArrayList audioViews = new ArrayList();
   private FrameLayout avatarContainer;
   private BackupImageView avatarImageView;
   private ViewGroup centerButtonsView;
   private ViewGroup centerView;
   private ChatActivityEnterView chatActivityEnterView;
   private int classGuid;
   private TextView countText;
   private TLRPC.Chat currentChat;
   private int currentMessageNum = 0;
   private MessageObject currentMessageObject = null;
   private TLRPC.User currentUser;
   private boolean finished = false;
   private ArrayList imageViews = new ArrayList();
   private boolean isReply;
   private CharSequence lastPrintString;
   private int lastResumedAccount = -1;
   private ViewGroup leftButtonsView;
   private ViewGroup leftView;
   private ViewGroup messageContainer;
   private float moveStartX = -1.0F;
   private TextView nameTextView;
   private Runnable onAnimationEndRunnable = null;
   private TextView onlineTextView;
   private RelativeLayout popupContainer;
   private ArrayList popupMessages = new ArrayList();
   private ViewGroup rightButtonsView;
   private ViewGroup rightView;
   private boolean startedMoving = false;
   private StatusDrawable[] statusDrawables = new StatusDrawable[5];
   private ArrayList textViews = new ArrayList();
   private VelocityTracker velocityTracker = null;
   private WakeLock wakeLock = null;

   private void applyViewsLayoutParams(int var1) {
      int var2 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0F);
      ViewGroup var3 = this.leftView;
      LayoutParams var4;
      if (var3 != null) {
         var4 = (LayoutParams)var3.getLayoutParams();
         if (var4.width != var2) {
            var4.width = var2;
            this.leftView.setLayoutParams(var4);
         }

         this.leftView.setTranslationX((float)(-var2 + var1));
      }

      var3 = this.leftButtonsView;
      if (var3 != null) {
         var3.setTranslationX((float)(-var2 + var1));
      }

      var3 = this.centerView;
      if (var3 != null) {
         var4 = (LayoutParams)var3.getLayoutParams();
         if (var4.width != var2) {
            var4.width = var2;
            this.centerView.setLayoutParams(var4);
         }

         this.centerView.setTranslationX((float)var1);
      }

      var3 = this.centerButtonsView;
      if (var3 != null) {
         var3.setTranslationX((float)var1);
      }

      var3 = this.rightView;
      if (var3 != null) {
         var4 = (LayoutParams)var3.getLayoutParams();
         if (var4.width != var2) {
            var4.width = var2;
            this.rightView.setLayoutParams(var4);
         }

         this.rightView.setTranslationX((float)(var2 + var1));
      }

      var3 = this.rightButtonsView;
      if (var3 != null) {
         var3.setTranslationX((float)(var2 + var1));
      }

      this.messageContainer.invalidate();
   }

   private void checkAndUpdateAvatar() {
      MessageObject var1 = this.currentMessageObject;
      if (var1 != null) {
         if (this.currentChat != null) {
            TLRPC.Chat var3 = MessagesController.getInstance(var1.currentAccount).getChat(this.currentChat.id);
            if (var3 == null) {
               return;
            }

            this.currentChat = var3;
            if (this.avatarImageView != null) {
               AvatarDrawable var2 = new AvatarDrawable(this.currentChat);
               this.avatarImageView.setImage((ImageLocation)ImageLocation.getForChat(var3, false), "50_50", (Drawable)var2, (Object)var3);
            }
         } else if (this.currentUser != null) {
            TLRPC.User var5 = MessagesController.getInstance(var1.currentAccount).getUser(this.currentUser.id);
            if (var5 == null) {
               return;
            }

            this.currentUser = var5;
            if (this.avatarImageView != null) {
               AvatarDrawable var4 = new AvatarDrawable(this.currentUser);
               this.avatarImageView.setImage((ImageLocation)ImageLocation.getForUser(var5, false), "50_50", (Drawable)var4, (Object)var5);
            }
         }

      }
   }

   private void fixLayout() {
      FrameLayout var1 = this.avatarContainer;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               if (PopupNotificationActivity.this.avatarContainer != null) {
                  PopupNotificationActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener(this);
               }

               int var1 = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0F)) / 2;
               PopupNotificationActivity.this.avatarContainer.setPadding(PopupNotificationActivity.this.avatarContainer.getPaddingLeft(), var1, PopupNotificationActivity.this.avatarContainer.getPaddingRight(), var1);
               return true;
            }
         });
      }

      ViewGroup var2 = this.messageContainer;
      if (var2 != null) {
         var2.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               PopupNotificationActivity.this.messageContainer.getViewTreeObserver().removeOnPreDrawListener(this);
               if (!PopupNotificationActivity.this.checkTransitionAnimation() && !PopupNotificationActivity.this.startedMoving) {
                  MarginLayoutParams var1 = (MarginLayoutParams)PopupNotificationActivity.this.messageContainer.getLayoutParams();
                  var1.topMargin = ActionBar.getCurrentActionBarHeight();
                  var1.bottomMargin = AndroidUtilities.dp(48.0F);
                  var1.width = -1;
                  var1.height = -1;
                  PopupNotificationActivity.this.messageContainer.setLayoutParams(var1);
                  PopupNotificationActivity.this.applyViewsLayoutParams(0);
               }

               return true;
            }
         });
      }

   }

   private LinearLayout getButtonsViewForMessage(int var1, boolean var2) {
      int var3 = this.popupMessages.size();
      LinearLayout var4 = null;
      if (var3 != 1 || var1 >= 0 && var1 < this.popupMessages.size()) {
         if (var1 == -1) {
            var3 = this.popupMessages.size() - 1;
         } else {
            var3 = var1;
            if (var1 == this.popupMessages.size()) {
               var3 = 0;
            }
         }

         MessageObject var5 = (MessageObject)this.popupMessages.get(var3);
         TLRPC.ReplyMarkup var6 = var5.messageOwner.reply_markup;
         ArrayList var7;
         int var8;
         int var9;
         int var10;
         TLRPC.TL_keyboardButtonRow var11;
         int var12;
         int var13;
         if (var5.getDialogId() == 777000L && var6 != null) {
            var7 = var6.rows;
            var8 = var7.size();
            var9 = 0;
            var1 = 0;

            while(true) {
               var10 = var1;
               if (var9 >= var8) {
                  break;
               }

               var11 = (TLRPC.TL_keyboardButtonRow)var7.get(var9);
               var12 = var11.buttons.size();

               for(var10 = 0; var10 < var12; var1 = var13) {
                  var13 = var1;
                  if ((TLRPC.KeyboardButton)var11.buttons.get(var10) instanceof TLRPC.TL_keyboardButtonCallback) {
                     var13 = var1 + 1;
                  }

                  ++var10;
               }

               ++var9;
            }
         } else {
            var10 = 0;
         }

         var8 = var5.currentAccount;
         if (var10 > 0) {
            var7 = var6.rows;
            var13 = var7.size();
            var4 = null;

            for(var1 = 0; var1 < var13; ++var1) {
               var11 = (TLRPC.TL_keyboardButtonRow)var7.get(var1);
               var12 = var11.buttons.size();

               LinearLayout var16;
               for(var9 = 0; var9 < var12; var4 = var16) {
                  TLRPC.KeyboardButton var14 = (TLRPC.KeyboardButton)var11.buttons.get(var9);
                  var16 = var4;
                  if (var14 instanceof TLRPC.TL_keyboardButtonCallback) {
                     var16 = var4;
                     if (var4 == null) {
                        var16 = new LinearLayout(this);
                        var16.setOrientation(0);
                        var16.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        var16.setWeightSum(100.0F);
                        var16.setTag("b");
                        var16.setOnTouchListener(_$$Lambda$PopupNotificationActivity$XvlaP2ODWCCStorSQi9nplxzY4s.INSTANCE);
                     }

                     TextView var15 = new TextView(this);
                     var15.setTextSize(1, 16.0F);
                     var15.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
                     var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                     var15.setText(var14.text.toUpperCase());
                     var15.setTag(var14);
                     var15.setGravity(17);
                     var15.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                     var16.addView(var15, LayoutHelper.createLinear(-1, -1, 100.0F / (float)var10));
                     var15.setOnClickListener(new _$$Lambda$PopupNotificationActivity$ox3mIPlvmBDmNDp_7DLxqyRSnLI(var8, var5));
                  }

                  ++var9;
               }
            }
         }

         if (var4 != null) {
            var9 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0F);
            android.widget.RelativeLayout.LayoutParams var17 = new android.widget.RelativeLayout.LayoutParams(-1, -2);
            var17.addRule(12);
            if (var2) {
               var1 = this.currentMessageNum;
               if (var3 == var1) {
                  var4.setTranslationX(0.0F);
               } else if (var3 == var1 - 1) {
                  var4.setTranslationX((float)(-var9));
               } else if (var3 == var1 + 1) {
                  var4.setTranslationX((float)var9);
               }
            }

            this.popupContainer.addView(var4, var17);
         }

         return var4;
      } else {
         return null;
      }
   }

   private void getNewMessage() {
      if (this.popupMessages.isEmpty()) {
         this.onFinish();
         this.finish();
      } else {
         boolean var4;
         label54: {
            if ((this.currentMessageNum != 0 || this.chatActivityEnterView.hasText() || this.startedMoving) && this.currentMessageObject != null) {
               int var1 = this.popupMessages.size();

               for(int var2 = 0; var2 < var1; ++var2) {
                  MessageObject var3 = (MessageObject)this.popupMessages.get(var2);
                  if (var3.currentAccount == this.currentMessageObject.currentAccount && var3.getDialogId() == this.currentMessageObject.getDialogId() && var3.getId() == this.currentMessageObject.getId()) {
                     this.currentMessageNum = var2;
                     var4 = true;
                     break label54;
                  }
               }
            }

            var4 = false;
         }

         if (!var4) {
            this.currentMessageNum = 0;
            this.currentMessageObject = (MessageObject)this.popupMessages.get(0);
            this.updateInterfaceForCurrentMessage(0);
         } else if (this.startedMoving) {
            if (this.currentMessageNum == this.popupMessages.size() - 1) {
               this.prepareLayouts(3);
            } else if (this.currentMessageNum == 1) {
               this.prepareLayouts(4);
            }
         }

         this.countText.setText(String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
      }
   }

   private ViewGroup getViewForMessage(int var1, boolean var2) {
      int var3 = var1;
      if (this.popupMessages.size() != 1 || var1 >= 0 && var1 < this.popupMessages.size()) {
         if (var1 == -1) {
            var1 = this.popupMessages.size() - 1;
         } else {
            var1 = var1;
            if (var3 == this.popupMessages.size()) {
               var1 = 0;
            }
         }

         MessageObject var4 = (MessageObject)this.popupMessages.get(var1);
         var3 = var4.type;
         Object var6;
         Object var16;
         TextView var24;
         if ((var3 == 1 || var3 == 4) && !var4.isSecretMedia()) {
            BackupImageView var25;
            if (this.imageViews.size() > 0) {
               var16 = (ViewGroup)this.imageViews.get(0);
               this.imageViews.remove(0);
            } else {
               var16 = new FrameLayout(this);
               FrameLayout var20 = new FrameLayout(this);
               var20.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F));
               var20.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               ((ViewGroup)var16).addView(var20, LayoutHelper.createFrame(-1, -1.0F));
               var25 = new BackupImageView(this);
               var25.setTag(311);
               var20.addView(var25, LayoutHelper.createFrame(-1, -1.0F));
               var24 = new TextView(this);
               var24.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               var24.setTextSize(1, 16.0F);
               var24.setGravity(17);
               var24.setTag(312);
               var20.addView(var24, LayoutHelper.createFrame(-1, -2, 17));
               ((ViewGroup)var16).setTag(2);
               ((ViewGroup)var16).setOnClickListener(new _$$Lambda$PopupNotificationActivity$yXN7dQz6jZF2SRmRmEwBYh62Ap0(this));
            }

            var6 = var16;
            TextView var8 = (TextView)((ViewGroup)var16).findViewWithTag(312);
            var25 = (BackupImageView)((ViewGroup)var16).findViewWithTag(311);
            var25.setAspectFit(true);
            var3 = var4.type;
            if (var3 == 1) {
               boolean var15;
               label99: {
                  label123: {
                     TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var4.photoThumbs, AndroidUtilities.getPhotoSize());
                     TLRPC.PhotoSize var21 = FileLoader.getClosestPhotoSizeWithSize(var4.photoThumbs, 100);
                     if (var9 != null) {
                        if (var4.type == 1 && !FileLoader.getPathToMessage(var4.messageOwner).exists()) {
                           var15 = false;
                        } else {
                           var15 = true;
                        }

                        if (!var4.needDrawBluredPreview()) {
                           if (var15 || DownloadController.getInstance(var4.currentAccount).canDownloadMedia(var4)) {
                              var25.setImage(ImageLocation.getForObject(var9, var4.photoThumbsObject), "100_100", ImageLocation.getForObject(var21, var4.photoThumbsObject), "100_100_b", var9.size, var4);
                              break label123;
                           }

                           if (var21 != null) {
                              var25.setImage(ImageLocation.getForObject(var21, var4.photoThumbsObject), "100_100_b", (String)null, (Drawable)null, var4);
                              break label123;
                           }
                        }
                     }

                     var15 = false;
                     break label99;
                  }

                  var15 = true;
               }

               if (!var15) {
                  var25.setVisibility(8);
                  var8.setVisibility(0);
                  var8.setTextSize(2, (float)SharedConfig.fontSize);
                  var8.setText(var4.messageText);
                  var16 = var6;
               } else {
                  var25.setVisibility(0);
                  var8.setVisibility(8);
                  var16 = var6;
               }
            } else {
               var16 = var16;
               if (var3 == 4) {
                  var8.setVisibility(8);
                  var8.setText(var4.messageText);
                  var25.setVisibility(0);
                  TLRPC.GeoPoint var26 = var4.messageOwner.media.geo;
                  double var10 = var26.lat;
                  double var12 = var26._long;
                  if (MessagesController.getInstance(var4.currentAccount).mapProvider == 2) {
                     var25.setImage(ImageLocation.getForWebFile(WebFile.createWithGeoPoint(var26, 100, 100, 15, Math.min(2, (int)Math.ceil((double)AndroidUtilities.density)))), (String)null, (String)null, (Drawable)null, var4);
                     var16 = var6;
                  } else {
                     var25.setImage(AndroidUtilities.formapMapUrl(var4.currentAccount, var10, var12, 100, 100, true, 15), (String)null, (Drawable)null);
                     var16 = var6;
                  }
               }
            }
         } else if (var4.type == 2) {
            PopupAudioView var7;
            if (this.audioViews.size() > 0) {
               var6 = (ViewGroup)this.audioViews.get(0);
               this.audioViews.remove(0);
               var7 = (PopupAudioView)((ViewGroup)var6).findViewWithTag(300);
            } else {
               var6 = new FrameLayout(this);
               FrameLayout var18 = new FrameLayout(this);
               var18.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F));
               var18.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               ((ViewGroup)var6).addView(var18, LayoutHelper.createFrame(-1, -1.0F));
               FrameLayout var5 = new FrameLayout(this);
               var18.addView(var5, LayoutHelper.createFrame(-1, -2.0F, 17, 20.0F, 0.0F, 20.0F, 0.0F));
               var7 = new PopupAudioView(this);
               var7.setTag(300);
               var5.addView(var7);
               ((ViewGroup)var6).setTag(3);
               ((ViewGroup)var6).setOnClickListener(new _$$Lambda$PopupNotificationActivity$VFWwjWrjLI64daw5erAQKADNXUs(this));
            }

            var7.setMessageObject(var4);
            var16 = var6;
            if (DownloadController.getInstance(var4.currentAccount).canDownloadMedia(var4)) {
               var7.downloadAudioIfNeed();
               var16 = var6;
            }
         } else {
            if (this.textViews.size() > 0) {
               var16 = (ViewGroup)this.textViews.get(0);
               this.textViews.remove(0);
            } else {
               var16 = new FrameLayout(this);
               ScrollView var22 = new ScrollView(this);
               var22.setFillViewport(true);
               ((ViewGroup)var16).addView(var22, LayoutHelper.createFrame(-1, -1.0F));
               LinearLayout var17 = new LinearLayout(this);
               var17.setOrientation(0);
               var17.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var22.addView(var17, LayoutHelper.createScroll(-1, -2, 1));
               var17.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F));
               var17.setOnClickListener(new _$$Lambda$PopupNotificationActivity$1_iHFPQDV_CYBmOOqbZFgFmuyU8(this));
               var24 = new TextView(this);
               var24.setTextSize(1, 16.0F);
               var24.setTag(301);
               var24.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               var24.setLinkTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               var24.setGravity(17);
               var17.addView(var24, LayoutHelper.createLinear(-1, -2, 17));
               ((ViewGroup)var16).setTag(1);
            }

            TextView var19 = (TextView)((ViewGroup)var16).findViewWithTag(301);
            var19.setTextSize(2, (float)SharedConfig.fontSize);
            var19.setText(var4.messageText);
         }

         if (((ViewGroup)var16).getParent() == null) {
            this.messageContainer.addView((View)var16);
         }

         ((ViewGroup)var16).setVisibility(0);
         if (var2) {
            var3 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0F);
            LayoutParams var23 = (LayoutParams)((ViewGroup)var16).getLayoutParams();
            var23.gravity = 51;
            var23.height = -1;
            var23.width = var3;
            int var14 = this.currentMessageNum;
            if (var1 == var14) {
               ((ViewGroup)var16).setTranslationX(0.0F);
            } else if (var1 == var14 - 1) {
               ((ViewGroup)var16).setTranslationX((float)(-var3));
            } else if (var1 == var14 + 1) {
               ((ViewGroup)var16).setTranslationX((float)var3);
            }

            ((ViewGroup)var16).setLayoutParams(var23);
            ((ViewGroup)var16).invalidate();
         }

         return (ViewGroup)var16;
      } else {
         return null;
      }
   }

   private void handleIntent(Intent var1) {
      boolean var2;
      if (var1 != null && var1.getBooleanExtra("force", false)) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.isReply = var2;
      this.popupMessages.clear();
      int var3;
      if (this.isReply) {
         if (var1 != null) {
            var3 = var1.getIntExtra("currentAccount", UserConfig.selectedAccount);
         } else {
            var3 = UserConfig.selectedAccount;
         }

         this.popupMessages.addAll(NotificationsController.getInstance(var3).popupReplyMessages);
      } else {
         for(var3 = 0; var3 < 3; ++var3) {
            if (UserConfig.getInstance(var3).isClientActivated()) {
               this.popupMessages.addAll(NotificationsController.getInstance(var3).popupMessages);
            }
         }
      }

      if (!((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode() && ApplicationLoader.isScreenOn) {
         this.getWindow().addFlags(2623488);
         this.getWindow().clearFlags(2);
      } else {
         this.getWindow().addFlags(2623490);
      }

      if (this.currentMessageObject == null) {
         this.currentMessageNum = 0;
      }

      this.getNewMessage();
   }

   // $FF: synthetic method
   static boolean lambda$getButtonsViewForMessage$4(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static void lambda$getButtonsViewForMessage$5(int var0, MessageObject var1, View var2) {
      TLRPC.KeyboardButton var3 = (TLRPC.KeyboardButton)var2.getTag();
      if (var3 != null) {
         SendMessagesHelper.getInstance(var0).sendNotificationCallback(var1.getDialogId(), var1.getId(), var3.data);
      }

   }

   private void openCurrentMessage() {
      if (this.currentMessageObject != null) {
         Intent var1 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
         long var2 = this.currentMessageObject.getDialogId();
         int var4 = (int)var2;
         if (var4 != 0) {
            if (var4 < 0) {
               var1.putExtra("chatId", -var4);
            } else {
               var1.putExtra("userId", var4);
            }
         } else {
            var1.putExtra("encId", (int)(var2 >> 32));
         }

         var1.putExtra("currentAccount", this.currentMessageObject.currentAccount);
         StringBuilder var5 = new StringBuilder();
         var5.append("com.tmessages.openchat");
         var5.append(Math.random());
         var5.append(Integer.MAX_VALUE);
         var1.setAction(var5.toString());
         var1.setFlags(32768);
         this.startActivity(var1);
         this.onFinish();
         this.finish();
      }
   }

   private void prepareLayouts(int var1) {
      int var2 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0F);
      if (var1 == 0) {
         this.reuseView(this.centerView);
         this.reuseView(this.leftView);
         this.reuseView(this.rightView);
         this.reuseButtonsView(this.centerButtonsView);
         this.reuseButtonsView(this.leftButtonsView);
         this.reuseButtonsView(this.rightButtonsView);
         var1 = this.currentMessageNum - 1;

         while(true) {
            var2 = this.currentMessageNum;
            if (var1 >= var2 + 2) {
               break;
            }

            if (var1 == var2 - 1) {
               this.leftView = this.getViewForMessage(var1, true);
               this.leftButtonsView = this.getButtonsViewForMessage(var1, true);
            } else if (var1 == var2) {
               this.centerView = this.getViewForMessage(var1, true);
               this.centerButtonsView = this.getButtonsViewForMessage(var1, true);
            } else if (var1 == var2 + 1) {
               this.rightView = this.getViewForMessage(var1, true);
               this.rightButtonsView = this.getButtonsViewForMessage(var1, true);
            }

            ++var1;
         }
      } else if (var1 == 1) {
         this.reuseView(this.rightView);
         this.reuseButtonsView(this.rightButtonsView);
         this.rightView = this.centerView;
         this.centerView = this.leftView;
         this.leftView = this.getViewForMessage(this.currentMessageNum - 1, true);
         this.rightButtonsView = this.centerButtonsView;
         this.centerButtonsView = this.leftButtonsView;
         this.leftButtonsView = this.getButtonsViewForMessage(this.currentMessageNum - 1, true);
      } else if (var1 == 2) {
         this.reuseView(this.leftView);
         this.reuseButtonsView(this.leftButtonsView);
         this.leftView = this.centerView;
         this.centerView = this.rightView;
         this.rightView = this.getViewForMessage(this.currentMessageNum + 1, true);
         this.leftButtonsView = this.centerButtonsView;
         this.centerButtonsView = this.rightButtonsView;
         this.rightButtonsView = this.getButtonsViewForMessage(this.currentMessageNum + 1, true);
      } else {
         ViewGroup var3;
         float var4;
         LayoutParams var5;
         LinearLayout var6;
         if (var1 == 3) {
            var3 = this.rightView;
            if (var3 != null) {
               var4 = var3.getTranslationX();
               this.reuseView(this.rightView);
               var3 = this.getViewForMessage(this.currentMessageNum + 1, false);
               this.rightView = var3;
               if (var3 != null) {
                  var5 = (LayoutParams)this.rightView.getLayoutParams();
                  var5.width = var2;
                  this.rightView.setLayoutParams(var5);
                  this.rightView.setTranslationX(var4);
                  this.rightView.invalidate();
               }
            }

            var3 = this.rightButtonsView;
            if (var3 != null) {
               var4 = var3.getTranslationX();
               this.reuseButtonsView(this.rightButtonsView);
               var6 = this.getButtonsViewForMessage(this.currentMessageNum + 1, false);
               this.rightButtonsView = var6;
               if (var6 != null) {
                  this.rightButtonsView.setTranslationX(var4);
               }
            }
         } else if (var1 == 4) {
            var3 = this.leftView;
            if (var3 != null) {
               var4 = var3.getTranslationX();
               this.reuseView(this.leftView);
               var3 = this.getViewForMessage(0, false);
               this.leftView = var3;
               if (var3 != null) {
                  var5 = (LayoutParams)this.leftView.getLayoutParams();
                  var5.width = var2;
                  this.leftView.setLayoutParams(var5);
                  this.leftView.setTranslationX(var4);
                  this.leftView.invalidate();
               }
            }

            var3 = this.leftButtonsView;
            if (var3 != null) {
               var4 = var3.getTranslationX();
               this.reuseButtonsView(this.leftButtonsView);
               var6 = this.getButtonsViewForMessage(0, false);
               this.leftButtonsView = var6;
               if (var6 != null) {
                  this.leftButtonsView.setTranslationX(var4);
               }
            }
         }
      }

   }

   private void reuseButtonsView(ViewGroup var1) {
      if (var1 != null) {
         this.popupContainer.removeView(var1);
      }
   }

   private void reuseView(ViewGroup var1) {
      if (var1 != null) {
         int var2 = (Integer)var1.getTag();
         var1.setVisibility(8);
         if (var2 == 1) {
            this.textViews.add(var1);
         } else if (var2 == 2) {
            this.imageViews.add(var1);
         } else if (var2 == 3) {
            this.audioViews.add(var1);
         }

      }
   }

   private void setTypingAnimation(boolean var1) {
      if (this.actionBar != null) {
         byte var2 = 0;
         int var3 = 0;
         if (var1) {
            Exception var10000;
            label47: {
               boolean var10001;
               Integer var4;
               try {
                  var4 = (Integer)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStringsTypes.get(this.currentMessageObject.getDialogId());
                  this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds(this.statusDrawables[var4], (Drawable)null, (Drawable)null, (Drawable)null);
                  this.onlineTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0F));
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label47;
               }

               while(true) {
                  label42: {
                     try {
                        if (var3 >= this.statusDrawables.length) {
                           return;
                        }

                        if (var3 == var4) {
                           this.statusDrawables[var3].start();
                           break label42;
                        }
                     } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                        break;
                     }

                     try {
                        this.statusDrawables[var3].stop();
                     } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                        break;
                     }
                  }

                  ++var3;
               }
            }

            Exception var8 = var10000;
            FileLog.e((Throwable)var8);
         } else {
            this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
            this.onlineTextView.setCompoundDrawablePadding(0);
            var3 = var2;

            while(true) {
               StatusDrawable[] var9 = this.statusDrawables;
               if (var3 >= var9.length) {
                  break;
               }

               var9[var3].stop();
               ++var3;
            }
         }

      }
   }

   private void switchToNextMessage() {
      if (this.popupMessages.size() > 1) {
         if (this.currentMessageNum < this.popupMessages.size() - 1) {
            ++this.currentMessageNum;
         } else {
            this.currentMessageNum = 0;
         }

         this.currentMessageObject = (MessageObject)this.popupMessages.get(this.currentMessageNum);
         this.updateInterfaceForCurrentMessage(2);
         this.countText.setText(String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
      }

   }

   private void switchToPreviousMessage() {
      if (this.popupMessages.size() > 1) {
         int var1 = this.currentMessageNum;
         if (var1 > 0) {
            this.currentMessageNum = var1 - 1;
         } else {
            this.currentMessageNum = this.popupMessages.size() - 1;
         }

         this.currentMessageObject = (MessageObject)this.popupMessages.get(this.currentMessageNum);
         this.updateInterfaceForCurrentMessage(1);
         this.countText.setText(String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
      }

   }

   private void updateInterfaceForCurrentMessage(int var1) {
      if (this.actionBar != null) {
         int var2 = this.lastResumedAccount;
         if (var2 != this.currentMessageObject.currentAccount) {
            if (var2 >= 0) {
               ConnectionsManager.getInstance(var2).setAppPaused(true, false);
            }

            this.lastResumedAccount = this.currentMessageObject.currentAccount;
            ConnectionsManager.getInstance(this.lastResumedAccount).setAppPaused(false, false);
         }

         this.currentChat = null;
         this.currentUser = null;
         long var3 = this.currentMessageObject.getDialogId();
         this.chatActivityEnterView.setDialogId(var3, this.currentMessageObject.currentAccount);
         var2 = (int)var3;
         if (var2 != 0) {
            if (var2 > 0) {
               this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(var2);
            } else {
               this.currentChat = MessagesController.getInstance(this.currentMessageObject.currentAccount).getChat(-var2);
               this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(this.currentMessageObject.messageOwner.from_id);
            }
         } else {
            TLRPC.EncryptedChat var5 = MessagesController.getInstance(this.currentMessageObject.currentAccount).getEncryptedChat((int)(var3 >> 32));
            this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(var5.user_id);
         }

         TLRPC.Chat var6 = this.currentChat;
         if (var6 != null && this.currentUser != null) {
            this.nameTextView.setText(var6.title);
            this.onlineTextView.setText(UserObject.getUserName(this.currentUser));
            this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.nameTextView.setCompoundDrawablePadding(0);
         } else {
            TLRPC.User var7 = this.currentUser;
            if (var7 != null) {
               this.nameTextView.setText(UserObject.getUserName(var7));
               if (var2 == 0) {
                  this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(2131165448, 0, 0, 0);
                  this.nameTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0F));
               } else {
                  this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                  this.nameTextView.setCompoundDrawablePadding(0);
               }
            }
         }

         this.prepareLayouts(var1);
         this.updateSubtitle();
         this.checkAndUpdateAvatar();
         this.applyViewsLayoutParams(0);
      }
   }

   private void updateSubtitle() {
      if (this.actionBar != null) {
         MessageObject var1 = this.currentMessageObject;
         if (var1 != null && this.currentChat == null) {
            TLRPC.User var2 = this.currentUser;
            if (var2 != null) {
               int var3 = var2.id;
               if (var3 / 1000 != 777 && var3 / 1000 != 333 && ContactsController.getInstance(var1.currentAccount).contactsDict.get(this.currentUser.id) == null && (ContactsController.getInstance(this.currentMessageObject.currentAccount).contactsDict.size() != 0 || !ContactsController.getInstance(this.currentMessageObject.currentAccount).isLoadingContacts())) {
                  String var6 = this.currentUser.phone;
                  if (var6 != null && var6.length() != 0) {
                     TextView var7 = this.nameTextView;
                     PhoneFormat var5 = PhoneFormat.getInstance();
                     StringBuilder var4 = new StringBuilder();
                     var4.append("+");
                     var4.append(this.currentUser.phone);
                     var7.setText(var5.format(var4.toString()));
                  } else {
                     this.nameTextView.setText(UserObject.getUserName(this.currentUser));
                  }
               } else {
                  this.nameTextView.setText(UserObject.getUserName(this.currentUser));
               }

               var2 = this.currentUser;
               if (var2 != null && var2.id == 777000) {
                  this.onlineTextView.setText(LocaleController.getString("ServiceNotifications", 2131560724));
               } else {
                  CharSequence var8 = (CharSequence)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
                  if (var8 != null && var8.length() != 0) {
                     this.lastPrintString = var8;
                     this.onlineTextView.setText(var8);
                     this.setTypingAnimation(true);
                  } else {
                     this.lastPrintString = null;
                     this.setTypingAnimation(false);
                     var2 = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(this.currentUser.id);
                     if (var2 != null) {
                        this.currentUser = var2;
                     }

                     this.onlineTextView.setText(LocaleController.formatUserStatus(this.currentMessageObject.currentAccount, this.currentUser));
                  }
               }
            }
         }
      }

   }

   public boolean checkTransitionAnimation() {
      if (this.animationInProgress && this.animationStartTime < System.currentTimeMillis() - 400L) {
         this.animationInProgress = false;
         Runnable var1 = this.onAnimationEndRunnable;
         if (var1 != null) {
            var1.run();
            this.onAnimationEndRunnable = null;
         }
      }

      return this.animationInProgress;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.appDidLogout) {
         if (var2 == this.lastResumedAccount) {
            this.onFinish();
            this.finish();
         }
      } else {
         int var4 = NotificationCenter.pushMessagesUpdated;
         byte var5 = 0;
         byte var6 = 0;
         byte var7 = 0;
         byte var8 = 0;
         if (var1 == var4) {
            if (!this.isReply) {
               this.popupMessages.clear();

               for(var1 = var8; var1 < 3; ++var1) {
                  if (UserConfig.getInstance(var1).isClientActivated()) {
                     this.popupMessages.addAll(NotificationsController.getInstance(var1).popupMessages);
                  }
               }

               this.getNewMessage();
            }
         } else if (var1 == NotificationCenter.updateInterfaces) {
            if (this.currentMessageObject == null || var2 != this.lastResumedAccount) {
               return;
            }

            var1 = (Integer)var3[0];
            if ((var1 & 1) != 0 || (var1 & 4) != 0 || (var1 & 16) != 0 || (var1 & 32) != 0) {
               this.updateSubtitle();
            }

            if ((var1 & 2) != 0 || (var1 & 8) != 0) {
               this.checkAndUpdateAvatar();
            }

            if ((var1 & 64) != 0) {
               CharSequence var9 = (CharSequence)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
               if ((this.lastPrintString == null || var9 != null) && (this.lastPrintString != null || var9 == null)) {
                  CharSequence var11 = this.lastPrintString;
                  if (var11 == null || var9 == null || var11.equals(var9)) {
                     return;
                  }
               }

               this.updateSubtitle();
            }
         } else {
            Integer var12;
            ViewGroup var18;
            View var19;
            if (var1 == NotificationCenter.messagePlayingDidReset) {
               var12 = (Integer)var3[0];
               var18 = this.messageContainer;
               if (var18 != null) {
                  int var17 = var18.getChildCount();

                  for(var1 = var5; var1 < var17; ++var1) {
                     var19 = this.messageContainer.getChildAt(var1);
                     if ((Integer)var19.getTag() == 3) {
                        PopupAudioView var10 = (PopupAudioView)var19.findViewWithTag(300);
                        MessageObject var20 = var10.getMessageObject();
                        if (var20 != null && var20.currentAccount == var2 && var20.getId() == var12) {
                           var10.updateButtonState();
                           break;
                        }
                     }
                  }
               }
            } else if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
               var12 = (Integer)var3[0];
               var18 = this.messageContainer;
               if (var18 != null) {
                  int var16 = var18.getChildCount();

                  for(var1 = var6; var1 < var16; ++var1) {
                     var19 = this.messageContainer.getChildAt(var1);
                     if ((Integer)var19.getTag() == 3) {
                        PopupAudioView var22 = (PopupAudioView)var19.findViewWithTag(300);
                        MessageObject var21 = var22.getMessageObject();
                        if (var21 != null && var21.currentAccount == var2 && var21.getId() == var12) {
                           var22.updateProgress();
                           break;
                        }
                     }
                  }
               }
            } else if (var1 == NotificationCenter.emojiDidLoad) {
               ViewGroup var13 = this.messageContainer;
               if (var13 != null) {
                  var2 = var13.getChildCount();

                  for(var1 = var7; var1 < var2; ++var1) {
                     View var14 = this.messageContainer.getChildAt(var1);
                     if ((Integer)var14.getTag() == 1) {
                        TextView var15 = (TextView)var14.findViewWithTag(301);
                        if (var15 != null) {
                           var15.invalidate();
                        }
                     }
                  }
               }
            } else if (var1 == NotificationCenter.contactsDidLoad && var2 == this.lastResumedAccount) {
               this.updateSubtitle();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$getViewForMessage$6$PopupNotificationActivity(View var1) {
      this.openCurrentMessage();
   }

   // $FF: synthetic method
   public void lambda$getViewForMessage$7$PopupNotificationActivity(View var1) {
      this.openCurrentMessage();
   }

   // $FF: synthetic method
   public void lambda$getViewForMessage$8$PopupNotificationActivity(View var1) {
      this.openCurrentMessage();
   }

   // $FF: synthetic method
   public void lambda$onRequestPermissionsResult$0$PopupNotificationActivity(DialogInterface var1, int var2) {
      try {
         Intent var3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
         StringBuilder var5 = new StringBuilder();
         var5.append("package:");
         var5.append(ApplicationLoader.applicationContext.getPackageName());
         var3.setData(Uri.parse(var5.toString()));
         this.startActivity(var3);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$onTouchEventMy$1$PopupNotificationActivity() {
      this.animationInProgress = false;
      this.switchToPreviousMessage();
      AndroidUtilities.unlockOrientation(this);
   }

   // $FF: synthetic method
   public void lambda$onTouchEventMy$2$PopupNotificationActivity() {
      this.animationInProgress = false;
      this.switchToNextMessage();
      AndroidUtilities.unlockOrientation(this);
   }

   // $FF: synthetic method
   public void lambda$onTouchEventMy$3$PopupNotificationActivity() {
      this.animationInProgress = false;
      this.applyViewsLayoutParams(0);
      AndroidUtilities.unlockOrientation(this);
   }

   public void onBackPressed() {
      if (this.chatActivityEnterView.isPopupShowing()) {
         this.chatActivityEnterView.hidePopup(true);
      } else {
         super.onBackPressed();
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      AndroidUtilities.checkDisplaySize(this, var1);
      this.fixLayout();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Theme.createChatResources(this, false);
      int var2 = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (var2 > 0) {
         AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(var2);
      }

      for(var2 = 0; var2 < 3; ++var2) {
         NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.appDidLogout);
         NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.updateInterfaces);
         NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
         NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.messagePlayingDidReset);
         NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.contactsDidLoad);
      }

      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pushMessagesUpdated);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.classGuid = ConnectionsManager.generateClassGuid();
      this.statusDrawables[0] = new TypingDotsDrawable();
      this.statusDrawables[1] = new RecordStatusDrawable();
      this.statusDrawables[2] = new SendingFileDrawable();
      this.statusDrawables[3] = new PlayingGameDrawable();
      this.statusDrawables[4] = new RoundStatusDrawable();
      SizeNotifierFrameLayout var4 = new SizeNotifierFrameLayout(this) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.getChildCount();
            int var7 = this.getKeyboardHeight();
            int var8 = AndroidUtilities.dp(20.0F);
            int var9 = 0;
            int var10;
            if (var7 <= var8) {
               var10 = PopupNotificationActivity.this.chatActivityEnterView.getEmojiPadding();
            } else {
               var10 = 0;
            }

            for(; var9 < var6; ++var9) {
               View var11 = this.getChildAt(var9);
               if (var11.getVisibility() != 8) {
                  LayoutParams var12 = (LayoutParams)var11.getLayoutParams();
                  int var13 = var11.getMeasuredWidth();
                  int var14 = var11.getMeasuredHeight();
                  var8 = var12.gravity;
                  var7 = var8;
                  if (var8 == -1) {
                     var7 = 51;
                  }

                  int var15;
                  label59: {
                     var15 = var7 & 112;
                     var7 = var7 & 7 & 7;
                     if (var7 != 1) {
                        if (var7 != 5) {
                           var8 = var12.leftMargin;
                           break label59;
                        }

                        var8 = var4 - var13;
                        var7 = var12.rightMargin;
                     } else {
                        var8 = (var4 - var2 - var13) / 2 + var12.leftMargin;
                        var7 = var12.rightMargin;
                     }

                     var8 -= var7;
                  }

                  label53: {
                     if (var15 != 16) {
                        if (var15 == 48) {
                           var7 = var12.topMargin;
                           break label53;
                        }

                        if (var15 != 80) {
                           var7 = var12.topMargin;
                           break label53;
                        }

                        var15 = var5 - var10 - var3 - var14;
                        var7 = var12.bottomMargin;
                     } else {
                        var15 = (var5 - var10 - var3 - var14) / 2 + var12.topMargin;
                        var7 = var12.bottomMargin;
                     }

                     var7 = var15 - var7;
                  }

                  if (PopupNotificationActivity.this.chatActivityEnterView.isPopupView(var11)) {
                     if (var10 != 0) {
                        var7 = this.getMeasuredHeight() - var10;
                     } else {
                        var7 = this.getMeasuredHeight();
                     }
                  } else if (PopupNotificationActivity.this.chatActivityEnterView.isRecordCircle(var11)) {
                     var7 = PopupNotificationActivity.this.popupContainer.getTop() + PopupNotificationActivity.this.popupContainer.getMeasuredHeight() - var11.getMeasuredHeight() - var12.bottomMargin;
                     var8 = PopupNotificationActivity.this.popupContainer.getLeft() + PopupNotificationActivity.this.popupContainer.getMeasuredWidth() - var11.getMeasuredWidth() - var12.rightMargin;
                  }

                  var11.layout(var8, var7, var13 + var8, var14 + var7);
               }
            }

            this.notifyHeightChanged();
         }

         protected void onMeasure(int var1, int var2) {
            MeasureSpec.getMode(var1);
            MeasureSpec.getMode(var2);
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            int var5 = var4;
            if (this.getKeyboardHeight() <= AndroidUtilities.dp(20.0F)) {
               var5 = var4 - PopupNotificationActivity.this.chatActivityEnterView.getEmojiPadding();
            }

            int var6 = this.getChildCount();

            for(var4 = 0; var4 < var6; ++var4) {
               View var7 = this.getChildAt(var4);
               if (var7.getVisibility() != 8) {
                  if (PopupNotificationActivity.this.chatActivityEnterView.isPopupView(var7)) {
                     var7.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var7.getLayoutParams().height, 1073741824));
                  } else if (PopupNotificationActivity.this.chatActivityEnterView.isRecordCircle(var7)) {
                     this.measureChildWithMargins(var7, var1, 0, var2, 0);
                  } else {
                     var7.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(2.0F) + var5), 1073741824));
                  }
               }
            }

         }
      };
      this.setContentView(var4);
      var4.setBackgroundColor(-1728053248);
      RelativeLayout var3 = new RelativeLayout(this);
      var4.addView(var3, LayoutHelper.createFrame(-1, -1.0F));
      this.popupContainer = new RelativeLayout(this) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);

            for(var2 = 0; var2 < this.getChildCount(); ++var2) {
               View var6 = this.getChildAt(var2);
               if (var6.getTag() instanceof String) {
                  var6.layout(var6.getLeft(), PopupNotificationActivity.this.chatActivityEnterView.getTop() + AndroidUtilities.dp(3.0F), var6.getRight(), PopupNotificationActivity.this.chatActivityEnterView.getBottom());
               }
            }

         }

         protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            int var3 = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredWidth();
            var2 = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredHeight();

            for(var1 = 0; var1 < this.getChildCount(); ++var1) {
               View var4 = this.getChildAt(var1);
               if (var4.getTag() instanceof String) {
                  var4.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(3.0F), 1073741824));
               }
            }

         }
      };
      this.popupContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      var3.addView(this.popupContainer, LayoutHelper.createRelative(-1, 240, 12, 0, 12, 0, 13));
      ChatActivityEnterView var8 = this.chatActivityEnterView;
      if (var8 != null) {
         var8.onDestroy();
      }

      this.chatActivityEnterView = new ChatActivityEnterView(this, var4, (ChatActivity)null, false);
      this.chatActivityEnterView.setId(1000);
      this.popupContainer.addView(this.chatActivityEnterView, LayoutHelper.createRelative(-1, -2, 12));
      this.chatActivityEnterView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
         public void didPressedAttachButton() {
         }

         public void needChangeVideoPreviewState(int var1, float var2) {
         }

         public void needSendTyping() {
            if (PopupNotificationActivity.this.currentMessageObject != null) {
               MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).sendTyping(PopupNotificationActivity.this.currentMessageObject.getDialogId(), 0, PopupNotificationActivity.this.classGuid);
            }

         }

         public void needShowMediaBanHint() {
         }

         public void needStartRecordAudio(int var1) {
         }

         public void needStartRecordVideo(int var1) {
         }

         public void onAttachButtonHidden() {
         }

         public void onAttachButtonShow() {
         }

         public void onMessageEditEnd(boolean var1) {
         }

         public void onMessageSend(CharSequence var1) {
            if (PopupNotificationActivity.this.currentMessageObject != null) {
               if (PopupNotificationActivity.this.currentMessageNum >= 0 && PopupNotificationActivity.this.currentMessageNum < PopupNotificationActivity.this.popupMessages.size()) {
                  PopupNotificationActivity.this.popupMessages.remove(PopupNotificationActivity.this.currentMessageNum);
               }

               MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).markDialogAsRead(PopupNotificationActivity.this.currentMessageObject.getDialogId(), PopupNotificationActivity.this.currentMessageObject.getId(), Math.max(0, PopupNotificationActivity.this.currentMessageObject.getId()), PopupNotificationActivity.this.currentMessageObject.messageOwner.date, true, 0, true);
               PopupNotificationActivity.this.currentMessageObject = null;
               PopupNotificationActivity.this.getNewMessage();
            }
         }

         public void onPreAudioVideoRecord() {
         }

         public void onStickersExpandedChange() {
         }

         public void onStickersTab(boolean var1) {
         }

         public void onSwitchRecordMode(boolean var1) {
         }

         public void onTextChanged(CharSequence var1, boolean var2) {
         }

         public void onTextSelectionChanged(int var1, int var2) {
         }

         public void onTextSpansChanged(CharSequence var1) {
         }

         public void onWindowSizeChanged(int var1) {
         }
      });
      this.messageContainer = new PopupNotificationActivity.FrameLayoutTouch(this);
      this.popupContainer.addView(this.messageContainer, 0);
      this.actionBar = new ActionBar(this);
      this.actionBar.setOccupyStatusBar(false);
      this.actionBar.setBackButtonImage(2131165437);
      this.actionBar.setBackgroundColor(Theme.getColor("actionBarDefault"));
      this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), false);
      this.popupContainer.addView(this.actionBar);
      android.view.ViewGroup.LayoutParams var5 = this.actionBar.getLayoutParams();
      var5.width = -1;
      this.actionBar.setLayoutParams(var5);
      ActionBarMenuItem var6 = this.actionBar.createMenu().addItemWithWidth(2, 0, AndroidUtilities.dp(56.0F));
      this.countText = new TextView(this);
      this.countText.setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
      this.countText.setTextSize(1, 14.0F);
      this.countText.setGravity(17);
      var6.addView(this.countText, LayoutHelper.createFrame(56, -1.0F));
      this.avatarContainer = new FrameLayout(this);
      this.avatarContainer.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
      this.actionBar.addView(this.avatarContainer);
      LayoutParams var7 = (LayoutParams)this.avatarContainer.getLayoutParams();
      var7.height = -1;
      var7.width = -2;
      var7.rightMargin = AndroidUtilities.dp(48.0F);
      var7.leftMargin = AndroidUtilities.dp(60.0F);
      var7.gravity = 51;
      this.avatarContainer.setLayoutParams(var7);
      this.avatarImageView = new BackupImageView(this);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.avatarContainer.addView(this.avatarImageView);
      var7 = (LayoutParams)this.avatarImageView.getLayoutParams();
      var7.width = AndroidUtilities.dp(42.0F);
      var7.height = AndroidUtilities.dp(42.0F);
      var7.topMargin = AndroidUtilities.dp(3.0F);
      this.avatarImageView.setLayoutParams(var7);
      this.nameTextView = new TextView(this);
      this.nameTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
      this.nameTextView.setTextSize(1, 18.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.nameTextView.setGravity(3);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.avatarContainer.addView(this.nameTextView);
      var7 = (LayoutParams)this.nameTextView.getLayoutParams();
      var7.width = -2;
      var7.height = -2;
      var7.leftMargin = AndroidUtilities.dp(54.0F);
      var7.bottomMargin = AndroidUtilities.dp(22.0F);
      var7.gravity = 80;
      this.nameTextView.setLayoutParams(var7);
      this.onlineTextView = new TextView(this);
      this.onlineTextView.setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
      this.onlineTextView.setTextSize(1, 14.0F);
      this.onlineTextView.setLines(1);
      this.onlineTextView.setMaxLines(1);
      this.onlineTextView.setSingleLine(true);
      this.onlineTextView.setEllipsize(TruncateAt.END);
      this.onlineTextView.setGravity(3);
      this.avatarContainer.addView(this.onlineTextView);
      var7 = (LayoutParams)this.onlineTextView.getLayoutParams();
      var7.width = -2;
      var7.height = -2;
      var7.leftMargin = AndroidUtilities.dp(54.0F);
      var7.bottomMargin = AndroidUtilities.dp(4.0F);
      var7.gravity = 80;
      this.onlineTextView.setLayoutParams(var7);
      this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PopupNotificationActivity.this.onFinish();
               PopupNotificationActivity.this.finish();
            } else if (var1 == 1) {
               PopupNotificationActivity.this.openCurrentMessage();
            } else if (var1 == 2) {
               PopupNotificationActivity.this.switchToNextMessage();
            }

         }
      });
      this.wakeLock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(268435462, "screen");
      this.wakeLock.setReferenceCounted(false);
      this.handleIntent(this.getIntent());
   }

   protected void onDestroy() {
      super.onDestroy();
      this.onFinish();
      MediaController.getInstance().setFeedbackView(this.chatActivityEnterView, false);
      if (this.wakeLock.isHeld()) {
         this.wakeLock.release();
      }

      BackupImageView var1 = this.avatarImageView;
      if (var1 != null) {
         var1.setImageDrawable((Drawable)null);
      }

   }

   protected void onFinish() {
      if (!this.finished) {
         this.finished = true;
         if (this.isReply) {
            this.popupMessages.clear();
         }

         for(int var1 = 0; var1 < 3; ++var1) {
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.contactsDidLoad);
         }

         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pushMessagesUpdated);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
         ChatActivityEnterView var2 = this.chatActivityEnterView;
         if (var2 != null) {
            var2.onDestroy();
         }

         if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
         }

      }
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.handleIntent(var1);
   }

   protected void onPause() {
      super.onPause();
      this.overridePendingTransition(0, 0);
      ChatActivityEnterView var1 = this.chatActivityEnterView;
      if (var1 != null) {
         var1.hidePopup(false);
         this.chatActivityEnterView.setFieldFocused(false);
      }

      int var2 = this.lastResumedAccount;
      if (var2 >= 0) {
         ConnectionsManager.getInstance(var2).setAppPaused(true, false);
      }

   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      if (var1 == 3) {
         if (var3[0] == 0) {
            return;
         }

         AlertDialog.Builder var4 = new AlertDialog.Builder(this);
         var4.setTitle(LocaleController.getString("AppName", 2131558635));
         var4.setMessage(LocaleController.getString("PermissionNoAudio", 2131560414));
         var4.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$PopupNotificationActivity$4j1X9I2molTl8UnVVYY3k_eiVzk(this));
         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var4.show();
      }

   }

   protected void onResume() {
      super.onResume();
      MediaController.getInstance().setFeedbackView(this.chatActivityEnterView, true);
      ChatActivityEnterView var1 = this.chatActivityEnterView;
      if (var1 != null) {
         var1.setFieldFocused(true);
      }

      this.fixLayout();
      this.checkAndUpdateAvatar();
      this.wakeLock.acquire(7000L);
   }

   public boolean onTouchEventMy(MotionEvent var1) {
      boolean var2 = this.checkTransitionAnimation();
      byte var3 = 0;
      if (var2) {
         return false;
      } else {
         if (var1 != null && var1.getAction() == 0) {
            this.moveStartX = var1.getX();
         } else {
            float var5;
            int var6;
            int var15;
            if (var1 != null && var1.getAction() == 2) {
               float var4 = var1.getX();
               var5 = this.moveStartX;
               var6 = (int)(var4 - var5);
               var15 = var6;
               VelocityTracker var16;
               if (var5 != -1.0F) {
                  var15 = var6;
                  if (!this.startedMoving) {
                     var15 = var6;
                     if (Math.abs(var6) > AndroidUtilities.dp(10.0F)) {
                        this.startedMoving = true;
                        this.moveStartX = var4;
                        AndroidUtilities.lockOrientation(this);
                        var16 = this.velocityTracker;
                        if (var16 == null) {
                           this.velocityTracker = VelocityTracker.obtain();
                        } else {
                           var16.clear();
                        }

                        var15 = 0;
                     }
                  }
               }

               if (this.startedMoving) {
                  var6 = var15;
                  if (this.leftView == null) {
                     var6 = var15;
                     if (var15 > 0) {
                        var6 = 0;
                     }
                  }

                  if (this.rightView == null && var6 < 0) {
                     var6 = var3;
                  }

                  var16 = this.velocityTracker;
                  if (var16 != null) {
                     var16.addMovement(var1);
                  }

                  this.applyViewsLayoutParams(var6);
               }
            } else if (var1 == null || var1.getAction() == 1 || var1.getAction() == 3) {
               VelocityTracker var11;
               if (var1 != null && this.startedMoving) {
                  byte var7;
                  int var13;
                  label120: {
                     var6 = (int)(var1.getX() - this.moveStartX);
                     var13 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0F);
                     var11 = this.velocityTracker;
                     if (var11 != null) {
                        var11.computeCurrentVelocity(1000);
                        if (this.velocityTracker.getXVelocity() >= 3500.0F) {
                           var7 = 1;
                           break label120;
                        }

                        if (this.velocityTracker.getXVelocity() <= -3500.0F) {
                           var7 = 2;
                           break label120;
                        }
                     }

                     var7 = 0;
                  }

                  ViewGroup var8;
                  ViewGroup var12;
                  if ((var7 == 1 || var6 > var13 / 3) && this.leftView != null) {
                     var5 = (float)var13 - this.centerView.getTranslationX();
                     var12 = this.leftView;
                     var8 = this.leftButtonsView;
                     this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$EUnPo4xnwynM9tMiGtirwuisKAk(this);
                  } else if ((var7 == 2 || var6 < -var13 / 3) && this.rightView != null) {
                     var5 = (float)(-var13) - this.centerView.getTranslationX();
                     var12 = this.rightView;
                     var8 = this.rightButtonsView;
                     this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$w3pAWV1vrsUpHtwKKGENN1Nl00M(this);
                  } else if (this.centerView.getTranslationX() != 0.0F) {
                     var5 = -this.centerView.getTranslationX();
                     if (var6 > 0) {
                        var12 = this.leftView;
                     } else {
                        var12 = this.rightView;
                     }

                     if (var6 > 0) {
                        var8 = this.leftButtonsView;
                     } else {
                        var8 = this.rightButtonsView;
                     }

                     this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$EAJL169S6xhlVfXmCKr6JNKWWPk(this);
                  } else {
                     var12 = null;
                     var8 = var12;
                     var5 = 0.0F;
                  }

                  if (var5 != 0.0F) {
                     var15 = (int)(Math.abs(var5 / (float)var13) * 200.0F);
                     ArrayList var9 = new ArrayList();
                     ViewGroup var10 = this.centerView;
                     var9.add(ObjectAnimator.ofFloat(var10, "translationX", new float[]{var10.getTranslationX() + var5}));
                     var10 = this.centerButtonsView;
                     if (var10 != null) {
                        var9.add(ObjectAnimator.ofFloat(var10, "translationX", new float[]{var10.getTranslationX() + var5}));
                     }

                     if (var12 != null) {
                        var9.add(ObjectAnimator.ofFloat(var12, "translationX", new float[]{var12.getTranslationX() + var5}));
                     }

                     if (var8 != null) {
                        var9.add(ObjectAnimator.ofFloat(var8, "translationX", new float[]{var8.getTranslationX() + var5}));
                     }

                     AnimatorSet var14 = new AnimatorSet();
                     var14.playTogether(var9);
                     var14.setDuration((long)var15);
                     var14.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator var1) {
                           if (PopupNotificationActivity.this.onAnimationEndRunnable != null) {
                              PopupNotificationActivity.this.onAnimationEndRunnable.run();
                              PopupNotificationActivity.this.onAnimationEndRunnable = null;
                           }

                        }
                     });
                     var14.start();
                     this.animationInProgress = true;
                     this.animationStartTime = System.currentTimeMillis();
                  }
               } else {
                  this.applyViewsLayoutParams(0);
               }

               var11 = this.velocityTracker;
               if (var11 != null) {
                  var11.recycle();
                  this.velocityTracker = null;
               }

               this.startedMoving = false;
               this.moveStartX = -1.0F;
            }
         }

         return this.startedMoving;
      }
   }

   private class FrameLayoutTouch extends FrameLayout {
      public FrameLayoutTouch(Context var2) {
         super(var2);
      }

      public FrameLayoutTouch(Context var2, AttributeSet var3) {
         super(var2, var3);
      }

      public FrameLayoutTouch(Context var2, AttributeSet var3, int var4) {
         super(var2, var3, var4);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!PopupNotificationActivity.this.checkTransitionAnimation() && !((PopupNotificationActivity)this.getContext()).onTouchEventMy(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         boolean var2;
         if (!PopupNotificationActivity.this.checkTransitionAnimation() && !((PopupNotificationActivity)this.getContext()).onTouchEventMy(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         ((PopupNotificationActivity)this.getContext()).onTouchEventMy((MotionEvent)null);
         super.requestDisallowInterceptTouchEvent(var1);
      }
   }
}
