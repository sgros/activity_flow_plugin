package org.telegram.ui;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.VoIPHelper;

public class CallLogActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int TYPE_IN = 1;
   private static final int TYPE_MISSED = 2;
   private static final int TYPE_OUT = 0;
   private OnClickListener callBtnClickListener = new OnClickListener() {
      public void onClick(View var1) {
         CallLogActivity.CallLogRow var2 = (CallLogActivity.CallLogRow)var1.getTag();
         CallLogActivity var3 = CallLogActivity.this;
         TLRPC.User var4 = var2.user;
         var3.lastCallUser = var4;
         VoIPHelper.startCall(var4, CallLogActivity.this.getParentActivity(), (TLRPC.UserFull)null);
      }
   };
   private ArrayList calls = new ArrayList();
   private EmptyTextProgressView emptyView;
   private boolean endReached;
   private boolean firstLoaded;
   private ImageView floatingButton;
   private boolean floatingHidden;
   private final AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
   private Drawable greenDrawable;
   private Drawable greenDrawable2;
   private ImageSpan iconIn;
   private ImageSpan iconMissed;
   private ImageSpan iconOut;
   private TLRPC.User lastCallUser;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private CallLogActivity.ListAdapter listViewAdapter;
   private boolean loading;
   private int prevPosition;
   private int prevTop;
   private Drawable redDrawable;
   private boolean scrollUpdated;

   private void confirmAndDelete(CallLogActivity.CallLogRow var1) {
      if (this.getParentActivity() != null) {
         (new AlertDialog.Builder(this.getParentActivity())).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("ConfirmDeleteCallLog", 2131559135)).setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$CallLogActivity$H8_jHw_nBqLvayxPjMSmdGRTSFk(this, var1)).setNegativeButton(LocaleController.getString("Cancel", 2131558891), (android.content.DialogInterface.OnClickListener)null).show().setCanceledOnTouchOutside(true);
      }
   }

   private void getCalls(int var1, int var2) {
      if (!this.loading) {
         this.loading = true;
         EmptyTextProgressView var3 = this.emptyView;
         if (var3 != null && !this.firstLoaded) {
            var3.showProgress();
         }

         CallLogActivity.ListAdapter var4 = this.listViewAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }

         TLRPC.TL_messages_search var5 = new TLRPC.TL_messages_search();
         var5.limit = var2;
         var5.peer = new TLRPC.TL_inputPeerEmpty();
         var5.filter = new TLRPC.TL_inputMessagesFilterPhoneCalls();
         var5.q = "";
         var5.offset_id = var1;
         var1 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI(this), 2);
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var1, super.classGuid);
      }
   }

   private void hideFloatingButton(boolean var1) {
      if (this.floatingHidden != var1) {
         this.floatingHidden = var1;
         ImageView var2 = this.floatingButton;
         float var3;
         if (this.floatingHidden) {
            var3 = (float)AndroidUtilities.dp(100.0F);
         } else {
            var3 = 0.0F;
         }

         ObjectAnimator var4 = ObjectAnimator.ofFloat(var2, "translationY", new float[]{var3}).setDuration(300L);
         var4.setInterpolator(this.floatingInterpolator);
         this.floatingButton.setClickable(var1 ^ true);
         var4.start();
      }
   }

   public View createView(Context var1) {
      this.greenDrawable = this.getParentActivity().getResources().getDrawable(2131165432).mutate();
      Drawable var2 = this.greenDrawable;
      var2.setBounds(0, 0, var2.getIntrinsicWidth(), this.greenDrawable.getIntrinsicHeight());
      this.greenDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("calls_callReceivedGreenIcon"), Mode.MULTIPLY));
      this.iconOut = new ImageSpan(this.greenDrawable, 0);
      this.greenDrawable2 = this.getParentActivity().getResources().getDrawable(2131165435).mutate();
      var2 = this.greenDrawable2;
      var2.setBounds(0, 0, var2.getIntrinsicWidth(), this.greenDrawable2.getIntrinsicHeight());
      this.greenDrawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("calls_callReceivedGreenIcon"), Mode.MULTIPLY));
      this.iconIn = new ImageSpan(this.greenDrawable2, 0);
      this.redDrawable = this.getParentActivity().getResources().getDrawable(2131165435).mutate();
      var2 = this.redDrawable;
      var2.setBounds(0, 0, var2.getIntrinsicWidth(), this.redDrawable.getIntrinsicHeight());
      this.redDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("calls_callReceivedRedIcon"), Mode.MULTIPLY));
      this.iconMissed = new ImageSpan(this.redDrawable, 0);
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("Calls", 2131558888));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               CallLogActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var3 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setText(LocaleController.getString("NoCallLog", 2131559917));
      var3.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var13 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var13;
      var4.setLayoutManager(var13);
      RecyclerListView var14 = this.listView;
      CallLogActivity.ListAdapter var16 = new CallLogActivity.ListAdapter(var1);
      this.listViewAdapter = var16;
      var14.setAdapter(var16);
      var14 = this.listView;
      byte var5;
      if (LocaleController.isRTL) {
         var5 = 1;
      } else {
         var5 = 2;
      }

      var14.setVerticalScrollbarPosition(var5);
      var3.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$CallLogActivity$6UJZSG3aF6E0mUq9h4dUUmBm5H0(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$CallLogActivity$gtRDuyh9OXWvOBCrfMrhHgkj_ds(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         // $FF: synthetic method
         public void lambda$onScrolled$0$CallLogActivity$3(CallLogActivity.CallLogRow var1) {
            CallLogActivity var2 = CallLogActivity.this;
            List var3 = var1.calls;
            var2.getCalls(((TLRPC.Message)var3.get(var3.size() - 1)).id, 100);
         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            int var4 = CallLogActivity.this.layoutManager.findFirstVisibleItemPosition();
            boolean var9 = false;
            if (var4 == -1) {
               var2 = 0;
            } else {
               var2 = Math.abs(CallLogActivity.this.layoutManager.findLastVisibleItemPosition() - var4) + 1;
            }

            int var5;
            if (var2 > 0) {
               var5 = CallLogActivity.this.listViewAdapter.getItemCount();
               if (!CallLogActivity.this.endReached && !CallLogActivity.this.loading && !CallLogActivity.this.calls.isEmpty() && var2 + var4 >= var5 - 5) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$CallLogActivity$3$1DV60DlgjFkI_3XLU_3k_ebYIpc(this, (CallLogActivity.CallLogRow)CallLogActivity.this.calls.get(CallLogActivity.this.calls.size() - 1)));
               }
            }

            if (CallLogActivity.this.floatingButton.getVisibility() != 8) {
               View var8 = var1.getChildAt(0);
               if (var8 != null) {
                  var2 = var8.getTop();
               } else {
                  var2 = 0;
               }

               boolean var7;
               label49: {
                  boolean var6;
                  if (CallLogActivity.this.prevPosition == var4) {
                     var5 = CallLogActivity.this.prevTop;
                     if (var2 < CallLogActivity.this.prevTop) {
                        var6 = true;
                     } else {
                        var6 = false;
                     }

                     var7 = var6;
                     if (Math.abs(var5 - var2) <= 1) {
                        break label49;
                     }
                  } else if (var4 > CallLogActivity.this.prevPosition) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var9 = true;
                  var7 = var6;
               }

               if (var9 && CallLogActivity.this.scrollUpdated) {
                  CallLogActivity.this.hideFloatingButton(var7);
               }

               CallLogActivity.this.prevPosition = var4;
               CallLogActivity.this.prevTop = var2;
               CallLogActivity.this.scrollUpdated = true;
            }

         }
      });
      if (this.loading) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      this.floatingButton = new ImageView(var1);
      this.floatingButton.setVisibility(0);
      this.floatingButton.setScaleType(ScaleType.CENTER);
      Drawable var17 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
      Object var15 = var17;
      if (VERSION.SDK_INT < 21) {
         Drawable var10 = var1.getResources().getDrawable(2131165387).mutate();
         var10.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var15 = new CombinedDrawable(var10, var17, 0, 0);
         ((CombinedDrawable)var15).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.floatingButton.setBackgroundDrawable((Drawable)var15);
      this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
      this.floatingButton.setImageResource(2131165429);
      this.floatingButton.setContentDescription(LocaleController.getString("Call", 2131558869));
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var11 = new StateListAnimator();
         ObjectAnimator var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var11.addState(new int[]{16842919}, var18);
         var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var11.addState(new int[0], var18);
         this.floatingButton.setStateListAnimator(var11);
         this.floatingButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      ImageView var12 = this.floatingButton;
      if (VERSION.SDK_INT >= 21) {
         var5 = 56;
      } else {
         var5 = 60;
      }

      float var6;
      if (VERSION.SDK_INT >= 21) {
         var6 = 56.0F;
      } else {
         var6 = 60.0F;
      }

      byte var7;
      if (LocaleController.isRTL) {
         var7 = 3;
      } else {
         var7 = 5;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 14.0F;
      } else {
         var8 = 0.0F;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 14.0F;
      }

      var3.addView(var12, LayoutHelper.createFrame(var5, var6, var7 | 80, var8, 0.0F, var9, 14.0F));
      this.floatingButton.setOnClickListener(new _$$Lambda$CallLogActivity$7Dw3WWwu34MwRrf1oRbnVFPZGBY(this));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.didReceiveNewMessages;
      boolean var10 = false;
      Iterator var5;
      if (var1 == var4 && this.firstLoaded) {
         var5 = ((ArrayList)var3[1]).iterator();

         while(true) {
            while(true) {
               MessageObject var13;
               TLRPC.Message var15;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  var13 = (MessageObject)var5.next();
                  var15 = var13.messageOwner;
               } while(!(var15.action instanceof TLRPC.TL_messageActionPhoneCall));

               if (var15.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                  var2 = var13.messageOwner.to_id.user_id;
               } else {
                  var2 = var13.messageOwner.from_id;
               }

               byte var9;
               if (var13.messageOwner.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                  var9 = 0;
               } else {
                  var9 = 1;
               }

               TLRPC.PhoneCallDiscardReason var16 = var13.messageOwner.action.reason;
               byte var14 = var9;
               if (var9 == 1) {
                  label92: {
                     if (!(var16 instanceof TLRPC.TL_phoneCallDiscardReasonMissed)) {
                        var14 = var9;
                        if (!(var16 instanceof TLRPC.TL_phoneCallDiscardReasonBusy)) {
                           break label92;
                        }
                     }

                     var14 = 2;
                  }
               }

               CallLogActivity.CallLogRow var17;
               if (this.calls.size() > 0) {
                  var17 = (CallLogActivity.CallLogRow)this.calls.get(0);
                  if (var17.user.id == var2 && var17.type == var14) {
                     var17.calls.add(0, var13.messageOwner);
                     this.listViewAdapter.notifyItemChanged(0);
                     continue;
                  }
               }

               var17 = new CallLogActivity.CallLogRow();
               var17.calls = new ArrayList();
               var17.calls.add(var13.messageOwner);
               var17.user = MessagesController.getInstance(super.currentAccount).getUser(var2);
               var17.type = var14;
               this.calls.add(0, var17);
               this.listViewAdapter.notifyItemInserted(0);
            }
         }
      } else if (var1 == NotificationCenter.messagesDeleted && this.firstLoaded) {
         ArrayList var11 = (ArrayList)var3[0];
         Iterator var6 = this.calls.iterator();

         while(var6.hasNext()) {
            CallLogActivity.CallLogRow var7 = (CallLogActivity.CallLogRow)var6.next();
            var5 = var7.calls.iterator();
            boolean var8 = var10;

            while(var5.hasNext()) {
               if (var11.contains(((TLRPC.Message)var5.next()).id)) {
                  var5.remove();
                  var8 = true;
               }
            }

            var10 = var8;
            if (var7.calls.size() == 0) {
               var6.remove();
               var10 = var8;
            }
         }

         if (var10) {
            CallLogActivity.ListAdapter var12 = this.listViewAdapter;
            if (var12 != null) {
               var12.notifyDataSetChanged();
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs var1 = new _$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LocationCell.class, CallLogActivity.CustomCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var12 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var38 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var13 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var39 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var16 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionIcon");
      ThemeDescription var17 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground");
      ThemeDescription var18 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionPressedBackground");
      RecyclerListView var19 = this.listView;
      Drawable var20 = Theme.dialogs_verifiedCheckDrawable;
      ThemeDescription var42 = new ThemeDescription(var19, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var20}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedCheck");
      RecyclerListView var21 = this.listView;
      Drawable var40 = Theme.dialogs_verifiedDrawable;
      ThemeDescription var41 = new ThemeDescription(var21, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var40}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedBackground");
      RecyclerListView var22 = this.listView;
      TextPaint var43 = Theme.dialogs_offlinePaint;
      ThemeDescription var44 = new ThemeDescription(var22, 0, new Class[]{ProfileSearchCell.class}, var43, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3");
      RecyclerListView var23 = this.listView;
      TextPaint var45 = Theme.dialogs_onlinePaint;
      ThemeDescription var46 = new ThemeDescription(var23, 0, new Class[]{ProfileSearchCell.class}, var45, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText3");
      RecyclerListView var24 = this.listView;
      TextPaint var47 = Theme.dialogs_namePaint;
      TextPaint var25 = Theme.dialogs_searchNamePaint;
      ThemeDescription var48 = new ThemeDescription(var24, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var47, var25}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_name");
      RecyclerListView var26 = this.listView;
      TextPaint var49 = Theme.dialogs_nameEncryptedPaint;
      var25 = Theme.dialogs_searchNameEncryptedPaint;
      ThemeDescription var50 = new ThemeDescription(var26, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var49, var25}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_secretName");
      var26 = this.listView;
      Drawable var27 = Theme.avatar_broadcastDrawable;
      Drawable var51 = Theme.avatar_savedDrawable;
      ThemeDescription var28 = new ThemeDescription(var26, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var27, var51}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text");
      ThemeDescription var54 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed");
      ThemeDescription var29 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange");
      ThemeDescription var30 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet");
      ThemeDescription var52 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen");
      ThemeDescription var31 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan");
      ThemeDescription var53 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue");
      ThemeDescription var37 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink");
      RecyclerListView var32 = this.listView;
      Drawable var33 = this.greenDrawable;
      Drawable var34 = this.greenDrawable2;
      Drawable var35 = Theme.calllog_msgCallUpRedDrawable;
      Drawable var36 = Theme.calllog_msgCallDownRedDrawable;
      ThemeDescription var55 = new ThemeDescription(var32, 0, new Class[]{View.class}, (Paint)null, new Drawable[]{var33, var34, var35, var36}, (ThemeDescription.ThemeDescriptionDelegate)null, "calls_callReceivedGreenIcon");
      RecyclerListView var56 = this.listView;
      var35 = this.redDrawable;
      var33 = Theme.calllog_msgCallUpGreenDrawable;
      var34 = Theme.calllog_msgCallDownGreenDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var12, var38, var13, var14, var15, var39, var16, var17, var18, var42, var41, var44, var46, var48, var50, var28, var54, var29, var30, var52, var31, var53, var37, var55, new ThemeDescription(var56, 0, new Class[]{View.class}, (Paint)null, new Drawable[]{var35, var33, var34}, (ThemeDescription.ThemeDescriptionDelegate)null, "calls_callReceivedRedIcon")};
   }

   // $FF: synthetic method
   public void lambda$confirmAndDelete$7$CallLogActivity(CallLogActivity.CallLogRow var1, DialogInterface var2, int var3) {
      ArrayList var5 = new ArrayList();
      Iterator var4 = var1.calls.iterator();

      while(var4.hasNext()) {
         var5.add(((TLRPC.Message)var4.next()).id);
      }

      MessagesController.getInstance(super.currentAccount).deleteMessages(var5, (ArrayList)null, (TLRPC.EncryptedChat)null, 0, false);
   }

   // $FF: synthetic method
   public void lambda$createView$0$CallLogActivity(View var1, int var2) {
      if (var2 >= 0 && var2 < this.calls.size()) {
         CallLogActivity.CallLogRow var3 = (CallLogActivity.CallLogRow)this.calls.get(var2);
         Bundle var4 = new Bundle();
         var4.putInt("user_id", var3.user.id);
         var4.putInt("message_id", ((TLRPC.Message)var3.calls.get(0)).id);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
         this.presentFragment(new ChatActivity(var4), true);
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$2$CallLogActivity(View var1, int var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (var2 >= 0) {
         if (var2 >= this.calls.size()) {
            var4 = var3;
         } else {
            CallLogActivity.CallLogRow var6 = (CallLogActivity.CallLogRow)this.calls.get(var2);
            ArrayList var5 = new ArrayList();
            var5.add(LocaleController.getString("Delete", 2131559227));
            if (VoIPHelper.canRateCall((TLRPC.TL_messageActionPhoneCall)((TLRPC.Message)var6.calls.get(0)).action)) {
               var5.add(LocaleController.getString("CallMessageReportProblem", 2131558878));
            }

            (new AlertDialog.Builder(this.getParentActivity())).setTitle(LocaleController.getString("Calls", 2131558888)).setItems((CharSequence[])var5.toArray(new String[0]), new _$$Lambda$CallLogActivity$attFsRotfjaCaQu5P_RAm6Vh5uM(this, var6)).show();
            var4 = true;
         }
      }

      return var4;
   }

   // $FF: synthetic method
   public void lambda$createView$4$CallLogActivity(View var1) {
      Bundle var2 = new Bundle();
      var2.putBoolean("destroyAfterSelect", true);
      var2.putBoolean("returnAsResult", true);
      var2.putBoolean("onlyUsers", true);
      ContactsActivity var3 = new ContactsActivity(var2);
      var3.setDelegate(new _$$Lambda$CallLogActivity$glOYCOInhnTGmJL8rtqySIZYCwY(this));
      this.presentFragment(var3);
   }

   // $FF: synthetic method
   public void lambda$getCalls$6$CallLogActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$CallLogActivity$UPlaIC1CV1ss5ZMXGEys0nI31eY(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$8$CallLogActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof ProfileSearchCell) {
               ((ProfileSearchCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$CallLogActivity(CallLogActivity.CallLogRow var1, DialogInterface var2, int var3) {
      if (var3 != 0) {
         if (var3 == 1) {
            VoIPHelper.showRateAlert(this.getParentActivity(), (TLRPC.TL_messageActionPhoneCall)((TLRPC.Message)var1.calls.get(0)).action);
         }
      } else {
         this.confirmAndDelete(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$CallLogActivity(TLRPC.User var1, String var2, ContactsActivity var3) {
      VoIPHelper.startCall(var1, this.getParentActivity(), (TLRPC.UserFull)null);
   }

   // $FF: synthetic method
   public void lambda$null$5$CallLogActivity(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         SparseArray var3 = new SparseArray();
         TLRPC.messages_Messages var4 = (TLRPC.messages_Messages)var2;
         this.endReached = var4.messages.isEmpty();

         int var5;
         for(var5 = 0; var5 < var4.users.size(); ++var5) {
            TLRPC.User var10 = (TLRPC.User)var4.users.get(var5);
            var3.put(var10.id, var10);
         }

         CallLogActivity.CallLogRow var12;
         if (this.calls.size() > 0) {
            ArrayList var11 = this.calls;
            var12 = (CallLogActivity.CallLogRow)var11.get(var11.size() - 1);
         } else {
            var12 = null;
         }

         CallLogActivity.CallLogRow var13;
         for(int var6 = 0; var6 < var4.messages.size(); var12 = var13) {
            TLRPC.Message var7 = (TLRPC.Message)var4.messages.get(var6);
            TLRPC.MessageAction var8 = var7.action;
            var13 = var12;
            if (var8 != null) {
               if (var8 instanceof TLRPC.TL_messageActionHistoryClear) {
                  var13 = var12;
               } else {
                  byte var17;
                  if (var7.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                     var17 = 0;
                  } else {
                     var17 = 1;
                  }

                  TLRPC.PhoneCallDiscardReason var14 = var7.action.reason;
                  byte var9 = var17;
                  if (var17 == 1) {
                     label74: {
                        if (!(var14 instanceof TLRPC.TL_phoneCallDiscardReasonMissed)) {
                           var9 = var17;
                           if (!(var14 instanceof TLRPC.TL_phoneCallDiscardReasonBusy)) {
                              break label74;
                           }
                        }

                        var9 = 2;
                     }
                  }

                  if (var7.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                     var5 = var7.to_id.user_id;
                  } else {
                     var5 = var7.from_id;
                  }

                  label96: {
                     if (var12 != null && var12.user.id == var5) {
                        var13 = var12;
                        if (var12.type == var9) {
                           break label96;
                        }
                     }

                     if (var12 != null && !this.calls.contains(var12)) {
                        this.calls.add(var12);
                     }

                     var13 = new CallLogActivity.CallLogRow();
                     var13.calls = new ArrayList();
                     var13.user = (TLRPC.User)var3.get(var5);
                     var13.type = var9;
                  }

                  var13.calls.add(var7);
               }
            }

            ++var6;
         }

         if (var12 != null && var12.calls.size() > 0 && !this.calls.contains(var12)) {
            this.calls.add(var12);
         }
      } else {
         this.endReached = true;
      }

      this.loading = false;
      this.firstLoaded = true;
      EmptyTextProgressView var15 = this.emptyView;
      if (var15 != null) {
         var15.showTextView();
      }

      CallLogActivity.ListAdapter var16 = this.listViewAdapter;
      if (var16 != null) {
         var16.notifyDataSetChanged();
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.getCalls(0, 50);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 101) {
         if (var3.length > 0 && var3[0] == 0) {
            VoIPHelper.startCall(this.lastCallUser, this.getParentActivity(), (TLRPC.UserFull)null);
         } else {
            VoIPHelper.permissionDenied(this.getParentActivity(), (Runnable)null);
         }
      }

   }

   public void onResume() {
      super.onResume();
      CallLogActivity.ListAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class CallLogRow {
      public List calls;
      public int type;
      public TLRPC.User user;

      private CallLogRow() {
      }

      // $FF: synthetic method
      CallLogRow(Object var2) {
         this();
      }
   }

   private class CustomCell extends FrameLayout {
      public CustomCell(Context var2) {
         super(var2);
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         int var1 = CallLogActivity.this.calls.size();
         int var2 = var1;
         if (!CallLogActivity.this.calls.isEmpty()) {
            var2 = var1;
            if (!CallLogActivity.this.endReached) {
               var2 = var1 + 1;
            }
         }

         return var2;
      }

      public int getItemViewType(int var1) {
         if (var1 < CallLogActivity.this.calls.size()) {
            return 0;
         } else {
            return !CallLogActivity.this.endReached && var1 == CallLogActivity.this.calls.size() ? 1 : 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getAdapterPosition() != CallLogActivity.this.calls.size()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            CallLogActivity.ViewItem var3 = (CallLogActivity.ViewItem)var1.itemView.getTag();
            ProfileSearchCell var4 = var3.cell;
            CallLogActivity.CallLogRow var5 = (CallLogActivity.CallLogRow)CallLogActivity.this.calls.get(var2);
            List var10 = var5.calls;
            boolean var6 = false;
            TLRPC.Message var7 = (TLRPC.Message)var10.get(0);
            String var11;
            if (LocaleController.isRTL) {
               var11 = "\u202b";
            } else {
               var11 = "";
            }

            StringBuilder var8;
            SpannableString var12;
            if (var5.calls.size() == 1) {
               var8 = new StringBuilder();
               var8.append(var11);
               var8.append("  ");
               var8.append(LocaleController.formatDateCallLog((long)var7.date));
               var12 = new SpannableString(var8.toString());
            } else {
               var8 = new StringBuilder();
               var8.append(var11);
               var8.append("  (%d) %s");
               var12 = new SpannableString(String.format(var8.toString(), var5.calls.size(), LocaleController.formatDateCallLog((long)var7.date)));
            }

            int var9 = var5.type;
            if (var9 != 0) {
               if (var9 != 1) {
                  if (var9 == 2) {
                     var12.setSpan(CallLogActivity.this.iconMissed, var11.length(), var11.length() + 1, 0);
                  }
               } else {
                  var12.setSpan(CallLogActivity.this.iconIn, var11.length(), var11.length() + 1, 0);
               }
            } else {
               var12.setSpan(CallLogActivity.this.iconOut, var11.length(), var11.length() + 1, 0);
            }

            var4.setData(var5.user, (TLRPC.EncryptedChat)null, (CharSequence)null, var12, false, false);
            if (var2 != CallLogActivity.this.calls.size() - 1 || !CallLogActivity.this.endReached) {
               var6 = true;
            }

            var4.useSeparator = var6;
            var3.button.setTag(var5);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var7;
         if (var2 != 0) {
            if (var2 != 1) {
               var7 = new TextInfoPrivacyCell(this.mContext);
               ((View)var7).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            } else {
               var7 = new LoadingCell(this.mContext);
               ((View)var7).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var7 = CallLogActivity.this.new CustomCell(this.mContext);
            ((FrameLayout)var7).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ProfileSearchCell var3 = new ProfileSearchCell(this.mContext);
            if (LocaleController.isRTL) {
               var2 = AndroidUtilities.dp(32.0F);
            } else {
               var2 = 0;
            }

            int var4;
            if (LocaleController.isRTL) {
               var4 = 0;
            } else {
               var4 = AndroidUtilities.dp(32.0F);
            }

            var3.setPadding(var2, 0, var4, 0);
            float var5;
            if (LocaleController.isRTL) {
               var5 = 2.0F;
            } else {
               var5 = -2.0F;
            }

            var3.setSublabelOffset(AndroidUtilities.dp(var5), -AndroidUtilities.dp(4.0F));
            ((FrameLayout)var7).addView(var3);
            ImageView var6 = new ImageView(this.mContext);
            var6.setImageResource(2131165791);
            var6.setAlpha(214);
            var6.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
            var6.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
            var6.setScaleType(ScaleType.CENTER);
            var6.setOnClickListener(CallLogActivity.this.callBtnClickListener);
            var6.setContentDescription(LocaleController.getString("Call", 2131558869));
            byte var8;
            if (LocaleController.isRTL) {
               var8 = 3;
            } else {
               var8 = 5;
            }

            ((FrameLayout)var7).addView(var6, LayoutHelper.createFrame(48, 48.0F, var8 | 16, 8.0F, 0.0F, 8.0F, 0.0F));
            ((View)var7).setTag(CallLogActivity.this.new ViewItem(var6, var3));
         }

         return new RecyclerListView.Holder((View)var7);
      }
   }

   private class ViewItem {
      public ImageView button;
      public ProfileSearchCell cell;

      public ViewItem(ImageView var2, ProfileSearchCell var3) {
         this.button = var2;
         this.cell = var3;
      }
   }
}
