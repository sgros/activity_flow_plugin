package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;

public class GroupCreateFinalActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ImageUpdater.ImageUpdaterDelegate {
   private static final int done_button = 1;
   private GroupCreateFinalActivity.GroupCreateAdapter adapter;
   private TLRPC.FileLocation avatar;
   private AnimatorSet avatarAnimation;
   private TLRPC.FileLocation avatarBig;
   private AvatarDrawable avatarDrawable;
   private ImageView avatarEditor;
   private BackupImageView avatarImage;
   private View avatarOverlay;
   private RadialProgressView avatarProgressView;
   private int chatType;
   private boolean createAfterUpload;
   private GroupCreateFinalActivity.GroupCreateFinalActivityDelegate delegate;
   private AnimatorSet doneItemAnimation;
   private boolean donePressed;
   private EditTextEmoji editText;
   private FrameLayout editTextContainer;
   private FrameLayout floatingButtonContainer;
   private ImageView floatingButtonIcon;
   private ImageUpdater imageUpdater;
   private RecyclerView listView;
   private String nameToSet;
   private ContextProgressView progressView;
   private int reqId;
   private ArrayList selectedContacts;
   private Drawable shadowDrawable;
   private TLRPC.InputFile uploadedAvatar;

   public GroupCreateFinalActivity(Bundle var1) {
      super(var1);
      this.chatType = var1.getInt("chatType", 0);
      this.avatarDrawable = new AvatarDrawable();
   }

   // $FF: synthetic method
   static ActionBar access$000(GroupCreateFinalActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$1500(GroupCreateFinalActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$200(GroupCreateFinalActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static boolean lambda$createView$1(View var0, MotionEvent var1) {
      return true;
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
                  GroupCreateFinalActivity.this.avatarAnimation = null;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (GroupCreateFinalActivity.this.avatarAnimation != null && GroupCreateFinalActivity.this.avatarEditor != null) {
                     if (var1) {
                        GroupCreateFinalActivity.this.avatarEditor.setVisibility(4);
                     } else {
                        GroupCreateFinalActivity.this.avatarProgressView.setVisibility(4);
                     }

                     GroupCreateFinalActivity.this.avatarAnimation = null;
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

   private void showEditDoneProgress(final boolean var1) {
      if (this.floatingButtonIcon != null) {
         AnimatorSet var2 = this.doneItemAnimation;
         if (var2 != null) {
            var2.cancel();
         }

         this.doneItemAnimation = new AnimatorSet();
         if (var1) {
            this.progressView.setVisibility(0);
            this.floatingButtonContainer.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.floatingButtonIcon, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.floatingButtonIcon, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.floatingButtonIcon, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{1.0F})});
         } else {
            this.floatingButtonIcon.setVisibility(0);
            this.floatingButtonContainer.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.floatingButtonIcon, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.floatingButtonIcon, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.floatingButtonIcon, "alpha", new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (GroupCreateFinalActivity.this.doneItemAnimation != null && GroupCreateFinalActivity.this.doneItemAnimation.equals(var1x)) {
                  GroupCreateFinalActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (GroupCreateFinalActivity.this.doneItemAnimation != null && GroupCreateFinalActivity.this.doneItemAnimation.equals(var1x)) {
                  if (!var1) {
                     GroupCreateFinalActivity.this.progressView.setVisibility(4);
                  } else {
                     GroupCreateFinalActivity.this.floatingButtonIcon.setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      }
   }

   public View createView(Context var1) {
      EditTextEmoji var2 = this.editText;
      if (var2 != null) {
         var2.onDestroy();
      }

      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("NewGroup", 2131559900));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               GroupCreateFinalActivity.this.finishFragment();
            }

         }
      });
      SizeNotifierFrameLayout var3 = new SizeNotifierFrameLayout(var1) {
         private boolean ignoreLayout;

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.getChildCount();
            int var7 = this.getKeyboardHeight();
            int var8 = AndroidUtilities.dp(20.0F);
            int var9 = 0;
            if (var7 <= var8 && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
               var8 = GroupCreateFinalActivity.this.editText.getEmojiPadding();
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

                        var7 = var4 - var12;
                        var14 = var11.rightMargin;
                     } else {
                        var7 = (var4 - var2 - var12) / 2 + var11.leftMargin;
                        var14 = var11.rightMargin;
                     }

                     var14 = var7 - var14;
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

                        var7 = var5 - var8 - var3 - var13;
                        var15 = var11.bottomMargin;
                     } else {
                        var7 = (var5 - var8 - var3 - var13) / 2 + var11.topMargin;
                        var15 = var11.bottomMargin;
                     }

                     var7 -= var15;
                  }

                  var15 = var7;
                  if (GroupCreateFinalActivity.this.editText != null) {
                     var15 = var7;
                     if (GroupCreateFinalActivity.this.editText.isPopupView(var10)) {
                        if (AndroidUtilities.isTablet()) {
                           var15 = this.getMeasuredHeight();
                           var7 = var10.getMeasuredHeight();
                        } else {
                           var15 = this.getMeasuredHeight() + this.getKeyboardHeight();
                           var7 = var10.getMeasuredHeight();
                        }

                        var15 -= var7;
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
            this.measureChildWithMargins(GroupCreateFinalActivity.access$000(GroupCreateFinalActivity.this), var1, 0, var2, 0);
            int var6 = this.getKeyboardHeight();
            int var7 = AndroidUtilities.dp(20.0F);
            var4 = 0;
            if (var6 > var7) {
               this.ignoreLayout = true;
               GroupCreateFinalActivity.this.editText.hideEmojiView();
               this.ignoreLayout = false;
            }

            for(var7 = this.getChildCount(); var4 < var7; ++var4) {
               View var8 = this.getChildAt(var4);
               if (var8 != null && var8.getVisibility() != 8 && var8 != GroupCreateFinalActivity.access$200(GroupCreateFinalActivity.this)) {
                  if (GroupCreateFinalActivity.this.editText != null && GroupCreateFinalActivity.this.editText.isPopupView(var8)) {
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
      super.fragmentView = var3;
      super.fragmentView.setLayoutParams(new android.view.ViewGroup.LayoutParams(-1, -1));
      super.fragmentView.setOnTouchListener(_$$Lambda$GroupCreateFinalActivity$zr63HmKznA_wKqWBGncMkUB2788.INSTANCE);
      this.shadowDrawable = var1.getResources().getDrawable(2131165396).mutate();
      LinearLayout var4 = new LinearLayout(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == GroupCreateFinalActivity.this.listView && GroupCreateFinalActivity.this.shadowDrawable != null) {
               int var6 = GroupCreateFinalActivity.this.editTextContainer.getMeasuredHeight();
               GroupCreateFinalActivity.this.shadowDrawable.setBounds(0, var6, this.getMeasuredWidth(), GroupCreateFinalActivity.this.shadowDrawable.getIntrinsicHeight() + var6);
               GroupCreateFinalActivity.this.shadowDrawable.draw(var1);
            }

            return var5;
         }
      };
      var4.setOrientation(1);
      var3.addView(var4, LayoutHelper.createFrame(-1, -1.0F));
      this.editTextContainer = new FrameLayout(var1);
      var4.addView(this.editTextContainer, LayoutHelper.createLinear(-1, -2));
      this.avatarImage = new BackupImageView(var1) {
         public void invalidate() {
            if (GroupCreateFinalActivity.this.avatarOverlay != null) {
               GroupCreateFinalActivity.this.avatarOverlay.invalidate();
            }

            super.invalidate();
         }

         public void invalidate(int var1, int var2, int var3, int var4) {
            if (GroupCreateFinalActivity.this.avatarOverlay != null) {
               GroupCreateFinalActivity.this.avatarOverlay.invalidate();
            }

            super.invalidate(var1, var2, var3, var4);
         }
      };
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(32.0F));
      AvatarDrawable var14 = this.avatarDrawable;
      boolean var5;
      if (this.chatType == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      var14.setInfo(5, (String)null, (String)null, var5);
      this.avatarImage.setImageDrawable(this.avatarDrawable);
      this.avatarImage.setContentDescription(LocaleController.getString("ChoosePhoto", 2131559091));
      FrameLayout var6 = this.editTextContainer;
      BackupImageView var15 = this.avatarImage;
      var5 = LocaleController.isRTL;
      byte var7 = 3;
      byte var8;
      if (var5) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 16.0F;
      }

      float var10;
      if (LocaleController.isRTL) {
         var10 = 16.0F;
      } else {
         var10 = 0.0F;
      }

      var6.addView(var15, LayoutHelper.createFrame(64, 64.0F, var8 | 48, var9, 16.0F, var10, 16.0F));
      final Paint var16 = new Paint(1);
      var16.setColor(1426063360);
      this.avatarOverlay = new View(var1) {
         protected void onDraw(Canvas var1) {
            if (GroupCreateFinalActivity.this.avatarImage != null && GroupCreateFinalActivity.this.avatarProgressView.getVisibility() == 0) {
               var16.setAlpha((int)(GroupCreateFinalActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0F * GroupCreateFinalActivity.this.avatarProgressView.getAlpha()));
               var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0F), var16);
            }

         }
      };
      var6 = this.editTextContainer;
      View var17 = this.avatarOverlay;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var10 = 16.0F;
      } else {
         var10 = 0.0F;
      }

      var6.addView(var17, LayoutHelper.createFrame(64, 64.0F, var8 | 48, var9, 16.0F, var10, 16.0F));
      this.avatarOverlay.setOnClickListener(new _$$Lambda$GroupCreateFinalActivity$PfZeWUefXyO_B3uVzcUzv8fJmaM(this));
      this.avatarEditor = new ImageView(var1) {
         public void invalidate() {
            super.invalidate();
            GroupCreateFinalActivity.this.avatarOverlay.invalidate();
         }

         public void invalidate(int var1, int var2, int var3, int var4) {
            super.invalidate(var1, var2, var3, var4);
            GroupCreateFinalActivity.this.avatarOverlay.invalidate();
         }
      };
      this.avatarEditor.setScaleType(ScaleType.CENTER);
      this.avatarEditor.setImageResource(2131165276);
      this.avatarEditor.setEnabled(false);
      this.avatarEditor.setClickable(false);
      this.avatarEditor.setPadding(AndroidUtilities.dp(2.0F), 0, 0, 0);
      FrameLayout var19 = this.editTextContainer;
      ImageView var26 = this.avatarEditor;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var10 = 16.0F;
      } else {
         var10 = 0.0F;
      }

      var19.addView(var26, LayoutHelper.createFrame(64, 64.0F, var8 | 48, var9, 16.0F, var10, 16.0F));
      this.avatarProgressView = new RadialProgressView(var1) {
         public void setAlpha(float var1) {
            super.setAlpha(var1);
            GroupCreateFinalActivity.this.avatarOverlay.invalidate();
         }
      };
      this.avatarProgressView.setSize(AndroidUtilities.dp(30.0F));
      this.avatarProgressView.setProgressColor(-1);
      var6 = this.editTextContainer;
      RadialProgressView var20 = this.avatarProgressView;
      if (LocaleController.isRTL) {
         var8 = 5;
      } else {
         var8 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var10 = 16.0F;
      } else {
         var10 = 0.0F;
      }

      var6.addView(var20, LayoutHelper.createFrame(64, 64.0F, var8 | 48, var9, 16.0F, var10, 16.0F));
      this.showAvatarProgress(false, false);
      this.editText = new EditTextEmoji(var1, var3, this, 0);
      EditTextEmoji var29 = this.editText;
      int var34 = this.chatType;
      String var21;
      if (var34 != 0 && var34 != 4) {
         var34 = 2131559370;
         var21 = "EnterListName";
      } else {
         var34 = 2131559369;
         var21 = "EnterGroupNamePlaceholder";
      }

      var29.setHint(LocaleController.getString(var21, var34));
      var21 = this.nameToSet;
      if (var21 != null) {
         this.editText.setText(var21);
         this.nameToSet = null;
      }

      LengthFilter var24 = new LengthFilter(100);
      this.editText.setFilters(new InputFilter[]{var24});
      var19 = this.editTextContainer;
      var29 = this.editText;
      if (LocaleController.isRTL) {
         var9 = 5.0F;
      } else {
         var9 = 96.0F;
      }

      if (LocaleController.isRTL) {
         var10 = 96.0F;
      } else {
         var10 = 5.0F;
      }

      var19.addView(var29, LayoutHelper.createFrame(-1, -2.0F, 16, var9, 0.0F, var10, 0.0F));
      LinearLayoutManager var25 = new LinearLayoutManager(var1, 1, false);
      this.listView = new RecyclerListView(var1);
      RecyclerView var31 = this.listView;
      GroupCreateFinalActivity.GroupCreateAdapter var11 = new GroupCreateFinalActivity.GroupCreateAdapter(var1);
      this.adapter = var11;
      var31.setAdapter(var11);
      this.listView.setLayoutManager(var25);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerView var27 = this.listView;
      if (LocaleController.isRTL) {
         var8 = 1;
      } else {
         var8 = 2;
      }

      var27.setVerticalScrollbarPosition(var8);
      GroupCreateDividerItemDecoration var28 = new GroupCreateDividerItemDecoration();
      var28.setSkipRows(2);
      this.listView.addItemDecoration(var28);
      var4.addView(this.listView, LayoutHelper.createLinear(-1, -1));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               AndroidUtilities.hideKeyboard(GroupCreateFinalActivity.this.editText);
            }

         }
      });
      this.floatingButtonContainer = new FrameLayout(var1);
      float var12 = 56.0F;
      Object var30 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
      if (VERSION.SDK_INT < 21) {
         Drawable var22 = var1.getResources().getDrawable(2131165387).mutate();
         var22.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var30 = new CombinedDrawable(var22, (Drawable)var30, 0, 0);
         ((CombinedDrawable)var30).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.floatingButtonContainer.setBackgroundDrawable((Drawable)var30);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var32 = new StateListAnimator();
         ObjectAnimator var23 = ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var32.addState(new int[]{16842919}, var23);
         var23 = ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var32.addState(new int[0], var23);
         this.floatingButtonContainer.setStateListAnimator(var32);
         this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      var19 = this.floatingButtonContainer;
      if (VERSION.SDK_INT >= 21) {
         var8 = 56;
      } else {
         var8 = 60;
      }

      if (VERSION.SDK_INT >= 21) {
         var9 = 56.0F;
      } else {
         var9 = 60.0F;
      }

      if (!LocaleController.isRTL) {
         var7 = 5;
      }

      if (LocaleController.isRTL) {
         var10 = 14.0F;
      } else {
         var10 = 0.0F;
      }

      float var13;
      if (LocaleController.isRTL) {
         var13 = 0.0F;
      } else {
         var13 = 14.0F;
      }

      var3.addView(var19, LayoutHelper.createFrame(var8, var9, var7 | 80, var10, 0.0F, var13, 14.0F));
      this.floatingButtonContainer.setOnClickListener(new _$$Lambda$GroupCreateFinalActivity$75__1xxdeXhVklExcewj9GeLGnQ(this));
      this.floatingButtonIcon = new ImageView(var1);
      this.floatingButtonIcon.setScaleType(ScaleType.CENTER);
      this.floatingButtonIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
      this.floatingButtonIcon.setImageResource(2131165355);
      this.floatingButtonIcon.setPadding(0, AndroidUtilities.dp(2.0F), 0, 0);
      this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", 2131559299));
      FrameLayout var18 = this.floatingButtonContainer;
      ImageView var33 = this.floatingButtonIcon;
      if (VERSION.SDK_INT >= 21) {
         var8 = 56;
      } else {
         var8 = 60;
      }

      if (VERSION.SDK_INT >= 21) {
         var9 = var12;
      } else {
         var9 = 60.0F;
      }

      var18.addView(var33, LayoutHelper.createFrame(var8, var9));
      this.progressView = new ContextProgressView(var1, 1);
      this.progressView.setAlpha(0.0F);
      this.progressView.setScaleX(0.1F);
      this.progressView.setScaleY(0.1F);
      this.progressView.setVisibility(4);
      this.floatingButtonContainer.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.updateInterfaces;
      byte var6 = 0;
      if (var1 == var4) {
         if (this.listView == null) {
            return;
         }

         var4 = (Integer)var3[0];
         if ((var4 & 2) != 0 || (var4 & 1) != 0 || (var4 & 4) != 0) {
            int var5 = this.listView.getChildCount();

            for(var1 = var6; var1 < var5; ++var1) {
               View var7 = this.listView.getChildAt(var1);
               if (var7 instanceof GroupCreateUserCell) {
                  ((GroupCreateUserCell)var7).update(var4);
               }
            }
         }
      } else {
         GroupCreateFinalActivity.GroupCreateFinalActivityDelegate var9;
         if (var1 == NotificationCenter.chatDidFailCreate) {
            this.reqId = 0;
            this.donePressed = false;
            this.showEditDoneProgress(false);
            EditTextEmoji var8 = this.editText;
            if (var8 != null) {
               var8.setEnabled(true);
            }

            var9 = this.delegate;
            if (var9 != null) {
               var9.didFailChatCreation();
            }
         } else if (var1 == NotificationCenter.chatDidCreated) {
            this.reqId = 0;
            var1 = (Integer)var3[0];
            var9 = this.delegate;
            if (var9 != null) {
               var9.didFinishChatCreation(this, var1);
            } else {
               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
               Bundle var10 = new Bundle();
               var10.putInt("chat_id", var1);
               this.presentFragment(new ChatActivity(var10), true);
            }

            if (this.uploadedAvatar != null) {
               MessagesController.getInstance(super.currentAccount).changeChatAvatar(var1, this.uploadedAvatar, this.avatar, this.avatarBig);
            }
         }
      }

   }

   public void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateFinalActivity$NmRkjRZ6_MrsxxvtnDOrMWsjyWE(this, var1, var3, var2));
   }

   public String getInitialSearchString() {
      return this.editText.getText().toString();
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8 var1 = new _$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8(this);
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollActive");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollInactive");
      ThemeDescription var11 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollText");
      RecyclerView var12 = this.listView;
      Paint var13 = Theme.dividerPaint;
      ThemeDescription var14 = new ThemeDescription(var12, 0, new Class[]{View.class}, var13, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var28 = new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var27 = new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_hintText");
      ThemeDescription var15 = new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_cursor");
      ThemeDescription var16 = new ThemeDescription(this.editText, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField");
      ThemeDescription var17 = new ThemeDescription(this.editText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated");
      ThemeDescription var18 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var19 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var20 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var21 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionText");
      ThemeDescription var22 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText");
      ThemeDescription var23 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText");
      RecyclerView var24 = this.listView;
      Drawable var25 = Theme.avatar_broadcastDrawable;
      Drawable var26 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14, var28, var27, var15, var16, var17, var18, var19, var20, var21, var22, var23, new ThemeDescription(var24, 0, new Class[]{GroupCreateUserCell.class}, (Paint)null, new Drawable[]{var25, var26}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2"), new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText")};
   }

   // $FF: synthetic method
   public void lambda$createView$3$GroupCreateFinalActivity(View var1) {
      ImageUpdater var3 = this.imageUpdater;
      boolean var2;
      if (this.avatar != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.openMenu(var2, new _$$Lambda$GroupCreateFinalActivity$oecJUKdxYSEn20J67Ur1QheSsBc(this));
   }

   // $FF: synthetic method
   public void lambda$createView$4$GroupCreateFinalActivity(View var1) {
      if (!this.donePressed) {
         if (this.editText.length() == 0) {
            Vibrator var2 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
            if (var2 != null) {
               var2.vibrate(200L);
            }

            AndroidUtilities.shakeView(this.editText, 2.0F, 0);
         } else {
            this.donePressed = true;
            AndroidUtilities.hideKeyboard(this.editText);
            this.editText.setEnabled(false);
            if (this.imageUpdater.uploadingImage != null) {
               this.createAfterUpload = true;
            } else {
               this.showEditDoneProgress(true);
               this.reqId = MessagesController.getInstance(super.currentAccount).createChat(this.editText.getText().toString(), this.selectedContacts, (String)null, this.chatType, this);
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$didUploadPhoto$5$GroupCreateFinalActivity(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      if (var1 != null) {
         this.uploadedAvatar = var1;
         if (this.createAfterUpload) {
            GroupCreateFinalActivity.GroupCreateFinalActivityDelegate var4 = this.delegate;
            if (var4 != null) {
               var4.didStartChatCreation();
            }

            MessagesController.getInstance(super.currentAccount).createChat(this.editText.getText().toString(), this.selectedContacts, (String)null, this.chatType, this);
         }

         this.showAvatarProgress(false, true);
         this.avatarEditor.setImageDrawable((Drawable)null);
      } else {
         this.avatar = var2.location;
         this.avatarBig = var3.location;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForLocal(this.avatar), "50_50", (Drawable)this.avatarDrawable, (Object)null);
         this.showAvatarProgress(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$6$GroupCreateFinalActivity() {
      RecyclerView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof GroupCreateUserCell) {
               ((GroupCreateUserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$GroupCreateFinalActivity() {
      this.avatar = null;
      this.avatarBig = null;
      this.uploadedAvatar = null;
      this.showAvatarProgress(false, true);
      this.avatarImage.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)null);
      this.avatarEditor.setImageResource(2131165276);
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$0$GroupCreateFinalActivity(ArrayList var1, ArrayList var2, CountDownLatch var3) {
      var1.addAll(MessagesStorage.getInstance(super.currentAccount).getUsers(var2));
      var3.countDown();
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      this.imageUpdater.onActivityResult(var1, var2, var3);
   }

   public boolean onBackPressed() {
      EditTextEmoji var1 = this.editText;
      if (var1 != null && var1.isPopupShowing()) {
         this.editText.hidePopup(true);
         return false;
      } else {
         return true;
      }
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidFailCreate);
      this.imageUpdater = new ImageUpdater();
      ImageUpdater var1 = this.imageUpdater;
      var1.parentFragment = this;
      var1.delegate = this;
      this.selectedContacts = this.getArguments().getIntegerArrayList("result");
      ArrayList var6 = new ArrayList();

      for(int var2 = 0; var2 < this.selectedContacts.size(); ++var2) {
         Integer var3 = (Integer)this.selectedContacts.get(var2);
         if (MessagesController.getInstance(super.currentAccount).getUser(var3) == null) {
            var6.add(var3);
         }
      }

      if (!var6.isEmpty()) {
         CountDownLatch var4 = new CountDownLatch(1);
         ArrayList var8 = new ArrayList();
         MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$GroupCreateFinalActivity$eKyq7cJMgDwCwGYOq_2k3JE_l_A(this, var8, var6, var4));

         try {
            var4.await();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         if (var6.size() != var8.size()) {
            return false;
         }

         if (var8.isEmpty()) {
            return false;
         }

         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            TLRPC.User var7 = (TLRPC.User)var9.next();
            MessagesController.getInstance(super.currentAccount).putUser(var7, true);
         }
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidFailCreate);
      this.imageUpdater.clear();
      if (this.reqId != 0) {
         ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
      }

      EditTextEmoji var1 = this.editText;
      if (var1 != null) {
         var1.onDestroy();
      }

      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onPause() {
      super.onPause();
      EditTextEmoji var1 = this.editText;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public void onResume() {
      super.onResume();
      EditTextEmoji var1 = this.editText;
      if (var1 != null) {
         var1.onResume();
      }

      GroupCreateFinalActivity.GroupCreateAdapter var2 = this.adapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         this.editText.openKeyboard();
      }

   }

   public void restoreSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      if (var2 != null) {
         var2.currentPicturePath = var1.getString("path");
      }

      String var3 = var1.getString("nameTextView");
      if (var3 != null) {
         EditTextEmoji var4 = this.editText;
         if (var4 != null) {
            var4.setText(var3);
         } else {
            this.nameToSet = var3;
         }
      }

   }

   public void saveSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      String var3;
      if (var2 != null) {
         var3 = var2.currentPicturePath;
         if (var3 != null) {
            var1.putString("path", var3);
         }
      }

      EditTextEmoji var4 = this.editText;
      if (var4 != null) {
         var3 = var4.getText().toString();
         if (var3 != null && var3.length() != 0) {
            var1.putString("nameTextView", var3);
         }
      }

   }

   public void setDelegate(GroupCreateFinalActivity.GroupCreateFinalActivityDelegate var1) {
      this.delegate = var1;
   }

   public class GroupCreateAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;

      public GroupCreateAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         return GroupCreateFinalActivity.this.selectedContacts.size() + 2;
      }

      public int getItemViewType(int var1) {
         if (var1 != 0) {
            return var1 != 1 ? 2 : 1;
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 1) {
            if (var3 == 2) {
               ((GroupCreateUserCell)var1.itemView).setObject(MessagesController.getInstance(GroupCreateFinalActivity.access$1500(GroupCreateFinalActivity.this)).getUser((Integer)GroupCreateFinalActivity.this.selectedContacts.get(var2 - 2)), (CharSequence)null, (CharSequence)null);
            }
         } else {
            ((HeaderCell)var1.itemView).setText(LocaleController.formatPluralString("Members", GroupCreateFinalActivity.this.selectedContacts.size()));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               var4 = new GroupCreateUserCell(this.context, false, 3);
            } else {
               var4 = new HeaderCell(this.context);
               ((HeaderCell)var4).setHeight(46);
            }
         } else {
            var4 = new ShadowSectionCell(this.context);
            Drawable var3 = Theme.getThemedDrawable(this.context, 2131165396, "windowBackgroundGrayShadow");
            CombinedDrawable var5 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var3);
            var5.setFullsize(true);
            ((View)var4).setBackgroundDrawable(var5);
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         if (var1.getItemViewType() == 2) {
            ((GroupCreateUserCell)var1.itemView).recycle();
         }

      }
   }

   public interface GroupCreateFinalActivityDelegate {
      void didFailChatCreation();

      void didFinishChatCreation(GroupCreateFinalActivity var1, int var2);

      void didStartChatCreation();
   }
}
