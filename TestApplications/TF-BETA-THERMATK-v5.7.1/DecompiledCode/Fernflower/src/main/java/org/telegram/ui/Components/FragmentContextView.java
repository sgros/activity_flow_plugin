package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.VoIPActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;

public class FragmentContextView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private FragmentContextView additionalContextView;
   private AnimatorSet animatorSet;
   private Runnable checkLocationRunnable = new Runnable() {
      public void run() {
         FragmentContextView.this.checkLocationString();
         AndroidUtilities.runOnUIThread(FragmentContextView.this.checkLocationRunnable, 1000L);
      }
   };
   private ImageView closeButton;
   private int currentStyle = -1;
   private boolean firstLocationsLoaded;
   private BaseFragment fragment;
   private FrameLayout frameLayout;
   private boolean isLocation;
   private int lastLocationSharingCount = -1;
   private MessageObject lastMessageObject;
   private String lastString;
   private boolean loadingSharingCount;
   private ImageView playButton;
   private ImageView playbackSpeedButton;
   private TextView titleTextView;
   private float topPadding;
   private boolean visible;
   private float yPosition;

   public FragmentContextView(Context var1, BaseFragment var2, boolean var3) {
      super(var1);
      this.fragment = var2;
      this.visible = true;
      this.isLocation = var3;
      ((ViewGroup)this.fragment.getFragmentView()).setClipToPadding(false);
      this.setTag(1);
      this.frameLayout = new FrameLayout(var1);
      this.frameLayout.setWillNotDraw(false);
      this.addView(this.frameLayout, LayoutHelper.createFrame(-1, 36.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
      View var4 = new View(var1);
      var4.setBackgroundResource(2131165407);
      this.addView(var4, LayoutHelper.createFrame(-1, 3.0F, 51, 0.0F, 36.0F, 0.0F, 0.0F));
      this.playButton = new ImageView(var1);
      this.playButton.setScaleType(ScaleType.CENTER);
      this.playButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("inappPlayerPlayPause"), Mode.MULTIPLY));
      this.addView(this.playButton, LayoutHelper.createFrame(36, 36, 51));
      this.playButton.setOnClickListener(new _$$Lambda$FragmentContextView$xaUr_8YxtF96bWuD8Iv6_gj2wjI(this));
      this.titleTextView = new TextView(var1);
      this.titleTextView.setMaxLines(1);
      this.titleTextView.setLines(1);
      this.titleTextView.setSingleLine(true);
      this.titleTextView.setEllipsize(TruncateAt.END);
      this.titleTextView.setTextSize(1, 15.0F);
      this.titleTextView.setGravity(19);
      this.addView(this.titleTextView, LayoutHelper.createFrame(-1, 36.0F, 51, 35.0F, 0.0F, 36.0F, 0.0F));
      if (!var3) {
         this.playbackSpeedButton = new ImageView(var1);
         this.playbackSpeedButton.setScaleType(ScaleType.CENTER);
         this.playbackSpeedButton.setImageResource(2131165910);
         this.playbackSpeedButton.setContentDescription(LocaleController.getString("AccDescrPlayerSpeed", 2131558458));
         if (AndroidUtilities.density >= 3.0F) {
            this.playbackSpeedButton.setPadding(0, 1, 0, 0);
         }

         this.addView(this.playbackSpeedButton, LayoutHelper.createFrame(36, 36.0F, 53, 0.0F, 0.0F, 36.0F, 0.0F));
         this.playbackSpeedButton.setOnClickListener(new _$$Lambda$FragmentContextView$mn4uoFbwvEVtbgf8Ptja5mtZJQM(this));
         this.updatePlaybackButton();
      }

      this.closeButton = new ImageView(var1);
      this.closeButton.setImageResource(2131165604);
      this.closeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("inappPlayerClose"), Mode.MULTIPLY));
      this.closeButton.setScaleType(ScaleType.CENTER);
      this.addView(this.closeButton, LayoutHelper.createFrame(36, 36, 53));
      this.closeButton.setOnClickListener(new _$$Lambda$FragmentContextView$A00dqLRerQA_JpKS29_7k_ZhsDA(this));
      this.setOnClickListener(new _$$Lambda$FragmentContextView$oHS8Qv6e4NDG6yqH2reqe1Dmqu0(this));
   }

   private void checkCall(boolean var1) {
      View var2 = this.fragment.getFragmentView();
      boolean var3 = var1;
      if (!var1) {
         var3 = var1;
         if (var2 != null) {
            label79: {
               if (var2.getParent() != null) {
                  var3 = var1;
                  if (((View)var2.getParent()).getVisibility() == 0) {
                     break label79;
                  }
               }

               var3 = true;
            }
         }
      }

      boolean var4;
      if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
         var4 = true;
      } else {
         var4 = false;
      }

      AnimatorSet var5;
      if (!var4) {
         if (this.visible) {
            this.visible = false;
            if (var3) {
               if (this.getVisibility() != 8) {
                  this.setVisibility(8);
               }

               this.setTopPadding(0.0F);
            } else {
               var5 = this.animatorSet;
               if (var5 != null) {
                  var5.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{0.0F})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.setVisibility(8);
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }
         }
      } else {
         this.updateStyle(1);
         FragmentContextView var6;
         if (var3 && this.topPadding == 0.0F) {
            this.setTopPadding((float)AndroidUtilities.dp2(36.0F));
            var6 = this.additionalContextView;
            if (var6 != null && var6.getVisibility() == 0) {
               ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0F);
            } else {
               ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0F);
            }

            this.yPosition = 0.0F;
         }

         if (!this.visible) {
            if (!var3) {
               var5 = this.animatorSet;
               if (var5 != null) {
                  var5.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               var6 = this.additionalContextView;
               if (var6 != null && var6.getVisibility() == 0) {
                  ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0F);
               } else {
                  ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0F);
               }

               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{(float)AndroidUtilities.dp2(36.0F)})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }

            this.visible = true;
            this.setVisibility(0);
         }
      }

   }

   private void checkLiveLocation(boolean var1) {
      View var2 = this.fragment.getFragmentView();
      boolean var3 = var1;
      if (!var1) {
         var3 = var1;
         if (var2 != null) {
            label80: {
               if (var2.getParent() != null) {
                  var3 = var1;
                  if (((View)var2.getParent()).getVisibility() == 0) {
                     break label80;
                  }
               }

               var3 = true;
            }
         }
      }

      BaseFragment var6 = this.fragment;
      if (var6 instanceof DialogsActivity) {
         if (LocationController.getLocationsCount() != 0) {
            var1 = true;
         } else {
            var1 = false;
         }
      } else {
         var1 = LocationController.getInstance(var6.getCurrentAccount()).isSharingLocation(((ChatActivity)this.fragment).getDialogId());
      }

      AnimatorSet var7;
      if (!var1) {
         this.lastLocationSharingCount = -1;
         AndroidUtilities.cancelRunOnUIThread(this.checkLocationRunnable);
         if (this.visible) {
            this.visible = false;
            if (var3) {
               if (this.getVisibility() != 8) {
                  this.setVisibility(8);
               }

               this.setTopPadding(0.0F);
            } else {
               var7 = this.animatorSet;
               if (var7 != null) {
                  var7.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{0.0F})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.setVisibility(8);
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }
         }
      } else {
         this.updateStyle(2);
         this.playButton.setImageDrawable(new ShareLocationDrawable(this.getContext(), true));
         if (var3 && this.topPadding == 0.0F) {
            this.setTopPadding((float)AndroidUtilities.dp2(36.0F));
            this.yPosition = 0.0F;
         }

         if (!this.visible) {
            if (!var3) {
               var7 = this.animatorSet;
               if (var7 != null) {
                  var7.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{(float)AndroidUtilities.dp2(36.0F)})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }

            this.visible = true;
            this.setVisibility(0);
         }

         if (this.fragment instanceof DialogsActivity) {
            String var4 = LocaleController.getString("AttachLiveLocation", 2131558721);
            ArrayList var8 = new ArrayList();

            int var5;
            for(var5 = 0; var5 < 3; ++var5) {
               var8.addAll(LocationController.getInstance(var5).sharingLocationsUI);
            }

            String var10;
            if (var8.size() == 1) {
               LocationController.SharingLocationInfo var9 = (LocationController.SharingLocationInfo)var8.get(0);
               var5 = (int)var9.messageObject.getDialogId();
               if (var5 > 0) {
                  var10 = UserObject.getFirstName(MessagesController.getInstance(var9.messageObject.currentAccount).getUser(var5));
               } else {
                  TLRPC.Chat var11 = MessagesController.getInstance(var9.messageObject.currentAccount).getChat(-var5);
                  if (var11 != null) {
                     var10 = var11.title;
                  } else {
                     var10 = "";
                  }
               }
            } else {
               var10 = LocaleController.formatPluralString("Chats", var8.size());
            }

            var10 = String.format(LocaleController.getString("AttachLiveLocationIsSharing", 2131558722), var4, var10);
            var5 = var10.indexOf(var4);
            SpannableStringBuilder var12 = new SpannableStringBuilder(var10);
            this.titleTextView.setEllipsize(TruncateAt.END);
            var12.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), var5, var4.length() + var5, 18);
            this.titleTextView.setText(var12);
         } else {
            this.checkLocationRunnable.run();
            this.checkLocationString();
         }
      }

   }

   private void checkLocationString() {
      BaseFragment var1 = this.fragment;
      if (var1 instanceof ChatActivity && this.titleTextView != null) {
         ChatActivity var14 = (ChatActivity)var1;
         long var2 = var14.getDialogId();
         int var4 = var14.getCurrentAccount();
         ArrayList var5 = (ArrayList)LocationController.getInstance(var4).locationsCache.get(var2);
         if (!this.firstLocationsLoaded) {
            LocationController.getInstance(var4).loadLiveLocations(var2);
            this.firstLocationsLoaded = true;
         }

         TLRPC.User var15 = null;
         int var9;
         if (var5 != null) {
            int var6 = UserConfig.getInstance(var4).getClientUserId();
            int var7 = ConnectionsManager.getInstance(var4).getCurrentTime();
            var15 = null;
            int var8 = 0;

            TLRPC.User var13;
            for(var9 = 0; var8 < var5.size(); var15 = var13) {
               TLRPC.Message var10 = (TLRPC.Message)var5.get(var8);
               TLRPC.MessageMedia var11 = var10.media;
               int var12;
               if (var11 == null) {
                  var12 = var9;
                  var13 = var15;
               } else {
                  var12 = var9;
                  var13 = var15;
                  if (var10.date + var11.period > var7) {
                     var13 = var15;
                     if (var15 == null) {
                        var13 = var15;
                        if (var10.from_id != var6) {
                           var13 = MessagesController.getInstance(var4).getUser(var10.from_id);
                        }
                     }

                     var12 = var9 + 1;
                  }
               }

               ++var8;
               var9 = var12;
            }
         } else {
            var9 = 0;
         }

         if (this.lastLocationSharingCount == var9) {
            return;
         }

         this.lastLocationSharingCount = var9;
         String var19 = LocaleController.getString("AttachLiveLocation", 2131558721);
         String var16;
         if (var9 == 0) {
            var16 = var19;
         } else {
            --var9;
            if (LocationController.getInstance(var4).isSharingLocation(var2)) {
               if (var9 != 0) {
                  if (var9 == 1 && var15 != null) {
                     var16 = String.format("%1$s - %2$s", var19, LocaleController.formatString("SharingYouAndOtherName", 2131560773, UserObject.getFirstName(var15)));
                  } else {
                     var16 = String.format("%1$s - %2$s %3$s", var19, LocaleController.getString("ChatYourSelfName", 2131559050), LocaleController.formatPluralString("AndOther", var9));
                  }
               } else {
                  var16 = String.format("%1$s - %2$s", var19, LocaleController.getString("ChatYourSelfName", 2131559050));
               }
            } else if (var9 != 0) {
               var16 = String.format("%1$s - %2$s %3$s", var19, UserObject.getFirstName(var15), LocaleController.formatPluralString("AndOther", var9));
            } else {
               var16 = String.format("%1$s - %2$s", var19, UserObject.getFirstName(var15));
            }
         }

         String var17 = this.lastString;
         if (var17 != null && var16.equals(var17)) {
            return;
         }

         this.lastString = var16;
         var9 = var16.indexOf(var19);
         SpannableStringBuilder var18 = new SpannableStringBuilder(var16);
         this.titleTextView.setEllipsize(TruncateAt.END);
         if (var9 >= 0) {
            var18.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), var9, var19.length() + var9, 18);
         }

         this.titleTextView.setText(var18);
      }

   }

   private void checkPlayer(boolean var1) {
      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      View var3 = this.fragment.getFragmentView();
      boolean var4 = var1;
      if (!var1) {
         var4 = var1;
         if (var3 != null) {
            label121: {
               if (var3.getParent() != null) {
                  var4 = var1;
                  if (((View)var3.getParent()).getVisibility() == 0) {
                     break label121;
                  }
               }

               var4 = true;
            }
         }
      }

      AnimatorSet var6;
      if (var2 != null && var2.getId() != 0 && !var2.isVideo()) {
         int var10 = this.currentStyle;
         this.updateStyle(0);
         FragmentContextView var7;
         if (var4 && this.topPadding == 0.0F) {
            this.setTopPadding((float)AndroidUtilities.dp2(36.0F));
            var7 = this.additionalContextView;
            if (var7 != null && var7.getVisibility() == 0) {
               ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0F);
            } else {
               ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0F);
            }

            this.yPosition = 0.0F;
         }

         if (!this.visible) {
            if (!var4) {
               var6 = this.animatorSet;
               if (var6 != null) {
                  var6.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               var7 = this.additionalContextView;
               if (var7 != null && var7.getVisibility() == 0) {
                  ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0F);
               } else {
                  ((LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0F);
               }

               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{(float)AndroidUtilities.dp2(36.0F)})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }

            this.visible = true;
            this.setVisibility(0);
         }

         if (MediaController.getInstance().isMessagePaused()) {
            this.playButton.setImageResource(2131165606);
            this.playButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
         } else {
            this.playButton.setImageResource(2131165605);
            this.playButton.setContentDescription(LocaleController.getString("AccActionPause", 2131558408));
         }

         if (this.lastMessageObject != var2 || var10 != 0) {
            this.lastMessageObject = var2;
            ImageView var8;
            SpannableStringBuilder var9;
            if (!this.lastMessageObject.isVoice() && !this.lastMessageObject.isRoundVideo()) {
               var8 = this.playbackSpeedButton;
               if (var8 != null) {
                  var8.setAlpha(0.0F);
                  this.playbackSpeedButton.setEnabled(false);
               }

               this.titleTextView.setPadding(0, 0, 0, 0);
               var9 = new SpannableStringBuilder(String.format("%s - %s", var2.getMusicAuthor(), var2.getMusicTitle()));
               this.titleTextView.setEllipsize(TruncateAt.END);
            } else {
               var8 = this.playbackSpeedButton;
               if (var8 != null) {
                  var8.setAlpha(1.0F);
                  this.playbackSpeedButton.setEnabled(true);
               }

               this.titleTextView.setPadding(0, 0, AndroidUtilities.dp(44.0F), 0);
               var9 = new SpannableStringBuilder(String.format("%s %s", var2.getMusicAuthor(), var2.getMusicTitle()));
               this.titleTextView.setEllipsize(TruncateAt.MIDDLE);
            }

            var9.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), 0, var2.getMusicAuthor().length(), 18);
            this.titleTextView.setText(var9);
         }
      } else {
         this.lastMessageObject = null;
         boolean var5;
         if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5) {
            this.checkCall(false);
            return;
         }

         if (this.visible) {
            this.visible = false;
            if (var4) {
               if (this.getVisibility() != 8) {
                  this.setVisibility(8);
               }

               this.setTopPadding(0.0F);
            } else {
               var6 = this.animatorSet;
               if (var6 != null) {
                  var6.cancel();
                  this.animatorSet = null;
               }

               this.animatorSet = new AnimatorSet();
               this.animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "topPadding", new float[]{0.0F})});
               this.animatorSet.setDuration(200L);
               this.animatorSet.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(var1)) {
                        FragmentContextView.this.setVisibility(8);
                        FragmentContextView.this.animatorSet = null;
                     }

                  }
               });
               this.animatorSet.start();
            }
         }
      }

   }

   private void checkVisibility() {
      boolean var2;
      byte var3;
      label32: {
         boolean var1 = this.isLocation;
         var2 = true;
         var3 = 0;
         if (var1) {
            BaseFragment var4 = this.fragment;
            if (!(var4 instanceof DialogsActivity)) {
               var2 = LocationController.getInstance(var4.getCurrentAccount()).isSharingLocation(((ChatActivity)this.fragment).getDialogId());
               break label32;
            }

            if (LocationController.getLocationsCount() != 0) {
               break label32;
            }
         } else {
            if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
               break label32;
            }

            MessageObject var5 = MediaController.getInstance().getPlayingMessageObject();
            if (var5 != null && var5.getId() != 0) {
               break label32;
            }
         }

         var2 = false;
      }

      if (!var2) {
         var3 = 8;
      }

      this.setVisibility(var3);
   }

   // $FF: synthetic method
   public static void lambda$Z72HHKSvAYjXtWgaFaDIC5DAHI8(FragmentContextView var0, LocationController.SharingLocationInfo var1) {
      var0.openSharingLocation(var1);
   }

   // $FF: synthetic method
   static void lambda$openSharingLocation$5(LocationController.SharingLocationInfo var0, long var1, TLRPC.MessageMedia var3, int var4) {
      SendMessagesHelper.getInstance(var0.messageObject.currentAccount).sendMessage((TLRPC.MessageMedia)var3, var1, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
   }

   private void openSharingLocation(LocationController.SharingLocationInfo var1) {
      if (var1 != null && this.fragment.getParentActivity() != null) {
         LaunchActivity var2 = (LaunchActivity)this.fragment.getParentActivity();
         var2.switchToAccount(var1.messageObject.currentAccount, true);
         LocationActivity var3 = new LocationActivity(2);
         var3.setMessageObject(var1.messageObject);
         var3.setDelegate(new _$$Lambda$FragmentContextView$qSdNXrOdRWHj2Hn2uLFHHQL36iI(var1, var1.messageObject.getDialogId()));
         var2.presentFragment(var3);
      }

   }

   private void updatePlaybackButton() {
      if (MediaController.getInstance().getPlaybackSpeed() > 1.0F) {
         this.playbackSpeedButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("inappPlayerPlayPause"), Mode.MULTIPLY));
      } else {
         this.playbackSpeedButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("inappPlayerClose"), Mode.MULTIPLY));
      }

   }

   private void updateStyle(int var1) {
      if (this.currentStyle != var1) {
         this.currentStyle = var1;
         ImageView var2;
         if (var1 != 0 && var1 != 2) {
            if (var1 == 1) {
               this.titleTextView.setText(LocaleController.getString("ReturnToCall", 2131560615));
               this.frameLayout.setBackgroundColor(Theme.getColor("returnToCallBackground"));
               this.frameLayout.setTag("returnToCallBackground");
               this.titleTextView.setTextColor(Theme.getColor("returnToCallText"));
               this.titleTextView.setTag("returnToCallText");
               this.closeButton.setVisibility(8);
               this.playButton.setVisibility(8);
               this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
               this.titleTextView.setTextSize(1, 14.0F);
               this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, 2.0F));
               this.titleTextView.setPadding(0, 0, 0, 0);
               var2 = this.playbackSpeedButton;
               if (var2 != null) {
                  var2.setVisibility(8);
               }
            }
         } else {
            this.frameLayout.setBackgroundColor(Theme.getColor("inappPlayerBackground"));
            this.frameLayout.setTag("inappPlayerBackground");
            this.titleTextView.setTextColor(Theme.getColor("inappPlayerTitle"));
            this.titleTextView.setTag("inappPlayerTitle");
            this.closeButton.setVisibility(0);
            this.playButton.setVisibility(0);
            this.titleTextView.setTypeface(Typeface.DEFAULT);
            this.titleTextView.setTextSize(1, 15.0F);
            if (var1 == 0) {
               this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0F, 51, 35.0F, 0.0F, 36.0F, 0.0F));
               var2 = this.playbackSpeedButton;
               if (var2 != null) {
                  var2.setVisibility(0);
               }

               this.closeButton.setContentDescription(LocaleController.getString("AccDescrClosePlayer", 2131558427));
            } else if (var1 == 2) {
               this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0F, 51, 8.0F, 0.0F, 0.0F, 0.0F));
               this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0F, 51, 51.0F, 0.0F, 36.0F, 0.0F));
               this.closeButton.setContentDescription(LocaleController.getString("AccDescrStopLiveLocation", 2131558477));
            }
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.liveLocationsChanged) {
         this.checkLiveLocation(false);
      } else if (var1 == NotificationCenter.liveLocationsCacheChanged) {
         if (this.fragment instanceof ChatActivity) {
            long var4 = (Long)var3[0];
            if (((ChatActivity)this.fragment).getDialogId() == var4) {
               this.checkLocationString();
            }
         }
      } else if (var1 != NotificationCenter.messagePlayingDidStart && var1 != NotificationCenter.messagePlayingPlayStateChanged && var1 != NotificationCenter.messagePlayingDidReset && var1 != NotificationCenter.didEndedCall) {
         if (var1 == NotificationCenter.didStartedCall) {
            this.checkCall(false);
         } else {
            this.checkPlayer(false);
         }
      } else {
         this.checkPlayer(false);
      }

   }

   public float getTopPadding() {
      return this.topPadding;
   }

   // $FF: synthetic method
   public void lambda$new$0$FragmentContextView(View var1) {
      if (this.currentStyle == 0) {
         if (MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
         } else {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$FragmentContextView(View var1) {
      if (MediaController.getInstance().getPlaybackSpeed() > 1.0F) {
         MediaController.getInstance().setPlaybackSpeed(1.0F);
      } else {
         MediaController.getInstance().setPlaybackSpeed(1.8F);
      }

      this.updatePlaybackButton();
   }

   // $FF: synthetic method
   public void lambda$new$3$FragmentContextView(View var1) {
      if (this.currentStyle == 2) {
         AlertDialog.Builder var4 = new AlertDialog.Builder(this.fragment.getParentActivity());
         var4.setTitle(LocaleController.getString("AppName", 2131558635));
         BaseFragment var2 = this.fragment;
         if (var2 instanceof DialogsActivity) {
            var4.setMessage(LocaleController.getString("StopLiveLocationAlertAll", 2131560824));
         } else {
            ChatActivity var3 = (ChatActivity)var2;
            TLRPC.Chat var5 = var3.getCurrentChat();
            TLRPC.User var6 = var3.getCurrentUser();
            if (var5 != null) {
               var4.setMessage(LocaleController.formatString("StopLiveLocationAlertToGroup", 2131560825, var5.title));
            } else if (var6 != null) {
               var4.setMessage(LocaleController.formatString("StopLiveLocationAlertToUser", 2131560826, UserObject.getFirstName(var6)));
            } else {
               var4.setMessage(LocaleController.getString("AreYouSure", 2131558666));
            }
         }

         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$FragmentContextView$n_uFawyX6mh1KEybpEHLF_yq7cs(this));
         var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var4.show();
      } else {
         MediaController.getInstance().cleanupPlayer(true, true);
      }

   }

   // $FF: synthetic method
   public void lambda$new$4$FragmentContextView(View var1) {
      int var2 = this.currentStyle;
      long var3 = 0L;
      int var6;
      if (var2 == 0) {
         MessageObject var7 = MediaController.getInstance().getPlayingMessageObject();
         if (this.fragment != null && var7 != null) {
            if (var7.isMusic()) {
               this.fragment.showDialog(new AudioPlayerAlert(this.getContext()));
            } else {
               BaseFragment var5 = this.fragment;
               if (var5 instanceof ChatActivity) {
                  var3 = ((ChatActivity)var5).getDialogId();
               }

               if (var7.getDialogId() == var3) {
                  ((ChatActivity)this.fragment).scrollToMessageId(var7.getId(), 0, false, 0, true);
               } else {
                  var3 = var7.getDialogId();
                  Bundle var11 = new Bundle();
                  var2 = (int)var3;
                  var6 = (int)(var3 >> 32);
                  if (var2 != 0) {
                     if (var6 == 1) {
                        var11.putInt("chat_id", var2);
                     } else if (var2 > 0) {
                        var11.putInt("user_id", var2);
                     } else if (var2 < 0) {
                        var11.putInt("chat_id", -var2);
                     }
                  } else {
                     var11.putInt("enc_id", var6);
                  }

                  var11.putInt("message_id", var7.getId());
                  this.fragment.presentFragment(new ChatActivity(var11), this.fragment instanceof ChatActivity);
               }
            }
         }
      } else if (var2 == 1) {
         Intent var8 = new Intent(this.getContext(), VoIPActivity.class);
         var8.addFlags(805306368);
         this.getContext().startActivity(var8);
      } else if (var2 == 2) {
         var6 = UserConfig.selectedAccount;
         BaseFragment var9 = this.fragment;
         if (var9 instanceof ChatActivity) {
            var3 = ((ChatActivity)var9).getDialogId();
            var2 = this.fragment.getCurrentAccount();
         } else {
            label69: {
               if (LocationController.getLocationsCount() == 1) {
                  for(var2 = 0; var2 < 3; ++var2) {
                     if (!LocationController.getInstance(var2).sharingLocationsUI.isEmpty()) {
                        LocationController.SharingLocationInfo var10 = (LocationController.SharingLocationInfo)LocationController.getInstance(var2).sharingLocationsUI.get(0);
                        var3 = var10.did;
                        var2 = var10.messageObject.currentAccount;
                        break label69;
                     }
                  }
               }

               var3 = 0L;
               var2 = var6;
            }
         }

         if (var3 != 0L) {
            this.openSharingLocation(LocationController.getInstance(var2).getSharingLocationInfo(var3));
         } else {
            this.fragment.showDialog(new SharingLocationsAlert(this.getContext(), new _$$Lambda$FragmentContextView$Z72HHKSvAYjXtWgaFaDIC5DAHI8(this)));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$FragmentContextView(DialogInterface var1, int var2) {
      BaseFragment var3 = this.fragment;
      if (var3 instanceof DialogsActivity) {
         for(var2 = 0; var2 < 3; ++var2) {
            LocationController.getInstance(var2).removeAllLocationSharings();
         }
      } else {
         LocationController.getInstance(var3.getCurrentAccount()).removeSharingLocation(((ChatActivity)this.fragment).getDialogId());
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      FragmentContextView var1;
      if (this.isLocation) {
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsCacheChanged);
         var1 = this.additionalContextView;
         if (var1 != null) {
            var1.checkVisibility();
         }

         this.checkLiveLocation(true);
      } else {
         for(int var2 = 0; var2 < 3; ++var2) {
            NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(var2).addObserver(this, NotificationCenter.messagePlayingDidStart);
         }

         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didStartedCall);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndedCall);
         var1 = this.additionalContextView;
         if (var1 != null) {
            var1.checkVisibility();
         }

         if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
            this.checkCall(true);
         } else {
            this.checkPlayer(true);
            this.updatePlaybackButton();
         }
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.topPadding = 0.0F;
      if (this.isLocation) {
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsCacheChanged);
      } else {
         for(int var1 = 0; var1 < 3; ++var1) {
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.messagePlayingDidStart);
         }

         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didStartedCall);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndedCall);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, AndroidUtilities.dp2(39.0F));
   }

   public void setAdditionalContextView(FragmentContextView var1) {
      this.additionalContextView = var1;
   }

   @Keep
   public void setTopPadding(float var1) {
      this.topPadding = var1;
      if (this.fragment != null && this.getParent() != null) {
         View var2 = this.fragment.getFragmentView();
         this.fragment.getActionBar();
         FragmentContextView var3 = this.additionalContextView;
         int var4;
         if (var3 != null && var3.getVisibility() == 0 && this.additionalContextView.getParent() != null) {
            var4 = AndroidUtilities.dp(36.0F);
         } else {
            var4 = 0;
         }

         if (var2 != null && this.getParent() != null) {
            var2.setPadding(0, (int)this.topPadding + var4, 0, 0);
         }

         if (this.isLocation) {
            var3 = this.additionalContextView;
            if (var3 != null) {
               ((LayoutParams)var3.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0F) - (int)this.topPadding;
            }
         }
      }

   }
}
