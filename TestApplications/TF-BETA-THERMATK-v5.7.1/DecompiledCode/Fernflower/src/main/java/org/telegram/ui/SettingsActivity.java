package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.SettingsSearchCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.ImageUpdater$ImageUpdaterDelegate$_CC;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.VoIPHelper;

public class SettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ImageUpdater.ImageUpdaterDelegate {
   private static final int edit_name = 1;
   private static final int logout = 2;
   private static final int search_button = 3;
   private TLRPC.FileLocation avatar;
   private AnimatorSet avatarAnimation;
   private TLRPC.FileLocation avatarBig;
   private FrameLayout avatarContainer;
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImage;
   private View avatarOverlay;
   private RadialProgressView avatarProgressView;
   private int bioRow;
   private int chatRow;
   private int dataRow;
   private EmptyTextProgressView emptyView;
   private int extraHeight;
   private View extraHeightView;
   private int helpRow;
   private ImageUpdater imageUpdater;
   private int languageRow;
   private LinearLayoutManager layoutManager;
   private SettingsActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private TextView nameTextView;
   private int notificationRow;
   private int numberRow;
   private int numberSectionRow;
   private TextView onlineTextView;
   private ActionBarMenuItem otherItem;
   private int overscrollRow;
   private int privacyRow;
   private PhotoViewer.PhotoViewerProvider provider = new PhotoViewer.EmptyPhotoViewerProvider() {
      public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
         if (var2 == null) {
            return null;
         } else {
            TLRPC.User var6 = MessagesController.getInstance(SettingsActivity.access$100(SettingsActivity.this)).getUser(UserConfig.getInstance(SettingsActivity.access$000(SettingsActivity.this)).getClientUserId());
            if (var6 != null) {
               TLRPC.UserProfilePhoto var7 = var6.photo;
               if (var7 != null) {
                  TLRPC.FileLocation var8 = var7.photo_big;
                  if (var8 != null && var8.local_id == var2.local_id && var8.volume_id == var2.volume_id && var8.dc_id == var2.dc_id) {
                     int[] var9 = new int[2];
                     SettingsActivity.this.avatarImage.getLocationInWindow(var9);
                     PhotoViewer.PlaceProviderObject var10 = new PhotoViewer.PlaceProviderObject();
                     var3 = 0;
                     var10.viewX = var9[0];
                     int var5 = var9[1];
                     if (VERSION.SDK_INT < 21) {
                        var3 = AndroidUtilities.statusBarHeight;
                     }

                     var10.viewY = var5 - var3;
                     var10.parentView = SettingsActivity.this.avatarImage;
                     var10.imageReceiver = SettingsActivity.this.avatarImage.getImageReceiver();
                     var10.dialogId = UserConfig.getInstance(SettingsActivity.access$300(SettingsActivity.this)).getClientUserId();
                     var10.thumb = var10.imageReceiver.getBitmapSafe();
                     var10.size = -1;
                     var10.radius = SettingsActivity.this.avatarImage.getImageReceiver().getRoundRadius();
                     var10.scale = SettingsActivity.this.avatarContainer.getScaleX();
                     return var10;
                  }
               }
            }

            return null;
         }
      }

      public void willHidePhotoViewer() {
         SettingsActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
      }
   };
   private int rowCount;
   private SettingsActivity.SearchAdapter searchAdapter;
   private int settingsSectionRow;
   private int settingsSectionRow2;
   private View shadowView;
   private TLRPC.UserFull userInfo;
   private int usernameRow;
   private int versionRow;
   private ImageView writeButton;
   private AnimatorSet writeButtonAnimation;

   // $FF: synthetic method
   static int access$000(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$100(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static View access$1400(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$1500(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$1800(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$1900(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static int access$2100(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2200(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2600(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2800(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$300(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static View access$3400(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$3500(SettingsActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static ActionBarLayout access$3600(SettingsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$3700(SettingsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$4100(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6000(SettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6200(SettingsActivity var0) {
      return var0.currentAccount;
   }

   private void fixLayout() {
      View var1 = super.fragmentView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               if (SettingsActivity.access$3400(SettingsActivity.this) != null) {
                  SettingsActivity.this.needLayout();
                  SettingsActivity.access$3500(SettingsActivity.this).getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }
   }

   private void needLayout() {
      final boolean var1 = super.actionBar.getOccupyStatusBar();
      byte var2 = 0;
      int var3;
      if (var1) {
         var3 = AndroidUtilities.statusBarHeight;
      } else {
         var3 = 0;
      }

      int var4 = var3 + ActionBar.getCurrentActionBarHeight();
      RecyclerListView var5 = this.listView;
      if (var5 != null) {
         LayoutParams var13 = (LayoutParams)var5.getLayoutParams();
         if (var13.topMargin != var4) {
            var13.topMargin = var4;
            this.listView.setLayoutParams(var13);
            this.extraHeightView.setTranslationY((float)var4);
         }

         var13 = (LayoutParams)this.emptyView.getLayoutParams();
         if (var13.topMargin != var4) {
            var13.topMargin = var4;
            this.emptyView.setLayoutParams(var13);
         }
      }

      FrameLayout var14 = this.avatarContainer;
      if (var14 != null) {
         if (var14.getVisibility() == 0) {
            var3 = this.extraHeight;
         } else {
            var3 = 0;
         }

         float var6 = (float)var3 / (float)AndroidUtilities.dp(88.0F);
         this.extraHeightView.setScaleY(var6);
         this.shadowView.setTranslationY((float)(var4 + var3));
         ImageView var15 = this.writeButton;
         if (super.actionBar.getOccupyStatusBar()) {
            var4 = AndroidUtilities.statusBarHeight;
         } else {
            var4 = 0;
         }

         var15.setTranslationY((float)(var4 + ActionBar.getCurrentActionBarHeight() + var3 - AndroidUtilities.dp(29.5F)));
         if (var6 > 0.2F) {
            var1 = true;
         } else {
            var1 = false;
         }

         boolean var7;
         if (this.writeButton.getTag() == null) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var1 != var7) {
            if (var1) {
               this.writeButton.setTag((Object)null);
               this.writeButton.setVisibility(0);
            } else {
               this.writeButton.setTag(0);
            }

            AnimatorSet var16 = this.writeButtonAnimation;
            if (var16 != null) {
               this.writeButtonAnimation = null;
               var16.cancel();
            }

            this.writeButtonAnimation = new AnimatorSet();
            if (var1) {
               this.writeButtonAnimation.setInterpolator(new DecelerateInterpolator());
               this.writeButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.writeButton, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.writeButton, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.writeButton, "alpha", new float[]{1.0F})});
            } else {
               this.writeButtonAnimation.setInterpolator(new AccelerateInterpolator());
               this.writeButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.writeButton, "scaleX", new float[]{0.2F}), ObjectAnimator.ofFloat(this.writeButton, "scaleY", new float[]{0.2F}), ObjectAnimator.ofFloat(this.writeButton, "alpha", new float[]{0.0F})});
            }

            this.writeButtonAnimation.setDuration(150L);
            this.writeButtonAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  if (SettingsActivity.this.writeButtonAnimation != null && SettingsActivity.this.writeButtonAnimation.equals(var1x)) {
                     ImageView var3 = SettingsActivity.this.writeButton;
                     byte var2;
                     if (var1) {
                        var2 = 0;
                     } else {
                        var2 = 8;
                     }

                     var3.setVisibility(var2);
                     SettingsActivity.this.writeButtonAnimation = null;
                  }

               }
            });
            this.writeButtonAnimation.start();
         }

         var14 = this.avatarContainer;
         float var8 = (18.0F * var6 + 42.0F) / 42.0F;
         var14.setScaleX(var8);
         this.avatarContainer.setScaleY(var8);
         this.avatarProgressView.setSize(AndroidUtilities.dp(26.0F / this.avatarContainer.getScaleX()));
         this.avatarProgressView.setStrokeWidth(3.0F / this.avatarContainer.getScaleX());
         var3 = var2;
         if (super.actionBar.getOccupyStatusBar()) {
            var3 = AndroidUtilities.statusBarHeight;
         }

         float var9 = (float)var3;
         var8 = (float)ActionBar.getCurrentActionBarHeight() / 2.0F;
         float var10 = AndroidUtilities.density;
         var14 = this.avatarContainer;
         double var11 = (double)(var9 + var8 * (var6 + 1.0F) - 21.0F * var10 + var10 * 27.0F * var6);
         var14.setTranslationY((float)Math.ceil(var11));
         this.nameTextView.setTranslationY((float)Math.floor(var11) - (float)Math.ceil((double)AndroidUtilities.density) + (float)Math.floor((double)(AndroidUtilities.density * 7.0F * var6)));
         this.onlineTextView.setTranslationY((float)Math.floor(var11) + (float)AndroidUtilities.dp(22.0F) + (float)Math.floor((double)(AndroidUtilities.density * 11.0F)) * var6);
         TextView var17 = this.nameTextView;
         var8 = 0.12F * var6 + 1.0F;
         var17.setScaleX(var8);
         this.nameTextView.setScaleY(var8);
         if (LocaleController.isRTL) {
            this.avatarContainer.setTranslationX((float)AndroidUtilities.dp(95.0F) * var6);
            this.nameTextView.setTranslationX(AndroidUtilities.density * 69.0F * var6);
            this.onlineTextView.setTranslationX(AndroidUtilities.density * 69.0F * var6);
         } else {
            this.avatarContainer.setTranslationX((float)(-AndroidUtilities.dp(47.0F)) * var6);
            this.nameTextView.setTranslationX(AndroidUtilities.density * -21.0F * var6);
            this.onlineTextView.setTranslationX(AndroidUtilities.density * -21.0F * var6);
         }
      }

   }

   private void sendLogs() {
      if (this.getParentActivity() != null) {
         AlertDialog var1 = new AlertDialog(this.getParentActivity(), 3);
         var1.setCanCacnel(false);
         var1.show();
         Utilities.globalQueue.postRunnable(new _$$Lambda$SettingsActivity$fDRhZsppR_aQC0aOePrWJpUZ93Y(this, var1));
      }
   }

   private void showAvatarProgress(final boolean var1, boolean var2) {
      if (this.avatarProgressView != null) {
         AnimatorSet var3 = this.avatarAnimation;
         if (var3 != null) {
            var3.cancel();
            this.avatarAnimation = null;
         }

         if (var2) {
            this.avatarAnimation = new AnimatorSet();
            if (var1) {
               this.avatarProgressView.setVisibility(0);
               this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{1.0F})});
            } else {
               this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{0.0F})});
            }

            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  SettingsActivity.this.avatarAnimation = null;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (SettingsActivity.this.avatarAnimation != null && SettingsActivity.this.avatarProgressView != null) {
                     if (!var1) {
                        SettingsActivity.this.avatarProgressView.setVisibility(4);
                     }

                     SettingsActivity.this.avatarAnimation = null;
                  }

               }
            });
            this.avatarAnimation.start();
         } else if (var1) {
            this.avatarProgressView.setAlpha(1.0F);
            this.avatarProgressView.setVisibility(0);
         } else {
            this.avatarProgressView.setAlpha(0.0F);
            this.avatarProgressView.setVisibility(4);
         }

      }
   }

   private void showHelpAlert() {
      if (this.getParentActivity() != null) {
         Activity var1 = this.getParentActivity();
         BottomSheet.Builder var2 = new BottomSheet.Builder(var1);
         var2.setApplyTopPadding(false);
         LinearLayout var3 = new LinearLayout(var1);
         var3.setOrientation(1);
         HeaderCell var4 = new HeaderCell(var1, true, 23, 15, false);
         var4.setHeight(47);
         var4.setText(LocaleController.getString("SettingsHelp", 2131560740));
         var3.addView(var4);
         LinearLayout var5 = new LinearLayout(var1);
         var5.setOrientation(1);
         var3.addView(var5, LayoutHelper.createLinear(-1, -2));

         for(int var6 = 0; var6 < 6; ++var6) {
            if ((var6 < 3 || var6 > 4 || BuildVars.LOGS_ENABLED) && (var6 != 5 || BuildVars.DEBUG_VERSION)) {
               TextCell var7 = new TextCell(var1);
               String var9;
               if (var6 != 0) {
                  if (var6 != 1) {
                     if (var6 != 2) {
                        if (var6 != 3) {
                           if (var6 != 4) {
                              var9 = "Switch Backend";
                           } else {
                              var9 = LocaleController.getString("DebugClearLogs", 2131559208);
                           }
                        } else {
                           var9 = LocaleController.getString("DebugSendLogs", 2131559221);
                        }
                     } else {
                        var9 = LocaleController.getString("PrivacyPolicy", 2131560502);
                     }
                  } else {
                     var9 = LocaleController.getString("TelegramFAQ", 2131560869);
                  }
               } else {
                  var9 = LocaleController.getString("AskAQuestion", 2131558706);
               }

               boolean var8;
               label59: {
                  label74: {
                     if (!BuildVars.LOGS_ENABLED && !BuildVars.DEBUG_VERSION) {
                        if (var6 != 2) {
                           break label74;
                        }
                     } else if (var6 != 5) {
                        break label74;
                     }

                     var8 = false;
                     break label59;
                  }

                  var8 = true;
               }

               var7.setText(var9, var8);
               var7.setTag(var6);
               var7.setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var5.addView(var7, LayoutHelper.createLinear(-1, -2));
               var7.setOnClickListener(new _$$Lambda$SettingsActivity$z3K3WuiCS2o_E03FhC5NesCF_7c(this, var2));
            }
         }

         var2.setCustomView(var3);
         this.showDialog(var2.create());
      }
   }

   private void updateUserData() {
      TLRPC.User var1 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
      if (var1 != null) {
         TLRPC.FileLocation var2 = null;
         TLRPC.UserProfilePhoto var3 = var1.photo;
         if (var3 != null) {
            var2 = var3.photo_big;
         }

         this.avatarDrawable = new AvatarDrawable(var1, true);
         this.avatarDrawable.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
         BackupImageView var4 = this.avatarImage;
         if (var4 != null) {
            var4.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(var2) ^ true, false);
            this.nameTextView.setText(UserObject.getUserName(var1));
            this.onlineTextView.setText(LocaleController.getString("Online", 2131560100));
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(var2) ^ true, false);
         }

      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      super.actionBar.setItemsBackgroundColor(Theme.getColor("avatar_actionBarSelectorBlue"), false);
      super.actionBar.setItemsColor(Theme.getColor("avatar_actionBarIconBlue"), false);
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAddToContainer(false);
      this.extraHeight = 88;
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               SettingsActivity.this.finishFragment();
            } else if (var1 == 1) {
               SettingsActivity.this.presentFragment(new ChangeNameActivity());
            } else if (var1 == 2) {
               SettingsActivity.this.presentFragment(new LogoutActivity());
            }

         }
      });
      ActionBarMenu var2 = super.actionBar.createMenu();
      ActionBarMenuItem var3 = var2.addItem(3, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            if (SettingsActivity.this.otherItem != null) {
               SettingsActivity.this.otherItem.setVisibility(0);
            }

            SettingsActivity.this.listView.setAdapter(SettingsActivity.this.listAdapter);
            SettingsActivity.this.listView.setEmptyView((View)null);
            SettingsActivity.this.emptyView.setVisibility(8);
            SettingsActivity.this.avatarContainer.setVisibility(0);
            SettingsActivity.this.writeButton.setVisibility(0);
            SettingsActivity.this.nameTextView.setVisibility(0);
            SettingsActivity.this.onlineTextView.setVisibility(0);
            SettingsActivity.this.extraHeightView.setVisibility(0);
            SettingsActivity.access$1800(SettingsActivity.this).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            SettingsActivity.access$1900(SettingsActivity.this).setTag("windowBackgroundGray");
            SettingsActivity.this.needLayout();
         }

         public void onSearchExpand() {
            if (SettingsActivity.this.otherItem != null) {
               SettingsActivity.this.otherItem.setVisibility(8);
            }

            SettingsActivity.this.searchAdapter.loadFaqWebPage();
            SettingsActivity.this.listView.setAdapter(SettingsActivity.this.searchAdapter);
            SettingsActivity.this.listView.setEmptyView(SettingsActivity.this.emptyView);
            SettingsActivity.this.avatarContainer.setVisibility(8);
            SettingsActivity.this.writeButton.setVisibility(8);
            SettingsActivity.this.nameTextView.setVisibility(8);
            SettingsActivity.this.onlineTextView.setVisibility(8);
            SettingsActivity.this.extraHeightView.setVisibility(8);
            SettingsActivity.access$1400(SettingsActivity.this).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            SettingsActivity.access$1500(SettingsActivity.this).setTag("windowBackgroundWhite");
            SettingsActivity.this.needLayout();
         }

         public void onTextChanged(EditText var1) {
            SettingsActivity.this.searchAdapter.search(var1.getText().toString().toLowerCase());
         }
      });
      var3.setContentDescription(LocaleController.getString("SearchInSettings", 2131560653));
      var3.setSearchFieldHint(LocaleController.getString("SearchInSettings", 2131560653));
      this.otherItem = var2.addItem(0, 2131165416);
      this.otherItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      this.otherItem.addSubItem(1, 2131165625, LocaleController.getString("EditName", 2131559326));
      this.otherItem.addSubItem(2, 2131165639, LocaleController.getString("LogOut", 2131559783));
      int var4;
      int var5;
      Object var22;
      if (this.listView != null) {
         var4 = this.layoutManager.findFirstVisibleItemPosition();
         View var20 = this.layoutManager.findViewByPosition(var4);
         if (var20 != null) {
            var5 = var20.getTop();
         } else {
            var4 = -1;
            var5 = 0;
         }

         var22 = this.writeButton.getTag();
      } else {
         var22 = null;
         var4 = -1;
         var5 = 0;
      }

      this.listAdapter = new SettingsActivity.ListAdapter(var1);
      this.searchAdapter = new SettingsActivity.SearchAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      super.fragmentView.setTag("windowBackgroundGray");
      FrameLayout var6 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var7 = this.listView;
      LinearLayoutManager var18 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var18;
      var7.setLayoutManager(var18);
      this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      var6.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$SettingsActivity$iv7dvdiS8exd3Bpi9ZCm9_zKqgo(this)));
      this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() {
         private int pressCount = 0;

         // $FF: synthetic method
         public void lambda$onItemClick$0$SettingsActivity$5(DialogInterface var1, int var2) {
            SettingsActivity.this.searchAdapter.clearRecent();
         }

         // $FF: synthetic method
         public void lambda$onItemClick$1$SettingsActivity$5(DialogInterface var1, int var2) {
            if (var2 == 0) {
               UserConfig.getInstance(SettingsActivity.access$2100(SettingsActivity.this)).syncContacts = true;
               UserConfig.getInstance(SettingsActivity.access$2200(SettingsActivity.this)).saveConfig(false);
               ContactsController.getInstance(SettingsActivity.access$2300(SettingsActivity.this)).forceImportContacts();
            } else if (var2 == 1) {
               ContactsController.getInstance(SettingsActivity.access$2400(SettingsActivity.this)).loadContacts(false, 0);
            } else if (var2 == 2) {
               ContactsController.getInstance(SettingsActivity.access$2500(SettingsActivity.this)).resetImportedContacts();
            } else if (var2 == 3) {
               MessagesController.getInstance(SettingsActivity.access$2600(SettingsActivity.this)).forceResetDialogs();
            } else if (var2 == 4) {
               BuildVars.LOGS_ENABLED ^= true;
               ApplicationLoader.applicationContext.getSharedPreferences("systemConfig", 0).edit().putBoolean("logsEnabled", BuildVars.LOGS_ENABLED).commit();
            } else if (var2 == 5) {
               SharedConfig.toggleInappCamera();
            } else if (var2 == 6) {
               MessagesStorage.getInstance(SettingsActivity.access$2700(SettingsActivity.this)).clearSentMedia();
               SharedConfig.setNoSoundHintShowed(false);
               MessagesController.getGlobalMainSettings().edit().remove("archivehint").remove("archivehint_l").remove("gifhint").remove("soundHint").commit();
            } else if (var2 == 7) {
               VoIPHelper.showCallDebugSettings(SettingsActivity.this.getParentActivity());
            } else if (var2 == 8) {
               SharedConfig.toggleRoundCamera16to9();
            } else if (var2 == 9) {
               ((LaunchActivity)SettingsActivity.this.getParentActivity()).checkAppUpdate(true);
            } else if (var2 == 10) {
               MessagesStorage.getInstance(SettingsActivity.access$2800(SettingsActivity.this)).readAllDialogs();
            }

         }

         public boolean onItemClick(View var1, int var2) {
            if (SettingsActivity.this.listView.getAdapter() == SettingsActivity.this.searchAdapter) {
               AlertDialog.Builder var16 = new AlertDialog.Builder(SettingsActivity.this.getParentActivity());
               var16.setTitle(LocaleController.getString("AppName", 2131558635));
               var16.setMessage(LocaleController.getString("ClearSearch", 2131559114));
               var16.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$SettingsActivity$5$ugZ8nxlV3bSe_GDAFB06httPzlQ(this));
               var16.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               SettingsActivity.this.showDialog(var16.create());
               return true;
            } else if (var2 != SettingsActivity.this.versionRow) {
               return false;
            } else {
               ++this.pressCount;
               if (this.pressCount < 2 && !BuildVars.DEBUG_PRIVATE_VERSION) {
                  try {
                     Toast.makeText(SettingsActivity.this.getParentActivity(), "¯\\_(ツ)_/¯", 0).show();
                  } catch (Exception var14) {
                     FileLog.e((Throwable)var14);
                  }
               } else {
                  AlertDialog.Builder var3 = new AlertDialog.Builder(SettingsActivity.this.getParentActivity());
                  var3.setTitle(LocaleController.getString("DebugMenu", 2131559209));
                  String var4 = LocaleController.getString("DebugMenuImportContacts", 2131559216);
                  String var5 = LocaleController.getString("DebugMenuReloadContacts", 2131559218);
                  String var6 = LocaleController.getString("DebugMenuResetContacts", 2131559219);
                  String var7 = LocaleController.getString("DebugMenuResetDialogs", 2131559220);
                  String var15;
                  if (BuildVars.LOGS_ENABLED) {
                     var2 = 2131559213;
                     var15 = "DebugMenuDisableLogs";
                  } else {
                     var2 = 2131559215;
                     var15 = "DebugMenuEnableLogs";
                  }

                  String var8 = LocaleController.getString(var15, var2);
                  if (SharedConfig.inappCamera) {
                     var2 = 2131559212;
                     var15 = "DebugMenuDisableCamera";
                  } else {
                     var2 = 2131559214;
                     var15 = "DebugMenuEnableCamera";
                  }

                  String var9 = LocaleController.getString(var15, var2);
                  String var10 = LocaleController.getString("DebugMenuClearMediaCache", 2131559211);
                  String var11 = LocaleController.getString("DebugMenuCallSettings", 2131559210);
                  if (BuildVars.DEBUG_PRIVATE_VERSION) {
                     var15 = "Check for app updates";
                  } else {
                     var15 = null;
                  }

                  String var12 = LocaleController.getString("DebugMenuReadAllDialogs", 2131559217);
                  _$$Lambda$SettingsActivity$5$xAsEgwepb1pw1bhC6d7Q2D8mdcE var13 = new _$$Lambda$SettingsActivity$5$xAsEgwepb1pw1bhC6d7Q2D8mdcE(this);
                  var3.setItems(new CharSequence[]{var4, var5, var6, var7, var8, var9, var10, var11, null, var15, var12}, var13);
                  var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  SettingsActivity.this.showDialog(var3.create());
               }

               return true;
            }
         }
      });
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showTextView();
      this.emptyView.setTextSize(18);
      this.emptyView.setVisibility(8);
      this.emptyView.setShowAtCenter(true);
      var6.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      var6.addView(super.actionBar);
      this.extraHeightView = new View(var1);
      this.extraHeightView.setPivotY(0.0F);
      this.extraHeightView.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      var6.addView(this.extraHeightView, LayoutHelper.createFrame(-1, 88.0F));
      this.shadowView = new View(var1);
      this.shadowView.setBackgroundResource(2131165407);
      var6.addView(this.shadowView, LayoutHelper.createFrame(-1, 3.0F));
      this.avatarContainer = new FrameLayout(var1);
      FrameLayout var19 = this.avatarContainer;
      float var8;
      if (LocaleController.isRTL) {
         var8 = (float)AndroidUtilities.dp(42.0F);
      } else {
         var8 = 0.0F;
      }

      var19.setPivotX(var8);
      this.avatarContainer.setPivotY(0.0F);
      var19 = this.avatarContainer;
      boolean var9 = LocaleController.isRTL;
      byte var10 = 5;
      byte var11;
      if (var9) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      byte var12;
      if (LocaleController.isRTL) {
         var12 = 0;
      } else {
         var12 = 64;
      }

      var8 = (float)var12;
      if (LocaleController.isRTL) {
         var12 = 112;
      } else {
         var12 = 0;
      }

      var6.addView(var19, LayoutHelper.createFrame(42, 42.0F, var11 | 48, var8, 0.0F, (float)var12, 0.0F));
      this.avatarContainer.setOnClickListener(new _$$Lambda$SettingsActivity$tP6tYHrZMyudHveG_joX5MC_Y2o(this));
      this.avatarImage = new BackupImageView(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.avatarImage.setContentDescription(LocaleController.getString("AccDescrProfilePicture", 2131558460));
      this.avatarContainer.addView(this.avatarImage, LayoutHelper.createFrame(42, 42.0F));
      final Paint var21 = new Paint(1);
      var21.setColor(1426063360);
      this.avatarProgressView = new RadialProgressView(var1) {
         protected void onDraw(Canvas var1) {
            if (SettingsActivity.this.avatarImage != null && SettingsActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
               var21.setAlpha((int)(SettingsActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0F));
               var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(21.0F), var21);
            }

            super.onDraw(var1);
         }
      };
      this.avatarProgressView.setSize(AndroidUtilities.dp(26.0F));
      this.avatarProgressView.setProgressColor(-1);
      this.avatarContainer.addView(this.avatarProgressView, LayoutHelper.createFrame(42, 42.0F));
      this.showAvatarProgress(false, false);
      this.nameTextView = new TextView(var1) {
         protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            float var3;
            if (LocaleController.isRTL) {
               var3 = (float)this.getMeasuredWidth();
            } else {
               var3 = 0.0F;
            }

            this.setPivotX(var3);
         }
      };
      this.nameTextView.setTextColor(Theme.getColor("profile_title"));
      this.nameTextView.setTextSize(1, 18.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      TextView var23 = this.nameTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      var23.setGravity(var11);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setPivotY(0.0F);
      var23 = this.nameTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 48.0F;
      } else {
         var8 = 118.0F;
      }

      float var13;
      if (LocaleController.isRTL) {
         var13 = 166.0F;
      } else {
         var13 = 96.0F;
      }

      var6.addView(var23, LayoutHelper.createFrame(-2, -2.0F, var11 | 48, var8, 0.0F, var13, 0.0F));
      this.onlineTextView = new TextView(var1);
      this.onlineTextView.setTextColor(Theme.getColor("profile_status"));
      this.onlineTextView.setTextSize(1, 14.0F);
      this.onlineTextView.setLines(1);
      this.onlineTextView.setMaxLines(1);
      this.onlineTextView.setSingleLine(true);
      this.onlineTextView.setEllipsize(TruncateAt.END);
      var23 = this.onlineTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      var23.setGravity(var11);
      var23 = this.onlineTextView;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 48.0F;
      } else {
         var8 = 118.0F;
      }

      if (LocaleController.isRTL) {
         var13 = 166.0F;
      } else {
         var13 = 96.0F;
      }

      var6.addView(var23, LayoutHelper.createFrame(-2, -2.0F, var11 | 48, var8, 0.0F, var13, 0.0F));
      this.writeButton = new ImageView(var1);
      Drawable var26 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
      Object var24 = var26;
      if (VERSION.SDK_INT < 21) {
         Drawable var15 = var1.getResources().getDrawable(2131165388).mutate();
         var15.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var24 = new CombinedDrawable(var15, var26, 0, 0);
         ((CombinedDrawable)var24).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.writeButton.setBackgroundDrawable((Drawable)var24);
      this.writeButton.setImageResource(2131165572);
      this.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), Mode.MULTIPLY));
      this.writeButton.setScaleType(ScaleType.CENTER);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var16 = new StateListAnimator();
         ObjectAnimator var25 = ObjectAnimator.ofFloat(this.writeButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var16.addState(new int[]{16842919}, var25);
         var25 = ObjectAnimator.ofFloat(this.writeButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var16.addState(new int[0], var25);
         this.writeButton.setStateListAnimator(var16);
         this.writeButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      ImageView var17 = this.writeButton;
      if (VERSION.SDK_INT >= 21) {
         var11 = 56;
      } else {
         var11 = 60;
      }

      if (VERSION.SDK_INT >= 21) {
         var8 = 56.0F;
      } else {
         var8 = 60.0F;
      }

      var12 = var10;
      if (LocaleController.isRTL) {
         var12 = 3;
      }

      if (LocaleController.isRTL) {
         var13 = 16.0F;
      } else {
         var13 = 0.0F;
      }

      float var14;
      if (LocaleController.isRTL) {
         var14 = 0.0F;
      } else {
         var14 = 16.0F;
      }

      var6.addView(var17, LayoutHelper.createFrame(var11, var8, var12 | 48, var13, 0.0F, var14, 0.0F));
      this.writeButton.setOnClickListener(new _$$Lambda$SettingsActivity$r8NmpkmDLMDut28p1uEJ0PAPo_c(this));
      this.writeButton.setContentDescription(LocaleController.getString("AccDescrChangeProfilePicture", 2131558425));
      if (var4 != -1) {
         this.layoutManager.scrollToPositionWithOffset(var4, var5);
         if (var22 != null) {
            this.writeButton.setTag(0);
            this.writeButton.setScaleX(0.2F);
            this.writeButton.setScaleY(0.2F);
            this.writeButton.setAlpha(0.0F);
            this.writeButton.setVisibility(8);
         }
      }

      this.needLayout();
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && SettingsActivity.this.listView.getAdapter() == SettingsActivity.this.searchAdapter) {
               AndroidUtilities.hideKeyboard(SettingsActivity.this.getParentActivity().getCurrentFocus());
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (SettingsActivity.this.layoutManager.getItemCount() != 0) {
               var2 = 0;
               byte var6 = 0;
               View var5 = var1.getChildAt(0);
               if (var5 != null && SettingsActivity.this.avatarContainer.getVisibility() == 0) {
                  if (SettingsActivity.this.layoutManager.findFirstVisibleItemPosition() == 0) {
                     int var4 = AndroidUtilities.dp(88.0F);
                     var2 = var6;
                     if (var5.getTop() < 0) {
                        var2 = var5.getTop();
                     }

                     var2 += var4;
                  }

                  if (SettingsActivity.this.extraHeight != var2) {
                     SettingsActivity.this.extraHeight = var2;
                     SettingsActivity.this.needLayout();
                  }
               }

            }
         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.updateInterfaces) {
         var1 = (Integer)var3[0];
         if ((var1 & 2) != 0 || (var1 & 1) != 0) {
            this.updateUserData();
         }
      } else if (var1 == NotificationCenter.userInfoDidLoad) {
         if ((Integer)var3[0] == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            SettingsActivity.ListAdapter var4 = this.listAdapter;
            if (var4 != null) {
               this.userInfo = (TLRPC.UserFull)var3[1];
               var4.notifyItemChanged(this.bioRow);
            }
         }
      } else if (var1 == NotificationCenter.emojiDidLoad) {
         RecyclerListView var5 = this.listView;
         if (var5 != null) {
            var5.invalidateViews();
         }
      }

   }

   public void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SettingsActivity$NCmSP4zZF_AHhm4jiT4j3ELozqs(this, var1, var3, var2));
   }

   // $FF: synthetic method
   public String getInitialSearchString() {
      return ImageUpdater$ImageUpdaterDelegate$_CC.$default$getInitialSearchString(this);
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{EmptyCell.class, HeaderCell.class, TextDetailCell.class, TextCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var6 = new ThemeDescription(this.extraHeightView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_actionBarIconBlue");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_actionBarSelectorBlue");
      ThemeDescription var10 = new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_title");
      ThemeDescription var11 = new ThemeDescription(this.onlineTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_status");
      ThemeDescription var12 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var13 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var14 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var16 = this.listView;
      Paint var17 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, new ThemeDescription(var16, 0, new Class[]{View.class}, var17, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.avatarImage, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription(this.avatarImage, 0, (Class[])null, (Paint)null, new Drawable[]{this.avatarDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundInProfileBlue"), new ThemeDescription(this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionIcon"), new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionBackground"), new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionPressedBackground"), new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"), new ThemeDescription(this.listView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{SettingsSearchCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$SettingsActivity(View var1, int var2) {
      if (this.listView.getAdapter() == this.listAdapter) {
         if (var2 == this.notificationRow) {
            this.presentFragment(new NotificationsSettingsActivity());
         } else if (var2 == this.privacyRow) {
            this.presentFragment(new PrivacySettingsActivity());
         } else if (var2 == this.dataRow) {
            this.presentFragment(new DataSettingsActivity());
         } else if (var2 == this.chatRow) {
            this.presentFragment(new ThemeActivity(0));
         } else if (var2 == this.helpRow) {
            this.showHelpAlert();
         } else if (var2 == this.languageRow) {
            this.presentFragment(new LanguageSelectActivity());
         } else if (var2 == this.usernameRow) {
            this.presentFragment(new ChangeUsernameActivity());
         } else if (var2 == this.bioRow) {
            if (this.userInfo != null) {
               this.presentFragment(new ChangeBioActivity());
            }
         } else if (var2 == this.numberRow) {
            this.presentFragment(new ChangePhoneHelpActivity());
         }
      } else {
         if (var2 < 0) {
            return;
         }

         Integer var3 = this.numberRow;
         Object var4;
         if (this.searchAdapter.searchWas) {
            if (var2 < this.searchAdapter.searchResults.size()) {
               var4 = this.searchAdapter.searchResults.get(var2);
            } else {
               var2 -= this.searchAdapter.searchResults.size() + 1;
               var4 = var3;
               if (var2 >= 0) {
                  var4 = var3;
                  if (var2 < this.searchAdapter.faqSearchResults.size()) {
                     var4 = this.searchAdapter.faqSearchResults.get(var2);
                  }
               }
            }
         } else {
            --var2;
            if (var2 < 0) {
               return;
            }

            var4 = var3;
            if (var2 < this.searchAdapter.recentSearches.size()) {
               var4 = this.searchAdapter.recentSearches.get(var2);
            }
         }

         if (var4 instanceof SettingsActivity.SearchAdapter.SearchResult) {
            ((SettingsActivity.SearchAdapter.SearchResult)var4).open();
         } else if (var4 instanceof SettingsActivity.SearchAdapter.FaqSearchResult) {
            SettingsActivity.SearchAdapter.FaqSearchResult var5 = (SettingsActivity.SearchAdapter.FaqSearchResult)var4;
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.openArticle, this.searchAdapter.faqWebPage, var5.url);
         }

         if (var4 != null) {
            this.searchAdapter.addRecent(var4);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$1$SettingsActivity(View var1) {
      if (this.avatar == null) {
         TLRPC.User var4 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
         if (var4 != null) {
            TLRPC.UserProfilePhoto var2 = var4.photo;
            if (var2 != null && var2.photo_big != null) {
               PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
               var2 = var4.photo;
               int var3 = var2.dc_id;
               if (var3 != 0) {
                  var2.photo_big.dc_id = var3;
               }

               PhotoViewer.getInstance().openPhoto(var4.photo.photo_big, this.provider);
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$createView$3$SettingsActivity(View var1) {
      TLRPC.User var2 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
      TLRPC.User var4 = var2;
      if (var2 == null) {
         var4 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
      }

      if (var4 != null) {
         ImageUpdater var5 = this.imageUpdater;
         TLRPC.UserProfilePhoto var6 = var4.photo;
         boolean var3;
         if (var6 != null && var6.photo_big != null && !(var6 instanceof TLRPC.TL_userProfilePhotoEmpty)) {
            var3 = true;
         } else {
            var3 = false;
         }

         var5.openMenu(var3, new _$$Lambda$SettingsActivity$Y3os7qOdvyHRbMkK3Q2dvtuvdp0(this));
      }
   }

   // $FF: synthetic method
   public void lambda$didUploadPhoto$6$SettingsActivity(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      if (var1 != null) {
         TLRPC.TL_photos_uploadProfilePhoto var4 = new TLRPC.TL_photos_uploadProfilePhoto();
         var4.file = var1;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$SettingsActivity$xEUCjiAh7lu_htPiO_5d1rnOSOk(this));
      } else {
         this.avatar = var2.location;
         this.avatarBig = var3.location;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForLocal(this.avatar), "50_50", (Drawable)this.avatarDrawable, (Object)null);
         this.showAvatarProgress(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$SettingsActivity() {
      MessagesController.getInstance(super.currentAccount).deleteUserPhoto((TLRPC.InputPhoto)null);
   }

   // $FF: synthetic method
   public void lambda$null$4$SettingsActivity() {
      this.avatar = null;
      this.avatarBig = null;
      this.updateUserData();
      this.showAvatarProgress(false, true);
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1535);
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
      UserConfig.getInstance(super.currentAccount).saveConfig(true);
   }

   // $FF: synthetic method
   public void lambda$null$5$SettingsActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.User var9 = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
         if (var9 == null) {
            var9 = UserConfig.getInstance(super.currentAccount).getCurrentUser();
            if (var9 == null) {
               return;
            }

            MessagesController.getInstance(super.currentAccount).putUser(var9, false);
         } else {
            UserConfig.getInstance(super.currentAccount).setCurrentUser(var9);
         }

         TLRPC.TL_photos_photo var3 = (TLRPC.TL_photos_photo)var1;
         ArrayList var6 = var3.photo.sizes;
         TLRPC.PhotoSize var4 = FileLoader.getClosestPhotoSizeWithSize(var6, 150);
         TLRPC.PhotoSize var7 = FileLoader.getClosestPhotoSizeWithSize(var6, 800);
         var9.photo = new TLRPC.TL_userProfilePhoto();
         TLRPC.UserProfilePhoto var5 = var9.photo;
         var5.photo_id = var3.photo.id;
         if (var4 != null) {
            var5.photo_small = var4.location;
         }

         if (var7 != null) {
            var9.photo.photo_big = var7.location;
         } else if (var4 != null) {
            var9.photo.photo_small = var4.location;
         }

         if (var3 != null) {
            if (var4 != null && this.avatar != null) {
               File var10 = FileLoader.getPathToAttach(var4, true);
               FileLoader.getPathToAttach(this.avatar, true).renameTo(var10);
               StringBuilder var11 = new StringBuilder();
               var11.append(this.avatar.volume_id);
               var11.append("_");
               var11.append(this.avatar.local_id);
               var11.append("@50_50");
               String var12 = var11.toString();
               StringBuilder var14 = new StringBuilder();
               var14.append(var4.location.volume_id);
               var14.append("_");
               var14.append(var4.location.local_id);
               var14.append("@50_50");
               String var13 = var14.toString();
               ImageLoader.getInstance().replaceImageInCache(var12, var13, ImageLocation.getForUser(var9, false), true);
            }

            if (var7 != null && this.avatarBig != null) {
               File var8 = FileLoader.getPathToAttach(var7, true);
               FileLoader.getPathToAttach(this.avatarBig, true).renameTo(var8);
            }
         }

         MessagesStorage.getInstance(super.currentAccount).clearUserPhotos(var9.id);
         var6 = new ArrayList();
         var6.add(var9);
         MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var6, (ArrayList)null, false, true);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$SettingsActivity$YjutmGJ_Hyc5u6zgslQKrIDUgoo(this));
   }

   // $FF: synthetic method
   public void lambda$null$7$SettingsActivity(DialogInterface var1, int var2) {
      SharedConfig.pushAuthKey = null;
      SharedConfig.pushAuthKeyId = null;
      SharedConfig.saveConfig();
      ConnectionsManager.getInstance(super.currentAccount).switchBackend();
   }

   // $FF: synthetic method
   public void lambda$null$9$SettingsActivity(AlertDialog var1, boolean[] var2, File var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
      }

      if (var2[0]) {
         Uri var5;
         if (VERSION.SDK_INT >= 24) {
            var5 = FileProvider.getUriForFile(this.getParentActivity(), "org.telegram.messenger.provider", var3);
         } else {
            var5 = Uri.fromFile(var3);
         }

         Intent var6 = new Intent("android.intent.action.SEND");
         if (VERSION.SDK_INT >= 24) {
            var6.addFlags(1);
         }

         var6.setType("message/rfc822");
         var6.putExtra("android.intent.extra.EMAIL", "");
         StringBuilder var7 = new StringBuilder();
         var7.append("Logs from ");
         var7.append(LocaleController.getInstance().formatterStats.format(System.currentTimeMillis()));
         var6.putExtra("android.intent.extra.SUBJECT", var7.toString());
         var6.putExtra("android.intent.extra.STREAM", var5);
         this.getParentActivity().startActivityForResult(Intent.createChooser(var6, "Select email application."), 500);
      } else {
         Toast.makeText(this.getParentActivity(), LocaleController.getString("ErrorOccurred", 2131559375), 0).show();
      }

   }

   // $FF: synthetic method
   public void lambda$sendLogs$10$SettingsActivity(AlertDialog param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$showHelpAlert$8$SettingsActivity(BottomSheet.Builder var1, View var2) {
      int var3 = (Integer)var2.getTag();
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 != 4) {
                     if (var3 == 5) {
                        if (this.getParentActivity() == null) {
                           return;
                        }

                        AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
                        var4.setMessage(LocaleController.getString("AreYouSure", 2131558666));
                        var4.setTitle(LocaleController.getString("AppName", 2131558635));
                        var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$SettingsActivity$ZCLGwEQbjP_a8nps5mf6ogxDy10(this));
                        var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                        this.showDialog(var4.create());
                     }
                  } else {
                     FileLog.cleanupLogs();
                  }
               } else {
                  this.sendLogs();
               }
            } else {
               Browser.openUrl(this.getParentActivity(), (String)LocaleController.getString("PrivacyPolicyUrl", 2131560504));
            }
         } else {
            Browser.openUrl(this.getParentActivity(), (String)LocaleController.getString("TelegramFaqUrl", 2131560871));
         }
      } else {
         this.showDialog(AlertsCreator.createSupportAlert(this));
      }

      var1.getDismissRunnable().run();
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      this.imageUpdater.onActivityResult(var1, var2, var3);
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.imageUpdater = new ImageUpdater();
      ImageUpdater var1 = this.imageUpdater;
      var1.parentFragment = this;
      var1.delegate = this;
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoad);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.rowCount = 0;
      int var2 = this.rowCount++;
      this.overscrollRow = var2;
      var2 = this.rowCount++;
      this.numberSectionRow = var2;
      var2 = this.rowCount++;
      this.numberRow = var2;
      var2 = this.rowCount++;
      this.usernameRow = var2;
      var2 = this.rowCount++;
      this.bioRow = var2;
      var2 = this.rowCount++;
      this.settingsSectionRow = var2;
      var2 = this.rowCount++;
      this.settingsSectionRow2 = var2;
      var2 = this.rowCount++;
      this.notificationRow = var2;
      var2 = this.rowCount++;
      this.privacyRow = var2;
      var2 = this.rowCount++;
      this.dataRow = var2;
      var2 = this.rowCount++;
      this.chatRow = var2;
      var2 = this.rowCount++;
      this.languageRow = var2;
      var2 = this.rowCount++;
      this.helpRow = var2;
      var2 = this.rowCount++;
      this.versionRow = var2;
      DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
      this.userInfo = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
      MessagesController.getInstance(super.currentAccount).loadUserInfo(UserConfig.getInstance(super.currentAccount).getCurrentUser(), true, super.classGuid);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      BackupImageView var1 = this.avatarImage;
      if (var1 != null) {
         var1.setImageDrawable((Drawable)null);
      }

      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoad);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      this.imageUpdater.clear();
   }

   public void onResume() {
      super.onResume();
      SettingsActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      this.updateUserData();
      this.fixLayout();
      this.setParentActivityTitle(LocaleController.getString("Settings", 2131560738));
   }

   public void restoreSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      if (var2 != null) {
         var2.currentPicturePath = var1.getString("path");
      }

   }

   public void saveSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      if (var2 != null) {
         String var3 = var2.currentPicturePath;
         if (var3 != null) {
            var1.putString("path", var3);
         }
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return SettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == SettingsActivity.this.overscrollRow) {
            return 0;
         } else if (var1 == SettingsActivity.this.settingsSectionRow) {
            return 1;
         } else if (var1 != SettingsActivity.this.notificationRow && var1 != SettingsActivity.this.privacyRow && var1 != SettingsActivity.this.languageRow && var1 != SettingsActivity.this.dataRow && var1 != SettingsActivity.this.chatRow && var1 != SettingsActivity.this.helpRow) {
            if (var1 == SettingsActivity.this.versionRow) {
               return 5;
            } else if (var1 != SettingsActivity.this.numberRow && var1 != SettingsActivity.this.usernameRow && var1 != SettingsActivity.this.bioRow) {
               return var1 != SettingsActivity.this.settingsSectionRow2 && var1 != SettingsActivity.this.numberSectionRow ? 2 : 4;
            } else {
               return 6;
            }
         } else {
            return 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != SettingsActivity.this.notificationRow && var2 != SettingsActivity.this.numberRow && var2 != SettingsActivity.this.privacyRow && var2 != SettingsActivity.this.languageRow && var2 != SettingsActivity.this.usernameRow && var2 != SettingsActivity.this.bioRow && var2 != SettingsActivity.this.versionRow && var2 != SettingsActivity.this.dataRow && var2 != SettingsActivity.this.chatRow && var2 != SettingsActivity.this.helpRow) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 2) {
               if (var3 != 4) {
                  if (var3 == 6) {
                     TextDetailCell var4 = (TextDetailCell)var1.itemView;
                     String var8;
                     if (var2 == SettingsActivity.this.numberRow) {
                        label76: {
                           TLRPC.User var7 = UserConfig.getInstance(SettingsActivity.access$6000(SettingsActivity.this)).getCurrentUser();
                           if (var7 != null) {
                              String var5 = var7.phone;
                              if (var5 != null && var5.length() != 0) {
                                 PhoneFormat var11 = PhoneFormat.getInstance();
                                 StringBuilder var6 = new StringBuilder();
                                 var6.append("+");
                                 var6.append(var7.phone);
                                 var8 = var11.format(var6.toString());
                                 break label76;
                              }
                           }

                           var8 = LocaleController.getString("NumberUnknown", 2131560096);
                        }

                        var4.setTextAndValue(var8, LocaleController.getString("TapToChangePhone", 2131560860), true);
                     } else if (var2 == SettingsActivity.this.usernameRow) {
                        TLRPC.User var12 = UserConfig.getInstance(SettingsActivity.access$6200(SettingsActivity.this)).getCurrentUser();
                        if (var12 != null && !TextUtils.isEmpty(var12.username)) {
                           StringBuilder var9 = new StringBuilder();
                           var9.append("@");
                           var9.append(var12.username);
                           var8 = var9.toString();
                        } else {
                           var8 = LocaleController.getString("UsernameEmpty", 2131561024);
                        }

                        var4.setTextAndValue(var8, LocaleController.getString("Username", 2131561021), true);
                     } else if (var2 == SettingsActivity.this.bioRow) {
                        if (SettingsActivity.this.userInfo != null && TextUtils.isEmpty(SettingsActivity.this.userInfo.about)) {
                           var4.setTextAndValue(LocaleController.getString("UserBio", 2131560987), LocaleController.getString("UserBioDetail", 2131560988), false);
                        } else {
                           if (SettingsActivity.this.userInfo == null) {
                              var8 = LocaleController.getString("Loading", 2131559768);
                           } else {
                              var8 = SettingsActivity.this.userInfo.about;
                           }

                           var4.setTextWithEmojiAndValue(var8, LocaleController.getString("UserBio", 2131560987), false);
                        }
                     }
                  }
               } else if (var2 == SettingsActivity.this.settingsSectionRow2) {
                  ((HeaderCell)var1.itemView).setText(LocaleController.getString("SETTINGS", 2131560623));
               } else if (var2 == SettingsActivity.this.numberSectionRow) {
                  ((HeaderCell)var1.itemView).setText(LocaleController.getString("Account", 2131558486));
               }
            } else {
               TextCell var10 = (TextCell)var1.itemView;
               if (var2 == SettingsActivity.this.languageRow) {
                  var10.setTextAndIcon(LocaleController.getString("Language", 2131559715), 2131165586, true);
               } else if (var2 == SettingsActivity.this.notificationRow) {
                  var10.setTextAndIcon(LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, true);
               } else if (var2 == SettingsActivity.this.privacyRow) {
                  var10.setTextAndIcon(LocaleController.getString("PrivacySettings", 2131560509), 2131165594, true);
               } else if (var2 == SettingsActivity.this.dataRow) {
                  var10.setTextAndIcon(LocaleController.getString("DataSettings", 2131559193), 2131165579, true);
               } else if (var2 == SettingsActivity.this.chatRow) {
                  var10.setTextAndIcon(LocaleController.getString("ChatSettings", 2131559043), 2131165574, true);
               } else if (var2 == SettingsActivity.this.helpRow) {
                  var10.setTextAndIcon(LocaleController.getString("SettingsHelp", 2131560740), 2131165582, false);
               }
            }
         } else if (var2 == SettingsActivity.this.overscrollRow) {
            ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(88.0F));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var9 = null;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 4) {
                     if (var2 != 5) {
                        if (var2 == 6) {
                           var9 = new TextDetailCell(this.mContext);
                           ((View)var9).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                     } else {
                        TextInfoPrivacyCell var3 = new TextInfoPrivacyCell(this.mContext, 10);
                        var3.getTextView().setGravity(1);
                        var3.getTextView().setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
                        var3.getTextView().setMovementMethod((MovementMethod)null);
                        var3.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));

                        label78: {
                           Exception var10000;
                           label88: {
                              PackageInfo var4;
                              boolean var10001;
                              try {
                                 var4 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                                 var2 = var4.versionCode / 10;
                              } catch (Exception var8) {
                                 var10000 = var8;
                                 var10001 = false;
                                 break label88;
                              }

                              String var10 = "";

                              label72: {
                                 label71: {
                                    label70: {
                                       label89: {
                                          label67: {
                                             try {
                                                switch(var4.versionCode % 10) {
                                                case 0:
                                                case 9:
                                                   break label89;
                                                case 1:
                                                case 3:
                                                   break label70;
                                                case 2:
                                                case 4:
                                                   break label71;
                                                case 5:
                                                case 7:
                                                   break;
                                                case 6:
                                                case 8:
                                                   break label67;
                                                default:
                                                   break label72;
                                                }
                                             } catch (Exception var7) {
                                                var10000 = var7;
                                                var10001 = false;
                                                break label88;
                                             }

                                             var10 = "arm64-v8a";
                                             break label72;
                                          }

                                          var10 = "x86_64";
                                          break label72;
                                       }

                                       try {
                                          StringBuilder var11 = new StringBuilder();
                                          var11.append("universal ");
                                          var11.append(Build.CPU_ABI);
                                          var11.append(" ");
                                          var11.append(Build.CPU_ABI2);
                                          var10 = var11.toString();
                                          break label72;
                                       } catch (Exception var6) {
                                          var10000 = var6;
                                          var10001 = false;
                                          break label88;
                                       }
                                    }

                                    var10 = "arm-v7a";
                                    break label72;
                                 }

                                 var10 = "x86";
                              }

                              try {
                                 var3.setText(String.format("Telegram-FOSS %1$s", String.format(Locale.US, "v%s (%d) %s", var4.versionName, var2, var10)));
                                 break label78;
                              } catch (Exception var5) {
                                 var10000 = var5;
                                 var10001 = false;
                              }
                           }

                           Exception var12 = var10000;
                           FileLog.e((Throwable)var12);
                        }

                        var3.getTextView().setPadding(0, AndroidUtilities.dp(14.0F), 0, AndroidUtilities.dp(14.0F));
                        var9 = var3;
                     }
                  } else {
                     var9 = new HeaderCell(this.mContext, 23);
                     ((View)var9).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var9 = new TextCell(this.mContext);
                  ((View)var9).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var9 = new ShadowSectionCell(this.mContext);
            }
         } else {
            var9 = new EmptyCell(this.mContext);
            ((View)var9).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var9).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var9);
      }
   }

   private class SearchAdapter extends RecyclerListView.SelectionAdapter {
      private ArrayList faqSearchArray = new ArrayList();
      private ArrayList faqSearchResults = new ArrayList();
      private TLRPC.WebPage faqWebPage;
      private String lastSearchString;
      private boolean loadingFaqPage;
      private Context mContext;
      private ArrayList recentSearches = new ArrayList();
      private ArrayList resultNames = new ArrayList();
      private SettingsActivity.SearchAdapter.SearchResult[] searchArray = new SettingsActivity.SearchAdapter.SearchResult[]{new SettingsActivity.SearchAdapter.SearchResult(500, LocaleController.getString("EditName", 2131559326), 0, new _$$Lambda$SettingsActivity$SearchAdapter$fYhNxPaKcCcAqdniDsRVcdOfAcw(this)), new SettingsActivity.SearchAdapter.SearchResult(501, LocaleController.getString("ChangePhoneNumber", 2131558913), 0, new _$$Lambda$SettingsActivity$SearchAdapter$AQE0PybSOWsTppwbXC4ScpJt5cU(this)), new SettingsActivity.SearchAdapter.SearchResult(502, LocaleController.getString("AddAnotherAccount", 2131558562), 0, new _$$Lambda$SettingsActivity$SearchAdapter$qf5DMONPDpbbFVQlIYoeluu6fAg(this)), new SettingsActivity.SearchAdapter.SearchResult(503, LocaleController.getString("UserBio", 2131560987), 0, new _$$Lambda$SettingsActivity$SearchAdapter$wEStD_IsL8y26JcLIYLGQZisGR0(this)), new SettingsActivity.SearchAdapter.SearchResult(1, LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$pgorJIrrbTNFMCB_ShcfXSVsFZo(this)), new SettingsActivity.SearchAdapter.SearchResult(2, LocaleController.getString("NotificationsPrivateChats", 2131560087), LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$EaevAppnRfEWu4EsBvHSVnfoJO4(this)), new SettingsActivity.SearchAdapter.SearchResult(3, LocaleController.getString("NotificationsGroups", 2131560071), LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$_Yb9Kq1MuLzQbkVBNuUX_K5eF_U(this)), new SettingsActivity.SearchAdapter.SearchResult(4, LocaleController.getString("NotificationsChannels", 2131560058), LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$zEBaSkQhcY7piZudVwqn9mABazs(this)), new SettingsActivity.SearchAdapter.SearchResult(5, LocaleController.getString("VoipNotificationSettings", 2131561075), "callsSectionRow", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$aceyqZ4yZ6dj0j20qgJOi7MQ0NE(this)), new SettingsActivity.SearchAdapter.SearchResult(6, LocaleController.getString("BadgeNumber", 2131558826), "badgeNumberSection", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$t5wmPo8eU16Y_X8HwQqExxITKKQ(this)), new SettingsActivity.SearchAdapter.SearchResult(7, LocaleController.getString("InAppNotifications", 2131559657), "inappSectionRow", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$S2uiNLXT4KCsYO2kv5G38zxY_Ow(this)), new SettingsActivity.SearchAdapter.SearchResult(8, LocaleController.getString("ContactJoined", 2131559144), "contactJoinedRow", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$Dee0X2OE_oxy67HDXUAjVnFzV7c(this)), new SettingsActivity.SearchAdapter.SearchResult(9, LocaleController.getString("PinnedMessages", 2131560452), "pinnedMessageRow", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$O36kxEh77vwepAjM1YBjwwIEdDY(this)), new SettingsActivity.SearchAdapter.SearchResult(10, LocaleController.getString("ResetAllNotifications", 2131560589), "resetNotificationsRow", LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, new _$$Lambda$SettingsActivity$SearchAdapter$OmbyIf1DIC6b_M8uR5C3UfAIxWo(this)), new SettingsActivity.SearchAdapter.SearchResult(100, LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$fw82m1Z1AIBCwhHRMVab7sgzrow(this)), new SettingsActivity.SearchAdapter.SearchResult(101, LocaleController.getString("BlockedUsers", 2131558835), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$z1aqQi8eq9En3J0XVwkCNdcd6mM(this)), new SettingsActivity.SearchAdapter.SearchResult(105, LocaleController.getString("PrivacyPhone", 2131560498), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$f_3utWtHCzlSn6039S3bgof9vWg(this)), new SettingsActivity.SearchAdapter.SearchResult(102, LocaleController.getString("PrivacyLastSeen", 2131560492), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$xiSHHuIuMQVeQ_fdA3Wj_Bgq4iA(this)), new SettingsActivity.SearchAdapter.SearchResult(103, LocaleController.getString("PrivacyProfilePhoto", 2131560505), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$RYPspq_lY6i8gNBxBdrIhxctFpo(this)), new SettingsActivity.SearchAdapter.SearchResult(104, LocaleController.getString("PrivacyForwards", 2131560484), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$6LoBQomheYgP8glc54xFz2WzjyY(this)), new SettingsActivity.SearchAdapter.SearchResult(105, LocaleController.getString("PrivacyP2P", 2131560493), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$VprFTsbEA5x2jnmQ8O1zs63h_9E(this)), new SettingsActivity.SearchAdapter.SearchResult(106, LocaleController.getString("Calls", 2131558888), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$c5DPXMo95Hg5TAoapaOpYope9ao(this)), new SettingsActivity.SearchAdapter.SearchResult(107, LocaleController.getString("GroupsAndChannels", 2131559624), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$YveYfMPgJtpzBc_Aye2IlUKRL_E(this)), new SettingsActivity.SearchAdapter.SearchResult(108, LocaleController.getString("Passcode", 2131560160), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$OE5ko5HrJRcdo_04aDjTiMIRF4o(this)), new SettingsActivity.SearchAdapter.SearchResult(109, LocaleController.getString("TwoStepVerification", 2131560919), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$_P9hp4zriAme7mOQTdmdfHidC8Y(this)), new SettingsActivity.SearchAdapter.SearchResult(110, LocaleController.getString("SessionsTitle", 2131560726), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$B_YXrprYoUDKN_R4RWC_vN8dQ3Y(this)), new SettingsActivity.SearchAdapter.SearchResult(111, LocaleController.getString("PrivacyDeleteCloudDrafts", 2131560481), "clearDraftsRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$5sT7VrARMFm8AXSv5eWPD8wnHec(this)), new SettingsActivity.SearchAdapter.SearchResult(112, LocaleController.getString("DeleteAccountIfAwayFor2", 2131559229), "deleteAccountRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$lQlwXu3fj_1ozEGBWi7cV_Yrauo(this)), new SettingsActivity.SearchAdapter.SearchResult(113, LocaleController.getString("PrivacyPaymentsClear", 2131560496), "paymentsClearRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$VLzl5etbBP7OOe7IzDvNV3te2pQ(this)), new SettingsActivity.SearchAdapter.SearchResult(114, LocaleController.getString("WebSessionsTitle", 2131561104), LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$i8RSZV17ziFovJKq7XfIMHoM02o(this)), new SettingsActivity.SearchAdapter.SearchResult(115, LocaleController.getString("SyncContactsDelete", 2131560851), "contactsDeleteRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$ykgPCWhfDa0SAQRBusRsel30WAo(this)), new SettingsActivity.SearchAdapter.SearchResult(116, LocaleController.getString("SyncContacts", 2131560849), "contactsSyncRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$ogTF__W6GjBUa6jCJUHIlUDUEH0(this)), new SettingsActivity.SearchAdapter.SearchResult(117, LocaleController.getString("SuggestContacts", 2131560840), "contactsSuggestRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$gXqnmEjhglRSWqeZPzgMYOpOhsk(this)), new SettingsActivity.SearchAdapter.SearchResult(118, LocaleController.getString("MapPreviewProvider", 2131559801), "secretMapRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$lb1RmyZ9zEsc2hcDKKU7dHdJeD8(this)), new SettingsActivity.SearchAdapter.SearchResult(119, LocaleController.getString("SecretWebPage", 2131560673), "secretWebpageRow", LocaleController.getString("PrivacySettings", 2131560509), 2131165594, new _$$Lambda$SettingsActivity$SearchAdapter$CPyFvh3ngQ1x3gECH51yA4WVVU8(this)), new SettingsActivity.SearchAdapter.SearchResult(200, LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$SR0QQ8fXitd2n2FmlZnOuv_PjCM(this)), new SettingsActivity.SearchAdapter.SearchResult(201, LocaleController.getString("DataUsage", 2131559194), "usageSectionRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$jxZ1ug5oHHZLFprdCJ2sObOMdeY(this)), new SettingsActivity.SearchAdapter.SearchResult(202, LocaleController.getString("StorageUsage", 2131560832), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$2a_PtgyUOPZ5daThh02W6OTXvjI(this)), new SettingsActivity.SearchAdapter.SearchResult(203, LocaleController.getString("KeepMedia", 2131559710), "keepMediaRow", LocaleController.getString("DataSettings", 2131559193), LocaleController.getString("StorageUsage", 2131560832), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$OfGBrtqXQbffkrMJs_wPG7G0fXA(this)), new SettingsActivity.SearchAdapter.SearchResult(204, LocaleController.getString("ClearMediaCache", 2131559110), "cacheRow", LocaleController.getString("DataSettings", 2131559193), LocaleController.getString("StorageUsage", 2131560832), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$d_3fa2mVqtsd9YcCWBrdaK4AvwI(this)), new SettingsActivity.SearchAdapter.SearchResult(205, LocaleController.getString("LocalDatabase", 2131559772), "databaseRow", LocaleController.getString("DataSettings", 2131559193), LocaleController.getString("StorageUsage", 2131560832), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$TWYG1OVbu9uZ6lFX1vM2hYJxs7w(this)), new SettingsActivity.SearchAdapter.SearchResult(206, LocaleController.getString("NetworkUsage", 2131559889), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$2CLVCiRkKjEbcz0jARW3ZP41JX8(this)), new SettingsActivity.SearchAdapter.SearchResult(207, LocaleController.getString("AutomaticMediaDownload", 2131558802), "mediaDownloadSectionRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$VUKBbuzi1KBkCnnX2JlWeTm2Cac(this)), new SettingsActivity.SearchAdapter.SearchResult(208, LocaleController.getString("WhenUsingMobileData", 2131561113), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$K3WxuNbtWdfw44GPZwu3Vih5_x4(this)), new SettingsActivity.SearchAdapter.SearchResult(209, LocaleController.getString("WhenConnectedOnWiFi", 2131561111), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$BLHHvMFpEDhqBkzKQy4VYJV2Wzg(this)), new SettingsActivity.SearchAdapter.SearchResult(210, LocaleController.getString("WhenRoaming", 2131561112), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$Pqx2xX3w9fZ7nlbzuimuQvpIHXo(this)), new SettingsActivity.SearchAdapter.SearchResult(211, LocaleController.getString("ResetAutomaticMediaDownload", 2131560590), "resetDownloadRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$LMKnGgygm7Zy5y5yA0ZMoVKQ0rQ(this)), new SettingsActivity.SearchAdapter.SearchResult(212, LocaleController.getString("AutoplayMedia", 2131558804), "autoplayHeaderRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$lrHAN36teH_lCOFJ5PXNFKTwHTM(this)), new SettingsActivity.SearchAdapter.SearchResult(213, LocaleController.getString("AutoplayGIF", 2131558803), "autoplayGifsRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$raORBf9hVrFvO0t2rS9X8ZjwExo(this)), new SettingsActivity.SearchAdapter.SearchResult(214, LocaleController.getString("AutoplayVideo", 2131558805), "autoplayVideoRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$ZTzEE1deo_nmR_UL_VYD8uhoHoI(this)), new SettingsActivity.SearchAdapter.SearchResult(215, LocaleController.getString("Streaming", 2131560833), "streamSectionRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$rOx4Eve_2YVGzrlUGIGIovv9JXE(this)), new SettingsActivity.SearchAdapter.SearchResult(216, LocaleController.getString("EnableStreaming", 2131559349), "enableStreamRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$rGwlYZXuIw_8MDI7I45SYbMs0G8(this)), new SettingsActivity.SearchAdapter.SearchResult(217, LocaleController.getString("Calls", 2131558888), "callsSectionRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$yQdZ2pbhXfP1NJfQiJKJVPctbyk(this)), new SettingsActivity.SearchAdapter.SearchResult(218, LocaleController.getString("VoipUseLessData", 2131561093), "useLessDataForCallsRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$B4etMl_s5sjFGhMYip4XHa1DxEY(this)), new SettingsActivity.SearchAdapter.SearchResult(219, LocaleController.getString("VoipQuickReplies", 2131561086), "quickRepliesRow", LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$i2kSSNMxTtfC03RmjV3ptURjCLA(this)), new SettingsActivity.SearchAdapter.SearchResult(220, LocaleController.getString("ProxySettings", 2131560519), LocaleController.getString("DataSettings", 2131559193), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$XMZSE9GUh_0GP5dBwOjHByrjteo(this)), new SettingsActivity.SearchAdapter.SearchResult(221, LocaleController.getString("UseProxyForCalls", 2131560972), "callsRow", LocaleController.getString("DataSettings", 2131559193), LocaleController.getString("ProxySettings", 2131560519), 2131165579, new _$$Lambda$SettingsActivity$SearchAdapter$t63Mb0PWa1_oMCnIjcS62wWkWhI(this)), new SettingsActivity.SearchAdapter.SearchResult(300, LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$oHMz_dP6H50aHqxTbdc3E7d4Oyo(this)), new SettingsActivity.SearchAdapter.SearchResult(301, LocaleController.getString("TextSizeHeader", 2131560888), "textSizeHeaderRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$PQPKVfMuNVXvznSLWUi9d_FbGD8(this)), new SettingsActivity.SearchAdapter.SearchResult(302, LocaleController.getString("ChatBackground", 2131559024), LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$hzumI77K9pwC4a7mUUTUW31jkYs(this)), new SettingsActivity.SearchAdapter.SearchResult(303, LocaleController.getString("SetColor", 2131560733), (String)null, LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("ChatBackground", 2131559024), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$Ov5OcCfPmPzmx4efoTSoB8_YMKk(this)), new SettingsActivity.SearchAdapter.SearchResult(304, LocaleController.getString("ResetChatBackgrounds", 2131560593), "resetRow", LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("ChatBackground", 2131559024), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$mEajmgrt04XurJBLXFyM6GNHru0(this)), new SettingsActivity.SearchAdapter.SearchResult(305, LocaleController.getString("AutoNightTheme", 2131558791), LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$vSOlOOSDVPitPhRnpx1o7rNw4zw(this)), new SettingsActivity.SearchAdapter.SearchResult(306, LocaleController.getString("ColorTheme", 2131559129), "themeHeaderRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$46GIfyE4OhRvg3Gs4uaPMW_J3UM(this)), new SettingsActivity.SearchAdapter.SearchResult(307, LocaleController.getString("ChromeCustomTabs", 2131559100), "customTabsRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$s0h_sbhuVis6N_TS7j6bEyh7FfQ(this)), new SettingsActivity.SearchAdapter.SearchResult(308, LocaleController.getString("DirectShare", 2131559268), "directShareRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$5_PGzT67poE7m5IzEQLBofn8u_o(this)), new SettingsActivity.SearchAdapter.SearchResult(309, LocaleController.getString("EnableAnimations", 2131559348), "enableAnimationsRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$djYxV0TLWfpCNMAt_hbj8er6rqo(this)), new SettingsActivity.SearchAdapter.SearchResult(310, LocaleController.getString("RaiseToSpeak", 2131560528), "raiseToSpeakRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$NM0akNEGYlYo_ZnAPY_boq0dLjY(this)), new SettingsActivity.SearchAdapter.SearchResult(311, LocaleController.getString("SendByEnter", 2131560688), "sendByEnterRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$WOvivT9gT9S9mbWbIsPI_YD8I38(this)), new SettingsActivity.SearchAdapter.SearchResult(312, LocaleController.getString("SaveToGallerySettings", 2131560631), "saveToGalleryRow", LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$LE1pN2xBggeckg4BXNGrk8dAfCA(this)), new SettingsActivity.SearchAdapter.SearchResult(313, LocaleController.getString("StickersAndMasks", 2131560807), LocaleController.getString("ChatSettings", 2131559043), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$guZCAugp_k8fJ3Gd84aSwGDA7co(this)), new SettingsActivity.SearchAdapter.SearchResult(314, LocaleController.getString("SuggestStickers", 2131560843), "suggestRow", LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("StickersAndMasks", 2131560807), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$a2LSsVzKR8HJrNBIEh5xSXFk_kY(this)), new SettingsActivity.SearchAdapter.SearchResult(315, LocaleController.getString("FeaturedStickers", 2131559479), (String)null, LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("StickersAndMasks", 2131560807), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$vTZmp1Lfwd9Jvf16tEr9C2amaYY(this)), new SettingsActivity.SearchAdapter.SearchResult(316, LocaleController.getString("Masks", 2131559809), (String)null, LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("StickersAndMasks", 2131560807), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$O_sT8FF_ujolqkcXgAFR2EWMCOQ(this)), new SettingsActivity.SearchAdapter.SearchResult(317, LocaleController.getString("ArchivedStickers", 2131558659), (String)null, LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("StickersAndMasks", 2131560807), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$dg765ZjLDUr492FSgTOKjOaKweE(this)), new SettingsActivity.SearchAdapter.SearchResult(317, LocaleController.getString("ArchivedMasks", 2131558654), (String)null, LocaleController.getString("ChatSettings", 2131559043), LocaleController.getString("StickersAndMasks", 2131560807), 2131165574, new _$$Lambda$SettingsActivity$SearchAdapter$h743V_ShaSJwbDnqAuxpCdWSljA(this)), new SettingsActivity.SearchAdapter.SearchResult(400, LocaleController.getString("Language", 2131559715), 2131165586, new _$$Lambda$SettingsActivity$SearchAdapter$6umUiixsrbn0nLlRd8bqshWjNeU(this)), new SettingsActivity.SearchAdapter.SearchResult(401, LocaleController.getString("SettingsHelp", 2131560740), 2131165582, new _$$Lambda$SettingsActivity$SearchAdapter$_pkjlu7nZwc3q0jMwngyqVOHNlk(SettingsActivity.this)), new SettingsActivity.SearchAdapter.SearchResult(402, LocaleController.getString("AskAQuestion", 2131558706), LocaleController.getString("SettingsHelp", 2131560740), 2131165582, new _$$Lambda$SettingsActivity$SearchAdapter$UKpWGlt__8lNdI9VfZj9_2dBm0k(this)), new SettingsActivity.SearchAdapter.SearchResult(403, LocaleController.getString("TelegramFAQ", 2131560869), LocaleController.getString("SettingsHelp", 2131560740), 2131165582, new _$$Lambda$SettingsActivity$SearchAdapter$dYFPJFhuxdXwNU2N5QyWTOm1hz8(this)), new SettingsActivity.SearchAdapter.SearchResult(404, LocaleController.getString("PrivacyPolicy", 2131560502), LocaleController.getString("SettingsHelp", 2131560740), 2131165582, new _$$Lambda$SettingsActivity$SearchAdapter$98mN5UJETCB_Tj6G_z_MUvkMLLk(this))};
      private ArrayList searchResults = new ArrayList();
      private Runnable searchRunnable;
      private boolean searchWas;

      public SearchAdapter(Context var2) {
         this.mContext = var2;
         HashMap var3 = new HashMap();
         int var4 = 0;

         while(true) {
            SettingsActivity.SearchAdapter.SearchResult[] var17 = this.searchArray;
            if (var4 >= var17.length) {
               Set var18 = MessagesController.getGlobalMainSettings().getStringSet("settingsSearchRecent2", (Set)null);
               if (var18 != null) {
                  Iterator var5 = var18.iterator();

                  label90:
                  while(true) {
                     SerializedData var6;
                     int var7;
                     boolean var10001;
                     do {
                        label74:
                        while(true) {
                           if (!var5.hasNext()) {
                              break label90;
                           }

                           String var19 = (String)var5.next();

                           try {
                              var6 = new SerializedData(Utilities.hexToBytes(var19));
                              var7 = var6.readInt32(false);
                              var4 = var6.readInt32(false);
                           } catch (Exception var14) {
                              var10001 = false;
                              continue;
                           }

                           if (var4 != 0) {
                              break;
                           }

                           String var8;
                           int var9;
                           try {
                              var8 = var6.readString(false);
                              var9 = var6.readInt32(false);
                           } catch (Exception var13) {
                              var10001 = false;
                              continue;
                           }

                           String[] var20 = null;
                           if (var9 > 0) {
                              String[] var21;
                              try {
                                 var21 = new String[var9];
                              } catch (Exception var12) {
                                 var10001 = false;
                                 continue;
                              }

                              var4 = 0;

                              while(true) {
                                 var20 = var21;
                                 if (var4 >= var9) {
                                    break;
                                 }

                                 try {
                                    var21[var4] = var6.readString(false);
                                 } catch (Exception var11) {
                                    var10001 = false;
                                    continue label74;
                                 }

                                 ++var4;
                              }
                           }

                           try {
                              String var22 = var6.readString(false);
                              SettingsActivity.SearchAdapter.FaqSearchResult var24 = new SettingsActivity.SearchAdapter.FaqSearchResult(var8, var20, var22);
                              var24.num = var7;
                              this.recentSearches.add(var24);
                           } catch (Exception var10) {
                              var10001 = false;
                           }
                        }
                     } while(var4 != 1);

                     SettingsActivity.SearchAdapter.SearchResult var23;
                     try {
                        var23 = (SettingsActivity.SearchAdapter.SearchResult)var3.get(var6.readInt32(false));
                     } catch (Exception var16) {
                        var10001 = false;
                        continue;
                     }

                     if (var23 != null) {
                        try {
                           var23.num = var7;
                           this.recentSearches.add(var23);
                        } catch (Exception var15) {
                           var10001 = false;
                        }
                     }
                  }
               }

               Collections.sort(this.recentSearches, new _$$Lambda$SettingsActivity$SearchAdapter$8ejWl7szf_KNcJH1StMby0vJtdc(this));
               return;
            }

            var3.put(var17[var4].guid, this.searchArray[var4]);
            ++var4;
         }
      }

      private int getNum(Object var1) {
         if (var1 instanceof SettingsActivity.SearchAdapter.SearchResult) {
            return ((SettingsActivity.SearchAdapter.SearchResult)var1).num;
         } else {
            return var1 instanceof SettingsActivity.SearchAdapter.FaqSearchResult ? ((SettingsActivity.SearchAdapter.FaqSearchResult)var1).num : 0;
         }
      }

      // $FF: synthetic method
      static void lambda$new$77(SettingsActivity var0) {
         var0.showHelpAlert();
      }

      private void loadFaqWebPage() {
         if (this.faqWebPage == null && !this.loadingFaqPage) {
            this.loadingFaqPage = true;
            TLRPC.TL_messages_getWebPage var1 = new TLRPC.TL_messages_getWebPage();
            var1.url = LocaleController.getString("TelegramFaqUrl", 2131560871);
            var1.hash = 0;
            ConnectionsManager.getInstance(SettingsActivity.access$4100(SettingsActivity.this)).sendRequest(var1, new _$$Lambda$SettingsActivity$SearchAdapter$mPyLXiUd_cjxJI7NTOZqkYVtkUM(this));
         }

      }

      public void addRecent(Object var1) {
         int var2 = this.recentSearches.indexOf(var1);
         if (var2 >= 0) {
            this.recentSearches.remove(var2);
         }

         ArrayList var3 = this.recentSearches;
         var2 = 0;
         var3.add(0, var1);
         if (!this.searchWas) {
            this.notifyDataSetChanged();
         }

         if (this.recentSearches.size() > 20) {
            ArrayList var5 = this.recentSearches;
            var5.remove(var5.size() - 1);
         }

         LinkedHashSet var6 = new LinkedHashSet();

         for(int var4 = this.recentSearches.size(); var2 < var4; ++var2) {
            Object var7 = this.recentSearches.get(var2);
            if (var7 instanceof SettingsActivity.SearchAdapter.SearchResult) {
               ((SettingsActivity.SearchAdapter.SearchResult)var7).num = var2;
            } else if (var7 instanceof SettingsActivity.SearchAdapter.FaqSearchResult) {
               ((SettingsActivity.SearchAdapter.FaqSearchResult)var7).num = var2;
            }

            var6.add(var7.toString());
         }

         MessagesController.getGlobalMainSettings().edit().putStringSet("settingsSearchRecent2", var6).commit();
      }

      public void clearRecent() {
         this.recentSearches.clear();
         MessagesController.getGlobalMainSettings().edit().remove("settingsSearchRecent2").commit();
         this.notifyDataSetChanged();
      }

      public int getItemCount() {
         boolean var1 = this.searchWas;
         byte var2 = 0;
         int var3 = 0;
         if (var1) {
            int var4 = this.searchResults.size();
            if (!this.faqSearchResults.isEmpty()) {
               var3 = this.faqSearchResults.size() + 1;
            }

            return var4 + var3;
         } else {
            if (this.recentSearches.isEmpty()) {
               var3 = var2;
            } else {
               var3 = this.recentSearches.size() + 1;
            }

            return var3;
         }
      }

      public int getItemViewType(int var1) {
         if (this.searchWas) {
            if (var1 < this.searchResults.size()) {
               return 0;
            } else {
               return var1 == this.searchResults.size() ? 1 : 0;
            }
         } else {
            return var1 == 0 ? 2 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      // $FF: synthetic method
      public void lambda$loadFaqWebPage$82$SettingsActivity$SearchAdapter(TLObject var1, TLRPC.TL_error var2) {
         if (var1 instanceof TLRPC.WebPage) {
            TLRPC.WebPage var3 = (TLRPC.WebPage)var1;
            TLRPC.Page var11 = var3.cached_page;
            if (var11 != null) {
               int var4 = var11.blocks.size();

               for(int var5 = 0; var5 < var4; ++var5) {
                  TLRPC.PageBlock var6 = (TLRPC.PageBlock)var3.cached_page.blocks.get(var5);
                  if (var6 instanceof TLRPC.TL_pageBlockList) {
                     var2 = null;
                     String var12 = var2;
                     if (var5 != 0) {
                        TLRPC.PageBlock var7 = (TLRPC.PageBlock)var3.cached_page.blocks.get(var5 - 1);
                        var12 = var2;
                        if (var7 instanceof TLRPC.TL_pageBlockParagraph) {
                           var12 = ArticleViewer.getPlainText(((TLRPC.TL_pageBlockParagraph)var7).text).toString();
                        }
                     }

                     TLRPC.TL_pageBlockList var16 = (TLRPC.TL_pageBlockList)var6;
                     int var8 = var16.items.size();

                     for(int var9 = 0; var9 < var8; ++var9) {
                        TLRPC.PageListItem var13 = (TLRPC.PageListItem)var16.items.get(var9);
                        if (var13 instanceof TLRPC.TL_pageListItemText) {
                           TLRPC.TL_pageListItemText var14 = (TLRPC.TL_pageListItemText)var13;
                           String var17 = ArticleViewer.getUrl(var14.text);
                           String var10 = ArticleViewer.getPlainText(var14.text).toString();
                           if (!TextUtils.isEmpty(var17) && !TextUtils.isEmpty(var10)) {
                              String[] var15;
                              if (var12 != null) {
                                 var15 = new String[]{LocaleController.getString("SettingsSearchFaq", 2131560744), var12};
                              } else {
                                 var15 = new String[]{LocaleController.getString("SettingsSearchFaq", 2131560744)};
                              }

                              this.faqSearchArray.add(new SettingsActivity.SearchAdapter.FaqSearchResult(var10, var15, var17));
                           }
                        }
                     }
                  } else if (var6 instanceof TLRPC.TL_pageBlockAnchor) {
                     break;
                  }
               }

               this.faqWebPage = var3;
            }
         }

         this.loadingFaqPage = false;
      }

      // $FF: synthetic method
      public void lambda$new$0$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ChangeNameActivity());
      }

      // $FF: synthetic method
      public void lambda$new$1$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ChangePhoneHelpActivity());
      }

      // $FF: synthetic method
      public void lambda$new$10$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$11$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$12$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$13$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$14$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$15$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyUsersActivity());
      }

      // $FF: synthetic method
      public void lambda$new$16$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(6, true));
      }

      // $FF: synthetic method
      public void lambda$new$17$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(0, true));
      }

      // $FF: synthetic method
      public void lambda$new$18$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(4, true));
      }

      // $FF: synthetic method
      public void lambda$new$19$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(5, true));
      }

      // $FF: synthetic method
      public void lambda$new$2$SettingsActivity$SearchAdapter() {
         int var1 = 0;

         while(true) {
            if (var1 >= 3) {
               var1 = -1;
               break;
            }

            if (!UserConfig.getInstance(var1).isClientActivated()) {
               break;
            }

            ++var1;
         }

         if (var1 >= 0) {
            SettingsActivity.this.presentFragment(new LoginActivity(var1));
         }

      }

      // $FF: synthetic method
      public void lambda$new$20$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(3, true));
      }

      // $FF: synthetic method
      public void lambda$new$21$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(2, true));
      }

      // $FF: synthetic method
      public void lambda$new$22$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacyControlActivity(1, true));
      }

      // $FF: synthetic method
      public void lambda$new$23$SettingsActivity$SearchAdapter() {
         SettingsActivity var1 = SettingsActivity.this;
         byte var2;
         if (SharedConfig.passcodeHash.length() > 0) {
            var2 = 2;
         } else {
            var2 = 0;
         }

         var1.presentFragment(new PasscodeActivity(var2));
      }

      // $FF: synthetic method
      public void lambda$new$24$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new TwoStepVerificationActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$25$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new SessionsActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$26$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$27$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$28$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$29$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new SessionsActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$3$SettingsActivity$SearchAdapter() {
         if (SettingsActivity.this.userInfo != null) {
            SettingsActivity.this.presentFragment(new ChangeBioActivity());
         }

      }

      // $FF: synthetic method
      public void lambda$new$30$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$31$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$32$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$33$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$34$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new PrivacySettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$35$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$36$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$37$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new CacheControlActivity());
      }

      // $FF: synthetic method
      public void lambda$new$38$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new CacheControlActivity());
      }

      // $FF: synthetic method
      public void lambda$new$39$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new CacheControlActivity());
      }

      // $FF: synthetic method
      public void lambda$new$4$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$40$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new CacheControlActivity());
      }

      // $FF: synthetic method
      public void lambda$new$41$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataUsageActivity());
      }

      // $FF: synthetic method
      public void lambda$new$42$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$43$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataAutoDownloadActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$44$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataAutoDownloadActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$45$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataAutoDownloadActivity(2));
      }

      // $FF: synthetic method
      public void lambda$new$46$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$47$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$48$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$49$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$5$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsCustomSettingsActivity(1, new ArrayList(), true));
      }

      // $FF: synthetic method
      public void lambda$new$50$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$51$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$52$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$53$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$54$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new DataSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$55$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ProxyListActivity());
      }

      // $FF: synthetic method
      public void lambda$new$56$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ProxyListActivity());
      }

      // $FF: synthetic method
      public void lambda$new$57$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$58$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$59$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new WallpapersListActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$6$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsCustomSettingsActivity(0, new ArrayList(), true));
      }

      // $FF: synthetic method
      public void lambda$new$60$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new WallpapersListActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$61$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new WallpapersListActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$62$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$63$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$64$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$65$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$66$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$67$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$68$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$69$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ThemeActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$7$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsCustomSettingsActivity(2, new ArrayList(), true));
      }

      // $FF: synthetic method
      public void lambda$new$70$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new StickersActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$71$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new StickersActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$72$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new FeaturedStickersActivity());
      }

      // $FF: synthetic method
      public void lambda$new$73$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new StickersActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$74$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ArchivedStickersActivity(0));
      }

      // $FF: synthetic method
      public void lambda$new$75$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new ArchivedStickersActivity(1));
      }

      // $FF: synthetic method
      public void lambda$new$76$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new LanguageSelectActivity());
      }

      // $FF: synthetic method
      public void lambda$new$78$SettingsActivity$SearchAdapter() {
         SettingsActivity var1 = SettingsActivity.this;
         var1.showDialog(AlertsCreator.createSupportAlert(var1));
      }

      // $FF: synthetic method
      public void lambda$new$79$SettingsActivity$SearchAdapter() {
         Browser.openUrl(SettingsActivity.this.getParentActivity(), (String)LocaleController.getString("TelegramFaqUrl", 2131560871));
      }

      // $FF: synthetic method
      public void lambda$new$8$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$new$80$SettingsActivity$SearchAdapter() {
         Browser.openUrl(SettingsActivity.this.getParentActivity(), (String)LocaleController.getString("PrivacyPolicyUrl", 2131560504));
      }

      // $FF: synthetic method
      public int lambda$new$81$SettingsActivity$SearchAdapter(Object var1, Object var2) {
         int var3 = this.getNum(var1);
         int var4 = this.getNum(var2);
         if (var3 < var4) {
            return -1;
         } else {
            return var3 > var4 ? 1 : 0;
         }
      }

      // $FF: synthetic method
      public void lambda$new$9$SettingsActivity$SearchAdapter() {
         SettingsActivity.this.presentFragment(new NotificationsSettingsActivity());
      }

      // $FF: synthetic method
      public void lambda$null$83$SettingsActivity$SearchAdapter(String var1, ArrayList var2, ArrayList var3, ArrayList var4) {
         if (var1.equals(this.lastSearchString)) {
            if (!this.searchWas) {
               SettingsActivity.this.emptyView.setTopImage(2131165816);
               SettingsActivity.this.emptyView.setText(LocaleController.getString("SettingsNoResults", 2131560742));
            }

            this.searchWas = true;
            this.searchResults = var2;
            this.faqSearchResults = var3;
            this.resultNames = var4;
            this.notifyDataSetChanged();
         }
      }

      // $FF: synthetic method
      public void lambda$search$84$SettingsActivity$SearchAdapter(String var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         String var5 = " ";
         String[] var6 = var1.split(" ");
         String[] var7 = new String[var6.length];

         int var8;
         for(var8 = 0; var8 < var6.length; ++var8) {
            var7[var8] = LocaleController.getInstance().getTranslitString(var6[var8]);
            if (var7[var8].equals(var6[var8])) {
               var7[var8] = null;
            }
         }

         int var9 = 0;

         while(true) {
            SettingsActivity.SearchAdapter.SearchResult[] var10 = this.searchArray;
            int var13;
            StringBuilder var15;
            int var16;
            String var27;
            if (var9 >= var10.length) {
               if (this.faqWebPage != null) {
                  var8 = this.faqSearchArray.size();
                  var9 = 0;

                  for(String var22 = var5; var9 < var8; ++var9) {
                     SettingsActivity.SearchAdapter.FaqSearchResult var26 = (SettingsActivity.SearchAdapter.FaqSearchResult)this.faqSearchArray.get(var9);
                     StringBuilder var23 = new StringBuilder();
                     var23.append(var22);
                     var23.append(var26.title.toLowerCase());
                     String var21 = var23.toString();
                     var13 = 0;

                     for(SpannableStringBuilder var24 = null; var13 < var6.length; ++var13) {
                        if (var6[var13].length() != 0) {
                           var5 = var6[var13];
                           var15 = new StringBuilder();
                           var15.append(var22);
                           var15.append(var5);
                           int var28 = var21.indexOf(var15.toString());
                           var27 = var5;
                           var16 = var28;
                           if (var28 < 0) {
                              var27 = var5;
                              var16 = var28;
                              if (var7[var13] != null) {
                                 var27 = var7[var13];
                                 StringBuilder var18 = new StringBuilder();
                                 var18.append(var22);
                                 var18.append(var27);
                                 var16 = var21.indexOf(var18.toString());
                              }
                           }

                           if (var16 < 0) {
                              break;
                           }

                           if (var24 == null) {
                              var24 = new SpannableStringBuilder(var26.title);
                           }

                           var24.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), var16, var27.length() + var16, 33);
                        }

                        if (var24 != null && var13 == var6.length - 1) {
                           var3.add(var26);
                           var4.add(var24);
                        }
                     }
                  }
               }

               AndroidUtilities.runOnUIThread(new _$$Lambda$SettingsActivity$SearchAdapter$Wj8gABgoRYs1_hhXXnmqoCxVN1M(this, var1, var2, var3, var4));
               return;
            }

            SettingsActivity.SearchAdapter.SearchResult var11 = var10[var9];
            StringBuilder var19 = new StringBuilder();
            var19.append(" ");
            var19.append(var11.searchTitle.toLowerCase());
            String var12 = var19.toString();
            SpannableStringBuilder var20 = null;

            for(var13 = 0; var13 < var6.length; ++var13) {
               if (var6[var13].length() != 0) {
                  String var14 = var6[var13];
                  var15 = new StringBuilder();
                  var15.append(" ");
                  var15.append(var14);
                  var16 = var12.indexOf(var15.toString());
                  var27 = var14;
                  var8 = var16;
                  if (var16 < 0) {
                     var27 = var14;
                     var8 = var16;
                     if (var7[var13] != null) {
                        var27 = var7[var13];
                        StringBuilder var25 = new StringBuilder();
                        var25.append(" ");
                        var25.append(var27);
                        var8 = var12.indexOf(var25.toString());
                     }
                  }

                  if (var8 < 0) {
                     break;
                  }

                  if (var20 == null) {
                     var20 = new SpannableStringBuilder(var11.searchTitle);
                  }

                  var20.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), var8, var27.length() + var8, 33);
               }

               if (var20 != null && var13 == var6.length - 1) {
                  if (var11.guid == 502) {
                     byte var17 = -1;
                     var8 = 0;

                     while(true) {
                        var16 = var17;
                        if (var8 >= 3) {
                           break;
                        }

                        if (!UserConfig.getInstance(var9).isClientActivated()) {
                           var16 = var8;
                           break;
                        }

                        ++var8;
                     }

                     if (var16 < 0) {
                        continue;
                     }
                  }

                  var2.add(var11);
                  var4.add(var20);
               }
            }

            ++var9;
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         boolean var5 = true;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  ((HeaderCell)var1.itemView).setText(LocaleController.getString("SettingsRecent", 2131560743));
               }
            } else {
               ((GraySectionCell)var1.itemView).setText(LocaleController.getString("SettingsFaqSearchTitle", 2131560739));
            }
         } else {
            SettingsSearchCell var6 = (SettingsSearchCell)var1.itemView;
            boolean var7 = this.searchWas;
            boolean var8 = false;
            boolean var9 = false;
            SettingsActivity.SearchAdapter.SearchResult var10;
            String[] var15;
            SettingsActivity.SearchAdapter.FaqSearchResult var16;
            if (var7) {
               CharSequence var12;
               if (var2 < this.searchResults.size()) {
                  var10 = (SettingsActivity.SearchAdapter.SearchResult)this.searchResults.get(var2);
                  SettingsActivity.SearchAdapter.SearchResult var11;
                  if (var2 > 0) {
                     var11 = (SettingsActivity.SearchAdapter.SearchResult)this.searchResults.get(var2 - 1);
                  } else {
                     var11 = null;
                  }

                  if (var11 != null && var11.iconResId == var10.iconResId) {
                     var3 = 0;
                  } else {
                     var3 = var10.iconResId;
                  }

                  var12 = (CharSequence)this.resultNames.get(var2);
                  var15 = var10.path;
                  if (var2 < this.searchResults.size() - 1) {
                     var9 = var5;
                  } else {
                     var9 = false;
                  }

                  var6.setTextAndValueAndIcon(var12, var15, var3, var9);
               } else {
                  var2 -= this.searchResults.size() + 1;
                  var16 = (SettingsActivity.SearchAdapter.FaqSearchResult)this.faqSearchResults.get(var2);
                  var12 = (CharSequence)this.resultNames.get(this.searchResults.size() + var2);
                  var15 = var16.path;
                  if (var2 < this.searchResults.size() - 1) {
                     var9 = true;
                  }

                  var6.setTextAndValue(var12, var15, true, var9);
               }
            } else {
               --var2;
               Object var13 = this.recentSearches.get(var2);
               String var14;
               if (var13 instanceof SettingsActivity.SearchAdapter.SearchResult) {
                  var10 = (SettingsActivity.SearchAdapter.SearchResult)var13;
                  var14 = var10.searchTitle;
                  var15 = var10.path;
                  if (var2 < this.recentSearches.size() - 1) {
                     var9 = var4;
                  } else {
                     var9 = false;
                  }

                  var6.setTextAndValue(var14, var15, false, var9);
               } else if (var13 instanceof SettingsActivity.SearchAdapter.FaqSearchResult) {
                  var16 = (SettingsActivity.SearchAdapter.FaqSearchResult)var13;
                  var14 = var16.title;
                  var15 = var16.path;
                  var9 = var8;
                  if (var2 < this.recentSearches.size() - 1) {
                     var9 = true;
                  }

                  var6.setTextAndValue(var14, var15, true, var9);
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new HeaderCell(this.mContext, 16);
            } else {
               var3 = new GraySectionCell(this.mContext);
            }
         } else {
            var3 = new SettingsSearchCell(this.mContext);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }

      public void search(String var1) {
         this.lastSearchString = var1;
         if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
         }

         if (TextUtils.isEmpty(var1)) {
            this.searchWas = false;
            this.searchResults.clear();
            this.faqSearchResults.clear();
            this.resultNames.clear();
            SettingsActivity.this.emptyView.setTopImage(0);
            SettingsActivity.this.emptyView.setText(LocaleController.getString("SettingsNoRecent", 2131560741));
            this.notifyDataSetChanged();
         } else {
            DispatchQueue var2 = Utilities.searchQueue;
            _$$Lambda$SettingsActivity$SearchAdapter$2i3P8i9DdS78PBZMuonLkeibTUg var3 = new _$$Lambda$SettingsActivity$SearchAdapter$2i3P8i9DdS78PBZMuonLkeibTUg(this, var1);
            this.searchRunnable = var3;
            var2.postRunnable(var3, 300L);
         }
      }

      private class FaqSearchResult {
         private int num;
         private String[] path;
         private String title;
         private String url;

         public FaqSearchResult(String var2, String[] var3, String var4) {
            this.title = var2;
            this.path = var3;
            this.url = var4;
         }

         public boolean equals(Object var1) {
            if (!(var1 instanceof SettingsActivity.SearchAdapter.FaqSearchResult)) {
               return false;
            } else {
               SettingsActivity.SearchAdapter.FaqSearchResult var2 = (SettingsActivity.SearchAdapter.FaqSearchResult)var1;
               return this.title.equals(var2.title);
            }
         }

         public String toString() {
            SerializedData var1 = new SerializedData();
            var1.writeInt32(this.num);
            byte var2 = 0;
            var1.writeInt32(0);
            var1.writeString(this.title);
            String[] var3 = this.path;
            int var4;
            if (var3 != null) {
               var4 = var3.length;
            } else {
               var4 = 0;
            }

            var1.writeInt32(var4);
            if (this.path != null) {
               var4 = var2;

               while(true) {
                  var3 = this.path;
                  if (var4 >= var3.length) {
                     break;
                  }

                  var1.writeString(var3[var4]);
                  ++var4;
               }
            }

            var1.writeString(this.url);
            return Utilities.bytesToHex(var1.toByteArray());
         }
      }

      private class SearchResult {
         private int guid;
         private int iconResId;
         private int num;
         private Runnable openRunnable;
         private String[] path;
         private String rowName;
         private String searchTitle;

         public SearchResult(int var2, String var3, int var4, Runnable var5) {
            this(var2, var3, (String)null, (String)null, (String)null, var4, var5);
         }

         public SearchResult(int var2, String var3, String var4, int var5, Runnable var6) {
            this(var2, var3, (String)null, var4, (String)null, var5, var6);
         }

         public SearchResult(int var2, String var3, String var4, String var5, int var6, Runnable var7) {
            this(var2, var3, var4, var5, (String)null, var6, var7);
         }

         public SearchResult(int var2, String var3, String var4, String var5, String var6, int var7, Runnable var8) {
            this.guid = var2;
            this.searchTitle = var3;
            this.rowName = var4;
            this.openRunnable = var8;
            this.iconResId = var7;
            if (var5 != null && var6 != null) {
               this.path = new String[]{var5, var6};
            } else if (var5 != null) {
               this.path = new String[]{var5};
            }

         }

         private void open() {
            this.openRunnable.run();
            if (this.rowName != null) {
               BaseFragment var1 = (BaseFragment)SettingsActivity.access$3700(SettingsActivity.this).fragmentsStack.get(SettingsActivity.access$3600(SettingsActivity.this).fragmentsStack.size() - 1);

               try {
                  Field var2 = var1.getClass().getDeclaredField("listView");
                  var2.setAccessible(true);
                  _$$Lambda$SettingsActivity$SearchAdapter$SearchResult$QTQolgPlTMmAnvlmOMDH38UI6H4 var3 = new _$$Lambda$SettingsActivity$SearchAdapter$SearchResult$QTQolgPlTMmAnvlmOMDH38UI6H4(this, var1);
                  ((RecyclerListView)var2.get(var1)).highlightRow(var3);
                  var2.setAccessible(false);
               } catch (Throwable var4) {
               }
            }

         }

         public boolean equals(Object var1) {
            boolean var2 = var1 instanceof SettingsActivity.SearchAdapter.SearchResult;
            boolean var3 = false;
            if (!var2) {
               return false;
            } else {
               SettingsActivity.SearchAdapter.SearchResult var4 = (SettingsActivity.SearchAdapter.SearchResult)var1;
               if (this.guid == var4.guid) {
                  var3 = true;
               }

               return var3;
            }
         }

         // $FF: synthetic method
         public int lambda$open$0$SettingsActivity$SearchAdapter$SearchResult(BaseFragment var1) {
            byte var2 = -1;
            int var3 = var2;

            boolean var10001;
            Field var4;
            try {
               var4 = var1.getClass().getDeclaredField(this.rowName);
            } catch (Throwable var15) {
               var10001 = false;
               return var3;
            }

            var3 = var2;

            Field var5;
            try {
               var5 = var1.getClass().getDeclaredField("layoutManager");
            } catch (Throwable var14) {
               var10001 = false;
               return var3;
            }

            var3 = var2;

            try {
               var4.setAccessible(true);
            } catch (Throwable var13) {
               var10001 = false;
               return var3;
            }

            var3 = var2;

            try {
               var5.setAccessible(true);
            } catch (Throwable var12) {
               var10001 = false;
               return var3;
            }

            var3 = var2;

            LinearLayoutManager var6;
            try {
               var6 = (LinearLayoutManager)var5.get(var1);
            } catch (Throwable var11) {
               var10001 = false;
               return var3;
            }

            var3 = var2;

            int var16;
            try {
               var16 = var4.getInt(var1);
            } catch (Throwable var10) {
               var10001 = false;
               return var3;
            }

            var3 = var16;

            try {
               var6.scrollToPositionWithOffset(var16, 0);
            } catch (Throwable var9) {
               var10001 = false;
               return var3;
            }

            var3 = var16;

            try {
               var4.setAccessible(false);
            } catch (Throwable var8) {
               var10001 = false;
               return var3;
            }

            var3 = var16;

            try {
               var5.setAccessible(false);
            } catch (Throwable var7) {
               var10001 = false;
               return var3;
            }

            var3 = var16;
            return var3;
         }

         public String toString() {
            SerializedData var1 = new SerializedData();
            var1.writeInt32(this.num);
            var1.writeInt32(1);
            var1.writeInt32(this.guid);
            return Utilities.bytesToHex(var1.toByteArray());
         }
      }
   }
}
