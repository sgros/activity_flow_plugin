// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.Utilities;
import android.graphics.RectF;
import android.view.View$MeasureSpec;
import android.widget.FrameLayout$LayoutParams;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.ImageLoader;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.content.res.Configuration;
import android.net.Uri;
import android.content.Intent;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.ArchiveHintInnerCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import com.airbnb.lottie.LottieDrawable;
import org.telegram.ui.Cells.DialogsEmptyCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.messenger.ContactsController;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.os.Build$VERSION;
import androidx.recyclerview.widget.LinearSmoothScrollerMiddle;
import android.view.animation.Interpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.graphics.Canvas;
import android.view.View$OnTouchListener;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.messenger.ImageLocation;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.BuildVars;
import android.widget.EditText;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.UserCell;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.SharedConfig;
import android.text.TextUtils;
import org.telegram.messenger.DialogObject;
import android.content.SharedPreferences;
import android.content.DialogInterface$OnDismissListener;
import android.widget.TextView;
import android.os.Vibrator;
import java.util.Collection;
import android.content.DialogInterface;
import android.view.MotionEvent;
import org.telegram.ui.ActionBar.Theme;
import android.util.Property;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.UserObject;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.AndroidUtilities;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.os.Bundle;
import org.telegram.ui.Components.UndoView;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.ProxyDrawable;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.PacmanAnimation;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Components.DialogsItemAnimator;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.Components.AnimatedArrowDrawable;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class DialogsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int archive = 105;
    private static final int clear = 103;
    private static final int delete = 102;
    public static boolean[] dialogsLoaded;
    private static ArrayList<TLRPC.Dialog> frozenDialogsList;
    private static final int mute = 104;
    private static final int pin = 100;
    private static final int read = 101;
    private ArrayList<View> actionModeViews;
    private String addToGroupAlertString;
    private float additionalFloatingTranslation;
    private boolean allowMoving;
    private boolean allowScrollToHiddenView;
    private boolean allowSwipeDuringCurrentTouch;
    private boolean allowSwitchAccount;
    private ActionBarMenuItem archiveItem;
    private AnimatedArrowDrawable arrowDrawable;
    private boolean askAboutContacts;
    private BackDrawable backDrawable;
    private int canClearCacheCount;
    private int canMuteCount;
    private int canPinCount;
    private int canReadCount;
    private int canUnmuteCount;
    private boolean cantSendToChannels;
    private boolean checkCanWrite;
    private boolean checkPermission;
    private ActionBarMenuSubItem clearItem;
    private boolean closeSearchFieldOnHide;
    private ChatActivityEnterView commentView;
    private int currentConnectionState;
    private DialogsActivityDelegate delegate;
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
    private final AccelerateDecelerateInterpolator floatingInterpolator;
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
    private SwipeController swipeController;
    private ActionBarMenuItem switchItem;
    private int totalConsumedAmount;
    private UndoView[] undoView;
    private boolean waitingForScrollFinished;
    
    static {
        DialogsActivity.dialogsLoaded = new boolean[3];
    }
    
    public DialogsActivity(final Bundle bundle) {
        super(bundle);
        this.undoView = new UndoView[2];
        this.actionModeViews = new ArrayList<View>();
        this.askAboutContacts = true;
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.checkPermission = true;
    }
    
    @TargetApi(23)
    private void askForPermissons(final boolean b) {
        final Activity parentActivity = this.getParentActivity();
        if (parentActivity == null) {
            return;
        }
        final ArrayList<String> list = new ArrayList<String>();
        if (this.getUserConfig().syncContacts && this.askAboutContacts && parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            if (b) {
                this.showDialog(this.permissionDialog = AlertsCreator.createContactsPermissionDialog(parentActivity, new _$$Lambda$DialogsActivity$0uSHLkwmlB9mVpQMUrJbrhf__rI(this)).create());
                return;
            }
            list.add("android.permission.READ_CONTACTS");
            list.add("android.permission.WRITE_CONTACTS");
            list.add("android.permission.GET_ACCOUNTS");
        }
        if (parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            list.add("android.permission.READ_EXTERNAL_STORAGE");
            list.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (list.isEmpty()) {
            return;
        }
        final String[] array = list.toArray(new String[0]);
        try {
            parentActivity.requestPermissions(array, 1);
        }
        catch (Exception ex) {}
    }
    
    private void closeSearch() {
        if (AndroidUtilities.isTablet()) {
            final ActionBar actionBar = super.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            final TLObject searchObject = this.searchObject;
            if (searchObject != null) {
                this.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, searchObject);
                this.searchObject = null;
            }
        }
        else {
            this.closeSearchFieldOnHide = true;
        }
    }
    
    private void didSelectResult(final long l, final boolean b, final boolean b2) {
        if (this.addToGroupAlertString == null && this.checkCanWrite) {
            final int n = (int)l;
            if (n < 0) {
                final MessagesController messagesController = this.getMessagesController();
                final int i = -n;
                final TLRPC.Chat chat = messagesController.getChat(i);
                if (ChatObject.isChannel(chat) && !chat.megagroup && (this.cantSendToChannels || !ChatObject.isCanWriteToChannel(i, super.currentAccount))) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("ChannelCantSendMessage", 2131558940));
                    builder.setNegativeButton(LocaleController.getString("OK", 2131560097), null);
                    this.showDialog(builder.create());
                    return;
                }
            }
        }
        if (b && ((this.selectAlertString != null && this.selectAlertStringGroup != null) || this.addToGroupAlertString != null)) {
            if (this.getParentActivity() == null) {
                return;
            }
            final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)this.getParentActivity());
            builder2.setTitle(LocaleController.getString("AppName", 2131558635));
            final int n2 = (int)l;
            final int j = (int)(l >> 32);
            if (n2 != 0) {
                if (j == 1) {
                    final TLRPC.Chat chat2 = this.getMessagesController().getChat(n2);
                    if (chat2 == null) {
                        return;
                    }
                    builder2.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, chat2.title));
                }
                else if (n2 == this.getUserConfig().getClientUserId()) {
                    builder2.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, LocaleController.getString("SavedMessages", 2131560633)));
                }
                else if (n2 > 0) {
                    final TLRPC.User user = this.getMessagesController().getUser(n2);
                    if (user == null) {
                        return;
                    }
                    builder2.setMessage(LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user)));
                }
                else if (n2 < 0) {
                    final TLRPC.Chat chat3 = this.getMessagesController().getChat(-n2);
                    if (chat3 == null) {
                        return;
                    }
                    final String addToGroupAlertString = this.addToGroupAlertString;
                    if (addToGroupAlertString != null) {
                        builder2.setMessage(LocaleController.formatStringSimple(addToGroupAlertString, chat3.title));
                    }
                    else {
                        builder2.setMessage(LocaleController.formatStringSimple(this.selectAlertStringGroup, chat3.title));
                    }
                }
            }
            else {
                final TLRPC.User user2 = this.getMessagesController().getUser(this.getMessagesController().getEncryptedChat(j).user_id);
                if (user2 == null) {
                    return;
                }
                builder2.setMessage(LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user2)));
            }
            builder2.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$aS99ZGtwHsaaGFpbpdjdMEuMtKw(this, l));
            builder2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            this.showDialog(builder2.create());
        }
        else if (this.delegate != null) {
            final ArrayList<Long> list = new ArrayList<Long>();
            list.add(l);
            this.delegate.didSelectDialogs(this, list, null, b2);
            this.delegate = null;
        }
        else {
            this.finishFragment();
        }
    }
    
    public static ArrayList<TLRPC.Dialog> getDialogsArray(final int n, final int n2, final int n3, final boolean b) {
        if (b) {
            final ArrayList<TLRPC.Dialog> frozenDialogsList = DialogsActivity.frozenDialogsList;
            if (frozenDialogsList != null) {
                return frozenDialogsList;
            }
        }
        final MessagesController messagesController = AccountInstance.getInstance(n).getMessagesController();
        if (n2 == 0) {
            return messagesController.getDialogs(n3);
        }
        if (n2 == 1) {
            return messagesController.dialogsServerOnly;
        }
        if (n2 == 2) {
            return messagesController.dialogsCanAddUsers;
        }
        if (n2 == 3) {
            return messagesController.dialogsForward;
        }
        if (n2 == 4) {
            return messagesController.dialogsUsersOnly;
        }
        if (n2 == 5) {
            return messagesController.dialogsChannelsOnly;
        }
        if (n2 == 6) {
            return messagesController.dialogsGroupsOnly;
        }
        return null;
    }
    
    private int getPinnedCount() {
        final ArrayList<TLRPC.Dialog> dialogs = this.getMessagesController().getDialogs(this.folderId);
        final int size = dialogs.size();
        int i = 0;
        int n = 0;
        while (i < size) {
            final TLRPC.Dialog dialog = dialogs.get(i);
            if (!(dialog instanceof TLRPC.TL_dialogFolder)) {
                final long id = dialog.id;
                if (!dialog.pinned) {
                    break;
                }
                ++n;
            }
            ++i;
        }
        return n;
    }
    
    private UndoView getUndoView() {
        if (this.undoView[0].getVisibility() == 0) {
            final UndoView[] undoView = this.undoView;
            final UndoView undoView2 = undoView[0];
            undoView[0] = undoView[1];
            (undoView[1] = undoView2).hide(true, 2);
            final ContentView contentView = (ContentView)super.fragmentView;
            contentView.removeView((View)this.undoView[0]);
            contentView.addView((View)this.undoView[0]);
        }
        return this.undoView[0];
    }
    
    private boolean hasHiddenArchive() {
        return this.listView.getAdapter() == this.dialogsAdapter && !this.onlySelect && this.dialogsType == 0 && this.folderId == 0 && this.getMessagesController().hasHiddenArchive();
    }
    
    private void hideActionMode(final boolean b) {
        super.actionBar.hideActionMode();
        if (this.menuDrawable != null) {
            super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
        }
        this.dialogsAdapter.getSelectedDialogs().clear();
        final MenuDrawable menuDrawable = this.menuDrawable;
        if (menuDrawable != null) {
            menuDrawable.setRotation(0.0f, true);
        }
        else {
            final BackDrawable backDrawable = this.backDrawable;
            if (backDrawable != null) {
                backDrawable.setRotation(0.0f, true);
            }
        }
        int n = 0;
        this.allowMoving = false;
        if (this.movingWas) {
            this.getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
            this.movingWas = false;
        }
        this.updateCounters(true);
        this.dialogsAdapter.onReorderStateChanged(false);
        if (b) {
            n = 8192;
        }
        this.updateVisibleRows(n | 0x30000);
    }
    
    private void hideFloatingButton(final boolean floatingHidden) {
        if (this.floatingHidden == floatingHidden) {
            return;
        }
        this.floatingHidden = floatingHidden;
        final AnimatorSet set = new AnimatorSet();
        final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
        final Property translation_Y = View.TRANSLATION_Y;
        float n;
        if (this.floatingHidden) {
            n = (float)AndroidUtilities.dp(100.0f);
        }
        else {
            n = -this.additionalFloatingTranslation;
        }
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)floatingButtonContainer, translation_Y, new float[] { n }) });
        set.setDuration(300L);
        set.setInterpolator((TimeInterpolator)this.floatingInterpolator);
        this.floatingButtonContainer.setClickable(floatingHidden ^ true);
        set.start();
    }
    
    private void onDialogAnimationFinished() {
        this.dialogRemoveFinished = 0;
        this.dialogInsertFinished = 0;
        this.dialogChangeFinished = 0;
        AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$M7BILTEWS_s5OccwAnpFNVy9dGs(this));
    }
    
    private void perfromSelectedDialogsAction(int n, final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final ArrayList<Long> selectedDialogs = this.dialogsAdapter.getSelectedDialogs();
        final int size = selectedDialogs.size();
        if (n == 105) {
            final ArrayList list = new ArrayList<Long>(selectedDialogs);
            final MessagesController messagesController = this.getMessagesController();
            if (this.folderId == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            messagesController.addDialogToFolder((ArrayList<Long>)list, n, -1, null, 0L);
            this.hideActionMode(false);
            if (this.folderId == 0) {
                final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                final boolean boolean1 = globalMainSettings.getBoolean("archivehint_l", false);
                globalMainSettings.edit().putBoolean("archivehint_l", true).commit();
                if (boolean1) {
                    if (list.size() > 1) {
                        n = 4;
                    }
                    else {
                        n = 2;
                    }
                }
                else if (list.size() > 1) {
                    n = 5;
                }
                else {
                    n = 3;
                }
                this.getUndoView().showWithAction(0L, n, null, new _$$Lambda$DialogsActivity$0CqBdEwjzLcyLDVjAtjOQrlvawc(this, list));
            }
            else if (this.getMessagesController().getDialogs(this.folderId).isEmpty()) {
                this.listView.setEmptyView(null);
                this.progressView.setVisibility(4);
                this.finishFragment();
            }
            return;
        }
        if (n == 100 && this.canPinCount != 0) {
            final ArrayList<TLRPC.Dialog> dialogs = this.getMessagesController().getDialogs(this.folderId);
            final int size2 = dialogs.size();
            int i = 0;
            int n2 = 0;
            int n3 = 0;
            while (i < size2) {
                final TLRPC.Dialog dialog = dialogs.get(i);
                if (!(dialog instanceof TLRPC.TL_dialogFolder)) {
                    final int n4 = (int)dialog.id;
                    if (!dialog.pinned) {
                        break;
                    }
                    if (n4 == 0) {
                        ++n3;
                    }
                    else {
                        ++n2;
                    }
                }
                ++i;
            }
            int j = 0;
            int n5 = 0;
            int n6 = 0;
            while (j < size) {
                final long longValue = selectedDialogs.get(j);
                final TLRPC.Dialog dialog2 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(longValue);
                int n7 = n5;
                int n8 = n6;
                if (dialog2 != null) {
                    if (dialog2.pinned) {
                        n7 = n5;
                        n8 = n6;
                    }
                    else if ((int)longValue == 0) {
                        n7 = n5 + 1;
                        n8 = n6;
                    }
                    else {
                        n8 = n6 + 1;
                        n7 = n5;
                    }
                }
                ++j;
                n5 = n7;
                n6 = n8;
            }
            int n9;
            if (this.folderId != 0) {
                n9 = this.getMessagesController().maxFolderPinnedDialogsCount;
            }
            else {
                n9 = this.getMessagesController().maxPinnedDialogsCount;
            }
            if (n5 + n3 > n9 || n6 + n2 > n9) {
                AlertsCreator.showSimpleToast(this, LocaleController.formatString("PinToTopLimitReached", 2131560448, LocaleController.formatPluralString("Chats", n9)));
                AndroidUtilities.shakeView((View)this.pinItem, 2.0f, 0);
                final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
                if (vibrator != null) {
                    vibrator.vibrate(200L);
                }
                return;
            }
        }
        else if ((n == 102 || n == 103) && size > 1 && b && b) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            if (n == 102) {
                builder.setTitle(LocaleController.formatString("DeleteFewChatsTitle", 2131559242, LocaleController.formatPluralString("ChatsSelected", size)));
                builder.setMessage(LocaleController.getString("AreYouSureDeleteFewChats", 2131558681));
                builder.setPositiveButton(LocaleController.getString("Delete", 2131559227), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$jWd3WTqe_JWytlcg5OAtRHiocr4(this, n));
            }
            else if (this.canClearCacheCount != 0) {
                builder.setTitle(LocaleController.formatString("ClearCacheFewChatsTitle", 2131559104, LocaleController.formatPluralString("ChatsSelectedClearCache", size)));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryCacheFewChats", 2131558670));
                builder.setPositiveButton(LocaleController.getString("ClearHistoryCache", 2131559108), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$iMQ7bU7WAaE9d3J2u7XQeCZi3Ow(this, n));
            }
            else {
                builder.setTitle(LocaleController.formatString("ClearFewChatsTitle", 2131559106, LocaleController.formatPluralString("ChatsSelectedClear", size)));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryFewChats", 2131558672));
                builder.setPositiveButton(LocaleController.getString("ClearHistory", 2131559107), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$4jeFfbPa00Df67qyJvj0h0oI_X4(this, n));
            }
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            final AlertDialog create = builder.create();
            this.showDialog(create);
            final TextView textView = (TextView)create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
            return;
        }
        int k = 0;
        boolean b2 = false;
        while (k < size) {
            final long longValue2 = selectedDialogs.get(k);
            final TLRPC.Dialog dialog3 = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(longValue2);
            Label_0938: {
                if (dialog3 != null) {
                    final int l = (int)longValue2;
                    final int m = (int)(longValue2 >> 32);
                    TLRPC.Chat chat = null;
                    TLRPC.User user = null;
                    Label_1061: {
                        TLRPC.User user2;
                        if (l != 0) {
                            if (l <= 0) {
                                chat = this.getMessagesController().getChat(-l);
                                user = null;
                                break Label_1061;
                            }
                            user2 = this.getMessagesController().getUser(l);
                        }
                        else {
                            final TLRPC.EncryptedChat encryptedChat = this.getMessagesController().getEncryptedChat(m);
                            if (encryptedChat != null) {
                                user2 = this.getMessagesController().getUser(encryptedChat.user_id);
                            }
                            else {
                                user2 = new TLRPC.TL_userEmpty();
                            }
                        }
                        final TLRPC.Chat chat2 = null;
                        user = user2;
                        chat = chat2;
                    }
                    Label_1071: {
                        if (chat != null || user != null) {
                            final boolean b3 = user != null && user.bot && !MessagesController.isSupportUser(user);
                            if (n == 100) {
                                if (this.canPinCount != 0) {
                                    if (dialog3.pinned) {
                                        break Label_1071;
                                    }
                                    if (!this.getMessagesController().pinDialog(longValue2, true, null, -1L)) {
                                        break Label_0938;
                                    }
                                }
                                else {
                                    if (!dialog3.pinned) {
                                        break Label_1071;
                                    }
                                    if (!this.getMessagesController().pinDialog(longValue2, false, null, -1L)) {
                                        break Label_0938;
                                    }
                                }
                                b2 = true;
                            }
                            else if (n == 101) {
                                if (this.canReadCount != 0) {
                                    this.getMessagesController().markMentionsAsRead(longValue2);
                                    final MessagesController messagesController2 = this.getMessagesController();
                                    final int top_message = dialog3.top_message;
                                    messagesController2.markDialogAsRead(longValue2, top_message, top_message, dialog3.last_message_date, false, 0, true);
                                }
                                else {
                                    this.getMessagesController().markDialogAsUnread(longValue2, null, 0L);
                                }
                            }
                            else if (n != 102 && n != 103) {
                                if (n == 104) {
                                    if (size == 1 && this.canMuteCount == 1) {
                                        this.showDialog(AlertsCreator.createMuteAlert((Context)this.getParentActivity(), longValue2), (DialogInterface$OnDismissListener)new _$$Lambda$DialogsActivity$1NIJBQ0Gz4LgguHSbB06qjn5FYs(this));
                                        return;
                                    }
                                    if (this.canUnmuteCount != 0) {
                                        if (this.getMessagesController().isDialogMuted(longValue2)) {
                                            this.getNotificationsController().setDialogNotificationsSettings(longValue2, 4);
                                        }
                                    }
                                    else if (!this.getMessagesController().isDialogMuted(longValue2)) {
                                        this.getNotificationsController().setDialogNotificationsSettings(longValue2, 3);
                                    }
                                }
                            }
                            else {
                                if (size == 1) {
                                    AlertsCreator.createClearOrDeleteDialogAlert(this, n == 103, chat, user, l == 0, new _$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg(this, n, chat, longValue2, b3));
                                    return;
                                }
                                if (n == 103 && this.canClearCacheCount != 0) {
                                    this.getMessagesController().deleteDialog(longValue2, 2, false);
                                }
                                else if (n == 103) {
                                    this.getMessagesController().deleteDialog(longValue2, 1, false);
                                }
                                else {
                                    if (chat != null) {
                                        if (ChatObject.isNotInChat(chat)) {
                                            this.getMessagesController().deleteDialog(longValue2, 0, false);
                                        }
                                        else {
                                            this.getMessagesController().deleteUserFromChat((int)(-longValue2), this.getMessagesController().getUser(this.getUserConfig().getClientUserId()), null);
                                        }
                                    }
                                    else {
                                        this.getMessagesController().deleteDialog(longValue2, 0, false);
                                        if (b3) {
                                            this.getMessagesController().blockUser(l);
                                        }
                                    }
                                    if (AndroidUtilities.isTablet()) {
                                        this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, longValue2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ++k;
        }
        if (n == 100) {
            this.getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
        }
        if (b2) {
            this.hideFloatingButton(false);
            this.listView.smoothScrollToPosition(this.hasHiddenArchive() ? 1 : 0);
        }
        boolean b4 = false;
        if (n != 100) {
            b4 = b4;
            if (n != 102) {
                b4 = true;
            }
        }
        this.hideActionMode(b4);
    }
    
    private void setDialogsListFrozen(final boolean b) {
        if (this.dialogsListFrozen == b) {
            return;
        }
        if (b) {
            DialogsActivity.frozenDialogsList = new ArrayList<TLRPC.Dialog>(getDialogsArray(super.currentAccount, this.dialogsType, this.folderId, false));
        }
        else {
            DialogsActivity.frozenDialogsList = null;
        }
        this.dialogsListFrozen = b;
        this.dialogsAdapter.setDialogsListFrozen(b);
        if (!b) {
            this.dialogsAdapter.notifyDataSetChanged();
        }
    }
    
    private void showOrUpdateActionMode(final TLRPC.Dialog dialog, View view) {
        this.dialogsAdapter.addOrRemoveSelectedDialog(dialog.id, view);
        final ArrayList<Long> selectedDialogs = this.dialogsAdapter.getSelectedDialogs();
        final boolean actionModeShowed = super.actionBar.isActionModeShowed();
        boolean b = true;
        if (actionModeShowed) {
            if (selectedDialogs.isEmpty()) {
                this.hideActionMode(true);
                return;
            }
        }
        else {
            super.actionBar.createActionMode();
            super.actionBar.showActionMode();
            if (this.menuDrawable != null) {
                super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
            }
            if (this.getPinnedCount() > 1) {
                this.dialogsAdapter.onReorderStateChanged(true);
                this.updateVisibleRows(131072);
            }
            final AnimatorSet set = new AnimatorSet();
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            for (int i = 0; i < this.actionModeViews.size(); ++i) {
                view = this.actionModeViews.get(i);
                view.setPivotY((float)(ActionBar.getCurrentActionBarHeight() / 2));
                AndroidUtilities.clearDrawableAnimation(view);
                list.add(ObjectAnimator.ofFloat((Object)view, View.SCALE_Y, new float[] { 0.1f, 1.0f }));
            }
            set.playTogether((Collection)list);
            set.setDuration(250L);
            set.start();
            final MenuDrawable menuDrawable = this.menuDrawable;
            if (menuDrawable != null) {
                menuDrawable.setRotateToBack(false);
                this.menuDrawable.setRotation(1.0f, true);
            }
            else {
                final BackDrawable backDrawable = this.backDrawable;
                if (backDrawable != null) {
                    backDrawable.setRotation(1.0f, true);
                }
            }
            b = false;
        }
        this.updateCounters(false);
        this.selectedDialogsCountTextView.setNumber(selectedDialogs.size(), b);
    }
    
    private void updateCounters(final boolean b) {
        this.canUnmuteCount = 0;
        this.canMuteCount = 0;
        this.canPinCount = 0;
        this.canReadCount = 0;
        this.canClearCacheCount = 0;
        if (b) {
            return;
        }
        final ArrayList<Long> selectedDialogs = this.dialogsAdapter.getSelectedDialogs();
        final int size = selectedDialogs.size();
        int index = 0;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        boolean enabled;
        while (true) {
            enabled = true;
            if (index >= size) {
                break;
            }
            final TLRPC.Dialog dialog = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get((long)selectedDialogs.get(index));
            int n6 = 0;
            int n7 = 0;
            Label_0608: {
                if (dialog == null) {
                    n6 = n3;
                    n7 = n4;
                }
                else {
                    final long id = dialog.id;
                    final boolean pinned = dialog.pinned;
                    final boolean b2 = dialog.unread_count != 0 || dialog.unread_mark;
                    if (this.getMessagesController().isDialogMuted(id)) {
                        ++this.canUnmuteCount;
                    }
                    else {
                        ++this.canMuteCount;
                    }
                    if (b2) {
                        ++this.canReadCount;
                    }
                    if (this.folderId == 1) {
                        n6 = n3 + 1;
                        n7 = n4;
                    }
                    else {
                        n6 = n3;
                        n7 = n4;
                        if (id != this.getUserConfig().getClientUserId()) {
                            n6 = n3;
                            n7 = n4;
                            if (id != 777000L) {
                                n6 = n3;
                                n7 = n4;
                                if (!this.getMessagesController().isProxyDialog(id, false)) {
                                    n7 = n4 + 1;
                                    n6 = n3;
                                }
                            }
                        }
                    }
                    final int i = (int)id;
                    final int j = (int)(id >> 32);
                    Label_0605: {
                        if (DialogObject.isChannel(dialog)) {
                            final TLRPC.Chat chat = this.getMessagesController().getChat(-i);
                            if (this.getMessagesController().isProxyDialog(dialog.id, true)) {
                                ++this.canClearCacheCount;
                                break Label_0608;
                            }
                            if (pinned) {
                                ++n5;
                            }
                            else {
                                ++this.canPinCount;
                            }
                            if (chat == null || !chat.megagroup) {
                                ++this.canClearCacheCount;
                                break Label_0605;
                            }
                            if (!TextUtils.isEmpty((CharSequence)chat.username)) {
                                ++this.canClearCacheCount;
                                break Label_0605;
                            }
                        }
                        else {
                            final boolean b3 = i < 0 && j != 1;
                            if (b3) {
                                this.getMessagesController().getChat(-i);
                            }
                            TLRPC.User user;
                            if (i == 0) {
                                final TLRPC.EncryptedChat encryptedChat = this.getMessagesController().getEncryptedChat(j);
                                if (encryptedChat != null) {
                                    user = this.getMessagesController().getUser(encryptedChat.user_id);
                                }
                                else {
                                    user = new TLRPC.TL_userEmpty();
                                }
                            }
                            else if (!b3 && i > 0 && j != 1) {
                                user = this.getMessagesController().getUser(i);
                            }
                            else {
                                user = null;
                            }
                            if (user != null && user.bot) {
                                MessagesController.isSupportUser(user);
                            }
                            if (pinned) {
                                ++n5;
                            }
                            else {
                                ++this.canPinCount;
                            }
                        }
                        ++n2;
                    }
                    ++n;
                }
            }
            ++index;
            n3 = n6;
            n4 = n7;
        }
        if (n != size) {
            this.deleteItem.setVisibility(8);
        }
        else {
            this.deleteItem.setVisibility(0);
        }
        final int canClearCacheCount = this.canClearCacheCount;
        if ((canClearCacheCount != 0 && canClearCacheCount != size) || (n2 != 0 && n2 != size)) {
            this.clearItem.setVisibility(8);
        }
        else {
            this.clearItem.setVisibility(0);
            if (this.canClearCacheCount != 0) {
                this.clearItem.setText(LocaleController.getString("ClearHistoryCache", 2131559108));
            }
            else {
                this.clearItem.setText(LocaleController.getString("ClearHistory", 2131559107));
            }
        }
        if (n3 != 0) {
            this.archiveItem.setIcon(2131165676);
            this.archiveItem.setContentDescription((CharSequence)LocaleController.getString("Unarchive", 2131560928));
        }
        else {
            this.archiveItem.setIcon(2131165613);
            this.archiveItem.setContentDescription((CharSequence)LocaleController.getString("Archive", 2131558642));
            final ActionBarMenuItem archiveItem = this.archiveItem;
            if (n4 == 0) {
                enabled = false;
            }
            archiveItem.setEnabled(enabled);
            final ActionBarMenuItem archiveItem2 = this.archiveItem;
            float alpha;
            if (n4 != 0) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.5f;
            }
            archiveItem2.setAlpha(alpha);
        }
        if (this.canPinCount + n5 != size) {
            this.pinItem.setVisibility(8);
        }
        else {
            this.pinItem.setVisibility(0);
        }
        if (this.canUnmuteCount != 0) {
            this.muteItem.setTextAndIcon(LocaleController.getString("ChatsUnmute", 2131559078), 2131165678);
        }
        else {
            this.muteItem.setTextAndIcon(LocaleController.getString("ChatsMute", 2131559059), 2131165648);
        }
        if (this.canReadCount != 0) {
            this.readItem.setTextAndIcon(LocaleController.getString("MarkAsRead", 2131559807), 2131165643);
        }
        else {
            this.readItem.setTextAndIcon(LocaleController.getString("MarkAsUnread", 2131559808), 2131165644);
        }
        if (this.canPinCount != 0) {
            this.pinItem.setIcon(2131165657);
            this.pinItem.setContentDescription((CharSequence)LocaleController.getString("PinToTop", 2131560447));
        }
        else {
            this.pinItem.setIcon(2131165679);
            this.pinItem.setContentDescription((CharSequence)LocaleController.getString("UnpinFromTop", 2131560941));
        }
    }
    
    private void updateDialogIndices() {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            if (listView.getAdapter() == this.dialogsAdapter) {
                final int currentAccount = super.currentAccount;
                final int dialogsType = this.dialogsType;
                final int folderId = this.folderId;
                int i = 0;
                final ArrayList<TLRPC.Dialog> dialogsArray = getDialogsArray(currentAccount, dialogsType, folderId, false);
                while (i < this.listView.getChildCount()) {
                    final View child = this.listView.getChildAt(i);
                    if (child instanceof DialogCell) {
                        final DialogCell dialogCell = (DialogCell)child;
                        final TLRPC.Dialog o = (TLRPC.Dialog)this.getMessagesController().dialogs_dict.get(dialogCell.getDialogId());
                        if (o != null) {
                            final int index = dialogsArray.indexOf(o);
                            if (index >= 0) {
                                dialogCell.setDialogIndex(index);
                            }
                        }
                    }
                    ++i;
                }
            }
        }
    }
    
    private void updatePasscodeButton() {
        if (this.passcodeItem == null) {
            return;
        }
        if (SharedConfig.passcodeHash.length() != 0 && !this.searching) {
            this.passcodeItem.setVisibility(0);
            if (SharedConfig.appLocked) {
                this.passcodeItem.setIcon(2131165542);
                this.passcodeItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrPasscodeUnlock", 2131558454));
            }
            else {
                this.passcodeItem.setIcon(2131165544);
                this.passcodeItem.setContentDescription((CharSequence)LocaleController.getString("AccDescrPasscodeLock", 2131558453));
            }
        }
        else {
            this.passcodeItem.setVisibility(8);
        }
    }
    
    private void updateProxyButton(final boolean b) {
        if (this.proxyDrawable == null) {
            return;
        }
        final Context applicationContext = ApplicationLoader.applicationContext;
        boolean b2 = false;
        final SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("mainconfig", 0);
        final String string = sharedPreferences.getString("proxy_ip", "");
        final boolean b3 = sharedPreferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty((CharSequence)string);
        if (!b3 && (!this.getMessagesController().blockedCountry || SharedConfig.proxyList.isEmpty())) {
            this.proxyItem.setVisibility(8);
            this.proxyItemVisisble = false;
        }
        else {
            if (!super.actionBar.isSearchFieldVisible()) {
                this.proxyItem.setVisibility(0);
            }
            final ProxyDrawable proxyDrawable = this.proxyDrawable;
            final int currentConnectionState = this.currentConnectionState;
            if (currentConnectionState == 3 || currentConnectionState == 5) {
                b2 = true;
            }
            proxyDrawable.setConnected(b3, b2, b);
            this.proxyItemVisisble = true;
        }
    }
    
    private void updateSelectedCount() {
        if (this.commentView == null) {
            return;
        }
        if (!this.dialogsAdapter.hasSelectedDialogs()) {
            if (this.dialogsType == 3 && this.selectAlertString == null) {
                super.actionBar.setTitle(LocaleController.getString("ForwardTo", 2131559505));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("SelectChat", 2131560677));
            }
            if (this.commentView.getTag() != null) {
                this.commentView.hidePopup(false);
                this.commentView.closeKeyboard();
                final AnimatorSet set = new AnimatorSet();
                final ChatActivityEnterView commentView = this.commentView;
                set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)commentView, View.TRANSLATION_Y, new float[] { 0.0f, (float)commentView.getMeasuredHeight() }) });
                set.setDuration(180L);
                set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        DialogsActivity.this.commentView.setVisibility(8);
                    }
                });
                set.start();
                this.commentView.setTag((Object)null);
                this.listView.requestLayout();
            }
        }
        else {
            if (this.commentView.getTag() == null) {
                this.commentView.setFieldText("");
                this.commentView.setVisibility(0);
                final AnimatorSet set2 = new AnimatorSet();
                final ChatActivityEnterView commentView2 = this.commentView;
                set2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)commentView2, View.TRANSLATION_Y, new float[] { (float)commentView2.getMeasuredHeight(), 0.0f }) });
                set2.setDuration(180L);
                set2.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                set2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        DialogsActivity.this.commentView.setTag((Object)2);
                        DialogsActivity.this.commentView.requestLayout();
                    }
                });
                set2.start();
                this.commentView.setTag((Object)1);
            }
            super.actionBar.setTitle(LocaleController.formatPluralString("Recipient", this.dialogsAdapter.getSelectedDialogs().size()));
        }
    }
    
    private void updateVisibleRows(final int n) {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            if (!this.dialogsListFrozen) {
                for (int childCount = listView.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.listView.getChildAt(i);
                    if (child instanceof DialogCell) {
                        if (this.listView.getAdapter() != this.dialogsSearchAdapter) {
                            final DialogCell dialogCell = (DialogCell)child;
                            final boolean b = true;
                            final boolean b2 = true;
                            boolean b3 = true;
                            if ((0x20000 & n) != 0x0) {
                                dialogCell.onReorderStateChanged(super.actionBar.isActionModeShowed(), true);
                            }
                            if ((0x10000 & n) != 0x0) {
                                if ((n & 0x2000) == 0x0) {
                                    b3 = false;
                                }
                                dialogCell.setChecked(false, b3);
                            }
                            else {
                                if ((n & 0x800) != 0x0) {
                                    dialogCell.checkCurrentDialogIndex(this.dialogsListFrozen);
                                    if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                                        dialogCell.setDialogSelected(dialogCell.getDialogId() == this.openedDialogId && b);
                                    }
                                }
                                else if ((n & 0x200) != 0x0) {
                                    if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                                        dialogCell.setDialogSelected(dialogCell.getDialogId() == this.openedDialogId && b2);
                                    }
                                }
                                else {
                                    dialogCell.update(n);
                                }
                                final ArrayList<Long> selectedDialogs = this.dialogsAdapter.getSelectedDialogs();
                                if (selectedDialogs != null) {
                                    dialogCell.setChecked(selectedDialogs.contains(dialogCell.getDialogId()), false);
                                }
                            }
                        }
                    }
                    else if (child instanceof UserCell) {
                        ((UserCell)child).update(n);
                    }
                    else if (child instanceof ProfileSearchCell) {
                        ((ProfileSearchCell)child).update(n);
                    }
                    else if (child instanceof RecyclerListView) {
                        final RecyclerListView recyclerListView = (RecyclerListView)child;
                        for (int childCount2 = recyclerListView.getChildCount(), j = 0; j < childCount2; ++j) {
                            final View child2 = recyclerListView.getChildAt(j);
                            if (child2 instanceof HintDialogCell) {
                                ((HintDialogCell)child2).update(n);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean waitingForDialogsAnimationEnd() {
        return this.dialogsItemAnimator.isRunning() || this.dialogRemoveFinished != 0 || this.dialogInsertFinished != 0 || this.dialogChangeFinished != 0;
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        this.pacmanAnimation = null;
        AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$Wz8eSYJLAR_xfNaDl4aQWUwW9N0(context));
        final ActionBarMenu menu = super.actionBar.createMenu();
        if (!this.onlySelect && this.searchString == null && this.folderId == 0) {
            this.proxyDrawable = new ProxyDrawable(context);
            (this.proxyItem = menu.addItem(2, this.proxyDrawable)).setContentDescription((CharSequence)LocaleController.getString("ProxySettings", 2131560519));
            this.passcodeItem = menu.addItem(1, 2131165542);
            this.updatePasscodeButton();
            this.updateProxyButton(false);
        }
        final ActionBarMenuItem setActionBarMenuItemSearchListener = menu.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
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
                }
                return true;
            }
            
            @Override
            public void onSearchCollapse() {
                DialogsActivity.this.searching = false;
                DialogsActivity.this.searchWas = false;
                if (DialogsActivity.this.listView != null) {
                    final RecyclerListView access$300 = DialogsActivity.this.listView;
                    RadialProgressView access$301;
                    if (DialogsActivity.this.folderId == 0) {
                        access$301 = DialogsActivity.this.progressView;
                    }
                    else {
                        access$301 = null;
                    }
                    access$300.setEmptyView(access$301);
                    DialogsActivity.this.searchEmptyView.setVisibility(8);
                    if (!DialogsActivity.this.onlySelect) {
                        DialogsActivity.this.floatingButtonContainer.setVisibility(0);
                        DialogsActivity.this.floatingHidden = true;
                        DialogsActivity.this.floatingButtonContainer.setTranslationY((float)AndroidUtilities.dp(100.0f));
                        DialogsActivity.this.hideFloatingButton(false);
                    }
                    if (DialogsActivity.this.listView.getAdapter() != DialogsActivity.this.dialogsAdapter) {
                        DialogsActivity.this.listView.setAdapter(DialogsActivity.this.dialogsAdapter);
                        DialogsActivity.this.dialogsAdapter.notifyDataSetChanged();
                    }
                }
                if (DialogsActivity.this.dialogsSearchAdapter != null) {
                    DialogsActivity.this.dialogsSearchAdapter.searchDialogs(null);
                }
                DialogsActivity.this.updatePasscodeButton();
                if (DialogsActivity.this.menuDrawable != null) {
                    DialogsActivity.this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
                }
            }
            
            @Override
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
                        DialogsActivity.this.listView.setEmptyView((View)DialogsActivity.this.searchEmptyView);
                        DialogsActivity.this.progressView.setVisibility(8);
                    }
                    if (!DialogsActivity.this.onlySelect) {
                        DialogsActivity.this.floatingButtonContainer.setVisibility(8);
                    }
                }
                DialogsActivity.this.updatePasscodeButton();
                DialogsActivity.this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", 2131558435));
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                final String string = editText.getText().toString();
                if (string.length() != 0 || (DialogsActivity.this.dialogsSearchAdapter != null && DialogsActivity.this.dialogsSearchAdapter.hasRecentRearch())) {
                    DialogsActivity.this.searchWas = true;
                    if (DialogsActivity.this.dialogsSearchAdapter != null && DialogsActivity.this.listView.getAdapter() != DialogsActivity.this.dialogsSearchAdapter) {
                        DialogsActivity.this.listView.setAdapter(DialogsActivity.this.dialogsSearchAdapter);
                        DialogsActivity.this.dialogsSearchAdapter.notifyDataSetChanged();
                    }
                    if (DialogsActivity.this.searchEmptyView != null && DialogsActivity.this.listView.getEmptyView() != DialogsActivity.this.searchEmptyView) {
                        DialogsActivity.this.progressView.setVisibility(8);
                        DialogsActivity.this.listView.setEmptyView((View)DialogsActivity.this.searchEmptyView);
                    }
                }
                if (DialogsActivity.this.dialogsSearchAdapter != null) {
                    DialogsActivity.this.dialogsSearchAdapter.searchDialogs(string);
                }
            }
        });
        setActionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        setActionBarMenuItemSearchListener.setContentDescription((CharSequence)LocaleController.getString("Search", 2131560640));
        if (this.onlySelect) {
            super.actionBar.setBackButtonImage(2131165409);
            if (this.dialogsType == 3 && this.selectAlertString == null) {
                super.actionBar.setTitle(LocaleController.getString("ForwardTo", 2131559505));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("SelectChat", 2131560677));
            }
        }
        else {
            if (this.searchString == null && this.folderId == 0) {
                super.actionBar.setBackButtonDrawable(this.menuDrawable = new MenuDrawable());
                super.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", 2131558451));
            }
            else {
                super.actionBar.setBackButtonDrawable(this.backDrawable = new BackDrawable(false));
            }
            if (this.folderId != 0) {
                super.actionBar.setTitle(LocaleController.getString("ArchivedChats", 2131558653));
            }
            else if (BuildVars.DEBUG_VERSION) {
                super.actionBar.setTitle("Telegram Beta");
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
            }
            super.actionBar.setSupportsHolidayImage(true);
        }
        super.actionBar.setTitleActionRunnable(new _$$Lambda$DialogsActivity$g_ZetuAZEmHs4itgVWSuCgX_8K4(this));
        if (this.allowSwitchAccount && UserConfig.getActivatedAccountsCount() > 1) {
            this.switchItem = menu.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0f));
            final AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            final BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(18.0f));
            this.switchItem.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 17));
            final TLRPC.User currentUser = this.getUserConfig().getCurrentUser();
            avatarDrawable.setInfo(currentUser);
            backupImageView.getImageReceiver().setCurrentAccount(super.currentAccount);
            backupImageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", avatarDrawable, currentUser);
            for (int i = 0; i < 3; ++i) {
                if (AccountInstance.getInstance(i).getUserConfig().getCurrentUser() != null) {
                    final AccountSelectCell accountSelectCell = new AccountSelectCell(context);
                    accountSelectCell.setAccount(i, true);
                    this.switchItem.addSubItem(i + 10, (View)accountSelectCell, AndroidUtilities.dp(230.0f), AndroidUtilities.dp(48.0f));
                }
            }
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (DialogsActivity.this.actionBar.isActionModeShowed()) {
                        DialogsActivity.this.hideActionMode(true);
                    }
                    else if (!DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0) {
                        if (DialogsActivity.this.parentLayout != null) {
                            DialogsActivity.this.parentLayout.getDrawerLayoutContainer().openDrawer(false);
                        }
                    }
                    else {
                        DialogsActivity.this.finishFragment();
                    }
                }
                else if (n == 1) {
                    SharedConfig.appLocked ^= true;
                    SharedConfig.saveConfig();
                    DialogsActivity.this.updatePasscodeButton();
                }
                else if (n == 2) {
                    DialogsActivity.this.presentFragment(new ProxyListActivity());
                }
                else if (n >= 10 && n < 13) {
                    if (DialogsActivity.this.getParentActivity() == null) {
                        return;
                    }
                    final DialogsActivityDelegate access$6200 = DialogsActivity.this.delegate;
                    final LaunchActivity launchActivity = (LaunchActivity)DialogsActivity.this.getParentActivity();
                    launchActivity.switchToAccount(n - 10, true);
                    final DialogsActivity dialogsActivity = new DialogsActivity(DialogsActivity.this.arguments);
                    dialogsActivity.setDelegate(access$6200);
                    launchActivity.presentFragment(dialogsActivity, false, true);
                }
                else if (n == 100 || n == 101 || n == 102 || n == 103 || n == 104 || n == 105) {
                    DialogsActivity.this.perfromSelectedDialogsAction(n, true);
                }
            }
        });
        final RecyclerView sideMenu = this.sideMenu;
        if (sideMenu != null) {
            sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
            this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
            this.sideMenu.getAdapter().notifyDataSetChanged();
        }
        final ActionBarMenu actionMode = super.actionBar.createActionMode();
        (this.selectedDialogsCountTextView = new NumberTextView(actionMode.getContext())).setTextSize(18);
        this.selectedDialogsCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.selectedDialogsCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
        actionMode.addView((View)this.selectedDialogsCountTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        this.selectedDialogsCountTextView.setOnTouchListener((View$OnTouchListener)_$$Lambda$DialogsActivity$pwzbQ3D6N1rpnIvtZZSb6O4Np6g.INSTANCE);
        this.pinItem = actionMode.addItemWithWidth(100, 2131165657, AndroidUtilities.dp(54.0f));
        this.archiveItem = actionMode.addItemWithWidth(105, 2131165613, AndroidUtilities.dp(54.0f));
        this.deleteItem = actionMode.addItemWithWidth(102, 2131165623, AndroidUtilities.dp(54.0f), LocaleController.getString("Delete", 2131559227));
        final ActionBarMenuItem addItemWithWidth = actionMode.addItemWithWidth(0, 2131165416, AndroidUtilities.dp(54.0f), LocaleController.getString("AccDescrMoreOptions", 2131558443));
        this.muteItem = addItemWithWidth.addSubItem(104, 2131165648, LocaleController.getString("ChatsMute", 2131559059));
        this.readItem = addItemWithWidth.addSubItem(101, 2131165643, LocaleController.getString("MarkAsRead", 2131559807));
        this.clearItem = addItemWithWidth.addSubItem(103, 2131165619, LocaleController.getString("ClearHistory", 2131559107));
        this.actionModeViews.add((View)this.pinItem);
        this.actionModeViews.add((View)this.archiveItem);
        this.actionModeViews.add((View)this.deleteItem);
        this.actionModeViews.add((View)addItemWithWidth);
        final ContentView fragmentView = new ContentView(context);
        super.fragmentView = (View)fragmentView;
        this.listView = new RecyclerListView(context) {
            private boolean firstLayout = true;
            private boolean ignoreLayout;
            
            private void checkIfAdapterValid() {
                if (DialogsActivity.this.listView != null && DialogsActivity.this.dialogsAdapter != null && DialogsActivity.this.listView.getAdapter() == DialogsActivity.this.dialogsAdapter && DialogsActivity.this.lastItemsCount != DialogsActivity.this.dialogsAdapter.getItemCount()) {
                    this.ignoreLayout = true;
                    DialogsActivity.this.dialogsAdapter.notifyDataSetChanged();
                    this.ignoreLayout = false;
                }
            }
            
            @Override
            protected void dispatchDraw(final Canvas canvas) {
                super.dispatchDraw(canvas);
                if (DialogsActivity.this.slidingView != null && DialogsActivity.this.pacmanAnimation != null) {
                    DialogsActivity.this.pacmanAnimation.draw(canvas, DialogsActivity.this.slidingView.getTop() + DialogsActivity.this.slidingView.getMeasuredHeight() / 2);
                }
            }
            
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (!DialogsActivity.this.waitingForScrollFinished && DialogsActivity.this.dialogRemoveFinished == 0 && DialogsActivity.this.dialogInsertFinished == 0 && DialogsActivity.this.dialogChangeFinished == 0) {
                    if (motionEvent.getAction() == 0) {
                        final DialogsActivity this$0 = DialogsActivity.this;
                        this$0.allowSwipeDuringCurrentTouch = (this$0.actionBar.isActionModeShowed() ^ true);
                        this.checkIfAdapterValid();
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                return false;
            }
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if ((DialogsActivity.this.dialogRemoveFinished != 0 || DialogsActivity.this.dialogInsertFinished != 0 || DialogsActivity.this.dialogChangeFinished != 0) && !DialogsActivity.this.dialogsItemAnimator.isRunning()) {
                    DialogsActivity.this.onDialogAnimationFinished();
                }
            }
            
            @Override
            protected void onMeasure(final int n, final int n2) {
                if (this.firstLayout && BaseFragment.this.getMessagesController().dialogsLoaded) {
                    if (DialogsActivity.this.hasHiddenArchive()) {
                        this.ignoreLayout = true;
                        DialogsActivity.this.layoutManager.scrollToPositionWithOffset(1, 0);
                        this.ignoreLayout = false;
                    }
                    this.firstLayout = false;
                }
                this.checkIfAdapterValid();
                super.onMeasure(n, n2);
            }
            
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (!DialogsActivity.this.waitingForScrollFinished && DialogsActivity.this.dialogRemoveFinished == 0 && DialogsActivity.this.dialogInsertFinished == 0 && DialogsActivity.this.dialogChangeFinished == 0) {
                    final int action = motionEvent.getAction();
                    if ((action == 1 || action == 3) && !DialogsActivity.this.itemTouchhelper.isIdle() && DialogsActivity.this.swipeController.swipingFolder) {
                        DialogsActivity.this.swipeController.swipeFolderBack = true;
                        if (DialogsActivity.this.itemTouchhelper.checkHorizontalSwipe(null, 4) != 0) {
                            SharedConfig.toggleArchiveHidden();
                            DialogsActivity.this.getUndoView().showWithAction(0L, 7, null, null);
                        }
                    }
                    final boolean onTouchEvent = super.onTouchEvent(motionEvent);
                    if ((action == 1 || action == 3) && DialogsActivity.this.allowScrollToHiddenView) {
                        final int firstVisibleItemPosition = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                        if (firstVisibleItemPosition == 0) {
                            final View viewByPosition = DialogsActivity.this.layoutManager.findViewByPosition(firstVisibleItemPosition);
                            float n;
                            if (SharedConfig.useThreeLinesLayout) {
                                n = 78.0f;
                            }
                            else {
                                n = 72.0f;
                            }
                            final int n2 = AndroidUtilities.dp(n) / 4;
                            final int n3 = viewByPosition.getTop() + viewByPosition.getMeasuredHeight();
                            if (viewByPosition != null) {
                                if (n3 < n2 * 3) {
                                    DialogsActivity.this.listView.smoothScrollBy(0, n3, (Interpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                                }
                                else {
                                    DialogsActivity.this.listView.smoothScrollBy(0, viewByPosition.getTop(), (Interpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                                }
                            }
                        }
                        DialogsActivity.this.allowScrollToHiddenView = false;
                    }
                    return onTouchEvent;
                }
                return false;
            }
            
            @Override
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
            
            @Override
            public void setAdapter(final Adapter adapter) {
                super.setAdapter(adapter);
                this.firstLayout = true;
            }
        };
        this.dialogsItemAnimator = new DialogsItemAnimator() {
            @Override
            public void onAddFinished(final ViewHolder viewHolder) {
                if (DialogsActivity.this.dialogInsertFinished == 2) {
                    DialogsActivity.this.dialogInsertFinished = 1;
                }
            }
            
            @Override
            protected void onAllAnimationsDone() {
                if (DialogsActivity.this.dialogRemoveFinished == 1 || DialogsActivity.this.dialogInsertFinished == 1 || DialogsActivity.this.dialogChangeFinished == 1) {
                    DialogsActivity.this.onDialogAnimationFinished();
                }
            }
            
            @Override
            public void onChangeFinished(final ViewHolder viewHolder, final boolean b) {
                if (DialogsActivity.this.dialogChangeFinished == 2) {
                    DialogsActivity.this.dialogChangeFinished = 1;
                }
            }
            
            @Override
            public void onRemoveFinished(final ViewHolder viewHolder) {
                if (DialogsActivity.this.dialogRemoveFinished == 2) {
                    DialogsActivity.this.dialogRemoveFinished = 1;
                }
            }
        };
        this.listView.setItemAnimator((RecyclerView.ItemAnimator)this.dialogsItemAnimator);
        this.listView.setVerticalScrollBarEnabled(true);
        this.listView.setInstantClick(true);
        this.listView.setTag((Object)4);
        (this.layoutManager = new LinearLayoutManager(context) {
            @Override
            public int scrollVerticallyBy(int n, final Recycler recycler, final State state) {
                int n2 = n;
                Label_0336: {
                    if (DialogsActivity.this.listView.getAdapter() != DialogsActivity.this.dialogsAdapter) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (DialogsActivity.this.dialogsType != 0) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (DialogsActivity.this.onlySelect) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (DialogsActivity.this.allowScrollToHiddenView) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (DialogsActivity.this.folderId != 0 || (n2 = n) >= 0) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (!BaseFragment.this.getMessagesController().hasHiddenArchive()) {
                        break Label_0336;
                    }
                    final int firstVisibleItemPosition = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                    int n3;
                    if ((n3 = firstVisibleItemPosition) == 0) {
                        final View viewByPosition = DialogsActivity.this.layoutManager.findViewByPosition(firstVisibleItemPosition);
                        n3 = firstVisibleItemPosition;
                        if (viewByPosition != null) {
                            n3 = firstVisibleItemPosition;
                            if (viewByPosition.getBottom() <= AndroidUtilities.dp(1.0f)) {
                                n3 = 1;
                            }
                        }
                    }
                    n2 = n;
                    if (n3 == 0) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (n3 == -1) {
                        break Label_0336;
                    }
                    final View viewByPosition2 = DialogsActivity.this.layoutManager.findViewByPosition(n3);
                    n2 = n;
                    if (viewByPosition2 == null) {
                        break Label_0336;
                    }
                    float n4;
                    if (SharedConfig.useThreeLinesLayout) {
                        n4 = 78.0f;
                    }
                    else {
                        n4 = 72.0f;
                    }
                    final int n5 = -viewByPosition2.getTop() + (n3 - 1) * (AndroidUtilities.dp(n4) + 1);
                    n2 = n;
                    if (n5 >= Math.abs(n)) {
                        break Label_0336;
                    }
                    final DialogsActivity this$0 = DialogsActivity.this;
                    this$0.totalConsumedAmount += Math.abs(n);
                    n = (n2 = -n5);
                    if (!DialogsActivity.this.startedScrollAtTop) {
                        break Label_0336;
                    }
                    n2 = n;
                    if (DialogsActivity.this.totalConsumedAmount < AndroidUtilities.dp(150.0f)) {
                        break Label_0336;
                    }
                    DialogsActivity.this.allowScrollToHiddenView = true;
                    try {
                        DialogsActivity.this.listView.performHapticFeedback(3, 2);
                        n2 = n;
                        return super.scrollVerticallyBy(n2, recycler, state);
                    }
                    catch (Exception ex) {
                        n2 = n;
                        return super.scrollVerticallyBy(n2, recycler, state);
                    }
                }
            }
            
            @Override
            public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
                if (DialogsActivity.this.hasHiddenArchive() && targetPosition == 1) {
                    super.smoothScrollToPosition(recyclerView, state, targetPosition);
                }
                else {
                    final LinearSmoothScrollerMiddle linearSmoothScrollerMiddle = new LinearSmoothScrollerMiddle(recyclerView.getContext());
                    ((RecyclerView.SmoothScroller)linearSmoothScrollerMiddle).setTargetPosition(targetPosition);
                    ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScrollerMiddle);
                }
            }
        }).setOrientation(1);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)this.layoutManager);
        final RecyclerListView listView = this.listView;
        int verticalScrollbarPosition;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        fragmentView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$DialogsActivity$vVlyn12mo3e9YWraUH_TSyb3Khs(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListenerExtended)new RecyclerListView.OnItemLongClickListenerExtended() {
            @Override
            public boolean onItemClick(final View view, int fixPosition, final float n, final float n2) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return false;
                }
                if (!DialogsActivity.this.actionBar.isActionModeShowed() && !AndroidUtilities.isTablet() && !DialogsActivity.this.onlySelect && view instanceof DialogCell) {
                    final DialogCell dialogCell = (DialogCell)view;
                    if (dialogCell.isPointInsideAvatar(n, n2)) {
                        final long dialogId = dialogCell.getDialogId();
                        final Bundle bundle = new Bundle();
                        final int n3 = (int)dialogId;
                        fixPosition = (int)(dialogId >> 32);
                        final int messageId = dialogCell.getMessageId();
                        if (n3 != 0) {
                            if (fixPosition == 1) {
                                bundle.putInt("chat_id", n3);
                            }
                            else if (n3 > 0) {
                                bundle.putInt("user_id", n3);
                            }
                            else if (n3 < 0) {
                                fixPosition = n3;
                                if (messageId != 0) {
                                    final TLRPC.Chat chat = BaseFragment.this.getMessagesController().getChat(-n3);
                                    fixPosition = n3;
                                    if (chat != null) {
                                        fixPosition = n3;
                                        if (chat.migrated_to != null) {
                                            bundle.putInt("migrated_to", n3);
                                            fixPosition = -chat.migrated_to.channel_id;
                                        }
                                    }
                                }
                                bundle.putInt("chat_id", -fixPosition);
                            }
                            if (messageId != 0) {
                                bundle.putInt("message_id", messageId);
                            }
                            if (DialogsActivity.this.searchString != null) {
                                if (BaseFragment.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                                    DialogsActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    DialogsActivity.this.presentFragmentAsPreview(new ChatActivity(bundle));
                                }
                            }
                            else if (BaseFragment.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                                DialogsActivity.this.presentFragmentAsPreview(new ChatActivity(bundle));
                            }
                            return true;
                        }
                        return false;
                    }
                }
                if (DialogsActivity.this.listView.getAdapter() == DialogsActivity.this.dialogsSearchAdapter) {
                    DialogsActivity.this.dialogsSearchAdapter.getItem(fixPosition);
                    return false;
                }
                final ArrayList<TLRPC.Dialog> dialogsArray = DialogsActivity.getDialogsArray(DialogsActivity.this.currentAccount, DialogsActivity.this.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.dialogsListFrozen);
                fixPosition = DialogsActivity.this.dialogsAdapter.fixPosition(fixPosition);
                if (fixPosition >= 0 && fixPosition < dialogsArray.size()) {
                    final TLRPC.Dialog dialog = dialogsArray.get(fixPosition);
                    if (DialogsActivity.this.onlySelect) {
                        if (DialogsActivity.this.dialogsType != 3 || DialogsActivity.this.selectAlertString != null) {
                            return false;
                        }
                        DialogsActivity.this.dialogsAdapter.addOrRemoveSelectedDialog(dialog.id, view);
                        DialogsActivity.this.updateSelectedCount();
                    }
                    else {
                        if (dialog instanceof TLRPC.TL_dialogFolder) {
                            return false;
                        }
                        if (DialogsActivity.this.actionBar.isActionModeShowed() && dialog.pinned) {
                            return false;
                        }
                        DialogsActivity.this.showOrUpdateActionMode(dialog, view);
                    }
                    return true;
                }
                return false;
            }
            
            @Override
            public void onLongClickRelease() {
                DialogsActivity.this.finishPreviewFragment();
            }
            
            @Override
            public void onMove(final float n, final float n2) {
                DialogsActivity.this.movePreviewFragment(n2);
            }
        });
        this.swipeController = new SwipeController();
        (this.itemTouchhelper = new ItemTouchHelper((ItemTouchHelper.Callback)this.swipeController)).attachToRecyclerView(this.listView);
        (this.searchEmptyView = new EmptyTextProgressView(context)).setVisibility(8);
        this.searchEmptyView.setShowAtCenter(true);
        this.searchEmptyView.setTopImage(2131165816);
        this.searchEmptyView.setText(LocaleController.getString("SettingsNoResults", 2131560742));
        fragmentView.addView((View)this.searchEmptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.progressView = new RadialProgressView(context)).setVisibility(8);
        fragmentView.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        this.floatingButtonContainer = new FrameLayout(context);
        final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
        int visibility;
        if (!this.onlySelect && this.folderId == 0) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        floatingButtonContainer.setVisibility(visibility);
        final FrameLayout floatingButtonContainer2 = this.floatingButtonContainer;
        int n;
        if (Build$VERSION.SDK_INT >= 21) {
            n = 56;
        }
        else {
            n = 60;
        }
        int n2;
        if (Build$VERSION.SDK_INT >= 21) {
            n2 = 56;
        }
        else {
            n2 = 60;
        }
        final float n3 = (float)(n2 + 14);
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 4.0f;
        }
        else {
            n5 = 0.0f;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 0.0f;
        }
        else {
            n6 = 4.0f;
        }
        fragmentView.addView((View)floatingButtonContainer2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n + 20, n3, n4 | 0x50, n5, 0.0f, n6, 0.0f));
        this.floatingButtonContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogsActivity$6X_tHzQFSQYoiAZxS6kgZUUhZCo(this));
        (this.floatingButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        CombinedDrawable simpleSelectorCircleDrawable;
        final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165387).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
            simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        this.floatingButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
        this.floatingButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.floatingButton.setImageResource(2131165386);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        this.floatingButtonContainer.setContentDescription((CharSequence)LocaleController.getString("NewMessageTitle", 2131559901));
        final FrameLayout floatingButtonContainer3 = this.floatingButtonContainer;
        final ImageView floatingButton = this.floatingButton;
        int n7;
        if (Build$VERSION.SDK_INT >= 21) {
            n7 = 56;
        }
        else {
            n7 = 60;
        }
        int n8;
        if (Build$VERSION.SDK_INT >= 21) {
            n8 = 56;
        }
        else {
            n8 = 60;
        }
        floatingButtonContainer3.addView((View)floatingButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n7, (float)n8, 51, 10.0f, 0.0f, 10.0f, 0.0f));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    if (DialogsActivity.this.searching && DialogsActivity.this.searchWas) {
                        AndroidUtilities.hideKeyboard(DialogsActivity.this.getParentActivity().getCurrentFocus());
                    }
                    DialogsActivity.this.scrollingManually = true;
                }
                else {
                    DialogsActivity.this.scrollingManually = false;
                }
                if (DialogsActivity.this.waitingForScrollFinished && n == 0) {
                    DialogsActivity.this.waitingForScrollFinished = false;
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, int n, int n2) {
                final int firstVisibleItemPosition = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                n2 = Math.abs(DialogsActivity.this.layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                n = recyclerView.getAdapter().getItemCount();
                if (DialogsActivity.this.searching && DialogsActivity.this.searchWas) {
                    if (n2 > 0 && DialogsActivity.this.layoutManager.findLastVisibleItemPosition() == n - 1 && !DialogsActivity.this.dialogsSearchAdapter.isMessagesSearchEndReached()) {
                        DialogsActivity.this.dialogsSearchAdapter.loadMoreSearchMessages();
                    }
                    return;
                }
                if (n2 > 0 && DialogsActivity.this.layoutManager.findLastVisibleItemPosition() >= DialogsActivity.getDialogsArray(DialogsActivity.this.currentAccount, DialogsActivity.this.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.dialogsListFrozen).size() - 10) {
                    final boolean b = BaseFragment.this.getMessagesController().isDialogsEndReached(DialogsActivity.this.folderId) ^ true;
                    if (b || !BaseFragment.this.getMessagesController().isServerDialogsEndReached(DialogsActivity.this.folderId)) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$8$owKzhPjqms37BNI54Vcmj0_m32Y(this, b));
                    }
                }
                if (DialogsActivity.this.floatingButtonContainer.getVisibility() != 8) {
                    n2 = 0;
                    final View child = recyclerView.getChildAt(0);
                    if (child != null) {
                        n = child.getTop();
                    }
                    else {
                        n = 0;
                    }
                    boolean b3 = false;
                    Label_0339: {
                        boolean b2;
                        if (DialogsActivity.this.prevPosition == firstVisibleItemPosition) {
                            final int access$8800 = DialogsActivity.this.prevTop;
                            b2 = (b3 = (n < DialogsActivity.this.prevTop));
                            if (Math.abs(access$8800 - n) <= 1) {
                                break Label_0339;
                            }
                        }
                        else {
                            b2 = (firstVisibleItemPosition > DialogsActivity.this.prevPosition);
                        }
                        n2 = 1;
                        b3 = b2;
                    }
                    if (n2 != 0 && DialogsActivity.this.scrollUpdated && (b3 || (!b3 && DialogsActivity.this.scrollingManually))) {
                        DialogsActivity.this.hideFloatingButton(b3);
                    }
                    DialogsActivity.this.prevPosition = firstVisibleItemPosition;
                    DialogsActivity.this.prevTop = n;
                    DialogsActivity.this.scrollUpdated = true;
                }
            }
        });
        if (this.searchString == null) {
            this.dialogsAdapter = new DialogsAdapter(context, this.dialogsType, this.folderId, this.onlySelect) {
                @Override
                public void notifyDataSetChanged() {
                    DialogsActivity.this.lastItemsCount = this.getItemCount();
                    super.notifyDataSetChanged();
                }
            };
            if (AndroidUtilities.isTablet()) {
                final long openedDialogId = this.openedDialogId;
                if (openedDialogId != 0L) {
                    this.dialogsAdapter.setOpenedDialogId(openedDialogId);
                }
            }
            this.listView.setAdapter(this.dialogsAdapter);
        }
        int n9;
        if (this.searchString != null) {
            n9 = 2;
        }
        else if (!this.onlySelect) {
            n9 = 1;
        }
        else {
            n9 = 0;
        }
        (this.dialogsSearchAdapter = new DialogsSearchAdapter(context, n9, this.dialogsType)).setDelegate((DialogsSearchAdapter.DialogsSearchAdapterDelegate)new DialogsSearchAdapter.DialogsSearchAdapterDelegate() {
            @Override
            public void didPressedOnSubDialog(final long openedDialogId) {
                if (DialogsActivity.this.onlySelect) {
                    if (DialogsActivity.this.dialogsAdapter.hasSelectedDialogs()) {
                        DialogsActivity.this.dialogsAdapter.addOrRemoveSelectedDialog(openedDialogId, null);
                        DialogsActivity.this.updateSelectedCount();
                        DialogsActivity.this.closeSearch();
                    }
                    else {
                        DialogsActivity.this.didSelectResult(openedDialogId, true, false);
                    }
                }
                else {
                    final int n = (int)openedDialogId;
                    final Bundle bundle = new Bundle();
                    if (n > 0) {
                        bundle.putInt("user_id", n);
                    }
                    else {
                        bundle.putInt("chat_id", -n);
                    }
                    DialogsActivity.this.closeSearch();
                    if (AndroidUtilities.isTablet() && DialogsActivity.this.dialogsAdapter != null) {
                        final DialogsAdapter access$1800 = DialogsActivity.this.dialogsAdapter;
                        DialogsActivity.this.openedDialogId = openedDialogId;
                        access$1800.setOpenedDialogId(openedDialogId);
                        DialogsActivity.this.updateVisibleRows(512);
                    }
                    if (DialogsActivity.this.searchString != null) {
                        if (BaseFragment.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                            DialogsActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                            DialogsActivity.this.presentFragment(new ChatActivity(bundle));
                        }
                    }
                    else if (BaseFragment.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                        DialogsActivity.this.presentFragment(new ChatActivity(bundle));
                    }
                }
            }
            
            @Override
            public void needClearList() {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)DialogsActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("ClearSearchAlertTitle", 2131559116));
                builder.setMessage(LocaleController.getString("ClearSearchAlert", 2131559115));
                builder.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$10$Q2pbWf5Q1Cqk_6Ep5ajOPUW2nyo(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                final AlertDialog create = builder.create();
                DialogsActivity.this.showDialog(create);
                final TextView textView = (TextView)create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                }
            }
            
            @Override
            public void needRemoveHint(final int i) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return;
                }
                final TLRPC.User user = BaseFragment.this.getMessagesController().getUser(i);
                if (user == null) {
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)DialogsActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("ChatHintsDeleteAlertTitle", 2131559032));
                builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("ChatHintsDeleteAlert", 2131559031, ContactsController.formatName(user.first_name, user.last_name))));
                builder.setPositiveButton(LocaleController.getString("StickersRemove", 2131560811), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$10$7JwjKBJfwtWw34NhDCOKWZDwXRQ(this, i));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                final AlertDialog create = builder.create();
                DialogsActivity.this.showDialog(create);
                final TextView textView = (TextView)create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                }
            }
            
            @Override
            public void searchStateChanged(final boolean b) {
                if (DialogsActivity.this.searching && DialogsActivity.this.searchWas && DialogsActivity.this.searchEmptyView != null) {
                    if (b) {
                        DialogsActivity.this.searchEmptyView.showProgress();
                    }
                    else {
                        DialogsActivity.this.searchEmptyView.showTextView();
                    }
                }
            }
        });
        final RecyclerListView listView2 = this.listView;
        RadialProgressView progressView;
        if (this.folderId == 0) {
            progressView = this.progressView;
        }
        else {
            progressView = null;
        }
        listView2.setEmptyView(progressView);
        final String searchString = this.searchString;
        if (searchString != null) {
            super.actionBar.openSearchField(searchString, false);
        }
        if (!this.onlySelect && this.dialogsType == 0) {
            final FragmentContextView additionalContextView = new FragmentContextView(context, this, true);
            fragmentView.addView((View)additionalContextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 39.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            final FragmentContextView additionalContextView2 = new FragmentContextView(context, this, false);
            fragmentView.addView((View)additionalContextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 39.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            additionalContextView2.setAdditionalContextView(additionalContextView);
            additionalContextView.setAdditionalContextView(additionalContextView2);
        }
        else if (this.dialogsType == 3 && this.selectAlertString == null) {
            final ChatActivityEnterView commentView = this.commentView;
            if (commentView != null) {
                commentView.onDestroy();
            }
            (this.commentView = new ChatActivityEnterView(this.getParentActivity(), fragmentView, null, false)).setAllowStickersAndGifs(false, false);
            this.commentView.setForceShowSendButton(true, false);
            this.commentView.setVisibility(8);
            fragmentView.addView((View)this.commentView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
            this.commentView.setDelegate((ChatActivityEnterView.ChatActivityEnterViewDelegate)new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
                @Override
                public void didPressedAttachButton() {
                }
                
                @Override
                public void needChangeVideoPreviewState(final int n, final float n2) {
                }
                
                @Override
                public void needSendTyping() {
                }
                
                @Override
                public void needShowMediaBanHint() {
                }
                
                @Override
                public void needStartRecordAudio(final int n) {
                }
                
                @Override
                public void needStartRecordVideo(final int n) {
                }
                
                @Override
                public void onAttachButtonHidden() {
                }
                
                @Override
                public void onAttachButtonShow() {
                }
                
                @Override
                public void onMessageEditEnd(final boolean b) {
                }
                
                @Override
                public void onMessageSend(final CharSequence charSequence) {
                    if (DialogsActivity.this.delegate == null) {
                        return;
                    }
                    final ArrayList<Long> selectedDialogs = DialogsActivity.this.dialogsAdapter.getSelectedDialogs();
                    if (selectedDialogs.isEmpty()) {
                        return;
                    }
                    DialogsActivity.this.delegate.didSelectDialogs(DialogsActivity.this, selectedDialogs, charSequence, false);
                }
                
                @Override
                public void onPreAudioVideoRecord() {
                }
                
                @Override
                public void onStickersExpandedChange() {
                }
                
                @Override
                public void onStickersTab(final boolean b) {
                }
                
                @Override
                public void onSwitchRecordMode(final boolean b) {
                }
                
                @Override
                public void onTextChanged(final CharSequence charSequence, final boolean b) {
                }
                
                @Override
                public void onTextSelectionChanged(final int n, final int n2) {
                }
                
                @Override
                public void onTextSpansChanged(final CharSequence charSequence) {
                }
                
                @Override
                public void onWindowSizeChanged(final int n) {
                }
            });
        }
        for (int j = 0; j < 2; ++j) {
            fragmentView.addView((View)(this.undoView[j] = new UndoView(context) {
                @Override
                protected boolean canUndo() {
                    return DialogsActivity.this.dialogsItemAnimator.isRunning() ^ true;
                }
                
                public void setTranslationY(float translationY) {
                    super.setTranslationY(translationY);
                    if (this == DialogsActivity.this.undoView[0] && DialogsActivity.this.undoView[1].getVisibility() != 0) {
                        translationY = this.getMeasuredHeight() + AndroidUtilities.dp(8.0f) - translationY;
                        if (!DialogsActivity.this.floatingHidden) {
                            DialogsActivity.this.floatingButtonContainer.setTranslationY(DialogsActivity.this.floatingButtonContainer.getTranslationY() + DialogsActivity.this.additionalFloatingTranslation - translationY);
                        }
                        DialogsActivity.this.additionalFloatingTranslation = translationY;
                    }
                }
            }), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
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
    
    @Override
    public void didReceivedNotification(int currentConnectionState, int intValue, final Object... array) {
        if (currentConnectionState == NotificationCenter.dialogsNeedReload) {
            if (this.dialogsListFrozen) {
                return;
            }
            final DialogsAdapter dialogsAdapter = this.dialogsAdapter;
            if (dialogsAdapter != null) {
                if (!dialogsAdapter.isDataSetChanged() && array.length <= 0) {
                    this.updateVisibleRows(2048);
                }
                else {
                    this.dialogsAdapter.notifyDataSetChanged();
                }
            }
            final RecyclerListView listView = this.listView;
            if (listView != null) {
                try {
                    final RecyclerView.Adapter adapter = listView.getAdapter();
                    final DialogsAdapter dialogsAdapter2 = this.dialogsAdapter;
                    View progressView = null;
                    if (adapter == dialogsAdapter2) {
                        this.searchEmptyView.setVisibility(8);
                        final RecyclerListView listView2 = this.listView;
                        if (this.folderId == 0) {
                            progressView = this.progressView;
                        }
                        listView2.setEmptyView(progressView);
                    }
                    else {
                        if (this.searching && this.searchWas) {
                            this.listView.setEmptyView((View)this.searchEmptyView);
                        }
                        else {
                            this.searchEmptyView.setVisibility(8);
                            this.listView.setEmptyView(null);
                        }
                        this.progressView.setVisibility(8);
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        else if (currentConnectionState == NotificationCenter.emojiDidLoad) {
            this.updateVisibleRows(0);
        }
        else if (currentConnectionState == NotificationCenter.closeSearchByActiveAction) {
            final ActionBar actionBar = super.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
        }
        else if (currentConnectionState == NotificationCenter.proxySettingsChanged) {
            this.updateProxyButton(false);
        }
        else if (currentConnectionState == NotificationCenter.updateInterfaces) {
            this.updateVisibleRows((int)array[0]);
        }
        else if (currentConnectionState == NotificationCenter.appDidLogout) {
            DialogsActivity.dialogsLoaded[super.currentAccount] = false;
        }
        else if (currentConnectionState == NotificationCenter.encryptedChatUpdated) {
            this.updateVisibleRows(0);
        }
        else if (currentConnectionState == NotificationCenter.contactsDidLoad) {
            if (this.dialogsListFrozen) {
                return;
            }
            if (this.dialogsType == 0 && this.getMessagesController().getDialogs(this.folderId).isEmpty()) {
                final DialogsAdapter dialogsAdapter3 = this.dialogsAdapter;
                if (dialogsAdapter3 != null) {
                    dialogsAdapter3.notifyDataSetChanged();
                }
            }
            else {
                this.updateVisibleRows(0);
            }
        }
        else if (currentConnectionState == NotificationCenter.openedChatChanged) {
            if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                final boolean booleanValue = (boolean)array[1];
                final long longValue = (long)array[0];
                if (booleanValue) {
                    if (longValue == this.openedDialogId) {
                        this.openedDialogId = 0L;
                    }
                }
                else {
                    this.openedDialogId = longValue;
                }
                final DialogsAdapter dialogsAdapter4 = this.dialogsAdapter;
                if (dialogsAdapter4 != null) {
                    dialogsAdapter4.setOpenedDialogId(this.openedDialogId);
                }
                this.updateVisibleRows(512);
            }
        }
        else if (currentConnectionState == NotificationCenter.notificationsSettingsUpdated) {
            this.updateVisibleRows(0);
        }
        else if (currentConnectionState != NotificationCenter.messageReceivedByAck && currentConnectionState != NotificationCenter.messageReceivedByServer && currentConnectionState != NotificationCenter.messageSendError) {
            if (currentConnectionState == NotificationCenter.didSetPasscode) {
                this.updatePasscodeButton();
            }
            else if (currentConnectionState == NotificationCenter.needReloadRecentDialogsSearch) {
                final DialogsSearchAdapter dialogsSearchAdapter = this.dialogsSearchAdapter;
                if (dialogsSearchAdapter != null) {
                    dialogsSearchAdapter.loadRecentSearch();
                }
            }
            else if (currentConnectionState == NotificationCenter.replyMessagesDidLoad) {
                this.updateVisibleRows(32768);
            }
            else if (currentConnectionState == NotificationCenter.reloadHints) {
                final DialogsSearchAdapter dialogsSearchAdapter2 = this.dialogsSearchAdapter;
                if (dialogsSearchAdapter2 != null) {
                    ((RecyclerView.Adapter)dialogsSearchAdapter2).notifyDataSetChanged();
                }
            }
            else if (currentConnectionState == NotificationCenter.didUpdateConnectionState) {
                currentConnectionState = AccountInstance.getInstance(intValue).getConnectionsManager().getConnectionState();
                if (this.currentConnectionState != currentConnectionState) {
                    this.currentConnectionState = currentConnectionState;
                    this.updateProxyButton(true);
                }
            }
            else if (currentConnectionState != NotificationCenter.dialogsUnreadCounterChanged) {
                if (currentConnectionState == NotificationCenter.needDeleteDialog) {
                    if (super.fragmentView == null) {
                        return;
                    }
                    final long longValue2 = (long)array[0];
                    final TLRPC.User user = (TLRPC.User)array[1];
                    final _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g $$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g = new _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g(this, (TLRPC.Chat)array[2], longValue2, (boolean)array[3]);
                    if (this.undoView[0] != null) {
                        this.getUndoView().showWithAction(longValue2, 1, $$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g);
                    }
                    else {
                        $$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g.run();
                    }
                }
                else if (currentConnectionState == NotificationCenter.folderBecomeEmpty) {
                    intValue = (int)array[0];
                    currentConnectionState = this.folderId;
                    if (currentConnectionState == intValue && currentConnectionState != 0) {
                        this.finishFragment();
                    }
                }
            }
        }
        else {
            this.updateVisibleRows(4096);
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c $$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c = new _$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c(this);
        final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        final DialogCell movingView = this.movingView;
        if (movingView != null) {
            list.add(new ThemeDescription((View)movingView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        }
        if (this.folderId == 0) {
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
            list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[] { Theme.dialogs_holidayDrawable }, null, "actionBarDefaultTitle"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        }
        else {
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefaultArchived"));
            list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefaultArchived"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultArchivedIcon"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[] { Theme.dialogs_holidayDrawable }, null, "actionBarDefaultArchivedTitle"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultArchivedSelector"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultArchivedSearch"));
            list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchArchivedPlaceholder"));
        }
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, null, null, null, null, "actionBarActionModeDefault"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, "actionBarActionModeDefaultTop"));
        list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, "actionBarActionModeDefaultSelector"));
        list.add(new ThemeDescription(this.selectedDialogsCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"));
        list.add(new ThemeDescription((View)this.searchEmptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        list.add(new ThemeDescription((View)this.searchEmptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { DialogsEmptyCell.class }, new String[] { "emptyTextView1" }, null, null, null, "chats_nameMessage_threeLines"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { DialogsEmptyCell.class }, new String[] { "emptyTextView2" }, null, null, null, "chats_message"));
        list.add(new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"));
        list.add(new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"));
        list.add(new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundRed"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundOrange"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundViolet"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundGreen"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundCyan"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundBlue"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundPink"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundSaved"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundArchived"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "avatar_backgroundArchivedHidden"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, Theme.dialogs_countPaint, null, null, "chats_unreadCounter"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, Theme.dialogs_countGrayPaint, null, null, "chats_unreadCounterMuted"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_countTextPaint, null, null, "chats_unreadCounterText"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_namePaint, (Paint)Theme.dialogs_searchNamePaint }, null, null, "chats_name"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_nameEncryptedPaint, (Paint)Theme.dialogs_searchNameEncryptedPaint }, null, null, "chats_secretName"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_lockDrawable }, null, "chats_secretIcon"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_groupDrawable, Theme.dialogs_broadcastDrawable, Theme.dialogs_botDrawable }, null, "chats_nameIcon"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_scamDrawable }, null, "chats_draft"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_pinnedDrawable, Theme.dialogs_reorderDrawable }, null, "chats_pinnedIcon"));
        if (SharedConfig.useThreeLinesLayout) {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_messagePaint, null, null, "chats_message_threeLines"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_messagePaint, null, null, "chats_message"));
        }
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_messageNamePaint, null, null, "chats_nameMessage_threeLines"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, null, null, "chats_draft"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_nameMessage"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_draft"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_attachMessage"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_nameArchived"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_nameMessageArchived"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_nameMessageArchived_threeLines"));
        list.add(new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_messageArchived"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_messagePrintingPaint, null, null, "chats_actionMessage"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, (Paint)Theme.dialogs_timePaint, null, null, "chats_date"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, Theme.dialogs_pinnedPaint, null, null, "chats_pinnedOverlay"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, Theme.dialogs_tabletSeletedPaint, null, null, "chats_tabletSelectedOverlay"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_checkDrawable, Theme.dialogs_halfCheckDrawable }, null, "chats_sentCheck"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_clockDrawable }, null, "chats_sentClock"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, Theme.dialogs_errorPaint, null, null, "chats_sentError"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_errorDrawable }, null, "chats_sentErrorIcon"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedCheckDrawable }, null, "chats_verifiedCheck"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class, ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedDrawable }, null, "chats_verifiedBackground"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_muteDrawable }, null, "chats_muteIcon"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { Theme.dialogs_mentionDrawable }, null, "chats_mentionIcon"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, null, null, "chats_archivePinBackground"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, null, null, "chats_archiveBackground"));
        if (SharedConfig.archiveHidden) {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Arrow1", "avatar_backgroundArchivedHidden"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Arrow2", "avatar_backgroundArchivedHidden"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Arrow1", "avatar_backgroundArchived"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Arrow2", "avatar_backgroundArchived"));
        }
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Box2", "avatar_text"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { Theme.dialogs_archiveAvatarDrawable }, "Box1", "avatar_text"));
        final Drawable dialogs_pinArchiveDrawable = Theme.dialogs_pinArchiveDrawable;
        if (dialogs_pinArchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable = (LottieDrawable)dialogs_pinArchiveDrawable;
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable }, "Arrow", "chats_archiveIcon"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable }, "Line", "chats_archiveIcon"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { dialogs_pinArchiveDrawable }, null, "chats_archiveIcon"));
        }
        final Drawable dialogs_unpinArchiveDrawable = Theme.dialogs_unpinArchiveDrawable;
        if (dialogs_unpinArchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable2 = (LottieDrawable)dialogs_unpinArchiveDrawable;
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable2 }, "Arrow", "chats_archiveIcon"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable2 }, "Line", "chats_archiveIcon"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { dialogs_unpinArchiveDrawable }, null, "chats_archiveIcon"));
        }
        final Drawable dialogs_archiveDrawable = Theme.dialogs_archiveDrawable;
        if (dialogs_archiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable3 = (LottieDrawable)dialogs_archiveDrawable;
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable3 }, "Arrow", "chats_archiveBackground"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable3 }, "Box2", "chats_archiveIcon"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable3 }, "Box1", "chats_archiveIcon"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { dialogs_archiveDrawable }, null, "chats_archiveIcon"));
        }
        final Drawable dialogs_unarchiveDrawable = Theme.dialogs_unarchiveDrawable;
        if (dialogs_unarchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable4 = (LottieDrawable)dialogs_unarchiveDrawable;
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable4 }, "Arrow1", "chats_archiveIcon"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable4 }, "Arrow2", "chats_archivePinBackground"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable4 }, "Box2", "chats_archiveIcon"));
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, new LottieDrawable[] { lottieDrawable4 }, "Box1", "chats_archiveIcon"));
        }
        else {
            list.add(new ThemeDescription((View)this.listView, 0, new Class[] { DialogCell.class }, null, new Drawable[] { dialogs_unarchiveDrawable }, null, "chats_archiveIcon"));
        }
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "chats_menuBackground"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuName"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuPhone"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuPhoneCats"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuCloudBackgroundCats"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chat_serviceBackground"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuTopShadow"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerProfileCell.class }, null, null, null, "chats_menuTopShadowCats"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { DrawerProfileCell.class }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_menuTopBackgroundCats"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { DrawerProfileCell.class }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$DialogsActivity$kk8TCoSt0Vvk5iX4TZpsEfeGO6c, "chats_menuTopBackground"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { DrawerActionCell.class }, new String[] { "textView" }, null, null, null, "chats_menuItemIcon"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerActionCell.class }, new String[] { "textView" }, null, null, null, "chats_menuItemText"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerUserCell.class }, new String[] { "textView" }, null, null, null, "chats_menuItemText"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { DrawerUserCell.class }, new String[] { "checkBox" }, null, null, null, "chats_unreadCounterText"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { DrawerUserCell.class }, new String[] { "checkBox" }, null, null, null, "chats_unreadCounter"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { DrawerUserCell.class }, new String[] { "checkBox" }, null, null, null, "chats_menuBackground"));
        list.add(new ThemeDescription((View)this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { DrawerAddCell.class }, new String[] { "textView" }, null, null, null, "chats_menuItemIcon"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DrawerAddCell.class }, new String[] { "textView" }, null, null, null, "chats_menuItemText"));
        list.add(new ThemeDescription((View)this.sideMenu, 0, new Class[] { DividerCell.class }, Theme.dividerPaint, null, null, "divider"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_offlinePaint, null, null, "windowBackgroundWhiteGrayText3"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_onlinePaint, null, null, "windowBackgroundWhiteBlueText3"));
        list.add(new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { HashtagSearchCell.class }, null, null, null, "windowBackgroundWhiteBlackText"));
        list.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        final DialogsAdapter dialogsAdapter = this.dialogsAdapter;
        final View view = null;
        Object archiveHintCellPager;
        if (dialogsAdapter != null) {
            archiveHintCellPager = dialogsAdapter.getArchiveHintCellPager();
        }
        else {
            archiveHintCellPager = null;
        }
        list.add(new ThemeDescription((View)archiveHintCellPager, 0, new Class[] { ArchiveHintInnerCell.class }, new String[] { "imageView" }, null, null, null, "chats_nameMessage_threeLines"));
        final DialogsAdapter dialogsAdapter2 = this.dialogsAdapter;
        Object archiveHintCellPager2;
        if (dialogsAdapter2 != null) {
            archiveHintCellPager2 = dialogsAdapter2.getArchiveHintCellPager();
        }
        else {
            archiveHintCellPager2 = null;
        }
        list.add(new ThemeDescription((View)archiveHintCellPager2, 0, new Class[] { ArchiveHintInnerCell.class }, new String[] { "imageView2" }, null, null, null, "chats_unreadCounter"));
        final DialogsAdapter dialogsAdapter3 = this.dialogsAdapter;
        Object archiveHintCellPager3;
        if (dialogsAdapter3 != null) {
            archiveHintCellPager3 = dialogsAdapter3.getArchiveHintCellPager();
        }
        else {
            archiveHintCellPager3 = null;
        }
        list.add(new ThemeDescription((View)archiveHintCellPager3, 0, new Class[] { ArchiveHintInnerCell.class }, new String[] { "headerTextView" }, null, null, null, "chats_nameMessage_threeLines"));
        final DialogsAdapter dialogsAdapter4 = this.dialogsAdapter;
        Object archiveHintCellPager4;
        if (dialogsAdapter4 != null) {
            archiveHintCellPager4 = dialogsAdapter4.getArchiveHintCellPager();
        }
        else {
            archiveHintCellPager4 = null;
        }
        list.add(new ThemeDescription((View)archiveHintCellPager4, 0, new Class[] { ArchiveHintInnerCell.class }, new String[] { "messageTextView" }, null, null, null, "chats_message"));
        final DialogsAdapter dialogsAdapter5 = this.dialogsAdapter;
        Object archiveHintCellPager5;
        if (dialogsAdapter5 != null) {
            archiveHintCellPager5 = dialogsAdapter5.getArchiveHintCellPager();
        }
        else {
            archiveHintCellPager5 = null;
        }
        list.add(new ThemeDescription((View)archiveHintCellPager5, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefaultArchived"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"));
        list.add(new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGray"));
        final DialogsSearchAdapter dialogsSearchAdapter = this.dialogsSearchAdapter;
        Object innerListView;
        if (dialogsSearchAdapter != null) {
            innerListView = dialogsSearchAdapter.getInnerListView();
        }
        else {
            innerListView = null;
        }
        list.add(new ThemeDescription((View)innerListView, 0, new Class[] { HintDialogCell.class }, Theme.dialogs_countPaint, null, null, "chats_unreadCounter"));
        final DialogsSearchAdapter dialogsSearchAdapter2 = this.dialogsSearchAdapter;
        Object innerListView2;
        if (dialogsSearchAdapter2 != null) {
            innerListView2 = dialogsSearchAdapter2.getInnerListView();
        }
        else {
            innerListView2 = null;
        }
        list.add(new ThemeDescription((View)innerListView2, 0, new Class[] { HintDialogCell.class }, Theme.dialogs_countGrayPaint, null, null, "chats_unreadCounterMuted"));
        final DialogsSearchAdapter dialogsSearchAdapter3 = this.dialogsSearchAdapter;
        Object innerListView3;
        if (dialogsSearchAdapter3 != null) {
            innerListView3 = dialogsSearchAdapter3.getInnerListView();
        }
        else {
            innerListView3 = null;
        }
        list.add(new ThemeDescription((View)innerListView3, 0, new Class[] { HintDialogCell.class }, (Paint)Theme.dialogs_countTextPaint, null, null, "chats_unreadCounterText"));
        final DialogsSearchAdapter dialogsSearchAdapter4 = this.dialogsSearchAdapter;
        Object innerListView4;
        if (dialogsSearchAdapter4 != null) {
            innerListView4 = dialogsSearchAdapter4.getInnerListView();
        }
        else {
            innerListView4 = null;
        }
        list.add(new ThemeDescription((View)innerListView4, 0, new Class[] { HintDialogCell.class }, (Paint)Theme.dialogs_archiveTextPaint, null, null, "chats_archiveText"));
        final DialogsSearchAdapter dialogsSearchAdapter5 = this.dialogsSearchAdapter;
        Object innerListView5;
        if (dialogsSearchAdapter5 != null) {
            innerListView5 = dialogsSearchAdapter5.getInnerListView();
        }
        else {
            innerListView5 = null;
        }
        list.add(new ThemeDescription((View)innerListView5, 0, new Class[] { HintDialogCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"));
        final DialogsSearchAdapter dialogsSearchAdapter6 = this.dialogsSearchAdapter;
        Object innerListView6 = view;
        if (dialogsSearchAdapter6 != null) {
            innerListView6 = dialogsSearchAdapter6.getInnerListView();
        }
        list.add(new ThemeDescription((View)innerListView6, 0, new Class[] { HintDialogCell.class }, null, null, null, "chats_onlineCircle"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[] { FragmentContextView.class }, new String[] { "frameLayout" }, null, null, null, "inappPlayerBackground"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { FragmentContextView.class }, new String[] { "playButton" }, null, null, null, "inappPlayerPlayPause"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { FragmentContextView.class }, new String[] { "titleTextView" }, null, null, null, "inappPlayerTitle"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[] { FragmentContextView.class }, new String[] { "titleTextView" }, null, null, null, "inappPlayerPerformer"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { FragmentContextView.class }, new String[] { "closeButton" }, null, null, null, "inappPlayerClose"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[] { FragmentContextView.class }, new String[] { "frameLayout" }, null, null, null, "returnToCallBackground"));
        list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { FragmentContextView.class }, new String[] { "titleTextView" }, null, null, null, "returnToCallText"));
        int n = 0;
        while (true) {
            final UndoView[] undoView = this.undoView;
            if (n >= undoView.length) {
                break;
            }
            list.add(new ThemeDescription((View)undoView[n], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "undo_background"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "undoImageView" }, null, null, null, "undo_cancelColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "undoTextView" }, null, null, null, "undo_cancelColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "infoTextView" }, null, null, null, "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "subinfoTextView" }, null, null, null, "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "textPaint" }, null, null, null, "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "progressPaint" }, null, null, null, "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "info1", "undo_background"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "info2", "undo_background"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc12", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc11", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc10", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc9", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc8", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc7", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc6", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc5", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc4", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc3", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc2", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "luc1", "undo_infoColor"));
            list.add(new ThemeDescription((View)this.undoView[n], 0, new Class[] { UndoView.class }, new String[] { "leftImageView" }, "Oval", "undo_infoColor"));
            ++n;
        }
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBackgroundGray"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlack"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextLink"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLinkSelection"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue2"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue3"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue4"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextRed"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextRed2"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray2"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray3"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray4"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogIcon"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRedIcon"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextHint"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogInputField"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogInputFieldActivated"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareCheck"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareUnchecked"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareDisabled"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRadioBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRadioBackgroundChecked"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogProgressCircle"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogButton"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogButtonSelector"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogScrollGlow"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRoundCheckBox"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRoundCheckBoxCheck"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBadgeBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBadgeText"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLineProgress"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLineProgressBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogGrayLine"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialog_inlineProgressBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialog_inlineProgress"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchHint"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchIcon"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchText"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogFloatingButton"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogFloatingIcon"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "dialogShadowLine"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "key_sheet_scrollUp"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "key_sheet_other"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBar"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarSelector"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarTitle"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarTop"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarSubtitle"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarItems"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_background"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_time"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_progressBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "key_player_progressCachedBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_progress"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_placeholder"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_placeholderBackground"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_button"));
        list.add(new ThemeDescription(null, 0, null, null, null, null, "player_buttonActive"));
        return list.toArray(new ThemeDescription[0]);
    }
    
    public boolean isMainDialogList() {
        return this.delegate == null && this.searchString == null;
    }
    
    @Override
    public boolean onBackPressed() {
        final ActionBar actionBar = super.actionBar;
        if (actionBar != null && actionBar.isActionModeShowed()) {
            this.hideActionMode(true);
            return false;
        }
        final ChatActivityEnterView commentView = this.commentView;
        if (commentView != null && commentView.isPopupShowing()) {
            this.commentView.hidePopup(true);
            return false;
        }
        return super.onBackPressed();
    }
    
    @Override
    protected void onBecomeFullyHidden() {
        if (this.closeSearchFieldOnHide) {
            final ActionBar actionBar = super.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            final TLObject searchObject = this.searchObject;
            if (searchObject != null) {
                this.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, searchObject);
                this.searchObject = null;
            }
            this.closeSearchFieldOnHide = false;
        }
        final UndoView[] undoView = this.undoView;
        if (undoView[0] != null) {
            undoView[0].hide(true, 0);
        }
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!this.onlySelect) {
            final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
            if (floatingButtonContainer != null) {
                floatingButtonContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        final FrameLayout access$4900 = DialogsActivity.this.floatingButtonContainer;
                        float translationY;
                        if (DialogsActivity.this.floatingHidden) {
                            translationY = (float)AndroidUtilities.dp(100.0f);
                        }
                        else {
                            translationY = -DialogsActivity.this.additionalFloatingTranslation;
                        }
                        access$4900.setTranslationY(translationY);
                        DialogsActivity.this.floatingButtonContainer.setClickable(DialogsActivity.this.floatingHidden ^ true);
                        if (DialogsActivity.this.floatingButtonContainer != null) {
                            DialogsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                        }
                    }
                });
            }
        }
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        super.onDialogDismiss(dialog);
        final AlertDialog permissionDialog = this.permissionDialog;
        if (permissionDialog != null && dialog == permissionDialog && this.getParentActivity() != null) {
            this.askForPermissons(false);
        }
    }
    
    @Override
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
        if (!DialogsActivity.dialogsLoaded[super.currentAccount]) {
            this.getMessagesController().loadGlobalNotificationsSettings();
            this.getMessagesController().loadDialogs(this.folderId, 0, 100, true);
            this.getMessagesController().loadHintDialogs();
            this.getContactsController().checkInviteText();
            this.getDataQuery().loadRecents(2, false, true, false);
            this.getDataQuery().checkFeaturedStickers();
            DialogsActivity.dialogsLoaded[super.currentAccount] = true;
        }
        this.getMessagesController().loadPinnedDialogs(this.folderId, 0L, null);
        return true;
    }
    
    @Override
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
        final ChatActivityEnterView commentView = this.commentView;
        if (commentView != null) {
            commentView.onDestroy();
        }
        final UndoView[] undoView = this.undoView;
        if (undoView[0] != null) {
            undoView[0].hide(true, 0);
        }
        this.delegate = null;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final ChatActivityEnterView commentView = this.commentView;
        if (commentView != null) {
            commentView.onResume();
        }
        final UndoView[] undoView = this.undoView;
        if (undoView[0] != null) {
            undoView[0].hide(true, 0);
        }
    }
    
    @Override
    public void onRequestPermissionsResultFragment(int n, final String[] array, final int[] array2) {
        if (n == 1) {
            for (int i = 0; i < array.length; ++i) {
                if (array2.length > i) {
                    final String s = array[i];
                    n = -1;
                    final int hashCode = s.hashCode();
                    if (hashCode != 1365911975) {
                        if (hashCode == 1977429404) {
                            if (s.equals("android.permission.READ_CONTACTS")) {
                                n = 0;
                            }
                        }
                    }
                    else if (s.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                        n = 1;
                    }
                    if (n != 0) {
                        if (n == 1) {
                            if (array2[i] == 0) {
                                ImageLoader.getInstance().checkMediaPaths();
                            }
                        }
                    }
                    else if (array2[i] == 0) {
                        this.getContactsController().forceImportContacts();
                    }
                    else {
                        final SharedPreferences$Editor edit = MessagesController.getGlobalNotificationsSettings().edit();
                        this.askAboutContacts = false;
                        edit.putBoolean("askAboutContacts", false).commit();
                    }
                }
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final DialogsAdapter dialogsAdapter = this.dialogsAdapter;
        if (dialogsAdapter != null && !this.dialogsListFrozen) {
            dialogsAdapter.notifyDataSetChanged();
        }
        final ChatActivityEnterView commentView = this.commentView;
        if (commentView != null) {
            commentView.onResume();
        }
        final DialogsSearchAdapter dialogsSearchAdapter = this.dialogsSearchAdapter;
        if (dialogsSearchAdapter != null) {
            ((RecyclerView.Adapter)dialogsSearchAdapter).notifyDataSetChanged();
        }
        final boolean checkPermission = this.checkPermission;
        boolean b = false;
        if (checkPermission && !this.onlySelect && Build$VERSION.SDK_INT >= 23) {
            final Activity parentActivity = this.getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                final boolean b2 = parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0;
                if (parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                    b = true;
                }
                if (b2 || b) {
                    if (b2 && this.askAboutContacts && this.getUserConfig().syncContacts && parentActivity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                        this.showDialog(this.permissionDialog = AlertsCreator.createContactsPermissionDialog(parentActivity, new _$$Lambda$DialogsActivity$h_XfCIY8uwUxVhs5pPRQujZay_s(this)).create());
                    }
                    else if (b && parentActivity.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)parentActivity);
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setMessage(LocaleController.getString("PermissionStorage", 2131560420));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                        this.showDialog(this.permissionDialog = builder.create());
                    }
                    else {
                        this.askForPermissons(true);
                    }
                }
            }
        }
        else if (!this.onlySelect && XiaomiUtilities.isMIUI() && Build$VERSION.SDK_INT >= 19 && !XiaomiUtilities.isCustomPermissionGranted(10020)) {
            if (this.getParentActivity() == null) {
                return;
            }
            if (MessagesController.getGlobalNotificationsSettings().getBoolean("askedAboutMiuiLockscreen", false)) {
                return;
            }
            this.showDialog(new AlertDialog.Builder((Context)this.getParentActivity()).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionXiaomiLockscreen", 2131560421)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$DialogsActivity$ad_mqRrDwcRXwvhesRJKLOMcnHI(this)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131559152), (DialogInterface$OnClickListener)_$$Lambda$DialogsActivity$om5eIuKoD_TUjWJtmdVNYsc_Woc.INSTANCE).create());
        }
    }
    
    public void setDelegate(final DialogsActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setSearchString(final String searchString) {
        this.searchString = searchString;
    }
    
    public void setSideMenu(final RecyclerView sideMenu) {
        (this.sideMenu = sideMenu).setBackgroundColor(Theme.getColor("chats_menuBackground"));
        this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
    }
    
    private class ContentView extends SizeNotifierFrameLayout
    {
        private int inputFieldHeight;
        
        public ContentView(final Context context) {
            super(context);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            final int actionMasked = motionEvent.getActionMasked();
            boolean b = true;
            if (actionMasked == 0 || actionMasked == 1 || actionMasked == 3) {
                if (actionMasked == 0) {
                    final int firstVisibleItemPosition = DialogsActivity.this.layoutManager.findFirstVisibleItemPosition();
                    final DialogsActivity this$0 = DialogsActivity.this;
                    if (firstVisibleItemPosition > 1) {
                        b = false;
                    }
                    this$0.startedScrollAtTop = b;
                }
                else if (DialogsActivity.this.actionBar.isActionModeShowed()) {
                    DialogsActivity.this.allowMoving = true;
                }
                DialogsActivity.this.totalConsumedAmount = 0;
                DialogsActivity.this.allowScrollToHiddenView = false;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        
        @Override
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            final int childCount = this.getChildCount();
            Object tag;
            if (DialogsActivity.this.commentView != null) {
                tag = DialogsActivity.this.commentView.getTag();
            }
            else {
                tag = null;
            }
            int i = 0;
            int emojiPadding;
            if (tag != null && tag.equals(2) && this.getKeyboardHeight() <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                emojiPadding = DialogsActivity.this.commentView.getEmojiPadding();
            }
            else {
                emojiPadding = 0;
            }
            this.setBottomClip(emojiPadding);
            while (i < childCount) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                    final int measuredWidth = child.getMeasuredWidth();
                    final int measuredHeight = child.getMeasuredHeight();
                    int gravity;
                    if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                        gravity = 51;
                    }
                    final int n5 = gravity & 0x70;
                    final int n6 = gravity & 0x7 & 0x7;
                    int leftMargin = 0;
                    Label_0256: {
                        int n7;
                        int n8;
                        if (n6 != 1) {
                            if (n6 != 5) {
                                leftMargin = frameLayout$LayoutParams.leftMargin;
                                break Label_0256;
                            }
                            n7 = n3 - measuredWidth;
                            n8 = frameLayout$LayoutParams.rightMargin;
                        }
                        else {
                            n7 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                            n8 = frameLayout$LayoutParams.rightMargin;
                        }
                        leftMargin = n7 - n8;
                    }
                    int topMargin = 0;
                    Label_0358: {
                        int n9;
                        int n10;
                        if (n5 != 16) {
                            if (n5 == 48) {
                                topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                break Label_0358;
                            }
                            if (n5 != 80) {
                                topMargin = frameLayout$LayoutParams.topMargin;
                                break Label_0358;
                            }
                            n9 = n4 - emojiPadding - n2 - measuredHeight;
                            n10 = frameLayout$LayoutParams.bottomMargin;
                        }
                        else {
                            n9 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                            n10 = frameLayout$LayoutParams.bottomMargin;
                        }
                        topMargin = n9 - n10;
                    }
                    int bottom = topMargin;
                    if (DialogsActivity.this.commentView != null) {
                        bottom = topMargin;
                        if (DialogsActivity.this.commentView.isPopupView(child)) {
                            if (AndroidUtilities.isInMultiwindow) {
                                bottom = DialogsActivity.this.commentView.getTop() - child.getMeasuredHeight() + AndroidUtilities.dp(1.0f);
                            }
                            else {
                                bottom = DialogsActivity.this.commentView.getBottom();
                            }
                        }
                    }
                    child.layout(leftMargin, bottom, measuredWidth + leftMargin, measuredHeight + bottom);
                }
                ++i;
            }
            this.notifyHeightChanged();
        }
        
        protected void onMeasure(final int n, final int n2) {
            final int size = View$MeasureSpec.getSize(n);
            final int size2 = View$MeasureSpec.getSize(n2);
            this.setMeasuredDimension(size, size2);
            final int n3 = size2 - this.getPaddingTop();
            this.measureChildWithMargins((View)DialogsActivity.this.actionBar, n, 0, n2, 0);
            final int keyboardHeight = this.getKeyboardHeight();
            final int childCount = this.getChildCount();
            final ChatActivityEnterView access$100 = DialogsActivity.this.commentView;
            final int n4 = 0;
            int n5 = n3;
            int i = n4;
            if (access$100 != null) {
                this.measureChildWithMargins((View)DialogsActivity.this.commentView, n, 0, n2, 0);
                final Object tag = DialogsActivity.this.commentView.getTag();
                if (tag != null && tag.equals(2)) {
                    n5 = n3;
                    if (keyboardHeight <= AndroidUtilities.dp(20.0f)) {
                        n5 = n3;
                        if (!AndroidUtilities.isInMultiwindow) {
                            n5 = n3 - DialogsActivity.this.commentView.getEmojiPadding();
                        }
                    }
                    this.inputFieldHeight = DialogsActivity.this.commentView.getMeasuredHeight();
                    i = n4;
                }
                else {
                    this.inputFieldHeight = 0;
                    i = n4;
                    n5 = n3;
                }
            }
            while (i < childCount) {
                final View child = this.getChildAt(i);
                if (child != null && child.getVisibility() != 8 && child != DialogsActivity.this.commentView) {
                    if (child != DialogsActivity.this.actionBar) {
                        if (child != DialogsActivity.this.listView && child != DialogsActivity.this.progressView && child != DialogsActivity.this.searchEmptyView) {
                            if (DialogsActivity.this.commentView != null && DialogsActivity.this.commentView.isPopupView(child)) {
                                if (AndroidUtilities.isInMultiwindow) {
                                    if (AndroidUtilities.isTablet()) {
                                        child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), n5 - this.inputFieldHeight - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                                    }
                                    else {
                                        child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n5 - this.inputFieldHeight - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                                    }
                                }
                                else {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                                }
                            }
                            else {
                                this.measureChildWithMargins(child, n, 0, n2, 0);
                            }
                        }
                        else {
                            child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), n5 - this.inputFieldHeight + AndroidUtilities.dp(2.0f)), 1073741824));
                        }
                    }
                }
                ++i;
            }
        }
    }
    
    public interface DialogsActivityDelegate
    {
        void didSelectDialogs(final DialogsActivity p0, final ArrayList<Long> p1, final CharSequence p2, final boolean p3);
    }
    
    class SwipeController extends Callback
    {
        private RectF buttonInstance;
        private ViewHolder currentItemViewHolder;
        private boolean swipeFolderBack;
        private boolean swipingFolder;
        
        @Override
        public int convertToAbsoluteDirection(final int n, final int n2) {
            if (this.swipeFolderBack) {
                return 0;
            }
            return super.convertToAbsoluteDirection(n, n2);
        }
        
        @Override
        public long getAnimationDuration(final RecyclerView recyclerView, final int n, final float n2, final float n3) {
            if (n == 4) {
                return 200L;
            }
            if (n == 8 && DialogsActivity.this.movingView != null) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsActivity$SwipeController$P5v0ux1s9L8VHZMGfvGmr58IqXA((View)DialogsActivity.this.movingView), ((RecyclerView.ItemAnimator)DialogsActivity.this.dialogsItemAnimator).getMoveDuration());
                DialogsActivity.this.movingView = null;
            }
            return super.getAnimationDuration(recyclerView, n, n2, n3);
        }
        
        @Override
        public int getMovementFlags(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            if (!DialogsActivity.this.waitingForDialogsAnimationEnd()) {
                if (DialogsActivity.this.parentLayout == null || !DialogsActivity.this.parentLayout.isInPreviewMode()) {
                    if (this.swipingFolder && this.swipeFolderBack) {
                        this.swipingFolder = false;
                        return 0;
                    }
                    if (!DialogsActivity.this.onlySelect && DialogsActivity.this.dialogsType == 0 && DialogsActivity.this.slidingView == null && recyclerView.getAdapter() == DialogsActivity.this.dialogsAdapter) {
                        final View itemView = viewHolder.itemView;
                        if (itemView instanceof DialogCell) {
                            final DialogCell dialogCell = (DialogCell)itemView;
                            final long dialogId = dialogCell.getDialogId();
                            if (DialogsActivity.this.actionBar.isActionModeShowed()) {
                                final TLRPC.Dialog dialog = (TLRPC.Dialog)BaseFragment.this.getMessagesController().dialogs_dict.get(dialogId);
                                if (DialogsActivity.this.allowMoving && dialog != null && dialog.pinned && !DialogObject.isFolderDialogId(dialogId)) {
                                    DialogsActivity.this.movingView = (DialogCell)viewHolder.itemView;
                                    DialogsActivity.this.movingView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                    return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
                                }
                                return 0;
                            }
                            else if (DialogsActivity.this.allowSwipeDuringCurrentTouch && dialogId != DialogsActivity.this.getUserConfig().clientUserId && dialogId != 777000L) {
                                if (!BaseFragment.this.getMessagesController().isProxyDialog(dialogId, false)) {
                                    this.swipeFolderBack = false;
                                    this.swipingFolder = (SharedConfig.archiveHidden && DialogObject.isFolderDialogId(dialogCell.getDialogId()));
                                    dialogCell.setSliding(true);
                                    return ItemTouchHelper.Callback.makeMovementFlags(0, 4);
                                }
                            }
                        }
                    }
                }
            }
            return 0;
        }
        
        @Override
        public float getSwipeEscapeVelocity(final float n) {
            return 3500.0f;
        }
        
        @Override
        public float getSwipeThreshold(final ViewHolder viewHolder) {
            return 0.3f;
        }
        
        @Override
        public float getSwipeVelocityThreshold(final float n) {
            return Float.MAX_VALUE;
        }
        
        @Override
        public boolean onMove(final RecyclerView recyclerView, final ViewHolder viewHolder, final ViewHolder viewHolder2) {
            final View itemView = viewHolder2.itemView;
            if (!(itemView instanceof DialogCell)) {
                return false;
            }
            final long dialogId = ((DialogCell)itemView).getDialogId();
            final TLRPC.Dialog dialog = (TLRPC.Dialog)BaseFragment.this.getMessagesController().dialogs_dict.get(dialogId);
            if (dialog != null && dialog.pinned && !DialogObject.isFolderDialogId(dialogId)) {
                DialogsActivity.this.dialogsAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
                DialogsActivity.this.updateDialogIndices();
                DialogsActivity.this.movingWas = true;
                return true;
            }
            return false;
        }
        
        @Override
        public void onSelectedChanged(final ViewHolder viewHolder, final int n) {
            if (viewHolder != null) {
                DialogsActivity.this.listView.hideSelector();
            }
            super.onSelectedChanged(viewHolder, n);
        }
        
        @Override
        public void onSwiped(final ViewHolder viewHolder, int adapterPosition) {
            if (viewHolder != null) {
                final DialogCell dialogCell = (DialogCell)viewHolder.itemView;
                if (DialogObject.isFolderDialogId(dialogCell.getDialogId())) {
                    SharedConfig.toggleArchiveHidden();
                    if (SharedConfig.archiveHidden) {
                        DialogsActivity.this.waitingForScrollFinished = true;
                        DialogsActivity.this.listView.smoothScrollBy(0, dialogCell.getMeasuredHeight() + dialogCell.getTop(), (Interpolator)CubicBezierInterpolator.EASE_OUT);
                        DialogsActivity.this.getUndoView().showWithAction(0L, 6, null, null);
                    }
                    return;
                }
                DialogsActivity.this.slidingView = dialogCell;
                adapterPosition = viewHolder.getAdapterPosition();
                final _$$Lambda$DialogsActivity$SwipeController$p1WvioWe4QIXpl_h9JruXCBarGg finishRunnable = new _$$Lambda$DialogsActivity$SwipeController$p1WvioWe4QIXpl_h9JruXCBarGg(this, DialogsActivity.this.dialogsAdapter.fixPosition(adapterPosition), DialogsActivity.this.dialogsAdapter.getItemCount(), adapterPosition);
                DialogsActivity.this.setDialogsListFrozen(true);
                if (Utilities.random.nextInt(1000) == 1) {
                    if (DialogsActivity.this.pacmanAnimation == null) {
                        final DialogsActivity this$0 = DialogsActivity.this;
                        this$0.pacmanAnimation = new PacmanAnimation((View)this$0.listView);
                    }
                    DialogsActivity.this.pacmanAnimation.setFinishRunnable(finishRunnable);
                    DialogsActivity.this.pacmanAnimation.start();
                }
                else {
                    finishRunnable.run();
                }
            }
            else {
                DialogsActivity.this.slidingView = null;
            }
        }
    }
}
