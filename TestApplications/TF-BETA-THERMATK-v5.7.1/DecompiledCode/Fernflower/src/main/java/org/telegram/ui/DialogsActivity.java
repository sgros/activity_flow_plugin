package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerMiddle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieDrawable;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.ArchiveHintInnerCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedArrowDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DialogsItemAnimator;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.PacmanAnimation;
import org.telegram.ui.Components.ProxyDrawable;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.UndoView;

public class DialogsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int archive = 105;
   private static final int clear = 103;
   private static final int delete = 102;
   public static boolean[] dialogsLoaded = new boolean[3];
   private static ArrayList frozenDialogsList;
   private static final int mute = 104;
   private static final int pin = 100;
   private static final int read = 101;
   private ArrayList actionModeViews = new ArrayList();
   private String addToGroupAlertString;
   private float additionalFloatingTranslation;
   private boolean allowMoving;
   private boolean allowScrollToHiddenView;
   private boolean allowSwipeDuringCurrentTouch;
   private boolean allowSwitchAccount;
   private ActionBarMenuItem archiveItem;
   private AnimatedArrowDrawable arrowDrawable;
   private boolean askAboutContacts = true;
   private BackDrawable backDrawable;
   private int canClearCacheCount;
   private int canMuteCount;
   private int canPinCount;
   private int canReadCount;
   private int canUnmuteCount;
   private boolean cantSendToChannels;
   private boolean checkCanWrite;
   private boolean checkPermission = true;
   private ActionBarMenuSubItem clearItem;
   private boolean closeSearchFieldOnHide;
   private ChatActivityEnterView commentView;
   private int currentConnectionState;
   private DialogsActivity.DialogsActivityDelegate delegate;
   private ActionBarMenuItem deleteItem;
   private int dialogChangeFinished;
   private int dialogInsertFinished;
   private int dialogRemoveFinished;
   private DialogsAdapter dialogsAdapter;
   private DialogsItemAnimator dialogsItemAnimator;
   private boolean dialogsListFrozen;
   private DialogsSearchAdapter dialogsSearchAdapter;
   private int dialogsType;
   private ImageView floatingButton;
   private FrameLayout floatingButtonContainer;
   private boolean floatingHidden;
   private final AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
   private int folderId;
   private ItemTouchHelper itemTouchhelper;
   private int lastItemsCount;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private MenuDrawable menuDrawable;
   private DialogCell movingView;
   private boolean movingWas;
   private ActionBarMenuSubItem muteItem;
   private boolean onlySelect;
   private long openedDialogId;
   private PacmanAnimation pacmanAnimation;
   private ActionBarMenuItem passcodeItem;
   private AlertDialog permissionDialog;
   private ActionBarMenuItem pinItem;
   private int prevPosition;
   private int prevTop;
   private RadialProgressView progressView;
   private ProxyDrawable proxyDrawable;
   private ActionBarMenuItem proxyItem;
   private boolean proxyItemVisisble;
   private ActionBarMenuSubItem readItem;
   private boolean scrollUpdated;
   private boolean scrollingManually;
   private long searchDialogId;
   private EmptyTextProgressView searchEmptyView;
   private TLObject searchObject;
   private String searchString;
   private boolean searchWas;
   private boolean searching;
   private String selectAlertString;
   private String selectAlertStringGroup;
   private NumberTextView selectedDialogsCountTextView;
   private RecyclerView sideMenu;
   private DialogCell slidingView;
   private boolean startedScrollAtTop;
   private DialogsActivity.SwipeController swipeController;
   private ActionBarMenuItem switchItem;
   private int totalConsumedAmount;
   private UndoView[] undoView = new UndoView[2];
   private boolean waitingForScrollFinished;

   public DialogsActivity(Bundle var1) {
      super(var1);
   }

   // $FF: synthetic method
   static ActionBar access$000(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1300(DialogsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1400(DialogsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$1900(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$200(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$3508(DialogsActivity var0) {
      int var1 = var0.lastItemsCount++;
      return var1;
   }

   // $FF: synthetic method
   static int access$3510(DialogsActivity var0) {
      int var1 = var0.lastItemsCount--;
      return var1;
   }

   // $FF: synthetic method
   static int access$3800(DialogsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$5100(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5700(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5800(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6000(DialogsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6100(DialogsActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static Bundle access$6300(DialogsActivity var0) {
      return var0.arguments;
   }

   // $FF: synthetic method
   static ActionBar access$7200(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$7400(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$7800(DialogsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$800(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$8100(DialogsActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$8400(DialogsActivity var0) {
      return var0.currentAccount;
   }

   @TargetApi(23)
   private void askForPermissons(boolean var1) {
      Activity var2 = this.getParentActivity();
      if (var2 != null) {
         ArrayList var3 = new ArrayList();
         if (this.getUserConfig().syncContacts && this.askAboutContacts && var2.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            if (var1) {
               AlertDialog var5 = AlertsCreator.createContactsPermissionDialog(var2, new _$$Lambda$DialogsActivity$0uSHLkwmlB9mVpQMUrJbrhf__rI(this)).create();
               this.permissionDialog = var5;
               this.showDialog(var5);
               return;
            }

            var3.add("android.permission.READ_CONTACTS");
            var3.add("android.permission.WRITE_CONTACTS");
            var3.add("android.permission.GET_ACCOUNTS");
         }

         if (var2.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            var3.add("android.permission.READ_EXTERNAL_STORAGE");
            var3.add("android.permission.WRITE_EXTERNAL_STORAGE");
         }

         if (!var3.isEmpty()) {
            String[] var6 = (String[])var3.toArray(new String[0]);

            try {
               var2.requestPermissions(var6, 1);
            } catch (Exception var4) {
            }

         }
      }
   }

   private void closeSearch() {
      if (AndroidUtilities.isTablet()) {
         ActionBar var1 = super.actionBar;
         if (var1 != null) {
            var1.closeSearchField();
         }

         TLObject var2 = this.searchObject;
         if (var2 != null) {
            this.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, var2);
            this.searchObject = null;
         }
      } else {
         this.closeSearchFieldOnHide = true;
      }

   }

   private void didSelectResult(long var1, boolean var3, boolean var4) {
      int var5;
      AlertDialog.Builder var12;
      if (this.addToGroupAlertString == null && this.checkCanWrite) {
         var5 = (int)var1;
         if (var5 < 0) {
            MessagesController var6 = this.getMessagesController();
            var5 = -var5;
            TLRPC.Chat var10 = var6.getChat(var5);
            if (ChatObject.isChannel(var10) && !var10.megagroup && (this.cantSendToChannels || !ChatObject.isCanWriteToChannel(var5, super.currentAccount))) {
               var12 = new AlertDialog.Builder(this.getParentActivity());
               var12.setTitle(LocaleController.getString("AppName", 2131558635));
               var12.setMessage(LocaleController.getString("ChannelCantSendMessage", 2131558940));
               var12.setNegativeButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               this.showDialog(var12.create());
               return;
            }
         }
      }

      if (var3 && (this.selectAlertString != null && this.selectAlertStringGroup != null || this.addToGroupAlertString != null)) {
         if (this.getParentActivity() == null) {
            return;
         }

         var12 = new AlertDialog.Builder(this.getParentActivity());
         var12.setTitle(LocaleController.getString("AppName", 2131558635));
         int var7 = (int)var1;
         var5 = (int)(var1 >> 32);
         TLRPC.User var13;
         if (var7 != 0) {
            if (var5 == 1) {
               TLRPC.Chat var8 = this.getMessagesController().getChat(var7);
               if (var8 == null) {
                  return;
               }

               var12.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, var8.title));
            } else if (var7 == this.getUserConfig().getClientUserId()) {
               var12.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, LocaleController.getString("SavedMessages", 2131560633)));
            } else if (var7 > 0) {
               var13 = this.getMessagesController().getUser(var7);
               if (var13 == null) {
                  return;
               }

               var12.setMessage(LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(var13)));
            } else if (var7 < 0) {
               TLRPC.Chat var9 = this.getMessagesController().getChat(-var7);
               if (var9 == null) {
                  return;
               }

               String var14 = this.addToGroupAlertString;
               if (var14 != null) {
                  var12.setMessage(LocaleController.formatStringSimple(var14, var9.title));
               } else {
                  var12.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, var9.title));
               }
            }
         } else {
            TLRPC.EncryptedChat var15 = this.getMessagesController().getEncryptedChat(var5);
            var13 = this.getMessagesController().getUser(var15.user_id);
            if (var13 == null) {
               return;
            }

            var12.setMessage(LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(var13)));
         }

         var12.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$DialogsActivity$aS99ZGtwHsaaGFpbpdjdMEuMtKw(this, var1));
         var12.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var12.create());
      } else if (this.delegate != null) {
         ArrayList var11 = new ArrayList();
         var11.add(var1);
         this.delegate.didSelectDialogs(this, var11, (CharSequence)null, var4);
         this.delegate = null;
      } else {
         this.finishFragment();
      }

   }

   public static ArrayList getDialogsArray(int var0, int var1, int var2, boolean var3) {
      if (var3) {
         ArrayList var4 = frozenDialogsList;
         if (var4 != null) {
            return var4;
         }
      }

      MessagesController var5 = AccountInstance.getInstance(var0).getMessagesController();
      if (var1 == 0) {
         return var5.getDialogs(var2);
      } else if (var1 == 1) {
         return var5.dialogsServerOnly;
      } else if (var1 == 2) {
         return var5.dialogsCanAddUsers;
      } else if (var1 == 3) {
         return var5.dialogsForward;
      } else if (var1 == 4) {
         return var5.dialogsUsersOnly;
      } else if (var1 == 5) {
         return var5.dialogsChannelsOnly;
      } else {
         return var1 == 6 ? var5.dialogsGroupsOnly : null;
      }
   }

   private int getPinnedCount() {
      ArrayList var1 = this.getMessagesController().getDialogs(this.folderId);
      int var2 = var1.size();
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var2; ++var3) {
         TLRPC.Dialog var5 = (TLRPC.Dialog)var1.get(var3);
         if (!(var5 instanceof TLRPC.TL_dialogFolder)) {
            long var6 = var5.id;
            if (!var5.pinned) {
               break;
            }

            ++var4;
         }
      }

      return var4;
   }

   private UndoView getUndoView() {
      if (this.undoView[0].getVisibility() == 0) {
         UndoView[] var1 = this.undoView;
         UndoView var2 = var1[0];
         var1[0] = var1[1];
         var1[1] = var2;
         var2.hide(true, 2);
         DialogsActivity.ContentView var3 = (DialogsActivity.ContentView)super.fragmentView;
         var3.removeView(this.undoView[0]);
         var3.addView(this.undoView[0]);
      }

      return this.undoView[0];
   }

   private boolean hasHiddenArchive() {
      boolean var1;
      if (this.listView.getAdapter() == this.dialogsAdapter && !this.onlySelect && this.dialogsType == 0 && this.folderId == 0 && this.getMessagesController().hasHiddenArchive()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void hideActionMode(boolean var1) {
      super.actionBar.hideActionMode();
      if (this.menuDrawable != null) {
         super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
      }

      this.dialogsAdapter.getSelectedDialogs().clear();
      MenuDrawable var2 = this.menuDrawable;
      if (var2 != null) {
         var2.setRotation(0.0F, true);
      } else {
         BackDrawable var4 = this.backDrawable;
         if (var4 != null) {
            var4.setRotation(0.0F, true);
         }
      }

      short var3 = 0;
      this.allowMoving = false;
      if (this.movingWas) {
         this.getMessagesController().reorderPinnedDialogs(this.folderId, (ArrayList)null, 0L);
         this.movingWas = false;
      }

      this.updateCounters(true);
      this.dialogsAdapter.onReorderStateChanged(false);
      if (var1) {
         var3 = 8192;
      }

      this.updateVisibleRows(var3 | 196608);
   }

   private void hideFloatingButton(boolean var1) {
      if (this.floatingHidden != var1) {
         this.floatingHidden = var1;
         AnimatorSet var2 = new AnimatorSet();
         FrameLayout var3 = this.floatingButtonContainer;
         Property var4 = View.TRANSLATION_Y;
         float var5;
         if (this.floatingHidden) {
            var5 = (float)AndroidUtilities.dp(100.0F);
         } else {
            var5 = -this.additionalFloatingTranslation;
         }

         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, var4, new float[]{var5})});
         var2.setDuration(300L);
         var2.setInterpolator(this.floatingInterpolator);
         this.floatingButtonContainer.setClickable(var1 ^ true);
         var2.start();
      }
   }

   // $FF: synthetic method
   static void lambda$createView$0(Context var0) {
      Theme.createChatResources(var0, false);
   }

   // $FF: synthetic method
   static boolean lambda$createView$2(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static void lambda$onResume$7(DialogInterface var0, int var1) {
      MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askedAboutMiuiLockscreen", true).commit();
   }

   private void onDialogAnimationFinished() {
      this.dialogRemoveFinished = 0;
      this.dialogInsertFinished = 0;
      this.dialogChangeFinished = 0;
      AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$M7BILTEWS_s5OccwAnpFNVy9dGs(this));
   }

   private void perfromSelectedDialogsAction(int var1, boolean var2) {
      if (this.getParentActivity() != null) {
         ArrayList var3 = this.dialogsAdapter.getSelectedDialogs();
         int var4 = var3.size();
         ArrayList var23;
         if (var1 == 105) {
            var23 = new ArrayList(var3);
            MessagesController var28 = this.getMessagesController();
            byte var20;
            if (this.folderId == 0) {
               var20 = 1;
            } else {
               var20 = 0;
            }

            var28.addDialogToFolder(var23, var20, -1, (ArrayList)null, 0L);
            this.hideActionMode(false);
            if (this.folderId == 0) {
               SharedPreferences var30 = MessagesController.getGlobalMainSettings();
               var2 = var30.getBoolean("archivehint_l", false);
               var30.edit().putBoolean("archivehint_l", true).commit();
               if (var2) {
                  if (var23.size() > 1) {
                     var20 = 4;
                  } else {
                     var20 = 2;
                  }
               } else if (var23.size() > 1) {
                  var20 = 5;
               } else {
                  var20 = 3;
               }

               this.getUndoView().showWithAction(0L, var20, (Runnable)null, new _$$Lambda$DialogsActivity$0CqBdEwjzLcyLDVjAtjOQrlvawc(this, var23));
            } else if (this.getMessagesController().getDialogs(this.folderId).isEmpty()) {
               this.listView.setEmptyView((View)null);
               this.progressView.setVisibility(4);
               this.finishFragment();
            }

         } else {
            int var7;
            int var8;
            int var9;
            long var12;
            if (var1 == 100 && this.canPinCount != 0) {
               var23 = this.getMessagesController().getDialogs(this.folderId);
               var7 = var23.size();
               var8 = 0;
               var9 = 0;

               int var10;
               int var11;
               for(var10 = 0; var8 < var7; ++var8) {
                  TLRPC.Dialog var6 = (TLRPC.Dialog)var23.get(var8);
                  if (!(var6 instanceof TLRPC.TL_dialogFolder)) {
                     var11 = (int)var6.id;
                     if (!var6.pinned) {
                        break;
                     }

                     if (var11 == 0) {
                        ++var10;
                     } else {
                        ++var9;
                     }
                  }
               }

               var11 = 0;
               var7 = 0;
               var8 = 0;

               while(true) {
                  if (var11 >= var4) {
                     if (this.folderId != 0) {
                        var11 = this.getMessagesController().maxFolderPinnedDialogsCount;
                     } else {
                        var11 = this.getMessagesController().maxPinnedDialogsCount;
                     }

                     if (var7 + var10 > var11 || var8 + var9 > var11) {
                        AlertsCreator.showSimpleToast(this, LocaleController.formatString("PinToTopLimitReached", 2131560448, LocaleController.formatPluralString("Chats", var11)));
                        AndroidUtilities.shakeView(this.pinItem, 2.0F, 0);
                        Vibrator var33 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
                        if (var33 != null) {
                           var33.vibrate(200L);
                        }

                        return;
                     }
                     break;
                  }

                  var12 = (Long)var3.get(var11);
                  TLRPC.Dialog var24 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(var12);
                  int var14 = var7;
                  int var15 = var8;
                  if (var24 != null) {
                     if (var24.pinned) {
                        var14 = var7;
                        var15 = var8;
                     } else if ((int)var12 == 0) {
                        var14 = var7 + 1;
                        var15 = var8;
                     } else {
                        var15 = var8 + 1;
                        var14 = var7;
                     }
                  }

                  ++var11;
                  var7 = var14;
                  var8 = var15;
               }
            } else if ((var1 == 102 || var1 == 103) && var4 > 1 && var2 && var2) {
               AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
               if (var1 == 102) {
                  var5.setTitle(LocaleController.formatString("DeleteFewChatsTitle", 2131559242, LocaleController.formatPluralString("ChatsSelected", var4)));
                  var5.setMessage(LocaleController.getString("AreYouSureDeleteFewChats", 2131558681));
                  var5.setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$DialogsActivity$jWd3WTqe_JWytlcg5OAtRHiocr4(this, var1));
               } else if (this.canClearCacheCount != 0) {
                  var5.setTitle(LocaleController.formatString("ClearCacheFewChatsTitle", 2131559104, LocaleController.formatPluralString("ChatsSelectedClearCache", var4)));
                  var5.setMessage(LocaleController.getString("AreYouSureClearHistoryCacheFewChats", 2131558670));
                  var5.setPositiveButton(LocaleController.getString("ClearHistoryCache", 2131559108), new _$$Lambda$DialogsActivity$iMQ7bU7WAaE9d3J2u7XQeCZi3Ow(this, var1));
               } else {
                  var5.setTitle(LocaleController.formatString("ClearFewChatsTitle", 2131559106, LocaleController.formatPluralString("ChatsSelectedClear", var4)));
                  var5.setMessage(LocaleController.getString("AreYouSureClearHistoryFewChats", 2131558672));
                  var5.setPositiveButton(LocaleController.getString("ClearHistory", 2131559107), new _$$Lambda$DialogsActivity$4jeFfbPa00Df67qyJvj0h0oI_X4(this, var1));
               }

               var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               AlertDialog var21 = var5.create();
               this.showDialog(var21);
               TextView var22 = (TextView)var21.getButton(-1);
               if (var22 != null) {
                  var22.setTextColor(Theme.getColor("dialogTextRed2"));
               }

               return;
            }

            var9 = 0;

            boolean var18;
            boolean var34;
            for(var34 = false; var9 < var4; ++var9) {
               var12 = (Long)var3.get(var9);
               TLRPC.Dialog var16 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(var12);
               if (var16 != null) {
                  Object var25;
                  TLRPC.Chat var27;
                  label230: {
                     var8 = (int)var12;
                     var7 = (int)(var12 >> 32);
                     Object var26;
                     if (var8 != 0) {
                        if (var8 <= 0) {
                           var27 = this.getMessagesController().getChat(-var8);
                           var25 = null;
                           break label230;
                        }

                        var26 = this.getMessagesController().getUser(var8);
                     } else {
                        TLRPC.EncryptedChat var29 = this.getMessagesController().getEncryptedChat(var7);
                        if (var29 != null) {
                           var26 = this.getMessagesController().getUser(var29.user_id);
                        } else {
                           var26 = new TLRPC.TL_userEmpty();
                        }
                     }

                     Object var17 = null;
                     var25 = var26;
                     var27 = (TLRPC.Chat)var17;
                  }

                  if (var27 != null || var25 != null) {
                     if (var25 != null && ((TLRPC.User)var25).bot && !MessagesController.isSupportUser((TLRPC.User)var25)) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     if (var1 == 100) {
                        if (this.canPinCount != 0) {
                           if (var16.pinned || !this.getMessagesController().pinDialog(var12, true, (TLRPC.InputPeer)null, -1L)) {
                              continue;
                           }
                        } else if (!var16.pinned || !this.getMessagesController().pinDialog(var12, false, (TLRPC.InputPeer)null, -1L)) {
                           continue;
                        }

                        var34 = true;
                     } else if (var1 == 101) {
                        if (this.canReadCount != 0) {
                           this.getMessagesController().markMentionsAsRead(var12);
                           MessagesController var32 = this.getMessagesController();
                           var8 = var16.top_message;
                           var32.markDialogAsRead(var12, var8, var8, var16.last_message_date, false, 0, true);
                        } else {
                           this.getMessagesController().markDialogAsUnread(var12, (TLRPC.InputPeer)null, 0L);
                        }
                     } else if (var1 != 102 && var1 != 103) {
                        if (var1 == 104) {
                           if (var4 == 1 && this.canMuteCount == 1) {
                              this.showDialog(AlertsCreator.createMuteAlert(this.getParentActivity(), var12), new _$$Lambda$DialogsActivity$1NIJBQ0Gz4LgguHSbB06qjn5FYs(this));
                              return;
                           }

                           if (this.canUnmuteCount != 0) {
                              if (this.getMessagesController().isDialogMuted(var12)) {
                                 this.getNotificationsController().setDialogNotificationsSettings(var12, 4);
                              }
                           } else if (!this.getMessagesController().isDialogMuted(var12)) {
                              this.getNotificationsController().setDialogNotificationsSettings(var12, 3);
                           }
                        }
                     } else {
                        if (var4 == 1) {
                           if (var1 == 103) {
                              var18 = true;
                           } else {
                              var18 = false;
                           }

                           boolean var19;
                           if (var8 == 0) {
                              var19 = true;
                           } else {
                              var19 = false;
                           }

                           AlertsCreator.createClearOrDeleteDialogAlert(this, var18, var27, (TLRPC.User)var25, var19, new _$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg(this, var1, var27, var12, var2));
                           return;
                        }

                        if (var1 == 103 && this.canClearCacheCount != 0) {
                           this.getMessagesController().deleteDialog(var12, 2, false);
                        } else if (var1 == 103) {
                           this.getMessagesController().deleteDialog(var12, 1, false);
                        } else {
                           if (var27 != null) {
                              if (ChatObject.isNotInChat(var27)) {
                                 this.getMessagesController().deleteDialog(var12, 0, false);
                              } else {
                                 TLRPC.User var31 = this.getMessagesController().getUser(this.getUserConfig().getClientUserId());
                                 this.getMessagesController().deleteUserFromChat((int)(-var12), var31, (TLRPC.ChatFull)null);
                              }
                           } else {
                              this.getMessagesController().deleteDialog(var12, 0, false);
                              if (var2) {
                                 this.getMessagesController().blockUser(var8);
                              }
                           }

                           if (AndroidUtilities.isTablet()) {
                              this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, var12);
                           }
                        }
                     }
                  }
               }
            }

            if (var1 == 100) {
               this.getMessagesController().reorderPinnedDialogs(this.folderId, (ArrayList)null, 0L);
            }

            if (var34) {
               this.hideFloatingButton(false);
               this.listView.smoothScrollToPosition(this.hasHiddenArchive());
            }

            var18 = false;
            var2 = var18;
            if (var1 != 100) {
               var2 = var18;
               if (var1 != 102) {
                  var2 = true;
               }
            }

            this.hideActionMode(var2);
         }
      }
   }

   private void setDialogsListFrozen(boolean var1) {
      if (this.dialogsListFrozen != var1) {
         if (var1) {
            frozenDialogsList = new ArrayList(getDialogsArray(super.currentAccount, this.dialogsType, this.folderId, false));
         } else {
            frozenDialogsList = null;
         }

         this.dialogsListFrozen = var1;
         this.dialogsAdapter.setDialogsListFrozen(var1);
         if (!var1) {
            this.dialogsAdapter.notifyDataSetChanged();
         }

      }
   }

   private void showOrUpdateActionMode(TLRPC.Dialog var1, View var2) {
      this.dialogsAdapter.addOrRemoveSelectedDialog(var1.id, var2);
      ArrayList var8 = this.dialogsAdapter.getSelectedDialogs();
      boolean var3 = super.actionBar.isActionModeShowed();
      boolean var4 = true;
      if (var3) {
         if (var8.isEmpty()) {
            this.hideActionMode(true);
            return;
         }
      } else {
         super.actionBar.createActionMode();
         super.actionBar.showActionMode();
         if (this.menuDrawable != null) {
            super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
         }

         if (this.getPinnedCount() > 1) {
            this.dialogsAdapter.onReorderStateChanged(true);
            this.updateVisibleRows(131072);
         }

         AnimatorSet var5 = new AnimatorSet();
         ArrayList var6 = new ArrayList();

         for(int var7 = 0; var7 < this.actionModeViews.size(); ++var7) {
            var2 = (View)this.actionModeViews.get(var7);
            var2.setPivotY((float)(ActionBar.getCurrentActionBarHeight() / 2));
            AndroidUtilities.clearDrawableAnimation(var2);
            var6.add(ObjectAnimator.ofFloat(var2, View.SCALE_Y, new float[]{0.1F, 1.0F}));
         }

         var5.playTogether(var6);
         var5.setDuration(250L);
         var5.start();
         MenuDrawable var9 = this.menuDrawable;
         if (var9 != null) {
            var9.setRotateToBack(false);
            this.menuDrawable.setRotation(1.0F, true);
         } else {
            BackDrawable var10 = this.backDrawable;
            if (var10 != null) {
               var10.setRotation(1.0F, true);
            }
         }

         var4 = false;
      }

      this.updateCounters(false);
      this.selectedDialogsCountTextView.setNumber(var8.size(), var4);
   }

   private void updateCounters(boolean var1) {
      this.canUnmuteCount = 0;
      this.canMuteCount = 0;
      this.canPinCount = 0;
      this.canReadCount = 0;
      this.canClearCacheCount = 0;
      if (!var1) {
         ArrayList var2 = this.dialogsAdapter.getSelectedDialogs();
         int var3 = var2.size();
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;

         while(true) {
            var1 = true;
            int var22;
            if (var4 >= var3) {
               if (var5 != var3) {
                  this.deleteItem.setVisibility(8);
               } else {
                  this.deleteItem.setVisibility(0);
               }

               var22 = this.canClearCacheCount;
               if ((var22 == 0 || var22 == var3) && (var6 == 0 || var6 == var3)) {
                  this.clearItem.setVisibility(0);
                  if (this.canClearCacheCount != 0) {
                     this.clearItem.setText(LocaleController.getString("ClearHistoryCache", 2131559108));
                  } else {
                     this.clearItem.setText(LocaleController.getString("ClearHistory", 2131559107));
                  }
               } else {
                  this.clearItem.setVisibility(8);
               }

               if (var7 != 0) {
                  this.archiveItem.setIcon(2131165676);
                  this.archiveItem.setContentDescription(LocaleController.getString("Unarchive", 2131560928));
               } else {
                  this.archiveItem.setIcon(2131165613);
                  this.archiveItem.setContentDescription(LocaleController.getString("Archive", 2131558642));
                  ActionBarMenuItem var21 = this.archiveItem;
                  if (var8 == 0) {
                     var1 = false;
                  }

                  var21.setEnabled(var1);
                  var21 = this.archiveItem;
                  float var17;
                  if (var8 != 0) {
                     var17 = 1.0F;
                  } else {
                     var17 = 0.5F;
                  }

                  var21.setAlpha(var17);
               }

               if (this.canPinCount + var9 != var3) {
                  this.pinItem.setVisibility(8);
               } else {
                  this.pinItem.setVisibility(0);
               }

               if (this.canUnmuteCount != 0) {
                  this.muteItem.setTextAndIcon(LocaleController.getString("ChatsUnmute", 2131559078), 2131165678);
               } else {
                  this.muteItem.setTextAndIcon(LocaleController.getString("ChatsMute", 2131559059), 2131165648);
               }

               if (this.canReadCount != 0) {
                  this.readItem.setTextAndIcon(LocaleController.getString("MarkAsRead", 2131559807), 2131165643);
               } else {
                  this.readItem.setTextAndIcon(LocaleController.getString("MarkAsUnread", 2131559808), 2131165644);
               }

               if (this.canPinCount != 0) {
                  this.pinItem.setIcon(2131165657);
                  this.pinItem.setContentDescription(LocaleController.getString("PinToTop", 2131560447));
               } else {
                  this.pinItem.setIcon(2131165679);
                  this.pinItem.setContentDescription(LocaleController.getString("UnpinFromTop", 2131560941));
               }

               return;
            }

            TLRPC.Dialog var10 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get((Long)var2.get(var4));
            int var11;
            if (var10 == null) {
               var11 = var7;
               var22 = var8;
            } else {
               label204: {
                  long var13 = var10.id;
                  var1 = var10.pinned;
                  boolean var12;
                  if (var10.unread_count == 0 && !var10.unread_mark) {
                     var12 = false;
                  } else {
                     var12 = true;
                  }

                  if (this.getMessagesController().isDialogMuted(var13)) {
                     ++this.canUnmuteCount;
                  } else {
                     ++this.canMuteCount;
                  }

                  if (var12) {
                     ++this.canReadCount;
                  }

                  if (this.folderId == 1) {
                     var11 = var7 + 1;
                     var22 = var8;
                  } else {
                     var11 = var7;
                     var22 = var8;
                     if (var13 != (long)this.getUserConfig().getClientUserId()) {
                        var11 = var7;
                        var22 = var8;
                        if (var13 != 777000L) {
                           var11 = var7;
                           var22 = var8;
                           if (!this.getMessagesController().isProxyDialog(var13, false)) {
                              var22 = var8 + 1;
                              var11 = var7;
                           }
                        }
                     }
                  }

                  label167: {
                     int var15 = (int)var13;
                     var7 = (int)(var13 >> 32);
                     if (DialogObject.isChannel(var10)) {
                        TLRPC.Chat var16 = this.getMessagesController().getChat(-var15);
                        if (this.getMessagesController().isProxyDialog(var10.id, true)) {
                           ++this.canClearCacheCount;
                           break label204;
                        }

                        if (var1) {
                           ++var9;
                        } else {
                           ++this.canPinCount;
                        }

                        if (var16 == null || !var16.megagroup) {
                           ++this.canClearCacheCount;
                           break label167;
                        }

                        if (!TextUtils.isEmpty(var16.username)) {
                           ++this.canClearCacheCount;
                           break label167;
                        }
                     } else {
                        boolean var18;
                        if (var15 < 0 && var7 != 1) {
                           var18 = true;
                        } else {
                           var18 = false;
                        }

                        if (var18) {
                           this.getMessagesController().getChat(-var15);
                        }

                        Object var19;
                        if (var15 == 0) {
                           TLRPC.EncryptedChat var20 = this.getMessagesController().getEncryptedChat(var7);
                           if (var20 != null) {
                              var19 = this.getMessagesController().getUser(var20.user_id);
                           } else {
                              var19 = new TLRPC.TL_userEmpty();
                           }
                        } else if (!var18 && var15 > 0 && var7 != 1) {
                           var19 = this.getMessagesController().getUser(var15);
                        } else {
                           var19 = null;
                        }

                        if (var19 != null && ((TLRPC.User)var19).bot) {
                           MessagesController.isSupportUser((TLRPC.User)var19);
                        }

                        if (var1) {
                           ++var9;
                        } else {
                           ++this.canPinCount;
                        }
                     }

                     ++var6;
                  }

                  ++var5;
               }
            }

            ++var4;
            var7 = var11;
            var8 = var22;
         }
      }
   }

   private void updateDialogIndices() {
      RecyclerListView var1 = this.listView;
      if (var1 != null && var1.getAdapter() == this.dialogsAdapter) {
         int var2 = super.currentAccount;
         int var3 = this.dialogsType;
         int var4 = this.folderId;
         int var5 = 0;
         ArrayList var8 = getDialogsArray(var2, var3, var4, false);

         for(var4 = this.listView.getChildCount(); var5 < var4; ++var5) {
            View var6 = this.listView.getChildAt(var5);
            if (var6 instanceof DialogCell) {
               DialogCell var9 = (DialogCell)var6;
               TLRPC.Dialog var7 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(var9.getDialogId());
               if (var7 != null) {
                  var3 = var8.indexOf(var7);
                  if (var3 >= 0) {
                     var9.setDialogIndex(var3);
                  }
               }
            }
         }
      }

   }

   private void updatePasscodeButton() {
      if (this.passcodeItem != null) {
         if (SharedConfig.passcodeHash.length() != 0 && !this.searching) {
            this.passcodeItem.setVisibility(0);
            if (SharedConfig.appLocked) {
               this.passcodeItem.setIcon(2131165542);
               this.passcodeItem.setContentDescription(LocaleController.getString("AccDescrPasscodeUnlock", 2131558454));
            } else {
               this.passcodeItem.setIcon(2131165544);
               this.passcodeItem.setContentDescription(LocaleController.getString("AccDescrPasscodeLock", 2131558453));
            }
         } else {
            this.passcodeItem.setVisibility(8);
         }

      }
   }

   private void updateProxyButton(boolean var1) {
      if (this.proxyDrawable != null) {
         Context var2 = ApplicationLoader.applicationContext;
         boolean var3 = false;
         SharedPreferences var4 = var2.getSharedPreferences("mainconfig", 0);
         String var7 = var4.getString("proxy_ip", "");
         boolean var5;
         if (var4.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(var7)) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5 || this.getMessagesController().blockedCountry && !SharedConfig.proxyList.isEmpty()) {
            if (!super.actionBar.isSearchFieldVisible()) {
               this.proxyItem.setVisibility(0);
            }

            ProxyDrawable var8 = this.proxyDrawable;
            int var6 = this.currentConnectionState;
            if (var6 == 3 || var6 == 5) {
               var3 = true;
            }

            var8.setConnected(var5, var3, var1);
            this.proxyItemVisisble = true;
         } else {
            this.proxyItem.setVisibility(8);
            this.proxyItemVisisble = false;
         }

      }
   }

   private void updateSelectedCount() {
      if (this.commentView != null) {
         if (!this.dialogsAdapter.hasSelectedDialogs()) {
            if (this.dialogsType == 3 && this.selectAlertString == null) {
               super.actionBar.setTitle(LocaleController.getString("ForwardTo", 2131559505));
            } else {
               super.actionBar.setTitle(LocaleController.getString("SelectChat", 2131560677));
            }

            if (this.commentView.getTag() != null) {
               this.commentView.hidePopup(false);
               this.commentView.closeKeyboard();
               AnimatorSet var1 = new AnimatorSet();
               ChatActivityEnterView var2 = this.commentView;
               var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(var2, View.TRANSLATION_Y, new float[]{0.0F, (float)var2.getMeasuredHeight()})});
               var1.setDuration(180L);
               var1.setInterpolator(new DecelerateInterpolator());
               var1.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     DialogsActivity.this.commentView.setVisibility(8);
                  }
               });
               var1.start();
               this.commentView.setTag((Object)null);
               this.listView.requestLayout();
            }
         } else {
            if (this.commentView.getTag() == null) {
               this.commentView.setFieldText("");
               this.commentView.setVisibility(0);
               AnimatorSet var4 = new AnimatorSet();
               ChatActivityEnterView var3 = this.commentView;
               var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, View.TRANSLATION_Y, new float[]{(float)var3.getMeasuredHeight(), 0.0F})});
               var4.setDuration(180L);
               var4.setInterpolator(new DecelerateInterpolator());
               var4.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     DialogsActivity.this.commentView.setTag(2);
                     DialogsActivity.this.commentView.requestLayout();
                  }
               });
               var4.start();
               this.commentView.setTag(1);
            }

            super.actionBar.setTitle(LocaleController.formatPluralString("Recipient", this.dialogsAdapter.getSelectedDialogs().size()));
         }

      }
   }

   private void updateVisibleRows(int var1) {
      RecyclerListView var2 = this.listView;
      if (var2 != null && !this.dialogsListFrozen) {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var11 = this.listView.getChildAt(var4);
            if (var11 instanceof DialogCell) {
               if (this.listView.getAdapter() != this.dialogsSearchAdapter) {
                  DialogCell var12 = (DialogCell)var11;
                  boolean var5 = true;
                  boolean var6 = true;
                  boolean var7 = true;
                  if ((131072 & var1) != 0) {
                     var12.onReorderStateChanged(super.actionBar.isActionModeShowed(), true);
                  }

                  if ((65536 & var1) != 0) {
                     if ((var1 & 8192) == 0) {
                        var7 = false;
                     }

                     var12.setChecked(false, var7);
                  } else {
                     if ((var1 & 2048) != 0) {
                        var12.checkCurrentDialogIndex(this.dialogsListFrozen);
                        if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                           if (var12.getDialogId() == this.openedDialogId) {
                              var7 = var5;
                           } else {
                              var7 = false;
                           }

                           var12.setDialogSelected(var7);
                        }
                     } else if ((var1 & 512) != 0) {
                        if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                           if (var12.getDialogId() == this.openedDialogId) {
                              var7 = var6;
                           } else {
                              var7 = false;
                           }

                           var12.setDialogSelected(var7);
                        }
                     } else {
                        var12.update(var1);
                     }

                     ArrayList var13 = this.dialogsAdapter.getSelectedDialogs();
                     if (var13 != null) {
                        var12.setChecked(var13.contains(var12.getDialogId()), false);
                     }
                  }
               }
            } else if (var11 instanceof UserCell) {
               ((UserCell)var11).update(var1);
            } else if (var11 instanceof ProfileSearchCell) {
               ((ProfileSearchCell)var11).update(var1);
            } else if (var11 instanceof RecyclerListView) {
               RecyclerListView var8 = (RecyclerListView)var11;
               int var9 = var8.getChildCount();

               for(int var10 = 0; var10 < var9; ++var10) {
                  var11 = var8.getChildAt(var10);
                  if (var11 instanceof HintDialogCell) {
                     ((HintDialogCell)var11).update(var1);
                  }
               }
            }
         }
      }

   }

   private boolean waitingForDialogsAnimationEnd() {
      boolean var1;
      if (!this.dialogsItemAnimator.isRunning() && this.dialogRemoveFinished == 0 && this.dialogInsertFinished == 0 && this.dialogChangeFinished == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      this.pacmanAnimation = null;
      AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$Wz8eSYJLAR_xfNaDl4aQWUwW9N0(var1));
      ActionBarMenu var2 = super.actionBar.createMenu();
      if (!this.onlySelect && this.searchString == null && this.folderId == 0) {
         this.proxyDrawable = new ProxyDrawable(var1);
         this.proxyItem = var2.addItem(2, this.proxyDrawable);
         this.proxyItem.setContentDescription(LocaleController.getString("ProxySettings", 2131560519));
         this.passcodeItem = var2.addItem(1, 2131165542);
         this.updatePasscodeButton();
         this.updateProxyButton(false);
      }

      ActionBarMenuItem var3 = var2.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public boolean canCollapseSearch() {
            if (DialogsActivity.this.switchItem != null) {
               DialogsActivity.this.switchItem.setVisibility(0);
            }

            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisisble) {
               DialogsActivity.this.proxyItem.setVisibility(0);
            }

            if (DialogsActivity.this.searchString != null) {
               DialogsActivity.this.finishFragment();
               return false;
            } else {
               return true;
            }
         }

         public void onSearchCollapse() {
            DialogsActivity.this.searching = false;
            DialogsActivity.this.searchWas = false;
            if (DialogsActivity.this.listView != null) {
               RecyclerListView var1 = DialogsActivity.this.listView;
               RadialProgressView var2;
               if (DialogsActivity.this.folderId == 0) {
                  var2 = DialogsActivity.this.progressView;
               } else {
                  var2 = null;
               }

               var1.setEmptyView(var2);
               DialogsActivity.this.searchEmptyView.setVisibility(8);
               if (!DialogsActivity.this.onlySelect) {
                  DialogsActivity.this.floatingButtonContainer.setVisibility(0);
                  DialogsActivity.this.floatingHidden = true;
                  DialogsActivity.this.floatingButtonContainer.setTranslationY((float)AndroidUtilities.dp(100.0F));
                  DialogsActivity.this.hideFloatingButton(false);
               }

               if (DialogsActivity.this.listView.getAdapter() != DialogsActivity.this.dialogsAdapter) {
                  DialogsActivity.this.listView.setAdapter(DialogsActivity.this.dialogsAdapter);
                  DialogsActivity.this.dialogsAdapter.notifyDataSetChanged();
               }
            }

            if (DialogsActivity.this.dialogsSearchAdapter != null) {
               DialogsActivity.this.dialogsSearchAdapter.searchDialogs((String)null);
            }

            DialogsActivity.this.updatePasscodeButton();
            if (DialogsActivity.this.menuDrawable != null) {
               DialogsActivity.access$5700(DialogsActivity.this).setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
            }

         }

         public void onSearchExpand() {
            DialogsActivity.this.searching = true;
            if (DialogsActivity.this.switchItem != null) {
               DialogsActivity.this.switchItem.setVisibility(8);
            }

            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisisble) {
               DialogsActivity.this.proxyItem.setVisibility(8);
            }

            if (DialogsActivity.this.listView != null) {
               if (DialogsActivity.this.searchString != null) {
                  DialogsActivity.this.listView.setEmptyView(DialogsActivity.this.searchEmptyView);
                  DialogsActivity.this.progressView.setVisibility(8);
               }

               if (!DialogsActivity.this.onlySelect) {
                  DialogsActivity.this.floatingButtonContainer.setVisibility(8);
               }
            }

            DialogsActivity.this.updatePasscodeButton();
            DialogsActivity.access$5100(DialogsActivity.this).setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
         }

         public void onTextChanged(EditText var1) {
            String var2 = var1.getText().toString();
            if (var2.length() != 0 || DialogsActivity.this.dialogsSearchAdapter != null && DialogsActivity.this.dialogsSearchAdapter.hasRecentRearch()) {
               DialogsActivity.this.searchWas = true;
               if (DialogsActivity.this.dialogsSearchAdapter != null && DialogsActivity.this.listView.getAdapter() != DialogsActivity.this.dialogsSearchAdapter) {
                  DialogsActivity.this.listView.setAdapter(DialogsActivity.this.dialogsSearchAdapter);
                  DialogsActivity.this.dialogsSearchAdapter.notifyDataSetChanged();
               }

               if (DialogsActivity.this.searchEmptyView != null && DialogsActivity.this.listView.getEmptyView() != DialogsActivity.this.searchEmptyView) {
                  DialogsActivity.this.progressView.setVisibility(8);
                  DialogsActivity.this.listView.setEmptyView(DialogsActivity.this.searchEmptyView);
               }
            }

            if (DialogsActivity.this.dialogsSearchAdapter != null) {
               DialogsActivity.this.dialogsSearchAdapter.searchDialogs(var2);
            }

         }
      });
      var3.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      var3.setContentDescription(LocaleController.getString("Search", 2131560640));
      if (this.onlySelect) {
         super.actionBar.setBackButtonImage(2131165409);
         if (this.dialogsType == 3 && this.selectAlertString == null) {
            super.actionBar.setTitle(LocaleController.getString("ForwardTo", 2131559505));
         } else {
            super.actionBar.setTitle(LocaleController.getString("SelectChat", 2131560677));
         }
      } else {
         ActionBar var15;
         if (this.searchString == null && this.folderId == 0) {
            var15 = super.actionBar;
            MenuDrawable var19 = new MenuDrawable();
            this.menuDrawable = var19;
            var15.setBackButtonDrawable(var19);
            super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
         } else {
            var15 = super.actionBar;
            BackDrawable var4 = new BackDrawable(false);
            this.backDrawable = var4;
            var15.setBackButtonDrawable(var4);
         }

         if (this.folderId != 0) {
            super.actionBar.setTitle(LocaleController.getString("ArchivedChats", 2131558653));
         } else if (BuildVars.DEBUG_VERSION) {
            super.actionBar.setTitle("Telegram Beta");
         } else {
            super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
         }

         super.actionBar.setSupportsHolidayImage(true);
      }

      super.actionBar.setTitleActionRunnable(new _$$Lambda$DialogsActivity$g_ZetuAZEmHs4itgVWSuCgX_8K4(this));
      int var5;
      if (this.allowSwitchAccount && UserConfig.getActivatedAccountsCount() > 1) {
         this.switchItem = var2.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0F));
         AvatarDrawable var12 = new AvatarDrawable();
         var12.setTextSize(AndroidUtilities.dp(12.0F));
         BackupImageView var17 = new BackupImageView(var1);
         var17.setRoundRadius(AndroidUtilities.dp(18.0F));
         this.switchItem.addView(var17, LayoutHelper.createFrame(36, 36, 17));
         TLRPC.User var22 = this.getUserConfig().getCurrentUser();
         var12.setInfo(var22);
         var17.getImageReceiver().setCurrentAccount(super.currentAccount);
         var17.setImage((ImageLocation)ImageLocation.getForUser(var22, false), "50_50", (Drawable)var12, (Object)var22);

         for(var5 = 0; var5 < 3; ++var5) {
            if (AccountInstance.getInstance(var5).getUserConfig().getCurrentUser() != null) {
               AccountSelectCell var13 = new AccountSelectCell(var1);
               var13.setAccount(var5, true);
               this.switchItem.addSubItem(var5 + 10, var13, AndroidUtilities.dp(230.0F), AndroidUtilities.dp(48.0F));
            }
         }
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (DialogsActivity.access$5800(DialogsActivity.this).isActionModeShowed()) {
                  DialogsActivity.this.hideActionMode(true);
               } else if (!DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0) {
                  if (DialogsActivity.access$6000(DialogsActivity.this) != null) {
                     DialogsActivity.access$6100(DialogsActivity.this).getDrawerLayoutContainer().openDrawer(false);
                  }
               } else {
                  DialogsActivity.this.finishFragment();
               }
            } else if (var1 == 1) {
               SharedConfig.appLocked ^= true;
               SharedConfig.saveConfig();
               DialogsActivity.this.updatePasscodeButton();
            } else if (var1 == 2) {
               DialogsActivity.this.presentFragment(new ProxyListActivity());
            } else if (var1 >= 10 && var1 < 13) {
               if (DialogsActivity.this.getParentActivity() == null) {
                  return;
               }

               DialogsActivity.DialogsActivityDelegate var2 = DialogsActivity.this.delegate;
               LaunchActivity var3 = (LaunchActivity)DialogsActivity.this.getParentActivity();
               var3.switchToAccount(var1 - 10, true);
               DialogsActivity var4 = new DialogsActivity(DialogsActivity.access$6300(DialogsActivity.this));
               var4.setDelegate(var2);
               var3.presentFragment(var4, false, true);
            } else if (var1 == 100 || var1 == 101 || var1 == 102 || var1 == 103 || var1 == 104 || var1 == 105) {
               DialogsActivity.this.perfromSelectedDialogsAction(var1, true);
            }

         }
      });
      RecyclerView var14 = this.sideMenu;
      if (var14 != null) {
         var14.setBackgroundColor(Theme.getColor("chats_menuBackground"));
         this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
         this.sideMenu.getAdapter().notifyDataSetChanged();
      }

      var2 = super.actionBar.createActionMode();
      this.selectedDialogsCountTextView = new NumberTextView(var2.getContext());
      this.selectedDialogsCountTextView.setTextSize(18);
      this.selectedDialogsCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.selectedDialogsCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
      var2.addView(this.selectedDialogsCountTextView, LayoutHelper.createLinear(0, -1, 1.0F, 72, 0, 0, 0));
      this.selectedDialogsCountTextView.setOnTouchListener(_$$Lambda$DialogsActivity$pwzbQ3D6N1rpnIvtZZSb6O4Np6g.INSTANCE);
      this.pinItem = var2.addItemWithWidth(100, 2131165657, AndroidUtilities.dp(54.0F));
      this.archiveItem = var2.addItemWithWidth(105, 2131165613, AndroidUtilities.dp(54.0F));
      this.deleteItem = var2.addItemWithWidth(102, 2131165623, AndroidUtilities.dp(54.0F), LocaleController.getString("Delete", 2131559227));
      ActionBarMenuItem var16 = var2.addItemWithWidth(0, 2131165416, AndroidUtilities.dp(54.0F), LocaleController.getString("AccDescrMoreOptions", 2131558443));
      this.muteItem = var16.addSubItem(104, 2131165648, LocaleController.getString("ChatsMute", 2131559059));
      this.readItem = var16.addSubItem(101, 2131165643, LocaleController.getString("MarkAsRead", 2131559807));
      this.clearItem = var16.addSubItem(103, 2131165619, LocaleController.getString("ClearHistory", 2131559107));
      this.actionModeViews.add(this.pinItem);
      this.actionModeViews.add(this.archiveItem);
      this.actionModeViews.add(this.deleteItem);
      this.actionModeViews.add(var16);
      DialogsActivity.ContentView var24 = new DialogsActivity.ContentView(var1);
      super.fragmentView = var24;
      this.listView = new RecyclerListView(var1) {
         private boolean firstLayout = true;
         private boolean ignoreLayout;

         private void checkIfAdapterValid() {
            if (DialogsActivity.this.listView != null && DialogsActivity.this.dialogsAdapter != null && DialogsActivity.this.listView.getAdapter() == DialogsActivity.this.dialogsAdapter && DialogsActivity.this.lastItemsCount != DialogsActivity.this.dialogsAdapter.getItemCount()) {
               this.ignoreLayout = true;
               DialogsActivity.this.dialogsAdapter.notifyDataSetChanged();
               this.ignoreLayout = false;
            }

         }

         protected void dispatchDraw(Canvas var1) {
            super.dispatchDraw(var1);
            if (DialogsActivity.this.slidingView != null && DialogsActivity.this.pacmanAnimation != null) {
               DialogsActivity.this.pacmanAnimation.draw(var1, DialogsActivity.this.slidingView.getTop() + DialogsActivity.this.slidingView.getMeasuredHeight() / 2);
            }

         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (!DialogsActivity.this.waitingForScrollFinished && DialogsActivity.this.dialogRemoveFinished == 0 && DialogsActivity.this.dialogInsertFinished == 0 && DialogsActivity.this.dialogChangeFinished == 0) {
               if (var1.getAction() == 0) {
                  DialogsActivity var2 = DialogsActivity.this;
                  var2.allowSwipeDuringCurrentTouch = DialogsActivity.access$7200(var2).isActionModeShowed() ^ true;
                  this.checkIfAdapterValid();
               }

               return super.onInterceptTouchEvent(var1);
            } else {
               return false;
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if ((DialogsActivity.this.dialogRemoveFinished != 0 || DialogsActivity.this.dialogInsertFinished != 0 || DialogsActivity.this.dialogChangeFinished != 0) && !DialogsActivity.this.dialogsItemAnimator.isRunning()) {
               DialogsActivity.this.onDialogAnimationFinished();
            }

         }

         protected void onMeasure(int var1, int var2) {
            if (this.firstLayout && DialogsActivity.this.getMessagesController().dialogsLoaded) {
               if (DialogsActivity.this.hasHiddenArchive()) {
                  this.ignoreLayout = true;
                  DialogsActivity.this.layoutManager.scrollToPositionWithOffset(1, 0);
                  this.ignoreLayout = false;
               }

               this.firstLayout = false;
            }

            this.checkIfAdapterValid();
            super.onMeasure(var1, var2);
         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (!DialogsActivity.this.waitingForScrollFinished && DialogsActivity.this.dialogRemoveFinished == 0 && DialogsActivity.this.dialogInsertFinished == 0 && DialogsActivity.this.dialogChangeFinished == 0) {
               int var2 = var1.getAction();
               if ((var2 == 1 || var2 == 3) && !DialogsActivity.this.itemTouchhelper.isIdle() && DialogsActivity.this.swipeController.swipingFolder) {
                  DialogsActivity.this.swipeController.swipeFolderBack = true;
                  if (DialogsActivity.this.itemTouchhelper.checkHorizontalSwipe((RecyclerView.ViewHolder)null, 4) != 0) {
                     SharedConfig.toggleArchiveHidden();
                     DialogsActivity.this.getUndoView().showWithAction(0L, 7, (Runnable)null, (Runnable)null);
                  }
               }

               boolean var3 = super.onTouchEvent(var1);
               if ((var2 == 1 || var2 == 3) && DialogsActivity.this.allowScrollToHiddenView) {
                  var2 = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                  if (var2 == 0) {
                     View var6 = DialogsActivity.this.layoutManager.findViewByPosition(var2);
                     float var4;
                     if (SharedConfig.useThreeLinesLayout) {
                        var4 = 78.0F;
                     } else {
                        var4 = 72.0F;
                     }

                     int var5 = AndroidUtilities.dp(var4) / 4;
                     var2 = var6.getTop() + var6.getMeasuredHeight();
                     if (var6 != null) {
                        if (var2 < var5 * 3) {
                           DialogsActivity.this.listView.smoothScrollBy(0, var2, CubicBezierInterpolator.EASE_OUT_QUINT);
                        } else {
                           DialogsActivity.this.listView.smoothScrollBy(0, var6.getTop(), CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                     }
                  }

                  DialogsActivity.this.allowScrollToHiddenView = false;
               }

               return var3;
            } else {
               return false;
            }
         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }

         public void setAdapter(RecyclerView.Adapter var1) {
            super.setAdapter(var1);
            this.firstLayout = true;
         }
      };
      this.dialogsItemAnimator = new DialogsItemAnimator() {
         public void onAddFinished(RecyclerView.ViewHolder var1) {
            if (DialogsActivity.this.dialogInsertFinished == 2) {
               DialogsActivity.this.dialogInsertFinished = 1;
            }

         }

         protected void onAllAnimationsDone() {
            if (DialogsActivity.this.dialogRemoveFinished == 1 || DialogsActivity.this.dialogInsertFinished == 1 || DialogsActivity.this.dialogChangeFinished == 1) {
               DialogsActivity.this.onDialogAnimationFinished();
            }

         }

         public void onChangeFinished(RecyclerView.ViewHolder var1, boolean var2) {
            if (DialogsActivity.this.dialogChangeFinished == 2) {
               DialogsActivity.this.dialogChangeFinished = 1;
            }

         }

         public void onRemoveFinished(RecyclerView.ViewHolder var1) {
            if (DialogsActivity.this.dialogRemoveFinished == 2) {
               DialogsActivity.this.dialogRemoveFinished = 1;
            }

         }
      };
      this.listView.setItemAnimator(this.dialogsItemAnimator);
      this.listView.setVerticalScrollBarEnabled(true);
      this.listView.setInstantClick(true);
      this.listView.setTag(4);
      this.layoutManager = new LinearLayoutManager(var1) {
         public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
            int var4 = var1;
            if (DialogsActivity.this.listView.getAdapter() == DialogsActivity.this.dialogsAdapter) {
               var4 = var1;
               if (DialogsActivity.this.dialogsType == 0) {
                  var4 = var1;
                  if (!DialogsActivity.this.onlySelect) {
                     var4 = var1;
                     if (!DialogsActivity.this.allowScrollToHiddenView) {
                        var4 = var1;
                        if (DialogsActivity.this.folderId == 0) {
                           var4 = var1;
                           if (var1 < 0) {
                              var4 = var1;
                              if (DialogsActivity.this.getMessagesController().hasHiddenArchive()) {
                                 var4 = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                                 int var5 = var4;
                                 View var6;
                                 if (var4 == 0) {
                                    var6 = DialogsActivity.this.layoutManager.findViewByPosition(var4);
                                    var5 = var4;
                                    if (var6 != null) {
                                       var5 = var4;
                                       if (var6.getBottom() <= AndroidUtilities.dp(1.0F)) {
                                          var5 = 1;
                                       }
                                    }
                                 }

                                 var4 = var1;
                                 if (var5 != 0) {
                                    var4 = var1;
                                    if (var5 != -1) {
                                       var6 = DialogsActivity.this.layoutManager.findViewByPosition(var5);
                                       var4 = var1;
                                       if (var6 != null) {
                                          float var7;
                                          if (SharedConfig.useThreeLinesLayout) {
                                             var7 = 78.0F;
                                          } else {
                                             var7 = 72.0F;
                                          }

                                          var4 = AndroidUtilities.dp(var7);
                                          var5 = -var6.getTop() + (var5 - 1) * (var4 + 1);
                                          var4 = var1;
                                          if (var5 < Math.abs(var1)) {
                                             DialogsActivity var9 = DialogsActivity.this;
                                             var9.totalConsumedAmount = var9.totalConsumedAmount + Math.abs(var1);
                                             var1 = -var5;
                                             var4 = var1;
                                             if (DialogsActivity.this.startedScrollAtTop) {
                                                var4 = var1;
                                                if (DialogsActivity.this.totalConsumedAmount >= AndroidUtilities.dp(150.0F)) {
                                                   DialogsActivity.this.allowScrollToHiddenView = true;

                                                   try {
                                                      DialogsActivity.this.listView.performHapticFeedback(3, 2);
                                                   } catch (Exception var8) {
                                                      var4 = var1;
                                                      return super.scrollVerticallyBy(var4, var2, var3);
                                                   }

                                                   var4 = var1;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            return super.scrollVerticallyBy(var4, var2, var3);
         }

         public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
            if (DialogsActivity.this.hasHiddenArchive() && var3 == 1) {
               super.smoothScrollToPosition(var1, var2, var3);
            } else {
               LinearSmoothScrollerMiddle var4 = new LinearSmoothScrollerMiddle(var1.getContext());
               var4.setTargetPosition(var3);
               this.startSmoothScroll(var4);
            }

         }
      };
      this.layoutManager.setOrientation(1);
      this.listView.setLayoutManager(this.layoutManager);
      RecyclerListView var18 = this.listView;
      byte var28;
      if (LocaleController.isRTL) {
         var28 = 1;
      } else {
         var28 = 2;
      }

      var18.setVerticalScrollbarPosition(var28);
      var24.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$DialogsActivity$vVlyn12mo3e9YWraUH_TSyb3Khs(this)));
      this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() {
         public boolean onItemClick(View var1, int var2, float var3, float var4) {
            if (DialogsActivity.this.getParentActivity() == null) {
               return false;
            } else {
               if (!DialogsActivity.access$7400(DialogsActivity.this).isActionModeShowed() && !AndroidUtilities.isTablet() && !DialogsActivity.this.onlySelect && var1 instanceof DialogCell) {
                  DialogCell var5 = (DialogCell)var1;
                  if (var5.isPointInsideAvatar(var3, var4)) {
                     long var6 = var5.getDialogId();
                     Bundle var10 = new Bundle();
                     int var8 = (int)var6;
                     var2 = (int)(var6 >> 32);
                     int var9 = var5.getMessageId();
                     if (var8 != 0) {
                        if (var2 == 1) {
                           var10.putInt("chat_id", var8);
                        } else if (var8 > 0) {
                           var10.putInt("user_id", var8);
                        } else if (var8 < 0) {
                           var2 = var8;
                           if (var9 != 0) {
                              TLRPC.Chat var13 = DialogsActivity.this.getMessagesController().getChat(-var8);
                              var2 = var8;
                              if (var13 != null) {
                                 var2 = var8;
                                 if (var13.migrated_to != null) {
                                    var10.putInt("migrated_to", var8);
                                    var2 = -var13.migrated_to.channel_id;
                                 }
                              }
                           }

                           var10.putInt("chat_id", -var2);
                        }

                        if (var9 != 0) {
                           var10.putInt("message_id", var9);
                        }

                        if (DialogsActivity.this.searchString != null) {
                           if (DialogsActivity.this.getMessagesController().checkCanOpenChat(var10, DialogsActivity.this)) {
                              DialogsActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats);
                              DialogsActivity.this.presentFragmentAsPreview(new ChatActivity(var10));
                           }
                        } else if (DialogsActivity.this.getMessagesController().checkCanOpenChat(var10, DialogsActivity.this)) {
                           DialogsActivity.this.presentFragmentAsPreview(new ChatActivity(var10));
                        }

                        return true;
                     }

                     return false;
                  }
               }

               if (DialogsActivity.this.listView.getAdapter() == DialogsActivity.this.dialogsSearchAdapter) {
                  DialogsActivity.this.dialogsSearchAdapter.getItem(var2);
                  return false;
               } else {
                  ArrayList var11 = DialogsActivity.getDialogsArray(DialogsActivity.access$7800(DialogsActivity.this), DialogsActivity.this.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.dialogsListFrozen);
                  var2 = DialogsActivity.this.dialogsAdapter.fixPosition(var2);
                  if (var2 >= 0 && var2 < var11.size()) {
                     TLRPC.Dialog var12 = (TLRPC.Dialog)var11.get(var2);
                     if (DialogsActivity.this.onlySelect) {
                        if (DialogsActivity.this.dialogsType != 3 || DialogsActivity.this.selectAlertString != null) {
                           return false;
                        }

                        DialogsActivity.this.dialogsAdapter.addOrRemoveSelectedDialog(var12.id, var1);
                        DialogsActivity.this.updateSelectedCount();
                     } else {
                        if (var12 instanceof TLRPC.TL_dialogFolder) {
                           return false;
                        }

                        if (DialogsActivity.access$8100(DialogsActivity.this).isActionModeShowed() && var12.pinned) {
                           return false;
                        }

                        DialogsActivity.this.showOrUpdateActionMode(var12, var1);
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }

         public void onLongClickRelease() {
            DialogsActivity.this.finishPreviewFragment();
         }

         public void onMove(float var1, float var2) {
            DialogsActivity.this.movePreviewFragment(var2);
         }
      });
      this.swipeController = new DialogsActivity.SwipeController();
      this.itemTouchhelper = new ItemTouchHelper(this.swipeController);
      this.itemTouchhelper.attachToRecyclerView(this.listView);
      this.searchEmptyView = new EmptyTextProgressView(var1);
      this.searchEmptyView.setVisibility(8);
      this.searchEmptyView.setShowAtCenter(true);
      this.searchEmptyView.setTopImage(2131165816);
      this.searchEmptyView.setText(LocaleController.getString("SettingsNoResults", 2131560742));
      var24.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.progressView = new RadialProgressView(var1);
      this.progressView.setVisibility(8);
      var24.addView(this.progressView, LayoutHelper.createFrame(-2, -2, 17));
      this.floatingButtonContainer = new FrameLayout(var1);
      FrameLayout var20 = this.floatingButtonContainer;
      if (!this.onlySelect && this.folderId == 0) {
         var28 = 0;
      } else {
         var28 = 8;
      }

      var20.setVisibility(var28);
      var20 = this.floatingButtonContainer;
      if (VERSION.SDK_INT >= 21) {
         var28 = 56;
      } else {
         var28 = 60;
      }

      byte var6;
      if (VERSION.SDK_INT >= 21) {
         var6 = 56;
      } else {
         var6 = 60;
      }

      float var7 = (float)(var6 + 14);
      if (LocaleController.isRTL) {
         var6 = 3;
      } else {
         var6 = 5;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 4.0F;
      } else {
         var8 = 0.0F;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 0.0F;
      } else {
         var9 = 4.0F;
      }

      var24.addView(var20, LayoutHelper.createFrame(var28 + 20, var7, var6 | 80, var8, 0.0F, var9, 0.0F));
      this.floatingButtonContainer.setOnClickListener(new _$$Lambda$DialogsActivity$6X_tHzQFSQYoiAZxS6kgZUUhZCo(this));
      this.floatingButton = new ImageView(var1);
      this.floatingButton.setScaleType(ScaleType.CENTER);
      Drawable var21 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
      Object var25 = var21;
      if (VERSION.SDK_INT < 21) {
         Drawable var26 = var1.getResources().getDrawable(2131165387).mutate();
         var26.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var25 = new CombinedDrawable(var26, var21, 0, 0);
         ((CombinedDrawable)var25).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.floatingButton.setBackgroundDrawable((Drawable)var25);
      this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
      this.floatingButton.setImageResource(2131165386);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var31 = new StateListAnimator();
         ObjectAnimator var23 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var31.addState(new int[]{16842919}, var23);
         var23 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var31.addState(new int[0], var23);
         this.floatingButton.setStateListAnimator(var31);
         this.floatingButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      this.floatingButtonContainer.setContentDescription(LocaleController.getString("NewMessageTitle", 2131559901));
      var20 = this.floatingButtonContainer;
      ImageView var27 = this.floatingButton;
      if (VERSION.SDK_INT >= 21) {
         var28 = 56;
      } else {
         var28 = 60;
      }

      if (VERSION.SDK_INT >= 21) {
         var6 = 56;
      } else {
         var6 = 60;
      }

      var20.addView(var27, LayoutHelper.createFrame(var28, (float)var6, 51, 10.0F, 0.0F, 10.0F, 0.0F));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         // $FF: synthetic method
         public void lambda$onScrolled$0$DialogsActivity$8(boolean var1) {
            DialogsActivity.this.getMessagesController().loadDialogs(DialogsActivity.this.folderId, -1, 100, var1);
         }

         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               if (DialogsActivity.this.searching && DialogsActivity.this.searchWas) {
                  AndroidUtilities.hideKeyboard(DialogsActivity.this.getParentActivity().getCurrentFocus());
               }

               DialogsActivity.this.scrollingManually = true;
            } else {
               DialogsActivity.this.scrollingManually = false;
            }

            if (DialogsActivity.this.waitingForScrollFinished && var2 == 0) {
               DialogsActivity.this.waitingForScrollFinished = false;
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            int var4 = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
            var3 = Math.abs(DialogsActivity.this.layoutManager.findLastVisibleItemPosition() - var4) + 1;
            var2 = var1.getAdapter().getItemCount();
            if (DialogsActivity.this.searching && DialogsActivity.this.searchWas) {
               if (var3 > 0 && DialogsActivity.this.layoutManager.findLastVisibleItemPosition() == var2 - 1 && !DialogsActivity.this.dialogsSearchAdapter.isMessagesSearchEndReached()) {
                  DialogsActivity.this.dialogsSearchAdapter.loadMoreSearchMessages();
               }

            } else {
               boolean var5;
               if (var3 > 0 && DialogsActivity.this.layoutManager.findLastVisibleItemPosition() >= DialogsActivity.getDialogsArray(DialogsActivity.access$8400(DialogsActivity.this), DialogsActivity.this.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.dialogsListFrozen).size() - 10) {
                  var5 = DialogsActivity.this.getMessagesController().isDialogsEndReached(DialogsActivity.this.folderId) ^ true;
                  if (var5 || !DialogsActivity.this.getMessagesController().isServerDialogsEndReached(DialogsActivity.this.folderId)) {
                     AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$8$owKzhPjqms37BNI54Vcmj0_m32Y(this, var5));
                  }
               }

               if (DialogsActivity.this.floatingButtonContainer.getVisibility() != 8) {
                  boolean var9 = false;
                  View var8 = var1.getChildAt(0);
                  if (var8 != null) {
                     var2 = var8.getTop();
                  } else {
                     var2 = 0;
                  }

                  boolean var7;
                  label62: {
                     if (DialogsActivity.this.prevPosition == var4) {
                        int var6 = DialogsActivity.this.prevTop;
                        if (var2 < DialogsActivity.this.prevTop) {
                           var5 = true;
                        } else {
                           var5 = false;
                        }

                        var7 = var5;
                        if (Math.abs(var6 - var2) <= 1) {
                           break label62;
                        }
                     } else if (var4 > DialogsActivity.this.prevPosition) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }

                     var9 = true;
                     var7 = var5;
                  }

                  if (var9 && DialogsActivity.this.scrollUpdated && (var7 || !var7 && DialogsActivity.this.scrollingManually)) {
                     DialogsActivity.this.hideFloatingButton(var7);
                  }

                  DialogsActivity.this.prevPosition = var4;
                  DialogsActivity.this.prevTop = var2;
                  DialogsActivity.this.scrollUpdated = true;
               }

            }
         }
      });
      if (this.searchString == null) {
         this.dialogsAdapter = new DialogsAdapter(var1, this.dialogsType, this.folderId, this.onlySelect) {
            public void notifyDataSetChanged() {
               DialogsActivity.this.lastItemsCount = this.getItemCount();
               super.notifyDataSetChanged();
            }
         };
         if (AndroidUtilities.isTablet()) {
            long var10 = this.openedDialogId;
            if (var10 != 0L) {
               this.dialogsAdapter.setOpenedDialogId(var10);
            }
         }

         this.listView.setAdapter(this.dialogsAdapter);
      }

      if (this.searchString != null) {
         var28 = 2;
      } else if (!this.onlySelect) {
         var28 = 1;
      } else {
         var28 = 0;
      }

      this.dialogsSearchAdapter = new DialogsSearchAdapter(var1, var28, this.dialogsType);
      this.dialogsSearchAdapter.setDelegate(new DialogsSearchAdapter.DialogsSearchAdapterDelegate() {
         public void didPressedOnSubDialog(long var1) {
            if (DialogsActivity.this.onlySelect) {
               if (DialogsActivity.this.dialogsAdapter.hasSelectedDialogs()) {
                  DialogsActivity.this.dialogsAdapter.addOrRemoveSelectedDialog(var1, (View)null);
                  DialogsActivity.this.updateSelectedCount();
                  DialogsActivity.this.closeSearch();
               } else {
                  DialogsActivity.this.didSelectResult(var1, true, false);
               }
            } else {
               int var3 = (int)var1;
               Bundle var4 = new Bundle();
               if (var3 > 0) {
                  var4.putInt("user_id", var3);
               } else {
                  var4.putInt("chat_id", -var3);
               }

               DialogsActivity.this.closeSearch();
               if (AndroidUtilities.isTablet() && DialogsActivity.this.dialogsAdapter != null) {
                  DialogsAdapter var5 = DialogsActivity.this.dialogsAdapter;
                  DialogsActivity.this.openedDialogId = var1;
                  var5.setOpenedDialogId(var1);
                  DialogsActivity.this.updateVisibleRows(512);
               }

               if (DialogsActivity.this.searchString != null) {
                  if (DialogsActivity.this.getMessagesController().checkCanOpenChat(var4, DialogsActivity.this)) {
                     DialogsActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats);
                     DialogsActivity.this.presentFragment(new ChatActivity(var4));
                  }
               } else if (DialogsActivity.this.getMessagesController().checkCanOpenChat(var4, DialogsActivity.this)) {
                  DialogsActivity.this.presentFragment(new ChatActivity(var4));
               }
            }

         }

         // $FF: synthetic method
         public void lambda$needClearList$1$DialogsActivity$10(DialogInterface var1, int var2) {
            if (DialogsActivity.this.dialogsSearchAdapter.isRecentSearchDisplayed()) {
               DialogsActivity.this.dialogsSearchAdapter.clearRecentSearch();
            } else {
               DialogsActivity.this.dialogsSearchAdapter.clearRecentHashtags();
            }

         }

         // $FF: synthetic method
         public void lambda$needRemoveHint$0$DialogsActivity$10(int var1, DialogInterface var2, int var3) {
            DialogsActivity.this.getDataQuery().removePeer(var1);
         }

         public void needClearList() {
            AlertDialog.Builder var1 = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            var1.setTitle(LocaleController.getString("ClearSearchAlertTitle", 2131559116));
            var1.setMessage(LocaleController.getString("ClearSearchAlert", 2131559115));
            var1.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), new _$$Lambda$DialogsActivity$10$Q2pbWf5Q1Cqk_6Ep5ajOPUW2nyo(this));
            var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            AlertDialog var2 = var1.create();
            DialogsActivity.this.showDialog(var2);
            TextView var3 = (TextView)var2.getButton(-1);
            if (var3 != null) {
               var3.setTextColor(Theme.getColor("dialogTextRed2"));
            }

         }

         public void needRemoveHint(int var1) {
            if (DialogsActivity.this.getParentActivity() != null) {
               TLRPC.User var2 = DialogsActivity.this.getMessagesController().getUser(var1);
               if (var2 != null) {
                  AlertDialog.Builder var3 = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
                  var3.setTitle(LocaleController.getString("ChatHintsDeleteAlertTitle", 2131559032));
                  var3.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ChatHintsDeleteAlert", 2131559031, ContactsController.formatName(var2.first_name, var2.last_name))));
                  var3.setPositiveButton(LocaleController.getString("StickersRemove", 2131560811), new _$$Lambda$DialogsActivity$10$7JwjKBJfwtWw34NhDCOKWZDwXRQ(this, var1));
                  var3.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  AlertDialog var4 = var3.create();
                  DialogsActivity.this.showDialog(var4);
                  TextView var5 = (TextView)var4.getButton(-1);
                  if (var5 != null) {
                     var5.setTextColor(Theme.getColor("dialogTextRed2"));
                  }

               }
            }
         }

         public void searchStateChanged(boolean var1) {
            if (DialogsActivity.this.searching && DialogsActivity.this.searchWas && DialogsActivity.this.searchEmptyView != null) {
               if (var1) {
                  DialogsActivity.this.searchEmptyView.showProgress();
               } else {
                  DialogsActivity.this.searchEmptyView.showTextView();
               }
            }

         }
      });
      RecyclerListView var29 = this.listView;
      RadialProgressView var32;
      if (this.folderId == 0) {
         var32 = this.progressView;
      } else {
         var32 = null;
      }

      var29.setEmptyView(var32);
      String var33 = this.searchString;
      if (var33 != null) {
         super.actionBar.openSearchField(var33, false);
      }

      if (!this.onlySelect && this.dialogsType == 0) {
         FragmentContextView var35 = new FragmentContextView(var1, this, true);
         var24.addView(var35, LayoutHelper.createFrame(-1, 39.0F, 51, 0.0F, -36.0F, 0.0F, 0.0F));
         FragmentContextView var30 = new FragmentContextView(var1, this, false);
         var24.addView(var30, LayoutHelper.createFrame(-1, 39.0F, 51, 0.0F, -36.0F, 0.0F, 0.0F));
         var30.setAdditionalContextView(var35);
         var35.setAdditionalContextView(var30);
      } else if (this.dialogsType == 3 && this.selectAlertString == null) {
         ChatActivityEnterView var34 = this.commentView;
         if (var34 != null) {
            var34.onDestroy();
         }

         this.commentView = new ChatActivityEnterView(this.getParentActivity(), var24, (ChatActivity)null, false);
         this.commentView.setAllowStickersAndGifs(false, false);
         this.commentView.setForceShowSendButton(true, false);
         this.commentView.setVisibility(8);
         var24.addView(this.commentView, LayoutHelper.createFrame(-1, -2, 83));
         this.commentView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
            public void didPressedAttachButton() {
            }

            public void needChangeVideoPreviewState(int var1, float var2) {
            }

            public void needSendTyping() {
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
               if (DialogsActivity.this.delegate != null) {
                  ArrayList var2 = DialogsActivity.this.dialogsAdapter.getSelectedDialogs();
                  if (!var2.isEmpty()) {
                     DialogsActivity.this.delegate.didSelectDialogs(DialogsActivity.this, var2, var1, false);
                  }
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
      }

      for(var5 = 0; var5 < 2; ++var5) {
         this.undoView[var5] = new UndoView(var1) {
            protected boolean canUndo() {
               return DialogsActivity.this.dialogsItemAnimator.isRunning() ^ true;
            }

            public void setTranslationY(float var1) {
               super.setTranslationY(var1);
               if (this == DialogsActivity.this.undoView[0] && DialogsActivity.this.undoView[1].getVisibility() != 0) {
                  var1 = (float)(this.getMeasuredHeight() + AndroidUtilities.dp(8.0F)) - var1;
                  if (!DialogsActivity.this.floatingHidden) {
                     DialogsActivity.this.floatingButtonContainer.setTranslationY(DialogsActivity.this.floatingButtonContainer.getTranslationY() + DialogsActivity.this.additionalFloatingTranslation - var1);
                  }

                  DialogsActivity.this.additionalFloatingTranslation = var1;
               }

            }
         };
         var24.addView(this.undoView[var5], LayoutHelper.createFrame(-1, -2.0F, 83, 8.0F, 0.0F, 8.0F, 8.0F));
      }

      if (this.folderId != 0) {
         super.actionBar.setBackgroundColor(Theme.getColor("actionBarDefaultArchived"));
         this.listView.setGlowColor(Theme.getColor("actionBarDefaultArchived"));
         super.actionBar.setTitleColor(Theme.getColor("actionBarDefaultArchivedTitle"));
         super.actionBar.setItemsColor(Theme.getColor("actionBarDefaultArchivedIcon"), false);
         super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultArchivedSelector"), false);
         super.actionBar.setSearchTextColor(Theme.getColor("actionBarDefaultArchivedSearch"), false);
         super.actionBar.setSearchTextColor(Theme.getColor("actionBarDefaultSearchArchivedPlaceholder"), true);
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.dialogsNeedReload) {
         if (this.dialogsListFrozen) {
            return;
         }

         DialogsAdapter var4 = this.dialogsAdapter;
         if (var4 != null) {
            if (!var4.isDataSetChanged() && var3.length <= 0) {
               this.updateVisibleRows(2048);
            } else {
               this.dialogsAdapter.notifyDataSetChanged();
            }
         }

         RecyclerListView var16 = this.listView;
         if (var16 != null) {
            Exception var10000;
            label183: {
               DialogsAdapter var5;
               boolean var10001;
               RecyclerView.Adapter var18;
               try {
                  var18 = var16.getAdapter();
                  var5 = this.dialogsAdapter;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label183;
               }

               RadialProgressView var17 = null;
               if (var18 == var5) {
                  label198: {
                     RecyclerListView var20;
                     try {
                        this.searchEmptyView.setVisibility(8);
                        var20 = this.listView;
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label198;
                     }

                     try {
                        if (this.folderId == 0) {
                           var17 = this.progressView;
                        }
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label198;
                     }

                     try {
                        var20.setEmptyView(var17);
                        return;
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                     }
                  }
               } else {
                  label201: {
                     label177: {
                        try {
                           if (this.searching && this.searchWas) {
                              this.listView.setEmptyView(this.searchEmptyView);
                              break label177;
                           }
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label201;
                        }

                        try {
                           this.searchEmptyView.setVisibility(8);
                           this.listView.setEmptyView((View)null);
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label201;
                        }
                     }

                     try {
                        this.progressView.setVisibility(8);
                        return;
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                     }
                  }
               }
            }

            Exception var19 = var10000;
            FileLog.e((Throwable)var19);
         }
      } else if (var1 == NotificationCenter.emojiDidLoad) {
         this.updateVisibleRows(0);
      } else if (var1 == NotificationCenter.closeSearchByActiveAction) {
         ActionBar var21 = super.actionBar;
         if (var21 != null) {
            var21.closeSearchField();
         }
      } else if (var1 == NotificationCenter.proxySettingsChanged) {
         this.updateProxyButton(false);
      } else if (var1 == NotificationCenter.updateInterfaces) {
         this.updateVisibleRows((Integer)var3[0]);
      } else if (var1 == NotificationCenter.appDidLogout) {
         dialogsLoaded[super.currentAccount] = false;
      } else if (var1 == NotificationCenter.encryptedChatUpdated) {
         this.updateVisibleRows(0);
      } else {
         DialogsAdapter var23;
         if (var1 == NotificationCenter.contactsDidLoad) {
            if (this.dialogsListFrozen) {
               return;
            }

            if (this.dialogsType == 0 && this.getMessagesController().getDialogs(this.folderId).isEmpty()) {
               var23 = this.dialogsAdapter;
               if (var23 != null) {
                  var23.notifyDataSetChanged();
               }
            } else {
               this.updateVisibleRows(0);
            }
         } else {
            long var7;
            if (var1 == NotificationCenter.openedChatChanged) {
               if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                  boolean var6 = (Boolean)var3[1];
                  var7 = (Long)var3[0];
                  if (var6) {
                     if (var7 == this.openedDialogId) {
                        this.openedDialogId = 0L;
                     }
                  } else {
                     this.openedDialogId = var7;
                  }

                  var23 = this.dialogsAdapter;
                  if (var23 != null) {
                     var23.setOpenedDialogId(this.openedDialogId);
                  }

                  this.updateVisibleRows(512);
               }
            } else if (var1 == NotificationCenter.notificationsSettingsUpdated) {
               this.updateVisibleRows(0);
            } else if (var1 != NotificationCenter.messageReceivedByAck && var1 != NotificationCenter.messageReceivedByServer && var1 != NotificationCenter.messageSendError) {
               if (var1 == NotificationCenter.didSetPasscode) {
                  this.updatePasscodeButton();
               } else {
                  DialogsSearchAdapter var24;
                  if (var1 == NotificationCenter.needReloadRecentDialogsSearch) {
                     var24 = this.dialogsSearchAdapter;
                     if (var24 != null) {
                        var24.loadRecentSearch();
                     }
                  } else if (var1 == NotificationCenter.replyMessagesDidLoad) {
                     this.updateVisibleRows(32768);
                  } else if (var1 == NotificationCenter.reloadHints) {
                     var24 = this.dialogsSearchAdapter;
                     if (var24 != null) {
                        var24.notifyDataSetChanged();
                     }
                  } else if (var1 == NotificationCenter.didUpdateConnectionState) {
                     var1 = AccountInstance.getInstance(var2).getConnectionsManager().getConnectionState();
                     if (this.currentConnectionState != var1) {
                        this.currentConnectionState = var1;
                        this.updateProxyButton(true);
                     }
                  } else if (var1 != NotificationCenter.dialogsUnreadCounterChanged) {
                     if (var1 == NotificationCenter.needDeleteDialog) {
                        if (super.fragmentView == null) {
                           return;
                        }

                        var7 = (Long)var3[0];
                        TLRPC.User var22 = (TLRPC.User)var3[1];
                        _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g var25 = new _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g(this, (TLRPC.Chat)var3[2], var7, (Boolean)var3[3]);
                        if (this.undoView[0] != null) {
                           this.getUndoView().showWithAction(var7, 1, var25);
                        } else {
                           var25.run();
                        }
                     } else if (var1 == NotificationCenter.folderBecomeEmpty) {
                        var2 = (Integer)var3[0];
                        var1 = this.folderId;
                        if (var1 == var2 && var1 != 0) {
                           this.finishFragment();
                        }
                     }
                  }
               }
            } else {
               this.updateVisibleRows(4096);
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c var1 = new _$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c(this);
      ArrayList var2 = new ArrayList();
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      DialogCell var3 = this.movingView;
      if (var3 != null) {
         var2.add(new ThemeDescription(var3, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      }

      if (this.folderId == 0) {
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
         var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, new Drawable[]{Theme.dialogs_holidayDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder"));
      } else {
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchived"));
         var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchived"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchivedIcon"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, new Drawable[]{Theme.dialogs_holidayDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchivedTitle"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchivedSelector"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchivedSearch"));
         var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchArchivedPlaceholder"));
      }

      var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultIcon"));
      var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefault"));
      var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultTop"));
      var2.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultSelector"));
      var2.add(new ThemeDescription(this.selectedDialogsCountTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarActionModeDefaultIcon"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
      RecyclerListView var12 = this.listView;
      Paint var4 = Theme.dividerPaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{View.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      var2.add(new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"));
      var2.add(new ThemeDescription(this.searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView1"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameMessage_threeLines"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DialogsEmptyCell.class}, new String[]{"emptyTextView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_message"));
      var2.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionIcon"));
      var2.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground"));
      var2.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionPressedBackground"));
      RecyclerListView var14 = this.listView;
      Drawable var5 = Theme.avatar_broadcastDrawable;
      Drawable var13 = Theme.avatar_savedDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var5, var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundSaved"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundArchived"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundArchivedHidden"));
      var12 = this.listView;
      var4 = Theme.dialogs_countPaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounter"));
      var12 = this.listView;
      var4 = Theme.dialogs_countGrayPaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounterMuted"));
      var12 = this.listView;
      TextPaint var16 = Theme.dialogs_countTextPaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounterText"));
      RecyclerListView var17 = this.listView;
      var16 = Theme.dialogs_namePaint;
      TextPaint var15 = Theme.dialogs_searchNamePaint;
      var2.add(new ThemeDescription(var17, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[])null, new Paint[]{var16, var15}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_name"));
      var17 = this.listView;
      var16 = Theme.dialogs_nameEncryptedPaint;
      var15 = Theme.dialogs_searchNameEncryptedPaint;
      var2.add(new ThemeDescription(var17, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[])null, new Paint[]{var16, var15}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_secretName"));
      var14 = this.listView;
      var13 = Theme.dialogs_lockDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_secretIcon"));
      var14 = this.listView;
      var5 = Theme.dialogs_groupDrawable;
      Drawable var6 = Theme.dialogs_broadcastDrawable;
      var13 = Theme.dialogs_botDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var5, var6, var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameIcon"));
      var12 = this.listView;
      ScamDrawable var18 = Theme.dialogs_scamDrawable;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var18}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_draft"));
      var12 = this.listView;
      var5 = Theme.dialogs_pinnedDrawable;
      Drawable var19 = Theme.dialogs_reorderDrawable;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var5, var19}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_pinnedIcon"));
      if (SharedConfig.useThreeLinesLayout) {
         var12 = this.listView;
         var16 = Theme.dialogs_messagePaint;
         var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_message_threeLines"));
      } else {
         var12 = this.listView;
         var16 = Theme.dialogs_messagePaint;
         var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_message"));
      }

      var12 = this.listView;
      var16 = Theme.dialogs_messageNamePaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameMessage_threeLines"));
      var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_draft"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_nameMessage"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_draft"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_attachMessage"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_nameArchived"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_nameMessageArchived"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_nameMessageArchived_threeLines"));
      var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "chats_messageArchived"));
      var14 = this.listView;
      var15 = Theme.dialogs_messagePrintingPaint;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionMessage"));
      var14 = this.listView;
      var15 = Theme.dialogs_timePaint;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_date"));
      var14 = this.listView;
      Paint var20 = Theme.dialogs_pinnedPaint;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, var20, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_pinnedOverlay"));
      var14 = this.listView;
      var20 = Theme.dialogs_tabletSeletedPaint;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, var20, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_tabletSelectedOverlay"));
      var17 = this.listView;
      var19 = Theme.dialogs_checkDrawable;
      var13 = Theme.dialogs_halfCheckDrawable;
      var2.add(new ThemeDescription(var17, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var19, var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_sentCheck"));
      var14 = this.listView;
      var13 = Theme.dialogs_clockDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_sentClock"));
      var12 = this.listView;
      var4 = Theme.dialogs_errorPaint;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_sentError"));
      var14 = this.listView;
      var13 = Theme.dialogs_errorDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_sentErrorIcon"));
      var12 = this.listView;
      var19 = Theme.dialogs_verifiedCheckDrawable;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var19}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedCheck"));
      var14 = this.listView;
      var13 = Theme.dialogs_verifiedDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedBackground"));
      var12 = this.listView;
      var19 = Theme.dialogs_muteDrawable;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var19}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_muteIcon"));
      var14 = this.listView;
      var13 = Theme.dialogs_mentionDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_mentionIcon"));
      var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archivePinBackground"));
      var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveBackground"));
      LottieDrawable var22;
      LottieDrawable var23;
      if (SharedConfig.archiveHidden) {
         var12 = this.listView;
         var23 = Theme.dialogs_archiveAvatarDrawable;
         var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var23}, "Arrow1", "avatar_backgroundArchivedHidden"));
         var14 = this.listView;
         var22 = Theme.dialogs_archiveAvatarDrawable;
         var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow2", "avatar_backgroundArchivedHidden"));
      } else {
         var12 = this.listView;
         var23 = Theme.dialogs_archiveAvatarDrawable;
         var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var23}, "Arrow1", "avatar_backgroundArchived"));
         var14 = this.listView;
         var22 = Theme.dialogs_archiveAvatarDrawable;
         var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow2", "avatar_backgroundArchived"));
      }

      var12 = this.listView;
      var23 = Theme.dialogs_archiveAvatarDrawable;
      var2.add(new ThemeDescription(var12, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var23}, "Box2", "avatar_text"));
      var14 = this.listView;
      var22 = Theme.dialogs_archiveAvatarDrawable;
      var2.add(new ThemeDescription(var14, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Box1", "avatar_text"));
      var13 = Theme.dialogs_pinArchiveDrawable;
      if (var13 instanceof LottieDrawable) {
         var22 = (LottieDrawable)var13;
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow", "chats_archiveIcon"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Line", "chats_archiveIcon"));
      } else {
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveIcon"));
      }

      var13 = Theme.dialogs_unpinArchiveDrawable;
      if (var13 instanceof LottieDrawable) {
         var22 = (LottieDrawable)var13;
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow", "chats_archiveIcon"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Line", "chats_archiveIcon"));
      } else {
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveIcon"));
      }

      var13 = Theme.dialogs_archiveDrawable;
      if (var13 instanceof LottieDrawable) {
         var22 = (LottieDrawable)var13;
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow", "chats_archiveBackground"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Box2", "chats_archiveIcon"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Box1", "chats_archiveIcon"));
      } else {
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveIcon"));
      }

      var13 = Theme.dialogs_unarchiveDrawable;
      if (var13 instanceof LottieDrawable) {
         var22 = (LottieDrawable)var13;
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow1", "chats_archiveIcon"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Arrow2", "chats_archivePinBackground"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Box2", "chats_archiveIcon"));
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, new LottieDrawable[]{var22}, "Box1", "chats_archiveIcon"));
      } else {
         var2.add(new ThemeDescription(this.listView, 0, new Class[]{DialogCell.class}, (Paint)null, new Drawable[]{var13}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveIcon"));
      }

      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuBackground"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuName"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuPhone"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuPhoneCats"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuCloudBackgroundCats"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chat_serviceBackground"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuTopShadow"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuTopShadowCats"));
      RecyclerView var25 = this.sideMenu;
      int var7 = ThemeDescription.FLAG_CELLBACKGROUNDCOLOR;
      var2.add(new ThemeDescription(var25, ThemeDescription.FLAG_CHECKTAG | var7, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, var1, "chats_menuTopBackgroundCats"));
      var25 = this.sideMenu;
      var7 = ThemeDescription.FLAG_CELLBACKGROUNDCOLOR;
      var2.add(new ThemeDescription(var25, ThemeDescription.FLAG_CHECKTAG | var7, new Class[]{DrawerProfileCell.class}, (Paint)null, (Drawable[])null, var1, "chats_menuTopBackground"));
      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuItemIcon"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuItemText"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerUserCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuItemText"));
      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounterText"));
      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounter"));
      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuBackground"));
      var2.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuItemIcon"));
      var2.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_menuItemText"));
      RecyclerView var8 = this.sideMenu;
      var20 = Theme.dividerPaint;
      var2.add(new ThemeDescription(var8, 0, new Class[]{DividerCell.class}, var20, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"));
      var2.add(new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
      RecyclerListView var9 = this.listView;
      var15 = Theme.dialogs_offlinePaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{ProfileSearchCell.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"));
      var9 = this.listView;
      var15 = Theme.dialogs_onlinePaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{ProfileSearchCell.class}, var15, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText3"));
      var2.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{HashtagSearchCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      var2.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
      DialogsAdapter var10 = this.dialogsAdapter;
      var3 = null;
      ViewPager var11;
      if (var10 != null) {
         var11 = var10.getArchiveHintCellPager();
      } else {
         var11 = null;
      }

      var2.add(new ThemeDescription(var11, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameMessage_threeLines"));
      var10 = this.dialogsAdapter;
      if (var10 != null) {
         var11 = var10.getArchiveHintCellPager();
      } else {
         var11 = null;
      }

      var2.add(new ThemeDescription(var11, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounter"));
      var10 = this.dialogsAdapter;
      if (var10 != null) {
         var11 = var10.getArchiveHintCellPager();
      } else {
         var11 = null;
      }

      var2.add(new ThemeDescription(var11, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"headerTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameMessage_threeLines"));
      var10 = this.dialogsAdapter;
      if (var10 != null) {
         var11 = var10.getArchiveHintCellPager();
      } else {
         var11 = null;
      }

      var2.add(new ThemeDescription(var11, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"messageTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_message"));
      var10 = this.dialogsAdapter;
      if (var10 != null) {
         var11 = var10.getArchiveHintCellPager();
      } else {
         var11 = null;
      }

      var2.add(new ThemeDescription(var11, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultArchived"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
      var2.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
      DialogsSearchAdapter var21 = this.dialogsSearchAdapter;
      if (var21 != null) {
         var9 = var21.getInnerListView();
      } else {
         var9 = null;
      }

      var4 = Theme.dialogs_countPaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounter"));
      var21 = this.dialogsSearchAdapter;
      if (var21 != null) {
         var9 = var21.getInnerListView();
      } else {
         var9 = null;
      }

      var4 = Theme.dialogs_countGrayPaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, var4, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounterMuted"));
      var21 = this.dialogsSearchAdapter;
      if (var21 != null) {
         var9 = var21.getInnerListView();
      } else {
         var9 = null;
      }

      var16 = Theme.dialogs_countTextPaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_unreadCounterText"));
      var21 = this.dialogsSearchAdapter;
      if (var21 != null) {
         var9 = var21.getInnerListView();
      } else {
         var9 = null;
      }

      var16 = Theme.dialogs_archiveTextPaint;
      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, var16, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_archiveText"));
      var21 = this.dialogsSearchAdapter;
      if (var21 != null) {
         var9 = var21.getInnerListView();
      } else {
         var9 = null;
      }

      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
      DialogsSearchAdapter var24 = this.dialogsSearchAdapter;
      var9 = var3;
      if (var24 != null) {
         var9 = var24.getInnerListView();
      }

      var2.add(new ThemeDescription(var9, 0, new Class[]{HintDialogCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_onlineCircle"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerBackground"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerPlayPause"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerTitle"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerPerformer"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerClose"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "returnToCallBackground"));
      var2.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "returnToCallText"));
      var7 = 0;

      while(true) {
         UndoView[] var26 = this.undoView;
         if (var7 >= var26.length) {
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogBackgroundGray"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlack"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextLink"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogLinkSelection"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlue"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlue2"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlue3"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlue4"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextRed"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextRed2"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextGray"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextGray2"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextGray3"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextGray4"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogIcon"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRedIcon"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextHint"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogInputField"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogInputFieldActivated"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogCheckboxSquareBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogCheckboxSquareCheck"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogCheckboxSquareUnchecked"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogCheckboxSquareDisabled"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRadioBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRadioBackgroundChecked"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogProgressCircle"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogButton"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogButtonSelector"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogScrollGlow"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRoundCheckBox"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRoundCheckBoxCheck"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogBadgeBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogBadgeText"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogLineProgress"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogLineProgressBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogGrayLine"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialog_inlineProgressBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialog_inlineProgress"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogSearchBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogSearchHint"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogSearchIcon"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogSearchText"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogFloatingButton"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogFloatingIcon"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogShadowLine"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_sheet_scrollUp"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_sheet_other"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBar"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarSelector"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarTitle"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarTop"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarSubtitle"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarItems"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_background"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_time"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progressBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_player_progressCachedBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_progress"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_placeholder"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_placeholderBackground"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_button"));
            var2.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_buttonActive"));
            return (ThemeDescription[])var2.toArray(new ThemeDescription[0]);
         }

         var2.add(new ThemeDescription(var26[var7], ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_background"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_cancelColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_cancelColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"subinfoTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info1", "undo_background"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info2", "undo_background"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc12", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc11", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc10", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc9", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc8", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc7", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc6", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc5", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc4", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc3", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc2", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc1", "undo_infoColor"));
         var2.add(new ThemeDescription(this.undoView[var7], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Oval", "undo_infoColor"));
         ++var7;
      }
   }

   public boolean isMainDialogList() {
      boolean var1;
      if (this.delegate == null && this.searchString == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$askForPermissons$16$DialogsActivity(int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.askAboutContacts = var2;
      MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).commit();
      this.askForPermissons(false);
   }

   // $FF: synthetic method
   public void lambda$createView$1$DialogsActivity() {
      this.hideFloatingButton(false);
      this.listView.smoothScrollToPosition(this.hasHiddenArchive());
   }

   // $FF: synthetic method
   public void lambda$createView$3$DialogsActivity(View var1, int var2) {
      TLRPC.TL_recentMeUrlChatInvite var7;
      TLRPC.ChatInvite var23;
      label200: {
         RecyclerListView var3 = this.listView;
         if (var3 != null && var3.getAdapter() != null && this.getParentActivity() != null) {
            long var5;
            boolean var12;
            Bundle var17;
            RecyclerView.Adapter var18;
            TLObject var21;
            TLRPC.Chat var25;
            label185: {
               label184: {
                  var18 = this.listView.getAdapter();
                  DialogsAdapter var4 = this.dialogsAdapter;
                  if (var18 == var4) {
                     label182: {
                        var21 = var4.getItem(var2);
                        if (var21 instanceof TLRPC.User) {
                           var2 = ((TLRPC.User)var21).id;
                        } else {
                           label192: {
                              if (var21 instanceof TLRPC.Dialog) {
                                 TLRPC.Dialog var26 = (TLRPC.Dialog)var21;
                                 if (var26 instanceof TLRPC.TL_dialogFolder) {
                                    if (super.actionBar.isActionModeShowed()) {
                                       return;
                                    }

                                    TLRPC.TL_dialogFolder var22 = (TLRPC.TL_dialogFolder)var26;
                                    var17 = new Bundle();
                                    var17.putInt("folderId", var22.folder.id);
                                    this.presentFragment(new DialogsActivity(var17));
                                    return;
                                 }

                                 var5 = var26.id;
                                 if (super.actionBar.isActionModeShowed()) {
                                    this.showOrUpdateActionMode(var26, var1);
                                    return;
                                 }
                                 break label182;
                              }

                              if (var21 instanceof TLRPC.TL_recentMeUrlChat) {
                                 var2 = ((TLRPC.TL_recentMeUrlChat)var21).chat_id;
                              } else {
                                 if (var21 instanceof TLRPC.TL_recentMeUrlUser) {
                                    var2 = ((TLRPC.TL_recentMeUrlUser)var21).user_id;
                                    break label192;
                                 }

                                 if (!(var21 instanceof TLRPC.TL_recentMeUrlChatInvite)) {
                                    if (var21 instanceof TLRPC.TL_recentMeUrlStickerSet) {
                                       TLRPC.StickerSet var16 = ((TLRPC.TL_recentMeUrlStickerSet)var21).set.set;
                                       TLRPC.TL_inputStickerSetID var20 = new TLRPC.TL_inputStickerSetID();
                                       var20.id = var16.id;
                                       var20.access_hash = var16.access_hash;
                                       this.showDialog(new StickersAlert(this.getParentActivity(), this, var20, (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null));
                                       return;
                                    }

                                    if (var21 instanceof TLRPC.TL_recentMeUrlUnknown) {
                                    }

                                    return;
                                 }

                                 var7 = (TLRPC.TL_recentMeUrlChatInvite)var21;
                                 var23 = var7.chat_invite;
                                 if (var23.chat == null && (!var23.channel || var23.megagroup)) {
                                    break label200;
                                 }

                                 TLRPC.Chat var8 = var23.chat;
                                 if (var8 != null && (!ChatObject.isChannel(var8) || var23.chat.megagroup)) {
                                    break label200;
                                 }

                                 var25 = var23.chat;
                                 if (var25 == null) {
                                    return;
                                 }

                                 var2 = var25.id;
                              }

                              var2 = -var2;
                           }
                        }

                        var5 = (long)var2;
                     }
                  } else {
                     DialogsSearchAdapter var27 = this.dialogsSearchAdapter;
                     if (var18 == var27) {
                        Object var28 = var27.getItem(var2);
                        boolean var9 = this.dialogsSearchAdapter.isGlobalSearch(var2);
                        long var10;
                        if (var28 instanceof TLRPC.User) {
                           TLRPC.User var29 = (TLRPC.User)var28;
                           var10 = (long)var29.id;
                           var5 = var10;
                           var12 = var9;
                           if (!this.onlySelect) {
                              this.searchDialogId = var10;
                              this.searchObject = var29;
                              var5 = var10;
                              var12 = var9;
                           }
                        } else if (var28 instanceof TLRPC.Chat) {
                           var25 = (TLRPC.Chat)var28;
                           var2 = var25.id;
                           if (var2 > 0) {
                              var10 = (long)(-var2);
                           } else {
                              var10 = AndroidUtilities.makeBroadcastId(var2);
                           }

                           var5 = var10;
                           var12 = var9;
                           if (!this.onlySelect) {
                              this.searchDialogId = var10;
                              this.searchObject = var25;
                              var5 = var10;
                              var12 = var9;
                           }
                        } else if (var28 instanceof TLRPC.EncryptedChat) {
                           TLRPC.EncryptedChat var30 = (TLRPC.EncryptedChat)var28;
                           var10 = (long)var30.id << 32;
                           var5 = var10;
                           var12 = var9;
                           if (!this.onlySelect) {
                              this.searchDialogId = var10;
                              this.searchObject = var30;
                              var5 = var10;
                              var12 = var9;
                           }
                        } else {
                           if (var28 instanceof MessageObject) {
                              MessageObject var31 = (MessageObject)var28;
                              var5 = var31.getDialogId();
                              var2 = var31.getId();
                              var27 = this.dialogsSearchAdapter;
                              var27.addHashtagsFromMessage(var27.getLastSearchString());
                              var12 = var9;
                              break label185;
                           }

                           if (var28 instanceof String) {
                              super.actionBar.openSearchField((String)var28, false);
                           }

                           var5 = 0L;
                           var12 = var9;
                        }
                        break label184;
                     }

                     var5 = 0L;
                  }

                  var12 = false;
               }

               var2 = 0;
            }

            if (var5 == 0L) {
               return;
            }

            if (this.onlySelect) {
               if (this.dialogsAdapter.hasSelectedDialogs()) {
                  this.dialogsAdapter.addOrRemoveSelectedDialog(var5, var1);
                  this.updateSelectedCount();
               } else {
                  this.didSelectResult(var5, true, false);
               }
            } else {
               var17 = new Bundle();
               int var13 = (int)var5;
               int var14 = (int)(var5 >> 32);
               if (var13 != 0) {
                  if (var14 == 1) {
                     var17.putInt("chat_id", var13);
                  } else if (var13 > 0) {
                     var17.putInt("user_id", var13);
                  } else if (var13 < 0) {
                     var14 = var13;
                     if (var2 != 0) {
                        var25 = this.getMessagesController().getChat(-var13);
                        var14 = var13;
                        if (var25 != null) {
                           var14 = var13;
                           if (var25.migrated_to != null) {
                              var17.putInt("migrated_to", var13);
                              var14 = -var25.migrated_to.channel_id;
                           }
                        }
                     }

                     var17.putInt("chat_id", -var14);
                  }
               } else {
                  var17.putInt("enc_id", var14);
               }

               if (var2 != 0) {
                  var17.putInt("message_id", var2);
               } else if (!var12) {
                  this.closeSearch();
               } else {
                  var21 = this.searchObject;
                  if (var21 != null) {
                     this.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, var21);
                     this.searchObject = null;
                  }
               }

               if (AndroidUtilities.isTablet()) {
                  if (this.openedDialogId == var5 && var18 != this.dialogsSearchAdapter) {
                     return;
                  }

                  DialogsAdapter var24 = this.dialogsAdapter;
                  if (var24 != null) {
                     this.openedDialogId = var5;
                     var24.setOpenedDialogId(var5);
                     this.updateVisibleRows(512);
                  }
               }

               if (this.searchString != null) {
                  if (this.getMessagesController().checkCanOpenChat(var17, this)) {
                     this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats);
                     this.presentFragment(new ChatActivity(var17));
                  }
               } else if (this.getMessagesController().checkCanOpenChat(var17, this)) {
                  this.presentFragment(new ChatActivity(var17));
               }
            }
         }

         return;
      }

      String var19 = var7.url;
      var2 = var19.indexOf(47);
      String var15 = var19;
      if (var2 > 0) {
         var15 = var19.substring(var2 + 1);
      }

      this.showDialog(new JoinGroupAlert(this.getParentActivity(), var23, var15, this));
   }

   // $FF: synthetic method
   public void lambda$createView$4$DialogsActivity(View var1) {
      Bundle var2 = new Bundle();
      var2.putBoolean("destroyAfterSelect", true);
      this.presentFragment(new ContactsActivity(var2));
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$17$DialogsActivity(TLRPC.Chat var1, long var2, boolean var4) {
      if (var1 != null) {
         if (ChatObject.isNotInChat(var1)) {
            this.getMessagesController().deleteDialog(var2, 0, var4);
         } else {
            this.getMessagesController().deleteUserFromChat((int)(-var2), this.getMessagesController().getUser(this.getUserConfig().getClientUserId()), (TLRPC.ChatFull)null, false, var4);
         }
      } else {
         this.getMessagesController().deleteDialog(var2, 0, var4);
      }

   }

   // $FF: synthetic method
   public void lambda$didSelectResult$18$DialogsActivity(long var1, DialogInterface var3, int var4) {
      this.didSelectResult(var1, false, false);
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$19$DialogsActivity() {
      RecyclerListView var1 = this.listView;
      int var2;
      int var3;
      View var5;
      if (var1 != null) {
         var2 = var1.getChildCount();

         for(var3 = 0; var3 < var2; ++var3) {
            var5 = this.listView.getChildAt(var3);
            if (var5 instanceof ProfileSearchCell) {
               ((ProfileSearchCell)var5).update(0);
            } else if (var5 instanceof DialogCell) {
               ((DialogCell)var5).update(0);
            }
         }
      }

      DialogsSearchAdapter var6 = this.dialogsSearchAdapter;
      if (var6 != null) {
         RecyclerListView var4 = var6.getInnerListView();
         if (var4 != null) {
            var2 = var4.getChildCount();

            for(var3 = 0; var3 < var2; ++var3) {
               var5 = var4.getChildAt(var3);
               if (var5 instanceof HintDialogCell) {
                  ((HintDialogCell)var5).update();
               }
            }
         }
      }

      RecyclerView var7 = this.sideMenu;
      if (var7 != null) {
         var5 = var7.getChildAt(0);
         if (var5 instanceof DrawerProfileCell) {
            ((DrawerProfileCell)var5).applyBackground();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$13$DialogsActivity(int var1, long var2, boolean var4, TLRPC.Chat var5, boolean var6) {
      if (var1 == 103) {
         this.getMessagesController().deleteDialog(var2, 1, var4);
      } else {
         if (var5 != null) {
            if (ChatObject.isNotInChat(var5)) {
               this.getMessagesController().deleteDialog(var2, 0, var4);
            } else {
               TLRPC.User var7 = this.getMessagesController().getUser(this.getUserConfig().getClientUserId());
               this.getMessagesController().deleteUserFromChat((int)(-var2), var7, (TLRPC.ChatFull)null);
            }
         } else {
            this.getMessagesController().deleteDialog(var2, 0, var4);
            if (var6) {
               this.getMessagesController().blockUser((int)var2);
            }
         }

         if (AndroidUtilities.isTablet()) {
            this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, var2);
         }

         MessagesController.getInstance(super.currentAccount).checkIfFolderEmpty(this.folderId);
      }

   }

   // $FF: synthetic method
   public void lambda$onDialogAnimationFinished$8$DialogsActivity() {
      if (this.folderId != 0 && frozenDialogsList.isEmpty()) {
         this.listView.setEmptyView((View)null);
         this.progressView.setVisibility(4);
         this.finishFragment();
      }

      this.setDialogsListFrozen(false);
      this.updateDialogIndices();
   }

   // $FF: synthetic method
   public void lambda$onResume$5$DialogsActivity(int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.askAboutContacts = var2;
      MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).commit();
      this.askForPermissons(false);
   }

   // $FF: synthetic method
   public void lambda$onResume$6$DialogsActivity(DialogInterface var1, int var2) {
      Intent var6 = XiaomiUtilities.getPermissionManagerIntent();
      if (var6 != null) {
         try {
            this.getParentActivity().startActivity(var6);
         } catch (Exception var5) {
            try {
               var6 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
               StringBuilder var3 = new StringBuilder();
               var3.append("package:");
               var3.append(ApplicationLoader.applicationContext.getPackageName());
               var6.setData(Uri.parse(var3.toString()));
               this.getParentActivity().startActivity(var6);
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$10$DialogsActivity(int var1, DialogInterface var2, int var3) {
      this.getMessagesController().setDialogsInTransaction(true);
      this.perfromSelectedDialogsAction(var1, false);
      this.getMessagesController().setDialogsInTransaction(false);
      MessagesController.getInstance(super.currentAccount).checkIfFolderEmpty(this.folderId);
      var1 = this.folderId;
      if (var1 != 0 && getDialogsArray(super.currentAccount, this.dialogsType, var1, false).size() == 0) {
         this.listView.setEmptyView((View)null);
         this.progressView.setVisibility(4);
         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$11$DialogsActivity(int var1, DialogInterface var2, int var3) {
      this.perfromSelectedDialogsAction(var1, false);
   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$12$DialogsActivity(int var1, DialogInterface var2, int var3) {
      this.perfromSelectedDialogsAction(var1, false);
   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$14$DialogsActivity(int var1, TLRPC.Chat var2, long var3, boolean var5, boolean var6) {
      this.hideActionMode(false);
      if (var1 != 103 || !ChatObject.isChannel(var2) || var2.megagroup && TextUtils.isEmpty(var2.username)) {
         if (var1 == 102) {
            int var7 = this.folderId;
            if (var7 != 0 && getDialogsArray(super.currentAccount, this.dialogsType, var7, false).size() == 1) {
               this.progressView.setVisibility(4);
            }
         }

         UndoView var8 = this.getUndoView();
         byte var9;
         if (var1 == 103) {
            var9 = 0;
         } else {
            var9 = 1;
         }

         var8.showWithAction(var3, var9, new _$$Lambda$DialogsActivity$txFO_N8hhOxXsqlooRmKgc2zyA8(this, var1, var3, var6, var2, var5));
      } else {
         this.getMessagesController().deleteDialog(var3, 2, var6);
      }

   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$15$DialogsActivity(DialogInterface var1) {
      this.hideActionMode(true);
   }

   // $FF: synthetic method
   public void lambda$perfromSelectedDialogsAction$9$DialogsActivity(ArrayList var1) {
      MessagesController var2 = this.getMessagesController();
      byte var3;
      if (this.folderId == 0) {
         var3 = 0;
      } else {
         var3 = 1;
      }

      var2.addDialogToFolder(var1, var3, -1, (ArrayList)null, 0L);
   }

   public boolean onBackPressed() {
      ActionBar var1 = super.actionBar;
      if (var1 != null && var1.isActionModeShowed()) {
         this.hideActionMode(true);
         return false;
      } else {
         ChatActivityEnterView var2 = this.commentView;
         if (var2 != null && var2.isPopupShowing()) {
            this.commentView.hidePopup(true);
            return false;
         } else {
            return super.onBackPressed();
         }
      }
   }

   protected void onBecomeFullyHidden() {
      if (this.closeSearchFieldOnHide) {
         ActionBar var1 = super.actionBar;
         if (var1 != null) {
            var1.closeSearchField();
         }

         TLObject var2 = this.searchObject;
         if (var2 != null) {
            this.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, var2);
            this.searchObject = null;
         }

         this.closeSearchFieldOnHide = false;
      }

      UndoView[] var3 = this.undoView;
      if (var3[0] != null) {
         var3[0].hide(true, 0);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if (!this.onlySelect) {
         FrameLayout var2 = this.floatingButtonContainer;
         if (var2 != null) {
            var2.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
               public void onGlobalLayout() {
                  FrameLayout var1 = DialogsActivity.this.floatingButtonContainer;
                  float var2;
                  if (DialogsActivity.this.floatingHidden) {
                     var2 = (float)AndroidUtilities.dp(100.0F);
                  } else {
                     var2 = -DialogsActivity.this.additionalFloatingTranslation;
                  }

                  var1.setTranslationY(var2);
                  DialogsActivity.this.floatingButtonContainer.setClickable(DialogsActivity.this.floatingHidden ^ true);
                  if (DialogsActivity.this.floatingButtonContainer != null) {
                     DialogsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  }

               }
            });
         }
      }

   }

   protected void onDialogDismiss(Dialog var1) {
      super.onDialogDismiss(var1);
      AlertDialog var2 = this.permissionDialog;
      if (var2 != null && var1 == var2 && this.getParentActivity() != null) {
         this.askForPermissons(false);
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      if (this.getArguments() != null) {
         this.onlySelect = super.arguments.getBoolean("onlySelect", false);
         this.cantSendToChannels = super.arguments.getBoolean("cantSendToChannels", false);
         this.dialogsType = super.arguments.getInt("dialogsType", 0);
         this.selectAlertString = super.arguments.getString("selectAlertString");
         this.selectAlertStringGroup = super.arguments.getString("selectAlertStringGroup");
         this.addToGroupAlertString = super.arguments.getString("addToGroupAlertString");
         this.allowSwitchAccount = super.arguments.getBoolean("allowSwitchAccount");
         this.checkCanWrite = super.arguments.getBoolean("checkCanWrite", true);
         this.folderId = super.arguments.getInt("folderId", 0);
      }

      if (this.dialogsType == 0) {
         this.askAboutContacts = MessagesController.getGlobalNotificationsSettings().getBoolean("askAboutContacts", true);
         SharedConfig.loadProxyList();
      }

      if (this.searchString == null) {
         this.currentConnectionState = this.getConnectionsManager().getConnectionState();
         this.getNotificationCenter().addObserver(this, NotificationCenter.dialogsNeedReload);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
         if (!this.onlySelect) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeSearchByActiveAction);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
         }

         this.getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
         this.getNotificationCenter().addObserver(this, NotificationCenter.encryptedChatUpdated);
         this.getNotificationCenter().addObserver(this, NotificationCenter.contactsDidLoad);
         this.getNotificationCenter().addObserver(this, NotificationCenter.appDidLogout);
         this.getNotificationCenter().addObserver(this, NotificationCenter.openedChatChanged);
         this.getNotificationCenter().addObserver(this, NotificationCenter.notificationsSettingsUpdated);
         this.getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByAck);
         this.getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByServer);
         this.getNotificationCenter().addObserver(this, NotificationCenter.messageSendError);
         this.getNotificationCenter().addObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
         this.getNotificationCenter().addObserver(this, NotificationCenter.replyMessagesDidLoad);
         this.getNotificationCenter().addObserver(this, NotificationCenter.reloadHints);
         this.getNotificationCenter().addObserver(this, NotificationCenter.didUpdateConnectionState);
         this.getNotificationCenter().addObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
         this.getNotificationCenter().addObserver(this, NotificationCenter.needDeleteDialog);
         this.getNotificationCenter().addObserver(this, NotificationCenter.folderBecomeEmpty);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
      }

      if (!dialogsLoaded[super.currentAccount]) {
         this.getMessagesController().loadGlobalNotificationsSettings();
         this.getMessagesController().loadDialogs(this.folderId, 0, 100, true);
         this.getMessagesController().loadHintDialogs();
         this.getContactsController().checkInviteText();
         this.getDataQuery().loadRecents(2, false, true, false);
         this.getDataQuery().checkFeaturedStickers();
         dialogsLoaded[super.currentAccount] = true;
      }

      this.getMessagesController().loadPinnedDialogs(this.folderId, 0L, (ArrayList)null);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      if (this.searchString == null) {
         this.getNotificationCenter().removeObserver(this, NotificationCenter.dialogsNeedReload);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
         if (!this.onlySelect) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeSearchByActiveAction);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
         }

         this.getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.encryptedChatUpdated);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.contactsDidLoad);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.appDidLogout);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.openedChatChanged);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByAck);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByServer);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.messageSendError);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.replyMessagesDidLoad);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.reloadHints);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.didUpdateConnectionState);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.needDeleteDialog);
         this.getNotificationCenter().removeObserver(this, NotificationCenter.folderBecomeEmpty);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
      }

      ChatActivityEnterView var1 = this.commentView;
      if (var1 != null) {
         var1.onDestroy();
      }

      UndoView[] var2 = this.undoView;
      if (var2[0] != null) {
         var2[0].hide(true, 0);
      }

      this.delegate = null;
   }

   public void onPause() {
      super.onPause();
      ChatActivityEnterView var1 = this.commentView;
      if (var1 != null) {
         var1.onResume();
      }

      UndoView[] var2 = this.undoView;
      if (var2[0] != null) {
         var2[0].hide(true, 0);
      }

   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 1) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var3.length > var4) {
               String var5 = var2[var4];
               byte var7 = -1;
               int var6 = var5.hashCode();
               if (var6 != 1365911975) {
                  if (var6 == 1977429404 && var5.equals("android.permission.READ_CONTACTS")) {
                     var7 = 0;
                  }
               } else if (var5.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                  var7 = 1;
               }

               if (var7 != 0) {
                  if (var7 == 1 && var3[var4] == 0) {
                     ImageLoader.getInstance().checkMediaPaths();
                  }
               } else if (var3[var4] == 0) {
                  this.getContactsController().forceImportContacts();
               } else {
                  Editor var8 = MessagesController.getGlobalNotificationsSettings().edit();
                  this.askAboutContacts = false;
                  var8.putBoolean("askAboutContacts", false).commit();
               }
            }
         }
      }

   }

   public void onResume() {
      super.onResume();
      DialogsAdapter var1 = this.dialogsAdapter;
      if (var1 != null && !this.dialogsListFrozen) {
         var1.notifyDataSetChanged();
      }

      ChatActivityEnterView var5 = this.commentView;
      if (var5 != null) {
         var5.onResume();
      }

      DialogsSearchAdapter var6 = this.dialogsSearchAdapter;
      if (var6 != null) {
         var6.notifyDataSetChanged();
      }

      boolean var2 = this.checkPermission;
      boolean var3 = false;
      if (var2 && !this.onlySelect && VERSION.SDK_INT >= 23) {
         Activity var7 = this.getParentActivity();
         if (var7 != null) {
            this.checkPermission = false;
            boolean var4;
            if (var7.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (var7.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
               var3 = true;
            }

            if (var4 || var3) {
               AlertDialog var9;
               if (var4 && this.askAboutContacts && this.getUserConfig().syncContacts && var7.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                  var9 = AlertsCreator.createContactsPermissionDialog(var7, new _$$Lambda$DialogsActivity$h_XfCIY8uwUxVhs5pPRQujZay_s(this)).create();
                  this.permissionDialog = var9;
                  this.showDialog(var9);
               } else if (var3 && var7.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                  AlertDialog.Builder var8 = new AlertDialog.Builder(var7);
                  var8.setTitle(LocaleController.getString("AppName", 2131558635));
                  var8.setMessage(LocaleController.getString("PermissionStorage", 2131560420));
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var9 = var8.create();
                  this.permissionDialog = var9;
                  this.showDialog(var9);
               } else {
                  this.askForPermissons(true);
               }
            }
         }
      } else if (!this.onlySelect && XiaomiUtilities.isMIUI() && VERSION.SDK_INT >= 19 && !XiaomiUtilities.isCustomPermissionGranted(10020)) {
         if (this.getParentActivity() == null) {
            return;
         }

         if (MessagesController.getGlobalNotificationsSettings().getBoolean("askedAboutMiuiLockscreen", false)) {
            return;
         }

         this.showDialog((new AlertDialog.Builder(this.getParentActivity())).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionXiaomiLockscreen", 2131560421)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$DialogsActivity$ad_mqRrDwcRXwvhesRJKLOMcnHI(this)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131559152), _$$Lambda$DialogsActivity$om5eIuKoD_TUjWJtmdVNYsc_Woc.INSTANCE).create());
      }

   }

   public void setDelegate(DialogsActivity.DialogsActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setSearchString(String var1) {
      this.searchString = var1;
   }

   public void setSideMenu(RecyclerView var1) {
      this.sideMenu = var1;
      this.sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
      this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
   }

   private class ContentView extends SizeNotifierFrameLayout {
      private int inputFieldHeight;

      public ContentView(Context var2) {
         super(var2);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         int var2 = var1.getActionMasked();
         boolean var3 = true;
         if (var2 == 0 || var2 == 1 || var2 == 3) {
            if (var2 == 0) {
               var2 = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
               DialogsActivity var4 = DialogsActivity.this;
               if (var2 > 1) {
                  var3 = false;
               }

               var4.startedScrollAtTop = var3;
            } else if (DialogsActivity.access$800(DialogsActivity.this).isActionModeShowed()) {
               DialogsActivity.this.allowMoving = true;
            }

            DialogsActivity.this.totalConsumedAmount = 0;
            DialogsActivity.this.allowScrollToHiddenView = false;
         }

         return super.onInterceptTouchEvent(var1);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         int var6 = this.getChildCount();
         Object var7;
         if (DialogsActivity.this.commentView != null) {
            var7 = DialogsActivity.this.commentView.getTag();
         } else {
            var7 = null;
         }

         int var8 = 0;
         int var9;
         if (var7 != null && var7.equals(2) && this.getKeyboardHeight() <= AndroidUtilities.dp(20.0F) && !AndroidUtilities.isInMultiwindow) {
            var9 = DialogsActivity.this.commentView.getEmojiPadding();
         } else {
            var9 = 0;
         }

         this.setBottomClip(var9);

         for(; var8 < var6; ++var8) {
            View var10 = this.getChildAt(var8);
            if (var10.getVisibility() != 8) {
               LayoutParams var17 = (LayoutParams)var10.getLayoutParams();
               int var11 = var10.getMeasuredWidth();
               int var12 = var10.getMeasuredHeight();
               int var13 = var17.gravity;
               int var14 = var13;
               if (var13 == -1) {
                  var14 = 51;
               }

               int var15;
               int var16;
               label64: {
                  var15 = var14 & 112;
                  var14 = var14 & 7 & 7;
                  if (var14 != 1) {
                     if (var14 != 5) {
                        var16 = var17.leftMargin;
                        break label64;
                     }

                     var13 = var4 - var11;
                     var14 = var17.rightMargin;
                  } else {
                     var13 = (var4 - var2 - var11) / 2 + var17.leftMargin;
                     var14 = var17.rightMargin;
                  }

                  var16 = var13 - var14;
               }

               label58: {
                  if (var15 != 16) {
                     if (var15 == 48) {
                        var14 = var17.topMargin + this.getPaddingTop();
                        break label58;
                     }

                     if (var15 != 80) {
                        var14 = var17.topMargin;
                        break label58;
                     }

                     var14 = var5 - var9 - var3 - var12;
                     var13 = var17.bottomMargin;
                  } else {
                     var14 = (var5 - var9 - var3 - var12) / 2 + var17.topMargin;
                     var13 = var17.bottomMargin;
                  }

                  var14 -= var13;
               }

               var13 = var14;
               if (DialogsActivity.this.commentView != null) {
                  var13 = var14;
                  if (DialogsActivity.this.commentView.isPopupView(var10)) {
                     if (AndroidUtilities.isInMultiwindow) {
                        var13 = DialogsActivity.this.commentView.getTop() - var10.getMeasuredHeight() + AndroidUtilities.dp(1.0F);
                     } else {
                        var13 = DialogsActivity.this.commentView.getBottom();
                     }
                  }
               }

               var10.layout(var16, var13, var11 + var16, var12 + var13);
            }
         }

         this.notifyHeightChanged();
      }

      protected void onMeasure(int var1, int var2) {
         int var3 = MeasureSpec.getSize(var1);
         int var4 = MeasureSpec.getSize(var2);
         this.setMeasuredDimension(var3, var4);
         int var5 = var4 - this.getPaddingTop();
         this.measureChildWithMargins(DialogsActivity.access$000(DialogsActivity.this), var1, 0, var2, 0);
         int var6 = this.getKeyboardHeight();
         int var7 = this.getChildCount();
         ChatActivityEnterView var8 = DialogsActivity.this.commentView;
         byte var9 = 0;
         var4 = var5;
         int var10 = var9;
         if (var8 != null) {
            this.measureChildWithMargins(DialogsActivity.this.commentView, var1, 0, var2, 0);
            Object var11 = DialogsActivity.this.commentView.getTag();
            if (var11 != null && var11.equals(2)) {
               var4 = var5;
               if (var6 <= AndroidUtilities.dp(20.0F)) {
                  var4 = var5;
                  if (!AndroidUtilities.isInMultiwindow) {
                     var4 = var5 - DialogsActivity.this.commentView.getEmojiPadding();
                  }
               }

               this.inputFieldHeight = DialogsActivity.this.commentView.getMeasuredHeight();
               var10 = var9;
            } else {
               this.inputFieldHeight = 0;
               var10 = var9;
               var4 = var5;
            }
         }

         for(; var10 < var7; ++var10) {
            View var12 = this.getChildAt(var10);
            if (var12 != null && var12.getVisibility() != 8 && var12 != DialogsActivity.this.commentView && var12 != DialogsActivity.access$200(DialogsActivity.this)) {
               if (var12 != DialogsActivity.this.listView && var12 != DialogsActivity.this.progressView && var12 != DialogsActivity.this.searchEmptyView) {
                  if (DialogsActivity.this.commentView != null && DialogsActivity.this.commentView.isPopupView(var12)) {
                     if (AndroidUtilities.isInMultiwindow) {
                        if (AndroidUtilities.isTablet()) {
                           var12.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0F), var4 - this.inputFieldHeight - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                        } else {
                           var12.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4 - this.inputFieldHeight - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                        }
                     } else {
                        var12.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var12.getLayoutParams().height, 1073741824));
                     }
                  } else {
                     this.measureChildWithMargins(var12, var1, 0, var2, 0);
                  }
               } else {
                  var12.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0F), var4 - this.inputFieldHeight + AndroidUtilities.dp(2.0F)), 1073741824));
               }
            }
         }

      }
   }

   public interface DialogsActivityDelegate {
      void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4);
   }

   class SwipeController extends ItemTouchHelper.Callback {
      private RectF buttonInstance;
      private RecyclerView.ViewHolder currentItemViewHolder;
      private boolean swipeFolderBack;
      private boolean swipingFolder;

      // $FF: synthetic method
      static void lambda$getAnimationDuration$2(View var0) {
         var0.setBackgroundDrawable((Drawable)null);
      }

      public int convertToAbsoluteDirection(int var1, int var2) {
         return this.swipeFolderBack ? 0 : super.convertToAbsoluteDirection(var1, var2);
      }

      public long getAnimationDuration(RecyclerView var1, int var2, float var3, float var4) {
         if (var2 == 4) {
            return 200L;
         } else {
            if (var2 == 8 && DialogsActivity.this.movingView != null) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$SwipeController$P5v0ux1s9L8VHZMGfvGmr58IqXA(DialogsActivity.this.movingView), DialogsActivity.this.dialogsItemAnimator.getMoveDuration());
               DialogsActivity.this.movingView = null;
            }

            return super.getAnimationDuration(var1, var2, var3, var4);
         }
      }

      public int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2) {
         if (!DialogsActivity.this.waitingForDialogsAnimationEnd() && (DialogsActivity.access$1300(DialogsActivity.this) == null || !DialogsActivity.access$1400(DialogsActivity.this).isInPreviewMode())) {
            if (this.swipingFolder && this.swipeFolderBack) {
               this.swipingFolder = false;
               return 0;
            }

            if (!DialogsActivity.this.onlySelect && DialogsActivity.this.dialogsType == 0 && DialogsActivity.this.slidingView == null && var1.getAdapter() == DialogsActivity.this.dialogsAdapter) {
               View var6 = var2.itemView;
               if (var6 instanceof DialogCell) {
                  DialogCell var7 = (DialogCell)var6;
                  long var3 = var7.getDialogId();
                  if (DialogsActivity.access$1900(DialogsActivity.this).isActionModeShowed()) {
                     TLRPC.Dialog var8 = (TLRPC.Dialog)DialogsActivity.this.getMessagesController().dialogs_dict.get(var3);
                     if (DialogsActivity.this.allowMoving && var8 != null && var8.pinned && !DialogObject.isFolderDialogId(var3)) {
                        DialogsActivity.this.movingView = (DialogCell)var2.itemView;
                        DialogsActivity.this.movingView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
                     }

                     return 0;
                  }

                  if (DialogsActivity.this.allowSwipeDuringCurrentTouch && var3 != (long)DialogsActivity.this.getUserConfig().clientUserId && var3 != 777000L && !DialogsActivity.this.getMessagesController().isProxyDialog(var3, false)) {
                     this.swipeFolderBack = false;
                     boolean var5;
                     if (SharedConfig.archiveHidden && DialogObject.isFolderDialogId(var7.getDialogId())) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }

                     this.swipingFolder = var5;
                     var7.setSliding(true);
                     return ItemTouchHelper.Callback.makeMovementFlags(0, 4);
                  }
               }
            }
         }

         return 0;
      }

      public float getSwipeEscapeVelocity(float var1) {
         return 3500.0F;
      }

      public float getSwipeThreshold(RecyclerView.ViewHolder var1) {
         return 0.3F;
      }

      public float getSwipeVelocityThreshold(float var1) {
         return Float.MAX_VALUE;
      }

      // $FF: synthetic method
      public void lambda$null$0$DialogsActivity$SwipeController(TLRPC.Dialog var1, int var2) {
         DialogsActivity.this.dialogsListFrozen = true;
         DialogsActivity.this.getMessagesController().addDialogToFolder(var1.id, 0, var2, 0L);
         DialogsActivity.this.dialogsListFrozen = false;
         ArrayList var3 = DialogsActivity.this.getMessagesController().getDialogs(0);
         var2 = var3.indexOf(var1);
         if (var2 >= 0) {
            ArrayList var4 = DialogsActivity.this.getMessagesController().getDialogs(1);
            if (!var4.isEmpty() || var2 != 1) {
               DialogsActivity.this.dialogInsertFinished = 2;
               DialogsActivity.this.setDialogsListFrozen(true);
               DialogsActivity.this.dialogsItemAnimator.prepareForRemove();
               DialogsActivity.access$3508(DialogsActivity.this);
               DialogsActivity.this.dialogsAdapter.notifyItemInserted(var2);
            }

            if (var4.isEmpty()) {
               var3.remove(0);
               if (var2 == 1) {
                  DialogsActivity.this.dialogChangeFinished = 2;
                  DialogsActivity.this.setDialogsListFrozen(true);
                  DialogsActivity.this.dialogsAdapter.notifyItemChanged(0);
               } else {
                  DialogsActivity.frozenDialogsList.remove(0);
                  DialogsActivity.this.dialogsItemAnimator.prepareForRemove();
                  DialogsActivity.access$3510(DialogsActivity.this);
                  DialogsActivity.this.dialogsAdapter.notifyItemRemoved(0);
               }
            }
         } else {
            DialogsActivity.this.dialogsAdapter.notifyDataSetChanged();
         }

      }

      // $FF: synthetic method
      public void lambda$onSwiped$1$DialogsActivity$SwipeController(int var1, int var2, int var3) {
         TLRPC.Dialog var4 = (TLRPC.Dialog)DialogsActivity.frozenDialogsList.remove(var1);
         int var5 = var4.pinnedNum;
         DialogsActivity.this.slidingView = null;
         DialogsActivity.this.listView.invalidate();
         MessagesController var6 = DialogsActivity.this.getMessagesController();
         long var7 = var4.id;
         byte var10;
         if (DialogsActivity.this.folderId == 0) {
            var10 = 1;
         } else {
            var10 = 0;
         }

         var1 = var6.addDialogToFolder(var7, var10, -1, 0L);
         if (var1 == 2) {
            DialogsActivity.this.dialogsAdapter.notifyItemChanged(var2 - 1);
         }

         if (var1 != 2 || var3 != 0) {
            DialogsActivity.this.dialogsItemAnimator.prepareForRemove();
            DialogsActivity.access$3510(DialogsActivity.this);
            DialogsActivity.this.dialogsAdapter.notifyItemRemoved(var3);
            DialogsActivity.this.dialogRemoveFinished = 2;
         }

         if (DialogsActivity.this.folderId == 0) {
            if (var1 == 2) {
               DialogsActivity.this.dialogsItemAnimator.prepareForRemove();
               if (var3 == 0) {
                  DialogsActivity.this.dialogChangeFinished = 2;
                  DialogsActivity.this.setDialogsListFrozen(true);
                  DialogsActivity.this.dialogsAdapter.notifyItemChanged(0);
               } else {
                  DialogsActivity.access$3508(DialogsActivity.this);
                  DialogsActivity.this.dialogsAdapter.notifyItemInserted(0);
               }

               ArrayList var11 = DialogsActivity.getDialogsArray(DialogsActivity.access$3800(DialogsActivity.this), DialogsActivity.this.dialogsType, DialogsActivity.this.folderId, false);
               DialogsActivity.frozenDialogsList.add(0, var11.get(0));
            } else if (var1 == 1) {
               RecyclerView.ViewHolder var12 = DialogsActivity.this.listView.findViewHolderForAdapterPosition(0);
               if (var12 != null) {
                  View var13 = var12.itemView;
                  if (var13 instanceof DialogCell) {
                     DialogCell var14 = (DialogCell)var13;
                     var14.checkCurrentDialogIndex(true);
                     var14.animateArchiveAvatar();
                  }
               }
            }

            SharedPreferences var15 = MessagesController.getGlobalMainSettings();
            boolean var9 = var15.getBoolean("archivehint_l", false);
            var15.edit().putBoolean("archivehint_l", true).commit();
            UndoView var16 = DialogsActivity.this.getUndoView();
            var7 = var4.id;
            if (var9) {
               var10 = 2;
            } else {
               var10 = 3;
            }

            var16.showWithAction(var7, var10, (Runnable)null, new _$$Lambda$DialogsActivity$SwipeController$YQ1mguBHuXIRhAK21aamsJthsAQ(this, var4, var5));
         }

         if (DialogsActivity.this.folderId != 0 && DialogsActivity.frozenDialogsList.isEmpty()) {
            DialogsActivity.this.listView.setEmptyView((View)null);
            DialogsActivity.this.progressView.setVisibility(4);
         }

      }

      public boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3) {
         View var8 = var3.itemView;
         if (!(var8 instanceof DialogCell)) {
            return false;
         } else {
            long var4 = ((DialogCell)var8).getDialogId();
            TLRPC.Dialog var9 = (TLRPC.Dialog)DialogsActivity.this.getMessagesController().dialogs_dict.get(var4);
            if (var9 != null && var9.pinned && !DialogObject.isFolderDialogId(var4)) {
               int var6 = var2.getAdapterPosition();
               int var7 = var3.getAdapterPosition();
               DialogsActivity.this.dialogsAdapter.notifyItemMoved(var6, var7);
               DialogsActivity.this.updateDialogIndices();
               DialogsActivity.this.movingWas = true;
               return true;
            } else {
               return false;
            }
         }
      }

      public void onSelectedChanged(RecyclerView.ViewHolder var1, int var2) {
         if (var1 != null) {
            DialogsActivity.this.listView.hideSelector();
         }

         super.onSelectedChanged(var1, var2);
      }

      public void onSwiped(RecyclerView.ViewHolder var1, int var2) {
         if (var1 != null) {
            DialogCell var3 = (DialogCell)var1.itemView;
            if (DialogObject.isFolderDialogId(var3.getDialogId())) {
               SharedConfig.toggleArchiveHidden();
               if (SharedConfig.archiveHidden) {
                  DialogsActivity.this.waitingForScrollFinished = true;
                  DialogsActivity.this.listView.smoothScrollBy(0, var3.getMeasuredHeight() + var3.getTop(), CubicBezierInterpolator.EASE_OUT);
                  DialogsActivity.this.getUndoView().showWithAction(0L, 6, (Runnable)null, (Runnable)null);
               }

               return;
            }

            DialogsActivity.this.slidingView = var3;
            var2 = var1.getAdapterPosition();
            _$$Lambda$DialogsActivity$SwipeController$p1WvioWe4QIXpl_h9JruXCBarGg var4 = new _$$Lambda$DialogsActivity$SwipeController$p1WvioWe4QIXpl_h9JruXCBarGg(this, DialogsActivity.this.dialogsAdapter.fixPosition(var2), DialogsActivity.this.dialogsAdapter.getItemCount(), var2);
            DialogsActivity.this.setDialogsListFrozen(true);
            if (Utilities.random.nextInt(1000) == 1) {
               if (DialogsActivity.this.pacmanAnimation == null) {
                  DialogsActivity var5 = DialogsActivity.this;
                  var5.pacmanAnimation = new PacmanAnimation(var5.listView);
               }

               DialogsActivity.this.pacmanAnimation.setFinishRunnable(var4);
               DialogsActivity.this.pacmanAnimation.start();
            } else {
               var4.run();
            }
         } else {
            DialogsActivity.this.slidingView = null;
         }

      }
   }
}
