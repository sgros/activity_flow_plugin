package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AdminedChannelCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;

public class ChannelCreateActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ImageUpdater.ImageUpdaterDelegate {
   private static final int done_button = 1;
   private ArrayList adminedChannelCells = new ArrayList();
   private TextInfoPrivacyCell adminedInfoCell;
   private LinearLayout adminnedChannelsLayout;
   private TLRPC.FileLocation avatar;
   private AnimatorSet avatarAnimation;
   private TLRPC.FileLocation avatarBig;
   private AvatarDrawable avatarDrawable;
   private ImageView avatarEditor;
   private BackupImageView avatarImage;
   private View avatarOverlay;
   private RadialProgressView avatarProgressView;
   private boolean canCreatePublic = true;
   private int chatId;
   private int checkReqId;
   private Runnable checkRunnable;
   private TextView checkTextView;
   private boolean createAfterUpload;
   private int currentStep;
   private EditTextBoldCursor descriptionTextView;
   private View doneButton;
   private boolean donePressed;
   private EditText editText;
   private HeaderCell headerCell;
   private TextView helpTextView;
   private ImageUpdater imageUpdater;
   private TLRPC.ExportedChatInvite invite;
   private boolean isPrivate;
   private String lastCheckName;
   private boolean lastNameAvailable;
   private LinearLayout linearLayout;
   private LinearLayout linearLayout2;
   private LinearLayout linkContainer;
   private LoadingCell loadingAdminedCell;
   private boolean loadingAdminedChannels;
   private boolean loadingInvite;
   private EditTextEmoji nameTextView;
   private String nameToSet;
   private TextBlockCell privateContainer;
   private AlertDialog progressDialog;
   private LinearLayout publicContainer;
   private RadioButtonCell radioButtonCell1;
   private RadioButtonCell radioButtonCell2;
   private ShadowSectionCell sectionCell;
   private TextInfoPrivacyCell typeInfoCell;
   private TLRPC.InputFile uploadedAvatar;

   public ChannelCreateActivity(Bundle var1) {
      super(var1);
      this.currentStep = var1.getInt("step", 0);
      int var2 = this.currentStep;
      if (var2 == 0) {
         this.avatarDrawable = new AvatarDrawable();
         this.imageUpdater = new ImageUpdater();
         TLRPC.TL_channels_checkUsername var4 = new TLRPC.TL_channels_checkUsername();
         var4.username = "1";
         var4.channel = new TLRPC.TL_inputChannelEmpty();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE(this));
      } else {
         if (var2 == 1) {
            this.canCreatePublic = var1.getBoolean("canCreatePublic", true);
            boolean var3 = this.canCreatePublic;
            this.isPrivate = var3 ^ true;
            if (!var3) {
               this.loadAdminedChannels();
            }
         }

         this.chatId = var1.getInt("chat_id", 0);
      }

   }

   // $FF: synthetic method
   static int access$1300(ChannelCreateActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1400(ChannelCreateActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$1500(ChannelCreateActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1600(ChannelCreateActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$700(ChannelCreateActivity var0) {
      return var0.currentAccount;
   }

   private boolean checkUserName(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.checkTextView.setVisibility(0);
      } else {
         this.checkTextView.setVisibility(8);
      }

      Runnable var2 = this.checkRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
         this.checkRunnable = null;
         this.lastCheckName = null;
         if (this.checkReqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.checkReqId, true);
         }
      }

      this.lastNameAvailable = false;
      if (var1 != null) {
         if (var1.startsWith("_") || var1.endsWith("_")) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
         }

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var4 = var1.charAt(var3);
            if (var3 == 0 && var4 >= '0' && var4 <= '9') {
               this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", 2131559759));
               this.checkTextView.setTag("windowBackgroundWhiteRedText4");
               this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
               return false;
            }

            if ((var4 < '0' || var4 > '9') && (var4 < 'a' || var4 > 'z') && (var4 < 'A' || var4 > 'Z') && var4 != '_') {
               this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
               this.checkTextView.setTag("windowBackgroundWhiteRedText4");
               this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
               return false;
            }
         }
      }

      if (var1 != null && var1.length() >= 5) {
         if (var1.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", 2131559756));
            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
         } else {
            this.checkTextView.setText(LocaleController.getString("LinkChecking", 2131559750));
            this.checkTextView.setTag("windowBackgroundWhiteGrayText8");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
            this.lastCheckName = var1;
            this.checkRunnable = new _$$Lambda$ChannelCreateActivity$mDP7DqCeyyygG_tNsF0680xfQvY(this, var1);
            AndroidUtilities.runOnUIThread(this.checkRunnable, 300L);
            return true;
         }
      } else {
         this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", 2131559757));
         this.checkTextView.setTag("windowBackgroundWhiteRedText4");
         this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
         return false;
      }
   }

   private void generateLink() {
      if (!this.loadingInvite && this.invite == null) {
         this.loadingInvite = true;
         TLRPC.TL_messages_exportChatInvite var1 = new TLRPC.TL_messages_exportChatInvite();
         var1.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chatId);
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$ChannelCreateActivity$B1f5C5wPqlSBUACduCE1syb6pss(this));
      }

   }

   // $FF: synthetic method
   static boolean lambda$createView$2(View var0, MotionEvent var1) {
      return true;
   }

   private void loadAdminedChannels() {
      if (!this.loadingAdminedChannels) {
         this.loadingAdminedChannels = true;
         this.updatePrivatePublic();
         TLRPC.TL_channels_getAdminedPublicChannels var1 = new TLRPC.TL_channels_getAdminedPublicChannels();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, new _$$Lambda$ChannelCreateActivity$sX4Ap2SwpMlriw_rHlFC0Efiffk(this));
      }
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
                  ChannelCreateActivity.this.avatarAnimation = null;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (ChannelCreateActivity.this.avatarAnimation != null && ChannelCreateActivity.this.avatarEditor != null) {
                     if (var1) {
                        ChannelCreateActivity.this.avatarEditor.setVisibility(4);
                     } else {
                        ChannelCreateActivity.this.avatarProgressView.setVisibility(4);
                     }

                     ChannelCreateActivity.this.avatarAnimation = null;
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

   private void showErrorAlert(String var1) {
      if (this.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         byte var3 = -1;
         int var4 = var1.hashCode();
         if (var4 != 288843630) {
            if (var4 == 533175271 && var1.equals("USERNAME_OCCUPIED")) {
               var3 = 1;
            }
         } else if (var1.equals("USERNAME_INVALID")) {
            var3 = 0;
         }

         if (var3 != 0) {
            if (var3 != 1) {
               var2.setMessage(LocaleController.getString("ErrorOccurred", 2131559375));
            } else {
               var2.setMessage(LocaleController.getString("LinkInUse", 2131559753));
            }
         } else {
            var2.setMessage(LocaleController.getString("LinkInvalid", 2131559755));
         }

         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         this.showDialog(var2.create());
      }
   }

   private void updatePrivatePublic() {
      if (this.sectionCell != null) {
         boolean var1 = this.isPrivate;
         byte var2 = 8;
         TextInfoPrivacyCell var3;
         if (!var1 && !this.canCreatePublic) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131558916));
            this.typeInfoCell.setTag("windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.sectionCell.setVisibility(8);
            if (this.loadingAdminedChannels) {
               this.loadingAdminedCell.setVisibility(0);
               this.adminnedChannelsLayout.setVisibility(8);
               var3 = this.typeInfoCell;
               var3.setBackgroundDrawable(Theme.getThemedDrawable(var3.getContext(), 2131165395, "windowBackgroundGrayShadow"));
               this.adminedInfoCell.setVisibility(8);
            } else {
               var3 = this.typeInfoCell;
               var3.setBackgroundDrawable(Theme.getThemedDrawable(var3.getContext(), 2131165394, "windowBackgroundGrayShadow"));
               this.loadingAdminedCell.setVisibility(8);
               this.adminnedChannelsLayout.setVisibility(0);
               this.adminedInfoCell.setVisibility(0);
            }
         } else {
            this.typeInfoCell.setTag("windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.sectionCell.setVisibility(0);
            this.adminedInfoCell.setVisibility(8);
            this.adminnedChannelsLayout.setVisibility(8);
            var3 = this.typeInfoCell;
            var3.setBackgroundDrawable(Theme.getThemedDrawable(var3.getContext(), 2131165395, "windowBackgroundGrayShadow"));
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            TextInfoPrivacyCell var4 = this.typeInfoCell;
            int var5;
            String var6;
            if (this.isPrivate) {
               var5 = 2131558990;
               var6 = "ChannelPrivateLinkHelp";
            } else {
               var5 = 2131559013;
               var6 = "ChannelUsernameHelp";
            }

            var4.setText(LocaleController.getString(var6, var5));
            HeaderCell var7 = this.headerCell;
            if (this.isPrivate) {
               var5 = 2131558952;
               var6 = "ChannelInviteLinkTitle";
            } else {
               var5 = 2131558960;
               var6 = "ChannelLinkTitle";
            }

            var7.setText(LocaleController.getString(var6, var5));
            LinearLayout var9 = this.publicContainer;
            byte var12;
            if (this.isPrivate) {
               var12 = 8;
            } else {
               var12 = 0;
            }

            var9.setVisibility(var12);
            TextBlockCell var10 = this.privateContainer;
            if (this.isPrivate) {
               var12 = 0;
            } else {
               var12 = 8;
            }

            var10.setVisibility(var12);
            var9 = this.linkContainer;
            if (this.isPrivate) {
               var5 = 0;
            } else {
               var5 = AndroidUtilities.dp(7.0F);
            }

            var9.setPadding(0, 0, 0, var5);
            TextBlockCell var8 = this.privateContainer;
            TLRPC.ExportedChatInvite var11 = this.invite;
            if (var11 != null) {
               var6 = var11.link;
            } else {
               var6 = LocaleController.getString("Loading", 2131559768);
            }

            var8.setText(var6, false);
            TextView var13 = this.checkTextView;
            var12 = var2;
            if (!this.isPrivate) {
               var12 = var2;
               if (var13.length() != 0) {
                  var12 = 0;
               }
            }

            var13.setVisibility(var12);
         }

         this.radioButtonCell1.setChecked(this.isPrivate ^ true, true);
         this.radioButtonCell2.setChecked(this.isPrivate, true);
         this.descriptionTextView.clearFocus();
         AndroidUtilities.hideKeyboard(this.descriptionTextView);
      }
   }

   public View createView(Context var1) {
      EditTextEmoji var2 = this.nameTextView;
      if (var2 != null) {
         var2.onDestroy();
      }

      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$onItemClick$0$ChannelCreateActivity$1(DialogInterface var1) {
            ChannelCreateActivity.this.createAfterUpload = false;
            ChannelCreateActivity.this.progressDialog = null;
            ChannelCreateActivity.this.donePressed = false;
         }

         // $FF: synthetic method
         public void lambda$onItemClick$1$ChannelCreateActivity$1(int var1, DialogInterface var2) {
            ConnectionsManager.getInstance(ChannelCreateActivity.access$1400(ChannelCreateActivity.this)).cancelRequest(var1, true);
            ChannelCreateActivity.this.donePressed = false;
         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChannelCreateActivity.this.finishFragment();
            } else if (var1 == 1) {
               Vibrator var3;
               if (ChannelCreateActivity.this.currentStep == 0) {
                  if (ChannelCreateActivity.this.donePressed || ChannelCreateActivity.this.getParentActivity() == null) {
                     return;
                  }

                  if (ChannelCreateActivity.this.nameTextView.length() == 0) {
                     var3 = (Vibrator)ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                     if (var3 != null) {
                        var3.vibrate(200L);
                     }

                     AndroidUtilities.shakeView(ChannelCreateActivity.this.nameTextView, 2.0F, 0);
                     return;
                  }

                  ChannelCreateActivity.this.donePressed = true;
                  ChannelCreateActivity var5;
                  if (ChannelCreateActivity.this.imageUpdater.uploadingImage != null) {
                     ChannelCreateActivity.this.createAfterUpload = true;
                     var5 = ChannelCreateActivity.this;
                     var5.progressDialog = new AlertDialog(var5.getParentActivity(), 3);
                     ChannelCreateActivity.this.progressDialog.setOnCancelListener(new _$$Lambda$ChannelCreateActivity$1$b7xFRmQZDJGprfBGkWoRomCKvDQ(this));
                     ChannelCreateActivity.this.progressDialog.show();
                     return;
                  }

                  var1 = MessagesController.getInstance(ChannelCreateActivity.access$700(ChannelCreateActivity.this)).createChat(ChannelCreateActivity.this.nameTextView.getText().toString(), new ArrayList(), ChannelCreateActivity.this.descriptionTextView.getText().toString(), 2, ChannelCreateActivity.this);
                  var5 = ChannelCreateActivity.this;
                  var5.progressDialog = new AlertDialog(var5.getParentActivity(), 3);
                  ChannelCreateActivity.this.progressDialog.setOnCancelListener(new _$$Lambda$ChannelCreateActivity$1$q1TLoAihH5rBozhaNhmW5QGac_s(this, var1));
                  ChannelCreateActivity.this.progressDialog.show();
               } else if (ChannelCreateActivity.this.currentStep == 1) {
                  if (!ChannelCreateActivity.this.isPrivate) {
                     if (ChannelCreateActivity.this.descriptionTextView.length() == 0) {
                        AlertDialog.Builder var4 = new AlertDialog.Builder(ChannelCreateActivity.this.getParentActivity());
                        var4.setTitle(LocaleController.getString("AppName", 2131558635));
                        var4.setMessage(LocaleController.getString("ChannelPublicEmptyUsername", 2131558992));
                        var4.setPositiveButton(LocaleController.getString("Close", 2131559117), (OnClickListener)null);
                        ChannelCreateActivity.this.showDialog(var4.create());
                        return;
                     }

                     if (!ChannelCreateActivity.this.lastNameAvailable) {
                        var3 = (Vibrator)ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                        if (var3 != null) {
                           var3.vibrate(200L);
                        }

                        AndroidUtilities.shakeView(ChannelCreateActivity.this.checkTextView, 2.0F, 0);
                        return;
                     }

                     MessagesController.getInstance(ChannelCreateActivity.access$1300(ChannelCreateActivity.this)).updateChannelUserName(ChannelCreateActivity.this.chatId, ChannelCreateActivity.this.lastCheckName);
                  }

                  Bundle var2 = new Bundle();
                  var2.putInt("step", 2);
                  var2.putInt("chatId", ChannelCreateActivity.this.chatId);
                  var2.putInt("chatType", 2);
                  ChannelCreateActivity.this.presentFragment(new GroupCreateActivity(var2), true);
               }
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      int var3 = this.currentStep;
      byte var13;
      TextView var14;
      if (var3 == 0) {
         super.actionBar.setTitle(LocaleController.getString("NewChannel", 2131559898));
         SizeNotifierFrameLayout var4 = new SizeNotifierFrameLayout(var1) {
            private boolean ignoreLayout;

            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               int var6 = this.getChildCount();
               int var7 = this.getKeyboardHeight();
               int var8 = AndroidUtilities.dp(20.0F);
               int var9 = 0;
               if (var7 <= var8 && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                  var8 = ChannelCreateActivity.this.nameTextView.getEmojiPadding();
               } else {
                  var8 = 0;
               }

               this.setBottomClip(var8);

               for(; var9 < var6; ++var9) {
                  View var10 = this.getChildAt(var9);
                  if (var10.getVisibility() != 8) {
                     LayoutParams var11 = (LayoutParams)var10.getLayoutParams();
                     int var12 = var10.getMeasuredWidth();
                     int var13 = var10.getMeasuredHeight();
                     int var14 = var11.gravity;
                     var7 = var14;
                     if (var14 == -1) {
                        var7 = 51;
                     }

                     int var15;
                     label61: {
                        var15 = var7 & 112;
                        var7 = var7 & 7 & 7;
                        if (var7 != 1) {
                           if (var7 != 5) {
                              var14 = var11.leftMargin;
                              break label61;
                           }

                           var14 = var4 - var12;
                           var7 = var11.rightMargin;
                        } else {
                           var14 = (var4 - var2 - var12) / 2 + var11.leftMargin;
                           var7 = var11.rightMargin;
                        }

                        var14 -= var7;
                     }

                     label55: {
                        if (var15 != 16) {
                           if (var15 == 48) {
                              var7 = var11.topMargin + this.getPaddingTop();
                              break label55;
                           }

                           if (var15 != 80) {
                              var7 = var11.topMargin;
                              break label55;
                           }

                           var15 = var5 - var8 - var3 - var13;
                           var7 = var11.bottomMargin;
                        } else {
                           var15 = (var5 - var8 - var3 - var13) / 2 + var11.topMargin;
                           var7 = var11.bottomMargin;
                        }

                        var7 = var15 - var7;
                     }

                     var15 = var7;
                     if (ChannelCreateActivity.this.nameTextView != null) {
                        var15 = var7;
                        if (ChannelCreateActivity.this.nameTextView.isPopupView(var10)) {
                           if (AndroidUtilities.isTablet()) {
                              var7 = this.getMeasuredHeight();
                              var15 = var10.getMeasuredHeight();
                           } else {
                              var7 = this.getMeasuredHeight() + this.getKeyboardHeight();
                              var15 = var10.getMeasuredHeight();
                           }

                           var15 = var7 - var15;
                        }
                     }

                     var10.layout(var14, var15, var12 + var14, var13 + var15);
                  }
               }

               this.notifyHeightChanged();
            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1);
               int var4 = MeasureSpec.getSize(var2);
               this.setMeasuredDimension(var3, var4);
               int var5 = var4 - this.getPaddingTop();
               this.measureChildWithMargins(ChannelCreateActivity.access$1500(ChannelCreateActivity.this), var1, 0, var2, 0);
               int var6 = this.getKeyboardHeight();
               int var7 = AndroidUtilities.dp(20.0F);
               var4 = 0;
               if (var6 > var7) {
                  this.ignoreLayout = true;
                  ChannelCreateActivity.this.nameTextView.hideEmojiView();
                  this.ignoreLayout = false;
               }

               for(var7 = this.getChildCount(); var4 < var7; ++var4) {
                  View var8 = this.getChildAt(var4);
                  if (var8 != null && var8.getVisibility() != 8 && var8 != ChannelCreateActivity.access$1600(ChannelCreateActivity.this)) {
                     if (ChannelCreateActivity.this.nameTextView != null && ChannelCreateActivity.this.nameTextView.isPopupView(var8)) {
                        if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                           var8.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var8.getLayoutParams().height, 1073741824));
                        } else if (AndroidUtilities.isTablet()) {
                           var6 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
                           float var9;
                           if (AndroidUtilities.isTablet()) {
                              var9 = 200.0F;
                           } else {
                              var9 = 320.0F;
                           }

                           var8.measure(var6, MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(var9), var5 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                        } else {
                           var8.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var5 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                        }
                     } else {
                        this.measureChildWithMargins(var8, var1, 0, var2, 0);
                     }
                  }
               }

            }

            public void requestLayout() {
               if (!this.ignoreLayout) {
                  super.requestLayout();
               }
            }
         };
         var4.setOnTouchListener(_$$Lambda$ChannelCreateActivity$lIiXta2UxN2m1Vi_R2Lzg4Rdjg4.INSTANCE);
         super.fragmentView = var4;
         super.fragmentView.setTag("windowBackgroundWhite");
         super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.linearLayout = new LinearLayout(var1);
         this.linearLayout.setOrientation(1);
         var4.addView(this.linearLayout, new LayoutParams(-1, -2));
         FrameLayout var10 = new FrameLayout(var1);
         this.linearLayout.addView(var10, LayoutHelper.createLinear(-1, -2));
         this.avatarImage = new BackupImageView(var1) {
            public void invalidate() {
               if (ChannelCreateActivity.this.avatarOverlay != null) {
                  ChannelCreateActivity.this.avatarOverlay.invalidate();
               }

               super.invalidate();
            }

            public void invalidate(int var1, int var2, int var3, int var4) {
               if (ChannelCreateActivity.this.avatarOverlay != null) {
                  ChannelCreateActivity.this.avatarOverlay.invalidate();
               }

               super.invalidate(var1, var2, var3, var4);
            }
         };
         this.avatarImage.setRoundRadius(AndroidUtilities.dp(32.0F));
         this.avatarDrawable.setInfo(5, (String)null, (String)null, false);
         this.avatarImage.setImageDrawable(this.avatarDrawable);
         BackupImageView var5 = this.avatarImage;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         float var6;
         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 16.0F;
         }

         float var7;
         if (LocaleController.isRTL) {
            var7 = 16.0F;
         } else {
            var7 = 0.0F;
         }

         var10.addView(var5, LayoutHelper.createFrame(64, 64.0F, var13 | 48, var6, 12.0F, var7, 12.0F));
         final Paint var22 = new Paint(1);
         var22.setColor(1426063360);
         this.avatarOverlay = new View(var1) {
            protected void onDraw(Canvas var1) {
               if (ChannelCreateActivity.this.avatarImage != null && ChannelCreateActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                  var22.setAlpha((int)(ChannelCreateActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0F));
                  var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0F), var22);
               }

            }
         };
         View var24 = this.avatarOverlay;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var7 = 16.0F;
         } else {
            var7 = 0.0F;
         }

         var10.addView(var24, LayoutHelper.createFrame(64, 64.0F, var13 | 48, var6, 12.0F, var7, 12.0F));
         this.avatarOverlay.setOnClickListener(new _$$Lambda$ChannelCreateActivity$s4ZwNFyULCtdH_y7TaKdhDy0a3c(this));
         this.avatarEditor = new ImageView(var1) {
            public void invalidate() {
               super.invalidate();
               ChannelCreateActivity.this.avatarOverlay.invalidate();
            }

            public void invalidate(int var1, int var2, int var3, int var4) {
               super.invalidate(var1, var2, var3, var4);
               ChannelCreateActivity.this.avatarOverlay.invalidate();
            }
         };
         this.avatarEditor.setScaleType(ScaleType.CENTER);
         this.avatarEditor.setImageResource(2131165572);
         this.avatarEditor.setEnabled(false);
         this.avatarEditor.setClickable(false);
         ImageView var25 = this.avatarEditor;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var7 = 16.0F;
         } else {
            var7 = 0.0F;
         }

         var10.addView(var25, LayoutHelper.createFrame(64, 64.0F, var13 | 48, var6, 12.0F, var7, 12.0F));
         this.avatarProgressView = new RadialProgressView(var1);
         this.avatarProgressView.setSize(AndroidUtilities.dp(30.0F));
         this.avatarProgressView.setProgressColor(-1);
         RadialProgressView var26 = this.avatarProgressView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         if (LocaleController.isRTL) {
            var6 = 0.0F;
         } else {
            var6 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var7 = 16.0F;
         } else {
            var7 = 0.0F;
         }

         var10.addView(var26, LayoutHelper.createFrame(64, 64.0F, var13 | 48, var6, 12.0F, var7, 12.0F));
         this.showAvatarProgress(false, false);
         this.nameTextView = new EditTextEmoji(var1, var4, this, 0);
         this.nameTextView.setHint(LocaleController.getString("EnterChannelName", 2131559367));
         String var17 = this.nameToSet;
         if (var17 != null) {
            this.nameTextView.setText(var17);
            this.nameToSet = null;
         }

         LengthFilter var19 = new LengthFilter(100);
         this.nameTextView.setFilters(new InputFilter[]{var19});
         EditTextEmoji var20 = this.nameTextView;
         if (LocaleController.isRTL) {
            var6 = 5.0F;
         } else {
            var6 = 96.0F;
         }

         if (LocaleController.isRTL) {
            var7 = 96.0F;
         } else {
            var7 = 5.0F;
         }

         var10.addView(var20, LayoutHelper.createFrame(-1, -2.0F, 16, var6, 0.0F, var7, 0.0F));
         this.descriptionTextView = new EditTextBoldCursor(var1);
         this.descriptionTextView.setTextSize(1, 18.0F);
         this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.descriptionTextView.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
         this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
         EditTextBoldCursor var11 = this.descriptionTextView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         var11.setGravity(var13);
         this.descriptionTextView.setInputType(180225);
         this.descriptionTextView.setImeOptions(6);
         LengthFilter var12 = new LengthFilter(120);
         this.descriptionTextView.setFilters(new InputFilter[]{var12});
         this.descriptionTextView.setHint(LocaleController.getString("DescriptionPlaceholder", 2131559265));
         this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0F));
         this.descriptionTextView.setCursorWidth(1.5F);
         this.linearLayout.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 24.0F, 18.0F, 24.0F, 0.0F));
         this.descriptionTextView.setOnEditorActionListener(new _$$Lambda$ChannelCreateActivity$MiQ4URkqTYXvUua86XByJz6HPDg(this));
         this.descriptionTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.helpTextView = new TextView(var1);
         this.helpTextView.setTextSize(1, 15.0F);
         this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
         TextView var8 = this.helpTextView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         var8.setGravity(var13);
         this.helpTextView.setText(LocaleController.getString("DescriptionInfo", 2131559263));
         LinearLayout var9 = this.linearLayout;
         var14 = this.helpTextView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         var9.addView(var14, LayoutHelper.createLinear(-2, -2, var13, 24, 10, 24, 20));
      } else if (var3 == 1) {
         super.fragmentView = new ScrollView(var1);
         ScrollView var15 = (ScrollView)super.fragmentView;
         var15.setFillViewport(true);
         this.linearLayout = new LinearLayout(var1);
         this.linearLayout.setOrientation(1);
         var15.addView(this.linearLayout, new LayoutParams(-1, -2));
         super.actionBar.setTitle(LocaleController.getString("ChannelSettings", 2131558998));
         super.fragmentView.setTag("windowBackgroundGray");
         super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
         this.linearLayout2 = new LinearLayout(var1);
         this.linearLayout2.setOrientation(1);
         this.linearLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.linearLayout.addView(this.linearLayout2, LayoutHelper.createLinear(-1, -2));
         this.radioButtonCell1 = new RadioButtonCell(var1);
         this.radioButtonCell1.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131558991), LocaleController.getString("ChannelPublicInfo", 2131558993), false, this.isPrivate ^ true);
         this.linearLayout2.addView(this.radioButtonCell1, LayoutHelper.createLinear(-1, -2));
         this.radioButtonCell1.setOnClickListener(new _$$Lambda$ChannelCreateActivity$ERDUGoRSlmYfCpWGH5A0thCtafY(this));
         this.radioButtonCell2 = new RadioButtonCell(var1);
         this.radioButtonCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131558988), LocaleController.getString("ChannelPrivateInfo", 2131558989), false, this.isPrivate);
         this.linearLayout2.addView(this.radioButtonCell2, LayoutHelper.createLinear(-1, -2));
         this.radioButtonCell2.setOnClickListener(new _$$Lambda$ChannelCreateActivity$4I7Y_V5uH9YemI181a2NrRWSAsc(this));
         this.sectionCell = new ShadowSectionCell(var1);
         this.linearLayout.addView(this.sectionCell, LayoutHelper.createLinear(-1, -2));
         this.linkContainer = new LinearLayout(var1);
         this.linkContainer.setOrientation(1);
         this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.linearLayout.addView(this.linkContainer, LayoutHelper.createLinear(-1, -2));
         this.headerCell = new HeaderCell(var1);
         this.linkContainer.addView(this.headerCell);
         this.publicContainer = new LinearLayout(var1);
         this.publicContainer.setOrientation(0);
         this.linkContainer.addView(this.publicContainer, LayoutHelper.createLinear(-1, 36, 17.0F, 7.0F, 17.0F, 0.0F));
         this.editText = new EditText(var1);
         EditText var16 = this.editText;
         StringBuilder var21 = new StringBuilder();
         var21.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var21.append("/");
         var16.setText(var21.toString());
         this.editText.setTextSize(1, 18.0F);
         this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.editText.setMaxLines(1);
         this.editText.setLines(1);
         this.editText.setEnabled(false);
         this.editText.setBackgroundDrawable((Drawable)null);
         this.editText.setPadding(0, 0, 0, 0);
         this.editText.setSingleLine(true);
         this.editText.setInputType(163840);
         this.editText.setImeOptions(6);
         this.publicContainer.addView(this.editText, LayoutHelper.createLinear(-2, 36));
         this.descriptionTextView = new EditTextBoldCursor(var1);
         this.descriptionTextView.setTextSize(1, 18.0F);
         this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
         this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.descriptionTextView.setMaxLines(1);
         this.descriptionTextView.setLines(1);
         this.descriptionTextView.setBackgroundDrawable((Drawable)null);
         this.descriptionTextView.setPadding(0, 0, 0, 0);
         this.descriptionTextView.setSingleLine(true);
         this.descriptionTextView.setInputType(163872);
         this.descriptionTextView.setImeOptions(6);
         this.descriptionTextView.setHint(LocaleController.getString("ChannelUsernamePlaceholder", 2131559014));
         this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0F));
         this.descriptionTextView.setCursorWidth(1.5F);
         this.publicContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, 36));
         this.descriptionTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               ChannelCreateActivity var5 = ChannelCreateActivity.this;
               var5.checkUserName(var5.descriptionTextView.getText().toString());
            }
         });
         this.privateContainer = new TextBlockCell(var1);
         this.privateContainer.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.linkContainer.addView(this.privateContainer);
         this.privateContainer.setOnClickListener(new _$$Lambda$ChannelCreateActivity$9kLD_6LVZi1jry2jmUaAGbUgTCw(this));
         this.checkTextView = new TextView(var1);
         this.checkTextView.setTextSize(1, 15.0F);
         var14 = this.checkTextView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         var14.setGravity(var13);
         this.checkTextView.setVisibility(8);
         LinearLayout var18 = this.linkContainer;
         TextView var23 = this.checkTextView;
         if (LocaleController.isRTL) {
            var13 = 5;
         } else {
            var13 = 3;
         }

         var18.addView(var23, LayoutHelper.createLinear(-2, -2, var13, 17, 3, 17, 7));
         this.typeInfoCell = new TextInfoPrivacyCell(var1);
         this.typeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.linearLayout.addView(this.typeInfoCell, LayoutHelper.createLinear(-1, -2));
         this.loadingAdminedCell = new LoadingCell(var1);
         this.linearLayout.addView(this.loadingAdminedCell, LayoutHelper.createLinear(-1, -2));
         this.adminnedChannelsLayout = new LinearLayout(var1);
         this.adminnedChannelsLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         this.adminnedChannelsLayout.setOrientation(1);
         this.linearLayout.addView(this.adminnedChannelsLayout, LayoutHelper.createLinear(-1, -2));
         this.adminedInfoCell = new TextInfoPrivacyCell(var1);
         this.adminedInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         this.linearLayout.addView(this.adminedInfoCell, LayoutHelper.createLinear(-1, -2));
         this.updatePrivatePublic();
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatDidFailCreate) {
         AlertDialog var7 = this.progressDialog;
         if (var7 != null) {
            try {
               var7.dismiss();
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }

         this.donePressed = false;
      } else if (var1 == NotificationCenter.chatDidCreated) {
         AlertDialog var4 = this.progressDialog;
         if (var4 != null) {
            try {
               var4.dismiss();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }
         }

         var1 = (Integer)var3[0];
         Bundle var8 = new Bundle();
         var8.putInt("step", 1);
         var8.putInt("chat_id", var1);
         var8.putBoolean("canCreatePublic", this.canCreatePublic);
         if (this.uploadedAvatar != null) {
            MessagesController.getInstance(super.currentAccount).changeChatAvatar(var1, this.uploadedAvatar, this.avatar, this.avatarBig);
         }

         this.presentFragment(new ChannelCreateActivity(var8), true);
      }

   }

   public void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$m0E_xD_NIIwbm9llzALGLpdnpvI(this, var1, var3, var2));
   }

   public String getInitialSearchString() {
      return this.nameTextView.getText().toString();
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c var1 = new _$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c(this);
      View var2 = super.fragmentView;
      int var3 = ThemeDescription.FLAG_BACKGROUND;
      return new ThemeDescription[]{new ThemeDescription(var2, ThemeDescription.FLAG_CHECKTAG | var3, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8"), new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.linkContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.sectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText8"), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGreenText"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.privateContainer, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.privateContainer, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.loadingAdminedCell, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackground"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "radioBackgroundChecked"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"), new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"deleteButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public void lambda$checkUserName$20$ChannelCreateActivity(String var1) {
      TLRPC.TL_channels_checkUsername var2 = new TLRPC.TL_channels_checkUsername();
      var2.username = var1;
      var2.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
      this.checkReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$ChannelCreateActivity$btlKiyTzqeUa7MaXnitfG9ByjcI(this, var1), 2);
   }

   // $FF: synthetic method
   public void lambda$createView$4$ChannelCreateActivity(View var1) {
      ImageUpdater var3 = this.imageUpdater;
      boolean var2;
      if (this.avatar != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.openMenu(var2, new _$$Lambda$ChannelCreateActivity$B1GdgGV1tK78Cjzb4rWLcAqpV0M(this));
   }

   // $FF: synthetic method
   public boolean lambda$createView$5$ChannelCreateActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         View var4 = this.doneButton;
         if (var4 != null) {
            var4.performClick();
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$createView$6$ChannelCreateActivity(View var1) {
      if (this.isPrivate) {
         this.isPrivate = false;
         this.updatePrivatePublic();
      }
   }

   // $FF: synthetic method
   public void lambda$createView$7$ChannelCreateActivity(View var1) {
      if (!this.isPrivate) {
         this.isPrivate = true;
         this.updatePrivatePublic();
      }
   }

   // $FF: synthetic method
   public void lambda$createView$8$ChannelCreateActivity(View var1) {
      if (this.invite != null) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$didUploadPhoto$11$ChannelCreateActivity(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      if (var1 != null) {
         this.uploadedAvatar = var1;
         if (this.createAfterUpload) {
            try {
               if (this.progressDialog != null && this.progressDialog.isShowing()) {
                  this.progressDialog.dismiss();
                  this.progressDialog = null;
               }
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            this.donePressed = false;
            this.doneButton.performClick();
         }

         this.showAvatarProgress(false, true);
      } else {
         this.avatar = var2.location;
         this.avatarBig = var3.location;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForLocal(this.avatar), "50_50", (Drawable)this.avatarDrawable, (Object)null);
         this.showAvatarProgress(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$generateLink$10$ChannelCreateActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$02OCxzLG_caeGKZbAxcXnBPRtU4(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$21$ChannelCreateActivity() {
      LinearLayout var1 = this.adminnedChannelsLayout;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.adminnedChannelsLayout.getChildAt(var3);
            if (var4 instanceof AdminedChannelCell) {
               ((AdminedChannelCell)var4).update();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadAdminedChannels$17$ChannelCreateActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$HGsozgXd5QuUD9PHWrJWhWbHHQs(this, var1));
   }

   // $FF: synthetic method
   public void lambda$new$1$ChannelCreateActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$3ctYbjqxEmY29fuS63ZiDPUydpA(this, var2));
   }

   // $FF: synthetic method
   public void lambda$null$0$ChannelCreateActivity(TLRPC.TL_error var1) {
      boolean var2;
      if (var1 != null && var1.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
         var2 = false;
      } else {
         var2 = true;
      }

      this.canCreatePublic = var2;
   }

   // $FF: synthetic method
   public void lambda$null$12$ChannelCreateActivity() {
      this.canCreatePublic = true;
      if (this.descriptionTextView.length() > 0) {
         this.checkUserName(this.descriptionTextView.getText().toString());
      }

      this.updatePrivatePublic();
   }

   // $FF: synthetic method
   public void lambda$null$13$ChannelCreateActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 instanceof TLRPC.TL_boolTrue) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$L_G3GfO7KOkNy_wD7C5sV0zstvg(this));
      }

   }

   // $FF: synthetic method
   public void lambda$null$14$ChannelCreateActivity(TLRPC.Chat var1, DialogInterface var2, int var3) {
      TLRPC.TL_channels_updateUsername var4 = new TLRPC.TL_channels_updateUsername();
      var4.channel = MessagesController.getInputChannel(var1);
      var4.username = "";
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$ChannelCreateActivity$itNfNKVGl3ReRwzmbqq8toziSoA(this), 64);
   }

   // $FF: synthetic method
   public void lambda$null$15$ChannelCreateActivity(View var1) {
      TLRPC.Chat var2 = ((AdminedChannelCell)var1.getParent()).getCurrentChannel();
      AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
      var4.setTitle(LocaleController.getString("AppName", 2131558635));
      StringBuilder var3;
      if (var2.megagroup) {
         var3 = new StringBuilder();
         var3.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var3.append("/");
         var3.append(var2.username);
         var4.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", 2131560621, var3.toString(), var2.title)));
      } else {
         var3 = new StringBuilder();
         var3.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var3.append("/");
         var3.append(var2.username);
         var4.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", 2131560622, var3.toString(), var2.title)));
      }

      var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var4.setPositiveButton(LocaleController.getString("RevokeButton", 2131560619), new _$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf_2lbcB8fcZcxM_I(this, var2));
      this.showDialog(var4.create());
   }

   // $FF: synthetic method
   public void lambda$null$16$ChannelCreateActivity(TLObject var1) {
      this.loadingAdminedChannels = false;
      if (var1 != null) {
         if (this.getParentActivity() == null) {
            return;
         }

         int var2;
         for(var2 = 0; var2 < this.adminedChannelCells.size(); ++var2) {
            this.linearLayout.removeView((View)this.adminedChannelCells.get(var2));
         }

         this.adminedChannelCells.clear();
         TLRPC.TL_messages_chats var3 = (TLRPC.TL_messages_chats)var1;

         for(var2 = 0; var2 < var3.chats.size(); ++var2) {
            AdminedChannelCell var7 = new AdminedChannelCell(this.getParentActivity(), new _$$Lambda$ChannelCreateActivity$v16kHitJ_P9_5lDgeOrLxNNrfqc(this));
            TLRPC.Chat var4 = (TLRPC.Chat)var3.chats.get(var2);
            int var5 = var3.chats.size();
            boolean var6 = true;
            if (var2 != var5 - 1) {
               var6 = false;
            }

            var7.setChannel(var4, var6);
            this.adminedChannelCells.add(var7);
            this.adminnedChannelsLayout.addView(var7, LayoutHelper.createLinear(-1, 72));
         }

         this.updatePrivatePublic();
      }

   }

   // $FF: synthetic method
   public void lambda$null$18$ChannelCreateActivity(String var1, TLRPC.TL_error var2, TLObject var3) {
      this.checkReqId = 0;
      String var4 = this.lastCheckName;
      if (var4 != null && var4.equals(var1)) {
         if (var2 == null && var3 instanceof TLRPC.TL_boolTrue) {
            this.checkTextView.setText(LocaleController.formatString("LinkAvailable", 2131559749, var1));
            this.checkTextView.setTag("windowBackgroundWhiteGreenText");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
            this.lastNameAvailable = true;
         } else {
            if (var2 != null && var2.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
               this.canCreatePublic = false;
               this.loadAdminedChannels();
            } else {
               this.checkTextView.setText(LocaleController.getString("LinkInUse", 2131559753));
            }

            this.checkTextView.setTag("windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.lastNameAvailable = false;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$19$ChannelCreateActivity(String var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$ONcbh_gtesiOrY2PbEXECoYX8sk(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public void lambda$null$3$ChannelCreateActivity() {
      this.avatar = null;
      this.avatarBig = null;
      this.uploadedAvatar = null;
      this.showAvatarProgress(false, true);
      this.avatarImage.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)null);
   }

   // $FF: synthetic method
   public void lambda$null$9$ChannelCreateActivity(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         this.invite = (TLRPC.ExportedChatInvite)var2;
      }

      this.loadingInvite = false;
      TextBlockCell var5 = this.privateContainer;
      TLRPC.ExportedChatInvite var3 = this.invite;
      String var4;
      if (var3 != null) {
         var4 = var3.link;
      } else {
         var4 = LocaleController.getString("Loading", 2131559768);
      }

      var5.setText(var4, false);
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      this.imageUpdater.onActivityResult(var1, var2, var3);
   }

   public boolean onBackPressed() {
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null && var1.isPopupShowing()) {
         this.nameTextView.hidePopup(true);
         return false;
      } else {
         return true;
      }
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidFailCreate);
      if (this.currentStep == 1) {
         this.generateLink();
      }

      ImageUpdater var1 = this.imageUpdater;
      if (var1 != null) {
         var1.parentFragment = this;
         var1.delegate = this;
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidFailCreate);
      ImageUpdater var1 = this.imageUpdater;
      if (var1 != null) {
         var1.clear();
      }

      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
      EditTextEmoji var2 = this.nameTextView;
      if (var2 != null) {
         var2.onDestroy();
      }

   }

   public void onPause() {
      super.onPause();
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public void onResume() {
      super.onResume();
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null) {
         var1.onResume();
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && this.currentStep != 1) {
         this.nameTextView.openKeyboard();
      }

   }

   public void restoreSelfArgs(Bundle var1) {
      if (this.currentStep == 0) {
         ImageUpdater var2 = this.imageUpdater;
         if (var2 != null) {
            var2.currentPicturePath = var1.getString("path");
         }

         String var3 = var1.getString("nameTextView");
         if (var3 != null) {
            EditTextEmoji var4 = this.nameTextView;
            if (var4 != null) {
               var4.setText(var3);
            } else {
               this.nameToSet = var3;
            }
         }
      }

   }

   public void saveSelfArgs(Bundle var1) {
      if (this.currentStep == 0) {
         ImageUpdater var2 = this.imageUpdater;
         String var3;
         if (var2 != null) {
            var3 = var2.currentPicturePath;
            if (var3 != null) {
               var1.putString("path", var3);
            }
         }

         EditTextEmoji var4 = this.nameTextView;
         if (var4 != null) {
            var3 = var4.getText().toString();
            if (var3 != null && var3.length() != 0) {
               var1.putString("nameTextView", var3);
            }
         }
      }

   }
}
